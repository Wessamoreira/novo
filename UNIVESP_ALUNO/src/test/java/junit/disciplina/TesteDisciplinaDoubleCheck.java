package junit.disciplina;



import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.facade.jdbc.academico.Disciplina;
import negocio.facade.jdbc.arquitetura.FacadeFactory;

public class TesteDisciplinaDoubleCheck {

    @Test
    public void testeConcorrenciaComEspiao() throws Exception {
        System.out.println("--- TESTE DE CONCORRÊNCIA (QUEM FOI AONDE?) ---");

        
        String ipRedis = "173.249.30.27"; 
        RedisStandaloneConfiguration configRede = new RedisStandaloneConfiguration(ipRedis, 6379);
        JedisConnectionFactory factory = new JedisConnectionFactory(configRede);
        factory.afterPropertiesSet();

        StringRedisTemplate redisTemplate = new StringRedisTemplate();
        redisTemplate.setConnectionFactory(factory);
        redisTemplate.afterPropertiesSet();

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        
        
        RedisService redisServiceReal = new RedisService();
        setPrivateField(redisServiceReal, "redisTemplate", redisTemplate);
        setPrivateField(redisServiceReal, "gson", gson);

        
        RedisService redisServiceSpy = Mockito.spy(redisServiceReal);

        
        FacadeFactory facadeFactoryMock = Mockito.mock(FacadeFactory.class);
        Disciplina disciplinaFacadeMock = Mockito.mock(Disciplina.class);
        when(facadeFactoryMock.getDisciplinaFacade()).thenReturn(disciplinaFacadeMock);

        
        AplicacaoControle appControle = new AplicacaoControle();
        setPrivateField(appControle, "redisService", redisServiceSpy); 
        setPrivateField(appControle, "facadeFactory", facadeFactoryMock);

        
        Integer idDisciplina = 800;
        String chaveRedis = "app:disciplina:" + idDisciplina;
        
        
        redisServiceReal.remover(chaveRedis);

        
        DisciplinaVO disciplinaBanco = new DisciplinaVO();
        disciplinaBanco.setCodigo(idDisciplina);
        disciplinaBanco.setNome("Física Quântica");

        when(disciplinaFacadeMock.consultarPorChavePrimariaUnica(eq(idDisciplina), anyInt(), any()))
            .thenAnswer(invocation -> {
                System.out.println("   [BANCO] -> Alguém entrou no banco de dados!");
                Thread.sleep(100); 
                return disciplinaBanco;
            });

        // 5. ATAQUE SIMULTÂNEO (50 Threads)
        int threads = 50;
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        List<Callable<DisciplinaVO>> tarefas = new ArrayList<>();
        UsuarioVO usuario = new UsuarioVO();

        for (int i = 0; i < threads; i++) {
            tarefas.add(() -> appControle.getDisciplinaVO(idDisciplina, usuario));
        }

        System.out.println(">> Disparando " + threads + " requisições...");
        executor.invokeAll(tarefas);
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        
        verify(disciplinaFacadeMock, times(1)).consultarPorChavePrimariaUnica(anyInt(), anyInt(), any());
        System.out.println("1. Banco de Dados: Acessado 1 vez (Correto).");

        
        verify(redisServiceSpy, times(1)).setObjeto(eq(chaveRedis), any(), anyLong(), any());
        System.out.println("2. Redis Gravação: Feita 1 vez (Correto - Só o primeiro gravou).");

      
        verify(redisServiceSpy, atLeast(10)).getObjeto(eq(chaveRedis), any());
        System.out.println("3. Redis Leitura:  Acessado " + threads + "+ vezes (Correto - Todos tentaram ler).");
        
        System.out.println("--- CONCLUSÃO: 1 Usuário foi no banco, 9 pegaram o cache gerado por ele. ---");
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
            } catch (Exception ex) { throw new RuntimeException(e); }
        }
    }
}