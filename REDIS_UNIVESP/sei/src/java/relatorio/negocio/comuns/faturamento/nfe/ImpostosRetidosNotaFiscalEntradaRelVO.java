/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.negocio.comuns.faturamento.nfe;

import java.util.Date;

/**
 * 
 * @author Ana Claudia
 */
public class ImpostosRetidosNotaFiscalEntradaRelVO {

	private String unidadeEnsino;
	private String fornecedor;
	private String imposto;
	private Double valorImposto;
	private Integer numeroNota;
	private Date dataEmissaoNota;
	private Date dataEntradaNota;
	private Date dataVencimentoNota;
	private Double porcentagemImposto;
	private Double valorNota;

	public ImpostosRetidosNotaFiscalEntradaRelVO() {
		setUnidadeEnsino("");
		setFornecedor("");
		setImposto("");
		setValorImposto(0.0);
		setNumeroNota(0);
		setDataEmissaoNota(new Date());
		setDataEntradaNota(new Date());
		setDataVencimentoNota(new Date());
		setPorcentagemImposto(0.0);
		setValorNota(0.0);		
	}

	public String getUnidadeEnsino() {
		return unidadeEnsino;
	}

	public void setUnidadeEnsino(String unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}

	public String getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(String fornecedor) {
		this.fornecedor = fornecedor;
	}

	public String getImposto() {
		return imposto;
	}

	public void setImposto(String imposto) {
		this.imposto = imposto;
	}

	public Double getValorImposto() {
		return valorImposto;
	}

	public void setValorImposto(Double valorImposto) {
		this.valorImposto = valorImposto;
	}

	public Integer getNumeroNota() {
		return numeroNota;
	}

	public void setNumeroNota(Integer numeroNota) {
		this.numeroNota = numeroNota;
	}

	public Date getDataEmissaoNota() {
		return dataEmissaoNota;
	}

	public void setDataEmissaoNota(Date dataEmissaoNota) {
		this.dataEmissaoNota = dataEmissaoNota;
	}

	public Date getDataEntradaNota() {
		return dataEntradaNota;
	}
	
	public void setDataEntradaNota(Date dataEntradaNota) {
		this.dataEntradaNota = dataEntradaNota;
	}
	
	public Date getDataVencimentoNota() {
		return dataVencimentoNota;
	}

	public void setDataVencimentoNota(Date dataVencimentoNota) {
		this.dataVencimentoNota = dataVencimentoNota;
	}
	
	public Double getPorcentagemImposto() {
		return porcentagemImposto;
	}
	
	public void setPorcentagemImposto(Double porcentagemImposto) {
		this.porcentagemImposto = porcentagemImposto;
	}

	public Double getValorNota() {
		return valorNota;
	}

	public void setValorNota(Double valorNota) {
		this.valorNota = valorNota;
	}
	
}