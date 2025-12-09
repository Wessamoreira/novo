package negocio.comuns.academico.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum TipoArquivoTCCEnum {
	ARQUIVO_TCC, ARQUIVO_APRESENTACAO, ARQUIVO_APOIO;
	
	public String getValorApresentar(){
		return UteisJSF.internacionalizar("enum_TipoArquivoTCCEnum_"+this.name());
	}
}
