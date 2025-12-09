package relatorio.controle.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.faces.model.SelectItem;

import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.financeiro.BancoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.dominios.TipoFormaPagamento;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.financeiro.PagamentoRelVO;

@Controller("PagamentoResumidoRelControle")
@Scope("request")
@Lazy
public class PagamentoResumidoRelControle extends SuperControleRelatorio {

    protected List listaConsultaFornecedor;
    protected String valorConsultaFornecedor;
    protected String campoConsultaFornecedor;
    protected List listaConsultaBanco;
    protected List listaConsultaFuncionario;
    protected String valorConsultaBanco;
    protected String valorConsultaFuncionario;
    protected String campoConsultaBanco;
    protected String campoConsultaFuncionario;
    private List<SelectItem> listaSelectItemUnidadeEnsino;
    //
    protected Date dataInicio;
    protected Date dataFim;
    protected String fornecedorNome;
    protected String fornecedorCpfCnpj;
    protected Integer fornecedor;
    protected UnidadeEnsinoVO unidadeEnsino;
    protected Integer funcionario;
    protected String funcionarioNome;
    protected Integer banco;
    protected String bancoNome;
    private Integer filtroFornecedor;
    private List<SelectItem> listaFiltroFornecedor;
    private Integer filtroFuncionario;
    private List<SelectItem> listaFiltroFuncionario;
    private Integer filtroBanco;
    private List<SelectItem> listaFiltroBanco;
    private Boolean apresentarFiltroFornecedor;
    private Boolean apresentarFiltroFuncionario;
    private Boolean apresentarFiltroBanco;
    private Boolean apresentarFiltroTipo;
    List<SelectItem> tipoConsultaComboCentroDespesa;
    private String formaPagamento;
    private List<SelectItem> listaFiltroTipo;
    private Integer filtroTipo;
    private String layout;
    private String ordenar;

    public PagamentoResumidoRelControle() throws Exception {
        inicializarListasSelectItemTodosComboBox();
        setMensagemID("msg_entre_prmrelatorio");
    }

