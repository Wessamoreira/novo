package relatorio.negocio.comuns.academico;

import java.util.Date;

public class QuadroMatriculaRelVO implements Cloneable {

    private String nomeUnidadeEnsino;
    private String nomeCurso;
    private String nomeTurma;
    private Date data;
    private Integer qtdeMatriculas;
    private String ano;
    private String semestre;
   
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
     * @return the nomeUnidadeEnsino
     */
    public String getNomeUnidadeEnsino() {
        if(nomeUnidadeEnsino == null){
            nomeUnidadeEnsino = "";
        }
        return nomeUnidadeEnsino;
    }

    /**
     * @param nomeUnidadeEnsino the nomeUnidadeEnsino to set
     */
    public void setNomeUnidadeEnsino(String nomeUnidadeEnsino) {
        this.nomeUnidadeEnsino = nomeUnidadeEnsino;
    }

    /**
     * @return the nomeCurso
     */
    public String getNomeCurso() {
        if(nomeCurso == null){
            nomeCurso = "";
        }
        return nomeCurso;
    }

    /**
     * @param nomeCurso the nomeCurso to set
     */
    public void setNomeCurso(String nomeCurso) {
        this.nomeCurso = nomeCurso;
    }

    /**
     * @return the nomeTurma
     */
    public String getNomeTurma() {
        if(nomeTurma==null){
            nomeTurma = "";
        }
        return nomeTurma;
    }

    /**
     * @param nomeTurma the nomeTurma to set
     */
    public void setNomeTurma(String nomeTurma) {
        this.nomeTurma = nomeTurma;
    }

    /**
     * @return the data
     */
    public Date getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(Date data) {
        this.data = data;
    }

    /**
     * @return the qtdeMatriculas
     */
    public Integer getQtdeMatriculas() {
        return qtdeMatriculas;
    }

    /**
     * @param qtdeMatriculas the qtdeMatriculas to set
     */
    public void setQtdeMatriculas(Integer qtdeMatriculas) {
        this.qtdeMatriculas = qtdeMatriculas;
    }

}
