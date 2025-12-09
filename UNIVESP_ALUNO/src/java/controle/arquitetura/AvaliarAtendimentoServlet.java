package controle.arquitetura;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import negocio.comuns.administrativo.enumeradores.AvaliacaoAtendimentoEnum;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.administrativo.Atendimento;
import negocio.interfaces.administrativo.AtendimentoInterfaceFacade;



/**
*
* @author Pedro
*/
public class AvaliarAtendimentoServlet extends HttpServlet{
	
	private AtendimentoInterfaceFacade atendimento;
	
	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String nomeAvaliacao = "";
		try {        	
        	String avaliacao = request.getParameter("aval");
            String codAtendimento = request.getParameter("aten");
            Integer codigo = new Integer(codAtendimento);            
    		if (avaliacao.equals("4")) {
    			nomeAvaliacao = AvaliacaoAtendimentoEnum.OTIMO.name();
    		} else if (avaliacao.equals("3")) {
    			nomeAvaliacao = AvaliacaoAtendimentoEnum.BOM.name();
    		} else if (avaliacao.equals("2")) {
    			nomeAvaliacao = AvaliacaoAtendimentoEnum.REGULAR.name();
    		} else if (avaliacao.equals("1")) {
    			nomeAvaliacao = AvaliacaoAtendimentoEnum.RUIM.name();
    		}else{
    			nomeAvaliacao = AvaliacaoAtendimentoEnum.NENHUM.name();
    		}    		
//    		atendimento = new Atendimento();
//            atendimento.realizarAvaliacaoOuvidoria(nomeAvaliacao, codigo, "");            
//            request.setAttribute("msg", "Obrigado por avaliar nosso atendimeto!");
        } catch (Exception ex) {
            Logger.getLogger(AvaliarAtendimentoServlet.class.getName()).log(Level.SEVERE, null, ex);            
//            request.setAttribute("msg", ex.getMessage());
//            nomeAvaliacao = request.getParameter("msg");
//            //System.out.println(nomeAvaliacao);
        }finally{
        	response.sendRedirect(UteisJSF.getAcessoAplicadoURL().substring(0, UteisJSF.getAcessoAplicadoURL().lastIndexOf("/"))+"/atendimentoAvaliado.xhtml");
        }
    }
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
        processRequest(request, response);
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
    }

    /**
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
