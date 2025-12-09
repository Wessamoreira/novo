package negocio.comuns.academico;

import negocio.comuns.arquitetura.SuperVO;

public class ObservacaoComplementarVO extends SuperVO{
    private Integer codigo;
    private String nome;
    private Integer ordem;	
    private String observacao;
    private Boolean exigeAssinatura;
    private String funcionarioAssinatura;
    private Boolean reapresentarNomeAluno;
    private String tituloObservacao;
    private String nomeAlunoRepresentar;
    
    // transiente - utilizado para controlar se a observacao
    // esta selecionada para emissao do diploma
    private Boolean selecionado;
    private Boolean disabled;

    public ObservacaoComplementarVO(){
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

    /**
     * @return the nome
     */
    public String getNome() {
        if (nome == null) {
            nome = "";
        }
        return nome;
    }

    /**
     * @param nome the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
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
     * @return the exigeAssinatura
     */
    public Boolean getExigeAssinatura() {
        if (exigeAssinatura == null) {
            exigeAssinatura = Boolean.FALSE;
        }
        return exigeAssinatura;
    }

    /**
     * @param exigeAssinatura the exigeAssinatura to set
     */
    public void setExigeAssinatura(Boolean exigeAssinatura) {
        this.exigeAssinatura = exigeAssinatura;
    }

    /**
     * @return the funcionarioAssinatura
     */
    public String getFuncionarioAssinatura() {
        if (funcionarioAssinatura == null) {
            funcionarioAssinatura = "1";
        }
        return funcionarioAssinatura;
    }

    /**
     * @param funcionarioAssinatura the funcionarioAssinatura to set
     */
    public void setFuncionarioAssinatura(String funcionarioAssinatura) {
        this.funcionarioAssinatura = funcionarioAssinatura;
    }

    public Boolean isNovoObj() {
        if (getCodigo().equals(0)) {
            return Boolean.TRUE; 
        } else {
            return Boolean.FALSE;
        }
    }

    /**
     * @return the selecionado
     */
    public Boolean getSelecionado() {
        if (selecionado == null) { 
            selecionado = Boolean.FALSE;
        }
        return selecionado;
    }

    /**
     * @param selecionado the selecionado to set
     */
    public void setSelecionado(Boolean selecionado) {
        this.selecionado = selecionado;
    }

	public Integer getOrdem() {
		if (ordem == null) {
			ordem = 0;
		}
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	public Boolean getReapresentarNomeAluno() {
		if (reapresentarNomeAluno == null) {
			reapresentarNomeAluno = Boolean.FALSE;
		}
		return reapresentarNomeAluno;
	}

	public void setReapresentarNomeAluno(Boolean reapresentarNomeAluno) {
		this.reapresentarNomeAluno = reapresentarNomeAluno;
	}

	public String getTituloObservacao() {
		if (tituloObservacao == null) {
			tituloObservacao = "";
		}
		return tituloObservacao;
	}

	public void setTituloObservacao(String tituloObservacao) {
		this.tituloObservacao = tituloObservacao;
	}
	
	public String getNomeAlunoRepresentar() {
		if (nomeAlunoRepresentar == null) {
			nomeAlunoRepresentar = "";
		}
		return nomeAlunoRepresentar;
	}

	public void setNomeAlunoRepresentar(String nomeAlunoRepresentar) {
		this.nomeAlunoRepresentar = nomeAlunoRepresentar;
	}
	
	public Boolean getDisabled() {
		if (disabled == null) {
			disabled = Boolean.FALSE;
		}
		return disabled;
	}
	
	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}
}
