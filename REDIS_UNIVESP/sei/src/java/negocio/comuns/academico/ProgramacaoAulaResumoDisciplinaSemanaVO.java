/**
 * 
 */
package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.utilitarias.Uteis;

/**
 * @author Rodrigo Wind
 *
 */
public class ProgramacaoAulaResumoDisciplinaSemanaVO {
	/**
	 * @author Rodrigo Wind - 17/09/2015
	 */
	private DisciplinaVO disciplinaVO;
	private Integer numeroAulaProgramada;
	private Integer cargaHorariaProgramada;
	private String sala;
	private String professor;
	private String horario;
	private String horarioDetalhado;
	/*
	 * ATRIBUTO TRANSIENTE
	 */
	private List<String> listaDataDisciplina;
	private List<String> listaHorarioDisciplina;
	/**
	 * @return the disciplinaVO
	 */
	public DisciplinaVO getDisciplinaVO() {
		if (disciplinaVO == null) {
			disciplinaVO = new DisciplinaVO();
		}
		return disciplinaVO;
	}
	/**
	 * @param disciplinaVO the disciplinaVO to set
	 */
	public void setDisciplinaVO(DisciplinaVO disciplinaVO) {
		this.disciplinaVO = disciplinaVO;
	}
	/**
	 * @return the numeroAulaProgramada
	 */
	public Integer getNumeroAulaProgramada() {
		if (numeroAulaProgramada == null) {
			numeroAulaProgramada = 0;
		}
		return numeroAulaProgramada;
	}
	/**
	 * @param numeroAulaProgramada the numeroAulaProgramada to set
	 */
	public void setNumeroAulaProgramada(Integer numeroAulaProgramada) {
		this.numeroAulaProgramada = numeroAulaProgramada;
	}
	/**
	 * @return the cargaHorariaProgramada
	 */
	public Integer getCargaHorariaProgramada() {
		if (cargaHorariaProgramada == null) {
			cargaHorariaProgramada = 0;
		}
		return cargaHorariaProgramada;
	}
	/**
	 * @param cargaHorariaProgramada the cargaHorariaProgramada to set
	 */
	public void setCargaHorariaProgramada(Integer cargaHorariaProgramada) {
		this.cargaHorariaProgramada = cargaHorariaProgramada;
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
	 * @return the professor
	 */
	public String getProfessor() {
		if (professor == null) {
			professor = "";
		}
		return professor;
	}
	/**
	 * @param professor the professor to set
	 */
	public void setProfessor(String professor) {
		this.professor = professor;
	}
	/**
	 * @return the horario
	 */
	public String getHorario() {
		if (horario == null) {
			horario = "";
		}
		return horario;
	}
	/**
	 * @param horario the horario to set
	 */
	public void setHorario(String horario) {
		this.horario = horario;
	}
	/**
	 * @return the horarioDetalhado
	 */
	public String getHorarioDetalhado() {
		if (horarioDetalhado == null) {
			horarioDetalhado = "";
		}
		return horarioDetalhado;
	}
	/**
	 * @param horarioDetalhado the horarioDetalhado to set
	 */
	public void setHorarioDetalhado(String horarioDetalhado) {
		this.horarioDetalhado = horarioDetalhado;
	}
	
	public String getCargaHorariaProgramacaoEmHoras(){
		return Uteis.converterMinutosEmHorasStr(getCargaHorariaProgramada().doubleValue());
	}
	
	public String getOrdenacao(){
		return getDisciplinaVO().getNome();
	}
	
	public List<String> getListaDataDisciplina() {
		if (listaDataDisciplina == null) {
			listaDataDisciplina = new ArrayList<>(0);
		}
		return listaDataDisciplina;
	}
	
	public void setListaDataDisciplina(List<String> listaDataDisciplina) {
		this.listaDataDisciplina = listaDataDisciplina;
	}

	public List<String> getListaHorarioDisciplina() {
		if (listaHorarioDisciplina == null) {
			listaHorarioDisciplina = new ArrayList<>(0);
		}
		return listaHorarioDisciplina;
	}
	
	public void setListaHorarioDisciplina(List<String> listaHorarioDisciplina) {
		this.listaHorarioDisciplina = listaHorarioDisciplina;
	}
	
}
