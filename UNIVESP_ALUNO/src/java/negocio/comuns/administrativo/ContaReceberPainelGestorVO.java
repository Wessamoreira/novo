/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.administrativo;

/**
 *
 * @author Rodrigo
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




public class ContaReceberPainelGestorVO implements Serializable {

    
   
    private Map<Integer, String> hashMapDesconto;
    private String mesAno;
    private Double valorRecebidoNoMes;
    private Double valorRecebidoDoMes;
    private Boolean contaDoPeriodo;
    
    
    public static final long serialVersionUID = 1L;

    public Boolean getContaDoPeriodo() {
        if (contaDoPeriodo == null) {
            contaDoPeriodo = Boolean.TRUE;
        }
        return contaDoPeriodo;
    }

    public void setContaDoPeriodo(Boolean contaDoPeriodo) {
        this.contaDoPeriodo = contaDoPeriodo;
    }

    public Double getValorRecebidoDoMes() {
        if (valorRecebidoDoMes == null) {
            valorRecebidoDoMes = 0.0;
        }
        return valorRecebidoDoMes;
    }

    public void setValorRecebidoDoMes(Double valorRecebidoDoMes) {
        this.valorRecebidoDoMes = valorRecebidoDoMes;
    }

    public Double getValorRecebidoNoMes() {
        if (valorRecebidoNoMes == null) {
            valorRecebidoNoMes = 0.0;
        }
        return valorRecebidoNoMes;
    }

    public void setValorRecebidoNoMes(Double valorRecebidoNoMes) {
        this.valorRecebidoNoMes = valorRecebidoNoMes;
    }

    public String getMesAno() {
        if (mesAno == null) {
            mesAno = "";
        }
        return mesAno;
    }

    public void setMesAno(String mesAno) {
        this.mesAno = mesAno;
    }

   

    public Map<Integer, String> getHashMapDesconto() {
        if (hashMapDesconto == null) {
            hashMapDesconto = new HashMap<Integer, String>();
        }
        return hashMapDesconto;
    }

    public void setHashMapDesconto(Map<Integer, String> hashMapDesconto) {
        this.hashMapDesconto = hashMapDesconto;
    }

   

   

}
