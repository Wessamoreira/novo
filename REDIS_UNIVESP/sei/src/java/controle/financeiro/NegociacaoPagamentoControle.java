package controle.financeiro;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas negociacaoPagamentoForm.jsp
 * negociacaoPagamentoCons.jsp) com as funcionalidades da classe <code>NegociacaoPagamento</code>. Implemtação da camada
 * controle (Backing Bean).
 * 
 * @see SuperControle
 * @see NegociacaoPagamento
 * @see NegociacaoPagamentoVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import controle.arquitetura.SelectItemOrdemValor;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.contabil.FechamentoMesHistoricoModificacaoVO;
import negocio.comuns.contabil.LancamentoContabilVO;
import negocio.comuns.contabil.enumeradores.TipoOrigemHistoricoBloqueioEnum;
import negocio.comuns.financeiro.BancoVO;
import negocio.comuns.financeiro.ChequeVO;
import negocio.comuns.financeiro.ConciliacaoContaCorrenteVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroCartaoVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ContaPagarAdiantamentoVO;
import negocio.comuns.financeiro.ContaPagarNegociacaoPagamentoVO;
import negocio.comuns.financeiro.ContaPagarVO;
import negocio.comuns.financeiro.FormaPagamentoNegociacaoPagamentoVO;
import negocio.comuns.financeiro.NegociacaoPagamentoVO;
import negocio.comuns.financeiro.OperadoraCartaoVO;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoCheque;
import negocio.comuns.utilitarias.dominios.TipoFormaPagamento;
import negocio.comuns.utilitarias.dominios.TipoSacado;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.financeiro.Cheque;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.controle.financeiro.ComprovantePagamentoRelControle;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;


@Controller("NegociacaoPagamentoControle")
@Scope("viewScope")
@Lazy
public class NegociacaoPagamentoControle extends SuperControleRelatorio implements Serializable {

	private List<SelectItem> listaSelectItemCaixa;
	private List<SelectItem> listaSelectItemFormaPagamento;
	private List<SelectItem> listaSelectItemBanco;
	private List<SelectItem> listaSelectItemContaCorrente;
	private List<SelectItem> listaSelectItemFornecedor;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private List<SelectItem> listaSelectItemConfiguracaoFinanceiroCartao;
	private ContaPagarNegociacaoPagamentoVO contaPagarNegociacaoPagamentoVO;
	private FormaPagamentoNegociacaoPagamentoVO formaPagamentoNegociacaoPagamentoVO;
	private NegociacaoPagamentoVO negociacaoPagamentoVO;
	private ConfiguracaoFinanceiroCartaoVO configuracaoFinanceiroCartaoVO;
	private ChequeVO chequeVO;
	private String campoConsultarContaPagar;
	private Date dataInicioConsultarContaPagar;
	private Date dataTerminoConsultarContaPagar;
	private List listaConsultarContaPagar;
	protected int ValorConsultaUnidadeEnsino;
	protected String valorConsultaCheque;
	protected String campoConsultaCheque;
	protected List listaConsultaCheque;
	private FornecedorVO fornecedor;
	private FuncionarioVO funcionario;
	protected BancoVO banco;
	protected PessoaVO aluno;
	protected Boolean botaoExcluir;
	private List listaConsultaFuncionario;
	private String valorConsultaFuncionario;
	private String campoConsultaFuncionario;
	private String valorConsultaFornecedor;
	private String campoConsultaFornecedor;
	private List listaConsultaFornecedor;
	private List listaConsultaParceiro;
	private String valorConsultaParceiro;
	private String campoConsultaParceiro;
	protected ComprovantePagamentoRelControle comprovantePagamentoRelControle;

	private OperadoraCartaoVO operadoraCartao;
	private String campoConsultaOperadoraCartao;
	private String valorConsultaOperadoraCartao;
	private List<OperadoraCartaoVO> listaConsultaOperadoraCartao;
	private List<ChequeVO> cheques = new ArrayList<ChequeVO>();
	public List<ContaCorrenteVO> contasCorrenteVO = new ArrayList<ContaCorrenteVO>();
	private ConciliacaoContaCorrenteVO conciliacaoContaCorrenteVO;
	private LancamentoContabilVO lancamentoContabil;
	private List<SelectItem> listaSelectItemFormaPagamentoTroco;
	private Boolean utilizarDataAdiantamento;	
	private DataModelo dataModeloAluno;

	public NegociacaoPagamentoControle() throws Exception {
		// obterUsuarioLogado();
		setControleConsulta(new ControleConsulta());
		getControleConsulta().setCampoConsulta("nomeSacado");
		setMensagemID("msg_entre_prmconsulta");
		montarListaSelectItemUnidadeEnsino();
	}

	/**
	 * Rotina responsavel por criar um novo PAGAMENTO com os dados ja inseridos
	 * de uma CONTA A PAGAR.
	 *
	 * @return
	 */
	// public String efetuarPagamento() {
	// ContaPagarVO contaPagar = (ContaPagarVO)
	// context().getExternalContext().getRequestMap().get("contaPagarItens");
	// novo();
	// getNegociacaoPagamentoVO().setUnidadeEnsino(contaPagar.getUnidadeEnsino());
	// getNegociacaoPagamentoVO().setTipoSacado(contaPagar.getTipoSacado());
	// getNegociacaoPagamentoVO().setFornecedor(contaPagar.getFornecedor());
	// getNegociacaoPagamentoVO().setFuncionario(contaPagar.getFuncionario());
	// getNegociacaoPagamentoVO().setBanco(contaPagar.getBanco());
	// getNegociacaoPagamentoVO().setAluno(contaPagar.getPessoa());
	// getNegociacaoPagamentoVO().setResponsavel(contaPagar.getResponsavel());
	//
	// montarListaSelectItemCaixa();
	// fornecedor = contaPagar.getFornecedor();
	// funcionario = contaPagar.getFuncionario();
	// banco = contaPagar.getBanco();
	// aluno = contaPagar.getPessoa();
	//
	// if (!contaPagar.isQuitada()) {
	// adicionarContaPagarFornecedor(contaPagar);
	// } else {
	// setMensagemDetalhada("msg_erro", "Não é possível pagar uma Conta a Pagar
	// QUITADA ou RE-NEGOCIADA.");
	// }
	// System.out.println(
	// getNegociacaoPagamentoVO().getUnidadeEnsino().getNome()+" "+
	// getNegociacaoPagamentoVO().getResponsavel().getNome());
	// return
	// Uteis.getCaminhoRedirecionamentoNavegacao("negociacaoPagamentoForm");
	// }

