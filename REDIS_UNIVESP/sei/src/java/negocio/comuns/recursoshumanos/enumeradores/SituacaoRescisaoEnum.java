package negocio.comuns.recursoshumanos.enumeradores;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.utilitarias.UteisJSF;

public enum SituacaoRescisaoEnum {

	ATIVO("ATIVO", "Ativo"),
	INATIVO("INATIVO", "Concurso"),
	FECHADA("FECHADA", "Nomeação"),
	CANCELADO("CANCELADO", "Cancelado");

	public static List<SelectItem> getSituacaoRescisaoEnum() {
		return montarListaSelectItem(SituacaoRescisaoEnum.values());
	}

	private static List<SelectItem> montarListaSelectItem(SituacaoRescisaoEnum[] modulos) {
		List<SelectItem> tagList = new ArrayList<>();
		for (SituacaoRescisaoEnum tag : modulos) {
			tagList.add(new SelectItem(tag, tag.getValorApresentar()));
		}
		return tagList;
	}
	
	String valor;
	String descricao;

	private SituacaoRescisaoEnum(String valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
	}

	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_SituacaoRescisaoEnum_" + this.name());
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getDescricao() {
		return UteisJSF.internacionalizar("enum_SituacaoRescisaoEnum_" + this.valor);
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
