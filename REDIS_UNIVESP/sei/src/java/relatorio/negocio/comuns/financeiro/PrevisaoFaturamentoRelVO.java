package relatorio.negocio.comuns.financeiro;

import java.util.Date;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.utilitarias.Uteis;

public class PrevisaoFaturamentoRelVO {

    private String nomePessoa;
    private String tipoPessoa;
    private String matricula;
    private String curso;
    private String turno;
    private String turma;
    private String tipoOrigem;
    private String parcela;
    private String unidadeEnsino;
    private Double valor;
    private Double valorPrimeiraFaixa;
    private Date data;
    
    public PrevisaoFaturamentoRelVO() {
        
        
    }

    /**
     * @return the tipoParcela
     */
    public String getTipoOrigem() {
        if(tipoOrigem == null) {
            tipoOrigem = "";
        }
        return tipoOrigem;
    }

    /**
     * @param tipoParcela the tipoParcela to set
     */
    public void setTipoOrigem(String tipoOrigem) {
        this.tipoOrigem = tipoOrigem;
    }

    /**
     * @return the parcela
     */
    public String getParcela() {
        if(parcela == null) {
            parcela = "";
        }
        return parcela;
    }

    /**
     * @param parcela the parcela to set
     */
    public void setParcela(String parcela) {
        this.parcela = parcela;
    }

    /**
     * @return the valor
     */
    public Double getValor() {
        if(valor == null) {
            valor = 0.0;
        }
        return valor;
    }

    /**
     * @param valor the valor to set
     */
    public void setValor(Double valor) {
        this.valor = valor;
    }

    /**
     * @return the valorPrimeiraFaixa
     */
    public Double getValorPrimeiraFaixa() {
        if(valorPrimeiraFaixa == null) {
            valorPrimeiraFaixa = 0.0;
        }
        return valorPrimeiraFaixa;
    }

    /**
     * @param valorPrimeiraFaixa the valorPrimeiraFaixa to set
     */
    public void setValorPrimeiraFaixa(Double valorPrimeiraFaixa) {
        this.valorPrimeiraFaixa = valorPrimeiraFaixa;
    }

    /**
     * @return the data
     */
    public Date getData() {
        return data;
    }
    
    public String getData_Apresentar() {
		return (Uteis.getData(data));
	}

    /**
     * @param data the data to set
     */
    public void setData(Date data) {
        this.data = data;
    }

    /**
     * @return the turma
     */
    public String getTurma() {
        if(turma == null) {
            turma = "";
        }
        return turma;
    }

    /**
     * @param turma the turma to set
     */
    public void setTurma(String turma) {
        this.turma = turma;
    }

	public String getNomePessoa() {
		if (nomePessoa == null) {
			nomePessoa = "";
		}
		return nomePessoa;
	}

	public void setNomePessoa(String nomePessoa) {
		this.nomePessoa = nomePessoa;
	}

	public String getTipoPessoa() {
		if (tipoPessoa == null) {
			tipoPessoa = "";
		}
		return tipoPessoa;
	}

	public void setTipoPessoa(String tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
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

	public String getUnidadeEnsino() {
		if (unidadeEnsino == null) {
			unidadeEnsino = "";
		}
		return unidadeEnsino;
	}

	public void setUnidadeEnsino(String unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}
    
}
