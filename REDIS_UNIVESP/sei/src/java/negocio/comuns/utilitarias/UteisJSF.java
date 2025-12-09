package negocio.comuns.utilitarias;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.ajax4jsf.util.HtmlColor;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.TipoUtilizacaoCorEnum;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import relatorio.negocio.comuns.arquitetura.SuperParametroRelVO;

public class UteisJSF implements Serializable {

	public static String getCaminhoWeb() throws Exception {
		String caminhoWeb = "";
		caminhoWeb = UteisJSF.class.getResource("").getPath();
		caminhoWeb = URLDecoder.decode(caminhoWeb, "UTF-8");
		int p = caminhoWeb.indexOf("WEB-INF");
		if (p == -1) {
			p = caminhoWeb.indexOf("build");
		}
		caminhoWeb = caminhoWeb.substring(0, p);
		if(Uteis.isSistemaOperacionalWindows() && caminhoWeb.startsWith("/")) {
			caminhoWeb = caminhoWeb.substring(1, caminhoWeb.length());
		}
		// String caminhoWeb2 = caminhoWeb.replace("/", File.separator);
		return caminhoWeb;
	}

	public static String getCaminhoWebFotos() throws Exception {
		String caminhoWeb = "";
		caminhoWeb = UteisJSF.class.getResource("").getPath();
		caminhoWeb = URLDecoder.decode(caminhoWeb, "UTF-8");
		caminhoWeb = getCaminhoWeb() + "WEB-INF" + File.separator + "imagem";
		return caminhoWeb;
	}

	public static String getCaminhoWebRelatorio() throws Exception {
		String caminhoWebRelatorio = "";
		caminhoWebRelatorio = getCaminhoWeb() + File.separator + "relatorio";
		return caminhoWebRelatorio;
	}

	public static String getCaminhoBase() throws Exception {
		String caminhoBase = "";
		try {
			if ((caminhoBase == null || caminhoBase.equals("")) && getCaminhoWeb() != null && !getCaminhoWeb().equals("")) {
				caminhoBase = getCaminhoWeb() + "WEB-INF" + File.separator + "classes";
			}
		} catch (Exception e) {
			caminhoBase = "";
		}
		return caminhoBase;
	}

	public static String internacionalizar(String mensagem) {
		String propriedade = obterArquivoPropriedades(mensagem);
		ResourceBundle bundle = ResourceBundle.getBundle(propriedade, getLocale(), getCurrentLoader(propriedade));
		try {
			return bundle.getString(mensagem);
		} catch (MissingResourceException e) {
			return mensagem;
		}
	}

	public static String obterArquivoPropriedades(String mensagem) {
		if (mensagem.startsWith("msg")) {
			return "propriedades.Mensagens";
		} else if (mensagem.startsWith("enum")) {
			return "propriedades.Enum_pt_BR";
		} else if (mensagem.startsWith("prt")) {
			return "propriedades.Aplicacao";
		} else if (mensagem.startsWith("Menu") || mensagem.startsWith("menu")) {
			return "propriedades.Menu";
		} else if (mensagem.startsWith("btn")) {
			return "propriedades.Botoes";
		} else if (mensagem.startsWith("dominio")) {
			return "propriedades.Dominio";
		} else if (mensagem.startsWith("per")) {
			return "propriedades.Permissao";
		} else if (mensagem.startsWith("parametrosConsulta")) {
			return "propriedades.ParametrosConsulta";
		} else {
			return "propriedades.Mensagens";
		}
	}

	public static String getLimitarTamanhoVideo(String video, Integer tamanhoMaximoWidth, Integer tamanhoMaximoHeigth) {
		if (video.contains("width") && video.contains("height")) {
			String width = video.substring(video.indexOf("width=\"") + 7, video.indexOf("width=\"") + 7 + video.substring(video.indexOf("width=\"") + 7).indexOf("\""));
			String heigth = video.substring(video.indexOf("height=\"") + 8, video.indexOf("height=\"") + 8 + video.substring(video.indexOf("height=\"") + 8).indexOf("\""));
			if (new Integer(width) > tamanhoMaximoWidth) {
				video = (video.replaceAll("width=\"" + width + "\"", "width=\"" + tamanhoMaximoWidth + "\""));
			}
			if (new Integer(heigth) > tamanhoMaximoHeigth) {
				video = (video.replaceAll("height=\"" + heigth + "\"", "height=\"" + tamanhoMaximoHeigth + "\""));
			}
		}
		return video;
	}

