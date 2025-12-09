package relatorio.negocio.comuns.financeiro;

import java.util.Date;

import negocio.comuns.utilitarias.dominios.TipoSacado;

public class PagamentoRelVO {

    private Date dataPagamento;
    private Integer negociacao;
    private Double valor;
    private Double valorPagamento;
    private Double valorTroco;
    private String tipoSacado;
    private Integer unidadeEnsino;
    private String nomeUnidadeEnsino;
    private String funcionario;
    private String responsavelFinanceiro;
    private String parceiro;
    private String fornecedor;
    private String banco;
    private String aluno;
    private String formaPagamento;
    private String categoriaDespesa;
    private String numeroDocumento;
    private Date dataContaPagar;
    private Date dataFatoGerador;
    private Double valorPago;
    private Integer codigoContaPagar;
    private Double juro;
    private Double multa;
    private Double desconto;
    private String formaPagamentoDinheiro;
    private String formaPagamentoDebito;
    private String formaPagamentoCartao;
    private String formaPagamentoCheque;
    private String formaPagamentoBoleto;
    private String formaPagamentoIsencao;
    private String formaPagamentoPermuta;
    private String formaPagamentoCreditoDebito;
    private String formaPagamentoDeposito;
    private String contaCorrente;
    private Boolean tipoContaCorrente;
    private Integer codigoPagamento;
    private String operadoraCartao;
    private String formaPagamentoApresentar;
    private String contaCorrenteApresentar;

    public PagamentoRelVO() {
        setBanco("");
        setCodigoContaPagar(0);
        setDataContaPagar(new Date());
        setDataPagamento(new Date());
        setDesconto(0.0);
        setFormaPagamentoBoleto("");
        setFormaPagamentoCartao("");
        setFormaPagamentoCheque("");
        setFormaPagamentoDebito("");
        setFormaPagamentoDinheiro("");
        setFormaPagamentoIsencao("");
        setFormaPagamentoPermuta("");
        setFormaPagamentoCreditoDebito("");
        setFormaPagamentoDeposito("");
        setFornecedor("");
        setFuncionario("");
        setJuro(0.0);
        setMulta(0.0);
        setNegociacao(0);
        setNumeroDocumento("");
        setTipoSacado("");
        setUnidadeEnsino(0);
        setNomeUnidadeEnsino("");
        setValor(0.0);
        setValorPagamento(0.0);
        setValorPago(0.0);
        setValorTroco(0.0);
    }

