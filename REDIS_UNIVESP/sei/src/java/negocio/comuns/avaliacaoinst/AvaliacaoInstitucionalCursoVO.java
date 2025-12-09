package negocio.comuns.avaliacaoinst;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.arquitetura.SuperVO;

public class AvaliacaoInstitucionalCursoVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4165315019359151222L;
	private Integer codigo;
	private AvaliacaoInstitucionalVO avaliacaoInstitucionalVO;
	private CursoVO cursoVO;
	
	public AvaliacaoInstitucionalCursoVO() {
		super();
	}
	
	public AvaliacaoInstitucionalCursoVO(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, CursoVO cursoVO) {
		super();
		this.avaliacaoInstitucionalVO = avaliacaoInstitucionalVO;
		this.cursoVO = cursoVO;
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
	public CursoVO getCursoVO() {
		if(cursoVO == null){
			cursoVO = new CursoVO();
		}
		return cursoVO;
	}
	public void setCursoVO(CursoVO cursoVO) {
		this.cursoVO = cursoVO;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cursoVO == null) ? 0 : cursoVO.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof CursoVO) && !(obj instanceof AvaliacaoInstitucionalCursoVO))
			return false;
		if (obj instanceof CursoVO) { 
				if(cursoVO == null && obj != null)
					return false;
				if(!cursoVO.getCodigo().equals(((CursoVO) obj).getCodigo()))
					return false;
				return true;
		}					
		AvaliacaoInstitucionalCursoVO other = (AvaliacaoInstitucionalCursoVO) obj;
		if (cursoVO == null) {
			if (other.cursoVO != null)
				return false;
		} else if (!cursoVO.getCodigo().equals(other.cursoVO.getCodigo()))
			return false;
		return true;
	}
	
	

}
