/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package controle.academico;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 *
 * @author Otimize-Not
 */
public class VisualizadorHorarioTurma extends HttpServlet {

    private HashMap parametrosRelatorio;
    private String nomeRelatorio;
    private String nomeEmpresa;
    private String nomeDesignIReport;
    private String caminhoParserXML;
    private String xmlDados;
    private String tipoRelatorio;
    private String mensagemRel;
    private String tipoImplementacao;
    private List listaObjetos;
    private String caminhoBaseAplicacao;
    private String diretorioWeb;

    /**
     * Rotina responsável por obter o diretório real da aplicação Web em execução.
     * É importante acessar este diretório para que seja possível utilizar recursos
     * existentes nos pacotes da aplicação.
     */
	public String obterCaminhoBaseAplicacao(HttpServletRequest request, HttpServletResponse response) throws Exception {

		if (getDiretorioWeb() == null || getDiretorioWeb().equals("")) {
			ServletContext servletContext = (ServletContext) this.getServletContext();
			setDiretorioWeb(servletContext.getRealPath(""));
                        setDiretorioWeb(getDiretorioWeb() + (File.separator + "WEB-INF" + File.separator + "classes"));
		}                
		return getDiretorioWeb() + (File.separator + "WEB-INF" + File.separator + "classes");
	}

    /**
     * Rotina responsável por obter o diretório real da aplicação Web em execução.
     * É importante acessar este diretório para que seja possível utilizar recursos
     * existentes nos pacotes da aplicação.
     */
	public String obterCaminhoWebAplicacao(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (diretorioWeb == null || diretorioWeb.equals("")) {
			ServletContext servletContext = (ServletContext) this.getServletContext();
			diretorioWeb = servletContext.getRealPath("");
		}
		return diretorioWeb;
	}

    public JasperPrint gerarRelatorioJasperPrintXML(HttpServletRequest request, HttpServletResponse response, String xmlDados, String caminhoParserXML, String nomeDesignIReport) throws Exception {
        JRXmlDataSource jrxmlds = new JRXmlDataSource(new ByteArrayInputStream(xmlDados.getBytes(), 0, xmlDados.length()), caminhoParserXML);

        //File arquivoIReport = new File(obterCaminhoBaseAplicacao(request, response) + File.separator + nomeDesignIReport);
        //JasperDesign jasperDesign = JRXmlLoader.load( arquivoIReport.getAbsoluteFile() );

        // Gerando relatório jasperReport com datasource do tipo XML
        //JasperReport jasperReport = JasperCompileManager.compileReport( jasperDesign );
        String nomeJasperReportDesignIReport = nomeDesignIReport.substring(0, nomeDesignIReport.lastIndexOf(".")) + ".jasper";
        File arquivoIReport = new File(obterCaminhoBaseAplicacao(request, response) + File.separator + nomeJasperReportDesignIReport);
        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(arquivoIReport);
        JasperPrint print = JasperFillManager.fillReport(jasperReport, getParametrosRelatorio(), jrxmlds);
        return print;
    }

    public JasperPrint gerarRelatorioJasperPrintObjeto(HttpServletRequest request, HttpServletResponse response, String nomeDesignIReport) throws Exception {

        JRDataSource jr = new JRBeanArrayDataSource(getListaObjetos().toArray());
        String nomeJasperReportDesignIReport = nomeDesignIReport.substring(0, nomeDesignIReport.lastIndexOf(".")) + ".jasper";
        File arquivoIReport = new File(obterCaminhoBaseAplicacao(request, response) + File.separator + nomeJasperReportDesignIReport);

        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(arquivoIReport);

        JasperPrint print = JasperFillManager.fillReport(jasperReport, getParametrosRelatorio(), jr);

        return print;
    }