    public void imprimirPDF() {
        List<PagamentoRelVO> lista = null;
        try {
            lista = getFacadeFactory().getPagamentoResumidoRelFacade().criarObjeto(getDataInicio(), getDataFim(), getUnidadeEnsino(), getFornecedor(),
                    getFornecedorNome(), getFornecedorCpfCnpj(), getFuncionarioNome(), getFuncionario(), getBanco(), getBancoNome(), getFiltroFornecedor(),
                    getFiltroFuncionario(), getFiltroBanco(), getFormaPagamento(), getFiltroTipo());
            if (!lista.isEmpty()) {
                getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getPagamentoResumidoRelFacade().designIReportRelatorio());
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
                getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getPagamentoResumidoRelFacade().caminhoBaseRelatorio());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setTituloRelatorio("Relatório de Pagamentos (Sintético)");
                getSuperParametroRelVO().setListaObjetos(lista);
                getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getPagamentoResumidoRelFacade().caminhoBaseRelatorio());
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());

                getSuperParametroRelVO().setPeriodo(
                        String.valueOf(Uteis.getData(getDataInicio())) + "  até  "
                        + String.valueOf(Uteis.getData(getDataFim())));

                if (getUnidadeEnsino().getCodigo() != null && getUnidadeEnsino().getCodigo() > 0) {
                    getSuperParametroRelVO().setUnidadeEnsino(
                            (getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsino().getCodigo(), false,
                            Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())).getNome());
                } else {
                    getSuperParametroRelVO().setUnidadeEnsino("Todas");
                }

                getSuperParametroRelVO().setFornecedor(getListaFiltroFornecedor().get(getFiltroFornecedor()).getLabel());

                getSuperParametroRelVO().setFuncionario(getListaFiltroFuncionario().get(getFiltroFuncionario()).getLabel());

                getSuperParametroRelVO().setBanco(getListaFiltroBanco().get(getFiltroBanco()).getLabel());

                realizarImpressaoRelatorio();
                removerObjetoMemoria(this);
                inicializarListasSelectItemTodosComboBox();
                setMensagemID("msg_relatorio_ok");
            } else {
                setMensagemID("msg_relatorio_sem_dados");
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            Uteis.liberarListaMemoria(lista);

        }

    }

    public void consultarFornecedor() {
        try {
            List objs = new ArrayList(0);
            if (getCampoConsultaFornecedor().equals("nome")) {
                objs = getFacadeFactory().getFornecedorFacade().consultarPorNome(getValorConsultaFornecedor(), "AT", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getCampoConsultaFornecedor().equals("razaoSocial")) {
                objs = getFacadeFactory().getFornecedorFacade().consultarPorRazaoSocial(getValorConsultaFornecedor(), "AT", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getCampoConsultaFornecedor().equals("CNPJ")) {
                objs = getFacadeFactory().getFornecedorFacade().consultarPorCNPJ(getValorConsultaFornecedor(), "AT",false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getCampoConsultaFornecedor().equals("CPF")) {
                objs = getFacadeFactory().getFornecedorFacade().consultarPorCPF(getValorConsultaFornecedor(), "AT",false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            setListaConsultaFornecedor(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaFornecedor(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void limparDadosFornecedor() {
        setFornecedor(0);
        setFornecedorCpfCnpj("");
        setFornecedorNome("");
    }

    public void limparDadosFuncionario() {
        setFuncionario(0);
        setFuncionarioNome("");

    }

    public void limparDadosBanco() {
        setBanco(0);
        setBancoNome("");

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

    public void consultarFuncionario() {
        try {
            List objs = new ArrayList(0);
            if (getCampoConsultaFuncionario().equals("nome")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultarPorNome(getValorConsultaFuncionario(), getUnidadeEnsino().getCodigo(), false,
                        Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("cpf")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultarPorCPF(getValorConsultaFuncionario(), getUnidadeEnsino().getCodigo(), false,
                        Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            setListaConsultaFuncionario(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaFuncionario(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public List<SelectItem> getTipoConsultaComboCentroDespesa() {
        if (tipoConsultaComboCentroDespesa == null) {
            tipoConsultaComboCentroDespesa = new ArrayList<SelectItem>(2);
            tipoConsultaComboCentroDespesa.add(new SelectItem("descricao", "Descrição"));
            tipoConsultaComboCentroDespesa.add(new SelectItem("identificadorCentroDespesa", "Identificador Centro Despesa"));
        }
        return tipoConsultaComboCentroDespesa;
    }

    public List<SelectItem> getTipoConsultaComboFornecedor() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("razaoSocial", "Razão Social"));
        itens.add(new SelectItem("CNPJ", "CNPJ"));
        itens.add(new SelectItem("CPF", "CPF"));
        return itens;
    }

    public List<SelectItem> getTipoConsultaComboFuncionario() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("CPF", "CPF"));
        return itens;
    }

    public List<SelectItem> getTipoConsultaComboBanco() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("nrBanco", "Número Banco"));
        return itens;
    }

    public List<SelectItem> getTipoConsultaComboPlanoConta() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("identificadorPlanoConta", "Identificador Plano Conta"));
        return itens;
    }

    public void selecionarFornecedor() {
        FornecedorVO obj = (FornecedorVO) context().getExternalContext().getRequestMap().get("fornecedor");
        this.setFornecedorNome(obj.getNome());
        if (obj.getTipoEmpresa().equals("FI")) {
            this.setFornecedorCpfCnpj(obj.getCPF());
        } else {
            this.setFornecedorCpfCnpj(obj.getCNPJ());
        }
        this.setFornecedor(obj.getCodigo());
        setCampoConsultaFornecedor("");
        setValorConsultaFornecedor("");
        setListaConsultaFornecedor(new ArrayList(0));

    }

    public void selecionarFuncionario() {
        FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionario");
        this.setFuncionario(obj.getCodigo());
        this.setFuncionarioNome(obj.getPessoa().getNome());
        setCampoConsultaFuncionario("");
        setValorConsultaFuncionario("");
        setListaConsultaFuncionario(new ArrayList(0));
    }

    public void selecionarBanco() {
        BancoVO obj = (BancoVO) context().getExternalContext().getRequestMap().get("banco");
        this.setBanco(obj.getCodigo());
        this.setBancoNome(obj.getNome());
        setCampoConsultaBanco("");
        setValorConsultaBanco("");
        setListaConsultaBanco(new ArrayList(0));
    }

    public void inicializarListasSelectItemTodosComboBox() {
        montarListaSelectItemOrdenacao();
        montarListaSelectItemUnidadeEnsino();
        inicializarListaFiltroFornecedor();
        inicializarListaFiltroFuncionario();
        inicializarListaFiltroBanco();
        inicializarListaFiltroTipo();
        setFiltroFornecedor(0);
        setFiltroFuncionario(0);
        setFiltroBanco(0);
        validarFiltroFornecedor();
        validarFiltroFuncionario();
        validarFiltroBanco();
    }

    public void montarListaSelectItemOrdenacao() {
        Vector opcoes = getFacadeFactory().getPagamentoResumidoRelFacade().getOrdenacoesRelatorio();
        Enumeration i = opcoes.elements();
        List<SelectItem> objs = new ArrayList<SelectItem>(0);
        int contador = 0;
        while (i.hasMoreElements()) {
            String option = (String) i.nextElement();
            objs.add(new SelectItem(new Integer(contador), option));
            contador++;
        }
        setListaSelectItemOrdenacoesRelatorio(objs);
    }

    public void montarListaSelectItemUnidadeEnsino() {
        try {
            montarListaSelectItemUnidadeEnsino("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }

    }

    public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
            i = resultadoConsulta.iterator();
            getListaSelectItemUnidadeEnsino().clear();
            if (super.getUnidadeEnsinoLogado().getCodigo().equals(0)) {
                getListaSelectItemUnidadeEnsino().add(new SelectItem(0, ""));
            }
            while (i.hasNext()) {
                UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
                getListaSelectItemUnidadeEnsino().add(new SelectItem(obj.getCodigo(), obj.getNome()));
                removerObjetoMemoria(obj);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    public List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
        List<UnidadeEnsinoVO> lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false,
                Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
        return lista;
    }

    public void inicializarListaFiltroFornecedor() {
        List<SelectItem> objs = new ArrayList<SelectItem>(0);
        setFiltroFornecedor(0);
        setListaFiltroFornecedor(new ArrayList<SelectItem>(0));
        objs.add(new SelectItem(0, "Todos"));
        objs.add(new SelectItem(new Integer(1), "Nenhum"));
        objs.add(new SelectItem(new Integer(2), "Filtrar"));
        setListaFiltroFornecedor(objs);
    }

    public void inicializarListaFiltroFuncionario() {
        List<SelectItem> objs = new ArrayList<SelectItem>(0);
        setFiltroFuncionario(0);
        setListaFiltroFuncionario(new ArrayList<SelectItem>(0));
        objs.add(new SelectItem(0, "Todos"));
        objs.add(new SelectItem(new Integer(1), "Nenhum"));
        objs.add(new SelectItem(new Integer(2), "Filtrar"));
        setListaFiltroFuncionario(objs);
    }

    public void inicializarListaFiltroBanco() {
        List<SelectItem> objs = new ArrayList<SelectItem>(0);
        setFiltroBanco(0);
        setListaFiltroBanco(new ArrayList<SelectItem>(0));
        objs.add(new SelectItem(0, "Todos"));
        objs.add(new SelectItem(new Integer(1), "Nenhum"));
        objs.add(new SelectItem(new Integer(2), "Filtrar"));
        setListaFiltroBanco(objs);
    }

    public void validarFiltroFornecedor() {
        if (filtroFornecedor.intValue() == 2) {
            setApresentarFiltroFornecedor(true);
        } else {
            setApresentarFiltroFornecedor(false);
        }
    }

    public void validarFiltroFuncionario() {
        if (filtroFuncionario.intValue() == 2) {
            setApresentarFiltroFuncionario(true);
        } else {
            setApresentarFiltroFuncionario(false);
        }
    }

    public void validarFiltroBanco() {
        if (filtroBanco.intValue() == 2) {
            setApresentarFiltroBanco(true);
        } else {
            setApresentarFiltroBanco(false);
        }
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

    /**
     * @return the listaConsultaBanco
     */
    public List getListaConsultaBanco() {
        return listaConsultaBanco;
    }

    /**
     * @param listaConsultaBanco the listaConsultaBanco to set
     */
    public void setListaConsultaBanco(List listaConsultaBanco) {
        this.listaConsultaBanco = listaConsultaBanco;
    }

    /**
     * @return the listaConsultaFuncionario
     */
    public List getListaConsultaFuncionario() {
        return listaConsultaFuncionario;
    }

    /**
     * @param listaConsultaFuncionario the listaConsultaFuncionario to set
     */
    public void setListaConsultaFuncionario(List listaConsultaFuncionario) {
        this.listaConsultaFuncionario = listaConsultaFuncionario;
    }

    /**
     * @return the valorConsultaBanco
     */
    public String getValorConsultaBanco() {
        return valorConsultaBanco;
    }

    /**
     * @param valorConsultaBanco the valorConsultaBanco to set
     */
    public void setValorConsultaBanco(String valorConsultaBanco) {
        this.valorConsultaBanco = valorConsultaBanco;
    }

    /**
     * @return the valorConsultaFuncionario
     */
    public String getValorConsultaFuncionario() {
        return valorConsultaFuncionario;
    }

    /**
     * @param valorConsultaFuncionario the valorConsultaFuncionario to set
     */
    public void setValorConsultaFuncionario(String valorConsultaFuncionario) {
        this.valorConsultaFuncionario = valorConsultaFuncionario;
    }

    /**
     * @return the campoConsultaBanco
     */
    public String getCampoConsultaBanco() {
        return campoConsultaBanco;
    }

    /**
     * @param campoConsultaBanco the campoConsultaBanco to set
     */
    public void setCampoConsultaBanco(String campoConsultaBanco) {
        this.campoConsultaBanco = campoConsultaBanco;
    }

    /**
     * @return the campoConsultaFuncionario
     */
    public String getCampoConsultaFuncionario() {
        return campoConsultaFuncionario;
    }

    /**
     * @param campoConsultaFuncionario the campoConsultaFuncionario to set
     */
    public void setCampoConsultaFuncionario(String campoConsultaFuncionario) {
        this.campoConsultaFuncionario = campoConsultaFuncionario;
    }

    /**
     * @return the listaSelectItemUnidadeEnsino
     */
    public List<SelectItem> getListaSelectItemUnidadeEnsino() {
        if (listaSelectItemUnidadeEnsino == null) {
            listaSelectItemUnidadeEnsino = new ArrayList(0);
        }
        return listaSelectItemUnidadeEnsino;
    }

    /**
     * @param listaSelectItemUnidadeEnsino the listaSelectItemUnidadeEnsino to set
     */
    public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
        this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
    }

    public String getFornecedorCpfCnpj() {
        return fornecedorCpfCnpj;
    }

    public void setFornecedorCpfCnpj(String fornecedorCpfCnpj) {
        this.fornecedorCpfCnpj = fornecedorCpfCnpj;
    }

    public Date getDataFim() {
        if (dataFim == null) {
            dataFim = new Date();
        }
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public Date getDataInicio() {
        if (dataInicio == null) {
            dataInicio = Uteis.getNewDateComMesesAMenos(1);
        }
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Integer getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Integer fornecedor) {
        this.fornecedor = fornecedor;
    }

    public String getFornecedorNome() {
        return fornecedorNome;
    }

    public void setFornecedorNome(String fornecedorNome) {
        this.fornecedorNome = fornecedorNome;
    }

    public UnidadeEnsinoVO getUnidadeEnsino() {
        if (unidadeEnsino == null) {
            unidadeEnsino = new UnidadeEnsinoVO();
        }
        return unidadeEnsino;
    }

    public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
        this.unidadeEnsino = unidadeEnsino;
    }

    public Integer getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Integer funcionario) {
        this.funcionario = funcionario;
    }

    public String getFuncionarioNome() {
        return funcionarioNome;
    }

    public void setFuncionarioNome(String funcionarioNome) {
        this.funcionarioNome = funcionarioNome;
    }

    public Integer getBanco() {
        return banco;
    }

    public void setBanco(Integer banco) {
        this.banco = banco;
    }

    public String getBancoNome() {
        return bancoNome;
    }

    public void setBancoNome(String bancoNome) {
        this.bancoNome = bancoNome;
    }

    public Integer getFiltroFornecedor() {
        return filtroFornecedor;
    }

    public void setFiltroFornecedor(Integer filtroFornecedor) {
        this.filtroFornecedor = filtroFornecedor;
    }

    public List<SelectItem> getListaFiltroFornecedor() {
        return listaFiltroFornecedor;
    }

    public void setListaFiltroFornecedor(List<SelectItem> listaFiltroFornecedor) {
        this.listaFiltroFornecedor = listaFiltroFornecedor;
    }

    public Integer getFiltroFuncionario() {
        return filtroFuncionario;
    }

    public void setFiltroFuncionario(Integer filtroFuncionario) {
        this.filtroFuncionario = filtroFuncionario;
    }

    public List<SelectItem> getListaFiltroFuncionario() {
        return listaFiltroFuncionario;
    }

    public void setListaFiltroFuncionario(List<SelectItem> listaFiltroFuncionario) {
        this.listaFiltroFuncionario = listaFiltroFuncionario;
    }

    public Integer getFiltroBanco() {
        return filtroBanco;
    }

    public void setFiltroBanco(Integer filtroBanco) {
        this.filtroBanco = filtroBanco;
    }

    public List<SelectItem> getListaFiltroBanco() {
        return listaFiltroBanco;
    }

    public void setListaFiltroBanco(List<SelectItem> listaFiltroBanco) {
        this.listaFiltroBanco = listaFiltroBanco;
    }

    public Boolean getApresentarFiltroFornecedor() {
        return apresentarFiltroFornecedor;
    }

    public void setApresentarFiltroFornecedor(Boolean apresentarFiltroFornecedor) {
        this.apresentarFiltroFornecedor = apresentarFiltroFornecedor;
    }

    public Boolean getApresentarFiltroFuncionario() {
        return apresentarFiltroFuncionario;
    }

    public void setApresentarFiltroFuncionario(Boolean apresentarFiltroFuncionario) {
        this.apresentarFiltroFuncionario = apresentarFiltroFuncionario;
    }

    public Boolean getApresentarFiltroBanco() {
        return apresentarFiltroBanco;
    }

    public void setApresentarFiltroBanco(Boolean apresentarFiltroBanco) {
        this.apresentarFiltroBanco = apresentarFiltroBanco;
    }

    public String getFormaPagamento() {
        if (formaPagamento == null) {
            formaPagamento = "";
        }
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public String getFormaPagamento_Apresentar() {
        if (formaPagamento.equals("CA")) {
            return "Cartão de Crédito";
        }
        if (formaPagamento.equals("DI")) {
            return "Dinheiro";
        }
        if (formaPagamento.equals("CH")) {
            return "Cheque";
        }
        if (formaPagamento.equals("BO")) {
            return "Boleto Bancário";
        }
        if (formaPagamento.equals("DE")) {
            return "Débito em Conta";
        }
        return formaPagamento;
    }

    public List getListaSelectItemTipoFormaPagamento() {
        List objs = new ArrayList(0);
        objs = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoFormaPagamento.class);
        objs.remove(0);
        return objs;

    }

    public Boolean getApresentarFiltroTipo() {
        if (apresentarFiltroTipo == null) {
            apresentarFiltroTipo = Boolean.FALSE;
        }
        return apresentarFiltroTipo;
    }

    public void setApresentarFiltroTipo(Boolean apresentarFiltroTipo) {
        this.apresentarFiltroTipo = apresentarFiltroTipo;
    }

    public void validarFiltroTipo() {
        if (getFiltroTipo().intValue() == 2) {
            setApresentarFiltroTipo(true);
        } else {
            setApresentarFiltroTipo(false);
        }
    }

    public void inicializarListaFiltroTipo() {
        List<SelectItem> objs = new ArrayList<SelectItem>(0);
        setFiltroTipo(0);
        setListaFiltroTipo(new ArrayList<SelectItem>(0));
        objs.add(new SelectItem(0, "Todos"));
        objs.add(new SelectItem(new Integer(1), "Nenhum"));
        objs.add(new SelectItem(new Integer(2), "Filtrar"));
        setListaFiltroTipo(objs);
    }

    public Integer getFiltroTipo() {
        if (filtroTipo == null) {
            filtroTipo = 0;
        }
        return filtroTipo;
    }

    public void setFiltroTipo(Integer filtroTipo) {
        this.filtroTipo = filtroTipo;
    }

    public List<SelectItem> getListaFiltroTipo() {
        if (listaFiltroTipo == null) {
            setListaFiltroTipo((List<SelectItem>) new ArrayList(0));
        }
        return listaFiltroTipo;
    }

    public void setListaFiltroTipo(List<SelectItem> listaFiltroTipo) {
        this.listaFiltroTipo = listaFiltroTipo;
    }

    public List<SelectItem> getTipoConsultaComboOrdenacao() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("nome", "Nome Favorecido"));
        itens.add(new SelectItem("dataVencimento", "Data de Vencimento"));
        itens.add(new SelectItem("dataPagamento", "Data de Pagamento"));
        return itens;
    }

    public String getOrdenar() {
        if (ordenar == null) {
            ordenar = "";
        }
        return ordenar;
    }

    public void setOrdenar(String ordenar) {
        this.ordenar = ordenar;
    }


}
