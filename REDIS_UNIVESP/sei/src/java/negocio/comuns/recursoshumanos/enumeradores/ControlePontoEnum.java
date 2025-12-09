package negocio.comuns.recursoshumanos.enumeradores;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.utilitarias.UteisJSF;

public enum ControlePontoEnum {

	SEM_INFORMACAO("SEM_INFORMACAO", "Sem informação (Somente para Empresas sem Vínculos)."),
	NAO_POSSUI("NAO_POSSUI", "Estabelecimento não adotou sistema de controle de ponto porque em nenhum mês do ano base possuía mais de 10 trabalhadores."),
	SISTEMA_MANUAL("SISTEMA_MANUAL", "Estabelecimento adotou sistema manual."),
	SISTEMA_MECANICO("SISTEMA_MECANICO","Estabelecimento adotou sistema mecânico."),
	SREP("SREP", "Estabelecimento adotou sistema de Registro Eletrônico de Ponto - SREP(Portaria 1.510/2009)."),
	ALTERNATIVO("ALTERNATIVO", "Estabelecimento adotou sistema não eletrônico alternativo previsto no art. 1º da Portaria 373/2011."),
	ELETRONICO_ALTERNATIVO("ELETRONICO_ALTERNATIVO", "Estabelecimento adotou sistema eletrônico alternativo previsto na Portaria 373/2011");

	public static List<SelectItem> getValorControlePontoEnum() {
		return montarListaSelectItem(ControlePontoEnum.values());
	}

	private static List<SelectItem> montarListaSelectItem(ControlePontoEnum[] modulos) {
		List<SelectItem> tagList = new ArrayList<SelectItem>();
		tagList.add(new SelectItem("", ""));
		for (ControlePontoEnum tag : modulos) {
			tagList.add(new SelectItem(tag, tag.getValorApresentar()));
		}
		return tagList;
	}

	String valor;
	String descricao;

	private ControlePontoEnum(String valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
	}

	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_ControlePontoEnum_" + this.name());
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getDescricao() {
		return UteisJSF.internacionalizar("enum_ControlePontoEnum_" + this.valor);
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
