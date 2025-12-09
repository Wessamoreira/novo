package controle.compras;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas fornecedorForm.jsp
 * fornecedorCons.jsp) com as funcionalidades da classe <code>Fornecedor</code>. Implemtação da camada controle (Backing
 * Bean).
 * 
 * @see SuperControle
 * @see Fornecedor
 * @see FornecedorVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.compras.CategoriaProdutoVO;
import negocio.comuns.compras.FornecedorCategoriaProdutoVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.financeiro.BancoVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis;

@Controller("FornecedorControle")
@Scope("viewScope")
@Lazy
public class FornecedorControle extends SuperControle implements Serializable {

    private FornecedorVO fornecedorVO;
    private List listaConsultaCidade;
    private String campoConsultaCidade;
    private String valorConsultaCidade;
    protected List listaSelectItemCategoriaProduto;
    private FornecedorCategoriaProdutoVO fornecedorCategoriaProdutoVO;
    private List<SelectItem> listaSelectItemBancos;
    
    

    public FornecedorControle() throws Exception {
        //obterUsuarioLogado();
        setControleConsulta(new ControleConsulta());
        setMensagemID("msg_entre_prmconsulta");
    }

    /**
     * Rotina responsável por disponibilizar um novo objeto da classe <code>Fornecedor</code> para edição pelo usuário
     * da aplicação.
     */
    public String novo(){
    	try {
	        registrarAtividadeUsuario(getUsuarioLogado(), "FornecedorControle", "Novo Fornecedor", "Novo");
	        removerObjetoMemoria(this);
	        setFornecedorVO(new FornecedorVO());
	        setFornecedorCategoriaProdutoVO(new FornecedorCategoriaProdutoVO());
	        inicializarListasSelectItemTodosComboBox();
	        setMensagemID("msg_entre_dados");
	        return Uteis.getCaminhoRedirecionamentoNavegacao("fornecedorForm");
    	} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
    	return "";
    }
    
    public void limpar() {
    	getFornecedorVO().setIsTemMei(false);
    	getFornecedorVO().setCPF("");
    	getFornecedorVO().setCpfFornecedor("");
    	getFornecedorVO().setNomePessoaFisica("");
    }

    /**
     * Rotina responsável por disponibilizar os dados de um objeto da classe <code>Fornecedor</code> para alteração. O
     * objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa
     * disponibilizá-lo para edição.
     */
    public String editar() throws Exception {
        registrarAtividadeUsuario(getUsuarioLogado(), "FornecedorControle", "Inicializando Editar Fornecedor", "Editando");
        FornecedorVO obj = (FornecedorVO) context().getExternalContext().getRequestMap().get("fornecedorItem");
        obj = montarDadosFornecedorVOCompletos(obj);
        obj.setNovoObj(Boolean.FALSE);        
       
        setFornecedorVO(obj);
        inicializarListasSelectItemTodosComboBox();
        setFornecedorCategoriaProdutoVO(new FornecedorCategoriaProdutoVO());
        registrarAtividadeUsuario(getUsuarioLogado(), "FornecedorControle", "Finalizando Editar Fornecedor", "Editando");
        setMensagemID("msg_dados_editar");
        return Uteis.getCaminhoRedirecionamentoNavegacao("fornecedorForm");
    }

    private FornecedorVO montarDadosFornecedorVOCompletos(FornecedorVO obj) {
        try {
            return getFacadeFactory().getFornecedorFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
        } catch (Exception e) {
        }
        return new FornecedorVO();
    }

