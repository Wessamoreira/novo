package relatorio.negocio.comuns.academico;

import java.util.Date;

public class DownloadRelVO {

	private Integer codigo;
	private String nomePessoa;
	private String dataDownload;
	private String nomeDisciplina;
	private String nomeTurma;
	private String nomeGrade;
	private String nomeTurno;
	private String periodoLetivo;
	private Integer cargaHoraria;
	private String nomeCurso;
	private String unidadeEnsino;
	private String horaDownload;
	private String descricaoArquivo;
	private String nomeArquivo;
	private String matricula;

	public DownloadRelVO() {
		setCargaHoraria(0);
		setCodigo(0);
		setDataDownload("");
		setDescricaoArquivo("");
		setHoraDownload("");
		setNomeCurso("");
		setNomeDisciplina("");
		setNomeGrade("");
		setNomePessoa("");
		setNomeTurma("");
		setNomeTurno("");
		setPeriodoLetivo("");
		setUnidadeEnsino("");
		setNomeArquivo("");
	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getNomePessoa() {
		return nomePessoa;
	}

	public void setNomePessoa(String nomePessoa) {
		this.nomePessoa = nomePessoa;
	}

	public String getDataDownload() {
		return dataDownload;
	}

	public void setDataDownload(String dataDownload) {
		this.dataDownload = dataDownload;
	}

	public String getNomeDisciplina() {
		return nomeDisciplina;
	}

	public void setNomeDisciplina(String nomeDisciplina) {
		this.nomeDisciplina = nomeDisciplina;
	}

	public String getNomeTurma() {
		return nomeTurma;
	}

	public void setNomeTurma(String nomeTurma) {
		this.nomeTurma = nomeTurma;
	}

	public String getNomeGrade() {
		return nomeGrade;
	}

	public void setNomeGrade(String nomeGrade) {
		this.nomeGrade = nomeGrade;
	}

	public String getNomeTurno() {
		return nomeTurno;
	}

	public void setNomeTurno(String nomeTurno) {
		this.nomeTurno = nomeTurno;
	}

	public String getPeriodoLetivo() {
		return periodoLetivo;
	}

	public void setPeriodoLetivo(String periodoLetivo) {
		this.periodoLetivo = periodoLetivo;
	}

	public Integer getCargaHoraria() {
		return cargaHoraria;
	}

	public void setCargaHoraria(Integer cargaHoraria) {
		this.cargaHoraria = cargaHoraria;
	}

	public String getNomeCurso() {
		return nomeCurso;
	}

	public void setNomeCurso(String nomeCurso) {
		this.nomeCurso = nomeCurso;
	}

	public String getUnidadeEnsino() {
		return unidadeEnsino;
	}

	public void setUnidadeEnsino(String unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}

	public String getHoraDownload() {
		return horaDownload;
	}

	public void setHoraDownload(String horaDownload) {
		this.horaDownload = horaDownload;
	}

	public String getDescricaoArquivo() {
		return descricaoArquivo;
	}

	public void setDescricaoArquivo(String descricaoArquivo) {
		this.descricaoArquivo = descricaoArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

	public String getNomeArquivo() {
		return nomeArquivo;
	}
	
	public String getMatricula() {
		if(matricula == null){
			matricula = "";
		}
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

}