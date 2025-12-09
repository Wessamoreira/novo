package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import negocio.comuns.arquitetura.SuperVO;

public class TurmaCensoVO extends SuperVO {

	private Integer codigo;
	private String codigoINEP;
	private String codigoTurmaINEP;
	private String nomeTurma;
	private String horarioInicialHoraTurma;
	private String horarioInicialMinutoTurma;
	private String horarioFinalHoraTurma;
	private String horarioFinalMinutoTurma;
	private Integer diaDaSemanaDomingo;
	private Integer diaDaSemanaSeguenda;
	private Integer diaDaSemanaTerca;
	private Integer diaDaSemanaQuarta;
	private Integer diaDaSemanaQuinta;
	private Integer diaDaSemanaSexta;
	private Integer diaDaSemanaSabado;
	private String modalidade;
	private String etapaEnsino;
	private String nivelEducacional;
	private Integer codigoCurso;
	private List<String> listaTurnoHorarioVOs;
	private static final long serialVersionUID = 1L;

	public Integer getCodigo() {
		return codigo;
	}

	public String getCodigoINEP() {
		return codigoINEP;
	}

	public String getCodigoTurmaINEP() {
		return codigoTurmaINEP;
	}

	public String getNomeTurma() {
		return nomeTurma;
	}

	public String getHorarioInicialHoraTurma() {
		return horarioInicialHoraTurma;
	}

	public String getHorarioInicialMinutoTurma() {
		return horarioInicialMinutoTurma;
	}

	public String getHorarioFinalHoraTurma() {
		return horarioFinalHoraTurma;
	}

	public String getHorarioFinalMinutoTurma() {
		return horarioFinalMinutoTurma;
	}

	public Integer getDiaDaSemanaDomingo() {
		return diaDaSemanaDomingo;
	}

	public Integer getDiaDaSemanaSeguenda() {
		return diaDaSemanaSeguenda;
	}

	public Integer getDiaDaSemanaTerca() {
		return diaDaSemanaTerca;
	}

	public Integer getDiaDaSemanaQuarta() {
		return diaDaSemanaQuarta;
	}

	public Integer getDiaDaSemanaQuinta() {
		return diaDaSemanaQuinta;
	}

	public Integer getDiaDaSemanaSexta() {
		return diaDaSemanaSexta;
	}

	public Integer getDiaDaSemanaSabado() {
		return diaDaSemanaSabado;
	}

	public String getModalidade() {
		return modalidade;
	}

	public String getEtapaEnsino() {
		return etapaEnsino;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public void setCodigoINEP(String codigoINEP) {
		this.codigoINEP = codigoINEP;
	}

	public void setCodigoTurmaINEP(String codigoTurmaINEP) {
		this.codigoTurmaINEP = codigoTurmaINEP;
	}

	public void setNomeTurma(String nomeTurma) {
		this.nomeTurma = nomeTurma;
	}

	public void setHorarioInicialHoraTurma(String horarioInicialHoraTurma) {
		this.horarioInicialHoraTurma = horarioInicialHoraTurma;
	}

	public void setHorarioInicialMinutoTurma(String horarioInicialMinutoTurma) {
		this.horarioInicialMinutoTurma = horarioInicialMinutoTurma;
	}

	public void setHorarioFinalHoraTurma(String horarioFinalHoraTurma) {
		this.horarioFinalHoraTurma = horarioFinalHoraTurma;
	}

	public void setHorarioFinalMinutoTurma(String horarioFinalMinutoTurma) {
		this.horarioFinalMinutoTurma = horarioFinalMinutoTurma;
	}

	public void setDiaDaSemanaDomingo(Integer diaDaSemanaDomingo) {
		this.diaDaSemanaDomingo = diaDaSemanaDomingo;
	}

	public void setDiaDaSemanaSeguenda(Integer diaDaSemanaSeguenda) {
		this.diaDaSemanaSeguenda = diaDaSemanaSeguenda;
	}

	public void setDiaDaSemanaTerca(Integer diaDaSemanaTerca) {
		this.diaDaSemanaTerca = diaDaSemanaTerca;
	}

	public void setDiaDaSemanaQuarta(Integer diaDaSemanaQuarta) {
		this.diaDaSemanaQuarta = diaDaSemanaQuarta;
	}

	public void setDiaDaSemanaQuinta(Integer diaDaSemanaQuinta) {
		this.diaDaSemanaQuinta = diaDaSemanaQuinta;
	}

	public void setDiaDaSemanaSexta(Integer diaDaSemanaSexta) {
		this.diaDaSemanaSexta = diaDaSemanaSexta;
	}

	public void setDiaDaSemanaSabado(Integer diaDaSemanaSabado) {
		this.diaDaSemanaSabado = diaDaSemanaSabado;
	}

	public void setModalidade(String modalidade) {
		this.modalidade = modalidade;
	}

	public void setEtapaEnsino(String etapaEnsino) {
		this.etapaEnsino = etapaEnsino;
	}

	@Override
	public int hashCode() {
		return Objects.hash(codigo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TurmaCensoVO other = (TurmaCensoVO) obj;
		return Objects.equals(codigo, other.codigo);
	}
	
	public String getNivelEducacional() {
		return nivelEducacional;
	}

	public void setNivelEducacional(String nivelEducacional) {
		this.nivelEducacional = nivelEducacional;
	}

	public Integer getCodigoCurso() {
		return codigoCurso;
	}

	public void setCodigoCurso(Integer codigoCurso) {
		this.codigoCurso = codigoCurso;
	}

	public List<String> getListaTurnoHorarioVOs() {
		if(listaTurnoHorarioVOs == null) {
			listaTurnoHorarioVOs = new ArrayList<String>(0);
		}
		return listaTurnoHorarioVOs;
	}

	public void setListaTurnoHorarioVOs(List<String> listaTurnoHorarioVOs) {
		this.listaTurnoHorarioVOs = listaTurnoHorarioVOs;
	}	
			
}
