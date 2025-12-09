package controle.academico;

import java.io.File;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.*;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import negocio.comuns.utilitarias.*;
import org.richfaces.event.DataScrollEvent;
import org.richfaces.event.FileUploadEvent;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import controle.arquitetura.SuperControle.MSG_TELA;
import negocio.comuns.academico.DocumetacaoMatriculaVO;
import negocio.comuns.academico.FiliacaoVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PlanoDescontoVO;
import negocio.comuns.academico.PlanoFinanceiroAlunoVO;
import negocio.comuns.academico.PlanoFinanceiroCursoVO;
import negocio.comuns.academico.RegistroAtividadeComplementarMatriculaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FormacaoAcademicaVO;
import negocio.comuns.administrativo.FormacaoExtraCurricularVO;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.TipoMidiaCaptacaoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.OperacaoFuncionalidadeEnum;
import negocio.comuns.arquitetura.enumeradores.OrigemOperacaoFuncionalidadeEnum;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoAcademicoEnum;
import negocio.comuns.bancocurriculum.AreaProfissionalVO;
import negocio.comuns.basico.AreaProfissionalInteresseContratacaoVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.DadosComerciaisVO;
import negocio.comuns.basico.EnderecoVO;
import negocio.comuns.basico.PaizVO;
import negocio.comuns.basico.PessoaEmailInstitucionalVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.basico.enumeradores.ModalidadeBolsaEnum;
import negocio.comuns.basico.enumeradores.SituacaoMilitarEnum;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;
import negocio.comuns.crm.AgendaPessoaHorarioVO;
import negocio.comuns.crm.CompromissoAgendaPessoaHorarioVO;
import negocio.comuns.crm.InteracaoWorkflowVO;
import negocio.comuns.crm.ProspectsVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaReceberRecebimentoVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.PerfilEconomicoVO;
import negocio.comuns.financeiro.TextoPadraoVO;
import negocio.comuns.gsuite.PessoaGsuiteVO;
import negocio.comuns.pesquisa.AreaConhecimentoVO;
import negocio.comuns.processosel.InscricaoVO;
import negocio.comuns.utilitarias.dominios.CorRaca;
import negocio.comuns.utilitarias.dominios.NivelFormacaoAcademica;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.TipoDeficiencia;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.comuns.utilitarias.dominios.TipoTranstornosNeurodivergentes;
import negocio.facade.jdbc.academico.Matricula;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.basico.Pessoa;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.controle.academico.HistoricoAlunoRelControle;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.academico.BoletimAcademicoRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.academico.BoletimAcademicoRel;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das
 * páginas pessoaForm.jsp pessoaCons.jsp) com as funcionalidades da classe
 * <code>Pessoa</code>. Implemtação da camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see Pessoa
 * @see PessoaVO
 */
