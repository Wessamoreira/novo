package negocio.comuns.biblioteca;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.arquitetura.SuperVO;

public class CatalogoCursoVO extends SuperVO {

	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private CatalogoVO catalogoVO;
	private CursoVO cursoVO;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public CatalogoVO getCatalogoVO() {
		if (catalogoVO == null) {
			catalogoVO = new CatalogoVO();
		}
		return catalogoVO;
	}

	public void setCatalogoVO(CatalogoVO catalogoVO) {
		this.catalogoVO = catalogoVO;
	}

	public CursoVO getCursoVO() {
		if (cursoVO == null) {
			cursoVO = new CursoVO();
		}
		return cursoVO;
	}

	public void setCursoVO(CursoVO cursoVO) {
		this.cursoVO = cursoVO;
	}

}
