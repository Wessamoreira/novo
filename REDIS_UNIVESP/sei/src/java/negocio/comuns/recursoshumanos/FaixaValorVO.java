package negocio.comuns.recursoshumanos;

import java.math.BigDecimal;

import negocio.comuns.arquitetura.SuperVO;

/**
 * Reponsável por manter os dados da entidade FaixaValor. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class FaixaValorVO extends SuperVO {

	private static final long serialVersionUID = 9199163541846898714L;

	private Integer codigo;
	private BigDecimal limiteSuperior;
	private BigDecimal limiteInferior;
	private BigDecimal percentual;
	private BigDecimal valorDeduzir;
	private BigDecimal valorAcrescentar;
	private ValorReferenciaFolhaPagamentoVO valorReferenciaFolhaPagamento;

	//Transiente
	private Boolean itemEmEdicao;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public BigDecimal getLimiteSuperior() {
		if (limiteSuperior == null) {
			limiteSuperior = BigDecimal.ZERO;
		}
		return limiteSuperior;
	}

	public void setLimiteSuperior(BigDecimal limiteSuperior) {
		this.limiteSuperior = limiteSuperior;
	}

	public BigDecimal getLimiteInferior() {
		if (limiteInferior == null) {
			limiteInferior = BigDecimal.ZERO;
		}
		return limiteInferior;
	}

	public void setLimiteInferior(BigDecimal limiteInferior) {
		this.limiteInferior = limiteInferior;
	}

	public BigDecimal getPercentual() {
		if (percentual == null) {
			percentual = BigDecimal.ZERO;
		}
		return percentual;
	}

	public void setPercentual(BigDecimal percentual) {
		this.percentual = percentual;
	}

	public BigDecimal getValorDeduzir() {
		if (valorDeduzir == null) {
			valorDeduzir = BigDecimal.ZERO;
		}
		return valorDeduzir;
	}

	public void setValorDeduzir(BigDecimal valorDeduzir) {
		this.valorDeduzir = valorDeduzir;
	}

	public BigDecimal getValorAcrescentar() {
		if (valorAcrescentar == null) {
			valorAcrescentar = BigDecimal.ZERO;
		}
		return valorAcrescentar;
	}

	public void setValorAcrescentar(BigDecimal valorAcrescentar) {
		this.valorAcrescentar = valorAcrescentar;
	}

	public ValorReferenciaFolhaPagamentoVO getValorReferenciaFolhaPagamento() {
		if (valorReferenciaFolhaPagamento == null) {
			valorReferenciaFolhaPagamento = new ValorReferenciaFolhaPagamentoVO();
		}
		return valorReferenciaFolhaPagamento;
	}

	public void setValorReferenciaFolhaPagamento(ValorReferenciaFolhaPagamentoVO valorReferenciaFolhaPagamento) {
		this.valorReferenciaFolhaPagamento = valorReferenciaFolhaPagamento;
	}

	public Boolean getItemEmEdicao() {
		if (itemEmEdicao == null) {
			itemEmEdicao = Boolean.FALSE;
		}
		return itemEmEdicao;
	}

	public void setItemEmEdicao(Boolean itemEmEdicao) {
		this.itemEmEdicao = itemEmEdicao;
	}

	@Override
	public String toString() {
		
		StringBuilder str = new StringBuilder("Faixa de Valores:  \n");
		
		str.append("Limite Superior=").append(getLimiteSuperior()).append(", Limite Inferior=")
		.append(getLimiteInferior()).append(", Percentual=").append(getPercentual()).append(", Valor a Deduzir=").append(getValorDeduzir())
		.append(", Valor a Acrescentar=").append(getValorAcrescentar()).append(", ")
		.append(getValorReferenciaFolhaPagamento().toString());
		
		return str.toString();
	}
}
