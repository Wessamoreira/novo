/**
 * 
 */
package negocio.comuns.secretaria;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.processosel.ResultadoProcessamentoArquivoRespostaProvaPresencialVO;
import negocio.comuns.secretaria.enumeradores.SituacaoMatriculaProvaPresencialEnum;

/**
 * @author Carlos Eugênio
 *
 */
public class MatriculaProvaPresencialVO extends SuperVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer codigo;
	private ResultadoProcessamentoArquivoRespostaProvaPresencialVO resultadoProcessamentoArquivoRespostaProvaPresencialVO;
	private MatriculaVO matriculaVO;
	private List<MatriculaProvaPresencialDisciplinaVO> matriculaProvaPresencialDisciplinaVOs;
	private List<MatriculaProvaPresencialRespostaVO> matriculaProvaPresencialRespostaVOs;
	private BigDecimal totalAcerto;
	private BigDecimal totalErro;
	private Integer quantidadeDisciplina;
	private Integer quantidadeDisciplinaLocalizada;
	private Integer quantidadeDisciplinaNaoLocalizada;
	private Integer quantidadeDisciplinaConfiguracaoAcademicoNaoLocalizada;
	private SituacaoMatriculaProvaPresencialEnum situacaoMatriculaProvaPresencialEnum;
	private Boolean selecionado;

	public MatriculaProvaPresencialVO() {
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

	public MatriculaVO getMatriculaVO() {
		if (matriculaVO == null) {
			matriculaVO = new MatriculaVO();
		}
		return matriculaVO;
	}

	public void setMatriculaVO(MatriculaVO matriculaVO) {
		this.matriculaVO = matriculaVO;
	}

	public List<MatriculaProvaPresencialRespostaVO> getMatriculaProvaPresencialRespostaVOs() {
		if (matriculaProvaPresencialRespostaVOs == null) {
			matriculaProvaPresencialRespostaVOs = new ArrayList<MatriculaProvaPresencialRespostaVO>(0);
		}
		return matriculaProvaPresencialRespostaVOs;
	}

	public void setMatriculaProvaPresencialRespostaVOs(List<MatriculaProvaPresencialRespostaVO> matriculaProvaPresencialRespostaVOs) {
		this.matriculaProvaPresencialRespostaVOs = matriculaProvaPresencialRespostaVOs;
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

	public List<MatriculaProvaPresencialDisciplinaVO> getMatriculaProvaPresencialDisciplinaVOs() {
		if (matriculaProvaPresencialDisciplinaVOs == null) {
			matriculaProvaPresencialDisciplinaVOs = new ArrayList<MatriculaProvaPresencialDisciplinaVO>(0);
		}
		return matriculaProvaPresencialDisciplinaVOs;
	}

	public void setMatriculaProvaPresencialDisciplinaVOs(List<MatriculaProvaPresencialDisciplinaVO> matriculaProvaPresencialDisciplinaVOs) {
		this.matriculaProvaPresencialDisciplinaVOs = matriculaProvaPresencialDisciplinaVOs;
	}

	public BigDecimal getTotalErro() {
		if (totalErro == null) {
			totalErro = BigDecimal.ZERO;
		}
		return totalErro;
	}

	public void setTotalErro(BigDecimal totalErro) {
		this.totalErro = totalErro;
	}

	public SituacaoMatriculaProvaPresencialEnum getSituacaoMatriculaProvaPresencialEnum() {
		if (situacaoMatriculaProvaPresencialEnum == null) {
			situacaoMatriculaProvaPresencialEnum = SituacaoMatriculaProvaPresencialEnum.MATRICULA_ENCONTRADA;
		}
		return situacaoMatriculaProvaPresencialEnum;
	}

	public void setSituacaoMatriculaProvaPresencialEnum(SituacaoMatriculaProvaPresencialEnum situacaoMatriculaProvaPresencialEnum) {
		this.situacaoMatriculaProvaPresencialEnum = situacaoMatriculaProvaPresencialEnum;
	}

	public Integer getQuantidadeDisciplinaLocalizada() {
		if (quantidadeDisciplinaLocalizada == null) {
			quantidadeDisciplinaLocalizada = 0;
		}
		return quantidadeDisciplinaLocalizada;
	}

	public void setQuantidadeDisciplinaLocalizada(Integer quantidadeDisciplinaLocalizada) {
		this.quantidadeDisciplinaLocalizada = quantidadeDisciplinaLocalizada;
	}

	public Integer getQuantidadeDisciplinaNaoLocalizada() {
		if (quantidadeDisciplinaNaoLocalizada == null) {
			quantidadeDisciplinaNaoLocalizada = 0;
		}
		return quantidadeDisciplinaNaoLocalizada;
	}

	public void setQuantidadeDisciplinaNaoLocalizada(Integer quantidadeDisciplinaNaoLocalizada) {
		this.quantidadeDisciplinaNaoLocalizada = quantidadeDisciplinaNaoLocalizada;
	}

	public ResultadoProcessamentoArquivoRespostaProvaPresencialVO getResultadoProcessamentoArquivoRespostaProvaPresencialVO() {
		if (resultadoProcessamentoArquivoRespostaProvaPresencialVO == null) {
			resultadoProcessamentoArquivoRespostaProvaPresencialVO = new ResultadoProcessamentoArquivoRespostaProvaPresencialVO();
		}
		return resultadoProcessamentoArquivoRespostaProvaPresencialVO;
	}

	public void setResultadoProcessamentoArquivoRespostaProvaPresencialVO(ResultadoProcessamentoArquivoRespostaProvaPresencialVO resultadoProcessamentoArquivoRespostaProvaPresencialVO) {
		this.resultadoProcessamentoArquivoRespostaProvaPresencialVO = resultadoProcessamentoArquivoRespostaProvaPresencialVO;
	}

	public Integer getQuantidadeDisciplina() {
		if (quantidadeDisciplina == null) {
			quantidadeDisciplina = 0;
		}
		return quantidadeDisciplina;
	}

	public void setQuantidadeDisciplina(Integer quantidadeDisciplina) {
		this.quantidadeDisciplina = quantidadeDisciplina;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((matriculaVO == null) ? 0 : matriculaVO.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MatriculaProvaPresencialVO other = (MatriculaProvaPresencialVO) obj;
		if (matriculaVO == null) {
			if (other.matriculaVO != null)
				return false;
		} else if (!matriculaVO.equals(other.matriculaVO))
			return false;
		return true;
	}

	public Integer getQuantidadeDisciplinaConfiguracaoAcademicoNaoLocalizada() {
		if (quantidadeDisciplinaConfiguracaoAcademicoNaoLocalizada == null) {
			quantidadeDisciplinaConfiguracaoAcademicoNaoLocalizada = 0;
		}
		return quantidadeDisciplinaConfiguracaoAcademicoNaoLocalizada;
	}

	public void setQuantidadeDisciplinaConfiguracaoAcademicoNaoLocalizada(Integer quantidadeDisciplinaConfiguracaoAcademicoNaoLocalizada) {
		this.quantidadeDisciplinaConfiguracaoAcademicoNaoLocalizada = quantidadeDisciplinaConfiguracaoAcademicoNaoLocalizada;
	}

	public Boolean getSelecionado() {
		if (selecionado == null) {
			selecionado = Boolean.TRUE;
		}
		return selecionado;
	}

	public void setSelecionado(Boolean selecionado) {
		this.selecionado = selecionado;
	}

}
