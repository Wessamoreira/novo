package relatorio.negocio.comuns.academico;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

public class ProfessoresPorUnidadeCursoTurmaRelVO {

    private List<ProfessoresPorUnidadeCursoTurmaRelVOs> professoresVOs;
    private String unidadeEnsino;
    private String curso;
    private String turno;
    private String turma;
    private String disciplina;
    private String semestre;
    private String ano;
    private String filtroSituacao;
    private Integer qtdeProfessores;

    public ProfessoresPorUnidadeCursoTurmaRelVO() {
    }

    public JRDataSource getListaProfessorVOs() {
        JRDataSource jr = new JRBeanArrayDataSource(getProfessoresVOs().toArray());
        return jr;
    }

    public List<ProfessoresPorUnidadeCursoTurmaRelVOs> getProfessoresVOs() {
        if (professoresVOs == null) {
            professoresVOs = new ArrayList<ProfessoresPorUnidadeCursoTurmaRelVOs>(0);
        }
        return professoresVOs;
    }

    public void setProfessoresVOs(List<ProfessoresPorUnidadeCursoTurmaRelVOs> professoresVOs) {
        this.professoresVOs = professoresVOs;
    }

    public String getUnidadeEnsino() {
        if (unidadeEnsino == null) {
            unidadeEnsino = "";
        }
        return unidadeEnsino;
    }

    public void setUnidadeEnsino(String unidadeEnsino) {
        this.unidadeEnsino = unidadeEnsino;
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

    public String getTurno() {
        if (turno == null) {
            turno = "";
        }
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
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

    public String getSemestre() {
        if (semestre == null) {
            semestre = "";
        }
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    public String getAno() {
        if (ano == null) {
            ano = "";
        }
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public Integer getQtdeProfessores() {
        if (qtdeProfessores == null) {
            qtdeProfessores = 0;
        }
        return qtdeProfessores;
    }

    public void setQtdeProfessores(Integer qtdeProfessores) {
        this.qtdeProfessores = qtdeProfessores;
    }

    public String getDisciplina() {
        if(disciplina == null){
            disciplina = "";
        }
        return disciplina;
    }

    public void setDisciplina(String disciplina) {
        this.disciplina = disciplina;
    }

    public String getFiltroSituacao() {
        if(filtroSituacao == null){
            filtroSituacao = "";
        }
        return filtroSituacao;
    }

    public void setFiltroSituacao(String filtroSituacao) {
        this.filtroSituacao = filtroSituacao;
    }
}
