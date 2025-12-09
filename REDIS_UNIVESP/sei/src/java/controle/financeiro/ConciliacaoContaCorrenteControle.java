package controle.financeiro;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.richfaces.event.DataScrollEvent;
import org.richfaces.event.DropEvent;
import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import controle.arquitetura.FilterFactory;
import controle.arquitetura.SelectItemOrdemValor;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.TotalizadorPorFormaPagamentoVO;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoFinanceiroEnum;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.ead.enumeradores.SituacaoEnum;
import negocio.comuns.financeiro.BancoVO;
import negocio.comuns.financeiro.CategoriaDespesaVO;
import negocio.comuns.financeiro.CentroReceitaVO;
import negocio.comuns.financeiro.CentroResultadoOrigemVO;
import negocio.comuns.financeiro.CentroResultadoVO;
import negocio.comuns.financeiro.ConciliacaoContaCorrenteDiaExtratoVO;
import negocio.comuns.financeiro.ConciliacaoContaCorrenteDiaVO;
import negocio.comuns.financeiro.ConciliacaoContaCorrenteVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroCartaoVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ConfiguracaoRecebimentoCartaoOnlineVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ContaPagarVO;
import negocio.comuns.financeiro.ContaReceberNegociacaoRecebimentoVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.ExtratoContaCorrenteVO;
import negocio.comuns.financeiro.FormaPagamentoNegociacaoPagamentoVO;
import negocio.comuns.financeiro.FormaPagamentoNegociacaoRecebimentoVO;
import negocio.comuns.financeiro.MovimentacaoFinanceiraItemVO;
import negocio.comuns.financeiro.MovimentacaoFinanceiraVO;
import negocio.comuns.financeiro.NegociacaoPagamentoVO;
import negocio.comuns.financeiro.NegociacaoRecebimentoVO;
import negocio.comuns.financeiro.ParametrizarOperacoesAutomaticasConciliacaoItemVO;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.financeiro.enumerador.OrigemExtratoContaCorrenteEnum;
import negocio.comuns.financeiro.enumerador.TipoFinanciamentoEnum;
import negocio.comuns.financeiro.enumerador.TipoNivelCentroResultadoEnum;
import negocio.comuns.financeiro.enumerador.TipoSacadoExtratoContaCorrenteEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.SituacaoMovimentacaoFinanceiraEnum;
import negocio.comuns.utilitarias.dominios.TipoFormaPagamento;
import negocio.comuns.utilitarias.dominios.TipoMovimentacaoFinanceira;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.comuns.utilitarias.dominios.TipoSacado;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.financeiro.ConciliacaoContaCorrente;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.controle.arquitetura.SuperControleRelatorio;

@Controller("ConciliacaoContaCorrenteControle")
@Scope("viewScope")
@Lazy
public class ConciliacaoContaCorrenteControle extends SuperControleRelatorio {

	/***
	 * 
	 */
	private static final long serialVersionUID = 2092329305770889555L;

	private ConciliacaoContaCorrenteVO conciliacaoContaCorrenteVO;
	private ParametrizarOperacoesAutomaticasConciliacaoItemVO parametrizarOperacoesAutomaticasConciliacaoItem;
	private ConciliacaoContaCorrenteDiaVO conciliacaoContaCorrenteDiaVO;
	private ConciliacaoContaCorrenteDiaExtratoVO conciliacaoContaCorrenteDiaExtratoVO;

	private OrigemExtratoContaCorrenteEnum origemExtratoContaCorrente;
	private TipoFormaPagamento tipoFormaPagamento;

	private List<SelectItem>  listaSelectItemFormaPagamento;
	private List<SelectItem>  listaSelectItemContaCorrente;
	private List<SelectItem>  listaSelectItemContaCorrenteDestino;
	private List<SelectItem>  listaSelectItemUnidadeEnsino;

	private List<CentroReceitaVO> listaConsultaCentroReceita;
	private String valorConsultaCentroReceita;
	private String campoConsultaCentroReceita;

	private String valorConsultaParceiro;
	private String campoConsultaParceiro;
	private List<ParceiroVO> listaConsultaParceiro;

	private List<FornecedorVO> listaConsultaFornecedor;
	private String valorConsultaFornecedor;
	private String campoConsultaFornecedor;

	private List<FuncionarioVO> listaConsultaFuncionario;
	private String valorConsultaFuncionario;
	private String campoConsultaFuncionario;

	private List listaConsultaBanco;
	private String valorConsultaBanco;
	private String campoConsultaBanco;

	private String tituloPanelLocalizacaoDocumentoSei;
	private FormaPagamentoVO formaPagamentoVO;
	/**
	 * Filtros utilizado para consulta de Conta Receber
	 */
	private ContaReceberVO contaReceber;
	private Boolean consDataCompetencia;
	private int valorConsultaUnidadeEnsino;
	private String ano;
	private String semestre;
	private String tipoOrigem;
	private String valorConsultaReceberRecebidoNegociado;
	private FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO;
	private NegociacaoRecebimentoVO negociacaoRecebimentoVO;
	private List<SelectItem> listaSelectItemQuantidadeParcelas;
	/**
	 * Termino Filtros utilizado para consulta de Conta Receber
	 */

	/**
	 * Filtros utilizado para consulta de Conta Pagar
	 */
	private ContaPagarVO contaPagar;
	private Boolean filtrarTodoPeriodo;
	private String valorConsultaSituacaoFinanceiraDaConta;
	private Boolean filtrarDataFactorGerador;
	private FormaPagamentoNegociacaoPagamentoVO formaPagamentoNegociacaoPagamentoVO;
	private NegociacaoPagamentoVO negociacaoPagamentoVO;
	private List<SelectItem> listaSelectItemConfiguracaoFinanceiroCartao;
	private List<SelectItem> listaSelectItemUnidadeEnsinoCategoriaDespesa;
	private List<SelectItem> listaSelectItemTipoNivelCentroResultadoEnum;
	private ConfiguracaoFinanceiroCartaoVO configuracaoFinanceiroCartaoVO;
	private ConfiguracaoRecebimentoCartaoOnlineVO configuracaoRecebimentoCartaoOnlineVO;

	private String valorConsultaCentroDespesa;
	private String campoConsultaCentroDespesa;
	private List<CategoriaDespesaVO> listaConsultaCentroDespesa;

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
	
	public List<SelectItem> tipoConsultaComboAluno;
    private String valorConsultaAluno;
    private String campoConsultaAluno;
    private List<MatriculaVO> listaConsultaAluno;

	/**
	 * Termino Filtros utilizado para consulta de Conta Pagar
	 */

	/**
	 * Filtros utilizado para consulta de Movimentacao Financeira
	 */
	private MovimentacaoFinanceiraVO movimentacaoFinanceiraVO;

	/**
	 * Termino Filtros utilizado para consulta de Movimentacao Financeira
	 */

	private String manterPanelPagamentoRecebimentoConta;
	private String abaParametrizarOperacoesAutomaticasConciliacao;
	private String  apresentarModalOperacaoEstorno;
	private ExtratoContaCorrenteVO extratoContaCorrenteEstorno;
	private Date filtroListaDataConciliacao;
	private String campoNavegacaoEstornoConciliacaoExtratoDia;
	private String apresentarModalLocalizacaoDocumentoSei;
	private String  apresentarModalValoresAproximado;
	private Date dataCredito;
	private Boolean permitirIncluirConciliacaoContaCorrente;
	private Boolean permitirAbrirConciliacaoContaCorrente;

	public ConciliacaoContaCorrenteControle() {
		setMensagemID("msg_entre_prmconsulta", Uteis.ALERTA);
		setFazerDownload(false);
		verificarPermissaoIncluirConciliacaoContaCorrente();
		verificarPermissaoAbrirConciliacaoContaCorrente();
	}

