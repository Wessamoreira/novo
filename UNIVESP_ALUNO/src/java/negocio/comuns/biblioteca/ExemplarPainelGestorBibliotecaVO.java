/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.biblioteca;

/**
 *
 * @author Rogerio
 */
public class ExemplarPainelGestorBibliotecaVO {

    private Integer codigoBiblioteca;
    private Integer qtdAtivo;
    private Integer qtdExtraviado;
    private Integer qtdDoacao;
    private Integer qtdMutilado;
    private Integer qtdDefasado;
    private Integer qtdDefasadoComplementar;
    private String nomeBiblioteca;

    public Integer getCodigoBiblioteca() {
        if (codigoBiblioteca == null) {
            codigoBiblioteca = 0;
        }
        return codigoBiblioteca;
    }

    public void setCodigoBiblioteca(Integer codigoBiblioteca) {
        this.codigoBiblioteca = codigoBiblioteca;
    }

    public Integer getQtdAtivo() {
        if (qtdAtivo == null) {
            qtdAtivo = 0;
        }
        return qtdAtivo;
    }

    public void setQtdAtivo(Integer qtdAtivo) {
        this.qtdAtivo = qtdAtivo;
    }

    public Integer getQtdDoacao() {
        if (qtdDoacao == null) {
            qtdDoacao = 0;
        }
        return qtdDoacao;
    }

    public void setQtdDoacao(Integer qtdDoacao) {
        this.qtdDoacao = qtdDoacao;
    }

    public Integer getQtdExtraviado() {
        if (qtdExtraviado == null) {
            qtdExtraviado = 0;
        }
        return qtdExtraviado;
    }

    public void setQtdExtraviado(Integer qtdExtraviado) {
        this.qtdExtraviado = qtdExtraviado;
    }

    public Integer getQtdMutilado() {
        if (qtdMutilado == null) {
            qtdMutilado = 0;
        }
        return qtdMutilado;
    }

    public void setQtdMutilado(Integer qtdMutilado) {
        this.qtdMutilado = qtdMutilado;
    }

    public String getNomeBiblioteca() {
        if (nomeBiblioteca == null) {
            nomeBiblioteca = "";
        }
        return nomeBiblioteca;
    }

    public void setNomeBiblioteca(String nomeBiblioteca) {
        this.nomeBiblioteca = nomeBiblioteca;
    }

    public Integer getQtdDefasado() {
        if (qtdDefasado == null) {
            qtdDefasado = 0;
        }
        return qtdDefasado;
    }

    public void setQtdDefasado(Integer qtdDefasado) {
        this.qtdDefasado = qtdDefasado;
    }

    public Integer getQtdDefasadoComplementar() {
        if (qtdDefasadoComplementar == null) {
            qtdDefasadoComplementar = 0;
        }
        return qtdDefasadoComplementar;
    }

    public void setQtdDefasadoComplementar(Integer qtdDefasadoComplementar) {
        this.qtdDefasadoComplementar = qtdDefasadoComplementar;
    }
}
