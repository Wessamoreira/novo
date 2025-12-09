package controle.financeiro;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das
 * páginas contaPagarForm.jsp contaPagarCons.jsp) com as funcionalidades da
 * classe <code>ContaPagar</code>. Implemtação da camada controle (Backing
 * Bean).
 * 
 * @see SuperControle
 * @see ContaPagar
 * @see GrupoContaPagarVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import controle.compras.FornecedorControle;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.financeiro.BancoVO;
import negocio.comuns.financeiro.CategoriaDespesaVO;
import negocio.comuns.financeiro.CentroCustoVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ContaPagarVO;
import negocio.comuns.financeiro.GrupoContaPagarVO;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.controle.financeiro.ExtratoContaPagarRelControle;

@Controller("GrupoContaPagarControle")
@Scope("viewScope")
@Lazy
public class GrupoContaPagarControle extends SuperControle implements Serializable {

    private GrupoContaPagarVO grupoContaPagarVO;
    protected String valorConsultaAluno;
    protected String campoConsultaAluno;
    protected List listaConsultaAluno;
    protected List listaConsultaCentroDespesa;
    protected String valorConsultaCentroDespesa;
    protected String campoConsultaCentroDespesa;
    protected List listaConsultaFornecedor;
    protected String valorConsultaFornecedor;
    protected String campoConsultaFornecedor;
    protected List listaSelectItemContaCorrente;
    protected List listaSelectItemUnidadeEnsino;
    protected List listaSelectItemTipoDesconto;
    private String valorConsultaFuncionario;
    private String campoConsultaFuncionario;
    private List listaConsultaFuncionario;
    private String valorConsultaFuncionarioCentroCusto;
    protected String valorConsultaSituacaoFinanceiraDaConta;
    private String campoConsultaFuncionarioCentroCusto;
    private List listaConsultaFuncionarioCentroCusto;
    private CentroCustoVO centroCusto;
    protected List listaSelectItemUnidadeEnsinoFornecedor;
    protected List listaSelectItemDepartamentoFornecedor;
    protected List listaSelectItemFuncionarioFornecedor;
    private List listaSelectItemCentroCusto;
    private String campoConsultaTurma;
    private String valorConsultaTurma;
    private List<TurmaVO> listaConsultaTurma;
    private String campoConsultaCurso;
    private String valorConsultaCurso;
    private List<CursoVO> listaConsultaCurso;
    private String campoConsultaCursoTurno;
    private String valorConsultaCursoTurno;
    private List<UnidadeEnsinoCursoVO> listaConsultaCursoTurno;
    private ContaPagarVO contaPagarVO;
    private String filtroTipoData;
    private String caminhoRelatorioExtratoContaPagar;
    private Boolean gerarRelatorio;
    protected List listaSelectItemBanco;
    private String matriculaAluno;

    public GrupoContaPagarControle() throws Exception {
        setControleConsulta(new ControleConsulta());
        getControleConsulta().setCampoConsulta("favorecido");
        getControleConsulta().setDataIni(Uteis.getDataPrimeiroDiaMes(new Date()));
        getControleConsulta().setDataFim(Uteis.getDataUltimoDiaMes(new Date()));
        setMensagemID("msg_entre_prmconsulta");
    }

    /**
     * Rotina responsável por disponibilizar um novo objeto da classe
     * <code>ContaPagar</code> para edição pelo usuário da aplicação.
     */
    public String novo() throws Exception {
        removerObjetoMemoria(this);
        setGrupoContaPagarVO(new GrupoContaPagarVO());
        setCentroCusto(new CentroCustoVO());
        setContaPagarVO(new ContaPagarVO());
        montarListaSelectItemUnidadeEnsino();
        montarListaSelectItemBanco();
        getGrupoContaPagarVO().setResponsavel(getUsuarioLogadoClone());
        setMensagemID("msg_entre_dados");
        return Uteis.getCaminhoRedirecionamentoNavegacao("grupoContaPagarForm.xhtml");
    }

    public String getFormNovoFornecedor() {
        try {
            executarMetodoControle(FornecedorControle.class.getSimpleName(), "novo", (Object[]) null);
            return "popup('../compras/fornecedorForm.xhtml', 'fornecedorForm' , 790, 595)";
        } catch (Exception e) {
            setMensagemID("msg_erro");
            //System.out.println("MENSAGEM => " + e.getMessage());;
            return "";
        }
    }

    /**
     * Rotina responsável por disponibilizar os dados de um objeto da classe
     * <code>ContaPagar</code> para alteração. O objeto desta classe é
     * disponibilizado na session da página (request) para que o JSP
     * correspondente possa disponibilizá-lo para edição.
     */
    public String editar() throws Exception {
        GrupoContaPagarVO obj = (GrupoContaPagarVO) context().getExternalContext().getRequestMap().get("grupoContaPagarItens");
        obj = montarDadosParaEdicaoGrupoContaPagarVO(obj);
        obj.setNovoObj(Boolean.FALSE);
        setGrupoContaPagarVO(obj);
        inicializarListasSelectItemTodosComboBox();
        montarListaSelectItemUnidadeEnsino();
        montarSelectItemDepartamentoFornecedorEditar();
        calcularValorTotal();
        setMensagemID("msg_dados_editar");
        return Uteis.getCaminhoRedirecionamentoNavegacao("grupoContaPagarForm.xhtml");
    }

