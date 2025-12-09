/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.negocio.comuns.financeiro;

import java.util.Date;

/**
 * 
 * @author Ana Claudia
 */
public class AdiantamentoRelVO {

	private Integer codigoContaPagarUtilizada;
	private Integer codigoContaPagar;
	private String tipoSacado;
	private String nomeSacado;
	private String unidadeEnsino;
	private Date dataLancamento;
	private Date dataPagamento;
	private String situacaoContaPagar;
	private String situacaoAdiantamento;
	private String numeroDocumento;
	private String numeroNotaFiscalEntrada;	
	private Double valorDocumento;
	private Double valorUtilizado;
	private Double valorTotal;
	private Double valorTotalUtilizado;
	private Double valorSaldo;

	public AdiantamentoRelVO() {
		setTipoSacado("");
		setNomeSacado("");
		setUnidadeEnsino("");
		setSituacaoContaPagar("");
		setSituacaoAdiantamento("");
		setNumeroDocumento("");
		setNumeroNotaFiscalEntrada("");
		setValorDocumento(0.0);
		setValorUtilizado(0.0);
		setValorTotal(0.0);
		setValorTotalUtilizado(0.0);
		setValorSaldo(0.0);
	}

	public Integer getCodigoContaPagar() {
		return codigoContaPagar;
	}

	public void setCodigoContaPagar(Integer codigoContaPagar) {
		this.codigoContaPagar = codigoContaPagar;
	}

	public String getTipoSacado() {
		return tipoSacado;
	}

	public void setTipoSacado(String tipoSacado) {
		this.tipoSacado = tipoSacado;
	}

	public String getNomeSacado() {
		return nomeSacado;
	}

	public void setNomeSacado(String nomeSacado) {
		this.nomeSacado = nomeSacado;
	}


	public String getUnidadeEnsino() {
		return unidadeEnsino;
	}

	public void setUnidadeEnsino(String unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}
	
	public Date getDataLancamento() {
		return dataLancamento;
	}

	public void setDataLancamento(Date dataLancamento) {
		this.dataLancamento = dataLancamento;
	}

	public Date getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public String getSituacaoContaPagar() {
		return situacaoContaPagar;
	}

	public void setSituacaoContaPagar(String situacaoContaPagar) {
		this.situacaoContaPagar = situacaoContaPagar;
	}

	public String getSituacaoAdiantamento() {
		return situacaoAdiantamento;
	}

	public void setSituacaoAdiantamento(String situacaoAdiantamento) {
		this.situacaoAdiantamento = situacaoAdiantamento;
	}

	public String getNumeroDocumento() {
		return numeroDocumento;
	}

	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	public Double getValorDocumento() {
		return valorDocumento;
	}

	public String getNumeroNotaFiscalEntrada() {
		return numeroNotaFiscalEntrada;
	}

	public void setNumeroNotaFiscalEntrada(String numeroNotaFiscalEntrada) {
		this.numeroNotaFiscalEntrada = numeroNotaFiscalEntrada;
	}

	public void setValorDocumento(Double valorDocumento) {
		this.valorDocumento = valorDocumento;
	}

	public Double getValorUtilizado() {
		return valorUtilizado;
	}

	public void setValorUtilizado(Double valorUtilizado) {
		this.valorUtilizado = valorUtilizado;
	}

	public Double getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(Double valorTotal) {
		this.valorTotal = valorTotal;
	}

	public Double getValorTotalUtilizado() {
		return valorTotalUtilizado;
	}

	public void setValorTotalUtilizado(Double valorTotalUtilizado) {
		this.valorTotalUtilizado = valorTotalUtilizado;
	}

	public Double getValorSaldo() {
		return valorSaldo;
	}

	public void setValorSaldo(Double valorSaldo) {
		this.valorSaldo = valorSaldo;
	}

	public Integer getCodigoContaPagarUtilizada() {
		return codigoContaPagarUtilizada;
	}

	public void setCodigoContaPagarUtilizada(Integer codigoContaPagarUtilizada) {
		this.codigoContaPagarUtilizada = codigoContaPagarUtilizada;
	}
	
}