/**
 * 
 */
package negocio.comuns.academico.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

/**
 * @author Ana Claudia
 *
 */
public enum MotivoSolicitacaoLiberacaoMatriculaEnum {
	SOLICITAR_APROVACAO_LIBERACAO_FINANCEIRA,
	SOLICITAR_LIBERACAO_MATRICULA_APOS_X_MODULOS;
	
	public String getValorApresentar(){
        return UteisJSF.internacionalizar("enum_MotivoSolicitacaoLiberacaoMatriculaEnum"+this.name());
    }
}
