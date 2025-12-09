package controle.financeiro;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas provisaoCustoForm.jsp
 * provisaoCustoCons.jsp) com as funcionalidades da classe <code>ProvisaoCusto</code>. Implemtação da camada controle
 * (Backing Bean).
 * 
 * @see SuperControle
 * @see ProvisaoCusto
 * @see ProvisaoCustoVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.FluxoCaixaVO;
import negocio.comuns.financeiro.ItensProvisaoVO;
import negocio.comuns.financeiro.MovimentacaoCaixaVO;
import negocio.comuns.financeiro.ProvisaoCustoVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.dominios.SituacaoProvisaoCusto;

@Controller("ProvisaoCustoControle")
@Scope("viewScope")
@Lazy
public class ProvisaoCustoControle extends SuperControle implements Serializable {

    private ProvisaoCustoVO provisaoCustoVO;
    private String responsavel_Erro;
    private String campoConsultarRequisitante;
    private String valorConsultarRequisitante;
    private String situacaoProvisaoCusto;
    private List listaConsultarRequisitante;
    protected List<SelectItem> listaSelectItemContaCorrente;
    protected List<SelectItem> listaSelectItemContaCorrenteTroco;
    private ItensProvisaoVO itensProvisaoVO;
    private MovimentacaoCaixaVO movimentacaoCaixaVO;
    private List<FluxoCaixaVO> fluxoCaixaVOs;

    public ProvisaoCustoControle() throws Exception {
        //obterUsuarioLogado();
        setControleConsulta(new ControleConsulta());
        setMensagemID("msg_entre_prmconsulta");
    }

    /**
     * Rotina responsável por disponibilizar um novo objeto da classe <code>ProvisaoCusto</code> para edição pelo
     * usuário da aplicação.
     */
    public String novo() throws Exception {
        removerObjetoMemoria(this);
        setProvisaoCustoVO(new ProvisaoCustoVO());
        inicializarListasSelectItemTodosComboBox();
        setItensProvisaoVO(new ItensProvisaoVO());
        getProvisaoCustoVO().setResponsavel(getUsuarioLogadoClone());
        setMensagemID("msg_entre_dados");
        return Uteis.getCaminhoRedirecionamentoNavegacao("provisaoCustoForm.xhtml");
    }

    /**
     * Rotina responsável por disponibilizar os dados de um objeto da classe <code>ProvisaoCusto</code> para alteração.
     * O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa
     * disponibilizá-lo para edição.
     */
    public String editar() {
        ProvisaoCustoVO obj = (ProvisaoCustoVO) context().getExternalContext().getRequestMap().get("provisaoCustoItens");
        obj.setNovoObj(Boolean.FALSE);
        setProvisaoCustoVO(montarDadosCompletosProvisaoCustoVO(obj));
        inicializarListasSelectItemTodosComboBox();
        setItensProvisaoVO(new ItensProvisaoVO());
        setMensagemID("msg_dados_editar");
        return Uteis.getCaminhoRedirecionamentoNavegacao("provisaoCustoForm.xhtml");
    }

