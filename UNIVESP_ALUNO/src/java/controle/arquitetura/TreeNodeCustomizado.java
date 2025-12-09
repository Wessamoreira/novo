package controle.arquitetura;


import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import java.util.ArrayList;
import java.util.List;


/**
 * Richfaces 4.x não possui o atributo data, por isso foi necessário criar a classe 
 * TreeNodeCustomizado 
 * @version SEI 6.0.0.0 
 * @author Geber
 */
public class TreeNodeCustomizado<T>  extends DefaultTreeNode<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6078096122134775997L;
	private List<T> listaObjetos;
	
	public TreeNodeCustomizado() {
		super();
		setType(DEFAULT_TYPE);
	}
	
	public TreeNodeCustomizado(T data, TreeNodeCustomizado<T> root) {
		super.setData(data);
		super.setParent(root);
		setType(DEFAULT_TYPE);
	}
	
	public List<TreeNode<T>> getFilhos() {
		return getChildren();
	}
	
	public void setMaximizarTree(boolean maximizado) {
		setExpanded(maximizado);
	}
	
	public boolean  getMaximizarTree() {
		return isExpanded();
	}

	public List<T> getListaObjetos() {
		if(listaObjetos == null) {
			listaObjetos = new ArrayList<T>(0);
		}
		return listaObjetos;
	}

	public void setListaObjetos(List<T> listaObjetos) {
		this.listaObjetos = listaObjetos;
	}
	
}
