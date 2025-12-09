package controle.academico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.ControleLivroFolhaReciboVO;
import negocio.comuns.academico.ControleLivroRegistroDiplomaVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.academico.ControleLivroFolhaRecibo;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas 
 * controleLivroFolhaReciboForm.jsp controleLivroFolhaReciboCons.jsp) com as funcionalidades da classe <code>ControleLivroFolhaRecibo</code>.
 * Implemtação da camada controle (Backing Bean).
 * @see SuperControle
 * @see ControleLivroFolhaRecibo
 * @see ControleLivroFolhaReciboVO
 */
@SuppressWarnings("unchecked")
@Controller("ControleLivroFolhaReciboControle")
@Scope("request")
@Lazy
public class ControleLivroFolhaReciboControle extends SuperControle implements Serializable {

    private ControleLivroFolhaReciboVO controleLivroFolhaReciboVO;
    protected List listaSelectItemControleLivroRegistroDiploma;
    protected List listaSelectItemMatricula;

    /**
     * Interface <code>ControleLivroFolhaReciboInterfaceFacade</code> responsável pela interconexão da camada de controle com a camada de negócio.
     * Criando uma independência da camada de controle com relação a tenologia de persistência dos dados (DesignPatter: Façade).
     */
    public ControleLivroFolhaReciboControle() throws Exception {
        //obterUsuarioLogado();
        setControleConsulta(new ControleConsulta());
        setMensagemID("msg_entre_prmconsulta");
    }

    /**
     * Rotina responsável por disponibilizar um novo objeto da classe <code>ControleLivroFolhaRecibo</code>
     * para edição pelo usuário da aplicação.
     */
    public String novo() {
        removerObjetoMemoria(this);
        setControleLivroFolhaReciboVO(new ControleLivroFolhaReciboVO());
        inicializarListasSelectItemTodosComboBox();
        setMensagemID("msg_entre_dados");
        return "editar";
    }

    /**
     * Rotina responsável por disponibilizar os dados de um objeto da classe <code>ControleLivroFolhaRecibo</code> para alteração.
     * O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa disponibilizá-lo para edição.
     */
    public String editar() {
        ControleLivroFolhaReciboVO obj = (ControleLivroFolhaReciboVO) context().getExternalContext().getRequestMap().get("controleLivroFolhaRecibo");
        obj.setNovoObj(Boolean.FALSE);
        setControleLivroFolhaReciboVO(obj);
        inicializarListasSelectItemTodosComboBox();
        setMensagemID("msg_dados_editar");
        return "editar";
    }

