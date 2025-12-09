package relatorio.negocio.comuns.academico;

import java.util.Date;

public class ProcessoMatriculaRelVO {

	private String situacao;
	private Integer unidadeEnsino;
	private Boolean exigeConfirmacaoPresencial;
	private Boolean validoPelaInternet;
	private Date dataFinal;
	private Date dataInicio;
	private Date data;
	private String descricao;
	private Date dataFinalMatForaPrazo;
	private Date dataInicioMatForaPrazo;
	private Date dataFinalMatricula;
	private Date dataInicioMatricula;
	private String nomeCurso;
	private String nomeUnidadeEnsino;

	public ProcessoMatriculaRelVO() {
		setData(new Date());
		setDataFinal(new Date());
		setDataFinalMatForaPrazo(new Date());
		setDataFinalMatricula(new Date());
		setDataInicio(new Date());
		setDataInicioMatForaPrazo(new Date());
		setDataInicioMatricula(new Date());
		setDescricao("");
		setExigeConfirmacaoPresencial(false);
		setNomeCurso("");
		setNomeUnidadeEnsino("");
		setSituacao("");
		setUnidadeEnsino(0);
		setValidoPelaInternet(false);
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public Integer getUnidadeEnsino() {
		return unidadeEnsino;
	}

	public void setUnidadeEnsino(Integer unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}

	public Boolean getExigeConfirmacaoPresencial() {
		return exigeConfirmacaoPresencial;
	}

	public void setExigeConfirmacaoPresencial(Boolean exigeConfirmacaoPresencial) {
		this.exigeConfirmacaoPresencial = exigeConfirmacaoPresencial;
	}

	public Boolean getValidoPelaInternet() {
		return validoPelaInternet;
	}

	public void setValidoPelaInternet(Boolean validoPelaInternet) {
		this.validoPelaInternet = validoPelaInternet;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Date getDataFinalMatForaPrazo() {
		return dataFinalMatForaPrazo;
	}

	public void setDataFinalMatForaPrazo(Date dataFinalMatForaPrazo) {
		this.dataFinalMatForaPrazo = dataFinalMatForaPrazo;
	}

	public Date getDataInicioMatForaPrazo() {
		return dataInicioMatForaPrazo;
	}

	public void setDataInicioMatForaPrazo(Date dataInicioMatForaPrazo) {
		this.dataInicioMatForaPrazo = dataInicioMatForaPrazo;
	}

	public Date getDataFinalMatricula() {
		return dataFinalMatricula;
	}

	public void setDataFinalMatricula(Date dataFinalMatricula) {
		this.dataFinalMatricula = dataFinalMatricula;
	}

	public Date getDataInicioMatricula() {
		return dataInicioMatricula;
	}

	public void setDataInicioMatricula(Date dataInicioMatricula) {
		this.dataInicioMatricula = dataInicioMatricula;
	}

	public String getNomeCurso() {
		return nomeCurso;
	}

	public void setNomeCurso(String nomeCurso) {
		this.nomeCurso = nomeCurso;
	}

	public String getNomeUnidadeEnsino() {
		return nomeUnidadeEnsino;
	}

	public void setNomeUnidadeEnsino(String nomeUnidadeEnsino) {
		this.nomeUnidadeEnsino = nomeUnidadeEnsino;
	}

}