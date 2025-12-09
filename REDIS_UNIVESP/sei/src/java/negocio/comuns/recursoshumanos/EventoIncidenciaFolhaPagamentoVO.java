package negocio.comuns.recursoshumanos;

import negocio.comuns.arquitetura.SuperVO;

public class EventoIncidenciaFolhaPagamentoVO extends SuperVO {
	
	private static final long serialVersionUID = 3126765402504180256L;
	
	private Integer codigo;
	private IncidenciaFolhaPagamentoVO incidencia;
	private EventoFolhaPagamentoVO evento;
	
	public Integer getCodigo() {
		if (codigo == null)
			codigo = 0;
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	public IncidenciaFolhaPagamentoVO getIncidencia() {
		if (incidencia == null)
			incidencia = new IncidenciaFolhaPagamentoVO();
		return incidencia;
	}
	public EventoFolhaPagamentoVO getEvento() {
		if (evento == null)
			evento = new EventoFolhaPagamentoVO();
		return evento;
	}
	public void setIncidencia(IncidenciaFolhaPagamentoVO incidencia) {
		this.incidencia = incidencia;
	}
	public void setEvento(EventoFolhaPagamentoVO evento) {
		this.evento = evento;
	}
	
}