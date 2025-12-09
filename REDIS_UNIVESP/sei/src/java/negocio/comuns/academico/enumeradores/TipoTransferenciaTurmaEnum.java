/**
 * 
 */
package negocio.comuns.academico.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

/**
 * @author Rodrigo Wind
 *
 */
public enum TipoTransferenciaTurmaEnum {
	TURMA_BASE, TURMA_PRATICA, TURMA_TEORICA;
	
	public String getValorApresentar(){
		return UteisJSF.internacionalizar("enum_TipoTransferenciaTurmaEnum_"+this.name());
	}
}
