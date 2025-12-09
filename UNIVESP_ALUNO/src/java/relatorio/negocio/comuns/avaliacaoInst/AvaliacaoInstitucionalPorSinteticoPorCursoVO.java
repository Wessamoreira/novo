package relatorio.negocio.comuns.avaliacaoInst;

import negocio.comuns.utilitarias.Uteis;

public class AvaliacaoInstitucionalPorSinteticoPorCursoVO {

	private String curso;
	private Integer codCurso;
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
		if (curso == null) {
			curso = "";
		}
		return curso;
	}

	public void setCurso(String curso) {
		this.curso = curso;
	}

	public Integer getCodCurso() {
		if (codCurso == null) {
			codCurso = 0;
		}
		return codCurso;
	}

	public void setCodCurso(Integer codCurso) {
		this.codCurso = codCurso;
	}

}
