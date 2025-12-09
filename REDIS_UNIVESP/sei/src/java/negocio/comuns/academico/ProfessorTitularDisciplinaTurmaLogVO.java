package negocio.comuns.academico;

import negocio.comuns.arquitetura.SuperVO;

/**
 * Reponsável por manter os dados da entidade ContaReceber. Classe do tipo VO - Value Object composta pelos atributos da
 * entidade com visibilidade protegida e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 *
 * @see SuperVO
 */
public class ProfessorTitularDisciplinaTurmaLogVO extends SuperVO {

    private Integer codigo;

    private String semestre;
    private String ano;
    private Integer turma;
    private Integer disciplina;
    private Integer professor;
    private Boolean titular;
    //Código do usuário
    private Integer responsavel;
    private Integer curso;
    private String operacao;
    public static final long serialVersionUID = 1L;

    public ProfessorTitularDisciplinaTurmaLogVO() {
        super();
    }

    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public Integer getResponsavel() {
        if (responsavel == null) {
            responsavel = 0;
        }
        return responsavel;
    }

    public void setResponsavel(Integer responsavel) {
        this.responsavel = responsavel;
    }

    public String getOperacao() {
        if (operacao == null) {
            operacao = "";
        }
        return operacao;
    }

    public void setOperacao(String operacao) {
        this.operacao = operacao;
    }

    /**
     * @return the semestre
     */
    public String getSemestre() {
        if (semestre == null) {
            semestre = "";
        }
        return semestre;
    }

    /**
     * @param semestre the semestre to set
     */
    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    /**
     * @return the ano
     */
    public String getAno() {
        if (ano == null) {
            ano = "";
        }
        return ano;
    }

    /**
     * @param ano the ano to set
     */
    public void setAno(String ano) {
        this.ano = ano;
    }

    /**
     * @return the turma
     */
    public Integer getTurma() {
        if (turma == null) {
            turma = 0;
        }
        return turma;
    }

    /**
     * @param turma the turma to set
     */
    public void setTurma(Integer turma) {
        this.turma = turma;
    }

    /**
     * @return the disciplina
     */
    public Integer getDisciplina() {
        if (disciplina == null) {
            disciplina = 0;
        }
        return disciplina;
    }

    /**
     * @param disciplina the disciplina to set
     */
    public void setDisciplina(Integer disciplina) {
        this.disciplina = disciplina;
    }

    /**
     * @return the professor
     */
    public Integer getProfessor() {
        if (professor == null) {
            professor = 0;
        }
        return professor;
    }

    /**
     * @param professor the professor to set
     */
    public void setProfessor(Integer professor) {
        this.professor = professor;
    }

    /**
     * @return the titular
     */
    public Boolean getTitular() {
        return titular;
    }

    /**
     * @param titular the titular to set
     */
    public void setTitular(Boolean titular) {
        this.titular = titular;
    }
    
    public Integer getCurso() {
		if (curso == null) {
			curso = 0;
		}
		return curso;
	}

	public void setCurso(Integer curso) {
		this.curso = curso;
	}
}
