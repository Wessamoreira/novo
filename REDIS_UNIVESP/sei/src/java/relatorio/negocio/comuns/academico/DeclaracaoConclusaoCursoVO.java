package relatorio.negocio.comuns.academico;

import negocio.comuns.administrativo.FuncionarioVO;

public class DeclaracaoConclusaoCursoVO {

	private String curso;
	private String nome;
	private String cpf;
	private String rg;
	private String matricula;
	private String dataHoje;
	private String dataColacao;
	private String semestre;
	private String ano;
	private String nomeUnidadeEnsino;
	private Integer tipoLayout;
	private String informacoesAdicionais;
	private String cidadeUnidadeEnsino;
	private String tituloCurso;
	private String cargaHoraria;
	private String resolucao;
	private String inicio;
	private String termino;
	private FuncionarioVO funcionarioPrincipalVO;
	private String texto;
	private Integer textoPadraoDeclaracao;

	public DeclaracaoConclusaoCursoVO() {

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

	public String getCurso() {
		if (curso == null) {
			curso = "";
		}
		return curso;
	}

	public void setCurso(String curso) {
		this.curso = curso;
	}

	public String getCpf() {
		if (cpf == null) {
			cpf = "";
		}
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getRg() {
		if (rg == null) {
			rg = "";
		}
		return rg;
	}

	public void setRg(String rg) {
		this.rg = rg;
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

	public String getDataHoje() {
		if (dataHoje == null) {
			dataHoje = "";
		}
		return dataHoje;
	}

	public void setDataHoje(String dataHoje) {
		this.dataHoje = dataHoje;
	}

	public String getDataColacao() {
		if (dataColacao == null) {
			dataColacao = "";
		}
		return dataColacao;
	}

	public void setDataColacao(String dataColacao) {
		this.dataColacao = dataColacao;
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

	public void setNomeUnidadeEnsino(String nomeUnidadeEnsino) {
		this.nomeUnidadeEnsino = nomeUnidadeEnsino;
	}

	public String getNomeUnidadeEnsino() {
		if (nomeUnidadeEnsino == null) {
			nomeUnidadeEnsino = "";
		}
		return nomeUnidadeEnsino;
	}

    public Integer getTipoLayout() {
        if (tipoLayout == null){
            tipoLayout = 0;
        }
        return tipoLayout;
    }

    public void setTipoLayout(Integer tipoDeclaracao) {
        this.tipoLayout = tipoDeclaracao;
    }

	public String getInformacoesAdicionais() {
		if (informacoesAdicionais == null) {
			informacoesAdicionais = "";
		}
		return informacoesAdicionais;
	}

    public void setInformacoesAdicionais(String informacoesAdicionais) {
        this.informacoesAdicionais = informacoesAdicionais;
    }

    public String getCidadeUnidadeEnsino() {
        return cidadeUnidadeEnsino;
    }

    public void setCidadeUnidadeEnsino(String cidadeUnidadeEnsino) {
        this.cidadeUnidadeEnsino = cidadeUnidadeEnsino;
    }

    public String getTituloCurso() {
        return tituloCurso;
    }

    public void setTituloCurso(String tituloCurso) {
        this.tituloCurso = tituloCurso;
    }

    public String getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(String cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    public String getResolucao() {
        return resolucao;
    }

    public void setResolucao(String resolucao) {
        this.resolucao = resolucao;
    }

    public String getInicio() {
        return inicio;
    }

    public void setInicio(String inicio) {
        this.inicio = inicio;
    }

    public String getTermino() {
        return termino;
    }

    public void setTermino(String termino) {
        this.termino = termino;
    }

    public Boolean getApresentarInformacoesAdicionais() {
        if (getTipoLayout().intValue() == 0){
            return false;
        }
        return true;
    }


    public FuncionarioVO getFuncionarioPrincipalVO() {
        if (funcionarioPrincipalVO == null){
            funcionarioPrincipalVO = new FuncionarioVO();
        }
        return funcionarioPrincipalVO;
    }

    public void setFuncionarioPrincipalVO(FuncionarioVO funcionarioPrincipalVO) {
        this.funcionarioPrincipalVO = funcionarioPrincipalVO;
    }

    public String getTexto() {
        if (texto == null){
            if (getTipoLayout().intValue() == 1){
                texto = "Adicionalmente informamos que a aprovação no Trabalho de Conclusão do Curso (TCC) se deu em ____ de __________________de ______________com nota _____.";
            } else {
                texto = "";
            }
        }
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
    
    public Integer getTextoPadraoDeclaracao() {
		if(textoPadraoDeclaracao == null){
			textoPadraoDeclaracao = 0;
		}
		return textoPadraoDeclaracao;
	}

	public void setTextoPadraoDeclaracao(Integer textoPadraoDeclaracao) {
		this.textoPadraoDeclaracao = textoPadraoDeclaracao;
	}
	


}
