package controle.arquitetura;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import kong.unirest.Unirest;

/**
 * Servlet implementation class MestreGRSV
 */
@WebServlet("/MestreGRSV")
public class MestreGRSV extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MestreGRSV() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String alert = new String("Prezados estudantes, estamos com sobrecarga de acesso em nossos sistemas.\\n\\nOrientamos você a fechar esta janela e acesse novamente o Sistema de Prova através do link útil no Portal do Aluno.");		
		String login = (String) request.getSession().getAttribute("loginMGR");
		request.getSession().removeAttribute("loginMGR");
		String action = (String) request.getSession().getAttribute("actionMGR");
		request.getSession().removeAttribute("actionMGR");
		String key = (String) request.getSession().getAttribute("keyMGR");
		request.getSession().removeAttribute("keyMGR");
		String token = (String) request.getSession().getAttribute("tokenMGR");
		request.getSession().removeAttribute("tokenMGR");
		String headerBar = (String) request.getSession().getAttribute("headerBarMGR");
		request.getSession().removeAttribute("headerBarMGR");
		// TODO Auto-generated method stub
		response.getWriter().append("<html>").append("<body onload=\"document.getElementById('btnSistemasProvasMestreGR').click();setTimeout(function(){alert('").append(alert).append("'); window.close();}, 5000)\"><form action=\"").append(action).append("\"  method=\"post\" id=\"form\" >");
		response.getWriter().append("<span style=\"font-size:40px;margin-top:10px;margin-left:20px\">Prezados estudantes, estamos redirecionando para sistemas de provas.</span>");
		response.getWriter().append("<div style=\"display: none;\">");
		response.getWriter().append("<input name=\"login\" value=\""+login+"\" />");
		response.getWriter().append("<input name=\"key\" value=\""+key+"\" />");
		response.getWriter().append("<input name=\"token\" value=\""+token+"\" />");
		response.getWriter().append("<input name=\"headerBar\" value=\""+headerBar+"\" />");
		response.getWriter().append("<input type=\"submit\" id=\"btnSistemasProvasMestreGR\" />");
		response.getWriter().append("</div>");
		response.getWriter().append("</form></body></html>");		
		response.getWriter().flush();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
