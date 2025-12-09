package arquitetura;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.utilitarias.Uteis;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import relatorio.negocio.comuns.arquitetura.CrosstabVO;

public class RelatorioCrossTabTest extends TestManager {

	private static final long serialVersionUID = -2171102677420116280L;

	public RelatorioCrossTabTest() {
		try {
			JasperDesign jasperDesign = JRXmlLoader
					.load("C:\\Users\\Renato Borges\\JaspersoftWorkspace\\MyReports\\relatorioDinamico.jrxml");

			List<Relatorio> listaRelatorioPrincipal = montarDadosRelatorio();

			jasperDesign.setName("report");
			JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
			JRBeanCollectionDataSource jbds = new JRBeanCollectionDataSource(listaRelatorioPrincipal);
			Map<String, Object> parameters = new HashMap<String, Object>();

			FuncionarioCargoVO funcionarioCargoVO = getFacadeFactoryTest().getFuncionarioCargoFacade()
					.consultarPorChavePrimaria(144, Uteis.NIVELMONTARDADOS_PROCESSAMENTO, null);

			parameters.put("SUBREPORT_DIR", "C:\\Users\\Renato Borges\\JaspersoftWorkspace\\MyReports\\");
			parameters.put("funcionarioCargo", funcionarioCargoVO.getMatriculaCargo());
			parameters.put("nome", funcionarioCargoVO.getFuncionarioVO().getPessoa().getNome());
			parameters.put("cpf", funcionarioCargoVO.getFuncionarioVO().getPessoa().getCPF());
			parameters.put("dataNascimento", funcionarioCargoVO.getFuncionarioVO().getPessoa().getDataNasc_Apresentar());
			parameters.put("rg", funcionarioCargoVO.getFuncionarioVO().getPessoa().getRgFiador());
			parameters.put("dataAdmissao", Uteis.getData(funcionarioCargoVO.getDataAdmissao()));
			parameters.put("cargo", funcionarioCargoVO.getCargo().getDescricao());
			parameters.put("secao", funcionarioCargoVO.getSecaoFolhaPagamento().getDescricao());
			parameters.put("situacao", funcionarioCargoVO.getSituacaoFuncionarioApresentar());

			// resultado
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, jbds);
			// ver
			JasperViewer.viewReport(jasperPrint);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public List<Relatorio> montarDadosRelatorio() {
		List<Relatorio> listaRelatorioPrincipal = new ArrayList<>();
		Relatorio relatorioPrincipal = new Relatorio();

		List<CrosstabVO> listaCrosstabVO = new ArrayList<>();

		List<String> todosMeses = montarListaMeses();

		CrosstabVO obj = new CrosstabVO();
		for (int contador = 0; contador < 5; contador++) {
			obj = new CrosstabVO();

			for (int i = 0; i < todosMeses.size(); i++) {
				obj.setLabelLinha("Linha" + contador);
				obj.setLabelColuna(todosMeses.get(i));
				obj.setOrdemColuna(i);
				// obj.setOrdemLinha(i);
				obj.setValorInteger(i + contador);
				listaCrosstabVO.add(obj);
				obj = new CrosstabVO();
			}
		}

		relatorioPrincipal.getObjetos().put("campo1", "Dados Basicos Relatorio Principal");
		relatorioPrincipal.getObjetos().put("crossTab", relatorioPrincipal.getListaFrequencia(listaCrosstabVO));
		listaRelatorioPrincipal.add(relatorioPrincipal);
		return listaRelatorioPrincipal;
	}

	public List<String> montarListaMeses() {
		List<String> todosMeses = new ArrayList<>(0);
		todosMeses.add("Janeiro");
		todosMeses.add("Fevereiro");
		todosMeses.add("Março");
		todosMeses.add("Abril");
		todosMeses.add("Maio");
		todosMeses.add("Junho");
		todosMeses.add("Julho");
		todosMeses.add("Agosto");
		todosMeses.add("Setembro");
		todosMeses.add("Outubro");
		todosMeses.add("Novembro");
		todosMeses.add("Dezembro");
		return todosMeses;
	}

	@Test
	public void executar() {
		new RelatorioCrossTabTest();
	}

	public class Relatorio {

		private Map<String, Object> objetos;
		private Map<String, Object> objetosFuncionarioCargo;

		public Map<String, Object> getObjetos() {
			if (objetos == null) {
				objetos = new HashMap<String, Object>();
			}
			return objetos;
		}

		public void setObjetos(Map<String, Object> objetos) {
			this.objetos = objetos;
		}

		public Map<String, Object> getObjetosFuncionarioCargo() {
			if (objetosFuncionarioCargo == null) {
				objetosFuncionarioCargo = new HashMap<>();
			}
			return objetosFuncionarioCargo;
		}

		public void setObjetosFuncionarioCargo(Map<String, Object> objetosFuncionarioCargo) {
			this.objetosFuncionarioCargo = objetosFuncionarioCargo;
		}

		public JRDataSource getListaFrequencia(List<CrosstabVO> listaCrosstabVO) {
			return new JRBeanArrayDataSource(listaCrosstabVO.toArray());
		}
	}
}
