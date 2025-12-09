package controle.financeiro;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas perfilEconomicoForm.jsp
 * perfilEconomicoCons.jsp) com as funcionalidades da classe <code>PerfilEconomico</code>. Implemtação da camada
 * controle (Backing Bean).
 * 
 * @see SuperControle
 * @see PerfilEconomico
 * @see PerfilEconomicoVO
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
import negocio.comuns.financeiro.CondicaoNegociacaoVO;
import negocio.comuns.financeiro.PerfilEconomicoCondicaoNegociacaoVO;
import negocio.comuns.financeiro.PerfilEconomicoVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;

@Controller("PerfilEconomicoControle")
@Scope("viewScope")
@Lazy
public class PerfilEconomicoControle extends SuperControle implements Serializable {

    private PerfilEconomicoVO perfilEconomicoVO;
    private PerfilEconomicoCondicaoNegociacaoVO perfilEconomicoCondicaoNegociacaoVO;
    protected List listaSelectItemCondicaoNegociacao;

    public PerfilEconomicoControle() throws Exception {
        //obterUsuarioLogado();
        setControleConsulta(new ControleConsulta());
        setMensagemID("msg_entre_prmconsulta");
    }

    /**
     * Rotina responsável por disponibilizar um novo objeto da classe <code>PerfilEconomico</code> para edição pelo
     * usuário da aplicação.
     */
    public String novo() {
        removerObjetoMemoria(this);
        setPerfilEconomicoVO(new PerfilEconomicoVO());
        inicializarListasSelectItemTodosComboBox();
        setPerfilEconomicoCondicaoNegociacaoVO(new PerfilEconomicoCondicaoNegociacaoVO());
        setMensagemID("msg_entre_dados");
        return Uteis.getCaminhoRedirecionamentoNavegacao("perfilEconomicoForm.xhtml");
    }

    /**
     * Rotina responsável por disponibilizar os dados de um objeto da classe <code>PerfilEconomico</code> para
     * alteração. O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente
     * possa disponibilizá-lo para edição.
     */
    public String editar() {
        PerfilEconomicoVO obj = (PerfilEconomicoVO) context().getExternalContext().getRequestMap().get("perfilEconomicoItens");
        obj.setNovoObj(Boolean.FALSE);
        setPerfilEconomicoVO(obj);
        inicializarListasSelectItemTodosComboBox();
        setPerfilEconomicoCondicaoNegociacaoVO(new PerfilEconomicoCondicaoNegociacaoVO());
        setMensagemID("msg_dados_editar");
        return Uteis.getCaminhoRedirecionamentoNavegacao("perfilEconomicoForm.xhtml");
    }