    /**
     * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>ControleLivroFolhaRecibo</code>.
     * Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>.
     * Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de erro.
     */
    public String gravar() {
        try {
            if (controleLivroFolhaReciboVO.isNovoObj().booleanValue()) {
                getFacadeFactory().getControleLivroFolhaReciboFacade().incluir(controleLivroFolhaReciboVO, getUsuarioLogado());
            } else {
                getFacadeFactory().getControleLivroFolhaReciboFacade().alterar(controleLivroFolhaReciboVO, getUsuarioLogado());
            }
            setMensagemID("msg_dados_gravados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            return "editar";
        }
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP ControleLivroFolhaReciboCons.jsp.
     * Define o tipo de consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP.
     * Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
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
                objs = getFacadeFactory().getControleLivroFolhaReciboFacade().consultarPorCodigo(new Integer(valorInt), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("codigoControleLivroRegistroDiploma")) {
                if (getControleConsulta().getValorConsulta().equals("")) {
                    getControleConsulta().setValorConsulta("0");
                }
                int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
//                objs = getFacadeFactory().getControleLivroFolhaReciboFacade().consultarPorCodigoSituacaoControleLivroRegistroDiploma(new Integer(valorInt), "", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            setListaConsulta(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            return "consultar";
        }
    }

    /**
     * Operação responsável por processar a exclusão um objeto da classe <code>ControleLivroFolhaReciboVO</code>
     * Após a exclusão ela automaticamente aciona a rotina para uma nova inclusão.
     */
    public String excluir() {
        try {
            getFacadeFactory().getControleLivroFolhaReciboFacade().excluir(controleLivroFolhaReciboVO, getUsuarioLogado());
            novo();
            setMensagemID("msg_dados_excluidos");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            return "editar";
        }
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher
     * o comboBox relativo ao atributo <code>Matricula</code>.
     */
    public void montarListaSelectItemMatricula(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarMatriculaPorMatricula(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem("", ""));
            while (i.hasNext()) {
                MatriculaVO obj = (MatriculaVO) i.next();
                objs.add(new SelectItem(obj.getMatricula(), obj.getMatricula()));
            }
            Uteis.liberarListaMemoria(resultadoConsulta);
            setListaSelectItemMatricula(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>Matricula</code>.
     * Buscando todos os objetos correspondentes a entidade <code>Matricula</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é
     * importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemMatricula() {
        try {
            montarListaSelectItemMatricula("");
        } catch (Exception e) {
            // System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>matricula</code>
     * Este atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox correspondente
     */
    public List consultarMatriculaPorMatricula(String matriculaPrm) throws Exception {
        List lista = getFacadeFactory().getMatriculaFacade().consultarPorMatricula(matriculaPrm, Uteis.NIVELMONTARDADOS_TODOS, false, Uteis.NIVELMONTARDADOS_TODOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
        return lista;
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher
     * o comboBox relativo ao atributo <code>ControleLivroRegistroDiploma</code>.
     */
    public void montarListaSelectItemControleLivroRegistroDiploma(Integer prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarControleLivroRegistroDiplomaPorCodigo(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                ControleLivroRegistroDiplomaVO obj = (ControleLivroRegistroDiplomaVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getCodigo().toString()));
            }
            Uteis.liberarListaMemoria(resultadoConsulta);
            setListaSelectItemControleLivroRegistroDiploma(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>ControleLivroRegistroDiploma</code>.
     * Buscando todos os objetos correspondentes a entidade <code>ControleLivroRegistroDiploma</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é
     * importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemControleLivroRegistroDiploma() {
        try {
            montarListaSelectItemControleLivroRegistroDiploma(0);
        } catch (Exception e) {
            // System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>codigo</code>
     * Este atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox correspondente
     */
    public List consultarControleLivroRegistroDiplomaPorCodigo(Integer codigoPrm) throws Exception {
        List lista = getFacadeFactory().getControleLivroFolhaReciboFacade().consultarPorCodigo(codigoPrm, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
        return lista;
    }

    /**
     * Método responsável por inicializar a lista de valores (<code>SelectItem</code>) para todos os ComboBox's.
     */
    public void inicializarListasSelectItemTodosComboBox() {
        montarListaSelectItemControleLivroRegistroDiploma();
//        montarListaSelectItemMatricula();
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
        itens.add(new SelectItem("codigo", "Codigo"));
        itens.add(new SelectItem("codigoControleLivroRegistroDiploma", "ControleLivroRegistroDiploma"));
        itens.add(new SelectItem("matriculaMatricula", "Matricula"));
        return itens;
    }

    /**
     * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
     */
    public String inicializarConsultar() {
        removerObjetoMemoria(this);
        setListaConsulta(new ArrayList(0));
        setMensagemID("msg_entre_prmconsulta");
        return "consultar";
    }

    public List getListaSelectItemMatricula() {
        if (listaSelectItemMatricula == null) {
            listaSelectItemMatricula = new ArrayList(0);
        }
        return (listaSelectItemMatricula);
    }

    public void setListaSelectItemMatricula(List listaSelectItemMatricula) {
        this.listaSelectItemMatricula = listaSelectItemMatricula;
    }

    public List getListaSelectItemControleLivroRegistroDiploma() {
        if (listaSelectItemControleLivroRegistroDiploma == null) {
            listaSelectItemControleLivroRegistroDiploma = new ArrayList(0);
        }
        return (listaSelectItemControleLivroRegistroDiploma);
    }

    public void setListaSelectItemControleLivroRegistroDiploma(List listaSelectItemControleLivroRegistroDiploma) {
        this.listaSelectItemControleLivroRegistroDiploma = listaSelectItemControleLivroRegistroDiploma;
    }

    public ControleLivroFolhaReciboVO getControleLivroFolhaReciboVO() {
        return controleLivroFolhaReciboVO;
    }

    public void setControleLivroFolhaReciboVO(ControleLivroFolhaReciboVO controleLivroFolhaReciboVO) {
        this.controleLivroFolhaReciboVO = controleLivroFolhaReciboVO;
    }
}
