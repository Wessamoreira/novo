package arquitetura;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JRDesignParameter;
import net.sf.jasperreports.engine.design.JRDesignSection;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignSubreport;
import net.sf.jasperreports.engine.design.JRDesignSubreportParameter;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

public class RelatorioDinamicoTest extends TestManager {

	private static final long serialVersionUID = -2171102677420116280L;
	
	public RelatorioDinamicoTest() {
		try {
			JasperDesign jasperDesign = JRXmlLoader.load("C:\\Users\\Renato Borges\\JaspersoftWorkspace\\MyReports\\relatorioDinamico.jrxml");
			
			List<Relatorio> listaRelatorioPrincipal = new ArrayList<Relatorio>(0);
			List<Relatorio> listaSubrelatorio = new ArrayList<Relatorio>();

			Relatorio relatorioPrincipal = new Relatorio();
			Relatorio relatorioSubRelatorio = new Relatorio();
			
			relatorioPrincipal.getObjetos().put("campo1", "Brasil");
			relatorioSubRelatorio.getObjetos().put("campo1", "Goias");
			listaSubrelatorio.add(relatorioSubRelatorio);
			
			relatorioSubRelatorio = new Relatorio();
			relatorioSubRelatorio.getObjetos().put("campo1", "Sao Paulo");
			listaSubrelatorio.add(relatorioSubRelatorio);
			relatorioPrincipal.getObjetos().put("campo2", listaSubrelatorio);
			listaRelatorioPrincipal.add(relatorioPrincipal);

			relatorioPrincipal = new Relatorio();
			relatorioPrincipal.getObjetos().put("campo1", "EUA");
			listaSubrelatorio = new ArrayList<Relatorio>();
			relatorioSubRelatorio = new Relatorio();
			relatorioSubRelatorio.getObjetos().put("campo1", "California");
			listaSubrelatorio.add(relatorioSubRelatorio);
			relatorioSubRelatorio = new Relatorio();
			relatorioSubRelatorio.getObjetos().put("campo1", "Texas");
			listaSubrelatorio.add(relatorioSubRelatorio);
			relatorioPrincipal.getObjetos().put("campo2", listaSubrelatorio);
			listaRelatorioPrincipal.add(relatorioPrincipal);
			jasperDesign.setName("report");

			JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
			JRBeanCollectionDataSource jbds = new JRBeanCollectionDataSource(listaRelatorioPrincipal);
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("SUBREPORT_DIR", "C:\\Users\\Renato Borges\\JaspersoftWorkspace\\MyReports\\");
			// resultado
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, jbds);
			// ver
			JasperViewer.viewReport(jasperPrint);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	

	private JasperDesign getReport2() throws JRException, ClassNotFoundException {
		JasperDesign jasperDesign = JRXmlLoader.load("C:\\Users\\Renato Borges\\JaspersoftWorkspace\\MyReports\\relatorioDinamico.jrxml");
		List<Map<String, Object>> listaMap = new ArrayList<Map<String, Object>>(0);
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("campo1", "Brasil");
		listaMap.add(map1);
		map1 = new HashMap<String, Object>();
		map1.put("campo1", "EUA");
		listaMap.add(map1);
		jasperDesign.setName("report");
		
		JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
		JRBeanCollectionDataSource jbds = new JRBeanCollectionDataSource(listaMap);
		// resultado
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<String, Object>(), jbds);
		// ver
		JasperViewer.viewReport(jasperPrint);
		return jasperDesign;
	}
	private JasperDesign getReport() throws JRException, ClassNotFoundException {
		JasperDesign jasperDesign = JRXmlLoader.load("C:\\Users\\Renato Borges\\JaspersoftWorkspace\\MyReports\\relatorioDinamico.jrxml");
		jasperDesign.setName("report");
		/*JRDesignBand titleRel = new JRDesignBand();
		titleRel.setHeight(25);
		titleRel.addElement(addJRDesignStaticText("Title - Report", 0, 0, 150, 20));
		jasperDesign.setTitle(titleRel);*/
		JRDesignBand sumaryRel = new JRDesignBand();
		sumaryRel.setHeight(25);
		sumaryRel.addElement(addJRDesignStaticText("Title FIM", 0, 0, 150, 20));
		jasperDesign.setSummary(sumaryRel);
		
		// PARAMETERS
		JRDesignField fieldNome = new JRDesignField();
		fieldNome.setName("nomePais");
		fieldNome.setValueClass(String.class);
		jasperDesign.addField(fieldNome);
		JRDesignField fieldlistaEstado = new JRDesignField();
		fieldlistaEstado.setName("listaEstado");
		fieldlistaEstado.setValueClass(Collection.class);
		jasperDesign.addField(fieldlistaEstado);
		JRDesignParameter parametroSubReport = new JRDesignParameter();
		parametroSubReport.setName("subRelatorio");
		parametroSubReport.setValueClass(java.lang.Object.class);
		jasperDesign.addParameter(parametroSubReport);
		JRDesignParameter parametroSubReportFinal = new JRDesignParameter();
		parametroSubReportFinal.setName("subRelatorioFinal");
		parametroSubReportFinal.setValueClass(java.lang.Object.class);
		jasperDesign.addParameter(parametroSubReportFinal);
		// FIM PARAMETERS
		// DETAIL
		JRDesignBand detailRel = new JRDesignBand();
		detailRel.setHeight(500);
		detailRel.addElement(addTextField(
				" \"Pais: \" +$F{nomePais}+ \" tem \" + $F{listaEstado}.size() + \" estados\"", 0, 0, 300, 20));
		// componente do subrelatorio
		JRDesignSubreport subReport = new JRDesignSubreport(jasperDesign);
		subReport.setUsingCache(false);
		subReport.setX(0);
		subReport.setY(0);
		subReport.setWidth(555);
		subReport.setHeight(400);
		// pega field subrelatorio compilado que foi passado por parametro
		JRDesignExpression expression2 = new JRDesignExpression();
		expression2.setText("$P{subRelatorio}");
		subReport.setExpression(expression2);
		// passa DS para sub report
		JRDesignExpression expression3 = new JRDesignExpression();
		expression3.setText("new net.sf.jasperreports.engine.data.JRBeanCollectionDataSource($F{listaEstado})");
		subReport.setDataSourceExpression(expression3);
		// passa relatorio compilado
		JRDesignExpression jrParam = new JRDesignExpression();
		jrParam.setText("$P{subRelatorioFinal}");
		JRDesignSubreportParameter par = new JRDesignSubreportParameter();
		par.setExpression(jrParam);
		par.setName("subRelatorioFinal");
		subReport.addParameter(par);
		// add elemnto subrelatorio
		detailRel.addElement(subReport);
		((JRDesignSection) jasperDesign.getDetailSection()).getBandsList().add(0, detailRel);

		return jasperDesign;
	}

