package controle.academico;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas trancamentoForm.jsp trancamentoCons.jsp) com as funcionalidades da classe <code>Trancamento</code>. Implemtação
 * da camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see Trancamento
 * @see TrancamentoVO
 */
import java.io.Serializable;
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
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.MotivoCancelamentoTrancamentoVO;
import negocio.comuns.academico.TextoPadraoDeclaracaoVO;
import negocio.comuns.academico.TrancamentoVO;
import negocio.comuns.academico.enumeradores.TipoDoTextoImpressaoContratoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.protocolo.RequerimentoVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.SituacaoCancelamentoTrancamentoEnum;
import negocio.comuns.utilitarias.dominios.SituacaoRequerimento;
import negocio.comuns.utilitarias.dominios.TipoTrancamentoEnum;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.academico.DeclaracaoTrancamentoRel;

@Controller("TrancamentoControle")
@Scope("viewScope")
@Lazy
public class TrancamentoControle extends SuperControleRelatorio implements Serializable {

	private static final long serialVersionUID = 1L;
	private TrancamentoVO trancamentoVO;
	private List<MatriculaVO> listaConsultaAluno;
	private String campoConsultaAluno;
	private String valorConsultaAluno;
	private List<SelectItem> listaSelectItemMotivoCancelamentoTrancamento;
	private Boolean imprimirContrato;
	private Integer textoPadraoDeclaracao;
	private List<SelectItem> listaSelectItemTipoTextoPadrao;
	private String abrirModalPanelHistorico;
	private Boolean realizarMarcacaoDesmarcacaoTodos;
	private List<ImpressaoDeclaracaoVO> listaImpressaoDeclaracaoVOs;
	private List<SelectItem> listaSelectItemTipoTrancamento;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private String valorConsultaSituacao;
	private String valorConsultaMotivoTrancamento;
	private String valorConsultaTipo;

	/**
	 * Interface <code>TrancamentoInterfaceFacade</code> responsável pela
	 * interconexão da camada de controle com a camada de negócio. Criando uma
	 * independência da camada de controle com relação a tenologia de
	 * persistência dos dados (DesignPatter: Façade).
	 */
	public TrancamentoControle() throws Exception {
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
			TrancamentoVO obj = (TrancamentoVO) context().getExternalContext().getSessionMap().get("trancamento");
			if (obj != null && !obj.getCodigo().equals(0)) {
				editarTrancamentoVO(obj);
				return Uteis.getCaminhoRedirecionamentoNavegacao("trancamentoForm.xhtml");
			}	
		} finally {
			context().getExternalContext().getSessionMap().remove("trancamento");
		}
		return "";
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe
	 * <code>Trancamento</code> para edição pelo usuário da aplicação.
	 */
	public String novo() {
		removerObjetoMemoria(this);
		setTrancamentoVO(new TrancamentoVO());
		inicializarUsuarioResponsavelTransferenciaSaidaUsuarioLogado();
		montarListaSelectItemMotivoCancelamentoTrancamento();
		listaSelectItemTipoTrancamento = null;
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("trancamentoForm.xhtml");
	}

