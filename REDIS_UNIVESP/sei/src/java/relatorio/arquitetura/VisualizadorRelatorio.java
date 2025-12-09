package relatorio.arquitetura;

import controle.arquitetura.LoginControle;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.faces.context.FacesContext;


import appletImpressaoMatricial.ArquivoHelper;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.crm.InteracaoWorkflowVO;
import negocio.comuns.crm.enumerador.TipoInteracaoEnum;
import negocio.comuns.crm.enumerador.TipoOrigemInteracaoEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.crm.InteracaoWorkflow;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import relatorio.negocio.comuns.financeiro.BoletoBancarioRelVO;

/**
 * Servlet responsável por gerar a visualização do relatório e apresentá-lo ao usuário. É utilizado por todos os relatórios do sistema com esta finalidade. É capaz de receber os
 * dados do relatório e compilar o relatório final utilizando o JasperReport.
 */
public class VisualizadorRelatorio extends HttpServlet {

    private HashMap parametrosRelatorio;
    private String nomeRelatorio;
    private String nomeEmpresa;
    private String nomeDesignIReport;
    private String caminhoParserXML;
    private String xmlDados;
    private String tipoRelatorio;
    private String mensagemRel;
    private String tipoImplementacao;
    private String caminhoBaseAplicacao;
    private String logoPadraoRelatorio;
    private String logoCliente;
    private List listaObjetos;
    private String diretorioWeb;
    private String nomePDF;
    private String caminhoBase;
    private String caminhoBaseWeb;

    private String persistirImpressaoBoleto;
    private String codigoContaReceber;
    private String parcela;
    private String codigoUsuario;

    /**
     * Rotina responsável por obter o diretório real da aplicação Web em execução. É importante acessar este diretório para que seja possível utilizar recursos existentes nos
     * pacotes da aplicação.
     */
    public String obterCaminhoBaseAplicacao() throws Exception {
        String caminhoBaseAplicacaoPrm = "";
        if (getCaminhoBase() == null) {
            caminhoBaseAplicacaoPrm = obterCaminhoWebAplicacao() + File.separator + "WEB-INF" + File.separator + "classes";
        } else {
            caminhoBaseAplicacaoPrm = getCaminhoBase();
        }
        return caminhoBaseAplicacaoPrm;
    }

    /**
     * Rotina responsável por obter o diretório real da aplicação Web em execução. É importante acessar este diretório para que seja possível utilizar recursos existentes nos
     * pacotes da aplicação.
     */
    private String obterCaminhoWebAplicacao() throws Exception {
        if (diretorioWeb == null || diretorioWeb.equals("")) {
            if (getCaminhoBaseWeb() == null) {
                ServletContext servletContext = (ServletContext) this.getServletContext();
                diretorioWeb = servletContext.getRealPath("");
            } else {
                return getCaminhoBaseWeb();
            }
        }
        return diretorioWeb;
    }

    public JasperPrint gerarRelatorioJasperPrintXML(HttpServletRequest request, HttpServletResponse response, String xmlDados, String caminhoParserXML, String nomeDesignIReport) throws Exception {
        JRXmlDataSource jrxmlds = null;
        String nomeJasperReportDesignIReport = null;
        File arquivoIReport = null;
        JasperReport jasperReport = null;
        try {
            jrxmlds = new JRXmlDataSource(new ByteArrayInputStream(xmlDados.getBytes()), caminhoParserXML);

            // File arquivoIReport = new File(obterCaminhoBaseAplicacao(request, response) + File.separator + nomeDesignIReport);
            // JasperDesign jasperDesign = JRXmlLoader.load( arquivoIReport.getAbsoluteFile() );

            // Gerando relatório jasperReport com datasource do tipo XML
            // JasperReport jasperReport = JasperCompileManager.compileReport( jasperDesign );
            nomeJasperReportDesignIReport = nomeDesignIReport.substring(0, nomeDesignIReport.lastIndexOf(".")) + ".jasper";
            arquivoIReport = new File(obterCaminhoBaseAplicacao() + File.separator + nomeJasperReportDesignIReport);

            jasperReport = (JasperReport) JRLoader.loadObject(arquivoIReport);

            JasperPrint print = JasperFillManager.fillReport(jasperReport, getParametrosRelatorio(), jrxmlds);
            return print;
        } catch (Exception e) {
            e.getMessage();
            return null;
        } finally {
            jrxmlds = null;
            nomeJasperReportDesignIReport = null;
            arquivoIReport = null;
            jasperReport = null;
        }
    }

