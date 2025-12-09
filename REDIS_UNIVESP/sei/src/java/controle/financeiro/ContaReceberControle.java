package controle.financeiro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.academico.AlunoControle;
import controle.academico.VisaoAlunoControle;
import controle.arquitetura.DataModelo;
import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DescontoProgressivoVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PlanoDescontoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.Obrigatorio;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoFinanceiroEnum;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoVisaoAlunoEnum;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.contabil.FechamentoMesHistoricoModificacaoVO;
import negocio.comuns.contabil.LancamentoContabilCentroNegocioVO;
import negocio.comuns.contabil.LancamentoContabilVO;
import negocio.comuns.contabil.PlanoContaVO;
import negocio.comuns.contabil.enumeradores.TipoOrigemHistoricoBloqueioEnum;
import negocio.comuns.contabil.enumeradores.TipoPlanoContaEnum;
import negocio.comuns.crm.FollowUpVO;
import negocio.comuns.crm.InteracaoWorkflowVO;
import negocio.comuns.crm.ProspectsVO;
import negocio.comuns.crm.enumerador.TipoInteracaoEnum;
import negocio.comuns.crm.enumerador.TipoOrigemInteracaoEnum;
import negocio.comuns.ead.enumeradores.SituacaoEnum;
import negocio.comuns.financeiro.CartaoCreditoDebitoRecorrenciaPessoaVO;
import negocio.comuns.financeiro.CentroReceitaVO;
import negocio.comuns.financeiro.CentroResultadoOrigemVO;
import negocio.comuns.financeiro.CentroResultadoVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroCartaoVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ConfiguracaoRecebimentoCartaoOnlineVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ContaPagarVO;
import negocio.comuns.financeiro.ContaReceberNegociacaoRecebimentoVO;
import negocio.comuns.financeiro.ContaReceberRecebimentoVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.FormaPagamentoNegociacaoRecebimentoVO;
import negocio.comuns.financeiro.IndiceReajustePeriodoMatriculaPeriodoVencimentoVO;
import negocio.comuns.financeiro.MatriculaPeriodoVencimentoVO;
import negocio.comuns.financeiro.NegociacaoRecebimentoVO;
import negocio.comuns.financeiro.OrdemDescontoVO;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.financeiro.PlanoDescontoContaReceberVO;
import negocio.comuns.financeiro.RecebimentoVO;
import negocio.comuns.financeiro.RegistroNegativacaoCobrancaContaReceberItemVO;
import negocio.comuns.financeiro.TransacaoCartaoOnlineVO;
import negocio.comuns.financeiro.enumerador.FormaPadraoDataBaseCartaoRecorrenteEnum;
import negocio.comuns.financeiro.enumerador.PermitirCartaoEnum;
import negocio.comuns.financeiro.enumerador.TipoCentroNegocioEnum;
import negocio.comuns.financeiro.enumerador.TipoCentroResultadoOrigemEnum;
import negocio.comuns.financeiro.enumerador.TipoMovimentacaoCentroResultadoOrigemEnum;
import negocio.comuns.financeiro.enumerador.TipoNivelCentroResultadoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.SituacaoExecucaoEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.SituacaoContaReceber;
import negocio.comuns.utilitarias.dominios.TipoBoletoBancario;
import negocio.comuns.utilitarias.dominios.TipoCartaoOperadoraCartaoEnum;
import negocio.comuns.utilitarias.dominios.TipoDescontoAluno;
import negocio.comuns.utilitarias.dominios.TipoOrigemContaReceber;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.facade.jdbc.academico.Matricula;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.arquitetura.FacadeFactory;
import negocio.facade.jdbc.financeiro.NegociacaoRecebimento;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.controle.financeiro.ComprovanteRecebimentoRelControle;
import relatorio.negocio.comuns.financeiro.FiltroRelatorioFinanceiroVO;

@SuppressWarnings("deprecation")
@Controller("ContaReceberControle")
@Scope("viewScope")
@Lazy
public class ContaReceberControle extends SuperControle implements Serializable {

	private static final long serialVersionUID = -3541087459710037078L;

	private ContaReceberVO contaReceberVO;
	protected List listaSelectItemMatriculaAluno;
	protected List listaSelectItemContaCorrente;
	protected List listaSelectItemUnidadeEnsino;
	protected List listaSelectItemUnidadeEnsinoFinanceira;
	protected List listaSelectItemFuncionario;
	protected List listaSelectItemCurso;
	protected CursoVO cursoVO;
	protected List listaConsultaFuncionario;
	protected String valorConsultaFuncionario;
	protected int valorConsultaUnidadeEnsino;
	protected String campoConsultaFuncionario;
	protected List listaConsultaAluno;
	protected String valorConsultaAluno;
	protected String campoConsultaAluno;
	protected List<PessoaVO> listaConsultaResponsavelFinanceiro;
	protected String valorConsultaResponsavelFinanceiro;
	protected String campoConsultaResponsavelFinanceiro;
	protected List listaConsultaCandidato;
	protected String valorConsultaCandidato;
	protected String campoConsultaCandidato;
	protected List listaConsultaRequisitante;
	protected String valorConsultaRequisitante;
	protected String campoConsultaRequisitante;
	protected List listaConsultaCentroReceita;
	protected String valorConsultaCentroReceita;
	protected String campoConsultaCentroReceita;
	protected List<FornecedorVO> listaConsultaFornecedor;
	protected String valorConsultaFornecedor;
	protected String campoConsultaFornecedor;
	protected RecebimentoVO recebimentoVO;
	protected List listaSelectItemTipoContasAPagar;
	protected String tipoContasAPagar;
	protected List<ContaPagarVO> listaContasAPagar;
	protected List<ContaReceberVO> listaRecebimentos;
	protected ContaReceberVO contaReceberVisaoAluno;
	protected List<ContaReceberVO> listaContasPagarVisaoAluno;
	protected List<ContaReceberVO> listaContasPagarParceiroVisaoAluno;
	protected String valorConsultaParceiro;
	protected String campoConsultaParceiro;
	protected List<ParceiroVO> listaConsultaParceiro;
	private Double totalReceber;
	private Double totalRecebido;
	private Double totalCancelado;
	private Double totalNegociado;
	private ConfiguracaoFinanceiroVO configuracaoFinanceiroVO;
	private String abrirModalCobrarReimpressao;
	private Boolean permissaoControleFinanceiroManual;
	private Boolean permissaoAlterarDescontoFinanceiroManualDesativado;
	private Boolean financeiroManualMatriculaPeriodo;
	private Boolean novaContaReceber;
	private Boolean apresentarRichModalFinanceiroManual;
	private Boolean consultaPainelGestorFinanceiro;
	private String situacaoConsultaPainelGestorFinanceiro;
	private Boolean consultaDataScroller;
	protected TurmaVO turmaVO;
	private String campoConsultaTurma;
	private String valorConsultaTurma;
	private List listaConsultaTurma;
	private String campoConsultaCurso;
	private String valorConsultaCurso;
	private List listaConsultaCurso;
	private UnidadeEnsinoCursoVO unidadeEnsinoCursoVO;
	private String tipoContaReceberAtualizar;
	private ContaReceberVO ultimaContaReceberGeradaManualmente;
	private List<SelectItem> listaSelectItemAluno;
	private Boolean isentarJuroMulta;
	private Date dataVencimento;
	private String ano;
	private String semestre;
	private ContaReceberVO contaReceberSimulada;
	private String tipoBoleto;
	private String tipoOrigem;
	private String apresentarMsgPreMatricula;
	private Boolean apresentarBotaoAlterarVencimentoEValor;
	private Boolean apresentarBotaoAlterarVencimento;
	private Boolean apresentarBotaoAlterarValor;
	private Boolean permiteReceber;
	private Boolean consDataCompetencia;
	private Double valor;
	private String motivoNeg;
	private LancamentoContabilVO lancamentoContabilVO;
	private LancamentoContabilCentroNegocioVO lancamentoContabilCentroNegocioVO;
	private TipoCentroNegocioEnum tipoCategoriaDespesaRateioEnum;
	private String modalAptoParaSerFechado;
	private String valorConsultaPlanoConta;
	private String campoConsultaPlanoConta;
	private List<PlanoContaVO> listaConsultaPlanoConta;
	private List<IndiceReajustePeriodoMatriculaPeriodoVencimentoVO> listaIndiceReajustePeriodoMatriculaPeriodoVencimentoVOs;
	private List<IndiceReajustePeriodoMatriculaPeriodoVencimentoVO> listaIndiceReajustePeriodoMatriculaPeriodoVencimentoCancelamentoVOs;

	private Boolean permiteImprimirBoletoRecebido;

	private Boolean marcarTodos;
	private Boolean marcarTodosTipoOrigem;

	private Map<String, Boolean> situacoesContaReceber;
	private List<String> situacoes;
	private CentroResultadoOrigemVO centroResultadoOrigemVO;
	private DataModelo centroResultadoDataModelo;
	
	protected List<CentroReceitaVO> listaConsultaCentroReceitaCentroResultado;
	protected String valorConsultaCentroReceitaCentroResultado;
	protected String campoConsultaCentroReceitaCentroResultado;

	private FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO;
	
	private String userNameLiberarFuncionalidade;
	private String senhaLiberarFuncionalidade;
	
	private TipoCartaoOperadoraCartaoEnum tipoCartao;
	private List<SelectItem> opFiltrosContaReceber;
	private List<SelectItem> opFiltrosSelecionado;
	
	private Boolean mensagemConfirmacaoRecorrenciaApresentada;
	
	private List<CartaoCreditoDebitoRecorrenciaPessoaVO> listaCartaoCreditoDebitoRecorrenciaPessoaVOs;
	private CartaoCreditoDebitoRecorrenciaPessoaVO cartaoCreditoDebitoRecorrenciaPessoaVO;
	private List<SelectItem> listaSelectItemCartaoCreditoDebitoRecorrenciaPessoaVOs;
	private List<TransacaoCartaoOnlineVO> listaTransacaoCartaoOnlineVOs;
	private Boolean descontoMaiorValorContaReceber;
	
	private Double totalContaPagarVisaoAlunoValorBase;
	private Double totalContaPagarVisaoAlunoValorFinal;
	private Double totalContaPagarVisaoAlunoCancelado;
	private Double totalContaPagarVisaoAlunoNegociado;
	private Double totalContaPagarVisaoAlunoPago;
	
	private Boolean permiteVisualizarAbaTransacaoCartaoVisaoAluno;
	private Boolean permiteVisualizarAbaRecorrenciaVisaoAluno;
	
	public void montarOpFiltrosContaReceber() {
		setOpFiltrosContaReceber(new ArrayList<SelectItem>());
		getOpFiltrosContaReceber().add(new SelectItem("contareceber.matriculaaluno", "Matrícula"));
		getOpFiltrosContaReceber().add(new SelectItem("contareceber.datavencimento", "Data Vencimento"));
		getOpFiltrosContaReceber().add(new SelectItem("contareceber.situacao", "Situação"));
		getOpFiltrosContaReceber().add(new SelectItem("nomesacado", "Sacado"));
		getOpFiltrosContaReceber().add(new SelectItem("contareceber.codigo", "Código"));
	}
	
	public boolean getIsCampoNomeSacado() {
		 
		if( getControleConsulta().getCampoConsulta().equals("") || getControleConsulta().getCampoConsulta().equals("nomeSacado") || getControleConsulta().getCampoConsulta().equals("aluno")
			|| getControleConsulta().getCampoConsulta().equals("alunoNome") || getControleConsulta().getCampoConsulta().equals("responsavelFinanceiro")
			|| getControleConsulta().getCampoConsulta().equals("funcionario") || getControleConsulta().getCampoConsulta().equals("identificadorCentroReceitaCentroReceita")
			|| getControleConsulta().getCampoConsulta().equals("convenio") || getControleConsulta().getCampoConsulta().equals("fornecedor")
			|| getControleConsulta().getCampoConsulta().equals("anoSemestre")
			|| getControleConsulta().getCampoConsulta().equals("cpf")) {
			
			return true;
		}
		return false;
	}
	
	public boolean getIsCampoCodigo() {
		 
		if(getControleConsulta().getCampoConsulta().equals("codigo")) {
			return true;
		}
		return false;
	}
	
	public List<SelectItem> getOpFiltrosContaReceber() {
		if(opFiltrosContaReceber == null) {
			opFiltrosContaReceber = new ArrayList<SelectItem>();
		}
		return opFiltrosContaReceber;
	}

	public void setOpFiltrosContaReceber(List<SelectItem> opFiltrosContaReceber) {
		this.opFiltrosContaReceber = opFiltrosContaReceber;
	}


	public List<SelectItem> getOpFiltrosSelecionado() {
		if(opFiltrosSelecionado == null) {
			opFiltrosSelecionado = new ArrayList<SelectItem>();
		}
		return opFiltrosSelecionado;
	}

	public void setOpFiltrosSelecionado(List<SelectItem> opFiltrosSelecionado) {
		this.opFiltrosSelecionado = opFiltrosSelecionado;
	}
	
	
	public List<String> buscaFiltroSelecionado() throws Exception {
		List<String> itensString = new ArrayList<String>();
		 
		 for (int j = 0; j <getOpFiltrosSelecionado().size(); j++) {
			 Object value = getOpFiltrosSelecionado().get(j);
			 if(value.toString().equals("nomesacado")) {
				 itensString.add("contareceber.nomesacado");
			 }else {
				 
			 itensString.add(value.toString());
			 }
		
			 
		}	 
		return itensString;
	}
		

