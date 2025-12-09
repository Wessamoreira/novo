package webservice.servicos.objetos.enumeradores;

import negocio.comuns.ead.enumeradores.TipoUsoEnum;
import negocio.comuns.utilitarias.Uteis;

/**
 * @author Victor Hugo de Paula Costa - 1 de nov de 2016
 *
 */
public enum OrigemAgendaAlunoEnum {
	/**
	 * @author Victor Hugo de Paula Costa - 1 de nov de 2016
	 */
	FERIADO, 
	HORARIO_AULA,
	HORARIO_PROFESSOR,
	CALENDARIO_EAD, 
	REQUERIMENTO, 
	EVENTOS;
	
	public boolean isFeriado() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(OrigemAgendaAlunoEnum.FERIADO.name());
	}
	
	public boolean isHorarioProfessor() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(OrigemAgendaAlunoEnum.HORARIO_PROFESSOR.name());
	}
	
	public boolean isHorarioAula() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(OrigemAgendaAlunoEnum.HORARIO_AULA.name());
	}
	
	public boolean isCalendarioEad() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(OrigemAgendaAlunoEnum.CALENDARIO_EAD.name());
	}
	
	public boolean isRequerimento() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(OrigemAgendaAlunoEnum.REQUERIMENTO.name());
	}
	
	public boolean isEventos() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(OrigemAgendaAlunoEnum.EVENTOS.name());
	}
	
}
