package relatorio.negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

public class RecebimentoPorTurmaRelVO {

    private String tipoPessoa;
    private Date data;
    private String tipoOrigem;
    private String nrDocumento;
    private Double valor;
    private Double valorRecebido;
    private Double multa;
    private Double juro;
    private Double acrescimo;
    private Double desconto;
    private String parcela;
    private String numero;
    private String digito;
    private String nomePessoa;
    private String nomeParceiro;
    private String identificadorTurma;
    private Date dataPagamento;
    private String formaPagamento;
    private String nomeCurso;
    private Double descontoRecebido;
    private Double valorDescontoCalculadoPrimeiraFaixaDescontos;
    private String nossoNumero;
    private Double valorInstChancela;
    private Integer porcentagemInstChancela;
    private Integer quantidadeAluno;
    private Boolean valorPorAluno;
    private Boolean descontoChancela;
    private String observacao;    
    private String nomeUnidadeEnsino;
    private String instituicaoChanceladora;
    private Double valorTotalRecebido;
    private Boolean layoutComObservacao;
    private Double valordescontorecebido;
    private String nomeBanco;
    private Double valorIndiceReajustePorAtraso;
    private Double valorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa;
    
    
    private List<ContasRecebimentoRelVO> contasRecebimentoRelVOs;
    
    /**
     * ATRIBUTO TRANSIENT
     */
    private Boolean apresentarValorRecebidoComImpostosRetido;
	
	public JRDataSource getContasRecebimento() {
        return new JRBeanArrayDataSource(getContasRecebimentoRelVOs().toArray());
    }

    public RecebimentoPorTurmaRelVO() {
        setTipoPessoa("");
        setData(new Date());
        setTipoOrigem("");
        setNrDocumento("");
        setValor(0.0);
        setValorRecebido(0.0);
        setMulta(0.0);
        setJuro(0.0);
        setDesconto(0.0);
        setParcela("");
        setNumero("");
        setDigito("");
        setNomePessoa("");
        setNomeParceiro("");
        setIdentificadorTurma("");
        setFormaPagamento("");
        setNossoNumero("");
        setDescontoRecebido(0.0);
        setValorDescontoCalculadoPrimeiraFaixaDescontos(0.0);
        setNomeCurso("");
        setObservacao("");
        setValordescontorecebido(0.0);
       
    }

    public String getTipoPessoa() {
        return tipoPessoa;
    }

