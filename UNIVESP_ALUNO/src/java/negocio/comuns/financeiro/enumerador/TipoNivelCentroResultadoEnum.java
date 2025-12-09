package negocio.comuns.financeiro.enumerador;

import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

/**
 * 
 * @author PedroOtimize
 *
 */
public enum TipoNivelCentroResultadoEnum {
	UNIDADE_ENSINO, DEPARTAMENTO, CURSO, CURSO_TURNO, TURMA;

	public boolean isUnidadeEnsino() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoNivelCentroResultadoEnum.UNIDADE_ENSINO.name());
	}

	public boolean isDepartamento() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoNivelCentroResultadoEnum.DEPARTAMENTO.name());
	}

	public boolean isCurso() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoNivelCentroResultadoEnum.CURSO.name());
	}

	public boolean isCursoTurno() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoNivelCentroResultadoEnum.CURSO_TURNO.name());
	}

	public boolean isTurma() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoNivelCentroResultadoEnum.TURMA.name());
	}
	
	public static String valorApresentar(TipoNivelCentroResultadoEnum tipo) {
		if (Uteis.isAtributoPreenchido(tipo)) {
			return UteisJSF.internacionalizar(""+tipo.name());
		} else {
			return "";
		}
	}

}
