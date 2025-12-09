package relatorio.negocio.comuns.academico;

public class AlunosNaoRenovaramRelVO {

    protected Integer unidadeEnsinoId;
    protected String unidadeEnsino;
    protected String matriculaAluno;
    protected String nomeAluno;
    protected String curso;
    protected String turma;
    protected String turno;
    protected String email;
    protected String celular;
    protected String telefoneRes;
    protected Boolean trazerAlunosUltimoSemestre;
    protected String ultimoAnoSemestre;
    protected String qtdTurma;
    private Boolean possivelFormando;

    public AlunosNaoRenovaramRelVO() {
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

	public String getTurno() {
		if(turno == null){
			turno = "";
		}
		return turno;
	}

	public void setTurno(String turno) {
		this.turno = turno;
	}

	public Boolean getTrazerAlunosUltimoSemestre() {
		if(trazerAlunosUltimoSemestre == null){
			trazerAlunosUltimoSemestre = false;
		}
		return trazerAlunosUltimoSemestre;
	}

	public void setTrazerAlunosUltimoSemestre(Boolean trazerAlunosUltimoSemestre) {
		this.trazerAlunosUltimoSemestre = trazerAlunosUltimoSemestre;
	}

	public String getUltimoAnoSemestre() {
		if(ultimoAnoSemestre == null){
			ultimoAnoSemestre = "";
		}
		return ultimoAnoSemestre;
	}

	public void setUltimoAnoSemestre(String ultimoAnoSemestre) {
		this.ultimoAnoSemestre = ultimoAnoSemestre;
	}

	public String getQtdTurma() {
		return qtdTurma;
	}

	public void setQtdTurma(String qtdTurma) {
		this.qtdTurma = qtdTurma;
	}


	public Boolean getPossivelFormando() {
		if(possivelFormando == null){
			possivelFormando = false;
		}
		return possivelFormando;
	}

	public void setPossivelFormando(Boolean possivelFormando) {
		this.possivelFormando = possivelFormando;
	}
    
}
