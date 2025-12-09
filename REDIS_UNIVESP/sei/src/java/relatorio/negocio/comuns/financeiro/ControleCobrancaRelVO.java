package relatorio.negocio.comuns.financeiro;

import java.util.Date;

public class ControleCobrancaRelVO {

	private String nossoNumero;
	private String nome;
	private String curso;
	private String parcela;
	private Double valorPago;
	private Date dataCredito;
	private Date dataVencimento;
	private Double juro;
	private Double multa;
	private Double acrescimo;
	private Double desconto;
	private Double valorReceber;
	private String turma;

	public ControleCobrancaRelVO() {
		setNome("");
		setCurso("");
		setNossoNumero("");
		setParcela("");
		setValorPago(0.0);
		setJuro(0.0);
		setMulta(0.0);
		setDataCredito(new Date());
	}

	public String getCurso() {
		return curso;
	}

	public void setCurso(String curso) {
		this.curso = curso;
	}

	public String getNossoNumero() {
		return nossoNumero;
	}

	public void setNossoNumero(String nossoNumero) {
		this.nossoNumero = nossoNumero;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getParcela() {
		return parcela;
	}

	public void setParcela(String parcela) {
		this.parcela = parcela;
	}

	public Double getValorPago() {
		return valorPago;
	}

	public void setValorPago(Double valorPago) {
		this.valorPago = valorPago;
	}

	public Date getDataCredito() {
		return dataCredito;
	}

	public void setDataCredito(Date dataCredito) {
		this.dataCredito = dataCredito;
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

	public String getTurma() {
		if (turma == null) {
			turma = "";
		}
		return turma;
	}

	public void setTurma(String turma) {
		this.turma = turma;
	}
	
	public String getOrdenacao(){
		return getCurso()+" - "+getNome();
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

	public Double getValorReceber() {
		if (valorReceber == null) {
			valorReceber = 0.0;
		}
		return valorReceber;
	}

	public void setValorReceber(Double valorReceber) {
		this.valorReceber = valorReceber;
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

	public Date getDataVencimento() {
		if (dataVencimento == null) {
			dataVencimento = new Date();
		}
		return dataVencimento;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}
	
}
