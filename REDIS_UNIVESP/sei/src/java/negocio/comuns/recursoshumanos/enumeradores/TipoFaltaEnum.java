package negocio.comuns.recursoshumanos.enumeradores;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.utilitarias.UteisJSF;

public enum TipoFaltaEnum {
	JUSTIFICADA("JUSTIFICADA", "Justificada"),
	INJUSTIFICADA("INJUSTIFICADA", "Injustificada"),
	ESTORNO("ESTORNO", "Estorno");

	public static List<SelectItem> getValorTipoFaltaEnum() {
		return montarListaSelectItem(TipoFaltaEnum.values());
	}

	private static List<SelectItem> montarListaSelectItem(TipoFaltaEnum[] modulos) {
		List<SelectItem> tagList = new ArrayList<SelectItem>();
		for (TipoFaltaEnum tag : modulos) {
			tagList.add(new SelectItem(tag, tag.getValorApresentar()));
		}
		return tagList;
	}

	String valor;
	String descricao;

	private TipoFaltaEnum(String valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
	}

	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_FaltaEnum_" + this.name());
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getDescricao() {
		return UteisJSF.internacionalizar("enum_FaltaEnum_" + this.name());
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}