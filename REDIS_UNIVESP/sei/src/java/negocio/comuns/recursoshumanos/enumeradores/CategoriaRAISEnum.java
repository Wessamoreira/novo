package negocio.comuns.recursoshumanos.enumeradores;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.utilitarias.UteisJSF;

public enum CategoriaRAISEnum {

	ENTIDADE_FILANTROPICA("ENTIDADE_FILANTROPICA", "Entidade Filantrópica"),
	CLUBE_FUTEBOL("CLUBE_FUTEBOL", "Clube de Futebol"),
	PESSOA_FISICA_URBANA("PESSOA_FISICA_URBANA", "Pessoa Física urbana"),
	PESSOA_FISICA_RURAL("PESSOA_FISICA_RURAL","Pessoa Física rural"),
	PESSOA_JURIDICA_RURAL("PESSOA_JURIDICA_RURAL", "Pessoa jurídica rural"),
	SINDICATO("SINDICATO", "Sindicato Agregado Trabalho Avulso"),
	OUTROS("OUTROS", "Outros");

	public static List<SelectItem> getValorCategoriaRAISEnum() {
		return montarListaSelectItem(CategoriaRAISEnum.values());
	}

	private static List<SelectItem> montarListaSelectItem(CategoriaRAISEnum[] modulos) {
		List<SelectItem> tagList = new ArrayList<SelectItem>();
		tagList.add(new SelectItem("", ""));
		for (CategoriaRAISEnum tag : modulos) {
			tagList.add(new SelectItem(tag, tag.getValorApresentar()));
		}
		return tagList;
	}

	String valor;
	String descricao;

	private CategoriaRAISEnum(String valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
	}

	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_CategoriaRAISEnum_" + this.name());
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getDescricao() {
		return UteisJSF.internacionalizar("enum_CategoriaRAISEnum_" + this.valor);
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}
