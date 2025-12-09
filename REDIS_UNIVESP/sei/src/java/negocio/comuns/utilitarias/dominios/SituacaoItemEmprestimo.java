package negocio.comuns.utilitarias.dominios;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

/**
 * 
 * @author Diego
 */
public enum SituacaoItemEmprestimo {

	EM_EXECUCAO("EX", "Emprestado"), DEVOLVIDO("DE", "Devolvido"), ATRASADO("AT", "Atrasado"), RENOVADO("RE", "Renovado");

	String valor;
	String descricao;

	SituacaoItemEmprestimo(String valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
	}

	public static SituacaoItemEmprestimo getEnum(String valor) {
		SituacaoItemEmprestimo[] valores = values();
		for (SituacaoItemEmprestimo obj : valores) {
			if (obj.getValor().equals(valor)) {
				return obj;
			}
		}
		return null;
	}

	public static String getDescricao(String valor) {
		SituacaoItemEmprestimo obj = getEnum(valor);
		if (obj != null) {
			return obj.getDescricao();
		}
		return valor;
	}
	
	public static List<SelectItem> getListaSelectItemSituacaoItemEmprestimoVOs(Boolean obrigatorio) {
    	SituacaoItemEmprestimo[] valores = values();
    	List<SelectItem> itens = new ArrayList<SelectItem>(0);
    	if (!obrigatorio) {
    		itens.add(new SelectItem("", ""));
    	}
        for (SituacaoItemEmprestimo obj : valores) {
            itens.add(new SelectItem(obj, obj.getDescricao()));
        }
        return itens;
    }

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
