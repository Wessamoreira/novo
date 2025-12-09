package webservice;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.crm.ProspectsVO;
import webservice.servicos.IntegracaoRdStationRS;

public class IntegracaoRDStationTest {


	IntegracaoRdStationRS integracaoRD;
	
	ProspectsVO prospectsVO;
	
	ConfiguracaoGeralSistemaVO config;
	
	@Before
    public void before() throws Exception {
		config = new ConfiguracaoGeralSistemaVO();
		prospectsVO = new ProspectsVO();
		integracaoRD = new IntegracaoRdStationRS();
    }
	
    @After
    public void after() throws Exception {
    }
	
	@Test
	public void testarIncluirLeadComParametroDesabilitado() throws Exception {

		config.setAtivarIntegracaoRdStation(false);
		assertTrue(integracaoRD.incluirLeadRDStation(prospectsVO, config).equals("401"));
		
	}
	
	@Test
	public void testarLeadEnviadoSemToken() throws Exception {
		
		config.setAtivarIntegracaoRdStation(true);
		prospectsVO.setNome("Teste JUNIT");
		prospectsVO.setEmailPrincipal("testejunit@otimize-ti.com.br");
		prospectsVO.setCelular("+55 (62) 9.9999-9999");
		
		String codigoResposta = integracaoRD.incluirLeadRDStation(prospectsVO, config);
		
		assertTrue(codigoResposta.equals("400"));
		
	}
	
	@Test
	public void testarLeadEnviadoComSucesso() throws Exception {
		
		config.setAtivarIntegracaoRdStation(true);
		config.setTokenRdStation("295c131c17e535398f192d867f54a28e");
		config.setIdentificadorRdStation("Teste JUnit");
		prospectsVO.setNome("Teste JUNIT");
		prospectsVO.setEmailPrincipal("testejunit@otimize-ti.com.br");
		prospectsVO.setCelular("+55 (62) 9.9999-9999");
		
		String codigoResposta = integracaoRD.incluirLeadRDStation(prospectsVO, config);
		
		assertTrue(codigoResposta.equals("200"));
		
	}
}