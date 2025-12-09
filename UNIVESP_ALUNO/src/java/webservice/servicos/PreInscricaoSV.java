/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice.servicos;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.BeansException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FormacaoAcademicaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.basico.PessoaVO;

import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.FacadeFactory;

/**
 *
 * @author Edigar
 * 
 * Ver html de exemplo chamado preInscricaoExterna.html, 
 * observe que as combobox de cidade e naturalidade 
 * devem ser geradas de acordo com a tabela cidade da base do cliente.
 * Nem todos os campos precisam ser usado e porém deve ser mantidos os campos Nome, Email e um dos telefones.
 * Caso esteja a configuração padrão do sistema esteja definido pra validar o cpf na pré-inscrição o mesmo deve ser mantido 
 * 
 */
public class PreInscricaoSV extends HttpServlet {
    
    public static final long serialVersionUID = 1L;
    private WebApplicationContext applicationContext;
    private FacadeFactory facadeFactory;    
    
   @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        final WebApplicationContext context =
                WebApplicationContextUtils.getWebApplicationContext(
                config.getServletContext());

        setApplicationContext(context);
    }
   
   

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws Exception 
     * @throws BeansException 
     */
   

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
//        try {
//			processRequest(request, response);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//		}
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
//        try {
//			processRequest(request, response);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//		}
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    /**
     * @return the applicationContext
     */
    public WebApplicationContext getApplicationContext() throws Exception {
        if (applicationContext == null) {
            throw new Exception("Não foi possível obter o Contexto do Spring");
        }
        return applicationContext;
    }

    /**
     * @param applicationContext the applicationContext to set
     */
    public void setApplicationContext(WebApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
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
}