    public ProvisaoCustoVO montarDadosCompletosProvisaoCustoVO(ProvisaoCustoVO obj) {
        try {
            ProvisaoCustoVO pc = getFacadeFactory().getProvisaoCustoFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            if (obj != null) {
                return pc;
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
        return new ProvisaoCustoVO();
    }

    /**
     * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>ProvisaoCusto</code>.
     * Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é
     * acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado
     * para o usuário juntamente com uma mensagem de erro.
     */
    public String gravar() {
        try {
            if (provisaoCustoVO.isNovoObj().booleanValue()) {
                getFacadeFactory().getProvisaoCustoFacade().incluir(provisaoCustoVO, getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema());
            } else {
                getFacadeFactory().getProvisaoCustoFacade().alterar(provisaoCustoVO, getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema());
            }
            setMensagemID("msg_dados_gravados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("provisaoCustoForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("provisaoCustoForm.xhtml");
        }
    }

    /**
     * criado para atender as especificacoes de qualidade
     * - Ao clicar em graver primeiro é apresentado o modal de confirmação e somente dempois é validado os campos obrigatórios;
     * 
     * @return
     */
    public String realizarValidacaoDadosAntesGravar() {
        try {
            ProvisaoCustoVO.validarDados(getProvisaoCustoVO());
            return "";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("provisaoCustoForm.xhtml");
        }
    }

    public boolean isApresentarCamposProvisaoFinalizada() {
        return SituacaoProvisaoCusto.FINALIZADO.getValor().equals(getProvisaoCustoVO().getSituacao());
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP ProvisaoCustoCons.jsp. Define o tipo de consulta
     * a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como resultado,
     * disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    public String consultar() {
        try {
            super.consultar();
            List objs = new ArrayList(0);
            if (getControleConsulta().getCampoConsulta().equals("codigo")) {
                int valorInt = 0;
                if (!getControleConsulta().getValorConsulta().equals("")) {
                    valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
                }
                objs = getFacadeFactory().getProvisaoCustoFacade().consultarPorCodigo(new Integer(valorInt), getSituacaoProvisaoCusto(), getControleConsulta().getDataIni(),
                        getControleConsulta().getDataFim(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("nomeUsuario")) {
                objs = getFacadeFactory().getProvisaoCustoFacade().consultarPorNomeUsuario(getControleConsulta().getValorConsulta(), getSituacaoProvisaoCusto(), getControleConsulta().getDataIni(),
                        getControleConsulta().getDataFim(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("nomeFuncionario")) {
                objs = getFacadeFactory().getProvisaoCustoFacade().consultarPorNomeFuncionario(getControleConsulta().getValorConsulta(), getSituacaoProvisaoCusto(),
                        getControleConsulta().getDataIni(), getControleConsulta().getDataFim(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            setListaConsulta(objs);
            setMensagemID("msg_dados_consultados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("provisaoCustoCons.xhtml");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("provisaoCustoCons.xhtml");
        }
    }

    /**
     * Operação responsável por processar a exclusão um objeto da classe <code>ProvisaoCustoVO</code> Após a exclusão
     * ela automaticamente aciona a rotina para uma nova inclusão.
     */
    public String excluir() {
        try {

            getFacadeFactory().getProvisaoCustoFacade().excluir(provisaoCustoVO, getUsuarioLogado());
            novo();
            setMensagemID("msg_dados_excluidos");
            return Uteis.getCaminhoRedirecionamentoNavegacao("provisaoCustoForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("provisaoCustoForm.xhtml");
        }
    }

    /*
     * Método responsável por adicionar um novo objeto da classe <code>ItensProvisao</code> para o objeto
     * <code>provisaoCustoVO</code> da classe <code>ProvisaoCusto</code>
     */
    public String adicionarItensProvisao() throws Exception {
        try {
            if (!getProvisaoCustoVO().getCodigo().equals(0)) {
                itensProvisaoVO.setProvisaoCusto(getProvisaoCustoVO().getCodigo());
            }
            getProvisaoCustoVO().adicionarObjItensProvisaoVOs(getItensProvisaoVO());
            this.setItensProvisaoVO(new ItensProvisaoVO());
            setMensagemID("msg_dados_adicionados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("provisaoCustoForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("provisaoCustoForm.xhtml");
        }
    }

    /*
     * Método responsável por disponibilizar dados de um objeto da classe <code>ItensProvisao</code> para edição pelo
     * usuário.
     */
    public String editarItensProvisao() throws Exception {
        ItensProvisaoVO obj = (ItensProvisaoVO) context().getExternalContext().getRequestMap().get("itensProvisaoItens");
        setItensProvisaoVO(obj);
        return Uteis.getCaminhoRedirecionamentoNavegacao("provisaoCustoForm.xhtml");
    }

    /*
     * Método responsável por remover um novo objeto da classe <code>ItensProvisao</code> do objeto
     * <code>provisaoCustoVO</code> da classe <code>ProvisaoCusto</code>
     */
    public String removerItensProvisao() throws Exception {
        ItensProvisaoVO obj = (ItensProvisaoVO) context().getExternalContext().getRequestMap().get("itensProvisaoItens");
        getProvisaoCustoVO().excluirObjItensProvisaoVOs(obj.getDescricao());
        setMensagemID("msg_dados_excluidos");
        return Uteis.getCaminhoRedirecionamentoNavegacao("provisaoCustoForm.xhtml");
    }

    /*
     * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo
     * <code>situacao</code>
     */
    public List getListaSelectItemSituacaoProvisaoCusto() throws Exception {
        List objs = new ArrayList(0);
        objs = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(SituacaoProvisaoCusto.class);
        SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
        Collections.sort((List) objs, ordenador);
        return objs;
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
     * relativo ao atributo <code>FluxoCaixaTroco</code>.
     */
    public void montarListaSelectItemContaCorrenteTroco(List<FluxoCaixaVO> fluxoCaixaVOs) throws Exception {
        try {
        	getListaSelectItemContaCorrenteTroco().clear();
        	getListaSelectItemContaCorrenteTroco().add(new SelectItem(0, ""));
        	getListaSelectItemContaCorrenteTroco().addAll(fluxoCaixaVOs.stream()
             	.map(f -> new SelectItem(f.getContaCaixa().getCodigo(), f.getContaCaixa().getDescricaoParaComboBox()))
             	.collect(Collectors.toList()));
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>ContaCorrenteTrocoS</code>. Buscando todos
     * os objetos correspondentes a entidade <code>ContaCorrente</code>. Esta rotina não recebe parâmetros para
     * filtragem de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio
     * requisições Ajax.
     */
    public void montarListaSelectItemContaCorrenteTroco() {
        try {
        	if (getFluxoCaixaVOs().isEmpty()) {
        		setFluxoCaixaVOs(consultarFluxoCaixaPorNumero(""));
        	}
            montarListaSelectItemContaCorrenteTroco(getFluxoCaixaVOs());
        } catch (Exception e) {
        	setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
     * relativo ao atributo <code>FluxoCaixa</code>.
     */
    public void montarListaSelectItemContaCorrente(List<FluxoCaixaVO> fluxoCaixaVOs) throws Exception {
        try {
        	getListaSelectItemContaCorrente().clear();
            getListaSelectItemContaCorrente().add(new SelectItem(0, ""));
            getListaSelectItemContaCorrente().addAll(fluxoCaixaVOs.stream()
            	.map(f -> new SelectItem(f.getContaCaixa().getCodigo(), f.getContaCaixa().getDescricaoParaComboBox()))
            	.collect(Collectors.toList()));
        } catch (Exception e) {
            throw e;
        }
    }

    public UnidadeEnsinoVO getUnidadeEnsino(Integer codigo) throws Exception {
        UnidadeEnsinoVO obj = (UnidadeEnsinoVO) getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(codigo, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
        return (obj != null) ? obj : new UnidadeEnsinoVO();
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>ContaCorrente</code>. Buscando todos os
     * objetos correspondentes a entidade <code>ContaCorrente</code>. Esta rotina não recebe parâmetros para filtragem
     * de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemContaCorrente() {
        try {
        	if (getFluxoCaixaVOs().isEmpty()) {
        		setFluxoCaixaVOs(consultarFluxoCaixaPorNumero(""));
        	} 
       		montarListaSelectItemContaCorrente(getFluxoCaixaVOs());
        } catch (Exception e) {
        	 setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>numero</code> Este
     * atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox
     * correspondente
     */
    public List<FluxoCaixaVO> consultarFluxoCaixaPorNumero(String numeroPrm) throws Exception {
        return getFacadeFactory().getFluxoCaixaFacade()
        		.consultarPorNumeroContaCorrente(numeroPrm, "A", true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
        		
    }

    /**
     * Método responsável por inicializar a lista de valores (<code>SelectItem</code>) para todos os ComboBox's.
     */
	public void inicializarListasSelectItemTodosComboBox() {
		try {
			setFluxoCaixaVOs(consultarFluxoCaixaPorNumero(""));
			montarListaSelectItemContaCorrente(getFluxoCaixaVOs());
			montarListaSelectItemContaCorrenteTroco(getFluxoCaixaVOs());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

    /**
     * Método responsável por processar a consulta na entidade <code>Funcionario</code> por meio dos parametros
     * informados no richmodal. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pelos
     * parâmentros informados no richModal montando automaticamente o resultado da consulta para apresentação.
     */
    public void consultarRequisitante() {
        try {
            List objs = new ArrayList(0);
            if (getCampoConsultarRequisitante().equals("codigo")) {
                if (getValorConsultarRequisitante().equals("")) {
                    setValorConsultarRequisitante("0");
                }
                int valorInt = Integer.parseInt(getValorConsultarRequisitante());
                objs = getFacadeFactory().getFuncionarioFacade().consultarPorCodigo(new Integer(valorInt), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultarRequisitante().equals("nomeUnidadeEnsino")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeUnidadeEnsino(getValorConsultarRequisitante(), "FU", this.getUnidadeEnsinoLogado().getCodigo(), false,
                        Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultarRequisitante().equals("nomeDepartamento")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeDepartamento(getValorConsultarRequisitante(), "FU", this.getUnidadeEnsinoLogado().getCodigo(), false,
                        Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultarRequisitante().equals("nomeCargo")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeCargo(getValorConsultarRequisitante(), this.getUnidadeEnsinoLogado().getCodigo(), false,
                        Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultarRequisitante().equals("dataAdmissao")) {
                Date valorData = Uteis.getDate(getValorConsultarRequisitante());
                objs = getFacadeFactory().getFuncionarioFacade().consultarPorDataAdmissao(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59),
                        this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultarRequisitante().equals("nomePessoa")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultarPorNome(getValorConsultarRequisitante(), this.getUnidadeEnsinoLogado().getCodigo(), false,
                        Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultarRequisitante().equals("matricula")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultarPorMatricula(getValorConsultarRequisitante(), this.getUnidadeEnsinoLogado().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultarRequisitante().equals("cpf")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultarPorCPF(getValorConsultarRequisitante(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            setListaConsultarRequisitante(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultarRequisitante(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarRequisitante() throws Exception {
        FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItens");
        obj = getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimaria(obj.getCodigo(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
        if (getMensagemDetalhada().equals("")) {
            this.getProvisaoCustoVO().setRequisitante(obj);
        }
        Uteis.liberarListaMemoria(this.getListaConsultarRequisitante());
        this.setValorConsultarRequisitante(null);
        this.setCampoConsultarRequisitante(null);
    }

    public void limparCampoRequisitante() {
        this.getProvisaoCustoVO().setRequisitante(new FuncionarioVO());
    }

    /**
     * Rotina responsável por preencher a combo de consulta dos RichModal da telas.
     */
    public List getTipoConsultarComboRequisitante() {
        List itens = new ArrayList(0);
        
        itens.add(new SelectItem("nomePessoa", "Nome"));
        itens.add(new SelectItem("cpf", "CPF"));
        itens.add(new SelectItem("matricula", "Matrícula"));
        return itens;
    }

    /**
     * Método responsável por processar a consulta na entidade <code>Usuario</code> por meio de sua respectiva chave
     * primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave primária
     * da entidade montando automaticamente o resultado da consulta para apresentação.
     */
    public void consultarUsuarioPorChavePrimaria() {
        try {
            Integer campoConsulta = provisaoCustoVO.getResponsavel().getCodigo();
            UsuarioVO usuario = getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(campoConsulta, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            provisaoCustoVO.getResponsavel().setNome(usuario.getNome());
            this.setResponsavel_Erro("");
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemID("msg_erro_dadosnaoencontrados");
            provisaoCustoVO.getResponsavel().setNome("");
            provisaoCustoVO.getResponsavel().setCodigo(0);
            this.setResponsavel_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
        }
    }

    /**
     * Rotina responsável por atribui um javascript com o método de mascara para campos do tipo Data, CPF, CNPJ, etc.
     */
    public String getMascaraConsulta() {
        if (getControleConsulta().getCampoConsulta().equals("data")) {
            return "return mascara(this.form, 'form:valorConsulta', '99/99/9999', event);";
        }
        return "";
    }

    /**
     * Rotina responsável por preencher a combo de consulta da telas.
     */
    public List getTipoConsultaCombo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nomeFuncionario", "Requisitante"));
        itens.add(new SelectItem("codigo", "Número"));
        itens.add(new SelectItem("nomeUsuario", "Responsável"));
        return itens;
    }

    public boolean isConsultaPorCodigo() {
        return "codigo".equals(getControleConsulta().getCampoConsulta());
    }

    /**
     * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
     */
    public String inicializarConsultar() {
        removerObjetoMemoria(this);
        setListaConsulta(new ArrayList(0));
        setMensagemID("msg_entre_prmconsulta");
        return Uteis.getCaminhoRedirecionamentoNavegacao("provisaoCustoCons.xhtml");
    }

    /**
     * Operação que libera todos os recursos (atributos, listas, objetos) do backing bean. Garantindo uma melhor atuação
     * do Garbage Coletor do Java. A mesma é automaticamente quando realiza o logout.
     */
    protected void limparRecursosMemoria() {
        super.limparRecursosMemoria();
        provisaoCustoVO = null;
        responsavel_Erro = null;
        Uteis.liberarListaMemoria(listaSelectItemContaCorrente);
        Uteis.liberarListaMemoria(listaSelectItemContaCorrenteTroco);
        itensProvisaoVO = null;
    }

    public ItensProvisaoVO getItensProvisaoVO() {
        return itensProvisaoVO;
    }

    public void setItensProvisaoVO(ItensProvisaoVO itensProvisaoVO) {
        this.itensProvisaoVO = itensProvisaoVO;
    }

    public List<SelectItem> getListaSelectItemContaCorrenteTroco() {
        if (listaSelectItemContaCorrenteTroco == null) {
            listaSelectItemContaCorrenteTroco = new ArrayList<>(0);
        }
        return (listaSelectItemContaCorrenteTroco);
    }

    public void setListaSelectItemContaCorrenteTroco(List<SelectItem> listaSelectItemContaCorrenteTroco) {
        this.listaSelectItemContaCorrenteTroco = listaSelectItemContaCorrenteTroco;
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

    public String getCampoConsultarRequisitante() {
        return campoConsultarRequisitante;
    }

    public void setCampoConsultarRequisitante(String campoConsultarRequisitante) {
        this.campoConsultarRequisitante = campoConsultarRequisitante;
    }

    public String getValorConsultarRequisitante() {
        return valorConsultarRequisitante;
    }

    public void setValorConsultarRequisitante(String valorConsultarRequisitante) {
        this.valorConsultarRequisitante = valorConsultarRequisitante;
    }

    public List getListaConsultarRequisitante() {
        return listaConsultarRequisitante;
    }

    public void setListaConsultarRequisitante(List listaConsultarRequisitante) {
        this.listaConsultarRequisitante = listaConsultarRequisitante;
    }

    public String getResponsavel_Erro() {
        return responsavel_Erro;
    }

    public void setResponsavel_Erro(String responsavel_Erro) {
        this.responsavel_Erro = responsavel_Erro;
    }

    public ProvisaoCustoVO getProvisaoCustoVO() {
        return provisaoCustoVO;
    }

    public void setProvisaoCustoVO(ProvisaoCustoVO provisaoCustoVO) {
        this.provisaoCustoVO = provisaoCustoVO;
    }

    /**
     * @return the movimentacaoCaixaVO
     */
    public MovimentacaoCaixaVO getMovimentacaoCaixaVO() {
        return movimentacaoCaixaVO;
    }

    /**
     * @param movimentacaoCaixaVO
     *            the movimentacaoCaixaVO to set
     */
    public void setMovimentacaoCaixaVO(MovimentacaoCaixaVO movimentacaoCaixaVO) {
        this.movimentacaoCaixaVO = movimentacaoCaixaVO;
    }

    /**
     * @return the situacaoProvisaoCusto
     */
    public String getSituacaoProvisaoCusto() {
        return situacaoProvisaoCusto;
    }

    /**
     * @param situacaoProvisaoCusto
     *            the situacaoProvisaoCusto to set
     */
    public void setSituacaoProvisaoCusto(String situacaoProvisaoCusto) {
        this.situacaoProvisaoCusto = situacaoProvisaoCusto;
    }

	public List<FluxoCaixaVO> getFluxoCaixaVOs() {
		if (fluxoCaixaVOs == null) {
			fluxoCaixaVOs = new ArrayList<FluxoCaixaVO>(0);
		}
		return fluxoCaixaVOs;
	}

	public void setFluxoCaixaVOs(List<FluxoCaixaVO> fluxoCaixaVOs) {
		this.fluxoCaixaVOs = fluxoCaixaVOs;
	}
}