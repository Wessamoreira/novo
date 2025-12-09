package controle.financeiro;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas contaPagarForm.jsp contaPagarCons.jsp) com as funcionalidades da classe
 * <code>ContaPagar</code>. Implemtação da camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see ContaPagar
 * @see ContaPagarVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import controle.compras.FornecedorControle;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoFinanceiroEnum;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.contabil.FechamentoMesHistoricoModificacaoVO;
import negocio.comuns.contabil.LancamentoContabilCentroNegocioVO;
import negocio.comuns.contabil.LancamentoContabilVO;
import negocio.comuns.contabil.PlanoContaVO;
import negocio.comuns.contabil.enumeradores.TipoOrigemHistoricoBloqueioEnum;
import negocio.comuns.contabil.enumeradores.TipoPlanoContaEnum;
import negocio.comuns.ead.enumeradores.SituacaoEnum;
import negocio.comuns.financeiro.BancoVO;
import negocio.comuns.financeiro.CategoriaDespesaVO;
import negocio.comuns.financeiro.CentroResultadoOrigemVO;
import negocio.comuns.financeiro.CentroResultadoVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ContaPagarControleRemessaContaPagarVO;
import negocio.comuns.financeiro.ContaPagarPagamentoVO;
import negocio.comuns.financeiro.ContaPagarVO;
import negocio.comuns.financeiro.OperadoraCartaoVO;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.financeiro.enumerador.BancoEnum;
import negocio.comuns.financeiro.enumerador.ModalidadeTransferenciaBancariaEnum;
import negocio.comuns.financeiro.enumerador.TipoCentroNegocioEnum;
import negocio.comuns.financeiro.enumerador.TipoIdentificacaoChavePixEnum;
import negocio.comuns.financeiro.enumerador.TipoLancamentoContaPagarEnum;
import negocio.comuns.financeiro.enumerador.TipoMovimentacaoCentroResultadoOrigemEnum;
import negocio.comuns.financeiro.enumerador.TipoNivelCentroResultadoEnum;
import negocio.comuns.financeiro.enumerador.TipoServicoContaPagarEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.boleto.GeradorDeLinhaDigitavelOuCodigoBarra;
import negocio.comuns.utilitarias.dominios.Bancos;
import negocio.comuns.utilitarias.dominios.OrigemContaPagar;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import relatorio.controle.financeiro.AutorizacaoPagamentoRelControle;

