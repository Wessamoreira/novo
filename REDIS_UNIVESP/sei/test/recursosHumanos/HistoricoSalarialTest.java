package recursosHumanos;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import arquitetura.TestManager;
import controle.arquitetura.DataModelo;
import negocio.facade.jdbc.recursoshumanos.HistoricoSalarial;

public class HistoricoSalarialTest extends TestManager {

	private HistoricoSalarial historicoSalarial;
	DataModelo dataModelo = new DataModelo();
	
	public HistoricoSalarialTest() {
		try {
			historicoSalarial = new HistoricoSalarial(getConexao(), getFacadeFactoryTest());
			montarDadosDataModelo();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void consultarHistoricosSalarial() {
		try {
			historicoSalarial.consultarPorEnumCampoConsulta(dataModelo);
			assertTrue(dataModelo.getListaConsulta().size() > 0);
		} catch (Exception e) {
			e.printStackTrace();
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
