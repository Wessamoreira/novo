package controle.academico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.TipoAtividadeComplementarVO;
import negocio.comuns.utilitarias.Uteis;

@Controller("TipoAtividadeComplementarControle")
@Scope("viewScope")
@Lazy
public class TipoAtividadeComplementarControle extends SuperControle implements Serializable {

	private static final long serialVersionUID = 1L;
	private TipoAtividadeComplementarVO tipoAtividadeComplementarVO;
	
	private List<TipoAtividadeComplementarVO> listaConsultaSuperior;
    private String valorConsultaSuperior;
    private String campoConsultaSuperior;

	public String novo() {
		removerObjetoMemoria(this);
		this.setTipoAtividadeComplementarVO(new TipoAtividadeComplementarVO());
		this.setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("tipoAtividadeComplementarForm.xhtml");
	}

	public String editar() {
		TipoAtividadeComplementarVO obj = (TipoAtividadeComplementarVO) context().getExternalContext().getRequestMap().get("tipoAtividadeComplementarItens");
		obj.setNovoObj(Boolean.FALSE);
		this.setTipoAtividadeComplementarVO(obj);
		this.setMensagemID("msg_dados_editar");
		return Uteis.getCaminhoRedirecionamentoNavegacao("tipoAtividadeComplementarForm.xhtml");
	}

	public String gravar() {
		try {
			if (this.tipoAtividadeComplementarVO.isNovoObj()) {
				getFacadeFactory().getTipoAtividadeComplementarFacade().incluir(this.getTipoAtividadeComplementarVO(), getUsuarioLogado());
			} else {
				getFacadeFactory().getTipoAtividadeComplementarFacade().alterar(this.getTipoAtividadeComplementarVO(), getUsuarioLogado());
			}
			this.setMensagemID("msg_dados_gravados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("tipoAtividadeComplementarForm.xhtml");
		} catch (Exception e) {
			this.setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("tipoAtividadeComplementarForm.xhtml");
		}
	}

	public String excluir() {
		try {
			getFacadeFactory().getTipoAtividadeComplementarFacade().excluir(this.getTipoAtividadeComplementarVO(), this.getUsuarioLogado());
			this.setTipoAtividadeComplementarVO(new TipoAtividadeComplementarVO());
			this.setMensagemID("msg_dados_excluidos");
			return Uteis.getCaminhoRedirecionamentoNavegacao("tipoAtividadeComplementarForm.xhtml");
		} catch (Exception e) {
			this.setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("tipoAtividadeComplementarForm.xhtml");
		}
	}
	
	public String consultar() {
		try {
			super.consultar();
			List<TipoAtividadeComplementarVO> objs = new ArrayList<TipoAtividadeComplementarVO>(0);
			if (getControleConsulta().getCampoConsulta().equals("codigo")) {
                if (getControleConsulta().getValorConsulta().equals("")) {
                    getControleConsulta().setValorConsulta("0");
                }
                int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
                objs = getFacadeFactory().getTipoAtividadeComplementarFacade().consultarPorCodigo(valorInt, true, getUsuarioLogado());
        		}
            if (getControleConsulta().getCampoConsulta().equals("nome")) {
            	objs = getFacadeFactory().getTipoAtividadeComplementarFacade().consultarPorNome(getControleConsulta().getValorConsulta(), true, getUsuarioLogado());
    		}
            if (getControleConsulta().getCampoConsulta().equals("tipoSuperior")) {
            	objs = getFacadeFactory().getTipoAtividadeComplementarFacade().consultarPorTipoSuperior(getControleConsulta().getValorConsulta(), true, getUsuarioLogado());
    		}
			this.setListaConsulta(objs);
			this.setMensagemID("msg_dados_consultados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("tipoAtividadeComplementarCons.xhtml");
		} catch (Exception e) {
			this.setListaConsulta(new ArrayList<SelectItem>(0));
			this.setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("tipoAtividadeComplementarCons.xhtml");
		}
	}

    public String inicializarConsultar() {
        removerObjetoMemoria(this);
        setListaConsulta(new ArrayList<SelectItem>(0));
        setMensagemID("msg_entre_prmconsulta");
        return Uteis.getCaminhoRedirecionamentoNavegacao("tipoAtividadeComplementarCons.xhtml");
    }	
    
    public List<SelectItem> getTipoConsultaCombo() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("tipoSuperior", "Tipo Atividade Superior"));
        itens.add(new SelectItem("codigo", "Código"));
        return itens;
    }

