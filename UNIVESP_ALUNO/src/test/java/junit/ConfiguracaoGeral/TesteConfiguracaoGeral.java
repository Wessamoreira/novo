package junit.ConfiguracaoGeral;



import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.TimeUnit;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import controle.arquitetura.AplicacaoControle;
import controle.arquitetura.RedisService;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.facade.jdbc.administrativo.ConfiguracaoGeralSistema;
import negocio.facade.jdbc.arquitetura.FacadeFactory;
import negocio.comuns.utilitarias.Uteis;

public class TesteConfiguracaoGeral {

    @Test
    public void testeCacheHitMissConfiguracao() throws Exception {
        System.out.println("--- INICIANDO TESTE: CONFIGURAÇÃO GERAL (REDIS vs BANCO) ---");

        // =================================================================================
        // 1. SETUP DA INFRAESTRUTURA (Conexão Real com Redis 173...)
        // =================================================================================
        
        // Configuração do Redis (IP que validamos anteriormente)
        String ipRedis = "173.249.30.27"; 
        RedisStandaloneConfiguration configRede = new RedisStandaloneConfiguration(ipRedis, 6379);
        JedisConnectionFactory factory = new JedisConnectionFactory(configRede);
        factory.afterPropertiesSet();

        StringRedisTemplate redisTemplate = new StringRedisTemplate();
        redisTemplate.setConnectionFactory(factory);
        redisTemplate.afterPropertiesSet();

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        
        RedisService redisService = new RedisService();
        setPrivateField(redisService, "redisTemplate", redisTemplate);
        setPrivateField(redisService, "gson", gson);

        // =================================================================================
        // 2. SETUP DOS MOCKS (Simulando o Banco de Dados)
        // =================================================================================
        
        // Criamos os Mocks para enganar a aplicação e achar que tem banco
        FacadeFactory facadeFactoryMock = Mockito.mock(FacadeFactory.class);
        ConfiguracaoGeralSistema configFacadeMock = Mockito.mock(ConfiguracaoGeralSistema.class);
        
        // Ensinamos: Quando pedir o Facade de Config, entregue o nosso Mock
        when(facadeFactoryMock.getConfiguracaoGeralSistemaFacade()).thenReturn(configFacadeMock);

        // Instanciamos a Classe que vamos testar
        AplicacaoControle appControle = new AplicacaoControle();
        
        // Injetamos as dependências nela
        setPrivateField(appControle, "redisService", redisService);
        setPrivateField(appControle, "facadeFactory", facadeFactoryMock);

        // =================================================================================
        // 3. PREPARAÇÃO DO DADO DE TESTE
        // =================================================================================
        Integer idUnidadeTeste = 88; // Vamos usar a Unidade 88
        UsuarioVO usuario = new UsuarioVO();
        String chaveRedis = "app:configuracaogeral:unidade:" + idUnidadeTeste;
        
        // LIMPEZA: Garante que o Redis está vazio para essa unidade (para forçar o Cache Miss)
        redisService.remover(chaveRedis); 

        // O QUE O BANCO VAI RETORNAR:
        ConfiguracaoGeralSistemaVO configDoBanco = new ConfiguracaoGeralSistemaVO();
        configDoBanco.setCodigo(500);
        configDoBanco.setMensagemTelaLogin("Mensagem vinda do Banco de Dados (Teste)");

        // MOCK:
        // 1. O sistema pergunta: "Qual o ID da config da unidade 88?" -> Resposta: 500
        when(configFacadeMock.consultarCodigoConfiguracaoReferenteUnidadeEnsino(idUnidadeTeste)).thenReturn(500);
        
        // 2. O sistema pergunta: "Me dá o objeto 500?" -> Resposta: Nosso objeto criado acima
        when(configFacadeMock.consultarPorCodigoConfiguracaoGeralSistema(eq(500), anyBoolean(), any(), anyInt()))
             .thenReturn(configDoBanco);

        // =================================================================================
        // 4. FASE 1: CACHE MISS (O Redis está vazio, TEM que ir no Banco)
        // =================================================================================
        System.out.println("\n>> FASE 1: Chamando pela primeira vez...");
        
        ConfiguracaoGeralSistemaVO resultado1 = appControle.getConfiguracaoGeralSistemaVO(idUnidadeTeste, usuario);
        
        // Validações Fase 1
        assertNotNull("O retorno não pode ser nulo", resultado1);
        assertEquals("Mensagem vinda do Banco de Dados (Teste)", resultado1.getMensagemTelaLogin());
        
        // PROVA DE FOGO 1: Verifica se o método do banco FOI chamado
        verify(configFacadeMock, times(1)).consultarPorCodigoConfiguracaoGeralSistema(eq(500), anyBoolean(), any(), anyInt());
        System.out.println("   [OK] O sistema foi buscar no banco de dados.");

        // =================================================================================
        // 5. FASE 2: CACHE HIT (O Redis tem o dado, NÃO PODE ir no Banco)
        // =================================================================================
        System.out.println("\n>> FASE 2: Chamando pela segunda vez (Cache deve estar quente)...");
        
        // Limpa os contadores do Mock para começar a contar do zero
        clearInvocations(configFacadeMock); 

        // Chama o método de novo
        ConfiguracaoGeralSistemaVO resultado2 = appControle.getConfiguracaoGeralSistemaVO(idUnidadeTeste, usuario);

        // Validações Fase 2
        assertNotNull("O retorno da segunda chamada não pode ser nulo", resultado2);
        assertEquals("Mensagem vinda do Banco de Dados (Teste)", resultado2.getMensagemTelaLogin());

        // PROVA DE FOGO 2: Verifica se o Facade foi chamado ZERO VEZES agora
        // Se der erro aqui, significa que sua lógica de Redis falhou e ele foi no banco à toa.
        verify(configFacadeMock, times(0)).consultarPorCodigoConfiguracaoGeralSistema(anyInt(), anyBoolean(), any(), anyInt());
        verify(configFacadeMock, times(0)).consultarCodigoConfiguracaoReferenteUnidadeEnsino(anyInt());
        
        System.out.println("   [OK] Sucesso! O Facade NÃO foi chamado. O dado veio instantaneamente do Redis.");
        System.out.println("\n--- TESTE FINALIZADO COM SUCESSO (BARRA VERDE) ---");
    }

    // Método auxiliar obrigatório para injetar dependências privadas sem Spring
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
            } catch (Exception ex) { throw new RuntimeException(e); }
        }
    }
}
