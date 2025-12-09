package negocio.comuns.patrimonio.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum SituacaoPatrimonioUnidadeEnum {
	
	EM_MANUTENCAO, EMPRESTADO, DISPONIVEL, SEPARADO_PARA_DESCARTE, DESCARTADO, COM_DEFEITO;

	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_SituacaoPatrimonioUnidadeEnum_" + this.name());
	}

}
