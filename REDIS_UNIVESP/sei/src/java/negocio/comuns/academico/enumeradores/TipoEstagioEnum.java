package negocio.comuns.academico.enumeradores;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

public enum TipoEstagioEnum {

	NENHUM,
	OBRIGATORIO,
	OBRIGATORIO_APROVEITAMENTO,
	OBRIGATORIO_EQUIVALENCIA,
	NAO_OBRIGATORIO;

	public boolean isObrigatorio() {
		return Uteis.isAtributoPreenchido(name()) && 
				(name().equals(TipoEstagioEnum.OBRIGATORIO.name()) || name().equals(TipoEstagioEnum.OBRIGATORIO_APROVEITAMENTO.name()) || name().equals(TipoEstagioEnum.OBRIGATORIO_EQUIVALENCIA.name()));
	}
	
	public boolean isTipoObrigatorio() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoEstagioEnum.OBRIGATORIO.name());
	}
	
	public boolean isTipoObrigatorioAproveitamento() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoEstagioEnum.OBRIGATORIO_APROVEITAMENTO.name());
	}
	public boolean isTipoObrigatorioEquivalencia() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoEstagioEnum.OBRIGATORIO_EQUIVALENCIA.name());
	}
	
	public boolean isTipoNaoObrigatorio() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoEstagioEnum.NAO_OBRIGATORIO.name());
	}

	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_TipoEstagioEnum_" + this.name());
	}

	public static TipoEstagioEnum getEnum(String valor) {
		TipoEstagioEnum[] valores = values();
		for (TipoEstagioEnum obj : valores) {
			if (obj.toString().equals(valor)) {
				return obj;
			}
		}
		return null;
	}

	public static List<SelectItem> combobox;

	public static List<SelectItem> getCombobox() {
		if (combobox == null) {
			combobox = new ArrayList<SelectItem>(0);
			for (TipoEstagioEnum tipoEstagioEnum : values()) {
				combobox.add(new SelectItem(tipoEstagioEnum, UteisJSF.internacionalizar("enum_TipoEstagioEnum_" + tipoEstagioEnum.name())));
			}
		}
		return combobox;
	}

}
