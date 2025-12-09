package negocio.comuns.basico.enumeradores;

import java.util.Iterator;

import negocio.comuns.utilitarias.UteisJSF;

public enum SituacaoMilitarEnum {
	
	ALISTAMENTO,
	ATIVA,
	CARTA_PATENTE,
	DISPENSADO,
	ISENTO,
	RESERVISTA;
	
	public String getValorApresentar(){
		return UteisJSF.internacionalizar("enum_SituacaoMilitarEnum_"+this.name());
	}

	public static Boolean getExisteValor(String valor) {
		for (SituacaoMilitarEnum situacaoMilitarEnum : SituacaoMilitarEnum.values()) {
			if (situacaoMilitarEnum.toString().equals(valor)) {
				return true;
			}
		}
		return false;
	}
}
