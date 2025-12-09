package controle.financeiro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.LayoutPadraoVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoFinanceiroEnum;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.biblioteca.ImpressoraVO;
import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.contabil.FechamentoMesHistoricoModificacaoVO;
import negocio.comuns.contabil.LancamentoContabilVO;
import negocio.comuns.contabil.enumeradores.TipoOrigemHistoricoBloqueioEnum;
import negocio.comuns.financeiro.ChequeVO;
import negocio.comuns.financeiro.ConciliacaoContaCorrenteVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroCartaoVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ConfiguracaoRecebimentoCartaoOnlineVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ContaReceberNegociacaoRecebimentoVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO;
import negocio.comuns.financeiro.FormaPagamentoNegociacaoRecebimentoVO;
import negocio.comuns.financeiro.NegociacaoRecebimentoVO;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.financeiro.enumerador.PermitirCartaoEnum;
import negocio.comuns.financeiro.enumerador.TipoFinanciamentoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.SituacaoContaReceber;
import negocio.comuns.utilitarias.dominios.TipoDescontoAluno;
import negocio.comuns.utilitarias.dominios.TipoFormaPagamento;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import negocio.comuns.utilitarias.dominios.TipoOrigemContaReceber;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.financeiro.NegociacaoRecebimento;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.controle.financeiro.ComprovanteRecebimentoRelControle;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas negociacaoRecebimentoForm.jsp negociacaoRecebimentoCons.jsp) com as funcionalidades da classe <code>NegociacaoRecebimento</code>. Implemtação da camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see NegociacaoRecebimento
 * @see NegociacaoRecebimentoVO
 */
@Lazy
@Scope("viewScope")
@SuppressWarnings("unchecked")
@Controller("NegociacaoRecebimentoControle")
public class NegociacaoRecebimentoControle extends SuperControle implements Serializable {

	public static final long serialVersionUID = 1L;
	private NegociacaoRecebimentoVO negociacaoRecebimentoVO;
	private ChequeVO chequeVO;
	private ContaReceberVO contaReceberVO;
	private Date dataInicioConsultar;
	protected List<SelectItem> listaSelectItemCaixa;
	protected List<SelectItem> listaSelectItemContaCorrente;
	protected List<SelectItem> listaSelectItemFormaPagamento;
	protected List<SelectItem> listaSelectItemFormaPagamentoIsencaoPermuta;
	protected List<SelectItem> listaSelectItemConfiguracaoFinanceiroCartao;
	protected List<SelectItem> listaSelectItemUnidadeEnsino;
	private Boolean botaoExcluir;
	private String tipoContasAReceber;
	protected List listaConsultaFuncionario;
	protected String valorConsultaFuncionario;
	protected String campoConsultaFuncionario;
	protected List listaConsultaAluno;
	protected String valorConsultaAluno;
	protected String campoConsultaAluno;
	protected List listaConsultaCandidato;
	protected String valorConsultaCandidato;
	protected String campoConsultaCandidato;
	protected List listaConsultaParceiro;
	protected String valorConsultaParceiro;
	protected String campoConsultaParceiro;
	private int valorConsultaUnidadeEnsino;
	protected List listaConsultaRequisitante;
	protected String valorConsultaRequisitante;
	protected String campoConsultaRequisitante;
	protected String valorConsultaContaReceber;
	protected String campoConsultaContaReceber;
	protected List listaConsultaContaReceber;
	private ContaReceberNegociacaoRecebimentoVO contaReceberNegociacaoRecebimentoVO;
	private FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO;
	private ConfiguracaoFinanceiroCartaoVO configuracaoFinanceiroCartaoVO;
	private Boolean mostrarModalJustificativaDesconto;
	private Boolean mostrarModalExplicacaoMovimentacaoCheque;
	private Boolean autorizacaoRecebimentoRetroativo;
	protected ComprovanteRecebimentoRelControle comprovanteRecebimentoRelControle;
	protected List<PessoaVO> listaConsultaResponsavelFinanceiro;
	protected String valorConsultaResponsavelFinanceiro;
	protected String campoConsultaResponsavelFinanceiro;
	private List listaContaCorrenteVOs;
	private List listaContaCorrenteResponsavelEstornoVOs;
	private ContaCorrenteVO contaCaixaVO;
	private Boolean alterarContaCaixa;
	private Boolean permitirAlterarContaCaixaEstorno;
	protected List<FornecedorVO> listaConsultaFornecedor;
	protected String valorConsultaFornecedor;
	protected String campoConsultaFornecedor;
	private Boolean funcionarioResponsavelCaixa;
	private String tipoOrigem;
	private String textoComprovante;
	private Boolean receberContaReceberTerceiro;
	private String tipoPessoaTerceiro;
	private PessoaVO pessoaTerceiroVO;
	private FornecedorVO fornecedorTerceiroVO;
	private ParceiroVO parceiroTerceiroVO;
	private MatriculaVO matriculaTerceiroVO;
	private FuncionarioVO funcionarioTerceiroVO;

	private Boolean permitirRecebimentoTerceiro;
	private FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO formaPagamentoNegociacaoRecebimentoCartaoCreditoVO;

	private Integer quantidadeContaReceber;
	private String mensagemAvisoAdicionarApenasCinquentaContaReceberParceiro;
	private Boolean bloqueiaDescontoNoRecebimento;
	private UsuarioVO usuarioDesbloquearDescontoRecebimento;
	private UsuarioVO usuarioDesbloquearFormaRecebimentoNoRecebimento;

	private List<ChequeVO> listaChequesDevolvidoParaReutilizar;
	private Double valorDescontoLancadoRecebimento;
	private String tipoDescontoLancadoRecebimento;
	// Atribuitos Criados Para Alterar Todas Contas a Receber
	private String justifivativaDescontoListaContaReceber;
	private String tipoDescontoLancadoRecebimentoListaContaReceber;
	private Double valorDescontoLancadoRecebimentoListaContaReceber;
	private Boolean fecharModalAdicionarDescontoJustificativaTodasContasReceber;
	private Boolean fecharModalDesbloquearDescontoJustificativaTodasContasReceber;
	private ConciliacaoContaCorrenteVO conciliacaoContaCorrenteVO;
	private Boolean permitePagarReceberContasEmCaixaUnidadeDiferenteDaConta;
	private LancamentoContabilVO lancamentoContabil;
	private Boolean permiteEstornarRecebimentoCartaoCreditoJaRecebido;
	private boolean apresentarValorIndiceReajustePorAtraso = false;
	private List<SelectItem> listaSelectItemTipoFinanciamento;
	private boolean campoNumeroReciboObrigatorio = false;

	public NegociacaoRecebimentoControle() throws Exception {
		// obterUsuarioLogado();
		this.setDataInicioConsultar(new Date());
		setBotaoExcluir(false);
		// setControleConsulta(new ControleConsulta());
		inicializarResponsavel();
		getControleConsulta().setCampoConsulta("pessoa");
		setMensagemID("msg_entre_prmconsulta");
		montarListaSelectItemUnidadeEnsino();
		getControleConsulta().setDataIni(Uteis.getDataPrimeiroDiaMes(new Date()));
		getControleConsulta().setDataFim(Uteis.getDataUltimoDiaMes(new Date()));
		setValorConsultaUnidadeEnsino(getUnidadeEnsinoLogado().getCodigo());
		setValorDescontoLancadoRecebimento(null);
		setTipoDescontoLancadoRecebimento(null);
		verificarPermissaoUsuarioPagarReceberContasEmCaixaUnidadeDiferenteDaConta();
		verificarPermissaoBloquearDescontoNoRecebimento();
		verificarOBrigatoriedadeCampoNumeroRecibo();
	}

	@PostConstruct
	public void postConstructUnificado() {
		realizarCarregamentoRecebimentoVindoTelaConciliacaoBancaria();
		recuperarDadosVindoContaReceberForm();
		recuperarDadosVindoRenovarMatricula();
		realizarCarregamentoRecebimentoVindoTelaFichaAluno();
		realizarCarregamentoRecebimentoTodasContasAReceberVindoTelaFichaAluno();
		realizarCarregamentoRecebimentoVindoTelaExtratoContaCorrente();
		realizarCarregamentoNegociacaoVindoTelaControleCobranca();
	}
	
	public void realizarCarregamentoNegociacaoVindoTelaControleCobranca() {
		String nossoNumero = (String) context().getExternalContext().getSessionMap().get("contaReceberNegociacao");
		
		if (nossoNumero != null && !nossoNumero.trim().isEmpty()) {
			try {
				List<NegociacaoRecebimentoVO> lista = getFacadeFactory().getNegociacaoRecebimentoFacade().consultaRapidaPorNossoNumero(nossoNumero, 0, "", false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
				if (!lista.isEmpty()) {
					NegociacaoRecebimentoVO obj = (NegociacaoRecebimentoVO)lista.get(0);
					setNegociacaoRecebimentoVO(getFacadeFactory().getNegociacaoRecebimentoFacade().carregarDados(obj, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getNegociacaoRecebimentoVO().getUnidadeEnsino().getCodigo()), getUsuarioLogado()));
					carregarDadosParaEditarNegociacaoRecebimento(getNegociacaoRecebimentoVO());					
				}
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			} finally {
				context().getExternalContext().getSessionMap().remove("contaReceberNegociacao");
			}
		}
	}
	
	public void realizarCarregamentoRecebimentoVindoTelaConciliacaoBancaria() {
		Boolean realizarEstornoConciliacaoBancaria = (Boolean) context().getExternalContext().getSessionMap()
				.get("realizarEstornoConciliacaoBancaria");
		try {
			if (realizarEstornoConciliacaoBancaria != null && realizarEstornoConciliacaoBancaria) {
				NegociacaoRecebimentoVO obj = (NegociacaoRecebimentoVO) context().getExternalContext().getSessionMap()
						.get("negociacaoConciliacaoBancaria");
				if (Uteis.isAtributoPreenchido(obj)) {
					setNegociacaoRecebimentoVO(obj);
					carregarDadosParaEditarNegociacaoRecebimento(obj);
				}
				setConciliacaoContaCorrenteVO((ConciliacaoContaCorrenteVO) context().getExternalContext()
						.getSessionMap().get("conciliacaoBancaria"));
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
	
	   private boolean verificarOBrigatoriedadeCampoNumeroRecibo() {
	    	try {
	    		ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoFinanceiroEnum.CAMPO_NUMERO_RECIBO_OBRIGATORIO, getUsuarioLogado());
	    		setCampoNumeroReciboObrigatorio(true);
			} catch (Exception e) {
				setCampoNumeroReciboObrigatorio(false);
			}
	    	return isCampoNumeroReciboObrigatorio();
	   }

	/***
	 * Método usado apra recupar os objetos vindo da tela de ContaReceberForm.xhtml
	 * 
	 * @author Otimize - 18 de ago de 2015
	 */
	private void recuperarDadosVindoContaReceberForm() {
		try {
			ContaReceberVO contaReceberVO = (ContaReceberVO)context().getExternalContext().getSessionMap().get("contaReceberItem");
			if (contaReceberVO != null) {
				novo();				
				setNegociacaoRecebimentoVO(contaReceberVO.gerarNegociacaoRecebimentoVOPreenchido(getUsuarioLogado(),  contaReceberVO.getPessoa(), contaReceberVO.getResponsavelFinanceiro(), contaReceberVO.getFuncionario(), contaReceberVO.getParceiroVO(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(contaReceberVO.getUnidadeEnsinoFinanceira().getCodigo())));
				selecionarContaReceber(contaReceberVO);				
				montarListaSelectItemFormaPagamento(contaReceberVO);
				montarListaSelectItemCaixa();
				montarListaSelectItemContaCorrente();
				montarUltimaMatriculaPeriodoAluno();
				verificarPermissaoUsuarioRealizarRecebimentoRetroativo();
				montarListaSelectItemImpressora();						
				if(!isApresentarValorIndiceReajustePorAtraso()){
					setApresentarValorIndiceReajustePorAtraso(getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getNegociacaoRecebimentoVO().getUnidadeEnsino().getCodigo()).isAplicarIndireReajustePorAtrasoContaReceber());	
				}				
				context().getExternalContext().getSessionMap().remove("contaReceberItem");			
			}	
		} catch (Exception e) {
			e.getMessage();
		}
	}

	private void recuperarDadosVindoRenovarMatricula() {
		NegociacaoRecebimentoControle negociacaoRecebimentoControle = (NegociacaoRecebimentoControle) context()
				.getExternalContext().getSessionMap().get("NegociacaoRecebimentoControle");
		context().getExternalContext().getSessionMap().remove("NegociacaoRecebimentoControle");
		if (negociacaoRecebimentoControle != null) {
			this.setNegociacaoRecebimentoVO(negociacaoRecebimentoControle.getNegociacaoRecebimentoVO());
			montarUltimaMatriculaPeriodoAluno();
			montarListaSelectItemImpressora();
		}
	}

	public void realizarCarregamentoRecebimentoVindoTelaFichaAluno() {
		ContaReceberVO obj = (ContaReceberVO) context().getExternalContext().getSessionMap().get("contaReceberFichaAluno");
		if (obj != null && !obj.getCodigo().equals(0)) {
			try {
				receberConta(obj);
				setMatriculaPeriodoVO(
						getFacadeFactory().getMatriculaPeriodoFacade().consultarUltimaMatriculaPeriodoAtivaPorMatricula(
								getNegociacaoRecebimentoVO().getMatricula(), false, Uteis.NIVELMONTARDADOS_TODOS,
								getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getNegociacaoRecebimentoVO().getUnidadeEnsino().getCodigo()), getUsuarioLogado()));
				setMatriculaPeriodoVO(getFacadeFactory().getMatriculaPeriodoFacade().consultarPorChavePrimaria(
						getMatriculaPeriodoVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS,
						getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getNegociacaoRecebimentoVO().getUnidadeEnsino().getCodigo()), getUsuarioLogado()));
				montarListaSelectItemImpressora();
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			} finally {
				context().getExternalContext().getSessionMap().remove("contaReceberFichaAluno");
			}
		}
	}

