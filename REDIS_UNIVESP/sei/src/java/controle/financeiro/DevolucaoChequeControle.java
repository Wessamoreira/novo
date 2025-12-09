package controle.financeiro;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas devolChqRForm.jsp
 * devolChqRCons.jsp) com as funcionalidades da classe <code>DevolChqR</code>. Implemtação da camada controle (Backing
 * Bean).
 * 
 * @see SuperControle
 * @see DevolChqR
 * @see DevolucaoChequeVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.financeiro.CentroReceitaVO;
import negocio.comuns.financeiro.ChequeVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.DevolucaoChequeVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoCheque;

@Controller("DevolucaoChequeControle")
@Scope("viewScope")
@Lazy
public class DevolucaoChequeControle extends SuperControle implements Serializable {

    private DevolucaoChequeVO devolucaoChequeVO;
    protected List listaSelectItemCaixa;
    protected List listaSelectItemContaCorrente;
    protected List listaSelectItemCentroReceita;
    protected List listaSelectItemUnidadeEnsino;
    private String valorConsultaCheque;
    private String camposConsultaCheque;
    private List listaConsultaCheque;

    public DevolucaoChequeControle() throws Exception {
        //obterUsuarioLogado();
        setControleConsulta(new ControleConsulta());
        setMensagemID("msg_entre_prmconsulta");
    }

    public void incializarConfiguracaoGeralSistemaPadrao() {
        try {
//            ConfiguracaoFinanceiroVO obj = getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), null);
            ConfiguracaoFinanceiroVO obj = getConfiguracaoFinanceiroPadraoSistema();
            getDevolucaoChequeVO().getCentroReceita().setCodigo(obj.getCentroReceitaNegociacaoPadrao().getCodigo());
        } catch (Exception e) {
        }
    }

    public void incializarResponsavel() {
        try {
            getDevolucaoChequeVO().setResponsavel(getUsuarioLogadoClone());
        } catch (Exception e) {
        }
    }

    /**
     * Rotina responsável por disponibilizar um novo objeto da classe <code>DevolChqR</code> para edição pelo usuário da
     * aplicação.
     */
    public String novo() {
        removerObjetoMemoria(this);
        setDevolucaoChequeVO(new DevolucaoChequeVO());
        inicializarListasSelectItemTodosComboBox();
        incializarResponsavel();
        incializarConfiguracaoGeralSistemaPadrao();
        setMensagemID("msg_entre_dados");
        return Uteis.getCaminhoRedirecionamentoNavegacao("devolucaoChequeForm.xhtml");
    }

    public String novoMapaLancamento() {
    	removerObjetoMemoria(this);
    	setDevolucaoChequeVO(new DevolucaoChequeVO());
    	incializarResponsavel();
    	incializarConfiguracaoGeralSistemaPadrao();
    	setMensagemID("msg_entre_dados");
    	return Uteis.getCaminhoRedirecionamentoNavegacao("devolucaoChequeForm.xhtml");
    }
    
    /**
     * Rotina responsável por disponibilizar os dados de um objeto da classe <code>DevolChqR</code> para alteração. O
     * objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa
     * disponibilizá-lo para edição.
     */
    public String editar() {
    	try {
    		DevolucaoChequeVO obj = (DevolucaoChequeVO) context().getExternalContext().getRequestMap().get("devolucaoChequeItens");
    		editar(obj.getCodigo());
    		setMensagemID("msg_dados_editar");
    		return Uteis.getCaminhoRedirecionamentoNavegacao("devolucaoChequeForm.xhtml");
    	}catch (Exception e) {
    		setMensagemDetalhada("msg_erro", e.getMessage());
    		return Uteis.getCaminhoRedirecionamentoNavegacao("devolucaoChequeCons.xhtml");
		}
    }