	@PostConstruct
	public void realizarCarregamentoConciliacaoVindoTelaEstorno() {
		ConciliacaoContaCorrenteVO obj = (ConciliacaoContaCorrenteVO) context().getExternalContext().getSessionMap().get("conciliacaoBancaria");
		if (obj != null && !obj.getNomeArquivo().isEmpty()) {
			try {
				setConciliacaoContaCorrenteVO(getFacadeFactory().getConciliacaoContaCorrenteFacade().consultarPorChavePrimaria(obj.getCodigo(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
				getFacadeFactory().getConciliacaoContaCorrenteFacade().realizarCalculoTotalizadoresDias(getConciliacaoContaCorrenteVO(), getUsuarioLogado());
				setMensagemID("msg_dados_editar", Uteis.ALERTA);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			} finally {
				context().getExternalContext().getSessionMap().remove("conciliacaoBancaria");
			}
		}
	}

	public String novo() {
		removerObjetoMemoria(this);
		setFiltroListaDataConciliacao(null);
		setConciliacaoContaCorrenteVO(new ConciliacaoContaCorrenteVO());
		getConciliacaoContaCorrenteVO().setResponsavel(getUsuarioLogadoClone());
		getConciliacaoContaCorrenteVO().setDataInicioSei(new Date());
		getConciliacaoContaCorrenteVO().setDataFimSei(new Date());
		montarListaSelectItemUnidadeEnsino();
		setMensagemID("msg_entre_dados");

		return Uteis.getCaminhoRedirecionamentoNavegacao("conciliacaoContaCorrenteForm.xhtml");
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe <code>Agencia</code> para alteração. O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa disponibilizá-lo para edição.
	 */
	public String editar() {
		try {
			ConciliacaoContaCorrenteVO obj = (ConciliacaoContaCorrenteVO) context().getExternalContext().getRequestMap().get("conciliacaoContaCorrenteItens");
			setConciliacaoContaCorrenteVO(getFacadeFactory().getConciliacaoContaCorrenteFacade().consultarPorChavePrimaria(obj.getCodigo(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			getFacadeFactory().getConciliacaoContaCorrenteFacade().realizarCalculoTotalizadoresDias(getConciliacaoContaCorrenteVO(), getUsuarioLogado());
			verificarPermissaoIncluirConciliacaoContaCorrente();
			verificarPermissaoAbrirConciliacaoContaCorrente();
			setMensagemID("msg_dados_editar");
			return Uteis.getCaminhoRedirecionamentoNavegacao("conciliacaoContaCorrenteForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("conciliacaoContaCorrenteCons.xhtml");
		}
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>Agencia</code>. Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de erro.
	 */
	public String gravar() {
		try {
			getFacadeFactory().getConciliacaoContaCorrenteFacade().persistir(getConciliacaoContaCorrenteVO(), true,  getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(), getUsuarioLogado());
			getConciliacaoContaCorrenteVO().getListaConciliacaoContaCorrenteDia().parallelStream().forEach(p -> p.getListaConciliacaoContaCorrenteExtratoExcluir().clear());
			setMensagemID("msg_dados_gravados");
			return "editar";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "editar";
		}
	}

	public String finalizarConciliacao() {
		try {
			getFacadeFactory().getConciliacaoContaCorrenteFacade().finalizarConciliacao(getConciliacaoContaCorrenteVO(), true, getUsuarioLogado());
			setMensagemID("msg_dados_gravados");
			return "editar";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "editar";
		}
	}
	
	public String abrirConciliacao() {
		try {
			getFacadeFactory().getConciliacaoContaCorrenteFacade().abrirConciliacao(getConciliacaoContaCorrenteVO(), true, getUsuarioLogado());
			setMensagemID("msg_dados_gravados");
			return "editar";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "editar";
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP AgenciaCons.jsp. Define o tipo de consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
	 */
	public String consultar() {
		try {
			super.consultar();
			List<ConciliacaoContaCorrenteVO> objs = new ArrayList<>(0);
			if (getControleConsulta().getCampoConsulta().equals("codigo")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getConciliacaoContaCorrenteFacade().consultaRapidaPorCodigo(new Integer(valorInt), true, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("dataGeracao")) {
				objs = getFacadeFactory().getConciliacaoContaCorrenteFacade().consultaRapidaPorDataGeracao(getControleConsulta().getDataIni(), getControleConsulta().getDataFim(), true, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("contaCorrente")) {
				objs = getFacadeFactory().getConciliacaoContaCorrenteFacade().consultaRapidaPorContaCorrente(getControleConsulta().getValorConsulta(), true, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("responsavel")) {
				objs = getFacadeFactory().getConciliacaoContaCorrenteFacade().consultaRapidaPorResponsavel(getControleConsulta().getValorConsulta(), true, getUsuarioLogado());
			}
			setListaConsulta(objs);
			verificarPermissaoIncluirConciliacaoContaCorrente();
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("conciliacaoContaCorrenteCons.xhtml");
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe <code>AgenciaVO</code> Após a exclusão ela automaticamente aciona a rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			getFacadeFactory().getConciliacaoContaCorrenteFacade().excluir(getConciliacaoContaCorrenteVO(), true, getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(), getUsuarioLogado());
			setFiltroListaDataConciliacao(null);
			setConciliacaoContaCorrenteVO(new ConciliacaoContaCorrenteVO());
			getConciliacaoContaCorrenteVO().setResponsavel(getUsuarioLogadoClone());
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("conciliacaoContaCorrenteForm.xhtml");
	}

	public void selecionarConciliacaoContaCorrenteDia() {
		try {
			ConciliacaoContaCorrenteDiaVO obj = (ConciliacaoContaCorrenteDiaVO) context().getExternalContext().getRequestMap().get("conciliacaoDia");
			getConciliacaoContaCorrenteVO().setConciliacaoDiaSelecionada(obj);
			setFiltroListaDataConciliacao(obj.getData());
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

	}

	public void realizarCorrecaoParaRegistroDuplicadosConciliacaoExtratoContaCorrente() {
		try {
			getFacadeFactory().getConciliacaoContaCorrenteFacade().realizarCorrecaoParaRegistroDuplicadosConciliacaoExtratoContaCorrente(getConciliacaoContaCorrenteVO(), getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(), getUsuarioLogado());
			setMensagemID("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void realizarAtualizacaoConciliacaoExtratoContaCorrente() {
		try {
			List<ConciliacaoContaCorrenteDiaExtratoVO> listaDuplicidade = getFacadeFactory().getConciliacaoContaCorrenteFacade().validarUnicidadeParaConciliacaoContaCorrenteExtratoDia(getConciliacaoContaCorrenteVO());
			if (Uteis.isAtributoPreenchido(listaDuplicidade)) {
				setApresentarModalValoresAproximado("RichFaces.$('panelAtencaoRegistroDuplicidade').show();");
			} else {
				setApresentarModalValoresAproximado("");
				getFacadeFactory().getConciliacaoContaCorrenteFacade().realizarAtualizacaoConciliacaoExtratoContaCorrente(getConciliacaoContaCorrenteVO(), getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(), getUsuarioLogado());
				limparFiltroListaDataTransacao();
				setMensagemID("");
			}
		} catch (Exception e) {
			setApresentarModalValoresAproximado("");
			if(e.getMessage().contains("foi conciliado")){
				getConciliacaoContaCorrenteVO().getListaConciliacaoContaCorrenteDia().clear();	
			}
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void realizarConciliacaoParametrizada() {
		try {
			getFacadeFactory().getParametrizarOperacoesAutomaticasConciliacaoItemInterfaceFacade().persistir(getConciliacaoContaCorrenteVO(), true, getUsuarioLogado());
			getFacadeFactory().getConciliacaoContaCorrenteFacade().realizarCriacaoDeNovoDocumentosParaConciliacaoAutomatica(getConciliacaoContaCorrenteVO(), getConfiguracaoFinanceiroPadraoSistema(),  getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(), getUsuarioLogado());
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void realizarLinkConciliacaoExtratoContaCorrente(DropEvent dropEvent) {
		ConciliacaoContaCorrenteDiaExtratoVO conciliacaoExtrato = null;
		ConciliacaoContaCorrenteDiaExtratoVO conciliacaoExtratoSei = null;
		try {

			if (dropEvent.getDragValue() instanceof ConciliacaoContaCorrenteDiaExtratoVO && dropEvent.getDropValue() instanceof ConciliacaoContaCorrenteDiaExtratoVO) {
				if (dropEvent.getDragValue().equals("OFXCredito") || dropEvent.getDragValue().equals("OFXDebito")) {
					conciliacaoExtrato = (ConciliacaoContaCorrenteDiaExtratoVO) dropEvent.getDragValue();
					conciliacaoExtratoSei = (ConciliacaoContaCorrenteDiaExtratoVO) dropEvent.getDropValue();
				} else {
					conciliacaoExtrato = (ConciliacaoContaCorrenteDiaExtratoVO) dropEvent.getDropValue();
					conciliacaoExtratoSei = (ConciliacaoContaCorrenteDiaExtratoVO) dropEvent.getDragValue();
				}
				getFacadeFactory().getConciliacaoContaCorrenteFacade().realizarLinkConciliacaoExtratoContaCorrente(getConciliacaoContaCorrenteVO(), conciliacaoExtrato, conciliacaoExtratoSei, getUsuarioLogado());
			}
			setMensagemID("msg_dados_conciliado");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		} finally {
			conciliacaoExtrato = null;
			conciliacaoExtratoSei = null;
		}
	}

	public void realizarRemocaoLinkConciliacaoExtratoContaCorrente() {
		ConciliacaoContaCorrenteDiaExtratoVO conciliacaoExtrato = null;
		try {
			conciliacaoExtrato = (ConciliacaoContaCorrenteDiaExtratoVO) context().getExternalContext().getRequestMap().get("conciliacaoDiaExtrato");
			getFacadeFactory().getConciliacaoContaCorrenteFacade().realizarRemocaoLinkConciliacaoExtratoContaCorrente(getConciliacaoContaCorrenteVO(), conciliacaoExtrato, getUsuarioLogado());
			setMensagemID("msg_dados_desconciliado");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		} finally {
			conciliacaoExtrato = null;
		}
	}

	public void excluirConciliacaoContaCorrenteDiaExtrato() {
		ConciliacaoContaCorrenteDiaExtratoVO conciliacaoExtrato = null;
		try {
			conciliacaoExtrato = (ConciliacaoContaCorrenteDiaExtratoVO) context().getExternalContext().getRequestMap().get("conciliacaoDiaExtrato");
			getFacadeFactory().getConciliacaoContaCorrenteFacade().excluirConciliacaoContaCorrenteDiaExtrato(getConciliacaoContaCorrenteVO(), conciliacaoExtrato, getUsuarioLogado());
			setMensagemID("msg_excluido_extrato_conciliacao");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		} finally {
			conciliacaoExtrato = null;
		}
	}

	public void realizarDesagrupamentoPorValorConciliacaoContaCorrenteDiaExtrato() {
		try {
			setApresentarModalValoresAproximado("");
			getFacadeFactory().getConciliacaoContaCorrenteFacade().realizarDesagrupamentoPorValorConciliacaoContaCorrenteDiaExtrato(getConciliacaoContaCorrenteVO(), getConciliacaoContaCorrenteDiaExtratoVO(), getUsuarioLogado());
			if (getConciliacaoContaCorrenteDiaExtratoVO().getMapaCodigoExtratoPorValor().size() > 0) {
				setApresentarModalValoresAproximado("RichFaces.$('panelDetalhesValoresAproximado').show();");
			}
			setMensagemID("msg_dados_desagrupado_conciliacao");
		} catch (Exception e) {
			setApresentarModalValoresAproximado("");
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void realizarDesagrupamentoPorValorAproximadoConciliacaoContaCorrenteDiaExtrato() {
		try {
			String key = (String) context().getExternalContext().getRequestMap().get("key");
			getFacadeFactory().getConciliacaoContaCorrenteFacade().realizarDesagrupamentoPorValorAproximadoConciliacaoContaCorrenteDiaExtrato(getConciliacaoContaCorrenteVO(), getConciliacaoContaCorrenteDiaExtratoVO(), key, getUsuarioLogado());
			setApresentarModalValoresAproximado("");
			setMensagemID("msg_dados_desagrupado_conciliacao");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void realizarDesagrupamentoPorFormaPagamentoConciliacaoContaCorrenteDiaExtrato() {
		TotalizadorPorFormaPagamentoVO obj = (TotalizadorPorFormaPagamentoVO) context().getExternalContext().getRequestMap().get("totalizadorPorFormaPagamentoItens");
		try {
			getFacadeFactory().getConciliacaoContaCorrenteFacade().realizarDesagrupamentoPorFormaPagamentoConciliacaoContaCorrenteDiaExtrato(getConciliacaoContaCorrenteVO(), getConciliacaoContaCorrenteDiaExtratoVO(), obj, getUsuarioLogado());
			setMensagemID("msg_dados_desagrupado_conciliacao");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		} finally {
			obj = null;
		}
	}

	public void realizarDesagrupamentoConciliacaoContaCorrenteDiaExtrato() {
		ExtratoContaCorrenteVO obj = (ExtratoContaCorrenteVO) context().getExternalContext().getRequestMap().get("extratoContaCorrenteItens");
		try {
			getFacadeFactory().getConciliacaoContaCorrenteFacade().realizarDesagrupamentoConciliacaoContaCorrenteDiaExtrato(getConciliacaoContaCorrenteVO(), getConciliacaoContaCorrenteDiaExtratoVO(), obj, getUsuarioLogado());
			setMensagemID("msg_dados_desagrupado_conciliacao");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		} finally {
			obj = null;
		}
	}

	public void realizarDesagrupamentoConciliacaoContaCorrenteDiaExtratoConjunta() {
		try {
			getFacadeFactory().getConciliacaoContaCorrenteFacade().realizarDesagrupamentoConciliacaoContaCorrenteDiaExtratoConjunta(getConciliacaoContaCorrenteVO(), getConciliacaoContaCorrenteDiaExtratoVO(), getUsuarioLogado());
			setApresentarModalValoresAproximado("RichFaces.$('panelDetalhesExtratoContaCorrente').hide();");
			setMensagemID("msg_dados_desagrupado_conciliacao");
		} catch (Exception e) {
			setApresentarModalValoresAproximado("");
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void realizarAgrupamentoConciliacaoContaCorrenteDiaExtrato(DropEvent dropEvent) {
		ConciliacaoContaCorrenteDiaExtratoVO conciliacaoExtratoDrag = null;
		ConciliacaoContaCorrenteDiaExtratoVO conciliacaoExtratoDrop = null;
		try {
			if (dropEvent.getDragValue() instanceof ConciliacaoContaCorrenteDiaExtratoVO && dropEvent.getDropValue() instanceof ConciliacaoContaCorrenteDiaExtratoVO) {
				conciliacaoExtratoDrag = (ConciliacaoContaCorrenteDiaExtratoVO) dropEvent.getDragValue();
				conciliacaoExtratoDrop = (ConciliacaoContaCorrenteDiaExtratoVO) dropEvent.getDropValue();
				getFacadeFactory().getConciliacaoContaCorrenteFacade().realizarAgrupamentoConciliacaoContaCorrenteDiaExtrato(getConciliacaoContaCorrenteVO(), conciliacaoExtratoDrag, conciliacaoExtratoDrop, getUsuarioLogado());
			}
			setMensagemID("msg_dados_agrupado");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		} finally {
			conciliacaoExtratoDrag = null;
			conciliacaoExtratoDrop = null;
		}
	}

	public void uploadArquivo(FileUploadEvent upload) {
		try {
			setFiltroListaDataConciliacao(null);			
			getFacadeFactory().getConciliacaoContaCorrenteFacade().uploadArquivo(getConciliacaoContaCorrenteVO(), upload, getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(), getUsuarioLogado());
			inicializarMensagemVazia();
		} catch (Exception e) {
			setConciliacaoContaCorrenteVO(new ConciliacaoContaCorrenteVO());
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void downloadArquivoOfx() {
		try {
			File arquivo = new File(getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoTemp() + File.separator + getConciliacaoContaCorrenteVO().getArquivoVO().getPastaBaseArquivo() + File.separator  + getConciliacaoContaCorrenteVO().getArquivoVO().getNome());
			HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
			request.getSession().setAttribute("nomeArquivo", arquivo.getName());
			request.getSession().setAttribute("pastaBaseArquivo", arquivo.getPath().substring(0, arquivo.getPath().lastIndexOf(File.separator)));
			request.getSession().setAttribute("deletarArquivo", false);
			context().getExternalContext().dispatch("/DownloadSV");
			FacesContext.getCurrentInstance().responseComplete();
			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	

	public void limparDadosUpLoad() throws Exception {
		getConciliacaoContaCorrenteVO().setArquivoVO(new ArquivoVO());
		getConciliacaoContaCorrenteVO().setNomeArquivo("");
	}

	public void visualizarDetalheExtratoContaCorrente() {
		try {
			setConciliacaoContaCorrenteDiaExtratoVO((ConciliacaoContaCorrenteDiaExtratoVO) context().getExternalContext().getRequestMap().get("conciliacaoDiaExtrato"));
			getFacadeFactory().getConciliacaoContaCorrenteFacade().preencherDetalheExtratoContaCorrente(getConciliacaoContaCorrenteDiaExtratoVO(), getUsuarioLogado());
			getConciliacaoContaCorrenteDiaExtratoVO().setDesagruparValor(0.0);
			getConciliacaoContaCorrenteDiaExtratoVO().atualizarListaTotalizadores();
			FilterFactory ff = (FilterFactory) getControlador("FilterFactory");
			ff.getMapFilter().clear();
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void realizarNavegacaoParaOrigem() {
		try {
			setExtratoContaCorrenteEstorno((ExtratoContaCorrenteVO) context().getExternalContext().getRequestMap().get("extratoContaCorrenteItens"));
			switch (getExtratoContaCorrenteEstorno().getOrigemExtratoContaCorrente()) {
			case PAGAMENTO:
				if (getFacadeFactory().getNegociacaoPagamentoFacade().validarNegociacaoPagamentoExistente(getExtratoContaCorrenteEstorno().getCodigoOrigem(), false, getUsuarioLogado())) {
					carregarDadosNegociacaoPagamentoParaEstorno();
					setCampoNavegacaoEstornoConciliacaoExtratoDia("popup('../financeiro/negociacaoPagamentoForm.xhtml', 'negociacaoPagamentoForm' , 1024, 800)");
				} else {
					setCampoNavegacaoEstornoConciliacaoExtratoDia("");
				}
				break;
			case RECEBIMENTO:
				if (getFacadeFactory().getNegociacaoRecebimentoFacade().validarNegociacaoRecebimentoExistente(getExtratoContaCorrenteEstorno().getCodigoOrigem(), false, getUsuarioLogado())) {
					carregarDadosNegociacaoRecebimentoParaEstorno();
					setCampoNavegacaoEstornoConciliacaoExtratoDia("popup('../financeiro/negociacaoRecebimentoForm.xhtml', 'negociacaoRecebimentoForm' , 1024, 800)");
				} else {
					setCampoNavegacaoEstornoConciliacaoExtratoDia("");
				}
				break;
			case MOVIMENTACAO_FINANCEIRA:
				if (getFacadeFactory().getMovimentacaoFinanceiraFacade().validarMovimentacaoFinanceiraExistente(getExtratoContaCorrenteEstorno().getCodigoOrigem(), false, getUsuarioLogado())) {
					carregarDadosMovimentacaoFinanceiraParaEstorno();
					setCampoNavegacaoEstornoConciliacaoExtratoDia("popup('../financeiro/movimentacaoFinanceiraForm.xhtml', 'movimentacaoFinanceiraForm' , 1024, 800)");
				} else {
					setCampoNavegacaoEstornoConciliacaoExtratoDia("");
				}
				break;
			case COMPENSACAO_CHEQUE:
				setCampoNavegacaoEstornoConciliacaoExtratoDia("");
				Uteis.checkState(true, "Não é possível realizar o Navegação para uma Origem de Compensação Cheque.");
				break;
			case COMPENSACAO_CARTAO:
				setCampoNavegacaoEstornoConciliacaoExtratoDia("");
				Uteis.checkState(true, "Não é possível realizar o Navegação para uma Origem de Compensação Cartão.");
				break;
			default:
				setCampoNavegacaoEstornoConciliacaoExtratoDia("");
				Uteis.checkState(true, "Não é possível realizar o Navegação pois nao foi encontrado sua  Origem .");
				break;
			}
			setExtratoContaCorrenteEstorno(new ExtratoContaCorrenteVO());
		} catch (Exception e) {
			setCampoNavegacaoEstornoConciliacaoExtratoDia("");
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void fecharNavegacaoParaEstornoExtrato() {
		try {
			context().getExternalContext().getSessionMap().remove("negociacaoConciliacaoBancaria");
			context().getExternalContext().getSessionMap().remove("conciliacaoBancaria");
			context().getExternalContext().getSessionMap().remove("movimentacaoConciliacaoBancaria");
			context().getExternalContext().getSessionMap().remove("realizarEstornoConciliacaoBancaria");
			setExtratoContaCorrenteEstorno(new ExtratoContaCorrenteVO());
			setCampoNavegacaoEstornoConciliacaoExtratoDia("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void realizarEstornoExtratoContaCorrenteSemOrigem() {
		try {
			getFacadeFactory().getConciliacaoContaCorrenteFacade().persistir(getConciliacaoContaCorrenteVO(), true,  getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(), getUsuarioLogado());
			getFacadeFactory().getExtratoContaCorrenteFacade().validarExtratoContaCorrenteComVinculoConciliacaoContaCorrenteParaEstorno(getExtratoContaCorrenteEstorno(), getUsuarioLogado());
			setConciliacaoContaCorrenteVO(getFacadeFactory().getConciliacaoContaCorrenteFacade().consultarPorChavePrimaria(getConciliacaoContaCorrenteVO().getCodigo(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			getFacadeFactory().getConciliacaoContaCorrenteFacade().realizarCalculoTotalizadoresDias(getConciliacaoContaCorrenteVO(), getUsuarioLogado());
			setExtratoContaCorrenteEstorno(new ExtratoContaCorrenteVO());
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setCampoNavegacaoEstornoConciliacaoExtratoDia("");
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public String realizarNavegacaoParaEstornoExtrato() {
		try {
			getFacadeFactory().getConciliacaoContaCorrenteFacade().persistir(getConciliacaoContaCorrenteVO(), true,  getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(), getUsuarioLogado());
			setExtratoContaCorrenteEstorno(new ExtratoContaCorrenteVO());
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setCampoNavegacaoEstornoConciliacaoExtratoDia("");
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return getCampoNavegacaoEstornoConciliacaoExtratoDia();
	}

	public void prepararNavegacaoParaEstornoExtratoDeAcordoComTipoDetalhado() {
		setExtratoContaCorrenteEstorno((ExtratoContaCorrenteVO) context().getExternalContext().getRequestMap().get("extratoContaCorrenteItens"));
		try {
			carregarDadosExtratoContaCorrenteEstorno();
			inicializarMensagemVazia();
		} catch (Exception e) {
			setApresentarModalOperacaoEstorno("");
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void prepararNavegacaoParaEstornoExtratoDeAcordoComTipo() {
		ConciliacaoContaCorrenteDiaExtratoVO obj = (ConciliacaoContaCorrenteDiaExtratoVO) context().getExternalContext().getRequestMap().get("conciliacaoDiaExtrato");
		try {
			setExtratoContaCorrenteEstorno(getFacadeFactory().getExtratoContaCorrenteFacade().consultarCodigoOrigemExtratoContaCorrente(Integer.parseInt(obj.getCodigoSei()), getUsuarioLogado()));
			if (!Uteis.isAtributoPreenchido(getExtratoContaCorrenteEstorno().getConciliacaoContaCorrenteDiaExtratoVO().getCodigo())) {
				getExtratoContaCorrenteEstorno().getConciliacaoContaCorrenteDiaExtratoVO().setCodigo(obj.getCodigo());
			}
			carregarDadosExtratoContaCorrenteEstorno();
			inicializarMensagemVazia();
		} catch (Exception e) {
			setApresentarModalOperacaoEstorno("");
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void carregarDadosExtratoContaCorrenteEstorno() throws Exception {
		switch (getExtratoContaCorrenteEstorno().getOrigemExtratoContaCorrente()) {
		case PAGAMENTO:
			if (getFacadeFactory().getNegociacaoPagamentoFacade().validarNegociacaoPagamentoExistente(getExtratoContaCorrenteEstorno().getCodigoOrigem(), false, getUsuarioLogado())) {
				carregarDadosNegociacaoPagamentoParaEstorno();
			} else {
				setApresentarModalOperacaoEstorno("RichFaces.$('panelEstornoExtratoContaCorrente').show();");
			}
			break;
		case RECEBIMENTO:
			if (getFacadeFactory().getNegociacaoRecebimentoFacade().validarNegociacaoRecebimentoExistente(getExtratoContaCorrenteEstorno().getCodigoOrigem(), false, getUsuarioLogado())) {
				carregarDadosNegociacaoRecebimentoParaEstorno();
			} else {
				setApresentarModalOperacaoEstorno("RichFaces.$('panelEstornoExtratoContaCorrente').show();");
			}
			break;
		case MOVIMENTACAO_FINANCEIRA:
			if (getFacadeFactory().getMovimentacaoFinanceiraFacade().validarMovimentacaoFinanceiraExistente(getExtratoContaCorrenteEstorno().getCodigoOrigem(), false, getUsuarioLogado())) {
				carregarDadosMovimentacaoFinanceiraParaEstorno();
			} else {
				setApresentarModalOperacaoEstorno("RichFaces.$('panelEstornoExtratoContaCorrente').show();");
			}
			break;
		case COMPENSACAO_CHEQUE:
			Uteis.checkState(true, "Não é possível realizar o estorno para uma Origem de Compensação Cheque.");
			break;
		case COMPENSACAO_CARTAO:
			Uteis.checkState(true, "Não é possível realizar o estorno para uma Origem de Compensação Cartão.");
			break;
		default:
			break;
		}

	}

	public void carregarDadosNegociacaoRecebimentoParaEstorno() throws Exception {
		NegociacaoRecebimentoVO neg = new NegociacaoRecebimentoVO();
		neg.setCodigo(getExtratoContaCorrenteEstorno().getCodigoOrigem());
		neg = getFacadeFactory().getNegociacaoRecebimentoFacade().carregarDados(neg, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
		context().getExternalContext().getSessionMap().put("negociacaoConciliacaoBancaria", neg);
		context().getExternalContext().getSessionMap().put("conciliacaoBancaria", getConciliacaoContaCorrenteVO());
		context().getExternalContext().getSessionMap().put("realizarEstornoConciliacaoBancaria", true);
		setCampoNavegacaoEstornoConciliacaoExtratoDia(Uteis.getCaminhoRedirecionamentoNavegacao("negociacaoRecebimentoForm.xhtml"));
		removerControleMemoriaFlash("NegociacaoRecebimentoControle");
		removerControleMemoriaTela("NegociacaoRecebimentoControle");
		setApresentarModalOperacaoEstorno("RichFaces.$('panelAvisoNavegacaoEstornoExtratoDeAcordoComTipo').show();");
	}

	public void carregarDadosNegociacaoPagamentoParaEstorno() throws Exception {
		NegociacaoPagamentoVO neg =  getFacadeFactory().getNegociacaoPagamentoFacade().consultarPorChavePrimaria(getExtratoContaCorrenteEstorno().getCodigoOrigem(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
		context().getExternalContext().getSessionMap().put("negociacaoConciliacaoBancaria", neg);
		context().getExternalContext().getSessionMap().put("conciliacaoBancaria", getConciliacaoContaCorrenteVO());
		context().getExternalContext().getSessionMap().put("realizarEstornoConciliacaoBancaria", true);
		setCampoNavegacaoEstornoConciliacaoExtratoDia(Uteis.getCaminhoRedirecionamentoNavegacao("negociacaoPagamentoForm.xhtml"));
		removerControleMemoriaFlash("NegociacaoPagamentoControle");
		removerControleMemoriaTela("NegociacaoPagamentoControle");
		setApresentarModalOperacaoEstorno("RichFaces.$('panelAvisoNavegacaoEstornoExtratoDeAcordoComTipo').show();");
	}

	public void carregarDadosMovimentacaoFinanceiraParaEstorno() throws Exception {
		MovimentacaoFinanceiraVO mf = getFacadeFactory().getMovimentacaoFinanceiraFacade().consultarPorChavePrimaria(getExtratoContaCorrenteEstorno().getCodigoOrigem(), false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
		if (getLoginControle().getPermissaoAcessoMenuVO().getEstornarMovimentacaoFinanceira()
				&& !mf.getIsSituacaoPendente()
				&& ((!mf.getContaCorrenteOrigem().getContaCaixa() && !mf.getContaCorrenteDestino().getContaCaixa()) || (Uteis.getData(mf.getData()).equals(Uteis.getData(new Date()))))) {
			context().getExternalContext().getSessionMap().put("movimentacaoConciliacaoBancaria", mf);
			context().getExternalContext().getSessionMap().put("conciliacaoBancaria", getConciliacaoContaCorrenteVO());
			context().getExternalContext().getSessionMap().put("realizarEstornoConciliacaoBancaria", true);
			setCampoNavegacaoEstornoConciliacaoExtratoDia(Uteis.getCaminhoRedirecionamentoNavegacao("movimentacaoFinanceiraForm.xhtml"));
			removerControleMemoriaFlash("MovimentacaoFinanceiraControle");
			removerControleMemoriaTela("MovimentacaoFinanceiraControle");
			setApresentarModalOperacaoEstorno("RichFaces.$('panelAvisoNavegacaoEstornoExtratoDeAcordoComTipo').show();");

		} else {
    		throw new Exception("Não é permitido realizar o estorno da movimentação financeira. Por favor verifique sua permissão ou a mesma não foi realizada hoje.");
    	}
	}
	
	public void prepararNavegacaoParaMapaPendenciaCartao() {
		try {
			ConciliacaoContaCorrenteDiaExtratoVO obj = (ConciliacaoContaCorrenteDiaExtratoVO) context().getExternalContext().getRequestMap().get("conciliacaoDiaExtrato");
			context().getExternalContext().getSessionMap().put("nrBanco",obj.getConciliacaoContaCorrenteDia().getConciliacaoContaCorrente().getBanco().getNrBanco());
			context().getExternalContext().getSessionMap().put("numeroContaCorrente",obj.getConciliacaoContaCorrenteDia().getConciliacaoContaCorrente().getContaCorrenteArquivo());
			context().getExternalContext().getSessionMap().put("digitoContaCorrente",obj.getConciliacaoContaCorrenteDia().getConciliacaoContaCorrente().getDigitoContaCorrenteArquivo());
			context().getExternalContext().getSessionMap().put("diaCorrente",obj.getConciliacaoContaCorrenteDia().getData());
			removerControleMemoriaFlash("MapaPendenciaCartaoCreditoControle");
			removerControleMemoriaTela("MapaPendenciaCartaoCreditoControle");
		} catch (Exception e) {
			setApresentarModalOperacaoEstorno("");
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public String getRealizarNavegacaoParaMapaPendenciaCartao() {
		try {
			if(Uteis.isAtributoPreenchido((String) context().getExternalContext().getSessionMap().get("numeroContaCorrente"))){
				return "popup('../financeiro/mapaPendenciaCartaoCreditoCons.xhtml', 'mapaPendenciaCartaoCreditoCons' , 1024, 800)";	
			}
			return "";
		} catch (Exception e) {
			setMensagemID("msg_erro");
			return "";
		}
	}

	public void limparCamposParametrizarOperacoesAutomaticasConciliacaoPorOrigemExtrato() {
		try {
			getParametrizarOperacoesAutomaticasConciliacaoItem().setOrigemExtratoContaCorrenteEnum(getOrigemExtratoContaCorrente());
			if (Uteis.isAtributoPreenchido(getParametrizarOperacoesAutomaticasConciliacaoItem().getOrigemExtratoContaCorrenteEnum()) && getParametrizarOperacoesAutomaticasConciliacaoItem().getOrigemExtratoContaCorrenteEnum().isMovimentacaoFinanceira()) {
				getParametrizarOperacoesAutomaticasConciliacaoItem().setTipoSacado(null);
				getParametrizarOperacoesAutomaticasConciliacaoItem().setTipoFormaPagamento(TipoFormaPagamento.DINHEIRO);
				setTipoFormaPagamento(TipoFormaPagamento.DINHEIRO);
			} else {
				getParametrizarOperacoesAutomaticasConciliacaoItem().setTipoFormaPagamento(TipoFormaPagamento.BOLETO_BANCARIO);
				setTipoFormaPagamento(TipoFormaPagamento.BOLETO_BANCARIO);
				getParametrizarOperacoesAutomaticasConciliacaoItem().setTipoSacado(TipoSacadoExtratoContaCorrenteEnum.FORNECEDOR);
			}
			setFormaPagamentoVO(new FormaPagamentoVO());
			setCentroResultadoOrigemVO(new CentroResultadoOrigemVO());
			getParametrizarOperacoesAutomaticasConciliacaoItem().setContaCorrenteDestinoVO(new ContaCorrenteVO());
			getComboboxTipoFormaPagamento();
			montarListaSelectItemFormaPagamento();
			montarListaSelectItemContaCorrente();
			montarListaSelectItemContaCorrenteDestino();
			setMensagemID("msg_entre_dados", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void novoParametrizarOperacoesAutomaticasConciliacao() {
		try {
			getFacadeFactory().getParametrizarOperacoesAutomaticasConciliacaoItemInterfaceFacade().consultaRapidaPorParametrizarOperacoesAutomaticasConciliacao(getConciliacaoContaCorrenteVO(), getUsuarioLogado());
			setParametrizarOperacoesAutomaticasConciliacaoItem(new ParametrizarOperacoesAutomaticasConciliacaoItemVO());
			setValorConsultaUnidadeEnsino(0);
			setFormaPagamentoVO(new FormaPagamentoVO());
			setCentroResultadoOrigemVO(new CentroResultadoOrigemVO());
			setAbaParametrizarOperacoesAutomaticasConciliacao("");
			setMensagemID("msg_entre_prmconsulta", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void inicializarParametrizarOperacoesAutomaticasConciliacao() {
		try {
			novoParametrizarOperacoesAutomaticasConciliacao();
			setConciliacaoContaCorrenteDiaExtratoVO((ConciliacaoContaCorrenteDiaExtratoVO) context().getExternalContext().getRequestMap().get("conciliacaoDiaExtrato"));
			getParametrizarOperacoesAutomaticasConciliacaoItem().setNomeLancamento(getConciliacaoContaCorrenteDiaExtratoVO().getLancamentoOfx());
			if (getConciliacaoContaCorrenteDiaExtratoVO().isTipoTransacaoEntrada()) {
				getParametrizarOperacoesAutomaticasConciliacaoItem().setTipoMovimentacaoFinanceira(TipoMovimentacaoFinanceira.ENTRADA);
				setAbaParametrizarOperacoesAutomaticasConciliacao("abaCreditoParametrizarOperacoesAutomaticasConciliacao");
			} else {
				getParametrizarOperacoesAutomaticasConciliacaoItem().setTipoMovimentacaoFinanceira(TipoMovimentacaoFinanceira.SAIDA);
				setAbaParametrizarOperacoesAutomaticasConciliacao("abaDebitoParametrizarOperacoesAutomaticasConciliacao");
			}
			getParametrizarOperacoesAutomaticasConciliacaoItem().setTipoFormaPagamento(TipoFormaPagamento.DINHEIRO);
			getParametrizarOperacoesAutomaticasConciliacaoItem().setOrigemExtratoContaCorrenteEnum(OrigemExtratoContaCorrenteEnum.MOVIMENTACAO_FINANCEIRA);
			setOrigemExtratoContaCorrente(OrigemExtratoContaCorrenteEnum.MOVIMENTACAO_FINANCEIRA);
			setTipoFormaPagamento(TipoFormaPagamento.DINHEIRO);
			montarListaSelectItemFormaPagamento();
			montarListaSelectItemUnidadeEnsino();
			setMensagemID("msg_entre_dados", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void addParametrizarOperacoesAutomaticasConciliacao() {
		try {
			getParametrizarOperacoesAutomaticasConciliacaoItem().getUnidadeEnsinoVO().setCodigo(getValorConsultaUnidadeEnsino());
			getParametrizarOperacoesAutomaticasConciliacaoItem().setFormaPagamentoVO(getFormaPagamentoVO());
			getFacadeFactory().getConciliacaoContaCorrenteFacade().addParametrizarOperacoesAutomaticasConciliacao(getConciliacaoContaCorrenteVO(), getParametrizarOperacoesAutomaticasConciliacaoItem(), getUsuarioLogado());
			if (getParametrizarOperacoesAutomaticasConciliacaoItem().getTipoMovimentacaoFinanceira().isMovimentacaoEntrada()) {
				setAbaParametrizarOperacoesAutomaticasConciliacao("abaCreditoParametrizarOperacoesAutomaticasConciliacao");
			} else {
				setAbaParametrizarOperacoesAutomaticasConciliacao("abaDebitoParametrizarOperacoesAutomaticasConciliacao");
			}
			setParametrizarOperacoesAutomaticasConciliacaoItem(new ParametrizarOperacoesAutomaticasConciliacaoItemVO());
			setOrigemExtratoContaCorrente(null);
			setFormaPagamentoVO(new FormaPagamentoVO());
			setCentroResultadoOrigemVO(new CentroResultadoOrigemVO());
			setValorConsultaUnidadeEnsino(0);
			setTipoFormaPagamento(null);
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void editarParametrizarOperacoesAutomaticasConciliacao() {
		try {
			setParametrizarOperacoesAutomaticasConciliacaoItem((ParametrizarOperacoesAutomaticasConciliacaoItemVO) context().getExternalContext().getRequestMap().get("parametroItem"));
			if (getParametrizarOperacoesAutomaticasConciliacaoItem().getTipoMovimentacaoFinanceira().isMovimentacaoEntrada()) {
				setAbaParametrizarOperacoesAutomaticasConciliacao("abaCreditoParametrizarOperacoesAutomaticasConciliacao");
			} else {
				setAbaParametrizarOperacoesAutomaticasConciliacao("abaDebitoParametrizarOperacoesAutomaticasConciliacao");
			}
			setFormaPagamentoVO(getParametrizarOperacoesAutomaticasConciliacaoItem().getFormaPagamentoVO());
			setValorConsultaUnidadeEnsino(getParametrizarOperacoesAutomaticasConciliacaoItem().getUnidadeEnsinoVO().getCodigo());
			setOrigemExtratoContaCorrente(getParametrizarOperacoesAutomaticasConciliacaoItem().getOrigemExtratoContaCorrenteEnum());
			setTipoFormaPagamento(getParametrizarOperacoesAutomaticasConciliacaoItem().getTipoFormaPagamento());
			montarListaSelectItemFormaPagamento();
			montarListaSelectItemUnidadeEnsino();
			montarListaSelectItemUnidadeEnsinoCategoriaDespesa();
			montarListaSelectItemContaCorrente();
			montarListaSelectItemContaCorrenteDestino();
			setMensagemID("msg_dados_selecionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void removeParametrizarOperacoesAutomaticasConciliacao() {
		try {
			ParametrizarOperacoesAutomaticasConciliacaoItemVO parametro = (ParametrizarOperacoesAutomaticasConciliacaoItemVO) context().getExternalContext().getRequestMap().get("parametroItem");
			if (parametro.getTipoMovimentacaoFinanceira().isMovimentacaoEntrada()) {
				setAbaParametrizarOperacoesAutomaticasConciliacao("abaCreditoParametrizarOperacoesAutomaticasConciliacao");
			} else {
				setAbaParametrizarOperacoesAutomaticasConciliacao("abaDebitoParametrizarOperacoesAutomaticasConciliacao");
			}
			getFacadeFactory().getConciliacaoContaCorrenteFacade().removeParametrizarOperacoesAutomaticasConciliacao(getConciliacaoContaCorrenteVO(), parametro, getUsuarioLogado());
			setParametrizarOperacoesAutomaticasConciliacaoItem(new ParametrizarOperacoesAutomaticasConciliacaoItemVO());
			setValorConsultaUnidadeEnsino(0);
			setFormaPagamentoVO(new FormaPagamentoVO());
			setCentroResultadoOrigemVO(new CentroResultadoOrigemVO());
			setOrigemExtratoContaCorrente(null);
			setAbaParametrizarOperacoesAutomaticasConciliacao("");
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void gravarParametrizarOperacoesAutomaticasConciliacao() {
		try {
			getFacadeFactory().getParametrizarOperacoesAutomaticasConciliacaoItemInterfaceFacade().persistir(getConciliacaoContaCorrenteVO(), true, getUsuarioLogado());
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void excluirParametrizarOperacoesAutomaticasConciliacao() {
		try {
			getFacadeFactory().getParametrizarOperacoesAutomaticasConciliacaoItemInterfaceFacade().excluir(getConciliacaoContaCorrenteVO(), true, getUsuarioLogado());
			getConciliacaoContaCorrenteVO().setListaParametrizarEntradaItens(new ArrayList<ParametrizarOperacoesAutomaticasConciliacaoItemVO>());
			getConciliacaoContaCorrenteVO().setListaParametrizarSaidaItens(new ArrayList<ParametrizarOperacoesAutomaticasConciliacaoItemVO>());
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void limparCamposNovoDocumentoSeiPorOrigemExtrato() {
		try {
			if (Uteis.isAtributoPreenchido(getOrigemExtratoContaCorrente()) && getOrigemExtratoContaCorrente().isMovimentacaoFinanceira()) {
				setTituloPanelLocalizacaoDocumentoSei("Nova Movimentação Financeira");
				setMovimentacaoFinanceiraVO(new MovimentacaoFinanceiraVO());
				getMovimentacaoFinanceiraVO().setDescricao(getConciliacaoContaCorrenteDiaExtratoVO().getLancamentoOfx());
				getComboboxTipoFormaPagamento();
				montarListaSelectItemFormaPagamento();
				montarListaSelectItemContaCorrenteDestino();
				setContaPagar(new ContaPagarVO());
				setContaReceber(new ContaReceberVO());
			} else if (getConciliacaoContaCorrenteDiaExtratoVO().isTipoTransacaoEntrada()) {
				setContaReceber(new ContaReceberVO());
				setTituloPanelLocalizacaoDocumentoSei("Nova Conta Receber");
				getContaReceber().setData(getConciliacaoContaCorrenteDiaExtratoVO().getDataOfx());
				getContaReceber().setDataOriginalVencimento(getConciliacaoContaCorrenteDiaExtratoVO().getDataOfx());
				getContaReceber().setDataVencimento(getConciliacaoContaCorrenteDiaExtratoVO().getDataOfx());
				getContaReceber().setDataCompetencia(getConciliacaoContaCorrenteDiaExtratoVO().getDataOfx());
				getContaReceber().setValor(getConciliacaoContaCorrenteDiaExtratoVO().getValorOfx());
				getContaReceber().setValorBaseContaReceber(getConciliacaoContaCorrenteDiaExtratoVO().getValorOfx());
				getContaReceber().setDescricaoPagamento(getConciliacaoContaCorrenteDiaExtratoVO().getLancamentoOfx());
				getContaReceber().setNrDocumento(getConciliacaoContaCorrenteDiaExtratoVO().getDocumentoOfx());
				getContaReceber().setTipoPessoa(TipoPessoa.FUNCIONARIO.getValor());
				montarListaSelectItemFormaPagamentoContaReceber();
				setMovimentacaoFinanceiraVO(new MovimentacaoFinanceiraVO());
				setContaPagar(new ContaPagarVO());
			} else {
				setContaPagar(new ContaPagarVO());
				setTituloPanelLocalizacaoDocumentoSei("Nova Conta Pagar");
				getContaPagar().setData(getConciliacaoContaCorrenteDiaExtratoVO().getDataOfx());
				getContaPagar().setDataFatoGerador(getConciliacaoContaCorrenteDiaExtratoVO().getDataOfx());
				getContaPagar().setDataVencimento(getConciliacaoContaCorrenteDiaExtratoVO().getDataOfx());
				getContaPagar().setDescricao(getConciliacaoContaCorrenteDiaExtratoVO().getLancamentoOfx());
				getContaPagar().setNrDocumento(getConciliacaoContaCorrenteDiaExtratoVO().getDocumentoOfx());
				getContaPagar().setResponsavel(getUsuarioLogadoClone());
				getContaPagar().setResponsavelFinanceiro(getUsuarioLogado().getPessoa());
				getContaPagar().setSituacao("AP");
				getContaPagar().setTipoSacado(TipoSacado.FUNCIONARIO_PROFESSOR.getValor());
				getContaPagar().setJuro(0.0);
				getContaPagar().setMulta(0.0);
				getContaPagar().setValor(getConciliacaoContaCorrenteDiaExtratoVO().getValorOfx() * -1);
				montarListaSelectItemFormaPagamentoContaPagar();
				setMovimentacaoFinanceiraVO(new MovimentacaoFinanceiraVO());
				setContaReceber(new ContaReceberVO());
			}
			setFormaPagamentoVO(new FormaPagamentoVO());
			setParametrizarOperacoesAutomaticasConciliacaoItem(new ParametrizarOperacoesAutomaticasConciliacaoItemVO());
			montarListaSelectItemContaCorrente();
			setMensagemID("msg_entre_dados", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void inicializarNovoDocumentoSei() {
		try {
			setConciliacaoContaCorrenteDiaVO((ConciliacaoContaCorrenteDiaVO) context().getExternalContext().getRequestMap().get("conciliacaoDia"));
			setConciliacaoContaCorrenteDiaExtratoVO((ConciliacaoContaCorrenteDiaExtratoVO) context().getExternalContext().getRequestMap().get("conciliacaoDiaExtrato"));
			setParametrizarOperacoesAutomaticasConciliacaoItem(new ParametrizarOperacoesAutomaticasConciliacaoItemVO());
			setCentroResultadoOrigemVO(new CentroResultadoOrigemVO());
			setFormaPagamentoVO(new FormaPagamentoVO());
			setFormaPagamentoNegociacaoPagamentoVO(new FormaPagamentoNegociacaoPagamentoVO());
			setFormaPagamentoNegociacaoRecebimentoVO(new FormaPagamentoNegociacaoRecebimentoVO());
			setConfiguracaoFinanceiroCartaoVO(new ConfiguracaoFinanceiroCartaoVO());
			setConfiguracaoRecebimentoCartaoOnlineVO(new ConfiguracaoRecebimentoCartaoOnlineVO());
			setOrigemExtratoContaCorrente(OrigemExtratoContaCorrenteEnum.MOVIMENTACAO_FINANCEIRA);
			setTipoFormaPagamento(TipoFormaPagamento.DINHEIRO);
			setTituloPanelLocalizacaoDocumentoSei("Nova Movimentação Financeira");
			setMovimentacaoFinanceiraVO(new MovimentacaoFinanceiraVO());
			getMovimentacaoFinanceiraVO().setDescricao(getConciliacaoContaCorrenteDiaExtratoVO().getLancamentoOfx());
			montarListaSelectItemContaCorrente();
			montarListaSelectItemContaCorrenteDestino();
			montarListaSelectItemUnidadeEnsino();
			montarListaSelectItemFormaPagamento();
			setValorConsultaUnidadeEnsino(0);
			setMensagemID("msg_entre_dados", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void validarPersistenciaConciliacaoAntesDaGeracaoDocumento(OrigemExtratoContaCorrenteEnum origem) throws Exception {
		if (!Uteis.isAtributoPreenchido(getValorConsultaUnidadeEnsino())) {
			throw new Exception("O campo Unidade Ensino deve ser informado");
		}
		if (origem.isMovimentacaoFinanceira()) {
			Uteis.checkState(!Uteis.isAtributoPreenchido(getFormaPagamentoVO().getCodigo()), "O campo forma de pagamento deve ser informado.");
		} else if (origem.isRecebimento()) {
			if (!Uteis.isAtributoPreenchido(getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamento())) {
				throw new Exception("O campo forma pagamento deve ser informado");
			}
			if (getFormaPagamentoNegociacaoRecebimentoVO().getInformaContaCorrente() && !Uteis.isAtributoPreenchido(getFormaPagamentoNegociacaoRecebimentoVO().getContaCorrente()) || !Uteis.isAtributoPreenchido(getContaReceber().getContaCorrenteVO())) {
				throw new Exception("O campo conta corrente deve ser informado");
			}
		} else if (origem.isPagamento()) {
			if (!Uteis.isAtributoPreenchido(getFormaPagamentoNegociacaoPagamentoVO().getFormaPagamento())) {
				throw new Exception("O campo forma pagamento deve ser informado");
			}
			if (getFormaPagamentoNegociacaoRecebimentoVO().getInformaContaCorrente() && !Uteis.isAtributoPreenchido(getFormaPagamentoNegociacaoPagamentoVO().getContaCorrente()) || !Uteis.isAtributoPreenchido(getContaPagar().getContaCorrente())) {
				throw new Exception("O campo conta corrente deve ser informado");
		}
		}
		if (!Uteis.isAtributoPreenchido(getConciliacaoContaCorrenteVO())) {
			gravar();
		}
	}

	public void realizarGeracaoMovimentacaoFinanceiraNovoDocumento() {
		try {
			validarPersistenciaConciliacaoAntesDaGeracaoDocumento(OrigemExtratoContaCorrenteEnum.MOVIMENTACAO_FINANCEIRA);
			getMovimentacaoFinanceiraVO().setSomenteContaDestino(false);
			getMovimentacaoFinanceiraVO().setData(getConciliacaoContaCorrenteDiaVO().getData());
			getMovimentacaoFinanceiraVO().setResponsavel(getUsuarioLogadoClone());
			getMovimentacaoFinanceiraVO().setSituacao(SituacaoMovimentacaoFinanceiraEnum.FINALIZADA.getValor());
			getMovimentacaoFinanceiraVO().getUnidadeEnsinoVO().setCodigo(getValorConsultaUnidadeEnsino());
			getMovimentacaoFinanceiraVO().getMovimentacaoFinanceiraItemVOs().clear();
			MovimentacaoFinanceiraItemVO mfi = new MovimentacaoFinanceiraItemVO();
			mfi.setFormaPagamento(getFormaPagamentoVO());
			mfi.setMovimentacaoFinanceira(getMovimentacaoFinanceiraVO().getCodigo());
			if (getConciliacaoContaCorrenteDiaExtratoVO().isTipoTransacaoEntrada()) {
				mfi.setValor(getConciliacaoContaCorrenteDiaExtratoVO().getValorOfx());
			} else {
				mfi.setValor(getConciliacaoContaCorrenteDiaExtratoVO().getValorOfx() * -1);
			}
			getMovimentacaoFinanceiraVO().getMovimentacaoFinanceiraItemVOs().add(mfi);
			getMovimentacaoFinanceiraVO().setValor(mfi.getValor());
			getFacadeFactory().getConciliacaoContaCorrenteFacade().gravarMovimentacaoFinanceiraPorConciliacaoContaCorrenteDiaExtrato(getConciliacaoContaCorrenteDiaVO(), getConciliacaoContaCorrenteDiaExtratoVO(), getMovimentacaoFinanceiraVO(), getFormaPagamentoVO(), getUsuarioLogado());
			getFacadeFactory().getConciliacaoContaCorrenteFacade().realizarCalculoTotalizadoresDias(getConciliacaoContaCorrenteVO(), getUsuarioLogado());
			setConciliacaoContaCorrenteDiaExtratoVO(new ConciliacaoContaCorrenteDiaExtratoVO());
			setConciliacaoContaCorrenteDiaVO(new ConciliacaoContaCorrenteDiaVO());
			setMovimentacaoFinanceiraVO(new MovimentacaoFinanceiraVO());
			setOrigemExtratoContaCorrente(null);
			setTipoFormaPagamento(null);
			setFormaPagamentoVO(new FormaPagamentoVO());
			setManterPanelPagamentoRecebimentoConta("RichFaces.$('panelNovoDocumentoSei').hide();");
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setManterPanelPagamentoRecebimentoConta("");
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void realizarGeracaoNegociacaoPagamentoNovoDocumento() {
		try {
			if (getFormaPagamentoNegociacaoPagamentoVO().getInformarContaCorrente() && Uteis.isAtributoPreenchido(getContaPagar().getContaCorrente().getCodigo())) {
				getFormaPagamentoNegociacaoPagamentoVO().setContaCorrente(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(getContaPagar().getContaCorrente().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			}
			validarPersistenciaConciliacaoAntesDaGeracaoDocumento(OrigemExtratoContaCorrenteEnum.PAGAMENTO);
			getContaPagar().getUnidadeEnsino().setCodigo(getValorConsultaUnidadeEnsino());
			getFacadeFactory().getConciliacaoContaCorrenteFacade().gravarContaPagarPorConciliacaoContaCorrenteDiaExtrato(getConciliacaoContaCorrenteDiaVO(), getConciliacaoContaCorrenteDiaExtratoVO(), getContaPagar(), getFormaPagamentoNegociacaoPagamentoVO(), getUsuarioLogado());
			getFacadeFactory().getConciliacaoContaCorrenteFacade().realizarCalculoTotalizadoresDias(getConciliacaoContaCorrenteVO(), getUsuarioLogado());
			setConciliacaoContaCorrenteDiaExtratoVO(new ConciliacaoContaCorrenteDiaExtratoVO());
			setConciliacaoContaCorrenteDiaVO(new ConciliacaoContaCorrenteDiaVO());
			setContaPagar(new ContaPagarVO());
			setOrigemExtratoContaCorrente(null);
			setTipoFormaPagamento(null);
			setFormaPagamentoVO(new FormaPagamentoVO());
			setCentroResultadoOrigemVO(new CentroResultadoOrigemVO());
			setManterPanelPagamentoRecebimentoConta("RichFaces.$('panelNovoDocumentoSei').hide();");
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setManterPanelPagamentoRecebimentoConta("");
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void realizarGeracaoNegociacaoRecebimentoNovoDocumento() {
		try {
			if (getFormaPagamentoNegociacaoRecebimentoVO().getInformaContaCorrente() && Uteis.isAtributoPreenchido(getContaReceber().getContaCorrenteVO().getCodigo())) {
				getFormaPagamentoNegociacaoRecebimentoVO().setContaCorrente(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(getContaReceber().getContaCorrenteVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			}
			validarPersistenciaConciliacaoAntesDaGeracaoDocumento(OrigemExtratoContaCorrenteEnum.RECEBIMENTO);
			getContaReceber().setContaCorrente(getContaReceber().getContaCorrenteVO().getCodigo());
			getContaReceber().getUnidadeEnsino().setCodigo(getValorConsultaUnidadeEnsino());
			getFacadeFactory().getConciliacaoContaCorrenteFacade().gravarContaReceberPorConciliacaoContaCorrenteDiaExtrato(getConciliacaoContaCorrenteDiaVO(), getConciliacaoContaCorrenteDiaExtratoVO(), getContaReceber(), getFormaPagamentoNegociacaoRecebimentoVO(), getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			getFacadeFactory().getConciliacaoContaCorrenteFacade().realizarCalculoTotalizadoresDias(getConciliacaoContaCorrenteVO(), getUsuarioLogado());
			setConciliacaoContaCorrenteDiaExtratoVO(new ConciliacaoContaCorrenteDiaExtratoVO());
			setConciliacaoContaCorrenteDiaVO(new ConciliacaoContaCorrenteDiaVO());
			setContaReceber(new ContaReceberVO());
			setOrigemExtratoContaCorrente(null);
			setTipoFormaPagamento(null);
			setFormaPagamentoVO(new FormaPagamentoVO());
			setManterPanelPagamentoRecebimentoConta("RichFaces.$('panelNovoDocumentoSei').hide();");
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setManterPanelPagamentoRecebimentoConta("");
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void inicializarLocalizacaoDocumentoSei() {
		try {
			setConciliacaoContaCorrenteDiaVO((ConciliacaoContaCorrenteDiaVO) context().getExternalContext().getRequestMap().get("conciliacaoDia"));
			setConciliacaoContaCorrenteDiaExtratoVO((ConciliacaoContaCorrenteDiaExtratoVO) context().getExternalContext().getRequestMap().get("conciliacaoDiaExtrato"));
			setCentroResultadoOrigemVO(new CentroResultadoOrigemVO());
			setFormaPagamentoVO(new FormaPagamentoVO());
			setFormaPagamentoNegociacaoPagamentoVO(new FormaPagamentoNegociacaoPagamentoVO());
			setFormaPagamentoNegociacaoRecebimentoVO(new FormaPagamentoNegociacaoRecebimentoVO());
			setConfiguracaoFinanceiroCartaoVO(new ConfiguracaoFinanceiroCartaoVO());
			setConfiguracaoRecebimentoCartaoOnlineVO(new ConfiguracaoRecebimentoCartaoOnlineVO());
			if (getConciliacaoContaCorrenteDiaExtratoVO().isTipoTransacaoEntrada()) {
				setTituloPanelLocalizacaoDocumentoSei("Consultar Conta Receber");
				setContaReceber(new ContaReceberVO());
				setControleConsulta(new ControleConsulta());
				getControleConsulta().setDataIni(getConciliacaoContaCorrenteDiaVO().getData());
				setControleConsultaOtimizado(new DataModelo());
				setOrigemExtratoContaCorrente(OrigemExtratoContaCorrenteEnum.RECEBIMENTO);
			} else {
				setOrigemExtratoContaCorrente(OrigemExtratoContaCorrenteEnum.PAGAMENTO);
				setTituloPanelLocalizacaoDocumentoSei("Consultar Conta Pagar");
				setContaPagar(new ContaPagarVO());
				setControleConsultaOtimizado(new DataModelo());
				getControleConsultaOtimizado().setDataIni(getConciliacaoContaCorrenteDiaVO().getData());
				getControleConsultaOtimizado().setDataFim(getConciliacaoContaCorrenteDiaVO().getData());
				getControleConsultaOtimizado().setCampoConsulta(ContaPagarVO.enumCampoConsultaContaPagar.FAVORECIDO.name());
			}
			montarListaSelectItemUnidadeEnsino();
			setValorConsultaUnidadeEnsino(0);
			setMensagemID("msg_entre_prmconsulta", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void consultarContaReceber() {
		try {
			getControleConsultaOtimizado().getListaConsulta().clear();
			super.consultar();
			getControleConsultaOtimizado().setLimitePorPagina(10);
			if(getControleConsultaOtimizado().getOffset() < 0) {
				getControleConsultaOtimizado().setPaginaAtual(1);
				getControleConsultaOtimizado().getOffset();
			}
			List<ContaReceberVO> objs = new ArrayList<ContaReceberVO>(0);
			List<String> situacoes = new ArrayList<>();
			situacoes.add(getValorConsultaReceberRecebidoNegociado());
			objs = getFacadeFactory().getContaReceberFacade().consultar(getControleConsulta(), getConsDataCompetencia(), getValorConsultaUnidadeEnsino(), getAno(), getSemestre(), getConfiguracaoFinanceiroPadraoSistema(), null, getTipoOrigem(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), Uteis.NIVELMONTARDADOS_DADOSCONSULTA, situacoes, getUsuarioLogado(), null, new ArrayList<String>());
			getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getContaReceberFacade().consultarTotalRegistros(getControleConsulta(), getConsDataCompetencia(), getValorConsultaUnidadeEnsino(), getAno(), getSemestre(), getConfiguracaoFinanceiroPadraoSistema(), null, getTipoOrigem(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), Uteis.NIVELMONTARDADOS_DADOSCONSULTA, situacoes, getUsuarioLogado(), null, new ArrayList<String>()));
			getControleConsultaOtimizado().setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			getControleConsultaOtimizado().getListaConsulta().clear();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarContaReceber() {
		try {
			setContaReceber((ContaReceberVO) context().getExternalContext().getRequestMap().get("contaReceberItens"));
			getFacadeFactory().getContaReceberFacade().carregarDados(getContaReceber(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getContaReceber().getUnidadeEnsinoFinanceira().getCodigo()), getUsuarioLogado());
			if (getConciliacaoContaCorrenteDiaExtratoVO().getValorOfx() > getContaReceber().getValor()) {
				getContaReceber().setAcrescimo(Uteis.arrendondarForcando2CasasDecimais(getConciliacaoContaCorrenteDiaExtratoVO().getValorOfx() - getContaReceber().getValor()));
			} else {
				getContaReceber().setTipoDescontoLancadoRecebimento("VA");
				getContaReceber().setValorDescontoLancadoRecebimento(Uteis.arrendondarForcando2CasasDecimais(getContaReceber().getValor() - getConciliacaoContaCorrenteDiaExtratoVO().getValorOfx()));
				getContaReceber().setValorCalculadoDescontoLancadoRecebimento(Uteis.arrendondarForcando2CasasDecimais(getContaReceber().getValor() - getConciliacaoContaCorrenteDiaExtratoVO().getValorOfx()));
			}
			setValorConsultaUnidadeEnsino(getContaReceber().getUnidadeEnsino().getCodigo());
			if (getContaReceber().getSituacaoAReceber()) {
				setApresentarModalLocalizacaoDocumentoSei("RichFaces.$('panelPagamentoRecebimentoConta').show();");
				montarListaSelectItemFormaPagamentoContaReceber();
				montarListaSelectItemContaCorrente();
			} else {
				setNegociacaoRecebimentoVO(getFacadeFactory().getNegociacaoRecebimentoFacade().consultaRapidaPorNossoNumeroUnicaContaReceber(getContaReceber().getNossoNumero(), Uteis.NIVELMONTARDADOS_TODOS, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getContaReceber().getUnidadeEnsino().getCodigo()), getUsuarioLogado()));
				setApresentarModalLocalizacaoDocumentoSei("RichFaces.$('panelPagamentoRecebimentoContaJaNegociada').show();");
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			getControleConsultaOtimizado().getListaConsulta().clear();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void realizarGeracaoNegociacaoRecebiemnto() {
		try {
			if (Uteis.isAtributoPreenchido(getFormaPagamentoNegociacaoRecebimentoVO().getContaCorrente())) {
				getFormaPagamentoNegociacaoRecebimentoVO().setContaCorrente(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(getFormaPagamentoNegociacaoRecebimentoVO().getContaCorrente().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			}
			validarPersistenciaConciliacaoAntesDaGeracaoDocumento(OrigemExtratoContaCorrenteEnum.RECEBIMENTO);
			getFacadeFactory().getConciliacaoContaCorrenteFacade().gerarNegociacaoContaReceberPorConciliacaoContaCorrenteDiaExtrato(getConciliacaoContaCorrenteDiaVO(), getConciliacaoContaCorrenteDiaExtratoVO(), getContaReceber(), getFormaPagamentoNegociacaoRecebimentoVO(), getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			getFacadeFactory().getConciliacaoContaCorrenteFacade().realizarCalculoTotalizadoresDias(getConciliacaoContaCorrenteVO(), getUsuarioLogado());
			setManterPanelPagamentoRecebimentoConta("RichFaces.$('panelPagamentoRecebimentoConta').hide();RichFaces.$('panelPagamentoRecebimentoContaJaNegociada').hide();RichFaces.$('panelLocalizacaoDocumentoSei').hide();");
			setMensagemID("msg_dados_gravados");
		} catch (ConsistirException ce) {
			setManterPanelPagamentoRecebimentoConta("");
			setConsistirExceptionMensagemDetalhada("msg_erro", ce, Uteis.ERRO);
			setMensagemDetalhada("msg_erro", ce.getMessage());
		} catch (Exception e) {
			setManterPanelPagamentoRecebimentoConta("");
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String realizarEstornoRecebimentoExistente() {
		try {
			gravar();
			context().getExternalContext().getSessionMap().put("negociacaoConciliacaoBancaria", getNegociacaoRecebimentoVO());
			context().getExternalContext().getSessionMap().put("conciliacaoBancaria", getConciliacaoContaCorrenteVO());
			context().getExternalContext().getSessionMap().put("realizarEstornoConciliacaoBancaria", true);
			removerControleMemoriaFlash("NegociacaoRecebimentoControle");
			removerControleMemoriaTela("NegociacaoRecebimentoControle");
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {

			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("negociacaoRecebimentoForm.xhtml");
	}

	public void scrollerListenerContaReceber(DataScrollEvent dataScrollerEvent) throws Exception {
		getControleConsultaOtimizado().setPaginaAtual(dataScrollerEvent.getPage());
		getControleConsultaOtimizado().setPage(dataScrollerEvent.getPage());
		consultarContaReceber();
	}

	public void consultarFormaPagamento() {
		try {
			if (getFormaPagamentoVO().getCodigo().intValue() != 0) {
				setFormaPagamentoVO(getFacadeFactory().getFormaPagamentoFacade().consultarPorChavePrimaria(getFormaPagamentoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
				getParametrizarOperacoesAutomaticasConciliacaoItem().setFormaPagamentoVO(getFormaPagamentoVO());
				getFormaPagamentoNegociacaoPagamentoVO().setFormaPagamento(getFormaPagamentoVO());
				getFormaPagamentoNegociacaoRecebimentoVO().setFormaPagamento(getFormaPagamentoVO());
				if (getFormaPagamentoVO().isInformaOperadoraCartao()) {
					montarListaSelectItemConfiguracaoFinanceiroCartao();
					getParametrizarOperacoesAutomaticasConciliacaoItem().setTipoFinanciamentoEnum(TipoFinanciamentoEnum.OPERADORA);
					getParametrizarOperacoesAutomaticasConciliacaoItem().setCodigoVerificacao("");
					getParametrizarOperacoesAutomaticasConciliacaoItem().setNumeroCartao("");
					getParametrizarOperacoesAutomaticasConciliacaoItem().setAnoValidade(null);
					getParametrizarOperacoesAutomaticasConciliacaoItem().setMesValidade(null);
					getParametrizarOperacoesAutomaticasConciliacaoItem().setNomeCartaoCredito("");
					getParametrizarOperacoesAutomaticasConciliacaoItem().setQtdeParcelasCartaoCredito(0);
					getParametrizarOperacoesAutomaticasConciliacaoItem().setTipoFinanciamentoEnum(null);
					getParametrizarOperacoesAutomaticasConciliacaoItem().setOperadoraCartao(null);
					getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setTipoFinanciamentoEnum(TipoFinanciamentoEnum.OPERADORA);
				}
			} else {
				getParametrizarOperacoesAutomaticasConciliacaoItem().setFormaPagamentoVO(new FormaPagamentoVO());
				getParametrizarOperacoesAutomaticasConciliacaoItem().setCodigoVerificacao("");
				getParametrizarOperacoesAutomaticasConciliacaoItem().setNumeroCartao("");
				getParametrizarOperacoesAutomaticasConciliacaoItem().setAnoValidade(null);
				getParametrizarOperacoesAutomaticasConciliacaoItem().setMesValidade(null);
				getParametrizarOperacoesAutomaticasConciliacaoItem().setNomeCartaoCredito("");
				getParametrizarOperacoesAutomaticasConciliacaoItem().setQtdeParcelasCartaoCredito(0);
				getParametrizarOperacoesAutomaticasConciliacaoItem().setTipoFinanciamentoEnum(null);
				getParametrizarOperacoesAutomaticasConciliacaoItem().setOperadoraCartao(null);
				getFormaPagamentoNegociacaoPagamentoVO().setFormaPagamento(new FormaPagamentoVO());
				getFormaPagamentoNegociacaoRecebimentoVO().setFormaPagamento(new FormaPagamentoVO());
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			getFormaPagamentoNegociacaoPagamentoVO().setFormaPagamento(new FormaPagamentoVO());
		}
	}

	public void validarNumeroCartaoCredito() {
		ConsistirException ce = new ConsistirException();
		try {
			if (getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamento().getTipo().equals(TipoFormaPagamento.CARTAO_DE_CREDITO.getValor()) && !getConfiguracaoRecebimentoCartaoOnlineVO().getCodigo().equals(0) && getConfiguracaoFinanceiroCartaoVO().getPermitiRecebimentoCartaoOnline()) {
				if (!getFacadeFactory().getGerenciamentoDeTransacaoCartaoDeCreditoFacade().validarNumeroCartaoCredito(getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNumeroCartao(), getConfiguracaoFinanceiroCartaoVO().getOperadoraCartaoVO().getNome())) {
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

	public void validarNumeroCartaoCreditoParametrizarOperacoesAutomaticas() {
		ConsistirException ce = new ConsistirException();
		try {
			if (getParametrizarOperacoesAutomaticasConciliacaoItem().getFormaPagamentoVO().getTipo().equals(TipoFormaPagamento.CARTAO_DE_CREDITO.getValor()) && !getConfiguracaoRecebimentoCartaoOnlineVO().getCodigo().equals(0) && getConfiguracaoFinanceiroCartaoVO().getPermitiRecebimentoCartaoOnline()) {
				if (!getFacadeFactory().getGerenciamentoDeTransacaoCartaoDeCreditoFacade().validarNumeroCartaoCredito(getParametrizarOperacoesAutomaticasConciliacaoItem().getNumeroCartao(), getConfiguracaoFinanceiroCartaoVO().getOperadoraCartaoVO().getNome())) {
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
//		for (ConfiguracaoFinanceiroCartaoRecebimentoVO obj : getConfiguracaoRecebimentoCartaoOnlineVO().getConfiguracaoFinanceiroCartaoRecebimentoVOs()) {
//			if (obj.getParcelasAte() >= getFormaPagamentoNegociacaoRecebimentoVO().getQtdeParcelasCartaoCredito()) {
//				getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setTipoFinanciamentoEnum(obj.getTipoFinanciamentoEnum());
//				return;
//			}
//		}
		Date menorDataVencimento = null;
		for (ContaReceberNegociacaoRecebimentoVO c : getNegociacaoRecebimentoVO().getContaReceberNegociacaoRecebimentoVOs()) {
			if (Uteis.isAtributoPreenchido(c.getContaReceber().getDataVencimento()) &&
					(menorDataVencimento == null || c.getContaReceber().getDataVencimento().before(menorDataVencimento))) {
				menorDataVencimento = c.getContaReceber().getDataVencimento();
			}
		}
		getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO()
			.setTipoFinanciamentoEnum(getConfiguracaoRecebimentoCartaoOnlineVO()
				.getTipoFinanciamentoPermitido(getFormaPagamentoNegociacaoRecebimentoVO().getValorRecebimento(), menorDataVencimento, getUsuarioLogado(), getNegociacaoRecebimentoVO().getListaTipoOrigemContaReceber()));
	}

	public void atribuirTipoFinanciamentoParametrizarOperacoesAutomaticas() {
//		for (ConfiguracaoFinanceiroCartaoRecebimentoVO obj : getConfiguracaoRecebimentoCartaoOnlineVO().getConfiguracaoFinanceiroCartaoRecebimentoVOs()) {
//			if (obj.getParcelasAte() >= getParametrizarOperacoesAutomaticasConciliacaoItem().getQtdeParcelasCartaoCredito()) {
//				getParametrizarOperacoesAutomaticasConciliacaoItem().setTipoFinanciamentoEnum(obj.getTipoFinanciamentoEnum());
//				return;
//			}
//		}
		Date menorDataVencimento = null;
		for (ContaReceberNegociacaoRecebimentoVO c : getNegociacaoRecebimentoVO().getContaReceberNegociacaoRecebimentoVOs()) {
			if (Uteis.isAtributoPreenchido(c.getContaReceber().getDataVencimento()) &&
					(menorDataVencimento == null || c.getContaReceber().getDataVencimento().before(menorDataVencimento))) {
				menorDataVencimento = c.getContaReceber().getDataVencimento();
			}
		}
		getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO()
		.setTipoFinanciamentoEnum(getConfiguracaoRecebimentoCartaoOnlineVO()
			.getTipoFinanciamentoPermitido(getFormaPagamentoNegociacaoRecebimentoVO().getValorRecebimento(), menorDataVencimento, getUsuarioLogado(), getNegociacaoRecebimentoVO().getListaTipoOrigemContaReceber()));
	}

	/**
	 * Responsável po executar a definição da configuração financeiro a ser utilizada no Recebimento. Primeiro é verificado se existe uma configuração financeiro vinculada a unidade ensino selecionada no recebimento, caso contrário, é utilizada a regra de definida no SuperControle, que verifica se existe uma unidade ensino logada, caso exista é utilizada a configuração financeira vinculada a tal unidade de ensino, senão é utilizada a configuração financeira padrão do sistema.
	 * 
	 * @author Wellington Rodrigues 25/02/2015
	 * @return ConfiguracaoFinanceiroVO
	 * @throws Exception
	 */
	private ConfiguracaoFinanceiroVO executarDefinicaoConfiguracaoFinanceiroUtilizar() throws Exception {
		ConfiguracaoFinanceiroVO configuracaoFinanceiro = getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarPorUnidadeEnsino(getContaReceber().getUnidadeEnsino().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		if (!Uteis.isAtributoPreenchido(configuracaoFinanceiro)) {
			configuracaoFinanceiro = configuracaoFinanceiroUnidadeLogada();
		}
		return configuracaoFinanceiro;
	}

	public void realizarMontagemCartaoCredito() throws Exception {
		try {
			if (Uteis.isAtributoPreenchido(getConfiguracaoFinanceiroCartaoVO())) {
				setConfiguracaoFinanceiroCartaoVO(getFacadeFactory().getConfiguracaoFinanceiroCartaoFacade().consultarPorChavePrimaria(getConfiguracaoFinanceiroCartaoVO().getCodigo(), NivelMontarDados.TODOS, getUsuarioLogado()));
				getFormaPagamentoNegociacaoRecebimentoVO().setOperadoraCartaoVO(getConfiguracaoFinanceiroCartaoVO().getOperadoraCartaoVO());
				getFormaPagamentoNegociacaoRecebimentoVO().setCategoriaDespesaVO(getConfiguracaoFinanceiroCartaoVO().getCategoriaDespesaVO());
				getFormaPagamentoNegociacaoRecebimentoVO().setContaCorrenteOperadoraCartaoVO(getConfiguracaoFinanceiroCartaoVO().getContaCorrenteVO());
				getFormaPagamentoNegociacaoRecebimentoVO().setContaCorrente(getConfiguracaoFinanceiroCartaoVO().getContaCorrenteVO());
				getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setConfiguracaoFinanceiroCartaoVO(getConfiguracaoFinanceiroCartaoVO());
				getFormaPagamentoNegociacaoRecebimentoVO().setConfiguracaoFinanceiroCartaoVO(getConfiguracaoFinanceiroCartaoVO());
				if (getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamento().isCartaoDebito()) {
					getFormaPagamentoNegociacaoRecebimentoVO().setQtdeParcelasCartaoCredito(1);
				} else {
					if (getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getConfiguracaoFinanceiroCartaoVO().getPermitiRecebimentoCartaoOnline()) {
						consultarConfiguracaoCartaoPermiteRecebimentoOnline();
						if (!getConfiguracaoRecebimentoCartaoOnlineVO().getCodigo().equals(0) && getConfiguracaoFinanceiroCartaoVO().getPermitiRecebimentoCartaoOnline()) {
							getFormaPagamentoNegociacaoRecebimentoVO().setConfiguracaoRecebimentoCartaoOnlineVO(getConfiguracaoRecebimentoCartaoOnlineVO());
						}
					}
					montarListaSelectItemQuantidadeParcelasConfiguracaoFinanceiroCartao();
				}
			} else {
				getFormaPagamentoNegociacaoRecebimentoVO().setOperadoraCartaoVO(null);
				getFormaPagamentoNegociacaoRecebimentoVO().setCategoriaDespesaVO(null);
				getFormaPagamentoNegociacaoRecebimentoVO().setContaCorrenteOperadoraCartaoVO(null);
				getFormaPagamentoNegociacaoRecebimentoVO().setContaCorrente(null);
				getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setConfiguracaoFinanceiroCartaoVO(null);
				getFormaPagamentoNegociacaoRecebimentoVO().setConfiguracaoFinanceiroCartaoVO(null);
				getFormaPagamentoNegociacaoRecebimentoVO().setQtdeParcelasCartaoCredito(0);
			}
			limparMensagem();
		} catch (ConsistirException e) {
			getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setTipoFinanciamentoEnum(TipoFinanciamentoEnum.OPERADORA);
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setConfiguracaoFinanceiroCartaoVO(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void realizarMontagemCartaoCreditoPagamento() throws Exception {
		try {
			if (getConfiguracaoFinanceiroCartaoVO().getCodigo().intValue() != 0) {
				setConfiguracaoFinanceiroCartaoVO(getFacadeFactory().getConfiguracaoFinanceiroCartaoFacade().consultarPorChavePrimaria(getConfiguracaoFinanceiroCartaoVO().getCodigo(), NivelMontarDados.TODOS, getUsuarioLogado()));
				getFormaPagamentoNegociacaoPagamentoVO().setOperadoraCartaoVO(getConfiguracaoFinanceiroCartaoVO().getOperadoraCartaoVO());
				getFormaPagamentoNegociacaoPagamentoVO().setCategoriaDespesaVO(getConfiguracaoFinanceiroCartaoVO().getCategoriaDespesaVO());
				getFormaPagamentoNegociacaoPagamentoVO().setContaCorrenteOperadoraCartaoVO(getConfiguracaoFinanceiroCartaoVO().getContaCorrenteVO());
				getFormaPagamentoNegociacaoPagamentoVO().setContaCorrente(getConfiguracaoFinanceiroCartaoVO().getContaCorrenteVO());
				if (getFormaPagamentoNegociacaoPagamentoVO().getFormaPagamento().getTipo().equals("DE")) {
					getFormaPagamentoNegociacaoPagamentoVO().setQtdeParcelasCartaoCredito(1);
				}

			} else {
				getFormaPagamentoNegociacaoPagamentoVO().setOperadoraCartaoVO(null);
				getFormaPagamentoNegociacaoPagamentoVO().setCategoriaDespesaVO(null);
				getFormaPagamentoNegociacaoPagamentoVO().setContaCorrenteOperadoraCartaoVO(null);
				getFormaPagamentoNegociacaoPagamentoVO().setContaCorrente(null);
				setConfiguracaoFinanceiroCartaoVO(null);
			}
			limparMensagem();
		} catch (ConsistirException e) {
			getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setTipoFinanciamentoEnum(TipoFinanciamentoEnum.OPERADORA);
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setConfiguracaoFinanceiroCartaoVO(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void realizarMontagemCartaoCreditoParametrizarOperacaoAutomaticas() throws Exception {
		try {
			if (Uteis.isAtributoPreenchido(getConfiguracaoFinanceiroCartaoVO())) {
				setConfiguracaoFinanceiroCartaoVO(getFacadeFactory().getConfiguracaoFinanceiroCartaoFacade().consultarPorChavePrimaria(getConfiguracaoFinanceiroCartaoVO().getCodigo(), NivelMontarDados.TODOS, getUsuarioLogado()));
				getParametrizarOperacoesAutomaticasConciliacaoItem().setOperadoraCartao(getConfiguracaoFinanceiroCartaoVO().getOperadoraCartaoVO());
				getParametrizarOperacoesAutomaticasConciliacaoItem().setCategoriaDespesaVO(getConfiguracaoFinanceiroCartaoVO().getCategoriaDespesaVO());
				getParametrizarOperacoesAutomaticasConciliacaoItem().setCentroResultadoAdministrativo(getConfiguracaoFinanceiroCartaoVO().getCentroResultadoAdministrativo());
				getParametrizarOperacoesAutomaticasConciliacaoItem().setContaCorrenteVO(getConfiguracaoFinanceiroCartaoVO().getContaCorrenteVO());
				getParametrizarOperacoesAutomaticasConciliacaoItem().setConfiguracaoFinanceiroCartaoVO(getConfiguracaoFinanceiroCartaoVO());
				if (getParametrizarOperacoesAutomaticasConciliacaoItem().getFormaPagamentoVO().isCartaoDebito()) {
					getParametrizarOperacoesAutomaticasConciliacaoItem().setQtdeParcelasCartaoCredito(1);
				} else {
					if (getParametrizarOperacoesAutomaticasConciliacaoItem().getConfiguracaoFinanceiroCartaoVO().getPermitiRecebimentoCartaoOnline()) {
						consultarConfiguracaoCartaoPermiteRecebimentoOnline();
						if (!getConfiguracaoRecebimentoCartaoOnlineVO().getCodigo().equals(0) && getConfiguracaoFinanceiroCartaoVO().getPermitiRecebimentoCartaoOnline()) {
							getParametrizarOperacoesAutomaticasConciliacaoItem().setConfiguracaoRecebimentoCartaoOnlineVO(getConfiguracaoRecebimentoCartaoOnlineVO());
						}
					}
					montarListaSelectItemQuantidadeParcelasConfiguracaoFinanceiroCartaoParametrizarOperacaoAutomaticas();
				}
			} else {
				getParametrizarOperacoesAutomaticasConciliacaoItem().setOperadoraCartao(null);
				getParametrizarOperacoesAutomaticasConciliacaoItem().setCategoriaDespesaVO(null);
				getParametrizarOperacoesAutomaticasConciliacaoItem().setCentroResultadoAdministrativo(null);
				getParametrizarOperacoesAutomaticasConciliacaoItem().setContaCorrenteVO(null);
				getParametrizarOperacoesAutomaticasConciliacaoItem().setConfiguracaoFinanceiroCartaoVO(null);
				getParametrizarOperacoesAutomaticasConciliacaoItem().setQtdeParcelasCartaoCredito(0);
			}
			limparMensagem();
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setConfiguracaoFinanceiroCartaoVO(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarConfiguracaoCartaoPermiteRecebimentoOnline() {
		try {
			setConfiguracaoRecebimentoCartaoOnlineVO(new ConfiguracaoRecebimentoCartaoOnlineVO());
			setConfiguracaoRecebimentoCartaoOnlineVO(getFacadeFactory().getConfiguracaoRecebimentoCartaoOnlineFacade().consultarConfiguracaoRecebimentoCartaoOnlineDisponivel(0, 0, "", getUnidadeEnsinoLogado().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			verificarContasRecbimentoOnline();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void verificarContasRecbimentoOnline() {
		try {
			if (!getConfiguracaoRecebimentoCartaoOnlineVO().getCodigo().equals(0)) {
				ConsistirException consistirException = new ConsistirException();
				List<ContaReceberNegociacaoRecebimentoVO> lista = new ArrayList<>();
				ContaReceberNegociacaoRecebimentoVO cnr = new ContaReceberNegociacaoRecebimentoVO();
				cnr.setContaReceber(getContaReceber());
				lista.add(cnr);
				getFacadeFactory().getConfiguracaoRecebimentoCartaoOnlineFacade().verificarContasRecebimentoOnline(lista, getConfiguracaoRecebimentoCartaoOnlineVO(), consistirException, true, false, false, false, false, false, getUsuarioLogado());
				if (!consistirException.getListaMensagemErro().isEmpty()) {
					getConfiguracaoFinanceiroCartaoVO().setPermitiRecebimentoCartaoOnline(Boolean.FALSE);
					throw consistirException;
				} else {
					getConfiguracaoFinanceiroCartaoVO().setPermitiRecebimentoCartaoOnline(Boolean.TRUE);
				}
			}
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void montarListaSelectItemQuantidadeParcelasConfiguracaoFinanceiroCartao() {
		Integer maiorParcela = 1;
		getListaSelectItemQuantidadeParcelas().clear();
		if (getConfiguracaoFinanceiroCartaoVO().getPermitiRecebimentoCartaoOnline() && !getConfiguracaoRecebimentoCartaoOnlineVO().getCodigo().equals(0)) {
			Date menorDataVencimento = null;
			for (ContaReceberNegociacaoRecebimentoVO c : getNegociacaoRecebimentoVO().getContaReceberNegociacaoRecebimentoVOs()) {
				if (Uteis.isAtributoPreenchido(c.getContaReceber().getDataVencimento()) &&
						(menorDataVencimento == null || c.getContaReceber().getDataVencimento().before(menorDataVencimento))) {
					menorDataVencimento = c.getContaReceber().getDataVencimento();
				}
			}
			maiorParcela = getConfiguracaoRecebimentoCartaoOnlineVO().getQtdeParcelasPermitida(getFormaPagamentoNegociacaoRecebimentoVO()
					.getValorRecebimento(), menorDataVencimento, getUsuarioLogado(), getNegociacaoRecebimentoVO().getListaTipoOrigemContaReceber());
		} else if (Uteis.isAtributoPreenchido(getConfiguracaoFinanceiroCartaoVO())) {
			maiorParcela = getConfiguracaoFinanceiroCartaoVO().getQuantidadeParcelasCartaoCredito();
		}
		Integer i;
		if (maiorParcela > 1) {
			getListaSelectItemQuantidadeParcelas().add(new SelectItem("", ""));
		}
		for (i = 1; i <= maiorParcela; i++) {
			getListaSelectItemQuantidadeParcelas().add(new SelectItem(i, i.toString()));
		}
		if (getFormaPagamentoNegociacaoRecebimentoVO().getQtdeParcelasCartaoCredito() > maiorParcela) {
			getFormaPagamentoNegociacaoRecebimentoVO().setQtdeParcelasCartaoCredito(maiorParcela);
		}
	}

	public void montarListaSelectItemQuantidadeParcelasConfiguracaoFinanceiroCartaoParametrizarOperacaoAutomaticas() {
		Integer maiorParcela = 1;
		getListaSelectItemQuantidadeParcelas().clear();
		if (getConfiguracaoFinanceiroCartaoVO().getPermitiRecebimentoCartaoOnline() &&
				!getConfiguracaoRecebimentoCartaoOnlineVO().getCodigo().equals(0)) {
			Date menorDataVencimento = null;
			for (ContaReceberNegociacaoRecebimentoVO c : getNegociacaoRecebimentoVO().getContaReceberNegociacaoRecebimentoVOs()) {
				if (Uteis.isAtributoPreenchido(c.getContaReceber().getDataVencimento()) &&
						(menorDataVencimento == null || c.getContaReceber().getDataVencimento().before(menorDataVencimento))) {
					menorDataVencimento = c.getContaReceber().getDataVencimento();
				}
			}
			maiorParcela = getConfiguracaoRecebimentoCartaoOnlineVO().getQtdeParcelasPermitida(getFormaPagamentoNegociacaoRecebimentoVO()
					.getValorRecebimento(), menorDataVencimento, getUsuarioLogado(), getNegociacaoRecebimentoVO().getListaTipoOrigemContaReceber());
		} else if (Uteis.isAtributoPreenchido(getConfiguracaoFinanceiroCartaoVO())) {
			maiorParcela = getConfiguracaoFinanceiroCartaoVO().getQuantidadeParcelasCartaoCredito();
		}
		Integer i;
		if (maiorParcela > 1) {
			getListaSelectItemQuantidadeParcelas().add(new SelectItem("", ""));
		}
		for (i = 1; i <= maiorParcela; i++) {
			getListaSelectItemQuantidadeParcelas().add(new SelectItem(i, i.toString()));
		}
		if (getFormaPagamentoNegociacaoRecebimentoVO().getQtdeParcelasCartaoCredito() > maiorParcela) {
			getFormaPagamentoNegociacaoRecebimentoVO().setQtdeParcelasCartaoCredito(maiorParcela);
		}
	}

	public List<SelectItem> getTipoConsultaComboContaReceber() {
		List<SelectItem> itens = new ArrayList<>(0);
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
		itens.add(new SelectItem("convenio", "Descrição Convênio"));
		itens.add(new SelectItem("fornecedor", "Nome Fornecedor"));
		itens.add(new SelectItem("codigoFinanceiroMatricula", "Código Financeiro Aluno"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	public List<SelectItem> getComboSemestre() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("", ""));
		itens.add(new SelectItem("1", "1°"));
		itens.add(new SelectItem("2", "2°"));
		return itens;
	}

	@SuppressWarnings("UseOfObsoleteCollectionType")
	public List<SelectItem> getListaSelectItemReceberRecebidoNegociado() throws Exception {
		List<SelectItem> objs = new ArrayList<>(0);
		objs.add(new SelectItem("AR", "A Receber"));
		return objs;
	}

	public void limparSituacao() {
		if (getControleConsulta().getCampoConsulta().equals("nrDocumento") || getControleConsulta().getCampoConsulta().equals("nossoNumero")) {
			setValorConsultaReceberRecebidoNegociado("AR");
		}
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

	public boolean getIsApresentarCamposDatasBuscarTodoPeriodo() {
		return !(getControleConsulta().getCampoConsulta().equals("codigo") || getControleConsulta().getCampoConsulta().equals("nossoNumero") || getControleConsulta().getCampoConsulta().equals("nrDocumento"));
	}

	public boolean getIsApresentarCamposDatas() {
		return !(getControleConsulta().getCampoConsulta().equals("codigo") || getControleConsulta().getCampoConsulta().equals("nossoNumero") || getControleConsulta().getCampoConsulta().equals("nrDocumento") || getControleConsulta().getBuscarPeriodoCompleto());
	}

	public void consultarContaPagar() {
		try {
			super.consultar();
			if (isConsultaPorCodigo()) {
				setValorConsultaUnidadeEnsino(getUnidadeEnsinoLogado().getCodigo());
			}
			getControleConsultaOtimizado().preencherDadosParaConsulta(true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			getFacadeFactory().getContaPagarFacade().consultar(getValorConsultaSituacaoFinanceiraDaConta(), getValorConsultaUnidadeEnsino(), getFiltrarDataFactorGerador(), false, getControleConsultaOtimizado());
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void scrollerListenerContaPagar(DataScrollEvent dataScrollerEvent) throws Exception {
		getControleConsultaOtimizado().setPaginaAtual(dataScrollerEvent.getPage());
		getControleConsultaOtimizado().setPage(dataScrollerEvent.getPage());
		consultarContaPagar();
	}

	public void selecionarContaPagar() {
		try {
			setContaPagar((ContaPagarVO) context().getExternalContext().getRequestMap().get("contaPagarItens"));
			setContaPagar(getFacadeFactory().getContaPagarFacade().consultarPorChavePrimaria(getContaPagar().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			Double valorOfx = getConciliacaoContaCorrenteDiaExtratoVO().getValorOfx() * -1;
			if (valorOfx > getContaPagar().getValor()) {
				getContaPagar().setJuro(Uteis.arrendondarForcando2CasasDecimais(valorOfx - getContaPagar().getValor()));
			} else {
				getContaPagar().setDesconto(Uteis.arrendondarForcando2CasasDecimais(getContaPagar().getValor() - valorOfx));
			}
			setValorConsultaUnidadeEnsino(getContaPagar().getUnidadeEnsino().getCodigo());
			if (!getContaPagar().isQuitada()) {
				setApresentarModalLocalizacaoDocumentoSei("RichFaces.$('panelPagamentoRecebimentoConta').show();");
				montarListaSelectItemFormaPagamentoContaPagar();
				montarListaSelectItemContaCorrente();
			} else {
				setNegociacaoPagamentoVO(getFacadeFactory().getNegociacaoPagamentoFacade().consultarPorCodigoContaPagar(getContaPagar().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
				setApresentarModalLocalizacaoDocumentoSei("RichFaces.$('panelPagamentoRecebimentoContaJaNegociada').show();");
			}

			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			getControleConsultaOtimizado().getListaConsulta().clear();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void ajustarValorPorConciliacaoBancaria() {
		try {
			Double valorOfx = getConciliacaoContaCorrenteDiaExtratoVO().getValorOfx() * -1;
			if (getContaPagar().isAjustarValorPorConciliacaoBancaria()) {
				getContaPagar().setValor(valorOfx);
				getContaPagar().setJuro(0.0);
				getContaPagar().setDesconto(0.0);
			} else {
				getContaPagar().setValor(getFacadeFactory().getContaPagarFacade().consultarValorParaAtualizarContaPagar(getContaPagar().getCodigo()));
				if (valorOfx > getContaPagar().getValor()) {
					getContaPagar().setJuro(Uteis.arrendondarForcando2CasasDecimais(valorOfx - getContaPagar().getValor()));
				} else {
					getContaPagar().setDesconto(Uteis.arrendondarForcando2CasasDecimais(getContaPagar().getValor() - valorOfx));
			}
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			getControleConsultaOtimizado().getListaConsulta().clear();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void realizarGeracaoNegociacaoPagamento() {
		try {
			if (Uteis.isAtributoPreenchido(getFormaPagamentoNegociacaoPagamentoVO().getContaCorrente())) {
				getFormaPagamentoNegociacaoPagamentoVO().setContaCorrente(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(getFormaPagamentoNegociacaoPagamentoVO().getContaCorrente().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
				getContaPagar().setContaCorrente(getFormaPagamentoNegociacaoPagamentoVO().getContaCorrente());
			}
			validarPersistenciaConciliacaoAntesDaGeracaoDocumento(OrigemExtratoContaCorrenteEnum.PAGAMENTO);
			getFacadeFactory().getConciliacaoContaCorrenteFacade().gerarNegociacaoContaPagarPorConciliacaoContaCorrenteDiaExtrato(getConciliacaoContaCorrenteDiaVO(), getConciliacaoContaCorrenteDiaExtratoVO(), getContaPagar(), getFormaPagamentoNegociacaoPagamentoVO(), getUsuarioLogado());
			getFacadeFactory().getConciliacaoContaCorrenteFacade().realizarCalculoTotalizadoresDias(getConciliacaoContaCorrenteVO(), getUsuarioLogado());
			setManterPanelPagamentoRecebimentoConta("RichFaces.$('panelPagamentoRecebimentoConta').hide();RichFaces.$('panelPagamentoRecebimentoContaJaNegociada').hide();RichFaces.$('panelLocalizacaoDocumentoSei').hide();");
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setManterPanelPagamentoRecebimentoConta("");
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String realizarEstornoPagamentoExistente() {
		try {
			gravar();
			context().getExternalContext().getSessionMap().put("negociacaoConciliacaoBancaria", getNegociacaoPagamentoVO());
			context().getExternalContext().getSessionMap().put("conciliacaoBancaria", getConciliacaoContaCorrenteVO());
			context().getExternalContext().getSessionMap().put("realizarEstornoConciliacaoBancaria", true);
			removerControleMemoriaFlash("NegociacaoPagamentoControle");
			removerControleMemoriaTela("NegociacaoPagamentoControle");
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("negociacaoPagamentoForm.xhtml");
	}

	public List<SelectItem> getTipoConsultaComboContaPagar() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("codigo", "Número"));
		itens.add(new SelectItem("favorecido", "Favorecido - Período"));
		itens.add(new SelectItem("categoriaDespesa", "Categoria de Despesa"));
		itens.add(new SelectItem("codigoPagamento", "Código do Pagamento"));
		itens.add(new SelectItem("turma", "Turma"));
		itens.add(new SelectItem("departamento", "Departamento"));
		itens.add(new SelectItem("cheque", "Número do Cheque"));
		return itens;
	}

	public List<SelectItem> getListaSelectItemSituacaoFinanceira() throws Exception {
		List<SelectItem> objs = new ArrayList<>(0);
		objs.add(new SelectItem("AP", "A Pagar"));
		objs.add(new SelectItem("PA", "Pago"));
		objs.add(new SelectItem("TO", "Todos"));
		return objs;
	}

	public List<SelectItem> getListaSelectItemCategoriaDespesa()  {
		List<SelectItem> objs = new ArrayList<>(0);
		try {
			objs.add(new SelectItem(0, ""));
			List<CategoriaDespesaVO> listaCategoria = getFacadeFactory().getCategoriaDespesaFacade().consultarPorIdentificadorCategoriaDespesa("", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			listaCategoria.stream().forEach(p -> objs.add(new SelectItem(p.getDescricao(), p.getDescricao())));
			SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
			Collections.sort((List) objs, ordenador);
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return objs;

	}

	public void anularControleOtimizado() {
		montarListaSelectItemUnidadeEnsino();
		getControleConsultaOtimizado().setValorConsulta("");
		getControleConsultaOtimizado().getListaConsulta().clear();
	}

	public boolean isConsultaPorCategoriaDespesa() {
		return getControleConsultaOtimizado().getCampoConsulta().equals(ContaPagarVO.enumCampoConsultaContaPagar.CATEGORIA_DESPESA.name());
	}

	public boolean isConsultaPorCodigo() {
		return getControleConsultaOtimizado().getCampoConsulta().equals(ContaPagarVO.enumCampoConsultaContaPagar.CODIGO.name())
				|| getControleConsultaOtimizado().getCampoConsulta().equals(ContaPagarVO.enumCampoConsultaContaPagar.CODIGO_PAGAMENTO.name())
		|| getControleConsultaOtimizado().getCampoConsulta().equals(ContaPagarVO.enumCampoConsultaContaPagar.NUMERO_NOTA_FISCAL_ENTRADA.name());
	}

	public boolean isCampoText() {
		return  isConsultaPorCodigo()
				|| getControleConsultaOtimizado().getCampoConsulta().equals(ContaPagarVO.enumCampoConsultaContaPagar.FAVORECIDO.name())
				|| getControleConsultaOtimizado().getCampoConsulta().equals(ContaPagarVO.enumCampoConsultaContaPagar.TURMA.name())
				|| getControleConsultaOtimizado().getCampoConsulta().equals(ContaPagarVO.enumCampoConsultaContaPagar.NR_DOCUMENTO.name())
				|| getControleConsultaOtimizado().getCampoConsulta().equals(ContaPagarVO.enumCampoConsultaContaPagar.DEPARTAMENTO.name());
	}

	public void montarListaSelectItemConfiguracaoFinanceiroCartao() {
		try {
			List<ConfiguracaoFinanceiroCartaoVO> lista = new ArrayList<ConfiguracaoFinanceiroCartaoVO>(0);
			getListaSelectItemConfiguracaoFinanceiroCartao().clear();
			if (getFormaPagamentoVO().isCartaoCredito()) {
				lista = getFacadeFactory().getConfiguracaoFinanceiroCartaoFacade().consultarPorTipoOperadoraCartaoConfiguracaoFinanceiro("CARTAO_CREDITO", getConfiguracaoFinanceiroPadraoSistema().getCodigo(), false, getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamento().getCodigo(), false, getUsuarioLogado());
			} else if (getFormaPagamentoVO().isCartaoDebito()) {
				lista = getFacadeFactory().getConfiguracaoFinanceiroCartaoFacade().consultarPorTipoOperadoraCartaoConfiguracaoFinanceiro("CARTAO_DEBITO", getConfiguracaoFinanceiroPadraoSistema().getCodigo(), false, getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamento().getCodigo(), false, getUsuarioLogado());
			}
			getListaSelectItemConfiguracaoFinanceiroCartao().add(new SelectItem(0, ""));
			for (ConfiguracaoFinanceiroCartaoVO obj : lista) {
				if (obj.getOperadoraCartaoVO().getTipo().equals("CARTAO_CREDITO")) {
					getListaSelectItemConfiguracaoFinanceiroCartao().add(new SelectItem(obj.getCodigo(), obj.getContaCorrenteVO().getBancoAgenciaContaCorrente() + "-" + obj.getOperadoraCartaoVO().getNome() + " - "+ obj.getOperadoraCartaoVO().getOperadoraCartaoCreditoApresentar()));
				} else {
					getListaSelectItemConfiguracaoFinanceiroCartao().add(new SelectItem(obj.getCodigo(), obj.getContaCorrenteVO().getBancoAgenciaContaCorrente() + "-" + obj.getOperadoraCartaoVO().getNome()));
				}
			}
			setConfiguracaoFinanceiroCartaoVO(new ConfiguracaoFinanceiroCartaoVO());
			removerObjetoMemoria(lista);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

	}

	public void montarListaSelectItemUnidadeEnsinoCategoriaDespesa() throws Exception {
		getListaSelectItemUnidadeEnsinoCategoriaDespesa().clear();
		if (getUnidadeEnsinoLogado().getCodigo().intValue() != 0) {
			getListaSelectItemUnidadeEnsinoCategoriaDespesa().add(new SelectItem(getUnidadeEnsinoLogado().getCodigo(), getUnidadeEnsinoLogado().getNome()));
			return;
		}
		getListaSelectItemUnidadeEnsinoCategoriaDespesa().add(new SelectItem(0, ""));
		List<UnidadeEnsinoVO> listaUnidadeEnsino = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome("", 0, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		listaUnidadeEnsino.stream().forEach(p -> getListaSelectItemUnidadeEnsinoCategoriaDespesa().add(new SelectItem(p.getCodigo(), p.getNome())));

	}

	public void montarListaSelectItemUnidadeEnsino() {
		try {
			getListaSelectItemUnidadeEnsino().clear();
			if (getUnidadeEnsinoLogado().getCodigo().intValue() != 0) {
				setListaSelectItemUnidadeEnsino(new ArrayList<SelectItem>());
				getListaSelectItemUnidadeEnsino().add(new SelectItem(0, ""));
				UnidadeEnsinoVO obj = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
				getListaSelectItemUnidadeEnsino().add(new SelectItem(obj.getCodigo(), obj.getNome()));
				getParametrizarOperacoesAutomaticasConciliacaoItem().setUnidadeEnsinoVO(obj);
				return;
			}
			List resultadoConsulta = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome("", 0, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
		} catch (Exception e) {
			e.printStackTrace();
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

	public void selecionarCentroReceita() {
		CentroReceitaVO obj = (CentroReceitaVO) context().getExternalContext().getRequestMap().get("centroReceitaItens");
		getParametrizarOperacoesAutomaticasConciliacaoItem().setCentroReceitaVO(obj);
		getContaReceber().setCentroReceita(obj);
	}

	public List<SelectItem> getTipoConsultaComboCentroReceita() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("descricao", "Descrição"));
		itens.add(new SelectItem("identificadorCentroReceita", "Identificador Centro Receita"));
		itens.add(new SelectItem("nomeDepartamento", "Departamento"));
		return itens;
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
			CategoriaDespesaVO obj = (CategoriaDespesaVO) context().getExternalContext().getRequestMap().get("centroDespesaItens");
			obj = getFacadeFactory().getCategoriaDespesaFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			getCentroResultadoOrigemVO().setCategoriaDespesaVO(obj);
			getParametrizarOperacoesAutomaticasConciliacaoItem().setCategoriaDespesaVO(obj);
			limparDadosPorCategoriaDespesa();
			montarListaSelectItemTipoNivelCentroResultadoEnum();
			montarListaSelectItemUnidadeEnsinoCategoriaDespesa();
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
				objs = getFacadeFactory().getCategoriaDespesaFacade().consultarPorDescricao(getValorConsultaCentroDespesa(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
				break;
			case IDENTIFICADOR_CENTRO_DESPESA:
				objs = getFacadeFactory().getCategoriaDespesaFacade().consultarPorIdentificadorCategoriaDespesa(getValorConsultaCentroDespesa(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
				break;
			default:
				break;
			}
			setListaConsultaCentroDespesa(objs);
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setListaConsultaCentroDespesa(new ArrayList<>(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarFuncionarioCentroCusto() {
		try {
			List<FuncionarioCargoVO> objs = new ArrayList<>(0);
			if (getCampoConsultaFuncionarioCentroCusto().equals("nomeFuncionario")) {
				objs = getFacadeFactory().getFuncionarioCargoFacade().consultarPorNomeFuncionarioUnidadeEnsinoSituacao(getValorConsultaFuncionarioCentroCusto(), null, true, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionarioCentroCusto().equals("nomeCargo")) {
				objs = getFacadeFactory().getFuncionarioCargoFacade().consultarPorNomeCargoUnidadeEnsinoSituacao(getValorConsultaFuncionarioCentroCusto(), null, true, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
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
			FuncionarioCargoVO obj = (FuncionarioCargoVO) context().getExternalContext().getRequestMap().get("funcionarioCentroCustoItens");
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
				objs = getFacadeFactory().getDepartamentoFacade().consultarPorCodigoPorUnidadeEnsino(new Integer(valorInt), unidadeEnsino, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaDepartamento().equals("nome")) {
				objs = getFacadeFactory().getDepartamentoFacade().consultarPorNomePorUnidadeEnsino(getValorConsultaDepartamento(), unidadeEnsino, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
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
			Uteis.checkState(!Uteis.isAtributoPreenchido(getCentroResultadoOrigemVO().getUnidadeEnsinoVO()), "O campo Unidade Ensino (CEntro Resultado Origem) deve ser informado.");
			setListaConsultaTurma(getFacadeFactory().getTurmaFacade().consultar(getCampoConsultaTurma(), getValorConsultaTurma(), getCentroResultadoOrigemVO().getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			getListaConsultaTurma().clear();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem>  getTipoConsultaComboTurma() {
		List<SelectItem>  itens = new ArrayList<>(0);
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
			Uteis.checkState(!Uteis.isAtributoPreenchido(getCentroResultadoOrigemVO().getUnidadeEnsinoVO()), "O campo Unidade Ensino (Centro Resultado Origem) deve ser informado.");
			setListaConsultaCurso(getFacadeFactory().getCursoFacade().consultar(getCampoConsultaCurso(), getValorConsultaCurso(), getCentroResultadoOrigemVO().getUnidadeEnsinoVO().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setListaConsultaCurso(null);
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public List<SelectItem>  getTipoConsultaComboCurso() {
		List<SelectItem>  itens = new ArrayList<>(0);
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
			UnidadeEnsinoCursoVO obj = (UnidadeEnsinoCursoVO) context().getExternalContext().getRequestMap().get("unidadeEnsinoCursoItens");
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
			Uteis.checkState(!Uteis.isAtributoPreenchido(getCentroResultadoOrigemVO().getUnidadeEnsinoVO()), "O campo Unidade Ensino (Centro Resultado Origem) deve ser informado.");
			setListaConsultaCursoTurno(getFacadeFactory().getUnidadeEnsinoCursoFacade().consultar(getCampoConsultaCursoTurno(), getValorConsultaCursoTurno(), getCentroResultadoOrigemVO().getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setListaConsultaCursoTurno(null);
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaComboCursoTurno() {
		List<SelectItem>  itens = new ArrayList<>(0);
		itens.add(new SelectItem("curso", "Curso"));
		itens.add(new SelectItem("turno", "Turno"));
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
				getFacadeFactory().getCategoriaDespesaFacade().montarListaSelectItemTipoNivelCentroResultadoEnum(getCentroResultadoOrigemVO().getCategoriaDespesaVO(), getListaSelectItemTipoNivelCentroResultadoEnum());
				if (!getListaSelectItemTipoNivelCentroResultadoEnum().isEmpty() && !Uteis.isAtributoPreenchido(getCentroResultadoOrigemVO().getTipoNivelCentroResultadoEnum())) {
					getCentroResultadoOrigemVO().setTipoNivelCentroResultadoEnum((TipoNivelCentroResultadoEnum) getListaSelectItemTipoNivelCentroResultadoEnum().get(0).getValue());
				}
			}
			inicializarMensagemVazia();
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
				getParametrizarOperacoesAutomaticasConciliacaoItem().setCentroResultadoAdministrativo(obj);
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

	public void realizarDistribuicaoValoresCentroResultado() {
		try {
			getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().realizarDistribuicaoValoresCentroResultado(getContaPagar().getListaCentroResultadoOrigemVOs(), getContaPagar().getPrevisaoValorPago(), getUsuarioLogado());
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void calcularPorcentagemCentroResultadoOrigem() {
		try {
			getCentroResultadoOrigemVO().calcularPorcentagem(getContaPagar().getPrevisaoValorPago());
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void calcularValorCentroResultadoOrigem() {
		try {
			getCentroResultadoOrigemVO().calcularValor(getContaPagar().getPrevisaoValorPago());
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void addCentroResultadoOrigem() {
		try {
			getCentroResultadoOrigemVO().setQuantidade(1.0);
			getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().adicionarCentroResultadoOrigem(getContaPagar().getListaCentroResultadoOrigemVOs(), getCentroResultadoOrigemVO(), getContaPagar().getPrevisaoValorPago(), true, getUsuarioLogado());
			setCentroResultadoOrigemVO(new CentroResultadoOrigemVO());
			setMensagemID(MSG_TELA.msg_dados_adicionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void editarCentroResultadoOrigem() {
		try {
			setCentroResultadoOrigemVO((CentroResultadoOrigemVO) context().getExternalContext().getRequestMap().get("centroResultadoOrigemItem"));
			montarListaSelectItemTipoNivelCentroResultadoEnum();
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}

	}

	public void removerCentroResultadoOrigem() {
		try {
			CentroResultadoOrigemVO centroResultadoOrigem = (CentroResultadoOrigemVO) context().getExternalContext().getRequestMap().get("centroResultadoOrigemItem");
			getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().removerCentroResultadoOrigem(getContaPagar().getListaCentroResultadoOrigemVOs(), centroResultadoOrigem, getUsuarioLogado());
			setMensagemID(MSG_TELA.msg_dados_excluidos.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void preencherDadosPorCategoriaDespesa() {
		try {
			if (Uteis.isAtributoPreenchido(getCentroResultadoOrigemVO().getCategoriaDespesaVO())) {
				getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().preencherDadosPorCategoriaDespesa(getCentroResultadoOrigemVO(), getUsuarioLogado());
			}
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void limparCamposTipoSacado() {
		try {
			if ((Uteis.isAtributoPreenchido(getParametrizarOperacoesAutomaticasConciliacaoItem().getTipoSacado()) && getParametrizarOperacoesAutomaticasConciliacaoItem().getTipoSacado().isBanco()) || getContaPagar().isTipoSacadoBanco()) {
				getParametrizarOperacoesAutomaticasConciliacaoItem().setFornecedorSacado(new FornecedorVO());
				getParametrizarOperacoesAutomaticasConciliacaoItem().setFuncionarioSacado(new FuncionarioVO());
				getParametrizarOperacoesAutomaticasConciliacaoItem().setParceiroSacado(new ParceiroVO());
				getContaPagar().setFornecedor(new FornecedorVO());
				getContaPagar().setFuncionario(new FuncionarioVO());
				getContaPagar().setParceiro(new ParceiroVO());
			} else if ((Uteis.isAtributoPreenchido(getParametrizarOperacoesAutomaticasConciliacaoItem().getTipoSacado()) && getParametrizarOperacoesAutomaticasConciliacaoItem().getTipoSacado().isFornecedor()) || getContaPagar().isTipoSacadoFornecedor() || getContaReceber().getTipoFornecedor()) {
				getParametrizarOperacoesAutomaticasConciliacaoItem().setFuncionarioSacado(new FuncionarioVO());
				getParametrizarOperacoesAutomaticasConciliacaoItem().setParceiroSacado(new ParceiroVO());
				getParametrizarOperacoesAutomaticasConciliacaoItem().setBancoSacado(new BancoVO());
				getContaPagar().setFuncionario(new FuncionarioVO());
				getContaPagar().setParceiro(new ParceiroVO());
				getContaPagar().setBanco(new BancoVO());
				getContaReceber().setFuncionario(new FuncionarioVO());
				getContaReceber().setParceiroVO(new ParceiroVO());
			} else if ((Uteis.isAtributoPreenchido(getParametrizarOperacoesAutomaticasConciliacaoItem().getTipoSacado()) && getParametrizarOperacoesAutomaticasConciliacaoItem().getTipoSacado().isFuncionario()) || getContaPagar().isTipoSacadoFuncionario() || getContaReceber().getTipoFuncionario()) {
				getParametrizarOperacoesAutomaticasConciliacaoItem().setFornecedorSacado(new FornecedorVO());
				getParametrizarOperacoesAutomaticasConciliacaoItem().setParceiroSacado(new ParceiroVO());
				getParametrizarOperacoesAutomaticasConciliacaoItem().setBancoSacado(new BancoVO());
				getContaPagar().setFornecedor(new FornecedorVO());
				getContaPagar().setParceiro(new ParceiroVO());
				getContaPagar().setBanco(new BancoVO());
				getContaReceber().setFornecedor(new FornecedorVO());
				getContaReceber().setParceiroVO(new ParceiroVO());
			} else if ((Uteis.isAtributoPreenchido(getParametrizarOperacoesAutomaticasConciliacaoItem().getTipoSacado()) && getParametrizarOperacoesAutomaticasConciliacaoItem().getTipoSacado().isParceiro()) || getContaPagar().isTipoSacadoParceiro() || getContaReceber().getTipoParceiro()) {
				getParametrizarOperacoesAutomaticasConciliacaoItem().setFornecedorSacado(new FornecedorVO());
				getParametrizarOperacoesAutomaticasConciliacaoItem().setFuncionarioSacado(new FuncionarioVO());
				getParametrizarOperacoesAutomaticasConciliacaoItem().setBancoSacado(new BancoVO());
				getContaPagar().setFornecedor(new FornecedorVO());
				getContaPagar().setFuncionario(new FuncionarioVO());
				getContaPagar().setBanco(new BancoVO());
				getContaReceber().setFuncionario(new FuncionarioVO());
				getContaReceber().setFornecedor(new FornecedorVO());
			}  else if ((Uteis.isAtributoPreenchido(getParametrizarOperacoesAutomaticasConciliacaoItem().getTipoSacado()) && getParametrizarOperacoesAutomaticasConciliacaoItem().getTipoSacado().isAluno()) || getContaPagar().getTipoAluno() || getContaReceber().getTipoAluno()) {
				getContaReceber().setMatriculaAluno(new MatriculaVO());
				getContaReceber().setPessoa(new PessoaVO());
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

	}

	public void selecionarParceiro() {
		try {
			ParceiroVO obj = (ParceiroVO) context().getExternalContext().getRequestMap().get("parceiroItens");
			getParametrizarOperacoesAutomaticasConciliacaoItem().setParceiroSacado(obj);
			getContaPagar().setParceiro(obj);
			getContaReceber().setParceiroVO(obj);
			setMensagemID("msg_dados_selecionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
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
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaComboParceiro() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("razaoSocial", "Razão Social"));
		itens.add(new SelectItem("RG", "RG"));
		itens.add(new SelectItem("CPF", "CPF"));
		itens.add(new SelectItem("tipoParceiro", "Tipo Parceiro"));
		return itens;
	}

	public void selecionarFuncionario() {
		try {
			FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItens");
			getParametrizarOperacoesAutomaticasConciliacaoItem().setFuncionarioSacado(obj);
			getContaPagar().setFuncionario(obj);
			getContaReceber().setFuncionario(obj);
			setMensagemID("msg_dados_selecionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarFuncionarioPorCodigo() {
		FuncionarioVO funcionario = null;
		try {
			if (!getParametrizarOperacoesAutomaticasConciliacaoItem().getFuncionarioSacado().getMatricula().isEmpty() && (Uteis.isAtributoPreenchido(getParametrizarOperacoesAutomaticasConciliacaoItem().getTipoSacado()) && getParametrizarOperacoesAutomaticasConciliacaoItem().getTipoSacado().isFuncionario())) {
				funcionario = getFacadeFactory().getFuncionarioFacade().consultarPorRequisitanteMatricula(getParametrizarOperacoesAutomaticasConciliacaoItem().getFuncionarioSacado().getMatricula(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			} else if (!getContaPagar().getFuncionario().getMatricula().isEmpty() && getContaPagar().isTipoSacadoFuncionario()) {
				funcionario = getFacadeFactory().getFuncionarioFacade().consultarPorRequisitanteMatricula(getContaPagar().getFuncionario().getMatricula(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			} else if (!getContaReceber().getFuncionario().getMatricula().isEmpty() && getContaReceber().getTipoFuncionario()) {
				funcionario = getFacadeFactory().getFuncionarioFacade().consultarPorRequisitanteMatricula(getContaReceber().getFuncionario().getMatricula(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (funcionario != null && funcionario.getCodigo().intValue() != 0) {
				getParametrizarOperacoesAutomaticasConciliacaoItem().setFuncionarioSacado(funcionario);
				getContaPagar().setFuncionario(funcionario);
				getContaReceber().setFuncionario(funcionario);
				setMensagemID("msg_dados_consultados");
			} else {
				throw new Exception();
			}
		} catch (Exception e) {
			getParametrizarOperacoesAutomaticasConciliacaoItem().getFuncionarioSacado().setCodigo(0);
			getParametrizarOperacoesAutomaticasConciliacaoItem().getFuncionarioSacado().setMatricula("");
			getParametrizarOperacoesAutomaticasConciliacaoItem().getFuncionarioSacado().getPessoa().setNome("");
			getContaPagar().getFuncionario().setCodigo(0);
			getContaPagar().getFuncionario().setMatricula("");
			getContaPagar().getFuncionario().getPessoa().setNome("");
			getContaReceber().getFuncionario().setCodigo(0);
			getContaReceber().getFuncionario().setMatricula("");
			getContaReceber().getFuncionario().getPessoa().setNome("");
			setMensagemID("msg_erro_dadosnaoencontrados");
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
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorNome(getValorConsultaFuncionario(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("matricula")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorMatricula(getValorConsultaFuncionario(), getUnidadeEnsinoLogado().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("CPF")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorCPF(getValorConsultaFuncionario(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("cargo")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeCargo(getValorConsultaFuncionario(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("departamento")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeDepartamento(getValorConsultaFuncionario(), "FU", getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaFuncionario(objs);
			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			setListaConsultaFuncionario(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarAlunoPorMatricula() {
        try {
            if (!getContaPagar().getMatricula().equals("")) {
            	MatriculaVO objs = getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(getContaPagar().getMatricula(), 0 , 0,  false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,  getUsuarioLogado().getCodigo().equals(0) ? getUsuarioLogado() : getUsuarioLogado());
                
                if (Uteis.isAtributoPreenchido(objs.getMatricula())) {
                	getContaPagar().setPessoa(objs.getAluno());
                	setMensagemID("msg_dados_consultados");
                    return;
                } else {
                	getContaPagar().setPessoa(new PessoaVO());
                	getContaPagar().setMatricula("");
                }
                setMensagemID("msg_erro_dadosnaoencontrados");
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

	public void consultarAluno() {
        try {
        	Integer codigoUnidadeEnsino = Uteis.isAtributoPreenchido(this.getUnidadeEnsinoLogado().getCodigo()) ? this.getUnidadeEnsinoLogado().getCodigo() : Uteis.isAtributoPreenchido(getContaReceber().getUnidadeEnsino()) ? getContaReceber().getUnidadeEnsino().getCodigo() : 0;
            List<MatriculaVO> objs = new ArrayList<MatriculaVO>(0);
            if (getValorConsultaAluno().equals("")) {
                setMensagemID("msg_entre_prmconsulta");
                return;
            }
            if (getCampoConsultaAluno().equals("matricula")) {
                objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatricula(getValorConsultaAluno(), codigoUnidadeEnsino, 0, true, false, getUsuarioLogado());
            }
            if (getCampoConsultaAluno().equals("nomePessoa")) {
                objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), codigoUnidadeEnsino, 0, true, false, getUsuarioLogado());
            }
            setListaConsultaAluno(objs);
            setMensagemID("msg_dados_consultados");

        } catch (Exception e) {
            setListaConsultaAluno(new ArrayList<MatriculaVO>(0));
            setMensagemDetalhada("msg_erro", e.getMessage());

        }
    }

	public void selecionarAluno() {
		try {
			MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("alunoItens");
			getContaPagar().setPessoa(obj.getAluno());
			getContaPagar().setMatricula(obj.getMatricula());
			campoConsultaAluno = null;
			valorConsultaAluno = null;
			setListaConsultaAluno(new ArrayList<>());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public List<SelectItem> getTipoConsultaComboFuncionario() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("CPF", "CPF"));
		itens.add(new SelectItem("cargo", "Cargo"));
		itens.add(new SelectItem("departamento", "Departamento"));
		return itens;
	}

	public void selecionarFornecedor() {
		try {
			FornecedorVO obj = (FornecedorVO) context().getExternalContext().getRequestMap().get("fornecedorItens");
			getParametrizarOperacoesAutomaticasConciliacaoItem().setFornecedorSacado(obj);
			getContaPagar().setFornecedor(obj);
			getContaReceber().setFornecedor(obj);
			setMensagemID("msg_dados_selecionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
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

	public void consultarBanco() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaBanco().equals("nome")) {
				objs = getFacadeFactory().getBancoFacade().consultarPorNome(getValorConsultaBanco(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaBanco().equals("nrBanco")) {
				objs = getFacadeFactory().getBancoFacade().consultarPorNrBanco(getValorConsultaBanco(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaBanco(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaBanco(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarBanco() {
		try {
			BancoVO obj = (BancoVO) context().getExternalContext().getRequestMap().get("bancoItens");
			getParametrizarOperacoesAutomaticasConciliacaoItem().setBancoSacado(obj);
			getContaPagar().setBanco(obj);
			setCampoConsultaBanco("");
			setValorConsultaBanco("");
			setListaConsultaBanco(new ArrayList(0));
			setMensagemID("msg_dados_selecionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

	}

	public List<SelectItem> getTipoConsultaComboBanco() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("nrBanco", "Número Banco"));
		return itens;
	}

	public List<SelectItem> getTipoConsultaComboFornecedor() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("razaoSocial", "Razão Social"));
		itens.add(new SelectItem("CPF", "CPF"));
		itens.add(new SelectItem("CNPJ", "CNPJ"));
		itens.add(new SelectItem("RG", "RG"));

		return itens;
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox relativo ao atributo <code>FormaPagamento</code>.
	 */
	public void montarListaSelectItemFormaPagamento() throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			if (Uteis.isAtributoPreenchido(getTipoFormaPagamento())) {
				resultadoConsulta = getFacadeFactory().getFormaPagamentoFacade().consultarPorTipo(getTipoFormaPagamento().getValor(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
				i = resultadoConsulta.iterator();
				List objs = new ArrayList<>();
				objs.add(new SelectItem(0, ""));
				while (i.hasNext()) {
					FormaPagamentoVO obj = (FormaPagamentoVO) i.next();
					objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
				}
				setListaSelectItemFormaPagamento(objs);
			} else {
				setListaSelectItemFormaPagamento(new ArrayList<>());
			}
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo <code>FormaPagamento</code>. Buscando todos os objetos correspondentes a entidade <code>FormaPagamento</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemFormaPagamentoContaPagar() {
		try {
			montarListaSelectItemFormaPagamentoContaPagar("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox relativo ao atributo <code>FormaPagamento</code>.
	 */
	public void montarListaSelectItemFormaPagamentoContaPagar(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = getFacadeFactory().getFormaPagamentoFacade().consultarPorNome(prm, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				FormaPagamentoVO obj = (FormaPagamentoVO) i.next();
				// if (obj.isDebitoEmConta() || obj.isDinheiro() || obj.isCheque() || obj.isCartaoCredito() || obj.isCartaoDebito() || obj.isBoletoBancario() || obj.isPermuta()) {
				if (obj.isDebitoEmConta() || obj.isBoletoBancario() || obj.isDeposito()) {
					objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
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
	 * Método responsável por atualizar o ComboBox relativo ao atributo <code>FormaPagamento</code>. Buscando todos os objetos correspondentes a entidade <code>FormaPagamento</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemFormaPagamentoContaReceber() {
		try {
			montarListaSelectItemFormaPagamentoContaReceber("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox relativo ao atributo <code>FormaPagamento</code>.
	 */
	public void montarListaSelectItemFormaPagamentoContaReceber(String prm) throws Exception {
		getListaSelectItemFormaPagamento().clear();
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = getFacadeFactory().getFormaPagamentoFacade().consultarPorNomeUsaNoRecebimento(prm, true, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				FormaPagamentoVO obj = (FormaPagamentoVO) i.next();
				if (obj.isDebitoEmConta() || obj.isBoletoBancario() || obj.isDeposito()) {
					objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
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
	 * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox relativo ao atributo <code>FormaPagamento</code>.
	 */
	public void montarListaSelectItemContaCorrente() throws Exception {
		List<ContaCorrenteVO> resultadoConsulta = null;
		ContaCorrenteVO contaCorrenteTemp = new ContaCorrenteVO();
		try {
			if (Uteis.isAtributoPreenchido(getOrigemExtratoContaCorrente()) && getOrigemExtratoContaCorrente().isMovimentacaoFinanceira() && ((Uteis.isAtributoPreenchido(getConciliacaoContaCorrenteDiaExtratoVO().getTipoTransacaoOFXEnum()) && getConciliacaoContaCorrenteDiaExtratoVO().isTipoTransacaoEntrada()) || (Uteis.isAtributoPreenchido(getParametrizarOperacoesAutomaticasConciliacaoItem().getNomeLancamento()) && getParametrizarOperacoesAutomaticasConciliacaoItem().getTipoMovimentacaoFinanceira().isMovimentacaoEntrada()))) {
				resultadoConsulta = getFacadeFactory().getContaCorrenteFacade().consultarContaCorrenteCaixaPorNumero("", getValorConsultaUnidadeEnsino(), verificarPermissaoMovimentacaoContaCaixaParaContaCorrente(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			} else {
				resultadoConsulta = getFacadeFactory().getContaCorrenteFacade().consultarPorBancoPorNumeroContaCorrentePorDigitoContaCorrente(getConciliacaoContaCorrenteVO().getBanco().getNrBanco(), StringUtils.stripStart(getConciliacaoContaCorrenteVO().getContaCorrenteArquivo(), "0"), getConciliacaoContaCorrenteVO().getDigitoContaCorrenteArquivo(), "", 0, true, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
				Optional<ContaCorrenteVO> findFirst = resultadoConsulta.stream().filter(p -> p.getTipoContaCorrenteEnum().isCorrente()).findFirst();
				if (findFirst.isPresent()) {
					contaCorrenteTemp = findFirst.get();
				}
			}
			List<SelectItem> objs = new ArrayList<>();
			objs.add(new SelectItem(0, ""));
			resultadoConsulta.stream().forEach(p -> {
				if (Uteis.isAtributoPreenchido(p.getNomeApresentacaoSistema())) {
					objs.add(new SelectItem(p.getCodigo(), p.getNomeApresentacaoSistema()));
				} else {
					objs.add(new SelectItem(p.getCodigo(), p.getNomeBancoAgenciaContaApresentar()));
				}
			});

			if (resultadoConsulta.size() == 1 && !Uteis.isAtributoPreenchido(contaCorrenteTemp)) {
				contaCorrenteTemp = resultadoConsulta.get(0);
			}
			if (Uteis.isAtributoPreenchido(getParametrizarOperacoesAutomaticasConciliacaoItem().getNomeLancamento()) && Uteis.isAtributoPreenchido(contaCorrenteTemp)) {
				getParametrizarOperacoesAutomaticasConciliacaoItem().setContaCorrenteVO(contaCorrenteTemp);
			} else if (Uteis.isAtributoPreenchido(getMovimentacaoFinanceiraVO().getDescricao()) && Uteis.isAtributoPreenchido(contaCorrenteTemp)) {
				getMovimentacaoFinanceiraVO().setContaCorrenteOrigem(contaCorrenteTemp);
			} else if ((Uteis.isAtributoPreenchido(getContaReceber()) || Uteis.isAtributoPreenchido(getContaReceber().getDescricaoPagamento())) && Uteis.isAtributoPreenchido(contaCorrenteTemp)) {
				getContaReceber().setContaCorrenteVO(contaCorrenteTemp);
				getContaReceber().setContaCorrente(getContaReceber().getContaCorrenteVO().getCodigo());
				getFormaPagamentoNegociacaoRecebimentoVO().setContaCorrente(contaCorrenteTemp);
			} else if ((Uteis.isAtributoPreenchido(getContaPagar()) || Uteis.isAtributoPreenchido(getContaPagar().getDescricao())) && Uteis.isAtributoPreenchido(contaCorrenteTemp)) {
				getContaPagar().setContaCorrente(contaCorrenteTemp);
				getFormaPagamentoNegociacaoPagamentoVO().setContaCorrente(contaCorrenteTemp);
			}
			setListaSelectItemContaCorrente(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
		}
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox relativo ao atributo <code>FormaPagamento</code>.
	 */
	public void montarListaSelectItemContaCorrenteDestino() throws Exception {
		List<ContaCorrenteVO> resultadoConsulta = null;
		ContaCorrenteVO contaCorrenteTemp = new ContaCorrenteVO();
		try {
			if ((Uteis.isAtributoPreenchido(getConciliacaoContaCorrenteDiaExtratoVO().getTipoTransacaoOFXEnum()) && getConciliacaoContaCorrenteDiaExtratoVO().isTipoTransacaoSaida()) || (Uteis.isAtributoPreenchido(getParametrizarOperacoesAutomaticasConciliacaoItem().getNomeLancamento()) && getParametrizarOperacoesAutomaticasConciliacaoItem().getTipoMovimentacaoFinanceira().isMovimentacaoSaida())) {
				resultadoConsulta = getFacadeFactory().getContaCorrenteFacade().consultarContaCorrenteCaixaPorNumero("", getValorConsultaUnidadeEnsino(), verificarPermissaoMovimentacaoContaCaixaParaContaCorrente(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			} else {
				resultadoConsulta = getFacadeFactory().getContaCorrenteFacade().consultarPorBancoPorNumeroContaCorrentePorDigitoContaCorrente(getConciliacaoContaCorrenteVO().getBanco().getNrBanco(), StringUtils.stripStart(getConciliacaoContaCorrenteVO().getContaCorrenteArquivo(), "0"), getConciliacaoContaCorrenteVO().getDigitoContaCorrenteArquivo(), "", 0, true, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
				Optional<ContaCorrenteVO> findFirst = resultadoConsulta.stream().filter(p -> p.getTipoContaCorrenteEnum().isCorrente()).findFirst();
				if (findFirst.isPresent()) {
					contaCorrenteTemp = findFirst.get();
				}
			}
			List<SelectItem> objs = new ArrayList<>();
			objs.add(new SelectItem(0, ""));
			resultadoConsulta.stream()
					.forEach(p -> {
						if (Uteis.isAtributoPreenchido(p.getNomeApresentacaoSistema())) {
							objs.add(new SelectItem(p.getCodigo(), p.getNomeApresentacaoSistema()));
						} else {
							objs.add(new SelectItem(p.getCodigo(), p.getNomeBancoAgenciaContaApresentar()));
						}
					});
			if (resultadoConsulta.size() == 1 && !Uteis.isAtributoPreenchido(contaCorrenteTemp)) {
				contaCorrenteTemp = resultadoConsulta.get(0);
			}

			if (Uteis.isAtributoPreenchido(getParametrizarOperacoesAutomaticasConciliacaoItem().getNomeLancamento()) && Uteis.isAtributoPreenchido(contaCorrenteTemp)) {
				getParametrizarOperacoesAutomaticasConciliacaoItem().setContaCorrenteDestinoVO(contaCorrenteTemp);
			} else if (Uteis.isAtributoPreenchido(getMovimentacaoFinanceiraVO().getDescricao()) && Uteis.isAtributoPreenchido(contaCorrenteTemp)) {
				getMovimentacaoFinanceiraVO().setContaCorrenteDestino(contaCorrenteTemp);
			}
			setListaSelectItemContaCorrenteDestino(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
		}
	}

	public boolean verificarPermissaoMovimentacaoContaCaixaParaContaCorrente() {
		try {
			ControleAcesso.incluir("MovimentacaoContaCaixaContaCorrente", getUsuarioLogado());
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public void inicializarListasSelectItemTodosComboBox() throws Exception {
		montarListaSelectItemUnidadeEnsino();
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
	 */
	public void preencherFormaPagamento() {
		try {
			montarListaSelectItemFormaPagamento();
			getParametrizarOperacoesAutomaticasConciliacaoItem().setTipoFormaPagamento(getTipoFormaPagamento());
			getParametrizarOperacoesAutomaticasConciliacaoItem().setFormaPagamentoVO(new FormaPagamentoVO());
			getFormaPagamentoNegociacaoPagamentoVO().setFormaPagamento(new FormaPagamentoVO());
			getFormaPagamentoNegociacaoRecebimentoVO().setFormaPagamento(new FormaPagamentoVO());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void preencherContaCorrente() {
		try {
			if (Uteis.isAtributoPreenchido(getOrigemExtratoContaCorrente()) && getOrigemExtratoContaCorrente().isMovimentacaoFinanceira()) {
				montarListaSelectItemContaCorrenteDestino();
			}
			montarListaSelectItemContaCorrente();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void realizarGeracaoRelatorioExcelConciliacaoBancaria()  {
		try {
			File arquivo = getFacadeFactory().getConciliacaoContaCorrenteFacade().realizarValidacaoArquivoRetornoComExtratoContaCorrente(getConciliacaoContaCorrenteVO().getBanco().getNrBanco(), getConciliacaoContaCorrenteVO().getContaCorrenteArquivo(), getDataCredito(), getLogoPadraoRelatorio(), getUsuarioLogado());
			setCaminhoRelatorio(arquivo.getName());
			setFazerDownload(true);
			setMensagemID("msg_relatorio_ok");
		} catch (Exception e) {
			setFazerDownload(false);
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}		
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
	 */
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		verificarPermissaoIncluirConciliacaoContaCorrente();
		setListaConsulta(new ArrayList(0));
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("conciliacaoContaCorrenteCons.xhtml");
	}

	public void limparCampoConsulta() {
		getControleConsulta().setValorConsulta("");
		getControleConsulta().setDataIni(Uteis.getDataPrimeiroDiaMes(new Date()));
		getControleConsulta().setDataFim(Uteis.getDataUltimoDiaMes(new Date()));
	}

	public boolean getIsConsultaPorNumeroSelecionado() {
		return getControleConsulta().getCampoConsulta().equals("codigo");
	}

	public boolean getIsConsultaPorDataGeracaoSelecionado() {
		return getControleConsulta().getCampoConsulta().equals("dataGeracao");
	}

	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("contaCorrente", "Conta Corrente"));
		itens.add(new SelectItem("dataGeracao", "Data de Geração"));
		itens.add(new SelectItem("responsavel", "Responsável"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	public List<SelectItem> getTipoSacadoExtratoContaCorrenteEnum() {
		List<SelectItem> itens = new ArrayList<>();		
		itens.add(new SelectItem(TipoSacadoExtratoContaCorrenteEnum.FORNECEDOR, UteisJSF.internacionalizar("enum_TipoSacadoExtratoContaCorrenteEnum_FORNECEDOR")));
		itens.add(new SelectItem(TipoSacadoExtratoContaCorrenteEnum.FUNCIONARIO_PROFESSOR, UteisJSF.internacionalizar("enum_TipoSacadoExtratoContaCorrenteEnum_FUNCIONARIO_PROFESSOR")));
		itens.add(new SelectItem(TipoSacadoExtratoContaCorrenteEnum.PARCEIRO, UteisJSF.internacionalizar("enum_TipoSacadoExtratoContaCorrenteEnum_PARCEIRO")));
		if(Uteis.isAtributoPreenchido(getParametrizarOperacoesAutomaticasConciliacaoItem().getOrigemExtratoContaCorrenteEnum()) && getParametrizarOperacoesAutomaticasConciliacaoItem().getOrigemExtratoContaCorrenteEnum().isPagamento()){
			itens.add(new SelectItem(TipoSacadoExtratoContaCorrenteEnum.BANCO, UteisJSF.internacionalizar("enum_TipoSacadoExtratoContaCorrenteEnum_BANCO")));			
		}
		return itens;
	}

	public List<SelectItem> getListaSelectItemTipoSacado()  {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("FO", "Fornecedor"));
		itens.add(new SelectItem("BA", "Banco"));
		itens.add(new SelectItem("FU", "Funcionário"));
		itens.add(new SelectItem("PA", "Parceiro"));
		itens.add(new SelectItem("AL", "Aluno"));
		return itens;
	}

	public List<SelectItem> getListaSelectItemTipoPessoa()  {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("FO", "Fornecedor"));
		itens.add(new SelectItem("FU", "Funcionário"));
		itens.add(new SelectItem("PA", "Parceiro"));
		return itens;
	}

	public List<SelectItem> getComboboxOrigemExtratoContaCorrenteEnum() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem(OrigemExtratoContaCorrenteEnum.MOVIMENTACAO_FINANCEIRA, UteisJSF.internacionalizar("enum_OrigemExtratoContaCorrenteEnum_MOVIMENTACAO_FINANCEIRA")));
		if ((Uteis.isAtributoPreenchido(getConciliacaoContaCorrenteDiaExtratoVO().getTipoTransacaoOFXEnum()) && getConciliacaoContaCorrenteDiaExtratoVO().isTipoTransacaoEntrada()) || (Uteis.isAtributoPreenchido(getParametrizarOperacoesAutomaticasConciliacaoItem().getNomeLancamento()) && getParametrizarOperacoesAutomaticasConciliacaoItem().getTipoMovimentacaoFinanceira().isMovimentacaoEntrada())) {
			itens.add(new SelectItem(OrigemExtratoContaCorrenteEnum.RECEBIMENTO, UteisJSF.internacionalizar("enum_OrigemExtratoContaCorrenteEnum_RECEBIMENTO")));
		} else {
			itens.add(new SelectItem(OrigemExtratoContaCorrenteEnum.PAGAMENTO, UteisJSF.internacionalizar("enum_OrigemExtratoContaCorrenteEnum_PAGAMENTO")));
		}
		return itens;
	}

	public List<SelectItem> getComboboxTipoFormaPagamento() {
		List<SelectItem> itens = new ArrayList<>(0);
		if (Uteis.isAtributoPreenchido(getOrigemExtratoContaCorrente()) && getOrigemExtratoContaCorrente().isMovimentacaoFinanceira()) {
			itens.add(new SelectItem(TipoFormaPagamento.DINHEIRO, UteisJSF.internacionalizar("enum_TipoFormaPagamento_DINHEIRO")));
			itens.add(new SelectItem(TipoFormaPagamento.CHEQUE, UteisJSF.internacionalizar("enum_TipoFormaPagamento_CHEQUE")));
		} else if (Uteis.isAtributoPreenchido(getOrigemExtratoContaCorrente()) && (getOrigemExtratoContaCorrente().isPagamento() || getOrigemExtratoContaCorrente().isRecebimento())) {
			itens.add(new SelectItem(TipoFormaPagamento.BOLETO_BANCARIO, UteisJSF.internacionalizar("enum_TipoFormaPagamento_BOLETO_BANCARIO")));
			itens.add(new SelectItem(TipoFormaPagamento.DEPOSITO, UteisJSF.internacionalizar("enum_TipoFormaPagamento_DEPOSITO")));
			itens.add(new SelectItem(TipoFormaPagamento.DEBITO_EM_CONTA_CORRENTE, UteisJSF.internacionalizar("enum_TipoFormaPagamento_DEBITO_EM_CONTA_CORRENTE")));
		}
		return itens;
	}
	
	

	public Date getDataCredito() {
		if (dataCredito == null) {
			if(Uteis.isAtributoPreenchido(getConciliacaoContaCorrenteVO().getListaConciliacaoContaCorrenteDia())) {
				dataCredito = getConciliacaoContaCorrenteVO().getListaConciliacaoContaCorrenteDia().get(0).getData();
			}else {
				dataCredito = new Date();
			}
		}
		return dataCredito;
	}

	public void setDataCredito(Date dataCredito) {
		this.dataCredito = dataCredito;
	}

	public List<SelectItem> getListaSelectItemTipoNivelCentroResultadoEnum() {
		listaSelectItemTipoNivelCentroResultadoEnum = Optional.ofNullable(listaSelectItemTipoNivelCentroResultadoEnum).orElse(new ArrayList<>());
		return listaSelectItemTipoNivelCentroResultadoEnum;
	}

	public void setListaSelectItemTipoNivelCentroResultadoEnum(List<SelectItem> listaSelectItemTipoNivelCentroResultadoEnum) {
		this.listaSelectItemTipoNivelCentroResultadoEnum = listaSelectItemTipoNivelCentroResultadoEnum;
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

	public String getCampoConsultaCentroDespesa() {
		if (campoConsultaCentroDespesa == null) {
			campoConsultaCentroDespesa = "";
		}
		return campoConsultaCentroDespesa;
	}

	public void setCampoConsultaCentroDespesa(String campoConsultaCentroDespesa) {
		this.campoConsultaCentroDespesa = campoConsultaCentroDespesa;
	}

	public List<CategoriaDespesaVO> getListaConsultaCentroDespesa() {
		if (listaConsultaCentroDespesa == null) {
			listaConsultaCentroDespesa = new ArrayList<>(0);
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
		listaConsultaFuncionarioCentroCusto = Optional.ofNullable(listaConsultaFuncionarioCentroCusto).orElse(new ArrayList<>());
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

	public boolean isCentroResultadoAdministrativo() {
		return centroResultadoAdministrativo;
	}

	public boolean isPermiteAlterarCentroResultado() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoFinanceiroEnum.PERMITE_ALTERAR_CENTRO_RESULTADO_CONTA_PAGAR, getUsuarioLogado());
			return true;
		} catch (Exception e) {
			return false;
		}
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

	public List<CentroReceitaVO> getListaConsultaCentroReceita() {
		return listaConsultaCentroReceita;
	}

	public void setListaConsultaCentroReceita(List<CentroReceitaVO> listaConsultaCentroReceita) {
		this.listaConsultaCentroReceita = listaConsultaCentroReceita;
	}

	public String getValorConsultaCentroReceita() {
		return valorConsultaCentroReceita;
	}

	public void setValorConsultaCentroReceita(String valorConsultaCentroReceita) {
		this.valorConsultaCentroReceita = valorConsultaCentroReceita;
	}

	public String getCampoConsultaCentroReceita() {
		return campoConsultaCentroReceita;
	}

	public void setCampoConsultaCentroReceita(String campoConsultaCentroReceita) {
		this.campoConsultaCentroReceita = campoConsultaCentroReceita;
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

	public List<SelectItem> getListaSelectItemUnidadeEnsinoCategoriaDespesa() {
		listaSelectItemUnidadeEnsinoCategoriaDespesa = Optional.ofNullable(listaSelectItemUnidadeEnsinoCategoriaDespesa).orElse(new ArrayList<>());
		return listaSelectItemUnidadeEnsinoCategoriaDespesa;
	}

	public void setListaSelectItemUnidadeEnsinoCategoriaDespesa(List<SelectItem> listaSelectItemUnidadeEnsinoCategoriaDespesa) {
		this.listaSelectItemUnidadeEnsinoCategoriaDespesa = listaSelectItemUnidadeEnsinoCategoriaDespesa;
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

	public List getListaSelectItemContaCorrenteDestino() {
		if (listaSelectItemContaCorrenteDestino == null) {
			listaSelectItemContaCorrenteDestino = new ArrayList(0);
		}
		return listaSelectItemContaCorrenteDestino;
	}

	public void setListaSelectItemContaCorrenteDestino(List listaSelectItemContaCorrenteDestino) {
		this.listaSelectItemContaCorrenteDestino = listaSelectItemContaCorrenteDestino;
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

	public ConciliacaoContaCorrenteDiaVO getConciliacaoContaCorrenteDiaVO() {
		if (conciliacaoContaCorrenteDiaVO == null) {
			conciliacaoContaCorrenteDiaVO = new ConciliacaoContaCorrenteDiaVO();
		}
		return conciliacaoContaCorrenteDiaVO;
	}

	public void setConciliacaoContaCorrenteDiaVO(ConciliacaoContaCorrenteDiaVO conciliacaoContaCorrenteDiaVO) {
		this.conciliacaoContaCorrenteDiaVO = conciliacaoContaCorrenteDiaVO;
	}

	public ConciliacaoContaCorrenteDiaExtratoVO getConciliacaoContaCorrenteDiaExtratoVO() {
		if (conciliacaoContaCorrenteDiaExtratoVO == null) {
			conciliacaoContaCorrenteDiaExtratoVO = new ConciliacaoContaCorrenteDiaExtratoVO();
		}
		return conciliacaoContaCorrenteDiaExtratoVO;
	}

	public void setConciliacaoContaCorrenteDiaExtratoVO(ConciliacaoContaCorrenteDiaExtratoVO conciliacaoContaCorrenteDiaExtratoVO) {
		this.conciliacaoContaCorrenteDiaExtratoVO = conciliacaoContaCorrenteDiaExtratoVO;
	}

	public ParametrizarOperacoesAutomaticasConciliacaoItemVO getParametrizarOperacoesAutomaticasConciliacaoItem() {
		if (parametrizarOperacoesAutomaticasConciliacaoItem == null) {
			parametrizarOperacoesAutomaticasConciliacaoItem = new ParametrizarOperacoesAutomaticasConciliacaoItemVO();
		}
		return parametrizarOperacoesAutomaticasConciliacaoItem;
	}

	public void setParametrizarOperacoesAutomaticasConciliacaoItem(ParametrizarOperacoesAutomaticasConciliacaoItemVO parametrizarOperacoesAutomaticasConciliacaoItem) {
		this.parametrizarOperacoesAutomaticasConciliacaoItem = parametrizarOperacoesAutomaticasConciliacaoItem;
	}

	public List<SelectItem> getListaSelectItemTipoFormaPagamento() {
		return UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoFormaPagamento.class, false);
	}

	public String getAbaParametrizarOperacoesAutomaticasConciliacao() {
		if (abaParametrizarOperacoesAutomaticasConciliacao == null) {
			abaParametrizarOperacoesAutomaticasConciliacao = "";
		}
		return abaParametrizarOperacoesAutomaticasConciliacao;
	}

	public void setAbaParametrizarOperacoesAutomaticasConciliacao(String abaParametrizarOperacoesAutomaticasConciliacao) {
		this.abaParametrizarOperacoesAutomaticasConciliacao = abaParametrizarOperacoesAutomaticasConciliacao;
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

	public List<ParceiroVO> getListaConsultaParceiro() {
		if (listaConsultaParceiro == null) {
			listaConsultaParceiro = new ArrayList<>();
		}
		return listaConsultaParceiro;
	}

	public void setListaConsultaParceiro(List<ParceiroVO> listaConsultaParceiro) {
		this.listaConsultaParceiro = listaConsultaParceiro;
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

	public List<FuncionarioVO> getListaConsultaFuncionario() {
		if (listaConsultaFuncionario == null) {
			listaConsultaFuncionario = new ArrayList<>();
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

	public List getListaConsultaBanco() {
		if (listaConsultaBanco == null) {
			listaConsultaBanco = new ArrayList<>();
		}
		return listaConsultaBanco;
	}

	public void setListaConsultaBanco(List listaConsultaBanco) {
		this.listaConsultaBanco = listaConsultaBanco;
	}

	public String getValorConsultaBanco() {
		if (valorConsultaBanco == null) {
			valorConsultaBanco = "";
		}
		return valorConsultaBanco;
	}

	public void setValorConsultaBanco(String valorConsultaBanco) {
		this.valorConsultaBanco = valorConsultaBanco;
	}

	public String getCampoConsultaBanco() {
		if (campoConsultaBanco == null) {
			campoConsultaBanco = "";
		}
		return campoConsultaBanco;
	}

	public void setCampoConsultaBanco(String campoConsultaBanco) {
		this.campoConsultaBanco = campoConsultaBanco;
	}

	public String getMascaraConsultaFornecedor() {
		if (getCampoConsultaFornecedor().equals("CNPJ")) {
			return "return mascara(this.form, 'formFornecedor:valorConsultaFornecedor', '99.999.999/9999-99', event);";
		} else if (getCampoConsultaFornecedor().equals("CPF")) {
			return "return mascara(this.form, 'formFornecedor:valorConsultaFornecedor', '999.999.999-99', event);";
		}
		return "";
	}

	public String getTituloPanelLocalizacaoDocumentoSei() {
		if (tituloPanelLocalizacaoDocumentoSei == null) {
			tituloPanelLocalizacaoDocumentoSei = "";
		}
		return tituloPanelLocalizacaoDocumentoSei;
	}

	public void setTituloPanelLocalizacaoDocumentoSei(String tituloPanelLocalizacaoDocumentoSei) {
		this.tituloPanelLocalizacaoDocumentoSei = tituloPanelLocalizacaoDocumentoSei;
	}

	public ContaReceberVO getContaReceber() {
		if (contaReceber == null) {
			contaReceber = new ContaReceberVO();
		}
		return contaReceber;
	}

	public void setContaReceber(ContaReceberVO contaReceber) {
		this.contaReceber = contaReceber;
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

	public Boolean getConsDataCompetencia() {
		if (consDataCompetencia == null) {
			consDataCompetencia = Boolean.FALSE;
		}
		return consDataCompetencia;
	}

	public void setConsDataCompetencia(Boolean consDataCompetencia) {
		this.consDataCompetencia = consDataCompetencia;
	}

	public int getValorConsultaUnidadeEnsino() {
		return valorConsultaUnidadeEnsino;
	}

	public void setValorConsultaUnidadeEnsino(int valorConsultaUnidadeEnsino) {
		this.valorConsultaUnidadeEnsino = valorConsultaUnidadeEnsino;
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

	public String getSemestre() {
		if (semestre == null) {
			semestre = "";
		}
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
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

	public String getValorConsultaReceberRecebidoNegociado() {
		if (valorConsultaReceberRecebidoNegociado == null) {
			valorConsultaReceberRecebidoNegociado = "AR";
		}
		return valorConsultaReceberRecebidoNegociado;
	}

	public void setValorConsultaReceberRecebidoNegociado(String valorConsultaReceberRecebidoNegociado) {
		this.valorConsultaReceberRecebidoNegociado = valorConsultaReceberRecebidoNegociado;
	}

	public Boolean getFiltrarTodoPeriodo() {
		if (filtrarTodoPeriodo == null) {
			filtrarTodoPeriodo = Boolean.FALSE;
		}
		return filtrarTodoPeriodo;
	}

	public void setFiltrarTodoPeriodo(Boolean filtrarTodoPeriodo) {
		this.filtrarTodoPeriodo = filtrarTodoPeriodo;
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

	public ContaPagarVO getContaPagar() {
		if (contaPagar == null) {
			contaPagar = new ContaPagarVO();
		}
		return contaPagar;
	}

	public void setContaPagar(ContaPagarVO contaPagar) {
		this.contaPagar = contaPagar;
	}

	public String getValorConsultaSituacaoFinanceiraDaConta() {
		if(valorConsultaSituacaoFinanceiraDaConta == null ) {
			valorConsultaSituacaoFinanceiraDaConta = "AP";
		}
		return valorConsultaSituacaoFinanceiraDaConta;
	}

	public void setValorConsultaSituacaoFinanceiraDaConta(String valorConsultaSituacaoFinanceiraDaConta) {
		this.valorConsultaSituacaoFinanceiraDaConta = valorConsultaSituacaoFinanceiraDaConta;
	}

	public Boolean getFiltrarDataFactorGerador() {
		if (filtrarDataFactorGerador == null) {
			filtrarDataFactorGerador = Boolean.FALSE;
		}
		return filtrarDataFactorGerador;
	}

	public void setFiltrarDataFactorGerador(Boolean filtrarDataFactorGerador) {
		this.filtrarDataFactorGerador = filtrarDataFactorGerador;
	}

	public FormaPagamentoNegociacaoPagamentoVO getFormaPagamentoNegociacaoPagamentoVO() {
		if (formaPagamentoNegociacaoPagamentoVO == null) {
			formaPagamentoNegociacaoPagamentoVO = new FormaPagamentoNegociacaoPagamentoVO();
		}
		return formaPagamentoNegociacaoPagamentoVO;
	}

	public void setFormaPagamentoNegociacaoPagamentoVO(FormaPagamentoNegociacaoPagamentoVO formaPagamentoNegociacaoPagamentoVO) {
		this.formaPagamentoNegociacaoPagamentoVO = formaPagamentoNegociacaoPagamentoVO;
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

	public ConfiguracaoRecebimentoCartaoOnlineVO getConfiguracaoRecebimentoCartaoOnlineVO() {
		if (configuracaoRecebimentoCartaoOnlineVO == null) {
			configuracaoRecebimentoCartaoOnlineVO = new ConfiguracaoRecebimentoCartaoOnlineVO();
		}
		return configuracaoRecebimentoCartaoOnlineVO;
	}

	public void setConfiguracaoRecebimentoCartaoOnlineVO(ConfiguracaoRecebimentoCartaoOnlineVO configuracaoRecebimentoCartaoOnlineVO) {
		this.configuracaoRecebimentoCartaoOnlineVO = configuracaoRecebimentoCartaoOnlineVO;
	}

	public void apresentarDicaCVCartaoCredito() {
		getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setApresentarDicaCVCartaoCredito(true);
	}

	public void esconderDicaCVCartaoCredito() {
		getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setApresentarDicaCVCartaoCredito(false);
	}

	public List<SelectItem> getListaSelectItemQuantidadeParcelas() {
		if (listaSelectItemQuantidadeParcelas == null) {
			listaSelectItemQuantidadeParcelas = new ArrayList<SelectItem>();
		}
		return listaSelectItemQuantidadeParcelas;
	}

	public void setListaSelectItemQuantidadeParcelas(List<SelectItem> listaSelectItemQuantidadeParcelas) {
		this.listaSelectItemQuantidadeParcelas = listaSelectItemQuantidadeParcelas;
	}

	public String getManterPanelPagamentoRecebimentoConta() {
		if (manterPanelPagamentoRecebimentoConta == null) {
			manterPanelPagamentoRecebimentoConta = "";
		}
		return manterPanelPagamentoRecebimentoConta;
	}

	public void setManterPanelPagamentoRecebimentoConta(String manterPanelPagamentoRecebimentoConta) {
		this.manterPanelPagamentoRecebimentoConta = manterPanelPagamentoRecebimentoConta;
	}

	public String getCampoNavegacaoEstornoConciliacaoExtratoDia() {
		if (campoNavegacaoEstornoConciliacaoExtratoDia == null) {
			campoNavegacaoEstornoConciliacaoExtratoDia = "";
		}
		return campoNavegacaoEstornoConciliacaoExtratoDia;
	}

	public void setCampoNavegacaoEstornoConciliacaoExtratoDia(String campoNavegacaoEstornoConciliacaoExtratoDia) {
		this.campoNavegacaoEstornoConciliacaoExtratoDia = campoNavegacaoEstornoConciliacaoExtratoDia;
	}

	public OrigemExtratoContaCorrenteEnum getOrigemExtratoContaCorrente() {
		return origemExtratoContaCorrente;
	}

	public void setOrigemExtratoContaCorrente(OrigemExtratoContaCorrenteEnum origemExtratoContaCorrente) {
		this.origemExtratoContaCorrente = origemExtratoContaCorrente;
	}

	public TipoFormaPagamento getTipoFormaPagamento() {
		return tipoFormaPagamento;
	}

	public void setTipoFormaPagamento(TipoFormaPagamento tipoFormaPagamento) {
		this.tipoFormaPagamento = tipoFormaPagamento;
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

	public String getApresentarModalOperacaoEstorno() {
		if (apresentarModalOperacaoEstorno == null) {
			apresentarModalOperacaoEstorno = "";
		}
		return apresentarModalOperacaoEstorno;
	}

	public void setApresentarModalOperacaoEstorno(String apresentarModalOperacaoEstorno) {
		this.apresentarModalOperacaoEstorno = apresentarModalOperacaoEstorno;
	}

	public ExtratoContaCorrenteVO getExtratoContaCorrenteEstorno() {
		if (extratoContaCorrenteEstorno == null) {
			extratoContaCorrenteEstorno = new ExtratoContaCorrenteVO();
		}
		return extratoContaCorrenteEstorno;
	}

	public void setExtratoContaCorrenteEstorno(ExtratoContaCorrenteVO extratoContaCorrenteEstorno) {
		this.extratoContaCorrenteEstorno = extratoContaCorrenteEstorno;
	}

	public List<SelectItem> getListaSelectItemAnoValidade() {
		List<SelectItem> listaSelectItemAnoValidade = new ArrayList<SelectItem>();
		Integer anoAtual = Integer.parseInt(Uteis.getAnoDataAtual());
		for (int i = 0; i < 11; i++) {
			listaSelectItemAnoValidade.add(new SelectItem(anoAtual.toString(), anoAtual.toString()));
			anoAtual++;
		}
		return listaSelectItemAnoValidade;
	}

	public List<SelectItem> getListaSelectItemMesValidade() {
		List<SelectItem> listaSelectItemMesValidade = new ArrayList<SelectItem>();
		for (Integer i = 1; i < 13; i++) {
			listaSelectItemMesValidade.add(new SelectItem(i.toString(), i.toString()));
		}

		return listaSelectItemMesValidade;
	}

	public void limparFiltroListaDataTransacao() {
		setFiltroListaDataConciliacao(null);
		getConciliacaoContaCorrenteVO().setConciliacaoDiaSelecionada(new ConciliacaoContaCorrenteDiaVO());
	}

	public boolean isFiltroListaDataConciliacaoExistente() {
		return Uteis.isAtributoPreenchido(getFiltroListaDataConciliacao());
	}

	public String getFiltroListaDataConciliacao_Apresentar() {
		if (isFiltroListaDataConciliacaoExistente()) {
			return UteisData.getData(getFiltroListaDataConciliacao());
		}
		return "";
	}

	public Date getFiltroListaDataConciliacao() {
		return filtroListaDataConciliacao;
	}

	public void setFiltroListaDataConciliacao(Date filtroListaDataConciliacao) {
		this.filtroListaDataConciliacao = filtroListaDataConciliacao;
	}

	public String getApresentarModalLocalizacaoDocumentoSei() {
		if (apresentarModalLocalizacaoDocumentoSei == null) {
			apresentarModalLocalizacaoDocumentoSei = "";
		}
		return apresentarModalLocalizacaoDocumentoSei;
	}

	public void setApresentarModalLocalizacaoDocumentoSei(String apresentarModalLocalizacaoDocumentoSei) {
		this.apresentarModalLocalizacaoDocumentoSei = apresentarModalLocalizacaoDocumentoSei;
	}

	public String getApresentarModalValoresAproximado() {
		if (apresentarModalValoresAproximado == null) {
			apresentarModalValoresAproximado = "";
		}
		return apresentarModalValoresAproximado;
	}

	public void setApresentarModalValoresAproximado(String apresentarModalValoresAproximado) {
		this.apresentarModalValoresAproximado = apresentarModalValoresAproximado;
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

	public NegociacaoPagamentoVO getNegociacaoPagamentoVO() {
		if (negociacaoPagamentoVO == null) {
			negociacaoPagamentoVO = new NegociacaoPagamentoVO();
		}
		return negociacaoPagamentoVO;
	}

	public void setNegociacaoPagamentoVO(NegociacaoPagamentoVO negociacaoPagamentoVO) {
		this.negociacaoPagamentoVO = negociacaoPagamentoVO;
	}

	public boolean filtrarDataTransacaoConciliacaoDia(Object obj) {
		ConciliacaoContaCorrenteDiaVO conciliacaoDia = null;
		try {
			if (isFiltroListaDataConciliacaoExistente()) {
				if (obj instanceof ConciliacaoContaCorrenteDiaVO) {
					conciliacaoDia = (ConciliacaoContaCorrenteDiaVO) obj;
					if (Uteis.isAtributoPreenchido(conciliacaoDia.getData()) && UteisData.getCompararDatas(conciliacaoDia.getData(), getFiltroListaDataConciliacao())) {
						return true;
					}
					return false;
				} else {
					return false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conciliacaoDia = null;
		}
		return true;
	}	
	
	public Boolean getPermitirIncluirConciliacaoContaCorrente() {
		if(permitirIncluirConciliacaoContaCorrente == null) {
			permitirIncluirConciliacaoContaCorrente = Boolean.FALSE;
		}
		return permitirIncluirConciliacaoContaCorrente;
	}

	public void setPermitirIncluirConciliacaoContaCorrente(Boolean permitirIncluirConciliacaoContaCorrente) {
		this.permitirIncluirConciliacaoContaCorrente = permitirIncluirConciliacaoContaCorrente;
	}

	public Boolean getPermitirAbrirConciliacaoContaCorrente() {
		if(permitirAbrirConciliacaoContaCorrente == null) {
			permitirAbrirConciliacaoContaCorrente = Boolean.FALSE;
		}
		return permitirAbrirConciliacaoContaCorrente;
	}

	public void setPermitirAbrirConciliacaoContaCorrente(Boolean permitirAbrirConciliacaoContaCorrente) {
		this.permitirAbrirConciliacaoContaCorrente = permitirAbrirConciliacaoContaCorrente;
	}

	public void verificarPermissaoIncluirConciliacaoContaCorrente() {
		try{
			ConciliacaoContaCorrente.incluir("ConciliacaoContaCorrente", getUsuarioLogado());
			setPermitirIncluirConciliacaoContaCorrente(Boolean.TRUE);
		}catch(Exception e){
			setPermitirIncluirConciliacaoContaCorrente(Boolean.FALSE);
		}
	}
	
	public void verificarPermissaoAbrirConciliacaoContaCorrente() {
		try {
			 if(ControleAcesso.verificarPermissaoFuncionalidadeUsuario("AbrirConciliacaoContaCorrente", getUsuarioLogado())) {
				 setPermitirAbrirConciliacaoContaCorrente(Boolean.TRUE);
			 }
		} catch (Exception e) {
			setPermitirAbrirConciliacaoContaCorrente(Boolean.FALSE);
		}
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

	public String getCampoConsultaAluno() {
		if (campoConsultaAluno == null) {
			campoConsultaAluno = "";
		}
		return campoConsultaAluno;
	}

	public void setCampoConsultaAluno(String campoConsultaAluno) {
		this.campoConsultaAluno = campoConsultaAluno;
	}

	public List<MatriculaVO> getListaConsultaAluno() {
		if (listaConsultaAluno == null) {
			listaConsultaAluno = new ArrayList<>();
		}
		return listaConsultaAluno;
	}

	public void setListaConsultaAluno(List<MatriculaVO> listaConsultaAluno) {
		this.listaConsultaAluno = listaConsultaAluno;
	}
	
	public List<SelectItem> getTipoConsultaComboAluno() {
    	if(tipoConsultaComboAluno == null) {
    		tipoConsultaComboAluno = new ArrayList<SelectItem>(0);
    		tipoConsultaComboAluno.add(new SelectItem("nomePessoa", "Aluno"));
    		tipoConsultaComboAluno.add(new SelectItem("matricula", "Matrícula"));
    	}
        return tipoConsultaComboAluno;
    }
}
