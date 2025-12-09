package junit;

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class TesteStressCidade {

    // ==========================================
    // 1. MOCKS (Simulando o Ambiente)
    // ==========================================

    // VO da Cidade
    static class CidadeVO implements Cloneable {
        Integer codigo;
        String nome;
        
        public CidadeVO(Integer codigo, String nome) { 
            this.codigo = codigo; 
            this.nome = nome; 
        }
        
        @Override
        public Object clone() {
            return new CidadeVO(this.codigo, this.nome);
        }
        
        @Override
        public String toString() { 
            return "CidadeVO { id=" + codigo + ", nome='" + nome + "' }"; 
        }
    }

    static class UsuarioVO {} 

    static class Uteis {
        public static boolean isAtributoPreenchido(Object obj) { return obj != null; }
    }

    // Mock do Redis
    static class MockRedisService {
        private Map<String, Object> redisStorage = new ConcurrentHashMap<>();

        public <T> T getObjeto(String key, Class<T> clazz) {
            // Simula latência de rede minúscula (1ms)
            try { Thread.sleep(1); } catch (InterruptedException e) {}
            return (T) redisStorage.get(key);
        }

        public void setObjeto(String key, Object value, long time, TimeUnit unit) {
            redisStorage.put(key, value);
            System.out.println("[REDIS WRITE] Gravando Objeto: " + value + " na chave: " + key);
        }
    }

    static class MockFacadeFactory {
        public MockCidadeFacade getCidadeFacade() { return new MockCidadeFacade(); }
    }

    // Mock do Banco (Lento)
    static class MockCidadeFacade {
        public static AtomicInteger totalAcessosBanco = new AtomicInteger(0);

        public CidadeVO consultarPorChavePrimariaUnica(Integer codigo, boolean lock, UsuarioVO usuario) {
            int n = totalAcessosBanco.incrementAndGet();
            
            System.out.println("[BANCO DE DADOS] SELECT * FROM CIDADE WHERE ID = " + codigo + " (Acesso Real #" + n + ")");
            
            // SIMULAÇÃO DE LENTIDÃO REAL (200ms)
            // É aqui que as outras threads vão ficar presas na fila do synchronized
            try { Thread.sleep(200); } catch (InterruptedException e) {}

            if (codigo == 21) {
                return new CidadeVO(21, "Rio de Janeiro");
            }
            return new CidadeVO(codigo, "Cidade Desconhecida");
        }
    }

    // ==========================================
    // 2. SUA LÓGICA (AplicacaoControle)
    // ==========================================
    static class AplicacaoControle {
        
        private MockRedisService redisService = new MockRedisService();
        private MockFacadeFactory facadeFactory = new MockFacadeFactory();
        
        // O CADEADO (Lock)
        private final Object lockBuscaCidade = new Object();

        public MockFacadeFactory getFacadeFactory() { return facadeFactory; }

        public CidadeVO getCidadeVO(Integer codigoCidade, UsuarioVO usuario) throws Exception {
            
            if (!Uteis.isAtributoPreenchido(codigoCidade)) return null;
    
            String chaveRedis = "app:cidade:" + codigoCidade;
            String threadName = Thread.currentThread().getName(); 
    
            // --- CHECK 1: Tenta Redis (Sem Lock) ---
            try {
                CidadeVO cidadeRedis = redisService.getObjeto(chaveRedis, CidadeVO.class);
                if (cidadeRedis != null) {
                    return cidadeRedis; 
                }
            } catch (Exception e) {}
    
            // --- ZONA DE CONGESTIONAMENTO (Lock) ---
            synchronized (lockBuscaCidade) {
                
                // --- CHECK 2: O Pulo do Gato (Double Check) ---
                try {
                    CidadeVO cidadeRedis = redisService.getObjeto(chaveRedis, CidadeVO.class);
                    if (cidadeRedis != null) {
                        // AQUI ESTA A PROVA QUE VOCE PEDIU
                        System.out.println("[CHECK 2 - SUCESSO] " + threadName + " entrou no Lock mas JA ACHOU no Redis: " + cidadeRedis.nome);
                        return cidadeRedis; 
                    }
                } catch (Exception e) {}
    
                // --- BUSCA NO BANCO (Só o primeiro da fila chega aqui) ---
                System.out.println("[LOCK] " + threadName + " nao achou nada. Indo para o Banco...");
                
                CidadeVO cidadeBanco = getFacadeFactory()
                        .getCidadeFacade()
                        .consultarPorChavePrimariaUnica(codigoCidade, false, usuario);
    
                if (cidadeBanco != null) {
                    try {
                        redisService.setObjeto(chaveRedis, cidadeBanco, 24, TimeUnit.HOURS);
                    } catch (Exception e) {}
                    return (CidadeVO) cidadeBanco.clone();
                }
            }
            return null;
        }
    }

    // ==========================================
    // 3. O TESTE (Simulando a Manada)
    // ==========================================
    public static void main(String[] args) throws InterruptedException {
        System.out.println("--- INICIANDO SIMULACAO: 50 Alunos buscando RIO DE JANEIRO (ID 21) ---");

        AplicacaoControle controle = new AplicacaoControle();
        int qtdAlunos = 50; 
        
        ExecutorService executor = Executors.newFixedThreadPool(qtdAlunos);
        CountDownLatch largada = new CountDownLatch(1);
        CountDownLatch chegada = new CountDownLatch(qtdAlunos);

        for (int i = 0; i < qtdAlunos; i++) {
            executor.submit(() -> {
                try {
                    largada.await(); // Espera o tiro de largada
                    controle.getCidadeVO(21, new UsuarioVO()); // Busca Rio de Janeiro
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    chegada.countDown();
                }
            });
        }

        Thread.sleep(500); // Prepara as threads
        System.out.println("[SISTEMA] VAI! Todos buscando ao mesmo tempo...");
        
        long inicio = System.currentTimeMillis();
        largada.countDown(); // Libera a manada
        chegada.await();     // Espera todos terminarem
        long fim = System.currentTimeMillis();
        
        executor.shutdown();

        // VALIDAÇÃO FINAL
        int acessos = MockCidadeFacade.totalAcessosBanco.get();
        System.out.println("\n--------------------------------------------------");
        System.out.println("RESULTADO DA PROVA REAL:");
        System.out.println("Tempo Total: " + (fim - inicio) + "ms");
        System.out.println("Requisicoes Totais: " + qtdAlunos);
        System.out.println("Consultas REAIS no Banco: " + acessos);
        
        if (acessos == 1) {
            System.out.println("[SUCESSO] Apenas 1 thread foi no banco. As outras pegaram do Redis no Check 2.");
        } else {
            System.out.println("[FALHA] O banco foi sobrecarregado.");
        }
    }
}