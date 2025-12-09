package controle.academico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.PlanoDescontoInclusaoDisciplinaVO;
import negocio.comuns.utilitarias.ControleConsulta;

/**
 *
 * @author Carlos
 */
@Controller("PlanoDescontoInclusaoDisciplinaControle")
@Scope("request")
@Lazy
public class PlanoDescontoInclusaoDisciplinaControle extends SuperControle implements Serializable {

	private static final long serialVersionUID = 1L;
	private PlanoDescontoInclusaoDisciplinaVO planoDescontoInclusaoDisciplinaVO;

    public PlanoDescontoInclusaoDisciplinaControle() {
        setControleConsulta(new ControleConsulta());
        setMensagemID("msg_entre_prmconsulta");
    }

    public String novo() {
        setPlanoDescontoInclusaoDisciplinaVO(new PlanoDescontoInclusaoDisciplinaVO());
        getPlanoDescontoInclusaoDisciplinaVO().setSituacao("CO");
        getPlanoDescontoInclusaoDisciplinaVO().setResponsavelAtivacao(getUsuarioLogadoClone());
        return "editar";
    }

    public String persistir() {
        try {
            getFacadeFactory().getPlanoDescontoInclusaoDisciplinaFacade().persistir(getPlanoDescontoInclusaoDisciplinaVO(), getUsuarioLogado());
            setMensagemID("msg_dados_gravados");
            return "editar";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "editar";
        }

    }
    
    public String excluir() {
        try {
            getFacadeFactory().getPlanoDescontoInclusaoDisciplinaFacade().excluir(getPlanoDescontoInclusaoDisciplinaVO(), getUsuarioLogado());
            novo();
            setMensagemID("msg_dados_excluidos");
            return "editar";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "editar";
        }
    }

    public String editar() {
        PlanoDescontoInclusaoDisciplinaVO obj = (PlanoDescontoInclusaoDisciplinaVO) context().getExternalContext().getRequestMap().get("planoDescontoInclusaoDisciplina");
        setPlanoDescontoInclusaoDisciplinaVO(obj);
        obj.setNovoObj(Boolean.FALSE);
        return "editar";
    }

    public String consultar() {
        try {
            super.consultar();
            setListaConsulta(getFacadeFactory().getPlanoDescontoInclusaoDisciplinaFacade().consultar(getControleConsulta().getCampoConsulta(), getControleConsulta().getValorConsulta(), getUsuarioLogado()));
            setMensagemID("msg_dados_consultados");
            return "consultar";
        } catch (Exception e) {
            getListaConsulta().clear();
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "consultar";
        }
    }
    
    public void realizarAtivacaoPlanoDescontoInclusaoDisciplina() {
        try {
            getFacadeFactory().getPlanoDescontoInclusaoDisciplinaFacade().realizarAtivacaoPlanoDescontoInclusaoDisciplina(getPlanoDescontoInclusaoDisciplinaVO(), getUsuarioLogado());
            setMensagemID("msg_dados_ativado");
        } catch (Exception ex) {
            setMensagemDetalhada("msg_erro", ex.getMessage());
        }
    }

    public void realizarInativacaoPlanoDescontoInclusaoDisciplina() {
        try {
            getFacadeFactory().getPlanoDescontoInclusaoDisciplinaFacade().realizarInativacaoPlanoDescontoInclusaoDisciplina(getPlanoDescontoInclusaoDisciplinaVO(), getUsuarioLogado());
            setMensagemID("msg_dados_ativado");
        } catch (Exception ex) {
            setMensagemDetalhada("msg_erro", ex.getMessage());
        }
    }

    public List<SelectItem> getTipoConsultaCombo() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("descricao", "Descrição"));
        itens.add(new SelectItem("codigo", "Código"));
        return itens;
    }

    public String inicializarConsultar() {
        removerObjetoMemoria(this);
        getListaConsulta().clear();
        setMensagemID("msg_entre_prmconsulta");
        return "consultar";
    }
    
    

    public Boolean getIsApresentarBotaoDesativar() {
        return getPlanoDescontoInclusaoDisciplinaVO().getSituacao().equals("AT");
    }

    public Boolean getIsApresentarBotaoAtivar() {
        return getPlanoDescontoInclusaoDisciplinaVO().getSituacao().equals("CO") && getPlanoDescontoInclusaoDisciplinaVO().getCodigo() != 0;
    }
    
    public boolean getIsApresentarBotaoGravarExcluir() {
        return getPlanoDescontoInclusaoDisciplinaVO().getSituacao().equals("CO");
    }

    public PlanoDescontoInclusaoDisciplinaVO getPlanoDescontoInclusaoDisciplinaVO() {
        if (planoDescontoInclusaoDisciplinaVO == null) {
            planoDescontoInclusaoDisciplinaVO = new PlanoDescontoInclusaoDisciplinaVO();
        }
        return planoDescontoInclusaoDisciplinaVO;
    }

    public void setPlanoDescontoInclusaoDisciplinaVO(PlanoDescontoInclusaoDisciplinaVO planoDescontoInclusaoDisciplinaVO) {
        this.planoDescontoInclusaoDisciplinaVO = planoDescontoInclusaoDisciplinaVO;
    }

    
}
