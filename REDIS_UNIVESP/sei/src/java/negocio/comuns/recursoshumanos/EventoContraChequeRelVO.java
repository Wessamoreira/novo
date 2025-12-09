package negocio.comuns.recursoshumanos;

import java.math.BigDecimal;

import negocio.comuns.arquitetura.SuperVO;

/**
 * Reponsável por manter os dados da entidade ContraCheque. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class EventoContraChequeRelVO extends SuperVO {

	private static final long serialVersionUID = -6771318146607691440L;
	
	private String identificadorEvento;
	private String nomeEvento;
	private String referencia;
	private BigDecimal valor;
	private BigDecimal provento;
	private BigDecimal desconto;
	
	public String getIdentificadorEvento() {
		if (identificadorEvento == null)
			identificadorEvento = "";
		return identificadorEvento;
	}
	public String getNomeEvento() {
		if (nomeEvento == null)
			nomeEvento = "";
		return nomeEvento;
	}
	public String getReferencia() {
		if (referencia == null)
			referencia = "";
		return referencia;
	}
	public BigDecimal getValor() {
		if (valor == null)
			valor = BigDecimal.ZERO;
		return valor;
	}
	public void setIdentificadorEvento(String identificadorEvento) {
		this.identificadorEvento = identificadorEvento;
	}
	public void setNomeEvento(String nomeEvento) {
		this.nomeEvento = nomeEvento;
	}
	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	public BigDecimal getProvento() {
		if (provento == null)
			provento = BigDecimal.ZERO;
		return provento;
	}
	public BigDecimal getDesconto() {
		if (desconto == null)
			desconto = BigDecimal.ZERO;
		return desconto;
	}
	public void setProvento(BigDecimal provento) {
		this.provento = provento;
	}
	public void setDesconto(BigDecimal desconto) {
		this.desconto = desconto;
	}
}