    /**
     * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>DevolChqR</code>. Caso o
     * objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é
     * acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado
     * para o usuário juntamente com uma mensagem de erro.
     */
    public String gravar() {
        try {
            if (devolucaoChequeVO.isNovoObj().booleanValue()) {
                getFacadeFactory().getDevolucaoChequeFacade().incluir(devolucaoChequeVO, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(devolucaoChequeVO.getUnidadeEnsino().getCodigo()), getUsuarioLogado());
            } else {
                throw new Exception("Não é possivel alterar uma devolução de cheque.");
            }
            executarMetodoControle(MapaLancamentoFuturoControle.class.getSimpleName(), "consultar");
            setMensagemID("msg_dados_gravados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("devolucaoChequeForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("devolucaoChequeForm.xhtml");
        }
    }

    public void setDevolucaoChequeVOPreenchido(DevolucaoChequeVO devolucaoChequeVO) {
    	novoMapaLancamento();
        this.devolucaoChequeVO = devolucaoChequeVO;
        inicializarListasSelectItemTodosComboBox();
        
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP DevolChqRCons.jsp. Define o tipo de consulta a
     * ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como resultado,
     * disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    public String consultar() {
        try {
            super.consultar();
            List objs = new ArrayList(0);
            if (getControleConsulta().getCampoConsulta().equals("codigo")) {
                if (getControleConsulta().getValorConsulta().equals("")) {
                    getControleConsulta().setValorConsulta("0");
                }
                if (getControleConsulta().getValorConsulta().trim() != null || !getControleConsulta().getValorConsulta().trim().isEmpty()) {
                    Uteis.validarSomenteNumeroString(getControleConsulta().getValorConsulta().trim());
                }
                int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
                objs = getFacadeFactory().getDevolucaoChequeFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("data")) {
                objs = getFacadeFactory().getDevolucaoChequeFacade().consultarPorData(Uteis.getDateTime(getControleConsulta().getDataIni(), 0, 0, 0),
                        Uteis.getDateTime(getControleConsulta().getDataIni(), 23, 59, 59), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("nomeUsuario")) {
                objs = getFacadeFactory().getDevolucaoChequeFacade().consultarPorNomeUsuario(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("nomePessoa")) {
                objs = getFacadeFactory().getDevolucaoChequeFacade().consultarPorNomePessoa(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("numeroCheque")) {
                objs = getFacadeFactory().getDevolucaoChequeFacade().consultarPorNumeroCheque(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            setListaConsulta(objs);
            setMensagemID("msg_dados_consultados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("devolucaoChequeCons.xhtml");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("devolucaoChequeCons.xhtml");
        }
    }

    /**
     * Operação responsável por processar a exclusão um objeto da classe <code>DevolucaoChequeVO</code> Após a exclusão
     * ela automaticamente aciona a rotina para uma nova inclusão.
     */
    public String excluir() {
        try {
            getFacadeFactory().getDevolucaoChequeFacade().excluir(devolucaoChequeVO, getUsuarioLogado());
            setDevolucaoChequeVO(new DevolucaoChequeVO());
            incializarResponsavel();
            incializarConfiguracaoGeralSistemaPadrao();
            setMensagemID("msg_dados_excluidos");
            return Uteis.getCaminhoRedirecionamentoNavegacao("devolucaoChequeForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("devolucaoChequeForm.xhtml");
        }
    }

    public void consultarChequePorNumero() {
        try {
            String numero = getDevolucaoChequeVO().getCheque().getNumero();
            ChequeVO cheque = getFacadeFactory().getChequeFacade().consultarPorNumeroUnico(numero, getDevolucaoChequeVO().getContaCorrente().getCodigo(),
                    getDevolucaoChequeVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            getDevolucaoChequeVO().setCheque(cheque);
            getDevolucaoChequeVO().setPessoa(cheque.getPessoa());            
            getDevolucaoChequeVO().setContaCorrente(cheque.getLocalizacaoCheque());
            numero = null;
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            getDevolucaoChequeVO().getCheque().setNumero("");
            getDevolucaoChequeVO().getCheque().setSacado("");
            getDevolucaoChequeVO().getCheque().getPessoa().setNome("");
            getDevolucaoChequeVO().setContaCorrente(new ContaCorrenteVO());
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void consultarCheque() {
        try {
            if (getCamposConsultaCheque().equals("nomePessoa")) {
            	setListaConsultaCheque(getFacadeFactory().getChequeFacade().consultarPorNomePessoaSituacao(getValorConsultaCheque(), Arrays.asList(SituacaoCheque.PENDENTE.getValor(),SituacaoCheque.BANCO.getValor()), 0, getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
            }
            if (getCamposConsultaCheque().equals("sacado")) {
            	setListaConsultaCheque(getFacadeFactory().getChequeFacade().consultarPorSacadoSituacao(getValorConsultaCheque(), Arrays.asList(SituacaoCheque.PENDENTE.getValor(),SituacaoCheque.BANCO.getValor()), 0, getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
            }
            if (getCamposConsultaCheque().equals("nomeBanco")) {
            	setListaConsultaCheque(getFacadeFactory().getChequeFacade().consultarPorNomeBancoSituacao(getValorConsultaCheque(), Arrays.asList(SituacaoCheque.PENDENTE.getValor(),SituacaoCheque.BANCO.getValor()), 0, getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
            }
            if (getCamposConsultaCheque().equals("numeroAgenciaAgencia")) {
            	setListaConsultaCheque(getFacadeFactory().getChequeFacade().consultarPorNumeroAgenciaAgenciaSituacao(getValorConsultaCheque(), Arrays.asList(SituacaoCheque.PENDENTE.getValor(),SituacaoCheque.BANCO.getValor()), 0, getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
            }
            if (getCamposConsultaCheque().equals("numero")) {
            	setListaConsultaCheque(getFacadeFactory().getChequeFacade().consultarPorNumeroSituacao(getValorConsultaCheque(), Arrays.asList(SituacaoCheque.PENDENTE.getValor(),SituacaoCheque.BANCO.getValor()), 0, getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
            }
            if (getCamposConsultaCheque().equals("dataEmissao")) {
            	setListaConsultaCheque(getFacadeFactory().getChequeFacade().consultarPorDataEmissaoSituacao(getControleConsulta().getDataIni(), getControleConsulta().getDataIni(), Arrays.asList(SituacaoCheque.PENDENTE.getValor(),SituacaoCheque.BANCO.getValor()), 0, getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
            }
            if (getCamposConsultaCheque().equals("dataPrevisao")) {
            	setListaConsultaCheque(getFacadeFactory().getChequeFacade().consultarPorDataPrevisaoSituacao(getControleConsulta().getDataIni(), getControleConsulta().getDataIni(), Arrays.asList(SituacaoCheque.PENDENTE.getValor(),SituacaoCheque.BANCO.getValor()), 0, getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
            }
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaCheque(new ArrayList<ChequeVO>(0));
            setMensagemDetalhada("msg_erro", e.getMessage());

        }
    }

    private List<SelectItem> tipoConsultaComboCheque;
    
    public List<SelectItem> getTipoConsultaComboCheque() {
    	if (tipoConsultaComboCheque == null) {
    		tipoConsultaComboCheque = new ArrayList<>(0);
    		tipoConsultaComboCheque.add(new SelectItem("nomePessoa", "Emitente"));
    		tipoConsultaComboCheque.add(new SelectItem("sacado", "Sacado"));
    		tipoConsultaComboCheque.add(new SelectItem("nomeBanco", "Banco"));
    		tipoConsultaComboCheque.add(new SelectItem("numeroAgenciaAgencia", "Agência"));
    		tipoConsultaComboCheque.add(new SelectItem("numero", "Número"));
            tipoConsultaComboCheque.add(new SelectItem("dataEmissao", "Data Emissão"));
            tipoConsultaComboCheque.add(new SelectItem("dataPrevisao", "Data Previsão"));
    	}
    	return tipoConsultaComboCheque;
    }

    public boolean isCampoConsultaChequeData() {
        return getCamposConsultaCheque() != null && (getCamposConsultaCheque().equals("dataEmissao") || getCamposConsultaCheque().equals("dataPrevisao"));
    }

    public void selecionarCheque() {
        ChequeVO cheque = (ChequeVO) context().getExternalContext().getRequestMap().get("chequeItens");
        getDevolucaoChequeVO().setCheque(cheque);
        getDevolucaoChequeVO().setPessoa(cheque.getPessoa());
        getDevolucaoChequeVO().setParceiro(cheque.getParceiro());
        getDevolucaoChequeVO().setFornecedor(cheque.getFornecedor());
        getDevolucaoChequeVO().setContaCorrente(cheque.getLocalizacaoCheque());
    }

    public void irPaginaInicial() throws Exception {
        // controleConsulta.setPaginaAtual(1);
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

    public void montarListaSelectItemContaCorrente(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarContaCorrentePorNumero(false);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                ContaCorrenteVO obj = (ContaCorrenteVO) i.next();
                if(Uteis.isAtributoPreenchido(obj.getNomeApresentacaoSistema())){
                	objs.add(new SelectItem(obj.getCodigo(), obj.getNomeApresentacaoSistema()));	
                }else{
                	objs.add(new SelectItem(obj.getCodigo(), obj.getNumero().toString() + "-" + obj.getDigito().toString()));
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
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>Agencia</code>. Buscando todos os objetos
     * correspondentes a entidade <code>Agencia</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto
     * é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemContaCorrente() {
        try {
            montarListaSelectItemContaCorrente("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
     * relativo ao atributo <code>Caixa</code>.
     */
    public void montarListaSelectItemCaixa(String prm) throws Exception {
        List resultadoConsulta = consultarContaCorrentePorNumero(true);
        Iterator i = resultadoConsulta.iterator();
        List objs = new ArrayList(0);
        objs.add(new SelectItem(0, ""));
        while (i.hasNext()) {
            ContaCorrenteVO obj = (ContaCorrenteVO) i.next();
            if(Uteis.isAtributoPreenchido(obj.getNomeApresentacaoSistema())){
            	objs.add(new SelectItem(obj.getCodigo(), obj.getNomeApresentacaoSistema()));	
            }else{
            	objs.add(new SelectItem(obj.getCodigo(), obj.getNumero().toString() + " - " + obj.getDigito()));
            } 
        }
        Uteis.liberarListaMemoria(resultadoConsulta);
        i = null;
        setListaSelectItemCaixa(objs);

    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>Caixa</code>. Buscando todos os objetos
     * correspondentes a entidade <code>ContaCorrente</code>. Esta rotina não recebe parâmetros para filtragem de dados,
     * isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemCaixa() {
        try {
            montarListaSelectItemCaixa("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>numero</code> Este
     * atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox
     * correspondente
     */
    public List consultarContaCorrentePorNumero(Boolean contaCaixa) throws Exception {
        List lista = getFacadeFactory().getContaCorrenteFacade().consultarPorContaCaixa(contaCaixa, getDevolucaoChequeVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
        return lista;
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
     * relativo ao atributo <code>CentroReceitaPadraoContaReceber</code>.
     */
    public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            if (getUnidadeEnsinoLogado().getCodigo().intValue() != 0) {
                getListaSelectItemUnidadeEnsino().add(new SelectItem(getUnidadeEnsinoLogado().getCodigo(), getUnidadeEnsinoLogado().getNome()));
                getDevolucaoChequeVO().getUnidadeEnsino().setCodigo(getUnidadeEnsinoLogado().getCodigo());
                return;
            }
            resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
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
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>CentroReceitaPadraoContaReceber</code>.
     * Buscando todos os objetos correspondentes a entidade <code>CentroReceita</code>. Esta rotina não recebe
     * parâmetros para filtragem de dados, isto é importante para a inicialização dos dados da tela para o acionamento
     * por meio requisições Ajax.
     */
    public void montarListaSelectItemUnidadeEnsino() {
        try {
            montarListaSelectItemUnidadeEnsino("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nome</code> Este
     * atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox
     * correspondente
     */
    public List consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, 0, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
        return lista;
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
     * relativo ao atributo <code>CentroReceitaPadraoContaReceber</code>.
     */
    public void montarListaSelectItemCentroReceita(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarCentroReceitaPorNome(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                CentroReceitaVO obj = (CentroReceitaVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getDescricao().toString()));
            }
            setListaSelectItemCentroReceita(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>CentroReceitaPadraoContaReceber</code>.
     * Buscando todos os objetos correspondentes a entidade <code>CentroReceita</code>. Esta rotina não recebe
     * parâmetros para filtragem de dados, isto é importante para a inicialização dos dados da tela para o acionamento
     * por meio requisições Ajax.
     */
    public void montarListaSelectItemCentroReceita() {
        try {
            montarListaSelectItemCentroReceita("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nome</code> Este
     * atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox
     * correspondente
     */
    public List consultarCentroReceitaPorNome(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getCentroReceitaFacade().consultarPorDescricao(nomePrm, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
        return lista;
    }

    /**
     * Método responsável por inicializar a lista de valores (<code>SelectItem</code>) para todos os ComboBox's.
     */
    public void inicializarListasSelectItemTodosComboBox() {
    	montarListaSelectItemUnidadeEnsino();
        montarListaSelectItemContaCorrente();
//        montarListaSelectItemCaixa();
        montarListaSelectItemCentroReceita();
        if (getDevolucaoChequeVO().getUnidadeEnsino().getCodigo() != 0) {
            montarListaSelectItemCaixa();
        }
    }

    /**
     * Rotina responsável por preencher a combo de consulta da telas.
     */
    public List getTipoConsultaCombo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nomePessoa", "Sacado"));
        itens.add(new SelectItem("numeroCheque", "Cheque"));
        itens.add(new SelectItem("codigo", "Código"));
        itens.add(new SelectItem("data", "Data"));
        itens.add(new SelectItem("nomeUsuario", "Responsável"));        
        return itens;
    }

    public boolean isCampoData() {
        return (getControleConsulta().getCampoConsulta().equals("data"));
    }

    /**
     * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
     */
    public String inicializarConsultar() {
        removerObjetoMemoria(this);
        setListaConsulta(new ArrayList(0));
        setMensagemID("msg_entre_prmconsulta");
        return Uteis.getCaminhoRedirecionamentoNavegacao("devolucaoChequeCons.xhtml");
    }

    public List getListaSelectItemCentroReceita() {
        if (listaSelectItemCentroReceita == null) {
            listaSelectItemCentroReceita = new ArrayList(0);
        }
        return listaSelectItemCentroReceita;
    }

    public void setListaSelectItemCentroReceita(List listaSelectItemCentroReceita) {
        this.listaSelectItemCentroReceita = listaSelectItemCentroReceita;
    }

    public String getCamposConsultaCheque() {
        if (camposConsultaCheque == null) {
            camposConsultaCheque = "";
        }
        return camposConsultaCheque;
    }

    public void setCamposConsultaCheque(String camposConsultaCheque) {
        this.camposConsultaCheque = camposConsultaCheque;
    }

    public List<ChequeVO> getListaConsultaCheque() {
        if (listaConsultaCheque == null) {
            listaConsultaCheque = new ArrayList<ChequeVO>(0);
        }
        return listaConsultaCheque;
    }

    public void setListaConsultaCheque(List listaConsultaCheque) {
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

    public DevolucaoChequeVO getDevolucaoChequeVO() {
        if (devolucaoChequeVO == null) {
            devolucaoChequeVO = new DevolucaoChequeVO();
        }
        return devolucaoChequeVO;
    }

    public void setDevolucaoChequeVO(DevolucaoChequeVO devolucaoChequeVO) {
        this.devolucaoChequeVO = devolucaoChequeVO;
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

    @Override
    protected void limparRecursosMemoria() {
        super.limparRecursosMemoria();
        devolucaoChequeVO = null;
        Uteis.liberarListaMemoria(listaSelectItemCaixa);
        Uteis.liberarListaMemoria(listaSelectItemContaCorrente);
        valorConsultaCheque = null;
        camposConsultaCheque = null;
        Uteis.liberarListaMemoria(listaConsultaCheque);
    }
    
    @PostConstruct
	public void realizarCarregamentoDevolucaoChequeVindoTelaExtratoContaCorrente() {
		try {
			if (context().getExternalContext().getSessionMap().get("devolucaoChequeExtratoContaCorrente") != null && context().getExternalContext().getSessionMap().get("devolucaoChequeExtratoContaCorrente") instanceof Integer && Uteis.isAtributoPreenchido(((Integer)context().getExternalContext().getSessionMap().get("devolucaoChequeExtratoContaCorrente")))) {
				Integer codigo = (Integer) context().getExternalContext().getSessionMap().get("devolucaoChequeExtratoContaCorrente");
				if (Uteis.isAtributoPreenchido(codigo)) {
					editar(codigo);					
				}
				setMensagemID("msg_dados_editar", Uteis.ALERTA);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			context().getExternalContext().getSessionMap().remove("devolucaoChequeExtratoContaCorrente");
		}
	}
    
    public void editar(Integer codigo) throws Exception{
    	setDevolucaoChequeVO(getFacadeFactory().getDevolucaoChequeFacade().consultarPorChavePrimaria(codigo, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
        getDevolucaoChequeVO().setNovoObj(Boolean.FALSE);
        inicializarListasSelectItemTodosComboBox();
        incializarConfiguracaoGeralSistemaPadrao();
    }
}
