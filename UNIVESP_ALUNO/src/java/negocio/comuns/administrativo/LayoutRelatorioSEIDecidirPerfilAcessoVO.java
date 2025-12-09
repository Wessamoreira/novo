package negocio.comuns.administrativo;

import negocio.comuns.arquitetura.PerfilAcessoVO;
import negocio.comuns.arquitetura.SuperVO;

public class LayoutRelatorioSEIDecidirPerfilAcessoVO extends SuperVO {

	private static final long serialVersionUID = 3492062723854107341L;

	private Integer codigo;
	private PerfilAcessoVO perfilAcessoVO;
	private LayoutRelatorioSEIDecidirVO layoutRelatorioSEIDecidirVO;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public PerfilAcessoVO getPerfilAcessoVO() {
		if (perfilAcessoVO == null) {
			perfilAcessoVO = new PerfilAcessoVO();
		}
		return perfilAcessoVO;
	}

	public void setPerfilAcessoVO(PerfilAcessoVO perfilAcessoVO) {
		this.perfilAcessoVO = perfilAcessoVO;
	}

	public LayoutRelatorioSEIDecidirVO getLayoutRelatorioSEIDecidirVO() {
		if (layoutRelatorioSEIDecidirVO == null) {
			layoutRelatorioSEIDecidirVO = new LayoutRelatorioSEIDecidirVO();
		}
		return layoutRelatorioSEIDecidirVO;
	}

	public void setLayoutRelatorioSEIDecidirVO(LayoutRelatorioSEIDecidirVO layoutRelatorioSEIDecidirVO) {
		this.layoutRelatorioSEIDecidirVO = layoutRelatorioSEIDecidirVO;
	}

}
