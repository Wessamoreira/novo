package negocio.comuns.biblioteca;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;

public class ArquivoMarc21VO extends SuperVO {

	private Integer codigo;
	private UsuarioVO responsavel;
	private ArquivoVO arquivoVO;
	private List<ArquivoMarc21CatalogoVO> arquivoMarc21CatalogoVOs;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public UsuarioVO getResponsavel() {
		if (responsavel == null) {
			responsavel = new UsuarioVO();
		}
		return responsavel;
	}

	public void setResponsavel(UsuarioVO responsavel) {
		this.responsavel = responsavel;
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

	public List<ArquivoMarc21CatalogoVO> getArquivoMarc21CatalogoVOs() {
		if (arquivoMarc21CatalogoVOs == null) {
			arquivoMarc21CatalogoVOs = new ArrayList<ArquivoMarc21CatalogoVO>(0);
		}
		return arquivoMarc21CatalogoVOs;
	}

	public void setArquivoMarcCatalogoVOs(List<ArquivoMarc21CatalogoVO> arquivoMarc21CatalogoVOs) {
		this.arquivoMarc21CatalogoVOs = arquivoMarc21CatalogoVOs;
	}

}
