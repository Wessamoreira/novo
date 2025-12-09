package negocio.comuns.biblioteca.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum RegraAplicacaoBloqueioBibliotecaEnum {

	QUANTIDADE_DIAS_ATRASO, QUANTIDADE_DIAS_ESPECIFICOS;
	
	public String getValorApresentar(){
		return UteisJSF.internacionalizar("enum_RegraAplicacaoBloqueioBibliotecaEnum_"+this.name());
	}
	
	
}
