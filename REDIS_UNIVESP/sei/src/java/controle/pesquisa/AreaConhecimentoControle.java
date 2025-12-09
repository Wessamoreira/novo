package controle.pesquisa;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas areaConhecimentoForm.jsp
 * areaConhecimentoCons.jsp) com as funcionalidades da classe <code>AreaConhecimento</code>. Implemtação da camada
 * controle (Backing Bean).
 * 
 * @see SuperControle
 * @see AreaConhecimento
 * @see AreaConhecimentoVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.administrativo.FormacaoAcademicaVO;
import negocio.comuns.pesquisa.AreaConhecimentoVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;

@Controller("AreaConhecimentoControle")
@Scope("viewScope")
@Lazy
public class AreaConhecimentoControle extends SuperControle implements Serializable {

    private AreaConhecimentoVO areaConhecimentoVO;
    private String areaConhecimentoPrincipal_Erro;
    private List<SelectItem> listaSelectItemAreaConhecimentoPrincipal;

    public AreaConhecimentoControle() throws Exception {
        //obterUsuarioLogado();
        setControleConsulta(new ControleConsulta());
        setMensagemID("msg_entre_prmconsulta");
    }

    /**
     * Rotina responsável por disponibilizar um novo objeto da classe <code>AreaConhecimento</code> para edição pelo
     * usuário da aplicação.
     */
    public String novo() {
        removerObjetoMemoria(this);
        setAreaConhecimentoVO(new AreaConhecimentoVO());
        inicializarListasSelectItemTodosComboBox();
        setMensagemID("msg_entre_dados");
        return Uteis.getCaminhoRedirecionamentoNavegacao("areaConhecimentoForm");
    }

    /**
     * Rotina responsável por disponibilizar os dados de um objeto da classe <code>AreaConhecimento</code> para
     * alteração. O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente
     * possa disponibilizá-lo para edição.
     */
    public String editar() {
        AreaConhecimentoVO obj = (AreaConhecimentoVO) context().getExternalContext().getRequestMap().get("areaConhecimentoItem");
        obj.setNovoObj(Boolean.FALSE);
        setAreaConhecimentoVO(obj);
        inicializarListasSelectItemTodosComboBox();
        setMensagemID("msg_dados_editar");
        return Uteis.getCaminhoRedirecionamentoNavegacao("areaConhecimentoForm");
    }