    public void setTipoPessoa(String tipoPessoa) {
        this.tipoPessoa = tipoPessoa;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getTipoOrigem() {
        return tipoOrigem;
    }

    public void setTipoOrigem(String tipoOrigem) {
        this.tipoOrigem = tipoOrigem;
    }

    public String getNrDocumento() {
        return nrDocumento;
    }

    public void setNrDocumento(String nrDocumento) {
        this.nrDocumento = nrDocumento;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Double getValorRecebido() {
        return valorRecebido;
    }

    public void setValorRecebido(Double valorRecebido) {
        this.valorRecebido = valorRecebido;
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

    public Double getDesconto() {
        return desconto;
    }

    public void setDesconto(Double desconto) {
        this.desconto = desconto;
    }

    public String getParcela() {
        return parcela;
    }

    public void setParcela(String parcela) {
        this.parcela = parcela;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getDigito() {
        return digito;
    }

    public void setDigito(String digito) {
        this.digito = digito;
    }

    public String getNomePessoa() {
        return nomePessoa;
    }

    public void setNomePessoa(String nomePessoa) {
        this.nomePessoa = nomePessoa;
    }

    public String getNomeParceiro() {
        return nomeParceiro;
    }

    public void setNomeParceiro(String nomeParceiro) {
        this.nomeParceiro = nomeParceiro;
    }

    public String getIdentificadorTurma() {
        return identificadorTurma;
    }

    public void setIdentificadorTurma(String identificadorTurma) {
        this.identificadorTurma = identificadorTurma;
    }

    public Date getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(Date dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public void setNomeCurso(String nomeCurso) {
        this.nomeCurso = nomeCurso;
    }

    public String getNomeCurso() {
        return nomeCurso;
    }

    public void setDescontoRecebido(Double descontoRecebido) {
        this.descontoRecebido = descontoRecebido;
    }

    public Double getDescontoRecebido() {
        if(descontoRecebido == null){
            descontoRecebido = 0.0;
        }
        return descontoRecebido;
    }

    public void setNossoNumero(String nossoNumero) {
        this.nossoNumero = nossoNumero;
    }

    public String getNossoNumero() {
        return nossoNumero;
    }

	public Double getValorInstChancela() {
		return valorInstChancela;
	}

	public void setValorInstChancela(Double valorInstChancela) {
		this.valorInstChancela = valorInstChancela;
	}

	public Integer getPorcentagemInstChancela() {
		return porcentagemInstChancela;
	}

	public void setPorcentagemInstChancela(Integer porcentagemInstChancela) {
		this.porcentagemInstChancela = porcentagemInstChancela;
	}

	public Integer getQuantidadeAluno() {
		return quantidadeAluno;
	}

	public void setQuantidadeAluno(Integer quantidadeAluno) {
		this.quantidadeAluno = quantidadeAluno;
	}

    
    public Boolean getValorPorAluno() {
        return valorPorAluno;
    }

    
    public void setValorPorAluno(Boolean valorPorAluno) {
        this.valorPorAluno = valorPorAluno;
    }

    
    public Boolean getDescontoChancela() {
        return descontoChancela;
    }

    
    public void setDescontoChancela(Boolean descontoChancela) {
        this.descontoChancela = descontoChancela;
    }

    public Double getValorDescontoCalculadoPrimeiraFaixaDescontos() {
        if(valorDescontoCalculadoPrimeiraFaixaDescontos == null){
            valorDescontoCalculadoPrimeiraFaixaDescontos = 0.0;
        }
        return valorDescontoCalculadoPrimeiraFaixaDescontos;
    }

    public void setValorDescontoCalculadoPrimeiraFaixaDescontos(Double valorDescontoCalculadoPrimeiraFaixaDescontos) {
        this.valorDescontoCalculadoPrimeiraFaixaDescontos = valorDescontoCalculadoPrimeiraFaixaDescontos;
    }

    public String getObservacao() {
        if(observacao==null){
            observacao = "";
        }
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getNomeUnidadeEnsino() {
        if (nomeUnidadeEnsino == null) {
            nomeUnidadeEnsino = "";
        }
        return nomeUnidadeEnsino;
    }

    public void setNomeUnidadeEnsino(String nomeUnidadeEnsino) {
        this.nomeUnidadeEnsino = nomeUnidadeEnsino;
    }

	public void setInstituicaoChanceladora(String instituicaoChanceladora) {
		this.instituicaoChanceladora = instituicaoChanceladora;
	}

	public String getInstituicaoChanceladora() {
		if (instituicaoChanceladora == null) {
			instituicaoChanceladora = "";
		}
		return instituicaoChanceladora;
	}

	public void setContasRecebimentoRelVOs(List<ContasRecebimentoRelVO> contasRecebimentoRelVOs) {
		this.contasRecebimentoRelVOs = contasRecebimentoRelVOs;
	}

	public List<ContasRecebimentoRelVO> getContasRecebimentoRelVOs() {
		if (contasRecebimentoRelVOs == null) {
			contasRecebimentoRelVOs = new ArrayList<ContasRecebimentoRelVO>(0);
		}
		return contasRecebimentoRelVOs;
	}

	public void setValorTotalRecebido(Double valorTotalRecebido) {
		this.valorTotalRecebido = valorTotalRecebido;
	}

	public Double getValorTotalRecebido() {
		if (valorTotalRecebido == null) {
			valorTotalRecebido = 0.0;
		}
		return valorTotalRecebido;
	}

    /**
     * @return the layoutComObservacao
     */
    public Boolean getLayoutComObservacao() {
        if(layoutComObservacao== null){
            layoutComObservacao = Boolean.FALSE;
        }
        return layoutComObservacao;
    }

    /**
     * @param layoutComObservacao the layoutComObservacao to set
     */
    public void setLayoutComObservacao(Boolean layoutComObservacao) {
        this.layoutComObservacao = layoutComObservacao;
    }

	public Double getValordescontorecebido() {
		if (valordescontorecebido == null) {
			valordescontorecebido = 0.0;
		}
		return valordescontorecebido;
	}

	public void setValordescontorecebido(Double valordescontorecebido) {
		this.valordescontorecebido = valordescontorecebido;
	}

	public String getNomeBanco() {
		return nomeBanco;
	}

	public void setNomeBanco(String nomeBanco) {
		this.nomeBanco = nomeBanco;
	}

	public Double getValorIndiceReajustePorAtraso() {
		if (valorIndiceReajustePorAtraso == null) {
			valorIndiceReajustePorAtraso = 0.0;
		}
		return valorIndiceReajustePorAtraso;
	}

	public void setValorIndiceReajustePorAtraso(Double valorIndiceReajustePorAtraso) {
		this.valorIndiceReajustePorAtraso = valorIndiceReajustePorAtraso;
	}

	public Double getValorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa() {
		if (valorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa == null) {
			valorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa = 0.0;
		}
		return valorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa;
	}

	public void setValorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa(
			Double valorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa) {
		this.valorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa = valorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa;
	}

	public Double getAcrescimo() {
		if(acrescimo == null) {
			acrescimo = 0.0;
		}
		return acrescimo;
	}

	public void setAcrescimo(Double acrescimo) {
		this.acrescimo = acrescimo;
	}

	public Boolean getApresentarValorRecebidoComImpostosRetido() {
		if(apresentarValorRecebidoComImpostosRetido == null) {
			apresentarValorRecebidoComImpostosRetido = false;
		}
		return apresentarValorRecebidoComImpostosRetido;
	}

	public void setApresentarValorRecebidoComImpostosRetido(Boolean apresentarValorRecebidoComImpostosRetido) {
		this.apresentarValorRecebidoComImpostosRetido = apresentarValorRecebidoComImpostosRetido;
	}
}
