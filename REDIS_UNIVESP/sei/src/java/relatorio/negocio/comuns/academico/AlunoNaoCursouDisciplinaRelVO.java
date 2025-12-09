/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package relatorio.negocio.comuns.academico;

/**
 *
 * @author Otimize-Not
 */
public class AlunoNaoCursouDisciplinaRelVO {

    private String matricula;
    private String curso;
    private String aluno;
    private String cpf;
    private String telefoneRes;
    private String telefoneComer;
    private String telefoneRec;
    private String celular;
    private String email;
    private String unidadeEnsino;
    private String anoMatricula;
    private Integer codigoDisciplina;
    private String nomeDisciplina;
    private String cargaHorariaDisciplina;
    private String periodoLetivoDescricao;

    public String getAluno() {
        if(aluno == null){
           aluno = "";
        }
        return aluno;
    }

    public void setAluno(String aluno) {
        this.aluno = aluno;
    }

    public String getAnoMatricula() {
        if(anoMatricula == null){
            anoMatricula = "";
        }
        return anoMatricula;
    }

    public void setAnoMatricula(String anoMatricula) {
        this.anoMatricula = anoMatricula;
    }

    public String getCelular() {
        if(celular == null){
            celular = "";
        }
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getCpf() {
        if(cpf == null){
            cpf = "";
        }
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCurso() {
        if(curso == null){
            curso = "";
        }
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getEmail() {
        if(email == null){
            email = "";
        }
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMatricula() {
        if(matricula == null){
           matricula = "";
        }
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getTelefoneComer() {
        if(telefoneComer == null){
            telefoneComer = "";
        }
        return telefoneComer;
    }

    public void setTelefoneComer(String telefoneComer) {
        this.telefoneComer = telefoneComer;
    }

    public String getTelefoneRec() {
        if(telefoneRec == null){
            telefoneRec = "";
        }
        return telefoneRec;
    }

    public void setTelefoneRec(String telefoneRec) {
        this.telefoneRec = telefoneRec;
    }

    public String getTelefoneRes() {
        if(telefoneRes == null){
            telefoneRes = "";
        }
        return telefoneRes;
    }

    public void setTelefoneRes(String telefoneRes) {
        this.telefoneRes = telefoneRes;
    }

    public String getUnidadeEnsino() {
        if(unidadeEnsino == null){
        unidadeEnsino = "";
        }
        return unidadeEnsino;
    }

    public void setUnidadeEnsino(String unidadeEnsino) {
        this.unidadeEnsino = unidadeEnsino;
    }

	public Integer getCodigoDisciplina() {
		if (codigoDisciplina == null) {
			codigoDisciplina = 0;
		}
		return codigoDisciplina;
	}

	public void setCodigoDisciplina(Integer codigoDisciplina) {
		this.codigoDisciplina = codigoDisciplina;
	}

	public String getNomeDisciplina() {
		if (nomeDisciplina == null) {
			nomeDisciplina = "";
		}
		return nomeDisciplina;
	}

	public void setNomeDisciplina(String nomeDisciplina) {
		this.nomeDisciplina = nomeDisciplina;
	}

	public String getCargaHorariaDisciplina() {
		if (cargaHorariaDisciplina == null) {
			cargaHorariaDisciplina = "";
		}
		return cargaHorariaDisciplina;
	}

	public void setCargaHorariaDisciplina(String cargaHorariaDisciplina) {
		this.cargaHorariaDisciplina = cargaHorariaDisciplina;
	}

	public String getPeriodoLetivoDescricao() {
		if (periodoLetivoDescricao == null) {
			periodoLetivoDescricao = "";
		}
		return periodoLetivoDescricao;
	}

	public void setPeriodoLetivoDescricao(String periodoLetivoDescricao) {
		this.periodoLetivoDescricao = periodoLetivoDescricao;
	}
}