	private JasperDesign getSubReport() throws JRException, ClassNotFoundException {
		JasperDesign jasperSubDesign = new JasperDesign();
		jasperSubDesign.setName("subReport");
		JRDesignBand title = new JRDesignBand();
		title.setHeight(25);
		title.addElement(addJRDesignStaticText("ESTADOS", 0, 0, 150, 20));
		jasperSubDesign.setTitle(title);
		JRDesignBand sumary = new JRDesignBand();
		sumary.setHeight(25);
		sumary.addElement(addJRDesignStaticText("FIM ESTADOS", 0, 0, 150, 20));
		jasperSubDesign.setSummary(sumary);
		// DECLARA
		JRDesignField field = new JRDesignField();
		field.setName("nomeEstado");
		field.setValueClass(String.class);
		jasperSubDesign.addField(field);
		JRDesignField fieldList = new JRDesignField();
		fieldList.setName("listaCidade");
		fieldList.setValueClass(Collection.class);
		jasperSubDesign.addField(fieldList);
		JRDesignParameter parametroSubReportFinal = new JRDesignParameter();
		parametroSubReportFinal.setName("subRelatorioFinal");
		parametroSubReportFinal.setValueClass(java.lang.Object.class);
		jasperSubDesign.addParameter(parametroSubReportFinal);
		// FIM DECLARA
		
		JRDesignBand detail = new JRDesignBand();
		detail.setHeight(300);
		detail.addElement(addTextField("$F{nomeEstado}", 0, 0, 100, 20));
		detail.addElement(addTextField("$F{listaCidade}.size()  ", 100, 0, 100, 20));
		
		// DETAIL
		// Cria componente sub relatorio
		JRDesignSubreport subReport = montarDadosSubRelatorio(jasperSubDesign);

		detail.addElement(subReport);
		((JRDesignSection) jasperSubDesign.getDetailSection()).getBandsList().add(0, detail);
		return jasperSubDesign;
	}

