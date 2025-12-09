/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package relatorio.negocio.comuns.academico;

/**
 *
 * @author Carlos
 */
public class AtaProvaRelVO {

    private String pessoaNome;
    private String cursoNome;
    private String matricula;
    private String turmaNome;
    private String disciplinaNome;
    private String professorNome;
    private Integer totalAlunos;
    private String mediaFinal;

	/**
     * @return the pessoaNome
     */
    public String getPessoaNome() {
        if(pessoaNome == null){
            pessoaNome = "";
        }
        return pessoaNome;
    }

    /**
     * @param pessoaNome the pessoaNome to set
     */
    public void setPessoaNome(String pessoaNome) {
        this.pessoaNome = pessoaNome;
    }

    /**
     * @return the cursoNome
     */
    public String getCursoNome() {
        if(cursoNome == null){
            cursoNome = "";
        }
        return cursoNome;
    }

    /**
     * @param cursoNome the cursoNome to set
     */
    public void setCursoNome(String cursoNome) {
        this.cursoNome = cursoNome;
    }

    /**
     * @return the matricula
     */
    public String getMatricula() {
        if(matricula == null){
            matricula = "";
        }
        return matricula;
    }

    /**
     * @param matricula the matricula to set
     */
    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    /**
     * @return the turmaNome
     */
    public String getTurmaNome() {
        if(turmaNome == null){
            turmaNome = "";
        }
        return turmaNome;
    }

    /**
     * @param turmaNome the turmaNome to set
     */
    public void setTurmaNome(String turmaNome) {
        this.turmaNome = turmaNome;
    }

    /**
     * @return the disciplinaNome
     */
    public String getDisciplinaNome() {
        if(disciplinaNome == null){
            disciplinaNome = "";
        }
        return disciplinaNome;
    }

    /**
     * @param disciplinaNome the disciplinaNome to set
     */
    public void setDisciplinaNome(String disciplinaNome) {
        this.disciplinaNome = disciplinaNome;
    }

    /**
     * @return the professorNome
     */
    public String getProfessorNome() {
        if(professorNome == null){
            professorNome = "";
        }
        return professorNome;
    }

    /**
     * @param professorNome the professorNome to set
     */
    public void setProfessorNome(String professorNome) {
        this.professorNome = professorNome;
    }

    /**
     * @return the totalAlunos
     */
    public Integer getTotalAlunos() {
        if(totalAlunos == null){
            totalAlunos = 0;
        }
        return totalAlunos;
    }

    /**
     * @param totalAlunos the totalAlunos to set
     */
    public void setTotalAlunos(Integer totalAlunos) {
        this.totalAlunos = totalAlunos;
    }
    
    public String getMediaFinal() {
		if (mediaFinal == null) {
			mediaFinal = "";
		}
		return mediaFinal;
	}

	public void setMediaFinal(String mediaFinal) {
		this.mediaFinal = mediaFinal;
	}

}
