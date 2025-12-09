package controle.financeiro;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas recebimentoForm.jsp
 * recebimentoCons.jsp) com as funcionalidades da classe <code>Recebimento</code>. Implemtação da camada controle
 * (Backing Bean).
 * 
 * @see SuperControle
 * @see Recebimento
 * @see RecebimentoVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.faces.component.UICommand;
import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.financeiro.CentroReceitaVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.RecebimentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.financeiro.Recebimento;

@Controller("RecebimentoControle")
@Scope("request")
@Lazy
public class RecebimentoControle extends SuperControle implements Serializable {

    private RecebimentoVO recebimentoVO;
    protected List listaSelectItemContaCorrente;
    protected List listaSelectItemUnidadeEnsino;
    protected List listaSelectItemCentroReceita;
    protected List listaSelectItemMatriculaAluno;
    protected List listaSelectItemFuncionario;
    protected List listaSelectItemContaReceber;
    protected List listaConsultaFuncionario;
    protected String valorConsultaFuncionario;
    protected String campoConsultaFuncionario;
    protected List listaConsultaAluno;
    protected String valorConsultaAluno;
    protected String campoConsultaAluno;
    protected List listaConsultaCandidato;
    protected String valorConsultaCandidato;
    protected String campoConsultaCandidato;
    protected List listaConsultaRequisitante;
    protected String valorConsultaRequisitante;
    protected String campoConsultaRequisitante;
    protected List listaConsultaCentroReceita;
    protected String valorConsultaCentroReceita;
    protected String campoConsultaCentroReceita;
    private ConfiguracaoFinanceiroVO configuracaoFinanceiroVO;

    public RecebimentoControle() throws Exception {
        //obterUsuarioLogado();
        setControleConsulta(new ControleConsulta());
        novo();
        recebimentoContaReceber();
        setMensagemID("msg_entre_prmconsulta");
    }

    /**
     * Rotina responsável por disponibilizar um novo objeto da classe <code>Recebimento</code> para edição pelo usuário
     * da aplicação.
     */
    public String novo() throws Exception {
        registrarAtividadeUsuario(getUsuarioLogado(), "RecebimentoControle", "Novo Recebimento", "Novo");
        removerObjetoMemoria(this);
        setRecebimentoVO(new RecebimentoVO());
        inicializarListasSelectItemTodosComboBox();
        inicializarConfiguracaoFinanceiro();
        setMensagemID("msg_entre_dados");
        return "editar";
    }

    public void inicializarConfiguracaoFinanceiro() throws Exception {
//		setConfiguracaoFinanceiroVO(getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), null));
        setConfiguracaoFinanceiroVO(getConfiguracaoFinanceiroPadraoSistema());
    }

    public void recebimentoContaReceber() throws Exception {
        ContaReceberControle contaReceberControle = (ContaReceberControle) context().getExternalContext().getSessionMap().get("ContaReceberControle");
        if (contaReceberControle != null) {
            this.getRecebimentoVO().getContaReceber().setCodigo(contaReceberControle.getContaReceberVO().getCodigo());
            montarDadosContaReceberRecebimento();
            apresentarPrimeiro = new UICommand();
            apresentarAnterior = new UICommand();
            apresentarPosterior = new UICommand();
            apresentarUltimo = new UICommand();
        }
    }