    public JasperPrint gerarRelatorioJasperPrintObjeto(HttpServletRequest request, HttpServletResponse response, String nomeDesignIReport) throws Exception {
        JRDataSource jr = null;
        String nomeJasperReportDesignIReport = null;
        File arquivoIReport = null;
        JasperReport jasperReport = null;
        try {
            jr = new JRBeanArrayDataSource(getListaObjetos().toArray());
            nomeJasperReportDesignIReport = nomeDesignIReport.substring(0, nomeDesignIReport.lastIndexOf(".")) + ".jasper";
            arquivoIReport = new File(obterCaminhoBaseAplicacao() + File.separator + nomeJasperReportDesignIReport);

            jasperReport = (JasperReport) JRLoader.loadObject(arquivoIReport);

            JasperPrint print = JasperFillManager.fillReport(jasperReport, getParametrosRelatorio(), jr);

            return print;
        } catch (Exception e) {
            throw e;
        } catch (Throwable ex) {
            throw new Exception(ex);
        } finally {
            arquivoIReport = null;
            nomeJasperReportDesignIReport = null;
            jr = null;
            jasperReport = null;
//            getListaObjetos().clear();
        }

    }

    protected void visualizarRelatorioHTML(HttpServletRequest request,
            HttpServletResponse response,
            JasperPrint print) throws ServletException, IOException, Exception {
    	response.setContentType("text/html;charset=UTF-8");

        
        HtmlExporter jrhtmlexporter = new HtmlExporter();
        jrhtmlexporter.setExporterInput(new SimpleExporterInput(print));
		jrhtmlexporter.setExporterOutput(new SimpleHtmlExporterOutput(response.getWriter()));	
        
        jrhtmlexporter.exportReport();
    }

    public void visualizarRelatorioPDF(JasperPrint print, String origemRotina) throws ServletException, IOException, Exception {
        DateFormat formatador = null;
        String nomeRelPDF = null;
        File pdfFile = null;
        String dataStr = null;
        JRPdfExporter jrpdfexporter = null;
        try {
            long tempoEmMilissegundos = new Date().getTime();
            setNomePDF(getNomeRelatorio() + tempoEmMilissegundos + ".pdf");
            nomeRelPDF = "relatorio" + File.separator + getNomePDF();
            pdfFile = new File(obterCaminhoWebAplicacao() + File.separator + nomeRelPDF);

            if (pdfFile.exists()) {
                try {
                    if (!pdfFile.delete()) {
                        formatador = DateFormat.getDateInstance(DateFormat.MEDIUM);
                        dataStr = formatador.format(new Date());
                        setNomePDF(getNomeRelatorio() + dataStr + ".pdf");
                        nomeRelPDF = "relatorios" + File.separator + getNomePDF();
                        pdfFile = new File(obterCaminhoWebAplicacao() + File.separator + nomeRelPDF);
                    }
                } catch (Exception e) {
                    formatador = DateFormat.getDateInstance(DateFormat.MEDIUM);
                    dataStr = formatador.format(new Date());
                    setNomePDF(getNomeRelatorio() + dataStr + ".pdf");
                    nomeRelPDF = "relatorios" + File.separator + getNomePDF();
                    pdfFile = new File(obterCaminhoWebAplicacao() + File.separator + nomeRelPDF);
                }
            }
            try {
	            if (getPersistirImpressaoBoleto() != null && !getPersistirImpressaoBoleto().equals("")
	            		&& Boolean.valueOf(getPersistirImpressaoBoleto())) {
	            	persistirFollowUpContaReceber(origemRotina);
	            }
            } catch (Exception e) {
            	e.getMessage();
            }
            jrpdfexporter = new JRPdfExporter();
            jrpdfexporter.setExporterInput(new SimpleExporterInput(print));
    		jrpdfexporter.setExporterOutput(new SimpleOutputStreamExporterOutput(pdfFile));	
            jrpdfexporter.exportReport();
        } catch (Exception e) {
            throw e;
        } finally {
            formatador = null;
            nomeRelPDF = null;
            pdfFile = null;
            dataStr = null;
            jrpdfexporter = null;
            print = null;
        }
    }

    public void persistirFollowUpContaReceber(String origemRotina) throws Exception {
    	Iterator<BoletoBancarioRelVO> i = getListaObjetos().iterator();
    	while (i.hasNext()) {
    		BoletoBancarioRelVO boleto = i.next();
    		new InteracaoWorkflow().persistirFollowUpContaReceber(boleto, getCodigoUsuario(), origemRotina, getNomePDF());
    	}
    }
    
