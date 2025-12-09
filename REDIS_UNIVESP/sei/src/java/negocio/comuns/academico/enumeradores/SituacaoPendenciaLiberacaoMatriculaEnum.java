/**
 * 
 */
package negocio.comuns.academico.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

/**
 * @author Ana Claudia
 *
 */
public enum SituacaoPendenciaLiberacaoMatriculaEnum {
	PENDENTE,
	DEFERIDO,
	INDEFERIDO;
	
	public String getValorApresentar(){
        return UteisJSF.internacionalizar("enum_SituacaoPendenciaLiberacaoMatriculaEnum"+this.name());
    }
}
