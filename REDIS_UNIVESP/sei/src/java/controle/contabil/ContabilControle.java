package controle.contabil;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas 
 * contabilForm.jsp contabilCons.jsp) com as funcionalidades da classe <code>Contabil</code>.
 * Implemtação da camada controle (Backing Bean).
 * @see SuperControle
 * @see Contabil
 * @see ContabilVO
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

import controle.arquitetura.SuperControle;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.contabil.ContabilVO;
import negocio.comuns.contabil.PlanoContaVO;
import negocio.comuns.contabil.TipoEventoContabilVO;
import negocio.comuns.financeiro.BancoVO;
import negocio.comuns.financeiro.ContaPagarVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoPessoa; @Controller("ContabilControle")
@Scope("request")
@Lazy
public class ContabilControle extends SuperControle implements Serializable {

    private ContabilVO contabilVO;
    protected List listaSelectItemConta;
    protected List listaSelectItemPessoa;
    protected List listaSelectItemFornecedor;
    protected List listaConsultaContaCredito;
    protected String campoConsultaContaCredito;
    protected String valorConsultaContaCredito;
    protected List listaConsultaContaContraPartida;
    protected String campoConsultaContaContraPartida;
    protected String valorConsultaContaContraPartida;
    protected List listaConsultaContaDebito;
    protected String campoConsultaContaDebito;
    protected String valorConsultaContaDebito;
    protected String campoApresentarTipoEventoContabil;
    private String campoConsultaFornecedor;
    private String valorConsultaFornecedor;
    private List listaConsultaFornecedor;
    private String campoConsultaPessoa;
    private String valorConsultaPessoa;
    private List listaConsultaPessoa;
    private String campoConsultaTipoEventoContabil;
    private String valorConsultaTipoEventoContabil;
    private List listaConsultaTipoEventoContabil;
    private PlanoContaVO contaContraPartida;
    private PlanoContaVO contaCredito;
    private PlanoContaVO contaDebito;
    private String tipoLancamentoContabil;
    private String tipoDestinoLancamento;
    private String matricula;
    private String matriculaFuncionario;
    protected List listaConsultaBanco;
    protected String valorConsultaBanco;
    protected String campoConsultaBanco;
    protected List listaConsultaFuncionario;
    protected String valorConsultaFuncionario;
    protected String campoConsultaFuncionario;
    protected List listaConsultaContaReceber;
    protected String valorConsultaContaReceber;
    protected String campoConsultaContaReceber;
    protected String situacaoContaReceber;
    private String campoConsultarContaPagar;
    private Date dataInicioConsultarContaPagar;
    private Date dataTerminoConsultarContaPagar;
    private List listaConsultarContaPagar;
    private String campoApresentarContaReceber;
    private String campoApresentarContaPagar;

    public ContabilControle() throws Exception {
        //obterUsuarioLogado();
        setControleConsulta(new ControleConsulta());
        setMensagemID("msg_entre_prmconsulta");
    }

    /**
     * Rotina responsável por disponibilizar um novo objeto da classe <code>Contabil</code>
     * para edição pelo usuário da aplicação.
     */
    public String novo() throws Exception {         removerObjetoMemoria(this);
        setContabilVO(new ContabilVO());
        getContabilVO().setUnidadeEnsino(getUnidadeEnsinoLogadoClone());
        inicializarListasSelectItemTodosComboBox();
        setContaContraPartida(new PlanoContaVO());
        limparCamposTipoEventoContabil();
        limparCamposTipoLancamentoContabil();
        setTipoLancamentoContabil("recebimento");
        setMensagemID("msg_entre_dados");
        return "editar";
    }

    /**
     * Rotina responsável por disponibilizar os dados de um objeto da classe <code>Contabil</code> para alteração.
     * O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa disponibilizá-lo para edição.
     */
    public String editar() {
        ContabilVO obj = (ContabilVO) context().getExternalContext().getRequestMap().get("contabil");
        inicializarAtributosRelacionados(obj);
        obj.setNovoObj(Boolean.FALSE);
        setContabilVO(obj);
        inicializarListasSelectItemTodosComboBox();
        setMensagemID("msg_dados_editar");
        return "editar";
    }