    /**
     * Método responsável por inicializar a lista de valores (<code>SelectItem</code>) para todos os ComboBox's.
     */
    public void inicializarListasSelectItemTodosComboBox() {
        montarListaSelectItemAreaConhecimentoPrincipal();
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nome</code> Este
     * atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox
     * correspondente
     */
    public List consultarAreaConhecimentoPorNome(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getAreaConhecimentoFacade().consultarPorNome(nomePrm, false, getUsuarioLogado());
        return lista;
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
     * relativo ao atributo <code>AreaConhecimento</code>.
     */
    public void montarListaSelectItemAreaConhecimentoPrincipal(String prm) throws Exception {

        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarAreaConhecimentoPorNome(prm);
            i = resultadoConsulta.iterator();
            List<SelectItem> objs = new ArrayList<SelectItem>(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                AreaConhecimentoVO obj = (AreaConhecimentoVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
            }
            setListaSelectItemAreaConhecimentoPrincipal(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>AreaConhecimento</code>. Buscando todos os
     * objetos correspondentes a entidade <code>AreaConhecimento</code>. Esta rotina não recebe parâmetros para
     * filtragem de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio
     * requisições Ajax.
     */
    public void montarListaSelectItemAreaConhecimentoPrincipal() {
        try {
            montarListaSelectItemAreaConhecimentoPrincipal("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>AreaConhecimento</code>.
     * Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é
     * acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado
     * para o usuário juntamente com uma mensagem de erro.
     */
    public void gravar() {
        try {
            if (areaConhecimentoVO.isNovoObj().booleanValue()) {
                getFacadeFactory().getAreaConhecimentoFacade().incluir(areaConhecimentoVO, true, getUsuarioLogado());
            } else {
                getFacadeFactory().getAreaConhecimentoFacade().alterar(areaConhecimentoVO, true, getUsuarioLogado());
                getAplicacaoControle().removerAreaConhecimentoVO(areaConhecimentoVO.getCodigo());
            }
            
            setMensagemID("msg_dados_gravados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP AreaConhecimentoCons.jsp. Define o tipo de
     * consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
     * resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
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
                objs = getFacadeFactory().getAreaConhecimentoFacade().consultarPorCodigo(new Integer(valorInt), true, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("nome")) {
                objs = getFacadeFactory().getAreaConhecimentoFacade().consultarPorNome(getControleConsulta().getValorConsulta(), true, getUsuarioLogado());
            }
            setListaConsulta(objs);
            setMensagemID("msg_dados_consultados");
            return "";
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "";
        }
    }

    /**
     * Operação responsável por processar a exclusão um objeto da classe <code>AreaConhecimentoVO</code> Após a exclusão
     * ela automaticamente aciona a rotina para uma nova inclusão.
     */
    public void excluir() {
        try {
        	getFacadeFactory().getFormacaoAcademicaFacade().validarVinculoFormacaoAcademica(areaConhecimentoVO.getCodigo(), getUsuario());
        	getFacadeFactory().getAreaConhecimentoFacade().excluir(areaConhecimentoVO, true, getUsuarioLogado());
            setAreaConhecimentoVO(new AreaConhecimentoVO());
            setMensagemID("msg_dados_excluidos");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void irPaginaInicial() throws Exception {
        controleConsulta.setPaginaAtual(1);
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
     * Método responsável por processar a consulta na entidade <code>AreaConhecimento</code> por meio de sua respectiva
     * chave primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave
     * primária da entidade montando automaticamente o resultado da consulta para apresentação.
     */
    public void consultarAreaConhecimentoPorChavePrimaria() {
        try {
            Integer campoConsulta = areaConhecimentoVO.getAreaConhecimentoPrincipal().getCodigo();
            AreaConhecimentoVO areaConhecimento = getFacadeFactory().getAreaConhecimentoFacade().consultarPorChavePrimaria(campoConsulta, getUsuarioLogado());
            areaConhecimentoVO.getAreaConhecimentoPrincipal().setNome(areaConhecimento.getNome());
            this.setAreaConhecimentoPrincipal_Erro("");
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemID("msg_erro_dadosnaoencontrados");
            areaConhecimentoVO.getAreaConhecimentoPrincipal().setNome("");
            areaConhecimentoVO.getAreaConhecimentoPrincipal().setCodigo(0);
            this.setAreaConhecimentoPrincipal_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
        }
    }

    /**
     * Rotina responsável por preencher a combo de consulta da telas.
     */
    public List<SelectItem> getTipoConsultaCombo() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("nome", "Nome"));
        return itens;
    }

    /**
     * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
     */
    public String inicializarConsultar() {
        removerObjetoMemoria(this);
        setListaConsulta(new ArrayList(0));
        setMensagemID("msg_entre_prmconsulta");
        return Uteis.getCaminhoRedirecionamentoNavegacao("areaConhecimentoCons");
    }
    
    public void scrollerListener(DataScrollEvent DataScrollEvent) throws Exception {
        getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
        getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
        consultar();
    }

    public String getAreaConhecimentoPrincipal_Erro() {
        return areaConhecimentoPrincipal_Erro;
    }

    public void setAreaConhecimentoPrincipal_Erro(String areaConhecimentoPrincipal_Erro) {
        this.areaConhecimentoPrincipal_Erro = areaConhecimentoPrincipal_Erro;
    }

    public AreaConhecimentoVO getAreaConhecimentoVO() {
        return areaConhecimentoVO;
    }

    public void setAreaConhecimentoVO(AreaConhecimentoVO areaConhecimentoVO) {
        this.areaConhecimentoVO = areaConhecimentoVO;
    }

    public List<SelectItem> getListaSelectItemAreaConhecimentoPrincipal() {
        return listaSelectItemAreaConhecimentoPrincipal;
    }

    public void setListaSelectItemAreaConhecimentoPrincipal(List listaSelectItemAreaConhecimentoPrincipal) {
        this.listaSelectItemAreaConhecimentoPrincipal = listaSelectItemAreaConhecimentoPrincipal;
    }
}