@Controller("AlunoControle")
@Scope("viewScope")
@Lazy
public class AlunoControle extends SuperControleRelatorio implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7810714027556635803L;
	private PessoaVO pessoaVO;
	protected List<SelectItem> listaSelectItemNacionalidade;
	protected List<SelectItem> listaSelectItemAreaConhecimento;
	protected List<SelectItem> listaSelectItemPerfilEconomico;
	protected List<SelectItem> listaSelectItemAreaProfissional;
	protected String campoConsultaCidade;
	protected String valorConsultaCidade;
	protected List listaConsultaCidade;
	private String campoConsultaFiliacaoCidade;
	private String valorConsultaFiliacaoCidade;
	private List listaConsultaFiliacaoCidade;
	private String campoConsultaNaturalidade;
	private String valorConsultaNaturalidade;
	private List listaConsultaNaturalidade;
	private String campoConsultaFiador;
	private String valorConsultaFiador;
	private List listaConsultaFiador;
	private String ajaxRedirecionarFocoCampo;
	private Boolean enviarEmail;
	private InteracaoWorkflowVO interacaoWorkflowVO;
	private ProspectsVO prospectsVO;
	private PessoaGsuiteVO pessoaGsuiteVO;
	private PessoaEmailInstitucionalVO pessoaEmailInstitucionalVO;
	/**
	 * Interface <code>PessoaInterfaceFacade</code> responsável pela
	 * interconexão da camada de controle com a camada de negócio. Criando uma
	 * independência da camada de controle com relação a tenologia de
	 * persistência dos dados (DesignPatter: Façade).
	 */
	private FormacaoAcademicaVO formacaoAcademicaVO;
	private String disciplina_Erro;
	private FiliacaoVO filiacaoVO;
	private String turno_Erro;
	private AgendaPessoaHorarioVO agendaPessoaHorarioVO;
	private Boolean permitirAlterarCompromisso;
	public String tipoPessoa = "AL";
	public Boolean verificarCpf;
	public Boolean consultarPessoa;
	public Boolean editarDados;
	protected boolean habilitarIniciarInscricao;
	// Atributo para listar a(s) matricula(s) do(s) aluno(s) na visualização da
	// Ficha do Aluno @Autor Carlos
	private List listaMatriculaAluno;
	private List listaContaReceberAluno;
	private List listaHistoricoVO;
	private MatriculaVO matriculaVO;
	private MatriculaPeriodoVO matriculaPeriodoVO;
	private Boolean apresentarAbaDadosAcademicosFichaAluno;
	private Boolean apresentarAbaDadosTurmaDisciplinaFichaAluno;
	private Boolean apresentarAbaDadosFinanceiroFichaAluno;
	private String abaSelecionada;
	private Boolean existePendenciaFinanceira;
	private PessoaVO pessoaExistente;
	private Boolean importarDadosPessoa;
	private Boolean imprimirContrato;
	private String campoConsultaCidadeEmpresa;
	private String valorConsultaCidadeEmpresa;
	private List listaConsultaCidadeEmpresa;
	private DadosComerciaisVO dadosComerciaisVO;
	private FormacaoExtraCurricularVO formacaoExtraCurricularVO;
	private AreaProfissionalVO areaProfissionalVO;
	private String tipoDocumento;
	private CompromissoAgendaPessoaHorarioVO compromissoAgendaPessoaHorarioVO;
	private Boolean apresentarMatriculaCRM;
	private Boolean matriculaDiretaAgendaCRM;
	private List<SelectItem> listaSelectItemMatricula;
	private List<SelectItem> selectItemsCargoFuncionarioPrincipal;
	private List<SelectItem> selectItemsCargoFuncionarioSecundario;
	private List<FuncionarioVO> listaConsultaFuncionario;
	private String valorConsultaFuncionario;
	private String campoConsultaFuncionario;
	private List<SelectItem> listaSelectItemSituacaoMilitar;
	private Boolean apresentarCampoAssinatura;
	private Boolean apresentarCampoAssinaturaResponsavel;
	/**
	 * Atributo utilizado para geração do relatório de ficha individual do
	 * aluno.
	 */
	private BoletimAcademicoRelVO boletimAcademicoRelVO;
	private Boolean apresentarDisciplinaComposta;
	private Boolean permiteMatricularAluno;	
	/**
	 * Atributo Utilizado para geração doa lista de Documentos 
	 *
	 */
	protected List documetacaoMatriculaVOS;
	private String documentoEntrege;
	/**
	 * 
	 * Atributo Utilizado para geração doa lista de Registro Atividade Complementar 
	 */
	private List<RegistroAtividadeComplementarMatriculaVO> listaRegistroAtividadeComplementarMatriculaVOs;
	private RegistroAtividadeComplementarMatriculaVO registroAtividadeComplementarMatricula;
	private List<RegistroAtividadeComplementarMatriculaVO> listaConsultaRegistroAtividadeComplementarMatriculaVOs;
	private List<InscricaoVO> inscricaoVOs;
	private Boolean validaCamposEnadeCenso;
	private Boolean tornarCampoMidiaCaptacaoObrigatorio;
	
	private List<SelectItem> listaSelectItemTipoMidiaCaptacao;
	private boolean liberarEdicaoAlunoSomenteComSenha = false;
	private String userNameLiberarFuncionalidade;
	private String senhaLiberarFuncionalidade;
	private boolean existeConfiguracaoSeiGsuite = false;
	private Boolean liberarEdicaoRegistroAcademicoAluno ;
	private List<SelectItem> listaSelectItemTitulo;
	private int hashPessoaInicial;

	public AlunoControle() throws Exception {
		// obterUsuarioLogado();
		setApresentarMatriculaCRM(Boolean.FALSE);
		String cd = (String) ((HttpServletRequest) context().getExternalContext().getRequest()).getParameter("cd");
		PessoaVO obj = null;
		if (cd == null) {
			obj = (PessoaVO) context().getExternalContext().getSessionMap().get("pessoaItem");
		} else {
			obj = new PessoaVO();
			obj.setCodigo(Integer.valueOf(cd));
		}
		if (obj == null) {
			novo();
			setControleConsulta(new ControleConsulta());
		} else {
			executarVisualizacaoFichaAluno(obj);
			context().getExternalContext().getSessionMap().remove("pessoa");
			context().getExternalContext().getRequestMap().remove("cd");
		}
		getControleConsulta().setCampoConsulta("nome");
		verificarPermissaoPodeMatricula();		
		verificarTornarCampoMidiaCaptacaoObrigatorio();
		setMensagemID("msg_entre_prmconsulta");
	}

	public void verificarPermissaoPodeMatricula() {
		try {
			ControleAcesso.incluir("Matricula", true, getUsuarioLogado());
			setPermiteMatricularAluno(Boolean.TRUE);
		} catch (Exception e) {
			setPermiteMatricularAluno(Boolean.FALSE);
		}
	}
	
	@PostConstruct
	public void realizarCarregamentoAlunoVindoTelaFichaAluno() {
		PessoaVO obj = (PessoaVO) context().getExternalContext().getSessionMap().get("alunoFichaAluno");
		if (obj != null && !obj.getCodigo().equals(0)) {
			try {
				obj.setFormacaoAcademicaVOs(new ArrayList<FormacaoAcademicaVO>(0));
				obj.setFiliacaoVOs(null);
				obj = montarDadosPessoaVOCompleto(obj);
				obj.setNovoObj(Boolean.FALSE);
				setPessoaVO(obj);
				inicializarListasSelectItemTodosComboBox();
				setFiliacaoVO(new FiliacaoVO());
				setConsultarPessoa(Boolean.FALSE);
				setFormacaoAcademicaVO(new FormacaoAcademicaVO());
				setDadosComerciaisVO(new DadosComerciaisVO());
				setFormacaoExtraCurricularVO(new FormacaoExtraCurricularVO());
				setImportarDadosPessoa(false);
				inicializarDadosFotoUsuario();
				if (getPessoaVO().getQtdFilhos() > 0) {
					getPessoaVO().setPossuiFilho(true);
				}
				verificarPermissaoPodeMatricula();						
				registrarAtividadeUsuario(getUsuarioLogado(), "AlunoControle", "Finalizando Editar Aluno", "Editando");
				setHashPessoaInicial(hashPessoa(clonarPessoa(obj)));
				setMensagemID("msg_dados_editar");
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			} finally {
				context().getExternalContext().getSessionMap().remove("alunoFichaAluno");
			}
		}
	}
	
	@PostConstruct
	public void realizarCarregamentoResponsavelFinanceiroVindoTelaFichaAluno() {
		PessoaVO obj = (PessoaVO) context().getExternalContext().getSessionMap().get("responsavelFinanceiroFichaAluno");
		if (obj != null && !obj.getCodigo().equals(0)) {
			try {
				obj.setFormacaoAcademicaVOs(new ArrayList<FormacaoAcademicaVO>(0));
				obj.setFiliacaoVOs(null);
				obj = montarDadosPessoaVOCompleto(obj);
				obj.setNovoObj(Boolean.FALSE);
				setPessoaVO(obj);
				inicializarListasSelectItemTodosComboBox();
				setFiliacaoVO(new FiliacaoVO());
				setConsultarPessoa(Boolean.FALSE);
				setFormacaoAcademicaVO(new FormacaoAcademicaVO());
				setDadosComerciaisVO(new DadosComerciaisVO());
				setFormacaoExtraCurricularVO(new FormacaoExtraCurricularVO());
				setImportarDadosPessoa(false);
				inicializarDadosFotoUsuario();
				if (getPessoaVO().getQtdFilhos() > 0) {
					getPessoaVO().setPossuiFilho(true);
				}
				verificarPermissaoPodeMatricula();						
				registrarAtividadeUsuario(getUsuarioLogado(), "AlunoControle", "Finalizando Editar Aluno", "Editando");
				setHashPessoaInicial(hashPessoa(clonarPessoa(obj)));
				setMensagemID("msg_dados_editar");
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			} finally {
				context().getExternalContext().getSessionMap().remove("responsavelFinanceiroFichaAluno");
			}

			
		}
	}


	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe
	 * <code>Pessoa</code> para edição pelo usuário da aplicação.
	 */
	public String novo() throws Exception {
		registrarAtividadeUsuario(getUsuarioLogado(), "AlunoControle", "Novo Aluno", "Novo");
		removerObjetoMemoria(this);
		setPessoaVO(new PessoaVO());
		setValidaCamposEnadeCenso(false);
		inicializarListasSelectItemTodosComboBox();
		setFiliacaoVO(new FiliacaoVO());
		setFormacaoAcademicaVO(new FormacaoAcademicaVO());
		setDadosComerciaisVO(new DadosComerciaisVO());
		setFormacaoExtraCurricularVO(new FormacaoExtraCurricularVO());
		setVerificarCpf(this.validarCadastroPorCpf());
		setConsultarPessoa(Boolean.TRUE);
		setEditarDados(Boolean.FALSE);
		setHabilitarIniciarInscricao(true);
		setImportarDadosPessoa(false);
		inicializarDadosFotoUsuario();
		setTipoDocumento("CPF");
		verificarTornarCampoMidiaCaptacaoObrigatorio();
		inicializarBooleanoFoto();
		isPermiteConfiguracaoSeiGsuite();
		montartListaSelectItemTitulo();
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("alunoForm");
	}

	public void realizarLiberarBloqueioAlunoInadimplente() {
		MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItem");
		getFacadeFactory().getMatriculaFacade().alterarLiberarBloqueioAlunoInadimplente(obj);
	}

	public void rotacionar90GrausParaEsquerda() {
		try {
			getFacadeFactory().getArquivoHelper().rotacionar90GrausParaEsquerda(getPessoaVO().getArquivoImagem(), getConfiguracaoGeralPadraoSistema());
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public void rotacionar90GrausParaDireita() {
		try {
			getFacadeFactory().getArquivoHelper().rotacionar90GrausParaDireita(getPessoaVO().getArquivoImagem(), getConfiguracaoGeralPadraoSistema());
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public void rotacionar180Graus() {
		try {
			getFacadeFactory().getArquivoHelper().rotacionar180Graus(getPessoaVO().getArquivoImagem(), getConfiguracaoGeralPadraoSistema());
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public void executarZoomIn() {
		getFacadeFactory().getArquivoHelper().executarZoomImagemRequerimento("in", getPessoaVO().getArquivoImagem(), getConfiguracaoGeralPadraoSistema());
	}

	public void executarZoomOut() {
		getFacadeFactory().getArquivoHelper().executarZoomImagemRequerimento("out", getPessoaVO().getArquivoImagem(), getConfiguracaoGeralPadraoSistema());
	}

	public void executarCapturarFotoWebCam() {
		try {
			HttpSession session = (HttpSession) context().getExternalContext().getSession(true);
			getPessoaVO().getArquivoImagem().setCpfRequerimento(getPessoaVO().getCPF());
			String arquivoFoto = getFacadeFactory().getArquivoHelper().getArquivoUploadFoto(getPessoaVO().getArquivoImagem(), getConfiguracaoGeralPadraoSistema(), PastaBaseArquivoEnum.IMAGEM_TMP, getUsuarioLogado());
			String arquivoExterno = getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo() + File.separator + PastaBaseArquivoEnum.IMAGEM_TMP.getValue() + File.separator + getPessoaVO().getCPF() + File.separator + getPessoaVO().getArquivoImagem().getNome();
			session.setAttribute("arquivoFoto", arquivoFoto);
			setExibirBotao(Boolean.TRUE);
			setExibirUpload(false);
			setCaminhoFotoUsuario(arquivoExterno);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public String getUrlWebCam() {
		try {
			String url = request().getRequestURL().toString().substring(0, request().getRequestURL().toString().indexOf(request().getContextPath())) + request().getContextPath();
//			String url = request().getParameter("teste");
//			return "webcam.snap(); webcam.upload(data_uri,'"+ url +"/UploadServlet')";
			return "webcam.snap();webcam.upload('"+ url +"/UploadServlet')";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}

	public void inicializarDadosFotoUsuario() throws Exception {
		getPessoaVO().getArquivoImagem().setCpfRequerimento(getPessoaVO().getCPF());
		setCaminhoFotoUsuario(getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(getPessoaVO().getArquivoImagem(), PastaBaseArquivoEnum.IMAGEM.getValue(), getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb() + File.separator + "resources", "foto_usuario.jpg", false));
	}

	@PostConstruct
	public void realizarInicializacaoPeloServlet() {
		try {
			setApresentarMatriculaCRM(Boolean.FALSE);
			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
			if (request.getAttribute("codigo") == null || ((String) request.getAttribute("codigo")).equals("")) {
				return;
			}
			Integer codigoCompromisso = Integer.parseInt((String) request.getAttribute("codigo"));
			setCompromissoAgendaPessoaHorarioVO(getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().consultarPorChavePrimaria(codigoCompromisso, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));

			// verificar aqui

			setPessoaVO(getFacadeFactory().getProspectsFacade().verificaCriandoPessoaProspect(getCompromissoAgendaPessoaHorarioVO().getProspect(), getUsuarioLogado()));
			setApresentarMatriculaCRM(Boolean.TRUE);
			matriculaDiretaAgendaCRM = Boolean.FALSE;
			request.getSession().setAttribute("matriculaItem", Boolean.FALSE);
		} catch (Exception e) {

		}
	}

	@PostConstruct
	public void iniciarAlunoApartirOutraTela() {
		try {
			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
			if (request.getParameter("pessoa") == null || ((String) request.getParameter("pessoa")).equals("")) {
				return;
			}
			setConsultarPessoa(Boolean.FALSE);
			Integer codigoPessoa = Integer.parseInt((String) request.getParameter("pessoa"));
			if (codigoPessoa == null || codigoPessoa == 0) {
				return;
			}
			PessoaVO obj = new PessoaVO();
			obj.setCodigo(codigoPessoa);
			montarDadosPessoaVOCompleto(obj);
			obj.setNovoObj(Boolean.FALSE);
			setPessoaVO(obj);
			inicializarListasSelectItemTodosComboBox();
			setFiliacaoVO(new FiliacaoVO());
			setConsultarPessoa(Boolean.FALSE);
			setFormacaoAcademicaVO(new FormacaoAcademicaVO());
			setDadosComerciaisVO(new DadosComerciaisVO());
			setFormacaoExtraCurricularVO(new FormacaoExtraCurricularVO());
			setImportarDadosPessoa(false);
			inicializarDadosFotoUsuario();
			if (getPessoaVO().getQtdFilhos() > 0) {
				getPessoaVO().setPossuiFilho(true);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	@PostConstruct
	public void iniciarMatriculaCRMInteracaoWorkflow() {
		try {
			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
			if (request.getParameter("interacaoWorkflow") == null || ((String) request.getParameter("interacaoWorkflow")).equals("")) {
				return;
			}
			setConsultarPessoa(Boolean.FALSE);
			Integer codigoInteracaoWorkflowVO = Integer.parseInt((String) request.getParameter("interacaoWorkflow"));
			if (codigoInteracaoWorkflowVO == null || codigoInteracaoWorkflowVO == 0) {
				return;
			}
			setIdControlador(codigoInteracaoWorkflowVO.toString());
			InteracaoWorkflowVO interacaoWorkflowVO = getFacadeFactory().getInteracaoWorkflowFacade().consultarPorChavePrimaria(codigoInteracaoWorkflowVO, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			setInteracaoWorkflowVO(interacaoWorkflowVO);
			MatriculaVO matricula = new MatriculaVO();
			if (interacaoWorkflowVO.getProspect().getNome().equals("")) {
				throw new Exception("O campo NOME (Prospect) deve ser informado.");
			}
			if (interacaoWorkflowVO.getProspect().getDataNascimento() == null) {
				throw new Exception("O campo DATA NASCIMENTO (Prospect) deve ser informado.");
			}

			PessoaVO pessoaTemp = getFacadeFactory().getPessoaFacade().consultarPorCPFProspects(interacaoWorkflowVO.getProspect().getCpf(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());

			if (pessoaTemp.getCodigo() != 0) {
				pessoaTemp.setAluno(Boolean.TRUE);
				pessoaTemp.setCodProspect(interacaoWorkflowVO.getProspect().getCodigo());
				interacaoWorkflowVO.getProspect().setPessoa(pessoaTemp);
				getFacadeFactory().getProspectsFacade().alterarPessoaProspect(interacaoWorkflowVO.getProspect(), getUsuarioLogado());
				getFacadeFactory().getProspectsFacade().alterarProspectConformePessoa(pessoaTemp, false, getUsuarioLogado());
				setPessoaVO(pessoaTemp);
				getPessoaVO().setNovoObj(Boolean.FALSE);
			} else {
				PessoaVO pessoa = getFacadeFactory().getProspectsFacade().realizarPreenchimentoPessoaPorProspect(interacaoWorkflowVO.getProspect(), getUsuarioLogado());
				pessoa.setAluno(Boolean.TRUE);
				pessoa.setCodProspect(interacaoWorkflowVO.getProspect().getCodigo());
				getPessoaVO().setNovoObj(Boolean.TRUE);
				setPessoaVO(pessoa);
				getFacadeFactory().getPessoaFacade().incluirPessoaProspectMatriculaCRM(getPessoaVO(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema(), false);
			}
			matricula.setUnidadeEnsino(getInteracaoWorkflowVO().getUnidadeEnsino());
			matricula.setCurso(getInteracaoWorkflowVO().getCurso());
			matricula.setTurno(getInteracaoWorkflowVO().getTurno());
			if (getInteracaoWorkflowVO().getResponsavel().getPessoa().getCodigo() > 0) {
				matricula.setConsultor(getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCodigoPessoa(getInteracaoWorkflowVO().getResponsavel().getPessoa().getCodigo(), false, getUsuarioLogado()));
			}
			matricula.setGuiaAba("DadosBasicos");
			matricula.setAluno(getPessoaVO());
			setMatriculaVO(matricula);
			getMatriculaVO().setUsuario(getUsuarioLogadoClone());
			apresentarMatriculaCRM = Boolean.TRUE;
			matriculaDiretaAgendaCRM = Boolean.FALSE;
			habilitarIniciarInscricao = Boolean.FALSE;
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	@PostConstruct
	public void iniciarMatriculaCRMFollowUpWorkflow() {
		try {
			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
			if (request.getParameter("prospect") == null || ((String) request.getParameter("prospect")).equals("")) {
				return;
			}
			setConsultarPessoa(Boolean.FALSE);
			Integer codigoProspectVO = Integer.parseInt((String) request.getParameter("prospect"));
			if (codigoProspectVO == null || codigoProspectVO == 0) {
				return;
			}
			ProspectsVO prospectsVO = new ProspectsVO();
			prospectsVO.setCodigo(codigoProspectVO);
			getFacadeFactory().getProspectsFacade().carregarDados(prospectsVO, getUsuarioLogado());
			MatriculaVO matricula = new MatriculaVO();
			if (prospectsVO.getNome().equals("")) {
				throw new Exception("O campo NOME (Prospect) deve ser informado.");
			}
			if (prospectsVO.getDataNascimento() == null) {
				throw new Exception("O campo DATA NASCIMENTO (Prospect) deve ser informado.");
			}

			PessoaVO pessoaTemp = prospectsVO.getCpf().trim().isEmpty() ? new PessoaVO() : getFacadeFactory().getPessoaFacade().consultarPorCPFProspects(prospectsVO.getCpf(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());

			if (pessoaTemp.getCodigo() != 0) {
				pessoaTemp.setAluno(Boolean.TRUE);
				pessoaTemp.setCodProspect(prospectsVO.getCodigo());
				
				prospectsVO.setPessoa(pessoaTemp);
				getFacadeFactory().getProspectsFacade().alterarPessoaProspect(prospectsVO, getUsuarioLogado());
				getFacadeFactory().getProspectsFacade().alterarProspectConformePessoa(pessoaTemp, false, getUsuarioLogado());
				setPessoaVO(pessoaTemp);
				getPessoaVO().setNovoObj(Boolean.FALSE);
			} else {
				PessoaVO pessoa = getFacadeFactory().getProspectsFacade().realizarPreenchimentoPessoaPorProspect(prospectsVO, getUsuarioLogado());
				pessoa.setAluno(Boolean.TRUE);
				pessoa.setCodProspect(prospectsVO.getCodigo());
				getPessoaVO().setNovoObj(Boolean.TRUE);
				setPessoaVO(pessoa);
				getFacadeFactory().getPessoaFacade().incluirPessoaProspectMatriculaCRM(getPessoaVO(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema(), false);
				ProspectsVO prospectsAlterar = new ProspectsVO();
				prospectsAlterar.setCodigo(prospectsVO.getCodigo());
				prospectsAlterar.setPessoa(getPessoaVO());
				getFacadeFactory().getProspectsFacade().alterarPessoaProspect(prospectsAlterar, getUsuarioLogado());
			}
			if (Uteis.isAtributoPreenchido(getPessoaVO().getCodProspect())) {
				  getFacadeFactory().getFiliacaoFacade().incluirFiliacaoConformeProspect(getPessoaVO().getCodProspect(), getPessoaVO().getFiliacaoVOs(), getUsuario());
				}
			if (prospectsVO.getConsultorPadrao().getPessoa().getCodigo() > 0) {
				matricula.setConsultor(getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCodigoPessoa(prospectsVO.getConsultorPadrao().getPessoa().getCodigo(), false, getUsuarioLogado()));
			}
			matricula.setGuiaAba("DadosBasicos");
			matricula.setAluno(getPessoaVO());
			setMatriculaVO(matricula);
			getMatriculaVO().setUsuario(getUsuarioLogadoClone());
			apresentarMatriculaCRM = Boolean.FALSE;
			matriculaDiretaAgendaCRM = Boolean.FALSE;
			habilitarIniciarInscricao = Boolean.TRUE;
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	@PostConstruct
	public void iniciarMatriculaCRMAgenda() {
		try {
			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
			if (request.getParameter("iniciarMatriculaAgenda") == null || ((String) request.getParameter("iniciarMatriculaAgenda")).equals("")) {
				return;
			}
			setConsultarPessoa(Boolean.FALSE);
			InteracaoWorkflowVO interacaoWorkflowVO = (InteracaoWorkflowVO) request.getSession().getAttribute("interacaoWorkFlowMatriculaAgenda");
			setInteracaoWorkflowVO(interacaoWorkflowVO);
			MatriculaVO matricula = new MatriculaVO();
			if (interacaoWorkflowVO.getProspect().getNome().equals("")) {
				throw new Exception("O campo NOME (Prospect) deve ser informado.");
			}
			// if (interacaoWorkflowVO.getProspect().getNome().equals("")) {
			// throw new
			// Exception("O campo DATA NASCIMENTO (Prospect) deve ser informado.");
			// }

			PessoaVO pessoa = getFacadeFactory().getProspectsFacade().realizarPreenchimentoPessoaPorProspect(interacaoWorkflowVO.getProspect(), getUsuarioLogado());

			PessoaVO pessoaTemp = getFacadeFactory().getPessoaFacade().consultarPorCPFProspects(interacaoWorkflowVO.getProspect().getCpf(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());

			if (pessoaTemp.getCodigo() != 0) {
				pessoa = pessoaTemp;
				pessoa.setAluno(Boolean.TRUE);
				pessoa.setCodProspect(interacaoWorkflowVO.getProspect().getCodigo());
				getFacadeFactory().getProspectsFacade().realizarPreenchimentoPessoaPorProspect(pessoa, interacaoWorkflowVO.getProspect(), getUsuarioLogado());
				setPessoaVO(pessoa);
				getPessoaVO().setNovoObj(Boolean.FALSE);
			} else {
				pessoa.setAluno(Boolean.TRUE);
				pessoa.setCodProspect(interacaoWorkflowVO.getProspect().getCodigo());
				getPessoaVO().setNovoObj(Boolean.TRUE);
				setPessoaVO(pessoa);
				getFacadeFactory().getPessoaFacade().incluirPessoaProspectMatriculaCRM(getPessoaVO(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema(), false);
			}
			setVerificarCpf(this.validarCadastroPorCpf());
			matricula.setUnidadeEnsino(getInteracaoWorkflowVO().getUnidadeEnsino());
			matricula.setCurso(getInteracaoWorkflowVO().getCurso());
			matricula.setTurno(getInteracaoWorkflowVO().getTurno());
			matricula.setConsultor(getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCodigoPessoa(getInteracaoWorkflowVO().getResponsavel().getPessoa().getCodigo(), false, getUsuarioLogado()));
			matricula.setGuiaAba("DadosBasicos");
			matricula.setAluno(pessoa);
			setMatriculaVO(matricula);
			getMatriculaVO().setUsuario(getUsuarioLogadoClone());
			apresentarMatriculaCRM = Boolean.TRUE;
			matriculaDiretaAgendaCRM = Boolean.TRUE;
			habilitarIniciarInscricao = Boolean.FALSE;

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	@PostConstruct
	public void iniciarPessoaEmprestimoMembroComunidade() {
		try {
			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
			HttpSession session = request.getSession(true);
			if (session.getAttribute("iniciarPessoaEmprestimoMembroComunidade") == null || (!(Boolean) session.getAttribute("iniciarPessoaEmprestimoMembroComunidade"))) {
				return;
			}
			novo();
			setHabilitarIniciarInscricao(false);
			getPessoaVO().setAluno(false);
			getPessoaVO().setFuncionario(false);
			getPessoaVO().setProfessor(false);
			getPessoaVO().setMembroComunidade(true);
			session.removeAttribute("iniciarPessoaEmprestimoMembroComunidade");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}


	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe
	 * <code>Pessoa</code> para alteração. O objeto desta classe é
	 * disponibilizado na session da página (request) para que o JSP
	 * correspondente possa disponibilizá-lo para edição.
	 */
	public String editar() {
		try {
			setExibirBotao(false);
			setExibirUpload(true);
			registrarAtividadeUsuario(getUsuarioLogado(), "AlunoControle", "Inicializando Editar Aluno", "Editando");
			PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("pessoaItem");
			editarDadosPessoa(obj);
			registrarAtividadeUsuario(getUsuarioLogado(), "AlunoControle", "Finalizando Editar Aluno", "Editando");
			setMensagemID("msg_dados_editar");
			//System.out.println("Editar: Ok");
			return Uteis.getCaminhoRedirecionamentoNavegacao("alunoForm.xhtml");
		} catch (Exception e) {
			//System.out.println("Erro: " + e.getMessage());
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("alunoForm.xhtml");
		}
	}

	public PessoaVO montarDadosPessoaVOCompleto(PessoaVO obj) throws Exception {
		try {
			getFacadeFactory().getPessoaFacade().carregarDados(obj, getUsuarioLogado());
			isPermiteConfiguracaoSeiGsuite();
			//System.out.println("Editar: Ok");
			return obj;
		} catch (Exception e) {
			//System.out.println("Erro: " + e.getMessage());
			throw e;
		}
	}

	public String editarDadosPessoa() throws Exception {
		try {
			editarDadosPessoa(getPessoaVO());
			isPermiteConfiguracaoSeiGsuite();
			setMensagemID("msg_dados_editar");
			return Uteis.getCaminhoRedirecionamentoNavegacao("alunoForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}
	
	public void editarDadosPessoa(PessoaVO obj) throws Exception  {
		setValidaCamposEnadeCenso(false);
		obj.setFormacaoAcademicaVOs(new ArrayList<FormacaoAcademicaVO>(0));
		obj = montarDadosPessoaVOCompleto(obj);
		obj.setNovoObj(Boolean.FALSE);
		getPessoaVO().setContaCorrente(obj.getContaCorrente());
		getPessoaVO().setAgencia(obj.getAgencia());
		getPessoaVO().setBanco(obj.getBanco());
		inicializarListasSelectItemTodosComboBox();
		if (Uteis.isAtributoPreenchido(obj.getCodProspect())) {
		  getFacadeFactory().getFiliacaoFacade().incluirFiliacaoConformeProspect(obj.getCodProspect(), getPessoaVO().getFiliacaoVOs(), getUsuario());
		}
		obj.setListaPessoaEmailInstitucionalVO(getFacadeFactory().getPessoaEmailInstitucionalFacade().consultarPorPessoaFuncionario(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
		for (PessoaEmailInstitucionalVO pessoaEmailInstitucionalVO : obj.getListaPessoaEmailInstitucionalVO()) {
			try {
				if (pessoaEmailInstitucionalVO.getStatusAtivoInativoEnum().equals(StatusAtivoInativoEnum.ATIVO)) {
					pessoaEmailInstitucionalVO.getApresentarBotaoAtivar();
				} else {
					pessoaEmailInstitucionalVO.getApresentarBotaoAtivar();
				}
			} catch (Exception e) {
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			}
		}
		setPessoaVO(obj);
		setFiliacaoVO(new FiliacaoVO());
		setConsultarPessoa(Boolean.FALSE);
		setFormacaoAcademicaVO(new FormacaoAcademicaVO());
		setDadosComerciaisVO(new DadosComerciaisVO());
		setFormacaoExtraCurricularVO(new FormacaoExtraCurricularVO());
		setImportarDadosPessoa(false);
		inicializarDadosFotoUsuario();
		if (getPessoaVO().getQtdFilhos() > 0) {
			getPessoaVO().setPossuiFilho(true);
		}
		verificarPermissaoPodeMatricula();
		verificarPermissaoLiberarEdicaoAlunoSomenteComSenha();
		verificarPermissaoLiberarEdicaoRegistroAcademicoAluno();
		setVerificarCpf(this.validarCadastroPorCpf());
		verificarTornarCampoMidiaCaptacaoObrigatorio();
		montartListaSelectItemTitulo();
		setHashPessoaInicial(hashPessoa(clonarPessoa(obj)));
	}
	
	public void changeEmpregoAtual(ValueChangeEvent event){
		this.dadosComerciaisVO.setEmpregoAtual((Boolean)event.getNewValue());  
	}

	public String fechar() {
		setMensagemDetalhada("", "");
		return Uteis.getCaminhoRedirecionamentoNavegacao("alunoCons");
	}

	public void carregarEnderecoPessoaFiliacao() {
		try {
			getFacadeFactory().getEnderecoFacade().carregarEndereco(getFiliacaoVO().getPais(), getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	

	public void carregarEnderecoPessoa() {
		try {
			getFacadeFactory().getEnderecoFacade().carregarEndereco(getPessoaVO(), getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void carregarEnderecoFiador() {
		try {
			List<EnderecoVO> listaEndereco = getFacadeFactory().getEnderecoFacade().consultarPorCep(getPessoaVO().getCepFiador(), false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado());
	        if (listaEndereco.size() == 0) {
	        	 getPessoaVO().setEnderecoFiador("");
	        	 getPessoaVO().setSetorFiador("");
	        	 getPessoaVO().setCidadeFiador(new CidadeVO());
	        } else {
	            EnderecoVO enderecoVO = listaEndereco.get(0);
	            getPessoaVO().setEnderecoFiador(enderecoVO.getLogradouro());
	            getPessoaVO().setSetorFiador(enderecoVO.getBairrocodigo().getDescricao());
	            getPessoaVO().setCidadeFiador(enderecoVO.getBairrocodigo().getCidade());
	        }
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void selecionarCidadeFiador() {
		CidadeVO obj = (CidadeVO) context().getExternalContext().getRequestMap().get("cidadeItem");
		getPessoaVO().setCidadeFiador(obj);
		listaConsultaCidade.clear();
		this.setValorConsultaCidade("");
		this.setCampoConsultaCidade("");
	}

	public void carregarEnderecoEmpresaPessoa() {
		try {
			getFacadeFactory().getEnderecoFacade().carregarEnderecoEmpresa(getDadosComerciaisVO(), getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void carregarFiliacaoPessoa() {
		try {
			setFiliacaoVO(getFacadeFactory().getFiliacaoFacade().carregarApenasUmPorCPF(getFiliacaoVO(), getPessoaVO(), true, getUsuarioLogado()));
			if (getFiliacaoVO().getPais().getCodigo().intValue() == 0 && getFiliacaoVO().getPais().getCEP().isEmpty()) {				
				getFiliacaoVO().getPais().setCEP(getPessoaVO().getCEP());
				getFiliacaoVO().getPais().setEndereco(getPessoaVO().getEndereco());
				getFiliacaoVO().getPais().setSetor(getPessoaVO().getSetor());
				getFiliacaoVO().getPais().setNumero(getPessoaVO().getNumero());
				getFiliacaoVO().getPais().setComplemento(getPessoaVO().getComplemento());
				getFiliacaoVO().getPais().setCidade(getPessoaVO().getCidade());
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto
	 * da classe <code>Pessoa</code>. Caso o objeto seja novo (ainda não gravado
	 * no BD) é acionado a operação <code>incluir()</code>. Caso contrário é
	 * acionado o <code>alterar()</code>. Se houver alguma inconsistência o
	 * objeto não é gravado, sendo re-apresentado para o usuário juntamente com
	 * uma mensagem de erro.
	 * 
	 * @throws Exception
	 */
	public void gravar() throws Exception {
		try {
			pessoaVO.setAluno(true);
			getFacadeFactory().getPessoaFacade().setIdEntidade("Aluno");
			getPessoaVO().inicializarDadosNecessidadeGerarProspectPelaConfiguracaoGeralSistemaTipoOrigem(getConfiguracaoGeralPadraoSistema(), "ALUNO");
			if (!Uteis.isAtributoPreenchido(getPessoaVO().getCPF()) && !Uteis.isAtributoPreenchido(getPessoaVO().getCertidaoNascimento())) {
				throw new Exception("É Obrigatório Preencher o Campo CPF ou Certidão de Nascimento(Documentos Pessoais)");
			}
			getPessoaVO().setGerarNumeroCPF(getPessoaVO().getIdade() <= 18 && !Uteis.isAtributoPreenchido(getPessoaVO().getCPF()));
			if (!getVerificarCpf() && !Uteis.isAtributoPreenchido(getPessoaVO().getCPF())) {
				getPessoaVO().setGerarNumeroCPF(Boolean.TRUE);
			} else if (getVerificarCpf() && Uteis.isAtributoPreenchido(getPessoaVO().getCPF()) && ((Uteis.isAtributoPreenchido(getPessoaVO().getDataNasc()) && getPessoaVO().getIdadeAluno() > 18) || 
					(getPessoaVO().getIdadeAluno() <= 18 && !getPessoaVO().getCPF().contains("T")))) {
				if (!Uteis.verificaCPF(getPessoaVO().getCPF())) {
					throw new Exception("CPF Inválido (Documentos Pessoais)");
				}
			} 
			ConfiguracaoGeralSistemaVO conf = getAplicacaoControle().getConfiguracaoGeralSistemaVO(getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado()); 
			if (pessoaVO.isNovoObj().booleanValue()) {				
				getPessoaVO().setValorCssTopoLogo(conf.getVisaoPadraoAluno().getValorCssTopoLogo());
				getPessoaVO().setValorCssBackground(conf.getVisaoPadraoAluno().getValorCssBackground());
				getPessoaVO().setValorCssMenu(conf.getVisaoPadraoAluno().getValorCssMenu());
				registrarAtividadeUsuario(getUsuarioLogado(), "AlunoControle", "Inicializando Incluir Aluno","Incluir");
				getFacadeFactory().getPessoaFacade().persistirPessoaComEmailInstitucional(pessoaVO, true, getUsuarioLogado(),conf, false, true, false, getValidaCamposEnadeCenso(), true);
				registrarAtividadeUsuario(getUsuarioLogado(), "AlunoControle", "Finalizando Incluir Aluno", "Incluir");
			} else {
				if (getPessoaVO().getCodProspect().equals(0) && !Uteis.isAtributoPreenchido(getPessoaVO().getCPF())) {
					throw new Exception("Informe o CPF (Documentos Pessoais)");					
				} else {
					getPessoaVO().setGerarNumeroCPF(!getPessoaVO().getCodProspect().equals(0) && !Uteis.isAtributoPreenchido(getPessoaVO().getCPF()) && getPessoaVO().getIdadeAluno() <= 18);	
				}
				registrarAtividadeUsuario(getUsuarioLogado(), "AlunoControle", "Inicializando Alterar Aluno","Alterando");
				getFacadeFactory().getPessoaFacade().persistirPessoaComEmailInstitucional(pessoaVO, true, getUsuarioLogado(),conf, false, true, false, getValidaCamposEnadeCenso(), true);
				int hashPessoaAtualizada = hashPessoa(clonarPessoa(getPessoaVO()));
				ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoPadraoSistema();
				getFacadeFactory().getIntegracaoMestreGRInterfaceFacade().atualizarAlunoIntegrado(getPessoaVO(), getHashPessoaInicial(), hashPessoaAtualizada, getUsuarioLogado(), configuracaoGeralSistemaVO);
				setHashPessoaInicial(hashPessoaAtualizada);
				registrarAtividadeUsuario(getUsuarioLogado(), "AlunoControle", "Finalizando Alterar Aluno","Alterando");
			}
			verificarPermissaoAlterarResponsavelFinanceiroContas();
			inicializarDadosFotoUsuario();
			verificarPermissaoPodeMatricula();
			setMensagemID("msg_dados_gravados");
//			return Uteis.getCaminhoRedirecionamentoNavegacao("alunoForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
//			return Uteis.getCaminhoRedirecionamentoNavegacao("alunoForm.xhtml");
		} finally {
			removerImagensUploadArquivoTemp();
		}
	}

	private PessoaVO clonarPessoa(PessoaVO pessoaVO) {
		PessoaVO copia = new PessoaVO();
		BeanUtils.copyProperties(pessoaVO, copia);
		copia.setListaPessoaEmailInstitucionalVO(pessoaVO.getListaPessoaEmailInstitucionalVO());
		return copia;
	}

	private int hashPessoa(PessoaVO pessoaVO) {
		int hash = Objects.hash(
				pessoaVO.getNome(),
				pessoaVO.getRegistroAcademico(),
				pessoaVO.getListaPessoaEmailInstitucionalVO(),
				pessoaVO.getTempoEstendidoProva(),
				pessoaVO.getSabatista()
		);
		return hash;
	}


	public void verificarPermissaoAlterarResponsavelFinanceiroContas() throws Exception {
		try {
			if(getFacadeFactory().getContaReceberFacade().verificarExistenciaDeContaParaResponsavelFinanceiroDiferenteDoAtual(getPessoaVO())){
				ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("DefinirResponsavelFinanceiro", getUsuarioLogado());
				setPossuiPermissaoAlterarResponsavelFinanceiroContas(Boolean.TRUE);
			}
		} catch (Exception e) {
			setPossuiPermissaoAlterarResponsavelFinanceiroContas(Boolean.FALSE);
		}
	}

	private Boolean possuiPermissaoAlterarResponsavelFinanceiroContas; 
	
	public Boolean getPossuiPermissaoAlterarResponsavelFinanceiroContas() {
		if(possuiPermissaoAlterarResponsavelFinanceiroContas == null){
			possuiPermissaoAlterarResponsavelFinanceiroContas = Boolean.FALSE;
		}
		return possuiPermissaoAlterarResponsavelFinanceiroContas;
	}

	public void setPossuiPermissaoAlterarResponsavelFinanceiroContas(Boolean possuiPermissaoAlterarResponsavelFinanceiroContas) {
		this.possuiPermissaoAlterarResponsavelFinanceiroContas = possuiPermissaoAlterarResponsavelFinanceiroContas;
	}

	public String getAbrirPopUpAlterarResponsavelFinanceiro(){
		return "abrirPopup('../../visaoAdministrativo/financeiro/definirResponsavelFinanceiro.xhtml', 'DefinirResponsavelFinanceiro', 950, 595)";
	}
	
	public String getAbrirModalConfirmacaoAlterarResponsavelContas(){
		if (getPossuiPermissaoAlterarResponsavelFinanceiroContas()) {
			setPossuiPermissaoAlterarResponsavelFinanceiroContas(Boolean.FALSE);
			return "RichFaces.$('panelConfirmacaoAlterarResponsavelContas').show()";
		}
		return "";
	}
	
	public void carregarDadosParaTelaAlterarResponsavelFinanceiro(){
		try {
			List<MatriculaVO> matriculas = getFacadeFactory().getMatriculaFacade().consultarPorPessoaSituacao(getPessoaVO().getCodigo(), "AT", 0, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			context().getExternalContext().getSessionMap().put("matriculaVOTelaDefinirResponsavelFinanceiro", matriculas.get(0));
			
			for (FiliacaoVO filiacao : getPessoaVO().getFiliacaoVOs()) {
				if(filiacao.getResponsavelFinanceiro()){
					context().getExternalContext().getSessionMap().put("responsavelFinanceiroInformadoTelaDefinirResponsavelFinanceiro", filiacao.getPais());
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	public String alterarMatriculaPeriodo() {
		try {
			getFacadeFactory().getMatriculaPeriodoFacade().alterarReconheceuDivida(matriculaVO);
			setMensagemID("msg_dados_gravados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("fichaAlunoForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}

	public void paint(OutputStream out, Object data) throws Exception {
		ArquivoHelper arquivoHelper = new ArquivoHelper();
		try {
			arquivoHelper.renderizarImagemNaTela(out, getPessoaVO().getArquivoImagem(), getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(), "foto_usuario.jpg");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			arquivoHelper = null;
		}
	}

	public long getTimeStamp() {
		return System.currentTimeMillis();
	}

	public void paintConsulta(OutputStream out, Object data) throws Exception {
		ArquivoHelper arquivoHelper = new ArquivoHelper();
		List<PessoaVO> listaConsultas = getListaConsulta();
		try {
			arquivoHelper.renderizarImagemNaTela(out, listaConsultas.get((Integer) data).getArquivoImagem(), getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(), "foto_usuario.jpg");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			arquivoHelper = null;
			listaConsultas = null;
		}
	}

	public void consultarHorariosNaoDisponiveis() {
		try {
			setAgendaPessoaHorarioVO(getFacadeFactory().getAgendaPessoaHorarioFacade().consultarAPartirDiaMesAnoPorCodigoProspect(getPessoaVO().getCodProspect(), getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado().getPessoa().getCodigo(), getInteracaoWorkflowVO().getCampanha().getCodigo(), new Date(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void editarCompromissoAgendaPessoaHorario() {
		try {
			CompromissoAgendaPessoaHorarioVO obj = (CompromissoAgendaPessoaHorarioVO) context().getExternalContext().getRequestMap().get("compromissoItem");
			setCompromissoAgendaPessoaHorarioVO(obj);
			setMensagemDetalhada("", "");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}

	}

	public void alterarCompromissoAgendaPessoaHorario() {
		try {
			getCompromissoAgendaPessoaHorarioVO().setAgendaPessoaHorario(getAgendaPessoaHorarioVO());
			getFacadeFactory().getAgendaPessoaFacade().adicionarCompromissoAgendaPessoaHorarioRealizandoValidacaoSeExisteAgendaHorario(getCompromissoAgendaPessoaHorarioVO(), getCompromissoAgendaPessoaHorarioVO().getAgendaPessoaHorario().getAgendaPessoa(), getUsuarioLogado());
			consultarHorariosNaoDisponiveis();
			setPermitirAlterarCompromisso(true);
			setMensagemDetalhada("", "");
		} catch (Exception e) {
			try {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
				setPermitirAlterarCompromisso(false);
			} catch (Exception ex) {
				setMensagemDetalhada("msg_erro", ex.getMessage(), Uteis.ERRO);
			}
		}
	}

	public String getFecharRichModalCompromisso() {
		if (getPermitirAlterarCompromisso()) {
			return "RichFaces.$('panelNovoCompromisso').hide();";
		}
		return "";
	}

	public void removerCompromissoAgendaPessoaHorario() {
		try {
			CompromissoAgendaPessoaHorarioVO obj = (CompromissoAgendaPessoaHorarioVO) context().getExternalContext().getRequestMap().get("compromissoItem");
			getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().excluir(obj, getUsuarioLogado());
			setCompromissoAgendaPessoaHorarioVO(new CompromissoAgendaPessoaHorarioVO());
			consultarHorariosNaoDisponiveis();
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void anularDataModelo() {
		setControleConsultaOtimizado(null);
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP
	 * ContaReceberCons.jsp. Define o tipo de consulta a ser executada, por meio
	 * de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
	 * resultado, disponibiliza um List com os objetos selecionados na sessao da
	 * pagina.
	 */
	public void scrollerListener(DataScrollEvent DataScrollEvent) throws Exception {
		getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
		getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
		consultar();
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP
	 * PessoaCons.jsp. Define o tipo de consulta a ser executada, por meio de
	 * ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
	 * resultado, disponibiliza um List com os objetos selecionados na sessao da
	 * pagina.
	 */
	public String consultar() {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "AlunoControle", "Inicializando Consultar Aluno", "Consultando");
			super.consultar();
			getControleConsultaOtimizado().getListaConsulta().clear();
			getControleConsultaOtimizado().setLimitePorPagina(10);
			getFacadeFactory().getPessoaFacade().setIdEntidade("Aluno");

//			if (getControleConsulta().getCampoConsulta().equals("matricula")) {
//				getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getPessoaFacade().consultaRapidaResumidaPorMatricula(getControleConsulta().getValorConsulta(), TipoPessoa.ALUNO.getValor(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
//				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getPessoaFacade().consultaTotalDeRegistroRapidaResumidaPorMatricula(getControleConsulta().getValorConsulta(), TipoPessoa.ALUNO.getValor(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
//			}			
			if (getControleConsulta().getCampoConsulta().equals("registroAcademico")) {			
		    	getControleConsultaOtimizado().setPaginaAtual(1);
				getControleConsultaOtimizado().setPage(1); 				
				getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getPessoaFacade().consultaRapidaResumidaPorRegistroAcademico(getControleConsulta().getValorConsulta(), TipoPessoa.ALUNO.getValor(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getControleConsultaOtimizado() , getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("emailInstitucional")) {					    	
				getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getPessoaFacade().consultaRapidaResumidaPorEmailInstitucional(getControleConsulta().getValorConsulta(), TipoPessoa.ALUNO.getValor(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getControleConsultaOtimizado() , getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("nome")) {
				getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getPessoaFacade().consultaRapidaResumidaPorNome(getControleConsulta().getValorConsulta(), TipoPessoa.ALUNO.getValor(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getPessoaFacade().consultaTotalDeRegistroRapidaResumidaPorNome(getControleConsulta().getValorConsulta(), TipoPessoa.ALUNO.getValor(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeMae")) {
				getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getPessoaFacade().consultaRapidaResumidaPorNomeMae(getControleConsulta().getValorConsulta(), TipoPessoa.ALUNO.getValor(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getPessoaFacade().consultaTotalDeRegistroRapidaResumidaPorNomeMae(getControleConsulta().getValorConsulta(), TipoPessoa.ALUNO.getValor(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("CPF")) {
				getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getPessoaFacade().consultaRapidaResumidaPorCPF(getControleConsulta().getValorConsulta(), TipoPessoa.ALUNO.getValor(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getPessoaFacade().consultaTotalDeRegistroRapidaResumidaPorCPF(getControleConsulta().getValorConsulta(), TipoPessoa.ALUNO.getValor(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("RG")) {
				getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getPessoaFacade().consultaRapidaResumidaPorRG(getControleConsulta().getValorConsulta(), TipoPessoa.ALUNO.getValor(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getPessoaFacade().consultaTotalDeRegistroRapidaResumidaPorRG(getControleConsulta().getValorConsulta(), TipoPessoa.ALUNO.getValor(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			if(getControleConsulta().getCampoConsulta().equals("nomePai")){
				getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getPessoaFacade().consultaRapidaResumidaPorNomePai(getControleConsulta().getValorConsulta(), TipoPessoa.ALUNO.getValor(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getPessoaFacade().consultaTotalRegistroRapidaResumidaPorNomePai(getControleConsulta().getValorConsulta(), TipoPessoa.ALUNO.getValor(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			if(getControleConsulta().getCampoConsulta().equals("responsavelLegal")){
				getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getPessoaFacade().consultaRapidaResumidaPorResponsavelLegal(getControleConsulta().getValorConsulta(), TipoPessoa.ALUNO.getValor(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getPessoaFacade().consultaTotalRegistroRapidaResumidaPorResponsavelLegal(getControleConsulta().getValorConsulta(), TipoPessoa.ALUNO.getValor(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			registrarAtividadeUsuario(getUsuarioLogado(), "AlunoControle", "Finalizando Consultar Aluno", "Consultando");
			setMensagemID("msg_dados_consultados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("alunoCons.xhtml");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("alunoCons.xhtml");
		}
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe
	 * <code>PessoaVO</code> Após a exclusão ela automaticamente aciona a rotina
	 * para uma nova inclusão.
	 */
	public String excluir() {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "AlunoControle", "Inicializando Excluir Aluno", "Excluindo");
			getFacadeFactory().getPessoaFacade().setIdEntidade("Aluno");
			getFacadeFactory().getPessoaFacade().excluir(pessoaVO, getUsuarioLogado());
			setPessoaVO(new PessoaVO());
			setFiliacaoVO(new FiliacaoVO());
			setFormacaoAcademicaVO(new FormacaoAcademicaVO());
			setDadosComerciaisVO(new DadosComerciaisVO());
			setFormacaoExtraCurricularVO(new FormacaoExtraCurricularVO());
			setVerificarCpf(this.validarCadastroPorCpf());
			setConsultarPessoa(Boolean.TRUE);
			setEditarDados(Boolean.FALSE);
			registrarAtividadeUsuario(getUsuarioLogado(), "AlunoControle", "Finalizando Excluir Aluno", "Excluindo");
			setMensagemID("msg_dados_excluidos");
			return Uteis.getCaminhoRedirecionamentoNavegacao("alunoForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("alunoForm.xhtml");
		}
	}

	/*
	 * Método responsável por adicionar um novo objeto da classe
	 * <code>Filiacao</code> para o objeto <code>pessoaVO</code> da classe
	 * <code>Pessoa</code>
	 */
	public void adicionarFiliacao() {
		try {
			if (!getPessoaVO().getCodigo().equals(0)) {
				filiacaoVO.setAluno(getPessoaVO().getCodigo());
			}
			if (getPessoaVO().getCPF().equals(getFiliacaoVO().getCPF())) {
				throw new ConsistirException("O campo CPF do responsável deve ser diferente do aluno.");
			}
			if (getFiliacaoVO().getCep().equals("")) {
				getFiliacaoVO().setCep(getPessoaVO().getCEP());
			}
			if (getFiliacaoVO().getResponsavelFinanceiro()) {
				if (!Uteis.isAtributoPreenchido(getFiliacaoVO().getCPF())) {
					throw new ConsistirException("O campo CPF do responsável financeiro deve ser informado!");
				}
				if (!Uteis.validaCPF(Uteis.removerAcentos(Uteis.removeCaractersEspeciais(getFiliacaoVO().getCPF())).replaceAll(" ", ""))) {
					throw new ConsistirException("O campo CPF do responsável financeiro é inválido (" + getFiliacaoVO().getCPF()+ ")!");
				}
				if (getFiliacaoVO().getEndereco().equals("")) {
					getFiliacaoVO().setEndereco(getPessoaVO().getEndereco());
				}
				if (getFiliacaoVO().getSetor().equals("")) {
					getFiliacaoVO().setSetor(getPessoaVO().getSetor());
				}
				if (getFiliacaoVO().getCidade().getCodigo().intValue() == 0) {
					getFiliacaoVO().setCidade(getPessoaVO().getCidade());
				}
			}			
			getPessoaVO().adicionarObjFiliacaoVOs(getFiliacaoVO());
			this.setFiliacaoVO(new FiliacaoVO());
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/*
	 * Método responsável por disponibilizar dados de um objeto da classe
	 * <code>Filiacao</code> para edição pelo usuário.
	 */
	public void editarFiliacao() {
		try {
			FiliacaoVO obj = (FiliacaoVO) context().getExternalContext().getRequestMap().get("filiacaoItem");
			setFiliacaoVO(obj.getClone());
			setMensagemID("msg_dados_selecionados", Uteis.SUCESSO, true);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

	}

	/*
	 * Método responsável por remover um novo objeto da classe
	 * <code>Filiacao</code> do objeto <code>pessoaVO</code> da classe
	 * <code>Pessoa</code>
	 */
	public void removerFiliacao()  {
		try {
			FiliacaoVO obj = (FiliacaoVO) context().getExternalContext().getRequestMap().get("filiacaoItem");
			getPessoaVO().excluirObjFiliacaoVOs(obj.getNome());
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/*
	 * Método responsável por adicionar um novo objeto da classe
	 * <code>FormacaoAcademica</code> para o objeto <code>pessoaVO</code> da
	 * classe <code>Pessoa</code>
	 */
	public void adicionarFormacaoAcademica() throws Exception {
		try {
			if (!getPessoaVO().getCodigo().equals(0)) {
				formacaoAcademicaVO.setPessoa(getPessoaVO().getCodigo());
			}
			getPessoaVO().adicionarObjFormacaoAcademicaVOs(getFormacaoAcademicaVO());
			this.setFormacaoAcademicaVO(new FormacaoAcademicaVO());
			montartListaSelectItemTitulo();
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/*
	 * Método responsável por disponibilizar dados de um objeto da classe
	 * <code>FormacaoAcademica</code> para edição pelo usuário.
	 */
	public void editarFormacaoAcademica() throws Exception {
		try {
		if(isLiberarEdicaoAlunoSomenteComSenha()) {
			throw new Exception();
		}
		FormacaoAcademicaVO obj = (FormacaoAcademicaVO) context().getExternalContext().getRequestMap().get("formacaoAcademicaItem");
		setFormacaoAcademicaVO(obj);
		montartListaSelectItemTitulo();
		} catch (Exception e) {
			setMensagemID("Para Editar Libere a EDIÇÃO ALUNO!" , Uteis.ALERTA, true);
		}
	}

	/*
	 * Método responsável por adicionar um novo objeto da classe
	 * <code>FormacaoAcademica</code> para o objeto <code>pessoaVO</code> da
	 * classe <code>Pessoa</code>
	 */

	public void adicionarDadosComerciais() {
		try {
			if (!getPessoaVO().getCodigo().equals(0)) {
				getDadosComerciaisVO().setPessoa(getPessoaVO());
			}
			getFacadeFactory().getDadosComerciaisFacade().adicionarObjDadosComerciaisVOs(getDadosComerciaisVO(), getPessoaVO());
			this.setDadosComerciaisVO(new DadosComerciaisVO());
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/*
	 * Método responsável por disponibilizar dados de um objeto da classe
	 * <code>FormacaoAcademica</code> para edição pelo usuário.
	 */
	public void editarDadosComerciais() throws Exception {
		DadosComerciaisVO obj = (DadosComerciaisVO) context().getExternalContext().getRequestMap().get("dadosComerciaisItem");
		setDadosComerciaisVO(obj);
	}

	/**
	 * Método responsável por remover um novo objeto da classe
	 * <code>FormacaoAcademica</code> do objeto <code>pessoaVO</code> da classe
	 * <code>Pessoa</code>
	 **/
	public void removerDadosComerciais() throws Exception {
		DadosComerciaisVO obj = (DadosComerciaisVO) context().getExternalContext().getRequestMap().get("dadosComerciaisItem");
		getFacadeFactory().getDadosComerciaisFacade().excluirObjDadosComerciaisVOs(obj, getPessoaVO());
		setMensagemID("msg_dados_excluidos");
	}

	/*
	 * Método responsável por adicionar um novo objeto da classe
	 * <code>FormacaoAcademica</code> para o objeto <code>pessoaVO</code> da
	 * classe <code>Pessoa</code>
	 */
	public void adicionarAreaProfissional() {
		try {
			AreaProfissionalVO areaProfissional = getFacadeFactory().getAreaProfissionalFacade().consultarPorChavePrimaria(getAreaProfissionalVO().getCodigo(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			getFacadeFactory().getAreaProfissionalInteresseContratacaoFacade().adicionarObjAreaProfissionalVOs(areaProfissional, getPessoaVO());
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Método responsável por remover um novo objeto da classe
	 * <code>FormacaoAcademica</code> do objeto <code>pessoaVO</code> da classe
	 * <code>Pessoa</code>
	 **/
	public void removerAreaProfissional() throws Exception {
		AreaProfissionalInteresseContratacaoVO obj = (AreaProfissionalInteresseContratacaoVO) context().getExternalContext().getRequestMap().get("apicItem");
		getFacadeFactory().getAreaProfissionalInteresseContratacaoFacade().excluirObjAreaProfissionalVOs(obj, getPessoaVO());
		setMensagemID("msg_dados_excluidos");
	}

	public void adicionarFormacaoExtraCurricular() {
		try {
			if (!getPessoaVO().getCodigo().equals(0)) {
				getFormacaoExtraCurricularVO().setPessoa(getPessoaVO());
			}
			getFacadeFactory().getFormacaoExtraCurricularFacade().adicionarObjFormacaoExtraCurricularVOs(getFormacaoExtraCurricularVO(), getPessoaVO());
			this.setFormacaoExtraCurricularVO(new FormacaoExtraCurricularVO());
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/*
	 * Método responsável por disponibilizar dados de um objeto da classe
	 * <code>FormacaoAcademica</code> para edição pelo usuário.
	 */
	public void editarFormacaoExtraCurricular() throws Exception {
		FormacaoExtraCurricularVO obj = (FormacaoExtraCurricularVO) context().getExternalContext().getRequestMap().get("formacaoExtraCurricularItem");
		setFormacaoExtraCurricularVO(obj);
	}

	/**
	 * Método responsável por remover um novo objeto da classe
	 * <code>FormacaoAcademica</code> do objeto <code>pessoaVO</code> da classe
	 * <code>Pessoa</code>
	 **/
	public void removerFormacaoExtraCurricular() throws Exception {
		FormacaoExtraCurricularVO obj = (FormacaoExtraCurricularVO) context().getExternalContext().getRequestMap().get("formacaoExtraCurricularItem");
		getFacadeFactory().getFormacaoExtraCurricularFacade().excluirObjFormacaoExtraCurricularVOs(obj, getPessoaVO());
		setMensagemID("msg_dados_excluidos");
	}

	public String iniciarMatriculaCRM() throws Exception {
		try {
			if (!pessoaVO.isNovoObj().booleanValue()) {
				registrarAtividadeUsuario(getUsuarioLogado(), "AlunoControle", "Inicializando Alterar - Matrícula CRM", "Alterando");
				getFacadeFactory().getPessoaFacade().alterar(pessoaVO, false, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema(), false);
				registrarAtividadeUsuario(getUsuarioLogado(), "AlunoControle", "Finalizando Alterar Aluno  - Matrícula CRM", "Alterando");
			}
			MatriculaVO matricula = new MatriculaVO();
			if (getMatriculaVO().getAluno().getCodigo() != 0) {
				matricula = getMatriculaVO();
			}
//			RenovarMatriculaControle renovarControle = new RenovarMatriculaControle();
//			context().getExternalContext().getSessionMap().put(RenovarMatriculaControle.class.getSimpleName(), renovarControle);
//			renovarControle.novaMatriculaAluno();
//			try {
//				getFacadeFactory().getMatriculaFacade().verificaAlunoJaMatriculado(getMatriculaVO(), false, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
//				renovarControle.setRealizandoNovaMatriculaAluno(Boolean.TRUE);
//			} catch (Exception e) {
//				renovarControle.setRealizandoNovaMatriculaAluno(Boolean.FALSE);
//			}
			matricula.setGuiaAba("DadosBasicos");
			matricula.setAluno(getPessoaVO());
			if ((getInteracaoWorkflowVO().getCodigo() != 0) || (getMatriculaDiretaAgendaCRM())) {
				matricula.setUnidadeEnsino(getInteracaoWorkflowVO().getProspect().getUnidadeEnsino());
				matricula.setCurso(getInteracaoWorkflowVO().getCurso());
				matricula.setTurno(getInteracaoWorkflowVO().getTurno());
				matricula.setConsultor(getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCodigoPessoa(getInteracaoWorkflowVO().getResponsavel().getPessoa().getCodigo(), false, getUsuarioLogado()));
			} else {
				matricula.setUnidadeEnsino(getCompromissoAgendaPessoaHorarioVO().getCampanha().getUnidadeEnsino());
				matricula.setCurso(getCompromissoAgendaPessoaHorarioVO().getCampanha().getCurso());
				matricula.setConsultor(getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCodigoPessoa(getCompromissoAgendaPessoaHorarioVO().getAgendaPessoaHorario().getAgendaPessoa().getPessoa().getCodigo(), false, getUsuarioLogado()));
			}
//			renovarControle.setMatriculaVO(matricula);
			matricula.setUsuario(getUsuarioLogadoClone());
			if (getMatriculaVO().getCurso().getCodigo() != 0 && getMatriculaVO().getUnidadeEnsino().getCodigo() != 0 && getMatriculaVO().getTurno().getCodigo() != 0) {
				if (getMatriculaVO().getTurno().getNome().equals("")) {
					getMatriculaVO().setTurno(getFacadeFactory().getTurnoFacade().consultarPorChavePrimaria(getMatriculaVO().getTurno().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
				}
//				renovarControle.selecionarCurso(getFacadeFactory().getUnidadeEnsinoCursoFacade().consultaRapidaPorCursoUnidadeTurno(getMatriculaVO().getCurso().getCodigo(), getMatriculaVO().getUnidadeEnsino().getCodigo(), getMatriculaVO().getTurno().getCodigo(), getUsuarioLogado()));
			}
			matriculaDiretaAgendaCRM = Boolean.FALSE;
			habilitarIniciarInscricao = Boolean.FALSE;
			removerControleMemoriaFlashTela("RenovarMatriculaControle");
			context().getExternalContext().getSessionMap().put("matriculaCRMTelaAluno", matricula);
			return Uteis.getCaminhoRedirecionamentoNavegacao("renovarMatriculaForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("alunoForm.xhtml");
		}
	}

	public void iniciarMatricula() throws Exception {
		try {	
			removerControleMemoriaFlashTela("RenovarMatriculaControle");
			context().getExternalContext().getSessionMap().put("pessoaItem", getPessoaVO());
//			return Uteis.getCaminhoRedirecionamentoNavegacao("renovarMatriculaForm.xhtml");
			
		} catch (Exception e) {
			
		}
	}

	public void validarPessoaCadastrada() {
		Boolean cpfInvalido = false;
		Boolean certidaoInvalido = false;
		try {
			if (getTipoDocumento().equals("CPF")) {
				validarPessoaCadastradaPorCPF(cpfInvalido);
			} else {
				validarPessoaCadastradaPorCertidaoNascimento(certidaoInvalido);
			}
		} catch (Exception e) {
			pessoaVO.setCPF("");
			pessoaVO.setRG("");
			if (getTipoDocumento().equals("CPF")) {
				if (e.getMessage().contains("CPF inv")) {
					cpfInvalido = true;
				}
				if (!cpfInvalido) {
					setImportarDadosPessoa(Boolean.TRUE);
					setConsultarPessoa(Boolean.FALSE);
				}
			} else {
				if (!certidaoInvalido) {
					setImportarDadosPessoa(Boolean.TRUE);
					setConsultarPessoa(Boolean.FALSE);
				}
			}
			setMensagemDetalhada(e.getMessage());
		}
	}

	public void validarPessoaCadastradaPorCPF(Boolean cpfInvalido) throws Exception {
		ProspectsVO obj = null;
		String mensagem = "";
		String cpf = "";
		if (getPessoaVO().getCPF().length() == 14) {
			cpf = getPessoaVO().getCPF();
			setPessoaExistente(getFacadeFactory().getPessoaFacade().consultarPorCPFUnico(cpf, 0, "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			if (pessoaExistente.getCodigo().intValue() == 0) {
				obj = new ProspectsVO();
				obj.setCpf(cpf);
				obj = getFacadeFactory().getProspectsFacade().consultarPorCPFCNPJUnico(obj, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				setProspectsVO(obj);
			}
		} else {
			ajaxRedirecionarFocoCampo = "";
			throw new Exception("CPF inválido.");
		}
		if (obj != null) {
			if (obj.getCodigo().intValue() != 0) {
				ajaxRedirecionarFocoCampo = "";
				mensagem = "Já existe um ";
				mensagem += "Prospect cadastrado";
				mensagem += " com este CPF. \nDeseja cadastrá-lo como aluno?";
				// getFacadeFactory().getProspectsFacade().realizarPreenchimentoPessoaPorProspect(obj,
				// getUsuarioLogado());
				throw new Exception(mensagem);
			}
		}
		if (pessoaExistente.getCodigo().intValue() != 0) {
			ajaxRedirecionarFocoCampo = "";
			mensagem = "Já existe um ";

			if (pessoaExistente.getProfessor().equals(Boolean.TRUE)) {
				mensagem += "Professor cadastrado";
				setPessoaVO(pessoaExistente);
				setEditarDados(true);
			} else if (pessoaExistente.getFuncionario().equals(Boolean.TRUE)) {
				mensagem += "Funcionário cadastrado";
				setPessoaVO(pessoaExistente);
				setEditarDados(true);
			} else if (pessoaExistente.getAluno().equals(Boolean.TRUE)) {
				mensagem += "Aluno cadastrado";
			} else if (pessoaExistente.getCandidato().equals(Boolean.TRUE)) {
				mensagem += "Candidato cadastrado";
			} else if (pessoaExistente.getMembroComunidade().equals(Boolean.TRUE)) {
				mensagem += "Membro da Comunidade cadastrado";
			}
			mensagem += " com este CPF. \n " + (pessoaExistente.getAluno() ? "Deseja editar o cadastro do aluno?" : "Deseja cadastrá-lo como aluno?");
			throw new Exception(mensagem);
		} else {
			ajaxRedirecionarFocoCampo = "document.getElementById('form:nome').focus();";
		}
//		setPessoaVO(new PessoaVO());
		getPessoaVO().setCPF(cpf);
		setConsultarPessoa(Boolean.FALSE);
		setImportarDadosPessoa(Boolean.FALSE);
	}

	public void validarPessoaCadastradaPorCertidaoNascimento(Boolean certidaoInvalido) throws Exception {
		String mensagem = "";
		String certidaoNascimento = "";
		if (!getPessoaVO().getCertidaoNascimento().equals("")) {
			certidaoNascimento = getPessoaVO().getCertidaoNascimento();
			setPessoaExistente(getFacadeFactory().getPessoaFacade().consultarPorCertidaoNascimentoUnico(certidaoNascimento, "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
		} else {
			ajaxRedirecionarFocoCampo = "";
			throw new Exception("Certidão inválida.");
		}
		if (pessoaExistente.getCodigo().intValue() != 0) {
			ajaxRedirecionarFocoCampo = "";
			mensagem = "Já existe um ";

			if (pessoaExistente.getProfessor().equals(Boolean.TRUE)) {
				mensagem += "Professor cadastrado";
				setPessoaVO(pessoaExistente);
				setEditarDados(true);
			} else if (pessoaExistente.getFuncionario().equals(Boolean.TRUE)) {
				mensagem += "Funcionário cadastrado";
				setPessoaVO(pessoaExistente);
				setEditarDados(true);
			} else if (pessoaExistente.getAluno().equals(Boolean.TRUE)) {
				mensagem += "Aluno cadastrado";
			} else if (pessoaExistente.getCandidato().equals(Boolean.TRUE)) {
				mensagem += "Candidato cadastrado";
			} else if (pessoaExistente.getMembroComunidade().equals(Boolean.TRUE)) {
				mensagem += "Membro da Comunidade cadastrado";
			}
			mensagem += " com esta Certidão. \nDeseja cadastrá-lo como aluno?";
			throw new Exception(mensagem);
		} else {
			ajaxRedirecionarFocoCampo = "document.getElementById('form:nome').focus();";
		}
		setPessoaVO(new PessoaVO());
		getPessoaVO().setCertidaoNascimento(certidaoNascimento);
		getPessoaVO().setGerarNumeroCPF(!getVerificarCpf());
		setConsultarPessoa(Boolean.FALSE);
		setImportarDadosPessoa(Boolean.FALSE);
	}

	/**
	 * Método responsável por remover um novo objeto da classe
	 * <code>FormacaoAcademica</code> do objeto <code>pessoaVO</code> da classe
	 * <code>Pessoa</code>
	 **/
	public void removerFormacaoAcademica() throws Exception {
		try {
			Boolean aptoRemover = Boolean.TRUE;
			FormacaoAcademicaVO obj = (FormacaoAcademicaVO) context().getExternalContext().getRequestMap().get("formacaoAcademicaItem");
			if (Uteis.isAtributoPreenchido(obj.getCodigo())) {
				aptoRemover = getFacadeFactory().getFormacaoAcademicaFacade().validarRemoverFormacaoAcademica(getPessoaVO().getCodigo(), obj);
			}
			if (aptoRemover != null && !aptoRemover) {
				throw new Exception("Não é possível remover está Formação Acadêmica.O Registro está vinculado a uma MATRÍCULA do Aluno.");
			}
			getPessoaVO().excluirObjFormacaoAcademicaVOs(obj.getCurso());
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void irPaginaInicial() throws Exception {
		this.consultar();
	}

	public void irPaginaAnterior() throws Exception {
		controleConsulta.setPaginaAtual(controleConsulta.getPaginaAtual() - 1);
		this.consultar();
	}

	public void irPaginaPosterior() throws Exception {
		controleConsulta.setPaginaAtual(controleConsulta.getPaginaAtual() + 1);
		this.consultar();
	}

	public void irPaginaFinal() throws Exception {
		controleConsulta.setPaginaAtual(controleConsulta.getNrTotalPaginas());
		this.consultar();
	}

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do
	 * ComboBox correspondente ao atributo <code>diaSemana</code>
	 */
	public List<SelectItem> getListaSelectItemDiaSemanaDisponibilidadeHorario() throws Exception {
		List<SelectItem> objs = new ArrayList<SelectItem>(0);
		objs.add(new SelectItem("", ""));
		Hashtable diaSemanaDisponibilidadeHorarios = (Hashtable) Dominios.getDiaSemanaDisponibilidadeHorario();
		Enumeration keys = diaSemanaDisponibilidadeHorarios.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) diaSemanaDisponibilidadeHorarios.get(value);
			objs.add(new SelectItem(value, label));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		return objs;
	}

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do
	 * ComboBox correspondente ao atributo <code>tipo</code>
	 */
	public List getListaSelectItemTipoFiliacao() throws Exception {
		List<SelectItem> objs = new ArrayList<SelectItem>(0);
		objs.add(new SelectItem("", ""));
		Hashtable tipoFiliacaos = (Hashtable) Dominios.getTipoFiliacao();
		Enumeration keys = tipoFiliacaos.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) tipoFiliacaos.get(value);
			objs.add(new SelectItem(value, label));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		return objs;
	}

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do
	 * ComboBox correspondente ao atributo <code>situacao</code>
	 */
	public List getListaSelectItemSituacaoFormacaoAcademica() throws Exception {
		List<SelectItem> objs = new ArrayList<SelectItem>(0);
		objs.add(new SelectItem("", ""));
		Hashtable situacaoFormacaoAcademicas = (Hashtable) Dominios.getSituacaoFormacaoAcademica();
		Enumeration keys = situacaoFormacaoAcademicas.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) situacaoFormacaoAcademicas.get(value);
			objs.add(new SelectItem(value, label));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		return objs;
	}

	public List getListaSelectItemTipoInstFormacaoAcademica() throws Exception {
		List<SelectItem> objs = new ArrayList<SelectItem>(0);
		objs.add(new SelectItem("", ""));
		Hashtable tipoInstFormacaoAcademicas = (Hashtable) Dominios.getTipoInstFormacaoAcademica();
		Enumeration keys = tipoInstFormacaoAcademicas.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) tipoInstFormacaoAcademicas.get(value);
			objs.add(new SelectItem(value, label));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		return objs;
	}

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do
	 * ComboBox correspondente ao atributo <code>escolaridade</code>
	 */
	public List getListaSelectItemEscolaridadeFormacaoAcademica() throws Exception {
		List objs = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(NivelFormacaoAcademica.class, false);
		return objs;
	}

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do
	 * ComboBox correspondente ao atributo <code>tipoPessoa</code>
	 */
	public List getListaSelectItemTipoPessoaPessoa() throws Exception {
		List<SelectItem> objs = new ArrayList<SelectItem>(0);
		objs.add(new SelectItem("", ""));
		Hashtable tipoPessoaBasicoPessoas = (Hashtable) Dominios.getTipoPessoaBasicoPessoa();
		Enumeration keys = tipoPessoaBasicoPessoas.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) tipoPessoaBasicoPessoas.get(value);
			objs.add(new SelectItem(value, label));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		return objs;
	}

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do
	 * ComboBox correspondente ao atributo <code>estadoEmissaoRG</code>
	 */
	public List getListaSelectItemEstadoEmissaoRGPessoa() throws Exception {
		List<SelectItem> objs = new ArrayList<SelectItem>(0);
		objs.add(new SelectItem("", ""));
		Hashtable estados = (Hashtable) Dominios.getEstado();
		Enumeration keys = estados.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) estados.get(value);
			objs.add(new SelectItem(value, label));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		return objs;
	}
	
	public List<SelectItem> listaSelectItemModalidadeBolsa;
	public List<SelectItem> getListaSelectItemModalidadeBolsa() throws Exception{
		if(listaSelectItemModalidadeBolsa == null) {
			listaSelectItemModalidadeBolsa = new ArrayList<SelectItem>(0);	
			for(ModalidadeBolsaEnum m: ModalidadeBolsaEnum.values()) {				
				listaSelectItemModalidadeBolsa.add(new SelectItem(m.name(), m.getValorApresentar()));
			}							
		}
		return listaSelectItemModalidadeBolsa;
	} 

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do
	 * ComboBox correspondente ao atributo <code>estadoCivil</code>
	 */
	public List<SelectItem> getListaSelectItemEstadoCivilPessoa() throws Exception {
		List<SelectItem> objs = new ArrayList<SelectItem>(0);
		objs.add(new SelectItem("", ""));
		Hashtable estadoCivils = (Hashtable) Dominios.getEstadoCivil();
		Enumeration keys = estadoCivils.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) estadoCivils.get(value);
			objs.add(new SelectItem(value, label));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		return objs;
	}

	public List<SelectItem> getListaSelectItemCorRaca() throws Exception {
		return UtilPropriedadesDoEnum.getListaSelectItemDoEnum(CorRaca.class);
	}

	public List<SelectItem> getListaSelectItemDeficiencia() throws Exception {
		return UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoDeficiencia.class);
	}

	public List getListaSelectItemSexoPessoa() throws Exception {
		List<SelectItem> objs = new ArrayList<SelectItem>(0);
		objs.add(new SelectItem("", ""));
		Hashtable sexos = (Hashtable) Dominios.getSexo();
		Enumeration keys = sexos.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) sexos.get(value);
			objs.add(new SelectItem(value, label));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		return objs;
	}

	/**
	 * Método responsável por consultar Cidade <code>Cidade/code>.
	 * Buscando todos os objetos correspondentes a entidade <code>Cidade</code>.
	 * Esta rotina não recebe parâmetros para filtragem de dados, isto é
	 * importante para a inicialização dos dados da tela para o acionamento por
	 * meio requisições Ajax.
	 */
	public void consultarCidade() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaCidade().equals("codigo")) {
				if (getValorConsultaCidade().equals("")) {
					setValorConsultaCidade("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaCidade());
				objs = getFacadeFactory().getCidadeFacade().consultarPorCodigo(new Integer(valorInt), false, getUsuarioLogado());
			}
			if (getCampoConsultaCidade().equals("nome")) {
				if (getValorConsultaCidade().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getCidadeFacade().consultarPorNome(getValorConsultaCidade(), false, getUsuarioLogado());
			}
			if (getCampoConsultaCidade().equals("estado")) {
				objs = getFacadeFactory().getCidadeFacade().consultarPorSiglaEstado(getValorConsultaCidade(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}

			setListaConsultaCidade(objs);
			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			setListaConsultaCidade(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}

	public void consultarFiliacaoCidade() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaFiliacaoCidade().equals("codigo")) {
				if (getValorConsultaFiliacaoCidade().equals("")) {
					setValorConsultaFiliacaoCidade("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaFiliacaoCidade());
				objs = getFacadeFactory().getCidadeFacade().consultarPorCodigo(new Integer(valorInt), false, getUsuarioLogado());
			}
			if (getCampoConsultaFiliacaoCidade().equals("nome")) {
				if (getValorConsultaFiliacaoCidade().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getCidadeFacade().consultarPorNome(getValorConsultaFiliacaoCidade(), false, getUsuarioLogado());
			}
			if (getCampoConsultaFiliacaoCidade().equals("estado")) {
				objs = getFacadeFactory().getCidadeFacade().consultarPorSiglaEstado(getValorConsultaFiliacaoCidade(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}

			setListaConsultaFiliacaoCidade(objs);
			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			setListaConsultaCidade(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}

	public void consultarCidadeEmpresa() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaCidadeEmpresa().equals("codigo")) {
				if (getValorConsultaCidadeEmpresa().equals("")) {
					setValorConsultaCidadeEmpresa("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaCidadeEmpresa());
				objs = getFacadeFactory().getCidadeFacade().consultarPorCodigo(new Integer(valorInt), false, getUsuarioLogado());
			}
			if (getCampoConsultaCidadeEmpresa().equals("nome")) {
				objs = getFacadeFactory().getCidadeFacade().consultarPorNome(getValorConsultaCidadeEmpresa(), false, getUsuarioLogado());
			}
			if (getCampoConsultaCidadeEmpresa().equals("estado")) {
				objs = getFacadeFactory().getCidadeFacade().consultarPorSiglaEstado(getValorConsultaCidadeEmpresa(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaCidadeEmpresa(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCidade(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}

	/**
	 * Método responsável por selecionar o objeto CidadeVO <code>Cidade/code>.
	 */
	public void selecionarCidade() {
		CidadeVO obj = (CidadeVO) context().getExternalContext().getRequestMap().get("cidadeItem");
		getPessoaVO().setCidade(obj);
		listaConsultaCidade.clear();
		this.setValorConsultaCidade("");
		this.setCampoConsultaCidade("");
	}

	public void selecionarFiliacaoCidade() {
		CidadeVO obj = (CidadeVO) context().getExternalContext().getRequestMap().get("filiacaoCidadeItem");
		getFiliacaoVO().getPais().setCidade(obj);
		listaConsultaFiliacaoCidade.clear();
		this.setValorConsultaFiliacaoCidade("");
		this.setCampoConsultaFiliacaoCidade("");
	}

	public void selecionarCidadeEmpresa() {
		CidadeVO obj = (CidadeVO) context().getExternalContext().getRequestMap().get("cidadeEmpresaItem");
		getDadosComerciaisVO().setCidadeEmpresa(obj);
		getListaConsultaCidadeEmpresa().clear();
		this.setValorConsultaCidadeEmpresa("");
		this.setCampoConsultaCidadeEmpresa("");
	}

	public void selecionarCidadeFormacao() {
		CidadeVO obj = (CidadeVO) context().getExternalContext().getRequestMap().get("cidadeItem");
		getFormacaoAcademicaVO().setCidade(obj);
		listaConsultaCidade.clear();
		this.setValorConsultaCidade("");
		this.setCampoConsultaCidade("");
	}

	/**
	 * Método responsável por carregar umaCombobox com os tipos de pesquisa de
	 * Cidade <code>Cidade/code>.
	 */
	public List<SelectItem> getTipoConsultaCidade() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		itens.add(new SelectItem("estado", "Estado"));
		return itens;
	}

	/**
	 * Método responsável por carregar umaCombobox com os tipos de pesquisa de
	 * Cidade <code>Cidade/code>.
	 */
	public List<SelectItem> getTipoConsultaCidadeEmpresa() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		itens.add(new SelectItem("estado", "Estado"));
		return itens;
	}

	public List<SelectItem> getTipoConsultaComboSalario() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("Até R$999", "Até R$999,00"));
		itens.add(new SelectItem("R$1000 à R$1999", "De R$ 1000,00 até R$ 1999,00"));
		itens.add(new SelectItem("R$2000 à R$2999", "De R$ 2000,00 até R$ 2999,00"));
		itens.add(new SelectItem("R$3000 à R$3999", "De R$ 3000,00 até R$ 3999,00"));
		itens.add(new SelectItem("R$4000 à R$4999", "De R$ 4000,00 até R$ 4999,00"));
		itens.add(new SelectItem("R$5000 à R$5999", "De R$ 5000,00 até R$ 5999,00"));
		itens.add(new SelectItem("acima de R$6000", "Acima de R$ 6000,00"));
		return itens;
	}

	public List<SelectItem> getTipoComboNivelIngles() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("", ""));
		itens.add(new SelectItem("inicial", "Inicial"));
		itens.add(new SelectItem("intermediario", "Intermediário"));
		itens.add(new SelectItem("avancado", "Avançado"));
		return itens;
	}

	public List<SelectItem> getTipoComboTipoDocumento() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("CPF", "CPF"));
		itens.add(new SelectItem("CN", "Certidão Nascimento"));
		return itens;
	}

	public List <SelectItem>getTipoComboNivelFrances() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("", ""));
		itens.add(new SelectItem("inicial", "Inicial"));
		itens.add(new SelectItem("intermediario", "Intermediário"));
		itens.add(new SelectItem("avancado", "Avançado"));
		return itens;
	}

	public List<SelectItem> getTipoComboNivelEspanhol() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("", ""));
		itens.add(new SelectItem("inicial", "Inicial"));
		itens.add(new SelectItem("intermediario", "Intermediário"));
		itens.add(new SelectItem("avancado", "Avançado"));
		return itens;
	}

	public List<SelectItem> getTipoComboNivelOutrosIdiomas() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("", ""));
		itens.add(new SelectItem("inicial", "Inicial"));
		itens.add(new SelectItem("intermediario", "Intermediário"));
		itens.add(new SelectItem("avancado", "Avançado"));
		return itens;
	}

	/**
	 * Método responsável por consultar Naturalidade <code>Cidade/code>.
	 * Buscando todos os objetos correspondentes a entidade <code>Cidade</code>.
	 * Esta rotina não recebe parâmetros para filtragem de dados, isto é
	 * importante para a inicialização dos dados da tela para o acionamento por
	 * meio requisições Ajax.
	 */
	public void consultarNaturalidade() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaNaturalidade().equals("codigo")) {
				if (getValorConsultaNaturalidade().equals("")) {
					setValorConsultaNaturalidade("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaNaturalidade());
				objs = getFacadeFactory().getCidadeFacade().consultarPorCodigo(new Integer(valorInt), false, getUsuarioLogado());
			}
			if (getCampoConsultaNaturalidade().equals("nome")) {
				objs = getFacadeFactory().getCidadeFacade().consultarPorNome(getValorConsultaNaturalidade(), false, getUsuarioLogado());
			}

			setListaConsultaNaturalidade(objs);
			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			setListaConsultaNaturalidade(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}
	
	//Geber
	public void consultarFiador() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaFiador().equals("codigo")) {
				if (getValorConsultaFiador().equals("")) {
					setValorConsultaFiador("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaFiador());
				objs = getFacadeFactory().getCidadeFacade().consultarPorCodigo(new Integer(valorInt), false, getUsuarioLogado());
			}
			if (getCampoConsultaFiador().equals("nome")) {
				objs = getFacadeFactory().getCidadeFacade().consultarPorNome(getValorConsultaFiador(), false, getUsuarioLogado());
			}

			setListaConsultaFiador(objs);
			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			setListaConsultaFiador(new ArrayList<CidadeVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}
	

	/**
	 * Método responsável por selecionar o objeto CidadeVO em Naturalidade
	 * <code>Cidade/code>.
	 */
	public void selecionarNaturalidade() {
		CidadeVO obj = (CidadeVO) context().getExternalContext().getRequestMap().get("naturalidadeItem");
		getPessoaVO().setNaturalidade(obj);
		getListaConsultaNaturalidade().clear();
		this.setValorConsultaNaturalidade("");
		this.setCampoConsultaNaturalidade("");
	}
	
	
	public void selecionarFiador() {
		CidadeVO obj = (CidadeVO) context().getExternalContext().getRequestMap().get("fiador");
		getPessoaVO().setCidadeFiador(obj);
		setListaConsultaFiador(new ArrayList<CidadeVO>(0));
		this.setValorConsultaFiador("");
		this.setCampoConsultaFiador("");
	}	
	
	public void limparFiador(){
		getListaConsultaFiador().clear();
	}

	/**
	 * Método responsável por carregar umaCombobox com os tipos de pesquisa de
	 * Cidade para Naturalidade <code>Cidade/code>.
	 */
	public List<SelectItem> getTipoConsultaNaturalidade() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}
	
	public List<SelectItem> getTipoConsultaFiador() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>Nacionalidade</code>.
	 */
	public void montarListaSelectItemNacionalidade(String prm) throws Exception {
		SelectItemOrdemValor ordenador = null;
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarPaizPorNome(prm);
			i = resultadoConsulta.iterator();
			List<SelectItem> objs = new ArrayList<SelectItem>(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				PaizVO obj = (PaizVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNacionalidade()));
			}
			ordenador = new SelectItemOrdemValor();
			Collections.sort((List) objs, ordenador);
			setListaSelectItemNacionalidade(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			ordenador = null;
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}

	}
	

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>Nacionalidade</code>. Buscando todos os objetos correspondentes a
	 * entidade <code>Paiz</code>. Esta rotina não recebe parâmetros para
	 * filtragem de dados, isto é importante para a inicialização dos dados da
	 * tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemNacionalidade() {
		try {
			montarListaSelectItemNacionalidade("");
		} catch (Exception e) {
			//System.out.println("MENSAGEM => " + e.getMessage());
			;
		}
	}

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>nome</code> Este atributo é uma
	 * lista (<code>List</code>) utilizada para definir os valores a serem
	 * apresentados no ComboBox correspondente
	 */
	public List consultarPaizPorNome(String nomePrm) throws Exception {
		List lista = getFacadeFactory().getPaizFacade().consultarPorNome(nomePrm, false, getUsuarioLogado());
		return lista;
	}

	public void montarListaSelectItemAreaConhecimento(String prm) throws Exception {
		SelectItemOrdemValor ordenador = null;
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarAreaConhecimentoPorNome(prm);
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				AreaConhecimentoVO obj = (AreaConhecimentoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
			}
			ordenador = new SelectItemOrdemValor();
			Collections.sort((List) objs, ordenador);
			setListaSelectItemAreaConhecimento(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			ordenador = null;
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>Nacionalidade</code>. Buscando todos os objetos correspondentes a
	 * entidade <code>Paiz</code>. Esta rotina não recebe parâmetros para
	 * filtragem de dados, isto é importante para a inicialização dos dados da
	 * tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemAreaConhecimento() {
		try {
			montarListaSelectItemAreaConhecimento("");
		} catch (Exception e) {
			//System.out.println("MENSAGEM => " + e.getMessage());
			;
		}
	}

	public void permitirIniciarInscricao() {
		setHabilitarIniciarInscricao(false);
	}

	public boolean isInscricaoPermitida() {
		try {
    		return ControleAcesso.verificarPermissaoFuncionalidadeUsuario("AlunoIniciarMatricula", getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return false;
		}
	}

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>nome</code> Este atributo é uma
	 * lista (<code>List</code>) utilizada para definir os valores a serem
	 * apresentados no ComboBox correspondente
	 */
	public List consultarAreaConhecimentoPorNome(String nomePrm) throws Exception {
		List lista = getFacadeFactory().getAreaConhecimentoFacade().consultarPorNome(nomePrm, false, getUsuarioLogado());
		return lista;
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>Cidade</code>.
	 */
	public void montarListaSelectItemPerfilEconomico(String prm) throws Exception {
		SelectItemOrdemValor ordenador = null;
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarPerfilEconomicoPorNome(prm);
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				PerfilEconomicoVO obj = (PerfilEconomicoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
			}
			ordenador = new SelectItemOrdemValor();
			Collections.sort((List) objs, ordenador);
			setListaSelectItemPerfilEconomico(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			ordenador = null;
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>Cidade</code>.
	 */
	public void montarListaSelectItemAreaProfissional(String prm) throws Exception {
		SelectItemOrdemValor ordenador = null;
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarAreaProfissionalPorNome(prm);
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				AreaProfissionalVO obj = (AreaProfissionalVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getDescricaoAreaProfissional()));
			}
			ordenador = new SelectItemOrdemValor();
			Collections.sort((List) objs, ordenador);
			setListaSelectItemAreaProfissional(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			ordenador = null;
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>Cidade</code>. Buscando todos os objetos correspondentes a entidade
	 * <code>Cidade</code>. Esta rotina não recebe parâmetros para filtragem de
	 * dados, isto é importante para a inicialização dos dados da tela para o
	 * acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemPerfilEconomico() {
		try {
			montarListaSelectItemPerfilEconomico("");
		} catch (Exception e) {
			//System.out.println("MENSAGEM => " + e.getMessage());
			;
		}
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>Cidade</code>. Buscando todos os objetos correspondentes a entidade
	 * <code>Cidade</code>. Esta rotina não recebe parâmetros para filtragem de
	 * dados, isto é importante para a inicialização dos dados da tela para o
	 * acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemAreaProfissional() {
		try {
			montarListaSelectItemAreaProfissional("");
		} catch (Exception e) {
			//System.out.println("MENSAGEM => " + e.getMessage());
			;
		}
	}

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>nome</code> Este atributo é uma
	 * lista (<code>List</code>) utilizada para definir os valores a serem
	 * apresentados no ComboBox correspondente
	 */
	public List consultarPerfilEconomicoPorNome(String nomePrm) throws Exception {
		List lista = getFacadeFactory().getPerfilEconomicoFacade().consultarPorNome(nomePrm, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		return lista;
	}

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>nome</code> Este atributo é uma
	 * lista (<code>List</code>) utilizada para definir os valores a serem
	 * apresentados no ComboBox correspondente
	 */
	public List consultarAreaProfissionalPorNome(String nomePrm) throws Exception {
		List lista = getFacadeFactory().getAreaProfissionalFacade().consultarPorDescricaoAreaProfissional(nomePrm, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		return lista;
	}

	/**
	 * Método responsável por inicializar a lista de valores (
	 * <code>SelectItem</code>) para todos os ComboBox's.
	 */
	public void inicializarListasSelectItemTodosComboBox() {
		montarListaSelectItemNacionalidade();
		montarListaSelectItemAreaConhecimento();
		montarListaSelectItemPerfilEconomico();
		montarListaSelectItemAreaProfissional();
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List<SelectItem> tipoConsultaCombo;
	public List<SelectItem> getTipoConsultaCombo() {
		if(tipoConsultaCombo == null) {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
//		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("nomeMae", "Nome da Mãe"));
		itens.add(new SelectItem("nomePai", "Nome do Pai"));
		itens.add(new SelectItem("responsavelLegal", "Responsável Legal"));
		itens.add(new SelectItem("CPF", "CPF"));
		itens.add(new SelectItem("RG", "RG"));		
		itens.add(new SelectItem("registroAcademico", "Registro Acadêmico"));
		itens.add(new SelectItem("emailInstitucional", "E-mail Institucional"));
		tipoConsultaCombo =  itens;
		}
		
		return tipoConsultaCombo;
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes
	 * de uma consulta.
	 */
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setListaConsulta(new ArrayList(0));
		getControleConsultaOtimizado().getListaConsulta().clear();
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("alunoCons");
	}

	public void inicializarDadosFichaAluno() {
		getPessoaVO().setFiliacaoVOs(null);
		setMatriculaVO(null);
		setMatriculaPeriodoVO(null);
		setApresentarAbaDadosAcademicosFichaAluno(null);
		setApresentarAbaDadosTurmaDisciplinaFichaAluno(null);
		setApresentarAbaDadosFinanceiroFichaAluno(null);
		setAbaSelecionada(null);
	}

	/**
	 * Método responsavel por consultar o Aluno pelo codigo no momento em que o
	 * usuário clicar no botão(fichaAluno) preenchendo a lista de Matricula.
	 * 
	 * @Autor Carlos
	 */
	public void executarVisualizacaoFichaAluno() throws Exception {
		PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("pessoaItem");
		obj.getArquivoImagem().setCpfRequerimento(obj.getCPF());
		setCaminhoFotoUsuario(getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(obj.getArquivoImagem(), PastaBaseArquivoEnum.IMAGEM.getValue(), getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(), "foto_usuario.png", false));
		context().getExternalContext().getSessionMap().put("alunoFichaVO", obj);
	}

	public String getCaminhoFotoUsuarioFichaAluno() {
		String caminhoFoto = "";
		if (this.getPessoaVO().getCodigo().intValue() > 0) {
			this.getPessoaVO().getArquivoImagem().setCpfRequerimento(this.getPessoaVO().getCPF());
			caminhoFoto = getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(this.getPessoaVO().getArquivoImagem(), PastaBaseArquivoEnum.IMAGEM.getValue(), getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(), "foto_usuario.png", false);
		}
		return caminhoFoto;
	}
	
	public void executarVisualizacaoFichaAlunoMontarFoto(PessoaVO obj) throws Exception {
		obj.getArquivoImagem().setCpfRequerimento(obj.getCPF());
		setCaminhoFotoUsuario(getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(obj.getArquivoImagem(), PastaBaseArquivoEnum.IMAGEM.getValue(), getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(), "foto_usuario.png", false));
		context().getExternalContext().getSessionMap().put("alunoFichaVO", obj);
	}

	public void executarVisualizacaoFichaAluno(PessoaVO obj) {
		try {
			getPessoaVO().getFiliacaoVOs().clear();
			obj.getFiliacaoVOs().clear();
			inicializarDadosFichaAluno();
			// PessoaVO obj = (PessoaVO)
			// context().getExternalContext().getRequestMap().get("pessoa");
			getFacadeFactory().getPessoaFacade().carregarDados(obj, getUsuarioLogado());
			setPessoaVO(obj);
			List<MatriculaVO> lista = getFacadeFactory().getMatriculaFacade().consultaRapidaCompletaPorCodigoPessoa(obj.getCodigo().intValue(), getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			Iterator i = lista.iterator();
			while (i.hasNext()) {
				MatriculaVO mat = (MatriculaVO) i.next();
				MatriculaVO matricula = getFacadeFactory().getMatriculaFacade().consultaRapidaBasicaPorMatriculaNaoCancelada(mat.getMatricula(), false, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
				MatriculaPeriodoVO matPer = getFacadeFactory().getMatriculaPeriodoFacade().consultarUltimaMatriculaPeriodoAtivaPorMatriculaSemExcecao(mat.getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, new ConfiguracaoFinanceiroVO(), getUsuarioLogado());
				matricula.getCurso().setNome(matricula.getCurso().getNome() + " - " + matPer.getTurma().getIdentificadorTurma());
				getListaMatriculaAluno().add(matricula);				
			}
			validarPendenciaFinanceiraFichaAluno(obj.getCodigo().intValue());
			executarMontagemInscricaoVOs();
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	/**
	 * Método responsavel por invocar outro Controlador passando por parâmetro o
	 * método a ser executado e o obj.
	 * 
	 * @Autor Carlos
	 */
	public void executarVisualizacaoHistoricoAlunoFichaAluno() {
		MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItem");
		executarMetodoControle(HistoricoAlunoRelControle.class.getSimpleName(), "selecionarAlunoVindoOutraTela", obj);
	}

	public void executarAdiarBloqueioAcessoAluno() {
		try {
			MatriculaVO obj = getMatriculaVO();
			getFacadeFactory().getMatriculaFacade().carregarDados(obj, getUsuarioLogado());
			getFacadeFactory().getMatriculaFacade().registrarAdiarBloqueio(obj);
			setMensagem("Bloqueio de Acesso adiado com Sucesso!");
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	/**
	 * Método responsável por apresentar a aba Dados Acadêmicos e consultar uma
	 * matricula pela chave primária e seus filhos(MatriculaPeriodo no caso).
	 * 
	 * @Autor Carlos
	 */
	public void executarVisualizacaoDadosAcademicoFichaAluno() {
		try {
			MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItem");
			String dadosCursoTurma = obj.getCurso().getNome();
			obj.setMatriculaPeriodoVOs(new ArrayList(0));
			getFacadeFactory().getMatriculaFacade().consultaRapidaFichaAlunoPorMatricula(obj, obj.getMatricula(), getUsuarioLogado());
			for (MatriculaPeriodoVO mp : obj.getMatriculaPeriodoVOs()) {
				mp.setMatriculaVO(obj);
				PlanoFinanceiroAlunoVO pfa = getFacadeFactory().getPlanoFinanceiroAlunoFacade().consultarPorMatriculaPeriodo(mp.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
				mp.setLogPlanoFinanceiroAluno(getFacadeFactory().getPlanoFinanceiroAlunoLogFacade().realizarCriacaoLogPlanoFinanceiroAlunoAtual(pfa));
			}
			obj.getCurso().setNome(dadosCursoTurma);
			setMatriculaVO(obj);
			getDocumetacaoMatriculaVOS().clear();
			getListaRegistroAtividadeComplementarMatriculaVOs().clear();
			 getListaRegistroAtividadeComplementarMatriculaVOs().clear();
			atualizarListaDocumentosMatriculaPeloCurso();
			montarListaAtividadeComplementarMatriculaVO();
			setApresentarAbaDadosAcademicosFichaAluno(true);
			setAbaSelecionada("dadosAcademicos");
			setMensagemID("msg_dados_consultados");
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}

	}

	public void validarPendenciaFinanceiraFichaAluno(Integer codPessoa) {
		try {
			setExistePendenciaFinanceira(getFacadeFactory().getContaReceberFacade().consultarPendenciaFinanceiraPorCodPessoaFichaAluno(codPessoa.intValue(), getUsuarioLogado()));
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	/**
	 * Método responsável por apresentar a aba Turma Disciplina e consultar uma
	 * matriculaPeriodo pela chave primária e seus
	 * filhos(MatriculaPeriodoTurmaDisciplina no caso).
	 * 
	 * @Autor Carlos
	 */
	public void executarVisualizacaoTurmaDisciplinaFichaAlunoXxx() {
		try {
			setMatriculaPeriodoVO((MatriculaPeriodoVO) context().getExternalContext().getRequestMap().get("matriculaPeriodoLista"));
			setListaHistoricoVO(new ArrayList(0));
			getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaFichaAlunoPorMatriculaPeriodo(getMatriculaPeriodoVO(), getMatriculaPeriodoVO().getCodigo());
			setListaHistoricoVO(getFacadeFactory().getHistoricoFacade().consultaRapidaHistoricoPorMatriculaPeriodo(getMatriculaPeriodoVO().getCodigo(), getMatriculaPeriodoVO().getMatriculaVO().getGradeCurricularAtual().getCodigo(), getUsuarioLogado()));
			setApresentarAbaDadosTurmaDisciplinaFichaAluno(true);
			setAbaSelecionada("turmaDisciplina");
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public void executarVisualizacaoDadosFinanceiroFichaAluno() {
		try {
			setListaContaReceberAluno(new ArrayList(0));
			MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItem");
			setListaContaReceberAluno(getFacadeFactory().getContaReceberFacade().consultaRapidaPorMatricula(obj.getMatricula()));
			Iterator i = getListaContaReceberAluno().iterator();
			while (i.hasNext()) {
				ContaReceberVO cr = (ContaReceberVO)i.next();
				if (cr.getSituacao().equals("RE")) {
					ContaReceberRecebimentoVO crr = (ContaReceberRecebimentoVO)getFacadeFactory().getContaReceberRecebimentoFacade().consultarPorCodigoContaReceber(cr.getCodigo().intValue());
					cr.setDataRecebimento(crr.getDataRecebimeto());
				}
			}
			setMatriculaVO(obj);
			setApresentarAbaDadosFinanceiroFichaAluno(true);
			setAbaSelecionada("dadosFinanceiro");
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public void executarImpressaoBoletimAcademicoPdfYyy() {
		MatriculaPeriodoVO obj = (MatriculaPeriodoVO) context().getExternalContext().getRequestMap().get("matriculaPeriodoLista");
		executarMetodoControle("BoletimAcademicoRelControle", "imprimirPDFVindoOutraTela", getMatriculaVO(), obj);
	}

	public String getExecutarImpressaoPlanoEstudoPDF() {
		MatriculaPeriodoVO obj = (MatriculaPeriodoVO) context().getExternalContext().getRequestMap().get("matriculaPeriodoLista");
		return "abrirPopup('../../DadosMatriculaSV?matriculaPeriodo=" + obj.getCodigo() + "&titulo=matricula', 'dadosMatricula', 780, 585)";
	}

	public void executarImpressaoDadosFinanceiroPDF() {
		executarMetodoControle("SituacaoFinanceiraAlunoRelControle", "imprimirPDFVindoOutraTela", getMatriculaVO());
	}

	public Boolean getIsdadosFinanceiro() {
		if (getAbaSelecionada().equals("dadosFinanceiro")) {
			return true;
		}
		return false;
	}

	public String getMascaraConsulta() {
		if (getControleConsulta().getCampoConsulta().equals("CPF")) {
			return "return mascara(this.form,'formCadastro:valorConsulta','999.999.999-99',event)";
		}
		return "";
	}

	public void importarPessoaCadastrada() throws Exception {
		if (getPessoaExistente().getCodigo().intValue() == 0) {
			setPessoaVO(getFacadeFactory().getProspectsFacade().realizarPreenchimentoPessoaPorProspect(getProspectsVO(), getUsuarioLogado()));
		} else {
			setPessoaVO((PessoaVO)Uteis.clonar(getPessoaExistente()));
			editarDadosPessoa(getPessoaVO());
		}
		// setFuncionarioVO(getFacadeFactory().getFuncionarioFacade().importarPessoaCadastrada(getFuncionarioVO(),
		// getPessoaExistente()));
		// setPessoaVO(new PessoaVO());
		// setPessoaVO(getFuncionarioVO().getPessoa());
		setImportarDadosPessoa(Boolean.FALSE);
		setConsultarPessoa(Boolean.FALSE);
		setMensagemDetalhada("");
		// return "editar";
	}

	public void naoImportarPessoaCadastrada() {
		setPessoaVO(new PessoaVO());
		setImportarDadosPessoa(Boolean.FALSE);
		setConsultarPessoa(Boolean.TRUE);
		setMensagemDetalhada("");
		// return "editar";
	}

	public void verificarExistenciaResponsavelFinanceiroExistente() throws Exception {
		try {
			if (getFiliacaoVO().getResponsavelFinanceiro()) {
				PessoaVO.validarDadosFiliacaoResponsavelFinanceiro(getPessoaVO(), getFiliacaoVO());
			}
			setMensagemDetalhada("");
			setMensagemID("msg_entre_dados");
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	// public void verificarExistenciaResponsavelLegalExistente() throws
	// Exception {
	// try {
	// setMensagemDetalhada("");
	// setMensagemID("msg_entre_dados");
	// getFacadeFactory().getPessoaFacade().validarDadosFiliacaoResponsavelLegal(getPessoaVO(),
	// getFiliacaoVO());
	// } catch (Exception ex) {
	// setMensagemDetalhada("msg_erro", ex.getMessage());
	// }
	// }

	public List getListaSelectItemAreaConhecimento() {
		return listaSelectItemAreaConhecimento;
	}

	public void setListaSelectItemAreaConhecimento(List listaSelectItemAreaConhecimento) {
		this.listaSelectItemAreaConhecimento = listaSelectItemAreaConhecimento;
	}

	public String getTurno_Erro() {
		return turno_Erro;
	}

	public void setTurno_Erro(String turno_Erro) {
		this.turno_Erro = turno_Erro;
	}

	public FiliacaoVO getFiliacaoVO() {
		if(filiacaoVO == null){
			filiacaoVO = new FiliacaoVO();
		}
		return filiacaoVO;
	}

	public void setFiliacaoVO(FiliacaoVO filiacaoVO) {
		this.filiacaoVO = filiacaoVO;
	}

	public String getDisciplina_Erro() {
		return disciplina_Erro;
	}

	public void setDisciplina_Erro(String disciplina_Erro) {
		this.disciplina_Erro = disciplina_Erro;
	}

	public FormacaoAcademicaVO getFormacaoAcademicaVO() {
		if(formacaoAcademicaVO == null){
			formacaoAcademicaVO = new FormacaoAcademicaVO();
		}
		return formacaoAcademicaVO;
	}

	public void setFormacaoAcademicaVO(FormacaoAcademicaVO formacaoAcademicaVO) {
		this.formacaoAcademicaVO = formacaoAcademicaVO;
	}

	public List getListaSelectItemNacionalidade() {
		if (listaSelectItemNacionalidade == null) {
			listaSelectItemNacionalidade = new ArrayList();
		}
		return (listaSelectItemNacionalidade);
	}

	public void setListaSelectItemNacionalidade(List listaSelectItemNacionalidade) {
		this.listaSelectItemNacionalidade = listaSelectItemNacionalidade;
	}

	public PessoaVO getPessoaVO() {
		if (pessoaVO == null) {
			pessoaVO = new PessoaVO();
		}
		return pessoaVO;
	}

	public void setPessoaVO(PessoaVO pessoaVO) {
		this.pessoaVO = pessoaVO;
	}

	public String formacaoAcademica() {
		return "formacaoAcademica";
	}

	public String dadosPessoais() {
		return "dadosPessoais";
	}

	public String documentosDadosFuncionais() {
		return "documentosDadosFuncionais";
	}

	public String filiacao() {
		return "filiacao";
	}

	public Boolean getVerificarCpf() {
		if (verificarCpf == null) {
			verificarCpf = this.validarCadastroPorCpf();
		}
		return verificarCpf;
	}

	public void setVerificarCpf(Boolean verificarCpf) {
		this.verificarCpf = verificarCpf;
	}

	public Boolean getEditarDados() {
		return editarDados;
	}

	public void setEditarDados(Boolean editarDados) {
		this.editarDados = editarDados;
	}

	public Boolean getConsultarPessoa() {
		return consultarPessoa;
	}

	public void setConsultarPessoa(Boolean consultarPessoa) {
		this.consultarPessoa = consultarPessoa;
	}

	public List getListaSelectItemPerfilEconomico() {
		if (listaSelectItemPerfilEconomico == null) {
			listaSelectItemPerfilEconomico = new ArrayList(0);
		}
		return listaSelectItemPerfilEconomico;
	}

	public void setListaSelectItemPerfilEconomico(List listaSelectItemPerfilEconomico) {
		this.listaSelectItemPerfilEconomico = listaSelectItemPerfilEconomico;
	}

	@Override
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
		pessoaVO = null;
		Uteis.liberarListaMemoria(listaConsultaCidade);
		Uteis.liberarListaMemoria(listaConsultaNaturalidade);
		Uteis.liberarListaMemoria(listaSelectItemNacionalidade);
		Uteis.liberarListaMemoria(listaSelectItemAreaConhecimento);
		Uteis.liberarListaMemoria(listaSelectItemPerfilEconomico);
		campoConsultaCidade = null;
		valorConsultaCidade = null;
		campoConsultaNaturalidade = null;
		valorConsultaNaturalidade = null;
		formacaoAcademicaVO = null;
		disciplina_Erro = null;
		filiacaoVO = null;
		turno_Erro = null;
	}

	/**
	 * @return the campoConsultaCidade
	 */
	public String getCampoConsultaCidade() {
		return campoConsultaCidade;
	}

	/**
	 * @param campoConsultaCidade
	 *            the campoConsultaCidade to set
	 */
	public void setCampoConsultaCidade(String campoConsultaCidade) {
		this.campoConsultaCidade = campoConsultaCidade;
	}

	/**
	 * @return the valorConsultaCidade
	 */
	public String getValorConsultaCidade() {
		return valorConsultaCidade;
	}

	/**
	 * @param valorConsultaCidade
	 *            the valorConsultaCidade to set
	 */
	public void setValorConsultaCidade(String valorConsultaCidade) {
		this.valorConsultaCidade = valorConsultaCidade;
	}

	/**
	 * @return the listaConsultaCidade
	 */
	public List getListaConsultaCidade() {
		return listaConsultaCidade;
	}

	/**
	 * @param listaConsultaCidade
	 *            the listaConsultaCidade to set
	 */
	public void setListaConsultaCidade(List listaConsultaCidade) {
		this.listaConsultaCidade = listaConsultaCidade;
	}

	/**
	 * @return the campoConsultaNaturalidade
	 */
	public String getCampoConsultaNaturalidade() {
		return campoConsultaNaturalidade;
	}

	/**
	 * @param campoConsultaNaturalidade
	 *            the campoConsultaNaturalidade to set
	 */
	public void setCampoConsultaNaturalidade(String campoConsultaNaturalidade) {
		this.campoConsultaNaturalidade = campoConsultaNaturalidade;
	}

	/**
	 * @return the valorConsultaNaturalidade
	 */
	public String getValorConsultaNaturalidade() {
		return valorConsultaNaturalidade;
	}

	/**
	 * @param valorConsultaNaturalidade
	 *            the valorConsultaNaturalidade to set
	 */
	public void setValorConsultaNaturalidade(String valorConsultaNaturalidade) {
		this.valorConsultaNaturalidade = valorConsultaNaturalidade;
	}

	/**
	 * @return the listaConsultaNaturalidade
	 */
	public List getListaConsultaNaturalidade() {
		return listaConsultaNaturalidade;
	}

	/**
	 * @param listaConsultaNaturalidade
	 *            the listaConsultaNaturalidade to set
	 */
	public void setListaConsultaNaturalidade(List listaConsultaNaturalidade) {
		this.listaConsultaNaturalidade = listaConsultaNaturalidade;
	}

	/**
	 * @return the habilitarIniciarInscricao
	 */
	public boolean getHabilitarIniciarInscricao() {
		return habilitarIniciarInscricao;
	}

	/**
	 * @param habilitarIniciarInscricao
	 *            the habilitarIniciarInscricao to set
	 */
	public void setHabilitarIniciarInscricao(boolean habilitarIniciarInscricao) {
		this.habilitarIniciarInscricao = habilitarIniciarInscricao;
	}

	/**
	 * @return the listaMatriculaAluno
	 */
	public List getListaMatriculaAluno() {
		if (listaMatriculaAluno == null) {
			listaMatriculaAluno = new ArrayList(0);
		}
		return listaMatriculaAluno;
	}

	/**
	 * @param listaMatriculaAluno
	 *            the listaMatriculaAluno to set
	 */
	public void setListaMatriculaAluno(List listaMatriculaAluno) {
		this.listaMatriculaAluno = listaMatriculaAluno;
	}

	/**
	 * @return the matriculaVO
	 */
	public MatriculaVO getMatriculaVO() {
		if (matriculaVO == null) {
			matriculaVO = new MatriculaVO();
		}
		return matriculaVO;
	}

	/**
	 * @param matriculaVO
	 *            the matriculaVO to set
	 */
	public void setMatriculaVO(MatriculaVO matriculaVO) {
		this.matriculaVO = matriculaVO;
	}

	/**
	 * @return the apresentarAbaDadosAcademicosFichaAluno
	 */
	public Boolean getApresentarAbaDadosAcademicosFichaAluno() {
		if (apresentarAbaDadosAcademicosFichaAluno == null) {
			apresentarAbaDadosAcademicosFichaAluno = Boolean.FALSE;
		}
		return apresentarAbaDadosAcademicosFichaAluno;
	}

	/**
	 * @param apresentarAbaDadosAcademicosFichaAluno
	 *            the apresentarAbaDadosAcademicosFichaAluno to set
	 */
	public void setApresentarAbaDadosAcademicosFichaAluno(Boolean apresentarAbaDadosAcademicosFichaAluno) {
		this.apresentarAbaDadosAcademicosFichaAluno = apresentarAbaDadosAcademicosFichaAluno;
	}

	/**
	 * @return the matriculaPeriodoVO
	 */
	public MatriculaPeriodoVO getMatriculaPeriodoVO() {
		if (matriculaPeriodoVO == null) {
			matriculaPeriodoVO = new MatriculaPeriodoVO();
		}
		return matriculaPeriodoVO;
	}

	/**
	 * @param matriculaPeriodoVO
	 *            the matriculaPeriodoVO to set
	 */
	public void setMatriculaPeriodoVO(MatriculaPeriodoVO matriculaPeriodoVO) {
		this.matriculaPeriodoVO = matriculaPeriodoVO;
	}

	/**
	 * @return the apresentarAbaDadosTurmaDisciplinaFichaAluno
	 */
	public Boolean getApresentarAbaDadosTurmaDisciplinaFichaAluno() {
		if (apresentarAbaDadosTurmaDisciplinaFichaAluno == null) {
			apresentarAbaDadosTurmaDisciplinaFichaAluno = Boolean.FALSE;
		}
		return apresentarAbaDadosTurmaDisciplinaFichaAluno;
	}

	/**
	 * @param apresentarAbaDadosTurmaDisciplinaFichaAluno
	 *            the apresentarAbaDadosTurmaDisciplinaFichaAluno to set
	 */
	public void setApresentarAbaDadosTurmaDisciplinaFichaAluno(Boolean apresentarAbaDadosTurmaDisciplinaFichaAluno) {
		this.apresentarAbaDadosTurmaDisciplinaFichaAluno = apresentarAbaDadosTurmaDisciplinaFichaAluno;
	}

	/**
	 * @return the abaSelecionada
	 */
	public String getAbaSelecionada() {
		if (abaSelecionada == null) {
			abaSelecionada = "";
		}
		return abaSelecionada;
	}

	/**
	 * @param abaSelecionada
	 *            the abaSelecionada to set
	 */
	public void setAbaSelecionada(String abaSelecionada) {
		this.abaSelecionada = abaSelecionada;
	}

	/**
	 * @return the listaContaReceberAluno
	 */
	public List getListaContaReceberAluno() {
		if (listaContaReceberAluno == null) {
			listaContaReceberAluno = new ArrayList(0);
		}
		return listaContaReceberAluno;
	}

	/**
	 * @param listaContaReceberAluno
	 *            the listaContaReceberAluno to set
	 */
	public void setListaContaReceberAluno(List listaContaReceberAluno) {
		this.listaContaReceberAluno = listaContaReceberAluno;
	}

	/**
	 * @return the apresentarAbaDadosFinanceiroFichaAluno
	 */
	public Boolean getApresentarAbaDadosFinanceiroFichaAluno() {
		if (apresentarAbaDadosFinanceiroFichaAluno == null) {
			apresentarAbaDadosFinanceiroFichaAluno = Boolean.FALSE;
		}
		return apresentarAbaDadosFinanceiroFichaAluno;
	}

	/**
	 * @param apresentarAbaDadosFinanceiroFichaAluno
	 *            the apresentarAbaDadosFinanceiroFichaAluno to set
	 */
	public void setApresentarAbaDadosFinanceiroFichaAluno(Boolean apresentarAbaDadosFinanceiroFichaAluno) {
		this.apresentarAbaDadosFinanceiroFichaAluno = apresentarAbaDadosFinanceiroFichaAluno;
	}

	/**
	 * @return the existePendenciaFinanceira
	 */
	public Boolean getExistePendenciaFinanceira() throws Exception {
		if (existePendenciaFinanceira == null) {
			existePendenciaFinanceira = Boolean.FALSE;
		}
		return existePendenciaFinanceira;
	}

	/**
	 * @param existePendenciaFinanceira
	 *            the existePendenciaFinanceira to set
	 */
	public void setExistePendenciaFinanceira(Boolean existePendenciaFinanceira) {
		this.existePendenciaFinanceira = existePendenciaFinanceira;
	}

	/**
	 * @return the listaHistoricoVO
	 */
	public List getListaHistoricoVO() {
		if (listaHistoricoVO == null) {
			listaHistoricoVO = new ArrayList(0);
		}
		return listaHistoricoVO;
	}

	/**
	 * @param listaHistoricoVO
	 *            the listaHistoricoVO to set
	 */
	public void setListaHistoricoVO(List listaHistoricoVO) {
		this.listaHistoricoVO = listaHistoricoVO;
	}

	public PessoaVO getPessoaExistente() {
		if (pessoaExistente == null) {
			pessoaExistente = new PessoaVO();
		}
		return pessoaExistente;
	}

	public void setPessoaExistente(PessoaVO pessoaExistente) {
		this.pessoaExistente = pessoaExistente;
	}

	public Boolean getImportarDadosPessoa() {
		return importarDadosPessoa;
	}

	public void setImportarDadosPessoa(Boolean importarDadosPessoa) {
		this.importarDadosPessoa = importarDadosPessoa;
	}

	public void realizarRegistroNaoEnviarMensagemCobranca() {
		try {
			MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItem");
			getFacadeFactory().getMatriculaFacade().alterarNaoEnviarMensagemCobranca(obj.getMatricula(), obj.getNaoEnviarMensagemCobranca());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setImprimirContrato(false);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void realizarRegistroAlunoConcluiuDisciplinasRegulares() {
		try {
			MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItem");
			getFacadeFactory().getMatriculaFacade().alterarAlunoConcluiuDisciplinasRegulares(obj.getMatricula(), obj.getAlunoConcluiuDisciplinasRegulares());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setImprimirContrato(false);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void emitirContratoMatricula() {
		try {
			MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItem");
			setMatriculaVO(obj);
			setMatriculaPeriodoVO(getFacadeFactory().getMatriculaPeriodoFacade().consultarPorChavePrimaria(obj.getMatriculaPeriodoVOAtiva().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
			if (Uteis.isAtributoPreenchido(getMatriculaPeriodoVO().getPlanoFinanceiroCurso().getCodigo())) {
				setImprimirContrato(true);
				imprimirContrato("MA");
			} else {
				setMensagemID("msg_matricula_semcontratodefinido");
				setImprimirContrato(false);
			}
		} catch (Exception e) {
			setImprimirContrato(false);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String getContrato() {
		if (getImprimirContrato()) {
			return "abrirPopup('../../VisualizarContrato', 'RelatorioContrato', 730, 545)";
		}
		return "";
	}

	public void imprimirContrato(String tipoContrato) throws Exception {
		PlanoFinanceiroCursoVO planoFin = getFacadeFactory().getPlanoFinanceiroCursoFacade().consultarPorChavePrimaria(getMatriculaPeriodoVO().getPlanoFinanceiroCurso().getCodigo(), "", Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
		if(Uteis.isAtributoPreenchido(planoFin.getTextoPadraoContratoMatricula().getCodigo())){
			setImprimirContrato(true);
		}else{
			setMensagemID("msg_matricula_semcontratodefinido");
			setImprimirContrato(false);
			return;
		}
		Matricula.montarDadosUnidadeEnsino(getMatriculaVO(), Uteis.NIVELMONTARDADOS_DADOSBASICOS);
		getFacadeFactory().getMatriculaPeriodoFacade().carregarDadosConsultorUsuarioResp(getMatriculaVO(), getMatriculaPeriodoVO(), getUsuarioLogado());
		getMatriculaVO().setAluno(getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(getMatriculaVO().getAluno().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
 		getMatriculaVO().setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(getMatriculaVO().getCurso().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, getUsuarioLogado()));
		String ano = Constantes.EMPTY;
		String semestre = Constantes.EMPTY;
		if (!getMatriculaVO().getCurso().getNivelEducacionalPosGraduacao()) {
			ano = getMatriculaPeriodoVO().getAno();
			semestre = getMatriculaPeriodoVO().getSemestre();
		}
		getMatriculaPeriodoVO().setDataInicioAula(getFacadeFactory().getHorarioTurmaDiaFacade().consultarPrimeiraDataAulaPorTurmaAgrupada(getMatriculaPeriodoVO().getTurma().getCodigo(), ano, semestre));
		getMatriculaPeriodoVO().setDataFinalAula(getFacadeFactory().getHorarioTurmaDiaFacade().consultarUltimaDataAulaPorTurmaAgrupada(getMatriculaPeriodoVO().getTurma().getCodigo(), ano, semestre));
		PlanoFinanceiroAlunoVO plano = getFacadeFactory().getPlanoFinanceiroAlunoFacade().consultarPorMatriculaPeriodo(getMatriculaPeriodoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		DadosComerciaisVO dc = getFacadeFactory().getDadosComerciaisFacade().consultarEmpregoAtualPorCodigoPessoa(getMatriculaVO().getAluno().getCodigo(), getUsuarioLogado());
		String contratoPronto = Constantes.EMPTY;
		if (tipoContrato.equals("MA")) {
			contratoPronto = planoFin.getTextoPadraoContratoMatricula().substituirTagsTextoPadraoContratoMatricula(null, getMatriculaVO(),  getMatriculaPeriodoVO().getMatriculaPeriodoVencimentoVOs(), getMatriculaPeriodoVO(), plano, new ArrayList<PlanoDescontoVO>(), dc, getUsuarioLogado());
		}
		if (tipoContrato.equals("FI")) {
			contratoPronto = planoFin.getTextoPadraoContratoFiador().substituirTagsTextoPadraoContratoFiador(getMatriculaVO(),  getMatriculaPeriodoVO().getMatriculaPeriodoVencimentoVOs(), getMatriculaPeriodoVO(), plano, new ArrayList<PlanoDescontoVO>(), dc, getUsuarioLogado());
		}
		HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
		request.getSession().setAttribute("textoRelatorio", contratoPronto);
	}

	public void upLoadImagem(FileUploadEvent uploadEvent) {
		try {
			getPessoaVO().getArquivoImagem().setCpfRequerimento(getPessoaVO().getCPF());
			getFacadeFactory().getArquivoHelper().upLoad(uploadEvent, getPessoaVO().getArquivoImagem(), getConfiguracaoGeralPadraoSistema(), PastaBaseArquivoEnum.IMAGEM_TMP, getUsuarioLogado());
			setCaminhoFotoUsuario(getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(getPessoaVO().getArquivoImagem(), PastaBaseArquivoEnum.IMAGEM_TMP.getValue(), getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(), "foto_usuario.png", true));
			setExibirBotao(Boolean.TRUE);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			uploadEvent = null;
		}
	}

	public void recortarFoto() {
		try {
			getFacadeFactory().getArquivoHelper().recortarFoto(getPessoaVO().getArquivoImagem(), getConfiguracaoGeralPadraoSistema(), getLargura(), getAltura(), getX(), getY());
			setCaminhoFotoUsuario(getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(getPessoaVO().getArquivoImagem(), PastaBaseArquivoEnum.IMAGEM.getValue(), getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(), "foto_usuario.jpg", false));
			inicializarBooleanoFoto();
			getPessoaVO().getArquivoImagem().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.IMAGEM);
			gravar();
			setOncompleteModal("RichFaces.$('panelImagem').hide();");
		} catch (Exception ex) {
			setOncompleteModal("RichFaces.$('panelImagem').show();");
			setMensagemDetalhada("msg_erro", ex.getMessage(), Uteis.ERRO);
		}
	}
	
	public void removerImagensUploadArquivoTemp() throws Exception {
		try {
			String arquivoExterno = getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoTemp() + File.separator + PastaBaseArquivoEnum.IMAGEM_TMP.getValue() + File.separator + getPessoaVO().getCPF();
			File arquivo = new File(arquivoExterno);
			getArquivoHelper().delete(arquivo);
		} catch (Exception e) {
			throw e;
		}
	}
	
	public void renderizarUpload() {
		setExibirUpload(false);
	}

	public void cancelar()  {
		try {
			if(getPessoaVO().getArquivoImagem().getDescricao() != "" && getPessoaVO().getArquivoImagem() != null) {
				if(getPessoaVO().getArquivoImagem().getPastaBaseArquivoEnum() != null && getPessoaVO().getArquivoImagem().getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.IMAGEM_TMP)) {
					removerImagensUploadArquivoTemp();
				}
				if(getPessoaVO().getArquivoImagem().getCodigo()  > 0) {
					getPessoaVO().setArquivoImagem(getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(getPessoaVO().getArquivoImagem().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
				}else {					
					getPessoaVO().setArquivoImagem(null);
				}
				setCaminhoFotoUsuario(getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(getPessoaVO().getArquivoImagem(), PastaBaseArquivoEnum.IMAGEM.getValue(), getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(), "foto_usuario.jpg", false)+"?uid="+new Date().getTime());				
				inicializarBooleanoFoto();
			}			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

	}

	public String getShowFotoCrop() {
		try {
			if (getPessoaVO().getArquivoImagem().getNome() == null) {
				return "resources/imagens/usuarioPadrao.jpg";
			}
			return getCaminhoFotoUsuario()+"?UID="+new Date().getTime();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getTagImageComFotoPadrao();
		}
	}
	
	 public void adicionarPessoaGsuite() {
			try {
				getFacadeFactory().getPessoaFacade().adicionarPessoaGsuite(getPessoaVO(), getPessoaGsuiteVO(), getUsuarioLogadoClone());
				setPessoaGsuiteVO(new PessoaGsuiteVO());
//				gravarPessoaGsuite();
				setMensagemID(MSG_TELA.msg_dados_adicionados.name());
			} catch (Exception e) {
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			}
		}
		
		public void editarPessoaGsuite() {
			try {
				PessoaGsuiteVO obj = (PessoaGsuiteVO) context().getExternalContext().getRequestMap().get("pessoaGsuiteItem");
				setPessoaGsuiteVO((PessoaGsuiteVO) obj.clone());
				setMensagemID(MSG_TELA.msg_dados_selecionados.name());
			} catch (Exception e) {
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			}

		}

		public void removerPessoaGsuite() {
			try {
				PessoaGsuiteVO obj = (PessoaGsuiteVO) context().getExternalContext().getRequestMap().get("pessoaGsuiteItem");
				getFacadeFactory().getPessoaFacade().removerPessoaGsuite(getPessoaVO(), obj, getUsuarioLogadoClone());
				setMensagemID(MSG_TELA.msg_dados_excluidos.name());
			} catch (Exception e) {
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			}
		}

		public PessoaGsuiteVO getPessoaGsuiteVO() {
			if (pessoaGsuiteVO == null) {
				pessoaGsuiteVO = new PessoaGsuiteVO();
			}
			return pessoaGsuiteVO;
		}

		public void setPessoaGsuiteVO(PessoaGsuiteVO pessoaGsuiteVO) {
			this.pessoaGsuiteVO = pessoaGsuiteVO;
		}
		

		public void adicionarPessoaEmailInstitucional() {
			try {
				getFacadeFactory().getPessoaFacade().adicionarPessoaEmailInstitucionalVO(getPessoaVO(), getPessoaEmailInstitucionalVO(), getUsuarioLogadoClone());
				setPessoaEmailInstitucionalVO(new PessoaEmailInstitucionalVO());
				setMensagemID(MSG_TELA.msg_dados_adicionados.name());
			} catch (Exception e) {
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			}
		}

		public void editarPessoaEmailInstitucional() {
			try {
				PessoaEmailInstitucionalVO obj = (PessoaEmailInstitucionalVO) context().getExternalContext().getRequestMap().get("pessoaEmailInstitucionalItem");
				setPessoaEmailInstitucionalVO((PessoaEmailInstitucionalVO) obj.clone());
				setMensagemID(MSG_TELA.msg_dados_selecionados.name());
			} catch (Exception e) {
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			}

		}

		public void removerPessoaEmailInstitucional() {
			try {
				PessoaEmailInstitucionalVO obj = (PessoaEmailInstitucionalVO) context().getExternalContext().getRequestMap().get("pessoaEmailInstitucionalItem");
				getFacadeFactory().getPessoaFacade().removerPessoaEmailInstitucionalVO(getPessoaVO(), obj, getUsuarioLogadoClone());
				setMensagemID(MSG_TELA.msg_dados_excluidos.name());
			} catch (Exception e) {
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			}
		}

	public PessoaEmailInstitucionalVO getPessoaEmailInstitucionalVO() {
		if (pessoaEmailInstitucionalVO == null) {
			pessoaEmailInstitucionalVO = new PessoaEmailInstitucionalVO();
		}
		return pessoaEmailInstitucionalVO;
	}

	public void setPessoaEmailInstitucionalVO(PessoaEmailInstitucionalVO pessoaEmailInstitucionalVO) {
		this.pessoaEmailInstitucionalVO = pessoaEmailInstitucionalVO;
	}

	public void setImprimirContrato(Boolean imprimirContrato) {
		this.imprimirContrato = imprimirContrato;
	}

	public Boolean getImprimirContrato() {
		if (imprimirContrato == null) {
			imprimirContrato = true;
		}
		return imprimirContrato;
	}

	public Boolean getControleDataInicio() {
		Boolean retorno;
		if (getFormacaoAcademicaVO().getSituacao().equals("CO")) {
			retorno = false;
		} else {
			retorno = true;
		}
		return retorno;
	}

	/**
	 * @return the campoConsultaCidadeEmpresa
	 */
	public String getCampoConsultaCidadeEmpresa() {
		if (campoConsultaCidadeEmpresa == null) {
			campoConsultaCidadeEmpresa = "";
		}
		return campoConsultaCidadeEmpresa;
	}

	/**
	 * @param campoConsultaCidadeEmpresa
	 *            the campoConsultaCidadeEmpresa to set
	 */
	public void setCampoConsultaCidadeEmpresa(String campoConsultaCidadeEmpresa) {
		this.campoConsultaCidadeEmpresa = campoConsultaCidadeEmpresa;
	}

	/**
	 * @return the valorConsultaCidadeEmpresa
	 */
	public String getValorConsultaCidadeEmpresa() {
		if (valorConsultaCidadeEmpresa == null) {
			valorConsultaCidadeEmpresa = "";
		}
		return valorConsultaCidadeEmpresa;
	}

	/**
	 * @param valorConsultaCidadeEmpresa
	 *            the valorConsultaCidadeEmpresa to set
	 */
	public void setValorConsultaCidadeEmpresa(String valorConsultaCidadeEmpresa) {
		this.valorConsultaCidadeEmpresa = valorConsultaCidadeEmpresa;
	}

	/**
	 * @return the listaConsultaCidadeEmpresa
	 */
	public List getListaConsultaCidadeEmpresa() {
		if (listaConsultaCidadeEmpresa == null) {
			listaConsultaCidadeEmpresa = new ArrayList(0);
		}
		return listaConsultaCidadeEmpresa;
	}

	/**
	 * @param listaConsultaCidadeEmpresa
	 *            the listaConsultaCidadeEmpresa to set
	 */
	public void setListaConsultaCidadeEmpresa(List listaConsultaCidadeEmpresa) {
		this.listaConsultaCidadeEmpresa = listaConsultaCidadeEmpresa;
	}

	/**
	 * @return the ajaxRedirecionarFocoCampo
	 */
	public String getAjaxRedirecionarFocoCampo() {
		if (ajaxRedirecionarFocoCampo == null) {
			ajaxRedirecionarFocoCampo = "";
		}
		return ajaxRedirecionarFocoCampo;
	}

	/**
	 * @param ajaxRedirecionarFocoCampo
	 *            the ajaxRedirecionarFocoCampo to set
	 */
	public void setAjaxRedirecionarFocoCampo(String ajaxRedirecionarFocoCampo) {
		this.ajaxRedirecionarFocoCampo = ajaxRedirecionarFocoCampo;
	}

	public boolean getIsApresentarBotaoGravarFotoUsuario() {
		return !getPessoaVO().getNovoObj();
	}

	public DadosComerciaisVO getDadosComerciaisVO() {
		return dadosComerciaisVO;
	}

	public void setDadosComerciaisVO(DadosComerciaisVO dadosComerciaisVO) {
		this.dadosComerciaisVO = dadosComerciaisVO;
	}

	public FormacaoExtraCurricularVO getFormacaoExtraCurricularVO() {
		if(formacaoExtraCurricularVO == null){
			formacaoExtraCurricularVO = new FormacaoExtraCurricularVO();
		}
		return formacaoExtraCurricularVO;
	}

	public void setFormacaoExtraCurricularVO(FormacaoExtraCurricularVO formacaoExtraCurricularVO) {
		this.formacaoExtraCurricularVO = formacaoExtraCurricularVO;
	}

	public List getListaSelectItemAreaProfissional() {
		return listaSelectItemAreaProfissional;
	}

	public void setListaSelectItemAreaProfissional(List listaSelectItemAreaProfissional) {
		this.listaSelectItemAreaProfissional = listaSelectItemAreaProfissional;
	}

	public AreaProfissionalVO getAreaProfissionalVO() {
		if (areaProfissionalVO == null) {
			areaProfissionalVO = new AreaProfissionalVO();
		}
		return areaProfissionalVO;
	}

	public void setAreaProfissionalVO(AreaProfissionalVO areaProfissionalVO) {
		this.areaProfissionalVO = areaProfissionalVO;
	}

	public String getTipoDocumento() {
		if (tipoDocumento == null) {
			tipoDocumento = "";
		}
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public Boolean getApresentarTipoDocumentoCPF() {
		return getTipoDocumento().equals("CPF");
	}

	/**
	 * @return the compromissoAgendaPessoaHorarioVO
	 */
	public CompromissoAgendaPessoaHorarioVO getCompromissoAgendaPessoaHorarioVO() {
		return compromissoAgendaPessoaHorarioVO;
	}

	/**
	 * @param compromissoAgendaPessoaHorarioVO
	 *            the compromissoAgendaPessoaHorarioVO to set
	 */
	public void setCompromissoAgendaPessoaHorarioVO(CompromissoAgendaPessoaHorarioVO compromissoAgendaPessoaHorarioVO) {
		this.compromissoAgendaPessoaHorarioVO = compromissoAgendaPessoaHorarioVO;
	}

	/**
	 * @return the apresentarMatriculaCRM
	 */
	public Boolean getApresentarMatriculaCRM() {
		if (apresentarMatriculaCRM == null) {
			apresentarMatriculaCRM = false;
		}
		return apresentarMatriculaCRM;
	}

	/**
	 * @param apresentarMatriculaCRM
	 *            the apresentarMatriculaCRM to set
	 */
	public void setApresentarMatriculaCRM(Boolean apresentarMatriculaCRM) {
		this.apresentarMatriculaCRM = apresentarMatriculaCRM;
	}

	public String getCampoConsultaFiliacaoCidade() {
		if (campoConsultaFiliacaoCidade == null) {
			campoConsultaFiliacaoCidade = "";
		}
		return campoConsultaFiliacaoCidade;
	}

	public void setCampoConsultaFiliacaoCidade(String campoConsultaFiliacaoCidade) {
		this.campoConsultaFiliacaoCidade = campoConsultaFiliacaoCidade;
	}

	public String getValorConsultaFiliacaoCidade() {
		if (valorConsultaFiliacaoCidade == null) {
			valorConsultaFiliacaoCidade = "";
		}
		return valorConsultaFiliacaoCidade;
	}

	public void setValorConsultaFiliacaoCidade(String valorConsultaFiliacaoCidade) {
		this.valorConsultaFiliacaoCidade = valorConsultaFiliacaoCidade;
	}

	public List getListaConsultaFiliacaoCidade() {
		if (listaConsultaFiliacaoCidade == null) {
			listaConsultaFiliacaoCidade = new ArrayList(0);
		}
		return listaConsultaFiliacaoCidade;
	}

	public void setListaConsultaFiliacaoCidade(List listaConsultaFiliacaoCidade) {
		this.listaConsultaFiliacaoCidade = listaConsultaFiliacaoCidade;
	}

	public boolean getApresentarCamposEditarFiliacao() {
		return (getFiliacaoVO() != null && getFiliacaoVO().getCodigo() != 0);
	}

	public Boolean getPermiteEditarNomeFiliacao() {
		if (getFiliacaoVO() != null && getFiliacaoVO().getCodigo() != 0) {
			return true;
		}
		return false;
	}

	public String getApresentarCss() {
		if (getFiliacaoVO() != null && getFiliacaoVO().getCodigo() != 0) {
			return "camposSomenteLeitura";
		} else {
			return "campos";
		}

	}

	public InteracaoWorkflowVO getInteracaoWorkflowVO() {
		if (interacaoWorkflowVO == null) {
			interacaoWorkflowVO = new InteracaoWorkflowVO();
		}
		return interacaoWorkflowVO;
	}

	public void setInteracaoWorkflowVO(InteracaoWorkflowVO interacaoWorkflowVO) {
		this.interacaoWorkflowVO = interacaoWorkflowVO;
	}

	public AgendaPessoaHorarioVO getAgendaPessoaHorarioVO() {
		if (agendaPessoaHorarioVO == null) {
			agendaPessoaHorarioVO = new AgendaPessoaHorarioVO();
		}
		return agendaPessoaHorarioVO;
	}

	public void setAgendaPessoaHorarioVO(AgendaPessoaHorarioVO agendaPessoaHorarioVO) {
		this.agendaPessoaHorarioVO = agendaPessoaHorarioVO;
	}

	public Boolean getPermitirAlterarCompromisso() {
		if (permitirAlterarCompromisso == null) {
			permitirAlterarCompromisso = Boolean.FALSE;
		}
		return permitirAlterarCompromisso;
	}

	public void setPermitirAlterarCompromisso(Boolean permitirAlterarCompromisso) {
		this.permitirAlterarCompromisso = permitirAlterarCompromisso;
	}

	/**
	 * @return the prospectsVO
	 */
	public ProspectsVO getProspectsVO() {
		if (prospectsVO == null) {
			prospectsVO = new ProspectsVO();
		}
		return prospectsVO;
	}

	/**
	 * @param prospectsVO
	 *            the prospectsVO to set
	 */
	public void setProspectsVO(ProspectsVO prospectsVO) {
		this.prospectsVO = prospectsVO;
	}

	/**
	 * @return the matriculaDiretaAgendaCRM
	 */
	public Boolean getMatriculaDiretaAgendaCRM() {
		if (matriculaDiretaAgendaCRM == null) {
			matriculaDiretaAgendaCRM = Boolean.FALSE;
		}
		return matriculaDiretaAgendaCRM;
	}

	/**
	 * @param matriculaDiretaAgendaCRM
	 *            the matriculaDiretaAgendaCRM to set
	 */
	public void setMatriculaDiretaAgendaCRM(Boolean matriculaDiretaAgendaCRM) {
		this.matriculaDiretaAgendaCRM = matriculaDiretaAgendaCRM;
	}

	public void imprimirFichaAluno() {
		List<BoletimAcademicoRelVO> boletimAcademicoRelVOs = new ArrayList<BoletimAcademicoRelVO>(0);
		String titulo = "FICHA INDIVIDUAL";
		String nomeEntidade = super.getUnidadeEnsinoLogado().getNome();
		String design = BoletimAcademicoRel.getDesignIReportRelatorio("FichaAluno2Rel");
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "BoletimAcademicoRelControle", "Inicializando Geração de Relatório Boletim Acadêmico", "Emitindo Relatório");
			getFacadeFactory().getMatriculaFacade().carregarDados(getMatriculaVO(), NivelMontarDados.TODOS, getUsuarioLogado());
			boletimAcademicoRelVOs.addAll(getFacadeFactory().getBoletimAcademicoRelFacade().criarObjeto(getMatriculaVO(), "FichaAluno2Rel", true, getMatriculaPeriodoVO().getAno() + getMatriculaPeriodoVO().getSemestre(), getMatriculaPeriodoVO().getTurma(), getMatriculaVO().getUnidadeEnsino(), getConfiguracaoFinanceiroPadraoSistema(), getApresentarDisciplinaComposta(), getUsuarioLogado(), getApresentarCampoAssinatura(), false, getBoletimAcademicoRelVO().getFuncionarioPrincipalVO(), getBoletimAcademicoRelVO().getCargoFuncionarioPrincipal(), getBoletimAcademicoRelVO().getFuncionarioSecundarioVO(), getBoletimAcademicoRelVO().getCargoFuncionarioSecundario(), null,null, false, true, getApresentarCampoAssinaturaResponsavel(), false, new FiltroRelatorioAcademicoVO(), getMatriculaVO().getGradeCurricularAtual(), false, false, new ArrayList<>(), null));
			UnidadeEnsinoVO unidadeEnsinoVO = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getMatriculaVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			getSuperParametroRelVO().setNomeDesignIreport(design);
			getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
			getSuperParametroRelVO().setSubReport_Dir(BoletimAcademicoRel.getCaminhoBaseRelatorio());
			getSuperParametroRelVO().setCaminhoBaseRelatorio(BoletimAcademicoRel.getCaminhoBaseRelatorio());
			getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
			getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
			getSuperParametroRelVO().setTituloRelatorio(titulo);
			getSuperParametroRelVO().setNomeEmpresa(super.getUnidadeEnsinoLogado().getNome());
			getSuperParametroRelVO().setUnidadeEnsino(nomeEntidade);
			getSuperParametroRelVO().setListaObjetos(boletimAcademicoRelVOs);
			String enderecoUnidadeEnsino = unidadeEnsinoVO.getEndereco() + ", nº " + unidadeEnsinoVO.getNumero() + ", " + unidadeEnsinoVO.getSetor() + ", " + unidadeEnsinoVO.getCidade().getNome() + "-" + unidadeEnsinoVO.getCidade().getEstado().getSigla() + ", Fone:" + unidadeEnsinoVO.getTelComercial1();
			getSuperParametroRelVO().adicionarParametro("endereco", enderecoUnidadeEnsino);
			realizarImpressaoRelatorio();
			removerObjetoMemoria(this);
			registrarAtividadeUsuario(getUsuarioLogado(), "BoletimAcademicoRelControle", "Finalizando Geração de Relatório Boletim Acadêmico", "Emitindo Relatório");
			if (boletimAcademicoRelVOs.isEmpty()) {
				setMensagemID("msg_relatorio_sem_dados");
			} else {
				setMensagemID("msg_relatorio_ok");
			}
			getBoletimAcademicoRelVO().setFuncionarioPrincipalVO(null);
			getBoletimAcademicoRelVO().setFuncionarioSecundarioVO(null);
			setValorConsultaFuncionario("");
			getListaConsultaFuncionario().clear();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(boletimAcademicoRelVOs);
			titulo = null;
			nomeEntidade = null;
			design = null;
		}
	}

	public List<SelectItem> getListaSelectItemMatricula() {
		if (listaSelectItemMatricula == null) {
			listaSelectItemMatricula = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemMatricula;
	}

	public void setListaSelectItemMatricula(List<SelectItem> listaSelectItemMatricula) {
		this.listaSelectItemMatricula = listaSelectItemMatricula;
	}

	public void montarListaSelectItemMatricula() throws Exception {
		List<SelectItem> objs = new ArrayList<SelectItem>(0);
		PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("pessoaItem");
		List<MatriculaVO> matriculaVOs = getFacadeFactory().getMatriculaFacade().consultarMatriculaPorCodigoPessoaParaComboBox(obj.getCodigo(), getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
		for (MatriculaVO matriculaVO : matriculaVOs) {
			objs.add(new SelectItem(matriculaVO.getMatricula(), matriculaVO.getMatricula() + " - "+matriculaVO.getCurso().getNome() + " - " + matriculaVO.getSituacao_Apresentar()));
		}
		setListaSelectItemMatricula(objs);
	}

	public List getTipoConsultaComboFuncionario() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("CPF", "CPF"));
		itens.add(new SelectItem("cargo", "Cargo"));
		itens.add(new SelectItem("departamento", "Departamento"));
		return itens;
	}

	public void selecionarFuncionarioPrincipal() throws Exception {
		FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioPrincipalItem");
		getBoletimAcademicoRelVO().setFuncionarioPrincipalVO(obj);
		consultarFuncionarioPrincipal();
		getListaConsultaFuncionario().clear();
		setValorConsultaFuncionario("");
	}

	public void selecionarFuncionarioSecundario() throws Exception {
		FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioSecundarioItem");
		getBoletimAcademicoRelVO().setFuncionarioSecundarioVO(obj);
		consultarFuncionarioSecundario();
		getListaConsultaFuncionario().clear();
		setValorConsultaFuncionario("");
	}

	public void consultarFuncionarioPrincipal() throws Exception {
		try {
			getBoletimAcademicoRelVO().setFuncionarioPrincipalVO(consultarFuncionarioPorMatricula(getBoletimAcademicoRelVO().getFuncionarioPrincipalVO().getMatricula()));
			setSelectItemsCargoFuncionarioPrincipal(montarComboCargoFuncionario(getBoletimAcademicoRelVO().getFuncionarioPrincipalVO().getFuncionarioCargoVOs()));
			if (!getSelectItemsCargoFuncionarioPrincipal().isEmpty()) {
				getBoletimAcademicoRelVO().setCargoFuncionarioPrincipal(getFacadeFactory().getCargoFacade().consultarPorChavePrimaria((Integer) getSelectItemsCargoFuncionarioPrincipal().get(0).getValue(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			} else {
				getBoletimAcademicoRelVO().getCargoFuncionarioPrincipal().setCodigo(0);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarFuncionarioSecundario() throws Exception {
		try {
			getBoletimAcademicoRelVO().setFuncionarioSecundarioVO(consultarFuncionarioPorMatricula(getBoletimAcademicoRelVO().getFuncionarioSecundarioVO().getMatricula()));
			setSelectItemsCargoFuncionarioSecundario(montarComboCargoFuncionario(getBoletimAcademicoRelVO().getFuncionarioSecundarioVO().getFuncionarioCargoVOs()));
			if (!getSelectItemsCargoFuncionarioSecundario().isEmpty()) {
				getBoletimAcademicoRelVO().setCargoFuncionarioSecundario(getFacadeFactory().getCargoFacade().consultarPorChavePrimaria((Integer) getSelectItemsCargoFuncionarioSecundario().get(0).getValue(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			} else {
				getBoletimAcademicoRelVO().getCargoFuncionarioSecundario().setCodigo(0);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public FuncionarioVO consultarFuncionarioPorMatricula(String matricula) throws Exception {
		FuncionarioVO funcionarioVO = null;
		try {
			funcionarioVO = getFacadeFactory().getFuncionarioFacade().consultarPorMatricula(matricula, 0, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			if (Uteis.isAtributoPreenchido(funcionarioVO)) {
				return funcionarioVO;
			} else {
				setMensagemDetalhada("msg_erro", "Funcionário de matrícula " + matricula + " não encontrado. Verifique se o número de matrícula está correto.");
			}
			setMensagemDetalhada("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return new FuncionarioVO();
	}
	
	public String getNomeResponsavelFinanceiro() {
		if (!getPessoaVO().getFiliacaoVOs().isEmpty()) {
			for (FiliacaoVO filiacaoVO : getPessoaVO().getFiliacaoVOs()) {
				if (filiacaoVO.getResponsavelFinanceiro()) {
					return filiacaoVO.getNome();
				}
			}
		}
		return "";
	}
	
	public String getCpfResponsavelFinanceiro() {
		if (!getPessoaVO().getFiliacaoVOs().isEmpty()) {
			for (FiliacaoVO filiacaoVO : getPessoaVO().getFiliacaoVOs()) {
				if (filiacaoVO.getResponsavelFinanceiro()) {
					return filiacaoVO.getCPF();
				}
			}
		}
		return "";
	}

	public List<SelectItem> montarComboCargoFuncionario(List<FuncionarioCargoVO> cargos) throws Exception {
		try {
			if (cargos != null && !cargos.isEmpty()) {
				List<SelectItem> selectItems = new ArrayList<SelectItem>();
				for (FuncionarioCargoVO funcionarioCargoVO : cargos) {
					selectItems.add(new SelectItem(funcionarioCargoVO.getCargo().getCodigo(), funcionarioCargoVO.getCargo().getNome() + " - " + funcionarioCargoVO.getUnidade().getNome()));
				}
				return selectItems;
			} else {
				setMensagemDetalhada("O Funcionário selecionado não possui cargo configurado");
			}
			return null;
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(cargos);
		}
	}

	public void consultarFuncionario() {
		try {
			List objs = new ArrayList(0);
			getFacadeFactory().getPessoaFacade().setIdEntidade("Funcionario");
			if (getCampoConsultaFuncionario().equals("nome")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNome(getValorConsultaFuncionario(), "FU", 0, true, true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("matricula")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorMatricula(getValorConsultaFuncionario(), 0, true, true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("nomeCidade")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCidade(getValorConsultaFuncionario(), 0, true, true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("CPF")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCPF(getValorConsultaFuncionario(), "FU", 0, true, true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("cargo")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCargo(getValorConsultaFuncionario(), 0, true, true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("departamento")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNomeDepartamento(getValorConsultaFuncionario(), "FU", 0, true, true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("unidadeEnsino")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorUnidadeEnsino(getValorConsultaFuncionario(), "FU", 0, true, true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaFuncionario(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaFuncionario(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public boolean isApresentarCampoCargoFuncionarioSecundario() {
		return Uteis.isAtributoPreenchido(getBoletimAcademicoRelVO().getFuncionarioSecundarioVO());
	}

	public boolean isApresentarCampoCargoFuncionarioPrincipal() {
		return Uteis.isAtributoPreenchido(getBoletimAcademicoRelVO().getFuncionarioPrincipalVO());
	}

	public void limparDadosFuncionarioPrincipal() {
		removerObjetoMemoria(getBoletimAcademicoRelVO().getFuncionarioPrincipalVO());
		getBoletimAcademicoRelVO().setFuncionarioPrincipalVO(new FuncionarioVO());
	}

	public void limparDadosFuncionarioSecundario() {
		removerObjetoMemoria(getBoletimAcademicoRelVO().getFuncionarioSecundarioVO());
		getBoletimAcademicoRelVO().setFuncionarioSecundarioVO(new FuncionarioVO());
	}
	
	public void isPermiteConfiguracaoSeiGsuite() {
		try {
			setExisteConfiguracaoSeiGsuite(getFacadeFactory().getConfiguracaoSeiGsuiteFacade().consultarSeExisteConfiguracaoSeiGsuitePadrao(getUsuarioLogadoClone()));			
		} catch (Exception e) {
			setExisteConfiguracaoSeiGsuite(false);
		}
	}
	
	public boolean isExisteConfiguracaoSeiGsuite() {
		return existeConfiguracaoSeiGsuite;
	}

	public void setExisteConfiguracaoSeiGsuite(boolean existeConfiguracaoSeiGsuite) {
		this.existeConfiguracaoSeiGsuite = existeConfiguracaoSeiGsuite;
	}

	public BoletimAcademicoRelVO getBoletimAcademicoRelVO() {
		if (boletimAcademicoRelVO == null) {
			boletimAcademicoRelVO = new BoletimAcademicoRelVO();
		}
		return boletimAcademicoRelVO;
	}

	public void setBoletimAcademicoRelVO(BoletimAcademicoRelVO boletimAcademicoRelVO) {
		this.boletimAcademicoRelVO = boletimAcademicoRelVO;
	}

	public List<SelectItem> getSelectItemsCargoFuncionarioPrincipal() {
		if (selectItemsCargoFuncionarioPrincipal == null) {
			selectItemsCargoFuncionarioPrincipal = new ArrayList<SelectItem>(0);
		}
		return selectItemsCargoFuncionarioPrincipal;
	}

	public void setSelectItemsCargoFuncionarioPrincipal(List<SelectItem> selectItemsCargoFuncionarioPrincipal) {
		this.selectItemsCargoFuncionarioPrincipal = selectItemsCargoFuncionarioPrincipal;
	}

	public List<SelectItem> getSelectItemsCargoFuncionarioSecundario() {
		if (selectItemsCargoFuncionarioSecundario == null) {
			selectItemsCargoFuncionarioSecundario = new ArrayList<SelectItem>(0);
		}
		return selectItemsCargoFuncionarioSecundario;
	}

	public void setSelectItemsCargoFuncionarioSecundario(List<SelectItem> selectItemsCargoFuncionarioSecundario) {
		this.selectItemsCargoFuncionarioSecundario = selectItemsCargoFuncionarioSecundario;
	}

	public List<FuncionarioVO> getListaConsultaFuncionario() {
		if (listaConsultaFuncionario == null) {
			listaConsultaFuncionario = new ArrayList<FuncionarioVO>(0);
		}
		return listaConsultaFuncionario;
	}

	public void setListaConsultaFuncionario(List<FuncionarioVO> listaConsultaFuncionario) {
		this.listaConsultaFuncionario = listaConsultaFuncionario;
	}

	public String getValorConsultaFuncionario() {
		if (valorConsultaFuncionario == null) {
			valorConsultaFuncionario = "";
		}
		return valorConsultaFuncionario;
	}

	public void setValorConsultaFuncionario(String valorConsultaFuncionario) {
		this.valorConsultaFuncionario = valorConsultaFuncionario;
	}

	public String getCampoConsultaFuncionario() {
		if (campoConsultaFuncionario == null) {
			campoConsultaFuncionario = "";
		}
		return campoConsultaFuncionario;
	}

	public void setCampoConsultaFuncionario(String campoConsultaFuncionario) {
		this.campoConsultaFuncionario = campoConsultaFuncionario;
	}

	public Boolean getApresentarDisciplinaComposta() {
		if (apresentarDisciplinaComposta == null) {
			apresentarDisciplinaComposta = false;
		}
		return apresentarDisciplinaComposta;
	}

	public void setApresentarDisciplinaComposta(Boolean apresentarDisciplinaComposta) {
		this.apresentarDisciplinaComposta = apresentarDisciplinaComposta;
	}
	
	public List<SelectItem> getListaSelectItemSituacaoMilitar() {
		if (listaSelectItemSituacaoMilitar == null) {
			listaSelectItemSituacaoMilitar = new ArrayList<SelectItem>(0);
			listaSelectItemSituacaoMilitar.add(new SelectItem("", ""));
			for (SituacaoMilitarEnum obj : SituacaoMilitarEnum.values()) {
				listaSelectItemSituacaoMilitar.add(new SelectItem(obj, obj.getValorApresentar()));
			}
		}
		return listaSelectItemSituacaoMilitar;
	}
	

	public List getDocumetacaoMatriculaVOS() {
		if (documetacaoMatriculaVOS == null) {
			documetacaoMatriculaVOS = new ArrayList(0);
		}
		return documetacaoMatriculaVOS;
	}

	public void setDocumetacaoMatriculaVOS(List documetacaoMatriculaVOS) {
		this.documetacaoMatriculaVOS = documetacaoMatriculaVOS;
	}
	
	public String getDocumentoEntrege() {
		if (documentoEntrege == null) {
			documentoEntrege = "";
		}
		return documentoEntrege;
	}

	public void setDocumentoEntrege(String documentoEntrege) {
		this.documentoEntrege = documentoEntrege;
	}

	public List<RegistroAtividadeComplementarMatriculaVO> getListaRegistroAtividadeComplementarMatriculaVOs() {
		if (listaRegistroAtividadeComplementarMatriculaVOs == null) {
			listaRegistroAtividadeComplementarMatriculaVOs = new ArrayList<RegistroAtividadeComplementarMatriculaVO>(0);
		}
		return listaRegistroAtividadeComplementarMatriculaVOs;
	}

	public void setListaRegistroAtividadeComplementarMatriculaVOs(List<RegistroAtividadeComplementarMatriculaVO> listaRegistroAtividadeComplementarMatriculaVOs) {
		this.listaRegistroAtividadeComplementarMatriculaVOs = listaRegistroAtividadeComplementarMatriculaVOs;
	}
	
	public void atualizarListaDocumentosMatriculaPeloCurso() throws Exception {
		try {
			setDocumetacaoMatriculaVOS(getFacadeFactory().getDocumetacaoMatriculaFacade().consultarDocumetacaoMatriculas(getMatriculaVO().getMatricula(), Uteis.NIVELMONTARDADOS_TODOS, true, getUsuarioLogado()));
			Iterator i = getDocumetacaoMatriculaVOS().iterator();
			while (i.hasNext()) {
				DocumetacaoMatriculaVO objExistente = (DocumetacaoMatriculaVO) i.next();
				if (objExistente.getDocumentoEntregue() == true) {
					setDocumentoEntrege("SIM");
				} else {
					setDocumentoEntrege("NÂO");
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void montarListaAtividadeComplementarMatriculaVO(){
		try {
			setListaRegistroAtividadeComplementarMatriculaVOs(getFacadeFactory().getAcompanhamentoAtividadeComplementarFacade().consultar(this.getMatriculaVO().getCurso().getCodigo(), 0, "", "", "", this.getMatriculaVO().getMatricula(), 0, null, false, this.getMatriculaVO().getGradeCurricularAtual().getCodigo(), getControleConsultaOtimizado(), null, null, this.getUsuarioLogado()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void consultarPorMatricula() {
		try {
			getListaConsultaRegistroAtividadeComplementarMatriculaVOs().clear();
			setRegistroAtividadeComplementarMatricula((RegistroAtividadeComplementarMatriculaVO) context().getExternalContext().getRequestMap().get("registroAtividadeComplementarMatriculaVO"));
			this.setListaConsultaRegistroAtividadeComplementarMatriculaVOs(getFacadeFactory().getAcompanhamentoAtividadeComplementarFacade().consultarPorMatricula(getMatriculaVO().getMatricula(), false, this.getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void consultarListaRegistroAtividadeComplementarMatrilculaVOsAluno() {
		try {
			setRegistroAtividadeComplementarMatricula((RegistroAtividadeComplementarMatriculaVO) context().getExternalContext().getRequestMap().get("registroAtividadeComplementarMatriculaVO"));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public RegistroAtividadeComplementarMatriculaVO getRegistroAtividadeComplementarMatricula() {
		if (registroAtividadeComplementarMatricula == null) {
			registroAtividadeComplementarMatricula = new RegistroAtividadeComplementarMatriculaVO();
		}
		return registroAtividadeComplementarMatricula;
	}

	public void setRegistroAtividadeComplementarMatricula(RegistroAtividadeComplementarMatriculaVO registroAtividadeComplementarMatricula) {
		this.registroAtividadeComplementarMatricula = registroAtividadeComplementarMatricula;
	}

	public List<RegistroAtividadeComplementarMatriculaVO> getListaConsultaRegistroAtividadeComplementarMatriculaVOs() {
		if (listaConsultaRegistroAtividadeComplementarMatriculaVOs == null) {
			listaConsultaRegistroAtividadeComplementarMatriculaVOs = new ArrayList<RegistroAtividadeComplementarMatriculaVO>(0);
		}
		return listaConsultaRegistroAtividadeComplementarMatriculaVOs;
	}

	public void setListaConsultaRegistroAtividadeComplementarMatriculaVOs(List<RegistroAtividadeComplementarMatriculaVO> listaConsultaRegistroAtividadeComplementarMatriculaVOs) {
		this.listaConsultaRegistroAtividadeComplementarMatriculaVOs = listaConsultaRegistroAtividadeComplementarMatriculaVOs;
	}
	
	public Boolean getApresentarIniciarMatricula() {		
		return !(getApresentarMatriculaCRM() || getMatriculaDiretaAgendaCRM() || getPessoaVO().getCodigo().intValue() == 0);
	}	
	public String getCampoConsultaFiador() {
		if(campoConsultaFiador == null){
			campoConsultaFiador = "";
		}
		return campoConsultaFiador;
	}

	public void setCampoConsultaFiador(String campoConsultaFiador) {
		this.campoConsultaFiador = campoConsultaFiador;
	}

	public String getValorConsultaFiador() {
		if(valorConsultaFiador == null){
			valorConsultaFiador = "";
		}
		return valorConsultaFiador;
	}

	public void setValorConsultaFiador(String valorConsultaFiador) {
		this.valorConsultaFiador = valorConsultaFiador;
	}

	public List getListaConsultaFiador() {
		if(listaConsultaFiador == null){
			listaConsultaFiador = new ArrayList<>(0);
		}
		return listaConsultaFiador;
	}

	public void setListaConsultaFiador(List listaConsultaFiador) {
		this.listaConsultaFiador = listaConsultaFiador;
	}
		
 

	public List<InscricaoVO> getInscricaoVOs() {
		if (inscricaoVOs == null) {
			inscricaoVOs = new ArrayList<InscricaoVO>(0);
		}
		
		return inscricaoVOs;
	}

	public void setInscricaoVOs(List<InscricaoVO> inscricaoVOs) {
		this.inscricaoVOs = inscricaoVOs;
	}
	
	public void executarMontagemInscricaoVOs() {
		try {
			setInscricaoVOs(getFacadeFactory().getInscricaoFacade().executarMontagemInscricaoVOs(getPessoaVO().getCodigo(), false, getUsuarioLogado()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}


	public Boolean getPermi() {
		if (getApresentarMatriculaCRM() || getMatriculaDiretaAgendaCRM()) {
			return false;
		}
		if (getPessoaVO().getCodigo().intValue() == 0) {
			return false;
		}
		return true;
	}

	public Boolean getPermiteMatricularAluno() {
		if (permiteMatricularAluno == null) {
			permiteMatricularAluno = Boolean.FALSE;
		}
		return permiteMatricularAluno;
	}

	public void setPermiteMatricularAluno(Boolean permiteMatricularAluno) {
		this.permiteMatricularAluno = permiteMatricularAluno;
	}
	
	public boolean getApresentarIniciarMatricula2() {
		if (!getPermiteMatricularAluno()) {
			return false;
		}
		if (getApresentarMatriculaCRM() || getMatriculaDiretaAgendaCRM()) {
			return true;
		} else {
			return false;
		}
	}
	
	@PostConstruct
	public void realizarAlteracaoDadosAlunoVindoDaTelaDeMatriculaRenovacao() {
		if (context().getExternalContext().getSessionMap().get("aluno") != null) {
			setPessoaVO((PessoaVO) context().getExternalContext().getSessionMap().get("aluno"));
			setValidaCamposEnadeCenso((Boolean) context().getExternalContext().getSessionMap().get("validaCamposEnadeCenso"));
			getPessoaVO().setFiliacaoVOs(new ArrayList<FiliacaoVO>(0));
			getPessoaVO().setFormacaoAcademicaVOs(new ArrayList<FormacaoAcademicaVO>(0));
			try {
				getFacadeFactory().getPessoaFacade().carregarDados(getPessoaVO(), getUsuarioLogado());
					
			getPessoaVO().setNovoObj(Boolean.FALSE);			
			inicializarListasSelectItemTodosComboBox();
			setFiliacaoVO(new FiliacaoVO());
			setConsultarPessoa(Boolean.FALSE);
			setFormacaoAcademicaVO(new FormacaoAcademicaVO());
			setDadosComerciaisVO(new DadosComerciaisVO());
			setFormacaoExtraCurricularVO(new FormacaoExtraCurricularVO());
			setImportarDadosPessoa(false);
			inicializarDadosFotoUsuario();
			if (getPessoaVO().getQtdFilhos() > 0) {
				getPessoaVO().setPossuiFilho(true);
			}
			verificarPermissaoPodeMatricula();	
			context().getExternalContext().getSessionMap().remove("aluno");
			context().getExternalContext().getSessionMap().remove("validaCamposEnadeCenso");
			setImportarDadosPessoa(Boolean.FALSE);
			setConsultarPessoa(Boolean.FALSE);
			setHashPessoaInicial(hashPessoa(clonarPessoa(getPessoaVO())));
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			}	
		}
	}
	
	public void inicializarBooleanoFoto() {
		setRemoverFoto((Boolean) false);
		setExibirUpload(true);
		setExibirBotao(false);
	}

	public void verificarBloqueioMatricula() {
		try {
			MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matricula");
			setMatriculaVO(obj);
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}		
	}
	
	public Boolean getApresentarCampoAssinatura() {
		if (apresentarCampoAssinatura == null) {
			apresentarCampoAssinatura = false;
		}
		return apresentarCampoAssinatura;
	}

	public void setApresentarCampoAssinatura(Boolean apresentarCampoAssinatura) {
		this.apresentarCampoAssinatura = apresentarCampoAssinatura;
	}

	public Boolean getApresentarCampoAssinaturaResponsavel() {
		if (apresentarCampoAssinaturaResponsavel == null) {
			apresentarCampoAssinaturaResponsavel = false;
		}
		return apresentarCampoAssinaturaResponsavel;
	}

	public void setApresentarCampoAssinaturaResponsavel(Boolean apresentarCampoAssinaturaResponsavel) {
		this.apresentarCampoAssinaturaResponsavel = apresentarCampoAssinaturaResponsavel;
	}

	public Boolean getValidaCamposEnadeCenso() {
		if(validaCamposEnadeCenso == null) {
			validaCamposEnadeCenso =  false;
		}
		return validaCamposEnadeCenso;
	}

	public void setValidaCamposEnadeCenso(Boolean validaCamposEnadeCenso) {
		this.validaCamposEnadeCenso = validaCamposEnadeCenso;
	}
	
	public String getUserNameLiberarFuncionalidade() {
		if (userNameLiberarFuncionalidade == null) {
			userNameLiberarFuncionalidade = "";
		}
		return userNameLiberarFuncionalidade;
	}

	public void setUserNameLiberarFuncionalidade(String userNameLiberarValorAcimaPrevisto) {
		this.userNameLiberarFuncionalidade = userNameLiberarValorAcimaPrevisto;
	}

	public String getSenhaLiberarFuncionalidade() {
		if (senhaLiberarFuncionalidade == null) {
			senhaLiberarFuncionalidade = "";
		}
		return senhaLiberarFuncionalidade;
	}

	public void setSenhaLiberarFuncionalidade(String senhaLiberarValorAcimaPrevisto) {
		this.senhaLiberarFuncionalidade = senhaLiberarValorAcimaPrevisto;
	}
	
	public boolean isLiberarEdicaoAlunoSomenteComSenha() {
		return liberarEdicaoAlunoSomenteComSenha;
	}
	
	public void setLiberarEdicaoAlunoSomenteComSenha(boolean liberarEdicaoAlunoSomenteComSenha) {
		this.liberarEdicaoAlunoSomenteComSenha = liberarEdicaoAlunoSomenteComSenha;
	}
	
	public void verificarPermissaoLiberarEdicaoAlunoSomenteComSenha() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoAcademicoEnum.LIBERAR_EDICAO_ALUNO_SOMENTE_COM_SENHA, getUsuarioLogadoClone());
			setLiberarEdicaoAlunoSomenteComSenha(true);
		} catch (Exception e) {
			setLiberarEdicaoAlunoSomenteComSenha(false);
		}
	}
	
	public void persistirLiberacaoAlunoSomenteComSenha() {
		try {
			UsuarioVO usuarioVerif = ControleAcesso.verificarLoginUsuario(this.getUserNameLiberarFuncionalidade(), this.getSenhaLiberarFuncionalidade(), true, Uteis.NIVELMONTARDADOS_TODOS);
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoAcademicoEnum.LIBERAR_EDICAO_ALUNO_SOMENTE_COM_SENHA, usuarioVerif);
			getFacadeFactory().getOperacaoFuncionalidadeFacade().incluir(getFacadeFactory().getOperacaoFuncionalidadeFacade().executarGeracaoOperacaoFuncionalidade(OrigemOperacaoFuncionalidadeEnum.ALUNO, getPessoaVO().getCodigo().toString(), OperacaoFuncionalidadeEnum.LIBERAR_EDICAO_COM_SENHA, usuarioVerif, ""));
			setLiberarEdicaoAlunoSomenteComSenha(false);
			setMensagemID("Dados Pronto Para Edição" , Uteis.SUCESSO, true);
		} catch (Exception e) {
			setLiberarEdicaoAlunoSomenteComSenha(true);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> getListaSelectItemTipoMidiaCaptacao() {
		if(listaSelectItemTipoMidiaCaptacao == null) {
			try {
				listaSelectItemTipoMidiaCaptacao = new ArrayList<SelectItem>();
				List<TipoMidiaCaptacaoVO> tiposMidiaCaptacao = new ArrayList<TipoMidiaCaptacaoVO>();
				
				tiposMidiaCaptacao = getFacadeFactory().getTipoMidiaCaptacaoFacade().consultarPorNomeMidia("", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				
				listaSelectItemTipoMidiaCaptacao.add(new SelectItem("", ""));
				
				for(TipoMidiaCaptacaoVO tipoMidiaCaptacaoVO : tiposMidiaCaptacao) {
					listaSelectItemTipoMidiaCaptacao.add(new SelectItem(tipoMidiaCaptacaoVO.getCodigo(), tipoMidiaCaptacaoVO.getNomeMidia()));
				}
				
			}catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
		}
		
		return listaSelectItemTipoMidiaCaptacao;
	}

	public void setListaSelectItemTipoMidiaCaptacao(List<SelectItem> listaSelectItemTipoMidiaCaptacao) {
		this.listaSelectItemTipoMidiaCaptacao = listaSelectItemTipoMidiaCaptacao;
	}
	
	public void selecionarTipoMidiaCaptacao() {
		try {
			
			
//			DisciplinaVO obj = (DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinaItens");
//			setDisciplinaVO(obj);
			// getPlanoEnsinoVO().setDisciplina(null);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void setarNomeBatismo() {
		if(!Uteis.isAtributoPreenchido(getPessoaVO().getNomeBatismo())) {
			getPessoaVO().setNomeBatismo(getPessoaVO().getNome());
		}
	}
	
	public void setarNomeSocial() {
		if(!Uteis.isAtributoPreenchido(getPessoaVO().getNome())) {
			getPessoaVO().setNome(getPessoaVO().getNomeBatismo());
		}
	}
	
	public void setarNomeBatismoFiliacao() {
		if(!Uteis.isAtributoPreenchido(getFiliacaoVO().getPais().getNomeBatismo())) {
			getFiliacaoVO().getPais().setNomeBatismo(getFiliacaoVO().getPais().getNome());
		}
	}
	
	public void setarNomeSocialFiliacao() {
		if(!Uteis.isAtributoPreenchido(getFiliacaoVO().getPais().getNome())) {
			getFiliacaoVO().getPais().setNome(getFiliacaoVO().getPais().getNomeBatismo());
		}
	}

	public Boolean getTornarCampoMidiaCaptacaoObrigatorio() {
		if(tornarCampoMidiaCaptacaoObrigatorio == null) {
			tornarCampoMidiaCaptacaoObrigatorio = Boolean.FALSE;
		}
		return tornarCampoMidiaCaptacaoObrigatorio;
	}

	public void setTornarCampoMidiaCaptacaoObrigatorio(Boolean tornarCampoMidiaCaptacaoObrigatorio) {
		this.tornarCampoMidiaCaptacaoObrigatorio = tornarCampoMidiaCaptacaoObrigatorio;
	}
	
	public void verificarTornarCampoMidiaCaptacaoObrigatorio() throws Exception {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("AlunoTornarCampoTipoMidiaCaptacaoObrigatorio", getUsuarioLogado());
			setTornarCampoMidiaCaptacaoObrigatorio(Boolean.TRUE);
		} catch (Exception e) {
			setTornarCampoMidiaCaptacaoObrigatorio(Boolean.FALSE);
		}
	}
	
	public void ativarPessoaEmailInstitucional() {
		try {
			if (getPessoaEmailInstitucionalVO().getStatusAtivoInativoEnum().equals(StatusAtivoInativoEnum.INATIVO)) {
				getPessoaEmailInstitucionalVO().setStatusAtivoInativoEnum(StatusAtivoInativoEnum.ATIVO);
				setMensagemID("E-mail Institucional ATIVADO", Uteis.SUCESSO, true);
			} 
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void inativarPessoaEmailInstitucional() {
		try {
			if (getPessoaEmailInstitucionalVO().getStatusAtivoInativoEnum().equals(StatusAtivoInativoEnum.ATIVO)) {
				getPessoaEmailInstitucionalVO().setStatusAtivoInativoEnum(StatusAtivoInativoEnum.INATIVO);
				setMensagemID("E-mail Institucional INATIVADO", Uteis.ALERTA, true);
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public Boolean getLiberarEdicaoRegistroAcademicoAluno() {
		if(liberarEdicaoRegistroAcademicoAluno == null ) {
			liberarEdicaoRegistroAcademicoAluno = Boolean.FALSE;
		}
		return liberarEdicaoRegistroAcademicoAluno;
	}

	public void setLiberarEdicaoRegistroAcademicoAluno(Boolean liberarEdicaoRegistroAcademicoAluno) {
		this.liberarEdicaoRegistroAcademicoAluno = liberarEdicaoRegistroAcademicoAluno;
	}
	
	
	public void verificarPermissaoLiberarEdicaoRegistroAcademicoAluno() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoAcademicoEnum.LIBERAR_EDICAO_REGISTRO_ACADEMICO_ALUNO, getUsuarioLogadoClone());
			setLiberarEdicaoRegistroAcademicoAluno(true);
		} catch (Exception e) {
			setLiberarEdicaoRegistroAcademicoAluno(false);
		}
	}
	
	public List<SelectItem> getListaSelectItemTitulo() {
		if (listaSelectItemTitulo == null) {
			listaSelectItemTitulo = new ArrayList<>(0);
		}
		return listaSelectItemTitulo;
	}

	public void setListaSelectItemTitulo(List<SelectItem> listaSelectItemTitulo) {
		this.listaSelectItemTitulo = listaSelectItemTitulo;
	}

	public void montartListaSelectItemTitulo() {
		setListaSelectItemTitulo(NivelFormacaoAcademica.getListaSelectItemTituloPorValorNivelFormacaoAcademica(getFormacaoAcademicaVO().getEscolaridade()));
	}
	
	public boolean getApresentarListaSelectItemTitulo() {
		return getListaSelectItemTitulo().size() > 1;
	}
	
	public void selecionarPessoaEmailInstitucional() {
		try {
			PessoaEmailInstitucionalVO obj = (PessoaEmailInstitucionalVO) context().getExternalContext().getRequestMap().get("pessoaEmailInstitucionalItem");
			setPessoaEmailInstitucionalVO(obj);
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public List<SelectItem> getListaSelectItemTranstornosNeurodivergentes() throws Exception {
		return UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoTranstornosNeurodivergentes.class);
	}

	public int getHashPessoaInicial() {
		return hashPessoaInicial;
	}

	public void setHashPessoaInicial(int hashPessoaInicial) {
		this.hashPessoaInicial = hashPessoaInicial;
	}
	
}
