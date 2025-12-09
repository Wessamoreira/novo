/**
 * 
 */
package negocio.comuns.secretaria.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

/**
 * @author Carlos Eugênio
 *
 */
public enum SituacaoMatriculaProvaPresencialEnum {
	MATRICULA_ENCONTRADA,
	MATRICULA_NAO_ENCONTRADA_ARQUIVO,
	MATRICULA_NAO_ENCONTRADA_SEI;
	
	public String getValorApresentar(){
        return UteisJSF.internacionalizar("enum_SituacaoMatriculaProvaPresencialEnum_"+this.name());
    }
}
