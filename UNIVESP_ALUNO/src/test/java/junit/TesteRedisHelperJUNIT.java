package junit;



import org.junit.Test;

import com.google.gson.Gson;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import controle.arquitetura.RedisConfig;
import controle.arquitetura.RedisService;
import negocio.comuns.basico.CidadeVO;

public class TesteRedisHelperJUNIT {

    @Test
    public void prepararCidade9999NoRedis() {
        // 1) Monta RedisConfig "na mão"
        RedisConfig config = new RedisConfig();
        JedisConnectionFactory cf = config.jedisConnectionFactory();
        cf.afterPropertiesSet();

        StringRedisTemplate template = config.redisTemplate();
        template.setConnectionFactory(cf);
        template.afterPropertiesSet();

        // 2) Monta RedisService manualmente
        RedisService redisService = new RedisService();
        setPrivateField(redisService, "redisTemplate", template);
        setPrivateField(redisService, "gson", new Gson());

        // 3) Grava cidade 9999
        CidadeVO cidade = new CidadeVO();
        cidade.setCodigo(9999);
        cidade.setNome("Cidade Teste 9999");

        String chave = "app:cidade:" + cidade.getCodigo();
        redisService.setObjeto(chave, cidade);

        System.out.println("Gravou no Redis a chave " + chave);
    }

    // Helper para injetar campo privado
    private void setPrivateField(Object target, String fieldName, Object value) {
        try {
            java.lang.reflect.Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
