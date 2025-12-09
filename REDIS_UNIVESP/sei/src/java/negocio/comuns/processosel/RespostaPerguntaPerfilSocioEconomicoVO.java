/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.processosel;

/**
 * 
 * @author Otimize-TI
 */
import java.io.Serializable;

public class RespostaPerguntaPerfilSocioEconomicoVO implements Serializable {

    private Boolean selecionado;
    private int pergunta;
    private String valor;
    private String key;
    public static final long serialVersionUID = 1L;

    public RespostaPerguntaPerfilSocioEconomicoVO() {
    }

    public void inicializarDados() {
        setKey("");
        setValor("");
        setSelecionado(Boolean.FALSE);
        setPergunta(0);
    }

    public String getKey() {
        if (key == null) {
            key = "";
        }
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Boolean getSelecionado() {
        return selecionado;
    }

    public void setSelecionado(Boolean selecionado) {
        this.selecionado = selecionado;
    }

    public String getValor() {
        if (valor == null) {
            valor = "";
        }
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public int getPergunta() {
        return pergunta;
    }

    public void setPergunta(int pergunta) {
        this.pergunta = pergunta;
    }
}
