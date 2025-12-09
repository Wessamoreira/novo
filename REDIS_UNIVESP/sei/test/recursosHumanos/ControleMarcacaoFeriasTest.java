package recursosHumanos;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import arquitetura.TestManager;
import negocio.comuns.recursoshumanos.CompetenciaFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.ControleMarcacaoFeriasVO;
import negocio.comuns.recursoshumanos.MarcacaoFeriasVO;
import negocio.comuns.recursoshumanos.enumeradores.SituacaoMarcacaoFeriasEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.recursoshumanos.CompetenciaFolhaPagamento;
import negocio.facade.jdbc.recursoshumanos.ControleMarcacaoFerias;
import negocio.facade.jdbc.recursoshumanos.MarcacaoFerias;

public class ControleMarcacaoFeriasTest extends TestManager {

	private static final long serialVersionUID = 8356698752514860854L;

	private ControleMarcacaoFerias controleMarcacaoFerias;
	private ControleMarcacaoFeriasVO controleMarcacaoFeriasVO;
	
	private CompetenciaFolhaPagamento competenciaFolhaPagamento;
	private CompetenciaFolhaPagamentoVO competenciaAtiva;
	private MarcacaoFerias marcacaoFerias;
	private MarcacaoFeriasVO marcacaoFeriasVO;
	
	/**
	 * Contrutor que instancia o {@link ControleMarcacaoFerias}
	 */
	public ControleMarcacaoFeriasTest() {
		try {
			controleMarcacaoFerias = new ControleMarcacaoFerias(getConexao(), getFacadeFactoryTest());
			competenciaFolhaPagamento = new CompetenciaFolhaPagamento(getConexao(), getFacadeFactoryTest());
			marcacaoFerias = new MarcacaoFerias(getConexao(), getFacadeFactoryTest());
		//	competenciaAtiva = competenciaFolhaPagamento.consultarCompetenciaAtiva();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void fabricaDeDados(Boolean lancadoAdiantamento) throws Exception{
		
		marcacaoFeriasVO = new MarcacaoFeriasVO();
		marcacaoFeriasVO.getPeriodoAquisitivoFeriasVO().setCodigo(1);
		marcacaoFeriasVO.getFuncionarioCargoVO().setCodigo(3598);
		marcacaoFeriasVO.setDataInicioGozo(competenciaAtiva.getDataCompetencia());
		marcacaoFeriasVO.setDataFinalGozo(UteisData.adicionarDiasEmData(competenciaAtiva.getDataCompetencia(), 15));
		marcacaoFeriasVO.setSituacaoMarcacao(SituacaoMarcacaoFeriasEnum.CALCULADA);
		
		marcacaoFerias.incluir(marcacaoFeriasVO, false, null);
		
		controleMarcacaoFeriasVO = new ControleMarcacaoFeriasVO();
		controleMarcacaoFeriasVO.setMarcacaoFerias(marcacaoFeriasVO);
		controleMarcacaoFeriasVO.setLancadoReciboNoContraCheque(lancadoAdiantamento);
		controleMarcacaoFeriasVO.getFuncionarioCargo().setCodigo(3598);
		
		controleMarcacaoFerias.incluir(controleMarcacaoFeriasVO, false, null);
	}
	
	@Before
	public void before() throws Exception {
	}

	@After
	public void after() throws Exception {
		controleMarcacaoFerias.excluir(controleMarcacaoFeriasVO, false, null);
		marcacaoFerias.excluirMarcacaoFerias(marcacaoFeriasVO, SituacaoMarcacaoFeriasEnum.MARCADA, null);
	}

	@Test
	public void testFuncionarioComMarcacaoDeFeriasSemRecibo() {
		try {
			fabricaDeDados(false);
			List<ControleMarcacaoFeriasVO> obj = controleMarcacaoFerias.consultarPorDataCompetenciaDataInicioGozoDaMarcacaoFerias(competenciaAtiva.getDataCompetencia(), false);
			boolean retorno = Uteis.isAtributoPreenchido(obj);
			assertTrue(retorno);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testFuncionarioComMarcacaoDeFeriasComRecibo() {
		try {
			fabricaDeDados(true);
			List<ControleMarcacaoFeriasVO> obj = controleMarcacaoFerias.consultarPorDataCompetenciaDataInicioGozoDaMarcacaoFerias(competenciaAtiva.getDataCompetencia(), false);
			boolean retorno = Uteis.isAtributoPreenchido(obj);
			assertTrue(!retorno);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
