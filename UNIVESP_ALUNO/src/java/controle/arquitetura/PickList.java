package controle.arquitetura;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilReflexao;
import org.primefaces.event.DragDropEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import jakarta.inject.Named;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller("PickList")
@Scope("viewScope")
public class PickList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3691148122520445650L;
	private String label;
	private String campoSelecionar;
	private String filtroSelecionado;
	private String filtroNaoSelecionado;
	private String labelSelecionados;
	private String labelSelecionado;
	private SuperControle bean;
	private List value;	
	private Map<String, PickList> mapPickList;
	
	
	public PickList() {
		super();
	}
	
	
	public PickList(String label, String selecionar, List value, SuperControle bean, String labelSelecionado) {
		super();
		this.label = label;
		this.campoSelecionar = selecionar;
		this.value = value;
		this.labelSelecionado = labelSelecionado;
		this.bean = bean;
	}



	public PickList pick(String id, List value, String label, String selecionar, SuperControle bean, String labelSelecionado) {
		if(getMapPickList().containsKey(id) && (!getMapPickList().get(id).getValue().equals(value) 
				|| !getMapPickList().get(id).getLabel().equals(label)
				|| !getMapPickList().get(id).getCampoSelecionar().equals(selecionar)
				|| !getMapPickList().get(id).getLabelSelecionado().equals(labelSelecionado)
				|| !getMapPickList().get(id).getBean().equals(bean))) {
			getMapPickList().remove(id);
		}
		if(!getMapPickList().containsKey(id)) {
			getMapPickList().put(id, new PickList(label, selecionar, value, bean, labelSelecionado));
		}
		return 	getMapPickList().get(id);
	}
	
	public void selecionarGeral(String id, Boolean marcar) {
		PickList pick = getMapPickList().get(id);
		if(pick != null) {
		for(Object obj: pick.getValue()) {
			if(marcar && filtrar(id, obj, false) && !pick.getSelecionado(obj)) {
				UtilReflexao.invocarMetodoSet1Parametro(obj, pick.getCampoSelecionar(), Boolean.class, marcar);
			}else if(!marcar && filtrar(id, obj, true) && pick.getSelecionado(obj)) {
				UtilReflexao.invocarMetodoSet1Parametro(obj, pick.getCampoSelecionar(),  Boolean.class, marcar);
			}
		}
		pick.registrarLabelSelecionado();
		}
	}
	
	public void selecionarEspecifico(String id, Object obj, Boolean marcar) {
		PickList pick = getMapPickList().get(id);
		if(pick != null) {
			UtilReflexao.invocarMetodoSet1Parametro(obj, pick.getCampoSelecionar(), Boolean.class, marcar);
			pick.registrarLabelSelecionado();
		}
	}
	
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public List getValue() {
		return value;
	}

	public void setValue(List value) {
		this.value = value;
	}

	public String getCampoSelecionar() {
		return campoSelecionar;
	}
	public void setCampoSelecionar(String campoSelecionar) {
		this.campoSelecionar = campoSelecionar;
	}
	
	public Map<String, PickList> getMapPickList() {
		if(mapPickList == null) {
			mapPickList =  new HashMap<String, PickList>(0);
		}
		return mapPickList;
	}
	public void setMapPickList(Map<String, PickList> mapPickList) {
		this.mapPickList = mapPickList;
	}
	
	
	public Boolean getSelecionado( Object obj) {
		try {
			
			return obj != null && Uteis.isAtributoPreenchido(getCampoSelecionar()) ? (Boolean) UtilReflexao.invocarMetodoGet(obj, getCampoSelecionar()) : false;
		}catch (Exception e) {
			return false;
		}
	}
	
	public String getTitulo(Object obj) {
		try {
			return obj != null && Uteis.isAtributoPreenchido(getLabel()) ? (String) UtilReflexao.invocarMetodoGet(obj, getLabel()) : "";
		}catch (Exception e) {
			return null;
		}
	}


	public String getFiltroSelecionado() {
		if(filtroSelecionado == null) {
			filtroSelecionado =  "";
		}
		return filtroSelecionado;
	}


	public void setFiltroSelecionado(String filtroSelecionado) {
		this.filtroSelecionado = filtroSelecionado;
	}


	public String getFiltroNaoSelecionado() {
		if(filtroNaoSelecionado == null) {
			filtroNaoSelecionado =  "";
		}
		return filtroNaoSelecionado;
	}


	public void setFiltroNaoSelecionado(String filtroNaoSelecionado) {
		this.filtroNaoSelecionado = filtroNaoSelecionado;
	}

	/* (non-Javadoc)
	 * @see org.richfaces.model.Filter#accept(java.lang.Object)
	 */	
	public boolean filtrar(String id, Object arg0, Boolean selecionado) {
		PickList pick = getMapPickList().get(id);
		if(pick != null) {
		if(!Uteis.isAtributoPreenchido(pick.getLabel())){
			return true;
		}
		String[] campos = pick.getLabel().split("\\.");
		String campoComparar = pick.getLabel();
		if(campos.length > 1){
			int tam = campos.length;
			int x = 1;
			for(String bean:campos){
				if(x == tam){
					campoComparar = bean;
					break;
				}
				Object obj = UtilReflexao.invocarMetodoGet(arg0, bean);
				if(obj instanceof SuperVO){
					arg0 = (SuperVO) obj;
				}else if(obj instanceof Enum){
					arg0 = (Enum) obj;
				}
				x++;
			}
		}
		Object obj = UtilReflexao.invocarMetodoGet(arg0, campoComparar);
		if(obj instanceof String) {
			String valor = (String) obj;
			if(selecionado) {
				return (pick.getFiltroSelecionado().trim().isEmpty() || (valor != null && Uteis.removerAcentos(valor.toUpperCase()).contains(Uteis.removerAcentos(pick.getFiltroSelecionado().toUpperCase()))));
			}
			return (pick.getFiltroNaoSelecionado().trim().isEmpty() || (valor != null && Uteis.removerAcentos(valor.toUpperCase()).contains(Uteis.removerAcentos(pick.getFiltroNaoSelecionado().toUpperCase()))));
		}
		}
		return true;
		
	}
	
	public String getLabelSelecionados() {
		if(labelSelecionados == null) {
			labelSelecionados =  "";
		}
		return labelSelecionados;
	}


	public void setLabelSelecionados(String labelSelecionados) {
		this.labelSelecionados = labelSelecionados;
	}

	private void registrarLabelSelecionado() {
		setLabelSelecionados("");
		for(Object object: getValue()) {
			if(getSelecionado(object)) {
				if(!getLabelSelecionados().isEmpty()) {
					setLabelSelecionados(getLabelSelecionados()+"; ");
				}
				setLabelSelecionados(getLabelSelecionados()+getTitulo(object));
			}
		}
		if(getBean() != null && Uteis.isAtributoPreenchido(getLabelSelecionado())) {
			String[] campos = getLabelSelecionado().split("\\.");
    		String campoComparar = getLabelSelecionado();
    		Object primeiro = getBean();
    		if(campos.length > 1){
    			int tam = campos.length;
    			int x = 1;
    			for(String bean:campos){
    				if(x == tam){
    					campoComparar = bean;
    					break;
    				}
    				if(UtilReflexao.invocarMetodoGet(primeiro, bean) instanceof SuperVO) {
    					primeiro = (SuperVO)UtilReflexao.invocarMetodoGet(primeiro, bean);
    				}else if(UtilReflexao.invocarMetodoGet(primeiro, bean) instanceof Enum) {
    					primeiro = (Enum)UtilReflexao.invocarMetodoGet(primeiro, bean);
    				} else {
    					primeiro = UtilReflexao.invocarMetodoGet(primeiro, bean);
    				}   				
    				x++;
    			}
    		}
    		UtilReflexao.invocarMetodoSet1Parametro(primeiro, campoComparar, String.class, getLabelSelecionados());
		}
	}
	
	public void dragDropSelecionar(DragDropEvent<?> dropEvent) {
		if(dropEvent.getData() != null) {
			UtilReflexao.invocarMetodoSet1Parametro(dropEvent.getData(), getCampoSelecionar(), Boolean.class, true);
			registrarLabelSelecionado();
		}
	}

	public void dragDropNaoSelecionar(DragDropEvent<?> dropEvent) {
		if(dropEvent.getData() != null) {
			UtilReflexao.invocarMetodoSet1Parametro(dropEvent.getData(), getCampoSelecionar(), Boolean.class, false);
			registrarLabelSelecionado();
		}
	}


	public String getLabelSelecionado() {
		return labelSelecionado;
	}


	public void setLabelSelecionado(String labelSelecionado) {
		this.labelSelecionado = labelSelecionado;
	}


	public SuperControle getBean() {
		return bean;
	}


	public void setBean(SuperControle bean) {
		this.bean = bean;
	}

	
}