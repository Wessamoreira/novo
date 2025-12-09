package relatorio.arquitetura;

import java.io.IOException;
import java.util.List;

import jakarta.faces. context.FacesContext;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.BeansException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.facade.jdbc.arquitetura.FacadeFactory;
import controle.arquitetura.LoginControle;

/**
 *
 * @author Edgar
 */
 
public class SuperRelatorioSV extends HttpServlet {

    public static final long serialVersionUID = 1L;
    private WebApplicationContext applicationContext;
    private FacadeFactory facadeFactory;
    private String diretorioPDFBoleto;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        final WebApplicationContext context =
                WebApplicationContextUtils.getWebApplicationContext(
                config.getServletContext());

        setApplicationContext(context);

    }

    public FacesContext context() {
        return FacesContext.getCurrentInstance();
    }

    public void apresentarRelatorio(HttpServletRequest request,
            HttpServletResponse response,
            String nomeRelatorio,
            String xml,
            String tituloRelatorio,
            String nomeEmpresa,
            String mensagemRel,
            String tipoRelatorio,
            String parserBuscaTags,
            String designIReport,
            String nomeUsuario,
            String filtros,
            String parametro1,
            String parametro2,
            String parametro3) throws Exception {
        request.setAttribute("xmlRelatorio", xml);
        request.setAttribute("tipoRelatorio", tipoRelatorio);
        request.setAttribute("nomeRelatorio", nomeRelatorio);
        request.setAttribute("nomeEmpresa", nomeEmpresa);
        request.setAttribute("mensagemRel", mensagemRel);
        request.setAttribute("tituloRelatorio", tituloRelatorio);
        request.setAttribute("caminhoParserXML", parserBuscaTags);
        request.setAttribute("nomeDesignIReport", designIReport);
        request.setAttribute("nomeUsuario", nomeUsuario);
        request.setAttribute("parametro1", parametro1);
        request.setAttribute("parametro2", parametro2);
        request.setAttribute("parametro3", parametro3);

        if (filtros.equals("")) {
            filtros = "nenhum";
        }
        request.setAttribute("filtros", filtros);
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/VisualizadorRelatorio");
        dispatcher.forward(request, response);
        setDiretorioPDFBoleto((String) request.getAttribute("diretorioPDFBoleto"));
    }

    public void apresentarRelatorio(HttpServletRequest request,
            HttpServletResponse response,
            String nomeRelatorio,
            String xml,
            String tituloRelatorio,
            String nomeEmpresa,
            String mensagemRel,
            String tipoRelatorio,
            String parserBuscaTags,
            String designIReport,
            String nomeUsuario,
            String persistirImpressaoBoleto,
            String origemImpressaoBoleto,
            String filtros) throws Exception {
        request.setAttribute("xmlRelatorio", xml);
        request.setAttribute("tipoRelatorio", tipoRelatorio);
        request.setAttribute("nomeRelatorio", nomeRelatorio);
        request.setAttribute("nomeEmpresa", nomeEmpresa);
        request.setAttribute("mensagemRel", mensagemRel);
        request.setAttribute("tituloRelatorio", tituloRelatorio);
        request.setAttribute("caminhoParserXML", parserBuscaTags);
        request.setAttribute("nomeDesignIReport", designIReport);
        request.setAttribute("nomeUsuario", nomeUsuario);
        request.setAttribute("persistirImpressaoBoleto", persistirImpressaoBoleto);
        request.setAttribute("origemImpressaoBoleto", origemImpressaoBoleto);
        if (filtros.equals("")) {
            filtros = "nenhum";
        }
        request.setAttribute("filtros", filtros);
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/VisualizadorRelatorio");
        dispatcher.forward(request, response);
    }

    public void apresentarRelatorioObjetos(String nomeRelatorio,String tituloRelatorio,String nomeEmpresa,String mensagemRel, String tipoRelatorio,
            String parserBuscaTags, String designIReport,String nomeUsuario,String filtros,List listaObjetos,String caminhoBaseRelatorio, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	
//        HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
        request.setAttribute("tipoRelatorio", tipoRelatorio);
        request.setAttribute("nomeRelatorio", nomeRelatorio);
        request.setAttribute("nomeEmpresa", nomeEmpresa);
        request.setAttribute("mensagemRel", mensagemRel);
        request.setAttribute("tituloRelatorio", tituloRelatorio);
        request.setAttribute("caminhoParserXML", parserBuscaTags);
        request.setAttribute("nomeDesignIReport", designIReport);
        request.setAttribute("nomeUsuario", nomeUsuario);
        request.setAttribute("listaObjetos", listaObjetos);
        request.setAttribute("tipoImplementacao", "OBJETO");
        request.setAttribute("caminhoBaseRelatorio", caminhoBaseRelatorio);
        if (filtros.equals("")) {
            filtros = "nenhum";
        }
        request.setAttribute("filtros", filtros);
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/VisualizadorRelatorio");
        dispatcher.forward(request, response);
//        context().getExternalContext().dispatch("/VisualizadorRelatorio");
//        FacesContext.getCurrentInstance().responseComplete();
        
    }
    

    public void apresentarRelatorioObjetos(String nomeRelatorio,String tituloRelatorio,String nomeEmpresa, String logoUnidadeEnsinoRelatorio, String logoUnidadeEnsino,
    			String mensagemRel,String tipoRelatorio,String parserBuscaTags,String designIReport,String nomeUsuario,
    			String filtros,List listaObjetos,String caminhoBaseRelatorio,String parametro1,String parametro2,String parametro3, String persistirImpressaoBoleto, String origemImpressaoBoleto, String codigoUsuario, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	
//        HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
        request.setAttribute("tipoRelatorio", tipoRelatorio);
        request.setAttribute("nomeRelatorio", nomeRelatorio);
        request.setAttribute("nomeEmpresa", nomeEmpresa);
        request.setAttribute("mensagemRel", mensagemRel);
        request.setAttribute("tituloRelatorio", tituloRelatorio);
        request.setAttribute("caminhoParserXML", parserBuscaTags);
        request.setAttribute("nomeDesignIReport", designIReport);
        request.setAttribute("nomeUsuario", nomeUsuario);
        request.setAttribute("listaObjetos", listaObjetos);
        request.setAttribute("tipoImplementacao", "OBJETO");
        request.setAttribute("caminhoBaseRelatorio", caminhoBaseRelatorio);
        request.setAttribute("parametro1", parametro1);
        request.setAttribute("parametro2", parametro2);
        request.setAttribute("parametro3", parametro3);
        request.setAttribute("realizarDownload", "SIM");
        request.setAttribute("logoPadraoRelatorio", logoUnidadeEnsinoRelatorio);
        request.setAttribute("logoCliente", logoUnidadeEnsino);

        if (filtros.equals("")) {
            filtros = "nenhum";
        }
        request.setAttribute("filtros", filtros);
        
        request.setAttribute("persistirImpressaoBoleto", persistirImpressaoBoleto);
        request.setAttribute("origemImpressaoBoleto", origemImpressaoBoleto);
        request.setAttribute("codigoUsuario", codigoUsuario);
        
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/VisualizadorRelatorio");
        dispatcher.forward(request, response);
//        context().getExternalContext().dispatch("/VisualizadorRelatorio");
//        FacesContext.getCurrentInstance().responseComplete();
    }

    private LoginControle getLoginControle() {
        try {
            return (LoginControle) context().getExternalContext().getSessionMap().get("LoginControle");
        } catch (Exception e) {
            return null;
        }
    }

    protected static UnidadeEnsinoVO getUnidadeEnsinoLogado() throws Exception {
        LoginControle loginControle = (LoginControle) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("LoginControle");
        UnidadeEnsinoVO unidadeEnsinoVO = loginControle.getUsuario().getUnidadeEnsinoLogado();
        return unidadeEnsinoVO;
    }

    public String getNomeUsuario() {
        try {
            return getLoginControle().getUsuario().getNome();
        } catch (Exception e) {
            return "";
        }
    }

    public String getNomeUnidadeEnsino() {
        try {
            return getUnidadeEnsinoLogado().getNome();
        } catch (Exception e) {
            return "";
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    // <editor-fold defaultstate="collapsed" desc="Métodos HttpServlet. Clique no sinal de + à esquerda para editar o código.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    public void setApplicationContext(WebApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public WebApplicationContext getApplicationContext() throws Exception {
        if (applicationContext == null) {
            throw new Exception("Não foi possível obter o Contexto do Spring");
        }
        return applicationContext;
    }

    public void setFacadeFactory(FacadeFactory facadeFactory) {
        this.facadeFactory = facadeFactory;
    }

    public FacadeFactory getFacadeFactory() throws BeansException, Exception {
        if (facadeFactory == null) {
            facadeFactory = (FacadeFactory) getApplicationContext().getBean("facadeFactory");
        }
        return facadeFactory;
    }

    public String getDiretorioPDFBoleto() {
        if (diretorioPDFBoleto == null) {
            diretorioPDFBoleto = "";
        }
        return diretorioPDFBoleto;
    }

    public void setDiretorioPDFBoleto(String diretorioPDFBoleto) {
        this.diretorioPDFBoleto = diretorioPDFBoleto;
    }
}