	public void realizarCarregamentoRecebimentoTodasContasAReceberVindoTelaFichaAluno() {
		MatriculaVO obj = (MatriculaVO) context().getExternalContext().getSessionMap().get("matriculaRealizarRecebimentoTodasContasAReceberFichaAluno");
		if (obj != null && !obj.getMatricula().equals("")) {
			try {
				inicializarDadosRecebimentoVindoTelaFichaAluno(obj);
				for (ContaReceberVO contaReceberVO : obj.getListaContaReceberVOs()) {
					if (contaReceberVO.getSituacao().equals(SituacaoContaReceber.A_RECEBER.getValor())) {
						getFacadeFactory().getContaReceberFacade().carregarDados(contaReceberVO, NivelMontarDados.TODOS,
								getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getNegociacaoRecebimentoVO().getUnidadeEnsino().getCodigo()), getUsuarioLogado());
						adicionarContaReceber(contaReceberVO);
					}
				}
				montarListaSelectItemImpressora();
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			} finally {
				context().getExternalContext().getSessionMap().remove("matriculaRealizarRecebimentoTodasContasAReceberFichaAluno");
			}
		}
	}

	public void inicializarDadosRecebimentoVindoTelaFichaAluno(MatriculaVO matriculaVO) {
		novo();
		getNegociacaoRecebimentoVO().setUnidadeEnsino(matriculaVO.getUnidadeEnsino());
		getNegociacaoRecebimentoVO().setTipoPessoa(TipoPessoa.ALUNO.getValor());
		getNegociacaoRecebimentoVO().setMatricula(matriculaVO.getMatricula());
		getNegociacaoRecebimentoVO().setPessoa(matriculaVO.getAluno());
		montarUltimaMatriculaPeriodoAluno();
	}

	public String receberConta(ContaReceberVO contaReceber) {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "ContaReceberControle", "Iniciando metodo receber conta Conta Receber", "Editar");
			ConfiguracaoFinanceiroVO conf = getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getNegociacaoRecebimentoVO().getUnidadeEnsino().getCodigo());
			contaReceber.setRealizandoRecebimento(Boolean.TRUE);
			getFacadeFactory().getContaReceberFacade().carregarDados(contaReceber, NivelMontarDados.TODOS, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getNegociacaoRecebimentoVO().getUnidadeEnsino().getCodigo()), getUsuarioLogado());
			contaReceber.setTipoDescontoLancadoRecebimento("VA");
			contaReceber.setRealizandoRecebimento(Boolean.TRUE);
			Boolean usaDesc = contaReceber.getUsaDescontoCompostoPlanoDesconto();
			conf.setUsaDescontoCompostoPlanoDesconto(contaReceber.getUsaDescontoCompostoPlanoDesconto());
			verificarDataVencimentoUtilizarDiaUtil(contaReceber);
			contaReceber.getMatriculaAluno().setPlanoFinanceiroAluno(getFacadeFactory().getPlanoFinanceiroAlunoFacade().consultarPorMatriculaMatriculaUnico(contaReceber.getMatriculaAluno().getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			setNegociacaoRecebimentoVOPreenchido(contaReceber.gerarNegociacaoRecebimentoVOPreenchido(getUsuarioLogado(), contaReceber.getPessoa(), contaReceber.getResponsavelFinanceiro(), contaReceber.getFuncionario(), contaReceber.getParceiroVO(), conf));
			if (contaReceber.getContaReceberAgrupada().intValue() > 0) {
				List listaContas = getFacadeFactory().getContaReceberFacade().consultaRapidaPorCodigoContaReceberAgrupada(contaReceber.getContaReceberAgrupada(), getUsuarioLogado());
				Iterator i = listaContas.iterator();
				while (i.hasNext()) {
					ContaReceberVO conta = (ContaReceberVO) i.next();
					getFacadeFactory().getContaReceberFacade().carregarDados(conta, NivelMontarDados.TODOS, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getNegociacaoRecebimentoVO().getUnidadeEnsino().getCodigo()), getUsuarioLogado());
					adicionarContaReceber(conta);
				}
			}
			montarListaSelectItemFormaPagamento(contaReceber);
			montarListaSelectItemCaixa();
			montarListaSelectItemContaCorrente();
			conf.setUsaDescontoCompostoPlanoDesconto(usaDesc);
			registrarAtividadeUsuario(getUsuarioLogado(), "ContaReceberControle", "Finalizando metodo receber conta Conta Receber", "Editar");
			return "receber";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return "consultar";
		}
	}

	public String novo() {
		try {
			setNegociacaoRecebimentoVO(new NegociacaoRecebimentoVO());
//			setRecorrencia(false);
			setChequeVO(new ChequeVO());
			setContaReceberVO(new ContaReceberVO());
			setFormaPagamentoNegociacaoRecebimentoVO(new FormaPagamentoNegociacaoRecebimentoVO());
			setContaReceberNegociacaoRecebimentoVO(new ContaReceberNegociacaoRecebimentoVO());
			verificarPermissaoUsuarioRealizarRecebimentoRetroativo();
			verificarPermissaoRecebimentoTerceiro();
			verificarPermissaoBloquearDescontoNoRecebimento();
			setBotaoExcluir(Boolean.FALSE);
			setApresentarValorIndiceReajustePorAtraso(false);
			inicializarResponsavel();
			inicializarListasSelectItemTodosComboBox();
			setMensagemID("msg_entre_dados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("negociacaoRecebimentoForm.xhtml");
	}

	public void verificarPermissaoBloquearDescontoNoRecebimento() throws Exception {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("BloquearDescontoRecebimento",
					getUsuarioLogado());
			setBloqueiaDescontoNoRecebimento(Boolean.TRUE);
		} catch (Exception e) {
			setBloqueiaDescontoNoRecebimento(Boolean.FALSE);
		}
	}

	public void confirmaDescontoNoRecebimentoUsuarioBloqueadoDesconto() {
		try {
			UsuarioVO usuarioVerif = getUsuarioDesbloquearDescontoRecebimento();
			usuarioVerif = ControleAcesso.verificarLoginUsuario(usuarioVerif.getUsername(), usuarioVerif.getSenha(), true, Uteis.NIVELMONTARDADOS_TODOS);
			boolean permitirInformarDesconto = Boolean.FALSE;
			try {
				ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("BloquearDescontoRecebimento", usuarioVerif);
				permitirInformarDesconto = Boolean.TRUE;
			} catch (Exception e) {
				permitirInformarDesconto = Boolean.FALSE;
			}
			if (permitirInformarDesconto) {
				throw new Exception(
						UteisJSF.internacionalizar("prt_NegociacaoRecebimento_AvisoUsuarioNaoPossuiPermissaoDesconto"));
			}
			ContaReceberNegociacaoRecebimentoVO obj = getContaReceberNegociacaoRecebimentoVO();
			obj.getContaReceber().setTipoDescontoLancadoRecebimento(getTipoDescontoLancadoRecebimento());
			obj.getContaReceber().setValorDescontoLancadoRecebimento(getValorDescontoLancadoRecebimento());
			setContaReceberVO(obj.getContaReceber());
			getContaReceberVO().setUsuarioDesbloqueouDescontoRecebimento(usuarioVerif);
			getContaReceberVO().setDataUsuarioDesbloqueouDescontoRecebimento(new Date());
			verificarAbrirModalJustificativaDescontoUsuarioBloqueadoPermissaoDesconto();
			setUsuarioDesbloquearDescontoRecebimento(null);
		} catch (Exception e) {
			cancelaDescontoNoRecebimentoUsuarioBloqueadoDesconto();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void cancelaFormaRecebimentoNoRecebimentoUsuarioBloqueadoDesconto() {
		try {
		} catch (Exception e) {
		}
	}

	public void verificarPermissaoRecebimentoTerceiro() throws Exception {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("PermitirReceberTerceiro", getUsuarioLogado());
			setPermitirRecebimentoTerceiro(Boolean.TRUE);
		} catch (Exception e) {
			setPermitirRecebimentoTerceiro(Boolean.FALSE);
		}
	}

	public void preparDesbloquearDescontoRecebimento() {
		try {
			ContaReceberNegociacaoRecebimentoVO obj = (ContaReceberNegociacaoRecebimentoVO) context()
					.getExternalContext().getRequestMap().get("contaReceberNegociacaoRecebimentoItens");
			setContaReceberVO(obj.getContaReceber());
			setContaReceberNegociacaoRecebimentoVO(obj);
			setTipoDescontoLancadoRecebimento(obj.getContaReceber().getTipoDescontoLancadoRecebimento());
			setValorDescontoLancadoRecebimento(obj.getContaReceber().getValorDescontoLancadoRecebimento());
			setUsuarioDesbloquearDescontoRecebimento(null);
		} catch (Exception e) {

		}
	}

	public void preparDesbloquearFormaRecebimentoDescontoRecebimento() {
		try {
			consultarFormaPagamento();
		} catch (Exception e) {
		}
	}

	public void cancelaDescontoNoRecebimentoUsuarioBloqueadoDesconto() {
		try {
			// ContaReceberNegociacaoRecebimentoVO obj =
			// getContaReceberNegociacaoRecebimentoVO();
			// setContaReceberVO(obj.getContaReceber());
			// getContaReceberVO().setValorDescontoLancadoRecebimento(0.0);
			// verificarAbrirModalJustificativaDescontoUsuarioBloqueadoPermissaoDesconto();
			// setUsuarioDesbloquearDescontoRecebimento(null);
		} catch (Exception e) {

		}
	}

	public void confirmaFormaRecebimentoNoRecebimentoUsuarioBloqueadoDesconto() {
		try {
			UsuarioVO usuarioVerif = getUsuarioDesbloquearFormaRecebimentoNoRecebimento();
			usuarioVerif = ControleAcesso.verificarLoginUsuario(usuarioVerif.getUsername(), usuarioVerif.getSenha(),true, Uteis.NIVELMONTARDADOS_TODOS);
			boolean permitirInformarDesconto = Boolean.FALSE;
			try {
				ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("BloquearDescontoRecebimento", usuarioVerif);
				permitirInformarDesconto = Boolean.TRUE;
			} catch (Exception e) {
				permitirInformarDesconto = Boolean.FALSE;
			}
			if (permitirInformarDesconto) {
				throw new Exception("Usuário informado não possui permissão para adicionar um desconto no recebimento, verifique as funcionalidades do perfil de acesso desse usuário!");
			}
			getFormaPagamentoNegociacaoRecebimentoVO().setUsuarioDesbloqueouFormaRecebimentoNoRecebimento(usuarioVerif);
			getFormaPagamentoNegociacaoRecebimentoVO().setDataUsuarioDesbloqueouFormaRecebimentoNoRecebimento(new Date());
			adicionarFormaPagamentoNegociacaoRecebimentoPorFormaPagamento();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public static void verificarPermissaoUsuarioRealizarRecebimentoRetroativo(UsuarioVO usuario, String nomeEntidade)
			throws Exception {
		ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico(nomeEntidade, usuario);
	}

	public void verificarPermissaoUsuarioRealizarRecebimentoRetroativo() {
		Boolean liberar = false;
		try {
			verificarPermissaoUsuarioRealizarRecebimentoRetroativo(getUsuarioLogado(), "RecebimentoRetroativo");
			liberar = true;
		} catch (Exception e) {
			liberar = false;
		}
		this.setAutorizacaoRecebimentoRetroativo(liberar);
	}

	public void inicializarResponsavel() {
		try {
			getNegociacaoRecebimentoVO().getResponsavel().setCodigo(getUsuarioLogado().getCodigo());
			getNegociacaoRecebimentoVO().getResponsavel().setNome(getUsuarioLogado().getNome());
			getNegociacaoRecebimentoVO().getResponsavel().getPessoa().setCodigo(getUsuarioLogado().getPessoa().getCodigo());
			getNegociacaoRecebimentoVO().getResponsavel().getPessoa().setNome(getUsuarioLogado().getPessoa().getNome());
		} catch (Exception e) {
			getNegociacaoRecebimentoVO().setResponsavel(new UsuarioVO());
		}
	}

	public String editar() throws Exception {
		registrarAtividadeUsuario(getUsuarioLogado(), "NegociacaoRecebimentoControle", "Iniciando Editar Negociacao Recebimento", "Editando");
		NegociacaoRecebimentoVO obj = (NegociacaoRecebimentoVO) context().getExternalContext().getRequestMap().get("negociacaoRecebimentoItens");
		setNegociacaoRecebimentoVO(getFacadeFactory().getNegociacaoRecebimentoFacade().carregarDados(obj, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getNegociacaoRecebimentoVO().getUnidadeEnsino().getCodigo()), getUsuarioLogado()));
		carregarDadosParaEditarNegociacaoRecebimento(getNegociacaoRecebimentoVO());
		verificarPermissaoBloquearDescontoNoRecebimento();
		setMensagemID("msg_dados_editar", Uteis.ALERTA);
		registrarAtividadeUsuario(getUsuarioLogado(), "NegociacaoRecebimentoControle", "Finalizando Editar Negociacao Recebimento", "Editando");
		return Uteis.getCaminhoRedirecionamentoNavegacao("negociacaoRecebimentoForm.xhtml");
	}

	public void carregarDadosParaEditarNegociacaoRecebimento(NegociacaoRecebimentoVO obj) throws Exception {
		// TODO Alberto 07/12/10 corrigido para não gravar duas negociações recebimento com mesma contareceber
		carregarDadosContaRecber(getNegociacaoRecebimentoVO());
		obj.setNovoObj(Boolean.FALSE);
		setChequeVO(null);
		setContaReceberVO(null);
		getNegociacaoRecebimentoVO().atualizarChequeRecebimento();
		setFormaPagamentoNegociacaoRecebimentoVO(null);
		setContaReceberNegociacaoRecebimentoVO(null);
		inicializarListasSelectItemTodosComboBox();
		verificarSeBotaoExcluirPodeAparecer();
		verificarNegociacaoRecebimentoRecorrencia();
		if(!isApresentarValorIndiceReajustePorAtraso()){
			setApresentarValorIndiceReajustePorAtraso(getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getNegociacaoRecebimentoVO().getUnidadeEnsino().getCodigo()).isAplicarIndireReajustePorAtrasoContaReceber());
		}
		Ordenacao.ordenarLista(getNegociacaoRecebimentoVO().getFormaPagamentoNegociacaoRecebimentoVOs(), "codigo");
		montarListaSelectItemImpressora();
		montarUltimaMatriculaPeriodoAluno();
	}

	public void gravar() {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "NegociacaoRecebimentoControle",
					"Iniciando Incluir Negociacao Recebimento", "Incluindo");
			if (!negociacaoRecebimentoVO.isNovoObj().booleanValue()) {
				throw new ConsistirException("Não é possível alterar uma negociação recebimento.");
			}
			if (getNegociacaoRecebimentoVO().getApresentarModalMotivo()) {
				return;
			}
			NegociacaoRecebimentoVO.validarDados(getNegociacaoRecebimentoVO());
			if (negociacaoRecebimentoVO.getAlterouConteudo()) {
				if (negociacaoRecebimentoVO.isNovoObj()) {
					if ((Uteis.arrendondarForcando2CadasDecimais(getNegociacaoRecebimentoVO().getValorTotal()) < Uteis
							.arrendondarForcando2CadasDecimais(getNegociacaoRecebimentoVO().getValorTotalRecebimento()))
							&& getNegociacaoRecebimentoVO().getValorTroco().equals(0.0)) {
						throw new ConsistirException("O valor recebido é maior que o valor total da conta, por favor verifique as formas de pagamento.");
					}
					Uteis.aplicarHoraMinutoSegundoAtualEmData(getNegociacaoRecebimentoVO().getData());
					getNegociacaoRecebimentoVO().setConfiguracaoRecebimentoCartaoOnlineVO(getConfiguracaoRecebimentoCartaoOnlineVO());
//					if (getRecorrencia() && getPermiteRecebimentoCartaoCreditoOnline()) {
//						getNegociacaoRecebimentoVO().setPagamentoComDCC(true);
//						getFacadeFactory().getNegociacaoRecebimentoDCCFacade().incluir(getNegociacaoRecebimentoVO(),
//								getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getNegociacaoRecebimentoVO().getUnidadeEnsino().getCodigo()), true, getUsuarioLogado());
//						setModalConfirmacaoPagamentoDCC("RichFaces.$('panelConfirmacaoDCC').show()");
//					} else {
						getFacadeFactory().getNegociacaoRecebimentoFacade().incluir(negociacaoRecebimentoVO,
								executarDefinicaoConfiguracaoFinanceiroUtilizar(), true, getUsuarioLogado());
//					}
				}
				getFacadeFactory().getCampanhaFacade().finalizarAgendaCompromissoContaReceber(getNegociacaoRecebimentoVO());
				negociacaoRecebimentoVO.setAlterouConteudo(false);
				negociacaoRecebimentoVO.setMotivoAlteracao("");
			}
			verificarSeBotaoExcluirPodeAparecer();
			getNegociacaoRecebimentoVO().reiniciarControleBloqueioCompetencia();
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
			//setMensagemDetalhada("msg_dados_gravados", getNegociacaoRecebimentoVO().getMensagemPagamentoCartaoCredito(),Uteis.SUCESSO);
			registrarAtividadeUsuario(getUsuarioLogado(), "NegociacaoRecebimentoControle","Finalizando Incluir Negociacao Recebimento", "Incluindo");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void executarAutenticacaoRecebimento() {
		try {
			setTagAppletImpressaoMatricial("");
			ContaReceberNegociacaoRecebimentoVO obj = (ContaReceberNegociacaoRecebimentoVO) context()
					.getExternalContext().getRequestMap().get("contaReceberNegociacaoRecebimentoItens");
			imprimirModoMatricial(getFacadeFactory().getNegociacaoRecebimentoFacade().executarAutenticacaoRecebimento(
					getNegociacaoRecebimentoVO(), obj, getUsuarioLogado(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getNegociacaoRecebimentoVO().getUnidadeEnsino().getCodigo())));
		} catch (Exception e) {
			setTagAppletImpressaoMatricial("");
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

	}

	public String getExibirModalMotivo() {
		if (getNegociacaoRecebimentoVO().getApresentarModalMotivo()) {
			return "RichFaces.$('panelExcluir').show()";
		}
		// if (!getBotaoExcluir() &&
		// !getNegociacaoRecebimentoVO().getApresentarModalMotivo()) {
		// return "RichFaces.$('panelExcluir').show()";
		// }
		return "RichFaces.$('panelMotivo').hide(); RichFaces.$('panelExcluir').hide(); this.disabled=false";
	}

	public void setNegociacaoRecebimentoVOPreenchido(NegociacaoRecebimentoVO negociacaoRecebimentoVO) throws Exception {
		setChequeVO(new ChequeVO());
		setContaReceberVO(new ContaReceberVO());
		setFormaPagamentoNegociacaoRecebimentoVO(new FormaPagamentoNegociacaoRecebimentoVO());
		setContaReceberNegociacaoRecebimentoVO(new ContaReceberNegociacaoRecebimentoVO());
		setBotaoExcluir(false);
		setNegociacaoRecebimentoVO(negociacaoRecebimentoVO);
		montarListaSelectItemCaixa();
//		montarListaSelectItemFormaPagamento();
		montarListaSelectItemFormaPagamentoIsencaoPermuta();
		verificarPermissaoUsuarioRealizarRecebimentoRetroativo();
		verificarPermissaoRecebimentoTerceiro();
		verificarPermissaoBloquearDescontoNoRecebimento();
		// montarUltimaMatriculaPeriodoAluno();
//		setRecorrencia(false);
		setMensagemID("msg_dados_editar");
	}

	public void limparDadosPessoa() {
		getNegociacaoRecebimentoVO().setMatricula("");
		getNegociacaoRecebimentoVO().getPessoa().setNome("");
		getNegociacaoRecebimentoVO().getPessoa().setCPF("");
		getNegociacaoRecebimentoVO().getPessoa().setCodigo(0);

	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP NegociacaoRecebimentoCons.jsp. Define o tipo de consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
	 */
	public String consultar() {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "NegociacaoRecebimentoControle",
					"Iniciando Consultar Negociacao Recebimento", "Consultando");
			super.consultar();
			setListaConsulta(getFacadeFactory().getNegociacaoRecebimentoFacade().consultar(getControleConsulta(),
					getValorConsultaUnidadeEnsino(), getTipoOrigem(), Uteis.NIVELMONTARDADOS_DADOSCONSULTA, true,
					getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
			registrarAtividadeUsuario(getUsuarioLogado(), "NegociacaoRecebimentoControle",
					"Finalizando Consultar Negociacao Recebimento", "Consultando");
			return Uteis.getCaminhoRedirecionamentoNavegacao("negociacaoRecebimentoCons.xhtml");
		} catch (RuntimeException i) {
			setListaConsulta(new ArrayList<NegociacaoRecebimentoVO>(0));
			setMensagemDetalhada("msg_erro", i.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("negociacaoRecebimentoCons.xhtml");
		} catch (Exception e) {
			setListaConsulta(new ArrayList<NegociacaoRecebimentoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("negociacaoRecebimentoCons.xhtml");
		}
	}

	public void alterarContaCaixaEstorno() {
		consultarContaCaixaUsuarioResponsavel();
	}

	public void consultarContaCaixaUsuarioResponsavel() {
		try {
			List resultadoConsulta = getFacadeFactory().getContaCorrenteFacade().consultarPorFuncionarioResponsavel(
					getUsuarioLogado().getPessoa().getCodigo(),
					getNegociacaoRecebimentoVO().getUnidadeEnsino().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS,
					getUsuarioLogado());
			Iterator i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				ContaCorrenteVO obj = (ContaCorrenteVO) i.next();
				if (obj.getSituacao().equals("AT") && obj.getContaCaixa().booleanValue()) {
					objs.add(new SelectItem(obj.getCodigo(), obj.getDescricaoCompletaConta()));
				}
			}
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
			setListaContaCorrenteVOs(objs);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public static void verificarPermissaoUsuarioAlterarContaCaixaEstorno(UsuarioVO usuario, String nomeEntidade)
			throws Exception {
		ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico(nomeEntidade, usuario);
	}

	public void verificarUsuarioPodeAlterarContaCaixaEstorno() {
		Boolean liberar = false;
		try {
			setFuncionarioResponsavelCaixa(verificarfuncionarioResponsavelCaixaEstorno());
			if (!getFuncionarioResponsavelCaixa()) {
				liberar = true;
				consultarContaCaixaUsuarioResponsavel();
			} else {
				verificarPermissaoUsuarioAlterarContaCaixaEstorno(getUsuarioLogado(),
						"Recebimento_PermitirAlterarContaCaixaEstorno");
				liberar = true;
			}
		} catch (Exception e) {
			liberar = false;
		}
		this.setPermitirAlterarContaCaixaEstorno(liberar);
	}

	public Boolean verificarfuncionarioResponsavelCaixaEstorno() throws Exception {
		if (getNegociacaoRecebimentoVO().getContaCorrenteCaixa().getContaCaixa()) {
			if (getNegociacaoRecebimentoVO().getContaCorrenteCaixa().getFuncionarioResponsavel().getCodigo()
					.intValue() == 0) {
				return true;
			}
			return getFacadeFactory().getContaCorrenteFacade().consultarFuncionarioResponsavelPorCaixa(
					getUsuarioLogado().getPessoa().getCodigo(),
					getNegociacaoRecebimentoVO().getContaCorrenteCaixa().getCodigo(), getUsuarioLogado());
		}
		return true;
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe <code>NegociacaoRecebimentoVO</code> Após a exclusão ela automaticamente aciona a rotina para uma nova inclusão.
	 */
	public void excluir() {
		ContaCorrenteVO contaCorrenteTempVO = getNegociacaoRecebimentoVO().getContaCorrenteCaixa();
		ConsistirException ce = new ConsistirException();
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "NegociacaoRecebimentoControle",
					"Iniciando Excluir Negociacao Recebimento", "Excluindo");
			setBotaoExcluir(true);
			if (getNegociacaoRecebimentoVO().getApresentarModalMotivo()) {
				return;
			}
			if (getFuncionarioResponsavelCaixa()) {
				if (getAlterarContaCaixa()) {
					if (!getContaCaixaVO().getCodigo().equals(0)) {
						setContaCaixaVO(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(getContaCaixaVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
						if (negociacaoRecebimentoVO.getTipoAluno()) {
							getFacadeFactory().getNegociacaoRecebimentoFacade().incluirLogAlteracaoContaCaixaEstorno(
									negociacaoRecebimentoVO.getMatricula(),
									negociacaoRecebimentoVO.getContaCorrenteCaixa().getCodigo(),
									getContaCaixaVO().getCodigo(), getUsuarioLogado());
						}
						negociacaoRecebimentoVO.setContaCorrenteCaixa(getContaCaixaVO());
					}
				}
			} else {
				if (getContaCaixaVO().getCodigo().equals(0)) {
					ce.adicionarListaMensagemErro(
							"Deve ser informado uma conta caixa vinculada ao responsável pelo estorno.");
					throw ce;
				}
				negociacaoRecebimentoVO.setContaCorrenteCaixa(getContaCaixaVO());
			}
			getFacadeFactory().getNegociacaoRecebimentoFacade().excluir(negociacaoRecebimentoVO,
					executarDefinicaoConfiguracaoFinanceiroUtilizar(), getUsuarioLogado());
			if (Uteis.isAtributoPreenchido(getConciliacaoContaCorrenteVO())) {
				context().getExternalContext().getSessionMap().put("negociacaoConciliacaoBancaria", null);
				context().getExternalContext().getSessionMap().put("conciliacaoBancaria",
						getConciliacaoContaCorrenteVO());
				context().getExternalContext().getSessionMap().put("realizarEstornoConciliacaoBancaria", true);
			}
			novo();
			registrarAtividadeUsuario(getUsuarioLogado(), "NegociacaoRecebimentoControle",
					"Finalizando Excluir Negociacao Recebimento", "Excluindo");
			setMensagemID("msg_dados_excluidos");
		} catch (ConsistirException e) {
			negociacaoRecebimentoVO.setContaCorrenteCaixa(contaCorrenteTempVO);
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			negociacaoRecebimentoVO.setContaCorrenteCaixa(contaCorrenteTempVO);
			ce.adicionarListaMensagemErro(e.getMessage());
			setConsistirExceptionMensagemDetalhada("msg_erro", ce, Uteis.ERRO);
		}
	}

	public boolean getApresentarDataConsulta() throws Exception {
		if (getControleConsulta().getCampoConsulta().equals("data")) {
			return true;
		} else {
			return false;
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

	public void alterarTipoDescontoContaReceber() {
		ContaReceberNegociacaoRecebimentoVO obj = (ContaReceberNegociacaoRecebimentoVO) context().getExternalContext()
				.getRequestMap().get("contaReceberNegociacaoRecebimentoItens");
		if (obj != null) {
			if (obj.getContaReceber().getTipoDescontoLancadoRecebimento().equals("PO")) {
				obj.getContaReceber().setTipoDescontoLancadoRecebimento("VA");
				calcularTotal();
			} else if (obj.getContaReceber().getTipoDescontoLancadoRecebimento().equals("VA")) {
				obj.getContaReceber().setTipoDescontoLancadoRecebimento("PO");
				calcularTotal();
			}
		}
	}

	public void alterarTipoDescontoContaReceberUsuarioBloqueadoDesconto() {
		if (getTipoDescontoLancadoRecebimento().equals("PO")) {
			setTipoDescontoLancadoRecebimento("VA");
		} else if (getTipoDescontoLancadoRecebimento().equals("VA")) {
			setTipoDescontoLancadoRecebimento("PO");
		}
	}

	public void consultarFormaPagamento() throws Exception {
		try {
			setConfiguracaoFinanceiroCartaoVO(new ConfiguracaoFinanceiroCartaoVO());
			getFormaPagamentoNegociacaoRecebimentoVO().setOperadoraCartaoVO(null);
			getFormaPagamentoNegociacaoRecebimentoVO().setCategoriaDespesaVO(null);
			getFormaPagamentoNegociacaoRecebimentoVO().setConfiguracaoFinanceiroCartaoVO(null);
			getFormaPagamentoNegociacaoRecebimentoVO().setContaCorrente(null);
			getFormaPagamentoNegociacaoRecebimentoVO().setFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO(null);
			getFormaPagamentoNegociacaoRecebimentoVO().setContaCorrenteOperadoraCartaoVO(null);		
			limparMensagem();
			if (getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamento().getCodigo().intValue() != 0) {
				getFormaPagamentoNegociacaoRecebimentoVO()
						.setFormaPagamento(getFacadeFactory().getFormaPagamentoFacade().consultarPorChavePrimaria(
								getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamento().getCodigo(), false,
								Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
				if (getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamento().getTipo().equals("CH")
						&& (getNegociacaoRecebimentoVO().getPessoa().getCodigo().intValue() != 0
								|| getNegociacaoRecebimentoVO().getParceiroVO().getCodigo().intValue() != 0
								|| getNegociacaoRecebimentoVO().getFornecedor().getCodigo().intValue() != 0)) {
					Integer codigoSacado = getNegociacaoRecebimentoVO().getTipoFornecedor()
							? getNegociacaoRecebimentoVO().getFornecedor().getCodigo()
							: getNegociacaoRecebimentoVO().getTipoParceiro()
									? getNegociacaoRecebimentoVO().getParceiroVO().getCodigo()
									: getNegociacaoRecebimentoVO().getPessoa().getCodigo();
					ChequeVO chequeExistente = null;
					if (!getNegociacaoRecebimentoVO().getChequeVOs().isEmpty()) {
						chequeExistente = getNegociacaoRecebimentoVO().getChequeVOs().get(0);
					} else {
						chequeExistente = getFacadeFactory().getChequeFacade().consultarUltimoChequePorSacadoTipoSacado(
								getNegociacaoRecebimentoVO().getTipoPessoa(), codigoSacado,
								getNegociacaoRecebimentoVO().getUnidadeEnsino().getCodigo(), false,
								Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
					}
					setChequeVO(new ChequeVO());
					if (getNegociacaoRecebimentoVO().getTipoFornecedor()) {
						getChequeVO().setFornecedor(getNegociacaoRecebimentoVO().getFornecedor());
						getChequeVO().setSacado(getNegociacaoRecebimentoVO().getFornecedor().getNome());
						getChequeVO().setCpf(getNegociacaoRecebimentoVO().getFornecedor().getCPF());
						getChequeVO().setCnpj(getNegociacaoRecebimentoVO().getFornecedor().getCNPJ());
						getChequeVO().setEmitentePessoaJuridica(
								getNegociacaoRecebimentoVO().getFornecedor().getApresentarCamposCNPJ());
					} else if (getNegociacaoRecebimentoVO().getTipoParceiro()) {
						getChequeVO().setParceiro(getNegociacaoRecebimentoVO().getParceiroVO());
						getChequeVO().setSacado(getNegociacaoRecebimentoVO().getParceiroVO().getNome());
						getChequeVO().setCpf(getNegociacaoRecebimentoVO().getParceiroVO().getCPF());
						getChequeVO().setCnpj(getNegociacaoRecebimentoVO().getParceiroVO().getCNPJ());
						getChequeVO().setEmitentePessoaJuridica(
								getNegociacaoRecebimentoVO().getParceiroVO().getParceiroJuridico());
					} else {
						getChequeVO().setPessoa(getNegociacaoRecebimentoVO().getPessoa());
						getChequeVO().setSacado(getNegociacaoRecebimentoVO().getPessoa().getNome());
						getChequeVO().setCpf(getNegociacaoRecebimentoVO().getPessoa().getCPF());
					}
					if (!chequeExistente.getNumero().trim().isEmpty()) {
						getChequeVO().setEmitentePessoaJuridica(chequeExistente.getEmitentePessoaJuridica());
						getChequeVO().setAgencia(chequeExistente.getAgencia());
						getChequeVO().setBanco(chequeExistente.getBanco());
						getChequeVO().setNumeroContaCorrente(chequeExistente.getNumeroContaCorrente());
						getChequeVO().setNumero(chequeExistente.getNumero());
						getChequeVO().setNumeroFinal(chequeExistente.getNumeroFinal());
					}
					if (chequeExistente.getCodigo() > 0) {
						getChequeVO().setSacado(chequeExistente.getSacado());
					}
					getChequeVO().setValor(getNegociacaoRecebimentoVO().getResiduo());
				}				
				getFormaPagamentoNegociacaoRecebimentoVO().setOperadoraCartaoVO(null);
				getFormaPagamentoNegociacaoRecebimentoVO().setCategoriaDespesaVO(null);
				getFormaPagamentoNegociacaoRecebimentoVO().setValorRecebimento(getNegociacaoRecebimentoVO().getResiduo());
				if (getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamento().getTipo().equals("CA")
						|| getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamento().getTipo().equals("CD")) {
					consultarConfiguracaoCartaoPermiteRecebimentoOnline();
					montarListaSelectItemConfiguracaoFinanceiroCartao();
					montarListaSelectItemTipoFinanciamento();
				}
				inicializarMensagemVazia();
				return;
			}
			getFormaPagamentoNegociacaoRecebimentoVO().setFormaPagamento(new FormaPagamentoVO());
			inicializarMensagemVazia();
		} catch (ConsistirException ex) {
			setConsistirExceptionMensagemDetalhada("msg_erro", ex, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			getFormaPagamentoNegociacaoRecebimentoVO().setFormaPagamento(new FormaPagamentoVO());
		}
	}

	public void montarListaSelectItemConfiguracaoFinanceiroCartao() throws Exception {
		List<ConfiguracaoFinanceiroCartaoVO> lista = new ArrayList<ConfiguracaoFinanceiroCartaoVO>(0);
		getListaSelectItemConfiguracaoFinanceiroCartao().clear();
		ConfiguracaoFinanceiroVO configuracaoFinanceiro =  Uteis.isAtributoPreenchido(getConfiguracaoRecebimentoCartaoOnlineVO().getConfiguracaoFinanceiroVO()) ?  getConfiguracaoRecebimentoCartaoOnlineVO().getConfiguracaoFinanceiroVO() : executarDefinicaoConfiguracaoFinanceiroUtilizar();
		if (getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamento().getTipo().equals("CA")) {
			lista = getFacadeFactory().getConfiguracaoFinanceiroCartaoFacade()
					.consultarPorTipoOperadoraCartaoConfiguracaoFinanceiro("CARTAO_CREDITO", configuracaoFinanceiro.getCodigo(),
						false,
						getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamento().getCodigo(), false, getUsuarioLogado());
		} else if (getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamento().getTipo().equals("CD")) {
			lista = getFacadeFactory().getConfiguracaoFinanceiroCartaoFacade()
					.consultarPorTipoOperadoraCartaoConfiguracaoFinanceiro("CARTAO_DEBITO", configuracaoFinanceiro.getCodigo(),
						false,
						getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamento().getCodigo(), false, getUsuarioLogado());
		}
		getListaSelectItemConfiguracaoFinanceiroCartao().add(new SelectItem(0, ""));
		for (ConfiguracaoFinanceiroCartaoVO obj : lista) {
			if (obj.getOperadoraCartaoVO().getTipo().equals("CARTAO_CREDITO")) {
				getListaSelectItemConfiguracaoFinanceiroCartao()
						.add(new SelectItem(obj.getCodigo(), obj.getContaCorrenteVO().getBancoAgenciaContaCorrente() + "-" + obj.getOperadoraCartaoVO().getNome() + " - "
								+ obj.getOperadoraCartaoVO().getOperadoraCartaoCreditoApresentar()));
			} else {
				getListaSelectItemConfiguracaoFinanceiroCartao()
						.add(new SelectItem(obj.getCodigo(), obj.getContaCorrenteVO().getBancoAgenciaContaCorrente() + "-" + obj.getOperadoraCartaoVO().getNome()));
			}
		}
		removerObjetoMemoria(lista);
		if (lista.isEmpty()) {
			setConfiguracaoFinanceiroCartaoVO(new ConfiguracaoFinanceiroCartaoVO());
		}
	}

	public void realizarMontagemCartaoCredito()  {
		try {
			if (Uteis.isAtributoPreenchido(getConfiguracaoFinanceiroCartaoVO())) {
				setConfiguracaoFinanceiroCartaoVO(getFacadeFactory().getConfiguracaoFinanceiroCartaoFacade().consultarPorChavePrimaria(getConfiguracaoFinanceiroCartaoVO().getCodigo(),NivelMontarDados.TODOS, getUsuarioLogado()));				
				getFormaPagamentoNegociacaoRecebimentoVO().setOperadoraCartaoVO(getConfiguracaoFinanceiroCartaoVO().getOperadoraCartaoVO());
				getFormaPagamentoNegociacaoRecebimentoVO().setCategoriaDespesaVO(getConfiguracaoFinanceiroCartaoVO().getCategoriaDespesaVO());
				getFormaPagamentoNegociacaoRecebimentoVO().setContaCorrente(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(getConfiguracaoFinanceiroCartaoVO().getContaCorrenteVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
				getFormaPagamentoNegociacaoRecebimentoVO().setContaCorrenteOperadoraCartaoVO(getFormaPagamentoNegociacaoRecebimentoVO().getContaCorrente());				
				getFormaPagamentoNegociacaoRecebimentoVO().setConfiguracaoFinanceiroCartaoVO(getConfiguracaoFinanceiroCartaoVO());
				getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setConfiguracaoFinanceiroCartaoVO(getConfiguracaoFinanceiroCartaoVO());
				if (getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamento().getTipo().equals("CD")) {
					getFormaPagamentoNegociacaoRecebimentoVO().setQtdeParcelasCartaoCredito(1);
					getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoFacade().montarDataCreditoPorConfiguracaoFinanceiraCartao(getFormaPagamentoNegociacaoRecebimentoVO(), getNegociacaoRecebimentoVO().getData(), getUsuarioLogado());
					if (getFormaPagamentoNegociacaoRecebimentoVO().getContaCorrenteOperadoraCartaoVO().isUtilizaTaxaCartaoDebito()) {
						getFormaPagamentoNegociacaoRecebimentoVO().setTaxaDeOperacao(getFormaPagamentoNegociacaoRecebimentoVO().getConfiguracaoFinanceiroCartaoVO().getTaxaBancaria(1, getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getTipoFinanciamentoEnum()));
					}
				} else {
					verificarContasRecbimentoOnline();
					if (getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getConfiguracaoFinanceiroCartaoVO().getPermitiRecebimentoCartaoOnline()) {
						if (!getConfiguracaoRecebimentoCartaoOnlineVO().getCodigo().equals(0) && getPermiteRecebimentoCartaoCreditoOnline()) {
							getFormaPagamentoNegociacaoRecebimentoVO().setConfiguracaoRecebimentoCartaoOnlineVO(getConfiguracaoRecebimentoCartaoOnlineVO());
							getNegociacaoRecebimentoVO().setConfiguracaoRecebimentoCartaoOnlineVO(getConfiguracaoRecebimentoCartaoOnlineVO());
						}
					}
					montarListaSelectItemQuantidadeParcelasConfiguracaoFinanceiroCartao();
					getFormaPagamentoNegociacaoRecebimentoVO().setConfiguracaoFinanceiroCartaoVO(getConfiguracaoFinanceiroCartaoVO());					
				}
				if (getPermiteRecebimentoCartaoCreditoOnline()) {
					atribuirTipoFinanciamentoFormaPagamentoRecebimentoCartaoCredito();
				}
				return;
			}
			limparMensagem();
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
			setFormaPagamentoNegociacaoRecebimentoVO(new FormaPagamentoNegociacaoRecebimentoVO());
		} catch (Exception e) {
			setFormaPagamentoNegociacaoRecebimentoVO(new FormaPagamentoNegociacaoRecebimentoVO());
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarFuncionario() {
		try {
			List objs = new ArrayList(0);
			if (getValorConsultaFuncionario().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			getFacadeFactory().getPessoaFacade().setIdEntidade("Funcionario");
			if (getCampoConsultaFuncionario().equals("nome")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorNome(getValorConsultaFuncionario(),
						this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
						getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("matricula")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorMatricula(getValorConsultaFuncionario(),
						this.getUnidadeEnsinoLogado().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS,
						getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("nomeCidade")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeCidade(getValorConsultaFuncionario(),
						this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
						getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("CPF")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorCPF(getValorConsultaFuncionario(),
						this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
						getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("cargo")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeCargo(getValorConsultaFuncionario(),
						this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
						getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("departamento")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeDepartamento(
						getValorConsultaFuncionario(), "FU", this.getUnidadeEnsinoLogado().getCodigo(), false,
						Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("unidadeEnsino")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeUnidadeEnsino(
						getValorConsultaFuncionario(), "FU", this.getUnidadeEnsinoLogado().getCodigo(), false,
						Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaFuncionario(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaFuncionario(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarAluno() {
		try {
			List objs = new ArrayList(0);
			if (getValorConsultaAluno().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			if (getCampoConsultaAluno().equals("matricula")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatricula(getValorConsultaAluno(),
						this.getUnidadeEnsinoLogado().getCodigo(), true, false, getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("nomePessoa")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(),
						this.getUnidadeEnsinoLogado().getCodigo(), true, false, getUsuarioLogado());
			}
			setListaConsultaAluno(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAluno(null);
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}

	public void consultarRequisitante() {
		try {
			getFacadeFactory().getPessoaFacade().setIdEntidade("Candidato");
			List objs = new ArrayList(0);
			if (getValorConsultaRequisitante().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			if (getCampoConsultaRequisitante().equals("codigo")) {
				int valorInt = Integer.parseInt(getValorConsultaRequisitante());
				objs = getFacadeFactory().getPessoaFacade().consultarPorCodigo(new Integer(valorInt), "", false,
						Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaRequisitante().equals("nome")) {
				objs = getFacadeFactory().getPessoaFacade().consultarPorNome(getValorConsultaRequisitante(), "", false,
						Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}

			if (getCampoConsultaRequisitante().equals("nomeCidade")) {
				objs = getFacadeFactory().getPessoaFacade().consultarPorNomeCidade(getValorConsultaRequisitante(), "",
						false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaRequisitante().equals("CPF")) {
				objs = getFacadeFactory().getPessoaFacade().consultarPorCPF(getValorConsultaRequisitante(), "", false,
						Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaRequisitante().equals("RG")) {
				objs = getFacadeFactory().getPessoaFacade().consultarPorRG(getValorConsultaRequisitante(), "", false,
						Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaRequisitante().equals("necessidadesEspeciais")) {
				objs = getFacadeFactory().getPessoaFacade().consultarPorNecessidadesEspeciais(
						getValorConsultaRequisitante(), "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
						getUsuarioLogado());
			}
			setListaConsultaRequisitante(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaRequisitante(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarCandidato() {
		try {
			getFacadeFactory().getPessoaFacade().setIdEntidade("Candidato");
			List objs = new ArrayList(0);
			if (getValorConsultaCandidato().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			if (getCampoConsultaCandidato().equals("codigo")) {
				int valorInt = Integer.parseInt(getValorConsultaCandidato());
				objs = getFacadeFactory().getPessoaFacade().consultarPorCodigo(new Integer(valorInt), "CA", false,
						Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaCandidato().equals("nome")) {
				objs = getFacadeFactory().getPessoaFacade().consultarPorNome(getValorConsultaCandidato(), "CA", false,
						Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}

			if (getCampoConsultaCandidato().equals("nomeCidade")) {
				objs = getFacadeFactory().getPessoaFacade().consultarPorNomeCidade(getValorConsultaCandidato(), "CA",
						false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaCandidato().equals("CPF")) {
				objs = getFacadeFactory().getPessoaFacade().consultarPorCPF(getValorConsultaCandidato(), "CA", false,
						Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaCandidato().equals("RG")) {
				objs = getFacadeFactory().getPessoaFacade().consultarPorRG(getValorConsultaCandidato(), "CA", false,
						Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaCandidato().equals("necessidadesEspeciais")) {
				objs = getFacadeFactory().getPessoaFacade().consultarPorNecessidadesEspeciais(
						getValorConsultaCandidato(), "CA", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
						getUsuarioLogado());
			}
			setListaConsultaCandidato(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCandidato(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP ParceiroCons.jsp. Define o tipo de consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
	 */
	public void consultarParceiro() {
		try {
			super.consultar();
			List objs = new ArrayList(0);
			if (getCampoConsultaParceiro().equals("codigo")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getParceiroFacade().consultarPorCodigo(new Integer(valorInt), false,
						Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaParceiro().equals("nome")) {
				objs = getFacadeFactory().getParceiroFacade().consultarPorNome(getValorConsultaParceiro(), false,
						Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado());
			}
			if (getCampoConsultaParceiro().equals("razaoSocial")) {
				objs = getFacadeFactory().getParceiroFacade().consultarPorRazaoSocial(getValorConsultaParceiro(), false,
						Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaParceiro().equals("RG")) {
				objs = getFacadeFactory().getParceiroFacade().consultarPorRG(getValorConsultaParceiro(), false,
						Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaParceiro().equals("CPF")) {
				objs = getFacadeFactory().getParceiroFacade().consultarPorCPF(getValorConsultaParceiro(), false,
						Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaParceiro().equals("tipoParceiro")) {
				objs = getFacadeFactory().getParceiroFacade().consultarPorTipoParceiro(getValorConsultaParceiro(),
						false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaParceiro(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List getTipoConsultaComboAluno() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nomePessoa", "Aluno"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		return itens;
	}

	public List getTipoConsultaComboFuncionario() {
		List itens = new ArrayList(0);

		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("CPF", "CPF"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("cargo", "Cargo"));
		itens.add(new SelectItem("departamento", "Departamento"));

		return itens;
	}

	public List getTipoConsultaComboCandidato() {
		List itens = new ArrayList(0);

		itens.add(new SelectItem("nome", "Nome"));

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

	public void selecionarFuncionario() {
		FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItens");
		this.getNegociacaoRecebimentoVO().setMatricula(obj.getMatricula());
		this.getNegociacaoRecebimentoVO().setPessoa(obj.getPessoa());
		Uteis.liberarListaMemoria(getListaConsultaFuncionario());
		removerObjetoMemoria(obj);
		valorConsultaFuncionario = "";
		campoConsultaFuncionario = "";

	}

	public void selecionarFuncionarioTerceiro() {
		FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItens");
		this.setFuncionarioTerceiroVO(obj);
		this.setPessoaTerceiroVO(obj.getPessoa());
		Uteis.liberarListaMemoria(getListaConsultaFuncionario());
		valorConsultaFuncionario = "";
		campoConsultaFuncionario = "";

	}

	public void selecionarAluno() {
		try {
			MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("alunoItens");
			this.getNegociacaoRecebimentoVO().setMatricula(obj.getMatricula());
			this.getNegociacaoRecebimentoVO().setPessoa(obj.getAluno());
			getNegociacaoRecebimentoVO().setMatriculaVO(obj);
			Uteis.liberarListaMemoria(getListaConsultaAluno());
			montarUltimaMatriculaPeriodoAluno();
			removerObjetoMemoria(obj);
			valorConsultaAluno = "";
			campoConsultaAluno = "";
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void selecionarAlunoTerceiro() {
		MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("alunoTerceiroItens");
		this.setMatriculaTerceiroVO(obj);
		this.setPessoaTerceiroVO(obj.getAluno());
		this.setTipoPessoaTerceiro("AL");
		consultarContaReceberTerceiro();
		Uteis.liberarListaMemoria(getListaConsultaAluno());
		valorConsultaAluno = "";
		campoConsultaAluno = "";

	}

	public void selecionarCandidato() {
		PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("candidatoItens");
		this.getNegociacaoRecebimentoVO().setPessoa(obj);
		Uteis.liberarListaMemoria(getListaConsultaCandidato());
		// removerObjetoMemoria(obj);
		valorConsultaCandidato = "";
		campoConsultaCandidato = "";
	}

	public void selecionarCandidatoTerceiro() {
		PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("candidatoItens");
		this.setPessoaTerceiroVO(obj);
		consultarContaReceberTerceiro();
		Uteis.liberarListaMemoria(getListaConsultaCandidato());
		valorConsultaCandidato = "";
		campoConsultaCandidato = "";
	}

	public void selecionarParceiro() {
		ParceiroVO obj = (ParceiroVO) context().getExternalContext().getRequestMap().get("parceiroItens");
		this.getNegociacaoRecebimentoVO().setParceiroVO(obj);
		Uteis.liberarListaMemoria(getListaConsultaCandidato());
		removerObjetoMemoria(obj);
		valorConsultaCandidato = "";
		campoConsultaCandidato = "";
	}

	public void selecionarParceiroTerceiro() {
		ParceiroVO obj = (ParceiroVO) context().getExternalContext().getRequestMap().get("parceiroItens");
		this.setParceiroTerceiroVO(obj);
		consultarContaReceberTerceiro();
		Uteis.liberarListaMemoria(getListaConsultaParceiro());
		removerObjetoMemoria(obj);
		valorConsultaCandidato = "";
		campoConsultaCandidato = "";
	}

	public void selecionarRequisitante() {
		PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("requerenteItens");
		this.getNegociacaoRecebimentoVO().setPessoa(obj);
		Uteis.liberarListaMemoria(getListaConsultaRequisitante());
		// removerObjetoMemoria(obj);
		valorConsultaRequisitante = "";
		campoConsultaRequisitante = "";
	}

	public void selecionarRequisitanteTerceiro() {
		PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("requerenteItens");
		this.setPessoaTerceiroVO(obj);
		consultarContaReceberTerceiro();
		Uteis.liberarListaMemoria(getListaConsultaRequisitante());
		valorConsultaRequisitante = "";
		campoConsultaRequisitante = "";
	}

	public void consultarAlunoPorMatricula() {
		try {
			if (!getNegociacaoRecebimentoVO().getMatricula().equals("")
					&& getNegociacaoRecebimentoVO().getTipoPessoa().equals("AL")) {
				MatriculaVO objs = getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(
						getNegociacaoRecebimentoVO().getMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), true,
						false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
				if (!objs.getMatricula().equals("")) {
					this.getNegociacaoRecebimentoVO().setMatricula(objs.getMatricula());
					this.getNegociacaoRecebimentoVO().setPessoa(objs.getAluno());
					montarUltimaMatriculaPeriodoAluno();
					setMensagemID("msg_dados_consultados");
					return;
				}
				getNegociacaoRecebimentoVO().setMatricula("");
				getNegociacaoRecebimentoVO().getPessoa().setNome("");
				getNegociacaoRecebimentoVO().getPessoa().setCodigo(0);
				setMensagemID("msg_erro_dadosnaoencontrados");
			}
		} catch (Exception e) {
			getNegociacaoRecebimentoVO().setMatricula("");
			getNegociacaoRecebimentoVO().getPessoa().setCodigo(0);
			getNegociacaoRecebimentoVO().getPessoa().setNome("");
			getNegociacaoRecebimentoVO().getPessoa().setCPF("");
			setMensagemID("msg_erro_dadosnaoencontrados");
		}
	}

	public void consultarFuncionarioPorCodigo() {
		try {
			if (!this.getNegociacaoRecebimentoVO().getMatricula().equals("")
					&& getNegociacaoRecebimentoVO().getTipoPessoa().equals("FU")) {
				FuncionarioVO funcionario = getFacadeFactory().getFuncionarioFacade().consultarPorRequisitanteMatricula(
						this.getNegociacaoRecebimentoVO().getMatricula(), this.getUnidadeEnsinoLogado().getCodigo(),
						false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				if (funcionario.getCodigo().intValue() != 0) {
					this.getNegociacaoRecebimentoVO().setPessoa(funcionario.getPessoa());
					this.getNegociacaoRecebimentoVO().setMatricula(funcionario.getMatricula());
					setMensagemID("msg_dados_consultados");
				} else {
					throw new Exception();
				}
			}
		} catch (Exception e) {
			getNegociacaoRecebimentoVO().setMatricula("");
			getNegociacaoRecebimentoVO().getPessoa().setCodigo(0);
			getNegociacaoRecebimentoVO().getPessoa().setNome("");
			getNegociacaoRecebimentoVO().getPessoa().setCPF("");
			setMensagemID("msg_erro_dadosnaoencontrados");
		}
	}

	public void consultarCandidatoPorCPF() {
		try {
			if (!this.getNegociacaoRecebimentoVO().getPessoa().getCPF().equals("")) {
				PessoaVO pessoa = getFacadeFactory().getPessoaFacade().consultarPorCPFUnico(
						this.getNegociacaoRecebimentoVO().getPessoa().getCPF(), 0, "CA", false,
						Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				if (pessoa.getCodigo().intValue() != 0) {
					this.getNegociacaoRecebimentoVO().setPessoa(pessoa);
					setMensagemID("msg_dados_consultados");
					return;
				}
				throw new Exception();
			}
		} catch (Exception e) {
			getNegociacaoRecebimentoVO().getPessoa().setCodigo(0);
			getNegociacaoRecebimentoVO().getPessoa().setNome("");
			getNegociacaoRecebimentoVO().getPessoa().setCPF("");
			setMensagemID("msg_erro_dadosnaoencontrados");
		}
	}

	public void consultarParceiroPorCPF() {
		try {
			if (!this.getNegociacaoRecebimentoVO().getParceiroVO().getCPF().equals("")) {
				ParceiroVO parceiro = getFacadeFactory().getParceiroFacade().consultarPorCPFUnico(
						this.getNegociacaoRecebimentoVO().getParceiroVO().getCPF(), false,
						Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				if (parceiro.getCodigo().intValue() != 0) {
					this.getNegociacaoRecebimentoVO().setParceiroVO(parceiro);
					setMensagemID("msg_dados_consultados");
					return;
				}
				throw new Exception();
			}
		} catch (Exception e) {
			getNegociacaoRecebimentoVO().getParceiroVO().setCodigo(0);
			getNegociacaoRecebimentoVO().getParceiroVO().setNome("");
			getNegociacaoRecebimentoVO().getParceiroVO().setCPF("");
			setMensagemID("msg_erro_dadosnaoencontrados");
		}
	}

	public void consultarParceiroPorCNPJ() {
		try {
			if (!this.getNegociacaoRecebimentoVO().getParceiroVO().getCPF().equals("")) {
				ParceiroVO parceiro = getFacadeFactory().getParceiroFacade().consultarPorCNPJUnico(
						this.getNegociacaoRecebimentoVO().getParceiroVO().getCNPJ(), false,
						Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				if (parceiro.getCodigo().intValue() != 0) {
					this.getNegociacaoRecebimentoVO().setParceiroVO(parceiro);
					setMensagemID("msg_dados_consultados");
					return;
				}
				throw new Exception();
			}
		} catch (Exception e) {
			getNegociacaoRecebimentoVO().getParceiroVO().setCodigo(0);
			getNegociacaoRecebimentoVO().getParceiroVO().setNome("");
			getNegociacaoRecebimentoVO().getParceiroVO().setCPF("");
			setMensagemID("msg_erro_dadosnaoencontrados");
		}
	}

	public void consultarRequerentePorCPF() {
		try {
			if (!this.getNegociacaoRecebimentoVO().getPessoa().getCPF().equals("")) {
				PessoaVO pessoa = getFacadeFactory().getPessoaFacade().consultarPorCPFUnico(
						this.getNegociacaoRecebimentoVO().getPessoa().getCPF(), 0, "", false,
						Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				if (pessoa.getCodigo().intValue() != 0) {
					this.getNegociacaoRecebimentoVO().setPessoa(pessoa);
					setMensagemID("msg_dados_consultados");
					return;
				}
				throw new Exception();
			}
		} catch (Exception e) {
			getNegociacaoRecebimentoVO().getPessoa().setCodigo(0);
			getNegociacaoRecebimentoVO().getPessoa().setNome("");
			getNegociacaoRecebimentoVO().getPessoa().setCPF("");
			setMensagemID("msg_erro_dadosnaoencontrados");
		}
	}

	public List getListaSelectItemAlunoFuncionarioCandidato() throws Exception {
		List objs = new ArrayList(0);
		objs.add(new SelectItem("", ""));
		Hashtable alunoFuncionarioCandidato = (Hashtable) Dominios.getAlunoFuncionarioCandidatoParceiro();
		Enumeration keys = alunoFuncionarioCandidato.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) alunoFuncionarioCandidato.get(value);
			objs.add(new SelectItem(value, label));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		return objs;
	}

	public String getTipoContasAReceber() {
		if (tipoContasAReceber == null) {
			tipoContasAReceber = "VE";
		}
		return tipoContasAReceber;
	}

	/**
	 * @param tipoContasAPagar
	 *            the tipoContasAPagar to set
	 */
	public void setTipoContasAReceber(String tipoContasAReceber) {
		this.tipoContasAReceber = tipoContasAReceber;
	}

	public List getListaSelectItemTipoContasAPagar() throws Exception {
		List objs = new ArrayList(0);
		objs.add(new SelectItem("VE", "Vencidas"));
		objs.add(new SelectItem("AV", "A Vencer"));
		return objs;
	}

	public List getListaSelectItemTipoPessoaTerceiro() throws Exception {
		List objs = new ArrayList(0);
		objs.add(new SelectItem("AL", "Aluno"));
		objs.add(new SelectItem("CA", "Candidato"));
		objs.add(new SelectItem("RF", "Responsável Financeiro"));
		objs.add(new SelectItem("RE", "Requerente"));
		objs.add(new SelectItem("FU", "Funcionário"));
		return objs;
	}

	public void consultarContaReceberTipoPessoa() {
		if (!getReceberContaReceberTerceiro()) {
			consultarContaReceber();
		} else {
			consultarContaReceberTerceiro();
		}
	}

	public void consultarContaReceber() {
		try {
			if (getListaTipoOrigemPermidoContaCaixa().isEmpty() && negociacaoRecebimentoVO.getContaCorrenteCaixa().getCodigo() > 0) {
				setListaConsultaContaReceber(new ArrayList(0));
				setMensagemDetalhada("msg_erro", "Esta conta caixa não permite recebimento de nenhum tipo de origem!");
			} else {
				verificarPermissaoUsuarioPagarReceberContasEmCaixaUnidadeDiferenteDaConta();
				setMensagemAvisoAdicionarApenasCinquentaContaReceberParceiro("");
				setQuantidadeContaReceber(0);
				List listaContaReceber = new ArrayList(0);
				Integer unidadeEnsino = getNegociacaoRecebimentoVO().getUnidadeEnsino().getCodigo();
				if (getNegociacaoRecebimentoVO().getTipoParceiro()) {
					// List listaConvenio = new ArrayList(0);
					// listaConvenio =
					// getFacadeFactory().getConvenioFacade().consultarPorNomeParceiro(getNegociacaoRecebimentoVO().getParceiroVO().getNome(),
					// getTipoOrigem(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,
					// getUsuarioLogado());
					// for (ConvenioVO convenio : (List<ConvenioVO>) listaConvenio)
					// {
					// listaContaReceber.addAll(getFacadeFactory().getContaReceberFacade().consultarPorTipoEConvenioEUnidadeEnsino(getTipoContasAReceber(),
					// convenio.getCodigo(),
					// getNegociacaoRecebimentoVO().getUnidadeEnsino().getCodigo(),
					// getTipoOrigem(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
					// getConfiguracaoFinanceiroPadraoSistema(),
					// getUsuarioLogado()));
					// }
					listaContaReceber.addAll(getFacadeFactory().getContaReceberFacade()
							.consultarPorTipoEConvenioEUnidadeEnsinoDadosApresentacaoModalNegociacaoRecebimento(
									getNegociacaoRecebimentoVO().getParceiroVO().getNome(), getTipoContasAReceber(),
									unidadeEnsino, getTipoOrigem(), getListaTipoOrigemPermidoContaCaixa(), false,
									getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(unidadeEnsino),
									getUsuarioLogado()));
					// if
					// (getLoginControle().getPermissaoAcessoMenuVO().getPermitirApenasContasDaBiblioteca())
					// {
					// listaContaReceber.addAll(getFacadeFactory().getContaReceberFacade().consultarPorParceiroPorTipoOrigem(getTipoContasAReceber(),
					// getNegociacaoRecebimentoVO().getParceiroVO().getCodigo(),
					// getTipoOrigem(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,
					// getConfiguracaoFinanceiroPadraoSistema(),
					// getUsuarioLogado()));
					// } else {
					// listaContaReceber.addAll(getFacadeFactory().getContaReceberFacade().consultarPorParceiroPorTipoOrigem(getTipoContasAReceber(),
					// getNegociacaoRecebimentoVO().getParceiroVO().getCodigo(), "",
					// false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,
					// getConfiguracaoFinanceiroPadraoSistema(),
					// getUsuarioLogado()));
					// }

					Collections.sort(listaContaReceber, new Uteis.OrdenaListaContaReceberVOPorDataVencimento());
				} else if (getNegociacaoRecebimentoVO().getTipoAluno()) {
					listaContaReceber.addAll(getFacadeFactory().getContaReceberFacade()
							.consultarPorTipoUnidadeEnsinoMatricula(getTipoContasAReceber(), 0,
									getNegociacaoRecebimentoVO().getMatricula(), unidadeEnsino, getTipoOrigem(),
									getListaTipoOrigemPermidoContaCaixa(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
									getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(unidadeEnsino),
									getUsuarioLogado()));
				} else if (getNegociacaoRecebimentoVO().getTipoFornecedor()) {
					listaContaReceber
							.addAll(getFacadeFactory().getContaReceberFacade().consultarPorTipoUnidadeEnsinoFornecedor(
									getTipoContasAReceber(), getNegociacaoRecebimentoVO().getFornecedor().getCodigo(),
									unidadeEnsino, getTipoOrigem(), getListaTipoOrigemPermidoContaCaixa(), false,
									Uteis.NIVELMONTARDADOS_DADOSBASICOS,
									getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(unidadeEnsino),
									getUsuarioLogado()));
				} else {
					listaContaReceber.addAll(getFacadeFactory().getContaReceberFacade().consultarPorTipoEUnidadeEnsino(
							getTipoContasAReceber(), getNegociacaoRecebimentoVO().getPessoa().getCodigo(), unidadeEnsino,
							getTipoOrigem(), getListaTipoOrigemPermidoContaCaixa(), false,
							Uteis.NIVELMONTARDADOS_DADOSBASICOS,
							getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(unidadeEnsino), getUsuarioLogado()));
				}
				setListaConsultaContaReceber(
						getFacadeFactory().getContaReceberFacade().executarCalculoValorFinalASerPago(listaContaReceber,
								getUsuarioLogado(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(unidadeEnsino),
								getNegociacaoRecebimentoVO().getData()));
				setMensagemID("msg_dados_consultados");
			}

		} catch (Exception e) {
			setListaConsultaContaReceber(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarContaReceberTerceiro() {
		try {
			if (getListaTipoOrigemPermidoContaCaixa().isEmpty() && negociacaoRecebimentoVO.getContaCorrenteCaixa().getCodigo() > 0) {
				setListaConsultaContaReceber(new ArrayList(0));
				setMensagemDetalhada("msg_erro", "Esta conta caixa não permite recebimento de nenhum tipo de origem!");
			} else {
				List listaContaReceber = new ArrayList(0);
				if (this.getTipoParceiroTerceiro()) {
					// List listaConvenio = new ArrayList(0);
					// listaConvenio =
					// getFacadeFactory().getConvenioFacade().consultarPorNomeParceiro(this.getParceiroTerceiroVO().getNome(),
					// getTipoOrigem(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,
					// getUsuarioLogado());
					// for (ConvenioVO convenio : (List<ConvenioVO>) listaConvenio)
					// {
					// listaContaReceber.addAll(getFacadeFactory().getContaReceberFacade().consultarPorTipoEConvenioEUnidadeEnsino(getTipoContasAReceber(),
					// convenio.getCodigo(),
					// getNegociacaoRecebimentoVO().getUnidadeEnsino().getCodigo(),
					// getTipoOrigem(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
					// getConfiguracaoFinanceiroPadraoSistema(),
					// getUsuarioLogado()));
					// }
					listaContaReceber.addAll(getFacadeFactory().getContaReceberFacade()
							.consultarPorTipoEConvenioEUnidadeEnsinoDadosApresentacaoModalNegociacaoRecebimento(
									this.getParceiroTerceiroVO().getNome(), getTipoContasAReceber(),
									getNegociacaoRecebimentoVO().getUnidadeEnsino().getCodigo(), getTipoOrigem(),
									getListaTipoOrigemPermidoContaCaixa(), false, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getNegociacaoRecebimentoVO().getUnidadeEnsino().getCodigo()),
									getUsuarioLogado()));
					if (getLoginControle().getPermissaoAcessoMenuVO().getPermitirApenasContasDaBiblioteca()) {
						listaContaReceber
								.addAll(getFacadeFactory().getContaReceberFacade().consultarPorParceiroPorTipoOrigem(
										getTipoContasAReceber(), this.getParceiroTerceiroVO().getCodigo(), getTipoOrigem(),
										false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,
										getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getNegociacaoRecebimentoVO().getUnidadeEnsino().getCodigo()), getUsuarioLogado()));
					} else {
						listaContaReceber
								.addAll(getFacadeFactory().getContaReceberFacade().consultarPorParceiroPorTipoOrigem(
										getTipoContasAReceber(), this.getParceiroTerceiroVO().getCodigo(), "", false,
										Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getNegociacaoRecebimentoVO().getUnidadeEnsino().getCodigo()),
										getUsuarioLogado()));
					}

					Collections.sort(listaContaReceber, new Uteis.OrdenaListaContaReceberVOPorDataVencimento());
				} else if (this.getTipoAlunoTerceiro()) {
					listaContaReceber
							.addAll(getFacadeFactory().getContaReceberFacade().consultarPorTipoUnidadeEnsinoMatricula(
									getTipoContasAReceber(), 0, this.getMatriculaTerceiroVO().getMatricula(),
									getNegociacaoRecebimentoVO().getUnidadeEnsino().getCodigo(), getTipoOrigem(),
									getListaTipoOrigemPermidoContaCaixa(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
									getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getNegociacaoRecebimentoVO().getUnidadeEnsino().getCodigo()), getUsuarioLogado()));
				} else if (getNegociacaoRecebimentoVO().getTipoFornecedor()) {
					listaContaReceber
							.addAll(getFacadeFactory().getContaReceberFacade().consultarPorTipoUnidadeEnsinoFornecedor(
									getTipoContasAReceber(), this.getFornecedorTerceiroVO().getCodigo(),
									getNegociacaoRecebimentoVO().getUnidadeEnsino().getCodigo(), getTipoOrigem(),
									getListaTipoOrigemPermidoContaCaixa(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
									getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getNegociacaoRecebimentoVO().getUnidadeEnsino().getCodigo()), getUsuarioLogado()));
				} else {
					listaContaReceber.addAll(getFacadeFactory().getContaReceberFacade().consultarPorTipoEUnidadeEnsino(
							getTipoContasAReceber(), this.getPessoaTerceiroVO().getCodigo(),
							getNegociacaoRecebimentoVO().getUnidadeEnsino().getCodigo(), getTipoOrigem(),
							getListaTipoOrigemPermidoContaCaixa(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
							getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getNegociacaoRecebimentoVO().getUnidadeEnsino().getCodigo()), getUsuarioLogado()));
				}
				setListaConsultaContaReceber(getFacadeFactory().getContaReceberFacade().executarCalculoValorFinalASerPago(
						listaContaReceber, getUsuarioLogado(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getNegociacaoRecebimentoVO().getUnidadeEnsino().getCodigo()),
						getNegociacaoRecebimentoVO().getData()));
				setMensagemID("msg_dados_consultados");
			}

		} catch (Exception e) {
			setListaConsultaContaReceber(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarContaReceber() {
		try {
			if (!negociacaoRecebimentoVO.isNovoObj().booleanValue()) {
				throw new ConsistirException("Não é possível alterar uma negociação recebimento.");
			}
			ContaReceberVO obj = (ContaReceberVO) context().getExternalContext().getRequestMap().get("contaReceberItens");
			selecionarContaReceber(obj);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void selecionarContaReceber(ContaReceberVO obj) {
		try {
			if (!negociacaoRecebimentoVO.isNovoObj().booleanValue()) {
				throw new ConsistirException("Não é possível alterar uma negociação recebimento.");
			}			
			List<ContaReceberVO> listaAuxiliar = new ArrayList<ContaReceberVO>(0);
			if (obj.getContaReceberAgrupada().intValue() == 0) {
				adicionarContaReceber(obj);
				listaAuxiliar.add(obj);
			} else {
				List<ContaReceberVO> listaContas = getFacadeFactory().getContaReceberFacade()
						.consultaRapidaPorCodigoContaReceberAgrupada(obj.getContaReceberAgrupada(), getUsuarioLogado());
				Iterator i = listaContas.iterator();
				while (i.hasNext()) {
					ContaReceberVO conta = (ContaReceberVO) i.next();
					adicionarContaReceber(conta);
					listaAuxiliar.add(conta);
				}
			}
			if (Uteis.isAtributoPreenchido(obj.getProcessamentoIntegracaoFinanceiraDetalheVO().getCodigo())) {
				Map<Integer, ConfiguracaoRecebimentoCartaoOnlineVO> mapConfRecebCartaoOnlineVOs = getFacadeFactory().getContaReceberFacade()
					.realizarVerificacaoPermiteRecebimentoOnlineUsarMinhasContasVisaoAluno(obj.getMatriculaAluno().getMatricula(), listaAuxiliar, getUsuarioLogado());
				if (Uteis.isAtributoPreenchido(obj.getProcessamentoIntegracaoFinanceiraDetalheVO().getCodigo())) {
					if (mapConfRecebCartaoOnlineVOs.isEmpty() ||
							!mapConfRecebCartaoOnlineVOs.containsKey(getNegociacaoRecebimentoVO().getUnidadeEnsino().getCodigo()) ||
							!Uteis.isAtributoPreenchido(mapConfRecebCartaoOnlineVOs.get(getNegociacaoRecebimentoVO().getUnidadeEnsino().getCodigo()))) {
						getNegociacaoRecebimentoVO().getContaReceberNegociacaoRecebimentoVOs().clear();
						throw new Exception("Somente é possível realizar o recebimento de uma conta de integração financeira com recebimento de cartão online habilitado.");
					}
				}
				setConfiguracaoRecebimentoCartaoOnlineVO(mapConfRecebCartaoOnlineVOs.get(getNegociacaoRecebimentoVO().getUnidadeEnsino().getCodigo()));
			}
			listaAuxiliar = null;
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparListaContaReceber() {
		getListaConsultaContaReceber().clear();
	}

	public void limparDadosAlunoTerceiro() {
		setMatriculaTerceiroVO(null);
		limparDadosPessoaTerceiro();
	}

	public void limparDadosPessoaTerceiro() {
		setFuncionarioTerceiroVO(null);
		setPessoaTerceiroVO(null);
	}

	public void limparDadosParceiroTerceiro() {
		setParceiroTerceiroVO(null);
	}

	public void limparDadosFornecedorTerceiro() {
		setFornecedorTerceiroVO(null);
	}

	public void adicionarContaReceber(ContaReceberVO obj) throws Exception {
		if(!obj.getNivelMontarDados().equals(NivelMontarDados.TODOS)) {
			getFacadeFactory().getContaReceberFacade().carregarDados(obj, NivelMontarDados.TODOS, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getNegociacaoRecebimentoVO().getUnidadeEnsino().getCodigo()), getUsuarioLogado());
		}
		for (ContaReceberNegociacaoRecebimentoVO obj2 : getNegociacaoRecebimentoVO().getContaReceberNegociacaoRecebimentoVOs()) {
			if (obj.getCodigo().equals(obj2.getContaReceber().getCodigo())) {
				return;
			}
		}
		obj.getMatriculaAluno().setPlanoFinanceiroAluno(getFacadeFactory().getPlanoFinanceiroAlunoFacade().consultarPorMatriculaMatriculaUnico(obj.getMatriculaAluno().getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
		if (obj.getTipoDescontoLancadoRecebimento().equals("")) {
			obj.setTipoDescontoLancadoRecebimento("PO");
		}
		obj.setRealizandoRecebimento(true);
		Date dataVencimentoOriginal = null;
		if (getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getNegociacaoRecebimentoVO().getUnidadeEnsino().getCodigo()).getVencimentoParcelaDiaUtil()) {
			dataVencimentoOriginal = obj.getDataVencimento();
		}
		verificarDataVencimentoUtilizarDiaUtil(obj);
		getContaReceberNegociacaoRecebimentoVO().setValorTotal(obj.getCalcularValorFinal(getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getNegociacaoRecebimentoVO().getUnidadeEnsino().getCodigo()), getUsuarioLogado()));
		getContaReceberNegociacaoRecebimentoVO().setContaReceber(obj);
		adicionarContaReceberNegociacaoRecebimento();
		removerContaReceberListaConsulta(obj.getCodigo());
		calcularTotal();
		if (getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getNegociacaoRecebimentoVO().getUnidadeEnsino().getCodigo()).getVencimentoParcelaDiaUtil()) {
			obj.setDataVencimento(dataVencimentoOriginal);
			atualizarDataVencimentoAposSerSubstituidaPorDataUtil(getNegociacaoRecebimentoVO().getContaReceberNegociacaoRecebimentoVOs(), obj, dataVencimentoOriginal);
		}

	}

	public void adicionarTodasContaReceber() {
		try {
			Iterator i = getListaConsultaContaReceber().iterator();

			while (i.hasNext()) {
				ContaReceberVO obj = (ContaReceberVO) i.next();
				if (!negociacaoRecebimentoVO.isNovoObj().booleanValue()) {
					throw new ConsistirException("Não é possível alterar uma negociação recebimento.");
				}
				getFacadeFactory().getContaReceberFacade().carregarDados(obj, NivelMontarDados.TODOS, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getNegociacaoRecebimentoVO().getUnidadeEnsino().getCodigo()), getUsuarioLogado());
				if (!Uteis.isAtributoPreenchido(obj.getProcessamentoIntegracaoFinanceiraDetalheVO().getCodigo())) {
					obj.getMatriculaAluno().setPlanoFinanceiroAluno(getFacadeFactory().getPlanoFinanceiroAlunoFacade().consultarPorMatriculaMatriculaUnico(obj.getMatriculaAluno().getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
					Date dataVencimentoOriginal = null;
					if (getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getNegociacaoRecebimentoVO().getUnidadeEnsino().getCodigo()).getVencimentoParcelaDiaUtil()) {
						dataVencimentoOriginal = obj.getDataVencimento();
					}
					verificarDataVencimentoUtilizarDiaUtil(obj);
					getContaReceberNegociacaoRecebimentoVO().setContaReceber(obj);
					getContaReceberNegociacaoRecebimentoVO().setValorTotal(obj.getCalcularValorFinal(getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getNegociacaoRecebimentoVO().getUnidadeEnsino().getCodigo()), getUsuarioLogado()));
					if (getReceberContaReceberTerceiro()) {
						getContaReceberNegociacaoRecebimentoVO().setContaReceberTerceiro(Boolean.TRUE);
					}
					adicionarContaReceberNegociacaoRecebimento();
					calcularTotal();
					if (getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getNegociacaoRecebimentoVO().getUnidadeEnsino().getCodigo()).getVencimentoParcelaDiaUtil()) {
						obj.setDataVencimento(dataVencimentoOriginal);
						atualizarDataVencimentoAposSerSubstituidaPorDataUtil(getNegociacaoRecebimentoVO().getContaReceberNegociacaoRecebimentoVOs(), obj, dataVencimentoOriginal);
					}
				}
			}
			getListaConsultaContaReceber().clear();
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Foi definido pelo Edgar que seria adicionadas apenas 50 contas dos alunos vinculados ao Parceiro por questão de performance
	 */
	public void adicionarCinquentaContaReceberParceiro() {
		try {
			int index = 1;
			Iterator i = getListaConsultaContaReceber().iterator();
			while (i.hasNext()) {
				if (getNegociacaoRecebimentoVO().getContaReceberNegociacaoRecebimentoVOs().size() >= 500) {
					break;
				}
				ContaReceberVO obj = (ContaReceberVO) i.next();
				if (!negociacaoRecebimentoVO.isNovoObj().booleanValue()) {
					throw new ConsistirException("Não é possível alterar uma negociação recebimento.");
				}
				getFacadeFactory().getContaReceberFacade().carregarDados(obj, NivelMontarDados.TODOS, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getNegociacaoRecebimentoVO().getUnidadeEnsino().getCodigo()), getUsuarioLogado());
				obj.getMatriculaAluno().setPlanoFinanceiroAluno(getFacadeFactory().getPlanoFinanceiroAlunoFacade().consultarPorMatriculaMatriculaUnico(obj.getMatriculaAluno().getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
				getContaReceberNegociacaoRecebimentoVO().setContaReceber(obj);
				getContaReceberNegociacaoRecebimentoVO().setValorTotal(obj.getCalcularValorFinal(getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getNegociacaoRecebimentoVO().getUnidadeEnsino().getCodigo()), getUsuarioLogado()));
				if (getReceberContaReceberTerceiro()) {
					getContaReceberNegociacaoRecebimentoVO().setContaReceberTerceiro(Boolean.TRUE);
				}
				adicionarContaReceberNegociacaoRecebimento();
				index++;
			}
			calcularTotal();
			getListaConsultaContaReceber().clear();
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void verificarTipoParaAdicionarContas() {
		if (!getReceberContaReceberTerceiro()) {
			if (!getNegociacaoRecebimentoVO().getTipoParceiro()) {
				adicionarTodasContaReceber();
			} else {
				setQuantidadeContaReceber(getListaConsultaContaReceber().size());
				setMensagemAvisoAdicionarApenasCinquentaContaReceberParceiro("Existe(m) " + getQuantidadeContaReceber()
						+ " "
						+ UteisJSF.internacionalizar("prt_NegociacaoRecebimento_AvisoAdicionarTodasContasReceber"));
			}
		} else {
			if (!this.getTipoParceiroTerceiro()) {
				adicionarTodasContaReceber();
			} else {
				setQuantidadeContaReceber(getListaConsultaContaReceber().size());
				setMensagemAvisoAdicionarApenasCinquentaContaReceberParceiro("Existe(m) " + getQuantidadeContaReceber()
						+ " "
						+ UteisJSF.internacionalizar("prt_NegociacaoRecebimento_AvisoAdicionarTodasContasReceber"));
			}
		}
	}

	public String getAbrirModalAvisoAdicionarTodaContaReceberParceiro() {
		if (getNegociacaoRecebimentoVO().getTipoParceiro() || this.getTipoParceiroTerceiro()) {
			return "RichFaces.$('panelAvisoAdicionarTodasContasReceber').show()";
		}
		return "";
	}

	public void removerContaReceberListaConsulta(Integer contaReceber) {
		int index = 0;
		Integer codContaAgrupada = 0;
		Iterator i = getListaConsultaContaReceber().iterator();
		while (i.hasNext()) {
			ContaReceberVO obj = (ContaReceberVO) i.next();
			if (obj.getCodigo().intValue() == contaReceber) {
				getListaConsultaContaReceber().remove(index);
				return;
			}
			index++;
		}
	}

	public Boolean getIsMostrarModalJustificativaDesconto() {
		if (mostrarModalJustificativaDesconto == null) {
			mostrarModalJustificativaDesconto = false;
		}
		return mostrarModalJustificativaDesconto;
	}

	public void setMostrarModalJustificativaDesconto(Boolean mostrarModalJustificativaDesconto) {
		this.mostrarModalJustificativaDesconto = mostrarModalJustificativaDesconto;
	}

	public String getMostrarModalPanelJustificativaDesconto() {
		if (getIsMostrarModalJustificativaDesconto()) {
			return "RichFaces.$('panelDescontoRecebimento').hide(),RichFaces.$('panelJustificativaDesconto').show()";
		} else {
			return "";
		}
	}

	public void verificarFecharModalJustificativaDesconto() {
		calcularTotal();
		setMostrarModalJustificativaDesconto(false);
	}

	public void verificarAbrirModalJustificativaDesconto() {
		try {
			ContaReceberNegociacaoRecebimentoVO obj = (ContaReceberNegociacaoRecebimentoVO) context()
					.getExternalContext().getRequestMap().get("contaReceberNegociacaoRecebimentoItens");
			setContaReceberVO(obj.getContaReceber());
			if (obj.getContaReceber().getValorDescontoLancadoRecebimento() > 0.0) {
				setMostrarModalJustificativaDesconto(true);
				obj.getContaReceber().setValorAnteriorDescontoLancadoControle(
						obj.getContaReceber().getValorDescontoLancadoRecebimento());
			} else {
				if (obj.getContaReceber().getValorDescontoLancadoRecebimento() < 0.0) {
					obj.getContaReceber().setValorDescontoLancadoRecebimento(0.0);
				}
				if (obj.getContaReceber().getValorAnteriorDescontoLancadoControle() == null) {
					obj.getContaReceber().setValorAnteriorDescontoLancadoControle(
							obj.getContaReceber().getValorDescontoLancadoRecebimento());
					calcularTotal();
				} else {
					if (!obj.getContaReceber().getValorAnteriorDescontoLancadoControle()
							.equals(obj.getContaReceber().getValorDescontoLancadoRecebimento())) {
						obj.getContaReceber().setValorAnteriorDescontoLancadoControle(
								obj.getContaReceber().getValorDescontoLancadoRecebimento());
						calcularTotal();
					}
				}
			}
			setMensagemDetalhada("msg_dados_editar", "");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void verificarAbrirModalJustificativaDescontoUsuarioBloqueadoPermissaoDesconto() {
		try {
			ContaReceberNegociacaoRecebimentoVO obj = getContaReceberNegociacaoRecebimentoVO();
			setContaReceberVO(obj.getContaReceber());
			if (obj.getContaReceber().getValorDescontoLancadoRecebimento() > 0.0) {
				setMostrarModalJustificativaDesconto(true);
				obj.getContaReceber().setValorAnteriorDescontoLancadoControle(
						obj.getContaReceber().getValorDescontoLancadoRecebimento());
			} else {
				if (obj.getContaReceber().getValorDescontoLancadoRecebimento() < 0.0) {
					obj.getContaReceber().setValorDescontoLancadoRecebimento(0.0);
				}
				if (obj.getContaReceber().getValorAnteriorDescontoLancadoControle() == null) {
					obj.getContaReceber().setValorAnteriorDescontoLancadoControle(
							obj.getContaReceber().getValorDescontoLancadoRecebimento());
					calcularTotal();
				} else {
					if (!obj.getContaReceber().getValorAnteriorDescontoLancadoControle()
							.equals(obj.getContaReceber().getValorDescontoLancadoRecebimento())) {
						obj.getContaReceber().setValorAnteriorDescontoLancadoControle(
								obj.getContaReceber().getValorDescontoLancadoRecebimento());
						calcularTotal();
					}
				}
			}
			setMensagemDetalhada("msg_dados_editar", "");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void calcularTotal() {
		// ConfiguracaoFinanceiroVO conf;
		try {
			// conf =
			// getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_TODOS,
			// getUsuarioLogado(), null);
			getNegociacaoRecebimentoVO().calcularTotal(getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getNegociacaoRecebimentoVO().getUnidadeEnsino().getCodigo()), getUsuarioLogado());
			setMensagemDetalhada("msg_dados_editar", "");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void realizarLimpezaListaRecebimento() {
		setMensagemDetalhada("");
		getNegociacaoRecebimentoVO().setValorTotalRecebimento(0.0);
		getNegociacaoRecebimentoVO()
				.setFormaPagamentoNegociacaoRecebimentoVOs(new ArrayList<FormaPagamentoNegociacaoRecebimentoVO>(0));
		try {
			getNegociacaoRecebimentoVO().setContaCorrenteCaixa(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(negociacaoRecebimentoVO.getContaCorrenteCaixa().getCodigo(), false,  Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		if (!getNegociacaoRecebimentoVO().getContaReceberNegociacaoRecebimentoVOs().isEmpty() && negociacaoRecebimentoVO.getContaCorrenteCaixa().getCodigo() > 0) {
			if (verificaTipoOrigemPermitidoContaCaixa()) {
				negociacaoRecebimentoVO.getContaCorrenteCaixa().setCodigo(0);
			}
		}
		
	}

	/*
	 * Método responsável por adicionar um novo objeto da classe <code>FormaPagamentoNegociacaoRecebimento</code> para o objeto <code>negociacaoRecebimentoVO</code> da classe <code>NegociacaoRecebimento</code>
	 */
	public void setarNumeroFim() {
		if (!getChequeVO().getNumero().equals("")) {
			getChequeVO().setNumeroFinal(getChequeVO().getNumero());
		}
	}

	public void preencherListaChequesDevolvidoParaReutilizar() {
		try {
			setListaChequesDevolvidoParaReutilizar(new ArrayList<ChequeVO>(0));
			Integer codigoSacado = getNegociacaoRecebimentoVO().getTipoFornecedor()
					? getNegociacaoRecebimentoVO().getFornecedor().getCodigo()
					: getNegociacaoRecebimentoVO().getTipoParceiro()
							? getNegociacaoRecebimentoVO().getParceiroVO().getCodigo()
							: getNegociacaoRecebimentoVO().getPessoa().getCodigo();
			setListaChequesDevolvidoParaReutilizar(
					getFacadeFactory().getChequeFacade().consultarPorCodigoSacadoTipoPessoaESituacao(codigoSacado,
							getNegociacaoRecebimentoVO().getTipoPessoa(), "DS", false,
							Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			Iterator<ChequeVO> i = getListaChequesDevolvidoParaReutilizar().iterator();
			while (i.hasNext()) {
				ChequeVO chequeDevolvido = (ChequeVO) i.next();
				for (ChequeVO cheque : getNegociacaoRecebimentoVO().getChequeVOs()) {
					if (cheque.getCodigo().equals(chequeDevolvido.getCodigo())) {
						i.remove();
					}
				}
			}
			if (getListaChequesDevolvidoParaReutilizar().isEmpty()) {
				throw new Exception("Não existe cheques devolvido para serem adicionados.");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void adicionarFormaPagamentoNegociacaoRecebimentoChequeDevolvido() {
		try {
			Iterator<ChequeVO> i = getListaChequesDevolvidoParaReutilizar().iterator();
			while (i.hasNext()) {
				ChequeVO cheque = (ChequeVO) i.next();
				if (cheque.getSelecionado()) {
					setChequeVO(new ChequeVO());
					try {
						Integer.parseInt(cheque.getNumero());
					} catch (Exception e) {
						throw new Exception("O número do cheque não deve conter letras.");
					}
					cheque.setNumeroFinal(cheque.getNumero());
					setChequeVO(cheque);
					adicionarFormaPagamentoNegociacaoRecebimentoCheque();
					i.remove();
				}
			}
			consultarFormaPagamento();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void adicionarFormaPagamentoNegociacaoRecebimentoCheque() {
		try {
			if (!getChequeVO().getNumero().equals("") && !getChequeVO().getNumeroFinal().equals("")) {
				int valor;
				try {
					valor = Integer.parseInt(getChequeVO().getNumeroFinal())
							- Integer.parseInt(getChequeVO().getNumero());
				} catch (Exception e) {
					throw new ConsistirException("O número do cheque não deve conter letras.");
				}
				if (valor > 50) {
					throw new ConsistirException("O intervalo entre os cheques não pode ser superior a 50 cheques.");
				}
				valor++;
				if (valor > 0) {
					String zeroEsquerda = Uteis.obterZeroEsquerda(getChequeVO().getNumero());
					int nrCheque;
					try {
						nrCheque = Integer.parseInt(getChequeVO().getNumero());
					} catch (Exception e) {
						throw new ConsistirException("O número do cheque não deve conter letras.");
					}
					Date dataPrevisao = getChequeVO().getDataPrevisao();
					Double valorCheque = getChequeVO().getValor();
					while (valor > 0) {
						adicionarFormaPagamentoNegociacaoRecebimento();
						nrCheque++;
						dataPrevisao = Uteis.obterDataAvancadaPorMes(dataPrevisao, 1);
						getChequeVO().setNumero(zeroEsquerda + String.valueOf(nrCheque));
						getChequeVO().setDataPrevisao(dataPrevisao);
						getChequeVO().setValor(valorCheque);
						valor--;
					}
					getChequeVO().setNumero("");
					getChequeVO().setValor(new Double(0));
				} else {
					throw new ConsistirException("Número Final do cheque deve ser maior que o número inicial.");
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void adicionarFormaPagamentoNegociacaoRecebimentoPorFormaPagamento() {
		try {					
			if (getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamento().getTipo().equals(TipoFormaPagamento.CARTAO_DE_CREDITO.getValor())){
				verificarContasRecbimentoOnline();
				if(!getPermiteRecebimentoCartaoCreditoOnline() &&
						getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNumeroReciboTransacao().isEmpty() 
						&& isCampoNumeroReciboObrigatorio()) {
					throw new ConsistirException("O campo NÚMERO AUTORIZAÇÃO deve ser informado.");
				}
				if (getConfiguracaoRecebimentoCartaoOnlineVO().getUsarConfiguracaoVisaoAdministrativa() 
						&& getUsuarioLogado().getIsApresentarVisaoAdministrativa()
						&& getFormaPagamentoNegociacaoRecebimentoVO().getConfiguracaoFinanceiroCartaoVO().getPermitiRecebimentoCartaoOnline()) {
					getFacadeFactory().getNegociacaoRecebimentoFacade().validarDadosRecebimentoCartaoCreditoPorFormaPagamentoNegociacaoRecebimento(getNegociacaoRecebimentoVO(), getFormaPagamentoNegociacaoRecebimentoVO(), getUsuarioLogado());
				}
				if(getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getTipoFinanciamentoEnum().equals(TipoFinanciamentoEnum.OPERADORA)) {				
					getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoFacade().preencherFormaPagamentoNegociacaoRecebimentoCartaoCredito(getFormaPagamentoNegociacaoRecebimentoVO(),
							getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO(),
							getConfiguracaoFinanceiroCartaoVO(), getNegociacaoRecebimentoVO().getData(),
							getFormaPagamentoNegociacaoRecebimentoVO().getQtdeParcelasCartaoCredito(),
							getFormaPagamentoNegociacaoRecebimentoVO().getQtdeParcelasCartaoCredito(),
							getFormaPagamentoNegociacaoRecebimentoVO().getValorRecebimento(), getUsuarioLogado());
					getNegociacaoRecebimentoVO().adicionarObjFormaPagamentoNegociacaoRecebimentoVOs(getFormaPagamentoNegociacaoRecebimentoVO());					
				} else if (getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getTipoFinanciamentoEnum().equals(TipoFinanciamentoEnum.INSTITUICAO)) {
					getFacadeFactory().getNegociacaoRecebimentoFacade().realizarGeracaoFormaPagamentoNegociacaoRecebimentoCartaoCreditoFinanciamentoInstituicao(getFormaPagamentoNegociacaoRecebimentoVO(), getNegociacaoRecebimentoVO(), getUsuarioLogado());
				}
				setFormaPagamentoNegociacaoRecebimentoVO(new FormaPagamentoNegociacaoRecebimentoVO());
				setConfiguracaoFinanceiroCartaoVO(new ConfiguracaoFinanceiroCartaoVO());
			} else if ((getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamento().getTipo().equals(TipoFormaPagamento.CARTAO_DE_CREDITO.getValor()))
					&& (getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getTipoFinanciamentoEnum().equals(TipoFinanciamentoEnum.INSTITUICAO))) {			
					realizarMontagemCartaoCredito();
					getFacadeFactory().getNegociacaoRecebimentoFacade().realizarGeracaoFormaPagamentoNegociacaoRecebimentoCartaoCreditoFinanciamentoInstituicao(getFormaPagamentoNegociacaoRecebimentoVO(), getNegociacaoRecebimentoVO(),getUsuarioLogado());
					setFormaPagamentoNegociacaoRecebimentoVO(new FormaPagamentoNegociacaoRecebimentoVO());
					setConfiguracaoFinanceiroCartaoVO(new ConfiguracaoFinanceiroCartaoVO());
					getFacadeFactory().getNegociacaoRecebimentoFacade().calcularValorTrocoDeAcordoFormaPagamento(getNegociacaoRecebimentoVO());			
			} else {
				adicionarFormaPagamentoNegociacaoRecebimento();
			}
			getFormaPagamentoNegociacaoRecebimentoVO().setValorRecebimento(getNegociacaoRecebimentoVO().getResiduo());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/*public void preencherFormaPagamentoNegociacaoRecebimentoCartaoCredito() throws Exception {
		getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoFacade()
				.preencherFormaPagamentoNegociacaoRecebimentoCartaoCredito(getFormaPagamentoNegociacaoRecebimentoVO(),
						getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO(),
						getConfiguracaoFinanceiroCartaoVO(), getNegociacaoRecebimentoVO().getData(),
						getFormaPagamentoNegociacaoRecebimentoVO().getQtdeParcelasCartaoCredito(),
						getFormaPagamentoNegociacaoRecebimentoVO().getQtdeParcelasCartaoCredito(),
						getFormaPagamentoNegociacaoRecebimentoVO().getValorRecebimento(), getUsuarioLogado());
		adicionarFormaPagamentoNegociacaoRecebimento();
		// Double valorRecebimento =
		// getFormaPagamentoNegociacaoRecebimentoVO().getValorRecebimento();
		// int qtdeParcelasCartaoCredito =
		// getFormaPagamentoNegociacaoRecebimentoVO().getQtdeParcelasCartaoCredito();
		// Date dataAnterior = new Date();
		// FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO
		// formaPagamentoNegociacaoRecebimentoCartaoCreditoVO =
		// getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO();
		// for (int nrParcela = 1; nrParcela <= qtdeParcelasCartaoCredito; nrParcela++)
		// {
		// realizarMontagemCartaoCredito();
		// getFormaPagamentoNegociacaoRecebimentoVO().setContaCorrenteOperadoraCartaoVO(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(getFormaPagamentoNegociacaoRecebimentoVO().getContaCorrenteOperadoraCartaoVO().getCodigo(),
		// false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
		// getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoFacade().preencherFormaPagamentoNegociacaoRecebimentoCartaoCredito(getFormaPagamentoNegociacaoRecebimentoVO(),
		// formaPagamentoNegociacaoRecebimentoCartaoCreditoVO,
		// getConfiguracaoFinanceiroCartaoVO(), dataAnterior, nrParcela,
		// qtdeParcelasCartaoCredito, valorRecebimento, getUsuarioLogado());
		// dataAnterior =
		// getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getDataVencimento();
		// adicionarFormaPagamentoNegociacaoRecebimento();
		// }
	}*/

	public void adicionarFormaPagamentoNegociacaoRecebimento() throws Exception {
		if (!negociacaoRecebimentoVO.isNovoObj().booleanValue()) {
			throw new ConsistirException("Não é possível alterar uma negociação recebimento.");
		}
		if (getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamento().getTipo().equals(TipoFormaPagamento.CHEQUE.getValor()) && getChequeVO().getCodigo().equals(0)) {
			getFacadeFactory().getChequeFacade().validarUnicidadeCheque(getChequeVO(), getUsuarioLogado());
		}
		if (!getNegociacaoRecebimentoVO().getCodigo().equals(0)) {
			formaPagamentoNegociacaoRecebimentoVO.setNegociacaoRecebimento(getNegociacaoRecebimentoVO().getCodigo());
		}
		if ((getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamento().getTipo().equals(TipoFormaPagamento.DINHEIRO.getValor()))
				|| (getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamento().getTipo().equals(TipoFormaPagamento.CHEQUE.getValor()))
				|| (getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamento().getTipo().equals(TipoFormaPagamento.ISENCAO.getValor()))
				|| (getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamento().getTipo().equals(TipoFormaPagamento.PERMUTA.getValor()))) {
			if (getNegociacaoRecebimentoVO().getContaCorrenteCaixa().getCodigo() == 0) {
				throw new ConsistirException("O campo CONTA CORRENTE CAIXA (Negociação Recebimento) deve ser informado.");
			}
			if (getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamento().getTipo().equals(TipoFormaPagamento.ISENCAO.getValor())
					&& Uteis.isAtributoPreenchido(getFormaPagamentoNegociacaoRecebimentoVO().getValorRecebimento())) {
				getFormaPagamentoNegociacaoRecebimentoVO().setFormaPagamento(new FormaPagamentoVO());
				throw new ConsistirException(UteisJSF.internacionalizar("msg_NegociacaoRecebimento_recebimentoIsencaoDiferenteDeZero"));
			}
			getFormaPagamentoNegociacaoRecebimentoVO().setOperadoraCartaoVO(getConfiguracaoFinanceiroCartaoVO().getOperadoraCartaoVO());
			if (!getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamento().getTipo().equals(TipoFormaPagamento.PERMUTA.getValor())) {
				getFormaPagamentoNegociacaoRecebimentoVO().setContaCorrente(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(getNegociacaoRecebimentoVO().getContaCorrenteCaixa().getCodigo(),false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
			}
		} else {
			if (Uteis.isAtributoPreenchido(getFormaPagamentoNegociacaoRecebimentoVO().getContaCorrente().getCodigo())) {
				getFormaPagamentoNegociacaoRecebimentoVO().setContaCorrente(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(getFormaPagamentoNegociacaoRecebimentoVO().getContaCorrente().getCodigo(), false,Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
			}
			if (getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamento().getTipo().equals(TipoFormaPagamento.CARTAO_DE_DEBITO.getValor())){
				if(!getPermiteRecebimentoCartaoCreditoOnline() &&
						getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNumeroReciboTransacao().isEmpty() 
						&& isCampoNumeroReciboObrigatorio()) {
					throw new ConsistirException("O campo NÚMERO AUTORIZAÇÃO deve ser informado.");
				}
				if (!Uteis.isAtributoPreenchido(getFormaPagamentoNegociacaoRecebimentoVO().getOperadoraCartaoVO())) {
					throw new ConsistirException("O campo OPERADORA deve ser informado.");
				}
			}
		}
		if (getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamento().getTipo().equals(TipoFormaPagamento.CHEQUE.getValor())) {
			getChequeVO().getUnidadeEnsino().setCodigo(getNegociacaoRecebimentoVO().getUnidadeEnsino().getCodigo());
			getChequeVO().setContaCorrente(getNegociacaoRecebimentoVO().getContaCorrenteCaixa());
			getChequeVO().setChequeProprio(false);
			ChequeVO.validarDados(getChequeVO());
			getFormaPagamentoNegociacaoRecebimentoVO().setCheque(getChequeVO());
			getFormaPagamentoNegociacaoRecebimentoVO().setValorRecebimento(getChequeVO().getValor());
		}
		getNegociacaoRecebimentoVO().adicionarObjFormaPagamentoNegociacaoRecebimentoVOs(getFormaPagamentoNegociacaoRecebimentoVO());
		FormaPagamentoVO formaPagamentoVO = new FormaPagamentoVO();
		formaPagamentoVO = getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamento();
		setFormaPagamentoNegociacaoRecebimentoVO(new FormaPagamentoNegociacaoRecebimentoVO());
		adicionarFormaPagamentoCheque();
		getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamento().setCodigo(formaPagamentoVO.getCodigo());
		getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamento().setNome(formaPagamentoVO.getNome());
		getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamento().setTipo(formaPagamentoVO.getTipo());
		getFacadeFactory().getNegociacaoRecebimentoFacade().calcularValorTrocoDeAcordoFormaPagamento(getNegociacaoRecebimentoVO());
		setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
	}

	public void adicionarFormaPagamentoCheque() {
		String banco = getChequeVO().getBanco();
		String agencia = getChequeVO().getAgencia();
		String contaCorrente = getChequeVO().getNumeroContaCorrente();
		String nomeSacado = getChequeVO().getSacado();
		Boolean emitentePessoaJuridica = getChequeVO().getEmitentePessoaJuridica();
		String cpf = getChequeVO().getCpf();
		String cnpj = getChequeVO().getCnpj();
		setChequeVO(new ChequeVO());

		if (getNegociacaoRecebimentoVO().getTipoParceiro()) {
			getChequeVO().setParceiro(getNegociacaoRecebimentoVO().getParceiroVO());
			getChequeVO().setSacado(getNegociacaoRecebimentoVO().getParceiroVO().getNome());
		} else if (getNegociacaoRecebimentoVO().getTipoFornecedor()) {
			getChequeVO().setFornecedor(getNegociacaoRecebimentoVO().getFornecedor());
			getChequeVO().setSacado(getNegociacaoRecebimentoVO().getFornecedor().getNome());
		} else {
			getChequeVO().setPessoa(getNegociacaoRecebimentoVO().getPessoa());
			getChequeVO().setSacado(nomeSacado);
		}
		getChequeVO().setEmitentePessoaJuridica(emitentePessoaJuridica);
		getChequeVO().setCpf(cpf);
		getChequeVO().setCnpj(cnpj);
		getChequeVO().setBanco(banco);
		getChequeVO().setNumeroContaCorrente(contaCorrente);
		getChequeVO().setAgencia(agencia);
	}

	public void removerRecebimentoCheque() throws Exception {
		try {
			ChequeVO obj = (ChequeVO) context().getExternalContext().getRequestMap().get("chequeItens");
			getNegociacaoRecebimentoVO().removerFormaPagamentoCheque(obj);
			getNegociacaoRecebimentoVO().removerChequeNegociacao(obj);
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void apresentarBotaoExcluir() {
		try {
			if (getFacadeFactory().getNegociacaoRecebimentoFacade()
					.verificarSeChequeFoiMovimentadoParaOutraContaCaixa(getNegociacaoRecebimentoVO().getCodigo())) {
				setMostrarModalExplicacaoMovimentacaoCheque(Boolean.TRUE);
				setBotaoExcluir(Boolean.FALSE);
			} else {
				setBotaoExcluir(Boolean.TRUE);
			}
			verificarUsuarioPodeAlterarContaCaixaEstorno();
			getNegociacaoRecebimentoVO().setDesconsiderarConciliacaoBancaria(true);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String getApresentarMensagemCheque() {
		try {
			if (getFacadeFactory().getNegociacaoRecebimentoFacade()
					.verificarSeChequeFoiMovimentadoParaOutraContaCaixa(getNegociacaoRecebimentoVO().getCodigo())) {
				return "RichFaces.$('panelExplicacaoMovimentacaoCheque').show()";
			} else {
				setBotaoExcluir(Boolean.TRUE);
				return "RichFaces.$('panelMotivo').show()";
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}

	public String getLimparFormaPagamento() {
		getFormaPagamentoNegociacaoRecebimentoVO().setFormaPagamento(new FormaPagamentoVO());
		return "";
	}

	public String getApresentarModalPanel() throws Exception {
		return getFormaPagamentoNegociacaoRecebimentoVO().getTipoFormaPagamento();
	}

	/*
	 * Método responsável por disponibilizar dados de um objeto da classe <code>FormaPagamentoNegociacaoRecebimento</code> para edição pelo usuário.
	 */
	public void editarFormaPagamentoNegociacaoRecebimento() throws Exception {
		FormaPagamentoNegociacaoRecebimentoVO obj = (FormaPagamentoNegociacaoRecebimentoVO) context()
				.getExternalContext().getRequestMap().get("formaPagamentoNegociacaoRecebimentoVOItens");
		setFormaPagamentoNegociacaoRecebimentoVO(obj);
	}

	public void editarFormaPagamentoNegociacaoRecebimentoCartaoCredito() throws Exception {
		FormaPagamentoNegociacaoRecebimentoVO obj = (FormaPagamentoNegociacaoRecebimentoVO) context().getExternalContext().getRequestMap().get("formaPagamentoNegociacaoRecebimentoVOItens");
		setFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO(obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO());
		getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setFormaPagamentoNegociacaoRecebimentoVO(obj);
	}

	public void editarCheque() {
		try {
			FormaPagamentoNegociacaoRecebimentoVO obj = (FormaPagamentoNegociacaoRecebimentoVO) context()
					.getExternalContext().getRequestMap().get("formaPagamentoNegociacaoRecebimentoVOItens");
			if (obj.getCheque().getCodigo() > 0) {
				setChequeVO(getFacadeFactory().getChequeFacade().consultarPorChavePrimaria(obj.getCheque().getCodigo(),
						false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			} else {
				setChequeVO(obj.getCheque());
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/*
	 * Método responsável por remover um novo objeto da classe <code>FormaPagamentoNegociacaoRecebimento</code> do objeto <code>negociacaoRecebimentoVO</code> da classe <code>NegociacaoRecebimento</code>
	 */
	public void removerFormaPagamentoNegociacaoRecebimento() throws Exception {
		try {
			if (!negociacaoRecebimentoVO.isNovoObj().booleanValue()) {
				throw new ConsistirException("Não é possível alterar uma negociação recebimento.");
			}
			FormaPagamentoNegociacaoRecebimentoVO obj = (FormaPagamentoNegociacaoRecebimentoVO) context()
					.getExternalContext().getRequestMap().get("formaPagamentoNegociacaoRecebimentoVOItens");
			if (obj.getFormaPagamento().getTipo().equals("CH")) {
				getNegociacaoRecebimentoVO().removerChequeNegociacao(obj.getCheque());
			}
			getNegociacaoRecebimentoVO().excluirObjFormaPagamentoNegociacaoRecebimentoVOs(obj);
			getFacadeFactory().getNegociacaoRecebimentoFacade().calcularValorTrocoDeAcordoFormaPagamento(getNegociacaoRecebimentoVO());
			
			setMensagemID("msg_dados_excluidos");
		} catch (ConsistirException ce) {
			setConsistirExceptionMensagemDetalhada("msg_erro", ce, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarContaReceberNegociacaoRecebimento() throws Exception {
		if (!getNegociacaoRecebimentoVO().getCodigo().equals(0)) {
			contaReceberNegociacaoRecebimentoVO.setNegociacaoRecebimento(getNegociacaoRecebimentoVO().getCodigo());
		}
		if (getReceberContaReceberTerceiro()) {
			getContaReceberNegociacaoRecebimentoVO().setContaReceberTerceiro(Boolean.TRUE);
		}
//		if (getRecorrencia()) {
//			if (!getNegociacaoRecebimentoVO().getContaReceberNegociacaoRecebimentoVOs().isEmpty()) {
//				FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO = new FormaPagamentoNegociacaoRecebimentoVO();
//				for (FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO2 : getNegociacaoRecebimentoVO().getContaReceberNegociacaoRecebimentoVOs().get(0).getFormaPagamentoNegociacaoRecebimentoVOs()) {
//					formaPagamentoNegociacaoRecebimentoVO = formaPagamentoNegociacaoRecebimentoVO2.clone();
//					formaPagamentoNegociacaoRecebimentoVO.setValorRecebimento(0.0);
//					formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setValorParcela(formaPagamentoNegociacaoRecebimentoVO.getValorRecebimento());
//					formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setContaReceberNegociacaoRecebimentoVO(new ContaReceberNegociacaoRecebimentoVO());
//					formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setContaReceberNegociacaoRecebimentoVO(getContaReceberNegociacaoRecebimentoVO());
//					getContaReceberNegociacaoRecebimentoVO().getFormaPagamentoNegociacaoRecebimentoVOs().add(formaPagamentoNegociacaoRecebimentoVO);
//					getNegociacaoRecebimentoVO().getFormaPagamentoNegociacaoRecebimentoVOs().add(formaPagamentoNegociacaoRecebimentoVO);
//				}
//			}
//		}
		getNegociacaoRecebimentoVO().adicionarObjContaReceberNegociacaoRecebimentoVOs(getContaReceberNegociacaoRecebimentoVO());
		if(!isApresentarValorIndiceReajustePorAtraso()){
			setApresentarValorIndiceReajustePorAtraso(getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getNegociacaoRecebimentoVO().getUnidadeEnsino().getCodigo()).isAplicarIndireReajustePorAtrasoContaReceber());	
		}
		this.setContaReceberNegociacaoRecebimentoVO(new ContaReceberNegociacaoRecebimentoVO());
		calcularValorRecebido();
	}

	/*
	 * Método responsável por disponibilizar dados de um objeto da classe <code>ContaReceberNegociacaoRecebimento</code> para edição pelo usuário.
	 */
	public void editarContaReceberNegociacaoRecebimento() throws Exception {
		ContaReceberNegociacaoRecebimentoVO obj = (ContaReceberNegociacaoRecebimentoVO) context().getExternalContext()
				.getRequestMap().get("contaReceberNegociacaoRecebimentoItens");
		setContaReceberNegociacaoRecebimentoVO(obj);
	}

	/*
	 * Método responsável por remover um novo objeto da classe <code>ContaReceberNegociacaoRecebimento</code> do objeto <code>negociacaoRecebimentoVO</code> da classe <code>NegociacaoRecebimento</code>
	 */
	public void removerContaReceberNegociacaoRecebimento() throws Exception {
		try {
			if (!negociacaoRecebimentoVO.isNovoObj().booleanValue()) {
				throw new ConsistirException("Não é possível alterar uma negociação recebimento.");
			}
			ContaReceberNegociacaoRecebimentoVO obj = (ContaReceberNegociacaoRecebimentoVO) context()
					.getExternalContext().getRequestMap().get("contaReceberNegociacaoRecebimentoItens");
			if (obj.getContaReceber().getContaReceberAgrupada().intValue() > 0) {
				removerContaReceberNegociacaoRecebimento(obj.getContaReceber().getContaReceberAgrupada());
			} else {
				removerContaReceberNegociacaoRecebimento(obj);
			}
			setMensagemID("msg_dados_excluidos");
		} catch (ConsistirException ce) {
			setConsistirExceptionMensagemDetalhada("msg_erro", ce, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void removerContaReceberNegociacaoRecebimento(ContaReceberNegociacaoRecebimentoVO obj) throws Exception {
		if (!obj.getFormaPagamentoNegociacaoRecebimentoVOs().isEmpty()) {
			for (Iterator iterator = getNegociacaoRecebimentoVO().getFormaPagamentoNegociacaoRecebimentoVOs()
					.iterator(); iterator.hasNext();) {
				FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO = (FormaPagamentoNegociacaoRecebimentoVO) iterator
						.next();
				if (obj.getFormaPagamentoNegociacaoRecebimentoVOs().contains(formaPagamentoNegociacaoRecebimentoVO)) {
					iterator.remove();
				}
			}
		}
		getNegociacaoRecebimentoVO().excluirObjContaReceberNegociacaoRecebimentoVOs(obj.getContaReceber().getCodigo());
		calcularValorRecebido();
	}

	public void removerContaReceberNegociacaoRecebimento(Integer codContaReceberAgrupada) throws Exception {
		getNegociacaoRecebimentoVO()
				.excluirObjContaReceberNegociacaoRecebimentoVOsContaReceberAgrupada(codContaReceberAgrupada);
	}

	public void selecionarContaReceberNegociacaoRecebimento() throws Exception {
		ContaReceberNegociacaoRecebimentoVO obj = (ContaReceberNegociacaoRecebimentoVO) context().getExternalContext()
				.getRequestMap().get("contaReceberNegociacaoRecebimentoItens");
		setContaReceberVO(obj.getContaReceber());
		setMensagemID("msg_entre_dados");
	}

	/*
	 * Método responsável por disponibilizar dados de um objeto da classe <code>ContaPagarNegociacaoPagamento</code> para edição pelo usuário.
	 */
	public void visualizarLancamentoContabil() {
		try {
			LancamentoContabilVO obj = (LancamentoContabilVO) context().getExternalContext().getRequestMap()
					.get("lancamentoContabilItens");
			setLancamentoContabil(obj);
			setMensagemID("msg_dados_selecionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox relativo ao atributo <code>FormaPagamento</code>.
	 */
	public void montarListaSelectItemFormaPagamento(String prm) throws Exception {
		List<FormaPagamentoVO> resultadoConsulta = consultarFormaPagamentoPorNome(prm);
		Iterator<FormaPagamentoVO> i = resultadoConsulta.iterator();
		List<SelectItem> objs = new ArrayList<SelectItem>(0);
		objs.add(new SelectItem(0, ""));
		while (i.hasNext()) {
			FormaPagamentoVO obj = (FormaPagamentoVO) i.next();
			if (getBloqueiaDescontoNoRecebimento().booleanValue() && !obj.getTipo().equals("IS")
					&& !obj.getTipo().equals("PE")) {
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
			} else if (!getBloqueiaDescontoNoRecebimento().booleanValue()) {
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
			}
		}
		Uteis.liberarListaMemoria(resultadoConsulta);
		i = null;
		setListaSelectItemFormaPagamento(objs);
	}
	
	public void montarListaSelectItemFormaPagamento(ContaReceberVO contaReceber) throws Exception {
		List<FormaPagamentoVO> resultadoConsulta = null;
		if (Uteis.isAtributoPreenchido(contaReceber.getProcessamentoIntegracaoFinanceiraDetalheVO().getCodigo())) {
			resultadoConsulta = consultarFormaPagamentoOnline(getConfiguracaoRecebimentoCartaoOnlineVO().getPermitirCartao());
		} else {
			resultadoConsulta = consultarFormaPagamentoPorNome("");
		}
		Iterator<FormaPagamentoVO> i = resultadoConsulta.iterator();
		List<SelectItem> objs = new ArrayList<SelectItem>(0);
		objs.add(new SelectItem(0, ""));
		while (i.hasNext()) {
			FormaPagamentoVO obj = (FormaPagamentoVO) i.next();
			if (getBloqueiaDescontoNoRecebimento().booleanValue() && !obj.getTipo().equals("IS")
					&& !obj.getTipo().equals("PE")) {
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
			} else if (!getBloqueiaDescontoNoRecebimento().booleanValue()) {
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
			}
		}
		Uteis.liberarListaMemoria(resultadoConsulta);
		i = null;
		setListaSelectItemFormaPagamento(objs);
	}

	public void montarListaSelectItemFormaPagamentoIsencaoPermuta(String prm) throws Exception {
		List resultadoConsulta = consultarFormaPagamentoPorNome(prm);
		Iterator i = resultadoConsulta.iterator();
		List objs = new ArrayList(0);
		objs.add(new SelectItem(0, ""));
		while (i.hasNext()) {
			FormaPagamentoVO obj = (FormaPagamentoVO) i.next();
			if (obj.getTipo().equals("IS") || obj.getTipo().equals("PE")) {
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
			}
		}
		Uteis.liberarListaMemoria(resultadoConsulta);
		i = null;
		setListaSelectItemFormaPagamentoIsencaoPermuta(objs);
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo <code>FormaPagamento</code>. Buscando todos os objetos correspondentes a entidade <code>FormaPagamento</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemFormaPagamento() {
		try {
			montarListaSelectItemFormaPagamento("");
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());
		}
	}

	public void montarListaSelectItemFormaPagamentoIsencaoPermuta() {
		try {
			montarListaSelectItemFormaPagamentoIsencaoPermuta("");
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());
		}
	}

	/**
	 * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nome</code> Este atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox correspondente
	 */
	public List<FormaPagamentoVO> consultarFormaPagamentoPorNome(String nomePrm) throws Exception {
		List<FormaPagamentoVO> lista = getFacadeFactory().getFormaPagamentoFacade().consultarPorNomeUsaNoRecebimento(nomePrm, true, false,
				Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
		return lista;
	}
	
	public List<FormaPagamentoVO> consultarFormaPagamentoOnline(PermitirCartaoEnum tipo) throws Exception {
		return getFacadeFactory().getFormaPagamentoFacade().consultarPorTipoCartaoOnline(tipo, true, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox relativo ao atributo <code>Caixa</code>.
	 */
	public void montarListaSelectItemCaixa(String prm) throws Exception {
		List resultadoConsulta = consultarContaCorrentePorNumero(true);
		Iterator i = resultadoConsulta.iterator();
		List objs = new ArrayList(0);
		objs.add(new SelectItem(0, ""));
		Boolean existe = false;
		while (i.hasNext()) {
			ContaCorrenteVO obj = (ContaCorrenteVO) i.next();
			if (obj.getCodigo().equals(getNegociacaoRecebimentoVO().getContaCorrenteCaixa().getCodigo())) {
				existe = true;
			}
			if (obj.getSituacao().equals("AT")) {
				if (Uteis.isAtributoPreenchido(obj.getNomeApresentacaoSistema())) {
					objs.add(new SelectItem(obj.getCodigo(), obj.getNomeApresentacaoSistema()));
				} else {
					objs.add(new SelectItem(obj.getCodigo(), obj.getDescricaoCompletaConta()));
				}
			}
		}
		if (!existe && getNegociacaoRecebimentoVO().getContaCorrenteCaixa().getCodigo() > 0) {
			objs.add(new SelectItem(getNegociacaoRecebimentoVO().getContaCorrenteCaixa().getCodigo(),
					getNegociacaoRecebimentoVO().getContaCorrenteCaixa().getDescricaoCompletaConta()));
		}
		Uteis.liberarListaMemoria(resultadoConsulta);
		i = null;
		setListaSelectItemCaixa(objs);
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo <code>Caixa</code>. Buscando todos os objetos correspondentes a entidade <code>ContaCorrente</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemCaixa() {
		try {
			montarListaSelectItemCaixa("");
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());
		}
	}

	/**
	 * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>numero</code> Este atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox correspondente
	 */
	public List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String prm) throws Exception {
		List<UnidadeEnsinoVO> lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(prm, 0, false,
				Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		return lista;
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo <code>Agencia</code>. Buscando todos os objetos correspondentes a entidade <code>Agencia</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemUnidadeEnsino() {
		try {
			if (getUnidadeEnsinoLogado().getCodigo() != null && getUnidadeEnsinoLogado().getCodigo().intValue() != 0) {
				// setListaSelectItemUnidadeEnsino(new ArrayList<SelectItem>());
				getListaSelectItemUnidadeEnsino().clear();
				getListaSelectItemUnidadeEnsino()
						.add(new SelectItem(getUnidadeEnsinoLogado().getCodigo(), getUnidadeEnsinoLogado().getNome()));
				getNegociacaoRecebimentoVO().getUnidadeEnsino().setCodigo(getUnidadeEnsinoLogado().getCodigo());
				montarListaSelectItemCaixa();
			} else {
				List<UnidadeEnsinoVO> unidadeEnsino = consultarUnidadeEnsinoPorNome("");
				List<SelectItem> lista = UtilSelectItem.getListaSelectItem(unidadeEnsino, "codigo", "nome");
				setListaSelectItemUnidadeEnsino(lista);
			}
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());
		}
	}

	public List<ContaCorrenteVO> consultarContaCorrentePorNumero(Boolean contaCaixa) throws Exception {
		List<ContaCorrenteVO> lista = new ArrayList<ContaCorrenteVO>(0);
		Integer unidadeEnsino = getNegociacaoRecebimentoVO().getUnidadeEnsino().getCodigo();
		if (contaCaixa) {
			if (getPermitePagarReceberContasEmCaixaUnidadeDiferenteDaConta()) {
				unidadeEnsino = 0;
			}
			if (getNegociacaoRecebimentoVO() != null) {
				Boolean usuarioTemContaCaixa = false;
				if (getNegociacaoRecebimentoVO().getCodigo().equals(0)) {
					usuarioTemContaCaixa = getFacadeFactory().getContaCorrenteFacade()
							.consultarSeUsuarioTemContaCaixaVinculadoAEle(getUsuarioLogado().getPessoa().getCodigo());
				}
				if (usuarioTemContaCaixa) {
					if (getNegociacaoRecebimentoVO().getCodigo() != 0) {
						lista = getFacadeFactory().getContaCorrenteFacade().consultarPorFuncionarioResponsavel(
								getUsuarioLogado().getPessoa().getCodigo(), unidadeEnsino, Uteis.NIVELMONTARDADOS_TODOS,
								getUsuarioLogado());
					} else {
						lista = getFacadeFactory().getContaCorrenteFacade()
								.consultarPorFuncionarioResponsavelDataAberturaFluxoCaixaSituacao(contaCaixa,
										getUsuarioLogado().getPessoa().getCodigo(), unidadeEnsino, new Date(), "A",
										Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
					}
				} else {
					if (getNegociacaoRecebimentoVO().getCodigo() != 0) {
						lista = getFacadeFactory().getContaCorrenteFacade().consultarPorContaCaixa(contaCaixa,
								unidadeEnsino, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
					} else {
						lista = getFacadeFactory().getContaCorrenteFacade()
								.consultarPorContaCaixaDataAberturaFluxoCaixaSituacao(contaCaixa, unidadeEnsino,
										new Date(), "A", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
					}
				}
			}
		} else {
			lista = getFacadeFactory().getContaCorrenteFacade().consultarPorContaCaixa(contaCaixa, unidadeEnsino, false,
					Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());

		}
		return lista;
	}

	public void montarListaSelectItemRelacionadasUnidadeEnsino() {
		montarListaSelectItemCaixa();
		montarListaSelectItemContaCorrente();
		getNegociacaoRecebimentoVO().getContaReceberNegociacaoRecebimentoVOs().clear();
		getNegociacaoRecebimentoVO().getFormaPagamentoNegociacaoRecebimentoVOs().clear();
		getNegociacaoRecebimentoVO().getChequeVOs().clear();
		if (!getPermitePagarReceberContasEmCaixaUnidadeDiferenteDaConta()) {
			getNegociacaoRecebimentoVO().setPessoa(new PessoaVO());
			getNegociacaoRecebimentoVO().setMatricula("");
			montarListaSelectItemImpressora();
		}
	}

	public void montarListaSelectItemContaCorrente(String prm) throws Exception {
		List<ContaCorrenteVO> resultadoConsulta = consultarContaCorrentePorNumero(false);
		Iterator i = resultadoConsulta.iterator();
		List objs = new ArrayList(0);
		objs.add(new SelectItem(0, ""));
		while (i.hasNext()) {
			ContaCorrenteVO obj = (ContaCorrenteVO) i.next();
			if (Uteis.isAtributoPreenchido(obj.getNomeApresentacaoSistema())) {
				objs.add(new SelectItem(obj.getCodigo(), obj.getNomeApresentacaoSistema()));
			} else {
				objs.add(new SelectItem(obj.getCodigo(), obj.getBancoAgenciaContaCorrente()));
			}
		}
		Uteis.liberarListaMemoria(resultadoConsulta);
		i = null;
		setListaSelectItemContaCorrente(objs);
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo <code>Agencia</code>. Buscando todos os objetos correspondentes a entidade <code>Agencia</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemContaCorrente() {
		try {
			montarListaSelectItemContaCorrente("");
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());
		}
	}

	public void inicializarListasSelectItemTodosComboBox() {
		montarListaSelectItemUnidadeEnsino();
		montarListaSelectItemFormaPagamento();
		montarListaSelectItemFormaPagamentoIsencaoPermuta();
		if (!getNegociacaoRecebimentoVO().isNovoObj()) {
			montarListaSelectItemCaixa();
			montarListaSelectItemContaCorrente();
		}

	}

	// TODO Alberto 07/12/10 corrigido para não gravar duas negociações
	// recebimento com mesma contareceber
	public void carregarDadosContaRecber(NegociacaoRecebimentoVO obj) throws Exception {
		for (ContaReceberNegociacaoRecebimentoVO crNegRecb : obj.getContaReceberNegociacaoRecebimentoVOs()) {
			getFacadeFactory().getContaReceberFacade().carregarDados(crNegRecb.getContaReceber(),getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getNegociacaoRecebimentoVO().getUnidadeEnsino().getCodigo()), getUsuarioLogado());		
		}
	}

	public void selecionarFornecedor() {
		FornecedorVO obj = (FornecedorVO) context().getExternalContext().getRequestMap().get("fornecedorItens");
		this.getNegociacaoRecebimentoVO().setFornecedor(obj);
	}

	public void selecionarFornecedorTerceiro() {
		FornecedorVO obj = (FornecedorVO) context().getExternalContext().getRequestMap().get("fornecedorItens");
		this.setFornecedorTerceiroVO(obj);
	}

	private List<SelectItem> tipoConsultaComboFornecedor;

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

	public void consultarFornecedor() {
		try {
			super.consultar();
			List objs = new ArrayList(0);
			if (getCampoConsultaFornecedor().equals("codigo")) {
				if (getValorConsultaFornecedor().equals("")) {
					setValorConsultaFornecedor("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaFornecedor());
				objs = getFacadeFactory().getFornecedorFacade().consultarPorCodigo(new Integer(valorInt), "AT", false,
						Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFornecedor().equals("nome")) {
				objs = getFacadeFactory().getFornecedorFacade().consultarPorNome(getValorConsultaFornecedor(), "AT",
						false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFornecedor().equals("razaoSocial")) {
				objs = getFacadeFactory().getFornecedorFacade().consultarPorRazaoSocial(getValorConsultaFornecedor(),
						"AT", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFornecedor().equals("RG")) {
				objs = getFacadeFactory().getFornecedorFacade().consultarPorRG(getValorConsultaFornecedor(), "AT",
						false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFornecedor().equals("CPF")) {
				objs = getFacadeFactory().getFornecedorFacade().consultarPorCPF(getValorConsultaFornecedor(), "AT",
						false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFornecedor().equals("CNPJ")) {
				objs = getFacadeFactory().getFornecedorFacade().consultarPorCNPJ(getValorConsultaFornecedor(), "AT",
						false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}

			setListaConsultaFornecedor(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaFornecedor(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	// TODO Alberto 07/12/10 corrigido para não gravar duas negociações
	// recebimento com mesma contareceber

	/**
	 * Rotina responsável por atribui um javascript com o método de mascara para campos do tipo Data, CPF, CNPJ, etc.
	 */
	public String getMascaraConsulta() {
		if (getControleConsulta().getCampoConsulta().equals("data")) {
			return "return mascara(this.form,'form:valorConsulta','99/99/9999',event);";
		}
		if (getControleConsulta().getCampoConsulta().equals("cpf")) {
			return "return mascara(this.form,'form:valorConsulta','999.999.999-99',event);";
		}
		return "";
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List getTipoConsultaCombo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("pessoa", "Pessoa/Parceiro/Fornecedor"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("cpf", "CPF"));
		itens.add(new SelectItem("codigo", "Número"));
		itens.add(new SelectItem("responsavel", "Responsável"));
		itens.add(new SelectItem("nossoNumero", "Nosso Número"));
		itens.add(new SelectItem("contaCorrenteCaixa", "Conta Corrente Caixa"));
		return itens;
	}

	public String irConsultarContaReceber() {
		return Uteis.getCaminhoRedirecionamentoNavegacao("contaReceberCons.xhtml");
	}

	public boolean getIsApresentarCamposData() {
		return !(getControleConsulta().getCampoConsulta().equals("nossoNumero"));
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
	 */
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		montarListaSelectItemUnidadeEnsino();
		setListaConsulta(new ArrayList(0));
		getControleConsulta().setDataIni(Uteis.getDataPrimeiroDiaMes(new Date()));
		getControleConsulta().setDataFim(Uteis.getDataUltimoDiaMes(new Date()));
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("negociacaoRecebimentoCons.xhtml");
	}

	public String getMascaraCampo() {
		if (getControleConsulta().getCampoConsulta().equals("cpf")) {
			return "return mascara(this.form,'form:valorConsulta','999.999.999-99',event)";
		}
		return "";
	}

	public void imprimirComprovanteRecebimento() {
		try {
			comprovanteRecebimentoRelControle = null;
			comprovanteRecebimentoRelControle = (ComprovanteRecebimentoRelControle) context().getExternalContext()
					.getSessionMap().get(ComprovanteRecebimentoRelControle.class.getSimpleName());
			if (comprovanteRecebimentoRelControle == null) {
				comprovanteRecebimentoRelControle = new ComprovanteRecebimentoRelControle();
				context().getExternalContext().getSessionMap().put(
						ComprovanteRecebimentoRelControle.class.getSimpleName(), comprovanteRecebimentoRelControle);
			}
			if (!getNegociacaoRecebimentoVO().getCodigo().equals(0)) {
				getComprovanteRecebimentoRelControle()
						.setNegociacaoRecebimentoVO((NegociacaoRecebimentoVO) getNegociacaoRecebimentoVO().clone());
				if (getNegociacaoRecebimentoVO().getPagamentoComDCC()) {
					getComprovanteRecebimentoRelControle().imprimirPDFDCC();
				} else {
					if (verificarRecebimentoCartaoFeitoVisaoAluno()) {
						getComprovanteRecebimentoRelControle().imprimirPDFRecebimentoCartaoCredito();
					} else {
						getComprovanteRecebimentoRelControle().imprimirPDF();
					}
				}
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void imprimirComprovanteRecebimentoBemateck() {
		try {
			if (getIsHabilitarImpressaoApplet()) {
				setImpressoraVO(null);
			}
			setTextoComprovante(getFacadeFactory().getNegociacaoRecebimentoFacade().realizarGeracaoTextoComprovante(
					getNegociacaoRecebimentoVO(),
					getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(
							getNegociacaoRecebimentoVO().getUnidadeEnsino().getCodigo()),
					getImpressoraVO(), getUsuarioLogado()));
			if (getIsHabilitarImpressaoPoll()) {
				registrarImpressoraPadraoUsuarioFinanceiro();
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String getTextoComprovante() {
		if (textoComprovante == null) {
			textoComprovante = "";
		}
		return textoComprovante;
	}

	public void setTextoComprovante(String textoComprovante) {
		this.textoComprovante = textoComprovante;
	}

	/**
	 * Operação que libera todos os recursos (atributos, listas, objetos) do backing bean. Garantindo uma melhor atuação do Garbage Coletor do Java. A mesma é automaticamente quando realiza o logout.
	 */
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
		negociacaoRecebimentoVO = null;
		contaReceberNegociacaoRecebimentoVO = null;
		formaPagamentoNegociacaoRecebimentoVO = null;
	}

	public FormaPagamentoNegociacaoRecebimentoVO getFormaPagamentoNegociacaoRecebimentoVO() {
		if (formaPagamentoNegociacaoRecebimentoVO == null) {
			formaPagamentoNegociacaoRecebimentoVO = new FormaPagamentoNegociacaoRecebimentoVO();
		}
		return formaPagamentoNegociacaoRecebimentoVO;
	}

	public void setFormaPagamentoNegociacaoRecebimentoVO(
			FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO) {
		this.formaPagamentoNegociacaoRecebimentoVO = formaPagamentoNegociacaoRecebimentoVO;
	}

	public ContaReceberNegociacaoRecebimentoVO getContaReceberNegociacaoRecebimentoVO() {
		if (contaReceberNegociacaoRecebimentoVO == null) {
			contaReceberNegociacaoRecebimentoVO = new ContaReceberNegociacaoRecebimentoVO();
		}
		return contaReceberNegociacaoRecebimentoVO;
	}

	public void setContaReceberNegociacaoRecebimentoVO(
			ContaReceberNegociacaoRecebimentoVO contaReceberNegociacaoRecebimentoVO) {
		this.contaReceberNegociacaoRecebimentoVO = contaReceberNegociacaoRecebimentoVO;
	}

	public NegociacaoRecebimentoVO getNegociacaoRecebimentoVO() {
		if (negociacaoRecebimentoVO == null) {
			negociacaoRecebimentoVO = new NegociacaoRecebimentoVO();
		}
		return negociacaoRecebimentoVO;
	}

	public void setNegociacaoRecebimentoVO(NegociacaoRecebimentoVO negociacaoRecebimentoVO) {
		this.negociacaoRecebimentoVO = negociacaoRecebimentoVO;
	}

	public String getCampoConsultaAluno() {
		if (campoConsultaAluno == null) {
			campoConsultaAluno = "";
		}
		return campoConsultaAluno;
	}

	public void setCampoConsultaAluno(String campoConsultaAluno) {
		this.campoConsultaAluno = campoConsultaAluno;
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

	public String getCampoConsultaContaReceber() {
		if (campoConsultaContaReceber == null) {
			campoConsultaContaReceber = "";
		}
		return campoConsultaContaReceber;
	}

	public void setCampoConsultaContaReceber(String campoConsultaContaReceber) {
		this.campoConsultaContaReceber = campoConsultaContaReceber;
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

	public String getCampoConsultaRequisitante() {
		if (campoConsultaRequisitante == null) {
			campoConsultaRequisitante = "";
		}
		return campoConsultaRequisitante;
	}

	public void setCampoConsultaRequisitante(String campoConsultaRequisitante) {
		this.campoConsultaRequisitante = campoConsultaRequisitante;
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

	public List getListaConsultaAluno() {
		if (listaConsultaAluno == null) {
			listaConsultaAluno = new ArrayList(0);
		}
		return listaConsultaAluno;
	}

	public void setListaConsultaAluno(List listaConsultaAluno) {
		this.listaConsultaAluno = listaConsultaAluno;
	}

	public List getListaConsultaCandidato() {
		if (listaConsultaCandidato == null) {
			listaConsultaCandidato = new ArrayList(0);
		}
		return listaConsultaCandidato;
	}

	public void setListaConsultaCandidato(List listaConsultaCandidato) {
		this.listaConsultaCandidato = listaConsultaCandidato;
	}

	public List getListaConsultaContaReceber() {
		if (listaConsultaContaReceber == null) {
			listaConsultaContaReceber = new ArrayList(0);
		}
		return listaConsultaContaReceber;
	}

	public void setListaConsultaContaReceber(List listaConsultaContaReceber) {
		this.listaConsultaContaReceber = listaConsultaContaReceber;
	}

	public List getListaConsultaFuncionario() {
		if (listaConsultaFuncionario == null) {
			listaConsultaFuncionario = new ArrayList(0);
		}
		return listaConsultaFuncionario;
	}

	public void setListaConsultaFuncionario(List listaConsultaFuncionario) {
		this.listaConsultaFuncionario = listaConsultaFuncionario;
	}

	public List getListaConsultaRequisitante() {
		if (listaConsultaRequisitante == null) {
			listaConsultaRequisitante = new ArrayList(0);
		}
		return listaConsultaRequisitante;
	}

	public void setListaConsultaRequisitante(List listaConsultaRequisitante) {
		this.listaConsultaRequisitante = listaConsultaRequisitante;
	}

	public List getListaSelectItemCaixa() {
		if (listaSelectItemCaixa == null) {
			listaSelectItemCaixa = new ArrayList(0);
		}
		return listaSelectItemCaixa;
	}

	public void setListaSelectItemCaixa(List listaSelectItemCaixa) {
		this.listaSelectItemCaixa = listaSelectItemCaixa;
	}

	public List getListaSelectItemContaCorrente() {
		if (listaSelectItemContaCorrente == null) {
			listaSelectItemContaCorrente = new ArrayList(0);
		}
		return listaSelectItemContaCorrente;
	}

	public void setListaSelectItemContaCorrente(List listaSelectItemContaCorrente) {
		this.listaSelectItemContaCorrente = listaSelectItemContaCorrente;
	}

	public List getListaSelectItemFormaPagamento() {
		if (listaSelectItemFormaPagamento == null) {
			listaSelectItemFormaPagamento = new ArrayList(0);
		}
		return listaSelectItemFormaPagamento;
	}

	public void setListaSelectItemFormaPagamento(List listaSelectItemFormaPagamento) {
		this.listaSelectItemFormaPagamento = listaSelectItemFormaPagamento;
	}

	public String getValorConsultaAluno() {
		if (valorConsultaAluno == null) {
			valorConsultaAluno = "";
		}
		return valorConsultaAluno;
	}

	public void setValorConsultaAluno(String valorConsultaAluno) {
		this.valorConsultaAluno = valorConsultaAluno;
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

	public String getValorConsultaContaReceber() {
		if (valorConsultaContaReceber == null) {
			valorConsultaContaReceber = "";
		}
		return valorConsultaContaReceber;
	}

	public void setValorConsultaContaReceber(String valorConsultaContaReceber) {
		this.valorConsultaContaReceber = valorConsultaContaReceber;
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

	public String getValorConsultaRequisitante() {
		if (valorConsultaRequisitante == null) {
			valorConsultaRequisitante = "";
		}
		return valorConsultaRequisitante;
	}

	public void setValorConsultaRequisitante(String valorConsultaRequisitante) {
		this.valorConsultaRequisitante = valorConsultaRequisitante;
	}

	public Boolean getBotaoExcluir() {
		return botaoExcluir;
	}

	public void setBotaoExcluir(Boolean botaoExcluir) {
		this.botaoExcluir = botaoExcluir;
	}

	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public Date getDataInicioConsultar() {
		return dataInicioConsultar;
	}

	public void setDataInicioConsultar(Date dataInicioConsultar) {
		this.dataInicioConsultar = dataInicioConsultar;
	}

	/**
	 * @return the valorConsultaUnidadeEnsino
	 */
	public int getValorConsultaUnidadeEnsino() {
		return valorConsultaUnidadeEnsino;
	}

	/**
	 * @param valorConsultaUnidadeEnsino
	 *            the valorConsultaUnidadeEnsino to set
	 */
	public void setValorConsultaUnidadeEnsino(int valorConsultaUnidadeEnsino) {
		this.valorConsultaUnidadeEnsino = valorConsultaUnidadeEnsino;
	}

	/**
	 * @return the listaConsultaParceiro
	 */
	public List getListaConsultaParceiro() {
		if (listaConsultaParceiro == null) {
			listaConsultaParceiro = new ArrayList(0);
		}
		return listaConsultaParceiro;
	}

	/**
	 * @param listaConsultaParceiro
	 *            the listaConsultaParceiro to set
	 */
	public void setListaConsultaParceiro(List listaConsultaParceiro) {
		this.listaConsultaParceiro = listaConsultaParceiro;
	}

	/**
	 * @return the valorConsultaParceiro
	 */
	public String getValorConsultaParceiro() {
		if (valorConsultaParceiro == null) {
			valorConsultaParceiro = "";
		}
		return valorConsultaParceiro;
	}

	/**
	 * @param valorConsultaParceiro
	 *            the valorConsultaParceiro to set
	 */
	public void setValorConsultaParceiro(String valorConsultaParceiro) {
		this.valorConsultaParceiro = valorConsultaParceiro;
	}

	/**
	 * @return the campoConsultaParceiro
	 */
	public String getCampoConsultaParceiro() {
		if (campoConsultaParceiro == null) {
			campoConsultaParceiro = "";
		}
		return campoConsultaParceiro;
	}

	/**
	 * @param campoConsultaParceiro
	 *            the campoConsultaParceiro to set
	 */
	public void setCampoConsultaParceiro(String campoConsultaParceiro) {
		this.campoConsultaParceiro = campoConsultaParceiro;
	}

	/**
	 * @return the contaReceberVO
	 */
	public ContaReceberVO getContaReceberVO() {
		if (contaReceberVO == null) {
			contaReceberVO = new ContaReceberVO();
		}
		return contaReceberVO;
	}

	/**
	 * @param contaReceberVO
	 *            the contaReceberVO to set
	 */
	public void setContaReceberVO(ContaReceberVO contaReceberVO) {
		this.contaReceberVO = contaReceberVO;
	}

	public List<SelectItem> getListaSelectItemConfiguracaoFinanceiroCartao() {
		if (listaSelectItemConfiguracaoFinanceiroCartao == null) {
			listaSelectItemConfiguracaoFinanceiroCartao = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemConfiguracaoFinanceiroCartao;
	}

	public void setListaSelectItemConfiguracaoFinanceiroCartao(
			List<SelectItem> listaSelectItemConfiguracaoFinanceiroCartao) {
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

	public void setMostrarModalExplicacaoMovimentacaoCheque(Boolean mostrarModalExplicacaoMovimentacaoCheque) {
		this.mostrarModalExplicacaoMovimentacaoCheque = mostrarModalExplicacaoMovimentacaoCheque;
	}

	public Boolean getMostrarModalExplicacaoMovimentacaoCheque() {
		if (mostrarModalExplicacaoMovimentacaoCheque == null) {
			mostrarModalExplicacaoMovimentacaoCheque = Boolean.FALSE;
		}
		return mostrarModalExplicacaoMovimentacaoCheque;
	}

	public void recalcularDescontos() {
		try {
			for (ContaReceberNegociacaoRecebimentoVO contaReceberNegociacaoRecebimentoVO : getNegociacaoRecebimentoVO().getContaReceberNegociacaoRecebimentoVOs()) {
				verificarDataVencimentoUtilizarDiaUtil(contaReceberNegociacaoRecebimentoVO.getContaReceber());
				contaReceberNegociacaoRecebimentoVO.getContaReceber().getCalcularValorFinal(getNegociacaoRecebimentoVO().getData(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getNegociacaoRecebimentoVO().getUnidadeEnsino().getCodigo()), false, getNegociacaoRecebimentoVO().getData(), getUsuarioLogado());
			//	ContaReceber.montarListaDescontosAplicaveisContaReceber(contaReceberNegociacaoRecebimentoVO.getContaReceber(), getNegociacaoRecebimentoVO().getData(), contaReceberNegociacaoRecebimentoVO.getContaReceber().getUsaDescontoCompostoPlanoDesconto(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getNegociacaoRecebimentoVO().getUnidadeEnsino().getCodigo()), getUsuarioLogado(), null);
			}
			calcularTotal();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void verificarDataVencimentoUtilizarDiaUtil(ContaReceberVO contaReceberVO) throws Exception {
		if (getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getNegociacaoRecebimentoVO().getUnidadeEnsino().getCodigo()).getVencimentoParcelaDiaUtil()) {
			contaReceberVO.getDataOriginalVencimento();
			contaReceberVO.setDataVencimentoDiaUtil(contaReceberVO.getDataVencimento());
			contaReceberVO.setDataVencimentoDiaUtil(getFacadeFactory().getContaReceberFacade().obterDataVerificandoDiaUtil(contaReceberVO.getDataVencimento(), contaReceberVO.getUnidadeEnsino().getCidade().getCodigo(), getUsuarioLogado()));
			contaReceberVO.setDataVencimento(contaReceberVO.getDataVencimentoDiaUtil());
		}
	}

	/**
	 * @return the autorizacaoRecebimentoRetroativo
	 */
	public Boolean getAutorizacaoRecebimentoRetroativo() {
		if (autorizacaoRecebimentoRetroativo == null) {
			autorizacaoRecebimentoRetroativo = Boolean.FALSE;
		}
		return autorizacaoRecebimentoRetroativo;
	}

	/**
	 * @param autorizacaoRecebimentoRetroativo
	 *            the autorizacaoRecebimentoRetroativo to set
	 */
	public void setAutorizacaoRecebimentoRetroativo(Boolean autorizacaoRecebimentoRetroativo) {
		this.autorizacaoRecebimentoRetroativo = autorizacaoRecebimentoRetroativo;
	}

	public Boolean getDesabilitarDataRecebimento() {
		if (!getNegociacaoRecebimentoVO().getEdicao() || !getAutorizacaoRecebimentoRetroativo()) {
			return true;
		} else {
			return false;
		}
	}

	public boolean getIsRecebimentoGravado() {
		return !getNegociacaoRecebimentoVO().getNovoObj();
	}

	public ComprovanteRecebimentoRelControle getComprovanteRecebimentoRelControle() {
		return comprovanteRecebimentoRelControle;
	}

	public void setComprovanteRecebimentoRelControle(
			ComprovanteRecebimentoRelControle comprovanteRecebimentoRelControle) {
		this.comprovanteRecebimentoRelControle = comprovanteRecebimentoRelControle;
	}

	public void selecionarResponsavelFinanceiro() {
		try {
			PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("responsavelFinanceiroItens");
			this.getNegociacaoRecebimentoVO().setPessoa(obj);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarResponsavelFinanceiroTerceiro() {
		try {
			PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("responsavelFinanceiroItens");
			this.setPessoaTerceiroVO(obj);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
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
				setListaConsultaResponsavelFinanceiro(getFacadeFactory().getPessoaFacade()
						.consultaRapidaPorNomeResponsavelFinanceiro(getValorConsultaResponsavelFinanceiro(),
								this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado()));
			}
			if (getCampoConsultaResponsavelFinanceiro().equals("nomeAluno")) {
				setListaConsultaResponsavelFinanceiro(getFacadeFactory().getPessoaFacade()
						.consultaRapidaPorNomeAlunoResponsavelFinanceiro(getValorConsultaResponsavelFinanceiro(),
								this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado()));
			}
			if (getCampoConsultaResponsavelFinanceiro().equals("CPF")) {
				setListaConsultaResponsavelFinanceiro(getFacadeFactory().getPessoaFacade()
						.consultaRapidaPorCpfResponsavelFinanceiro(getValorConsultaResponsavelFinanceiro(),
								this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado()));
			}

			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			setListaConsultaResponsavelFinanceiro(new ArrayList<PessoaVO>(0));
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

	public List getListaContaCorrenteVOs() {
		if (listaContaCorrenteVOs == null) {
			listaContaCorrenteVOs = new ArrayList(0);
		}
		return listaContaCorrenteVOs;
	}

	public void setListaContaCorrenteVOs(List listaContaCorrenteVOs) {
		this.listaContaCorrenteVOs = listaContaCorrenteVOs;
	}

	public ContaCorrenteVO getContaCaixaVO() {
		if (contaCaixaVO == null) {
			contaCaixaVO = new ContaCorrenteVO();
		}
		return contaCaixaVO;
	}

	public void setContaCaixaVO(ContaCorrenteVO contaCaixaVO) {
		this.contaCaixaVO = contaCaixaVO;
	}

	public Boolean getAlterarContaCaixa() {
		if (alterarContaCaixa == null) {
			alterarContaCaixa = Boolean.FALSE;
		}
		return alterarContaCaixa;
	}

	public void setAlterarContaCaixa(Boolean alterarContaCaixa) {
		this.alterarContaCaixa = alterarContaCaixa;
	}

	public Boolean getPermitirAlterarContaCaixaEstorno() {
		if (permitirAlterarContaCaixaEstorno == null) {
			permitirAlterarContaCaixaEstorno = Boolean.FALSE;
		}
		return permitirAlterarContaCaixaEstorno;
	}

	public void setPermitirAlterarContaCaixaEstorno(Boolean permitirAlterarContaCaixaEstorno) {
		this.permitirAlterarContaCaixaEstorno = permitirAlterarContaCaixaEstorno;
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

	public List getListaContaCorrenteResponsavelEstornoVOs() {
		if (listaContaCorrenteResponsavelEstornoVOs == null) {
			listaContaCorrenteResponsavelEstornoVOs = new ArrayList();
		}
		return listaContaCorrenteResponsavelEstornoVOs;
	}

	public void setListaContaCorrenteResponsavelEstornoVOs(List listaContaCorrenteResponsavelEstornoVOs) {
		this.listaContaCorrenteResponsavelEstornoVOs = listaContaCorrenteResponsavelEstornoVOs;
	}

	public Boolean getFuncionarioResponsavelCaixa() {
		if (funcionarioResponsavelCaixa == null) {
			funcionarioResponsavelCaixa = Boolean.TRUE;
		}
		return funcionarioResponsavelCaixa;
	}

	public void setFuncionarioResponsavelCaixa(Boolean funcionarioResponsavelCaixa) {
		this.funcionarioResponsavelCaixa = funcionarioResponsavelCaixa;
	}

	public String getTipoOrigem() {
		if (tipoOrigem == null) {
			tipoOrigem = getLoginControle().getPermissaoAcessoMenuVO().getPermitirApenasContasDaBiblioteca() ? "BIB"
					: "";
		}
		return tipoOrigem;
	}

	public void setTipoOrigem(String tipoOrigem) {
		this.tipoOrigem = tipoOrigem;
	}

	public Boolean getReceberContaReceberTerceiro() {
		if (receberContaReceberTerceiro == null) {
			receberContaReceberTerceiro = Boolean.FALSE;
		}
		return receberContaReceberTerceiro;
	}

	public void setReceberContaReceberTerceiro(Boolean receberContaReceberTerceiro) {
		this.receberContaReceberTerceiro = receberContaReceberTerceiro;
	}

	public String getTipoPessoaTerceiro() {
		if (tipoPessoaTerceiro == null) {
			tipoPessoaTerceiro = "AL";
		}
		return tipoPessoaTerceiro;
	}

	public void setTipoPessoaTerceiro(String tipoPessoaTerceiro) {
		this.tipoPessoaTerceiro = tipoPessoaTerceiro;
	}

	public Boolean getTipoAlunoTerceiro() {
		return getTipoPessoaTerceiro().equals("AL");
	}

	public Boolean getTipoCandidatoTerceiro() {
		return getTipoPessoaTerceiro().equals("CA");
	}

	public Boolean getTipoResponsavelFinanceiroTerceiro() {
		return getTipoPessoaTerceiro().equals("RF");
	}

	public Boolean getTipoRequerenteTerceiro() {
		return getTipoPessoaTerceiro().equals("RE");
	}

	public Boolean getTipoFuncionarioTerceiro() {
		return getTipoPessoaTerceiro().equals("FU");
	}

	public Boolean getTipoParceiroTerceiro() {
		return getTipoPessoaTerceiro().equals("PA");
	}

	public Boolean getTipoFornecedorTerceiro() {
		return getTipoPessoaTerceiro().equals("FO");
	}

	public PessoaVO getPessoaTerceiroVO() {
		if (pessoaTerceiroVO == null) {
			pessoaTerceiroVO = new PessoaVO();
		}
		return pessoaTerceiroVO;
	}

	public void setPessoaTerceiroVO(PessoaVO pessoaTerceiroVO) {
		this.pessoaTerceiroVO = pessoaTerceiroVO;
	}

	public FornecedorVO getFornecedorTerceiroVO() {
		if (fornecedorTerceiroVO == null) {
			fornecedorTerceiroVO = new FornecedorVO();
		}
		return fornecedorTerceiroVO;
	}

	public void setFornecedorTerceiroVO(FornecedorVO fornecedorTerceiroVO) {
		this.fornecedorTerceiroVO = fornecedorTerceiroVO;
	}

	public MatriculaVO getMatriculaTerceiroVO() {
		if (matriculaTerceiroVO == null) {
			matriculaTerceiroVO = new MatriculaVO();
		}
		return matriculaTerceiroVO;
	}

	public void setMatriculaTerceiroVO(MatriculaVO matriculaTerceiroVO) {
		this.matriculaTerceiroVO = matriculaTerceiroVO;
	}

	public FuncionarioVO getFuncionarioTerceiroVO() {
		if (funcionarioTerceiroVO == null) {
			funcionarioTerceiroVO = new FuncionarioVO();
		}
		return funcionarioTerceiroVO;
	}

	public void setFuncionarioTerceiroVO(FuncionarioVO funcionarioTerceiroVO) {
		this.funcionarioTerceiroVO = funcionarioTerceiroVO;
	}

	public ParceiroVO getParceiroTerceiroVO() {
		if (parceiroTerceiroVO == null) {
			parceiroTerceiroVO = new ParceiroVO();
		}
		return parceiroTerceiroVO;
	}

	public void setParceiroTerceiroVO(ParceiroVO parceiroTerceiroVO) {
		this.parceiroTerceiroVO = parceiroTerceiroVO;
	}

	public Boolean getPermitirRecebimentoTerceiro() {
		if (permitirRecebimentoTerceiro == null) {
			permitirRecebimentoTerceiro = Boolean.FALSE;
		}
		return permitirRecebimentoTerceiro;
	}

	public void setPermitirRecebimentoTerceiro(Boolean permitirRecebimentoTerceiro) {
		this.permitirRecebimentoTerceiro = permitirRecebimentoTerceiro;
	}

	public FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO() {
		if (formaPagamentoNegociacaoRecebimentoCartaoCreditoVO == null) {
			formaPagamentoNegociacaoRecebimentoCartaoCreditoVO = new FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO();
		}
		return formaPagamentoNegociacaoRecebimentoCartaoCreditoVO;
	}

	public void setFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO(
			FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO formaPagamentoNegociacaoRecebimentoCartaoCreditoVO) {
		this.formaPagamentoNegociacaoRecebimentoCartaoCreditoVO = formaPagamentoNegociacaoRecebimentoCartaoCreditoVO;
	}

	public Integer getQuantidadeContaReceber() {
		if (quantidadeContaReceber == null) {
			quantidadeContaReceber = 0;
		}
		return quantidadeContaReceber;
	}

	public void setQuantidadeContaReceber(Integer quantidadeContaReceber) {
		this.quantidadeContaReceber = quantidadeContaReceber;
	}

	public String getMensagemAvisoAdicionarApenasCinquentaContaReceberParceiro() {
		if (mensagemAvisoAdicionarApenasCinquentaContaReceberParceiro == null) {
			mensagemAvisoAdicionarApenasCinquentaContaReceberParceiro = "";
			;
		}
		return mensagemAvisoAdicionarApenasCinquentaContaReceberParceiro;
	}

	public void setMensagemAvisoAdicionarApenasCinquentaContaReceberParceiro(
			String mensagemAvisoAdicionarApenasCinquentaContaReceberParceiro) {
		this.mensagemAvisoAdicionarApenasCinquentaContaReceberParceiro = mensagemAvisoAdicionarApenasCinquentaContaReceberParceiro;
	}

	public List<ChequeVO> getListaChequesDevolvidoParaReutilizar() {
		if (listaChequesDevolvidoParaReutilizar == null) {
			listaChequesDevolvidoParaReutilizar = new ArrayList<ChequeVO>(0);
		}
		return listaChequesDevolvidoParaReutilizar;
	}

	public void setListaChequesDevolvidoParaReutilizar(List<ChequeVO> listaChequesDevolvidoParaReutilizar) {
		this.listaChequesDevolvidoParaReutilizar = listaChequesDevolvidoParaReutilizar;
	}

	public String getExibirPanelChequeDevolvido() {
		if (!getListaChequesDevolvidoParaReutilizar().isEmpty()) {
			return "RichFaces.$('panelChequeDevolvido').show()";
		}
		return "";
	}

	/**
	 * Responsável po executar a definição da configuração financeiro a ser utilizada no Recebimento. Primeiro é verificado se existe uma configuração financeiro vinculada a unidade ensino selecionada no recebimento, caso contrário, é utilizada a regra de definida no SuperControle, que verifica se existe uma unidade ensino logada, caso exista é utilizada a configuração financeira vinculada a tal unidade de ensino, senão é utilizada a configuração financeira padrão do sistema.
	 * 
	 * @author Wellington Rodrigues 25/02/2015
	 * @return ConfiguracaoFinanceiroVO
	 * @throws Exception
	 */
		
	private ConfiguracaoFinanceiroVO executarDefinicaoConfiguracaoFinanceiroUtilizar() throws Exception {
		ConfiguracaoFinanceiroVO configuracaoFinanceiro = getFacadeFactory().getConfiguracaoFinanceiroFacade()
				.consultarPorUnidadeEnsino(getNegociacaoRecebimentoVO().getUnidadeEnsino().getCodigo(),
						Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		if (!Uteis.isAtributoPreenchido(configuracaoFinanceiro)) {
			configuracaoFinanceiro = configuracaoFinanceiroUnidadeLogada();
		}
		return configuracaoFinanceiro;
	}

	/**
	 * @author Victor Hugo de Paula Costa 25/02/2015
	 */
	private List<SelectItem> listaSelectItemQuantidadeParcelas;
	private Boolean recorrencia;

	public List<SelectItem> getListaSelectItemQuantidadeParcelas() {
		if (listaSelectItemQuantidadeParcelas == null) {
			listaSelectItemQuantidadeParcelas = new ArrayList<SelectItem>();
		}
		return listaSelectItemQuantidadeParcelas;
	}

	public void setListaSelectItemQuantidadeParcelas(List<SelectItem> listaSelectItemQuantidadeParcelas) {
		this.listaSelectItemQuantidadeParcelas = listaSelectItemQuantidadeParcelas;
	}

	public void montarListaSelectItemQuantidadeParcelasConfiguracaoFinanceiroCartao() {
		Integer maiorParcela = 1;
		getListaSelectItemQuantidadeParcelas().clear();
		if (getConfiguracaoFinanceiroCartaoVO().getPermitiRecebimentoCartaoOnline()
				&& !getConfiguracaoRecebimentoCartaoOnlineVO().getCodigo().equals(0)) {
			maiorParcela = getConfiguracaoRecebimentoCartaoOnlineVO().getQtdeParcelasPermitida(getFormaPagamentoNegociacaoRecebimentoVO()
					.getValorRecebimento(), getNegociacaoRecebimentoVO().realizarCalculoMaiorDataVencimento(), getUsuarioLogado(), getNegociacaoRecebimentoVO().getListaTipoOrigemContaReceber());
		} else if (Uteis.isAtributoPreenchido(getConfiguracaoFinanceiroCartaoVO())) {
			maiorParcela = getConfiguracaoFinanceiroCartaoVO().getQuantidadeParcelasCartaoCredito();
		}
		Integer i;
		for (i = 1; i <= maiorParcela; i++) {
			getListaSelectItemQuantidadeParcelas().add(new SelectItem(i, i.toString()));
		}
		if (getFormaPagamentoNegociacaoRecebimentoVO().getQtdeParcelasCartaoCredito() > maiorParcela) {
			getFormaPagamentoNegociacaoRecebimentoVO().setQtdeParcelasCartaoCredito(maiorParcela);
		}
	}

	public void validarNumeroCartaoCredito() {
		ConsistirException ce = new ConsistirException();
		try {
			if (getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamento().getTipo()
					.equals(TipoFormaPagamento.CARTAO_DE_CREDITO.getValor())
					&& !getConfiguracaoRecebimentoCartaoOnlineVO().getCodigo().equals(0)
					&& getPermiteRecebimentoCartaoCreditoOnline()) {
				if (!getFacadeFactory().getGerenciamentoDeTransacaoCartaoDeCreditoFacade().validarNumeroCartaoCredito(
						getFormaPagamentoNegociacaoRecebimentoVO()
								.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNumeroCartao(),
						getConfiguracaoFinanceiroCartaoVO().getOperadoraCartaoVO().getNome())) {
					ce.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_NumeroCartaoCreditoInvalido"));
					throw ce;
				} else {
					setMensagemID("msg_NumeroCartaoCreditoValido", Uteis.SUCESSO);
				}
			}
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", ce, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

public void atribuirTipoFinanciamentoFormaPagamentoRecebimentoCartaoCredito() {
		try {
			if(getPermiteRecebimentoCartaoCreditoOnline()) {
				getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO()
				.setTipoFinanciamentoEnum(getConfiguracaoRecebimentoCartaoOnlineVO().getTipoFinanciamentoPermitido(getFormaPagamentoNegociacaoRecebimentoVO()
					.getValorRecebimento(), getNegociacaoRecebimentoVO().realizarCalculoMaiorDataVencimento(), getUsuarioLogado(), getNegociacaoRecebimentoVO().getListaTipoOrigemContaReceber()));
				//
				inicializarMensagemVazia();
			}	
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		
	}

//	public Boolean getRecorrencia() {
//		if (recorrencia == null) {
//			recorrencia = false;
//		}
//		return recorrencia;
//	}
//
//	public void setRecorrencia(Boolean recorrencia) {
//		this.recorrencia = recorrencia;
//	}

	public List getListaSelectItemFormaPagamentoIsencaoPermuta() {
		if (listaSelectItemFormaPagamentoIsencaoPermuta == null) {
			listaSelectItemFormaPagamentoIsencaoPermuta = new ArrayList(0);
		}
		return listaSelectItemFormaPagamentoIsencaoPermuta;
	}

	public void setListaSelectItemFormaPagamentoIsencaoPermuta(List listaSelectItemFormaPagamentoIsencaoPermuta) {
		this.listaSelectItemFormaPagamentoIsencaoPermuta = listaSelectItemFormaPagamentoIsencaoPermuta;
	}

	public Boolean getBloqueiaDescontoNoRecebimento() {
		if (bloqueiaDescontoNoRecebimento == null) {
			bloqueiaDescontoNoRecebimento = Boolean.FALSE;
		}
		return bloqueiaDescontoNoRecebimento;
	}

	public void setBloqueiaDescontoNoRecebimento(Boolean bloqueiaDescontoNoRecebimento) {
		this.bloqueiaDescontoNoRecebimento = bloqueiaDescontoNoRecebimento;
	}

	public UsuarioVO getUsuarioDesbloquearDescontoRecebimento() {
		if (usuarioDesbloquearDescontoRecebimento == null) {
			usuarioDesbloquearDescontoRecebimento = new UsuarioVO();
		}
		return usuarioDesbloquearDescontoRecebimento;
	}

	public void setUsuarioDesbloquearDescontoRecebimento(UsuarioVO usuarioDesbloquearDescontoRecebimento) {
		this.usuarioDesbloquearDescontoRecebimento = usuarioDesbloquearDescontoRecebimento;
	}

	public UsuarioVO getUsuarioDesbloquearFormaRecebimentoNoRecebimento() {
		if (usuarioDesbloquearFormaRecebimentoNoRecebimento == null) {
			usuarioDesbloquearFormaRecebimentoNoRecebimento = new UsuarioVO();
		}
		return usuarioDesbloquearFormaRecebimentoNoRecebimento;
	}

	public void setUsuarioDesbloquearFormaRecebimentoNoRecebimento(
			UsuarioVO usuarioDesbloquearFormaRecebimentoNoRecebimento) {
		this.usuarioDesbloquearFormaRecebimentoNoRecebimento = usuarioDesbloquearFormaRecebimentoNoRecebimento;
	}

	public String getTipoDescLancado() {
		return TipoDescontoAluno.getSimbolo(getTipoDescontoLancadoRecebimento());
	}

	public Double getValorDescontoLancadoRecebimento() {
		if (valorDescontoLancadoRecebimento == null) {
			valorDescontoLancadoRecebimento = 0.0;
		}
		return valorDescontoLancadoRecebimento;
	}

	public void setValorDescontoLancadoRecebimento(Double valorDescontoLancadoRecebimento) {
		this.valorDescontoLancadoRecebimento = valorDescontoLancadoRecebimento;
	}

	public String getTipoDescontoLancadoRecebimento() {
		if (tipoDescontoLancadoRecebimento == null) {
			tipoDescontoLancadoRecebimento = "PO";
		}
		return tipoDescontoLancadoRecebimento;
	}

	public void setTipoDescontoLancadoRecebimento(String tipoDescontoLancadoRecebimento) {
		this.tipoDescontoLancadoRecebimento = tipoDescontoLancadoRecebimento;
	}

	public Boolean getFecharModalAdicionarDescontoJustificativaTodasContasReceber() {
		if (fecharModalAdicionarDescontoJustificativaTodasContasReceber == null) {
			fecharModalAdicionarDescontoJustificativaTodasContasReceber = Boolean.FALSE;
		}
		return fecharModalAdicionarDescontoJustificativaTodasContasReceber;
	}

	public void setFecharModalAdicionarDescontoJustificativaTodasContasReceber(
			Boolean fecharModalAdicionarDescontoJustificativaTodasContasReceber) {
		this.fecharModalAdicionarDescontoJustificativaTodasContasReceber = fecharModalAdicionarDescontoJustificativaTodasContasReceber;
	}

	public String getJustifivativaDescontoListaContaReceber() {
		if (justifivativaDescontoListaContaReceber == null) {
			justifivativaDescontoListaContaReceber = "";
		}
		return justifivativaDescontoListaContaReceber;
	}

	public void setJustifivativaDescontoListaContaReceber(String justifivativaDescontoListaContaReceber) {
		this.justifivativaDescontoListaContaReceber = justifivativaDescontoListaContaReceber;
	}

	public String getTipoDescontoLancadoRecebimentoListaContaReceber() {
		if (tipoDescontoLancadoRecebimentoListaContaReceber == null) {
			tipoDescontoLancadoRecebimentoListaContaReceber = "PO";
		}
		return TipoDescontoAluno.getSimbolo(tipoDescontoLancadoRecebimentoListaContaReceber);
	}

	public void setTipoDescontoLancadoRecebimentoListaContaReceber(
			String tipoDescontoLancadoRecebimentoListaContaReceber) {
		this.tipoDescontoLancadoRecebimentoListaContaReceber = tipoDescontoLancadoRecebimentoListaContaReceber;
	}

	public Double getValorDescontoLancadoRecebimentoListaContaReceber() {
		if (valorDescontoLancadoRecebimentoListaContaReceber == null) {
			valorDescontoLancadoRecebimentoListaContaReceber = 0.0;
		}
		return valorDescontoLancadoRecebimentoListaContaReceber;
	}

	public void setValorDescontoLancadoRecebimentoListaContaReceber(
			Double valorDescontoLancadoRecebimentoListaContaReceber) {
		this.valorDescontoLancadoRecebimentoListaContaReceber = valorDescontoLancadoRecebimentoListaContaReceber;
	}

	public Boolean getFecharModalDesbloquearDescontoJustificativaTodasContasReceber() {
		if (fecharModalDesbloquearDescontoJustificativaTodasContasReceber == null) {
			fecharModalDesbloquearDescontoJustificativaTodasContasReceber = Boolean.FALSE;
		}
		return fecharModalDesbloquearDescontoJustificativaTodasContasReceber;
	}

	public void setFecharModalDesbloquearDescontoJustificativaTodasContasReceber(
			Boolean fecharModalDesbloquearDescontoJustificativaTodasContasReceber) {
		this.fecharModalDesbloquearDescontoJustificativaTodasContasReceber = fecharModalDesbloquearDescontoJustificativaTodasContasReceber;
	}

	public void atualizarDataVencimentoAposSerSubstituidaPorDataUtil(
			List<ContaReceberNegociacaoRecebimentoVO> listaContaReceberNegociacaoRecebimentoVOs, ContaReceberVO obj,
			Date dataVencimentoOriginal) {
		for (ContaReceberNegociacaoRecebimentoVO contaReceberNegociacaoRecebimentoVO : listaContaReceberNegociacaoRecebimentoVOs) {
			if (contaReceberNegociacaoRecebimentoVO.getContaReceber().getCodigo().equals(obj.getCodigo())) {
				contaReceberNegociacaoRecebimentoVO.getContaReceber().setDataVencimento(dataVencimentoOriginal);
				break;
			}
		}
	}

	/**
	 * @author Victor Hugo de Paula Costa 14/03/2016 15:42
	 */
	private ConfiguracaoRecebimentoCartaoOnlineVO configuracaoRecebimentoCartaoOnlineVO;

	public ConfiguracaoRecebimentoCartaoOnlineVO getConfiguracaoRecebimentoCartaoOnlineVO() {
		if (configuracaoRecebimentoCartaoOnlineVO == null) {
			configuracaoRecebimentoCartaoOnlineVO = new ConfiguracaoRecebimentoCartaoOnlineVO();
		}
		return configuracaoRecebimentoCartaoOnlineVO;
	}

	public void setConfiguracaoRecebimentoCartaoOnlineVO(ConfiguracaoRecebimentoCartaoOnlineVO configuracaoRecebimentoCartaoOnlineVO) {
		this.configuracaoRecebimentoCartaoOnlineVO = configuracaoRecebimentoCartaoOnlineVO;
	}

	public void consultarConfiguracaoCartaoPermiteRecebimentoOnline() throws Exception {
			setPermiteRecebimentoCartaoCreditoOnline(false);
			setConfiguracaoRecebimentoCartaoOnlineVO(new ConfiguracaoRecebimentoCartaoOnlineVO());
			if (getNegociacaoRecebimentoVO().getTipoAluno() && Uteis.isAtributoPreenchido(getMatriculaPeriodoVO())) {
				setConfiguracaoRecebimentoCartaoOnlineVO(
						getFacadeFactory().getConfiguracaoRecebimentoCartaoOnlineFacade()
								.consultarConfiguracaoRecebimentoCartaoOnlineDisponivel(
										getMatriculaPeriodoVO().getTurma().getCodigo(),
										getMatriculaPeriodoVO().getTurma().getCurso().getCodigo(),
										TipoNivelEducacional.getEnum(getMatriculaPeriodoVO().getMatriculaVO().getCurso()
												.getNivelEducacional()).getValor(),
										getNegociacaoRecebimentoVO().getUnidadeEnsino().getCodigo(),
										Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			} else {
				setConfiguracaoRecebimentoCartaoOnlineVO(
						getFacadeFactory().getConfiguracaoRecebimentoCartaoOnlineFacade()
								.consultarConfiguracaoRecebimentoCartaoOnlineDisponivel(0, 0, "",
										getNegociacaoRecebimentoVO().getUnidadeEnsino().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS,
										getUsuarioLogado()));
			}
	}

	public void verificarContasRecbimentoOnline() throws Exception {
		try {
		if(Uteis.isAtributoPreenchido(getConfiguracaoRecebimentoCartaoOnlineVO())) {
			setPermiteRecebimentoCartaoCreditoOnline(false);
			ConsistirException consistirException = new ConsistirException();
			getFacadeFactory().getConfiguracaoRecebimentoCartaoOnlineFacade().verificarContasRecebimentoOnline(getNegociacaoRecebimentoVO().getContaReceberNegociacaoRecebimentoVOs(), getConfiguracaoRecebimentoCartaoOnlineVO(), consistirException, true, false, false, false, false, false, getUsuarioLogado());
			if (!consistirException.getListaMensagemErro().isEmpty() && getConfiguracaoFinanceiroCartaoVO().getPermitiRecebimentoCartaoOnline()) {
				throw consistirException;
			} else {
				if (consistirException.getListaMensagemErro().isEmpty() &&
						getConfiguracaoFinanceiroCartaoVO().getPermitiRecebimentoCartaoOnline() &&
						(PermitirCartaoEnum.AMBOS.equals(getConfiguracaoRecebimentoCartaoOnlineVO().getPermitirCartao()) ||
						(PermitirCartaoEnum.DEBITO.equals(getConfiguracaoRecebimentoCartaoOnlineVO().getPermitirCartao()) &&
								getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamento().getTipo().equals("CD")) ||
						(PermitirCartaoEnum.CREDITO.equals(getConfiguracaoRecebimentoCartaoOnlineVO().getPermitirCartao()) &&
								getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamento().getTipo().equals("CA")))
						) {
					setPermiteRecebimentoCartaoCreditoOnline(true);
				}
			}
		}
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void atualizarDescontoJustificativaListaContaReceber(UsuarioVO usuario) {
		try {
			alterarTipoDescontoContaReceberSimboloValorPercentual();
			for (ContaReceberNegociacaoRecebimentoVO contaReceberNegociacaoRecebimentoVO : getNegociacaoRecebimentoVO()
					.getContaReceberNegociacaoRecebimentoVOs()) {
				contaReceberNegociacaoRecebimentoVO.getContaReceber()
						.setJustificativaDesconto(justifivativaDescontoListaContaReceber);
				contaReceberNegociacaoRecebimentoVO.getContaReceber()
						.setValorDescontoLancadoRecebimento(valorDescontoLancadoRecebimentoListaContaReceber);
				contaReceberNegociacaoRecebimentoVO.getContaReceber()
						.setTipoDescontoLancadoRecebimento(tipoDescontoLancadoRecebimentoListaContaReceber);
				if (bloqueiaDescontoNoRecebimento) {
					getContaReceberVO().setUsuarioDesbloqueouDescontoRecebimento(usuario);
					getContaReceberVO().setDataUsuarioDesbloqueouDescontoRecebimento(new Date());
					setUsuarioDesbloquearDescontoRecebimento(null);
				}
				if (contaReceberNegociacaoRecebimentoVO.getContaReceber().getValorDescontoLancadoRecebimento() > 0.0) {
					contaReceberNegociacaoRecebimentoVO.getContaReceber().setValorAnteriorDescontoLancadoControle(
							contaReceberNegociacaoRecebimentoVO.getContaReceber().getValorDescontoLancadoRecebimento());
				} else if (contaReceberNegociacaoRecebimentoVO.getContaReceber()
						.getValorAnteriorDescontoLancadoControle() == null) {
					contaReceberNegociacaoRecebimentoVO.getContaReceber().setValorAnteriorDescontoLancadoControle(
							contaReceberNegociacaoRecebimentoVO.getContaReceber().getValorDescontoLancadoRecebimento());
				}
			}
			calcularTotal();
			limparDadosModalAdicionarDescontoJustificativaListaContaReceber();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void verificarFecharModalJustificativaDescontoListaDesconto() {
		try {
			validarDadosModalAdicionarDescontoJustificativaListaContaReceber();
			atualizarDescontoJustificativaListaContaReceber(null);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String getIsFecharPanelAdicionarDescontoJustificativaTodasContasReceber() {
		if (getFecharModalAdicionarDescontoJustificativaTodasContasReceber()) {
			return "RichFaces.$('panelAdicionarDescontoJustificativaTodasContasReceber').hide(),RichFaces.$('panelDescontoJustificativaRecebimento').hide()";
		} else {
			return "";
		}
	}

	public void alterarSimboloTipoDescontoModalDescontoJustificativaListaContaReceber() {
		if (getTipoDescontoLancadoRecebimentoListaContaReceber().equals("%")) {
			setTipoDescontoLancadoRecebimentoListaContaReceber("R$");
		} else if (getTipoDescontoLancadoRecebimentoListaContaReceber().equals("R$")) {
			setTipoDescontoLancadoRecebimentoListaContaReceber("%");
		}
	}

	public void alterarTipoDescontoContaReceberSimboloValorPercentual() {
		if (getTipoDescontoLancadoRecebimentoListaContaReceber().equals("%")) {
			setTipoDescontoLancadoRecebimentoListaContaReceber("PO");
		} else if (getTipoDescontoLancadoRecebimentoListaContaReceber().equals("R$")) {
			setTipoDescontoLancadoRecebimentoListaContaReceber("VA");
		}
	}

	public void limparDadosModalAdicionarDescontoJustificativaListaContaReceber() {
		justifivativaDescontoListaContaReceber = null;
		valorDescontoLancadoRecebimentoListaContaReceber = null;
		tipoDescontoLancadoRecebimentoListaContaReceber = null;
	}

	public void validarDadosModalAdicionarDescontoJustificativaListaContaReceber() throws Exception {
		if (getValorDescontoLancadoRecebimentoListaContaReceber() < 0) {
			setFecharModalAdicionarDescontoJustificativaTodasContasReceber(Boolean.FALSE);
			throw new Exception(UteisJSF
					.internacionalizar("prt_NegociacaoRecebimento_AvisoAdicionarDescontoNegativoListaContareceber"));
		} else if (getTipoDescontoLancadoRecebimentoListaContaReceber().equals("%")
				&& getValorDescontoLancadoRecebimentoListaContaReceber() > 100.0) {
			setFecharModalAdicionarDescontoJustificativaTodasContasReceber(Boolean.FALSE);
			throw new Exception(UteisJSF.internacionalizar(
					"prt_NegociacaoRecebimento_AvisoAdicionarDescontoPorcentagemMaiorCemListaContareceber"));
		} else {
			setFecharModalAdicionarDescontoJustificativaTodasContasReceber(Boolean.TRUE);
		}
	}

	public void confirmaDescontoJustificativaNoRecebimentoUsuarioBloqueadoDesconto() {
		try {
			UsuarioVO usuarioVerif = getUsuarioDesbloquearDescontoRecebimento();
			usuarioVerif = ControleAcesso.verificarLoginUsuario(usuarioVerif.getUsername(), usuarioVerif.getSenha(),
					true, Uteis.NIVELMONTARDADOS_TODOS);
			boolean permitirInformarDesconto = Boolean.FALSE;
			try {
				ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico(
						"BloquearDescontoRecebimento", usuarioVerif);
				permitirInformarDesconto = Boolean.TRUE;
			} catch (Exception e) {
				permitirInformarDesconto = Boolean.FALSE;
			}
			if (permitirInformarDesconto) {
				throw new Exception(
						UteisJSF.internacionalizar("prt_NegociacaoRecebimento_AvisoUsuarioNaoPossuiPermissaoDesconto"));
			}
			validarDadosModalAdicionarDescontoJustificativaListaContaReceber();
			atualizarDescontoJustificativaListaContaReceber(usuarioVerif);
			setFecharModalDesbloquearDescontoJustificativaTodasContasReceber(true);
		} catch (Exception e) {
			setFecharModalDesbloquearDescontoJustificativaTodasContasReceber(false);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String getIsFecharPanelDesbloquearDescontoJustificativaTodasContasReceber() {
		if (getFecharModalDesbloquearDescontoJustificativaTodasContasReceber()) {
			return "RichFaces.$('panelDescontoJustificativaRecebimento').hide()";
		} else {
			return "";
		}
	}

	public void verificarSeBotaoExcluirPodeAparecer() {
		try {
			verificarPermissaoEstornarRecebimentoCartaoCreditoJaRecebido();
			for (FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO : getNegociacaoRecebimentoVO()
					.getFormaPagamentoNegociacaoRecebimentoVOs()) {
				if ((formaPagamentoNegociacaoRecebimentoVO.getFormaPagamento().getTipo()
						.equals(TipoFormaPagamento.CARTAO_DE_CREDITO.getValor())
						&& getNegociacaoRecebimentoVO().getPagamentoComDCC()
						&& !getPermiteEstornarRecebimentoCartaoCreditoJaRecebido())
						|| (formaPagamentoNegociacaoRecebimentoVO.getFormaPagamento().getTipo()
								.equals(TipoFormaPagamento.CARTAO_DE_CREDITO.getValor())
								&& formaPagamentoNegociacaoRecebimentoVO
										.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getSituacao()
										.equals(SituacaoContaReceber.RECEBIDO.getValor())
								&& !getPermiteEstornarRecebimentoCartaoCreditoJaRecebido())) {
					setBotaoExcluir(false);
					return;
				}
				setBotaoExcluir(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void calcularValorQuandoTipoRecebimentoDCC() {
		try {
			FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO = (FormaPagamentoNegociacaoRecebimentoVO) context()
					.getExternalContext().getRequestMap().get("cartaoItem2");
			getFacadeFactory().getNegociacaoRecebimentoDCCFacade().calcularValorQuandoTipoRecebimentoDCC(
					getNegociacaoRecebimentoVO(), formaPagamentoNegociacaoRecebimentoVO);
			limparMensagem();
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		}
	}

	public boolean getIsVerificarListaFormaPagamentoNegociacaoRecebimento() {
		return getNegociacaoRecebimentoVO().getFormaPagamentoNegociacaoRecebimentoVOs().size() > 0;
	}

	private String modalConfirmacaoPagamentoDCC;

	public String getModalConfirmacaoPagamentoDCC() {
		if (modalConfirmacaoPagamentoDCC == null) {
			modalConfirmacaoPagamentoDCC = "";
		}
		return modalConfirmacaoPagamentoDCC;
	}

	public void setModalConfirmacaoPagamentoDCC(String modalConfirmacaoPagamentoDCC) {
		this.modalConfirmacaoPagamentoDCC = modalConfirmacaoPagamentoDCC;
	}

	public void apresentarDicaCVCartaoCredito() {
		getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO()
				.setApresentarDicaCVCartaoCredito(true);
	}

	public void esconderDicaCVCartaoCredito() {
		getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO()
				.setApresentarDicaCVCartaoCredito(false);
	}

	public void removerCartaoCreditoDCC() {
		FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO = (FormaPagamentoNegociacaoRecebimentoVO) context()
				.getExternalContext().getRequestMap().get("cartaoItem2");
		for (Iterator iterator = getNegociacaoRecebimentoVO().getFormaPagamentoNegociacaoRecebimentoVOs()
				.iterator(); iterator.hasNext();) {
			FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO2 = (FormaPagamentoNegociacaoRecebimentoVO) iterator
					.next();
			if (formaPagamentoNegociacaoRecebimentoVO2.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO()
					.getNumeroCartao().equals(formaPagamentoNegociacaoRecebimentoVO
							.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNumeroCartao())) {
				iterator.remove();
			}
		}
		for (ContaReceberNegociacaoRecebimentoVO contaReceberNegociacaoRecebimentoVO : getNegociacaoRecebimentoVO()
				.getContaReceberNegociacaoRecebimentoVOs()) {
			for (Iterator iterator = contaReceberNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoVOs()
					.iterator(); iterator.hasNext();) {
				FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO2 = (FormaPagamentoNegociacaoRecebimentoVO) iterator
						.next();
				if (formaPagamentoNegociacaoRecebimentoVO2.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO()
						.getNumeroCartao().equals(formaPagamentoNegociacaoRecebimentoVO
								.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNumeroCartao())) {
					iterator.remove();
				}
			}
		}
		getNegociacaoRecebimentoVO().setValorTotalRecebimento(0.00);
		for (ContaReceberNegociacaoRecebimentoVO contaReceberNegociacaoRecebimentoVO : getNegociacaoRecebimentoVO()
				.getContaReceberNegociacaoRecebimentoVOs()) {
			for (FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO2 : contaReceberNegociacaoRecebimentoVO
					.getFormaPagamentoNegociacaoRecebimentoVOs()) {
				getNegociacaoRecebimentoVO()
						.setValorTotalRecebimento(getNegociacaoRecebimentoVO().getValorTotalRecebimento()
								+ formaPagamentoNegociacaoRecebimentoVO2.getValorRecebimento());
			}
		}
		setConfiguracaoFinanceiroCartaoVO(new ConfiguracaoFinanceiroCartaoVO());
	}

	public void verificarNegociacaoRecebimentoRecorrencia() {
//		if (getNegociacaoRecebimentoVO().getPagamentoComDCC()) {
//			setRecorrencia(true);
//		}
	}

	private Boolean permiteRecebimentoCartaoCreditoOnline;

	public Boolean getPermiteRecebimentoCartaoCreditoOnline() {
		if (permiteRecebimentoCartaoCreditoOnline == null) {
			permiteRecebimentoCartaoCreditoOnline = false;
		}
		return permiteRecebimentoCartaoCreditoOnline;
	}

	public void setPermiteRecebimentoCartaoCreditoOnline(Boolean permiteRecebimentoCartaoCreditoOnline) {
		this.permiteRecebimentoCartaoCreditoOnline = permiteRecebimentoCartaoCreditoOnline;
	}

	public void calcularValorRecebido() {
		getNegociacaoRecebimentoVO().setValorTotalRecebimento(0.0);
		for (FormaPagamentoNegociacaoRecebimentoVO obj : getNegociacaoRecebimentoVO().getFormaPagamentoNegociacaoRecebimentoVOs()) {
			getNegociacaoRecebimentoVO().setValorTotalRecebimento(getNegociacaoRecebimentoVO().getValorTotalRecebimento() + obj.getValorRecebimento());
		}
	}

	/**
	 * @author Victor Hugo de Paula Costa
	 */
	private MatriculaPeriodoVO matriculaPeriodoVO;

	public MatriculaPeriodoVO getMatriculaPeriodoVO() {
		if (matriculaPeriodoVO == null) {
			matriculaPeriodoVO = new MatriculaPeriodoVO();
		}
		return matriculaPeriodoVO;
	}

	public void setMatriculaPeriodoVO(MatriculaPeriodoVO matriculaPeriodoVO) {
		this.matriculaPeriodoVO = matriculaPeriodoVO;
	}

	public void montarUltimaMatriculaPeriodoAluno() {
		if (getNegociacaoRecebimentoVO().getTipoAluno()) {
			try {
				setMatriculaPeriodoVO(new MatriculaPeriodoVO());
				setMatriculaPeriodoVO(getFacadeFactory().getMatriculaPeriodoFacade().consultarUltimaMatriculaPeriodoAtivaPorMatricula(
								getNegociacaoRecebimentoVO().getMatricula(), false, Uteis.NIVELMONTARDADOS_TODOS,
								getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getNegociacaoRecebimentoVO().getUnidadeEnsino().getCodigo()), getUsuarioLogado()));
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public String realizarNavegacaoParaConciliacaoBancaria() {
		context().getExternalContext().getSessionMap().put("conciliacaoBancaria", getConciliacaoContaCorrenteVO());
		removerControleMemoriaFlash("ConciliacaoContaCorrenteControle");
		removerControleMemoriaTela("ConciliacaoContaCorrenteControle");
		return Uteis.getCaminhoRedirecionamentoNavegacao("conciliacaoContaCorrenteForm.xhtml");
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

	public String getRealizarNavegacaoParaContaReceber() {
		try {
			context().getExternalContext().getSessionMap().put("contaReceberLancamentoContabil",
					getContaReceberNegociacaoRecebimentoVO().getContaReceber());
			removerControleMemoriaFlash("ContaReceberControle");
			removerControleMemoriaTela("ContaReceberControle");
			return "popup('../financeiro/contaReceberForm.xhtml', 'contaReceberForm' , 1024, 800)";
		} catch (Exception e) {
			setMensagemID("msg_erro");
			return "";
		}
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

	private ImpressoraVO impressoraVO;
	private List<SelectItem> listaSelectItemImpressora;

	public ImpressoraVO getImpressoraVO() {
		if (impressoraVO == null) {
			impressoraVO = new ImpressoraVO();
		}
		return impressoraVO;
	}

	public void setImpressoraVO(ImpressoraVO impressoraVO) {
		this.impressoraVO = impressoraVO;
	}

	public void montarListaSelectItemImpressora() {
		try {
			if (Uteis.isAtributoPreenchido(getNegociacaoRecebimentoVO().getUnidadeEnsino())) {
				List<ImpressoraVO> impressoraVOs = getFacadeFactory().getImpressoraFacade().consultar(
						"codigoUnidadeEnsino", getNegociacaoRecebimentoVO().getUnidadeEnsino().getCodigo().toString(),
						getUnidadeEnsinoLogado(), false, getUsuarioLogado());
				setListaSelectItemImpressora(
						UtilSelectItem.getListaSelectItem(impressoraVOs, "codigo", "nomeImpressora", false));
				consultarImpressoraPadraoUsuarioFinanceiro();
				limparMensagem();
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void registrarImpressoraPadraoUsuarioFinanceiro() {
		try {
			if (Uteis.isAtributoPreenchido(getNegociacaoRecebimentoVO().getUnidadeEnsino())) {
				getFacadeFactory().getLayoutPadraoFacade()
						.persistirLayoutPadrao2(getImpressoraVO().getCodigo().toString(), "Recebimento",
								"ImpressoraU" + getUsuarioLogado().getCodigo() + "UE"
										+ getNegociacaoRecebimentoVO().getUnidadeEnsino().getCodigo(),
								getUsuarioLogado());
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void consultarImpressoraPadraoUsuarioFinanceiro() {
		try {
			if (Uteis.isAtributoPreenchido(getNegociacaoRecebimentoVO().getUnidadeEnsino())) {
				LayoutPadraoVO layoutPadraoVO = getFacadeFactory().getLayoutPadraoFacade().consultarPorEntidadeCampo(
						"Recebimento",
						"ImpressoraU" + getUsuarioLogado().getCodigo() + "UE"
								+ getNegociacaoRecebimentoVO().getUnidadeEnsino().getCodigo(),
						false, getUsuarioLogado());
				if (layoutPadraoVO != null && !layoutPadraoVO.getValor().trim().isEmpty()
						&& Uteis.getIsValorNumerico(layoutPadraoVO.getValor())) {
					getImpressoraVO().setCodigo(Integer.valueOf(layoutPadraoVO.getValor()));
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public boolean isApresentarValorIndiceReajustePorAtraso() {
		return apresentarValorIndiceReajustePorAtraso;
	}

	public void setApresentarValorIndiceReajustePorAtraso(boolean apresentarValorIndiceReajustePorAtraso) {
		this.apresentarValorIndiceReajustePorAtraso = apresentarValorIndiceReajustePorAtraso;
	}

	public List<SelectItem> getListaSelectItemImpressora() {
		if (listaSelectItemImpressora == null) {
			listaSelectItemImpressora = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemImpressora;
	}

	public void setListaSelectItemImpressora(List<SelectItem> listaSelectItemImpressora) {
		this.listaSelectItemImpressora = listaSelectItemImpressora;
	}

	public Boolean getIsHabilitarImpressaoApplet() {
		return getListaSelectItemImpressora().isEmpty()
				&& !getNegociacaoRecebimentoVO().getUnidadeEnsino().getCodigo().equals(0);
	}

	public Boolean getIsHabilitarImpressaoPoll() {
		return !getListaSelectItemImpressora().isEmpty()
				&& !getNegociacaoRecebimentoVO().getUnidadeEnsino().getCodigo().equals(0);
	}

	public static void verificarPermitirUsuarioPagarReceberContasEmCaixaUnidadeDiferenteDaConta(UsuarioVO usuario,
			String nomeEntidade) throws Exception {
		ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico(nomeEntidade, usuario);
	}

	public void verificarPermissaoUsuarioPagarReceberContasEmCaixaUnidadeDiferenteDaConta() {
		Boolean liberar = false;
		try {
			verificarPermitirUsuarioPagarReceberContasEmCaixaUnidadeDiferenteDaConta(getUsuarioLogado(),
					"ContaCorrente_PagarReceberCaixaUnidadeDiferente");
			liberar = true;
		} catch (Exception e) {
			liberar = false;
		}
		setPermitePagarReceberContasEmCaixaUnidadeDiferenteDaConta(liberar);
	}

	public Boolean getPermitePagarReceberContasEmCaixaUnidadeDiferenteDaConta() {
		if (permitePagarReceberContasEmCaixaUnidadeDiferenteDaConta == null) {
			permitePagarReceberContasEmCaixaUnidadeDiferenteDaConta = false;
		}
		return permitePagarReceberContasEmCaixaUnidadeDiferenteDaConta;
	}

	public void setPermitePagarReceberContasEmCaixaUnidadeDiferenteDaConta(
			Boolean permitePagarReceberContasEmCaixaUnidadeDiferenteDaConta) {
		this.permitePagarReceberContasEmCaixaUnidadeDiferenteDaConta = permitePagarReceberContasEmCaixaUnidadeDiferenteDaConta;
	}

	public void verificarPermissaoEstornarRecebimentoCartaoCreditoJaRecebido() throws Exception {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico(
					"Recebimento_PermitirEstornarRecebimentoCartaoCreditoJaRecebido", getUsuarioLogado());
			setPermiteEstornarRecebimentoCartaoCreditoJaRecebido(Boolean.TRUE);
		} catch (Exception e) {
			setPermiteEstornarRecebimentoCartaoCreditoJaRecebido(Boolean.FALSE);
		}
	}

	public Boolean getPermiteEstornarRecebimentoCartaoCreditoJaRecebido() {
		if (permiteEstornarRecebimentoCartaoCreditoJaRecebido == null) {
			permiteEstornarRecebimentoCartaoCreditoJaRecebido = false;
		}
		return permiteEstornarRecebimentoCartaoCreditoJaRecebido;
	}

	public void setPermiteEstornarRecebimentoCartaoCreditoJaRecebido(
			Boolean permiteEstornarRecebimentoCartaoCreditoJaRecebido) {
		this.permiteEstornarRecebimentoCartaoCreditoJaRecebido = permiteEstornarRecebimentoCartaoCreditoJaRecebido;
	}

	public boolean verificaTipoOrigemPermitidoContaCaixa() {
		boolean existeTipoOrigemNaoPermitido = false;

		try {

			List<String> listaTipoOrigemPermitidoContaCaixa = getListaTipoOrigemPermidoContaCaixa();

			for (ContaReceberNegociacaoRecebimentoVO contaReceberNegociacaoRecebimentoVO : getNegociacaoRecebimentoVO()
					.getContaReceberNegociacaoRecebimentoVOs()) {
				if (!listaTipoOrigemPermitidoContaCaixa
						.contains(contaReceberNegociacaoRecebimentoVO.getContaReceber().getTipoOrigem())) {
					existeTipoOrigemNaoPermitido = true;
				}
			}

			if (existeTipoOrigemNaoPermitido) {

				StringBuilder sbMensagemTipoOrigemPermitida = new StringBuilder();
				int cont = 1;

				if (!listaTipoOrigemPermitidoContaCaixa.isEmpty()) {
					sbMensagemTipoOrigemPermitida
							.append(" Esta Conta Caixa só permite Conta a Receber do tipo origem: ");

					for (String tipoOrigemPermitida : listaTipoOrigemPermitidoContaCaixa) {

						sbMensagemTipoOrigemPermitida.append(TipoOrigemContaReceber.getDescricao(tipoOrigemPermitida));

						if (cont == (listaTipoOrigemPermitidoContaCaixa.size() - 1)) {
							sbMensagemTipoOrigemPermitida.append(" e ");
						} else if (cont == listaTipoOrigemPermitidoContaCaixa.size()) {
							sbMensagemTipoOrigemPermitida.append(".");
						} else {
							sbMensagemTipoOrigemPermitida.append(", ");
						}
						cont++;
					}
				} else {
					sbMensagemTipoOrigemPermitida.append("Esta conta não permite recebimento de nenhum tipo de origem!");

				}
				setMensagemDetalhada("msg_erro", sbMensagemTipoOrigemPermitida.toString());

			}

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

		return existeTipoOrigemNaoPermitido;
	}

	public List<String> getListaTipoOrigemPermidoContaCaixa() throws Exception {

		List<String> listaTipoOrigemPermitidoContaCaixa = new ArrayList(0);

		if (negociacaoRecebimentoVO.getContaCorrenteCaixa().getCodigo() > 0) {
			ContaCorrenteVO contaCorrenteVO;
			contaCorrenteVO = getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(
					negociacaoRecebimentoVO.getContaCorrenteCaixa().getCodigo(), false,
					Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());

			if (contaCorrenteVO.getTipoOrigemBiblioteca()) {
				listaTipoOrigemPermitidoContaCaixa.add("BIB");
			}
			if (contaCorrenteVO.getTipoOrigemBolsaCusteadaConvenio()) {
				listaTipoOrigemPermitidoContaCaixa.add("BCC");
			}
			if (contaCorrenteVO.getTipoOrigemContratoReceita()) {
				listaTipoOrigemPermitidoContaCaixa.add("CTR");
			}
			if (contaCorrenteVO.getTipoOrigemDevolucaoCheque()) {
				listaTipoOrigemPermitidoContaCaixa.add("DCH");
			}
			if (contaCorrenteVO.getTipoOrigemInclusaoReposicao()) {
				listaTipoOrigemPermitidoContaCaixa.add("IRE");
			}
			if (contaCorrenteVO.getTipoOrigemInscricaoProcessoSeletivo()) {
				listaTipoOrigemPermitidoContaCaixa.add("IPS");
			}
			if (contaCorrenteVO.getTipoOrigemMaterialDidatico()) {
				listaTipoOrigemPermitidoContaCaixa.add("MDI");
			}
			if (contaCorrenteVO.getTipoOrigemMatricula()) {
				listaTipoOrigemPermitidoContaCaixa.add("MAT");
			}
			if (contaCorrenteVO.getTipoOrigemMensalidade()) {
				listaTipoOrigemPermitidoContaCaixa.add("MEN");
			}
			if (contaCorrenteVO.getTipoOrigemNegociacao()) {
				listaTipoOrigemPermitidoContaCaixa.add("NCR");
			}
			if (contaCorrenteVO.getTipoOrigemOutros()) {
				listaTipoOrigemPermitidoContaCaixa.add("OUT");
			}
			if (contaCorrenteVO.getTipoOrigemRequerimento()) {
				listaTipoOrigemPermitidoContaCaixa.add("REQ");
			}
		}
		return listaTipoOrigemPermitidoContaCaixa;

	}

	public Boolean getApresentarBotaoLiberarBloqueio() {
		return this.getNegociacaoRecebimentoVO().getApresentarBotaoLiberarBloqueioFechamentoMes();
	}

	public void liberarRegistroCompetenciaFechada() {
		try {
			this.getNegociacaoRecebimentoVO().setBloqueioPorFechamentoMesLiberado(Boolean.TRUE);
			this.getNegociacaoRecebimentoVO().setVerificouBloqueioPorFechamentoMes(Boolean.TRUE);
			FechamentoMesHistoricoModificacaoVO historico = getFacadeFactory().getFechamentoMesHistoricoModificacaoFacade().gerarNovoHistoricoModificacao(this.getNegociacaoRecebimentoVO().getFechamentoMesVOBloqueio(), getUsuarioLogado(), TipoOrigemHistoricoBloqueioEnum.RECEBIMENTO, this.getNegociacaoRecebimentoVO().getDescricaoBloqueio(), this.getNegociacaoRecebimentoVO().toString());
			getFacadeFactory().getFechamentoMesHistoricoModificacaoFacade().incluir(historico, getUsuarioLogado());
			setMensagemID("msg_registro_liberado_mes");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			this.getNegociacaoRecebimentoVO().setBloqueioPorFechamentoMesLiberado(Boolean.FALSE);
			this.getNegociacaoRecebimentoVO().setVerificouBloqueioPorFechamentoMes(Boolean.FALSE);
		}
	}

	public void verificarPermissaoLiberarBloqueioCompetencia() {
		try {
			UsuarioVO usuarioVerif = ControleAcesso.verificarLoginUsuario(this.getUsernameLiberacaoBloqueioPorFechamentoMes(), this.getSenhaLiberacaoBloqueioPorFechamentoMes(), true, Uteis.NIVELMONTARDADOS_TODOS);
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("FuncionarioMes_liberarBloqueioPagamentoContaPagar", usuarioVerif);
			liberarRegistroCompetenciaFechada();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void alterarDataReferencia() {
		try {
			getNegociacaoRecebimentoVO().reiniciarControleBloqueioCompetencia();
			getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoFacade().atualizarValoresDeDatasParaFormaPagamentoNegociacaoRecebimento(getNegociacaoRecebimentoVO(), getUsuarioLogadoClone());
			recalcularDescontos();	
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public String getMensagemAvisoEstornoPorOutraConta() {
		return new String("Aviso! Deve ser selecionada uma nova conta caixa para realizar este estorno, pois você não é o responsável pelo caixa que realizou este recebimento.");
	}

	public void realizarCarregamentoRecebimentoVindoTelaExtratoContaCorrente() {
		try {
			if (context().getExternalContext().getSessionMap().get("negociacaoExtratoContaCorrente") != null) {
				Integer codigo = (Integer) context().getExternalContext().getSessionMap().get("negociacaoExtratoContaCorrente");
				if (Uteis.isAtributoPreenchido(codigo)) {
					getNegociacaoRecebimentoVO().setCodigo(codigo);
					setNegociacaoRecebimentoVO(getFacadeFactory().getNegociacaoRecebimentoFacade().carregarDados(getNegociacaoRecebimentoVO(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getNegociacaoRecebimentoVO().getUnidadeEnsino().getCodigo()), getUsuarioLogado()));
					carregarDadosParaEditarNegociacaoRecebimento(getNegociacaoRecebimentoVO());
				}
				setMensagemID("msg_dados_editar", Uteis.ALERTA);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			context().getExternalContext().getSessionMap().remove("negociacaoExtratoContaCorrente");
		}
	}
	
	public boolean getTipoCreditoDebito() {
		return Uteis.isAtributoPreenchido(getConfiguracaoFinanceiroCartaoVO()) &&
			(getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamento().getTipo().equals("CA") ||
				getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamento().getTipo().equals("CD"));
	}
	
	private boolean verificarRecebimentoCartaoFeitoVisaoAluno() throws Exception {
		Predicate<FormaPagamentoNegociacaoRecebimentoVO> isRecebimentoCartaoCredito = f -> Uteis.isAtributoPreenchido(f.getOperadoraCartaoVO()) 
				&& Uteis.isAtributoPreenchido(f.getFormaPagamento().getTipoFormaPagamentoEnum()) && f.getFormaPagamento().getTipoFormaPagamentoEnum().equals(TipoFormaPagamento.CARTAO_DE_CREDITO);
		
		UsuarioVO responsavel = getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(getNegociacaoRecebimentoVO().getResponsavel().getCodigo(), 
				Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		
		PessoaVO responsavelFinanceiro = getFacadeFactory().getPessoaFacade().consultarResponsavelFinanceiroAluno(responsavel.getPessoa().getCodigo(), getUsuarioLogado());
		
		return getNegociacaoRecebimentoVO().getFormaPagamentoNegociacaoRecebimentoVOs().stream().allMatch(isRecebimentoCartaoCredito)
				&& ((Uteis.isAtributoPreenchido(responsavelFinanceiro) && responsavelFinanceiro.getCodigo().equals(getNegociacaoRecebimentoVO().getPessoa().getCodigo()))
						|| (responsavel.getPessoa().getCodigo().equals(getNegociacaoRecebimentoVO().getPessoa().getCodigo())));
	}
	public List<SelectItem> getListaSelectItemTipoFinanciamento() {
		if (listaSelectItemTipoFinanciamento == null) {
			listaSelectItemTipoFinanciamento = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemTipoFinanciamento;
	}

	public void setListaSelectItemTipoFinanciamento(List<SelectItem> listaSelectItemTipoFinanciamento) {
		this.listaSelectItemTipoFinanciamento = listaSelectItemTipoFinanciamento;
	}

	public void montarListaSelectItemTipoFinanciamento() {
		getListaSelectItemTipoFinanciamento().clear();
		getListaSelectItemTipoFinanciamento().add(new SelectItem(TipoFinanciamentoEnum.INSTITUICAO, TipoFinanciamentoEnum.INSTITUICAO.getValorApresentar()));
		getListaSelectItemTipoFinanciamento().add(new SelectItem(TipoFinanciamentoEnum.OPERADORA, TipoFinanciamentoEnum.OPERADORA.getValorApresentar()));			
	}

	public boolean isCampoNumeroReciboObrigatorio() {
		return campoNumeroReciboObrigatorio;
	}

	public void setCampoNumeroReciboObrigatorio(boolean campoNumeroReciboObrigatorio) {
		this.campoNumeroReciboObrigatorio = campoNumeroReciboObrigatorio;
	}
	
	public boolean getTipoCredito() {
		return Uteis.isAtributoPreenchido(getConfiguracaoFinanceiroCartaoVO()) && getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamento().getTipo().equals("CA");
	}
	
}
