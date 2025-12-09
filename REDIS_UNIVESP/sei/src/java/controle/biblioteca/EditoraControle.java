package controle.biblioteca;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas editoraForm.jsp editoraCons.jsp)
 * com as funcionalidades da classe <code>Editora</code>. Implemtação da camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see Editora
 * @see EditoraVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.biblioteca.EditoraVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;

@Controller("EditoraControle")
@Scope("viewScope")
@Lazy
public class EditoraControle extends SuperControle implements Serializable {

    private EditoraVO editoraVO;
    protected List listaSelectItemCidade;
    private String campoConsultaCidade;
    private String valorConsultaCidade;
    private List<CidadeVO> listaConsultaCidade;

    public EditoraControle() throws Exception {
        //obterUsuarioLogado();
        setControleConsulta(new ControleConsulta());
        setMensagemID("msg_entre_prmconsulta");
    }

    /**
     * Rotina responsável por disponibilizar um novo objeto da classe <code>Editora</code> para edição pelo usuário da
     * aplicação.
     */
    public String novo() {
        removerObjetoMemoria(this);
        setEditoraVO(new EditoraVO());
        inicializarListasSelectItemTodosComboBox();
        setMensagemID("msg_entre_dados");
        return Uteis.getCaminhoRedirecionamentoNavegacao("editoraForm.xhtml");
    }

    /**
     * Rotina responsável por disponibilizar os dados de um objeto da classe <code>Editora</code> para alteração. O
     * objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa
     * disponibilizá-lo para edição.
     */
    public String editar() {
        EditoraVO obj = (EditoraVO) context().getExternalContext().getRequestMap().get("editoraItens");
        obj.setNovoObj(Boolean.FALSE);
        setEditoraVO(obj);
        inicializarListasSelectItemTodosComboBox();
        setMensagemID("msg_dados_editar");
        return Uteis.getCaminhoRedirecionamentoNavegacao("editoraForm.xhtml");
    }

