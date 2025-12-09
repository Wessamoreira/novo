package junit;


import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;

import controle.arquitetura.AplicacaoControle;
import controle.arquitetura.RedisConfig;
import controle.arquitetura.RedisService;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.CidadeVO;
import negocio.facade.jdbc.arquitetura.FacadeFactory;
import negocio.facade.jdbc.basico.Cidade;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;




public class AplicacaoControleRedisIT {

    private RedisService redisService;
    private AplicacaoControle aplicacaoControle;

    private FacadeFactory facadeFactoryMock;
    private Cidade cidadeFacadeMock;

    @Before
    public void setUp() {
        //  Configura Redis "real" igual ao RedisConfig
        //    Se preferir, pode instanciar RedisConfig e chamar os @Beans de lá.
        RedisConfig config = new RedisConfig();

        JedisConnectionFactory cf = config.jedisConnectionFactory();
        cf.afterPropertiesSet(); 

        StringRedisTemplate template = config.redisTemplate();
        template.setConnectionFactory(cf); 
        template.afterPropertiesSet();

        //  Cria RedisService e injeta redisTemplate e gson manualmente
        redisService = new RedisService();
        setPrivateField(redisService, "redisTemplate", template);
        setPrivateField(redisService, "gson", new Gson());

        //  Mock da camada de banco (FacadeFactory + CidadeFacade)
        facadeFactoryMock = Mockito.mock(FacadeFactory.class);
        cidadeFacadeMock = Mockito.mock(Cidade.class);
        when(facadeFactoryMock.getCidadeFacade()).thenReturn(cidadeFacadeMock);

        //  Cria AplicacaoControle e injeta RedisService
        //  E sobrescreve getFacadeFactory() para devolver o mock.
        aplicacaoControle = new AplicacaoControle() {
            @Override
            public FacadeFactory getFacadeFactory() {
                return facadeFactoryMock;
            }
        };
        setPrivateField(aplicacaoControle, "redisService", redisService);
    }

    // Helper para injetar campo privado via reflection
    private void setPrivateField(Object target, String fieldName, Object value) {
        try {
            java.lang.reflect.Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void deveLerCidadeDoRedisSemChamarBanco() throws Exception {
        
        Integer codigoCidade = 123;
        String chave = "app:cidade:" + codigoCidade;

        
        UsuarioVO usuario = new UsuarioVO();
        usuario.setNome("usuarioTeste");

        
        CidadeVO cidade = new CidadeVO();
        cidade.setCodigo(codigoCidade);
        cidade.setNome("Cidade Teste Redis");

        
        redisService.setObjeto(chave, cidade);

        //  Chamando o método real
        CidadeVO resultado = aplicacaoControle.getCidadeVO(codigoCidade, usuario);

        // Assert
        assertNotNull("Cidade não pode ser null", resultado);
        assertEquals(codigoCidade, resultado.getCodigo());
        assertEquals("Cidade Teste Redis", resultado.getNome());

        //  Garante que o banco NÃO foi chamado
        verify(cidadeFacadeMock, never())
                .consultarPorChavePrimariaUnica(anyInt(), anyBoolean(), any());
    }
}
