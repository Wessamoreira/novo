package relatorio.negocio.comuns.academico;

import java.math.BigDecimal;

import negocio.comuns.arquitetura.SuperVO;

public class PlanoDisciplinaConteudoPlanejamentoRelVO extends SuperVO {

	private static final long serialVersionUID = 1L;
	private String habilidade;
	private String conteudo;
	private String atitude;
	private String metodologia;
	private BigDecimal cargaHoraria;
	private String classificacao;
	private String praticaSupervisionada;

	public String getHabilidade() {
		if (habilidade == null) {
			habilidade = "";
		}
		return habilidade;
	}

	public void setHabilidade(String habilidade) {
		this.habilidade = habilidade;
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

	public String getAtitude() {
		if (atitude == null) {
			atitude = "";
		}
		return atitude;
	}

	public void setAtitude(String atitude) {
		this.atitude = atitude;
	}

	public String getMetodologia() {
		if (metodologia == null) {
			metodologia = "";
		}
		return metodologia;
	}

	public void setMetodologia(String metodologia) {
		this.metodologia = metodologia;
	}

	public BigDecimal getCargaHoraria() {
		if (cargaHoraria == null) {
			cargaHoraria = BigDecimal.ZERO;
		}
		return cargaHoraria;
	}

	public void setCargaHoraria(BigDecimal cargaHoraria) {
		this.cargaHoraria = cargaHoraria;
	}

	public String getClassificacao() {
		if (classificacao == null) {
			classificacao = "";
		}
		return classificacao;
	}

	public void setClassificacao(String classificacao) {
		this.classificacao = classificacao;
	}

	public String getPraticaSupervisionada() {
		if (praticaSupervisionada == null) {
			praticaSupervisionada = "";
		}
		return praticaSupervisionada;
	}

	public void setPraticaSupervisionada(String praticaSupervisionada) {
		this.praticaSupervisionada = praticaSupervisionada;
	}
}
