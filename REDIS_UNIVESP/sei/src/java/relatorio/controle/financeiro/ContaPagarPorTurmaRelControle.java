package relatorio.controle.financeiro;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.faces.model.SelectItem;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.financeiro.BancoVO;
import negocio.comuns.financeiro.CategoriaDespesaVO;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.financeiro.ContaPagarRelVO;
import relatorio.negocio.jdbc.financeiro.enumeradores.ContaPagarFiltrosEnum;
import controle.arquitetura.SelectItemOrdemValor;
import relatorio.negocio.comuns.financeiro.ContaPagarPorTurmaRelVO;

@Controller("ContaPagarPorTurmaRelControle")
@Scope("viewScope")
@Lazy
public class ContaPagarPorTurmaRelControle extends SuperControleRelatorio {

    protected List listaConsultaFornecedor;
    protected String valorConsultaFornecedor;
    protected String campoConsultaFornecedor;
    protected List listaConsultaBanco;
    protected List listaConsultaFuncionario;
    protected String valorConsultaBanco;
    protected String valorConsultaFuncionario;
    protected String campoConsultaBanco;
    protected String campoConsultaFuncionario;
    protected List listaConsultaCentroDespesa;
    protected String valorConsultaCentroDespesa;
    protected String campoConsultaCentroDespesa;
    private List<SelectItem> listaSelectItemUnidadeEnsino;
    protected List<SelectItem> listaSelectItemTurma;
    private String nomeRelatorio;
    private TurmaVO turmaVO;
    private String campoConsultaTurma;
    private String valorConsultaTurma;
    private List<TurmaVO> listaConsultaTurma;
    private ContaPagarPorTurmaRelVO contaPagarPorTurmaRelVO;
    private Boolean apresentarFiltroFornecedor;
    private Boolean apresentarFiltroFuncionario;
    private Boolean apresentarFiltroBanco;
    private Boolean apresentarFiltroAluno;
    private Boolean apresentarFiltroResponsavelFinanceiro;
    private Boolean apresentarFiltroParceiro;
    private String filtroAPagar;
	private String filtroPago;
	private String filtroCancelado;
	private String filtroPagoParcialmente;
	private List<SelectItem> listaSelectItemFiltroContaAPaga;
	private List<SelectItem> listaSelectItemFiltroPaga;

    public ContaPagarPorTurmaRelControle() {

        inicializarDadosControle();
        setMensagemID("msg_entre_prmrelatorio");
    }

    private void inicializarDadosControle() {
        try {
            inicializarListasSelectItemTodosComboBox();
            getContaPagarPorTurmaRelVO().setFornecedor(ContaPagarFiltrosEnum.TODOS.getDescricao());
            getContaPagarPorTurmaRelVO().setFuncionario(ContaPagarFiltrosEnum.TODOS.getDescricao());
            getContaPagarPorTurmaRelVO().setBanco(ContaPagarFiltrosEnum.TODOS.getDescricao());
            getContaPagarPorTurmaRelVO().setAluno(ContaPagarFiltrosEnum.TODOS.getDescricao());
            getContaPagarPorTurmaRelVO().setParceiro(ContaPagarFiltrosEnum.TODOS.getDescricao());
            getContaPagarPorTurmaRelVO().setResponsavelFinanceiro(ContaPagarFiltrosEnum.TODOS.getDescricao());
            getContaPagarPorTurmaRelVO().setDataFim(new Date());
            getContaPagarPorTurmaRelVO().setDataInicio(Uteis.getNewDateComMesesAMenos(1));
            getContaPagarPorTurmaRelVO().setUnidadeEnsino(getUnidadeEnsinoLogado().getCodigo());
        } catch (Exception ex) {
            setMensagemDetalhada("msg_erro", ex.getMessage());
        }

    }

