package controle.arquitetura;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import negocio.comuns.arquitetura.UsuarioVO;

/**
 *
 * @author Alessandro Lima
 */
@Service
@Lazy
@WebServlet(name = "SessionSV")
public class SessionSV extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private Gson gson = new Gson();

	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
		String json = "[]";
		try {
			String authTokenHeader = request.getHeader("Authorization");
			if (authTokenHeader != null && authTokenHeader.equals("#vmA4OmtIT6@4PxO")) {
				Sessoes sessoes = new Sessoes();
				List<HttpSession> sessions = SessionTracker.instance().getSessions();
				for (HttpSession session : sessions) {
	//				session.getMaxInactiveInterval();
					try {
						Enumeration<String> e = session.getAttributeNames();
						while (e.hasMoreElements()) {
							String param = e.nextElement();
							if ("usuarioLogado".equals(param)) {
								UsuarioVO usuario = (UsuarioVO) session.getAttribute(param);
								sessoes.sessoes.add(
									new Sessao(
										session.getId(),
										usuario.getCodigo(),
										usuario.getUsername(),
										usuario.getNome(),
										usuario.getVisaoLogar(),
										usuario.getIpUltimoAcesso(),
										(new Date().getTime() - new Date(session.getCreationTime()).getTime()) / 1000,
										(new Date().getTime() - new Date(session.getLastAccessedTime()).getTime()) / 1000
									)
								);
								break;
							}
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
				json = this.gson.toJson(sessoes);
				response.setStatus(HttpServletResponse.SC_OK);
			} else {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
    
    class Sessao {
    	@SuppressWarnings("unused")
		private String id;
    	@SuppressWarnings("unused")
		private Integer codigoUsuario;
    	@SuppressWarnings("unused")
		private String username;
    	@SuppressWarnings("unused")
		private String nome;
    	@SuppressWarnings("unused")
		private String visaoLogada;
    	@SuppressWarnings("unused")
		private String ip;
    	@SuppressWarnings("unused")
		private Long inicio;
    	@SuppressWarnings("unused")
		private Long inativo;
    	    	
    	private Sessao (String id,
    			Integer codigoUsuario,
    			String username,
    			String nome,
    			String visaoLogada,
    			String ip,
    			Long inicio,
    			Long inativo) {
    		this.id = id;
    		this.codigoUsuario = codigoUsuario;
    		this.username = username;
    		this.nome = nome;
    		this.visaoLogada = visaoLogada;
    		this.ip = ip;
    		this.inicio = inicio;
    		this.inativo = inativo;
    	}
    }
    
    class Sessoes {
		private List<Sessao> sessoes;
    	
    	private Sessoes () {
    		this.sessoes = new ArrayList<Sessao>();
    	}
    }

}