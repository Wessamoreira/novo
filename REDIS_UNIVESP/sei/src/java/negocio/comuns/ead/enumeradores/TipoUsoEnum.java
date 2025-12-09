package negocio.comuns.ead.enumeradores;

import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

/*
 * @author Victor Hugo 10/10/2014
 */
public enum TipoUsoEnum {
	
	GERAL, DISCIPLINA, REA, TURMA;
	
	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_TipoUsoEnum_" + this.name());
	}
	
	public String getName() {
		return this.name();
	}
	
	public boolean isGeral() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoUsoEnum.GERAL.name());
	}

	public boolean isDisciplina() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoUsoEnum.DISCIPLINA.name());
	}

	public boolean isRea() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoUsoEnum.REA.name());
	}
	
	public boolean isTurma() {
		return Uteis.isAtributoPreenchido(name()) &&  name().equals(TipoUsoEnum.TURMA.name());
	}
}
