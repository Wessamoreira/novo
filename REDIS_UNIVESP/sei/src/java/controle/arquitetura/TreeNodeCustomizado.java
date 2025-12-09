package controle.arquitetura;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.richfaces.model.TreeNode;
import org.richfaces.model.TreeNodeImpl;

/**
 * Richfaces 4.x não possui o atributo data, por isso foi necessário criar a classe 
 * TreeNodeCustomizado 
 * @version SEI 6.0.0.0 
 * @author Geber
 */
public class TreeNodeCustomizado extends TreeNodeImpl  implements Serializable{ 
	
	private static final long serialVersionUID = 2205070607517218597L;
	private Object data;
	private List<Object> filhos;
	private Boolean maximizarTree;
	private List<Object> listaObjetos;
	

	public TreeNodeCustomizado() {
		super();	
	}	

	public TreeNodeCustomizado(Object data) {
		super();
		this.data = data;		
	}

	public Object getData() {
		if (data == null) {
			data = new Object();
		}
		
		return data;
	}
	
	public void setData(Object data) {
		this.data = data;
	}
	
	public Iterator<Map.Entry<Object, TreeNode>> getChildren(){
		
		return null;
	}

	public List<Object> getFilhos() {
		if (filhos == null) {
			filhos = new ArrayList<>(0);
		}
		return filhos;
	}

	public void setFilhos(List<Object> filhos) {
		this.filhos = filhos;
	}
	
	
	public Boolean getMaximizarTree() {
		if (maximizarTree == null) {
			maximizarTree = false;
		}
		return maximizarTree;
	}

	public void setMaximizarTree(Boolean maximizarTree) {
		this.maximizarTree = maximizarTree;
	}

	public List<Object> getListaObjetos() {
		if(listaObjetos == null) {
			listaObjetos =  new ArrayList<Object>(0);
		}
		return listaObjetos;
	}

	public void setListaObjetos(List<Object> listaObjetos) {
		this.listaObjetos = listaObjetos;
	}
	
	
}