    public Date getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(Date dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public Integer getNegociacao() {
        return negociacao;
    }

    public void setNegociacao(Integer negociacao) {
        this.negociacao = negociacao;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Double getValorPagamento() {
        return valorPagamento;
    }

    public void setValorPagamento(Double valorPagamento) {
        this.valorPagamento = valorPagamento;
    }

    public Double getValorTroco() {
        return valorTroco;
    }

    public void setValorTroco(Double valorTroco) {
        this.valorTroco = valorTroco;
    }

    public String getTipoSacado() {
        return tipoSacado;
    }

    public void setTipoSacado(String tipoSacado) {
        this.tipoSacado = tipoSacado;
    }

    public Integer getUnidadeEnsino() {
        return unidadeEnsino;
    }

    public void setUnidadeEnsino(Integer unidadeEnsino) {
        this.unidadeEnsino = unidadeEnsino;
    }

    public String getNomeUnidadeEnsino() {
        return nomeUnidadeEnsino;
    }

    public void setNomeUnidadeEnsino(String nomeUnidadeEnsino) {
        this.nomeUnidadeEnsino = nomeUnidadeEnsino;
    }

    public String getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(String funcionario) {
        this.funcionario = funcionario;
    }

    public String getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(String fornecedor) {
        this.fornecedor = fornecedor;
    }

    public String getBanco() {
		if (banco == null) {
			banco = "";
		}
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public Date getDataContaPagar() {
        return dataContaPagar;
    }

    public void setDataContaPagar(Date dataContaPagar) {
        this.dataContaPagar = dataContaPagar;
    }

    public Double getValorPago() {
        return valorPago;
    }

    public void setValorPago(Double valorPago) {
        this.valorPago = valorPago;
    }

    public Integer getCodigoContaPagar() {
        return codigoContaPagar;
    }

    public void setCodigoContaPagar(Integer codigoContaPagar) {
        this.codigoContaPagar = codigoContaPagar;
    }

    public Double getJuro() {
        return juro;
    }

    public void setJuro(Double juro) {
        this.juro = juro;
    }

    public Double getMulta() {
        return multa;
    }

    public void setMulta(Double multa) {
        this.multa = multa;
    }

    public Double getDesconto() {
        return desconto;
    }

    public void setDesconto(Double desconto) {
        this.desconto = desconto;
    }

    public String getFormaPagamentoDinheiro() {
        return formaPagamentoDinheiro;
    }

    public void setFormaPagamentoDinheiro(String formaPagamentoDinheiro) {
        this.formaPagamentoDinheiro = formaPagamentoDinheiro;
    }

    public String getFormaPagamentoDebito() {
        return formaPagamentoDebito;
    }

    public void setFormaPagamentoDebito(String formaPagamentoDebito) {
        this.formaPagamentoDebito = formaPagamentoDebito;
    }

    public String getFormaPagamentoCartao() {
        return formaPagamentoCartao;
    }

    public void setFormaPagamentoCartao(String formaPagamentoCartao) {
        this.formaPagamentoCartao = formaPagamentoCartao;
    }

    public String getFormaPagamentoCheque() {
        return formaPagamentoCheque;
    }

    public void setFormaPagamentoCheque(String formaPagamentoCheque) {
        this.formaPagamentoCheque = formaPagamentoCheque;
    }

    public String getFormaPagamentoBoleto() {
        return formaPagamentoBoleto;
    }

    public void setFormaPagamentoBoleto(String formaPagamentoBoleto) {
        this.formaPagamentoBoleto = formaPagamentoBoleto;
    }

    public String getContaCorrente() {
        return contaCorrente;
    }

    public void setContaCorrente(String contaCorrente) {
        this.contaCorrente = contaCorrente;
    }

    public Boolean getTipoContaCorrente() {
        return tipoContaCorrente;
    }

    public void setTipoContaCorrente(Boolean tipoContaCorrente) {
        this.tipoContaCorrente = tipoContaCorrente;
    }

    public String getAluno() {
        if (aluno == null) {
            aluno = "";
        }
        return aluno;
    }

    public void setAluno(String aluno) {
        this.aluno = aluno;
    }

    public String getCategoriaDespesa() {
        if (categoriaDespesa == null) {
            categoriaDespesa = "";
        }

        return categoriaDespesa;
    }

    public void setCategoriaDespesa(String categoriaDespesa) {
        this.categoriaDespesa = categoriaDespesa;
    }

    public String getFormaPagamento() {
        if (formaPagamento == null) {
            formaPagamento = "";
        }
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public Integer getCodigoPagamento() {
        if (codigoPagamento == null) {
            codigoPagamento = 0;
        }
        return codigoPagamento;
    }

    public void setCodigoPagamento(Integer codigoPagamento) {
        this.codigoPagamento = codigoPagamento;
    }

    public String getResponsavelFinanceiro() {
        if (responsavelFinanceiro == null) {
            responsavelFinanceiro = "";
        }
        return responsavelFinanceiro;
    }

    public void setResponsavelFinanceiro(String responsavelFinanceiro) {
        this.responsavelFinanceiro = responsavelFinanceiro;
    }

    public String getParceiro() {
        if (parceiro == null) {
            parceiro = "";
        }
        return parceiro;
    }

    public void setParceiro(String parceiro) {
        this.parceiro = parceiro;
    }

    private String favorecido;

    public void setFavorecido(String favorecido) {
        this.favorecido = favorecido;
    }

    public String getFavorecido() {
        if (favorecido == null) {
            if (getTipoSacado().equals(TipoSacado.FORNECEDOR.getValor())) {
                setFavorecido(getFornecedor());
            } else if (getTipoSacado().equals(TipoSacado.FUNCIONARIO_PROFESSOR.getValor())) {
                setFavorecido(getFuncionario());
            } else if (getTipoSacado().equals(TipoSacado.ALUNO.getValor())) {
                setFavorecido(getAluno());
            } else if (getTipoSacado().equals(TipoSacado.BANCO.getValor())) {
                setFavorecido(getBanco());
            } else if (getTipoSacado().equals(TipoSacado.FORNECEDOR.getValor())) {
                setFavorecido(getFornecedor());
            } else if (getTipoSacado().equals(TipoSacado.PARCEIRO.getValor())) {
                setFavorecido(getParceiro());
            } else if (getTipoSacado().equals(TipoSacado.RESPONSAVEL_FINANCEIRO.getValor())) {
                setFavorecido(getResponsavelFinanceiro());
            } else if (getTipoSacado().equals(TipoSacado.OPERADORA_CARTAO.getValor())) {
            	setFavorecido(getOperadoraCartao());
            } else {
                favorecido = "";
            }
        }
        return favorecido;
    }

    /**
     * @return the dataFatoGerador
     */
    public Date getDataFatoGerador() {    	
        return dataFatoGerador;
    }

    /**
     * @param dataFatoGerador the dataFatoGerador to set
     */
    public void setDataFatoGerador(Date dataFatoGerador) {
        this.dataFatoGerador = dataFatoGerador;
    }

	public String getFormaPagamentoIsencao() {
		return formaPagamentoIsencao;
	}

	public void setFormaPagamentoIsencao(String formaPagamentoIsencao) {
		this.formaPagamentoIsencao = formaPagamentoIsencao;
	}

	public String getFormaPagamentoPermuta() {
		return formaPagamentoPermuta;
	}

	public void setFormaPagamentoPermuta(String formaPagamentoPermuta) {
		this.formaPagamentoPermuta = formaPagamentoPermuta;
	}

	public String getFormaPagamentoCreditoDebito() {
		return formaPagamentoCreditoDebito;
	}

	public void setFormaPagamentoCreditoDebito(String formaPagamentoCreditoDebito) {
		this.formaPagamentoCreditoDebito = formaPagamentoCreditoDebito;
	}

	public String getFormaPagamentoDeposito() {
		return formaPagamentoDeposito;
	}

	public void setFormaPagamentoDeposito(String formaPagamentoDeposito) {
		this.formaPagamentoDeposito = formaPagamentoDeposito;
	}

	public String getOperadoraCartao() {
		if (operadoraCartao == null) {
			operadoraCartao = "";
		}
		return operadoraCartao;
	}

	public void setOperadoraCartao(String operadoraCartao) {
		this.operadoraCartao = operadoraCartao;
	}

	public String getFormaPagamentoApresentar() {
		if (formaPagamentoApresentar == null) {
			formaPagamentoApresentar = "";
		}
		return formaPagamentoApresentar;
	}

	public void setFormaPagamentoApresentar(String formaPagamentoApresentar) {
		this.formaPagamentoApresentar = formaPagamentoApresentar;
	}

	public String getContaCorrenteApresentar() {
		if (contaCorrenteApresentar == null) {
			contaCorrenteApresentar = "";
		}
		return contaCorrenteApresentar;
	}

	public void setContaCorrenteApresentar(String contaCorrenteApresentar) {
		this.contaCorrenteApresentar = contaCorrenteApresentar;
	}
}