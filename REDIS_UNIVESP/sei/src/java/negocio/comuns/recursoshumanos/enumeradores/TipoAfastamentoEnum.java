package negocio.comuns.recursoshumanos.enumeradores;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.utilitarias.UteisJSF;

public enum TipoAfastamentoEnum {

	AFASTAMENTO_ACIDENTE_TRABALHO("AFASTAMENTO_ACIDENTE_TRABALHO", "Afastamento Acidente de Trabalho"),
	LICENCA_MATERNIDADE("LICENCA_MATERNIDADE", "Licença Maternidade"),
	LICENCA_REMUNERADA("LICENCA_REMUNERADA", "Licença Remunerada"),
	LICENCA_MATERNIDADE_PRORROGACAO("LICENCA_MATERNIDADE_PRORROGACAO", "Licença Maternidade - Prorrogação"),
	LICENCA_PATERNIDADE("LICENCA_PATERNIDADE", "Licença Paternidade"),
	AFASTAMENTO_PREVIDENCIA("AFASTAMENTO_PREVIDENCIA", "Afastamento Previdência"),
	LICENCA_SEM_VENCIMENTO("LICENCA_SEM_VENCIMENTO", "Licença sem Vencimentos"),
	OUTROS("OUTROS", "Outros");

	public static List<SelectItem> getValorTipoAfastamentoEnum() {
		return montarListaSelectItem(TipoAfastamentoEnum.values());
	}

	private static List<SelectItem> montarListaSelectItem(TipoAfastamentoEnum[] modulos) {
		List<SelectItem> tagList = new ArrayList<SelectItem>();
		tagList.add(new SelectItem("", ""));
		for (TipoAfastamentoEnum tag : modulos) {
			tagList.add(new SelectItem(tag, tag.getValorApresentar()));
		}
		return tagList;
	}

	String valor;
	String descricao;

	private TipoAfastamentoEnum(String valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
	}

	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_TipoAfastamentoEnum_" + this.name());
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getDescricao() {
		return UteisJSF.internacionalizar("enum_TipoAfastamentoEnum_" + this.valor);
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}