    /**
     * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>Fornecedor</code>. Caso o
     * objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é
     * acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado
     * para o usuário juntamente com uma mensagem de erro.
     */
    public String gravar() {
        try {
        	adicionarMascaraCPF();
        	
            if (fornecedorVO.isNovoObj().booleanValue()) {
                registrarAtividadeUsuario(getUsuarioLogado(), "FornecedorControle", "Inicializando Incluir Fornecedor", "Incluindo");
                getFacadeFactory().getFornecedorFacade().incluir(fornecedorVO, getUsuarioLogado());
                registrarAtividadeUsuario(getUsuarioLogado(), "FornecedorControle", "Finalizando Incluir Fornecedor", "Incluindo");
            } else {
                registrarAtividadeUsuario(getUsuarioLogado(), "FornecedorControle", "Inicializando Alterar Fornecedor", "Alterando");
                getFacadeFactory().getFornecedorFacade().alterar(fornecedorVO, getUsuarioLogado());
                registrarAtividadeUsuario(getUsuarioLogado(), "FornecedorControle", "Inicializando Alterar Fornecedor", "Alterando");
            }
            setMensagemID("msg_dados_gravados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("fornecedorForm");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("fornecedorForm");
        }
    }

    public String inativar() {
        try {
            if (!fornecedorVO.isNovoObj().booleanValue()) {
                getFornecedorVO().setSituacao("IN");
                getFacadeFactory().getFornecedorFacade().alterarSituacao(fornecedorVO, getUsuarioLogado());                
            }
            setMensagemID("msg_dados_gravados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("fornecedorForm");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("fornecedorForm");
        }
    }
    
    public String ativar() {
        try {
            if (!fornecedorVO.isNovoObj().booleanValue()) {
                getFornecedorVO().setSituacao("AT");
                getFacadeFactory().getFornecedorFacade().alterarSituacao(fornecedorVO, getUsuarioLogado());                
            }
            setMensagemID("msg_dados_gravados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("fornecedorForm");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("fornecedorForm");
        }
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP FornecedorCons.jsp. Define o tipo de consulta a
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
                int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
                registrarAtividadeUsuario(getUsuarioLogado(), "FornecedorControle", "Inicializando Consultar Fornecedor", "Consultando");
                objs = getFacadeFactory().getFornecedorFacade().consultarPorCodigo(new Integer(valorInt), "", true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
                registrarAtividadeUsuario(getUsuarioLogado(), "FornecedorControle", "Finalizando Excluir Fornecedor", "Consultando");
            }
            if (getControleConsulta().getCampoConsulta().equals("nome")) {
                registrarAtividadeUsuario(getUsuarioLogado(), "FornecedorControle", "Inicializando Consultar Fornecedor", "Consultando");
                objs = getFacadeFactory().getFornecedorFacade().consultarPorNome(getControleConsulta().getValorConsulta(), "", true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
                registrarAtividadeUsuario(getUsuarioLogado(), "FornecedorControle", "Finalizando Excluir Fornecedor", "Consultando");
            }
            if (getControleConsulta().getCampoConsulta().equals("razaoSocial")) {
                registrarAtividadeUsuario(getUsuarioLogado(), "FornecedorControle", "Inicializando Consultar Fornecedor", "Consultando");
                objs = getFacadeFactory().getFornecedorFacade().consultarPorRazaoSocial(getControleConsulta().getValorConsulta(), "", true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
                registrarAtividadeUsuario(getUsuarioLogado(), "FornecedorControle", "Finalizando Excluir Fornecedor", "Consultando");
            }
            if (getControleConsulta().getCampoConsulta().equals("nomeCidade")) {
                registrarAtividadeUsuario(getUsuarioLogado(), "FornecedorControle", "Inicializando Consultar Fornecedor", "Consultando");
                objs = getFacadeFactory().getFornecedorFacade().consultarPorNomeCidade(getControleConsulta().getValorConsulta(), "", true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
                registrarAtividadeUsuario(getUsuarioLogado(), "FornecedorControle", "Finalizando Excluir Fornecedor", "Consultando");
            }
            if (getControleConsulta().getCampoConsulta().equals("CNPJ")) {
                registrarAtividadeUsuario(getUsuarioLogado(), "FornecedorControle", "Inicializando Consultar Fornecedor", "Consultando");
                objs = getFacadeFactory().getFornecedorFacade().consultarPorCNPJ(getControleConsulta().getValorConsulta(), "", true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
                registrarAtividadeUsuario(getUsuarioLogado(), "FornecedorControle", "Finalizando Excluir Fornecedor", "Consultando");
            }
            if (getControleConsulta().getCampoConsulta().equals("inscEstadual")) {
                registrarAtividadeUsuario(getUsuarioLogado(), "FornecedorControle", "Inicializando Consultar Fornecedor", "Consultando");
                objs = getFacadeFactory().getFornecedorFacade().consultarPorInscEstadual(getControleConsulta().getValorConsulta(), "", true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
                registrarAtividadeUsuario(getUsuarioLogado(), "FornecedorControle", "Finalizando Excluir Fornecedor", "Consultando");
            }
            if (getControleConsulta().getCampoConsulta().equals("RG")) {
                registrarAtividadeUsuario(getUsuarioLogado(), "FornecedorControle", "Inicializando Consultar Fornecedor", "Consultando");
                objs = getFacadeFactory().getFornecedorFacade().consultarPorRG(getControleConsulta().getValorConsulta(), "", true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
                registrarAtividadeUsuario(getUsuarioLogado(), "FornecedorControle", "Finalizando Excluir Fornecedor", "Consultando");
            }
            if (getControleConsulta().getCampoConsulta().equals("CPF")) {
                registrarAtividadeUsuario(getUsuarioLogado(), "FornecedorControle", "Inicializando Consultar Fornecedor", "Consultando");
                objs = getFacadeFactory().getFornecedorFacade().consultarPorCPF(getControleConsulta().getValorConsulta(), "", true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
                registrarAtividadeUsuario(getUsuarioLogado(), "FornecedorControle", "Finalizando Excluir Fornecedor", "Consultando");
            }
            setListaConsulta(objs);
            setMensagemID("msg_dados_consultados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("fornecedorCons");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("fornecedorCons");
        }
    }

    /**
     * Operação responsável por processar a exclusão um objeto da classe <code>FornecedorVO</code> Após a exclusão ela
     * automaticamente aciona a rotina para uma nova inclusão.
     */
    public String excluir() {
        try {
            registrarAtividadeUsuario(getUsuarioLogado(), "FornecedorControle", "Inicializando Excluir Fornecedor", "Excluindo");
            getFacadeFactory().getFornecedorFacade().excluir(fornecedorVO, getUsuarioLogado());
            setFornecedorVO(new FornecedorVO());
            registrarAtividadeUsuario(getUsuarioLogado(), "FornecedorControle", "Finalizando Excluir Fornecedor", "Excluindo");
            setMensagemID("msg_dados_excluidos");
            return Uteis.getCaminhoRedirecionamentoNavegacao("fornecedorForm");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("fornecedorForm");
        }
    }

    public void removerFornecedorCategoriaProduto() {
        FornecedorCategoriaProdutoVO obj = (FornecedorCategoriaProdutoVO) context().getExternalContext().getRequestMap().get("fornecedorCategoriaProdutoItem");
        getFornecedorVO().removerObjFornecedorCategoriaProdutoVOs(obj);
        setMensagemID("msg_dados_excluidos");
    }

    public void adicionarFornecedorCategoriaProduto() {
        try {
            if (getFornecedorVO().getCodigo().intValue() != 0) {
                getFornecedorCategoriaProdutoVO().setFornecedor(getFornecedorVO().getCodigo());
            }
            if (getFornecedorCategoriaProdutoVO().getCategoriaProdutoVO().getCodigo().intValue() != 0) {
                CategoriaProdutoVO obj = getFacadeFactory().getCategoriaProdutoFacade().consultarPorChavePrimaria(fornecedorCategoriaProdutoVO.getCategoriaProdutoVO().getCodigo(), false,
                        Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
                getFornecedorCategoriaProdutoVO().setCategoriaProdutoVO(obj);
            }
            getFornecedorVO().adicionarObjFornecedorCategoriaProdutoVOs(fornecedorCategoriaProdutoVO);
            setFornecedorCategoriaProdutoVO(new FornecedorCategoriaProdutoVO());
            setMensagemID("msg_dados_adicionados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void irPaginaInicial() throws Exception {
        // controleConsulta.setPaginaAtual(1);
        this.consultar();
    }

    public void irPaginaAnterior() throws Exception {
        // controleConsulta.setPaginaAtual(controleConsulta.getPaginaAtual() - 1);
        this.consultar();
    }

    public void irPaginaPosterior() throws Exception {
        // controleConsulta.setPaginaAtual(controleConsulta.getPaginaAtual() + 1);
        this.consultar();
    }

    public void irPaginaFinal() throws Exception {
        // controleConsulta.setPaginaAtual(controleConsulta.getNrTotalPaginas());
        this.consultar();
    }

    /**
     * Método responsável por consultar Cidade <code>Cidade/code>.
     * Buscando todos os objetos correspondentes a entidade <code>Cidade</code>. Esta rotina não recebe parâmetros para
     * filtragem de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio
     * requisições Ajax.
     */
    public void consultarCidade() {
        try {
            List objs = new ArrayList(0);
            if (getCampoConsultaCidade().equals("codigo")) {
                if (getValorConsultaCidade().equals("")) {
                    setValorConsultaCidade("0");
                }
                int valorInt = Integer.parseInt(getValorConsultaCidade());
                objs = getFacadeFactory().getCidadeFacade().consultarPorCodigo(new Integer(valorInt), false, getUsuarioLogado());
            }
            if (getCampoConsultaCidade().equals("nome")) {
                objs = getFacadeFactory().getCidadeFacade().consultarPorNome(getValorConsultaCidade(), false, getUsuarioLogado());
            }
            setListaConsultaCidade(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaCidade(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    /**
     * Método responsável por selecionar o objeto CidadeVO <code>Cidade/code>.
     */
    public void selecionarCidade() {
        CidadeVO obj = (CidadeVO) context().getExternalContext().getRequestMap().get("cidadeItem");
        getFornecedorVO().setCidade(obj);
        getListaConsultaCidade().clear();
        this.setValorConsultaCidade("");
        this.setCampoConsultaCidade("");
    }

    /**
     * Método responsável por carregar umaCombobox com os tipos de pesquisa de Cidade <code>Cidade/code>.
     */
    public List getTipoConsultaCidade() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("codigo", "Código"));
        return itens;
    }

    /*
     * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo
     * <code>tipoEmpresa</code>
     */
    public List getListaSelectItemTipoEmpresaFornecedor() throws Exception {
        List objs = new ArrayList(0);
        Hashtable tipoEmpresas = (Hashtable) Dominios.getTipoEmpresa();
        Enumeration keys = tipoEmpresas.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) tipoEmpresas.get(value);
            objs.add(new SelectItem(value, label));
        }
        return objs;
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
     * relativo ao atributo <code>Cidade</code>.
     */
    public void montarListaSelectItemCategoriaProduto(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarCategoriaProdutoPorNome(prm, Uteis.NIVELMONTARDADOS_TODOS);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                CategoriaProdutoVO obj = (CategoriaProdutoVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
            }
            setListaSelectItemCategoriaProduto(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>Cidade</code>. Buscando todos os objetos
     * correspondentes a entidade <code>Cidade</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é
     * importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemCategoriaProduto() {
        try {
            montarListaSelectItemCategoriaProduto("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nome</code> Este
     * atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox
     * correspondente
     */
    public List consultarCategoriaProdutoPorNome(String nomePrm, int nivelMontarDados) throws Exception {
        List lista = getFacadeFactory().getCategoriaProdutoFacade().consultarPorNome(nomePrm, false, nivelMontarDados, getUsuarioLogado());
        return lista;
    }

    /**
     * Método responsável por inicializar a lista de valores (<code>SelectItem</code>) para todos os ComboBox's.
     */
    public void inicializarListasSelectItemTodosComboBox() {
        montarListaSelectItemCategoriaProduto();
        montarListaSelectItemComTodosOsBancos();
    }

    /**
     * Rotina responsável por preencher a combo de consulta da telas.
     */
    public List getTipoConsultaCombo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("razaoSocial", "Razão Social"));
        itens.add(new SelectItem("codigo", "Código"));
        itens.add(new SelectItem("nomeCidade", "Cidade"));
        itens.add(new SelectItem("CNPJ", "CNPJ"));
        itens.add(new SelectItem("inscEstadual", "Inscrição Estadual"));
        itens.add(new SelectItem("RG", "RG"));
        itens.add(new SelectItem("CPF", "CPF"));
        return itens;
    }

    /**
     * Metodo responsavel por criar a mascara no componente da consulta
     *
     * @return
     */
    public String getMascaraConsulta() {
        if (getControleConsulta().getCampoConsulta().equals("CNPJ")) {
            return "return mascara(this.form,'formCadastro:valorConsulta','99.999.999/9999-99',event)";
        }
        if (getControleConsulta().getCampoConsulta().equals("CPF")) {
            return "return mascara(this.form,'formCadastro:valorConsulta','999.999.999-99',event)";
        }
        return "";
    }

    /**
     * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
     */
    public String inicializarConsultar() {
        removerObjetoMemoria(this);
        setListaConsulta(new ArrayList(0));
        setMensagemID("msg_entre_prmconsulta");
        return Uteis.getCaminhoRedirecionamentoNavegacao("fornecedorCons");
    }

    public void carregarEnderecoPessoa() {
        try {
        	getFornecedorVO().setCEP(Uteis.adicionarMascaraCEPConformeTamanhoCampo(getFornecedorVO().getCEP()));
            getFacadeFactory().getEnderecoFacade().carregarEndereco(fornecedorVO, getUsuarioLogado());
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public FornecedorVO getFornecedorVO() {
        if (fornecedorVO == null) {
            fornecedorVO = new FornecedorVO();
        }
        return fornecedorVO;
    }

    public void setFornecedorVO(FornecedorVO fornecedorVO) {
        this.fornecedorVO = fornecedorVO;
    }

    public FornecedorCategoriaProdutoVO getFornecedorCategoriaProdutoVO() {
        if (fornecedorCategoriaProdutoVO == null) {
            fornecedorCategoriaProdutoVO = new FornecedorCategoriaProdutoVO();
        }
        return fornecedorCategoriaProdutoVO;
    }

    public void setFornecedorCategoriaProdutoVO(FornecedorCategoriaProdutoVO fornecedorCategoriaProdutoVO) {
        this.fornecedorCategoriaProdutoVO = fornecedorCategoriaProdutoVO;
    }

    public List getListaSelectItemCategoriaProduto() {
        if (listaSelectItemCategoriaProduto == null) {
            listaSelectItemCategoriaProduto = new ArrayList();
        }
        return listaSelectItemCategoriaProduto;
    }

    public void setListaSelectItemCategoriaProduto(List listaSelectItemCategoriaProduto) {
        this.listaSelectItemCategoriaProduto = listaSelectItemCategoriaProduto;
    }

    /**
     * @return the listaConsultaCidade
     */
    public List getListaConsultaCidade() {
        if (listaConsultaCidade == null) {
            listaConsultaCidade = new ArrayList();
        }
        return listaConsultaCidade;
    }

    /**
     * @param listaConsultaCidade
     *            the listaConsultaCidade to set
     */
    public void setListaConsultaCidade(List listaConsultaCidade) {
        this.listaConsultaCidade = listaConsultaCidade;
    }

    /**
     * @return the campoConsultaCidade
     */
    public String getCampoConsultaCidade() {
        if (campoConsultaCidade == null) {
            campoConsultaCidade = "";
        }
        return campoConsultaCidade;
    }

    /**
     * @param campoConsultaCidade
     *            the campoConsultaCidade to set
     */
    public void setCampoConsultaCidade(String campoConsultaCidade) {
        this.campoConsultaCidade = campoConsultaCidade;
    }

    /**
     * @return the valorConsultaCidade
     */
    public String getValorConsultaCidade() {
        if (valorConsultaCidade == null) {
            valorConsultaCidade = "";
        }
        return valorConsultaCidade;
    }

    /**
     * @param valorConsultaCidade
     *            the valorConsultaCidade to set
     */
    public void setValorConsultaCidade(String valorConsultaCidade) {
        this.valorConsultaCidade = valorConsultaCidade;
    }
    
    public void adicionarMascaraCPF() {
    	getFornecedorVO().setCPF(Uteis.adicionarMascaraCPFConformeTamanhoCampo(getFornecedorVO().getCPF()));
    	if(getFornecedorVO().getIsTemMei()) {    		
        	getFornecedorVO().setCpfFornecedor(Uteis.adicionarMascaraCPFConformeTamanhoCampo(getFornecedorVO().getCpfFornecedor()));
	    		
    	}
    }
   
    
    public void adicionarMascaraCEP() {
    	getFornecedorVO().setCEP(Uteis.adicionarMascaraCEPConformeTamanhoCampo(getFornecedorVO().getCEP()));
    }
    
    public void montarListaSelectItemComTodosOsBancos() {
    	try {
			List<BancoVO> listaBancoVOs = getFacadeFactory().getBancoFacade().consultarPorBancoNivelComboBox(false, getUsuarioLogado());
			List<SelectItem> objs = new ArrayList<SelectItem>(0);
			objs.add(new SelectItem(0,""));
			Iterator<BancoVO> j = listaBancoVOs.iterator();
			while(j.hasNext()) {
				BancoVO item =(BancoVO) j.next();
				objs.add(new SelectItem(item.getCodigo(),item.getNome()));
			}
			setListaSelectItemBancos(objs);
    	} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
    }
    
    public void retornaNrBanco() {
    	
    	try {
			List<BancoVO> listaBancoVOs = getFacadeFactory().getBancoFacade().consultarPorCodigo(getFornecedorVO().getBancoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			
			if(!listaBancoVOs.isEmpty()) {
				Iterator<BancoVO> j = listaBancoVOs.iterator();
				BancoVO item =(BancoVO) j.next();
				getFornecedorVO().getBancoVO().setNrBanco(item.getNrBanco());
				
			}
    	} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
    }

	public List<SelectItem> getListaSelectItemBancos() {
		return listaSelectItemBancos;
	}

	public void setListaSelectItemBancos(List<SelectItem> listaSelectItemBancos) {
		this.listaSelectItemBancos = listaSelectItemBancos;
	}
	
	public String getSizeChaveEnderecamentoPix() {
		return getSizeChavePix(getFornecedorVO().getTipoIdentificacaoChavePixEnum());
	}


	
	public boolean isNumeroBancoItau() {
		return Uteis.isAtributoPreenchido(getFornecedorVO().getNumeroBancoRecebimento()) &&  getFornecedorVO().getNumeroBancoRecebimento().equals("341");
	}
}
