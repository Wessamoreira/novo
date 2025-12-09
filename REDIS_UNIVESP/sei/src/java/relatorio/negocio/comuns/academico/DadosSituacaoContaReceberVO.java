package relatorio.negocio.comuns.academico;

import java.math.BigDecimal;
import java.util.Date;

import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoContaReceber;

public class DadosSituacaoContaReceberVO {

    private Integer contaReceber;
    private String tipoOrigem;
    private String nossoNumero;
    
    
    private Date dataVencimento;
    private Date dataPagamento;
    private String situacao;
    private String parcela;
    
    private Double valorRecebido;
    private Double valor;
    private Double valorReceberCalculado;
    private Double valorDescontoAlunoJaCalculado;
    private Double valorDescontoConvenio;
    private Double valorDescontoRateio;
    private Double valorDescontoInstituicao;
    private Double valorDescontoProgressivo;
    private Double valorCalculadoDescontoLancadoRecebimento;
    
    private Double multa;
    private Double juro;
    private Double acrescimo;
    private BigDecimal valorIndiceReajustePorAtraso;
    
    private Double valorDescontoCalculadoPrimeiraFaixaDescontos;
    private Double valorDescontoCalculadoSegundaFaixaDescontos;
    private Double valorDescontoCalculadoTerceiraFaixaDescontos;
    private Double valorDescontoCalculadoQuartaFaixaDescontos;
    private String periodoConta;
    private String descricaoCentroReceita;
    private BigDecimal valorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa;
    /**
     * Este é o método oficial para calcular o valor total dos descontos de uma conta a receber.
     * O mesmo considera que o método espírita (ContaReceber.montarListaDescontosAplicaveisContaReceber)
     * já foi chamado para atualizar todos os atributos que armazenam os valores dos descontos de uma conta
     * a receber (desconto progressivo, desconto convenio, desconto instituicao, desconto do alnuo, desconto 
     * do recebimento). Quando a conta a receber já estiver recebida, o método também retorna a soma de todos
     * estes descontos, para título de informação e relatórios. 
     */
    public Double getValorTotalDescontoContaReceber() {
        Double totalDesconto = (this.getValorDescontoAlunoJaCalculado() + 
                this.getValorDescontoConvenio() + 
                this.getValorDescontoInstituicao() + 
                this.getValorDescontoProgressivo() +
                this.getValorDescontoRateio() +
                this.getValorCalculadoDescontoLancadoRecebimento());
        		if(getValorIndiceReajustePorAtraso().doubleValue() < 0.0){
        			totalDesconto = totalDesconto +  (getValorIndiceReajustePorAtraso().doubleValue() * -1);	
        		}
        return Uteis.arrendondarForcando2CadasDecimais(totalDesconto);
    }
    
    

    public Double getValorReceberCalculado() {
		if (valorReceberCalculado == null) {
			valorReceberCalculado = 0.0;
		}
		return valorReceberCalculado;
	}



	public void setValorReceberCalculado(Double valorReceberCalculado) {
		this.valorReceberCalculado = valorReceberCalculado;
	}



	public Double getValorDescontoAlunoJaCalculado() {
		if (valorDescontoAlunoJaCalculado == null) {
			valorDescontoAlunoJaCalculado = 0.0;
		}
		return valorDescontoAlunoJaCalculado;
	}



	public void setValorDescontoAlunoJaCalculado(Double valorDescontoAlunoJaCalculado) {
		this.valorDescontoAlunoJaCalculado = valorDescontoAlunoJaCalculado;
	}



	public Double getValorDescontoConvenio() {
		if (valorDescontoConvenio == null) {
			valorDescontoConvenio = 0.0;
		}
		return valorDescontoConvenio;
	}



	public void setValorDescontoConvenio(Double valorDescontoConvenio) {
		this.valorDescontoConvenio = valorDescontoConvenio;
	}



	public Double getValorDescontoInstituicao() {
		if (valorDescontoInstituicao == null) {
			valorDescontoInstituicao = 0.0;
		}
		return valorDescontoInstituicao;
	}



	public void setValorDescontoInstituicao(Double valorDescontoInstituicao) {
		this.valorDescontoInstituicao = valorDescontoInstituicao;
	}



	public Double getValorCalculadoDescontoLancadoRecebimento() {
		if (valorCalculadoDescontoLancadoRecebimento == null) {
			valorCalculadoDescontoLancadoRecebimento = 0.0;
		}
		return valorCalculadoDescontoLancadoRecebimento;
	}



	public void setValorCalculadoDescontoLancadoRecebimento(Double valorCalculadoDescontoLancadoRecebimento) {
		this.valorCalculadoDescontoLancadoRecebimento = valorCalculadoDescontoLancadoRecebimento;
	}



	public DadosSituacaoContaReceberVO() {
    	
    }

    public String getNossoNumero() {
        if (nossoNumero == null) {
            nossoNumero = "";
        }
        return nossoNumero;
    }

    public void setNossoNumero(String nossoNumero) {
        this.nossoNumero = nossoNumero;
    }

    public Double getAcrescimo() {
        if (acrescimo == null) {
            acrescimo = 0.0;
        }
//        if(getValorIndiceReajustePorAtraso().doubleValue() >= 0.0){
//        	acrescimo = acrescimo +  getValorIndiceReajustePorAtraso().doubleValue();	
//		}
        return Uteis.arrendondarForcando2CadasDecimais(acrescimo);
    }

    public void setAcrescimo(Double acrescimo) {
        this.acrescimo = acrescimo;
    }

    public String getTipoOrigem() {
        if (tipoOrigem == null) {
            tipoOrigem = "";
        }
        return tipoOrigem;
    }

    public void setTipoOrigem(String tipoOrigem) {
        this.tipoOrigem = tipoOrigem;
    }

