/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controle.academico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.ObservacaoComplementarVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;

@Controller("ObservacaoComplementarControle")
@Scope("viewScope")
@Lazy
public class ObservacaoComplementarControle extends SuperControle implements Serializable {

    private ObservacaoComplementarVO ObservacaoComplementarVO;

    public ObservacaoComplementarControle() throws Exception {
        setControleConsulta(new ControleConsulta());
        getControleConsulta().setCampoConsulta("nome");
        setMensagemID("msg_entre_prmconsulta");
    }

    public String novo() throws Exception {
        removerObjetoMemoria(this);
        setObservacaoComplementarVO(new ObservacaoComplementarVO());
        setMensagemID("msg_entre_dados");
        return Uteis.getCaminhoRedirecionamentoNavegacao("observacaoComplementarForm.xhtml");
    }

    public String editar() throws Exception {
        ObservacaoComplementarVO obj = (ObservacaoComplementarVO) context().getExternalContext().getRequestMap().get("observacaoComplementarItens");
        setObservacaoComplementarVO(getFacadeFactory().getObservacaoComplementarFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
        setMensagemID("msg_dados_editar");
        return Uteis.getCaminhoRedirecionamentoNavegacao("observacaoComplementarForm.xhtml");
    }

    public String gravar() {
        try {
            if (getObservacaoComplementarVO().isNovoObj().booleanValue()) {
                getFacadeFactory().getObservacaoComplementarFacade().incluir(getObservacaoComplementarVO(), getUsuarioLogado());
            } else {
                getFacadeFactory().getObservacaoComplementarFacade().alterar(getObservacaoComplementarVO(), getUsuarioLogado());
            }
            setMensagemID("msg_dados_gravados");
            getListaConsulta().clear();
            return Uteis.getCaminhoRedirecionamentoNavegacao("observacaoComplementarForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("observacaoComplementarForm.xhtml");
        }
    }

    public String consultar() {
        try {
            super.consultar();
            List objs = new ArrayList(0);
            if (getControleConsulta().getCampoConsulta().equals("codigo")) {
                if (getControleConsulta().getValorConsulta().equals("")) {
                    getControleConsulta().setValorConsulta("0");
                }
                int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
                objs = getFacadeFactory().getObservacaoComplementarFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("nome")) {
                objs = getFacadeFactory().getObservacaoComplementarFacade().consultarPorNome(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            setListaConsulta(objs);
            setMensagemID("msg_dados_consultados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("observacaoComplementarCons.xhtml");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("observacaoComplementarCons.xhtml");
        }
    }

    /**
     * Operação responsável por processar a exclusão um objeto da classe <code>ParceiroVO</code> Após a exclusão ela
     * automaticamente aciona a rotina para uma nova inclusão.
     */
    public String excluir() {
        try {
            getFacadeFactory().getObservacaoComplementarFacade().excluir(getObservacaoComplementarVO(), getUsuarioLogado());
            setObservacaoComplementarVO(new ObservacaoComplementarVO());
            setMensagemID("msg_dados_excluidos");
            return "";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "";
        }
    }

    public List getListaSelectItemFuncionario() throws Exception {
        List objs = new ArrayList(0);
        objs.add(new SelectItem("1", "1"));
        objs.add(new SelectItem("2", "2"));
        objs.add(new SelectItem("3", "3"));
        return objs;
    }

    public List getListaSelectItemOrdem() throws Exception {
    	List objs = new ArrayList(0);
    	objs.add(new SelectItem("0", "0"));
    	objs.add(new SelectItem("1", "1"));
    	objs.add(new SelectItem("2", "2"));
    	return objs;
    }

    /**
     * Rotina responsável por preencher a combo de consulta da telas.
     */
    public List getTipoConsultaCombo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("codigo", "Codigo"));
        return itens;
    }

    /**
     * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
     */
    public String inicializarConsultar() {
        removerObjetoMemoria(this);
        setListaConsulta(new ArrayList(0));
        setMensagemID("msg_entre_prmconsulta");
        return Uteis.getCaminhoRedirecionamentoNavegacao("observacaoComplementarCons.xhtml");
    }

    public ObservacaoComplementarVO getObservacaoComplementarVO() {
        if (ObservacaoComplementarVO == null) {
            ObservacaoComplementarVO = new ObservacaoComplementarVO();
        }
        return ObservacaoComplementarVO;
    }

    public void setObservacaoComplementarVO(ObservacaoComplementarVO ObservacaoComplementarVO) {
        this.ObservacaoComplementarVO = ObservacaoComplementarVO;
    }

    public void irPaginaInicial() throws Exception {
        this.consultar();
    }
}
