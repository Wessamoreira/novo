package recursosHumanos;

import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import arquitetura.TestManager;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.recursoshumanos.CompetenciaFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.PeriodoAquisitivoFeriasVO;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.recursoshumanos.PeriodoAquisitivoFerias;

public class PeriodoAquisitivoFeriasTest extends TestManager {
	
	PeriodoAquisitivoFerias periodoAquisitivoFerias;
	
	@Before
    public void before() throws Exception {
    }
	
    @After
    public void after() throws Exception {
    }
	
	@Test
	public void testConsutaSemErroOUltimoPeriodoAquisitivoDoFuncionario() throws Exception {
		
		PeriodoAquisitivoFeriasVO periodoAquisitivo = periodoAquisitivoFerias.consultarUltimoPeriodoAquisitivoPorFuncionarioCargo(new FuncionarioCargoVO());
			
		assertTrue(periodoAquisitivo.getCodigo().equals(0));
	}
	
	@Test
	public void testNaoPrecisaAbrirUmNovoPeriodoAquisitivo() {
		
		PeriodoAquisitivoFeriasVO pa = new PeriodoAquisitivoFeriasVO();
		pa.setCodigo(1);
		pa.setInicioPeriodo(UteisData.adicionarDiasEmData(new Date(), 0));
		periodoAquisitivoFerias.preencherFinalPeriodoAquisitivo(pa);		
		
		CompetenciaFolhaPagamentoVO competencia = new CompetenciaFolhaPagamentoVO();
		competencia.setDataCompetencia(new Date());
		
		assertTrue(!periodoAquisitivoFerias.validarAberturaNovoPeriodoAquisitivo(pa, competencia));
		
	}
	
	@Test
	public void testPrecisaAbrirUmNovoPeriodoAquisitivoSemPeriodoAquisitivoAberto() {
		
		PeriodoAquisitivoFeriasVO pa = new PeriodoAquisitivoFeriasVO();
		pa.setCodigo(0);
		
		CompetenciaFolhaPagamentoVO competencia = new CompetenciaFolhaPagamentoVO();
		competencia.setDataCompetencia(new Date());
		
		assertTrue(periodoAquisitivoFerias.validarAberturaNovoPeriodoAquisitivo(pa, competencia));
		
	}
	
	@Test
	public void testPrecisaAbrirUmNovoPeriodoAquisitivoDataFinalPeriodoAquisitivoMenorQueDataCompetencia() {
		
		PeriodoAquisitivoFeriasVO pa = new PeriodoAquisitivoFeriasVO();
		pa.setCodigo(1);
		pa.setInicioPeriodo(UteisData.adicionarDiasEmData(new Date(), -400));
		periodoAquisitivoFerias.preencherFinalPeriodoAquisitivo(pa);	
		
		CompetenciaFolhaPagamentoVO competencia = new CompetenciaFolhaPagamentoVO();
		competencia.setDataCompetencia(new Date());
		
		assertTrue(periodoAquisitivoFerias.validarAberturaNovoPeriodoAquisitivo(pa, competencia));
		
	}
}