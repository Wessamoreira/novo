package negocio.comuns.biblioteca;

import negocio.comuns.arquitetura.SuperVO;

public class CatalogoAreaConhecimentoVO extends SuperVO {
	
	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private CatalogoVO catalogoVO;
	
	public CatalogoAreaConhecimentoVO() {
        super();
    }
	
	public Integer getCodigo() {
		if(codigo == null) {
			codigo = 0;
		}
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	public CatalogoVO getCatalogoVO() {
		if(catalogoVO == null) {
			catalogoVO = new CatalogoVO();
		}
		return catalogoVO;
	}
	public void setCatalogoVO(CatalogoVO catalogoVO) {
		this.catalogoVO = catalogoVO;
	}
//	public AreaConhecimentoVO getAreaConhecimentoVO() {
//		if(areaConhecimentoVO == null) {
//			areaConhecimentoVO = new AreaConhecimentoVO();
//		}
//		return areaConhecimentoVO;
//	}
//	public void setAreaConhecimentoVO(AreaConhecimentoVO areaConhecimentoVO) {
//		this.areaConhecimentoVO = areaConhecimentoVO;
//	}

	
}
