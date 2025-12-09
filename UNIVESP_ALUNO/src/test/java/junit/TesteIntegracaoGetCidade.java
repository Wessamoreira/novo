package junit;

import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import controle.arquitetura.AplicacaoControle;
import controle.arquitetura.RedisService;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.CidadeVO;
import negocio.facade.jdbc.arquitetura.FacadeFactory;
import java.util.concurrent.TimeUnit;

public class TesteIntegracaoGetCidade {

    @Test
    public void testeBuscarCidade9999NaAplicacaoControle() throws Exception {
        System.out.println("--- INICIANDO TESTE INTEGRAÇÃO ---");

        // CONFIGURAÇÃO DO REDIS
        String ipRedis = "173.249.30.27"; 
        int portaRedis = 6379;

        RedisStandaloneConfiguration configRede = new RedisStandaloneConfiguration(ipRedis, portaRedis);
        JedisConnectionFactory factory = new JedisConnectionFactory(configRede);
        factory.afterPropertiesSet();

        StringRedisTemplate redisTemplate = new StringRedisTemplate();
        redisTemplate.setConnectionFactory(factory);
        redisTemplate.afterPropertiesSet();

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

        RedisService redisService = new RedisService();
        setPrivateField(redisService, "redisTemplate", redisTemplate);
        setPrivateField(redisService, "gson", gson);

        // ===================================================================
        // 1. PREPARAÇÃO (SETUP) - GARANTIR QUE O DADO EXISTE E ESTÁ LIMPO
        // ===================================================================
        System.out.println("1. Preparando ambiente (Gravando cidade limpa no Redis)...");
        CidadeVO cidadeLimpa = new CidadeVO();
        cidadeLimpa.setCodigo(9999);
        cidadeLimpa.setNome("Cidade Teste"); // Nome sem espaços extras
        
        // Força a gravação antes de testar
        redisService.setObjeto("app:cidade:9999", cidadeLimpa, 1, TimeUnit.HOURS);

        // ===================================================================
        // 2. CONFIGURAR APLICAÇÃO
        // ===================================================================
        AplicacaoControle appControle = new AplicacaoControle();
        setPrivateField(appControle, "redisService", redisService);
        
        // Mock do Banco (para garantir que não vai lá)
        FacadeFactory facadeMock = Mockito.mock(FacadeFactory.class);
        setPrivateField(appControle, "facadeFactory", facadeMock);

        // ===================================================================
        // 3. EXECUÇÃO
        // ===================================================================
        System.out.println("2. Buscando via AplicacaoControle...");
        UsuarioVO usuarioDummy = new UsuarioVO();
        CidadeVO resultado = appControle.getCidadeVO(9999, usuarioDummy);

        // ===================================================================
        // 4. VALIDAÇÃO
        // ===================================================================
        assertNotNull("O retorno não deveria ser nulo", resultado);
        
        System.out.println("   Nome retornado: '" + resultado.getNome() + "'");
        
        // Valida o ID
        // Dica: Usamos .intValue() para garantir que compara número com número
        assertEquals("O ID deve ser 9999", 9999, resultado.getCodigo().intValue());
        
        // Valida o Nome
        assertEquals("O Nome deve ser 'Cidade Teste'", "Cidade Teste", resultado.getNome());
        
        System.out.println(">>> SUCESSO! BARRA VERDE GARANTIDA <<<");
    }

    private void setPrivateField(Object target, String fieldName, Object value) {
        try {
            java.lang.reflect.Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
             try {
                 java.lang.reflect.Field field = target.getClass().getSuperclass().getDeclaredField(fieldName);
                 field.setAccessible(true);
                 field.set(target, value);
            } catch (Exception ex) {
                throw new RuntimeException(e);
            }
        }
    }
}