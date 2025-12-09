/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.negocio.comuns.financeiro;

import java.util.Date;

/**
 * 
 * @author Thyago
 */
public class CartaoCreditoDebitoRelVO {

    private String unidadeEnsino;
    private String bandeira;
    private String tipoCartao;
    private String operador;
    private String sacado;
    private Date dataRecebimento;
    private Date dataPrevRecebimento;
    private String parcela;
    private Double valor;
    private String situacao;
    
    // Campos para relatorio SINTETICO
    private Double credCompensado;
    private Double credCompensar;
    private Double debito;
    
    private String numeroCartaoCredito;
    private String numeroReciboTransacao;

	public CartaoCreditoDebitoRelVO() {
    }

	public String getUnidadeEnsino() {
		if (unidadeEnsino == null) {
			unidadeEnsino = "";
		}
		return unidadeEnsino;
	}

	public void setUnidadeEnsino(String unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}

	public String getOperador() {
		if (operador == null) {
			operador = "";
		}
		return operador;
	}

	public void setOperador(String operador) {
		this.operador = operador;
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

	public Date getDataRecebimento() {
		if (dataRecebimento == null) {
			dataRecebimento = new Date();
		}
		return dataRecebimento;
	}

	public void setDataRecebimento(Date dataRecebimento) {
		this.dataRecebimento = dataRecebimento;
	}

	public Date getDataPrevRecebimento() {
		if (dataPrevRecebimento == null) {
			dataPrevRecebimento = new Date();
		}
		return dataPrevRecebimento;
	}

	public void setDataPrevRecebimento(Date dataPrevRecebimento) {
		this.dataPrevRecebimento = dataPrevRecebimento;
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

	public Double getValor() {
		if (valor == null) {
			valor = 0.0;
		}
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

	public String getBandeira() {
		if (bandeira == null) {
			bandeira = "";
		}
		return bandeira;
	}

	public void setBandeira(String bandeira) {
		this.bandeira = bandeira;
	}

	public String getTipoCartao() {
		if (tipoCartao == null) {
			tipoCartao = "";
		}
		return tipoCartao;
	}

	public void setTipoCartao(String tipoCartao) {
		this.tipoCartao = tipoCartao;
	}

	public Double getCredCompensado() {
		if (credCompensado == null) {
			credCompensado = 0.0;
		}
		return credCompensado;
	}

	public void setCredCompensado(Double credCompensado) {
		this.credCompensado = credCompensado;
	}

	public Double getCredCompensar() {
		if (credCompensar == null) {
			credCompensar = 0.0;
		}
		return credCompensar;
	}

	public void setCredCompensar(Double credCompensar) {
		this.credCompensar = credCompensar;
	}

	public Double getDebito() {
		if (debito == null) {
			debito = 0.0;
		}
		return debito;
	}

	public void setDebito(Double debito) {
		this.debito = debito;
	}

	public String getNumeroCartaoCredito() {
		if (numeroCartaoCredito == null) {
			numeroCartaoCredito = "";
		}
		return numeroCartaoCredito;
	}

	public void setNumeroCartaoCredito(String numeroCartaoCredito) {
		this.numeroCartaoCredito = numeroCartaoCredito;
	}

	public String getNumeroReciboTransacao() {
		if (numeroReciboTransacao == null) {
			numeroReciboTransacao = "";
		}
		return numeroReciboTransacao;
	}

	public void setNumeroReciboTransacao(String numeroReciboTransacao) {
		this.numeroReciboTransacao = numeroReciboTransacao;
	}
}
