/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package relatorio.arquitetura;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.arquitetura.SuperArquitetura;
import negocio.comuns.utilitarias.Uteis;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRParagraph;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRPptxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRProperties;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.governors.MaxPagesGovernorException;
import relatorio.negocio.comuns.arquitetura.SuperParametroRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;

/**
 * 
 * @author brethener
 */
public class GeradorRelatorio extends SuperArquitetura implements Serializable {

	public JasperPrint gerarRelatorioJasperPrintObjeto(SuperParametroRelVO superRelVO) throws Exception {
		JasperPrint print = null;
		String nomeJasperReportDesignIReport = null;
		File arquivoIReport = null;		
		try {			
			if(superRelVO.getParametros().containsKey("configuracaoLayoutHistoricoVO")) {
				arquivoIReport = new File(getAplicacaoControle().getConfiguracaoGeralSistemaVO(null, null).getLocalUploadArquivoFixo() + File.separator + superRelVO.getNomeDesignIreport());
			}else if(superRelVO.getParametros().containsKey("configuracaoLayoutAtaResultadosFinaisVO")) {
				arquivoIReport = new File(getAplicacaoControle().getConfiguracaoGeralSistemaVO(null, null).getLocalUploadArquivoFixo() + File.separator + superRelVO.getNomeDesignIreport());
			}else if(superRelVO.getNomeDesignIreport().contains("jrxml")){
				nomeJasperReportDesignIReport = superRelVO.getNomeDesignIreport().substring(0, superRelVO.getNomeDesignIreport().lastIndexOf(".")) + ".jasper";
				arquivoIReport = new File(getCaminhoClassesAplicacao() + File.separator + nomeJasperReportDesignIReport);
			}else{
				arquivoIReport = new File(superRelVO.getNomeDesignIreport());
			}
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(arquivoIReport);
			if (superRelVO.getListaObjetos() == null || superRelVO.getListaObjetos().isEmpty()) {
				print = JasperFillManager.fillReport(jasperReport, superRelVO.getParametros(), new JREmptyDataSource());
			} else {
				JRDataSource jr = new JRBeanArrayDataSource(superRelVO.getListaObjetos().toArray());
				JRProperties.setProperty (JRParagraph.DEFAULT_TAB_STOP_WIDTH, "10");
				 print = JasperFillManager.fillReport(jasperReport, superRelVO.getParametros(), jr);
			}
			nomeJasperReportDesignIReport = null;
			arquivoIReport = null;
			jasperReport = null;
			return print;
		} catch (Exception e) {
			throw excecaoTratada(e);
		}

	}

	public String realizarExportacaoRelatorio(SuperParametroRelVO superRelVO) throws Exception {
		JasperPrint print = gerarRelatorioJasperPrintObjeto(superRelVO);
		if (superRelVO.getTipoRelatorioEnum().equals(TipoRelatorioEnum.PDF)) {
			return realizarExportacaoPDF(superRelVO, print);
		} else if (superRelVO.getTipoRelatorioEnum().equals(TipoRelatorioEnum.EXCEL)) {
			return realizarExportacaoEXCEL(superRelVO, print);
		} else if (superRelVO.getTipoRelatorioEnum().equals(TipoRelatorioEnum.HTML)) {
			return realizarExportacaoHTML(superRelVO, print);
		} else if (superRelVO.getTipoRelatorioEnum().equals(TipoRelatorioEnum.DOC)) {
			return realizarExportacaoDOC(superRelVO, print);
		} else if (superRelVO.getTipoRelatorioEnum().equals(TipoRelatorioEnum.IMAGEM)) {
			return realizarExportacaoIMAGEM(superRelVO, print);
		} else {
			return realizarExportacaoPPT(superRelVO, print);
		}
	}

	public static String realizarExportacaoPDF(SuperParametroRelVO superParametroRelVO, String caminhoBaseSalvarArquivo, String nomeArquivo) throws Exception {

		File pdfFile = new File(caminhoBaseSalvarArquivo);
		if (!pdfFile.exists()) {
			pdfFile.mkdirs();
		}
		pdfFile = new File(caminhoBaseSalvarArquivo + File.separator + nomeArquivo);
		if (pdfFile.exists()) {
			try {
				pdfFile.delete();
			} catch (Exception e) {
				throw new Exception("Erro ao exportar arquivo em PDF.");
			}
		}

		JRPdfExporter jrpdfexporter = new JRPdfExporter();
		jrpdfexporter.setExporterInput(new SimpleExporterInput(new GeradorRelatorio().gerarRelatorioJasperPrintObjeto(superParametroRelVO)));
		jrpdfexporter.setExporterOutput(new SimpleOutputStreamExporterOutput(pdfFile));
		jrpdfexporter.exportReport();
		jrpdfexporter = null;
		return pdfFile.getName();
		// return pdfFile.getAbsolutePath();
	}

