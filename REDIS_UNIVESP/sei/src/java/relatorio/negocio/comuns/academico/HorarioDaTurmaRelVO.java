package relatorio.negocio.comuns.academico;

import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.Uteis;

public class HorarioDaTurmaRelVO {

    private String turma;
    private String disciplina;
    private PessoaVO professor;
    private String sala;
    private String titulacaoProfessor;
    private String dataInicio;
    private String dataFim;
    private String observacao;
    private String horario;
    private String diaSemana;
    private Integer ordemApresentacaoSemanal;

    public String getTurma() {
        if (turma == null) {
            turma = "";
        }
        return turma;
    }

    public String getDisciplina() {
        return disciplina;
    }

    public PessoaVO getProfessor() {
        if (professor == null) {
            professor = new PessoaVO();
        }
        return professor;
    }

    public String getTitulacaoProfessor() {
        if (titulacaoProfessor == null) {
            titulacaoProfessor = "";
        }
        return titulacaoProfessor;
    }

    public void setTitulacaoProfessor(String titulacaoProfessor) {
        this.titulacaoProfessor = titulacaoProfessor;
    }

    public String getDataInicio() {
        if (dataInicio == null) {
            dataInicio = "";
        }
        return dataInicio;
    }

    public String getDataFim() {
        if (dataFim == null) {
            dataFim = "";
        }
        return dataFim;
    }

    public void setTurma(String turma) {
        this.turma = turma;
    }

    public void setDisciplina(String disciplina) {
        this.disciplina = disciplina;
    }

    public void setProfessor(PessoaVO professor) {
        this.professor = professor;
    }

    public void setDataInicio(String dataInicio) {
        this.dataInicio = dataInicio;
    }

    public void setDataFim(String dataFim) {
        this.dataFim = dataFim;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getObservacao() {
        if (observacao == null) {
            observacao = "";
        }
        return observacao;
    }

	public String getHorario() {
		if (horario == null) {
			horario = "";
		}
		return horario;
	}

	public void setHorario(String horario) {
		this.horario = horario;
	}

	/**
	 * @return the sala
	 */
	public String getSala() {
		if (sala == null) {
			sala = "";
		}
		return sala;
	}

	/**
	 * @param sala the sala to set
	 */
	public void setSala(String sala) {
		this.sala = sala;
	}

	public String getDiaSemana() {
		if (diaSemana == null) {
			diaSemana = "";
		}
		return diaSemana;
	}

	public void setDiaSemana(String diaSemana) {
		this.diaSemana = diaSemana;
	}

	public Integer getOrdemApresentacaoSemanal() {
		return ordemApresentacaoSemanal;
	}

	public void setOrdemApresentacaoSemanal(Integer ordemApresentacaoSemanal) {
		this.ordemApresentacaoSemanal = ordemApresentacaoSemanal;
	}
	
	
}
