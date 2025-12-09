package controle.administrativo;

import java.io.File;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.SerializationUtils;
import org.richfaces.event.DataScrollEvent;
import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import controle.arquitetura.SuperControle.MSG_TELA;
import controle.eventos.EventoEmprestimoCargoFuncionarioControle;
import controle.eventos.EventoFixoCargoFuncionarioControle;
import controle.recursoshumanos.EventoValeTransporteFuncionarioCargoControle;
import controle.recursoshumanos.MarcacaoFeriasControle;
import controle.recursoshumanos.PeriodoAquisitivoFeriasControle;
import controle.recursoshumanos.SalarioCompostoControle;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.DisciplinasInteresseVO;
import negocio.comuns.academico.DisponibilidadeHorarioVO;
import negocio.comuns.academico.DocumentoAssinadoVO;
import negocio.comuns.academico.DocumetacaoMatriculaVO;
import negocio.comuns.academico.FiliacaoVO;
import negocio.comuns.academico.FuncionarioDependenteVO;
import negocio.comuns.academico.HorarioProfessorDiaItemVO;
import negocio.comuns.academico.HorarioProfessorDiaVO;
import negocio.comuns.academico.HorarioProfessorTurnoVO;
import negocio.comuns.academico.HorarioProfessorVO;
import negocio.comuns.academico.LayoutPadraoVO;
import negocio.comuns.academico.QuadroHorarioVO;
import negocio.comuns.academico.TipoDocumentoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.academico.enumeradores.GraduacaoPosGraduacaoEnum;
import negocio.comuns.administrativo.CargoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.ConfiguracaoSeiGsuiteUnidadeEnsinoVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.DepartamentoVO.EnumCampoConsultaDepartamento;
import negocio.comuns.administrativo.FormacaoAcademicaVO;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.PessoaPreInscricaoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.FormaContratacaoFuncionarioEnum;
import negocio.comuns.administrativo.enumeradores.SituacaoFuncionarioEnum;
import negocio.comuns.administrativo.enumeradores.TipoRecebimentoEnum;
import negocio.comuns.arquitetura.PerfilAcessoVO;
import negocio.comuns.arquitetura.UsuarioPerfilAcessoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.ServidorArquivoOnlineEnum;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.DocumetacaoPessoaVO;
import negocio.comuns.basico.PaizVO;
import negocio.comuns.basico.PessoaEmailInstitucionalVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;
import negocio.comuns.financeiro.ContaPagarVO;
import negocio.comuns.financeiro.PerfilEconomicoVO;
import negocio.comuns.gsuite.PessoaGsuiteVO;
import negocio.comuns.pesquisa.AreaConhecimentoVO;
import negocio.comuns.recursoshumanos.EventoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.FaixaSalarialVO;
import negocio.comuns.recursoshumanos.FormulaFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.HistoricoDependentesVO;
import negocio.comuns.recursoshumanos.HistoricoFuncaoVO;
import negocio.comuns.recursoshumanos.HistoricoSalarialVO;
import negocio.comuns.recursoshumanos.HistoricoSecaoVO;
import negocio.comuns.recursoshumanos.NivelSalarialVO;
import negocio.comuns.recursoshumanos.ProgressaoSalarialItemVO;
import negocio.comuns.recursoshumanos.SecaoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.SecaoFolhaPagamentoVO.EnumCampoConsultaSecaoFolhaPagamento;
import negocio.comuns.recursoshumanos.SindicatoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.NivelFormacaoAcademica;
import negocio.comuns.utilitarias.dominios.OrigemArquivo;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.SituacaoFinanceira;
import negocio.comuns.utilitarias.dominios.TipoDeficiencia;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.academico.HorarioProfessorDia;
import negocio.facade.jdbc.administrativo.FormacaoAcademica;
import negocio.facade.jdbc.administrativo.Funcionario;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.basico.PessoaInterfaceFacade;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.academico.CronogramaDeAulasRelVO;
import relatorio.negocio.comuns.arquitetura.SuperParametroRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import negocio.comuns.utilitarias.dominios.SituacaoArquivo;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das
 * páginas funcionarioForm.xhtml funcionarioCons.xhtml) com as funcionalidades
 * da classe <code>Funcionario</code>. Implemtação da camada controle (Backing
 * Bean).
 * 
 * @see SuperControle
 * @see Funcionario
 * @see FuncionarioVO
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
@Controller("FuncionarioControle")
@Scope("viewScope")
@Lazy
public class FuncionarioControle extends SuperControleRelatorio implements Serializable {

	private static final long serialVersionUID = -7201192553420985895L;
	private FuncionarioVO funcionarioVO;
	private FuncionarioVO funcionarioUnificarVO;
	private PessoaVO pessoaVO;
	private PessoaVO pessoaExistente;
	private QuadroHorarioVO quadroHorario;
	protected FuncionarioCargoVO funcionarioCargoVO;
	private DisciplinasInteresseVO disciplinasInteresseVO;
	private DisponibilidadeHorarioVO disponibilidadeHorarioVO;
	protected List<SelectItem> listaSelectItemUnidadeEnsino;
	protected List<SelectItem> listaSelectItemDepartamento;
	protected List<SelectItem> listaSelectItemCargo;
	protected List<SelectItem> listaSelectItemTurno;
	private List<SelectItem> listaSelectItemNaturalidade;
	private List<SelectItem> listaSelectItemNacionalidade;
	protected List<SelectItem> listaSelectItemAreaConhecimento;
	protected List<SelectItem> listaSelectItemPerfilEconomico;
	private String pessoa_Erro;
	private String disciplina_Erro;
	private String panelDirecionarUsuario;
	protected List listaConsultaDisciplina;
	private String campoConsultaDisciplina;
	private String valorConsultaDisciplina;
	private String diaSemana = "02";
	protected String campoConsultaCidade;
	protected String valorConsultaCidade;
	protected List listaConsultaCidade;
	private String campoConsultaNaturalidade;
	private String valorConsultaNaturalidade;
	private List listaConsultaNaturalidade;
	// Referente a pessoa
	private FormacaoAcademicaVO formacaoAcademicaVO;
	private PessoaPreInscricaoCursoVO pessoaPreInscricaoCursoVO;
	private FiliacaoVO filiacaoVO;
	private String turno_Erro;
	public Boolean verificarCpf;
	public Boolean consultarPessoa;
	public Boolean importarDadosPessoa;
	public Boolean editarDados;
	private Integer codigoTurno;
	private Boolean horarioSimples;
	private boolean controladorTela;

	private boolean cpfInvalido;
	private boolean naoFuncionario;
	protected HorarioProfessorDiaVO horarioProfessorDiaVO;
	protected List<HorarioProfessorDiaItemVO> horarioProfessorGraduacao;
	protected List<HorarioProfessorDiaItemVO> horarioProfessorPosGraduacao;
	private Integer index;
	// Visão professor
	private Date dataInicio;
	private Date dataFim;
	private List<SelectItem> listaSelectItemTurma;
	private List<SelectItem> listaSelectItemProfessoresTurma;
	private String tipoConsulta;
	private String valorConsultaProfessor;
	private String campoConsultaProfessor;
	private DisciplinaVO disciplinaVO;
	private List listaContaPagar;
	private Double totalPagar;
	private Double totalPago;
	private Date dataInicioContaPagar;
	private Date dataFimContaPagar;
	private String campoConsultaCurso;
	private String valorConsultaCurso;
	private List listaConsultaCurso;
	private UnidadeEnsinoCursoVO unidadeEnsinoCursoVO;
	private List<SelectItem> tipoConsultaComboCurso;
	private String campoConsultaTurma;
	private String valorConsultaTurma;
	private List listaConsultaTurma;
	private TurmaVO turmaVO;
	private List<SelectItem> tipoConsultaComboTurma;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private List<DocumetacaoPessoaVO> listaDocumentacaoPessoaProfessor;
	private String campoConsultaFiliacaoCidade;
	private String valorConsultaFiliacaoCidade;
	private List listaConsultaFiliacaoCidade;
	private List listaConsultaFuncionarioUnificar;
	private String valorConsultaFuncionarioUnificar;
	private String campoConsultaFuncionarioUnificar;
	private Boolean permitirUnificarFuncionariosDuplicados;
	private Boolean exibirFormCriarUsuario;

	// Atributos usado nos Meus horários Visão Professor
	private String layout;
	private List<HorarioProfessorTurnoVO> horarioProfessorTurnoVOs;
	private Boolean calendarioMensal;
	private Date dataBaseHorarioAula;
	private Boolean ocultarHorarioLivre;

	protected List<SelectItem> listaSelectItemSituacaoFuncionario;
	protected List<SelectItem> listaSelectItemFormaContratacao;
	protected List<SelectItem> listaSelectItemTipoRecebimento;

	public Boolean permiteAdicionarCargo;
	public Boolean permiteAlterarSituacaoCargo;
	public Boolean permiteCadastrarEvento;
	public Boolean permiteAcessarFichaFinanceira;
	public Boolean permiteAcessarMarcacaoFerias;
	public Boolean permiteAcessarPeriodoAquisitivo;
	public Boolean permiteAcessarEventoEmprestimo;
	public Boolean permiteAcessarSalarioComposto;
	public Boolean permiteAcessarValeTransporte;

	protected FuncionarioDependenteVO dependenteVO;
	public List listaSelectItemGrauParentesco;
	public List listaSelectItemSexoDependente;
	public List listaSelectItemEstadoCivilDependente;

	private String campoConsultaFormula;
	private String valorConsultaFormula;
	protected List listaConsultaFormula;

	private String campoConsultaSecaoFolhaPagamento;
	private String valorConsultaSecaoFolhaPagamento;
	protected List listaConsultaSecaoFolhaPagamento;

	private String campoConsultaSindicato;
	private String valorConsultaSindicato;
	protected List<SindicatoVO> listaConsultaSindicato;

	private FuncionarioCargoVO funcionarioCargoVOEditado;
	private FuncionarioDependenteVO funcionarioDependenteVOEditado;

	private List<SelectItem> listaSelectItemNivelSalarial;
	private List<SelectItem> listaSelectItemFaixaSalarial;

	private String campoConsultaDepartamento;
	private String valorConsultaDepartamento;
	protected List<DepartamentoVO> listaConsultaDepartamento;

	private List<ProgressaoSalarialItemVO> listaProgressaoSalarialItemVOs;

	private String campoConsultaEvento;
	private String valorConsultaEvento;
	private List<EventoFolhaPagamentoVO> listaEventosFolhaPagamento;
	private boolean existeConfiguracaoSeiGsuite = false;
	private PessoaGsuiteVO pessoaGsuiteVO;
	private PessoaEmailInstitucionalVO pessoaEmailInstitucionalVO;

	private FuncionarioCargoVO funcionarioCargoHistorico;
	private List<HistoricoFuncaoVO> historicoFuncaoVOs;
	private List<HistoricoSalarialVO> historicoSalarialVOs;
	private List<HistoricoSecaoVO> historicoSecaoVOs;
	private List<HistoricoDependentesVO> historicoDependentesVOs;
	private String matriculaApresentarExclusaoFormacaoAcademica;

	private DocumetacaoPessoaVO documetacaoPessoaVO;
	private DocumetacaoPessoaVO documetacaoPessoaVOAux;
	private String nomeArquivo;
	private String abrirModalInclusaoArquivoVerso;

	private boolean abrirpopup = false;
	private String ordenacao;

	private List<SelectItem> listaTipoDocumentoUtilizarDocumentoFuncionario;
	private List<TipoDocumentoVO> listaTipoDocumento;
	private List<TipoDocumentoVO> listaTipoDocumentoAux;
	private Integer tipoDocumentoVOSelecionado;
	private Boolean assinarDocumentoDigitalmente;
	private TipoDocumentoVO tipoDocumentoRemover;
	private UsuarioPerfilAcessoVO usuarioPerfilAcessoVO;
	private List<SelectItem> listaSelectItemCodPerfilAcesso;

	public FuncionarioControle() throws Exception {
	}

