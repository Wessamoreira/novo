package negocio.comuns.recursoshumanos.enumeradores;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.utilitarias.UteisJSF;

public enum SituacaoMarcacaoFeriasEnum {
	MARCADA("MARCADA", "Marcada"),
	CALCULADA("CALCULADA", "Calculada"),
	FECHADA("FECHADA", "Fechada");

	public static List<SelectItem> getValorSituacaoMarcacaoFeriasEnum() {
		return montarListaSelectItem(SituacaoMarcacaoFeriasEnum.values());
	}

	private static List<SelectItem> montarListaSelectItem(SituacaoMarcacaoFeriasEnum[] modulos) {
		List<SelectItem> tagList = new ArrayList<SelectItem>();
		for (SituacaoMarcacaoFeriasEnum tag : modulos) {
			tagList.add(new SelectItem(tag, tag.getValorApresentar()));
		}
		return tagList;
	}

	String valor;
	String descricao;

	private SituacaoMarcacaoFeriasEnum(String valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
	}

	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_SituacaoMarcacaoFeriasEnum_" + this.name());
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getDescricao() {
		return UteisJSF.internacionalizar("enum_SituacaoMarcacaoFeriasEnum_" + this.name());
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}