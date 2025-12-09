package webservice.servicos;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.BeansException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import controle.arquitetura.AplicacaoControle;
import controle.arquitetura.LoginControle;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.utilitarias.SpringUtil;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.TipoOrigemContaReceber;
import negocio.facade.jdbc.arquitetura.FacadeFactory;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.arquitetura.VisualizadorRelatorio;
import relatorio.negocio.jdbc.financeiro.BoletoBancarioRel;

/**
 * Servlet implementation class LoginServlet
 */
public class SituacaoMatriculaAlunoSV extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private WebApplicationContext applicationContext;
	private FacadeFactory facadeFactory;
	private LoginControle loginControle;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SituacaoMatriculaAlunoSV() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public AplicacaoControle getAplicacaoControle() {
		return (AplicacaoControle) SpringUtil.getApplicationContext().getBean(AplicacaoControle.class);
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
			String turma = request.getParameter("turma");
			Integer codTurma = Integer.parseInt(turma);

			String cpf = request.getParameter("cpf");

			
			String diretorioContrato = "";
			String diretorioBoleto = "";
			String dataVencimento = "";
			String pagoOuNao = "nao";
			Boolean contratoAssinado = Boolean.FALSE;
			
			MatriculaPeriodoVO matriculaPeriodo = getFacadeFactory().getMatriculaPeriodoFacade().consultarMatriculaPorCpfAlunoTurma(cpf, codTurma, new UsuarioVO());			
			getFacadeFactory().getMatriculaFacade().carregarDados(matriculaPeriodo.getMatriculaVO(), NivelMontarDados.TODOS, new UsuarioVO());
			ConfiguracaoFinanceiroVO confFinan = getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(matriculaPeriodo.getMatriculaVO().getUnidadeEnsino().getCodigo());
			getFacadeFactory().getMatriculaPeriodoFacade().carregarDados(matriculaPeriodo, NivelMontarDados.TODOS, confFinan, new UsuarioVO());
			contratoAssinado = getFacadeFactory().getDocumetacaoMatriculaFacade().consultarContratoAssinadoPorMatriculaAlunoEntregue(matriculaPeriodo.getMatriculaVO().getMatricula(), new UsuarioVO());
			ConfiguracaoGeralSistemaVO configuracaoGeralSistema = getAplicacaoControle().getConfiguracaoGeralSistemaVO(matriculaPeriodo.getMatriculaVO().getUnidadeEnsino().getCodigo(), new UsuarioVO());
			// boleto
			List<ContaReceberVO> listaContaReceber = getFacadeFactory().getContaReceberFacade().consultarPorMatriculaAlunoPorTipoOrigem(matriculaPeriodo.getMatriculaVO().getMatricula(), TipoOrigemContaReceber.MATRICULA.valor, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, confFinan, new UsuarioVO());
			if (!listaContaReceber.isEmpty()) {
				matriculaPeriodo.getMatriculaPeriodoVencimentoReferenteMatricula().setContaReceber(listaContaReceber.get(0));
			}
			UsuarioVO usuResp = getFacadeFactory().getUsuarioFacade().consultarUsuarioUnicoDMParaMatriculaCRM(Uteis.NIVELMONTARDADOS_DADOSLOGIN, null);
			usuResp.setPerfilAcesso(getFacadeFactory().getPerfilAcessoFacade().consultarPerfilParaFuncionarioAdministrador(0, usuResp));
//			List<UsuarioVO> listaUsuario = getFacadeFactory().getUsuarioFacade().consultarPorCodigoPessoa(configuracaoGeralSistema.getUsuarioResponsavelOperacoesExternas().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuResp);
//			if (!listaUsuario.isEmpty()) {
//				usuResp = listaUsuario.get(0);
//			}

			if (matriculaPeriodo.getMatriculaPeriodoVencimentoReferenteMatricula().getContaReceber().getCodigo() > 0) {
				getFacadeFactory().getMatriculaPeriodoFacade().emitirBoletoMatricula(matriculaPeriodo, usuResp, confFinan);
	            VisualizadorRelatorio ser = new VisualizadorRelatorio();
	            List lista = getFacadeFactory().getBoletoBancarioRelFacade().emitirRelatorioLista(false, matriculaPeriodo.getMatriculaPeriodoVencimentoReferenteMatricula().getContaReceber().getCodigo(), null, null, null, null, null, null, null, null, null, "", 0, usuResp, confFinan, 0, true);
	            if (!lista.isEmpty()) {
		            String design = BoletoBancarioRel.getDesignIReportRelatorio();
		            ser.setTipoRelatorio("PDF");
		            ser.setNomeRelatorio(BoletoBancarioRel.getIdEntidade());
		            ser.setNomeEmpresa("");
		            ser.setMensagemRel("");
		            ser.setTipoRelatorio("Recibo do Sacado");
		            ser.setCaminhoParserXML("");
		            ser.setNomeDesignIReport(design);
		            //ser.setNomeUDesignIReport(design);
		            ser.setListaObjetos(lista);
		            ser.setCaminhoBaseAplicacao(BoletoBancarioRel.getCaminhoBaseRelatorio());
		            ser.setCaminhoBase(UteisJSF.getCaminhoBase().replace("/", File.separator));
		            ser.setCaminhoBaseWeb(UteisJSF.getCaminhoWeb().replace("/", File.separator));
		            ser.setPersistirImpressaoBoleto("true");
		            ser.visualizarRelatorioPDF(ser.gerarRelatorioJasperPrintObjeto(null, null, design), "SituacaoMatriculaAlunoSV");
		            //diretorioBoleto = UteisJSF.getCaminhoWeb().replace("/", File.separator) + "relatorio" + File.separator + ser.getNomePDF();
		            //diretorioBoleto = diretorioBoleto.substring(1);
		            diretorioBoleto = configuracaoGeralSistema.getUrlAcessoExternoAplicacao() + File.separator + "relatorio" + File.separator + ser.getNomePDF();
	            }
	        }
			
			if (matriculaPeriodo.getMatriculaPeriodoVencimentoReferenteMatricula().getContaReceber().getCodigo() > 0) {
				//getFacadeFactory().getContaReceberFacade().carregarDados(matriculaPeriodo.getMatriculaPeriodoVencimentoReferenteMatricula().getContaReceber(), confFinan, new UsuarioVO());
				dataVencimento = matriculaPeriodo.getMatriculaPeriodoVencimentoReferenteMatricula().getContaReceber().getDataVencimento_Apresentar();
				if (matriculaPeriodo.getMatriculaPeriodoVencimentoReferenteMatricula().getContaReceber().getSituacaoEQuitada()) {
					pagoOuNao = "sim";
				}
			}
            
			if ((matriculaPeriodo.getSituacao().equals("CO")) || (matriculaPeriodo.getSituacao().equals("AT"))) {
				pagoOuNao = "sim";
			}
			
			// Contrato
            String diretorioContratoPdf = getFacadeFactory().getImpressaoContratoFacade().imprimirContratoRenovarMatricula("MA", matriculaPeriodo.getMatriculaVO(), matriculaPeriodo, confFinan, configuracaoGeralSistema, usuResp);
            //diretorioContrato = UteisJSF.getCaminhoWeb().replace("/", File.separator) + "relatorio" + File.separator + diretorioContratoPdf;
            diretorioContrato = configuracaoGeralSistema.getUrlAcessoExternoAplicacao() + File.separator + "relatorio" + File.separator + diretorioContratoPdf;
			
			JSONObject my_obj_unid_nome = new JSONObject();
				
				JSONObject my_obj_unid_cod = new JSONObject();
				my_obj_unid_cod.put("matricula", matriculaPeriodo.getMatriculaVO().getMatricula());
				my_obj_unid_cod.put("dataVencimento", dataVencimento);
				my_obj_unid_cod.put("linkBoleto", diretorioBoleto);
				my_obj_unid_cod.put("linkContrato", diretorioContrato);
				my_obj_unid_cod.put("pago", pagoOuNao);
				my_obj_unid_cod.put("contratoAssinado", contratoAssinado ? "sim" : "nao");
				my_obj_unid_nome.put(cpf, my_obj_unid_cod.toString().replace("\\", "").replace("\"{", "{").replace("}\"", "}"));				

				my_obj.put("response", my_obj_unid_nome.toString().replace("\\", "").replace("\"{", "{").replace("}\"", "}"));
			my_obj.put("cliente", "ipog");
			my_obj.put("systemException", "null");
			String json_string = my_obj.toString().replace("\\", "").replace("\"{", "{").replace("}\"", "}");
			//System.out.println(json_string);
			PrintWriter out = response.getWriter();
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

	public ConfiguracaoFinanceiroVO getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(Integer unidadeEnsino) throws Exception {
		if(Uteis.isAtributoPreenchido(unidadeEnsino)){
				ConfiguracaoFinanceiroVO cfg = getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_TODOS, new UsuarioVO(), unidadeEnsino);
				if ((cfg != null) && (!cfg.getCodigo().equals(0))) {
					return cfg;
				}else {
					return new ConfiguracaoFinanceiroVO();
				}
		}
		return new ConfiguracaoFinanceiroVO();
	}	
}
