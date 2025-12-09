package negocio.comuns.academico.enumeradores;

import negocio.comuns.utilitarias.Uteis;

/**
 * 
 * @author pedroandrade
 *
 */
public enum TipoUsoProcessoMatriculaEnum {
	CALOURO, 
	VETERANO,
	AMBOS;
	
	public boolean isCalouro() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoUsoProcessoMatriculaEnum.CALOURO.name());
	}
	
	public boolean isVeterano() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoUsoProcessoMatriculaEnum.VETERANO.name());
	}
	
	public boolean isAmbos() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoUsoProcessoMatriculaEnum.AMBOS.name());
	}

}
