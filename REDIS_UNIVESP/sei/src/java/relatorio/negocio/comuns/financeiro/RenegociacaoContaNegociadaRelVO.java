package relatorio.negocio.comuns.financeiro;

import java.util.Date;

import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoContaReceber;

/**
 *
 * @author Carlos
 */
public class RenegociacaoContaNegociadaRelVO {

	private String parcela;
	private Double valor;
	private Double valorNegociado;
	private Double multa;
	private Double juro;
	private Date dataVencimento;
	private String nossoNumero;
	private String situacao;
	private Double acrescimo;
	private Double valorDesconto;
	private String nomePessoa;
	private Date dataCompetencia;
	private String centroReceita;
	private Double valorReceberCalculado;
	private Double valorDescontoConvenio;

	public Double getAcrescimo() {
		if (acrescimo == null) {
			acrescimo = 0.0;
		}
		return acrescimo;
	}

	/**
	 * @param acrescimo
	 *            the acrescimo to set
	 */
	public void setAcrescimo(Double acrescimo) {
		this.acrescimo = acrescimo;
	}

	public Double getValorDesconto() {
		if (valorDesconto == null) {
			valorDesconto = 0.0;
		}
		return (valorDesconto);
	}

	public void setValorDesconto(Double valorDesconto) {
		this.valorDesconto = Uteis.arrendondarForcando2CadasDecimais(valorDesconto);
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

	public Date getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
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

	public String getDataVencimento_Apresentar() {
		if (dataVencimento == null) {
			return "";
		}
		return (Uteis.getData(dataVencimento));
	}

	public Double getValorNegociado() {
		if (valorNegociado == null) {
			valorNegociado = 0.0;
		}
		return valorNegociado;
	}

	public void setValorNegociado(Double valorNegociado) {
		this.valorNegociado = valorNegociado;
	}

	public Double getJuro() {
		if (juro == null) {
			juro = 0.0;
		}
		return (juro);
	}

	public void setJuro(Double juro) {
		this.juro = juro;
	}

	public Double getMulta() {
		if (multa == null) {
			multa = 0.0;
		}
		return (multa);
	}

	public void setMulta(Double multa) {
		this.multa = multa;
	}

	public String getNomePessoa() {
		if (nomePessoa == null) {
			nomePessoa = "";
		}
		return nomePessoa;
	}

	public void setNomePessoa(String nomePessoa) {
		this.nomePessoa = nomePessoa;
	}

	public Date getDataCompetencia() {
		return dataCompetencia;
	}

	public void setDataCompetencia(Date dataCompetencia) {
		this.dataCompetencia = dataCompetencia;
	}

	public String getCentroReceita() {
		if (centroReceita == null) {
			centroReceita = "";
		}
		return centroReceita;
	}

	public void setCentroReceita(String centroReceita) {
		this.centroReceita = centroReceita;
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

	public Double getValorDescontoConvenio() {
		if (valorDescontoConvenio == null) {
			valorDescontoConvenio = 0.0;
		}
		return valorDescontoConvenio;
	}

	public void setValorDescontoConvenio(Double valorDescontoConvenio) {
		this.valorDescontoConvenio = valorDescontoConvenio;
	}

}
