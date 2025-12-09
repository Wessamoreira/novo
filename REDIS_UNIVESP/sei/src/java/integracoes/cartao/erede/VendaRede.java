package integracoes.cartao.erede;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.financeiro.FormaPagamentoNegociacaoRecebimentoVO;

public class VendaRede {
	
	private String requisicao;
	private String resposta;
	private String requisicaoCancelamento;
	private String respostaCancelamento;
	private Double valor;
	private Integer parcelas;
	private String referencia;
	private String tipoCartao;
	private String codigoAutorizacao;
	private String tid;
	private List<FormaPagamentoNegociacaoRecebimentoVO> formaPagamentoNegociacaoRecebimentoVOs;

	public Double getValor() {
		if (valor == null) {
			valor = 0.0;
		}
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public Integer getParcelas() {
		if (parcelas == null) {
			parcelas = 1;
		}
		return parcelas;
	}

	public void setParcelas(Integer parcelas) {
		this.parcelas = parcelas;
	}

	public String getReferencia() {
		if (referencia == null) {
			referencia = "";
		}
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
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

	public String getRequisicao() {
		if (requisicao == null) {
			requisicao = "";
		}
		return requisicao;
	}

	public void setRequisicao(String requisicao) {
		this.requisicao = requisicao;
	}

	public String getResposta() {
		if (resposta == null) {
			resposta = "";
		}
		return resposta;
	}

	public void setResposta(String resposta) {
		this.resposta = resposta;
	}

	public String getTid() {
		if (tid == null) {
			tid = "";
		}
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getCodigoAutorizacao() {
		if (codigoAutorizacao == null) {
			codigoAutorizacao = "";
		}
		return codigoAutorizacao;
	}

	public void setCodigoAutorizacao(String codigoAutorizacao) {
		this.codigoAutorizacao = codigoAutorizacao;
	}

	public List<FormaPagamentoNegociacaoRecebimentoVO> getFormaPagamentoNegociacaoRecebimentoVOs() {
		if (formaPagamentoNegociacaoRecebimentoVOs == null) {
			formaPagamentoNegociacaoRecebimentoVOs = new ArrayList<FormaPagamentoNegociacaoRecebimentoVO>();
		}
		return formaPagamentoNegociacaoRecebimentoVOs;
	}

	public void setFormaPagamentoNegociacaoRecebimentoVOs(List<FormaPagamentoNegociacaoRecebimentoVO> formaPagamentoNegociacaoRecebimentoVOs) {
		this.formaPagamentoNegociacaoRecebimentoVOs = formaPagamentoNegociacaoRecebimentoVOs;
	}

	public String getRequisicaoCancelamento() {
		if (requisicaoCancelamento == null) {
			requisicaoCancelamento = "";
		}
		return requisicaoCancelamento;
	}

	public void setRequisicaoCancelamento(String requisicaoCancelamento) {
		this.requisicaoCancelamento = requisicaoCancelamento;
	}

	public String getRespostaCancelamento() {
		if (respostaCancelamento == null) {
			respostaCancelamento = "";
		}
		return respostaCancelamento;
	}

	public void setRespostaCancelamento(String respostaCancelamento) {
		this.respostaCancelamento = respostaCancelamento;
	}

}
