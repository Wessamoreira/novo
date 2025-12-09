package negocio.comuns.recursoshumanos;

import negocio.comuns.arquitetura.SuperVO;

public class SindicatoMedia13VO extends SuperVO {

	private static final long serialVersionUID = 5772594046690437836L;
	
	private Integer codigo;
	private String grupo;
	private EventoFolhaPagamentoVO eventoMediaVO;
	private SindicatoVO sindicatoVO;
	
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
	
	public String getGrupo() {
		if (grupo == null)
			grupo = "";
		return grupo;
	}
	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}
	
	public EventoFolhaPagamentoVO getEventoMediaVO() {
		if (eventoMediaVO == null)
			eventoMediaVO = new EventoFolhaPagamentoVO();
		return eventoMediaVO;
	}
	public void setEventoMediaVO(EventoFolhaPagamentoVO eventoMediaVO) {
		this.eventoMediaVO = eventoMediaVO;
	}
	
	public SindicatoVO getSindicatoVO() {
		if (sindicatoVO == null)
			sindicatoVO = new SindicatoVO();
		return sindicatoVO;
	}
	public void setSindicatoVO(SindicatoVO sindicatoVO) {
		this.sindicatoVO = sindicatoVO;
	}
	public Boolean getItemEmEdicao() {
		if (itemEmEdicao == null)
			itemEmEdicao = false;
		return itemEmEdicao;
	}
	public void setItemEmEdicao(Boolean itemEmEdicao) {
		this.itemEmEdicao = itemEmEdicao;
	}
}