    public void montarDadosContaReceberRecebimento() throws Exception {
        ContaReceberVO obj = getFacadeFactory().getContaReceberFacade().consultarPorChavePrimaria(this.getRecebimentoVO().getContaReceber().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
        getRecebimentoVO().montarContaReceberRecebimento(obj, getConfiguracaoFinanceiroVO());
    }

    /**
     * Rotina responsável por disponibilizar os dados de um objeto da classe <code>Recebimento</code> para alteração. O
     * objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa
     * disponibilizá-lo para edição.
     */
    public String editar() throws Exception {
        registrarAtividadeUsuario(getUsuarioLogado(), "RecebimentoControle", "Inicializando Editar Recebimento", "Editando");
        RecebimentoVO obj = (RecebimentoVO) context().getExternalContext().getRequestMap().get("recebimento");
        obj = getFacadeFactory().getRecebimentoFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
        obj.setNovoObj(Boolean.FALSE);
        setRecebimentoVO(obj);
        inicializarListasSelectItemTodosComboBox();
        registrarAtividadeUsuario(getUsuarioLogado(), "RecebimentoControle", "Finalizando Editar Recebimento", "Editando");
        setMensagemID("msg_dados_editar");
        return "editar";
    }

    /**
     * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>Recebimento</code>. Caso
     * o objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é
     * acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado
     * para o usuário juntamente com uma mensagem de erro.
     */
    public String gravar() {
        try {
            if (recebimentoVO.getContaReceber().getSituacao().equals("AR")) {

                if (recebimentoVO.isNovoObj().booleanValue()) {
                    registrarAtividadeUsuario(getUsuarioLogado(), "RecebimentoControle", "Inicializando Incluir Recebimento", "Incluindo");
                    getFacadeFactory().getRecebimentoFacade().incluir(recebimentoVO, getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema());
                    registrarAtividadeUsuario(getUsuarioLogado(), "RecebimentoControle", "Finalizando Incluir Recebimento", "Incluindo");
                } else {
                    throw new Exception("Este recebimento não pode ser alterado.");
                }
                setMensagemID("msg_dados_gravados");
            } else {
                setMensagemDetalhada("msg_erro", "A conta a receber já foi paga e não pode ser alterada.");
            }

            return "editar";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "editar";
        }
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP RecebimentoCons.jsp. Define o tipo de consulta a
     * ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como resultado,
     * disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    public String consultar() {
        try {
            super.consultar();
            List objs = new ArrayList(0);
            if (!getControleConsulta().getValorConsulta().equals("")) {
                if (getControleConsulta().getCampoConsulta().equals("codigo")) {
                    if (getControleConsulta().getValorConsulta().equals("")) {
                        getControleConsulta().setValorConsulta("0");
                    }
                    int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
                    registrarAtividadeUsuario(getUsuarioLogado(), "RecebimentoControle", "Inicializando Consultar Recebimento", "Consultando");
                    objs = getFacadeFactory().getRecebimentoFacade().consultarPorCodigo(new Integer(valorInt), this.getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
                    registrarAtividadeUsuario(getUsuarioLogado(), "RecebimentoControle", "Finalizando Consultar Recebimento", "Consultando");
                }
                if (getControleConsulta().getCampoConsulta().equals("codigobarra")) {
                    registrarAtividadeUsuario(getUsuarioLogado(), "RecebimentoControle", "Inicializando Consultar Recebimento", "Consultando");
                    objs = getFacadeFactory().getRecebimentoFacade().consultarPorCodigoBarra(getControleConsulta().getValorConsulta(), this.getUnidadeEnsinoLogado().getCodigo(), true,
                            Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
                    registrarAtividadeUsuario(getUsuarioLogado(), "RecebimentoControle", "Finalizando Consultar Recebimento", "Consultando");
                }
                if (getControleConsulta().getCampoConsulta().equals("candidato")) {
                    registrarAtividadeUsuario(getUsuarioLogado(), "RecebimentoControle", "Inicializando Consultar Recebimento", "Consultando");
                    objs = getFacadeFactory().getRecebimentoFacade().consultarPorCandidato(getControleConsulta().getValorConsulta(), this.getUnidadeEnsinoLogado().getCodigo(), true,
                            Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
                    registrarAtividadeUsuario(getUsuarioLogado(), "RecebimentoControle", "Finalizando Consultar Recebimento", "Consultando");
                }
                if (getControleConsulta().getCampoConsulta().equals("aluno")) {
                    registrarAtividadeUsuario(getUsuarioLogado(), "RecebimentoControle", "Inicializando Consultar Recebimento", "Consultando");
                    objs = getFacadeFactory().getRecebimentoFacade().consultarPorAluno(getControleConsulta().getValorConsulta(), this.getUnidadeEnsinoLogado().getCodigo(), true,
                            Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
                    registrarAtividadeUsuario(getUsuarioLogado(), "RecebimentoControle", "Finalizando Consultar Recebimento", "Consultando");
                }
                if (getControleConsulta().getCampoConsulta().equals("funcionario")) {
                    registrarAtividadeUsuario(getUsuarioLogado(), "RecebimentoControle", "Inicializando Consultar Recebimento", "Consultando");
                    objs = getFacadeFactory().getRecebimentoFacade().consultarPorFuncionario(getControleConsulta().getValorConsulta(), this.getUnidadeEnsinoLogado().getCodigo(), true,
                            Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
                    registrarAtividadeUsuario(getUsuarioLogado(), "RecebimentoControle", "Finalizando Consultar Recebimento", "Consultando");
                }
                if (getControleConsulta().getCampoConsulta().equals("nrDocumento")) {
                    registrarAtividadeUsuario(getUsuarioLogado(), "RecebimentoControle", "Inicializando Consultar Recebimento", "Consultando");
                    objs = getFacadeFactory().getRecebimentoFacade().consultarPorNrDocumento(getControleConsulta().getValorConsulta(), this.getUnidadeEnsinoLogado().getCodigo(), true,
                            Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
                    registrarAtividadeUsuario(getUsuarioLogado(), "RecebimentoControle", "Finalizando Consultar Recebimento", "Consultando");
                }
                if (getControleConsulta().getCampoConsulta().equals("identificadorCentroReceitaCentroReceita")) {
                    registrarAtividadeUsuario(getUsuarioLogado(), "RecebimentoControle", "Inicializando Consultar Recebimento", "Consultando");
                    objs = getFacadeFactory().getRecebimentoFacade().consultarPorIdentificadorCentroReceitaCentroReceita(getControleConsulta().getValorConsulta(),
                            this.getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
                    registrarAtividadeUsuario(getUsuarioLogado(), "RecebimentoControle", "Finalizando Consultar Recebimento", "Consultando");
                }
            } else {
                throw new ConsistirException("Por Favor informe o valor para consulta!");
            }
            setListaConsulta(objs);
            setMensagemID("msg_dados_consultados");
            return "consultar";
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "consultar";
        }
    }

    /**
     * Operação responsável por processar a exclusão um objeto da classe <code>RecebimentoVO</code> Após a exclusão ela
     * automaticamente aciona a rotina para uma nova inclusão.
     */
    public String excluir() {
        try {
            registrarAtividadeUsuario(getUsuarioLogado(), "RecebimentoControle", "Inicializando Excluir Recebimento", "Excluindo");
            getFacadeFactory().getRecebimentoFacade().excluir(recebimentoVO, getUsuarioLogado());
            setRecebimentoVO(new RecebimentoVO());
            registrarAtividadeUsuario(getUsuarioLogado(), "RecebimentoControle", "Finalizando Excluir Recebimento", "Excluindo");
            setMensagemID("msg_dados_excluidos");
            return "editar";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "editar";
        }
    }

    public void fechar() {
        this.getRecebimentoVO().setRenegociacaoContaReceberVOs(new ArrayList(0));
        getRecebimentoVO().calcularValorFinal();
    }

    public void consultarAlunoPorMatricula() {
        try {
            if (!getRecebimentoVO().getMatriculaAluno().getMatricula().equals("")) {
                MatriculaVO objs = getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(getRecebimentoVO().getMatriculaAluno().getMatricula(),
                        this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
                if (!objs.getMatricula().equals("")) {
                    this.getRecebimentoVO().setMatriculaAluno(objs);
                    setMensagemID("msg_dados_consultados");
                    return;
                }
                getRecebimentoVO().getMatriculaAluno().setMatricula("");
                getRecebimentoVO().getMatriculaAluno().getAluno().setNome("");
                setMensagemID("msg_erro_dadosnaoencontrados");
            }
        } catch (Exception e) {
            getRecebimentoVO().getMatriculaAluno().setMatricula("");
            getRecebimentoVO().getMatriculaAluno().getAluno().setNome("");
            setMensagemID("msg_erro_dadosnaoencontrados");
        }
    }

    public void consultarFuncionarioPorCodigo() {
        try {
            if (!this.getRecebimentoVO().getFuncionario().getMatricula().equals("") && getRecebimentoVO().getTipoPessoa().equals("FU")) {
                this.getRecebimentoVO().setFuncionario(
                        getFacadeFactory().getFuncionarioFacade().consultarPorRequisitanteMatricula(getRecebimentoVO().getFuncionario().getMatricula(), this.getUnidadeEnsinoLogado().getCodigo(),
                        false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
                setMensagemID("msg_dados_consultados");
            }
        } catch (Exception e) {
            getRecebimentoVO().getFuncionario().setMatricula("");
            getRecebimentoVO().getFuncionario().getPessoa().setNome("");
            getRecebimentoVO().getFuncionario().setCodigo(0);
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

    public void consultarAluno() {
        try {

            List objs = new ArrayList(0);
            if (getValorConsultaAluno().equals("")) {
                setMensagemID("msg_entre_prmconsulta");
                return;
            }
            if (getCampoConsultaAluno().equals("matricula")) {
                objs.add(getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), true,
                        Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
            }
            if (getCampoConsultaAluno().equals("nomePessoa")) {
                objs = getFacadeFactory().getMatriculaFacade().consultarPorNomePessoa(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            }
            if (getCampoConsultaAluno().equals("nomeCurso")) {
                objs = getFacadeFactory().getMatriculaFacade().consultarPorNomeCurso(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            }
            if (getCampoConsultaAluno().equals("data")) {
                Date valorData = Uteis.getDate(getValorConsultaAluno());
                objs = getFacadeFactory().getMatriculaFacade().consultarPorData(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59),
                        this.getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            }
            if (getCampoConsultaAluno().equals("situacao")) {
                objs = getFacadeFactory().getMatriculaFacade().consultarPorSituacao(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            }
            if (getCampoConsultaAluno().equals("nomeResponsavel")) {
                objs = getFacadeFactory().getMatriculaFacade().consultarPorNomePessoa(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            }

            setListaConsultaAluno(objs);
            setMensagemID("msg_dados_consultados");

        } catch (Exception e) {
            setListaConsultaAluno(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());

        }
    }

    public void consultarCandidatoPorCPF() {
        try {
            if (!this.getRecebimentoVO().getCandidato().getCPF().equals("")) {
                PessoaVO pessoa = getFacadeFactory().getPessoaFacade().consultarPorCPFUnico(this.getRecebimentoVO().getCandidato().getCPF(), 0, "CA", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
                if (pessoa.getCodigo().intValue() != 0) {
                    this.getRecebimentoVO().setCandidato(pessoa);
                    setMensagemID("msg_dados_consultados");
                    return;
                }
                throw new Exception();
            }
        } catch (Exception e) {
            getRecebimentoVO().getCandidato().setCPF("");
            getRecebimentoVO().getCandidato().setNome("");
            getRecebimentoVO().getCandidato().setCodigo(0);
            setMensagemID("msg_erro_dadosnaoencontrados");
        }
    }

    public void consultarRequerentePorCPF() {
        try {
            if (!this.getRecebimentoVO().getPessoa().getCPF().equals("")) {
                PessoaVO pessoa = getFacadeFactory().getPessoaFacade().consultarPorCPFUnico(this.getRecebimentoVO().getPessoa().getCPF(), 0, "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
                if (pessoa.getCodigo().intValue() != 0) {
                    this.getRecebimentoVO().setPessoa(pessoa);
                    setMensagemID("msg_dados_consultados");
                    return;
                }
                throw new Exception();
            }
        } catch (Exception e) {
            getRecebimentoVO().getPessoa().setCodigo(0);
            getRecebimentoVO().getPessoa().setNome("");
            getRecebimentoVO().getPessoa().setCPF("");
            setMensagemID("msg_erro_dadosnaoencontrados");
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

    public List getTipoConsultaComboCentroReceita() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("descricao", "Descrição"));
        itens.add(new SelectItem("identificadorCentroReceita", "Identificador Centro Receita"));
        itens.add(new SelectItem("nomeDepartamento", "Departamento"));
        return itens;
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

    public void selecionarRequisitante() {
        PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("requerente");
        this.getRecebimentoVO().setPessoa(obj);

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

    public List getTipoConsultaComboCandidato() {
        List itens = new ArrayList(0);

        itens.add(new SelectItem("nome", "Nome"));

        itens.add(new SelectItem("CPF", "CPF"));
        itens.add(new SelectItem("RG", "RG"));
        return itens;
    }

    public void selecionarFuncionario() {
        FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionario");
        this.getRecebimentoVO().setFuncionario(obj);
    }

    public void selecionarAluno() {
        MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("aluno");
        this.getRecebimentoVO().setMatriculaAluno(obj);
    }

    public void selecionarCandidato() {
        PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("candidato");
        this.getRecebimentoVO().setCandidato(obj);
    }

    public void selecionarCentroReceita() {
        CentroReceitaVO obj = (CentroReceitaVO) context().getExternalContext().getRequestMap().get("centroReceita");
        this.getRecebimentoVO().setCentroReceita(obj);
    }

    public void gerarParcelas() throws ConsistirException, Exception {
        try {
            Recebimento.validarPermisaoRenegociarContaReceber(getUsuarioLogadoClone());
            getRecebimentoVO().gerarParcelas();
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

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

    public void gravarContaReceber() {
        try {
            if (recebimentoVO.getContaReceber().getSituacao().equals("AR")) {
                recebimentoVO.setRenegociar(Boolean.TRUE);
                if (recebimentoVO.isNovoObj().booleanValue()) {
                    getFacadeFactory().getRecebimentoFacade().incluir(recebimentoVO, getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema());
                } else {
                    throw new Exception("Este recebimento não pode ser alterado.");
                }
                setMensagemID("msg_dados_gravados");
            } else {
                setMensagemDetalhada("msg_erro", "A conta a receber já foi paga e não pode ser alterada.");
            }
        } catch (Exception e) {
            recebimentoVO.setRenegociar(Boolean.FALSE);
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
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
     * relativo ao atributo <code>ContaReceber</code>.
     */
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

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
     * relativo ao atributo <code>ContaCorrente</code>.
     */
    public void montarListaSelectItemContaCorrente(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarContaCorrentePorNumero(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                ContaCorrenteVO obj = (ContaCorrenteVO) i.next();
                if (obj.getContaCaixa()) {
                    objs.add(new SelectItem(obj.getCodigo(), "CC:" + obj.getNumero() + "-" + obj.getDigito()));
                } else {
                    objs.add(new SelectItem(obj.getCodigo(), "Banco:" + obj.getAgencia().getBanco().getNome() + " Ag:" + obj.getAgencia().getNumeroAgencia() + "-" + obj.getAgencia().getDigito() + " CC:"
                            + obj.getNumero() + "-" + obj.getDigito()));
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
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>ContaCorrente</code>. Buscando todos os
     * objetos correspondentes a entidade <code>ContaCorrente</code>. Esta rotina não recebe parâmetros para filtragem
     * de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemContaCorrente() {
        try {
            montarListaSelectItemContaCorrente("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
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
     * relativo ao atributo <code>UnidadeEnsino</code>.
     */
    public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            if (getUnidadeEnsinoLogado().getCodigo().intValue() != 0) {
                UnidadeEnsinoVO unidadeEnsino = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
                getListaSelectItemUnidadeEnsino().add(new SelectItem(unidadeEnsino.getCodigo(), unidadeEnsino.getNome()));
                getRecebimentoVO().getUnidadeEnsino().setCodigo(unidadeEnsino.getCodigo());
                unidadeEnsino = null;
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
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>UnidadeEnsino</code>. Buscando todos os
     * objetos correspondentes a entidade <code>UnidadeEnsino</code>. Esta rotina não recebe parâmetros para filtragem
     * de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
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
     * Método responsável por inicializar a lista de valores (<code>SelectItem</code>) para todos os ComboBox's.
     */
    public void inicializarListasSelectItemTodosComboBox() {
        montarListaSelectItemContaCorrente();
        montarListaSelectItemUnidadeEnsino();

    }

    /**
     * Rotina responsável por preencher a combo de consulta da telas.
     */
    public List getTipoConsultaCombo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("aluno", "Matrícula Aluno"));
        itens.add(new SelectItem("candidato", "CPF Aluno/Candidato"));
        itens.add(new SelectItem("funcionario", "Matrícula Funcionário"));
        itens.add(new SelectItem("nrDocumento", "Nr. Documento"));
        itens.add(new SelectItem("codigobarra", "Código de Barra"));
        itens.add(new SelectItem("identificadorCentroReceitaCentroReceita", "Centro de Receita"));
        return itens;
    }

    /**
     * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
     */
    public String inicializarConsultar() {
        removerObjetoMemoria(this);
        // setPaginaAtualDeTodas("0/0");
        setListaConsulta(new ArrayList(0));
        // definirVisibilidadeLinksNavegacao(0, 0);
        setMensagemID("msg_entre_prmconsulta");
        return "consultar";
    }

    public ConfiguracaoFinanceiroVO getConfiguracaoFinanceiroVO() {
        return configuracaoFinanceiroVO;
    }

    public void setConfiguracaoFinanceiroVO(ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) {
        this.configuracaoFinanceiroVO = configuracaoFinanceiroVO;
    }

    public List getListaSelectItemContaReceber() {
        return (listaSelectItemContaReceber);
    }

    public void setListaSelectItemContaReceber(List listaSelectItemContaReceber) {
        this.listaSelectItemContaReceber = listaSelectItemContaReceber;
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

    public String getCampoConsultaFuncionario() {
        return campoConsultaFuncionario;
    }

    public void setCampoConsultaFuncionario(String campoConsultaFuncionario) {
        this.campoConsultaFuncionario = campoConsultaFuncionario;
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

    public List getListaConsultaFuncionario() {
        return listaConsultaFuncionario;
    }

    public void setListaConsultaFuncionario(List listaConsultaFuncionario) {
        this.listaConsultaFuncionario = listaConsultaFuncionario;
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

    public String getValorConsultaFuncionario() {
        return valorConsultaFuncionario;
    }

    public void setValorConsultaFuncionario(String valorConsultaFuncionario) {
        this.valorConsultaFuncionario = valorConsultaFuncionario;
    }

    public List getListaSelectItemCentroReceita() {
        return (listaSelectItemCentroReceita);
    }

    public void setListaSelectItemCentroReceita(List listaSelectItemCentroReceita) {
        this.listaSelectItemCentroReceita = listaSelectItemCentroReceita;
    }

    public List getListaSelectItemContaCorrente() {
        return (listaSelectItemContaCorrente);
    }

    public void setListaSelectItemContaCorrente(List listaSelectItemContaCorrente) {
        this.listaSelectItemContaCorrente = listaSelectItemContaCorrente;
    }

    public RecebimentoVO getRecebimentoVO() {
        return recebimentoVO;
    }

    public void setRecebimentoVO(RecebimentoVO recebimentoVO) {
        this.recebimentoVO = recebimentoVO;
    }

    public List getListaSelectItemUnidadeEnsino() {
        return listaSelectItemUnidadeEnsino;
    }

    public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
        this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
    }

    public String getCampoConsultaRequisitante() {
        return campoConsultaRequisitante;
    }

    public void setCampoConsultaRequisitante(String campoConsultaRequisitante) {
        this.campoConsultaRequisitante = campoConsultaRequisitante;
    }

    public List getListaConsultaRequisitante() {
        return listaConsultaRequisitante;
    }

    public void setListaConsultaRequisitante(List listaConsultaRequisitante) {
        this.listaConsultaRequisitante = listaConsultaRequisitante;
    }

    public String getValorConsultaRequisitante() {
        return valorConsultaRequisitante;
    }

    public void setValorConsultaRequisitante(String valorConsultaRequisitante) {
        this.valorConsultaRequisitante = valorConsultaRequisitante;
    }

    @Override
    protected void limparRecursosMemoria() {
        super.limparRecursosMemoria();
        recebimentoVO = null;
        Uteis.liberarListaMemoria(listaSelectItemContaCorrente);
        Uteis.liberarListaMemoria(listaSelectItemCentroReceita);
        Uteis.liberarListaMemoria(listaSelectItemMatriculaAluno);
        Uteis.liberarListaMemoria(listaSelectItemFuncionario);
        Uteis.liberarListaMemoria(listaSelectItemContaReceber);
        Uteis.liberarListaMemoria(listaSelectItemUnidadeEnsino);
        configuracaoFinanceiroVO = null;
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
        valorConsultaCentroReceita = null;
        campoConsultaCentroReceita = null;
    }
}
