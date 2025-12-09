package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.blackboard.SalaAulaBlackboardVO;
import negocio.comuns.gsuite.ClassroomGoogleVO;

public class HorarioTurmaDisciplinaProgramadaVO extends SuperVO {

	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private String nomeDisciplina;
	private Integer codigoDisciplina;
	private Integer chDisciplina;
	private Integer creditoDisciplina;
	private Integer creditoDisciplinaProgramada;
	private Integer chProgramada;
	private Integer horaAula;
	private String professores;	
	private SalaLocalAulaVO sala;
	private List<HorarioTurmaDisciplinaProgramadaVO> turmaAulaProgramadaVOs; 
	private List<String> professorVOs;
	private HorarioTurmaVO horarioTurmaVO;
	private String modalidadeDisciplina;
	private ClassroomGoogleVO classroomGoogleVO;
	private SalaAulaBlackboardVO salaAulaBlackboardVO;
	private Boolean registrarAulaAutomaticamente;
	private String conteudo;
	private String definicoesTutoriaOnline;

	public String getNomeDisciplina() {
		if (nomeDisciplina == null) {
			nomeDisciplina = "";
		}
		return nomeDisciplina;
	}

	public void setNomeDisciplina(String nomeDisciplina) {
		this.nomeDisciplina = nomeDisciplina;
	}

	public Integer getChDisciplina() {
		if (chDisciplina == null) {
			chDisciplina = 0;
		}
		return chDisciplina;
	}

	public void setChDisciplina(Integer chDisciplina) {
		this.chDisciplina = chDisciplina;
	}

	public Integer getChProgramada() {
		if (chProgramada == null) {
			chProgramada = 0;
		}
		return chProgramada;
	}

	public void setChProgramada(Integer chProgramada) {
		this.chProgramada = chProgramada;
	}

	public String getProfessores() {
		if (professores == null) {
			professores = "";
		}
		return professores;
	}

	public void setProfessores(String professores) {
		this.professores = professores;
	}

	public Integer getCodigoDisciplina() {
		if(codigoDisciplina == null){
			codigoDisciplina = 0;
		}
		return codigoDisciplina;
	}

	public void setCodigoDisciplina(Integer codigoDisciplina) {
		this.codigoDisciplina = codigoDisciplina;
	}

	/**
	 * @return the creditoDisciplina
	 */
	public Integer getCreditoDisciplina() {
		if (creditoDisciplina == null) {
			creditoDisciplina = 0;
		}
		return creditoDisciplina;
	}

	/**
	 * @param creditoDisciplina the creditoDisciplina to set
	 */
	public void setCreditoDisciplina(Integer creditoDisciplina) {
		this.creditoDisciplina = creditoDisciplina;
	}

	/**
	 * @return the creditoDisciplinaProgramada
	 */
	public Integer getCreditoDisciplinaProgramada() {
		if (creditoDisciplinaProgramada == null) {
			creditoDisciplinaProgramada = 0;
		}
		return creditoDisciplinaProgramada;
	}

	/**
	 * @param creditoDisciplinaProgramada the creditoDisciplinaProgramada to set
	 */
	public void setCreditoDisciplinaProgramada(Integer creditoDisciplinaProgramada) {
		this.creditoDisciplinaProgramada = creditoDisciplinaProgramada;
	}

	/**
	 * @return the horaAula
	 */
	public Integer getHoraAula() {
		if (horaAula == null) {
			horaAula = 0;
		}
		return horaAula;
	}

	/**
	 * @param horaAula the horaAula to set
	 */
	public void setHoraAula(Integer horaAula) {
		this.horaAula = horaAula;
	}

	/**
	 * @return the sala
	 */
	public SalaLocalAulaVO getSala() {
		if (sala == null) {
			sala = new SalaLocalAulaVO();
		}
		return sala;
	}

	/**
	 * @param sala the sala to set
	 */
	public void setSala(SalaLocalAulaVO sala) {
		this.sala = sala;
	}

	/**
	 * @return the turmaAulaProgramadaVOs
	 */
	public List<HorarioTurmaDisciplinaProgramadaVO> getTurmaAulaProgramadaVOs() {
		if (turmaAulaProgramadaVOs == null) {
			turmaAulaProgramadaVOs = new ArrayList<HorarioTurmaDisciplinaProgramadaVO>(0);
		}
		return turmaAulaProgramadaVOs;
	}

	/**
	 * @param turmaAulaProgramadaVOs the turmaAulaProgramadaVOs to set
	 */
	public void setTurmaAulaProgramadaVOs(List<HorarioTurmaDisciplinaProgramadaVO> turmaAulaProgramadaVOs) {
		this.turmaAulaProgramadaVOs = turmaAulaProgramadaVOs;
	}

	/**
	 * @return the professorVOs
	 */
	public List<String> getProfessorVOs() {
		if (professorVOs == null) {
			professorVOs = new ArrayList<String>(0);
		}
		return professorVOs;
	}

	/**
	 * @param professorVOs the professorVOs to set
	 */
	public void setProfessorVOs(List<String> professorVOs) {
		this.professorVOs = professorVOs;
	}

	/**
	 * @return the horarioTurmaVO
	 */
	public HorarioTurmaVO getHorarioTurmaVO() {
		if (horarioTurmaVO == null) {
			horarioTurmaVO = new HorarioTurmaVO();
		}
		return horarioTurmaVO;
	}

	/**
	 * @param horarioTurmaVO the horarioTurmaVO to set
	 */
	public void setHorarioTurmaVO(HorarioTurmaVO horarioTurmaVO) {
		this.horarioTurmaVO = horarioTurmaVO;
	}

	public String getModalidadeDisciplina() {
		return modalidadeDisciplina;
	}

	public void setModalidadeDisciplina(String modalidadeDisciplina) {
		this.modalidadeDisciplina = modalidadeDisciplina;
	}

	public ClassroomGoogleVO getClassroomGoogleVO() {
		if(classroomGoogleVO == null) {
			classroomGoogleVO = new ClassroomGoogleVO();
		}
		return classroomGoogleVO;
	}

	public void setClassroomGoogleVO(ClassroomGoogleVO classroomGoogleVO) {
		this.classroomGoogleVO = classroomGoogleVO;
	}
	
	

	public SalaAulaBlackboardVO getSalaAulaBlackboardVO() {
		if (salaAulaBlackboardVO == null) {
			salaAulaBlackboardVO = new SalaAulaBlackboardVO();
		}
		return salaAulaBlackboardVO;
	}

	public void setSalaAulaBlackboardVO(SalaAulaBlackboardVO salaAulaBlackboardVO) {
		this.salaAulaBlackboardVO = salaAulaBlackboardVO;
	}

	public Boolean getRegistrarAulaAutomaticamente() {
		if (registrarAulaAutomaticamente == null) {
			registrarAulaAutomaticamente = false;
		}
		return registrarAulaAutomaticamente;
	}

	public void setRegistrarAulaAutomaticamente(Boolean registrarAulaAutomaticamente) {
		this.registrarAulaAutomaticamente = registrarAulaAutomaticamente;
	}

	public String getConteudo() {
		if (conteudo == null) {
			conteudo = "";
		}
		return conteudo;
	}

	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getDefinicoesTutoriaOnline() {
		return definicoesTutoriaOnline;
	}

	public void setDefinicoesTutoriaOnline(String definicoesTutoriaOnline) {
		this.definicoesTutoriaOnline = definicoesTutoriaOnline;
	}

	

}
