package negocio.comuns.recursoshumanos.enumeradores;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.utilitarias.UteisJSF;

public enum OptanteSimpleEnum {

	NAO_OPTANTE("NAO_OPTANTE", "Não optante"),
	OPTANTE("OPTANTE", "Optante"),
	OPTANTE_FATURAMENTO("OPTANTE_FATURAMENTO", "Optante - faturamento anual superior a R$ 1.200.000,00"),
	NAO_OPTANTE_PRODUTOR_RURAL("NAO_OPTANTE_PRODUTOR_RURAL", "Não optante - Produtor Rural Pessoa Física (CEI e FPAS 604) com faturamento anual superior a R$ 1.200.000,00"),
	NAO_OPTANTE_CONTRIBUICAO_SOCIAL("NAO_OPTANTE_CONTRIBUICAO_SOCIAL", "Não optante - Empresa com liminar para não recolhimento da Contribuição Social - Lei Complementar"),
	OPTANTE_FATURAMENTO_CONTRIBUICAO_SOCIAL("OPTANTE_FATURAMENTO_CONTRIBUICAO_SOCIAL", "Optante - Faturamento anual superior a R$ 1.200.000,00 - Empresa com liminar para não recolhimento da Contribuição Social - Lei comp 110/01 de 26/06/2001");

	public static List<SelectItem> getValorOptanteSimpleEnum() {
		return montarListaSelectItem(OptanteSimpleEnum.values());
	}

	private static List<SelectItem> montarListaSelectItem(OptanteSimpleEnum[] modulos) {
		List<SelectItem> tagList = new ArrayList<SelectItem>();
		tagList.add(new SelectItem("", ""));
		for (OptanteSimpleEnum tag : modulos) {
			tagList.add(new SelectItem(tag, tag.getValorApresentar()));
		}
		return tagList;
	}
	
	String valor;
	String descricao;

	private OptanteSimpleEnum(String valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
	}

	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_OptanteSimpleEnum_" + this.name());
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getDescricao() {
		return UteisJSF.internacionalizar("enum_OptanteSimpleEnum_" + this.valor);
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}