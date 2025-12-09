package controle.ead;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.ead.ItemParametrosMonitoramentoAvaliacaoOnlineVO;
import negocio.comuns.ead.ParametrosMonitoramentoAvaliacaoOnlineVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

@Controller("ParametrosMonitoramentoAvaliacaoOnlineControle")
@Scope("viewScope")
public class ParametrosMonitoramentoAvaliacaoOnlineControle extends SuperControle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ParametrosMonitoramentoAvaliacaoOnlineVO parametrosMonitoramentoAvaliacaoOnlineVO;
	private ItemParametrosMonitoramentoAvaliacaoOnlineVO itemParametrosMonitoramentoAvaliacaoOnlineVO;
	
	@PostConstruct
	public void init() {
		setMensagemID("msg_dados_parametroConsulta", Uteis.ALERTA);	
	}
 
	public String consultar() {
		try {
			getControleConsulta().getListaConsulta().clear();
			getControleConsulta().setListaConsulta(getFacadeFactory().getParametrosMonitoramentoAvaliacaoOnlineFacade().consultar(getControleConsulta().getValorConsulta(), getControleConsulta().getCampoConsulta(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			if (getControleConsulta().getListaConsulta().isEmpty()) {
				throw new Exception(UteisJSF.internacionalizar("msg_relatorio_vazio"));
			}
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return "";
	}

	public String novo() {
		setParametrosMonitoramentoAvaliacaoOnlineVO(new ParametrosMonitoramentoAvaliacaoOnlineVO());
		setItemParametrosMonitoramentoAvaliacaoOnlineVO(new ItemParametrosMonitoramentoAvaliacaoOnlineVO());
		setMensagemID("msg_entre_dados", Uteis.ALERTA);
		return Uteis.getCaminhoRedirecionamentoNavegacao("parametrosMonitoramentoAvaliacaoOnlineForm");
	}

	public String editar() {
		try {
			ParametrosMonitoramentoAvaliacaoOnlineVO parametrosMonitoramentoAvaliacaoOnlineVO = (ParametrosMonitoramentoAvaliacaoOnlineVO) context().getExternalContext().getRequestMap().get("parametro");
			setParametrosMonitoramentoAvaliacaoOnlineVO(getFacadeFactory().getParametrosMonitoramentoAvaliacaoOnlineFacade().consultarPorChavePrimaria(parametrosMonitoramentoAvaliacaoOnlineVO.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			setMensagemID("msg_dados_editar", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("parametrosMonitoramentoAvaliacaoOnlineForm");
	}
	
	public String voltarTelaConsulta() {
		setMensagemID("msg_dados_parametroConsulta", Uteis.ALERTA);
		getControleConsulta().getListaConsulta().clear();
		return Uteis.getCaminhoRedirecionamentoNavegacao("parametrosMonitoramentoAvaliacaoOnlineCons");
	}
	
	public void excluir() {
		try {
			getFacadeFactory().getParametrosMonitoramentoAvaliacaoOnlineFacade().excluir(getParametrosMonitoramentoAvaliacaoOnlineVO(), false, getUsuarioLogado());
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
			setParametrosMonitoramentoAvaliacaoOnlineVO(new ParametrosMonitoramentoAvaliacaoOnlineVO());
			setItemParametrosMonitoramentoAvaliacaoOnlineVO(new ItemParametrosMonitoramentoAvaliacaoOnlineVO());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void gravar() {
		try {
			getFacadeFactory().getParametrosMonitoramentoAvaliacaoOnlineFacade().persistir(getParametrosMonitoramentoAvaliacaoOnlineVO(), false, getUsuarioLogado());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void removerItemParametros() {
		try {
			ItemParametrosMonitoramentoAvaliacaoOnlineVO itemParametrosMonitoramentoAvaliacaoOnlineVO = (ItemParametrosMonitoramentoAvaliacaoOnlineVO) context().getExternalContext().getRequestMap().get("itens");
			getFacadeFactory().getItemParametrosMonitoramentoAvaliacaoOnlineFacade().removerItemParametros(itemParametrosMonitoramentoAvaliacaoOnlineVO, getParametrosMonitoramentoAvaliacaoOnlineVO(), getUsuarioLogado());
			setMensagemID("msg_ParametrosMonitoramentoAvalaicaoOnline_itemRemovidoComSucesso", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void adicionarItemParametros() {
		try {
			getFacadeFactory().getItemParametrosMonitoramentoAvaliacaoOnlineFacade().adicionarItemParametros(getParametrosMonitoramentoAvaliacaoOnlineVO(), getItemParametrosMonitoramentoAvaliacaoOnlineVO(), getUsuarioLogado());
			setItemParametrosMonitoramentoAvaliacaoOnlineVO(new ItemParametrosMonitoramentoAvaliacaoOnlineVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	// Getters and Setters
	public List<SelectItem> getListaSelectItemCampoConsulta() {
		List<SelectItem> objs = new ArrayList<SelectItem>(0);

		objs.add(new SelectItem("descricao", "Descrição"));
		return objs;
	}

	public ParametrosMonitoramentoAvaliacaoOnlineVO getParametrosMonitoramentoAvaliacaoOnlineVO() {
		if (parametrosMonitoramentoAvaliacaoOnlineVO == null) {
			parametrosMonitoramentoAvaliacaoOnlineVO = new ParametrosMonitoramentoAvaliacaoOnlineVO();
		}
		return parametrosMonitoramentoAvaliacaoOnlineVO;
	}

	public void setParametrosMonitoramentoAvaliacaoOnlineVO(ParametrosMonitoramentoAvaliacaoOnlineVO parametrosMonitoramentoAvaliacaoOnlineVO) {
		this.parametrosMonitoramentoAvaliacaoOnlineVO = parametrosMonitoramentoAvaliacaoOnlineVO;
	}

	public ItemParametrosMonitoramentoAvaliacaoOnlineVO getItemParametrosMonitoramentoAvaliacaoOnlineVO() {
		if (itemParametrosMonitoramentoAvaliacaoOnlineVO == null) {
			itemParametrosMonitoramentoAvaliacaoOnlineVO = new ItemParametrosMonitoramentoAvaliacaoOnlineVO();
		}
		return itemParametrosMonitoramentoAvaliacaoOnlineVO;
	}

	public void setItemParametrosMonitoramentoAvaliacaoOnlineVO(ItemParametrosMonitoramentoAvaliacaoOnlineVO itemParametrosMonitoramentoAvaliacaoOnlineVO) {
		this.itemParametrosMonitoramentoAvaliacaoOnlineVO = itemParametrosMonitoramentoAvaliacaoOnlineVO;
	}
}