     protected void visualizarRelatorioPDF(HttpServletRequest request,
            HttpServletResponse response,
            JasperPrint print) throws ServletException, IOException, Exception {
        response.setContentType("text/html;charset=ISO-8859-1");
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Visualizador de Relatório</title>");
        out.println("</head>");

        String nomePDF = getNomeRelatorio() + ".pdf";
        String nomeRelPDF = "relatorio" + File.separator + nomePDF;
        //System.out.println("THYAGO 4 " + nomePDF);
        //System.out.println("THYAGO 4 " + obterCaminhoWebAplicacao(request, response) + File.separator + nomeRelPDF);
        File pdfFile = new File(obterCaminhoWebAplicacao(request, response) + File.separator + nomeRelPDF);        
        
        if (pdfFile.exists()) {
            try {
                if (!pdfFile.delete()) {
                	//System.out.println("THYAGO DELETE ");
                    DateFormat formatador = DateFormat.getDateInstance(DateFormat.MEDIUM);
                    String dataStr = formatador.format(new Date());
                    nomePDF = getNomeRelatorio() + dataStr + ".pdf";
                    nomeRelPDF = "relatorio" + File.separator + nomePDF;
                    pdfFile = new File(obterCaminhoWebAplicacao(request, response) + File.separator + nomeRelPDF);
                }
            } catch (Exception e) {
            	//System.out.println("THYAGO ERRO " + e.getMessage());
                DateFormat formatador = DateFormat.getDateInstance(DateFormat.MEDIUM);
                String dataStr = formatador.format(new Date());
                nomePDF = getNomeRelatorio() + dataStr + ".pdf";
                nomeRelPDF = "relatorio" + File.separator + nomePDF;
                pdfFile = new File(obterCaminhoWebAplicacao(request, response) + File.separator + nomeRelPDF);
            }

        }

        JRPdfExporter jrpdfexporter = new JRPdfExporter();
        jrpdfexporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
        jrpdfexporter.setParameter(JRExporterParameter.OUTPUT_FILE, pdfFile);

        jrpdfexporter.exportReport();

        out.println("<frameset cols=\"*\" frameborder=\"NO\" border=\"0\" framespacing=\"0\">");
        String urlAplicacao = request.getRequestURI().toString();
        urlAplicacao = urlAplicacao.substring(0, urlAplicacao.lastIndexOf("/"));
        out.println("<frame src=\"" + urlAplicacao + "/relatorio/" + nomePDF + "\" name=\"mainFrame\">");
        out.println("</frameset>");
        out.println("<noframes><body>");
        out.println("</body></noframes>");
        out.println("</html>");
        out.close();
    }

    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {
        setNomeRelatorio((String) request.getSession().getAttribute("nomeRelatorio"));
        //System.out.println("THYAGO 1 " + (String) request.getSession().getAttribute("nomeRelatorio"));
        setNomeEmpresa((String) request.getSession().getAttribute("nomeEmpresa"));
        setNomeDesignIReport((String) request.getSession().getAttribute("nomeDesignIReport"));
        setListaObjetos((List) request.getSession().getAttribute("listaObjetos"));
        //System.out.println("THYAGO 2 ");
        setCaminhoBaseAplicacao((String)request.getSession().getAttribute("caminhoBaseAplicacao"));
        HashMap parameters = new HashMap();
        String caminhoImagemLogo = "resources" + File.separator + "imagens" + File.separator + "logoPadraoRelatorio.png";
        String caminhoLogoCliente = "resources" + File.separator + "imagens" + File.separator + "logo.png";
        parameters.put("logoCliente", obterCaminhoWebAplicacao(request, response) + File.separator + caminhoLogoCliente);
        String logo = (String)request.getSession().getAttribute("logoPadraoRelatorio");
        if (logo == null || logo.equals("")) {
        	parameters.put("logoPadraoRelatorio", obterCaminhoWebAplicacao(request, response) + File.separator + caminhoImagemLogo);
        } else {
        	parameters.put("logoPadraoRelatorio", logo);
        }
        parameters.put("pastaPadraoImagens", obterCaminhoWebAplicacao(request, response) + "resources" + File.separator + "imagens" + File.separator);
        parameters.put("tituloRelatorio", (String) request.getSession().getAttribute("nomeRelatorio"));
        parameters.put("usuario", (String) request.getSession().getAttribute("nomeUsuario"));
        parameters.put("versaoSoftware", getServletContext().getInitParameter("versaoSoftware"));
        parameters.put("SUBREPORT_DIR", obterCaminhoBaseAplicacao(request, response) + File.separator + getCaminhoBaseAplicacao());
        //System.out.println("THYAGO 3 ");
        setParametrosRelatorio(parameters);
        visualizarRelatorioPDF(request, response,gerarRelatorioJasperPrintObjeto(request, response, getNomeDesignIReport()));
    }

    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Short description";
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

    public String getCaminhoBaseAplicacao() {
        return caminhoBaseAplicacao;
    }

    public void setCaminhoBaseAplicacao(String caminhoBaseAplicacao) {
        this.caminhoBaseAplicacao = caminhoBaseAplicacao;
    }

	public void setDiretorioWeb(String diretorioWeb) {
		this.diretorioWeb = diretorioWeb;
	}

	public String getDiretorioWeb() {
		return diretorioWeb;
	}

      

}
