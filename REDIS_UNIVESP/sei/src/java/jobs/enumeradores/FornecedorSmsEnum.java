package jobs.enumeradores;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.utilitarias.UteisJSF;

public enum FornecedorSmsEnum {
	
	LOCASMS("LOCASMS", "LOCASMS"), HUMANSMS("HUMANSMS", "HUMANSMS"), FACILITASMS("FACILITASMS", "FACILITASMS"), ROBBUSMS("ROBBUSMS", "ROBBUSMS");
	
	String valor;
    String descricao;
    
	private FornecedorSmsEnum(String valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
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
	
	private static List<SelectItem> listaSelectItemFornecedorSMS;
	
	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_FornecedorSmsEnum_" + this.name());
	}

	public static List<SelectItem> getListaSelectItemItemFornecedorSMS() {
		if (listaSelectItemFornecedorSMS == null) {
			listaSelectItemFornecedorSMS = new ArrayList<SelectItem>(0);
			listaSelectItemFornecedorSMS.add(new SelectItem(FornecedorSmsEnum.LOCASMS, FornecedorSmsEnum.LOCASMS.getValorApresentar()));
			listaSelectItemFornecedorSMS.add(new SelectItem(FornecedorSmsEnum.HUMANSMS, FornecedorSmsEnum.HUMANSMS.getValorApresentar()));
			listaSelectItemFornecedorSMS.add(new SelectItem(FornecedorSmsEnum.FACILITASMS, FornecedorSmsEnum.FACILITASMS.getValorApresentar()));
			listaSelectItemFornecedorSMS.add(new SelectItem(FornecedorSmsEnum.ROBBUSMS, FornecedorSmsEnum.ROBBUSMS.getValorApresentar()));
		}
		return listaSelectItemFornecedorSMS;
	}

}
