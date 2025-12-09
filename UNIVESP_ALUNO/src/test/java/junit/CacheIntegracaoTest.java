package junit;

// 1. IMPORTS DO JUNIT 4
import org.junit.Test;
import org.junit.runner.RunWith; // 
import static org.junit.Assert.*; // 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner; //
import org.springframework.data.redis.core.StringRedisTemplate;

import controle.arquitetura.AplicacaoControle;
import controle.arquitetura.SeiApplication;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;

@RunWith(SpringRunner.class) 
@SpringBootTest(
	    classes = SeiApplication.class, 
	    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT 
	)
public class CacheIntegracaoTest { 

    @Autowired
    private AplicacaoControle aplicacaoControle;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    public void testeFluxoCompletoDeInvalidacao() throws Exception { 
       
        System.out.println("--- ETAPA 1: Carregando Cache L1 ---");
        ConfiguracaoGeralSistemaVO config1 = aplicacaoControle.getConfiguracaoGeralSistemaVO(0, null);
        
        // NOTA: No JUnit 4, a mensagem é o PRIMEIRO argumento
        assertNotNull("Deveria ter carregado do banco/redis", config1);

        // Sanity check (ver se cache local funcionou)
        ConfiguracaoGeralSistemaVO config1_cached = aplicacaoControle.getConfiguracaoGeralSistemaVO(0, null);
        assertSame("O cache L1 não está funcionando antes do teste", config1, config1_cached);

        // 2. AÇÃO
        System.out.println("--- ETAPA 2: Simulando ADM publicando evento ---");
        redisTemplate.convertAndSend("univep:cache:invalidation", "app:configuracaogeral:unidade:0");

        Thread.sleep(500); // Delay para o Redis processar

        // 3. VERIFICAÇÃO
        System.out.println("--- ETAPA 3: Consultando novamente ---");
        ConfiguracaoGeralSistemaVO config2 = aplicacaoControle.getConfiguracaoGeralSistemaVO(0, null);
        
        // Verifica se as referências são diferentes (prova que o cache L1 foi limpo e o objeto recriado)
        assertNotSame("O objeto deveria ter sido recarregado (referências diferentes)", config1, config2);
        
        System.out.println(">>> SUCESSO: O Cache foi invalidado e recarregado!");
    }
}