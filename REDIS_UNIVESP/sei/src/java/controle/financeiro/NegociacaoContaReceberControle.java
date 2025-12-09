package controle.financeiro;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas negociacaoContaReceberForm.jsp
 * negociacaoContaReceberCons.jsp) com as funcionalidades da classe <code>NegociacaoContaReceber</code>. Implemtação da
 * camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see NegociacaoContaReceber
 * @see NegociacaoContaReceberVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle.MSG_TELA;
import negocio.comuns.academico.DescontoProgressivoVO;
import negocio.comuns.academico.ItemPlanoFinanceiroAlunoVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PlanoDescontoVO;
import negocio.comuns.academico.TextoPadraoDeclaracaoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.Obrigatorio;
import negocio.comuns.arquitetura.enumeradores.OperacaoFuncionalidadeEnum;
import negocio.comuns.arquitetura.enumeradores.OrigemOperacaoFuncionalidadeEnum;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoFinanceiroEnum;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.financeiro.CentroReceitaVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ContaReceberNegociadoVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.ItemCondicaoDescontoRenegociacaoVO;
import negocio.comuns.financeiro.ItemCondicaoRenegociacaoVO;
import negocio.comuns.financeiro.NegociacaoContaReceberPlanoDescontoVO;
import negocio.comuns.financeiro.NegociacaoContaReceberVO;
import negocio.comuns.financeiro.OpcaoAlunoCondicaoRenegociacaoVO;
import negocio.comuns.financeiro.OpcaoPagamentoDividaVO;
import negocio.comuns.financeiro.OrdemDescontoVO;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.financeiro.PerfilEconomicoVO;
import negocio.comuns.financeiro.enumerador.TipoAcrescimoEnum;
import negocio.comuns.financeiro.enumerador.TipoIntervaloParcelaEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.SituacaoContaReceber;
import negocio.comuns.utilitarias.dominios.TipoDescontoAluno;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.financeiro.ConfiguracaoFinanceiro;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.controle.financeiro.BoletosRelControle;
import relatorio.controle.financeiro.TermoReconhecimentoDividaRelControle;
import relatorio.negocio.comuns.financeiro.BoletoBancarioRelVO;

@Controller("NegociacaoContaReceberControle")
@Scope("viewScope")
@Lazy
public class NegociacaoContaReceberControle extends SuperControleRelatorio implements Serializable {

	 /**
		 * 
		 */
	private static final long serialVersionUID = 7722627309519956515L;
    private NegociacaoContaReceberVO negociacaoContaReceberVO;
    private List<SelectItem> listaSelectItemUnidadeEnsino;
    private List<SelectItem> listaSelectItemContaCorrente;
    private List<FuncionarioVO> listaConsultaFuncionario;
    private String valorConsultaFuncionario;
    private String campoConsultaFuncionario;
    private List<MatriculaVO> listaConsultaAluno;
    private String valorConsultaAluno;
    private String campoConsultaAluno;
    private List<CentroReceitaVO> listaConsultaCentroReceita;
    private String valorConsultaCentroReceita;
    private String campoConsultaCentroReceita;
    private List<ContaReceberVO> listaConsultaContaReceber;
    private String valorConsultaContaReceber;
    private String campoConsultaContaReceber;
    private String situacaoContaReceber;
    private ContaReceberNegociadoVO contaReceberNegociadoVO;
//    private String tipoDesconto;
    private String abrirModalRenegociacao;
    private List<SelectItem> listaSelectItemDescontoProgressivo;
    private List<MatriculaVO> listaAlunosParceiro;
    private List<ParceiroVO> listaConsultaParceiro;
    private String valorConsultaParceiro;
    private String campoConsultaParceiro;
    private TermoReconhecimentoDividaRelControle termoReconhecimentoDividaRelControle;
    private String observacaoHistorico;
    private BoletosRelControle boletosRelControle;
    private Double valorTotalApresentar;
    private String descricaoPeriodoLetivoAluno;
    private String tipoBoleto;
    protected List<FornecedorVO> listaConsultaFornecedor;
	protected String valorConsultaFornecedor;
	protected String campoConsultaFornecedor;
	protected String usernameLiberarDesconto;
	protected String senhaLiberarDesconto;
	public Boolean apresentarBotaoLiberarDesconto;
	private String tipoLayout;
    private List<SelectItem> listaSelectItemTipoTextoPadrao;
    private Integer textoPadraoDeclaracao;
    private List<SelectItem> listaSelectItemCaixa;
    public String apresentarModalEstornoRenegociacao;
    public Boolean desconsiderarDescontoProgressivo; 
    public Boolean desconsiderarDescontoAluno;
    public Boolean desconsiderarDescontoInstituicaoComValidade;
    public Boolean desconsiderarDescontoInstituicaoSemValidade;
    private TipoAcrescimoEnum tipoAcrescimo;
    private Double acrescimoPorParcela;
    private Double valorBaseParcela;
    private List<SelectItem> listaSelectItemTipoAcrescimo;
    private List<SelectItem> listaSelectItemPlanoDesconto;
    private Integer planoDescontoAdicionar;
    private ContaReceberVO contaReceberVO;
	private String oncompleteImpressaoBoleto;
	private List<SelectItem> listaSelectItemOpcaoRenegociacao;
    private Boolean apresentarDadosOpcaoRenegociacao;
    private Boolean permitirInformarDescontoProg_Inst;
    private Boolean permitirLiberarRenovacaoAposPagamento;
    private List<ContaReceberVO> listaContaReceberVOs;
    private String userNameLiberarUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao;
    private String senhaLiberarUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao;
    
    private String userNameLiberarRenegociacaoParaNaoValidarCondicaoRenegociacao;
    private String senhaLiberarRenegociacaoParaNaoValidarCondicaoRenegociacao;
    
    private String userNameLiberarFuncionalidade;
	private String senhaLiberarFuncionalidade;
    
    private boolean apresentarValorIndiceReajustePorAtraso = false;
    private String habilitarModalContaReceber;
    private String responsavelCadastro;
    private String comissionado;
    private Integer unidadeEnsinoConsulta;
    
    private String observacaoComplementar;
    private Boolean permitirNegociarParcelasNegociacaoNaoCumprida;
    private String msgNegociarParcelasNegociacaoNaoCumprida;
    
    private List<SelectItem> listaSelectItemItemCondicaoRenegociacao;
    private List<ContaReceberVO> contaReceberVOs;
    private List<SelectItem> listaSelectItemAgenteNegativacaoCobranca;
    
    public NegociacaoContaReceberControle() throws Exception {
        setMensagemID("msg_entre_prmconsulta");
        verificarPermissaoRenegociacaoApenasComCondicaoRenegociacao();
        verificarPermissaoInformarDescontoProg_Inst();
        verificarPermissaoLiberarRenovacao();
        setControleConsultaOtimizado(new DataModelo());
        getControleConsultaOtimizado().setLimitePorPagina(10);
        getControleConsultaOtimizado().setPage(0);
        getControleConsultaOtimizado().setPaginaAtual(0);
        getControleConsultaOtimizado().setDataIni(Uteis.obterDataAntiga(new Date(), 30));
    }

