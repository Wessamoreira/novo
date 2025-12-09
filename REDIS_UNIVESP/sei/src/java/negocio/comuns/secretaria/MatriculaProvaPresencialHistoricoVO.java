/**
 * 
 */
package negocio.comuns.secretaria;

import java.io.Serializable;

import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.arquitetura.SuperVO;

/**
 * @author Carlos Eugênio
 *
 */
public class MatriculaProvaPresencialHistoricoVO extends SuperVO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private MatriculaProvaPresencialDisciplinaVO matriculaProvaPresencialDisciplinaVO;
	private HistoricoVO historicoVO;
	private String notaAtualizada;
	private Double nota;
	private Boolean realizarCalculoMediaLancamentoNota;
	private DisciplinaVO disciplinaVO;
	private ConfiguracaoAcademicoVO configuracaoAcademicoVO;
	private String ano;
	private String semestre;
	

	public MatriculaProvaPresencialHistoricoVO() {
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

	public HistoricoVO getHistoricoVO() {
		if (historicoVO == null) {
			historicoVO = new HistoricoVO();
		}
		return historicoVO;
	}

	public void setHistoricoVO(HistoricoVO historicoVO) {
		this.historicoVO = historicoVO;
	}

	public String getNotaAtualizada() {
		if (notaAtualizada == null) {
			notaAtualizada = "";
		}
		return notaAtualizada;
	}

	public void setNotaAtualizada(String notaAtualizada) {
		this.notaAtualizada = notaAtualizada;
	}

	public Double getNota() {
		return nota;
	}

	public void setNota(Double nota) {
		this.nota = nota;
	}

	public Boolean getRealizarCalculoMediaLancamentoNota() {
		if (realizarCalculoMediaLancamentoNota == null) {
			realizarCalculoMediaLancamentoNota = Boolean.TRUE;
		}
		return realizarCalculoMediaLancamentoNota;
	}

	public void setRealizarCalculoMediaLancamentoNota(Boolean realizarCalculoMediaLancamentoNota) {
		this.realizarCalculoMediaLancamentoNota = realizarCalculoMediaLancamentoNota;
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

	public ConfiguracaoAcademicoVO getConfiguracaoAcademicoVO() {
		if (configuracaoAcademicoVO == null) {
			configuracaoAcademicoVO = new ConfiguracaoAcademicoVO();
		}
		return configuracaoAcademicoVO;
	}

	public void setConfiguracaoAcademicoVO(ConfiguracaoAcademicoVO configuracaoAcademicoVO) {
		this.configuracaoAcademicoVO = configuracaoAcademicoVO;
	}

	public String getAno() {
		if (ano == null) {
			ano = "";
		}
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getSemestre() {
		if (semestre == null) {
			semestre = "";
		}
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	public MatriculaProvaPresencialDisciplinaVO getMatriculaProvaPresencialDisciplinaVO() {
		if (matriculaProvaPresencialDisciplinaVO == null) {
			matriculaProvaPresencialDisciplinaVO = new MatriculaProvaPresencialDisciplinaVO();
		}
		return matriculaProvaPresencialDisciplinaVO;
	}

	public void setMatriculaProvaPresencialDisciplinaVO(MatriculaProvaPresencialDisciplinaVO matriculaProvaPresencialDisciplinaVO) {
		this.matriculaProvaPresencialDisciplinaVO = matriculaProvaPresencialDisciplinaVO;
	}
}
