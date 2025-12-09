/**
 * 
 */
package negocio.comuns.secretaria;

import java.io.Serializable;
import java.math.BigDecimal;

import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.pesquisa.AreaConhecimentoVO;
import negocio.comuns.secretaria.enumeradores.SituacaoMatriculaProvaPresencialDisciplinaEnum;

/**
 * @author Carlos Eugênio
 *
 */
public class MatriculaProvaPresencialDisciplinaVO extends SuperVO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer codigo;
	private MatriculaProvaPresencialVO matriculaProvaPresencialVO;
	private DisciplinaVO disciplinaVO;
	private AreaConhecimentoVO areaConhecimentoVO;
	private BigDecimal nota;
	private SituacaoMatriculaProvaPresencialDisciplinaEnum situacaoMatriculaProvaPresencialDisciplinaEnum;
	
	//Transient
	private HistoricoVO historicoVO;

	public MatriculaProvaPresencialDisciplinaVO() {
		super();
	}

	public DisciplinaVO getDisciplinaVO() {
		if (disciplinaVO == null) {
			disciplinaVO = new DisciplinaVO();
		}
		return disciplinaVO;
	}

	public void setDisciplinaVO(DisciplinaVO disciplinaVO) {
		this.disciplinaVO = disciplinaVO;
	}

	public BigDecimal getNota() {
		return nota;
	}

	public void setNota(BigDecimal nota) {
		this.nota = nota;
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

	public SituacaoMatriculaProvaPresencialDisciplinaEnum getSituacaoMatriculaProvaPresencialDisciplinaEnum() {
		if (situacaoMatriculaProvaPresencialDisciplinaEnum == null) {
			situacaoMatriculaProvaPresencialDisciplinaEnum = SituacaoMatriculaProvaPresencialDisciplinaEnum.DISCIPLINA_LOCALIZADA;
		}
		return situacaoMatriculaProvaPresencialDisciplinaEnum;
	}

	public void setSituacaoMatriculaProvaPresencialDisciplinaEnum(SituacaoMatriculaProvaPresencialDisciplinaEnum situacaoMatriculaProvaPresencialDisciplinaEnum) {
		this.situacaoMatriculaProvaPresencialDisciplinaEnum = situacaoMatriculaProvaPresencialDisciplinaEnum;
	}

	public AreaConhecimentoVO getAreaConhecimentoVO() {
		if (areaConhecimentoVO == null) {
			areaConhecimentoVO = new AreaConhecimentoVO();
		}
		return areaConhecimentoVO;
	}

	public void setAreaConhecimentoVO(AreaConhecimentoVO areaConhecimentoVO) {
		this.areaConhecimentoVO = areaConhecimentoVO;
	}

	public MatriculaProvaPresencialVO getMatriculaProvaPresencialVO() {
		if (matriculaProvaPresencialVO == null) {
			matriculaProvaPresencialVO = new MatriculaProvaPresencialVO();
		}
		return matriculaProvaPresencialVO;
	}

	public void setMatriculaProvaPresencialVO(MatriculaProvaPresencialVO matriculaProvaPresencialVO) {
		this.matriculaProvaPresencialVO = matriculaProvaPresencialVO;
	}

	public HistoricoVO getHistoricoVO() {
		if (historicoVO == null) {
			historicoVO = new HistoricoVO();
		}
		return historicoVO;
	}

	public void setHistoricoVO(HistoricoVO historicoVO) {
		this.historicoVO = historicoVO;
	}
}
