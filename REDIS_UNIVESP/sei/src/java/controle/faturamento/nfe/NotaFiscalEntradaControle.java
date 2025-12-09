package controle.faturamento.nfe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoFinanceiroEnum;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoNotaFiscalEnum;
import negocio.comuns.compras.CategoriaProdutoVO;
import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.compras.ProdutoServicoVO;
import negocio.comuns.compras.RecebimentoCompraItemVO;
import negocio.comuns.compras.enumeradores.TipoProdutoServicoEnum;
import negocio.comuns.contabil.FechamentoMesHistoricoModificacaoVO;
import negocio.comuns.contabil.LancamentoContabilCentroNegocioVO;
import negocio.comuns.contabil.LancamentoContabilVO;
import negocio.comuns.contabil.PlanoContaVO;
import negocio.comuns.contabil.enumeradores.TipoOrigemHistoricoBloqueioEnum;
import negocio.comuns.contabil.enumeradores.TipoOrigemLancamentoContabilEnum;
import negocio.comuns.contabil.enumeradores.TipoPlanoContaEnum;
import negocio.comuns.ead.enumeradores.SituacaoEnum;
import negocio.comuns.faturamento.nfe.ImpostoVO;
import negocio.comuns.faturamento.nfe.NaturezaOperacaoVO;
import negocio.comuns.faturamento.nfe.NotaFiscalEntradaImpostoVO;
import negocio.comuns.faturamento.nfe.NotaFiscalEntradaItemVO;
import negocio.comuns.faturamento.nfe.NotaFiscalEntradaRecebimentoCompraVO;
import negocio.comuns.faturamento.nfe.NotaFiscalEntradaVO;
import negocio.comuns.faturamento.nfe.NotaFiscalEntradaVO.tipoLancamentoContabilNotaFiscalEntrada;
import negocio.comuns.faturamento.nfe.enumeradores.TipoNaturezaOperacaoEnum;
import negocio.comuns.faturamento.nfe.enumeradores.TipoNotaFiscalEntradaEnum;
import negocio.comuns.financeiro.BancoVO;
import negocio.comuns.financeiro.CategoriaDespesaVO;
import negocio.comuns.financeiro.CentroResultadoOrigemVO;
import negocio.comuns.financeiro.CentroResultadoVO;
import negocio.comuns.financeiro.ContaPagarAdiantamentoVO;
import negocio.comuns.financeiro.ContaPagarVO;
import negocio.comuns.financeiro.enumerador.BancoEnum;
import negocio.comuns.financeiro.enumerador.TipoIdentificacaoChavePixEnum;
import negocio.comuns.financeiro.enumerador.TipoLancamentoContaPagarEnum;
import negocio.comuns.financeiro.enumerador.TipoNivelCentroResultadoEnum;
import negocio.comuns.financeiro.enumerador.TipoServicoContaPagarEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.boleto.GeradorDeLinhaDigitavelOuCodigoBarra;
import negocio.comuns.utilitarias.dominios.Bancos;
import negocio.comuns.utilitarias.dominios.OrigemContaPagar;
import negocio.facade.jdbc.arquitetura.ControleAcesso;

/**
 * 
 * @author PedroOtimize
 *
 */