    public void imprimirPDF() {
        List<ContaPagarPorTurmaRelVO> listaContaPagarRelVO = null;
        String situacao = null;
        try {
            getFacadeFactory().getContaPagarPorTurmaRelFacade().validarDados(getContaPagarPorTurmaRelVO());
            listaContaPagarRelVO = getFacadeFactory().getContaPagarPorTurmaRelFacade().criarObjeto(getContaPagarPorTurmaRelVO(), getTurmaVO().getCodigo(), getFiltroAPagar(), getFiltroPago(), getFiltroPagoParcialmente(), getFiltroCancelado());
            if (!listaContaPagarRelVO.isEmpty()) {
                getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getContaPagarPorTurmaRelFacade().designIReportRelatorio());
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
                getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getContaPagarPorTurmaRelFacade().caminhoBaseRelatorio());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setTituloRelatorio("Relatório de Contas a Pagar Por Turma");
                getSuperParametroRelVO().setListaObjetos(listaContaPagarRelVO);
                getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getContaPagarPorTurmaRelFacade().caminhoBaseRelatorio());
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());

                getSuperParametroRelVO().setPeriodo(
                        String.valueOf(Uteis.getData(getContaPagarPorTurmaRelVO().getDataInicio())) + "  até  "
                        + String.valueOf(Uteis.getData(getContaPagarPorTurmaRelVO().getDataFim())));

                getSuperParametroRelVO().setUnidadeEnsino(
                        (getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getContaPagarPorTurmaRelVO().getUnidadeEnsino(), false,
                        Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())).getNome());

                getSuperParametroRelVO().setTurma(
                        getTurmaVO().getIdentificadorTurma() == null ? ContaPagarFiltrosEnum.TODOS.getDescricao() : getTurmaVO().getIdentificadorTurma().equals("") ? ContaPagarFiltrosEnum.TODOS.getDescricao() : getTurmaVO().getIdentificadorTurma());

                situacao = "TODAS";

                if (getContaPagarPorTurmaRelVO().getSituacao() != null && getContaPagarPorTurmaRelVO().getSituacao().equals("PA")) {
                    situacao = "Pago";
                } else if (getContaPagarPorTurmaRelVO().getSituacao() != null && getContaPagarPorTurmaRelVO().getSituacao().equals("AP")) {
                    situacao = "A Pagar";
                }

                getSuperParametroRelVO().setSituacao(situacao);

                getSuperParametroRelVO().setFornecedor(getContaPagarPorTurmaRelVO().getFornecedor());

                getSuperParametroRelVO().setFuncionario(getContaPagarPorTurmaRelVO().getFuncionario());

                getSuperParametroRelVO().setBanco(getContaPagarPorTurmaRelVO().getBanco());
                
                getSuperParametroRelVO().setAluno(getContaPagarPorTurmaRelVO().getAluno());
                
                getSuperParametroRelVO().setResponsavelFinanceiro(getContaPagarPorTurmaRelVO().getResponsavelFinanceiro());
                
                getSuperParametroRelVO().setParceiro(getContaPagarPorTurmaRelVO().getParceiro());

                getSuperParametroRelVO().setCategoriaDespesa(
                        getContaPagarPorTurmaRelVO().getCategoriaDespesa() == null ? ContaPagarFiltrosEnum.TODOS.getDescricao() : getContaPagarPorTurmaRelVO().getCategoriaDespesa());

                realizarImpressaoRelatorio();
                removerObjetoMemoria(this);
                inicializarDadosControle();
                setMensagemID("msg_relatorio_ok");
            } else {
                setMensagemID("msg_relatorio_sem_dados");
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            situacao = null;
            Uteis.liberarListaMemoria(listaContaPagarRelVO);
        }

    }

    public void consultarCentroDespesa() {
        try {
            List objs = new ArrayList(0);
            if (getCampoConsultaCentroDespesa().equals("descricao")) {
                objs = getFacadeFactory().getCategoriaDespesaFacade().consultarPorDescricao(getValorConsultaCentroDespesa(), false,
                        Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getCampoConsultaCentroDespesa().equals("identificadorCentroDespesa")) {
                objs = getFacadeFactory().getCategoriaDespesaFacade().consultarPorIdentificadorCategoriaDespesa(getValorConsultaCentroDespesa(), false,
                        Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            setListaConsultaCentroDespesa(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaCentroDespesa(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
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
                objs = getFacadeFactory().getFornecedorFacade().consultarPorCNPJ(getValorConsultaFornecedor(), "AT", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getCampoConsultaFornecedor().equals("CPF")) {
                objs = getFacadeFactory().getFornecedorFacade().consultarPorCPF(getValorConsultaFornecedor(), "AT", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            setListaConsultaFornecedor(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaFornecedor(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public List<SelectItem> getListaFornecedor() {
        List<SelectItem> lista = new ArrayList<SelectItem>(0);
        lista.add(new SelectItem(ContaPagarFiltrosEnum.TODOS.getDescricao(), ContaPagarFiltrosEnum.TODOS.getDescricao()));
        lista.add(new SelectItem(ContaPagarFiltrosEnum.NENHUM.getDescricao(), ContaPagarFiltrosEnum.NENHUM.getDescricao()));
        lista.add(new SelectItem(ContaPagarFiltrosEnum.FILTRAR.getDescricao(), ContaPagarFiltrosEnum.FILTRAR.getDescricao()));
        return lista;
    }

    public List<SelectItem> getListaFuncionario() {
        List<SelectItem> lista = new ArrayList<SelectItem>(0);
        lista.add(new SelectItem(ContaPagarFiltrosEnum.TODOS.getDescricao(), ContaPagarFiltrosEnum.TODOS.getDescricao()));
        lista.add(new SelectItem(ContaPagarFiltrosEnum.NENHUM.getDescricao(), ContaPagarFiltrosEnum.NENHUM.getDescricao()));
        lista.add(new SelectItem(ContaPagarFiltrosEnum.FILTRAR.getDescricao(), ContaPagarFiltrosEnum.FILTRAR.getDescricao()));
        return lista;
    }

    public List<SelectItem> getListaBanco() {
        List<SelectItem> lista = new ArrayList<SelectItem>(0);
        lista.add(new SelectItem(ContaPagarFiltrosEnum.TODOS.getDescricao(), ContaPagarFiltrosEnum.TODOS.getDescricao()));
        lista.add(new SelectItem(ContaPagarFiltrosEnum.NENHUM.getDescricao(), ContaPagarFiltrosEnum.NENHUM.getDescricao()));
        lista.add(new SelectItem(ContaPagarFiltrosEnum.FILTRAR.getDescricao(), ContaPagarFiltrosEnum.FILTRAR.getDescricao()));
        return lista;
    }
    
    public List<SelectItem> getListaAluno() {
        List<SelectItem> lista = new ArrayList<SelectItem>(0);
        lista.add(new SelectItem(ContaPagarFiltrosEnum.TODOS.getDescricao(), ContaPagarFiltrosEnum.TODOS.getDescricao()));
        lista.add(new SelectItem(ContaPagarFiltrosEnum.NENHUM.getDescricao(), ContaPagarFiltrosEnum.NENHUM.getDescricao()));
        lista.add(new SelectItem(ContaPagarFiltrosEnum.FILTRAR.getDescricao(), ContaPagarFiltrosEnum.FILTRAR.getDescricao()));
        return lista;
    }
    
    public List<SelectItem> getListaResponsavelFinanceiro() {
        List<SelectItem> lista = new ArrayList<SelectItem>(0);
        lista.add(new SelectItem(ContaPagarFiltrosEnum.TODOS.getDescricao(), ContaPagarFiltrosEnum.TODOS.getDescricao()));
        lista.add(new SelectItem(ContaPagarFiltrosEnum.NENHUM.getDescricao(), ContaPagarFiltrosEnum.NENHUM.getDescricao()));
        lista.add(new SelectItem(ContaPagarFiltrosEnum.FILTRAR.getDescricao(), ContaPagarFiltrosEnum.FILTRAR.getDescricao()));
        return lista;
    }
    
    public List<SelectItem> getListaParceiro() {
        List<SelectItem> lista = new ArrayList<SelectItem>(0);
        lista.add(new SelectItem(ContaPagarFiltrosEnum.TODOS.getDescricao(), ContaPagarFiltrosEnum.TODOS.getDescricao()));
        lista.add(new SelectItem(ContaPagarFiltrosEnum.NENHUM.getDescricao(), ContaPagarFiltrosEnum.NENHUM.getDescricao()));
        lista.add(new SelectItem(ContaPagarFiltrosEnum.FILTRAR.getDescricao(), ContaPagarFiltrosEnum.FILTRAR.getDescricao()));
        return lista;
    }

    public void validarFiltroFornecedor() {
        if (getContaPagarPorTurmaRelVO().getFornecedor().equals("Filtrar")) {
            setApresentarFiltroFornecedor(true);
        } else {
            setApresentarFiltroFornecedor(false);
        }
    }

    public void validarFiltroFuncionario() {
        if (getContaPagarPorTurmaRelVO().getFuncionario().equals("Filtrar")) {
            setApresentarFiltroFuncionario(true);
        } else {
            setApresentarFiltroFuncionario(false);
        }
    }

    public void validarFiltroBanco() {
        if (getContaPagarPorTurmaRelVO().getBanco().equals("Filtrar")) {
            setApresentarFiltroBanco(true);
        } else {
            setApresentarFiltroBanco(false);
        }
    }
    
    public void validarFiltroAluno() {
        getContaPagarPorTurmaRelVO().getSituacao();
        if (getContaPagarPorTurmaRelVO().getAluno().equals("Filtrar")) {
            setApresentarFiltroAluno(true);
        } else {
            setApresentarFiltroAluno(false);
        }
    }
    
    public void validarFiltroParceiro() {
        getContaPagarPorTurmaRelVO().getSituacao();
        if (getContaPagarPorTurmaRelVO().getParceiro().equals("Filtrar")) {
            setApresentarFiltroParceiro(true);
        } else {
            setApresentarFiltroParceiro(false);
        }
    }
    
    public void validarFiltroResponsavelFinanceiro() {
        getContaPagarPorTurmaRelVO().getSituacao();
        if (getContaPagarPorTurmaRelVO().getResponsavelFinanceiro().equals("Filtrar")) {
            setApresentarFiltroResponsavelFinanceiro(true);
        } else {
            setApresentarFiltroResponsavelFinanceiro(false);
        }
    }
    
    public Boolean getApresentarFiltroAluno() {
        if(apresentarFiltroAluno==null){
            apresentarFiltroAluno = false;
        }
        return apresentarFiltroAluno;
    }

    
    public void setApresentarFiltroAluno(Boolean apresentarFiltroAluno) {
        this.apresentarFiltroAluno = apresentarFiltroAluno;
    }

    
    public Boolean getApresentarFiltroResponsavelFinanceiro() {
        if(apresentarFiltroResponsavelFinanceiro==null){
            apresentarFiltroResponsavelFinanceiro = false;
        }
        return apresentarFiltroResponsavelFinanceiro;
    }

    
    public void setApresentarFiltroResponsavelFinanceiro(Boolean apresentarFiltroResponsavelFinanceiro) {
        this.apresentarFiltroResponsavelFinanceiro = apresentarFiltroResponsavelFinanceiro;
    }

    
    public Boolean getApresentarFiltroParceiro() {
        if(apresentarFiltroParceiro==null){
            apresentarFiltroParceiro = false;
        }
        return apresentarFiltroParceiro;
    }

    
    public void setApresentarFiltroParceiro(Boolean apresentarFiltroParceiro) {
        this.apresentarFiltroParceiro = apresentarFiltroParceiro;
    }


    public void limparDadosFornecedor() {
        getContaPagarPorTurmaRelVO().setNomeFornecedor("");
        getContaPagarPorTurmaRelVO().setFornecedorCpfCnpj("");
        getContaPagarPorTurmaRelVO().setCodigoFornecedor(0);
    }

    public void limparDadosFuncionario() {
        getContaPagarPorTurmaRelVO().setNomeFuncionario("");
        getContaPagarPorTurmaRelVO().setCodigoFuncionario(0);
    }

    public void limparDadosBanco() {
        getContaPagarPorTurmaRelVO().setNomeBanco("");
        getContaPagarPorTurmaRelVO().setCodigoBanco(0);
    }

    public void limparDadosCentroDespesa() {
        getContaPagarPorTurmaRelVO().setCategoriaDespesa("");
        getContaPagarPorTurmaRelVO().setCodigoCategoriaDespesa(0);
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
                objs = getFacadeFactory().getFuncionarioFacade().consultarPorNome(getValorConsultaFuncionario(), getContaPagarPorTurmaRelVO().getUnidadeEnsino(), false,
                        Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("cpf")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultarPorCPF(getValorConsultaFuncionario(), getContaPagarPorTurmaRelVO().getUnidadeEnsino(), false,
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
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("descricao", "Descrição"));
        itens.add(new SelectItem("identificadorCentroDespesa", "Identificador Centro Despesa"));
        return itens;
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
        FornecedorVO obj = (FornecedorVO) context().getExternalContext().getRequestMap().get("fornecedorItem");
        getContaPagarPorTurmaRelVO().setNomeFornecedor(obj.getNome());
        if (obj.getTipoEmpresa().equals("FI")) {
            getContaPagarPorTurmaRelVO().setFornecedorCpfCnpj(obj.getCPF());
        } else {
            getContaPagarPorTurmaRelVO().setFornecedorCpfCnpj(obj.getCNPJ());
        }
        getContaPagarPorTurmaRelVO().setCodigoFornecedor(obj.getCodigo());
        setCampoConsultaFornecedor("");
        setValorConsultaFornecedor("");
        setListaConsultaFornecedor(new ArrayList(0));

    }

    public void selecionarCentroDespesa() {
        CategoriaDespesaVO obj = (CategoriaDespesaVO) context().getExternalContext().getRequestMap().get("centroDespesa");
        getContaPagarPorTurmaRelVO().setCodigoCategoriaDespesa(obj.getCodigo());
        getContaPagarPorTurmaRelVO().setCategoriaDespesa(obj.getDescricao());
        setCampoConsultaCentroDespesa("");
        setValorConsultaCentroDespesa("");
        setListaConsultaCentroDespesa(new ArrayList(0));
    }

    public void selecionarFuncionario() {
        FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItem");
        getContaPagarPorTurmaRelVO().setNomeFuncionario(obj.getPessoa().getNome());
        getContaPagarPorTurmaRelVO().setCodigoFuncionario(obj.getCodigo());
        setCampoConsultaFuncionario("");
        setValorConsultaFuncionario("");
        setListaConsultaFuncionario(new ArrayList(0));
    }

    public void selecionarBanco() {
        BancoVO obj = (BancoVO) context().getExternalContext().getRequestMap().get("bancoItem");
        getContaPagarPorTurmaRelVO().setNomeBanco(obj.getNome());
        getContaPagarPorTurmaRelVO().setCodigoBanco(obj.getCodigo());
        setCampoConsultaBanco("");
        setValorConsultaBanco("");
        setListaConsultaBanco(new ArrayList(0));
    }

    public void inicializarListasSelectItemTodosComboBox() throws Exception {
        montarListaSelectItemOrdenacao();
        montarListaSelectItemUnidadeEnsino();
        montarListaSelectItemTurma();
    }

    public void montarListaSelectItemOrdenacao() {
        Vector opcoes = getFacadeFactory().getContaPagarPorTurmaRelFacade().getOrdenacoesRelatorio();
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
            List<SelectItem> objs = new ArrayList<SelectItem>(0);
            if (super.getUnidadeEnsinoLogado().getCodigo().equals(0)) {
                objs.add(new SelectItem(0, ""));
            }
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

    public void montarListaSelectItemTurma() throws Exception {
        List<SelectItem> objs = new ArrayList<SelectItem>(0);
        objs.add(new SelectItem(0, ""));
        List<TurmaVO> listaTurma = null;
        listaTurma = getFacadeFactory().getTurmaFacade().consultarPorCodigoUnidadeEnsino(getContaPagarPorTurmaRelVO().getUnidadeEnsino(), false,
                Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
        for (TurmaVO turma : listaTurma) {
            objs.add(new SelectItem(turma.getCodigo(), turma.getIdentificadorTurma()));
        }
        setListaSelectItemTurma(objs);
    }

    public boolean isApresentarComboTurma() {
        try {
            return (getUnidadeEnsinoLogado().getCodigo() != 0);
        } catch (Exception e) {
            return false;
        }
    }

    public List consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false,
                Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
        return lista;
    }

    public List<SelectItem> getTipoComboBoxFiltroData() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("dataVencimento", "Data Vencimento"));
        itens.add(new SelectItem("dataCompetencia", "Data Competência"));
        itens.add(new SelectItem("dataPagamento", "Data Pagamento"));
        return itens;
    }
    public List<SelectItem> getTipoComboBoxOrdernarPor() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("turma", "Turma"));
        itens.add(new SelectItem("data", "Data"));
        return itens;
    }
    public List getListaSelectItemSituacaoContaPagar() throws Exception {
        List<SelectItem> objs = new ArrayList<SelectItem>(0);
        objs.add(new SelectItem("", "TODAS"));
        Hashtable pagarRecebidoNegociado = (Hashtable) Dominios.getPagarPagoNegociado();
        Enumeration keys = pagarRecebidoNegociado.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) pagarRecebidoNegociado.get(value);
            objs.add(new SelectItem(value, label));
        }
        SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
        Collections.sort((List) objs, ordenador);
        return objs;
    }

    public String getCampoConsultaCentroDespesa() {
        return campoConsultaCentroDespesa;
    }

    public void setCampoConsultaCentroDespesa(String campoConsultaCentroDespesa) {
        this.campoConsultaCentroDespesa = campoConsultaCentroDespesa;
    }

    public String getCampoConsultaFornecedor() {
        return campoConsultaFornecedor;
    }

    public void setCampoConsultaFornecedor(String campoConsultaFornecedor) {
        this.campoConsultaFornecedor = campoConsultaFornecedor;
    }

    public List getListaConsultaCentroDespesa() {
        return listaConsultaCentroDespesa;
    }

    public void setListaConsultaCentroDespesa(List listaConsultaCentroDespesa) {
        this.listaConsultaCentroDespesa = listaConsultaCentroDespesa;
    }

    public List getListaConsultaFornecedor() {
        return listaConsultaFornecedor;
    }

    public void setListaConsultaFornecedor(List listaConsultaFornecedor) {
        this.listaConsultaFornecedor = listaConsultaFornecedor;
    }

    public String getValorConsultaCentroDespesa() {
        return valorConsultaCentroDespesa;
    }

    public void setValorConsultaCentroDespesa(String valorConsultaCentroDespesa) {
        this.valorConsultaCentroDespesa = valorConsultaCentroDespesa;
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
        return listaSelectItemUnidadeEnsino;
    }

    /**
     * @param listaSelectItemUnidadeEnsino the listaSelectItemUnidadeEnsino to set
     */
    public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
        this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
    }

    /**
     * @return the nomeRelatorio
     */
    public String getNomeRelatorio() {
        return nomeRelatorio;
    }

    /**
     * @param nomeRelatorio the nomeRelatorio to set
     */
    public void setNomeRelatorio(String nomeRelatorio) {
        this.nomeRelatorio = nomeRelatorio;
    }

    /**
     * @return the listaSelectItemTurma
     */
    public List getListaSelectItemTurma() {
        return listaSelectItemTurma;
    }

    /**
     * @param listaSelectItemTurma the listaSelectItemTurma to set
     */
    public void setListaSelectItemTurma(List listaSelectItemTurma) {
        this.listaSelectItemTurma = listaSelectItemTurma;
    }

    public void consultarTurma() {
        try {
            super.consultar();
            List<TurmaVO> objs = new ArrayList<TurmaVO>(0);
            if (getCampoConsultaTurma().equals("identificadorTurma")) {
                objs = getFacadeFactory().getTurmaFacade().consultarPorIdentificadorTurma(getValorConsultaTurma(), getContaPagarPorTurmaRelVO().getUnidadeEnsino(), false,
                        Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaTurma().equals("nomeUnidadeEnsino")) {
                objs = getFacadeFactory().getTurmaFacade().consultarPorNomeUnidadeEnsino(getValorConsultaTurma(), getContaPagarPorTurmaRelVO().getUnidadeEnsino(), false,
                        Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaTurma().equals("nomeTurno")) {
                objs = getFacadeFactory().getTurmaFacade().consultarPorNomeTurno(getValorConsultaTurma(), getContaPagarPorTurmaRelVO().getUnidadeEnsino(), false,
                        Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaTurma().equals("nomeCurso")) {
                objs = getFacadeFactory().getTurmaFacade().consultarPorNomeCurso(getValorConsultaTurma(), getContaPagarPorTurmaRelVO().getUnidadeEnsino(), false,
                        Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            setListaConsultaTurma(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            getListaConsulta().clear();
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public List<SelectItem> getTipoConsultaComboTurma() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("identificadorTurma", "Identificador"));
        itens.add(new SelectItem("nomeUnidadeEnsino", "Unidade Ensino"));
        itens.add(new SelectItem("nomeTurno", "Turno"));
        itens.add(new SelectItem("nomeCurso", "Curso"));
        return itens;
    }

    public void selecionarTurma() {
        try {
            TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItem");
            setTurmaVO(obj);
        } catch (Exception ex) {
            setListaSelectItemTurma(new ArrayList<SelectItem>(0));
            setMensagemDetalhada("msg_erro", ex.getMessage());
        }
    }
    
    public void scrollerListener(DataScrollEvent DataScrollEvent) throws Exception {
        getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
        getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
        consultar();
    }

    public void limparDadosTurma() {
        setTurmaVO(new TurmaVO());
        getTurmaVO().setCurso(new CursoVO());
        getTurmaVO().setTurno(new TurnoVO());
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

    public TurmaVO getTurmaVO() {
        if (turmaVO == null) {
            turmaVO = new TurmaVO();
        }
        return turmaVO;
    }

    public void setTurmaVO(TurmaVO turmaVO) {
        this.turmaVO = turmaVO;
    }

    public ContaPagarPorTurmaRelVO getContaPagarPorTurmaRelVO() {
        if (contaPagarPorTurmaRelVO == null) {
            contaPagarPorTurmaRelVO = new ContaPagarPorTurmaRelVO();
        }
        return contaPagarPorTurmaRelVO;
    }

    public void setContaPagarPorTurmaRelVO(ContaPagarPorTurmaRelVO contaPagarPorTurmaRelVO) {
        this.contaPagarPorTurmaRelVO = contaPagarPorTurmaRelVO;
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
    
    private List listaConsultaParceiro;
    private String valorConsultaParceiro;
    private String campoConsultaParceiro;
    protected String valorConsultaAluno;
    protected String campoConsultaAluno;
    protected List listaConsultaAluno;
    
    public String getCampoConsultaAluno() {
        return campoConsultaAluno;
    }

    public void setCampoConsultaAluno(String campoConsultaAluno) {
        this.campoConsultaAluno = campoConsultaAluno;
    }

    public List getListaConsultaAluno() {
        return listaConsultaAluno;
    }

    public void setListaConsultaAluno(List listaConsultaAluno) {
        this.listaConsultaAluno = listaConsultaAluno;
    }

    public String getValorConsultaAluno() {
        return valorConsultaAluno;
    }

    public void setValorConsultaAluno(String valorConsultaAluno) {
        this.valorConsultaAluno = valorConsultaAluno;
    }
    
    public List<SelectItem> getTipoConsultaComboAluno() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("nomePessoa", "Aluno"));
        itens.add(new SelectItem("matricula", "Matrícula"));
        return itens;
    }

    public void consultarAluno() {
        try {
            List objs = new ArrayList(0);
            if (getValorConsultaAluno().equals("")) {
                setMensagemID("msg_entre_prmconsulta");
                return;
            }
            if (getCampoConsultaAluno().equals("matricula")) {
                objs.add(getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false,
                        Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
            }
            if (getCampoConsultaAluno().equals("nomePessoa")) {
                objs = getFacadeFactory().getMatriculaFacade().consultarPorNomePessoa(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            }
            if (getCampoConsultaAluno().equals("nomeCurso")) {
                objs = getFacadeFactory().getMatriculaFacade().consultarPorNomeCurso(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            }
            if (getCampoConsultaAluno().equals("data")) {
                Date valorData = Uteis.getDate(getValorConsultaAluno());
                objs = getFacadeFactory().getMatriculaFacade().consultarPorData(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59),
                        this.getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
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

    public void selecionarAluno() {
        MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItem");
        getContaPagarPorTurmaRelVO().setMatriculaAluno(obj.getMatricula());
        getContaPagarPorTurmaRelVO().setNomeAluno(obj.getAluno().getNome());
        getContaPagarPorTurmaRelVO().setCodigoAluno(obj.getAluno().getCodigo());
        Uteis.liberarListaMemoria(getListaConsultaAluno());
        campoConsultaAluno = null;
        valorConsultaAluno = null;
    }
    
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
    
    protected List<PessoaVO> listaConsultaResponsavelFinanceiro;
    protected String valorConsultaResponsavelFinanceiro;
    protected String campoConsultaResponsavelFinanceiro;
    
    
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
            PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("responsavelFinanceiro");
            getContaPagarPorTurmaRelVO().setNomeResponsavelFinanceiro(obj.getNome());
            getContaPagarPorTurmaRelVO().setCodigoResponsavelFinanceiro(obj.getCodigo());
            getListaConsultaResponsavelFinanceiro().clear();
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

    public List<SelectItem> getTipoConsultaParceiro() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("razaoSocial", "Razão social"));
        return itens;
    }

    public void selecionarParceiro() {
        ParceiroVO obj = (ParceiroVO) context().getExternalContext().getRequestMap().get("parceiroItem");
        getContaPagarPorTurmaRelVO().setNomeParceiro(obj.getNome());
        getContaPagarPorTurmaRelVO().setCodigoParceiro(obj.getCodigo());
        listaConsultaParceiro.clear();
        this.setValorConsultaParceiro("");
        this.setCampoConsultaParceiro("");
    }
    
    public void limparDadosParceiro(){
        getContaPagarPorTurmaRelVO().setNomeParceiro("");
        getContaPagarPorTurmaRelVO().setCodigoParceiro(0);
    }
    
    public void limparDadosAluno(){
        getContaPagarPorTurmaRelVO().setMatriculaAluno("");
        getContaPagarPorTurmaRelVO().setNomeAluno("");
        getContaPagarPorTurmaRelVO().setCodigoAluno(0);
    }
    
    public void limparDadosResponsavelFinanceiro(){        
        getContaPagarPorTurmaRelVO().setNomeResponsavelFinanceiro("");
        getContaPagarPorTurmaRelVO().setCodigoResponsavelFinanceiro(0);
    }
    
    public String getFiltroAPagar() {
		if (filtroAPagar == null) {
			filtroAPagar = "contapagar.datavencimento";
		}
		return filtroAPagar;
	}

	public void setFiltroAPagar(String filtroAPagar) {
		this.filtroAPagar = filtroAPagar;
	}

	public String getFiltroPago() {
		if (filtroPago == null) {
			filtroPago = "contapagar.datavencimento";
		}
		return filtroPago;
	}

	public void setFiltroPago(String filtroPago) {
		this.filtroPago = filtroPago;
	}

	public String getFiltroPagoParcialmente() {
		if (filtroPagoParcialmente == null) {
			filtroPagoParcialmente = "contapagar.datavencimento";
		}
		return filtroPagoParcialmente;
	}

	public void setFiltroPagoParcialmente(String filtroPagoParcialmente) {
		this.filtroPagoParcialmente = filtroPagoParcialmente;
	}

	public String getFiltroCancelado() {
		if (filtroCancelado == null) {
			filtroCancelado = "contapagar.datavencimento";
		}
		return filtroCancelado;
	}

	public void setFiltroCancelado(String filtroCancelado) {
		this.filtroCancelado = filtroCancelado;
	}

	public List<SelectItem> getListaSelectItemFiltroContaAPaga() {
		if (listaSelectItemFiltroContaAPaga == null) {
			listaSelectItemFiltroContaAPaga = new ArrayList<SelectItem>(0);
			listaSelectItemFiltroContaAPaga.add(new SelectItem("contapagar.datavencimento", "Data Vencimento"));
			listaSelectItemFiltroContaAPaga.add(new SelectItem("contapagar.dataFatoGerador", "Data Competência"));
			listaSelectItemFiltroContaAPaga.add(new SelectItem("naoFiltrar", "Não Filtrar"));
		}
		return listaSelectItemFiltroContaAPaga;
	}

	public void setListaSelectItemFiltroContaAPaga(List<SelectItem> listaSelectItemFiltroContaAPaga) {
		this.listaSelectItemFiltroContaAPaga = listaSelectItemFiltroContaAPaga;
	}

	public List<SelectItem> getListaSelectItemFiltroPaga() {
		if (listaSelectItemFiltroPaga == null) {
			listaSelectItemFiltroPaga = new ArrayList<SelectItem>(0);
			listaSelectItemFiltroPaga.add(new SelectItem("contapagar.datavencimento", "Data Vencimento"));
			listaSelectItemFiltroPaga.add(new SelectItem("negociacaopagamento.data", "Data Pagamento"));
			listaSelectItemFiltroPaga.add(new SelectItem("contapagar.dataFatoGerador", "Data Competência"));
			listaSelectItemFiltroPaga.add(new SelectItem("naoFiltrar", "Não Filtrar"));
		}
		return listaSelectItemFiltroPaga;
	}

	public void setListaSelectItemFiltroPaga(List<SelectItem> listaSelectItemFiltroPaga) {
		this.listaSelectItemFiltroPaga = listaSelectItemFiltroPaga;
	}
}
