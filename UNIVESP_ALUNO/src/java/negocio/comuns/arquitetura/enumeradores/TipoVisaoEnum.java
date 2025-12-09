/**
 * 
 */
package negocio.comuns.arquitetura.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

/**
 * @author Rodrigo Wind
 *
 */
public enum TipoVisaoEnum {
	ADMINISTRATIVA,
	ALUNO,
	PAIS, 
	PROFESSOR,
	COORDENADOR,
	CANDIDATO,
	PARCEIRO;
	
	public String getValorApresentar(){
		return UteisJSF.internacionalizar("enum_TipoVisaoEnum_"+this.name());
	}
	
	
}
