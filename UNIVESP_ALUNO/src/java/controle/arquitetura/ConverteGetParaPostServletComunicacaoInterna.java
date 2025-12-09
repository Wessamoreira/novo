package controle.arquitetura;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ConverteGetParaPostServletComunicacaoInterna extends HttpServlet {
	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("cdgOrigem") != null) {
            RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher("/" + request.getParameter("pagina"));
            request.setAttribute(request.getParameter("paramCodigoOrigem"), request.getParameter("cdgOrigem"));
            request.setAttribute(request.getParameter("paramTipoOrigem"), request.getParameter("tpOrigem"));
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