@Controller("ContaPagarControle")
@Scope("viewScope")
@Lazy
public class ContaPagarControle extends SuperControle implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2455844060124386063L;
	private ContaPagarVO contaPagarVO;
	private CentroResultadoOrigemVO centroResultadoOrigemVO;
	
	private List<SelectItem> listaSelectItemContaCorrente;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private List<SelectItem> listaSelectItemUnidadeEnsinoCategoriaDespesa;
	private List<SelectItem> listaSelectItemTipoNivelCentroResultadoEnum;

	private String valorConsultaAluno;
	private String campoConsultaAluno;

	private List<CategoriaDespesaVO> listaConsultaCentroDespesa;
	private String valorConsultaCentroDespesa;
	private String campoConsultaCentroDespesa;

	private List<FornecedorVO> listaConsultaFornecedor;
	private String valorConsultaFornecedor;
	private String campoConsultaFornecedor;

	private List<ParceiroVO> listaConsultaParceiro;
	private String valorConsultaParceiro;
	private String campoConsultaParceiro;

	private String valorConsultaFuncionario;
	private String campoConsultaFuncionario;
	private List<FuncionarioVO> listaConsultaFuncionario;

	private String valorConsultaFuncionarioCentroCusto;
	private String campoConsultaFuncionarioCentroCusto;
	private List<FuncionarioCargoVO> listaConsultaFuncionarioCentroCusto;

	private String campoConsultaTurma;
	private String valorConsultaTurma;
	private List<TurmaVO> listaConsultaTurma;

	private String campoConsultaCurso;
	private String valorConsultaCurso;
	private List<CursoVO> listaConsultaCurso;

	private String campoConsultaCursoTurno;
	private String valorConsultaCursoTurno;
	private List<UnidadeEnsinoCursoVO> listaConsultaCursoTurno;

	private String campoConsultaOperadoraCartao;
	private String valorConsultaOperadoraCartao;
	private List<OperadoraCartaoVO> listaConsultaOperadoraCartao;

	private String campoConsultaDepartamento;
	private String valorConsultaDepartamento;
	private List<DepartamentoVO> listaConsultaDepartamento;

	private String valorConsultaPlanoConta;
	private String campoConsultaPlanoConta;
	private List<PlanoContaVO> listaConsultaPlanoConta;

	private String valorConsultaSituacaoFinanceiraDaConta;
	private Double totalPagar;
	private Double totalPago;
	private Double totalNegociado;
	private Boolean filtroFavorecido;
	private Boolean mostrarGeracaoGrafico;
	private Boolean consultaPainelGestorFinanceiro;
	private int valorConsultaUnidadeEnsino;
	private String situacaoConsultaPainelGestorFinanceiro;
	private Boolean consultaDataScroller;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private Boolean filtrarDataFactorGerador;
	private ContaPagarPagamentoVO contaPagarPagamentoVO;
	private LancamentoContabilVO lancamentoContabilVO;
	private LancamentoContabilCentroNegocioVO lancamentoContabilCentroNegocioVO;
	private TipoCentroNegocioEnum tipoCategoriaDespesaRateioEnum;
	private String modalAptoParaSerFechado;
	private List<SelectItem> listaSelectItemBanco;
	private List<SelectItem> comboTipoLancamentoContaPagar;
	private List<SelectItem> comboTipoServicoContaPagar;
	private List<SelectItem> comboFormaPagamento;
	private List<SelectItem> listaSelectItemBancoFavorecido;
	private DataModelo centroResultadoDataModelo;
	private boolean centroResultadoAdministrativo = false;
	private List<SelectItem> listaSelectItemBancoRemessaBancaria;
	private boolean existeContaPagarControleRemessa = false;
	private ContaPagarControleRemessaContaPagarVO contaPagarControleRemessaContaPagarVO;
	private boolean bancoDePagamanetoSicoob = false;
    private String valorConsultafaixaValorMenor;
    private String valorConsultafaixaValorMaior;
    private List<String> listConsulta;
    private DataModelo dataModeloAluno;
	private List<SelectItem> comboTipoIdentificacaoChavePixEnum;
    
	public ContaPagarControle() throws Exception {
		montarListaSelectItemUnidadeEnsino();
		montarListaSelectItemFormaPagamento();
		montarListaSelectItemLayoutsBancos();
		montarListaSelectItemBancosRemessaBancaria();
		setControleConsultaOtimizado(new DataModelo());
		getControleConsultaOtimizado().setCampoConsulta(ContaPagarVO.enumCampoConsultaContaPagar.FAVORECIDO.name());
		getControleConsultaOtimizado().setDataIni(Uteis.getDataPrimeiroDiaMes(new Date()));
		getControleConsultaOtimizado().setDataFim(Uteis.getDataUltimoDiaMes(new Date()));
		setTotalPagar(0.0);
		setTotalPago(0.0);
		setMensagemID("msg_entre_prmconsulta");
		context().getExternalContext().getSessionMap().put("graficoContaPagarForm", "");
	}

	@PostConstruct
	public String editarContaPagarVindoTelaMapaPendenciaFinanceira() throws Exception {
		ContaPagarVO obj = (ContaPagarVO) context().getExternalContext().getSessionMap().get("contaPagarChequeVO");
		if (obj != null && !obj.getCodigo().equals(0)) {
			obj = montarDadosParaEdicaoContaPagarVO(obj);
			obj.setNovoObj(Boolean.FALSE);
			setContaPagarVO(obj);
			setLancamentoContabilVO(new LancamentoContabilVO());
			setLancamentoContabilCentroNegocioVO(new LancamentoContabilCentroNegocioVO());
			inicializarListasSelectItemTodosComboBox();
			montarListaSelectItemUnidadeEnsino();
			// montarGraficoLinhaContaPagar();
			context().getExternalContext().getSessionMap().remove("contaPagarChequeVO");
			registrarAtividadeUsuario(getUsuarioLogado(), "ContaPagarControle", "Finalizando Editar Conta Pagar", "Editando");
			setMensagemID("msg_dados_editar");
			return Uteis.getCaminhoRedirecionamentoNavegacao("contaPagarForm.xhtml");
		}
		return "";
	}

	@PostConstruct
	public String editarContaPagarVindoTelaExterna() throws Exception {
		ContaPagarVO obj = (ContaPagarVO) context().getExternalContext().getSessionMap().get("contaPagarLancamentoContabil");
		if (obj != null && !obj.getCodigo().equals(0)) {
			preencherDadosContaPagarVindoTelaExterna(obj);
			return "";	
		}
		obj = (ContaPagarVO) context().getExternalContext().getSessionMap().get("contaPagarGestaoContaPagar");
		if (obj != null && !obj.getCodigo().equals(0)) {
			preencherDadosContaPagarVindoTelaExterna(obj);
			return "";	
		}
		return "";
	}

	private void preencherDadosContaPagarVindoTelaExterna(ContaPagarVO obj) throws Exception {
		obj = montarDadosParaEdicaoContaPagarVO(obj);
		obj.setNovoObj(Boolean.FALSE);
		setContaPagarVO(obj);
		setLancamentoContabilVO(new LancamentoContabilVO());
		setLancamentoContabilCentroNegocioVO(new LancamentoContabilCentroNegocioVO());

		inicializarListasSelectItemTodosComboBox();
		montarListaSelectItemUnidadeEnsino();
		verificarPermissaoLancamentoContabilPagar();
		context().getExternalContext().getSessionMap().remove("contaPagarLancamentoContabil");
		registrarAtividadeUsuario(getUsuarioLogado(), "ContaPagarControle", "Finalizando Editar Conta Pagar", "Editando");
		setMensagemID("msg_dados_editar");
	}

	public String getvalidarGraficoVisivel() {
		String graficoContaPagarForm = (String) context().getExternalContext().getSessionMap().get("graficoContaPagarForm");
		return graficoContaPagarForm.isEmpty() ? "false" : "true";
	}

	public String getretornarDadosGraficos() {
		String graficoContaPagarForm = (String) context().getExternalContext().getSessionMap().get("graficoContaPagarForm");
		return graficoContaPagarForm.isEmpty() ? "" : graficoContaPagarForm;
	}

	// [<%= ((String) (session.getAttribute("graficoContaPagarForm")!=
	// null?session.getAttribute("graficoContaPagarForm"):""))%>]

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe <code>ContaPagar</code> para edição pelo usuário da aplicação.
	 */
	public String novo() throws Exception {
		registrarAtividadeUsuario(getUsuarioLogado(), "ContaPagarControle", "Nova Conta Pagar", "Novo");
		removerObjetoMemoria(this);
		this.existeContaPagarControleRemessa = false;
		setContaPagarVO(new ContaPagarVO());
		setCentroResultadoOrigemVO(new CentroResultadoOrigemVO());
		setLancamentoContabilVO(new LancamentoContabilVO());
		setLancamentoContabilCentroNegocioVO(new LancamentoContabilCentroNegocioVO());
		inicializarListasSelectItemTodosComboBox();
		getContaPagarVO().setResponsavel(getUsuarioLogadoClone());
		getContaPagarVO().setTipoOrigem(OrigemContaPagar.REGISTRO_MANUAL.getValor());
		verificarPermissaoLancamentoContabilPagar();
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("contaPagarForm.xhtml");
	}

	/**
	 * Rotina responsável por disponibilizar dados previamente inseridos pelos dados da Classe Conta a Pagar.
	 * 
	 * @return
	 */
	public String editarPagamentoInicializado() {
		NegociacaoPagamentoControle negociacaoPagamentoControle = (NegociacaoPagamentoControle) context().getExternalContext().getSessionMap().get("RegistroAulaControle");
		ContaPagarVO contaPagar = (ContaPagarVO) context().getExternalContext().getRequestMap().get("contaPagarItens");

		negociacaoPagamentoControle.novo();
		negociacaoPagamentoControle.getNegociacaoPagamentoVO().setUnidadeEnsino(contaPagar.getUnidadeEnsino());
		return Uteis.getCaminhoRedirecionamentoNavegacao("negociacaoPagamentoForm.xhtml");
	}

	public String getFormNovoFornecedor() {
		try {
			executarMetodoControle(FornecedorControle.class.getSimpleName(), "novo", (Object[]) null);
			return "popup('../compras/fornecedorForm.xhtml', 'fornecedorForm' , 790, 595)";
		} catch (Exception e) {
			setMensagemID("msg_erro");
			// System.out.println("MENSAGEM => " + e.getMessage());
			;
			return "";
		}
	}

	public void imprimirAutorizacaoPagamento() {
		try {
			executarMetodoControle(AutorizacaoPagamentoRelControle.class.getSimpleName(), "imprimirPDF", getContaPagarVO());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}	

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe <code>ContaPagar</code> para alteração. O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa disponibilizá-lo para edição.
	 */
	public String editar() throws Exception {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "ContaPagarControle", "Inicializando Editar Conta Pagar", "Editando");
			ContaPagarVO obj = (ContaPagarVO) context().getExternalContext().getRequestMap().get("contaPagarItens");
			obj = montarDadosParaEdicaoContaPagarVO(obj);
			obj.setNovoObj(Boolean.FALSE);
			setContaPagarVO(obj);
			setCentroResultadoOrigemVO(new CentroResultadoOrigemVO());
			setLancamentoContabilVO(new LancamentoContabilVO());
			setLancamentoContabilCentroNegocioVO(new LancamentoContabilCentroNegocioVO());
			inicializarListasSelectItemTodosComboBox();
			// montarGraficoLinhaContaPagar();
			registrarAtividadeUsuario(getUsuarioLogado(), "ContaPagarControle", "Finalizando Editar Conta Pagar", "Editando");
			verificarPermissaoLancamentoContabilPagar();
		    contaPagarControleRemessaContaPagarVO = getFacadeFactory().getContaPagarControleRemessaContaPagarFacade().consultarControleRemessaPorContaPagar(obj);
			this.existeContaPagarControleRemessa = Uteis.isAtributoPreenchido(contaPagarControleRemessaContaPagarVO) ? true : false;
			verificarBancoDePagamento();			
			getContaPagarVO().validarSubistituirTipoLancamentoDepreciado();
			setMensagemID("msg_dados_editar");
			return Uteis.getCaminhoRedirecionamentoNavegacao("contaPagarForm.xhtml");
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
			return Uteis.getCaminhoRedirecionamentoNavegacao("contaPagarForm.xhtml");
		}
	}
	
	public void upLoadArquivo(FileUploadEvent uploadEvent) {
		try {
			getFacadeFactory().getArquivoHelper().upLoad(uploadEvent, getContaPagarVO().getArquivoVO(), getConfiguracaoGeralPadraoSistema(), PastaBaseArquivoEnum.CONTA_PAGAR_TMP, getUsuarioLogado());
		} catch (Exception e) {
			e.printStackTrace();
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			uploadEvent = null;
		}
	}

	public void montarGraficoLinhaContaPagar() {
		try {
			setMostrarGeracaoGrafico(true);
			Date dataFinal = Uteis.getDataUltimoDiaMes(new Date());
			Date dataInicio = Uteis.getDataPrimeiroDiaMes(Uteis.getObterDataPorDataDiaMesAno(dataFinal, "ano", -1));
			getFacadeFactory().getContaPagarFacade().executarCriacaoGraficoLinhaContaPagar(getContaPagarVO(), getFiltroFavorecido(), dataInicio, dataFinal);
			context().getExternalContext().getSessionMap().put("graficoContaPagarForm", getContaPagarVO().getListaGraficoContaPagar());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String getFiltroNomeFavorecido() {
		if (getContaPagarVO().getTipoSacado().equals("FO")) {
			return getContaPagarVO().getFornecedor().getNome();
		} else if (getContaPagarVO().getTipoSacado().equals("BA")) {
			return getContaPagarVO().getBanco().getNome();
		} else if (getContaPagarVO().getTipoSacado().equals("AL") || getContaPagarVO().getTipoSacado().equals("FU")) {
			return getContaPagarVO().getPessoa().getNome();
		}
		return "";
	}

	/**
	 * Método responsavel por carregar todos os dados do ContaPagarVO para edição.
	 * 
	 * @param obj
	 */
	public ContaPagarVO montarDadosParaEdicaoContaPagarVO(ContaPagarVO obj) {
		try {
			return getFacadeFactory().getContaPagarFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return new ContaPagarVO();
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>ContaPagar</code>. Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de erro.
	 */
	public String gravar() {
		try {
			if (getContaPagarVO().getBanco().getCodigo() != 0 && getContaPagarVO().getBanco().getNome().equals("")) {
				getContaPagarVO().setBanco(getFacadeFactory().getBancoFacade().consultarPorChavePrimaria(getContaPagarVO().getBanco().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			}
			if (contaPagarVO.isNovoObj().booleanValue()) {
				registrarAtividadeUsuario(getUsuarioLogado(), "ContaPagarControle", "Inicializando Incluir Incluir Conta Pagar", "Incluindo");
				getFacadeFactory().getContaPagarFacade().incluir(contaPagarVO, true, true, getUsuarioLogado());
				registrarAtividadeUsuario(getUsuarioLogado(), "ContaPagarControle", "Finalizando Incluir Conta Pagar", "Incluindo");
			} else {				
				registrarAtividadeUsuario(getUsuarioLogado(), "ContaPagarControle", "Inicializando Alterar Conta Pagar", "Alterando");
				getFacadeFactory().getContaPagarFacade().alterar(contaPagarVO, true, true, getUsuarioLogado());
				registrarAtividadeUsuario(getUsuarioLogado(), "ContaPagarControle", "Finalizando Alterar Conta Pagar", "Alterando");
			}
			getContaPagarVO().reiniciarControleBloqueioCompetencia();
	        getContaPagarVO().setDataVencimentoAntesAlteracao(getContaPagarVO().getDataVencimento());
	        getContaPagarVO().setDataCompetenciaAntesAlteracao(getContaPagarVO().getDataFatoGerador());
			setMensagemID("msg_dados_gravados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("contaPagarForm.xhtml");
		} catch (Exception e) {
			if (e.getMessage().contains("fn_validarintegridadesituacaocontapagar")) {
				setMensagemDetalhada("msg_erro", "Conta Pagar Está Vinculada a Uma Negociação e a Situação é diferente de NEGOCIADA");
			} else {
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
			return Uteis.getCaminhoRedirecionamentoNavegacao("contaPagarForm.xhtml");
		}
	}
	
	public void cancelarContaPagar() {
		try {			
			getFacadeFactory().getContaPagarFacade().cancelarContaPagar(getContaPagarVO(), true, getUsuarioLogadoClone());
			setMensagemID(MSG_TELA.msg_dados_gravados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		} 
	}
	
	public void reativarContaPagarCancelada() {
		try {			
			getFacadeFactory().getContaPagarFacade().reativarContaPagarCancelada(getContaPagarVO(), true, getUsuarioLogadoClone());
			setMensagemID(MSG_TELA.msg_dados_gravados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		} 
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP ContaPagarCons.jsp. Define o tipo de consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
	 */
	public void scrollerListener(DataScrollEvent dataScrollEvent) {
		getControleConsultaOtimizado().setPaginaAtual(dataScrollEvent.getPage());
		getControleConsultaOtimizado().setPage(dataScrollEvent.getPage());
		setConsultaDataScroller(true);
		consultar();
	}
	
	public void scrollerListenerAluno(DataScrollEvent dataScrollEvent) {
		getDataModeloAluno().setPaginaAtual(dataScrollEvent.getPage());
		getDataModeloAluno().setPage(dataScrollEvent.getPage());
		consultarAluno();
	}

	@Override
	public String consultar() {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "ContaPagarControle", "Inicializando Consultar Conta Pagar", "Consultando");
			super.consultar();
			getControleConsultaOtimizado().preencherDadosParaConsulta(true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			
			if(getControleConsultaOtimizado().getCampoConsulta().equals("FAIXA_VALOR") && !Uteis.isAtributoPreenchido(getListConsulta())){
				getListConsulta().add(getValorConsultafaixaValorMenor());
				getListConsulta().add(getValorConsultafaixaValorMaior());
				
			}

			getControleConsultaOtimizado().setListaConsulta(getListConsulta());
			if (isConsultaPorCodigo()) {
				getUnidadeEnsinoVO().setCodigo(getUnidadeEnsinoLogado().getCodigo());
			}
			getFacadeFactory().getContaPagarFacade().consultar(getValorConsultaSituacaoFinanceiraDaConta(), getUnidadeEnsinoVO().getCodigo(), getFiltrarDataFactorGerador(), getConsultaPainelGestorFinanceiro(), getControleConsultaOtimizado());
			/*
			 * if (getControleConsulta().getCampoConsulta().equals("codigo") && !getConsultaPainelGestorFinanceiro()) { int valorInt = Uteis.getValorInteiro(getControleConsulta().getValorConsulta()); objs = getFacadeFactory().getContaPagarFacade().consultarPorCodigo(valorInt, getUnidadeEnsinoVO().getCodigo(), true, getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()); getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getContaPagarFacade().consultarPorCodigoTotalRegistros(valorInt, getUnidadeEnsinoVO().getCodigo(), true, getUsuarioLogado())); } if (getControleConsulta().getCampoConsulta().equals("favorecido") && !getConsultaPainelGestorFinanceiro()) { objs = getFacadeFactory().getContaPagarFacade().consultarPorNomeFavorecido(getControleConsulta().getValorConsulta(), getValorConsultaSituacaoFinanceiraDaConta(), getControleConsulta().getDataIni(), getControleConsulta().getDataFim(),
			 * getUnidadeEnsinoVO().getCodigo(), getFiltrarDataFactorGerador(), true, getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()); getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getContaPagarFacade().consultarPorNomeFavorecidoTotalRegistros(getControleConsulta().getValorConsulta(), getValorConsultaSituacaoFinanceiraDaConta(), getControleConsulta().getDataIni(), getControleConsulta().getDataFim(), getUnidadeEnsinoVO().getCodigo(), getFiltrarDataFactorGerador(), true, getUsuarioLogado())); } if (getControleConsulta().getCampoConsulta().equals("categoriaDespesa") && !getConsultaPainelGestorFinanceiro()) { objs = getFacadeFactory().getContaPagarFacade().consultarPorDescricaoCentroDespesaCentroDespesa(getControleConsulta().getValorConsulta(), getValorConsultaSituacaoFinanceiraDaConta(), getControleConsulta().getDataIni(), getControleConsulta().getDataFim(),
			 * getUnidadeEnsinoVO().getCodigo(), getFiltrarDataFactorGerador(), true, getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()); getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getContaPagarFacade().consultarPorDescricaoCentroDespesaCentroDespesaTotalRegistros(getControleConsulta().getValorConsulta(), getValorConsultaSituacaoFinanceiraDaConta(), getControleConsulta().getDataIni(), getControleConsulta().getDataFim(), getUnidadeEnsinoVO().getCodigo(), getFiltrarDataFactorGerador(), true, getUsuarioLogado())); } if (getControleConsulta().getCampoConsulta().equals("codigoPagamento") && !getConsultaPainelGestorFinanceiro()) { int valorInt = Uteis.getValorInteiro(getControleConsulta().getValorConsulta()); objs = getFacadeFactory().getContaPagarFacade().consultarPorCodigoPagamento(valorInt, getUnidadeEnsinoVO().getCodigo(), true,
			 * getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()); getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getContaPagarFacade().consultarPorCodigoPagamentoTotalRegistros(valorInt, getUnidadeEnsinoVO().getCodigo(), true, getUsuarioLogado())); } if (getControleConsulta().getCampoConsulta().equals("turma") && !getConsultaPainelGestorFinanceiro()) { objs = getFacadeFactory().getContaPagarFacade().consultarPorIdentificadorTurma(getControleConsulta().getValorConsulta(), getValorConsultaSituacaoFinanceiraDaConta(), getControleConsulta().getDataIni(), getControleConsulta().getDataFim(), getUnidadeEnsinoVO().getCodigo(), getFiltrarDataFactorGerador(), true, getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			 * getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getContaPagarFacade().consultarPorIdentificadorTurmaTotalRegistros(getControleConsulta().getValorConsulta(), getValorConsultaSituacaoFinanceiraDaConta(), getControleConsulta().getDataIni(), getControleConsulta().getDataFim(), getUnidadeEnsinoVO().getCodigo(), getFiltrarDataFactorGerador(), true, getUsuarioLogado())); } if (getControleConsulta().getCampoConsulta().equals("departamento") && !getConsultaPainelGestorFinanceiro()) { objs = getFacadeFactory().getContaPagarFacade().consultarPorDepartamento(getControleConsulta().getValorConsulta(), getValorConsultaSituacaoFinanceiraDaConta(), getControleConsulta().getDataIni(), getControleConsulta().getDataFim(), getUnidadeEnsinoVO().getCodigo(), getFiltrarDataFactorGerador(), true, getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			 * getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getContaPagarFacade().consultarPorDepartamentoTotalRegistros(getControleConsulta().getValorConsulta(), getValorConsultaSituacaoFinanceiraDaConta(), getControleConsulta().getDataIni(), getControleConsulta().getDataFim(), getUnidadeEnsinoVO().getCodigo(), getFiltrarDataFactorGerador(), true, getUsuarioLogado())); }
			 */

			if (getConsultaPainelGestorFinanceiro()) {
				List<ContaPagarVO> objs = getFacadeFactory().getContaPagarFacade().consultaRapidaContaPagarPorSituacaoUnidadeEnsino(getUnidadeEnsinoVO().getCodigo(), getSituacaoConsultaPainelGestorFinanceiro(), new Date(), 10, 0, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getContaPagarFacade().consultaRapidaContaPagarPorSituacaoUnidadeEnsinoTotalRegistros(getUnidadeEnsinoVO().getCodigo(), getSituacaoConsultaPainelGestorFinanceiro(), new Date()));
				getControleConsultaOtimizado().setListaConsulta(objs);
			}
			calcularTotalPagarTotalPago();
			setMensagemID("msg_dados_consultados");
			registrarAtividadeUsuario(getUsuarioLogado(), "ContaPagarControle", "Finalizando Consultar Conta Pagar", "Consultando");
			this.setListConsulta(new ArrayList<>(0));
			return Uteis.getCaminhoRedirecionamentoNavegacao("contaPagarCons.xhtml");
		} catch (Exception e) {
			getControleConsultaOtimizado().setListaConsulta(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("contaPagarCons.xhtml");
		}
	}
	
	
	public void removerArquivoContaReceber() throws Exception {
		try {
			getFacadeFactory().getContaPagarFacade().excluirArquivoContaPagar(getContaPagarVO(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			setMensagemID("msg_dados_excluidos");
			registrarAtividadeUsuario(getUsuarioLogado(), "RequerimentoControle", "Finalizando Remover Arquivo Requerimento ", "Downloading - Removendo");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe <code>ContaPagarVO</code> Após a exclusão ela automaticamente aciona a rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "ContaPagarControle", "Inicializando Excluir Conta Pagar", "Excluindo");
			if(getContaPagarVO().getTipoOrigem().equals(OrigemContaPagar.NEGOCICACAO_CONTA_PAGAR.getValor())) {
				throw new Exception("Esta conta pertence a NEGOCIAÇÃO DE CONTA PAGAR de código "+getContaPagarVO().getCodOrigem()+", neste caso para exlcuir esta conta é necessário realizar o estorno desta negociação.");
			}
			getFacadeFactory().getContaPagarFacade().excluir(contaPagarVO, true, getUsuarioLogado());
			setContaPagarVO(new ContaPagarVO());
			registrarAtividadeUsuario(getUsuarioLogado(), "ContaPagarControle", "Finalizando Excluir Conta Pagar", "Excluindo");
			setMensagemID("msg_dados_excluidos");
			return Uteis.getCaminhoRedirecionamentoNavegacao("contaPagarForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("contaPagarForm.xhtml");
		}
	}

	public void limparDadosPorCategoriaDespesa() {
		getCentroResultadoOrigemVO().setUnidadeEnsinoVO(null);
		getCentroResultadoOrigemVO().setDepartamentoVO(null);
		getCentroResultadoOrigemVO().setFuncionarioCargoVO(null);
		getCentroResultadoOrigemVO().setTurmaVO(null);
		getCentroResultadoOrigemVO().setCursoVO(null);
		getCentroResultadoOrigemVO().setTurnoVO(null);
		getCentroResultadoOrigemVO().setCentroResultadoAdministrativo(null);
	}

	public void selecionarDepartamentoCategoriaDespesa() {
		try {
			DepartamentoVO obj = (DepartamentoVO) context().getExternalContext().getRequestMap().get("departamentoItens");
			getCentroResultadoOrigemVO().setDepartamentoVO(obj);
			preencherDadosPorCategoriaDespesa();
			setCampoConsultaDepartamento("");
			setValorConsultaDepartamento("");
			getListaConsultaDepartamento().clear();
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void selecionarCentroDespesa() {
		try {
			CategoriaDespesaVO obj = (CategoriaDespesaVO) context().getExternalContext().getRequestMap().get("centroDespesaItens");
			getCentroResultadoOrigemVO().setCategoriaDespesaVO(getFacadeFactory().getCategoriaDespesaFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			limparDadosPorCategoriaDespesa();
			montarListaSelectItemTipoNivelCentroResultadoEnum();
			montarListaSelectItemUnidadeEnsinoCategoriaDespesa();
			getCentroResultadoOrigemVO().setPorcentagem(100.0);
			getCentroResultadoOrigemVO().calcularValor(getContaPagarVO().getPrevisaoValorPago());
			preencherDadosPorCategoriaDespesa();
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarCentroDespesa() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaCentroDespesa().equals("descricao")) {
				objs = getFacadeFactory().getCategoriaDespesaFacade().consultarPorDescricao(getValorConsultaCentroDespesa(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultaCentroDespesa().equals("identificadorCentroDespesa")) {
				objs = getFacadeFactory().getCategoriaDespesaFacade().consultarPorIdentificadorCategoriaDespesa(getValorConsultaCentroDespesa(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			setListaConsultaCentroDespesa(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCentroDespesa(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List getTipoConsultaComboAluno() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nomePessoa", "Aluno/Candidato"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		return itens;
	}

	public List getTipoConsultaComboCentroDespesa() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("descricao", "Descrição"));
		itens.add(new SelectItem("identificadorCentroDespesa", "Identificador Centro Despesa"));
		return itens;
	}

	public Integer getValorUnidadeEnsino() throws Exception {
		if (this.getUnidadeEnsinoLogado().getCodigo() == 0) {
			return getValorConsultaUnidadeEnsino();
		} else {
			return this.getUnidadeEnsinoLogado().getCodigo();
		}
	}

	public void calcularTotalPagarTotalPago() throws Exception {
		try {
			if (!getConsultaDataScroller()) {
				setTotalPagar(0.0);
				setTotalPago(0.0);
				setTotalNegociado(0.0);
				Map<String, Double> totalPagarTotalPago = null;
				if (getControleConsultaOtimizado().getCampoConsulta().equals(ContaPagarVO.enumCampoConsultaContaPagar.CODIGO.name()) && !getConsultaPainelGestorFinanceiro()) {
					int valorInt = Uteis.getValorInteiro(getControleConsultaOtimizado().getValorConsulta());
					totalPagarTotalPago = getFacadeFactory().getContaPagarFacade().consultarPorCodigoTotalPagarTotalPago(valorInt, getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado());
				}
				if (getControleConsultaOtimizado().getCampoConsulta().equals(ContaPagarVO.enumCampoConsultaContaPagar.CODIGO_PAGAMENTO.name()) && !getConsultaPainelGestorFinanceiro() && !getControleConsultaOtimizado().getListaConsulta().isEmpty()) {
					setTotalPago(((ContaPagarVO) getControleConsultaOtimizado().getListaConsulta().get(0)).getValorPago());
				}
				if (getControleConsultaOtimizado().getCampoConsulta().equals(ContaPagarVO.enumCampoConsultaContaPagar.FAVORECIDO.name()) && !getConsultaPainelGestorFinanceiro()) {
					totalPagarTotalPago = getFacadeFactory().getContaPagarFacade().consultaRapidaPorNomeSacadoTotalPagarTotalPago(getControleConsultaOtimizado().getValorConsulta(), getValorConsultaSituacaoFinanceiraDaConta(), getUnidadeEnsinoVO().getCodigo(), getControleConsultaOtimizado().getDataIni(), getControleConsultaOtimizado().getDataFim(), getFiltrarDataFactorGerador(), false, getUsuarioLogado());
				}
				if (getControleConsultaOtimizado().getCampoConsulta().equals(ContaPagarVO.enumCampoConsultaContaPagar.CATEGORIA_DESPESA.name()) && !getConsultaPainelGestorFinanceiro()) {
					totalPagarTotalPago = getFacadeFactory().getContaPagarFacade().consultaRapidaPorIdentificadorCentroReceitaTotalPagarTotalPago(getControleConsultaOtimizado().getValorConsulta(), getControleConsultaOtimizado().getDataIni(), getControleConsultaOtimizado().getDataFim(), getValorConsultaSituacaoFinanceiraDaConta(), getUnidadeEnsinoVO().getCodigo(), getFiltrarDataFactorGerador(), false, getUsuarioLogado());
				}
				if (getControleConsultaOtimizado().getCampoConsulta().equals(ContaPagarVO.enumCampoConsultaContaPagar.NR_DOCUMENTO.name()) && !getConsultaPainelGestorFinanceiro()) {
					totalPagarTotalPago = getFacadeFactory().getContaPagarFacade().consultaRapidaPorNrDocumentoTotalPagarTotalPago(getControleConsultaOtimizado().getValorConsulta(), getControleConsultaOtimizado().getDataIni(), getControleConsultaOtimizado().getDataFim(), getValorConsultaSituacaoFinanceiraDaConta(), getUnidadeEnsinoVO().getCodigo(), getFiltrarDataFactorGerador(), false, getUsuarioLogado());
				}
				if (getControleConsultaOtimizado().getCampoConsulta().equals(ContaPagarVO.enumCampoConsultaContaPagar.NUMERO_NOTA_FISCAL_ENTRADA.name()) && !getConsultaPainelGestorFinanceiro()) {
					if (getControleConsultaOtimizado().getValorConsulta().length() < 1) {
						throw new StreamSeiException(UteisJSF.internacionalizar("msg_ParametroConsulta_informeUmParametro"));
					}
					if(!Uteis.getIsValorNumerico(getControleConsultaOtimizado().getValorConsulta())) {
						throw new StreamSeiException("Infome apenas números.");
					}
					int valorInt = Uteis.getValorInteiro(getControleConsultaOtimizado().getValorConsulta());
					totalPagarTotalPago = getFacadeFactory().getContaPagarFacade().consultarPorNumeroNotaFiscalEntradaTotalPagarTotalPago(valorInt, getUnidadeEnsinoVO().getCodigo(),  getControleConsultaOtimizado().getDataIni(), getControleConsultaOtimizado().getDataFim(), getValorConsultaSituacaoFinanceiraDaConta(),  getFiltrarDataFactorGerador());
				}
				if (getConsultaPainelGestorFinanceiro()) {
					totalPagarTotalPago = getFacadeFactory().getContaPagarFacade().consultaRapidaPorSituacaoUnidadeEnsinoTotalPagarTotalPago(getValorConsultaUnidadeEnsino(), getSituacaoConsultaPainelGestorFinanceiro(), new Date(), false, getUsuarioLogado());
				}
				if (totalPagarTotalPago != null) {
					setTotalPagar(totalPagarTotalPago.get("valorAPagar"));
					setTotalPago(totalPagarTotalPago.get("valorPago"));
					setTotalNegociado(totalPagarTotalPago.get("valorNegociado"));
				}
			}
		} catch (Exception e) {
			setMensagem(e.getMessage());
			throw e;
		}
	}

	public void consultarAluno() {
		try {
			List<MatriculaVO> objs = new ArrayList<>(0);
			if (getValorConsultaAluno().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			if (getCampoConsultaAluno().equals("matricula")) {
				objs.add(getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
				getDataModeloAluno().setListaConsulta(objs);
				if (Uteis.isAtributoPreenchido(objs) && Uteis.isAtributoPreenchido(objs.get(0).getAluno().getNome())) {					
					getDataModeloAluno().setTotalRegistrosEncontrados(1);
				} else {
					getDataModeloAluno().setTotalRegistrosEncontrados(0);
				}
			}
			if (getCampoConsultaAluno().equals("nomePessoa")) {
				getDataModeloAluno().setLimitePorPagina(10);
				getDataModeloAluno().setValorConsulta(getValorConsultaAluno());
				getFacadeFactory().getMatriculaFacade().consultarMatriculas(getDataModeloAluno(), getUsuarioLogado());
			}
		
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarAluno() {
		try {
			MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("alunoItens");
			this.getContaPagarVO().setPessoa(obj.getAluno());
			this.getContaPagarVO().setMatricula(obj.getMatricula());
			this.getContaPagarVO().setFornecedor(null);
			this.getContaPagarVO().setBanco(null);
			this.getContaPagarVO().setFuncionario(null);
			getFacadeFactory().getContaPagarFacade().realizarVinculoContaReceberComResponsavelFinanceiro(getContaPagarVO(), getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

	}

	public void selecionarFornecedor() {
		try {
			FornecedorVO obj = (FornecedorVO) context().getExternalContext().getRequestMap().get("fornecedorItens");
			this.getContaPagarVO().setFornecedor(obj);
			if (obj.getPermiteenviarremessa()) {
				getFacadeFactory().getContaPagarFacade().preencherDadosRemessaContaPagar(getContaPagarVO(), obj.getNumeroBancoRecebimento(), obj.getNumeroAgenciaRecebimento(), obj.getDigitoAgenciaRecebimento(), obj.getContaCorrenteRecebimento(), obj.getDigitoCorrenteRecebimento(),
						getContaPagarVO().getFornecedor().getChaveEnderecamentoPix() ,	getContaPagarVO().getFornecedor().getTipoIdentificacaoChavePixEnum(), getUsuarioLogado());
				setComboTipoLancamentoContaPagar(null);
				setComboTipoServicoContaPagar(null);
			}
			this.getContaPagarVO().setBanco(null);
			this.getContaPagarVO().setFuncionario(null);
			this.getContaPagarVO().setPessoa(null);
			this.getContaPagarVO().setMatricula("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

	}

	public void consultarParceiro() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaParceiro().equals("nome")) {
				objs = getFacadeFactory().getParceiroFacade().consultarPorNome(getValorConsultaParceiro(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaParceiro().equals("razaoSocial")) {
				objs = getFacadeFactory().getParceiroFacade().consultarPorRazaoSocial(getValorConsultaParceiro(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaParceiro(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List getTipoConsultaParceiro() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("razaoSocial", "Razão social"));
		return itens;
	}

	public void selecionarParceiro() {
		try {
			ParceiroVO obj = (ParceiroVO) context().getExternalContext().getRequestMap().get("parceiroItens");
			getContaPagarVO().setParceiro(obj);
			if (obj.getPermiteenviarremessa()) {
				getFacadeFactory().getContaPagarFacade().preencherDadosRemessaContaPagar(getContaPagarVO(), obj.getNumeroBancoRecebimento(), obj.getNumeroAgenciaRecebimento(), obj.getDigitoAgenciaRecebimento(), obj.getContaCorrenteRecebimento(), obj.getDigitoCorrenteRecebimento(),
						getContaPagarVO().getParceiro().getChaveEnderecamentoPix() ,	getContaPagarVO().getParceiro().getTipoIdentificacaoChavePixEnum(), getUsuarioLogado());
				setComboTipoLancamentoContaPagar(null);
				setComboTipoServicoContaPagar(null);
			}
			listaConsultaParceiro.clear();
			this.setValorConsultaParceiro("");
			this.setCampoConsultaParceiro("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarFornecedor() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaFornecedor().equals("nome")) {
				objs = getFacadeFactory().getFornecedorFacade().consultarPorNome(getValorConsultaFornecedor(), "AT", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultaFornecedor().equals("razaoSocial")) {
				objs = getFacadeFactory().getFornecedorFacade().consultarPorRazaoSocial(getValorConsultaFornecedor(), "AT", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultaFornecedor().equals("CNPJ")) {
				objs = getFacadeFactory().getFornecedorFacade().consultarPorCNPJ(getValorConsultaFornecedor(), "AT", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultaFornecedor().equals("CPF")) {
				objs = getFacadeFactory().getFornecedorFacade().consultarPorCPF(getValorConsultaFornecedor(), "AT", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			setListaConsultaFornecedor(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaFornecedor(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String getCampoConsultaFornecedor() {
		return campoConsultaFornecedor;
	}

	public void setCampoConsultaFornecedor(String campoConsultaFornecedor) {
		this.campoConsultaFornecedor = campoConsultaFornecedor;
	}

	public List getListaConsultaFornecedor() {
		return listaConsultaFornecedor;
	}

	public void setListaConsultaFornecedor(List listaConsultaFornecedor) {
		this.listaConsultaFornecedor = listaConsultaFornecedor;
	}

	public String getValorConsultaFornecedor() {
		return valorConsultaFornecedor;
	}

	public void setValorConsultaFornecedor(String valorConsultaFornecedor) {
		this.valorConsultaFornecedor = valorConsultaFornecedor;
	}

	public void selecionarFuncionario() throws Exception {
		FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItens");
		obj = getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimaria(obj.getCodigo(), 0, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
		this.getContaPagarVO().setFuncionario(obj);
		this.getContaPagarVO().getPessoa().setCodigo(obj.getPessoa().getCodigo());
		
		List<BancoVO> bancos = getFacadeFactory().getBancoFacade().consultarPorNrBanco(obj.getNumeroBancoRecebimento(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
		if (!bancos.isEmpty()) {
			this.getContaPagarVO().setBancoRecebimento(bancos.get(0));
		}
		this.getContaPagarVO().setContaCorrenteRecebimento(obj.getContaCorrenteRecebimento());
		this.getContaPagarVO().setDigitoCorrenteRecebimento(obj.getDigitoCorrenteRecebimento());
		this.getContaPagarVO().setNumeroAgenciaRecebimento(obj.getNumeroAgenciaRecebimento());
		this.getContaPagarVO().setDigitoAgenciaRecebimento(obj.getDigitoAgenciaRecebimento());
		if (Uteis.isAtributoPreenchido(getContaPagarVO().getBancoRemessaPagar())) {
			getFacadeFactory().getContaPagarFacade().preencherDadosRemessaContaPagar(getContaPagarVO(), obj.getNumeroBancoRecebimento(), obj.getNumeroAgenciaRecebimento(), obj.getDigitoAgenciaRecebimento(), obj.getContaCorrenteRecebimento(), obj.getDigitoCorrenteRecebimento(), 
					getContaPagarVO().getFuncionario().getChaveEnderecamentoPix() ,	getContaPagarVO().getFuncionario().getTipoIdentificacaoChavePixEnum(), getUsuarioLogado());
			setComboTipoLancamentoContaPagar(null);
			setComboTipoServicoContaPagar(null);
		}
		this.getContaPagarVO().setMatricula("");

		Uteis.liberarListaMemoria(getListaConsultaFuncionario());
		campoConsultaFuncionario = null;
		valorConsultaFuncionario = null;
	}

	public List getListaSelectItemTipoSacado() throws Exception {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("AL", "Aluno/Candidato"));
		itens.add(new SelectItem("RF", "Responsável Financeiro"));
		itens.add(new SelectItem("BA", "Banco"));
		itens.add(new SelectItem("FO", "Fornecedor"));
		itens.add(new SelectItem("FU", "Funcionário"));
		itens.add(new SelectItem("PA", "Parceiro"));
		itens.add(new SelectItem("OC", "Operadora Cartão"));
		return itens;
	}
	
	public List<SelectItem> getTipoConsultaComboFornecedor() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("razaoSocial", "Razão Social"));
		itens.add(new SelectItem("CNPJ", "CNPJ"));
		itens.add(new SelectItem("CPF", "CPF"));
		return itens;
	}

	public List getListaSelectItemSituacaoFinanceira() throws Exception {
		List<SelectItem> objs = new ArrayList<>(0);
		objs.add(new SelectItem(0, "Todos"));
		Hashtable listaSituacaoFinanceira = (Hashtable) Dominios.getSituacaoFinanceira();
		Enumeration keys = listaSituacaoFinanceira.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) listaSituacaoFinanceira.get(value);
			objs.add(new SelectItem(value, label));
		}
		objs.add(new SelectItem("NE", "Negociado"));
		objs.add(new SelectItem("CF", "Cancelado"));
		objs.add(new SelectItem("PAAP", "Pago e A Pagar"));
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		return objs;
	}

	public void alterarTipoSacado() {
		getContaPagarVO().setFuncionario(new FuncionarioVO());
		getContaPagarVO().setResponsavelFinanceiro(new PessoaVO());
		getContaPagarVO().setFornecedor(new FornecedorVO());
		getContaPagarVO().setParceiro(new ParceiroVO());
	}

	public List getListaSelectItemCategoriaDespesa() throws Exception {
		List objs = new ArrayList(0);
		objs.add(new SelectItem(0, ""));
		List<CategoriaDespesaVO> listaCategoria = null;
		listaCategoria = getFacadeFactory().getCategoriaDespesaFacade().consultarPorIdentificadorCategoriaDespesa("", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
		if (listaCategoria != null && listaCategoria.size() != 0) {
			for (CategoriaDespesaVO categoriaDespesa : listaCategoria) {
				objs.add(new SelectItem(categoriaDespesa.getDescricao(), categoriaDespesa.getDescricao()));
			}
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		return objs;
	}

	public Boolean getHabilitarComboBoxCentroCusto() {
		return contaPagarVO.isTipoSacadoFuncionario();
	}

	public void consultarFuncionario() {
		try {
			List objs = null;

			getFacadeFactory().getFuncionarioFacade().setIdEntidade("Funcionario");

			if (getCampoConsultaFuncionario().equals("nome")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNome(getValorConsultaFuncionario(), "FU", this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("matricula")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorMatricula(getValorConsultaFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("nomeCidade")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeCidade(getValorConsultaFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("CPF")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCPF(getValorConsultaFuncionario(), "FU", this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("cargo")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeCargo(getValorConsultaFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("departamento")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeDepartamento(getValorConsultaFuncionario(), "FU", this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("unidadeEnsino")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeUnidadeEnsino(getValorConsultaFuncionario(), "FU", this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaFuncionario(objs);
			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			setListaConsultaFuncionario(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List getTipoConsultaComboFuncionario() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("razaoSocial", "Razão Social"));
		itens.add(new SelectItem("CNPJ", "CNPJ"));
		itens.add(new SelectItem("CPF", "CPF"));
		return itens;
	}

	public String getCampoConsultaFuncionario() {
		return campoConsultaFuncionario;
	}

	public void setCampoConsultaFuncionario(String campoConsultaFuncionario) {
		this.campoConsultaFuncionario = campoConsultaFuncionario;
	}

	public List getListaConsultaFuncionario() {
		return listaConsultaFuncionario;
	}

	public void setListaConsultaFuncionario(List listaConsultaFuncionario) {
		this.listaConsultaFuncionario = listaConsultaFuncionario;
	}

	public String getValorConsultaFuncionario() {
		return valorConsultaFuncionario;
	}

	public void setValorConsultaFuncionario(String valorConsultaFuncionario) {
		this.valorConsultaFuncionario = valorConsultaFuncionario;
	}

	public String getCampoConsultaFuncionarioCentroCusto() {
		return campoConsultaFuncionarioCentroCusto;
	}

	public void setCampoConsultaFuncionarioCentroCusto(String campoConsultaFuncionario) {
		this.campoConsultaFuncionarioCentroCusto = campoConsultaFuncionario;
	}

	public List<FuncionarioCargoVO> getListaConsultaFuncionarioCentroCusto() {
		return listaConsultaFuncionarioCentroCusto;
	}

	public void setListaConsultaFuncionarioCentroCusto(List<FuncionarioCargoVO> listaConsultaFuncionario) {
		this.listaConsultaFuncionarioCentroCusto = listaConsultaFuncionario;
	}

	public String getValorConsultaFuncionarioCentroCusto() {
		return valorConsultaFuncionarioCentroCusto;
	}

	public void setValorConsultaFuncionarioCentroCusto(String valorConsultaFuncionario) {
		this.valorConsultaFuncionarioCentroCusto = valorConsultaFuncionario;
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
	 * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo <code>situacao</code>
	 */
	public List getListaSelectItemSituacaoContaPagar() throws Exception {
		List objs = new ArrayList(0);
		Hashtable pagarPagoNegociados = (Hashtable) Dominios.getPagarPagoNegociado();
		Enumeration keys = pagarPagoNegociados.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) pagarPagoNegociados.get(value);
			objs.add(new SelectItem(value, label));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		return objs;
	}

	public List getListaSelectItemTipoOrigem() throws Exception {
		List objs = new ArrayList(0);
		objs.add(new SelectItem("", ""));
		Hashtable tipoOrigem = (Hashtable) Dominios.getTipoOrigemContaPagar();
		Enumeration keys = tipoOrigem.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) tipoOrigem.get(value);
			objs.add(new SelectItem(value, label));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		return objs;
	}

	public List getListaSelectItemTipoJuroTipoMulta() throws Exception {
		List objs = new ArrayList(0);
		Hashtable tipoJuroTipoMulta = (Hashtable) Dominios.getContaPagarTipoJuroTipoMulta();
		Enumeration keys = tipoJuroTipoMulta.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) tipoJuroTipoMulta.get(value);
			objs.add(new SelectItem(value, label));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		return objs;
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox relativo ao atributo <code>ContaCorrente</code>.
	 */
	public void montarListaSelectItemContaCorrente(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		List objs = new ArrayList(0);
		try {
			resultadoConsulta = consultarContaCorrentePorNumero(prm);
			if (resultadoConsulta.isEmpty()) {
				getListaSelectItemContaCorrente().clear();
			} else {
				i = resultadoConsulta.iterator();
				objs.add(new SelectItem(0, ""));
				while (i.hasNext()) {
					ContaCorrenteVO obj = (ContaCorrenteVO) i.next();
					objs.add(new SelectItem(obj.getCodigo(),obj.getDescricaoParaComboBox()));
				}
				setListaSelectItemContaCorrente(objs);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo <code>ContaCorrente</code>. Buscando todos os objetos correspondentes a entidade <code>ContaCorrente</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemUnidadeEnsino() {
		try {
			montarListaSelectItemUnidadeEnsino(0);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	/**
	 * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>numero</code> Este atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox correspondente
	 */
	public List consultarUnidadeEnsinoPorCodigo(Integer numeroPrm) throws Exception {
		List lista = null;
		if ((numeroPrm != null && !numeroPrm.equals(0)) || (getUnidadeEnsinoLogado().getCodigo() != null && !getUnidadeEnsinoLogado().getCodigo().equals(0))) {
			lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorCodigo(numeroPrm, getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		} else {
			lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarTodasUnidades(false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		}
		return lista;
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox relativo ao atributo <code>ContaCorrente</code>.
	 */
	public List getListaSelectItemBanco() throws Exception {
		if (listaSelectItemBanco == null) {
			listaSelectItemBanco = new ArrayList<>();	
		}
		return listaSelectItemBanco;
	}

	/**
	 * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>numero</code> Este atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox correspondente
	 */
	public List consultarBancoPorNome(String prm) throws Exception {
		List lista = getFacadeFactory().getBancoFacade().consultarPorNome(prm, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		return lista;
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox relativo ao atributo <code>ContaCorrente</code>.
	 */
	public void montarListaSelectItemUnidadeEnsino(Integer prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			if (getUnidadeEnsinoLogado().getCodigo().intValue() != 0) {
				setListaSelectItemUnidadeEnsino(new ArrayList<SelectItem>());
				getListaSelectItemUnidadeEnsino().add(new SelectItem(getUnidadeEnsinoLogado().getCodigo(), getUnidadeEnsinoLogado().getNome()));
				getContaPagarVO().setUnidadeEnsino(getUnidadeEnsinoLogadoClone());
				return;
			}
			resultadoConsulta = consultarUnidadeEnsinoPorCodigo(prm);
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
			}
			setListaSelectItemUnidadeEnsino(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo <code>ContaCorrente</code>. Buscando todos os objetos correspondentes a entidade <code>ContaCorrente</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemContaCorrente() {
		try {
			montarListaSelectItemContaCorrente("");
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());
			;
		}
	}

	/**
	 * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>numero</code> Este atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox correspondente
	 */
	public List consultarContaCorrentePorNumero(String numeroPrm) throws Exception {
		List lista = getFacadeFactory().getContaCorrenteFacade().consultaRapidaPorBancoControleRemessaNivelComboBox(getContaPagarVO().getBanco().getCodigo(), getUsuarioLogado().getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
		return lista;
	}
	
	public void limparCamposPorTipoNivelCentroResultadoEnum() {
		try {
			getCentroResultadoOrigemVO().limparCamposPorTipoNivelCentroResultadoEnum();
			preencherDadosPorCategoriaDespesa();
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}

	}
	
	private void montarListaSelectItemTipoNivelCentroResultadoEnum() {
		try {
			getListaSelectItemTipoNivelCentroResultadoEnum().clear();
			if(getCentroResultadoOrigemVO().isCategoriaDespesaInformada()){
				getFacadeFactory().getCategoriaDespesaFacade().montarListaSelectItemTipoNivelCentroResultadoEnum(getCentroResultadoOrigemVO().getCategoriaDespesaVO(), getListaSelectItemTipoNivelCentroResultadoEnum());
				if(!getListaSelectItemTipoNivelCentroResultadoEnum().isEmpty()){
					getCentroResultadoOrigemVO().setTipoNivelCentroResultadoEnum((TipoNivelCentroResultadoEnum) getListaSelectItemTipoNivelCentroResultadoEnum().get(0).getValue());	
				} else {
					getCentroResultadoOrigemVO().setTipoNivelCentroResultadoEnum(TipoNivelCentroResultadoEnum.UNIDADE_ENSINO);
				}
			}
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
		
	}

	
	public void inicializarListasSelectItemTodosComboBox() throws Exception {
		if (getContaPagarVO().isTipoSacadoBanco()) {
			montarListaSelectItemContaCorrente();
		}
		if (Uteis.isAtributoPreenchido(getContaPagarVO().getFormaPagamentoVO())) {
			montarListaSelectItemFormaPagamento();
		}
		montarListaSelectItemUnidadeEnsino();
		montarListaSelectItemLayoutsBancos();
		montarListaSelectItemBancoFavorecido();
		montarListaSelectItemFormaPagamento();
		montarListaSelectItemBancosRemessaBancaria();
		if (Uteis.isAtributoPreenchido(getContaPagarVO())) {
			montarListaSelectItemUnidadeEnsinoCategoriaDespesa();
		}
	}

	public List getListaSelectItemTipoDesconto() throws Exception {
		List objs = new ArrayList(0);
		Hashtable tipoDesconto = (Hashtable) Dominios.getTipoDesconto();
		objs.add(new SelectItem("", ""));
		Enumeration keys = tipoDesconto.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) tipoDesconto.get(value);
			objs.add(new SelectItem(value, label));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		return objs;
	}

	public boolean isConsultaPorCategoriaDespesa() {
		return getControleConsultaOtimizado().getCampoConsulta().equals(ContaPagarVO.enumCampoConsultaContaPagar.CATEGORIA_DESPESA.name());
	}

	public boolean isConsultaPorCodigo() {
		return getControleConsultaOtimizado().getCampoConsulta().equals(ContaPagarVO.enumCampoConsultaContaPagar.CODIGO.name())
				|| getControleConsultaOtimizado().getCampoConsulta().equals(ContaPagarVO.enumCampoConsultaContaPagar.CODIGO_PAGAMENTO.name());
	}

	public boolean isCampoValorNumerico() {
		return  getControleConsultaOtimizado().getCampoConsulta().equals(ContaPagarVO.enumCampoConsultaContaPagar.VALOR.name());
				 
	}
	public boolean isCampoValorNumericoFaixaValor() {
		return   getControleConsultaOtimizado().getCampoConsulta().equals(ContaPagarVO.enumCampoConsultaContaPagar.FAIXA_VALOR.name());
	}
	public boolean isCampoText() {
		return  getControleConsultaOtimizado().getCampoConsulta().equals(ContaPagarVO.enumCampoConsultaContaPagar.FAVORECIDO.name())
				|| getControleConsultaOtimizado().getCampoConsulta().equals(ContaPagarVO.enumCampoConsultaContaPagar.TURMA.name())
				|| getControleConsultaOtimizado().getCampoConsulta().equals(ContaPagarVO.enumCampoConsultaContaPagar.DEPARTAMENTO.name())
				|| getControleConsultaOtimizado().getCampoConsulta().equals(ContaPagarVO.enumCampoConsultaContaPagar.NUMERO_NOTA_FISCAL_ENTRADA.name())
				|| getControleConsultaOtimizado().getCampoConsulta().equals(ContaPagarVO.enumCampoConsultaContaPagar.NR_DOCUMENTO.name())
				|| getControleConsultaOtimizado().getCampoConsulta().equals(ContaPagarVO.enumCampoConsultaContaPagar.CODIGO_NOTA_FISCAL_ENTRADA.name())
		|| getControleConsultaOtimizado().getCampoConsulta().equals(ContaPagarVO.enumCampoConsultaContaPagar.CODIGO_CONTRATO_DESPESA.name());
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
	 * 
	 * @throws Exception
	 */
	public String inicializarConsultar() throws Exception {
		removerObjetoMemoria(this);
		montarListaSelectItemUnidadeEnsino();
		setControleConsultaOtimizado(new DataModelo());
		getControleConsultaOtimizado().setCampoConsulta(ContaPagarVO.enumCampoConsultaContaPagar.FAVORECIDO.name());
		getControleConsultaOtimizado().setDataIni(Uteis.getDataPrimeiroDiaMes(new Date()));
		getControleConsultaOtimizado().setDataFim(Uteis.getDataUltimoDiaMes(new Date()));
		setTotalPagar(0.0);
		setTotalPago(0.0);
		getControleConsultaOtimizado().setPaginaAtual(1);
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("contaPagarCons.xhtml");
	}

	public String getCampoConsultaCentroDespesa() {
		if (campoConsultaCentroDespesa == null) {
			campoConsultaCentroDespesa = "";
		}
		return campoConsultaCentroDespesa;
	}

	public void setCampoConsultaCentroDespesa(String campoConsultaCentroDespesa) {
		this.campoConsultaCentroDespesa = campoConsultaCentroDespesa;
	}

	public List getListaConsultaCentroDespesa() {
		if (listaConsultaCentroDespesa == null) {
			listaConsultaCentroDespesa = new ArrayList(0);
		}
		return listaConsultaCentroDespesa;
	}

	public void setListaConsultaCentroDespesa(List listaConsultaCentroDespesa) {
		this.listaConsultaCentroDespesa = listaConsultaCentroDespesa;
	}

	public String getValorConsultaCentroDespesa() {
		if (valorConsultaCentroDespesa == null) {
			valorConsultaCentroDespesa = "";
		}
		return valorConsultaCentroDespesa;
	}

	public void setValorConsultaCentroDespesa(String valorConsultaCentroDespesa) {
		this.valorConsultaCentroDespesa = valorConsultaCentroDespesa;
	}

	public List<SelectItem> getListaSelectItemContaCorrente() {
		if (listaSelectItemContaCorrente == null) {
			listaSelectItemContaCorrente = new ArrayList(0);
		}
		return listaSelectItemContaCorrente;
	}

	public void setListaSelectItemContaCorrente(List listaSelectItemContaCorrente) {
		this.listaSelectItemContaCorrente = listaSelectItemContaCorrente;
	}

	public ContaPagarVO getContaPagarVO() {
		if (contaPagarVO == null) {
			contaPagarVO = new ContaPagarVO();
		}
		return contaPagarVO;
	}

	public void setContaPagarVO(ContaPagarVO contaPagarVO) {
		this.contaPagarVO = contaPagarVO;
	}

	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList(0);
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public Boolean getApresentarOrigemContaTextual() {
		if ((this.getContaPagarVO().getTipoOrigem().equals("AD")) ||
		    (this.getContaPagarVO().getTipoOrigem().equals("CP"))) {
		    	return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	public Boolean getHabilitarCamposEditaveis() {
		return contaPagarVO.getValidarSePossuiOrigem();
	}

	public List<SelectItem> getListaSelectItemUnidadeEnsinoCategoriaDespesa() {
		listaSelectItemUnidadeEnsinoCategoriaDespesa = Optional.ofNullable(listaSelectItemUnidadeEnsinoCategoriaDespesa).orElse(new ArrayList<>());
		return listaSelectItemUnidadeEnsinoCategoriaDespesa;
	}

	public void setListaSelectItemUnidadeEnsinoCategoriaDespesa(List<SelectItem> listaSelectItemUnidadeEnsinoCategoriaDespesa) {
		this.listaSelectItemUnidadeEnsinoCategoriaDespesa = listaSelectItemUnidadeEnsinoCategoriaDespesa;
	}

	public List<SelectItem> getListaSelectItemTipoNivelCentroResultadoEnum() {
		listaSelectItemTipoNivelCentroResultadoEnum = Optional.ofNullable(listaSelectItemTipoNivelCentroResultadoEnum).orElse(new ArrayList<>());
		return listaSelectItemTipoNivelCentroResultadoEnum;
	}

	public void setListaSelectItemTipoNivelCentroResultadoEnum(List<SelectItem> listaSelectItemTipoNivelCentroResultadoEnum) {
		this.listaSelectItemTipoNivelCentroResultadoEnum = listaSelectItemTipoNivelCentroResultadoEnum;
	}

	/**
	 * @return the valorConsultaSituacaoDaConta
	 */
	public String getValorConsultaSituacaoFinanceiraDaConta() {
		if (valorConsultaSituacaoFinanceiraDaConta == null) {
			valorConsultaSituacaoFinanceiraDaConta = "";
		}
		return valorConsultaSituacaoFinanceiraDaConta;
	}

	/**
	 * @param valorConsultaSituacaoDaConta
	 *            the valorConsultaSituacaoDaConta to set
	 */
	public void setValorConsultaSituacaoFinanceiraDaConta(String valorConsultaSituacaoFinanceiraDaConta) {
		this.valorConsultaSituacaoFinanceiraDaConta = valorConsultaSituacaoFinanceiraDaConta;
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

	public List<SelectItem> getTipoConsultaComboTurma() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador"));
		itens.add(new SelectItem("nomeUnidadeEnsino", "Unidade Ensino"));
		itens.add(new SelectItem("nomeTurno", "Turno"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}

	public void limparTurma() {
		try {
			getCentroResultadoOrigemVO().setTurmaVO(new TurmaVO());
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void selecionarTurma() {
		try {
			TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
			obj = getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			getCentroResultadoOrigemVO().setTurmaVO(obj);
			getCentroResultadoOrigemVO().setCursoVO(obj.getCurso());
			getCentroResultadoOrigemVO().setTurnoVO(obj.getTurno());
			preencherDadosPorCategoriaDespesa();
			setValorConsultaTurma("");
			setCampoConsultaTurma("");
			setListaConsultaTurma(null);
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarTurma() {
		try {
			super.consultar();
			Uteis.checkState(!Uteis.isAtributoPreenchido(getCentroResultadoOrigemVO().getUnidadeEnsinoVO()), "O campo Unidade Ensino deve ser informado.");
			setListaConsultaTurma(getFacadeFactory().getTurmaFacade().consultar(getCampoConsultaTurma(), getValorConsultaTurma(), getCentroResultadoOrigemVO().getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			getListaConsultaTurma().clear();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
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

	public List<TurmaVO> getListaConsultaTurma() {
		if (listaConsultaTurma == null) {
			listaConsultaTurma = new ArrayList<TurmaVO>(0);
		}
		return listaConsultaTurma;
	}

	public void setListaConsultaTurma(List<TurmaVO> listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
	}

	public List getTipoConsultaComboCurso() {
		List itens = new ArrayList(0);
		// itens.add(new SelectItem("codigo", "Código"));

		itens.add(new SelectItem("nome", "Nome"));
		return itens;
	}

	public List getTipoConsultaComboCursoTurno() {
		List itens = new ArrayList(0);
		// itens.add(new SelectItem("codigo", "Código"));
		itens.add(new SelectItem("curso", "Curso"));
		itens.add(new SelectItem("turno", "Turno"));
		return itens;
	}

	public void limparCurso() {
		try {
			getCentroResultadoOrigemVO().setCursoVO(new CursoVO());
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarCurso() {
		try {
			Uteis.checkState(!Uteis.isAtributoPreenchido(getCentroResultadoOrigemVO().getUnidadeEnsinoVO()), "O campo Unidade Ensino deve ser informado.");
			setListaConsultaCurso(getFacadeFactory().getCursoFacade().consultar(getCampoConsultaCurso(), getValorConsultaCurso(), getCentroResultadoOrigemVO().getUnidadeEnsinoVO().getCodigo(), true, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCurso(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarCurso() {
		try {
			CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItens");
			getLancamentoContabilCentroNegocioVO().setCursoVO(obj);
			getCentroResultadoOrigemVO().setCursoVO(obj);
			preencherDadosPorCategoriaDespesa();
			listaConsultaCurso.clear();
			this.setValorConsultaCurso("");
			this.setCampoConsultaCurso("");
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
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

	public List<CursoVO> getListaConsultaCurso() {
		if (listaConsultaCurso == null) {
			listaConsultaCurso = new ArrayList<CursoVO>(0);
		}
		return listaConsultaCurso;
	}

	public void setListaConsultaCurso(List<CursoVO> listaConsultaCurso) {
		this.listaConsultaCurso = listaConsultaCurso;
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

	public void consultarCursoTurno() {
		try {
			Uteis.checkState(!Uteis.isAtributoPreenchido(getCentroResultadoOrigemVO().getUnidadeEnsinoVO()), "O campo Unidade Ensino deve ser informado.");
			setListaConsultaCursoTurno(getFacadeFactory().getUnidadeEnsinoCursoFacade().consultar(getCampoConsultaCursoTurno(), getValorConsultaCursoTurno(), getCentroResultadoOrigemVO().getUnidadeEnsinoVO().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCursoTurno(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparTurno() {
		try {
			getCentroResultadoOrigemVO().setTurnoVO(new TurnoVO());
			getCentroResultadoOrigemVO().setCursoVO(new CursoVO());
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void selecionarCursoTurno() {
		try {
			UnidadeEnsinoCursoVO obj = (UnidadeEnsinoCursoVO) context().getExternalContext().getRequestMap().get("unidadeEnsinoCursoItens");
			getCentroResultadoOrigemVO().setCursoVO(obj.getCurso());
			getCentroResultadoOrigemVO().setTurnoVO(obj.getTurno());
			preencherDadosPorCategoriaDespesa();
			setListaConsultaCursoTurno(null);
			this.setValorConsultaCursoTurno("");
			this.setCampoConsultaCursoTurno("");
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void inicializarDadoConsultaCentroResultadoAdministrativo() {
		try {
			setCentroResultadoAdministrativo(true);
			inicializarDadosComunsCentroResultado();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}

	}	

	private void inicializarDadosComunsCentroResultado() {
		setCentroResultadoDataModelo(new DataModelo());
		getCentroResultadoDataModelo().setCampoConsulta(CentroResultadoVO.enumCampoConsultaCentroResultado.DESCRICAO_CENTRO_RESULTADO.name());
	}

	public void selecionarCentroResultado() {
		try {
			CentroResultadoVO obj = (CentroResultadoVO) context().getExternalContext().getRequestMap().get("centroResultadoItens");
			if (isCentroResultadoAdministrativo()) {
				getCentroResultadoOrigemVO().setCentroResultadoAdministrativo(obj);
			} 
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void scrollerListenerCentroResultado(DataScrollEvent dataScrollerEvent) {
		try {
			getCentroResultadoDataModelo().setPaginaAtual(dataScrollerEvent.getPage());
			getCentroResultadoDataModelo().setPage(dataScrollerEvent.getPage());
			consultarCentroResultado();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarCentroResultado() {
		try {
			super.consultar();
			getCentroResultadoDataModelo().preencherDadosParaConsulta(false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			getFacadeFactory().getCentroResultadoFacade().consultar(SituacaoEnum.ATIVO, true, getCentroResultadoOrigemVO().getDepartamentoVO(), getCentroResultadoOrigemVO().getCursoVO(), getCentroResultadoOrigemVO().getTurmaVO(), getCentroResultadoDataModelo());
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			getControleConsultaOtimizado().getListaConsulta().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarFuncionarioCentroCusto() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaFuncionarioCentroCusto().equals("nomeFuncionario")) {
				objs = getFacadeFactory().getFuncionarioCargoFacade().consultarPorNomeFuncionarioUnidadeEnsinoSituacao(getValorConsultaFuncionarioCentroCusto(), null, true, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionarioCentroCusto().equals("nomeCargo")) {
				objs = getFacadeFactory().getFuncionarioCargoFacade().consultarPorNomeCargoUnidadeEnsinoSituacao(getValorConsultaFuncionarioCentroCusto(), null, true, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaFuncionarioCentroCusto(objs);
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setListaConsultaFuncionarioCentroCusto(new ArrayList(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void selecionarFuncionarioCentroCusto() {
		try {
			FuncionarioCargoVO obj = (FuncionarioCargoVO) context().getExternalContext().getRequestMap().get("funcionarioCentroCustoItens");
			getCentroResultadoOrigemVO().setFuncionarioCargoVO(obj);
			preencherDadosPorCategoriaDespesa();
			Uteis.liberarListaMemoria(getListaConsultaFuncionarioCentroCusto());
			campoConsultaFuncionarioCentroCusto = null;
			valorConsultaFuncionarioCentroCusto = null;
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setListaConsultaFuncionarioCentroCusto(new ArrayList(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public List getTipoConsultaComboFuncionarioCentroCusto() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nomeFuncionario", "Nome"));
		itens.add(new SelectItem("nomeCargo", "Cargo"));
		return itens;
	}

	public void realizarDistribuicaoValoresCentroResultado() {
		try {
			getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().realizarDistribuicaoValoresCentroResultado(getContaPagarVO().getListaCentroResultadoOrigemVOs(), getContaPagarVO().getPrevisaoValorPago(), getUsuarioLogado());
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void calcularPorcentagemCentroResultadoOrigem() {
		try {
			getCentroResultadoOrigemVO().calcularPorcentagem(getContaPagarVO().getPrevisaoValorPago());
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void calcularValorCentroResultadoOrigem() {
		try {
			getCentroResultadoOrigemVO().calcularValor(getContaPagarVO().getPrevisaoValorPago());
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void addCentroResultadoOrigem() {
		try {
			getCentroResultadoOrigemVO().setQuantidade(1.0);
			getCentroResultadoOrigemVO().setTipoMovimentacaoCentroResultadoOrigemEnum(TipoMovimentacaoCentroResultadoOrigemEnum.ENTRADA);
			validarValorPorcentagemCentroResultadoOrigem(getCentroResultadoOrigemVO());
			Integer indiceSubstituirCentroResultado = indiceSubstituirCentroResultadoLista(getContaPagarVO().getListaCentroResultadoOrigemVOs());
			if (indiceSubstituirCentroResultado >= 0 && indiceSubstituirCentroResultado < getContaPagarVO().getListaCentroResultadoOrigemVOs().size()) {
				getContaPagarVO().getListaCentroResultadoOrigemVOs().set(indiceSubstituirCentroResultado, getCentroResultadoOrigemVO());
			}
			getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().adicionarCentroResultadoOrigem(getContaPagarVO().getListaCentroResultadoOrigemVOs(), getCentroResultadoOrigemVO(), getContaPagarVO().getPrevisaoValorPago(), true, getUsuarioLogado());
			setCentroResultadoOrigemVO(new CentroResultadoOrigemVO());
			getContaPagarVO().getListaCentroResultadoOrigemVOs().forEach(cro -> cro.setEdicaoManual(cro.equalsCentroResultadoOrigem(getCentroResultadoOrigemVO())));
			setMensagemID(MSG_TELA.msg_dados_adicionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void editarCentroResultadoOrigem() {
		try {
			CentroResultadoOrigemVO crovo = (CentroResultadoOrigemVO) context().getExternalContext().getRequestMap().get("centroResultadoOrigemItem");
			getContaPagarVO().getListaCentroResultadoOrigemVOs().forEach(cro -> cro.setEdicaoManual(cro.equalsCentroResultadoOrigem(crovo)));
			setCentroResultadoOrigemVO(crovo.getClone());
			montarListaSelectItemTipoNivelCentroResultadoEnum();
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}

	}	

	public void removerCentroResultadoOrigem() {
		try {
			CentroResultadoOrigemVO centroResultadoOrigem = (CentroResultadoOrigemVO) context().getExternalContext().getRequestMap().get("centroResultadoOrigemItem");
			getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().removerCentroResultadoOrigem(getContaPagarVO().getListaCentroResultadoOrigemVOs(), centroResultadoOrigem, getUsuarioLogado());
			setMensagemID(MSG_TELA.msg_dados_excluidos.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void preencherDadosPorCategoriaDespesa() {
		try {
			if(Uteis.isAtributoPreenchido(getCentroResultadoOrigemVO().getCategoriaDespesaVO())){
				getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().preencherDadosPorCategoriaDespesa(getCentroResultadoOrigemVO(), getUsuarioLogado());	
			}
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void montarListaSelectItemUnidadeEnsinoCategoriaDespesa() throws Exception {
		getListaSelectItemUnidadeEnsinoCategoriaDespesa().clear();
		if(Uteis.isAtributoPreenchido(getContaPagarVO().getUnidadeEnsino())){
			getCentroResultadoOrigemVO().setUnidadeEnsinoVO(getContaPagarVO().getUnidadeEnsino());
		}
		if (getUnidadeEnsinoLogado().getCodigo().intValue() != 0) {
			getListaSelectItemUnidadeEnsinoCategoriaDespesa().add(new SelectItem(getUnidadeEnsinoLogado().getCodigo(), getUnidadeEnsinoLogado().getNome()));
			return;
		}
		getListaSelectItemUnidadeEnsinoCategoriaDespesa().add(new SelectItem(0, ""));
		List<UnidadeEnsinoVO> listaUnidadeEnsino = consultarUnidadeEnsinoPorCodigo(0);
		listaUnidadeEnsino.stream().forEach(p -> getListaSelectItemUnidadeEnsinoCategoriaDespesa().add(new SelectItem(p.getCodigo(), p.getNome())));

	}

	public String efetuarPagamento() {
		try {
			context().getExternalContext().getSessionMap().put("contaPagarItens", getContaPagarVO());
			context().getExternalContext().getSessionMap().put("origemContaPagarControle", true);
			removerControleMemoriaFlash("NegociacaoPagamentoControle");
			removerControleMemoriaTela("NegociacaoPagamentoControle");
			return Uteis.getCaminhoRedirecionamentoNavegacao("negociacaoPagamentoForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("contaPagarForm.xhtml");
		}
	}

	public String efetuarPagamentoPorTelaConsulta() {
		try {
			ContaPagarVO obj = (ContaPagarVO) context().getExternalContext().getRequestMap().get("contaPagarItens");
			obj = montarDadosParaEdicaoContaPagarVO(obj);
			obj.setNovoObj(Boolean.FALSE);
			context().getExternalContext().getSessionMap().put("contaPagarItens", obj);
			context().getExternalContext().getSessionMap().put("origemContaPagarControle", true);
			removerControleMemoriaFlash("NegociacaoPagamentoControle");
			removerControleMemoriaTela("NegociacaoPagamentoControle");
			return Uteis.getCaminhoRedirecionamentoNavegacao("negociacaoPagamentoForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("contaPagarForm.xhtml");
		}
	}

	public String getCampoConsultaCursoTurno() {
		if (campoConsultaCursoTurno == null) {
			campoConsultaCursoTurno = "";
		}
		return campoConsultaCursoTurno;
	}

	public String getValorConsultaCursoTurno() {
		if (valorConsultaCursoTurno == null) {
			valorConsultaCursoTurno = "";
		}
		return valorConsultaCursoTurno;
	}

	public List<UnidadeEnsinoCursoVO> getListaConsultaCursoTurno() {
		if (listaConsultaCursoTurno == null) {
			listaConsultaCursoTurno = new ArrayList<UnidadeEnsinoCursoVO>(0);
		}
		return listaConsultaCursoTurno;
	}

	public void setCampoConsultaCursoTurno(String campoConsultaCursoTurno) {
		this.campoConsultaCursoTurno = campoConsultaCursoTurno;
	}

	public void setValorConsultaCursoTurno(String valorConsultaCursoTurno) {
		this.valorConsultaCursoTurno = valorConsultaCursoTurno;
	}

	public void setListaConsultaCursoTurno(List<UnidadeEnsinoCursoVO> listaConsultaCursoTurno) {
		this.listaConsultaCursoTurno = listaConsultaCursoTurno;
	}

	public boolean getIsPermitePagamento() {
		if (!getContaPagarVO().isNovoObj() && getContaPagarVO().getSituacao().equals("AP")) {
			return true;
		}
		return false;
	}

	public String getCampoConsultaAluno() {
		return campoConsultaAluno;
	}

	public void setCampoConsultaAluno(String campoConsultaAluno) {
		this.campoConsultaAluno = campoConsultaAluno;
	}

	public String getValorConsultaAluno() {
		return valorConsultaAluno;
	}

	public void setValorConsultaAluno(String valorConsultaAluno) {
		this.valorConsultaAluno = valorConsultaAluno;
	}

	public Boolean getFiltroFavorecido() {
		if (filtroFavorecido == null) {
			filtroFavorecido = true;
		}
		return filtroFavorecido;
	}

	public void setFiltroFavorecido(Boolean filtroFavorecido) {
		this.filtroFavorecido = filtroFavorecido;
	}

	public Boolean getMostrarGeracaoGrafico() {
		if (mostrarGeracaoGrafico == null) {
			mostrarGeracaoGrafico = false;
		}
		return mostrarGeracaoGrafico;
	}

	public void setMostrarGeracaoGrafico(Boolean mostrarGeracaoGrafico) {
		this.mostrarGeracaoGrafico = mostrarGeracaoGrafico;
	}

	public boolean getIsObjetoGravado() {
		return !getContaPagarVO().getNovoObj();
	}

	public int getValorConsultaUnidadeEnsino() {
		return valorConsultaUnidadeEnsino;
	}

	public void setValorConsultaUnidadeEnsino(int valorConsultaUnidadeEnsino) {
		this.valorConsultaUnidadeEnsino = valorConsultaUnidadeEnsino;
	}

	public Boolean getConsultaPainelGestorFinanceiro() {
		if (consultaPainelGestorFinanceiro == null) {
			consultaPainelGestorFinanceiro = false;
		}
		return consultaPainelGestorFinanceiro;
	}

	public void setConsultaPainelGestorFinanceiro(Boolean consultaPainelGestorFinanceiro) {
		this.consultaPainelGestorFinanceiro = consultaPainelGestorFinanceiro;
	}

	public String getSituacaoConsultaPainelGestorFinanceiro() {
		if (situacaoConsultaPainelGestorFinanceiro == null) {
			situacaoConsultaPainelGestorFinanceiro = "VH";
		}
		return situacaoConsultaPainelGestorFinanceiro;
	}

	public void setSituacaoConsultaPainelGestorFinanceiro(String situacaoConsultaPainelGestorFinanceiro) {
		this.situacaoConsultaPainelGestorFinanceiro = situacaoConsultaPainelGestorFinanceiro;
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

	public List getListaConsultaParceiro() {
		if (listaConsultaParceiro == null) {
			listaConsultaParceiro = new ArrayList(0);
		}
		return listaConsultaParceiro;
	}

	public void setListaConsultaParceiro(List listaConsultaParceiro) {
		this.listaConsultaParceiro = listaConsultaParceiro;
	}

	public String getValorConsultaParceiro() {
		if (valorConsultaParceiro == null) {
			valorConsultaParceiro = "";
		}
		return valorConsultaParceiro;
	}

	public void setValorConsultaParceiro(String valorConsultaParceiro) {
		this.valorConsultaParceiro = valorConsultaParceiro;
	}

	public String getCampoConsultaParceiro() {
		if (campoConsultaParceiro == null) {
			campoConsultaParceiro = "";
		}
		return campoConsultaParceiro;
	}

	public void setCampoConsultaParceiro(String campoConsultaParceiro) {
		this.campoConsultaParceiro = campoConsultaParceiro;
	}

	private List<PessoaVO> listaConsultaResponsavelFinanceiro;
	private String valorConsultaResponsavelFinanceiro;
	private String campoConsultaResponsavelFinanceiro;

	public void consultarResponsavelFinanceiro() {
		try {

			if (getValorConsultaResponsavelFinanceiro().trim().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			getFacadeFactory().getPessoaFacade().setIdEntidade("ResponsavelFinanceiro");
			getListaConsultaResponsavelFinanceiro().clear();
			if (getCampoConsultaResponsavelFinanceiro().equals("nome")) {
				setListaConsultaResponsavelFinanceiro(getFacadeFactory().getPessoaFacade().consultaRapidaPorNomeResponsavelFinanceiro(getValorConsultaResponsavelFinanceiro(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado()));
			}
			if (getCampoConsultaResponsavelFinanceiro().equals("nomeAluno")) {
				setListaConsultaResponsavelFinanceiro(getFacadeFactory().getPessoaFacade().consultaRapidaPorNomeAlunoResponsavelFinanceiro(getValorConsultaResponsavelFinanceiro(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado()));
			}
			if (getCampoConsultaResponsavelFinanceiro().equals("CPF")) {
				setListaConsultaResponsavelFinanceiro(getFacadeFactory().getPessoaFacade().consultaRapidaPorCpfResponsavelFinanceiro(getValorConsultaResponsavelFinanceiro(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado()));
			}

			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			setListaConsultaResponsavelFinanceiro(new ArrayList<PessoaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	private List<SelectItem> tipoConsultaComboResponsavelFinanceiro;

	public List<SelectItem> getTipoConsultaComboResponsavelFinanceiro() {
		if (tipoConsultaComboResponsavelFinanceiro == null) {
			tipoConsultaComboResponsavelFinanceiro = new ArrayList<SelectItem>(0);
			tipoConsultaComboResponsavelFinanceiro.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboResponsavelFinanceiro.add(new SelectItem("nomeAluno", "Aluno"));
			tipoConsultaComboResponsavelFinanceiro.add(new SelectItem("CPF", "CPF"));
		}
		return tipoConsultaComboResponsavelFinanceiro;
	}

	public void anularControleOtimizado() {
		montarListaSelectItemUnidadeEnsino();
		getControleConsultaOtimizado().setValorConsulta("");
		getControleConsultaOtimizado().getListaConsulta().clear();
	}

	public void selecionarResponsavelFinanceiro() {
		try {
			PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("responsavelFinanceiroItens");
			this.getContaPagarVO().setResponsavelFinanceiro(obj);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<PessoaVO> getListaConsultaResponsavelFinanceiro() {
		if (listaConsultaResponsavelFinanceiro == null) {
			listaConsultaResponsavelFinanceiro = new ArrayList<PessoaVO>(0);
		}
		return listaConsultaResponsavelFinanceiro;
	}

	public void setListaConsultaResponsavelFinanceiro(List<PessoaVO> listaConsultaResponsavelFinanceiro) {
		this.listaConsultaResponsavelFinanceiro = listaConsultaResponsavelFinanceiro;
	}

	public String getValorConsultaResponsavelFinanceiro() {
		if (valorConsultaResponsavelFinanceiro == null) {
			valorConsultaResponsavelFinanceiro = "";
		}
		return valorConsultaResponsavelFinanceiro;
	}

	public void setValorConsultaResponsavelFinanceiro(String valorConsultaResponsavelFinanceiro) {
		this.valorConsultaResponsavelFinanceiro = valorConsultaResponsavelFinanceiro;
	}

	public String getCampoConsultaResponsavelFinanceiro() {
		if (campoConsultaResponsavelFinanceiro == null) {
			campoConsultaResponsavelFinanceiro = "";
		}
		return campoConsultaResponsavelFinanceiro;
	}

	public void setCampoConsultaResponsavelFinanceiro(String campoConsultaResponsavelFinanceiro) {
		this.campoConsultaResponsavelFinanceiro = campoConsultaResponsavelFinanceiro;
	}

	public void consultarAlunoResponsavelFinanceiro() {
		try {
			limparMensagem();
			getDataModeloAluno().setListaConsulta(getFacadeFactory().getMatriculaFacade().consultaRapidaBasicaPorCodigoResponsavelFinanceiro(getContaPagarVO().getResponsavelFinanceiro().getCodigo(), false, getUsuarioLogado()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
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

	public boolean getApresentarContaCorrente() {
		return !getListaSelectItemContaCorrente().isEmpty();
	}

	/**
	 * @return the filtrarDataFactorGerador
	 */
	public Boolean getFiltrarDataFactorGerador() {
		if (filtrarDataFactorGerador == null) {
			filtrarDataFactorGerador = Boolean.FALSE;
		}
		return filtrarDataFactorGerador;
	}

	/**
	 * @param filtrarDataFactorGerador
	 *            the filtrarDataFactorGerador to set
	 */
	public void setFiltrarDataFactorGerador(Boolean filtrarDataFactorGerador) {
		this.filtrarDataFactorGerador = filtrarDataFactorGerador;
	}

	public boolean getApresentarBotaoAutorizacaoPagamento() {
		return !getContaPagarVO().getCodigo().equals(0);
	}

	public List<SelectItem> getTipoConsultaOperadoraCartao() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("NOME", "Nome"));
		itens.add(new SelectItem("CODIGO", "Código"));
		itens.add(new SelectItem("TIPO", "Tipo"));
		return itens;
	}

	public String getCampoConsultaOperadoraCartao() {
		if (campoConsultaOperadoraCartao == null) {
			campoConsultaOperadoraCartao = "";
		}
		return campoConsultaOperadoraCartao;
	}

	public void setCampoConsultaOperadoraCartao(String campoConsultaOperadoraCartao) {
		this.campoConsultaOperadoraCartao = campoConsultaOperadoraCartao;
	}

	public String getValorConsultaOperadoraCartao() {
		if (valorConsultaOperadoraCartao == null) {
			valorConsultaOperadoraCartao = "";
		}
		return valorConsultaOperadoraCartao;
	}

	public void setValorConsultaOperadoraCartao(String valorConsultaOperadoraCartao) {
		this.valorConsultaOperadoraCartao = valorConsultaOperadoraCartao;
	}

	public void consultarOperadoraCartao() {
		try {
			getListaConsultaOperadoraCartao().clear();
			getListaConsultaOperadoraCartao().addAll(getFacadeFactory().getOperadoraCartaoFacade().consultar(getValorConsultaOperadoraCartao(), getCampoConsultaOperadoraCartao(), false, getUsuarioLogado()));
		} catch (Exception e) {
			setListaConsultaOperadoraCartao(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<OperadoraCartaoVO> getListaConsultaOperadoraCartao() {
		if (listaConsultaOperadoraCartao == null) {
			listaConsultaOperadoraCartao = new ArrayList<OperadoraCartaoVO>(0);
		}
		return listaConsultaOperadoraCartao;
	}

	public void setListaConsultaOperadoraCartao(List<OperadoraCartaoVO> listaConsultaOperadoraCartao) {
		this.listaConsultaOperadoraCartao = listaConsultaOperadoraCartao;
	}

	public void selecionarOperadoraCartao() {
		try {
			OperadoraCartaoVO obj = (OperadoraCartaoVO) context().getExternalContext().getRequestMap().get("operadoraCartaoItens");
			getContaPagarVO().setOperadoraCartao(obj);
			getListaConsultaOperadoraCartao().clear();
			setValorConsultaOperadoraCartao("");
			setCampoConsultaOperadoraCartao("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

	}

	public ContaPagarPagamentoVO getContaPagarPagamentoVO() {
		if (contaPagarPagamentoVO == null) {
			contaPagarPagamentoVO = new ContaPagarPagamentoVO();
		}
		return contaPagarPagamentoVO;
	}

	public void setContaPagarPagamentoVO(ContaPagarPagamentoVO contaPagarPagamentoVO) {
		this.contaPagarPagamentoVO = contaPagarPagamentoVO;
	}

	public void inicializarDadosCheque() {
		ContaPagarPagamentoVO obj = (ContaPagarPagamentoVO) context().getExternalContext().getRequestMap().get("pagamentoItens");
		setContaPagarPagamentoVO(obj);
	}

	public String consultarDepartamento() {
		try {
			List objs = new ArrayList(0);
			Integer unidadeEnsino = 0;
			if (Uteis.isAtributoPreenchido(getLancamentoContabilVO().getUnidadeEnsinoVO())) {
				unidadeEnsino = getLancamentoContabilVO().getUnidadeEnsinoVO().getCodigo();
			} else if (Uteis.isAtributoPreenchido(getCentroResultadoOrigemVO().getUnidadeEnsinoVO())) {
				unidadeEnsino = getCentroResultadoOrigemVO().getUnidadeEnsinoVO().getCodigo();
			} else {
				unidadeEnsino = getUnidadeEnsinoLogado().getCodigo();
			}
			if (getCampoConsultaDepartamento().equals("codigo")) {
				int valorInt = 0;
				if (!getValorConsultaDepartamento().equals("")) {
					valorInt = Integer.parseInt(getValorConsultaDepartamento());
				}
				objs = getFacadeFactory().getDepartamentoFacade().consultarPorCodigoPorUnidadeEnsino(valorInt, unidadeEnsino, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaDepartamento().equals("nome")) {
				objs = getFacadeFactory().getDepartamentoFacade().consultarPorNomePorUnidadeEnsino(getValorConsultaDepartamento(), unidadeEnsino, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaDepartamento(objs);
			setMensagemID("msg_dados_consultados");
			return "consultar";
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "consultar";
		}
	}

	public void selecionarDepartamento() {
		try {
			DepartamentoVO obj = (DepartamentoVO) context().getExternalContext().getRequestMap().get("departamentoItens");
			getLancamentoContabilCentroNegocioVO().setDepartamentoVO(obj);
			setCampoConsultaDepartamento("");
			setValorConsultaDepartamento("");
			getListaConsultaDepartamento().clear();
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}

	}

	public List getTipoConsultaComboDepartamento() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	public void persistirLancamentoContabil() {
		try {
			getFacadeFactory().getContaPagarFacade().persistirLancamentoContabilVO(getContaPagarVO(), false, getUsuarioLogado());
			setMensagemID("msg_dados_editar");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void gerarLancamentoContabilPadrao() {
		try {
			getFacadeFactory().getContaPagarFacade().persistirLancamentoContabilPadrao(getContaPagarVO(), false, getUsuarioLogado());
			setMensagemID("msg_dados_editar");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void novoLancamentoContabilCentroNegocioAdministrativo() {
		try {
			setLancamentoContabilCentroNegocioVO(new LancamentoContabilCentroNegocioVO());
			getLancamentoContabilCentroNegocioVO().setLancamentoContabilVO(getLancamentoContabilVO());
			setTipoCategoriaDespesaRateioEnum(TipoCentroNegocioEnum.ADMINISTRATIVO);
			setModalAptoParaSerFechado("");
			setMensagemID("msg_dados_editar");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void novoLancamentoContabilCentroNegocioAcademico() {
		try {
			setLancamentoContabilCentroNegocioVO(new LancamentoContabilCentroNegocioVO());
			getLancamentoContabilCentroNegocioVO().setLancamentoContabilVO(getLancamentoContabilVO());
			setTipoCategoriaDespesaRateioEnum(TipoCentroNegocioEnum.ACADEMICO);
			setModalAptoParaSerFechado("");
			setMensagemID("msg_dados_editar");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void addLancamentoContabilCentroNegocioVO() {
		try {
			getLancamentoContabilCentroNegocioVO().setEdicaoManual(true);
			getFacadeFactory().getLancamentoContabilFacade().addLancamentoContabilCentroNegociacao(getLancamentoContabilVO(), getLancamentoContabilCentroNegocioVO(), getTipoCategoriaDespesaRateioEnum(), getUsuarioLogado());
			setLancamentoContabilCentroNegocioVO(new LancamentoContabilCentroNegocioVO());
			setModalAptoParaSerFechado("RichFaces.$('panelCentroNegocio').hide();");
			setMensagemID("msg_dados_editar");
		} catch (Exception e) {
			setModalAptoParaSerFechado("");
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void editarLancamentoContabilCentroNegocioVO() {
		try {
			LancamentoContabilCentroNegocioVO obj = (LancamentoContabilCentroNegocioVO) context().getExternalContext().getRequestMap().get("lccnItens");
			setLancamentoContabilCentroNegocioVO(obj);
			if (getLancamentoContabilCentroNegocioVO().isRateioAcademico()) {
				setTipoCategoriaDespesaRateioEnum(TipoCentroNegocioEnum.ACADEMICO);
			} else {
				setTipoCategoriaDespesaRateioEnum(TipoCentroNegocioEnum.ADMINISTRATIVO);
			}
			setModalAptoParaSerFechado("");
			setMensagemID("msg_dados_editar");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void removerLancamentoContabilCentroNegocioVO() {
		try {
			LancamentoContabilCentroNegocioVO obj = (LancamentoContabilCentroNegocioVO) context().getExternalContext().getRequestMap().get("lccnItens");
			getFacadeFactory().getLancamentoContabilFacade().removeLancamentoContabilCentroNegociacaoVO(getLancamentoContabilVO(), obj, getUsuarioLogado());
			setMensagemID("msg_dados_editar");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void novoLancamentoContabilCredito() {
		try {
			setLancamentoContabilVO(new LancamentoContabilVO());
			getLancamentoContabilVO().setTipoPlanoConta(TipoPlanoContaEnum.CREDITO);
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
			setModalAptoParaSerFechado("");
			setMensagemID("msg_dados_editar");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void addLancamentoContabilVO() {
		try {
			getFacadeFactory().getContaPagarFacade().addLancamentoContabilVO(getContaPagarVO(), getLancamentoContabilVO(), getUsuarioLogado());
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
			obj.setEdicaoManual(true);
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
			getFacadeFactory().getContaPagarFacade().removeLancamentoContabilVO(getContaPagarVO(), obj, getUsuarioLogado());
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

	public void recalcularLancamentoContabilCentroNegocioPorValor() {
		try {
			getLancamentoContabilCentroNegocioVO().recalcularLancmentoValor();
			setMensagemID("msg_dados_editar");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void recalcularLancamentoContabilCentroNegocioPorPercentual() {
		try {
			getLancamentoContabilCentroNegocioVO().recalcularLancmentoPercentual();
			setMensagemID("msg_dados_editar");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarPlanoContaDebito() {
		try {
			PlanoContaVO obj = (PlanoContaVO) context().getExternalContext().getRequestMap().get("planoContaItens");
			getLancamentoContabilVO().setPlanoContaVO(obj);
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
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

	public void selecionarPlanoContaCredito() {
		try {
			PlanoContaVO obj = (PlanoContaVO) context().getExternalContext().getRequestMap().get("planoContaItens");
			getLancamentoContabilVO().setPlanoContaVO(obj);
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
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

	public List getTipoConsultaComboPlanoConta() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("codigo", "Código"));
		itens.add(new SelectItem("identificadorPlanoConta", "Identificador Plano Conta"));
		itens.add(new SelectItem("descricao", "Descrição"));
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

	public LancamentoContabilCentroNegocioVO getLancamentoContabilCentroNegocioVO() {
		if (lancamentoContabilCentroNegocioVO == null) {
			lancamentoContabilCentroNegocioVO = new LancamentoContabilCentroNegocioVO();
		}
		return lancamentoContabilCentroNegocioVO;
	}

	public void setLancamentoContabilCentroNegocioVO(LancamentoContabilCentroNegocioVO lancamentoContabilCentroNegocioVO) {
		this.lancamentoContabilCentroNegocioVO = lancamentoContabilCentroNegocioVO;
	}

	public TipoCentroNegocioEnum getTipoCategoriaDespesaRateioEnum() {
		if (tipoCategoriaDespesaRateioEnum == null) {
			tipoCategoriaDespesaRateioEnum = TipoCentroNegocioEnum.ACADEMICO;
		}
		return tipoCategoriaDespesaRateioEnum;
	}

	public void setTipoCategoriaDespesaRateioEnum(TipoCentroNegocioEnum tipoCategoriaDespesaRateioEnum) {
		this.tipoCategoriaDespesaRateioEnum = tipoCategoriaDespesaRateioEnum;
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

	public List<DepartamentoVO> getListaConsultaDepartamento() {
		if (listaConsultaDepartamento == null) {
			listaConsultaDepartamento = new ArrayList<DepartamentoVO>(0);
		}
		return listaConsultaDepartamento;
	}

	public void setListaConsultaDepartamento(List listaConsultaDepartamento) {
		this.listaConsultaDepartamento = listaConsultaDepartamento;
	}

	public CentroResultadoOrigemVO getCentroResultadoOrigemVO() {
		centroResultadoOrigemVO = Optional.ofNullable(centroResultadoOrigemVO).orElse(new CentroResultadoOrigemVO());
		return centroResultadoOrigemVO;
	}

	public void setCentroResultadoOrigemVO(CentroResultadoOrigemVO centroResultadoOrigemVO) {
		this.centroResultadoOrigemVO = centroResultadoOrigemVO;
	}

	public DataModelo getCentroResultadoDataModelo() {
		centroResultadoDataModelo = Optional.ofNullable(centroResultadoDataModelo).orElse(new DataModelo());
		return centroResultadoDataModelo;
	}

	public void setCentroResultadoDataModelo(DataModelo centroResultadoDataModelo) {
		this.centroResultadoDataModelo = centroResultadoDataModelo;
	}

	public boolean isCentroResultadoAdministrativo() {
		return centroResultadoAdministrativo;
	}

	public void setCentroResultadoAdministrativo(boolean centroResultadoAdministrativo) {
		this.centroResultadoAdministrativo = centroResultadoAdministrativo;
	}

	public Boolean permiteAlterarCentroResultado;
	public Boolean getPermiteAlterarCentroResultado() {
			if(permiteAlterarCentroResultado == null) {
				try {
					ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoFinanceiroEnum.PERMITE_ALTERAR_CENTRO_RESULTADO_CONTA_PAGAR, getUsuarioLogado());
					permiteAlterarCentroResultado = true;
				} catch (Exception e) {
					permiteAlterarCentroResultado = false;
				}
			}
			return permiteAlterarCentroResultado;
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

	public void verificarPermissaoLancamentoContabilPagar() {
		try {
			if (!getContaPagarVO().isLancamentoContabil()) {
				getContaPagarVO().setLancamentoContabil(getFacadeFactory().getConfiguracaoContabilFacade().consultaSeExisteConfiguracaoContabilPorCodigoUnidadeEnsino(getContaPagarVO().getUnidadeEnsino().getCodigo(), getUsuarioLogado()));
			}
		} catch (Exception e) {
			getContaPagarVO().setLancamentoContabil(false);
		}
	}

	public void montarListaSelectItemLayoutsBancos() {
		try {
			List listaBancoVOs = getFacadeFactory().getBancoFacade().consultarPorBancoNivelComboBox(false, getUsuarioLogado());
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
			Iterator j = listaBancoVOs.iterator();
			while (j.hasNext()) {
				BancoVO item = (BancoVO) j.next();
				objs.add(new SelectItem(item.getCodigo(), item.getNome()));
			}
			SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
			Collections.sort((List) objs, ordenador);
			setListaSelectItemBanco(objs);
			setComboTipoLancamentoContaPagar(null);
			setComboTipoServicoContaPagar(null);
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}

	}

	public void limparCamposRemessaBancaria() {
		try {
			if (Uteis.isAtributoPreenchido(getContaPagarVO().getBancoRemessaPagar().getCodigo())) {
				getContaPagarVO().setBancoRemessaPagar(getFacadeFactory().getBancoFacade().consultarPorChavePrimaria(getContaPagarVO().getBancoRemessaPagar().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
				getContaPagarVO().setTipoLancamentoContaPagar(null);
				getContaPagarVO().setTipoServicoContaPagar(null);
				getContaPagarVO().setModalidadeTransferenciaBancariaEnum(null);
				getContaPagarVO().setFinalidadeDocEnum(null);
				getContaPagarVO().setFinalidadeTedEnum(null);
				setComboTipoLancamentoContaPagar(null);
				setComboTipoServicoContaPagar(null);
				montarListaSelectItemFormaPagamento();				
				selecionarFormaPagamentoContaPagar();
			} else {
				getContaPagarVO().setTipoLancamentoContaPagar(null);
				getContaPagarVO().setTipoServicoContaPagar(null);
				getContaPagarVO().setModalidadeTransferenciaBancariaEnum(null);
				getContaPagarVO().setFinalidadeDocEnum(null);
				getContaPagarVO().setFinalidadeTedEnum(null);
				setComboTipoLancamentoContaPagar(null);
				setComboTipoServicoContaPagar(null);
				montarListaSelectItemFormaPagamento();
//				getContaPagarVO().setFormaPagamentoVO(new FormaPagamentoVO());
//				getComboFormaPagamento().clear();
			}
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}

	}

	public void preencherCamposTipoSacadoBanco() {
		try {
			montarListaSelectItemContaCorrente();
			if (Uteis.isAtributoPreenchido(getContaPagarVO().getBanco().getCodigo())) {
				getContaPagarVO().setBancoRecebimento(getFacadeFactory().getBancoFacade().consultarPorChavePrimaria(getContaPagarVO().getBanco().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			}
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}

	}

	public void selecionarFormaPagamentoContaPagar() {
		try {
			if (getContaPagarVO().isTipoSacadoFornecedor() && Uteis.isAtributoPreenchido(getContaPagarVO().getFornecedor()) && getContaPagarVO().getFornecedor().getPermiteenviarremessa()) {
				getFacadeFactory().getContaPagarFacade().preencherDadosRemessaContaPagar(getContaPagarVO(), getContaPagarVO().getFornecedor().getNumeroBancoRecebimento(), getContaPagarVO().getFornecedor().getNumeroAgenciaRecebimento(), getContaPagarVO().getFornecedor().getDigitoAgenciaRecebimento(), getContaPagarVO().getFornecedor().getContaCorrenteRecebimento(), getContaPagarVO().getFornecedor().getDigitoCorrenteRecebimento(), 
						getContaPagarVO().getFornecedor().getChaveEnderecamentoPix() ,	getContaPagarVO().getFornecedor().getTipoIdentificacaoChavePixEnum(), getUsuarioLogado());
			} else if (getContaPagarVO().isTipoSacadoParceiro() && Uteis.isAtributoPreenchido(getContaPagarVO().getParceiro()) && getContaPagarVO().getParceiro().getPermiteenviarremessa()) {
				getFacadeFactory().getContaPagarFacade().preencherDadosRemessaContaPagar(getContaPagarVO(), getContaPagarVO().getParceiro().getNumeroBancoRecebimento(), getContaPagarVO().getParceiro().getNumeroAgenciaRecebimento(), getContaPagarVO().getParceiro().getDigitoAgenciaRecebimento(), getContaPagarVO().getParceiro().getContaCorrenteRecebimento(), getContaPagarVO().getParceiro().getDigitoCorrenteRecebimento(), 
						getContaPagarVO().getParceiro().getChaveEnderecamentoPix() ,getContaPagarVO().getParceiro().getTipoIdentificacaoChavePixEnum(), getUsuarioLogado());
			} else if (getContaPagarVO().isTipoSacadoFuncionario() && Uteis.isAtributoPreenchido(getContaPagarVO().getFuncionario())) {
				getFacadeFactory().getContaPagarFacade().preencherDadosRemessaContaPagar(getContaPagarVO(), getContaPagarVO().getFuncionario().getNumeroBancoRecebimento(), getContaPagarVO().getFuncionario().getNumeroAgenciaRecebimento(), getContaPagarVO().getFuncionario().getDigitoAgenciaRecebimento(), getContaPagarVO().getFuncionario().getContaCorrenteRecebimento(), getContaPagarVO().getFuncionario().getDigitoCorrenteRecebimento(), 
						getContaPagarVO().getFuncionario().getChaveEnderecamentoPix() ,	getContaPagarVO().getFuncionario().getTipoIdentificacaoChavePixEnum(), getUsuarioLogado());
			}			
			if (getContaPagarVO().getBancoRemessaPagar().getNrBanco().equals("033") && (Uteis.isAtributoPreenchido(getContaPagarVO().getFormaPagamentoVO().getCodigo()))) {
					getContaPagarVO().setFormaPagamentoVO(getFacadeFactory().getFormaPagamentoFacade().consultarPorChavePrimaria(getContaPagarVO().getFormaPagamentoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
					switch (getContaPagarVO().getFormaPagamentoVO().getTipoFormaPagamentoEnum()) {
					case BOLETO_BANCARIO:
						getContaPagarVO().setTipoLancamentoContaPagar(TipoLancamentoContaPagarEnum.LIQUIDACAO_TITULO_OUTRO_BANCO);
						break;
					case DEBITO_EM_CONTA_CORRENTE:
						getContaPagarVO().setTipoLancamentoContaPagar(TipoLancamentoContaPagarEnum.CREDITO_CONTA_CORRENTE);
						break;
					case DEPOSITO:
						getContaPagarVO().setTipoLancamentoContaPagar(TipoLancamentoContaPagarEnum.TRANSFERENCIA_OUTRO_BANCO);
						break;
					default:
						break;
					}
					preencherCamposTipoLancamento();
					getContaPagarVO().setTipoServicoContaPagar(TipoServicoContaPagarEnum.PAGAMENTO_DIVERSOS);				
			}else if( getContaPagarVO().getBancoRemessaPagar().getNrBanco().equals("756") && (Uteis.isAtributoPreenchido(getContaPagarVO().getFormaPagamentoVO().getCodigo()))) {
					getContaPagarVO().setFormaPagamentoVO(getFacadeFactory().getFormaPagamentoFacade().consultarPorChavePrimaria(getContaPagarVO().getFormaPagamentoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
					switch (getContaPagarVO().getFormaPagamentoVO().getTipoFormaPagamentoEnum()) {
					case BOLETO_BANCARIO:
						getContaPagarVO().setTipoLancamentoContaPagar(TipoLancamentoContaPagarEnum.LIQUIDACAO_TITULO_PROPRIO_BANCO);
						break;
					case DEBITO_EM_CONTA_CORRENTE:
						getContaPagarVO().setTipoLancamentoContaPagar(TipoLancamentoContaPagarEnum.CREDITO_CONTA_CORRENTE);
						break;
					case DEPOSITO:
						getContaPagarVO().setTipoLancamentoContaPagar(TipoLancamentoContaPagarEnum.TED_OUTRA_TITULARIDADE);
						break;
					default:
						break;
					}
					preencherCamposTipoLancamento();
					getContaPagarVO().setTipoServicoContaPagar(TipoServicoContaPagarEnum.PAGAMENTO_DIVERSOS);				
				}
			else if ((getContaPagarVO().getBancoRemessaPagar().getNrBanco().equals("237")) && (Uteis.isAtributoPreenchido(getContaPagarVO().getFormaPagamentoVO().getCodigo()))) {
				getContaPagarVO().setFormaPagamentoVO(getFacadeFactory().getFormaPagamentoFacade().consultarPorChavePrimaria(getContaPagarVO().getFormaPagamentoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
				switch (getContaPagarVO().getFormaPagamentoVO().getTipoFormaPagamentoEnum()) {
				case BOLETO_BANCARIO:
					getContaPagarVO().setTipoServicoContaPagar(TipoServicoContaPagarEnum.BRADESCO_FATURA);
					getContaPagarVO().setTipoLancamentoContaPagar(TipoLancamentoContaPagarEnum.LIQUIDACAO_TITULO_OUTRO_BANCO);
					break;
				case DEBITO_EM_CONTA_CORRENTE:
					getContaPagarVO().setTipoServicoContaPagar(TipoServicoContaPagarEnum.BRADESCO_OUTROS);
					getContaPagarVO().setTipoLancamentoContaPagar(TipoLancamentoContaPagarEnum.CREDITO_CONTA_CORRENTE);
					break;
				case DEPOSITO:
					getContaPagarVO().setTipoServicoContaPagar(TipoServicoContaPagarEnum.BRADESCO_OUTROS);
					getContaPagarVO().setTipoLancamentoContaPagar(TipoLancamentoContaPagarEnum.TED_OUTRA_TITULARIDADE);
					break;
				default:
					break;
				}
				preencherCamposTipoLancamento();
				getContaPagarVO().setTipoServicoContaPagar(TipoServicoContaPagarEnum.PAGAMENTO_DIVERSOS);	
			} else if (getContaPagarVO().getBancoRemessaPagar().getNrBanco().equals("104") && Uteis.isAtributoPreenchido(getContaPagarVO().getFormaPagamentoVO().getCodigo())) {
				getContaPagarVO().setFormaPagamentoVO(getFacadeFactory().getFormaPagamentoFacade().consultarPorChavePrimaria(getContaPagarVO().getFormaPagamentoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
				switch (getContaPagarVO().getFormaPagamentoVO().getTipoFormaPagamentoEnum()) {
				case BOLETO_BANCARIO:
					getContaPagarVO().setTipoLancamentoContaPagar(TipoLancamentoContaPagarEnum.LIQUIDACAO_TITULO_OUTRO_BANCO);
					break;
				case DEBITO_EM_CONTA_CORRENTE:
					getContaPagarVO().setTipoLancamentoContaPagar(TipoLancamentoContaPagarEnum.CREDITO_CONTA_CORRENTE);
					break;
				case DEPOSITO:
					getContaPagarVO().setTipoLancamentoContaPagar(TipoLancamentoContaPagarEnum.TED_OUTRA_TITULARIDADE);
					break;
				default:
					break;
				}
				preencherCamposTipoLancamento();
				getContaPagarVO().setTipoServicoContaPagar(TipoServicoContaPagarEnum.PAGAMENTO_DIVERSOS);
			}else if( getContaPagarVO().getBancoRemessaPagar().getNrBanco().equals("341") && (Uteis.isAtributoPreenchido(getContaPagarVO().getFormaPagamentoVO().getCodigo()))) {
				getContaPagarVO().setFormaPagamentoVO(getFacadeFactory().getFormaPagamentoFacade().consultarPorChavePrimaria(getContaPagarVO().getFormaPagamentoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
				switch (getContaPagarVO().getFormaPagamentoVO().getTipoFormaPagamentoEnum()) {
				case BOLETO_BANCARIO:				
					getContaPagarVO().setTipoLancamentoContaPagar(TipoLancamentoContaPagarEnum.LIQUIDACAO_TITULO_OUTRO_BANCO);
					break;
				case DEBITO_EM_CONTA_CORRENTE:					
					getContaPagarVO().setTipoLancamentoContaPagar(TipoLancamentoContaPagarEnum.CREDITO_CONTA_CORRENTE);
					break;
				case DEPOSITO:				
					getContaPagarVO().setTipoLancamentoContaPagar(TipoLancamentoContaPagarEnum.TRANSFERENCIA_OUTRO_BANCO);
					break;
				default:
					break;				
				}
				preencherCamposTipoLancamento();
				getContaPagarVO().setTipoServicoContaPagar(TipoServicoContaPagarEnum.PAGAMENTO_DIVERSOS);		
			} else {
				if (Uteis.isAtributoPreenchido(getContaPagarVO().getFormaPagamentoVO().getCodigo())) {
					getContaPagarVO().setFormaPagamentoVO(getFacadeFactory().getFormaPagamentoFacade().consultarPorChavePrimaria(getContaPagarVO().getFormaPagamentoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
				} else {
					getContaPagarVO().setFormaPagamentoVO(new FormaPagamentoVO());
				}
				preencherCamposTipoLancamento();
				getContaPagarVO().setTipoLancamentoContaPagar(null);
				getContaPagarVO().setTipoServicoContaPagar(null);
			}
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void preencherCamposTipoLancamento() {
		try {
			if(Uteis.isAtributoPreenchido(getContaPagarVO().getCodigoBarra())) {
				converterCodigoBarraParaLinhaDigitavel();
			}
			getFacadeFactory().getContaPagarFacade().preencherCamposRemessaPorTipoLancamento(getContaPagarVO());
			verificarBancoDePagamento();
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}

	}

	private void verificarBancoDePagamento() {
		if(getContaPagarVO().getBancoRemessaPagar().getNrBanco().equals("756")
				&& getContaPagarVO().getTipoLancamentoContaPagar() != null
				&& getContaPagarVO().getTipoLancamentoContaPagar().getValor().equals(TipoLancamentoContaPagarEnum.TRANSFERENCIA_OUTRO_BANCO.getValor())) {
			getContaPagarVO().setModalidadeTransferenciaBancariaEnum(ModalidadeTransferenciaBancariaEnum.DOC);
			setBancoDePagamanetoSicoob(true);
		}else if(getContaPagarVO().getBancoRemessaPagar().getNrBanco().equals("756") 
				&& getContaPagarVO().getTipoLancamentoContaPagar() != null
				&& getContaPagarVO().getTipoLancamentoContaPagar().getValor().equals(TipoLancamentoContaPagarEnum.TED_OUTRA_TITULARIDADE.getValor())) {
			getContaPagarVO().setModalidadeTransferenciaBancariaEnum(ModalidadeTransferenciaBancariaEnum.TED);
			setBancoDePagamanetoSicoob(true);
		} else {
			setBancoDePagamanetoSicoob(false);
		}
	}

	public void converterCodigoBarraParaLinhaDigitavel() {
		try {
			if (getContaPagarVO().getTipoLancamentoContaPagar() != null ) {
				GeradorDeLinhaDigitavelOuCodigoBarra gd = new GeradorDeLinhaDigitavelOuCodigoBarra();
				gd.geraLinhaDigitavelApartirCodigoBarra(getContaPagarVO() , true);
			}
//			getLancamentoContabilCentroNegocioVO().recalcularLancmentoValor();
			getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().realizarDistribuicaoValoresCentroResultado(getContaPagarVO().getListaCentroResultadoOrigemVOs(), getContaPagarVO().getPrevisaoValorPago(), getUsuarioLogado());
			
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}
	
	
	public void converterCodigoBarraParaLinhaDigitavelCampoAbaDadosBasicos() {
		try {
			
			GeradorDeLinhaDigitavelOuCodigoBarra gd = new GeradorDeLinhaDigitavelOuCodigoBarra();
			gd.geraLinhaDigitavelApartirCodigoBarra(getContaPagarVO(), false);			
			getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().realizarDistribuicaoValoresCentroResultado(getContaPagarVO().getListaCentroResultadoOrigemVOs(), getContaPagarVO().getPrevisaoValorPago(), getUsuarioLogado());
			
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public void converterLinhaDigitavelParaCodigoBarra() {
		try {
			GeradorDeLinhaDigitavelOuCodigoBarra gd = new GeradorDeLinhaDigitavelOuCodigoBarra();
			if ((getContaPagarVO().getTipoLancamentoContaPagar() != null ) && 
			   ((getContaPagarVO().getTipoLancamentoContaPagar().isPagamentoContasTributosComCodigoBarra()
					&& Uteis.isAtributoPreenchido(getContaPagarVO().getLinhaDigitavel1())
					&& Uteis.isAtributoPreenchido(getContaPagarVO().getLinhaDigitavel2())
					&& Uteis.isAtributoPreenchido(getContaPagarVO().getLinhaDigitavel3())
					&& Uteis.isAtributoPreenchido(getContaPagarVO().getLinhaDigitavel4()))
					||
					(!getContaPagarVO().getTipoLancamentoContaPagar().isPagamentoContasTributosComCodigoBarra()
							&& Uteis.isAtributoPreenchido(getContaPagarVO().getLinhaDigitavel1())
							&& Uteis.isAtributoPreenchido(getContaPagarVO().getLinhaDigitavel2())
							&& Uteis.isAtributoPreenchido(getContaPagarVO().getLinhaDigitavel3())
							&& Uteis.isAtributoPreenchido(getContaPagarVO().getLinhaDigitavel4())
							&& Uteis.isAtributoPreenchido(getContaPagarVO().getLinhaDigitavel5())
							&& Uteis.isAtributoPreenchido(getContaPagarVO().getLinhaDigitavel6())
							&& Uteis.isAtributoPreenchido(getContaPagarVO().getLinhaDigitavel7())
							&& Uteis.isAtributoPreenchido(getContaPagarVO().getLinhaDigitavel8())))) {
				gd.geraCodigoBarraApartirLinhaDigitavel(getContaPagarVO());
			}
//			getLancamentoContabilCentroNegocioVO().recalcularLancmentoValor();
			getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().realizarDistribuicaoValoresCentroResultado(getContaPagarVO().getListaCentroResultadoOrigemVOs(), getContaPagarVO().getPrevisaoValorPago(), getUsuarioLogado());
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public void setListaSelectItemBanco(List<SelectItem> listaSelectItemBanco) {
		this.listaSelectItemBanco = listaSelectItemBanco;
	}

	public List<SelectItem> getComboTipoServicoContaPagar() {
		if (comboTipoServicoContaPagar == null) {
			comboTipoServicoContaPagar = new ArrayList<>();
			Bancos layout = Bancos.getEnum(getContaPagarVO().getBancoRemessaPagar().getNrBanco());
			if (layout != null) {
				for (TipoServicoContaPagarEnum tipoServicoContaPagarEnum : TipoServicoContaPagarEnum.values()) {
					if (tipoServicoContaPagarEnum.isTipoServicoPorNrBanco(layout.getNumeroBanco())
							|| (tipoServicoContaPagarEnum.isTipoServicoPorNrBanco("033") && layout.getNumeroBanco().equals("756"))) {
						if (comboTipoServicoContaPagar.isEmpty() && !Uteis.isAtributoPreenchido(getContaPagarVO().getTipoServicoContaPagar())) {
							getContaPagarVO().setTipoServicoContaPagar(tipoServicoContaPagarEnum);
						}
						comboTipoServicoContaPagar.add(new SelectItem(tipoServicoContaPagarEnum, tipoServicoContaPagarEnum.getDescricao()));
					}
				}
				comboTipoServicoContaPagar.sort(Comparator.comparing(SelectItem::getLabel));
			}
		}
		return comboTipoServicoContaPagar;
	}

	public void setComboTipoServicoContaPagar(List<SelectItem> comboTipoServicoContaPagar) {
		this.comboTipoServicoContaPagar = comboTipoServicoContaPagar;
	}

	public List<SelectItem> getComboTipoLancamentoContaPagar() {
		if (comboTipoLancamentoContaPagar == null) {
			comboTipoLancamentoContaPagar = new ArrayList<>();
			Bancos layout = Bancos.getEnum(getContaPagarVO().getBancoRemessaPagar().getNrBanco());
			if (layout != null) {
				for (TipoLancamentoContaPagarEnum tipoLancamentoContaPagarEnum : TipoLancamentoContaPagarEnum.values()) {
					if (tipoLancamentoContaPagarEnum.isTipoLancamentoPorNrBanco(layout.getNumeroBanco())) {
						if (comboTipoLancamentoContaPagar.isEmpty() && !Uteis.isAtributoPreenchido(getContaPagarVO().getTipoLancamentoContaPagar())) {
							getContaPagarVO().setTipoLancamentoContaPagar(tipoLancamentoContaPagarEnum);
						}						
						if(!tipoLancamentoContaPagarEnum.isDepreciado()) {							
							comboTipoLancamentoContaPagar.add(new SelectItem(tipoLancamentoContaPagarEnum, tipoLancamentoContaPagarEnum.getDescricao()));	
						}
					}
				}				
			}
		}
		return comboTipoLancamentoContaPagar;
	}

	public void setComboTipoLancamentoContaPagar(List<SelectItem> comboTipoLancamentoContaPagar) {
		this.comboTipoLancamentoContaPagar = comboTipoLancamentoContaPagar;
	}

	public void montarListaSelectItemFormaPagamento() {
		List<FormaPagamentoVO> resultadoConsulta = null;
		try {
			resultadoConsulta = getFacadeFactory().getFormaPagamentoFacade().consultarPorNome("", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			getComboFormaPagamento().clear();
			getComboFormaPagamento().add(new SelectItem(0, ""));
			resultadoConsulta.stream().filter(p -> p.isBoletoBancario() || p.isDebitoEmConta() || p.isDeposito())
					.forEach(p -> getComboFormaPagamento().add(new SelectItem(p.getCodigo(), p.getNome())));
		} catch (Exception e) {
			throw new StreamSeiException(e);
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
		}
	}

	public void montarListaSelectItemBancoFavorecido() {
		List<BancoVO> resultadoConsulta = null;
		try {
			resultadoConsulta = getFacadeFactory().getBancoFacade().consultarPorNome("", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			getListaSelectItemBancoFavorecido().clear();
			getListaSelectItemBancoFavorecido().add(new SelectItem(0, ""));
			resultadoConsulta.stream().forEach(p -> getListaSelectItemBancoFavorecido().add(new SelectItem(p.getCodigo(), p.getNome())));
		} catch (Exception e) {
			throw new StreamSeiException(e);
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
		}
	}

	public List<SelectItem> getComboFormaPagamento() {
		if (comboFormaPagamento == null) {
			comboFormaPagamento = new ArrayList<>();
		}
		return comboFormaPagamento;
	}

	public void setComboFormaPagamento(List<SelectItem> comboFormaPagamento) {
		this.comboFormaPagamento = comboFormaPagamento;
	}

	public List<SelectItem> getListaSelectItemBancoFavorecido() {
		listaSelectItemBancoFavorecido = Optional.ofNullable(listaSelectItemBancoFavorecido).orElse(new ArrayList<>());
		return listaSelectItemBancoFavorecido;
	}

	public void setListaSelectItemBancoFavorecido(List<SelectItem> listaSelectItemBancoFavorecido) {
		this.listaSelectItemBancoFavorecido = listaSelectItemBancoFavorecido;
	}

	public Double getTotalNegociado() {
		if (totalNegociado == null) {
			totalNegociado = 0.0;
		}
		return totalNegociado;
	}

	public void setTotalNegociado(Double totalNegociado) {
		this.totalNegociado = totalNegociado;
	}
	
	/**
	 * INICIO MERGE EDIGAR 22/05/2018
	 */
	private List<SelectItem> listaSelectItemTipoOrigemContaPagar;
	
	/*
	 * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo <code>situacao</code>
	 */
	public List<SelectItem> getListaSelectItemTipoOrigemContaPagar() throws Exception {
		if (listaSelectItemTipoOrigemContaPagar != null) {
			return listaSelectItemTipoOrigemContaPagar;
		}
		List<SelectItem> listaSelectItemTipoOrigemContaPagar = new ArrayList<SelectItem>();
		listaSelectItemTipoOrigemContaPagar.add(new SelectItem(OrigemContaPagar.REGISTRO_MANUAL.getValor(), UtilSelectItem.internacionalizarEnum(OrigemContaPagar.REGISTRO_MANUAL)));				
		listaSelectItemTipoOrigemContaPagar.add(new SelectItem(OrigemContaPagar.ADIANTAMENTO.getValor(), UtilSelectItem.internacionalizarEnum(OrigemContaPagar.ADIANTAMENTO)));				
		return listaSelectItemTipoOrigemContaPagar;
	}
	
	public void prepararListarAdiantamentosUtilizadosBaixarConta() {
		setMensagemID("msg_dados_consultados");
	}
	
	public Boolean getApresentarBotaoAdiantamentosUtilizados() {
		if ((this.getContaPagarVO().getDescontoPorUsoAdiantamento().doubleValue() > 0.0)) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
	public void liberarRegistroCompetenciaFechada() {
		try {
			this.getContaPagarVO().setBloqueioPorFechamentoMesLiberado(Boolean.TRUE);		
			FechamentoMesHistoricoModificacaoVO historico = getFacadeFactory().getFechamentoMesHistoricoModificacaoFacade().gerarNovoHistoricoModificacao(this.getContaPagarVO().getFechamentoMesVOBloqueio(), getUsuarioLogado(), TipoOrigemHistoricoBloqueioEnum.APAGAR, this.getContaPagarVO().getDescricaoBloqueio(), this.getContaPagarVO().toString());
			getFacadeFactory().getFechamentoMesHistoricoModificacaoFacade().incluir(historico, getUsuarioLogado());
			setMensagemID("msg_registro_liberado_mes");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			this.getContaPagarVO().setBloqueioPorFechamentoMesLiberado(Boolean.FALSE);
		}
	}
	
	public void verificarPermissaoLiberarBloqueioCompetencia() {
		try {
			UsuarioVO usuarioVerif = ControleAcesso.verificarLoginUsuario(this.getUsernameLiberacaoBloqueioPorFechamentoMes(), this.getSenhaLiberacaoBloqueioPorFechamentoMes(), true, Uteis.NIVELMONTARDADOS_TODOS);
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("FuncionarioMes_liberarBloqueioIncluirAlterarContaPagar", usuarioVerif);
			liberarRegistroCompetenciaFechada();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
 	}	
	
    public Boolean getApresentarBotaoLiberarBloqueio() {
    	return this.getContaPagarVO().getApresentarBotaoLiberarBloqueioFechamentoMes();
    }	
    
	public void alterarDataReferencia() {
		getContaPagarVO().reiniciarControleBloqueioCompetencia();
	}

	public List<SelectItem> getListaSelectItemBancoRemessaBancaria() {
		if (listaSelectItemBancoRemessaBancaria == null) {
			listaSelectItemBancoRemessaBancaria = new ArrayList<>();
		}
		return listaSelectItemBancoRemessaBancaria;
	}

	public void setListaSelectItemBancoRemessaBancaria(List<SelectItem> listaSelectItemBancoRemessaBancaria) {
		this.listaSelectItemBancoRemessaBancaria = listaSelectItemBancoRemessaBancaria;
	}    
	
	public void montarListaSelectItemBancosRemessaBancaria() {
		List objs = new ArrayList(0);
		try {
			List resultadoConsulta = consultarBancoPorNome("");
			Iterator i = resultadoConsulta.iterator();
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				BancoVO obj = (BancoVO) i.next();
				BancoEnum banco = BancoEnum.getEnum("CNAB240", obj.getNrBanco());
				if (banco != null && banco.getPossuiRemessaContaPagar()) {
					objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
		setListaSelectItemBancoRemessaBancaria(objs);
	}

	public boolean getExisteContaPagarControleRemessa() {
		return existeContaPagarControleRemessa;
	}

	public void setExisteContaPagarControleRemessa(boolean existeContaPagarControleRemessa) {
		this.existeContaPagarControleRemessa = existeContaPagarControleRemessa;
	}

	public ContaPagarControleRemessaContaPagarVO getContaPagarControleRemessaContaPagarVO() {
		if (contaPagarControleRemessaContaPagarVO == null) {
			contaPagarControleRemessaContaPagarVO = new ContaPagarControleRemessaContaPagarVO();
		}
		return contaPagarControleRemessaContaPagarVO;
	}

	public void setContaPagarControleRemessaContaPagarVO(
			ContaPagarControleRemessaContaPagarVO contaPagarControleRemessaContaPagarVO) {
		this.contaPagarControleRemessaContaPagarVO = contaPagarControleRemessaContaPagarVO;
	}
	private void validarValorPorcentagemCentroResultadoOrigem(CentroResultadoOrigemVO centroResultadoOrigemVO) throws ConsistirException {
		Double valor = Uteis.arrendondarForcando2CadasDecimais(centroResultadoOrigemVO.getValor());
		if (valor.doubleValue() == 0.00) {
			throw new ConsistirException("O valor informado/gerado é menor que R$ 0,01.");
		}
	}
	
	private Integer indiceSubstituirCentroResultadoLista(List<CentroResultadoOrigemVO> listaCentroResultadoOrigem) {
		return IntStream.range(0, listaCentroResultadoOrigem.size()).filter(i -> listaCentroResultadoOrigem.get(i).isEdicaoManual()).findFirst().orElse(-1);
	}

	public boolean isBancoDePagamanetoSicoob() {
		return bancoDePagamanetoSicoob;
	}

	public void setBancoDePagamanetoSicoob(boolean bancoDePagamanetoSicoob) {
		this.bancoDePagamanetoSicoob = bancoDePagamanetoSicoob;
	}

	public String getValorConsultafaixaValorMenor() {
		if(valorConsultafaixaValorMenor == null) {
			valorConsultafaixaValorMenor ="";
		}
		return valorConsultafaixaValorMenor;
	}

	public void setValorConsultafaixaValorMenor(String valorConsultafaixaValorMenor) {
		getListConsulta().add(0, valorConsultafaixaValorMenor);
		this.valorConsultafaixaValorMenor = valorConsultafaixaValorMenor;
	}

	public String getValorConsultafaixaValorMaior() {
		if(valorConsultafaixaValorMaior == null) {
			valorConsultafaixaValorMaior = "";
		}
		return valorConsultafaixaValorMaior;
	}

	public void setValorConsultafaixaValorMaior(String valorConsultafaixaValorMaior) {
		getListConsulta().add(1, valorConsultafaixaValorMaior);
		this.valorConsultafaixaValorMaior = valorConsultafaixaValorMaior;
	}

	public List<String> getListConsulta() {
		if(listConsulta == null ) {
			listConsulta = new ArrayList<String>();
		}
		return listConsulta;
	}

	public void setListConsulta(List<String> listConsulta) {
		this.listConsulta = listConsulta;
	}

	public void addCentroResultadoOrigemConformeRateioCategoriaDespesa() {
		try {
			getCentroResultadoOrigemVO().setQuantidade(1.0);
			getCentroResultadoOrigemVO().setTipoMovimentacaoCentroResultadoOrigemEnum(TipoMovimentacaoCentroResultadoOrigemEnum.ENTRADA);
			getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().adicionarCentroResultadoOrigemPorRateioCategoriaDespesa(getContaPagarVO().getListaCentroResultadoOrigemVOs(), getCentroResultadoOrigemVO(), getContaPagarVO().getPrevisaoValorPago(), true, getUsuarioLogado());
			setCentroResultadoOrigemVO(new CentroResultadoOrigemVO());
			setMensagemID(MSG_TELA.msg_dados_adicionados.name());
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada(MSG_TELA.msg_erro.name(), e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public DataModelo getDataModeloAluno() {
		if (dataModeloAluno == null) {
			dataModeloAluno = new DataModelo();
		}
		return dataModeloAluno;
	}

	public void setDataModeloAluno(DataModelo dataModeloAluno) {
		this.dataModeloAluno = dataModeloAluno;
	}
	
	public String getSizeChaveEnderecamentoPix() {
		return getSizeChavePix(getContaPagarVO().getTipoIdentificacaoChavePixEnum());
		
	}
	
	public List<SelectItem> getComboTipoIdentificacaoChavePixEnum() {		
		comboTipoIdentificacaoChavePixEnum = null ;
		comboTipoIdentificacaoChavePixEnum = new ArrayList<>();
		Bancos layout = Bancos.getEnum(getContaPagarVO().getBancoRemessaPagar().getNrBanco());
		if (layout != null) {
			for (TipoIdentificacaoChavePixEnum tipoIdentificacaoChavePixEnum : TipoIdentificacaoChavePixEnum.values()) {					
				if (tipoIdentificacaoChavePixEnum.equals(TipoIdentificacaoChavePixEnum.DADOS_BANCARIOS)) {		
					if(!layout.getNumeroBanco().equals("341")) {							
						comboTipoIdentificacaoChavePixEnum.add(new SelectItem(tipoIdentificacaoChavePixEnum, tipoIdentificacaoChavePixEnum.getDescricao()));
					}
				}else {
					comboTipoIdentificacaoChavePixEnum.add(new SelectItem(tipoIdentificacaoChavePixEnum, tipoIdentificacaoChavePixEnum.getDescricao()));	
				}
			}				
		}
	
	return comboTipoIdentificacaoChavePixEnum;
}

public void setComboTipoIdentificacaoChavePixEnum(List<SelectItem> comboTipoIdentificacaoChavePixEnum) {
	this.comboTipoIdentificacaoChavePixEnum = comboTipoIdentificacaoChavePixEnum;
}
	
	
	
}
