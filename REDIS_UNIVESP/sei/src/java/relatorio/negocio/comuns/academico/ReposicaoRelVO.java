package relatorio.negocio.comuns.academico;

import java.util.Date;
import java.util.List;
import negocio.comuns.utilitarias.Uteis;

public class ReposicaoRelVO {

    private String unidadeEnsino;
    private String matricula;
    private String curso;
    private Integer codTurmaOrigem;
    private String turmaOrigem;
    private Integer codTurmaInclusao;
    private String turmaInclusao;
    private String unidadeInclusao;
    private String aluno;
    private String disciplinaInclusao;
    private Date dataInclusao;
    private Boolean reposicao;
    private String situacao;
    private String justificativa;
    private String observacao;
    private Double desconto;
    private Date dataPagamento;
    private Date dataAula;
    private String situacaoHist;
    private Double valorTotalRecebimento;

    public ReposicaoRelVO() {

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

    public String getTurmaOrigem() {
        if (turmaOrigem == null) {
            turmaOrigem = "";
        }
        return turmaOrigem;
    }

    public void setTurmaOrigem(String turmaOrigem) {
        this.turmaOrigem = turmaOrigem;
    }

    public String getTurmaInclusao() {
        if (turmaInclusao == null) {
            turmaInclusao = "";
        }
        return turmaInclusao;
    }

    public void setTurmaInclusao(String turmaInclusao) {
        this.turmaInclusao = turmaInclusao;
    }
    
    /**
     * @return the matricula
     */
    public String getMatricula() {
        return matricula;
    }

    /**
     * @param matricula the matricula to set
     */
    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    /**
     * @return the codTurmaOrigem
     */
    public Integer getCodTurmaOrigem() {
        if (codTurmaOrigem == null) {
            codTurmaOrigem = 0;
        }
        return codTurmaOrigem;
    }

    /**
     * @param codTurmaOrigem the codTurmaOrigem to set
     */
    public void setCodTurmaOrigem(Integer codTurmaOrigem) {
        this.codTurmaOrigem = codTurmaOrigem;
    }

    /**
     * @return the codTurmaInclusao
     */
    public Integer getCodTurmaInclusao() {
        if (codTurmaInclusao == null) {
            codTurmaInclusao = 0;
        }
        return codTurmaInclusao;
    }

    /**
     * @param codTurmaInclusao the codTurmaInclusao to set
     */
    public void setCodTurmaInclusao(Integer codTurmaInclusao) {
        this.codTurmaInclusao = codTurmaInclusao;
    }

    /**
     * @return the aluno
     */
    public String getAluno() {
        if (aluno == null) {
            aluno = "";
        }
        return aluno;
    }

    /**
     * @param aluno the aluno to set
     */
    public void setAluno(String aluno) {
        this.aluno = aluno;
    }

    /**
     * @return the disciplinaInclusao
     */
    public String getDisciplinaInclusao() {
        if (disciplinaInclusao == null) {
            disciplinaInclusao = "";
        }
        return disciplinaInclusao;
    }

    /**
     * @param disciplinaInclusao the disciplinaInclusao to set
     */
    public void setDisciplinaInclusao(String disciplinaInclusao) {
        this.disciplinaInclusao = disciplinaInclusao;
    }

    /**
     * @return the dataInclusao
     */
    public Date getDataInclusao() {
        if (dataInclusao == null) {
            dataInclusao = new Date();
        }
        return dataInclusao;
    }

    public String getDataInclusao_Apresentar() {
        return (Uteis.getData(getDataInclusao()));
    }

    public String getDataPagamento_Apresentar() {
    	return (Uteis.getData(getDataPagamento()));
    }
    
    public String getDataAula_Apresentar() {
    	return (Uteis.getData(getDataAula()));
    }
    
    /**
     * @param dataInclusao the dataInclusao to set
     */
    public void setDataInclusao(Date dataInclusao) {
        this.dataInclusao = dataInclusao;
    }

    /**
     * @return the reposicao
     */
    public Boolean getReposicao() {
        if (reposicao == null) {
            reposicao = Boolean.FALSE;
        }
        return reposicao;
    }

    /**
     * @param reposicao the reposicao to set
     */
    public void setReposicao(Boolean reposicao) {
        this.reposicao = reposicao;
    }

    /**
     * @return the situacao
     */
    public String getSituacao() {
        if (situacao == null) {
            situacao = "";
        }
        return situacao;
    }

    /**
     * @param situacao the situacao to set
     */
    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    /**
     * @return the justificativa
     */
    public String getJustificativa() {
        if (justificativa == null) {
            justificativa = "";
        }
        return justificativa;
    }

    /**
     * @param justificativa the justificativa to set
     */
    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }

    /**
     * @return the observacao
     */
    public String getObservacao() {
        if (observacao == null) {
            observacao = "";
        }
        return observacao;
    }

    /**
     * @param observacao the observacao to set
     */
    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    /**
     * @return the desconto
     */
    public Double getDesconto() {
        if (desconto == null) {
            desconto = new Double(0);
        }
        return desconto;
    }

    /**
     * @param desconto the desconto to set
     */
    public void setDesconto(Double desconto) {
        this.desconto = desconto;
    }

	public Date getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public Date getDataAula() {		
		return dataAula;
	}

	public void setDataAula(Date dataAula) {
		this.dataAula = dataAula;
	}

	public String getSituacaoHist() {
		if (situacaoHist == null) {
			situacaoHist = "";
		}
		return situacaoHist;
	}

	public void setSituacaoHist(String situacaoHist) {
		this.situacaoHist = situacaoHist;
	}

	public Double getValorTotalRecebimento() {
		if (valorTotalRecebimento == null) {
			valorTotalRecebimento = 0.0;
		}
		return valorTotalRecebimento;
	}

	public void setValorTotalRecebimento(Double valorTotalRecebimento) {
		this.valorTotalRecebimento = valorTotalRecebimento;
	}

	public String getUnidadeInclusao() {
		if (unidadeInclusao == null) {
			unidadeInclusao = "";
		}
		return unidadeInclusao;
	}

	public void setUnidadeInclusao(String unidadeInclusao) {
		this.unidadeInclusao = unidadeInclusao;
	}
	
	

}
