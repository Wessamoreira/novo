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
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import jobs.JobBaixaCartaoCreditoDCC;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoFinanceiroEnum;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.contabil.LancamentoContabilVO;
import negocio.comuns.contabil.enumeradores.TipoOrigemLancamentoContabilEnum;
import negocio.comuns.contabil.enumeradores.TipoPlanoContaEnum;
import negocio.comuns.ead.enumeradores.SituacaoEnum;
import negocio.comuns.financeiro.CartaoCreditoDebitoRecorrenciaPessoaVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroCartaoVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.FormaPagamentoNegociacaoRecebimentoVO;
import negocio.comuns.financeiro.LogExecucaoJobCartaoCreditoDebitoRecorrenciaVO;
import negocio.comuns.financeiro.MapaPendenciaCartaoCreditoTotalVO;
import negocio.comuns.financeiro.MapaPendenciaCartaoCreditoVO;
import negocio.comuns.financeiro.OperadoraCartaoVO;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.financeiro.TransacaoCartaoOnlineVO;
import negocio.comuns.financeiro.enumerador.FormaPadraoDataBaseCartaoRecorrenteEnum;
import negocio.comuns.financeiro.enumerador.TipoFinanciamentoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.SituacaoContaReceber;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

@Controller("MapaPendenciaCartaoCreditoControle")
@Scope("viewScope")
@Lazy
public class MapaPendenciaCartaoCreditoControle extends SuperControle implements Serializable {

	private static final long serialVersionUID = 1L;
	private Boolean selecionarTodasParcelas;
	private Integer qtdeParcelasSelecionadas;
	private Double valorAReceberTotal;
	private Double valorLiquidoTotal;
	private List<MapaPendenciaCartaoCreditoVO> listaMapaPendenciaCartaoCreditoVO;
	private ContaCorrenteVO contaCorrenteVO;
	private List<SelectItem> listaSelectItemContaCorrente;
	private Boolean executarConsultaDetalhada;
	private Date dataEmissaoInicial;
	private Date dataEmissaoFinal;
	private Date dataPrevisaoInicial;
	private Date dataPrevisaoFinal;
	private Date dataRecebimentoOperadoraInicial;
	private Date dataRecebimentoOperadoraFinal;
	private String tipoData;
	private String ordenarPor;
	private String situacaoCartaoCredito;
	private Date dataVencimento;
	private Boolean criarContaUnidadeMatriz;
	private Double valorTaxaOperadoraTotal;
	private FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO;
	private MapaPendenciaCartaoCreditoVO mapaPendenciaCartaoCreditoVO;
	private List<MapaPendenciaCartaoCreditoTotalVO> mapaPendenciaCartaoCreditoTotalVOs;
	private List<SelectItem> listaSelectItemFormaPagamento;
	private FormaPagamentoVO formaPagamentoVO;
	private Double taxaTroca;
	private Boolean taxaAntecipacaoTroca;
	private Date dataPrevisao;
	private LancamentoContabilVO lancamentoContabilVO;
	private boolean efetuarBaixaListaMapaCartao = true;
	private boolean permitirRealizarAlteracoesNegociacaoRecebimento = false;
	private boolean existeFormaPagamentoNegociacaoRecebimentoRecebida = false;
	private List<SelectItem> listaSelectItemConfiguracaoFinanceiroCartao;
	private List<SelectItem> listaSelectItemQuantidadeParcelas;
	private List<SelectItem> listaSelectItemTipoFinanciamento;

	private String tipoTransacao;
	private List<TransacaoCartaoOnlineVO> listaTransacaoCartaoOnlineVOs;
	private OperadoraCartaoVO operadoraCartaoVO;
	private MatriculaVO matriculaTransacaoVO;
	private MatriculaVO matriculaRecorrenciaVO;
	private Date dataInicioTransacao;
	private Date dataFimTransacao;
	private List<MatriculaVO> listaConsultaMatriculaVOs;
	private String valorConsultaMatricula;
	private String campoConsultaMatricula;
	private String controleAba;
	private TipoPessoa tipoPessoaTransacao;
	private TipoPessoa tipoPessoaRecorrencia;
	private Boolean aluno;
	private Boolean funcionario;
	private Boolean parceiro;
	private Boolean candidato;
	private Boolean responsavelFinanceiro;
	private Boolean fornecedor;

	private List<SelectItem> listaSelectItemFuncionario;
	private List<FuncionarioVO> listaConsultaFuncionario;
	private String valorConsultaFuncionario;
	private String campoConsultaFuncionario;
	private List<PessoaVO> listaConsultaCandidato;
	private String valorConsultaCandidato;
	private String campoConsultaCandidato;
	private List<ParceiroVO> listaConsultaParceiro;
	private String valorConsultaParceiro;
	private String campoConsultaParceiro;
	protected List<PessoaVO> listaConsultaResponsavelFinanceiro;
	protected String valorConsultaResponsavelFinanceiro;
	protected String campoConsultaResponsavelFinanceiro;
	protected List<FornecedorVO> listaConsultaFornecedor;
	protected String valorConsultaFornecedor;
	protected String campoConsultaFornecedor;

	private PessoaVO PessoaVO;
	private ParceiroVO parceiroVO;
	private FornecedorVO fornecedorVO;
	private String campoDescritivo;
	private String valorDescritivo;

	private String nomeCartao;
	private FormaPadraoDataBaseCartaoRecorrenteEnum formaPadraoDataBaseCartaoRecorrente;
	private Integer diaPadraoPagamento;
	private Integer mesValidade;
	private String anoValidade;
	private PessoaVO responsavelFinanceiroVO;
	private List<CartaoCreditoDebitoRecorrenciaPessoaVO> listaCartaoCreditoDebitoRecorrenciaPessoaVOs;
	private boolean permitirExcluirCartaoRecorrencia = false;
	private boolean permitirExecutarJobCartaoRecorrencia = false;
	private String numeroFinalCartaoCredito;
	private String chaveTransacaoCartaoCredito;
	private String dataUltimaExecucaoJob;
	private List<LogExecucaoJobCartaoCreditoDebitoRecorrenciaVO> listaLogExecucaoJobCartaoCreditoDebitoRecorrenciaVOs;
	

	public MapaPendenciaCartaoCreditoControle() throws Exception {
		novo();
		setControleConsulta(new ControleConsulta());
		setExecutarConsultaDetalhada(true);
		setDataPrevisaoInicial(Uteis.getDataPrimeiroDiaMes(new Date()));
		setDataPrevisaoFinal(Uteis.getDataUltimoDiaMes(new Date()));
		verificarPermissaoUsuarioExcluirCartaoRecorrencia();
		verificarPermissaoUsuarioExecutarJobCartaoRecorrencia();
		consultarDataUltimaExecucaoJobBaixaCartaoCreditoRecorrenciaDCC();
		setTipoData("PR");
		setMensagemID("msg_entre_prmconsulta", Uteis.ALERTA);
	}

