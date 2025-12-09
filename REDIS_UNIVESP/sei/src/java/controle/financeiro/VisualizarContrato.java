//relatorio
package controle.financeiro;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.engine.util.JRLoader; @Controller("VisualizarContrato") @Scope("request") @Lazy
public class VisualizarContrato extends HttpServlet {

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

    /**
     * Rotina responsável por obter o diretório real da aplicação Web em execução.
     * É importante acessar este diretório para que seja possível utilizar recursos
     * existentes nos pacotes da aplicação.
     */
    public String obterCaminhoBaseAplicacao(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ServletContext servletContext = (ServletContext) this.getServletContext();
        String caminhoBaseAplicacao = servletContext.getRealPath(request.getContextPath());
        File caminhoBaseAplicacaoFile = new File(caminhoBaseAplicacao);
        caminhoBaseAplicacao = caminhoBaseAplicacaoFile.getParent() + File.separator + "WEB-INF" + File.separator + "classes";
        return caminhoBaseAplicacao;
    }

    /**
     * Rotina responsável por obter o diretório real da aplicação Web em execução.
     * É importante acessar este diretório para que seja possível utilizar recursos
     * existentes nos pacotes da aplicação.
     */
    public String obterCaminhoWebAplicacao(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ServletContext servletContext = (ServletContext) this.getServletContext();
        String caminhoBaseAplicacao = servletContext.getRealPath(request.getContextPath());
        File caminhoBaseAplicacaoFile = new File(caminhoBaseAplicacao);
        caminhoBaseAplicacao = caminhoBaseAplicacaoFile.getParent();
        return caminhoBaseAplicacao;
    }

    public JasperPrint gerarRelatorioJasperPrintXML(HttpServletRequest request, HttpServletResponse response, String xmlDados, String caminhoParserXML, String nomeDesignIReport) throws Exception {
        JRXmlDataSource jrxmlds = new JRXmlDataSource((InputStream)new ByteArrayInputStream(xmlDados.getBytes(), 0, xmlDados.length()), caminhoParserXML);

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

    protected void visualizarRelatorio(HttpServletRequest request,
            HttpServletResponse response,
            String texto) throws ServletException, IOException, Exception {
        response.setContentType("text/html;charset=ISO-8859-1");
        PrintWriter out = response.getWriter();
        out.println(texto);
        out.close();
    }

    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {
        String texto = (String) request.getSession().getAttribute("textoRelatorio");
        visualizarRelatorio(request, response, texto);
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
}
