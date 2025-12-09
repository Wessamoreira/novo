package negocio.comuns.academico;

import java.util.Date;

import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.DiaSemana;

public class ChoqueHorarioVO extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2123403788195295914L;
	private Date data;
	private Integer nrAula;
	private Integer nrAulaBase;
	private String horario;
	private String horarioBase;
	private String turma;
	private String turmaBase;
	private String disciplina;
	private String disciplinaBase;
	private String professor;
	private String professorBase;	
	private String turno;
	private String turnoBase;
	private String sala;	
	private String salaBase;	
	private String feriado;
	private Boolean aulaRegistrada;
	private Boolean choqueHorarioProfessor;
	private Boolean choqueHorarioSala;
	private Boolean choqueHorarioAulaExcesso;
	
	
	
	public ChoqueHorarioVO() {
		super();
	}

	public ChoqueHorarioVO(Date data, Integer nrAula, String horario, String turma, String disciplina, String professor, String turno, String feriado,  Boolean aulaRegistrada) {
		super();
		this.data = data;
		this.nrAula = nrAula;
		this.horario = horario;
		this.turma = turma;
		this.disciplina = disciplina;
		this.professor = professor;
		this.turno = turno;
		this.feriado = feriado;
		this.aulaRegistrada = aulaRegistrada;
	}
	
	

	public ChoqueHorarioVO(Date data, Integer nrAula, Integer nrAulaBase, String horario, String horarioBase, String turma, String turmaBase, String disciplina, String disciplinaBase, String professor, String professorBase, String turno, String turnoBase, String feriado, Boolean aulaRegistrada, Boolean choqueHorarioProfessor) {
		super();
		this.data = data;
		this.nrAula = nrAula;
		this.nrAulaBase = nrAulaBase;
		this.horario = horario;
		this.horarioBase = horarioBase;
		this.turma = turma;
		this.turmaBase = turmaBase;
		this.disciplina = disciplina;
		this.disciplinaBase = disciplinaBase;
		this.professor = professor;
		this.professorBase = professorBase;
		this.turno = turno;
		this.turnoBase = turnoBase;
		this.feriado = feriado;
		this.aulaRegistrada = aulaRegistrada;
		this.choqueHorarioProfessor = choqueHorarioProfessor;
	}
	
	public ChoqueHorarioVO(Date data, String turma, String disciplina, String professor, String sala) {
		super();
		this.data = data;		
		this.turma = turma;
		this.disciplina = disciplina;
		this.professor = professor;
		this.sala = sala;
		this.choqueHorarioSala = true;
	}

	public Boolean getAulaRegistrada() {
		if (aulaRegistrada == null) {
			aulaRegistrada = false;
		}
		return aulaRegistrada;
	}

	public void setAulaRegistrada(Boolean aulaRegistrada) {
		this.aulaRegistrada = aulaRegistrada;
	}

	public String getDataApresentar(){
		return Uteis.getData(data);
	}
	
	public Date getData() {		
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public Integer getNrAula() {
		if (nrAula == null) {
			nrAula = 0;
		}
		return nrAula;
	}
	public void setNrAula(Integer nrAula) {
		this.nrAula = nrAula;
	}
	public String getHorario() {
		if (horario == null) {
			horario = "";
		}
		return horario;
	}
	public void setHorario(String horario) {
		this.horario = horario;
	}
	public String getTurma() {
		if (turma == null) {
			turma = "";
		}
		return turma;
	}
	public void setTurma(String turma) {
		this.turma = turma;
	}
	public String getDisciplina() {
		if (disciplina == null) {
			disciplina = "";
		}
		return disciplina;
	}
	public void setDisciplina(String disciplina) {
		this.disciplina = disciplina;
	}
	public String getProfessor() {
		if (professor == null) {
			professor = "";
		}
		return professor;
	}
	public void setProfessor(String professor) {
		this.professor = professor;
	}
	public String getTurno() {
		if (turno == null) {
			turno = "";
		}
		return turno;
	}
	public void setTurno(String turno) {
		this.turno = turno;
	}
	public String getFeriado() {
		if (feriado == null) {
			feriado = "";
		}
		return feriado;
	}
	public void setFeriado(String feriado) {
		this.feriado = feriado;
	}

	public Boolean getChoqueHorarioProfessor() {
		if (choqueHorarioProfessor == null) {
			choqueHorarioProfessor = false;
		}
		return choqueHorarioProfessor;
	}

	public void setChoqueHorarioProfessor(Boolean choqueHorarioProfessor) {
		this.choqueHorarioProfessor = choqueHorarioProfessor;
	}

	public Integer getNrAulaBase() {
		if (nrAulaBase == null) {
			nrAulaBase = 0;
		}
		return nrAulaBase;
	}

	public void setNrAulaBase(Integer nrAulaBase) {
		this.nrAulaBase = nrAulaBase;
	}

	public String getHorarioBase() {
		if (horarioBase == null) {
			horarioBase = "";
		}
		return horarioBase;
	}

	public void setHorarioBase(String horarioBase) {
		this.horarioBase = horarioBase;
	}

	public String getTurmaBase() {
		if (turmaBase == null) {
			turmaBase = "";
		}
		return turmaBase;
	}

	public void setTurmaBase(String turmaBase) {
		this.turmaBase = turmaBase;
	}

	public String getDisciplinaBase() {
		if (disciplinaBase == null) {
			disciplinaBase = "";
		}
		return disciplinaBase;
	}

	public void setDisciplinaBase(String disciplinaBase) {
		this.disciplinaBase = disciplinaBase;
	}

	public String getTurnoBase() {
		if (turnoBase == null) {
			turnoBase = "";
		}
		return turnoBase;
	}

	public void setTurnoBase(String turnoBase) {
		this.turnoBase = turnoBase;
	}

	@Override
	public String getMessage() {
		if(getAulaRegistrada()){
			UteisJSF.internacionalizar("msg_HorarioTurma_horarioTurmaDiaComRegistroAula").replace("{0}", getDataApresentar()).replace("{1}", getHorario()).replace("{2}", getDisciplina()).replace("{3}", getProfessor());
		}
		if(getChoqueHorarioProfessor()){
			return UteisJSF.internacionalizar("msg_HorarioTurma_choqueHorario").replace("{0}", getNrAulaBase().toString() + "(" + getHorarioBase() + ")").replace("{1}", Uteis.getData(getData(), "dd/MM/yyyy")).replace("{2}", getTurmaBase()).replace("{3}", Uteis.getData(getData(), "dd/MM/yyyy")).replace("{4}", getNrAula().toString() + "(" + getHorario() + ")").replace("{5}", getTurma()).replace("{6}", getDisciplina()).replace("{7}", getTurno());
		}
		if(getChoqueHorarioSala()){
			return UteisJSF.internacionalizar("msg_HorarioTurma_choqueHorarioSala").replace("{0}", (Uteis.getData(getData(), "dd/MM/yyyy")+"("+DiaSemana.getAbreviatura(Uteis.getDiaSemanaEnum(getData()).getValor())+")")).replace("{1}", getNrAula().toString()).replace("{2}", getSala()).replace("{3}", getTurma()).replace("{4}", getDisciplina()).replace("{5}", getProfessor());
		}
		return super.getMessage();
	}

	public String getProfessorBase() {
		if (professorBase == null) {
			professorBase = "";
		}
		return professorBase;
	}

	public void setProfessorBase(String professorBase) {
		this.professorBase = professorBase;
	}

	/**
	 * @return the sala
	 */
	public String getSala() {
		if (sala == null) {
			sala = "";
		}
		return sala;
	}

	/**
	 * @param sala the sala to set
	 */
	public void setSala(String sala) {
		this.sala = sala;
	}

	/**
	 * @return the salaBase
	 */
	public String getSalaBase() {
		if (salaBase == null) {
			salaBase = "";
		}
		return salaBase;
	}

	/**
	 * @param salaBase the salaBase to set
	 */
	public void setSalaBase(String salaBase) {
		this.salaBase = salaBase;
	}

	/**
	 * @return the choqueHorarioSala
	 */
	public Boolean getChoqueHorarioSala() {
		if (choqueHorarioSala == null) {
			choqueHorarioSala = false;
		}
		return choqueHorarioSala;
	}

	/**
	 * @param choqueHorarioSala the choqueHorarioSala to set
	 */
	public void setChoqueHorarioSala(Boolean choqueHorarioSala) {
		this.choqueHorarioSala = choqueHorarioSala;
	}

	/**
	 * @return the choqueHorarioAulaExcesso
	 */
	public Boolean getChoqueHorarioAulaExcesso() {
		if (choqueHorarioAulaExcesso == null) {
			choqueHorarioAulaExcesso = false;
		}
		return choqueHorarioAulaExcesso;
	}

	/**
	 * @param choqueHorarioAulaExcesso the choqueHorarioAulaExcesso to set
	 */
	public void setChoqueHorarioAulaExcesso(Boolean choqueHorarioAulaExcesso) {
		this.choqueHorarioAulaExcesso = choqueHorarioAulaExcesso;
	}
	
	
    public String getOrdenacao(){
    	return getData().getTime()+"-"+getNrAula();
    }		
	
}