	public TipoAtividadeComplementarVO getTipoAtividadeComplementarVO() {
		if(this.tipoAtividadeComplementarVO == null){
			this.tipoAtividadeComplementarVO = new TipoAtividadeComplementarVO();
		}
		return this.tipoAtividadeComplementarVO;
	}

	public void setTipoAtividadeComplementarVO(TipoAtividadeComplementarVO tipoAtividadeComplementarVO) {
		this.tipoAtividadeComplementarVO = tipoAtividadeComplementarVO;
	}
	
	public List<TipoAtividadeComplementarVO> getListaConsultaSuperior() {
		if (listaConsultaSuperior == null) {
			listaConsultaSuperior = new ArrayList<TipoAtividadeComplementarVO>(0);
		}
		return listaConsultaSuperior;
	}

	public void setListaConsultaSuperior(
			List<TipoAtividadeComplementarVO> listaConsultaSuperior) {
		this.listaConsultaSuperior = listaConsultaSuperior;
	}

	public String getValorConsultaSuperior() {
		if (valorConsultaSuperior == null) {
			valorConsultaSuperior = "";
		}
		return valorConsultaSuperior;
	}

	public void setValorConsultaSuperior(String valorConsultaSuperior) {
		this.valorConsultaSuperior = valorConsultaSuperior;
	}

	public String getCampoConsultaSuperior() {
		if (campoConsultaSuperior == null) {
			campoConsultaSuperior = "";
		}
		return campoConsultaSuperior;
	}

	public void setCampoConsultaSuperior(String campoConsultaSuperior) {
		this.campoConsultaSuperior = campoConsultaSuperior;
	}
	
    public void limparTipoAtividadeComplementarSuperior() {
        getTipoAtividadeComplementarVO().setTipoAtividadeComplementarSuperior(new TipoAtividadeComplementarVO());
        getListaConsultaSuperior().clear();
    }
    
    public void consultarTipoAtividadeComplementarSuperior() {
        try {
            List<TipoAtividadeComplementarVO> objs = new ArrayList<TipoAtividadeComplementarVO>(0);
            if (getCampoConsultaSuperior().equals("codigo")) {
            	if (getValorConsultaSuperior().equals("")) {
                    setValorConsultaSuperior("0");
                }
            	int codigo = Integer.parseInt(getValorConsultaSuperior());
                objs = getFacadeFactory().getTipoAtividadeComplementarFacade().consultarPorCodigo(codigo, true, getUsuarioLogado());
            }
            if (getCampoConsultaSuperior().equals("nome")) {
                objs = getFacadeFactory().getTipoAtividadeComplementarFacade().consultarPorNome(getValorConsultaSuperior(), true, getUsuarioLogado());
            }
            setListaConsultaSuperior(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaSuperior(new ArrayList<TipoAtividadeComplementarVO>(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarTipoAtividadeComplementarSuperior() {
        try {
        	TipoAtividadeComplementarVO obj = (TipoAtividadeComplementarVO) context().getExternalContext().getRequestMap().get("superior");
			obj = getFacadeFactory().getTipoAtividadeComplementarFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, true, getUsuarioLogado());
	        getTipoAtividadeComplementarVO().setTipoAtividadeComplementarSuperior(obj);
	        setValorConsultaSuperior("");
	        setCampoConsultaSuperior("");
	        getListaConsultaSuperior().clear();
	        setMensagemDetalhada("");
        } catch (Exception e) {
        	setMensagemDetalhada("msg_erro", e.getMessage());
		}
    }
    
	public List<SelectItem> getTipoConsultaComboSuperior() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("codigo", "Código"));
        return itens;
    }
	
}
