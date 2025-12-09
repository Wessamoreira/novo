/**
 * 
 */
package negocio.comuns.bancocurriculum;

import java.io.Serializable;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.financeiro.ParceiroVO;

/**
 * @author Carlos Eugênio
 *
 */
public class AreaProfissionalParceiroVO extends SuperVO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private ParceiroVO parceiroVO;
	private AreaProfissionalVO areaProfissionalVO;

	public AreaProfissionalParceiroVO() {
		super();
	}

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public ParceiroVO getParceiroVO() {
		if (parceiroVO == null) {
			parceiroVO = new ParceiroVO();
		}
		return parceiroVO;
	}

	public void setParceiroVO(ParceiroVO parceiroVO) {
		this.parceiroVO = parceiroVO;
	}

	public AreaProfissionalVO getAreaProfissionalVO() {
		if (areaProfissionalVO == null) {
			areaProfissionalVO = new AreaProfissionalVO();
		}
		return areaProfissionalVO;
	}

	public void setAreaProfissionalVO(AreaProfissionalVO areaProfissionalVO) {
		this.areaProfissionalVO = areaProfissionalVO;
	}
	
}
