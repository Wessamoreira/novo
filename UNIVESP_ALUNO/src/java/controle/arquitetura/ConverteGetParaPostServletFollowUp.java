/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controle.arquitetura;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author PEDRO
 */

@WebServlet(name="ConverteGetParaPostServletFollowUp")
public class ConverteGetParaPostServletFollowUp extends HttpServlet {
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("valor") != null) {        	
            RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher("/" + request.getParameter("pagina"));
            String param = "";
            if (request.getParameter("param") == null) {
            	param = request.getParameter("key");
            } else {
            	param = request.getParameter("param");
            }
            request.setAttribute(param, request.getParameter("valor"));
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
