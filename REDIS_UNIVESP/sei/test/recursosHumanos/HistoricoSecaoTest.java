package recursosHumanos;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import arquitetura.TestManager;
import controle.arquitetura.DataModelo;
import negocio.facade.jdbc.recursoshumanos.HistoricoSecao;

public class HistoricoSecaoTest extends TestManager {

	private HistoricoSecao historicoSecao;
	DataModelo dataModelo = new DataModelo();
	
	public HistoricoSecaoTest() {
		try {
			historicoSecao = new HistoricoSecao(getConexao(), getFacadeFactoryTest());
			montarDadosDataModelo();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void consultarHistoricosSecao() {
		try {
			historicoSecao.consultarPorEnumCampoConsulta(dataModelo);
			System.out.println(dataModelo.getListaConsulta().size());;
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
