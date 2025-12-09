/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controle.arquitetura;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author PEDRO
 */
public class ConverteGetParaPostServletInteracao extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("valor") != null && request.getParameter("valorEtapaAtual") != null ) {
            RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher("/"+request.getParameter("pagina"));
            request.setAttribute(request.getParameter("param"), request.getParameter("valor"));
            request.setAttribute(request.getParameter("paramEtapaAtual"), request.getParameter("valorEtapaAtual"));
            request.setAttribute(request.getParameter("paramNovoProspect"), request.getParameter("valorNovoProspect"));
            requestDispatcher.forward(request, response);
        }
        if (request.getParameter("valorLigacaoAtivaSemAgenda") != null && request.getParameter("valorCodigoProspectLigacaoAtivaSemAgenda") != null) {
            RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher("/" + request.getParameter("pagina"));
            request.setAttribute(request.getParameter("paramCodigoProspectLigacaoAtivaSemAgenda"), request.getParameter("valorCodigoProspectLigacaoAtivaSemAgenda"));
            request.setAttribute(request.getParameter("paramLigacaoAtivaSemAgenda"), request.getParameter("valorLigacaoAtivaSemAgenda"));
            requestDispatcher.forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }
}
