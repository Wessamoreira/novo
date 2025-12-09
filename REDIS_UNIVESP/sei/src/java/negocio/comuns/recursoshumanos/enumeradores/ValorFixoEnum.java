package negocio.comuns.recursoshumanos.enumeradores;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.utilitarias.UteisJSF;

public enum ValorFixoEnum {

	OUTRAS_FINALIDADES,
	SALARIO_MINIMO,
	INSS ,
	IRRF ,
	VALOR_DEDUZIR_DEPENDENTE_IRRF ,
	PERCENTUAL_DEDUCAO_PREVIDENCIA ,
	VALOR_DEDUCAO_PLANO_SAUDE,
	SALARIO_FAMILIA,
	QUANTIDADE_DEPENDENTES_IRRF,
	TABELA_FALTAS_FERIAS;

	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_ValorFixoEnum_" + this.name());
	}

	public static List<SelectItem> getValorFixoEnum() {
		return montarListaSelectItem(ValorFixoEnum.values());
	}

	private static List<SelectItem> montarListaSelectItem(ValorFixoEnum[] modulos) {
		List<SelectItem> tagList = new ArrayList<SelectItem>();
		for (ValorFixoEnum tag : modulos) {
			tagList.add(new SelectItem(tag, tag.getValorApresentar()));
		}
		return tagList;
	}

}
