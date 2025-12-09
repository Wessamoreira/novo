package controle.contabil;

import java.io.File;
/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas 
 * calculoMesForm.jsp calculoMesCons.jsp) com as funcionalidades da classe <code>calculoMes</code>.
 * Implemtação da camada controle (Backing Bean).
 * @see SuperControle
 * @see calculoMes
 * @see CalculoMesVO
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

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.contabil.CalculoMesVO;
import negocio.comuns.contabil.DREVO;
import negocio.comuns.contabil.FechamentoMesVO;
import negocio.comuns.contabil.PlanoContaVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.contabil.BalancoVO;
@Controller("CalculoMesControle")
@Scope("request")
@Lazy
public class CalculoMesControle extends SuperControleRelatorio implements Serializable {

    private CalculoMesVO calculoMesVO;
    private String campoConsultarPlanoConta;
    private String valorConsultarPlanoConta;
    private List listaConsultarPlanoConta;
    private List<CalculoMesVO> listaCalculoMes;
    protected List listaSelectItemUnidadeEnsino;
    protected Boolean calculado = false;
    protected Date dataIni;
    protected Date dataFim;

    public CalculoMesControle() throws Exception {
        //obterUsuarioLogado();
        setControleConsulta(new ControleConsulta());
        setCalculoMesVO(new CalculoMesVO());
        getCalculoMesVO().setAno(Uteis.getAnoData(new Date()));
        getCalculoMesVO().setMes(Uteis.getMesData(new Date()));
        setMensagemID("msg_entre_prmconsulta");
    }

    /**
     * Rotina responsável por disponibilizar um novo objeto da classe <code>calculoMes</code>
     * para edição pelo usuário da aplicação.
     */
    public String novo() {         removerObjetoMemoria(this);
        setCalculoMesVO(new CalculoMesVO());
        getCalculoMesVO().setAno(Uteis.getAnoData(new Date()));
        getCalculoMesVO().setMes(Uteis.getMesData(new Date()));
        setCalculado(false);
        setMensagemID("msg_entre_dados");
        return "editar";
    }

    /**
     * Rotina responsável por disponibilizar os dados de um objeto da classe <code>calculoMes</code> para alteração.
     * O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa disponibilizá-lo para edição.
     */
    public String editar() throws Exception {
        CalculoMesVO obj = (CalculoMesVO) context().getExternalContext().getRequestMap().get("calculoMes");
        inicializarAtributosRelacionados(obj);
        obj.setNovoObj(Boolean.FALSE);
        setCalculado(true);
        setCalculoMesVO(obj);
        setListaCalculoMes(getFacadeFactory().getCalculoMesFacade().consultarPorAnoMes(obj.getAno(), obj.getMes(), false, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado()));
        setMensagemID("msg_dados_editar");
        return "editar";
    }

    /**
     * Método responsável inicializar objetos relacionados a classe <code>CalculoMesVO</code>.
     * Esta inicialização é necessária por exigência da tecnologia JSF, que não trabalha com valores nulos para estes atributos.
     */
    public void inicializarAtributosRelacionados(CalculoMesVO obj) {
        if (obj.getPlanoConta() == null) {
            obj.setPlanoConta(new PlanoContaVO());
        }
    }

