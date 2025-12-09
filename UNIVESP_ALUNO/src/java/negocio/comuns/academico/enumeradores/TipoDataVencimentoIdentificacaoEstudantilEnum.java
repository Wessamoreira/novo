package negocio.comuns.academico.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum TipoDataVencimentoIdentificacaoEstudantilEnum {
	DATA_ESPECIFICA, ULTIMO_DIA_ANO, PERIODO_LETIVO;
	
	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_TipoDataVencimentoIdentificacaoEstudantilEnum_"+this.name());
	}
}
