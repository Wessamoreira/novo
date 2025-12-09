package controle.recursoshumanos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import controle.arquitetura.SuperControle;
import negocio.comuns.recursoshumanos.ProgressaoSalarialItemVO;
import negocio.comuns.recursoshumanos.ProgressaoSalarialVO;
import negocio.comuns.recursoshumanos.ProgressaoSalarialVO.EnumCampoConsultaProgressaoSalarial;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilSelectItem;

/**
 * 
 * @author Gilberto Nery
 *
 */
@Controller("ProgressaoSalarialControle")
@Scope("viewScope")
@Lazy
@SuppressWarnings({"rawtypes"})
public class ProgressaoSalarialControle extends SuperControle implements Serializable {

	private static final long serialVersionUID = 1396024070909618852L;
	
	private static final String TELA_FORM = "progressaoSalarialForm";
	private static final String TELA_CONS = "progressaoSalarialCons";
	private static final String CONTEXT_PARA_EDICAO = "itemPesquisado";

	private ProgressaoSalarialVO progressaoSalarialVO;
	private ProgressaoSalarialItemVO progressaoSalarialItemVO;
	
	private List<SelectItem> listaSelectItemNivelSalarial;
	private List<SelectItem> listaSelectItemFaixaSalarial;
	
	public ProgressaoSalarialControle() {
		setControleConsultaOtimizado(new DataModelo());
		removerObjetoMemoria(this);
		setControleConsultaOtimizado(new DataModelo());
		getControleConsultaOtimizado().setCampoConsulta(EnumCampoConsultaProgressaoSalarial.DESCRICAO.name());
		setListaConsulta(new ArrayList(0));
		setMensagemID(MSG_TELA.msg_entre_prmconsulta.name());
	}

