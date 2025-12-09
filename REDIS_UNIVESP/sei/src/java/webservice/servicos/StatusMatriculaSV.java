package webservice.servicos;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import negocio.comuns.academico.CondicaoPagamentoPlanoFinanceiroCursoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.ComunicadoInternoDestinatarioVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaEADIPOGVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.PrioridadeComunicadoInterno;
import negocio.comuns.utilitarias.dominios.TipoComunicadoInterno;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.arquitetura.FacadeFactory;

import org.springframework.beans.BeansException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import controle.arquitetura.LoginControle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileWriter;

/**
 * Servlet implementation class LoginServlet
 */
public class StatusMatriculaSV extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private WebApplicationContext applicationContext;
	private FacadeFactory facadeFactory;
	private LoginControle loginControle;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public StatusMatriculaSV() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		final WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(config.getServletContext());
		setApplicationContext(context);
	}

	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();

		// cliente: Nome configurado do cliente para a integração. (ipog)
		// key : Chave de autenticação do sistema.
		// (222e0f44808a5851860446f4dc8b177005bdefd9af6c83f9b404508420dfccd4 )
		String msgresponse = "";
		String msgException = "";
		// instancia um novo JSONObject
		JSONObject my_obj = new JSONObject();
		try {

			String cliente = request.getParameter("cliente");
			if (!cliente.equalsIgnoreCase("IPOG")) {
				msgException = "Cliente não autenciado!";
				throw new Exception(msgException);
			}
			String key = request.getParameter("key");
			if (!key.equalsIgnoreCase("222e0f44808a5851860446f4dc8b177005bdefd9af6c83f9b404508420dfccd4") &&
					!key.equalsIgnoreCase("3adfd16142367cda550a845ead20500b948b02cea84fd02512f3d0b8f2a59913")) {
				msgException = "Key não autentiado!";
				throw new Exception(msgException);
			}
			String unidadeEnsino = request.getParameter("unidadeensino");
			Integer codUnidadeEnsino = Integer.parseInt(unidadeEnsino);
			if (key.equalsIgnoreCase("3adfd16142367cda550a845ead20500b948b02cea84fd02512f3d0b8f2a59913")) {
				codUnidadeEnsino = 18;
			}			
			
			String matricula = request.getParameter("matricula");
			List<MatriculaVO> listaMat = getFacadeFactory().getMatriculaFacade().obterStatusMatricula(codUnidadeEnsino);
			Iterator i = listaMat.iterator();
			JSONObject my_obj_unid_nome = new JSONObject();
			while (i.hasNext()) {
				MatriculaVO matriculaVO = (MatriculaVO) i.next();
				JSONObject my_obj_unid_cod = new JSONObject();
				my_obj_unid_cod.put("situacaoParcela", matriculaVO.getSituacaoParcela());
				my_obj_unid_cod.put("dataPagamento", matriculaVO.getDataPagamento());
				my_obj_unid_cod.put("consultor", matriculaVO.getConsultor().getPessoa().getNome());
				my_obj_unid_cod.put("situacao", matriculaVO.getSituacao_Apresentar());
				my_obj_unid_cod.put("matricula", matriculaVO.getMatricula());
				my_obj_unid_nome.put(matriculaVO.getMatricula(), my_obj_unid_cod.toString().replace("\\", "").replace("\"{", "{").replace("}\"", "}"));				
			}
			my_obj.put("response", my_obj_unid_nome.toString().replace("\\", "").replace("\"{", "{").replace("}\"", "}"));
			my_obj.put("cliente", "ipog");
			my_obj.put("systemException", "null");
			String json_string = my_obj.toString().replace("\\", "").replace("\"{", "{").replace("}\"", "}");
			PrintWriter out = response.getWriter();
			// preenche o objeto
			out.print(json_string);
		} catch (Exception e) {
			try {
				msgresponse = "";
				msgException = e.getMessage();
				// preenche o objeto
				my_obj.put("response", "null");
				my_obj.put("systemException", msgException);
				String json_string = my_obj.toString();
				//System.out.println(json_string);
				PrintWriter out = response.getWriter();
				out.print(json_string);				
			} catch (Exception k) {
			}
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
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

	public FacadeFactory getFacadeFactory() throws BeansException, Exception {
		if (facadeFactory == null) {
			facadeFactory = (FacadeFactory) getApplicationContext().getBean("facadeFactory");
		}
		return facadeFactory;
	}

	public void setFacadeFactory(FacadeFactory facadeFactory) {
		this.facadeFactory = facadeFactory;
	}

	public LoginControle getLoginControle() {
		if (loginControle == null) {
			loginControle = new LoginControle();
		}
		return loginControle;
	}

	public void setLoginControle(LoginControle loginControle) {
		this.loginControle = loginControle;
	}

}
