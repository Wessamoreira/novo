package relatorio.negocio.comuns.financeiro;

import negocio.comuns.arquitetura.SuperVO;

public class AutorizacaoPagamentoRelVO extends SuperVO {

	private static final long serialVersionUID = 1L;
	private String unidadeEnsino;
	private String dataVencimento;
	private Double valor;
	private Double juro;
	private Double multa;
	private Double desconto;
	private Double valorTotal;
	private String valorPorExtenso;
	private String favorecido;
	private String dataRegistro;
	private String parcela;
	private String nrDocumento;
	private String descricao;
	private String categoriaDespesa;
	private String centroCusto;
	private String responsavelLancamento;
	
	public String getUnidadeEnsino() {
		if (unidadeEnsino == null) {
			unidadeEnsino = "";
		}
		return unidadeEnsino;
	}
	public void setUnidadeEnsino(String unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}
	public String getDataVencimento() {
		if (dataVencimento == null) {
			dataVencimento = "";
		}
		return dataVencimento;
	}
	public void setDataVencimento(String dataVencimento) {
		this.dataVencimento = dataVencimento;
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
	public Double getValorTotal() {
		if (valorTotal == null) {
			valorTotal = 0.0;
		}
		return valorTotal;
	}
	public void setValorTotal(Double valorTotal) {
		this.valorTotal = valorTotal;
	}
	public String getValorPorExtenso() {
		if (valorPorExtenso == null) {
			valorPorExtenso = "";
		}
		return valorPorExtenso;
	}
	public void setValorPorExtenso(String valorPorExtenso) {
		this.valorPorExtenso = valorPorExtenso;
	}
	public String getFavorecido() {
		if (favorecido == null) {
			favorecido = "";
		}
		return favorecido;
	}
	public void setFavorecido(String favorecido) {
		this.favorecido = favorecido;
	}
	public String getDataRegistro() {
		if (dataRegistro == null) {
			dataRegistro = "";
		}
		return dataRegistro;
	}
	public void setDataRegistro(String dataRegistro) {
		this.dataRegistro = dataRegistro;
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
	public String getNrDocumento() {
		if (nrDocumento == null) {
			nrDocumento = "";
		}
		return nrDocumento;
	}
	public void setNrDocumento(String nrDocumento) {
		this.nrDocumento = nrDocumento;
	}
	public String getDescricao() {
		if (descricao == null) {
			descricao = "";
		}
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public String getCategoriaDespesa() {
		if (categoriaDespesa == null) {
			categoriaDespesa = "";
		}
		return categoriaDespesa;
	}
	public void setCategoriaDespesa(String categoriaDespesa) {
		this.categoriaDespesa = categoriaDespesa;
	}
	public String getCentroCusto() {
		if (centroCusto == null) {
			centroCusto = "";
		}
		return centroCusto;
	}
	public void setCentroCusto(String centroCusto) {
		this.centroCusto = centroCusto;
	}
	public String getResponsavelLancamento() {
		if (responsavelLancamento == null) {
			responsavelLancamento = "";
		}
		return responsavelLancamento;
	}
	public void setResponsavelLancamento(String responsavelLancamento) {
		this.responsavelLancamento = responsavelLancamento;
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
	
	

}
 