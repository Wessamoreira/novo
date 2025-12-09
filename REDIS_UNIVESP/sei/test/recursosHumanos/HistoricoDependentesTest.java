package recursosHumanos;

import static org.junit.Assert.fail;

import org.junit.Test;

import arquitetura.TestManager;
import controle.arquitetura.DataModelo;
import negocio.facade.jdbc.recursoshumanos.HistoricoDependentes;

public class HistoricoDependentesTest extends TestManager {

	private static final long serialVersionUID = 6833639754220900123L;
	
	private HistoricoDependentes historicoDependentes;
	DataModelo dataModelo = new DataModelo();
	
	public HistoricoDependentesTest() {
		try {
			historicoDependentes = new HistoricoDependentes(getConexao(), getFacadeFactoryTest());
			montarDadosDataModelo();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void consultarHistoricosDependentes() {
		try {
			historicoDependentes.consultarPorEnumCampoConsulta(dataModelo);
			//assertTrue(dataModelo.getListaConsulta().size() > 0);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		
	}

	private void montarDadosDataModelo() {
		dataModelo.setCampoConsulta("FUNCIONARIO");
		dataModelo.setValorConsulta("a");
		dataModelo.setPage(1);
		dataModelo.setOffset(1);
		dataModelo.setLimitePorPagina(10);
	}
}
