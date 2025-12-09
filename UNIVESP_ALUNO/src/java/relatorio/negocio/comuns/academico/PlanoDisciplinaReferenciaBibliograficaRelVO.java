package relatorio.negocio.comuns.academico;

import negocio.comuns.arquitetura.SuperVO;

public class PlanoDisciplinaReferenciaBibliograficaRelVO extends SuperVO {

	private static final long serialVersionUID = 1L;
	private String titulo;
	private String anoPublicacao;
	private String edicao;
	private String localPublicacao;
	private String isbn;
	private String autores;
	private String tipoPublicacao;
	private String tipoReferencia;
	private String subtitulo;
	private String justificativa;
	
	public String getTitulo() {
		if (titulo == null) {
			titulo = "";
		}
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getAnoPublicacao() {
		if (anoPublicacao == null) {
			anoPublicacao = "";
		}
		return anoPublicacao;
	}
	public void setAnoPublicacao(String anoPublicacao) {
		this.anoPublicacao = anoPublicacao;
	}
	public String getEdicao() {
		if (edicao == null) {
			edicao = "";
		}
		return edicao;
	}
	public void setEdicao(String edicao) {
		this.edicao = edicao;
	}
	public String getLocalPublicacao() {
		if (localPublicacao == null) {
			localPublicacao = "";
		}
		return localPublicacao;
	}
	public void setLocalPublicacao(String localPublicacao) {
		this.localPublicacao = localPublicacao;
	}
	public String getIsbn() {
		if (isbn == null) {
			isbn = "";
		}
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	public String getAutores() {
		if (autores == null) {
			autores = "";
		}
		return autores;
	}
	public void setAutores(String autores) {
		this.autores = autores;
	}
	public String getTipoPublicacao() {
		if (tipoPublicacao == null) {
			tipoPublicacao = "";
		}
		return tipoPublicacao;
	}
	public void setTipoPublicacao(String tipoPublicacao) {
		this.tipoPublicacao = tipoPublicacao;
	}
	public String getTipoReferencia() {
		if (tipoReferencia == null) {
			tipoReferencia = "";
		}
		return tipoReferencia;
	}
	public void setTipoReferencia(String tipoReferencia) {
		this.tipoReferencia = tipoReferencia;
	}
	
	public String getSubtitulo() {
		if (subtitulo == null) {
			subtitulo = "";
		}
		return subtitulo;
	}
	
	public void setSubtitulo(String subtitulo) {
		this.subtitulo = subtitulo;
	}
	
	public String getJustificativa() {
		if(justificativa == null) {
			justificativa = "";
		}
		return justificativa;
	}
	public void setJustificativa(String justificativa) {
		this.justificativa = justificativa;
	}
}
