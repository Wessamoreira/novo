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

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.academico.MatriculaDiretaControle;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.financeiro.ChequeVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ContaPagarVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.DevolucaoChequeVO;
import negocio.comuns.financeiro.EstatisticasLancamentosFuturosVO;
import negocio.comuns.financeiro.FluxoCaixaVO;
import negocio.comuns.financeiro.ItensProvisaoVO;
import negocio.comuns.financeiro.MapaLancamentoFuturoVO;
import negocio.comuns.financeiro.NegociacaoRecebimentoVO;
import negocio.comuns.financeiro.ProvisaoCustoVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.PrioridadeComunicadoInterno;
import negocio.comuns.utilitarias.dominios.SituacaoProvisaoCusto;
import negocio.comuns.utilitarias.dominios.TipoDestinatarioComunicadoInterno;
import negocio.comuns.utilitarias.dominios.TipoMapaLancamentoFuturo;
import negocio.comuns.utilitarias.dominios.TipoOrigemContaReceber;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

@Controller("MapaLancamentoFuturoControle")
@Scope("viewScope")
@Lazy
public class MapaLancamentoFuturoControle extends SuperControle implements Serializable {

    private MapaLancamentoFuturoVO mapaLancamentoFuturoVO;
    private Boolean selecionarTodos;
    private Double taxaTodos;
    private Integer qtdCheque;
    private Integer qtdProvisaoCustoTotal;
    private Double valorProvisaoCustoTotal;
    private Integer qtdChequeAReceberTotal;
    private Double valorAReceberTotal;
    private Integer qtdChequeAPagarTotal;
    private Double valorAPagarTotal;
    private Integer qtdChequeDevolvidos;
    private Double valorChequesDevolvidoTotal;
    private Integer qtdMatricula;
    private Double valorMatriculaTotal;
    protected List<SelectItem> listaSelectItemContaCorrenteOrigem;
    protected List<SelectItem> listaSelectItemContaCorrenteDestino;
    private List<SelectItem> listaSelectItemContaCorrenteTroco;
    private List<MapaLancamentoFuturoVO> listaChequesAPagar;
    private List<MapaLancamentoFuturoVO> listaChequesAReceber;
    private List listaProvisaoCusto;
    private List listaChequesDevolvidos;
    private List<SelectItem> listaSelectItemCentroCusto;
    private List listaMatricula;
    private MatriculaVO matriculaVO;
    private String tabSelecionada;
    private ProvisaoCustoVO provisaoCustoVO;
    private ItensProvisaoVO itensProvisaoVO;
    private List<SelectItem> listaSelectItemFormaPagamento;
    private ComunicacaoInternaVO comunicacaoInternaVO;
    private ChequeVO chequeVO;
    private boolean consultarDetalhada;
    private Date dataReapresentacao;
    private MapaLancamentoFuturoVO MapaLancamentoFuturoVOReapresentacao;
    private List<EstatisticasLancamentosFuturosVO> listaEstatisticas;
    private List<ContaPagarVO> listaContaPagarChequeVOs;

    public MapaLancamentoFuturoControle() throws Exception {
        //obterUsuarioLogado();
        inicializarResumo();
        novo();
        abaChequesAPagar();
        setControleConsulta(new ControleConsulta());
        inicializarListas();
        consultaDetalhada();
        setMensagemID("msg_entre_prmconsulta");
    }

    public void inicializarResumo() throws Exception {
        setListaEstatisticas(new ArrayList<EstatisticasLancamentosFuturosVO>(0));
    }

    public void inicializarListas() {
        if (getListaSelectItemCentroCusto() == null) {
            setListaSelectItemCentroCusto(new ArrayList(0));
        }
        if (getListaSelectItemContaCorrenteTroco() == null) {
            setListaSelectItemContaCorrenteTroco(new ArrayList(0));
        }
        setItensProvisaoVO(new ItensProvisaoVO());

    }

    public void inicializarResponsavel() {
        try {
            getMapaLancamentoFuturoVO().setResponsavel(getUsuarioLogadoClone());
        } catch (Exception e) {
            getMapaLancamentoFuturoVO().setResponsavel(new UsuarioVO());
        }

    }

    /**
     * Rotina responsável por disponibilizar um novo objeto da classe <code>MovimentacaoFinanceira</code> para edição
     * pelo usuário da aplicação.
     */
    public String novo() {
        removerObjetoMemoria(this);
        setQtdCheque(0);
        setMapaLancamentoFuturoVO(new MapaLancamentoFuturoVO());
        inicializarListasSelectItemTodosComboBox();
        setTaxaTodos(0.0);
        setSelecionarTodos(Boolean.FALSE);
        setMensagemID("msg_entre_dados");
        return "editar";
    }

    /**
     * Rotina responsável por disponibilizar os dados de um objeto da classe <code>MovimentacaoFinanceira</code> para
     * alteração. O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente
     * possa disponibilizá-lo para edição.
     */
    public String editar() throws Exception {
        MapaLancamentoFuturoVO obj = (MapaLancamentoFuturoVO) context().getExternalContext().getRequestMap().get("mapaLancamentoFuturoItens");
        obj = getFacadeFactory().getMapaLancamentoFuturoFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
        obj.setNovoObj(Boolean.FALSE);
        setMapaLancamentoFuturoVO(obj);
        inicializarListasSelectItemTodosComboBox();
        setMensagemID("msg_dados_editar");
        return Uteis.getCaminhoRedirecionamentoNavegacao("mapaLancamentoFuturoCons.xhtml");
    }

