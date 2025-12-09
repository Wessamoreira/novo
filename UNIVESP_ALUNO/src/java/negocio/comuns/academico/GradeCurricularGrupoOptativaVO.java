package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;

public class GradeCurricularGrupoOptativaVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5458345219566988335L;
	private Integer codigo;
	private GradeCurricularVO gradeCurricular;
	private String descricao;
	private List<GradeCurricularGrupoOptativaDisciplinaVO> gradeCurricularGrupoOptativaDisciplinaVOs;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public GradeCurricularVO getGradeCurricular() {
		if (gradeCurricular == null) {
			gradeCurricular = new GradeCurricularVO();
		}
		return gradeCurricular;
	}

	public void setGradeCurricular(GradeCurricularVO gradeCurricular) {
		this.gradeCurricular = gradeCurricular;
	}

	public String getDescricao() {
		if (descricao == null) {
			descricao = "";
		}
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public List<GradeCurricularGrupoOptativaDisciplinaVO> getGradeCurricularGrupoOptativaDisciplinaVOs() {
		if (gradeCurricularGrupoOptativaDisciplinaVOs == null) {
			gradeCurricularGrupoOptativaDisciplinaVOs = new ArrayList<GradeCurricularGrupoOptativaDisciplinaVO>(0);
		}
		return gradeCurricularGrupoOptativaDisciplinaVOs;
	}

	public void setGradeCurricularGrupoOptativaDisciplinaVOs(List<GradeCurricularGrupoOptativaDisciplinaVO> gradeCurricularGrupoOptativaDisciplinaVOs) {
		this.gradeCurricularGrupoOptativaDisciplinaVOs = gradeCurricularGrupoOptativaDisciplinaVOs;
	}

}
