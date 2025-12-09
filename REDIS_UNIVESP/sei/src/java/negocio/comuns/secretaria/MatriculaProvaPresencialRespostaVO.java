/**
 * 
 */
package negocio.comuns.secretaria;

import java.io.Serializable;
import java.math.BigDecimal;

import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.pesquisa.AreaConhecimentoVO;

/**
 * @author Carlos Eugênio
 *
 */
public class MatriculaProvaPresencialRespostaVO extends SuperVO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer codigo;
	private MatriculaProvaPresencialVO matriculaProvaPresencialVO;
	private DisciplinaVO disciplinaVO;
	private AreaConhecimentoVO areaConhecimentoVO;
	private Integer nrQuestao;
	private String respostaAluno;
	private BigDecimal totalAcerto;
	private String respostaGabarito;
	private String situacaoQuestao;

	public MatriculaProvaPresencialRespostaVO() {
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

	public Integer getNrQuestao() {
		if (nrQuestao == null) {
			nrQuestao = 0;
		}
		return nrQuestao;
	}

	public void setNrQuestao(Integer nrQuestao) {
		this.nrQuestao = nrQuestao;
	}

	public String getRespostaAluno() {
		if (respostaAluno == null) {
			respostaAluno = "";
		}
		return respostaAluno;
	}

	public void setRespostaAluno(String respostaAluno) {
		this.respostaAluno = respostaAluno;
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

	public String getRespostaGabarito() {
		if (respostaGabarito == null) {
			respostaGabarito = "";
		}
		return respostaGabarito;
	}

	public void setRespostaGabarito(String respostaGabarito) {
		this.respostaGabarito = respostaGabarito;
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

	public DisciplinaVO getDisciplinaVO() {
		if (disciplinaVO == null) {
			disciplinaVO = new DisciplinaVO();
		}
		return disciplinaVO;
	}

	public void setDisciplinaVO(DisciplinaVO disciplinaVO) {
		this.disciplinaVO = disciplinaVO;
	}	

	public String getSituacaoQuestao() {
		if (situacaoQuestao == null) {
			situacaoQuestao = "";
		}
		return situacaoQuestao;
	}

	public void setSituacaoQuestao(String situacaoQuestao) {
		this.situacaoQuestao = situacaoQuestao;
	}

	public BigDecimal getTotalAcerto() {
		if (totalAcerto == null) {
			totalAcerto = BigDecimal.ZERO;
		}
		return totalAcerto;
	}

	public void setTotalAcerto(BigDecimal totalAcerto) {
		this.totalAcerto = totalAcerto;
	}
	
}
