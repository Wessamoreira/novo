package negocio.comuns.estagio.enumeradores;

import negocio.comuns.utilitarias.Uteis;

public enum TipoConsultaComboSituacaoAproveitamentoEnum {	
	 
	NENHUM,
	DOCENTE_REGULAR, 
	LICENCIADO_OUTRO_CURSO;	
	
	public boolean isDocenteRegular() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoConsultaComboSituacaoAproveitamentoEnum.DOCENTE_REGULAR.name());
	}
	
	public boolean isLicenciadoOutroCurso() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoConsultaComboSituacaoAproveitamentoEnum.LICENCIADO_OUTRO_CURSO.name());
	}

	public boolean isNenhum() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoConsultaComboSituacaoAproveitamentoEnum.NENHUM.name());
	}
	
}
