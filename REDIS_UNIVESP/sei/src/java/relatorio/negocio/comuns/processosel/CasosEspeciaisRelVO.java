package relatorio.negocio.comuns.processosel;

import java.util.Date;


public class CasosEspeciaisRelVO {
	
	private String candidato;
	private String necessidadesEspeciais;
	private String curso;
	private Date dataProva;
	private String sala;
	private Boolean canhoto;
	private Boolean gravida;
	private Boolean necessidadeEspecial;
	
	public String getCandidato() {
		if (candidato == null) {
			candidato = "";
		}
		return candidato;
	}
	public void setCandidato(String candidato) {
		this.candidato = candidato;
	}
	public String getNecessidadesEspeciais() {
		if (necessidadesEspeciais == null) {
			necessidadesEspeciais = "";
		}
		return necessidadesEspeciais;
	}
	public void setNecessidadesEspeciais(String necessidadesEspeciais) {
		this.necessidadesEspeciais = necessidadesEspeciais;
	}
	public String getCurso() {
		if (curso == null) {
			curso = "";
		}
		return curso;
	}
	public void setCurso(String curso) {
		this.curso = curso;
	}
	public Date getDataProva() {
		if (dataProva == null) {
			dataProva = new Date();
		}
		return dataProva;
	}
	public void setDataProva(Date dataProva) {
		this.dataProva = dataProva;
	}
	public String getSala() {
		if (sala == null) {
			sala = "";
		}
		return sala;
	}
	public void setSala(String sala) {
		this.sala = sala;
	}
	public Boolean getCanhoto() {
		if (canhoto == null) {
			canhoto = Boolean.FALSE;
		}
		return canhoto;
	}
	public void setCanhoto(Boolean canhoto) {
		this.canhoto = canhoto;
	}
	public Boolean getGravida() {
		if (gravida == null) {
			gravida = Boolean.FALSE;
		}
		return gravida;
	}
	public void setGravida(Boolean gravida) {
		this.gravida = gravida;
	}
	public Boolean getNecessidadeEspecial() {
		if (necessidadeEspecial == null) {
			necessidadeEspecial = Boolean.FALSE;
		}
		return necessidadeEspecial;
	}
	public void setNecessidadeEspecial(Boolean necessidadeEspecial) {
		this.necessidadeEspecial = necessidadeEspecial;
	}

}
