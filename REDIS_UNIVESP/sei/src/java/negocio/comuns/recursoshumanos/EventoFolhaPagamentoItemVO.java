package negocio.comuns.recursoshumanos;

import negocio.comuns.arquitetura.SuperVO;

public class EventoFolhaPagamentoItemVO extends SuperVO {

	private static final long serialVersionUID = -8599524770948702950L;

	private Integer codigo;
	private EventoFolhaPagamentoVO eventoFolhaPagamentoItem;
	private EventoFolhaPagamentoVO eventoFolhaPagamento;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public EventoFolhaPagamentoVO getEventoFolhaPagamentoItem() {
		if (eventoFolhaPagamentoItem == null) {
			eventoFolhaPagamentoItem = new EventoFolhaPagamentoVO();
		}
		return eventoFolhaPagamentoItem;
	}

	public void setEventoFolhaPagamentoItem(EventoFolhaPagamentoVO eventoFolha) {
		this.eventoFolhaPagamentoItem = eventoFolha;
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

}
