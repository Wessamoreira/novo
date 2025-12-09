package negocio.comuns.biblioteca;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.arquitetura.SuperVO;

public class ArquivoMarc21CatalogoVO extends SuperVO {
	
	private Integer codigo;
	private ArquivoMarc21VO arquivoMarc21VO;
	private CatalogoVO catalogoVO;
	private Boolean selecionado;
	private ArquivoVO arquivoVO;
	
	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	public ArquivoMarc21VO getArquivoMarc21VO() {
		if (arquivoMarc21VO == null) {
			arquivoMarc21VO = new ArquivoMarc21VO();
		}
		return arquivoMarc21VO;
	}
	public void setArquivoMarc21VO(ArquivoMarc21VO arquivoMarc21VO) {
		this.arquivoMarc21VO = arquivoMarc21VO;
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
	public Boolean getSelecionado() {
		if (selecionado == null) {
			selecionado = Boolean.FALSE;
		}
		return selecionado;
	}
	public void setSelecionado(Boolean selecionado) {
		this.selecionado = selecionado;
	}
	public ArquivoVO getArquivoVO() {
		return arquivoVO;
	}
	public void setArquivoVO(ArquivoVO arquivoVO) {
		this.arquivoVO = arquivoVO;
	}

}
