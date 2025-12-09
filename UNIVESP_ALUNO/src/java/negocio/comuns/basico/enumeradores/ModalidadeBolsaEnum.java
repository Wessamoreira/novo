package negocio.comuns.basico.enumeradores;

import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

public enum ModalidadeBolsaEnum {
	NENHUM,
	MESTRANDO, 
	DOUTORANDO,
	ESTAGIO_SEDUC;
	
	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_ModalidadeBolsaEnum_"+this.name());
	}
	
	public static ModalidadeBolsaEnum getEnumPorValor(String valor) {
		try {
			if(Uteis.isAtributoPreenchido(valor) && ModalidadeBolsaEnum.valueOf(Uteis.removerAcentos(valor.toUpperCase().replace(" ", "_"))) != null) {
				return  ModalidadeBolsaEnum.valueOf(Uteis.removerAcentos(valor.toUpperCase().replace(" ", "_")));
			}
			return NENHUM;
		}catch (Exception e) {
			return NENHUM;
		}
	}
}
