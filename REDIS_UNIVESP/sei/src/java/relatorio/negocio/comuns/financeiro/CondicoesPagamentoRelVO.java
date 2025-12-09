package relatorio.negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.financeiro.ContaReceberVO;

public class CondicoesPagamentoRelVO {

    protected Integer unidadeEnsinoId;
    protected String unidadeEnsino;
    protected String matriculaAluno;
    protected String nomeAluno;
    protected String situacao;
    protected String turma;
    protected String curso;
    protected String quantidadeParcelas;
    protected String quantidadeParcelasGeradas;
    protected Double valorParcela;
    protected Double valorDesconto;
    protected Double valorFinal;
    protected String dataPrimeiroVencimento;
    private String bolsa;
    protected ContaReceberVO contaReceber;
    protected String ano;
    protected String semestre;
    private Integer qtdeAlunosNaoAtivos;
    private Integer qtdeBolsistas;
    private Integer qtdePagantes;
    private Double qtdeAlunosUsamPlanoFinanceiro;
    private Integer totalAlunosUsamPlanoFinanceiro;
    private String nomeConsultor;
    private Integer codigoCondicaoPagamento;
    private String condicaoPagamento;
    private String categoriaCondicaoPagamento;
    private String descontoAluno;
    public List<CondicoesPagamentoRelVO> listaCondicaoPagamentoVOs;
    public List<CondicoesPagamentoRelVO> listaDescontoAlunoVOs;
    private Double qtdeAlunosUsamPlanoDesconto;

    public CondicoesPagamentoRelVO() {
    }

	public Integer getUnidadeEnsinoId() {
		return unidadeEnsinoId;
	}

	public void setUnidadeEnsinoId(Integer unidadeEnsinoId) {
		this.unidadeEnsinoId = unidadeEnsinoId;
	}

	public String getUnidadeEnsino() {
		return unidadeEnsino;
	}

	public void setUnidadeEnsino(String unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}

	public String getMatriculaAluno() {
		return matriculaAluno;
	}

	public void setMatriculaAluno(String matriculaAluno) {
		this.matriculaAluno = matriculaAluno;
	}

	public String getNomeAluno() {
		return nomeAluno;
	}