    /**
     * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>PerfilEconomico</code>.
     * Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é
     * acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado
     * para o usuário juntamente com uma mensagem de erro.
     */
    public String gravar() {
        try {
            if (perfilEconomicoVO.isNovoObj().booleanValue()) {
                getFacadeFactory().getPerfilEconomicoFacade().incluir(perfilEconomicoVO, getUsuarioLogado());
            } else {
                getFacadeFactory().getPerfilEconomicoFacade().alterar(perfilEconomicoVO, getUsuarioLogado());
            }
            setMensagemID("msg_dados_gravados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("perfilEconomicoForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("perfilEconomicoForm.xhtml");
        }
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP PerfilEconomicoCons.jsp. Define o tipo de
     * consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
     * resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    public String consultar() {
        try {
            super.consultar();
            List objs = new ArrayList(0);
//			if (getControleConsulta().getCampoConsulta().equals("codigo")) {
//				if (getControleConsulta().getValorConsulta().equals("")) {
//					getControleConsulta().setValorConsulta("0");
//				}
//				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
//				objs = getFacadeFactory().getPerfilEconomicoFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_TODOS);
//			}
            if (getControleConsulta().getCampoConsulta().equals("nome")) {
                objs = getFacadeFactory().getPerfilEconomicoFacade().consultarPorNome(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            setListaConsulta(objs);
            setMensagemID("msg_dados_consultados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("perfilEconomicoCons.xhtml");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("perfilEconomicoCons.xhtml");
        }
    }

    /**
     * Operação responsável por processar a exclusão um objeto da classe <code>PerfilEconomicoVO</code> Após a exclusão
     * ela automaticamente aciona a rotina para uma nova inclusão.
     */
    public String excluir() {
        try {
            getFacadeFactory().getPerfilEconomicoFacade().excluir(perfilEconomicoVO, getUsuarioLogado());
            setPerfilEconomicoVO(new PerfilEconomicoVO());

            setPerfilEconomicoCondicaoNegociacaoVO(new PerfilEconomicoCondicaoNegociacaoVO());
            setMensagemID("msg_dados_excluidos");
            return Uteis.getCaminhoRedirecionamentoNavegacao("perfilEconomicoForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("perfilEconomicoForm.xhtml");
        }
    }

    /*
     * Método responsável por adicionar um novo objeto da classe <code>PerfilEconomicoCondicaoNegociacao</code> para o
     * objeto <code>perfilEconomicoVO</code> da classe <code>PerfilEconomico</code>
     */
    public String adicionarPerfilEconomicoCondicaoNegociacao() throws Exception {
        try {
            if (!getPerfilEconomicoVO().getCodigo().equals(0)) {
                perfilEconomicoCondicaoNegociacaoVO.setPerfilEconomico(getPerfilEconomicoVO().getCodigo());
            }
            if (getPerfilEconomicoCondicaoNegociacaoVO().getCondicaoNegociacao().getCodigo().intValue() != 0) {
                Integer campoConsulta = getPerfilEconomicoCondicaoNegociacaoVO().getCondicaoNegociacao().getCodigo();
                CondicaoNegociacaoVO condicaoNegociacao = getFacadeFactory().getCondicaoNegociacaoFacade().consultarPorChavePrimaria(campoConsulta, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
                getPerfilEconomicoCondicaoNegociacaoVO().setCondicaoNegociacao(condicaoNegociacao);
            }
            getPerfilEconomicoVO().adicionarObjPerfilEconomicoCondicaoNegociacaoVOs(getPerfilEconomicoCondicaoNegociacaoVO());
            this.setPerfilEconomicoCondicaoNegociacaoVO(new PerfilEconomicoCondicaoNegociacaoVO());
            setMensagemID("msg_dados_adicionados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("perfilEconomicoForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("perfilEconomicoForm.xhtml");
        }
    }

    /*
     * Método responsável por disponibilizar dados de um objeto da classe <code>PerfilEconomicoCondicaoNegociacao</code>
     * para edição pelo usuário.
     */
    public String editarPerfilEconomicoCondicaoNegociacao() throws Exception {
        PerfilEconomicoCondicaoNegociacaoVO obj = (PerfilEconomicoCondicaoNegociacaoVO) context().getExternalContext().getRequestMap().get("perfilEconomicoCondicaoNegociacao");
        setPerfilEconomicoCondicaoNegociacaoVO(obj);
        return Uteis.getCaminhoRedirecionamentoNavegacao("perfilEconomicoForm.xhtml");
    }

    /*
     * Método responsável por remover um novo objeto da classe <code>PerfilEconomicoCondicaoNegociacao</code> do objeto
     * <code>perfilEconomicoVO</code> da classe <code>PerfilEconomico</code>
     */
    public String removerPerfilEconomicoCondicaoNegociacao() throws Exception {
        PerfilEconomicoCondicaoNegociacaoVO obj = (PerfilEconomicoCondicaoNegociacaoVO) context().getExternalContext().getRequestMap().get("perfilEconomicoCondicaoNegociacao");
        getPerfilEconomicoVO().excluirObjPerfilEconomicoCondicaoNegociacaoVOs(obj.getCondicaoNegociacao().getCodigo());
        setMensagemID("msg_dados_excluidos");
        return Uteis.getCaminhoRedirecionamentoNavegacao("perfilEconomicoForm.xhtml");
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

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
     * relativo ao atributo <code>CondicaoNegociacao</code>.
     */
    public void montarListaSelectItemCondicaoNegociacao(Integer prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarCondicaoNegociacaoPorCodigo(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                CondicaoNegociacaoVO obj = (CondicaoNegociacaoVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getCondicaoPagamento().getNome().toString()));
            }
            setListaSelectItemCondicaoNegociacao(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>CondicaoNegociacao</code>. Buscando todos
     * os objetos correspondentes a entidade <code>CondicaoNegociacao</code>. Esta rotina não recebe parâmetros para
     * filtragem de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio
     * requisições Ajax.
     */
    public void montarListaSelectItemCondicaoNegociacao() {
        try {
            montarListaSelectItemCondicaoNegociacao(0);
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>codigo</code> Este
     * atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox
     * correspondente
     */
    public List consultarCondicaoNegociacaoPorCodigo(Integer codigoPrm) throws Exception {
        List lista = getFacadeFactory().getCondicaoNegociacaoFacade().consultarPorCodigo(codigoPrm, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
        return lista;
    }

    /**
     * Método responsável por inicializar a lista de valores (<code>SelectItem</code>) para todos os ComboBox's.
     */
    public void inicializarListasSelectItemTodosComboBox() {
        montarListaSelectItemCondicaoNegociacao();
    }

    /**
     * Rotina responsável por preencher a combo de consulta da telas.
     */
    public List getTipoConsultaCombo() {
        List itens = new ArrayList(0);
//		itens.add(new SelectItem("codigo", "Código"));
        itens.add(new SelectItem("nome", "Nome"));
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
        return Uteis.getCaminhoRedirecionamentoNavegacao("perfilEconomicoCons.xhtml");
    }

    public List getListaSelectItemCondicaoNegociacao() {
        return (listaSelectItemCondicaoNegociacao);
    }

    public void setListaSelectItemCondicaoNegociacao(List listaSelectItemCondicaoNegociacao) {
        this.listaSelectItemCondicaoNegociacao = listaSelectItemCondicaoNegociacao;
    }

    public PerfilEconomicoCondicaoNegociacaoVO getPerfilEconomicoCondicaoNegociacaoVO() {
        return perfilEconomicoCondicaoNegociacaoVO;
    }

    public void setPerfilEconomicoCondicaoNegociacaoVO(PerfilEconomicoCondicaoNegociacaoVO perfilEconomicoCondicaoNegociacaoVO) {
        this.perfilEconomicoCondicaoNegociacaoVO = perfilEconomicoCondicaoNegociacaoVO;
    }

    public PerfilEconomicoVO getPerfilEconomicoVO() {
        return perfilEconomicoVO;
    }

    public void setPerfilEconomicoVO(PerfilEconomicoVO perfilEconomicoVO) {
        this.perfilEconomicoVO = perfilEconomicoVO;
    }
}