	public void inicializarUsuarioResponsavelTransferenciaSaidaUsuarioLogado() {
		try {
			getTrancamentoVO().setResponsavelAutorizacao(getUsuarioLogadoClone());
		} catch (Exception e) {
		}
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe
	 * <code>Trancamento</code> para alteração. O objeto desta classe é
	 * disponibilizado na session da página (request) para que o JSP
	 * correspondente possa disponibilizá-lo para edição.
	 */
	public String editar() {
		try {
			TrancamentoVO obj = (TrancamentoVO) context().getExternalContext().getRequestMap().get("trancamentoItens"); 
			editarTrancamentoVO(obj);
			setMensagemID("msg_dados_editar");
			return Uteis.getCaminhoRedirecionamentoNavegacao("trancamentoForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("trancamentoCons.xhtml");
		}
	}
	
	public void editarTrancamentoVO(TrancamentoVO obj) throws Exception { 
		setTrancamentoVO(getFacadeFactory().getTrancamentoFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema()));
		montarListaSelectItemMotivoCancelamentoTrancamento();
		consultarListaSelectItemTipoTextoPadrao(obj.getMatricula().getUnidadeEnsino().getCodigo());
		getListaSelectItemTipoTrancamento();
		if(getTrancamentoVO().getTipoTrancamento().equals(TipoTrancamentoEnum.RENOVACAO_AUTOMATICA.getValor())) {
			getListaSelectItemTipoTrancamento().add(new SelectItem(TipoTrancamentoEnum.RENOVACAO_AUTOMATICA.getValor(), TipoTrancamentoEnum.RENOVACAO_AUTOMATICA.getDescricao()));
		}	
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto
	 * da classe <code>Trancamento</code>. Caso o objeto seja novo (ainda não
	 * gravado no BD) é acionado a operação <code>incluir()</code>. Caso
	 * contrário é acionado o <code>alterar()</code>. Se houver alguma
	 * inconsistência o objeto não é gravado, sendo re-apresentado para o
	 * usuário juntamente com uma mensagem de erro.
	 */
	public void persistir() {
		try {
			getFacadeFactory().getTrancamentoFacade().persistir(getTrancamentoVO(), getConfiguracaoGeralPadraoSistema(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getTrancamentoVO().getMatricula().getUnidadeEnsino().getCodigo()), getUsuarioLogado());
			consultarListaSelectItemTipoTextoPadrao(getTrancamentoVO().getMatricula().getUnidadeEnsino().getCodigo());
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP
	 * TrancamentoCons.jsp. Define o tipo de consulta a ser executada, por meio
	 * de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
	 * resultado, disponibiliza um List com os objetos selecionados na sessao da
	 * pagina.
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public String consultar() {
		try {
			super.consultar();
			getFacadeFactory().getTrancamentoFacade().consultaOtimizada(getControleConsultaOtimizado(), getValorConsultaMotivoTrancamento(), getValorConsultaSituacao(), getValorConsultaTipo(), getControleConsultaOtimizado().getUnidadeEnsinoVO().getCodigo(), true, getUsuarioLogado());
//			List<TrancamentoVO> objs = new ArrayList<TrancamentoVO>(0);
//			if (getControleConsulta().getCampoConsulta().equals("codigo")) {
//				if (getControleConsulta().getValorConsulta().equals("")) {
//					getControleConsulta().setValorConsulta("0");
//				}
//				if (getControleConsulta().getValorConsulta().trim() != null || !getControleConsulta().getValorConsulta().trim().isEmpty()) {
//					Uteis.validarSomenteNumeroString(getControleConsulta().getValorConsulta().trim());
//				}
//				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
//				objs = getFacadeFactory().getTrancamentoFacade().consultaRapidaPorCodigo(new Integer(valorInt), getUnidadeEnsinoLogado().getCodigo(), true, getUsuarioLogado());
//			}
//			if (getControleConsulta().getCampoConsulta().equals("data")) {
//				Date valorData = Uteis.getDate(getControleConsulta().getValorConsulta());
//				objs = getFacadeFactory().getTrancamentoFacade().consultarPorData(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
//			}
//			if (getControleConsulta().getCampoConsulta().equals("situacao")) {
//				objs = getFacadeFactory().getTrancamentoFacade().consultarPorSituacao(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
//			}
//			if (getControleConsulta().getCampoConsulta().equals("nomeAluno")) {
//				if (getControleConsulta().getValorConsulta().length() < 2) {
//					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
//				}
//				objs = getFacadeFactory().getTrancamentoFacade().consultaRapidaPorNomeAluno(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), true, getUsuarioLogado());
//			}
//			if (getControleConsulta().getCampoConsulta().equals("registroAcademico")) {
//				if (getControleConsulta().getValorConsulta().length() < 2) {
//					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
//				}
//				objs = getFacadeFactory().getTrancamentoFacade().consultaRapidaPorRegistroAcademicoAluno(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), true, getUsuarioLogado());
//			}
//			if (getControleConsulta().getCampoConsulta().equals("matriculaMatricula")) {
//				if (getControleConsulta().getValorConsulta().length() < 2) {
//					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
//				}
//				objs = getFacadeFactory().getTrancamentoFacade().consultaRapidaPorMatricula(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), true, getUsuarioLogado());
//			}
//			if (getControleConsulta().getCampoConsulta().equals("codigoRequerimento")) {
//				if (getControleConsulta().getValorConsulta().equals("")) {
//					getControleConsulta().setValorConsulta("0");
//				}
//				if (getControleConsulta().getValorConsulta().trim() != null || !getControleConsulta().getValorConsulta().trim().isEmpty()) {
//					Uteis.validarSomenteNumeroString(getControleConsulta().getValorConsulta().trim());
//				}
//				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
//				objs = getFacadeFactory().getTrancamentoFacade().consultaRapidaPorCodigoRequerimento(new Integer(valorInt), getUnidadeEnsinoLogado().getCodigo(), true, getUsuarioLogado());
//			}
//			if (getControleConsulta().getCampoConsulta().equals("tipoJustificativa")) {
//				if (getControleConsulta().getValorConsulta().length() < 2) {
//					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
//				}
//				objs = getFacadeFactory().getTrancamentoFacade().consultaRapidaPorTipoJustificativa(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), true, getUsuarioLogado());
//			}
//			if (getControleConsulta().getCampoConsulta().equals("nomePessoa")) {
//				objs = getFacadeFactory().getTrancamentoFacade().consultaRapidaPorNomeAluno(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), true, getUsuarioLogado());
//			}
//			if (getControleConsulta().getCampoConsulta().equals("turma")) {
//				if (getControleConsulta().getValorConsulta().length() < 2) {
//					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
//				}
//				objs = getFacadeFactory().getTrancamentoFacade().consultaRapidaPorTurma(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), true, getUsuarioLogado());
//			}
//			setListaConsulta(objs); 
			setMensagemID("msg_dados_consultados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("trancamentoCons.xhtml");
		} catch (Exception e) {
//			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("trancamentoCons.xhtml");
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

	/**
	 * Operação responsável por processar a exclusão um objeto da classe
	 * <code>TrancamentoVO</code> Após a exclusão ela automaticamente aciona a
	 * rotina para uma nova inclusão.
	 */
	public void excluir() {
		try {
			getFacadeFactory().getTrancamentoFacade().excluir(trancamentoVO, getUsuarioLogado());
			setTrancamentoVO(new TrancamentoVO());
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
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

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do
	 * ComboBox correspondente ao atributo <code>tipoJustificativa</code>
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<SelectItem> listaSelectItemTipoJustificativaTrancamento;
	public List<SelectItem> getListaSelectItemTipoJustificativaTrancamento() throws Exception {
		if(listaSelectItemTipoJustificativaTrancamento == null) {
		listaSelectItemTipoJustificativaTrancamento = new ArrayList<SelectItem>(0);
		listaSelectItemTipoJustificativaTrancamento.add(new SelectItem("", ""));
		Hashtable tipoJustificativaAlteracaoMatriculas = (Hashtable) Dominios.getTipoJustificativaAlteracaoMatricula();
		Enumeration keys = tipoJustificativaAlteracaoMatriculas.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) tipoJustificativaAlteracaoMatriculas.get(value);
			listaSelectItemTipoJustificativaTrancamento.add(new SelectItem(value, label));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) listaSelectItemTipoJustificativaTrancamento, ordenador);
		}
		return listaSelectItemTipoJustificativaTrancamento;
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

	/**
	 * Método responsável por inicializar List<SelectItem> de valores do
	 * ComboBox correspondente ao atributo <code>situacao</code>
	 */
	
	public List<SelectItem> listaSelectItemSituacaoTrancamento;
	public List<SelectItem> getListaSelectItemSituacaoTrancamento() throws Exception {
		if(listaSelectItemSituacaoTrancamento == null) {
			listaSelectItemSituacaoTrancamento = new ArrayList<SelectItem>(0);
			listaSelectItemSituacaoTrancamento.add(new SelectItem("", ""));
		Hashtable situacaoTrancamentos = (Hashtable) Dominios.getSituacaoTrancamento();
		Enumeration keys = situacaoTrancamentos.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) situacaoTrancamentos.get(value);
			listaSelectItemSituacaoTrancamento.add(new SelectItem(value, label));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) listaSelectItemSituacaoTrancamento, ordenador);
		}
		return listaSelectItemSituacaoTrancamento;
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
			getTrancamentoVO().setCodigoRequerimento(getFacadeFactory().getRequerimentoFacade().consultarPorChavePrimariaFiltrandoPorUnidadeEnsino(getTrancamentoVO().getCodigoRequerimento().getCodigo(), "TR", super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema()));
			getFacadeFactory().getTrancamentoFacade().validarSituacaoRequerimento(getTrancamentoVO().getCodigoRequerimento());
			getTrancamentoVO().setMatricula(getTrancamentoVO().getCodigoRequerimento().getMatricula());
			//getFacadeFactory().getTrancamentoFacade().executarValidacaoExistePendenciaFinanceiraEPreMatriculaAtiva(getTrancamentoVO().getMatricula(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			getTrancamentoVO().setUltimaMatriculaPeriodoVO(getFacadeFactory().getMatriculaPeriodoFacade().consultarUltimaMatriculaPeriodoPorMatriculaSituacoes(trancamentoVO.getMatricula().getMatricula(), "'AT', 'PR', 'PC', 'AC', 'TR', 'ER', 'FI'", false, Uteis.NIVELMONTARDADOS_COMBOBOX, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
			getTrancamentoVO().setAno(getTrancamentoVO().getUltimaMatriculaPeriodoVO().getAno());
			getTrancamentoVO().setSemestre(getTrancamentoVO().getUltimaMatriculaPeriodoVO().getSemestre());
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setTrancamentoVO(null);
			inicializarUsuarioResponsavelTransferenciaSaidaUsuarioLogado();
		}
	}

	public void selecionarRequerimento() {
		try {
			RequerimentoVO obj = (RequerimentoVO) context().getExternalContext().getRequestMap().get("requerimentoItens");
			getTrancamentoVO().setCodigoRequerimento(getFacadeFactory().getRequerimentoFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema()));
			getFacadeFactory().getTrancamentoFacade().validarSituacaoRequerimento(getTrancamentoVO().getCodigoRequerimento());
			getTrancamentoVO().setMatricula(getTrancamentoVO().getCodigoRequerimento().getMatricula());
			//getFacadeFactory().getTrancamentoFacade().executarValidacaoExistePendenciaFinanceiraEPreMatriculaAtiva(getTrancamentoVO().getMatricula(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			getTrancamentoVO().setUltimaMatriculaPeriodoVO(getFacadeFactory().getMatriculaPeriodoFacade().consultarUltimaMatriculaPeriodoPorMatriculaSituacoes(trancamentoVO.getMatricula().getMatricula(), "'AT', 'PR', 'PC', 'AC', 'TR', 'ER', 'FI'", false, Uteis.NIVELMONTARDADOS_COMBOBOX, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
			getTrancamentoVO().setAno(getTrancamentoVO().getUltimaMatriculaPeriodoVO().getAno());
			getTrancamentoVO().setSemestre(getTrancamentoVO().getUltimaMatriculaPeriodoVO().getSemestre());
			getListaConsultaRequerimento().clear();
			setCampoConsultaRequerimento("");
			setValorConsultaRequerimento("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setTrancamentoVO(null);
			inicializarUsuarioResponsavelTransferenciaSaidaUsuarioLogado();
		}
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
			limparMensagem();	
			setAbrirModalPanelHistorico("");
			if (Uteis.isAtributoPreenchido(getTrancamentoVO().getHistoricoVOs())) {
				setAbrirModalPanelHistorico("Richfaces.$('panelHistorico').show();");
			} else {
				getFacadeFactory().getTrancamentoFacade().persistir(getTrancamentoVO(), getConfiguracaoGeralPadraoSistema(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getTrancamentoVO().getMatricula().getUnidadeEnsino().getCodigo()), getUsuarioLogado());
				consultarListaSelectItemTipoTextoPadrao(getTrancamentoVO().getMatricula().getUnidadeEnsino().getCodigo());
				setMensagemID("msg_dados_gravados");				
			}
		} catch (Exception e) {
			setAbrirModalPanelHistorico("");
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	

	public void realizarDefinicaoMatriculaPeriodoRealizarTrancamento(){
		try{
			limparMensagem();
			setAbrirModalPanelHistorico("");
			getFacadeFactory().getTrancamentoFacade().realizarDefinicaoMatriculaPeriodoRealizarTrancamento(getTrancamentoVO(), getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(getTrancamentoVO().getMatricula().getUnidadeEnsino().getCodigo()), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getTrancamentoVO().getMatricula().getUnidadeEnsino().getCodigo()), getUsuarioLogado());			
			if(getTrancamentoVO().getMensagemConfirmacao().trim().isEmpty()){
				getTrancamentoVO().setMensagemConfirmacao(UteisJSF.internacionalizar("msg_Trancamento_avisoConfirmacaoTrancamento"));							
			}		
			setAbrirModalPanelHistorico("RichFaces.$('panelAviso').show()");
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage());
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
			getTrancamentoVO().setMatricula(getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(getTrancamentoVO().getMatricula().getMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), NivelMontarDados.TODOS, getUsuarioLogado()));
			//getFacadeFactory().getTrancamentoFacade().executarValidacaoExistePendenciaFinanceiraEPreMatriculaAtiva(getTrancamentoVO().getMatricula(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			getTrancamentoVO().setUltimaMatriculaPeriodoVO(getFacadeFactory().getMatriculaPeriodoFacade().consultarUltimaMatriculaPeriodoPorMatriculaSituacoes(trancamentoVO.getMatricula().getMatricula(), "'AT', 'PR', 'PC', 'AC', 'TR', 'ER', 'FI'", false, Uteis.NIVELMONTARDADOS_COMBOBOX, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
			getTrancamentoVO().setAno(getTrancamentoVO().getUltimaMatriculaPeriodoVO().getAno());
			getTrancamentoVO().setSemestre(getTrancamentoVO().getUltimaMatriculaPeriodoVO().getSemestre());
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setTrancamentoVO(null);
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
		tipoConsultaCombo.add(new SelectItem("turma", "Turma"));
//		tipoConsultaCombo.add(new SelectItem("data", "Data"));
//		tipoConsultaCombo.add(new SelectItem("situacao", "Situação Requerimento"));
		tipoConsultaCombo.add(new SelectItem("codigoRequerimento", "Código Requerimento"));
//		tipoConsultaCombo.add(new SelectItem("tipoJustificativa", "Tipo Justificativa"));
		tipoConsultaCombo.add(new SelectItem("nomePessoa", "Responsável Autorização"));
		tipoConsultaCombo.add(new SelectItem("codigo", "Código"));
		}
		return tipoConsultaCombo;
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
		return Uteis.getCaminhoRedirecionamentoNavegacao("trancamentoCons.xhtml");
	}

	public TrancamentoVO getTrancamentoVO() {
		if (trancamentoVO == null) {
			trancamentoVO = new TrancamentoVO();
		}
		return trancamentoVO;
	}

	public void setTrancamentoVO(TrancamentoVO trancamentoVO) {
		this.trancamentoVO = trancamentoVO;
	}

	/**
	 * @return the listaConsultaAluno
	 */
	public List<MatriculaVO> getListaConsultaAluno() {
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
		return valorConsultaAluno;
	}

	/**
	 * @param valorConsultaAluno
	 *            the valorConsultaAluno to set
	 */
	public void setValorConsultaAluno(String valorConsultaAluno) {
		this.valorConsultaAluno = valorConsultaAluno;
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

	public void consultarAluno() {
		try {
			List<MatriculaVO> objs = new ArrayList<MatriculaVO>(0);
			if (getValorConsultaAluno().equals("")) {
				throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
			}
			if (getCampoConsultaAluno().equals("matricula")) {
				MatriculaVO obj = getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatriculaAtivaOuTrancada(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
				if (!obj.getMatricula().equals("")) {
					objs.add(obj);
				}
			}
			if (getCampoConsultaAluno().equals("nomePessoa")) {
				if (getValorConsultaAluno().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getMatriculaFacade().consultarPorNomePessoaAtivaOuTrancada(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("registroAcademico")) {
				if (getValorConsultaAluno().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getMatriculaFacade().consultarPorRegistroAcademicoPessoaAtivaOuTrancada(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("nomeCurso")) {
				if (getValorConsultaAluno().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getMatriculaFacade().consultarPorNomeCursoAtivoTrancado(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
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
			getTrancamentoVO().setMatricula(getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula(), obj.getUnidadeEnsino().getCodigo(), NivelMontarDados.TODOS, getUsuarioLogado()));
			getTrancamentoVO().setUltimaMatriculaPeriodoVO(getFacadeFactory().getMatriculaPeriodoFacade().consultarUltimaMatriculaPeriodoPorMatriculaSituacoes(trancamentoVO.getMatricula().getMatricula(), "'AT', 'PR', 'PC', 'AC', 'TR', 'ER', 'FI'", false, Uteis.NIVELMONTARDADOS_COMBOBOX, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
			getTrancamentoVO().setAno(getTrancamentoVO().getUltimaMatriculaPeriodoVO().getAno());
			getTrancamentoVO().setSemestre(getTrancamentoVO().getUltimaMatriculaPeriodoVO().getSemestre());
			setValorConsultaAluno("");
			setCampoConsultaAluno("");
			getListaConsultaAluno().clear();
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setTrancamentoVO(null);
			inicializarUsuarioResponsavelTransferenciaSaidaUsuarioLogado();
		}
	}

	public void imprimirPDF() {
		try {
			List<MatriculaPeriodoVO> objtetos = getFacadeFactory().getDeclaracaoTrancamentoRelFacade().montarListaObjetos(getTrancamentoVO(), getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			if (!objtetos.isEmpty()) {
				getSuperParametroRelVO().setCaminhoBaseRelatorio(DeclaracaoTrancamentoRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setSubReport_Dir(DeclaracaoTrancamentoRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setListaObjetos(objtetos);
				getSuperParametroRelVO().setUnidadeEnsino(objtetos.get(0).getTurma().getUnidadeEnsino().getNome());
				getSuperParametroRelVO().setTituloRelatorio("DECLARAÇÃO DE TRANCAMENTO");
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setNomeDesignIreport(DeclaracaoTrancamentoRel.getDesignIReportRelatorio());
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				realizarImpressaoRelatorio();
				setMensagemID("msg_relatorio_ok");
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarListaSelectItemMotivoCancelamentoTrancamento() {
		try {
			montarListaSelectItemMotivoCancelamentoTrancamento("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
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

	public void consultarListaSelectItemTipoTextoPadrao(Integer unidadeEnsino) {
		try {
			getListaSelectItemTipoTextoPadrao().clear();
			List<TextoPadraoDeclaracaoVO> textoPadraoDeclaracaoVOs = getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorTipo("TR", unidadeEnsino, "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			setListaSelectItemTipoTextoPadrao(UtilSelectItem.getListaSelectItem(textoPadraoDeclaracaoVOs, "codigo", "descricao"));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList<MatriculaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
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
			getFacadeFactory().getTrancamentoFacade().validarDadosAntesImpressao(getTrancamentoVO(), getTextoPadraoDeclaracao());
			setListaImpressaoDeclaracaoVOs(getFacadeFactory().getImpressaoDeclaracaoFacade().consultarPorMatriculaPorTextoPadrao(getTrancamentoVO().getMatricula().getMatricula(), getTextoPadraoDeclaracao(), TipoDoTextoImpressaoContratoEnum.TEXTO_PADRAO_DECLARACAO, getUsuarioLogado()));
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
			getFacadeFactory().getTrancamentoFacade().validarDadosAntesImpressao(getTrancamentoVO(), getTextoPadraoDeclaracao());
			TextoPadraoDeclaracaoVO textoPadraoDeclaracaoVO = getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorChavePrimaria(textoPadraoDeclaracao, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			setCaminhoRelatorio(getFacadeFactory().getTrancamentoFacade().imprimirDeclaracaoTrancamento(getTrancamentoVO(), textoPadraoDeclaracaoVO, getConfiguracaoGeralPadraoSistema() , getUsuarioLogado()));
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

	public void limparDadosAluno() {
		getTrancamentoVO().setMatricula(null);
		setMensagemID("msg_entre_dados");
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
	 * serão Trancados.
	 * 
	 * @author Wellington Rodrigues - 02/04/2015
	 */
	public void executarMarcacaoDesmarcacaoTodos() {
		for (HistoricoVO obj : getTrancamentoVO().getHistoricoVOs()) {
			if (obj.getEditavel()) {
				obj.setRealizarAlteracaoSituacaoHistorico(getRealizarMarcacaoDesmarcacaoTodos());
			}
		}
	}
	
	public void executarEstorno() {
		UsuarioVO responsavelAntes = getTrancamentoVO().getResponsavelEstorno();
		Date dataAntes = getTrancamentoVO().getDataEstorno();
		try {
			getTrancamentoVO().setResponsavelEstorno(getUsuarioLogadoClone());
			getTrancamentoVO().setDataEstorno(new Date());
			getTrancamentoVO().setSituacao("ES");
			getFacadeFactory().getTrancamentoFacade().executarEstorno(getTrancamentoVO(), getUsuarioLogado());
			setMensagemID("msg_dados_estornados");
		} catch (Exception e) {
			getTrancamentoVO().setResponsavelEstorno(responsavelAntes);
			getTrancamentoVO().setDataEstorno(dataAntes);
			getTrancamentoVO().setSituacao("FD");
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public boolean getIsPermiteEstorno() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("Trancamento_Estornar", getUsuarioLogado());
			return getTrancamentoVO().getSituacao().equals("FD");
		} catch (Exception e) {
			return false;
		}
	}
	
	public List<SelectItem> getListaSelectItemTipoTrancamento () {
		if(listaSelectItemTipoTrancamento == null){
			listaSelectItemTipoTrancamento = new ArrayList<SelectItem>();
			listaSelectItemTipoTrancamento.add(new SelectItem(TipoTrancamentoEnum.NENHUM.getValor(), TipoTrancamentoEnum.NENHUM.getDescricao()));		
			listaSelectItemTipoTrancamento.add(new SelectItem(TipoTrancamentoEnum.ABANDONO_DE_CURSO.getValor(), TipoTrancamentoEnum.ABANDONO_DE_CURSO.getDescricao()));
			listaSelectItemTipoTrancamento.add(new SelectItem(TipoTrancamentoEnum.JUBILAMENTO.getValor(), TipoTrancamentoEnum.JUBILAMENTO.getDescricao()));
			listaSelectItemTipoTrancamento.add(new SelectItem(TipoTrancamentoEnum.TRANCAMENTO.getValor(), TipoTrancamentoEnum.TRANCAMENTO.getDescricao()));
		}
		return listaSelectItemTipoTrancamento;
		
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

	public String getValorConsultaMotivoTrancamento() {
		if(valorConsultaMotivoTrancamento == null) {
			valorConsultaMotivoTrancamento =  "";
		}
		return valorConsultaMotivoTrancamento;
	}

	public void setValorConsultaMotivoTrancamento(String valorConsultaMotivoTrancamento) {
		this.valorConsultaMotivoTrancamento = valorConsultaMotivoTrancamento;
	}

	public String getValorConsultaTipo() {
		if(valorConsultaTipo == null) {
			valorConsultaTipo =  "NE";
		}
		return valorConsultaTipo;
	}

	public void setValorConsultaTipo(String valorConsultaTipo) {
		this.valorConsultaTipo = valorConsultaTipo;
	}

	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if(listaSelectItemUnidadeEnsino == null) {
			try {
				listaSelectItemUnidadeEnsino =  UtilSelectItem.getListaSelectItem(getFacadeFactory().getUnidadeEnsinoFacade().consultarUnidadeEnsinoComboBox(getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado()), "codigo", "nome", !Uteis.isAtributoPreenchido(getUnidadeEnsinoLogado().getCodigo()), true);
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
