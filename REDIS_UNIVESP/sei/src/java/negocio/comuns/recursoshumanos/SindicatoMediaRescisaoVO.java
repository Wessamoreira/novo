package negocio.comuns.recursoshumanos;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.recursoshumanos.enumeradores.TipoEventoMediaRescisaoEnum;

public class SindicatoMediaRescisaoVO extends SuperVO {

	private static final long serialVersionUID = -8695398979806086934L;

	private Integer codigo;
	private String grupo;
	private EventoFolhaPagamentoVO eventoFolhaPagamento;
	private SindicatoVO sindicato;
	private TipoEventoMediaRescisaoEnum tipoEventoMediaRescisao;

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

	public String getGrupo() {
		if (grupo == null) {
			grupo = "";
		}
		return grupo;
	}

	public void setGrupo(String grupo) {
		this.grupo = grupo;
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

	public SindicatoVO getSindicato() {
		if (sindicato == null) {
			sindicato = new SindicatoVO();
		}
		return sindicato;
	}

	public void setSindicato(SindicatoVO sindicato) {
		this.sindicato = sindicato;
	}

	public TipoEventoMediaRescisaoEnum getTipoEventoMediaRescisao() {
		return tipoEventoMediaRescisao;
	}

	public void setTipoEventoMediaRescisao(TipoEventoMediaRescisaoEnum tipoEventoMediaRescisao) {
		this.tipoEventoMediaRescisao = tipoEventoMediaRescisao;
	}

	public Boolean getItemEmEdicao() {
		if (itemEmEdicao == null) {
			itemEmEdicao = false;
		}
		return itemEmEdicao;
	}

	public void setItemEmEdicao(Boolean itemEmEdicao) {
		this.itemEmEdicao = itemEmEdicao;
	}
}
