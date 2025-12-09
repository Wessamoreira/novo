package negocio.comuns.recursoshumanos.enumeradores;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.utilitarias.UteisJSF;

public enum TipoEntidadeSindicalEnum {
	
	SINDICATO("SINDICATO", "Sindicato"),
	FEDERACAO("FEDERACAO", "Federacao"),
	CONFEDERACAO("CONFEDERACAO", "Confederacao"),
	MTE("MTE", "MTE");

	public static List<SelectItem> getValorSituacaoPeriodoAquisitivoEnum() {
		return montarListaSelectItem(TipoEntidadeSindicalEnum.values());
	}

	private static List<SelectItem> montarListaSelectItem(TipoEntidadeSindicalEnum[] modulos) {
		List<SelectItem> tagList = new ArrayList<SelectItem>();
		for (TipoEntidadeSindicalEnum tag : modulos) {
			tagList.add(new SelectItem(tag, tag.getValorApresentar()));
		}
		return tagList;
	}

	String valor;
	String descricao;

	private TipoEntidadeSindicalEnum(String valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
	}

	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_TipoEntidadeSindicalEnum_" + this.name());
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getDescricao() {
		return UteisJSF.internacionalizar("enum_TipoEntidadeSindicalEnum_" + this.name());
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}