package relatorio.negocio.comuns.biblioteca;

import java.io.Serializable;
import negocio.comuns.arquitetura.SuperVO;


/**
 * Reponsável por manter os dados da entidade Catalogo. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class AcervoRelVO extends SuperVO implements Serializable {

    private String titulo;
    private String subtitulo;
    private String biblioteca;
    private String secao;
    private String editora;
    private String edicao;
    private String anoPublicacao;
    private Integer numExemplaresAtivos;
    private Integer numExemplaresInativos;
    private Integer volume;
    private Integer numero;
    private Integer serie;
    private Integer isbn;
    private Integer issn;
    private Integer cuttter;

    public String getAnoPublicacao() {
        return anoPublicacao;
    }

    public void setAnoPublicacao(String anoPublicacao) {
        this.anoPublicacao = anoPublicacao;
    }

    public String getBiblioteca() {
        return biblioteca;
    }

    public void setBiblioteca(String biblioteca) {
        this.biblioteca = biblioteca;
    }

    public Integer getCuttter() {
        return cuttter;
    }

    public void setCuttter(Integer cuttter) {
        this.cuttter = cuttter;
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

    public Integer getIsbn() {
        return isbn;
    }

    public void setIsbn(Integer isbn) {
        this.isbn = isbn;
    }

    public Integer getIssn() {
        return issn;
    }

    public void setIssn(Integer issn) {
        this.issn = issn;
    }

    public Integer getNumExemplaresAtivos() {
        return numExemplaresAtivos;
    }

    public void setNumExemplaresAtivos(Integer numExemplaresAtivos) {
        this.numExemplaresAtivos = numExemplaresAtivos;
    }

    public Integer getNumExemplaresInativos() {
        return numExemplaresInativos;
    }

    public void setNumExemplaresInativos(Integer numExemplaresInativos) {
        this.numExemplaresInativos = numExemplaresInativos;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getSecao() {
        return secao;
    }

    public void setSecao(String secao) {
        this.secao = secao;
    }

    public Integer getSerie() {
        return serie;
    }

    public void setSerie(Integer serie) {
        this.serie = serie;
    }

    public String getSubtitulo() {
        return subtitulo;
    }

    public void setSubtitulo(String subtitulo) {
        this.subtitulo = subtitulo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

}