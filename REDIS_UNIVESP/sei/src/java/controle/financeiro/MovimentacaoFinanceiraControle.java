package controle.financeiro;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas movimentacaoFinanceiraForm.jsp
 * movimentacaoFinanceiraCons.jsp) com as funcionalidades da classe <code>MovimentacaoFinanceira</code>. Implemtação da
 * camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see MovimentacaoFinanceira
 * @see MovimentacaoFinanceiraVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.OperacaoFuncionalidadeVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.OperacaoFuncionalidadeEnum;
import negocio.comuns.arquitetura.enumeradores.OrigemOperacaoFuncionalidadeEnum;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoFinanceiroEnum;
import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.contabil.FechamentoMesHistoricoModificacaoVO;
import negocio.comuns.contabil.LancamentoContabilVO;
import negocio.comuns.contabil.PlanoContaVO;
import negocio.comuns.contabil.enumeradores.TipoOrigemHistoricoBloqueioEnum;
import negocio.comuns.contabil.enumeradores.TipoPlanoContaEnum;
import negocio.comuns.financeiro.ChequeVO;
import negocio.comuns.financeiro.ConciliacaoContaCorrenteVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.FluxoCaixaVO;
import negocio.comuns.financeiro.MovimentacaoFinanceiraItemVO;
import negocio.comuns.financeiro.MovimentacaoFinanceiraVO;
import negocio.comuns.financeiro.enumerador.TipoContaCorrenteEnum;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.SituacaoMovimentacaoFinanceiraEnum;
import negocio.comuns.utilitarias.dominios.TipoFormaPagamento;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import relatorio.controle.arquitetura.SuperControleRelatorio;

@Controller("MovimentacaoFinanceiraControle")
@Scope("viewScope")
@Lazy
public class MovimentacaoFinanceiraControle extends SuperControleRelatorio implements Serializable {

	private static final long serialVersionUID = 1719632253424940008L;

	private MovimentacaoFinanceiraVO movimentacaoFinanceiraVO;
	protected List<SelectItem> listaSelectItemContaCorrenteOrigem;
	protected List<SelectItem> listaSelectItemContaCorrenteDestino;
	protected List<SelectItem> listaSelectItemFormaPagamento;
	private Date dataInicioConsultar;
	private MovimentacaoFinanceiraItemVO movimentacaoFinanceiraItemVO;
	private ChequeVO chequeVO;
	protected List<SelectItem> listaSelectItemCheque;
	protected String valorConsultaCheque;
	protected String campoConsultaCheque;
	protected List<ChequeVO> listaConsultaCheque;
	private ConciliacaoContaCorrenteVO conciliacaoContaCorrenteVO;
	private LancamentoContabilVO lancamentoContabilVO;
	private String modalAptoParaSerFechado;
	private String valorConsultaPlanoConta;
	private String campoConsultaPlanoConta;
	private List<PlanoContaVO> listaConsultaPlanoConta;
	private Boolean PermitirRealizarMovimentacaoFinanceiroContaCaixaResponsavel;
	private Boolean consultaDataScroller;
    private List<SelectItem> listaSelectItemUnidadeEnsino;    
    private boolean permitirDesconsiderandoContabilidadeConciliacao = false;
    
    private String modalLiberarContaCaixaFechada;
    
    private String usernameLiberarOperacaoFuncionalidade;
	private String senhaLiberarOperacaoFuncionalidade;
	private String nomeSacadoConsultaPorCheque;
	private String numeroConsultaPorCheque;
	private Boolean permiteRealizarMovimentacaoContasCaixaFechada;

	public MovimentacaoFinanceiraControle() throws Exception {
		this.setDataInicioConsultar(new Date());
		setControleConsulta(new ControleConsulta());
		setControleConsultaOtimizado(new DataModelo());
		setMensagemID("msg_entre_prmconsulta");
	}

	public void inicializarResponsavel() {
		try {
			getMovimentacaoFinanceiraVO().setResponsavel(getUsuarioLogadoClone());
		} catch (Exception e) {
			getMovimentacaoFinanceiraVO().setResponsavel(new UsuarioVO());
		}

	}

