/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controle.crm;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.crm.MotivoInsucessoVO;
import negocio.comuns.utilitarias.Uteis;

/**
 *
 * @author Philippe
 */
@Controller("MotivoInsucessoControle")
@Scope("viewScope")
@Lazy
public class MotivoInsucessoControle extends SuperControle {

    private MotivoInsucessoVO motivoInsucessoVO;

    public String novo() {
        removerObjetoMemoria(this);
        setMotivoInsucessoVO(new MotivoInsucessoVO());
        setMensagemID("msg_entre_dados");
        return Uteis.getCaminhoRedirecionamentoNavegacao("motivoInsucessoForm.xhtml");
    }

    public String editar() throws Exception {
        MotivoInsucessoVO obj = (MotivoInsucessoVO) context().getExternalContext().getRequestMap().get("motivoInsucessoItens");
        setMotivoInsucessoVO(obj);
        getMotivoInsucessoVO().setNovoObj(Boolean.FALSE);
        setMensagemID("msg_dados_editar");
        return Uteis.getCaminhoRedirecionamentoNavegacao("motivoInsucessoForm.xhtml");
    }

    public String gravar() {
        try {
            if (getMotivoInsucessoVO().isNovoObj().booleanValue()) {
                getFacadeFactory().getMotivoInsucessoFacade().incluir(getMotivoInsucessoVO(), getUsuarioLogado());
            } else {
                getFacadeFactory().getMotivoInsucessoFacade().alterar(getMotivoInsucessoVO(), getUsuarioLogado());
            }
            setMensagemID("msg_dados_gravados");
            getListaConsulta().clear();
            return "";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "";
        }
    }

    public String consultar() {
        try {
            super.consultar();
            setListaConsulta(getFacadeFactory().getMotivoInsucessoFacade().consultar(getControleConsulta().getCampoConsulta(), getControleConsulta().getValorConsulta(), getUsuarioLogado()));
            setMensagemID("msg_dados_consultados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("motivoInsucessoCons.xhtml");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("motivoInsucessoCons.xhtml");
        }
    }

    public List getMontarListaSelectItemConsulta() throws Exception {
        List objs = new ArrayList(0);
        objs.add(new SelectItem("descricao", "Descrição"));
        return objs;
    }

    public String excluir() {
        try {
            getFacadeFactory().getMotivoInsucessoFacade().excluir(getMotivoInsucessoVO(), getUsuarioLogado());
            setMotivoInsucessoVO(new MotivoInsucessoVO());
            setMensagemID("msg_dados_excluidos");
            return "";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "";
        }
    }

    public MotivoInsucessoVO getMotivoInsucessoVO() {
        return motivoInsucessoVO;
    }

    public void setMotivoInsucessoVO(MotivoInsucessoVO motivoInsucessoVO) {
        this.motivoInsucessoVO = motivoInsucessoVO;
    }
    
	public String irPaginaConsulta(){
		setMensagemID("msg_entre_prmconsulta",Uteis.ALERTA );
		return Uteis.getCaminhoRedirecionamentoNavegacao("motivoInsucessoCons.xhtml");
	}
}
