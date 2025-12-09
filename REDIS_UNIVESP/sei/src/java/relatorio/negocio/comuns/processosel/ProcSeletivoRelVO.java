package relatorio.negocio.comuns.processosel;

import java.util.Date;

public class ProcSeletivoRelVO {

	private String descricao;
	private Date dataInicio;
	private Date dataFim;
	private Date dataInicioInternet;
	private Date dataFimInternet;
	private Double valorInscricao;
	private Date dataProva;
	private String horarioProva;
	private Integer nrOpcoesCurso;
	private String nivelEducacional;
	private Double mediaMinimaAprovacao;
	private Integer nrInscricao;
	private Date dataInscricao;
	private String situacao;
	private String cursoOpcao1;
	private Integer unidadeEnsino;
	private String opcaoLinguaEstrangeira;
	private String formaAcessoProcSeletivo;
	private String descricaoNecessidadeEspecial;
	private String nomePessoa;
	private String CPF;
	private String nomeUnidadeEnsino;
	private String nomeCurso1;
	private String nomeCurso2;
	private String nomeCurso3;

	public ProcSeletivoRelVO() {
		setCPF("");
		setCursoOpcao1("");
		setDataFim(new Date());
		setDataFimInternet(new Date());
		setDataInicio(new Date());
		setDataInicioInternet(new Date());
		setDataInscricao(new Date());
		setDataProva(new Date());
		setDescricao("");
		setDescricaoNecessidadeEspecial("");
		setFormaAcessoProcSeletivo("");
		setHorarioProva("");
		setMediaMinimaAprovacao(0.0);
		setNivelEducacional("");
		setNomeCurso1("");
		setNomeCurso2("");
		setNomeCurso3("");
		setNomePessoa("");
		setNomeUnidadeEnsino("");
		setNrInscricao(0);
		setNrOpcoesCurso(0);
		setOpcaoLinguaEstrangeira("");
		setSituacao("");
		setUnidadeEnsino(0);
		setValorInscricao(0.0);
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public Date getDataInicioInternet() {
		return dataInicioInternet;
	}

	public void setDataInicioInternet(Date dataInicioInternet) {
		this.dataInicioInternet = dataInicioInternet;
	}

	public Date getDataFimInternet() {
		return dataFimInternet;
	}

	public void setDataFimInternet(Date dataFimInternet) {
		this.dataFimInternet = dataFimInternet;
	}

	public Double getValorInscricao() {
		return valorInscricao;
	}

	public void setValorInscricao(Double valorInscricao) {
		this.valorInscricao = valorInscricao;
	}

	public Date getDataProva() {
		return dataProva;
	}

	public void setDataProva(Date dataProva) {
		this.dataProva = dataProva;
	}

	public String getHorarioProva() {
		return horarioProva;
	}

	public void setHorarioProva(String horarioProva) {
		this.horarioProva = horarioProva;
	}

	public Integer getNrOpcoesCurso() {
		return nrOpcoesCurso;
	}

	public void setNrOpcoesCurso(Integer nrOpcoesCurso) {
		this.nrOpcoesCurso = nrOpcoesCurso;
	}

	public String getNivelEducacional() {
		return nivelEducacional;
	}

	public void setNivelEducacional(String nivelEducacional) {
		this.nivelEducacional = nivelEducacional;
	}

	public Double getMediaMinimaAprovacao() {
		return mediaMinimaAprovacao;
	}

	public void setMediaMinimaAprovacao(Double mediaMinimaAprovacao) {
		this.mediaMinimaAprovacao = mediaMinimaAprovacao;
	}

	public Integer getNrInscricao() {
		return nrInscricao;
	}

	public void setNrInscricao(Integer nrInscricao) {
		this.nrInscricao = nrInscricao;
	}

	public Date getDataInscricao() {
		return dataInscricao;
	}

	public void setDataInscricao(Date dataInscricao) {
		this.dataInscricao = dataInscricao;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public String getCursoOpcao1() {
		return cursoOpcao1;
	}

	public void setCursoOpcao1(String cursoOpcao1) {
		this.cursoOpcao1 = cursoOpcao1;
	}

	public Integer getUnidadeEnsino() {
		return unidadeEnsino;
	}

	public void setUnidadeEnsino(Integer unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}

	public String getOpcaoLinguaEstrangeira() {
		return opcaoLinguaEstrangeira;
	}

	public void setOpcaoLinguaEstrangeira(String opcaoLinguaEstrangeira) {
		this.opcaoLinguaEstrangeira = opcaoLinguaEstrangeira;
	}

	public String getFormaAcessoProcSeletivo() {
		return formaAcessoProcSeletivo;
	}

	public void setFormaAcessoProcSeletivo(String formaAcessoProcSeletivo) {
		this.formaAcessoProcSeletivo = formaAcessoProcSeletivo;
	}

	public String getDescricaoNecessidadeEspecial() {
		return descricaoNecessidadeEspecial;
	}

	public void setDescricaoNecessidadeEspecial(String descricaoNecessidadeEspecial) {
		this.descricaoNecessidadeEspecial = descricaoNecessidadeEspecial;
	}

	public String getNomePessoa() {
		return nomePessoa;
	}

	public void setNomePessoa(String nomePessoa) {
		this.nomePessoa = nomePessoa;
	}

	public String getCPF() {
		return CPF;
	}

	public void setCPF(String cPF) {
		CPF = cPF;
	}

	public String getNomeUnidadeEnsino() {
		return nomeUnidadeEnsino;
	}

	public void setNomeUnidadeEnsino(String nomeUnidadeEnsino) {
		this.nomeUnidadeEnsino = nomeUnidadeEnsino;
	}

	public String getNomeCurso1() {
		return nomeCurso1;
	}

	public void setNomeCurso1(String nomeCurso1) {
		this.nomeCurso1 = nomeCurso1;
	}

	public String getNomeCurso2() {
		return nomeCurso2;
	}

	public void setNomeCurso2(String nomeCurso2) {
		this.nomeCurso2 = nomeCurso2;
	}

	public String getNomeCurso3() {
		return nomeCurso3;
	}

	public void setNomeCurso3(String nomeCurso3) {
		this.nomeCurso3 = nomeCurso3;
	}

}