	private JRDesignSubreport montarDadosSubRelatorio(JasperDesign jasperSubDesign) {
		JRDesignSubreport subReport = new JRDesignSubreport(jasperSubDesign);
		subReport.setUsingCache(false);
		subReport.setX(0);
		subReport.setY(0);
		subReport.setWidth(555);
		subReport.setHeight(200);
		JRDesignExpression expression2 = new JRDesignExpression();
		expression2.setText("$P{subRelatorioFinal}");
		subReport.setExpression(expression2);
		// Passa o DataSource para o sub relatório.
		JRDesignExpression expression3 = new JRDesignExpression();
		expression3.setText("new net.sf.jasperreports.engine.data.JRBeanCollectionDataSource($F{listaCidade})");
		subReport.setDataSourceExpression(expression3);
		return subReport;
	}

	private JasperDesign getSubSonReport() throws JRException, ClassNotFoundException {
		JasperDesign jasperSubSonDesign = new JasperDesign();
		jasperSubSonDesign.setName("subSonReport");
		JRDesignBand title = new JRDesignBand();
		title.setHeight(25);
		title.addElement(addJRDesignStaticText("CIDADES", 0, 0, 150, 20));
		jasperSubSonDesign.setTitle(title);
		JRDesignBand sumary = new JRDesignBand();
		sumary.setHeight(25);
		sumary.addElement(addJRDesignStaticText("FIM CIDADES", 0, 0, 150, 20));
		jasperSubSonDesign.setSummary(sumary);
		// DECLARA
		JRDesignField fieldParam = new JRDesignField();
		fieldParam.setName("nomeCidade");
		fieldParam.setValueClass(String.class);
		jasperSubSonDesign.addField(fieldParam);
		// FIM DECLARA
		JRDesignBand detail = new JRDesignBand();
		detail.setHeight(25);
		detail.addElement(addTextField("$F{nomeCidade}", 50, 0, 100, 20));
		((JRDesignSection) jasperSubSonDesign.getDetailSection()).getBandsList().add(0, detail);
		return jasperSubSonDesign;
	}

	// #######################################################################################################3
	private JRDesignTextField addTextField(String fieldName, int x, int y, int width, int height) {
		JRDesignExpression expression = new JRDesignExpression();
		expression = new JRDesignExpression();
		expression.setText(fieldName);
		JRDesignTextField textField = new JRDesignTextField();
		textField.setExpression(expression);
		textField.setX(x);
		textField.setY(y);
		textField.setWidth(width);
		textField.setHeight(height);
		return textField;
	}

	private JRDesignStaticText addJRDesignStaticText(String text, int x, int y, int width, int height) {
		JRDesignStaticText staticText = new JRDesignStaticText();
		staticText.setText(text);
		staticText.setX(x);
		staticText.setY(y);
		staticText.setWidth(width);
		staticText.setHeight(height);
		return staticText;
	}

	@Test
	public void executar() {
		new RelatorioDinamicoTest();
	}

	public class Relatorio{
		
		private Map<String, Object> objetos;
		
		public Map<String, Object> getObjetos() {
			if(objetos == null) {
				objetos =  new HashMap<String, Object>();
			} 
			return objetos;
		}
		
		public void setObjetos(Map<String, Object> objetos) {
			this.objetos = objetos;
		}
	}
}
