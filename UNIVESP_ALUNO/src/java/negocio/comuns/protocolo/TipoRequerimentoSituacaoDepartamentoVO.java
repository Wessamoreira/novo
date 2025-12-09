package negocio.comuns.protocolo;

import negocio.comuns.arquitetura.SuperVO;

public class TipoRequerimentoSituacaoDepartamentoVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 681303386731141593L;
	private Integer codigo;
	private SituacaoRequerimentoDepartamentoVO situacaoRequerimentoDepartamentoVO;
	private TipoRequerimentoDepartamentoVO tipoRequerimentoDepartamentoVO;
	
	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	public SituacaoRequerimentoDepartamentoVO getSituacaoRequerimentoDepartamentoVO() {
		if (situacaoRequerimentoDepartamentoVO == null) {
			situacaoRequerimentoDepartamentoVO = new SituacaoRequerimentoDepartamentoVO();
		}
		return situacaoRequerimentoDepartamentoVO;
	}
	public void setSituacaoRequerimentoDepartamentoVO(SituacaoRequerimentoDepartamentoVO situacaoRequerimentoDepartamentoVO) {
		this.situacaoRequerimentoDepartamentoVO = situacaoRequerimentoDepartamentoVO;
	}
	
	public TipoRequerimentoDepartamentoVO getTipoRequerimentoDepartamentoVO() {
		if (tipoRequerimentoDepartamentoVO == null) {
			tipoRequerimentoDepartamentoVO = new TipoRequerimentoDepartamentoVO();
		}
		return tipoRequerimentoDepartamentoVO;
	}
	
	public void setTipoRequerimentoDepartamentoVO(TipoRequerimentoDepartamentoVO tipoRequerimentoDepartamentoVO) {
		this.tipoRequerimentoDepartamentoVO = tipoRequerimentoDepartamentoVO;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((situacaoRequerimentoDepartamentoVO == null) ? 0 : situacaoRequerimentoDepartamentoVO.getCodigo().hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TipoRequerimentoSituacaoDepartamentoVO other = (TipoRequerimentoSituacaoDepartamentoVO) obj;
		if (situacaoRequerimentoDepartamentoVO == null) {
			if (other.situacaoRequerimentoDepartamentoVO != null)
				return false;
		} else if (!situacaoRequerimentoDepartamentoVO.getCodigo().equals(other.situacaoRequerimentoDepartamentoVO.getCodigo()))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "TipoRequerimentoSituacaoDepartamentoVO [situacaoRequerimentoDepartamentoVO=" + situacaoRequerimentoDepartamentoVO.getSituacao() + "]";
	}
	
	
}
