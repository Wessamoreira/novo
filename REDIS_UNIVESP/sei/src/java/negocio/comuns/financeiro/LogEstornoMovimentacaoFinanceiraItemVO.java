package negocio.comuns.financeiro;

import java.util.Date;

public class LogEstornoMovimentacaoFinanceiraItemVO {

	private Integer codigo;
	private Double valor;
	private Integer responsavel;
	private Date dataEstorno;
	private Integer codigoContaOrigem;
	private Integer codigoContaDestino;
	private String descricaoContaOrigem;
	private String descricaoContaDestino;
	private String formaPagamento;
	private Integer codigoCheque;
	
	public Double getValor() {
		if (valor == null) {
			valor = 0.0;
		}
		return valor;
	}
	
	public void setValor(Double valor) {
		this.valor = valor;
	}
	
	public Integer getResponsavel() {
		if (responsavel == null) {
			responsavel = 0;
		}
		return responsavel;
	}
	
	public void setResponsavel(Integer responsavel) {
		this.responsavel = responsavel;
	}
	
	public Date getDataEstorno() {
		if (dataEstorno == null) {
			dataEstorno = new Date();
		}
		return dataEstorno;
	}
	
	public void setDataEstorno(Date dataEstorno) {
		this.dataEstorno = dataEstorno;
	}
	
	public Integer getCodigoContaOrigem() {
		if (codigoContaOrigem == null) {
			codigoContaOrigem = 0;
		}
		return codigoContaOrigem;
	}
	
	public void setCodigoContaOrigem(Integer codigoContaOrigem) {
		this.codigoContaOrigem = codigoContaOrigem;
	}
	
	public Integer getCodigoContaDestino() {
		if (codigoContaDestino == null) {
			codigoContaDestino = 0;
		}
		return codigoContaDestino;
	}
	
	public void setCodigoContaDestino(Integer codigoContaDestino) {
		this.codigoContaDestino = codigoContaDestino;
	}
	
	public String getDescricaoContaOrigem() {
		if (descricaoContaOrigem == null) {
			descricaoContaOrigem = "";
		}
		return descricaoContaOrigem;
	}
	
	public void setDescricaoContaOrigem(String descricaoContaOrigem) {
		this.descricaoContaOrigem = descricaoContaOrigem;
	}
	
	public String getDescricaoContaDestino() {
		if (descricaoContaDestino == null) {
			descricaoContaDestino = "";
		}
		return descricaoContaDestino;
	}
	
	public void setDescricaoContaDestino(String descricaoContaDestino) {
		this.descricaoContaDestino = descricaoContaDestino;
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

	public Integer getCodigoCheque() {
		if (codigoCheque == null) { 
			codigoCheque = 0;
		}
		return codigoCheque;
	}

	public void setCodigoCheque(Integer codigoCheque) {
		this.codigoCheque = codigoCheque;
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
	
	
}
