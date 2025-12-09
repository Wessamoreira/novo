package negocio.comuns.basico.enumeradores;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.utilitarias.UteisJSF;

public enum ModuloLayoutEtiquetaEnum {

	INSCRICAO_SELETIVO, BIBLIOTECA, MATRICULA, PROVA, CARTEIRA_ESTUDANTIL, CARTA_COBRANCA, CRONOGRAMA_AULA,ANIVERSARIANTE;

	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_ModuloLayoutEtiquetaEnum_" + this.name());
	}

	public static List<SelectItem> getModuloLayoutEtiqueta() {
		return montarListaSelectItem(ModuloLayoutEtiquetaEnum.values());
	}

	private static List<SelectItem> montarListaSelectItem(ModuloLayoutEtiquetaEnum[] modulos) {
		List<SelectItem> tagList = new ArrayList<SelectItem>();
		tagList.add(new SelectItem("", ""));
		for (ModuloLayoutEtiquetaEnum tag : modulos) {
			tagList.add(new SelectItem(tag, tag.getValorApresentar()));
		}
		return tagList;
	}
}
