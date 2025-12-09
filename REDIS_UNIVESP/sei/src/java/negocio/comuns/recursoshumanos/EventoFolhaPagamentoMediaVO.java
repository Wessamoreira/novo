package negocio.comuns.recursoshumanos;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.recursoshumanos.enumeradores.TipoEventoMediaEnum;

public class EventoFolhaPagamentoMediaVO extends SuperVO {

	private static final long serialVersionUID = -7756598963136274887L;

	private Integer codigo;
	private String grupo;
	private TipoEventoMediaEnum tipoEventoMediaEnum;
	private EventoFolhaPagamentoVO eventoFolhaPagamentoVO;
	
	public Integer getCodigo() {
		if(codigo == null)
			codigo = 0;
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getGrupo() {
		if(grupo == null)
			grupo = "";
		return grupo;
	}

	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}

	public TipoEventoMediaEnum getTipoEventoMediaEnum() {
		if(tipoEventoMediaEnum == null)
			tipoEventoMediaEnum = TipoEventoMediaEnum.INCIDE_FERIAS;
		return tipoEventoMediaEnum;
	}

	public void setTipoEventoMediaEnum(TipoEventoMediaEnum tipoEventoMediaEnum) {
		this.tipoEventoMediaEnum = tipoEventoMediaEnum;
	}

	public EventoFolhaPagamentoVO getEventoFolhaPagamentoVO() {
		if(eventoFolhaPagamentoVO == null)
			eventoFolhaPagamentoVO = new EventoFolhaPagamentoVO();
		return eventoFolhaPagamentoVO;
	}

	public void setEventoFolhaPagamentoVO(EventoFolhaPagamentoVO eventoFolhaPagamentoVO) {
		this.eventoFolhaPagamentoVO = eventoFolhaPagamentoVO;
	}
	
	public String getTipoMediaApresentar() {
		return getTipoEventoMediaEnum().getValorApresentar();
	}
}