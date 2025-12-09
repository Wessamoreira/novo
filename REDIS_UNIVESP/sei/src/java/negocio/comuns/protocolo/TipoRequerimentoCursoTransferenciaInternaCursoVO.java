package negocio.comuns.protocolo;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.arquitetura.SuperVO;

public class TipoRequerimentoCursoTransferenciaInternaCursoVO extends SuperVO{

	private static final long serialVersionUID = 1L;
	
	private Integer codigo ;
	private TipoRequerimentoCursoVO tipoRequerimentoCursoVO;
	private CursoVO cursoVO ;
	
	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	
	public TipoRequerimentoCursoVO getTipoRequerimentoCursoVO() {
		if(tipoRequerimentoCursoVO == null ) {
			tipoRequerimentoCursoVO = new TipoRequerimentoCursoVO();
		}
		return tipoRequerimentoCursoVO;
	}
	public void setTipoRequerimentoCursoVO(TipoRequerimentoCursoVO tipoRequerimentoCursoVO) {
		this.tipoRequerimentoCursoVO = tipoRequerimentoCursoVO;
	}
	public CursoVO getCursoVO() {
		if(cursoVO == null ) {
			cursoVO = new CursoVO();
		}
		return cursoVO;
	}
	public void setCursoVO(CursoVO cursoVO) {
		this.cursoVO = cursoVO;
	}
	
	
	
	
}
