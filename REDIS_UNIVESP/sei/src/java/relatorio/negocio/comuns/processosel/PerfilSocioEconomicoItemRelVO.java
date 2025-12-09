/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package relatorio.negocio.comuns.processosel;

/**
 *
 * @author Otimize-TI
 */
public class PerfilSocioEconomicoItemRelVO {
    
    protected String tipo;
    protected Integer quantidade;
    
    public PerfilSocioEconomicoItemRelVO(){
        setTipo("");
        setQuantidade(0);
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    

}