    /**
     * Método responsável inicializar objetos relacionados a classe <code>ContabilVO</code>.
     * Esta inicialização é necessária por exigência da tecnologia JSF, que não trabalha com valores nulos para estes atributos.
     */
    public void inicializarAtributosRelacionados(ContabilVO obj) {
        if (obj.getConta() == null) {
            obj.setConta(new PlanoContaVO());
        }
        if (obj.getPessoa() == null) {
            obj.setPessoa(new PessoaVO());
        }
        if (obj.getFornecedor() == null) {
            obj.setFornecedor(new FornecedorVO());
        }
    }

    
    public String gravarComContraPartida() {
        try {
            if (getTipoLancamentoContabilPagamento() &&
                    !getTipoDestinoLancamentoBanco() &&
                    !getTipoDestinoLancamentoFornecedor() &&
                    !getTipoDestinoLancamentoFuncionario()){
                throw new Exception("Informe um tipo de Sacado.");
            }
            getFacadeFactory().getContabilFacade().gravarComContraPartida(getContabilVO(), getContaDebito(), getContaCredito(), getContaContraPartida(), getTipoLancamentoContabilPagamento(), getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema());
            setMensagemID("msg_dados_gravados");
            return "editar";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "editar";
        }
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP ContabilCons.jsp.
     * Define o tipo de consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP.
     * Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    public String consultar() {
        try {
            super.consultar();
            setListaConsulta(getFacadeFactory().getContabilFacade().consultar(getControleConsulta().getCampoConsulta(), getControleConsulta().getValorConsulta(), getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
            setMensagemID("msg_dados_consultados");
            return "consultar";
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "consultar";
        }
    }

    /**
     * Operação responsável por processar a exclusão um objeto da classe <code>ContabilVO</code>
     * Após a exclusão ela automaticamente aciona a rotina para uma nova inclusão.
     */
    public String excluir() {
        try {
            getFacadeFactory().getContabilFacade().excluir(contabilVO);
            novo();
            setMensagemID("msg_dados_excluidos");
            return "editar";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "editar";
        }
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher
     * o comboBox relativo ao atributo <code>Fornecedor</code>.
     */
    public void montarListaSelectItemFornecedor(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarFornecedorPorNome(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                FornecedorVO obj = (FornecedorVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
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
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>Fornecedor</code>.
     * Buscando todos os objetos correspondentes a entidade <code>Fornecedor</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é
     * importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemFornecedor() {
        try {
            montarListaSelectItemFornecedor("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nome</code>
     * Este atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox correspondente
     */
    public List consultarFornecedorPorNome(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getFornecedorFacade().consultarPorNome(nomePrm, "AT", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
        return lista;
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher
     * o comboBox relativo ao atributo <code>Conta</code>.
     */
    public void montarListaSelectItemConta(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarPlanoContaPorIdentificadorPlanoConta(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                PlanoContaVO obj = (PlanoContaVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getIdentificadorPlanoConta().toString()));
            }
            resultadoConsulta = new ArrayList(0);
            setListaSelectItemConta(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>Conta</code>.
     * Buscando todos os objetos correspondentes a entidade <code>PlanoConta</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é
     * importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemConta() {
        try {
            montarListaSelectItemConta("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    public void consultarPessoaPorMatricula() {
        try {
            getContabilVO().setPessoa(getFacadeFactory().getPessoaFacade().consultarAlunoPorMatricula(getMatricula(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
            setMensagemDetalhada("");
        } catch (Exception e) {
            setMensagemDetalhada(e.getMessage());
        }
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>identificadorPlanoConta</code>
     * Este atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox correspondente
     */
    public List consultarPlanoContaPorIdentificadorPlanoConta(String identificadorPlanoContaPrm) throws Exception {
        List lista = getFacadeFactory().getPlanoContaFacade().consultarPorIdentificadorPlanoConta(identificadorPlanoContaPrm, getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
        return lista;
    }

    public void selecionarBanco() {
        BancoVO obj = (BancoVO) context().getExternalContext().getRequestMap().get("banco");
        this.getContabilVO().setBanco(obj);
    }

    public void consultarBancos() {
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

    public List getTipoConsultaComboBancos() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("nrBanco", "Número do Banco"));
        return itens;
    }

    public void selecionarFuncionario() {
        FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionario");
        this.getContabilVO().setPessoa(obj.getPessoa());
        setMatriculaFuncionario(obj.getMatricula());
        Uteis.liberarListaMemoria(getListaConsultaFuncionario());
        valorConsultaFuncionario = "";
        campoConsultaFuncionario = "";

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
                objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeCidade(getValorConsultaFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(), false,
                        Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("CPF")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultarPorCPF(getValorConsultaFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("cargo")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeCargo(getValorConsultaFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(), false,
                        Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("departamento")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeDepartamento(getValorConsultaFuncionario(), "FU", this.getUnidadeEnsinoLogado().getCodigo(), false,
                        Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("unidadeEnsino")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeUnidadeEnsino(getValorConsultaFuncionario(), "FU", this.getUnidadeEnsinoLogado().getCodigo(), false,
                        Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
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
        itens.add(new SelectItem("CPF", "CPF"));
        itens.add(new SelectItem("matricula", "Matrícula"));
        itens.add(new SelectItem("cargo", "Cargo"));
        itens.add(new SelectItem("departamento", "Departamento"));

        return itens;
    }

    public void consultarFuncionarioPorCodigo() {
        try {
            FuncionarioVO funcionario = getFacadeFactory().getFuncionarioFacade().consultarPorRequisitanteMatricula(this.getMatriculaFuncionario(),
                    this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            if (funcionario.getCodigo().intValue() != 0) {
                this.getContabilVO().setPessoa(funcionario.getPessoa());
                setMensagemID("msg_dados_consultados");
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            setMatriculaFuncionario("");
            getContabilVO().setPessoa(new PessoaVO());
            setMensagemID("msg_erro_dadosnaoencontrados");
        }
    }

    /**
     * Método responsável por inicializar a lista de valores (<code>SelectItem</code>) para todos os ComboBox's.
     */
    public void inicializarListasSelectItemTodosComboBox() {
//        montarListaSelectItemConta();
//        montarListaSelectItemPessoa();
//        montarListaSelectItemFornecedor();
    }

    public void limparCamposTipoLancamentoContabil() {
        getContabilVO().setPessoa(new PessoaVO());
        getContabilVO().setBanco(new BancoVO());
        getContabilVO().setFornecedor(new FornecedorVO());
        getContabilVO().setContaPagar(new ContaPagarVO());
        setCampoApresentarContaPagar("");
        setCampoApresentarContaReceber("");
        getContabilVO().setContaReceber(new ContaReceberVO());
        setMatricula("");
        setMatriculaFuncionario("");
        getContabilVO().setJuro(0.0);
        getContabilVO().setDesconto(0.0);
    }

    public void selecionarContaReceber() {
        try {
            ContaReceberVO obj = (ContaReceberVO) context().getExternalContext().getRequestMap().get("contaReceber");
            getContabilVO().setContaReceber(obj);
            getContabilVO().setJuro(obj.getJuro());
            getContabilVO().setDesconto(obj.getValorDesconto());
            getContabilVO().setValor(obj.getValor());
            getContabilVO().setNumeroDocumento(obj.getNrDocumento());
            setCampoApresentarContaReceber(Uteis.formatarDecimalDuasCasas(getTotal()) + " - " + obj.getDataVencimento_Apresentar());
            setMensagemID("msg_dados_adicionados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());

        }
    }

    public void consultarContaReceber() {
        try {
            List objs = new ArrayList(0);

            if (getCampoConsultaContaReceber().equals("nrDocumento")) {
                objs = getFacadeFactory().getContaReceberFacade().consultarPorNrDocumentoPessoa(getValorConsultaContaReceber(), getContabilVO().getPessoa().getCodigo(),
                        getSituacaoContaReceber(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            }
            if (getCampoConsultaContaReceber().equals("codigobarra")) {
                objs = getFacadeFactory().getContaReceberFacade().consultarPorCodigoBarraSituacaoPessoa(TipoPessoa.ALUNO,getValorConsultaContaReceber(), getContabilVO().getPessoa().getCodigo(), 0, 0, "",
                        getSituacaoContaReceber(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            }
            if (getCampoConsultaContaReceber().equals("identificadorCentroReceitaCentroReceita")) {
                objs = getFacadeFactory().getContaReceberFacade().consultarPorIdentificadorCentroReceitaSituacaoPessoa(getValorConsultaContaReceber(),
                        getContabilVO().getPessoa().getCodigo(), getSituacaoContaReceber(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            }
            if (getCampoConsultaContaReceber().equals("data")) {
                Date valorData = Uteis.getDate(getValorConsultaContaReceber());
                objs = getFacadeFactory().getContaReceberFacade().consultarPorDataSituacaoPessoa(TipoPessoa.ALUNO,Uteis.getDateTime(valorData, 0, 0, 0), getContabilVO().getPessoa().getCodigo(), 0, 0, "",  
                        getSituacaoContaReceber(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            }

            setListaConsultaContaReceber(getFacadeFactory().getContaReceberFacade().executarCalculoValorFinalASerPago(objs, getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema(), null));
            setMensagemID("msg_dados_consultados");

        } catch (Exception e) {
            setListaConsultaContaReceber(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public List getListasSituacaoContaReceber() throws Exception {
        List objs = new ArrayList(0);
        objs.add(new SelectItem("VE", "Vencido"));
        objs.add(new SelectItem("AV", "A vencer"));
        objs.add(new SelectItem("AM", "Ambos"));
        return objs;
    }

    public List getTipoConsultaComboContaReceber() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nrDocumento", "Nr. Documento"));
        itens.add(new SelectItem("identificadorCentroReceitaCentroReceita", "Centro de Receita"));
        itens.add(new SelectItem("data", "Data"));
        return itens;
    }

    public void selecionarContaPagar() throws Exception {
        try {
            ContaPagarVO obj = (ContaPagarVO) context().getExternalContext().getRequestMap().get("contaPagar");
            this.getContabilVO().setContaPagar(obj);
            getContabilVO().setValor(obj.getValor());
            getContabilVO().setJuro(obj.getJuro());
            getContabilVO().setDesconto(obj.getDesconto());
            getContabilVO().setNumeroDocumento(obj.getNrDocumento());
            setCampoApresentarContaPagar(Uteis.formatarDecimalDuasCasas(obj.getValor()) + " - " + obj.getDataVencimento_Apresentar());
            setListaConsultarContaPagar(new ArrayList(0));
            setMensagemID("msg_dados_adicionados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    /**
     * Rotina responsável por preencher a combo de consulta dos RichModal da telas.
     */
    public List getTipoConsultarComboContaPagar() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("vencidas", "Vencidas"));
        itens.add(new SelectItem("aVencer", "A vencer"));
        itens.add(new SelectItem("periodo", "Período"));
        return itens;
    }

    public void consultarContaPagar() {
        try {
            List objs = null;

            Integer codigoSacado = null;
            String tipoSacado = "";
            if (getTipoDestinoLancamentoFornecedor()) {
                codigoSacado = getContabilVO().getFornecedor().getCodigo();
                tipoSacado = "FO";
            } else if (getTipoDestinoLancamentoFuncionario()) {
                codigoSacado = getContabilVO().getPessoa().getCodigo();
                tipoSacado = "FU";
            } else if (getTipoDestinoLancamentoBanco()) {
                codigoSacado = getContabilVO().getBanco().getCodigo();
                tipoSacado = "BA";
            }

            if (getCampoConsultarContaPagar().equals("vencidas")) {
                objs = getFacadeFactory().getContaPagarFacade().consultarVencidosPorTipoSacado(codigoSacado, tipoSacado, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getCampoConsultarContaPagar().equals("aVencer")) {
                objs = getFacadeFactory().getContaPagarFacade().consultarAVencerPorTipoSacado(codigoSacado, tipoSacado, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getCampoConsultarContaPagar().equals("periodo")) {
                objs = getFacadeFactory().getContaPagarFacade().consultarVencidosEmPeriodo(codigoSacado, tipoSacado, dataInicioConsultarContaPagar,
                        dataTerminoConsultarContaPagar, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            setListaConsultarContaPagar(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultarContaPagar(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    /**
     * Rotina responsável por atribui um javascript com o método de mascara para campos do tipo Data, CPF, CNPJ, etc.
     */
    public String getMascaraConsulta() {
        return "";
    }

    public List getTipoSinal() throws Exception {
        List objs = new ArrayList(0);
        objs.add(new SelectItem("", ""));
        objs.add(new SelectItem("DE", "Débito"));
        objs.add(new SelectItem("CR", "Crédito"));
        return objs;
    }

    public List getTipoConsultaComboFornecedor() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("razaoSocial", "Razão Social"));
        itens.add(new SelectItem("codigo", "Código"));
        itens.add(new SelectItem("estado", "Estado"));
        itens.add(new SelectItem("cpf", "CPF"));
        itens.add(new SelectItem("cnpj", "CNPJ"));
        itens.add(new SelectItem("tipoPessoa", "Tipo Pessoa"));
        return itens;
    }

    public void consultarPessoa() {
        try {
            List objs = new ArrayList(0);
            if (getCampoConsultaPessoa().equals("nome")) {
                objs = getFacadeFactory().getPessoaFacade().consultarPorNome(getValorConsultaPessoa(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getCampoConsultaPessoa().equals("codigo")) {
                if (getValorConsultaPessoa().equals("")) {
                    setValorConsultaPessoa("0");
                }
                int valorInt = Integer.parseInt(getValorConsultaPessoa());
                objs = getFacadeFactory().getPessoaFacade().consultarPorCodigo(new Integer(valorInt), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }

            setListaConsultaPessoa(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaPessoa(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarPessoa() throws Exception {
        PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("cliente");
        obj = getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
        this.getContabilVO().setPessoa(obj);
        this.getListaConsultaPessoa().clear();
        obj = null;
        this.setValorConsultaPessoa(null);
        this.setCampoConsultaPessoa(null);
    }

    public void consultarFornecedor() {
        try {
            List objs = new ArrayList(0);
            if (getCampoConsultaFornecedor().equals("razaoSocial")) {
                objs = getFacadeFactory().getFornecedorFacade().consultarPorRazaoSocial(getValorConsultaFornecedor(), "AT", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getCampoConsultaFornecedor().equals("nome")) {
                objs = getFacadeFactory().getFornecedorFacade().consultarPorNome(getValorConsultaFornecedor(), "AT", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getCampoConsultaFornecedor().equals("codigo")) {
                if (getValorConsultaFornecedor().equals("")) {
                    setValorConsultaFornecedor("0");
                }
                int valorInt = Integer.parseInt(getValorConsultaFornecedor());
                objs = getFacadeFactory().getFornecedorFacade().consultarPorCodigo(new Integer(valorInt), "AT", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getCampoConsultaFornecedor().equals("cpf")) {
                objs = getFacadeFactory().getFornecedorFacade().consultarPorCPF(getValorConsultaFornecedor(), "AT", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getCampoConsultaFornecedor().equals("cnpj")) {
                objs = getFacadeFactory().getFornecedorFacade().consultarPorCNPJ(getValorConsultaFornecedor(),"AT",  false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getCampoConsultaFornecedor().equals("razaoSocial")) {
                objs = getFacadeFactory().getFornecedorFacade().consultarPorRazaoSocial(getValorConsultaFornecedor(), "AT", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }

            setListaConsultaFornecedor(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarFornecedor() throws Exception {
        FornecedorVO obj = (FornecedorVO) context().getExternalContext().getRequestMap().get("fornecedor");
        if (getMensagemDetalhada().equals("")) {
            this.getContabilVO().setFornecedor(obj);
        }
        this.getListaConsultaFornecedor().clear();
        obj = null;
        this.setValorConsultaFornecedor(null);
        this.setCampoConsultaFornecedor(null);
    }

    public List getTipoConsultaComboPessoa() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("codigo", "Código"));
        return itens;
    }

    /**
     * Rotina responsável por preencher a combo de consulta da telas.
     */
    public List getTipoConsultaCombo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("codigo", "Código"));
        itens.add(new SelectItem("numeroRegistro", "Número Registro"));
        itens.add(new SelectItem("identificadorPlanoContaPlanoConta", "Conta"));
        //itens.add(new SelectItem("numeroDocumento", "Número Documento"));
        //itens.add(new SelectItem("sinal", "Sinal"));
        itens.add(new SelectItem("nomePessoa", "Pessoa"));
        itens.add(new SelectItem("nomeFornecedor", "Fornecedor"));
        return itens;
    }

    public void consultarTipoEventoContabil() {
        try {
            List objs = new ArrayList(0);

            if (getCampoConsultaTipoEventoContabil().equals("codigo")) {
                if (getValorConsultaTipoEventoContabil().equals("")) {
                    setValorConsultaTipoEventoContabil("0");
                }
                int valorInt = Integer.parseInt(getValorConsultaTipoEventoContabil());
                objs = getFacadeFactory().getTipoEventoContabilFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getCampoConsultaTipoEventoContabil().equals("descricao")) {
                objs = getFacadeFactory().getTipoEventoContabilFacade().consultarPorDescricao(getValorConsultaTipoEventoContabil(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getCampoConsultaTipoEventoContabil().equals("historico")) {
                objs = getFacadeFactory().getTipoEventoContabilFacade().consultarPorDescricaoHistorico(getValorConsultaTipoEventoContabil(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getCampoConsultaTipoEventoContabil().equals("contaCredito")) {
                objs = getFacadeFactory().getTipoEventoContabilFacade().consultarPorTipoPlanoContaCredito(getValorConsultaTipoEventoContabil(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getCampoConsultaTipoEventoContabil().equals("contaDebito")) {
                objs = getFacadeFactory().getTipoEventoContabilFacade().consultarPorTipoPlanoContaDebito(getValorConsultaTipoEventoContabil(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            setListaConsultaTipoEventoContabil(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaTipoEventoContabil(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarTipoEventoContabil() throws Exception {
        TipoEventoContabilVO obj = (TipoEventoContabilVO) context().getExternalContext().getRequestMap().get("tipoEventoContabil");
        if (getMensagemDetalhada().equals("")) {
            setContaCredito(obj.getContaCredito());
            setContaDebito(obj.getContaDebito());
            setCampoApresentarTipoEventoContabil(obj.getDescricao());
            getContabilVO().setHistorico(obj.getHistorico().getDescricao());
        }
        this.getListaConsultaTipoEventoContabil().clear();
        obj = null;
        this.setValorConsultaTipoEventoContabil(null);
        this.setCampoConsultaTipoEventoContabil(null);
    }

    public void limparCamposTipoEventoContabil() {
        setCampoApresentarTipoEventoContabil("");
        setContaCredito(new PlanoContaVO());
        setContaDebito(new PlanoContaVO());
        getContabilVO().setHistorico("");
    }

    public void limparCampoContaDebito() {
        setContaDebito(new PlanoContaVO());
    }

    public void limparCampoContaCredito() {
        setContaCredito(new PlanoContaVO());
    }

    public void limparCampoContaContraPartida() {
        setContaContraPartida(new PlanoContaVO());
    }

    public List getTipoConsultaComboTipoEventoContabil() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("codigo", "Código"));
        itens.add(new SelectItem("descricao", "Descrição"));
        itens.add(new SelectItem("historico", "Histórico"));
        itens.add(new SelectItem("contaCredito", "Conta Crédito"));
        itens.add(new SelectItem("contaDebito", "Conta Débito"));
        return itens;
    }

    public void consultarPlanoContaDebito() {
        try {
            List<PlanoContaVO> objs = new ArrayList(0);
            List<PlanoContaVO> lista = new ArrayList(0);
            if (getCampoConsultaContaDebito().equals("nome")) {
                objs = getFacadeFactory().getPlanoContaFacade().consultarPorDescricao(getValorConsultaContaDebito(), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getCampoConsultaContaDebito().equals("identificadorPlanoConta")) {
                objs = getFacadeFactory().getPlanoContaFacade().consultarPorIdentificadorPlanoConta(getValorConsultaContaDebito(), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            for (PlanoContaVO obj : objs) {
                Integer i = Uteis.contarQuantidadeDePontos(obj.getIdentificadorPlanoConta(), ".");
                if (i > 1) {
                    lista.add(obj);
                }
            }

            setListaConsultaContaDebito(lista);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaContaDebito(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void consultarPlanoContaContraPartida() {
        try {
            List<PlanoContaVO> objs = new ArrayList(0);
            List<PlanoContaVO> lista = new ArrayList(0);
            if (getCampoConsultaContaContraPartida().equals("nome")) {
                objs = getFacadeFactory().getPlanoContaFacade().consultarPorDescricao(getValorConsultaContaContraPartida(), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getCampoConsultaContaContraPartida().equals("identificadorPlanoConta")) {
                objs = getFacadeFactory().getPlanoContaFacade().consultarPorIdentificadorPlanoConta(getValorConsultaContaContraPartida(), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            for (PlanoContaVO obj : objs) {
                Integer i = Uteis.contarQuantidadeDePontos(obj.getIdentificadorPlanoConta(), ".");
                if (i > 1) {
                    lista.add(obj);
                }
            }

            setListaConsultaContaContraPartida(lista);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaContaContraPartida(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarPlanoContaCredito() {
        PlanoContaVO obj = (PlanoContaVO) context().getExternalContext().getRequestMap().get("planoContaCredito");
        this.setContaCredito(obj);
        setListaConsultaContaCredito(new ArrayList(0));
    }

    public void selecionarPlanoContaContraPartida() {
        PlanoContaVO obj = (PlanoContaVO) context().getExternalContext().getRequestMap().get("planoContaContraPartida");
        this.setContaContraPartida(obj);
        setListaConsultaContaContraPartida(new ArrayList(0));
    }

    public void consultarPlanoContaCredito() {
        try {
            List<PlanoContaVO> objs = new ArrayList(0);
            List<PlanoContaVO> lista = new ArrayList(0);
            if (getCampoConsultaContaCredito().equals("nome")) {
                objs = getFacadeFactory().getPlanoContaFacade().consultarPorDescricao(getValorConsultaContaCredito(), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getCampoConsultaContaCredito().equals("identificadorPlanoConta")) {
                objs = getFacadeFactory().getPlanoContaFacade().consultarPorIdentificadorPlanoConta(getValorConsultaContaCredito(), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            for (PlanoContaVO obj : objs) {
                Integer i = Uteis.contarQuantidadeDePontos(obj.getIdentificadorPlanoConta(), ".");
                if (i > 1) {
                    lista.add(obj);
                }
            }
            setListaConsultaContaCredito(lista);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaContaCredito(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarPlanoContaDebito() {
        PlanoContaVO obj = (PlanoContaVO) context().getExternalContext().getRequestMap().get("planoContaDebito");
        this.setContaDebito(obj);
        setListaConsultaContaDebito(new ArrayList(0));
    }

    public List getTipoConsultaComboPlanoConta() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("identificadorPlanoConta", "Identificador do Plano de Conta"));
        itens.add(new SelectItem("codigo", "Código"));
        return itens;
    }

    public void limparPlanoContaCredito() {
        setContaCredito(new PlanoContaVO());
    }

    public void limparPlanoContaDebito() {
        setContaDebito(new PlanoContaVO());
    }

    public void setarIdentificadorPlanoContaCredito() {
        setValorConsultaContaCredito(getContaCredito().getIdentificadorPlanoConta());
        setCampoConsultaContaCredito("identificadorPlanoConta");
        consultarPlanoContaCredito();
    }

    public void setarIdentificadorPlanoContaDebito() {
        setValorConsultaContaDebito(getContaDebito().getIdentificadorPlanoConta());
        setCampoConsultaContaDebito("identificadorPlanoConta");
        consultarPlanoContaDebito();
    }

    /**
     * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
     */
    public String inicializarConsultar() {         removerObjetoMemoria(this);
        setListaConsulta(new ArrayList(0));
        setMensagemID("msg_entre_prmconsulta");
        return "consultar";
    }

    /**
     * Operação que libera todos os recursos (atributos, listas, objetos) do backing bean.
     * Garantindo uma melhor atuação do Garbage Coletor do Java. A mesma é automaticamente
     * quando realiza o logout.
     */
    protected void limparRecursosMemoria() {
        super.limparRecursosMemoria();
        contabilVO = null;



    }

    public List getListaSelectItemFornecedor() {
        if (listaSelectItemFornecedor == null) {
            listaSelectItemFornecedor = new ArrayList(0);
        }
        return (listaSelectItemFornecedor);
    }

    public void setListaSelectItemFornecedor(List listaSelectItemFornecedor) {
        this.listaSelectItemFornecedor = listaSelectItemFornecedor;
    }

    public List getListaSelectItemPessoa() {
        if (listaSelectItemPessoa == null) {
            listaSelectItemPessoa = new ArrayList(0);
        }
        return (listaSelectItemPessoa);
    }

    public void setListaSelectItemPessoa(List listaSelectItemPessoa) {
        this.listaSelectItemPessoa = listaSelectItemPessoa;
    }

    public List getListaSelectItemConta() {
        if (listaSelectItemConta == null) {
            listaSelectItemConta = new ArrayList(0);
        }
        return (listaSelectItemConta);
    }

    public void setListaSelectItemConta(List listaSelectItemConta) {
        this.listaSelectItemConta = listaSelectItemConta;
    }

    public ContabilVO getContabilVO() {
        return contabilVO;
    }

    public void setContabilVO(ContabilVO contabilVO) {
        this.contabilVO = contabilVO;
    }

    public String getCampoConsultaContaCredito() {
        return campoConsultaContaCredito;
    }

    public void setCampoConsultaContaCredito(String campoConsultaContaCredito) {
        this.campoConsultaContaCredito = campoConsultaContaCredito;
    }

    public String getCampoConsultaContaDebito() {
        return campoConsultaContaDebito;
    }

    public void setCampoConsultaContaDebito(String campoConsultaContaDebito) {
        this.campoConsultaContaDebito = campoConsultaContaDebito;
    }

    public List getListaConsultaContaCredito() {
        return listaConsultaContaCredito;
    }

    public void setListaConsultaContaCredito(List listaConsultaContaCredito) {
        this.listaConsultaContaCredito = listaConsultaContaCredito;
    }

    public List getListaConsultaContaDebito() {
        return listaConsultaContaDebito;
    }

    public void setListaConsultaContaDebito(List listaConsultaContaDebito) {
        this.listaConsultaContaDebito = listaConsultaContaDebito;
    }

    public String getValorConsultaContaCredito() {
        return valorConsultaContaCredito;
    }

    public void setValorConsultaContaCredito(String valorConsultaContaCredito) {
        this.valorConsultaContaCredito = valorConsultaContaCredito;
    }

    public String getValorConsultaContaDebito() {
        return valorConsultaContaDebito;
    }

    public void setValorConsultaContaDebito(String valorConsultaContaDebito) {
        this.valorConsultaContaDebito = valorConsultaContaDebito;
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

    public String getCampoConsultaPessoa() {
        return campoConsultaPessoa;
    }

    public void setCampoConsultaPessoa(String campoConsultaPessoa) {
        this.campoConsultaPessoa = campoConsultaPessoa;
    }

    public List getListaConsultaPessoa() {
        return listaConsultaPessoa;
    }

    public void setListaConsultaPessoa(List listaConsultaPessoa) {
        this.listaConsultaPessoa = listaConsultaPessoa;
    }

    public String getValorConsultaPessoa() {
        return valorConsultaPessoa;
    }

    public void setValorConsultaPessoa(String valorConsultaPessoa) {
        this.valorConsultaPessoa = valorConsultaPessoa;
    }

    public PlanoContaVO getContaCredito() {
        return contaCredito;
    }

    public void setContaCredito(PlanoContaVO contaCredito) {
        this.contaCredito = contaCredito;
    }

    public PlanoContaVO getContaDebito() {
        return contaDebito;
    }

    public void setContaDebito(PlanoContaVO contaDebito) {
        this.contaDebito = contaDebito;
    }

    public String getCampoConsultaTipoEventoContabil() {
        return campoConsultaTipoEventoContabil;
    }

    public void setCampoConsultaTipoEventoContabil(String campoConsultaTipoEventoContabil) {
        this.campoConsultaTipoEventoContabil = campoConsultaTipoEventoContabil;
    }

    public List getListaConsultaTipoEventoContabil() {
        return listaConsultaTipoEventoContabil;
    }

    public void setListaConsultaTipoEventoContabil(List listaConsultaTipoEventoContabil) {
        this.listaConsultaTipoEventoContabil = listaConsultaTipoEventoContabil;
    }

    public String getValorConsultaTipoEventoContabil() {
        return valorConsultaTipoEventoContabil;
    }

    public void setValorConsultaTipoEventoContabil(String valorConsultaTipoEventoContabil) {
        this.valorConsultaTipoEventoContabil = valorConsultaTipoEventoContabil;
    }

    public String getCampoApresentarTipoEventoContabil() {
        return campoApresentarTipoEventoContabil;
    }

    public void setCampoApresentarTipoEventoContabil(String campoApresentarTipoEventoContabil) {
        this.campoApresentarTipoEventoContabil = campoApresentarTipoEventoContabil;
    }

    public List getListaTipoLancamentoContabil() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("pagamento", "Pagamento"));
        itens.add(new SelectItem("recebimento", "Recebimento"));
        return itens;
    }

    public List getListaTipoDestinoLancamento() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("banco", "Banco"));
        itens.add(new SelectItem("fornecedor", "Fornecedor"));
        itens.add(new SelectItem("funcionario", "Funcionario"));
        return itens;
    }

    public Boolean getTipoLancamentoContabilPagamento() {
        if (getTipoLancamentoContabil().equals("pagamento")) {
            return true;
        }
        return false;
    }

    public Boolean getTipoDestinoLancamentoBanco() {
        if (getTipoDestinoLancamento().equals("banco") && getTipoLancamentoContabil().equals("pagamento")) {
            return true;
        }
        return false;
    }

    public Boolean getTipoDestinoLancamentoFornecedor() {
        if (getTipoDestinoLancamento().equals("fornecedor") && getTipoLancamentoContabil().equals("pagamento")) {
            return true;
        }
        return false;
    }

    public Boolean getTipoDestinoLancamentoFuncionario() {
        if (getTipoDestinoLancamento().equals("funcionario") && getTipoLancamentoContabil().equals("pagamento")) {
            return true;
        }
        return false;
    }

    public String getTipoLancamentoContabil() {
        return tipoLancamentoContabil;
    }

    public void setTipoLancamentoContabil(String tipoLancamentoContabil) {
        this.tipoLancamentoContabil = tipoLancamentoContabil;
    }

    public Double getTotal() {
        if (getTipoLancamentoContabil().equals("pagamento")) {
            return getContabilVO().getContaPagar().getValorPago();
        } else {
            return getContabilVO().getContaReceber().getValor();
        }
    }

    public String getMatricula() {
        if (matricula == null) {
            matricula = "";
        }
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getCampoConsultaBanco() {
        return campoConsultaBanco;
    }

    public void setCampoConsultaBanco(String campoConsultaBanco) {
        this.campoConsultaBanco = campoConsultaBanco;
    }

    public List getListaConsultaBanco() {
        return listaConsultaBanco;
    }

    public void setListaConsultaBanco(List listaConsultaBanco) {
        this.listaConsultaBanco = listaConsultaBanco;
    }

    public String getValorConsultaBanco() {
        return valorConsultaBanco;
    }

    public void setValorConsultaBanco(String valorConsultaBanco) {
        this.valorConsultaBanco = valorConsultaBanco;
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

    public String getMatriculaFuncionario() {
        return matriculaFuncionario;
    }

    public void setMatriculaFuncionario(String matriculaFuncionario) {
        this.matriculaFuncionario = matriculaFuncionario;
    }

    public String getTipoDestinoLancamento() {
        if (tipoDestinoLancamento == null) {
            tipoDestinoLancamento = "";
        }
        return tipoDestinoLancamento;
    }

    public void setTipoDestinoLancamento(String tipoDestinoLancamento) {
        this.tipoDestinoLancamento = tipoDestinoLancamento;
    }

    public String getCampoConsultaContaReceber() {
        return campoConsultaContaReceber;
    }

    public void setCampoConsultaContaReceber(String campoConsultaContaReceber) {
        this.campoConsultaContaReceber = campoConsultaContaReceber;
    }

    public List getListaConsultaContaReceber() {
        return listaConsultaContaReceber;
    }

    public void setListaConsultaContaReceber(List listaConsultaContaReceber) {
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

    public String getCampoApresentarContaPagar() {
        if (campoApresentarContaPagar == null) {
            campoApresentarContaPagar = "";
        }
        return campoApresentarContaPagar;
    }

    public void setCampoApresentarContaPagar(String campoApresentarContaPagar) {
        this.campoApresentarContaPagar = campoApresentarContaPagar;
    }

    public String getCampoApresentarContaReceber() {
        if (campoApresentarContaReceber == null) {
            campoApresentarContaReceber = "";
        }
        return campoApresentarContaReceber;
    }

    public void setCampoApresentarContaReceber(String campoApresentarContaReceber) {
        this.campoApresentarContaReceber = campoApresentarContaReceber;
    }

    public String getCampoConsultaContaContraPartida() {
        if (campoConsultaContaContraPartida == null) {
            campoConsultaContaContraPartida = "";
        }
        return campoConsultaContaContraPartida;
    }

    public void setCampoConsultaContaContraPartida(String campoConsultaContaContraPartida) {
        this.campoConsultaContaContraPartida = campoConsultaContaContraPartida;
    }

    public List getListaConsultaContaContraPartida() {
        if (listaConsultaContaContraPartida == null) {
            listaConsultaContaContraPartida = new ArrayList(0);
        }
        return listaConsultaContaContraPartida;
    }

    public void setListaConsultaContaContraPartida(List listaConsultaContaContraPartida) {
        this.listaConsultaContaContraPartida = listaConsultaContaContraPartida;
    }

    public String getValorConsultaContaContraPartida() {
        if (valorConsultaContaContraPartida == null) {
            valorConsultaContaContraPartida = "";
        }
        return valorConsultaContaContraPartida;
    }

    public void setValorConsultaContaContraPartida(String valorConsultaContaContraPartida) {
        this.valorConsultaContaContraPartida = valorConsultaContaContraPartida;
    }

    public PlanoContaVO getContaContraPartida() {
        if (contaContraPartida == null) {
            contaContraPartida = new PlanoContaVO();
        }
        return contaContraPartida;
    }

    public void setContaContraPartida(PlanoContaVO contaContraPartida) {
        this.contaContraPartida = contaContraPartida;
    }
}