	public static Locale getLocale() {
		if (context() != null && context().getViewRoot() != null && context().getViewRoot().getLocale() != null) {
			return context().getViewRoot().getLocale();
		}
		return new Locale("pt", "BR");
	}

	public static FacesContext context() {
		return FacesContext.getCurrentInstance();
	}

	public static ClassLoader getCurrentLoader(Object fallbackClass) {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		if (loader == null) {
			loader = fallbackClass.getClass().getClassLoader();
		}
		return loader;
	}

	public static File realizarGeracaoArquivoPDF(UsuarioVO usuarioVO, Long projeto, List listaObjetos, String titulo, String nomeRelatorio, String caminhoDesignRelatorio, String caminhoBaseRelatorio, byte[] logoProjeto) {
		long tempoEmMilissegundos = new Date().getTime();
		nomeRelatorio = "relatorio" + File.separator + nomeRelatorio + tempoEmMilissegundos + ".pdf";
		try {
			File pdfFile = new File(getCaminhoWeb() + File.separator + nomeRelatorio);
			if (pdfFile.exists()) {
				pdfFile.delete();
			}
			JRPdfExporter jrpdfexporter = new JRPdfExporter();
			jrpdfexporter.setParameter(JRExporterParameter.JASPER_PRINT, gerarRelatorioJasperPrintObjeto(listaObjetos, caminhoDesignRelatorio, inicializarParametroBase(usuarioVO, projeto, titulo, caminhoBaseRelatorio, logoProjeto)));
			jrpdfexporter.setParameter(JRExporterParameter.OUTPUT_FILE, pdfFile);
			jrpdfexporter.exportReport();
			return pdfFile;
		} catch (Exception e) {
			return null;
		} finally {
			nomeRelatorio = null;
			caminhoDesignRelatorio = null;
			nomeRelatorio = null;
		}
	}

	public static File realizarGeracaoArquivoPDF(SuperParametroRelVO superParametroRelVO) {

		try {
			File pdfFile = new File(getCaminhoWeb() + File.separator + "relatorio" + File.separator + superParametroRelVO.getNomeRelatorio() + ".pdf");
			if (pdfFile.exists()) {
				pdfFile.delete();
			}
			JRPdfExporter jrpdfexporter = new JRPdfExporter();
			/*jrpdfexporter.setParameter(JRExporterParameter.JASPER_PRINT, gerarRelatorioJasperPrintObjeto(superParametroRelVO.getListaObjetos(), superParametroRelVO.getNomeDesignIreport(), superParametroRelVO.getParametros()));
			jrpdfexporter.setParameter(JRExporterParameter.OUTPUT_FILE, pdfFile);*/
			jrpdfexporter.setExporterInput(new SimpleExporterInput(gerarRelatorioJasperPrintObjeto(superParametroRelVO.getListaObjetos(), superParametroRelVO.getNomeDesignIreport(), superParametroRelVO.getParametros())));
			jrpdfexporter.setExporterOutput(new SimpleOutputStreamExporterOutput(pdfFile));		
			jrpdfexporter.exportReport();
			return pdfFile;
		} catch (Exception e) {
			return null;
		} finally {

		}
	}

