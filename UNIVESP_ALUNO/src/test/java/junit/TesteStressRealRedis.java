package junit;



import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import controle.arquitetura.RedisService;
import negocio.comuns.arquitetura.UsuarioVO;
import java.util.concurrent.TimeUnit;

public class TesteStressRealRedis {

    // ==========================================
    // 1. MOCKS (Apenas o Banco de Dados continua Fake)
    // ==========================================

    // VO Simples para o teste
    static class CidadeVO implements Cloneable {
        Integer codigo;
        String nome;
        public CidadeVO(Integer codigo, String nome) { this.codigo = codigo; this.nome = nome; }
        @Override public Object clone() { return new CidadeVO(this.codigo, this.nome); }
    }

    // Mock do Banco (Lento)
    static class MockFacadeReal {
        public static AtomicInteger totalAcessosBanco = new AtomicInteger(0);

        public CidadeVO consultarCidade(Integer codigo) {
            int n = totalAcessosBanco.incrementAndGet();
            System.out.println("   [BANCO DE DADOS] SELECT * FROM CIDADE... (Acesso Real #" + n + ")");
            
            // SIMULAÇÃO DE LENTIDÃO DO BANCO (500ms)
            try { Thread.sleep(500); } catch (InterruptedException e) {}

            return new CidadeVO(codigo, "Rio de Janeiro Real");
        }
    }

    // ==========================================
    // 2. SUA LÓGICA (AplicacaoControle Modificada para Teste)
    // ==========================================
    static class AplicacaoControleReal {
        
        private RedisService redisService; // AGORA É O REAL!
        private MockFacadeReal facade = new MockFacadeReal();
        private final Object lockBuscaCidade = new Object();

        public void setRedisService(RedisService redis) { this.redisService = redis; }

        public CidadeVO getCidadeVO(Integer codigoCidade) {
            String chaveRedis = "app:cidade:" + codigoCidade;
            String threadName = Thread.currentThread().getName(); 

            // --- CHECK 1: Tenta Redis Real ---
            try {
                CidadeVO cidadeRedis = redisService.getObjeto(chaveRedis, CidadeVO.class);
                if (cidadeRedis != null) return cidadeRedis; 
            } catch (Exception e) { System.err.println("Erro Redis 1: " + e.getMessage()); }
    
            // --- ZONA DE CONGESTIONAMENTO (Lock) ---
            synchronized (lockBuscaCidade) {
                
                // --- CHECK 2: Double Check Real ---
                try {
                    CidadeVO cidadeRedis = redisService.getObjeto(chaveRedis, CidadeVO.class);
                    if (cidadeRedis != null) {
                        System.out.println("   [CHECK 2 - SUCESSO] " + threadName + " pegou do Redis!");
                        return cidadeRedis; 
                    }
                } catch (Exception e) {}
    
                // --- BUSCA NO BANCO ---
                System.out.println("   [LOCK] " + threadName + " indo no Banco...");
                CidadeVO cidadeBanco = facade.consultarCidade(codigoCidade);
    
                if (cidadeBanco != null) {
                    // Grava no Redis Real (TTL 1 hora)
                    redisService.setObjeto(chaveRedis, cidadeBanco, 1, TimeUnit.HOURS);
                    return (CidadeVO) cidadeBanco.clone();
                }
            }
            return null;
        }
    }

    // ==========================================
    // 3. O TESTE
    // ==========================================
    public static void main(String[] args) throws Exception {
        System.out.println("--- INICIANDO STRESS TEST NO REDIS REAL (173.249.30.27) ---");

        // 1. CONFIGURAÇÃO REAL DO REDIS
        RedisStandaloneConfiguration configRede = new RedisStandaloneConfiguration("173.249.30.27", 6379);
        JedisConnectionFactory factory = new JedisConnectionFactory(configRede);
        factory.afterPropertiesSet();

        StringRedisTemplate redisTemplate = new StringRedisTemplate();
        redisTemplate.setConnectionFactory(factory);
        redisTemplate.afterPropertiesSet();

        Gson gson = new GsonBuilder().create();
        
        // Serviço Real
        RedisService redisService = new RedisService();
        injetarDependencia(redisService, "redisTemplate", redisTemplate);
        injetarDependencia(redisService, "gson", gson);

        // Limpa a chave antes de começar para garantir que vai no banco na 1ª vez
        System.out.println("Limpando chave antiga...");
        redisService.remover("app:cidade:21");

        // 2. PREPARA APLICAÇÃO
        AplicacaoControleReal controle = new AplicacaoControleReal();
        controle.setRedisService(redisService);

        // 3. EXECUÇÃO PARALELA
        int qtdAlunos = 50; 
        ExecutorService executor = Executors.newFixedThreadPool(qtdAlunos);
        CountDownLatch largada = new CountDownLatch(1);
        CountDownLatch chegada = new CountDownLatch(qtdAlunos);

        for (int i = 0; i < qtdAlunos; i++) {
            executor.submit(() -> {
                try {
                    largada.await(); // Espera
                    controle.getCidadeVO(21); // Busca
                } catch (Exception e) { e.printStackTrace(); } 
                finally { chegada.countDown(); }
            });
        }

        Thread.sleep(1000); 
        System.out.println(">>> VAI! DISPARANDO 50 THREADS NO REDIS REAL...");
        long inicio = System.currentTimeMillis();
        largada.countDown(); 
        chegada.await();     
        long fim = System.currentTimeMillis();
        
        executor.shutdown();

        // 4. RESULTADO
        int acessos = MockFacadeReal.totalAcessosBanco.get();
        System.out.println("\n--------------------------------------------------");
        System.out.println("Tempo Total: " + (fim - inicio) + "ms");
        System.out.println("Consultas REAIS no Banco: " + acessos);
        
        if (acessos == 1) {
            System.out.println("[SUCESSO] O Bloqueio funcionou com Redis Real!");
            System.out.println("AGORA PODE IR NO SHELL E DIGITAR: GET app:cidade:21");
        } else {
            System.out.println("[FALHA] O banco foi sobrecarregado.");
        }
    }

    private static void injetarDependencia(Object target, String fieldName, Object value) {
        try {
            java.lang.reflect.Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) { throw new RuntimeException(e); }
    }
}