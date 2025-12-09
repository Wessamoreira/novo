/**
 * 
 */
package negocio.comuns.ead.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

/**
 * @author Rodrigo Wind
 *
 */
public enum PublicoAlvoAtividadeDiscursivaEnum {
	
	TURMA, ALUNO;
	
	public String getValorApresentar(){
		return UteisJSF.internacionalizar("enum_PublicoAlvoAtividadeDiscursivaEnum_"+this.name());
	}

	public Boolean getIsTipoAluno(){
		return this.equals(PublicoAlvoAtividadeDiscursivaEnum.ALUNO);
	}
	
	public Boolean getIsTipoTurma(){
		return this.equals(PublicoAlvoAtividadeDiscursivaEnum.TURMA);
	}
	
}
