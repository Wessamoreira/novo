package negocio.comuns.recursoshumanos.enumeradores;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.utilitarias.UteisJSF;

public enum TipoValorReferenciaEnum {

	FAIXA_VALOR("FAIXA_VALOR", "Admissão"),
	FIXO("FIXO", "Concurso"),
	SQL("SQL", "Nomeação");

	public static List<SelectItem> getTipoValorReferenciaEnum() {
		return montarListaSelectItem(TipoValorReferenciaEnum.values());
	}

	private static List<SelectItem> montarListaSelectItem(TipoValorReferenciaEnum[] modulos) {
		List<SelectItem> tagList = new ArrayList<SelectItem>();
		for (TipoValorReferenciaEnum tag : modulos) {
			tagList.add(new SelectItem(tag, tag.getValorApresentar()));
		}
		return tagList;
	}
	
	String valor;
	String descricao;

	private TipoValorReferenciaEnum(String valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
	}

	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_TipoValorRefereniaEnum_" + this.name());
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getDescricao() {
		return UteisJSF.internacionalizar("enum_TipoValorRefereniaEnum_" + this.valor);
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}


}