	public void setNomeAluno(String nomeAluno) {
		this.nomeAluno = nomeAluno;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public String getQuantidadeParcelas() {
		return quantidadeParcelas;
	}

	public void setQuantidadeParcelas(String quantidadeParcelas) {
		this.quantidadeParcelas = quantidadeParcelas;
	}

	public String getQuantidadeParcelasGeradas() {
		return quantidadeParcelasGeradas;
	}

	public void setQuantidadeParcelasGeradas(String quantidadeParcelasGeradas) {
		this.quantidadeParcelasGeradas = quantidadeParcelasGeradas;
	}

	public Double getValorParcela() {
		return valorParcela;
	}

	public void setValorParcela(Double valorParcela) {
		this.valorParcela = valorParcela;
	}

	public Double getValorDesconto() {
		return valorDesconto;
	}

	public void setValorDesconto(Double valorDesconto) {
		this.valorDesconto = valorDesconto;
	}

	public Double getValorFinal() {
		return valorFinal;
	}

	public void setValorFinal(Double valorFinal) {
		this.valorFinal = valorFinal;
	}

	public String getDataPrimeiroVencimento() {
		return dataPrimeiroVencimento;
	}

	public void setDataPrimeiroVencimento(String dataPrimeiroVencimento) {
		this.dataPrimeiroVencimento = dataPrimeiroVencimento;
	}

	public ContaReceberVO getContaReceber() {
		return contaReceber;
	}

	public void setContaReceber(ContaReceberVO contaReceber) {
		this.contaReceber = contaReceber;
	}

	public String getTurma() {
		return turma;
	}

	public void setTurma(String turma) {
		this.turma = turma;
	}

	public String getCurso() {
		return curso;
	}

	public void setCurso(String curso) {
		this.curso = curso;
	}

	public String getCondicaoPagamento() {
		if (condicaoPagamento == null) {
			condicaoPagamento = "";
		}
		return condicaoPagamento;
	}

	public void setCondicaoPagamento(String condicaoPagamento) {
		this.condicaoPagamento = condicaoPagamento;
	}

	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getSemestre() {
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

    /**
     * @return the qtdeAlunosNaoAtivos
     */
    public Integer getQtdeAlunosNaoAtivos() {
        if(qtdeAlunosNaoAtivos == null) {
            qtdeAlunosNaoAtivos = 0;
        }
        return qtdeAlunosNaoAtivos;
    }

    /**
     * @param qtdeAlunosNaoAtivos the qtdeAlunosNaoAtivos to set
     */
    public void setQtdeAlunosNaoAtivos(Integer qtdeAlunosNaoAtivos) {
        this.qtdeAlunosNaoAtivos = qtdeAlunosNaoAtivos;
    }

    /**
     * @return the qtdeBolsistas
     */
    public Integer getQtdeBolsistas() {
        if(qtdeBolsistas == null) {
            qtdeBolsistas = 0;
        }
        return qtdeBolsistas;
    }

    /**
     * @param qtdeBolsistas the qtdeBolsistas to set
     */
    public void setQtdeBolsistas(Integer qtdeBolsistas) {
        this.qtdeBolsistas = qtdeBolsistas;
    }

    /**
     * @return the qtdePagantes
     */
    public Integer getQtdePagantes() {
        if(qtdePagantes == null) {
            qtdePagantes = 0;
        }
        return qtdePagantes;
    }

    /**
     * @param qtdePagantes the qtdePagantes to set
     */
    public void setQtdePagantes(Integer qtdePagantes) {
        this.qtdePagantes = qtdePagantes;
    }

    /**
     * @return the bolsa
     */
    public String getBolsa() {
        if(bolsa == null) {
            bolsa = "";
        }
        return bolsa;
    }

    /**
     * @param bolsa the bolsa to set
     */
    public void setBolsa(String bolsa) {
        this.bolsa = bolsa;
    }

    public String getNomeConsultor() {
        return nomeConsultor;
    }

    public void setNomeConsultor(String nomeConsultor) {
        this.nomeConsultor = nomeConsultor;
    }

	public Double getQtdeAlunosUsamPlanoFinanceiro() {
		if (qtdeAlunosUsamPlanoFinanceiro == null) {
			qtdeAlunosUsamPlanoFinanceiro = 0.0;
		}
		return qtdeAlunosUsamPlanoFinanceiro;
	}

	public void setQtdeAlunosUsamPlanoFinanceiro(Double qtdeAlunosUsamPlanoFinanceiro) {
		this.qtdeAlunosUsamPlanoFinanceiro = qtdeAlunosUsamPlanoFinanceiro;
	}

	public String getCategoriaCondicaoPagamento() {
		if (categoriaCondicaoPagamento == null)
			categoriaCondicaoPagamento = "";
		return categoriaCondicaoPagamento;
	}

	public void setCategoriaCondicaoPagamento(String categoriaCondicaoPagamento) {
		this.categoriaCondicaoPagamento = categoriaCondicaoPagamento;
	}

	public Integer getCodigoCondicaoPagamento() {
		if (codigoCondicaoPagamento == null) {
			codigoCondicaoPagamento = 0;
		}
		return codigoCondicaoPagamento;
	}

	public void setCodigoCondicaoPagamento(Integer codigoCondicaoPagamento) {
		this.codigoCondicaoPagamento = codigoCondicaoPagamento;
	}

	public Integer getTotalAlunosUsamPlanoFinanceiro() {
		if (totalAlunosUsamPlanoFinanceiro == null) {
			totalAlunosUsamPlanoFinanceiro = 0;
		}
		return totalAlunosUsamPlanoFinanceiro;
	}

	public void setTotalAlunosUsamPlanoFinanceiro(Integer totalAlunosUsamPlanoFinanceiro) {
		this.totalAlunosUsamPlanoFinanceiro = totalAlunosUsamPlanoFinanceiro;
	}

	public String getDescontoAluno() {
		if (descontoAluno == null) {
			descontoAluno = "";
		}
		return descontoAluno;
	}

	public void setDescontoAluno(String descontoAluno) {
		this.descontoAluno = descontoAluno;
	}

	public List<CondicoesPagamentoRelVO> getListaCondicaoPagamentoVOs() {
		if (listaCondicaoPagamentoVOs == null) {
			listaCondicaoPagamentoVOs = new ArrayList<CondicoesPagamentoRelVO>();
		}
		return listaCondicaoPagamentoVOs;
	}

	public void setListaCondicaoPagamentoVOs(List<CondicoesPagamentoRelVO> listaCondicaoPagamentoVOs) {
		this.listaCondicaoPagamentoVOs = listaCondicaoPagamentoVOs;
	}

	public List<CondicoesPagamentoRelVO> getListaDescontoAlunoVOs() {
		if (listaDescontoAlunoVOs == null) {
			listaDescontoAlunoVOs = new ArrayList<CondicoesPagamentoRelVO>();
		}
		return listaDescontoAlunoVOs;
	}

	public void setListaDescontoAlunoVOs(List<CondicoesPagamentoRelVO> listaDescontoAlunoVOs) {
		this.listaDescontoAlunoVOs = listaDescontoAlunoVOs;
	}

	public Double getQtdeAlunosUsamPlanoDesconto() {
		return qtdeAlunosUsamPlanoDesconto;
	}

	public void setQtdeAlunosUsamPlanoDesconto(Double qtdeAlunosUsamPlanoDesconto) {
		this.qtdeAlunosUsamPlanoDesconto = qtdeAlunosUsamPlanoDesconto;
	}

}