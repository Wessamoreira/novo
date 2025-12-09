package negocio.comuns.recursoshumanos;

import java.math.BigDecimal;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.recursoshumanos.enumeradores.TipoLancamentoFolhaPagamentoEnum;

public class ReciboFeriasEventoVO extends SuperVO {

	private static final long serialVersionUID = 7520368572039725763L;

	private Integer codigo;
	private ReciboFeriasVO recibo;
	private EventoFolhaPagamentoVO evento;
	private BigDecimal provento;
	private BigDecimal desconto;
	private BigDecimal baseCalculo;
	private String referencia;
	private Boolean informadoManual;
	private BigDecimal valorReferencia;

	// Transiente
	private Boolean itemEmEdicao;

	public Integer getCodigo() {
		if (codigo == null)
			codigo = 0;
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public ReciboFeriasVO getRecibo() {
		if (recibo == null)
			recibo = new ReciboFeriasVO();
		return recibo;
	}

	public void setRecibo(ReciboFeriasVO recibo) {
		this.recibo = recibo;
	}

	public EventoFolhaPagamentoVO getEvento() {
		if (evento == null)
			evento = new EventoFolhaPagamentoVO();
		return evento;
	}

	public void setEvento(EventoFolhaPagamentoVO evento) {
		this.evento = evento;
	}

	public BigDecimal getProvento() {
		if (provento == null)
			provento = BigDecimal.ZERO;
		return provento;
	}

	public void setProvento(BigDecimal provento) {
		this.provento = provento;
	}

	public BigDecimal getDesconto() {
		if (desconto == null)
			desconto = BigDecimal.ZERO;
		return desconto;
	}

	public void setDesconto(BigDecimal desconto) {
		this.desconto = desconto;
	}

	public BigDecimal getBaseCalculo() {
		if (baseCalculo == null)
			baseCalculo = BigDecimal.ZERO;
		return baseCalculo;
	}

	public void setBaseCalculo(BigDecimal baseCalculo) {
		this.baseCalculo = baseCalculo;
	}

	public String getReferencia() {
		if (referencia == null)
			referencia = "";
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
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

	public void montarDadosInformacoesEventoNoReciboEvento(EventoFolhaPagamentoVO evento) {
		setEvento(evento);
		setValorDoEvento(evento);
		setReferencia(evento.getReferencia());
		setCodigo(evento.getReciboFeriasEventoVO().getCodigo());
		setInformadoManual(evento.getValorInformado());
		if (evento.getValorInformado()) {
			setValorReferencia(evento.getValorTemporario());
		}
	}

	public BigDecimal getValorReferencia() {
		if (valorReferencia == null)
			valorReferencia = BigDecimal.ZERO;
		return valorReferencia;
	}

	public void setValorReferencia(BigDecimal valorReferencia) {
		this.valorReferencia = valorReferencia;
	}

	public Boolean getInformadoManual() {
		if (informadoManual == null)
			informadoManual = false;
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
	public BigDecimal getValorDoEventoTratado() {

		if (getEvento().getTipoLancamento() == null) {
			return BigDecimal.ZERO;
		}

		if (getInformadoManual() && getValorReferencia().compareTo(BigDecimal.ZERO) > 0) {
			return getValorReferencia();
		}

		switch (getEvento().getTipoLancamento()) {
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

	@Override
	public boolean equals(Object obj) {

		if (obj instanceof ReciboFeriasEventoVO) {

			if (((ReciboFeriasEventoVO) obj).getRecibo().getCodigo().equals(getRecibo().getCodigo())
					&& ((ReciboFeriasEventoVO) obj).getEvento().equals(getEvento()))
				return true;

		}
		return false;
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