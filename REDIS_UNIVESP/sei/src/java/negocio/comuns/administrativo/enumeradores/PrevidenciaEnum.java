package negocio.comuns.administrativo.enumeradores;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.utilitarias.UteisJSF;

public enum PrevidenciaEnum {

	INSS("INSS", "INSS"),
	PREVIDENCIA_PROPRIA("PREVIDENCIA_PROPRIA", "PREVIDENCIA PROPRIA"),
	OUTROS("OUTROS", "OUTROS");

	private PrevidenciaEnum(String valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
	}

	public static List<SelectItem> getTipoValorReferenciaEnum() {
		return montarListaSelectItem(PrevidenciaEnum.values());
	}

	private static List<SelectItem> montarListaSelectItem(PrevidenciaEnum[] modulos) {
		List<SelectItem> tagList = new ArrayList<SelectItem>();
		for (PrevidenciaEnum tag : modulos) {
			tagList.add(new SelectItem(tag, tag.getValorApresentar()));
		}
		return tagList;
	}

	String valor;
	String descricao;

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getDescricao() {
		return UteisJSF.internacionalizar("enum_PrevidenciaEnum_" + this.valor);
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_PrevidenciaEnum_" + this.name());
	}
}
