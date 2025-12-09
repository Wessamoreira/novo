package junit;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import controle.arquitetura.RedisConfig;
import controle.arquitetura.RedisService;
import negocio.comuns.basico.CidadeVO;

public class TesteConexaoRedis {

    @Test
    public void testeRealConexaoGravacaoLeitura() throws Exception {
        System.out.println("--- INICIANDO TESTE REAL DE CONEXÃO REDIS ---");

        // 1. Configuração Manual (Simulando o que o Spring faz)
        RedisConfig config = new RedisConfig();
        
        // Inicia a fábrica de conexões (vai tentar conectar no IP 173.249.30.27)
        System.out.println("Tentando conectar em 173.249.30.27...");
        JedisConnectionFactory factory = config.jedisConnectionFactory();
        factory.afterPropertiesSet(); // Inicializa o Jedis

        // Cria o Template do Spring
        StringRedisTemplate redisTemplate = new StringRedisTemplate();
        redisTemplate.setConnectionFactory(factory);
        redisTemplate.afterPropertiesSet();

        // Configura o Gson
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

        // 2. Prepara o nosso Service
        RedisService redisService = new RedisService();
        
        // Injeta as dependências manualmente (pois não estamos rodando o servidor web completo)
        setPrivateField(redisService, "redisTemplate", redisTemplate);
        setPrivateField(redisService, "gson", gson);

        // 3. DADOS PARA O TESTE
        String chaveTeste = "teste:integracao:cidade:1";
        
        CidadeVO cidadeTeste = new CidadeVO();
        cidadeTeste.setCodigo(1);
        cidadeTeste.setNome("Goiânia - Teste Real");
        // Preencha outros campos obrigatórios se houver

        // 4. AÇÃO: GRAVAR
        System.out.println("Gravando objeto no Redis...");
        redisService.setObjeto(chaveTeste, cidadeTeste, 5, TimeUnit.MINUTES);

        // 5. AÇÃO: LER
        System.out.println("Lendo objeto do Redis...");
        CidadeVO cidadeRetornada = redisService.getObjeto(chaveTeste, CidadeVO.class);

        // 6. VALIDAÇÃO
        System.out.println("Validando dados...");
        assertNotNull("O objeto retornado não deveria ser nulo", cidadeRetornada);
        assertEquals("O ID da cidade deve ser igual", cidadeTeste.getCodigo(), cidadeRetornada.getCodigo());
        assertEquals("O Nome da cidade deve ser igual", cidadeTeste.getNome(), cidadeRetornada.getNome());

        System.out.println("--- SUCESSO! O REDIS ESTÁ FUNCIONANDO ---");
        System.out.println("Objeto recuperado: " + cidadeRetornada.getNome());
    }

    // Método auxiliar para injetar dependências privadas sem Spring
    private void setPrivateField(Object target, String fieldName, Object value) {
        try {
            java.lang.reflect.Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao injetar campo: " + fieldName, e);
        }
    }
    
    
    @Test
    public void testeDeveQuebrarComPortaErrada() throws Exception {
        System.out.println("--- INICIANDO TESTE DE FALHA (DEVE DAR ERRO) ---");

        // 1. Configuração COM PORTA ERRADA (1234 em vez de 6379)
        RedisConfig config = new RedisConfig();
        
        // Estamos forçando o erro aqui:
        System.out.println("Tentando conectar na porta errada (1234)...");
        
        // Criamos a configuração manual para forçar a porta errada
        org.springframework.data.redis.connection.RedisStandaloneConfiguration configRede 
            = new org.springframework.data.redis.connection.RedisStandaloneConfiguration("173.249.30.27", 1234);
        
        JedisConnectionFactory factory = new JedisConnectionFactory(configRede);
        factory.afterPropertiesSet();

        StringRedisTemplate redisTemplate = new StringRedisTemplate();
        redisTemplate.setConnectionFactory(factory);
        redisTemplate.afterPropertiesSet();

        // Configura o Gson
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

        // 2. Prepara o Service
        RedisService redisService = new RedisService();
        setPrivateField(redisService, "redisTemplate", redisTemplate);
        setPrivateField(redisService, "gson", gson);

        // 3. AÇÃO: Tentar gravar (AQUI DEVE EXPLODIR O ERRO)
        System.out.println("Tentando enviar comando para porta inexistente...");
        
        try {
            redisService.setObjeto("teste:falha", "Isso nao deve salvar");
            
            // 4. Se passar daqui, algo está muito errado (o teste falhou em falhar)
            fail("ERRO GRAVE: O teste conectou numa porta que não existe! O teste anterior pode ser falso.");
            
        } catch (Exception e) {
            // 5. Se cair aqui, EXCELENTE!
            System.out.println("SUCESSO! Ocorreu o erro esperado: " + e.getMessage());
            // Re-lançamos o erro para a barra ficar VERMELHA no Eclipse, como você pediu.
            throw e; 
        }
    }
    
    
    
    
//    docker exec -it trusting_cori redis-cli -h 173.249.30.27
//    
//    SET app:cidade:9999 "{\"codigo\":9999,\"nome\":\"CIDADE_TESTE_MANUAL\"}"
    
    
    @Test
    public void testeLerCidadeCriadaManualmente() {
        System.out.println("--- TESTE DE LEITURA MANUAL ---");

        // 1. Configurações (Mesmas do RedisConfig)
        RedisConfig config = new RedisConfig();
        JedisConnectionFactory factory = config.jedisConnectionFactory(); // Garanta que está com o IP 173...
        factory.afterPropertiesSet();

        StringRedisTemplate redisTemplate = new StringRedisTemplate();
        redisTemplate.setConnectionFactory(factory);
        redisTemplate.afterPropertiesSet();

        Gson gson = new GsonBuilder().create();
        RedisService redisService = new RedisService();
        setPrivateField(redisService, "redisTemplate", redisTemplate);
        setPrivateField(redisService, "gson", gson);

        // 2. TENTA LER O DADO QUE VOCÊ CRIOU NO DOCKER (ID 9999)
        String chave = "app:cidade:9999";
        System.out.println("Buscando chave: " + chave);

        CidadeVO cidade = redisService.getObjeto(chave, CidadeVO.class);

        // 3. RESULTADO
        if (cidade != null) {
            System.out.println(">>> SUCESSO TOTAL! <<<");
            System.out.println("Nome recuperado do Redis: " + cidade.getNome());
            
            // Validação
            assertEquals("CIDADE_TESTE_MANUAL", cidade.getNome());
        } else {
            System.out.println(">>> FALHA <<<");
            System.out.println("O Java conectou, mas não achou a chave. Verifique se o IP do Java é o mesmo do Docker.");
            fail("Retornou nulo");
        }
    }
}