    /**
     * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>Editora</code>. Caso o
     * objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é
     * acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado
     * para o usuário juntamente com uma mensagem de erro.
     */
    public String gravar() {
        try {
            if (editoraVO.isNovoObj().booleanValue()) {
                getFacadeFactory().getEditoraFacade().incluir(editoraVO, getUsuarioLogado());
            } else {
                getFacadeFactory().getEditoraFacade().alterar(editoraVO, getUsuarioLogado());
            }
            setMensagemID("msg_dados_gravados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("editoraForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("editoraForm.xhtml");
        }
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP EditoraCons.jsp. Define o tipo de consulta a ser
     * executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como resultado,
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
                objs = getFacadeFactory().getEditoraFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("nome")) {
                objs = getFacadeFactory().getEditoraFacade().consultarPorNome(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            setListaConsulta(objs);
            setMensagemID("msg_dados_consultados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("editoraCons.xhtml");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("editoraCons.xhtml");
        }
    }

    public void consultarCidade() {
        try {
            List objs = new ArrayList(0);
            if (getCampoConsultaCidade().equals("codigo")) {
                if (getValorConsultaCidade().equals("")) {
                    setValorConsultaCidade("0");
                }
                int valorInt = Integer.parseInt(getValorConsultaCidade());
                //objs = getFacadeFactory().getCidadeFacade().consultarPorCodigo(new Integer(valorInt), false, getUsuarioLogado());
                objs = getFacadeFactory().getCidadeFacade().consultaRapidaPorCodigo(new Integer(valorInt), false, getUsuarioLogado());
            }
            if (getCampoConsultaCidade().equals("nome")) {
                //objs = getFacadeFactory().getCidadeFacade().consultarPorNome(getValorConsultaCidade(), false, getUsuarioLogado());
                objs = getFacadeFactory().getCidadeFacade().consultaRapidaPorNome(getValorConsultaCidade(), false, getUsuarioLogado());
            }

            setListaConsultaCidade(objs);
            setMensagemID("msg_dados_consultados");

        } catch (Exception e) {
            setListaConsultaCidade(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());

        }
    }

    public void selecionarCidade() {
        CidadeVO obj = (CidadeVO) context().getExternalContext().getRequestMap().get("cidadeItens");
        getEditoraVO().setCidade(obj);
        getListaConsultaCidade().clear();
        this.setValorConsultaCidade("");
        this.setCampoConsultaCidade("");
    }

    /**
     * Operação responsável por processar a exclusão um objeto da classe <code>EditoraVO</code> Após a exclusão ela
     * automaticamente aciona a rotina para uma nova inclusão.
     */
    public String excluir() {
        try {
            getFacadeFactory().getEditoraFacade().excluir(editoraVO, getUsuarioLogado());
            novo();
            setMensagemID("msg_dados_excluidos");
            return Uteis.getCaminhoRedirecionamentoNavegacao("editoraForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("editoraForm.xhtml");
        }
    }

    public void carregarEnderecoPessoa() {
        try {
            getFacadeFactory().getEnderecoFacade().carregarEndereco(getEditoraVO(), getUsuarioLogado());
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
     * relativo ao atributo <code>Cidade</code>.
     */
    public void montarListaSelectItemCidade(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarCidadePorNome(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                CidadeVO obj = (CidadeVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
            }
            Uteis.liberarListaMemoria(resultadoConsulta);
            setListaSelectItemCidade(objs);
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
    public void montarListaSelectItemCidade() {
        try {
            montarListaSelectItemCidade("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nome</code> Este
     * atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox
     * correspondente
     */
    public List consultarCidadePorNome(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getCidadeFacade().consultarPorNome(nomePrm, false, getUsuarioLogado());
        return lista;
    }

    /**
     * Método responsável por inicializar a lista de valores ( <code>SelectItem</code>) para todos os ComboBox's.
     */
    public void inicializarListasSelectItemTodosComboBox() {
    }

    /**
     * Rotina responsável por atribui um javascript com o método de mascara para campos do tipo Data, CPF, CNPJ, etc.
     */
    public String getMascaraConsulta() {
        if (getControleConsulta().getCampoConsulta().equals("telefone")) {
            return "return mascara(this.form, 'form:valorConsulta', '(99)9999-9999', event);";
        }
        if (getControleConsulta().getCampoConsulta().equals("telefone1")) {
            return "return mascara(this.form, 'form:valorConsulta', '(99)9999-9999', event);";
        }
        if (getControleConsulta().getCampoConsulta().equals("fax")) {
            return "return mascara(this.form, 'form:valorConsulta', '(99)9999-9999', event);";
        }
        return "";
    }

    public List getTipoConsultaCidade() {
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
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("codigo", "Código"));
        return itens;
    }

    /**
     * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
     */
    public String inicializarConsultar() {
        removerObjetoMemoria(this);
        setListaConsulta(new ArrayList(0));
        setMensagemID("msg_entre_prmconsulta");
        return Uteis.getCaminhoRedirecionamentoNavegacao("editoraCons.xhtml");
    }

    /**
     * Operação que libera todos os recursos (atributos, listas, objetos) do backing bean. Garantindo uma melhor atuação
     * do Garbage Coletor do Java. A mesma é automaticamente quando realiza o logout.
     */
    protected void limparRecursosMemoria() {
        super.limparRecursosMemoria();
        editoraVO = null;
        Uteis.liberarListaMemoria(listaSelectItemCidade);
    }

    public List getListaSelectItemCidade() {
        if (listaSelectItemCidade == null) {
            listaSelectItemCidade = new ArrayList(0);
        }
        return (listaSelectItemCidade);
    }

    public void setListaSelectItemCidade(List listaSelectItemCidade) {
        this.listaSelectItemCidade = listaSelectItemCidade;
    }

    public EditoraVO getEditoraVO() {
        if (editoraVO == null) {
            editoraVO = new EditoraVO();
        }
        return editoraVO;
    }

    public void setEditoraVO(EditoraVO editoraVO) {
        this.editoraVO = editoraVO;
    }

    public String getCampoConsultaCidade() {
        return campoConsultaCidade;
    }

    public void setCampoConsultaCidade(String campoConsultaCidade) {
        this.campoConsultaCidade = campoConsultaCidade;
    }

    public String getValorConsultaCidade() {
        return valorConsultaCidade;
    }

    public void setValorConsultaCidade(String valorConsultaCidade) {
        this.valorConsultaCidade = valorConsultaCidade;
    }

    public List<CidadeVO> getListaConsultaCidade() {
        if (listaConsultaCidade == null) {
            listaConsultaCidade = new ArrayList<CidadeVO>(0);
        }
        return listaConsultaCidade;
    }

    public void setListaConsultaCidade(List<CidadeVO> listaConsultaCidade) {
        this.listaConsultaCidade = listaConsultaCidade;
    }
}
