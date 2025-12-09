package negocio.comuns.avaliacaoinst;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;

public class AvaliacaoInstitucionalUnidadeEnsinoVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5627354922108120328L;
	private Integer codigo;
	private AvaliacaoInstitucionalVO avaliacaoInstitucionalVO;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	
	public AvaliacaoInstitucionalUnidadeEnsinoVO() {
		super();
	}
	
	public AvaliacaoInstitucionalUnidadeEnsinoVO(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, UnidadeEnsinoVO unidadeEnsinoVO) {
		super();
		this.avaliacaoInstitucionalVO = avaliacaoInstitucionalVO;
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}
	
	public Integer getCodigo() {
		if(codigo == null){
			codigo =0 ;
		}
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	public AvaliacaoInstitucionalVO getAvaliacaoInstitucionalVO() {
		if(avaliacaoInstitucionalVO == null){
			avaliacaoInstitucionalVO = new AvaliacaoInstitucionalVO();
		}
		return avaliacaoInstitucionalVO;
	}
	public void setAvaliacaoInstitucionalVO(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO) {
		this.avaliacaoInstitucionalVO = avaliacaoInstitucionalVO;
	}
	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if(unidadeEnsinoVO == null){
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}
	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((unidadeEnsinoVO == null) ? 0 : unidadeEnsinoVO.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof UnidadeEnsinoVO) && !(obj instanceof AvaliacaoInstitucionalUnidadeEnsinoVO))
			return false;
		if (obj instanceof UnidadeEnsinoVO) {
				if(unidadeEnsinoVO == null && obj != null)
					return false;
				if(!unidadeEnsinoVO.getCodigo().equals(((UnidadeEnsinoVO) obj).getCodigo()))
					return false;
				return true;
		}					
		AvaliacaoInstitucionalUnidadeEnsinoVO other = (AvaliacaoInstitucionalUnidadeEnsinoVO) obj;
		if (unidadeEnsinoVO == null) {
			if (other.unidadeEnsinoVO != null)
				return false;
		} else if (!unidadeEnsinoVO.getCodigo().equals(other.unidadeEnsinoVO.getCodigo()))
			return false;
		return true;
	}
	
	

}