	public String novo() {
		removerObjetoMemoria(this);
		setControleConsultaOtimizado(new DataModelo());
		inicializarComboNivelEFaixaSalarial();
		setMensagemID(MSG_TELA.msg_entre_dados.name());
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	public String editar() {
		try {
			inicializarComboNivelEFaixaSalarial();
			progressaoSalarialVO = (ProgressaoSalarialVO) context().getExternalContext().getRequestMap().get(CONTEXT_PARA_EDICAO);
			setProgressaoSalarialVO(progressaoSalarialVO);
			
			List<ProgressaoSalarialItemVO> objs = getFacadeFactory().getProgressaoSalarialItemInterfaceFacade().consultarProgressaoTabelaItem(getProgressaoSalarialVO().getCodigo().longValue());
			progressaoSalarialVO.setProgressaoSalarialItens(objs);
			
			setControleConsultaOtimizado(new DataModelo());
			setMensagemID(MSG_TELA.msg_dados_editar.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			return "";
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	public void persistir() {
		try {
			getFacadeFactory().getProgressaoSalarialInterfaceFacade().persistir(getProgressaoSalarialVO(), true, getUsuarioLogado());
			setMensagemID(MSG_TELA.msg_dados_gravados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setControleConsultaOtimizado(new DataModelo());
		getControleConsultaOtimizado().setCampoConsulta(EnumCampoConsultaProgressaoSalarial.DESCRICAO.name());
		setListaConsulta(new ArrayList(0));
		setMensagemID(MSG_TELA.msg_entre_prmconsulta.name());
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_CONS);
	}

	public void scrollerListener(DataScrollEvent dataScrollerEvent) {
		try {
			getControleConsultaOtimizado().setPaginaAtual(dataScrollerEvent.getPage());
			getControleConsultaOtimizado().setPage(dataScrollerEvent.getPage());
			consultarDados();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}


	@Override
	public void consultarDados() {
		try {
			super.consultar();
			getControleConsultaOtimizado().preencherDadosParaConsulta(true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			getFacadeFactory().getProgressaoSalarialInterfaceFacade().consultarPorEnumCampoConsulta(getControleConsultaOtimizado());
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			getControleConsultaOtimizado().getListaConsulta().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	

	public String excluir() {
		try {
			getFacadeFactory().getProgressaoSalarialInterfaceFacade().excluir(getProgressaoSalarialVO(), true, getUsuarioLogado());
			novo();
			setMensagemID(MSG_TELA.msg_dados_excluidos.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());

		}
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	public void irPaginaInicial() throws Exception {
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

	public void inicializarComboNivelEFaixaSalarial() {
		montarListaSelectItemNivelSalarial();
		montarListaSelectItemFaixaSalarial();
	}

	public ProgressaoSalarialVO getProgressaoSalarialVO() {
		if (progressaoSalarialVO == null)
			progressaoSalarialVO = new ProgressaoSalarialVO();
		return progressaoSalarialVO;
	}
	
	public ProgressaoSalarialItemVO getProgressaoSalarialItemVO() {
		if (progressaoSalarialItemVO == null)
			progressaoSalarialItemVO = new ProgressaoSalarialItemVO();
		return progressaoSalarialItemVO;
	}

	public void setProgressaoSalarialItemVO(ProgressaoSalarialItemVO progressaoSalarialItemVO) {
		this.progressaoSalarialItemVO = progressaoSalarialItemVO;
	}

	public void setProgressaoSalarialVO(ProgressaoSalarialVO progressaoSalarialVO) {
		this.progressaoSalarialVO = progressaoSalarialVO;
	}

	public List<SelectItem> getListaSelectItemNivelSalarial() {
		if (listaSelectItemNivelSalarial == null)
			listaSelectItemNivelSalarial = new ArrayList<>();
		return listaSelectItemNivelSalarial;
	}

	public List<SelectItem> getListaSelectItemFaixaSalarial() {
		if (listaSelectItemFaixaSalarial == null)
			listaSelectItemFaixaSalarial = new ArrayList<>();
		return listaSelectItemFaixaSalarial;
	}

	public void setListaSelectItemNivelSalarial(List<SelectItem> listaSelectItemNivelSalarial) {
		this.listaSelectItemNivelSalarial = listaSelectItemNivelSalarial;
	}

	public void setListaSelectItemFaixaSalarial(List<SelectItem> listaSelectItemFaixaSalarial) {
		this.listaSelectItemFaixaSalarial = listaSelectItemFaixaSalarial;
	}

	public void montarListaSelectItemNivelSalarial()  {
    	try {
    		List resultadoConsulta = consultarNivelSalarial();
            setListaSelectItemNivelSalarial(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "descricao"));	
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
    public List consultarNivelSalarial()  {
    	List lista = new ArrayList<>();
    	try {
    		lista = getFacadeFactory().getNivelSalarialInterfaceFacade().consultarListaDeNivelSalarial();			
		} catch (Exception e) {
		}
        return lista;
    }
    
    public void montarListaSelectItemFaixaSalarial()  {
    	try {
    		List resultadoConsulta = consultarFaixaSalarial();
            setListaSelectItemFaixaSalarial(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "descricao"));	
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
    public List consultarFaixaSalarial()  {
    	List lista = new ArrayList<>();
    	try {
    		lista = getFacadeFactory().getFaixaSalarialInterfaceFacade().consultarListaDeFaixaSalarial();			
		} catch (Exception e) {
		}
        return lista;
    }
    
	public void adicionarProgressaoSalarialItem() {

		if(getProgressaoSalarialItemVO().getNivelSalarialVO().getCodigo() <= 0 || getProgressaoSalarialItemVO().getFaixaSalarialVO().getCodigo() <= 0) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), UteisJSF.internacionalizar("msg_ProgressaoSalarial_CamposObrigatoriosItensNaoPreenchidos"));
			return;
		}
		
		if(!validarDadosFaixaENivelJaCadastrados(getProgressaoSalarialItemVO())) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), UteisJSF.internacionalizar("msg_ProgressaoSalarial_NivelEFaixaSalarialJaInformados"));
			return;
		}
		
		if (getProgressaoSalarialItemVO().getItemEdicao()) {
			Iterator<ProgressaoSalarialItemVO> i = getProgressaoSalarialVO().getProgressaoSalarialItens().iterator();
			int index = 0;
			int aux = -1;
			ProgressaoSalarialItemVO objAux = new ProgressaoSalarialItemVO();
			while(i.hasNext()) {
				ProgressaoSalarialItemVO objExistente = i.next();
				
				if (objExistente.getCodigo().equals(getProgressaoSalarialItemVO().getCodigo()) && objExistente.getItemEdicao()){
					getProgressaoSalarialItemVO().setItemEdicao(false);
			       	aux = index;
			       	objAux = getProgressaoSalarialItemVO();
	 			}
	            index++;
			}
			
			if(aux >= 0) {
				getProgressaoSalarialVO().getProgressaoSalarialItens().set(aux, objAux);
			}
			
		} else {
			getProgressaoSalarialVO().getProgressaoSalarialItens().add(getProgressaoSalarialItemVO());
		}
		setProgressaoSalarialItemVO(new ProgressaoSalarialItemVO());
		
		setMensagemID(MSG_TELA.msg_dados_editar.name());
		setMensagemDetalhada("");
	}
	
	
	/**
	 * Valida se ja existe cadastrado na grid o mesmo registro informado pelo usuario
	 * 
	 * @param itemVO
	 * @return <br> true: nao existe -> dados validos
	 * false: existe -> dados ja cadastrados
	 */
	private boolean validarDadosFaixaENivelJaCadastrados(ProgressaoSalarialItemVO itemVO) {
		for(ProgressaoSalarialItemVO progressaoItem  : getProgressaoSalarialVO().getProgressaoSalarialItens()) {
        	
        	if(progressaoItem.getNivelSalarialVO().getCodigo().equals(itemVO.getNivelSalarialVO().getCodigo()) &&
					progressaoItem.getFaixaSalarialVO().getCodigo().equals(itemVO.getFaixaSalarialVO().getCodigo())) {
			
				if(progressaoItem.getCodigo() != null && progressaoItem.getCodigo() > 0 && !progressaoItem.getCodigo().equals(itemVO.getCodigo())) {
					return false;
				} 
			}
        }
		return true;
	}

	public void removerProgressaoSalarialItem() {
		try {
    		ProgressaoSalarialItemVO obj = (ProgressaoSalarialItemVO) context().getExternalContext().getRequestMap().get("progressaoItem");
            getProgressaoSalarialVO().excluirProgressaoItemVO(obj);
            setMensagemID("msg_dados_excluidos");    		
    	}catch (Exception e) {
    		e.printStackTrace();
		}	
	}
	
	public void editarProgressaoSalarialItem() {
		ProgressaoSalarialItemVO obj = (ProgressaoSalarialItemVO) context().getExternalContext().getRequestMap().get("progressaoItem");
		obj.setItemEdicao(true);
		setProgressaoSalarialItemVO(obj);
	}
	
	public void getDescricaoFaixaSalarial() {
		for (SelectItem selectItem : getListaSelectItemFaixaSalarial()) {
			if (selectItem.getValue().equals(getProgressaoSalarialItemVO().getFaixaSalarialVO().getCodigo())) {
				getProgressaoSalarialItemVO().getFaixaSalarialVO().setDescricao(selectItem.getLabel());
			}
		}
	}
	
	public void getDescricaoNivelSalarial() {
		for (SelectItem selectItem : getListaSelectItemNivelSalarial()) {
			if (selectItem.getValue().equals(getProgressaoSalarialItemVO().getNivelSalarialVO().getCodigo())) {
				getProgressaoSalarialItemVO().getNivelSalarialVO().setDescricao(selectItem.getLabel());
			}
		}
	}

}