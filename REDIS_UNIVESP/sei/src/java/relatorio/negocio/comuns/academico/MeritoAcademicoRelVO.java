package relatorio.negocio.comuns.academico;

import negocio.comuns.academico.MatriculaVO;

/**
 * @author Otimize-TI
 */
public class MeritoAcademicoRelVO implements Cloneable {

	private MatriculaVO matriculaVO;
	private String identificadorTurma;
	private Double mediaFinalCurso;
	private String periodoLetivo;
	private String disciplina;
	private Double nota;
	private String notaConceito;
	private String notaStr;
	private String mediaFinalCursoStr;
	private Double mediaDisciplina;
	private Integer quantidadeNotas;

	public MeritoAcademicoRelVO() {
	}

	public String getIdentificadorTurma() {
		if (identificadorTurma == null) {
			identificadorTurma = "";
		}
		return identificadorTurma;
	}

	public void setIdentificadorTurma(String identificadorTurma) {
		this.identificadorTurma = identificadorTurma;
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

	public Double getMediaFinalCurso() {
		return mediaFinalCurso;
	}

	public void setMediaFinalCurso(Double mediaFinalCurso) {
		this.mediaFinalCurso = mediaFinalCurso;
	}

	public String getPeriodoLetivo() {
		if (periodoLetivo == null) {
			periodoLetivo = "";
		}
		return periodoLetivo;
	}

	public void setPeriodoLetivo(String periodoLetivo) {
		this.periodoLetivo = periodoLetivo;
	}

	/**
	 * @return the disciplina
	 */
	public String getDisciplina() {
		if (disciplina == null) {
			disciplina = "";
		}
		return disciplina;
	}

	/**
	 * @param disciplina
	 *            the disciplina to set
	 */
	public void setDisciplina(String disciplina) {
		this.disciplina = disciplina;
	}

	/**
	 * @return the nota
	 */
	public Double getNota() {
		return nota;
	}

	/**
	 * @param nota
	 *            the nota to set
	 */
	public void setNota(Double nota) {
		this.nota = nota;
	}

	/**
	 * @return the notaConceito
	 */
	public String getNotaConceito() {
		if (notaConceito == null) {
			notaConceito = "";
		}
		return notaConceito;
	}

	/**
	 * @param notaConceito
	 *            the notaConceito to set
	 */
	public void setNotaConceito(String notaConceito) {
		this.notaConceito = notaConceito;
	}

	public String getNotaStr() {
		if (notaStr == null) {
			notaStr = "";
		}
		return notaStr;
	}

	public void setNotaStr(String notaStr) {
		this.notaStr = notaStr;
	}

	public String getMediaFinalCursoStr() {
		if (mediaFinalCursoStr == null) {
			mediaFinalCursoStr = "";
		}
		return mediaFinalCursoStr;
	}

	public void setMediaFinalCursoStr(String mediaFinalCursoStr) {
		this.mediaFinalCursoStr = mediaFinalCursoStr;
	}

	public Double getMediaDisciplina() {
		return mediaDisciplina;
	}

	public void setMediaDisciplina(Double mediaDisciplina) {
		this.mediaDisciplina = mediaDisciplina;
	}

	public Integer getQuantidadeNotas() {
		return quantidadeNotas;
	}

	public void setQuantidadeNotas(Integer quantidadeNotas) {
		this.quantidadeNotas = quantidadeNotas;
	}
}
