package negocio.comuns.recursoshumanos.enumeradores;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.utilitarias.UteisJSF;

public enum SituacaoPeriodoAquisitivoEnum {
	ABERTO("ABERTO", "Aberto"),
	VENCIDO("VENCIDO", "Vencido"),
	PERDIDO("PERDIDO", "Perdido"),
	FECHADO("FECHADO", "Fechado");

	public static List<SelectItem> getValorSituacaoPeriodoAquisitivoEnum() {
		return montarListaSelectItem(SituacaoPeriodoAquisitivoEnum.values());
	}

	private static List<SelectItem> montarListaSelectItem(SituacaoPeriodoAquisitivoEnum[] modulos) {
		List<SelectItem> tagList = new ArrayList<SelectItem>();
		for (SituacaoPeriodoAquisitivoEnum tag : modulos) {
			tagList.add(new SelectItem(tag, tag.getValorApresentar()));
		}
		return tagList;
	}

	String valor;
	String descricao;

	private SituacaoPeriodoAquisitivoEnum(String valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
	}

	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_SituacaoPeriodoAquisitivoEnum_" + this.name());
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getDescricao() {
		return UteisJSF.internacionalizar("enum_SituacaoPeriodoAquisitivoEnum_" + this.name());
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}