/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.protocolo;

/**
 *
 * @author Carlos
 */
import java.io.Serializable;

public class EstatisticaRequerimentoVO implements Serializable {

    private String tipoRequerimento;
    private Integer codigoTipoRequerimento;
    private Integer unidadeEnsino;
    private String nomeUnidade;
    private Integer quantidadeTipoRequerimento;
    public static final long serialVersionUID = 1L;

    /**
     * @return the tipoRequerimento
     */
    public String getTipoRequerimento() {
        if (tipoRequerimento == null) {
            tipoRequerimento = "";
        }
        return tipoRequerimento;
    }

    /**
     * @param tipoRequerimento the tipoRequerimento to set
     */
    public void setTipoRequerimento(String tipoRequerimento) {
        this.tipoRequerimento = tipoRequerimento;
    }

    /**
     * @return the unidadeEnsino
     */
    public Integer getUnidadeEnsino() {
        if (unidadeEnsino == null) {
            unidadeEnsino = 0;
        }
        return unidadeEnsino;
    }

    /**
     * @param unidadeEnsino the unidadeEnsino to set
     */
    public void setUnidadeEnsino(Integer unidadeEnsino) {
        this.unidadeEnsino = unidadeEnsino;
    }

    /**
     * @return the nomeUnidade
     */
    public String getNomeUnidade() {
        if (nomeUnidade == null) {
            nomeUnidade = "";
        }
        return nomeUnidade;
    }

    /**
     * @param nomeUnidade the nomeUnidade to set
     */
    public void setNomeUnidade(String nomeUnidade) {
        this.nomeUnidade = nomeUnidade;
    }

    /**
     * @return the quantidadeTipoRequerimento
     */
    public Integer getQuantidadeTipoRequerimento() {
        if (quantidadeTipoRequerimento == null) {
            quantidadeTipoRequerimento = 0;
        }
        return quantidadeTipoRequerimento;
    }

    /**
     * @param quantidadeTipoRequerimento the quantidadeTipoRequerimento to set
     */
    public void setQuantidadeTipoRequerimento(Integer quantidadeTipoRequerimento) {
        this.quantidadeTipoRequerimento = quantidadeTipoRequerimento;
    }

    /**
     * @return the codigoTipoRequerimento
     */
    public Integer getCodigoTipoRequerimento() {
        if (codigoTipoRequerimento == null) {
            codigoTipoRequerimento = 0;
        }
        return codigoTipoRequerimento;

    }

    /**
     * @param codigoTipoRequerimento the codigoTipoRequerimento to set
     */
    public void setCodigoTipoRequerimento(Integer codigoTipoRequerimento) {
        this.codigoTipoRequerimento = codigoTipoRequerimento;
    }
}
