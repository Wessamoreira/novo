package negocio.comuns.basico.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum TipoCampoTagTextoPadraoEnum {
	
	STRING, DOUBLE, INTEGER, BOOLEAN, LISTA, DATA, DATA_COM_HORA, MES_ANO, ENUM;
	
	public String getValorApresentar(){
		return UteisJSF.internacionalizar("enum_TipoCampoTagTextoPadraoEnum_"+ this.name());
	}
}
