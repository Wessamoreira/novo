/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.financeiro;

/**
 *
 * @author Administrador
 */
import java.io.Serializable;

import negocio.comuns.arquitetura.SuperVO;

public class MarcadorVO extends SuperVO implements Serializable {

    private String nome;
    private String tag;
    private Boolean selecionado;
    public static final long serialVersionUID = 1L;

    public String getTag() {
        if (tag == null) {
            tag = "";
        }
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getNome() {
        if (nome == null) {
            nome = "";
        }
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Boolean getSelecionado() {
        if (selecionado == null) {
            selecionado = Boolean.FALSE;
        }
        return selecionado;
    }

    public void setSelecionado(Boolean selecionado) {
        this.selecionado = selecionado;
    }
}
