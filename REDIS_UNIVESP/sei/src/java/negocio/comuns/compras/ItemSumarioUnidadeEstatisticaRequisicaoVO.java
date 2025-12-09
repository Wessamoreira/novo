/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.compras;

/**
 * 
 * @author Edigar
 */
import java.io.Serializable;

public class ItemSumarioUnidadeEstatisticaRequisicaoVO implements Serializable {

    private Integer codigoUnidade;
    private String nomeUnidadeResumido;
    private Integer quantidade;
    public static final long serialVersionUID = 1L;

    /**
     * @return the quantidade
     */
    public Integer getQuantidade() {
        return quantidade;
    }

    /**
     * @param quantidade
     *            the quantidade to set
     */
    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    /**
     * @return the codigoUnidade
     */
    public Integer getCodigoUnidade() {
        return codigoUnidade;
    }

    /**
     * @param codigoUnidade
     *            the codigoUnidade to set
     */
    public void setCodigoUnidade(Integer codigoUnidade) {
        this.codigoUnidade = codigoUnidade;
    }

    /**
     * @return the nomeUnidadeResumido
     */
    public String getNomeUnidadeResumido() {
        return nomeUnidadeResumido;
    }

    /**
     * @param nomeUnidadeResumido
     *            the nomeUnidadeResumido to set
     */
    public void setNomeUnidadeResumido(String nomeUnidadeResumido) {
        this.nomeUnidadeResumido = nomeUnidadeResumido;
    }
}
