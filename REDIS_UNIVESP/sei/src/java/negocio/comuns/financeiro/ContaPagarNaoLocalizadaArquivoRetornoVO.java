package negocio.comuns.financeiro;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.Uteis;

public class ContaPagarNaoLocalizadaArquivoRetornoVO extends SuperVO {

	/**
	* 
	*/
	private static final long serialVersionUID = 2940790550383313441L;
	private Integer codigo;
	private Long nossoNumero;
	private Double valorPago;
	private Date dataVcto;
	private Date dataPagamento;
	private Boolean tratada;
	private String observacao;
	

	public Date getDataPagamento() {
		return dataPagamento;
	}

	public String getDataPagamento_Apresentar() {
		if (dataPagamento != null) {
			return Uteis.getData(dataPagamento);
		}
		return null;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
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

	public Long getNossoNumero() {
		if (nossoNumero == null) {
			nossoNumero = 0L;
		}
		return nossoNumero;
	}

	public void setNossoNumero(Long nossoNumero) {
		this.nossoNumero = nossoNumero;
	}	

	public Double getValorPago() {
		if (valorPago == null) {
			valorPago = 0.0;
		}
		return valorPago;
	}

	public void setValorPago(Double valorRecebido) {
		this.valorPago = valorRecebido;
	}

	public Date getDataVcto() {
		if (dataVcto == null) {
			dataVcto = new Date();
		}
		return dataVcto;
	}

	public String getDataVcto_Apresentar() {
		return Uteis.getData(getDataVcto());
	}

	public void setDataVcto(Date dataVcto) {
		this.dataVcto = dataVcto;
	}
	

	public Boolean getTratada() {
		if (tratada == null) {
			tratada = Boolean.FALSE;
		}
		return tratada;
	}

	public void setTratada(Boolean tratada) {
		this.tratada = tratada;
	}

	public String getObservacao() {
		if (observacao == null) {
			observacao = "";
		}
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

}