	public ContaReceberControle() throws Exception {
		// obterUsuarioLogado();
		setControleConsulta(new ControleConsulta());
		setTipoContasAPagar("");
		setListaContasAPagar(new ArrayList<>(0));
		setListaSelectItemTipoContasAPagar(new ArrayList<>(0));
		setListaRecebimentos(new ArrayList<>(0));
		ConfiguracaoFinanceiroVO configuracaoFinanceiroVO = getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getUnidadeEnsinoLogado().getCodigo());
		setTipoContasAPagar(configuracaoFinanceiroVO.getFiltroPadraoContaReceberVisaoAluno());
		setContaReceberVisaoAluno(new ContaReceberVO());
		setMensagemID("msg_entre_prmconsulta");
		setTotalReceber(0.0);
		setTotalRecebido(0.0);
		montarListaSelectItemUnidadeEnsino();
		verificarPermissaoReceberConta();
		verificarPermissaoImprimirContaRecebidaCanceladaRenegociada();
		getControleConsulta().setDataIni(Uteis.getDataPrimeiroDiaMes(new Date()));
		getControleConsulta().setDataFim(Uteis.getDataUltimoDiaMes(new Date()));
		getControleConsulta().setBuscarPeriodoCompleto(Boolean.FALSE);
		montarOpFiltrosContaReceber();
		montarSituacoesContaReceber();
		setMarcarTodos(false);		
		filtroRelatorioFinanceiroVO = new FiltroRelatorioFinanceiroVO(getLoginControle().getPermissaoAcessoMenuVO().getPermitirApenasContasDaBiblioteca());
		filtroRelatorioFinanceiroVO.realizarDesmarcarTodasSituacoes();
		filtroRelatorioFinanceiroVO.setSituacaoReceber(true);
		if(getSituacoes() == null) {
			setSituacoes(new ArrayList<String>(0));
			getSituacoes().add("AR");
		}
		if (getUsuarioLogado().getVisaoLogar().equals("aluno")) {
			verificarPermissaoVisualizarAbaRecorrenciaVisaoAluno();
			verificarPermissaoVisualizarAbaTransacaoCartaoVisaoAluno();
		}
		realizarSelecaoCheckboxMarcarDesmarcarTodosTipoOrigem();
    	getControleConsultaOtimizado().setPage(0);
        getControleConsultaOtimizado().setPaginaAtual(1);
	}

	public void montarSituacoesContaReceber() {
		situacoesContaReceber = new HashMap<>();
		situacoesContaReceber.put("A RECEBER", true);
		situacoesContaReceber.put("RECEBIDO", false);
		situacoesContaReceber.put("NEGOCIADO", false);
		situacoesContaReceber.put("CANCELADO", false);
	}

	@SuppressWarnings({ "unchecked" })
	@PostConstruct
	public void realizarCarregamentoContaReceberVindoTelaFichaAluno() {
		if(getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
		ContaReceberVO obj = (ContaReceberVO) context().getExternalContext().getSessionMap().get("contaReceberFichaAluno");
		if (obj != null && !obj.getCodigo().equals(0)) {
			try {
				obj.setTelaEdicaoContaReceber(true);
				getFacadeFactory().getContaReceberFacade().carregarDados(obj, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(obj.getUnidadeEnsino().getCodigo()), getUsuarioLogado());
				obj.setContaReceberHistoricoVOs(getFacadeFactory().getContaReceberHistoricoFacade().consultarPorCodigoContaReceber(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
				obj.setNovoObj(Boolean.FALSE);
				if (!obj.getContaReceberRecebimentoVOs().isEmpty()) {
					obj.verificarUtilizacaoDesconto(obj.getContaReceberRecebimentoVOs().get(0).getDataRecebimeto());
				}
				setContaReceberVO(obj);
				MatriculaPeriodoVO matriculaPeriodo = getFacadeFactory().getMatriculaPeriodoFacade().consultarFinanceiroManulaPorChavePrimaria(obj.getMatriculaPeriodo().intValue(), getUsuarioLogado());
				if (matriculaPeriodo != null && matriculaPeriodo.getCodigo() != 0) {
					if (obj.getSituacao().equals("AR")) {
						if (matriculaPeriodo.getFinanceiroManual()) {
							setFinanceiroManualMatriculaPeriodo(true);
							setApresentarRichModalFinanceiroManual(true);
						} else {
							setApresentarRichModalFinanceiroManual(true);
							setFinanceiroManualMatriculaPeriodo(false);
						}
					}
				}
				setNovaContaReceber(Boolean.FALSE);
				inicializarListasSelectItemTodosComboBox();
				verificarPermissaoApresentarBotaoAlterarDataVencimentoValorRecebimento();
				verificarPermissaoApresentarBotaoEditarManualmenteConta();
				verificarPermissaoReceberConta();
				// inicializarListaRecebimentos();
				this.setValor(obj.getValorBaseContaReceber());
				if (getContaReceberVO().getPagoComDCC()) {
					setModalPagoDCC("RichFaces.$('panelAvisoPagoDCC').show()");
				}
				setMensagemID("msg_dados_editar");
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			} finally {
				context().getExternalContext().getSessionMap().remove("contaReceberFichaAluno");
			}
		}
		}
	}

	@PostConstruct
	public void realizarInclusaoContaReceberVindoTelaFichaAluno() {
		if(getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
		MatriculaVO obj = (MatriculaVO) context().getExternalContext().getSessionMap().get("matriculaIncluirNovaContaFichaAluno");
		if (obj != null && !obj.getMatricula().equals("")) {
			try {
				novo();
				getContaReceberVO().setTipoPessoa(TipoPessoa.ALUNO.getValor());
				getContaReceberVO().getMatriculaAluno().setMatricula(obj.getMatricula());
				getContaReceberVO().getMatriculaAluno().getAluno().setCodigo(obj.getAluno().getCodigo());
				getContaReceberVO().getMatriculaAluno().getAluno().setNome(obj.getAluno().getNome());
				getContaReceberVO().getUnidadeEnsino().setCodigo(obj.getUnidadeEnsino().getCodigo());
				getContaReceberVO().getUnidadeEnsino().setNome(obj.getUnidadeEnsino().getNome());
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			} finally {
				context().getExternalContext().getSessionMap().remove("matriculaIncluirNovaContaFichaAluno");
			}
		}
		}
	}

	@PostConstruct
	public String editarContaReceberVindoTelaNegociacaoRecebimentoContabil() throws Exception {
		if(getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
		ContaReceberVO obj = (ContaReceberVO) context().getExternalContext().getSessionMap().get("contaReceberLancamentoContabil");
		if (obj != null && !obj.getCodigo().equals(0)) {
			try {
				obj.setTelaEdicaoContaReceber(true);
				getFacadeFactory().getContaReceberFacade().carregarDados(obj, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(obj.getUnidadeEnsino().getCodigo()), getUsuarioLogado());
				obj.setContaReceberHistoricoVOs(getFacadeFactory().getContaReceberHistoricoFacade().consultarPorCodigoContaReceber(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
				obj.setNovoObj(Boolean.FALSE);
				if (!obj.getContaReceberRecebimentoVOs().isEmpty()) {
					obj.verificarUtilizacaoDesconto(obj.getContaReceberRecebimentoVOs().get(0).getDataRecebimeto());
				}
				setContaReceberVO(obj);
				MatriculaPeriodoVO matriculaPeriodo = getFacadeFactory().getMatriculaPeriodoFacade().consultarFinanceiroManulaPorChavePrimaria(obj.getMatriculaPeriodo().intValue(), getUsuarioLogado());
				if (matriculaPeriodo != null && matriculaPeriodo.getCodigo() != 0) {
					if (obj.getSituacao().equals("AR")) {
						if (matriculaPeriodo.getFinanceiroManual()) {
							setFinanceiroManualMatriculaPeriodo(true);
							setApresentarRichModalFinanceiroManual(true);
						} else {
							setApresentarRichModalFinanceiroManual(true);
							setFinanceiroManualMatriculaPeriodo(false);
						}
					}
				}
				setNovaContaReceber(Boolean.FALSE);
				inicializarListasSelectItemTodosComboBox();
				verificarPermissaoApresentarBotaoAlterarDataVencimentoValorRecebimento();
				verificarPermissaoApresentarBotaoEditarManualmenteConta();
				verificarPermissaoReceberConta();
				verificarPermissaoLancamentoContabilPagar();
				this.setValor(obj.getValorBaseContaReceber());
				if (getContaReceberVO().getPagoComDCC()) {
					setModalPagoDCC("RichFaces.$('panelAvisoPagoDCC').show()");
				}
				setMensagemID("msg_dados_editar");
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			} finally {
				context().getExternalContext().getSessionMap().remove("contaReceberLancamentoContabil");
			}
		}
		}
		return "";
	}

	public void verificarPermissaoReceberConta() {
		try {
			NegociacaoRecebimento.incluir("NegociacaoRecebimento", getUsuarioLogado());
			setPermiteReceber(Boolean.TRUE);
		} catch (Exception e) {
			setPermiteReceber(Boolean.FALSE);
		}
	}

	public void verificarPermissaoImprimirContaRecebidaCanceladaRenegociada() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("ContaReceber_PermitirImprimirBoletoRecebidoCanceladoRenegociado", getUsuarioLogado());
			setPermiteImprimirBoletoRecebido(Boolean.TRUE);
		} catch (Exception e) {
			setPermiteImprimirBoletoRecebido(Boolean.FALSE);
		}
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe <code>ContaReceber</code> para edição pelo usuário da aplicação.
	 */
	public String novo() throws Exception {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "ContaReceberControle", "Nova Conta Receber", "Novo");
			removerObjetoMemoria(this);
			setContaReceberVO(new ContaReceberVO());
			setNovaContaReceber(Boolean.TRUE);
			inicializarListasSelectItemTodosComboBox();
			inicializarConfiguracaoFinanceiro();
			inicializarJurosEMultasConfiguracaoFinanceiro();
			inicializarListaRecebimentos();
			setUltimaContaReceberGeradaManualmente(new ContaReceberVO());
			getUltimaContaReceberGeradaManualmente().setNovoObj(Boolean.TRUE);
			setListaRecebimentos(new ArrayList(0));
			setLancamentoContabilVO(new LancamentoContabilVO());
			setLancamentoContabilCentroNegocioVO(new LancamentoContabilCentroNegocioVO());
			verificarPermissaoReceberConta();
			verificarPermissaoLancamentoContabilPagar();
			verificarPermissaoApresentarBotaoEditarManualmenteConta();
			if (this.getApresentarBotaoEditarManualmenteConta()) {
				// se o usuário tem permissão para editar manualmente a conta, entao
				// iremos ativar esse recurso, haja vista, que assim o usuario poderá
				// adicionar planos de descontos manuais para a conta.
				habilitarEditarManualmenteConta();
			}
			getContaReceberVO().setNovoObj(Boolean.TRUE);
			getContaReceberVO().setTipoOrigem("OUT");
			getContaReceberVO().setContaEditadaManualmente(true);
			setCentroResultadoOrigemVO(new CentroResultadoOrigemVO());
			setApresentarRichPanelGravarContaReceber(Boolean.FALSE);
			setLancadoOuAlteradoAlgumFollowUp(Boolean.FALSE);
			setCarregadoFollowUpConta(Boolean.FALSE);
			setMensagemID("msg_entre_dados");
		} catch (Exception e) {
			// System.out.println(e.getMessage());
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("contaReceberForm.xhtml");
	}

	public void carregarDadosPorTipoPessoa() {
		try {
			getContaReceberVO().setMatriculaAluno(new MatriculaVO());
			getContaReceberVO().setParceiroVO(new ParceiroVO());
			getContaReceberVO().setCandidato(new PessoaVO());
			getContaReceberVO().setFornecedor(new FornecedorVO());
			getContaReceberVO().setFuncionario(new FuncionarioVO());
			getContaReceberVO().setPessoa(new PessoaVO());
			getContaReceberVO().setResponsavelFinanceiro(new PessoaVO());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void carregarDadosPorTipoOrigem() {
		try {
			if (getContaReceberVO().isContaReceberReferenteMaterialDidatico()) {
				montarListaSelectItemUnidadeEnsinoFinanceira();
				if (getContaReceberVO().getTipoParceiro() && Uteis.isAtributoPreenchido(getContaReceberVO().getParceiroVO()) && !getContaReceberVO().getParceiroVO().isCusteaParcelasMaterialDidatico()) {
					getContaReceberVO().setMatriculaAluno(new MatriculaVO());
					getContaReceberVO().setParceiroVO(new ParceiroVO());
					throw new Exception("O parceiro informado não custea parcela de material didático.");
				}
				if (!getContaReceberVO().getTipoAluno() && !getContaReceberVO().getTipoResponsavelFinanceiro() && !getContaReceberVO().getTipoParceiro()) {
					getContaReceberVO().setTipoPessoa("");
					getContaReceberVO().setCandidato(new PessoaVO());
					getContaReceberVO().setFornecedor(new FornecedorVO());
					getContaReceberVO().setFuncionario(new FuncionarioVO());
					getContaReceberVO().setPessoa(new PessoaVO());
				}
				if (getContaReceberVO().getTipoAluno() && !getContaReceberVO().getMatriculaAluno().getMatricula().isEmpty()) {
					vincularMatriculaVOContaReceber(getContaReceberVO().getMatriculaAluno());
				}
			} else {
				if (getContaReceberVO().getTipoAluno() && !getContaReceberVO().getMatriculaAluno().getMatricula().isEmpty() && getContaReceberVO().getTipoOrigemContaReceber().isBiblioteca()) {
					vincularMatriculaVOContaReceber(getContaReceberVO().getMatriculaAluno());
				}
				getContaReceberVO().setUnidadeEnsinoFinanceira(new UnidadeEnsinoVO());
			}
			carregarDadosPorUnidadeEnsino();
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	@PostConstruct
	public void obterContaReceberPainelGestor() {
		if(getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
		ContaReceberVO obj = (ContaReceberVO) context().getExternalContext().getSessionMap().get("ContaReceberPainelGestor");
		if (obj != null && !obj.getCodigo().equals(0)) {
			try {
				getFacadeFactory().getContaReceberFacade().carregarDados(obj, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(obj.getUnidadeEnsino().getCodigo()), getUsuarioLogado());
				setContaReceberVO(obj);
				obj.setContaReceberHistoricoVOs(getFacadeFactory().getContaReceberHistoricoFacade().consultarPorCodigoContaReceber(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
				obj.setNovoObj(Boolean.FALSE);
				if (!obj.getContaReceberRecebimentoVOs().isEmpty()) {
					obj.verificarUtilizacaoDesconto(obj.getContaReceberRecebimentoVOs().get(0).getDataRecebimeto());
				}
				setContaReceberVO(obj);
				MatriculaPeriodoVO matriculaPeriodo = getFacadeFactory().getMatriculaPeriodoFacade().consultarPorCodigo(obj.getMatriculaPeriodo().intValue(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(obj.getUnidadeEnsino().getCodigo()), getUsuarioLogado());
				if (matriculaPeriodo.getCodigo() != 0) {
					if (obj.getSituacao().equals("AR")) {
						if (matriculaPeriodo.getFinanceiroManual()) {
							setFinanceiroManualMatriculaPeriodo(true);
							setApresentarRichModalFinanceiroManual(true);
						} else {
							setApresentarRichModalFinanceiroManual(true);
							setFinanceiroManualMatriculaPeriodo(false);
						}
					}
				}
				setNovaContaReceber(Boolean.FALSE);
				inicializarListasSelectItemTodosComboBox();
				verificarPermissaoApresentarBotaoAlterarDataVencimentoValorRecebimento();
				verificarPermissaoApresentarBotaoEditarManualmenteConta();
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			} finally {
				context().getExternalContext().getSessionMap().put("ContaReceberPainelGestor", null);
			}
		}
		}
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe <code>ContaReceber</code> para alteração. O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa disponibilizá-lo para edição.
	 */
	public String editar() throws Exception {
		try {
			setContaReceberVO(null);
			registrarAtividadeUsuario(getUsuarioLogado(), "ContaReceberControle", "Editando Conta Receber", "Editar");
			setModalPagoDCC("");
			setApresentarRichPanelGravarContaReceber(Boolean.FALSE);
			ContaReceberVO obj = (ContaReceberVO) context().getExternalContext().getRequestMap().get("contaReceberItens");
			obj.setTelaEdicaoContaReceber(true);
			getFacadeFactory().getContaReceberFacade().carregarDados(obj, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(obj.getUnidadeEnsino().getCodigo()), getUsuarioLogado());
			obj.setContaReceberHistoricoVOs(getFacadeFactory().getContaReceberHistoricoFacade().consultarPorCodigoContaReceber(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			obj.setHistoricoNegativacoes(getFacadeFactory().getRegistroNegativacaoCobrancaContaReceberItemFacade().carregarHistoricoNegativacaoContaReceber(obj.getCodigo()));
			obj.setNovoObj(Boolean.FALSE);
			if (!obj.getContaReceberRecebimentoVOs().isEmpty()) {
				obj.verificarUtilizacaoDesconto(obj.getContaReceberRecebimentoVOs().get(0).getDataRecebimeto());
			}
			setContaReceberVO(obj);
			MatriculaPeriodoVO matriculaPeriodo = getFacadeFactory().getMatriculaPeriodoFacade().consultarFinanceiroManulaPorChavePrimaria(obj.getMatriculaPeriodo().intValue(), getUsuarioLogado());
			if (matriculaPeriodo != null && matriculaPeriodo.getCodigo() != 0) {
				if (obj.getSituacao().equals("AR")) {
					if ((matriculaPeriodo.getFinanceiroManual()) || (obj.getContaEditadaManualmente())) {
						setFinanceiroManualMatriculaPeriodo(true);
						setApresentarRichModalFinanceiroManual(true);
					} else {
						setApresentarRichModalFinanceiroManual(true);
						setFinanceiroManualMatriculaPeriodo(false);
					}
				}
			}
			setNovaContaReceber(Boolean.FALSE);
			setLancadoOuAlteradoAlgumFollowUp(Boolean.FALSE);
			setCarregadoFollowUpConta(Boolean.FALSE);
			inicializarListasSelectItemTodosComboBox();
			verificarPermissaoApresentarBotaoAlterarDataVencimentoValorRecebimento();
			verificarPermissaoApresentarBotaoEditarManualmenteConta();
			verificarPermissaoReceberConta();
			verificarPermissaoImprimirContaRecebidaCanceladaRenegociada();
			// inicializarListaRecebimentos();
			this.setValor(obj.getValorBaseContaReceber());
			if (getLoginControle().getPermissaoAcessoMenuVO().getPermitirAlterarValorContaReceber()) {
				this.setValor(obj.getValorBaseContaReceber() + obj.getJuro() + obj.getMulta() + obj.getAcrescimo()+ obj.getValorIndiceReajustePorAtraso().doubleValue());
			}
			if (getContaReceberVO().getPagoComDCC()) {
				setModalPagoDCC("RichFaces.$('panelAvisoPagoDCC').show()");
			}
			setLancamentoContabilVO(new LancamentoContabilVO());
			setLancamentoContabilCentroNegocioVO(new LancamentoContabilCentroNegocioVO());
			setCentroResultadoOrigemVO(new CentroResultadoOrigemVO());
			validarCentroResultadoPorContaReceber();
			verificarPermissaoLancamentoContabilPagar();
			setMensagemID("msg_dados_editar");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			// System.out.println(e.getMessage());
		} finally {
			registrarAtividadeUsuario(getUsuarioLogado(), "ContaReceberControle", "Finalizando Editar Conta Receber", "Editar");
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("contaReceberForm.xhtml");
	}

	public void realizarEdicaoEEnvioEmailCobranca() {
		try {
			ContaReceberVO obj = (ContaReceberVO) context().getExternalContext().getRequestMap().get("contaReceberItens");
			getFacadeFactory().getContaReceberFacade().carregarDados(obj, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(obj.getUnidadeEnsino().getCodigo()), getUsuarioLogado());
			obj.setConfiguracaoFinanceiro(getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(obj.getUnidadeEnsino().getCodigo()));
			getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemCobrancaAlunoInadimplenteContaReceberEspecifica(obj);
			setMensagemID("msg_ContaReceber_cobrancaSucesso");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			// System.out.println(e.getMessage());
		}
	}

	public void realizarEnvioEmailCobranca() {
		try {
			getContaReceberVO().setConfiguracaoFinanceiro(getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getContaReceberVO().getUnidadeEnsino().getCodigo()));
			getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemCobrancaAlunoInadimplenteContaReceberEspecifica(getContaReceberVO());
			setMensagemID("msg_ContaReceber_cobrancaSucesso");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			// System.out.println(e.getMessage());
		}
	}

	public void editarContaVisaoAluno() {
		try {
			ContaReceberVO obj = (ContaReceberVO) context().getExternalContext().getRequestMap().get("contaAPagarItens");
			setContaReceberVisaoAluno(obj);
			getFacadeFactory().getContaReceberFacade().carregarDados(getContaReceberVisaoAluno(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(obj.getUnidadeEnsino().getCodigo()), getUsuarioLogado());
			setMensagemID("msg_dados_editar");
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public String receberConta() {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "ContaReceberControle", "Iniciando metodo receber conta Conta Receber", "Editar");
			ContaReceberVO contaReceber = (ContaReceberVO) context().getExternalContext().getRequestMap().get("contaReceberItens");
			if (contaReceber == null) {
				contaReceber = getContaReceberVO();
			}
			getFacadeFactory().getContaReceberFacade().validarContaReceberEmMemoriaAntesAlteracao(contaReceber);
			contaReceber.setRealizandoRecebimento(Boolean.TRUE);
			getFacadeFactory().getContaReceberFacade().carregarDados(contaReceber, NivelMontarDados.TODOS, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(contaReceberVO.getUnidadeEnsinoFinanceira().getCodigo()), getUsuarioLogado());			
			registrarAtividadeUsuario(getUsuarioLogado(), "ContaReceberControle", "Finalizando metodo receber conta Conta Receber", "Editar");
			removerControleMemoriaFlash("NegociacaoRecebimentoControle");
			removerControleMemoriaTela("NegociacaoRecebimentoControle");
			context().getExternalContext().getSessionMap().put("contaReceberItem", contaReceber);
			return Uteis.getCaminhoRedirecionamentoNavegacao("negociacaoRecebimentoForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return Uteis.getCaminhoRedirecionamentoNavegacao("");
		}
	}

	public void verificarDataVencimentoUtilizarDiaUtil(ContaReceberVO contaReceberVO) throws Exception {
		if (getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(contaReceberVO.getUnidadeEnsino().getCodigo()).getVencimentoParcelaDiaUtil()) {
			contaReceberVO.getDataOriginalVencimento();
			contaReceberVO.setDataVencimentoDiaUtil(getFacadeFactory().getContaReceberFacade().obterDataVerificandoDiaUtil(contaReceberVO.getDataVencimento(), contaReceberVO.getUnidadeEnsino().getCidade().getCodigo(), getUsuarioLogado()));
		}
	}

	public void inicializarListaRecebimentos() throws Exception {
		setListaRecebimentos(getFacadeFactory().getRecebimentoFacade().consultarPorCodigoContaReceber(contaReceberVO.getCodigo(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
	}

	public String recebimento() throws Exception {
		try {
			if (!this.getContaReceberVO().getSituacao().equals("RE")) {
				if (contaReceberVO.isNovoObj().booleanValue()) {
					getFacadeFactory().getContaReceberFacade().incluir(contaReceberVO, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(contaReceberVO.getUnidadeEnsino().getCodigo()), getUsuarioLogado());
				} else {
					getFacadeFactory().getContaReceberFacade().alterar(contaReceberVO, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(contaReceberVO.getUnidadeEnsino().getCodigo()), true, getUsuarioLogado());
					getFacadeFactory().getContaReceberFacade().atualizarAtributoUpdatedContaReceberAposAlteracaoPeloSistemaGarantindoAssimIntegridade(contaReceberVO);
				}
				RecebimentoControle recebimentoControle = (RecebimentoControle) context().getExternalContext().getSessionMap().get("RecebimentoControle");
				if (recebimentoControle != null) {
					recebimentoControle.setRecebimentoVO(new RecebimentoVO());
					recebimentoControle.getRecebimentoVO().setContaReceber(getContaReceberVO());
					recebimentoControle.montarDadosContaReceberRecebimento();
				}
				return "recebimento";
			}
			setMensagemDetalhada("Esta Conta Já foi Paga");
			return Uteis.getCaminhoRedirecionamentoNavegacao("contaReceberForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("contaReceberForm.xhtml");
		}
	}

	public void executarVisualizacaoFichaAluno() {
		try {
			ContaReceberVO obj = (ContaReceberVO) context().getExternalContext().getRequestMap().get("contaReceberItens");
			AlunoControle alunoControle = (AlunoControle) getControlador("AlunoControle");
			getFacadeFactory().getPessoaFacade().carregarDados(obj.getPessoa(), getUsuarioLogado());
			alunoControle.executarVisualizacaoFichaAlunoMontarFoto(obj.getPessoa());
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public void inicializarConfiguracaoFinanceiro() throws Exception {
		// ConfiguracaoFinanceiroVO obj =
		// getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS,
		// getUsuarioLogado(), null);
		// if (obj != null) {
		ConfiguracaoFinanceiroVO cfgFinanceira = getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getContaReceberVO().getUnidadeEnsino().getCodigo());
		this.setConfiguracaoFinanceiroVO(cfgFinanceira);
	}

	public void inicializarJurosEMultasConfiguracaoFinanceiro() {
		getContaReceberVO().setJuroPorcentagem(this.getConfiguracaoFinanceiroVO().getPercentualJuroPadrao());
		getContaReceberVO().setMultaPorcentagem(this.getConfiguracaoFinanceiroVO().getPercentualMultaPadrao());
	}

	public String atualizarLinhaDigitavelSemGerarNossoNumero() {
		try {
			getFacadeFactory().getContaReceberFacade().gerarLinhaDigitavelSemGerarNossoNumero(this.getContaReceberVO(), getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			setMensagemID("msg_dados_boletosRegerados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("contaReceberForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("contaReceberForm.xhtml");
		}
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>ContaReceber</code>. Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de erro.
	 */
	public String gravar() {
		String tipoOrigem = contaReceberVO.getTipoOrigem();
		try {
			if (Uteis.isAtributoPreenchido(getContaReceberVO().getProcessamentoIntegracaoFinanceiraDetalheVO().getCodigo())) {
				throw new Exception("Não é possível realizar a alteração de uma conta de integração financeira.");
			}
			if (getContaReceberVO().getSituacaoAReceber() && !Uteis.isAtributoPreenchido(getContaReceberVO().getValorReceberCalculado())) {
				throw new Exception("Não é possível alterar a conta a receber, pois o valor dos descontos é maior ou igual a conta a receber. Realize o recebimento para baixa do boleto!");
			}
			executarValidacaoSimulacaoVisaoAluno();
			if (contaReceberVO.isNovoObj().booleanValue()) {
				registrarAtividadeUsuario(getUsuarioLogado(), "ContaReceberControle", "Inicializando Incluir Conta Receber", "Incluindo");
				getFacadeFactory().getContaReceberFacade().incluir(contaReceberVO, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getContaReceberVO().getUnidadeEnsinoFinanceira().getCodigo()), getUsuarioLogado());
				registrarAtividadeUsuario(getUsuarioLogado(), "ContaReceberControle", "Finalizando metodo receber conta Conta Receber", "Incluindo");
			} else {
				registrarAtividadeUsuario(getUsuarioLogado(), "ContaReceberControle", "Iniciando Alterar Conta Receber", "Alterando");
				getFacadeFactory().getContaReceberFacade().alterar(contaReceberVO, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getContaReceberVO().getUnidadeEnsinoFinanceira().getCodigo()), true, getUsuarioLogado());
				/*if(contaReceberVO.getTipoOrigem().equals("IPS")) {					
					getFacadeFactory().getContaReceberFacade().gerarDadosBoleto(contaReceberVO, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getContaReceberVO().getUnidadeEnsinoFinanceira().getCodigo()), true,true, getUsuarioLogado());
				}else {
					getFacadeFactory().getContaReceberFacade().gerarDadosBoleto(contaReceberVO, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getContaReceberVO().getUnidadeEnsinoFinanceira().getCodigo()), true, getUsuarioLogado());
				}*/
				getFacadeFactory().getContaReceberFacade().gerarDadosBoleto(contaReceberVO, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getContaReceberVO().getUnidadeEnsinoFinanceira().getCodigo()), true, getUsuarioLogado());
				getFacadeFactory().getMatriculaFacade().realizarVerificacaoELiberacaoSuspensaoMatricula(getContaReceberVO().getMatriculaAluno().getMatricula());
				registrarAtividadeUsuario(getUsuarioLogado(), "ContaReceberControle", "Finalizando Alterar Conta Receber", "Alterando");
			}
			getFacadeFactory().getContaReceberFacade().atualizarAtributoUpdatedContaReceberAposAlteracaoPeloSistemaGarantindoAssimIntegridade(getContaReceberVO());
			if (this.getLancadoOuAlteradoAlgumFollowUp()) {
				getFacadeFactory().getInteracaoWorkflowFacade().persistirListaInteracaoWorkflow(getContaReceberVO().getListaFollowUpConta(), false, getUsuarioLogado());
				this.setLancadoOuAlteradoAlgumFollowUp(Boolean.FALSE);
			}
			getContaReceberVO().reiniciarControleBloqueioCompetencia();
			getContaReceberVO().setDataVencimentoAntesAlteracao(getContaReceberVO().getDataVencimento());
			verificarContaEditadaManualmenteEExistePlanoDescontoReplicarOutrasContas();
			setMensagemID("msg_dados_gravados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("contaReceberForm.xhtml");
		} catch (Exception e) {
			contaReceberVO.setTipoOrigem(tipoOrigem);
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("contaReceberForm.xhtml");
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP ContaReceberCons.jsp. Define o tipo de consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
	 */
	// public void scrollerListener(DataScrollEvent DataScrollEvent) throws
	// Exception {
	// getControleConsultaOtimizado().setPagina(DataScrollEvent.getPage());
	// consultar();
	// }
	@Override
	public void anularDataModelo() {
		setControleConsultaOtimizado(null);
	}

	// TODO CONSULTAR
	@Override
	public String consultar() {
		try {
			if (getFacadeFactory().getIntegracaoFinanceiroFacade().realizarVerificacaoProcessamentoIntegracaoFinanceira() && getConfiguracaoFinanceiroPadraoSistema().getUtilizarIntegracaoFinanceira()) {
				throw new Exception("Prezado, a emissão dos boletos estão indisponíveis temporariamente, tente mais tarde.");
			}
			getControleConsultaOtimizado().getListaConsulta().clear();
			registrarAtividadeUsuario(getUsuarioLogado(), "ContaReceberControle", "Iniciando Consultar Conta Receber", "Consultando");
			super.consultar();
			getControleConsultaOtimizado().setLimitePorPagina(10);
			List<ContaReceberVO> objs = new ArrayList<ContaReceberVO>(0);
			if (!getConsultaPainelGestorFinanceiro()) {
				objs = getFacadeFactory().getContaReceberFacade().consultar(getControleConsulta(), getConsDataCompetencia(), getValorConsultaUnidadeEnsino(), getAno(), getSemestre(), getConfiguracaoFinanceiroPadraoSistema(), getContaReceberVO(), getTipoOrigem(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getSituacoes(), getUsuarioLogado(), getFiltroRelatorioFinanceiroVO(), buscaFiltroSelecionado());
//				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getContaReceberFacade().consultarTotalRegistros(getControleConsulta(), getConsDataCompetencia(), getValorConsultaUnidadeEnsino(), getAno(), getSemestre(), getConfiguracaoFinanceiroPadraoSistema(), getContaReceberVO(), getTipoOrigem(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getSituacoes(), getUsuarioLogado(), getFiltroRelatorioFinanceiroVO(), buscaFiltroSelecionado()));
					calcularTotalReceberTotalRecebido();
			} else {
//				objs = getFacadeFactory().getContaReceberFacade().consultaRapidaPorSituacaoUnidadeEnsino(getValorUnidadeEnsino(), getSituacaoConsultaPainelGestorFinanceiro(), new Date(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), getTipoOrigem(), false, getUsuarioLogado(), buscaFiltroSelecionado());
//				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getContaReceberFacade().consultaRapidaPorSituacaoUnidadeEnsinoTotalRegistros(getValorUnidadeEnsino(), getSituacaoConsultaPainelGestorFinanceiro(), new Date(), getTipoOrigem(), false, getUsuarioLogado(), buscaFiltroSelecionado()));
//				calcularTotalReceberTotalRecebido();
			}
			
			getControleConsultaOtimizado().setListaConsulta(objs);
			getFiltroRelatorioFinanceiroVO().setMaximixado(false);
			setMensagemID("msg_dados_consultados");
			registrarAtividadeUsuario(getUsuarioLogado(), "ContaReceberControle", "Finalizando Consultar Conta Receber", "Consultando");
			return Uteis.getCaminhoRedirecionamentoNavegacao("contaReceberCons.xhtml");
		} catch (Exception e) {
			getControleConsultaOtimizado().getListaConsulta().clear();
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("contaReceberCons.xhtml");
		}
	}

	public void scrollerListener(DataScrollEvent DataScrollEvent) throws Exception {
		getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
		getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
		setConsultaDataScroller(true);
		consultar();
	}
	
	public void scrollerListenerVisaoAluno(DataScrollEvent DataScrollEvent) throws Exception {
		getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
		getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
		setConsultaDataScroller(true);
		VisaoAlunoControle visao = (VisaoAlunoControle) context().getExternalContext().getSessionMap().get("VisaoAlunoControle");
		consultarContasAPagarVisaoAluno(visao.getMatricula().getMatricula());
	}

	public void calcularTotalReceberTotalRecebido() throws Exception {
		try {
			if (!getConsultaDataScroller()) {
				setTotalReceber(0.0);
				setTotalRecebido(0.0);
				HashMap<String, Double> totalReceberTotalRecebido = null;
				Date dataInicio = getControleConsulta().getDataIni();
				Date dataFinal = getControleConsulta().getDataFim();

				if (getControleConsulta().getBuscarPeriodoCompleto()) {
					dataInicio = Uteis.getDate("01/01/1980");
					dataFinal = Uteis.getDate("01/01/2100");
				}
				if (getConsDataCompetencia()) {
					dataInicio = Uteis.getDataPrimeiroDiaMes(dataInicio);
					dataFinal = Uteis.getDataUltimoDiaMes(dataFinal);
				}

				if (getControleConsulta().getCampoConsulta().equals("identificadorCentroReceitaCentroReceita") && !contaReceberVO.getCentroReceita().getIdentificadorCentroReceita().equals("")) {
					getControleConsulta().setValorConsulta(contaReceberVO.getCentroReceita().getIdentificadorCentroReceita());
				}

				totalReceberTotalRecebido = getFacadeFactory().getContaReceberFacade().consultaTotalContaReceber(getControleConsulta().getCampoConsulta(), getControleConsulta().getValorConsulta(),
						ano, semestre, consDataCompetencia, dataInicio, dataFinal, getSituacoes(), getValorConsultaUnidadeEnsino(),
						true, getUsuarioLogado(), filtroRelatorioFinanceiroVO);

				if (totalReceberTotalRecebido != null) {
					setTotalReceber(totalReceberTotalRecebido.get("valorAReceber"));
					setTotalRecebido(totalReceberTotalRecebido.get("valorRecebido"));
					setTotalCancelado(totalReceberTotalRecebido.get("valorCancelado"));
					setTotalNegociado(totalReceberTotalRecebido.get("valorNegociado"));
					getControleConsultaOtimizado().setTotalRegistrosEncontrados(totalReceberTotalRecebido.get("qtde").intValue());
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public boolean getIsApresentarCamposDatasBuscarTodoPeriodo() {
		return !(getControleConsulta().getCampoConsulta().equals("codigo")
				|| getControleConsulta().getCampoConsulta().equals("nossoNumero")
				|| getControleConsulta().getCampoConsulta().equals("nrDocumento"));
	}

	public boolean getIsApresentarCamposDatas() {
		return !(getControleConsulta().getCampoConsulta().equals("codigo")
				|| getControleConsulta().getCampoConsulta().equals("nossoNumero")
				|| getControleConsulta().getCampoConsulta().equals("nrDocumento")
				|| getControleConsulta().getBuscarPeriodoCompleto());
	}

	public boolean getIsApresentarCamposCentroReceita() {
		return (getControleConsulta().getCampoConsulta().equals("identificadorCentroReceitaCentroReceita"));
	}

	public boolean getIsApresentarCamposSituacaoETipoOrigem() {
		return (!getControleConsulta().getCampoConsulta().equals("codigo") && !getControleConsulta().getCampoConsulta().equals("codigobarra")
				&& !getControleConsulta().getCampoConsulta().equals("nossoNumero") && !getControleConsulta().getCampoConsulta().equals("nrDocumento"));
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe <code>ContaReceberVO</code> Após a exclusão ela automaticamente aciona a rotina para uma nova inclusão.
	 */
	public String excluir() {
		Boolean verificarPermissao = Boolean.TRUE;
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "ContaReceberControle", "Iniciando Excluir Conta Receber", "Excluindo");
			if (contaReceberVO.getTipoOrigem().equals("REQ") ) {
				throw new Exception("Não é possível realizar a exclusão da conta a receber, a mesma é referente a um requerimento, sendo assim é necessário realizar a exclusão do requerimento e assim a conta a receber será excluída automaticamente!");
			}		
			if (Uteis.isAtributoPreenchido(contaReceberVO.getNotaFiscalSaidaServicoVO().getCodigo())) {
				throw new Exception("Não é possível realizar a exclusão da conta a receber, a mesma possui uma nota fiscal vinculada !");
			}
			if (contaReceberVO.getSituacao().equals("AR")) {

				MatriculaPeriodoVencimentoVO matriculaPeriodoVencimentoVO;
				try {
					matriculaPeriodoVencimentoVO = getFacadeFactory().getMatriculaPeriodoVencimentoFacade().consultarPorContaReceber(getContaReceberVO().getCodigo().intValue(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(contaReceberVO.getUnidadeEnsino().getCodigo()), getUsuarioLogado());
					getFacadeFactory().getContaReceberFacade().excluir(contaReceberVO, matriculaPeriodoVencimentoVO, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(contaReceberVO.getUnidadeEnsino().getCodigo()), verificarPermissao, getUsuarioLogado());
				} catch (Exception e) {
					getFacadeFactory().getContaReceberFacade().excluir(contaReceberVO, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(contaReceberVO.getUnidadeEnsino().getCodigo()), true, getUsuarioLogado());
				}
				getFacadeFactory().getMatriculaFacade().realizarVerificacaoELiberacaoSuspensaoMatricula(getContaReceberVO().getMatriculaAluno().getMatricula());
				setContaReceberVO(new ContaReceberVO());
				setMensagemID("msg_dados_excluidos");
				novo();

			} else if (contaReceberVO.getSituacao().equals(SituacaoContaReceber.RECEBIDO.getValor())) {
				if (!getFacadeFactory().getContaReceberFacade().excluirContaReceberRecebidaSemRegistroRecebimento(contaReceberVO, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(contaReceberVO.getUnidadeEnsino().getCodigo()), true, getUsuarioLogado())) {
					setMensagemDetalhada("Não é possivel Excluir uma Conta a Receber já Paga. Pois há registro de Recebimento da mesma.");
				} else {
					setContaReceberVO(new ContaReceberVO());
					setMensagemID("msg_dados_excluidos");
				}
			} else {
				setMensagemDetalhada("Não é possivel Excluir uma Conta a Receber já Paga.");
			}
			registrarAtividadeUsuario(getUsuarioLogado(), "ContaReceberControle", "Finalizando Excluir Conta Receber", "Excluindo");
			return Uteis.getCaminhoRedirecionamentoNavegacao("contaReceberForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("contaReceberForm.xhtml");
		}
	}

	public void consultarParceiro() {
		try {
			super.consultar();
			List objs = new ArrayList(0);
			if (getCampoConsultaParceiro().equals("codigo")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getParceiroFacade().consultarPorCodigo(new Integer(valorInt), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
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
			setListaConsulta(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarHistoricoNeg() {
		RegistroNegativacaoCobrancaContaReceberItemVO obj = (RegistroNegativacaoCobrancaContaReceberItemVO) context().getExternalContext().getRequestMap().get("historicoNegativacao");
		this.setMotivoNeg(obj.getMotivo());
	}

	public void selecionarFornecedor() {
		FornecedorVO obj = (FornecedorVO) context().getExternalContext().getRequestMap().get("fornecedorItens");
		this.getContaReceberVO().setFornecedor(obj);
	}

	private List<SelectItem> tipoConsultaComboFornecedor;

	public String getMascaraConsultaFornecedor() {
		if (getCampoConsultaFornecedor().equals("CNPJ")) {
			return "return mascara(this.form, 'formFornecedor:valorConsultaFornecedor', '99.999.999/9999-99', event);";
		} else if (getCampoConsultaFornecedor().equals("CPF")) {
			return "return mascara(this.form, 'formFornecedor:valorConsultaFornecedor', '999.999.999-99', event);";
		}
		return "";
	}

	public List<SelectItem> getTipoConsultaComboFornecedor() {
		if (tipoConsultaComboFornecedor == null) {
			tipoConsultaComboFornecedor = new ArrayList<SelectItem>(0);
			tipoConsultaComboFornecedor.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboFornecedor.add(new SelectItem("razaoSocial", "Razão Social"));
			tipoConsultaComboFornecedor.add(new SelectItem("CPF", "CPF"));
			tipoConsultaComboFornecedor.add(new SelectItem("CNPJ", "CNPJ"));
			tipoConsultaComboFornecedor.add(new SelectItem("CNPJ", "CNPJ"));
			tipoConsultaComboFornecedor.add(new SelectItem("RG", "RG"));
		}
		return tipoConsultaComboFornecedor;
	}

	public void verificarPermicaoCriarContaAluno() {
		if (getContaReceberVO().getTipoPessoa().equals("AL")) {
			try {
				if (getConfiguracaoFinanceiroPadraoSistema().getPermitirGerarParcelaPreMatricula()) {
					setApresentarMsgPreMatricula("");
				} else {
					boolean possuiPreMatricula = getFacadeFactory().getMatriculaPeriodoFacade().verificaPossuiMatriculaPreMatricula(getContaReceberVO().getMatriculaAluno().getMatricula());
					if (possuiPreMatricula) {
						getContaReceberVO().setTipoPessoa("");
						getContaReceberVO().setMatriculaAluno(new MatriculaVO());
						getContaReceberVO().getPessoa().setCodigo(new Integer(0));
						setApresentarMsgPreMatricula("RichFaces.$('panelMsgPreMatricula').show()");
					} else {
						setApresentarMsgPreMatricula("");
					}
				}
			} catch (Exception e) {
				setApresentarMsgPreMatricula("");
			}
		}
	}

	public void consultarFornecedor() {
		try {
			super.consultar();
			List<FornecedorVO> objs = new ArrayList<FornecedorVO>(0);
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
			setListaConsultaFornecedor(new ArrayList<FornecedorVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarParceiro() {
		try {
			ParceiroVO obj = (ParceiroVO) context().getExternalContext().getRequestMap().get("parceiroItens");
			getContaReceberVO().setParceiroVO(getFacadeFactory().getParceiroFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			if (getContaReceberVO().isContaReceberReferenteMaterialDidatico() && !getContaReceberVO().getParceiroVO().isCusteaParcelasMaterialDidatico()) {
				getContaReceberVO().setParceiroVO(new ParceiroVO());
				throw new Exception("O parceiro informado não custea parcela de material didático.");
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
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
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorNome(getValorConsultaFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("matricula")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorMatricula(getValorConsultaFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("nomeCidade")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeCidade(getValorConsultaFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("CPF")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorCPF(getValorConsultaFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
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

	@PostConstruct
	public void consultarContaPagarVisaoAluno() throws Exception {
		if(getUsuarioLogado().getIsApresentarVisaoAlunoOuPais()) {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "ContaReceberControle", "Inicializando Consultar Conta Pagar Visão Aluno", "Consultando");
			super.consultar();
			getControleConsultaOtimizado().setOffset(0);
			getControleConsultaOtimizado().setPage(0);
			getControleConsultaOtimizado().setPaginaAtual(0);
			VisaoAlunoControle visao = (VisaoAlunoControle) context().getExternalContext().getSessionMap().get("VisaoAlunoControle");
			if (getFacadeFactory().getIntegracaoFinanceiroFacade().realizarVerificacaoProcessamentoIntegracaoFinanceira() && getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getUnidadeEnsinoLogado().getCodigo()).getUtilizarIntegracaoFinanceira()) {
				throw new Exception("Prezado, a emissão dos boletos está indisponível temporariamente, tente mais tarde.");
			}
			
			if (visao != null) {
				visao.inicializarMenuMinhasContasAPagar();
				getCursoVO().setCodigo(visao.getMatricula().getCurso().getCodigo());
				setConfiguracaoRecebimentoCartaoOnlineVO(new ConfiguracaoRecebimentoCartaoOnlineVO());
				consultarContasAPagarVisaoAluno(visao.getMatricula().getMatricula());
//				setConfiguracaoRecebimentoCartaoOnlineVOs(getFacadeFactory().getContaReceberFacade()
//					.realizarVerificacaoPermiteRecebimentoOnlineUsarMinhasContasVisaoAluno(visao.getMatricula().getMatricula(), getListaContasPagarVisaoAluno(), getUsuarioLogado()));
			}
			registrarAtividadeUsuario(getUsuarioLogado(), "ContaReceberControle", "Finalizando Consultar Conta Pagar Visão Aluno", "Consultando");

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
//		return Uteis.getCaminhoRedirecionamentoNavegacao("minhasContasPagarAluno");
		}
	}

	public void consultarContasAPagarVisaoAluno(String matricula) throws Exception {
		try {
			getControleConsultaOtimizado().getListaConsulta().clear();
			getControleConsultaOtimizado().setLimitePorPagina(10);
			getListaContasPagarVisaoAluno().clear();
			getListaContasPagarParceiroVisaoAluno().clear();
			getListaContasPagarVisaoAluno().addAll(getFacadeFactory().getContaReceberFacade().consultarContasAPagarVisaoAluno(getConfiguracaoGeralPadraoSistema(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getUnidadeEnsinoLogado().getCodigo()), getCursoVO().getCodigo(), matricula, getTipoContasAPagar(), getRealizarValidacaoParaObterQualSeraUsuarioCorrente(getUsuarioLogado()), getConfiguracaoRecebimentoCartaoOnlineVOs(), 
					Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado(), true, getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), getControleConsultaOtimizado()));
			if(isPermitirApresentarFinanciamentoEstudantil()){
				getListaContasPagarParceiroVisaoAluno().addAll(getFacadeFactory().getContaReceberFacade().consultarConvenioFinanciamentoProprioVisaoAluno(getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getUnidadeEnsinoLogado().getCodigo()), matricula, getRealizarValidacaoParaObterQualSeraUsuarioCorrente(getUsuarioLogado()), getUnidadeEnsinoLogado().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado()));
			}
			getControleConsultaOtimizado().setListaConsulta(getListaContasPagarVisaoAluno());
			realizarMontagemTotalizadoresContaReceber();
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public Double getTotalParceiroFinanciamentoProprioValorBase() {
		return getListaContasPagarParceiroVisaoAluno().stream().map(p -> p.getValor()).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b));
	}
	
	public Double getTotalParceiroFinanciamentoProprioValorFinal() {
		return getListaContasPagarParceiroVisaoAluno().stream().map(p -> p.getValorTotalAPagarNaDataAtual()).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b));
	}
	
	public Double getTotalParceiroFinanciamentoProprioCancelado() {
		return getListaContasPagarParceiroVisaoAluno().stream().filter(p-> p.getSituacao().equals("CF")).map(p -> p.getValor()).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b));
	}
	
	public Double getTotalParceiroFinanciamentoProprioNegociado() {
		return getListaContasPagarParceiroVisaoAluno().stream().filter(p-> p.getSituacao().equals("NE")).map(p -> p.getValor()).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b));
	}
	
	public Double getTotalParceiroFinanciamentoProprioPago() {
		return getListaContasPagarParceiroVisaoAluno().stream().filter(p-> p.getSituacao().equals("RE")).map(p -> p.getValorTotalAPagarNaDataAtual()).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b));
	}

	public void editarContaReceberVisaoAluno() {
		ContaReceberVO obj = (ContaReceberVO) context().getExternalContext().getRequestMap().get("contaAPagarItens");
		setContaReceberVisaoAluno(obj);
	}

	@SuppressWarnings("UseOfObsoleteCollectionType")
	public List getListaSelectItemTipoContasAPagar() throws Exception {
		List objs = new ArrayList(0);
		Hashtable tipos = (Hashtable) Dominios.getTipoContasAPagar();
		Enumeration keys = tipos.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) tipos.get(value);
			objs.add(new SelectItem(value, label));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		return objs;
	}

	public String getBoleto() {
		if (getApresentarImprimirBoleto()) {
			return "abrirPopup('BoletoBancarioSV?codigoContaReceber=" + getContaReceberVisaoAluno().getCodigo() + "&titulo=matricula&tipoBoleto=" + getTipoBoleto() + "', 'boletoMatricula', 780, 585)";
		} else {
			return "";
		}
	}

	public Boolean verificarUsuarioPossuiPermissaoImprimirBoletoBloqueadoMatriculaAlunoComDebitos() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("ContaReceber_permitirImpressaoBoletoBloqueadoMatriculaAlunoComDebitos", getUsuarioLogado());
			return Boolean.TRUE;
		} catch (Exception e) {
		}
		return Boolean.FALSE;
	}

	public Boolean veriricarAlunoPossuiContasReceberEmAberto(ContaReceberVO obj) {
		try {
			List<ContaReceberVO> contasAReceber = getFacadeFactory().getContaReceberFacade().consultaRapidaPorAluno(obj.getMatriculaAluno().getMatricula(), null, null, "AR", 0, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			for (ContaReceberVO conta : contasAReceber) {
				if (!conta.getCodigo().equals(obj.getCodigo())) {
					// se entrar aqui é por que existe ao menos uma conta a receber
					// com a situacao AR diferente da qual o aluno esta imprimindo.
					// Logo, como o bloqueio para impressao do boleto da matricula
					// está ativado, deve ser retornado true para ver se o usuario
					// possui permissão para imprimir o mesmo.
					return Boolean.TRUE;
				}
			}
		} catch (Exception e) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public void selecionarContaEmissao() throws Exception {
		ContaReceberVO obj = (ContaReceberVO) context().getExternalContext().getRequestMap().get("contaReceber");
		setContaReceberVO(obj);
	}

	public void imprimirBoleto() throws Exception {
		ContaReceberVO obj = (ContaReceberVO) context().getExternalContext().getRequestMap().get("contaReceberItens");
		if (obj.getSituacaoEQuitada() && !getPermiteImprimirBoletoRecebido()) {
			setMensagemDetalhada("msg_erro", "Não é possível emitir o boleto de uma parcela já paga!");
		}
		if ((obj.getContaReceberReferenteMatricula()) &&
				(getConfiguracaoFinanceiroPadraoSistema().getBloquearPagarMatriculaRenovacaoVisaoAlunoInadimplente())) {
			if (veriricarAlunoPossuiContasReceberEmAberto(obj)) {
				// se entrar aqui é por que o aluno possui contas a receber em aberto, diferente da parcela
				// de matrícula. Logo, como está se trabalhando com bloqueio (conforme configuracao financeira ->
				// BloquearPagarMatriculaRenovacaoVisaoAlunoInadimplente) entao temos que ver se o usuário tem permissão
				// para imprimir o boleto.
				if (!verificarUsuarioPossuiPermissaoImprimirBoletoBloqueadoMatriculaAlunoComDebitos()) {
					setMensagemDetalhada("msg_erro", "O boleto de matrícula está bloqueado para impressão pois este aluno possui pendências financeiras (vencidas ou a vencer).");
					setAbrirModalCobrarReimpressao("");
					return;
				}
			}
		}
		setContaReceberVO(obj);
		if (getConfiguracaoFinanceiroVO().getCodigo().equals(0)) {
			// setConfiguracaoFinanceiroVO(getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS,
			// getUsuarioLogado(), null));
			setConfiguracaoFinanceiroVO(getConfiguracaoFinanceiroPadraoSistema());
		}
		try {

			if (getConfiguracaoFinanceiroVO().getCobrarReimpressaoBoletos() && getContaReceberVO().getImpressaoBoletoRealizada()) {
				setAbrirModalCobrarReimpressao("RichFaces.$('panelCobrarReimpressao').show()");
			} else {
				setAbrirModalCobrarReimpressao("");
				setAbrirModalCobrarReimpressao(getBoletoConsultaContaReceber());
			}
			// Na conta corrente é permitido informar para bloquear emissão de boletos, esse metodo realiza a validação dessa emissão.
			if (getFacadeFactory().getContaReceberFacade().validaContaCorrenteBloqueadoEmissaoBoleto(getContaReceberVO().getCodigo())) {
				setAbrirModalCobrarReimpressao("");
				setAbrirModalCobrarReimpressao("");
				setMensagemDetalhada("msg_erro", "Conta a Receber vinculada a uma conta corrente bloqueada para emissão de boleto, realize a mudança de carteira ou reative a emissão de boleto dentro do cadastro da conta corrente!");
			}

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			obj = null;
		}
	}

	public void imprimirBoletoTelaEdicao() throws Exception {
		if (getContaReceberVO().getSituacaoEQuitada() && !getPermiteImprimirBoletoRecebido()) {
			setMensagemDetalhada("msg_erro", "Não é possível emitir o boleto de uma parcela já paga!");
		}
		if ((getContaReceberVO().getContaReceberReferenteMatricula()) &&
				(getConfiguracaoFinanceiroPadraoSistema().getBloquearPagarMatriculaRenovacaoVisaoAlunoInadimplente())) {
			if (veriricarAlunoPossuiContasReceberEmAberto(getContaReceberVO())) {
				// se entrar aqui é por que o aluno possui contas a receber em aberto, diferente da parcela
				// de matrícula. Logo, como está se trabalhando com bloqueio (conforme configuracao financeira ->
				// BloquearPagarMatriculaRenovacaoVisaoAlunoInadimplente) entao temos que ver se o usuário tem permissão
				// para imprimir o boleto.
				if (!verificarUsuarioPossuiPermissaoImprimirBoletoBloqueadoMatriculaAlunoComDebitos()) {
					setMensagemDetalhada("msg_erro", "O boleto de matrícula está bloqueado para impressão pois este aluno possui pendências financeiras (vencidas ou a vencer).");
					setAbrirModalCobrarReimpressao("");
					return;
				}
			}
		}
		if (getConfiguracaoFinanceiroVO().getCodigo().equals(0)) {
			// setConfiguracaoFinanceiroVO(getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS,
			// getUsuarioLogado(), null));
			setConfiguracaoFinanceiroVO(getConfiguracaoFinanceiroPadraoSistema());
		}
		try {
			if (getConfiguracaoFinanceiroVO().getCobrarReimpressaoBoletos() && getContaReceberVO().getImpressaoBoletoRealizada()) {
				setAbrirModalCobrarReimpressao("RichFaces.$('panelCobrarReimpressao').show()");
			} else {

				setAbrirModalCobrarReimpressao("RichFaces.$('panelEmissaoBoleto').show()");
				// setAbrirModalCobrarReimpressao(getBoletoConsultaContaReceber());
			}
			// Na conta corrente é permitido informar para bloquear emissão de boletos, esse metodo realiza a validação dessa emissão.
			if (getFacadeFactory().getContaReceberFacade().validaContaCorrenteBloqueadoEmissaoBoleto(getContaReceberVO().getCodigo())) {
				setAbrirModalCobrarReimpressao("");
				throw new Exception("Conta a Receber vinculada a uma conta corrente bloqueada para emissão de boleto, realize a mudança de carteira ou reative a emissão de boleto dentro do cadastro da conta corrente!");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void imprimirBoletoCriandoContaReimpressao() throws Exception {
		try {
			getFacadeFactory().getContaReceberFacade().carregarDados(getContaReceberVO(), getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			if (!getContaReceberVO().getTipoParceiro() && !getContaReceberVO().getTipoFornecedor()) {
				getFacadeFactory().getPessoaFacade().carregarDados(getContaReceberVO().getPessoa(), getUsuarioLogado());
				getFacadeFactory().getContaReceberFacade().criarContaReceber(null, null, getContaReceberVO().getPessoa(), getContaReceberVO().getUnidadeEnsino(), getConfiguracaoFinanceiroVO().getContaCorrenteReimpressaoBoletos(), 0, getContaReceberVO().getTipoOrigem(), new Date(), new Date(), getConfiguracaoFinanceiroVO().getValorCobrarReimpressaoBoletos().doubleValue(), getConfiguracaoFinanceiroVO().getCentroReceitaReimpressaoBoletos().getCodigo(), 1, 1, "OU", getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado(), null, "");
			} else if (getContaReceberVO().getTipoParceiro()) {
				getFacadeFactory().getContaReceberFacade().criarContaReceber(null, getContaReceberVO().getParceiroVO(), null, getContaReceberVO().getUnidadeEnsino(), getConfiguracaoFinanceiroVO().getContaCorrenteReimpressaoBoletos(), 0, getContaReceberVO().getTipoOrigem(), new Date(), new Date(), getConfiguracaoFinanceiroVO().getValorCobrarReimpressaoBoletos().doubleValue(), getConfiguracaoFinanceiroVO().getCentroReceitaReimpressaoBoletos().getCodigo(), 1, 1, "OU", getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado(), null, "");
			} else if (getContaReceberVO().getTipoFornecedor()) {
				getFacadeFactory().getContaReceberFacade().criarContaReceber(null, null, null, getContaReceberVO().getUnidadeEnsino(), getConfiguracaoFinanceiroVO().getContaCorrenteReimpressaoBoletos(), 0, getContaReceberVO().getTipoOrigem(), new Date(), new Date(), getConfiguracaoFinanceiroVO().getValorCobrarReimpressaoBoletos().doubleValue(), getConfiguracaoFinanceiroVO().getCentroReceitaReimpressaoBoletos().getCodigo(), 1, 1, "OU", getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado(), getContaReceberVO().getFornecedor(), "");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	// public String definirBoletoParaImpressao() {
	// ContaReceberVO obj = (ContaReceberVO)
	// context().getExternalContext().getRequestMap().get("contaReceber");
	// if (obj.getSituacaoEQuitada()) {
	// setMensagemDetalhada("msg_erro",
	// "Não é possível emitir o boleto de uma parcela já paga!");
	// }
	// setContaReceberVO(obj);
	// return "";
	// }
	public String definirBoletoParaImpressao() {
		return "RichFaces.$('panelCobrarReimpressao').hide()";
	}

	public String getBoletoConsultaContaReceber() {
		if (getApresentarImprimirBoletoConsultaContaReceber() || getPermiteImprimirBoletoRecebido()) {
			try {
				getContaReceberVO().setImpressaoBoletoRealizada(Boolean.TRUE);
				getFacadeFactory().getContaReceberFacade().alterarBooleanEmissaoBoletoRealizada(getContaReceberVO().getCodigo(), true, getUsuarioLogado());
				return "RichFaces.$('panelCobrarReimpressao').hide(); RichFaces.$('panelEmissaoBoleto').show()";
			} catch (Exception e) {
				return "";
			}
		} else {
			return "";
		}
	}

	public Boolean getApresentarImprimirBoletoConsultaContaReceber() {
		if (getContaReceberVO().getSituacaoEQuitada()) {
			return Boolean.FALSE;
		} else {
			return Boolean.TRUE;
		}
	}

	public Boolean getApresentarImprimirBoleto() {
		if (getContaReceberVisaoAluno().getSituacao().equalsIgnoreCase("AR")) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
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

	public void consultarAluno() {
		try {

			List objs = new ArrayList(0);
			if (getValorConsultaAluno().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			if (getCampoConsultaAluno().equals("matricula")) {
				objs.add(getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
			}
			if (getCampoConsultaAluno().equals("nomePessoa")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, "","", getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("nomeCurso")) {
				objs = getFacadeFactory().getMatriculaFacade().consultarPorNomeCurso(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("data")) {
				Date valorData = Uteis.getDate(getValorConsultaAluno());
				objs = getFacadeFactory().getMatriculaFacade().consultarPorData(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), this.getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("situacao")) {
				objs = getFacadeFactory().getMatriculaFacade().consultarPorSituacao(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("nomeResponsavel")) {
				objs = getFacadeFactory().getMatriculaFacade().consultarPorNomePessoa(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			}
			setListaConsultaAluno(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList(0));
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
				objs = getFacadeFactory().getPessoaFacade().consultarPorCodigo(new Integer(valorInt), "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaRequisitante().equals("nome")) {
				objs = getFacadeFactory().getPessoaFacade().consultarPorNome(getValorConsultaRequisitante(), "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}

			if (getCampoConsultaRequisitante().equals("nomeCidade")) {
				objs = getFacadeFactory().getPessoaFacade().consultarPorNomeCidade(getValorConsultaRequisitante(), "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaRequisitante().equals("CPF")) {
				objs = getFacadeFactory().getPessoaFacade().consultarPorCPF(getValorConsultaRequisitante(), "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaRequisitante().equals("RG")) {
				objs = getFacadeFactory().getPessoaFacade().consultarPorRG(getValorConsultaRequisitante(), "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaRequisitante().equals("necessidadesEspeciais")) {
				objs = getFacadeFactory().getPessoaFacade().consultarPorNecessidadesEspeciais(getValorConsultaRequisitante(), "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaRequisitante(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaRequisitante(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}

	@SuppressWarnings("UseOfObsoleteCollectionType")
	public List getListaSelectItemReceberRecebidoNegociado() throws Exception {
		List objs = new ArrayList(0);
		Hashtable listaSituacaoRecebimento = (Hashtable) Dominios.getReceberRecebidoNegociado();
		Enumeration keys = listaSituacaoRecebimento.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) listaSituacaoRecebimento.get(value);
			objs.add(new SelectItem(value, label));
		}
		objs.add(new SelectItem("NG", "Negociado"));
		objs.add(new SelectItem("CA", "Cancelado"));
		return objs;
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
				objs = getFacadeFactory().getPessoaFacade().consultarPorCodigo(new Integer(valorInt), "CA", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaCandidato().equals("nome")) {
				objs = getFacadeFactory().getPessoaFacade().consultarPorNome(getValorConsultaCandidato(), "CA", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}

			if (getCampoConsultaCandidato().equals("nomeCidade")) {
				objs = getFacadeFactory().getPessoaFacade().consultarPorNomeCidade(getValorConsultaCandidato(), "CA", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaCandidato().equals("CPF")) {
				objs = getFacadeFactory().getPessoaFacade().consultarPorCPF(getValorConsultaCandidato(), "CA", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaCandidato().equals("RG")) {
				objs = getFacadeFactory().getPessoaFacade().consultarPorRG(getValorConsultaCandidato(), "CA", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaCandidato().equals("necessidadesEspeciais")) {
				objs = getFacadeFactory().getPessoaFacade().consultarPorNecessidadesEspeciais(getValorConsultaCandidato(), "CA", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaCandidato(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCandidato(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}

	public void consultarCentroReceita() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaCentroReceita().equals("descricao")) {
				objs = getFacadeFactory().getCentroReceitaFacade().consultarPorDescricao(getValorConsultaCentroReceita(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultaCentroReceita().equals("identificadorCentroReceita")) {
				objs = getFacadeFactory().getCentroReceitaFacade().consultarPorIdentificadorCentroReceita(getValorConsultaCentroReceita(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultaCentroReceita().equals("nomeDepartamento")) {
				objs = getFacadeFactory().getCentroReceitaFacade().consultarPorNomeDepartamento(getValorConsultaCentroReceita(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			setListaConsultaCentroReceita(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCentroReceita(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void realizarVerificacaoRecebimentoTerceirizado() {
		try {
			getFacadeFactory().getContaReceberRecebimentoFacade().realizarMontagemDadosNegociacaoRecebimentoDeRecebimentoTerceirizado(getContaReceberVO().getContaReceberRecebimentoVOs(), getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List getComboSemestre() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("", ""));
		itens.add(new SelectItem("1", "1°"));
		itens.add(new SelectItem("2", "2°"));
		return itens;
	}

	public List getTipoConsultaComboCentroReceita() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("descricao", "Descrição"));
		itens.add(new SelectItem("identificadorCentroReceita", "Identificador Centro Receita"));
		itens.add(new SelectItem("nomeDepartamento", "Departamento"));
		return itens;
	}

	public List getTipoConsultaComboParceiro() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("razaoSocial", "Razão Social"));
		itens.add(new SelectItem("RG", "RG"));
		itens.add(new SelectItem("CPF", "CPF"));
		itens.add(new SelectItem("tipoParceiro", "Tipo Parceiro"));
		return itens;
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
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("CPF", "CPF"));
		itens.add(new SelectItem("cargo", "Cargo"));
		itens.add(new SelectItem("departamento", "Departamento"));
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

	public List getTipoConsultaComboCandidato() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("CPF", "CPF"));
		itens.add(new SelectItem("RG", "RG"));
		return itens;
	}

	public void selecionarFuncionario() {
		try {
			FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItens");
			this.getContaReceberVO().setFuncionario(obj);
			this.getContaReceberVO().getPessoa().setCodigo(obj.getPessoa().getCodigo());
			setUltimaContaReceberGeradaManualmente(getFacadeFactory().getContaReceberFacade().consultarNomeUltimaParcela(obj.getPessoa().getCodigo()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarResponsavelFinanceiro() {
		try {
			PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("responsavelFinanceiroItens");
			this.getContaReceberVO().setResponsavelFinanceiro(obj);
			setUltimaContaReceberGeradaManualmente(getFacadeFactory().getContaReceberFacade().consultarNomeUltimaParcela(obj.getCodigo()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarAlunoResponsavelFinanceiro() {
		try {
			limparMensagem();
			setListaConsultaAluno(getFacadeFactory().getMatriculaFacade().consultaRapidaBasicaPorCodigoResponsavelFinanceiro(getContaReceberVO().getResponsavelFinanceiro().getCodigo(), false, getUsuarioLogado()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void consultarAlunoParceiro() {
		try {
			limparMensagem();
			setListaConsultaAluno(getFacadeFactory().getMatriculaFacade().consultaRapidaBasicaPorCodigoResponsavelFinanceiro(getContaReceberVO().getResponsavelFinanceiro().getCodigo(), false, getUsuarioLogado()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void selecionarAluno() {
		try {
			MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("alunoItens");
			if(obj.getBloqueioPorSolicitacaoLiberacaoMatricula()) {
				throw new Exception(getMensagemInternalizacao("msg_RenovarMatricula_avisoMatriculaSuspensa"));
			}
			if (obj != null && !obj.getMatricula().equals("")) {
				vincularMatriculaVOContaReceber(obj);
				validarCentroResultadoPorContaReceber();
				inicializarListasSelectItemTodosComboBox();
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarCandidato() {
		try {
			PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("candidatoItens");
			this.getContaReceberVO().setCandidato(obj);
			this.getContaReceberVO().setPessoa(obj);
			setUltimaContaReceberGeradaManualmente(getFacadeFactory().getContaReceberFacade().consultarNomeUltimaParcela(obj.getCodigo()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

	}

	public void selecionarRequisitante() {
		PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("requerenteItens");
		this.getContaReceberVO().setPessoa(obj);
	}

	public void selecionarCentroReceita() {
		CentroReceitaVO obj = (CentroReceitaVO) context().getExternalContext().getRequestMap().get("centroReceitaItens");
		this.getContaReceberVO().setCentroReceita(obj);
	}

	public void consultarAlunoPorMatricula() {
		try {
			if (!getContaReceberVO().getMatriculaAluno().getMatricula().equals("")) {
				MatriculaVO objs = getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(getContaReceberVO().getMatriculaAluno().getMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
				if (!objs.getMatricula().equals("")) {
					if(objs.getBloqueioPorSolicitacaoLiberacaoMatricula()) {
						throw new Exception(getMensagemInternalizacao("msg_RenovarMatricula_avisoMatriculaSuspensa"));
					}
					vincularMatriculaVOContaReceber(objs);
					validarCentroResultadoPorContaReceber();
					inicializarListasSelectItemTodosComboBox();
					setMensagemID("msg_dados_consultados");
					return;
				}
				getContaReceberVO().getMatriculaAluno().setMatricula("");
				getContaReceberVO().getMatriculaAluno().getAluno().setNome("");
				setMensagemID("msg_erro_dadosnaoencontrados");
			}
		} catch (Exception e) {
			getContaReceberVO().getMatriculaAluno().setMatricula("");
			getContaReceberVO().getMatriculaAluno().getAluno().setNome("");
			setMensagemID("msg_erro_dadosnaoencontrados");
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarFuncionarioPorCodigo() {
		try {
			if (!this.getContaReceberVO().getFuncionario().getMatricula().equals("") && getContaReceberVO().getTipoPessoa().equals("FU")) {
				FuncionarioVO funcionario = getFacadeFactory().getFuncionarioFacade().consultarPorRequisitanteMatricula(this.getContaReceberVO().getFuncionario().getMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				if (funcionario.getCodigo().intValue() != 0) {
					this.getContaReceberVO().setFuncionario(funcionario);
					setUltimaContaReceberGeradaManualmente(getFacadeFactory().getContaReceberFacade().consultarNomeUltimaParcela(funcionario.getCodigo()));
					setMensagemID("msg_dados_consultados");
				} else {
					throw new Exception();
				}
			}
		} catch (Exception e) {
			getContaReceberVO().getFuncionario().setCodigo(0);
			getContaReceberVO().getFuncionario().setMatricula("");
			getContaReceberVO().getFuncionario().getPessoa().setNome("");
			setMensagemID("msg_erro_dadosnaoencontrados");
		}
	}

	public void consultarCandidatoPorCPF() {
		try {
			if (!this.getContaReceberVO().getCandidato().getCPF().equals("")) {
				PessoaVO pessoa = getFacadeFactory().getPessoaFacade().consultarPorCPFUnico(this.getContaReceberVO().getCandidato().getCPF(), 0, "CA", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				if (pessoa.getCodigo().intValue() != 0) {
					this.getContaReceberVO().setCandidato(pessoa);
					setUltimaContaReceberGeradaManualmente(getFacadeFactory().getContaReceberFacade().consultarNomeUltimaParcela(pessoa.getCodigo()));
					setMensagemID("msg_dados_consultados");
					return;
				}
				throw new Exception();
			}
		} catch (Exception e) {
			getContaReceberVO().getCandidato().setCodigo(0);
			getContaReceberVO().getCandidato().setNome("");
			getContaReceberVO().getCandidato().setCPF("");
			setMensagemID("msg_erro_dadosnaoencontrados");
		}
	}

	public void consultarRequerentePorCPF() {
		try {
			if (!this.getContaReceberVO().getPessoa().getCPF().equals("")) {
				PessoaVO pessoa = getFacadeFactory().getPessoaFacade().consultarPorCPFUnico(this.getContaReceberVO().getPessoa().getCPF(), 0, "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				if (pessoa.getCodigo().intValue() != 0) {
					this.getContaReceberVO().setPessoa(pessoa);
					setMensagemID("msg_dados_consultados");
					return;
				}
				throw new Exception();
			}
		} catch (Exception e) {
			getContaReceberVO().getPessoa().setCodigo(0);
			getContaReceberVO().getPessoa().setNome("");
			getContaReceberVO().getPessoa().setCPF("");
			setMensagemID("msg_erro_dadosnaoencontrados");
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

	@SuppressWarnings("UseOfObsoleteCollectionType")
	public List getListaSelectItemSituacaoContaReceber() throws Exception {
		List objs = new ArrayList(0);
		Hashtable receberRecebidoNegociado = (Hashtable) Dominios.getReceberRecebidoNegociado();
		Enumeration keys = receberRecebidoNegociado.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) receberRecebidoNegociado.get(value);
			objs.add(new SelectItem(value, label));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		return objs;
	}

	@SuppressWarnings("UseOfObsoleteCollectionType")
	public List getListaSelectItemTipoOrigemContaReceber() throws Exception {
		List objs = new ArrayList(0);
		objs.add(new SelectItem("", ""));
		Hashtable tipoOrigemContaReceber = (Hashtable) Dominios.getTipoOrigemContaReceber();
		Enumeration keys = tipoOrigemContaReceber.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) tipoOrigemContaReceber.get(value);
			objs.add(new SelectItem(value, label));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		return objs;
	}

	@SuppressWarnings("UseOfObsoleteCollectionType")
	public List getListaSelectItemAlunoFuncionarioCandidato() throws Exception {
		List objs = new ArrayList(0);
		objs.add(new SelectItem("", ""));
		if (getContaReceberVO().isContaReceberReferenteMaterialDidatico()) {
			objs.add(new SelectItem("AL", "Aluno"));
			objs.add(new SelectItem("PA", "Parceiro"));
			objs.add(new SelectItem("RF", "Responsável Financeiro"));
		} else {
			Hashtable alunoFuncionarioCandidato = (Hashtable) Dominios.getAlunoFuncionarioCandidatoParceiro();
			Enumeration keys = alunoFuncionarioCandidato.keys();
			while (keys.hasMoreElements()) {
				String value = (String) keys.nextElement();
				String label = (String) alunoFuncionarioCandidato.get(value);
				objs.add(new SelectItem(value, label));
			}
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		return objs;
	}

	public List<SelectItem> getListaSelectItemTipoBoleto() {
		List<SelectItem> objs = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoBoletoBancario.class);
		return objs;
	}

	public List<SelectItem> getListaSelectItemTipoOrigem() {
		List objs = new ArrayList(0);
		if (!Uteis.isAtributoPreenchido(getContaReceberVO())) {
			if (getContaReceberVO().getTipoAluno() || getContaReceberVO().getTipoResponsavelFinanceiro() || getContaReceberVO().getTipoFuncionario() || getContaReceberVO().getTipoRequerente()) {
				objs.add(new SelectItem(TipoOrigemContaReceber.BIBLIOTECA.getValor(), TipoOrigemContaReceber.BIBLIOTECA.getDescricao()));
			}
			objs.add(new SelectItem(TipoOrigemContaReceber.MATERIAL_DIDATICO.getValor(), TipoOrigemContaReceber.MATERIAL_DIDATICO.getDescricao()));
			objs.add(new SelectItem(TipoOrigemContaReceber.OUTROS.getValor(), TipoOrigemContaReceber.OUTROS.getDescricao()));
		} else {
			objs.add(new SelectItem(TipoOrigemContaReceber.getEnum(getContaReceberVO().getTipoOrigem()).getValor(), TipoOrigemContaReceber.getEnum(getContaReceberVO().getTipoOrigem()).getDescricao()));
		}
		return objs;
	}

	public void montarListaSelectItemContaCorrente(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		Integer unidadeEnsino = getContaReceberVO().getUnidadeEnsino().getCodigo();
		try {
			if (getContaReceberVO().isContaReceberReferenteMaterialDidatico()) {
				unidadeEnsino = getContaReceberVO().getUnidadeEnsinoFinanceira().getCodigo();
			}
			resultadoConsulta = consultarContaCorrentePorNome(unidadeEnsino);
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
			boolean existeConta = !Uteis.isAtributoPreenchido(getContaReceberVO().getContaCorrente());
			while (i.hasNext()) {
				ContaCorrenteVO obj = (ContaCorrenteVO) i.next();	
				if(obj.getCodigo().equals(getContaReceberVO().getContaCorrente())) {
					existeConta = true;
				}
				if (!obj.getContaCaixa().booleanValue() && (obj.getSituacao().equals("AT") || obj.getCodigo().equals(getContaReceberVO().getContaCorrente())) ) {
					if (Uteis.isAtributoPreenchido(obj.getNomeApresentacaoSistema())) {
						objs.add(new SelectItem(obj.getCodigo(), obj.getNomeApresentacaoSistema()));
					} else {
						objs.add(new SelectItem(obj.getCodigo(), "Banco:" + obj.getAgencia().getBanco().getNome() + " Ag:" + obj.getAgencia().getNumeroAgencia() + "-" + obj.getAgencia().getDigito() + " CC:" + obj.getNumero() + "-" + obj.getDigito() + " Carteira: " + obj.getCarteira()));
					}
				}
			}
			if(!existeConta) {
				if (Uteis.isAtributoPreenchido(getContaReceberVO().getContaCorrenteVO().getNomeApresentacaoSistema())) {
					objs.add(new SelectItem(getContaReceberVO().getContaCorrenteVO().getCodigo(), getContaReceberVO().getContaCorrenteVO().getNomeApresentacaoSistema()));
				} else {
					objs.add(new SelectItem(getContaReceberVO().getContaCorrenteVO().getCodigo(), "Banco:" + getContaReceberVO().getContaCorrenteVO().getAgencia().getBanco().getNome() + " Ag:" + getContaReceberVO().getContaCorrenteVO().getAgencia().getNumeroAgencia() + "-" + getContaReceberVO().getContaCorrenteVO().getAgencia().getDigito() + " CC:" + getContaReceberVO().getContaCorrenteVO().getNumero() + "-" + getContaReceberVO().getContaCorrenteVO().getDigito() + " Carteira: " + getContaReceberVO().getContaCorrenteVO().getCarteira()));
				}			
			}
			SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
			Collections.sort((List) objs, ordenador);
			setListaSelectItemContaCorrente(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public void montarListaSelectItemContaCorrente() throws Exception {
		try {
			montarListaSelectItemContaCorrente("");
		} catch (Exception e) {
			// System.out.println("Erro montarListaSelectItemContaCorrente: " + e.getMessage());
			throw e;
		}
	}

	public List consultarContaCorrentePorNome(Integer unidadeEnsino) throws Exception {
		List lista = getFacadeFactory().getContaCorrenteFacade().consultarPorCodigoSomenteContasCorrente(0, unidadeEnsino, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
		return lista;
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo <code>UnidadeEnsino</code>. Buscando todos os objetos correspondentes a entidade <code>UnidadeEnsino</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemUnidadeEnsino() throws Exception {
		List resultadoConsulta = null;
		try {
			if (getUsuarioLogado().getTipoUsuario().equals("FU")) {
				setListaSelectItemUnidadeEnsino(new ArrayList<SelectItem>());
				resultadoConsulta = consultarUnidadeEnsinoPorUsuarioLogado(getUsuarioLogadoClone());
				setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
				//getListaSelectItemUnidadeEnsino().add(new SelectItem(getUnidadeEnsinoLogado().getCodigo(), getUnidadeEnsinoLogado().getNome()));
				// setContaReceberVO(new ContaReceberVO());
				getContaReceberVO().setUnidadeEnsino(getUnidadeEnsinoLogadoClone());
				return;
			}
			resultadoConsulta = consultarUnidadeEnsinoPorNome("");
			setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
			
		} catch (Exception e) {
			// System.out.println("Erro montarListaSelectItemUnidadeEnsino: " + e.getMessage());
			throw e;
		}
	}

	public void montarListaSelectItemUnidadeEnsinoFinanceira() throws Exception {
		List resultadoConsulta = null;
		try {
			if (getUnidadeEnsinoLogado().getCodigo().intValue() != 0) {
				setListaSelectItemUnidadeEnsinoFinanceira(new ArrayList<SelectItem>());
				getListaSelectItemUnidadeEnsinoFinanceira().add(new SelectItem(getUnidadeEnsinoLogado().getCodigo(), getUnidadeEnsinoLogado().getNome()));
				getContaReceberVO().setUnidadeEnsinoFinanceira(getUnidadeEnsinoLogadoClone());
				return;
			}
			resultadoConsulta = consultarUnidadeEnsinoPorNome("");
			setListaSelectItemUnidadeEnsinoFinanceira(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
		} catch (Exception e) {
			// System.out.println("Erro montarListaSelectItemUnidadeEnsino: " + e.getMessage());
			throw e;
		}
	}

	// TODO Alberto 08/12/10 Acrescentado opção para permitir Aluno ver
	// ContasConvenio
	public void montarListaSelectItemCurso() throws Exception {
		try {
			List<MatriculaVO> listaMatricula = new ArrayList<MatriculaVO>(0);
			listaMatricula = getFacadeFactory().getMatriculaFacade().consultaRapidaPorCodigoPessoa(getUsuarioLogado().getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			setListaSelectItemCurso(new ArrayList<SelectItem>());
			getListaSelectItemCurso().add(new SelectItem(0, ""));
			for (MatriculaVO matricula : listaMatricula) {
				getListaSelectItemCurso().add(new SelectItem(matricula.getCurso().getCodigo(), matricula.getCurso().getNome()));
			}
		} catch (Exception e) {
			// System.out.println("Erro montarListaSelectItemCurso: " + e.getMessage());
			throw e;
		}
	}

	// TODO Alberto 08/12/10 Acrescentado opção para permitir Aluno ver
	// ContasConvenio
	/**
	 * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nome</code> Este atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox correspondente
	 */
	public List consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		List lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, 0, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		return lista;
	}
	
	public List consultarUnidadeEnsinoPorUsuarioLogado(UsuarioVO usuarioLogado) throws Exception {
		List lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorUsuario(usuarioLogado);
		return lista;
	}
	

	/**
	 * Método responsável por inicializar a lista de valores ( <code>SelectItem</code>) para todos os ComboBox's.
	 */
	public void inicializarListasSelectItemTodosComboBox() throws Exception {
		montarListaSelectItemContaCorrente();
		montarListaSelectItemUnidadeEnsino();
		montarListaSelectItemUnidadeEnsinoFinanceira();
		montarListaSelectItemCurso();
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List getTipoConsultaCombo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nomeSacado", "Nome Sacado"));
		itens.add(new SelectItem("aluno", "Matrícula Aluno"));
		itens.add(new SelectItem("alunoNome", "Nome Aluno"));
		itens.add(new SelectItem("anoSemestre", "Ano/Semestre"));
		itens.add(new SelectItem("responsavelFinanceiro", "Responsável Financeiro"));
		itens.add(new SelectItem("cpf", "CPF"));
		itens.add(new SelectItem("funcionario", "Matrícula Funcionário"));
		itens.add(new SelectItem("nrDocumento", "Nr. Documento"));
		itens.add(new SelectItem("nossoNumero", "Nosso Número"));
		itens.add(new SelectItem("codigobarra", "Código de Barra"));
		itens.add(new SelectItem("identificadorCentroReceitaCentroReceita", "Centro de Receita"));
		itens.add(new SelectItem("convenio", "Descrição Convênio"));
		itens.add(new SelectItem("fornecedor", "Nome Fornecedor"));
		itens.add(new SelectItem("codigoFinanceiroMatricula", "Código Financeiro Aluno"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	public boolean isCampoData() {
		if (getControleConsulta().getCampoConsulta().equals("data")) {
			return true;
		}
		return false;
	}

	public boolean getIsCampoAnoSemestre() {
		if (getControleConsulta().getCampoConsulta().equals("anoSemestre")) {
			return true;
		}
		return false;
	}

	public String getMascaraConsulta() {
		if (getControleConsulta().getCampoConsulta().equals("cpf")) {
			return "return mascara(this.form,'form:valorConsulta','999.999.999-99',event);";
		}
		return "";
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
	 */
	public String inicializarConsultar() {
		try {
			// novo();
			// getControleConsulta().setDataIni(Uteis.getDataPrimeiroDiaMes(new Date()));
			// getControleConsulta().setDataFim(Uteis.getDataUltimoDiaMes(new Date()));
//			getControleConsultaOtimizado().setListaConsulta(null);
			getControleConsultaOtimizado().getListaConsulta().clear();
			//limparSituacao();
			setMarcarTodos(false);		
			filtroRelatorioFinanceiroVO = new FiltroRelatorioFinanceiroVO(getLoginControle().getPermissaoAcessoMenuVO().getPermitirApenasContasDaBiblioteca());
			filtroRelatorioFinanceiroVO.realizarDesmarcarTodasSituacoes();
			filtroRelatorioFinanceiroVO.setSituacaoReceber(true);
			if(getSituacoes() == null) {
				setSituacoes(new ArrayList<String>(0));
				getSituacoes().add("AR");
			}
			realizarSelecaoCheckboxMarcarDesmarcarTodosTipoOrigem();
		} catch (Exception e) {
		}
		setListaConsulta(new ArrayList(0));
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("contaReceberCons.xhtml");
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

	public String getCampoConsultaAluno() {
		return campoConsultaAluno;
	}

	public void setCampoConsultaAluno(String campoConsultaAluno) {
		this.campoConsultaAluno = campoConsultaAluno;
	}

	public String getCampoConsultaCandidato() {
		return campoConsultaCandidato;
	}

	public void setCampoConsultaCandidato(String campoConsultaCandidato) {
		this.campoConsultaCandidato = campoConsultaCandidato;
	}

	public String getCampoConsultaCentroReceita() {
		return campoConsultaCentroReceita;
	}

	public void setCampoConsultaCentroReceita(String campoConsultaCentroReceita) {
		this.campoConsultaCentroReceita = campoConsultaCentroReceita;
	}

	public List getListaConsultaAluno() {
		return listaConsultaAluno;
	}

	public void setListaConsultaAluno(List listaConsultaAluno) {
		this.listaConsultaAluno = listaConsultaAluno;
	}

	public List getListaConsultaCandidato() {
		return listaConsultaCandidato;
	}

	public void setListaConsultaCandidato(List listaConsultaCandidato) {
		this.listaConsultaCandidato = listaConsultaCandidato;
	}

	public List getListaConsultaCentroReceita() {
		return listaConsultaCentroReceita;
	}

	public void setListaConsultaCentroReceita(List listaConsultaCentroReceita) {
		this.listaConsultaCentroReceita = listaConsultaCentroReceita;
	}

	public String getValorConsultaAluno() {
		return valorConsultaAluno;
	}

	public void setValorConsultaAluno(String valorConsultaAluno) {
		this.valorConsultaAluno = valorConsultaAluno;
	}

	public String getValorConsultaCandidato() {
		return valorConsultaCandidato;
	}

	public void setValorConsultaCandidato(String valorConsultaCandidato) {
		this.valorConsultaCandidato = valorConsultaCandidato;
	}

	public String getValorConsultaCentroReceita() {
		return valorConsultaCentroReceita;
	}

	public void setValorConsultaCentroReceita(String valorConsultaCentroReceita) {
		this.valorConsultaCentroReceita = valorConsultaCentroReceita;
	}

	public List getListaSelectItemFuncionario() {
		return (listaSelectItemFuncionario);
	}

	public void setListaSelectItemFuncionario(List listaSelectItemFuncionario) {
		this.listaSelectItemFuncionario = listaSelectItemFuncionario;
	}

	public List getListaSelectItemMatriculaAluno() {
		return (listaSelectItemMatriculaAluno);
	}

	public void setListaSelectItemMatriculaAluno(List listaSelectItemMatriculaAluno) {
		this.listaSelectItemMatriculaAluno = listaSelectItemMatriculaAluno;
	}

	// public Boolean getAluno() {
	// return aluno;
	// }
	//
	// public void setAluno(Boolean aluno) {
	// this.aluno = aluno;
	// }
	//
	// public Boolean getCandidato() {
	// return candidato;
	// }
	//
	// public void setCandidato(Boolean candidato) {
	// this.candidato = candidato;
	// }
	//
	// public Boolean getFuncionario() {
	// return funcionario;
	// }
	//
	// public void setFuncionario(Boolean funcionario) {
	// this.funcionario = funcionario;
	// }
	public ContaReceberVO getContaReceberVO() {
		if (contaReceberVO == null) {
			contaReceberVO = new ContaReceberVO();
		}
		return contaReceberVO;
	}

	public void setContaReceberVO(ContaReceberVO contaReceberVO) {
		this.contaReceberVO = contaReceberVO;
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

	public List getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList(0);
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public List getListaSelectItemUnidadeEnsinoFinanceira() {
		if (listaSelectItemUnidadeEnsinoFinanceira == null) {
			listaSelectItemUnidadeEnsinoFinanceira = new ArrayList(0);
		}
		return listaSelectItemUnidadeEnsinoFinanceira;
	}

	public void setListaSelectItemUnidadeEnsinoFinanceira(List listaSelectItemUnidadeEnsinoFinanceira) {
		this.listaSelectItemUnidadeEnsinoFinanceira = listaSelectItemUnidadeEnsinoFinanceira;
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

	public List getListaConsultaRequisitante() {
		if (listaConsultaRequisitante == null) {
			listaConsultaRequisitante = new ArrayList(0);
		}
		return listaConsultaRequisitante;
	}

	public void setListaConsultaRequisitante(List listaConsultaRequisitante) {
		this.listaConsultaRequisitante = listaConsultaRequisitante;
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

	public List<SelectItem> listaSelectItemTipoDesconto;

	public List<SelectItem> getListaSelectItemTipoDesconto() throws Exception {
		if (listaSelectItemTipoDesconto == null) {
			listaSelectItemTipoDesconto = new ArrayList<SelectItem>(0);
			listaSelectItemTipoDesconto.add(new SelectItem(TipoDescontoAluno.PORCENTO.getValor(), TipoDescontoAluno.PORCENTO.getSimbolo()));
			listaSelectItemTipoDesconto.add(new SelectItem(TipoDescontoAluno.VALOR.getValor(), TipoDescontoAluno.VALOR.getSimbolo()));
		}
		return listaSelectItemTipoDesconto;
	}

	@Override
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
		contaReceberVO = null;
		Uteis.liberarListaMemoria(listaSelectItemMatriculaAluno);
		Uteis.liberarListaMemoria(listaSelectItemFuncionario);
		Uteis.liberarListaMemoria(listaSelectItemUnidadeEnsino);
		recebimentoVO = null;
		Uteis.liberarListaMemoria(listaConsultaFuncionario);
		valorConsultaFuncionario = null;
		campoConsultaFuncionario = null;
		Uteis.liberarListaMemoria(listaConsultaAluno);
		valorConsultaAluno = null;
		campoConsultaAluno = null;
		Uteis.liberarListaMemoria(listaConsultaCandidato);
		valorConsultaCandidato = null;
		campoConsultaCandidato = null;
		Uteis.liberarListaMemoria(listaConsultaCentroReceita);
		Uteis.liberarListaMemoria(listaSelectItemContaCorrente);
		valorConsultaCentroReceita = null;
		campoConsultaCentroReceita = null;
		recebimentoVO = null;
	}

	/**
	 * @param listaSelectItemTipoContasAPagar
	 *            the listaSelectItemTipoContasAPagar to set
	 */
	public void setListaSelectItemTipoContasAPagar(List listaSelectItemTipoContasAPagar) {
		this.listaSelectItemTipoContasAPagar = listaSelectItemTipoContasAPagar;
	}

	/**
	 * @return the tipoContasAPagar
	 */
	public String getTipoContasAPagar() {
		if (tipoContasAPagar == null) {
			tipoContasAPagar = "";
		}
		return tipoContasAPagar;
	}

	/**
	 * @param tipoContasAPagar
	 *            the tipoContasAPagar to set
	 */
	public void setTipoContasAPagar(String tipoContasAPagar) {
		this.tipoContasAPagar = tipoContasAPagar;
	}

	public boolean isTipoContaApagarEstaPaga() {
		return getTipoContasAPagar().equals("PG") ? true : false;
	}

	/**
	 * @return the listaContasAPagar
	 */
	public List<ContaPagarVO> getListaContasAPagar() {
		if (listaContasAPagar == null) {
			listaContasAPagar = new ArrayList<ContaPagarVO>(0);
		}
		return listaContasAPagar;
	}

	/**
	 * @param listaContasAPagar
	 *            the listaContasAPagar to set
	 */
	public void setListaContasAPagar(List<ContaPagarVO> listaContasAPagar) {
		this.listaContasAPagar = listaContasAPagar;
	}

	/**
	 * @return the contaReceberVisaoAluno
	 */
	public ContaReceberVO getContaReceberVisaoAluno() {
		if (contaReceberVisaoAluno == null) {
			contaReceberVisaoAluno = new ContaReceberVO();
		}
		return contaReceberVisaoAluno;
	}

	/**
	 * @param contaReceberVisaoAluno
	 *            the contaReceberVisaoAluno to set
	 */
	public void setContaReceberVisaoAluno(ContaReceberVO contaReceberVisaoAluno) {
		this.contaReceberVisaoAluno = contaReceberVisaoAluno;
	}

	/**
	 * @return the listaRecebimentos
	 */
	public List<ContaReceberVO> getListaRecebimentos() {
		if (listaRecebimentos == null) {
			listaRecebimentos = new ArrayList<ContaReceberVO>(0);
		}
		return listaRecebimentos;
	}

	/**
	 * @param listaRecebimentos
	 *            the listaRecebimentos to set
	 */
	public void setListaRecebimentos(List<ContaReceberVO> listaRecebimentos) {
		this.listaRecebimentos = listaRecebimentos;
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
	 * @return the listaConsultaParceiro
	 */
	public List<ParceiroVO> getListaConsultaParceiro() {
		if (listaConsultaParceiro == null) {
			listaConsultaParceiro = new ArrayList<ParceiroVO>(0);
		}
		return listaConsultaParceiro;
	}

	/**
	 * @param listaConsultaParceiro
	 *            the listaConsultaParceiro to set
	 */
	public void setListaConsultaParceiro(List<ParceiroVO> listaConsultaParceiro) {
		this.listaConsultaParceiro = listaConsultaParceiro;
	}

	public Double getTotalReceber() {
		return totalReceber;
	}

	public void setTotalReceber(Double totalReceber) {
		this.totalReceber = totalReceber;
	}

	public Double getTotalRecebido() {
		return totalRecebido;
	}

	public void setTotalRecebido(Double totalRecebido) {
		this.totalRecebido = totalRecebido;
	}

	public Double getTotalCancelado() {
		return totalCancelado;
	}

	public void setTotalCancelado(Double totalCancelado) {
		this.totalCancelado = totalCancelado;
	}

	public Double getTotalNegociado() {
		return totalNegociado;
	}

	public void setTotalNegociado(Double totalNegociado) {
		this.totalNegociado = totalNegociado;
	}

	public List<ContaReceberVO> getListaContasPagarVisaoAluno() {
		if (listaContasPagarVisaoAluno == null) {
			listaContasPagarVisaoAluno = new ArrayList<ContaReceberVO>(0);
		}
		return listaContasPagarVisaoAluno;
	}

	public void setListaContasPagarVisaoAluno(List<ContaReceberVO> listaContasPagarVisaoAluno) {
		this.listaContasPagarVisaoAluno = listaContasPagarVisaoAluno;
	}

	public List<ContaReceberVO> getListaContasPagarParceiroVisaoAluno() {
		if (listaContasPagarParceiroVisaoAluno == null) {
			listaContasPagarParceiroVisaoAluno = new ArrayList<ContaReceberVO>(0);
		}
		return listaContasPagarParceiroVisaoAluno;
	}

	public void setListaContasPagarParceiroVisaoAluno(List<ContaReceberVO> listaContasPagarParceiroVisaoAluno) {
		this.listaContasPagarParceiroVisaoAluno = listaContasPagarParceiroVisaoAluno;
	}

	public List getListaSelectItemCurso() {
		if (listaSelectItemCurso == null) {
			listaSelectItemCurso = new ArrayList(0);
		}
		return listaSelectItemCurso;
	}

	public void setListaSelectItemCurso(List listaSelectItemCurso) {
		this.listaSelectItemCurso = listaSelectItemCurso;
	}

	public CursoVO getCursoVO() {
		if (cursoVO == null) {
			cursoVO = new CursoVO();
		}
		return cursoVO;
	}

	public void setCursoVO(CursoVO cursoVO) {
		this.cursoVO = cursoVO;
	}

	public String executarAjusteNossoNumeroRepetido() {
		try {
			getFacadeFactory().getContaReceberFacade().executarAjusteNossoNumeroRepetido(getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			setMensagemID("msg_dados_gravados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("contaReceberForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("contaReceberForm.xhtml");
		}
	}

	public String executarAjusteNossoNumeroMatriculaCancelada() {
		try {
			getFacadeFactory().getContaReceberFacade().executarAjusteNossoNumeroMatriculaCancelada(getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			setMensagemID("msg_dados_gravados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("contaReceberForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("contaReceberForm.xhtml");
		}
	}

	public String executarAjusteNossoNumeroParceiro() {
		try {
			getFacadeFactory().getContaReceberFacade().executarAjusteNossoNumeroParceiro(getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			setMensagemID("msg_dados_gravados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("contaReceberForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("contaReceberForm.xhtml");
		}
	}

	public String executarAjusteNossoNumeroPorCursoTurmaSituacaoAreceberAlunosComContasPagas() {
		try {
			getFacadeFactory().getContaReceberFacade().executarAjusteNossoNumeroPorCursoTurmaSituacaoAreceberAlunosComContasPagas(getCursoVO().getCodigo(), getTurmaVO().getCodigo(), getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			setMensagemID("msg_dados_gravados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("contaReceberForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("contaReceberForm.xhtml");
		}
	}

	public List getListaSelectItemTipoContaReceberAtualizar() throws Exception {
		List objs = new ArrayList(0);
		objs.add(new SelectItem("ACR", "Alunos com Contas Recebidas"));
		objs.add(new SelectItem("OUT", "Outros"));
		return objs;
	}

	public void selecionarTurma() {
		try {
			TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
			getListaConsultaTurma().clear();
			getContaReceberVO().setTurma(obj);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparTurma() {
		try {
			getContaReceberVO().setTurma(null);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void carregarDadosPorUnidadeEnsino() {
		try {
			if (!getContaReceberVO().isContaReceberReferenteMaterialDidatico() && !getContaReceberVO().getTipoOrigemContaReceber().isBiblioteca()) {
				limparTurma();
			}
			validarCentroResultadoPorContaReceber();
			montarListaSelectItemContaCorrente();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void validarCentroResultadoPorContaReceber() {
		try {
			if (!Uteis.isAtributoPreenchido(getCentroResultadoOrigemVO().getCentroResultadoAdministrativo())) {
				getCentroResultadoOrigemVO().setUnidadeEnsinoVO(getContaReceberVO().isContaReceberReferenteMaterialDidatico() ? getContaReceberVO().getUnidadeEnsinoFinanceira() : getContaReceberVO().getUnidadeEnsino());
				Uteis.checkState(!Uteis.isAtributoPreenchido(getCentroResultadoOrigemVO().getUnidadeEnsinoVO()), "O campo Unidade Ensino Aba Dados Básicos deve ser informado. ");
				if (Uteis.isAtributoPreenchido(getContaReceberVO().getTurma())) {
					getCentroResultadoOrigemVO().setCentroResultadoAdministrativo(getFacadeFactory().getCentroResultadoFacade().consultarCentroResultadoPorTurma(getContaReceberVO().getTurma().getCodigo(), getUsuarioLogado()));
					getCentroResultadoOrigemVO().setTurmaVO(getContaReceberVO().getTurma());
					getCentroResultadoOrigemVO().setTipoNivelCentroResultadoEnum(TipoNivelCentroResultadoEnum.TURMA);
				} else if (Uteis.isAtributoPreenchido(getCentroResultadoOrigemVO().getUnidadeEnsinoVO())) {
					getCentroResultadoOrigemVO().setCentroResultadoAdministrativo(getFacadeFactory().getCentroResultadoFacade().consultarCentroResultadoPorUnidadeEnsino(getCentroResultadoOrigemVO().getUnidadeEnsinoVO().getCodigo(), getUsuarioLogado()));
					getCentroResultadoOrigemVO().setTipoNivelCentroResultadoEnum(TipoNivelCentroResultadoEnum.UNIDADE_ENSINO);
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparCentroResultado() {
		try {
			getCentroResultadoOrigemVO().setCentroResultadoAdministrativo(new CentroResultadoVO());
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public List getTipoConsultaComboTurma() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador"));
		return itens;
	}

	public void consultarTurma() {
		try {
			super.consultar();
			List objs = new ArrayList(0);
			if (getContaReceberVO().getUnidadeEnsino().getCodigo() == 0) {
				throw new Exception("O campo UNIDADE ENSINO deve ser informado.");
			}
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				objs = getFacadeFactory().getTurmaFacade().consultar(getCampoConsultaTurma(), getValorConsultaTurma(), getContaReceberVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaTurma(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarTurmaPorIdentificador() {
		try {
			if (getContaReceberVO().getUnidadeEnsino().getCodigo() == 0) {
				throw new Exception("O campo UNIDADE ENSINO deve ser informado.");
			}

			getContaReceberVO().setTurma(getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(getContaReceberVO().getTurma(), getContaReceberVO().getTurma().getIdentificadorTurma(), getContaReceberVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));

			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			getContaReceberVO().setTurma(null);
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarCurso() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaCurso().equals("nome")) {
				objs = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultaRapidaPorNomeCursoUnidadeEnsino(getValorConsultaCurso(), getUnidadeEnsinoCursoVO().getUnidadeEnsino(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			setListaConsultaCurso(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarCurso() throws Exception {
		try {
			UnidadeEnsinoCursoVO obj = (UnidadeEnsinoCursoVO) context().getExternalContext().getRequestMap().get("unidadeensinocursoItens");
			setUnidadeEnsinoCursoVO(obj);
			limparTurma();
		} catch (Exception e) {
		}
	}

	public void limparCurso() throws Exception {
		try {
			setUnidadeEnsinoCursoVO(null);
		} catch (Exception e) {
		}
	}

	public List getTipoConsultaComboCurso() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		return itens;
	}

	public void limparListasConsultas() {
		setTurmaVO(null);
		setUnidadeEnsinoCursoVO(null);
		getListaConsultaCurso().clear();
		getListaConsultaTurma().clear();
	}

	public String regerarBoleto() {
		try {
			if (!this.getContaReceberVO().getCodigo().equals(0)) {				
				getFacadeFactory().getContaReceberFacade().gerarDadosBoleto(this.getContaReceberVO(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(contaReceberVO.getUnidadeEnsino().getCodigo()), true, getUsuarioLogado());
			} else {
				throw new Exception("A conta a receber deve estar gravada, para que o código de barras seja regerado!");
			}

			setMensagemID("msg_dados_boletosRegerados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("contaReceberForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("contaReceberForm.xhtml");
		}
	}

	public void setAbrirModalCobrarReimpressao(String abrirModalCobrarReimpressao) {
		this.abrirModalCobrarReimpressao = abrirModalCobrarReimpressao;
	}

	public String getAbrirModalCobrarReimpressao() {
		if (abrirModalCobrarReimpressao == null) {
			abrirModalCobrarReimpressao = "";
		}
		return abrirModalCobrarReimpressao;
	}

	public void setConfiguracaoFinanceiroVO(ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) {
		this.configuracaoFinanceiroVO = configuracaoFinanceiroVO;
	}

	public ConfiguracaoFinanceiroVO getConfiguracaoFinanceiroVO() {
		if (configuracaoFinanceiroVO == null) {
			configuracaoFinanceiroVO = new ConfiguracaoFinanceiroVO();
		}
		return configuracaoFinanceiroVO;
	}

	public Boolean getIsPermissaoFinanceiroManual() {
		if (getContaReceberVO().getSituacao().equals("AR")) {
			verificarPermissaoAlterarDescontoFinanceiroManualDesativado();
			verificarPermissaoFinanceiroManual();
			if (getContaReceberVO().getTipoOrigem().equals(TipoOrigemContaReceber.REQUERIMENTO.getValor())
					|| getContaReceberVO().getTipoOrigem().equals(TipoOrigemContaReceber.BIBLIOTECA.getValor())
					|| getContaReceberVO().getTipoOrigem().equals(TipoOrigemContaReceber.INSCRICAO_PROCESSO_SELETIVO.getValor())) {
				//atualizarValoresContaReceber();
				return false;
			}
			if ((getFinanceiroManualMatriculaPeriodo() && getPermissaoControleFinanceiroManual()) || (!getFinanceiroManualMatriculaPeriodo() && getPermissaoAlterarDescontoFinanceiroManualDesativado())) {
				return false;
			}
		} else {
			return true;
		}
		return true;
	}

	public Boolean getIsPermissaoFinanceiroManual_Valor() {
		if (getContaReceberVO().getSituacao().equals("AR") && getNovaContaReceber()) {
			return false;

		} else if (getContaReceberVO().getSituacao().equals("AR")) {
			verificarPermissaoAlterarDescontoFinanceiroManualDesativado();
			verificarPermissaoFinanceiroManual();
			if ((getFinanceiroManualMatriculaPeriodo() && getPermissaoControleFinanceiroManual())) {
				return false;
			}
		} else {
			return true;
		}
		return true;
	}

	public void verificarPermissaoFinanceiroManual() {
		try {
			Matricula.verificarPermissaoUsuarioFuncionalidade(getUsuarioLogado(), "AtivarFinanceiroManual");
			setPermissaoControleFinanceiroManual(true);
		} catch (Exception ex) {
			setPermissaoControleFinanceiroManual(false);
		}
	}

	public boolean getExibirBotaoCancelar() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico("PermitirCancelarContaReceber", getUsuarioLogado());
			return getContaReceberVO().getPermiteCancelar() && !getContaReceberVO().isAgrupado();
		} catch (Exception ex) {
			return false;
		}
	}

	public void verificarPermissaoAlterarDescontoFinanceiroManualDesativado() {
		try {
			Matricula.verificarPermissaoUsuarioFuncionalidade(getUsuarioLogado(), "ContaReceber_PermitirDescontoFinanceiroManualDesativado");
			setPermissaoAlterarDescontoFinanceiroManualDesativado(true);
		} catch (Exception ex) {
			setPermissaoAlterarDescontoFinanceiroManualDesativado(false);
		}
	}

	/**
	 * @return the permissaoControleFinanceiroManual
	 */
	public Boolean getPermissaoControleFinanceiroManual() {
		if (permissaoControleFinanceiroManual == null) {
			permissaoControleFinanceiroManual = Boolean.FALSE;
		}
		return permissaoControleFinanceiroManual;
	}

	/**
	 * @param permissaoControleFinanceiroManual
	 *            the permissaoControleFinanceiroManual to set
	 */
	public void setPermissaoControleFinanceiroManual(Boolean permissaoControleFinanceiroManual) {
		this.permissaoControleFinanceiroManual = permissaoControleFinanceiroManual;
	}

	/**
	 * @return the financeiroManualMatriculaPeriodo
	 */
	public Boolean getFinanceiroManualMatriculaPeriodo() {
		if (financeiroManualMatriculaPeriodo == null) {
			financeiroManualMatriculaPeriodo = Boolean.FALSE;
		}
		return financeiroManualMatriculaPeriodo;
	}

	/**
	 * @param financeiroManualMatriculaPeriodo
	 *            the financeiroManualMatriculaPeriodo to set
	 */
	public void setFinanceiroManualMatriculaPeriodo(Boolean financeiroManualMatriculaPeriodo) {
		this.financeiroManualMatriculaPeriodo = financeiroManualMatriculaPeriodo;
	}

	public Boolean getApresentarMensagemControleFinanceiroManualAtivo() {
		if (getFinanceiroManualMatriculaPeriodo()) {
			return true;
		}
		return false;
	}

	/**
	 * @return the apresentarRichModalFinanceiroManual
	 */
	public Boolean getApresentarRichModalFinanceiroManual() {
		if (apresentarRichModalFinanceiroManual == null) {
			apresentarRichModalFinanceiroManual = Boolean.FALSE;
		}
		return apresentarRichModalFinanceiroManual;
	}

	/**
	 * @param apresentarRichModalFinanceiroManual
	 *            the apresentarRichModalFinanceiroManual to set
	 */
	public void setApresentarRichModalFinanceiroManual(Boolean apresentarRichModalFinanceiroManual) {
		this.apresentarRichModalFinanceiroManual = apresentarRichModalFinanceiroManual;
	}

	public void realizarFechamentoRichModalFinanceiroManual() {
		setApresentarRichModalFinanceiroManual(false);
	}

	public boolean getIsPossuiValorLancadoRecebimento() {
		if (!getContaReceberVO().getValorCalculadoDescontoLancadoRecebimento().equals(0.0)) {
			return true;
		}
		return false;
	}

	/**
	 * @return the novaContaReceber
	 */
	public Boolean getNovaContaReceber() {
		if (novaContaReceber == null) {
			novaContaReceber = Boolean.FALSE;
		}
		return novaContaReceber;
	}

	/**
	 * @param novaContaReceber
	 *            the novaContaReceber to set
	 */
	public void setNovaContaReceber(Boolean novaContaReceber) {
		this.novaContaReceber = novaContaReceber;
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

	public TurmaVO getTurmaVO() {
		if (turmaVO == null) {
			turmaVO = new TurmaVO();
		}
		return turmaVO;
	}

	public void setTurmaVO(TurmaVO turmaVO) {
		this.turmaVO = turmaVO;
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

	public String getTipoContaReceberAtualizar() {
		if (tipoContaReceberAtualizar == null) {
			tipoContaReceberAtualizar = "OUT";
		}
		return tipoContaReceberAtualizar;
	}

	public void setTipoContaReceberAtualizar(String tipoContaReceberAtualizar) {
		this.tipoContaReceberAtualizar = tipoContaReceberAtualizar;
	}

	public boolean getTipoContaReceberAtualizarAlunosContaRecebida() {
		if (getTipoContaReceberAtualizar().equals("ACR")) {
			return true;
		}
		return false;
	}

	public Boolean getApresentarBotaoImprimirBoleto() {
		return !getContaReceberVO().isNovoObj() && !getContaReceberVO().getSituacao().equals("NE");
	}

	public String getCssParcela() {
		if (getContaReceberVO().isNovoObj()) {
			return "form-control campos";
		}
		return "form-control camposSomenteLeitura";
	}

	public Boolean getApresentarBotaoSugestaoParcela() {
		if (getContaReceberVO().getPessoa().getCodigo() != 0) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public ContaReceberVO getUltimaContaReceberGeradaManualmente() {
		if (ultimaContaReceberGeradaManualmente == null) {
			ultimaContaReceberGeradaManualmente = new ContaReceberVO();
		}
		return ultimaContaReceberGeradaManualmente;
	}

	public void setUltimaContaReceberGeradaManualmente(ContaReceberVO ultimaContaReceberGeradaManualmente) {
		this.ultimaContaReceberGeradaManualmente = ultimaContaReceberGeradaManualmente;
	}

	public Boolean getApresentarBotaoUltimaParcelaGeradaManualmente() {
		return !getUltimaContaReceberGeradaManualmente().isNovoObj() && (getContaReceberVO().getFuncionario().getCodigo() != 0 || getContaReceberVO().getCandidato().getCodigo() != 0 || !getContaReceberVO().getMatriculaAluno().getMatricula().equals(""));
	}

	public Boolean getPermissaoAlterarDescontoFinanceiroManualDesativado() {
		if (permissaoAlterarDescontoFinanceiroManualDesativado == null) {
			permissaoAlterarDescontoFinanceiroManualDesativado = Boolean.FALSE;
		}
		return permissaoAlterarDescontoFinanceiroManualDesativado;
	}

	public void setPermissaoAlterarDescontoFinanceiroManualDesativado(Boolean permissaoAlterarDescontoFinanceiroManualDesativado) {
		this.permissaoAlterarDescontoFinanceiroManualDesativado = permissaoAlterarDescontoFinanceiroManualDesativado;
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

	public List<SelectItem> getListaSelectItemAluno() {
		if (listaSelectItemAluno == null) {
			listaSelectItemAluno = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemAluno;
	}

	public void setListaSelectItemAluno(List<SelectItem> listaSelectItemAluno) {
		this.listaSelectItemAluno = listaSelectItemAluno;
	}

	public Boolean getIsentarJuroMulta() {
		if (isentarJuroMulta == null) {
			isentarJuroMulta = false;
		}
		return isentarJuroMulta;
	}

	public void setIsentarJuroMulta(Boolean isentarJuroMulta) {
		this.isentarJuroMulta = isentarJuroMulta;
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

	public void gravarRenegociacao() {
		try {
			// simularRenegociacao();
			setContaReceberVO(getFacadeFactory().getNegociacaoContaReceberFacade().incluirRenegociacaoApartirContaReceber(getContaReceberSimulada(), getContaReceberVO(), getContaReceberSimulada().getDataVencimento(), (!getLoginControle().getPermissaoAcessoMenuVO().getPermitirAlterarValorContaReceber() || getLoginControle().getPermissaoAcessoMenuVO().getContaReceber_PermitirAlterarDataVencimento()), getAplicacaoControle().getConfiguracaoFinanceiroVO(getContaReceberVO().getUnidadeEnsinoFinanceira().getCodigo()), false, getUsuarioLogado()));
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	private Boolean contaSimulada;

	public void iniciarSimulacaoContaReceber() {
		try {
			setContaReceberSimulada(new ContaReceberVO());
			setContaReceberSimulada((ContaReceberVO) getContaReceberVO().clone());
			getContaReceberSimulada().setAcrescimo(0.0);
			getContaReceberSimulada().setValorDescontoRateio(0.0);
			limparMensagem();
		} catch (Exception e) {
		}
	}

	public void simularRenegociacao() {
		try {
			getContaReceberSimulada().setRealizandoRecebimento(Boolean.TRUE);
			getContaReceberSimulada().getCalcularValorFinal(getContaReceberSimulada().getDataVencimento(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getContaReceberSimulada().getUnidadeEnsinoFinanceira().getCodigo()), false, getContaReceberSimulada().getDataVencimento(), getUsuarioLogado());
			getContaReceberSimulada().setRealizandoRecebimento(Boolean.FALSE);
			getContaReceberSimulada().setAcrescimo(getContaReceberVO().getMulta() + getContaReceberVO().getJuro() + getContaReceberVO().getAcrescimo());
			setMensagemID("msg_dados_simulado");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public Boolean getContaSimulada() {
		if (contaSimulada == null) {
			contaSimulada = false;
		}
		return contaSimulada;
	}

	public void setContaSimulada(Boolean contaSimulada) {
		this.contaSimulada = contaSimulada;
	}

	public Boolean getApresentarBotaoAlterarDataVencimento() {
		try {
			return !getContaReceberVO().isNovoObj() && getContaReceberVO().getSituacao().equals("AR") && (getContaReceberVO().getTipoAluno() || getContaReceberVO().getTipoFuncionario() || getContaReceberVO().getTipoResponsavelFinanceiro() || (getContaReceberVO().getTipoParceiro() && getContaReceberVO().getMatriculaAluno().getMatricula().isEmpty()));
		} catch (Exception e) {
			return false;

		}
	}

	public String getSemestre() {
		if (semestre == null) {
			semestre = "";
		}
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	public void setTipoConsultaComboResponsavelFinanceiro(List<SelectItem> tipoConsultaComboResponsavelFinanceiro) {
		this.tipoConsultaComboResponsavelFinanceiro = tipoConsultaComboResponsavelFinanceiro;
	}

	public String getAno() {
		if (ano == null) {
			ano = "";
		}
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public ContaReceberVO getContaReceberSimulada() {
		if (contaReceberSimulada == null) {
			contaReceberSimulada = new ContaReceberVO();
		}
		return contaReceberSimulada;
	}

	public void setContaReceberSimulada(ContaReceberVO contaReceberSimulada) {
		this.contaReceberSimulada = contaReceberSimulada;
	}

	private List<SelectItem> tipoConsultaComboTipoBoleto;

	public List<SelectItem> getTipoConsultaComboTipoBoleto() {
		if (tipoConsultaComboTipoBoleto == null) {
			tipoConsultaComboTipoBoleto = new ArrayList<SelectItem>();
			tipoConsultaComboTipoBoleto.add(new SelectItem("boleto", "Boleto"));
			tipoConsultaComboTipoBoleto.add(new SelectItem("boletoSegundo", "Boleto (LayOut 2)"));
			tipoConsultaComboTipoBoleto.add(new SelectItem("carne", "Carnê"));
		}
		return tipoConsultaComboTipoBoleto;
	}

	public String getTipoBoleto() {
		if (tipoBoleto == null) {
			tipoBoleto = "boleto";
		}
		return tipoBoleto;
	}

	public void setTipoBoleto(String tipoBoleto) {
		this.tipoBoleto = tipoBoleto;
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

	public void realizarProcessamentoCalculoValorPrimeiraFaixaDesconto() {
		try {
			getFacadeFactory().getContaReceberFacade().realizarProcessamentoCalculoValorPrimeiraFaixaDesconto(getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public String getTipoOrigem() {
		if (tipoOrigem == null) {
			tipoOrigem = getLoginControle().getPermissaoAcessoMenuVO().getPermitirApenasContasDaBiblioteca() ? "BIB" : "";
		}
		return tipoOrigem;
	}

	public void setTipoOrigem(String tipoOrigem) {
		this.tipoOrigem = tipoOrigem;
	}

	public String getApresentarMsgPreMatricula() {
		if (apresentarMsgPreMatricula == null) {
			apresentarMsgPreMatricula = "";
		}
		return apresentarMsgPreMatricula;
	}

	public void setApresentarMsgPreMatricula(String apresentarMsgPreMatricula) {
		this.apresentarMsgPreMatricula = apresentarMsgPreMatricula;
	}

	public void verificarPermissaoApresentarBotaoEditarManualmenteConta() {
		setApresentarBotaoEditarManualmenteConta(Boolean.FALSE);
		if ((getContaReceberVO().getSituacao().equals("AR")) &&
				(!getContaReceberVO().isAgrupado()) &&
				(getContaReceberVO().getProcessamentoIntegracaoFinanceiraDetalheVO().getCodigo() == 0) &&
				!getContaReceberVO().getContaEditadaManualmente() &&
				(getLoginControle().getPermissaoAcessoMenuVO().getContaReceber_PermitirEditarManualmenteConta())) {
			setApresentarBotaoEditarManualmenteConta(Boolean.TRUE);
		}
	}

	public void verificarPermissaoApresentarBotaoAlterarDataVencimentoValorRecebimento() {
		if (getLoginControle().getPermissaoAcessoMenuVO().getContaReceber_PermitirAlterarDataVencimento()
				&& getLoginControle().getPermissaoAcessoMenuVO().getPermitirAlterarValorContaReceber()
				&& !getContaReceberVO().isNovoObj() && getContaReceberVO().getSituacao().equals("AR")
				&& !getContaReceberVO().getTipoOrigemContaReceber().equals(TipoOrigemContaReceber.INSCRICAO_PROCESSO_SELETIVO)
				&& !getContaReceberVO().getTipoOrigemContaReceber().equals(TipoOrigemContaReceber.REQUERIMENTO)
				&& (getContaReceberVO().getTipoAluno() || getContaReceberVO().getTipoFuncionario()
						|| getContaReceberVO().getTipoResponsavelFinanceiro() || (getContaReceberVO().getTipoParceiro() && getContaReceberVO().getMatriculaAluno().getMatricula().isEmpty()))) {
			setApresentarBotaoAlterarVencimentoEValor(Boolean.TRUE);
			setApresentarBotaoAlterarVencimento(Boolean.FALSE);
			setApresentarBotaoAlterarValor(Boolean.FALSE);
		} else if (getLoginControle().getPermissaoAcessoMenuVO().getContaReceber_PermitirAlterarDataVencimento()
				&& !getLoginControle().getPermissaoAcessoMenuVO().getPermitirAlterarValorContaReceber()
				&& !getContaReceberVO().isNovoObj() && getContaReceberVO().getSituacao().equals("AR")
				&& !getContaReceberVO().getTipoOrigemContaReceber().equals(TipoOrigemContaReceber.INSCRICAO_PROCESSO_SELETIVO)
				&& !getContaReceberVO().getTipoOrigemContaReceber().equals(TipoOrigemContaReceber.REQUERIMENTO)
				&& (getContaReceberVO().getTipoAluno() || getContaReceberVO().getTipoFuncionario()
						|| getContaReceberVO().getTipoResponsavelFinanceiro() || (getContaReceberVO().getTipoParceiro() && getContaReceberVO().getMatriculaAluno().getMatricula().isEmpty()))) {
			setApresentarBotaoAlterarVencimentoEValor(Boolean.FALSE);
			setApresentarBotaoAlterarVencimento(Boolean.TRUE);
			setApresentarBotaoAlterarValor(Boolean.FALSE);
		} else if (!getLoginControle().getPermissaoAcessoMenuVO().getContaReceber_PermitirAlterarDataVencimento() 
				&& getLoginControle().getPermissaoAcessoMenuVO().getPermitirAlterarValorContaReceber() 
				&& !getContaReceberVO().isNovoObj() && getContaReceberVO().getSituacao().equals("AR")
				&& !getContaReceberVO().getTipoOrigemContaReceber().equals(TipoOrigemContaReceber.INSCRICAO_PROCESSO_SELETIVO)
				&& !getContaReceberVO().getTipoOrigemContaReceber().equals(TipoOrigemContaReceber.REQUERIMENTO)
				&& (getContaReceberVO().getTipoAluno() || getContaReceberVO().getTipoFuncionario() || getContaReceberVO().getTipoResponsavelFinanceiro() || (getContaReceberVO().getTipoParceiro() && getContaReceberVO().getMatriculaAluno().getMatricula().isEmpty()))) {
			setApresentarBotaoAlterarVencimentoEValor(Boolean.FALSE);
			setApresentarBotaoAlterarVencimento(Boolean.FALSE);
			setApresentarBotaoAlterarValor(Boolean.TRUE);
		} else {
			setApresentarBotaoAlterarVencimentoEValor(Boolean.FALSE);
			setApresentarBotaoAlterarVencimento(Boolean.FALSE);
			setApresentarBotaoAlterarValor(Boolean.FALSE);
		}
	}

	public Boolean getApresentarBotaoAlterarVencimentoEValor() {
		if (apresentarBotaoAlterarVencimentoEValor == null) {
			apresentarBotaoAlterarVencimentoEValor = Boolean.FALSE;
		}
		return apresentarBotaoAlterarVencimentoEValor;
	}

	public void setApresentarBotaoAlterarVencimentoEValor(Boolean apresentarBotaoAlterarVencimentoEValor) {
		this.apresentarBotaoAlterarVencimentoEValor = apresentarBotaoAlterarVencimentoEValor;
	}

	public Boolean getApresentarBotaoEditarManualmenteConta() {
		if (apresentarBotaoEditarManualmenteConta == null) {
			apresentarBotaoEditarManualmenteConta = Boolean.FALSE;
		}
		return apresentarBotaoEditarManualmenteConta;
	}

	public void setApresentarBotaoEditarManualmenteConta(Boolean apresentarBotaoEditarManualmenteConta) {
		this.apresentarBotaoEditarManualmenteConta = apresentarBotaoEditarManualmenteConta;
	}

	public Boolean getApresentarBotaoAlterarVencimento() {
		if (apresentarBotaoAlterarVencimento == null) {
			apresentarBotaoAlterarVencimento = Boolean.FALSE;
		}
		return apresentarBotaoAlterarVencimento;
	}

	public void setApresentarBotaoAlterarVencimento(Boolean apresentarBotaoAlterarVencimento) {
		this.apresentarBotaoAlterarVencimento = apresentarBotaoAlterarVencimento;
	}

	public Boolean getApresentarBotaoAlterarValor() {
		if (apresentarBotaoAlterarValor == null) {
			apresentarBotaoAlterarValor = Boolean.FALSE;
		}
		return apresentarBotaoAlterarValor;
	}

	public void setApresentarBotaoAlterarValor(Boolean apresentarBotaoAlterarValor) {
		this.apresentarBotaoAlterarValor = apresentarBotaoAlterarValor;
	}

	public Double getValor() {
		if (valor == null) {
			valor = getContaReceberVO().getValor();
		}
		return (valor);
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public Boolean getPossuiBolsaCusteada() {
		return !this.getContaReceberVO().getPlanoDescontoConvenioCusteadoContaReceber().isEmpty();
		/*if (this.getContaReceberVO().getValorCusteadoContaReceber() > 0) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;*/
	}

	public Boolean getPermiteReceber() {
		if (permiteReceber == null) {
			permiteReceber = Boolean.FALSE;
		}
		return permiteReceber;
	}

	public void setPermiteReceber(Boolean permiteReceber) {
		this.permiteReceber = permiteReceber;
	}

	public Boolean getConsDataCompetencia() {
		if (consDataCompetencia == null) {
			consDataCompetencia = Boolean.FALSE;
		}
		return consDataCompetencia;
	}

	public void setConsDataCompetencia(Boolean consDataCompetencia) {
		this.consDataCompetencia = consDataCompetencia;
	}

	public String cancelarContaReceber() {
		try {
			if (this.getContaReceberVO().getSituacaoAReceber()) {
				getFacadeFactory().getContaReceberFacade().cancelarContaReceber(this.getContaReceberVO(), getUsuarioLogado());
				this.getContaReceberVO().setSituacao(SituacaoContaReceber.CANCELADO_FINANCEIRO.getValor());
				getFacadeFactory().getContaReceberFacade().atualizarAtributoUpdatedContaReceberAposAlteracaoPeloSistemaGarantindoAssimIntegridade(getContaReceberVO());
				getFacadeFactory().getMatriculaFacade().realizarVerificacaoELiberacaoSuspensaoMatricula(getContaReceberVO().getMatriculaAluno().getMatricula());
			} else {
				throw new Exception("Não é possível cancelar um título que não esteja na situação A RECEBER.");
			}
			setMensagemID("msg_dados_cancelados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("contaReceberForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("contaReceberForm.xhtml");
		}
	}

	public String reativarContaReceberCancelada() {
		try {
			if (this.getContaReceberVO().getSituacao().equals(SituacaoContaReceber.CANCELADO_FINANCEIRO.getValor())) {
				getFacadeFactory().getContaReceberFacade().reativarContaReceberCancelada(this.getContaReceberVO(), getUsuarioLogado());
				this.getContaReceberVO().setSituacao(SituacaoContaReceber.A_RECEBER.getValor());
			} else {
				throw new Exception("Não é possível reativar um título que não está CANCELADO.");
			}
			setMensagemID("msg_dados_reativados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("contaReceberForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("contaReceberForm.xhtml");
		}
	}

	/**
	 * @author Victor Hugo de Paula Costa
	 */
	private ComprovanteRecebimentoRelControle comprovanteRecebimentoRelControle;

	public void realizarRecebimentoCartaoCredito() {
		if (getNegociacaoRecebimentoVO().getPossuiRegistroUtilizarCartaoComoRecorrencia() && 
				getNegociacaoRecebimentoVO().getConfiguracaoRecebimentoCartaoOnlineVO().getExigirConfirmacaoRecorrencia()
				&& !getMensagemConfirmacaoRecorrenciaApresentada()) {
			setModalPagamentoOnline("RichFaces.$('panelConfirmacaoRecorrencia').show();");
			return;
		}
		List<FormaPagamentoNegociacaoRecebimentoVO> listaCartoesAntesAlteracao = getNegociacaoRecebimentoVO().getFormaPagamentoNegociacaoRecebimentoVOs().stream().collect(Collectors.toList());
		try {
			executarValidacaoSimulacaoVisaoAluno();
			getFacadeFactory().getNegociacaoRecebimentoFacade().realizarRecebimentoCartaoCreditoMatriculaRenovacaoOnline(getNegociacaoRecebimentoVO(), getNegociacaoRecebimentoVO().getMatricula(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getNegociacaoRecebimentoVO().getUnidadeEnsino().getCodigo()), getUsuarioLogado());
			imprimirComprovanteRecebimento();
			setModalPagamentoOnline("RichFaces.$('panelPagamento').hide();");
			setModalConfirmacaoPagamento("RichFaces.$('panelConfirmacaoPagamento').show()");
			consultarContaPagarVisaoAluno();
		} catch (Exception e) {
			getNegociacaoRecebimentoVO().getFormaPagamentoNegociacaoRecebimentoVOs().clear();
			getNegociacaoRecebimentoVO().getFormaPagamentoNegociacaoRecebimentoVOs().addAll(listaCartoesAntesAlteracao);
			setMensagemConfirmacaoRecorrenciaApresentada(false);
			setModalPagamentoOnline("RichFaces.$('panelPagamento').show();");
			setModalConfirmacaoPagamento("");
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void imprimirComprovanteRecebimento() {
		try {
			comprovanteRecebimentoRelControle = null;
			comprovanteRecebimentoRelControle = (ComprovanteRecebimentoRelControle) context().getExternalContext().getSessionMap().get(ComprovanteRecebimentoRelControle.class.getSimpleName());
			if (comprovanteRecebimentoRelControle == null) {
				comprovanteRecebimentoRelControle = new ComprovanteRecebimentoRelControle();
				context().getExternalContext().getSessionMap().put(ComprovanteRecebimentoRelControle.class.getSimpleName(), comprovanteRecebimentoRelControle);
			}
			if (!getNegociacaoRecebimentoVO().getCodigo().equals(0)) {
				getComprovanteRecebimentoRelControle().setNegociacaoRecebimentoVO((NegociacaoRecebimentoVO) getNegociacaoRecebimentoVO().clone());
				getComprovanteRecebimentoRelControle().imprimirPDFRecebimentoCartaoCredito();
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public ComprovanteRecebimentoRelControle getComprovanteRecebimentoRelControle() {
		return comprovanteRecebimentoRelControle;
	}

	public void setComprovanteRecebimentoRelControle(ComprovanteRecebimentoRelControle comprovanteRecebimentoRelControle) {
		this.comprovanteRecebimentoRelControle = comprovanteRecebimentoRelControle;
	}

	public void validarNumeroCartaoCredito() {
		try {
			FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO = (FormaPagamentoNegociacaoRecebimentoVO) context().getExternalContext().getRequestMap().get("formaPagamentoItens");
			formaPagamentoNegociacaoRecebimentoVO.setOperadoraCartaoVO(getFacadeFactory().getOperadoraCartaoFacade().consultarPorCodigoConfiguracaoFinanceiroCartao(formaPagamentoNegociacaoRecebimentoVO.getConfiguracaoFinanceiroCartaoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			if (!getFacadeFactory().getGerenciamentoDeTransacaoCartaoDeCreditoFacade().validarNumeroCartaoCredito(formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNumeroCartao(), formaPagamentoNegociacaoRecebimentoVO.getOperadoraCartaoVO().getNome())) {
				throw new Exception(UteisJSF.internacionalizar("msg_NumeroCartaoCreditoInvalido"));
			} else {
				setMensagemID("msg_NumeroCartaoCreditoValido", Uteis.SUCESSO);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public Boolean getDesativarBotaoCartaoCredito() {
		if (desativarBotaoCartaoCredito == null) {
			desativarBotaoCartaoCredito = false;
		}
		return desativarBotaoCartaoCredito;
	}

	public void setDesativarBotaoCartaoCredito(Boolean desativarBotaoCartaoCredito) {
		this.desativarBotaoCartaoCredito = desativarBotaoCartaoCredito;
	}

	public void consultarContaReceberAluno() {
		try {
			limparMensagem();
			ContaReceberVO contaReceberVO = (ContaReceberVO) context().getExternalContext().getRequestMap().get("contaAPagarItens");
			if(getConfiguracaoRecebimentoCartaoOnlineVOs().isEmpty() || !getConfiguracaoRecebimentoCartaoOnlineVOs().containsKey(contaReceberVO.getUnidadeEnsinoFinanceira().getCodigo())
					|| !Uteis.isAtributoPreenchido(getConfiguracaoRecebimentoCartaoOnlineVOs().get(contaReceberVO.getUnidadeEnsinoFinanceira().getCodigo()))) {
				throw new Exception(UteisJSF.internacionalizar("msg_RecebimentoCartao_semConfiguracao"));
			}
			setConfiguracaoRecebimentoCartaoOnlineVO(getConfiguracaoRecebimentoCartaoOnlineVOs().get(contaReceberVO.getUnidadeEnsinoFinanceira().getCodigo()));
			ConfiguracaoFinanceiroVO confFinanceiro = Uteis.isAtributoPreenchido(getConfiguracaoRecebimentoCartaoOnlineVO().getConfiguracaoFinanceiroVO()) ? getConfiguracaoRecebimentoCartaoOnlineVO().getConfiguracaoFinanceiroVO() : getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(contaReceberVO.getUnidadeEnsinoFinanceira().getCodigo());
			contaReceberVO = getFacadeFactory().getContaReceberFacade().consultarPorChavePrimaria(contaReceberVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, confFinanceiro, getUsuarioLogado());
			contaReceberVO.setRealizandoRecebimento(true);			
			if (getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(contaReceberVO.getUnidadeEnsinoFinanceira().getCodigo()).getVencimentoParcelaDiaUtil()) {
				contaReceberVO.getDataOriginalVencimento();
				contaReceberVO.setDataVencimentoDiaUtil(contaReceberVO.getDataVencimento());
				contaReceberVO.setDataVencimentoDiaUtil(getFacadeFactory().getContaReceberFacade().obterDataVerificandoDiaUtil(contaReceberVO.getDataVencimento(), contaReceberVO.getUnidadeEnsino().getCidade().getCodigo(), getUsuarioLogado()));
				contaReceberVO.setDataVencimento(contaReceberVO.getDataVencimentoDiaUtil());
			}
			ContaReceberNegociacaoRecebimentoVO contaReceberNegociacaoRecebimentoVO = new ContaReceberNegociacaoRecebimentoVO();
			contaReceberNegociacaoRecebimentoVO.setContaReceber(contaReceberVO);
			contaReceberNegociacaoRecebimentoVO.setValorTotal(contaReceberVO.getCalcularValorFinal(new Date(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(contaReceberVO.getUnidadeEnsino().getCodigo()), false, getUsuarioLogado()));
			setNegociacaoRecebimentoVO(new NegociacaoRecebimentoVO());
			setQuantidadeCartao(0);
			getNegociacaoRecebimentoVO().setMatricula(contaReceberVO.getMatriculaAluno().getMatricula());
			getNegociacaoRecebimentoVO().adicionarObjContaReceberNegociacaoRecebimentoVOs(contaReceberNegociacaoRecebimentoVO);
			getNegociacaoRecebimentoVO().setValorTotal(getNegociacaoRecebimentoVO().getValorTotal() + contaReceberVO.getValorTotalAPagarNaDataAtual());
			getNegociacaoRecebimentoVO().setTipoOrigemContaReceber(TipoOrigemContaReceber.getEnum(contaReceberVO.getTipoOrigem()));
			getNegociacaoRecebimentoVO().setConfiguracaoRecebimentoCartaoOnlineVO(getConfiguracaoRecebimentoCartaoOnlineVO());
			if (getNegociacaoRecebimentoVO().getConfiguracaoRecebimentoCartaoOnlineVO().getApresentarOpcaoRecorrenciaAluno() && getNegociacaoRecebimentoVO().getConfiguracaoRecebimentoCartaoOnlineVO().getUtilizarOpcaoRecorrenciaDefaulMarcado()) {
				getNegociacaoRecebimentoVO().setCriarRegistroRecorrenciaDCC(true);
			} else {
				getNegociacaoRecebimentoVO().setCriarRegistroRecorrenciaDCC(false);
			}
			getConfiguracaoFinanceiroCartaoVOs().clear();
			getConfiguracaoFinanceiroCartaoVOs().addAll(getFacadeFactory().getConfiguracaoFinanceiroCartaoFacade().consultarConfiguracaoFinanceiroCartaoPorCodigoConfiguracaoFinanceiro(confFinanceiro.getCodigo(), getNegociacaoRecebimentoVO().getValorTotal(), "usarminhascontasvisaoaluno", getTipoCartao().name(), getUsuarioLogado()));
			setPermitirRecebimentoCartaoCreditoOnline(true);
			inicializarDadosCartaoCreditoRecorrenciaCadastrada();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	private String modalPagamentoOnline;

	public String getModalPagamentoOnline() {
		if (modalPagamentoOnline == null) {
			modalPagamentoOnline = "";
		}
		return modalPagamentoOnline;
	}

	public void setModalPagamentoOnline(String modalPagamentoOnline) {
		this.modalPagamentoOnline = modalPagamentoOnline;
	}

	private List<FormaPagamentoNegociacaoRecebimentoVO> formaPagamentoNegociacaoRecebimentoVOs;
	private NegociacaoRecebimentoVO negociacaoRecebimentoVO;
	private Integer quantidadeCartao;
	private Boolean desativarBotaoCartaoCredito;

	public List<FormaPagamentoNegociacaoRecebimentoVO> getFormaPagamentoNegociacaoRecebimentoVOs() {
		if (formaPagamentoNegociacaoRecebimentoVOs == null) {
			formaPagamentoNegociacaoRecebimentoVOs = new ArrayList<FormaPagamentoNegociacaoRecebimentoVO>();
		}
		return formaPagamentoNegociacaoRecebimentoVOs;
	}

	public void setFormaPagamentoNegociacaoRecebimentoVOs(List<FormaPagamentoNegociacaoRecebimentoVO> formaPagamentoNegociacaoRecebimentoVOs) {
		this.formaPagamentoNegociacaoRecebimentoVOs = formaPagamentoNegociacaoRecebimentoVOs;
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
	
	public void adicionarNovoCartaoCredito() {
		try {
			ConfiguracaoFinanceiroCartaoVO configuracaoFinanceiroCartaoVO = (ConfiguracaoFinanceiroCartaoVO) context().getExternalContext().getRequestMap().get("itemConfiguracaoFinanceiroCartao");
			for (FormaPagamentoNegociacaoRecebimentoVO obj : negociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoVOs()) {
				if (TipoCartaoOperadoraCartaoEnum.CARTAO_DEBITO.name().equals(obj.getOperadoraCartaoVO().getTipo())) {
					if (TipoCartaoOperadoraCartaoEnum.CARTAO_DEBITO.name().equals(configuracaoFinanceiroCartaoVO.getOperadoraCartaoVO().getTipo())) {
						throw new Exception("Somente um cartão de débito pode ser utilizado!");
					} else {
						throw new Exception("Operações de débito e crédito juntas não permitida!");
					}
				} else {
					if (TipoCartaoOperadoraCartaoEnum.CARTAO_DEBITO.name().equals(configuracaoFinanceiroCartaoVO.getOperadoraCartaoVO().getTipo())) {
						throw new Exception("Operações de débito e crédito juntas não permitida!");
					}
				}
			}
			setQuantidadeCartao(getQuantidadeCartao() + 1);
			int parcelas = 1;
			if (TipoCartaoOperadoraCartaoEnum.CARTAO_CREDITO.name().equals(configuracaoFinanceiroCartaoVO.getOperadoraCartaoVO().getTipo())) {
				Date menorDataVencimento = null;
				for (ContaReceberNegociacaoRecebimentoVO c : getNegociacaoRecebimentoVO().getContaReceberNegociacaoRecebimentoVOs()) {
					if (Uteis.isAtributoPreenchido(c.getContaReceber().getDataVencimento()) &&
							(menorDataVencimento == null || c.getContaReceber().getDataVencimento().before(menorDataVencimento))) {
						menorDataVencimento = c.getContaReceber().getDataVencimento();
					}
				}				
				parcelas = getConfiguracaoRecebimentoCartaoOnlineVO().getQtdeParcelasPermitida(getNegociacaoRecebimentoVO().getResiduo(), menorDataVencimento, getUsuarioLogado(), getNegociacaoRecebimentoVO().getListaTipoOrigemContaReceber());
			}
			getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoFacade().adicionarNovoCartaoCredito(getNegociacaoRecebimentoVO(), configuracaoFinanceiroCartaoVO, parcelas, getQuantidadeCartao(), getUsuarioLogado());
			getNegociacaoRecebimentoVO().setValorTotalRecebimento(0.0);
			for (FormaPagamentoNegociacaoRecebimentoVO obj : getNegociacaoRecebimentoVO().getFormaPagamentoNegociacaoRecebimentoVOs()) {
				getNegociacaoRecebimentoVO().setValorTotalRecebimento(getNegociacaoRecebimentoVO().getValorTotalRecebimento() + obj.getValorRecebimento());
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	/*public void adicionarNovoCartaoCredito() {
		try {
			ConfiguracaoFinanceiroCartaoVO configuracaoFinanceiroCartaoVO = (ConfiguracaoFinanceiroCartaoVO) context().getExternalContext().getRequestMap().get("itemConfiguracaoFinanceiroCartao");
			setQuantidadeCartao(getQuantidadeCartao() + 1);
			int parcelas = 1;
			if (TipoCartaoOperadoraCartaoEnum.CARTAO_CREDITO.name().equals(configuracaoFinanceiroCartaoVO.getOperadoraCartaoVO().getTipo())) {				
				parcelas = getConfiguracaoRecebimentoCartaoOnlineVO().getQtdeParcelasPermitida(getNegociacaoRecebimentoVO().getResiduo(), getUsuarioLogado());
			}
			getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoFacade().adicionarNovoCartaoCredito(getNegociacaoRecebimentoVO(), configuracaoFinanceiroCartaoVO, parcelas, getQuantidadeCartao(), getUsuarioLogado());
			getNegociacaoRecebimentoVO().setValorTotalRecebimento(0.0);
			for (FormaPagamentoNegociacaoRecebimentoVO obj : getNegociacaoRecebimentoVO().getFormaPagamentoNegociacaoRecebimentoVOs()) {
				getNegociacaoRecebimentoVO().setValorTotalRecebimento(getNegociacaoRecebimentoVO().getValorTotalRecebimento() + obj.getValorRecebimento());
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}*/

	public void removerCartaoCredito() {
		try {
			FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO = (FormaPagamentoNegociacaoRecebimentoVO) context().getExternalContext().getRequestMap().get("formaPagamentoItens");
			getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoFacade().removerCartaoCredito(formaPagamentoNegociacaoRecebimentoVO, getNegociacaoRecebimentoVO(), getUsuarioLogado());
			setQuantidadeCartao(getNegociacaoRecebimentoVO().getFormaPagamentoNegociacaoRecebimentoVOs().size());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void calcularTotalPago() {
		FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO = (FormaPagamentoNegociacaoRecebimentoVO) context().getExternalContext().getRequestMap().get("formaPagamentoItens");
		try {
			Date menorDataVencimento = null;
			for (ContaReceberNegociacaoRecebimentoVO c : getNegociacaoRecebimentoVO().getContaReceberNegociacaoRecebimentoVOs()) {
				if (Uteis.isAtributoPreenchido(c.getContaReceber().getDataVencimento()) &&
						(menorDataVencimento == null || c.getContaReceber().getDataVencimento().before(menorDataVencimento))) {
					menorDataVencimento = c.getContaReceber().getDataVencimento();
				}
			}
			int parcelas = getConfiguracaoRecebimentoCartaoOnlineVO().getQtdeParcelasPermitida(formaPagamentoNegociacaoRecebimentoVO.getValorRecebimento(), menorDataVencimento, getUsuarioLogado(), getNegociacaoRecebimentoVO().getListaTipoOrigemContaReceber());
			formaPagamentoNegociacaoRecebimentoVO.montarListasSelectItemParcelas(parcelas);
			if (formaPagamentoNegociacaoRecebimentoVO.getQtdeParcelasCartaoCredito().intValue() > parcelas) {
				formaPagamentoNegociacaoRecebimentoVO.setQtdeParcelasCartaoCredito(parcelas);
			}
			getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoFacade().calcularTotalPago(getNegociacaoRecebimentoVO(), formaPagamentoNegociacaoRecebimentoVO, getUsuarioLogado());
			setQuantidadeCartao(getNegociacaoRecebimentoVO().getFormaPagamentoNegociacaoRecebimentoVOs().size());
			limparMensagem();
		} catch (Exception e) {
			formaPagamentoNegociacaoRecebimentoVO.setValorRecebimento(0.0);
			getNegociacaoRecebimentoVO().setValorTotalRecebimento(0.0);
			for (FormaPagamentoNegociacaoRecebimentoVO obj : getNegociacaoRecebimentoVO().getFormaPagamentoNegociacaoRecebimentoVOs()) {
				getNegociacaoRecebimentoVO().setValorTotalRecebimento(getNegociacaoRecebimentoVO().getValorTotalRecebimento() + obj.getValorRecebimento());
			}
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public Integer getQuantidadeCartao() {
		if (quantidadeCartao == null) {
			quantidadeCartao = 0;
		}
		return quantidadeCartao;
	}

	public void setQuantidadeCartao(Integer quantidadeCartao) {
		this.quantidadeCartao = quantidadeCartao;
	}

	public void consultarConfiguracaoFinanceiroCartao() {
		try {
			FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO = (FormaPagamentoNegociacaoRecebimentoVO) context().getExternalContext().getRequestMap().get("formaPagamentoItens");
			if (!formaPagamentoNegociacaoRecebimentoVO.getConfiguracaoFinanceiroCartaoVO().getCodigo().equals(0)) {
				formaPagamentoNegociacaoRecebimentoVO.setConfiguracaoFinanceiroCartaoVO(getFacadeFactory().getConfiguracaoFinanceiroCartaoFacade().consultarPorChavePrimaria(formaPagamentoNegociacaoRecebimentoVO.getConfiguracaoFinanceiroCartaoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			} else {
				formaPagamentoNegociacaoRecebimentoVO.setConfiguracaoFinanceiroCartaoVO(new ConfiguracaoFinanceiroCartaoVO());
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void apresentarDicaCVCartaoCredito() {
		FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO = (FormaPagamentoNegociacaoRecebimentoVO) context().getExternalContext().getRequestMap().get("formaPagamentoItens");
		formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setApresentarDicaCVCartaoCredito(true);
	}

	public void esconderDicaCVCartaoCredito() {
		FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO = (FormaPagamentoNegociacaoRecebimentoVO) context().getExternalContext().getRequestMap().get("formaPagamentoItens");
		formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setApresentarDicaCVCartaoCredito(false);
	}

	private String modalConfirmacaoPagamento;

	public String getModalConfirmacaoPagamento() {
		if (modalConfirmacaoPagamento == null) {
			modalConfirmacaoPagamento = "";
		}
		return modalConfirmacaoPagamento;
	}

	public void setModalConfirmacaoPagamento(String modalConfirmacaoPagamento) {
		this.modalConfirmacaoPagamento = modalConfirmacaoPagamento;
	}

	/**
	 * @author Victor Hugo de Paula Costa 23/02/2016 15:12
	 */
	private List<ConfiguracaoFinanceiroCartaoVO> configuracaoFinanceiroCartaoVOs;

	public List<ConfiguracaoFinanceiroCartaoVO> getConfiguracaoFinanceiroCartaoVOs() {
		if (configuracaoFinanceiroCartaoVOs == null) {
			configuracaoFinanceiroCartaoVOs = new ArrayList<ConfiguracaoFinanceiroCartaoVO>(0);
		}
		return configuracaoFinanceiroCartaoVOs;
	}

	public void setConfiguracaoFinanceiroCartaoVOs(List<ConfiguracaoFinanceiroCartaoVO> configuracaoFinanceiroCartaoVOs) {
		this.configuracaoFinanceiroCartaoVOs = configuracaoFinanceiroCartaoVOs;
	}

	public Boolean getIsApresentarResiduoPagamentoOnline() {
		return getNegociacaoRecebimentoVO().getResiduo() != 0.0;
	}

	public int getRetornarTamanhoListaConfiguracaoFinanceiroCartaoVOs() {
		if (!getConfiguracaoFinanceiroCartaoVOs().isEmpty())
			return getConfiguracaoFinanceiroCartaoVOs().size();
		return 0;
	}
	
	public boolean getIsExibirScrollCartoes() {
		return getConfiguracaoFinanceiroCartaoVOs().size() > 7;
	}

	public void incicializarDadosPagamentoOnline() {
		getNegociacaoRecebimentoVO().getFormaPagamentoNegociacaoRecebimentoVOs().clear();
		getNegociacaoRecebimentoVO().setValorTotalRecebimento(0.0);
	}

	public void selecionarTodasContaReceber() {
		int cont = 0;
		for (ContaReceberVO obj : getListaContasPagarVisaoAluno()) {
			obj.setSelecionarContaAReceber(getSelecionarContaReceber());
		}
	}

	/**
	 * @author Victor Hugo de Paula Costa 21/03/2016 11:18
	 */
	private Boolean selecionarContaReceber;
	private Map<Integer, ConfiguracaoRecebimentoCartaoOnlineVO> configuracaoRecebimentoCartaoOnlineVOs;
	private ConfiguracaoRecebimentoCartaoOnlineVO configuracaoRecebimentoCartaoOnlineVO;

	public Boolean getSelecionarContaReceber() {
		if (selecionarContaReceber == null)
			selecionarContaReceber = Boolean.FALSE;
		return selecionarContaReceber;
	}

	public void setSelecionarContaReceber(Boolean selecionarContaReceber) {
		this.selecionarContaReceber = selecionarContaReceber;
	}

	

	/**
	 * @author Victor Hugo de Paula Costa 5.0.4.0 07/04/2016
	 */
	private List<TransacaoCartaoOnlineVO> transacaoCartaoOnlineVOs;

	public List<TransacaoCartaoOnlineVO> getTransacaoCartaoOnlineVOs() {
		if (transacaoCartaoOnlineVOs == null) {
			transacaoCartaoOnlineVOs = new ArrayList<TransacaoCartaoOnlineVO>();
		}
		return transacaoCartaoOnlineVOs;
	}

	public void setTransacaoCartaoOnlineVOs(List<TransacaoCartaoOnlineVO> transacaoCartaoOnlineVOs) {
		this.transacaoCartaoOnlineVOs = transacaoCartaoOnlineVOs;
	}

	public void consultarHistoricoTransacao() {
		try {
			ContaReceberRecebimentoVO contaReceberRecebimentoVO = (ContaReceberRecebimentoVO) context().getExternalContext().getRequestMap().get("recebimentoItens");
			setTransacaoCartaoOnlineVOs(getFacadeFactory().getTransacaoCartaoOnlineInterfaceFacade().consultarPorCodigoFormaPagamentoNegociacaoRecebimento(contaReceberRecebimentoVO.getContaReceberVO().getTipoPessoa(), contaReceberRecebimentoVO.getContaReceberVO().getPessoa().getCodigo(), contaReceberRecebimentoVO.getContaReceberVO().getParceiroVO().getCodigo(), contaReceberRecebimentoVO.getContaReceberVO().getFornecedor().getCodigo(), contaReceberRecebimentoVO.getContaReceber(), contaReceberRecebimentoVO.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarHistoricoTransacaoDCC() {
		try {
			FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO = (FormaPagamentoNegociacaoRecebimentoVO) context().getExternalContext().getRequestMap().get("cartaoItem2");
			setTransacaoCartaoOnlineVOs(getFacadeFactory().getTransacaoCartaoOnlineInterfaceFacade().consultarPorCodigoFormaPagamentoNegociacaoRecebimentoDCC(formaPagamentoNegociacaoRecebimentoVO.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	private String modalPagoDCC;

	public String getModalPagoDCC() {
		if (modalPagoDCC == null) {
			modalPagoDCC = "";
		}
		return modalPagoDCC;
	}

	public void setModalPagoDCC(String modalPagoDCC) {
		this.modalPagoDCC = modalPagoDCC;
	}

	private Boolean permitirRecebimentoCartaoCreditoOnline;

	public Boolean getPermitirRecebimentoCartaoCreditoOnline() {
		if (permitirRecebimentoCartaoCreditoOnline == null) {
			permitirRecebimentoCartaoCreditoOnline = false;
		}
		return permitirRecebimentoCartaoCreditoOnline;
	}

	public void setPermitirRecebimentoCartaoCreditoOnline(Boolean permitirRecebimentoCartaoCreditoOnline) {
		this.permitirRecebimentoCartaoCreditoOnline = permitirRecebimentoCartaoCreditoOnline;
	}

	public void persistirLancamentoContabil() {
		try {
			getFacadeFactory().getContaReceberFacade().persistirLancamentoContabilVO(getContaReceberVO(), false, getUsuarioLogado());
			setMensagemID("msg_dados_editar");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void gerarLancamentoContabilPadrao() {
		try {
			getFacadeFactory().getContaReceberFacade().persistirLancamentoContabilPadrao(getContaReceberVO(), false, getUsuarioLogado());
			setMensagemID("msg_dados_editar");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void novoLancamentoContabilCentroNegocioAdministrativo() {
		try {
			setLancamentoContabilCentroNegocioVO(new LancamentoContabilCentroNegocioVO());
			getLancamentoContabilCentroNegocioVO().setLancamentoContabilVO(getLancamentoContabilVO());
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
			setModalAptoParaSerFechado("");
			setMensagemID("msg_dados_editar");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void addLancamentoContabilCentroNegocioVO() {
		try {
			ControleAcesso.incluir(PerfilAcessoPermissaoFinanceiroEnum.PERMITIR_ALTERAR_LANCAMENTO_CONTABIL_RECEBER.getValor(), getUsuarioLogado());
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
			ControleAcesso.incluir(PerfilAcessoPermissaoFinanceiroEnum.PERMITIR_ALTERAR_LANCAMENTO_CONTABIL_RECEBER.getValor(), getUsuarioLogado());
			getFacadeFactory().getContaReceberFacade().addLancamentoContabilVO(getContaReceberVO(), getLancamentoContabilVO(), getUsuarioLogado());
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
			ControleAcesso.incluir(PerfilAcessoPermissaoFinanceiroEnum.PERMITIR_ALTERAR_LANCAMENTO_CONTABIL_RECEBER.getValor(), getUsuarioLogado());
			LancamentoContabilVO obj = (LancamentoContabilVO) context().getExternalContext().getRequestMap().get("lancamentoContabilItens");
			getFacadeFactory().getContaReceberFacade().removeLancamentoContabilVO(getContaReceberVO(), obj, getUsuarioLogado());
			setMensagemID("msg_dados_editar");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void marcarDesmarcarTodosSituacao() {
		if(getMarcarTodos()) {			
			getFiltroRelatorioFinanceiroVO().realizarMarcarTodasSituacoes();
			situacoes.clear();
			situacoes.add(Constantes.AR);
			situacoes.add(Constantes.RE);
			situacoes.add(Constantes.CF);
			situacoes.add(Constantes.NE);
		}else {
			getFiltroRelatorioFinanceiroVO().realizarDesmarcarTodasSituacoes();
			situacoes.clear();
		}
	}

	public void verificarSituacaoMarcadoTodos(String situacao) {		
		if(situacao != null) {
			if(situacoes.contains(situacao)){
				situacoes.remove(situacao);
			}else {
				situacoes.add(situacao);
			}
		}
		setMarcarTodos(situacoes.size() == 4);
	}

	public FiltroRelatorioFinanceiroVO getFiltroRelatorioFinanceiroVO() {
		if (filtroRelatorioFinanceiroVO == null) {
			filtroRelatorioFinanceiroVO = new FiltroRelatorioFinanceiroVO(false);
		}
		return filtroRelatorioFinanceiroVO;
	}

	public void setFiltroRelatorioFinanceiroVO(FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO) {
		this.filtroRelatorioFinanceiroVO = filtroRelatorioFinanceiroVO;
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

	public List getTipoConsultaComboPlanoConta() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("codigo", "Código"));
		itens.add(new SelectItem("identificadorPlanoConta", "Identificador Plano Conta"));
		itens.add(new SelectItem("descricao", "Descrição"));
		return itens;
	}

	public List<SelectItem> getListaSelectItemTipoCategoriaDespesaRateioEnum() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem(TipoCentroNegocioEnum.ACADEMICO, TipoCentroNegocioEnum.ACADEMICO.getDescricao()));
		itens.add(new SelectItem(TipoCentroNegocioEnum.ADMINISTRATIVO, TipoCentroNegocioEnum.ADMINISTRATIVO.getDescricao()));
		return itens;
	}

	public void consultarCursoVO() {
		try {
			setListaConsultaCurso(getFacadeFactory().getCursoFacade().consultar(getCampoConsultaCurso(), getValorConsultaCurso(), getContaReceberVO().getUnidadeEnsino().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCurso(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarCursoVO() {
		try {
			CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItens");
			getLancamentoContabilCentroNegocioVO().setCursoVO(obj);
			listaConsultaCurso.clear();
			this.setValorConsultaCurso("");
			this.setCampoConsultaCurso("");
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
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

	public String getModalAptoParaSerFechado() {
		if (modalAptoParaSerFechado == null) {
			modalAptoParaSerFechado = "";
		}
		return modalAptoParaSerFechado;
	}

	public void setModalAptoParaSerFechado(String modalAptoParaSerFechado) {
		this.modalAptoParaSerFechado = modalAptoParaSerFechado;
	}

	public void isVerificarPermissaoUsuarioPagarReceberContasEmCaixaUnidadeDiferenteDaConta(ContaReceberVO contaReceber) throws Exception {
		if (Uteis.isAtributoPreenchido(getUnidadeEnsinoLogado().getCodigo()) && !getUnidadeEnsinoLogado().getCodigo().equals(contaReceber.getUnidadeEnsinoFinanceira().getCodigo())) {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("ContaCorrente_PagarReceberCaixaUnidadeDiferente", getUsuarioLogado());
		}
	}

	public void verificarPermissaoLancamentoContabilPagar() {
		try {
			if (!getContaReceberVO().isLancamentoContabil()) {
				getContaReceberVO().setLancamentoContabil(getFacadeFactory().getConfiguracaoContabilFacade().consultaSeExisteConfiguracaoContabilPorCodigoUnidadeEnsino(getContaReceberVO().getUnidadeEnsino().getCodigo(), getUsuarioLogado()));
			}
		} catch (Exception e) {
			getContaReceberVO().setLancamentoContabil(false);
		}
	}

	public void inicializarDadosDetalhamentoIndiceReajustePeriodoMatriculaPeriodoVencimento() {
		try {
			getListaIndiceReajustePeriodoMatriculaPeriodoVencimentoVOs().clear();
			String parcela = getContaReceberVO().getParcela().contains("/") ? getContaReceberVO().getParcela().substring(0, getContaReceberVO().getParcela().indexOf("/")) : getContaReceberVO().getParcela();
			String tipoOrigem = getContaReceberVO().getTipoOrigem().equals("MEN") ? "MENSALIDADE" : "BCC";
			List<IndiceReajustePeriodoMatriculaPeriodoVencimentoVO> listaPeriodoVencimentoVOs = getFacadeFactory().getIndiceReajustePeriodoMatriculaPeriodoVencimentoFacade().consultarPorMatriculaPeriodoSituacaoParcela(getContaReceberVO().getMatriculaPeriodo(), SituacaoExecucaoEnum.PROCESSADO, parcela, tipoOrigem, getUsuarioLogado());
			for (IndiceReajustePeriodoMatriculaPeriodoVencimentoVO indiceReajustePeriodoMatriculaPeriodoVencimentoVO : listaPeriodoVencimentoVOs) {
				getListaIndiceReajustePeriodoMatriculaPeriodoVencimentoVOs().add(indiceReajustePeriodoMatriculaPeriodoVencimentoVO);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void inicializarDadosDetalhamentoIndiceReajustePeriodoMatriculaPeriodoVencimentoParcelaRecebidaOuEnviadaRemessa() {
		try {
			getListaIndiceReajustePeriodoMatriculaPeriodoVencimentoVOs().clear();
			String parcela = getContaReceberVO().getParcela().contains("/") ? getContaReceberVO().getParcela().substring(0, getContaReceberVO().getParcela().indexOf("/")) : getContaReceberVO().getParcela();
			String tipoOrigem = getContaReceberVO().getTipoOrigem() == "MEN" ? "MENSALIDADE" : "BCC";
			List<IndiceReajustePeriodoMatriculaPeriodoVencimentoVO> listaPeriodoVencimentoVOs = getFacadeFactory().getIndiceReajustePeriodoMatriculaPeriodoVencimentoFacade().consultarPorMatriculaPeriodoSituacaoParcela(getContaReceberVO().getMatriculaPeriodo(), SituacaoExecucaoEnum.PROCESSADO, parcela, tipoOrigem, getUsuarioLogado());
			for (IndiceReajustePeriodoMatriculaPeriodoVencimentoVO indiceReajustePeriodoMatriculaPeriodoVencimentoVO : listaPeriodoVencimentoVOs) {
				if (indiceReajustePeriodoMatriculaPeriodoVencimentoVO.getValorReferenteDiferencaParcelaRecebidaOuEnviadaRemessa().doubleValue() > 0.0) {
					getListaIndiceReajustePeriodoMatriculaPeriodoVencimentoVOs().add(indiceReajustePeriodoMatriculaPeriodoVencimentoVO);
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void inicializarDadosDetalhamentoIndiceReajustePeriodoMatriculaPeriodoVencimentoCancelamento() {
		try {
			String parcela = getContaReceberVO().getParcela().contains("/") ? getContaReceberVO().getParcela().substring(0, getContaReceberVO().getParcela().indexOf("/")) : getContaReceberVO().getParcela();
			String tipoOrigem = getContaReceberVO().getTipoOrigem() == "MEN" ? "MENSALIDADE" : "BCC";
			setListaIndiceReajustePeriodoMatriculaPeriodoVencimentoCancelamentoVOs(getFacadeFactory().getIndiceReajustePeriodoMatriculaPeriodoVencimentoFacade().consultarPorMatriculaPeriodoSituacaoParcela(getContaReceberVO().getMatriculaPeriodo(), SituacaoExecucaoEnum.CANCELADO, parcela, tipoOrigem, getUsuarioLogado()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<IndiceReajustePeriodoMatriculaPeriodoVencimentoVO> getListaIndiceReajustePeriodoMatriculaPeriodoVencimentoVOs() {
		if (listaIndiceReajustePeriodoMatriculaPeriodoVencimentoVOs == null) {
			listaIndiceReajustePeriodoMatriculaPeriodoVencimentoVOs = new ArrayList<IndiceReajustePeriodoMatriculaPeriodoVencimentoVO>(0);
		}
		return listaIndiceReajustePeriodoMatriculaPeriodoVencimentoVOs;
	}

	public void setListaIndiceReajustePeriodoMatriculaPeriodoVencimentoVOs(List<IndiceReajustePeriodoMatriculaPeriodoVencimentoVO> listaIndiceReajustePeriodoMatriculaPeriodoVencimentoVOs) {
		this.listaIndiceReajustePeriodoMatriculaPeriodoVencimentoVOs = listaIndiceReajustePeriodoMatriculaPeriodoVencimentoVOs;
	}

	public void consultarSituacaoContaReceberRegistroCobranca() {
		try {
			// RegistroNegativacaoCobrancaContaReceberVO registro = getFacadeFactory().getRegistroNegativacaoCobrancaContaReceberFacade().consultarPorChavePrimaria(getContaReceberVO().getRegistroNegativacaoCobrancaContaReceber().getCodigo(), false, getUsuarioLogado());
			// if (getContaReceberVO().getRegistroNegativacaoCobrancaContaReceber().getAgente().getPossuiIntegracao()) {
			// getContaReceberVO().setSituacaoRegistroNegativacaoCobrancaContaReceber(getFacadeFactory().getRegistroNegativacaoCobrancaContaReceberFacade()
			// .consultarSituacaoContaReceber(getContaReceberVO().getRegistroNegativacaoCobrancaContaReceber().getAgente().getIntegracao(), getContaReceberVO().getMatriculaAluno().getMatricula(), 0));
			// } else {
			// throw new Exception("Agente " + getContaReceberVO().getRegistroNegativacaoCobrancaContaReceber().getAgente().getNome() + " não possui integração!");
			// }
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			getContaReceberVO().setSituacaoRegistroNegativacaoCobrancaContaReceber("");
		}
	}

	public String getIsApresentarTextoCheckBoxMarcarDesmarcarTodasSituacoesRequerimento() {
		if (getMarcarTodos()) {
			return UteisJSF.internacionalizar("prt_Inadimplencia_desmarcarTodos");
		}
		return UteisJSF.internacionalizar("prt_Inadimplencia_marcarTodos");
	}

	public Boolean getPermiteImprimirBoletoRecebido() {
		if (permiteImprimirBoletoRecebido == null) {
			permiteImprimirBoletoRecebido = Boolean.FALSE;
		}
		return permiteImprimirBoletoRecebido;
	}

	public void setPermiteImprimirBoletoRecebido(Boolean permiteImprimirBoletoRecebido) {
		this.permiteImprimirBoletoRecebido = permiteImprimirBoletoRecebido;
	}

	public Boolean getMarcarTodos() {
		if (marcarTodos == null) {
			marcarTodos = Boolean.TRUE;
		}
		return marcarTodos;
	}

	public void setMarcarTodos(Boolean marcarTodos) {
		this.marcarTodos = marcarTodos;
	}

	public Map<String, Boolean> getSituacoesContaReceber() {
		if (situacoesContaReceber == null) {
			montarSituacoesContaReceber();
		}
		return situacoesContaReceber;
	}

	public void setSituacoesContaReceber(Map<String, Boolean> situacoesContaReceber) {
		this.situacoesContaReceber = situacoesContaReceber;
	}

	public List<String> getSelectedItems() {
		return situacoes;
	}

	public void setSelectedItems(List<String> selectedItems) {
		this.situacoes = selectedItems;
	}

	public void setListaSelectItemTipoDesconto(List<SelectItem> listaSelectItemTipoDesconto) {
		this.listaSelectItemTipoDesconto = listaSelectItemTipoDesconto;
	}

	public List<String> getSituacoes() {
		return situacoes;
	}

	public void setSituacoes(List<String> situacoes) {
		this.situacoes = situacoes;
	}

	public Boolean getMarcarTodosTipoOrigem() {
		if (marcarTodosTipoOrigem == null) {
			marcarTodosTipoOrigem = Boolean.TRUE;
		}
		return marcarTodosTipoOrigem;
	}

	public void setMarcarTodosTipoOrigem(Boolean marcarTodosTipoOrigem) {
		this.marcarTodosTipoOrigem = marcarTodosTipoOrigem;
	}

	/*
	 * public Boolean setMarcarTodosTipoOrigem(Boolean marcarTodosTipoOrigem) { this.marcarTodosTipoOrigem = marcarTodosTipoOrigem; if(request().getSession().getAttribute("ERRO") !=null && request().getSession().getAttribute("ERRO") instanceof Exception) { setMensagemDetalhada("msg_erro", ((Exception) request().getSession().getAttribute("ERRO")).getMessage()); request().getSession().removeAttribute("ERRO"); return true; } return false; }
	 */

	/**
	 * INICIO DO MERGE EDIGAR - 21/03/18
	 */
	private Boolean apresentarBotaoEditarManualmenteConta;
	protected List<PlanoDescontoVO> listaConsultaPlanoDesconto;
	protected String valorConsultaPlanoDesconto;
	private Boolean adicionandoNovoPlanoDescontoContaReceber;
	private PlanoDescontoContaReceberVO planoDescontoContaReceberVO;
	private Integer indicePlanoDescontoEditar;
	protected List listaSelectTipoDescontoAluno;
	protected List listaSelectTipoPlanoDescontoContaReceber;
	private Boolean utilizaDiaLimite1;
	private Boolean utilizaDiaLimite2;
	private Boolean utilizaDiaLimite3;
	private Boolean utilizaDiaLimite4;
	protected List ordemDesconto;
	protected OrdemDescontoVO ordemDescontoVO;
	private InteracaoWorkflowVO interacaoWorkflowVO;
	private Boolean registrandoNovoFollowUp;
	private Boolean lancadoOuAlteradoAlgumFollowUp;
	private Boolean carregadoFollowUpConta;

	public Boolean getCarregadoFollowUpConta() {
		if (carregadoFollowUpConta == null) {
			carregadoFollowUpConta = Boolean.FALSE;
		}
		return carregadoFollowUpConta;
	}

	public void setCarregadoFollowUpConta(Boolean carregadoFollowUpConta) {
		this.carregadoFollowUpConta = carregadoFollowUpConta;
	}

	private Integer indiceFollowUpEdicao;
	private Boolean permiteAlterarFollowUp;

	public Boolean getPermiteAlterarFollowUp() {
		if (permiteAlterarFollowUp == null) {
			permiteAlterarFollowUp = Boolean.FALSE;
		}
		return permiteAlterarFollowUp;
	}

	public void setPermiteAlterarFollowUp(Boolean permiteAlterarFollowUp) {
		this.permiteAlterarFollowUp = permiteAlterarFollowUp;
	}

	public Integer getIndiceFollowUpEdicao() {
		if (indiceFollowUpEdicao == null) {
			indiceFollowUpEdicao = 0;
		}
		return indiceFollowUpEdicao;
	}

	public void setIndiceFollowUpEdicao(Integer indiceFollowUpEdicao) {
		this.indiceFollowUpEdicao = indiceFollowUpEdicao;
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

	public Boolean getRegistrandoNovoFollowUp() {
		if (registrandoNovoFollowUp == null) {
			registrandoNovoFollowUp = Boolean.FALSE;
		}
		return registrandoNovoFollowUp;
	}

	public void setRegistrandoNovoFollowUp(Boolean registrandoNovoFollowUp) {
		this.registrandoNovoFollowUp = registrandoNovoFollowUp;
	}

	public Boolean getLancadoOuAlteradoAlgumFollowUp() {
		if (lancadoOuAlteradoAlgumFollowUp == null) {
			lancadoOuAlteradoAlgumFollowUp = Boolean.FALSE;
		}
		return lancadoOuAlteradoAlgumFollowUp;
	}

	public void setLancadoOuAlteradoAlgumFollowUp(Boolean lancadoOuAlteradoAlgumFollowUp) {
		this.lancadoOuAlteradoAlgumFollowUp = lancadoOuAlteradoAlgumFollowUp;
	}

	public OrdemDescontoVO getOrdemDescontoVO() {
		if (ordemDescontoVO == null) {
			ordemDescontoVO = new OrdemDescontoVO();
		}
		return ordemDescontoVO;
	}

	public void setOrdemDescontoVO(OrdemDescontoVO ordemDescontoVO) {
		this.ordemDescontoVO = ordemDescontoVO;
	}

	public List getOrdemDesconto() {
		if (ordemDesconto == null) {
			ordemDesconto = new ArrayList<SelectItem>();
		}
		return ordemDesconto;
	}

	public void setOrdemDesconto(List ordemDesconto) {
		this.ordemDesconto = ordemDesconto;
	}

	public void habilitarEditarManualmenteConta() {
		try {
			getContaReceberVO().setContaEditadaManualmente(Boolean.TRUE);
			inicializarConfiguracaoFinanceiro();
			setMensagemID("msg_Conta_Habilitada_Para_Edicao_Manual");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void prepararBuscarPlanoDescontoContaReceber() {
		getListaConsultaPlanoDesconto().clear();
		setValorConsultaPlanoDesconto("");
	}

	public void prepararEditarPlanoDescontoContaReceber() {
		try {
			setOncompleteModal("");
			PlanoDescontoContaReceberVO obj = (PlanoDescontoContaReceberVO) context().getExternalContext().getRequestMap().get("descontoInstitucionaisVO");
			setIndicePlanoDescontoEditar(getContaReceberVO().getPlanoDescontoContaReceberVOs().indexOf(obj));
			if (!obj.getTipoItemPlanoFinanceiro().equals("DM")) {
				if ((obj.getDiasValidadeVencimento() == null) ||
						(obj.getDiasValidadeVencimento().equals(0))) {
					obj.setDiasValidadeVencimento(obj.getPlanoDescontoVO().getDiasValidadeVencimento());
				}
			}
			if (getContaReceberVO().getContaReferenteParcelaConvenio()) {
				montarListaSelectImpostos();
			}
			setPlanoDescontoContaReceberVO((PlanoDescontoContaReceberVO) obj.clone());
			setAdicionandoNovoPlanoDescontoContaReceber(Boolean.FALSE);
			setMensagemID("msg_dados_editar");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void alterarContaCorrenteContaReceber() {
		try {
			this.getContaReceberVO().setNossoNumero("");
			if (this.getContaReceberVO().getCodigo().equals(0)) {
				throw new Exception("A conta a receber deve estar gravada, para que o código de barras e nosso número sejam regerados!");
			}
			if (Uteis.isAtributoPreenchido(this.getContaReceberVO().getContaCorrente())) {
				getContaReceberVO().setContaCorrenteVO(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(this.getContaReceberVO().getContaCorrente(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
//				getFacadeFactory().getContaReceberFacade().gerarDadosBoleto(this.getContaReceberVO(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(contaReceberVO.getUnidadeEnsino().getCodigo()), true, true, getUsuarioLogado());
			}else {
				throw new Exception("Conta Corrente Não Informada!");
			}
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void alterarPlanoDescontoContaReceber() {
		try {
			setOncompleteModal("");
			planoDescontoContaReceberVO.validarDados(getPlanoDescontoContaReceberVO());
			// Para um plano de desconto que já existia, a única alteração que pode ser realizada e na nr dias validade do desconto.
			if ((getPlanoDescontoContaReceberVO().getDiasValidadeVencimento() == null || getPlanoDescontoContaReceberVO().getDiasValidadeVencimento().intValue() == 0) && !getPlanoDescontoContaReceberVO().getPlanoDescontoVO().getUtilizarDescontoSemLimiteValidade()) {
				getPlanoDescontoContaReceberVO().setDiasValidadeVencimento(getPlanoDescontoContaReceberVO().getPlanoDescontoVO().getDiasValidadeVencimento());
				throw new Exception("O campo não pode ser alterado para zero (0) dias.");
			}
			getPlanoDescontoContaReceberVO().setNome(getPlanoDescontoContaReceberVO().getPlanoDescontoVO().getNome());
			getPlanoDescontoContaReceberVO().getPlanoDescontoVO().setDiasValidadeVencimento(getPlanoDescontoContaReceberVO().getDiasValidadeVencimento());
			getPlanoDescontoContaReceberVO().getPlanoDescontoVO().setDescricaoValidade(null); // forcar a atualizacao da descricao
			getPlanoDescontoContaReceberVO().setUsuarioResponsavel(getUsuarioLogadoClone());
			getPlanoDescontoContaReceberVO().setDataConcessaoDesconto(new Date());
			getContaReceberVO().alterarObjPlanoDescontoContaReceberVOs(getPlanoDescontoContaReceberVO(), getIndicePlanoDescontoEditar());
			
			setAdicionandoNovoPlanoDescontoContaReceber(Boolean.FALSE);
			getContaReceberVO().setPlanoDescontoInstitucionalContaReceber(null);
			getContaReceberVO().getPlanoDescontoInstitucionalContaReceber();
			atualizarValoresContaReceber();
			setOncompleteModal("RichFaces.$('panelEditarPlanoDescontoContaReceber').hide()");
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void excluirPlanoDescontoContaReceber() {
		try {
			PlanoDescontoContaReceberVO obj = (PlanoDescontoContaReceberVO) context().getExternalContext().getRequestMap().get("descontoInstitucionaisVO");
			setIndicePlanoDescontoEditar(getContaReceberVO().getPlanoDescontoContaReceberVOs().indexOf(obj));
			getContaReceberVO().setPlanoDescontoInstitucionalContaReceber(null);
			getContaReceberVO().getPlanoDescontoContaReceberVOs().remove(getIndicePlanoDescontoEditar().intValue());
			// a lista abaixo precisa ser reiniciada para ser montada, conforme a nova lista de planos de desconto

			getContaReceberVO().getPlanoDescontoInstitucionalContaReceber();
			atualizarValoresContaReceber();
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void alterarTipoItemNovoPlanoDescontoContaReceber() {
		if (getPlanoDescontoContaReceberVO().getTipoItemPlanoFinanceiro().equals("DM")) {
			getPlanoDescontoContaReceberVO().setPlanoDescontoVO(new PlanoDescontoVO());
			getPlanoDescontoContaReceberVO().getPlanoDescontoVO().setTipoDescontoParcela(getPlanoDescontoContaReceberVO().getTipoDesconto().getValor());
		}
		if (getPlanoDescontoContaReceberVO().getTipoItemPlanoFinanceiro().equals("PD")) {
			getPlanoDescontoContaReceberVO().setPlanoDescontoVO(new PlanoDescontoVO());
			getPlanoDescontoContaReceberVO().getPlanoDescontoVO().setTipoDescontoParcela(getPlanoDescontoContaReceberVO().getTipoDesconto().getValor());
		}
	}

	public void prepararAdicionarNovoPlanoDescontoContaReceber() {
		try {
			setOncompleteModal("");
			setAdicionandoNovoPlanoDescontoContaReceber(Boolean.TRUE);
			setPlanoDescontoContaReceberVO(new PlanoDescontoContaReceberVO());
			getPlanoDescontoContaReceberVO().setUsuarioResponsavel(getUsuarioLogadoClone());
			getPlanoDescontoContaReceberVO().setDataConcessaoDesconto(new Date());
			getPlanoDescontoContaReceberVO().setTipoItemPlanoFinanceiro("DM"); // setando que é do tipo desconto manual.
			getPlanoDescontoContaReceberVO().setOrdemPrioridadeParaCalculo(getContaReceberVO().obterProximoNrPrioridadeDescontosManuaisRegistradosConta());
			getPlanoDescontoContaReceberVO().setNome("Desconto Manual " + (this.getContaReceberVO().obterNrDescontosManuaisRegistradosConta() + 1));
			getPlanoDescontoContaReceberVO().setAplicarSobreValorCheio(getContaReceberVO().getOrdemPlanoDescontoValorCheio());
			if (getContaReceberVO().getContaReferenteParcelaConvenio()) {
				montarListaSelectImpostos();
			}
			setMensagemID("msg_entre_dados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void adicionarNovoPlanoDescontoContaReceber() {
		try {
			setOncompleteModal("");
			getPlanoDescontoContaReceberVO().setUsuarioResponsavel(getUsuarioLogadoClone());
			getPlanoDescontoContaReceberVO().setDataConcessaoDesconto(new Date());
			getPlanoDescontoContaReceberVO().setContaReceber(getContaReceberVO().getCodigo());
			getPlanoDescontoContaReceberVO().getPlanoDescontoVO().setDescricaoValidade(null); // forcar a atualizacao da descricao
			getPlanoDescontoContaReceberVO().inicializarAtributosPlanoDescontoComDadosDescontoManualContaReceber();
			getContaReceberVO().adicionarObjPlanoDescontoContaReceberVOs(getPlanoDescontoContaReceberVO());
			setAdicionandoNovoPlanoDescontoContaReceber(Boolean.FALSE);
			// a lista abaixo precisa ser reiniciada para ser montada, conforme a nova lista de planos de desconto
			getContaReceberVO().setPlanoDescontoInstitucionalContaReceber(null);
			getContaReceberVO().getPlanoDescontoInstitucionalContaReceber();
			atualizarValoresContaReceber();
			setOncompleteModal("RichFaces.$('panelEditarPlanoDescontoContaReceber').hide()");
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void alterarNovoPlanoDescontoContaReceberManual() {
		try {
			setOncompleteModal("");
			// Alterando um novo plano de desconto manual
			if (getPlanoDescontoContaReceberVO().getValorDesconto().doubleValue() <= 0) {
				throw new Exception("Não é possível adicionar um desconto com valor zero.");
			}
			if (getPlanoDescontoContaReceberVO().getNome().trim().isEmpty()) {
				throw new Exception("O campo NOME DESCONTO deve ser informado.");
			}
			if (getContaReceberVO().verificarExistePlanoDescontoMesmoNome(getPlanoDescontoContaReceberVO().getNome(), getIndicePlanoDescontoEditar())) {
				throw new Exception("Não é possível permitido adicionar dois Descontos Manuais com o mesmo nome.");
			}
			getPlanoDescontoContaReceberVO().setUsuarioResponsavel(getUsuarioLogadoClone());
			getPlanoDescontoContaReceberVO().setDataConcessaoDesconto(new Date());
			getPlanoDescontoContaReceberVO().inicializarAtributosPlanoDescontoComDadosDescontoManualContaReceber();
			getPlanoDescontoContaReceberVO().getPlanoDescontoVO().setDescricaoValidade(null); // forcar a atualizacao da descricao
			getContaReceberVO().alterarObjPlanoDescontoContaReceberVOs(getPlanoDescontoContaReceberVO(), getIndicePlanoDescontoEditar());
			setAdicionandoNovoPlanoDescontoContaReceber(Boolean.FALSE);
			getContaReceberVO().setPlanoDescontoInstitucionalContaReceber(null);
			getContaReceberVO().getPlanoDescontoInstitucionalContaReceber();
			atualizarValoresContaReceber();
			setOncompleteModal("RichFaces.$('panelEditarPlanoDescontoContaReceber').hide()");
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void adicionarNovoPlanoDescontoContaReceberManual() {
		try {
			setOncompleteModal("");
			if (getPlanoDescontoContaReceberVO().getValorDesconto().doubleValue() <= 0) {
				throw new Exception("Não é possível adicionar um desconto com valor zero.");
			}
			if (getPlanoDescontoContaReceberVO().getNome().trim().isEmpty()) {
				throw new Exception("O campo NOME DESCONTO deve ser informado.");
			}
			if (getContaReceberVO().verificarExistePlanoDescontoMesmoNome(getPlanoDescontoContaReceberVO().getNome(), -1)) {
				throw new Exception("Não é possível permitido adicionar dois Descontos Manuais com o mesmo nome.");
			}
			getPlanoDescontoContaReceberVO().inicializarAtributosPlanoDescontoComDadosDescontoManualContaReceber();
			getPlanoDescontoContaReceberVO().setUsuarioResponsavel(getUsuarioLogadoClone());
			getPlanoDescontoContaReceberVO().setDataConcessaoDesconto(new Date());
			getPlanoDescontoContaReceberVO().getPlanoDescontoVO().setDescricaoValidade(null); // forcar a atualizacao da descricao
			getPlanoDescontoContaReceberVO().setContaReceber(getContaReceberVO().getCodigo());
			getContaReceberVO().adicionarObjPlanoDescontoContaReceberVOs(getPlanoDescontoContaReceberVO());
			setAdicionandoNovoPlanoDescontoContaReceber(Boolean.FALSE);
			// a lista abaixo precisa ser reiniciada para ser montada, conforme a nova lista de planos de desconto
			getContaReceberVO().setPlanoDescontoInstitucionalContaReceber(null);
			getContaReceberVO().getPlanoDescontoInstitucionalContaReceber();
			atualizarValoresContaReceber();
			setOncompleteModal("RichFaces.$('panelEditarPlanoDescontoContaReceber').hide()");
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void calcularPorcentagemCentroResultadoOrigem() {
		try {
			getCentroResultadoOrigemVO().calcularPorcentagem(getContaReceberVO().getValorReceberCalculado());
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void calcularValorCentroResultadoOrigem() {
		try {
			getCentroResultadoOrigemVO().calcularValor(getContaReceberVO().getValorReceberCalculado());
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void visualizarCentroResultadoOrigemDetalhe() {
		try {
			
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}

	}
	
	public void fecharCentroResultadoOrigemDetalhe() {
		try {			
			
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
		
	}

	public void adicionarCentroResultadoOrigem() {
		try {
			Uteis.checkState(!Uteis.isAtributoPreenchido(getContaReceberVO().getValorReceberCalculado()), "O campo Valor (Aba Dados Básicos) deve ser informado. ");
			getCentroResultadoOrigemVO().setQuantidade(1.0);
			if (getContaReceberVO().getTipoOrigemContaReceber().isBiblioteca()) {
				getCentroResultadoOrigemVO().setTipoNivelCentroResultadoEnum(TipoNivelCentroResultadoEnum.UNIDADE_ENSINO);
			}
			getCentroResultadoOrigemVO().setTipoMovimentacaoCentroResultadoOrigemEnum(TipoMovimentacaoCentroResultadoOrigemEnum.ENTRADA);
			getCentroResultadoOrigemVO().setTipoCentroResultadoOrigemEnum(TipoCentroResultadoOrigemEnum.CONTA_RECEBER);
			getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().adicionarCentroResultadoOrigem(getContaReceberVO().getListaCentroResultadoOrigem(), getCentroResultadoOrigemVO(), getContaReceberVO().getValorReceberCalculado(), true, getUsuarioLogado());
			if(getContaReceberVO().getListaCentroResultadoOrigem().size()>=2){
				getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().removerCentroResultadoOrigem(getContaReceberVO().getListaCentroResultadoOrigem(), getCentroResultadoOrigemVO(), getUsuarioLogado());
				Uteis.checkState(true, "Não é possível adicionar mais de um Centro de Resultado Movimentação para conta receber.");
			}
			setCentroResultadoOrigemVO(new CentroResultadoOrigemVO());
			validarCentroResultadoPorContaReceber();
			setMensagemID(MSG_TELA.msg_dados_adicionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	
	public void editarCentroResultadoOrigem() {
		try {
			setCentroResultadoOrigemVO((CentroResultadoOrigemVO) context().getExternalContext().getRequestMap().get("centroResultadoOrigemItem"));
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
		
	}

	public void removerCentroResultadoOrigem() {
		try {
			CentroResultadoOrigemVO centroResultadoOrigem = (CentroResultadoOrigemVO) context().getExternalContext().getRequestMap().get("centroResultadoOrigemItem");
			getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().removerCentroResultadoOrigem(getContaReceberVO().getListaCentroResultadoOrigem(), centroResultadoOrigem, getUsuarioLogado());
			setMensagemID(MSG_TELA.msg_dados_excluidos.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void inicializarDadosCentroResultadoConsulta() {
		try {
			setCentroResultadoDataModelo(new DataModelo());
			getCentroResultadoDataModelo().setCampoConsulta(CentroResultadoVO.enumCampoConsultaCentroResultado.DESCRICAO_CENTRO_RESULTADO.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void selecionarCentroResultado() {
		try {
			CentroResultadoVO obj = (CentroResultadoVO) context().getExternalContext().getRequestMap().get("centroResultadoItens");
			getCentroResultadoOrigemVO().setCentroResultadoAdministrativo(obj);
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
	
	
	public void consultarCentroReceitaCentroResultado() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaCentroReceitaCentroResultado().equals("descricao")) {
				objs = getFacadeFactory().getCentroReceitaFacade().consultarPorDescricao(getValorConsultaCentroReceitaCentroResultado(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultaCentroReceitaCentroResultado().equals("identificadorCentroReceita")) {
				objs = getFacadeFactory().getCentroReceitaFacade().consultarPorIdentificadorCentroReceita(getValorConsultaCentroReceitaCentroResultado(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultaCentroReceitaCentroResultado().equals("nomeDepartamento")) {
				objs = getFacadeFactory().getCentroReceitaFacade().consultarPorNomeDepartamento(getValorConsultaCentroReceitaCentroResultado(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			setListaConsultaCentroReceitaCentroResultado(objs);
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setListaConsultaCentroReceita(new ArrayList(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void selecionarCentroReceitaCentroResultado() {
		try {
			CentroReceitaVO obj = (CentroReceitaVO) context().getExternalContext().getRequestMap().get("centroReceitaItens");
			getCentroResultadoOrigemVO().setCentroReceitaVO(obj);
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public DataModelo getCentroResultadoDataModelo() {
		centroResultadoDataModelo = Optional.ofNullable(centroResultadoDataModelo).orElse(new DataModelo());
		return centroResultadoDataModelo;
	}

	public void setCentroResultadoDataModelo(DataModelo centroResultadoDataModelo) {
		this.centroResultadoDataModelo = centroResultadoDataModelo;
	}

	public CentroResultadoOrigemVO getCentroResultadoOrigemVO() {
		centroResultadoOrigemVO = Optional.ofNullable(centroResultadoOrigemVO).orElse(new CentroResultadoOrigemVO());
		return centroResultadoOrigemVO;
	}

	public void setCentroResultadoOrigemVO(CentroResultadoOrigemVO centroResultadoOrigemVO) {
		this.centroResultadoOrigemVO = centroResultadoOrigemVO;
	}

	public boolean isPermiteAlterarCentroResultado() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoFinanceiroEnum.PERMITE_ALTERAR_CENTRO_RESULTADO_CONTA_RECEBER, getUsuarioLogado());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public List<CentroReceitaVO> getListaConsultaCentroReceitaCentroResultado() {
		listaConsultaCentroReceitaCentroResultado = Optional.ofNullable(listaConsultaCentroReceitaCentroResultado).orElse(new ArrayList<>());
		return listaConsultaCentroReceitaCentroResultado;
	}

	public void setListaConsultaCentroReceitaCentroResultado(List<CentroReceitaVO> listaConsultaCentroReceitaCentroResultado) {
		this.listaConsultaCentroReceitaCentroResultado = listaConsultaCentroReceitaCentroResultado;
	}

	public String getValorConsultaCentroReceitaCentroResultado() {
		valorConsultaCentroReceitaCentroResultado = Optional.ofNullable(valorConsultaCentroReceitaCentroResultado).orElse("");
		return valorConsultaCentroReceitaCentroResultado;
	}

	public void setValorConsultaCentroReceitaCentroResultado(String valorConsultaCentroReceitaCentroResultado) {
		this.valorConsultaCentroReceitaCentroResultado = valorConsultaCentroReceitaCentroResultado;
	}

	public String getCampoConsultaCentroReceitaCentroResultado() {
		campoConsultaCentroReceitaCentroResultado = Optional.ofNullable(campoConsultaCentroReceitaCentroResultado).orElse("");
		return campoConsultaCentroReceitaCentroResultado;
	}

	public void setCampoConsultaCentroReceitaCentroResultado(String campoConsultaCentroReceitaCentroResultado) {
		this.campoConsultaCentroReceitaCentroResultado = campoConsultaCentroReceitaCentroResultado;
	}

	public PlanoDescontoContaReceberVO getPlanoDescontoContaReceberVO() {
		if (planoDescontoContaReceberVO == null) {
			planoDescontoContaReceberVO = new PlanoDescontoContaReceberVO();
		}
		return planoDescontoContaReceberVO;
	}

	public void setPlanoDescontoContaReceberVO(PlanoDescontoContaReceberVO planoDescontoContaReceberVO) {
		this.planoDescontoContaReceberVO = planoDescontoContaReceberVO;
	}

	public Boolean getAdicionandoNovoPlanoDescontoContaReceber() {
		if (adicionandoNovoPlanoDescontoContaReceber == null) {
			adicionandoNovoPlanoDescontoContaReceber = Boolean.FALSE;
		}
		return adicionandoNovoPlanoDescontoContaReceber;
	}

	public void setAdicionandoNovoPlanoDescontoContaReceber(Boolean adicionandoNovoPlanoDescontoContaReceber) {
		this.adicionandoNovoPlanoDescontoContaReceber = adicionandoNovoPlanoDescontoContaReceber;
	}

	public Integer getIndicePlanoDescontoEditar() {
		if (indicePlanoDescontoEditar == null) {
			indicePlanoDescontoEditar = 0;
		}
		return indicePlanoDescontoEditar;
	}

	public void setIndicePlanoDescontoEditar(Integer indicePlanoDescontoEditar) {
		this.indicePlanoDescontoEditar = indicePlanoDescontoEditar;
	}

	public List getListaSelectTipoDescontoAluno() {
		if (listaSelectTipoDescontoAluno == null) {
			listaSelectTipoDescontoAluno = UtilSelectItem.getListaSelectItemEnum(TipoDescontoAluno.values(), Obrigatorio.SIM);
		}
		return listaSelectTipoDescontoAluno;
	}

	public void setListaSelectTipoDescontoAluno(List listaSelectTipoDescontoAluno) {
		this.listaSelectTipoDescontoAluno = listaSelectTipoDescontoAluno;
	}

	public List getListaSelectTipoPlanoDescontoContaReceber() {
		if (listaSelectTipoPlanoDescontoContaReceber == null) {
			listaSelectTipoPlanoDescontoContaReceber = new ArrayList<SelectItem>();
			listaSelectTipoPlanoDescontoContaReceber.add(new SelectItem("PD", "Plano de Desconto"));
			// listaSelectTipoPlanoDescontoContaReceber.add(new SelectItem("CO", "Convênio"));
			listaSelectTipoPlanoDescontoContaReceber.add(new SelectItem("DM", "Desconto Manual"));
		}
		return listaSelectTipoPlanoDescontoContaReceber;
	}

	public void setListaSelectTipoPlanoDescontoContaReceber(List listaSelectTipoPlanoDescontoContaReceber) {
		this.listaSelectTipoPlanoDescontoContaReceber = listaSelectTipoPlanoDescontoContaReceber;
	}

	private List listaSelectImpostos;

	public List getListaSelectImpostos() {
		if (listaSelectImpostos == null) {
			listaSelectImpostos = new ArrayList<SelectItem>();
		}
		return listaSelectImpostos;
	}

	public void setListaSelectImpostos(List listaSelectImpostos) {
		this.listaSelectImpostos = listaSelectImpostos;
	}

	public void montarListaSelectImpostos() throws Exception {
		List resultadoConsulta = null;
		try {
			setListaSelectImpostos(new ArrayList<SelectItem>());
			DataModelo prmConsulta = new DataModelo();
			prmConsulta.setCampoConsulta("nome");
			prmConsulta.setUsuario(getUsuarioLogadoClone());
			prmConsulta.setControlarAcesso(false);
			prmConsulta.setLimitePorPagina(0);
			List lista = getFacadeFactory().getImpostoFacade().consultaRapidaPorNome("", prmConsulta);
			setListaSelectImpostos(UtilSelectItem.getListaSelectItem(lista, "codigo", "nome"));
		} catch (Exception e) {
			throw e;
		}
	}

	public void realizarMarcacaoDiaUtil() {
		getPlanoDescontoContaReceberVO().setUtilizarDiaFixo(false);
		getPlanoDescontoContaReceberVO().setUtilizarAvancoDiaUtil(false);
		getPlanoDescontoContaReceberVO().setDescontoValidoAteDataVencimento(false);
	}

	public void realizarMarcacaoDiaFixo() {
		getPlanoDescontoContaReceberVO().setUtilizarDiaUtil(false);
		getPlanoDescontoContaReceberVO().setDescontoValidoAteDataVencimento(false);
	}

	public void realizarMarcacaoDescontoValidoAteDataVencimento() {
		if (getPlanoDescontoContaReceberVO().getDescontoValidoAteDataVencimento().booleanValue()) {
			getPlanoDescontoContaReceberVO().setDiasValidadeVencimento(0);
			getPlanoDescontoContaReceberVO().setUtilizarDiaUtil(false);
			getPlanoDescontoContaReceberVO().setUtilizarDiaFixo(false);
			getPlanoDescontoContaReceberVO().setUtilizarAvancoDiaUtil(false);
		}
	}

	public void atualizarAplicarSobreValorBaseDeduzidoValorOutrosPlanosDesconto() {
		if (getPlanoDescontoContaReceberVO().getAplicarSobreValorCheio()) {
			getPlanoDescontoContaReceberVO().setAplicarSobreValorBaseDeduzidoValorOutrosPlanosDesconto(Boolean.FALSE);
		} else {
			getPlanoDescontoContaReceberVO().setAplicarSobreValorBaseDeduzidoValorOutrosPlanosDesconto(Boolean.TRUE);
		}
	}

	public List<PlanoDescontoVO> getListaConsultaPlanoDesconto() {
		if (listaConsultaPlanoDesconto == null) {
			listaConsultaPlanoDesconto = new ArrayList<PlanoDescontoVO>();
		}
		return listaConsultaPlanoDesconto;
	}

	public void setListaConsultaPlanoDesconto(List listaConsultaPlanoDesconto) {
		this.listaConsultaPlanoDesconto = listaConsultaPlanoDesconto;
	}

	public String getValorConsultaPlanoDesconto() {
		if (valorConsultaPlanoDesconto == null) {
			valorConsultaPlanoDesconto = "";
		}
		return valorConsultaPlanoDesconto;
	}

	public void setValorConsultaPlanoDesconto(String valorConsultaPlanoDesconto) {
		this.valorConsultaPlanoDesconto = valorConsultaPlanoDesconto;
	}

	public void consultarPlanoDesconto() {
		try {
			List objs = new ArrayList(0);
			objs = getFacadeFactory().getPlanoDescontoFacade().consultarPorNomeSomenteAtivaConsiderandoUnidadeEnsino(getValorConsultaPlanoDesconto(), getContaReceberVO().getUnidadeEnsino().getCodigo(), false, getUsuarioLogado());
			setListaConsultaPlanoDesconto(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			getListaConsultaPlanoDesconto().clear();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarPlanoDescontoContaReceber() {
		PlanoDescontoVO obj = (PlanoDescontoVO) context().getExternalContext().getRequestMap().get("planoDescontoItens");
		// remontando a ordem de prioridade que já estava setada no momento de adicionar o plano de desconto a conta
		obj.setOrdemPrioridadeParaCalculo(getPlanoDescontoContaReceberVO().getOrdemPrioridadeParaCalculo());
		getPlanoDescontoContaReceberVO().setPlanoDescontoVO(obj);
		getPlanoDescontoContaReceberVO().setDiasValidadeVencimento(obj.getDiasValidadeVencimento());
	}

	public Boolean getUtilizaDiaLimite1() {
		if (utilizaDiaLimite1 == null) {
			utilizaDiaLimite1 = Boolean.FALSE;
		}
		return utilizaDiaLimite1;
	}

	public void setUtilizaDiaLimite1(Boolean utilizaDiaLimite1) {
		this.utilizaDiaLimite1 = utilizaDiaLimite1;
	}

	public Boolean getUtilizaDiaLimite2() {
		if (utilizaDiaLimite2 == null) {
			utilizaDiaLimite2 = Boolean.FALSE;
		}
		return utilizaDiaLimite2;
	}

	public void setUtilizaDiaLimite2(Boolean utilizaDiaLimite2) {
		this.utilizaDiaLimite2 = utilizaDiaLimite2;
	}

	public Boolean getUtilizaDiaLimite3() {
		if (utilizaDiaLimite3 == null) {
			utilizaDiaLimite3 = Boolean.FALSE;
		}
		return utilizaDiaLimite3;
	}

	public void setUtilizaDiaLimite3(Boolean utilizaDiaLimite3) {
		this.utilizaDiaLimite3 = utilizaDiaLimite3;
	}

	public Boolean getUtilizaDiaLimite4() {
		if (utilizaDiaLimite4 == null) {
			utilizaDiaLimite4 = Boolean.FALSE;
		}
		return utilizaDiaLimite4;
	}

	public void setUtilizaDiaLimite4(Boolean utilizaDiaLimite4) {
		this.utilizaDiaLimite4 = utilizaDiaLimite4;
	}

	public void prepararEditarDescontoProgressivoContaReceber() {
		try {
			this.setUtilizaDiaLimite1(Boolean.FALSE);
			this.setUtilizaDiaLimite2(Boolean.FALSE);
			this.setUtilizaDiaLimite3(Boolean.FALSE);
			this.setUtilizaDiaLimite4(Boolean.FALSE);

			if (getContaReceberVO().getCodigo().intValue() == 0) {
				throw new Exception("Este conta a receber não utiliza desconto progressivo.");
			}

			// DescontoProgressivoVO descProdVO = getFacadeFactory().getDescontoProgressivoFacade().consultarPorChavePrimaria(getContaReceberVO().getDescontoProgressivo().getCodigo(), getUsuarioLogado());
			// getContaReceberVO().setDescontoProgressivo(descProdVO);

			if (getContaReceberVO().getDescontoProgressivo().getDiaLimite1().intValue() > 0) {
				this.setUtilizaDiaLimite1(Boolean.TRUE);
			}
			if (getContaReceberVO().getDescontoProgressivo().getDiaLimite2().intValue() > 0) {
				this.setUtilizaDiaLimite2(Boolean.TRUE);
			}
			if (getContaReceberVO().getDescontoProgressivo().getDiaLimite3().intValue() > 0) {
				this.setUtilizaDiaLimite3(Boolean.TRUE);
			}
			if (getContaReceberVO().getDescontoProgressivo().getDiaLimite4().intValue() > 0) {
				this.setUtilizaDiaLimite4(Boolean.TRUE);
			}
			if ((getContaReceberVO().getDiaLimite1() == null) &&
					(this.getUtilizaDiaLimite1())) {
				getContaReceberVO().setDiaLimite1(getContaReceberVO().getDescontoProgressivo().getDiaLimite1());
			}
			if ((getContaReceberVO().getDiaLimite2() == null) &&
					(this.getUtilizaDiaLimite2())) {
				getContaReceberVO().setDiaLimite2(getContaReceberVO().getDescontoProgressivo().getDiaLimite2());
			}
			if ((getContaReceberVO().getDiaLimite3() == null) &&
					(this.getUtilizaDiaLimite3())) {
				getContaReceberVO().setDiaLimite3(getContaReceberVO().getDescontoProgressivo().getDiaLimite3());
			}
			if ((getContaReceberVO().getDiaLimite4() == null) &&
					(this.getUtilizaDiaLimite4())) {
				getContaReceberVO().setDiaLimite4(getContaReceberVO().getDescontoProgressivo().getDiaLimite4());
			}
			setMensagemID("msg_dados_editar");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void alterarDescontoProgressivoContaReceber() {
		try {
			if ((!this.getUtilizaDiaLimite1()) &&
					(!this.getUtilizaDiaLimite2()) &&
					(!this.getUtilizaDiaLimite3()) &&
					(!this.getUtilizaDiaLimite4())) {
				throw new Exception("Desconto progressivo não pode ser alterado - Conta a Receber não está utilizando desconto de progressivo!");
			}

			this.getContaReceberVO().setUtilizarDescontoProgressivoManual(Boolean.TRUE);
			this.getContaReceberVO().getDescontoProgressivo().setDescontoProgressivoEditadoManualmenteContaReceber(Boolean.TRUE);
			if (this.getUtilizaDiaLimite1()) {
				getContaReceberVO().getDescontoProgressivo().setDiaLimite1(getContaReceberVO().getDiaLimite1());
			}
			if (this.getUtilizaDiaLimite2()) {
				getContaReceberVO().getDescontoProgressivo().setDiaLimite2(getContaReceberVO().getDiaLimite2());
			}
			if (this.getUtilizaDiaLimite3()) {
				getContaReceberVO().getDescontoProgressivo().setDiaLimite3(getContaReceberVO().getDiaLimite3());
			}
			if (this.getUtilizaDiaLimite4()) {
				getContaReceberVO().getDescontoProgressivo().setDiaLimite4(getContaReceberVO().getDiaLimite4());
			}
			DescontoProgressivoVO.validarDados(getContaReceberVO().getDescontoProgressivo());

			atualizarValoresContaReceber();
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void subirItemOrdemDesconto() {
		try {
			OrdemDescontoVO ordemSubir = (OrdemDescontoVO) context().getExternalContext().getRequestMap().get("ordemItens");
			setOrdemDescontoVO(ordemSubir);
			this.getContaReceberVO().alterarOrdemAplicacaoDescontosSubindoItem(this.ordemDesconto, ordemSubir);
		} catch (Exception e) {
			setMensagemDetalhada("msg_Ordem_Desconto_Alterada_Sucesso", e.getMessage(), Uteis.ERRO);
		}
	}

	public void descerItemOrdemDesconto() {
		try {
			setOrdemDescontoVO((OrdemDescontoVO) context().getExternalContext().getRequestMap().get("ordemItens"));
			this.getContaReceberVO().alterarOrdemAplicacaoDescontosDescentoItem(this.ordemDesconto, getOrdemDescontoVO());
			setMensagemID("msg_Ordem_Desconto_Alterada_Sucesso");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void aplicarAlteracaoOrdemDescontos() {
		if (this.getContaReceberVO().getContaEditadaManualmente()) {
			atualizarValoresContaReceber();
		}
	}

	public void prepararAlterarOrdemDescontoContaReceber() {
		setOrdemDesconto(this.getContaReceberVO().obterOrdemAplicacaoDescontosPadraoAtual());
	}

	public void atualizarValoresContaReceber() {
		try {
			if ((this.getContaReceberVO().getContaEditadaManualmente()) 
				|| (!getFinanceiroManualMatriculaPeriodo() && getPermissaoAlterarDescontoFinanceiroManualDesativado())
				|| !getIsPermissaoFinanceiroManual_Valor()) {				
				getContaReceberVO().setPrecisaCalcularDesconto(true);
				if(!getPossuiBolsaCusteada()) {
					getContaReceberVO().setValorBaseContaReceber(getContaReceberVO().getValor());
				}
				getContaReceberVO().setDataOriginalVencimento(getContaReceberVO().getDataVencimento());
				List<ContaCorrenteVO> lista = new ArrayList<>();
				lista.add(getContaReceberVO().getContaCorrenteVO());
				if (Uteis.isAtributoPreenchido(getContaReceberVO().getNossoNumero())
						&& Uteis.isAtributoPreenchido(getFacadeFactory().getControleRemessaContaReceberFacade().consultaRapidaContaArquivoRemessaPorNossoNumeroContaReceber(lista, getContaReceberVO().getNossoNumero()))
						) {
					getContaReceberVO().setNossoNumero("");
				}				
				getContaReceberVO().getCalcularValorFinal(null, this.getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getContaReceberVO().getUnidadeEnsino().getCodigo()), false, null);
				getFacadeFactory().getContaReceberFacade().realizarAtualizacaoValorDescontoFaixaContaReceber(getContaReceberVO(), this.getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getContaReceberVO().getUnidadeEnsino().getCodigo()), false);
				getFacadeFactory().getDetalhamentoValorContaInterfaceFacade().validarGeracaoCentroResultadoOrigemDetalhePadrao(getContaReceberVO(), this.getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getContaReceberVO().getUnidadeEnsino().getCodigo()), getUsuarioLogado());
				for (CentroResultadoOrigemVO cro : getContaReceberVO().getListaCentroResultadoOrigem()) {
					cro.calcularValor(getContaReceberVO().getValorReceberCalculado());					
				}
			}
		} catch (ConsistirException e) {
			if (e.getReferenteDescontoContaReceber()) {
				setDescontoMaiorValorContaReceber(e.getReferenteDescontoContaReceber());
			} 
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void atualizarValoresContaReceberPorDescontoAluno() {
		try {
			getContaReceberVO().setValorDesconto(getContaReceberVO().getValorDescontoAlunoJaCalculado());
			atualizarValoresContaReceber();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void prepararFollowUpContaReceber() {
		try {
			if (getCarregadoFollowUpConta()) {
				return;
			}
			if (getContaReceberVO().getCodigo().intValue() == 0) {
				// se a conta é nova, vamos inicializar os dados para lancer novos followups
				setCarregadoFollowUpConta(Boolean.TRUE);
				verificarPermissaoAlterarFollowUp();
				setMensagemID("msg_dados_consultados");
				return;
			}
			setCarregadoFollowUpConta(Boolean.TRUE);
			verificarPermissaoAlterarFollowUp();
			Integer codigoProspect = 0;
			Integer codigoPessoa = 0;
			FollowUpVO followUpVO = new FollowUpVO();

			if (this.getContaReceberVO().getTipoAluno()) {
				// neste caso temos que mapear o proposct para relativo a pessoa
				codigoPessoa = this.getContaReceberVO().getPessoa().getCodigo();
				ProspectsVO prospectVO = getFacadeFactory().getProspectsFacade().consultarPorCodigoPessoa(codigoPessoa, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				getContaReceberVO().setProspectVO(prospectVO);
				codigoProspect = prospectVO.getCodigo();
				if (codigoProspect != null && codigoProspect != 0) {
					followUpVO.getProspect().setCodigo(codigoProspect);
					// followUpVO = getFacadeFactory().getFollowUpFacade().consultarFollowUpPorCodigoProspect(codigoProspect, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				} else if (codigoPessoa != null && codigoPessoa != 0) {
					PessoaVO pessoaVO = getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(codigoPessoa, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
					getFacadeFactory().getProspectsFacade().alterarProspectConformePessoa(pessoaVO, false, getUsuarioLogado());
					followUpVO = getFacadeFactory().getFollowUpFacade().consultarFollowUpPorCodigoPessoa(codigoPessoa, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
					if (followUpVO.getProspect().getCodigo().intValue() == 0) {
						followUpVO.getProspect().setPessoa(getContaReceberVO().getPessoa());
						if (!getAplicacaoControle().getConfiguracaoGeralSistemaVO(getContaReceberVO().getUnidadeEnsino().getCodigo(), getUsuarioLogado()).getCriarProspectAluno()) {
							followUpVO.getProspect().getPessoa().setGerarProspectInativo(Boolean.TRUE);
						}
						followUpVO.setProspect(getFacadeFactory().getPessoaFacade().realizarVinculoPessoaProspect(followUpVO.getProspect().getPessoa(), getUsuarioLogado()));
					}
				}
				String identificadorOrigem = getContaReceberVO().getIdentificadorOrigem();
				Integer codigoEntidadeOrigem = getContaReceberVO().getCodigoEntidadeOrigem();
				getContaReceberVO().setListaFollowUpConta(getFacadeFactory().getFollowUpFacade().consultarInteracoes(followUpVO.getCodigo(), 0, 0, 0, TipoOrigemInteracaoEnum.CONTA_RECEBER, getContaReceberVO().getIdentificadorOrigem(), getContaReceberVO().getCodigoEntidadeOrigem(), null, null, followUpVO, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			} else {
				// neste caso iremos buscar a interacao (followup) sem considerar um prospect especifico.
				// Para estes casos, o followup é registrado vinculado a
				String identificadorOrigem = getContaReceberVO().getIdentificadorOrigem();
				Integer codigoEntidadeOrigem = getContaReceberVO().getCodigoEntidadeOrigem();
				getContaReceberVO().setListaFollowUpConta(getFacadeFactory().getFollowUpFacade().consultarInteracoes(followUpVO.getCodigo(), 0, 0, 0, TipoOrigemInteracaoEnum.CONTA_RECEBER, getContaReceberVO().getIdentificadorOrigem(), getContaReceberVO().getCodigoEntidadeOrigem(), null, null, followUpVO, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void alterarAdicionarFollowUpConta() {
		try {
			if (getRegistrandoNovoFollowUp()) {
				if (!getLoginControle().getPermissaoAcessoMenuVO().getGravarInteracaoFollowUp()) {
					throw new Exception("Usuário não possuir informação para gravar Observações / Interações FollowUp.");
				}
				getContaReceberVO().getListaFollowUpConta().add(getInteracaoWorkflowVO());
			} else {
				getContaReceberVO().getListaFollowUpConta().set(getIndiceFollowUpEdicao(), getInteracaoWorkflowVO());
			}
			setInteracaoWorkflowVO(null);
			setRegistrandoNovoFollowUp(Boolean.FALSE);
			setLancadoOuAlteradoAlgumFollowUp(Boolean.TRUE);
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void prepararAdicionarFollowUpConta() {
		try {
			setInteracaoWorkflowVO(new InteracaoWorkflowVO());
			getInteracaoWorkflowVO().setResponsavel(getUsuarioLogadoClone());
			getInteracaoWorkflowVO().setDataInicio(new Date());
			getInteracaoWorkflowVO().setHoraInicio(Uteis.getHoraAtual());
			getInteracaoWorkflowVO().setDataTermino(new Date());
			getInteracaoWorkflowVO().setHoraTermino(Uteis.getHoraAtual());
			getInteracaoWorkflowVO().setTipoOrigemInteracao(TipoOrigemInteracaoEnum.CONTA_RECEBER);
			getInteracaoWorkflowVO().setIdentificadorOrigem(getContaReceberVO().getIdentificadorOrigem());
			getInteracaoWorkflowVO().setCodigoEntidadeOrigem(getContaReceberVO().getCodigoEntidadeOrigem());
			if (getContaReceberVO().getTipoAluno()) {
				getInteracaoWorkflowVO().setProspect(getContaReceberVO().getProspectVO());
				// followUpVO = getFacadeFactory().getFollowUpFacade().consultarFollowUpPorCodigoProspect(codigoProspect, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			getInteracaoWorkflowVO().setObservacao("");
			getInteracaoWorkflowVO().setTipoInteracao(TipoInteracaoEnum.TELEFONE);
			setRegistrandoNovoFollowUp(Boolean.TRUE);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void preparaAlterarFollowUpConta() {
		try {
			InteracaoWorkflowVO interacaoSelecionada = (InteracaoWorkflowVO) context().getExternalContext().getRequestMap().get("followUpConta");
			int indice = getContaReceberVO().getListaFollowUpConta().indexOf(interacaoSelecionada);
			setIndiceFollowUpEdicao(indice);
			setInteracaoWorkflowVO(interacaoSelecionada);
			// atualizando data, hora e responsabel pela observacao.
			getInteracaoWorkflowVO().setResponsavel(getUsuarioLogadoClone());
			getInteracaoWorkflowVO().setDataInicio(new Date());
			getInteracaoWorkflowVO().setHoraInicio(Uteis.getHoraAtual());
			getInteracaoWorkflowVO().setDataTermino(new Date());
			getInteracaoWorkflowVO().setHoraTermino(Uteis.getHoraAtual());
			setRegistrandoNovoFollowUp(Boolean.FALSE);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void verificarPermissaoAlterarFollowUp() {
		setPermiteAlterarFollowUp(Boolean.FALSE);
		if (getLoginControle().getPermissaoAcessoMenuVO().getContaReceber_PermitirEditarManualmenteConta()) {
			setPermiteAlterarFollowUp(Boolean.TRUE);
		}
	}

	public void gravarFollowUpContas() {
		try {
			if (this.getLancadoOuAlteradoAlgumFollowUp()) {
				getFacadeFactory().getInteracaoWorkflowFacade().persistirListaInteracaoWorkflow(getContaReceberVO().getListaFollowUpConta(), false, getUsuarioLogado());
				this.setLancadoOuAlteradoAlgumFollowUp(Boolean.FALSE);
			}
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void regerarConvenio() {
		try {
			getFacadeFactory().getContaReceberFacade().regerarBolsaCusteadaConvenio(getContaReceberVO(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getContaReceberVO().getUnidadeEnsinoFinanceira().getCodigo()), getUsuarioLogadoClone());
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void carregarDadosLiberarFuncionalidadeConvenioCusteado() {
		try {
			setPlanoDescontoContaReceberVO(new PlanoDescontoContaReceberVO());
			setUserNameLiberarFuncionalidade("");
			setSenhaLiberarFuncionalidade("");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void carregarDadosLiberarFuncionalidadeConvenio() {
		try {
			setPlanoDescontoContaReceberVO(new PlanoDescontoContaReceberVO());
			setPlanoDescontoContaReceberVO((PlanoDescontoContaReceberVO) context().getExternalContext().getRequestMap().get("descontoConveniosVO"));
			setUserNameLiberarFuncionalidade("");
			setSenhaLiberarFuncionalidade("");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void realizarVerificacaoUsuarioLiberacaoSuspensaoConvenio() {
		try {
			UsuarioVO usuarioVerif = ControleAcesso.verificarLoginUsuario(this.getUserNameLiberarFuncionalidade(), this.getSenhaLiberarFuncionalidade(), true, Uteis.NIVELMONTARDADOS_TODOS);
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoFinanceiroEnum.CONTA_RECEBER_PERMITIR_LIBERAR_SUSPENSAO_CONVENIO, usuarioVerif);
			getFacadeFactory().getContaReceberFacade().realizarLiberacaoSuspensaoConvenioPorOperacaoFuncionalidade(getContaReceberVO(), getPlanoDescontoContaReceberVO(), usuarioVerif, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getContaReceberVO().getUnidadeEnsinoFinanceira().getCodigo()), getUsuarioLogado());
			getFacadeFactory().getContaReceberFacade().realizarProcessamentoLiberacaoSuspensaoConvenioFinanciamentoProprio(getContaReceberVO(), getPlanoDescontoContaReceberVO(), getUsuarioLogado());
			setPlanoDescontoContaReceberVO(new PlanoDescontoContaReceberVO());
			setMensagemID(MSG_TELA.msg_dados_gravados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void carregarDadosLiberarFuncionalidadeIndiceReajustePorAtraso() {
		try {
			setUserNameLiberarFuncionalidade("");
			setSenhaLiberarFuncionalidade("");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void realizarVerificacaoUsuarioLiberacaoIndiceReajustePorAtraso() {
		try {
			UsuarioVO usuarioVerif = ControleAcesso.verificarLoginUsuario(this.getUserNameLiberarFuncionalidade(), this.getSenhaLiberarFuncionalidade(), true, Uteis.NIVELMONTARDADOS_TODOS);
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoFinanceiroEnum.PERMITE_LIBERAR_INDICE_REAJUSTE_ATRASO_CONTA_RECEBER, usuarioVerif);
			getFacadeFactory().getContaReceberFacade().atualizarLiberacaoIndiceReajustePorAtraso(getContaReceberVO(), usuarioVerif, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getContaReceberVO().getUnidadeEnsinoFinanceira().getCodigo()), getUsuarioLogado());
			setMensagemID(MSG_TELA.msg_dados_gravados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
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
	
	private Boolean permitirApresentarFinanciamentoEstudantil;
	
	public Boolean getPermitirApresentarFinanciamentoEstudantil() {
		if(permitirApresentarFinanciamentoEstudantil == null){
			permitirApresentarFinanciamentoEstudantil = false;
		}
		return permitirApresentarFinanciamentoEstudantil;
	}

	public void setPermitirApresentarFinanciamentoEstudantil(Boolean permitirApresentarFinanciamentoEstudantil) {
		this.permitirApresentarFinanciamentoEstudantil = permitirApresentarFinanciamentoEstudantil;
	}

	public boolean isPermitirApresentarFinanciamentoEstudantil() {
		try {
			if(!getPermitirApresentarFinanciamentoEstudantil()){
				ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoVisaoAlunoEnum.PERMITIR_VISUALIZAR_FINANCIAMENTO_ESTUDANTIL, getUsuarioLogado());
				permitirApresentarFinanciamentoEstudantil = true;
				inicializarMensagemVazia();
			}
		} catch (Exception e) {
			permitirApresentarFinanciamentoEstudantil= false;
		}
		return getPermitirApresentarFinanciamentoEstudantil();
	}
	

	/**
	 * INICIO DO MERGE EDIGAR - 15/05/18
	 */
	protected List<ContaReceberVO> listaContaReceberAdicionarPlanoDescontoEdicaoManual;
	protected Boolean apresentarRichPanelGravarContaReceber;
	// protected String

	public Boolean getApresentarRichPanelGravarContaReceber() {
		if (apresentarRichPanelGravarContaReceber == null) {
			apresentarRichPanelGravarContaReceber = Boolean.FALSE;
		}
		return apresentarRichPanelGravarContaReceber;
	}

	public void setApresentarRichPanelGravarContaReceber(Boolean apresentarRichPanelGravarContaReceber) {
		this.apresentarRichPanelGravarContaReceber = apresentarRichPanelGravarContaReceber;
	}

	public List<ContaReceberVO> getListaContaReceberAdicionarPlanoDescontoEdicaoManual() {
		if (listaContaReceberAdicionarPlanoDescontoEdicaoManual == null) {
			listaContaReceberAdicionarPlanoDescontoEdicaoManual = new ArrayList<ContaReceberVO>();
		}
		return listaContaReceberAdicionarPlanoDescontoEdicaoManual;
	}

	public void setListaContaReceberAdicionarPlanoDescontoEdicaoManual(List<ContaReceberVO> listaContaReceberAdicionarPlanoDescontoEdicaoManual) {
		this.listaContaReceberAdicionarPlanoDescontoEdicaoManual = listaContaReceberAdicionarPlanoDescontoEdicaoManual;
	}

	public Boolean getApresentarOpcaoReplicarDescontoOutrasContasReceber() {
		if ((this.getContaReceberVO().getContaEditadaManualmente()) &&
				((this.getContaReceberVO().getIsContaReceberReferenteMensalidade()) ||
						(this.getContaReceberVO().getIsContaReceberReferenteMatricula()) ||
						(this.getContaReceberVO().getContaReferenteParcelaConvenio()))) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public void limparPlanosDescontosMarcadosParaReplicacao() {
		this.setListaContaReceberAdicionarPlanoDescontoEdicaoManual(null);
		for (PlanoDescontoContaReceberVO plano : this.getContaReceberVO().getPlanoDescontoContaReceberVOs()) {
			plano.setReplicarPlanoDescontoOutrasContas(Boolean.FALSE);
		}
	}

	/**
	 * Metodo que replica os planos de descontos marcados para as outras contas a receber selecionadas
	 */
	public void replicarPlanoDescontoOutrasContasReceber() {
		try {
			getFacadeFactory().getContaReceberFacade().alterarContasReplicandoPlanoDescontoManual(this.getListaContaReceberAdicionarPlanoDescontoEdicaoManual(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getUsuarioLogado().getUnidadeEnsinoLogado().getCodigo()), getUsuarioLogado());
			limparPlanosDescontosMarcadosParaReplicacao();
			setMensagemID("msg_ContaReceber_planoDescontoReplicado");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void finalizarSemReplicarPlanosDesconto() {
		try {
			limparPlanosDescontosMarcadosParaReplicacao();
			setMensagemID("msg_ContaReceber_planoDescontoNaoReplicado");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/*
	 * Se a conta foi grava corretamente e esta sendo editada manualmente, iremos verificar se algum desconto lancado manualmente foi marcado para ser replicado para as outras conta a receber da mesma matricula. Neste caso, iremos listas todas essas contas e iremos confirmar com o usuario o lancamento do mesmo.
	 */
	public void verificarContaEditadaManualmenteEExistePlanoDescontoReplicarOutrasContas() {
		try {
			this.setListaContaReceberAdicionarPlanoDescontoEdicaoManual(null);
			this.setApresentarRichPanelGravarContaReceber(Boolean.FALSE);
			if (this.getContaReceberVO().getContaEditadaManualmente().booleanValue()) {
				boolean existePlanoMarcadoParaReplicar = false;
				for (PlanoDescontoContaReceberVO planoDescontoContaReceberVO : this.getContaReceberVO().getPlanoDescontoContaReceberVOs()) {
					if (planoDescontoContaReceberVO.getReplicarPlanoDescontoOutrasContas()) {
						existePlanoMarcadoParaReplicar = true;
					}
				}
				if (existePlanoMarcadoParaReplicar) {
					List<ContaReceberVO> listaResultadoConsulta = null;
					if (this.getContaReceberVO().getContaReferenteParcelaConvenio()) {
						FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO = new FiltroRelatorioFinanceiroVO(false);
						filtroRelatorioFinanceiroVO.setTipoOrigemBolsaCusteadaConvenio(true);
						listaResultadoConsulta = getFacadeFactory().getContaReceberFacade().consultaRapidaPorMatriculaESituacaoAReceberCompleto(getContaReceberVO().getMatriculaAluno().getMatricula(), filtroRelatorioFinanceiroVO, Boolean.FALSE, null, null, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getContaReceberVO().getUnidadeEnsino().getCodigo()), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
						for (ContaReceberVO contaReceber : listaResultadoConsulta) {
							if ((!contaReceber.getCodigo().equals(this.getContaReceberVO().getCodigo())) &&
									(contaReceber.getConvenio().getCodigo().equals(this.getContaReceberVO().getConvenio().getCodigo()))) {
								this.getListaContaReceberAdicionarPlanoDescontoEdicaoManual().add(contaReceber);
							}
						}
					}
					if ((this.getContaReceberVO().getContaReceberReferenteMatricula()) ||
							(this.getContaReceberVO().getIsContaReceberReferenteMensalidade())) {
						FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO = new FiltroRelatorioFinanceiroVO(false);
						filtroRelatorioFinanceiroVO.setTipoOrigemMensalidade(true);
						listaResultadoConsulta = getFacadeFactory().getContaReceberFacade().consultaRapidaPorMatriculaESituacaoAReceberCompleto(getContaReceberVO().getMatriculaAluno().getMatricula(), filtroRelatorioFinanceiroVO, Boolean.FALSE,  null, null, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getContaReceberVO().getUnidadeEnsino().getCodigo()), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
						for (ContaReceberVO contaReceber : listaResultadoConsulta) {
							if (!contaReceber.getCodigo().equals(this.getContaReceberVO().getCodigo())) {
								this.getListaContaReceberAdicionarPlanoDescontoEdicaoManual().add(contaReceber);
							}
						}
					}

					for (ContaReceberVO contaReceber : this.getListaContaReceberAdicionarPlanoDescontoEdicaoManual()) {
						contaReceber.setContaEditadaManualmente(Boolean.TRUE);
						for (PlanoDescontoContaReceberVO planoDescontoContaReceberVO : this.getContaReceberVO().getPlanoDescontoContaReceberVOs()) {
							if (planoDescontoContaReceberVO.getReplicarPlanoDescontoOutrasContas()) {
								PlanoDescontoContaReceberVO planoDescontoClonado = planoDescontoContaReceberVO.obterClone();
								planoDescontoClonado.setContaReceber(contaReceber.getCodigo());
								planoDescontoClonado.setReplicarPlanoDescontoOutrasContas(Boolean.TRUE);
								planoDescontoClonado.setUsuarioResponsavel(getUsuarioLogadoClone());
								contaReceber.adicionarObjPlanoDescontoContaReceberVOs(planoDescontoClonado);
							}
						}
						contaReceber.atualizarSituacaoContaReceberDeAcordoComDescontos(this.getConfiguracaoFinanceiroVO(), null,getUsuarioLogado());
					}

					this.setApresentarRichPanelGravarContaReceber(Boolean.TRUE);
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<IndiceReajustePeriodoMatriculaPeriodoVencimentoVO> getListaIndiceReajustePeriodoMatriculaPeriodoVencimentoCancelamentoVOs() {
		if (listaIndiceReajustePeriodoMatriculaPeriodoVencimentoCancelamentoVOs == null) {
			listaIndiceReajustePeriodoMatriculaPeriodoVencimentoCancelamentoVOs = new ArrayList<IndiceReajustePeriodoMatriculaPeriodoVencimentoVO>(0);
		}
		return listaIndiceReajustePeriodoMatriculaPeriodoVencimentoCancelamentoVOs;
	}

	public void setListaIndiceReajustePeriodoMatriculaPeriodoVencimentoCancelamentoVOs(List<IndiceReajustePeriodoMatriculaPeriodoVencimentoVO> listaIndiceReajustePeriodoMatriculaPeriodoVencimentoCancelamentoVOs) {
		this.listaIndiceReajustePeriodoMatriculaPeriodoVencimentoCancelamentoVOs = listaIndiceReajustePeriodoMatriculaPeriodoVencimentoCancelamentoVOs;
	}

	public boolean getApresentarModalErroBoleto() {
		if (request().getSession().getAttribute("ERRO") != null && request().getSession().getAttribute("ERRO") instanceof Exception) {
			setMensagemDetalhada("msg_erro", ((Exception) request().getSession().getAttribute("ERRO")).getMessage());
			request().getSession().removeAttribute("ERRO");
			return true;
		}
		return false;
	}

	public void removerContaReceberListaReplicarDesconto() {
		try {
			ContaReceberVO contaReceberRemover = (ContaReceberVO) context().getExternalContext().getRequestMap().get("contaReceberReplicar");
			int index = this.getListaContaReceberAdicionarPlanoDescontoEdicaoManual().size() - 1;
			while (index >= 0) {
				ContaReceberVO contaLista = this.getListaContaReceberAdicionarPlanoDescontoEdicaoManual().get(index);
				if (contaLista.getCodigo().equals(contaReceberRemover.getCodigo())) {
					this.getListaContaReceberAdicionarPlanoDescontoEdicaoManual().remove(index);
					break;
				}
				index--;
			}
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void realizarSelecaoCheckboxMarcarDesmarcarTodosTipoOrigem() {
		if (getMarcarTodosTipoOrigem()) {
			filtroRelatorioFinanceiroVO.realizarMarcarTodasOrigens();
		} else {
			filtroRelatorioFinanceiroVO.realizarDesmarcarTodasOrigens();
		}
	}

	public String getTipoDescontoPlanoDesconto() {
		if (this.getContaReceberVO().getContaReceberReferenteMatricula()) {
			return getPlanoDescontoContaReceberVO().getPlanoDescontoVO().getTipoDescMatricula();
		} else {
			return getPlanoDescontoContaReceberVO().getPlanoDescontoVO().getTipoDescParcela();
		}
	}

	public Boolean getApresentarBotaoLiberarBloqueio() {
		return this.getContaReceberVO().getApresentarBotaoLiberarBloqueioFechamentoMes();
	}

	public void liberarRegistroCompetenciaFechada() {
		try {
			this.getContaReceberVO().setBloqueioPorFechamentoMesLiberado(Boolean.TRUE);
			FechamentoMesHistoricoModificacaoVO historico = getFacadeFactory().getFechamentoMesHistoricoModificacaoFacade().gerarNovoHistoricoModificacao(this.getContaReceberVO().getFechamentoMesVOBloqueio(), getUsuarioLogado(), TipoOrigemHistoricoBloqueioEnum.ARECEBER, this.getContaReceberVO().getDescricaoBloqueio(), this.getContaReceberVO().toString());
			getFacadeFactory().getFechamentoMesHistoricoModificacaoFacade().incluir(historico, getUsuarioLogado());
			setMensagemID("msg_registro_liberado_mes");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			this.getContaReceberVO().setBloqueioPorFechamentoMesLiberado(Boolean.FALSE);
		}
	}

	public void verificarPermissaoLiberarBloqueioCompetencia() {
		try {
			UsuarioVO usuarioVerif = ControleAcesso.verificarLoginUsuario(this.getUsernameLiberacaoBloqueioPorFechamentoMes(), this.getSenhaLiberacaoBloqueioPorFechamentoMes(), true, Uteis.NIVELMONTARDADOS_TODOS);
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("FuncionarioMes_liberarBloqueioIncluirAlterarContaReceber", usuarioVerif);
			liberarRegistroCompetenciaFechada();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public String getMotivoNeg() {
		if (motivoNeg == null) {
			motivoNeg = "";
		}
		return motivoNeg;
	}

	public void setMotivoNeg(String motivoNeg) {
		this.motivoNeg = motivoNeg;
	}

	public void atualizarValorCheioTipoDesconto() {
		setDescontoMaiorValorContaReceber(null);
		OrdemDescontoVO desconto = (OrdemDescontoVO) context().getExternalContext().getRequestMap().get("ordemItens");
		getContaReceberVO().atualizarSituacaoValorCheioOrdemDesconto(desconto);
		atualizarValoresContaReceber();
		if (getDescontoMaiorValorContaReceber()) {
			desconto.setValorCheio(false);
			getContaReceberVO().atualizarSituacaoValorCheioOrdemDesconto(desconto);	
			}
	}

	public String getIsApresentarTextoCheckBoxMarcarDesmarcarTodosTipoOrigem() {
		if (getMarcarTodosTipoOrigem()) {
			return UteisJSF.internacionalizar("prt_Inadimplencia_desmarcarTodos");
		}
		return UteisJSF.internacionalizar("prt_Inadimplencia_marcarTodos");
	}

	public void limparSituacao() {
		setMarcarTodos(true);
		setMarcarTodosTipoOrigem(true);
		montarSituacoesContaReceber();
		realizarSelecaoCheckboxMarcarDesmarcarTodosTipoOrigem();
	}	
	
	public boolean getExibirBotaoDesfazerCancelamentoContaReceber() {
		if (getContaReceberVO().getTipoOrigem().equals(TipoOrigemContaReceber.REQUERIMENTO.getValor())) {
			return Boolean.FALSE;
		} else {
			return getContaReceberVO().getPermiteReativarContaReceberCancelada()
					&& !getContaReceberVO().isAgrupado()
					&& getContaReceberVO().getProcessamentoIntegracaoFinanceiraDetalheVO().getCodigo().equals(0);
		}
	}
	
	public boolean getReadOnlyDataVencimentoContaReceber() {
		return getContaReceberVO().isNovoObj() ? false : getIsPermissaoFinanceiroManual();
	}

	public TipoCartaoOperadoraCartaoEnum getTipoCartao() {
		if (tipoCartao == null) {
			if (Uteis.isAtributoPreenchido(getConfiguracaoRecebimentoCartaoOnlineVO()) &&
					PermitirCartaoEnum.DEBITO.equals(getConfiguracaoRecebimentoCartaoOnlineVO().getPermitirCartao())) {
				tipoCartao = TipoCartaoOperadoraCartaoEnum.CARTAO_DEBITO;
			} else {
				tipoCartao = TipoCartaoOperadoraCartaoEnum.CARTAO_CREDITO;
			}
		}
		return tipoCartao;
	}

	public void setTipoCartao(TipoCartaoOperadoraCartaoEnum tipoCartao) {
		this.tipoCartao = tipoCartao;
	}
	
	public void selecionarTipoCartao() {
		try {
			getConfiguracaoFinanceiroCartaoVOs().clear();
			getConfiguracaoFinanceiroCartaoVOs().addAll(getFacadeFactory().getConfiguracaoFinanceiroCartaoFacade().consultarConfiguracaoFinanceiroCartaoPorCodigoConfiguracaoFinanceiro(getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(contaReceberVO.getUnidadeEnsinoFinanceira().getCodigo()).getCodigo(), getNegociacaoRecebimentoVO().getValorTotal(), "usarminhascontasvisaoaluno", getTipoCartao().name(), getUsuarioLogado()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public Map<Integer, ConfiguracaoRecebimentoCartaoOnlineVO> getConfiguracaoRecebimentoCartaoOnlineVOs() {
		if(configuracaoRecebimentoCartaoOnlineVOs == null) {
			configuracaoRecebimentoCartaoOnlineVOs = new HashMap<Integer, ConfiguracaoRecebimentoCartaoOnlineVO>(0);
		}
		return configuracaoRecebimentoCartaoOnlineVOs;
	}

	public void setConfiguracaoRecebimentoCartaoOnlineVOs(
			Map<Integer, ConfiguracaoRecebimentoCartaoOnlineVO> configuracaoRecebimentoCartaoOnlineVOs) {
		this.configuracaoRecebimentoCartaoOnlineVOs = configuracaoRecebimentoCartaoOnlineVOs;
	}

	public ConfiguracaoRecebimentoCartaoOnlineVO getConfiguracaoRecebimentoCartaoOnlineVO() {
		if(configuracaoRecebimentoCartaoOnlineVO == null) {
			configuracaoRecebimentoCartaoOnlineVO =  new ConfiguracaoRecebimentoCartaoOnlineVO();
		}
		return configuracaoRecebimentoCartaoOnlineVO;
	}

	public void setConfiguracaoRecebimentoCartaoOnlineVO(ConfiguracaoRecebimentoCartaoOnlineVO configuracaoRecebimentoCartaoOnlineVO) {
		this.configuracaoRecebimentoCartaoOnlineVO = configuracaoRecebimentoCartaoOnlineVO;
	}

	private void vincularMatriculaVOContaReceber(MatriculaVO matriculaVO) throws Exception {
		verificarPermicaoCriarContaAluno();
		getContaReceberVO().setMatriculaAluno(matriculaVO);
		getContaReceberVO().getPessoa().setCodigo(matriculaVO.getAluno().getCodigo());
		setUltimaContaReceberGeradaManualmente(getFacadeFactory().getContaReceberFacade().consultarNomeUltimaParcela(matriculaVO.getAluno().getCodigo()));
		getFacadeFactory().getContaReceberFacade().realizarVinculoContaReceberComResponsavelFinanceiro(getContaReceberVO(), getUsuarioLogado());
		getContaReceberVO().setUnidadeEnsino(matriculaVO.getUnidadeEnsino());
		getContaReceberVO().setTurma(getFacadeFactory().getTurmaFacade().consultaRapidaPorMatriculaUltimaMatriculaPeriodo(matriculaVO.getMatricula(), getUsuarioLogado()));
		realizarVinculoContaReceberComUltimaMatriculaPeriodo();
	}
	
	private void realizarVinculoContaReceberComUltimaMatriculaPeriodo() throws Exception {
		if (Uteis.isAtributoPreenchido(getContaReceberVO().getMatriculaAluno().getMatricula())) {
			getContaReceberVO().setMatriculaPeriodo(getFacadeFactory().getMatriculaPeriodoFacade().consultaCodigoUltimaMatriculaPeriodoPorMatricula(getContaReceberVO().getMatriculaAluno().getMatricula(), false, getUsuarioLogado()));
		}
	}
	
	public List<SelectItem> getComboBoxConsultaComboFormaPadraoDataBaseCartaoRecorrente() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem(FormaPadraoDataBaseCartaoRecorrenteEnum.DIA_FIXO, FormaPadraoDataBaseCartaoRecorrenteEnum.DIA_FIXO.getValorApresentar()));
		itens.add(new SelectItem(FormaPadraoDataBaseCartaoRecorrenteEnum.VENCIMENTO_PRIMEIRA_FAIXA_DESCONTO, FormaPadraoDataBaseCartaoRecorrenteEnum.VENCIMENTO_PRIMEIRA_FAIXA_DESCONTO.getValorApresentar()));
		return itens;
    }
	
	public void validarDiaPagamento() {
		
	}

	public Boolean getMensagemConfirmacaoRecorrenciaApresentada() {
		if (mensagemConfirmacaoRecorrenciaApresentada == null) {
			mensagemConfirmacaoRecorrenciaApresentada = false;
		}
		return mensagemConfirmacaoRecorrenciaApresentada;
	}

	public void setMensagemConfirmacaoRecorrenciaApresentada(Boolean mensagemConfirmacaoRecorrenciaApresentada) {
		this.mensagemConfirmacaoRecorrenciaApresentada = mensagemConfirmacaoRecorrenciaApresentada;
	}
	
	public void finalizarMensagemConfirmacaoRecorrenciaApresentada() {
		setMensagemConfirmacaoRecorrenciaApresentada(true);
		realizarRecebimentoCartaoCredito();
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
	
	public void consultarCartaoCreditoDebitoRecorrenciaPessoa() {
		try {
			setListaCartaoCreditoDebitoRecorrenciaPessoaVOs(getFacadeFactory().getCartaoCreditoDebitoRecorrenciaPessoaFacade().consultarContasRecorrenciaPorMatricula(getVisaoAlunoControle().getMatricula().getMatricula(), true, getUsuarioLogado()));
			montarListaSelectItemOperadoraCartaoAutorizadoRecebimentoOnline();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void removerCartaoCreditoDebitoRecorrenciaPessoa() {
		CartaoCreditoDebitoRecorrenciaPessoaVO obj = (CartaoCreditoDebitoRecorrenciaPessoaVO) context().getExternalContext().getRequestMap().get("cartaoCreditoDebitoPessoaItens");
		obj.setSituacao(SituacaoEnum.INATIVO);
		try {
			if (Uteis.isAtributoPreenchido(obj.getCodigo())) {
				getFacadeFactory().getCartaoCreditoDebitoRecorrenciaPessoaFacade().alterarSituacaoPorCodigo(obj.getCodigo(), obj.getSituacao(), getUsuarioLogado());
			}
			getFacadeFactory().getCartaoCreditoDebitoRecorrenciaPessoaFacade().removerCartaoCreditoDebitoRecorrenciaPessoa(obj, getListaCartaoCreditoDebitoRecorrenciaPessoaVOs());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void selecionarCartaoCreditoDebitoRecorrenciaPessoa() {
		CartaoCreditoDebitoRecorrenciaPessoaVO obj = (CartaoCreditoDebitoRecorrenciaPessoaVO) context().getExternalContext().getRequestMap().get("cartaoCreditoDebitoPessoaItens");
		try {
			setCartaoCreditoDebitoRecorrenciaPessoaVO((CartaoCreditoDebitoRecorrenciaPessoaVO) obj.clone());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void adicionarCartaoCreditoDebitoRecorrenciaPessoa() {
		try {
			getFacadeFactory().getCartaoCreditoDebitoRecorrenciaPessoaFacade().inicializarDadosAlunoCartaoCreditoDebito(getCartaoCreditoDebitoRecorrenciaPessoaVO(), getVisaoAlunoControle().getMatricula(), getUsuarioLogado());
			getFacadeFactory().getCartaoCreditoDebitoRecorrenciaPessoaFacade().adicionarCartaoCreditoDebitoRecorrenciaPessoa(getCartaoCreditoDebitoRecorrenciaPessoaVO(), getListaCartaoCreditoDebitoRecorrenciaPessoaVOs(), getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		setCartaoCreditoDebitoRecorrenciaPessoaVO(new CartaoCreditoDebitoRecorrenciaPessoaVO());
	}
	
	private List<SelectItem> listaSelectItemOperadoraCartaoVOs;
	
	public void montarListaSelectItemOperadoraCartaoAutorizadoRecebimentoOnline() {
		try {
			List<ConfiguracaoFinanceiroCartaoVO> listaConfiguracaoCartaoFinanceiroCartaoVOs = getFacadeFactory().getConfiguracaoFinanceiroCartaoFacade().consultarConfiguracaoFinanceiroCartaoPorCodigoConfiguracaoFinanceiro(getConfiguracaoFinanceiroPadraoSistema().getCodigo(), 0.0, "usarminhascontasvisaoaluno", TipoCartaoOperadoraCartaoEnum.CARTAO_CREDITO.name(), getUsuarioLogado());
			List<SelectItem> itens = new ArrayList<SelectItem>(0);
			for (ConfiguracaoFinanceiroCartaoVO configuracaoFinanceiroCartaoVO : listaConfiguracaoCartaoFinanceiroCartaoVOs) {
				itens.add(new SelectItem(configuracaoFinanceiroCartaoVO.getOperadoraCartaoVO().getCodigo(), configuracaoFinanceiroCartaoVO.getOperadoraCartaoVO().getNome() +" - "+ configuracaoFinanceiroCartaoVO.getOperadoraCartaoVO().getOperadoraCartaoCreditoApresentar()));
			}
			setListaSelectItemOperadoraCartaoVOs(itens);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public CartaoCreditoDebitoRecorrenciaPessoaVO getCartaoCreditoDebitoRecorrenciaPessoaVO() {
		if (cartaoCreditoDebitoRecorrenciaPessoaVO == null) {
			cartaoCreditoDebitoRecorrenciaPessoaVO = new CartaoCreditoDebitoRecorrenciaPessoaVO();
		}
		return cartaoCreditoDebitoRecorrenciaPessoaVO;
	}

	public void setCartaoCreditoDebitoRecorrenciaPessoaVO(CartaoCreditoDebitoRecorrenciaPessoaVO cartaoCreditoDebitoRecorrenciaPessoaVO) {
		this.cartaoCreditoDebitoRecorrenciaPessoaVO = cartaoCreditoDebitoRecorrenciaPessoaVO;
	}
	
	public List<SelectItem> listaSelectItemMesValidade;
	public List<SelectItem> listaSelectItemAnoValidade;
	
	public List<SelectItem> getListaSelectItemMesValidade() {
		if(listaSelectItemMesValidade == null) {
			listaSelectItemMesValidade = new ArrayList<SelectItem>();
			for (Integer i = 1; i < 13; i++) {
				listaSelectItemMesValidade.add(new SelectItem(i.toString(), i.toString()));
			}
		}
		return listaSelectItemMesValidade;
	}

	public void setListaSelectItemMesValidade(List<SelectItem> listaSelectItemMesValidade) {
		this.listaSelectItemMesValidade = listaSelectItemMesValidade;
	}

	public List<SelectItem> getListaSelectItemAnoValidade() {
		if(listaSelectItemAnoValidade == null) {
			listaSelectItemAnoValidade = new ArrayList<SelectItem>();
			Integer anoAtual = Integer.parseInt(Uteis.getAnoDataAtual());
			for (int i = 0; i < 11; i++) {
				listaSelectItemAnoValidade.add(new SelectItem(anoAtual.toString(), anoAtual.toString()));
				anoAtual++;
			}
		}
		return listaSelectItemAnoValidade;
	}

	public void setListaSelectItemAnoValidade(List<SelectItem> listaSelectItemAnoValidade) {
		this.listaSelectItemAnoValidade = listaSelectItemAnoValidade;
	}

	public List<SelectItem> getListaSelectItemOperadoraCartaoVOs() {
		if (listaSelectItemOperadoraCartaoVOs == null) {
			listaSelectItemOperadoraCartaoVOs = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemOperadoraCartaoVOs;
	}

	public void setListaSelectItemOperadoraCartaoVOs(List<SelectItem> listaSelectItemOperadoraCartaoVOs) {
		this.listaSelectItemOperadoraCartaoVOs = listaSelectItemOperadoraCartaoVOs;
	}

	public void inicializarDadosCartaoCreditoRecorrenciaCadastrada() {
//		getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoFacade().inicializarDadosCartaoCreditoRecorrenciaCadastrada(obj, getVisaoAlunoControle().getMatricula().getMatricula(), getUsuarioLogado());
		
	}

	public void setListaSelectItemCartaoCreditoDebitoRecorrenciaPessoaVOs(List<SelectItem> listaSelectItemCartaoCreditoDebitoRecorrenciaPessoaVOs) {
		this.listaSelectItemCartaoCreditoDebitoRecorrenciaPessoaVOs = listaSelectItemCartaoCreditoDebitoRecorrenciaPessoaVOs;
	}
	
	public void inicializarApresentacaoVersoCartao() {
		FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO = (FormaPagamentoNegociacaoRecebimentoVO) context().getExternalContext().getRequestMap().get("formaPagamentoItens");
		formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setApresentarVersoCartao(true);
	}
	
	public void cancelarApresentacaoVersoCartao() {
		FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO = (FormaPagamentoNegociacaoRecebimentoVO) context().getExternalContext().getRequestMap().get("formaPagamentoItens");
		formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setApresentarVersoCartao(false);
	}
	
	public void consultarTransacaoCartaoCreditoPorMatricula() {
		try {
			setListaTransacaoCartaoOnlineVOs(getFacadeFactory().getTransacaoCartaoOnlineInterfaceFacade().consultarPorMatricula(getVisaoAlunoControle().getMatricula().getMatricula(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
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
	
	public Boolean getDescontoMaiorValorContaReceber() {
		if (descontoMaiorValorContaReceber == null) {
			descontoMaiorValorContaReceber = false;
		}
		return descontoMaiorValorContaReceber;
	}

	public void setDescontoMaiorValorContaReceber(Boolean descontoMaiorValorContaReceber) {
		this.descontoMaiorValorContaReceber = descontoMaiorValorContaReceber;
	}

	public Double getTotalContaPagarVisaoAlunoValorBase() {
		if (totalContaPagarVisaoAlunoValorBase == null) {
			totalContaPagarVisaoAlunoValorBase = 0.0;
		}
		return totalContaPagarVisaoAlunoValorBase;
	}

	public void setTotalContaPagarVisaoAlunoValorBase(Double totalContaPagarVisaoAlunoValorBase) {
		this.totalContaPagarVisaoAlunoValorBase = totalContaPagarVisaoAlunoValorBase;
	}

	public Double getTotalContaPagarVisaoAlunoValorFinal() {
		if (totalContaPagarVisaoAlunoValorFinal == null) {
			totalContaPagarVisaoAlunoValorFinal = 0.0;
		}
		return totalContaPagarVisaoAlunoValorFinal;
	}

	public void setTotalContaPagarVisaoAlunoValorFinal(Double totalContaPagarVisaoAlunoValorFinal) {
		this.totalContaPagarVisaoAlunoValorFinal = totalContaPagarVisaoAlunoValorFinal;
	}

	public Double getTotalContaPagarVisaoAlunoCancelado() {
		if (totalContaPagarVisaoAlunoCancelado == null) {
			totalContaPagarVisaoAlunoCancelado = 0.0;
		}
		return totalContaPagarVisaoAlunoCancelado;
	}

	public void setTotalContaPagarVisaoAlunoCancelado(Double totalContaPagarVisaoAlunoCancelado) {
		this.totalContaPagarVisaoAlunoCancelado = totalContaPagarVisaoAlunoCancelado;
	}

	public Double getTotalContaPagarVisaoAlunoNegociado() {
		if (totalContaPagarVisaoAlunoNegociado == null) {
			totalContaPagarVisaoAlunoNegociado = 0.0;
		}
		return totalContaPagarVisaoAlunoNegociado;
	}

	public void setTotalContaPagarVisaoAlunoNegociado(Double totalContaPagarVisaoAlunoNegociado) {
		this.totalContaPagarVisaoAlunoNegociado = totalContaPagarVisaoAlunoNegociado;
	}

	public Double getTotalContaPagarVisaoAlunoPago() {
		if (totalContaPagarVisaoAlunoPago == null) {
			totalContaPagarVisaoAlunoPago = 0.0;
		}
		return totalContaPagarVisaoAlunoPago;
	}

	public void setTotalContaPagarVisaoAlunoPago(Double totalContaPagarVisaoAlunoPago) {
		this.totalContaPagarVisaoAlunoPago = totalContaPagarVisaoAlunoPago;
	}
	
	private void realizarMontagemTotalizadoresContaReceber() {
		setTotalContaPagarVisaoAlunoValorBase(getControleConsultaOtimizado().getTotalizadores().get("valortotal_base_contareceber"));
		setTotalContaPagarVisaoAlunoValorFinal(getControleConsultaOtimizado().getTotalizadores().get("valortotal_final_contareceber") - getControleConsultaOtimizado().getTotalizadores().get("valortotal_pago_contareceber"));
		setTotalContaPagarVisaoAlunoCancelado(getControleConsultaOtimizado().getTotalizadores().get("valortotal_cancelado_contareceber"));
		setTotalContaPagarVisaoAlunoNegociado(getControleConsultaOtimizado().getTotalizadores().get("valortotal_negociado_contareceber"));
		setTotalContaPagarVisaoAlunoPago(getControleConsultaOtimizado().getTotalizadores().get("valortotal_pago_contareceber"));
	}
	

	public void verificarPermissaoVisualizarAbaTransacaoCartaoVisaoAluno() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("PermiteVisualizarAbaTransacaoCartaoVisaoAluno", getUsuarioLogado());
			setPermiteVisualizarAbaTransacaoCartaoVisaoAluno(true);
		} catch (Exception e) {
			setPermiteVisualizarAbaTransacaoCartaoVisaoAluno(false);
		}
	}
	
	public void verificarPermissaoVisualizarAbaRecorrenciaVisaoAluno() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("PermiteVisualizarAbaRecorrenciaVisaoAluno", getUsuarioLogado());
			setPermiteVisualizarAbaRecorrenciaVisaoAluno(true);
		} catch (Exception e) {
			setPermiteVisualizarAbaRecorrenciaVisaoAluno(false);
		}
	}
	
	public Boolean getPermiteVisualizarAbaTransacaoCartaoVisaoAluno() {
		if (permiteVisualizarAbaTransacaoCartaoVisaoAluno == null) {
			permiteVisualizarAbaTransacaoCartaoVisaoAluno = false;
		}
		return permiteVisualizarAbaTransacaoCartaoVisaoAluno;
	}

	public void setPermiteVisualizarAbaTransacaoCartaoVisaoAluno(Boolean permiteVisualizarAbaTransacaoCartaoVisaoAluno) {
		this.permiteVisualizarAbaTransacaoCartaoVisaoAluno = permiteVisualizarAbaTransacaoCartaoVisaoAluno;
	}
	
	public Boolean getPermiteVisualizarAbaRecorrenciaVisaoAluno() {
		if (permiteVisualizarAbaRecorrenciaVisaoAluno == null) {
			permiteVisualizarAbaRecorrenciaVisaoAluno = false;
		}
		return permiteVisualizarAbaRecorrenciaVisaoAluno;
	}

	public void setPermiteVisualizarAbaRecorrenciaVisaoAluno(Boolean permiteVisualizarAbaRecorrenciaVisaoAluno) {
		this.permiteVisualizarAbaRecorrenciaVisaoAluno = permiteVisualizarAbaRecorrenciaVisaoAluno;
	}
	
}