	@PostConstruct
	public void realizarCarregamentoVindoTelaConciliacaoBancaria() {
		try {
			String numeroContaCorrente = (String) context().getExternalContext().getSessionMap().get("numeroContaCorrente");
			if (Uteis.isAtributoPreenchido(numeroContaCorrente)) {
				setDataPrevisaoInicial((Date) context().getExternalContext().getSessionMap().get("diaCorrente"));
				setDataPrevisaoFinal((Date) context().getExternalContext().getSessionMap().get("diaCorrente"));
				montarListaSelectItemContaCorrente((String) context().getExternalContext().getSessionMap().get("nrBanco"), numeroContaCorrente, (String) context().getExternalContext().getSessionMap().get("digitoContaCorrente"));
				setMarcarTodasUnidadeEnsino(false);
				marcarTodasUnidadesEnsinoAction();
				consultar();
				setMensagemID("msg_dados_editar", Uteis.ALERTA);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			context().getExternalContext().getSessionMap().remove("numeroContaCorrente");
			context().getExternalContext().getSessionMap().remove("digitoContaCorrente");
			context().getExternalContext().getSessionMap().remove("diaCorrente");
			context().getExternalContext().getSessionMap().remove("nrBanco");
		}

	}

	@PostConstruct
	public void realizarCarregamentoVindoTelaIntegracaoContabil() {
		try {
			String formapagamentonegociacaorecebimentocartaocredito = (String) context().getExternalContext().getSessionMap().get("formapagamentonegociacaorecebimentocartaocredito");
			if (Uteis.isAtributoPreenchido(formapagamentonegociacaorecebimentocartaocredito)) {
				setSelecionarTodasParcelas(false);
				setQtdeParcelasSelecionadas(0);
				setValorAReceberTotal(0.0);
				setValorTaxaOperadoraTotal(0.0);
				setValorLiquidoTotal(0.0);
				getListaMapaPendenciaCartaoCreditoVO().clear();
				setListaMapaPendenciaCartaoCreditoVO(getFacadeFactory().getMapaPendenciaCartaoCreditoFacade().consultarPorOrigemFormaPagamentoNegociacaoRecebimentoCartaoCredito(formapagamentonegociacaorecebimentocartaocredito, getUsuarioLogado()));
				setMapaPendenciaCartaoCreditoTotalVOs(getFacadeFactory().getMapaPendenciaCartaoCreditoFacade().realizarCalculoMapaPendenciaCartaCreditoTotal(getDataVencimento(), getListaMapaPendenciaCartaoCreditoVO()));
				if (Uteis.isAtributoPreenchido(getListaMapaPendenciaCartaoCreditoVO())) {
					setDataPrevisaoInicial(getListaMapaPendenciaCartaoCreditoVO().get(0).getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getDataVencimento());
					setDataPrevisaoFinal(getDataPrevisaoInicial());
					montarListaSelectItemContaCorrente(getListaMapaPendenciaCartaoCreditoVO().get(0).getFormaPagamentoNegociacaoRecebimentoVO().getContaCorrenteOperadoraCartaoVO().getAgencia().getBanco().getNrBanco(), getListaMapaPendenciaCartaoCreditoVO().get(0).getFormaPagamentoNegociacaoRecebimentoVO().getContaCorrenteOperadoraCartaoVO().getNumero(), getListaMapaPendenciaCartaoCreditoVO().get(0).getFormaPagamentoNegociacaoRecebimentoVO().getContaCorrenteOperadoraCartaoVO().getDigito());
				}
				setMarcarTodasUnidadeEnsino(false);
				marcarTodasUnidadesEnsinoAction();
				setMensagemID("msg_dados_editar", Uteis.ALERTA);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			context().getExternalContext().getSessionMap().remove("formapagamentonegociacaorecebimentocartaocredito");
		}

	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe
	 * <code>MovimentacaoFinanceira</code> para edição pelo usuário da aplicação.
	 */
	public String novo() {
		removerObjetoMemoria(this);
		setSelecionarTodasParcelas(false);
		setExecutarConsultaDetalhada(false);
		getListaMapaPendenciaCartaoCreditoVO().clear();
		consultarUnidadeEnsinoFiltroRelatorio("");
		verificarTodasUnidadeEnsinoSelecionados();
		montarListaSelectItemContaCorrente();
		verificarPermissaoUsuarioAlteracoesNegociacaoRecebimento();
		setMensagemID("msg_entre_dados");
		return "editar";
	}

	public void preencherCamposFiltrarPorData() {
		try {
			setTipoData("");
			getListaSelectItemFiltroData();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void validarDadosParaEfetuarBaixa() {
		try {
			setEfetuarBaixaListaMapaCartao(true);
			montarListaSelectItemFormaPagamentoBaixarPendenciaCartao();
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);

		}
	}

	public void validarDadosParaEstornaBaixa() {
		try {
			setEfetuarBaixaListaMapaCartao(false);
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);

		}
	}

	public String gravar() {
		try {
			setOncompleteModal("");
			if (!getListaMapaPendenciaCartaoCreditoVO().isEmpty() && getQtdeParcelasSelecionadasResumoOperacao() > 0) {
				getFacadeFactory().getMapaPendenciaCartaoCreditoFacade().executarBaixaParcelaCartaoCredito(getMapaPendenciaCartaoCreditoTotalVOs(), getCriarContaUnidadeMatriz(), getDataVencimento(), getDataVencimento(), getFormaPagamentoVO(), getUsuarioLogado());

			} else {
				throw new ConsistirException("Não há parcelas selecionadas para efetuar baixa.");
			}
			setQtdeParcelasSelecionadas(0);
			setValorAReceberTotal(0.0);
			setValorTotalTaxa(0.0);
			setValorLiquidoTotal(0.0);
			setMapaPendenciaCartaoCreditoTotalVOs(getFacadeFactory().getMapaPendenciaCartaoCreditoFacade().realizarCalculoMapaPendenciaCartaCreditoTotal(getDataVencimento(), getListaMapaPendenciaCartaoCreditoVO()));
			setOncompleteModal("RichFaces.$('panelDataVencimento').hide();");
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
			return "editar";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return "editar";
		}
	}

	public void estornar() {
		try {
			setOncompleteModal("");
			if (!getListaMapaPendenciaCartaoCreditoVO().isEmpty() && getQtdeParcelasSelecionadasResumoOperacao() > 0) {
				getFacadeFactory().getMapaPendenciaCartaoCreditoFacade().executarEstornoBaixa(getMapaPendenciaCartaoCreditoTotalVOs(), getCriarContaUnidadeMatriz(), getUsuarioLogado());
			} else {
				throw new ConsistirException("Não há parcelas selecionadas para efetuar estorno.");
			}
			setQtdeParcelasSelecionadas(0);
			setValorAReceberTotal(0.0);
			setValorTotalTaxa(0.0);
			setValorLiquidoTotal(0.0);
			setMapaPendenciaCartaoCreditoTotalVOs(getFacadeFactory().getMapaPendenciaCartaoCreditoFacade().realizarCalculoMapaPendenciaCartaCreditoTotal(getDataVencimento(), getListaMapaPendenciaCartaoCreditoVO()));
			setOncompleteModal("RichFaces.$('panelDataVencimento').hide();");
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			consultar();
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	@Override
	public String consultar() {
		try {
			List<UnidadeEnsinoVO> listaUnidadeEnsinoTemp = getUnidadeEnsinoVOMarcadasParaSeremUtilizadas();
			List<OperadoraCartaoVO> listaOperadoraCartaoTemp = getOperadoraCartaoVOMarcadasParaSeremUtilizadas();
			setSelecionarTodasParcelas(false);
			validarDadosConsulta();
			setQtdeParcelasSelecionadas(0);
			setValorAReceberTotal(0.0);
			setValorTaxaOperadoraTotal(0.0);
			setValorLiquidoTotal(0.0);
			getListaMapaPendenciaCartaoCreditoVO().clear();
			if (!getExecutarConsultaDetalhada()) {
				setDataEmissaoInicial(null);
				setDataEmissaoFinal(null);
				setDataRecebimentoOperadoraInicial(null);
				setDataRecebimentoOperadoraFinal(null);
				setDataPrevisaoInicial(Uteis.getDataPrimeiroDiaMes(new Date()));
				setDataPrevisaoFinal(Uteis.getDataUltimoDiaMes(new Date()));
			} else {
				if (getTipoData().equals("EM")) {
					setDataPrevisaoInicial(null);
					setDataPrevisaoFinal(null);
					setDataRecebimentoOperadoraInicial(null);
					setDataRecebimentoOperadoraFinal(null);
				} else if (getTipoData().equals("PR")) {
					setDataEmissaoInicial(null);
					setDataEmissaoFinal(null);
					setDataRecebimentoOperadoraInicial(null);
					setDataRecebimentoOperadoraFinal(null);
				} else if (getTipoData().equals("RO")) {
					setDataPrevisaoInicial(null);
					setDataPrevisaoFinal(null);
					setDataEmissaoInicial(null);
					setDataEmissaoFinal(null);
				} else {
					setDataRecebimentoOperadoraInicial(null);
					setDataRecebimentoOperadoraFinal(null);
				}
			}
			setListaMapaPendenciaCartaoCreditoVO(getFacadeFactory().getMapaPendenciaCartaoCreditoFacade().consultarPorParcelasCartaoCredito(getSituacaoCartaoCredito(), getDataEmissaoInicial(), getDataEmissaoFinal(), getDataPrevisaoInicial(), getDataPrevisaoFinal(), getDataRecebimentoOperadoraInicial(), getDataRecebimentoOperadoraFinal(), listaUnidadeEnsinoTemp, listaOperadoraCartaoTemp, getContaCorrenteVO().getCodigo(), getOrdenarPor(), false, getUsuarioLogado()));
			setMapaPendenciaCartaoCreditoTotalVOs(getFacadeFactory().getMapaPendenciaCartaoCreditoFacade().realizarCalculoMapaPendenciaCartaCreditoTotal(getDataVencimento(), getListaMapaPendenciaCartaoCreditoVO()));
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
			return "consultar";
		} catch (Exception e) {
			setListaConsulta(new ArrayList<MapaPendenciaCartaoCreditoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return "consultar";
		}
	}

	public void selecionarParcelaParaEfetuarBaixa() {
		try {
			MapaPendenciaCartaoCreditoVO obj = (MapaPendenciaCartaoCreditoVO) context().getExternalContext().getRequestMap().get("mapaPendenciaCartaoCreditoItens");
			if (obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getSituacao().equals("AR")) {
				if (obj.getEfetuarBaixa()) {
					getFacadeFactory().getMapaPendenciaCartaoCreditoFacade().realizarCalculoMapaPendenciaCartaCreditoTotal(obj, getMapaPendenciaCartaoCreditoTotalVOs(), true, getDataVencimento());
				} else {
					getFacadeFactory().getMapaPendenciaCartaoCreditoFacade().realizarCalculoMapaPendenciaCartaCreditoTotal(obj, getMapaPendenciaCartaoCreditoTotalVOs(), false, null);
				}
				realizarCalculoTotal();
			}
			setSelecionarTodasParcelas(getListaMapaPendenciaCartaoCreditoVO().stream().allMatch(p -> p.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getSituacao().equals("AR") && p.getEfetuarBaixa()));
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void selecionarParcelaParaEstornoBaixa() {
		try {
			MapaPendenciaCartaoCreditoVO obj = (MapaPendenciaCartaoCreditoVO) context().getExternalContext().getRequestMap().get("mapaPendenciaCartaoCreditoItens");
			if (obj.getEfetuarBaixa()) {
				getFacadeFactory().getMapaPendenciaCartaoCreditoFacade().realizarCalculoMapaPendenciaCartaCreditoTotal(obj, getMapaPendenciaCartaoCreditoTotalVOs(), true, obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getDataRecebimento());
			} else {
				getFacadeFactory().getMapaPendenciaCartaoCreditoFacade().realizarCalculoMapaPendenciaCartaCreditoTotal(obj, getMapaPendenciaCartaoCreditoTotalVOs(), false, null);
			}
			realizarCalculoTotal();
			setSelecionarTodasParcelas(getListaMapaPendenciaCartaoCreditoVO().stream().allMatch(p -> p.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getSituacao().equals("RE") && p.getEfetuarBaixa()));
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void selecionarTodasParcelas(String matricula, String sacado, String operadora, String contaCorrente, String dataEmissao_Apresentar, String dataVencimento_Apresentar, String dataRecebimento_Apresentar, String dadosCartao) {
		try {
			if (getSelecionarTodasParcelas()) {
				getListaMapaPendenciaCartaoCreditoVO().stream().filter(p -> (!Uteis.isAtributoPreenchido(matricula) || (Uteis.isAtributoPreenchido(matricula) && Uteis.removerAcentos(p.getFormaPagamentoNegociacaoRecebimentoVO().getNegociacaoRecebimentoVO().getMatricula().toLowerCase()).contains(Uteis.removerAcentos(matricula.toLowerCase())))) && (!Uteis.isAtributoPreenchido(sacado) || (Uteis.isAtributoPreenchido(sacado) && Uteis.removerAcentos(p.getFormaPagamentoNegociacaoRecebimentoVO().getNegociacaoRecebimentoVO().getNomePessoaParceiro().toLowerCase()).contains(Uteis.removerAcentos(sacado.toLowerCase())))) && (!Uteis.isAtributoPreenchido(operadora) || (Uteis.isAtributoPreenchido(operadora) && Uteis.removerAcentos(p.getFormaPagamentoNegociacaoRecebimentoVO().getOperadoraCartaoVO().getOperadoraCartaoCreditoApresentarDetalhado().toLowerCase()).contains(Uteis.removerAcentos(operadora.toLowerCase())))) && (!Uteis.isAtributoPreenchido(contaCorrente) || (Uteis.isAtributoPreenchido(contaCorrente) && Uteis.removerAcentos(p.getFormaPagamentoNegociacaoRecebimentoVO().getContaCorrenteOperadoraCartaoVO().getDescricaoCompletaConta().toLowerCase()).contains(Uteis.removerAcentos(contaCorrente.toLowerCase())))) && (!Uteis.isAtributoPreenchido(dataEmissao_Apresentar) || (Uteis.isAtributoPreenchido(dataEmissao_Apresentar) && Uteis.removerAcentos(p.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getDataEmissao_Apresentar().toLowerCase()).contains(Uteis.removerAcentos(dataEmissao_Apresentar.toLowerCase())))) && (!Uteis.isAtributoPreenchido(dataVencimento_Apresentar) || (Uteis.isAtributoPreenchido(dataVencimento_Apresentar) && Uteis.removerAcentos(p.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getDataVencimento_Apresentar().toLowerCase()).contains(Uteis.removerAcentos(dataVencimento_Apresentar.toLowerCase())))) && (!Uteis.isAtributoPreenchido(dataRecebimento_Apresentar) || (Uteis.isAtributoPreenchido(dataRecebimento_Apresentar) && Uteis.removerAcentos(p.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getDataRecebimento_Apresentar().toLowerCase()).contains(Uteis.removerAcentos(dataRecebimento_Apresentar.toLowerCase())))) && (!Uteis.isAtributoPreenchido(dadosCartao) || (Uteis.isAtributoPreenchido(dadosCartao) && Uteis.removerAcentos(p.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getDadosCartaoApresentar().toLowerCase()).contains(Uteis.removerAcentos(dadosCartao.toLowerCase()))))).forEach(p -> {
					if (p.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getSituacao().equals("AR")) {
						p.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setDataRecebimento(getDataVencimento());
					}
					p.setEfetuarBaixa(true);
				});
			} else {
				getListaMapaPendenciaCartaoCreditoVO().stream().filter(p -> p.getEfetuarBaixa()).forEach(p -> {
					if (p.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getSituacao().equals("AR")) {
						p.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setDataRecebimento(null);
					}
					p.setEfetuarBaixa(false);
				});
			}
			setSelecionarTodasParcelas(getListaMapaPendenciaCartaoCreditoVO().stream().allMatch(p -> p.getEfetuarBaixa()));
			setMapaPendenciaCartaoCreditoTotalVOs(getFacadeFactory().getMapaPendenciaCartaoCreditoFacade().realizarCalculoMapaPendenciaCartaCreditoTotal(getDataVencimento(), getListaMapaPendenciaCartaoCreditoVO()));
			realizarCalculoTotal();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void atualizarDadosPelaUnidadeEnsino() {
		try {
			setContaCorrenteVO(new ContaCorrenteVO());
			montarListaSelectItemContaCorrente();
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void consultarLancamentoContabil() {
		try {
			MapaPendenciaCartaoCreditoVO obj = (MapaPendenciaCartaoCreditoVO) context().getExternalContext().getRequestMap().get("mapaPendenciaCartaoCreditoItens");
			setMapaPendenciaCartaoCreditoVO(obj);
			getMapaPendenciaCartaoCreditoVO().getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setListaLancamentoContabeisCredito(getFacadeFactory().getLancamentoContabilFacade().consultaRapidaPorCodOrigemPorTipoOrigemPorTipoPlanoConta(obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getCodigo().toString(), TipoOrigemLancamentoContabilEnum.CARTAO_CREDITO, TipoPlanoContaEnum.CREDITO, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			getMapaPendenciaCartaoCreditoVO().getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setListaLancamentoContabeisDebito(getFacadeFactory().getLancamentoContabilFacade().consultaRapidaPorCodOrigemPorTipoOrigemPorTipoPlanoConta(obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getCodigo().toString(), TipoOrigemLancamentoContabilEnum.CARTAO_CREDITO, TipoPlanoContaEnum.DEBITO, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void visualizarLancamentoContabilRateio() {
		try {
			LancamentoContabilVO obj = (LancamentoContabilVO) context().getExternalContext().getRequestMap().get("lancamentoContabilItens");
			setLancamentoContabilVO(obj);
			setMensagemID("msg_dados_selecionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void limparCamposAlteracaoNegociacaoRecebimento() {
		try {
			if (!Uteis.isAtributoPreenchido(getMapaPendenciaCartaoCreditoVO().getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamento())) {
				getMapaPendenciaCartaoCreditoVO().getFormaPagamentoNegociacaoRecebimentoVO().setConfiguracaoFinanceiroCartaoVO(new ConfiguracaoFinanceiroCartaoVO());
				getMapaPendenciaCartaoCreditoVO().getFormaPagamentoNegociacaoRecebimentoVO().setQtdeParcelasCartaoCredito(0);
				getMapaPendenciaCartaoCreditoVO().getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setTipoFinanciamentoEnum(null);
				getMapaPendenciaCartaoCreditoVO().getListaFormaPagamentoNegociacaoRecebimentoVO().clear();
				getListaSelectItemConfiguracaoFinanceiroCartao().clear();
				getListaSelectItemTipoFinanciamento().clear();
				getListaSelectItemQuantidadeParcelas().clear();
			} else {
				getMapaPendenciaCartaoCreditoVO().getFormaPagamentoNegociacaoRecebimentoVO().setFormaPagamento(getFacadeFactory().getFormaPagamentoFacade().consultarPorChavePrimaria(getMapaPendenciaCartaoCreditoVO().getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamento().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
				montarListaSelectItemConfiguracaoFinanceiroCartao();
				realizarMontagemCartaoCredito();
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void carregarMapaPendenciaParaRealizarAlteracaoNegociacaoRecebimento() {
		try {
			MapaPendenciaCartaoCreditoVO obj = (MapaPendenciaCartaoCreditoVO) context().getExternalContext().getRequestMap().get("mapaPendenciaCartaoCreditoItens");
			setMapaPendenciaCartaoCreditoVO(getFacadeFactory().getMapaPendenciaCartaoCreditoFacade().consultarPorCodigoFormaPagamentoNegociacaoRecebimentoCartaoCredito(obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getCodigo(), getUsuarioLogado()));
			setExisteFormaPagamentoNegociacaoRecebimentoRecebida(getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoFacade().consultarSeExisteFormaPagamentoNegociacaoRecebimentoRecebidaCartaoCredito(getMapaPendenciaCartaoCreditoVO().getFormaPagamentoNegociacaoRecebimentoVO().getNegociacaoRecebimentoVO().getCodigo(), getMapaPendenciaCartaoCreditoVO().getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamento().getCodigo(), getMapaPendenciaCartaoCreditoVO().getFormaPagamentoNegociacaoRecebimentoVO().getContaCorrente().getCodigo(), getMapaPendenciaCartaoCreditoVO().getFormaPagamentoNegociacaoRecebimentoVO().getOperadoraCartaoVO().getCodigo(), "RE", null, getUsuarioLogado()));
			Uteis.checkState(Uteis.isAtributoPreenchido(getMapaPendenciaCartaoCreditoVO().getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getChaveDaTransacao()), "");
			getMapaPendenciaCartaoCreditoVO().getOperadoraCartaoVO().setCodigo(getMapaPendenciaCartaoCreditoVO().getFormaPagamentoNegociacaoRecebimentoVO().getOperadoraCartaoVO().getCodigo());
			getMapaPendenciaCartaoCreditoVO().getContaCorrenteVO().setCodigo(getMapaPendenciaCartaoCreditoVO().getFormaPagamentoNegociacaoRecebimentoVO().getContaCorrente().getCodigo());
			getMapaPendenciaCartaoCreditoVO().getFormaPagamentoVO().setCodigo(getMapaPendenciaCartaoCreditoVO().getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamento().getCodigo());
			getMapaPendenciaCartaoCreditoVO().getFormaPagamentoNegociacaoRecebimentoVO().getNegociacaoRecebimentoVO().setNivelMontarDados(NivelMontarDados.NAO_INICIALIZADO);
			getMapaPendenciaCartaoCreditoVO().getFormaPagamentoNegociacaoRecebimentoVO().setNegociacaoRecebimentoVO(getFacadeFactory().getNegociacaoRecebimentoFacade().carregarDados(getMapaPendenciaCartaoCreditoVO().getFormaPagamentoNegociacaoRecebimentoVO().getNegociacaoRecebimentoVO(), NivelMontarDados.TODOS, getUsuarioLogado()));
			getMapaPendenciaCartaoCreditoVO().getFormaPagamentoNegociacaoRecebimentoVO().setNegociacaoRecebimento(getMapaPendenciaCartaoCreditoVO().getFormaPagamentoNegociacaoRecebimentoVO().getNegociacaoRecebimentoVO().getCodigo());
			getMapaPendenciaCartaoCreditoVO().carregarTotalPorContaCorrentePorOperadoraCartao();
			montarListaSelectItemFormaPagamentoAlteracaoNegociacaoRecebimento();
			montarListaSelectItemConfiguracaoFinanceiroCartao();
			realizarMontagemCartaoCredito();
			setOncompleteModal("RichFaces.$('panelAlterarNegociacaoRecebimento').show();");
		} catch (Exception e) {
			setMapaPendenciaCartaoCreditoVO(new MapaPendenciaCartaoCreditoVO());
			setOncompleteModal("RichFaces.$('panelAlterarNegociacaoRecebimento').hide();");
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void realizarGeracaoFormaPagamentoNegociacaoRecebimento() {
		try {
			getMapaPendenciaCartaoCreditoVO().getListaFormaPagamentoNegociacaoRecebimentoVO().clear();
			getMapaPendenciaCartaoCreditoVO().getFormaPagamentoNegociacaoRecebimentoVO().setFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO(getMapaPendenciaCartaoCreditoVO().getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO());
			if (getMapaPendenciaCartaoCreditoVO().getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getTipoFinanciamentoEnum().equals(TipoFinanciamentoEnum.INSTITUICAO)) {
				getMapaPendenciaCartaoCreditoVO().setListaFormaPagamentoNegociacaoRecebimentoVO(getFacadeFactory().getNegociacaoRecebimentoFacade().preencherFormaPagamentoNegociacaoRecebimentoCartaoCredito(getMapaPendenciaCartaoCreditoVO().getFormaPagamentoNegociacaoRecebimentoVO(), getMapaPendenciaCartaoCreditoVO().getFormaPagamentoNegociacaoRecebimentoVO().getNegociacaoRecebimentoVO(), getMapaPendenciaCartaoCreditoVO().getTotalPorFormaPagamentoNegociacaoRecebimento(), getUsuarioLogado()));
			} else {
				getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoFacade().preencherFormaPagamentoNegociacaoRecebimentoCartaoCredito(getMapaPendenciaCartaoCreditoVO().getFormaPagamentoNegociacaoRecebimentoVO(), getMapaPendenciaCartaoCreditoVO().getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO(), getMapaPendenciaCartaoCreditoVO().getFormaPagamentoNegociacaoRecebimentoVO().getConfiguracaoFinanceiroCartaoVO(), getMapaPendenciaCartaoCreditoVO().getFormaPagamentoNegociacaoRecebimentoVO().getNegociacaoRecebimentoVO().getData(), getMapaPendenciaCartaoCreditoVO().getFormaPagamentoNegociacaoRecebimentoVO().getQtdeParcelasCartaoCredito(), getMapaPendenciaCartaoCreditoVO().getFormaPagamentoNegociacaoRecebimentoVO().getQtdeParcelasCartaoCredito(), getMapaPendenciaCartaoCreditoVO().getTotalPorFormaPagamentoNegociacaoRecebimento(), getUsuarioLogado());
				getMapaPendenciaCartaoCreditoVO().getListaFormaPagamentoNegociacaoRecebimentoVO().add(getMapaPendenciaCartaoCreditoVO().getFormaPagamentoNegociacaoRecebimentoVO());
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}

	}

	public void persitirAlteracoesNegociacaoRecebimento() {
		try {
			ConfiguracaoFinanceiroVO configuracaoFinanceiro = getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarPorUnidadeEnsino(getMapaPendenciaCartaoCreditoVO().getFormaPagamentoNegociacaoRecebimentoVO().getNegociacaoRecebimentoVO().getUnidadeEnsino().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			if (!Uteis.isAtributoPreenchido(configuracaoFinanceiro)) {
				configuracaoFinanceiro = configuracaoFinanceiroUnidadeLogada();
			}
			getFacadeFactory().getMapaPendenciaCartaoCreditoFacade().persitirAlteracoesNegociacaoRecebimento(getMapaPendenciaCartaoCreditoVO(), configuracaoFinanceiro, getUsuarioLogado());
			consultar();
			setMapaPendenciaCartaoCreditoVO(new MapaPendenciaCartaoCreditoVO());
			setOncompleteModal("RichFaces.$('panelAlterarNegociacaoRecebimento').hide();");
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setOncompleteModal("RichFaces.$('panelAlterarNegociacaoRecebimento').show();");
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setOncompleteModal("RichFaces.$('panelAlterarNegociacaoRecebimento').show();");
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void persitirAlteracoesAjusteValorLiquido() {
		try {
			getMapaPendenciaCartaoCreditoVO().getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setResponsavelAjustarValorLiquido(getUsuarioLogadoClone());
			getFacadeFactory().getMapaPendenciaCartaoCreditoFacade().persitirAlteracoesAjusteValorLiquido(getMapaPendenciaCartaoCreditoVO(), getUsuarioLogado());
			consultar();
			setMapaPendenciaCartaoCreditoVO(new MapaPendenciaCartaoCreditoVO());
			setOncompleteModal("RichFaces.$('panelAlterarNegociacaoRecebimento').hide();");
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setOncompleteModal("RichFaces.$('panelAlterarNegociacaoRecebimento').show();");
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setOncompleteModal("RichFaces.$('panelAlterarNegociacaoRecebimento').show();");
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void montarListaSelectItemContaCorrente() {
		List<ContaCorrenteVO> resultadoConsulta = null;
		getListaSelectItemContaCorrente().clear();
		try {
			resultadoConsulta = getFacadeFactory().getContaCorrenteFacade().consultarContaCorrenteCaixaPorNumeroPorUnidadeEnsino("", getUnidadeEnsinoVOMarcadasParaSeremUtilizadas(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			getListaSelectItemContaCorrente().add(new SelectItem(0, ""));
			resultadoConsulta.stream().forEach(p -> {
				if (Uteis.isAtributoPreenchido(p.getNomeApresentacaoSistema())) {
					getListaSelectItemContaCorrente().add(new SelectItem(p.getCodigo(), p.getNomeApresentacaoSistema()));
				} else {
					getListaSelectItemContaCorrente().add(new SelectItem(p.getCodigo(), p.getNomeBancoAgenciaContaApresentar()));
				}
			});
			if (resultadoConsulta.size() == 1) {
				setContaCorrenteVO(resultadoConsulta.get(0));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
		}
	}

	public void montarListaSelectItemContaCorrente(String nrBanco, String numeroContaCorrente, String digitoContaCorrente) {
		List<ContaCorrenteVO> resultadoConsulta = null;
		getListaSelectItemContaCorrente().clear();
		try {
			resultadoConsulta = getFacadeFactory().getContaCorrenteFacade().consultarPorBancoPorNumeroContaCorrentePorDigitoContaCorrente(nrBanco, numeroContaCorrente, digitoContaCorrente, "", 0, true, false, Uteis.NIVELMONTARDADOS_DADOSCONSULTARTODOS, getUsuarioLogado());
			getListaSelectItemContaCorrente().add(new SelectItem(0, ""));
			resultadoConsulta.stream().forEach(p -> {
				if (Uteis.isAtributoPreenchido(p.getNomeApresentacaoSistema())) {
					getListaSelectItemContaCorrente().add(new SelectItem(p.getCodigo(), p.getNomeApresentacaoSistema()));
				} else {
					getListaSelectItemContaCorrente().add(new SelectItem(p.getCodigo(), p.getNomeBancoAgenciaContaApresentar()));
				}
			});
			if (resultadoConsulta.size() == 1) {
				setContaCorrenteVO(resultadoConsulta.get(0));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
		}
	}

	public void montarListaSelectItemFormaPagamentoAlteracaoNegociacaoRecebimento() {
		List<FormaPagamentoVO> resultadoConsulta = null;
		getListaSelectItemFormaPagamento().clear();
		try {
			resultadoConsulta = getFacadeFactory().getFormaPagamentoFacade().consultarFormaPagamentoCartoesCredito(false, true, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			getListaSelectItemFormaPagamento().add(new SelectItem(0, ""));
			resultadoConsulta.stream().forEach(p -> {
				getListaSelectItemFormaPagamento().add(new SelectItem(p.getCodigo(), p.getNome()));
			});
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
		}
	}

	public void montarListaSelectItemConfiguracaoFinanceiroCartao() {
		List<ConfiguracaoFinanceiroCartaoVO> resultadoConsulta = null;
		getListaSelectItemConfiguracaoFinanceiroCartao().clear();
		try {
			ConfiguracaoFinanceiroVO configuracaoFinanceiro = getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarPorUnidadeEnsino(getMapaPendenciaCartaoCreditoVO().getFormaPagamentoNegociacaoRecebimentoVO().getNegociacaoRecebimentoVO().getUnidadeEnsino().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			if (!Uteis.isAtributoPreenchido(configuracaoFinanceiro)) {
				configuracaoFinanceiro = configuracaoFinanceiroUnidadeLogada();
			}
			resultadoConsulta = getFacadeFactory().getConfiguracaoFinanceiroCartaoFacade().consultarPorTipoOperadoraCartaoConfiguracaoFinanceiro("CARTAO_CREDITO", configuracaoFinanceiro.getCodigo(), false, 0, false, getUsuarioLogado());
			getListaSelectItemConfiguracaoFinanceiroCartao().add(new SelectItem(0, ""));			
			resultadoConsulta.stream().forEach(p -> {
				getListaSelectItemConfiguracaoFinanceiroCartao().add(new SelectItem(p.getCodigo(), p.getContaCorrenteVO().getBancoAgenciaContaCorrente() +" - "+ p.getOperadoraCartaoVO().getNome() + " - " + p.getOperadoraCartaoVO().getOperadoraCartaoCreditoApresentar()));
			});
			if (!Uteis.isAtributoPreenchido(getMapaPendenciaCartaoCreditoVO().getFormaPagamentoNegociacaoRecebimentoVO().getConfiguracaoFinanceiroCartaoVO())) {
				resultadoConsulta.stream().filter(p -> p.getOperadoraCartaoVO().getCodigo().equals(getMapaPendenciaCartaoCreditoVO().getOperadoraCartaoVO().getCodigo())).forEach(p -> getMapaPendenciaCartaoCreditoVO().getFormaPagamentoNegociacaoRecebimentoVO().getConfiguracaoFinanceiroCartaoVO().setCodigo(p.getCodigo()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
		}
	}

	public void realizarMontagemCartaoCredito() {
		try {
			getListaSelectItemQuantidadeParcelas().clear();
			getListaSelectItemTipoFinanciamento().clear();
			getMapaPendenciaCartaoCreditoVO().getListaFormaPagamentoNegociacaoRecebimentoVO().clear();
			if (Uteis.isAtributoPreenchido(getMapaPendenciaCartaoCreditoVO().getFormaPagamentoNegociacaoRecebimentoVO().getConfiguracaoFinanceiroCartaoVO())) {
				getMapaPendenciaCartaoCreditoVO().getFormaPagamentoNegociacaoRecebimentoVO().setConfiguracaoFinanceiroCartaoVO(getFacadeFactory().getConfiguracaoFinanceiroCartaoFacade().consultarPorChavePrimaria(getMapaPendenciaCartaoCreditoVO().getFormaPagamentoNegociacaoRecebimentoVO().getConfiguracaoFinanceiroCartaoVO().getCodigo(), NivelMontarDados.TODOS, getUsuarioLogado()));
				getMapaPendenciaCartaoCreditoVO().getFormaPagamentoNegociacaoRecebimentoVO().setOperadoraCartaoVO(getMapaPendenciaCartaoCreditoVO().getFormaPagamentoNegociacaoRecebimentoVO().getConfiguracaoFinanceiroCartaoVO().getOperadoraCartaoVO());
				getMapaPendenciaCartaoCreditoVO().getFormaPagamentoNegociacaoRecebimentoVO().setCategoriaDespesaVO(getMapaPendenciaCartaoCreditoVO().getFormaPagamentoNegociacaoRecebimentoVO().getConfiguracaoFinanceiroCartaoVO().getCategoriaDespesaVO());
				getMapaPendenciaCartaoCreditoVO().getFormaPagamentoNegociacaoRecebimentoVO().setContaCorrente(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(getMapaPendenciaCartaoCreditoVO().getFormaPagamentoNegociacaoRecebimentoVO().getConfiguracaoFinanceiroCartaoVO().getContaCorrenteVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
				getMapaPendenciaCartaoCreditoVO().getFormaPagamentoNegociacaoRecebimentoVO().setContaCorrenteOperadoraCartaoVO(getMapaPendenciaCartaoCreditoVO().getFormaPagamentoNegociacaoRecebimentoVO().getContaCorrente());
				getMapaPendenciaCartaoCreditoVO().getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setConfiguracaoFinanceiroCartaoVO(getMapaPendenciaCartaoCreditoVO().getFormaPagamentoNegociacaoRecebimentoVO().getConfiguracaoFinanceiroCartaoVO());
				montarListaSelectItemQuantidadeParcelasConfiguracaoFinanceiroCartao();
				montarListaSelectItemTipoFinanciamento();
				realizarGeracaoFormaPagamentoNegociacaoRecebimento();
			}
			limparMensagem();
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	private void montarListaSelectItemQuantidadeParcelasConfiguracaoFinanceiroCartao() {
		getListaSelectItemQuantidadeParcelas().clear();
		for (Integer i = 1; i <= getMapaPendenciaCartaoCreditoVO().getFormaPagamentoNegociacaoRecebimentoVO().getConfiguracaoFinanceiroCartaoVO().getQuantidadeParcelasCartaoCredito(); i++) {
			getListaSelectItemQuantidadeParcelas().add(new SelectItem(i, i.toString()));
		}
		if (!Uteis.isAtributoPreenchido(getMapaPendenciaCartaoCreditoVO().getFormaPagamentoNegociacaoRecebimentoVO().getQtdeParcelasCartaoCredito())) {
			getMapaPendenciaCartaoCreditoVO().getFormaPagamentoNegociacaoRecebimentoVO().setQtdeParcelasCartaoCredito(1);
		}
	}

	public void montarListaSelectItemTipoFinanciamento() {
		getListaSelectItemTipoFinanciamento().clear();
		getListaSelectItemTipoFinanciamento().add(new SelectItem(TipoFinanciamentoEnum.INSTITUICAO, TipoFinanciamentoEnum.INSTITUICAO.getValorApresentar()));
		getListaSelectItemTipoFinanciamento().add(new SelectItem(TipoFinanciamentoEnum.OPERADORA, TipoFinanciamentoEnum.OPERADORA.getValorApresentar()));
	}

	public void montarListaSelectItemFormaPagamentoBaixarPendenciaCartao() {
		List<FormaPagamentoVO> resultadoConsulta = null;
		getListaSelectItemFormaPagamento().clear();
		try {
			resultadoConsulta = getFacadeFactory().getFormaPagamentoFacade().consultarPorTipo("DE", false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			getListaSelectItemFormaPagamento().add(new SelectItem(0, ""));
			resultadoConsulta.stream().forEach(p -> {
				getListaSelectItemFormaPagamento().add(new SelectItem(p.getCodigo(), p.getNome()));
			});
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
		}
	}

	/**
	 * Rotina responsável por preencher a combo de filtro pos situação.
	 */
	public List<SelectItem> getListaSelectItemSituacaoCartaoCredito() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("AR", "A Receber"));
		itens.add(new SelectItem("RE", "Recebida"));
		itens.add(new SelectItem("TO", "Todas"));
		return itens;
	}

	/**
	 * Rotina responsável por preencher a combo de filtro por data.
	 */
	public List<SelectItem> getListaSelectItemFiltroData() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("PR", "Data de Previsão"));
		itens.add(new SelectItem("EM", "Data de Emissão"));
		itens.add(new SelectItem("TO", "Data Previsão e Emissão"));
		if (getSituacaoCartaoCredito().equals("RE")) {
			itens.add(new SelectItem("RO", "Data Recebimento Operadora"));
		}
		return itens;
	}

	public List<SelectItem> getListaSelectItemOrdenarPor() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("PR", "Data de Previsão"));
		itens.add(new SelectItem("EM", "Data de Emissão"));
		itens.add(new SelectItem("OP", "Operadora"));
		itens.add(new SelectItem("NO", "Nome"));
		return itens;
	}

	public void validarDadosConsulta() throws Exception {
		if (getExecutarConsultaDetalhada()) {
			if (getTipoData().equals("PR")) {
				if ((getDataPrevisaoInicial() == null) || (getDataPrevisaoFinal() == null)) {
					throw new ConsistirException("Informe o período de Previsão para efetuar a consulta.");
				} else if (getDataPrevisaoFinal().before(getDataPrevisaoInicial())) {
					throw new ConsistirException("A Data Previsão Final deve ser Maior que a Data Previsão Inicial.");
				}
			}
			if (getTipoData().equals("EM") && ((getDataEmissaoInicial() == null) || (getDataEmissaoFinal() == null))) {
				throw new ConsistirException("Informe o período de Emissão para efetuar a consulta.");
			}
			if (getTipoData().equals("TO") && ((getDataPrevisaoInicial() == null || getDataPrevisaoFinal() == null) || (getDataEmissaoInicial() == null || getDataEmissaoFinal() == null))) {
				throw new ConsistirException("Informe o período de Previsão e da Emissão para efetuar a consulta.");
			}
		}
	}

	public void iniciarFiltroConsulta() {
		if (getExecutarConsultaDetalhada()) {
			setTipoData("PR");
		}
	}

	/**
	 * @return the selecionarTodasParcelas
	 */
	public Boolean getSelecionarTodasParcelas() {
		return selecionarTodasParcelas;
	}

	/**
	 * @param selecionarTodasParcelas the selecionarTodasParcelas to set
	 */
	public void setSelecionarTodasParcelas(Boolean selecionarTodasParcelas) {
		this.selecionarTodasParcelas = selecionarTodasParcelas;
	}

	public Boolean getExecutarConsultaDetalhada() {
		if (executarConsultaDetalhada == null) {
			executarConsultaDetalhada = false;
		}
		return executarConsultaDetalhada;
	}

	public void setExecutarConsultaDetalhada(Boolean executarConsultaDetalhada) {
		this.executarConsultaDetalhada = executarConsultaDetalhada;
	}

	/**
	 * @return the valorAReceberTotal
	 */
	public Double getValorAReceberTotal() {
		if (valorAReceberTotal == null) {
			valorAReceberTotal = 0.0;
		}
		return valorAReceberTotal;
	}

	/**
	 * @param valorAReceberTotal the valorAReceberTotal to set
	 */
	public void setValorAReceberTotal(Double valorAReceberTotal) {
		this.valorAReceberTotal = valorAReceberTotal;
	}

	public Double getValorLiquidoTotal() {
		if (valorLiquidoTotal == null) {
			valorLiquidoTotal = 0.0;
		}
		return valorLiquidoTotal;
	}

	public void setValorLiquidoTotal(Double valorLiquidoTotal) {
		this.valorLiquidoTotal = valorLiquidoTotal;
	}

	public List<MapaPendenciaCartaoCreditoVO> getListaMapaPendenciaCartaoCreditoVO() {
		if (listaMapaPendenciaCartaoCreditoVO == null) {
			listaMapaPendenciaCartaoCreditoVO = new ArrayList<>(0);
		}
		return listaMapaPendenciaCartaoCreditoVO;
	}

	public void setListaMapaPendenciaCartaoCreditoVO(List<MapaPendenciaCartaoCreditoVO> listaMapaPendenciaCartaoCreditoVO) {
		this.listaMapaPendenciaCartaoCreditoVO = listaMapaPendenciaCartaoCreditoVO;
	}

	public Integer getQtdeParcelasSelecionadas() {
		if (qtdeParcelasSelecionadas == null) {
			qtdeParcelasSelecionadas = 0;
		}
		return qtdeParcelasSelecionadas;
	}

	public void setQtdeParcelasSelecionadas(Integer qtdeParcelasSelecionadas) {
		this.qtdeParcelasSelecionadas = qtdeParcelasSelecionadas;
	}

	public Date getDataEmissaoInicial() {
		return dataEmissaoInicial;
	}

	public void setDataEmissaoInicial(Date dataEmissaoInicial) {
		this.dataEmissaoInicial = dataEmissaoInicial;
	}

	public Date getDataEmissaoFinal() {
		return dataEmissaoFinal;
	}

	public void setDataEmissaoFinal(Date dataEmissaoFinal) {
		this.dataEmissaoFinal = dataEmissaoFinal;
	}

	public Date getDataPrevisaoInicial() {
		return dataPrevisaoInicial;
	}

	public void setDataPrevisaoInicial(Date dataPrevisaoInicial) {
		this.dataPrevisaoInicial = dataPrevisaoInicial;
	}

	public Date getDataPrevisaoFinal() {
		return dataPrevisaoFinal;
	}

	public void setDataPrevisaoFinal(Date dataPrevisaoFinal) {
		this.dataPrevisaoFinal = dataPrevisaoFinal;
	}

	public Date getDataRecebimentoOperadoraInicial() {
		return dataRecebimentoOperadoraInicial;
	}

	public void setDataRecebimentoOperadoraInicial(Date dataRecebimentoOperadoraInicial) {
		this.dataRecebimentoOperadoraInicial = dataRecebimentoOperadoraInicial;
	}

	public Date getDataRecebimentoOperadoraFinal() {
		return dataRecebimentoOperadoraFinal;
	}

	public void setDataRecebimentoOperadoraFinal(Date dataRecebimentoOperadoraFinal) {
		this.dataRecebimentoOperadoraFinal = dataRecebimentoOperadoraFinal;
	}

	public String getTipoData() {
		if (tipoData == null) {
			tipoData = "";
		}
		return tipoData;
	}

	public void setTipoData(String tipoData) {
		this.tipoData = tipoData;
	}

	public String getSituacaoCartaoCredito() {
		if (situacaoCartaoCredito == null) {
			situacaoCartaoCredito = "TO";
		}
		return situacaoCartaoCredito;
	}

	public void setSituacaoCartaoCredito(String situacaoCartaoCredito) {
		this.situacaoCartaoCredito = situacaoCartaoCredito;
	}

	public boolean getIsFiltrarPorDataEmissao() {
		return getExecutarConsultaDetalhada() && (getTipoData().equals("TO") || getTipoData().equals("EM"));
	}

	public boolean getIsFiltrarPorDataPrevisao() {
		return getExecutarConsultaDetalhada() && (getTipoData().equals("TO") || getTipoData().equals("PR"));
	}

	public boolean isFiltrarPorDataRecebimentoOperadora() {
		return getExecutarConsultaDetalhada() && getSituacaoCartaoCredito().equals("RE") && (getTipoData().equals("RO"));
	}

	public boolean getIsPossuiParcelas() {
		return !getListaMapaPendenciaCartaoCreditoVO().isEmpty();

	}

	/**
	 * @return the ordenarPor
	 */
	public String getOrdenarPor() {
		if (ordenarPor == null) {
			ordenarPor = "NO";
		}
		return ordenarPor;
	}

	/**
	 * @param ordenarPor the ordenarPor to set
	 */
	public void setOrdenarPor(String ordenarPor) {
		this.ordenarPor = ordenarPor;
	}

	public Date getDataVencimento() {
		if (dataVencimento == null) {
			dataVencimento = new Date();
		}
		return dataVencimento;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public Boolean getCriarContaUnidadeMatriz() {
		if (criarContaUnidadeMatriz == null) {
			criarContaUnidadeMatriz = Boolean.FALSE;
		}
		return criarContaUnidadeMatriz;
	}

	public void setCriarContaUnidadeMatriz(Boolean criarContaUnidadeMatriz) {
		this.criarContaUnidadeMatriz = criarContaUnidadeMatriz;
	}

	public ContaCorrenteVO getContaCorrenteVO() {
		if (contaCorrenteVO == null) {
			contaCorrenteVO = new ContaCorrenteVO();
		}
		return contaCorrenteVO;
	}

	public void setContaCorrenteVO(ContaCorrenteVO contaCorrenteVO) {
		this.contaCorrenteVO = contaCorrenteVO;
	}

	public List<SelectItem> getListaSelectItemContaCorrente() {
		if (listaSelectItemContaCorrente == null) {
			listaSelectItemContaCorrente = new ArrayList<>();
		}
		return listaSelectItemContaCorrente;
	}

	public void setListaSelectItemContaCorrente(List<SelectItem> listaSelectItemContaCorrente) {
		this.listaSelectItemContaCorrente = listaSelectItemContaCorrente;
	}

	public List<SelectItem> getListaSelectItemConfiguracaoFinanceiroCartao() {
		if (listaSelectItemConfiguracaoFinanceiroCartao == null) {
			listaSelectItemConfiguracaoFinanceiroCartao = new ArrayList<>();
		}
		return listaSelectItemConfiguracaoFinanceiroCartao;
	}

	public void setListaSelectItemConfiguracaoFinanceiroCartao(List<SelectItem> listaSelectItemConfiguracaoFinanceiroCartao) {
		this.listaSelectItemConfiguracaoFinanceiroCartao = listaSelectItemConfiguracaoFinanceiroCartao;
	}

	public List<SelectItem> getListaSelectItemQuantidadeParcelas() {
		if (listaSelectItemQuantidadeParcelas == null) {
			listaSelectItemQuantidadeParcelas = new ArrayList<>();
		}
		return listaSelectItemQuantidadeParcelas;
	}

	public void setListaSelectItemQuantidadeParcelas(List<SelectItem> listaSelectItemQuantidadeParcelas) {
		this.listaSelectItemQuantidadeParcelas = listaSelectItemQuantidadeParcelas;
	}

	public List<SelectItem> getListaSelectItemTipoFinanciamento() {
		if (listaSelectItemTipoFinanciamento == null) {
			listaSelectItemTipoFinanciamento = new ArrayList<>(0);
		}
		return listaSelectItemTipoFinanciamento;
	}

	public void setListaSelectItemTipoFinanciamento(List<SelectItem> listaSelectItemTipoFinanciamento) {
		this.listaSelectItemTipoFinanciamento = listaSelectItemTipoFinanciamento;
	}

	public Double getValorTaxaOperadoraTotal() {
		if (valorTaxaOperadoraTotal == null) {
			valorTaxaOperadoraTotal = 0.0;
		}
		return valorTaxaOperadoraTotal;
	}

	public void setValorTaxaOperadoraTotal(Double valorTaxaOperadoraTotal) {
		this.valorTaxaOperadoraTotal = valorTaxaOperadoraTotal;
	}

	public String consultarDCC() {
		try {
			List<UnidadeEnsinoVO> listaUnidadeEnsinoTemp = getUnidadeEnsinoVOMarcadasParaSeremUtilizadas();
			List<OperadoraCartaoVO> listaOperadoraCartaoTemp = getOperadoraCartaoVOMarcadasParaSeremUtilizadas();
			setSelecionarTodasParcelas(false);
			validarDadosConsulta();
			setQtdeParcelasSelecionadas(0);
			setValorAReceberTotal(0.0);
			setValorTaxaOperadoraTotal(0.0);
			setValorLiquidoTotal(0.0);
			getListaMapaPendenciaCartaoCreditoVO().clear();
			if (!getExecutarConsultaDetalhada()) {
				setDataEmissaoInicial(null);
				setDataEmissaoFinal(null);
				setDataPrevisaoInicial(Uteis.getDataPrimeiroDiaMes(new Date()));
				setDataPrevisaoFinal(Uteis.getDataUltimoDiaMes(new Date()));
				setListaMapaPendenciaCartaoCreditoVO(getFacadeFactory().getMapaPendenciaCartaoCreditoFacade().consultarPorParcelasCartaoCredito(getSituacaoCartaoCredito(), getDataEmissaoInicial(), getDataEmissaoFinal(), getDataPrevisaoInicial(), getDataPrevisaoFinal(), getDataRecebimentoOperadoraInicial(), getDataRecebimentoOperadoraFinal(), listaUnidadeEnsinoTemp, listaOperadoraCartaoTemp, getContaCorrenteVO().getCodigo(), ordenarPor, false, getUsuarioLogado()));
			} else {
				if (getTipoData().equals("EM")) {
					setDataPrevisaoInicial(null);
					setDataPrevisaoFinal(null);
					setDataRecebimentoOperadoraInicial(null);
					setDataRecebimentoOperadoraFinal(null);
				} else if (getTipoData().equals("PR")) {
					setDataEmissaoInicial(null);
					setDataEmissaoFinal(null);
					setDataRecebimentoOperadoraInicial(null);
					setDataRecebimentoOperadoraFinal(null);
				} else if (getTipoData().equals("RO")) {
					setDataPrevisaoInicial(null);
					setDataPrevisaoFinal(null);
					setDataEmissaoInicial(null);
					setDataEmissaoFinal(null);
				}
				setListaMapaPendenciaCartaoCreditoVO(getFacadeFactory().getMapaPendenciaCartaoCreditoFacade().consultarPorParcelasCartaoCredito(getSituacaoCartaoCredito(), getDataEmissaoInicial(), getDataEmissaoFinal(), getDataPrevisaoInicial(), getDataPrevisaoFinal(), getDataRecebimentoOperadoraInicial(), getDataRecebimentoOperadoraFinal(), listaUnidadeEnsinoTemp, listaOperadoraCartaoTemp, getContaCorrenteVO().getCodigo(), ordenarPor, false, getUsuarioLogado()));

			}
			if (getListaMapaPendenciaCartaoCreditoVO().isEmpty()) {
				setMensagemID("msg_erro_dadosnaoencontrados", Uteis.ALERTA);
			} else {
				setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
			}
			return "consultar";
		} catch (Exception e) {
			setListaConsulta(new ArrayList<MapaPendenciaCartaoCreditoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return "consultar";
		}
	}

	public void montarDadosCancelamentoDCC() {
		try {
			MapaPendenciaCartaoCreditoVO obj = (MapaPendenciaCartaoCreditoVO) context().getExternalContext().getRequestMap().get("mapaPendenciaCartaoCreditoPrevistoVO");
			setMapaPendenciaCartaoCreditoVO(obj);
			setFormaPagamentoNegociacaoRecebimentoVO(obj.getFormaPagamentoNegociacaoRecebimentoVO());
			setFormaPagamentoNegociacaoRecebimentoVO(getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoDCCFacade().consultarPorChavePrimaria(getFormaPagamentoNegociacaoRecebimentoVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void realizarCancelamentoDCCPrevisto() {
		try {
			if (getFormaPagamentoNegociacaoRecebimentoVO().getMotivoCancelamento().isEmpty()) {
				throw new Exception(UteisJSF.internacionalizar("msg_MapaPendenciaCartaoCredito_motivoCancelamento"));
			}
			List<FormaPagamentoNegociacaoRecebimentoVO> formaPagamentoNegociacaoRecebimentoVOs = getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoDCCFacade().consultarFormaPagamentoNegociacaoRecebimentoVOSDCCPorCodigoContaReceber(getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getContaReceberVO().getCodigo(), getUsuarioLogado());
			for (FormaPagamentoNegociacaoRecebimentoVO obj : formaPagamentoNegociacaoRecebimentoVOs) {
				getFacadeFactory().getGerenciamentoDeTransacaoCartaoDeCreditoFacade().realizarCancelamentoOuEstornoTransacaoEspecificaCartaoCredito(obj, getUsuarioLogado());
				getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoDCCFacade().incluirMotivoCancelamento(obj, getUsuarioLogado());
				getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoCartaoCreditoFacade().alterar(obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO(), getUsuarioLogado());
			}
			getFacadeFactory().getContaReceberFacade().alterarSituacaoPagoDCCFalso(getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getContaReceberVO().getCodigo());
			consultar();
			setMensagemID("msg_MapaPendenciaCartaoCredito_dccCanceladoComSucesso", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void realizarCancelamentoCartaoCreditoOperadora() {
		try {
			if (getFormaPagamentoNegociacaoRecebimentoVO().getMotivoCancelamento().isEmpty()) {
				throw new Exception(UteisJSF.internacionalizar("msg_MapaPendenciaCartaoCredito_motivoCancelamento"));
			}
			getFacadeFactory().getGerenciamentoDeTransacaoCartaoDeCreditoFacade().realizarCancelamentoOuEstornoTransacaoEspecificaCartaoCredito(getFormaPagamentoNegociacaoRecebimentoVO(), getUsuarioLogado());
			getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoFacade().incluirMotivoCancelamento(getFormaPagamentoNegociacaoRecebimentoVO(), getUsuarioLogado());
			getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoCartaoCreditoFacade().alterar(getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO(), getUsuarioLogado());
			setMensagemID("msg_MapaPendenciaCartaoCredito_cartaoCanceladoComSucesso", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void realizarBaixaCancelamentoPendente() {
		try {
			MapaPendenciaCartaoCreditoVO obj = (MapaPendenciaCartaoCreditoVO) context().getExternalContext().getRequestMap().get("mapaPendenciaCartaoCreditoCancelamentoPendenteVO");
			setFormaPagamentoNegociacaoRecebimentoVO(obj.getFormaPagamentoNegociacaoRecebimentoVO());
			setFormaPagamentoNegociacaoRecebimentoVO(getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoDCCFacade().consultarPorChavePrimaria(getFormaPagamentoNegociacaoRecebimentoVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setSituacao(SituacaoContaReceber.CANCELADO_FINANCEIRO.getValor());
			getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoCartaoCreditoFacade().alterar(getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO(), getUsuarioLogado());
			setMensagemID("msg_MapaPendenciaCartaoCredito_cartaoCanceladoComSucesso", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void realizarBaixaEstornoPendente() {
		try {
			MapaPendenciaCartaoCreditoVO obj = (MapaPendenciaCartaoCreditoVO) context().getExternalContext().getRequestMap().get("mapaPendenciaCartaoCreditoEstornoPendenteVO");
			setFormaPagamentoNegociacaoRecebimentoVO(obj.getFormaPagamentoNegociacaoRecebimentoVO());
			setFormaPagamentoNegociacaoRecebimentoVO(getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoDCCFacade().consultarPorChavePrimaria(getFormaPagamentoNegociacaoRecebimentoVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setSituacao(SituacaoContaReceber.CANCELADO_FINANCEIRO.getValor());
			getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoCartaoCreditoFacade().alterar(getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO(), getUsuarioLogado());
			setMensagemID("msg_MapaPendenciaCartaoCredito_cartaoCanceladoComSucesso", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public FormaPagamentoNegociacaoRecebimentoVO getFormaPagamentoNegociacaoRecebimentoVO() {
		if (formaPagamentoNegociacaoRecebimentoVO == null) {
			formaPagamentoNegociacaoRecebimentoVO = new FormaPagamentoNegociacaoRecebimentoVO();
		}
		return formaPagamentoNegociacaoRecebimentoVO;
	}

	public void setFormaPagamentoNegociacaoRecebimentoVO(FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO) {
		this.formaPagamentoNegociacaoRecebimentoVO = formaPagamentoNegociacaoRecebimentoVO;
	}

	public MapaPendenciaCartaoCreditoVO getMapaPendenciaCartaoCreditoVO() {
		if (mapaPendenciaCartaoCreditoVO == null) {
			mapaPendenciaCartaoCreditoVO = new MapaPendenciaCartaoCreditoVO();
		}
		return mapaPendenciaCartaoCreditoVO;
	}

	public void setMapaPendenciaCartaoCreditoVO(MapaPendenciaCartaoCreditoVO mapaPendenciaCartaoCreditoVO) {
		this.mapaPendenciaCartaoCreditoVO = mapaPendenciaCartaoCreditoVO;
	}

	private Boolean usuarioPodeAlterarDataVencimento;

	public void setUsuarioPodeAlterarDataVencimento(Boolean usuarioPodeAlterarDataVencimento) {
		this.usuarioPodeAlterarDataVencimento = usuarioPodeAlterarDataVencimento;
	}

	public Boolean getUsuarioPodeAlterarDataVencimento() {
		if (usuarioPodeAlterarDataVencimento == null) {
			try {
				ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("MapaPendenciaCartaoCredito_permitirAlterarDataVencimento", getUsuarioLogado());
				usuarioPodeAlterarDataVencimento = true;
			} catch (Exception e) {
				usuarioPodeAlterarDataVencimento = false;
			}
		}
		return usuarioPodeAlterarDataVencimento;
	}

	public void alterarDataVencimento() {
		try {
			MapaPendenciaCartaoCreditoVO obj = (MapaPendenciaCartaoCreditoVO) getRequestMap().get("mapaPendenciaCartaoCreditoItens");
			getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoCartaoCreditoFacade().alterarDataVencimento(obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO(), getUsuarioLogado());
			if (obj.getEfetuarBaixa()) {
				getFacadeFactory().getMapaPendenciaCartaoCreditoFacade().realizarCalculoMapaPendenciaCartaCreditoTotal(obj, getMapaPendenciaCartaoCreditoTotalVOs(), false, null);
				obj.setValorTaxaUsar(null);
				getFacadeFactory().getMapaPendenciaCartaoCreditoFacade().realizarCalculoMapaPendenciaCartaCreditoTotal(obj, getMapaPendenciaCartaoCreditoTotalVOs(), true, getDataVencimento());
			} else {
				obj.setValorTaxaUsar(null);
			}
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public Double valorTotalTaxa;

	public Double getValorTotalTaxa() {
		if (valorTotalTaxa == null) {
			valorTotalTaxa = 0.0;
		}
		return valorTotalTaxa;
	}

	public void setValorTotalTaxa(Double valorTotalTaxa) {
		this.valorTotalTaxa = valorTotalTaxa;
	}

	public String getValorTotalTaxa_Apresentar() {
		return "R$ " + Uteis.getDoubleFormatado(getValorTotalTaxa());
	}

	public List<MapaPendenciaCartaoCreditoTotalVO> getMapaPendenciaCartaoCreditoTotalVOs() {
		if (mapaPendenciaCartaoCreditoTotalVOs == null) {
			mapaPendenciaCartaoCreditoTotalVOs = new ArrayList<>(0);
		}
		return mapaPendenciaCartaoCreditoTotalVOs;
	}

	public void setMapaPendenciaCartaoCreditoTotalVOs(List<MapaPendenciaCartaoCreditoTotalVO> mapaPendenciaCartaoCreditoTotalVOs) {
		this.mapaPendenciaCartaoCreditoTotalVOs = mapaPendenciaCartaoCreditoTotalVOs;
	}

	public void realizarCalculoTotal() {
		setQtdeParcelasSelecionadas(0);
		setValorAReceberTotal(0.0);
		setValorTaxaOperadoraTotal(0.0);
		setValorTotalTaxa(0.0);
		setValorLiquidoTotal(0.0);
		for (MapaPendenciaCartaoCreditoTotalVO mapaPendenciaCartaoCreditoTotalVO : getMapaPendenciaCartaoCreditoTotalVOs()) {
			setQtdeParcelasSelecionadas(getQtdeParcelasSelecionadas() + mapaPendenciaCartaoCreditoTotalVO.getQuantidade());
			setValorAReceberTotal(getValorAReceberTotal() + mapaPendenciaCartaoCreditoTotalVO.getValor());
			setValorTotalTaxa(getValorTotalTaxa() + mapaPendenciaCartaoCreditoTotalVO.getValorTaxa());
			setValorLiquidoTotal(getValorLiquidoTotal() + mapaPendenciaCartaoCreditoTotalVO.getValorLiquido());
		}
		setValorAReceberTotal(Uteis.arrendondarForcando2CadasDecimais(getValorAReceberTotal()));
		setValorTotalTaxa(Uteis.arrendondarForcando2CadasDecimais(getValorTotalTaxa()));
		setValorLiquidoTotal(Uteis.arrendondarForcando2CadasDecimais(getValorLiquidoTotal()));
	}

	public void removerMapaPendenciaCartaoCreditoTotal() {
		try {
			MapaPendenciaCartaoCreditoTotalVO mapaPendenciaCartaoCreditoTotalVO = (MapaPendenciaCartaoCreditoTotalVO) getRequestMap().get("total");
			getFacadeFactory().getMapaPendenciaCartaoCreditoFacade().removerMapaPendenciaCartaoPorMapaPendenciaCartaoCreditoTotal(getListaMapaPendenciaCartaoCreditoVO(), mapaPendenciaCartaoCreditoTotalVO, getMapaPendenciaCartaoCreditoTotalVOs());
			realizarCalculoTotal();
			setSelecionarTodasParcelas(false);
			setMensagemID("msg_dados_removidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarMapaPendenciaCartaoCreditoTotal() {
		try {
			MapaPendenciaCartaoCreditoTotalVO mapaPendenciaCartaoCreditoTotalVO = (MapaPendenciaCartaoCreditoTotalVO) getRequestMap().get("total");

			getFacadeFactory().getMapaPendenciaCartaoCreditoFacade().selecionarMapaPendenciaCartaoPorMapaPendenciaCartaoCreditoTotal(getListaMapaPendenciaCartaoCreditoVO(), mapaPendenciaCartaoCreditoTotalVO, getMapaPendenciaCartaoCreditoTotalVOs(), getDataVencimento());
			setSelecionarTodasParcelas(false);
			realizarCalculoTotal();
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public List<SelectItem> getListaSelectItemFormaPagamento() {
		if (listaSelectItemFormaPagamento == null) {
			listaSelectItemFormaPagamento = new ArrayList<>(0);
		}
		return listaSelectItemFormaPagamento;
	}

	public void setListaSelectItemFormaPagamento(List<SelectItem> listaSelectItemFormaPagamento) {
		this.listaSelectItemFormaPagamento = listaSelectItemFormaPagamento;
	}

	public FormaPagamentoVO getFormaPagamentoVO() {
		if (formaPagamentoVO == null) {
			formaPagamentoVO = new FormaPagamentoVO();
		}
		return formaPagamentoVO;
	}

	public void setFormaPagamentoVO(FormaPagamentoVO formaPagamentoVO) {
		this.formaPagamentoVO = formaPagamentoVO;
	}

	public void realizarAtualizacaoCalculo() {
		try {
			setMapaPendenciaCartaoCreditoTotalVOs(getFacadeFactory().getMapaPendenciaCartaoCreditoFacade().realizarCalculoMapaPendenciaCartaCreditoTotal(getDataVencimento(), getListaMapaPendenciaCartaoCreditoVO()));
			realizarCalculoTotal();
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public Double getTaxaTroca() {
		if (taxaTroca == null) {
			taxaTroca = 0.0;
		}
		return taxaTroca;
	}

	public void setTaxaTroca(Double taxaTroca) {
		this.taxaTroca = taxaTroca;
	}

	public Boolean getTaxaAntecipacaoTroca() {
		if (taxaAntecipacaoTroca == null) {
			taxaAntecipacaoTroca = false;
		}
		return taxaAntecipacaoTroca;
	}

	public void setTaxaAntecipacaoTroca(Boolean taxaAntecipacaoTroca) {
		this.taxaAntecipacaoTroca = taxaAntecipacaoTroca;
	}

	public Date getDataPrevisao() {
		if (dataPrevisao == null) {
			dataPrevisao = new Date();
		}
		return dataPrevisao;
	}

	public void setDataPrevisao(Date dataPrevisao) {
		this.dataPrevisao = dataPrevisao;
	}

	public void realizarValidacaoPermiteAlterar(String modalAbrir) {
		try {
			setOncompleteModal("");
			if (getQtdeParcelasSelecionadas().equals(0)) {
				throw new Exception(UteisJSF.internacionalizar("msg_MapaPendenciaCartaoCredito_selecionarParaAlterar"));
			}
			setOncompleteModal("RichFaces.$('" + modalAbrir + "').show();");
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void realizarAtualizacaoDataPrevisao() {
		try {
			setOncompleteModal("");
			getFacadeFactory().getMapaPendenciaCartaoCreditoFacade().realizarAtualizacaoDataPrevisao(getListaMapaPendenciaCartaoCreditoVO(), getDataPrevisao(), getUsuarioLogado());
			realizarAtualizacaoCalculo();
			setOncompleteModal("RichFaces.$('panelAlterarDataPrevisao').hide();");
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	private Boolean usuarioPodeAlterarTaxa;

	public void setUsuarioPodeAlterarTaxa(Boolean usuarioPodeAlterarTaxa) {
		this.usuarioPodeAlterarTaxa = usuarioPodeAlterarTaxa;
	}

	public Boolean getUsuarioPodeAlterarTaxa() {
		if (usuarioPodeAlterarTaxa == null) {
			try {
				ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("MapaPendenciaCartaoCredito_permitirAlterarTaxa", getUsuarioLogado());
				usuarioPodeAlterarTaxa = true;
			} catch (Exception e) {
				usuarioPodeAlterarTaxa = false;
			}
		}
		return usuarioPodeAlterarTaxa;
	}

	public void alterarTaxa() {
		try {
			MapaPendenciaCartaoCreditoVO obj = (MapaPendenciaCartaoCreditoVO) getRequestMap().get("mapaPendenciaCartaoCreditoItens");
			getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoFacade().alterarTaxaCartaoCredito(obj.getFormaPagamentoNegociacaoRecebimentoVO(), getUsuarioLogado());
			if (obj.getEfetuarBaixa()) {
				realizarAtualizacaoCalculo();
			} else {
				obj.setValorTaxaUsar(null);
			}
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void alterarTaxaAntecipacao() {
		try {
			MapaPendenciaCartaoCreditoVO obj = (MapaPendenciaCartaoCreditoVO) getRequestMap().get("mapaPendenciaCartaoCreditoItens");
			getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoFacade().alterarTaxaAntecipacaoCartaoCredito(obj.getFormaPagamentoNegociacaoRecebimentoVO(), getUsuarioLogado());
			if (obj.getEfetuarBaixa()) {
				realizarAtualizacaoCalculo();
			} else {
				obj.setValorTaxaUsar(null);
			}
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void alterarVariasTaxa() {
		try {
			setOncompleteModal("");
			getFacadeFactory().getMapaPendenciaCartaoCreditoFacade().realizarAlteracaoTaxa(getListaMapaPendenciaCartaoCreditoVO(), getTaxaTroca(), getTaxaAntecipacaoTroca(), getUsuarioLogado());
			realizarAtualizacaoCalculo();
			setOncompleteModal("RichFaces.$('panelAlterarTaxa').hide();");
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
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

	public boolean isEfetuarBaixaListaMapaCartao() {
		return efetuarBaixaListaMapaCartao;
	}

	public void setEfetuarBaixaListaMapaCartao(boolean efetuarBaixaListaMapaCartao) {
		this.efetuarBaixaListaMapaCartao = efetuarBaixaListaMapaCartao;
	}

	public Long getQtdeParcelasSelecionadasResumoOperacao() {
		return getMapaPendenciaCartaoCreditoTotalVOs().stream().mapToLong(p -> p.getQuantidadeResumoOperacao(isEfetuarBaixaListaMapaCartao())).reduce(0L, (a, b) -> a + b);
	}

	public Double getValorAReceberTotalResumoOperacao() {
		return getMapaPendenciaCartaoCreditoTotalVOs().stream().mapToDouble(p -> p.getValorResumoOperacao(isEfetuarBaixaListaMapaCartao())).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b));
	}

	public Double getValorTaxaSelecionadasResumoOperacao() {
		return getMapaPendenciaCartaoCreditoTotalVOs().stream().mapToDouble(p -> p.getValorTaxaResumoOperacao(isEfetuarBaixaListaMapaCartao())).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b));
	}

	public Double getValorLiquidoSelecionadasResumoOperacao() {
		return getMapaPendenciaCartaoCreditoTotalVOs().stream().mapToDouble(p -> p.getValorLiquidoResumoOperacao(isEfetuarBaixaListaMapaCartao())).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b));
	}

	public void verificarPermissaoUsuarioAlteracoesNegociacaoRecebimento() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoFinanceiroEnum.PERMITIR_REALIZAR_ALTERACOES_NEGOCIACAO_RECEBIMENTO, getUsuarioLogado());
			permitirRealizarAlteracoesNegociacaoRecebimento = true;
		} catch (Exception e) {
			permitirRealizarAlteracoesNegociacaoRecebimento = false;
		}
	}

	public boolean isPermitirRealizarAlteracoesNegociacaoRecebimento() {
		return permitirRealizarAlteracoesNegociacaoRecebimento;
	}

	public void setPermitirRealizarAlteracoesNegociacaoRecebimento(boolean permitirRealizarAlteracoesNegociacaoRecebimento) {
		this.permitirRealizarAlteracoesNegociacaoRecebimento = permitirRealizarAlteracoesNegociacaoRecebimento;
	}

	public boolean isExisteFormaPagamentoNegociacaoRecebimentoRecebida() {
		return existeFormaPagamentoNegociacaoRecebimentoRecebida;
	}

	public void setExisteFormaPagamentoNegociacaoRecebimentoRecebida(boolean existeFormaPagamentoNegociacaoRecebimentoRecebida) {
		this.existeFormaPagamentoNegociacaoRecebimentoRecebida = existeFormaPagamentoNegociacaoRecebimentoRecebida;
	}

	public List<TransacaoCartaoOnlineVO> getListaTransacaoCartaoOnlineVOs() {
		if (listaTransacaoCartaoOnlineVOs == null) {
			listaTransacaoCartaoOnlineVOs = new ArrayList<TransacaoCartaoOnlineVO>(0);
		}
		return listaTransacaoCartaoOnlineVOs;
	}

	public void setListaTransacaoCartaoOnlineVOs(List<TransacaoCartaoOnlineVO> listaTransacaoCartaoOnlineVOs) {
		this.listaTransacaoCartaoOnlineVOs = listaTransacaoCartaoOnlineVOs;
	}

	public void consultarTransacaoDCC() {
		try {
			setListaTransacaoCartaoOnlineVOs(getFacadeFactory().getTransacaoCartaoOnlineInterfaceFacade().consultarPorFiltrosMapaPendenciaCartaoCreditoDCC(getTipoTransacao(), getUnidadeEnsinoVOMarcadasParaSeremUtilizadas(), getOperadoraCartaoVOMarcadasParaSeremUtilizadas(), getMatriculaTransacaoVO().getMatricula(), getTipoPessoaTransacao(), getPessoaVO(), getParceiroVO(), getFornecedorVO(), getDataInicioTransacao(), getDataFimTransacao(), getChaveTransacaoCartaoCredito(), getUsuarioLogado()));
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void consultarRecorrenciaDCC() {
		try {
			setListaCartaoCreditoDebitoRecorrenciaPessoaVOs(getFacadeFactory().getCartaoCreditoDebitoRecorrenciaPessoaFacade().consultarPorFiltrosMapaPendenciaCartaoCreditoRecorrenciaDCC(getUnidadeEnsinoVOMarcadasParaSeremUtilizadas(), getOperadoraCartaoVOMarcadasParaSeremUtilizadas(), getMatriculaRecorrenciaVO().getMatricula(), getResponsavelFinanceiroVO(), getTipoPessoaRecorrencia(), getNomeCartao(), getMesValidade(), getAnoValidade(), getFormaPadraoDataBaseCartaoRecorrente(), getDiaPadraoPagamento(), getNumeroFinalCartaoCredito(), getUsuarioLogado()));
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void excluirRecorrenciaDCC() {
		CartaoCreditoDebitoRecorrenciaPessoaVO obj = (CartaoCreditoDebitoRecorrenciaPessoaVO) context().getExternalContext().getRequestMap().get("recorrenciaItens");
		try {
			if (Uteis.isAtributoPreenchido(obj.getCodigo())) {
				obj.setSituacao(SituacaoEnum.INATIVO);
				getFacadeFactory().getCartaoCreditoDebitoRecorrenciaPessoaFacade().alterarSituacaoPorCodigo(obj.getCodigo(), obj.getSituacao(), getUsuarioLogado());
				getFacadeFactory().getCartaoCreditoDebitoRecorrenciaPessoaFacade().removerCartaoCreditoDebitoRecorrenciaPessoa(obj, getListaCartaoCreditoDebitoRecorrenciaPessoaVOs());
				consultarRecorrenciaDCC();
				setMensagemID("msg_dados_removidos", Uteis.SUCESSO);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}

	}

	public List<SelectItem> getListaSelectItemTipoTransacao() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("TODAS", "Todas"));
		itens.add(new SelectItem("SUCESSO", "Transacao com Sucesso"));
		itens.add(new SelectItem("INSUCESSO", "Transacao com Insucesso"));
		return itens;
	}

	public void consultarAluno() {
		try {
			List objs = new ArrayList(0);
			if (getValorConsultaMatricula().equals("")) {
				throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
			}
			if (getCampoConsultaMatricula().equals("matricula")) {
				MatriculaVO obj = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimariaSituacaoDadosCompletos(getValorConsultaMatricula(), 0, false, "", getUsuarioLogado());
				if (!obj.getMatricula().equals("")) {
					objs.add(obj);
				}
			}
			if (getCampoConsultaMatricula().equals("nomePessoa")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaMatricula(), 0, false, "", "", getUsuarioLogado());
			}
			setListaConsultaMatriculaVOs(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaMatriculaVOs(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarAlunoPorMatriculaTransacao() throws Exception {
		try {
			MatriculaVO matricula = new MatriculaVO();
			matricula.setMatricula(getMatriculaTransacaoVO().getMatricula());
			getFacadeFactory().getMatriculaFacade().carregarDados(matricula, NivelMontarDados.BASICO, getUsuarioLogado());
			setMatriculaTransacaoVO(matricula);
			if (getMatriculaTransacaoVO().getMatricula().equals("")) {
				throw new Exception("Aluno de matrícula " + getMatriculaTransacaoVO().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
			}
			setPessoaVO(getMatriculaTransacaoVO().getAluno());		
			setControleAba("abaRecorrenciaDCC");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setMatriculaTransacaoVO(new MatriculaVO());
		}
	}

	public void selecionarAlunoTransacao() throws Exception {
		try {
			MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
			setMatriculaTransacaoVO(obj);
			setPessoaVO(obj.getAluno());
			setValorConsultaMatricula("");
			setCampoConsultaMatricula("");
			getListaConsultaMatriculaVOs().clear();
			setControleAba("abaTransacaoDCC");
		} catch (Exception e) {
			setValorConsultaMatricula("");
			setCampoConsultaMatricula("");
			getListaConsultaMatriculaVOs().clear();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparDadosMatriculaTransacao() throws Exception {
		setMatriculaTransacaoVO(null);
		setPessoaVO(null);
		setMensagemID("msg_entre_dados");
	}

	public void consultarAlunoPorMatriculaRecorrencia() throws Exception {
		try {
			getFacadeFactory().getMatriculaFacade().carregarDados(getMatriculaRecorrenciaVO(), NivelMontarDados.BASICO, getUsuarioLogado());
			if (getMatriculaRecorrenciaVO().getMatricula().equals("")) {
				throw new Exception("Aluno de matrícula " + getMatriculaRecorrenciaVO().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
			}
			setPessoaVO(getMatriculaRecorrenciaVO().getAluno());
			setControleAba("abaRecorrenciaDCC");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setMatriculaRecorrenciaVO(new MatriculaVO());
		}
	}

	public void selecionarAlunoRecorrencia() throws Exception {
		try {
			MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
			setMatriculaRecorrenciaVO(obj);
			setPessoaVO(obj.getAluno());
			setValorConsultaMatricula("");
			setCampoConsultaMatricula("");
			getListaConsultaMatriculaVOs().clear();
			setControleAba("abaTransacaoDCC");
		} catch (Exception e) {
			setValorConsultaMatricula("");
			setCampoConsultaMatricula("");
			getListaConsultaMatriculaVOs().clear();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparDadosMatriculaRecorrencia() throws Exception {
		setMatriculaRecorrenciaVO(null);
		setPessoaVO(null);
		setMensagemID("msg_entre_dados");
	}
	

	public List getTipoConsultaComboMatricula() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nomePessoa", "Aluno"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		return itens;
	}

	public String getTipoTransacao() {
		if (tipoTransacao == null) {
			tipoTransacao = "";
		}
		return tipoTransacao;
	}

	public void setTipoTransacao(String tipoTransacao) {
		this.tipoTransacao = tipoTransacao;
	}

	public OperadoraCartaoVO getOperadoraCartaoVO() {
		if (operadoraCartaoVO == null) {
			operadoraCartaoVO = new OperadoraCartaoVO();
		}
		return operadoraCartaoVO;
	}

	public void setOperadoraCartaoVO(OperadoraCartaoVO operadoraCartaoVO) {
		this.operadoraCartaoVO = operadoraCartaoVO;
	}

	public Date getDataInicioTransacao() {
		if (dataInicioTransacao == null) {
			dataInicioTransacao = Uteis.getDataPrimeiroDiaMes(new Date());
		}
		return dataInicioTransacao;
	}

	public void setDataInicioTransacao(Date dataInicioTransacao) {
		this.dataInicioTransacao = dataInicioTransacao;
	}

	public Date getDataFimTransacao() {
		if (dataFimTransacao == null) {
			dataFimTransacao = Uteis.getDataUltimoDiaMes(new Date());
		}
		return dataFimTransacao;
	}

	public void setDataFimTransacao(Date dataFimTransacao) {
		this.dataFimTransacao = dataFimTransacao;
	}

	public List<MatriculaVO> getListaConsultaMatriculaVOs() {
		if (listaConsultaMatriculaVOs == null) {
			listaConsultaMatriculaVOs = new ArrayList<MatriculaVO>(0);
		}
		return listaConsultaMatriculaVOs;
	}

	public void setListaConsultaMatriculaVOs(List<MatriculaVO> listaConsultaMatriculaVOs) {
		this.listaConsultaMatriculaVOs = listaConsultaMatriculaVOs;
	}

	public String getValorConsultaMatricula() {
		if (valorConsultaMatricula == null) {
			valorConsultaMatricula = "";
		}
		return valorConsultaMatricula;
	}

	public void setValorConsultaMatricula(String valorConsultaMatricula) {
		this.valorConsultaMatricula = valorConsultaMatricula;
	}

	public String getCampoConsultaMatricula() {
		if (campoConsultaMatricula == null) {
			campoConsultaMatricula = "";
		}
		return campoConsultaMatricula;
	}

	public void setCampoConsultaMatricula(String campoConsultaMatricula) {
		this.campoConsultaMatricula = campoConsultaMatricula;
	}

	public String getControleAba() {
		if (controleAba == null) {
			controleAba = "abaCartaoCreditoOperadora";
		}
		return controleAba;
	}

	public void setControleAba(String controleAba) {
		this.controleAba = controleAba;
	}
	
	public void selecionarFiltro() throws Exception {
		setMatriculaTransacaoVO(null);
		if (getTipoPessoaTransacao().equals(TipoPessoa.ALUNO)) {
			setAluno(true);
			setFuncionario(false);
			setCandidato(false);
			setParceiro(false);
			setResponsavelFinanceiro(false);
			setFornecedor(false);
		} else if (getTipoPessoaTransacao().equals(TipoPessoa.FUNCIONARIO)) {
			setAluno(false);
			setFuncionario(true);
			setCandidato(false);
			setParceiro(false);
			setResponsavelFinanceiro(false);
			setFornecedor(false);
		} else if (getTipoPessoaTransacao().equals(TipoPessoa.CANDIDATO)) {
			setAluno(false);
			setFuncionario(false);
			setCandidato(true);
			setParceiro(false);
			setResponsavelFinanceiro(false);
			setFornecedor(false);
		} else if (getTipoPessoaTransacao().equals(TipoPessoa.PARCEIRO)) {
			setAluno(false);
			setFuncionario(false);
			setCandidato(false);
			setParceiro(true);
			setResponsavelFinanceiro(false);
			setFornecedor(false);
		} else if (getTipoPessoaTransacao().equals(TipoPessoa.RESPONSAVEL_FINANCEIRO)) {
			setAluno(false);
			setFuncionario(false);
			setCandidato(false);
			setParceiro(false);
			setResponsavelFinanceiro(true);
			setFornecedor(false);
		} else if (getTipoPessoaTransacao().equals(TipoPessoa.FORNECEDOR)) {
			setAluno(false);
			setFuncionario(false);
			setCandidato(false);
			setParceiro(false);
			setResponsavelFinanceiro(false);
			setFornecedor(true);
		}
	}

	public Boolean getAluno() {
		if (aluno == null) {
			aluno = true;
		}
		return aluno;
	}

	public void setAluno(Boolean aluno) {
		this.aluno = aluno;
	}

	public Boolean getFuncionario() {
		if (funcionario == null) {
			funcionario = false;
		}
		return funcionario;
	}

	public void setFuncionario(Boolean funcionario) {
		this.funcionario = funcionario;
	}

	public Boolean getParceiro() {
		if (parceiro == null) {
			parceiro = false;
		}
		return parceiro;
	}

	public void setParceiro(Boolean parceiro) {
		this.parceiro = parceiro;
	}

	public Boolean getCandidato() {
		if (candidato == null) {
			candidato = false;
		}
		return candidato;
	}

	public void setCandidato(Boolean candidato) {
		this.candidato = candidato;
	}

	public Boolean getResponsavelFinanceiro() {
		if (responsavelFinanceiro == null) {
			responsavelFinanceiro = false;
		}
		return responsavelFinanceiro;
	}

	public void setResponsavelFinanceiro(Boolean responsavelFinanceiro) {
		this.responsavelFinanceiro = responsavelFinanceiro;
	}

	public Boolean getFornecedor() {
		if (fornecedor == null) {
			fornecedor = false;
		}
		return fornecedor;
	}

	public void setFornecedor(Boolean fornecedor) {
		this.fornecedor = fornecedor;
	}

	public List<SelectItem> getListaSelectItemFuncionario() {
		if (listaSelectItemFuncionario == null) {
			listaSelectItemFuncionario = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemFuncionario;
	}

	public void setListaSelectItemFuncionario(List<SelectItem> listaSelectItemFuncionario) {
		this.listaSelectItemFuncionario = listaSelectItemFuncionario;
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

	public List<PessoaVO> getListaConsultaCandidato() {
		if (listaConsultaCandidato == null) {
			listaConsultaCandidato = new ArrayList<PessoaVO>(0);
		}
		return listaConsultaCandidato;
	}

	public void setListaConsultaCandidato(List<PessoaVO> listaConsultaCandidato) {
		this.listaConsultaCandidato = listaConsultaCandidato;
	}

	public String getValorConsultaCandidato() {
		if (valorConsultaCandidato == null) {
			valorConsultaCandidato = "";
		}
		return valorConsultaCandidato;
	}

	public void setValorConsultaCandidato(String valorConsultaCandidato) {
		this.valorConsultaCandidato = valorConsultaCandidato;
	}

	public String getCampoConsultaCandidato() {
		if (campoConsultaCandidato == null) {
			campoConsultaCandidato = "";
		}
		return campoConsultaCandidato;
	}

	public void setCampoConsultaCandidato(String campoConsultaCandidato) {
		this.campoConsultaCandidato = campoConsultaCandidato;
	}

	public List<ParceiroVO> getListaConsultaParceiro() {
		if (listaConsultaParceiro == null) {
			listaConsultaParceiro = new ArrayList<ParceiroVO>(0);
		}
		return listaConsultaParceiro;
	}

	public void setListaConsultaParceiro(List<ParceiroVO> listaConsultaParceiro) {
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

	public void consultarFuncionario() {
		List objs = new ArrayList(0);
		try {
			if (getValorConsultaFuncionario().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			getFacadeFactory().getPessoaFacade().setIdEntidade("Funcionario");
			if (getCampoConsultaFuncionario().equals("nome")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNome(getValorConsultaFuncionario(), TipoPessoa.FUNCIONARIO.getValor(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("matricula")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorMatricula(getValorConsultaFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("CPF")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCPF(getValorConsultaFuncionario(), TipoPessoa.FUNCIONARIO.getValor(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("unidadeEnsino")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorUnidadeEnsino(getValorConsultaFuncionario(), TipoPessoa.FUNCIONARIO.getValor(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			setListaConsultaFuncionario(objs);
			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			getListaConsultaFuncionario().clear();
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			objs = null;
		}
	}

	public void selecionarFuncionario() throws Exception {
		FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItens");
		try {
			setPessoaVO(obj.getPessoa());
			setCampoDescritivo(obj.getMatricula());
			setValorDescritivo(obj.getPessoa().getNome());
			setCampoConsultaFuncionario("");
			setValorConsultaFuncionario("");
			getListaConsultaFuncionario().clear();
		} finally {
			obj = null;
		}
	}

	public void consultarCandidato() {
		List objs = new ArrayList(0);
		try {
			getFacadeFactory().getPessoaFacade().setIdEntidade("Candidato");
			if (getValorConsultaCandidato().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			if (getCampoConsultaCandidato().equals("codigo")) {
				int valorInt = Integer.parseInt(getValorConsultaCandidato());
				objs = getFacadeFactory().getPessoaFacade().consultarPorCodigo(new Integer(valorInt), "CA", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaCandidato().equals("nome")) {
				objs = getFacadeFactory().getPessoaFacade().consultaRapidaPorNome(getValorConsultaCandidato(), "CA", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaCandidato().equals("CPF")) {
				objs = getFacadeFactory().getPessoaFacade().consultaRapidaPorCPF(getValorConsultaCandidato(), "CA", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaCandidato().equals("RG")) {
				objs = getFacadeFactory().getPessoaFacade().consultaRapidaPorRG(getValorConsultaCandidato(), "CA", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaCandidato(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			getListaConsultaCandidato().clear();
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			objs = null;
		}
	}

	public void selecionarCandidato() throws Exception {
		PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("candidatoItens");
		try {
			setPessoaVO(obj);
			setCampoDescritivo(obj.getCPF());
			setValorDescritivo(obj.getNome());
			setCampoConsultaCandidato("");
			setValorConsultaCandidato("");
			getListaConsultaCandidato().clear();
		} finally {
			obj = null;
		}
	}

	public void consultarParceiro() {
		List objs = new ArrayList(0);
		try {
			super.consultar();
			if (getCampoConsultaParceiro().equals("codigo")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getParceiroFacade().consultarPorCodigo(valorInt, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaParceiro().equals("nome")) {
				objs = getFacadeFactory().getParceiroFacade().consultarPorNome(getValorConsultaParceiro(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaParceiro().equals("razaoSocial")) {
				objs = getFacadeFactory().getParceiroFacade().consultarPorRazaoSocial(getValorConsultaParceiro(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaParceiro().equals("RG")) {
				objs = getFacadeFactory().getParceiroFacade().consultarPorRG(getValorConsultaParceiro(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaParceiro().equals("CPF")) {
				objs = getFacadeFactory().getParceiroFacade().consultarPorCPF(getValorConsultaParceiro(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaParceiro().equals("tipoParceiro")) {
				objs = getFacadeFactory().getParceiroFacade().consultarPorTipoParceiro(getValorConsultaParceiro(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaParceiro(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			getListaConsulta().clear();
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			objs = null;
		}
	}

	public void selecionarParceiro() throws Exception {
		ParceiroVO obj = (ParceiroVO) context().getExternalContext().getRequestMap().get("parceiroItens");
		try {
			setParceiroVO(obj);
			if (obj.getCPF().equals("")) {
				setCampoDescritivo(obj.getCNPJ());
			} else {
				setCampoDescritivo(obj.getCPF());
			}
			setValorDescritivo(obj.getNome());
			setValorConsultaCandidato("");
			setCampoConsultaCandidato("");
			getListaConsultaCandidato().clear();

		} finally {
			obj = null;
		}
	}

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

	public void selecionarResponsavelFinanceiro() {
		try {
			PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("responsavelFinanceiroItens");
			setPessoaVO(obj);
			setCampoDescritivo(obj.getCPF());
			setValorDescritivo(obj.getNome());
			setResponsavelFinanceiroVO(obj);
			getListaConsultaResponsavelFinanceiro().clear();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparDadosResponsavelFinanceiro() {
		setResponsavelFinanceiroVO(null);
	}

	public void consultarFornecedor() {
		try {
			super.consultar();
			List objs = new ArrayList(0);
			if (getCampoConsultaFornecedor().equals("codigo")) {
				if (getValorConsultaFornecedor().equals("")) {
					setValorConsultaFornecedor("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaFornecedor());
				objs = getFacadeFactory().getFornecedorFacade().consultarPorCodigo(new Integer(valorInt), "AT", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFornecedor().equals("nome")) {
				objs = getFacadeFactory().getFornecedorFacade().consultarPorNome(getValorConsultaFornecedor(), "AT", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFornecedor().equals("razaoSocial")) {
				objs = getFacadeFactory().getFornecedorFacade().consultarPorRazaoSocial(getValorConsultaFornecedor(), "AT", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFornecedor().equals("RG")) {
				objs = getFacadeFactory().getFornecedorFacade().consultarPorRG(getValorConsultaFornecedor(), "AT", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFornecedor().equals("CPF")) {
				objs = getFacadeFactory().getFornecedorFacade().consultarPorCPF(getValorConsultaFornecedor(), "AT", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFornecedor().equals("CNPJ")) {
				objs = getFacadeFactory().getFornecedorFacade().consultarPorCNPJ(getValorConsultaFornecedor(), "AT", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}

			setListaConsultaFornecedor(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaFornecedor(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarFornecedor() {
		FornecedorVO obj = (FornecedorVO) context().getExternalContext().getRequestMap().get("fornecedorItens");
		setFornecedorVO(obj);
		this.setCampoDescritivo(obj.getCPF() + obj.getCNPJ());
		this.setValorDescritivo(obj.getNome());
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

	public ParceiroVO getParceiroVO() {
		if (parceiroVO == null) {
			parceiroVO = new ParceiroVO();
		}
		return parceiroVO;
	}

	public void setParceiroVO(ParceiroVO parceiroVO) {
		this.parceiroVO = parceiroVO;
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

	public List getTipoConsultaComboFuncionario() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("CPF", "CPF"));
		return itens;
	}

	public List getTipoConsultaComboCandidato() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		itens.add(new SelectItem("CPF", "CPF"));
		itens.add(new SelectItem("RG", "RG"));
		return itens;
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List getTipoConsultaComboParceiro() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("razaoSocial", "Razão Social"));
		itens.add(new SelectItem("RG", "RG"));
		itens.add(new SelectItem("CPF", "CPF"));
		itens.add(new SelectItem("tipoParceiro", "Tipo Parceiro"));
		return itens;
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

	public FornecedorVO getFornecedorVO() {
		if (fornecedorVO == null) {
			fornecedorVO = new FornecedorVO();
		}
		return fornecedorVO;
	}

	public void setFornecedorVO(FornecedorVO fornecedorVO) {
		this.fornecedorVO = fornecedorVO;
	}

	private List<SelectItem> tipoConsultaComboFornecedor;

	public List<SelectItem> getTipoConsultaComboFornecedor() {
		if (tipoConsultaComboFornecedor == null) {
			tipoConsultaComboFornecedor = new ArrayList<SelectItem>(0);
			tipoConsultaComboFornecedor.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboFornecedor.add(new SelectItem("razaoSocial", "Razão Social"));
			tipoConsultaComboFornecedor.add(new SelectItem("CNPJ", "CNPJ"));
			tipoConsultaComboFornecedor.add(new SelectItem("CPF", "CPF"));
			tipoConsultaComboFornecedor.add(new SelectItem("RG", "RG"));
			tipoConsultaComboFornecedor.add(new SelectItem("codigo", "codigo"));
		}
		return tipoConsultaComboFornecedor;
	}

	public List<FornecedorVO> getListaConsultaFornecedor() {
		if (listaConsultaFornecedor == null) {
			listaConsultaFornecedor = new ArrayList<FornecedorVO>(0);
		}
		return listaConsultaFornecedor;
	}

	public void setListaConsultaFornecedor(List<FornecedorVO> listaConsultaFornecedor) {
		this.listaConsultaFornecedor = listaConsultaFornecedor;
	}

	public String getValorConsultaFornecedor() {
		if (valorConsultaFornecedor == null) {
			valorConsultaFornecedor = "";
		}
		return valorConsultaFornecedor;
	}

	public void setValorConsultaFornecedor(String valorConsultaFornecedor) {
		this.valorConsultaFornecedor = valorConsultaFornecedor;
	}

	public String getCampoConsultaFornecedor() {
		if (campoConsultaFornecedor == null) {
			campoConsultaFornecedor = "";
		}
		return campoConsultaFornecedor;
	}

	public void setCampoConsultaFornecedor(String campoConsultaFornecedor) {
		this.campoConsultaFornecedor = campoConsultaFornecedor;
	}

	public void setTipoConsultaComboResponsavelFinanceiro(List<SelectItem> tipoConsultaComboResponsavelFinanceiro) {
		this.tipoConsultaComboResponsavelFinanceiro = tipoConsultaComboResponsavelFinanceiro;
	}

	public String getCampoDescritivo() {
		if (campoDescritivo == null) {
			campoDescritivo = "";
		}
		return campoDescritivo;
	}

	public void setCampoDescritivo(String campoDescritivo) {
		this.campoDescritivo = campoDescritivo;
	}

	public String getValorDescritivo() {
		if (valorDescritivo == null) {
			valorDescritivo = "";
		}
		return valorDescritivo;
	}

	public void setValorDescritivo(String valorDescritivo) {
		this.valorDescritivo = valorDescritivo;
	}

	public void limparDados() {
		setCampoDescritivo(null);
		setValorDescritivo(null);
		setParceiroVO(null);
		setFornecedorVO(null);
		setPessoaVO(null);
	}

	public PessoaVO getPessoaVO() {
		if (PessoaVO == null) {
			PessoaVO = new PessoaVO();
		}
		return PessoaVO;
	}

	public void setPessoaVO(PessoaVO pessoaVO) {
		PessoaVO = pessoaVO;
	}

	public List<SelectItem> getListaSelectItemTipoPessoa() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem(TipoPessoa.ALUNO, "Aluno"));
		itens.add(new SelectItem(TipoPessoa.RESPONSAVEL_FINANCEIRO, "Responsável Financeiro"));
		itens.add(new SelectItem(TipoPessoa.FUNCIONARIO, "Funcionário"));
		itens.add(new SelectItem(TipoPessoa.CANDIDATO, "Candidato"));
		itens.add(new SelectItem(TipoPessoa.PARCEIRO, "Parceiro"));
		itens.add(new SelectItem(TipoPessoa.FORNECEDOR, "Fornecedor"));
		return itens;
	}

	public String getMascaraConsultaFornecedor() {
		if (getCampoConsultaFornecedor().equals("CPF")) {
			return "return mascara(this.form, 'formFornecedor:valorConsultaFornecedor', '99.999.999/9999-99', event);";
		} else if (getCampoConsultaFornecedor().equals("CNPJ")) {
			return "return mascara(this.form, 'formFornecedor:valorConsultaFornecedor', '999.999.999-99', event);";
		} else if (getCampoConsultaFornecedor().equals("codigo")) {
			return "return mascara(this.form, 'formFornecedor:valorConsultaFornecedor', '99999999999999', event);";
		}
		return "";
	}

	public String getDescricao() {
		if (getTipoPessoaTransacao().equals(TipoPessoa.ALUNO)) {
			return "Aluno";
		} else if (getTipoPessoaTransacao().equals(TipoPessoa.FUNCIONARIO)) {
			return "Funcionário";
		} else if (getTipoPessoaTransacao().equals(TipoPessoa.CANDIDATO)) {
			return "Candidato";
		} else if (getTipoPessoaTransacao().equals(TipoPessoa.PARCEIRO)) {
			return "Parceiro";
		} else if (getTipoPessoaTransacao().equals(TipoPessoa.RESPONSAVEL_FINANCEIRO)) {
			return "Responsável Financeiro";
		} else if (getTipoPessoaTransacao().equals(TipoPessoa.FORNECEDOR)) {
			return "Fornecedor";
		}
		return "";
	}

	public List<SelectItem> listaSelectItemMesValidade;
	public List<SelectItem> listaSelectItemAnoValidade;

	public List<SelectItem> getListaSelectItemMesValidade() {
		if (listaSelectItemMesValidade == null) {
			listaSelectItemMesValidade = new ArrayList<SelectItem>();
			listaSelectItemMesValidade.add(new SelectItem(0, ""));
			for (Integer i = 1; i < 13; i++) {
				listaSelectItemMesValidade.add(new SelectItem(i.toString(), i.toString()));
			}
		}
		return listaSelectItemMesValidade;
	}

	public void setListaSelectItemMesValidade(List<SelectItem> listaSelectItemMesValidade) {
		this.listaSelectItemMesValidade = listaSelectItemMesValidade;
	}

	public List<SelectItem> getComboBoxConsultaComboFormaPadraoDataBaseCartaoRecorrente() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem(FormaPadraoDataBaseCartaoRecorrenteEnum.TODOS, FormaPadraoDataBaseCartaoRecorrenteEnum.TODOS.getValorApresentar()));
		itens.add(new SelectItem(FormaPadraoDataBaseCartaoRecorrenteEnum.DIA_FIXO, FormaPadraoDataBaseCartaoRecorrenteEnum.DIA_FIXO.getValorApresentar()));
		itens.add(new SelectItem(FormaPadraoDataBaseCartaoRecorrenteEnum.VENCIMENTO_PRIMEIRA_FAIXA_DESCONTO, FormaPadraoDataBaseCartaoRecorrenteEnum.VENCIMENTO_PRIMEIRA_FAIXA_DESCONTO.getValorApresentar()));
		return itens;
	}

	public String getNomeCartao() {
		if (nomeCartao == null) {
			nomeCartao = "";
		}
		return nomeCartao;
	}

	public void setNomeCartao(String nomeCartao) {
		this.nomeCartao = nomeCartao;
	}

	public FormaPadraoDataBaseCartaoRecorrenteEnum getFormaPadraoDataBaseCartaoRecorrente() {
		if (formaPadraoDataBaseCartaoRecorrente == null) {
			formaPadraoDataBaseCartaoRecorrente = FormaPadraoDataBaseCartaoRecorrenteEnum.TODOS;
		}
		return formaPadraoDataBaseCartaoRecorrente;
	}

	public void setFormaPadraoDataBaseCartaoRecorrente(FormaPadraoDataBaseCartaoRecorrenteEnum formaPadraoDataBaseCartaoRecorrente) {
		this.formaPadraoDataBaseCartaoRecorrente = formaPadraoDataBaseCartaoRecorrente;
	}

	public PessoaVO getResponsavelFinanceiroVO() {
		if (responsavelFinanceiroVO == null) {
			responsavelFinanceiroVO = new PessoaVO();
		}
		return responsavelFinanceiroVO;
	}

	public void setResponsavelFinanceiroVO(PessoaVO responsavelFinanceiroVO) {
		this.responsavelFinanceiroVO = responsavelFinanceiroVO;
	}

	public Integer getMesValidade() {
		if (mesValidade == null) {
			mesValidade = 0;
		}
		return mesValidade;
	}

	public void setMesValidade(Integer mesValidade) {
		this.mesValidade = mesValidade;
	}

	public String getAnoValidade() {
		if (anoValidade == null) {
			anoValidade = "";
		}
		return anoValidade;
	}

	public void setAnoValidade(String anoValidade) {
		this.anoValidade = anoValidade;
	}

	public List<CartaoCreditoDebitoRecorrenciaPessoaVO> getListaCartaoCreditoDebitoRecorrenciaPessoaVOs() {
		if (listaCartaoCreditoDebitoRecorrenciaPessoaVOs == null) {
			listaCartaoCreditoDebitoRecorrenciaPessoaVOs = new ArrayList<CartaoCreditoDebitoRecorrenciaPessoaVO>(0);
		}
		return listaCartaoCreditoDebitoRecorrenciaPessoaVOs;
	}

	public void setListaCartaoCreditoDebitoRecorrenciaPessoaVOs(List<CartaoCreditoDebitoRecorrenciaPessoaVO> listaCartaoCreditoDebitoRecorrenciaPessoaVOs) {
		this.listaCartaoCreditoDebitoRecorrenciaPessoaVOs = listaCartaoCreditoDebitoRecorrenciaPessoaVOs;
	}

	public List<SelectItem> getListaSelectItemTipoPessoaRecorrencia() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem(TipoPessoa.ALUNO, "Aluno"));
		itens.add(new SelectItem(TipoPessoa.RESPONSAVEL_FINANCEIRO, "Responsável Financeiro"));
		return itens;
	}

	public void inicializarDadosAbaTransacaoDCC() {
		setMatriculaTransacaoVO(null);
		limparDados();
	}

	public void inicializarDadosAbaRecorrenciaDCC() {
		setMatriculaRecorrenciaVO(null);
		setTipoPessoaRecorrencia(TipoPessoa.ALUNO);
		setResponsavelFinanceiroVO(null);
		setFormaPadraoDataBaseCartaoRecorrente(FormaPadraoDataBaseCartaoRecorrenteEnum.TODOS);
		setMesValidade(0);
		setAnoValidade("");
		consultarDataUltimaExecucaoJobBaixaCartaoCreditoRecorrenciaDCC();
	}

	public Integer getDiaPadraoPagamento() {
		if (diaPadraoPagamento == null) {
			diaPadraoPagamento = 0;
		}
		return diaPadraoPagamento;
	}

	public void setDiaPadraoPagamento(Integer diaPadraoPagamento) {
		this.diaPadraoPagamento = diaPadraoPagamento;
	}

	public TipoPessoa getTipoPessoaTransacao() {
		if (tipoPessoaTransacao == null) {
			tipoPessoaTransacao = TipoPessoa.ALUNO;
		}
		return tipoPessoaTransacao;
	}

	public void setTipoPessoaTransacao(TipoPessoa tipoPessoaTransacao) {
		this.tipoPessoaTransacao = tipoPessoaTransacao;
	}

	public TipoPessoa getTipoPessoaRecorrencia() {
		if (tipoPessoaRecorrencia == null) {
			tipoPessoaRecorrencia = TipoPessoa.ALUNO;
		}
		return tipoPessoaRecorrencia;
	}

	public void setTipoPessoaRecorrencia(TipoPessoa tipoPessoaRecorrencia) {
		this.tipoPessoaRecorrencia = tipoPessoaRecorrencia;
	}
	
	public void verificarPermissaoUsuarioExcluirCartaoRecorrencia() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoFinanceiroEnum.PERMITIR_EXCLUIR_CARTAO_RECORRENCIA, getUsuarioLogado());
			permitirExcluirCartaoRecorrencia = true;
		} catch (Exception e) {
			permitirExcluirCartaoRecorrencia = false;
		}
	}
	
	public void verificarPermissaoUsuarioExecutarJobCartaoRecorrencia() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoFinanceiroEnum.PERMITIR_EXECUTAR_JOB_CARTAO_RECORRENCIA, getUsuarioLogado());
			permitirExecutarJobCartaoRecorrencia = true;
		} catch (Exception e) {
			permitirExecutarJobCartaoRecorrencia = false;
		}
	}
	
	public void consultarDataUltimaExecucaoJobBaixaCartaoCreditoRecorrenciaDCC() {
		Date dataUltimaExecucaoJob = getFacadeFactory().getRegistroExecucaoJobFacade().consultarDataUltimaExecucaoJobBaixaCartaoCreditoRecorrenciaDCC();
		if (Uteis.isAtributoPreenchido(dataUltimaExecucaoJob)) {
			setDataUltimaExecucaoJob(UteisData.getDataComHora(dataUltimaExecucaoJob));
		}
	}
	
	public void executarJobBaixaCartaoCreditoRecorrenciaDCC() {
		JobBaixaCartaoCreditoDCC job = new JobBaixaCartaoCreditoDCC();
		job.setJobExecutadaManualmente(true);
		job.run();
		setListaLogExecucaoJobCartaoCreditoDebitoRecorrenciaVOs(job.getListaLogExecucaoJobCartaoCreditoDebitoRecorrenciaVOs());
		consultarDataUltimaExecucaoJobBaixaCartaoCreditoRecorrenciaDCC();
	}

	public boolean isPermitirExcluirCartaoRecorrencia() {
		return permitirExcluirCartaoRecorrencia;
	}

	public void setPermitirExcluirCartaoRecorrencia(boolean permitirExcluirCartaoRecorrencia) {
		this.permitirExcluirCartaoRecorrencia = permitirExcluirCartaoRecorrencia;
	}

	public String getDataUltimaExecucaoJob() {
		if (dataUltimaExecucaoJob == null) {
			dataUltimaExecucaoJob = "";
		}
		return dataUltimaExecucaoJob;
	}

	public void setDataUltimaExecucaoJob(String dataUltimaExecucaoJob) {
		this.dataUltimaExecucaoJob = dataUltimaExecucaoJob;
	}

	public boolean isPermitirExecutarJobCartaoRecorrencia() {
		return permitirExecutarJobCartaoRecorrencia;
	}

	public void setPermitirExecutarJobCartaoRecorrencia(boolean permitirExecutarJobCartaoRecorrencia) {
		this.permitirExecutarJobCartaoRecorrencia = permitirExecutarJobCartaoRecorrencia;
	}

	public MatriculaVO getMatriculaTransacaoVO() {
		if (matriculaTransacaoVO == null) {
			matriculaTransacaoVO = new MatriculaVO();
		}
		return matriculaTransacaoVO;
	}

	public void setMatriculaTransacaoVO(MatriculaVO matriculaTransacaoVO) {
		this.matriculaTransacaoVO = matriculaTransacaoVO;
	}

	public MatriculaVO getMatriculaRecorrenciaVO() {
		if (matriculaRecorrenciaVO == null) {
			matriculaRecorrenciaVO = new MatriculaVO();
		}
		return matriculaRecorrenciaVO;
	}

	public void setMatriculaRecorrenciaVO(MatriculaVO matriculaRecorrenciaVO) {
		this.matriculaRecorrenciaVO = matriculaRecorrenciaVO;
	}

	public String getNumeroFinalCartaoCredito() {
		if (numeroFinalCartaoCredito == null) {
			numeroFinalCartaoCredito = "";
		}
		return numeroFinalCartaoCredito;
	}

	public void setNumeroFinalCartaoCredito(String numeroFinalCartaoCredito) {
		this.numeroFinalCartaoCredito = numeroFinalCartaoCredito;
	}

	public String getChaveTransacaoCartaoCredito() {
		if (chaveTransacaoCartaoCredito == null) {
			chaveTransacaoCartaoCredito = "";
		}
		return chaveTransacaoCartaoCredito;
	}

	public void setChaveTransacaoCartaoCredito(String chaveTransacaoCartaoCredito) {
		this.chaveTransacaoCartaoCredito = chaveTransacaoCartaoCredito;
	}

	public List<LogExecucaoJobCartaoCreditoDebitoRecorrenciaVO> getListaLogExecucaoJobCartaoCreditoDebitoRecorrenciaVOs() {
		if (listaLogExecucaoJobCartaoCreditoDebitoRecorrenciaVOs == null) {
			listaLogExecucaoJobCartaoCreditoDebitoRecorrenciaVOs = new ArrayList<LogExecucaoJobCartaoCreditoDebitoRecorrenciaVO>(0);
		}
		return listaLogExecucaoJobCartaoCreditoDebitoRecorrenciaVOs;
	}

	public void setListaLogExecucaoJobCartaoCreditoDebitoRecorrenciaVOs(List<LogExecucaoJobCartaoCreditoDebitoRecorrenciaVO> listaLogExecucaoJobCartaoCreditoDebitoRecorrenciaVOs) {
		this.listaLogExecucaoJobCartaoCreditoDebitoRecorrenciaVOs = listaLogExecucaoJobCartaoCreditoDebitoRecorrenciaVOs;
	}
	
	public Integer getQuantidadeLogExecucaoJob() {
		return getListaLogExecucaoJobCartaoCreditoDebitoRecorrenciaVOs().size();
	}
}
