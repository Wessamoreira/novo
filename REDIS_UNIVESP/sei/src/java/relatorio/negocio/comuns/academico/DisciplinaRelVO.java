package relatorio.negocio.comuns.academico;

import java.io.Serializable;
import negocio.comuns.arquitetura.SuperVO;

public class DisciplinaRelVO extends SuperVO implements Serializable{

	private String nomeDisciplina;
	private Integer nrCreditos;
	private String tipoDisciplina;
	private Integer cargaHorariaDisciplina;
	private String tituloReferencia;
	private String tipoReferencia;
	private String tipoPublicacao;
	private String autores;
	private String isbn;
	private String localPublicacao;
	private String edicao;
	private String anoPublicacao;
	private String publicacaoExistenteBiblioteca;
	private String obra;
	private String nomeCurso;
	private String conteudo;
	private String classificacao;
	private Integer cargaHorariaConteudo;
	private String metodologia;
	private String habilidade;
	private String atitude;

	public DisciplinaRelVO() {
		setAnoPublicacao("");
		setAtitude("");
		setAutores("");
		setCargaHorariaConteudo(0);
		setCargaHorariaDisciplina(0);
		setClassificacao("");
		
		setConteudo("");
		setEdicao("");
		
		setHabilidade("");
		setIsbn("");
		setLocalPublicacao("");
		setMetodologia("");
		setNomeCurso("");
		setNomeDisciplina("");
		setNrCreditos(0);
		setObra("");
		setPublicacaoExistenteBiblioteca("");
		setTipoDisciplina("");
		setTipoPublicacao("");
		setTipoReferencia("");
		setTituloReferencia("");
	}

	public String getNomeDisciplina() {
		return nomeDisciplina;
	}

	public void setNomeDisciplina(String nomeDisciplina) {
		this.nomeDisciplina = nomeDisciplina;
	}

	public Integer getNrCreditos() {
		return nrCreditos;
	}

	public void setNrCreditos(Integer nrCreditos) {
		this.nrCreditos = nrCreditos;
	}

	

	public String getTipoDisciplina() {
		return tipoDisciplina;
	}

	public void setTipoDisciplina(String tipoDisciplina) {
		this.tipoDisciplina = tipoDisciplina;
	}

	public Integer getCargaHorariaDisciplina() {
		return cargaHorariaDisciplina;
	}

	public void setCargaHorariaDisciplina(Integer cargaHorariaDisciplina) {
		this.cargaHorariaDisciplina = cargaHorariaDisciplina;
	}

	public String getTituloReferencia() {
		return tituloReferencia;
	}

	public void setTituloReferencia(String tituloReferencia) {
		this.tituloReferencia = tituloReferencia;
	}

	public String getTipoReferencia() {
		return tipoReferencia;
	}

	public void setTipoReferencia(String tipoReferencia) {
		this.tipoReferencia = tipoReferencia;
	}

	public String getTipoPublicacao() {
		return tipoPublicacao;
	}

	public void setTipoPublicacao(String tipoPublicacao) {
		this.tipoPublicacao = tipoPublicacao;
	}

	public String getAutores() {
		return autores;
	}

	public void setAutores(String autores) {
		this.autores = autores;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getLocalPublicacao() {
		return localPublicacao;
	}

	public void setLocalPublicacao(String localPublicacao) {
		this.localPublicacao = localPublicacao;
	}

	public String getEdicao() {
		return edicao;
	}

	public void setEdicao(String edicao) {
		this.edicao = edicao;
	}

	public String getAnoPublicacao() {
		return anoPublicacao;
	}

	public void setAnoPublicacao(String anoPublicacao) {
		this.anoPublicacao = anoPublicacao;
	}

	public String getPublicacaoExistenteBiblioteca() {
		return publicacaoExistenteBiblioteca;
	}

	public void setPublicacaoExistenteBiblioteca(String publicacaoExistenteBiblioteca) {
		this.publicacaoExistenteBiblioteca = publicacaoExistenteBiblioteca;
	}

	public String getObra() {
		return obra;
	}

	public void setObra(String obra) {
		this.obra = obra;
	}

	public String getNomeCurso() {
		return nomeCurso;
	}

	public void setNomeCurso(String nomeCurso) {
		this.nomeCurso = nomeCurso;
	}

	public String getConteudo() {
		return conteudo;
	}

	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}

	public String getClassificacao() {
		return classificacao;
	}

	public void setClassificacao(String classificacao) {
		this.classificacao = classificacao;
	}

	public Integer getCargaHorariaConteudo() {
		return cargaHorariaConteudo;
	}

	public void setCargaHorariaConteudo(Integer cargaHorariaConteudo) {
		this.cargaHorariaConteudo = cargaHorariaConteudo;
	}

	public String getMetodologia() {
		return metodologia;
	}

	public void setMetodologia(String metodologia) {
		this.metodologia = metodologia;
	}

	public String getHabilidade() {
		return habilidade;
	}

	public void setHabilidade(String habilidade) {
		this.habilidade = habilidade;
	}

	public String getAtitude() {
		return atitude;
	}

	public void setAtitude(String atitude) {
		this.atitude = atitude;
	}

}