    /**
     * Método responsavel por carregar todos os dados do GrupoContaPagarVO para
     * edição.
     *
     * @param obj
     */
    public GrupoContaPagarVO montarDadosParaEdicaoGrupoContaPagarVO(GrupoContaPagarVO obj) {
        try {
            return getFacadeFactory().getGrupoContaPagarFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
        return new GrupoContaPagarVO();
    }

    /**
     * Rotina responsável por gravar no BD os dados editados de um novo objeto
     * da classe <code>ContaPagar</code>. Caso o objeto seja novo (ainda não
     * gravado no BD) é acionado a operação <code>incluir()</code>. Caso
     * contrário é acionado o <code>alterar()</code>. Se houver alguma
     * inconsistência o objeto não é gravado, sendo re-apresentado para o
     * usuário juntamente com uma mensagem de erro.
     */
    public String gravar() {
        try {
            if (grupoContaPagarVO.isNovoObj().booleanValue()) {
                getFacadeFactory().getGrupoContaPagarFacade().incluir(grupoContaPagarVO, getUsuarioLogado());
            } else {
                getFacadeFactory().getGrupoContaPagarFacade().alterar(grupoContaPagarVO, getUsuarioLogado());
            }
            setMensagemID("msg_dados_gravados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("grupoContaPagarForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("grupoContaPagarForm.xhtml");
        }
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP
     * ContaPagarCons.jsp. Define o tipo de consulta a ser executada, por meio
     * de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
     * resultado, disponibiliza um List com os objetos selecionados na sessao da
     * pagina.
     */
    @Override
    public String consultar() {
        try {
            super.consultar();
            List objs = new ArrayList(0);
            if (getControleConsulta().getCampoConsulta().equals("favorecido")) {
                objs = getFacadeFactory().getGrupoContaPagarFacade().consultarPorNomeFavorecidoResumido(getControleConsulta().getValorConsulta(), getControleConsulta().getDataIni(), getControleConsulta().getDataFim(), this.getUnidadeEnsinoLogado().getCodigo(), getFiltroTipoData(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("categoriaDespesa")) {
                objs = getFacadeFactory().getGrupoContaPagarFacade().consultarPorIdentificadorCentroDespesaCentroDespesaResumido(getControleConsulta().getValorConsulta(), getControleConsulta().getDataIni(), getControleConsulta().getDataFim(), this.getUnidadeEnsinoLogado().getCodigo(), getFiltroTipoData(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            setListaConsulta(objs);
            calcularValorTotal();
            setMensagemID("msg_dados_consultados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("grupoContaPagarCons.xhtml");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("grupoContaPagarCons.xhtml");
        }
    }

    /**
     * Operação responsável por processar a exclusão um objeto da classe
     * <code>GrupoContaPagarVO</code> Após a exclusão ela automaticamente aciona a
     * rotina para uma nova inclusão.
     */
    public String excluir() {
        try {
            getFacadeFactory().getGrupoContaPagarFacade().excluir(grupoContaPagarVO, getUsuarioLogado());
            setGrupoContaPagarVO(new GrupoContaPagarVO());
            setMensagemID("msg_dados_excluidos");
            return Uteis.getCaminhoRedirecionamentoNavegacao("grupoContaPagarForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("grupoContaPagarForm.xhtml");
        }
    }

    public void imprimirExtratoContaPagarPDF() {
        ExtratoContaPagarRelControle extratoContaPagarRelControle = null;
        try {
            extratoContaPagarRelControle = (ExtratoContaPagarRelControle) context().getExternalContext().getSessionMap().get(ExtratoContaPagarRelControle.class.getSimpleName());
            if (extratoContaPagarRelControle == null) {
                extratoContaPagarRelControle = new ExtratoContaPagarRelControle();
            }
            Ordenacao.ordenarLista(getGrupoContaPagarVO().getContaPagarVOs(), "dataVencimento");
            setCaminhoRelatorioExtratoContaPagar(extratoContaPagarRelControle.imprimirPDFGrupoContaPagar(getGrupoContaPagarVO()));
            setGerarRelatorio(true);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public String getDownloadRelatorio() {
        try {
            if (getGerarRelatorio()) {
                if (!getCaminhoRelatorioExtratoContaPagar().equals("")) {
                    setMensagemID("msg_relatorio_ok");
                    setGerarRelatorio(false);
                    return Uteis.getCaminhoDownloadRelatorio(true, getCaminhoRelatorioExtratoContaPagar());
                } else {
                    setMensagemID("msg_relatorio_sem_dados");
                    setGerarRelatorio(false);
                }
            }
            return "";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "";
        }
    }

    public void consultarCentroDespesa() {
        try {
            List objs = new ArrayList(0);
            if (getCampoConsultaCentroDespesa().equals("descricao")) {
                objs = getFacadeFactory().getCategoriaDespesaFacade().consultarPorDescricao(getValorConsultaCentroDespesa(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getCampoConsultaCentroDespesa().equals("identificadorCentroDespesa")) {
                objs = getFacadeFactory().getCategoriaDespesaFacade().consultarPorIdentificadorCategoriaDespesa(getValorConsultaCentroDespesa(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            setListaConsultaCentroDespesa(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaCentroDespesa(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public List getTipoConsultaComboAluno() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nomePessoa", "Aluno"));
        itens.add(new SelectItem("matricula", "Matrícula"));
        return itens;
    }

    public List getTipoConsultaComboCentroDespesa() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("descricao", "Descrição"));
        itens.add(new SelectItem("identificadorCentroDespesa", "Identificador Centro Despesa"));
        return itens;
    }

    public void calcularValorTotal() {
        getGrupoContaPagarVO().setValor(0.0);
        for (ContaPagarVO contaPagar : (List<ContaPagarVO>) getGrupoContaPagarVO().getContaPagarVOs()) {
            getGrupoContaPagarVO().setValor(getGrupoContaPagarVO().getValor() + contaPagar.getValor());
        }
    }

    public void consultarAluno() {
        try {
            List objs = new ArrayList(0);
            if (getValorConsultaAluno().equals("")) {
                throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
            }
            if (getCampoConsultaAluno().equals("matricula")) {
                objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatricula(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
            }
            if (getCampoConsultaAluno().equals("nomePessoa")) {
                objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
            }
            if (getCampoConsultaAluno().equals("nomeCurso")) {
                objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
            }

            
//            if (getCampoConsultaAluno().equals("matricula")) {
//                objs.add(getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false,
//                        Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
//            }
//            if (getCampoConsultaAluno().equals("nomePessoa")) {
//                objs = getFacadeFactory().getMatriculaFacade().consultarPorNomePessoa(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
//            }
//            if (getCampoConsultaAluno().equals("nomeCurso")) {
//                objs = getFacadeFactory().getMatriculaFacade().consultarPorNomeCurso(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
//            }
//            if (getCampoConsultaAluno().equals("data")) {
//                Date valorData = Uteis.getDate(getValorConsultaAluno());
//                objs = getFacadeFactory().getMatriculaFacade().consultarPorData(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59),
//                        this.getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
//            }
//            if (getCampoConsultaAluno().equals("situacao")) {
//                objs = getFacadeFactory().getMatriculaFacade().consultarPorSituacao(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
//            }
//            if (getCampoConsultaAluno().equals("nomeResponsavel")) {
//                objs = getFacadeFactory().getMatriculaFacade().consultarPorNomePessoa(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
//            }
            setListaConsultaAluno(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaAluno(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarAluno() {
        try{
        MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("alunoItens");
        setMatriculaAluno(obj.getMatricula());
        this.getGrupoContaPagarVO().setPessoa(obj.getAluno());
        getFacadeFactory().getGrupoContaPagarFacade().realizarVinculoContaReceberComResponsavelFinanceiro(getGrupoContaPagarVO(), getUsuarioLogado());
        }catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void limparMatriculaAluno() {
        setMatriculaAluno("");
    }

    public void selecionarFornecedor() {
        FornecedorVO obj = (FornecedorVO) context().getExternalContext().getRequestMap().get("fornecedorItens");
        this.getGrupoContaPagarVO().setFornecedor(obj);
    }

    public void selecionarCentroDespesa() {
        CategoriaDespesaVO obj = (CategoriaDespesaVO) context().getExternalContext().getRequestMap().get("centroDespesaItens");
        /*this.getContaPagarVO().setDepartamento(new DepartamentoVO());
        this.getContaPagarVO().setFuncionarioCentroCusto(new FuncionarioVO());
        this.getContaPagarVO().setTurma(null);
        this.getContaPagarVO().setCursoVO(null);
        this.getContaPagarVO().setTurnoVO(null);
        this.getContaPagarVO().setCentroDespesa(obj);*/
//        this.getContaPagarVO().setUnidadeEnsino(getGrupoContaPagarVO().getUnidadeEnsino());
        try {
            montarSelectItemDepartamentoFornecedor();
//            montarListaSelectItemUnidadeEnsino();
            setMensagemID("msg_entre_dados");
        } catch (Exception e) {
        }
    }

    public void consultarFornecedor() {
        try {
            List objs = new ArrayList(0);
            if (getCampoConsultaFornecedor().equals("nome")) {
                objs = getFacadeFactory().getFornecedorFacade().consultarPorNome(getValorConsultaFornecedor(), "AT", false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
            }
            if (getCampoConsultaFornecedor().equals("razaoSocial")) {
                objs = getFacadeFactory().getFornecedorFacade().consultarPorRazaoSocial(getValorConsultaFornecedor(), "AT", false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
            }
            if (getCampoConsultaFornecedor().equals("CNPJ")) {
                objs = getFacadeFactory().getFornecedorFacade().consultarPorCNPJ(getValorConsultaFornecedor(), "AT", false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
            }
            if (getCampoConsultaFornecedor().equals("CPF")) {
                objs = getFacadeFactory().getFornecedorFacade().consultarPorCPF(getValorConsultaFornecedor(), "AT", false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
            }
            setListaConsultaFornecedor(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaFornecedor(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public List getTipoConsultaComboFornecedor() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("razaoSocial", "Razão Social"));
        itens.add(new SelectItem("CNPJ", "CNPJ"));
        itens.add(new SelectItem("CPF", "CPF"));
        return itens;
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

    public void selecionarFuncionario() throws Exception {
        FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItens");
        obj = getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimaria(obj.getCodigo(), 0, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
        this.getGrupoContaPagarVO().setFuncionario(obj);
        this.getGrupoContaPagarVO().getPessoa().setCodigo(obj.getPessoa().getCodigo());
        montarSelectItemCentroCusto();
        Uteis.liberarListaMemoria(getListaConsultaFuncionario());
        campoConsultaFuncionario = null;
        valorConsultaFuncionario = null;
    }

    public List getListaSelectItemTipoSacado() throws Exception {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("AL", "Aluno"));
        itens.add(new SelectItem("BA", "Banco"));
        itens.add(new SelectItem("FO", "Fornecedor"));
        itens.add(new SelectItem("FU", "Funcionário"));
        itens.add(new SelectItem("PA", "Parceiro"));
        itens.add(new SelectItem("RF", "Responsável Financeiro"));
        return itens;
    }

    public List getListaSelectItemSituacaoFinanceira() throws Exception {
        List objs = new ArrayList(0);
        objs.add(new SelectItem(0, "Todos"));
        Hashtable listaSituacaoFinanceira = (Hashtable) Dominios.getSituacaoFinanceira();
        Enumeration keys = listaSituacaoFinanceira.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) listaSituacaoFinanceira.get(value);
            objs.add(new SelectItem(value, label));
        }
        SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
        Collections.sort((List) objs, ordenador);
        return objs;
    }

    public void alterarTipoSacado() {
        getGrupoContaPagarVO().setFuncionario(new FuncionarioVO());
        getGrupoContaPagarVO().setFornecedor(new FornecedorVO());
        /*getContaPagarVO().setDepartamento(new DepartamentoVO());
        getContaPagarVO().setTurma(new TurmaVO());
        getContaPagarVO().setCentroCusto("");*/
        setContaPagarVO(null);
    }

    public List getListaSelectItemCentroCusto() {
        return listaSelectItemCentroCusto;
    }

    public void montarSelectItemCentroCusto() {
        List listaCentroCusto = new ArrayList(0);
        List listaSelectItem = new ArrayList(0);
        if ("FU".equals(grupoContaPagarVO.getTipoSacado())) {
            listaCentroCusto = grupoContaPagarVO.getFuncionario().getListaCentroCusto();
        }
        listaSelectItem.add(new SelectItem(0, ""));
        for (CentroCustoVO centroCustoVO : (List<CentroCustoVO>) listaCentroCusto) {
            listaSelectItem.add(new SelectItem(centroCustoVO.getIdentificadorCentroCusto(), centroCustoVO.getIdentificadorCentroCusto() + " - " + centroCustoVO.getDescricaoCentroCusto2()));
        }
        setListaSelectItemCentroCusto(listaSelectItem);
    }

    public void montarSelectItemUnidadeEnsinoFornecedor() throws Exception {
        if (false) { // se for super usuario, trazer todas as ue
            List resultadoConsulta = consultarUnidadeEnsinoPorCodigo(0);
            Iterator i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
            }
            listaSelectItemUnidadeEnsinoFornecedor = objs;
        } else { // senao trazer apenas as ue do usuario logado
            if (getUsuarioLogado().getPessoa().getFuncionario()) {
                FuncionarioVO funcionario = getFacadeFactory().getFuncionarioFacade().consultarPorCodigoPessoa(getUsuarioLogado().getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
                if (funcionario != null) {
                    List objs = new ArrayList(0);
                    objs.add(new SelectItem(0, ""));
                    for (FuncionarioCargoVO funcionarioCargo : (List<FuncionarioCargoVO>) funcionario.getFuncionarioCargoVOs()) {
                        objs.add(new SelectItem(funcionarioCargo.getUnidade().getCodigo(), funcionarioCargo.getUnidade().getNome()));
                    }
                    listaSelectItemUnidadeEnsinoFornecedor = objs;
                }
            }
        }
    }

    public void montarSelectItemDepartamentoFornecedor() throws Exception {
        List objs = new ArrayList(0);
        objs.add(new SelectItem(0, ""));
        List<DepartamentoVO> listaDepartamento = null;
        if (!getContaPagarVO().getUnidadeEnsino().getCodigo().equals(0)) {
            listaDepartamento = getFacadeFactory().getDepartamentoFacade().consultarPorCodigoUnidadeEnsinoESemUE(getContaPagarVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            for (DepartamentoVO depto : listaDepartamento) {
                objs.add(new SelectItem(depto.getCodigo(), depto.getNome()));
            }
        }
        listaSelectItemDepartamentoFornecedor = objs;
		/*this.getContaPagarVO().setDepartamento(new DepartamentoVO());
		this.getContaPagarVO().setCursoVO(new CursoVO());
		this.getContaPagarVO().setTurnoVO(new TurnoVO());
		this.getContaPagarVO().setTurma(new TurmaVO());*/				
    }

    public void montarSelectItemDepartamentoFornecedorEditar() throws Exception {
        List objs = new ArrayList(0);
        objs.add(new SelectItem(0, ""));
        List<DepartamentoVO> listaDepartamento = null;
        if (!getContaPagarVO().getUnidadeEnsino().getCodigo().equals(0)) {
            listaDepartamento = getFacadeFactory().getDepartamentoFacade().consultarPorCodigoUnidadeEnsinoESemUE(getContaPagarVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            for (DepartamentoVO depto : listaDepartamento) {
                objs.add(new SelectItem(depto.getCodigo(), depto.getNome()));
            }
        }
        listaSelectItemDepartamentoFornecedor = objs;
    }


    public List getListaSelectItemCategoriaDespesa() throws Exception {
        List objs = new ArrayList(0);
        objs.add(new SelectItem(0, ""));
        List<CategoriaDespesaVO> listaCategoria = null;
        listaCategoria = getFacadeFactory().getCategoriaDespesaFacade().consultarPorIdentificadorCategoriaDespesa("", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
        if (listaCategoria != null && listaCategoria.size() != 0) {
            for (CategoriaDespesaVO categoriaDespesa : listaCategoria) {
                objs.add(new SelectItem(categoriaDespesa.getIdentificadorCategoriaDespesa(), categoriaDespesa.getDescricao()));
            }
        }
        return objs;
    }

    public Boolean getHabilitarComboBoxCentroCusto() {
        return grupoContaPagarVO.isTipoSacadoFuncionario();
    }

    public void consultarFuncionario() {
        try {
            List objs = null;

            getFacadeFactory().getFuncionarioFacade().setIdEntidade("Funcionario");

            if (getCampoConsultaFuncionario().equals("nome")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNome(getValorConsultaFuncionario(), "FU", this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("matricula")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultarPorMatricula(getValorConsultaFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("nomeCidade")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeCidade(getValorConsultaFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(), false,
                        Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("CPF")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCPF(getValorConsultaFuncionario(), "FU", this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
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
        itens.add(new SelectItem("razaoSocial", "Razão Social"));
        itens.add(new SelectItem("CNPJ", "CNPJ"));
        itens.add(new SelectItem("CPF", "CPF"));
        return itens;
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

    public void consultarFuncionarioCentroCusto() {
       /* try {
            List objs = null;
            if (getValorConsultaFuncionarioCentroCusto().equals("")) {
                setMensagemID("msg_entre_prmconsulta");
                return;
            }
            getFacadeFactory().getFuncionarioFacade().setIdEntidade("Funcionario");

            if (getCampoConsultaFuncionarioCentroCusto().equals("nome")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeECodigoDepartamentoEMultiDepartamento(getValorConsultaFuncionarioCentroCusto(), getContaPagarVO().getDepartamento().getCodigo(), "FI", grupoContaPagarVO.getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionarioCentroCusto().equals("CPF")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultarPorCPFECodigoDepartamentoEMultiDepartamento(getValorConsultaFuncionarioCentroCusto(), getContaPagarVO().getDepartamento().getCodigo(), "FI", grupoContaPagarVO.getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionarioCentroCusto().equals("matricula")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultarPorMatriculaECodigoDepartamentoEMultiDepartamento(getValorConsultaFuncionarioCentroCusto(), getContaPagarVO().getDepartamento().getCodigo(), "FI", grupoContaPagarVO.getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            setListaConsultaFuncionarioCentroCusto(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaFuncionarioCentroCusto(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }*/
    }

    public void selecionarFuncionarioCentroCusto() throws Exception {
        FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioCentroCustoItens");
        obj = getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimaria(obj.getCodigo(), 0, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
        /*this.getContaPagarVO().setFuncionarioCentroCusto(obj);*/
        Uteis.liberarListaMemoria(getListaConsultaFuncionario());
        campoConsultaFuncionario = null;
        valorConsultaFuncionario = null;
    }

    public List getTipoConsultaComboFuncionarioCentroCusto() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("matricula", "Matrícula"));
        itens.add(new SelectItem("CPF", "CPF"));
        return itens;
    }

    public String getCampoConsultaFuncionarioCentroCusto() {
        return campoConsultaFuncionarioCentroCusto;
    }

    public void setCampoConsultaFuncionarioCentroCusto(String campoConsultaFuncionario) {
        this.campoConsultaFuncionarioCentroCusto = campoConsultaFuncionario;
    }

    public List getListaConsultaFuncionarioCentroCusto() {
        return listaConsultaFuncionarioCentroCusto;
    }

    public void setListaConsultaFuncionarioCentroCusto(List listaConsultaFuncionario) {
        this.listaConsultaFuncionarioCentroCusto = listaConsultaFuncionario;
    }

    public String getValorConsultaFuncionarioCentroCusto() {
        return valorConsultaFuncionarioCentroCusto;
    }

    public void setValorConsultaFuncionarioCentroCusto(String valorConsultaFuncionario) {
        this.valorConsultaFuncionarioCentroCusto = valorConsultaFuncionario;
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

    public List getListaSelectItemTipoOrigem() throws Exception {
        List objs = new ArrayList(0);
        objs.add(new SelectItem("", ""));
        Hashtable tipoOrigem = (Hashtable) Dominios.getTipoOrigemContaPagar();
        Enumeration keys = tipoOrigem.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) tipoOrigem.get(value);
            objs.add(new SelectItem(value, label));
        }
        SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
        Collections.sort((List) objs, ordenador);
        return objs;
    }

    public List getListaSelectItemTipoJuroTipoMulta() throws Exception {
        List objs = new ArrayList(0);
        Hashtable tipoJuroTipoMulta = (Hashtable) Dominios.getContaPagarTipoJuroTipoMulta();
        Enumeration keys = tipoJuroTipoMulta.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) tipoJuroTipoMulta.get(value);
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
    public void montarListaSelectItemBanco(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarBancoPorNome(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                BancoVO obj = (BancoVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
            }
            setListaSelectItemBanco(objs);
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
    public void montarListaSelectItemBanco() {
        try {
            montarListaSelectItemBanco("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo
     * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
     * <code>ContaCorrente</code>.
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
                    objs.add(new SelectItem(obj.getCodigo(), "Banco:" + obj.getAgencia().getBanco().getNome() + " Ag:" + obj.getAgencia().getNumeroAgencia() + "-" + obj.getAgencia().getDigito() + " CC:" + obj.getNumero() + "-" + obj.getDigito()));
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
     * Método responsável por atualizar o ComboBox relativo ao atributo
     * <code>ContaCorrente</code>. Buscando todos os objetos correspondentes a
     * entidade <code>ContaCorrente</code>. Esta rotina não recebe parâmetros
     * para filtragem de dados, isto é importante para a inicialização dos dados
     * da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemUnidadeEnsino() {
        try {
            montarListaSelectItemUnidadeEnsino(0);
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Método responsável por consultar dados da entidade
     * <code><code> e montar o atributo <code>numero</code> Este atributo é uma
     * lista (<code>List</code>) utilizada para definir os valores a serem
     * apresentados no ComboBox correspondente
     */
    public List consultarUnidadeEnsinoPorCodigo(Integer numeroPrm) throws Exception {
        List lista = null;
        if ((numeroPrm != null && !numeroPrm.equals(0)) || (getUnidadeEnsinoLogado().getCodigo() != null && !getUnidadeEnsinoLogado().getCodigo().equals(0))) {
            lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorCodigo(numeroPrm, getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
        } else {
            lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarTodasUnidades(false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
        }
        return lista;
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo
     * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
     * <code>ContaCorrente</code>.
     */
//    public List getListaSelectItemBanco() throws Exception {
//        List resultadoConsulta = consultarBancoPorNome("");
//        Iterator i = resultadoConsulta.iterator();
//        List objs = new ArrayList(0);
//        objs.add(new SelectItem(0, ""));
//        while (i.hasNext()) {
//            BancoVO obj = (BancoVO) i.next();
//            objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
//        }
//        return objs;
//    }
    /**
     * Método responsável por consultar dados da entidade
     * <code><code> e montar o atributo <code>numero</code> Este atributo é uma
     * lista (<code>List</code>) utilizada para definir os valores a serem
     * apresentados no ComboBox correspondente
     */
    public List consultarBancoPorNome(String prm) throws Exception {
        List lista = getFacadeFactory().getBancoFacade().consultarPorNome(prm, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
        return lista;
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo
     * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
     * <code>ContaCorrente</code>.
     */
    public void montarListaSelectItemUnidadeEnsino(Integer prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            if (getUnidadeEnsinoLogado().getCodigo().intValue() != 0) {
                setListaSelectItemUnidadeEnsino(new ArrayList<SelectItem>());
                getListaSelectItemUnidadeEnsino().add(new SelectItem(getUnidadeEnsinoLogado().getCodigo(), getUnidadeEnsinoLogado().getNome()));
                getGrupoContaPagarVO().setUnidadeEnsino(getUnidadeEnsinoLogadoClone());
                return;
            }
            resultadoConsulta = consultarUnidadeEnsinoPorCodigo(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
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
     * Método responsável por atualizar o ComboBox relativo ao atributo
     * <code>ContaCorrente</code>. Buscando todos os objetos correspondentes a
     * entidade <code>ContaCorrente</code>. Esta rotina não recebe parâmetros
     * para filtragem de dados, isto é importante para a inicialização dos dados
     * da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemContaCorrente() {
        try {
            montarListaSelectItemContaCorrente("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Método responsável por consultar dados da entidade
     * <code><code> e montar o atributo <code>numero</code> Este atributo é uma
     * lista (<code>List</code>) utilizada para definir os valores a serem
     * apresentados no ComboBox correspondente
     */
    public List consultarContaCorrentePorNumero(String numeroPrm) throws Exception {
        List lista = getFacadeFactory().getContaCorrenteFacade().consultarPorNumero(numeroPrm, 0, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
        return lista;
    }
    /*
     * Método responsável por inicializar List<SelectItem> de valores do
     * ComboBox correspondente ao atributo <code>situacao</code>
     */

    public List getListaSelectItemSituacaoContaPagar() throws Exception {
        List objs = new ArrayList(0);
        Hashtable pagarPagoNegociados = (Hashtable) Dominios.getPagarPagoNegociado();
        Enumeration keys = pagarPagoNegociados.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) pagarPagoNegociados.get(value);
            objs.add(new SelectItem(value, label));
        }
        SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
        Collections.sort((List) objs, ordenador);
        return objs;
    }

    /**
     * Método responsável por inicializar a lista de valores (
     * <code>SelectItem</code>) para todos os ComboBox's.
     */
    public void inicializarListasSelectItemTodosComboBox() throws Exception {
        // montarListaSelectItemContaCorrente();
        // montarListaSelectItemUnidadeEnsino();
        // montarSelectItemUnidadeEnsinoFornecedor();
        // montarSelectItemCentroCusto();
        //montarSelectItemDepartamentoFornecedor();
        montarListaSelectItemBanco();
        // montarSelectItemTurmaFornecedor();
    }

    public List getListaSelectItemTipoDesconto() throws Exception {
        List objs = new ArrayList(0);
        Hashtable tipoDesconto = (Hashtable) Dominios.getTipoDesconto();
        objs.add(new SelectItem("", ""));
        Enumeration keys = tipoDesconto.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) tipoDesconto.get(value);
            objs.add(new SelectItem(value, label));
        }
        SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
        Collections.sort((List) objs, ordenador);
        return objs;
    }

    public boolean getSituacao() {
        if (getControleConsulta().getCampoConsulta().equals("situacao")) {
            return true;
        }
        return false;
    }

    public boolean getData() {
        if (getControleConsulta().getCampoConsulta().equals("dataVencimento")) {
            return true;
        }
        return false;
    }

    /**
     * Rotina responsável por preencher a combo de consulta da telas.
     */
    public List getTipoConsultaCombo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("favorecido", "Favorecido"));
        itens.add(new SelectItem("categoriaDespesa", "Categoria de Despesa"));
        return itens;
    }

    /**
     * Rotina responsável por preencher a combo de consulta da telas.
     */
    public List getTipoConsultaComboData() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("vencimento", "Data de Vencimento"));
        itens.add(new SelectItem("fatoGerador", "Data Fato Gerador"));
        itens.add(new SelectItem("emissão", "Data de Emissão"));
        itens.add(new SelectItem("semData", "Sem Data"));
        return itens;
    }

    public boolean isConsultaPorCategoriaDespesa() {
        return getControleConsulta().getCampoConsulta().equals("categoriaDespesa");
    }

    public boolean isConsultaPorCodigo() {
        return getControleConsulta().getCampoConsulta().equals("codigo");
    }

    public boolean isConsultaPorBanco() {
        return getControleConsulta().getCampoConsulta().equals("banco");
    }

    public boolean isCampoText() {
        return getControleConsulta().getCampoConsulta().equals("favorecido") || getControleConsulta().getCampoConsulta().equals("codigo");
    }

    /**
     * Rotina responsável por organizar a paginação entre as páginas resultantes
     * de uma consulta.
     */
    public String inicializarConsultar() {
        removerObjetoMemoria(this);
        setListaConsulta(new ArrayList(0));
        getControleConsulta().setDataIni(Uteis.getDataPrimeiroDiaMes(new Date()));
        getControleConsulta().setDataFim(Uteis.getDataUltimoDiaMes(new Date()));
        setMensagemID("msg_entre_prmconsulta");
        return Uteis.getCaminhoRedirecionamentoNavegacao("grupoContaPagarCons.xhtml");
    }

    public void adicionarContaPagar() {
        ContaPagarVO contaPagar = getContaPagarVO();
        try {
            if (!contaPagar.getUnidadeEnsino().getCodigo().equals(0)) {
                contaPagar.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getContaPagarVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
            }
            contaPagar.setTipoSacado(getGrupoContaPagarVO().getTipoSacado());
            if(getGrupoContaPagarVO().getTipoSacado().equals("AL") || getGrupoContaPagarVO().getTipoSacado().equals("RF")){
                contaPagar.setMatricula(getMatriculaAluno());
                contaPagar.setPessoa(getGrupoContaPagarVO().getPessoa());
            }            
            if(getGrupoContaPagarVO().getTipoSacado().equals("FO")){
                contaPagar.setFornecedor(getGrupoContaPagarVO().getFornecedor());
            }
            if(getGrupoContaPagarVO().getTipoSacado().equals("FU")){
                contaPagar.setFuncionario(getGrupoContaPagarVO().getFuncionario());
            }
            if(getGrupoContaPagarVO().getTipoSacado().equals("PA")){
                contaPagar.setParceiro(getGrupoContaPagarVO().getParceiro());
            }
            if(getGrupoContaPagarVO().getTipoSacado().equals("RF")){
                contaPagar.setResponsavelFinanceiro(getGrupoContaPagarVO().getResponsavelFinanceiro());
            }
            if (!getGrupoContaPagarVO().getBanco().getCodigo().equals(0) && getGrupoContaPagarVO().getTipoSacado().equals("BA")) {
                contaPagar.setBanco(getFacadeFactory().getBancoFacade().consultarPorChavePrimaria(getGrupoContaPagarVO().getBanco().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
            }
            contaPagar.setDataVencimento(getGrupoContaPagarVO().getDataVencimento());
            contaPagar.setData(getGrupoContaPagarVO().getDataEmissao());
            contaPagar.setDataFatoGerador(getGrupoContaPagarVO().getDataFatoGerador());
            contaPagar.setResponsavel(getGrupoContaPagarVO().getResponsavel());
            getFacadeFactory().getGrupoContaPagarFacade().validarDadosAdicionarContaPagar(contaPagar);
            getGrupoContaPagarVO().getContaPagarVOs().add(contaPagar);
            getGrupoContaPagarVO().setValor(getGrupoContaPagarVO().getValor() + contaPagar.getValor());
            setMensagemID("msg_dados_adicionados");
            setContaPagarVO(new ContaPagarVO());
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void validarDadosConsultaTurmaCurso(ContaPagarVO contaPagar) throws ConsistirException {
        if (contaPagar.getUnidadeEnsino().getCodigo().equals(0)) {
            throw new ConsistirException("O campo UNIDADE DE ENSINO (Conta à Pagar) deve ser informado para efetuar a consulta!");
        }
    }

    public void removerContaPagar() {
        try {
            ContaPagarVO obj = (ContaPagarVO) context().getExternalContext().getRequestMap().get("contaPagarItens");
            for (ContaPagarVO contaPagar : getGrupoContaPagarVO().getContaPagarVOs()) {
                if (contaPagar.equals(obj)) {
                    getGrupoContaPagarVO().getContaPagarVOs().remove(obj);
                    getGrupoContaPagarVO().setValor(getGrupoContaPagarVO().getValor() - contaPagar.getValor());
                    break;
                }
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
        }
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

    public List getListaConsultaCentroDespesa() {
        if (listaConsultaCentroDespesa == null) {
            listaConsultaCentroDespesa = new ArrayList(0);
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

    public List getListaSelectItemContaCorrente() {
        if (listaSelectItemContaCorrente == null) {
            listaSelectItemContaCorrente = new ArrayList(0);
        }
        return listaSelectItemContaCorrente;
    }

    public void setListaSelectItemContaCorrente(List listaSelectItemContaCorrente) {
        this.listaSelectItemContaCorrente = listaSelectItemContaCorrente;
    }

    public GrupoContaPagarVO getGrupoContaPagarVO() {
        if (grupoContaPagarVO == null) {
            grupoContaPagarVO = new GrupoContaPagarVO();
        }
        return grupoContaPagarVO;
    }

    public void setGrupoContaPagarVO(GrupoContaPagarVO grupoContaPagarVO) {
        this.grupoContaPagarVO = grupoContaPagarVO;
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

    public Boolean getHabilitarComboBoxUnidadeEnsino() throws Exception {
        Boolean retorno = grupoContaPagarVO.getHabilitarComboBoxUnidadeEnsino(getUnidadeEnsinoLogado().getCodigo());
        return retorno;
    }

    public CentroCustoVO getCentroCusto() {
        if (centroCusto == null) {
            centroCusto = new CentroCustoVO();
        }
        return centroCusto;
    }

    public void setCentroCusto(CentroCustoVO centroCusto) {
        this.centroCusto = centroCusto;
    }

    public List getListaSelectItemUnidadeEnsinoFornecedor() {
        if (listaSelectItemUnidadeEnsinoFornecedor == null) {
            listaSelectItemUnidadeEnsinoFornecedor = new ArrayList(0);
        }
        return listaSelectItemUnidadeEnsinoFornecedor;
    }

    public void setListaSelectItemUnidadeEnsinoFornecedor(List listaSelectItemUnidadeEnsinoFornecedor) {
        this.listaSelectItemUnidadeEnsinoFornecedor = listaSelectItemUnidadeEnsinoFornecedor;
    }

    public List getListaSelectItemDepartamentoFornecedor() {
        if (listaSelectItemDepartamentoFornecedor == null) {
            listaSelectItemDepartamentoFornecedor = new ArrayList(0);
        }
        return listaSelectItemDepartamentoFornecedor;
    }

    public void setListaSelectItemDepartamentoFornecedor(List listaSelectItemDepartamentoFornecedor) {
        this.listaSelectItemDepartamentoFornecedor = listaSelectItemDepartamentoFornecedor;
    }

    public List getListaSelectItemFuncionarioFornecedor() {
        if (listaSelectItemFuncionarioFornecedor == null) {
            listaSelectItemFuncionarioFornecedor = new ArrayList(0);
        }
        return listaSelectItemFuncionarioFornecedor;
    }

    public void setListaSelectItemFuncionarioFornecedor(List listaSelectItemFuncionarioFornecedor) {
        this.listaSelectItemFuncionarioFornecedor = listaSelectItemFuncionarioFornecedor;
    }

    /**
     * @param listaSelectItemCentroCusto
     *            the listaSelectItemCentroCusto to set
     */
    public void setListaSelectItemCentroCusto(List listaSelectItemCentroCusto) {
        this.listaSelectItemCentroCusto = listaSelectItemCentroCusto;
    }

    /**
     * @return the valorConsultaSituacaoDaConta
     */
    public String getValorConsultaSituacaoFinanceiraDaConta() {
        if (valorConsultaSituacaoFinanceiraDaConta == null) {
            valorConsultaSituacaoFinanceiraDaConta = "";
        }
        return valorConsultaSituacaoFinanceiraDaConta;
    }

    /**
     * @param valorConsultaSituacaoDaConta
     *            the valorConsultaSituacaoDaConta to set
     */
    public void setValorConsultaSituacaoFinanceiraDaConta(String valorConsultaSituacaoFinanceiraDaConta) {
        this.valorConsultaSituacaoFinanceiraDaConta = valorConsultaSituacaoFinanceiraDaConta;
    }

    public void consultarTurma() {
        try {
            super.consultar();
            validarDadosConsultaTurmaCurso(getContaPagarVO());
            setListaConsultaTurma(getFacadeFactory().getTurmaFacade().consultar(getCampoConsultaTurma(), getValorConsultaTurma(), getContaPagarVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            getListaConsultaTurma().clear();
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public List<SelectItem> getTipoConsultaComboTurma() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("identificadorTurma", "Identificador"));
//        itens.add(new SelectItem("nomeUnidadeEnsino", "Unidade Ensino"));
        itens.add(new SelectItem("nomeTurno", "Turno"));
        itens.add(new SelectItem("nomeCurso", "Curso"));
        return itens;
    }

    public void selecionarTurma() {
        try {
            TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
            getFacadeFactory().getTurmaFacade().carregarDados(obj, NivelMontarDados.BASICO, getUsuarioLogado());
          /*  getContaPagarVO().setTurma(obj);
            getContaPagarVO().setCursoVO(obj.getCurso());
            getContaPagarVO().setTurnoVO(obj.getTurno());*/
			setListaConsultaTurma(null);
        } catch (Exception ex) {
            setMensagemDetalhada("msg_erro", ex.getMessage());
        }
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

    public List<TurmaVO> getListaConsultaTurma() {
        if (listaConsultaTurma == null) {
            listaConsultaTurma = new ArrayList<TurmaVO>(0);
        }
        return listaConsultaTurma;
    }

    public void setListaConsultaTurma(List<TurmaVO> listaConsultaTurma) {
        this.listaConsultaTurma = listaConsultaTurma;
    }

    public List getTipoConsultaComboCurso() {
        List itens = new ArrayList(0);
        // itens.add(new SelectItem("codigo", "Código"));
        itens.add(new SelectItem("nome", "Nome"));
        return itens;
    }

    public List getTipoConsultaComboCursoTurno() {
        List itens = new ArrayList(0);
        // itens.add(new SelectItem("codigo", "Código"));
        itens.add(new SelectItem("curso", "Curso"));
        itens.add(new SelectItem("turno", "Turno"));
        return itens;
    }

    public void consultarCurso() {
        try {
            validarDadosConsultaTurmaCurso(getContaPagarVO());
            setListaConsultaCurso(getFacadeFactory().getCursoFacade().consultar(getCampoConsultaCurso(), getValorConsultaCurso(), getContaPagarVO().getUnidadeEnsino().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaCurso(null);
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarCurso() {
        try {
            CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItens");
            //getContaPagarVO().setCursoVO(obj);
//			montarListaSelectItemUnidadeEnsino();
            listaConsultaCurso.clear();
            this.setValorConsultaCurso("");
            this.setCampoConsultaCurso("");
        } catch (Exception ex) {
            setMensagemDetalhada("msg_erro", ex.getMessage());
        }
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

    public List<CursoVO> getListaConsultaCurso() {
        if (listaConsultaCurso == null) {
            listaConsultaCurso = new ArrayList<CursoVO>(0);
        }
        return listaConsultaCurso;
    }

    public void setListaConsultaCurso(List<CursoVO> listaConsultaCurso) {
        this.listaConsultaCurso = listaConsultaCurso;
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

    public void consultarCursoTurno() {
        try {
            validarDadosConsultaTurmaCurso(getContaPagarVO());
            setListaConsultaCursoTurno(getFacadeFactory().getUnidadeEnsinoCursoFacade().consultar(getCampoConsultaCursoTurno(), getValorConsultaCursoTurno(), getContaPagarVO().getUnidadeEnsino().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaCursoTurno(null);
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarCursoTurno() {
        try {
            UnidadeEnsinoCursoVO obj = (UnidadeEnsinoCursoVO) context().getExternalContext().getRequestMap().get("unidadeEnsinoCursoItens");
            /*getContaPagarVO().setCursoVO(obj.getCurso());
            getContaPagarVO().setTurnoVO(obj.getTurno());*/
            setListaConsultaCursoTurno(null);
            this.setValorConsultaCursoTurno("");
            this.setCampoConsultaCursoTurno("");
        } catch (Exception ex) {
            setMensagemDetalhada("msg_erro", ex.getMessage());
        }
    }

//    public String efetuarPagamento() {
//        try {
//            NegociacaoPagamentoControle negociacaoPagamentoControle = null;
//            negociacaoPagamentoControle = (NegociacaoPagamentoControle) context().getExternalContext().getSessionMap().get(NegociacaoPagamentoControle.class.getSimpleName());
//            if (negociacaoPagamentoControle == null) {
//                negociacaoPagamentoControle = new NegociacaoPagamentoControle();
//                context().getExternalContext().getSessionMap().put(NegociacaoPagamentoControle.class.getSimpleName(), negociacaoPagamentoControle);
//            }
//            context().getExternalContext().getRequestMap().put("contaPagar", getGrupoContaPagarVO());
//            return negociacaoPagamentoControle.efetuarPagamento();
//        } catch (Exception e) {
//            setMensagemDetalhada("msg_erro", e.getMessage());
//            return "editar";
//        }
//    }
    public String getCampoConsultaCursoTurno() {
        if (campoConsultaCursoTurno == null) {
            campoConsultaCursoTurno = "";
        }
        return campoConsultaCursoTurno;
    }

    public String getValorConsultaCursoTurno() {
        if (valorConsultaCursoTurno == null) {
            valorConsultaCursoTurno = "";
        }
        return valorConsultaCursoTurno;
    }

    public List<UnidadeEnsinoCursoVO> getListaConsultaCursoTurno() {
        if (listaConsultaCursoTurno == null) {
            listaConsultaCursoTurno = new ArrayList<UnidadeEnsinoCursoVO>(0);
        }
        return listaConsultaCursoTurno;
    }

    public void setCampoConsultaCursoTurno(String campoConsultaCursoTurno) {
        this.campoConsultaCursoTurno = campoConsultaCursoTurno;
    }

    public void setValorConsultaCursoTurno(String valorConsultaCursoTurno) {
        this.valorConsultaCursoTurno = valorConsultaCursoTurno;
    }

    public void setListaConsultaCursoTurno(List<UnidadeEnsinoCursoVO> listaConsultaCursoTurno) {
        this.listaConsultaCursoTurno = listaConsultaCursoTurno;
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

    public String getValorConsultaAluno() {
        if (valorConsultaAluno == null) {
            valorConsultaAluno = "";
        }
        return valorConsultaAluno;
    }

    public void setValorConsultaAluno(String valorConsultaAluno) {
        this.valorConsultaAluno = valorConsultaAluno;
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

    public ContaPagarVO getContaPagarVO() {
        if (contaPagarVO == null) {
            contaPagarVO = new ContaPagarVO();
        }
        return contaPagarVO;
    }

    public String getFiltroTipoData() {
        if (filtroTipoData == null) {
            filtroTipoData = "vencimento";
        }
        return filtroTipoData;
    }

    public void setFiltroTipoData(String filtroTipoData) {
        this.filtroTipoData = filtroTipoData;
    }

    public void setContaPagarVO(ContaPagarVO contaPagarVO) {
        this.contaPagarVO = contaPagarVO;
    }

    public String getCaminhoRelatorioExtratoContaPagar() {
        if (caminhoRelatorioExtratoContaPagar == null) {
            caminhoRelatorioExtratoContaPagar = "";
        }
        return caminhoRelatorioExtratoContaPagar;
    }

    public void setCaminhoRelatorioExtratoContaPagar(String caminhoRelatorioExtratoContaPagar) {
        this.caminhoRelatorioExtratoContaPagar = caminhoRelatorioExtratoContaPagar;
    }

    public Boolean getGerarRelatorio() {
        if (gerarRelatorio == null) {
            gerarRelatorio = false;
        }
        return gerarRelatorio;
    }

    public void setGerarRelatorio(Boolean gerarRelatorio) {
        this.gerarRelatorio = gerarRelatorio;
    }

   /* public boolean getIsApresentarCampoCurso() {
        return getContaPagarVO().getCentroDespesa().getInformarTurma() != null && getContaPagarVO().getCentroDespesa().getInformarTurma().equals("CU");
    }

    public boolean getIsApresentarCampoCursoTurno() {
        return getContaPagarVO().getCentroDespesa().getInformarTurma() != null && getContaPagarVO().getCentroDespesa().getInformarTurma().equals("CT");
    }

    public boolean getIsApresentarCampoTurma() {
        return getContaPagarVO().getCentroDespesa().getInformarTurma() != null && getContaPagarVO().getCentroDespesa().getInformarTurma().equals("TU");
    }

    public boolean getApresentarRecebimento() {
        return getContaPagarVO().getCentroDespesa().getInformarTurma() != null && getContaPagarVO().getCentroDespesa().getInformarTurma().equals("TU");
    }
*/
    public boolean getIsEditandoObj() {
        return !getGrupoContaPagarVO().getNovoObj();
    }

    public boolean getIsFiltrarPorData() {
        return (!getFiltroTipoData().equals("")) && (!getFiltroTipoData().equals("semData"));
    }

    public List getListaSelectItemBanco() {
        if (listaSelectItemBanco == null) {
            listaSelectItemBanco = new ArrayList();
        }
        return listaSelectItemBanco;
    }

    public void setListaSelectItemBanco(List listaSelectItemBanco) {
        this.listaSelectItemBanco = listaSelectItemBanco;
    }

    public String getMatriculaAluno() {
        if (matriculaAluno == null) {
            matriculaAluno = "";
        }
        return matriculaAluno;
    }

    public void setMatriculaAluno(String matriculaAluno) {
        this.matriculaAluno = matriculaAluno;
    }
    
    protected List<PessoaVO> listaConsultaResponsavelFinanceiro;
    protected String valorConsultaResponsavelFinanceiro;
    protected String campoConsultaResponsavelFinanceiro;
    private List listaConsultaParceiro;
    private String valorConsultaParceiro;
    private String campoConsultaParceiro;
  
    
    public List getListaConsultaParceiro() {
        if (listaConsultaParceiro == null) {
            listaConsultaParceiro = new ArrayList(0);
        }
        return listaConsultaParceiro;
    }

    public void setListaConsultaParceiro(List listaConsultaParceiro) {
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
    
    public void consultarParceiro() {
        try {
            List objs = new ArrayList(0);
            if (getCampoConsultaParceiro().equals("nome")) {
                objs = getFacadeFactory().getParceiroFacade().consultarPorNome(getValorConsultaParceiro(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaParceiro().equals("razaoSocial")) {
                objs = getFacadeFactory().getParceiroFacade().consultarPorRazaoSocial(getValorConsultaParceiro(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            setListaConsultaParceiro(objs);
            setMensagemID(
                    "msg_dados_consultados");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada(
                    "msg_erro", e.getMessage());
        }
    }

    public List getTipoConsultaParceiro() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("razaoSocial", "Razão social"));
        return itens;
    }

    public void selecionarParceiro() {
        ParceiroVO obj = (ParceiroVO) context().getExternalContext().getRequestMap().get("parceiroItens");
        getGrupoContaPagarVO().setParceiro(obj);
        listaConsultaParceiro.clear();
        this.setValorConsultaParceiro("");
        this.setCampoConsultaParceiro("");
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
            this.getGrupoContaPagarVO().setResponsavelFinanceiro(obj);                       
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
    
    public void consultarAlunoResponsavelFinanceiro() {
        try {
            limparMensagem();
            setListaConsultaAluno(getFacadeFactory().getMatriculaFacade().consultaRapidaBasicaPorCodigoResponsavelFinanceiro(getGrupoContaPagarVO().getResponsavelFinanceiro().getCodigo(),false, getUsuarioLogado()));           
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }
    
    public void scrollerListener(DataScrollEvent DataScrollEvent) throws Exception {
        getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
        getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
        consultar();
    }
    
}
