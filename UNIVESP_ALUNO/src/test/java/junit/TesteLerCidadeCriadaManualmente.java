package junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import controle.arquitetura.RedisConfig;
import controle.arquitetura.RedisService;
import negocio.comuns.basico.CidadeVO;


// forma rapidao de executar este , no shell faca um set da cidade e do codigo dela
// comandos para criar a cidade manualmente no redis


//  redis-cli -h 173.249.30.27 -p 6379
//  SET app:cidade:9999 '{"codigo":9999,"nome":"Cidade Teste"}'





public class TesteLerCidadeCriadaManualmente {
	
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
	public void testeLerCidadeCriadaManualmente() {
	    System.out.println("--- TESTE DE LEITURA MANUAL ---");

	    // 1. Configurações (Mesmas do RedisConfig)
	    RedisConfig config = new RedisConfig();
	    JedisConnectionFactory factory = config.jedisConnectionFactory(); // Garanta que está com o IP correto na redis config
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
	     // CORRETO (Vai dar barra VERDE)
	        assertEquals("Cidade Teste", cidade.getNome());
	    } else {
	        System.out.println(">>> FALHA <<<");
	        System.out.println("O Java conectou, mas não achou a chave. Verifique se o IP do Java é o mesmo do Docker.");
	        fail("Retornou nulo");
	    }
	}
	
	
	@Test
	public void testeCicloCompletoGravarELer() throws Exception {
	    System.out.println("--- INICIANDO TESTE DE CICLO COMPLETO (JAVA -> REDIS -> JAVA) ---");

	    // 1. Configuração (IP REAL)
	    String ipRedis = "173.249.30.27"; 
	    int portaRedis = 6379;
	    
	    System.out.println("1. Configurando conexão com " + ipRedis + ":" + portaRedis);

	    org.springframework.data.redis.connection.RedisStandaloneConfiguration configRede 
	        = new org.springframework.data.redis.connection.RedisStandaloneConfiguration(ipRedis, portaRedis);
	    
	    JedisConnectionFactory factory = new JedisConnectionFactory(configRede);
	    factory.afterPropertiesSet();

	    StringRedisTemplate redisTemplate = new StringRedisTemplate();
	    redisTemplate.setConnectionFactory(factory);
	    redisTemplate.afterPropertiesSet();

	    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	    RedisService redisService = new RedisService();
	    setPrivateField(redisService, "redisTemplate", redisTemplate);
	    setPrivateField(redisService, "gson", gson);

	    // 2. PREPARAR DADO
	    String chaveTeste = "teste:java:automatico";
	    CidadeVO cidadeParaGravar = new CidadeVO();
	    cidadeParaGravar.setCodigo(777);
	    cidadeParaGravar.setNome("Goiania Teste Java");
	    
	    // 3. GRAVAR
	    System.out.println("2. Gravando objeto no Redis...");
	    redisService.setObjeto(chaveTeste, cidadeParaGravar, 10, TimeUnit.MINUTES);
	    System.out.println("   >>> Gravado com sucesso!");

	    // 4. LER
	    System.out.println("3. Tentando ler de volta...");
	    CidadeVO cidadeLida = redisService.getObjeto(chaveTeste, CidadeVO.class);

	    // 5. VALIDAR
	    if (cidadeLida != null) {
	        System.out.println("4. Objeto recuperado!");
	        // AQUI ESTAVA O ERRO, AGORA ESTÁ CORRIGIDO COM getNome():
	        System.out.println("   Nome original: " + cidadeParaGravar.getNome());
	        System.out.println("   Nome lido do Redis: " + cidadeLida.getNome());
	        
	        assertEquals(cidadeParaGravar.getNome(), cidadeLida.getNome());
	        System.out.println("--- SUCESSO! SEU SISTEMA ESTÁ 100% OPERACIONAL ---");
	    } else {
	        System.out.println("--- FALHA: O objeto voltou NULO ---");
	        fail("O Java conectou e gravou, mas não conseguiu ler de volta.");
	    }
	}
}
