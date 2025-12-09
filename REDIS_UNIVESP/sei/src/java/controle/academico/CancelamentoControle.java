package controle.academico;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas cancelamentoForm.jsp cancelamentoCons.jsp) com as funcionalidades da classe <code>Cancelamento</code>.
 * Implemtação da camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see Cancelamento
 * @see CancelamentoVO
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SelectItemOrdemValor;
import negocio.comuns.academico.CancelamentoVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.ImpressaoContratoVO;
import negocio.comuns.academico.ImpressaoDeclaracaoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.MotivoCancelamentoTrancamentoVO;
import negocio.comuns.academico.TextoPadraoDeclaracaoVO;
import negocio.comuns.academico.enumeradores.TipoDoTextoImpressaoContratoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.contabil.LancamentoContabilCentroNegocioVO;
import negocio.comuns.contabil.LancamentoContabilVO;
import negocio.comuns.financeiro.ContaPagarVO;
import negocio.comuns.protocolo.RequerimentoVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.SituacaoCancelamentoTrancamentoEnum;
import negocio.comuns.utilitarias.dominios.TipoJustificativaCancelamento;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.controle.academico.DeclaracaoCancelamentoRelControle;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.academico.DeclaracaoCancelamentoRel;

@Controller("CancelamentoControle")
@Scope("viewScope")
@Lazy
public class CancelamentoControle extends SuperControleRelatorio {

	private static final long serialVersionUID = 1L;
	private CancelamentoVO cancelamentoVO;
	private List<MatriculaVO> listaConsultaAluno;
	private String campoConsultaAluno;
	private String valorConsultaAluno;
	private List<SelectItem> listaSelectItemMotivoCancelamentoTrancamento;
	private List<SelectItem> listaSelectItemTipoTextoPadrao;
	private Integer textoPadraoDeclaracao;
	private Boolean imprimirContrato;
	private String abrirModalPanelHistorico;
	private Boolean realizarMarcacaoDesmarcacaoTodos;
	private List<ImpressaoDeclaracaoVO> listaImpressaoDeclaracaoVOs;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private String valorConsultaJustificativa;
	private String valorConsultaSituacao;
	

	/**
	 * Interface <code>CancelamentoInterfaceFacade</code> responsável pela
	 * interconexão da camada de controle com a camada de negócio. Criando uma
	 * independência da camada de controle com relação a tenologia de
	 * persistência dos dados (DesignPatter: Façade).
	 */
	public CancelamentoControle() throws Exception {
		// obterUsuarioLogado();
		setControleConsulta(new ControleConsulta());
		getControleConsultaOtimizado().setLimitePorPagina(10);
		getControleConsultaOtimizado().setDataIni(null);
		getControleConsultaOtimizado().getUnidadeEnsinoVO().setCodigo(getUnidadeEnsinoLogado().getCodigo());
		setMensagemID("msg_entre_prmconsulta");
	}
	
