package negocio.comuns.recursoshumanos.enumeradores;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.utilitarias.UteisJSF;

public enum PorteEmpresaEnum {

	MICRO_EMPRESA("MICRO_EMPRESA", "Microempresa"),
	PEQUENO_PORTE("PEQUENO_PORTE", "Pequeno Porte"),
	MEI("MEI", "Micro Empreendedor Individual"),
	NENHUMA("NENHUMA","Empresa/Orgão não classificados nos itens anteriores");

	public static List<SelectItem> getValorPorteEmpresaEnum() {
		return montarListaSelectItem(PorteEmpresaEnum.values());
	}

	private static List<SelectItem> montarListaSelectItem(PorteEmpresaEnum[] modulos) {
		List<SelectItem> tagList = new ArrayList<SelectItem>();
		tagList.add(new SelectItem("", ""));
		for (PorteEmpresaEnum tag : modulos) {
			tagList.add(new SelectItem(tag, tag.getValorApresentar()));
		}
		return tagList;
	}

	String valor;
	String descricao;

	private PorteEmpresaEnum(String valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
	}

	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_PorteEmpresaEnum_" + this.name());
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getDescricao() {
		return UteisJSF.internacionalizar("enum_PorteEmpresaEnum_" + this.valor);
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
