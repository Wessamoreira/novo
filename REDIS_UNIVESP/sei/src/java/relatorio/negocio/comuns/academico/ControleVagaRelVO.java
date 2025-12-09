package relatorio.negocio.comuns.academico;

import negocio.comuns.utilitarias.Constantes;

public class ControleVagaRelVO implements Cloneable {

    private String nomeUnidadeEnsino;
    private String identificadorTurma;
    private String nomeCurso;
    private String nomeGradeCurricular;
    private String descricaoPeriodoLetivo;
    private String abreviaturaDisciplina;
    private String matricula;
    private String nomeAluno;
    private String situacao;
    private Integer veterano;
    private Integer nrmaximomatricula;
    private Integer qtdeMatricula;
    private Integer qtdeMatriculaAtiva;
    private Integer nrMaximoVagasReposicao;
    private String tipohistorico;
    private Integer qtdeMatriculaAtivaReposicao;
    private Integer qtdPreMatriculas;
    private Integer qtdPreMatriculasReposicao;
    private Integer nrVagas;

    public ControleVagaRelVO() {
    }

    public String getIdentificadorTurma() {
        if (identificadorTurma == null) {
            identificadorTurma = "";
        }
        return identificadorTurma;
    }

    public void setIdentificadorTurma(String identificadorTurma) {
        this.identificadorTurma = identificadorTurma;
    }
    
	public String getNomeUnidadeEnsino() {
        if (nomeUnidadeEnsino == null) {
        	nomeUnidadeEnsino = "";
        }
		return nomeUnidadeEnsino;
	}

	public void setNomeUnidadeEnsino(String nomeUnidadeEnsino) {
		this.nomeUnidadeEnsino = nomeUnidadeEnsino;
	}

	public String getNomeCurso() {
        if (nomeCurso == null) {
        	nomeCurso = "";
        }
		return nomeCurso;
	}

	public void setNomeCurso(String nomeCurso) {
		this.nomeCurso = nomeCurso;
	}

	public String getNomeGradeCurricular() {
        if (nomeGradeCurricular == null) {
        	nomeGradeCurricular = "";
        }
		return nomeGradeCurricular;
	}

	public void setNomeGradeCurricular(String nomeGradeCurricular) {
		this.nomeGradeCurricular = nomeGradeCurricular;
	}

	public String getDescricaoPeriodoLetivo() {
        if (descricaoPeriodoLetivo == null) {
        	descricaoPeriodoLetivo = "";
        }
		return descricaoPeriodoLetivo;
	}

	public void setDescricaoPeriodoLetivo(String descricaoPeriodoLetivo) {
		this.descricaoPeriodoLetivo = descricaoPeriodoLetivo;
	}

	public String getAbreviaturaDisciplina() {
        if (abreviaturaDisciplina == null) {
        	abreviaturaDisciplina = "";
        }
		return abreviaturaDisciplina;
	}

	public void setAbreviaturaDisciplina(String abreviaturaDisciplina) {
		this.abreviaturaDisciplina = abreviaturaDisciplina;
	}

	public String getMatricula() {
        if (matricula == null) {
        	matricula = "";
        }
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
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

	public String getSituacao() {
        if (situacao == null) {
        	situacao = "";
        }
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public Integer getVeterano() {
        if (veterano == null) {
        	veterano = 0;
        }
		return veterano;
	}

	public void setVeterano(Integer veterano) {
		this.veterano = veterano;
	}

	public Integer getNrmaximomatricula() {
        if (nrmaximomatricula == null) {
        	nrmaximomatricula = 0;
        }
		return nrmaximomatricula;
	}

	public void setNrmaximomatricula(Integer nrmaximomatricula) {
		this.nrmaximomatricula = nrmaximomatricula;
	}

	public Integer getQtdeMatricula() {
        if (qtdeMatricula == null) {
        	qtdeMatricula = 0;
        }
		return qtdeMatricula;
	}

	public void setQtdeMatricula(Integer qtdeMatricula) {
		this.qtdeMatricula = qtdeMatricula;
	}

	public Integer getQtdeMatriculaAtiva() {
        if (qtdeMatriculaAtiva == null) {
        	qtdeMatriculaAtiva = 0;
        }
		return qtdeMatriculaAtiva;
	}

	public void setQtdeMatriculaAtiva(Integer qtdeMatriculaAtiva) {
		this.qtdeMatriculaAtiva = qtdeMatriculaAtiva;
	}
	
	public Integer getNrMaximoVagasReposicao() {
		if (nrMaximoVagasReposicao == null) {
			nrMaximoVagasReposicao = 0;
		}
		return nrMaximoVagasReposicao;
	}
	
	public void setNrMaximoVagasReposicao(Integer nrMaximoVagasReposicao) {
		this.nrMaximoVagasReposicao = nrMaximoVagasReposicao;
	}
	
	public String getTipohistorico() {
		if (tipohistorico == null) {
			tipohistorico = "";
		}
		return tipohistorico;
	}
	
	public void setTipohistorico(String tipohistorico) {
		this.tipohistorico = tipohistorico;
	}
	
	public Integer getQtdeMatriculaAtivaReposicao() {
		if (qtdeMatriculaAtivaReposicao == null) {
			qtdeMatriculaAtivaReposicao = 0;
		}
		return qtdeMatriculaAtivaReposicao;
	}
	
	public void setQtdeMatriculaAtivaReposicao(Integer qtdeMatriculaAtivaReposicao) {
		this.qtdeMatriculaAtivaReposicao = qtdeMatriculaAtivaReposicao;
	}
	
	public Integer getQtdPreMatriculas() {
		if (qtdPreMatriculas == null) {
			qtdPreMatriculas = 0;
		}
		return qtdPreMatriculas;
	}
	
	public void setQtdPreMatriculas(Integer qtdPreMatriculas) {
		this.qtdPreMatriculas = qtdPreMatriculas;
	}
	
	public Integer getQtdPreMatriculasReposicao() {
		if (qtdPreMatriculasReposicao == null) {
			qtdPreMatriculasReposicao = 0;
		}
		return qtdPreMatriculasReposicao;
	}
	
	public void setQtdPreMatriculasReposicao(Integer qtdPreMatriculasReposicao) {
		this.qtdPreMatriculasReposicao = qtdPreMatriculasReposicao;
	}
	
	public Integer getNrVagas() {
		if (nrVagas == null) {
			nrVagas = 0;
		}
		return nrVagas;
	}
	
	public void setNrVagas(Integer nrVagas) {
		this.nrVagas = nrVagas;
	}
}