    @PostConstruct
    public void negociacaoApartirInteracaoWorkflow() {
        try {        	            
            String matricula = (String) context().getExternalContext().getSessionMap().get("matricula");
			if (matricula != null && !matricula.trim().equals("")) {
				novo();
				this.getNegociacaoContaReceberVO().setTipoPessoa("AL");
				getNegociacaoContaReceberVO().getMatriculaAluno().setMatricula(matricula);
				consultarAlunoPorMatricula();
				montarListaSelectItemContaCorrente();
				context().getExternalContext().getSessionMap().remove("matricula");
			}
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
    
    @PostConstruct
	public void realizarRenegociacaoContaReceberVindoTelaFichaAluno() {
		MatriculaVO obj = (MatriculaVO) context().getExternalContext().getSessionMap().get("matriculaRenegociacaoContaReceberFichaAluno");
		if (obj != null && !obj.getMatricula().equals("")) {
			try {
				novo();
				getNegociacaoContaReceberVO().setTipoPessoa(TipoPessoa.ALUNO.getValor());
				getNegociacaoContaReceberVO().getMatriculaAluno().setMatricula(obj.getMatricula());
				getNegociacaoContaReceberVO().getMatriculaAluno().getAluno().setCodigo(obj.getAluno().getCodigo());
				getNegociacaoContaReceberVO().getMatriculaAluno().getAluno().setNome(obj.getAluno().getNome());
				getNegociacaoContaReceberVO().getUnidadeEnsino().setCodigo(obj.getUnidadeEnsino().getCodigo());
				getNegociacaoContaReceberVO().getUnidadeEnsino().setNome(obj.getUnidadeEnsino().getNome());
				getNegociacaoContaReceberVO().getMatriculaAluno().getCurso().setNome(obj.getCurso().getNome());
				MatriculaPeriodoVO matriculaPeriodoVO = getFacadeFactory().getMatriculaPeriodoFacade().consultaUltimaMatriculaPeriodoPorMatricula(obj.getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(obj.getUnidadeEnsino().getCodigo()), getUsuarioLogado());
	            setDescricaoPeriodoLetivoAluno(matriculaPeriodoVO.getPeridoLetivo().getDescricao());
				realizarInclusaoContasVencidasFichaAluno(obj.getListaContaReceberVOs());
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			} finally {
				context().getExternalContext().getSessionMap().remove("matriculaRenegociacaoContaReceberFichaAluno");
			}
		}
	}

	public void realizarInclusaoContasVencidasFichaAluno(List<ContaReceberVO> listaContaReceberVOs) throws Exception {
		ConfiguracaoFinanceiroVO conf;
		conf = getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getNegociacaoContaReceberVO().getUnidadeEnsino().getCodigo());
		for (ContaReceberVO obj : listaContaReceberVOs) {
			if (Uteis.getDataJDBC(obj.getDataVencimento()).compareTo(Uteis.getDataJDBC(new Date())) < 0 && obj.getSituacao().equals(SituacaoContaReceber.A_RECEBER.getValor())) {
				getFacadeFactory().getContaReceberFacade().carregarDados(obj, NivelMontarDados.TODOS, conf, getUsuarioLogado());
				obj.setRealizandoRecebimento(true);
				obj.setValorFinalCalculado(obj.getCalcularValorFinal(null, conf, false, new Date(), getUsuarioLogado()));
				getContaReceberNegociadoVO().setContaReceber(obj);
				adicionarContaReceberNegociado(conf);
			}
		}
	}
    
    /**
     * Rotina responsável por disponibilizar um novo objeto da classe <code>NegociacaoContaReceber</code> para edição
     * pelo usuário da aplicação.
     */
    public String novo() {
        removerObjetoMemoria(this);
        try {
            setNegociacaoContaReceberVO(null);
            inicializarListasSelectItemTodosComboBox();
            setContaReceberNegociadoVO(null);
            incializarResponsavel();
            incializarConfiguracaoFinanceiro();
            executarVerificacaoUsuarioLogadoPodeLiberarDesconto();
            setTipoAcrescimo(TipoAcrescimoEnum.PORCENTAGEM);
            setAcrescimoPorParcela(0.0);
            setValorBaseParcela(0.0);
            setApresentarValorIndiceReajustePorAtraso(false);
            verificarPermissaoRenegociacaoApenasComCondicaoRenegociacao();
            verificarPermissaoInformarDescontoProg_Inst();
            verificarPermissaoLiberarRenovacao();
            setMensagemID("msg_entre_dados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("negociacaoContaReceberForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("negociacaoContaReceberForm.xhtml");
        }

    }

    public void incializarConfiguracaoFinanceiro() {
        try {
//            ConfiguracaoFinanceiroVO obj = getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), null);
            ConfiguracaoFinanceiroVO obj = getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getUnidadeEnsinoLogado().getCodigo());
            ConfiguracaoFinanceiro.montarDadosCentroReceitaNegociacaoPadrao(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            getNegociacaoContaReceberVO().setCentroReceita(obj.getCentroReceitaNegociacaoPadrao());
            getNegociacaoContaReceberVO().getContaCorrente().setCodigo(obj.getContaCorrentePadraoNegociacao());
        } catch (Exception e) {
        }
    }

    public void incializarResponsavel() {
        try {
            getNegociacaoContaReceberVO().getResponsavel().setCodigo(getUsuarioLogado().getCodigo());
            getNegociacaoContaReceberVO().getResponsavel().setNome(getUsuarioLogado().getNome());
            getNegociacaoContaReceberVO().getPessoaComissionada().setCodigo(getUsuarioLogado().getPessoa().getCodigo());
            getNegociacaoContaReceberVO().getPessoaComissionada().setNome(getUsuarioLogado().getPessoa().getNome());
        } catch (Exception e) {
        }
    }
    
    
    public void alterarPermitirPagamentoCartaoCredito() {
    	try {
			getFacadeFactory().getNegociacaoContaReceberFacade().alterarPermitirPagamentoCartaoCredito(getNegociacaoContaReceberVO(), getUsuarioLogado());
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			 setMensagemDetalhada("msg_erro", e.getMessage());
		}
    }
    
    public void abrirModalConfirmacaoAlteracao() {
    	if(Uteis.isAtributoPreenchido(getNegociacaoContaReceberVO().getCodigo())) {
    		setOncompleteModal("RichFaces.$('panelPermitirPagamentoCartaoCredito').show()");
    	}else {
    		setOncompleteModal("");
    	}
    }

    /**
     * Rotina responsável por disponibilizar os dados de um objeto da classe <code>NegociacaoContaReceber</code> para
     * alteração. O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente
     * possa disponibilizá-lo para edição.
     */
    public String editar() {
        try {
            NegociacaoContaReceberVO obj = (NegociacaoContaReceberVO) context().getExternalContext().getRequestMap().get("negociacaoContaReceberItens");
            NegociacaoContaReceberVO negociacaoContaReceberVOLocal = getFacadeFactory().getNegociacaoContaReceberFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(obj.getUnidadeEnsino().getCodigo()), getUsuarioLogado());
            setNegociacaoContaReceberVO(negociacaoContaReceberVOLocal);
            MatriculaPeriodoVO matriculaPeriodoVO = getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaBasicaUltimaMatriculaPeriodoPorMatricula(obj.getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            setDescricaoPeriodoLetivoAluno(matriculaPeriodoVO.getPeridoLetivo().getDescricao());
            inicializarListasSelectItemTodosComboBox();
            executarVerificacaoUsuarioLogadoPodeLiberarDesconto();
			getNegociacaoContaReceberVO().setLiberarIsencaoJuroMultaDescontoAcimaMaximo(getFacadeFactory().getOperacaoFuncionalidadeFacade().consultarPorCodigoOrigemPorOrigemOperacaoFuncionalidadePorOperacaoFuncionalidade(getNegociacaoContaReceberVO().getCodigo(), OrigemOperacaoFuncionalidadeEnum.NEGOCIACAO_CONTA_RECEBER, OperacaoFuncionalidadeEnum.NEGOCIACAO_CONTA_RECEBER_LIBERAR_ISENCAO_JURO_MULTA_DESCONTO, getUsuarioLogado()));
            setTipoAcrescimo(TipoAcrescimoEnum.PORCENTAGEM);
            setAcrescimoPorParcela(0.0);
            setValorBaseParcela(0.0);
            if(!isApresentarValorIndiceReajustePorAtraso()){
				setApresentarValorIndiceReajustePorAtraso(getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getNegociacaoContaReceberVO().getUnidadeEnsino().getCodigo()).isAplicarIndireReajustePorAtrasoContaReceber());
			}
            verificarPermissaoInformarDescontoProg_Inst();
            verificarPermissaoLiberarRenovacao();
            setMensagemID("msg_dados_editar");
            return Uteis.getCaminhoRedirecionamentoNavegacao("negociacaoContaReceberForm.xhtml");
        } catch (Exception e) {
            setDescricaoPeriodoLetivoAluno("");
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("negociacaoContaReceberForm.xhtml");
        }
    }

    public void selecionarNegociacao() {
    	 try {
	        OpcaoPagamentoDividaVO obj = (OpcaoPagamentoDividaVO) context().getExternalContext().getRequestMap().get("opcaoPagamentoDividaItens");
	        getNegociacaoContaReceberVO().setNovaContaReceber(obj.getListaCondicaoPagamentoVOs());
	        setMensagemID("msg_dados_editar");
	    } catch (Exception e) {
	        setMensagemDetalhada("msg_erro", e.getMessage());
	    }
    }

    public void gerarOpcaoPagamento() {
        try {
        	setApresentarDadosOpcaoRenegociacao(false);
            setAbrirModalRenegociacao("");
            if (getNegociacaoContaReceberVO().getPermitirRenegociacaoApenasComCondicaoRenegociacao()) {
            	getListaSelectItemOpcaoRenegociacao().clear();            
            	getListaSelectItemItemCondicaoRenegociacao().clear();
            	getFacadeFactory().getNegociacaoContaReceberFacade().realizarInicializacaoDadosOpcaoRenegociacaoBaseadoCondicaoRenegociacao(getNegociacaoContaReceberVO(), getListaContaReceberVOs(), getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(getNegociacaoContaReceberVO().getUnidadeEnsino().getCodigo()), true, getUsuarioLogado());
            	setApresentarDadosOpcaoRenegociacao(!getNegociacaoContaReceberVO().getItemCondicaoRenegociacaoVOs().isEmpty());
            	montarComboBoxItemCondicaoRenegociacao();
            	if(!getNegociacaoContaReceberVO().getOpcaoAlunoCondicaoRenegociacaoVOs().isEmpty()){            		
            		for(OpcaoAlunoCondicaoRenegociacaoVO opcao : getNegociacaoContaReceberVO().getOpcaoAlunoCondicaoRenegociacaoVOs()){
            			getListaSelectItemOpcaoRenegociacao().add(new SelectItem(opcao.getNumeroParcela(), opcao.getDescricao()));
            		}            		
            	}
            } else {
            	realizarCalculoValorBaseParcela();
	            Integer codigoPerfilEconomico = getNegociacaoContaReceberVO().getCodigoPerfilEcnonomico();
	            if (codigoPerfilEconomico == null || codigoPerfilEconomico.intValue() == 0) {
	                codigoPerfilEconomico = consultarPerfilEconomicoPadrao();
	            }
	            if (codigoPerfilEconomico != null && codigoPerfilEconomico.intValue() != 0) {
	                PerfilEconomicoVO obj = getFacadeFactory().getPerfilEconomicoFacade().consultarPorChavePrimaria(codigoPerfilEconomico, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
	                getNegociacaoContaReceberVO().gerarOpcoesPagamento(obj);
	                obj = null;
	            } else {
	                NegociacaoContaReceberVO.validarDadosBasicos(getNegociacaoContaReceberVO());
	            }
	            codigoPerfilEconomico = null;
            }
            setAbrirModalRenegociacao("RichFaces.$('panelNegociar').show()");
            limparMensagem();
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public Integer consultarPerfilEconomicoPadrao() throws Exception {
        ConfiguracaoGeralSistemaVO obj = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarPorCodigoUnidadeEnsino(getUnidadeEnsinoLogado().getCodigo(), false,
                Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
        if (obj.getCodigo().intValue() == 0 || obj.getPerfilEconomicoPadrao().getCodigo().intValue() == 0) {
            obj = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarPorCodigoUnidadeEnsino(0, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            return obj.getPerfilEconomicoPadrao().getCodigo();
        }
        return obj.getPerfilEconomicoPadrao().getCodigo();
    }

    /**
     * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe
     * <code>NegociacaoContaReceber</code>. Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação
     * <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>. Se houver alguma inconsistência o
     * objeto não é gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de erro.
     */
    public void gravar() {
        try {
        	if (!Uteis.isAtributoPreenchido(negociacaoContaReceberVO.getResponsavel().getCodigo())) {
                getNegociacaoContaReceberVO().getResponsavel().setCodigo(getUsuarioLogado().getCodigo());
                getNegociacaoContaReceberVO().getResponsavel().setNome(getUsuarioLogado().getNome());
        	}
        	 if (negociacaoContaReceberVO.isNovoObj().booleanValue()) {
        		 if (negociacaoContaReceberVO.getNrParcela() == 0) {
        			 negociacaoContaReceberVO.setNrParcela(1);
        		 }
                 getFacadeFactory().getNegociacaoContaReceberFacade().incluir(negociacaoContaReceberVO, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getNegociacaoContaReceberVO().getUnidadeEnsino().getCodigo()), true, getUsuarioLogado());
             } else {
                 getFacadeFactory().getNegociacaoContaReceberFacade().alterar(negociacaoContaReceberVO, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getNegociacaoContaReceberVO().getUnidadeEnsino().getCodigo()), getUsuarioLogado());
             }
            setMensagemID("msg_dados_gravados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP NegociacaoContaReceberCons.jsp. Define o tipo de
     * consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
     * resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    @Override
    public String consultar() {
        try {
        	getFacadeFactory().getNegociacaoContaReceberFacade().consultar(getControleConsultaOtimizado(), getResponsavelCadastro(), getComissionado(), true, Uteis.NIVELMONTARDADOS_COMBOBOX, getUnidadeEnsinoConsulta(), getUsuarioLogado());
            setMensagemID("msg_dados_consultados");
            return "";
        } catch (Exception e) {
            getControleConsulta().setListaConsulta(new ArrayList<>(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "";
        }
    }

    public void paginarConsulta(DataScrollEvent DataScrollEvent) throws Exception {
		getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
		getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());		
		consultar();
	}

    public void estornarNegociacaoContaReceber(){
    	try {
    		setApresentarModalEstornoRenegociacao("RichFaces.$('panelEstornoRenegociacaoReceber').show();");
    		getListaSelectItemCaixa().clear();
    		if(getNegociacaoContaReceberVO().isExisteContaReceberTipoOrigemCheque()){
        		montarListaSelectItemCaixa();
        		setMensagemID("msg_entre_dados");
        	}	
		} catch (Exception e) {
			  setMensagemDetalhada("msg_erro", e.getMessage());
		}
    }
    
    

    /**
     * Operação responsável por processar a exclusão um objeto da classe <code>NegociacaoContaReceberVO</code> Após a
     * exclusão ela automaticamente aciona a rotina para uma nova inclusão.
     */
    public void excluir() {
        List<ContaReceberVO> contaReceberVOs = new ArrayList<ContaReceberVO>(0);
        setApresentarModalEstornoRenegociacao("");
        try {
            
        	contaReceberVOs = getFacadeFactory().getContaReceberFacade().consultarContasVinculadasNegociacao(getNegociacaoContaReceberVO().getCodigo(), "", Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getNegociacaoContaReceberVO().getUnidadeEnsino().getCodigo()), getUsuarioLogado());            
            getFacadeFactory().getNegociacaoContaReceberFacade().verificarPermissaoExcluirNegociacao(getNegociacaoContaReceberVO(), contaReceberVOs, getUsuarioLogado());
            getFacadeFactory().getNegociacaoContaReceberFacade().excluir(getNegociacaoContaReceberVO(), contaReceberVOs,  getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getNegociacaoContaReceberVO().getUnidadeEnsino().getCodigo()),true, getUsuarioLogado());
            List<ContaReceberVO> contaReceberProcessarJob = new ArrayList<ContaReceberVO>(0);
            for (ContaReceberNegociadoVO contaReceberNegociadoVO : getNegociacaoContaReceberVO().getContaReceberNegociadoVOs()) {
					contaReceberProcessarJob.add(contaReceberNegociadoVO.getContaReceber());
			}
            getFacadeFactory().getContaReceberFacade().realizarExecucaoJobCalculoValorTemporarioContaReceber(contaReceberProcessarJob, getUsuarioLogado());
            novo();
            setMensagemID("msg_dados_excluidos");
            setApresentarModalEstornoRenegociacao("RichFaces.$('panelEstornoRenegociacaoReceber').hide();");            
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            contaReceberVOs = null;
        }
    }

    public void consultarFuncionario() {
        try {
        	Integer codigoUnidadeEnsino = Uteis.isAtributoPreenchido(getNegociacaoContaReceberVO().getUnidadeEnsino()) ? getNegociacaoContaReceberVO().getUnidadeEnsino().getCodigo() : this.getUnidadeEnsinoLogado().getCodigo();
            List<FuncionarioVO> objs = new ArrayList<FuncionarioVO>(0);
            if (getValorConsultaFuncionario().equals("")) {
                setMensagemID("msg_entre_prmconsulta");
                return;
            }
            getFacadeFactory().getPessoaFacade().setIdEntidade("Funcionario");
            if (getCampoConsultaFuncionario().equals("nome")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultarPorNome(getValorConsultaFuncionario(), codigoUnidadeEnsino, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("matricula")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultarPorMatricula(getValorConsultaFuncionario(), codigoUnidadeEnsino, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("nomeCidade")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeCidade(getValorConsultaFuncionario(), codigoUnidadeEnsino, false,
                        Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("CPF")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultarPorCPF(getValorConsultaFuncionario(), codigoUnidadeEnsino, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("cargo")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeCargo(getValorConsultaFuncionario(), codigoUnidadeEnsino, false,
                        Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("departamento")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeDepartamento(getValorConsultaFuncionario(), "FU", codigoUnidadeEnsino, false,
                        Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("unidadeEnsino")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeUnidadeEnsino(getValorConsultaFuncionario(), "FU", codigoUnidadeEnsino, false,
                        Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
            }
            setListaConsultaFuncionario(objs);
            setMensagemID("msg_dados_consultados");

        } catch (Exception e) {
            setListaConsultaFuncionario(new ArrayList<FuncionarioVO>(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
    
    public void selecionarFornecedor() {
		FornecedorVO obj = (FornecedorVO) context().getExternalContext().getRequestMap().get("fornecedorItens");
		getNegociacaoContaReceberVO().setFornecedor(obj);
		limparListaContaReceber();
	}

    public void executarVerificacaoUsuarioLogadoPodeLiberarDesconto() {
        try {
            ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("DescontoNegociacaoContaReceber", getUsuarioLogado());            
            this.setApresentarBotaoLiberarDesconto(Boolean.FALSE);
            this.setUsernameLiberarDesconto("");
            this.setSenhaLiberarDesconto("");
            setMensagemID("msg_ConfirmacaoLiberacaoDesconto");
        } catch (Exception e) {
            this.setUsernameLiberarDesconto("");
            this.setSenhaLiberarDesconto("");
            this.setApresentarBotaoLiberarDesconto(Boolean.TRUE);
            //setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    
    public void executarVerificacaoUsuarioPodeLiberarDesconto() {
        try {
            UsuarioVO usuarioVerif = ControleAcesso.verificarLoginUsuario(this.getUsernameLiberarDesconto(), this.getSenhaLiberarDesconto(), true, Uteis.NIVELMONTARDADOS_TODOS);
            ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("DescontoNegociacaoContaReceber", usuarioVerif);            
            this.setApresentarBotaoLiberarDesconto(Boolean.FALSE);
            this.setUsernameLiberarDesconto("");
            this.setSenhaLiberarDesconto("");
            setMensagemID("msg_ConfirmacaoLiberacaoDesconto");
        } catch (Exception e) {
            this.setUsernameLiberarDesconto("");
            this.setSenhaLiberarDesconto("");
            this.setApresentarBotaoLiberarDesconto(Boolean.TRUE);
            //setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
    
	private List<SelectItem> tipoConsultaComboFornecedor;

	public String getMascaraConsultaFornecedor(){
		if(getCampoConsultaFornecedor().equals("CPF")){
			return "return mascara(this.form, 'formFornecedor:valorConsultaFornecedor', '99.999.999/9999-99', event);";
		}else if(getCampoConsultaFornecedor().equals("CNPJ")){
			return "return mascara(this.form, 'formFornecedor:valorConsultaFornecedor', '999.999.999-99', event);";
		}else if(getCampoConsultaFornecedor().equals("codigo")){
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
			List<FornecedorVO> objs = new ArrayList<FornecedorVO>(0);
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
			setListaConsultaFornecedor(new ArrayList<FornecedorVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
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
	
	
	public void validarDadosParaConsultarContaReceber() {
		try {
			
			if(!Uteis.isAtributoPreenchido(getNegociacaoContaReceberVO().getUnidadeEnsino())){
				throw new ConsistirException("O campo Unidade Ensino (Negociação Conta Receber) deve ser informado.");
			}
			if(!Uteis.isAtributoPreenchido(getNegociacaoContaReceberVO().getTipoPessoa())){
				throw new ConsistirException("O campo Tipo Pessoa (Negociação Conta Receber) deve ser informado.");
			}
			if(getNegociacaoContaReceberVO().getTipoParceiro()  && !Uteis.isAtributoPreenchido(getNegociacaoContaReceberVO().getParceiro())){
				throw new ConsistirException("O campo Parceiro (Negociação Conta Receber) deve ser informado.");
			}
			if(((getNegociacaoContaReceberVO().getTipoParceiro() && getNegociacaoContaReceberVO().getParceiro().isFinanciamentoProprio()) || getNegociacaoContaReceberVO().getTipoAluno()) && !Uteis.isAtributoPreenchido(getNegociacaoContaReceberVO().getMatriculaAluno().getMatricula())){
				throw new ConsistirException("O campo Aluno (Negociação Conta Receber) deve ser informado.");
			}
			if(getNegociacaoContaReceberVO().getTipoResponsavelFinanceiro()  && !Uteis.isAtributoPreenchido(getNegociacaoContaReceberVO().getPessoa())){
				throw new ConsistirException("O campo Responsável Financeiro (Negociação Conta Receber) deve ser informado.");
			}
			if(getNegociacaoContaReceberVO().getTipoFornecedor()  && !Uteis.isAtributoPreenchido(getNegociacaoContaReceberVO().getFornecedor())){
				throw new ConsistirException("O campo Fornecedor (Negociação Conta Receber) deve ser informado.");
			}
			if(getNegociacaoContaReceberVO().getTipoFuncionario()  && !Uteis.isAtributoPreenchido(getNegociacaoContaReceberVO().getFuncionario())){
				throw new ConsistirException("O campo Funcionário (Negociação Conta Receber) deve ser informado.");
			}
			getListaConsultaContaReceber().clear();
			setHabilitarModalContaReceber("RichFaces.$('panelContaReceber').show();");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setHabilitarModalContaReceber("");
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	@SuppressWarnings("unchecked")
	public void consultarContaReceber() {
		try {
			Integer unidadeEnsino = getNegociacaoContaReceberVO().getUnidadeEnsino().getCodigo();
			List<ContaReceberVO> objs = new ArrayList<ContaReceberVO>(0);
			if (getCampoConsultaContaReceber().equals("nrDocumento")) {
				if (getNegociacaoContaReceberVO().getTipoFornecedor() && getNegociacaoContaReceberVO().getFornecedor().getCodigo() > 0) {
					objs = getFacadeFactory().getContaReceberFacade().consultarPorNrDocumentoSituacaoPessoa(TipoPessoa.getEnum(getNegociacaoContaReceberVO().getTipoPessoa()), getValorConsultaContaReceber(), 0, 0, getNegociacaoContaReceberVO().getFornecedor().getCodigo(), "", getSituacaoContaReceber(), unidadeEnsino, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(unidadeEnsino), getUsuarioLogado());
				} else if (getNegociacaoContaReceberVO().getTipoParceiro() && getNegociacaoContaReceberVO().getParceiro().getCodigo() != 0) {
					objs = getFacadeFactory().getContaReceberFacade().consultarPorNrDocumentoSituacaoPessoa(TipoPessoa.getEnum(getNegociacaoContaReceberVO().getTipoPessoa()), getValorConsultaContaReceber(), getNegociacaoContaReceberVO().getMatriculaAluno().getAluno().getCodigo(), getNegociacaoContaReceberVO().getParceiro().getCodigo(), 0, getNegociacaoContaReceberVO().getMatriculaAluno().getMatricula(), getSituacaoContaReceber(), unidadeEnsino, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(unidadeEnsino), getUsuarioLogado());
				} else if (getNegociacaoContaReceberVO().getTipoAluno() && getNegociacaoContaReceberVO().getMatriculaAluno().getAluno().getCodigo() != 0) {
					objs = getFacadeFactory().getContaReceberFacade().consultarPorNrDocumentoSituacaoPessoa(TipoPessoa.getEnum(getNegociacaoContaReceberVO().getTipoPessoa()), getValorConsultaContaReceber(), getNegociacaoContaReceberVO().getMatriculaAluno().getAluno().getCodigo(), 0, 0, getNegociacaoContaReceberVO().getMatriculaAluno().getMatricula(), getSituacaoContaReceber(), unidadeEnsino, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(unidadeEnsino), getUsuarioLogado());
				} else if (getNegociacaoContaReceberVO().getTipoFuncionario() && getNegociacaoContaReceberVO().getFuncionario().getPessoa().getCodigo() != 0) {
					objs = getFacadeFactory().getContaReceberFacade().consultarPorNrDocumentoSituacaoPessoa(TipoPessoa.getEnum(getNegociacaoContaReceberVO().getTipoPessoa()), getValorConsultaContaReceber(), getNegociacaoContaReceberVO().getFuncionario().getPessoa().getCodigo(), 0, 0, "", getSituacaoContaReceber(), unidadeEnsino, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(unidadeEnsino), getUsuarioLogado());
				} else if (getNegociacaoContaReceberVO().getTipoResponsavelFinanceiro() && getNegociacaoContaReceberVO().getPessoa().getCodigo() != 0) {
					objs = getFacadeFactory().getContaReceberFacade().consultarPorNrDocumentoSituacaoPessoa(TipoPessoa.getEnum(getNegociacaoContaReceberVO().getTipoPessoa()), getValorConsultaContaReceber(), getNegociacaoContaReceberVO().getPessoa().getCodigo(), 0, 0, "", getSituacaoContaReceber(), unidadeEnsino, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(unidadeEnsino), getUsuarioLogado());
				}
			}
			if (getCampoConsultaContaReceber().equals("codigobarra")) {
				if (getNegociacaoContaReceberVO().getTipoFornecedor() && getNegociacaoContaReceberVO().getFornecedor().getCodigo() == 0) {
					objs = getFacadeFactory().getContaReceberFacade().consultarPorCodigoBarraSituacaoPessoa(TipoPessoa.getEnum(getNegociacaoContaReceberVO().getTipoPessoa()), getValorConsultaContaReceber(), 0, 0, getNegociacaoContaReceberVO().getFornecedor().getCodigo(), "", getSituacaoContaReceber(), unidadeEnsino, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(unidadeEnsino), getUsuarioLogado());
				} else if (getNegociacaoContaReceberVO().getTipoParceiro() && getNegociacaoContaReceberVO().getParceiro().getCodigo() != 0) {
					objs = getFacadeFactory().getContaReceberFacade().consultarPorCodigoBarraSituacaoPessoa(TipoPessoa.getEnum(getNegociacaoContaReceberVO().getTipoPessoa()), getValorConsultaContaReceber(), getNegociacaoContaReceberVO().getMatriculaAluno().getAluno().getCodigo(), getNegociacaoContaReceberVO().getParceiro().getCodigo(), 0, getNegociacaoContaReceberVO().getMatriculaAluno().getMatricula(), getSituacaoContaReceber(), unidadeEnsino, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(unidadeEnsino), getUsuarioLogado());
				} else if (getNegociacaoContaReceberVO().getTipoAluno() && getNegociacaoContaReceberVO().getMatriculaAluno().getAluno().getCodigo() != 0) {
					objs = getFacadeFactory().getContaReceberFacade().consultarPorCodigoBarraSituacaoPessoa(TipoPessoa.getEnum(getNegociacaoContaReceberVO().getTipoPessoa()), getValorConsultaContaReceber(), getNegociacaoContaReceberVO().getMatriculaAluno().getAluno().getCodigo(), 0, 0, getNegociacaoContaReceberVO().getMatriculaAluno().getMatricula(), getSituacaoContaReceber(), unidadeEnsino, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(unidadeEnsino), getUsuarioLogado());
				} else if (getNegociacaoContaReceberVO().getTipoFuncionario() && getNegociacaoContaReceberVO().getFuncionario().getPessoa().getCodigo() != 0) {
					objs = getFacadeFactory().getContaReceberFacade().consultarPorCodigoBarraSituacaoPessoa(TipoPessoa.getEnum(getNegociacaoContaReceberVO().getTipoPessoa()), getValorConsultaContaReceber(), getNegociacaoContaReceberVO().getFuncionario().getPessoa().getCodigo(), 0, 0, "", getSituacaoContaReceber(), unidadeEnsino, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(unidadeEnsino), getUsuarioLogado());
				} else if (getNegociacaoContaReceberVO().getTipoResponsavelFinanceiro() && getNegociacaoContaReceberVO().getPessoa().getCodigo() != 0) {
					objs = getFacadeFactory().getContaReceberFacade().consultarPorCodigoBarraSituacaoPessoa(TipoPessoa.getEnum(getNegociacaoContaReceberVO().getTipoPessoa()), getValorConsultaContaReceber(), getNegociacaoContaReceberVO().getPessoa().getCodigo(), 0, 0, "", getSituacaoContaReceber(), unidadeEnsino, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(unidadeEnsino), getUsuarioLogado());
				}
			}
			if (getCampoConsultaContaReceber().equals("data")) {
				Date valorData = Uteis.getDate(Uteis.isAtributoPreenchido(getValorConsultaContaReceber()) ? getValorConsultaContaReceber() : Uteis.getData(new Date()));
				if (getNegociacaoContaReceberVO().getTipoFornecedor() && getNegociacaoContaReceberVO().getFornecedor().getCodigo() == 0) {
					objs = getFacadeFactory().getContaReceberFacade().consultarPorDataSituacaoPessoa(TipoPessoa.getEnum(getNegociacaoContaReceberVO().getTipoPessoa()), Uteis.getDateTime(valorData, 0, 0, 0), 0, 0, getNegociacaoContaReceberVO().getFornecedor().getCodigo(), "", getSituacaoContaReceber(), unidadeEnsino, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(unidadeEnsino), getUsuarioLogado());
				} else if (getNegociacaoContaReceberVO().getTipoParceiro() && getNegociacaoContaReceberVO().getParceiro().getCodigo() != 0) {
					objs = getFacadeFactory().getContaReceberFacade().consultarPorDataSituacaoPessoa(TipoPessoa.getEnum(getNegociacaoContaReceberVO().getTipoPessoa()), Uteis.getDateTime(valorData, 0, 0, 0), getNegociacaoContaReceberVO().getMatriculaAluno().getAluno().getCodigo(), getNegociacaoContaReceberVO().getParceiro().getCodigo(), 0, getNegociacaoContaReceberVO().getMatriculaAluno().getMatricula(), getSituacaoContaReceber(), unidadeEnsino, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(unidadeEnsino), getUsuarioLogado());
				} else if (getNegociacaoContaReceberVO().getTipoAluno() && getNegociacaoContaReceberVO().getMatriculaAluno().getAluno().getCodigo() != 0) {
					objs = getFacadeFactory().getContaReceberFacade().consultarPorDataSituacaoPessoa(TipoPessoa.getEnum(getNegociacaoContaReceberVO().getTipoPessoa()), Uteis.getDateTime(valorData, 0, 0, 0), getNegociacaoContaReceberVO().getMatriculaAluno().getAluno().getCodigo(), 0, 0, getNegociacaoContaReceberVO().getMatriculaAluno().getMatricula(), getSituacaoContaReceber(), unidadeEnsino, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(unidadeEnsino), getUsuarioLogado());
				} else if (getNegociacaoContaReceberVO().getTipoFuncionario() && getNegociacaoContaReceberVO().getFuncionario().getCodigo() != 0) {
					objs = getFacadeFactory().getContaReceberFacade().consultarPorDataSituacaoPessoa(TipoPessoa.getEnum(getNegociacaoContaReceberVO().getTipoPessoa()), Uteis.getDateTime(valorData, 0, 0, 0), getNegociacaoContaReceberVO().getFuncionario().getPessoa().getCodigo(), 0, 0, "", getSituacaoContaReceber(), unidadeEnsino, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(unidadeEnsino), getUsuarioLogado());
				} else if (getNegociacaoContaReceberVO().getTipoResponsavelFinanceiro() && getNegociacaoContaReceberVO().getPessoa().getCodigo() != 0) {
					objs = getFacadeFactory().getContaReceberFacade().consultarPorDataSituacaoPessoa(TipoPessoa.getEnum(getNegociacaoContaReceberVO().getTipoPessoa()), Uteis.getDateTime(valorData, 0, 0, 0), getNegociacaoContaReceberVO().getPessoa().getCodigo(), 0, 0, "", getSituacaoContaReceber(), unidadeEnsino, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(unidadeEnsino), getUsuarioLogado());
				}
			}
			setListaConsultaContaReceber(getFacadeFactory().getContaReceberFacade().executarCalculoValorFinalASerPago(objs, getUsuarioLogado(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(unidadeEnsino), new Date()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaContaReceber(new ArrayList<ContaReceberVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

    public void adicionarTodos() {
        try {
        	ConfiguracaoFinanceiroVO conf = getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getNegociacaoContaReceberVO().getUnidadeEnsino().getCodigo());
        	Date dataBase = new Date();
        	if (getListaConsultaContaReceber().isEmpty()) {
        		throw new ConsistirException("Não há nenhum item na lista.");
        	}
            Iterator<ContaReceberVO> i = getListaConsultaContaReceber().iterator();
            List<Integer> listaContaReceberNaoPermitidaNegociar = new ArrayList<Integer>();
            while (i.hasNext()) {
                ContaReceberVO obj = (ContaReceberVO)i.next();
                if(obj.getTipoOrigem().equals("NCR")) {
              	   boolean isTemPermissao = getFacadeFactory().getNegociacaoContaReceberFacade().validarPermissaoNegociacarParcelaNegociadaNaoCumprida(obj, getUsuarioLogado());
                     if(!isTemPermissao) {
                    	 listaContaReceberNaoPermitidaNegociar.add(obj.getCodigo());
         				continue;
                     }
                 }
                getFacadeFactory().getContaReceberFacade().carregarDados(obj, NivelMontarDados.TODOS,conf, getUsuarioLogado());
                obj.setRealizandoRecebimento(true);
                obj.setValorFinalCalculado(obj.getCalcularValorFinal(null, conf, false, dataBase, getUsuarioLogado()));
                setContaReceberNegociadoVO(new ContaReceberNegociadoVO());
                getContaReceberNegociadoVO().setContaReceber(obj);
                adicionarContaReceberNegociado(conf);
            }
            getListaConsultaContaReceber().clear();
            if(Uteis.isAtributoPreenchido(listaContaReceberNaoPermitidaNegociar)) {
 				setMsgNegociarParcelasNegociacaoNaoCumprida(UteisJSF.internacionalizar("msg_NegociacaoContaReceber_permitirNegociarTodasParcelasNegociacaoNaoCumprida").replace("{0}", montarListaContaReceberNaoPermitidasNegocias(listaContaReceberNaoPermitidaNegociar)));
 				setOncompleteModal("RichFaces.$('panelNegociarparcelasNegociacaoNaoCumprida').show()");
 				listaContaReceberNaoPermitidaNegociar.clear();
            }
            setMensagemID("msg_dados_adicionados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());

        }
    }

   private String montarListaContaReceberNaoPermitidasNegocias(List<Integer> listaContaReceberNaoPermitidaNegociar) {
    
		StringBuilder contaReceberNaoPermitidaNegociar = new StringBuilder();
		int cont = 0;
		for(Integer codigoContaReceber : listaContaReceberNaoPermitidaNegociar) {
			if(cont != 0) {
				contaReceberNaoPermitidaNegociar.append(", ");
			}
			contaReceberNaoPermitidaNegociar.append(codigoContaReceber);
			cont ++;
		}
		return contaReceberNaoPermitidaNegociar.toString();
	}

	public void selecionarContaReceber() {
		setOncompleteModal("");
        try {
        	ContaReceberVO obj = (ContaReceberVO) context().getExternalContext().getRequestMap().get("contaReceberItens");
            ConfiguracaoFinanceiroVO conf = getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getNegociacaoContaReceberVO().getUnidadeEnsino().getCodigo());
            obj.setNivelMontarDados(NivelMontarDados.NAO_INICIALIZADO);
            if(obj.getTipoOrigem().equals("NCR")) {
         	   boolean isTemPermissao = getFacadeFactory().getNegociacaoContaReceberFacade().validarPermissaoNegociacarParcelaNegociadaNaoCumprida(obj, getUsuarioLogado());
                if(!isTemPermissao) {
    				setMsgNegociarParcelasNegociacaoNaoCumprida(UteisJSF.internacionalizar("msg_NegociacaoContaReceber_permitirNegociarParcelasNegociacaoNaoCumprida").replace("{0}", String.valueOf(obj.getCodOrigem())));
    				setOncompleteModal("RichFaces.$('panelNegociarparcelasNegociacaoNaoCumprida').show()");
    				return;
                }
            }
            getFacadeFactory().getContaReceberFacade().carregarDados(obj, NivelMontarDados.TODOS, conf, getUsuarioLogado());
            if(Uteis.isAtributoPreenchido(obj.getProcessamentoIntegracaoFinanceiraDetalheVO().getCodigo())){
				throw new Exception("Não é possível realizar o recebimento de uma conta de integração financeira.");
			}
            obj.setRealizandoRecebimento(true);
            obj.setValorFinalCalculado(obj.getCalcularValorFinal(new Date(), conf, false, new Date(), getUsuarioLogado()));
            setContaReceberNegociadoVO(new ContaReceberNegociadoVO());
            getContaReceberNegociadoVO().setContaReceber(obj);
            getOncompleteModal();
            adicionarContaReceberNegociado(conf);
            removerContaReceberListConsulta(obj);
            getContaReceberVOs().add(obj);
            if (getNegociacaoContaReceberVO().getPermitirRenegociacaoApenasComCondicaoRenegociacao()) {
//        		atualizarParcelasNegociacao();
        	}
            setMensagemID("msg_dados_adicionados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());

        }
    }

    public String getFecharContaReceber() {
        Uteis.liberarListaMemoria(getListaConsultaContaReceber());
        setValorConsultaContaReceber("");
        return "";
    }

    public void removerContaReceberListConsulta(ContaReceberVO obj) throws Exception {
        getListaConsultaContaReceber().remove(obj);
    }

    public List<SelectItem> tipoConsultaComboContaReceber;
    public List<SelectItem> getTipoConsultaComboContaReceber() {
    	if(tipoConsultaComboContaReceber == null) {
    		tipoConsultaComboContaReceber = new ArrayList<SelectItem>(0);
    		tipoConsultaComboContaReceber.add(new SelectItem("nrDocumento", "Nr. Documento"));
    		tipoConsultaComboContaReceber.add(new SelectItem("codigobarra", "Codigo de Barras"));
    		tipoConsultaComboContaReceber.add(new SelectItem("data", "Data"));
    }
        return tipoConsultaComboContaReceber;
    }

    public List<SelectItem> tipoConsultaComboTipoBoleto;
    public List<SelectItem> getTipoConsultaComboTipoBoleto() {
    	if(tipoConsultaComboTipoBoleto == null) {
    	tipoConsultaComboTipoBoleto = new ArrayList<SelectItem>(0);
    	tipoConsultaComboTipoBoleto.add(new SelectItem("boleto", "Boleto"));
    	tipoConsultaComboTipoBoleto.add(new SelectItem("boletoSegundo", "Boleto (LayOut 2)"));
    	tipoConsultaComboTipoBoleto.add(new SelectItem("carne", "Carnê"));
    }
        return tipoConsultaComboTipoBoleto;
    }

    public void consultarAluno() {
        try {
        	Integer codigoUnidadeEnsino = Uteis.isAtributoPreenchido(this.getUnidadeEnsinoLogado().getCodigo()) ? this.getUnidadeEnsinoLogado().getCodigo() : Uteis.isAtributoPreenchido(getNegociacaoContaReceberVO().getUnidadeEnsino()) ? getNegociacaoContaReceberVO().getUnidadeEnsino().getCodigo() : 0;
            List<MatriculaVO> objs = new ArrayList<MatriculaVO>(0);
            if (getValorConsultaAluno().equals("")) {
                setMensagemID("msg_entre_prmconsulta");
                return;
            }
            if (getCampoConsultaAluno().equals("matricula")) {
                objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatricula(getValorConsultaAluno(), codigoUnidadeEnsino, getNegociacaoContaReceberVO().getParceiro().getCodigo(), true, false, getUsuarioLogado());
            }
            if (getCampoConsultaAluno().equals("nomePessoa")) {
                objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), codigoUnidadeEnsino, getNegociacaoContaReceberVO().getParceiro().getCodigo(), true, false, getUsuarioLogado());
            }
            setListaConsultaAluno(objs);
            setMensagemID("msg_dados_consultados");

        } catch (Exception e) {
            setListaConsultaAluno(new ArrayList<MatriculaVO>(0));
            setMensagemDetalhada("msg_erro", e.getMessage());

        }
    }

    public void consultarCentroReceita() {
        try {
            List<CentroReceitaVO> objs = new ArrayList<CentroReceitaVO>(0);
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
            setListaConsultaCentroReceita(new ArrayList<CentroReceitaVO>(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public List<SelectItem> tipoConsultaComboCentroReceita;
    public List<SelectItem> getTipoConsultaComboCentroReceita() {
    	if(tipoConsultaComboCentroReceita == null) {
    		tipoConsultaComboCentroReceita = new ArrayList<SelectItem>(0);
    		tipoConsultaComboCentroReceita.add(new SelectItem("descricao", "Descrição"));
    		tipoConsultaComboCentroReceita.add(new SelectItem("identificadorCentroReceita", "Identificador Centro Receita"));
    		tipoConsultaComboCentroReceita.add(new SelectItem("nomeDepartamento", "Departamento"));
    }
        return tipoConsultaComboCentroReceita;
    }

    public List<SelectItem> tipoConsultaComboAluno;
    public List<SelectItem> getTipoConsultaComboAluno() {
    	if(tipoConsultaComboAluno == null) {
    		tipoConsultaComboAluno = new ArrayList<SelectItem>(0);
    		tipoConsultaComboAluno.add(new SelectItem("nomePessoa", "Aluno"));
    		tipoConsultaComboAluno.add(new SelectItem("matricula", "Matrícula"));
    }
        return tipoConsultaComboAluno;
    }

    public List<SelectItem> tipoConsultaComboFuncionario;
    public List<SelectItem> getTipoConsultaComboFuncionario() {
    	if(tipoConsultaComboFuncionario == null) {
    	tipoConsultaComboFuncionario = new ArrayList<SelectItem>(0);
        tipoConsultaComboFuncionario.add(new SelectItem("nome", "Nome"));
        tipoConsultaComboFuncionario.add(new SelectItem("CPF", "CPF"));
        tipoConsultaComboFuncionario.add(new SelectItem("matricula", "Matrícula"));
        tipoConsultaComboFuncionario.add(new SelectItem("cargo", "Cargo"));
        tipoConsultaComboFuncionario.add(new SelectItem("departamento", "Departamento"));
    }
        return tipoConsultaComboFuncionario;
    }

    public void selecionarFuncionario() {
    	try {
    		FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItens");
            this.getNegociacaoContaReceberVO().setFuncionario(obj);
            this.getNegociacaoContaReceberVO().getPessoa().setCodigo(obj.getPessoa().getCodigo());
            limparListaContaReceber();
            Uteis.liberarListaMemoria(getListaConsultaFuncionario());
            campoConsultaFuncionario = null;
            valorConsultaFuncionario = null;	
            inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
        
    }
    
    public void selecionarPessoaComissionada() {
    	try {
	    	FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItens");
	    	this.getNegociacaoContaReceberVO().setPessoaComissionada(obj.getPessoa());
	    	Uteis.liberarListaMemoria(getListaConsultaFuncionario());
	    	campoConsultaFuncionario = null;
	    	valorConsultaFuncionario = null;
	    	inicializarMensagemVazia();
    	} catch (Exception e) {
    		setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
    }
    

	public void selecionarAluno() {
		try {
			MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("alunoItens");
			carregarDadosMatricula(obj);
			Uteis.liberarListaMemoria(getListaConsultaAluno());			
			campoConsultaAluno = null;
			valorConsultaAluno = null;
		} catch (Exception e) {
			getNegociacaoContaReceberVO().setMatriculaAluno(new MatriculaVO());
			getNegociacaoContaReceberVO().getContaReceberNegociadoVOs().clear();
			getNegociacaoContaReceberVO().getNovaContaReceber().clear();
			setDescricaoPeriodoLetivoAluno("");
			getNegociacaoContaReceberVO().getMatriculaAluno().setMatricula("");
			getNegociacaoContaReceberVO().getMatriculaAluno().getAluno().setNome("");
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	
	private void carregarDadosMatricula(MatriculaVO obj) throws Exception{
		limparListaContaReceber();
		this.getNegociacaoContaReceberVO().setMatriculaAluno(obj);
		MatriculaPeriodoVO matriculaPeriodoVO = getFacadeFactory().getMatriculaPeriodoFacade().consultaUltimaMatriculaPeriodoPorMatriculaDadosNegociacao(obj.getMatricula(), false, getUsuarioLogado());
		getNegociacaoContaReceberVO().getMatriculaAluno().setPlanoFinanceiroAluno(getFacadeFactory().getPlanoFinanceiroAlunoFacade().consultarPorMatriculaPeriodo(matriculaPeriodoVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
		setDescricaoPeriodoLetivoAluno(matriculaPeriodoVO.getPeridoLetivo().getDescricao());
		this.getNegociacaoContaReceberVO().getPessoa().setCodigo(obj.getAluno().getCodigo());
		if(!Uteis.isAtributoPreenchido(getNegociacaoContaReceberVO().getUnidadeEnsino().getCodigo()) && Uteis.isAtributoPreenchido(obj.getUnidadeEnsino())) {
			getNegociacaoContaReceberVO().setUnidadeEnsino(obj.getUnidadeEnsino());
		}else if(!getFacadeFactory().getContaReceberFacade().realizarVerificacaoAlunoPossuiContaAReceberVinculadoUnidadeEnsinoFinanceira(obj.getMatricula(), getNegociacaoContaReceberVO().getUnidadeEnsino().getCodigo()) && Uteis.isAtributoPreenchido(obj.getUnidadeEnsino())) {
			getNegociacaoContaReceberVO().setUnidadeEnsino(obj.getUnidadeEnsino());
		}
		carregarDadosFinanceiros();
	}

    public void selecionarCentroReceita() {
        CentroReceitaVO obj = (CentroReceitaVO) context().getExternalContext().getRequestMap().get("centroReceitaItens");
        this.getNegociacaoContaReceberVO().setCentroReceita(obj);
        Uteis.liberarListaMemoria(getListaConsultaCentroReceita());
        campoConsultaCentroReceita = null;
        valorConsultaCentroReceita = null;
    }

    public void consultarAlunoPorMatricula() {
        try {
            if (!getNegociacaoContaReceberVO().getMatriculaAluno().getMatricula().equals("")) {
            	Integer codigoUnidadeEnsino = Uteis.isAtributoPreenchido(getNegociacaoContaReceberVO().getUnidadeEnsino()) ? getNegociacaoContaReceberVO().getUnidadeEnsino().getCodigo() : this.getUnidadeEnsinoLogado().getCodigo();
                MatriculaVO objs = getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(getNegociacaoContaReceberVO().getMatriculaAluno().getMatricula(), codigoUnidadeEnsino , getNegociacaoContaReceberVO().getParceiro().getCodigo(),  true, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,  getUsuarioLogado().getCodigo().equals(0) ? getUsuarioLogado() : getUsuarioLogado());
                carregarDadosMatricula(objs);
                if (!objs.getMatricula().equals("")) {
                    setMensagemID("msg_dados_consultados");
                    return;
                }
                getNegociacaoContaReceberVO().getMatriculaAluno().setMatricula("");
                getNegociacaoContaReceberVO().getMatriculaAluno().getAluno().setNome("");
                setMensagemID("msg_erro_dadosnaoencontrados");
            }
        } catch (Exception e) {
            getNegociacaoContaReceberVO().setMatriculaAluno(new MatriculaVO());
            setDescricaoPeriodoLetivoAluno("");
            limparListaContaReceber();
            getNegociacaoContaReceberVO().getMatriculaAluno().setMatricula("");
            getNegociacaoContaReceberVO().getMatriculaAluno().getAluno().setNome("");
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void consultarFuncionarioPorCodigo() {
        try {
            if (!this.getNegociacaoContaReceberVO().getFuncionario().getMatricula().equals("") && getNegociacaoContaReceberVO().getTipoPessoa().equals("FU")) {
                FuncionarioVO funcionario = getFacadeFactory().getFuncionarioFacade().consultarPorRequisitanteMatricula(this.getNegociacaoContaReceberVO().getFuncionario().getMatricula(),
                        this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
                limparListaContaReceber();
                if (funcionario.getCodigo().intValue() != 0) {
                    this.getNegociacaoContaReceberVO().setFuncionario(funcionario);
                    this.getNegociacaoContaReceberVO().setPessoa(funcionario.getPessoa());
                    setMensagemID("msg_dados_consultados");
                } else {
                    throw new Exception();
                }
            }
        } catch (Exception e) {
            getNegociacaoContaReceberVO().getFuncionario().setCodigo(0);
            getNegociacaoContaReceberVO().getFuncionario().setMatricula("");
            getNegociacaoContaReceberVO().getFuncionario().getPessoa().setNome("");
            limparListaContaReceber();
            setMensagemID("msg_erro_dadosnaoencontrados");
        }
    }

    public List<SelectItem> listaSelectItemAlunoFuncionarioParceiro;
    public List<SelectItem> getListaSelectItemAlunoFuncionarioParceiro() throws Exception {
		if (listaSelectItemAlunoFuncionarioParceiro == null) {
			listaSelectItemAlunoFuncionarioParceiro = new ArrayList<SelectItem>(0);
			listaSelectItemAlunoFuncionarioParceiro.add(new SelectItem("", ""));
			listaSelectItemAlunoFuncionarioParceiro.add(new SelectItem("AL", "Aluno"));
			listaSelectItemAlunoFuncionarioParceiro.add(new SelectItem("RF", "Responsável Financeiro"));
			listaSelectItemAlunoFuncionarioParceiro.add(new SelectItem("FU", "Funcionário"));
			listaSelectItemAlunoFuncionarioParceiro.add(new SelectItem("PA", "Parceiro"));
			listaSelectItemAlunoFuncionarioParceiro.add(new SelectItem("FO", "Fornecedor"));
    }
        return listaSelectItemAlunoFuncionarioParceiro;
    }

    public List<SelectItem> listasSituacaoContaReceber;
	
    public List<SelectItem> getListasSituacaoContaReceber() throws Exception {
    	if(listasSituacaoContaReceber == null){
    		listasSituacaoContaReceber = new ArrayList<SelectItem>(0);
    		listasSituacaoContaReceber.add(new SelectItem("VE", "Vencido"));
    		listasSituacaoContaReceber.add(new SelectItem("AV", "A vencer"));
    		listasSituacaoContaReceber.add(new SelectItem("AM", "Ambos"));
    	}
        return listasSituacaoContaReceber;
    }

    public void gerarParcelas() {
        try {
        	setAbrirModalRenegociacao("");
        	Uteis.checkState(getNegociacaoContaReceberVO().getTipoIntervaloParcelaEnum().isIntervaloEntreDias()  && !Uteis.isAtributoPreenchido(getNegociacaoContaReceberVO().getIntervaloParcela()), "O campo Intervalo de Parcela deve ser informado.");
        	Uteis.checkState(getNegociacaoContaReceberVO().getTipoIntervaloParcelaEnum().isIntervaloDataBase()  && !Uteis.isAtributoPreenchido(getNegociacaoContaReceberVO().getDataBaseParcela()), "O campo Data Base Parcela deve ser informado.");
        	//Uteis.checkState(getApresentarDadosOpcaoRenegociacao()  && !Uteis.isAtributoPreenchido(getNegociacaoContaReceberVO().getNrParcela()), "Deve ser selecionado uma condição de renegociação.");
        	if (getNegociacaoContaReceberVO().getPermitirRenegociacaoApenasComCondicaoRenegociacao()) {
        		getFacadeFactory().getNegociacaoContaReceberFacade().validarValorEntrada(getNegociacaoContaReceberVO());
        	}
        	getFacadeFactory().getNegociacaoContaReceberFacade().gerarParcelas(getNegociacaoContaReceberVO(), getTipoAcrescimo(), getAcrescimoPorParcela(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getNegociacaoContaReceberVO().getUnidadeEnsino().getCodigo()), getUsuarioLogado());
            calcularValorTotalConfirmacaoNegociacao();   
//            negociacaoContaReceberVO.setDesconto(negociacaoContaReceberVO.getValorJuroDesconto() + negociacaoContaReceberVO.getValorMultaDesconto() + negociacaoContaReceberVO.getValorIndiceReajusteDesconto());
            limparMensagem();
            setAbrirModalRenegociacao("RichFaces.$('panelConfirmarNegociacao').show();");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void calcularValorTotalConfirmacaoNegociacao() {
    	 try {
 			setValorTotalApresentar(getFacadeFactory().getNegociacaoContaReceberFacade().calcularValorTotalConfirmacaoNegociacao(getNegociacaoContaReceberVO(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getNegociacaoContaReceberVO().getUnidadeEnsino().getCodigo()), getUsuarioLogado()));
 			limparMensagem();
 		} catch (Exception e) {
 			setMensagemDetalhada("msg_erro", e.getMessage());
 		}
    }

    public String getFecharModal() {
        if (getNegociacaoContaReceberVO().getEditar()) {
            return "RichFaces.$('panelConfirmarNegociacao').hide(); RichFaces.$('panelNegociar').hide()";
        }
        return "";
    }

    /*
     * Método responsável por adicionar um novo objeto da classe <code>ContaReceberNegociado</code> para o objeto
     * <code>negociacaoContaReceberVO</code> da classe <code>NegociacaoContaReceber</code>
     */
    public void adicionarContaReceberNegociado(ConfiguracaoFinanceiroVO conf) throws Exception {
    	getFacadeFactory().getNegociacaoContaReceberFacade().adicionarContaReceberNegociado(getNegociacaoContaReceberVO(), getContaReceberNegociadoVO(), getDesconsiderarDescontoProgressivo(), getDesconsiderarDescontoAluno(), getDesconsiderarDescontoInstituicaoComValidade(), getDesconsiderarDescontoInstituicaoSemValidade(), 
    			conf, getUsuario());      	
        setContaReceberNegociadoVO(new ContaReceberNegociadoVO());
        if(!isApresentarValorIndiceReajustePorAtraso()){
			setApresentarValorIndiceReajustePorAtraso(getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getNegociacaoContaReceberVO().getUnidadeEnsino().getCodigo()).isAplicarIndireReajustePorAtrasoContaReceber());
		}

    }   

    /*
     * Método responsável por remover um novo objeto da classe <code>ContaReceberNegociado</code> do objeto
     * <code>negociacaoContaReceberVO</code> da classe <code>NegociacaoContaReceber</code>
     */
    public void removerContaReceberNegociado()  {
    	try {
    		ConfiguracaoFinanceiroVO conf = getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getNegociacaoContaReceberVO().getUnidadeEnsino().getCodigo());
            ContaReceberNegociadoVO obj = (ContaReceberNegociadoVO) context().getExternalContext().getRequestMap().get("contaReceberNegociadoItens");
            getNegociacaoContaReceberVO().excluirObjContaReceberNegociadoVOs(obj);
        	if(getNegociacaoContaReceberVO().getContaReceberNegociadoVOs().isEmpty()){
        		getNegociacaoContaReceberVO().setValorIsencaoTotalJuroMaximo(0.0);
        		getNegociacaoContaReceberVO().setValorIsencaoTotalMultaMaximo(0.0);
				getNegociacaoContaReceberVO().setValorConcecaoDescontoPerdidoMaximo(0.0);
				getNegociacaoContaReceberVO().setJuro(0.0);
				getNegociacaoContaReceberVO().setDesconto(0.0);
				getNegociacaoContaReceberVO().setAcrescimoGeral(0.0);
				getNegociacaoContaReceberVO().setTotalAcrescimoPorParcela(0.0);
        		getNegociacaoContaReceberVO().setItemCondicaoDescontoRenegociacaoVO(new ItemCondicaoDescontoRenegociacaoVO());
        		getNegociacaoContaReceberVO().calcularValorTotal();
        		getContaReceberVOs().remove(obj.getContaReceber());
    		}else{
    			getFacadeFactory().getNegociacaoContaReceberFacade().validarExistenciaItemCondicaoDescontoRenegociacao(getNegociacaoContaReceberVO(), conf, getUsuarioLogado());
    		}
            obj = null;
            setMensagemID("msg_dados_excluidos");	
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
    	

    }
    
    public void validarAtualizacaoValorIsencaoJuro()  {
    	try {
    		Uteis.checkState(!getNegociacaoContaReceberVO().isLiberarIsencaoJuroMultaDescontoAcimaMaximo() && getNegociacaoContaReceberVO().getValorIsencaoTotalJuro()>getNegociacaoContaReceberVO().getValorIsencaoTotalJuroMaximo(), "O valor Máximo para Isenção Juro é de: "+Uteis.arrendondarForcando2CadasDecimaisStrComSepador(getNegociacaoContaReceberVO().getValorIsencaoTotalJuroMaximo(),","));
    		getNegociacaoContaReceberVO().setTipoDesconto("VA");
    		getNegociacaoContaReceberVO().setDesconto(Uteis.arrendondarForcando2CadasDecimais(getNegociacaoContaReceberVO().getValorIsencaoTotalJuro() + getNegociacaoContaReceberVO().getValorIsencaoTotalMulta() + getNegociacaoContaReceberVO().getValorConcecaoDescontoPerdido()));
    		getNegociacaoContaReceberVO().calcularValorTotal();
    		inicializarMensagemVazia();
		} catch (Exception e) {
			getNegociacaoContaReceberVO().setValorIsencaoTotalJuro(0.0);
			getNegociacaoContaReceberVO().calcularValorTotal();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
    }
    
    public void validarAtualizacaoValorIsencaoMulta()  {
    	try {
    		Uteis.checkState(!getNegociacaoContaReceberVO().isLiberarIsencaoJuroMultaDescontoAcimaMaximo() && getNegociacaoContaReceberVO().getValorIsencaoTotalMulta()>getNegociacaoContaReceberVO().getValorIsencaoTotalMultaMaximo(), "O valor Máximo para Isenção Multa é de: "+Uteis.arrendondarForcando2CadasDecimaisStrComSepador(getNegociacaoContaReceberVO().getValorIsencaoTotalMultaMaximo(),","));
    		getNegociacaoContaReceberVO().setTipoDesconto("VA");
    		getNegociacaoContaReceberVO().setDesconto(Uteis.arrendondarForcando2CadasDecimais(getNegociacaoContaReceberVO().getValorIsencaoTotalJuro() + getNegociacaoContaReceberVO().getValorIsencaoTotalMulta() + getNegociacaoContaReceberVO().getValorConcecaoDescontoPerdido()));
    		getNegociacaoContaReceberVO().calcularValorTotal();
    		inicializarMensagemVazia();
    	} catch (Exception e) {
    		getNegociacaoContaReceberVO().setValorIsencaoTotalMulta(0.0);
    		getNegociacaoContaReceberVO().calcularValorTotal();
    		setMensagemDetalhada("msg_erro", e.getMessage());
    	}
    }
    
    public void validarAtualizacaoValorConcecaoDescontosPerdidos()  {
    	try {
    		Uteis.checkState(!getNegociacaoContaReceberVO().isLiberarIsencaoJuroMultaDescontoAcimaMaximo() && getNegociacaoContaReceberVO().getValorConcecaoDescontoPerdido()>getNegociacaoContaReceberVO().getValorConcecaoDescontoPerdidoMaximo(), "O valor Máximo para Conceção de Descontos Perdidos é de: "+Uteis.arrendondarForcando2CadasDecimaisStrComSepador(getNegociacaoContaReceberVO().getValorConcecaoDescontoPerdidoMaximo(),","));
    		getNegociacaoContaReceberVO().setTipoDesconto("VA");
    		getNegociacaoContaReceberVO().setDesconto(Uteis.arrendondarForcando2CadasDecimais(getNegociacaoContaReceberVO().getValorIsencaoTotalJuro() + getNegociacaoContaReceberVO().getValorIsencaoTotalMulta() + getNegociacaoContaReceberVO().getValorConcecaoDescontoPerdido()));
    		getNegociacaoContaReceberVO().calcularValorTotal();
    		inicializarMensagemVazia();
    	} catch (Exception e) {
    		getNegociacaoContaReceberVO().setValorConcecaoDescontoPerdido(0.0);
    		getNegociacaoContaReceberVO().calcularValorTotal();
    		setMensagemDetalhada("msg_erro", e.getMessage());
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

    /**
     * Método responsável por processar a consulta na entidade <code>ContaReceber</code> por meio de sua respectiva
     * chave primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave
     * primária da entidade montando automaticamente o resultado da consulta para apresentação.
     */
    public void consultarContaReceberPorChavePrimaria() {
        try {
            Integer campoConsulta = contaReceberNegociadoVO.getContaReceber().getCodigo();
            ContaReceberVO contaReceber = getFacadeFactory().getContaReceberFacade().consultarPorChavePrimaria(campoConsulta, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getNegociacaoContaReceberVO().getUnidadeEnsino().getCodigo()), getUsuarioLogado());
            contaReceberNegociadoVO.getContaReceber().setCodigo(contaReceber.getCodigo());

            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemID("msg_erro_dadosnaoencontrados");
            contaReceberNegociadoVO.getContaReceber().setCodigo(0);
            contaReceberNegociadoVO.getContaReceber().setCodigo(0);

        }
    }
    
    @SuppressWarnings("unchecked")
    public List<ContaCorrenteVO> consultarContaCorrentePorNumero(Boolean contaCaixa) throws Exception {
		List<ContaCorrenteVO> lista = new ArrayList<ContaCorrenteVO>(0);
		if (contaCaixa) {
			if (getNegociacaoContaReceberVO() != null) {
				Boolean usuarioTemContaCaixa = false;
				if (getNegociacaoContaReceberVO().getCodigo().equals(0)) {
					usuarioTemContaCaixa = getFacadeFactory().getContaCorrenteFacade().consultarSeUsuarioTemContaCaixaVinculadoAEle(getUsuarioLogado().getPessoa().getCodigo());
				}
				if (usuarioTemContaCaixa) {
					if (getNegociacaoContaReceberVO().getCodigo() != 0) {
						lista = getFacadeFactory().getContaCorrenteFacade().consultarPorFuncionarioResponsavel(getUsuarioLogado().getPessoa().getCodigo(), getNegociacaoContaReceberVO().getUnidadeEnsino().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
					} else {
						lista = getFacadeFactory().getContaCorrenteFacade().consultarPorFuncionarioResponsavelDataAberturaFluxoCaixaSituacao(contaCaixa, getUsuarioLogado().getPessoa().getCodigo(), getNegociacaoContaReceberVO().getUnidadeEnsino().getCodigo(), new Date(), "A", Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
					}
				} else {
					if (getNegociacaoContaReceberVO().getCodigo() != 0) {
						lista = getFacadeFactory().getContaCorrenteFacade().consultarPorContaCaixa(contaCaixa, getNegociacaoContaReceberVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
					} else {
						lista = getFacadeFactory().getContaCorrenteFacade().consultarPorContaCaixaDataAberturaFluxoCaixaSituacao(contaCaixa, getNegociacaoContaReceberVO().getUnidadeEnsino().getCodigo(), new Date(), "A", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
					}
				}
			}
		} else {
			lista = getFacadeFactory().getContaCorrenteFacade().consultarPorContaCaixa(contaCaixa, getNegociacaoContaReceberVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());

		}
		return lista;
	}
    
    /**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>Caixa</code>.
	 */
	public void montarListaSelectItemCaixa(String prm) throws Exception {
		List<ContaCorrenteVO> resultadoConsulta = consultarContaCorrentePorNumero(true);
		Iterator<ContaCorrenteVO> i = resultadoConsulta.iterator();
		List<SelectItem> objs = new ArrayList<SelectItem>(0);
		objs.add(new SelectItem(0, ""));
		while (i.hasNext()) {
			ContaCorrenteVO obj = (ContaCorrenteVO) i.next();
			if (obj.getSituacao().equals("AT")) {
				objs.add(new SelectItem(obj.getCodigo(), obj.getDescricaoCompletaConta()));
			}
		}
		Uteis.liberarListaMemoria(resultadoConsulta);
		i = null;
		setListaSelectItemCaixa(objs);
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
			// System.out.println("MENSAGEM => " + e.getMessage());
		}
	}

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
     * relativo ao atributo <code>ContaCorrente</code>.
     */
    public void montarListaSelectItemContaCorrente(String prm) throws Exception {
    		List<ContaCorrenteVO> resultadoConsulta = null;
    		Iterator<ContaCorrenteVO> i = null;
    		try {
                if (getNegociacaoContaReceberVO().getUnidadeEnsino().getCodigo() == 0) {
                    setListaSelectItemContaCorrente(new ArrayList<SelectItem>(0));
                    return;
                }
    			resultadoConsulta = getFacadeFactory().getContaCorrenteFacade().consultarRapidaContaCorrentePorTipo(false, false, true, getNegociacaoContaReceberVO().getUnidadeEnsino().getCodigo(), getUsuarioLogado());
    			i = resultadoConsulta.iterator();
    			List<SelectItem> objs = new ArrayList<SelectItem>(0);
    			objs.add(new SelectItem(0, ""));
    			while (i.hasNext()) {
    				ContaCorrenteVO obj = (ContaCorrenteVO) i.next();
    				if (obj.getSituacao().equals("AT")) {
    	            	objs.add(new SelectItem(obj.getCodigo(), obj.getDescricaoCompletaConta()));    	            	
    					}
    				}
    			setListaSelectItemContaCorrente(objs);
    			if (getNegociacaoContaReceberVO().getNovoObj()) {
        			if(!getNegociacaoContaReceberVO().getCondicaoRenegociacao().getListaCondicaoRenegociacaoUnidadeEnsinoVOs().isEmpty()
        					&& Uteis.isAtributoPreenchido(getNegociacaoContaReceberVO().getCondicaoRenegociacao().getListaCondicaoRenegociacaoUnidadeEnsinoVOs().get(0).getContaCorrenteVO())){
        				getNegociacaoContaReceberVO().setContaCorrente(getNegociacaoContaReceberVO().getCondicaoRenegociacao().getListaCondicaoRenegociacaoUnidadeEnsinoVOs().get(0).getContaCorrenteVO());
        			}else if (!getNegociacaoContaReceberVO().getUnidadeEnsino().getContaCorrentePadraoNegociacao().equals(0)) {
    	    			getNegociacaoContaReceberVO().getContaCorrente().setCodigo(getNegociacaoContaReceberVO().getUnidadeEnsino().getContaCorrentePadraoNegociacao());
    	    		} else {	    			
    	    			getNegociacaoContaReceberVO().getContaCorrente().setCodigo(getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getNegociacaoContaReceberVO().getUnidadeEnsino().getCodigo()).getContaCorrentePadraoNegociacao());
    	    		}
    			}


    		} catch (Exception e) {
    			throw e;
    		} finally {
    			Uteis.liberarListaMemoria(resultadoConsulta);
    			i = null;
    		}
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>ContaCorrente</code>. Buscando todos os
     * objetos correspondentes a entidade <code>ContaCorrente</code>. Esta rotina não recebe parâmetros para filtragem
     * de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemContaCorrente() throws Exception {
        try {
            montarListaSelectItemContaCorrente("");
        } catch (Exception e) {
            //System.out.println(e.getMessage());
            throw e;
        }
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>numero</code> Este
     * atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox
     * correspondente
     */
    public List<ContaCorrenteVO> consultarContaCorrentePorNumero(String numeroPrm) throws Exception {
        List<ContaCorrenteVO> lista = getFacadeFactory().getContaCorrenteFacade().consultarRapidaContaCorrentePorTipo(false, true, getNegociacaoContaReceberVO().getUnidadeEnsino().getCodigo(), getUsuarioLogado());
        return lista;
    }

    /**
     * Método responsável por processar a consulta na entidade <code>Funcionario</code> por meio de sua respectiva chave
     * primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave primária
     * da entidade montando automaticamente o resultado da consulta para apresentação.
     */
    public void consultarFuncionarioPorChavePrimaria() {
        try {
            Integer campoConsulta = negociacaoContaReceberVO.getFuncionario().getCodigo();
            FuncionarioVO funcionario = getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimaria(campoConsulta, 0, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            negociacaoContaReceberVO.getFuncionario().setMatricula(funcionario.getMatricula());
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemID("msg_erro_dadosnaoencontrados");
            negociacaoContaReceberVO.getFuncionario().setMatricula("");
            negociacaoContaReceberVO.getFuncionario().setCodigo(0);
        }
    }

    /**
     * Método responsável por processar a consulta na entidade <code>Matricula</code> por meio de sua respectiva chave
     * primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave primária
     * da entidade montando automaticamente o resultado da consulta para apresentação.
     */
    public void consultarMatriculaPorChavePrimaria() {
        try {
            String campoConsulta = negociacaoContaReceberVO.getMatriculaAluno().getMatricula();
            Integer unidadeEnsino = Uteis.isAtributoPreenchido(getUnidadeEnsinoLogado().getCodigo()) ? getUnidadeEnsinoLogado().getCodigo() : getNegociacaoContaReceberVO().getUnidadeEnsino().getCodigo();
            MatriculaVO matricula = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(campoConsulta, unidadeEnsino, NivelMontarDados.BASICO, getUsuarioLogado());
            carregarDadosMatricula(matricula); 
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemID("msg_erro_dadosnaoencontrados");
            negociacaoContaReceberVO.getMatriculaAluno().setMatricula("");
        }
    }

    /**
     * Método responsável por processar a consulta na entidade <code>Pessoa</code> por meio de sua respectiva chave
     * primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave primária
     * da entidade montando automaticamente o resultado da consulta para apresentação.
     */
    public void consultarPessoaPorChavePrimaria() {
        try {
            Integer campoConsulta = negociacaoContaReceberVO.getPessoa().getCodigo();
            PessoaVO pessoa = getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(campoConsulta, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            negociacaoContaReceberVO.getPessoa().setNome(pessoa.getNome());
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemID("msg_erro_dadosnaoencontrados");
            negociacaoContaReceberVO.getPessoa().setNome("");
            negociacaoContaReceberVO.getPessoa().setCodigo(0);
        }
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
     * relativo ao atributo <code>UnidadeEnsino</code>.
     */
    public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
        List<UnidadeEnsinoVO> resultadoConsulta = null;
        Iterator<UnidadeEnsinoVO> i = null;
        try {
        	if (Uteis.isAtributoPreenchido(getUnidadeEnsinoLogado().getCodigo())) {
            	resultadoConsulta = getFacadeFactory().getUnidadeEnsinoFacade().consultarUnidadeEnsinoAcademicaEFinanceiraConformeUnidadeEnsinoLogada(getUnidadeEnsinoLogado().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
            	setUnidadeEnsinoConsulta(getUnidadeEnsinoLogado().getCodigo());
            	getNegociacaoContaReceberVO().getUnidadeEnsino().setCodigo(getUnidadeEnsinoLogado().getCodigo());
            }else {
            	resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
            }
            i = resultadoConsulta.iterator();
            List<SelectItem> objs = new ArrayList<SelectItem>(0);    
            if (!Uteis.isAtributoPreenchido(getUnidadeEnsinoLogado().getCodigo())) {
            objs.add(new SelectItem(0, ""));
            }
            while (i.hasNext()) {
                UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
                if(!Uteis.isAtributoPreenchido(getNegociacaoContaReceberVO().getUnidadeEnsino())) {
                	getNegociacaoContaReceberVO().getUnidadeEnsino().setCodigo(obj.getCodigo());
                }
            }
            setListaSelectItemUnidadeEnsino(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    @SuppressWarnings("unchecked")
    public void montarListaSelectItemDescontoProgressivo() throws Exception {
    	 List<DescontoProgressivoVO> resultadoConsulta = null;
         Iterator<DescontoProgressivoVO> i = null;
         try {
             resultadoConsulta = getFacadeFactory().getDescontoProgressivoFacade().consultarDescontoProgressivoPorSituacao(getUsuarioLogado(), true);
             i = resultadoConsulta.iterator();
             List<SelectItem> objs = new ArrayList<SelectItem>(0);
             objs.add(new SelectItem("", ""));
             boolean existeDesconto = false;
             while (i.hasNext()) {
                 DescontoProgressivoVO obj = (DescontoProgressivoVO) i.next();
                 objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
                 if(obj.getCodigo().equals(getNegociacaoContaReceberVO().getDescontoProgressivoVO().getCodigo())){
                 	existeDesconto = true;
                 }
             }
             if(!existeDesconto && Uteis.isAtributoPreenchido(getNegociacaoContaReceberVO()) 
             		&& Uteis.isAtributoPreenchido(getNegociacaoContaReceberVO().getDescontoProgressivoVO())){
             	getNegociacaoContaReceberVO().setDescontoProgressivoVO(getFacadeFactory().getDescontoProgressivoFacade().consultarPorChavePrimaria(getNegociacaoContaReceberVO().getDescontoProgressivoVO().getCodigo(), getUsuarioLogado()));
                 objs.add(new SelectItem(getNegociacaoContaReceberVO().getDescontoProgressivoVO().getCodigo(), getNegociacaoContaReceberVO().getDescontoProgressivoVO().getNome().toString()));
             }
             setListaSelectItemDescontoProgressivo(objs);
         } catch (Exception e) {
             throw e;
         } finally {
             Uteis.liberarListaMemoria(resultadoConsulta);
             i = null;
         }
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>UnidadeEnsino</code>. Buscando todos os
     * objetos correspondentes a entidade <code>UnidadeEnsino</code>. Esta rotina não recebe parâmetros para filtragem
     * de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemUnidadeEnsino() throws Exception {
        try {
            montarListaSelectItemUnidadeEnsino("");
        } catch (Exception e) {
            //System.out.println(e.getMessage());
            throw e;
        }
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nome</code> Este
     * atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox
     * correspondente
     */
    public List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
        List<UnidadeEnsinoVO> lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, 0, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
        return lista;
    }

    /**
     * Método responsável por inicializar a lista de valores (<code>SelectItem</code>) para todos os ComboBox's.
     */
    public void inicializarListasSelectItemTodosComboBox() throws Exception {
//        montarListaSelectItemUnidadeEnsino();
        montarListaSelectItemContaCorrente();
        montarListaSelectItemDescontoProgressivo();
    }

    /**
     * Rotina responsável por preencher a combo de consulta da telas.
     */
    public List<SelectItem> tipoConsultaCombo;
    public List<SelectItem> getTipoConsultaCombo() {
		if (tipoConsultaCombo == null) {
			tipoConsultaCombo = new ArrayList<SelectItem>(0);
			tipoConsultaCombo.add(new SelectItem("nomeMatricula", "Nome Aluno"));
			tipoConsultaCombo.add(new SelectItem("responsavelFinanceiro", "Nome Responsável Financeiro"));
			tipoConsultaCombo.add(new SelectItem("matriculaMatricula", "Matrícula Aluno"));
			tipoConsultaCombo.add(new SelectItem("matriculaFuncionario", "Matrícula Funcionário"));
			tipoConsultaCombo.add(new SelectItem("nomeFuncionario", "Nome Funcionário"));
			tipoConsultaCombo.add(new SelectItem("nomeFornecedor", "Nome Fornecedor"));
			tipoConsultaCombo.add(new SelectItem("nomeParceiro", "Nome Parceiro"));
			tipoConsultaCombo.add(new SelectItem("identificadorCentroReceitaCentroReceita", "Centro Receita"));
			tipoConsultaCombo.add(new SelectItem("nossoNumeroContaAntiga", "Nosso Número Conta Negociada"));
			tipoConsultaCombo.add(new SelectItem("nossoNumeroNovaConta", "Nosso Número Conta Gerada"));
			tipoConsultaCombo.add(new SelectItem("codigo", "Código"));
    }
        return tipoConsultaCombo;
    }

    public List<SelectItem> tipoConsultaComboDesconto;
    public List<SelectItem> getTipoConsultaComboDesconto() {
    	if(tipoConsultaComboDesconto == null) {
    		tipoConsultaComboDesconto = new ArrayList<SelectItem>(0);
    		tipoConsultaComboDesconto.add(new SelectItem("porcentagem", "Desconto(%)"));
    		tipoConsultaComboDesconto.add(new SelectItem("dinheiro", "Desconto(R$)"));
    }
        return tipoConsultaComboDesconto;
    }

    public void calcularValorTotalRenegociacao() {
    	getNegociacaoContaReceberVO().calcularValorTotal();
    }

    public void calcularValorTotalRenegociacaoAcrescimo() {
    		getNegociacaoContaReceberVO().calcularValorTotal();
    }
    
    public void consultarParceiro() {
        try {
            super.consultar();
            List<ParceiroVO> objs = new ArrayList<ParceiroVO>(0);
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
            setListaConsultaParceiro(new ArrayList<ParceiroVO>(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarParceiro() {
        try {
            ParceiroVO obj = (ParceiroVO) context().getExternalContext().getRequestMap().get("parceiroItens");
            getNegociacaoContaReceberVO().setParceiro(obj);
            getNegociacaoContaReceberVO().setMatriculaAluno(new MatriculaVO());
            limparListaContaReceber();
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
    

    public List<SelectItem> tipoConsultaComboParceiro;
    public List<SelectItem> getTipoConsultaComboParceiro() {
    	if(tipoConsultaComboParceiro == null) {
    		tipoConsultaComboParceiro = new ArrayList<SelectItem>(0);
    		tipoConsultaComboParceiro.add(new SelectItem("nome", "Nome"));
    		tipoConsultaComboParceiro.add(new SelectItem("razaoSocial", "Razão Social"));
    		tipoConsultaComboParceiro.add(new SelectItem("RG", "RG"));
    		tipoConsultaComboParceiro.add(new SelectItem("CPF", "CPF"));
    		tipoConsultaComboParceiro.add(new SelectItem("tipoParceiro", "Tipo Parceiro"));
    }
        return tipoConsultaComboParceiro;
    }

    public boolean isCampoData() {
        return getControleConsulta().getCampoConsulta().equals("data");
    }

    /**
     * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
     */
    public String inicializarConsultar() {
    	setControleConsultaOtimizado(new DataModelo());
        setMensagemID("msg_entre_prmconsulta");
        getControleConsultaOtimizado().setDataIni(Uteis.obterDataAntiga(new Date(), 30));
        return Uteis.getCaminhoRedirecionamentoNavegacao("negociacaoContaReceberCons.xhtml");
    }

    public void emitirTermoReconhecimentoDivida() {
        try {
            setTermoReconhecimentoDividaRelControle(null);
            setTermoReconhecimentoDividaRelControle((TermoReconhecimentoDividaRelControle) context().getExternalContext().getSessionMap().get(TermoReconhecimentoDividaRelControle.class.getSimpleName()));
            if (getTermoReconhecimentoDividaRelControle() == null) {
                setTermoReconhecimentoDividaRelControle(new TermoReconhecimentoDividaRelControle());
                context().getExternalContext().getSessionMap().put(TermoReconhecimentoDividaRelControle.class.getSimpleName(), getTermoReconhecimentoDividaRelControle());
            }
            if (!getNegociacaoContaReceberVO().getCodigo().equals(0)) {
                getTermoReconhecimentoDividaRelControle().setNegociacaoContaReceberVO(getNegociacaoContaReceberVO());
                getTermoReconhecimentoDividaRelControle().setObservacaoHistorico(getObservacaoHistorico());
                getTermoReconhecimentoDividaRelControle().setObservacaoComplementar(getObservacaoComplementar());
                //getTermoReconhecimentoDividaRelControle().setFazerDownload(Boolean.TRUE);
                getTermoReconhecimentoDividaRelControle().setAlunoVO(getNegociacaoContaReceberVO().getMatriculaAluno());
            	getTermoReconhecimentoDividaRelControle().setTipoLayout(getTipoLayout());
            	getTermoReconhecimentoDividaRelControle().setTextoPadraoDeclaracao(getTextoPadraoDeclaracao());
                getTermoReconhecimentoDividaRelControle().imprimirPDF();
                setMensagemID("msg_relatorio_ok");
            } else {
                setMensagemID("msg_relatorio_sem_dados");
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
    
    public void atualizarParcelasNegociacao() {
    	try {    		
    		getFacadeFactory().getNegociacaoContaReceberFacade().realizarAtualizacaoOpcoesPagamentoConformeItemCondicaoNegociacao(getNegociacaoContaReceberVO());
	    	getListaSelectItemOpcaoRenegociacao().clear();//	    	
	    	for(OpcaoAlunoCondicaoRenegociacaoVO opcao: getNegociacaoContaReceberVO().getItemCondicaoRenegociacao().getOpcaoAlunoCondicaoRenegociacaoVOs()){	    		
	    			getListaSelectItemOpcaoRenegociacao().add(new SelectItem(opcao.getNumeroParcela(), opcao.getDescricao()));	    	
	        }
	    	limparMensagem();
    	} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
    }
    
    public String getMensagemFaixaEntradaApresentar() {
    	return UteisJSF.internacionalizar("msg_ItemRenociacaoContaReceber_informeUmValorEntre")
				.replace("{0}", " " + Uteis.formatarDoubleParaMoeda(getNegociacaoContaReceberVO().getValorMinimoEntrada()))
				.replace("{1}", " " + Uteis.formatarDoubleParaMoeda(getNegociacaoContaReceberVO().getValorMaximoEntrada()));
	}
    
    public String dataVencimentoApresentar() {
		return UteisData.getDataAplicandoFormatacao(UteisData.adicionarDiasEmData(new Date(), getNegociacaoContaReceberVO().getItemCondicaoRenegociacao().getQtdeDiasEntrada()), "dd/MM/yyyy");
	}

    public void validarValorEntrada() {
    	try {
    		getFacadeFactory().getNegociacaoContaReceberFacade().validarValorEntrada(getNegociacaoContaReceberVO());
    		limparMensagem();
    	} catch (Exception e) {
    		getNegociacaoContaReceberVO().setValorEntrada(0.0);
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		} finally {
			atualizarParcelasNegociacao();			
		}
    }
    
    public void montarComboBoxItemCondicaoRenegociacao() {
		try {
			setListaSelectItemItemCondicaoRenegociacao(new ArrayList<>());
			getNegociacaoContaReceberVO().getItemCondicaoRenegociacaoVOs().sort((p1, p2) -> p1.getFaixaEntradaInicial().compareTo(p2.getFaixaEntradaInicial()));
			for(ItemCondicaoRenegociacaoVO itemCondicaoRenegociacaoVO : getNegociacaoContaReceberVO().getItemCondicaoRenegociacaoVOs()) {
				getListaSelectItemItemCondicaoRenegociacao().add(new SelectItem(itemCondicaoRenegociacaoVO.getCodigo(), itemCondicaoRenegociacaoVO.getDescricao()));
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
    
    public boolean getApresentarValorFinalRenegociado() {
    	return getNegociacaoContaReceberVO().getValorDescontoIsencaoJuroMultaIndiceReajuste() > 0 
    			|| getNegociacaoContaReceberVO().getValorAcrescimoCondicaoRenegociacao() > 0.0
    			|| getNegociacaoContaReceberVO().getValorDescontoCondicaoRenegociacao() > 0.0;
    }
    
    public List<SelectItem> listaTipoLayout;
	public List<SelectItem> getListaTipoLayout() {
		if(listaTipoLayout == null) {
			listaTipoLayout = new ArrayList<SelectItem>(0);
			listaTipoLayout.add(new SelectItem("TermoReconhecimentoDividaRel", "Layout 1"));
			if (!getNegociacaoContaReceberVO().getTipoFornecedor()) {
				listaTipoLayout.add(new SelectItem("TermoReconhecimentoDividaLayout3Rel", "Layout 3"));
			}
			listaTipoLayout.add(new SelectItem("TextoPadrao", "Texto Padrão"));
		}
		return listaTipoLayout;
	}
	
	public void setListaTipoLayout(List<SelectItem> listaTipoLayout) {
		this.listaTipoLayout = listaTipoLayout;
	}

    public ContaReceberNegociadoVO getContaReceberNegociadoVO() {
        if (contaReceberNegociadoVO == null) {
            contaReceberNegociadoVO = new ContaReceberNegociadoVO();
        }
        return contaReceberNegociadoVO;
    }

    public void setContaReceberNegociadoVO(ContaReceberNegociadoVO contaReceberNegociadoVO) {
        this.contaReceberNegociadoVO = contaReceberNegociadoVO;
    }

    public List<SelectItem> getListaSelectItemContaCorrente() {
        if (listaSelectItemContaCorrente == null) {
            listaSelectItemContaCorrente = new ArrayList<SelectItem>(0);
        }
        return (listaSelectItemContaCorrente);
    }

    public void setListaSelectItemContaCorrente(List<SelectItem> listaSelectItemContaCorrente) {
        this.listaSelectItemContaCorrente = listaSelectItemContaCorrente;
    }

    public List<SelectItem> getListaSelectItemUnidadeEnsino() {
        if (listaSelectItemUnidadeEnsino == null) {
            listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>(0);
            try {
				montarListaSelectItemUnidadeEnsino();
			} catch (Exception e) {
				e.printStackTrace();
        }
        }
        return (listaSelectItemUnidadeEnsino);
    }

    public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
        this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
    }

    public String getCampoConsultaAluno() {
        return campoConsultaAluno;
    }

    public void setCampoConsultaAluno(String campoConsultaAluno) {
        this.campoConsultaAluno = campoConsultaAluno;
    }

    public String getCampoConsultaCentroReceita() {
        return campoConsultaCentroReceita;
    }

    public void setCampoConsultaCentroReceita(String campoConsultaCentroReceita) {
        this.campoConsultaCentroReceita = campoConsultaCentroReceita;
    }

    public String getCampoConsultaFuncionario() {
        return campoConsultaFuncionario;
    }

    public void setCampoConsultaFuncionario(String campoConsultaFuncionario) {
        this.campoConsultaFuncionario = campoConsultaFuncionario;
    }

    public List<MatriculaVO> getListaConsultaAluno() {
        return listaConsultaAluno;
    }

    public void setListaConsultaAluno(List<MatriculaVO> listaConsultaAluno) {
        this.listaConsultaAluno = listaConsultaAluno;
    }

    public List<CentroReceitaVO> getListaConsultaCentroReceita() {
        return listaConsultaCentroReceita;
    }

    public void setListaConsultaCentroReceita(List<CentroReceitaVO> listaConsultaCentroReceita) {
        this.listaConsultaCentroReceita = listaConsultaCentroReceita;
    }

    public List<FuncionarioVO> getListaConsultaFuncionario() {
        return listaConsultaFuncionario;
    }

    public void setListaConsultaFuncionario(List<FuncionarioVO> listaConsultaFuncionario) {
        this.listaConsultaFuncionario = listaConsultaFuncionario;
    }

    public String getValorConsultaAluno() {
        return valorConsultaAluno;
    }

    public void setValorConsultaAluno(String valorConsultaAluno) {
        this.valorConsultaAluno = valorConsultaAluno;
    }

    public String getValorConsultaCentroReceita() {
        return valorConsultaCentroReceita;
    }

    public void setValorConsultaCentroReceita(String valorConsultaCentroReceita) {
        this.valorConsultaCentroReceita = valorConsultaCentroReceita;
    }

    public String getValorConsultaFuncionario() {
        return valorConsultaFuncionario;
    }

    public void setValorConsultaFuncionario(String valorConsultaFuncionario) {
        this.valorConsultaFuncionario = valorConsultaFuncionario;
    }

    public NegociacaoContaReceberVO getNegociacaoContaReceberVO() {
        if (negociacaoContaReceberVO == null) {
            negociacaoContaReceberVO = new NegociacaoContaReceberVO();
        }
        return negociacaoContaReceberVO;
    }

    public void setNegociacaoContaReceberVO(NegociacaoContaReceberVO negociacaoContaReceberVO) {
        this.negociacaoContaReceberVO = negociacaoContaReceberVO;
    }

    public String getCampoConsultaContaReceber() {
        return campoConsultaContaReceber;
    }

    public void setCampoConsultaContaReceber(String campoConsultaContaReceber) {
        this.campoConsultaContaReceber = campoConsultaContaReceber;
    }

    public List<ContaReceberVO> getListaConsultaContaReceber() {
    	if (listaConsultaContaReceber == null) {
    		listaConsultaContaReceber = new ArrayList<ContaReceberVO>(0);
    	}
        return listaConsultaContaReceber;
    }

    public void setListaConsultaContaReceber(List<ContaReceberVO> listaConsultaContaReceber) {
        this.listaConsultaContaReceber = listaConsultaContaReceber;
    }

    public String getValorConsultaContaReceber() {
        return valorConsultaContaReceber;
    }

    public void setValorConsultaContaReceber(String valorConsultaContaReceber) {
        this.valorConsultaContaReceber = valorConsultaContaReceber;
    }

    public String getSituacaoContaReceber() {
        return situacaoContaReceber;
    }

    public void setSituacaoContaReceber(String situacaoContaReceber) {
        this.situacaoContaReceber = situacaoContaReceber;
    }

    public String getAbrirModalRenegociacao() {
        if (abrirModalRenegociacao == null) {
            abrirModalRenegociacao = "";
        }
        return abrirModalRenegociacao;
    }

    public void setAbrirModalRenegociacao(String abrirModalRenegociacao) {
        this.abrirModalRenegociacao = abrirModalRenegociacao;
    }

    /**
     * @return the listaSelectItemDescontoProgressivo
     */
    public List<SelectItem> getListaSelectItemDescontoProgressivo() {
        if (listaSelectItemDescontoProgressivo == null) {
            listaSelectItemDescontoProgressivo = new ArrayList<SelectItem>(0);
        }
        return listaSelectItemDescontoProgressivo;
    }

    /**
     * @param listaSelectItemDescontoProgressivo the listaSelectItemDescontoProgressivo to set
     */
    public void setListaSelectItemDescontoProgressivo(List<SelectItem> listaSelectItemDescontoProgressivo) {
        this.listaSelectItemDescontoProgressivo = listaSelectItemDescontoProgressivo;
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

    public void setListaAlunosParceiro(List<MatriculaVO> listaAlunosParceiro) {
        this.listaAlunosParceiro = listaAlunosParceiro;
    }

    public List<MatriculaVO> getListaAlunosParceiro() {
        if (listaAlunosParceiro == null) {
            listaAlunosParceiro = new ArrayList<MatriculaVO>(0);
        }
        return listaAlunosParceiro;
    }

    public boolean getIsDesabilitarUnidadeEnsino() {
        return !getNegociacaoContaReceberVO().getMatriculaAluno().getMatricula().equals("")
                || !getNegociacaoContaReceberVO().getFuncionario().getMatricula().equals("");
    }

    public TermoReconhecimentoDividaRelControle getTermoReconhecimentoDividaRelControle() {
        return termoReconhecimentoDividaRelControle;
    }

    public void setTermoReconhecimentoDividaRelControle(TermoReconhecimentoDividaRelControle termoReconhecimentoDividaRelControle) {
        this.termoReconhecimentoDividaRelControle = termoReconhecimentoDividaRelControle;
    }

    public String getObservacaoHistorico() {
        preencherListaNovaContaReceber();
        if (observacaoHistorico == null && !getNegociacaoContaReceberVO().getNovaContaReceber().isEmpty()) {
            ContaReceberVO obj0 = (ContaReceberVO) getNegociacaoContaReceberVO().getNovaContaReceber().get(0);
            ContaReceberVO obj1 = null;
            if (getNegociacaoContaReceberVO().getNovaContaReceber().size() > 1) {
                obj1 = (ContaReceberVO) getNegociacaoContaReceberVO().getNovaContaReceber().get(1);
            }
            observacaoHistorico = "O aluno fez acordo no total de R$ " + Uteis.formatarDecimalDuasCasas(getValorTotalNovasParcelas(getNegociacaoContaReceberVO().getNovaContaReceber())).toString().replace('.', ',') + " com entrada de R$ " + Uteis.formatarDecimalDuasCasas(obj0.getValor()).toString().replace('.', ',');
            if (obj1 != null && !obj1.getCodigo().equals(0)) {
                observacaoHistorico += " e mais " + (getNegociacaoContaReceberVO().getNovaContaReceber().size() - 1) + " x de R$ " + obj1.getValor().toString().replace('.', ',');
            }
        }
        return observacaoHistorico;
    }

    public void emitirBoleto() {
        try {
        	List<BoletoBancarioRelVO> boletoBancarioRelVOs = getFacadeFactory().getBoletoBancarioRelFacade().emitirRelatorioLista(null, null,"", "", "", "", 0, 0, null, null, 0, "negociação", 0, getUsuarioLogado(), "", getNegociacaoContaReceberVO().getCodigo(),getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getNegociacaoContaReceberVO().getUnidadeEnsino().getCodigo()),0, false, null, null);
        	getFacadeFactory().getBoletoBancarioRelFacade().realizarImpressaoPDF(boletoBancarioRelVOs, getSuperParametroRelVO(), getVersaoSistema(), getTipoBoleto(), 
        			getUsuarioLogado());
			realizarImpressaoRelatorio();            
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void preencherListaNovaContaReceber() {
        try {
            if (getNegociacaoContaReceberVO().getNovaContaReceber().isEmpty()) {
                getNegociacaoContaReceberVO().setNovaContaReceber(getFacadeFactory().getTermoReconhecimentoDividaRelFacade().consultarNovaContaReceber(getNegociacaoContaReceberVO(), false, getUsuarioLogado()));
                for(ContaReceberVO contaReceberVO: getNegociacaoContaReceberVO().getNovaContaReceber()){
                	getFacadeFactory().getContaReceberFacade().carregarDados(contaReceberVO, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getNegociacaoContaReceberVO().getUnidadeEnsino().getCodigo()), getUsuarioLogado());
                }
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
    
    public void montarListaSelectItemTextoPadrao() {
		try {
	        if (getTipoLayout().equals("TextoPadrao")) {
	        	consultarListaSelectItemTipoTextoPadrao(0);
	        }
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
    public void consultarListaSelectItemTipoTextoPadrao(Integer unidadeEnsino) {
        try {
            getListaSelectItemTipoTextoPadrao().clear();
            List<TextoPadraoDeclaracaoVO> lista = getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorTipo("RD", unidadeEnsino, "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            for (TextoPadraoDeclaracaoVO objeto : lista) {
                getListaSelectItemTipoTextoPadrao().add(new SelectItem(objeto.getCodigo(), objeto.getDescricao()));
            }
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
    
	public String getVisualizarRelatorio() {
		if (getTermoReconhecimentoDividaRelControle() != null) {
			if (getTermoReconhecimentoDividaRelControle().getFazerDownload()) {
				return getTermoReconhecimentoDividaRelControle().getDownload();
			} else {
				return "abrirPopup('../../VisualizarContrato', 'RelatorioContrato', 730, 545);";
			}
		}
		return "";
	}

    public Double getValorTotalNovasParcelas(List<ContaReceberVO> lista) {
        Double total = 0.0;
        for (ContaReceberVO obj : lista) {
            total += obj.getValor();
        }
        return total;
    }

    public void setObservacaoHistorico(String observacaoHistorico) {
        this.observacaoHistorico = observacaoHistorico;
    }

    public Boolean getPossibilidadeImprimirTermoReconhecimentoDivida() {
        if (!getNegociacaoContaReceberVO().getCodigo().equals(0)) {
            return true;
        }
        return false;
    }
    
    public boolean isPossibilidadeImprimirBoleto() {
    	try {
    		if (getNegociacaoContaReceberVO().getCodigo().equals(0)) {
    			return false;
    		}
    		Boolean possuiPermissaoEmitirBoletoVencido = ControleAcesso.verificarPermissaoFuncionalidadeUsuario("ImprimirBoletoVencidoVisaoAluno", getUsuarioLogado());
    		for (ContaReceberVO obj : getNegociacaoContaReceberVO().getNovaContaReceber()) {
    			getFacadeFactory().getContaReceberFacade().validarTipoImpressaoPorContaReceber(obj, possuiPermissaoEmitirBoletoVencido, getUsuarioLogado());
    			if(!obj.getPermiteImprimirBoleto() && !obj.getPermiteImprimirBoletoLinkBanco()){
    				return false;
    			}
    		}
    		inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
    	return true;
    }

    public BoletosRelControle getBoletosRelControle() {
        return boletosRelControle;
    }

    public void setBoletosRelControle(BoletosRelControle boletosRelControle) {
        this.boletosRelControle = boletosRelControle;
    }

    private List<SelectItem> listaSelectItemTipoDesconto;   
    public List<SelectItem> getListaSelectItemTipoDesconto() throws Exception {   
    	if(listaSelectItemTipoDesconto == null){
    		listaSelectItemTipoDesconto = new ArrayList<SelectItem>(0);
    		listaSelectItemTipoDesconto.add(new SelectItem(TipoDescontoAluno.PORCENTO.getValor(), TipoDescontoAluno.PORCENTO.getSimbolo()));
    		listaSelectItemTipoDesconto.add(new SelectItem(TipoDescontoAluno.VALOR.getValor(), TipoDescontoAluno.VALOR.getSimbolo()));
    	}
    	return listaSelectItemTipoDesconto;
    }

    public Double getValorTotalApresentar() {
        if (valorTotalApresentar == null) {
            valorTotalApresentar = 0.0;
        }
        return valorTotalApresentar;
    }

    public void setValorTotalApresentar(Double valorTotalApresentar) {
        this.valorTotalApresentar = valorTotalApresentar;
    }

    /**
     * @return the descricaoPeriodoLetivoAluno
     */
    public String getDescricaoPeriodoLetivoAluno() {
        if (descricaoPeriodoLetivoAluno == null) {
            descricaoPeriodoLetivoAluno = "";
        }
        return descricaoPeriodoLetivoAluno;
    }

    /**
     * @param descricaoPeriodoLetivoAluno the descricaoPeriodoLetivoAluno to set
     */
    public void setDescricaoPeriodoLetivoAluno(String descricaoPeriodoLetivoAluno) {
        this.descricaoPeriodoLetivoAluno = descricaoPeriodoLetivoAluno;
    }

    /**
     * @return the tipoBoleto
     */
    public String getTipoBoleto() {
        if (tipoBoleto == null) {
            tipoBoleto = "";
        }
        return tipoBoleto;
    }

    /**
     * @param tipoBoleto the tipoBoleto to set
     */
    public void setTipoBoleto(String tipoBoleto) {
        this.tipoBoleto = tipoBoleto;
    }
    
    protected List<PessoaVO> listaConsultaResponsavelFinanceiro;
    protected String valorConsultaResponsavelFinanceiro;
    protected String campoConsultaResponsavelFinanceiro;
    
    
    public void consultarResponsavelFinanceiro() {
        try {
        	Integer codigoUnidadeEnsino = Uteis.isAtributoPreenchido(getNegociacaoContaReceberVO().getUnidadeEnsino()) ? getNegociacaoContaReceberVO().getUnidadeEnsino().getCodigo() : this.getUnidadeEnsinoLogado().getCodigo();
            if (getValorConsultaResponsavelFinanceiro().trim().equals("")) {
                setMensagemID("msg_entre_prmconsulta");
                return;
            }
            getFacadeFactory().getPessoaFacade().setIdEntidade("ResponsavelFinanceiro");
            getListaConsultaResponsavelFinanceiro().clear();
            if (getCampoConsultaResponsavelFinanceiro().equals("nome")) {
                setListaConsultaResponsavelFinanceiro(getFacadeFactory().getPessoaFacade().consultaRapidaPorNomeResponsavelFinanceiro(getValorConsultaResponsavelFinanceiro(), codigoUnidadeEnsino, false, getUsuarioLogado()));
            }
            if (getCampoConsultaResponsavelFinanceiro().equals("nomeAluno")) {
                setListaConsultaResponsavelFinanceiro(getFacadeFactory().getPessoaFacade().consultaRapidaPorNomeAlunoResponsavelFinanceiro(getValorConsultaResponsavelFinanceiro(), codigoUnidadeEnsino, false, getUsuarioLogado()));
            }
            if (getCampoConsultaResponsavelFinanceiro().equals("CPF")) {
                setListaConsultaResponsavelFinanceiro(getFacadeFactory().getPessoaFacade().consultaRapidaPorCpfResponsavelFinanceiro(getValorConsultaResponsavelFinanceiro(), codigoUnidadeEnsino, false, getUsuarioLogado()));
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
            this.getNegociacaoContaReceberVO().setPessoa(obj);
            limparListaContaReceber();
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

	public String getUsernameLiberarDesconto() {
		if (usernameLiberarDesconto == null) {
			usernameLiberarDesconto = "";
		}
		return usernameLiberarDesconto;
	}

	public void setUsernameLiberarDesconto(String usernameLiberarDesconto) {
		this.usernameLiberarDesconto = usernameLiberarDesconto;
	}

	public String getSenhaLiberarDesconto() {
		if (senhaLiberarDesconto == null) {
			senhaLiberarDesconto = "";
		}
		return senhaLiberarDesconto;
	}

	public void setSenhaLiberarDesconto(String senhaLiberarDesconto) {
		this.senhaLiberarDesconto = senhaLiberarDesconto;
	}

	public Boolean getApresentarBotaoLiberarDesconto() {
		if (apresentarBotaoLiberarDesconto == null) {
			apresentarBotaoLiberarDesconto = Boolean.TRUE;
		}
		return apresentarBotaoLiberarDesconto;
	}

	public void setApresentarBotaoLiberarDesconto(Boolean apresentarBotaoLiberarDesconto) {
		this.apresentarBotaoLiberarDesconto = apresentarBotaoLiberarDesconto;
	}

	public String getTipoLayout() {
		if (tipoLayout == null) {
			tipoLayout = "";
		}
		return tipoLayout;
	}

	public void setTipoLayout(String tipoLayout) {
		this.tipoLayout = tipoLayout;
	}
	
	public ContaReceberVO getContaReceberVO() {
		if (contaReceberVO == null) {
			contaReceberVO = new ContaReceberVO();
		}
		return contaReceberVO;
	}

	public void setContaReceberVO(ContaReceberVO contaReceberVO) {
		this.contaReceberVO = contaReceberVO;
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
	
	public List<SelectItem> getListaSelectItemCaixa() {
		if (listaSelectItemCaixa == null) {
			listaSelectItemCaixa = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemCaixa;
	}

	public void setListaSelectItemCaixa(List<SelectItem> listaSelectItemCaixa) {
		this.listaSelectItemCaixa = listaSelectItemCaixa;
	}

	public String getApresentarModalEstornoRenegociacao() {
		if(apresentarModalEstornoRenegociacao == null){
			apresentarModalEstornoRenegociacao = "";
		}
		return apresentarModalEstornoRenegociacao;
	}

	public void setApresentarModalEstornoRenegociacao(String apresentarModalEstornoRenegociacao) {
		this.apresentarModalEstornoRenegociacao = apresentarModalEstornoRenegociacao;
	}


	public Boolean getDesconsiderarDescontoProgressivo() {
		if(desconsiderarDescontoProgressivo == null){
			desconsiderarDescontoProgressivo = false;
		}
		return desconsiderarDescontoProgressivo;
	}

	public void setDesconsiderarDescontoProgressivo(Boolean desconsiderarDescontoProgressivo) {
		this.desconsiderarDescontoProgressivo = desconsiderarDescontoProgressivo;
	}

	public Boolean getDesconsiderarDescontoAluno() {
		if(desconsiderarDescontoAluno == null){
			desconsiderarDescontoAluno = false;
		}
		return desconsiderarDescontoAluno;
	}

	public void setDesconsiderarDescontoAluno(Boolean desconsiderarDescontoAluno) {
		this.desconsiderarDescontoAluno = desconsiderarDescontoAluno;
	}

	public Boolean getDesconsiderarDescontoInstituicaoComValidade() {
		if(desconsiderarDescontoInstituicaoComValidade == null){
			desconsiderarDescontoInstituicaoComValidade = false;
		}
		return desconsiderarDescontoInstituicaoComValidade;
	}

	public void setDesconsiderarDescontoInstituicaoComValidade(Boolean desconsiderarDescontoInstituicaoComValidade) {
		this.desconsiderarDescontoInstituicaoComValidade = desconsiderarDescontoInstituicaoComValidade;
	}

	public Boolean getDesconsiderarDescontoInstituicaoSemValidade() {
		if(desconsiderarDescontoInstituicaoSemValidade == null){
			desconsiderarDescontoInstituicaoSemValidade = false;
		}
		return desconsiderarDescontoInstituicaoSemValidade;
	}

	public void setDesconsiderarDescontoInstituicaoSemValidade(Boolean desconsiderarDescontoInstituicaoSemValidade) {
		this.desconsiderarDescontoInstituicaoSemValidade = desconsiderarDescontoInstituicaoSemValidade;
	}
 
	public void realizarCalculoValorContaReceberAdicionar(){
		try{
			getFacadeFactory().getContaReceberNegociadoFacade().realizarCalculoValorContaReceberDesconsiderandoDescontos(getContaReceberNegociadoVO());
			getFacadeFactory().getNegociacaoContaReceberFacade().realizarCalculoTotal(getNegociacaoContaReceberVO());
			limparMensagem();
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void realizarCalculoValorTodasContaReceberAdicionar(){
		try{
			getFacadeFactory().getNegociacaoContaReceberFacade().realizarCalculoValorTodasContaReceberAdicionar(getNegociacaoContaReceberVO(), getDesconsiderarDescontoProgressivo(), getDesconsiderarDescontoAluno(), getDesconsiderarDescontoInstituicaoComValidade(), getDesconsiderarDescontoInstituicaoSemValidade());			
			limparMensagem();
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public TipoAcrescimoEnum getTipoAcrescimo() {
		if(tipoAcrescimo == null){
			tipoAcrescimo =  TipoAcrescimoEnum.PORCENTAGEM;
		}
		return tipoAcrescimo;
	}

	public void setTipoAcrescimo(TipoAcrescimoEnum tipoAcrescimo) {
		this.tipoAcrescimo = tipoAcrescimo;
	}

	public Double getAcrescimoPorParcela() {
		if(acrescimoPorParcela == null){
			acrescimoPorParcela = 0.0;
		}
		return acrescimoPorParcela;
	}

	public void setAcrescimoPorParcela(Double acrescimoPorParcela) {
		this.acrescimoPorParcela = acrescimoPorParcela;
	}

	public List<SelectItem> getListaSelectItemTipoAcrescimo() {
		if(listaSelectItemTipoAcrescimo == null){
			listaSelectItemTipoAcrescimo = UtilSelectItem.getListaSelectItemEnum(TipoAcrescimoEnum.values(), Obrigatorio.SIM);
		}
		return listaSelectItemTipoAcrescimo;
	}

	public void setListaSelectItemTipoAcrescimo(List<SelectItem> listaSelectItemTipoAcrescimo) {
		this.listaSelectItemTipoAcrescimo = listaSelectItemTipoAcrescimo;
	}
	

	public void realizarCancelamentoGeracaoParcelas(){
		setAcrescimoPorParcela(0.0);
		getNegociacaoContaReceberVO().setAcrescimoPorParcela(0.0);
		getNegociacaoContaReceberVO().setTotalAcrescimoPorParcela(0.0);
		getNegociacaoContaReceberVO().getNovaContaReceber().clear();
		if(getApresentarDadosOpcaoRenegociacao()) {
			atualizarParcelasNegociacao();
		}
//		if(Uteis.isAtributoPreenchido(getNegociacaoContaReceberVO().getValorDescontoCondicaoRenegociacao())){
//			getNegociacaoContaReceberVO().setDesconto(getNegociacaoContaReceberVO().getDesconto() - getNegociacaoContaReceberVO().getValorDescontoCondicaoRenegociacao());
//			getNegociacaoContaReceberVO().setValorDescontoCondicaoRenegociacao(0.0);
//		}
//		if(Uteis.isAtributoPreenchido(getNegociacaoContaReceberVO().getValorAcrescimoCondicaoRenegociacao())){
//			getNegociacaoContaReceberVO().setAcrescimoGeral(getNegociacaoContaReceberVO().getAcrescimoGeral() - getNegociacaoContaReceberVO().getValorAcrescimoCondicaoRenegociacao());
//			getNegociacaoContaReceberVO().setValorAcrescimoCondicaoRenegociacao(0.0);
//		}
//		getNegociacaoContaReceberVO().calcularValorTotal();
	}

	public void realizarCalculoValorBaseParcela(){
		setValorBaseParcela(getFacadeFactory().getNegociacaoContaReceberFacade().realizarCalculoValorBaseParcela(getNegociacaoContaReceberVO(), getTipoAcrescimo(), getAcrescimoPorParcela()));		
	}

	public Double getValorBaseParcela() {
		if(valorBaseParcela == null){
			valorBaseParcela = 0.0;
		}
		return valorBaseParcela;
	}

	public void setValorBaseParcela(Double valorBaseParcela) {
		this.valorBaseParcela = valorBaseParcela;
	}
	

	public List<SelectItem> getListaSelectItemPlanoDesconto() {
		if(listaSelectItemPlanoDesconto == null){
			listaSelectItemPlanoDesconto = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemPlanoDesconto;
	}

	public void setListaSelectItemPlanoDesconto(List<SelectItem> listaSelectItemPlanoDesconto) {
		this.listaSelectItemPlanoDesconto = listaSelectItemPlanoDesconto;
	}
	
	@SuppressWarnings("unchecked")
	public void montarListaSelectItemPlanoDesconto() {
		try {
			List<PlanoDescontoVO> resultadoConsulta = consultarPlanoDescontoPorNome("");
			Iterator<PlanoDescontoVO> i = resultadoConsulta.iterator();
			List<SelectItem> objs = new ArrayList<SelectItem>(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				PlanoDescontoVO obj = (PlanoDescontoVO) i.next();
				if (obj.getUnidadeEnsinoVO().getCodigo().equals(0) || obj.getUnidadeEnsinoVO().getCodigo().equals(getNegociacaoContaReceberVO().getUnidadeEnsino().getCodigo())) {
					objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
				}
			}
			SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
			Collections.sort((List<SelectItem>) objs, ordenador);
			setListaSelectItemPlanoDesconto(objs);
		} catch (Exception e) {
			// System.out.println(e.getMessage());
		}
	}
	
	public List<PlanoDescontoVO> consultarPlanoDescontoPorNome(String nomePrm) throws Exception {
		List<PlanoDescontoVO> lista = getFacadeFactory().getPlanoDescontoFacade().consultarPlanoDescontoAtivoPorUnidadeEnsinoNivelComboBox(getNegociacaoContaReceberVO().getUnidadeEnsino(), true, getUsuarioLogado());		
		return lista;
	}
	
	public void adicionarPlanoDesconto(){
		try {
			getFacadeFactory().getNegociacaoContaReceberFacade().adicionarPlanoDesconto(getNegociacaoContaReceberVO(), getPlanoDescontoAdicionar(), getUsuarioLogado());
			setPlanoDescontoAdicionar(0);
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}		
	}
	
	public void removerPlanoDesconto(){
		try {
			getFacadeFactory().getNegociacaoContaReceberFacade().removerPlanoDesconto(getNegociacaoContaReceberVO(), (NegociacaoContaReceberPlanoDescontoVO) getRequestMap().get("planoDescontoItem"));
			setMensagemID("msg_dados_removidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public Integer getPlanoDescontoAdicionar() {
		if(planoDescontoAdicionar == null){
			planoDescontoAdicionar = 0;
		}
		return planoDescontoAdicionar;
	}

	public void setPlanoDescontoAdicionar(Integer planoDescontoAdicionar) {
		this.planoDescontoAdicionar = planoDescontoAdicionar;
	}
	
	public void carregarDadosFinanceiros(){
		try {
			limparListaContaReceber();
			getNegociacaoContaReceberVO().adicionarOrdemAplicacaoDesconto(getNegociacaoContaReceberVO().getMatriculaAluno().getPlanoFinanceiroAluno(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getNegociacaoContaReceberVO().getUnidadeEnsino().getCodigo()));
			if (getNegociacaoContaReceberVO().getUnidadeEnsino().getCodigo().intValue() > 0) {
				getNegociacaoContaReceberVO().setUnidadeEnsino(getAplicacaoControle().getUnidadeEnsinoVO(getNegociacaoContaReceberVO().getUnidadeEnsino().getCodigo(), getUsuarioLogado()));
				getNegociacaoContaReceberVO().setCondicaoRenegociacao(getFacadeFactory().getCondicaoRenegociacaoFacade().consultarCondicaoRenegociacaoPorUnidadeEnsino(getNegociacaoContaReceberVO().getUnidadeEnsino().getCodigo(), true, getUsuarioLogado()));
			}
			montarListaSelectItemContaCorrente();
			montarListaSelectItemPlanoDesconto();			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}		
		
	}
	
	public void adicionarDescontoProgressivoPlanoFinanceiroAluno(){
		try {			
			getNegociacaoContaReceberVO().getDescontoProgressivoVO().setCodigo(getNegociacaoContaReceberVO().getMatriculaAluno().getPlanoFinanceiroAluno().getDescontoProgressivo().getCodigo());
			boolean existe = false;
			for(SelectItem item: getListaSelectItemDescontoProgressivo()) {
				if(item.getValue().equals(getNegociacaoContaReceberVO().getMatriculaAluno().getPlanoFinanceiroAluno().getDescontoProgressivo().getCodigo())) {
					existe = true;
					break;
				}
			}
			if(!existe) {
				getListaSelectItemDescontoProgressivo().add(new SelectItem(getNegociacaoContaReceberVO().getMatriculaAluno().getPlanoFinanceiroAluno().getDescontoProgressivo().getCodigo(), getNegociacaoContaReceberVO().getMatriculaAluno().getPlanoFinanceiroAluno().getDescontoProgressivo().getNome()));
			}
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void adicionarPlanoDescontoPlanoFinanceiroAluno(){
		try {						
			getNegociacaoContaReceberVO().getNegociacaoContaReceberPlanoDescontoVOs().clear();
			for(ItemPlanoFinanceiroAlunoVO itemPlanoFinanceiroAlunoVO : getNegociacaoContaReceberVO().getMatriculaAluno().getPlanoFinanceiroAluno().getItemPlanoFinanceiroAlunoVOs()){
				if(Uteis.isAtributoPreenchido(itemPlanoFinanceiroAlunoVO.getPlanoDesconto())){
					getFacadeFactory().getNegociacaoContaReceberFacade().adicionarPlanoDesconto(getNegociacaoContaReceberVO(), itemPlanoFinanceiroAlunoVO.getPlanoDesconto().getCodigo(), getUsuarioLogado());
				}
			}
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void selecionarContaReceberEmissoBoleto() {
		try {
			if (isPossibilidadeImprimirBoleto()) {
				setOncompleteImpressaoBoleto("RichFaces.$('panelEmissaoBoleto').show()");
			} else {
				setContaReceberVO(getNegociacaoContaReceberVO().getNovaContaReceber().stream().findFirst().orElse(new ContaReceberVO()));
				setOncompleteImpressaoBoleto("RichFaces.$('panelEmissaoBloqueada').show()");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void atualizarValorCheioOrdemDesconto() {
		OrdemDescontoVO ordemClicou = (OrdemDescontoVO) context().getExternalContext().getRequestMap().get("ordem");
		try {			
			getNegociacaoContaReceberVO().atualizarSituacaoValorCheioOrdemDesconto(ordemClicou);			
			setMensagemID("msg_entre_dados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void subirOrdemDesconto() {
		try {
			OrdemDescontoVO ordemSubir = (OrdemDescontoVO) context().getExternalContext().getRequestMap().get("ordem");			
			getNegociacaoContaReceberVO().alterarOrdemAplicacaoDescontosSubindoItem(ordemSubir);			
			setMensagemID("msg_entre_dados"); 
		} catch (Exception e) {			
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);			
		}
	}

	public void descerOrdemDesconto() {
		try {			
			getNegociacaoContaReceberVO().alterarOrdemAplicacaoDescontosDescentoItem((OrdemDescontoVO) context().getExternalContext().getRequestMap().get("ordem"));			
			setMensagemID("msg_entre_dados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);			
		}
	}


	public void limparListaContaReceberPorTipoPessoa() {
		getNegociacaoContaReceberVO().setParceiro(new ParceiroVO());
		getNegociacaoContaReceberVO().setMatriculaAluno(new MatriculaVO());
		getNegociacaoContaReceberVO().setPessoa( new PessoaVO());
		getNegociacaoContaReceberVO().setFuncionario(new FuncionarioVO());
		getNegociacaoContaReceberVO().setFornecedor(new FornecedorVO());
		limparListaContaReceber();
		inicializarMensagemVazia();
	}
	
	public void limparListaContaReceber() {
		getNegociacaoContaReceberVO().setValor(0.0);
		getNegociacaoContaReceberVO().setValorEntrada(0.0);
		getNegociacaoContaReceberVO().setAcrescimoGeral(0.0);
		getNegociacaoContaReceberVO().setTotalAcrescimoPorParcela(0.0);
		getNegociacaoContaReceberVO().setAcrescimoPorParcela(0.0);
		getNegociacaoContaReceberVO().setDesconto(0.0);
		getNegociacaoContaReceberVO().setValorTotal(0.0);
		getNegociacaoContaReceberVO().setDescontoProgressivoVO(new DescontoProgressivoVO());
		getNegociacaoContaReceberVO().getNegociacaoContaReceberPlanoDescontoVOs().clear();
		getNegociacaoContaReceberVO().getNovaContaReceber().clear();
		getNegociacaoContaReceberVO().getContaReceberNegociadoVOs().clear();		
	}

	public String getOncompleteImpressaoBoleto() {
		return oncompleteImpressaoBoleto;
	}

	public void setOncompleteImpressaoBoleto(String oncompleteImpressaoBoleto) {
		this.oncompleteImpressaoBoleto = oncompleteImpressaoBoleto;
	}
	
	public List<SelectItem> getListaSelectItemOpcaoRenegociacao() {
		if (listaSelectItemOpcaoRenegociacao == null) {
			listaSelectItemOpcaoRenegociacao = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemOpcaoRenegociacao;
	}

	public void setListaSelectItemOpcaoRenegociacao(List<SelectItem> listaSelectItemOpcaoRenegociacao) {
		this.listaSelectItemOpcaoRenegociacao = listaSelectItemOpcaoRenegociacao;
	}

	public Boolean getApresentarDadosOpcaoRenegociacao() {
		if (apresentarDadosOpcaoRenegociacao == null) {
			apresentarDadosOpcaoRenegociacao = false;
		}
		return apresentarDadosOpcaoRenegociacao;
	}

	public void setApresentarDadosOpcaoRenegociacao(Boolean apresentarDadosOpcaoRenegociacao) {
		this.apresentarDadosOpcaoRenegociacao = apresentarDadosOpcaoRenegociacao;
	}

	public List<ContaReceberVO> getListaContaReceberVOs() {
		if (listaContaReceberVOs == null) {
			listaContaReceberVOs = new ArrayList<ContaReceberVO>(0);
		}
		return listaContaReceberVOs;
	}

	public void setListaContaReceberVOs(List<ContaReceberVO> listaContaReceberVOs) {
		this.listaContaReceberVOs = listaContaReceberVOs;
	}

	public void verificarPermissaoRenegociacaoApenasComCondicaoRenegociacao() throws Exception {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("PermitirRenegociacaoApenasComCondicaoRenegociacao", getUsuarioLogado());
			getNegociacaoContaReceberVO().setPermitirRenegociacaoApenasComCondicaoRenegociacao(true);
			getNegociacaoContaReceberVO().setTipoDesconto("VA");
		} catch (Exception e) {
			getNegociacaoContaReceberVO().setPermitirRenegociacaoApenasComCondicaoRenegociacao(false);
			getNegociacaoContaReceberVO().setTipoDesconto("");
		}
	}

	public void verificarPermissaoInformarDescontoProg_Inst() throws Exception {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("PermitirInformarDescontoProgInstNegociacaoContaReceber", getUsuarioLogado());
			setPermitirInformarDescontoProg_Inst(true);
		} catch (Exception e) {
			setPermitirInformarDescontoProg_Inst(false);
		}
	}
	
	public void verificarPermissaoLiberarRenovacao() throws Exception {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("PermitirLiberarRenovacaoApoPagamento", getUsuarioLogado());
			setPermitirLiberarRenovacaoAposPagamento(true);
		} catch (Exception e) {
			setPermitirLiberarRenovacaoAposPagamento(false);
		}
	}
	
	public void verificarUsuarioPodeLiberarNegociacaoParaNaoValidarCondicaoRenegociacao() {
		try {
			UsuarioVO usuarioVerif = ControleAcesso.verificarLoginUsuario(this.getUserNameLiberarRenegociacaoParaNaoValidarCondicaoRenegociacao(), this.getSenhaLiberarRenegociacaoParaNaoValidarCondicaoRenegociacao(), true, Uteis.NIVELMONTARDADOS_TODOS);
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("LiberarRenegociacaoParaNaoValidarCondicaoRenegociacao", usuarioVerif);
			
			getNegociacaoContaReceberVO().setLiberarRenegociarDesativandoCondicaoRenegociacao(true);
			getNegociacaoContaReceberVO().setResponsavelLiberacaoRenegociarDesativandoCondicaoRenegociacao(usuarioVerif);
			getNegociacaoContaReceberVO().setDataLiberacaoRenegociarDesativandoCondicaoRenegociacao(new Date());			
			getNegociacaoContaReceberVO().setApresentarBotaoLiberarUsuarioRealizarNegociacaoDesativandoCondicaoRenegociacao(false);
			getNegociacaoContaReceberVO().setPermitirRenegociacaoApenasComCondicaoRenegociacao(false);			
			getNegociacaoContaReceberVO().setTipoDesconto("");
			this.setUserNameLiberarRenegociacaoParaNaoValidarCondicaoRenegociacao("");
			this.setSenhaLiberarRenegociacaoParaNaoValidarCondicaoRenegociacao("");
			setMensagemID("msg_PermissaoUsuarioRealizarMatriculaDisciplinaPreRequisito");
		} catch (Exception e) {
			this.setUserNameLiberarRenegociacaoParaNaoValidarCondicaoRenegociacao("");
			this.setSenhaLiberarRenegociacaoParaNaoValidarCondicaoRenegociacao("");
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void verificarLiberarUsuarioNaoVinculadoCondicaoRenegociacaoRealizarCondicao() {
		try {
			UsuarioVO usuarioVerif = ControleAcesso.verificarLoginUsuario(this.getUserNameLiberarUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao(), this.getSenhaLiberarUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao(), true, Uteis.NIVELMONTARDADOS_TODOS);
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("LiberarUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao", usuarioVerif);
			getNegociacaoContaReceberVO().setLiberarUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao(true);
			getNegociacaoContaReceberVO().setResponsavelLiberacaoUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao(usuarioVerif);
			getNegociacaoContaReceberVO().setDataLiberacaoUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao(new Date());
			getNegociacaoContaReceberVO().setApresentarBotaoLiberarUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao(false);
			
			this.setUserNameLiberarUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao("");
			this.setSenhaLiberarUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao("");
			setMensagemID("msg_PermissaoUsuarioRealizarMatriculaDisciplinaPreRequisito");
		} catch (Exception e) {
			this.setUserNameLiberarUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao("");
			this.setSenhaLiberarUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao("");
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public String getUserNameLiberarRenegociacaoParaNaoValidarCondicaoRenegociacao() {
		if (userNameLiberarRenegociacaoParaNaoValidarCondicaoRenegociacao == null) {
			userNameLiberarRenegociacaoParaNaoValidarCondicaoRenegociacao = "";
		}
		return userNameLiberarRenegociacaoParaNaoValidarCondicaoRenegociacao;
	}

	public void setUserNameLiberarRenegociacaoParaNaoValidarCondicaoRenegociacao(String userNameLiberarRenegociacaoParaNaoValidarCondicaoRenegociacao) {
		this.userNameLiberarRenegociacaoParaNaoValidarCondicaoRenegociacao = userNameLiberarRenegociacaoParaNaoValidarCondicaoRenegociacao;
	}

	public String getSenhaLiberarRenegociacaoParaNaoValidarCondicaoRenegociacao() {
		if (senhaLiberarRenegociacaoParaNaoValidarCondicaoRenegociacao == null) {
			senhaLiberarRenegociacaoParaNaoValidarCondicaoRenegociacao = "";
		}
		return senhaLiberarRenegociacaoParaNaoValidarCondicaoRenegociacao;
	}

	public void setSenhaLiberarRenegociacaoParaNaoValidarCondicaoRenegociacao(String senhaLiberarRenegociacaoParaNaoValidarCondicaoRenegociacao) {
		this.senhaLiberarRenegociacaoParaNaoValidarCondicaoRenegociacao = senhaLiberarRenegociacaoParaNaoValidarCondicaoRenegociacao;
	}
	
//	public Boolean getApresentarBotaoLiberarUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao() {
//		return getNegociacaoContaReceberVO().getPermitirRenegociacaoApenasComCondicaoRenegociacao() 
//		&& !getNegociacaoContaReceberVO().getCondicaoRenegociacao().getUsuarioPossuiPermissaoParaRealizarNegociacao(getUsuarioLogado().getPessoa().getCodigo())
//		&& !getNegociacaoContaReceberVO().getLiberarUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao();
//	}
//	
//	public Boolean getApresentarBotaoLiberarNegociacaoParaNaoValidarCondicaoRenegociacao() {
//		return getNegociacaoContaReceberVO().getPermitirRenegociacaoApenasComCondicaoRenegociacao() 
//		&& !getNegociacaoContaReceberVO().getCondicaoRenegociacao().getUsuarioPossuiPermissaoParaRealizarNegociacao(getUsuarioLogado().getPessoa().getCodigo())
//		&& !getNegociacaoContaReceberVO().getLiberarUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao();
//	}

	public String getUserNameLiberarUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao() {
		if (userNameLiberarUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao == null) {
			userNameLiberarUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao = "";
		}
		return userNameLiberarUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao;
	}

	public void setUserNameLiberarUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao(String userNameLiberarUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao) {
		this.userNameLiberarUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao = userNameLiberarUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao;
	}

	public String getSenhaLiberarUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao() {
		if (senhaLiberarUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao == null) {
			senhaLiberarUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao = "";
		}
		return senhaLiberarUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao;
	}

	public void setSenhaLiberarUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao(String senhaLiberarUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao) {
		this.senhaLiberarUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao = senhaLiberarUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao;
	}
	
	public boolean isApresentarValorIndiceReajustePorAtraso() {
		return apresentarValorIndiceReajustePorAtraso;
	}

	public void setApresentarValorIndiceReajustePorAtraso(boolean apresentarValorIndiceReajustePorAtraso) {
		this.apresentarValorIndiceReajustePorAtraso = apresentarValorIndiceReajustePorAtraso;
	}

	public String getHabilitarModalContaReceber() {
		if (habilitarModalContaReceber == null) {
			habilitarModalContaReceber = "";
		}
		return habilitarModalContaReceber;
	}

	public void setHabilitarModalContaReceber(String habilitarModalContaReceber) {
		this.habilitarModalContaReceber = habilitarModalContaReceber;
	}
	
	
	private Boolean liberarPessoaComissionada;
	
	public Boolean getLiberarPessoaComissionada() {
		if(liberarPessoaComissionada == null){
			liberarPessoaComissionada = false;
		}
		return liberarPessoaComissionada;
	}

	public void setLiberarPessoaComissionada(Boolean liberarPessoaComissionada) {
		this.liberarPessoaComissionada = liberarPessoaComissionada;
	}

	public boolean isLiberarPessoaComissionada() {
		try {
			if(!getLiberarPessoaComissionada()){
				ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoFinanceiroEnum.LIBERAR_PESSOA_COMISSIONADA_NEGOCIACAO_CONTA_RECEBER, getUsuarioLogado());
				liberarPessoaComissionada = true;
				inicializarMensagemVazia();
			}
		} catch (Exception e) {
			liberarPessoaComissionada= false;
		}
		return getLiberarPessoaComissionada();
	}
	
	
	public void carregarDadosLiberarFuncionalidadeIsencaoJuroMultaDesconto() {
		try {			
			setUserNameLiberarFuncionalidade("");
			setSenhaLiberarFuncionalidade("");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void realizarVerificacaoUsuarioLiberarIsencaoJuroMultaDesconto() {
		try {
			UsuarioVO usuarioVerif = ControleAcesso.verificarLoginUsuario(this.getUserNameLiberarFuncionalidade(), this.getSenhaLiberarFuncionalidade(), true, Uteis.NIVELMONTARDADOS_TODOS);
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoFinanceiroEnum.LIBERAR_ISENCAO_JURO_MULTA_DESCONTO_NEGOCIACAO_CONTA_RECEBER, usuarioVerif);
			getNegociacaoContaReceberVO().setLiberarIsencaoJuroMultaDescontoAcimaMaximo(true);
			getNegociacaoContaReceberVO().setUsuarioLiberarIsencaoJuroMultaDescontoAcimaMaximo(usuarioVerif);
			setUserNameLiberarFuncionalidade("");
			setSenhaLiberarFuncionalidade("");
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

	public Boolean getPermitirInformarDescontoProg_Inst() {
		if (permitirInformarDescontoProg_Inst == null) {
			permitirInformarDescontoProg_Inst = Boolean.FALSE;
		}
		return permitirInformarDescontoProg_Inst;
	}

	public void setPermitirInformarDescontoProg_Inst(Boolean permitirInformarDescontoProg_Inst) {
		this.permitirInformarDescontoProg_Inst = permitirInformarDescontoProg_Inst;
	}

	public Boolean getPermitirLiberarRenovacaoAposPagamento() {
		if (permitirLiberarRenovacaoAposPagamento == null) {
			permitirLiberarRenovacaoAposPagamento = Boolean.FALSE;
		}
		return permitirLiberarRenovacaoAposPagamento;
	}

	public void setPermitirLiberarRenovacaoAposPagamento(Boolean permitirLiberarRenovacaoAposPagamento) {
		this.permitirLiberarRenovacaoAposPagamento = permitirLiberarRenovacaoAposPagamento;
	}
	
	public String getResponsavelCadastro() {
		if(responsavelCadastro == null) {
			responsavelCadastro = "";
		}
		return responsavelCadastro;
	}
	
	public void setResponsavelCadastro(String responsavelCadastro) {
		this.responsavelCadastro = responsavelCadastro;
	}
	
	public String getComissionado() {
		if(comissionado == null) {
			comissionado = "";
		}
		return comissionado;
	}
	
	public void setComissionado(String comissionado) {
		this.comissionado = comissionado;
	}
	
	public Integer getUnidadeEnsinoConsulta() {
		if(unidadeEnsinoConsulta == null) {
			unidadeEnsinoConsulta = 0;
}
		return unidadeEnsinoConsulta;
	}

	public void setUnidadeEnsinoConsulta(Integer unidadeEnsinoConsulta) {
		this.unidadeEnsinoConsulta = unidadeEnsinoConsulta;
	}

	public String getObservacaoComplementar() {
		if (observacaoComplementar == null) {
			observacaoComplementar = "";
		}
		return observacaoComplementar;
	}

	public void setObservacaoComplementar(String observacaoComplementar) {
		this.observacaoComplementar = observacaoComplementar;
	}

	public Boolean isPermitirNegociarParcelasNegociacaoNaoCumprida() {
		return permitirNegociarParcelasNegociacaoNaoCumprida;
	}

	public void setPermitirNegociarParcelasNegociacaoNaoCumprida(Boolean permitirNegociarParcelasNegociacaoNaoCumprida) {
		this.permitirNegociarParcelasNegociacaoNaoCumprida = permitirNegociarParcelasNegociacaoNaoCumprida;
	}

	public String getMsgNegociarParcelasNegociacaoNaoCumprida() {
		if(msgNegociarParcelasNegociacaoNaoCumprida == null) {
			msgNegociarParcelasNegociacaoNaoCumprida = "";
		}
		return msgNegociarParcelasNegociacaoNaoCumprida;
	}

	public void setMsgNegociarParcelasNegociacaoNaoCumprida(String msgNegociarParcelasNegociacaoNaoCumprida) {
		this.msgNegociarParcelasNegociacaoNaoCumprida = msgNegociarParcelasNegociacaoNaoCumprida;
	}
	
	public List<SelectItem> getListaSelectItemItemCondicaoRenegociacao() {
		if (listaSelectItemItemCondicaoRenegociacao == null)  {
			listaSelectItemItemCondicaoRenegociacao = new ArrayList<>();
		}
		return listaSelectItemItemCondicaoRenegociacao;
	}

	public void setListaSelectItemItemCondicaoRenegociacao(List<SelectItem> listaSelectItemItemCondicaoRenegociacao) {
		this.listaSelectItemItemCondicaoRenegociacao = listaSelectItemItemCondicaoRenegociacao;
	}
	
	public List<ContaReceberVO> getContaReceberVOs() {
        if (contaReceberVOs == null) {
            contaReceberVOs = new ArrayList<ContaReceberVO>(0);
        }
        return contaReceberVOs;
    }

    public void setContaReceberVOs(List<ContaReceberVO> contaReceberVOs) {
        this.contaReceberVOs = contaReceberVOs;
    }
	
    public void inicializarListaTipoLayoutTermoReconhecimentoDivida() {
    	setListaTipoLayout(null);
    }
    
    public void persistirAgenteNegativacao() {
    	try {
    		getFacadeFactory().getNegociacaoContaReceberFacade().persistirAgenteNegativacao(getNegociacaoContaReceberVO(), getUsuarioLogado());
    		setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
    	}catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
}
    }

	public List<SelectItem> getListaSelectItemAgenteNegativacaoCobranca() {
		if(listaSelectItemAgenteNegativacaoCobranca == null) {			
			try {
				listaSelectItemAgenteNegativacaoCobranca = UtilSelectItem.getListaSelectItem(
						getFacadeFactory().getAgenteNegativacaoCobrancaContaReceberFacade().consultarPorNome("%%", false, getUsuarioLogado()), "codigo", "nome");
			} catch (Exception e) {
				listaSelectItemAgenteNegativacaoCobranca = new ArrayList<SelectItem>(0);
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
		return listaSelectItemAgenteNegativacaoCobranca;
	}

	public void setListaSelectItemAgenteNegativacaoCobranca(List<SelectItem> listaSelectItemAgenteNegativacaoCobranca) {
		this.listaSelectItemAgenteNegativacaoCobranca = listaSelectItemAgenteNegativacaoCobranca;
	}
    
    
    
}