	@PostConstruct
	public void realizarCarregamentoPagamentoVindoTelaConciliacaoBancaria() {
		Boolean realizarEstornoConciliacaoBancaria = (Boolean) context().getExternalContext().getSessionMap().get("realizarEstornoConciliacaoBancaria");
		try {
			if (realizarEstornoConciliacaoBancaria != null && realizarEstornoConciliacaoBancaria) {
				NegociacaoPagamentoVO obj = (NegociacaoPagamentoVO) context().getExternalContext().getSessionMap().get("negociacaoConciliacaoBancaria");
				if (Uteis.isAtributoPreenchido(obj)) {
					carregarDadosParaEditarNegociacaoPagamento(obj);
				}
				setConciliacaoContaCorrenteVO((ConciliacaoContaCorrenteVO) context().getExternalContext().getSessionMap().get("conciliacaoBancaria"));
				setMensagemID("msg_dados_editar", Uteis.ALERTA);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			context().getExternalContext().getSessionMap().remove("realizarEstornoConciliacaoBancaria");
			context().getExternalContext().getSessionMap().remove("negociacaoConciliacaoBancaria");
			context().getExternalContext().getSessionMap().remove("conciliacaoBancaria");
		}
	}

	@PostConstruct
	public void efetuarPagamento() {
		Boolean origemContaPagarControle = (Boolean) context().getExternalContext().getSessionMap().get("origemContaPagarControle");
		try {
			if (origemContaPagarControle != null && origemContaPagarControle == true) {
				ContaPagarVO contaPagar = (ContaPagarVO) context().getExternalContext().getSessionMap().get("contaPagarItens");
				novo();
				getNegociacaoPagamentoVO().setUnidadeEnsino(contaPagar.getUnidadeEnsino());
				getNegociacaoPagamentoVO().setTipoSacado(contaPagar.getTipoSacado());
				getNegociacaoPagamentoVO().setFornecedor(contaPagar.getFornecedor());
				getNegociacaoPagamentoVO().setFuncionario(contaPagar.getFuncionario());
				getNegociacaoPagamentoVO().setBanco(contaPagar.getBanco());
				getNegociacaoPagamentoVO().setAluno(contaPagar.getPessoa());
				getNegociacaoPagamentoVO().setOperadoraCartao(contaPagar.getOperadoraCartao());
				setOperadoraCartao(contaPagar.getOperadoraCartao());
				
				montarListaSelectItemCaixa();
				fornecedor = contaPagar.getFornecedor();
				funcionario = contaPagar.getFuncionario();
				banco = contaPagar.getBanco();
				aluno = contaPagar.getPessoa();

				if (!contaPagar.isQuitada()) {
					adicionarContaPagarFornecedor(contaPagar);
				} else {
					setMensagemDetalhada("msg_erro", "Não é possível pagar uma Conta a Pagar QUITADA ou RE-NEGOCIADA.");
				}
				context().getExternalContext().getSessionMap().remove("contaPagarItens");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			context().getExternalContext().getSessionMap().remove("origemContaPagarControle");
		}
	}

	public void inicializarResponsavel() {
		try {
			getNegociacaoPagamentoVO().getResponsavel().setCodigo(getUsuarioLogado().getCodigo());
			getNegociacaoPagamentoVO().getResponsavel().setNome(getUsuarioLogado().getNome());
		} catch (Exception e) {
			getNegociacaoPagamentoVO().setResponsavel(new UsuarioVO());
		}

	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe
	 * <code>NegociacaoPagamento</code> para edição pelo usuário da aplicação.
	 */
	public String novo() {
		setNegociacaoPagamentoVO(new NegociacaoPagamentoVO());

		setFormaPagamentoNegociacaoPagamentoVO(new FormaPagamentoNegociacaoPagamentoVO());
		setContaPagarNegociacaoPagamentoVO(new ContaPagarNegociacaoPagamentoVO());
		setChequeVO(new ChequeVO());
		setBotaoExcluir(Boolean.FALSE);
		inicializarResponsavel();
		inicializarListasSelectItemTodosComboBox();
		getNegociacaoPagamentoVO().setDataRegistro(new Date());

		setFuncionario(new FuncionarioVO());
		setBanco(new BancoVO());
		setFornecedor(new FornecedorVO());
		setMensagemDetalhada("");
		setListaConsultaFornecedor(new ArrayList<FornecedorVO>(0));
		setValorTotalConsideradoDistribuicaoAdiantamentos(0.0);
		setApresentarAdiantamentosDisponiveisDistribuicao(Boolean.FALSE);		

		setMensagemID("msg_entre_dados");
		setDataModeloAluno(new DataModelo());
		return Uteis.getCaminhoRedirecionamentoNavegacao("negociacaoPagamentoForm.xhtml");
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe
	 * <code>NegociacaoPagamento</code> para alteração. O objeto desta classe é
	 * disponibilizado na session da página (request) para que o JSP
	 * correspondente possa disponibilizá-lo para edição.
	 */
	public String editar() {
		try {
			NegociacaoPagamentoVO obj = (NegociacaoPagamentoVO) context().getExternalContext().getRequestMap().get("negociacaoPagamentoItens");
			obj = getFacadeFactory().getNegociacaoPagamentoFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			carregarDadosParaEditarNegociacaoPagamento(obj);

			setMensagemID("msg_dados_editar");
			return Uteis.getCaminhoRedirecionamentoNavegacao("negociacaoPagamentoForm.xhtml");
		} catch (Exception e) {	
			e.printStackTrace();
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("negociacaoPagamentoCons.xhtml");
		}
	}

	public void carregarDadosParaEditarNegociacaoPagamento(NegociacaoPagamentoVO obj) throws Exception {
		obj.setNovoObj(Boolean.FALSE);
		setNegociacaoPagamentoVO(obj);
		getNegociacaoPagamentoVO().atualizarChequePagamento();
		setFormaPagamentoNegociacaoPagamentoVO(new FormaPagamentoNegociacaoPagamentoVO());
		setContaPagarNegociacaoPagamentoVO(new ContaPagarNegociacaoPagamentoVO());
		setChequeVO(new ChequeVO());
		setBotaoExcluir(Boolean.FALSE);
		inicializarListasSelectItemTodosComboBox();
		if (getNegociacaoPagamentoVO().getTipoSacado().equals("BA")) {
			setBanco(getNegociacaoPagamentoVO().getBanco());
		}
		if (getNegociacaoPagamentoVO().getTipoSacado().equals("FU")) {
			setFuncionario(getNegociacaoPagamentoVO().getFuncionario());
		}
		if (getNegociacaoPagamentoVO().getTipoSacado().equals("FO")) {
			setFornecedor(getNegociacaoPagamentoVO().getFornecedor());
		}
		if (getNegociacaoPagamentoVO().getTipoSacado().equals("AL")) {
			setAluno(getNegociacaoPagamentoVO().getAluno());
		}
		if (getNegociacaoPagamentoVO().getTipoSacado().equals(TipoSacado.OPERADORA_CARTAO.getValor())) {
			setOperadoraCartao(getNegociacaoPagamentoVO().getOperadoraCartao());
		}

	}

	public String getExibirModalMotivo() {
		if (getNegociacaoPagamentoVO().getApresentarModalMotivo()) {
			return "RichFaces.$('panelMotivo').show();";
		}
		if (getBotaoExcluir() && !getNegociacaoPagamentoVO().getApresentarModalMotivo()) {
			return "RichFaces.$('panelExcluir').show();";
		}
		return "RichFaces.$('panelMotivo').hide(); RichFaces.$('panelExcluir').hide();";
	}

	public Boolean getApresentarCamposFornecedor() {
		try {
			return negociacaoPagamentoVO.getTipoSacado().equals("FO");
		} catch (Exception e) {
			return false;
		}
	}

	public Boolean getApresentarCamposOperadoraCartao() {
		try {
			return negociacaoPagamentoVO.getTipoSacado().equals("OC");
		} catch (Exception e) {
			return false;
		}
	}

	public Boolean getApresentarCamposBanco() {
		try {
			return negociacaoPagamentoVO.getTipoSacado().equals("BA");
		} catch (Exception e) {
			return false;
		}
	}

	public void setApresentarCamposFornecedor(Boolean b) {
	}

	public void setApresentarCamposFuncionario(Boolean b) {
	}

	public Boolean getApresentarCamposFuncionario() {
		try {
			return negociacaoPagamentoVO.getTipoSacado().equals("FU");
		} catch (Exception e) {
			return false;
		}
	}

	public Boolean getApresentarCamposAluno() {
		try {
			return negociacaoPagamentoVO.getTipoSacado().equals("AL");
		} catch (Exception e) {
			return false;
		}
	}

	public List getTipoConsultaComboAluno() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nomePessoa", "Aluno"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		return itens;
	}

	@SuppressWarnings("deprecation")
	public void consultarAluno() {
		try {
			if (getDataModeloAluno().getValorConsulta().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			List<MatriculaVO> objs = new ArrayList<>(0);
			if (getDataModeloAluno().getCampoConsulta().equals("matricula")) {
				objs.add(getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(getDataModeloAluno().getValorConsulta(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
				getDataModeloAluno().setListaConsulta(objs);
				if (Uteis.isAtributoPreenchido(objs) && Uteis.isAtributoPreenchido(objs.get(0).getAluno().getNome())) {					
					getDataModeloAluno().setTotalRegistrosEncontrados(1);
				} else {
					getDataModeloAluno().setTotalRegistrosEncontrados(0);
				}
			}
			if (getDataModeloAluno().getCampoConsulta().equals("nomePessoa")) {
				getDataModeloAluno().setLimitePorPagina(10);
				getFacadeFactory().getMatriculaFacade().consultarMatriculas(getDataModeloAluno(), getUsuarioLogado());
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarAluno() {
		MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
		this.setAluno(obj.getAluno());
		setDataModeloAluno(new DataModelo());
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto
	 * da classe <code>NegociacaoPagamento</code>. Caso o objeto seja novo
	 * (ainda não gravado no BD) é acionado a operação <code>incluir()</code>.
	 * Caso contrário é acionado o <code>alterar()</code>. Se houver alguma
	 * inconsistência o objeto não é gravado, sendo re-apresentado para o
	 * usuário juntamente com uma mensagem de erro.
	 */
	public void gravar() {
		try {
			setBotaoExcluir(false);
			if (getNegociacaoPagamentoVO().getApresentarModalMotivo()) {
				return;
			}

			if (getBanco() != null && !getBanco().isNovoObj()) {
				negociacaoPagamentoVO.setBanco(banco);
			}
			if (getFuncionario() != null && !getFuncionario().isNovoObj()) {
				negociacaoPagamentoVO.setFuncionario(funcionario);
			}
			if (getFornecedor() != null && !getFornecedor().isNovoObj()) {
				negociacaoPagamentoVO.setFornecedor(fornecedor);
			}
			if (getAluno() != null && !getAluno().isNovoObj()) {
				negociacaoPagamentoVO.setAluno(aluno);
			}
			if (getOperadoraCartao() != null && !getOperadoraCartao().isNovoObj()) {
				negociacaoPagamentoVO.setOperadoraCartao(getOperadoraCartao());
			}
			NegociacaoPagamentoVO.validarDados(getNegociacaoPagamentoVO());
			if (negociacaoPagamentoVO.getAlterouConteudo()) {
				if (negociacaoPagamentoVO.isNovoObj().booleanValue()) {
					getFacadeFactory().getNegociacaoPagamentoFacade().incluir(negociacaoPagamentoVO, getUsuarioLogado());
				} else {
					getFacadeFactory().getNegociacaoPagamentoFacade().alterar(negociacaoPagamentoVO, getUsuarioLogado());
				}
				negociacaoPagamentoVO.setAlterouConteudo(Boolean.FALSE);
				negociacaoPagamentoVO.setMotivoAlteracao("");
			}
			getNegociacaoPagamentoVO().reiniciarControleBloqueioCompetencia();
			setMensagemID("msg_dados_gravados");

		} catch (Exception e) {
			if (e.getMessage().contains("fn_validarintegridadesituacaocontapagar")) {
				setMensagemDetalhada("msg_erro", "Conta Pagar Está Vinculada a Uma Negociação e a Situação é Diferente de NEGOCIADA, Consulte Novamente a Conta.");
			} else {
				setMensagemDetalhada("msg_erro", e.getMessage());
			}

		}
	}

	public void scrollerListener(DataScrollEvent dataScrollEvent) {
		getControleConsultaOtimizado().setPaginaAtual(dataScrollEvent.getPage());
		getControleConsultaOtimizado().setPage(dataScrollEvent.getPage());
		consultar();
	}
	
	public void scrollerListenerAluno(DataScrollEvent dataScrollEvent) {
		getDataModeloAluno().setPaginaAtual(dataScrollEvent.getPage());
		getDataModeloAluno().setPage(dataScrollEvent.getPage());
		consultarAluno();
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP
	 * NegociacaoPagamentoCons.jsp. Define o tipo de consulta a ser executada,
	 * por meio de ComboBox denominado campoConsulta, disponivel neste mesmo
	 * JSP. Como resultado, disponibiliza um List com os objetos selecionados na
	 * sessao da pagina.
	 */
	@Override
	public String consultar() {
		try {
			super.consultar();
			getControleConsultaOtimizado().getListaConsulta().clear();
			getControleConsultaOtimizado().setLimitePorPagina(10);
			List objs = new ArrayList(0);
			if (getControleConsulta().getCampoConsulta().equals("codigo")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				if(!Uteis.getIsValorNumerico(getControleConsulta().getValorConsulta())) {
					throw new Exception(getMensagemInternalizacao("msg_validarSomenteNumeroString"));
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getNegociacaoPagamentoFacade().consultarPorCodigo(new Integer(valorInt), getControleConsulta().getDataIni(), getControleConsulta().getDataFim(), getValorUnidadeEnsino(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getNegociacaoPagamentoFacade().consultarTotalPorCodigo(new Integer(valorInt), getControleConsulta().getDataIni(), getControleConsulta().getDataFim(), getValorUnidadeEnsino(), true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("aluno")) {
				if (getControleConsulta().getValorConsulta().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getNegociacaoPagamentoFacade().consultaRapidaPorNomeAluno(getControleConsulta().getValorConsulta(), getControleConsulta().getCampoConsulta(), getControleConsulta().getDataIni(), getControleConsulta().getDataFim(), getValorUnidadeEnsino(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getNegociacaoPagamentoFacade().consultaRapidaTotalRegistrosPorNomeAluno(getControleConsulta().getValorConsulta(), getControleConsulta().getCampoConsulta(), getControleConsulta().getDataIni(), getControleConsulta().getDataFim(), getValorUnidadeEnsino(), true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));

			}
			if (getControleConsulta().getCampoConsulta().equals("numeroContaCorrente")) {
				objs = getFacadeFactory().getNegociacaoPagamentoFacade().consultarPorNumeroContaCorrente(getControleConsulta().getValorConsulta(), getControleConsulta().getDataIni(), getControleConsulta().getDataFim(), getValorUnidadeEnsino(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getNegociacaoPagamentoFacade().consultarPorNumeroContaCorrenteTotalRegistros(getControleConsulta().getValorConsulta(), getControleConsulta().getDataIni(), getControleConsulta().getDataFim(), getValorUnidadeEnsino(), true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeFornecedor")) {
				if (getControleConsulta().getValorConsulta().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getNegociacaoPagamentoFacade().consultaRapidaPorNomeFornecedor(getControleConsulta().getValorConsulta(), getControleConsulta().getCampoConsulta(), getControleConsulta().getDataIni(), getControleConsulta().getDataFim(), getValorUnidadeEnsino(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getNegociacaoPagamentoFacade().consultaRapidaPorNomeFornecedorTotalRegistros(getControleConsulta().getValorConsulta(), getControleConsulta().getCampoConsulta(), getControleConsulta().getDataIni(), getControleConsulta().getDataFim(), getValorUnidadeEnsino(), true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeResponsavelFinanceiro")) {
				if (getControleConsulta().getValorConsulta().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getNegociacaoPagamentoFacade().consultaRapidaPorNomeResponsavelFinanceiro(getControleConsulta().getValorConsulta(), getControleConsulta().getCampoConsulta(), getControleConsulta().getDataIni(), getControleConsulta().getDataFim(), getValorUnidadeEnsino(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getNegociacaoPagamentoFacade().consultaRapidaPorNomeResponsavelFinanceiroTotalRegistros(getControleConsulta().getValorConsulta(), getControleConsulta().getCampoConsulta(), getControleConsulta().getDataIni(), getControleConsulta().getDataFim(), getValorUnidadeEnsino(), true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeParceiro")) {
				if (getControleConsulta().getValorConsulta().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getNegociacaoPagamentoFacade().consultaRapidaPorNomeParceiro(getControleConsulta().getValorConsulta(), getControleConsulta().getCampoConsulta(), getControleConsulta().getDataIni(), getControleConsulta().getDataFim(), getValorUnidadeEnsino(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getNegociacaoPagamentoFacade().consultaRapidaPorNomeParceiroTotalRegistros(getControleConsulta().getValorConsulta(), getControleConsulta().getCampoConsulta(), getControleConsulta().getDataIni(), getControleConsulta().getDataFim(), getValorUnidadeEnsino(), true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeBanco")) {
				if (getControleConsulta().getValorConsulta().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getNegociacaoPagamentoFacade().consultaRapidaPorNomeBanco(getControleConsulta().getValorConsulta(), getControleConsulta().getCampoConsulta(), getControleConsulta().getDataIni(), getControleConsulta().getDataFim(), getValorUnidadeEnsino(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getNegociacaoPagamentoFacade().consultaRapidaPorNomeBancoTotalRegistros(getControleConsulta().getValorConsulta(), getControleConsulta().getCampoConsulta(), getControleConsulta().getDataIni(), getControleConsulta().getDataFim(), getValorUnidadeEnsino(), true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeFuncionario")) {
				if (getControleConsulta().getValorConsulta().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getNegociacaoPagamentoFacade().consultaRapidaPorNomeFuncionario(getControleConsulta().getValorConsulta(), getControleConsulta().getCampoConsulta(), getControleConsulta().getDataIni(), getControleConsulta().getDataFim(), getValorUnidadeEnsino(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getNegociacaoPagamentoFacade().consultaRapidaPorNomeFuncionarioTotalRegistros(getControleConsulta().getValorConsulta(), getControleConsulta().getCampoConsulta(), getControleConsulta().getDataIni(), getControleConsulta().getDataFim(), getValorUnidadeEnsino(), true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("codigoContaPagar")) {				
				if(!Uteis.getIsValorNumerico(getControleConsulta().getValorConsulta())) {
					throw new Exception(getMensagemInternalizacao("msg_validarSomenteNumeroString"));
				}
				objs = getFacadeFactory().getNegociacaoPagamentoFacade().consultaRapidaPorCodigoContaPagar(Integer.valueOf(getControleConsulta().getValorConsulta()), getControleConsulta().getCampoConsulta(), getControleConsulta().getDataIni(), getControleConsulta().getDataFim(), getValorUnidadeEnsino(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getNegociacaoPagamentoFacade().consultaRapidaPorCodigoContaPagarTotalRegistros(Integer.valueOf(getControleConsulta().getValorConsulta()), getControleConsulta().getCampoConsulta(), getControleConsulta().getDataIni(), getControleConsulta().getDataFim(), getValorUnidadeEnsino(), true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeSacado")) {
				if (getControleConsulta().getValorConsulta().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getNegociacaoPagamentoFacade().consultaRapidaPorNomeSacado(getControleConsulta().getValorConsulta(), getControleConsulta().getCampoConsulta(), getControleConsulta().getDataIni(), getControleConsulta().getDataFim(), getValorUnidadeEnsino(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getNegociacaoPagamentoFacade().consultaRapidaPorNomeSacadoTotalRegistros(getControleConsulta().getValorConsulta(), getControleConsulta().getCampoConsulta(), getControleConsulta().getDataIni(), getControleConsulta().getDataFim(), getValorUnidadeEnsino(), true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeOperadoraCartao")) {
				if (getControleConsulta().getValorConsulta().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getNegociacaoPagamentoFacade().consultaRapidaPorNomeOperadoraCartao(getControleConsulta().getValorConsulta(), getControleConsulta().getCampoConsulta(), getControleConsulta().getDataIni(), getControleConsulta().getDataFim(), getValorUnidadeEnsino(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getNegociacaoPagamentoFacade().consultaRapidaPorNomeOperadoraCartaoTotalRegistros(getControleConsulta().getValorConsulta(), getControleConsulta().getCampoConsulta(), getControleConsulta().getDataIni(), getControleConsulta().getDataFim(), getValorUnidadeEnsino(), true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
			}
			getControleConsultaOtimizado().setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("negociacaoPagamentoCons.xhtml");
		} catch (Exception e) {
			getControleConsultaOtimizado().getListaConsulta().clear();
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("negociacaoPagamentoCons.xhtml");
		}
	}

	public void imprimirComprovantePagamento() {
		try {
			comprovantePagamentoRelControle = null;
			comprovantePagamentoRelControle = (ComprovantePagamentoRelControle) context().getExternalContext().getSessionMap().get(ComprovantePagamentoRelControle.class.getSimpleName());
			if (comprovantePagamentoRelControle == null) {
				comprovantePagamentoRelControle = new ComprovantePagamentoRelControle();
				context().getExternalContext().getSessionMap().put(ComprovantePagamentoRelControle.class.getSimpleName(), comprovantePagamentoRelControle);
			}
			if (!getNegociacaoPagamentoVO().getCodigo().equals(0)) {
				getComprovantePagamentoRelControle().setNegociacaoPagamentoVO(getNegociacaoPagamentoVO());
				getComprovantePagamentoRelControle().imprimirPDF();
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void realizarSeparacaoChequesPorContaCorrenteParaImpressao() throws Exception {

		contasCorrenteVO = getFacadeFactory().getFormaPagamentoNegociacaoPagamentoFacade().separarChequesPorContaCorrente(getNegociacaoPagamentoVO().getFormaPagamentoNegociacaoPagamentoVOs(), getUsuarioLogado(), getNegociacaoPagamentoVO());

		if (contasCorrenteVO.size() == 1) {
			for (ContaCorrenteVO conta : contasCorrenteVO) {
				imprimirCheque(conta.getCheques());
				conta.getCheques().clear();
			}
		}

	}

	public String getOncompleteImpressaoCheque() {
		if (getContasCorrenteVO().size() > 1) {
			return "RichFaces.$('panelContaCorrente').show();";
		} else if (getContasCorrenteVO().size() == 1) {
			return getDownload();
		}
		return "";
	}

	public void imprimirCheque(List<ChequeVO> chequeVOs) {
		try {
			String design = "relatorio\\designRelatorio\\financeiro\\ChequeRel.jrxml";
			getSuperParametroRelVO().setNomeDesignIreport(design);
			getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
			getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getComprovantePagamentoRelFacade().getCaminhoBaseRelatorio());
			getSuperParametroRelVO().setListaObjetos(chequeVOs);
			setMensagemID("msg_relatorio_ok");
			realizarImpressaoRelatorio();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public boolean getIsImprimirCheque() {
		for (FormaPagamentoNegociacaoPagamentoVO forma : getNegociacaoPagamentoVO().getFormaPagamentoNegociacaoPagamentoVOs()) {
			if (forma.getCheque() != null && forma.getCheque().getValor() > 0) {
				return true;
			}
		}
		return false;
	}

	public boolean getIsPagamentoGravado() {
		return !getNegociacaoPagamentoVO().getNovoObj();
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe
	 * <code>NegociacaoPagamentoVO</code> Após a exclusão ela automaticamente
	 * aciona a rotina para uma nova inclusão.
	 */
	public void excluir() {
		try {
			setBotaoExcluir(true);
			if (getNegociacaoPagamentoVO().getApresentarModalMotivo()) {
				return;
			}
			if (getNegociacaoPagamentoVO().getMotivoAlteracao().equals("")) {
				throw new Exception("O motivo do estorno deve ser informado!");
			}
			getFacadeFactory().getNegociacaoPagamentoFacade().excluir(negociacaoPagamentoVO, getUsuarioLogado());
			if (Uteis.isAtributoPreenchido(getConciliacaoContaCorrenteVO())) {
				context().getExternalContext().getSessionMap().put("negociacaoConciliacaoBancaria", null);
				context().getExternalContext().getSessionMap().put("conciliacaoBancaria", getConciliacaoContaCorrenteVO());
				context().getExternalContext().getSessionMap().put("realizarEstornoConciliacaoBancaria", true);
			}
			novo();
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			try {
				carregarDadosParaEditarNegociacaoPagamento(getFacadeFactory().getNegociacaoPagamentoFacade().consultarPorChavePrimaria(getNegociacaoPagamentoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			} catch (Exception e2) {
			}
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void apresentarBotaoExcluir() {
		setBotaoExcluir(Boolean.TRUE);
	}

	public void consultarCheque() {
		try {
			List<ChequeVO> objs = new ArrayList<>(0);
			List<ContaCorrenteVO> lista = getFacadeFactory().getContaCorrenteFacade().consultarPorFuncionarioResponsavelDataAberturaFluxoCaixaSituacao(true, getUsuarioLogado().getPessoa().getCodigo(), getNegociacaoPagamentoVO().getUnidadeEnsino().getCodigo(), new Date(), "A", Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			if (getCampoConsultaCheque().equals("codigo")) {
				if (getValorConsultaCheque().equals("")) {
					setValorConsultaCheque("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaCheque());
				objs = getFacadeFactory().getChequeFacade().consultarPorCodigoSituacao(new Integer(valorInt), "EC", lista, 0, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultaCheque().equals("nomePessoa")) {
				objs = getFacadeFactory().getChequeFacade().consultarPorNomePessoaSituacao(getValorConsultaCheque(), Arrays.asList("EC"), lista, 0, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultaCheque().equals("sacado")) {
				objs = getFacadeFactory().getChequeFacade().consultarPorSacadoSituacao(getValorConsultaCheque(), Arrays.asList("EC"), lista, 0, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultaCheque().equals("nomeBanco")) {
				objs = getFacadeFactory().getChequeFacade().consultarPorNomeBancoSituacao(getValorConsultaCheque(), Arrays.asList("EC"), lista, 0, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultaCheque().equals("numeroAgenciaAgencia")) {
				objs = getFacadeFactory().getChequeFacade().consultarPorNumeroAgenciaAgenciaSituacao(getValorConsultaCheque(), Arrays.asList("EC"), lista, 0, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultaCheque().equals("numero")) {
				objs = getFacadeFactory().getChequeFacade().consultarPorNumeroSituacao(getValorConsultaCheque(), Arrays.asList("EC"), lista, getNegociacaoPagamentoVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultaCheque().equals("dataEmissao")) {
				Date valorData = Uteis.getDate(getValorConsultaCheque());
				objs = getFacadeFactory().getChequeFacade().consultarPorDataEmissaoSituacao(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), Arrays.asList("EC"), lista, 0, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultaCheque().equals("dataPrevisao")) {
				Date valorData = Uteis.getDate(getValorConsultaCheque());
				objs = getFacadeFactory().getChequeFacade().consultarPorDataPrevisaoSituacao(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), Arrays.asList("EC"), lista, 0, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			setListaConsultaCheque(objs);
			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			setListaConsultaCheque(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}

	public List getTipoConsultaComboCheque() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("sacado", "Sacado"));
		itens.add(new SelectItem("nomePessoa", "Emitente"));
		itens.add(new SelectItem("nomeBanco", "Banco"));
		itens.add(new SelectItem("numeroAgenciaAgencia", "Agência"));
		itens.add(new SelectItem("numero", "Número"));
		itens.add(new SelectItem("dataEmissao", "Data Emissão"));
		itens.add(new SelectItem("dataPrevisao", "Data Previsão"));
		return itens;
	}

	public void selecionarCheque() {
		ChequeVO obj = (ChequeVO) context().getExternalContext().getRequestMap().get("chequeItens");
		setChequeVO(obj);
	}

	public void calcularTotal() {
		getNegociacaoPagamentoVO().calcularTotal();
	}
	
	public void validarExisteAdiantamentoECalcularTotal() {	
		try {
			ContaPagarNegociacaoPagamentoVO obj = (ContaPagarNegociacaoPagamentoVO) context().getExternalContext().getRequestMap().get("contaPagarNegociacaoPagamentoItens");
			getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().realizarDistribuicaoValoresCentroResultado(obj.getContaPagar().getListaCentroResultadoOrigemVOs(), obj.getContaPagar().getPrevisaoValorPago(), getUsuarioLogado());
			limparDistribuicaoAdiantamentosParaAbatimentoContasPagar();
			getNegociacaoPagamentoVO().calcularTotal();
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public boolean getApresentarComboUnidadeEnsino() {
		try {
			return (this.getUnidadeEnsinoLogado().getCodigo() == 0);
		} catch (Exception e) {
			return false;
		}
	}

	public Integer getValorUnidadeEnsino() throws Exception {
		if (this.getUnidadeEnsinoLogado().getCodigo() == 0) {
			return getValorConsultaUnidadeEnsino();
		} else {
			return this.getUnidadeEnsinoLogado().getCodigo();
		}
	}

	public void adicionarContaPagarFornecedor(ContaPagarVO conta) {
		try {
			getContaPagarNegociacaoPagamentoVO().setContaPagar(conta);
			getContaPagarNegociacaoPagamentoVO().setValorContaPagar(conta.getValorPrevisaoPagamento());
			adicionarContaPagarNegociacaoPagamento();
			calcularTotal();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void adicionarContaPagarFornecedor() {
		try {
			if (fornecedor.getCodigo() > 0) {
				List lista = consultarContaPagarPorFornecedor(fornecedor.getCodigo());
				Iterator i = lista.iterator();
				while (i.hasNext()) {
					ContaPagarVO obj = (ContaPagarVO) i.next();
					getContaPagarNegociacaoPagamentoVO().setContaPagar(obj);
					getContaPagarNegociacaoPagamentoVO().setValorContaPagar(obj.getValorPrevisaoPagamento());
					adicionarContaPagarNegociacaoPagamento();
				}
				limparDistribuicaoAdiantamentosParaAbatimentoContasPagar();
				calcularTotal();
				setMensagemID("msg_dados_adicionados");
			}
		} catch (Exception e) {
			setContaPagarNegociacaoPagamentoVO(new ContaPagarNegociacaoPagamentoVO());
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void adicionarContaPagarFuncionario() {
		try {

			if (funcionario.getCodigo() > 0) {
				List lista = consultarContaPagarPorFuncionario(funcionario.getCodigo());
				Iterator i = lista.iterator();
				while (i.hasNext()) {
					ContaPagarVO obj = (ContaPagarVO) i.next();
					getContaPagarNegociacaoPagamentoVO().setContaPagar(obj);
					getContaPagarNegociacaoPagamentoVO().setValorContaPagar(obj.getValorPrevisaoPagamento());
					adicionarContaPagarNegociacaoPagamento();
				}
				limparDistribuicaoAdiantamentosParaAbatimentoContasPagar();
				calcularTotal();
				setMensagemID("msg_dados_adicionados");
			}
		} catch (Exception e) {
			setContaPagarNegociacaoPagamentoVO(new ContaPagarNegociacaoPagamentoVO());
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List consultarContaPagarPorFornecedor(Integer fornec) throws Exception {
		List listaConsulta = getFacadeFactory().getContaPagarFacade().consultarPorCodigoFornecedor(fornec, false, true, true, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
		return listaConsulta;
	}

	public List consultarContaPagarPorFuncionario(Integer funcion) throws Exception {
		List listaConsulta = getFacadeFactory().getContaPagarFacade().consultarPorCodigoFuncionario(funcion, false, true, true, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
		return listaConsulta;
	}

	public void consultarConfiguracaoFinanceiroCartao() throws Exception {
		try {
			if (getConfiguracaoFinanceiroCartaoVO().getCodigo().intValue() != 0) {
				setConfiguracaoFinanceiroCartaoVO(getFacadeFactory().getConfiguracaoFinanceiroCartaoFacade().consultarPorChavePrimaria(getConfiguracaoFinanceiroCartaoVO().getCodigo(), NivelMontarDados.TODOS, getUsuarioLogado()));
				getFormaPagamentoNegociacaoPagamentoVO().setOperadoraCartaoVO(getConfiguracaoFinanceiroCartaoVO().getOperadoraCartaoVO());
				getFormaPagamentoNegociacaoPagamentoVO().setCategoriaDespesaVO(getConfiguracaoFinanceiroCartaoVO().getCategoriaDespesaVO());
				getFormaPagamentoNegociacaoPagamentoVO().setContaCorrenteOperadoraCartaoVO(getConfiguracaoFinanceiroCartaoVO().getContaCorrenteVO());
				if (getFormaPagamentoNegociacaoPagamentoVO().getFormaPagamento().getTipo().equals("DE")) {
					getFormaPagamentoNegociacaoPagamentoVO().setQtdeParcelasCartaoCredito(1);
				}
				return;
			}
			setConfiguracaoFinanceiroCartaoVO(null);

		} catch (Exception e) {
			setConfiguracaoFinanceiroCartaoVO(null);
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

	public List getTipoConsultaComboFornecedor() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("razaoSocial", "Razão Social"));
		itens.add(new SelectItem("CNPJ", "CNPJ"));
		itens.add(new SelectItem("CPF", "CPF"));
		return itens;
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

	public void selecionarFornecedor() {
		FornecedorVO obj = (FornecedorVO) context().getExternalContext().getRequestMap().get("fornecedorItens");
		this.setFornecedor(obj);
	}

	public void selecionarFuncionario() {
		FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItens");
		this.setFuncionario(obj);
		Uteis.liberarListaMemoria(getListaConsultaFuncionario());
		campoConsultaFuncionario = null;
		valorConsultaFuncionario = null;
	}

	public void consultarFuncionario() {
		try {
			List objs = null;
			if (getValorConsultaFuncionario().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			getFacadeFactory().getFuncionarioFacade().setIdEntidade("Funcionario");
			if (getCampoConsultaFuncionario().equals("nome")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNome(getValorConsultaFuncionario(), "", this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("matricula")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorMatricula(getValorConsultaFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			// if (getCampoConsultaFuncionario().equals("nomeCidade")) {
			// objs =
			// getFacadeFactory().getFuncionarioFacade().consultaraPorNomeCidade(getValorConsultaFuncionario(),
			// this.getUnidadeEnsinoLogado().getCodigo(), false,
			// Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			// }
			if (getCampoConsultaFuncionario().equals("CPF")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCPF(getValorConsultaFuncionario(), "", this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			// if (getCampoConsultaFuncionario().equals("cargo")) {
			// objs =
			// getFacadeFactory().getFuncionarioFacade().consultarPorNomeCargo(getValorConsultaFuncionario(),
			// this.getUnidadeEnsinoLogado().getCodigo(), false,
			// Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			// }
			if (getCampoConsultaFuncionario().equals("departamento")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNomeDepartamento(getValorConsultaFuncionario(), "FU", this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			// if (getCampoConsultaFuncionario().equals("unidadeEnsino")) {
			// objs =
			// getFacadeFactory().getFuncionarioFacade().consultarPorNomeUnidadeEnsino(getValorConsultaFuncionario(),
			// "FU", this.getUnidadeEnsinoLogado().getCodigo(), false,
			// Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			// }
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

	public String getApresentarModalPanel() throws Exception {
		return getFormaPagamentoNegociacaoPagamentoVO().getTipoFormaPagamento();
	}

	public void consultarFormaPagamento() throws Exception {
		try {
			if (getFormaPagamentoNegociacaoPagamentoVO().getFormaPagamento().getCodigo().intValue() != 0) {
				getFormaPagamentoNegociacaoPagamentoVO().setFormaPagamento(getFacadeFactory().getFormaPagamentoFacade().consultarPorChavePrimaria(getFormaPagamentoNegociacaoPagamentoVO().getFormaPagamento().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
				if (getFormaPagamentoNegociacaoPagamentoVO().getFormaPagamento().getTipo().equals("CA") || getFormaPagamentoNegociacaoPagamentoVO().getFormaPagamento().getTipo().equals("DE")) {
					montarListaSelectItemConfiguracaoFinanceiroCartao();
				}
				inicializarValorFormaPagamentoComResiduo();
				return;
			}
			getFormaPagamentoNegociacaoPagamentoVO().setFormaPagamento(new FormaPagamentoVO());
			inicializarValorFormaPagamentoComResiduo();
		} catch (Exception e) {
			getFormaPagamentoNegociacaoPagamentoVO().setFormaPagamento(new FormaPagamentoVO());
		}
	}

	public void montarListaSelectItemConfiguracaoFinanceiroCartao() throws Exception {
		List<ConfiguracaoFinanceiroCartaoVO> lista = new ArrayList<ConfiguracaoFinanceiroCartaoVO>(0);
		getListaSelectItemConfiguracaoFinanceiroCartao().clear();
		if (getFormaPagamentoNegociacaoPagamentoVO().getFormaPagamento().getTipo().equals("CA")) {
			lista = getFacadeFactory().getConfiguracaoFinanceiroCartaoFacade().consultarPorTipoOperadoraCartaoConfiguracaoFinanceiro("CARTAO_CREDITO", getConfiguracaoFinanceiroPadraoSistema().getCodigo(), false, 0, false, getUsuarioLogado());
		} else if (getFormaPagamentoNegociacaoPagamentoVO().getFormaPagamento().getTipo().equals("DE")) {
			lista = getFacadeFactory().getConfiguracaoFinanceiroCartaoFacade().consultarPorTipoOperadoraCartaoConfiguracaoFinanceiro("CARTAO_DEBITO", getConfiguracaoFinanceiroPadraoSistema().getCodigo(), false, 0, false, getUsuarioLogado());
		}
		getListaSelectItemConfiguracaoFinanceiroCartao().add(new SelectItem(0, ""));
		for (ConfiguracaoFinanceiroCartaoVO obj : lista) {
//			if (obj.getOperadoraCartaoVO().getTipo().equals("CARTAO_CREDITO")) {
				getListaSelectItemConfiguracaoFinanceiroCartao().add(new SelectItem(obj.getCodigo(), obj.getContaCorrenteVO().getBancoAgenciaContaCorrente() +" - "+ obj.getOperadoraCartaoVO().getNome() + " - " + obj.getOperadoraCartaoVO().getOperadoraCartaoCreditoApresentar()));
//			} else {
//				getListaSelectItemConfiguracaoFinanceiroCartao().add(new SelectItem(obj.getCodigo(), obj.getOperadoraCartaoVO().getNome()));
//			}
		}
		getListaSelectItemConfiguracaoFinanceiroCartao().get(0);
		removerObjetoMemoria(lista);
	}

	public void consultarFormaPagamentoTroco() throws Exception {
		try {
			if (getNegociacaoPagamentoVO().getFormaPagamentoTrocoVO().getCodigo().intValue() != 0) {
				getNegociacaoPagamentoVO().setFormaPagamentoTrocoVO(getFacadeFactory().getFormaPagamentoFacade().consultarPorChavePrimaria(getNegociacaoPagamentoVO().getFormaPagamentoTrocoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
				return;
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		getNegociacaoPagamentoVO().setFormaPagamentoTrocoVO(new FormaPagamentoVO());
	}

	public Boolean getInformarContaCorrenteTroco() {
		if (getNegociacaoPagamentoVO().getFormaPagamentoTrocoVO() != null) {
			if (getNegociacaoPagamentoVO().getFormaPagamentoTrocoVO().getTipo() == null) {
				getNegociacaoPagamentoVO().getFormaPagamentoTrocoVO().setTipo("");
			}
			if (getNegociacaoPagamentoVO().getFormaPagamentoTrocoVO().getTipo().equals("CA") || getNegociacaoPagamentoVO().getFormaPagamentoTrocoVO().getTipo().equals("BO") || getNegociacaoPagamentoVO().getFormaPagamentoTrocoVO().getTipo().equals("DE") || getNegociacaoPagamentoVO().getFormaPagamentoTrocoVO().getTipo().equals("DC")) {
				return true;
			}
		}
		return false;
	}

	public Boolean getInformarContaCaixaTroco() {
		if (getNegociacaoPagamentoVO().getFormaPagamentoTrocoVO() != null) {
			if (getNegociacaoPagamentoVO().getFormaPagamentoTrocoVO().getTipo() == null) {
				getNegociacaoPagamentoVO().getFormaPagamentoTrocoVO().setTipo("");
			}
			if (getNegociacaoPagamentoVO().getFormaPagamentoTrocoVO().getTipo().equals("DI")) {
				return true;
			}
		}
		return false;
	}

	/*
	 * Método responsável por adicionar um novo objeto da classe
	 * <code>Pagamento</code> para o objeto <code>negociacaoPagamentoVO</code>
	 * da classe <code>NegociacaoPagamento</code>
	 */
	public void adicionarFormaPagamentoNegociacaoPagamento() {
		try {
			ChequeVO cheque = new ChequeVO();
			ContaCorrenteVO conta;
			if (!getNegociacaoPagamentoVO().getCodigo().equals(0)) {
				formaPagamentoNegociacaoPagamentoVO.setNegociacaoContaPagar(getNegociacaoPagamentoVO().getCodigo());
			}
			if (!getNegociacaoPagamentoVO().getContaPagarNegociacaoPagamentoVOs().isEmpty() &&
				 getNegociacaoPagamentoVO().getValorTotal().equals(0.0) &&
				 !getFormaPagamentoNegociacaoPagamentoVO().getFormaPagamento().getTipo().equals(TipoFormaPagamento.ISENCAO.getValor()))	{
				throw new Exception("Deve ser utilizado apenas a forma de pagamento (ISENÇÃO) para negociações com valor total igual a zero.");
			}
			if (!getNegociacaoPagamentoVO().getContaPagarNegociacaoPagamentoVOs().isEmpty() &&
				getNegociacaoPagamentoVO().getValorTotal() > 0.0 &&
				getFormaPagamentoNegociacaoPagamentoVO().getFormaPagamento().getTipo().equals(TipoFormaPagamento.ISENCAO.getValor())) {
				throw new Exception("Não deve ser utilizado a forma de pagamento (ISENÇÃO) para negociações com valor maior do que zero.");
			}
			if (getFormaPagamentoNegociacaoPagamentoVO().getFormaPagamento().getTipo().equals(TipoFormaPagamento.CHEQUE.getValor())) {
				Cheque.montarDadosContaCorrente(getChequeVO(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
				if (getChequeVO().getChequeProprio()) {
					ChequeVO.validarDados(getChequeVO());
					getChequeVO().preencherDadosDoBanco();
					getChequeVO().setSituacao(SituacaoCheque.PENDENTE.getValor());
					conta = getChequeVO().getContaCorrente();
				} else {
					conta = getChequeVO().getLocalizacaoCheque();
				}
				getFormaPagamentoNegociacaoPagamentoVO().setCheque(getChequeVO());
				getFormaPagamentoNegociacaoPagamentoVO().setValor(getChequeVO().getValor());
				getFormaPagamentoNegociacaoPagamentoVO().setContaCorrente(conta);
				getNegociacaoPagamentoVO().adicionarObjFormaPagamentoNegociacaoPagamentoVOs(getFormaPagamentoNegociacaoPagamentoVO());
				novaFormaPagamentoNegociacaoPagamentoVO();
				cheque = getChequeVO();
				setChequeVO(new ChequeVO());
				getChequeVO().setContaCorrente(cheque.getContaCorrente());
				getChequeVO().setSacado(cheque.getSacado());
			} else if (getFormaPagamentoNegociacaoPagamentoVO().getFormaPagamento().getTipo().equals(TipoFormaPagamento.PERMUTA.getValor())) {
				getFormaPagamentoNegociacaoPagamentoVO().setContaCorrente(new ContaCorrenteVO());
				getNegociacaoPagamentoVO().adicionarObjFormaPagamentoNegociacaoPagamentoVOs(getFormaPagamentoNegociacaoPagamentoVO());
				novaFormaPagamentoNegociacaoPagamentoVO();
			} else {
				validaSelecaoFormaPagamento();
				conta = getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(getFormaPagamentoNegociacaoPagamentoVO().getContaCorrente().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
				getFormaPagamentoNegociacaoPagamentoVO().setContaCorrente(conta);					
				getNegociacaoPagamentoVO().adicionarObjFormaPagamentoNegociacaoPagamentoVOs(getFormaPagamentoNegociacaoPagamentoVO());
				novaFormaPagamentoNegociacaoPagamentoVO();
			}
			montarListaSelectItemFormaPagamentoTroco();
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void novaFormaPagamentoNegociacaoPagamentoVO() throws Exception {
		FormaPagamentoVO formaAux = getFormaPagamentoNegociacaoPagamentoVO().getFormaPagamento();
		formaAux = getFormaPagamentoNegociacaoPagamentoVO().getFormaPagamento();
		this.setFormaPagamentoNegociacaoPagamentoVO(new FormaPagamentoNegociacaoPagamentoVO());
		adicionarFormaPagamentoCheque();
		getFormaPagamentoNegociacaoPagamentoVO().getFormaPagamento().setCodigo(formaAux.getCodigo());
		getFormaPagamentoNegociacaoPagamentoVO().getFormaPagamento().setNome(formaAux.getNome());
		getFormaPagamentoNegociacaoPagamentoVO().getFormaPagamento().setTipo(formaAux.getTipo());
	}

	public void adicionarFormaPagamentoCheque() {
		setChequeVO(new ChequeVO());
		getChequeVO().setBanco(getChequeVO().getBanco());
		getChequeVO().setNumeroContaCorrente(getChequeVO().getNumeroContaCorrente());
		getChequeVO().setAgencia(getChequeVO().getAgencia());
	}

	public void limparDadosCheque() {
		Boolean chPro = getChequeVO().getChequeProprio();
		setChequeVO(new ChequeVO());
		getChequeVO().setChequeProprio(chPro);
	}

	/*
	 * Método responsável por remover um novo objeto da classe
	 * <code>Pagamento</code> do objeto <code>negociacaoPagamentoVO</code> da
	 * classe <code>NegociacaoPagamento</code>
	 */
	public void removerFormaPagamentoNegociacaoRecebimento() throws Exception {
		FormaPagamentoNegociacaoPagamentoVO obj = (FormaPagamentoNegociacaoPagamentoVO) context().getExternalContext().getRequestMap().get("pagamentoItens");
		getNegociacaoPagamentoVO().excluirObjFormaPagamentoNegociacaoPagamentoVOs(obj);
		setMensagemID("msg_dados_excluidos");
	}

	public void removerPagamentoCheque() throws Exception {
		try {
			ChequeVO obj = (ChequeVO) context().getExternalContext().getRequestMap().get("chqItens");
			getNegociacaoPagamentoVO().removerFormaPagamentoCheque(obj);
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/*
	 * Método responsável por adicionar um novo objeto da classe
	 * <code>ContaPagarNegociacaoPagamento</code> para o objeto
	 * <code>negociacaoPagamentoVO</code> da classe
	 * <code>NegociacaoPagamento</code>
	 */
	public void adicionarContaPagarNegociacaoPagamento() throws Exception {
		if (!getNegociacaoPagamentoVO().getCodigo().equals(0)) {
			contaPagarNegociacaoPagamentoVO.setNegociacaoContaPagar(getNegociacaoPagamentoVO().getCodigo());
		}
		getNegociacaoPagamentoVO().adicionarObjContaPagarNegociacaoPagamentoVOs(getContaPagarNegociacaoPagamentoVO());
		this.setContaPagarNegociacaoPagamentoVO(new ContaPagarNegociacaoPagamentoVO());
		setMensagemID("msg_dados_adicionados");
	}

	/*
	 * Método responsável por disponibilizar dados de um objeto da classe
	 * <code>ContaPagarNegociacaoPagamento</code> para edição pelo usuário.
	 */
	public void editarContaPagarNegociacaoPagamento() throws Exception {
		ContaPagarNegociacaoPagamentoVO obj = (ContaPagarNegociacaoPagamentoVO) context().getExternalContext().getRequestMap().get("contaPagarNegociacaoPagamentoItens");
		setContaPagarNegociacaoPagamentoVO(obj);
	}

	/*
	 * Método responsável por remover um novo objeto da classe
	 * <code>ContaPagarNegociacaoPagamento</code> do objeto
	 * <code>negociacaoPagamentoVO</code> da classe
	 * <code>NegociacaoPagamento</code>
	 */
	public void removerContaPagarNegociacaoPagamento() throws Exception {
		ContaPagarNegociacaoPagamentoVO obj = (ContaPagarNegociacaoPagamentoVO) context().getExternalContext().getRequestMap().get("contaPagarNegociacaoPagamentoItens");
		getNegociacaoPagamentoVO().excluirObjContaPagarNegociacaoPagamentoVOs(obj.getContaPagar().getCodigo());
		limparDistribuicaoAdiantamentosParaAbatimentoContasPagar();
		calcularTotal();
		setMensagemID("msg_dados_excluidos");
	}
	
	/*
	 * Método responsável por disponibilizar dados de um objeto da classe
	 * <code>ContaPagarNegociacaoPagamento</code> para edição pelo usuário.
	 */
	public void visualizarLancamentoContabil()  {
		try {
			LancamentoContabilVO obj = (LancamentoContabilVO) context().getExternalContext().getRequestMap().get("lancamentoContabilItens");
			setLancamentoContabil(obj);
			setMensagemID("msg_dados_selecionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>ContaCorrente</code>.
	 */
	public void montarListaSelectItemContaCorrente(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarContaCorrentePorNumero(false);
			i = resultadoConsulta.iterator();
			List objs = new ArrayList<>(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				ContaCorrenteVO obj = (ContaCorrenteVO) i.next();
				if (Uteis.isAtributoPreenchido(obj.getNomeApresentacaoSistema())) {
					objs.add(new SelectItem(obj.getCodigo(), obj.getNomeApresentacaoSistema()));
				} else {
					objs.add(new SelectItem(obj.getCodigo(), obj.getDescricaoCompletaConta()));
				}
			}
			setListaSelectItemContaCorrente(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>ContaCorrente</code>.
	 */
	public void montarListaSelectItemBanco(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarBancoPorNome(prm);
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				BancoVO obj = (BancoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
			}
			setListaSelectItemBanco(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>ContaCorrente</code>. Buscando todos os objetos correspondentes a
	 * entidade <code>ContaCorrente</code>. Esta rotina não recebe parâmetros
	 * para filtragem de dados, isto é importante para a inicialização dos dados
	 * da tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemBanco() {
		try {
			montarListaSelectItemBanco("");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>numero</code> Este atributo é uma
	 * lista (<code>List</code>) utilizada para definir os valores a serem
	 * apresentados no ComboBox correspondente
	 */
	public List consultarBancoPorNome(String prm) throws Exception {
		List lista = getFacadeFactory().getBancoFacade().consultarPorNome(prm, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		return lista;
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>ContaCorrente</code>. Buscando todos os objetos correspondentes a
	 * entidade <code>ContaCorrente</code>. Esta rotina não recebe parâmetros
	 * para filtragem de dados, isto é importante para a inicialização dos dados
	 * da tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemContaCorrente() {
		try {
			montarListaSelectItemContaCorrente("");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>FormaPagamento</code>.
	 */
	public void montarListaSelectItemFormaPagamento(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarFormaPagamentoPorNome(prm);
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				FormaPagamentoVO obj = (FormaPagamentoVO) i.next();
				if (obj.isDebitoEmConta() || obj.isDinheiro() || obj.isCheque() || obj.isCartaoCredito() || obj.isCartaoDebito() || obj.isBoletoBancario() || obj.isPermuta() || obj.getTipo().equals("IS")) {
					objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
				}
			}
			setListaSelectItemFormaPagamento(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>FormaPagamento</code>. Buscando todos os objetos correspondentes a
	 * entidade <code>FormaPagamento</code>. Esta rotina não recebe parâmetros
	 * para filtragem de dados, isto é importante para a inicialização dos dados
	 * da tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemFormaPagamento() {
		try {
			montarListaSelectItemFormaPagamento("");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name());
		}
	}

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>nome</code> Este atributo é uma
	 * lista (<code>List</code>) utilizada para definir os valores a serem
	 * apresentados no ComboBox correspondente
	 */
	public List consultarFormaPagamentoPorNome(String nomePrm) throws Exception {
		List lista = getFacadeFactory().getFormaPagamentoFacade().consultarPorNome(nomePrm, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
		return lista;
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>Fornecedor</code>.
	 */
	public void montarListaSelectItemFornecedor(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarFornecedorPorNome(prm);
			i = resultadoConsulta.iterator();
			List objs = new ArrayList<>(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				FornecedorVO obj = (FornecedorVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
			}
			setListaSelectItemFornecedor(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>Fornecedor</code>. Buscando todos os objetos correspondentes a
	 * entidade <code>Fornecedor</code>. Esta rotina não recebe parâmetros para
	 * filtragem de dados, isto é importante para a inicialização dos dados da
	 * tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemFornecedor() {
		try {
			montarListaSelectItemFornecedor("");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name());
		}
	}

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>nome</code> Este atributo é uma
	 * lista (<code>List</code>) utilizada para definir os valores a serem
	 * apresentados no ComboBox correspondente
	 */
	public List consultarFornecedorPorNome(String nomePrm) throws Exception {
		List lista = getFacadeFactory().getFornecedorFacade().consultarPorNome(nomePrm, "AT", false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		return lista;
	}

	public List consultarUnidadeEnsinoPorNome(String prm) throws Exception {
		List lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(prm, 0, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		return lista;
	}

	public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
		if (getUnidadeEnsinoLogado().getCodigo().intValue() != 0) {
			setListaSelectItemUnidadeEnsino(new ArrayList(0));
			getListaSelectItemUnidadeEnsino().add(new SelectItem(getUnidadeEnsinoLogado().getCodigo(), getUnidadeEnsinoLogado().getNome().toString()));
			getNegociacaoPagamentoVO().getUnidadeEnsino().setCodigo(getUnidadeEnsinoLogado().getCodigo());
			getNegociacaoPagamentoVO().getUnidadeEnsino().setNome(getUnidadeEnsinoLogado().getNome());
			return;
		}
		List resultadoConsulta = consultarUnidadeEnsinoPorNome("");
		Iterator i = resultadoConsulta.iterator();
		List objs = new ArrayList(0);
		objs.add(new SelectItem(0, ""));
		while (i.hasNext()) {
			UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
			objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
		}
		Uteis.liberarListaMemoria(resultadoConsulta);
		i = null;
		setListaSelectItemUnidadeEnsino(objs);
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>Agencia</code>. Buscando todos os objetos correspondentes a
	 * entidade <code>Agencia</code>. Esta rotina não recebe parâmetros para
	 * filtragem de dados, isto é importante para a inicialização dos dados da
	 * tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemUnidadeEnsino() {
		try {
			montarListaSelectItemUnidadeEnsino("");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name());
		}
	}

	public void setarBanco() {
		this.negociacaoPagamentoVO.setBanco(this.getBanco());
	}

	/**
	 * Método responsável por processar a consulta na entidade
	 * <code>ContaPagar</code> por meio dos parametros informados no richmodal.
	 * Esta rotina é utilizada fundamentalmente por requisições Ajax, que
	 * realizam busca pelos parâmentros informados no richModal montando
	 * automaticamente o resultado da consulta para apresentação.
	 */
	public void consultarContaPagar() {
		try {
			List<ContaPagarVO> objs = null;

			Integer codigoSacado = null;
			if (negociacaoPagamentoVO.getTipoSacado().equals("FO")) {
				codigoSacado = getFornecedor().getCodigo();
			} else if (negociacaoPagamentoVO.getTipoSacado().equals("FU")) {
				codigoSacado = getFuncionario().getPessoa().getCodigo();
			} else if (negociacaoPagamentoVO.getTipoSacado().equals("BA")) {
				codigoSacado = getBanco().getCodigo();
			} else if (negociacaoPagamentoVO.getTipoSacado().equals("AL")) {
				codigoSacado = getAluno().getCodigo();
			} else if (negociacaoPagamentoVO.getTipoSacado().equals("PA")) {
				codigoSacado = negociacaoPagamentoVO.getParceiro().getCodigo();
			} else if (negociacaoPagamentoVO.getTipoSacado().equals("RF")) {
				codigoSacado = negociacaoPagamentoVO.getResponsavelFinanceiro().getCodigo();
			} else if (negociacaoPagamentoVO.getTipoSacado().equals(TipoSacado.OPERADORA_CARTAO.getValor())) {
				codigoSacado = negociacaoPagamentoVO.getOperadoraCartao().getCodigo();
			}

			if (getCampoConsultarContaPagar().equals("vencidas")) {
				objs = getFacadeFactory().getContaPagarFacade().consultarVencidosPorTipoSacado(codigoSacado, negociacaoPagamentoVO.getTipoSacado(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultarContaPagar().equals("aVencer")) {
				objs = getFacadeFactory().getContaPagarFacade().consultarAVencerPorTipoSacado(codigoSacado, negociacaoPagamentoVO.getTipoSacado(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultarContaPagar().equals("periodo")) {
				objs = getFacadeFactory().getContaPagarFacade().consultarVencidosEmPeriodo(codigoSacado, negociacaoPagamentoVO.getTipoSacado(), dataInicioConsultarContaPagar, dataTerminoConsultarContaPagar, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultarContaPagar().equals("todas")) {
				objs = getFacadeFactory().getContaPagarFacade().consultarVencidosEmPeriodo(codigoSacado, negociacaoPagamentoVO.getTipoSacado(), null, null, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultarContaPagar(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultarContaPagar(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public Boolean getApresentarDatasConsultaContaPagar() {
		try {
			return getCampoConsultarContaPagar().equals("periodo");
		} catch (NullPointerException e) {
			return false;
		}
	}

	public void selecionarContaPagar() throws Exception {
		try {
			ContaPagarVO obj = (ContaPagarVO) context().getExternalContext().getRequestMap().get("contaPagarItens");
			obj = getFacadeFactory().getContaPagarFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			if (getMensagemDetalhada().equals("")) {
				this.getContaPagarNegociacaoPagamentoVO().setContaPagar(obj);
				this.getContaPagarNegociacaoPagamentoVO().setValorContaPagar(obj.getValorPrevisaoPagamento());
				adicionarContaPagarNegociacaoPagamento();
				removerContaReceberListaConsulta(obj.getCodigo());
			}
			limparDistribuicaoAdiantamentosParaAbatimentoContasPagar();
			calcularTotal();
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void removerContaReceberListaConsulta(Integer contaPagar) {
		int index = 0;
		Iterator i = getListaConsultarContaPagar().iterator();
		while (i.hasNext()) {
			ContaPagarVO obj = (ContaPagarVO) i.next();
			if (obj.getCodigo().intValue() == contaPagar) {
				getListaConsultarContaPagar().remove(index);
				return;
			}
			index++;
		}
	}

	public void limparCampoContaPagar() {
		this.getContaPagarNegociacaoPagamentoVO().setContaPagar(new ContaPagarVO());
	}

	/**
	 * Rotina responsável por preencher a combo de consulta dos RichModal da
	 * telas.
	 */
	public List getTipoConsultarComboContaPagar() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("vencidas", "Vencidas"));
		itens.add(new SelectItem("aVencer", "A vencer"));
		itens.add(new SelectItem("periodo", "Período"));
		itens.add(new SelectItem("todas", "Todas"));
		return itens;
	}

	/**
	 * Método responsável por processar a consulta na entidade
	 * <code>Fornecedor</code> por meio de sua respectiva chave primária. Esta
	 * rotina é utilizada fundamentalmente por requisições Ajax, que realizam
	 * busca pela chave primária da entidade montando automaticamente o
	 * resultado da consulta para apresentação.
	 */
	public void consultarFornecedorPorChavePrimaria() {
		try {
			Integer campoConsulta = negociacaoPagamentoVO.getFornecedor().getCodigo();
			FornecedorVO fornecedor = getFacadeFactory().getFornecedorFacade().consultarPorChavePrimaria(campoConsulta, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			negociacaoPagamentoVO.setFornecedor(fornecedor);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			negociacaoPagamentoVO.getFornecedor().setNome("");
			negociacaoPagamentoVO.getFornecedor().setCodigo(0);
		}
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>Caixa</code>.
	 */
	public void montarListaSelectItemCaixa(String prm) throws Exception {
		List resultadoConsulta = consultarContaCorrentePorNumero(true);
		Iterator i = resultadoConsulta.iterator();
		List objs = new ArrayList(0);
		objs.add(new SelectItem(0, ""));
		while (i.hasNext()) {
			ContaCorrenteVO obj = (ContaCorrenteVO) i.next();
			if (Uteis.isAtributoPreenchido(obj.getNomeApresentacaoSistema())) {
				objs.add(new SelectItem(obj.getCodigo(), obj.getNomeApresentacaoSistema()));
			} else {
				objs.add(new SelectItem(obj.getCodigo(), obj.getNumero() + "-" + obj.getDigito()));
			}
		}
		Uteis.liberarListaMemoria(resultadoConsulta);
		setListaSelectItemCaixa(objs);
		montarListaSelectItemContaCorrente();
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>Caixa</code>. Buscando todos os objetos correspondentes a entidade
	 * <code>ContaCorrente</code>. Esta rotina não recebe parâmetros para
	 * filtragem de dados, isto é importante para a inicialização dos dados da
	 * tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemCaixa() {
		try {
			montarListaSelectItemCaixa("");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name());
		}
	}

	public Boolean getApresentarContaCaixa() {
		try {
			return !negociacaoPagamentoVO.getUnidadeEnsino().isNovoObj();
		} catch (Exception e) {
			return false;
		}
	}

	public void limparModalConta() {
		setCampoConsultarContaPagar("vencidas");
		dataInicioConsultarContaPagar = Uteis.obterDataFutura(new Date(), -5);
		dataTerminoConsultarContaPagar = Uteis.obterDataFutura(new Date(), 5);
		setListaConsultarContaPagar(new ArrayList(0));
	}

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>numero</code> Este atributo é uma
	 * lista (<code>List</code>) utilizada para definir os valores a serem
	 * apresentados no ComboBox correspondente
	 */
	public List consultarContaCorrentePorNumero(Boolean contaCaixa) throws Exception {
		List<ContaCorrenteVO> lista = new ArrayList<ContaCorrenteVO>(0);
		if (contaCaixa) {
			if (getNegociacaoPagamentoVO() != null) {
				Boolean usuarioTemContaCaixa = false;
				if (getNegociacaoPagamentoVO().getCodigo().equals(0)) {
					usuarioTemContaCaixa = getFacadeFactory().getContaCorrenteFacade().consultarSeUsuarioTemContaCaixaVinculadoAEle(getUsuarioLogado().getPessoa().getCodigo());
				}
				if (usuarioTemContaCaixa) {
					if (getNegociacaoPagamentoVO().getCodigo() != 0) {
						lista = getFacadeFactory().getContaCorrenteFacade().consultarPorFuncionarioResponsavel(getUsuarioLogado().getPessoa().getCodigo(), getNegociacaoPagamentoVO().getUnidadeEnsino().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
					} else {
						lista = getFacadeFactory().getContaCorrenteFacade().consultarPorFuncionarioResponsavelDataAberturaFluxoCaixaSituacao(contaCaixa, getUsuarioLogado().getPessoa().getCodigo(), getNegociacaoPagamentoVO().getUnidadeEnsino().getCodigo(), new Date(), "A", Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
					}
				} else {
					if (getNegociacaoPagamentoVO().getCodigo() != 0) {
						lista = getFacadeFactory().getContaCorrenteFacade().consultarPorContaCaixa(contaCaixa, getNegociacaoPagamentoVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, true, getUsuarioLogado());
					} else {
						lista = getFacadeFactory().getContaCorrenteFacade().consultarPorContaCaixaDataAberturaFluxoCaixaSituacao(contaCaixa, getNegociacaoPagamentoVO().getUnidadeEnsino().getCodigo(), new Date(), "A", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
					}
				}
			}
		} else {
			lista = getFacadeFactory().getContaCorrenteFacade().consultarPorContaCaixa(contaCaixa, getNegociacaoPagamentoVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, true, getUsuarioLogado());
		}

		// List lista =
		// getFacadeFactory().getContaCorrenteFacade().consultarPorFuncionarioResponsavelDataAberturaFluxoCaixaSituacao(contaCaixa,
		// getUsuarioLogado().getPessoa().getCodigo(),
		// getNegociacaoPagamentoVO().getUnidadeEnsino().getCodigo(), new
		// Date(), "A", Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
		// List lista =
		// getFacadeFactory().getContaCorrenteFacade().consultarPorContaCaixa(contaCaixa,
		// getNegociacaoPagamentoVO().getUnidadeEnsino().getCodigo(), false,
		// Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
		return lista;
	}

	/**
	 * Método responsável por inicializar a lista de valores (
	 * <code>SelectItem</code>) para todos os ComboBox's.
	 */
	public void inicializarListasSelectItemTodosComboBox() {
		montarListaSelectItemFornecedor();
		montarListaSelectItemFormaPagamento();
		// montarListaSelectItemContaCorrente();
		montarListaSelectItemUnidadeEnsino();
		montarListaSelectItemCaixa();

		montarListaSelectItemBanco();
	}

	/**
	 * Rotina responsável por atribui um javascript com o método de mascara para
	 * campos do tipo Data, CPF, CNPJ, etc.
	 */
	public String getMascaraConsulta() {
		if (getControleConsulta().getCampoConsulta().equals("data")) {
			return "return mascara(this.form,'form:valorConsulta','99/99/9999',event);";
		}
		return "";
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List getTipoConsultaCombo() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("aluno", "Aluno/Candidato"));
		itens.add(new SelectItem("numeroContaCorrente", "Conta Corrente"));
		itens.add(new SelectItem("nomeFornecedor", "Fornecedor"));
		itens.add(new SelectItem("nomeSacado", "Sacado"));
		itens.add(new SelectItem("nomeOperadoraCartao", "Operadora Cartão"));
		itens.add(new SelectItem("nomeBanco", "Banco"));
		itens.add(new SelectItem("nomeFuncionario", "Funcionario"));
		itens.add(new SelectItem("nomeParceiro", "Parceiro"));
		itens.add(new SelectItem("nomeResponsavelFinanceiro", "Responsável Financeiro"));
		itens.add(new SelectItem("codigoContaPagar", "N° Conta a Pagar"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	public boolean isCampoData() {
		if (getControleConsulta().getCampoConsulta().equals("data")) {
			return true;
		}
		return false;
	}

	public boolean isConsultaPorCodigo() {
		return getControleConsulta().getCampoConsulta().equals("codigo") || getControleConsulta().getCampoConsulta().equals("codigoContaPagar");
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes
	 * de uma consulta.
	 */
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setListaConsulta(new ArrayList(0));
		inicializarListasSelectItemTodosComboBox();
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("negociacaoPagamentoCons.xhtml");
	}

	/**
	 * Operação que libera todos os recursos (atributos, listas, objetos) do
	 * backing bean. Garantindo uma melhor atuação do Garbage Coletor do Java. A
	 * mesma é automaticamente quando realiza o logout.
	 */
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
		negociacaoPagamentoVO = null;
		Uteis.liberarListaMemoria(listaSelectItemCaixa);
		contaPagarNegociacaoPagamentoVO = null;
		Uteis.liberarListaMemoria(listaSelectItemBanco);
		Uteis.liberarListaMemoria(listaSelectItemFormaPagamento);
		Uteis.liberarListaMemoria(listaSelectItemContaCorrente);
		Uteis.liberarListaMemoria(listaSelectItemFornecedor);
	}

	public ChequeVO getChequeVO() {
		return chequeVO;
	}

	public void setChequeVO(ChequeVO chequeVO) {
		this.chequeVO = chequeVO;
	}

	public FormaPagamentoNegociacaoPagamentoVO getFormaPagamentoNegociacaoPagamentoVO() {
		return formaPagamentoNegociacaoPagamentoVO;
	}

	public void setFormaPagamentoNegociacaoPagamentoVO(FormaPagamentoNegociacaoPagamentoVO formaPagamentoNegociacaoPagamentoVO) {
		this.formaPagamentoNegociacaoPagamentoVO = formaPagamentoNegociacaoPagamentoVO;
	}

	public List getListaSelectItemUnidadeEnsino() {
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
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

	public List getListaConsultaCheque() {
		return listaConsultaCheque;
	}

	public void setListaConsultaCheque(List listaConsultaCheque) {
		this.listaConsultaCheque = listaConsultaCheque;
	}

	public List getListaSelectItemFornecedor() {
		return listaSelectItemFornecedor;
	}

	public void setListaSelectItemFornecedor(List listaSelectItemFornecedor) {
		this.listaSelectItemFornecedor = listaSelectItemFornecedor;
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

	public List getListaSelectItemTipoSacado() throws Exception {
		List objs = new ArrayList(0);
		Hashtable listaTipoSacado = (Hashtable) Dominios.getTipoSacado();
		Enumeration keys = listaTipoSacado.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) listaTipoSacado.get(value);
			objs.add(new SelectItem(value, label));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		return objs;
	}

	public List<SelectItem> getListaSelectItemTipoSacadoContaPagar() throws Exception {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("AL", "Aluno/Candidato"));
		itens.add(new SelectItem("BA", "Banco"));
		itens.add(new SelectItem("FO", "Fornecedor"));
		itens.add(new SelectItem("FU", "Funcionário/Professor"));
		itens.add(new SelectItem("PA", "Parceiro"));
		itens.add(new SelectItem("RF", "Responsável Financeiro"));
		itens.add(new SelectItem("OC", "Operadora Cartão"));
		return itens;
	}

	public List getListaSelectItemContaCorrente() {
		if (listaSelectItemContaCorrente == null) {
			listaSelectItemContaCorrente = new ArrayList(0);
		}
		return (listaSelectItemContaCorrente);
	}

	public void setListaSelectItemContaCorrente(List listaSelectItemContaCorrente) {
		this.listaSelectItemContaCorrente = listaSelectItemContaCorrente;
	}

	public List getListaSelectItemFormaPagamento() {
		if (listaSelectItemFormaPagamento == null) {
			listaSelectItemFormaPagamento = new ArrayList(0);
		}
		return (listaSelectItemFormaPagamento);
	}

	public void setListaSelectItemFormaPagamento(List listaSelectItemFormaPagamento) {
		this.listaSelectItemFormaPagamento = listaSelectItemFormaPagamento;
	}

	public String getCampoConsultarContaPagar() {
		return campoConsultarContaPagar;
	}

	public void setCampoConsultarContaPagar(String campoConsultarContaPagar) {
		this.campoConsultarContaPagar = campoConsultarContaPagar;
	}

	public Date getDataInicioConsultarContaPagar() {
		return dataInicioConsultarContaPagar;
	}

	public void setDataInicioConsultarContaPagar(Date dataInicioConsultarContaPagar) {
		this.dataInicioConsultarContaPagar = dataInicioConsultarContaPagar;
	}

	public Date getDataTerminoConsultarContaPagar() {
		return dataTerminoConsultarContaPagar;
	}

	public void setDataTerminoConsultarContaPagar(Date dataTerminoConsultarContaPagar) {
		this.dataTerminoConsultarContaPagar = dataTerminoConsultarContaPagar;
	}

	public List getListaConsultarContaPagar() {
		return listaConsultarContaPagar;
	}

	public void setListaConsultarContaPagar(List listaConsultarContaPagar) {
		this.listaConsultarContaPagar = listaConsultarContaPagar;
	}

	public ContaPagarNegociacaoPagamentoVO getContaPagarNegociacaoPagamentoVO() {
		return contaPagarNegociacaoPagamentoVO;
	}

	public void setContaPagarNegociacaoPagamentoVO(ContaPagarNegociacaoPagamentoVO contaPagarNegociacaoPagamentoVO) {
		this.contaPagarNegociacaoPagamentoVO = contaPagarNegociacaoPagamentoVO;
	}

	public List getListaSelectItemCaixa() {
		if (listaSelectItemCaixa == null) {
			listaSelectItemCaixa = new ArrayList(0);
		}
		return (listaSelectItemCaixa);
	}

	public void setListaSelectItemCaixa(List listaSelectItemCaixa) {
		this.listaSelectItemCaixa = listaSelectItemCaixa;
	}

	public NegociacaoPagamentoVO getNegociacaoPagamentoVO() {
		if (negociacaoPagamentoVO == null) {
			negociacaoPagamentoVO = new NegociacaoPagamentoVO();
		}
		return negociacaoPagamentoVO;
	}

	public FornecedorVO getFornecedor() {
		if (fornecedor == null) {
			fornecedor = new FornecedorVO();
		}
		return fornecedor;
	}

	public void setFornecedor(FornecedorVO fornecedor) {
		this.fornecedor = fornecedor;
	}

	public void setNegociacaoPagamentoVO(NegociacaoPagamentoVO negociacaoPagamentoVO) {
		this.negociacaoPagamentoVO = negociacaoPagamentoVO;
	}

	public Boolean getBotaoExcluir() {
		if (botaoExcluir == null) {
			botaoExcluir = false;
		}
		return botaoExcluir;
	}

	public void setBotaoExcluir(Boolean botaoExcluir) {
		this.botaoExcluir = botaoExcluir;
	}

	public FuncionarioVO getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(FuncionarioVO funcionario) {
		this.funcionario = funcionario;
	}

	/**
	 * @return the listaSelectItemBanco
	 */
	public List getListaSelectItemBanco() {
		if (listaSelectItemBanco == null) {
			listaSelectItemBanco = new ArrayList(0);
		}
		return listaSelectItemBanco;
	}

	/**
	 * @param listaSelectItemBanco
	 *            the listaSelectItemBanco to set
	 */
	public void setListaSelectItemBanco(List listaSelectItemBanco) {
		this.listaSelectItemBanco = listaSelectItemBanco;
	}

	/**
	 * @return the banco
	 */
	public BancoVO getBanco() {
		return banco;
	}

	/**
	 * @param banco
	 *            the banco to set
	 */
	public void setBanco(BancoVO banco) {
		this.banco = banco;
	}

	/**
	 * @return the ValorConsultaUnidadeEnsino
	 */
	public int getValorConsultaUnidadeEnsino() {
		return ValorConsultaUnidadeEnsino;
	}

	/**
	 * @param ValorConsultaUnidadeEnsino
	 *            the ValorConsultaUnidadeEnsino to set
	 */
	public void setValorConsultaUnidadeEnsino(int ValorConsultaUnidadeEnsino) {
		this.ValorConsultaUnidadeEnsino = ValorConsultaUnidadeEnsino;
	}

	public String irConsultarContaPagar() {
		return Uteis.getCaminhoRedirecionamentoNavegacao("contaPagarCons.xhtml");
	}

	public PessoaVO getAluno() {
		if (aluno == null) {
			aluno = new PessoaVO();
		}
		return aluno;
	}

	public void setAluno(PessoaVO aluno) {
		this.aluno = aluno;
	}

	public List<SelectItem> getListaSelectItemConfiguracaoFinanceiroCartao() {
		if (listaSelectItemConfiguracaoFinanceiroCartao == null) {
			listaSelectItemConfiguracaoFinanceiroCartao = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemConfiguracaoFinanceiroCartao;
	}

	public void setListaSelectItemConfiguracaoFinanceiroCartao(List<SelectItem> listaSelectItemConfiguracaoFinanceiroCartao) {
		this.listaSelectItemConfiguracaoFinanceiroCartao = listaSelectItemConfiguracaoFinanceiroCartao;
	}

	public ConfiguracaoFinanceiroCartaoVO getConfiguracaoFinanceiroCartaoVO() {
		if (configuracaoFinanceiroCartaoVO == null) {
			configuracaoFinanceiroCartaoVO = new ConfiguracaoFinanceiroCartaoVO();
		}
		return configuracaoFinanceiroCartaoVO;
	}

	public void setConfiguracaoFinanceiroCartaoVO(ConfiguracaoFinanceiroCartaoVO configuracaoFinanceiroCartaoVO) {
		this.configuracaoFinanceiroCartaoVO = configuracaoFinanceiroCartaoVO;
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

	protected List<PessoaVO> listaConsultaResponsavelFinanceiro;
	protected String valorConsultaResponsavelFinanceiro;
	protected String campoConsultaResponsavelFinanceiro;

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

	public void selecionarResponsavelFinanceiro() {
		try {
			PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("responsavelFinanceiroItens");
			this.getNegociacaoPagamentoVO().setResponsavelFinanceiro(obj);
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

	public void consultarParceiro() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaParceiro().equals("nome")) {
				objs = getFacadeFactory().getParceiroFacade().consultarPorNome(getValorConsultaParceiro(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaParceiro().equals("razaoSocial")) {
				objs = getFacadeFactory().getParceiroFacade().consultarPorRazaoSocial(getValorConsultaParceiro(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
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
		ParceiroVO obj = (ParceiroVO) context().getExternalContext().getRequestMap().get("parceiroItens");
		getNegociacaoPagamentoVO().setParceiro(obj);
		listaConsultaParceiro.clear();
		this.setValorConsultaParceiro("");
		this.setCampoConsultaParceiro("");
	}

	public ComprovantePagamentoRelControle getComprovantePagamentoRelControle() {
		return comprovantePagamentoRelControle;
	}

	public void setComprovantePagamentoRelControle(ComprovantePagamentoRelControle comprovantePagamentoRelControle) {
		this.comprovantePagamentoRelControle = comprovantePagamentoRelControle;
	}

	public OperadoraCartaoVO getOperadoraCartao() {
		if (operadoraCartao == null) {
			operadoraCartao = new OperadoraCartaoVO();
		}
		return operadoraCartao;
	}

	public void setOperadoraCartao(OperadoraCartaoVO operadoraCartao) {
		this.operadoraCartao = operadoraCartao;
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
			setOperadoraCartao(obj);
			getListaConsultaOperadoraCartao().clear();
			setValorConsultaOperadoraCartao("");
			setCampoConsultaOperadoraCartao("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

	}

	public void selecionarContaCorrenteImpressaoCheque() {
		try {
			ContaCorrenteVO obj = (ContaCorrenteVO) context().getExternalContext().getRequestMap().get("contaCorrenteItens");
			imprimirCheque(obj.getCheques());

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<ContaCorrenteVO> getContasCorrenteVO() {
		if (contasCorrenteVO == null) {
			contasCorrenteVO = new ArrayList<ContaCorrenteVO>(0);
		}

		return contasCorrenteVO;
	}

	public void setContasCorrenteVO(List<ContaCorrenteVO> contasCorrenteVO) {
		this.contasCorrenteVO = contasCorrenteVO;
	}
	
	

	public LancamentoContabilVO getLancamentoContabil() {
		if (lancamentoContabil == null) {
			lancamentoContabil = new LancamentoContabilVO();
		}
		return lancamentoContabil;
	}

	public void setLancamentoContabil(LancamentoContabilVO lancamentoContabil) {
		this.lancamentoContabil = lancamentoContabil;
	}

	public String realizarNavegacaoParaConciliacaoBancaria() {
		context().getExternalContext().getSessionMap().put("conciliacaoBancaria", getConciliacaoContaCorrenteVO());
		removerControleMemoriaFlash("ConciliacaoContaCorrenteControle");
		removerControleMemoriaTela("ConciliacaoContaCorrenteControle");
		return Uteis.getCaminhoRedirecionamentoNavegacao("conciliacaoContaCorrenteForm.xhtml");
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

	public String getRealizarNavegacaoParaContaPagar() {
		try {
			context().getExternalContext().getSessionMap().put("contaPagarLancamentoContabil", getContaPagarNegociacaoPagamentoVO().getContaPagar());
			removerControleMemoriaFlash("ContaPagarControle");
			removerControleMemoriaTela("ContaPagarControle");
			return "popup('../financeiro/contaPagarForm.xhtml', 'contaPagarForm' , 1024, 800)";
		} catch (Exception e) {
			setMensagemID(MSG_TELA.msg_erro.name());
			return "";
		}
	}
	
	public void montarListaSelectItemFormaPagamentoTroco() throws Exception {
		List<FormaPagamentoVO> resultado = consultarFormaPagamentoPorNome("");
		getListaSelectItemFormaPagamentoTroco().clear();
		getListaSelectItemFormaPagamentoTroco().add(new SelectItem("", ""));
		resultado.forEach(formaPagamentoVO->{
			if (formaPagamentoVO.isDebitoEmConta() || formaPagamentoVO.isDinheiro() || formaPagamentoVO.isDeposito()) {
				getListaSelectItemFormaPagamentoTroco().add(new SelectItem(formaPagamentoVO.getCodigo(), formaPagamentoVO.getNome()));
			}
		});
	}
	
	public List<SelectItem> getListaSelectItemFormaPagamentoTroco() {
		if (listaSelectItemFormaPagamentoTroco == null) {
			listaSelectItemFormaPagamentoTroco = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemFormaPagamentoTroco;
	}
	
	public void setListaSelectItemFormaPagamentoTroco(List<SelectItem> listaSelectItemFormaPagamentoTroco) {
		this.listaSelectItemFormaPagamentoTroco = listaSelectItemFormaPagamentoTroco;
	}
	
	private void validaSelecaoFormaPagamento() throws Exception {
		if (!Uteis.isAtributoPreenchido(getFormaPagamentoNegociacaoPagamentoVO().getFormaPagamento())) {
			throw new ConsistirException("O campo FORMA PAGAMENTO (Pagamento) é obrigatório.");
		}
		if (!Uteis.isAtributoPreenchido(getFormaPagamentoNegociacaoPagamentoVO().getContaCorrente())) {
			throw new ConsistirException("O campo CONTA CORRENTE ou CAIXA (Pagamento) é obrigatório.");
		}
		if (!getFormaPagamentoNegociacaoPagamentoVO().getFormaPagamento().getTipo().equals("IS") && !Uteis.isAtributoPreenchido(getFormaPagamentoNegociacaoPagamentoVO().getValor())) {
			throw new ConsistirException("O campo VALOR (Pagamento) é obrigatório.");
		}
	}
	@PostConstruct
	public void realizarCarregamentoPagamentoVindoTelaExtratoContaCorrente() {
		try {
			if (context().getExternalContext().getSessionMap().get("pagamentoExtratoContaCorrente") != null && context().getExternalContext().getSessionMap().get("pagamentoExtratoContaCorrente") instanceof Integer && Uteis.isAtributoPreenchido(((Integer)context().getExternalContext().getSessionMap().get("pagamentoExtratoContaCorrente")))) {
				Integer codigo = (Integer) context().getExternalContext().getSessionMap().get("pagamentoExtratoContaCorrente");
				if (Uteis.isAtributoPreenchido(codigo)) {
					setNegociacaoPagamentoVO(getFacadeFactory().getNegociacaoPagamentoFacade().consultarPorChavePrimaria(codigo, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
					carregarDadosParaEditarNegociacaoPagamento(getNegociacaoPagamentoVO());
				}
				setMensagemID("msg_dados_editar", Uteis.ALERTA);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			context().getExternalContext().getSessionMap().remove("pagamentoExtratoContaCorrente");
		}
	}	
	
	/**
	 * INICIO MERGE EDIGAR 24/05/18
	 */
	
	/*
	 * Variavel utilizada para controlar se a distribuicao de adiantamentos foi realizada. 
	 * Caso ela esteja com valor 0.0 significa que a distribuicao nao foi feita, ou ainda que, a 
	 * mesmo precisa ser refeita, em funcao de alguma modificacao nas contas a pagar selecionadas 
	 * para a negociacao de pagamentos (adicionou um nova conta, ou removou um conta, ou alterou o valor 
	 * de um das contas).
	 */
	private Double valorTotalConsideradoDistribuicaoAdiantamentos;
	private Boolean apresentarAdiantamentosDisponiveisDistribuicao;
	private ContaPagarVO contaPagarVerAdiantamentos;
	
	public void prepararContaPagarVerAdiantamentosUtilizadosAbaterValor() {
		try {
			ContaPagarNegociacaoPagamentoVO obj = (ContaPagarNegociacaoPagamentoVO) context().getExternalContext().getRequestMap().get("contaPagarNegociacaoPagamentoItens");
			setContaPagarVerAdiantamentos(obj.getContaPagar());
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID(MSG_TELA.msg_erro.name());
		}			
	}

	public ContaPagarVO getContaPagarVerAdiantamentos() {
		if (contaPagarVerAdiantamentos == null) {
			contaPagarVerAdiantamentos = new ContaPagarVO();
		}
		return contaPagarVerAdiantamentos;
	}

	public void setContaPagarVerAdiantamentos(ContaPagarVO contaPagarVerAdiantamentos) {
		this.contaPagarVerAdiantamentos = contaPagarVerAdiantamentos;
	}

	public void inicializarValorFormaPagamentoComResiduo() {
		try {
			getFormaPagamentoNegociacaoPagamentoVO().setValor(this.getNegociacaoPagamentoVO().getResiduo());
		} catch (Exception e) {
		}
	}
	
	public void limparDistribuicaoAdiantamentosParaAbatimentoContasPagar() {
		try {
			getFacadeFactory().getNegociacaoPagamentoFacade().limparDistribuicaoAdiantamentosDisponiveisParaAbaterContasPagar(this.getNegociacaoPagamentoVO(), false);
			setValorTotalConsideradoDistribuicaoAdiantamentos(0.0);
			setMensagemID("msg_dados_atualizados");
		} catch (Exception e) {
			setMensagemID("msg_erro");
		}
	}
	
	public void confirmarDistribuicaoAdiantamentosParaAbatimentoContasPagar() {
		try {
			getNegociacaoPagamentoVO().setData(getNegociacaoPagamentoVO().getDataRegistro());
			for (ContaPagarVO obj : getNegociacaoPagamentoVO().getListaAdiantamentosUtilizadosAbaterContasPagar()) {
				if (obj.getUtilizarAdiantamentoNegociacaoPagamento()) {
					Date dataRecebimentoAdiantamento = getFacadeFactory().getNegociacaoPagamentoFacade().consultarDataPagamentoNegociacaoPagamentoPorContaPagar(obj.getCodigo(), getUsuarioLogado());
					if (UteisData.validarDataInicialMaiorFinal(getNegociacaoPagamentoVO().getData(), dataRecebimentoAdiantamento)) {
						getNegociacaoPagamentoVO().setData(dataRecebimentoAdiantamento);
					}
				}
			}
		setUtilizarDataAdiantamento(Boolean.TRUE);
		setMensagemID("msg_dados_atualizados");
		setApresentarAdiantamentosDisponiveisDistribuicao(Boolean.FALSE);
	} catch (Exception e) {
		e.printStackTrace();
	}
	}
	
	public void confirmarNaoUtilizacaoDosAdiantamentosDisponiveis() {
		setMensagemID("msg_dados_atualizados");
		limparDistribuicaoAdiantamentosParaAbatimentoContasPagar();
		setApresentarAdiantamentosDisponiveisDistribuicao(Boolean.FALSE);
	}
	
	public void removerAdiantamentosDistribuidosContaPagar() {
		getNegociacaoPagamentoVO().setData(getNegociacaoPagamentoVO().getDataRegistro());
		setUtilizarDataAdiantamento(Boolean.FALSE);
		setMensagemID("msg_dados_atualizados");
		limparDistribuicaoAdiantamentosParaAbatimentoContasPagar();		
	}
	public void removerAdiantamentosContasPagar() {
		try {
			ContaPagarAdiantamentoVO cpa = (ContaPagarAdiantamentoVO) context().getExternalContext().getRequestMap().get("adiantamentoItem");
			getFacadeFactory().getNegociacaoPagamentoFacade().removerAdiantamentosDisponiveisParaAbaterContasPagar(getNegociacaoPagamentoVO(), cpa, getUsuarioLogadoClone());
			setMensagemID("msg_dados_atualizados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void atualizarValoresAdiantamentoUtilizados() {
		try {
			//ContaPagarVO obj = (ContaPagarVO) context().getExternalContext().getRequestMap().get("adiantamentoItem");
			getFacadeFactory().getNegociacaoPagamentoFacade().limparDistribuicaoAdiantamentosDisponiveisParaAbaterContasPagar(this.getNegociacaoPagamentoVO(), true);
			setValorTotalConsideradoDistribuicaoAdiantamentos(this.getNegociacaoPagamentoVO().getValorTotal());
			getFacadeFactory().getNegociacaoPagamentoFacade().realizarDistribuicaoAutomaticaAdiantamentosDisponiveisParaAbaterContasPagar(this.getNegociacaoPagamentoVO());
			setApresentarAdiantamentosDisponiveisDistribuicao(Boolean.TRUE);
			setValorTotalConsideradoDistribuicaoAdiantamentos(this.getNegociacaoPagamentoVO().getValorTotal());
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro");
		}			
	}
	
	public void verificarEObterAdiantamentosDisponiveisParaAbatimentoContasPagar() {
		try {
			if (this.getNegociacaoPagamentoVO().getExisteContaPagarAdiantamentoNaNegociacaoPagamento()) {
				// condicao para impedir que possa ser usado um adiantamento para pagar (quitar) outro adiantamento.
				setValorTotalConsideradoDistribuicaoAdiantamentos(this.getNegociacaoPagamentoVO().getValorTotal());
				this.getNegociacaoPagamentoVO().setListaAdiantamentosUtilizadosAbaterContasPagar(null);
				setApresentarAdiantamentosDisponiveisDistribuicao(Boolean.FALSE);
			}		
			if (getNegociacaoPagamentoVO().getValorTotal().doubleValue() == 0.0) {
				// se o valor total a ser pago está zerado, entao nao temos mais o que distribuir.
				// sendo assim, nao iremos apresentar mais o painel de distribuicao. Iremos seguir
				// o fluxo normalmente, inclusive por que pode estar zerado em funcao do uso de adiantamentos
				setApresentarAdiantamentosDisponiveisDistribuicao(Boolean.FALSE);
				return;
			}			
			if (Uteis.isAtributoPreenchido(getNegociacaoPagamentoVO())) {
				setApresentarAdiantamentosDisponiveisDistribuicao(Boolean.FALSE);
				return;
			}
			if (getValorTotalConsideradoDistribuicaoAdiantamentos().doubleValue() == 0.0) {
				limparDistribuicaoAdiantamentosParaAbatimentoContasPagar();
				setValorTotalConsideradoDistribuicaoAdiantamentos(this.getNegociacaoPagamentoVO().getValorTotal());
				List<ContaPagarVO> listaAdiantamentosDisponiveisUsarAbatimento = getFacadeFactory().getContaPagarFacade().consultaRapidaContaPagarAdiantamentoPodemSerUtilizadasParaAbatimentoNegociacaoPagamento(this.getUnidadeEnsinoLogado().getCodigo(), this.getNegociacaoPagamentoVO().getTipoSacado(), this.getNegociacaoPagamentoVO().getCodigoSacado(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
				this.getNegociacaoPagamentoVO().setListaAdiantamentosUtilizadosAbaterContasPagar(listaAdiantamentosDisponiveisUsarAbatimento);
				getFacadeFactory().getNegociacaoPagamentoFacade().realizarDistribuicaoAutomaticaAdiantamentosDisponiveisParaAbaterContasPagar(this.getNegociacaoPagamentoVO());
				if (!this.getNegociacaoPagamentoVO().getListaAdiantamentosUtilizadosAbaterContasPagar().isEmpty()) {
					setApresentarAdiantamentosDisponiveisDistribuicao(Boolean.TRUE);
					setValorTotalConsideradoDistribuicaoAdiantamentos(this.getNegociacaoPagamentoVO().getValorTotal());
					setMensagemID("msg_NegociaoPagamento_adiantamentos");
				}
			}
		} catch (Exception e) {
			setMensagemID("msg_erro");
		}
	}
	
	public Boolean getApresentarRichModalDistribuicaoAdiantamentos() {
		if (!this.getNegociacaoPagamentoVO().getListaAdiantamentosUtilizadosAbaterContasPagar().isEmpty()) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public Double getValorTotalConsideradoDistribuicaoAdiantamentos() {
		if (valorTotalConsideradoDistribuicaoAdiantamentos == null) {
			valorTotalConsideradoDistribuicaoAdiantamentos = 0.0;
		}
		return valorTotalConsideradoDistribuicaoAdiantamentos;
	}

	public void setValorTotalConsideradoDistribuicaoAdiantamentos(Double valorTotalConsideradoDistribuicaoAdiantamentos) {
		this.valorTotalConsideradoDistribuicaoAdiantamentos = valorTotalConsideradoDistribuicaoAdiantamentos;
	}

	public Boolean getApresentarAdiantamentosDisponiveisDistribuicao() {
		if (apresentarAdiantamentosDisponiveisDistribuicao == null) {
			apresentarAdiantamentosDisponiveisDistribuicao = Boolean.FALSE;
		}
		return apresentarAdiantamentosDisponiveisDistribuicao;
	}

	public void setApresentarAdiantamentosDisponiveisDistribuicao(Boolean apresentarAdiantamentosDisponiveisDistribuicao) {
		this.apresentarAdiantamentosDisponiveisDistribuicao = apresentarAdiantamentosDisponiveisDistribuicao;
	}
	
    public Boolean getApresentarBotaoLiberarBloqueio() {
    	return this.getNegociacaoPagamentoVO().getApresentarBotaoLiberarBloqueioFechamentoMes();
    }		
	
	public void liberarRegistroCompetenciaFechada() {
		try {
			this.getNegociacaoPagamentoVO().setBloqueioPorFechamentoMesLiberado(Boolean.TRUE);		
			FechamentoMesHistoricoModificacaoVO historico = getFacadeFactory().getFechamentoMesHistoricoModificacaoFacade().gerarNovoHistoricoModificacao(this.getNegociacaoPagamentoVO().getFechamentoMesVOBloqueio(), getUsuarioLogado(), TipoOrigemHistoricoBloqueioEnum.PAGAMENTO, this.getNegociacaoPagamentoVO().getDescricaoBloqueio(), this.getNegociacaoPagamentoVO().toString());
			getFacadeFactory().getFechamentoMesHistoricoModificacaoFacade().incluir(historico, getUsuarioLogado());
			setMensagemID("msg_registro_liberado_mes");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			this.getNegociacaoPagamentoVO().setBloqueioPorFechamentoMesLiberado(Boolean.FALSE);
		}
	}
	
	public void verificarPermissaoLiberarBloqueioCompetencia() {
		try {
			UsuarioVO usuarioVerif = ControleAcesso.verificarLoginUsuario(this.getUsernameLiberacaoBloqueioPorFechamentoMes(), this.getSenhaLiberacaoBloqueioPorFechamentoMes(), true, Uteis.NIVELMONTARDADOS_TODOS);
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("FuncionarioMes_liberarBloqueioRecebimentoContaReceber", usuarioVerif);
			liberarRegistroCompetenciaFechada();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
 	}	
	
	public void alterarDataReferencia() {
		getNegociacaoPagamentoVO().reiniciarControleBloqueioCompetencia();
	}
	
	public Boolean getUtilizarDataAdiantamento() {
		if (utilizarDataAdiantamento == null) {
			utilizarDataAdiantamento = Boolean.FALSE;
		}
		return utilizarDataAdiantamento;
	}

	public void setUtilizarDataAdiantamento(Boolean utilizarDataAdiantamento) {
		this.utilizarDataAdiantamento = utilizarDataAdiantamento;
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

}