    /**
     * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe
     * <code>MovimentacaoFinanceira</code>. Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação
     * <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>. Se houver alguma inconsistência o
     * objeto não é gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de erro.
     */
    public String gravar() {
        try {
            if (isTabChequeAPagarSelecionada()) {
                getFacadeFactory().getMapaLancamentoFuturoFacade().baixarCheque(getListaChequesAPagar(), getUsuarioLogado());
            } else if (isTabChequeAReceberSelecionada()) {
                getFacadeFactory().getMapaLancamentoFuturoFacade().baixarCheque(getListaChequesAReceber(), getUsuarioLogado());
            }
            consultar();
            setMensagemID("msg_dados_gravados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("mapaLancamentoFuturoCons.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("mapaLancamentoFuturoCons.xhtml");
        }
    }

    public String gravarPrestacaoConta() throws Exception {
        try {
            getProvisaoCustoVO().setSituacao(SituacaoProvisaoCusto.FINALIZADO.getValor());
            getFacadeFactory().getProvisaoCustoFacade().alterar(getProvisaoCustoVO(), getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema());
            consultar();
            setMensagemID("msg_dados_gravados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
        return Uteis.getCaminhoRedirecionamentoNavegacao("mapaLancamentoFuturoCons");
    }

    public void gerarDestinatariosComunicadoInterno() throws Exception {
        setComunicacaoInternaVO(new ComunicacaoInternaVO());
        getComunicacaoInternaVO().setComunicadoInternoDestinatarioVOs(
                getFacadeFactory().getComunicacaoInternaFacade().consultarListaDeFuncionariosDestinatarios(getListaProvisaoCusto(), getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado()));
    }

    public void preencherDadosComunicadoInterno() throws Exception {
        getComunicacaoInternaVO().setAssunto(getAssuntoPadraoParaNotificacao());
        getComunicacaoInternaVO().setData(new Date());
        getComunicacaoInternaVO().setMensagem(getMensagemPadraoParaNotificacao());
        getComunicacaoInternaVO().setPrioridade(PrioridadeComunicadoInterno.NORMAL.getValor());
        getComunicacaoInternaVO().setResponsavel(getUsuarioLogado().getPessoa());
        getComunicacaoInternaVO().setTipoDestinatario(TipoDestinatarioComunicadoInterno.FUNCIONARIO.getValor());
    }

    public String getMensagemPadraoParaNotificacao() throws Exception {
//        return getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado(), null).getMensagemPadraoNotificacao();
        return getConfiguracaoFinanceiroPadraoSistema().getMensagemPadraoNotificacao();
    }

    public String getAssuntoPadraoParaNotificacao() throws Exception {
//        return getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado(), null).getAssuntoPadraoNotificacao();
        return getConfiguracaoFinanceiroPadraoSistema().getAssuntoPadraoNotificacao();
    }

    public String notificarProvisoesPendentesEAtrasadas() {
        try {
            gerarDestinatariosComunicadoInterno();
            preencherDadosComunicadoInterno();
            getFacadeFactory().getComunicacaoInternaFacade().incluir(getComunicacaoInternaVO(), false, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema(),null);
            setMensagemDetalhada("Todas as provisões pendentes foram notificadas!");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
        return Uteis.getCaminhoRedirecionamentoNavegacao("mapaLancamentoFuturoCons");
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP MovimentacaoFinanceiraCons.jsp. Define o tipo de
     * consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
     * resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    public void consultaDetalhada() {
        getMapaLancamentoFuturoVO().setDataPrevisao(new Date());
        getMapaLancamentoFuturoVO().setDataEmissao(new Date());
        getMapaLancamentoFuturoVO().setDataEmissaoFinal(Uteis.obterDataFutura(new Date(), 31));
        getMapaLancamentoFuturoVO().setDataPrevisaoFinal(Uteis.obterDataFutura(new Date(), 31));
        this.setConsultarDetalhada(Boolean.TRUE);
    }

    public void fecharConsultaDetalhada() {
        getMapaLancamentoFuturoVO().setDataPrevisao(null);
        getMapaLancamentoFuturoVO().setDataEmissao(null);
        getMapaLancamentoFuturoVO().setDataEmissaoFinal(null);
        getMapaLancamentoFuturoVO().setDataPrevisaoFinal(null);
        this.setConsultarDetalhada(Boolean.FALSE);
    }

    @Override
    public String consultar() {
        try {
            setQtdCheque(0);
            setTaxaTodos(0.0);
            setSelecionarTodos(Boolean.FALSE);
            super.consultar();

            TipoMapaLancamentoFuturo tipoMapa = TipoMapaLancamentoFuturo.getEnum(getMapaLancamentoFuturoVO().getTipoMapaLancamentoFuturo());

            List listaMapaLancamentoFuturo = getFacadeFactory().getMapaLancamentoFuturoFacade().consultarPorTodosParametros(getMapaLancamentoFuturoVO(), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            double valorTotal = getFacadeFactory().getMapaLancamentoFuturoFacade().consultarPorTodosParametrosValorTotal(getMapaLancamentoFuturoVO(), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());

            switch (tipoMapa) {
                case CHEQUE_A_PAGAR:
                    setListaChequesAPagar(listaMapaLancamentoFuturo);
                    setQtdChequeAPagarTotal(getListaChequesAPagar().size());
                    setValorAPagarTotal(valorTotal);
                    break;
                case CHEQUE_A_RECEBER:
                    setListaChequesAReceber(listaMapaLancamentoFuturo);
                    setQtdChequeAReceberTotal(getListaChequesAReceber().size());
                    setValorAReceberTotal(valorTotal);
                    break;
                case CHEQUE_DEVOLVIDO:
                    setListaChequesDevolvidos(listaMapaLancamentoFuturo);
                    setQtdChequeDevolvidos(getListaChequesDevolvidos().size());
                    setValorChequesDevolvidoTotal(valorTotal);
                    break;
                case PROVISAO_CUSTO:
                    setListaProvisaoCusto(listaMapaLancamentoFuturo);
                    setQtdProvisaoTotal(getListaProvisaoCusto().size());
                    setValorProvisaoCustoTotal(valorTotal);
                    break;
                case MATRICULA:
                    setListaMatricula(listaMapaLancamentoFuturo);
                    setQtdMatricula(getListaMatricula().size());
                    setValorMatriculaTotal(valorTotal);
                    break;
            }

            setMensagemID("msg_dados_consultados");
            return "";
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "";
        }
    }
    

    public Boolean getApresentarEstatisticas() throws Exception {
        if (this.getUsuarioLogado().getPerfilAdministrador()) {
            return true;
        } else {
            return false;
        }
    }

    public void selecionarCheque() {
        try {
            MapaLancamentoFuturoVO obj;
            Iterator<String> i = context().getExternalContext().getRequestMap().keySet().iterator();
            while (i.hasNext()) {
                Object requestMap = context().getExternalContext().getRequestMap().get(i.next());
                if (requestMap instanceof MapaLancamentoFuturoVO) {
                    obj = (MapaLancamentoFuturoVO) requestMap;

                    if (obj.getBaixarCheque()) {
                        setQtdCheque(new Integer(getQtdCheque().intValue() + 1));
                    } else {
                        obj.setTaxaDescontoCheque(0.0);
                        obj.setValorTaxaDescontoCheque(0.0);
                        if (getQtdCheque().intValue() > 0) {
                            setQtdCheque(new Integer(getQtdCheque().intValue() - 1));
                        } else {
                            setQtdCheque(0);
                        }
                    }
                }

            }

        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
    
    public void inicializarMapaLancamentoFuturoSelecionado() {
    	MapaLancamentoFuturoVO obj = (MapaLancamentoFuturoVO) context().getExternalContext().getRequestMap().get("mapaLancamentoFuturoChequesDevolvidos");
    	setMapaLancamentoFuturoVOReapresentacao(obj);
    }
    
    public void reapresentarCheque() {
    	try {
    		getFacadeFactory().getMapaLancamentoFuturoFacade().reapresentarCheque(getMapaLancamentoFuturoVOReapresentacao(), getDataReapresentacao(), getUsuarioLogado());
    		consultar();
    	} catch (Exception e) {
    		setMensagemDetalhada("msg_erro", e.getMessage());
    	}
    }

    public void montarListaSelectItemCentroCusto(FuncionarioVO requisitante) {
        try {
            FuncionarioVO funcionario = getFacadeFactory().getFuncionarioFacade().consultarPorCodigoPessoa(requisitante.getPessoa().getCodigo(), 0, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            if (funcionario != null) {
                List<SelectItem> objs = new ArrayList<SelectItem>(0);
                objs.add(new SelectItem(0, ""));
                for (FuncionarioCargoVO funcionarioCargo : (List<FuncionarioCargoVO>) funcionario.getFuncionarioCargoVOs()) {
                    objs.add(new SelectItem(funcionarioCargo.getCodigo(), funcionarioCargo.getCentroCusto().getIdentificadorCentroCusto() + " - "
                            + funcionarioCargo.getCentroCusto().getDescricaoCentroCusto2()));
                }
                setListaSelectItemCentroCusto(objs);
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            setListaSelectItemCentroCusto(new ArrayList(0));
        }
    }

    public void selecionarTodosCheque() {
        try {
            if (isTabChequeAPagarSelecionada()) {
                selecionarTodosCheque(getListaChequesAPagar(), getSelecionarTodos());
            } else if (isTabChequeAReceberSelecionada()) {
                selecionarTodosCheque(getListaChequesAReceber(), getSelecionarTodos());
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    private void selecionarTodosCheque(List<MapaLancamentoFuturoVO> listaDeCheques, boolean selecionarTodos) throws Exception {
        MapaLancamentoFuturoVO mapaLancamentoFuturo;
        for (Iterator<MapaLancamentoFuturoVO> iterator = listaDeCheques.iterator(); iterator.hasNext();) {
        if (selecionarTodos) {
                mapaLancamentoFuturo = iterator.next();
                mapaLancamentoFuturo.setBaixarCheque(true);
                mapaLancamentoFuturo.setTaxaDescontoCheque(this.getTaxaTodos());
                mapaLancamentoFuturo.atualizarValorTaxa();
            this.setQtdCheque(listaDeCheques.size());
        } else {
                mapaLancamentoFuturo = iterator.next();
                mapaLancamentoFuturo.setBaixarCheque(false);
                mapaLancamentoFuturo.setTaxaDescontoCheque(0.0);
                mapaLancamentoFuturo.setValorTaxaDescontoCheque(0.0);
            }
            this.setQtdCheque(0);
        }
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
     * relativo ao atributo <code>FluxoCaixaTroco</code>.
     */
    public void montarListaSelectItemContaCorrenteTroco(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarFluxoCaixaPorNumero(prm);
            i = resultadoConsulta.iterator();
            List<SelectItem> objs = new ArrayList<SelectItem>(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                FluxoCaixaVO obj = (FluxoCaixaVO) i.next();
                if ("A".equals(obj.getSituacao())) {
                  	if(Uteis.isAtributoPreenchido(obj.getContaCaixa().getNomeApresentacaoSistema())){
                		objs.add(new SelectItem(obj.getContaCaixa().getCodigo(), getUnidadeEnsino(obj.getUnidadeEnsino()).getNome() + " - " + obj.getContaCaixa().getNomeApresentacaoSistema()));	
                	}else{
                		objs.add(new SelectItem(obj.getContaCaixa().getCodigo(), getUnidadeEnsino(obj.getUnidadeEnsino()).getNome() + " - " + obj.getContaCaixa().getNumero().toString()));
                	}
                }
            }
            setListaSelectItemContaCorrenteTroco(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    public UnidadeEnsinoVO getUnidadeEnsino(Integer codigo) throws Exception {
        UnidadeEnsinoVO obj = (UnidadeEnsinoVO) getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(codigo, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
        return (obj != null) ? obj : new UnidadeEnsinoVO();
    }

    /*
     * Método responsável por adicionar um novo objeto da classe <code>ItensProvisao</code> para o objeto
     * <code>provisaoCustoVO</code> da classe <code>ProvisaoCusto</code>
     */
    public void adicionarItensProvisao() throws Exception {
        try {
            if (!getProvisaoCustoVO().getCodigo().equals(0)) {
                getItensProvisaoVO().setProvisaoCusto(getProvisaoCustoVO().getCodigo());
            }
            getProvisaoCustoVO().adicionarObjItensProvisaoVOs(getItensProvisaoVO());
            getProvisaoCustoVO().atualizarValorFinalETroco();
            this.setItensProvisaoVO(new ItensProvisaoVO());
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    /*
     * Método responsável por disponibilizar dados de um objeto da classe <code>ItensProvisao</code> para edição pelo
     * usuário.
     */
    public void editarItensProvisao() throws Exception {
        ItensProvisaoVO obj = (ItensProvisaoVO) context().getExternalContext().getRequestMap().get("itensProvisao");
        setItensProvisaoVO(obj);
    }

    /*
     * Método responsável por remover um novo objeto da classe <code>ItensProvisao</code> do objeto
     * <code>provisaoCustoVO</code> da classe <code>ProvisaoCusto</code>
     */
    public void removerItensProvisao() throws Exception {
        ItensProvisaoVO obj = (ItensProvisaoVO) context().getExternalContext().getRequestMap().get("itensProvisao");
        getProvisaoCustoVO().excluirObjItensProvisaoVOs(obj.getDescricao());
        setMensagemID("msg_dados_excluidos");
    }

    public String prepararPrestacaoDeContaProvisaoCusto() throws Exception {
        try {
            setMensagem("");
            MapaLancamentoFuturoVO mapaLancamentoFuturo = (MapaLancamentoFuturoVO) context().getExternalContext().getRequestMap().get("mapaLancamentoFuturoItens");
            setProvisaoCustoVO(getFacadeFactory().getProvisaoCustoFacade().consultarPorMapaLancamentoFuturo(mapaLancamentoFuturo.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
            getProvisaoCustoVO().atualizarValorFinalETroco();
            montarListaSelectItemCentroCusto(getProvisaoCustoVO().getRequisitante());
            montarListaSelectItemContaCorrenteTroco("");
            return "";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "";
        }
    }

    public String liberarDescontoMatricula() throws Exception {
        try {
            MapaLancamentoFuturoVO mapaLancamentoFuturo = (MapaLancamentoFuturoVO) context().getExternalContext().getRequestMap().get("mapaLancamentoFuturoMatricula");
            setMatriculaVO(getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(mapaLancamentoFuturo.getMatriculaOrigem(), getUnidadeEnsinoLogado().getCodigo(),
                    NivelMontarDados.TODOS, getUsuarioLogado()));
            return navegarPara(MatriculaDiretaControle.class.getSimpleName(), "getPreencherDadosMatricula", "matricula", getMatriculaVO());

        } catch (Exception e) {
            return "";
        }
    }

    public String negociarRecebimentoDeChequesDevolvidos() throws Exception {
        try {
            return navegarPara(NegociacaoRecebimentoControle.class.getSimpleName(), "setNegociacaoRecebimentoVOPreenchido", Uteis.getCaminhoRedirecionamentoNavegacao("negociacaoRecebimentoForm"),
                    gerarNegociacaoRecebimentoVOPreenchido(getMapaLancamentoFuturoVOSelecionado()));
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
        return "";
    }

    public String devolverChequePendenteDePagamento() throws Exception {
        try {
            return navegarPara(DevolucaoChequeControle.class.getSimpleName(), "setDevolucaoChequeVOPreenchido",  Uteis.getCaminhoRedirecionamentoNavegacao("devolucaoChequeForm"), gerarDevolucaoChequeVOPreenchido(getMapaLancamentoFuturoVOSelecionado()));
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
        return "";
    }

    public DevolucaoChequeVO gerarDevolucaoChequeVOPreenchido(MapaLancamentoFuturoVO mapaLancamentoFuturoVO) throws Exception {
        DevolucaoChequeVO devolucaoChequeVO = new DevolucaoChequeVO();
        ChequeVO cheque = new ChequeVO();
        cheque = getFacadeFactory().getChequeFacade().consultarPorChavePrimaria(mapaLancamentoFuturoVO.getCodigoCheque(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
        devolucaoChequeVO.setUnidadeEnsino(cheque.getUnidadeEnsino());
        devolucaoChequeVO.setResponsavel(getUsuarioLogadoClone());
        devolucaoChequeVO.setCheque(cheque);
        devolucaoChequeVO.setPessoa(cheque.getPessoa());
        devolucaoChequeVO.setContaCorrente(cheque.getLocalizacaoCheque());
        devolucaoChequeVO.setDataReapresentacaoCheque1(mapaLancamentoFuturoVO.getDataReapresentacaoCheque1());
        devolucaoChequeVO.setFornecedor(cheque.getFornecedor());
        devolucaoChequeVO.setParceiro(cheque.getParceiro());
        devolucaoChequeVO.setDataReapresentacaoCheque2(mapaLancamentoFuturoVO.getDataReapresentacaoCheque2());

        return devolucaoChequeVO;
    }

    public NegociacaoRecebimentoVO gerarNegociacaoRecebimentoVOPreenchido(MapaLancamentoFuturoVO mapaLancamentoFuturoVO) throws Exception {
        DevolucaoChequeVO devolucaoChequeVO = getFacadeFactory().getDevolucaoChequeFacade().consultarPorChavePrimaria(mapaLancamentoFuturoVO.getCodigoOrigem(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
        List<ContaReceberVO> contaReceberVOs = getFacadeFactory().getContaReceberFacade().consultarPorCodOrigemTipoOrigem(TipoOrigemContaReceber.DEVOLUCAO_CHEQUE.getValor(),
                mapaLancamentoFuturoVO.getCodigoOrigem(), 0, false, Uteis.NIVELMONTARDADOS_TODOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
//        ConfiguracaoFinanceiroVO conf = getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), null);
        ConfiguracaoFinanceiroVO conf = getConfiguracaoFinanceiroPadraoSistema();
        for (ContaReceberVO conta : contaReceberVOs) {
            // retorna primeira conta encontrada

            return conta.gerarNegociacaoRecebimentoVOPreenchido(getUsuarioLogado(), devolucaoChequeVO.getPessoa(), conta.getResponsavelFinanceiro(), conta.getFuncionario(), conta.getParceiroVO(), conf);
        }
        return new NegociacaoRecebimentoVO();
    }

    public MapaLancamentoFuturoVO getMapaLancamentoFuturoVOSelecionado() throws Exception {
        Iterator<String> i = context().getExternalContext().getRequestMap().keySet().iterator();
        while (i.hasNext()) {
            Object requestMap = context().getExternalContext().getRequestMap().get(i.next());
            if (requestMap instanceof MapaLancamentoFuturoVO) {
                return (MapaLancamentoFuturoVO) requestMap;
            }
        }
        return new MapaLancamentoFuturoVO();
    }

    public void atualizarTaxaTodos() {
        try {
            Iterator i = getListaChequesAPagar().iterator();
            while (i.hasNext()) {
                MapaLancamentoFuturoVO m = (MapaLancamentoFuturoVO) i.next();
                m.setTaxaDescontoCheque(this.getTaxaTodos());
                if (m.getApresentarCamposAntecipacao()) {
                    m.atualizarValorTaxa();
                }
            }
        } catch (Exception e) {
        }
    }

    public List<SelectItem> getTipoConsultaComboCheque() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("nomeCliente", "Cliente"));
        itens.add(new SelectItem("sacado", "Sacado"));
        itens.add(new SelectItem("nomeBanco", "Banco"));
        itens.add(new SelectItem("numeroAgenciaAgencia", "Agência"));
        itens.add(new SelectItem("numero", "Número"));
        itens.add(new SelectItem("dataEmissao", "Data Emissão"));
        itens.add(new SelectItem("dataPrevisao", "Data Previsão"));
        return itens;
    }

    public void abaChequesAPagar() {
        getMapaLancamentoFuturoVO().setTipoMapaLancamentoFuturo(TipoMapaLancamentoFuturo.CHEQUE_A_PAGAR.getValor());
        setTabSelecionada("form:tabChequesAPagar");
        consultar();
    }

    public void abaChequesDevolvidos() {
        getMapaLancamentoFuturoVO().setTipoMapaLancamentoFuturo(TipoMapaLancamentoFuturo.CHEQUE_DEVOLVIDO.getValor());
        setTabSelecionada("form:tabChequesDevolvidos");
        consultar();
    }

    public void abaChequesAReceber() {
        getMapaLancamentoFuturoVO().setTipoMapaLancamentoFuturo(TipoMapaLancamentoFuturo.CHEQUE_A_RECEBER.getValor());
        setTabSelecionada("form:tabChequesAReceber");
        consultar();
    }

    public void abaProvisaoCusto() {
        getMapaLancamentoFuturoVO().setTipoMapaLancamentoFuturo(TipoMapaLancamentoFuturo.PROVISAO_CUSTO.getValor());
        setTabSelecionada("form:tabProvisao");
        consultar();
    }

    public void abaMatricula() {
        getMapaLancamentoFuturoVO().setTipoMapaLancamentoFuturo(TipoMapaLancamentoFuturo.MATRICULA.getValor());
        setTabSelecionada("form:tabMatricula");
        consultar();
    }

    public boolean isTabChequeAPagarSelecionada() {
        return "tabChequesAPagar".equals(getTabSelecionada()) || "form:tabChequesAPagar".equals(getTabSelecionada());
    }

    public boolean isTabChequeAReceberSelecionada() {
        return "tabChequesAReceber".equals(getTabSelecionada()) || "form:tabChequesAReceber".equals(getTabSelecionada());
    }

    public boolean isTabChequeDevolvido() {
        return "tabChequesDevolvidos".equals(getTabSelecionada()) || "form:tabChequesDevolvidos".equals(getTabSelecionada());
    }

    public boolean isTabProvisaoCustoSelecionada() {
        return "tabProvisao".equals(getTabSelecionada()) || "form:tabProvisao".equals(getTabSelecionada());
    }

    public boolean isApresentarGridProvisaoCusto() {
        if (getListaProvisaoCusto() != null) {
            return getListaProvisaoCusto().size() != 0;
        }
        return false;
    }

    public boolean isApresentarGridMatricula() {
        if (getListaMatricula() != null) {
            return getListaMatricula().size() != 0;
        }
        return false;
    }

    public boolean isApresentarGridChequesAReceber() {
        if (getListaChequesAReceber() != null) {
            return getListaChequesAReceber().size() != 0;
        }
        return false;
    }

    public boolean isApresentarGridChequesDevolvidos() {
        if (getListaChequesDevolvidos() != null) {
            return getListaChequesDevolvidos().size() != 0;
        }
        return false;
    }

    public boolean isApresentarGridChequesAPagar() {
        if (getListaChequesAPagar() != null) {
            return getListaChequesAPagar().size() != 0;
        }
        return false;
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
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
     * relativo ao atributo <code>ContaCorrenteDestino</code>.
     */
    public void montarListaSelectItemContaCorrenteDestino(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarContaCorrentePorNumero(prm);
            i = resultadoConsulta.iterator();
            List<SelectItem> objs = new ArrayList<SelectItem>(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                ContaCorrenteVO obj = (ContaCorrenteVO) i.next();
                if(Uteis.isAtributoPreenchido(obj.getNomeApresentacaoSistema())){
                	objs.add(new SelectItem(obj.getCodigo(), obj.getNomeApresentacaoSistema()));
                }else{
                	objs.add(new SelectItem(obj.getCodigo(), obj.getNumero().toString() + "-" + obj.getDigito().toString()));
                }
            }
            setListaSelectItemContaCorrenteDestino(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>ContaCorrenteDestino</code>. Buscando
     * todos os objetos correspondentes a entidade <code>ContaCorrente</code>. Esta rotina não recebe parâmetros para
     * filtragem de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio
     * requisições Ajax.
     */
    public void montarListaSelectItemContaCorrenteDestino() {
        try {
            montarListaSelectItemContaCorrenteDestino("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
     * relativo ao atributo <code>ContaCorrenteOrigem</code>.
     */
    public void montarListaSelectItemContaCorrenteOrigem(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarContaCorrentePorNumero(prm);
            i = resultadoConsulta.iterator();
            List<SelectItem> objs = new ArrayList<SelectItem>(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                ContaCorrenteVO obj = (ContaCorrenteVO) i.next();
                if(Uteis.isAtributoPreenchido(obj.getNomeApresentacaoSistema())){
                	objs .add(new SelectItem(obj.getCodigo(), obj.getNomeApresentacaoSistema()));
                }else{
                	objs .add(new SelectItem(obj.getCodigo(), obj.getNumero().toString() + "-" + obj.getDigito().toString()));
                }
            }
            setListaSelectItemContaCorrenteOrigem(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>ContaCorrenteOrigem</code>. Buscando todos
     * os objetos correspondentes a entidade <code>ContaCorrente</code>. Esta rotina não recebe parâmetros para
     * filtragem de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio
     * requisições Ajax.
     */
    public void montarListaSelectItemContaCorrenteOrigem() {
        try {
            montarListaSelectItemContaCorrenteOrigem("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>numero</code> Este
     * atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox
     * correspondente
     */
    public List consultarFluxoCaixaPorNumero(String numeroPrm) throws Exception {
        List lista = getFacadeFactory().getFluxoCaixaFacade().consultarPorNumeroContaCorrente(numeroPrm, "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
        return lista;
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>numero</code> Este
     * atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox
     * correspondente
     */
    public List consultarContaCorrentePorNumero(String numeroPrm) throws Exception {
        List lista = getFacadeFactory().getContaCorrenteFacade().consultarPorNumero(numeroPrm, 0, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
        return lista;
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
     * relativo ao atributo <code>FormaPagamento</code>.
     */
    public void montarListaSelectItemFormaPagamento(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarFormaPagamentoPorNome(prm);
            i = resultadoConsulta.iterator();
            List<SelectItem> objs = new ArrayList<SelectItem>(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                FormaPagamentoVO obj = (FormaPagamentoVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
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
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>FormaPagamento</code>. Buscando todos os
     * objetos correspondentes a entidade <code>FormaPagamento</code>. Esta rotina não recebe parâmetros para filtragem
     * de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemFormaPagamento() {
        try {
            montarListaSelectItemFormaPagamento("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nome</code> Este
     * atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox
     * correspondente
     */
    public List consultarFormaPagamentoPorNome(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getFormaPagamentoFacade().consultarPorNome(nomePrm, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
        return lista;
    }

    /**
     * Método responsável por inicializar a lista de valores (<code>SelectItem</code>) para todos os ComboBox's.
     */
    public void inicializarListasSelectItemTodosComboBox() {
        montarListaSelectItemContaCorrenteOrigem();
        montarListaSelectItemContaCorrenteDestino();
        montarListaSelectItemFormaPagamento();
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
        itens.add(new SelectItem("data", "Data"));
        itens.add(new SelectItem("nomeUsuario", "Responsável"));
        itens.add(new SelectItem("numeroContaCorrente", "Conta Corrente Origem"));
        itens.add(new SelectItem("numeroContaCorrente", "Conta Corrente Destino"));
        return itens;
    }

    /**
     * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
     */
    public String inicializarConsultar() {
        removerObjetoMemoria(this);
        setListaConsulta(new ArrayList(0));
        setListaChequesAPagar(new ArrayList(0));
        setListaChequesAReceber(new ArrayList(0));
        setMensagemID("msg_entre_prmconsulta");
        return "consultar";
    }

    public ChequeVO getChequeVO() {
        return chequeVO;
    }

    public void setChequeVO(ChequeVO chequeVO) {
        this.chequeVO = chequeVO;
    }

    public List getListaSelectItemContaCorrenteDestino() {
        return (listaSelectItemContaCorrenteDestino);
    }

    public void setListaSelectItemContaCorrenteDestino(List listaSelectItemContaCorrenteDestino) {
        this.listaSelectItemContaCorrenteDestino = listaSelectItemContaCorrenteDestino;
    }

    public List getListaSelectItemContaCorrenteOrigem() {
        return (listaSelectItemContaCorrenteOrigem);
    }

    public void setListaSelectItemContaCorrenteOrigem(List listaSelectItemContaCorrenteOrigem) {
        this.listaSelectItemContaCorrenteOrigem = listaSelectItemContaCorrenteOrigem;
    }

    @Override
    protected void limparRecursosMemoria() {
        super.limparRecursosMemoria();
        setMapaLancamentoFuturoVO(null);
        Uteis.liberarListaMemoria(listaSelectItemContaCorrenteOrigem);
        Uteis.liberarListaMemoria(listaSelectItemContaCorrenteDestino);
        chequeVO = null;
    }

    /**
     * @return the mapaLancamentoFuturoVO
     */
    public MapaLancamentoFuturoVO getMapaLancamentoFuturoVO() {
        return mapaLancamentoFuturoVO;
    }

    /**
     * @param mapaLancamentoFuturoVO
     *            the mapaLancamentoFuturoVO to set
     */
    public void setMapaLancamentoFuturoVO(MapaLancamentoFuturoVO mapaLancamentoFuturoVO) {
        this.mapaLancamentoFuturoVO = mapaLancamentoFuturoVO;
    }

    /**
     * @return the qtdCheque
     */
    public Integer getQtdCheque() {
        return qtdCheque;
    }

    /**
     * @param qtdCheque
     *            the qtdCheque to set
     */
    public void setQtdCheque(Integer qtdCheque) {
        this.qtdCheque = qtdCheque;
    }

    /**
     * @return the selecionarTodos
     */
    public Boolean getSelecionarTodos() {
        return selecionarTodos;
    }

    /**
     * @param selecionarTodos
     *            the selecionarTodos to set
     */
    public void setSelecionarTodos(Boolean selecionarTodos) {
        this.selecionarTodos = selecionarTodos;
    }

    /**
     * @return the taxaTodos
     */
    public Double getTaxaTodos() {
        return taxaTodos;
    }

    /**
     * @param taxaTodos
     *            the taxaTodos to set
     */
    public void setTaxaTodos(Double taxaTodos) {
        this.taxaTodos = taxaTodos;
    }

    /**
     * @return the consultarDetalhada
     */
    public boolean getConsultarDetalhada() {
        return consultarDetalhada;
    }

    /**
     * @param consultarDetalhada
     *            the consultarDetalhada to set
     */
    public void setConsultarDetalhada(boolean consultarDetalhada) {
        this.consultarDetalhada = consultarDetalhada;
    }

    /**
     * @return the qtdProvisaoCustoTotal
     */
    public Integer getQtdProvisaoTotal() {
        return getQtdProvisaoCustoTotal();
    }

    /**
     * @param qtdProvisaoCustoTotal
     *            the qtdChequeTotal to set
     */
    public void setQtdProvisaoTotal(Integer qtdChequeTotal) {
        this.setQtdProvisaoCustoTotal(qtdChequeTotal);
    }

    /**
     * @return the valorProvisaoCustoTotal
     */
    public Double getValorProvisaoCustoTotal() {
        return valorProvisaoCustoTotal;
    }

    /**
     * @param valorProvisaoCustoTotal
     *            the valorProvisaoCustoTotal to set
     */
    public void setValorProvisaoCustoTotal(Double valorTotal) {
        this.valorProvisaoCustoTotal = valorTotal;
    }

    /**
     * @return the listaCheques
     */
    public List<MapaLancamentoFuturoVO> getListaChequesAPagar() {
        return listaChequesAPagar;
    }

    /**
     * @param listaChequesAPagar
     *            the listaCheques to set
     */
    public void setListaChequesAPagar(List<MapaLancamentoFuturoVO> listaChequesAPagar) {
        this.listaChequesAPagar = listaChequesAPagar;
    }

    /**
     * @return the listaProvisaoCusto
     */
    public List getListaProvisaoCusto() {
        return listaProvisaoCusto;
    }

    /**
     * @param listaProvisaoCusto
     *            the listaProvisaoCusto to set
     */
    public void setListaProvisaoCusto(List listaProvisaoCusto) {
        this.listaProvisaoCusto = listaProvisaoCusto;
    }

    /**
     * @return the tabSelecionada
     */
    public String getTabSelecionada() {
        return tabSelecionada;
    }

    /**
     * @param tabSelecionada
     *            the tabSelecionada to set
     */
    public void setTabSelecionada(String tabSelecionada) {
        this.tabSelecionada = tabSelecionada;
    }

    /**
     * @return the listaSelectItemCentroCusto
     */
    public List getListaSelectItemCentroCusto() {
        return listaSelectItemCentroCusto;
    }

    /**
     * @param listaSelectItemCentroCusto
     *            the listaSelectItemCentroCusto to set
     */
    public void setListaSelectItemCentroCusto(List listaSelectItemCentroCusto) {
        this.listaSelectItemCentroCusto = listaSelectItemCentroCusto;
    }

    /**
     * @return the provisaoCustoVO
     */
    public ProvisaoCustoVO getProvisaoCustoVO() {
        return provisaoCustoVO;
    }

    /**
     * @param provisaoCustoVO
     *            the provisaoCustoVO to set
     */
    public void setProvisaoCustoVO(ProvisaoCustoVO provisaoCustoVO) {
        this.provisaoCustoVO = provisaoCustoVO;
    }

    /**
     * @return the listaSelectItemContaCorrenteTroco
     */
    public List getListaSelectItemContaCorrenteTroco() {
        return listaSelectItemContaCorrenteTroco;
    }

    /**
     * @param listaSelectItemContaCorrenteTroco
     *            the listaSelectItemContaCorrenteTroco to set
     */
    public void setListaSelectItemContaCorrenteTroco(List listaSelectItemContaCorrenteTroco) {
        this.listaSelectItemContaCorrenteTroco = listaSelectItemContaCorrenteTroco;
    }

    /**
     * @return the itensProvisaoVO
     */
    public ItensProvisaoVO getItensProvisaoVO() {
        return itensProvisaoVO;
    }

    /**
     * @param itensProvisaoVO
     *            the itensProvisaoVO to set
     */
    public void setItensProvisaoVO(ItensProvisaoVO itensProvisaoVO) {
        this.itensProvisaoVO = itensProvisaoVO;
    }

    /**
     * @return the listaSelectItemFormaPagamento
     */
    public List getListaSelectItemFormaPagamento() {
        return listaSelectItemFormaPagamento;
    }

    /**
     * @param listaSelectItemFormaPagamento
     *            the listaSelectItemFormaPagamento to set
     */
    public void setListaSelectItemFormaPagamento(List listaSelectItemFormaPagamento) {
        this.listaSelectItemFormaPagamento = listaSelectItemFormaPagamento;
    }

    /**
     * @return the listaEstatisticas
     */
    public List<EstatisticasLancamentosFuturosVO> getListaEstatisticas() {
        return listaEstatisticas;
    }

    /**
     * @param listaEstatisticas
     *            the listaEstatisticas to set
     */
    public void setListaEstatisticas(List<EstatisticasLancamentosFuturosVO> listaEstatisticas) {
        this.listaEstatisticas = listaEstatisticas;
    }

    /**
     * @return the comunicacaoInternaVO
     */
    public ComunicacaoInternaVO getComunicacaoInternaVO() {
        return comunicacaoInternaVO;
    }

    /**
     * @param comunicacaoInternaVO
     *            the comunicacaoInternaVO to set
     */
    public void setComunicacaoInternaVO(ComunicacaoInternaVO comunicacaoInternaVO) {
        this.comunicacaoInternaVO = comunicacaoInternaVO;
    }

    /**
     * @return the listaChequesAReceber
     */
    public List<MapaLancamentoFuturoVO> getListaChequesAReceber() {
        return listaChequesAReceber;
    }

    /**
     * @param listaChequesAReceber
     *            the listaChequesAReceber to set
     */
    public void setListaChequesAReceber(List<MapaLancamentoFuturoVO> listaChequesAReceber) {
        this.listaChequesAReceber = listaChequesAReceber;
    }

    /**
     * @return the qtdChequeAReceberTotal
     */
    public Integer getQtdChequeAReceberTotal() {
        return qtdChequeAReceberTotal;
    }

    /**
     * @param qtdChequeAReceberTotal
     *            the qtdChequeAReceberTotal to set
     */
    public void setQtdChequeAReceberTotal(Integer qtdChequeAReceberTotal) {
        this.qtdChequeAReceberTotal = qtdChequeAReceberTotal;
    }

    /**
     * @return the valorAReceberTotal
     */
    public Double getValorAReceberTotal() {
        return valorAReceberTotal;
    }

    /**
     * @param valorAReceberTotal
     *            the valorAReceberTotal to set
     */
    public void setValorAReceberTotal(Double valorAReceberTotal) {
        this.valorAReceberTotal = valorAReceberTotal;
    }

    /**
     * @return the qtdChequeAPagarTotal
     */
    public Integer getQtdChequeAPagarTotal() {
        return qtdChequeAPagarTotal;
    }

    /**
     * @param qtdChequeAPagarTotal
     *            the qtdChequeAPagarTotal to set
     */
    public void setQtdChequeAPagarTotal(Integer qtdChequeAPagarTotal) {
        this.qtdChequeAPagarTotal = qtdChequeAPagarTotal;
    }

    /**
     * @return the valorAPagarTotal
     */
    public Double getValorAPagarTotal() {
        return valorAPagarTotal;
    }

    /**
     * @param valorAPagarTotal
     *            the valorAPagarTotal to set
     */
    public void setValorAPagarTotal(Double valorAPagarTotal) {
        this.valorAPagarTotal = valorAPagarTotal;
    }

    /**
     * @return the listaChequesDevolvidos
     */
    public List getListaChequesDevolvidos() {
        return listaChequesDevolvidos;
    }

    /**
     * @param listaChequesDevolvidos
     *            the listaChequesDevolvidos to set
     */
    public void setListaChequesDevolvidos(List listaChequesDevolvidos) {
        this.listaChequesDevolvidos = listaChequesDevolvidos;
    }

    /**
     * @return the qtdProvisaoCustoTotal
     */
    public Integer getQtdProvisaoCustoTotal() {
        return qtdProvisaoCustoTotal;
    }

    /**
     * @param qtdProvisaoCustoTotal
     *            the qtdProvisaoCustoTotal to set
     */
    public void setQtdProvisaoCustoTotal(Integer qtdProvisaoCustoTotal) {
        this.qtdProvisaoCustoTotal = qtdProvisaoCustoTotal;
    }

    /**
     * @return the valorChequesDevolvidoTotal
     */
    public Double getValorChequesDevolvidoTotal() {
        return valorChequesDevolvidoTotal;
    }

    /**
     * @param valorChequesDevolvidoTotal
     *            the valorChequesDevolvidoTotal to set
     */
    public void setValorChequesDevolvidoTotal(Double valorChequesDevolvidoTotal) {
        this.valorChequesDevolvidoTotal = valorChequesDevolvidoTotal;
    }

    /**
     * @return the qtdChequeDevolvidos
     */
    public Integer getQtdChequeDevolvidos() {
        return qtdChequeDevolvidos;
    }

    /**
     * @param qtdChequeDevolvidos
     *            the qtdChequeDevolvidos to set
     */
    public void setQtdChequeDevolvidos(Integer qtdChequeDevolvidos) {
        this.qtdChequeDevolvidos = qtdChequeDevolvidos;
    }

    /**
     * @return the listaMatricula
     */
    public List getListaMatricula() {
        return listaMatricula;
    }

    /**
     * @param listaMatricula
     *            the listaMatricula to set
     */
    public void setListaMatricula(List listaMatricula) {
        this.listaMatricula = listaMatricula;
    }

    /**
     * @return the matriculaVO
     */
    public MatriculaVO getMatriculaVO() {
        return matriculaVO;
    }

    /**
     * @param matriculaVO
     *            the matriculaVO to set
     */
    public void setMatriculaVO(MatriculaVO matriculaVO) {
        this.matriculaVO = matriculaVO;
    }

    /**
     * @return the qtdMatricula
     */
    public Integer getQtdMatricula() {
        return qtdMatricula;
    }

    /**
     * @param qtdMatricula
     *            the qtdMatricula to set
     */
    public void setQtdMatricula(Integer qtdMatricula) {
        this.qtdMatricula = qtdMatricula;
    }

    /**
     * @return the valorMatriculaTotal
     */
    public Double getValorMatriculaTotal() {
        return valorMatriculaTotal;
    }

    /**
     * @param valorMatriculaTotal
     *            the valorMatriculaTotal to set
     */
    public void setValorMatriculaTotal(Double valorMatriculaTotal) {
        this.valorMatriculaTotal = valorMatriculaTotal;
    }

	public Date getDataReapresentacao() {
		if (dataReapresentacao == null) {
			dataReapresentacao = new Date();
		}
		return dataReapresentacao;
	}

	public void setDataReapresentacao(Date dataReapresentacao) {
		this.dataReapresentacao = dataReapresentacao;
	}

	public MapaLancamentoFuturoVO getMapaLancamentoFuturoVOReapresentacao() {
		if (MapaLancamentoFuturoVOReapresentacao == null) {
			MapaLancamentoFuturoVOReapresentacao = new MapaLancamentoFuturoVO();
		}
		return MapaLancamentoFuturoVOReapresentacao;
	}

	public void setMapaLancamentoFuturoVOReapresentacao(MapaLancamentoFuturoVO mapaLancamentoFuturoVOReapresentacao) {
		MapaLancamentoFuturoVOReapresentacao = mapaLancamentoFuturoVOReapresentacao;
	}
	
	public void inicializarDadosHistoricoCheque() {
		MapaLancamentoFuturoVO obj = (MapaLancamentoFuturoVO) context().getExternalContext().getRequestMap().get("mapaLancamentoFuturoChequeAPagar");
		List<ContaPagarVO> listaContaPagarVOs = getFacadeFactory().getContaPagarFacade().consultarPorCodigoCheque(obj.getCodigoCheque(), getUsuarioLogado());
		if (listaContaPagarVOs != null && !listaContaPagarVOs.isEmpty()) {
			setListaContaPagarChequeVOs(listaContaPagarVOs);
		}
	}

	public void navegarTelaContaPagar() {
		ContaPagarVO obj = (ContaPagarVO) context().getExternalContext().getRequestMap().get("contaPagarChequeVO");
		context().getExternalContext().getSessionMap().put("contaPagarChequeVO", obj);
		
	}

	public List<ContaPagarVO> getListaContaPagarChequeVOs() {
		if (listaContaPagarChequeVOs == null) {
			listaContaPagarChequeVOs = new ArrayList<ContaPagarVO>(0);
		}
		return listaContaPagarChequeVOs;
	}

	public void setListaContaPagarChequeVOs(List<ContaPagarVO> listaContaPagarChequeVOs) {
		this.listaContaPagarChequeVOs = listaContaPagarChequeVOs;
	}
	
	public String alterarDataBaixa() {
		try {
            if (isTabChequeAReceberSelecionada() || isTabChequeAPagarSelecionada()) {
                getFacadeFactory().getMapaLancamentoFuturoFacade().alterarDataBaixa(getMapaLancamentoFuturoVOSelecionado(), getUsuarioLogado());
            }
            setMensagemID("msg_dados_gravados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("mapaLancamentoFuturoCons.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("mapaLancamentoFuturoCons.xhtml");
        }
	}
}