    protected void visualizarRelatorioPDF(HttpServletRequest request,
            HttpServletResponse response,
            JasperPrint print) throws ServletException, IOException, Exception {
        PrintWriter out = null;
        String urlAplicacao = null;
        try {
            if (request.getAttribute("realizarDownload") == null || request.getAttribute("realizarDownload").equals("NAO")) {
                response.setContentType("text/html;charset=ISO-8859-1");
                out = response.getWriter();
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Visualizador de Relatório</title>");
                out.println("</head>");

                visualizarRelatorioPDF(print, (String) request.getAttribute("origemImpressaoBoleto"));

                out.println("<frameset cols=\"*\" frameborder=\"NO\" border=\"0\" framespacing=\"0\">");
                urlAplicacao = request.getRequestURI().toString();
                urlAplicacao = urlAplicacao.substring(0, urlAplicacao.lastIndexOf("/"));
                out.println("<frame src=\"" + urlAplicacao + "/relatorio/" + getNomePDF() + "\" name=\"mainFrame\">");
                out.println("</frameset>");
                out.println("<noframes><body>");
                out.println("</body></noframes>");
                out.println("</html>");
            } else {
            	visualizarRelatorioPDF(print, (String) request.getAttribute("origemImpressaoBoleto"));
                File file = new File(UteisJSF.getCaminhoWeb() + "/relatorio/" + getNomePDF());                
                String userAgent = request.getHeader("User-Agent");
    			if(Uteis.ClienteMovel(userAgent)){
    				response.setHeader("Content-Disposition", "inline;filename=" + getNomePDF());
    			}else {
    				response.setHeader("Content-Disposition", "attachment;filename=" + getNomePDF());
    			}
                
                response.setHeader("Content-Disposition", "attachment;filename=" + getNomePDF());
                response.setContentType("application/pdf");
                ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
                FileInputStream fi = null;
                byte buffer[] = null;
                try {
                    buffer = new byte[4096];
                    int bytesRead = 0;
                    fi = new FileInputStream(file.getAbsolutePath());
                    while ((bytesRead = fi.read(buffer)) != -1) {
                        arrayOutputStream.write(buffer, 0, bytesRead);
                    }
                    arrayOutputStream.close();
                    fi.close();
                    arrayOutputStream.flush();
                    OutputStream out2 = response.getOutputStream();
                    out2.write(arrayOutputStream.toByteArray());
                    out2.flush();
                } catch (Exception e) {
                    throw e;
                } finally {
                    fi = null;
                    buffer = null;
                }

            }
        } catch (Exception e) {
            throw e;
        } finally {
			if (out != null) {
				out.close();
			}
            urlAplicacao = null;
            print = null;
        }
    }

