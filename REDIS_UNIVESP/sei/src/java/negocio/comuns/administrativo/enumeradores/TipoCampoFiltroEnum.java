package negocio.comuns.administrativo.enumeradores;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

public enum TipoCampoFiltroEnum {
	TEXTUAL("Textual"), 
	INTEIRO("Inteiro"), 
	DATA("Data"), 
	BOOLEAN("Boolean"),
	MULTIPLA_ESCOLHA("Múltipla Escolha"), 
	SIMPLES_ESCOLHA("Simples Escolha"),
	COMBOBOX("Combobox"),
	COMBOBOX_CUSTOMIZAVEL("Combobox Customizável");
	
	
	private String descricao;
	
	TipoCampoFiltroEnum(String descricao) {
		this.descricao = descricao;
	}
	
	public static List<SelectItem> getListaSelectItemTipoCampoFiltroVOs() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		TipoCampoFiltroEnum valores [] = values();
		for (TipoCampoFiltroEnum obj : valores) {
			itens.add(new SelectItem(obj, obj.getDescricao()));
		}
		return itens;
	}

	public String getDescricao() {
		if (descricao == null) {
			descricao = "";
		}
		return descricao;
	}


	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
