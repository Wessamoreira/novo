package negocio.comuns.administrativo;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.arquitetura.SuperVO;

public class LayoutRelatorioSEIDecidirArquivoVO extends SuperVO {

	private static final long serialVersionUID = 3492062723854107341L;

	private Integer codigo;
	private ArquivoVO arquivoVO;
	private LayoutRelatorioSEIDecidirVO layoutRelatorioSEIDecidirSuperiorVO;
	private LayoutRelatorioSEIDecidirVO layoutRelatorioSEIDecidirVO;
	private Boolean utilizarComoSumario;
	private Boolean utilizarFiltrosPrincipais;
	private String sqlWhere;

	// Transiente
	private Boolean itemEdicao;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public ArquivoVO getArquivoVO() {
		if (arquivoVO == null) {
			arquivoVO = new ArquivoVO();
		}
		return arquivoVO;
	}

	public void setArquivoVO(ArquivoVO arquivoVO) {
		this.arquivoVO = arquivoVO;
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

	public LayoutRelatorioSEIDecidirVO getLayoutRelatorioSEIDecidirSuperiorVO() {
		if (layoutRelatorioSEIDecidirSuperiorVO == null) {
			layoutRelatorioSEIDecidirSuperiorVO = new LayoutRelatorioSEIDecidirVO();
		}
		return layoutRelatorioSEIDecidirSuperiorVO;
	}

	public void setLayoutRelatorioSEIDecidirSuperiorVO(LayoutRelatorioSEIDecidirVO layoutRelatorioSEIDecidirSuperiorVO) {
		this.layoutRelatorioSEIDecidirSuperiorVO = layoutRelatorioSEIDecidirSuperiorVO;
	}

	public String getSqlWhere() {
		if (sqlWhere == null) {
			sqlWhere = "";
		}
		return sqlWhere;
	}

	public void setSqlWhere(String sqlWhere) {
		this.sqlWhere = sqlWhere;
	}

	public Boolean getItemEdicao() {
		if (itemEdicao == null) {
			itemEdicao = Boolean.FALSE;
		}
		return itemEdicao;
	}

	public void setItemEdicao(Boolean itemEdicao) {
		this.itemEdicao = itemEdicao;
	}

	public Boolean getUtilizarComoSumario() {
		if(utilizarComoSumario == null) {
			utilizarComoSumario =  false;
		}
		return utilizarComoSumario;
	}

	public void setUtilizarComoSumario(Boolean utilizarComoSumario) {
		this.utilizarComoSumario = utilizarComoSumario;
	}

	public Boolean getUtilizarFiltrosPrincipais() {
		if(utilizarFiltrosPrincipais == null) {
			utilizarFiltrosPrincipais =  false;
		}
		return utilizarFiltrosPrincipais;
	}

	public void setUtilizarFiltrosPrincipais(Boolean utilizarFiltrosPrincipais) {
		this.utilizarFiltrosPrincipais = utilizarFiltrosPrincipais;
	}

	
}
