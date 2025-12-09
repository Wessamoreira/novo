package relatorio.negocio.comuns.academico;

import negocio.comuns.academico.MatriculaVO;

/**
 * @author Otimize-TI
 */
public class PossiveisFormandosRelVO implements Cloneable {

    private MatriculaVO matriculaVO;
    private String identificadorTurma;
    private String semestre;
    private String ano;
    private Double mediaFinalCurso;
    private String telefones;
    private String email;

    public PossiveisFormandosRelVO() {
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

    public String getIdentificadorTurma() {
        if (identificadorTurma == null) {
            identificadorTurma = "";
        }
        return identificadorTurma;
    }

    public void setIdentificadorTurma(String identificadorTurma) {
        this.identificadorTurma = identificadorTurma;
    }

    public MatriculaVO getMatriculaVO() {
        if (matriculaVO == null) {
            matriculaVO = new MatriculaVO();
        }
        return matriculaVO;
    }

    public void setMatriculaVO(MatriculaVO matriculaVO) {
        this.matriculaVO = matriculaVO;
    }

    public Double getMediaFinalCurso() {
        if (mediaFinalCurso == null) {
            mediaFinalCurso = 0.0;
        }
        return mediaFinalCurso;
    }

    public void setMediaFinalCurso(Double mediaFinalCurso) {
        this.mediaFinalCurso = mediaFinalCurso;
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
    

	/**
	 * @return the telefones
	 */
	public String getTelefones() {
		if (telefones == null) {
			telefones = "";
		}
		return telefones;
	}

	/**
	 * @param telefones the telefones to set
	 */
	public void setTelefones(String telefones) {
		this.telefones = telefones;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		if (email == null) {
			email = "";
		}
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
    
    
}
