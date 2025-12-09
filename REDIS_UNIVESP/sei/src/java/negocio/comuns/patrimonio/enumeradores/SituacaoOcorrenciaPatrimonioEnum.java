/**
 * 
 */
package negocio.comuns.patrimonio.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

/**
 * @author Rodrigo Wind
 *
 */
public enum SituacaoOcorrenciaPatrimonioEnum {
	
	EM_ANDAMENTO, FINALIZADO;
	
	public String getValorApresentar(){
		return UteisJSF.internacionalizar("enum_SituacaoOcorrenciaPatrimonioEnum_"+this.name());
	}

}
