package negocio.comuns.blackboard;

public class SalaAulaBlackboardRelatorioExcelVO {

    private String id;
    private String idSalaAulaBlackboard;
    private String nome;
    private String disciplina;
    private Double notaAnterior;
    private Double mediaFinal;
    private String aluno;
    private String professor;
    private String facilitador;
    private String supervisor;

    public String getId() {
        if (id == null) {
            id = "";
        }
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdSalaAulaBlackboard() {
        if (idSalaAulaBlackboard == null) {
            idSalaAulaBlackboard = "";
        }
        return idSalaAulaBlackboard;
    }

    public void setIdSalaAulaBlackboard(String idSalaAulaBlackboard) {
        this.idSalaAulaBlackboard = idSalaAulaBlackboard;
    }

    public String getNome() {
        if (nome == null) {
            nome = "";
        }
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    public Double getNotaAnterior() {
        if (notaAnterior == null) {
            notaAnterior = 0.0;
        }
        return notaAnterior;
    }

    public void setNotaAnterior(Double notaAnterior) {
        this.notaAnterior = notaAnterior;
    }

    public Double getMediaFinal() {
        if (mediaFinal == null) {
            mediaFinal = 0.0;
        }
        return mediaFinal;
    }

    public void setMediaFinal(Double mediaFinal) {
        this.mediaFinal = mediaFinal;
    }

    public String getAluno() {
        if (aluno == null) {
            aluno = "";
        }
        return aluno;
    }

    public void setAluno(String aluno) {
        this.aluno = aluno;
    }

    public String getProfessor() {
        if (professor == null) {
            professor = "";
        }
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public String getFacilitador() {
        if (facilitador == null) {
            facilitador = "";
        }
        return facilitador;
    }

    public void setFacilitador(String facilitador) {
        this.facilitador = facilitador;
    }

    public String getSupervisor() {
        if (supervisor == null) {
            supervisor = "";
        }
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }
}
