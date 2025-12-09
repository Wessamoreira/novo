package recursosHumanos;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import arquitetura.TestManager;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.CompetenciaFolhaPagamentoVO;
import negocio.facade.jdbc.recursoshumanos.CompetenciaFolhaPagamento;

public class CompetenciaFolhaPagamentoTest extends TestManager {

	private CompetenciaFolhaPagamento competencia;

	public CompetenciaFolhaPagamentoTest() {
		try {
			competencia = new CompetenciaFolhaPagamento(getConexao(), getFacadeFactoryTest());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@Before
	public void before() throws Exception {
	}

	@After
	public void after() throws Exception {
	}

	@Test
	public void testSomaDosValoresMensaisDaListaDeSalarioComposto() {
		CompetenciaFolhaPagamentoVO competenciaFolhaPagamentoVO = new CompetenciaFolhaPagamentoVO();

		try {
			//competenciaFolhaPagamentoVO = competencia.consultarCompetenciaAtiva();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		assertTrue(competenciaFolhaPagamentoVO.getCodigo() != 0);
	}

	@Test
	public void testValidarValoresReferencia() {
		competencia.validarValoresReferencia(new UsuarioVO());
	}

	public CompetenciaFolhaPagamento getCompetencia() {
		return competencia;
	}

	public void setCompetencia(CompetenciaFolhaPagamento competencia) {
		this.competencia = competencia;
	}
}