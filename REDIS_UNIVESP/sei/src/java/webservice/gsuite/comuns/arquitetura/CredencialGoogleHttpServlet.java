package webservice.gsuite.comuns.arquitetura;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;

import com.google.gson.Gson;

import controle.arquitetura.LoginControle;
import kong.unirest.HttpResponse;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import relatorio.arquitetura.SuperRelatorioSV;
import webservice.arquitetura.InfoWSVO;

public class CredencialGoogleHttpServlet extends SuperRelatorioSV {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4553836940352952541L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			UsuarioVO usuarioVO = new UsuarioVO();
			LoginControle loginControle = (LoginControle) request.getSession().getAttribute("LoginControle");
		    if(loginControle != null && loginControle.getUsuario() != null &&  Uteis.isAtributoPreenchido(loginControle.getUsuario().getCodigo())) {
		    	usuarioVO.setCodigo(loginControle.getUsuario().getCodigo());        	
		    } 
		    if(!Uteis.isAtributoPreenchido(request.getParameter("code"))) {
		    	mensagem(request, response, "Não foi encontrado a CODE para geracao da credencial.");
		    }else {
				HttpResponse<String> resp = getFacadeFactory().getConfiguracaoSeiGsuiteFacade().realizarProcessoUrlCodeGoogle(request.getParameter("code"), usuarioVO);
				InfoWSVO rep = new Gson().fromJson(resp.getBody(), InfoWSVO.class);
				if (resp.getStatus() != (HttpStatus.OK.value())) {				
					mensagem(request, response,tratarMensagemErroWebService(rep, String.valueOf(resp.getStatus()), resp.getBody()));
				}else {
					mensagem(request, response, rep.getMensagem());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			mensagem(request, response, e.getMessage());
		}
	}
	
	private void mensagem(HttpServletRequest request, HttpServletResponse response, String msg) throws IOException {		
		PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<head>");
		out.println("<link href='resources/css/otimize2.css' rel='stylesheet' type='text/css'>");
		out.println("</head>");
		out.println("<body>");
		out.println("<span class='mensagemDetalhada'>"+msg+"</span>");
		out.println("</body>");
		out.println("</html>");
		
	}
	
	private String tratarMensagemErroWebService(InfoWSVO resp, String status,  String mensagemRetorno) {
		String msg = null;
		if(resp != null && resp.getCodigo() != 0) {
			msg = resp.getMensagem() + " - " + resp.getMensagemCampos();
		}else if(resp != null && resp.getStatus() != 0) {
			msg = resp.getMessage();
		}else {
			msg = mensagemRetorno;
		}
		if (status.contains("400")) {
			return ("A requisição é inválida, em geral conteúdo malformado. " + msg);
		}
		if (status.contains("401")) {
			return ("O usuário e senha ou token de acesso são inválidos. " + msg);
		}
		if (status.contains("403")) {
			return ("O acesso à API está bloqueado ou o usuário está bloqueado. " + msg);
		}
		if (status.contains("404")) {
			return ("O endereço acessado não existe. " + msg);
		}
		if (status.contains("422")) {
			return ("A Requisição é válida, mas os dados passados não são válidos. " + msg);
		}
		if (status.contains("429")) {
			return ("O usuário atingiu o limite de requisições. " + msg);
		}
		if (status.contains("500")) {
			return ("Houve um erro interno do servidor ao processar a requisição. Este erro pode ter sido causado por entrada mal formatada. Favor rever a sua entrada. " + msg);
		}
		return ("Erro Não tratato Web Service : " + msg);
	}

}
