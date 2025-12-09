package webservice.servicos;

import java.io.IOException;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Servlet implementation class LoginServlet
 */
public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private WebApplicationContext applicationContext;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		final WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(config.getServletContext());
		setApplicationContext(context);
	}

	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String usuario = request.getParameter("usuario");
		String senha = request.getParameter("senha");
		String ambiente = request.getParameter("ambiente");
		if (session.getAttribute("LoginControle") != null) {
			session.removeAttribute("LoginControle");
		}
		session.setAttribute("usuarioExterno", usuario);
		session.setAttribute("senhaExterna", senha);
		session.setAttribute("ambienteExterna", ambiente);
		response.sendRedirect("./index.xhtml");
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	public WebApplicationContext getApplicationContext() throws Exception {
		if (applicationContext == null) {
			throw new Exception("Não foi possível obter o Contexto do Spring");
		}
		return applicationContext;
	}

	public void setApplicationContext(WebApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

}