	public String realizarExportacaoPDF(SuperParametroRelVO superRelVO, JasperPrint print) throws Exception {
		String nome = null;
		MatriculaVO matriculaVO;
		
		if (superRelVO.getParametros().get("matriculaAluno") != null) {
			matriculaVO = (MatriculaVO) superRelVO.getParametros().get("matriculaAluno");
			nome = matriculaVO.getMatricula() +" - "+ matriculaVO.getAluno().getNome().replace("'", "") + " - " + Uteis.removeCaractersEspeciais2(Uteis.getDataComHoraCompleta(new Date()).replaceAll("/", "-").replaceAll(" ", "_")) +".pdf";
			if (nome.contains("/") || nome.contains(" ") || nome.contains("|")) {
				nome = nome.replace("/", "_");
				nome = nome.replace(" ", "_");
				nome = nome.replace("|", "_");
			}
		} else if(Uteis.isAtributoPreenchido(superRelVO.getNomeEspecificoRelatorio())){
			if (superRelVO.getNomeEspecificoRelatorio().contains(" ") || superRelVO.getNomeEspecificoRelatorio().contains("/")) {
				superRelVO.setNomeEspecificoRelatorio(superRelVO.getNomeEspecificoRelatorio().replace(" ", "_"));
				superRelVO.setNomeEspecificoRelatorio(superRelVO.getNomeEspecificoRelatorio().replace("/", "_"));
				superRelVO.setNomeEspecificoRelatorio(superRelVO.getNomeEspecificoRelatorio().replace("|", "_"));
			}
			nome = superRelVO.getNomeEspecificoRelatorio()+"_"+superRelVO.getNomeRelatorio() + ".pdf";
		}else{
			nome = superRelVO.getNomeRelatorio() + ".pdf";
		}
		String nomeRel = "relatorio" + File.separator + nome;
		File pdfFile = new File(getCaminhoPastaWeb() + File.separator + nomeRel);
		if (pdfFile.exists()) {
			try {
				pdfFile.delete();
			} catch (Exception e) {
				throw new Exception("Erro ao exportar arquivo em PDF.");
			}
		}

		JRPdfExporter jrpdfexporter = new JRPdfExporter();
		jrpdfexporter.setExporterInput(new SimpleExporterInput(print));
		jrpdfexporter.setExporterOutput(new SimpleOutputStreamExporterOutput(pdfFile));		
		jrpdfexporter.exportReport();
		nome = null;
		nomeRel = null;
		jrpdfexporter = null;
//		System.out.println("Caminho do Relatorio no jasper: "+pdfFile.getName());
		return pdfFile.getName();
		// return pdfFile.getAbsolutePath();
	}

	public static String realizarExportacaoEXCEL(SuperParametroRelVO superParametroRelVO, String caminhoBaseSalvarArquivo, String nomeArquivo) throws Exception {
		File file = new File(caminhoBaseSalvarArquivo);
		if (!file.exists()) {
			file.mkdirs();
		}
		
		File excelFile = new File(caminhoBaseSalvarArquivo + File.separator + nomeArquivo);
		if (excelFile.exists()) {
			try {
				excelFile.delete();
			} catch (Exception e) {
				throw new Exception("Erro ao exportar arquivo em Excel.");
			}
		}

		JRXlsxExporter jrpdfexporter = new JRXlsxExporter();
		jrpdfexporter.setExporterInput(new SimpleExporterInput(new GeradorRelatorio().gerarRelatorioJasperPrintObjeto(superParametroRelVO)));
		jrpdfexporter.setExporterOutput(new SimpleOutputStreamExporterOutput(excelFile));
		jrpdfexporter.exportReport();
		jrpdfexporter = null;
		return excelFile.getName();

	}

	public String realizarExportacaoEXCEL(SuperParametroRelVO superRelVO, JasperPrint print) throws Exception {
		String nome = superRelVO.getNomeRelatorio() + ".xlsx";
		String nomeRel = "relatorio" + File.separator + nome;
		File excelFile = new File(getCaminhoPastaWeb() + File.separator + nomeRel);
		if (excelFile.exists()) {
			try {
				excelFile.delete();
			} catch (Exception e) {
				throw new Exception("Erro ao exportar arquivo em EXCEL.");
			}
		}

		JRXlsxExporter jrpdfexporter = new JRXlsxExporter();
		jrpdfexporter.setExporterInput(new SimpleExporterInput(print));
		jrpdfexporter.setExporterOutput(new SimpleOutputStreamExporterOutput(excelFile));
		jrpdfexporter.exportReport();
		nome = null;
		nomeRel = null;
		jrpdfexporter = null;
		return excelFile.getName();
		// return excelFile.getAbsolutePath();
	}