	@PostConstruct
	public void init() {
		setAbaAtiva("tabDadosPessoais");
		String cd = (String) ((HttpServletRequest) context().getExternalContext().getRequest()).getParameter("cd");
		FuncionarioVO obj = null;
		if (cd != null) {
			try {
				obj = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCodigoPessoa(Integer.valueOf(cd),
						false, getUsuarioLogado());
				context().getExternalContext().getRequestMap().put("funcionarioItem", obj);
				editar();
			} catch (Exception e) {
			}
		} else if (getNomeTelaAtual().contains("meusHorariosProfessor")
				&& getUsuarioLogado().getIsApresentarVisaoProfessor()) {
			editarDadosVisaoProfessor();
		} else if (getNomeTelaAtual().contains("horariosProfessorVisaoCoordenador.xhtml")
				&& getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
			editarDadosVisaoCoordenador();
		} else {
			montarListaSelectItemFormaContratacao();
		}
		// Não retirar daqui pois os mesmo são utilizados na visão do professor e devem
		// ser inicializados
		// com este valores não podendo ser inicializados no get pois os mesmos podem
		// receber valores nulos
		Date data = new Date();
		setDataInicio(Uteis.getDataPrimeiroDiaMes(data));
		setDataFim(Uteis.getDataUltimoDiaMes(data));
		data = null;
		////
		try {
			verificarLayoutPadrao();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setMensagemID("msg_entre_prmconsulta");

	}

	public String getTituloScrollFirst() {
		if (getIndex() - 1 < 1) {
			return "";
		}
		if (getQuadroHorario().getHorarioProfessorVO().getCalendarioHorarioAulaVOs().size() >= getIndex()) {
			return getQuadroHorario().getHorarioProfessorVO().getCalendarioHorarioAulaVOs().get(getIndex() - 2)
					.getTituloCalendarioAbreviado();
		}
		return "";
	}

	public String getTituloScroll() {
		if (getIndex() - 1 < 0) {
			setIndex(1);
		}
		if (getQuadroHorario().getHorarioProfessorVO().getCalendarioHorarioAulaVOs().size() >= getIndex()) {
			return getQuadroHorario().getHorarioProfessorVO().getCalendarioHorarioAulaVOs().get(getIndex() - 1)
					.getTituloCalendarioAbreviado();
		}
		return "";
	}

	public String getTituloScrollNext() {
		if (getIndex() == getQuadroHorario().getHorarioProfessorVO().getCalendarioHorarioAulaVOs().size()) {
			return "";
		}
		if (getQuadroHorario().getHorarioProfessorVO().getCalendarioHorarioAulaVOs().size() >= getIndex()) {
			return getQuadroHorario().getHorarioProfessorVO().getCalendarioHorarioAulaVOs().get(getIndex())
					.getTituloCalendarioAbreviado();
		}
		return "";
	}

	public Integer getIndex() {
		if (index == null) {
			index = 0;
		}
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe
	 * <code>Funcionario</code> para edição pelo usuário da aplicação.
	 */
	public String novo() throws Exception {
		removerObjetoMemoria(this);
		setAbaAtiva("tabDadosPessoais");
		setFuncionarioVO(new FuncionarioVO());
		setPessoaVO(new PessoaVO());
		setPessoaExistente(new PessoaVO());
		setQuadroHorario(new QuadroHorarioVO());
		setDisciplinasInteresseVO(new DisciplinasInteresseVO());
		setDisponibilidadeHorarioVO(new DisponibilidadeHorarioVO());
		inicializarListasSelectItemTodosComboBox();
		setFiliacaoVO(new FiliacaoVO());
		setFormacaoAcademicaVO(new FormacaoAcademicaVO());
		setPessoaPreInscricaoCursoVO(new PessoaPreInscricaoCursoVO());
		setFuncionarioCargoVO(new FuncionarioCargoVO());
		setDependenteVO(new FuncionarioDependenteVO());
		setConsultarPessoa(true);
		setImportarDadosPessoa(false);
		setEditarDados(false);
		setPanelDirecionarUsuario("");
		setHorarioSimples(true);
		renderizarTela();
		setMensagemID("msg_entre_dados");
		setCodigoTurno(0);
		getFuncionarioVO().setCriarUsuario(false);
		setNaoFuncionario(false);
		setCpfInvalido(false);
		setHorarioProfessorDiaVO(new HorarioProfessorDiaVO());
		inicializarDadosFotoUsuario();
		inicializarBooleanoFoto();
		isPermiteConfiguracaoSeiGsuite();
		setExibirBotaoVerso(false);
		setExibirBotao(false);
		getControleConsulta().setDataIni(Uteis.getDataPrimeiroDiaMes(new Date()));
		getControleConsulta().setDataFim(Uteis.getDataUltimoDiaMes(new Date()));
		return Uteis.getCaminhoRedirecionamentoNavegacao("funcionarioDadosPessoaisForm");
	}

	public void inicializarDadosFotoUsuario() throws Exception {
		getPessoaVO().getArquivoImagem().setCpfRequerimento(getPessoaVO().getCPF());
		setCaminhoFotoUsuario(getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(
				getPessoaVO().getArquivoImagem(), PastaBaseArquivoEnum.IMAGEM.getValue(),
				getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(), "foto_usuario.png", false));
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe
	 * <code>Funcionario</code> para alteração. O objeto desta classe é
	 * disponibilizado na session da página (request) para que o JSP correspondente
	 * possa disponibilizá-lo para edição.
	 */
	public String editar() {
		try {
			setAbaAtiva("tabDadosPessoais");
			FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItem");
			setExibirBotaoVerso(false);
			setExibirBotao(false);
			obj.setNivelMontarDados(NivelMontarDados.BASICO);
			obj = montarDadosFuncionarioVOCompleto(obj);
			obj.setFuncionarioCargoVOs(getFacadeFactory().getFuncionarioCargoFacade().consultarPorFuncionario(
					obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));

			for (FuncionarioCargoVO funcionarioCargoVO : obj.getFuncionarioCargoVOs()) {
				try {
					funcionarioCargoVO.setSecaoFolhaPagamento(Uteis.montarDadosVO(
							funcionarioCargoVO.getSecaoFolhaPagamento().getCodigo(), SecaoFolhaPagamentoVO.class,
							p -> getFacadeFactory().getSecaoFolhaPagamentoInterfaceFacade().consultarPorChavePrimaria(
									funcionarioCargoVO.getSecaoFolhaPagamento().getCodigo().longValue())));
					funcionarioCargoVO.montarCentroCusto(obj);
				} catch (Exception e) {
					setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
				}
			}
			for (PessoaEmailInstitucionalVO pessoaEmailInstitucionalVO : obj.getPessoa().getListaPessoaEmailInstitucionalVO()) {
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
			getFacadeFactory().getFuncionarioFacade().inicializarDadosEntidadesSubordinadasFuncionarioVOCompleto(obj, getUsuarioLogado());
			getFacadeFactory().getFuncionarioFacade().inicializarDadosEntidadesSubordinadasFuncionarioVOCompleto(obj,
					getUsuarioLogado());
			setDisponibilidadeHorarioVO(new DisponibilidadeHorarioVO());
			inicializarAtributosRelacionados(obj, obj.getPessoa());
			obj.setNovoObj(false);
			setFuncionarioVO(obj);
			setDependenteVO(new FuncionarioDependenteVO());
			inicializarListasSelectItemTodosComboBox();
			setDisciplinasInteresseVO(new DisciplinasInteresseVO());
			setPessoaVO(new PessoaVO());
			getPessoaVO().setCodigo(obj.getPessoa().getCodigo());
			getFacadeFactory().getPessoaFacade().carregarDados(getPessoaVO(), getUsuarioLogado());
			setPessoaExistente(new PessoaVO());
			setCodigoTurno(0);
			setFuncionarioUnificarVO(null);
			setQuadroHorario(new QuadroHorarioVO());
			setPanelDirecionarUsuario("");
			renderizarTela();
			setHorarioSimples(true);
			obj.setNovoObj(false);
			setConsultarPessoa(false);
			setImportarDadosPessoa(false);
			setMensagemID("msg_dados_editar");
			getFuncionarioVO().setCriarUsuario(false);
			setHorarioProfessorDiaVO(new HorarioProfessorDiaVO());
			setEditarDados(false);
			inicializarDadosFotoUsuario();
			getFuncionarioVO().setInformarMatricula(false);
			getControleConsulta().setDataIni(Uteis.getDataPrimeiroDiaMes(new Date()));
			getControleConsulta().setDataFim(Uteis.getDataUltimoDiaMes(new Date()));
			montarListaContaPagar();
			validarQuantidadePermissoesBotoesFuncionario();
			atualizarCargo();
			setRemoverFoto(Uteis.isAtributoPreenchido(getPessoaVO().getArquivoImagem().getNome()));
			setExibirUpload(true);
			setExibirBotao(false);
			return Uteis.getCaminhoRedirecionamentoNavegacao("funcionarioDadosPessoaisForm");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("funcionarioCons");
		}
	}

	public void preencherListaDocumentacaoPessoaProfessor() {
		try {
			List lista = new ArrayList();
			setListaDocumentacaoPessoaProfessor(getFacadeFactory().getDocumetacaoPessoaFacade()
					.consultarDocumetacaoPessoaPorPessoaProfessorOuEntregue(getFuncionarioVO().getPessoa().getCodigo(),
							Uteis.NIVELMONTARDADOS_COMBOBOX, false, getUsuarioLogado()));
			List<TipoDocumentoVO> listaDocumentosProfessor = getFacadeFactory().getTipoDeDocumentoFacade()
					.consultarUtilizadosPorFuncionarios(getFuncionarioVO().getPessoa().getCodigo(), false,
							Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			for (TipoDocumentoVO objTipoDocumento : listaDocumentosProfessor) {
				DocumetacaoPessoaVO objDocumetacaoPessoa = new DocumetacaoPessoaVO();
				objDocumetacaoPessoa.setTipoDeDocumentoVO(objTipoDocumento);
				objDocumetacaoPessoa.setUsuario(getUsuarioLogadoClone());
				objDocumetacaoPessoa.setPessoa(getFuncionarioVO().getPessoa().getCodigo());
				for (DocumetacaoPessoaVO objExistente : getListaDocumentacaoPessoaProfessor()) {
					if (objExistente.getTipoDeDocumentoVO().getCodigo().equals(objTipoDocumento.getCodigo())) {
						objDocumetacaoPessoa.setCodigo(objExistente.getCodigo());
						objDocumetacaoPessoa.setEntregue(objExistente.getEntregue());
						objDocumetacaoPessoa.setDataEntrega(objExistente.getDataEntrega());
						objDocumetacaoPessoa.setArquivoVO(objExistente.getArquivoVO());
						objDocumetacaoPessoa.setArquivoVOVerso(objExistente.getArquivoVOVerso());
						objDocumetacaoPessoa.setAssinarDigitalmente(objExistente.getAssinarDigitalmente());
					}
				}
				lista.add(objDocumetacaoPessoa);
			}
			setListaDocumentacaoPessoaProfessor(lista);
			Ordenacao.ordenarLista(getListaDocumentacaoPessoaProfessor(), "ordenacaoNomeTipoDeDocumento");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public List<DocumetacaoPessoaVO> montarListaDocumentacaoPessoaProfessorManual() {
		List<DocumetacaoPessoaVO> listaDocumentoPessoa = new ArrayList<DocumetacaoPessoaVO>();
		for (TipoDocumentoVO tipoDocumentoVO : getListaTipoDocumento()) {
			if (getListaDocumentacaoPessoaProfessor().stream()
					.filter(dp -> dp.getTipoDeDocumentoVO().getCodigo().equals(tipoDocumentoVO.getCodigo())).findFirst()
					.isPresent()) {
				continue;
			}
			DocumetacaoPessoaVO objDocumetacaoPessoa = new DocumetacaoPessoaVO();
			objDocumetacaoPessoa.setTipoDeDocumentoVO(tipoDocumentoVO);
			objDocumetacaoPessoa.setUsuario(getUsuarioLogadoClone());
			objDocumetacaoPessoa.setPessoa(getFuncionarioVO().getPessoa().getCodigo());
			objDocumetacaoPessoa.setSituacao("PE");
			objDocumetacaoPessoa.setAssinarDigitalmente(tipoDocumentoVO.getAssinarDigitalmente());
			listaDocumentoPessoa.add(objDocumetacaoPessoa);
		}
		return listaDocumentoPessoa;
	}

	public void removerTipoDocumento() {
		try {
			getFacadeFactory().getDocumetacaoPessoaFacade().removerTipoDocumento(getListaDocumentacaoPessoaProfessor(),
					getListaTipoDocumento(), getTipoDocumentoRemover(), getUsuarioLogadoClone());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void adicionarTipoDocumento() {
		try {
			getListaTipoDocumentoAux().stream().filter(td -> td.getCodigo().equals(getTipoDocumentoVOSelecionado()))
					.findFirst().ifPresent(this::adicionarTipoDocumento);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	private void adicionarTipoDocumento(TipoDocumentoVO tipoDocumentoVO) {
		tipoDocumentoVO.setAssinarDigitalmente(getAssinarDocumentoDigitalmente());
		getFacadeFactory().getFuncionarioFacade().adicionarTipoDocumento(getListaTipoDocumento(), tipoDocumentoVO);
	}

	public void consultarTipoDocumentoFuncionario() {
		try {
			List<TipoDocumentoVO> listaTipoDocumentoTmp = getFacadeFactory().getTipoDeDocumentoFacade()
					.consultarTipoDocumentoUtilizadosPorFuncionarios(false, Uteis.NIVELMONTARDADOS_COMBOBOX,
							getUsuarioLogado());
			setListaTipoDocumento(getFacadeFactory().getTipoDeDocumentoFacade().consultarTipoDocumentoFuncionarioManual(
					getFuncionarioVO().getPessoa().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX,
					getUsuarioLogado()));
			if (Uteis.isAtributoPreenchido(listaTipoDocumentoTmp)) {
				setListaTipoDocumentoAux((List<TipoDocumentoVO>) Uteis.clonar((Serializable) listaTipoDocumentoTmp));
				setListaTipoDocumentoUtilizarDocumentoFuncionario(
						UtilSelectItem.getListaSelectItem(listaTipoDocumentoTmp, "codigo", "nome", true));
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarArquivoDocumentoEntregueExclusao() {
		setDocumetacaoPessoaVO(new DocumetacaoPessoaVO());
		DocumetacaoPessoaVO obj = (DocumetacaoPessoaVO) context().getExternalContext().getRequestMap()
				.get("documentacaoPessoaProfessor");
		setDocumetacaoPessoaVO(obj);
	}

	public void upLoadArquivoFrente(FileUploadEvent uploadEvent) {
		try {
			// getFacadeFactory().getDocumetacaoMatriculaFacade().validarExtensaoArquivoFrente(uploadEvent,
			// getDocumetacaoMatriculaVO());
			if (Uteis.isAtributoPreenchido(getDocumetacaoPessoaVO())) {
				getDocumetacaoPessoaVO().getArquivoVO().setCodOrigem(getDocumetacaoPessoaVO().getCodigo());
			}
			getDocumetacaoPessoaVO().setEntregue(Boolean.TRUE);
			getDocumetacaoPessoaVO().getArquivoVO()
					.setCpfPessoaDocumentacao(getFuncionarioVO().getPessoa().getCPF().trim());
			getDocumetacaoPessoaVO().getArquivoVO().setOrigem(OrigemArquivo.DOCUMENTACAO_PROFESSOR.getValor());
			getDocumetacaoPessoaVO().getArquivoVO()
					.setServidorArquivoOnline(ServidorArquivoOnlineEnum.getEnum("APACHE"));
			getFacadeFactory().getArquivoHelper().upLoadDocumentacaoMatriculaRequerimento(uploadEvent,
					getDocumetacaoPessoaVO().getArquivoVO(),
					getDocumetacaoPessoaVO().getArquivoVO().getCpfPessoaDocumentacao()
							+ getDocumetacaoPessoaVO().getTipoDeDocumentoVO().getNome(),
					getConfiguracaoGeralPadraoSistema(), PastaBaseArquivoEnum.DOCUMENTOS_TMP, getUsuarioLogado());
			getDocumetacaoPessoaVO().getArquivoVO()
					.setPastaBaseArquivoWeb(getFacadeFactory().getArquivoFacade().executarDefinicaoUrlAcessoArquivo(
							getDocumetacaoPessoaVO().getArquivoVO(),
							getDocumetacaoPessoaVO().getArquivoVO().getPastaBaseArquivoEnum(),
							getConfiguracaoGeralPadraoSistema()));
			setExibirBotao(Boolean.TRUE);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			uploadEvent = null;
		}
	}

	public void selecionarObjetoDocumentacaoFuncionario() {
		setTam(100);
		setDocumetacaoPessoaVO(new DocumetacaoPessoaVO());
		DocumetacaoPessoaVO obj = (DocumetacaoPessoaVO) context().getExternalContext().getRequestMap()
				.get("documentacaoPessoaProfessor");
		if (obj == null) {
			obj = (DocumetacaoPessoaVO) context().getExternalContext().getRequestMap().get("documentacao");
		}

		if (obj != null) {
			setDocumetacaoPessoaVO(obj);
			if (obj != null && obj.getCodigo().equals(0) && getDocumetacaoPessoaVOAux() == null) {
				try {
					setDocumetacaoPessoaVOAux((DocumetacaoPessoaVO) obj.clone());
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
					setMensagemDetalhada("msg_erro", e.getMessage());
				}
			}
			setNomeArquivo(getUsuarioLogado().getCodigo() + "_" + new Date().getTime() + ".jpg");
			if (obj.getArquivoVO().getNome().equals("")) {
				getDocumetacaoPessoaVO().setArquivoVO(new ArquivoVO());
				getDocumetacaoPessoaVO().getArquivoVO().setDescricao(obj.getTipoDeDocumentoVO().getNome());
			}
		}
	}

	public void upLoadArquivoVerso(FileUploadEvent uploadEvent) {
		try {
			if (Uteis.isAtributoPreenchido(getDocumetacaoPessoaVO())) {
				getDocumetacaoPessoaVO().getArquivoVOVerso().setCodOrigem(getDocumetacaoPessoaVO().getCodigo());
			}
			getDocumetacaoPessoaVO().getArquivoVOVerso()
					.setCpfPessoaDocumentacao(getFuncionarioVO().getPessoa().getCPF().trim());
			getDocumetacaoPessoaVO().getArquivoVOVerso().setOrigem(OrigemArquivo.DOCUMENTACAO_PROFESSOR.getValor());
			getDocumetacaoPessoaVO().getArquivoVOVerso()
					.setServidorArquivoOnline(ServidorArquivoOnlineEnum.getEnum("APACHE"));
			getFacadeFactory().getArquivoHelper().upLoadDocumentacaoMatriculaRequerimento(uploadEvent,
					getDocumetacaoPessoaVO().getArquivoVOVerso(),
					getDocumetacaoPessoaVO().getArquivoVOVerso().getCpfPessoaDocumentacao()
							+ getDocumetacaoPessoaVO().getTipoDeDocumentoVO().getNome(),
					getConfiguracaoGeralPadraoSistema(), PastaBaseArquivoEnum.DOCUMENTOS_TMP, getUsuarioLogado());
			getDocumetacaoPessoaVO().getArquivoVOVerso()
					.setPastaBaseArquivoWeb(getFacadeFactory().getArquivoFacade().executarDefinicaoUrlAcessoArquivo(
							getDocumetacaoPessoaVO().getArquivoVOVerso(),
							getDocumetacaoPessoaVO().getArquivoVOVerso().getPastaBaseArquivoEnum(),
							getConfiguracaoGeralPadraoSistema()));
			setExibirBotaoVerso(Boolean.TRUE);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			uploadEvent = null;
		}
	}
	
	private Boolean exibirBotaoVerso;
	
	public Boolean getExibirBotaoVerso() {
		if (exibirBotaoVerso == null) {
			exibirBotaoVerso = false;
		}
		return exibirBotaoVerso;
	}
	
	public void setExibirBotaoVerso(Boolean exibirBotaoVerso) {
		this.exibirBotaoVerso = exibirBotaoVerso;
	}

	public void selecionarObjetoDocumentacaoFuncionarioVerso() {
		setTam(100);
		setDocumetacaoPessoaVO(new DocumetacaoPessoaVO());
		DocumetacaoPessoaVO obj = (DocumetacaoPessoaVO) context().getExternalContext().getRequestMap()
				.get("documentacaoPessoaProfessor");
		setDocumetacaoPessoaVO(obj);
		setNomeArquivo(getUsuarioLogado().getCodigo() + "_" + new Date().getTime() + ".jpg");
		if (obj.getArquivoVOVerso().getNome().equals("")) {
			getDocumetacaoPessoaVO().setArquivoVOVerso(new ArquivoVO());
			getDocumetacaoPessoaVO().getArquivoVOVerso().setDescricao(obj.getTipoDeDocumentoVO().getNome() + "_VERSO");
		}
	}

	public String getCaminhoServidorDownloadDocumentacao() {
		try {
			return getFacadeFactory().getArquivoFacade().executarDefinicaoUrlAcessoArquivo(
					getDocumetacaoPessoaVO().getArquivoVO(),
					getDocumetacaoPessoaVO().getArquivoVO().getPastaBaseArquivoEnum(),
					getConfiguracaoGeralPadraoSistema()) + "?UID=" + new Date().getTime();
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
		return "";
	}

	public String getCaminhoServidorDownloadDocumentacaoVerso() {
		try {
			return getFacadeFactory().getArquivoFacade().executarDefinicaoUrlAcessoArquivo(
					getDocumetacaoPessoaVO().getArquivoVOVerso(),
					getDocumetacaoPessoaVO().getArquivoVOVerso().getPastaBaseArquivoEnum(),
					getConfiguracaoGeralPadraoSistema()) + "?UID=" + new Date().getTime();
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
		return "";
	}

	public void adicionarArquivoDocumentacaoPessoa() {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "FuncionarioControle",
					"Iniciando Adicionar Arquivo Documentação Professor", "Uploading");
			getDocumetacaoPessoaVO().getArquivoVO().getResponsavelUpload().setCodigo(getUsuarioLogado().getCodigo());
			getDocumetacaoPessoaVO().getArquivoVO().getResponsavelUpload().setNome(getUsuarioLogado().getNome());
			getDocumetacaoPessoaVO().getUsuario().setCodigo(getUsuarioLogado().getCodigo());
			getDocumetacaoPessoaVO().getArquivoVO().setDataUpload(new Date());
			getDocumetacaoPessoaVO().getArquivoVO().setManterDisponibilizacao(true);
			getDocumetacaoPessoaVO().getArquivoVO()
					.setDataDisponibilizacao(getDocumetacaoPessoaVO().getArquivoVO().getDataUpload());
			getDocumetacaoPessoaVO().getArquivoVO().setDataIndisponibilizacao(null);
			getDocumetacaoPessoaVO().getArquivoVO().setSituacao(SituacaoArquivo.ATIVO.getValor());
			getDocumetacaoPessoaVO().getArquivoVO().setOrigem(OrigemArquivo.DOCUMENTACAO_PROFESSOR.getValor());
			getDocumetacaoPessoaVO().getArquivoVO().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.DOCUMENTOS_TMP);
			getDocumetacaoPessoaVO().setExcluirArquivo(false);
			if (getDocumetacaoPessoaVO().getEntregue()
					&& getDocumetacaoPessoaVO().getTipoDeDocumentoVO().getDocumentoFrenteVerso()) {
				setAbrirModalInclusaoArquivoVerso("RichFaces.$('panelIncluirArquivoVerso').show()");
			} else {
				setDocumetacaoPessoaVO(new DocumetacaoPessoaVO());
			}
			registrarAtividadeUsuario(getUsuarioLogado(), "FuncionarioControle",
					"Finalizando Adicionar Arquivo Documentação Professor", "Uploading");
			setMensagemID("msg_sucesso_upload");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void adicionarArquivoDocumentacaoPessoaVerso() {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "FuncionarioControle",
					"Iniciando Adicionar Arquivo Documentação Professor", "Uploading");
			getDocumetacaoPessoaVO().getArquivoVOVerso().getResponsavelUpload()
					.setCodigo(getUsuarioLogado().getCodigo());
			getDocumetacaoPessoaVO().getArquivoVOVerso().getResponsavelUpload().setNome(getUsuarioLogado().getNome());
			getDocumetacaoPessoaVO().getUsuario().setCodigo(getUsuarioLogado().getCodigo());
			getDocumetacaoPessoaVO().getUsuario().setNome(getUsuarioLogado().getNome());
			getDocumetacaoPessoaVO().getArquivoVOVerso().setDataUpload(new Date());
			getDocumetacaoPessoaVO().getArquivoVOVerso().setManterDisponibilizacao(true);
			getDocumetacaoPessoaVO().getArquivoVOVerso()
					.setDataDisponibilizacao(getDocumetacaoPessoaVO().getArquivoVO().getDataUpload());
			getDocumetacaoPessoaVO().getArquivoVOVerso().setDataIndisponibilizacao(null);
			getDocumetacaoPessoaVO().getArquivoVOVerso().setSituacao(SituacaoArquivo.ATIVO.getValor());
			getDocumetacaoPessoaVO().getArquivoVOVerso().setOrigem(OrigemArquivo.DOCUMENTACAO_PROFESSOR.getValor());
			getDocumetacaoPessoaVO().setExcluirArquivo(false);
			getDocumetacaoPessoaVO().getArquivoVOVerso()
					.setServidorArquivoOnline(ServidorArquivoOnlineEnum.getEnum("APACHE"));
			setDocumetacaoPessoaVO(new DocumetacaoPessoaVO());
			registrarAtividadeUsuario(getUsuarioLogado(), "FuncionarioControle",
					"Finalizando Adicionar Arquivo Documentação Professor", "Uploading");
			setMensagemID("msg_sucesso_upload");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void realizarDownloadArquivoAssinado() {
		try {
			DocumetacaoPessoaVO obj = (DocumetacaoPessoaVO) context().getExternalContext().getRequestMap()
					.get("documentacaoPessoaProfessor");
			DocumentoAssinadoVO doc = getFacadeFactory().getDocumentoAssinadoFacade()
					.consultarDocumentoAssinadoPorArquivo(obj.getArquivoVO().getCodigo(), getUsuarioLogadoClone());
			if (Uteis.isAtributoPreenchido(doc) && doc.getProvedorDeAssinaturaEnum().isProvedorCertisign()) {
				doc.setArquivo(obj.getArquivoVO());
				getFacadeFactory().getDocumentoAssinadoFacade().realizarDownloadArquivoProvedorCertisign(doc,
						getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(doc.getUnidadeEnsinoVO().getCodigo()),
						getUsuarioLogadoClone());
			}
			if (Uteis.isAtributoPreenchido(doc) && doc.getProvedorDeAssinaturaEnum().isProvedorTechCert()) {
				doc.setArquivo(obj.getArquivoVO());
				getFacadeFactory().getDocumentoAssinadoFacade().realizarDownloadArquivoProvedorTechCert(doc,
						getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(doc.getUnidadeEnsinoVO().getCodigo()),
						getUsuarioLogadoClone());
			}
			realizarDownloadArquivo(obj.getArquivoVO());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public String getCaminhoPDF() {
		return getDocumetacaoPessoaVO().getArquivoVO().getPastaBaseArquivoWeb() + "?embedded=true";
	}

	public String getCaminhoPDFVerso() {
		return getDocumetacaoPessoaVO().getArquivoVOVerso().getPastaBaseArquivoWeb() + "?embedded=true";
	}

	public void cancelarExclusaoArquivoDocumentoEntregue() {
		if (getDocumetacaoPessoaVO() != null && !getDocumetacaoPessoaVO().getArquivoVO().getNome().equals("")
				&& !getDocumetacaoPessoaVO().getEntregue()) {
			getDocumetacaoPessoaVO().setEntregue(true);
		}
		setDocumetacaoPessoaVO(new DocumetacaoPessoaVO());
	}

	public void removerArquivoDocumentacao() throws Exception {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "FuncionarioControle",
					"Iniciando Remover Arquivo Documentação Professor ", "Downloading - Removendo");
			getFacadeFactory().getDocumetacaoPessoaFacade().excluirDocumentacaoPessoa(getDocumetacaoPessoaVO(),
					getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			getDocumetacaoPessoaVO().setEntregue(false);
			getDocumetacaoPessoaVO().setDataEntrega(null);
			getDocumetacaoPessoaVO().setUsuario(null);
			getDocumetacaoPessoaVO().setArquivoVO(new ArquivoVO());
			getDocumetacaoPessoaVO().getArquivoVO().setDescricao("");
			getDocumetacaoPessoaVO().setArquivoVOVerso(new ArquivoVO());
			getDocumetacaoPessoaVO().getArquivoVOVerso().setDescricao("");
			registrarAtividadeUsuario(getUsuarioLogado(), "FuncionarioControle",
					"Finalizando Remover Arquivo Documentação Professor ", "Downloading - Removendo");
			setDocumetacaoPessoaVO(new DocumetacaoPessoaVO());

			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void recortarImagemVerso() {
		try {
			if (getLarguraVerso() == 0f && getAlturaVerso() == 0f && getXcropVerso() == 0f && getYcropVerso() == 0f) {
				throw new Exception("Clique e arraste sobre a imagem para selecionar a área que deve ser recortada.");
			}
			getFacadeFactory().getArquivoHelper().recortarImagem(getDocumetacaoPessoaVO().getArquivoVOVerso(),
					PastaBaseArquivoEnum.DOCUMENTOS_TMP, getConfiguracaoGeralPadraoSistema(), getLarguraVerso(),
					getAlturaVerso(), getXcropVerso(), getYcropVerso(), getUsuarioLogado());
			limparMensagem();
		} catch (ConsistirException ex) {
			setConsistirExceptionMensagemDetalhada("msg_erro", ex, Uteis.ERRO);
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage(), Uteis.ERRO);
		}
	}

	public void executarZoomInVerso() {
		getFacadeFactory().getArquivoHelper().executarZoomImagemRequerimento("in",
				getDocumetacaoPessoaVO().getArquivoVOVerso(), getConfiguracaoGeralPadraoSistema());
	}

	public void executarZoomOutVerso() {
		getFacadeFactory().getArquivoHelper().executarZoomImagemRequerimento("out",
				getDocumetacaoPessoaVO().getArquivoVOVerso(), getConfiguracaoGeralPadraoSistema());
	}

	public void rotacionar90GrausParaEsquerdaVerso() {
		try {
			getFacadeFactory().getArquivoHelper().rotacionar90GrausParaEsquerda(
					getDocumetacaoPessoaVO().getArquivoVOVerso(), getConfiguracaoGeralPadraoSistema());
			limparMensagem();
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public void rotacionar90GrausParaDireitaVerso() {
		try {
			getFacadeFactory().getArquivoHelper().rotacionar90GrausParaDireita(
					getDocumetacaoPessoaVO().getArquivoVOVerso(), getConfiguracaoGeralPadraoSistema());
			limparMensagem();
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public void rotacionar180GrausVerso() {
		try {
			getFacadeFactory().getArquivoHelper().rotacionar180Graus(getDocumetacaoPessoaVO().getArquivoVOVerso(),
					getConfiguracaoGeralPadraoSistema());
			limparMensagem();
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}
	
	public void recortarImagemDocumento() {
		try {
			if (getLargura() == 0f && getAltura() == 0f && getX() == 0f && getY() == 0f) {
				throw new Exception("Clique e arraste sobre a imagem para selecionar a área que deve ser recortada.");
			}
			getFacadeFactory().getArquivoHelper().recortarImagem(getDocumetacaoPessoaVO().getArquivoVO(),
					PastaBaseArquivoEnum.DOCUMENTOS_TMP, getConfiguracaoGeralPadraoSistema(), getLargura(),
					getAltura(), getX(), getY(), getUsuarioLogado());
			limparMensagem();
		} catch (ConsistirException ex) {
			setConsistirExceptionMensagemDetalhada("msg_erro", ex, Uteis.ERRO);
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage(), Uteis.ERRO);
		}
	}

	public void executarZoomInDocumento() {
		getFacadeFactory().getArquivoHelper().executarZoomImagemRequerimento("in",
				getDocumetacaoPessoaVO().getArquivoVO(), getConfiguracaoGeralPadraoSistema());
	}

	public void executarZoomOutDocumento() {
		getFacadeFactory().getArquivoHelper().executarZoomImagemRequerimento("out",
				getDocumetacaoPessoaVO().getArquivoVO(), getConfiguracaoGeralPadraoSistema());
	}

	public void rotacionar90GrausParaEsquerdaDocumento() {
		try {
			getFacadeFactory().getArquivoHelper().rotacionar90GrausParaEsquerda(
					getDocumetacaoPessoaVO().getArquivoVO(), getConfiguracaoGeralPadraoSistema());
			limparMensagem();
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public void rotacionar90GrausParaDireitaDocumento() {
		try {
			getFacadeFactory().getArquivoHelper().rotacionar90GrausParaDireita(
					getDocumetacaoPessoaVO().getArquivoVO(), getConfiguracaoGeralPadraoSistema());
			limparMensagem();
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public void rotacionar180GrausDocumento() {
		try {
			getFacadeFactory().getArquivoHelper().rotacionar180Graus(getDocumetacaoPessoaVO().getArquivoVO(),
					getConfiguracaoGeralPadraoSistema());
			limparMensagem();
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public void atualizarDataEntrega() {
		DocumetacaoPessoaVO obj = (DocumetacaoPessoaVO) context().getExternalContext().getRequestMap()
				.get("documentacaoPessoaProfessor");
		if (obj.getEntregue()) {
			obj.setDataEntrega(new Date());
		} else {
			obj.setDataEntrega(null);
		}
	}

	public String editarDadosVisaoProfessor() {
		try {

			getPessoaVO().setCodigo(getUsuarioLogado().getPessoa().getCodigo());
			getPessoaVO().setNome(getUsuarioLogado().getPessoa().getNome());
			consultarHorarioProfessorDia();
			setMensagemID("msg_dados_editar");
			return Uteis.getCaminhoRedirecionamentoNavegacao("meusHorariosProfessor");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("meusHorariosProfessor");
		}
	}

	public String editarDadosVisaoCoordenador() {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "FuncionarioControle",
					"Inicializando Editar Dados Visão Coordenador", "Editando");
			setTipoConsulta("professor");
			registrarAtividadeUsuario(getUsuarioLogado(), "FuncionarioControle",
					"Finalizando Editar Dados Visão Coordenador", "Editando");
			setMensagemID("msg_dados_editar");
			return Uteis.getCaminhoRedirecionamentoNavegacao("horariosProfessorVisaoCoordenador.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("horariosProfessorVisaoCoordenador.xhtml");
		}
	}

	public void montarListaSelectItemTurmaCoordenador() {
		try {
			montarListaSelectItemTurmaCoordenador("");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void montarListaSelectItemTurmaCoordenador(String prm) {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			if (getListaSelectItemTurma().isEmpty()) {
				resultadoConsulta = consultarTurmaCoordenador();
				i = resultadoConsulta.iterator();
				getListaSelectItemTurma().clear();
				getListaSelectItemTurma().add(new SelectItem(0, ""));
				while (i.hasNext()) {
					TurmaVO obj = (TurmaVO) i.next();
					getListaSelectItemTurma()
							.add(new SelectItem(obj.getCodigo(), obj.getIdentificadorTurma().toString()));
					removerObjetoMemoria(obj);
				}
				resultadoConsulta.clear();
				resultadoConsulta = null;
				i = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public List consultarTurmaCoordenador() throws Exception {
		try {
			return getFacadeFactory().getTurmaFacade().consultaRapidaPorCoordenador(
					getUsuarioLogado().getPessoa().getCodigo(), false, false, false,
					getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}

	public void montarListaSelectItemProfessoresTurmaCoordenador() {
		try {
			montarListaSelectItemProfessoresTurmaCoordenador("");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void montarListaSelectItemProfessoresTurmaCoordenador(String prm) {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			getListaSelectItemProfessoresTurma().clear();
			if (getTurmaVO().getCodigo() != null && !getTurmaVO().getCodigo().equals(0)) {
				resultadoConsulta = consultarProfessoresTurmaCoordenador();
				i = resultadoConsulta.iterator();
				getListaSelectItemProfessoresTurma().clear();
				while (i.hasNext()) {
					PessoaVO obj = (PessoaVO) i.next();
					getListaSelectItemProfessoresTurma().add(new SelectItem(obj.getCodigo(), obj.getNome()));
					removerObjetoMemoria(obj);
				}
				resultadoConsulta.clear();
				resultadoConsulta = null;
				i = null;
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public List consultarProfessoresTurmaCoordenador() throws Exception {
		try {
			return getFacadeFactory().getPessoaFacade().consultarProfessoresDaTurmaPorTurma(getTurmaVO().getCodigo(),
					getUnidadeEnsinoLogado().getCodigo(), Uteis.getSemestreAtual(), Uteis.getData(new Date(), "yyyy"),
					false, getUsuarioLogado());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}

	public void limparListaHorariosProfessores() {
		try {
			getHorarioProfessorGraduacao().clear();
			getHorarioProfessorPosGraduacao().clear();
			setDataInicio(Uteis.getDataPrimeiroDiaMes(new Date()));
			setDataFim(Uteis.getDataUltimoDiaMes(new Date()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void consultarHorarioProfessorDiaVisaoCoordenador() {
		try {
			Map<GraduacaoPosGraduacaoEnum, List<HorarioProfessorDiaItemVO>> mapConsulta = getFacadeFactory()
					.getHorarioProfessorDiaFacade()
					.consultarHorariosProfessorSeparadoPorNivelEducacional(getFuncionarioVO().getPessoa().getCodigo(),
							getUnidadeEnsinoVO().getCodigo(), getUnidadeEnsinoCursoVO().getCurso().getCodigo(),
							getTurmaVO().getCodigo(), getDisciplinaVO().getCodigo(), getDataInicio(), getDataFim(),
							true, false, "", getUsuarioLogado(), getOrdenacao());
			getHorarioProfessorGraduacao().clear();
			getHorarioProfessorPosGraduacao().clear();
			if (mapConsulta.containsKey(GraduacaoPosGraduacaoEnum.GRADUACAO)) {
				getHorarioProfessorGraduacao().addAll(mapConsulta.get(GraduacaoPosGraduacaoEnum.GRADUACAO));
			}
			if (mapConsulta.containsKey(GraduacaoPosGraduacaoEnum.POS_GRADUACAO)) {
				getHorarioProfessorPosGraduacao().addAll(mapConsulta.get(GraduacaoPosGraduacaoEnum.POS_GRADUACAO));
			}
			mapConsulta.clear();
			// getHorarioProfessorDiaVO().setHorarioProfessorDiaItemVOs(getFacadeFactory().getHorarioProfessorDiaFacade().consultarHorariosProfessor(getUsuarioLogado().getPessoa().getCodigo(),
			// 0, 0, 0, 0, getDataInicio(), getDataFim(), getUsuarioLogado()));
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void consultarHorarioProfessor() {
		if (getLayout().equals("LAYOUT_DIA_A_DIA")) {
			consultarHorarioProfessorDia();
		} else {
			consultarHorarioProfessorRel();
		}
	}

	public void consultarHorarioProfessorDia() {
		try {
//            setDisciplina(null);
//            setTurma(null);
			Map<GraduacaoPosGraduacaoEnum, List<HorarioProfessorDiaItemVO>> mapConsulta = null;
			if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
				mapConsulta = getFacadeFactory().getHorarioProfessorDiaFacade()
						.consultarHorariosProfessorSeparadoPorNivelEducacional(
								getUsuarioLogado().getPessoa().getCodigo(), 0, 0, 0, 0, getDataInicio(), getDataFim(),
								true, "", getUsuarioLogado(), getOrdenacao(), false, null);
			} else {
				mapConsulta = getFacadeFactory().getHorarioProfessorDiaFacade()
						.consultarHorariosProfessorSeparadoPorNivelEducacional(getPessoaVO().getCodigo(), 0, 0, 0, 0,
								getDataInicio(), getDataFim(), true, "", getUsuarioLogado(), getOrdenacao(), false,
								null);
			}
			getHorarioProfessorGraduacao().clear();
			getHorarioProfessorPosGraduacao().clear();
			if (mapConsulta.containsKey(GraduacaoPosGraduacaoEnum.GRADUACAO)) {
				getHorarioProfessorGraduacao().addAll(mapConsulta.get(GraduacaoPosGraduacaoEnum.GRADUACAO));
			}
			if (mapConsulta.containsKey(GraduacaoPosGraduacaoEnum.POS_GRADUACAO)) {
				getHorarioProfessorPosGraduacao().addAll(mapConsulta.get(GraduacaoPosGraduacaoEnum.POS_GRADUACAO));
			}
			mapConsulta.clear();
			// getHorarioProfessorDiaVO().setHorarioProfessorDiaItemVOs(getFacadeFactory().getHorarioProfessorDiaFacade().consultarHorariosProfessor(getUsuarioLogado().getPessoa().getCodigo(),
			// 0, 0, 0, 0, getDataInicio(), getDataFim(), getUsuarioLogado()));
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public List<HorarioProfessorDiaItemVO> getHorarioProfessorGraduacao() {
		if (horarioProfessorGraduacao == null) {
			horarioProfessorGraduacao = new ArrayList<HorarioProfessorDiaItemVO>(0);
		}
		return horarioProfessorGraduacao;
	}

	public void setHorarioProfessorGraduacao(List<HorarioProfessorDiaItemVO> horarioProfessorGraduacao) {
		this.horarioProfessorGraduacao = horarioProfessorGraduacao;
	}

	public List<HorarioProfessorDiaItemVO> getHorarioProfessorPosGraduacao() {
		if (horarioProfessorPosGraduacao == null) {
			horarioProfessorPosGraduacao = new ArrayList<HorarioProfessorDiaItemVO>(0);
		}
		return horarioProfessorPosGraduacao;
	}

	public void setHorarioProfessorPosGraduacao(List<HorarioProfessorDiaItemVO> horarioProfessorPosGraduacao) {
		this.horarioProfessorPosGraduacao = horarioProfessorPosGraduacao;
	}

	public void realizarImpressaoPDFHorarioProfessorDia() {
		try {
			getHorarioProfessorDiaVO().getHorarioProfessorDiaItemVOs().clear();
			getHorarioProfessorDiaVO().getHorarioProfessorDiaItemVOs().addAll(getHorarioProfessorPosGraduacao());
			getHorarioProfessorDiaVO().getHorarioProfessorDiaItemVOs().addAll(getHorarioProfessorGraduacao());
			if (!getHorarioProfessorDiaVO().getHorarioProfessorDiaItemVOs().isEmpty()) {
				getSuperParametroRelVO().limparParametros();
				getSuperParametroRelVO().setNomeDesignIreport(
						getFacadeFactory().getHorarioProfessorDiaItemFacade().designIReportRelatorio());
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO()
						.setSubReport_Dir(getFacadeFactory().getHorarioProfessorDiaItemFacade().caminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio("Agenda do Professor");
				getSuperParametroRelVO().setListaObjetos(getHorarioProfessorDiaVO().getHorarioProfessorDiaItemVOs());
				getSuperParametroRelVO().setCaminhoBaseRelatorio(
						getFacadeFactory().getHorarioProfessorDiaItemFacade().caminhoBaseRelatorio());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setProfessor(getUsuarioLogado().getPessoa().getNome());
				getSuperParametroRelVO().setUnidadeEnsino(getUnidadeEnsinoLogado().getNome());
				getSuperParametroRelVO().getParametros().put("visaoProfessor",
						getUsuarioLogado().getIsApresentarVisaoProfessor());
				realizarImpressaoRelatorio();
				persistirLayoutPadrao(getLayout());
				getHorarioProfessorDiaVO().getHorarioProfessorDiaItemVOs().clear();
				setMensagemID("msg_relatorio_ok", Uteis.SUCESSO);
			} else {
				setMensagemID("msg_relatorio_sem_dados", Uteis.SUCESSO);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		} finally {
			getHorarioProfessorDiaVO().getHorarioProfessorDiaItemVOs().clear();
			removerObjetoMemoria(getSuperParametroRelVO());
		}
	}

	public void realizarImpressaoPDFHorarioProfessorDiaVisaoCoordenador() {
		try {
			getHorarioProfessorDiaVO().getHorarioProfessorDiaItemVOs().clear();
			getHorarioProfessorDiaVO().getHorarioProfessorDiaItemVOs().addAll(getHorarioProfessorPosGraduacao());
			getHorarioProfessorDiaVO().getHorarioProfessorDiaItemVOs().addAll(getHorarioProfessorGraduacao());
			if (!getHorarioProfessorDiaVO().getHorarioProfessorDiaItemVOs().isEmpty()) {
				setSuperParametroRelVO(new SuperParametroRelVO());
				getSuperParametroRelVO().setNomeDesignIreport(
						getFacadeFactory().getHorarioProfessorDiaItemFacade().designIReportRelatorio());
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO()
						.setSubReport_Dir(getFacadeFactory().getHorarioProfessorDiaItemFacade().caminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio("Agenda do Professor");
				getSuperParametroRelVO().setListaObjetos(getHorarioProfessorDiaVO().getHorarioProfessorDiaItemVOs());
				getSuperParametroRelVO().setCaminhoBaseRelatorio(
						getFacadeFactory().getHorarioProfessorDiaItemFacade().caminhoBaseRelatorio());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO()
						.setProfessor(getHorarioProfessorDiaVO().getHorarioProfessorDiaItemVOs().get(0).getProfessor());
				getSuperParametroRelVO().setUnidadeEnsino(getUnidadeEnsinoLogado().getNome());
				realizarImpressaoRelatorio();
				persistirLayoutPadrao(getLayout());
				getHorarioProfessorDiaVO().getHorarioProfessorDiaItemVOs().clear();
				setMensagemID("msg_relatorio_ok", Uteis.SUCESSO);
			} else {
				setMensagemID("msg_relatorio_sem_dados", Uteis.SUCESSO);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		} finally {
			getHorarioProfessorDiaVO().getHorarioProfessorDiaItemVOs().clear();
			removerObjetoMemoria(getSuperParametroRelVO());
		}
	}

	public void gravarVisaoProfessor() {
		try {

			pessoaVO.setHorarioProfessorVOs(pessoaVO.montarNovaListaHorarioProfessor());
			getFacadeFactory().getHorarioProfessorFacade().alterarHorarioProfessorPorTurno(pessoaVO, getCodigoTurno(),
					getUsuarioLogado());

//        	getFacadeFactory().getPessoaFacade().setIdEntidade("Professor");
//            getFacadeFactory().getPessoaFacade().validarIncluir();
//            pessoaVO.setProfessor(Boolean.TRUE);
//            pessoaVO.setFuncionario(Boolean.TRUE);
//            pessoaVO.montarListaHorarioProfessor();
//            if (pessoaVO.isNovoObj().booleanValue()) {
//                ConfiguracaoGeralSistemaVO conf = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarPorCodigoUnidadeEnsino(getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
//                if (conf.getCodigo().intValue() == 0 && getUnidadeEnsino().getCodigo().intValue() != 0) {
//                    conf = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarPorCodigoUnidadeEnsino(0, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
//                }
//                //getPessoaVO().setCssPadrao(conf.getVisaoPadraoProfessor().getCsspadrao());
//                getPessoaVO().setValorCssTopoLogo(conf.getVisaoPadraoProfessor().getValorCssTopoLogo());
//                getPessoaVO().setValorCssBackground(conf.getVisaoPadraoProfessor().getValorCssBackground());
//                getPessoaVO().setValorCssMenu(conf.getVisaoPadraoProfessor().getValorCssMenu());
//                getFacadeFactory().getPessoaFacade().incluir(pessoaVO);
//            } else {
//                getFacadeFactory().getPessoaFacade().alterar(pessoaVO);
//            }
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void carregarEnderecoPessoa() {
		try {
			getFacadeFactory().getEnderecoFacade().carregarEndereco(pessoaVO, getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public FuncionarioVO montarDadosFuncionarioVOCompleto(FuncionarioVO obj) {
		try {
			getFacadeFactory().getFuncionarioFacade().carregarDados(obj, getUsuarioLogado());
			isPermiteConfiguracaoSeiGsuite();
			return obj;
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			e.printStackTrace();
		}
		return new FuncionarioVO();
	}

	public List montarDadosFormacaoAcademicaVOCompleto(FuncionarioVO obj) {
		try {
			return FormacaoAcademica.consultarFormacaoAcademicas(obj.getPessoa().getCodigo(), false,
					getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
		return new ArrayList(0);
	}

	public String getCadastroUsuarioAutomatico() {
		try {
			HttpSession session = (HttpSession) context().getExternalContext().getSession(true);
			session.setAttribute("funcionarioItem", getFuncionarioVO());
			removerControleMemoriaFlashTela("UsuarioControle");
//            executarMetodoControle(UsuarioControle.class.getSimpleName(), "inicializarUsuarioAutomatico", getFuncionarioVO(), TipoPessoa.FUNCIONARIO.getValor());
			return "abrirPopup('usuarioForm.xhtml', 'usuario' , 780, 585);";
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			return "";
		}
	}

	public void executarValidacaoUsuarioCadastrado() {
		try {
			List<UsuarioVO> users = getFacadeFactory().getUsuarioFacade().consultarPorCodigoPessoa(
					getFuncionarioVO().getPessoa().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
					getUsuarioLogado());
			if (!getFuncionarioVO().getCodigo().equals(0) && (users == null || users.size() == 0)) {
				setExibirFormCriarUsuario(Boolean.TRUE);
			} else {
				setExibirFormCriarUsuario(Boolean.FALSE);
				throw new ConsistirException("O USUÁRIO já esta cadastrado.");
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	/**
	 * Método responsável inicializar objetos relacionados a classe
	 * <code>FuncionarioVO</code>. Esta inicialização é necessária por exigência da
	 * tecnologia JSF, que não trabalha com valores nulos para estes atributos.
	 */
	public void inicializarAtributosRelacionados(FuncionarioVO obj, PessoaVO pessoaVO) {
		setFiliacaoVO(new FiliacaoVO());
		setFormacaoAcademicaVO(new FormacaoAcademicaVO());
		setPessoaPreInscricaoCursoVO(new PessoaPreInscricaoCursoVO());
		setFuncionarioCargoVO(new FuncionarioCargoVO());
		if (obj.getPessoa() == null) {
			obj.setPessoa(new PessoaVO());
		}
		if (pessoaVO.getCidade() == null) {
			pessoaVO.setCidade(new CidadeVO());
		}
		if (pessoaVO.getNaturalidade() == null) {
			pessoaVO.setNaturalidade(new CidadeVO());
		}
		if (pessoaVO.getNacionalidade() == null) {
			pessoaVO.setNacionalidade(new PaizVO());
		}
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto da
	 * classe <code>Funcionario</code>. Caso o objeto seja novo (ainda não gravado
	 * no BD) é acionado a operação <code>incluir()</code>. Caso contrário é
	 * acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto
	 * não é gravado, sendo re-apresentado para o usuário juntamente com uma
	 * mensagem de erro.
	 */
	public void gravar() {
		try {
			getPessoaVO().getDocumetacaoPessoaVOs().clear();
			getPessoaVO().getDocumetacaoPessoaVOs().addAll(getListaDocumentacaoPessoaProfessor());
			getFacadeFactory().getFuncionarioFacade().validarDados(getPessoaVO());
			if (!getPessoaVO().getAtuaComoDocente().equals("FU")) {
				pessoaVO.setProfessor(Boolean.TRUE);
				pessoaVO.setFuncionario(Boolean.TRUE);
				getFacadeFactory().getPessoaFacade().setIdEntidade("Professor");
			} else {
				pessoaVO.setProfessor(Boolean.FALSE);
				pessoaVO.setFuncionario(Boolean.TRUE);
				getFacadeFactory().getPessoaFacade().setIdEntidade("Funcionario");
			}
			getPessoaVO().setAluno(
					getFacadeFactory().getPessoaFacade().verificarSeUsuarioIsAluno(getPessoaVO().getCodigo()));
			funcionarioVO.setPessoa(pessoaVO);
			getFuncionarioVO().getPessoa()
					.inicializarDadosNecessidadeGerarProspectPelaConfiguracaoGeralSistemaTipoOrigem(
							getConfiguracaoGeralPadraoSistema(), "FUNCIONARIO");
			if (funcionarioVO.isNovoObj().booleanValue()) {
				if (!getPessoaVO().getAtuaComoDocente().equals("FU")) {
					ConfiguracaoGeralSistemaVO conf = getFacadeFactory().getConfiguracaoGeralSistemaFacade()
							.consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado(), null);
					getPessoaVO().setValorCssTopoLogo(conf.getVisaoPadraoProfessor().getValorCssTopoLogo());
					getPessoaVO().setValorCssBackground(conf.getVisaoPadraoProfessor().getValorCssBackground());
					getPessoaVO().setValorCssMenu(conf.getVisaoPadraoProfessor().getValorCssMenu());
					pessoaVO.montarListaHorarioProfessor();
				}
				getFacadeFactory().getFuncionarioFacade().persistirFuncionarioComEmailInstituciona(getFuncionarioVO(),
						getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
			} else {
				getFacadeFactory().getFuncionarioFacade().persistirFuncionarioComEmailInstituciona(getFuncionarioVO(),
						getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
			}

			inicializarDadosFotoUsuario();

			getFuncionarioVO().getFuncionarioCargoVOs().stream().forEach(p -> {
				p.montarCentroCusto(getFuncionarioVO());
				if (!Uteis.isAtributoPreenchido(p.getMatriculaCargo())) {
					try {
						p.setMatriculaCargo(getFacadeFactory().getFuncionarioCargoFacade()
								.consultarMatriculaPorFuncionarioCargo(p.getCodigo(), false, getUsuarioLogado()));
					} catch (Exception e) {
						setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
					}
				}
			});
			if(getFuncionarioVO().isCriarUsuario()) {
				montarListaSelectItemCodPerfilAcesso();
			}
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void persistirDocumetacaoManualProfessor() {
		try {
			if (Uteis.isAtributoPreenchido(getListaTipoDocumento())) {
				List<DocumetacaoPessoaVO> listaDocumentacaoProfessorManual = montarListaDocumentacaoPessoaProfessorManual();
				if (Uteis.isAtributoPreenchido(listaDocumentacaoProfessorManual)) {
					getFacadeFactory().getDocumetacaoPessoaFacade().incluirDocumetacaoPessoas(
							getFuncionarioVO().getPessoa(), getFuncionarioVO().getPessoa().getCodigo(),
							listaDocumentacaoProfessorManual, getUsuarioLogadoClone(), getConfiguracaoGeralSistemaVO());
					getListaDocumentacaoPessoaProfessor().addAll(listaDocumentacaoProfessorManual);
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void atualizarGridComOsCargosDoFuncionario() {
		List listaDeCargosDoFuncionario;
		try {
			listaDeCargosDoFuncionario = getFacadeFactory().getFuncionarioCargoFacade().consultarFuncionarioCargos(
					getFuncionarioVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			getFuncionarioVO().setFuncionarioCargoVOs(listaDeCargosDoFuncionario);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void upload(FileUploadEvent uploadEvent) {

		try {
			getPessoaVO().getArquivoImagem().setCpfRequerimento(getPessoaVO().getCPF());
			getFacadeFactory().getArquivoHelper().upLoad(uploadEvent, getPessoaVO().getArquivoImagem(),
					getConfiguracaoGeralPadraoSistema(), PastaBaseArquivoEnum.IMAGEM_TMP, getUsuarioLogado());
			setCaminhoFotoUsuario(getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(
					getPessoaVO().getArquivoImagem(), PastaBaseArquivoEnum.IMAGEM_TMP.getValue(),
					getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(), "foto_usuario.png", true));
			setExibirBotao(Boolean.TRUE);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		} finally {
			uploadEvent = null;
		}
	}

	public void upLoadArquivo(FileUploadEvent uploadEvent) {
		try {
			getFacadeFactory().getArquivoHelper().upLoad(uploadEvent, getFuncionarioVO().getArquivoAssinaturaVO(),
					getConfiguracaoGeralPadraoSistema(), PastaBaseArquivoEnum.ASSINATURA_TMP, getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		} finally {
			uploadEvent = null;
		}
	}

	public String getUrlAssinaturaApresentar() {
		try {
			if (Uteis.isAtributoPreenchido(getFuncionarioVO().getArquivoAssinaturaVO().getPastaBaseArquivoEnum())
					&& getFuncionarioVO().getArquivoAssinaturaVO().getPastaBaseArquivoEnum()
							.equals(PastaBaseArquivoEnum.ASSINATURA_TMP)) {
				return getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(
						getFuncionarioVO().getArquivoAssinaturaVO(), PastaBaseArquivoEnum.ASSINATURA_TMP.getValue(),
						getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(), "", false);
			}
			return getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(
					getFuncionarioVO().getArquivoAssinaturaVO(), PastaBaseArquivoEnum.ASSINATURA.getValue(),
					getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(), "", false);
		} catch (Exception e) {
			return "";
		}
	}

	public void paint(OutputStream out, Object data) {
		try {
			getFacadeFactory().getArquivoHelper().renderizarImagemNaTela(out, getPessoaVO().getArquivoImagem(),
					getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(), "foto_usuario.jpg");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		} finally {
		}
	}

	public void removerFoto() {
		try {
			getFacadeFactory().getArquivoHelper().removerArquivoDiretorio(true, getPessoaVO().getArquivoImagem(),
					PastaBaseArquivoEnum.IMAGEM.getValue(), getConfiguracaoGeralPadraoSistema());
			getPessoaVO().setArquivoImagem(null);
			getFacadeFactory().getPessoaFacade().alterar(getPessoaVO(), false, getUsuarioLogado(),
					getConfiguracaoGeralPadraoSistema(), false);
			setCaminhoFotoUsuario(getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(
					getPessoaVO().getArquivoImagem(), PastaBaseArquivoEnum.IMAGEM.getValue(),
					getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(), "foto_usuario.png", false));
			setExibirBotao(false);
			setRemoverFoto(Uteis.isAtributoPreenchido(getPessoaVO().getArquivoImagem().getNome()));
			setExibirUpload(true);
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public void confirmarFoto() {
		try {
			getPessoaVO().getArquivoImagem().setPastaBaseArquivo(PastaBaseArquivoEnum.IMAGEM_TMP.getValue());
			getFacadeFactory().getPessoaFacade().alterar(getPessoaVO(), false, getUsuarioLogado(),
					getConfiguracaoGeralPadraoSistema(), false);
			setCaminhoFotoUsuario(getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(
					getPessoaVO().getArquivoImagem(), PastaBaseArquivoEnum.IMAGEM.getValue(),
					getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(), "foto_usuario.jpg", false));
			setRemoverFoto(true);
			setExibirUpload(true);
			setExibirBotao(false);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void anularDataModelo() {
		setControleConsultaOtimizado(null);
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP
	 * ContaReceberCons.jsp. Define o tipo de consulta a ser executada, por meio de
	 * ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
	 * resultado, disponibiliza um List com os objetos selecionados na sessao da
	 * pagina.
	 */
	public void scrollerListener(DataScrollEvent dataScrollEvent) {
		getControleConsultaOtimizado().setPaginaAtual(dataScrollEvent.getPage());
		getControleConsultaOtimizado().setPage(dataScrollEvent.getPage());
		consultar();
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP
	 * FuncionarioCons.jsp. Define o tipo de consulta a ser executada, por meio de
	 * ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
	 * resultado, disponibiliza um List com os objetos selecionados na sessao da
	 * pagina.
	 */
	@Override
	public String consultar() {
		try {
			super.consultar();
			getControleConsultaOtimizado().setLimitePorPagina(10);
			List<FuncionarioVO> objs = new ArrayList(0);
			getFacadeFactory().getPessoaFacade().setIdEntidade("Funcionario");

			if (getControleConsulta().getCampoConsulta().equals("nome")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNome(
						getControleConsulta().getValorConsulta(), 0, "", getUnidadeEnsinoLogado().getCodigo(), null,
						null, getControleConsultaOtimizado().getLimitePorPagina(),
						getControleConsultaOtimizado().getOffset(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,
						getUsuarioLogado());
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(
						getFacadeFactory().getFuncionarioFacade().consultaTotalDeRegistroRapidaPorNome(
								getControleConsulta().getValorConsulta(), "", getUnidadeEnsinoLogado().getCodigo(),
								true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));

			}
			if (getControleConsulta().getCampoConsulta().equals("matricula")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorMatricula(
						getControleConsulta().getValorConsulta(), 0, getUnidadeEnsinoLogado().getCodigo(), null, null,
						Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getControleConsultaOtimizado().getLimitePorPagina(),
						getControleConsultaOtimizado().getOffset(), getUsuarioLogado());
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(
						getFacadeFactory().getFuncionarioFacade().consultaTotalDeRegistroRapidaPorMatricula(
								getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), null,
								null, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeCidade")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCidade(
						getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), null, null,
						getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(),
						true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(
						getFacadeFactory().getFuncionarioFacade().consultaTotalDeRegistroRapidaPorCidade(
								getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), null,
								null, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("CPF")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCPF(
						getControleConsulta().getValorConsulta(), 0, "", getUnidadeEnsinoLogado().getCodigo(), null,
						null, getControleConsultaOtimizado().getLimitePorPagina(),
						getControleConsultaOtimizado().getOffset(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,
						getUsuarioLogado());
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(
						getFacadeFactory().getFuncionarioFacade().consultaTotalDeRegistroRapidaPorCPF(
								getControleConsulta().getValorConsulta(), "", getUnidadeEnsinoLogado().getCodigo(),
								null, null, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("cargo")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCargo(
						getControleConsulta().getValorConsulta(), 0, getUnidadeEnsinoLogado().getCodigo(), null, null,
						getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(),
						true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(
						getFacadeFactory().getFuncionarioFacade().consultaTotalDeRegistroRapidaPorCargo(
								getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), null,
								null, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("departamento")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNomeDepartamento(
						getControleConsulta().getValorConsulta(), "FU", getUnidadeEnsinoLogado().getCodigo(), null,
						null, getControleConsultaOtimizado().getLimitePorPagina(),
						getControleConsultaOtimizado().getOffset(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,
						getUsuarioLogado());
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(
						getFacadeFactory().getFuncionarioFacade().consultaTotalDeRegistroRapidaPorNomeDepartamento(
								getControleConsulta().getValorConsulta(), "FU", getUnidadeEnsinoLogado().getCodigo(),
								null, null, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("unidadeEnsino")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorUnidadeEnsino(
						getControleConsulta().getValorConsulta(), "FU", getUnidadeEnsinoLogado().getCodigo(), null,
						null, getControleConsultaOtimizado().getLimitePorPagina(),
						getControleConsultaOtimizado().getOffset(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,
						getUsuarioLogado());
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(
						getFacadeFactory().getFuncionarioFacade().consultaTotalDeRegistroRapidaPorUnidadeEnsino(
								getControleConsulta().getValorConsulta(), "FU", getUnidadeEnsinoLogado().getCodigo(),
								null, null, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("secao")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorSecao(
						getControleConsulta().getValorConsulta(), true,
						getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(),
						true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getFuncionarioFacade()
						.consultaTotalDeRegistroRapidaPorSecao(getControleConsulta().getValorConsulta(), true, false,
								Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("formaContratacao")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorFormaContratacao(
						getControleConsulta().getValorConsulta(), true,
						getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(),
						true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getFuncionarioFacade()
						.consultaTotalDeRegistroRapidaPorFormaContratacao(getControleConsulta().getValorConsulta(),
								true, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("emailInstitucional")) {					    	
				objs = (getFacadeFactory().getFuncionarioFacade().consultaRapidaResumidaPorEmailInstitucional(getControleConsulta().getValorConsulta(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getControleConsultaOtimizado() , getUsuarioLogado()));
			}
			getControleConsultaOtimizado().setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("funcionarioCons");

		} catch (Exception e) {
			setListaConsulta(new ArrayList<FuncionarioVO>(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("funcionarioCons");
		}
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe
	 * <code>FuncionarioVO</code> Após a exclusão ela automaticamente aciona a
	 * rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			setAbaAtiva("tabDadosPessoais");
			getFacadeFactory().getPessoaFacade().setIdEntidade("Funcionario");
			getFacadeFactory().getFuncionarioFacade().excluir(funcionarioVO, getUsuarioLogado());
			setDisciplinasInteresseVO(new DisciplinasInteresseVO());
			setDisponibilidadeHorarioVO(new DisponibilidadeHorarioVO());
			setHorarioSimples(Boolean.TRUE);
			setFuncionarioVO(new FuncionarioVO());
			setPessoaVO(new PessoaVO());
			setFiliacaoVO(new FiliacaoVO());
			setCodigoTurno(0);
			setFormacaoAcademicaVO(new FormacaoAcademicaVO());
			setPessoaPreInscricaoCursoVO(new PessoaPreInscricaoCursoVO());
			setVerificarCpf(this.validarCadastroPorCpf());
			setFuncionarioCargoVO(new FuncionarioCargoVO());
			setConsultarPessoa(Boolean.TRUE);
			setEditarDados(Boolean.FALSE);
			setMensagemID("msg_dados_excluidos");
			return Uteis.getCaminhoRedirecionamentoNavegacao("funcionarioDadosPessoaisForm");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("funcionarioDadosPessoaisForm");
		}
	}

	public String editarDadosPessoa() {

		try {
			FuncionarioVO obj = getFacadeFactory().getFuncionarioFacade().consultarPorCodigoPessoa(
					getPessoaVO().getCodigo(), this.getUnidadeEnsinoLogado().getCodigo(), false,
					Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			obj = montarDadosFuncionarioVOCompleto(obj);
			getFacadeFactory().getFuncionarioFacade().inicializarDadosEntidadesSubordinadasFuncionarioVOCompleto(obj,
					getUsuarioLogado());
			setFuncionarioVO(new FuncionarioVO());
			setPessoaVO(new PessoaVO());
			setDisciplinasInteresseVO(new DisciplinasInteresseVO());
			setDisponibilidadeHorarioVO(new DisponibilidadeHorarioVO());
			inicializarAtributosRelacionados(funcionarioVO, obj.getPessoa());
			funcionarioVO.setNovoObj(Boolean.FALSE);
			setFuncionarioVO(funcionarioVO);
			inicializarListasSelectItemTodosComboBox();
			setQuadroHorario(new QuadroHorarioVO());
			setFiliacaoVO(new FiliacaoVO());
			setFormacaoAcademicaVO(new FormacaoAcademicaVO());
			setPessoaPreInscricaoCursoVO(new PessoaPreInscricaoCursoVO());
			setPessoaVO(obj.getPessoa());
			setCodigoTurno(0);
			obj.setNovoObj(Boolean.FALSE);
			setFuncionarioVO(obj);
			setConsultarPessoa(Boolean.FALSE);
			setImportarDadosPessoa(false);
			setMensagemID("msg_dados_editar");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}

	public void salarioCompostoSelecionado() {
		if (getFuncionarioCargoVO().getSalarioComposto()) {

			BigDecimal salarioProgressaoSalarialItem = getFacadeFactory().getProgressaoSalarialItemInterfaceFacade()
					.consultarSalarioPorNivelFaixaProgressao(getFuncionarioCargoVO().getNivelSalarial().getCodigo(),
							getFuncionarioCargoVO().getFaixaSalarial().getCodigo(),
							getFuncionarioCargoVO().getCargo().getProgressaoSalarial().getCodigo());

			BigDecimal salario = new BigDecimal(getFuncionarioCargoVO().getJornada()).divide(new BigDecimal(5))
					.multiply(salarioProgressaoSalarialItem);
			getFuncionarioCargoVO().setSalario(salario);
		} else {
			salarioPorFaixaSalarial();
		}
	}

	public String adicionarFuncionarioCargo() {
		try {

			if (!getFuncionarioCargoVO().getCodigo().equals(0)) {
				getFuncionarioCargoVO().getFuncionarioVO().setCodigo(getFuncionarioVO().getCodigo());
			}

			getFuncionarioCargoVO().montarCentroCusto(getFuncionarioVO());

			getFuncionarioCargoVO().setUnidade(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(
					funcionarioCargoVO.getUnidade().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,
					getUsuarioLogado()));
			getFuncionarioCargoVO().setCargo(getFacadeFactory().getCargoFacade().consultarPorChavePrimaria(
					getFuncionarioCargoVO().getCargo().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
					getUsuarioLogado()));
			getFuncionarioCargoVO().setFuncionarioVO(getFuncionarioVO());
			getFuncionarioVO().adicionarObjFuncionarioCargoVOs(getFuncionarioCargoVO());
			this.setFuncionarioCargoVO(new FuncionarioCargoVO());
			setFuncionarioCargoHistorico(new FuncionarioCargoVO());
			limparHistoricos();
			setMensagemID("msg_dados_adicionados");
			setAbrirpopup(true);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			return "RichFaces.$('panelSituacoesHistoricos').hide();";
		}
		return "";
	}

	public String editarFuncionarioCargo() {
		try {
			setFuncionarioCargoVOEditado(
					(FuncionarioCargoVO) context().getExternalContext().getRequestMap().get("funcionarioCargoItem"));
			getFuncionarioCargoVOEditado().setItemEmEdicao(true);
			if (Uteis.isAtributoPreenchido(getFuncionarioCargoVOEditado().getNivelSalarial())) {
				getFuncionarioCargoVOEditado().setNivelSalarial(
						getFacadeFactory().getNivelSalarialInterfaceFacade().consultarPorChavePrimaria(
								getFuncionarioCargoVOEditado().getNivelSalarial().getCodigo().longValue()));
			}

			if (Uteis.isAtributoPreenchido(getFuncionarioCargoVOEditado().getFaixaSalarial())) {
				getFuncionarioCargoVOEditado().setFaixaSalarial(
						getFacadeFactory().getFaixaSalarialInterfaceFacade().consultarPorChavePrimaria(
								getFuncionarioCargoVOEditado().getFaixaSalarial().getCodigo().longValue()));
			}

			// Clona o objeto da grid que sera editado para criar outra referencia de
			// memoria
			setFuncionarioCargoVO((FuncionarioCargoVO) SerializationUtils.clone(getFuncionarioCargoVOEditado()));
			setFuncionarioCargoHistorico((FuncionarioCargoVO) SerializationUtils.clone(getFuncionarioCargoVOEditado()));
			getFuncionarioCargoVO().getHistoricoSalarialVO().setSalario(getFuncionarioCargoVOEditado().getSalario());
			atualizarCargo();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public String removerFuncionarioCargo() {
		try {
			FuncionarioCargoVO obj = (FuncionarioCargoVO) context().getExternalContext().getRequestMap()
					.get("funcionarioCargoItem");
			getFuncionarioVO().excluirObjFuncionarioCargoVOs(obj.getCargo().getCodigo());
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public String fechar() {
		setMensagemDetalhada("", "");
		return Uteis.getCaminhoRedirecionamentoNavegacao("funcionarioCons");
	}
	
	public void realizarCriacaoUsuarioPorFuncionario() {
		try {
			if(getFuncionarioVO().isNaoNotificarInclusaoUsuario()) {
				getFacadeFactory().getFuncionarioFacade().realizarAtualizacaoNaoNotificarInsercaoUsuario(getFuncionarioVO(), getUsuarioLogadoClone());
			}else {
				getFacadeFactory().getFuncionarioFacade().realizarCriacaoUsuarioPorFuncionario(getFuncionarioVO(), getUsuarioPerfilAcessoVO(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogadoClone());
			}
			getFuncionarioVO().setCriarUsuario(false);
		} catch (Exception e) {
			getFuncionarioVO().setCriarUsuario(true);
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void fecharApresentarModalCriarUsuario() {
		try {
			getFuncionarioVO().setCriarUsuario(false);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
		
	}
	
	public String getApresentarModalCriarUsuario() {
		return getFuncionarioVO().isCriarUsuario() ? "RichFaces.$('panelUsuarioFuncionario').show();":"RichFaces.$('panelUsuarioFuncionario').hide();";
	}

	public boolean getApresentarCriarUsuario() {
		try {
			return ControleAcesso.verificarPermissaoFuncionalidadeUsuario("Funcionario_permitirCriarUsuario",
					getUsuarioLogado()) && getFuncionarioVO().getCodigo().intValue() > 0;
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			return false;
		}
	}

	public void validarPessoaCadastrada() {
		try {
			String msg = "";
			String cpf;

			if (getPessoaVO().getCPF().length() == 14) {
				cpf = getPessoaVO().getCPF();
				Boolean validacpf = getFacadeFactory().getConfiguracaoGeralSistemaFacade()
						.realizarVerificacaoValidarCpf(false, getUsuarioLogado());
				if (Uteis.isAtributoPreenchido(validacpf) && validacpf) {
					boolean cpfValido = Uteis.verificaCPF(cpf);
					if (!cpfValido) {
						setCpfInvalido(true);
						setEditarDados(false);
						setNaoFuncionario(false);
						throw new ConsistirException("O CPF não é VÁLIDO.");
					}
				}
				pessoaExistente = getFacadeFactory().getPessoaFacade().consultarPorCPFUnico(cpf, 0, "", false,
						Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());

			} else {
				setCpfInvalido(true);
				setEditarDados(false);
				setNaoFuncionario(false);
				throw new Exception("CPF inválido.");
			}
			if (pessoaExistente.getCodigo().intValue() != 0) {
				msg = "Já existe um ";
				if (pessoaExistente.getFuncionario().equals(Boolean.TRUE)) {
					msg += "Funcionário cadastrado com este CPF. Deseja editá-lo?";
					setPessoaVO(pessoaExistente);
					setNaoFuncionario(false);
					setCpfInvalido(false);
					setEditarDados(true);
				} else if (pessoaExistente.getProfessor().equals(Boolean.TRUE)) {
					msg += "Professor cadastrado com este CPF. Deseja editá-lo?";
					setPessoaVO(pessoaExistente);
					setNaoFuncionario(false);
					setCpfInvalido(false);
					setEditarDados(true);
				} else {
					if (pessoaExistente.getAluno().equals(Boolean.TRUE)) {
						msg += "Aluno cadastrado";
					} else if (pessoaExistente.getCandidato().equals(Boolean.TRUE)) {
						msg += "Candidato cadastrado";
					} else if (pessoaExistente.getMembroComunidade().equals(Boolean.TRUE)) {
						msg += "Membro da Comunidade cadastrado";
					}
					msg += " com este CPF. \nDeseja cadastrá-lo como funcionário?";
					setEditarDados(false);
					setCpfInvalido(false);
					setNaoFuncionario(true);
				}
				throw new Exception(msg);
			}
			setPessoaVO(new PessoaVO());
			getPessoaVO().setCPF(cpf);
			setConsultarPessoa(Boolean.FALSE);
			setImportarDadosPessoa(Boolean.FALSE);

		} catch (Exception e) {
			pessoaVO.setCPF("");
			setImportarDadosPessoa(Boolean.TRUE);
			setConsultarPessoa(Boolean.FALSE);
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void importarPessoaCadastrada() {
		try {
			getFuncionarioVO().setPessoa(getFacadeFactory().getPessoaFacade().consultaRapidaCompletaPorChavePrimaria(
					getPessoaExistente().getCodigo(), false, true, false, getUsuarioLogado()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		setPessoaVO(new PessoaVO());
		setPessoaVO(getFuncionarioVO().getPessoa());
		setImportarDadosPessoa(Boolean.FALSE);
		setConsultarPessoa(Boolean.FALSE);
		setMensagemDetalhada("", "");
	}

	public void naoImportarPessoaCadastrada() {
		setPessoaVO(new PessoaVO());
		setImportarDadosPessoa(Boolean.FALSE);
		setConsultarPessoa(Boolean.TRUE);
		setMensagemDetalhada("", "");
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>Nacionalidade</code>. Buscando todos os objetos correspondentes a
	 * entidade <code>Paiz</code>. Esta rotina não recebe parâmetros para filtragem
	 * de dados, isto é importante para a inicialização dos dados da tela para o
	 * acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemNacionalidade() {
		try {
			montarListaSelectItemNacionalidade("");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>Nacionalidade</code>.
	 */
	public void montarListaSelectItemNacionalidade(String prm) {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarPaizPorNome(prm);
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				PaizVO obj = (PaizVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
			}
			setListaSelectItemNacionalidade(objs);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>nome</code> Este atributo é uma lista
	 * (<code>List</code>) utilizada para definir os valores a serem apresentados no
	 * ComboBox correspondente
	 */
	public List consultarPaizPorNome(String nomePrm) {
		List lista = new ArrayList<>();
		try {
			lista = getFacadeFactory().getPaizFacade().consultarPorNome(nomePrm, false, getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}

		return lista;
	}

	public void montarListaSelectItemAreaConhecimento(String prm) {
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
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		} finally {
			ordenador = null;
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>Nacionalidade</code>. Buscando todos os objetos correspondentes a
	 * entidade <code>Paiz</code>. Esta rotina não recebe parâmetros para filtragem
	 * de dados, isto é importante para a inicialização dos dados da tela para o
	 * acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemAreaConhecimento() {
		try {
			montarListaSelectItemAreaConhecimento("");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>nome</code> Este atributo é uma lista
	 * (<code>List</code>) utilizada para definir os valores a serem apresentados no
	 * ComboBox correspondente
	 */
	public List consultarAreaConhecimentoPorNome(String nomePrm) {
		List lista = new ArrayList<>();
		try {
			lista = getFacadeFactory().getAreaConhecimentoFacade().consultarPorNome(nomePrm, false, getUsuarioLogado());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lista;
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
			getPessoaVO().adicionarObjFiliacaoVOs(getFiliacaoVO());
			this.setFiliacaoVO(new FiliacaoVO());
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
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
			setMensagemID("msg_dados_selecionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/*
	 * Método responsável por remover um novo objeto da classe <code>Filiacao</code>
	 * do objeto <code>pessoaVO</code> da classe <code>Pessoa</code>
	 */
	public void removerFiliacao() {
		FiliacaoVO obj = (FiliacaoVO) context().getExternalContext().getRequestMap().get("filiacaoItem");
		try {
			getPessoaVO().excluirObjFiliacaoVOs(obj.getNome());
		} catch (Exception e) {
			e.printStackTrace();
		}
		setMensagemID("msg_dados_excluidos");
	}

	/*
	 * Método responsável por adicionar um novo objeto da classe
	 * <code>FormacaoAcademica</code> para o objeto <code>pessoaVO</code> da classe
	 * <code>Pessoa</code>
	 */
	public void adicionarFormacaoAcademica() {
		try {
			if (!getPessoaVO().getCodigo().equals(0)) {
				formacaoAcademicaVO.setPessoa(getPessoaVO().getCodigo());
			}
			getFormacaoAcademicaVO().setFuncionario(true);
			getPessoaVO().adicionarObjFormacaoAcademicaVOs(getFormacaoAcademicaVO());
			this.setFormacaoAcademicaVO(new FormacaoAcademicaVO());
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	/*
	 * Método responsável por disponibilizar dados de um objeto da classe
	 * <code>FormacaoAcademica</code> para edição pelo usuário.
	 */
	public void editarFormacaoAcademica() {
		FormacaoAcademicaVO obj = (FormacaoAcademicaVO) context().getExternalContext().getRequestMap()
				.get("formacaoAcademicaItem");
		setFormacaoAcademicaVO(obj);
		// return "editar";
	}

	/*
	 * Método responsável por remover um novo objeto da classe
	 * <code>FormacaoAcademica</code> do objeto <code>pessoaVO</code> da classe
	 * <code>Pessoa</code>
	 */
	public void removerFormacaoAcademica() {
		FormacaoAcademicaVO obj = (FormacaoAcademicaVO) context().getExternalContext().getRequestMap()
				.get("formacaoAcademicaItem");
		try {
			setMatriculaApresentarExclusaoFormacaoAcademica(getFacadeFactory().getMatriculaFacade()
					.consultarPossuiFormacaoAcademicaVinculadaMatricula(obj.getCodigo(), false, getUsuarioLogado()));
			if (Uteis.isAtributoPreenchido(getMatriculaApresentarExclusaoFormacaoAcademica())) {
				throw new Exception("A Formação Acadêmica Não Pode Ser Excluída Pois Está Vinculada a Matrícula "
						+ getMatriculaApresentarExclusaoFormacaoAcademica());
			}
			getPessoaVO().excluirObjFormacaoAcademicaVOs(obj.getCurso());
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemID(e.getMessage());
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}

	}

	/*
	 * Método responsável por adicionar um novo objeto da classe
	 * <code>FormacaoAcademica</code> para o objeto <code>pessoaVO</code> da classe
	 * <code>Pessoa</code>
	 */
	public void adicionarPessoaPreInscricaoCurso() {
		try {
			if (!getPessoaVO().getCodigo().equals(0)) {
				getPessoaPreInscricaoCursoVO().setPessoa(getPessoaVO().getCodigo());
			}
			getPessoaVO().adicionarObjPessoaPreInscricaoCursoVOs(getPessoaPreInscricaoCursoVO());
			this.setPessoaPreInscricaoCursoVO(new PessoaPreInscricaoCursoVO());
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	/*
	 * Método responsável por disponibilizar dados de um objeto da classe
	 * <code>FormacaoAcademica</code> para edição pelo usuário.
	 */
	public void editarPessoaPreInscricaoCurso() {
		PessoaPreInscricaoCursoVO obj = (PessoaPreInscricaoCursoVO) context().getExternalContext().getRequestMap()
				.get("pessoaPreInscricaoCursoItem");
		setPessoaPreInscricaoCursoVO(obj);
		// return "editar";
	}

	/*
	 * Método responsável por remover um novo objeto da classe
	 * <code>FormacaoAcademica</code> do objeto <code>pessoaVO</code> da classe
	 * <code>Pessoa</code>
	 */
	public void removerPessoaPreInscricaoCurso() {
		PessoaPreInscricaoCursoVO obj = (PessoaPreInscricaoCursoVO) context().getExternalContext().getRequestMap()
				.get("pessoaPreInscricaoCursoItem");
		try {
			getPessoaVO().excluirObjPessoaPreInscricaoCursoVOs(obj.getCurso().getCodigo());
		} catch (Exception e) {
			e.printStackTrace();
		}
		setMensagemID("msg_dados_excluidos");
	}

	public void consultarDisciplina() {
		try {
			List objs = new ArrayList(0);
			if (getValorConsultaDisciplina().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			if (getCampoConsultaDisciplina().equals("codigo")) {
				int valorInt = Integer.parseInt(getValorConsultaDisciplina());
				DisciplinaVO disciplina = getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(new Integer(valorInt), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				if (disciplina != null && (!disciplina.getCodigo().equals(0))) {
					objs.add(disciplina);
				}
			}
			if (getCampoConsultaDisciplina().equals("nome")) {
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorNome(getValorConsultaDisciplina(), false,
						Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaDisciplina().equals("areaConhecimento")) {
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorNomeAreaConhecimento(
						getValorConsultaDisciplina(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaDisciplina(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaDisciplina(new ArrayList(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void adicionarDisciplina() {
		try {
			setMensagemDetalhada("");
			DisciplinaVO disciplina = (DisciplinaVO) context().getExternalContext().getRequestMap()
					.get("disciplinaItem");
			getDisciplinasInteresseVO().setDisciplina(disciplina);
			adicionarDisciplinasInteresse();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public List getTipoConsultaComboDisciplina() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("areaConhecimento", "Área de Conhecimento"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	public void adicionarDisciplinasInteresse() {
		try {
			if (!getPessoaVO().getCodigo().equals(0)) {
				disciplinasInteresseVO.setProfessor(getPessoaVO().getCodigo());
			}
			if (getDisciplinasInteresseVO().getDisciplina().getCodigo().intValue() != 0) {
				Integer campoConsulta = getDisciplinasInteresseVO().getDisciplina().getCodigo();
				DisciplinaVO disciplina = getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(
						campoConsulta, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				getDisciplinasInteresseVO().setDisciplina(disciplina);
			}
			getPessoaVO().adicionarObjDisciplinasInteresseVOs(getDisciplinasInteresseVO());
			removerDisciplinaListaConsulta();
			this.setDisciplinasInteresseVO(new DisciplinasInteresseVO());
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void removerDisciplinaListaConsulta() {
		Iterator i = getListaConsultaDisciplina().iterator();
		int index = 0;
		while (i.hasNext()) {
			DisciplinaVO objExistente = (DisciplinaVO) i.next();
			if (objExistente.getCodigo().equals(getDisciplinasInteresseVO().getDisciplina().getCodigo())) {
				getListaConsultaDisciplina().remove(index);
				return;
			}
			index++;
		}
	}

	/*
	 * Método responsável por disponibilizar dados de um objeto da classe
	 * <code>DisciplinasInteresse</code> para edição pelo usuário.
	 */
	public void editarDisciplinasInteresse() {
		DisciplinasInteresseVO obj = (DisciplinasInteresseVO) context().getExternalContext().getRequestMap()
				.get("disciplinasInteresseItem");
		setDisciplinasInteresseVO(obj);
	}

	/*
	 * Método responsável por remover um novo objeto da classe
	 * <code>DisciplinasInteresse</code> do objeto <code>pessoaVO</code> da classe
	 * <code>Pessoa</code>
	 */
	public void removerDisciplinasInteresse() {
		DisciplinasInteresseVO obj = (DisciplinasInteresseVO) context().getExternalContext().getRequestMap()
				.get("disciplinasInteresseItem");
		try {
			getPessoaVO().excluirObjDisciplinasInteresseVOs(obj.getDisciplina().getCodigo());
		} catch (Exception e) {
			e.printStackTrace();
		}

		setMensagemID("msg_dados_excluidos");
	}

	public void consultarDisciplinaPorChavePrimaria() {
		try {
			Integer campoConsulta = disciplinasInteresseVO.getDisciplina().getCodigo();
			DisciplinaVO disciplina = getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(campoConsulta,
					Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			disciplinasInteresseVO.getDisciplina().setNome(disciplina.getNome());
			this.setDisciplina_Erro("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			disciplinasInteresseVO.getDisciplina().setNome("");
			disciplinasInteresseVO.getDisciplina().setCodigo(0);
			this.setDisciplina_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}
	}

	public List consultarDisciplinaSuggestionbox(Object event) {
		try {
			String valor = event.toString();
			List lista = getFacadeFactory().getDisciplinaFacade().consultarPorNome(valor, false,
					Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			this.setDisciplina_Erro("");
			disciplinasInteresseVO.getDisciplina().setNome("");
			return lista;
		} catch (Exception e) {
			this.setDisciplina_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
			return new ArrayList(0);
		}
	}

	/*
	 * Método responsável por disponibilizar dados de um objeto da classe
	 * <code>DisponibilidadeHorario</code> para edição pelo usuário.
	 */
	public void editarDisponibilidadeHorario() {
		DisponibilidadeHorarioVO obj = (DisponibilidadeHorarioVO) context().getExternalContext().getRequestMap()
				.get("disponibilidadeHorario");
		setDisponibilidadeHorarioVO(obj);
	}

	/*
	 * Método responsável por remover um novo objeto da classe
	 * <code>DisponibilidadeHorario</code> do objeto <code>pessoaVO</code> da classe
	 * <code>Pessoa</code>
	 */
	public void removerDisponibilidadeHorario() {
		try {
			getPessoaVO().excluirObjDisponibilidadeHorarioVOs(getDisponibilidadeHorarioVO());
		} catch (Exception e) {
			e.printStackTrace();
		}

		setMensagemID("msg_dados_excluidos");
	}

	public void consultarUnificarFuncionario() {
		try {
			setListaConsultaFuncionarioUnificar(getFacadeFactory().getFuncionarioFacade().consultarFuncionarioUnificar(
					getValorConsultaFuncionarioUnificar(), getCampoConsultaFuncionarioUnificar(), false,
					Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			getListaConsultaFuncionarioUnificar().clear();
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void selecionarFuncionarioUnificar() {
		FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioUnificar");
		this.setFuncionarioUnificarVO(obj);
		Uteis.liberarListaMemoria(this.getListaConsultaFuncionarioUnificar());
		this.setValorConsultaFuncionarioUnificar(null);
		this.setValorConsultaFuncionarioUnificar(null);
	}

	public void unificarFuncionario() {
		try {
			getFacadeFactory().getFuncionarioFacade().unificarFuncionario(getFuncionarioVO().getCodigo(),
					getFuncionarioUnificarVO().getCodigo(), getUsuarioLogado());
			setFuncionarioUnificarVO(null);
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setFuncionarioUnificarVO(null);
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void selecionarTurno() {
		try {
			TurnoVO obj = (TurnoVO) context().getExternalContext().getRequestMap().get("turnoItem");
			setCodigoTurno(obj.getCodigo());
			setHorarioSimples(true);
			montarListaDisponibilidadeTurno(false);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void selecionarTodosTurnos() {
		try {
			Iterator i = getListaSelectItemTurno().iterator();
			while (i.hasNext()) {
				TurnoVO obj = new TurnoVO();
				obj = (TurnoVO) i.next();
				setCodigoTurno(obj.getCodigo());
				montarListaDisponibilidadeTurno(true);
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void selecionarNivelSalarial() {
		getFuncionarioCargoVO().setFaixaSalarial(new FaixaSalarialVO());
		getFuncionarioCargoVO().setSalario(BigDecimal.ZERO);
		setListaSelectItemFaixaSalarial(new ArrayList<>());
	}

	public Integer getTamanhoListaTurno() {
		if (getListaSelectItemTurno() == null) {
			return 0;
		}
		return getListaSelectItemTurno().size();
	}

	public void montarDadosHorarioProfessorTurno() {
		try {
			montarListaDisponibilidadeTurno(Boolean.FALSE);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void montarListaDisponibilidadeTurno(Boolean detalhado) {
		if (getQuadroHorario().getTurno().getCodigo().intValue() != 0) {
			adicionarQuadroHorario();
			setQuadroHorario(new QuadroHorarioVO());
		}
		try {
			setQuadroHorario(getPessoaVO().consultarObjQuadroHorarioVO(getCodigoTurno()));
			if (getQuadroHorario().getTurno().getCodigo().equals(0) && getCodigoTurno().intValue() != 0) {
				for (HorarioProfessorVO horarioProfessorVO : getPessoaVO().getHorarioProfessorVOs()) {
					if (horarioProfessorVO.getTurno().getCodigo().intValue() == getCodigoTurno().intValue()) {
						getQuadroHorario().setTurno(horarioProfessorVO.getTurno());
						break;
					}
				}
				if (getQuadroHorario().getTurno().getCodigo() == 0) {
					HorarioProfessorVO horarioProfessorVO = getFacadeFactory().getHorarioProfessorFacade()
							.consultarRapidaHorarioProfessorTurno(getPessoaVO().getCodigo(), getCodigoTurno(),
									getUsuarioLogado());
					if (horarioProfessorVO.getCodigo() == 0) {
						getQuadroHorario().setTurno(getFacadeFactory().getTurnoFacade().consultarPorChavePrimaria(
								codigoTurno, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
					} else {
						getPessoaVO().getHorarioProfessorVOs().add(horarioProfessorVO);
						getQuadroHorario().setTurno(horarioProfessorVO.getTurno());
					}
				}

				atualizarQuadroHorario(false);
			} else if (getCodigoTurno().intValue() == 0) {
				setQuadroHorario(new QuadroHorarioVO());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void atualizarQuadroHorario(Boolean detalhado) {
		try {
			List lista = getPessoaVO().getHorarioProfessorVOs();
			Iterator i = lista.iterator();
			while (i.hasNext()) {
				HorarioProfessorVO obj = (HorarioProfessorVO) i.next();
				if (obj.getTurno().getCodigo().equals(getQuadroHorario().getTurno().getCodigo())) {
					setQuadroHorario(getFacadeFactory().getHorarioProfessorFacade().atualizarDadosQuadroHorario(obj,
							getQuadroHorario(), detalhado, null, null, getUsuarioLogado()));
					adicionarQuadroHorario();
					return;
				}
			}
			getFacadeFactory().getHorarioProfessorFacade().montarDadosListaQuadroHorarioVO(getQuadroHorario(),
					getUsuarioLogado());
			adicionarQuadroHorario();
			getPessoaVO().montarListaHorarioProfessor();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void montarDadosHorarioProfessorDia() {
		try {
			for (HorarioProfessorDiaVO dia : getQuadroHorario().getHorarioProfessorVO().getHorarioProfessorDiaVOs()) {
				if (dia.getHorarioProfessorDiaItemVOs().isEmpty()) {
					getFacadeFactory().getHorarioProfessorDiaFacade().montarDadosHorarioProfessorDiaItemVOs(dia,
							getQuadroHorario().getHorarioProfessorVO(),
							getQuadroHorario().getHorarioProfessorVO().getTurno(), null, null, getUsuarioLogado());
				}
				HorarioProfessorDia.montarDadosHorarioTurmaDiaItem(dia, "");
			}
			getFacadeFactory().getHorarioProfessorFacade()
					.inicializarDadosCalendario(getQuadroHorario().getHorarioProfessorVO(), getUsuarioLogado());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void montarQuadroHorarioTurno() {
		selecionarTurno();
		montarListaDisponibilidadeTurnoDetalhado();
	}

	public void verHorarioSimples() {
		setHorarioSimples(Boolean.TRUE);
	}

	public void verHorarioDetalhado() {
		setHorarioSimples(Boolean.FALSE);
	}

	public void montarListaDisponibilidadeTurnoDetalhado() {
		try {
			if (getCodigoTurno().intValue() != 0) {
				atualizarQuadroHorario(Boolean.TRUE);
				setDiaSemana("02");
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public List getListaSelectItemDiaSemanaDisponibilidadeHorario() {
		List objs = new ArrayList(0);
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

	public void consultarTurnoPorChavePrimaria() {
		try {
			Integer campoConsulta = disponibilidadeHorarioVO.getTurno().getCodigo();
			TurnoVO turno = getFacadeFactory().getTurnoFacade().consultarPorChavePrimaria(campoConsulta,
					Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			disponibilidadeHorarioVO.getTurno().setNome(turno.getNome());
			this.setTurno_Erro("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			disponibilidadeHorarioVO.getTurno().setNome("");
			disponibilidadeHorarioVO.getTurno().setCodigo(0);
			this.setTurno_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}
	}

	public List consultarTurnoSuggestionbox(Object event) {
		try {
			String valor = event.toString();
			List lista = getFacadeFactory().getTurnoFacade().consultarPorNome(valor, false,
					Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			this.setTurno_Erro("");
			disponibilidadeHorarioVO.getTurno().setNome("");
			return lista;
		} catch (Exception e) {
			this.setTurno_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
			return new ArrayList(0);
		}
	}

	/**
	 * Método responsável por consultar Cidade <code>Cidade/code>.
	 * Buscando todos os objetos correspondentes a entidade <code>Cidade</code>.
	 * Esta rotina não recebe parâmetros para filtragem de dados, isto é importante
	 * para a inicialização dos dados da tela para o acionamento por meio
	 * requisições Ajax.
	 */
	public void consultarCidade() {
		try {
			List<CidadeVO> objs = new ArrayList<CidadeVO>(0);
			if (getCampoConsultaCidade().equals("codigo")) {
				if (getValorConsultaCidade().equals("")) {
					setValorConsultaCidade("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaCidade());
				objs = getFacadeFactory().getCidadeFacade().consultarPorCodigo(new Integer(valorInt), false,
						getUsuarioLogado());
			}
			if (getCampoConsultaCidade().equals("nome")) {
				objs = getFacadeFactory().getCidadeFacade().consultarPorNome(getValorConsultaCidade(), false,
						getUsuarioLogado());
			}

			setListaConsultaCidade(objs);
			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			setListaConsultaCidade(new ArrayList(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());

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

	/**
	 * Método responsável por carregar umaCombobox com os tipos de pesquisa de
	 * Cidade <code>Cidade/code>.
	 */
	public List<SelectItem> getTipoConsultaCidade() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	/**
	 * Método responsável por consultar Naturalidade <code>Cidade/code>.
	 * Buscando todos os objetos correspondentes a entidade <code>Cidade</code>.
	 * Esta rotina não recebe parâmetros para filtragem de dados, isto é importante
	 * para a inicialização dos dados da tela para o acionamento por meio
	 * requisições Ajax.
	 */
	public void consultarNaturalidade() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaNaturalidade().equals("codigo")) {
				if (getValorConsultaNaturalidade().equals("")) {
					setValorConsultaNaturalidade("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaNaturalidade());
				objs = getFacadeFactory().getCidadeFacade().consultarPorCodigo(new Integer(valorInt), false,
						getUsuarioLogado());
			}
			if (getCampoConsultaNaturalidade().equals("nome")) {
				objs = getFacadeFactory().getCidadeFacade().consultarPorNome(getValorConsultaNaturalidade(), false,
						getUsuarioLogado());
			}

			setListaConsultaNaturalidade(objs);
			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			setListaConsultaNaturalidade(new ArrayList(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());

		}
	}

	/**
	 * Método responsável por selecionar o objeto CidadeVO em Naturalidade
	 * <code>Cidade/code>.
	 */
	public void selecionarNaturalidade() {
		CidadeVO obj = (CidadeVO) context().getExternalContext().getRequestMap().get("naturalidade");
		getPessoaVO().setNaturalidade(obj);
		getListaConsultaNaturalidade().clear();
		this.setValorConsultaNaturalidade("");
		this.setCampoConsultaNaturalidade("");
	}

	/**
	 * Método responsável por carregar umaCombobox com os tipos de pesquisa de
	 * Cidade para Naturalidade <code>Cidade/code>.
	 */
	public List getTipoConsultaNaturalidade() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	public void montarListaSelectItemTurno(String prm) {
		List resultadoConsulta = consultarTurnoPorNome(prm);
		setListaSelectItemTurno(resultadoConsulta);
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>Cargo</code>. Buscando todos os objetos correspondentes a entidade
	 * <code>Cargo</code>. Esta rotina não recebe parâmetros para filtragem de
	 * dados, isto é importante para a inicialização dos dados da tela para o
	 * acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemTurno() {
		try {
			montarListaSelectItemTurno("");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void adicionarQuadroHorario() {
		try {
			getPessoaVO().adicionarObjQuadroHorarioVOs(getQuadroHorario());
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void selecionarHorarioTurmaDia() {
		setHorarioProfessorDiaVO(
				(HorarioProfessorDiaVO) context().getExternalContext().getRequestMap().get("horarioProgramacaoDia"));
	}

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>nome</code> Este atributo é uma lista
	 * (<code>List</code>) utilizada para definir os valores a serem apresentados no
	 * ComboBox correspondente
	 */
	public List<TurnoVO> consultarTurnoPorNome(String nomePrm) {
		List<TurnoVO> lista = new ArrayList<>();
		try {
			lista = getFacadeFactory().getTurnoFacade().consultarPorNome(nomePrm, false,
					Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lista;
	}

	public void irPaginaInicial() {
		this.consultar();
	}

	public void irPaginaAnterior() {
		controleConsulta.setPaginaAtual(controleConsulta.getPaginaAtual() - 1);
		this.consultar();

	}

	public void irPaginaPosterior() {
		controleConsulta.setPaginaAtual(controleConsulta.getPaginaAtual() + 1);
		this.consultar();
	}

	public void irPaginaFinal() {
		controleConsulta.setPaginaAtual(controleConsulta.getNrTotalPaginas());
		this.consultar();
	}

	/**
	 * Método responsável por processar a consulta na entidade <code>Pessoa</code>
	 * por meio de sua respectiva chave primária. Esta rotina é utilizada
	 * fundamentalmente por requisições Ajax, que realizam busca pela chave primária
	 * da entidade montando automaticamente o resultado da consulta para
	 * apresentação.
	 */
	public void consultarPessoaPorChavePrimaria() {
		try {
			Integer campoConsulta = funcionarioVO.getPessoa().getCodigo();
			PessoaVO pessoa = getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(campoConsulta, false,
					Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			funcionarioVO.getPessoa().setNome(pessoa.getNome());
			this.setPessoa_Erro("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			funcionarioVO.getPessoa().setNome("");
			funcionarioVO.getPessoa().setCodigo(0);
			this.setPessoa_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>Cargo</code>.
	 */
	public void montarListaSelectItemCargo(String prm) {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarCargoPorNome(prm);
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				CargoVO obj = (CargoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
			}
			setListaSelectItemCargo(objs);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>Cargo</code>. Buscando todos os objetos correspondentes a entidade
	 * <code>Cargo</code>. Esta rotina não recebe parâmetros para filtragem de
	 * dados, isto é importante para a inicialização dos dados da tela para o
	 * acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemCargo() {
		try {
			montarListaSelectItemCargo("");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>nome</code> Este atributo é uma lista
	 * (<code>List</code>) utilizada para definir os valores a serem apresentados no
	 * ComboBox correspondente
	 */
	public List consultarCargoPorNome(String nomePrm) {
		List lista = new ArrayList<>();
		try {
			lista = getFacadeFactory().getCargoFacade().consultaRapidaPorNome(nomePrm, false,
					Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lista;
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>Departamento</code>.
	 */
	public void montarListaSelectItemDepartamento(String prm) {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarDepartamentoPorNome(prm);
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				DepartamentoVO obj = (DepartamentoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
			}
			setListaSelectItemDepartamento(objs);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>Departamento</code>. Buscando todos os objetos correspondentes a
	 * entidade <code>Departamento</code>. Esta rotina não recebe parâmetros para
	 * filtragem de dados, isto é importante para a inicialização dos dados da tela
	 * para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemDepartamento() {
		try {
			montarListaSelectItemDepartamento("");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>nome</code> Este atributo é uma lista
	 * (<code>List</code>) utilizada para definir os valores a serem apresentados no
	 * ComboBox correspondente
	 */
	public List consultarDepartamentoPorNome(String nomePrm) {
		List lista = new ArrayList<>();
		try {
			lista = getFacadeFactory().getDepartamentoFacade().consultarPorNome(nomePrm, false,
					Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lista;
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>UnidadeEnsino</code>.
	 */
	public void montarListaSelectItemUnidadeEnsino(String prm) {
		try {
			List resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
			setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>UnidadeEnsino</code>. Buscando todos os objetos correspondentes a
	 * entidade <code>UnidadeEnsino</code>. Esta rotina não recebe parâmetros para
	 * filtragem de dados, isto é importante para a inicialização dos dados da tela
	 * para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemUnidadeEnsino() {
		try {
			montarListaSelectItemUnidadeEnsino("");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>nome</code> Este atributo é uma lista
	 * (<code>List</code>) utilizada para definir os valores a serem apresentados no
	 * ComboBox correspondente
	 */
	public List consultarUnidadeEnsinoPorNome(String nomePrm) {
		List lista = new ArrayList<>();
		try {
			lista = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorNome(nomePrm,
					getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,
					getUsuarioLogado());
		} catch (Exception e) {
		}
		return lista;
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>Cidade</code>.
	 */
	public void montarListaSelectItemPerfilEconomico(String prm) {
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
			e.printStackTrace();
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
			e.printStackTrace();
		}
	}

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>nome</code> Este atributo é uma lista
	 * (<code>List</code>) utilizada para definir os valores a serem apresentados no
	 * ComboBox correspondente
	 */
	public List consultarPerfilEconomicoPorNome(String nomePrm) {
		List lista = new ArrayList<>();
		try {
			lista = getFacadeFactory().getPerfilEconomicoFacade().consultarPorNome(nomePrm, false,
					Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lista;
	}

	/**
	 * Método responsável por inicializar a lista de valores
	 * (<code>SelectItem</code>) para todos os ComboBox's.
	 */
	public void inicializarListasSelectItemTodosComboBox() {
		montarListaSelectItemUnidadeEnsino();
		montarListaSelectItemDepartamento();
		montarListaSelectItemCargo();
		montarListaSelectItemTurno();
		montarListaSelectItemNacionalidade();
		montarListaSelectItemAreaConhecimento();
		montarListaSelectItemPerfilEconomico();
		montarListaSelectItemSituacaoFuncionario();
		montarListaSelectItemFormaContratacao();
		montarListaSelectItemTipoRecebimento();
	}

	public void atualizarCargo() {
		try {
			getFuncionarioCargoVO().setCargo(getFacadeFactory().getCargoFacade().consultarPorChavePrimaria(
					getFuncionarioCargoVO().getCargo().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS,
					getUsuarioLogado()));
		} catch (Exception e) {
			getFuncionarioCargoVO().setCargo(new CargoVO());
		}
	}

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do ComboBox
	 * correspondente ao atributo <code>estadoEmissaoRG</code>
	 */
	public List getListaSelectItemEstadoEmissaoRGPessoa() {
		List objs = new ArrayList(0);
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

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do ComboBox
	 * correspondente ao atributo <code>estadoCivil</code>
	 */
	public List getListaSelectItemEstadoCivilPessoa() {
		List objs = new ArrayList(0);
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

	public List getListaSelectItemAtuaComoDocente() {
		List objs = new ArrayList(0);
		Hashtable atuaComoDocente = (Hashtable) Dominios.getAtuaComoDocente();
		Enumeration keys = atuaComoDocente.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) atuaComoDocente.get(value);
			objs.add(new SelectItem(value, label));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		return objs;
	}

	/*
	 * Método responsável por apresentar as abas Disciplinas de Interesse e Horários
	 * Livres
	 */
	public void renderizarTela() {
		if (getFuncionarioVO().getPessoa().getProfessor().booleanValue()) {
			setControladorTela(Boolean.TRUE);
			setPanelDirecionarUsuario("richTabDadosFuncionais");
			pessoaVO.setProfessor(Boolean.TRUE);
			pessoaVO.setFuncionario(Boolean.TRUE);
		} else {
			setControladorTela(Boolean.FALSE);
			pessoaVO.setProfessor(Boolean.FALSE);
			pessoaVO.setFuncionario(Boolean.TRUE);
			pessoaVO.setAtuaComoDocente("FU");
		}
		preencherListaDocumentacaoPessoaProfessor();
	}

	public boolean getApresentarPaletaProfessor() {
		try {
			return (getFuncionarioVO().getPessoa().getProfessor().booleanValue());
		} catch (Exception e) {
			return false;
		}
	}

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do ComboBox
	 * correspondente ao atributo <code>sexo</code>
	 */
	public List getListaSelectItemSexoPessoa() {
		List objs = new ArrayList(0);
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

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do ComboBox
	 * correspondente ao atributo <code>escolaridade</code>
	 */
	public List getListaSelectItemEscolaridadeFormacaoAcademica() {
		List objs = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(NivelFormacaoAcademica.class, false);
		return objs;
	}

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do ComboBox
	 * correspondente ao atributo <code>situacao</code>
	 */
	public List getListaSelectItemSituacaoFormacaoAcademica() {
		List objs = new ArrayList(0);
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

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do ComboBox
	 * correspondente ao atributo <code>tipo</code>
	 */
	public List getListaSelectItemTipoFiliacao() {
		List objs = new ArrayList(0);
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

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List<SelectItem> tipoConsultaCombo;
	public List<SelectItem> getTipoConsultaCombo() {
		if(tipoConsultaCombo == null) {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		// itens.add(new SelectItem("codigo", "Código"));
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		// itens.add(new SelectItem("nomeCidade", "Cidade"));
		itens.add(new SelectItem("CPF", "CPF"));
		itens.add(new SelectItem("emailInstitucional", "E-mail Institucional"));
		itens.add(new SelectItem("cargo", "Cargo"));
		itens.add(new SelectItem("departamento", "Departamento"));
		itens.add(new SelectItem("unidadeEnsino", "Unidade de Ensino"));
		itens.add(new SelectItem("secao", "Seção"));
		itens.add(new SelectItem("formaContratacao", "Forma de contratação"));
		tipoConsultaCombo = itens;
		}
		return tipoConsultaCombo;
	}

	public List getTipoConsultaComboFuncionarioUnificar() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("CPF", "CPF"));
		return itens;
	}

	/**
	 * @return the permitirUnificarProspectsDuplicados
	 */
	public Boolean getPermitirUnificarFuncionariosDuplicados() {
		if (permitirUnificarFuncionariosDuplicados == null) {
			try {
				ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico(
						"Funcionario_unificarFuncionariosDuplicados", getUsuarioLogado());
				permitirUnificarFuncionariosDuplicados = Boolean.TRUE;
			} catch (Exception e) {
				permitirUnificarFuncionariosDuplicados = Boolean.FALSE;
			}
		}
		return permitirUnificarFuncionariosDuplicados;
	}

	/**
	 * @param permitirUnificarProspectsDuplicados the
	 *                                            permitirUnificarProspectsDuplicados
	 *                                            to set
	 */
	public void setPermitirUnificarFuncionariosDuplicados(Boolean permitirUnificarFuncionariosDuplicados) {
		this.permitirUnificarFuncionariosDuplicados = permitirUnificarFuncionariosDuplicados;
	}

	public String getMascaraConsulta() {
		if (getControleConsulta().getCampoConsulta().equals("CPF")) {
			return "return mascara(this.form,'formCadastro:valorConsulta','999.999.999-99',event)";
		}
		return "";
	}

	public void limparMatricula() {
		if (getFuncionarioVO().getNovoObj()) {
			getFuncionarioVO().setMatricula("");
		}
	}

	public void limparDadosSecaoFolhaPagamento() {
		getFuncionarioCargoVO().setSecaoFolhaPagamento(new SecaoFolhaPagamentoVO());
	}

	public void limparDadosEvento() {
		getDependenteVO().setEventoFolhaPagamento(new EventoFolhaPagamentoVO());
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes de
	 * uma consulta.
	 */
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setListaConsulta(new ArrayList(0));
		setMensagemID("msg_entre_prmconsulta");

		return Uteis.getCaminhoRedirecionamentoNavegacao("funcionarioCons");
	}

	public void montarListaContaPagar() {
		try {
			List objs = new ArrayList(0);
			objs = getFacadeFactory().getContaPagarFacade()
					.consultarPorCodigoNomeFavorecidoDataVencimentoEdicaoFuncionario(
							getFuncionarioVO().getPessoa().getCodigo(), "", getControleConsulta().getDataIni(),
							getControleConsulta().getDataFim(), this.getUnidadeEnsinoLogado().getCodigo(), false,
							Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			setListaContaPagar(objs);
			calcularTotalPagarTotalPago();
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void calcularTotalPagarTotalPago() {
		setTotalPagar(0.0);
		setTotalPago(0.0);
		for (ContaPagarVO contaPagar : (List<ContaPagarVO>) getListaContaPagar()) {
			if (contaPagar.getSituacao().equals(SituacaoFinanceira.A_PAGAR.getValor())) {
				setTotalPagar(getTotalPagar() + contaPagar.getValor());
			}
			if (contaPagar.getSituacao().equals(SituacaoFinanceira.PAGO.getValor())) {
				setTotalPago(getTotalPago() + contaPagar.getValor());
			}
		}
	}

	public void carregarFiliacaoPessoa() {
		try {
			setFiliacaoVO(getFacadeFactory().getFiliacaoFacade().carregarApenasUmPorCPF(getFiliacaoVO(), getPessoaVO(),
					true, getUsuarioLogado()));
			if (getFiliacaoVO().getPais().getCodigo().intValue() == 0 || getFiliacaoVO().getPais().getCEP().isEmpty()) {
				getFiliacaoVO().getPais().setCEP(getPessoaVO().getCEP());
				getFiliacaoVO().getPais().setEndereco(getPessoaVO().getEndereco());
				getFiliacaoVO().getPais().setSetor(getPessoaVO().getSetor());
				getFiliacaoVO().getPais().setNumero(getPessoaVO().getNumero());
				getFiliacaoVO().getPais().setComplemento(getPessoaVO().getComplemento());
				getFiliacaoVO().getPais().setCidade(getPessoaVO().getCidade());
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void carregarEnderecoPessoaFiliacao() {
		try {
			getFacadeFactory().getEnderecoFacade().carregarEndereco(getFiliacaoVO().getPais(), getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
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
				objs = getFacadeFactory().getCidadeFacade().consultarPorCodigo(new Integer(valorInt), false,
						getUsuarioLogado());
			}
			if (getCampoConsultaFiliacaoCidade().equals("nome")) {
				if (getValorConsultaFiliacaoCidade().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getCidadeFacade().consultarPorNome(getValorConsultaFiliacaoCidade(), false,
						getUsuarioLogado());
			}
			if (getCampoConsultaFiliacaoCidade().equals("estado")) {
				objs = getFacadeFactory().getCidadeFacade().consultarPorSiglaEstado(getValorConsultaFiliacaoCidade(),
						false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}

			setListaConsultaFiliacaoCidade(objs);
			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			setListaConsultaCidade(new ArrayList(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());

		}
	}

	public void selecionarFiliacaoCidade() {
		try {
			CidadeVO obj = (CidadeVO) context().getExternalContext().getRequestMap().get("filiacaoCidade");
			getFiliacaoVO().getPais().setCidade(obj);
			listaConsultaFiliacaoCidade.clear();
			this.setValorConsultaFiliacaoCidade("");
			this.setCampoConsultaFiliacaoCidade("");
			setMensagemID("msg_dados_selecionados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}

	}

	public String getApresentarCss() {
		if (getFiliacaoVO() != null && getFiliacaoVO().getCodigo() != 0) {
			return "camposSomenteLeitura";
		} else {
			return "camposObrigatorios";
		}

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
			listaConsultaFiliacaoCidade = new ArrayList();
		}
		return listaConsultaFiliacaoCidade;
	}

	public void setListaConsultaFiliacaoCidade(List listaConsultaFiliacaoCidade) {
		this.listaConsultaFiliacaoCidade = listaConsultaFiliacaoCidade;
	}

	public Double getTotalPagar() {
		if (totalPagar == null) {
			totalPagar = 0.0;
		}
		return totalPagar;
	}

	public void setTotalPagar(Double totalPagar) {
		this.totalPagar = totalPagar;
	}

	public Double getTotalPago() {
		if (totalPago == null) {
			totalPago = 0.0;
		}
		return totalPago;
	}

	public void setTotalPago(Double totalPago) {
		this.totalPago = totalPago;
	}

	public Date getDataFimContaPagar() {
		if (dataFimContaPagar == null) {
			dataFimContaPagar = new Date();
		}
		return dataFimContaPagar;
	}

	public void setDataFimContaPagar(Date dataFimContaPagar) {
		this.dataFimContaPagar = dataFimContaPagar;
	}

	public Date getDataInicioContaPagar() {
		if (dataInicioContaPagar == null) {
			dataInicioContaPagar = new Date();
		}
		return dataInicioContaPagar;
	}

	public void setDataInicioContaPagar(Date dataInicioContaPagar) {
		this.dataInicioContaPagar = dataInicioContaPagar;
	}

	public String getPessoa_Erro() {
		return pessoa_Erro;
	}

	public void setPessoa_Erro(String pessoa_Erro) {
		this.pessoa_Erro = pessoa_Erro;
	}

	public List getListaSelectItemCargo() {
		return (listaSelectItemCargo);
	}

	public void setListaSelectItemCargo(List listaSelectItemCargo) {
		this.listaSelectItemCargo = listaSelectItemCargo;
	}

	public List getListaSelectItemDepartamento() {
		return (listaSelectItemDepartamento);
	}

	public void setListaSelectItemDepartamento(List listaSelectItemDepartamento) {
		this.listaSelectItemDepartamento = listaSelectItemDepartamento;
	}

	public List getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList();
		}
		return (listaSelectItemUnidadeEnsino);
	}

	public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public FuncionarioVO getFuncionarioVO() {
		if (funcionarioVO == null) {
			funcionarioVO = new FuncionarioVO();
		}
		return funcionarioVO;
	}

	public void setFuncionarioVO(FuncionarioVO funcionarioVO) {
		this.funcionarioVO = funcionarioVO;
	}

	public FormacaoAcademicaVO getFormacaoAcademicaVO() {
		if (formacaoAcademicaVO == null) {
			formacaoAcademicaVO = new FormacaoAcademicaVO();
		}
		return formacaoAcademicaVO;
	}

	public void setFormacaoAcademicaVO(FormacaoAcademicaVO formacaoAcademicaVO) {
		this.formacaoAcademicaVO = formacaoAcademicaVO;
	}

	public FiliacaoVO getFiliacaoVO() {
		if (filiacaoVO == null) {
			filiacaoVO = new FiliacaoVO();
		}
		return filiacaoVO;
	}

	public void setFiliacaoVO(FiliacaoVO filiacaoVO) {
		this.filiacaoVO = filiacaoVO;
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

	public List getListaSelectItemNaturalidade() {
		return listaSelectItemNaturalidade;
	}

	public void setListaSelectItemNaturalidade(List listaSelectItemNaturalidade) {
		this.listaSelectItemNaturalidade = listaSelectItemNaturalidade;
	}

	public List getListaSelectItemNacionalidade() {
		return listaSelectItemNacionalidade;
	}

	public List getListaSelectItemAreaConhecimento() {
		return listaSelectItemAreaConhecimento;
	}

	public void setListaSelectItemAreaConhecimento(List listaSelectItemAreaConhecimento) {
		this.listaSelectItemAreaConhecimento = listaSelectItemAreaConhecimento;
	}

	public void setListaSelectItemNacionalidade(List listaSelectItemNacionalidade) {
		this.listaSelectItemNacionalidade = listaSelectItemNacionalidade;
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

	public Boolean getConsultarPessoa() {
		return consultarPessoa;
	}

	public String getModalPanelCpf() {
		if (!getConsultarPessoa() && !getImportarDadosPessoa()) {
			return "RichFaces.$('panelCpf').hide();RichFaces.$('panelImportarDados').hide();";
		} else if (!getConsultarPessoa()) {
			return "RichFaces.$('panelCpf').hide();RichFaces.$('panelImportarDados').show();";
		}
		return "RichFaces.$('panelCpf').show();RichFaces.$('panelImportarDados').hide();";
	}

	public String getModalImportarDados() {
		if (getImportarDadosPessoa()) {
			return "RichFaces.$('panelImportarDados').show();RichFaces.$('panelCpf').hide();";
		}
		return "RichFaces.$('panelImportarDados').hide();RichFaces.$('panelCpf').show();";
	}

	public void setConsultarPessoa(Boolean consultarPessoa) {
		this.consultarPessoa = consultarPessoa;
	}

	public Boolean getEditarDados() {
		if (editarDados == null) {
			editarDados = false;
		}
		return editarDados;
	}

	public void setEditarDados(Boolean editarDados) {
		this.editarDados = editarDados;
	}

	public Boolean getVerificarCpf() {
		if (verificarCpf == null) {
			verificarCpf = false;
		}
		return verificarCpf;
	}

	public void setVerificarCpf(Boolean verificarCpf) {
		this.verificarCpf = verificarCpf;
	}

	public List getListaSelectItemPerfilEconomico() {
		return listaSelectItemPerfilEconomico;
	}

	public void setListaSelectItemPerfilEconomico(List listaSelectItemPerfilEconomico) {
		this.listaSelectItemPerfilEconomico = listaSelectItemPerfilEconomico;
	}

	/**
	 * @return the funcionarioCargoVO
	 */
	public FuncionarioCargoVO getFuncionarioCargoVO() {
		if (funcionarioCargoVO == null) {
			funcionarioCargoVO = new FuncionarioCargoVO();
		}
		return funcionarioCargoVO;
	}

	/**
	 * @param funcionarioCargoVO the funcionarioCargoVO to set
	 */
	public void setFuncionarioCargoVO(FuncionarioCargoVO funcionarioCargoVO) {
		this.funcionarioCargoVO = funcionarioCargoVO;
	}

	public void atualizarExerceCargo() {
		// boolean a = this.funcionarioVO.getExerceCargoAdministrativo().booleanValue();
		panelDirecionarUsuario = "richTabDadosFuncionais";
	}

	/**
	 * @return the panelDirecionarUsuario
	 */
	public String getPanelDirecionarUsuario() {
		return panelDirecionarUsuario;
	}

	/**
	 * @param panelDirecionarUsuario the panelDirecionarUsuario to set
	 */
	public void setPanelDirecionarUsuario(String panelDirecionarUsuario) {
		this.panelDirecionarUsuario = panelDirecionarUsuario;
	}

	/**
	 * @return the disciplinasInteresseVO
	 */
	public DisciplinasInteresseVO getDisciplinasInteresseVO() {
		return disciplinasInteresseVO;
	}

	/**
	 * @param disciplinasInteresseVO the disciplinasInteresseVO to set
	 */
	public void setDisciplinasInteresseVO(DisciplinasInteresseVO disciplinasInteresseVO) {
		this.disciplinasInteresseVO = disciplinasInteresseVO;
	}

	public String getCampoConsultaDisciplina() {
		return campoConsultaDisciplina;
	}

	public void setCampoConsultaDisciplina(String campoConsultaDisciplina) {
		this.campoConsultaDisciplina = campoConsultaDisciplina;
	}

	public List getListaConsultaDisciplina() {
		if (listaConsultaDisciplina == null) {
			listaConsultaDisciplina = new ArrayList(0);
		}
		return listaConsultaDisciplina;
	}

	public void setListaConsultaDisciplina(List listaConsultaDisciplina) {
		this.listaConsultaDisciplina = listaConsultaDisciplina;
	}

	public String getValorConsultaDisciplina() {
		return valorConsultaDisciplina;
	}

	public void setValorConsultaDisciplina(String valorConsultaDisciplina) {
		this.valorConsultaDisciplina = valorConsultaDisciplina;
	}

	public String getDisciplina_Erro() {
		return disciplina_Erro;
	}

	public void setDisciplina_Erro(String disciplina_Erro) {
		this.disciplina_Erro = disciplina_Erro;
	}

	public String disciplina() {
		return "disciplina";
	}

	public Integer getCodigoTurno() {
		if (codigoTurno == null) {
			codigoTurno = 0;
		}
		return codigoTurno;
	}

	public void setCodigoTurno(Integer codigoTurno) {
		this.codigoTurno = codigoTurno;
	}

	public String getTurno_Erro() {
		return turno_Erro;
	}

	public void setTurno_Erro(String turno_Erro) {
		this.turno_Erro = turno_Erro;
	}

	public List getListaSelectItemTurno() {
		return listaSelectItemTurno;
	}

	public void setListaSelectItemTurno(List listaSelectItemTurno) {
		this.listaSelectItemTurno = listaSelectItemTurno;
	}

	public QuadroHorarioVO getQuadroHorario() {
		return quadroHorario;
	}

	public void setQuadroHorario(QuadroHorarioVO quadroHorario) {
		this.quadroHorario = quadroHorario;
	}

	public String getDiaSemana() {
		return diaSemana;
	}

	public void setDiaSemana(String diaSemana) {
		this.diaSemana = diaSemana;
	}

	public DisponibilidadeHorarioVO getDisponibilidadeHorarioVO() {
		return disponibilidadeHorarioVO;
	}

	public void setDisponibilidadeHorarioVO(DisponibilidadeHorarioVO disponibilidadeHorarioVO) {
		this.disponibilidadeHorarioVO = disponibilidadeHorarioVO;
	}

	public String disponibilidadeHorario() {
		return "disponibilidadeHorario";
	}

	public Boolean getHorarioSimples() {
		return horarioSimples;
	}

	public void setHorarioSimples(Boolean horarioSimples) {
		this.horarioSimples = horarioSimples;
	}

	public boolean isControladorTela() {
		return controladorTela;
	}

	public void setControladorTela(boolean controladorTela) {
		this.controladorTela = controladorTela;
	}

	@Override
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
		funcionarioVO = null;
		Uteis.liberarListaMemoria(listaSelectItemUnidadeEnsino);
		Uteis.liberarListaMemoria(listaSelectItemDepartamento);
		Uteis.liberarListaMemoria(listaSelectItemCargo);
		Uteis.liberarListaMemoria(listaConsultaCidade);
		Uteis.liberarListaMemoria(listaConsultaNaturalidade);
		Uteis.liberarListaMemoria(listaSelectItemNacionalidade);
		Uteis.liberarListaMemoria(listaSelectItemPerfilEconomico);
		campoConsultaCidade = null;
		valorConsultaCidade = null;
		campoConsultaNaturalidade = null;
		valorConsultaNaturalidade = null;
		formacaoAcademicaVO = null;
		disciplina_Erro = null;
		filiacaoVO = null;
		turno_Erro = null;
		disciplinasInteresseVO = null;
		disponibilidadeHorarioVO = null;
		horarioSimples = null;
		listaSelectItemFaixaSalarial = null;
		listaSelectItemNivelSalarial = null;
	}

	public List getTipoConsultaPor() {
		List objs = new ArrayList();
		objs.add(new SelectItem("disciplina", "Disciplina"));
		objs.add(new SelectItem("professor", "Professor"));
		return objs;
	}

	public void consultarDisciplinaVisaoCoordenador() {
		try {
			List objs = new ArrayList(0);
			if (getValorConsultaDisciplina().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			if (getCampoConsultaDisciplina().equals("codigo")) {
				int valorInt = Integer.parseInt(getValorConsultaDisciplina());
//                DisciplinaVO disciplina = getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimariaVisaoCoordenador(new Integer(valorInt), getUsuarioLogado().getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				DisciplinaVO disciplina = getFacadeFactory().getDisciplinaFacade()
						.consultarPorChavePrimariaCursoTurmaVisaoCoordenador(new Integer(valorInt),
								getFuncionarioVO().getPessoa().getCodigo(), getTurmaVO().getCodigo(),
								getUnidadeEnsinoCursoVO().getCurso().getCodigo(), getUnidadeEnsinoVO().getCodigo(),
								Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				if (disciplina != null && (!disciplina.getCodigo().equals(0))) {
					objs.add(disciplina);
				}
			}
			if (getCampoConsultaDisciplina().equals("nome")) {
//                objs = getFacadeFactory().getDisciplinaFacade().consultarPorNomeVisaoCoordenador(getValorConsultaDisciplina(), getUsuarioLogado().getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorNomeCursoTurmaVisaoCoordenador(
						getValorConsultaDisciplina(), getFuncionarioVO().getPessoa().getCodigo(),
						getTurmaVO().getCodigo(), getUnidadeEnsinoCursoVO().getCurso().getCodigo(),
						getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
						getUsuarioLogado());
			}
			if (getCampoConsultaDisciplina().equals("areaConhecimento")) {
//                objs = getFacadeFactory().getDisciplinaFacade().consultarPorNomeAreaConhecimentoVisaoCoordenador(getValorConsultaDisciplina(), getUsuarioLogado().getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				objs = getFacadeFactory().getDisciplinaFacade()
						.consultarPorNomeAreaConhecimentoCursoTurmaVisaoCoordenador(getValorConsultaDisciplina(),
								getFuncionarioVO().getPessoa().getCodigo(), getTurmaVO().getCodigo(),
								getUnidadeEnsinoCursoVO().getCurso().getCodigo(), getUnidadeEnsinoVO().getCodigo(),
								false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaDisciplina(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaDisciplina(new ArrayList(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public List getTipoConsultaComboProfessor() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("matricula", "Matrícula"));
//        itens.add(new SelectItem("nomeCidade", "Cidade"));
//        itens.add(new SelectItem("CPF", "CPF"));
//        itens.add(new SelectItem("cargo", "Cargo"));
//        itens.add(new SelectItem("departamento", "Departamento"));
		return itens;
	}

	public List<FuncionarioVO> autocompleteFuncionario(Object suggest) {
		try {
			return getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNomePessoaAutoComplete((String) suggest,
					getUnidadeEnsinoVO().getCodigo(), 20, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,
					getUsuarioLogado());
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
			return new ArrayList<FuncionarioVO>();
		}
	}

	public void consultarProfessor() {
		try {
			List objs = new ArrayList(0);
			if (getValorConsultaProfessor().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			if (getCampoConsultaProfessor().equals("nome")) {
//                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaProfessorPorNomeAgendaProfessoresVisaoCoordenador(getValorConsultaProfessor(), getUsuarioLogado().getPessoa().getCodigo(), "PR", getUnidadeEnsinoLogado().getCodigo(), Uteis.getData(new Date(), "yyyy"), Uteis.getSemestreAtual(), false, getUsuarioLogado());
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNome(getValorConsultaProfessor(),
						"PR", getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,
						getUsuarioLogado());
			}
			if (getCampoConsultaProfessor().equals("matricula")) {
//                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaProfessorPorMatriculaAgendaProfessoresVisaoCoordenador(getValorConsultaProfessor(), getUsuarioLogado().getPessoa().getCodigo(), "PR", getUnidadeEnsinoLogado().getCodigo(), Uteis.getData(new Date(), "yyyy"), Uteis.getSemestreAtual(), false, getUsuarioLogado());
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorMatricula(getValorConsultaProfessor(),
						getUnidadeEnsinoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaProfessor().equals("nomeCidade")) {
				objs = getFacadeFactory().getFuncionarioFacade()
						.consultaRapidaProfessorPorNomeCidadeAgendaProfessoresVisaoCoordenador(
								getValorConsultaProfessor(), getUsuarioLogado().getPessoa().getCodigo(), "PR",
								getUnidadeEnsinoLogado().getCodigo(), Uteis.getData(new Date(), "yyyy"),
								Uteis.getSemestreAtual(), false, getUsuarioLogado());
			}
			if (getCampoConsultaProfessor().equals("CPF")) {
				objs = getFacadeFactory().getFuncionarioFacade()
						.consultaRapidaProfessorPorCPFAgendaProfessoresVisaoCoordenador(getValorConsultaProfessor(),
								getUsuarioLogado().getPessoa().getCodigo(), "PR", getUnidadeEnsinoLogado().getCodigo(),
								Uteis.getData(new Date(), "yyyy"), Uteis.getSemestreAtual(), false, getUsuarioLogado());
			}
			if (getCampoConsultaProfessor().equals("cargo")) {
				objs = getFacadeFactory().getFuncionarioFacade()
						.consultaRapidaProfessorPorCargoAgendaProfessoresVisaoCoordenador(getValorConsultaProfessor(),
								getUsuarioLogado().getPessoa().getCodigo(), "PR", getUnidadeEnsinoLogado().getCodigo(),
								Uteis.getData(new Date(), "yyyy"), Uteis.getSemestreAtual(), false, getUsuarioLogado());
			}
			if (getCampoConsultaProfessor().equals("departamento")) {
				objs = getFacadeFactory().getFuncionarioFacade()
						.consultaRapidaProfessorPorDepartamentoAgendaProfessoresVisaoCoordenador(
								getValorConsultaProfessor(), getUsuarioLogado().getPessoa().getCodigo(), "PR",
								getUnidadeEnsinoLogado().getCodigo(), Uteis.getData(new Date(), "yyyy"),
								Uteis.getSemestreAtual(), false, getUsuarioLogado());
			}
			setListaSelectItemProfessoresTurma(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaSelectItemProfessoresTurma(new ArrayList(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void selecionarProfessor() {
		FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItem");
		setFuncionarioVO(obj);
		montarListaSelectItemUnidadeEnsino();
	}

	public void montarListaSelectItemUnidadeEnsinoAgendaProfessorVisaoCoordenador() {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarUnidadeEnsinoPorProfessor(getFuncionarioVO().getPessoa().getCodigo());
			i = resultadoConsulta.iterator();
			getListaSelectItemUnidadeEnsino().clear();
			getListaSelectItemUnidadeEnsino().add(new SelectItem(0, ""));
			while (i.hasNext()) {
				UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
				getListaSelectItemUnidadeEnsino().add(new SelectItem(obj.getCodigo(), obj.getNome()));
				removerObjetoMemoria(obj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public List consultarUnidadeEnsinoPorProfessor(Integer codigoProfessor) {
		List lista = new ArrayList<>();
		try {
			lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorProfessor(codigoProfessor, false,
					Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		} catch (Exception e) {
		}
		return lista;
	}

	public void consultarProfessorPorChavePrimaria() {
		try {
			setFuncionarioVO(getFacadeFactory().getFuncionarioFacade()
					.consultaRapidaProfessorPorMatriculaUnicaAgendaProfessoresVisaoCoordenador(
							getFuncionarioVO().getMatricula(), getUsuarioLogado().getPessoa().getCodigo(), "PR",
							getUnidadeEnsinoLogado().getCodigo(), Uteis.getData(new Date(), "yyyy"),
							Uteis.getSemestreAtual(), false, getUsuarioLogado()));
			if (getFuncionarioVO().getCodigo().equals(0)) {
				getFuncionarioVO().setMatricula("");
			}
			montarListaSelectItemTurmaPorProfessorCoordenador(getFuncionarioVO().getPessoa().getCodigo());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void montarListaSelectItemTurmaPorProfessorCoordenador(Integer professor) {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			getListaSelectItemTurma().clear();
			resultadoConsulta = consultarTurmaCoordenadorPorProfessor(professor);
			i = resultadoConsulta.iterator();
			getListaSelectItemTurma().clear();
			getListaSelectItemTurma().add(new SelectItem(0, "Todas"));
			while (i.hasNext()) {
				TurmaVO obj = (TurmaVO) i.next();
				getListaSelectItemTurma().add(new SelectItem(obj.getCodigo(), obj.getIdentificadorTurma().toString()));
				removerObjetoMemoria(obj);
			}
			resultadoConsulta.clear();
			resultadoConsulta = null;
			i = null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}
	
	 
    public void montarListaSelectItemCodPerfilAcesso() {
    	getListaSelectItemCodPerfilAcesso().clear();
    	List<PerfilAcessoVO> resultadoConsulta = null;
        try {
        	getListaSelectItemCodPerfilAcesso().add(new SelectItem(0, ""));
            resultadoConsulta = getFacadeFactory().getPerfilAcessoFacade().consultarPorNome("", false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogadoClone());
            resultadoConsulta.stream().forEach(p->{getListaSelectItemCodPerfilAcesso().add(new SelectItem(p.getCodigo(), p.getNome()));});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
        }
    }

	public List consultarTurmaCoordenadorPorProfessor(Integer professor) {
		List lista = new ArrayList<>();
		try {
			lista = getFacadeFactory().getTurmaFacade().consultaRapidaPorProfessorCoordenador(professor,
					getUsuarioLogado().getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(),
					Uteis.getData(new Date(), "yyyy"), Uteis.getSemestreAtual(), false, getUsuarioLogado());
		} catch (Exception e) {
		}
		return lista;
	}

	public void selecionarDisciplina() {
		DisciplinaVO obj = (DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinaItem");
		setDisciplinaVO(obj);
	}

	public void limparDadosProfessorDisciplina() {
		limparListaHorariosProfessores();
		getListaSelectItemTurma().clear();
		setFuncionarioVO(null);
		setDisciplinaVO(null);
	}

	public void limparDadosProfessor() {
		limparDadosCurso();
		getListaSelectItemTurma().clear();
		getListaSelectItemProfessoresTurma().clear();
		setValorConsultaProfessor("");
		setFuncionarioVO(null);
		setUnidadeEnsinoVO(null);
	}

	public void limparDadosCurso() {
		limparDadosTurma();
		getListaConsultaCurso().clear();
		setValorConsultaCurso("");
		getUnidadeEnsinoCursoVO().setCurso(null);
	}

	public void limparDadosCursoFuncionarioCargo() {
		getListaConsultaCurso().clear();
		setValorConsultaCurso("");
		getFuncionarioCargoVO().setCursoVO(new CursoVO());
	}

	public void limparDadosTurma() {
		limparDadosDisciplina();
		getListaConsultaTurma().clear();
		setValorConsultaTurma("");
		setTurmaVO(null);
	}

	public void limparDadosDisciplina() {
		limparListaHorariosProfessores();
		getListaConsultaDisciplina().clear();
		setValorConsultaDisciplina("");
		setDisciplinaVO(null);
	}

	public void limparListasConsultas() {
		limparListaHorariosProfessores();
		setTurmaVO(null);
		setDisciplinaVO(null);
		setUnidadeEnsinoCursoVO(null);
		getListaConsultaCurso().clear();
		getListaConsultaTurma().clear();
		getListaConsultaDisciplina().clear();
	}

	public void consultarCurso() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaCurso().equals("codigo")) {
				if (getValorConsultaCurso().equals("")) {
					setValorConsultaCurso("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaCurso());
				if (getUsuarioLogado().getVisaoLogar().equals("coordenador")) {
					objs = getFacadeFactory().getCursoFacade().consultarPorCodigoCursoProfessor(valorInt,
							getFuncionarioVO().getPessoa().getCodigo(), getUnidadeEnsinoVO().getCodigo(), false,
							Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
				} else {
					objs = getFacadeFactory().getCursoFacade().consultarPorCodigoCursoProfessor(valorInt,
							getFuncionarioVO().getPessoa().getCodigo(), getUnidadeEnsinoVO().getCodigo(), false,
							Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
				}
			}
			if (getCampoConsultaCurso().equals("nome")) {
				if (getUsuarioLogado().getVisaoLogar().equals("coordenador")) {
					objs = getFacadeFactory().getCursoFacade().consultarPorProfessor(getValorConsultaCurso(),
							getFuncionarioVO().getPessoa().getCodigo(), getUnidadeEnsinoVO().getCodigo(), false,
							Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
				} else {
					objs = getFacadeFactory().getCursoFacade().consultarPorProfessor(getValorConsultaCurso(),
							getFuncionarioVO().getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), false,
							Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
				}
			}
			setListaConsultaCurso(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());

		}
	}

	public void consultarCursoFormFuncionario() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaCurso().equals("codigo")) {
				if (getValorConsultaCurso().equals("")) {
					setValorConsultaCurso("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaCurso());
				objs = getFacadeFactory().getCursoFacade().consultarPorCodigo(valorInt,
						getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
						getUsuarioLogado());
			}
			if (getCampoConsultaCurso().equals("nome")) {
				objs = getFacadeFactory().getCursoFacade().consultarPorNome(getValorConsultaCurso(),
						getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
						getUsuarioLogado());
			}
			setListaConsultaCurso(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			e.printStackTrace();
			setListaConsultaCurso(new ArrayList(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void selecionarCurso() {
		try {
			CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItem");
			getUnidadeEnsinoCursoVO().setCurso(obj);
			limparDadosTurma();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void selecionarCursoFormFuncionario() {
		try {
			CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItem");
			getPessoaPreInscricaoCursoVO().setCurso(obj);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void selecionarCursoFuncioniarioCargo() {
		try {
			CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItem");
			getFuncionarioCargoVO().setCursoVO(obj);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void limparCurso() {
		try {
			setUnidadeEnsinoCursoVO(null);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void limparCursoFormFuncionario() {
		try {
			setPessoaPreInscricaoCursoVO(new PessoaPreInscricaoCursoVO());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public List getTipoConsultaComboCurso() {
		if (tipoConsultaComboCurso == null) {
			tipoConsultaComboCurso = new ArrayList(0);
			tipoConsultaComboCurso.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboCurso.add(new SelectItem("codigo", "Código"));
		}
		return tipoConsultaComboCurso;
	}

	public void consultarTurma() {
		try {
			super.consultar();
			List objs = new ArrayList(0);
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				if (getUsuarioLogado().getVisaoLogar().equals("coordenador")) {
					objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaCurso(
							getValorConsultaTurma(), getUnidadeEnsinoCursoVO().getCurso().getCodigo(),
							getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,
							getUsuarioLogado());
				} else {
					objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaCurso(
							getValorConsultaTurma(), getUnidadeEnsinoCursoVO().getCurso().getCodigo(),
							getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,
							getUsuarioLogado());
				}
			}
			setListaConsultaTurma(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarTurmaPorChavePrimaria() {
		try {
			setTurmaVO(getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(getTurmaVO(),
					getTurmaVO().getIdentificadorTurma(), getUnidadeEnsinoLogado().getCodigo(), false,
					Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			if (getTurmaVO().getCodigo() == 0) {
				setTurmaVO(null);
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setTurmaVO(null);
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void selecionarTurma() {
		try {
			TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItem");
			setTurmaVO(obj);
			limparDadosDisciplina();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void limparTurma() {
		try {
			setTurmaVO(null);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public List getTipoConsultaComboTurma() {
		if (tipoConsultaComboTurma == null) {
			tipoConsultaComboTurma = new ArrayList(0);

			tipoConsultaComboTurma.add(new SelectItem("identificadorTurma", "Identificador"));
		}
		return tipoConsultaComboTurma;
	}

	public DisciplinaVO getDisciplinaVO() {
		if (disciplinaVO == null) {
			disciplinaVO = new DisciplinaVO();
		}
		return disciplinaVO;
	}

	public void setDisciplinaVO(DisciplinaVO disciplinaVO) {
		this.disciplinaVO = disciplinaVO;
	}

	public String getTipoConsulta() {
		if (tipoConsulta == null) {
			tipoConsulta = "disciplina";
		}
		return tipoConsulta;
	}

	public void setTipoConsulta(String tipoConsulta) {
		this.tipoConsulta = tipoConsulta;
	}

	public String getCampoConsultaProfessor() {
		if (campoConsultaProfessor == null) {
			campoConsultaProfessor = "";
		}
		return campoConsultaProfessor;
	}

	public void setCampoConsultaProfessor(String campoConsultaProfessor) {
		this.campoConsultaProfessor = campoConsultaProfessor;
	}

	public String getValorConsultaProfessor() {
		if (valorConsultaProfessor == null) {
			valorConsultaProfessor = "";
		}
		return valorConsultaProfessor;
	}

	public void setValorConsultaProfessor(String valorConsultaProfessor) {
		this.valorConsultaProfessor = valorConsultaProfessor;
	}

	public boolean getIsApresentarBotaoGravarFotoUsuario() {
		return !getFuncionarioVO().getNovoObj();
	}

	public boolean getIsConsultarPorProfessor() {
		return (getTipoConsulta().equals("professor"));
	}

	public boolean getIsConsultarPorDisciplina() {
		return (getTipoConsulta().equals("disciplina"));
	}

	public boolean getIsProfessorSelecionado() {
		return (getIsConsultarPorProfessor() && !getFuncionarioVO().getCodigo().equals(0)
				&& !getListaSelectItemTurma().isEmpty());
	}

	public boolean getIsDisciplinaProfessorTurmaSelecionado() {
		return ((getIsConsultarPorProfessor() && !getFuncionarioVO().getCodigo().equals(0))
				|| (getIsConsultarPorDisciplina() && !getDisciplinaVO().getCodigo().equals(0)));
	}
	
	public List<SelectItem> getListaSelectItemCodPerfilAcesso() {
		if (listaSelectItemCodPerfilAcesso == null) {
			listaSelectItemCodPerfilAcesso = new ArrayList<SelectItem>(0);
		}
		return (listaSelectItemCodPerfilAcesso);
	}

	public void setListaSelectItemCodPerfilAcesso(List listaSelectItemCodPerfilAcesso) {
		this.listaSelectItemCodPerfilAcesso = listaSelectItemCodPerfilAcesso;
	}
	

	public UsuarioPerfilAcessoVO getUsuarioPerfilAcessoVO() {
		if (usuarioPerfilAcessoVO == null) {
			usuarioPerfilAcessoVO = new UsuarioPerfilAcessoVO();
		}
		return usuarioPerfilAcessoVO;
	}

	public void setUsuarioPerfilAcessoVO(UsuarioPerfilAcessoVO usuarioPerfilAcessoVO) {
		this.usuarioPerfilAcessoVO = usuarioPerfilAcessoVO;
	}

	/**
	 * @return the campoConsultaCidade
	 */
	public String getCampoConsultaCidade() {
		return campoConsultaCidade;
	}

	/**
	 * @param campoConsultaCidade the campoConsultaCidade to set
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
	 * @param valorConsultaCidade the valorConsultaCidade to set
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
	 * @param listaConsultaCidade the listaConsultaCidade to set
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
	 * @param campoConsultaNaturalidade the campoConsultaNaturalidade to set
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
	 * @param valorConsultaNaturalidade the valorConsultaNaturalidade to set
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
	 * @param listaConsultaNaturalidade the listaConsultaNaturalidade to set
	 */
	public void setListaConsultaNaturalidade(List listaConsultaNaturalidade) {
		this.listaConsultaNaturalidade = listaConsultaNaturalidade;
	}

	public HorarioProfessorDiaVO getHorarioProfessorDiaVO() {
		if (horarioProfessorDiaVO == null) {
			horarioProfessorDiaVO = new HorarioProfessorDiaVO();
		}
		return horarioProfessorDiaVO;
	}

	public void setHorarioProfessorDiaVO(HorarioProfessorDiaVO horarioProfessorDiaVO) {
		this.horarioProfessorDiaVO = horarioProfessorDiaVO;
	}

	public Boolean getImportarDadosPessoa() {
		if (importarDadosPessoa == null) {
			importarDadosPessoa = false;
		}
		return importarDadosPessoa;
	}

	public void setImportarDadosPessoa(Boolean importarDadosPessoa) {
		this.importarDadosPessoa = importarDadosPessoa;
	}

	public PessoaVO getPessoaExistente() {
		return pessoaExistente;
	}

	public void setPessoaExistente(PessoaVO pessoaExistente) {
		this.pessoaExistente = pessoaExistente;
	}

	public boolean getCpfInvalido() {
		return cpfInvalido;
	}

	public void setCpfInvalido(boolean cpfInvalido) {
		this.cpfInvalido = cpfInvalido;
	}

	public boolean getIsInformarCpfNovamente() {
		if ((getEditarDados() && getEditarDados() != null) || (getNaoFuncionario())) {
			return true;
		}
		return false;
	}

	public void setNaoFuncionario(boolean naoFuncionario) {
		this.naoFuncionario = naoFuncionario;
	}

	public boolean getNaoFuncionario() {
		return naoFuncionario;
	}

	public boolean getIsInformarMatricula() {
		if (!getFuncionarioVO().getInformarMatricula()) {
			return true;
		}
		return false;
	}

	public boolean getIsMostrarCampoMatricula() {
		if (!getFuncionarioVO().getInformarMatricula() && getFuncionarioVO().getNovoObj()) {
			return false;
		}
		return true;
	}

	/**
	 * @return the dataInicio
	 */
	public Date getDataInicio() {
		if (dataInicio == null) {
			dataInicio = new Date();
		}
		return dataInicio;
	}

	/**
	 * @param dataInicio the dataInicio to set
	 */
	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	/**
	 * @return the dataFim
	 */
	public Date getDataFim() {
		return dataFim;
	}

	/**
	 * @param dataFim the dataFim to set
	 */
	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public HorarioProfessorDiaItemVO getSelected() {
		HorarioProfessorDiaItemVO obj = (HorarioProfessorDiaItemVO) context().getExternalContext().getRequestMap()
				.get("horarioProfessorItem");
		return obj;
	}

	public List getListaSelectItemTurma() {
		if (listaSelectItemTurma == null) {
			listaSelectItemTurma = new ArrayList(0);
		}
		return listaSelectItemTurma;
	}

	public void setListaSelectItemTurma(List listaSelectItemTurma) {
		this.listaSelectItemTurma = listaSelectItemTurma;
	}

	public TurmaVO getTurmaVO() {
		if (turmaVO == null) {
			turmaVO = new TurmaVO();
		}
		return turmaVO;
	}

	public void setTurmaVO(TurmaVO turmaVO) {
		this.turmaVO = turmaVO;
	}

	public List getListaSelectItemProfessoresTurma() {
		if (listaSelectItemProfessoresTurma == null) {
			listaSelectItemProfessoresTurma = new ArrayList(0);
		}
		return listaSelectItemProfessoresTurma;
	}

	public void setListaSelectItemProfessoresTurma(List listaSelectItemProfessoresTurma) {
		this.listaSelectItemProfessoresTurma = listaSelectItemProfessoresTurma;
	}

	public List getListaContaPagar() {
		if (listaContaPagar == null) {
			listaContaPagar = new ArrayList(0);
		}
		return listaContaPagar;
	}

	public void setListaContaPagar(List listaContaPagar) {
		this.listaContaPagar = listaContaPagar;
	}

	public boolean getIsExisteContaPagar() {
		return !getListaContaPagar().isEmpty();
	}

	public Boolean getApresentarFiltros() {
		if (getFuncionarioVO().getPessoa().getCodigo() != 0) {
			return true;
		} else {
			return false;
		}
	}

	public String getCampoConsultaCurso() {
		if (campoConsultaCurso == null) {
			campoConsultaCurso = "";
		}
		return campoConsultaCurso;
	}

	public void setCampoConsultaCurso(String campoConsultaCurso) {
		this.campoConsultaCurso = campoConsultaCurso;
	}

	public String getValorConsultaCurso() {
		if (valorConsultaCurso == null) {
			valorConsultaCurso = "";
		}
		return valorConsultaCurso;
	}

	public void setValorConsultaCurso(String valorConsultaCurso) {
		this.valorConsultaCurso = valorConsultaCurso;
	}

	public List getListaConsultaCurso() {
		if (listaConsultaCurso == null) {
			listaConsultaCurso = new ArrayList(0);
		}
		return listaConsultaCurso;
	}

	public void setListaConsultaCurso(List listaConsultaCurso) {
		this.listaConsultaCurso = listaConsultaCurso;
	}

	public UnidadeEnsinoCursoVO getUnidadeEnsinoCursoVO() {
		if (unidadeEnsinoCursoVO == null) {
			unidadeEnsinoCursoVO = new UnidadeEnsinoCursoVO();
		}
		return unidadeEnsinoCursoVO;
	}

	public void setUnidadeEnsinoCursoVO(UnidadeEnsinoCursoVO unidadeEnsinoCursoVO) {
		this.unidadeEnsinoCursoVO = unidadeEnsinoCursoVO;
	}

	public String getCampoConsultaTurma() {
		if (campoConsultaTurma == null) {
			campoConsultaTurma = "";
		}
		return campoConsultaTurma;
	}

	public void setCampoConsultaTurma(String campoConsultaTurma) {
		this.campoConsultaTurma = campoConsultaTurma;
	}

	public String getValorConsultaTurma() {
		if (valorConsultaTurma == null) {
			valorConsultaTurma = "";
		}
		return valorConsultaTurma;
	}

	public void setValorConsultaTurma(String valorConsultaTurma) {
		this.valorConsultaTurma = valorConsultaTurma;
	}

	public List getListaConsultaTurma() {
		if (listaConsultaTurma == null) {
			listaConsultaTurma = new ArrayList(0);
		}
		return listaConsultaTurma;
	}

	public void setListaConsultaTurma(List listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
	}

	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}

	public boolean getIsUnidadeEnsinoNaoSelecionada() {
		return (getUnidadeEnsinoVO().getCodigo().equals(0));
	}

	/**
	 * @return the pessoaPreInscricaoCursoVO
	 */
	public PessoaPreInscricaoCursoVO getPessoaPreInscricaoCursoVO() {
		return pessoaPreInscricaoCursoVO;
	}

	/**
	 * @param pessoaPreInscricaoCursoVO the pessoaPreInscricaoCursoVO to set
	 */
	public void setPessoaPreInscricaoCursoVO(PessoaPreInscricaoCursoVO pessoaPreInscricaoCursoVO) {
		this.pessoaPreInscricaoCursoVO = pessoaPreInscricaoCursoVO;
	}

	public boolean getApresentarCampoConsultorGerente() {
		if (getFuncionarioCargoVO().getCargo().getConsultorVendas() == null) {
			return false;
		}
		return getFuncionarioCargoVO().getCargo().getConsultorVendas();
	}

	public List<DocumetacaoPessoaVO> getListaDocumentacaoPessoaProfessor() {
		if (listaDocumentacaoPessoaProfessor == null) {
			listaDocumentacaoPessoaProfessor = new ArrayList<DocumetacaoPessoaVO>(0);
		}
		return listaDocumentacaoPessoaProfessor;
	}

	public void setListaDocumentacaoPessoaProfessor(List<DocumetacaoPessoaVO> listaDocumentacaoPessoaProfessor) {
		this.listaDocumentacaoPessoaProfessor = listaDocumentacaoPessoaProfessor;
	}

	/**
	 * @return the funcionarioUnificarVO
	 */
	public FuncionarioVO getFuncionarioUnificarVO() {
		if (funcionarioUnificarVO == null) {
			funcionarioUnificarVO = new FuncionarioVO();
		}
		return funcionarioUnificarVO;
	}

	/**
	 * @param funcionarioUnificarVO the funcionarioUnificarVO to set
	 */
	public void setFuncionarioUnificarVO(FuncionarioVO funcionarioUnificarVO) {
		this.funcionarioUnificarVO = funcionarioUnificarVO;
	}

	/**
	 * @return the listaConsultaFuncionarioUnificar
	 */
	public List getListaConsultaFuncionarioUnificar() {
		if (listaConsultaFuncionarioUnificar == null) {
			listaConsultaFuncionarioUnificar = new ArrayList();
		}
		return listaConsultaFuncionarioUnificar;
	}

	/**
	 * @param listaConsultaFuncionarioUnificar the listaConsultaFuncionarioUnificar
	 *                                         to set
	 */
	public void setListaConsultaFuncionarioUnificar(List listaConsultaFuncionarioUnificar) {
		this.listaConsultaFuncionarioUnificar = listaConsultaFuncionarioUnificar;
	}

	/**
	 * @return the valorConsultaFuncionarioUnificar
	 */
	public String getValorConsultaFuncionarioUnificar() {
		if (valorConsultaFuncionarioUnificar == null) {
			valorConsultaFuncionarioUnificar = "";
		}
		return valorConsultaFuncionarioUnificar;
	}

	/**
	 * @param valorConsultaFuncionarioUnificar the valorConsultaFuncionarioUnificar
	 *                                         to set
	 */
	public void setValorConsultaFuncionarioUnificar(String valorConsultaFuncionarioUnificar) {
		this.valorConsultaFuncionarioUnificar = valorConsultaFuncionarioUnificar;
	}

	/**
	 * @return the campoConsultaFuncionarioUnificar
	 */
	public String getCampoConsultaFuncionarioUnificar() {
		if (campoConsultaFuncionarioUnificar == null) {
			campoConsultaFuncionarioUnificar = "";
		}
		return campoConsultaFuncionarioUnificar;
	}

	/**
	 * @param campoConsultaFuncionarioUnificar the campoConsultaFuncionarioUnificar
	 *                                         to set
	 */
	public void setCampoConsultaFuncionarioUnificar(String campoConsultaFuncionarioUnificar) {
		this.campoConsultaFuncionarioUnificar = campoConsultaFuncionarioUnificar;
	}

	public Boolean getExibirFormCriarUsuario() {
		if (exibirFormCriarUsuario == null) {
			exibirFormCriarUsuario = Boolean.FALSE;
		}
		return exibirFormCriarUsuario;
	}

	public void setExibirFormCriarUsuario(Boolean exibirFormCriarUsuario) {
		this.exibirFormCriarUsuario = exibirFormCriarUsuario;
	}

	public boolean getIsApresentarCriarUsuario() {
		try {
			return getLoginControle().getPermissaoAcessoMenuVO().getUsuario();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			return false;
		}
	}

	@Deprecated
	public void alterarAtivo() {
		try {
			FuncionarioCargoVO obj = (FuncionarioCargoVO) context().getExternalContext().getRequestMap()
					.get("funcionarioCargoItem");
			obj.setAtivo(!obj.getAtivo());
			getFacadeFactory().getFuncionarioCargoFacade().alterarAtivo(obj, getFuncionarioVO().getCodigo(),
					getUsuarioLogado());
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void imprimirPDF() {
		if (getLayout().equals("LAYOUT_DIA_A_DIA")) {
			realizarImpressaoPDFHorarioProfessorDia();
		} else {
			realizarImpressaoPDFSemanal();
		}
	}

	public void realizarImpressaoPDFSemanal() {
		List<CronogramaDeAulasRelVO> listaObjetos = new ArrayList<CronogramaDeAulasRelVO>(0);
		try {
			listaObjetos = getFacadeFactory().getHorarioProfessorDiaFacade()
					.criarObjetoRelatorioSemanal(getHorarioProfessorTurnoVOs(), getUsuarioLogado());
			if (!listaObjetos.isEmpty()) {
				getSuperParametroRelVO().limparParametros();
				getSuperParametroRelVO().setNomeDesignIreport(HorarioProfessorDia.getDesignIReportRelatorio());
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setSubReport_Dir(HorarioProfessorDia.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio("Horário do Professor");
				if (getOrdenacao().equals("unidadeEnsino")) {
					Ordenacao.ordenarLista(listaObjetos, "unidadeEnsino");
				} else if (getOrdenacao().equals("data")) {
					Ordenacao.ordenarLista(listaObjetos, "dataInicio");
				} else if (getOrdenacao().equals("disciplina")) {
					Ordenacao.ordenarLista(listaObjetos, "disciplina");
				}
				/*
				 * if ((getUsuarioLogado().getIsApresentarVisaoAluno() ||
				 * getUsuarioLogado().getIsApresentarVisaoPais()) &&
				 * getVisaoAlunoControle().getMatricula().getCurso().getNivelEducacional().
				 * equals("PO")) { Ordenacao.ordenarLista(listaObjetos, "dataInicio"); } else {
				 * Ordenacao.ordenarLista(listaObjetos, "modulo"); }
				 */
				getSuperParametroRelVO().setListaObjetos(listaObjetos);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(HorarioProfessorDia.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().adicionarParametro("ocultarData", false);
				getSuperParametroRelVO().setProfessor(getPessoaVO().getNome());
				getSuperParametroRelVO().setUnidadeEnsino(getUnidadeEnsinoVO().getNome());
				getSuperParametroRelVO().setCurso(getUnidadeEnsinoCursoVO().getCurso().getNome());
				getSuperParametroRelVO().setMatricula(getFuncionarioVO().getMatricula());

				getSuperParametroRelVO().setDataInicio(Uteis.getDataAno4Digitos(getDataInicio()));
				getSuperParametroRelVO().setDataFim(Uteis.getDataAno4Digitos(getDataFim()));
				getSuperParametroRelVO().getParametros().put("visaoProfessor",
						getUsuarioLogado().getIsApresentarVisaoProfessor());
				realizarImpressaoRelatorio();
				persistirLayoutPadrao(getLayout());
				// removerObjetoMemoria(this);
				setMensagemID("msg_relatorio_ok");
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		} finally {
			Uteis.liberarListaMemoria(listaObjetos);
		}
	}

	public void consultarHorarioProfessorRel() {
		try {
			if (getPessoaVO().getCodigo().equals(0)) {
				throw new ConsistirException("O campo PROFESSOR deve ser informado.");
			}
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		consultarHorarioAulaProfessor();
	}

	public void consultarHorarioAulaProfessor() {
		try {
			if (getCalendarioMensal()) {
//				setDataInicio(Uteis.obterDataFutura(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(UteisData.getPrimeiroDiaSemana(Uteis.getDataPrimeiroDiaMes(getDataBaseHorarioAula()))), 1));
//				setDataTermino(Uteis.obterDataFutura(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(UteisData.getUltimoDiaSemana(Uteis.getDataUltimoDiaMes(getDataBaseHorarioAula()))), 1));
				setDataInicio(Uteis.obterDataPassada(new Date(), 500));
//				setDataTermino(Uteis.obterDataFutura(new Date(), 500));
			} else {
				setDataInicio(UteisData.obterDataFutura(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(
						UteisData.getPrimeiroDiaSemana(getDataBaseHorarioAula())), 1));
				setDataFim(UteisData.obterDataFutura(UteisData.getUltimoDiaSemana(getDataBaseHorarioAula()), 1));
			}
			setHorarioProfessorTurnoVOs(getFacadeFactory().getHorarioProfessorDiaFacade()
					.consultarMeusHorariosProfessor(getPessoaVO(), getDataBaseHorarioAula(), getUnidadeEnsinoVO(),
							getUnidadeEnsinoCursoVO().getCurso(), getTurmaVO(), getDisciplinaVO(), getUsuarioLogado()));
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void visualizarProximaSemana() {
		if (getCalendarioMensal()) {
			setDataBaseHorarioAula(
					Uteis.getDataPrimeiroDiaMes(Uteis.obterDataAvancadaPorMes(getDataBaseHorarioAula(), 1)));
		} else {
			setDataBaseHorarioAula(UteisData.getPrimeiroDiaProximaSemana(getDataBaseHorarioAula()));
		}

		consultarHorarioAulaProfessor();
	}

	public void visualizarSemanaAnterior() {
		if (getCalendarioMensal()) {
			setDataBaseHorarioAula(
					Uteis.getDataPrimeiroDiaMes(Uteis.obterDataAvancadaPorMes(getDataBaseHorarioAula(), -1)));
		} else {
			setDataBaseHorarioAula(UteisData.getPrimeiroDiaSemanaPassada(getDataBaseHorarioAula()));
		}

		consultarHorarioAulaProfessor();
	}

	public String getLayout() {
		if (layout == null) {
			layout = "LAYOUT_DIA_A_DIA";
		}
		return layout;
	}

	public void setLayout(String layout) {
		this.layout = layout;
	}

	public List<SelectItem> getListaSelectUtemLayoutVOs() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("LAYOUT_DIA_A_DIA", "Layout Dia a Dia"));
		itens.add(new SelectItem("LAYOUT_SEMANAL", "Layout Semanal"));
		return itens;
	}

	public List<HorarioProfessorTurnoVO> getHorarioProfessorTurnoVOs() {
		if (horarioProfessorTurnoVOs == null) {
			horarioProfessorTurnoVOs = new ArrayList<HorarioProfessorTurnoVO>(0);
		}
		return horarioProfessorTurnoVOs;
	}

	public void setHorarioProfessorTurnoVOs(List<HorarioProfessorTurnoVO> horarioProfessorTurnoVOs) {
		this.horarioProfessorTurnoVOs = horarioProfessorTurnoVOs;
	}

	public Boolean getCalendarioMensal() {
		if (calendarioMensal == null) {
			calendarioMensal = false;
		}
		return calendarioMensal;
	}

	public void setCalendarioMensal(Boolean calendarioMensal) {
		this.calendarioMensal = calendarioMensal;
	}

	public Date getDataBaseHorarioAula() {
		if (dataBaseHorarioAula == null) {
			dataBaseHorarioAula = new Date();
		}
		return dataBaseHorarioAula;
	}

	public void setDataBaseHorarioAula(Date dataBaseHorarioAula) {
		this.dataBaseHorarioAula = dataBaseHorarioAula;
	}

	public Boolean getOcultarHorarioLivre() {
		if (ocultarHorarioLivre == null) {
			ocultarHorarioLivre = false;
		}
		return ocultarHorarioLivre;
	}

	/**
	 * @param ocultarHorarioLivre the ocultarHorarioLivre to set
	 */
	public void setOcultarHorarioLivre(Boolean ocultarHorarioLivre) {
		this.ocultarHorarioLivre = ocultarHorarioLivre;
	}

	public void inicializarDadosHorarioProfessor() {
		if (getLayout().equals("LAYOUT_SEMANAL")) {
			consultarHorarioAulaProfessor();
		}
	}

	private void persistirLayoutPadrao(String valor) throws Exception {
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao(valor, "meusHorariosProfessor",
				"designMeusHorariosProfessor", getUsuarioLogado());
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao(getOrdenacao(), "meusHorariosProfessor",
				"ordenacao", getUsuarioLogado());
	}

	private void verificarLayoutPadrao() throws Exception {
		LayoutPadraoVO layoutPadraoVO = new LayoutPadraoVO();
		layoutPadraoVO = getFacadeFactory().getLayoutPadraoFacade().consultarPorEntidadeCampo("meusHorariosProfessor",
				"designMeusHorariosProfessor", false, getUsuarioLogado());
		if (!layoutPadraoVO.getValor().equals("")) {
			setLayout(layoutPadraoVO.getValor());
			layoutPadraoVO = getFacadeFactory().getLayoutPadraoFacade()
					.consultarPorEntidadeCampo("meusHorariosProfessor", "ordenacao", false, getUsuarioLogado());
			if (Uteis.isAtributoPreenchido(layoutPadraoVO.getValor())) {
				setOrdenacao(layoutPadraoVO.getValor());
			}
		}
	}

	public void consultarProfessoresCoordenador() {
		List objs = new ArrayList(0);
		if (getValorConsultaProfessor().equals("")) {
			setMensagemID("msg_entre_prmconsulta");
			return;
		}
		try {
			objs = getFacadeFactory().getFuncionarioFacade().consultarProfessoresCoordenadorPorPeriodo(
					getValorConsultaProfessor(), getCampoConsultaProfessor(), getUsuarioLogado());
			setListaSelectItemProfessoresTurma(objs);
			setTipoConsulta("professor");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaSelectItemProfessoresTurma(new ArrayList(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	/**
	 * Validar quantidade de permissões para apresentar os botoes do funcionario
	 * cargo.
	 * 
	 * @return
	 */
	public boolean validarQuantidadePermissoesBotoesFuncionario() {
		// Inicia com 2 o botaão de editar e excluir.
		int contador = 2;
		contador = getPermiteAcessarMarcacaoFerias() ? ++contador : contador;
		contador = getPermiteAcessarFichaFinanceira() ? ++contador : contador;
		contador = getPermiteAcessarEventoEmprestimo() ? ++contador : contador;
		contador = getPermiteAcessarSalarioComposto() ? ++contador : contador;
		contador = getPermiteAcessarPeriodoAquisitivo() ? ++contador : contador;
		contador = getPermiteAcessarValeTransporte() ? ++contador : contador;
		contador = getPermiteCadastrarEvento() ? ++contador : contador;
		contador = getPermiteAlterarSituacaoCargo() ? ++contador : contador;

		return contador <= 6 ? Boolean.FALSE : Boolean.TRUE;
	}

	public Boolean getPermiteAcessarMarcacaoFerias() {
		if (permiteAcessarMarcacaoFerias == null) {
			try {
				ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico(
						"Funcionario_acessarMarcacaoFerias", getUsuarioLogado());
				permiteAcessarMarcacaoFerias = true;
			} catch (Exception e) {
				permiteAcessarMarcacaoFerias = false;
			}
		}
		return permiteAcessarMarcacaoFerias;
	}

	public void setPermiteAcessarMarcacaoFerias(Boolean permiteAcessarMarcacaoFerias) {
		this.permiteAcessarMarcacaoFerias = permiteAcessarMarcacaoFerias;
	}

	public Boolean getPermiteAcessarPeriodoAquisitivo() {
		if (permiteAcessarPeriodoAquisitivo == null) {
			try {
				ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico(
						"Funcionario_acessarPeriodoAquisitivo", getUsuarioLogado());
				permiteAcessarPeriodoAquisitivo = true;
			} catch (Exception e) {
				permiteAcessarPeriodoAquisitivo = false;
			}
		}
		return permiteAcessarPeriodoAquisitivo;
	}

	public void setPermiteAcessarPeriodoAquisitivo(Boolean permiteAcessarPeriodoAquisitivo) {
		this.permiteAcessarPeriodoAquisitivo = permiteAcessarPeriodoAquisitivo;
	}

	public Boolean getPermiteAcessarEventoEmprestimo() {
		if (permiteAcessarEventoEmprestimo == null) {
			try {
				ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico(
						"Funcionario_acessarEventoEmprestimo", getUsuarioLogado());
				permiteAcessarEventoEmprestimo = true;
			} catch (Exception e) {
				permiteAcessarEventoEmprestimo = false;
			}
		}
		return permiteAcessarEventoEmprestimo;
	}

	public void setPermiteAcessarEventoEmprestimo(Boolean permiteAcessarEventoEmprestimo) {
		this.permiteAcessarEventoEmprestimo = permiteAcessarEventoEmprestimo;
	}

	public Boolean getPermiteAcessarSalarioComposto() {
		if (permiteAcessarSalarioComposto == null) {
			try {
				ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico(
						"Funcionario_acessarSalarioComposto", getUsuarioLogado());
				permiteAcessarSalarioComposto = true;
			} catch (Exception e) {
				permiteAcessarSalarioComposto = false;
			}
		}
		return permiteAcessarSalarioComposto;
	}

	public void setPermiteAcessarSalarioComposto(Boolean permiteAcessarSalarioComposto) {
		this.permiteAcessarSalarioComposto = permiteAcessarSalarioComposto;
	}

	public Boolean getPermiteAcessarValeTransporte() {
		if (permiteAcessarValeTransporte == null) {
			try {
				ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico(
						"Funcionario_acessarValeTransporte", getUsuarioLogado());
				permiteAcessarValeTransporte = true;
			} catch (Exception e) {
				permiteAcessarValeTransporte = false;
			}
		}
		return permiteAcessarValeTransporte;
	}

	public void setPermiteAcessarValeTransporte(Boolean permiteAcessarValeTransporte) {
		this.permiteAcessarValeTransporte = permiteAcessarValeTransporte;
	}

	public void scrollerListenerSindicato(DataScrollEvent dataScrollerEvent) {
		try {
			getControleConsultaOtimizado().setPaginaAtual(dataScrollerEvent.getPage());
			getControleConsultaOtimizado().setPage(dataScrollerEvent.getPage());
			consultarDados();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public List<SelectItem> getListaSelectItemSituacaoFuncionario() {
		if (listaSelectItemSituacaoFuncionario == null)
			listaSelectItemSituacaoFuncionario = new ArrayList<>();
		return listaSelectItemSituacaoFuncionario;
	}

	public void setListaSelectItemSituacaoFuncionario(List<SelectItem> listaSelectItemSituacaoFuncionario) {
		this.listaSelectItemSituacaoFuncionario = listaSelectItemSituacaoFuncionario;
	}

	public List<SelectItem> getListaSelectItemFormaContratacao() {
		if (listaSelectItemFormaContratacao == null)
			listaSelectItemFormaContratacao = new ArrayList<>();
		return listaSelectItemFormaContratacao;
	}

	public void setListaSelectItemFormaContratacao(List<SelectItem> listaSelectItemFormaContratacao) {
		this.listaSelectItemFormaContratacao = listaSelectItemFormaContratacao;
	}

	public List<SelectItem> getListaSelectItemTipoRecebimento() {
		if (listaSelectItemTipoRecebimento == null)
			listaSelectItemTipoRecebimento = new ArrayList<>();
		return listaSelectItemTipoRecebimento;
	}

	public void setListaSelectItemTipoRecebimento(List<SelectItem> listaSelectItemTipoRecebimento) {
		this.listaSelectItemTipoRecebimento = listaSelectItemTipoRecebimento;
	}

	private void montarListaSelectItemSituacaoFuncionario() {
		setListaSelectItemSituacaoFuncionario(
				UtilPropriedadesDoEnum.getListaSelectItemDoEnum(SituacaoFuncionarioEnum.class, false));
	}

	private void montarListaSelectItemFormaContratacao() {
		setListaSelectItemFormaContratacao(
				UtilPropriedadesDoEnum.getListaSelectItemDoEnum(FormaContratacaoFuncionarioEnum.class, true));
	}

	private void montarListaSelectItemTipoRecebimento() {
		setListaSelectItemTipoRecebimento(
				UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoRecebimentoEnum.class, true));
	}

	public void prepararDadosParaAlterarSituacaoDoCargoFuncionario()
			throws InstantiationException, InvocationTargetException, NoSuchMethodException {
		try {
			setFuncionarioCargoVO(
					(FuncionarioCargoVO) context().getExternalContext().getRequestMap().get("funcionarioCargoItem"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void alterarSituacaoFuncionario() {
		try {
			getFacadeFactory().getFuncionarioCargoFacade().alterarSituacaoFuncionarioCargo(getFuncionarioCargoVO(),
					getUsuarioLogado());
			setFuncionarioCargoVO(new FuncionarioCargoVO());
		} catch (Exception e) {
			try {
				getFuncionarioVO().setFuncionarioCargoVOs(getFacadeFactory().getFuncionarioCargoFacade()
						.consultarPorFuncionario(getFuncionarioVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX,
								getUsuarioLogado()));
				for (FuncionarioCargoVO funcionarioCargoVO : getFuncionarioVO().getFuncionarioCargoVOs()) {
					funcionarioCargoVO.setSecaoFolhaPagamento(Uteis.montarDadosVO(
							funcionarioCargoVO.getSecaoFolhaPagamento().getCodigo(), SecaoFolhaPagamentoVO.class,
							p -> getFacadeFactory().getSecaoFolhaPagamentoInterfaceFacade().consultarPorChavePrimaria(
									funcionarioCargoVO.getSecaoFolhaPagamento().getCodigo().longValue())));
					funcionarioCargoVO.montarCentroCusto(getFuncionarioVO());
				}
			} catch (Exception ex) {
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), ex.getMessage());
			}
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void atualizarGridCargoFuncionario() {
		try {

			getFuncionarioCargoVO().setFuncionarioVO(getFuncionarioVO());

			getFuncionarioCargoVO().setSituacaoFuncionario(getFuncionarioCargoVO().getSituacaoFuncionario());
			getFuncionarioCargoVO().setDataDemissao(getFuncionarioCargoVO().getDataDemissao());

			getFuncionarioVO().adicionarObjFuncionarioCargoVOs(getFuncionarioCargoVO());
			this.setFuncionarioCargoVO(new FuncionarioCargoVO());
			setMensagemID("msg_dados_adicionados");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setPermiteAdicionarCargo(Boolean permiteAdicionarCargo) {
		this.permiteAdicionarCargo = permiteAdicionarCargo;
	}

	public void setPermiteAlterarSituacaoCargo(Boolean permiteAlterarSituacaoCargo) {
		this.permiteAlterarSituacaoCargo = permiteAlterarSituacaoCargo;
	}

	public void setPermiteCadastrarEvento(Boolean permiteCadastrarEvento) {
		this.permiteCadastrarEvento = permiteCadastrarEvento;
	}

	public Boolean getPermiteAdicionarCargo() {
		if (permiteAdicionarCargo == null) {
			try {
				permiteAdicionarCargo = ControleAcesso.verificarPermissaoFuncionalidadeUsuario("Funcionario_adicionarCargo", getUsuarioLogado());
			} catch (Exception e) {
				permiteAdicionarCargo = false;
			}
		}
		return permiteAdicionarCargo;
	}

	public Boolean getPermiteAlterarSituacaoCargo() {
		if (permiteAlterarSituacaoCargo == null) {
			try {
				permiteAlterarSituacaoCargo = ControleAcesso.verificarPermissaoFuncionalidadeUsuario("Funcionario_alterarSituacaoCargo", getUsuarioLogado());
			} catch (Exception e) {
				permiteAlterarSituacaoCargo = false;
			}
		}
		return permiteAlterarSituacaoCargo;
	}

	public Boolean getPermiteCadastrarEvento() {

		if (permiteCadastrarEvento == null) {
			try {
				permiteCadastrarEvento = ControleAcesso.verificarPermissaoFuncionalidadeUsuario("Funcionario_cadastrarEventos", getUsuarioLogado());
			} catch (Exception e) {
				permiteCadastrarEvento = false;
			}
		}
		return permiteCadastrarEvento;

	}

	public Boolean getPermiteAcessarFichaFinanceira() {
		if (permiteAcessarFichaFinanceira == null) {
			try {
				permiteAcessarFichaFinanceira = ControleAcesso.verificarPermissaoFuncionalidadeUsuario("Funcionario_acessarFichaFinanceira", getUsuarioLogado());
			} catch (Exception e) {
				permiteAcessarFichaFinanceira = false;
			}
		}
		return permiteAcessarFichaFinanceira;
	}

	public void setPermiteAcessarFichaFinanceira(boolean permiteCadastrarFichaFinanceira) {
		this.permiteAcessarFichaFinanceira = permiteCadastrarFichaFinanceira;
	}

	public FuncionarioDependenteVO getDependenteVO() {
		if (dependenteVO == null)
			dependenteVO = new FuncionarioDependenteVO();
		return dependenteVO;
	}

	public void setDependenteVO(FuncionarioDependenteVO dependenteVO) {
		this.dependenteVO = dependenteVO;
	}

	public List getListaSelectItemGrauParentesco() {
		if (listaSelectItemGrauParentesco == null)
			listaSelectItemGrauParentesco = new ArrayList<>();
		return listaSelectItemGrauParentesco;
	}

	public void setListaSelectItemGrauParentesco(List listaSelectItemGrauParentesco) {
		this.listaSelectItemGrauParentesco = listaSelectItemGrauParentesco;
	}

	public void adicionarDependente() {
		try {
			getFacadeFactory().getFuncionarioDependenteInterfaceFacade().validarDados(dependenteVO);
			Iterator<FuncionarioDependenteVO> i = getFuncionarioVO().getDependenteVOs().iterator();
			int index = 0;
			int aux = -1;
			FuncionarioDependenteVO objAux = new FuncionarioDependenteVO();
			while (i.hasNext()) {

				FuncionarioDependenteVO objExistente = (FuncionarioDependenteVO) i.next();

				if (objExistente.getNome().equals(dependenteVO.getNome()) && !objExistente.getItemEmEdicao()) {
					throw new ConsistirException(UteisJSF.internacionalizar("prt_Dependentes_JaCadastrado"));
				}

				if (objExistente.getCodigo().equals(dependenteVO.getCodigo()) && objExistente.getItemEmEdicao()) {
					dependenteVO.setItemEmEdicao(false);
					aux = index;
					objAux = dependenteVO;
				}
				index++;
			}
			if (aux >= 0) {
				getFuncionarioVO().getDependenteVOs().set(aux, objAux);
			} else {
				getFuncionarioVO().getDependenteVOs().add(dependenteVO);
			}
			setDependenteVO(new FuncionarioDependenteVO());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void editarDependenteDoFuncionario() {

		// Clona o objeto da grid que sera editado para criar outra referencia de
		// memoria
		setFuncionarioDependenteVOEditado(
				(FuncionarioDependenteVO) context().getExternalContext().getRequestMap().get("dependenteItem"));
		getFuncionarioDependenteVOEditado().setNovoObj(false);
		getFuncionarioDependenteVOEditado().setItemEmEdicao(true);
		try {
			setDependenteVO((FuncionarioDependenteVO) BeanUtils.cloneBean(getFuncionarioDependenteVOEditado()));
		} catch (Exception e) {
			e.printStackTrace();
			setMensagemDetalhada("msg_erro", UteisJSF.internacionalizar("prt_RegistroEntrada_erro"));
		}
	}

	public String removerDependenteDoFuncionario() {
		try {
			FuncionarioDependenteVO obj = (FuncionarioDependenteVO) context().getExternalContext().getRequestMap()
					.get("dependenteItem");
			getFuncionarioVO().getDependenteVOs().remove(obj);
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public void cancelarEdicaoFuncionarioCargo() {
		setFuncionarioCargoVO(new FuncionarioCargoVO());
		limparHistoricos();
	}

	public void cancelarEdicaoObjetoFuncionarioDependente() {
		setDependenteVO(new FuncionarioDependenteVO());
	}

	public void gravarPessoaGsuite() {
		try {
			getFacadeFactory().getPessoaGsuiteFacade().persistir(getPessoaVO(), false, getUsuarioLogadoClone());
			setMensagemID(MSG_TELA.msg_dados_gravados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void adicionarPessoaGsuite() {
		try {
			getFacadeFactory().getPessoaFacade().adicionarPessoaGsuite(getPessoaVO(), getPessoaGsuiteVO(),
					getUsuarioLogadoClone());
			setPessoaGsuiteVO(new PessoaGsuiteVO());
			setMensagemID(MSG_TELA.msg_dados_adicionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void editarPessoaGsuite() {
		try {
			PessoaGsuiteVO obj = (PessoaGsuiteVO) context().getExternalContext().getRequestMap()
					.get("pessoaGsuiteItem");
			setPessoaGsuiteVO((PessoaGsuiteVO) obj.clone());
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}

	}

	public void removerPessoaGsuite() {
		try {
			PessoaGsuiteVO obj = (PessoaGsuiteVO) context().getExternalContext().getRequestMap()
					.get("pessoaGsuiteItem");
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

	public void isPermiteConfiguracaoSeiGsuite() {
		try {
			setExisteConfiguracaoSeiGsuite(getFacadeFactory().getConfiguracaoSeiGsuiteFacade()
					.consultarSeExisteConfiguracaoSeiGsuitePadrao(getUsuarioLogadoClone()));
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

	public void gravarPessoaEmailInstitucional() {
		try {
			getFacadeFactory().getPessoaEmailInstitucionalFacade().persistir(getPessoaVO(), false,
					getUsuarioLogadoClone());
			setMensagemID(MSG_TELA.msg_dados_gravados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void adicionarPessoaEmailInstitucional() {
		try {
			getFacadeFactory().getPessoaFacade().adicionarPessoaEmailInstitucionalVO(getPessoaVO(),
					getPessoaEmailInstitucionalVO(), getUsuarioLogadoClone());
			setPessoaEmailInstitucionalVO(new PessoaEmailInstitucionalVO());
			setMensagemID(MSG_TELA.msg_dados_adicionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void editarPessoaEmailInstitucional() {
		try {
			PessoaEmailInstitucionalVO obj = (PessoaEmailInstitucionalVO) context().getExternalContext().getRequestMap()
					.get("pessoaEmailInstitucionalItem");
			setPessoaEmailInstitucionalVO((PessoaEmailInstitucionalVO) obj.clone());
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}

	}

	public void removerPessoaEmailInstitucional() {
		try {
			PessoaEmailInstitucionalVO obj = (PessoaEmailInstitucionalVO) context().getExternalContext().getRequestMap()
					.get("pessoaEmailInstitucionalItem");
			getFacadeFactory().getPessoaFacade().removerPessoaEmailInstitucionalVO(getPessoaVO(), obj,
					getUsuarioLogadoClone());
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

	public String getCampoConsultaFormula() {
		if (campoConsultaFormula == null)
			campoConsultaFormula = "";
		return campoConsultaFormula;
	}

	public void setCampoConsultaFormula(String campoConsultaFormula) {
		this.campoConsultaFormula = campoConsultaFormula;
	}

	public String getValorConsultaFormula() {
		if (valorConsultaFormula == null)
			valorConsultaFormula = "";
		return valorConsultaFormula;
	}

	public void setValorConsultaFormula(String valorConsultaFormula) {
		this.valorConsultaFormula = valorConsultaFormula;
	}

	public void consultarFormula() {
		try {
			if (getCampoConsultaFormula().equals("codigo")) {
				Uteis.validarSomenteNumeroString(getControleConsulta().getValorConsulta());
			}

			List objs = new ArrayList(0);
			objs = getFacadeFactory().getFormulaFolhaPagamentoInterfaceFacade().consultarPorFiltro(
					getCampoConsultaFormula(), getValorConsultaFormula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
					getUsuarioLogado(), "ATIVO");
			setListaConsultaFormula(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}

	}

	public void consultarSecaoFolhaPagamento() {
		try {

			if (getCampoConsultaSecaoFolhaPagamento().equals(EnumCampoConsultaSecaoFolhaPagamento.CODIGO.name())) {
				if (getValorConsultaSecaoFolhaPagamento().trim().isEmpty()
						|| !Uteis.getIsValorNumerico(getValorConsultaSecaoFolhaPagamento())) {
					throw new ConsistirException(
							UteisJSF.internacionalizar("prt_SecaoFolhaPagamento_ConsultaCampoCodigoInvalido"));
				}
			}

			setListaConsultaSecaoFolhaPagamento(getFacadeFactory().getSecaoFolhaPagamentoInterfaceFacade().consultar(
					getCampoConsultaSecaoFolhaPagamento(), getValorConsultaSecaoFolhaPagamento(), false,
					Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));

			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			getControleConsultaOtimizado().getListaConsulta().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public boolean getApresentarResultadoConsultaSecao() {
		return getListaConsultaSecaoFolhaPagamento().size() > 0;
	}

	public void scrollerListenerSecaoFolhaPagamento(DataScrollEvent dataScrollerEvent) {
		try {
			getControleConsultaOtimizado().setPaginaAtual(dataScrollerEvent.getPage());
			getControleConsultaOtimizado().setPage(dataScrollerEvent.getPage());
			consultarDados();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void selecionarSecaoFolhaPagamento() {
		SecaoFolhaPagamentoVO obj = (SecaoFolhaPagamentoVO) context().getExternalContext().getRequestMap()
				.get("itemSecaoFolhaPagamento");
		funcionarioCargoVO.setSecaoFolhaPagamento(obj);
		this.getListaConsultaSecaoFolhaPagamento().clear();
	}

	public void scrollerListenerDepartamento(DataScrollEvent dataScrollerEvent) {
		try {
			getControleConsultaOtimizado().setPaginaAtual(dataScrollerEvent.getPage());
			getControleConsultaOtimizado().setPage(dataScrollerEvent.getPage());
			consultarDados();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarDepartamento() {
		try {
			super.consultar();
			List<DepartamentoVO> objs = new ArrayList<DepartamentoVO>(0);
			if (getCampoConsultaDepartamento().equals(EnumCampoConsultaDepartamento.CODIGO.toString())) {
				int valorInt = 0;
				if (!getValorConsultaDepartamento().equals("")) {
					valorInt = Integer.parseInt(getValorConsultaDepartamento());
				}
				objs = getFacadeFactory().getDepartamentoFacade().consultarPorCodigo(new Integer(valorInt), true,
						Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaDepartamento().equals(EnumCampoConsultaDepartamento.NOME.toString())) {
				objs = getFacadeFactory().getDepartamentoFacade().consultarPorNome(getValorConsultaDepartamento(), true,
						Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}

			if (getCampoConsultaDepartamento().equals(EnumCampoConsultaDepartamento.NOME_PESSOA.toString())) {
				objs = getFacadeFactory().getDepartamentoFacade().consultarPorNomePessoa(getValorConsultaDepartamento(),
						true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaDepartamento(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			getControleConsultaOtimizado().getListaConsulta().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void selecionarDepartamento() {
		DepartamentoVO obj = (DepartamentoVO) context().getExternalContext().getRequestMap().get("itemDepartamento");
		funcionarioCargoVO.setDepartamento(obj);
		this.getListaConsultaDepartamento().clear();
	}

	public boolean getApresentarResultadoConsultaDepartamento() {
		return getListaConsultaDepartamento().size() > 0;
	}

	public List getListaConsultaFormula() {
		if (listaConsultaFormula == null)
			listaConsultaFormula = new ArrayList<>();
		return listaConsultaFormula;
	}

	public void setListaConsultaFormula(List listaConsultaFormula) {
		this.listaConsultaFormula = listaConsultaFormula;
	}

	public void limparDadosConsultaFormula() {
		setListaConsultaFormula(new ArrayList<FormulaFolhaPagamentoVO>(0));
	}

	public void limparDadosDepartamento() {
		this.funcionarioCargoVO.setDepartamento(new DepartamentoVO());
	}

	public List<SelectItem> getTipoConsultaComboFormula() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("identificador", "Identificador"));
		itens.add(new SelectItem("descricao", "Descrição"));
		return itens;
	}

	public void setTipoConsultaComboFormula(List<SelectItem> tipoConsultaComboFormula) {
	}

	public String getCampoConsultaSecaoFolhaPagamento() {
		return campoConsultaSecaoFolhaPagamento;
	}

	public void setCampoConsultaSecaoFolhaPagamento(String campoConsultaSecaoFolhaPagamento) {
		this.campoConsultaSecaoFolhaPagamento = campoConsultaSecaoFolhaPagamento;
	}

	public String getValorConsultaSecaoFolhaPagamento() {
		return valorConsultaSecaoFolhaPagamento;
	}

	public void setValorConsultaSecaoFolhaPagamento(String valorConsultaSecaoFolhaPagamento) {
		this.valorConsultaSecaoFolhaPagamento = valorConsultaSecaoFolhaPagamento;
	}

	public List getListaConsultaSecaoFolhaPagamento() {
		if (listaConsultaSecaoFolhaPagamento == null) {
			listaConsultaSecaoFolhaPagamento = new ArrayList<>();
		}
		return listaConsultaSecaoFolhaPagamento;
	}

	public void setListaConsultaSecaoFolhaPagamento(List listaConsultaSecaoFolhaPagamento) {
		this.listaConsultaSecaoFolhaPagamento = listaConsultaSecaoFolhaPagamento;
	}

	public List<SelectItem> getListaSelectItemNivelSalarial() {
		try {
			if (Uteis.isAtributoPreenchido(getFuncionarioCargoVO().getCargo().getCodigo())) {
				listaSelectItemNivelSalarial = new ArrayList<>(0);
				CargoVO cargoVO = getFacadeFactory().getCargoFacade().consultarPorChavePrimaria(
						getFuncionarioCargoVO().getCargo().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
						getUsuarioLogado());
				List<ProgressaoSalarialItemVO> lista = getFacadeFactory().getProgressaoSalarialItemInterfaceFacade()
						.consultarPorProgressaoSalarial(cargoVO.getProgressaoSalarial().getCodigo());
				listaSelectItemNivelSalarial.add(new SelectItem(0, ""));
				for (ProgressaoSalarialItemVO progressaoSalarial : lista) {
					listaSelectItemNivelSalarial.add(new SelectItem(progressaoSalarial.getNivelSalarialVO().getCodigo(),
							progressaoSalarial.getNivelSalarialVO().getDescricao()));
				}

				if (listaSelectItemNivelSalarial.size() <= 1) {
					setListaSelectItemFaixaSalarial(new ArrayList<>());
					getFuncionarioCargoVO().setNivelSalarial(new NivelSalarialVO());
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}

		return listaSelectItemNivelSalarial;
	}

	public void setListaSelectItemNivelSalarial(List<SelectItem> listaSelectItemNivelSalarial) {
		this.listaSelectItemNivelSalarial = listaSelectItemNivelSalarial;
	}

	public List<SelectItem> getListaSelectItemFaixaSalarial() {
		try {
			if (Uteis.isAtributoPreenchido(getFuncionarioCargoVO().getNivelSalarial().getCodigo())) {
				listaSelectItemFaixaSalarial = new ArrayList<>(0);
				listaProgressaoSalarialItemVOs = getFacadeFactory().getProgressaoSalarialItemInterfaceFacade()
						.consultarPorProgressaoSalarialPorNivel(getFuncionarioCargoVO().getNivelSalarial().getCodigo());
				listaSelectItemFaixaSalarial.add(new SelectItem(0, ""));
				for (ProgressaoSalarialItemVO progressaoSalarial : listaProgressaoSalarialItemVOs) {
					listaSelectItemFaixaSalarial.add(new SelectItem(progressaoSalarial.getFaixaSalarialVO().getCodigo(),
							progressaoSalarial.getFaixaSalarialVO().getDescricao()));
				}
			} else {
				getFuncionarioCargoVO().setFaixaSalarial(new FaixaSalarialVO());
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}

		return listaSelectItemFaixaSalarial;
	}

	/**
	 * Atualiza o salario caso a faixa salarial seja selecionado.
	 */
	public void salarioPorFaixaSalarial() {
		if (Uteis.isAtributoPreenchido(getFuncionarioCargoVO().getFaixaSalarial().getCodigo())) {
			BigDecimal salario = getFacadeFactory().getProgressaoSalarialItemInterfaceFacade()
					.consultarSalarioPorNivelFaixaProgressao(getFuncionarioCargoVO().getNivelSalarial().getCodigo(),
							getFuncionarioCargoVO().getFaixaSalarial().getCodigo(),
							getFuncionarioCargoVO().getCargo().getProgressaoSalarial().getCodigo());
			getFuncionarioCargoVO().setSalario(salario);

		} else {
			getFuncionarioCargoVO().setSalario(BigDecimal.ZERO);
		}
	}

	public void setListaSelectItemFaixaSalarial(List<SelectItem> listaSelectItemFaixaSalarial) {
		this.listaSelectItemFaixaSalarial = listaSelectItemFaixaSalarial;
	}

	public FuncionarioCargoVO getFuncionarioCargoVOEditado() {
		if (funcionarioCargoVOEditado == null)
			funcionarioCargoVOEditado = new FuncionarioCargoVO();
		return funcionarioCargoVOEditado;
	}

	public void setFuncionarioCargoVOEditado(FuncionarioCargoVO funcionarioCargoVOEditado) {
		this.funcionarioCargoVOEditado = funcionarioCargoVOEditado;
	}

	public FuncionarioDependenteVO getFuncionarioDependenteVOEditado() {
		if (funcionarioDependenteVOEditado == null)
			funcionarioDependenteVOEditado = new FuncionarioDependenteVO();
		return funcionarioDependenteVOEditado;
	}

	public void setFuncionarioDependenteVOEditado(FuncionarioDependenteVO funcionarioDependenteVOEditado) {
		this.funcionarioDependenteVOEditado = funcionarioDependenteVOEditado;
	}

	public String getCampoConsultaDepartamento() {
		if (campoConsultaDepartamento == null) {
			campoConsultaDepartamento = "";
		}
		return campoConsultaDepartamento;
	}

	public void setCampoConsultaDepartamento(String campoConsultaDepartamento) {
		this.campoConsultaDepartamento = campoConsultaDepartamento;
	}

	public String getValorConsultaDepartamento() {
		if (valorConsultaDepartamento == null) {
			valorConsultaDepartamento = "";
		}
		return valorConsultaDepartamento;
	}

	public void setValorConsultaDepartamento(String valorConsultaDepartamento) {
		this.valorConsultaDepartamento = valorConsultaDepartamento;
	}

	public List getListaConsultaDepartamento() {
		if (listaConsultaDepartamento == null) {
			listaConsultaDepartamento = new ArrayList<DepartamentoVO>(0);
		}
		return listaConsultaDepartamento;
	}

	public void setListaConsultaDepartamento(List listaConsultaDepartamento) {
		this.listaConsultaDepartamento = listaConsultaDepartamento;
	}

	public void chamarTelaDeEventosFixosDoFuncionario() {
		FuncionarioCargoVO obj = (FuncionarioCargoVO) context().getExternalContext().getRequestMap()
				.get("funcionarioCargoItem");
		if (obj != null && !obj.getCodigo().equals(0)) {
			context().getExternalContext().getSessionMap().put("funcionarioCargo", obj);
		}
		removerControleMemoriaFlashTela(EventoFixoCargoFuncionarioControle.class.getSimpleName());
	}

	public void chamarTelaDeEventosEmprestimoDoFuncionario() {
		FuncionarioCargoVO obj = (FuncionarioCargoVO) context().getExternalContext().getRequestMap()
				.get("funcionarioCargoItem");
		if (obj != null && !obj.getCodigo().equals(0)) {
			context().getExternalContext().getSessionMap().put("funcionarioCargo", obj);
		}
		removerControleMemoriaFlashTela(EventoEmprestimoCargoFuncionarioControle.class.getSimpleName());
	}

	public void chamarTelaDeEventosSalarioCompostoDoFuncionario() {
		FuncionarioCargoVO obj = (FuncionarioCargoVO) context().getExternalContext().getRequestMap()
				.get("funcionarioCargoItem");
		if (obj != null && !obj.getCodigo().equals(0)) {
			context().getExternalContext().getSessionMap().put("funcionarioCargo", obj);
		}
		removerControleMemoriaFlashTela(SalarioCompostoControle.class.getSimpleName());
	}

	public void chamarTelaDeEventosValeTransporteDoFuncionario() {
		FuncionarioCargoVO obj = (FuncionarioCargoVO) context().getExternalContext().getRequestMap()
				.get("funcionarioCargoItem");
		if (obj != null && !obj.getCodigo().equals(0)) {
			context().getExternalContext().getSessionMap().put("funcionarioCargo", obj);
		}
		removerControleMemoriaFlashTela(EventoValeTransporteFuncionarioCargoControle.class.getSimpleName());
	}

	public void chamarTelaContraCheque() {
		FuncionarioCargoVO obj = (FuncionarioCargoVO) context().getExternalContext().getRequestMap()
				.get("funcionarioCargoItem");
		if (obj != null && !obj.getCodigo().equals(0)) {
			context().getExternalContext().getSessionMap().put("funcionarioCargo", obj);
		}
		removerControleMemoriaFlashTela(EventoFixoCargoFuncionarioControle.class.getSimpleName());
	}

	public void apresentarPossuiCargoComissionado() {
		if (Uteis.isAtributoPreenchido(getFuncionarioCargoVO().getFormaContratacao())) {
			if (!getFuncionarioCargoVO().getFormaContratacao().equals(FormaContratacaoFuncionarioEnum.ESTATUTARIO)) {
				getFuncionarioCargoVO().setComissionado(false);
			}
		}
	}

	public void limparFormulaFixa() {
		getDependenteVO().getFormulaCalculo().setCodigo(null);
		getDependenteVO().getFormulaCalculo().setIdentificador("");
		getDependenteVO().getFormulaCalculo().setDescricao("");
	}

	public void selecionarFormula() {

		FormulaFolhaPagamentoVO formula = (FormulaFolhaPagamentoVO) context().getExternalContext().getRequestMap()
				.get("formulaItens");

		getDependenteVO().getFormulaCalculo().setCodigo(formula.getCodigo());
		getDependenteVO().getFormulaCalculo().setIdentificador(formula.getIdentificador());
		getDependenteVO().getFormulaCalculo().setDescricao(formula.getDescricao());
	}

	public void validarDadosDependente() {
		getFacadeFactory().getFuncionarioDependenteInterfaceFacade().validarDadosDependente(getDependenteVO());
	}

	public List<SelectItem> getTipoConsultaComboEvento() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("descricao", UteisJSF.internacionalizar("prt_TextoPadrao_descricao")));
		itens.add(new SelectItem("identificador", UteisJSF.internacionalizar("prt_TextoPadrao_identificador")));
		return itens;
	}

	public void consultarEvento() {
		try {
			if (getCampoConsultaEvento().equals("codigo")) {
				Uteis.validarSomenteNumeroString(getControleConsulta().getValorConsulta());
			}

			setListaEventosFolhaPagamento(
					getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().consultarPorFiltro(campoConsultaEvento,
							valorConsultaEvento, "ATIVO", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public String getCampoConsultaEvento() {
		if (campoConsultaEvento == null)
			campoConsultaEvento = "";
		return campoConsultaEvento;
	}

	public void setCampoConsultaEvento(String campoConsultaEvento) {
		this.campoConsultaEvento = campoConsultaEvento;
	}

	public String getValorConsultaEvento() {
		if (valorConsultaEvento == null)
			valorConsultaEvento = "";
		return valorConsultaEvento;
	}

	public void setValorConsultaEvento(String valorConsultaEvento) {
		this.valorConsultaEvento = valorConsultaEvento;
	}

	public List<EventoFolhaPagamentoVO> getListaEventosFolhaPagamento() {
		if (listaEventosFolhaPagamento == null)
			listaEventosFolhaPagamento = new ArrayList<>();
		return listaEventosFolhaPagamento;
	}

	public void setListaEventosFolhaPagamento(List<EventoFolhaPagamentoVO> listaEventosFolhaPagamento) {
		this.listaEventosFolhaPagamento = listaEventosFolhaPagamento;
	}

	public void selecionarEvento() {
		EventoFolhaPagamentoVO obj = (EventoFolhaPagamentoVO) context().getExternalContext().getRequestMap()
				.get("eventoItem");
		getDependenteVO().setEventoFolhaPagamento(obj);

		valorConsultaEvento = "";
		campoConsultaEvento = "";
		getListaEventosFolhaPagamento().clear();
	}

	public void consultarEventoPorIdentificador() {
		try {
			if (Uteis.isAtributoPreenchido(this.getDependenteVO().getEventoFolhaPagamento().getIdentificador())) {
				this.getDependenteVO().setEventoFolhaPagamento(
						getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().consultarPorChaveIdentificador(
								this.getDependenteVO().getEventoFolhaPagamento().getIdentificador(), false,
								getUsuarioLogado()));
			}
		} catch (Exception e) {
			this.getDependenteVO().setEventoFolhaPagamento(new EventoFolhaPagamentoVO());
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void gravarSituacoesHistorico() {
		try {
			FuncionarioCargoVO.validarDados(getFuncionarioCargoVO());

			if (getFuncionarioCargoVO().getUtilizaRH()) {
				if (getFuncionarioCargoVO().getCodigo() == 0) {
					adicionarFuncionarioCargo();
					setAbrirpopup(false);
				} else {
					if (validarHistoricoSecao() || validarHistoricoCargo() || validarHistoricoSalario()
							|| validarHistoricoSituacao()) {
						setAbrirpopup(true);
						return;
					}
					adicionarFuncionarioCargo();
					cancelarEdicaoFuncionarioCargo();
					setAbrirpopup(false);
				}

			} else {
				adicionarFuncionarioCargo();
				setAbrirpopup(false);
			}

		} catch (Exception e) {
			setAbrirpopup(false);
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public String abrirPopUpSituacoesHistorico() {
		if (getAbrirpopup()) {
			return "RichFaces.$('panelSituacoesHistoricos').show();";
		}
		return "RichFaces.$('panelSituacoesHistoricos').hide();";
	}

	public boolean validarHistoricoSecao() {
		if (!Uteis.isAtributoPreenchido(getFuncionarioCargoVO().getSecaoFolhaPagamento())
				&& getFuncionarioCargoVO().getSecaoFolhaPagamento().getCodigo() != 0) {
			return Boolean.TRUE;
		}

		if (!Uteis.isAtributoPreenchido(getFuncionarioCargoVO().getSecaoFolhaPagamento())) {
			return Boolean.FALSE;
		}
		return !getFuncionarioCargoVO().getSecaoFolhaPagamento().getCodigo()
				.equals(getFuncionarioCargoHistorico().getSecaoFolhaPagamento().getCodigo());
	}

	public boolean validarHistoricoCargo() {
		if (!Uteis.isAtributoPreenchido(getFuncionarioCargoVO().getCargo())
				&& getFuncionarioCargoVO().getCargo().getCodigo() != 0) {
			return Boolean.TRUE;
		}
		if (!Uteis.isAtributoPreenchido(getFuncionarioCargoVO().getCargo())) {
			return Boolean.FALSE;
		}
		return !getFuncionarioCargoVO().getCargo().getCodigo()
				.equals(getFuncionarioCargoHistorico().getCargo().getCodigo());
	}

	public boolean validarHistoricoSalario() {
		if (!Uteis.isAtributoPreenchido(getFuncionarioCargoVO().getSalario())
				&& getFuncionarioCargoVO().getSalario() != BigDecimal.ZERO) {
			return Boolean.TRUE;
		}
		if (!Uteis.isAtributoPreenchido(getFuncionarioCargoVO().getSalario())) {
			return Boolean.FALSE;
		}
		return !getFuncionarioCargoVO().getSalario().setScale(2, BigDecimal.ROUND_UP)
				.equals(getFuncionarioCargoHistorico().getSalario().setScale(2, BigDecimal.ROUND_UP));
	}

	public boolean validarHistoricoSituacao() {
		if (!Uteis.isAtributoPreenchido(getFuncionarioCargoVO().getSituacaoFuncionario())) {
			return Boolean.TRUE;
		}
		if (!Uteis.isAtributoPreenchido(getFuncionarioCargoVO().getSituacaoFuncionario())) {
			return Boolean.FALSE;
		}
		return !getFuncionarioCargoVO().getSituacaoFuncionario()
				.equals(getFuncionarioCargoHistorico().getSituacaoFuncionario());
	}

	public void gravarAlteracoesFuncionarioCargo() {
		try {
			validacoesHistoricos();
			validarGerarHistorico();
			adicionarFuncionarioCargo();
			setAbrirpopup(false);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	private void validarGerarHistorico() {
		getFuncionarioCargoVO().getHistoricoFuncaoVO().setGerarHistorico(validarHistoricoCargo());
		getFuncionarioCargoVO().getHistoricoSalarialVO().setGerarHistorico(validarHistoricoSalario());
		getFuncionarioCargoVO().getHistoricoSecaoVO().setGerarHistorico(validarHistoricoSecao());
		getFuncionarioCargoVO().getHistoricoSituacaoVO().setGerarHistorico(validarHistoricoSituacao());
	}

	public void validacoesHistoricos() throws Exception {
		if (!Uteis.isAtributoPreenchido(getFuncionarioCargoVO().getHistoricoSalarialVO().getMotivoMudanca())
				&& validarHistoricoSalario()) {
			throw new Exception(UteisJSF.internacionalizar("msg_HistoricoFuncao_motivoMudancaSalarial"));
		}

		if (!Uteis.isAtributoPreenchido(getFuncionarioCargoVO().getHistoricoFuncaoVO().getMotivoMudanca())
				&& validarHistoricoCargo()) {
			throw new Exception(UteisJSF.internacionalizar("msg_HistoricoFuncao_motivoMudancaCargo"));
		}

		if (!Uteis.isAtributoPreenchido(getFuncionarioCargoVO().getHistoricoSecaoVO().getMotivoMudanca())
				&& validarHistoricoSecao()) {
			throw new Exception(UteisJSF.internacionalizar("msg_HistoricoFuncao_motivoMudancaSecao"));
		}

		if (!Uteis.isAtributoPreenchido(getFuncionarioCargoVO().getHistoricoSituacaoVO().getMotivoMudanca())
				&& validarHistoricoSituacao()) {
			throw new Exception(UteisJSF.internacionalizar("msg_HistoricoFuncao_motivoMudancaSituacao"));
		}
	}

	public void limparHistoricos() {
		getFuncionarioCargoVO().setHistoricoFuncaoVO(new HistoricoFuncaoVO());
		getFuncionarioCargoVO().setHistoricoSalarialVO(new HistoricoSalarialVO());
		getFuncionarioCargoVO().setHistoricoSecaoVO(new HistoricoSecaoVO());
	}

	public void limparCamposNaoUtilizadoRH() {
		if (!getFuncionarioCargoVO().getUtilizaRH()) {
			getFuncionarioCargoVO().setFormaContratacao(null);
			getFuncionarioCargoVO().setTipoRecebimento(null);
			getFuncionarioCargoVO().setSecaoFolhaPagamento(new SecaoFolhaPagamentoVO());
			getFuncionarioCargoVO().setSalario(null);
			getFuncionarioCargoVO().setJornada(null);
			getFuncionarioCargoVO().setNivelSalarial(new NivelSalarialVO());
			getFuncionarioCargoVO().setFaixaSalarial(new FaixaSalarialVO());
		}
	}

	public void consultarSindicato() {
		try {
			super.consultar();
			getControleConsultaOtimizado().preencherDadosParaConsulta(true, Uteis.NIVELMONTARDADOS_COMBOBOX,
					getUsuarioLogado());
			getControleConsultaOtimizado().setCampoConsulta(getCampoConsultaSindicato());
			getControleConsultaOtimizado().setValorConsulta(getValorConsultaSindicato());
			getFacadeFactory().getSindicatoInterfaceFacade().consultarPorFiltro(getControleConsultaOtimizado());

			setListaConsultaSindicato((List<SindicatoVO>) getControleConsultaOtimizado().getListaConsulta());
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			getControleConsultaOtimizado().getListaConsulta().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void selecionarSindicato() {
		SindicatoVO obj = (SindicatoVO) context().getExternalContext().getRequestMap().get("itemSindicato");
		funcionarioCargoVO.setSindicatoVO(obj);
		this.getListaConsultaSindicato().clear();
	}

	public boolean getApresentarResultadoConsultaSindicato() {
		if (Uteis.isAtributoPreenchido(getListaConsultaSindicato())) {
			return getListaConsultaSindicato().size() > 0;
		}
		return false;
	}

	public void chamarTelaMarcacaoFerias() {
		FuncionarioCargoVO obj = (FuncionarioCargoVO) context().getExternalContext().getRequestMap()
				.get("funcionarioCargoItem");
		if (obj != null && !obj.getCodigo().equals(0)) {
			context().getExternalContext().getSessionMap().put("funcionarioCargo", obj);
		}
		removerControleMemoriaFlashTela(MarcacaoFeriasControle.class.getSimpleName());
	}

	public void chamarTelaPeriodoAquisitivo() {
		FuncionarioCargoVO obj = (FuncionarioCargoVO) context().getExternalContext().getRequestMap()
				.get("funcionarioCargoItem");
		if (obj != null && !obj.getCodigo().equals(0)) {
			context().getExternalContext().getSessionMap().put("funcionarioCargo", obj);
		}
		removerControleMemoriaFlashTela(PeriodoAquisitivoFeriasControle.class.getSimpleName());
	}

	public void limparDadosSindicato() {
		getFuncionarioCargoVO().setSindicatoVO(new SindicatoVO());
	}

	public String getCampoConsultaSindicato() {
		if (campoConsultaSindicato == null) {
			campoConsultaSindicato = "";
		}
		return campoConsultaSindicato;
	}

	public void setCampoConsultaSindicato(String campoConsultaSindicato) {
		this.campoConsultaSindicato = campoConsultaSindicato;
	}

	public String getValorConsultaSindicato() {
		if (valorConsultaSindicato == null) {
			valorConsultaSindicato = "";
		}
		return valorConsultaSindicato;
	}

	public void setValorConsultaSindicato(String valorConsultaSindicato) {
		this.valorConsultaSindicato = valorConsultaSindicato;
	}

	public List<SindicatoVO> getListaConsultaSindicato() {
		if (listaConsultaSindicato == null) {
			listaConsultaSindicato = new ArrayList<>();
		}
		return listaConsultaSindicato;
	}

	public void setListaConsultaSindicato(List<SindicatoVO> listaConsultaSindicato) {
		this.listaConsultaSindicato = listaConsultaSindicato;
	}

	public FuncionarioCargoVO getFuncionarioCargoHistorico() {
		if (funcionarioCargoHistorico == null) {
			funcionarioCargoHistorico = new FuncionarioCargoVO();
		}
		return funcionarioCargoHistorico;
	}

	public void setFuncionarioCargoHistorico(FuncionarioCargoVO funcionarioCargoHistorico) {
		this.funcionarioCargoHistorico = funcionarioCargoHistorico;
	}

	public List<HistoricoFuncaoVO> getHistoricoFuncaoVOs() {
		if (historicoFuncaoVOs == null) {
			historicoFuncaoVOs = new ArrayList<>();
		}
		return historicoFuncaoVOs;
	}

	public void setHistoricoFuncaoVOs(List<HistoricoFuncaoVO> historicoFuncaoVOs) {
		this.historicoFuncaoVOs = historicoFuncaoVOs;
	}

	public List<HistoricoSalarialVO> getHistoricoSalarialVOs() {
		if (historicoSalarialVOs == null) {
			historicoSalarialVOs = new ArrayList<>();
		}
		return historicoSalarialVOs;
	}

	public void setHistoricoSalarialVOs(List<HistoricoSalarialVO> historicoSalarialVOs) {
		this.historicoSalarialVOs = historicoSalarialVOs;
	}

	public List<HistoricoSecaoVO> getHistoricoSecaoVOs() {
		if (historicoSecaoVOs == null) {
			historicoSecaoVOs = new ArrayList<>();
		}
		return historicoSecaoVOs;
	}

	public void setHistoricoSecaoVOs(List<HistoricoSecaoVO> historicoSecaoVOs) {
		this.historicoSecaoVOs = historicoSecaoVOs;
	}

	public List<HistoricoDependentesVO> getHistoricoDependentesVOs() {
		if (historicoDependentesVOs == null) {
			historicoDependentesVOs = new ArrayList<>();
		}
		return historicoDependentesVOs;
	}

	public void setHistoricoDependentesVOs(List<HistoricoDependentesVO> historicoDependentesVOs) {
		this.historicoDependentesVOs = historicoDependentesVOs;
	}

	public boolean getAbrirpopup() {
		return abrirpopup;
	}

	public void setAbrirpopup(boolean abrirpopup) {
		this.abrirpopup = abrirpopup;
	}

	public String getOrdenacao() {
		if (ordenacao == null) {
			ordenacao = "";
		}
		return ordenacao;
	}

	public void setOrdenacao(String ordenacao) {
		this.ordenacao = ordenacao;
	}

	public List<SelectItem> getTipoOrdenacaoCombo() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("unidadeEnsino", "Unidade de Ensino e Data e Hora da Aula"));
		itens.add(new SelectItem("data", "Data e Hora da Aula"));
		itens.add(new SelectItem("disciplina", "Disciplina e Data e Hora da Aula"));
		return itens;
	}

	public void executarCapturarFotoWebCam() {
		try {
			HttpSession session = (HttpSession) context().getExternalContext().getSession(true);
			getPessoaVO().getArquivoImagem().setCpfRequerimento(getPessoaVO().getCPF());
			String arquivoFoto = getFacadeFactory().getArquivoHelper().getArquivoUploadFoto(
					getPessoaVO().getArquivoImagem(), getConfiguracaoGeralPadraoSistema(),
					PastaBaseArquivoEnum.IMAGEM_TMP, getUsuarioLogado());
			String arquivoExterno = getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo() + File.separator
					+ PastaBaseArquivoEnum.IMAGEM_TMP.getValue() + File.separator + getPessoaVO().getCPF()
					+ File.separator + getPessoaVO().getArquivoImagem().getNome();
			session.setAttribute("arquivoFoto", arquivoFoto);
			setExibirBotao(Boolean.TRUE);
			setExibirUpload(false);
			setCaminhoFotoUsuario(arquivoExterno);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public String getShowFotoCrop() {
		try {
			if (getPessoaVO().getArquivoImagem().getNome() == null) {
				return "resources/imagens/usuarioPadrao.jpg";
			}
			return getCaminhoFotoUsuario() + "?UID=" + new Date().getTime();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			return Uteis.getTagImageComFotoPadrao();
		}
	}

	public void recortarFoto() {
		try {
			getFacadeFactory().getArquivoHelper().recortarFoto(getPessoaVO().getArquivoImagem(),
					getConfiguracaoGeralPadraoSistema(), getLargura(), getAltura(), getX(), getY());
			inicializarBooleanoFoto();
			gravar();
			setCaminhoFotoUsuario(getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(
					getPessoaVO().getArquivoImagem(), PastaBaseArquivoEnum.IMAGEM.getValue(),
					getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(), "foto_usuario.jpg", false));
			getPessoaVO().getArquivoImagem().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.IMAGEM);
			setOncompleteModal("RichFaces.$('panelImagem').hide();");
		} catch (Exception ex) {
			setOncompleteModal("RichFaces.$('panelImagem').show();");
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), ex.getMessage(), Uteis.ERRO);
		}
	}

	public void inicializarBooleanoFoto() {
		setRemoverFoto((Boolean) false);
		setExibirUpload(true);
		setExibirBotao(false);
	}

	public void renderizarUpload() {
		setExibirUpload(false);
	}

	public String getMatriculaApresentarExclusaoFormacaoAcademica() {
		if (matriculaApresentarExclusaoFormacaoAcademica == null) {
			matriculaApresentarExclusaoFormacaoAcademica = "";
		}
		return matriculaApresentarExclusaoFormacaoAcademica;
	}

	public void setMatriculaApresentarExclusaoFormacaoAcademica(String matriculaApresentarExclusaoFormacaoAcademica) {
		this.matriculaApresentarExclusaoFormacaoAcademica = matriculaApresentarExclusaoFormacaoAcademica;
	}

	public DocumetacaoPessoaVO getDocumetacaoPessoaVO() {
		if (documetacaoPessoaVO == null) {
			documetacaoPessoaVO = new DocumetacaoPessoaVO();
		}
		return documetacaoPessoaVO;
	}

	public void setDocumetacaoPessoaVO(DocumetacaoPessoaVO documetacaoPessoaVO) {
		this.documetacaoPessoaVO = documetacaoPessoaVO;
	}

	public String getNomeArquivo() {
		if (nomeArquivo == null) {
			nomeArquivo = "";
		}
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}
	
	public void setarNomeBatismo() {
		if (!Uteis.isAtributoPreenchido(getPessoaVO().getNomeBatismo())) {
			getPessoaVO().setNomeBatismo(getPessoaVO().getNome());
		}
	}

	public void setarNomeSocial() {
		if (!Uteis.isAtributoPreenchido(getPessoaVO().getNome())) {
			getPessoaVO().setNome(getPessoaVO().getNomeBatismo());
		}
	}

	public void rotacionar90GrausParaEsquerda() {
		try {
			getFacadeFactory().getArquivoHelper().rotacionar90GrausParaEsquerda(getPessoaVO().getArquivoImagem(),
					getConfiguracaoGeralPadraoSistema());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}

	public void rotacionar90GrausParaDireita() {
		try {
			getFacadeFactory().getArquivoHelper().rotacionar90GrausParaDireita(getPessoaVO().getArquivoImagem(),
					getConfiguracaoGeralPadraoSistema());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}

	public void rotacionar180Graus() {
		try {
			getFacadeFactory().getArquivoHelper().rotacionar180Graus(getPessoaVO().getArquivoImagem(),
					getConfiguracaoGeralPadraoSistema());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}

	public void executarZoomIn() {
		try {
			getFacadeFactory().getArquivoHelper().executarZoomImagemRequerimento("in", getPessoaVO().getArquivoImagem(),
					getConfiguracaoGeralPadraoSistema());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}

	public void executarZoomOut() {
		try {
			getFacadeFactory().getArquivoHelper().executarZoomImagemRequerimento("out",
					getPessoaVO().getArquivoImagem(), getConfiguracaoGeralPadraoSistema());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}

	public void recuperarDadosFuncionario() {
		setFuncionarioCargoVO(new FuncionarioCargoVO());
	}

	public String getAbrirModalInclusaoArquivoVerso() {
		if (abrirModalInclusaoArquivoVerso == null) {
			abrirModalInclusaoArquivoVerso = "";
		}
		return abrirModalInclusaoArquivoVerso;
	}

	public void setAbrirModalInclusaoArquivoVerso(String abrirModalInclusaoArquivoVerso) {
		this.abrirModalInclusaoArquivoVerso = abrirModalInclusaoArquivoVerso;
	}

	public DocumetacaoPessoaVO getDocumetacaoPessoaVOAux() {
		return documetacaoPessoaVOAux;
	}

	public void setDocumetacaoPessoaVOAux(DocumetacaoPessoaVO documetacaoPessoaVOAux) {
		this.documetacaoPessoaVOAux = documetacaoPessoaVOAux;
	}

	public void limparDocumentacaoPessoaVO() {
		try {
			getDocumetacaoPessoaVO().setArquivoVO(new ArquivoVO());
			getDocumetacaoPessoaVO().setArquivoVOVerso(new ArquivoVO());
			getDocumetacaoPessoaVO().setEntregue(false);
			setDocumetacaoPessoaVO(new DocumetacaoPessoaVO());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparDocumentacaoPessoaVOVerso() {
		getDocumetacaoPessoaVO().setArquivoVOVerso(new ArquivoVO());
	}

	public List<SelectItem> getListaTipoDocumentoUtilizarDocumentoFuncionario() {
		if (listaTipoDocumentoUtilizarDocumentoFuncionario == null) {
			listaTipoDocumentoUtilizarDocumentoFuncionario = new ArrayList<SelectItem>();
		}
		return listaTipoDocumentoUtilizarDocumentoFuncionario;
	}

	public void setListaTipoDocumentoUtilizarDocumentoFuncionario(
			List<SelectItem> listaTipoDocumentoUtilizarDocumentoFuncionario) {
		this.listaTipoDocumentoUtilizarDocumentoFuncionario = listaTipoDocumentoUtilizarDocumentoFuncionario;
	}

	public Integer getTipoDocumentoVOSelecionado() {
		if (tipoDocumentoVOSelecionado == null) {
			tipoDocumentoVOSelecionado = 0;
		}
		return tipoDocumentoVOSelecionado;
	}

	public void setTipoDocumentoVOSelecionado(Integer tipoDocumentoVOSelecionado) {
		this.tipoDocumentoVOSelecionado = tipoDocumentoVOSelecionado;
	}

	public List<TipoDocumentoVO> getListaTipoDocumento() {
		if (listaTipoDocumento == null) {
			listaTipoDocumento = new ArrayList<TipoDocumentoVO>();
		}
		return listaTipoDocumento;
	}

	public void setListaTipoDocumento(List<TipoDocumentoVO> listaTipoDocumento) {
		this.listaTipoDocumento = listaTipoDocumento;
	}

	public List<TipoDocumentoVO> getListaTipoDocumentoAux() {
		if (listaTipoDocumentoAux == null) {
			listaTipoDocumentoAux = new ArrayList<TipoDocumentoVO>();
		}
		return listaTipoDocumentoAux;
	}

	public void setListaTipoDocumentoAux(List<TipoDocumentoVO> listaTipoDocumentoAux) {
		this.listaTipoDocumentoAux = listaTipoDocumentoAux;
	}

	public TipoDocumentoVO getTipoDocumentoRemover() {
		if (tipoDocumentoRemover == null) {
			tipoDocumentoRemover = new TipoDocumentoVO();
		}
		return tipoDocumentoRemover;
	}

	public void setTipoDocumentoRemover(TipoDocumentoVO tipoDocumentoRemover) {
		this.tipoDocumentoRemover = tipoDocumentoRemover;
	}

	public Boolean getAssinarDocumentoDigitalmente() {
		return assinarDocumentoDigitalmente;
	}

	public void setAssinarDocumentoDigitalmente(Boolean assinarDocumentoDigitalmente) {
		this.assinarDocumentoDigitalmente = assinarDocumentoDigitalmente;
	}

	public String getSizeChaveEnderecamentoPix() {
		return getSizeChavePix(getFuncionarioVO().getTipoIdentificacaoChavePixEnum());
	}

	public List<SelectItem> getListaSelectItemDeficiencia() throws Exception {
		return UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoDeficiencia.class);
	}

	public boolean isNumeroBancoItau() {
		return Uteis.isAtributoPreenchido(getFuncionarioVO().getNumeroBancoRecebimento())
				&& getFuncionarioVO().getNumeroBancoRecebimento().equals("341");
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
	
	public void selecionarPessoaEmailInstitucional() {
		try {
			PessoaEmailInstitucionalVO obj = (PessoaEmailInstitucionalVO) context().getExternalContext().getRequestMap().get("pessoaEmailInstitucionalItem");
			setPessoaEmailInstitucionalVO(obj);
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
}
