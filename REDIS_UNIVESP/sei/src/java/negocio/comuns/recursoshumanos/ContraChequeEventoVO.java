package negocio.comuns.recursoshumanos;

import java.math.BigDecimal;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.recursoshumanos.enumeradores.TipoLancamentoFolhaPagamentoEnum;

public class ContraChequeEventoVO extends SuperVO {

	private static final long serialVersionUID = 3267980926338772360L;

	private Integer codigo;
	private EventoFolhaPagamentoVO eventoFolhaPagamento;
	private ContraChequeVO contraCheque;
	private BigDecimal valorReferencia;
	private BigDecimal provento;
	private BigDecimal desconto;
	private BigDecimal baseCalculo;
	private String referencia;
	private Boolean informadoManual;
	private Boolean valorInformado;

	// Sprint 6
	private CompetenciaPeriodoFolhaPagamentoVO periodo;

	// Transiente
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

	public EventoFolhaPagamentoVO getEventoFolhaPagamento() {
		if (eventoFolhaPagamento == null) {
			eventoFolhaPagamento = new EventoFolhaPagamentoVO();
		}
		return eventoFolhaPagamento;
	}

	public void setEventoFolhaPagamento(EventoFolhaPagamentoVO eventoFolhaPagamento) {
		this.eventoFolhaPagamento = eventoFolhaPagamento;
	}

	public ContraChequeVO getContraCheque() {
		if (contraCheque == null) {
			contraCheque = new ContraChequeVO();
		}
		return contraCheque;
	}

	public void setContraCheque(ContraChequeVO contraCheque) {
		this.contraCheque = contraCheque;
	}

	public BigDecimal getValorReferencia() {
		if (valorReferencia == null) {
			valorReferencia = BigDecimal.ZERO;
		}
		return valorReferencia;
	}

	public void setValorReferencia(BigDecimal valorReferencia) {
		this.valorReferencia = valorReferencia;
	}

	public BigDecimal getProvento() {
		if (provento == null)
			provento = new BigDecimal(0);
		return provento;
	}

	public void setProvento(BigDecimal provento) {
		this.provento = provento;
	}

	public BigDecimal getDesconto() {
		if (desconto == null)
			desconto = new BigDecimal(0);
		return desconto;
	}

	public void setDesconto(BigDecimal desconto) {
		this.desconto = desconto;
	}

	public String getReferencia() {
		if (referencia == null)
			referencia = "";
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public Boolean getInformadoManual() {
		if (informadoManual == null) {
			informadoManual = Boolean.FALSE;
		}
		return informadoManual;
	}

	public void setInformadoManual(Boolean informadoManual) {
		this.informadoManual = informadoManual;
	}

	/**
	 * Retorna o valor do evento, variando se o mesmo e PROVENTO, DESCONTO ou BASE
	 * CALCULO
	 * 
	 * @return
	 */
	public BigDecimal recuperarValorDoEventoTratado() {

		if (getEventoFolhaPagamento().getTipoLancamento() == null) {
			return BigDecimal.ZERO;
		}

		if (getInformadoManual() && getValorReferencia().compareTo(BigDecimal.ZERO) > 0) {
			return getValorReferencia();
		}

		switch (getEventoFolhaPagamento().getTipoLancamento()) {
		case PROVENTO:
			return getProvento();
		case DESCONTO:
			return getDesconto();
		case BASE_CALCULO:
			return getBaseCalculo();
		default:
			return getValorReferencia();
		}
	}

	public BigDecimal getBaseCalculo() {
		if (baseCalculo == null)
			baseCalculo = BigDecimal.ZERO;
		return baseCalculo;
	}

	public void setBaseCalculo(BigDecimal baseCalculo) {
		this.baseCalculo = baseCalculo;
	}

	/**
	 * Seta o valor do evento caso seja Provento, Desconto, Base Calculo
	 * 
	 * @param evento
	 * @param valorResultadoFormula
	 */
	public void setValorDoEvento(EventoFolhaPagamentoVO evento) {
		BigDecimal valorResultado = evento.getValorTemporario();

		switch (TipoLancamentoFolhaPagamentoEnum.valueOf(evento.getTipoLancamento().name())) {
		case PROVENTO:
			setProvento(valorResultado);
			break;
		case DESCONTO:
			setDesconto(valorResultado);
			break;
		case BASE_CALCULO:
			setBaseCalculo(valorResultado);
			break;
		default:
			break;
		}
	}

	public CompetenciaPeriodoFolhaPagamentoVO getPeriodo() {
		if (periodo == null)
			periodo = new CompetenciaPeriodoFolhaPagamentoVO();
		return periodo;
	}

	public void setPeriodo(CompetenciaPeriodoFolhaPagamentoVO periodo) {
		this.periodo = periodo;
	}

	public Boolean getValorInformado() {
		if (valorInformado == null)
			valorInformado = false;
		return valorInformado;
	}

	public void setValorInformado(Boolean valorInformado) {
		this.valorInformado = valorInformado;
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
}