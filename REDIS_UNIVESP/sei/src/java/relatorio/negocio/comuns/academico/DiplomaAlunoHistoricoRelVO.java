/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.negocio.comuns.academico;

/**
 * 
 * @author Otimize-TI
 */
public class DiplomaAlunoHistoricoRelVO implements Cloneable {

	protected String disciplinaNome;
	protected String cargaHorariaDisciplina;
	protected String notaDisciplina;
	private String nomePeriodo;
	private String resultadoFinal;
	private Double frequencia;
	private String cargaHorariaFrequentada;
	private String anoSemstre;
	private String instituicao;
	private String cidade;
	private String estado;
	private String matricula;

	public DiplomaAlunoHistoricoRelVO() {
	}

	public DiplomaAlunoHistoricoRelVO getClone() throws Exception {
		return (DiplomaAlunoHistoricoRelVO) super.clone();
	}

	/**
	 * @return the disciplinaNome
	 */
	public String getDisciplinaNome() {
		if (disciplinaNome == null) {
			disciplinaNome = "";
		}
		return disciplinaNome;
	}

	/**
	 * @param disciplinaNome
	 *            the disciplinaNome to set
	 */
	public void setDisciplinaNome(String disciplinaNome) {
		this.disciplinaNome = disciplinaNome;
	}

	/**
	 * @return the cargaHorariaDisciplina
	 */
	public String getCargaHorariaDisciplina() {
		if (cargaHorariaDisciplina == null) {
			cargaHorariaDisciplina = "";
		}
		return cargaHorariaDisciplina;
	}

	/**
	 * @param cargaHorariaDisciplina
	 *            the cargaHorariaDisciplina to set
	 */
	public void setCargaHorariaDisciplina(String cargaHorariaDisciplina) {
		this.cargaHorariaDisciplina = cargaHorariaDisciplina;
	}

	/**
	 * @return the notaDisciplina
	 */
	public String getNotaDisciplina() {
		if (notaDisciplina == null) {
			notaDisciplina = "";
		}
		return notaDisciplina;
	}

	/**
	 * @param notaDisciplina
	 *            the notaDisciplina to set
	 */
	public void setNotaDisciplina(String notaDisciplina) {
		this.notaDisciplina = notaDisciplina;
	}

	public String getNomePeriodo() {
		if (nomePeriodo == null) {
			nomePeriodo = "";
		}
		return nomePeriodo;
	}

	public void setNomePeriodo(String nomePeriodo) {
		this.nomePeriodo = nomePeriodo;
	}

	public String getResultadoFinal() {
		if (resultadoFinal == null) {
			resultadoFinal = "";
		}
		return resultadoFinal;
	}

	public void setResultadoFinal(String resultadoFinal) {
		this.resultadoFinal = resultadoFinal;
	}

	public Double getFrequencia() {
		if (frequencia == null) {
			frequencia = 0.0;
		}
		return frequencia;
	}

	public void setFrequencia(Double frequencia) {
		this.frequencia = frequencia;
	}

	public String getCargaHorariaFrequentada() {
		if (cargaHorariaFrequentada == null) {
			cargaHorariaFrequentada = "";
		}
		return cargaHorariaFrequentada;
	}

	public void setCargaHorariaFrequentada(String cargaHorariaFrequentada) {
		this.cargaHorariaFrequentada = cargaHorariaFrequentada;
	}

	public String getAnoSemstre() {
		if (anoSemstre == null) {
			anoSemstre = "";
		}
		return anoSemstre;
	}

	public void setAnoSemstre(String anoSemstre) {
		this.anoSemstre = anoSemstre;
	}

	public String getInstituicao() {
		if (instituicao == null) {
			instituicao = "";
		}
		return instituicao;
	}

	public void setInstituicao(String instituicao) {
		this.instituicao = instituicao;
	}

	public String getCidade() {
		if (cidade == null) {
			cidade = "";
		}
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getEstado() {
		if (estado == null) {
			estado = "";
		}
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getMatricula() {
		if (matricula == null) {
			matricula = "";
		}
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

}
