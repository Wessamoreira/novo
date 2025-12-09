package relatorio.negocio.comuns.academico;

public class CertificadoCursoExtensaoDisciplinasRelVO {
	
	private String ano;
	private String semestre;
	private String nome;
	private String cargaHoraria;
	private String media;
	private String situacaoFinal;
	private String tipoDisciplina;
	private String matricula;
	private String frequencia;
	private String nomeProfessor;
	private String titulacaoProfessor;
    private String nomeMonografia;
    private String notaMonografia;
	
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
	public String getNome() {
		if (nome == null) {
			nome = "";
		}
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCargaHoraria() {
		if (cargaHoraria == null) {
			cargaHoraria = "";
		}
		return cargaHoraria;
	}
	public void setCargaHoraria(String cargaHoraria) {
		this.cargaHoraria = cargaHoraria;
	}
	public String getMedia() {
		if (media == null) {
			media = "";
		}
		return media;
	}
	public void setMedia(String media) {
		this.media = media;
	}
	public String getSituacaoFinal() {
		if (situacaoFinal == null) {
			situacaoFinal = "";
		}
		return situacaoFinal;
	}
	public void setSituacaoFinal(String situacaoFinal) {
		this.situacaoFinal = situacaoFinal;
	}
	public String getTipoDisciplina() {
		if (tipoDisciplina == null) {
			tipoDisciplina = "";
		}
		return tipoDisciplina;
	}
	public void setTipoDisciplina(String tipoDisciplina) {
		this.tipoDisciplina = tipoDisciplina;
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
	public String getFrequencia() {
		if (frequencia == null) {
			frequencia = "";
		}
		return frequencia;
	}
	public void setFrequencia(String frequencia) {
		this.frequencia = frequencia;
	}
	public String getNomeProfessor() {
		if (nomeProfessor == null) {
			nomeProfessor = "";
		}
		return nomeProfessor;
	}
	public void setNomeProfessor(String nomeProfessor) {
		this.nomeProfessor = nomeProfessor;
	}
	public String getTitulacaoProfessor() {
		if (titulacaoProfessor == null) {
			titulacaoProfessor = "";
		}
		return titulacaoProfessor;
	}
	public void setTitulacaoProfessor(String titulacaoProfessor) {
		this.titulacaoProfessor = titulacaoProfessor;
	}
	public String getNomeMonografia() {
		if (nomeMonografia == null) {
			nomeMonografia = "";
		}
		return nomeMonografia;
	}
	public void setNomeMonografia(String nomeMonografia) {
		this.nomeMonografia = nomeMonografia;
	}
	public String getNotaMonografia() {
		if (notaMonografia == null) {
			notaMonografia = "";
		}
		return notaMonografia;
	}
	public void setNotaMonografia(String notaMonografia) {
		this.notaMonografia = notaMonografia;
	}
	
}