    public Double getMulta() {
        return multa;
    }

    public void setMulta(Double multa) {
        this.multa = multa;
    }

   

    public Double getJuro() {
        return juro;
    }

    public void setJuro(Double juro) {
        this.juro = juro;
    }

    public Double getValorRecebido() {
        return valorRecebido;
    }

    public void setValorRecebido(Double valorRecebido) {
        this.valorRecebido = valorRecebido;
    }

    public Date getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(Date dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

   

    public String getSituacao() {
        if (situacao == null) {
            situacao = "";
        }
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }
    
	public String getSituacao_Apresentar() {
		return SituacaoContaReceber.getDescricao(situacao);
	}

    public String getParcela() {
        if (parcela == null) {
            parcela = "";
        }
        return parcela;
    }

    public void setParcela(String parcela) {
        this.parcela = parcela;
    }

    public Date getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(Date dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

   

    /**
     * @return the valorDescontoProgressivo
     */
    public Double getValorDescontoProgressivo() {
        if (valorDescontoProgressivo == null) {
            valorDescontoProgressivo = 0.0;
        }
        return valorDescontoProgressivo;
    }

    /**
     * @param valorDescontoProgressivo the valorDescontoProgressivo to set
     */
    public void setValorDescontoProgressivo(Double valorDescontoProgressivo) {
        this.valorDescontoProgressivo = valorDescontoProgressivo;
    }

   

    public Double getValorDescontoCalculadoPrimeiraFaixaDescontos() {
        if (valorDescontoCalculadoPrimeiraFaixaDescontos == null) {
            valorDescontoCalculadoPrimeiraFaixaDescontos = 0.0;
        }
        return valorDescontoCalculadoPrimeiraFaixaDescontos;
    }

    public void setValorDescontoCalculadoPrimeiraFaixaDescontos(Double valorDescontoCalculadoPrimeiraFaixaDescontos) {
        this.valorDescontoCalculadoPrimeiraFaixaDescontos = valorDescontoCalculadoPrimeiraFaixaDescontos;
    }

    public Double getValorDescontoCalculadoSegundaFaixaDescontos() {
        if (valorDescontoCalculadoSegundaFaixaDescontos == null) {
            valorDescontoCalculadoSegundaFaixaDescontos = 0.0;
        }
        return valorDescontoCalculadoSegundaFaixaDescontos;
    }

    public void setValorDescontoCalculadoSegundaFaixaDescontos(Double valorDescontoCalculadoSegundaFaixaDescontos) {
        this.valorDescontoCalculadoSegundaFaixaDescontos = valorDescontoCalculadoSegundaFaixaDescontos;
    }

    public Double getValorDescontoCalculadoTerceiraFaixaDescontos() {
        if (valorDescontoCalculadoTerceiraFaixaDescontos == null) {
            valorDescontoCalculadoTerceiraFaixaDescontos = 0.0;
        }
        return valorDescontoCalculadoTerceiraFaixaDescontos;
    }

    public void setValorDescontoCalculadoTerceiraFaixaDescontos(Double valorDescontoCalculadoTerceiraFaixaDescontos) {
        this.valorDescontoCalculadoTerceiraFaixaDescontos = valorDescontoCalculadoTerceiraFaixaDescontos;
    }

    public Double getValorDescontoCalculadoQuartaFaixaDescontos() {
        if (valorDescontoCalculadoQuartaFaixaDescontos == null) {
            valorDescontoCalculadoQuartaFaixaDescontos = 0.0;
        }
        return valorDescontoCalculadoQuartaFaixaDescontos;
    }

    public void setValorDescontoCalculadoQuartaFaixaDescontos(Double valorDescontoCalculadoQuartaFaixaDescontos) {
        this.valorDescontoCalculadoQuartaFaixaDescontos = valorDescontoCalculadoQuartaFaixaDescontos;
    }

    public Integer getContaReceber() {
        if (contaReceber == null) {
            contaReceber = 0;
        }
        return contaReceber;
    }

    public void setContaReceber(Integer contaReceber) {
        this.contaReceber = contaReceber;
    }

   

	public String getPeriodoConta() {
		if (periodoConta == null) {
			periodoConta = "";
		}
		return periodoConta;
	}

	public void setPeriodoConta(String periodoConta) {
		this.periodoConta = periodoConta;
	}
	


	public Double getValorDescontoRateio() {
		if(valorDescontoRateio == null){
			valorDescontoRateio = 0.0;
		}
		return valorDescontoRateio;
	}



	public void setValorDescontoRateio(Double valorDescontoRateio) {
		this.valorDescontoRateio = valorDescontoRateio;
	}
	
	public String getDescricaoCentroReceita() {
		if (descricaoCentroReceita == null) {
			descricaoCentroReceita = "";
		}
		return descricaoCentroReceita;
	}



	public void setDescricaoCentroReceita(String descricaoCentroReceita) {
		this.descricaoCentroReceita = descricaoCentroReceita;
	}



	public BigDecimal getValorIndiceReajustePorAtraso() {
		if (valorIndiceReajustePorAtraso == null) {
			valorIndiceReajustePorAtraso = BigDecimal.ZERO;
		}
		return valorIndiceReajustePorAtraso;
	}



	public void setValorIndiceReajustePorAtraso(BigDecimal valorIndiceReajustePorAtraso) {
		this.valorIndiceReajustePorAtraso = valorIndiceReajustePorAtraso;
	}

	public BigDecimal getValorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa() {
		if (valorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa == null) {
			valorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa = BigDecimal.ZERO;
		}
		return valorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa;
	}

	public void setValorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa(
			BigDecimal valorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa) {
		this.valorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa = valorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa;
	}
}
