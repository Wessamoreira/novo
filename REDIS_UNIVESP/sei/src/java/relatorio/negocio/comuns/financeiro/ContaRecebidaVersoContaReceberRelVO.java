package relatorio.negocio.comuns.financeiro;

import java.util.Date;

import negocio.comuns.utilitarias.dominios.TipoOrigemContaReceber;

public class ContaRecebidaVersoContaReceberRelVO {

	private Integer contaReceber;
	private String tipoPessoa;
	private String situacao;
	private String matricula;
	private String turma;
	private Date dataVencimento;
	private Date dataRecebimento;
	private String parcela;
	private String nossoNumero;
	private String tipoOrigem;
	private String sacado;
	private Double valorDescontoCalculadoPrimeiraFaixaDescontos;
	private Double valor;
	private Double juro;
	private Double multa;
	private Double desconto;
	private Double valorRecebido;
	private Double acrescimo;

	public Integer getContaReceber() {
		return contaReceber;
	}

	public void setContaReceber(Integer contaReceber) {
		this.contaReceber = contaReceber;
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

	public String getSituacao() {
		if (situacao == null) {
			situacao = "";
		}
		return situacao;
	}

	public String getSituacaoApresentrar() {
		if (situacao == null) {
			return "";
		}
		if (situacao.equals("AR")) {
			return "A Receber";
		}
		if (situacao.equals("RE")) {
			return "Recebido";
		}
		return "";
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
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

	public String getTurma() {
		if (turma == null) {
			turma = "";
		}
		return turma;
	}

	public void setTurma(String turma) {
		this.turma = turma;
	}

	public Date getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public Date getDataRecebimento() {

		return dataRecebimento;
	}

	public void setDataRecebimento(Date dataRecebimento) {
		this.dataRecebimento = dataRecebimento;
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

	public String getNossoNumero() {
		if (nossoNumero == null) {
			nossoNumero = "";
		}
		return nossoNumero;
	}

	public void setNossoNumero(String nossoNumero) {
		this.nossoNumero = nossoNumero;
	}

	public String getTipoOrigem() {
		if (tipoOrigem == null) {
			tipoOrigem = "";
		}
		return tipoOrigem;
	}

	public String getTipoOrigemApresentar() {
		if (tipoOrigem == null || tipoOrigem.equals("")) {
			return "";
		}
		return TipoOrigemContaReceber.getDescricao(getTipoOrigem());
	}

	public void setTipoOrigem(String tipoOrigem) {
		this.tipoOrigem = tipoOrigem;
	}

	public String getSacado() {
		if (sacado == null) {
			sacado = "";
		}
		return sacado;
	}

	public void setSacado(String sacado) {
		this.sacado = sacado;
	}

	public Double getValorDescontoCalculadoPrimeiraFaixaDescontos() {
		if (valorDescontoCalculadoPrimeiraFaixaDescontos == null) {
			valorDescontoCalculadoPrimeiraFaixaDescontos = 0.0;
		}
		if (valorDescontoCalculadoPrimeiraFaixaDescontos < 0.0) {
			valorDescontoCalculadoPrimeiraFaixaDescontos = 0.0;
		}
		return valorDescontoCalculadoPrimeiraFaixaDescontos;
	}

	public void setValorDescontoCalculadoPrimeiraFaixaDescontos(Double valorDescontoCalculadoPrimeiraFaixaDescontos) {
		this.valorDescontoCalculadoPrimeiraFaixaDescontos = valorDescontoCalculadoPrimeiraFaixaDescontos;
	}

	public Double getValor() {
		if (valor == null) {
			valor = 0.0;
		}
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public Double getValorRecebido() {
		if (valorRecebido == null) {
			valorRecebido = 0.0;
		}
		if (valorRecebido < 0.0) {
			valorRecebido = 0.0;
		}
		return valorRecebido;
	}

	public void setValorRecebido(Double valorRecebido) {
		this.valorRecebido = valorRecebido;
	}

	public Double getJuro() {
		if (juro == null) {
			juro = 0.0;
		}
		return juro;
	}

	public void setJuro(Double juro) {
		this.juro = juro;
	}

	public Double getMulta() {
		if (multa == null) {
			multa = 0.0;
		}
		return multa;
	}

	public void setMulta(Double multa) {
		this.multa = multa;
	}

	public Double getDesconto() {
		if (desconto == null) {
			desconto = 0.0;
		}
		return desconto;
	}

	public void setDesconto(Double desconto) {
		this.desconto = desconto;
	}
	
	public Double getAcrescimo() {
		if (acrescimo == null) {
			acrescimo = 0.0;
		}
		return acrescimo;
	}

	public void setAcrescimo(Double acrescimo) {
		this.acrescimo = acrescimo;
	}

}
