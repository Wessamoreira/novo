/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.biblioteca;

/**
 *
 * @author OTIMIZE-09
 */
import java.io.Serializable;

public class AcervoVO implements Serializable {

    private String biblioteca;
    private String secao;
    private String local;
    private String titulo;
    private String subTitulo;
    private String editora;
    private String classificacao;
    private String edicao;
    private String anoPublicacao;
    private String volume;
    private String numero;
    private String serie;
    private String isbn;
    private String issn;
    private String cutterPha;
    private Long exemplarAtivos;
    private Long exemplarInativos;
    private Long exemplarEmprestado;
    private String nivelBibliografico;
    public static final long serialVersionUID = 1L;

    public String getNivelBibliografico() {
        return nivelBibliografico;
    }

    public void setNivelBibliografico(String nivelBibliografico) {
        this.nivelBibliografico = nivelBibliografico;
    }

    public String getAnoPublicacao() {
        return anoPublicacao;
    }

    public void setAnoPublicacao(String anoPublicacao) {
        this.anoPublicacao = anoPublicacao;
    }

    public String getClassificacao() {
        return classificacao;
    }

    public void setClassificacao(String classificacao) {
        this.classificacao = classificacao;
    }

    public String getCutterPha() {
        return cutterPha;
    }

    public void setCutterPha(String cutterPha) {
        this.cutterPha = cutterPha;
    }

    public String getEdicao() {
        return edicao;
    }

    public void setEdicao(String edicao) {
        this.edicao = edicao;
    }

    public String getEditora() {
        return editora;
    }

    public void setEditora(String editora) {
        this.editora = editora;
    }

    public Long getExemplarAtivos() {
        return exemplarAtivos;
    }

    public void setExemplarAtivos(Long exemplarAtivos) {
        this.exemplarAtivos = exemplarAtivos;
    }

    public Long getExemplarInativos() {
        return exemplarInativos;
    }

    public void setExemplarInativos(Long exemplarInativos) {
        this.exemplarInativos = exemplarInativos;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getIssn() {
        return issn;
    }

    public void setIssn(String issn) {
        this.issn = issn;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getSubTitulo() {
        return subTitulo;
    }

    public void setSubTitulo(String subTitulo) {
        this.subTitulo = subTitulo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getBiblioteca() {
        return biblioteca;
    }

    public void setBiblioteca(String biblioteca) {
        this.biblioteca = biblioteca;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getSecao() {
        return secao;
    }

    public void setSecao(String secao) {
        this.secao = secao;
    }

	public Long getExemplarEmprestado() {
		if(exemplarEmprestado == null){
			exemplarEmprestado = 0l;
		}
		return exemplarEmprestado;
	}

	public void setExemplarEmprestado(Long exemplarEmprestado) {
		this.exemplarEmprestado = exemplarEmprestado;
	}
	
	public Long getTotalExemplar(){
		return getExemplarAtivos()+getExemplarEmprestado()+getExemplarInativos();
	}
    
    
}
