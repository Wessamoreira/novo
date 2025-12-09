package junit; // Se o arquivo está na pasta junit, mantenha. Se moveu para controle.arquitetura, mude aqui.

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*; // Resolve o erro de any(), eq(), anyInt()
import static org.mockito.Mockito.*;

import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith; // Importante para o Mockito rodar
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner; // Importante
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import controle.arquitetura.AplicacaoControle;
import controle.arquitetura.RedisConfig;
import controle.arquitetura.RedisService;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.CidadeVO;
import negocio.facade.jdbc.arquitetura.FacadeFactory;

// IMPORTANTE: Verifique se este é o nome correto da sua classe de fachada de cidade
// Se der erro aqui, verifique o pacote correto em "negocio.facade..."
import negocio.facade.jdbc.basico.Cidade; 

@RunWith(MockitoJUnitRunner.class) // Inicializa os Mocks automaticamente
public class AplicacaoControleTest {

    @InjectMocks // Injeta os mocks abaixo dentro desta classe real
    private AplicacaoControle aplicacaoControle;

    @Mock
    private RedisService redisServiceMock;

    @Mock
    private FacadeFactory facadeFactoryMock;

    @Mock
    private Cidade cidadeFacadeMock;
    
 // Método auxiliar para injetar dependências privadas sem usar Spring (@Autowired)
    private void setPrivateField(Object target, String fieldName, Object value) {
        try {
            java.lang.reflect.Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao injetar campo via Reflection: " + fieldName, e);
        }
    }

    @Before
    public void setUp() {
        // Configura o comportamento padrão da Factory
        when(facadeFactoryMock.getCidadeFacade()).thenReturn(cidadeFacadeMock);
    }

    @Test
    public void deveRetornarCidadeDoRedisQuandoExisteNoCache() throws Exception {
        Integer codigoCidade = 123;
        UsuarioVO usuario = new UsuarioVO();
        
        // CORREÇÃO: Se setLogin não existe, tente setUsername ou setCodigo
        // Se sua classe realmente usa "login", verifique se o método é setDsLogin ou similar.
        usuario.setUsername("teste"); 

        CidadeVO cidadeRedis = new CidadeVO();
        cidadeRedis.setCodigo(codigoCidade);
        cidadeRedis.setNome("Cidade do Redis");

        String chave = "app:cidade:" + codigoCidade;

        when(redisServiceMock.getObjeto(chave, CidadeVO.class)).thenReturn(cidadeRedis);

        CidadeVO resultado = aplicacaoControle.getCidadeVO(codigoCidade, usuario);

        assertNotNull(resultado);
        assertEquals(codigoCidade, resultado.getCodigo());
        assertEquals("Cidade do Redis", resultado.getNome());
       
        verify(cidadeFacadeMock, never()).consultarPorChavePrimariaUnica(anyInt(), anyBoolean(), any());
    }

    @Test
    public void deveBuscarNoBancoQuandoNaoExisteNoRedis() throws Exception {
        Integer codigoCidade = 456;
        UsuarioVO usuario = new UsuarioVO();
        usuario.setUsername("teste2"); // Ajustado aqui também

        String chave = "app:cidade:" + codigoCidade;
        
        when(redisServiceMock.getObjeto(chave, CidadeVO.class)).thenReturn(null);

        CidadeVO cidadeBanco = new CidadeVO();
        cidadeBanco.setCodigo(codigoCidade);
        cidadeBanco.setNome("Cidade do Banco");

        when(cidadeFacadeMock.consultarPorChavePrimariaUnica(codigoCidade, false, usuario))
                .thenReturn(cidadeBanco);

        CidadeVO resultado = aplicacaoControle.getCidadeVO(codigoCidade, usuario);

        assertNotNull(resultado);
        assertEquals(codigoCidade, resultado.getCodigo());
        assertEquals("Cidade do Banco", resultado.getNome());

        verify(cidadeFacadeMock, times(1))
                .consultarPorChavePrimariaUnica(codigoCidade, false, usuario);

        // Verifica se salvou no Redis com expiração (conforme sua implementação na AplicacaoControle)
        verify(redisServiceMock, times(1))
                .setObjeto(eq(chave), eq(cidadeBanco), eq(24L), eq(TimeUnit.HOURS));
    }

    @Test
    public void deveRetornarNullQuandoCodigoNulo() throws Exception {
        UsuarioVO usuario = new UsuarioVO();
        // Não precisa setar login aqui se não for usado

        CidadeVO resultado = aplicacaoControle.getCidadeVO(null, usuario);

        assertNull(resultado);
        
        verifyNoInteractions(redisServiceMock);
        verifyNoInteractions(cidadeFacadeMock);
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
}