package negocio.comuns.financeiro;

import negocio.comuns.academico.PlanoDescontoVO;
import negocio.comuns.arquitetura.SuperVO;

public class NegociacaoContaReceberPlanoDescontoVO extends SuperVO {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7635074049064681067L;
	private Integer codigo;
	private NegociacaoContaReceberVO negociacaoContaReceberVO;
	private PlanoDescontoVO planoDescontoVO;
	
	public Integer getCodigo() {
		if(codigo == null){
			codigo = 0;
		}
		return codigo;
	}
	
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	
	public NegociacaoContaReceberVO getNegociacaoContaReceberVO() {
		if(negociacaoContaReceberVO == null){
			negociacaoContaReceberVO = new NegociacaoContaReceberVO();
		}
		return negociacaoContaReceberVO;
	}
	
	public void setNegociacaoContaReceberVO(NegociacaoContaReceberVO negociacaoContaReceberVO) {
		this.negociacaoContaReceberVO = negociacaoContaReceberVO;
	}
	
	public PlanoDescontoVO getPlanoDescontoVO() {
		if(planoDescontoVO == null){
			planoDescontoVO = new PlanoDescontoVO();
		}
		return planoDescontoVO;
	}
	
	public void setPlanoDescontoVO(PlanoDescontoVO planoDescontoVO) {
		this.planoDescontoVO = planoDescontoVO;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((negociacaoContaReceberVO == null) ? 0 : negociacaoContaReceberVO.getCodigo().hashCode());
		result = prime * result + ((planoDescontoVO == null) ? 0 : planoDescontoVO.getCodigo().hashCode());
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
		NegociacaoContaReceberPlanoDescontoVO other = (NegociacaoContaReceberPlanoDescontoVO) obj;
		if (negociacaoContaReceberVO == null) {
			if (other.negociacaoContaReceberVO != null)
				return false;
		} else if (!negociacaoContaReceberVO.getCodigo().equals(other.negociacaoContaReceberVO.getCodigo()))
			return false;
		if (planoDescontoVO == null) {
			if (other.planoDescontoVO != null)
				return false;
		} else if (!planoDescontoVO.getCodigo().equals(other.planoDescontoVO.getCodigo()))
			return false;
		return true;
	}
	

}