	public String realizarExportacaoHTML(SuperParametroRelVO superRelVO, JasperPrint print) throws Exception {
		String nome = superRelVO.getNomeRelatorio() + ".html";
		String nomeRel = "relatorio" + File.separator + nome;
		File htmlFile = new File(getCaminhoPastaWeb() + File.separator + nomeRel);
		if (htmlFile.exists()) {
			try {
				htmlFile.delete();
			} catch (Exception e) {
				throw new Exception("Erro ao exportar arquivo em HTML.");
			}
		}

		HtmlExporter jrhtmlexporter = new HtmlExporter();
		jrhtmlexporter.setExporterInput(new SimpleExporterInput(print));
		jrhtmlexporter.setExporterOutput(new SimpleHtmlExporterOutput(htmlFile));		
		jrhtmlexporter.exportReport();
		nome = null;
		nomeRel = null;
		jrhtmlexporter = null;
		return htmlFile.getName();
		// return htmlFile.getAbsolutePath();
	}

	public String realizarExportacaoDOC(SuperParametroRelVO superRelVO, JasperPrint print) throws Exception {
		String nome = superRelVO.getNomeRelatorio() + ".docx";
		String nomeRel = "relatorio" + File.separator + nome;
		File docFile = new File(getCaminhoPastaWeb() + File.separator + nomeRel);
		if (docFile.exists()) {
			try {
				docFile.delete();
			} catch (Exception e) {
				throw new Exception("Erro ao exportar arquivo em DOC.");
			}
		}
		JRDocxExporter jrDocxExporter = new JRDocxExporter();
		jrDocxExporter.setExporterInput(new SimpleExporterInput(print));
		jrDocxExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(docFile));
		jrDocxExporter.exportReport();
		nome = null;
		nomeRel = null;
		jrDocxExporter = null;
		return docFile.getName();
		// return docFile.getAbsolutePath();
	}

	public String realizarExportacaoIMAGEM(SuperParametroRelVO superRelVO, JasperPrint print) throws Exception {
		String nome = superRelVO.getNomeRelatorio() + ".jpg";
		String nomeRel = "relatorio" + File.separator + nome;
		File pdfFile = new File(getCaminhoPastaWeb() + File.separator + nomeRel);
		if (pdfFile.exists()) {
			try {
				pdfFile.delete();
			} catch (Exception e) {
				throw new Exception("Erro ao exportar arquivo em JPG.");
			}
		}
		JRPdfExporter jrpdfexporter = new JRPdfExporter();
		jrpdfexporter.setExporterInput(new SimpleExporterInput(print));
		jrpdfexporter.setExporterOutput(new SimpleOutputStreamExporterOutput(pdfFile));
		jrpdfexporter.exportReport();

		// //File pdfFile = new File(fileDir);
		// RandomAccessFile raf = new RandomAccessFile(pdfFile, "r");
		// FileChannel channel = raf.getChannel();
		// ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0,
		// channel.size());
		// PDFFile pdf = new PDFFile(buf);
		// PDFPage page = pdf.getPage(1);
		//
		// Rectangle rect = new Rectangle(0, 0, (int) page.getBBox().getWidth(),
		// (int) page.getBBox().getHeight());
		// BufferedImage bufferedImage = new BufferedImage(rect.width,
		// rect.height,
		// BufferedImage.TYPE_INT_RGB);
		//
		// Image image = page.getImage(rect.width, rect.height,
		// rect,
		// null,
		// true,
		// true);
		// Graphics2D bufImageGraphics = bufferedImage.createGraphics();
		// bufImageGraphics.drawImage(image, 0, 0, null);
		// ImageIO.write(bufferedImage, "jpg", new File(dir + "imagem.jpg"));
		//
		// nome = null;
		// nomeRel = null;
		// jrpdfexporter = null;
		//
		return pdfFile.getName();

		// return docFile.getAbsolutePath();
	}

	public String realizarExportacaoPPT(SuperParametroRelVO superRelVO, JasperPrint print) throws Exception {
		String nome = superRelVO.getNomeRelatorio() + ".pptx";
		String nomeRel = "relatorio" + File.separator + nome;
		File pptFile = new File(getCaminhoPastaWeb() + File.separator + nomeRel);
		if (pptFile.exists()) {
			try {
				pptFile.delete();
			} catch (Exception e) {
				throw new Exception("Erro ao exportar arquivo em PPT.");
			}
		}
		JRPptxExporter jrPptxExporter = new JRPptxExporter();
		jrPptxExporter.setExporterInput(new SimpleExporterInput(print));
		jrPptxExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(pptFile));
		jrPptxExporter.exportReport();
		nome = null;
		nomeRel = null;
		jrPptxExporter = null;
		return pptFile.getName();
		// return pptFile.getAbsolutePath();
	}
	
	private Exception excecaoTratada(Exception exception) {
		if (!Objects.isNull(exception) && exception instanceof MaxPagesGovernorException) {
			MaxPagesGovernorException maxPagesGovernorException = (MaxPagesGovernorException) exception;
			return new Exception("O TEXTO PADRÃO excedeu o limite de " + maxPagesGovernorException.getMaxPages() + " páginas. Procure o Administrador.");
		}
		return exception;
	}
}
