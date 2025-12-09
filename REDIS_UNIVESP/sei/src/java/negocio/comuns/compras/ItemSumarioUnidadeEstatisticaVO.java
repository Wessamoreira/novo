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

public class ItemSumarioUnidadeEstatisticaVO implements Serializable {

    private Integer codigo;
    private String nome;
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
    public Integer getCodigo() {
        return codigo;
    }

    /**
     * @param codigoUnidade
     *            the codigoUnidade to set
     */
    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    /**
     * @return the nomeUnidadeResumido
     */
    public String getNome() {
        if(nome == null){
            nome = "";
        }
        return nome;
    }

    /**
     * @param nomeUnidadeResumido
     *            the nomeUnidadeResumido to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }
}