    /**
     * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>calculoMes</code>.
     * Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>.
     * Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de erro.
     */
    public String gravar() {
        try {
            if (calculoMesVO.isNovoObj().booleanValue()) {
                getFacadeFactory().getCalculoMesFacade().incluir(calculoMesVO);
            } else {
                getFacadeFactory().getCalculoMesFacade().alterar(calculoMesVO);
            }
            setMensagemID("msg_dados_gravados");
            return "editar";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "editar";
        }
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP calculoMesCons.jsp.
     * Define o tipo de consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP.
     * Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    public String consultar() {
        try {
            super.consultar();
            List objs = new ArrayList(0);
            if (getControleConsulta().getCampoConsulta().equals("mes")) {
                if (getControleConsulta().getValorConsulta().equals("")) {
                    getControleConsulta().setValorConsulta("0");
                }
                int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
                objs = getFacadeFactory().getCalculoMesFacade().consultarPorMes(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("ano")) {
                if (getControleConsulta().getValorConsulta().equals("")) {
                    getControleConsulta().setValorConsulta("0");
                }
                int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
                objs = getFacadeFactory().getCalculoMesFacade().consultarPorAno(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
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
     * Operação responsável por processar a exclusão um objeto da classe <code>CalculoMesVO</code>
     * Após a exclusão ela automaticamente aciona a rotina para uma nova inclusão.
     */
    public String excluir() {
        try {
            getFacadeFactory().getCalculoMesFacade().excluir(calculoMesVO);
            novo();
            setMensagemID("msg_dados_excluidos");
            return "editar";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "editar";
        }
    }

    public void calcular() {
        try {
            verificarMesFechado();
            setListaCalculoMes(new ArrayList(0));
            setDataIni(Uteis.gerarDataInicioMes(getCalculoMesVO().getMes(), getCalculoMesVO().getAno()));
            setDataFim(Uteis.gerarDataFimMes(getCalculoMesVO().getMes(), getCalculoMesVO().getAno()));

            List<Integer> listaContas = getFacadeFactory().getContabilFacade().obterContasNoPeriodo(getDataIni(), getDataFim(), getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado());
            if (listaContas.size() == 0) {
                throw new Exception("Não existe nenhuma conta para o período informado.");
            } else {
                criarListaCalculoMes(listaContas);
                substituirCalculoExistente();
                gerarItemTabelaFechamento();
                setCalculado(true);
            }
            setMensagemID("msg_dados_calculados");
        } catch (Exception ex) {
            setListaCalculoMes(new ArrayList(0));
            setCalculado(false);
            setMensagemDetalhada("msg_erro", ex.getMessage());
        }

    }

    public void verificarMesFechado() throws Exception {
        if (getFacadeFactory().getFechamentoMesFacade().consultarMesFechado(getCalculoMesVO().getMes(), getCalculoMesVO().getAno(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()) != null) {
            throw new Exception("Este mês já está fechado.");
        }
    }
    public void gerarItemTabelaFechamento() throws Exception {
        if (getFacadeFactory().getFechamentoMesFacade().consultarExisteItemMesAno(getCalculoMesVO().getMes(), getCalculoMesVO().getAno(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()) == null) {
            getFacadeFactory().getFechamentoMesFacade().incluir(montarObjetoFechamentoMes(), false, getUsuarioLogado());
        }
    }
    public FechamentoMesVO montarObjetoFechamentoMes() throws Exception {
        FechamentoMesVO obj = new FechamentoMesVO();
        obj.setAno(getCalculoMesVO().getAno());
        obj.setMes(getCalculoMesVO().getMes());       
        obj.setFechado(false);
        return obj;
    }
    public void criarListaCalculoMes(List<Integer> listaContas) throws Exception {
        for (Integer conta : listaContas) {
            CalculoMesVO calculo = new CalculoMesVO();
            calculo.setValorCredito(getFacadeFactory().getContabilFacade().obterTotalValorPorConta(getDataIni(), getDataFim(), conta, "CR", getUnidadeEnsinoLogado().getCodigo()));
            calculo.setValorDebito(getFacadeFactory().getContabilFacade().obterTotalValorPorConta(getDataIni(), getDataFim(), conta, "DE", getUnidadeEnsinoLogado().getCodigo()));
            calculo.setPlanoConta(getFacadeFactory().getPlanoContaFacade().consultarPorChavePrimaria(conta, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
            calculo.setMes(getCalculoMesVO().getMes());
            calculo.setAno(getCalculoMesVO().getAno());
            calculo.setUnidadeEnsino(getUnidadeEnsinoLogadoClone());
            getListaCalculoMes().add(calculo);
        }
    }

    public void substituirCalculoExistente() throws Exception {
        List<Integer> listaCodigosCalculoAnterior = getFacadeFactory().getCalculoMesFacade().obterCodigosCalculoAnterior(getCalculoMesVO().getMes(), getCalculoMesVO().getAno(), getUnidadeEnsinoLogado().getCodigo());
        for (Integer codigo : listaCodigosCalculoAnterior) {
            CalculoMesVO obj = new CalculoMesVO();
            obj.setCodigo(codigo);
            getFacadeFactory().getCalculoMesFacade().excluir(obj);
        }
        for (CalculoMesVO calculoMes : getListaCalculoMes()) {
            CalculoMesVO obj = new CalculoMesVO();
            obj = calculoMes;
            try {
                getFacadeFactory().getCalculoMesFacade().incluir(obj);
            } catch (Exception e) {
                setMensagemDetalhada("msg_erro", e.getMessage());
            }

        }
    }

    public List gerarLista() {
        try {
            List<BalancoVO> listaBalanco = new ArrayList<BalancoVO>(0);
            List<PlanoContaVO> lista = getFacadeFactory().getPlanoContaFacade().consultarPorPlanoContaPrincipal(0, getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            for (PlanoContaVO nivelUm : lista) {
                BalancoVO balanco1 = gerarObjetoBalanco(nivelUm);
                List<PlanoContaVO> lista2 = getFacadeFactory().getPlanoContaFacade().consultarPorPlanoContaPrincipal(nivelUm.getCodigo(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
                listaBalanco.add(balanco1);
                for (PlanoContaVO nivelDois : lista2) {
                    BalancoVO balanco2 = gerarObjetoBalanco(nivelDois);
                    List<PlanoContaVO> lista3 = getFacadeFactory().getPlanoContaFacade().consultarPorPlanoContaPrincipal(nivelDois.getCodigo(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
                    listaBalanco.add(balanco2);
                    for (PlanoContaVO nivelTres : lista3) {
                        BalancoVO balanco3 = gerarObjetoBalanco(nivelTres);
                        List<PlanoContaVO> lista4 = getFacadeFactory().getPlanoContaFacade().consultarPorPlanoContaPrincipal(nivelTres.getCodigo(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
                        listaBalanco.add(balanco3);
                        for (PlanoContaVO nivelQuatro : lista4) {
                            BalancoVO balanco4 = gerarObjetoBalanco(nivelQuatro);
                            List<PlanoContaVO> lista5 = getFacadeFactory().getPlanoContaFacade().consultarPorPlanoContaPrincipal(nivelQuatro.getCodigo(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
                            listaBalanco.add(balanco4);
                            for (PlanoContaVO nivelCinco : lista5) {
                               BalancoVO balanco5 = gerarObjetoBalanco(nivelCinco);
                                List<PlanoContaVO> lista6 = getFacadeFactory().getPlanoContaFacade().consultarPorPlanoContaPrincipal(nivelCinco.getCodigo(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
                                 listaBalanco.add(balanco5);
                                for (PlanoContaVO nivelSeis : lista6) {
                                    BalancoVO balanco6 = gerarObjetoBalanco(nivelSeis);
                                    List<PlanoContaVO> lista7 = getFacadeFactory().getPlanoContaFacade().consultarPorPlanoContaPrincipal(nivelSeis.getCodigo(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
                                    listaBalanco.add(balanco6);
                                    for (PlanoContaVO nivelSete : lista7) {
                                        BalancoVO balanco7 = gerarObjetoBalanco(nivelSete);
                                        listaBalanco.add(balanco7);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            BalancoVO obj = new BalancoVO();
            obj.setNivelSubordinado(listaBalanco);
            List lista2 = new ArrayList(0);
            lista2.add(obj);
            return lista2;
        } catch (Exception ex) {
            setMensagemDetalhada("msg_erro", ex.getMessage());
            return null;
        }
    }

    public void imprimirDRE() {
        try {
            ControleAcesso.emitirRelatorio(getIdEntidade());
            List lista = new ArrayList(0);
            lista = gerarDRE();
            //mudarCaracterSinal(lista);
            String descricaoFiltros = "Mês: " + getCalculoMesVO().getMes() + "/" + getCalculoMesVO().getAno();

            if (!lista.isEmpty()) {
                String nomeRelatorio = getIdEntidadeDRE();
                String titulo = "Demonstração de Resultados";
                String design = getDesignIReportRelatorioDRE();
                apresentarRelatorioObjetos(nomeRelatorio, titulo, getUnidadeEnsinoLogado().getRazaoSocial(), "", "PDF",
                        "/" + getIdEntidadeDRE() + "/registros", design, getUsuarioLogado().getNome(), descricaoFiltros, lista, getDesignIReportRelatorioDRE());
            } else {
                setMensagemDetalhada("Não Foi possível gerar a DRE.");
            }
            setMensagemID("msg_relatorio_ok");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            removerObjetoMemoria(this);
        }
    }

    public Double calcularValorDRE(List<DREVO> listaDRE) throws Exception {
        Double valor = 0.0;
        for (DREVO obj : listaDRE) {
            valor += getFacadeFactory().getContabilFacade().obterTotalValorPorConta(getDataIni(), getDataFim(), obj.getCodigo(), "DE", getUnidadeEnsinoLogado().getCodigo());
            valor -= getFacadeFactory().getContabilFacade().obterTotalValorPorConta(getDataIni(), getDataFim(), obj.getCodigo(), "CR", getUnidadeEnsinoLogado().getCodigo());
        }
        return valor;
    }

    public Double calcularValorDRE(String identificador, String sinal) throws Exception {
        Double valor = 0.0;
        if (sinal.equals("DE")) {
            valor += getFacadeFactory().getContabilFacade().obterTotalValorPorIdentificadorPlanoContaConsiderandoFilhas(getDataIni(), getDataFim(), identificador, "DE", getUnidadeEnsinoLogado().getCodigo());
            valor = valor * -1;

        } else {
            valor += getFacadeFactory().getContabilFacade().obterTotalValorPorIdentificadorPlanoContaConsiderandoFilhas(getDataIni(), getDataFim(), identificador, "CR", getUnidadeEnsinoLogado().getCodigo());
        }
        return valor;
    }

    public List gerarDRE() throws Exception {
        List<DREVO> lista = getFacadeFactory().getDREFacade().consultarPorUnidadeEnsino(getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
        for (DREVO obj : lista) {
            obj.setValor(calcularValorDRE(obj.getPlanoConta().getIdentificadorPlanoConta(), obj.getSinal()));
        }
        return lista;
    }

    public BalancoVO gerarObjetoBalanco(PlanoContaVO conta) throws Exception {
        setDataIni(Uteis.gerarDataInicioMes(getCalculoMesVO().getMes(), getCalculoMesVO().getAno()));
        setDataFim(Uteis.gerarDataFimMes(getCalculoMesVO().getMes(), getCalculoMesVO().getAno()));
        BalancoVO obj = new BalancoVO();
        obj.setConta(conta);
        obj.setCredito(getFacadeFactory().getContabilFacade().obterTotalValorPorIdentificadorPlanoContaConsiderandoFilhas(getDataIni(), getDataFim(), conta.getIdentificadorPlanoConta(), "CR", getUnidadeEnsinoLogado().getCodigo()));
        obj.setDebito(getFacadeFactory().getContabilFacade().obterTotalValorPorIdentificadorPlanoContaConsiderandoFilhas(getDataIni(), getDataFim(), conta.getIdentificadorPlanoConta(), "DE", getUnidadeEnsinoLogado().getCodigo()));
        Date iniMesAnterior = Uteis.gerarDataInicioMes(getCalculoMesVO().getMes() - 1, getCalculoMesVO().getAno());
        Date fimMesAnterior = Uteis.gerarDataFimMes(getCalculoMesVO().getMes() - 1, getCalculoMesVO().getAno());
        obj.setSaldoAnterior(getFacadeFactory().getContabilFacade().obterTotalValorPorIdentificadorPlanoContaConsiderandoFilhas(iniMesAnterior, fimMesAnterior, conta.getIdentificadorPlanoConta(), "CR", getUnidadeEnsinoLogado().getCodigo()));
        obj.setSaldoAtual(obj.getSaldoAnterior() + obj.getDebito() - obj.getCredito());
        return obj;
    }

    public void imprimirPDF() {    
        try {
            ControleAcesso.emitirRelatorio(getIdEntidade());
            List lista = new ArrayList(0);
            lista = gerarLista();
            String descricaoFiltros = "Mês: " + getCalculoMesVO().getMes() + "/" + getCalculoMesVO().getAno();

            if (!lista.isEmpty()) {
                String nomeRelatorio = getIdEntidade();
                String titulo = "Balancete";
                String design = getDesignIReportRelatorio();
                apresentarRelatorioObjetos(nomeRelatorio, titulo, getUnidadeEnsinoLogado().getRazaoSocial(), "", "PDF",
                        "/" + getIdEntidade() + "/registros", design, getUsuarioLogado().getNome(), descricaoFiltros, lista, getCaminhoBaseRelatorio());
            } else {
                setMensagemDetalhada("Não Foi possível gerar o Balancete.");
            }
            setMensagemID("msg_relatorio_ok");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher
     * o comboBox relativo ao atributo <code>UnidadeEnsino</code>.
     */
    public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
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
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>UnidadeEnsino</code>.
     * Buscando todos os objetos correspondentes a entidade <code>UnidadeEnsino</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é
     * importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemUnidadeEnsino() {
        try {
            montarListaSelectItemUnidadeEnsino("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nomeFantasia</code>
     * Este atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox correspondente
     */
    public List consultarUnidadeEnsinoPorNome(String nomeFantasiaPrm) throws Exception {
        List lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomeFantasiaPrm, 0, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
        return lista;
    }

    /**
     * Método responsável por inicializar a lista de valores (<code>SelectItem</code>) para todos os ComboBox's.
     */
    public void inicializarListasSelectItemTodosComboBox() {
        montarListaSelectItemUnidadeEnsino();
    }

    /**
     * Método responsável por processar a consulta na entidade <code>PlanoConta</code> por meio dos parametros informados no richmodal.
     * Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pelos parâmentros informados no richModal
     * montando automaticamente o resultado da consulta para apresentação.
     */
    public void consultarPlanoConta() {
        try {
            setListaConsultarPlanoConta(getFacadeFactory().getPlanoContaFacade().consultar(getUnidadeEnsinoLogado().getCodigo(), getCampoConsultarPlanoConta(), getCampoConsultarPlanoConta(), false, getUsuarioLogado()));
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultarPlanoConta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarPlanoConta() throws Exception {
        PlanoContaVO obj = (PlanoContaVO) context().getExternalContext().getRequestMap().get("planoConta");
        if (getMensagemDetalhada().equals("")) {
            this.getCalculoMesVO().setPlanoConta(obj);
        }
        this.setValorConsultarPlanoConta(null);
        this.setCampoConsultarPlanoConta(null);
    }

    public void limparCampoPlanoConta() {
        this.getCalculoMesVO().setPlanoConta(new PlanoContaVO());
    }

    /**
     * Rotina responsável por preencher a combo de consulta dos RichModal da telas.
     */
    public List getTipoConsultarComboPlanoConta() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("codigo", "Código"));
        itens.add(new SelectItem("nomeFantasiaUnidadeEnsino", "UnidadeEnsino"));
        itens.add(new SelectItem("planoContaPrincipal", "Plano de Contas Principal"));
        itens.add(new SelectItem("identificadorPlanoConta", "Identificador do Plano de Contas"));
        itens.add(new SelectItem("descricao", "Descrição"));
        return itens;
    }

    /**
     * Rotina responsável por atribui um javascript com o método de mascara para campos do tipo Data, CPF, CNPJ, etc.
     */
    public String getMascaraConsulta() {
        return "";
    }

    /**
     * Rotina responsável por preencher a combo de consulta da telas.
     */
    public List getTipoConsultaCombo() {
        List itens = new ArrayList(0);
        //itens.add(new SelectItem("codigo", "Código"));
        //itens.add(new SelectItem("codigoAuxiliar", "Código Auxiliar"));
        //itens.add(new SelectItem("descricaoPlanoConta", "Plano de Contas"));
        //itens.add(new SelectItem("nomeFantasiaUnidadeEnsino", "UnidadeEnsino"));
        //itens.add(new SelectItem("valorDesconto", "Valor de Desconto"));
        //itens.add(new SelectItem("valorCredito", "Valor de Crédito"));
        itens.add(new SelectItem("mes", "Mês"));
        itens.add(new SelectItem("ano", "Ano"));
        return itens;
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
        calculoMesVO = null;



        setListaSelectItemUnidadeEnsino(new ArrayList(0));
    }

    public List getListaSelectItemUnidadeEnsino() {
        if (listaSelectItemUnidadeEnsino == null) {
            listaSelectItemUnidadeEnsino = new ArrayList(0);
        }
        return (listaSelectItemUnidadeEnsino);
    }

    public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
        this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
    }

    public String getCampoConsultarPlanoConta() {
        return campoConsultarPlanoConta;
    }

    public void setCampoConsultarPlanoConta(String campoConsultarPlanoConta) {
        this.campoConsultarPlanoConta = campoConsultarPlanoConta;
    }

    public String getValorConsultarPlanoConta() {
        return valorConsultarPlanoConta;
    }

    public void setValorConsultarPlanoConta(String valorConsultarPlanoConta) {
        this.valorConsultarPlanoConta = valorConsultarPlanoConta;
    }

    public List getListaConsultarPlanoConta() {
        return listaConsultarPlanoConta;
    }

    public void setListaConsultarPlanoConta(List listaConsultarPlanoConta) {
        this.listaConsultarPlanoConta = listaConsultarPlanoConta;
    }

    public CalculoMesVO getCalculoMesVO() {
        return calculoMesVO;
    }

    public void setCalculoMesVO(CalculoMesVO calculoMesVO) {
        this.calculoMesVO = calculoMesVO;
    }

    public Boolean getCalculado() {
        return calculado;
    }

    public void setCalculado(Boolean calculado) {
        this.calculado = calculado;
    }

    public List<CalculoMesVO> getListaCalculoMes() {
        return listaCalculoMes;
    }

    public void setListaCalculoMes(List<CalculoMesVO> listaCalculoMes) {
        this.listaCalculoMes = listaCalculoMes;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public Date getDataIni() {
        return dataIni;
    }

    public void setDataIni(Date dataIni) {
        this.dataIni = dataIni;
    }

    public static String getIdEntidade() {
        return ("BalancoRel");
    }

    public static String getIdEntidadeDRE() {
        return ("DRERel");
    }

    public static String getDesignIReportRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "contabil" + File.separator + getIdEntidade() + ".jrxml");
    }

    public static String getDesignIReportRelatorioDRE() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "contabil" + File.separator + getIdEntidadeDRE() + ".jrxml");
    }

    public static String getCaminhoBaseRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "contabil" + File.separator);
    }
}