    protected void inicializarParametrosRelatorio(HttpServletRequest request, HttpServletResponse response) throws Exception {
        setNomeRelatorio((String) request.getAttribute("nomeRelatorio"));
        setNomeEmpresa((String) request.getAttribute("nomeEmpresa"));
        setXmlDados((String) request.getAttribute("xmlRelatorio"));
        setNomeDesignIReport((String) request.getAttribute("nomeDesignIReport"));
        setCaminhoParserXML((String) request.getAttribute("caminhoParserXML"));
        setTipoRelatorio((String) request.getAttribute("tipoRelatorio"));
        setMensagemRel((String) request.getAttribute("mensagemRel"));
        setListaObjetos((List) request.getAttribute("listaObjetos"));
        setTipoImplementacao((String) request.getAttribute("tipoImplementacao"));
        setCaminhoBaseAplicacao((String) request.getAttribute("caminhoBaseRelatorio"));
        setLogoPadraoRelatorio((String) request.getAttribute("logoPadraoRelatorio"));
        setLogoCliente((String) request.getAttribute("logoCliente"));

        setPersistirImpressaoBoleto((String) request.getAttribute("persistirImpressaoBoleto"));
        setCodigoUsuario((String) request.getAttribute("codigoUsuario"));
        if ((getTipoRelatorio() == null) || getTipoRelatorio().equals("")) {
            setTipoRelatorio("HTML");
        }
        HashMap parameters = new HashMap();
        String caminhoImagemLogo = "resources" + File.separator + "imagens" + File.separator + "logoPadraoRelatorio.png";
        String caminhoLogoCliente = "resources" + File.separator + "imagens" + File.separator + "logo.png";
        //parameters.put("logoCliente", obterCaminhoWebAplicacao() + File.separator + caminhoLogoCliente);
        if (FacesContext.getCurrentInstance() != null) {
            LoginControle loginControle = (LoginControle) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("LoginControle");
            if (loginControle != null && !loginControle.getUrlLogoUnidadeEnsinoRelatorio().trim().isEmpty()) {
                parameters.put("logoPadraoRelatorio", loginControle.getUrlFisicoLogoUnidadeEnsinoRelatorio());
                parameters.put("logoCliente", loginControle.getUrlFisicoLogoUnidadeEnsino());
            }else if(Uteis.isAtributoPreenchido(getLogoPadraoRelatorio()) || Uteis.isAtributoPreenchido(getLogoCliente())){
            	if(Uteis.isAtributoPreenchido(getLogoPadraoRelatorio())){
            		parameters.put("logoPadraoRelatorio",getLogoPadraoRelatorio());	
            	}
            	if(Uteis.isAtributoPreenchido(getLogoCliente())){
            		parameters.put("logoCliente",getLogoCliente());	
            	}
            }else {
            	parameters.put("logoPadraoRelatorio", obterCaminhoWebAplicacao() + File.separator + caminhoImagemLogo);    
                parameters.put("logoCliente", obterCaminhoWebAplicacao() + File.separator + caminhoLogoCliente);
            }
        } else {
        		if(Uteis.isAtributoPreenchido(getLogoPadraoRelatorio())){
            		parameters.put("logoPadraoRelatorio",getLogoPadraoRelatorio());	
            	}else{
            		parameters.put("logoPadraoRelatorio", obterCaminhoWebAplicacao() + File.separator + caminhoImagemLogo);
            	}
            	if(Uteis.isAtributoPreenchido(getLogoCliente())){
            		parameters.put("logoCliente",getLogoCliente());	
            	}else{
            		parameters.put("logoCliente", obterCaminhoWebAplicacao() + File.separator + caminhoLogoCliente);
            	}
        }
        //parameters.put("logoPadraoRelatorio", obterCaminhoWebAplicacao() + File.separator + caminhoImagemLogo);
        parameters.put("pastaPadraoImagens", obterCaminhoWebAplicacao() + File.separator + "resources" + File.separator + "imagens" + File.separator);
        parameters.put("tituloRelatorio", (String) request.getAttribute("tituloRelatorio"));
        parameters.put("usuario", (String) request.getAttribute("nomeUsuario"));
        parameters.put("filtros", (String) request.getAttribute("filtros"));
        parameters.put("nomeEmpresa", getNomeEmpresa());
        parameters.put("mensagemRel", getMensagemRel());
        parameters.put("versaoSoftware", getServletContext().getInitParameter("versaoSoftware"));
        parameters.put("SUBREPORT_DIR", obterCaminhoBaseAplicacao() + File.separator + getCaminhoBaseAplicacao());
        Integer total = (Integer) request.getAttribute("total");
        if (total == null) {
            total = 0;
        }
        parameters.put("total", total);

        String parametro1 = (String) request.getAttribute("parametro1");
        if (parametro1 == null) {
            parametro1 = "";
        }
        parameters.put("parametro1", parametro1);

        String parametro2 = (String) request.getAttribute("parametro2");
        if (parametro2 == null) {
            parametro2 = "";
        }
        parameters.put("parametro2", parametro2);

        String parametro3 = (String) request.getAttribute("parametro3");
        if (parametro3 == null) {
            parametro3 = "";
        }
        parameters.put("parametro3", parametro3);
        setParametrosRelatorio(parameters);
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * 
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {
        JasperPrint print = null;
        try {
            inicializarParametrosRelatorio(request, response);

            if (tipoImplementacao == null) {
                tipoImplementacao = "";
            }
            if (tipoImplementacao.equals("")) {
                print = gerarRelatorioJasperPrintXML(request, response, getXmlDados(), getCaminhoParserXML(), getNomeDesignIReport());
            } else {
                print = gerarRelatorioJasperPrintObjeto(request, response, getNomeDesignIReport());
            }

            if (getTipoRelatorio().equals("PDF")) {
                visualizarRelatorioPDF(request, response, print);
            } else {
                visualizarRelatorioHTML(request, response, print);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            print = null;
            getListaObjetos().clear();
            Uteis.removerObjetoMemoria(this);
        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     * 
     * @param request servlet request
     * @param response servlet response
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception e) {
            // System.out.println("MENSAGEM => " + e.getMessage());
            throw new ServletException(e.getMessage());
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     * 
     * @param request servlet request
     * @param response servlet response
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception e) {
            // System.out.println("MENSAGEM => " + e.getMessage());
            throw new ServletException(e.getMessage());
        }
    }

    /**
     * Returns a short description of the servlet.
     */
    @Override
    public String getServletInfo() {
        return "Servlet Responsável por gerar o RELATORIO";
    }

    // </editor-fold>
    public HashMap getParametrosRelatorio() {
        return parametrosRelatorio;
    }

    public void setParametrosRelatorio(HashMap parametrosRelatorio) {
        this.parametrosRelatorio = parametrosRelatorio;
    }

    public String getNomeDesignIReport() {
        return nomeDesignIReport;
    }

    public void setNomeDesignIReport(String nomeDesignIReport) {
        this.nomeDesignIReport = nomeDesignIReport;
    }

    public String getCaminhoParserXML() {
        return caminhoParserXML;
    }

    public void setCaminhoParserXML(String caminhoParserXML) {
        this.caminhoParserXML = caminhoParserXML;
    }

    public String getCaminhoBaseAplicacao() {
        return caminhoBaseAplicacao;
    }

    public void setCaminhoBaseAplicacao(String caminhoBaseAplicacao) {
        this.caminhoBaseAplicacao = caminhoBaseAplicacao;
    }

    public String getXmlDados() {
        return xmlDados;
    }

    public void setXmlDados(String xmlDados) {
        this.xmlDados = xmlDados;
    }

    public String getTipoRelatorio() {
        return tipoRelatorio;
    }

    public void setTipoRelatorio(String tipoRelatorio) {
        this.tipoRelatorio = tipoRelatorio;
    }

    public String getNomeRelatorio() {
        return nomeRelatorio;
    }

    public void setNomeRelatorio(String nomeRelatorio) {
        this.nomeRelatorio = nomeRelatorio;
    }

    public String getNomeEmpresa() {
        return nomeEmpresa;
    }

    public void setNomeEmpresa(String nomeEmpresa) {
        this.nomeEmpresa = nomeEmpresa;
    }

    public String getMensagemRel() {
        return mensagemRel;
    }

    public void setMensagemRel(String mensagemRel) {
        this.mensagemRel = mensagemRel;
    }

    public String getTipoImplementacao() {
        return tipoImplementacao;
    }

    public void setTipoImplementacao(String tipoImplementacao) {
        this.tipoImplementacao = tipoImplementacao;
    }

    public List getListaObjetos() {
        return listaObjetos;
    }

    public void setListaObjetos(List listaObjetos) {
        this.listaObjetos = listaObjetos;
    }

    /**
     * @return the nomePDF
     */
    public String getNomePDF() {
        return nomePDF;
    }

    /**
     * @param nomePDF the nomePDF to set
     */
    public void setNomePDF(String nomePDF) {
        this.nomePDF = nomePDF;
    }

    /**
     * @return the caminhoBase
     */
    public String getCaminhoBase() {
        return caminhoBase;
    }

    /**
     * @param caminhoBase the caminhoBase to set
     */
    public void setCaminhoBase(String caminhoBase) {
        this.caminhoBase = caminhoBase;
    }

    /**
     * @return the caminhoBaseWeb
     */
    public String getCaminhoBaseWeb() {
        return caminhoBaseWeb;
    }

    /**
     * @param caminhoBaseWeb the caminhoBaseWeb to set
     */
    public void setCaminhoBaseWeb(String caminhoBaseWeb) {
        this.caminhoBaseWeb = caminhoBaseWeb;
    }

	public String getLogoPadraoRelatorio() {
		return logoPadraoRelatorio;
	}

	public void setLogoPadraoRelatorio(String logoUnidadeEnsino) {
		this.logoPadraoRelatorio = logoUnidadeEnsino;
	}

	public String getLogoCliente() {
		return logoCliente;
	}

	public void setLogoCliente(String logoCliente) {
		this.logoCliente = logoCliente;
	}

	public String getPersistirImpressaoBoleto() {
		return persistirImpressaoBoleto;
	}

	public void setPersistirImpressaoBoleto(String persistirImpressaoBoleto) {
		this.persistirImpressaoBoleto = persistirImpressaoBoleto;
	}

	public String getCodigoContaReceber() {
		return codigoContaReceber;
	}

	public void setCodigoContaReceber(String codigoContaReceber) {
		this.codigoContaReceber = codigoContaReceber;
	}

	public String getCodigoUsuario() {
		return codigoUsuario;
	}

	public void setCodigoUsuario(String codigoUsuario) {
		this.codigoUsuario = codigoUsuario;
	}

	public String getParcela() {
		return parcela;
	}

	public void setParcela(String parcela) {
		this.parcela = parcela;
	}
	
    
    
}
