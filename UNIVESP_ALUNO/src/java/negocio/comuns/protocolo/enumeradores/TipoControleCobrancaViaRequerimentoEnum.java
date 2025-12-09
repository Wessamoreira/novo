package negocio.comuns.protocolo.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum TipoControleCobrancaViaRequerimentoEnum {

		PERIODO_MATRICULA, CURSO, MENSAL;
		
		public String getValorApresentar(){
			return UteisJSF.internacionalizar("enum_TipoControleCobrancaViaRequerimentoEnum_"+this.name());
		}
		
		
}