	@PostConstruct
	public void realizarCarregamentoTela() {
		MovimentacaoFinanceiraVO obj = (MovimentacaoFinanceiraVO) context().getExternalContext().getSessionMap().get("movFinanceira");
		try {
			if (obj != null && !obj.getCodigo().equals(0)) {
				carregarDadosParaEditarMovimentacaoFinanceira(obj);
				setMensagemID("msg_dados_editar", Uteis.ALERTA);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			context().getExternalContext().getSessionMap().remove("conciliacaoBancaria");
		}

	}

	@PostConstruct
	public void realizarCarregamentoRecebimentoVindoTelaConciliacaoBancaria() {
		Boolean realizarEstornoConciliacaoBancaria = (Boolean) context().getExternalContext().getSessionMap().get("realizarEstornoConciliacaoBancaria");
		try {
			if (realizarEstornoConciliacaoBancaria != null && realizarEstornoConciliacaoBancaria) {
				MovimentacaoFinanceiraVO obj = (MovimentacaoFinanceiraVO) context().getExternalContext().getSessionMap().get("movimentacaoConciliacaoBancaria");
				if (Uteis.isAtributoPreenchido(obj)) {
					carregarDadosParaEditarMovimentacaoFinanceira(obj);
				}
				setConciliacaoContaCorrenteVO((ConciliacaoContaCorrenteVO) context().getExternalContext().getSessionMap().get("conciliacaoBancaria"));
				setMensagemID("msg_dados_editar", Uteis.ALERTA);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			context().getExternalContext().getSessionMap().remove("realizarEstornoConciliacaoBancaria");
			context().getExternalContext().getSessionMap().remove("movimentacaoConciliacaoBancaria");
			context().getExternalContext().getSessionMap().remove("conciliacaoBancaria");
		}

	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe <code>MovimentacaoFinanceira</code> para edição pelo usuário da aplicação.
	 */
	public String novo() {
		try {
			setMovimentacaoFinanceiraVO(new MovimentacaoFinanceiraVO());
			verificarPermitirRealizarMovimentacaoFinanceiroContaCaixaResponsavel();
			inicializarListasSelectItemTodosComboBox();
			setMovimentacaoFinanceiraItemVO(new MovimentacaoFinanceiraItemVO());
			inicializarResponsavel();
			verificarPermissaoLancamentoContabilMovimentacaoFinanceira();
			verificarPermitirMovimentacaoFinanceiroDesconsiderandoContabilidadeConciliacao();
			setOncompleteModal("");
			setMensagemID("msg_entre_dados");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("movimentacaoFinanceiraForm.xhtml");
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe <code>MovimentacaoFinanceira</code> para alteração. O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa disponibilizá-lo para edição.
	 */
	public String editar() {
		try {
			MovimentacaoFinanceiraVO obj = (MovimentacaoFinanceiraVO) context().getExternalContext().getRequestMap().get("movimentacaoFinanceiraItens");
			carregarDadosParaEditarMovimentacaoFinanceira(obj);
			setMensagemID("msg_dados_editar");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("movimentacaoFinanceiraForm.xhtml");
	}

	public void carregarDadosParaEditarMovimentacaoFinanceira(MovimentacaoFinanceiraVO obj) throws Exception {
		obj = getFacadeFactory().getMovimentacaoFinanceiraFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
		obj.setNovoObj(Boolean.FALSE);
		setMovimentacaoFinanceiraVO(obj);
		inicializarListasSelectItemTodosComboBox();
		setMovimentacaoFinanceiraItemVO(new MovimentacaoFinanceiraItemVO());
		verificarPermissaoLancamentoContabilMovimentacaoFinanceira();
		verificarPermitirMovimentacaoFinanceiroDesconsiderandoContabilidadeConciliacao();
		if(obj.getContaCorrenteOrigem().getCodigo() == null || obj.getContaCorrenteOrigem().getCodigo().equals(0)){
			obj.setSomenteContaDestino(true);
		}
		setOncompleteModal("");
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>MovimentacaoFinanceira</code>. Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de erro.
	 */
	public synchronized void gravar() {
		try {
			
			FluxoCaixaVO fluxoCaixaContaCorrenteOrigem = new FluxoCaixaVO();
			FluxoCaixaVO fluxoCaixaContaCorrenteDestino = new FluxoCaixaVO();
			
	    	if (!getMovimentacaoFinanceiraVO().isSomenteContaDestino() && getMovimentacaoFinanceiraVO().getContaCorrenteOrigem().getTipoContaCorrenteEnum().equals(TipoContaCorrenteEnum.CAIXA)) {
	    		 fluxoCaixaContaCorrenteOrigem = getFacadeFactory().getFluxoCaixaFacade().consultarPorFluxoCaixaAberto(new Date(), getMovimentacaoFinanceiraVO().getContaCorrenteOrigem().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
	    	if (getMovimentacaoFinanceiraVO().getContaCorrenteDestino().getTipoContaCorrenteEnum().equals(TipoContaCorrenteEnum.CAIXA)) {
	    		fluxoCaixaContaCorrenteDestino = getFacadeFactory().getFluxoCaixaFacade().consultarPorFluxoCaixaAberto(new Date(), getMovimentacaoFinanceiraVO().getContaCorrenteDestino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}

			if ((!Uteis.isAtributoPreenchido(fluxoCaixaContaCorrenteOrigem) && !getMovimentacaoFinanceiraVO().isSomenteContaDestino() && getMovimentacaoFinanceiraVO().getContaCorrenteOrigem().getTipoContaCorrenteEnum().equals(TipoContaCorrenteEnum.CAIXA))
				|| (!Uteis.isAtributoPreenchido(fluxoCaixaContaCorrenteDestino) && getMovimentacaoFinanceiraVO().getContaCorrenteDestino().getTipoContaCorrenteEnum().equals(TipoContaCorrenteEnum.CAIXA)) ) {
				setModalLiberarContaCaixaFechada("RichFaces.$('panelLiberarBloqueioMovimentacaoContaFechada').show();RichFaces.$('panelMovimentacaoFinanceiroDesconsiderandoContabilidadeConciliacao').hide();");
			} else {
				if (Uteis.isAtributoPreenchido(movimentacaoFinanceiraVO)) {
					getFacadeFactory().getMovimentacaoFinanceiraFacade().alterar(movimentacaoFinanceiraVO, getUsuarioLogado());					
				} else {
					if (!movimentacaoFinanceiraVO.getContaCorrenteDestino().getRequerConfirmacaoMovimentacaoFinanceira()) {
						movimentacaoFinanceiraVO.setSituacao("FI");
					}
					getFacadeFactory().getMovimentacaoFinanceiraFacade().incluir(movimentacaoFinanceiraVO, getUsuarioLogado());
				}

				verificarPermissaoLancamentoContabilMovimentacaoFinanceira();
				getMovimentacaoFinanceiraVO().reiniciarControleBloqueioCompetencia();
				setMensagemID("msg_dados_gravados");
				setModalLiberarContaCaixaFechada("RichFaces.$('panelMovimentacaoFinanceiroDesconsiderandoContabilidadeConciliacao').hide();");
				setOncompleteModal("");
			}			
		} catch (Exception e) {
			movimentacaoFinanceiraVO.setSituacao("PE");
			setMensagemDetalhada("msg_erro", e.getMessage());
			setModalLiberarContaCaixaFechada("");
		}
	}

	public synchronized void persistir() {
		try {
			UsuarioVO usuarioVerif = ControleAcesso.verificarLoginUsuario(this.getUsernameLiberarOperacaoFuncionalidade(), this.getSenhaLiberarOperacaoFuncionalidade(), true, Uteis.NIVELMONTARDADOS_TODOS);
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("MovimentacaoFinanceiraContaCaixaFechada", usuarioVerif);

			getFacadeFactory().getMovimentacaoFinanceiraFacade().persistir(movimentacaoFinanceiraVO, getUsuarioLogado(), true);

			OperacaoFuncionalidadeVO operacaoFuncionalidadeVO = getFacadeFactory().getOperacaoFuncionalidadeFacade().executarGeracaoOperacaoFuncionalidade(OrigemOperacaoFuncionalidadeEnum.MOVIMENTACAO_FINANCEIRA, "", OperacaoFuncionalidadeEnum.LIBERAR_MOVIMENTACAO_FINANCEIRA_CAIXA_FECHADO, usuarioVerif, "");
			getFacadeFactory().getOperacaoFuncionalidadeFacade().incluir(operacaoFuncionalidadeVO);

			verificarPermissaoLancamentoContabilMovimentacaoFinanceira();
			getMovimentacaoFinanceiraVO().reiniciarControleBloqueioCompetencia();
			setOncompleteModal("");
			setModalLiberarContaCaixaFechada("");
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			movimentacaoFinanceiraVO.setSituacao("PE");
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void prepararParaGravarMovimentacao() {
		try {			
			if(isPermitirDesconsiderandoContabilidadeConciliacao()) {
				setModalLiberarContaCaixaFechada("RichFaces.$('panelMovimentacaoFinanceiroDesconsiderandoContabilidadeConciliacao').show();");
			}else {
				 gravar();	
				 setMensagemID("msg_dados_gravados");
			}
		} catch (Exception e) {
			setModalLiberarContaCaixaFechada("");
			setOncompleteModal("");
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	private boolean realizarMovimentacaoContasCaixaFechada() {
		try {
			ControleAcesso.realizarMovimentacaoContasCaixaFechada("MovimentacaoFinanceiraContaCaixaFechada", getUsuarioLogado());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public void prepararParaEstornarMovimentacao() {
		try {
			if(!isPermitirDesconsiderandoContabilidadeConciliacao()) {
				setOncompleteModal("RichFaces.$('panelMotivo').show();");
			}else {
				estornar();
			}
			setMensagemID("msg_entre_dados");
		} catch (Exception e) {
			setOncompleteModal("");
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void estornarContabilidadeConciliacao() {
		try {
			getFacadeFactory().getMovimentacaoFinanceiraFacade().estornarContabilidadeConciliacao(getMovimentacaoFinanceiraVO(), getUsuarioLogadoClone());
			setOncompleteModal("");
			setMensagemID("msg_entre_dados");
		} catch (Exception e) {
			setOncompleteModal("");
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP MovimentacaoFinanceiraCons.jsp. Define o tipo de consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
	 */
	@SuppressWarnings("unchecked")
	public String consultar() {
		try {

			getControleConsultaOtimizado().getListaConsulta().clear();
			registrarAtividadeUsuario(getUsuarioLogado(), "MovimentacaoFinanceiraControle", "Iniciando Consultar Movimentação Financeira", "Consultando");
			super.consultar();
			getControleConsultaOtimizado().setLimitePorPagina(10);
			List<MovimentacaoFinanceiraVO> objs = new ArrayList<>(0);

			if (getControleConsulta().getCampoConsulta().equals("codigo")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getMovimentacaoFinanceiraFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getMovimentacaoFinanceiraFacade().consultarTotalRegistrosPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("data")) {
				objs = getFacadeFactory().getMovimentacaoFinanceiraFacade().consultarPorData(Uteis.getDateTime(getControleConsulta().getDataIni(), 0, 0, 0),
						Uteis.getDateTime(getControleConsulta().getDataIni(), 23, 59, 59), true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset());
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getMovimentacaoFinanceiraFacade().consultarTotalRegistrosPorData(Uteis.getDateTime(getControleConsulta().getDataIni(), 0, 0, 0),
						Uteis.getDateTime(getControleConsulta().getDataIni(), 23, 59, 59), true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeUsuario")) {
				objs = getFacadeFactory().getMovimentacaoFinanceiraFacade().consultarPorNomeUsuario(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset());
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getMovimentacaoFinanceiraFacade().consultarTotalRegistrosPorNomeUsuario(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("numeroContaCorrenteOrigem")) {
				objs = getFacadeFactory().getMovimentacaoFinanceiraFacade().consultarPorNumeroContaCorrente(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset());
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getMovimentacaoFinanceiraFacade().consultarTotalRegistrosPorNumeroContaCorrente(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("numeroContaCorrenteDestino")) {
				objs = getFacadeFactory().getMovimentacaoFinanceiraFacade().consultarPorNumeroContaCorrenteDestino(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset());
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getMovimentacaoFinanceiraFacade().consultarTotalRegistrosPorNumeroContaCorrenteDestino(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("situacao")) {
				objs = getFacadeFactory().getMovimentacaoFinanceiraFacade().consultarPorSituacao(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset());
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getMovimentacaoFinanceiraFacade().consultarTotalRegistrosPorSituacao(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
			}
			if ("cheque".equals(getControleConsulta().getCampoConsulta())) {
				objs = getFacadeFactory().getMovimentacaoFinanceiraFacade().consultarPorNumeroNomeSacadoCheque(getNumeroConsultaPorCheque(), getNomeSacadoConsultaPorCheque(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getMovimentacaoFinanceiraFacade().consultarTotalRegistrosPorNumeroNomeSacadoCheque(getNumeroConsultaPorCheque(), getNomeSacadoConsultaPorCheque(), true, getUsuarioLogado()));
			}
			getControleConsultaOtimizado().setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("movimentacaoFinanceiraCons.xhtml");
		} catch (Exception e) {
			setListaConsulta(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("movimentacaoFinanceiraCons.xhtml");
		}
	}

	@SuppressWarnings("deprecation")
	public void imprimirPDF() {
		try {
			limparMensagem();
			this.setCaminhoRelatorio("");
			setFazerDownload(true);
			// setCaminhoRelatorio(getFacadeFactory().getTextoPadraoProcessoSeletivoFacade().imprimirTextoPadrao(texto, getInscricaoVO(), listaRegistro, getConsultarPor(), false, getTipoRelatorio(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado()));
			setCaminhoRelatorio(getFacadeFactory().getMovimentacaoFinanceiraFacade().imprimirTextoPadrao(getMovimentacaoFinanceiraVO(), getConfiguracaoFinanceiroPadraoSistema(), getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(), getUsuarioLogado()));
			setMensagemID("msg_relatorio_ok");
		} catch (Exception e) {
			setMensagemDetalhada(e.getMessage());
		}
	}

	public List<SelectItem> getListaSituacaoMovimentacaoFinanceira() {
		return UtilPropriedadesDoEnum.getListaSelectItemDoEnum(SituacaoMovimentacaoFinanceiraEnum.class, false);
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe <code>MovimentacaoFinanceiraVO</code> Após a exclusão ela automaticamente aciona a rotina para uma nova inclusão.
	 */
	public String estornar() {
		try {
			getFacadeFactory().getMovimentacaoFinanceiraFacade().estornar(getMovimentacaoFinanceiraVO(), getPermiteRealizarMovimentacaoContasCaixaFechada(), getUsuarioLogado());
			if (Uteis.isAtributoPreenchido(getConciliacaoContaCorrenteVO())) {
				context().getExternalContext().getSessionMap().put("movimentacaoConciliacaoBancaria", null);
				context().getExternalContext().getSessionMap().put("conciliacaoBancaria", getConciliacaoContaCorrenteVO());
				context().getExternalContext().getSessionMap().put("realizarEstornoConciliacaoBancaria", true);
			}
			setMovimentacaoFinanceiraVO(new MovimentacaoFinanceiraVO());
			setMovimentacaoFinanceiraItemVO(new MovimentacaoFinanceiraItemVO());
			inicializarResponsavel();
			setOncompleteModal("");
			setMensagemID("msg_dados_excluidos");
			return Uteis.getCaminhoRedirecionamentoNavegacao("movimentacaoFinanceiraForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("movimentacaoFinanceiraForm.xhtml");
		}
	}

	/*
	 * Método responsável por adicionar um novo objeto da classe <code>MovimentacaoFinanceiraChqR</code> para o objeto <code>movimentacaoFinanceiraVO</code> da classe <code>MovimentacaoFinanceira</code>
	 */
	public void adicionarMovimentacaoFinanceiraItem() throws Exception {
		try {
			if (!getMovimentacaoFinanceiraVO().getCodigo().equals(0)) {
				movimentacaoFinanceiraItemVO.setMovimentacaoFinanceira(getMovimentacaoFinanceiraVO().getCodigo());
			}
			if (getMovimentacaoFinanceiraItemVO().getFormaPagamento().getCodigo().intValue() != 0) {
				FormaPagamentoVO obj = getFacadeFactory().getFormaPagamentoFacade().consultarPorChavePrimaria(getMovimentacaoFinanceiraItemVO().getFormaPagamento().getCodigo(), false,
						Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				getMovimentacaoFinanceiraItemVO().setFormaPagamento(obj);
			}
			getMovimentacaoFinanceiraVO().adicionarObjMovimentacaoFinanceiraItemVOs(getMovimentacaoFinanceiraItemVO());
			this.setMovimentacaoFinanceiraItemVO(new MovimentacaoFinanceiraItemVO());
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/*
	 * Método responsável por disponibilizar dados de um objeto da classe <code>MovimentacaoFinanceiraChqR</code> para edição pelo usuário.
	 */
	public void editarMovimentacaoFinanceiraItem() throws Exception {
		MovimentacaoFinanceiraItemVO obj = (MovimentacaoFinanceiraItemVO) context().getExternalContext().getRequestMap().get("movimentacaoFinanceiraItemItens");
		setChequeVO(obj.getCheque());
	}

	/*
	 * Método responsável por remover um novo objeto da classe <code>MovimentacaoFinanceiraChqR</code> do objeto <code>movimentacaoFinanceiraVO</code> da classe <code>MovimentacaoFinanceira</code>
	 */
	public void removerMovimentacaoFinanceiraItem() throws Exception {
		MovimentacaoFinanceiraItemVO obj = (MovimentacaoFinanceiraItemVO) context().getExternalContext().getRequestMap().get("movimentacaoFinanceiraItemItens");
		if (obj.getFormaPagamento().getTipo().equals("")) {
			obj.setFormaPagamento(getFacadeFactory().getFormaPagamentoFacade().consultarPorChavePrimaria(obj.getFormaPagamento().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
		}
		getMovimentacaoFinanceiraVO().excluirObjMovimentacaoFinanceiraItemVOs(obj);
		obj = null;
		setMensagemID("msg_dados_excluidos");
	}

	@SuppressWarnings("unchecked")
	public void consultarCheque() {
		try {
			List<ChequeVO> objs = new ArrayList<>(0);
			if (getMovimentacaoFinanceiraVO().getContaCorrenteOrigem().getCodigo() == 0) {
				throw new Exception("Deve ser informada uma CONTA ORIGEM para consulta de cheques.");
			}
			if (getCampoConsultaCheque().equals("nomeCliente")) {
				if (getValorConsultaCheque().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getChequeFacade().consultarPorNomePessoaSituacaoSemMovimentacaoFinanceiraPendenteFinalizada(getValorConsultaCheque(), "EC", getMovimentacaoFinanceiraVO().getContaCorrenteOrigem().getCodigo(),
						getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaCheque().equals("sacado")) {
				if (getValorConsultaCheque().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getChequeFacade().consultarPorSacadoSituacaoSemMovimentacaoFinanceiraPendenteFinalizada(getValorConsultaCheque(), "EC", getMovimentacaoFinanceiraVO().getContaCorrenteOrigem().getCodigo(),
						getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaCheque().equals("nomeBanco")) {
				if (getValorConsultaCheque().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getChequeFacade().consultarPorNomeBancoSituacaoSemMovimentacaoFinanceiraPendenteFinalizada(getValorConsultaCheque(), "EC", getMovimentacaoFinanceiraVO().getContaCorrenteOrigem().getCodigo(),
						getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaCheque().equals("numeroAgenciaAgencia")) {
				if (getValorConsultaCheque().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getChequeFacade().consultarPorNumeroAgenciaAgenciaSituacaoSemMovimentacaoFinanceiraPendenteFinalizada(getValorConsultaCheque(), "EC",
						getMovimentacaoFinanceiraVO().getContaCorrenteOrigem().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaCheque().equals("numero")) {
				if (getValorConsultaCheque().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getChequeFacade().consultarPorNumeroSituacaoSemMovimentacaoFinanceiraPendenteFinalizada(getValorConsultaCheque(), "EC", getMovimentacaoFinanceiraVO().getContaCorrenteOrigem().getCodigo(),
						getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaCheque().equals("dataEmissao")) {
				// if (getValorConsultaCheque().length() < 2) {
				// throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				// }
				objs = getFacadeFactory().getChequeFacade().consultarPorDataEmissaoSituacaoSemMovimentacaoFinanceiraPendenteFinalizada(getControleConsulta().getDataIni(), getControleConsulta().getDataIni(), "EC",
						getMovimentacaoFinanceiraVO().getContaCorrenteOrigem().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaCheque().equals("dataPrevisao")) {
				// if (getValorConsultaCheque().length() < 2) {
				// throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				// }
				objs = getFacadeFactory().getChequeFacade().consultarPorDataPrevisaoSituacaoSemMovimentacaoFinanceiraPendenteFinalizada(getControleConsulta().getDataIni(), getControleConsulta().getDataIni(), "EC",
						getMovimentacaoFinanceiraVO().getContaCorrenteOrigem().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaCheque(objs);
			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			setListaConsultaCheque(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}

	public List<SelectItem> getTipoConsultaComboCheque() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("nomeCliente", "Remetente"));
		itens.add(new SelectItem("sacado", "Sacado"));
		itens.add(new SelectItem("nomeBanco", "Banco"));
		itens.add(new SelectItem("numeroAgenciaAgencia", "Agência"));
		itens.add(new SelectItem("numero", "Número"));
		itens.add(new SelectItem("dataEmissao", "Data Emissão"));
		itens.add(new SelectItem("dataPrevisao", "Data Previsão"));
		return itens;
	}

	public boolean isCampoConsultaChequeData() {
		return getCampoConsultaCheque() != null && (getCampoConsultaCheque().equals("dataEmissao") || getCampoConsultaCheque().equals("dataPrevisao"));
	}

	public String getMascaraConsultaCheque() {
		if (getCampoConsultaCheque().equals("dataEmissao") || getCampoConsultaCheque().equals("dataPrevisao")) {
			return "return mascara(this.form,'formConsultaCheque:valorConsulta','99/99/9999',event)";
		}
		return "";
	}

	public void selecionarCheque() {
		try {
			ChequeVO obj = (ChequeVO) context().getExternalContext().getRequestMap().get("chequeItens");
			getMovimentacaoFinanceiraItemVO().setCheque(obj);
			getMovimentacaoFinanceiraItemVO().setValor(obj.getValor());
			if (!getMovimentacaoFinanceiraVO().getCodigo().equals(0)) {
				movimentacaoFinanceiraItemVO.setMovimentacaoFinanceira(getMovimentacaoFinanceiraVO().getCodigo());
			}
			getMovimentacaoFinanceiraVO().adicionarObjMovimentacaoFinanceiraItemVOs(getMovimentacaoFinanceiraItemVO());
			removerChequeListaConsulta(obj.getCodigo());
			FormaPagamentoVO formaPagamento = getMovimentacaoFinanceiraItemVO().getFormaPagamento();
			this.setMovimentacaoFinanceiraItemVO(new MovimentacaoFinanceiraItemVO());
			getMovimentacaoFinanceiraItemVO().getFormaPagamento().setCodigo(formaPagamento.getCodigo());
			getMovimentacaoFinanceiraItemVO().getFormaPagamento().setNome(formaPagamento.getNome());
			getMovimentacaoFinanceiraItemVO().getFormaPagamento().setTipo(formaPagamento.getTipo());
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void adicionarTodosCheques() {
		try {
			for (ChequeVO obj : getListaConsultaCheque()) {
				getMovimentacaoFinanceiraItemVO().setCheque(obj);
				getMovimentacaoFinanceiraItemVO().setValor(obj.getValor());

				if (!getMovimentacaoFinanceiraVO().getCodigo().equals(0)) {
					movimentacaoFinanceiraItemVO.setMovimentacaoFinanceira(getMovimentacaoFinanceiraVO().getCodigo());
				}
				getMovimentacaoFinanceiraVO().adicionarObjMovimentacaoFinanceiraItemVOs(getMovimentacaoFinanceiraItemVO());
				FormaPagamentoVO formaPagamento = getMovimentacaoFinanceiraItemVO().getFormaPagamento();
				this.setMovimentacaoFinanceiraItemVO(new MovimentacaoFinanceiraItemVO());
				getMovimentacaoFinanceiraItemVO().getFormaPagamento().setCodigo(formaPagamento.getCodigo());
				getMovimentacaoFinanceiraItemVO().getFormaPagamento().setNome(formaPagamento.getNome());
				getMovimentacaoFinanceiraItemVO().getFormaPagamento().setTipo(formaPagamento.getTipo());
			}
			setListaConsultaCheque(new ArrayList<ChequeVO>(0));
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void removerChequeListaConsulta(Integer cheque) {
		int index = 0;
		Iterator<ChequeVO> i = getListaConsultaCheque().iterator();
		while (i.hasNext()) {
			ChequeVO obj = (ChequeVO) i.next();
			if (obj.getCodigo().intValue() == cheque.intValue()) {
				getListaConsultaCheque().remove(index);
				return;
			}
			index++;
		}
	}

	public void proibirFormaPagamentoCheque() {
		if (getMovimentacaoFinanceiraVO().isSomenteContaDestino() == true) {
			try {
				getMovimentacaoFinanceiraItemVO().getFormaPagamento().setCodigo(5);
				FormaPagamentoVO obj = getFacadeFactory().getFormaPagamentoFacade().consultarPorChavePrimaria(getMovimentacaoFinanceiraItemVO().getFormaPagamento().getCodigo().intValue(), false,
						Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				getMovimentacaoFinanceiraItemVO().setFormaPagamento(obj);
			} catch (Exception ex) {
				getMovimentacaoFinanceiraItemVO().setFormaPagamento(new FormaPagamentoVO());
			}
		}
	}

	public void consultarFormaPagamento() {
		try {
			if (getMovimentacaoFinanceiraItemVO().getFormaPagamento().getCodigo().intValue() != 0) {
				FormaPagamentoVO obj = getFacadeFactory().getFormaPagamentoFacade().consultarPorChavePrimaria(getMovimentacaoFinanceiraItemVO().getFormaPagamento().getCodigo(), false,
						Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				getMovimentacaoFinanceiraItemVO().setFormaPagamento(obj);
				// if (getMovimentacaoFinanceiraItemVO().getFormaPagamento().getTipo().equals("CH")) {
				// setCampoConsultaCheque("dataPrevisao");
				// setValorConsultaCheque(Uteis.getDataAno4Digitos(new Date()));
				// consultarCheque();
				// }
			}
		} catch (Exception e) {
			getMovimentacaoFinanceiraItemVO().setFormaPagamento(new FormaPagamentoVO());
		}
	}

	public String getAbrilModalCheque() {
		if (getMovimentacaoFinanceiraItemVO().getFormaPagamento().getTipo().equals("CH")) {
			return "RichFaces.$('panelConsultaCheque').show()";
		}
		return "";
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

	/**
	 * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox relativo ao atributo <code>Cheque</code>.
	 */
	public void montarListaSelectItemFormaPagamento(String prm) throws Exception {
		List<FormaPagamentoVO> formaPagamentoVOs = consultarFormaPagamentoPorNome(prm);
		getListaSelectItemFormaPagamento().clear();
		getListaSelectItemFormaPagamento().add(new SelectItem(0, ""));
		for (FormaPagamentoVO forma : formaPagamentoVOs) {
			if (forma.getTipo().equals(TipoFormaPagamento.DINHEIRO.getValor()) ||
					forma.getTipo().equals(TipoFormaPagamento.CHEQUE.getValor())) {
				getListaSelectItemFormaPagamento().add(new SelectItem(forma.getCodigo(), forma.getNome()));
			}
		}
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo <code>Cheque</code>. Buscando todos os objetos correspondentes a entidade <code>Cheque</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemFormaPagamento() {
		try {
			montarListaSelectItemFormaPagamento("");
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());;
		}
	}

	/**
	 * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>numero</code> Este atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox correspondente
	 */
	public List<FormaPagamentoVO> consultarFormaPagamentoPorNome(String numeroPrm) throws Exception {
		List<FormaPagamentoVO> lista = getFacadeFactory().getFormaPagamentoFacade().consultarPorNome(numeroPrm, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
		return lista;
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox relativo ao atributo <code>ContaCorrenteDestino</code>.
	 */
	public void montarListaSelectItemContaCorrenteDestino(String prm) throws Exception {
		setListaSelectItemContaCorrenteDestino(UtilSelectItem.getListaSelectItem(consultarContaCorrentePorNumero(prm), "codigo", "descricaoParaComboBox"));
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo <code>ContaCorrenteDestino</code>. Buscando todos os objetos correspondentes a entidade <code>ContaCorrente</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemContaCorrenteDestino() {
		try {
			montarListaSelectItemContaCorrenteDestino("");
			if (Uteis.isAtributoPreenchido(getMovimentacaoFinanceiraVO().getCodigo())) {
				if (Uteis.isAtributoPreenchido(getMovimentacaoFinanceiraVO().getContaCorrenteDestino())) {
					getListaSelectItemContaCorrenteDestino().add(new SelectItem(getMovimentacaoFinanceiraVO().getContaCorrenteDestino().getCodigo(), getMovimentacaoFinanceiraVO().getContaCorrenteDestino().getDescricaoParaComboBox()));
				}
			}
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());;
		}
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox relativo ao atributo <code>ContaCorrenteOrigem</code>.
	 */
	public void montarListaSelectItemContaCorrenteOrigem(String prm) throws Exception {
		List<ContaCorrenteVO> resultadoConsulta = new ArrayList<>();
		/*
		 * if (getPermitirRealizarMovimentacaoFinanceiroContaCaixaResponsavel()) { resultadoConsulta = getFacadeFactory().getContaCorrenteFacade().consultarPorCodigoFuncionarioResponsavel(getUsuarioLogado().getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()); } else { resultadoConsulta = consultarContaCorrentePorNumero(prm); }
		 */
		resultadoConsulta = consultarContaCorrentePorNumero(prm);
		Iterator<ContaCorrenteVO> i = resultadoConsulta.iterator();
		List<SelectItem> objs = new ArrayList<SelectItem>();
		objs.add(new SelectItem(0, ""));
		while (i.hasNext()) {
			ContaCorrenteVO obj = (ContaCorrenteVO) i.next();
			objs.add(new SelectItem(obj.getCodigo(), obj.getDescricaoParaComboBox()));
		}
		Uteis.liberarListaMemoria(resultadoConsulta);
		i = null;
		setListaSelectItemContaCorrenteOrigem(objs);
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo <code>ContaCorrenteOrigem</code>. Buscando todos os objetos correspondentes a entidade <code>ContaCorrente</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemContaCorrenteOrigem() {
		try {
			montarListaSelectItemContaCorrenteOrigem("");
			
			if (Uteis.isAtributoPreenchido(getMovimentacaoFinanceiraVO().getCodigo())) {
				if (Uteis.isAtributoPreenchido(getMovimentacaoFinanceiraVO().getContaCorrenteOrigem())) {
					getListaSelectItemContaCorrenteDestino().add(new SelectItem(getMovimentacaoFinanceiraVO().getContaCorrenteOrigem().getCodigo(), getMovimentacaoFinanceiraVO().getContaCorrenteOrigem().getDescricaoParaComboBox()));
				}
			}

		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());;
		}
	}

	/**
	 * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>numero</code> Este atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox correspondente
	 */
	public List<ContaCorrenteVO> consultarContaCorrentePorNumero(String numeroPrm) throws Exception {
		return getFacadeFactory().getContaCorrenteFacade().consultarContaCorrenteCaixaPorNumero(numeroPrm, getUnidadeEnsinoLogado().getCodigo(), verificarPermissaoMovimentacaoContaCaixaParaContaCorrente(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
	}

	public boolean verificarPermissaoMovimentacaoContaCaixaParaContaCorrente() {
		try {
			if (getMovimentacaoFinanceiraVO().isNovoObj()) {
				ControleAcesso.incluir("MovimentacaoContaCaixaContaCorrente", getUsuarioLogado());
				return true;
			} else {
				return true;
			}
		} catch (Exception e) {
			return false;
		}
	}

	public void consultarContaDestinoPorCodigo() throws Exception {		
		if (!getMovimentacaoFinanceiraVO().getContaCorrenteDestino().getCodigo().equals(0)) {
			getMovimentacaoFinanceiraVO().setContaCorrenteDestino(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(getMovimentacaoFinanceiraVO().getContaCorrenteDestino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado()));
			
			montarListaSelectItemUnidadeEnsino(getMovimentacaoFinanceiraVO().getContaCorrenteDestino());			
			
		}else {
			getMovimentacaoFinanceiraVO().setUnidadeEnsinoVO(null);
		}
	}

	/**
	 * Método responsável por inicializar a lista de valores (<code>SelectItem</code>) para todos os ComboBox's.
	 */
	public void inicializarListasSelectItemTodosComboBox() {
		montarListaSelectItemContaCorrenteOrigem();
		montarListaSelectItemContaCorrenteDestino();
		montarListaSelectItemFormaPagamento();
		montarListaSelectItemUnidadeEnsino(getMovimentacaoFinanceiraVO().getContaCorrenteDestino());
	}

	public String getMascaraConsulta() {

		if (getControleConsulta().getCampoConsulta().equals("data")) {
			return "return mascara(this.form,'form:valorConsulta','99/99/9999',event);";
		}

		return "";
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nomeUsuario", "Responsável"));
		itens.add(new SelectItem("numeroContaCorrenteOrigem", "Conta Corrente Origem"));
		itens.add(new SelectItem("numeroContaCorrenteDestino", "Conta Corrente Destino"));
		itens.add(new SelectItem("data", "Data"));
		itens.add(new SelectItem("situacao", "Situação"));
		itens.add(new SelectItem("codigo", "Código"));
		itens.add(new SelectItem("cheque", "Cheque"));
		return itens;
	}

	public boolean isCampoData() {
		if (getControleConsulta().getCampoConsulta().equals("data")) {
			return true;
		}
		return false;
	}

	public boolean isCampoSituacao() {
		if (getControleConsulta().getCampoConsulta().equals("situacao")) {
			return true;
		}
		return false;
	}

	public boolean isCampoDiferenteDataSituacao() {
		if (getControleConsulta().getCampoConsulta().equals("situacao") || getControleConsulta().getCampoConsulta().equals("data") || getControleConsulta().getCampoConsulta().equals("cheque")) {
			return false;
		}
		return true;
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
	 */
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		// setPaginaAtualDeTodas("0/0");
		setListaConsulta(new ArrayList<MovimentacaoFinanceiraVO>(0));
		// definirVisibilidadeLinksNavegacao(0, 0);
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("movimentacaoFinanceiraCons.xhtml");
	}

	public ChequeVO getChequeVO() {
		if (chequeVO == null) {
			chequeVO = new ChequeVO();
		}
		return chequeVO;
	}

	public void setChequeVO(ChequeVO chequeVO) {
		this.chequeVO = chequeVO;
	}

	public List<SelectItem> getListaSelectItemCheque() {
		if (listaSelectItemCheque == null) {
			listaSelectItemCheque = new ArrayList<SelectItem>(0);
		}
		return (listaSelectItemCheque);
	}

	public void setListaSelectItemCheque(List<SelectItem> listaSelectItemCheque) {
		this.listaSelectItemCheque = listaSelectItemCheque;
	}

	public MovimentacaoFinanceiraItemVO getMovimentacaoFinanceiraItemVO() {
		if (movimentacaoFinanceiraItemVO == null) {
			movimentacaoFinanceiraItemVO = new MovimentacaoFinanceiraItemVO();
		}
		return movimentacaoFinanceiraItemVO;
	}

	public void setMovimentacaoFinanceiraItemVO(MovimentacaoFinanceiraItemVO movimentacaoFinanceiraItemVO) {
		this.movimentacaoFinanceiraItemVO = movimentacaoFinanceiraItemVO;
	}

	public List<SelectItem> getListaSelectItemContaCorrenteDestino() {
		if (listaSelectItemContaCorrenteDestino == null) {
			listaSelectItemContaCorrenteDestino = new ArrayList<SelectItem>(0);
		}
		return (listaSelectItemContaCorrenteDestino);
	}

	public void setListaSelectItemContaCorrenteDestino(List<SelectItem> listaSelectItemContaCorrenteDestino) {
		this.listaSelectItemContaCorrenteDestino = listaSelectItemContaCorrenteDestino;
	}

	public List<SelectItem> getListaSelectItemContaCorrenteOrigem() {
		if (listaSelectItemContaCorrenteOrigem == null) {
			listaSelectItemContaCorrenteOrigem = new ArrayList<SelectItem>(0);
		}
		return (listaSelectItemContaCorrenteOrigem);
	}

	public void setListaSelectItemContaCorrenteOrigem(List<SelectItem> listaSelectItemContaCorrenteOrigem) {
		this.listaSelectItemContaCorrenteOrigem = listaSelectItemContaCorrenteOrigem;
	}

	public MovimentacaoFinanceiraVO getMovimentacaoFinanceiraVO() {
		if (movimentacaoFinanceiraVO == null) {
			movimentacaoFinanceiraVO = new MovimentacaoFinanceiraVO();
		}
		return movimentacaoFinanceiraVO;
	}

	public void setMovimentacaoFinanceiraVO(MovimentacaoFinanceiraVO movimentacaoFinanceiraVO) {
		this.movimentacaoFinanceiraVO = movimentacaoFinanceiraVO;
	}

	public String getCampoConsultaCheque() {
		if (campoConsultaCheque == null) {
			campoConsultaCheque = "";
		}
		return campoConsultaCheque;
	}

	public void setCampoConsultaCheque(String campoConsultaCheque) {
		this.campoConsultaCheque = campoConsultaCheque;
	}

	public List<ChequeVO> getListaConsultaCheque() {
		if (listaConsultaCheque == null) {
			listaConsultaCheque = new ArrayList<>(0);
		}
		return listaConsultaCheque;
	}

	public void setListaConsultaCheque(List<ChequeVO> listaConsultaCheque) {
		this.listaConsultaCheque = listaConsultaCheque;
	}

	public String getValorConsultaCheque() {
		if (valorConsultaCheque == null) {
			valorConsultaCheque = "";
		}
		return valorConsultaCheque;
	}

	public void setValorConsultaCheque(String valorConsultaCheque) {
		this.valorConsultaCheque = valorConsultaCheque;
	}

	public List<SelectItem> getListaSelectItemFormaPagamento() {
		if (listaSelectItemFormaPagamento == null) {
			listaSelectItemFormaPagamento = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemFormaPagamento;
	}

	public void setListaSelectItemFormaPagamento(List<SelectItem> listaSelectItemFormaPagamento) {
		this.listaSelectItemFormaPagamento = listaSelectItemFormaPagamento;
	}

	@Override
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
		movimentacaoFinanceiraVO = null;
		Uteis.liberarListaMemoria(listaSelectItemContaCorrenteOrigem);
		Uteis.liberarListaMemoria(listaSelectItemContaCorrenteDestino);
		Uteis.liberarListaMemoria(listaSelectItemFormaPagamento);
		movimentacaoFinanceiraItemVO = null;
		chequeVO = null;
		Uteis.liberarListaMemoria(listaSelectItemCheque);
		valorConsultaCheque = null;
		campoConsultaCheque = null;
		Uteis.liberarListaMemoria(listaConsultaCheque);
	}

	public Date getDataInicioConsultar() {
		if (dataInicioConsultar == null) {
			dataInicioConsultar = new Date();
		}
		return dataInicioConsultar;
	}

	public void setDataInicioConsultar(Date dataInicioConsultar) {
		this.dataInicioConsultar = dataInicioConsultar;
	}

	public boolean getApresentarDataConsulta() throws Exception {
		if (getControleConsulta().getCampoConsulta().equals("data")) {
			return true;
		} else {
			return false;
		}
	}

	public boolean getIsMovimentacaoRecusada() {
		return (getMovimentacaoFinanceiraVO().getSituacao().equals("RE"));
	}

	public Boolean getPossibilidadeEstornar() {
		return getLoginControle().getPermissaoAcessoMenuVO().getEstornarMovimentacaoFinanceira()
				&& !getMovimentacaoFinanceiraVO().getIsSituacaoPendente()
				&& ((!getMovimentacaoFinanceiraVO().getContaCorrenteOrigem().getContaCaixa() && !getMovimentacaoFinanceiraVO().getContaCorrenteDestino().getContaCaixa()) 
						|| (!Uteis.getData(getMovimentacaoFinanceiraVO().getData()).equals(Uteis.getData(new Date())) && getPermiteRealizarMovimentacaoContasCaixaFechada())
						|| (Uteis.getData(getMovimentacaoFinanceiraVO().getData()).equals(Uteis.getData(new Date()))));
	}
	
	public boolean isApresentarBotaoDesconsiderandoContabilidadeConciliacao() {
		return isPermitirDesconsiderandoContabilidadeConciliacao() &&  !getMovimentacaoFinanceiraVO().isNovoObj() && !getMovimentacaoFinanceiraVO().isDesconsiderandoContabilidadeConciliacao();
	}

	public ConciliacaoContaCorrenteVO getConciliacaoContaCorrenteVO() {
		if (conciliacaoContaCorrenteVO == null) {
			conciliacaoContaCorrenteVO = new ConciliacaoContaCorrenteVO();
		}
		return conciliacaoContaCorrenteVO;
	}

	public void setConciliacaoContaCorrenteVO(ConciliacaoContaCorrenteVO conciliacaoContaCorrenteVO) {
		this.conciliacaoContaCorrenteVO = conciliacaoContaCorrenteVO;
	}

	public String realizarNavegacaoParaConciliacaoBancaria() {
		context().getExternalContext().getSessionMap().put("conciliacaoBancaria", getConciliacaoContaCorrenteVO());
		removerControleMemoriaFlash("ConciliacaoContaCorrenteControle");
		removerControleMemoriaTela("ConciliacaoContaCorrenteControle");
		return Uteis.getCaminhoRedirecionamentoNavegacao("conciliacaoContaCorrenteForm.xhtml");
	}

	public void persistirLancamentoContabil() {
		try {
			getFacadeFactory().getMovimentacaoFinanceiraFacade().persistirLancamentoContabilVO(getMovimentacaoFinanceiraVO(), false, getUsuarioLogado());
			setMensagemID("msg_dados_editar");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void gerarLancamentoContabilPadrao() {
		try {
			ControleAcesso.incluir(PerfilAcessoPermissaoFinanceiroEnum.PERMITIR_ALTERAR_LANCAMENTO_CONTABIL_MOVIMENTACAO_FINANCEIRA.getValor(), getUsuarioLogado());
			List<LancamentoContabilVO> listaTemp = new ArrayList<>();
			listaTemp.addAll(getMovimentacaoFinanceiraVO().getListaLancamentoContabeisCredito());
			listaTemp.addAll(getMovimentacaoFinanceiraVO().getListaLancamentoContabeisDebito());
			for (LancamentoContabilVO lc : listaTemp) {
				getFacadeFactory().getLancamentoContabilFacade().validarDados(lc);
			}
			getMovimentacaoFinanceiraVO().getListaLancamentoContabeisCredito().clear();
			getMovimentacaoFinanceiraVO().getListaLancamentoContabeisDebito().clear();
			getFacadeFactory().getLancamentoContabilFacade().gerarLancamentoContabilPorMovimentacaoFinanceira(getMovimentacaoFinanceiraVO(), true, getUsuarioLogado());
			setMensagemID("msg_dados_editar");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void novoLancamentoContabilCredito() {
		try {
			setLancamentoContabilVO(new LancamentoContabilVO());
			getLancamentoContabilVO().setTipoPlanoConta(TipoPlanoContaEnum.CREDITO);
			getLancamentoContabilVO().setContaCorrenteVO(getMovimentacaoFinanceiraVO().getContaCorrenteDestino());
			setModalAptoParaSerFechado("");
			setMensagemID("msg_dados_editar");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void novoLancamentoContabilDebito() {
		try {
			setLancamentoContabilVO(new LancamentoContabilVO());
			getLancamentoContabilVO().setTipoPlanoConta(TipoPlanoContaEnum.DEBITO);
			getLancamentoContabilVO().setContaCorrenteVO(getMovimentacaoFinanceiraVO().getContaCorrenteOrigem());
			setModalAptoParaSerFechado("");
			setMensagemID("msg_dados_editar");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void addLancamentoContabilVO() {
		try {
			getFacadeFactory().getMovimentacaoFinanceiraFacade().addLancamentoContabilVO(getMovimentacaoFinanceiraVO(), getLancamentoContabilVO(), getUsuarioLogado());
			setLancamentoContabilVO(new LancamentoContabilVO());
			setModalAptoParaSerFechado("RichFaces.$('modalLancamentoContabil').hide();");
			setMensagemID("msg_dados_editar");
		} catch (Exception e) {
			setModalAptoParaSerFechado("");
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void editarLancamentoContabilVO() {
		try {
			LancamentoContabilVO obj = (LancamentoContabilVO) context().getExternalContext().getRequestMap().get("lancamentoContabilItens");
			setLancamentoContabilVO(obj);
			setModalAptoParaSerFechado("");
			setMensagemID("msg_dados_editar");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void removerLancamentoContabilVO() {
		try {
			LancamentoContabilVO obj = (LancamentoContabilVO) context().getExternalContext().getRequestMap().get("lancamentoContabilItens");
			getFacadeFactory().getMovimentacaoFinanceiraFacade().removeLancamentoContabilVO(getMovimentacaoFinanceiraVO(), obj, getUsuarioLogado());
			setMensagemID("msg_dados_editar");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void recalcularRaterioLancamentoContabilVO() {
		try {
			getLancamentoContabilVO().recalcularRaterioLancamentoContabil();
			setMensagemID("msg_dados_editar");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarPlanoContaDebito() throws Exception {
		try {
			PlanoContaVO obj = (PlanoContaVO) context().getExternalContext().getRequestMap().get("planoContaItens");
			getLancamentoContabilVO().setPlanoContaVO(obj);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarPlanoContaDebito() {
		try {
			setListaConsultaPlanoConta(getFacadeFactory().getPlanoContaFacade().consultar(getLancamentoContabilVO().getUnidadeEnsinoVO().getCodigo(), getCampoConsultaPlanoConta(), getValorConsultaPlanoConta(), false, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaPlanoConta(new ArrayList<>());
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarPlanoContaCredito() throws Exception {
		try {
			PlanoContaVO obj = (PlanoContaVO) context().getExternalContext().getRequestMap().get("planoContaItens");
			getLancamentoContabilVO().setPlanoContaVO(obj);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarPlanoContaCredito() {
		try {
			setListaConsultaPlanoConta(getFacadeFactory().getPlanoContaFacade().consultar(getLancamentoContabilVO().getUnidadeEnsinoVO().getCodigo(), getCampoConsultaPlanoConta(), getValorConsultaPlanoConta(), false, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaPlanoConta(new ArrayList<>());
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparCamposPorPlanoConta() {
		try {
			setListaConsultaPlanoConta(new ArrayList<>());
			setValorConsultaPlanoConta("");
			setCampoConsultaPlanoConta("");
			getLancamentoContabilVO().getPlanoContaVO().setIdentificadorPlanoConta("");
			getLancamentoContabilVO().getPlanoContaVO().setDescricao("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaPlanoConta(new ArrayList<>());
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaComboPlanoConta() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("codigo", "Código"));
		itens.add(new SelectItem("identificadorPlanoConta", "Identificador Plano Conta"));
		itens.add(new SelectItem("descricao", "Descrição"));
		return itens;
	}

	public List<SelectItem> getListaSelectItemTipoPlanoConta() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem(TipoPlanoContaEnum.CREDITO, TipoPlanoContaEnum.CREDITO.getValorApresentar()));
		itens.add(new SelectItem(TipoPlanoContaEnum.DEBITO, TipoPlanoContaEnum.DEBITO.getValorApresentar()));
		return itens;
	}

	public LancamentoContabilVO getLancamentoContabilVO() {
		if (lancamentoContabilVO == null) {
			lancamentoContabilVO = new LancamentoContabilVO();
		}
		return lancamentoContabilVO;
	}

	public void setLancamentoContabilVO(LancamentoContabilVO lancamentoContabilVO) {
		this.lancamentoContabilVO = lancamentoContabilVO;
	}

	public String getValorConsultaPlanoConta() {
		if (valorConsultaPlanoConta == null) {
			valorConsultaPlanoConta = "";
		}
		return valorConsultaPlanoConta;
	}

	public void setValorConsultaPlanoConta(String valorConsultaPlanoConta) {
		this.valorConsultaPlanoConta = valorConsultaPlanoConta;
	}

	public String getCampoConsultaPlanoConta() {
		if (campoConsultaPlanoConta == null) {
			campoConsultaPlanoConta = "";
		}
		return campoConsultaPlanoConta;
	}

	public void setCampoConsultaPlanoConta(String campoConsultaPlanoConta) {
		this.campoConsultaPlanoConta = campoConsultaPlanoConta;
	}

	public List<PlanoContaVO> getListaConsultaPlanoConta() {
		if (listaConsultaPlanoConta == null) {
			listaConsultaPlanoConta = new ArrayList<PlanoContaVO>();
		}
		return listaConsultaPlanoConta;
	}

	public void setListaConsultaPlanoConta(List<PlanoContaVO> listaConsultaPlanoConta) {
		this.listaConsultaPlanoConta = listaConsultaPlanoConta;
	}

	public String getModalAptoParaSerFechado() {
		if (modalAptoParaSerFechado == null) {
			modalAptoParaSerFechado = "";
		}
		return modalAptoParaSerFechado;
	}

	public void setModalAptoParaSerFechado(String modalAptoParaSerFechado) {
		this.modalAptoParaSerFechado = modalAptoParaSerFechado;
	}

	public void verificarPermissaoLancamentoContabilMovimentacaoFinanceira() {
		try {
			if (!getMovimentacaoFinanceiraVO().isLancamentoContabil()) {
				if(getMovimentacaoFinanceiraVO().getUnidadeEnsinoVO().getCodigo() == null || getMovimentacaoFinanceiraVO().getUnidadeEnsinoVO().getCodigo().equals(0)) {
					getMovimentacaoFinanceiraVO().setLancamentoContabil(getFacadeFactory().getConfiguracaoContabilFacade().consultaSeExisteConfiguracaoContabilPorCodigoUnidadeEnsino(getMovimentacaoFinanceiraVO().getUnidadeEnsinoVO().getCodigo(), getUsuarioLogado()));
				}
			}
		} catch (Exception e) {
			getMovimentacaoFinanceiraVO().setLancamentoContabil(false);
		}
	}

	@PostConstruct
	public void realizarCarregamentoMovimentacaoFinanceiraVindoTelaExtratoContaCorrente() {
		try {
			if (context().getExternalContext().getSessionMap().get("movimentacaoFinanceiraExtratoContaCorrente") != null && context().getExternalContext().getSessionMap().get("movimentacaoFinanceiraExtratoContaCorrente") instanceof Integer && Uteis.isAtributoPreenchido(((Integer) context().getExternalContext().getSessionMap().get("movimentacaoFinanceiraExtratoContaCorrente")))) {
				Integer codigo = (Integer) context().getExternalContext().getSessionMap().get("movimentacaoFinanceiraExtratoContaCorrente");
				if (Uteis.isAtributoPreenchido(codigo)) {
					MovimentacaoFinanceiraVO obj = new MovimentacaoFinanceiraVO();
					obj.setCodigo(codigo);
					carregarDadosParaEditarMovimentacaoFinanceira(obj);
				}
				setMensagemID("msg_dados_editar", Uteis.ALERTA);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			context().getExternalContext().getSessionMap().remove("movimentacaoFinanceiraExtratoContaCorrente");
		}
	}

	public Boolean getPermitirRealizarMovimentacaoFinanceiroContaCaixaResponsavel() {
		if (PermitirRealizarMovimentacaoFinanceiroContaCaixaResponsavel == null) {
			PermitirRealizarMovimentacaoFinanceiroContaCaixaResponsavel = false;
		}
		return PermitirRealizarMovimentacaoFinanceiroContaCaixaResponsavel;
	}

	public void setPermitirRealizarMovimentacaoFinanceiroContaCaixaResponsavel(Boolean permitirRealizarMovimentacaoFinanceiroContaCaixaResponsavel) {
		PermitirRealizarMovimentacaoFinanceiroContaCaixaResponsavel = permitirRealizarMovimentacaoFinanceiroContaCaixaResponsavel;
	}

	public void verificarPermitirRealizarMovimentacaoFinanceiroContaCaixaResponsavel() throws Exception {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("PermitirRealizarMovimentacaoFinanceiroContaCaixaResponsavel", getUsuarioLogado());
			setPermitirRealizarMovimentacaoFinanceiroContaCaixaResponsavel(Boolean.TRUE);
		} catch (Exception e) {
			setPermitirRealizarMovimentacaoFinanceiroContaCaixaResponsavel(Boolean.FALSE);
		}
	}

	public Boolean getApresentarBotaoLiberarBloqueio() {
		return this.getMovimentacaoFinanceiraVO().getApresentarBotaoLiberarBloqueioFechamentoMes();
	}

	public void liberarRegistroCompetenciaFechada() {
		try {
			this.getMovimentacaoFinanceiraVO().setBloqueioPorFechamentoMesLiberado(Boolean.TRUE);
			FechamentoMesHistoricoModificacaoVO historico = getFacadeFactory().getFechamentoMesHistoricoModificacaoFacade().gerarNovoHistoricoModificacao(this.getMovimentacaoFinanceiraVO().getFechamentoMesVOBloqueio(), getUsuarioLogado(), TipoOrigemHistoricoBloqueioEnum.MOVIMENTACAOFINANCEIRA, this.getMovimentacaoFinanceiraVO().getDescricaoBloqueio(), this.getMovimentacaoFinanceiraVO().toString());
			getFacadeFactory().getFechamentoMesHistoricoModificacaoFacade().incluir(historico, getUsuarioLogado());
			setMensagemID("msg_registro_liberado_mes");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			this.getMovimentacaoFinanceiraVO().setBloqueioPorFechamentoMesLiberado(Boolean.FALSE);
		}
	}

	public void verificarPermissaoLiberarBloqueioCompetencia() {
		try {
			UsuarioVO usuarioVerif = ControleAcesso.verificarLoginUsuario(this.getUsernameLiberacaoBloqueioPorFechamentoMes(), this.getSenhaLiberacaoBloqueioPorFechamentoMes(), true, Uteis.NIVELMONTARDADOS_TODOS);
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("FuncionarioMes_liberarBloqueioIncluirAlterarMovimentacaoFinanceira", usuarioVerif);
			liberarRegistroCompetenciaFechada();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	

	
	public void verificarPermitirMovimentacaoFinanceiroDesconsiderandoContabilidadeConciliacao() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoFinanceiroEnum.PERMITIR_REALIZAR_MOVIMENTACAO_FINANCEIRO_DESCONSIDERANDO_CONTABILIDADE_CONCILIACAO, getUsuarioLogadoClone());
			setPermitirDesconsiderandoContabilidadeConciliacao(true);
		} catch (Exception e) {
			setPermitirDesconsiderandoContabilidadeConciliacao(false);
		}
	}

	public boolean isPermitirDesconsiderandoContabilidadeConciliacao() {
		return permitirDesconsiderandoContabilidadeConciliacao;
	}

	public void setPermitirDesconsiderandoContabilidadeConciliacao(boolean permitirDesconsiderandoContabilidadeConciliacao) {
		this.permitirDesconsiderandoContabilidadeConciliacao = permitirDesconsiderandoContabilidadeConciliacao;
	}

	public void alterarDataReferencia() {
		getMovimentacaoFinanceiraVO().reiniciarControleBloqueioCompetencia();
	}

	public void carregarDadosContaCorrenteOrigem() {
		try {
			if (this.getMovimentacaoFinanceiraVO().getContaCorrenteOrigem().getCodigo().equals(0)) {
				return;
			}
			ContaCorrenteVO contaOrigem = getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(this.getMovimentacaoFinanceiraVO().getContaCorrenteOrigem().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado());
			this.getMovimentacaoFinanceiraVO().setContaCorrenteOrigem(contaOrigem);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	
	public Boolean getConsultaDataScroller() {
		if (consultaDataScroller == null) {
			consultaDataScroller = false;
		}
		return consultaDataScroller;
	}

	public void setConsultaDataScroller(Boolean consultaDataScroller) {
		this.consultaDataScroller = consultaDataScroller;
	}
	public void scrollerListener(DataScrollEvent DataScrollEvent) throws Exception {
		getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
		getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
		setConsultaDataScroller(true);
		consultar();
	}
	
    public List<SelectItem> getListaSelectItemUnidadeEnsino() {
        if (listaSelectItemUnidadeEnsino == null) {
            listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>(0);
        }
        return listaSelectItemUnidadeEnsino;
    }

    public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
        this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
    }
    
    public void montarListaSelectItemUnidadeEnsino(ContaCorrenteVO contaCorrenteDestinoVO) {
    	//getMovimentacaoFinanceiraVO().setUnidadeEnsinoVO(null);
    	getListaSelectItemUnidadeEnsino().clear();
    	getListaSelectItemUnidadeEnsino().add(new SelectItem(0, ""));
    	if (!contaCorrenteDestinoVO.getUnidadeEnsinoContaCorrenteVOs().isEmpty()) {
	        contaCorrenteDestinoVO.getUnidadeEnsinoContaCorrenteVOs().stream().forEach(ueContaCorrente -> {
	        	getListaSelectItemUnidadeEnsino().add(new SelectItem(ueContaCorrente.getUnidadeEnsino().getCodigo(), ueContaCorrente.getUnidadeEnsino().getNome()));
				if (ueContaCorrente.getUsarPorDefaultMovimentacaoFinanceira() == Boolean.TRUE && !getMovimentacaoFinanceiraVO().getEdicao()) {
					try {
						getMovimentacaoFinanceiraVO().setUnidadeEnsinoVO((UnidadeEnsinoVO)ueContaCorrente.getUnidadeEnsino().clone());
					} catch (CloneNotSupportedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
	        
			if(!getMovimentacaoFinanceiraVO().getEdicao() && (getMovimentacaoFinanceiraVO().getUnidadeEnsinoVO().getCodigo() == null || getMovimentacaoFinanceiraVO().getUnidadeEnsinoVO().getCodigo().equals(0))) {				
				SelectItem selectItem = (SelectItem) getListaSelectItemUnidadeEnsino().get(1);
				getMovimentacaoFinanceiraVO().getUnidadeEnsinoVO().setCodigo((Integer) selectItem.getValue());
			}
		
    	}

    }

	public String getModalLiberarContaCaixaFechada() {
		if (modalLiberarContaCaixaFechada == null) {
			modalLiberarContaCaixaFechada = "";
		}
		return modalLiberarContaCaixaFechada;
	}

	public void setModalLiberarContaCaixaFechada(String modalLiberarContaCaixaFechada) {
		this.modalLiberarContaCaixaFechada = modalLiberarContaCaixaFechada;
	}

	public String getUsernameLiberarOperacaoFuncionalidade() {
		if (usernameLiberarOperacaoFuncionalidade == null) {
			usernameLiberarOperacaoFuncionalidade = "";
		}
		return usernameLiberarOperacaoFuncionalidade;
	}

	public void setUsernameLiberarOperacaoFuncionalidade(String usernameLiberarOperacaoFuncionalidade) {
		this.usernameLiberarOperacaoFuncionalidade = usernameLiberarOperacaoFuncionalidade;
	}

	public String getSenhaLiberarOperacaoFuncionalidade() {
		if (senhaLiberarOperacaoFuncionalidade == null) {
			senhaLiberarOperacaoFuncionalidade = "";
		}
		return senhaLiberarOperacaoFuncionalidade;
	}

	public void setSenhaLiberarOperacaoFuncionalidade(String senhaLiberarOperacaoFuncionalidade) {
		this.senhaLiberarOperacaoFuncionalidade = senhaLiberarOperacaoFuncionalidade;
	}
	
	public String getNomeSacadoConsultaPorCheque() {
		if (nomeSacadoConsultaPorCheque == null) {
			nomeSacadoConsultaPorCheque = "";
		}
		return nomeSacadoConsultaPorCheque;
	}

	public void setNomeSacadoConsultaPorCheque(String nomeSacadoConsultaPorCheque) {
		this.nomeSacadoConsultaPorCheque = nomeSacadoConsultaPorCheque;
	}

	public String getNumeroConsultaPorCheque() {
		if (numeroConsultaPorCheque == null) {
			numeroConsultaPorCheque = "";
		}
		return numeroConsultaPorCheque;
	}

	public void setNumeroConsultaPorCheque(String numeroConsultaPorCheque) {
		this.numeroConsultaPorCheque = numeroConsultaPorCheque;
	}

	public boolean isConsultaPorCheque() {
		return getControleConsulta().getCampoConsulta().equals("cheque");
	}

	public Boolean getPermiteRealizarMovimentacaoContasCaixaFechada() {
		if (permiteRealizarMovimentacaoContasCaixaFechada == null) {
			permiteRealizarMovimentacaoContasCaixaFechada = realizarMovimentacaoContasCaixaFechada();
		}
		return permiteRealizarMovimentacaoContasCaixaFechada;
	}

	public void setPermiteRealizarMovimentacaoContasCaixaFechada(Boolean permiteRealizarMovimentacaoContasCaixaFechada) {
		this.permiteRealizarMovimentacaoContasCaixaFechada = permiteRealizarMovimentacaoContasCaixaFechada;
	}
}
