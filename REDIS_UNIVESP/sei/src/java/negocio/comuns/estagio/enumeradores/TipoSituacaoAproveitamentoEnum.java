package negocio.comuns.estagio.enumeradores;

import negocio.comuns.utilitarias.Uteis;

public enum TipoSituacaoAproveitamentoEnum {	
	 
	NENHUM, 
	DOCENTE_REGULAR, 
	LICENCIADO_OUTRO_CURSO;	
	
	public boolean isDocenteRegular() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoSituacaoAproveitamentoEnum.DOCENTE_REGULAR.name());
	}
	
	public boolean isLicenciadoOutroCurso() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoSituacaoAproveitamentoEnum.LICENCIADO_OUTRO_CURSO.name());
	}
	
}
