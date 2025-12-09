package negocio.comuns.recursoshumanos.enumeradores;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.utilitarias.UteisJSF;

public enum MotivoMudancaCNPJEnum {

	NAO_ALTEROU("NAO_ALTEROU", "Não Alterou"),
	FUSAO("FUSAO", "Fusão"),
	INCORPORACAO("INCORPORACAO", "Incorporação"),
	CISAO("CISAO", "Cisão"),
	MUDANCA_CEI_CGC("MUDANCA_CEI_CGC","Mudança CEI para CGC"),
	ENCERRAMENTO_ATIVDADE("ENCERRAMENTO_ATIVIDADE", "Encerramento das Atividades"),
	MATRICULA_CEI_VINCULADA_CGC("MATRICULA_CEI_VINCULADA_CGC", "Matrícula CEI vinculada ao CGC");

	public static List<SelectItem> getValorMotivoMudancaCNPJEnum() {
		return montarListaSelectItem(MotivoMudancaCNPJEnum.values());
	}

	private static List<SelectItem> montarListaSelectItem(MotivoMudancaCNPJEnum[] modulos) {
		List<SelectItem> tagList = new ArrayList<SelectItem>();
		tagList.add(new SelectItem("", ""));
		for (MotivoMudancaCNPJEnum tag : modulos) {
			tagList.add(new SelectItem(tag, tag.getValorApresentar()));
		}
		return tagList;
	}
	
	String valor;
	String descricao;

	private MotivoMudancaCNPJEnum(String valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
	}

	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_MotivoMudancaCNPJEnum_" + this.name());
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getDescricao() {
		return UteisJSF.internacionalizar("enum_MotivoMudancaCNPJEnum_" + this.valor);
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}
