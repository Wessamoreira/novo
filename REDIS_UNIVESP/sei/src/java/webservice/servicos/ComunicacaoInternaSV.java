package webservice.servicos;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.ComunicadoInternoDestinatarioVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
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
public class ComunicacaoInternaSV extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private WebApplicationContext applicationContext;
	private FacadeFactory facadeFactory;
	private LoginControle loginControle;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ComunicacaoInternaSV() {
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
		// login: Login do usuário.
		// nome: nome do aluno.
		// matricula: matrícula do aluno.
		// curso: id numérico único do curso
		// turma: id numérico da turma
		// disciplina: ids numéricos das disciplinas cursadas pelo aluno
		// separados por vírgula.
		// email: email do aluno
		// subject: data do início da turma no formato YYYY/mm/dd
		// mensagem: data do fim da turma no formato YYYY/mm/dd
		// headers: Cabeçalho de headers de e-mail a serem vinculados nos
		// e-mails
		String msgresponse = "";
		String msgException = "";

		try {

			String cliente = request.getParameter("cliente");
			if (!cliente.equalsIgnoreCase("IPOG")) {
				msgException = "Cliente não autenciado!";
				throw new Exception(msgException);
			}
			String key = request.getParameter("key");
			if (!key.equalsIgnoreCase("222e0f44808a5851860446f4dc8b177005bdefd9af6c83f9b404508420dfccd4")) {
				msgException = "Key não autenciado!";
				throw new Exception(msgException);
			}
			String login = request.getParameter("login");
			if (login.equalsIgnoreCase("")) {
				msgException = "Login deve ser informado!";
				throw new Exception(msgException);
			}
			String matricula = request.getParameter("matricula");
			if (matricula.equalsIgnoreCase("")) {
				msgException = "Matrícula deve ser informado!";
				throw new Exception(msgException);
			}
			String nome = request.getParameter("nome");
			if (nome.equalsIgnoreCase("")) {
				msgException = "Nome deve ser informado!";
				throw new Exception(msgException);
			}
			String curso = request.getParameter("curso");
			if (curso.equalsIgnoreCase("")) {
				msgException = "Curso deve ser informado!";
				throw new Exception(msgException);
			}
			String turma = request.getParameter("turma");
			if (turma.equalsIgnoreCase("")) {
				msgException = "Turma deve ser informado!";
				throw new Exception(msgException);
			}
			String disciplina = request.getParameter("disciplina");
			if (disciplina.equalsIgnoreCase("")) {
				msgException = "Disciplina deve ser informado!";
				throw new Exception(msgException);
			}
//			String email = request.getParameter("email");
//			if (email.equalsIgnoreCase("")) {
//				msgException = "Email deve ser informado!";
//				throw new Exception(msgException);
//			}
			String subject = request.getParameter("subject");
			if (subject.equalsIgnoreCase("")) {
				msgException = "Subject deve ser informado!";
				throw new Exception(msgException);
			}
			String mensagem = request.getParameter("mensagem");
			if (mensagem.equalsIgnoreCase("")) {
				msgException = "Mensagem deve ser informado!";
				throw new Exception(msgException);
			}
			String headers = request.getParameter("headers");
			if (headers.equalsIgnoreCase("")) {
				msgException = "Headers deve ser informado!";
				throw new Exception(msgException);
			}
			try {
				Integer codDisciplina = Integer.parseInt(disciplina);
				Integer codTurma = Integer.parseInt(turma);
				PessoaEADIPOGVO pessoa = getFacadeFactory().getPessoaEADIPOGFacade().consultarPorDadosAluno(0, matricula, codDisciplina, codTurma, Uteis.NIVELMONTARDADOS_TODOS, null);
				PessoaVO pessoaVO = new PessoaVO();
				pessoaVO.setCodigo(pessoa.getAluno());
				getFacadeFactory().getPessoaFacade().carregarDados(pessoaVO, null);
				ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
				pessoaVO.setEmail(pessoaVO.getEmail());
				pessoaVO.setNome(pessoaVO.getNome());
				ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
				comunicacaoEnviar.setAssunto(subject);
				// Para obter a mensagem do email formatado Usamos um metodo a
				// parte.
				comunicacaoEnviar.setMensagem(mensagem);
				comunicacaoEnviar.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
				comunicacaoEnviar.setData(new Date());
				comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(pessoaVO));
				comunicacaoEnviar.setTipoDestinatario("AL");
				UsuarioVO usuario = new UsuarioVO();
				getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, usuario, config,null);
				msgresponse = "Comunicação Incluída Com Sucesso!";
				// instancia um novo JSONObject
				JSONObject my_obj = new JSONObject();
				// preenche o objeto
				my_obj.put("titulo", "Comunicação Interna");
				my_obj.put("response", msgresponse);
				my_obj.put("exception", msgException);
				String json_string = my_obj.toString();
				System.out.println(json_string);

				PrintWriter out = response.getWriter();
				out.print(json_string);
				
			} catch (Exception e) {
				msgresponse = "";
				msgException = e.getMessage();
				// instancia um novo JSONObject
				JSONObject my_obj = new JSONObject();
				// preenche o objeto
				my_obj.put("titulo", "Comunicação Interna");
				my_obj.put("response", msgresponse);
				my_obj.put("exception", msgException);
				String json_string = my_obj.toString();
				System.out.println(json_string);
				PrintWriter out = response.getWriter();
				out.print(json_string);
			}
			
		} catch (Exception e) {
			try {
				// instancia um novo JSONObject
				JSONObject my_obj = new JSONObject();
				// preenche o objeto
				my_obj.put("titulo", "Comunicação Interna");
				my_obj.put("response", msgresponse);
				my_obj.put("exception", msgException);
				String json_string = my_obj.toString();
				System.out.println(json_string);

				PrintWriter out = response.getWriter();
				out.print(json_string);

			} catch (Exception o) {

			}
		}

	}

	public ComunicacaoInternaVO inicializarDadosPadrao(ComunicacaoInternaVO comunicacaoEnviar) {
		// Caso o valor seja True, um email sera envida quando a comunicacao for
		// persistida.
		comunicacaoEnviar.setEnviarEmail(Boolean.TRUE);
		// Para obter a mensagem do email formatado Usamos um metodo a parte.
		comunicacaoEnviar.setTipoComunicadoInterno(TipoComunicadoInterno.SOMENTE_LEITURA.getValor());
		comunicacaoEnviar.setPrioridade(PrioridadeComunicadoInterno.NORMAL.getValor());
		comunicacaoEnviar.setTipoMarketing(Boolean.FALSE);
		comunicacaoEnviar.setTipoLeituraObrigatoria(Boolean.FALSE);
		comunicacaoEnviar.setDigitarMensagem(Boolean.TRUE);
		return comunicacaoEnviar;

	}

	public List<ComunicadoInternoDestinatarioVO> obterListaDestinatarios(PessoaVO pessoa) {
		ComunicadoInternoDestinatarioVO destinatario = new ComunicadoInternoDestinatarioVO();
		destinatario.setCiJaLida(Boolean.FALSE);
		destinatario.setDestinatario(pessoa);
		destinatario.setEmail(pessoa.getEmail());
		destinatario.setNome(pessoa.getNome());
		destinatario.setTipoComunicadoInterno(TipoComunicadoInterno.SOMENTE_LEITURA.getValor());
		List<ComunicadoInternoDestinatarioVO> listDestinatario = new ArrayList<ComunicadoInternoDestinatarioVO>();
		listDestinatario.add(destinatario);
		return listDestinatario;
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
