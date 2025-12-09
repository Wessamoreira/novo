package relatorio.negocio.comuns.academico;

public class AlunosMatriculadosGeralRelVO {

	private Integer unidadeEnsinoId;
	private String unidadeEnsino;
	private String matriculaAluno;
	private String nomeAluno;
	private String curso;
	private String turma;
	private String data;
	private String formaIngresso;
	private String email;
	private String celular;
	private String telefoneRes;
	private String qtdTurma;
	private String qtdPeriodoLetivo;
	private Double valorMatricula;
	private String turno;
	private Boolean calouro;
	private Boolean veterano;
	private Integer qtdeCalouro;
	private Integer qtdeVeterano;
	private String periodoLetivo;
	private Boolean transfInterna;
	private Boolean transfExterna;
	private Integer qtdeTransExterna;
	private Integer qtdeTransInterna;
	private String situacaoMatriculaPeriodo;
	
	public AlunosMatriculadosGeralRelVO() {
	}

	public String getCurso() {
		return curso;
	}

	public void setCurso(String curso) {
		this.curso = curso;
	}

	public String getUnidadeEnsino() {
		return unidadeEnsino;
	}

	public void setUnidadeEnsino(String unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}

	public Integer getUnidadeEnsinoId() {
		return unidadeEnsinoId;
	}

	public void setUnidadeEnsinoId(Integer unidadeEnsinoId) {
		this.unidadeEnsinoId = unidadeEnsinoId;
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

	public String getMatriculaAluno() {
		if (matriculaAluno == null) {
			matriculaAluno = "";
		}
		return matriculaAluno;
	}

	public void setMatriculaAluno(String matriculaAluno) {
		this.matriculaAluno = matriculaAluno;
	}

	public String getNomeAluno() {
		if (nomeAluno == null) {
			nomeAluno = "";
		}
		return nomeAluno;
	}

	public void setNomeAluno(String nomeAluno) {
		this.nomeAluno = nomeAluno;
	}

	public String getData() {
		if (data == null) {
			data = "";
		}
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getFormaIngresso() {
		if (formaIngresso == null) {
			formaIngresso = "";
		}
		return formaIngresso;
	}

	public void setFormaIngresso(String formaIngresso) {
		this.formaIngresso = formaIngresso;
	}

	public String getEmail() {
		if (email == null) {
			email = "";
		}
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCelular() {
		if (celular == null) {
			celular = "";
		}
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public String getTelefoneRes() {
		if (telefoneRes == null) {
			telefoneRes = "";
		}
		return telefoneRes;
	}

	public void setTelefoneRes(String telefoneRes) {
		this.telefoneRes = telefoneRes;
	}

	/**
	 * @return the qtdTurma
	 */
	public String getQtdTurma() {
		return qtdTurma;
	}

	/**
	 * @param qtdTurma
	 *            the qtdTurma to set
	 */
	public void setQtdTurma(String qtdTurma) {
		this.qtdTurma = qtdTurma;
	}

	public Double getValorMatricula() {
		if (valorMatricula == null) {
			valorMatricula = 0.0;
		}
		return valorMatricula;
	}

	public void setValorMatricula(Double valorMatricula) {
		this.valorMatricula = valorMatricula;
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

	public Boolean getCalouro() {
		if (calouro == null) {
			calouro = false;
		}
		return calouro;
	}

	public void setCalouro(Boolean calouro) {
		this.calouro = calouro;
	}

	public Boolean getVeterano() {
		if (veterano == null) {
			veterano = false;
		}
		return veterano;
	}

	public void setVeterano(Boolean veterano) {
		this.veterano = veterano;
	}

	public Integer getQtdeCalouro() {
		if (qtdeCalouro == null) {
			qtdeCalouro = 0;
		}
		return qtdeCalouro;
	}

	public void setQtdeCalouro(Integer qtdeCalouro) {
		this.qtdeCalouro = qtdeCalouro;
	}

	public Integer getQtdeVeterano() {
		if (qtdeVeterano == null) {
			qtdeVeterano = 0;
		}
		return qtdeVeterano;
	}

	public void setQtdeVeterano(Integer qtdeVeterano) {
		this.qtdeVeterano = qtdeVeterano;
	}
	
	public String getPeriodoLetivo() {
		if (periodoLetivo == null) {
			periodoLetivo = "";
		}
		return periodoLetivo;
	}

	public void setPeriodoLetivo(String periodoLetivo) {
		this.periodoLetivo = periodoLetivo;
	}

	public String getQtdPeriodoLetivo() {
		if (qtdPeriodoLetivo == null) {
			qtdPeriodoLetivo = "";
		}
		return qtdPeriodoLetivo;
	}

	public void setQtdPeriodoLetivo(String qtdPeriodoLetivo) {
		this.qtdPeriodoLetivo = qtdPeriodoLetivo;
	}
	

	public Integer getQtdeTransExterna() {
		if (qtdeTransExterna == null) {
			qtdeTransExterna = 0;
		}
		return qtdeTransExterna;
	}

	public void setQtdeTransExterna(Integer qtdeTransExterna) {
		this.qtdeTransExterna = qtdeTransExterna;
	}

	public Integer getQtdeTransInterna() {
		if (qtdeTransInterna == null) {
			qtdeTransInterna = 0;
		}
		return qtdeTransInterna;
	}

	public void setQtdeTransInterna(Integer qtdeTransInterna) {
		this.qtdeTransInterna = qtdeTransInterna;
	}

	public Boolean getTransfInterna() {
		if (transfInterna == null) {
			transfInterna = Boolean.FALSE;
		}
		return transfInterna;
	}

	public void setTransfInterna(Boolean transfInterna) {
		this.transfInterna = transfInterna;
	}

	public Boolean getTransfExterna() {
		if (transfExterna == null) {
			transfExterna = Boolean.FALSE;
		}
		return transfExterna;
	}

	public void setTransfExterna(Boolean transfExterna) {
		this.transfExterna = transfExterna;
	}

	public String getSituacaoMatriculaPeriodo() {
		return situacaoMatriculaPeriodo;
	}

	public void setSituacaoMatriculaPeriodo(String situacaoMatriculaPeriodo) {
		this.situacaoMatriculaPeriodo = situacaoMatriculaPeriodo;
	}
	
}