	public static File realizarGeracaoArquivoPDF2(UsuarioVO usuarioVO, Long projeto, List listaObjetos, String titulo, String nomeRelatorio, String caminhoDesignRelatorio, String caminhoBaseRelatorio, byte[] logoProjeto, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) {
		long tempoEmMilissegundos = new Date().getTime();
		nomeRelatorio = nomeRelatorio + tempoEmMilissegundos + ".pdf";
		try {
			File dir = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator + PastaBaseArquivoEnum.ARQUIVO_TMP.getValue());
			File fileArquivo = new File(dir.getPath() + File.separator + nomeRelatorio);

			JRPdfExporter jrpdfexporter = new JRPdfExporter();
			jrpdfexporter.setParameter(JRExporterParameter.JASPER_PRINT, gerarRelatorioJasperPrintObjeto2(listaObjetos, dir.getPath() + File.separator + nomeRelatorio, inicializarParametroBase(usuarioVO, projeto, titulo, dir.getPath() + File.separator + nomeRelatorio, logoProjeto)));
			jrpdfexporter.setParameter(JRExporterParameter.OUTPUT_FILE, fileArquivo);
			jrpdfexporter.exportReport();
			return fileArquivo;
		} catch (Exception e) {
			return null;
		} finally {
			nomeRelatorio = null;
			caminhoDesignRelatorio = null;
			nomeRelatorio = null;
		}
	}

	private static HashMap inicializarParametroBase(UsuarioVO usuarioVO, Long projeto, String titulo, String caminhoBaseRelatorio, byte[] logoProjeto) throws Exception {
		HashMap parameters = new HashMap();
		// parameters.put("logoPadraoOtimizeRelatorio",
		// Uteis.getImagem("logoPadraoOtimizeRelatorio.png", getCaminhoWeb()));
		// parameters.put("rodapePadraoRelatorio",
		// Uteis.getImagem("rodapePadraoRelatorio.png", getCaminhoWeb()));
		if (logoProjeto != null) {
			InputStream fs = new ByteArrayInputStream(logoProjeto);
			parameters.put("logoPadraoProjetoRelatorio", fs);
		}
		parameters.put("tituloRelatorio", titulo);
		if (usuarioVO != null) {
			parameters.put("usuario", usuarioVO.getNome());
		}
		parameters.put("SUBREPORT_DIR", getCaminhoBase() + File.separator + caminhoBaseRelatorio);
		return parameters;
	}

	private static JasperPrint gerarRelatorioJasperPrintObjeto(List listaObjetos, String nomeDesignIReport, HashMap parametrosRelatorio) throws Exception {
		JRDataSource jr = new JRBeanArrayDataSource(listaObjetos.toArray());
		String nomeJasperReportDesignIReport = nomeDesignIReport.substring(0, nomeDesignIReport.lastIndexOf(".")) + ".jasper";
		File arquivoIReport = new File(getCaminhoBase() + File.separator + nomeJasperReportDesignIReport);

		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(arquivoIReport);
		jasperReport.setProperty("net.sf.jasperreports.awt.ignore.missing.font", "true");
		jasperReport.setProperty("net.sf.jasperreports.default.pdf.font.name", "Arial");
		JasperPrint print = JasperFillManager.fillReport(jasperReport, parametrosRelatorio, jr);

		return print;
	}

	private static JasperPrint gerarRelatorioJasperPrintObjeto2(List listaObjetos, String nomeDesignIReport, HashMap parametrosRelatorio) throws Exception {
		JRDataSource jr = new JRBeanArrayDataSource(listaObjetos.toArray());
		String nomeJasperReportDesignIReport = nomeDesignIReport.substring(0, nomeDesignIReport.lastIndexOf(".")) + ".jasper";
		File arquivoIReport = new File(nomeJasperReportDesignIReport);

		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(arquivoIReport);
		jasperReport.setProperty("net.sf.jasperreports.awt.ignore.missing.font", "true");
		jasperReport.setProperty("net.sf.jasperreports.default.pdf.font.name", "Arial");
		JasperPrint print = JasperFillManager.fillReport(jasperReport, parametrosRelatorio, jr);

		return print;
	}

	public static String obterCaminhoWebFotos() throws Exception {
		return getCaminhoPastaWeb() + File.separator + "resources" + File.separator + "imagem";
	}

	public static String obterCaminhoWebImagem() throws Exception {
		return getCaminhoPastaWeb() + File.separator + "resources" + File.separator + "imagens";
	}

	public static String obterCaminhoWebImagemClass() throws Exception {
		String caminhoWeb = UteisJSF.class.getResource("").getPath();
		caminhoWeb = URLDecoder.decode(caminhoWeb, "UTF-8");
		if (caminhoWeb.indexOf("WEB-INF") > 0) {
			caminhoWeb = caminhoWeb.substring(0, caminhoWeb.indexOf("WEB-INF")) + File.separator + "resources" + File.separator +"imagens";
		}
		return caminhoWeb;
	}

	public static String getCaminhoPastaArquivosCenso() throws Exception {
		return getCaminhoPastaWeb() + File.separator + "arquivos";
	}

	public static String getCaminhoPastaArquivo() throws Exception {
		return getCaminhoPastaWeb() + File.separator + "arquivo";
	}

	public static String getCaminhoPastaArquivosCobranca() {
		return getCaminhoPastaWeb() + File.separator + "arquivos";
	}

	public static String getCaminhoPastaWeb() {
		String diretorioPastaWeb = "";
		ServletContext servletContext = (ServletContext) UteisJSF.context().getExternalContext().getContext();
		diretorioPastaWeb = servletContext.getRealPath("");
		return diretorioPastaWeb;
	}

	public static String getAcessoAplicadoURL() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		return request.getRequestURL().toString();
	}

	public static Map<TipoUtilizacaoCorEnum, String> obterCoresAleatoriasFundoETexto() {
		Map<TipoUtilizacaoCorEnum, String> cores = new HashMap<TipoUtilizacaoCorEnum, String>(0);

		Color color = Uteis.gerarCorAleatoria(Color.blue);
		cores.put(TipoUtilizacaoCorEnum.FUNDO, HtmlColor.encodeRGB(color));
		if (color.getBlue() + color.getGreen() + color.getRed() < ((255 + 255 + 255) / 2)) {
			cores.put(TipoUtilizacaoCorEnum.TEXTO, "#FFFFFF");
		} else {
			cores.put(TipoUtilizacaoCorEnum.TEXTO, "#000000");
		}
		return cores;
	}

	public static String getCaminhoWebNFe() throws Exception {
		return getCaminhoWeb() + File.separator + PastaBaseArquivoEnum.NFE;
	}

	public static HttpServletRequest request() {
		return ((HttpServletRequest) context().getExternalContext().getRequest());
	}

	public static String getUrlNFe() {
		String urlNFe = "";
		if (urlNFe == null) {
			String ip = request().getLocalAddr();
			int port = request().getLocalPort();
			urlNFe = "http://" + ip + ":" + port + request().getContextPath() + "/" + PastaBaseArquivoEnum.NFE + "/";
		}
		return urlNFe;
	}

	public static String getCaminhoPastaCertificado(UnidadeEnsinoVO unidadeEnsino) throws Exception {
		String caminhoPastaCertificado = "";
		if (caminhoPastaCertificado == null) {
			caminhoPastaCertificado = getCaminhoWebNFe() + File.separator + unidadeEnsino.getCodigo() + File.separator + unidadeEnsino.getCidade().getEstado().getSigla();
		}
		return caminhoPastaCertificado;
	}

	public static String getCaminhoCertificado(UnidadeEnsinoVO unidadeEnsino) throws Exception {
		String caminhoCertificado = "";
		if (caminhoCertificado == null) {
			caminhoCertificado = getCaminhoWebNFe() + File.separator + unidadeEnsino.getCodigo() + File.separator + unidadeEnsino.getCidade().getEstado().getSigla() + File.separator + "certificado.pfx";
		}
		return caminhoCertificado;
	}

	public static String getCaminhoClassesAplicacao() throws Exception {
		String caminhoClassesAplicacao = "";
		if (caminhoClassesAplicacao == null) {
			caminhoClassesAplicacao = getCaminhoPastaWeb() + File.separator + "WEB-INF" + File.separator + "classes";
		}
		return caminhoClassesAplicacao;
	}
	
	public static String getUrlAplicacaoExterna() {
		return request().getRequestURL().toString().substring(0, request().getRequestURL().toString().indexOf(request().getContextPath())) + request().getContextPath();
	}
	
	public static String getParametrosSistema(String mensagem) {
        ResourceBundle bundle = ResourceBundle.getBundle("propriedades.sistema", getLocale(), getCurrentLoader("propriedades.sistema"));
        try {
            return bundle.getString(mensagem);
        } catch (MissingResourceException e) {
            return mensagem;
        }
    }
	
	public static String internacionalizarEnum(Enum enumerador) {
		return internacionalizar("enum_" + enumerador.getClass().getSimpleName() + "_" + enumerador.toString());
	}
	
}
