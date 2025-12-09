package negocio.comuns.ead;

import negocio.comuns.academico.TemaAssuntoVO;
import negocio.comuns.arquitetura.SuperVO;

public class AvaliacaoOnlineTemaAssuntoVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1567044624615414513L;
	private Integer codigo;
	private AvaliacaoOnlineVO avaliacaoOnlineVO;
	private TemaAssuntoVO temaAssuntoVO;
	private Boolean selecionado;
	
	public Integer getCodigo() {
		if(codigo == null) {
			codigo = 0;
		}
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	public AvaliacaoOnlineVO getAvaliacaoOnlineVO() {
		if(avaliacaoOnlineVO == null) {
			avaliacaoOnlineVO = new AvaliacaoOnlineVO();
		}
		return avaliacaoOnlineVO;
	}
	public void setAvaliacaoOnlineVO(AvaliacaoOnlineVO avaliacaoOnlineVO) {
		this.avaliacaoOnlineVO = avaliacaoOnlineVO;
	}
	public TemaAssuntoVO getTemaAssuntoVO() {
		if(temaAssuntoVO == null) {
			temaAssuntoVO = new TemaAssuntoVO();
		}
		return temaAssuntoVO;
	}
	public void setTemaAssuntoVO(TemaAssuntoVO temaAssuntoVO) {
		this.temaAssuntoVO = temaAssuntoVO;
	}
	
	public Boolean getSelecionado() {
		if(selecionado == null) {
			selecionado =  false;
		}
		return selecionado;
	}
	public void setSelecionado(Boolean selecionado) {
		this.selecionado = selecionado;
	}
	
	
	
}