@Controller("NotaFiscalEntradaControle")
@Scope("viewScope")
@Lazy
public class NotaFiscalEntradaControle extends SuperControle implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8693245160172837943L;
	private static final String TELA_FORM = "notaFiscalEntradaForm.xhtml";
	private static final String TELA_CONS = "notaFiscalEntradaCons.xhtml";
	private static final String CONTEXT_PARA_EDICAO = "notaFiscalEntradaItens";

	private NotaFiscalEntradaVO notaFiscalEntradaVO;
	private NotaFiscalEntradaImpostoVO notaFiscalEntradaImpostoVO;
	private NotaFiscalEntradaItemVO notaFiscalEntradaItemVO;
	private NotaFiscalEntradaRecebimentoCompraVO notaFiscalEntradaRecebimentoCompraVO;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private List<SelectItem> listaSelectItemUnidadeEnsinoCategoriaDespesa;
	private List<SelectItem> listaSelectItemImposto;
	private List<SelectItem> listaSelectItemTipoNivelCentroResultadoEnum;

	private List<FornecedorVO> listaConsultaFornecedor;
	private String valorConsultaFornecedor;
	private String campoConsultaFornecedor;

	private List<NaturezaOperacaoVO> listaConsultaNaturezaOperacao;
	private String valorConsultaNaturezaOperacao;
	private String campoConsultaNaturezaOperacao;

	private List<ProdutoServicoVO> listaConsultaProduto;
	private String campoConsultaProduto;
	private String valorConsultaProduto;

	private List<CategoriaDespesaVO> listaConsultaCentroDespesa;
	private String valorConsultaCentroDespesa;
	private String campoConsultaCentroDespesa;

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

	private String campoConsultaDepartamento;
	private String valorConsultaDepartamento;
	private List<DepartamentoVO> listaConsultaDepartamento;

	private DataModelo centroResultadoDataModelo;
	private boolean centroResultadoAdministrativo = false;
	private CentroResultadoOrigemVO centroResultadoOrigemVO;

	private TipoNaturezaOperacaoEnum tipoNaturezaOperacaoEnumCampoConsulta;

	private LancamentoContabilVO lancamentoContabilVO;
	private LancamentoContabilCentroNegocioVO lancamentoContabilCentroNegocioVO;
	private ImpostoVO impostoFiltroLancamentoContabil;
	private CategoriaProdutoVO categoriaProdutoFiltroLancamentoContabil;
	private List<SelectItem> listaSelectItemImpostoFiltroLancamentoContabil;
	private List<SelectItem> listaSelectItemCategoriaProdutoFiltroLancamentoContabil;
	private String modalAptoParaSerFechado;
	private String valorConsultaPlanoConta;
	private String campoConsultaPlanoConta;
	private List<PlanoContaVO> listaConsultaPlanoConta;

	private ContaPagarVO contaPagarVO;
	private String modalAptoAdiantamentosDisponiveisDistribuicao;
	private List<SelectItem> comboTipoLancamentoContaPagar;
	private List<SelectItem> comboTipoServicoContaPagar;
	private List<SelectItem> comboFormaPagamento;
	private List<SelectItem> listaSelectItemBancoFavorecido;
	private List<BancoVO> listaSelectItemBanco;
	private String cnpjOuCpf;
	private Date dataEntradaInicio;
	private Date dataEntradaFim;
	private Boolean consultaDataScroller;
	private Double totalNotaFiscalEntrada;
	private List<ProdutoServicoVO> listaProdutoServico;
	private String valorConsultaProdutoServico;
	private List<SelectItem> comboTipoIdentificacaoChavePixEnum;
	
	
	

	public NotaFiscalEntradaControle() {
		setControleConsultaOtimizado(new DataModelo());
		getControleConsultaOtimizado()
				.setCampoConsulta(NotaFiscalEntradaVO.enumCampoConsultaNotaFiscalEntrada.NUMERO.name());
		getControleConsultaOtimizado().setDataIni(null);
		getControleConsultaOtimizado().setDataFim(null);
		montarListaSelectItemUnidadeEnsino();
		setMensagemID(MSG_TELA.msg_entre_prmconsulta.name());
	}

	@PostConstruct
	public String realizarCarregamentoTela() {
		try {
			NotaFiscalEntradaVO obj = (NotaFiscalEntradaVO) context().getExternalContext().getSessionMap()
					.get("notaFiscalEntrada");
			if (obj != null && !obj.getCodigo().equals(0)) {
				setNotaFiscalEntradaVO(getFacadeFactory().getNotaFiscalEntradaFacade().consultarPorChavePrimaria(
						obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
				setContaPagarVO(new ContaPagarVO());
				getContaPagarVO().setTipoOrigem(OrigemContaPagar.NOTA_FISCAL_ENTRADA.getValor());
				inicializarListasSelectItemTodosComboBox();
				verificarPermissaoLancamentoContabil();
				setCnpjOuCpf(getNotaFiscalEntradaVO().getFornecedorVO().getCnpjOuCfp_Apresentar());
				setMensagemID("msg_dados_editar");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			context().getExternalContext().getSessionMap().remove("notaFiscalEntrada");
		}
		return "";
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe
	 * <code>PlanoConta</code> para edição pelo usuário da aplicação.
	 */
	public String novo() {
		removerObjetoMemoria(this);
		setNotaFiscalEntradaVO(new NotaFiscalEntradaVO());
		getNotaFiscalEntradaVO().setTipoNotaFiscalEntradaEnum(TipoNotaFiscalEntradaEnum.PRODUTO);
		getNotaFiscalEntradaVO().setDataEmissao(new Date());
		getNotaFiscalEntradaVO().setDataEntrada(new Date());
		setContaPagarVO(new ContaPagarVO());
		getContaPagarVO().setTipoOrigem(OrigemContaPagar.NOTA_FISCAL_ENTRADA.getValor());
		inicializarListasSelectItemTodosComboBox();
		setMensagemID(MSG_TELA.msg_entre_dados.name());
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe
	 * <code>PlanoConta</code> para alteração. O objeto desta classe é
	 * disponibilizado na session da página (request) para que o JSP correspondente
	 * possa disponibilizá-lo para edição.
	 */
	public String editar() {
		try {
			NotaFiscalEntradaVO obj = (NotaFiscalEntradaVO) context().getExternalContext().getRequestMap()
					.get(CONTEXT_PARA_EDICAO);
			setNotaFiscalEntradaVO(getFacadeFactory().getNotaFiscalEntradaFacade().consultarPorChavePrimaria(
					obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			setContaPagarVO(new ContaPagarVO());
			getContaPagarVO().setTipoOrigem(OrigemContaPagar.NOTA_FISCAL_ENTRADA.getValor());
			inicializarListasSelectItemTodosComboBox();
			verificarPermissaoLancamentoContabil();
			setCnpjOuCpf(getNotaFiscalEntradaVO().getFornecedorVO().getCnpjOuCfp_Apresentar());
			setMensagemID(MSG_TELA.msg_dados_editar.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			return "";
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto da
	 * classe <code>PlanoConta</code>. Caso o objeto seja novo (ainda não gravado no
	 * BD) é acionado a operação <code>incluir()</code>. Caso contrário é acionado o
	 * <code>alterar()</code>. Se houver alguma inconsistência o objeto não é
	 * gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de
	 * erro.
	 */
	public void persistir() {
		try {
			getFacadeFactory().getNotaFiscalEntradaFacade().persistir(getNotaFiscalEntradaVO(), true,
					getUsuarioLogado());
			verificarPermissaoLancamentoContabil();
			preencherDadosParaNovoLancamentoContabil();
			getNotaFiscalEntradaVO().reiniciarControleBloqueioCompetencia();
			getNotaFiscalEntradaVO().setDataEntradaAnterior(getNotaFiscalEntradaVO().getDataEntrada());

			setMensagemID(MSG_TELA.msg_dados_gravados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public String clonar() {
        try {
        	setNotaFiscalEntradaVO(this.notaFiscalEntradaVO.clonar());
        	preencherDadosParaNovoLancamentoContabil();
        	gerarLancamentoContabilPadrao();
        	getNotaFiscalEntradaVO().reiniciarControleBloqueioCompetencia();
			getNotaFiscalEntradaVO().setDataEntradaAnterior(getNotaFiscalEntradaVO().getDataEntrada());
            return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
        }
    }

	public void atualizarListaNotaFiscalEntradaRecebimentoCompraVO() {
		try {
			getFacadeFactory().getNotaFiscalEntradaFacade()
					.preencherNotaFiscalEntradaCompraPorFornecedor(getNotaFiscalEntradaVO(), getUsuarioLogado());
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void visualizarRecebimentoCompraItem() {
		try {
			setNotaFiscalEntradaRecebimentoCompraVO((NotaFiscalEntradaRecebimentoCompraVO) context()
					.getExternalContext().getRequestMap().get("notaFiscalEntradaRecebimentoCompraItens"));
			if (!Uteis.isAtributoPreenchido(
					getNotaFiscalEntradaRecebimentoCompraVO().getRecebimentoCompraVO().getRecebimentoCompraItemVOs())) {
				getNotaFiscalEntradaRecebimentoCompraVO().getRecebimentoCompraVO()
						.setRecebimentoCompraItemVOs(getFacadeFactory().getRecebimentoCompraItemFacade()
								.consultaRapidaPorNotaFiscalEntradaRecebimentoCompraVO(
										getNotaFiscalEntradaRecebimentoCompraVO(), getUsuarioLogado()));
				getNotaFiscalEntradaRecebimentoCompraVO().getRecebimentoCompraVO().atualizarValorTotal();
			}
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void removerRecebimentoCompraItem() {
		try {
			RecebimentoCompraItemVO rci = (RecebimentoCompraItemVO) context().getExternalContext().getRequestMap()
					.get("recebimentoCompraItemItens");
			getNotaFiscalEntradaRecebimentoCompraVO().getRecebimentoCompraVO()
					.excluirObjRecebimentoCompraItemVOs(rci.getCompraItem().getCodigo());
			setMensagemID(MSG_TELA.msg_dados_excluidos.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	private void limparEntidadeNotaFiscalEntrada() {
		setNotaFiscalEntradaRecebimentoCompraVO(new NotaFiscalEntradaRecebimentoCompraVO());
		setNotaFiscalEntradaItemVO(new NotaFiscalEntradaItemVO());
		setNotaFiscalEntradaImpostoVO(new NotaFiscalEntradaImpostoVO());
		setContaPagarVO(new ContaPagarVO());
		getContaPagarVO().setFornecedor(getNotaFiscalEntradaVO().getFornecedorVO());
		getContaPagarVO().setTipoOrigem(OrigemContaPagar.NOTA_FISCAL_ENTRADA.getValor());
		setLancamentoContabilVO(new LancamentoContabilVO());
		setCentroResultadoOrigemVO(new CentroResultadoOrigemVO());
	}

	public void gerarNotaFiscalEntradaItemPorRecebimentoCompraItem() {
		try {
			getNotaFiscalEntradaRecebimentoCompraVO().setSelecionado(true);
			getFacadeFactory().getNotaFiscalEntradaFacade().gerarNotaFiscalEntradaItemPorCompra(
					getNotaFiscalEntradaVO(), getNotaFiscalEntradaRecebimentoCompraVO(), getUsuarioLogado());
			getNotaFiscalEntradaVO().getListaNotaFiscalEntradaItem().forEach(NotaFiscalEntradaItemVO::calcularValorCentroResultadoOrigem);
			getNotaFiscalEntradaVO().getListaNotaFiscalEntradaItem().forEach(NotaFiscalEntradaItemVO::calcularPorcentagemCentroResultadoOrigem);
			limparEntidadeNotaFiscalEntrada();
			setMensagemID(MSG_TELA.msg_dados_adicionados.name());
		} catch (Exception e) {
			getNotaFiscalEntradaRecebimentoCompraVO().setSelecionado(false);
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void removerNotaFiscalEntradaItemPorRecebimentoCompra() {
		try {
			getNotaFiscalEntradaRecebimentoCompraVO().setSelecionado(false);
			getFacadeFactory().getNotaFiscalEntradaFacade().gerarNotaFiscalEntradaItemPorCompra(
					getNotaFiscalEntradaVO(), getNotaFiscalEntradaRecebimentoCompraVO(), getUsuarioLogado());
			limparEntidadeNotaFiscalEntrada();
			setMensagemID(MSG_TELA.msg_dados_excluidos.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void removerNotaFiscalEntradaRecebimentoCompraItens() {
		try {
			setNotaFiscalEntradaRecebimentoCompraVO((NotaFiscalEntradaRecebimentoCompraVO) context()
					.getExternalContext().getRequestMap().get("notaFiscalEntradaRecebimentoCompraItens"));
			removerNotaFiscalEntradaItemPorRecebimentoCompra();
			setMensagemID(MSG_TELA.msg_dados_excluidos.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void adicionarNotaFiscalEntradaItem() {
		try {
			getFacadeFactory().getNotaFiscalEntradaFacade().adicionarNotaFiscalEntradaItemManual(
					getNotaFiscalEntradaVO(), getNotaFiscalEntradaItemVO(), getUsuarioLogado());
			limparEntidadeNotaFiscalEntrada();
			setMensagemID(MSG_TELA.msg_dados_adicionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void editarNotaFiscalEntradaItem() {
		try {
			setNotaFiscalEntradaItemVO(
					(NotaFiscalEntradaItemVO) context().getExternalContext().getRequestMap().get(CONTEXT_PARA_EDICAO));
			getNotaFiscalEntradaItemVO().setEdicaoManual(true);
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}

	}

	public void removerNotaFiscalEntradaItem() {
		try {
			NotaFiscalEntradaItemVO nfei = (NotaFiscalEntradaItemVO) context().getExternalContext().getRequestMap()
					.get(CONTEXT_PARA_EDICAO);
			getFacadeFactory().getNotaFiscalEntradaFacade().removerNotaFiscalEntradaItem(getNotaFiscalEntradaVO(), nfei,
					true, getUsuarioLogado());
			setMensagemID(MSG_TELA.msg_dados_excluidos.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void addNotaFiscalEntradaImposto() {
		try {
			getFacadeFactory().getNotaFiscalEntradaFacade().addNotaFiscalEntradaImposto(getNotaFiscalEntradaVO(),
					getNotaFiscalEntradaImpostoVO(), getUsuarioLogado());
			limparEntidadeNotaFiscalEntrada();
			setMensagemID(MSG_TELA.msg_dados_adicionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void editarNotaFiscalEntradaImposto() {
		try {
			NotaFiscalEntradaImpostoVO obj = (NotaFiscalEntradaImpostoVO) context().getExternalContext().getRequestMap()
					.get("notaFiscalEntradaImpostoItens");
			setNotaFiscalEntradaImpostoVO((NotaFiscalEntradaImpostoVO) obj.clone());
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}

	}

	public void removerNotaFiscalEntradaImposto() {
		try {
			NotaFiscalEntradaImpostoVO nfei = (NotaFiscalEntradaImpostoVO) context().getExternalContext()
					.getRequestMap().get("notaFiscalEntradaImpostoItens");
			getFacadeFactory().getNotaFiscalEntradaFacade().removerNotaFiscalEntradaImposto(getNotaFiscalEntradaVO(),
					nfei, getUsuarioLogado());
			setMensagemID(MSG_TELA.msg_dados_excluidos.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void calcularPorcentagemNotaFiscalEntradaImposto() {
		try {
			getNotaFiscalEntradaImpostoVO().setNotaFiscalEntradaVO(getNotaFiscalEntradaVO());
			getNotaFiscalEntradaImpostoVO().calcularPorcentagemNotaFiscalEntradaImposto();
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void calcularValorNotaFiscalEntradaImposto() {
		try {
			getNotaFiscalEntradaImpostoVO().setNotaFiscalEntradaVO(getNotaFiscalEntradaVO());
			getNotaFiscalEntradaImpostoVO().calcularValorNotaFiscalEntradaImposto();
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void scrollerListener(DataScrollEvent dataScrollerEvent) {
		try {
			getControleConsultaOtimizado().setPaginaAtual(dataScrollerEvent.getPage());
			getControleConsultaOtimizado().setPage(dataScrollerEvent.getPage());
			consultarDados();
			setConsultaDataScroller(true);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP
	 * PlanoContaCons.jsp. Define o tipo de consulta a ser executada, por meio de
	 * ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
	 * resultado, disponibiliza um List com os objetos selecionados na sessao da
	 * pagina.
	 */
	@Override
	public void consultarDados() {
		try {
			super.consultar();
			getControleConsultaOtimizado().preencherDadosParaConsulta(true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA,
					getUsuarioLogado());
			getFacadeFactory().getNotaFiscalEntradaFacade().consultar(getControleConsultaOtimizado(),
					getNotaFiscalEntradaVO(), getDataEntradaInicio(), getDataEntradaFim());

			setTotalNotaFiscalEntrada(getFacadeFactory().getNotaFiscalEntradaFacade()
					.consultarTotalNotaFiscalEntradaPorFiltros(getNotaFiscalEntradaVO(), getControleConsultaOtimizado(),
							getDataEntradaInicio(), getDataEntradaFim()));
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			getListaConsulta().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	
	public void consultarProdutoServico() {
		try {
		 setListaProdutoServico(getFacadeFactory().getProdutoServicoFacade().consultarPorNome(getValorConsultaProdutoServico(), null, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuario()));	
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList<>());
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	
	public void selecionarProdutoServico() {
		try {
			getNotaFiscalEntradaVO().setProdutoServicoVO((ProdutoServicoVO) context().getExternalContext().getRequestMap().get("produtoServicoItens"));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void limparProdutoServico() throws Exception {
		removerObjetoMemoria(getNotaFiscalEntradaVO().getProdutoServicoVO());
		setListaProdutoServico(new ArrayList<ProdutoServicoVO>());
		setValorConsultaProdutoServico("");
		setMensagemID("msg_entre_prmconsulta");
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe
	 * <code>PlanoContaVO</code> Após a exclusão ela automaticamente aciona a rotina
	 * para uma nova inclusão.
	 */
	public String excluir() {
		try {
			getFacadeFactory().getNotaFiscalEntradaFacade().excluir(getNotaFiscalEntradaVO(), true, getUsuarioLogado());
			novo();
			setMensagemID(MSG_TELA.msg_dados_excluidos.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());

		}
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	public void selecionarFornecedor() {
		try {
			FornecedorVO obj = (FornecedorVO) context().getExternalContext().getRequestMap().get("fornecedorItens");
			obj = getFacadeFactory().getFornecedorFacade().consultarPorChavePrimaria(obj.getCodigo(), false,
					Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			getNotaFiscalEntradaVO().setFornecedorVO(obj);
			setCnpjOuCpf(getNotaFiscalEntradaVO().getFornecedorVO().getCnpjOuCfp_Apresentar());
			getContaPagarVO().setFornecedor(obj);
			getNotaFiscalEntradaVO().limparCamposLista();
			getFacadeFactory().getNotaFiscalEntradaFacade()
					.preencherNotaFiscalEntradaCompraPorFornecedor(getNotaFiscalEntradaVO(), getUsuarioLogado());
			if (obj.getPermiteenviarremessa() && Uteis.isAtributoPreenchido(getContaPagarVO().getBancoRemessaPagar())) {
				getFacadeFactory().getContaPagarFacade().preencherDadosRemessaContaPagar(getContaPagarVO(), obj.getNumeroBancoRecebimento(), obj.getNumeroAgenciaRecebimento(), obj.getDigitoAgenciaRecebimento(), obj.getContaCorrenteRecebimento(), obj.getDigitoCorrenteRecebimento(), 
						obj.getChaveEnderecamentoPix() ,
						obj.getTipoIdentificacaoChavePixEnum() ,
						getUsuarioLogado());
				setComboTipoLancamentoContaPagar(null);
				setComboTipoServicoContaPagar(null);
			}			
			getFacadeFactory().getNotaFiscalEntradaFacade().preencherNotaFiscalEntradaCompraPorFornecedor(getNotaFiscalEntradaVO(), getUsuarioLogado());
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			getNotaFiscalEntradaVO().setFornecedorVO(new FornecedorVO());
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarFornecedor() {
		try {
			super.consultar();
			List<FornecedorVO> objs = new ArrayList<>(0);
			switch (FornecedorVO.enumCampoConsultaFornecedor.valueOf(getCampoConsultaFornecedor())) {
			case NOME:
				objs = getFacadeFactory().getFornecedorFacade().consultarPorNome(getValorConsultaFornecedor(), "AT",
						false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
				break;
			case RAZAOSOCIAL:
				objs = getFacadeFactory().getFornecedorFacade().consultarPorRazaoSocial(getValorConsultaFornecedor(),
						"AT", false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
				break;
			case RG:
				objs = getFacadeFactory().getFornecedorFacade().consultarPorRG(getValorConsultaFornecedor(), "AT",
						false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
				break;
			case CPF:
				objs = getFacadeFactory().getFornecedorFacade().consultarPorCPF(getValorConsultaFornecedor(), "AT",
						false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
				break;
			case CNPJ:
				objs = getFacadeFactory().getFornecedorFacade().consultarPorCNPJ(getValorConsultaFornecedor(), "AT",
						false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
				break;
			default:
				break;

			}
			setListaConsultaFornecedor(objs);
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setListaConsultaFornecedor(new ArrayList<>(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public String getMascaraConsultaFornecedor() {
		if (getCampoConsultaFornecedor().equals("CNPJ")) {
			return "return mascara(this.form, 'formFornecedor:valorConsultaFornecedor', '99.999.999/9999-99', event);";
		} else if (getCampoConsultaFornecedor().equals("CPF")) {
			return "return mascara(this.form, 'formFornecedor:valorConsultaFornecedor', '999.999.999-99', event);";
		}
		return "";
	}

	public void selecionarNaturezaOperacao() {
		try {
			NaturezaOperacaoVO obj = (NaturezaOperacaoVO) context().getExternalContext().getRequestMap()
					.get("naturezaOperacaoItens");
			getNotaFiscalEntradaVO().setNaturezaOperacaoVO(obj);
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarNaturezaOperacao() {
		try {
			super.consultar();
			List<NaturezaOperacaoVO> objs = new ArrayList<>();
			switch (NaturezaOperacaoVO.enumCampoConsultaNaturezaOperacao.valueOf(getCampoConsultaNaturezaOperacao())) {
			case CODIGO:
				int valorInt;
				if (!Uteis.getIsValorNumerico(getValorConsultaNaturezaOperacao())) {
					throw new Exception("Informe apenas NÚMEROS para este filtro.");
				}
				if (getValorConsultaNaturezaOperacao().isEmpty()) {
					setValorConsultaNaturezaOperacao("0");
				}
				valorInt = Integer.parseInt(getValorConsultaNaturezaOperacao());
				objs = getFacadeFactory().getNaturezaOperacaoFacade().consultaRapidaPorCodigo(valorInt, false,
						Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
				break;
			case CODIGONATUREZAOPERACAO:
				objs = consultarNaturezaOperacaoPorCampoCodigo();
				break;
			case NOME:
				objs = getFacadeFactory().getNaturezaOperacaoFacade().consultaRapidaPorNome(
						getValorConsultaNaturezaOperacao(), false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA,
						getUsuarioLogado());
				break;
			case TIPONATURECAOPERACAO:
				objs = getFacadeFactory().getNaturezaOperacaoFacade().consultaRapidaPorTipoNaturezaOperacaoEnum(
						getTipoNaturezaOperacaoEnumCampoConsulta(), false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA,
						getUsuarioLogado());
				break;
			default:
				break;
			}
			setListaConsultaNaturezaOperacao(objs);
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setListaConsultaNaturezaOperacao(new ArrayList<>(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	private List<NaturezaOperacaoVO> consultarNaturezaOperacaoPorCampoCodigo() throws Exception {
		List<NaturezaOperacaoVO> objs;
		int valorInt;
		if (!Uteis.getIsValorNumerico(getValorConsultaNaturezaOperacao())) {
			throw new Exception("Informe apenas NÚMEROS para este filtro.");
		}
		if (getValorConsultaNaturezaOperacao().isEmpty()) {
			setValorConsultaNaturezaOperacao("0");
		}
		valorInt = Integer.parseInt(getValorConsultaNaturezaOperacao());
		objs = getFacadeFactory().getNaturezaOperacaoFacade().consultaRapidaPorCodigoNaturezaOperacao(valorInt, true,
				Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
		return objs;
	}

	public boolean isCampoConsultaSomenteNumeroNaturezaOperacao() {
		return !isCampoConsultaTipoNaturezaOperacao() && (getCampoConsultaNaturezaOperacao()
				.equals(NaturezaOperacaoVO.enumCampoConsultaNaturezaOperacao.CODIGO.name())
				|| getCampoConsultaNaturezaOperacao()
						.equals(NaturezaOperacaoVO.enumCampoConsultaNaturezaOperacao.CODIGONATUREZAOPERACAO.name()));
	}

	public boolean isCampoConsultaTextoNaturezaOperacao() {
		return !isCampoConsultaTipoNaturezaOperacao() && !isCampoConsultaSomenteNumeroNaturezaOperacao();
	}

	public boolean isCampoConsultaTipoNaturezaOperacao() {
		return getCampoConsultaNaturezaOperacao()
				.equals(NaturezaOperacaoVO.enumCampoConsultaNaturezaOperacao.TIPONATURECAOPERACAO.name());
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

	public void selecionarCentroDespesa() {
		try {
			CategoriaDespesaVO obj = (CategoriaDespesaVO) context().getExternalContext().getRequestMap()
					.get("centroDespesaItens");
			getCentroResultadoOrigemVO().setCategoriaDespesaVO(
					getFacadeFactory().getCategoriaDespesaFacade().consultarPorChavePrimaria(obj.getCodigo(), false,
							Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			limparDadosPorCategoriaDespesa();
			montarListaSelectItemTipoNivelCentroResultadoEnum();
			montarListaSelectItemUnidadeEnsinoCategoriaDespesa();
			preencherDadosPorCategoriaDespesa();
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarCentroDespesa() {
		try {
			super.consultar();
			List<CategoriaDespesaVO> objs = new ArrayList<>();
			switch (CategoriaDespesaVO.enumCampoConsultaCategoriaDespesa.valueOf(getCampoConsultaCentroDespesa())) {
			case DESCRICAO:
				objs = getFacadeFactory().getCategoriaDespesaFacade().consultarPorDescricao(
						getValorConsultaCentroDespesa(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
				break;
			case IDENTIFICADOR_CENTRO_DESPESA:
				objs = getFacadeFactory().getCategoriaDespesaFacade().consultarPorIdentificadorCategoriaDespesa(
						getValorConsultaCentroDespesa(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
				break;
			default:
				break;
			}
			setListaConsultaCentroDespesa(objs);
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setListaConsultaCentroDespesa(new ArrayList<>());
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void selecionarProduto() {
		try {
			ProdutoServicoVO obj = (ProdutoServicoVO) context().getExternalContext().getRequestMap()
					.get("produtoItens");
			getNotaFiscalEntradaItemVO().setProdutoServicoVO(
					getFacadeFactory().getProdutoServicoFacade().consultarPorChavePrimaria(obj.getCodigo(), false,
							Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarProduto() {
		try {
			super.consultar();
			List<ProdutoServicoVO> objs = new ArrayList<>(0);
			TipoProdutoServicoEnum tipoProdutoServicoEnum = getNotaFiscalEntradaVO().getTipoNotaFiscalEntradaEnum()
					.isProduto() ? TipoProdutoServicoEnum.PRODUTO : TipoProdutoServicoEnum.SERVICO;
			if (getCampoConsultaProduto().equals("nome")) {
				objs = getFacadeFactory().getProdutoServicoFacade().consultarPorNome(getValorConsultaProduto(), null,
						tipoProdutoServicoEnum, false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			}
			if (getCampoConsultaProduto().equals("nomeCategoriaProduto")) {
				objs = getFacadeFactory().getProdutoServicoFacade().consultarPorNomeCategoriaProduto(
						getValorConsultaProduto(), null, tipoProdutoServicoEnum, false,
						Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			}
			setListaConsultaProduto(objs);
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setListaConsultaProduto(new ArrayList<>(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}

	}

	public List<SelectItem> getTipoConsultaComboProduto() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("nomeCategoriaProduto", "Categoria Produto"));
		return itens;
	}

	public void consultarFuncionarioCentroCusto() {
		try {
			List<FuncionarioCargoVO> objs = new ArrayList<>(0);
			if (getCampoConsultaFuncionarioCentroCusto().equals("nomeFuncionario")) {
				objs = getFacadeFactory().getFuncionarioCargoFacade().consultarPorNomeFuncionarioUnidadeEnsinoSituacao(
						getValorConsultaFuncionarioCentroCusto(), null, true, false,
						Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionarioCentroCusto().equals("nomeCargo")) {
				objs = getFacadeFactory().getFuncionarioCargoFacade().consultarPorNomeCargoUnidadeEnsinoSituacao(
						getValorConsultaFuncionarioCentroCusto(), null, true, false,
						Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaFuncionarioCentroCusto(objs);
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setListaConsultaFuncionarioCentroCusto(new ArrayList<>(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void selecionarFuncionarioCentroCusto() {
		try {
			FuncionarioCargoVO obj = (FuncionarioCargoVO) context().getExternalContext().getRequestMap()
					.get("funcionarioCentroCustoItens");
			getCentroResultadoOrigemVO().setFuncionarioCargoVO(obj);
			preencherDadosPorCategoriaDespesa();
			Uteis.liberarListaMemoria(getListaConsultaFuncionarioCentroCusto());
			campoConsultaFuncionarioCentroCusto = null;
			valorConsultaFuncionarioCentroCusto = null;
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setListaConsultaFuncionarioCentroCusto(new ArrayList<>(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaComboFuncionarioCentroCusto() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("nomeFuncionario", "Nome"));
		itens.add(new SelectItem("nomeCargo", "Cargo"));
		return itens;
	}

	public String consultarDepartamento() {
		try {
			List<DepartamentoVO> objs = new ArrayList<>(0);
			Integer unidadeEnsino = 0;
			if (Uteis.isAtributoPreenchido(getCentroResultadoOrigemVO().getUnidadeEnsinoVO())) {
				unidadeEnsino = getCentroResultadoOrigemVO().getUnidadeEnsinoVO().getCodigo();
			} else {
				unidadeEnsino = getUnidadeEnsinoLogado().getCodigo();
			}
			if (getCampoConsultaDepartamento().equals("codigo")) {
				int valorInt = 0;
				if (!getValorConsultaDepartamento().equals("")) {
					valorInt = Integer.parseInt(getValorConsultaDepartamento());
				}
				objs = getFacadeFactory().getDepartamentoFacade().consultarPorCodigoPorUnidadeEnsino(
						new Integer(valorInt), unidadeEnsino, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,
						getUsuarioLogado());
			}
			if (getCampoConsultaDepartamento().equals("nome")) {
				objs = getFacadeFactory().getDepartamentoFacade().consultarPorNomePorUnidadeEnsino(
						getValorConsultaDepartamento(), unidadeEnsino, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,
						getUsuarioLogado());
			}
			setListaConsultaDepartamento(objs);
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
			return "consultar";
		} catch (Exception e) {
			setListaConsultaDepartamento(new ArrayList<>(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			return "consultar";
		}
	}

	public void selecionarDepartamento() {
		try {
			DepartamentoVO obj = (DepartamentoVO) context().getExternalContext().getRequestMap()
					.get("departamentoItens");
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

	public List<SelectItem> getTipoConsultaComboDepartamento() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
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
			getCentroResultadoOrigemVO().setTurmaVO(obj);
			getCentroResultadoOrigemVO().setCursoVO(obj.getCurso());
			getCentroResultadoOrigemVO().setTurnoVO(obj.getTurno());
			preencherDadosPorCategoriaDespesa();
			valorConsultaTurma = "";
			campoConsultaTurma = "";
			listaConsultaTurma.clear();
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarTurma() {
		try {
			super.consultar();
			Uteis.checkState(!Uteis.isAtributoPreenchido(getCentroResultadoOrigemVO().getUnidadeEnsinoVO()),
					"O campo Unidade Ensino (CEntro Resultado Origem) deve ser informado.");
			setListaConsultaTurma(getFacadeFactory().getTurmaFacade().consultar(getCampoConsultaTurma(),
					getValorConsultaTurma(), getCentroResultadoOrigemVO().getUnidadeEnsinoVO().getCodigo(), false,
					Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			getListaConsultaTurma().clear();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaComboTurma() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
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

	public void selecionarCurso() {
		try {
			CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItens");
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

	public void consultarCurso() {
		try {
			Uteis.checkState(!Uteis.isAtributoPreenchido(getCentroResultadoOrigemVO().getUnidadeEnsinoVO()),
					"O campo Unidade Ensino (Centro Resultado Origem) deve ser informado.");
			setListaConsultaCurso(getFacadeFactory().getCursoFacade().consultar(getCampoConsultaCurso(),
					getValorConsultaCurso(), getCentroResultadoOrigemVO().getUnidadeEnsinoVO().getCodigo(), true,
					Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setListaConsultaCurso(null);
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaComboCurso() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("nome", "Nome"));
		return itens;
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
			UnidadeEnsinoCursoVO obj = (UnidadeEnsinoCursoVO) context().getExternalContext().getRequestMap()
					.get("unidadeEnsinoCursoItens");
			getCentroResultadoOrigemVO().setCursoVO(obj.getCurso());
			getCentroResultadoOrigemVO().setTurnoVO(obj.getTurno());
			preencherDadosPorCategoriaDespesa();
			listaConsultaCurso.clear();
			this.setValorConsultaCurso("");
			this.setCampoConsultaCurso("");
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarCursoTurno() {
		try {
			Uteis.checkState(!Uteis.isAtributoPreenchido(getCentroResultadoOrigemVO().getUnidadeEnsinoVO()),
					"O campo Unidade Ensino (Centro Resultado Origem) deve ser informado.");
			setListaConsultaCursoTurno(
					getFacadeFactory().getUnidadeEnsinoCursoFacade().consultar(getCampoConsultaCursoTurno(),
							getValorConsultaCursoTurno(), getCentroResultadoOrigemVO().getUnidadeEnsinoVO().getCodigo(),
							false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setListaConsultaCursoTurno(null);
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaComboCursoTurno() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("curso", "Curso"));
		itens.add(new SelectItem("turno", "Turno"));
		return itens;
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
		getCentroResultadoDataModelo()
				.setCampoConsulta(CentroResultadoVO.enumCampoConsultaCentroResultado.DESCRICAO_CENTRO_RESULTADO.name());
	}

	public void selecionarCentroResultado() {
		try {
			CentroResultadoVO obj = (CentroResultadoVO) context().getExternalContext().getRequestMap()
					.get("centroResultadoItens");
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
			getCentroResultadoDataModelo().preencherDadosParaConsulta(false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA,
					getUsuarioLogado());
			getFacadeFactory().getCentroResultadoFacade().consultar(SituacaoEnum.ATIVO, true,
					getCentroResultadoOrigemVO().getDepartamentoVO(), getCentroResultadoOrigemVO().getCursoVO(),
					getCentroResultadoOrigemVO().getTurmaVO(), getCentroResultadoDataModelo());
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			getControleConsultaOtimizado().getListaConsulta().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void realizarDistribuicaoValoresCentroResultado() {
		try {
			if (getContaPagarVO().isEdicaoManual()
					&& getContaPagarVO().getValor() > getNotaFiscalEntradaVO().getLiquidoPagar()) {
				getContaPagarVO().setValor(null);
				throw new Exception(
						"Não é possivel realizar essa operação, pois o valor informado ultrapassa o valor Total Liquido da nota fiscal de entrada.");
			} else {
				getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().realizarDistribuicaoValoresCentroResultado(
						getContaPagarVO().getListaCentroResultadoOrigemVOs(),
						getContaPagarVO().getPrevisaoValorPagoSemDescontoPorUsoAdiantamento(), getUsuarioLogado());
				if (Uteis.isAtributoPreenchido(getContaPagarVO().getListaContaPagarAdiantamentoVO())) {
					atualizarValoresAdiantamentoUtilizados();
				}
				setMensagemID(MSG_TELA.msg_dados_selecionados.name());
			}

		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void calcularPorcentagemCentroResultadoOrigem() {
		try {
			getCentroResultadoOrigemVO().calcularPorcentagem(getNotaFiscalEntradaItemVO().getValorTotal());
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void calcularValorCentroResultadoOrigem() {
		try {
			getCentroResultadoOrigemVO().calcularValor(getNotaFiscalEntradaItemVO().getValorTotal());
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void abrirCentroResultadoOrigemNotaFiscalEntradaItem() {
		try {
			setNotaFiscalEntradaItemVO(
					(NotaFiscalEntradaItemVO) context().getExternalContext().getRequestMap().get(CONTEXT_PARA_EDICAO));
			getNotaFiscalEntradaItemVO().setEdicaoManual(true);
			setCentroResultadoOrigemVO(new CentroResultadoOrigemVO());
			preencherCentroResultadoOrigemFiscalEntradaItem();
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	private void preencherCentroResultadoOrigemFiscalEntradaItem() throws Exception {
		if (Uteis.isAtributoPreenchido(
				getNotaFiscalEntradaItemVO().getProdutoServicoVO().getCategoriaProduto().getCategoriaDespesa())) {
			getCentroResultadoOrigemVO()
					.setCategoriaDespesaVO(getFacadeFactory().getCategoriaDespesaFacade().consultarPorChavePrimaria(
							getNotaFiscalEntradaItemVO().getProdutoServicoVO().getCategoriaProduto()
									.getCategoriaDespesa().getCodigo(),
							false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			montarListaSelectItemTipoNivelCentroResultadoEnum();
		}
		montarListaSelectItemUnidadeEnsinoCategoriaDespesa();
		preencherDadosPorCategoriaDespesa();
	}

	public void fecharCentroResultadoOrigemNotaFiscalEntradaItem() {
		try {
			setNotaFiscalEntradaItemVO(new NotaFiscalEntradaItemVO());
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void adicionarCentroResultadoOrigemNotaFiscalEntradaItem() {
		try {
			getFacadeFactory().getNotaFiscalEntradaFacade().adicionarCentroResultadoOrigemNotaFiscalEntradaItem(
					getNotaFiscalEntradaVO(), getNotaFiscalEntradaItemVO(), getCentroResultadoOrigemVO(),
					getUsuarioLogado());
			setCentroResultadoOrigemVO(new CentroResultadoOrigemVO());
			preencherCentroResultadoOrigemFiscalEntradaItem();
			setMensagemID(MSG_TELA.msg_dados_adicionados.name());
		} catch (Exception e) {

			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void editarCentroResultadoOrigemNotaFiscalEntradaItem() {
		try {
			setCentroResultadoOrigemVO((CentroResultadoOrigemVO) context().getExternalContext().getRequestMap()
					.get("centroResultadoOrigemItem"));
			montarListaSelectItemTipoNivelCentroResultadoEnum();
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}

	}

	public void removerCentroResultadoOrigemNotaFiscalEntradaItem() {
		try {
			CentroResultadoOrigemVO centroResultadoOrigem = (CentroResultadoOrigemVO) context().getExternalContext()
					.getRequestMap().get("centroResultadoOrigemItem");
			getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().removerCentroResultadoOrigem(
					getNotaFiscalEntradaItemVO().getListaCentroResultadoOrigemVOs(), centroResultadoOrigem,
					getUsuarioLogado());
			setMensagemID(MSG_TELA.msg_dados_excluidos.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void preencherDadosPorCategoriaDespesa() {
		try {
			if (Uteis.isAtributoPreenchido(getCentroResultadoOrigemVO().getCategoriaDespesaVO())) {
				getFacadeFactory().getCentroResultadoOrigemInterfaceFacade()
						.preencherDadosPorCategoriaDespesa(getCentroResultadoOrigemVO(), getUsuarioLogado());
			}
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	private void inicializarListasSelectItemTodosComboBox() {
		montarListaSelectItemUnidadeEnsino();
		montarListaSelectItemImposto();
		montarListaSelectItemFormaPagamento();
		// montarListaSelectItemTipoLancamentoCContaPagar();
		// montarListaSelectItemTipoServicoContaPagar();
		montarListaSelectItemBancoFavorecido();
	}

	public void montarListaSelectItemUnidadeEnsinoCategoriaDespesa() throws Exception {
		getListaSelectItemUnidadeEnsinoCategoriaDespesa().clear();
		if (getUnidadeEnsinoLogado().getCodigo().intValue() != 0) {
			getListaSelectItemUnidadeEnsinoCategoriaDespesa()
					.add(new SelectItem(getUnidadeEnsinoLogado().getCodigo(), getUnidadeEnsinoLogado().getNome()));
			getCentroResultadoOrigemVO().getUnidadeEnsinoVO().setCodigo(getUnidadeEnsinoLogado().getCodigo());
			return;
		}
		getListaSelectItemUnidadeEnsinoCategoriaDespesa().add(new SelectItem(0, ""));
		List<UnidadeEnsinoVO> listaUnidadeEnsino = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome("", 0,
				false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		listaUnidadeEnsino.stream().forEach(
				p -> getListaSelectItemUnidadeEnsinoCategoriaDespesa().add(new SelectItem(p.getCodigo(), p.getNome())));
		if (listaUnidadeEnsino.size() == 1) {
			getCentroResultadoOrigemVO().getUnidadeEnsinoVO().setCodigo(listaUnidadeEnsino.get(0).getCodigo());
		} else {
			getCentroResultadoOrigemVO().getUnidadeEnsinoVO()
					.setCodigo(getNotaFiscalEntradaVO().getUnidadeEnsinoVO().getCodigo());
		}
	}

	public void montarListaSelectItemUnidadeEnsino() {
		try {
			getListaSelectItemUnidadeEnsino().clear();
			if (getUnidadeEnsinoLogado().getCodigo().intValue() != 0) {
				setListaSelectItemUnidadeEnsino(new ArrayList<>());
				getListaSelectItemUnidadeEnsino().add(new SelectItem(0, ""));
				UnidadeEnsinoVO obj = getFacadeFactory().getUnidadeEnsinoFacade()
						.consultaRapidaPorCodigo(getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
				getNotaFiscalEntradaVO().setUnidadeEnsinoVO(obj);
				getListaSelectItemUnidadeEnsino().add(new SelectItem(obj.getCodigo(), obj.getNome_CNPJ()));
				return;
			}
			List<UnidadeEnsinoVO> resultadoConsulta = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome("",
					0, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			setListaSelectItemUnidadeEnsino(
					UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome_CNPJ", false));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void montarListaSelectItemImposto() {
		try {
			montarListaSelectItemImposto("");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void montarListaSelectItemImposto(String prm) {
		List<ImpostoVO> resultadoConsulta = null;
		try {
			DataModelo dataModelo = (new DataModelo(false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			dataModelo.setLimitePorPagina(0);
			resultadoConsulta = getFacadeFactory().getImpostoFacade().consultaRapidaPorNome(prm, dataModelo);
			getListaSelectItemImposto().clear();
			getListaSelectItemImposto().add(new SelectItem(0, ""));
			resultadoConsulta.stream()
					.forEach(p -> getListaSelectItemImposto().add(new SelectItem(p.getCodigo(), p.getNome())));
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
		}
	}

	public void montarListaSelectItemBancoFavorecido() {
		List<BancoVO> resultadoConsulta = null;
		try {
			resultadoConsulta = getFacadeFactory().getBancoFacade().consultarPorNome("", false,
					Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			getListaSelectItemBancoFavorecido().clear();
			getListaSelectItemBancoFavorecido().add(new SelectItem(0, ""));
			resultadoConsulta.stream()
					.forEach(p -> getListaSelectItemBancoFavorecido().add(new SelectItem(p.getCodigo(), p.getNome())));
		} catch (Exception e) {
			throw new StreamSeiException(e);
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
		}
	}

	public void montarListaSelectItemFormaPagamento() {
		List<FormaPagamentoVO> resultadoConsulta = null;
		try {
			resultadoConsulta = getFacadeFactory().getFormaPagamentoFacade().consultarPorNome("", false,
					Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
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

	public void montarListaSelectItemTipoServicoContaPagar() {
		getComboTipoServicoContaPagar().clear();
		for (TipoServicoContaPagarEnum tipoServicoContaPagarEnum : TipoServicoContaPagarEnum.values()) {
			getComboTipoServicoContaPagar()
					.add(new SelectItem(tipoServicoContaPagarEnum, tipoServicoContaPagarEnum.getDescricao()));
		}

	}

	public void montarListaSelectItemTipoLancamentoCContaPagar() {
		getComboTipoLancamentoContaPagar().clear();
		for (TipoLancamentoContaPagarEnum tipoLancamentoContaPagarEnum : TipoLancamentoContaPagarEnum.values()) {
			getComboTipoLancamentoContaPagar()
					.add(new SelectItem(tipoLancamentoContaPagarEnum, tipoLancamentoContaPagarEnum.getDescricao()));
		}

	}

	public void selecionarTipoNotaFiscalEntrada() {
		try {
			getNotaFiscalEntradaVO().getListaNotaFiscalEntradaImposto().clear();
			getNotaFiscalEntradaVO().getListaNotaFiscalEntradaItem().clear();
			getNotaFiscalEntradaVO().getListaContaPagar().clear();
			getNotaFiscalEntradaVO().getListaContaPagarOutrasOrigem().clear();
			getNotaFiscalEntradaVO().getListaLancamentoContabeisCredito().clear();
			getNotaFiscalEntradaVO().getListaLancamentoContabeisDebito().clear();
			getNotaFiscalEntradaVO().atualizarTotalizadoresLista();
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void gerarLancamentoContabilPadrao() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(
					PerfilAcessoPermissaoNotaFiscalEnum.PERMITIR_ALTERAR_LANCAMENTO_CONTABIL_NOTA_FISCAL_ENTRADA,
					getUsuarioLogado());
			getFacadeFactory().getNotaFiscalEntradaFacade().gerarLancamentoContabilPadrao(getNotaFiscalEntradaVO(),
					getUsuarioLogado());
			setMensagemID(MSG_TELA.msg_dados_editar.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void novoLancamentoContabilCredito() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(
					PerfilAcessoPermissaoNotaFiscalEnum.PERMITIR_ALTERAR_LANCAMENTO_CONTABIL_NOTA_FISCAL_ENTRADA,
					getUsuarioLogado());
			setLancamentoContabilVO(new LancamentoContabilVO());
			getLancamentoContabilVO().setTipoPlanoConta(TipoPlanoContaEnum.CREDITO);
			preencherDadosParaNovoLancamentoContabil();
			setModalAptoParaSerFechado("");
			setMensagemID(MSG_TELA.msg_dados_editar.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void novoLancamentoContabilDebito() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(
					PerfilAcessoPermissaoNotaFiscalEnum.PERMITIR_ALTERAR_LANCAMENTO_CONTABIL_NOTA_FISCAL_ENTRADA,
					getUsuarioLogado());
			setLancamentoContabilVO(new LancamentoContabilVO());
			getLancamentoContabilVO().setTipoPlanoConta(TipoPlanoContaEnum.DEBITO);
			preencherDadosParaNovoLancamentoContabil();
			setModalAptoParaSerFechado("");
			setMensagemID(MSG_TELA.msg_dados_editar.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void preencherDadosParaNovoLancamentoContabil() {
		try {
			getLancamentoContabilVO()
					.setTipoOrigemLancamentoContabilEnum(TipoOrigemLancamentoContabilEnum.NOTA_FISCAL_ENTRADA);
			setImpostoFiltroLancamentoContabil(Uteis.isAtributoPreenchido(getLancamentoContabilVO().getImpostoVO())
					? getLancamentoContabilVO().getImpostoVO()
					: new ImpostoVO());
			setCategoriaProdutoFiltroLancamentoContabil(
					Uteis.isAtributoPreenchido(getLancamentoContabilVO().getCategoriaProdutoVO())
							? getLancamentoContabilVO().getCategoriaProdutoVO()
							: new CategoriaProdutoVO());
			getListaSelectItemCategoriaProdutoFiltroLancamentoContabil().clear();
			getListaSelectItemImpostoFiltroLancamentoContabil().clear();
			for (Map.Entry<Integer, List<NotaFiscalEntradaItemVO>> mapaCategoriaProduto : getNotaFiscalEntradaVO()
					.getMapaCategoriaProdutoNotaFiscal().entrySet()) {
				getListaSelectItemCategoriaProdutoFiltroLancamentoContabil().add(new SelectItem(
						mapaCategoriaProduto.getKey(),
						mapaCategoriaProduto.getValue().get(0).getProdutoServicoVO().getCategoriaProduto().getNome()));
			}
			getNotaFiscalEntradaVO().getListaNotaFiscalEntradaImposto().stream()
					.filter(NotaFiscalEntradaImpostoVO::isRetido)
					.forEach(p -> getListaSelectItemImpostoFiltroLancamentoContabil()
							.add(new SelectItem(p.getImpostoVO().getCodigo(), p.getImpostoVO().getNome())));
			setMensagemID(MSG_TELA.msg_dados_editar.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void addLancamentoContabil() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(
					PerfilAcessoPermissaoNotaFiscalEnum.PERMITIR_ALTERAR_LANCAMENTO_CONTABIL_NOTA_FISCAL_ENTRADA,
					getUsuarioLogado());
			getFacadeFactory().getNotaFiscalEntradaFacade().addLancamentoContabilVO(getNotaFiscalEntradaVO(),
					getLancamentoContabilVO(), getCategoriaProdutoFiltroLancamentoContabil(),
					getImpostoFiltroLancamentoContabil(), getUsuarioLogado());
			setLancamentoContabilVO(new LancamentoContabilVO());
			setModalAptoParaSerFechado("RichFaces.$('modalLancamentoContabil').hide();");
			setMensagemID(MSG_TELA.msg_dados_editar.name());
		} catch (Exception e) {
			setModalAptoParaSerFechado("");
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void editarLancamentoContabil() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(
					PerfilAcessoPermissaoNotaFiscalEnum.PERMITIR_ALTERAR_LANCAMENTO_CONTABIL_NOTA_FISCAL_ENTRADA,
					getUsuarioLogado());
			LancamentoContabilVO obj = (LancamentoContabilVO) context().getExternalContext().getRequestMap()
					.get("lancamentoContabilItens");
			obj.setEdicaoManual(true);
			if (obj.isNotaFiscalEntradaCategoriaProduto()) {
				getNotaFiscalEntradaVO().setTipoLancamentoContabilNotaFiscalEntradaEnum(
						tipoLancamentoContabilNotaFiscalEntrada.NOTA_FISCAL_ENTRADA_CATEGORIA_PRODUTO);
			} else if (obj.isNotaFiscalEntradaImposto()) {
				getNotaFiscalEntradaVO().setTipoLancamentoContabilNotaFiscalEntradaEnum(
						tipoLancamentoContabilNotaFiscalEntrada.NOTA_FISCAL_ENTRADA_IMPOSTO);
			}
			setLancamentoContabilVO(obj);
			preencherDadosParaNovoLancamentoContabil();
			setModalAptoParaSerFechado("");
			setMensagemID(MSG_TELA.msg_dados_editar.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void removerLancamentoContabil() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(
					PerfilAcessoPermissaoNotaFiscalEnum.PERMITIR_ALTERAR_LANCAMENTO_CONTABIL_NOTA_FISCAL_ENTRADA,
					getUsuarioLogado());
			LancamentoContabilVO obj = (LancamentoContabilVO) context().getExternalContext().getRequestMap()
					.get("lancamentoContabilItens");
			getFacadeFactory().getNotaFiscalEntradaFacade().removeLancamentoContabilVO(getNotaFiscalEntradaVO(), obj,
					getUsuarioLogado());
			setMensagemID(MSG_TELA.msg_dados_editar.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void recalcularRaterioLancamentoContabil() {
		try {
			getLancamentoContabilVO().recalcularRaterioLancamentoContabil();
			setMensagemID(MSG_TELA.msg_dados_editar.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void selecionarPlanoContaDebito() {
		try {
			PlanoContaVO obj = (PlanoContaVO) context().getExternalContext().getRequestMap().get("planoContaItens");
			getLancamentoContabilVO().setPlanoContaVO(obj);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarPlanoContaDebito() {
		try {
			setListaConsultaPlanoConta(getFacadeFactory().getPlanoContaFacade().consultar(
					getLancamentoContabilVO().getUnidadeEnsinoVO().getCodigo(), getCampoConsultaPlanoConta(),
					getValorConsultaPlanoConta(), false, getUsuarioLogado()));
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setListaConsultaPlanoConta(new ArrayList<>());
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void selecionarPlanoContaCredito() {
		try {
			PlanoContaVO obj = (PlanoContaVO) context().getExternalContext().getRequestMap().get("planoContaItens");
			getLancamentoContabilVO().setPlanoContaVO(obj);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarPlanoContaCredito() {
		try {
			setListaConsultaPlanoConta(getFacadeFactory().getPlanoContaFacade().consultar(
					getLancamentoContabilVO().getUnidadeEnsinoVO().getCodigo(), getCampoConsultaPlanoConta(),
					getValorConsultaPlanoConta(), false, getUsuarioLogado()));
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setListaConsultaPlanoConta(new ArrayList<>());
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void limparCamposPorPlanoConta() {
		try {
			setListaConsultaPlanoConta(new ArrayList<>());
			setValorConsultaPlanoConta("");
			setCampoConsultaPlanoConta("");
			getLancamentoContabilVO().getPlanoContaVO().setIdentificadorPlanoConta("");
			getLancamentoContabilVO().getPlanoContaVO().setDescricao("");
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setListaConsultaPlanoConta(new ArrayList<>());
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void editarContaPagar() {
		try {
			setContaPagarVO((ContaPagarVO) context().getExternalContext().getRequestMap().get("contaPagarItens"));
			getContaPagarVO().setEdicaoManual(true);
			getContaPagarVO().validarSubistituirTipoLancamentoDepreciado();
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}

	}

	public void removerContaPagar() {
		try {
			setContaPagarVO((ContaPagarVO) context().getExternalContext().getRequestMap().get("contaPagarItens"));
			getFacadeFactory().getNotaFiscalEntradaFacade().removerNotaFiscalEntradaContaPagar(getNotaFiscalEntradaVO(),
					getContaPagarVO());
			setContaPagarVO(new ContaPagarVO());
			getContaPagarVO().setFornecedor(getNotaFiscalEntradaVO().getFornecedorVO());
			getContaPagarVO().setTipoOrigem(OrigemContaPagar.NOTA_FISCAL_ENTRADA.getValor());
			setMensagemID(MSG_TELA.msg_dados_excluidos.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void adicionarContaPagar() {
		try {	
			getFacadeFactory().getNotaFiscalEntradaFacade().preencherDadosContaPagarPadrao(getNotaFiscalEntradaVO(),getContaPagarVO(), getUsuarioLogado());
			if (!getContaPagarVO().isQuitada() && getContaPagarVO().getListaContaPagarAdiantamentoVO().isEmpty()) {
				getFacadeFactory().getNotaFiscalEntradaFacade().verificarAdiantamentosDisponiveisParaAbatimentoContasPagar(getNotaFiscalEntradaVO(),getUsuarioLogado());
				if (Uteis.isAtributoPreenchido(getNotaFiscalEntradaVO().getListaAdiantamentosUtilizadosAbaterContasPagar())) {
					setModalAptoAdiantamentosDisponiveisDistribuicao("RichFaces.$('painelAdiantamentosDisponiveis').show();");					                              
				}
			} else {
				setModalAptoAdiantamentosDisponiveisDistribuicao("");
			}
			setContaPagarVO(new ContaPagarVO());
			getContaPagarVO().setTipoOrigem(OrigemContaPagar.NOTA_FISCAL_ENTRADA.getValor());
			getContaPagarVO().setFornecedor(getNotaFiscalEntradaVO().getFornecedorVO());
			setMensagemID(MSG_TELA.msg_dados_adicionados.name());
		} catch (Exception e) {
			setModalAptoAdiantamentosDisponiveisDistribuicao("");
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void verificarAdiantamentosDisponiveisParaAbatimentoContasPagar() {
		try {
			if (!getNotaFiscalEntradaVO().getListaNotaFiscalEntradaRecebimentoCompra().isEmpty()
					&& !getNotaFiscalEntradaVO().getListaContaPagar().isEmpty()
					&& getNotaFiscalEntradaVO().getListaContaPagar().stream().allMatch(p -> !p.isQuitada())) {
				getFacadeFactory().getNotaFiscalEntradaFacade()
						.verificarAdiantamentosDisponiveisParaAbatimentoContasPagar(getNotaFiscalEntradaVO(),
								getUsuarioLogado());
				if (Uteis.isAtributoPreenchido(
						getNotaFiscalEntradaVO().getListaAdiantamentosUtilizadosAbaterContasPagar())) {
					setModalAptoAdiantamentosDisponiveisDistribuicao(
							"RichFaces.$('painelAdiantamentosDisponiveis').show();");
				}
			} else {
				setModalAptoAdiantamentosDisponiveisDistribuicao("");
			}
			setMensagemID(MSG_TELA.msg_dados_adicionados.name());
		} catch (Exception e) {
			setModalAptoAdiantamentosDisponiveisDistribuicao("");
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void atualizarValoresAdiantamentoUtilizados() {
		try {
			getFacadeFactory().getNotaFiscalEntradaFacade().limparDistribuicaoAdiantamentosDisponiveisParaAbaterContasPagar(getNotaFiscalEntradaVO(), true);
			getFacadeFactory().getNotaFiscalEntradaFacade().realizarDistribuicaoAutomaticaAdiantamentosDisponiveisNotaFiscalEntrada(getNotaFiscalEntradaVO(),getUsuarioLogado());
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemID("msg_erro");
		}
	}

	public void fecharAdiantamentosUtilizados() {
		try {
			//adicionarContaPagar();
			setMensagemID(MSG_TELA.msg_dados_adicionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void confirmarDistribuicaoAdiantamentosParaAbatimentoContasPagar() {
		try {
			setModalAptoAdiantamentosDisponiveisDistribuicao("");
			setMensagemID(MSG_TELA.msg_dados_adicionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void removerDistribuicaoAdiantamentosParaAbatimentoContasPagar() {
		try {
			getFacadeFactory().getNotaFiscalEntradaFacade()
					.limparDistribuicaoAdiantamentosDisponiveisParaAbaterContasPagar(getNotaFiscalEntradaVO(), false);
			getNotaFiscalEntradaVO().setListaAdiantamentosUtilizadosAbaterContasPagar(new ArrayList<>());
			setModalAptoAdiantamentosDisponiveisDistribuicao("");
			setMensagemID("msg_dados_atualizados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void removerAdiantamentosContasPagar() {
		try {
			ContaPagarAdiantamentoVO cpa = (ContaPagarAdiantamentoVO) context().getExternalContext().getRequestMap().get("adiantamentoItem");
			getFacadeFactory().getNotaFiscalEntradaFacade().removerAdiantamentosDisponiveisParaAbaterContasPagar(getNotaFiscalEntradaVO(), cpa, getUsuarioLogadoClone());
			setMensagemID("msg_dados_atualizados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void atualizarDatasParaLancamentoContabil() {
		try {
			getNotaFiscalEntradaVO().atualizarDatasParaLancamentoContabil();
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemID("msg_erro");
		}
	}

	public void selecionarFormaPagamentoContaPagar() {
		try {
			if (getContaPagarVO().isTipoSacadoFornecedor()
					&& Uteis.isAtributoPreenchido(getContaPagarVO().getFornecedor())
					&& getContaPagarVO().getFornecedor().getPermiteenviarremessa()) {
				getFacadeFactory().getContaPagarFacade().preencherDadosRemessaContaPagar(getContaPagarVO(),
						getContaPagarVO().getFornecedor().getNumeroBancoRecebimento(),
						getContaPagarVO().getFornecedor().getNumeroAgenciaRecebimento(),
						getContaPagarVO().getFornecedor().getDigitoAgenciaRecebimento(),
						getContaPagarVO().getFornecedor().getContaCorrenteRecebimento(),
						getContaPagarVO().getFornecedor().getDigitoCorrenteRecebimento(),
						getContaPagarVO().getFornecedor().getChaveEnderecamentoPix() ,
						getContaPagarVO().getFornecedor().getTipoIdentificacaoChavePixEnum(),
						getUsuarioLogado());
			} else if (getContaPagarVO().isTipoSacadoParceiro()
					&& Uteis.isAtributoPreenchido(getContaPagarVO().getParceiro())
					&& getContaPagarVO().getParceiro().getPermiteenviarremessa()) {
				getFacadeFactory().getContaPagarFacade().preencherDadosRemessaContaPagar(getContaPagarVO(),
						getContaPagarVO().getParceiro().getNumeroBancoRecebimento(),
						getContaPagarVO().getParceiro().getNumeroAgenciaRecebimento(),
						getContaPagarVO().getParceiro().getDigitoAgenciaRecebimento(),
						getContaPagarVO().getParceiro().getContaCorrenteRecebimento(),
						getContaPagarVO().getParceiro().getDigitoCorrenteRecebimento(),
						getContaPagarVO().getParceiro().getChaveEnderecamentoPix() ,
						getContaPagarVO().getParceiro().getTipoIdentificacaoChavePixEnum(),
						getUsuarioLogado());
			} else if (getContaPagarVO().isTipoSacadoFuncionario()
					&& Uteis.isAtributoPreenchido(getContaPagarVO().getFuncionario())) {
				getFacadeFactory().getContaPagarFacade().preencherDadosRemessaContaPagar(getContaPagarVO(),
						getContaPagarVO().getFuncionario().getNumeroBancoRecebimento(),
						getContaPagarVO().getFuncionario().getNumeroAgenciaRecebimento(),
						getContaPagarVO().getFuncionario().getDigitoAgenciaRecebimento(),
						getContaPagarVO().getFuncionario().getContaCorrenteRecebimento(),
						getContaPagarVO().getFuncionario().getDigitoCorrenteRecebimento(), 
						getContaPagarVO().getFuncionario().getChaveEnderecamentoPix() ,
						getContaPagarVO().getFuncionario().getTipoIdentificacaoChavePixEnum(),
						getUsuarioLogado());
			}
			if ((getContaPagarVO().getBancoRemessaPagar().getNrBanco().equals("033")
					|| getContaPagarVO().getBancoRemessaPagar().getNrBanco().equals("756"))
					&& (Uteis.isAtributoPreenchido(getContaPagarVO().getFormaPagamentoVO().getCodigo()))) {
				getContaPagarVO().setFormaPagamentoVO(getFacadeFactory().getFormaPagamentoFacade()
						.consultarPorChavePrimaria(getContaPagarVO().getFormaPagamentoVO().getCodigo(), false,
								Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
				switch (getContaPagarVO().getFormaPagamentoVO().getTipoFormaPagamentoEnum()) {
				case BOLETO_BANCARIO:
					getContaPagarVO()
							.setTipoLancamentoContaPagar(TipoLancamentoContaPagarEnum.LIQUIDACAO_TITULO_OUTRO_BANCO);
					break;
				case DEBITO_EM_CONTA_CORRENTE:
					getContaPagarVO().setTipoLancamentoContaPagar(TipoLancamentoContaPagarEnum.CREDITO_CONTA_CORRENTE);
					break;
				case DEPOSITO:
					getContaPagarVO()
							.setTipoLancamentoContaPagar(TipoLancamentoContaPagarEnum.TRANSFERENCIA_OUTRO_BANCO);
					break;
				default:
					break;
				}
				preencherCamposTipoLancamento();
				getContaPagarVO().setTipoServicoContaPagar(TipoServicoContaPagarEnum.PAGAMENTO_DIVERSOS);
			} else if ((getContaPagarVO().getBancoRemessaPagar().getNrBanco().equals("237"))
					&& (Uteis.isAtributoPreenchido(getContaPagarVO().getFormaPagamentoVO().getCodigo()))) {
				getContaPagarVO().setFormaPagamentoVO(getFacadeFactory().getFormaPagamentoFacade()
						.consultarPorChavePrimaria(getContaPagarVO().getFormaPagamentoVO().getCodigo(), false,
								Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
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
					getContaPagarVO().setTipoLancamentoContaPagar(TipoLancamentoContaPagarEnum.TRANSFERENCIA_OUTRO_BANCO);
					break;
				default:
					break;
				}
				preencherCamposTipoLancamento();
				getContaPagarVO().setTipoServicoContaPagar(TipoServicoContaPagarEnum.PAGAMENTO_DIVERSOS);
			}else if ((getContaPagarVO().getBancoRemessaPagar().getNrBanco().equals("341"))
					&& (Uteis.isAtributoPreenchido(getContaPagarVO().getFormaPagamentoVO().getCodigo()))) {
				getContaPagarVO().setFormaPagamentoVO(getFacadeFactory().getFormaPagamentoFacade()
						.consultarPorChavePrimaria(getContaPagarVO().getFormaPagamentoVO().getCodigo(), false,
								Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
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
					getContaPagarVO().setFormaPagamentoVO(getFacadeFactory().getFormaPagamentoFacade()
							.consultarPorChavePrimaria(getContaPagarVO().getFormaPagamentoVO().getCodigo(), false,
									Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
				} else {
					getContaPagarVO().setFormaPagamentoVO(null);
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
		} catch (Exception ex) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), ex.getMessage());
		}
	}

	public void limparCamposRemessaBancaria() {
		try {
			if (Uteis.isAtributoPreenchido(getContaPagarVO().getBancoRemessaPagar().getCodigo())) {
				getContaPagarVO().setBancoRemessaPagar(getFacadeFactory().getBancoFacade().consultarPorChavePrimaria(
						getContaPagarVO().getBancoRemessaPagar().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS,
						getUsuarioLogado()));
				getContaPagarVO().setTipoLancamentoContaPagar(null);
				getContaPagarVO().setTipoServicoContaPagar(null);
				getContaPagarVO().setModalidadeTransferenciaBancariaEnum(null);
				getContaPagarVO().setFinalidadeDocEnum(null);
				getContaPagarVO().setFinalidadeTedEnum(null);
				setComboTipoLancamentoContaPagar(null);
				setComboTipoServicoContaPagar(null);
				montarListaSelectItemFormaPagamento();
				if (getContaPagarVO().isTipoSacadoFornecedor()
						&& Uteis.isAtributoPreenchido(getContaPagarVO().getFornecedor())
						&& getContaPagarVO().getFornecedor().getPermiteenviarremessa()) {
					getFacadeFactory().getContaPagarFacade().preencherDadosRemessaContaPagar(getContaPagarVO(),
							getContaPagarVO().getFornecedor().getNumeroBancoRecebimento(),
							getContaPagarVO().getFornecedor().getNumeroAgenciaRecebimento(),
							getContaPagarVO().getFornecedor().getDigitoAgenciaRecebimento(),
							getContaPagarVO().getFornecedor().getContaCorrenteRecebimento(),
							getContaPagarVO().getFornecedor().getDigitoCorrenteRecebimento(), 
							getContaPagarVO().getFornecedor().getChaveEnderecamentoPix() ,
							getContaPagarVO().getFornecedor().getTipoIdentificacaoChavePixEnum(),
							getUsuarioLogado());
				} else if (getContaPagarVO().isTipoSacadoParceiro()
						&& Uteis.isAtributoPreenchido(getContaPagarVO().getParceiro())
						&& getContaPagarVO().getParceiro().getPermiteenviarremessa()) {
					getFacadeFactory().getContaPagarFacade().preencherDadosRemessaContaPagar(getContaPagarVO(),
							getContaPagarVO().getParceiro().getNumeroBancoRecebimento(),
							getContaPagarVO().getParceiro().getNumeroAgenciaRecebimento(),
							getContaPagarVO().getParceiro().getDigitoAgenciaRecebimento(),
							getContaPagarVO().getParceiro().getContaCorrenteRecebimento(),
							getContaPagarVO().getParceiro().getDigitoCorrenteRecebimento(), 
							getContaPagarVO().getParceiro().getChaveEnderecamentoPix() ,
							getContaPagarVO().getParceiro().getTipoIdentificacaoChavePixEnum(),
							getUsuarioLogado());
				} else if (getContaPagarVO().isTipoSacadoFuncionario()
						&& Uteis.isAtributoPreenchido(getContaPagarVO().getFuncionario())) {
					getFacadeFactory().getContaPagarFacade().preencherDadosRemessaContaPagar(getContaPagarVO(),
							getContaPagarVO().getFuncionario().getNumeroBancoRecebimento(),
							getContaPagarVO().getFuncionario().getNumeroAgenciaRecebimento(),
							getContaPagarVO().getFuncionario().getDigitoAgenciaRecebimento(),
							getContaPagarVO().getFuncionario().getContaCorrenteRecebimento(),
							getContaPagarVO().getFuncionario().getDigitoCorrenteRecebimento(), 
							getContaPagarVO().getFuncionario().getChaveEnderecamentoPix() ,
							getContaPagarVO().getFuncionario().getTipoIdentificacaoChavePixEnum(),
							getUsuarioLogado());
				}
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
			}
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public void converterCodigoBarraParaLinhaDigitavel() {
		try {
			if (getContaPagarVO().getTipoLancamentoContaPagar() != null) {
				GeradorDeLinhaDigitavelOuCodigoBarra gd = new GeradorDeLinhaDigitavelOuCodigoBarra();
				gd.geraLinhaDigitavelApartirCodigoBarra(getContaPagarVO() ,true);
			}
		} catch (Exception ex) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), ex.getMessage());
		}
	}

	public void converterLinhaDigitavelParaCodigoBarra() {
		try {
			GeradorDeLinhaDigitavelOuCodigoBarra gd = new GeradorDeLinhaDigitavelOuCodigoBarra();
			if ((getContaPagarVO().getTipoLancamentoContaPagar() != null) && ((getContaPagarVO()
					.getTipoLancamentoContaPagar().isPagamentoContasTributosComCodigoBarra()
					&& Uteis.isAtributoPreenchido(getContaPagarVO().getLinhaDigitavel1())
					&& Uteis.isAtributoPreenchido(getContaPagarVO().getLinhaDigitavel2())
					&& Uteis.isAtributoPreenchido(getContaPagarVO().getLinhaDigitavel3())
					&& Uteis.isAtributoPreenchido(getContaPagarVO().getLinhaDigitavel4()))
					|| (!getContaPagarVO().getTipoLancamentoContaPagar().isPagamentoContasTributosComCodigoBarra()
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
		} catch (Exception ex) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), ex.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaComboPlanoConta() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("codigo", "Código"));
		itens.add(new SelectItem("identificadorPlanoConta", "Identificador Plano Conta"));
		itens.add(new SelectItem("descricao", "Descrição"));
		return itens;
	}

	public List<SelectItem> getComboboxTipoOrigemLancamentoContabilEnum() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem(tipoLancamentoContabilNotaFiscalEntrada.NOTA_FISCAL_ENTRADA_CATEGORIA_PRODUTO,
				"Categoria Produto"));
		itens.add(new SelectItem(tipoLancamentoContabilNotaFiscalEntrada.NOTA_FISCAL_ENTRADA_IMPOSTO, "Imposto"));
		return itens;
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
			if (getCentroResultadoOrigemVO().isCategoriaDespesaInformada()) {
				getFacadeFactory().getCategoriaDespesaFacade().montarListaSelectItemTipoNivelCentroResultadoEnum(
						getCentroResultadoOrigemVO().getCategoriaDespesaVO(),
						getListaSelectItemTipoNivelCentroResultadoEnum());
				if (!getListaSelectItemTipoNivelCentroResultadoEnum().isEmpty() && !Uteis
						.isAtributoPreenchido(getCentroResultadoOrigemVO().getTipoNivelCentroResultadoEnum())) {
					getCentroResultadoOrigemVO().setTipoNivelCentroResultadoEnum(
							(TipoNivelCentroResultadoEnum) getListaSelectItemTipoNivelCentroResultadoEnum().get(0)
									.getValue());
				}
			}
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}

	}

	public List<SelectItem> getListaSelectItemTipoNivelCentroResultadoEnum() {
		listaSelectItemTipoNivelCentroResultadoEnum = Optional.ofNullable(listaSelectItemTipoNivelCentroResultadoEnum)
				.orElse(new ArrayList<>());
		return listaSelectItemTipoNivelCentroResultadoEnum;
	}

	public void setListaSelectItemTipoNivelCentroResultadoEnum(
			List<SelectItem> listaSelectItemTipoNivelCentroResultadoEnum) {
		this.listaSelectItemTipoNivelCentroResultadoEnum = listaSelectItemTipoNivelCentroResultadoEnum;
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
		lancamentoContabilCentroNegocioVO = Optional.ofNullable(lancamentoContabilCentroNegocioVO)
				.orElse(new LancamentoContabilCentroNegocioVO());
		return lancamentoContabilCentroNegocioVO;
	}

	public void setLancamentoContabilCentroNegocioVO(
			LancamentoContabilCentroNegocioVO lancamentoContabilCentroNegocioVO) {
		this.lancamentoContabilCentroNegocioVO = lancamentoContabilCentroNegocioVO;
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
			listaConsultaPlanoConta = new ArrayList<>();
		}
		return listaConsultaPlanoConta;
	}

	public void setListaConsultaPlanoConta(List<PlanoContaVO> listaConsultaPlanoConta) {
		this.listaConsultaPlanoConta = listaConsultaPlanoConta;
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

	public boolean isCampoConsultarUnidadeEnsino() {
		return getControleConsultaOtimizado().getCampoConsulta()
				.equals(NotaFiscalEntradaVO.enumCampoConsultaNotaFiscalEntrada.UNIDADEENSINO.name());
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes de
	 * uma consulta.
	 */
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setControleConsultaOtimizado(new DataModelo());
		getControleConsultaOtimizado()
				.setCampoConsulta(NotaFiscalEntradaVO.enumCampoConsultaNotaFiscalEntrada.NUMERO.name());
		getControleConsultaOtimizado().setDataIni(null);
		getControleConsultaOtimizado().setDataFim(null);
		montarListaSelectItemUnidadeEnsino();
		setMensagemID(MSG_TELA.msg_entre_prmconsulta.name());
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_CONS);
	}

	public NotaFiscalEntradaVO getNotaFiscalEntradaVO() {
		if (notaFiscalEntradaVO == null) {
			notaFiscalEntradaVO = new NotaFiscalEntradaVO();
		}
		return notaFiscalEntradaVO;
	}

	public void setNotaFiscalEntradaVO(NotaFiscalEntradaVO notaFiscalEntradaVO) {
		this.notaFiscalEntradaVO = notaFiscalEntradaVO;
	}

	public NotaFiscalEntradaImpostoVO getNotaFiscalEntradaImpostoVO() {
		if (notaFiscalEntradaImpostoVO == null) {
			notaFiscalEntradaImpostoVO = new NotaFiscalEntradaImpostoVO();
		}
		return notaFiscalEntradaImpostoVO;
	}

	public void setNotaFiscalEntradaImpostoVO(NotaFiscalEntradaImpostoVO notaFiscalEntradaImpostoVO) {
		this.notaFiscalEntradaImpostoVO = notaFiscalEntradaImpostoVO;
	}

	public NotaFiscalEntradaItemVO getNotaFiscalEntradaItemVO() {
		if (notaFiscalEntradaItemVO == null) {
			notaFiscalEntradaItemVO = new NotaFiscalEntradaItemVO();
		}
		return notaFiscalEntradaItemVO;
	}

	public void setNotaFiscalEntradaItemVO(NotaFiscalEntradaItemVO notaFiscalEntradaItemVO) {
		this.notaFiscalEntradaItemVO = notaFiscalEntradaItemVO;
	}

	public NotaFiscalEntradaRecebimentoCompraVO getNotaFiscalEntradaRecebimentoCompraVO() {
		if (notaFiscalEntradaRecebimentoCompraVO == null) {
			notaFiscalEntradaRecebimentoCompraVO = new NotaFiscalEntradaRecebimentoCompraVO();
		}
		return notaFiscalEntradaRecebimentoCompraVO;
	}

	public void setNotaFiscalEntradaRecebimentoCompraVO(
			NotaFiscalEntradaRecebimentoCompraVO notaFiscalEntradaCompraVO) {
		this.notaFiscalEntradaRecebimentoCompraVO = notaFiscalEntradaCompraVO;
	}

	public List<FornecedorVO> getListaConsultaFornecedor() {
		if (listaConsultaFornecedor == null) {
			listaConsultaFornecedor = new ArrayList<>();
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

	public List<NaturezaOperacaoVO> getListaConsultaNaturezaOperacao() {
		if (listaConsultaNaturezaOperacao == null) {
			listaConsultaNaturezaOperacao = new ArrayList<>();
		}
		return listaConsultaNaturezaOperacao;
	}

	public void setListaConsultaNaturezaOperacao(List<NaturezaOperacaoVO> listaConsultaNaturezaOperacao) {
		this.listaConsultaNaturezaOperacao = listaConsultaNaturezaOperacao;
	}

	public String getValorConsultaNaturezaOperacao() {
		if (valorConsultaNaturezaOperacao == null) {
			valorConsultaNaturezaOperacao = "";
		}
		return valorConsultaNaturezaOperacao;
	}

	public void setValorConsultaNaturezaOperacao(String valorConsultaNaturezaOperacao) {
		this.valorConsultaNaturezaOperacao = valorConsultaNaturezaOperacao;
	}

	public String getCampoConsultaNaturezaOperacao() {
		if (campoConsultaNaturezaOperacao == null) {
			campoConsultaNaturezaOperacao = "";
		}
		return campoConsultaNaturezaOperacao;
	}

	public void setCampoConsultaNaturezaOperacao(String campoConsultaNaturezaOperacao) {
		this.campoConsultaNaturezaOperacao = campoConsultaNaturezaOperacao;
	}

	public TipoNaturezaOperacaoEnum getTipoNaturezaOperacaoEnumCampoConsulta() {
		if (tipoNaturezaOperacaoEnumCampoConsulta == null) {
			tipoNaturezaOperacaoEnumCampoConsulta = TipoNaturezaOperacaoEnum.ENTRADA;
		}
		return tipoNaturezaOperacaoEnumCampoConsulta;
	}

	public void setTipoNaturezaOperacaoEnumCampoConsulta(
			TipoNaturezaOperacaoEnum tipoNaturezaOperacaoEnumCampoConsulta) {
		this.tipoNaturezaOperacaoEnumCampoConsulta = tipoNaturezaOperacaoEnumCampoConsulta;
	}

	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList<>();
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public String getCampoConsultaProduto() {
		return campoConsultaProduto;
	}

	public void setCampoConsultaProduto(String campoConsultaProduto) {
		this.campoConsultaProduto = campoConsultaProduto;
	}

	public List<ProdutoServicoVO> getListaConsultaProduto() {
		return listaConsultaProduto;
	}

	public void setListaConsultaProduto(List<ProdutoServicoVO> listaConsultaProduto) {
		this.listaConsultaProduto = listaConsultaProduto;
	}

	public String getValorConsultaProduto() {
		return valorConsultaProduto;
	}

	public void setValorConsultaProduto(String valorConsultaProduto) {
		this.valorConsultaProduto = valorConsultaProduto;
	}

	public ImpostoVO getImpostoFiltroLancamentoContabil() {
		if (impostoFiltroLancamentoContabil == null) {
			impostoFiltroLancamentoContabil = new ImpostoVO();
		}
		return impostoFiltroLancamentoContabil;
	}

	public void setImpostoFiltroLancamentoContabil(ImpostoVO impostoFiltroLancamentoContabil) {
		this.impostoFiltroLancamentoContabil = impostoFiltroLancamentoContabil;
	}

	public CategoriaProdutoVO getCategoriaProdutoFiltroLancamentoContabil() {
		if (categoriaProdutoFiltroLancamentoContabil == null) {
			categoriaProdutoFiltroLancamentoContabil = new CategoriaProdutoVO();
		}
		return categoriaProdutoFiltroLancamentoContabil;
	}

	public void setCategoriaProdutoFiltroLancamentoContabil(
			CategoriaProdutoVO categoriaProdutoFiltroLancamentoContabil) {
		this.categoriaProdutoFiltroLancamentoContabil = categoriaProdutoFiltroLancamentoContabil;
	}

	public List<SelectItem> getListaSelectItemImpostoFiltroLancamentoContabil() {
		if (listaSelectItemImpostoFiltroLancamentoContabil == null) {
			listaSelectItemImpostoFiltroLancamentoContabil = new ArrayList<>();
		}
		return listaSelectItemImpostoFiltroLancamentoContabil;
	}

	public void setListaSelectItemImpostoFiltroLancamentoContabil(
			List<SelectItem> listaSelectItemImpostoFiltroLancamentoContabil) {
		this.listaSelectItemImpostoFiltroLancamentoContabil = listaSelectItemImpostoFiltroLancamentoContabil;
	}

	public List<SelectItem> getListaSelectItemCategoriaProdutoFiltroLancamentoContabil() {
		if (listaSelectItemCategoriaProdutoFiltroLancamentoContabil == null) {
			listaSelectItemCategoriaProdutoFiltroLancamentoContabil = new ArrayList<>();
		}
		return listaSelectItemCategoriaProdutoFiltroLancamentoContabil;
	}

	public void setListaSelectItemCategoriaProdutoFiltroLancamentoContabil(
			List<SelectItem> listaSelectItemCategoriaProdutoFiltroLancamentoContabil) {
		this.listaSelectItemCategoriaProdutoFiltroLancamentoContabil = listaSelectItemCategoriaProdutoFiltroLancamentoContabil;
	}

	public List<SelectItem> getListaSelectItemImposto() {
		if (listaSelectItemImposto == null) {
			listaSelectItemImposto = new ArrayList<>();
		}
		return listaSelectItemImposto;
	}

	public void setListaSelectItemImposto(List<SelectItem> listaSelectItemImposto) {
		this.listaSelectItemImposto = listaSelectItemImposto;
	}

	public ContaPagarVO getContaPagarVO() {
		contaPagarVO = Optional.ofNullable(contaPagarVO).orElse(new ContaPagarVO());
		return contaPagarVO;
	}

	public void setContaPagarVO(ContaPagarVO contaPagarVO) {
		this.contaPagarVO = contaPagarVO;
	}

	public List<CategoriaDespesaVO> getListaConsultaCentroDespesa() {
		listaConsultaCentroDespesa = Optional.ofNullable(listaConsultaCentroDespesa).orElse(new ArrayList<>());
		return listaConsultaCentroDespesa;
	}

	public void setListaConsultaCentroDespesa(List<CategoriaDespesaVO> listaConsultaCentroDespesa) {
		this.listaConsultaCentroDespesa = listaConsultaCentroDespesa;
	}

	public String getValorConsultaCentroDespesa() {
		valorConsultaCentroDespesa = Optional.ofNullable(valorConsultaCentroDespesa).orElse("");
		return valorConsultaCentroDespesa;
	}

	public void setValorConsultaCentroDespesa(String valorConsultaCentroDespesa) {
		this.valorConsultaCentroDespesa = valorConsultaCentroDespesa;
	}

	public String getCampoConsultaCentroDespesa() {
		campoConsultaCentroDespesa = Optional.ofNullable(campoConsultaCentroDespesa).orElse("");
		return campoConsultaCentroDespesa;
	}

	public void setCampoConsultaCentroDespesa(String campoConsultaCentroDespesa) {
		this.campoConsultaCentroDespesa = campoConsultaCentroDespesa;
	}

	public List<SelectItem> getListaSelectItemBancoFavorecido() {
		listaSelectItemBancoFavorecido = Optional.ofNullable(listaSelectItemBancoFavorecido).orElse(new ArrayList<>());
		return listaSelectItemBancoFavorecido;
	}

	public void setListaSelectItemBancoFavorecido(List<SelectItem> listaSelectItemBancoFavorecido) {
		this.listaSelectItemBancoFavorecido = listaSelectItemBancoFavorecido;
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
					if (tipoLancamentoContaPagarEnum.isTipoLancamentoPorNrBanco(layout.getNumeroBanco()) ||
							(tipoLancamentoContaPagarEnum.isTipoLancamentoPorNrBanco("033") 	&& layout.getNumeroBanco().equals("756"))) {
						if (comboTipoLancamentoContaPagar.isEmpty()	&& !Uteis.isAtributoPreenchido(getContaPagarVO().getTipoLancamentoContaPagar())) {
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

	public List<SelectItem> getListaSelectItemUnidadeEnsinoCategoriaDespesa() {
		listaSelectItemUnidadeEnsinoCategoriaDespesa = Optional.ofNullable(listaSelectItemUnidadeEnsinoCategoriaDespesa)
				.orElse(new ArrayList<>());
		return listaSelectItemUnidadeEnsinoCategoriaDespesa;
	}

	public void setListaSelectItemUnidadeEnsinoCategoriaDespesa(
			List<SelectItem> listaSelectItemUnidadeEnsinoCategoriaDespesa) {
		this.listaSelectItemUnidadeEnsinoCategoriaDespesa = listaSelectItemUnidadeEnsinoCategoriaDespesa;
	}

	public String getValorConsultaFuncionarioCentroCusto() {
		valorConsultaFuncionarioCentroCusto = Optional.ofNullable(valorConsultaFuncionarioCentroCusto).orElse("");
		return valorConsultaFuncionarioCentroCusto;
	}

	public void setValorConsultaFuncionarioCentroCusto(String valorConsultaFuncionarioCentroCusto) {
		this.valorConsultaFuncionarioCentroCusto = valorConsultaFuncionarioCentroCusto;
	}

	public String getCampoConsultaFuncionarioCentroCusto() {
		campoConsultaFuncionarioCentroCusto = Optional.ofNullable(campoConsultaFuncionarioCentroCusto).orElse("");
		return campoConsultaFuncionarioCentroCusto;
	}

	public void setCampoConsultaFuncionarioCentroCusto(String campoConsultaFuncionarioCentroCusto) {
		this.campoConsultaFuncionarioCentroCusto = campoConsultaFuncionarioCentroCusto;
	}

	public List<FuncionarioCargoVO> getListaConsultaFuncionarioCentroCusto() {
		listaConsultaFuncionarioCentroCusto = Optional.ofNullable(listaConsultaFuncionarioCentroCusto)
				.orElse(new ArrayList<>());
		return listaConsultaFuncionarioCentroCusto;
	}

	public void setListaConsultaFuncionarioCentroCusto(List<FuncionarioCargoVO> listaConsultaFuncionarioCentroCusto) {
		this.listaConsultaFuncionarioCentroCusto = listaConsultaFuncionarioCentroCusto;
	}

	public String getCampoConsultaTurma() {
		campoConsultaTurma = Optional.ofNullable(campoConsultaTurma).orElse("");
		return campoConsultaTurma;
	}

	public void setCampoConsultaTurma(String campoConsultaTurma) {
		this.campoConsultaTurma = campoConsultaTurma;
	}

	public String getValorConsultaTurma() {
		valorConsultaTurma = Optional.ofNullable(valorConsultaTurma).orElse("");
		return valorConsultaTurma;
	}

	public void setValorConsultaTurma(String valorConsultaTurma) {
		this.valorConsultaTurma = valorConsultaTurma;
	}

	public List<TurmaVO> getListaConsultaTurma() {
		listaConsultaTurma = Optional.ofNullable(listaConsultaTurma).orElse(new ArrayList<>());
		return listaConsultaTurma;
	}

	public void setListaConsultaTurma(List<TurmaVO> listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
	}

	public String getCampoConsultaCurso() {
		campoConsultaCurso = Optional.ofNullable(campoConsultaCurso).orElse("");
		return campoConsultaCurso;
	}

	public void setCampoConsultaCurso(String campoConsultaCurso) {
		this.campoConsultaCurso = campoConsultaCurso;
	}

	public String getValorConsultaCurso() {
		valorConsultaCurso = Optional.ofNullable(valorConsultaCurso).orElse("");
		return valorConsultaCurso;
	}

	public void setValorConsultaCurso(String valorConsultaCurso) {
		this.valorConsultaCurso = valorConsultaCurso;
	}

	public List<CursoVO> getListaConsultaCurso() {
		listaConsultaCurso = Optional.ofNullable(listaConsultaCurso).orElse(new ArrayList<>());
		return listaConsultaCurso;
	}

	public void setListaConsultaCurso(List<CursoVO> listaConsultaCurso) {
		this.listaConsultaCurso = listaConsultaCurso;
	}

	public String getCampoConsultaCursoTurno() {
		campoConsultaCursoTurno = Optional.ofNullable(campoConsultaCursoTurno).orElse("");
		return campoConsultaCursoTurno;
	}

	public void setCampoConsultaCursoTurno(String campoConsultaCursoTurno) {
		this.campoConsultaCursoTurno = campoConsultaCursoTurno;
	}

	public String getValorConsultaCursoTurno() {
		valorConsultaCursoTurno = Optional.ofNullable(valorConsultaCursoTurno).orElse("");
		return valorConsultaCursoTurno;
	}

	public void setValorConsultaCursoTurno(String valorConsultaCursoTurno) {
		this.valorConsultaCursoTurno = valorConsultaCursoTurno;
	}

	public List<UnidadeEnsinoCursoVO> getListaConsultaCursoTurno() {
		listaConsultaCursoTurno = Optional.ofNullable(listaConsultaCursoTurno).orElse(new ArrayList<>());
		return listaConsultaCursoTurno;
	}

	public void setListaConsultaCursoTurno(List<UnidadeEnsinoCursoVO> listaConsultaCursoTurno) {
		this.listaConsultaCursoTurno = listaConsultaCursoTurno;
	}

	public String getCampoConsultaDepartamento() {
		campoConsultaDepartamento = Optional.ofNullable(campoConsultaDepartamento).orElse("");
		return campoConsultaDepartamento;
	}

	public void setCampoConsultaDepartamento(String campoConsultaDepartamento) {
		this.campoConsultaDepartamento = campoConsultaDepartamento;
	}

	public String getValorConsultaDepartamento() {
		valorConsultaDepartamento = Optional.ofNullable(valorConsultaDepartamento).orElse("");
		return valorConsultaDepartamento;
	}

	public void setValorConsultaDepartamento(String valorConsultaDepartamento) {
		this.valorConsultaDepartamento = valorConsultaDepartamento;
	}

	public List<DepartamentoVO> getListaConsultaDepartamento() {
		listaConsultaDepartamento = Optional.ofNullable(listaConsultaDepartamento).orElse(new ArrayList<>());
		return listaConsultaDepartamento;
	}

	public void setListaConsultaDepartamento(List<DepartamentoVO> listaConsultaDepartamento) {
		this.listaConsultaDepartamento = listaConsultaDepartamento;
	}

	public DataModelo getCentroResultadoDataModelo() {
		centroResultadoDataModelo = Optional.ofNullable(centroResultadoDataModelo).orElse(new DataModelo());
		return centroResultadoDataModelo;
	}

	public void setCentroResultadoDataModelo(DataModelo centroResultadoDataModelo) {
		this.centroResultadoDataModelo = centroResultadoDataModelo;
	}

	public String getModalAptoAdiantamentosDisponiveisDistribuicao() {
		modalAptoAdiantamentosDisponiveisDistribuicao = Optional.ofNullable(modalAptoAdiantamentosDisponiveisDistribuicao).orElse("");		
		return modalAptoAdiantamentosDisponiveisDistribuicao;
	}

	public void setModalAptoAdiantamentosDisponiveisDistribuicao(String modalAptoAdiantamentosDisponiveisDistribuicao) {
		this.modalAptoAdiantamentosDisponiveisDistribuicao = modalAptoAdiantamentosDisponiveisDistribuicao;
	}

	public boolean isPermiteAlterarCentroResultado() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(
					PerfilAcessoPermissaoFinanceiroEnum.PERMITE_ALTERAR_CENTRO_RESULTADO_CONTA_PAGAR,
					getUsuarioLogado());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean isCentroResultadoAdministrativo() {
		return centroResultadoAdministrativo;
	}

	public void setCentroResultadoAdministrativo(boolean centroResultadoAdministrativo) {
		this.centroResultadoAdministrativo = centroResultadoAdministrativo;
	}

	public CentroResultadoOrigemVO getCentroResultadoOrigemVO() {
		centroResultadoOrigemVO = Optional.ofNullable(centroResultadoOrigemVO).orElse(new CentroResultadoOrigemVO());
		return centroResultadoOrigemVO;
	}

	public void setCentroResultadoOrigemVO(CentroResultadoOrigemVO centroResultadoOrigemVO) {
		this.centroResultadoOrigemVO = centroResultadoOrigemVO;
	}

	public void verificarPermissaoLancamentoContabil() {
		try {
			if (!getNotaFiscalEntradaVO().isLancamentoContabil()) {
				getNotaFiscalEntradaVO().setLancamentoContabil(getFacadeFactory().getConfiguracaoContabilFacade()
						.consultaSeExisteConfiguracaoContabilPorCodigoUnidadeEnsino(
								getNotaFiscalEntradaVO().getUnidadeEnsinoVO().getCodigo(), getUsuarioLogado()));
			}
		} catch (Exception e) {
			getNotaFiscalEntradaVO().setLancamentoContabil(false);
		}
	}

	public List consultarBancoPorNome(String prm) throws Exception {
		List lista = getFacadeFactory().getBancoFacade().consultarPorNome(prm, false,
				Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		return lista;
	}

	public List getListaSelectItemBanco() throws Exception {
		List resultadoConsulta = consultarBancoPorNome("");
		Iterator i = resultadoConsulta.iterator();
		List objs = new ArrayList(0);
		objs.add(new SelectItem(0, ""));
		while (i.hasNext()) {
			BancoVO obj = (BancoVO) i.next();
			BancoEnum banco = BancoEnum.getEnum("CNAB240", obj.getNrBanco());
			if (banco != null && banco.getPossuiRemessaContaPagar()) {
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
			}
		}
		return objs;
	}

	public Boolean getApresentarBotaoLiberarBloqueio() {
		return this.getNotaFiscalEntradaVO().getApresentarBotaoLiberarBloqueioFechamentoMes();
	}

	public void liberarRegistroCompetenciaFechada() {
		try {
			this.getNotaFiscalEntradaVO().setBloqueioPorFechamentoMesLiberado(Boolean.TRUE);
			for (ContaPagarVO contaPagar : this.getNotaFiscalEntradaVO().getListaContaPagar()) {
				contaPagar.liberarVerificacaoBloqueioFechamentoMes();
			}

			FechamentoMesHistoricoModificacaoVO historico = getFacadeFactory()
					.getFechamentoMesHistoricoModificacaoFacade()
					.gerarNovoHistoricoModificacao(this.getNotaFiscalEntradaVO().getFechamentoMesVOBloqueio(),
							getUsuarioLogado(), TipoOrigemHistoricoBloqueioEnum.NFENTRADA,
							this.getNotaFiscalEntradaVO().getDescricaoBloqueio(),
							this.getNotaFiscalEntradaVO().toString());
			getFacadeFactory().getFechamentoMesHistoricoModificacaoFacade().incluir(historico, getUsuarioLogado());
			setMensagemID("msg_registro_liberado_mes");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			this.getNotaFiscalEntradaVO().setBloqueioPorFechamentoMesLiberado(Boolean.FALSE);
		}
	}

	public void verificarPermissaoLiberarBloqueioCompetencia() {
		try {
			UsuarioVO usuarioVerif = ControleAcesso.verificarLoginUsuario(
					this.getUsernameLiberacaoBloqueioPorFechamentoMes(),
					this.getSenhaLiberacaoBloqueioPorFechamentoMes(), true, Uteis.NIVELMONTARDADOS_TODOS);
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico(
					"FuncionarioMes_liberarBloqueioIncluirAlterarNFEntrada", usuarioVerif);
			liberarRegistroCompetenciaFechada();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void consultarFornecedorPorCnpjOuCpf() throws Exception {
		try {
			FornecedorVO objFornecedor = getFacadeFactory().getFornecedorFacade().consultarPorCnpjOuCpf(getCnpjOuCpf(),
					false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			if (objFornecedor.getCnpjOuCfp_Apresentar().equals("")) {
				throw new Exception(
						"Fornecedor de CNPJ/CPF " + getNotaFiscalEntradaVO().getFornecedorVO().getCnpjOuCfp_Apresentar()
								+ " não encontrado. Verifique se o número do CNPJ/CPF está correto.");
			}
			getNotaFiscalEntradaVO().setFornecedorVO(objFornecedor);
			getContaPagarVO().setFornecedor(objFornecedor);

			getNotaFiscalEntradaVO().limparCamposLista();
			getFacadeFactory().getNotaFiscalEntradaFacade()
					.preencherNotaFiscalEntradaCompraPorFornecedor(getNotaFiscalEntradaVO(), getUsuarioLogado());
			setMensagemDetalhada("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			getNotaFiscalEntradaVO().setFornecedorVO(new FornecedorVO());
			setCnpjOuCpf("");
		}
	}

	public void consultarNaturezaOperacaoPorCodigoIdentificador() throws Exception {
		try {

			NaturezaOperacaoVO objNaturezaOperacao = getFacadeFactory().getNaturezaOperacaoFacade()
					.consultarPorCodigoNaturezaOperacao(
							getNotaFiscalEntradaVO().getNaturezaOperacaoVO().getCodigoNaturezaOperacao(), false,
							Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			if (!Uteis.isAtributoPreenchido(objNaturezaOperacao.getCodigo())) {
				throw new Exception("Natureza Operação "
						+ getNotaFiscalEntradaVO().getNaturezaOperacaoVO().getCodigoNaturezaOperacao()
						+ " não encontrado. Verifique se o número está correto.");
			}
			getNotaFiscalEntradaVO().setNaturezaOperacaoVO(objNaturezaOperacao);
			setMensagemDetalhada("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			getNotaFiscalEntradaVO().setNaturezaOperacaoVO(new NaturezaOperacaoVO());
		}
	}

	public String getCnpjOuCpf() {
		if (cnpjOuCpf == null) {
			cnpjOuCpf = "";
		}
		return cnpjOuCpf;
	}

	public void setCnpjOuCpf(String cnpjOuCpf) {
		this.cnpjOuCpf = cnpjOuCpf;

		if (this.cnpjOuCpf.length() > 14) {
			getNotaFiscalEntradaVO().getFornecedorVO().setCNPJ(getCnpjOuCpf());
			getNotaFiscalEntradaVO().getFornecedorVO().setCPF("");
		} else {
			getNotaFiscalEntradaVO().getFornecedorVO().setCPF(getCnpjOuCpf());
			getNotaFiscalEntradaVO().getFornecedorVO().setCNPJ("");
		}
	}

	public void limparFornecedor() {
		getNotaFiscalEntradaVO().setFornecedorVO(new FornecedorVO());
		getContaPagarVO().setFornecedor(new FornecedorVO());
		setCnpjOuCpf("");
		getNotaFiscalEntradaVO().limparCamposLista();
	}

	public void limparNaturezaOperacao() {
		getNotaFiscalEntradaVO().setNaturezaOperacaoVO(new NaturezaOperacaoVO());
	}

	public Boolean getPermitirNotaFiscalEntradaSemOrdemCompra() {
		try {
			return ControleAcesso.verificarPermissaoFuncionalidadeUsuario("PermitirNotaFiscalEntradaSemOrdemCompra",
					getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return false;
		}
	}

	public void cancelarEdicaoContaPagar() {
		setContaPagarVO(new ContaPagarVO());
	}

	public Date getDataEntradaInicio() {
		return dataEntradaInicio;
	}

	public void setDataEntradaInicio(Date dataEntradaInicio) {
		this.dataEntradaInicio = dataEntradaInicio;
	}

	public Date getDataEntradaFim() {
		return dataEntradaFim;
	}

	public void setDataEntradaFim(Date dataEntradaFim) {
		this.dataEntradaFim = dataEntradaFim;
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

	public Double getTotalNotaFiscalEntrada() {
		if (totalNotaFiscalEntrada == null) {
			totalNotaFiscalEntrada = 0.0;
		}
		return totalNotaFiscalEntrada;
	}

	public void setTotalNotaFiscalEntrada(Double totalNotaFiscalEntrada) {
		this.totalNotaFiscalEntrada = totalNotaFiscalEntrada;
	}
    
	
	public void removerTodosCentroResultadoOrigemNotaFiscalEntradaItem() {
		try {
			getNotaFiscalEntradaItemVO().getListaCentroResultadoOrigemVOs().clear();

			setMensagemID(MSG_TELA.msg_dados_excluidos.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	
	
	public void upLoadArquivo(FileUploadEvent upload) {	
		try {
			
			getFacadeFactory().getNotaFiscalEntradaItemFacade().adicionarCentroResultadoOrigemPorArquivoImportacao(upload, getNotaFiscalEntradaVO(), getNotaFiscalEntradaItemVO(), getUsuarioLogado());
			setMensagemID(MSG_TELA.msg_dados_adicionados.name());
		} catch (ConsistirException e) {
			setListaMensagemErro(e.getListaMensagemErro());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);

		} finally {
			setOncompleteModal("RichFaces.$('panelImportarCentroResultado').hide()");
		}
		
	}

	public void adicionarCentroResultadoOrigemItemNotaFiscalEntrada(CentroResultadoOrigemVO centroResultadoOrigemVO) {
    	getNotaFiscalEntradaItemVO().getListaCentroResultadoOrigemVOs().add(centroResultadoOrigemVO);        
        getFacadeFactory().getNotaFiscalEntradaFacade().adicionarCentroResultadoOrigemNotaFiscalEntradaItem(getNotaFiscalEntradaVO(), getNotaFiscalEntradaItemVO(), centroResultadoOrigemVO, getUsuarioLogado());
    }
	
	public boolean getBloquearSelecaoUnidadeEnsino() {
		try {
			return Uteis.isAtributoPreenchido(getNotaFiscalEntradaVO().getListaNotaFiscalEntradaRecebimentoCompra());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return false;
		}
	}
	

	public void addCentroResultadoOrigemConformeRateioCategoriaDespesa() {
		try {			
			getFacadeFactory().getNotaFiscalEntradaFacade().adicionarCentroResultadoOrigemNotaFiscalEntradaItemPorRateioCategoriaDespesa(getNotaFiscalEntradaVO(), getNotaFiscalEntradaItemVO(), getCentroResultadoOrigemVO(), getUsuarioLogado());
			setCentroResultadoOrigemVO(new CentroResultadoOrigemVO());
			setMensagemID(MSG_TELA.msg_dados_adicionados.name());
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada(MSG_TELA.msg_erro.name(), e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	public List<ProdutoServicoVO> getListaProdutoServico() {
		if(listaProdutoServico == null) {
			listaProdutoServico = new ArrayList<ProdutoServicoVO>();
		}
		return listaProdutoServico;
	}

	public void setListaProdutoServico(List<ProdutoServicoVO> listaProdutoServico) {
		this.listaProdutoServico = listaProdutoServico;
	}

	public String getValorConsultaProdutoServico() {
		if(valorConsultaProdutoServico == null) {
			valorConsultaProdutoServico = "";
		}
		return valorConsultaProdutoServico;
	}

	public void setValorConsultaProdutoServico(String valorConsultaProdutoServico) {
		this.valorConsultaProdutoServico = valorConsultaProdutoServico;
	}
	
	public String getValueContaPagar() {
		return !getContaPagarVO().isEdicaoManual() ? "Adicionar Conta Pagar":"Editar Conta Pagar";
	}
	
	public String getStyleClassContaPagar() {
		return !getContaPagarVO().isEdicaoManual() ? "btn btn-primary":"btn btn-default";
	}
	
	public String getIconContaPagar() {
		return !getContaPagarVO().isEdicaoManual() ? "#{msg_botoes.btn_icon_adicionar_embutido}":"#{msg_botoes.btn_icon_editar_embutido}";
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
