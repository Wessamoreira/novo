package relatorio.negocio.comuns.avaliacaoInst;

import negocio.comuns.utilitarias.Uteis;

public class AvaliacaoInstitucionalPorTurmaSinteticoVO {

	private String turma;
	private String curso;
	private Integer qtdeRespondeu;
	private Integer qtdeNaoRespondeu;
	
	public Integer getQtdeTotal(){
		return getQtdeNaoRespondeu()+getQtdeRespondeu();
	}
	
	public Double getPorcentagemRespondeu(){
		return Uteis.arrendondarForcando2CadasDecimais((getQtdeRespondeu()*100)/getQtdeTotal());
	}
	public Double getPorcentagemNaoRespondeu(){
		return Uteis.arrendondarForcando2CadasDecimais((getQtdeNaoRespondeu()*100)/getQtdeTotal());
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

	public Integer getQtdeRespondeu() {
		if (qtdeRespondeu == null) {
			qtdeRespondeu = 0;
		}
		return qtdeRespondeu;
	}

	public void setQtdeRespondeu(Integer qtdeRespondeu) {
		this.qtdeRespondeu = qtdeRespondeu;
	}

	public Integer getQtdeNaoRespondeu() {
		if (qtdeNaoRespondeu == null) {
			qtdeNaoRespondeu = 0;
		}
		return qtdeNaoRespondeu;
	}

	public void setQtdeNaoRespondeu(Integer qtdeNaoRespondeu) {
		this.qtdeNaoRespondeu = qtdeNaoRespondeu;
	}

	public String getCurso() {
		if(curso == null){
			curso = "";
		}
		return curso;
	}

	public void setCurso(String curso) {
		this.curso = curso;
	}
	
	
}
