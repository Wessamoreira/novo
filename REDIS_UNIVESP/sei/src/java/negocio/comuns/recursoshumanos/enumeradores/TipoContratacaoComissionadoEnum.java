package negocio.comuns.recursoshumanos.enumeradores;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.utilitarias.UteisJSF;

public enum TipoContratacaoComissionadoEnum {

	ADMISSAO("ADMISSAO", "Admissão"),
	DEMISSAO("DEMISSAO", "Demissão"),
	CONCURSO("CONCURSO", "Concurso"),
	NOMEACAO("NOMEACAO", "Nomeação"),
	MUDANCA_CARGO("MUDANCA_CARGO","Mudança de Cargo"),
	ADEQUACAO_TABELA_SALARIAL("ADEQUACAO_TABELA_SALARIAL", "Adequação Tabela Salarial"),
	PROGRESSAO_SALARIAL("PROGRESSAO_SALARIAL", "Progressão"),
	OUTROS("OUTROS", "Outros");

	public static List<SelectItem> getTipoContratacaoComissionadoEnum() {
		return montarListaSelectItem(TipoContratacaoComissionadoEnum.values());
	}

	private static List<SelectItem> montarListaSelectItem(TipoContratacaoComissionadoEnum[] modulos) {
		List<SelectItem> tagList = new ArrayList<SelectItem>();
		tagList.add(new SelectItem("", ""));
		for (TipoContratacaoComissionadoEnum tag : modulos) {
			tagList.add(new SelectItem(tag, tag.getValorApresentar()));
		}
		return tagList;
	}
	
	String valor;
	String descricao;

	private TipoContratacaoComissionadoEnum(String valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
	}

	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_TipoContratacaoComissionadoEnum_" + this.name());
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getDescricao() {
		return UteisJSF.internacionalizar("enum_TipoContratacaoComissionadoEnum_" + this.valor);
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}


}