	@PostConstruct
	public String editarPorNavegacaoOutraTela()  {
		String retorno = "";
		try {
			retorno = editarPorNavegacaoMapaRegistroEvasaoCurso(); 
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		 return retorno;
		
	}
	
	public String editarPorNavegacaoMapaRegistroEvasaoCurso() throws Exception {
		try {
			CancelamentoVO obj = (CancelamentoVO) context().getExternalContext().getSessionMap().get("cancelamento");
			if (obj != null && !obj.getCodigo().equals(0)) {
				editarCancelamentoVO(obj);
				return Uteis.getCaminhoRedirecionamentoNavegacao("cancelamentoForm.xhtml");
			}	
		} finally {
			context().getExternalContext().getSessionMap().remove("cancelamento");
		}
		return "";
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe
	 * <code>Cancelamento</code> para edição pelo usuário da aplicação.
	 */
	public String novo() {
		removerObjetoMemoria(this);
		setCancelamentoVO(new CancelamentoVO());
		inicializarUsuarioResponsavelTransferenciaSaidaUsuarioLogado();
		setTipoRequerimento("CA");
		setMensagemID("msg_entre_dados");
		montarListaSelectItemMotivoCancelamentoTrancamento();
		return Uteis.getCaminhoRedirecionamentoNavegacao("cancelamentoForm.xhtml");
	}

	public void consultarRequerimento() {
		try {
			List<RequerimentoVO> objs = new ArrayList<RequerimentoVO>(0);
			if (getCampoConsultaRequerimento().equals("codigo")) {
				int valorInt = Uteis.getValorInteiro(getValorConsultaRequerimento());
				objs = getFacadeFactory().getRequerimentoFacade().consultarPorCodigo(new Integer(valorInt), getTipoRequerimento(), this.getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema());
			}
			if (getCampoConsultaRequerimento().equals("data")) {
				Date valorData = Uteis.getDate(getValorConsultaRequerimento());
				objs = getFacadeFactory().getRequerimentoFacade().consultarPorData(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), getTipoRequerimento(), this.getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema());
			}
			if (getCampoConsultaRequerimento().equals("nomeTipoRequerimento")) {
				objs = getFacadeFactory().getRequerimentoFacade().consultarPorNomeTipoRequerimento(getValorConsultaRequerimento(), this.getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema());
			}
			if (getCampoConsultaRequerimento().equals("situacao")) {
				objs = getFacadeFactory().getRequerimentoFacade().consultarPorSituacao(getValorConsultaRequerimento(), getTipoRequerimento(), this.getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema());
			}
			if (getCampoConsultaRequerimento().equals("situacaoFinanceira")) {
				objs = getFacadeFactory().getRequerimentoFacade().consultarPorSituacaoFinanceira(getValorConsultaRequerimento(), getTipoRequerimento(), this.getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema());
			}
			if (getCampoConsultaRequerimento().equals("nomePessoa")) {
				objs = getFacadeFactory().getRequerimentoFacade().consultarPorNomePessoa(getValorConsultaRequerimento(), getTipoRequerimento(), this.getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema());
			}
			if (getCampoConsultaRequerimento().equals("cpfPessoa")) {
				objs = getFacadeFactory().getRequerimentoFacade().consultarPorNomeCPFPessoa(getValorConsultaRequerimento(), getTipoRequerimento(), this.getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema());
			}
			if (getCampoConsultaRequerimento().equals("matriculaMatricula")) {
				objs = getFacadeFactory().getRequerimentoFacade().consultarPorMatriculaMatricula(getValorConsultaRequerimento(), getTipoRequerimento(), this.getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema());
			}
			setListaConsultaRequerimento(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaRequerimento(new ArrayList<RequerimentoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void inicializarUsuarioResponsavelTransferenciaSaidaUsuarioLogado() {
		try {
			getCancelamentoVO().setResponsavelAutorizacao(getUsuarioLogadoClone());
		} catch (Exception e) {
		}
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe
	 * <code>Cancelamento</code> para alteração. O objeto desta classe é
	 * disponibilizado na session da página (request) para que o JSP
	 * correspondente possa disponibilizá-lo para edição.
	 */
	public String editar() {
		try {
			CancelamentoVO obj = (CancelamentoVO) context().getExternalContext().getRequestMap().get("cancelamentoItens");
			editarCancelamentoVO(obj);
			setMensagemID("msg_dados_editar");
			return Uteis.getCaminhoRedirecionamentoNavegacao("cancelamentoForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("cancelamentoCons.xhtml");
		}
	}
	
	public void editarCancelamentoVO(CancelamentoVO obj) throws Exception {
		setCancelamentoVO(getFacadeFactory().getCancelamentoFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
		setTipoRequerimento(obj.getCodigoRequerimento().getTipoRequerimento().getTipo());
		montarListaSelectItemMotivoCancelamentoTrancamento();
		consultarListaSelectItemTipoTextoPadrao(obj.getMatricula().getUnidadeEnsino().getCodigo());
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto
	 * da classe <code>Cancelamento</code>. Caso o objeto seja novo (ainda não
	 * gravado no BD) é acionado a operação <code>incluir()</code>. Caso
	 * contrário é acionado o <code>alterar()</code>. Se houver alguma
	 * inconsistência o objeto não é gravado, sendo re-apresentado para o
	 * usuário juntamente com uma mensagem de erro.
	 */
	public void persistir() {
		try {
			getFacadeFactory().getCancelamentoFacade().persistir(getCancelamentoVO(), getConfiguracaoGeralPadraoSistema(), getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			consultarListaSelectItemTipoTextoPadrao(getCancelamentoVO().getMatricula().getUnidadeEnsino().getCodigo());
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP
	 * CancelamentoCons.jsp. Define o tipo de consulta a ser executada, por meio
	 * de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
	 * resultado, disponibiliza um List com os objetos selecionados na sessao da
	 * pagina.
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public String consultar() {
		try {
			super.consultar();
			getFacadeFactory().getCancelamentoFacade().consultaOtimizada(getControleConsultaOtimizado(), getValorConsultaJustificativa(), getValorConsultaSituacao(), getControleConsultaOtimizado().getUnidadeEnsinoVO().getCodigo(), true, getUsuarioLogado());
//			List<CancelamentoVO> objs = new ArrayList<CancelamentoVO>(0);
//			if (getControleConsulta().getCampoConsulta().equals("codigo")) {
//				if (getControleConsulta().getValorConsulta().equals("")) {
//					getControleConsulta().setValorConsulta("0");
//				}
//				if (getControleConsulta().getValorConsulta().trim() != null || !getControleConsulta().getValorConsulta().trim().isEmpty()) {
//					Uteis.validarSomenteNumeroString(getControleConsulta().getValorConsulta().trim());
//				}
//				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
//				objs = getFacadeFactory().getCancelamentoFacade().consultaRapidaPorCodigo(new Integer(valorInt), getUnidadeEnsinoLogado().getCodigo(), true, getUsuarioLogado());
//			}
//			if (getControleConsulta().getCampoConsulta().equals("data")) {
//				Date valorData = Uteis.getDate(getControleConsulta().getValorConsulta());
//				objs = getFacadeFactory().getCancelamentoFacade().consultarPorData(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
//			}
//			if (getControleConsulta().getCampoConsulta().equals("descricao")) {
//				if (getControleConsulta().getValorConsulta().length() < 2) {
//					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
//				}
//				objs = getFacadeFactory().getCancelamentoFacade().consultaRapidaPorDescricao(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), true, getUsuarioLogado());
//			}
//			if (getControleConsulta().getCampoConsulta().equals("situacao")) {
//				objs = getFacadeFactory().getCancelamentoFacade().consultaRapidaPorSituacao(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), true, getUsuarioLogado());
//			}
//			if (getControleConsulta().getCampoConsulta().equals("nomeAluno")) {
//				if (getControleConsulta().getValorConsulta().length() < 2) {
//					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
//				}
//				objs = getFacadeFactory().getCancelamentoFacade().consultaRapidaPorNomeAlunoUltimoCancelamento(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), true, getUsuarioLogado());
//			}
//			if (getControleConsulta().getCampoConsulta().equals("registroAcademico")) {
//				if (getControleConsulta().getValorConsulta().length() < 2) {
//					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
//				}
//				objs = getFacadeFactory().getCancelamentoFacade().consultaRapidaPorRegistroAcademicoUltimoCancelamento(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), true, getUsuarioLogado());
//			}
//			if (getControleConsulta().getCampoConsulta().equals("matriculaMatricula")) {
//				if (getControleConsulta().getValorConsulta().length() < 2) {
//					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
//				}
//				objs = getFacadeFactory().getCancelamentoFacade().consultaRapidaPorMatriculaUltimoCancelamento(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), true, getUsuarioLogado());
//			}
//			if (getControleConsulta().getCampoConsulta().equals("codigoRequerimento")) {
//				if (getControleConsulta().getValorConsulta().equals("")) {
//					getControleConsulta().setValorConsulta("0");
//				}
//				if (getControleConsulta().getValorConsulta().trim() != null || !getControleConsulta().getValorConsulta().trim().isEmpty()) {
//					Uteis.validarSomenteNumeroString(getControleConsulta().getValorConsulta().trim());
//				}
//				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
//				objs = getFacadeFactory().getCancelamentoFacade().consultaRapidaPorCodigoRequerimento(new Integer(valorInt), getUnidadeEnsinoLogado().getCodigo(), true, getUsuarioLogado());
//			}
//			if (getControleConsulta().getCampoConsulta().equals("tipoJustificativa")) {
//				objs = getFacadeFactory().getCancelamentoFacade().consultaRapidaPorTipoJustificativaUltimoCancelamento(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), true, getUsuarioLogado());
//			}
//			if (getControleConsulta().getCampoConsulta().equals("nomePessoa")) {
//				if (getControleConsulta().getValorConsulta().length() < 2) {
//					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
//				}
//				objs = getFacadeFactory().getCancelamentoFacade().consultaRapidaPorNomeAluno(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), true, getUsuarioLogado());
//			}
//			if (getControleConsulta().getCampoConsulta().equals("turma")) {
//				if (getControleConsulta().getValorConsulta().length() < 2) {
//					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
//				}
//				objs = getFacadeFactory().getCancelamentoFacade().consultaRapidaPorTurma(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), true, getUsuarioLogado());
//			}
//			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("cancelamentoCons.xhtml");
		} catch (Exception e) {
//			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("cancelamentoCons.xhtml");
		}
	}
	
	public void iniciarConsulta() {
		getControleConsultaOtimizado().setLimitePorPagina(10);
		getControleConsultaOtimizado().setPage(1);
		getControleConsultaOtimizado().setPaginaAtual(1);
		consultar();
	}
	public void scrollListener(DataScrollEvent event) {
		getControleConsultaOtimizado().setPage(event.getPage());
		getControleConsultaOtimizado().setPaginaAtual(event.getPage());
		consultar();
	}

	public void consultarAluno() {
		try {
			List<MatriculaVO> objs = new ArrayList<MatriculaVO>(0);
			if (getValorConsultaAluno().equals("")) {
				throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
			}
			if (getCampoConsultaAluno().equals("matricula")) {
				objs = getFacadeFactory().getMatriculaFacade().consultarPorMatricula(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());				
			}
			if (getCampoConsultaAluno().equals("registroAcademico")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorRegistroAcademico(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, "","", getUsuarioLogado());				
			}
			if (getCampoConsultaAluno().equals("nomePessoa")) {
				if (getValorConsultaAluno().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getMatriculaFacade().consultarPorNomePessoa(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
				if (objs.isEmpty()) {
					throw new Exception("Aluno não encontrado, talvez sua Matrícula Período não esteja ativa.");
				}
			}
			if (getCampoConsultaAluno().equals("nomeCurso")) {
				if (getValorConsultaAluno().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getMatriculaFacade().consultarPorNomeCurso(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
				if (objs.isEmpty()) {
					throw new Exception("Aluno não encontrado, talvez sua Matrícula Período não esteja ativa.");
				}
			}
			setListaConsultaAluno(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList<MatriculaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarAluno() throws Exception {
		try {
			MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
			if(obj.getBloqueioPorSolicitacaoLiberacaoMatricula()) {
				throw new Exception(getMensagemInternalizacao("msg_RenovarMatricula_avisoMatriculaSuspensa"));
			}
			getCancelamentoVO().setMatricula(getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula(), obj.getUnidadeEnsino().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
			getFacadeFactory().getCancelamentoFacade().executarValidacaoExistePendenciaFinanceiraEPreMatriculaAtiva(getCancelamentoVO().getMatricula(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			setValorConsultaAluno("");
			setCampoConsultaAluno("");
			getListaConsultaAluno().clear();
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setCancelamentoVO(null);
			inicializarUsuarioResponsavelTransferenciaSaidaUsuarioLogado();
		}

	}

	public void limparDadosAluno() throws Exception {
		getCancelamentoVO().setMatricula(new MatriculaVO());
	}

	public void irPaginaInicial() throws Exception {
		controleConsulta.setPaginaAtual(1);
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

	public List<SelectItem> listaSelectItemSituacaoFinalRequerimento;
	public List<SelectItem> getListaSelectItemSituacaoFinalRequerimento() throws Exception {
		if(listaSelectItemSituacaoFinalRequerimento == null) {
			listaSelectItemSituacaoFinalRequerimento = new ArrayList<SelectItem>(0);
			listaSelectItemSituacaoFinalRequerimento.add(new SelectItem("", ""));
			listaSelectItemSituacaoFinalRequerimento.add(new SelectItem(SituacaoCancelamentoTrancamentoEnum.FINALIZADO_DEFERIDO.getValor(), SituacaoCancelamentoTrancamentoEnum.FINALIZADO_DEFERIDO.getDescricao()));
			listaSelectItemSituacaoFinalRequerimento.add(new SelectItem(SituacaoCancelamentoTrancamentoEnum.ESTORNADO.getValor(), SituacaoCancelamentoTrancamentoEnum.ESTORNADO.getDescricao()));
			
		}
		return listaSelectItemSituacaoFinalRequerimento;
	}

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do
	 * ComboBox correspondente ao atributo <code>tipoJustificativa</code>
	 */
	public List<SelectItem> listaSelectItemTipoJustificativaCancelamento;
	public List<SelectItem> getListaSelectItemTipoJustificativaCancelamento() throws Exception {
		if(listaSelectItemTipoJustificativaCancelamento == null) {
			listaSelectItemTipoJustificativaCancelamento = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoJustificativaCancelamento.class, "valor", "descricao", true);
		}
		return listaSelectItemTipoJustificativaCancelamento;
	}

	public void montarListaSelectItemMotivoCancelamentoTrancamento() {
		try {
			montarListaSelectItemMotivoCancelamentoTrancamento("");
		} catch (Exception e) {
		}
	}

	public void montarListaSelectItemMotivoCancelamentoTrancamento(String prm) throws Exception {
		try {
			List<MotivoCancelamentoTrancamentoVO> resultadoConsulta = consultarMotivoCancelamentoTrancamentoPorNomeAtivo(prm);
			setListaSelectItemMotivoCancelamentoTrancamento(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
		} catch (Exception e) {
			throw e;
		}
	}

	public List<MotivoCancelamentoTrancamentoVO> consultarMotivoCancelamentoTrancamentoPorNomeAtivo(String nomePrm) throws Exception {
		return getFacadeFactory().getMotivoCancelamentoTrancamentoFacade().consultarPorNomeAtivo(nomePrm, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
	}

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do
	 * ComboBox correspondente ao atributo <code>situacao</code>
	 */
	
	public List<SelectItem> listaSelectItemSituacaoCancelamento;
	
	public List<SelectItem> getListaSelectItemSituacaoCancelamento() throws Exception {
		if(listaSelectItemSituacaoCancelamento == null) {
		listaSelectItemSituacaoCancelamento = new ArrayList<SelectItem>(0);
		listaSelectItemSituacaoCancelamento.add(new SelectItem("", ""));
		Hashtable situacaoAcademicoCancelamentos = (Hashtable) Dominios.getSituacaoProtocolo();
		Enumeration keys = situacaoAcademicoCancelamentos.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) situacaoAcademicoCancelamentos.get(value);
			listaSelectItemSituacaoCancelamento.add(new SelectItem(value, label));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) listaSelectItemSituacaoCancelamento, ordenador);
		}
		return listaSelectItemSituacaoCancelamento;
	}

	/**
	 * Método responsável por processar a consulta na entidade
	 * <code>Requerimento</code> por meio de sua respectiva chave primária. Esta
	 * rotina é utilizada fundamentalmente por requisições Ajax, que realizam
	 * busca pela chave primária da entidade montando automaticamente o
	 * resultado da consulta para apresentação.
	 */
	public void consultarRequerimentoPorChavePrimaria() {
		try {
			getCancelamentoVO().setCodigoRequerimento(getFacadeFactory().getRequerimentoFacade().consultarPorChavePrimariaFiltrandoPorUnidadeEnsino(getCancelamentoVO().getCodigoRequerimento().getCodigo(), "CA", super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema()));
			getCancelamentoVO().setMatricula(getCancelamentoVO().getCodigoRequerimento().getMatricula());
			getFacadeFactory().getCancelamentoFacade().executarValidacaoExistePendenciaFinanceiraEPreMatriculaAtiva(getCancelamentoVO().getMatricula(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setCancelamentoVO(null);
			inicializarUsuarioResponsavelTransferenciaSaidaUsuarioLogado();
		}
	}

	public void selecionarRequerimento() {
		try {
			RequerimentoVO obj = (RequerimentoVO) context().getExternalContext().getRequestMap().get("requerimentoItens");
			getCancelamentoVO().setCodigoRequerimento(getFacadeFactory().getRequerimentoFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema()));
			getCancelamentoVO().setMatricula(getCancelamentoVO().getCodigoRequerimento().getMatricula());
			getFacadeFactory().getCancelamentoFacade().executarValidacaoExistePendenciaFinanceiraEPreMatriculaAtiva(getCancelamentoVO().getMatricula(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setCancelamentoVO(null);
			inicializarUsuarioResponsavelTransferenciaSaidaUsuarioLogado();
		}
	}

	/**
	 * Método responsável por processar a consulta na entidade
	 * <code>Matricula</code> por meio de sua respectiva chave primária. Esta
	 * rotina é utilizada fundamentalmente por requisições Ajax, que realizam
	 * busca pela chave primária da entidade montando automaticamente o
	 * resultado da consulta para apresentação.
	 */
	public void consultarMatriculaPorChavePrimaria() {
		try {
			getCancelamentoVO().setMatricula(getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(getCancelamentoVO().getMatricula().getMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), NivelMontarDados.getEnum(Uteis.NIVELMONTARDADOS_TODOS), getUsuarioLogado()));
			if(getCancelamentoVO().getMatricula().getBloqueioPorSolicitacaoLiberacaoMatricula()) {
				throw new Exception(getMensagemInternalizacao("msg_RenovarMatricula_avisoMatriculaSuspensa"));
			}
			getFacadeFactory().getCancelamentoFacade().executarValidacaoExistePendenciaFinanceiraEPreMatriculaAtiva(getCancelamentoVO().getMatricula(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setCancelamentoVO(null);
			inicializarUsuarioResponsavelTransferenciaSaidaUsuarioLogado();
		}
	}

	public String getMascaraConsulta() {
		if (getControleConsulta().getCampoConsulta().equals("data")) {
			return "return mascara(this.form,'formCadastro:valorConsulta','99/99/9999',event);";
		}
		return "";
	}

	public Boolean getConsultarPorTipoJustificativa() {
		return getControleConsulta().getCampoConsulta().equals("tipoJustificativa");
	}

	public Boolean getConsultarPorSituacao() {
		return getControleConsulta().getCampoConsulta().equals("situacao");
	}

	public List<SelectItem> tipoConsultaComboAluno;
	public List<SelectItem> getTipoConsultaComboAluno() {
		if(tipoConsultaComboAluno == null) {
			tipoConsultaComboAluno = new ArrayList<SelectItem>(0);
			tipoConsultaComboAluno.add(new SelectItem("nomePessoa", "Aluno"));
			tipoConsultaComboAluno.add(new SelectItem("matricula", "Matrícula"));
			tipoConsultaComboAluno.add(new SelectItem("registroAcademico", "Registro Acadêmico"));
			tipoConsultaComboAluno.add(new SelectItem("nomeCurso", "Curso"));
		}
		return tipoConsultaComboAluno;
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List<SelectItem> tipoConsultaCombo;
	public List<SelectItem> getTipoConsultaCombo() {
		if(tipoConsultaCombo == null) {
			tipoConsultaCombo = new ArrayList<SelectItem>(0);
			tipoConsultaCombo.add(new SelectItem("nomeAluno", "Nome Aluno"));
			tipoConsultaCombo.add(new SelectItem("matriculaMatricula", "Matrícula"));
			tipoConsultaCombo.add(new SelectItem("registroAcademico", "Registro Acadêmico"));
			tipoConsultaCombo.add(new SelectItem("descricao", "Descrição"));
//		itens.add(new SelectItem("data", "Data"));
//		itens.add(new SelectItem("situacao", "Situação"));
			tipoConsultaCombo.add(new SelectItem("codigoRequerimento", "Código Requerimento"));
//		itens.add(new SelectItem("tipoJustificativa", "Tipo Justificativa"));
		//itens.add(new SelectItem("nomePessoa", "Responsável Autorizacão"));
//		itens.add(new SelectItem("codigo", "Código"));
		}
		return tipoConsultaCombo;
	}

	public void imprimirPDF() {
		try {
			List<CancelamentoVO> listaObjetos = new ArrayList<CancelamentoVO>();
			if (getCancelamentoVO().getMatricula().getCurso().getNivelEducacional().equals("BA") || getCancelamentoVO().getMatricula().getCurso().getNivelEducacional().equals("ME") || getCancelamentoVO().getMatricula().getCurso().getNivelEducacional().equals("EX") || getCancelamentoVO().getMatricula().getCurso().getNivelEducacional().equals("PR")) {
				getCancelamentoVO().setTitulacaoInstituicao("Instituto de Ensino");
			} else {
				getCancelamentoVO().setTitulacaoInstituicao("Instituto de Ensino Superior");
			}
			listaObjetos.add(getCancelamentoVO());
			getSuperParametroRelVO().limparParametros();
			getSuperParametroRelVO().setNomeDesignIreport(DeclaracaoCancelamentoRel.getDesignIReportRelatorio());
			getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
			getSuperParametroRelVO().setSubReport_Dir(DeclaracaoCancelamentoRel.getCaminhoBaseRelatorio());
			getSuperParametroRelVO().setTituloRelatorio("DECLARAÇÃO DE CANCELAMENTO");
			getSuperParametroRelVO().setListaObjetos(listaObjetos);
			getSuperParametroRelVO().setUnidadeEnsino(super.getUnidadeEnsinoLogado().getNome());
			getSuperParametroRelVO().setCaminhoBaseRelatorio(DeclaracaoCancelamentoRel.getCaminhoBaseRelatorio());
			realizarImpressaoRelatorio();
			setMensagemID("msg_relatorio_ok");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		executarMetodoControle(DeclaracaoCancelamentoRelControle.class.getSimpleName(), "imprimirPDF", getCancelamentoVO());
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes
	 * de uma consulta.
	 */
	@SuppressWarnings("rawtypes")
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setListaConsulta(new ArrayList(0));
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("cancelamentoCons.xhtml");
	}

	public CancelamentoVO getCancelamentoVO() {
		if (cancelamentoVO == null) {
			cancelamentoVO = new CancelamentoVO();
		}
		return cancelamentoVO;
	}

	public void setCancelamentoVO(CancelamentoVO cancelamentoVO) {
		this.cancelamentoVO = cancelamentoVO;
	}

	@Override
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
		cancelamentoVO = null;
	}

	/**
	 * @return the listaConsultaAluno
	 */
	public List<MatriculaVO> getListaConsultaAluno() {
		if (listaConsultaAluno == null) {
			listaConsultaAluno = new ArrayList<MatriculaVO>(0);
		}
		return listaConsultaAluno;
	}

	/**
	 * @param listaConsultaAluno
	 *            the listaConsultaAluno to set
	 */
	public void setListaConsultaAluno(List<MatriculaVO> listaConsultaAluno) {
		this.listaConsultaAluno = listaConsultaAluno;
	}

	/**
	 * @return the campoConsultaAluno
	 */
	public String getCampoConsultaAluno() {
		if (campoConsultaAluno == null) {
			campoConsultaAluno = "";
		}
		return campoConsultaAluno;
	}

	/**
	 * @param campoConsultaAluno
	 *            the campoConsultaAluno to set
	 */
	public void setCampoConsultaAluno(String campoConsultaAluno) {
		this.campoConsultaAluno = campoConsultaAluno;
	}

	/**
	 * @return the valorConsultaAluno
	 */
	public String getValorConsultaAluno() {
		if (valorConsultaAluno == null) {
			valorConsultaAluno = "";
		}
		return valorConsultaAluno;
	}

	/**
	 * @param valorConsultaAluno
	 *            the valorConsultaAluno to set
	 */
	public void setValorConsultaAluno(String valorConsultaAluno) {
		this.valorConsultaAluno = valorConsultaAluno;
	}

	/**
	 * @return the listaSelectItemMotivoCancelamentoTrancamento
	 */
	public List<SelectItem> getListaSelectItemMotivoCancelamentoTrancamento() {
		if (listaSelectItemMotivoCancelamentoTrancamento == null) {
			listaSelectItemMotivoCancelamentoTrancamento = new ArrayList<SelectItem>();
		}
		return listaSelectItemMotivoCancelamentoTrancamento;
	}

	/**
	 * @param listaSelectItemMotivoCancelamentoTrancamento
	 *            the listaSelectItemMotivoCancelamentoTrancamento to set
	 */
	public void setListaSelectItemMotivoCancelamentoTrancamento(List<SelectItem> listaSelectItemMotivoCancelamentoTrancamento) {
		this.listaSelectItemMotivoCancelamentoTrancamento = listaSelectItemMotivoCancelamentoTrancamento;
	}

	public void consultarListaSelectItemTipoTextoPadrao(Integer unidadeEnsino) throws Exception {
		List<TextoPadraoDeclaracaoVO> textoPadraoDeclaracaoVOs = getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorTipo("CA", unidadeEnsino, "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		setListaSelectItemTipoTextoPadrao(UtilSelectItem.getListaSelectItem(textoPadraoDeclaracaoVOs, "codigo", "descricao"));
		setMensagemID("msg_dados_consultados");
	}

	public List<SelectItem> getListaSelectItemTipoTextoPadrao() {
		if (listaSelectItemTipoTextoPadrao == null) {
			listaSelectItemTipoTextoPadrao = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemTipoTextoPadrao;
	}

	public void setListaSelectItemTipoTextoPadrao(List<SelectItem> listaSelectItemTipoTextoPadrao) {
		this.listaSelectItemTipoTextoPadrao = listaSelectItemTipoTextoPadrao;
	}

	public Integer getTextoPadraoDeclaracao() {
		if (textoPadraoDeclaracao == null) {
			textoPadraoDeclaracao = 0;
		}
		return textoPadraoDeclaracao;
	}

	public void setTextoPadraoDeclaracao(Integer textoPadraoDeclaracao) {
		this.textoPadraoDeclaracao = textoPadraoDeclaracao;
	}
	
	public void iniciarlizarDadosParaImpressao(){
		try {
			getListaImpressaoDeclaracaoVOs().clear();
			setMensagemID("msg_entre_prmconsulta");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void visualizarImpressaoDeclaracaoAluno() {
		try {
			getFacadeFactory().getCancelamentoFacade().validarDadosAntesImpressao(getCancelamentoVO(), getTextoPadraoDeclaracao());
			setListaImpressaoDeclaracaoVOs(getFacadeFactory().getImpressaoDeclaracaoFacade().consultarPorMatriculaPorTextoPadrao(getCancelamentoVO().getMatricula().getMatricula(), getTextoPadraoDeclaracao(), TipoDoTextoImpressaoContratoEnum.TEXTO_PADRAO_DECLARACAO, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void impressaoDeclaracaoContratoJaGerada() {
		ImpressaoDeclaracaoVO obj = (ImpressaoDeclaracaoVO) context().getExternalContext().getRequestMap().get("impressaoDeclaracaoItens");
		try {
			limparMensagem();
			this.setCaminhoRelatorio("");
			ImpressaoContratoVO impressaoContrato = new ImpressaoContratoVO();
			impressaoContrato.setTipoTextoEnum(TipoDoTextoImpressaoContratoEnum.TEXTO_PADRAO_DECLARACAO);
			impressaoContrato.setGerarNovoArquivoAssinado(false);
			impressaoContrato.setMatriculaVO(obj.getMatricula());
			setCaminhoRelatorio(getFacadeFactory().getImpressaoDeclaracaoFacade().executarValidacaoImpressaoEmPdf(impressaoContrato, obj.getTextoPadraoDeclaracao(), "", true, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado()));
			setImprimirContrato(false);
			setFazerDownload(true);
			setMensagemID("msg_relatorio_ok");
		} catch (Exception e) {
			setImprimirContrato(false);
			setFazerDownload(false);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	

	public void imprimirPDF2() throws Exception {
		try {
			getFacadeFactory().getCancelamentoFacade().validarDadosAntesImpressao(getCancelamentoVO(), getTextoPadraoDeclaracao());
			TextoPadraoDeclaracaoVO textoPadraoDeclaracaoVO = getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorChavePrimaria(textoPadraoDeclaracao, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			setCaminhoRelatorio(getFacadeFactory().getCancelamentoFacade().imprimirDeclaracaoCancelamento(getCancelamentoVO(), textoPadraoDeclaracaoVO, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado()));
			if (getCaminhoRelatorio().isEmpty()) {
				setImprimirContrato(true);
				setFazerDownload(false);
			} else {
				setImprimirContrato(false);
				setFazerDownload(true);
			}
			setMensagemID("msg_relatorio_ok");
		} catch (Exception e) {
			setImprimirContrato(false);
			setFazerDownload(false);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public String getContrato() {
		if (getImprimirContrato()) {
			return "abrirPopup('../../VisualizarContrato', 'RelatorioContrato', 730, 545); RichFaces.$('panelTextoPadraoDeclaracao').hide()";
		} else if (getFazerDownload()) {
			return getDownload();
		}
		return "";
	}

	public Boolean getImprimirContrato() {
		if (imprimirContrato == null) {
			imprimirContrato = Boolean.FALSE;
		}
		return imprimirContrato;
	}

	public void setImprimirContrato(Boolean imprimirContrato) {
		this.imprimirContrato = imprimirContrato;
	}
	
	public List<ImpressaoDeclaracaoVO> getListaImpressaoDeclaracaoVOs() {
		if (listaImpressaoDeclaracaoVOs == null) {
			listaImpressaoDeclaracaoVOs = new ArrayList<ImpressaoDeclaracaoVO>(0);
		}
		return listaImpressaoDeclaracaoVOs;
	}

	public void setListaImpressaoDeclaracaoVOs(List<ImpressaoDeclaracaoVO> listaImpressaoDeclaracaoVOs) {
		this.listaImpressaoDeclaracaoVOs = listaImpressaoDeclaracaoVOs;
	}

	/**
	 * Responsável por executar a montagem dos históricos para realizar
	 * alteração da situação de acordo com a ultima matrícula período cuja
	 * situação seja AT ou PR.
	 * 
	 * @author Wellington Rodrigues - 01/04/2015
	 * @param matriculaVO
	 * @param configuracaoFinanceiroVO
	 * @param usuarioVO
	 * @return
	 * @throws Exception
	 */
	public void executarMontagemHistoricosParaRealizarAlteracaoSituacao() {
		try {
			getCancelamentoVO().setHistoricoVOs(getFacadeFactory().getTrancamentoFacade().executarMontagemHistoricosParaRealizarAlteracaoSituacao(getCancelamentoVO().getMatricula(), null, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
			if (Uteis.isAtributoPreenchido(getCancelamentoVO().getHistoricoVOs())) {
				setAbrirModalPanelHistorico("RichFaces.$('panelHistorico').show()");
			} else {
				getFacadeFactory().getCancelamentoFacade().persistir(getCancelamentoVO(), getConfiguracaoGeralPadraoSistema(), getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
				consultarListaSelectItemTipoTextoPadrao(getCancelamentoVO().getMatricula().getUnidadeEnsino().getCodigo());
				setMensagemID("msg_dados_gravados");
			}
		} catch (Exception e) {
			setAbrirModalPanelHistorico("");
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * @return the abrirModalPanelHistorico
	 */
	public String getAbrirModalPanelHistorico() {
		if (abrirModalPanelHistorico == null) {
			abrirModalPanelHistorico = "";
		}
		return abrirModalPanelHistorico;
	}

	/**
	 * @param abrirModalPanelHistorico
	 *            the abrirModalPanelHistorico to set
	 */
	public void setAbrirModalPanelHistorico(String abrirModalPanelHistorico) {
		this.abrirModalPanelHistorico = abrirModalPanelHistorico;
	}

	/**
	 * @return the realizarMarcacaoDesmarcacaoTodos
	 */
	public Boolean getRealizarMarcacaoDesmarcacaoTodos() {
		if (realizarMarcacaoDesmarcacaoTodos == null) {
			realizarMarcacaoDesmarcacaoTodos = false;
		}
		return realizarMarcacaoDesmarcacaoTodos;
	}

	/**
	 * @param realizarMarcacaoDesmarcacaoTodos
	 *            the realizarMarcacaoDesmarcacaoTodos to set
	 */
	public void setRealizarMarcacaoDesmarcacaoTodos(Boolean realizarMarcacaoDesmarcacaoTodos) {
		this.realizarMarcacaoDesmarcacaoTodos = realizarMarcacaoDesmarcacaoTodos;
	}

	/**
	 * Responsável por executar a marcação e a desmarcação dos históricos que
	 * serão Cancelados.
	 * 
	 * @author Wellington Rodrigues - 02/04/2015
	 */
	public void executarMarcacaoDesmarcacaoTodos() {
		for (HistoricoVO obj : getCancelamentoVO().getHistoricoVOs()) {
			if (obj.getEditavel()) {
				obj.setRealizarAlteracaoSituacaoHistorico(getRealizarMarcacaoDesmarcacaoTodos());
			}
		}
	}
	
	public void executarEstorno() {
		UsuarioVO responsavelAntes = getCancelamentoVO().getResponsavelEstorno();
		Date dataAntes = getCancelamentoVO().getDataEstorno();
		try {
			getCancelamentoVO().setResponsavelEstorno(getUsuarioLogadoClone());
			getCancelamentoVO().setDataEstorno(new Date());
			getCancelamentoVO().setSituacao("ES");
			getFacadeFactory().getCancelamentoFacade().executarEstorno(getCancelamentoVO(), getUsuarioLogado());
			setMensagemID("msg_dados_estornados");
		} catch (Exception e) {
			getCancelamentoVO().setResponsavelEstorno(responsavelAntes);
			getCancelamentoVO().setDataEstorno(dataAntes);
			getCancelamentoVO().setSituacao("FD");
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public boolean getIsPermiteEstorno() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("Cancelamento_Estornar", getUsuarioLogado());
			boolean permitir = false;
			if (getCancelamentoVO().getMatricula().getSituacao().equals("CA")) {
				permitir = true;
			} else if (!getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatriculaUnica(getCancelamentoVO().getMatricula().getMatricula(), 0, false, "CA", getUsuarioLogado()).getMatricula().equals("")) {
				permitir = true;
			}
			return (permitir && getCancelamentoVO().getSituacao().equals("FD"));
		} catch (Exception e) {
			return false;
		}
	}

	public String getValorConsultaJustificativa() {
		if(valorConsultaJustificativa == null) {
			valorConsultaJustificativa =  "";
		}
		return valorConsultaJustificativa;
	}

	public void setValorConsultaJustificativa(String valorConsultaJustificativa) {
		this.valorConsultaJustificativa = valorConsultaJustificativa;
	}

	public String getValorConsultaSituacao() {
		if(valorConsultaSituacao == null) {
			valorConsultaSituacao =  "";
		}
		return valorConsultaSituacao;
	}

	public void setValorConsultaSituacao(String valorConsultaSituacao) {
		this.valorConsultaSituacao = valorConsultaSituacao;
	}


	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if(listaSelectItemUnidadeEnsino == null) {
			try {
				listaSelectItemUnidadeEnsino =  UtilSelectItem.getListaSelectItem(getFacadeFactory().getUnidadeEnsinoFacade().consultarUnidadeEnsinoComboBox(getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado()), "codigo", "nome",!Uteis.isAtributoPreenchido(getUnidadeEnsinoLogado().getCodigo()), true);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}
	
}
