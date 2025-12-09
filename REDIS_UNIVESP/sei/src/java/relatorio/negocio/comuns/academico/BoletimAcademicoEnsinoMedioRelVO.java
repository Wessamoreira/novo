package relatorio.negocio.comuns.academico;

import negocio.comuns.utilitarias.dominios.SituacaoHistorico;

public class BoletimAcademicoEnsinoMedioRelVO {

	private String matricula;
	private String disciplina;
	private Integer ordem;
	private Integer codigoDisciplina;
	private String nomeBimestre;
	private String nomeBimestreApresentar;
	private Integer ordemBimestre;
	private String nota;
	private Double notaCalculada;
	private String tituloNota;
	private String faltas;
	private Integer ordemNota;
	private String situacaoMatriculaPeriodo;
	private Boolean parteDiversificada;
	private SituacaoHistorico situacaoHistorico;

	public String getMatricula() {
		if (matricula == null) {
			matricula = "";
		}
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
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

	public String getNomeBimestre() {
		if (nomeBimestre == null) {
			nomeBimestre = "";
		}
		return nomeBimestre;
	}

	public void setNomeBimestre(String nomeBimestre) {
		this.nomeBimestre = nomeBimestre;
	}

	public String getNomeBimestreApresentar() {
		if (nomeBimestreApresentar == null) {
			nomeBimestreApresentar = "";
		}
		return nomeBimestreApresentar;
	}

	public void setNomeBimestreApresentar(String nomeBimestreApresentar) {
		this.nomeBimestreApresentar = nomeBimestreApresentar;
	}

	public String getNota() {
		if (nota == null) {
			nota = "";
		}
		return nota;
	}

	public void setNota(String nota) {
		this.nota = nota;
	}

	public String getTituloNota() {
		if (tituloNota == null) {
			tituloNota = "";
		}
		return tituloNota;
	}

	public void setTituloNota(String tituloNota) {
		this.tituloNota = tituloNota;
	}

	public String getFaltas() {
		if (faltas == null) {
			faltas = "";
		}
		return faltas;
	}

	public void setFaltas(String faltas) {
		this.faltas = faltas;
	}

	public Integer getOrdemNota() {
		return ordemNota;
	}

	public void setOrdemNota(Integer ordemNota) {
		this.ordemNota = ordemNota;
	}

	public String getOrdenacao() {
		return getNomeBimestre() + "/" + getOrdemNota() + "/" + (getParteDiversificada() ? "1" : "0") + "/" + getOrdem()
				+ "/" + getDisciplina();
	}

	public Double getNotaCalculada() {
		if (notaCalculada == null) {
			notaCalculada = 0.0;
		}
		return notaCalculada;
	}

	public void setNotaCalculada(Double notaCalculada) {
		this.notaCalculada = notaCalculada;
	}

	public String getSituacao() {
		return getSituacaoHistorico().getDescricao();
	}

	public String getSituacaoSigla() {
		return getSituacaoHistorico().getValor();
	}

	public Integer getCodigoDisciplina() {
		if (codigoDisciplina == null) {
			codigoDisciplina = 0;
		}
		return codigoDisciplina;
	}

	public void setCodigoDisciplina(Integer codigoDisciplina) {
		this.codigoDisciplina = codigoDisciplina;
	}

	public String getSituacaoMatriculaPeriodo() {
		if (situacaoMatriculaPeriodo == null) {
			situacaoMatriculaPeriodo = "";
		}
		return situacaoMatriculaPeriodo;
	}

	public void setSituacaoMatriculaPeriodo(String situacaoMatriculaPeriodo) {
		this.situacaoMatriculaPeriodo = situacaoMatriculaPeriodo;
	}

	public Integer getOrdemBimestre() {
		if (ordemBimestre == null) {
			ordemBimestre = 0;
		}
		return ordemBimestre;
	}

	public void setOrdemBimestre(Integer ordemBimestre) {
		this.ordemBimestre = ordemBimestre;
	}

	public Boolean getParteDiversificada() {
		if (parteDiversificada == null) {
			parteDiversificada = false;
		}
		return parteDiversificada;
	}

	public void setParteDiversificada(Boolean parteDiversificada) {
		this.parteDiversificada = parteDiversificada;
	}

	public SituacaoHistorico getSituacaoHistorico() {
		if (situacaoHistorico == null) {
			situacaoHistorico = SituacaoHistorico.CURSANDO;
		}
		return situacaoHistorico;
	}

	public void setSituacaoHistorico(SituacaoHistorico situacaoHistorico) {
		this.situacaoHistorico = situacaoHistorico;
	}

	@Override
	public String toString() {
		return "BoletimAcademicoEnsinoMedioRelVO [disciplina=" + disciplina + ", codigoDisciplina=" + codigoDisciplina
				+ ", situacaoHistorico=" + situacaoHistorico + "]";
	}

	public Integer getOrdem() {
		if (ordem == null) {
			ordem = 0;
		}
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

}
