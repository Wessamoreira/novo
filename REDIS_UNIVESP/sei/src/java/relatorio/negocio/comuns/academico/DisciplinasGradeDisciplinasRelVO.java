package relatorio.negocio.comuns.academico;

public class DisciplinasGradeDisciplinasRelVO {

	private Integer disciplina;
	private String nomeDisciplina;
	private Integer ordem;
	private Boolean disciplinaDiversificada;
	private String periodoLetivo;
	private Integer nrPeriodoLetivo;
	private String chDisciplina;
	private String chSemana;
	private Integer chDisciplinaCalculada;
	private Integer chSemanaCalculada;

	public Integer getDisciplina() {
		if (disciplina == null) {
			disciplina = 0;
		}
		return disciplina;
	}

	public void setDisciplina(Integer disciplina) {
		this.disciplina = disciplina;
	}

	public String getNomeDisciplina() {
		if (nomeDisciplina == null) {
			nomeDisciplina = "";
		}
		return nomeDisciplina;
	}

	public void setNomeDisciplina(String nomeDisciplina) {
		this.nomeDisciplina = nomeDisciplina;
	}

	public Boolean getDisciplinaDiversificada() {
		if (disciplinaDiversificada == null) {
			disciplinaDiversificada = false;
		}
		return disciplinaDiversificada;
	}

	public void setDisciplinaDiversificada(Boolean disciplinaDiversificada) {
		this.disciplinaDiversificada = disciplinaDiversificada;
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

	public String getChDisciplina() {
		if (chDisciplina == null) {
			chDisciplina = "";
		}
		return chDisciplina;
	}

	public void setChDisciplina(String chDisciplina) {
		this.chDisciplina = chDisciplina;
	}

	public String getChSemana() {
		if (chSemana == null) {
			chSemana = "";
		}
		return chSemana;
	}

	public void setChSemana(String chSemana) {
		this.chSemana = chSemana;
	}

	public Integer getChDisciplinaCalculada() {
		if (chDisciplinaCalculada == null) {
			chDisciplinaCalculada = 0;
		}
		return chDisciplinaCalculada;
	}

	public void setChDisciplinaCalculada(Integer chDisciplinaCalculada) {
		this.chDisciplinaCalculada = chDisciplinaCalculada;
	}

	public Integer getChSemanaCalculada() {
		if (chSemanaCalculada == null) {
			chSemanaCalculada = 0;
		}
		return chSemanaCalculada;
	}

	public void setChSemanaCalculada(Integer chSemanaCalculada) {
		this.chSemanaCalculada = chSemanaCalculada;
	}
	
	public Integer getNrPeriodoLetivo() {
		if (nrPeriodoLetivo == null) {
			nrPeriodoLetivo = 0;
		}
		return nrPeriodoLetivo;
	}

	public void setNrPeriodoLetivo(Integer nrPeriodoLetivo) {
		this.nrPeriodoLetivo = nrPeriodoLetivo;
	}

	public Integer getOrdem() {
		if (ordem == null) {
			ordem = 0;
		}
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

}
