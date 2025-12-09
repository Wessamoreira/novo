package relatorio.negocio.comuns.academico;


public class NotaNaoLancadaProfessorRelVO implements Cloneable {

    private String nomeUnidadeEnsino;
    private String nomeCurso;
    private String nomeProfessor;
    private String identificadorTurma;
    private String abreviaturaDisciplina;
    private Integer qtdeAlunosTurmaDisciplina;
    private Integer qtdeNotasNaoLancadas;

    public NotaNaoLancadaProfessorRelVO() {
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

	public String getAbreviaturaDisciplina() {
        if (abreviaturaDisciplina == null) {
        	abreviaturaDisciplina = "";
        }
		return abreviaturaDisciplina;
	}

	public void setAbreviaturaDisciplina(String abreviaturaDisciplina) {
		this.abreviaturaDisciplina = abreviaturaDisciplina;
	}

	public String getNomeProfessor() {
		if (nomeProfessor == null) {
			nomeProfessor = "";
		}
		return nomeProfessor;
	}

	public void setNomeProfessor(String nomeProfessor) {
		this.nomeProfessor = nomeProfessor;
	}

	public Integer getQtdeAlunosTurmaDisciplina() {
		if (qtdeAlunosTurmaDisciplina == null) {
			qtdeAlunosTurmaDisciplina = 0;
		}
		return qtdeAlunosTurmaDisciplina;
	}

	public void setQtdeAlunosTurmaDisciplina(Integer qtdeAlunosTurmaDisciplina) {
		this.qtdeAlunosTurmaDisciplina = qtdeAlunosTurmaDisciplina;
	}

	public Integer getQtdeNotasNaoLancadas() {
		if (qtdeNotasNaoLancadas == null) {
			qtdeNotasNaoLancadas = 0;
		}
		return qtdeNotasNaoLancadas;
	}

	public void setQtdeNotasNaoLancadas(Integer qtdeNotasNaoLancadas) {
		this.qtdeNotasNaoLancadas = qtdeNotasNaoLancadas;
	}

}
