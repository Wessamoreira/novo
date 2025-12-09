/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.arquitetura;

/**
 *
 * @author RODRIGO
 */
import java.io.Serializable;

public class LogVO implements Serializable {

    private String data;
    private String entidade;
    private String usuario;
    private String nivelLog;
    private String mensagem;
    public static final long serialVersionUID = 1L;

    public LogVO(String data, String entidade, String usuario, String nivelLog, String mensagem) {
        this.data = data;
        this.entidade = entidade;
        this.usuario = usuario;
        this.nivelLog = nivelLog;
        this.mensagem = mensagem;
    }

//    public String getData_Apresentar() {
//        return Uteis.getDataComHora(getData());
//    }
    public String getData() {
        if (data == null) {
            data = "";
        }
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getEntidade() {
        if (entidade == null) {
            entidade = "";
        }
        return entidade;
    }

    public void setEntidade(String entidade) {
        this.entidade = entidade;
    }

    public String getMensagem() {
        if (mensagem == null) {
            mensagem = "";
        }
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getNivelLog() {
        if (nivelLog == null) {
            nivelLog = "";
        }
        return nivelLog;
    }

    public void setNivelLog(String nivelLog) {
        this.nivelLog = nivelLog;
    }

    public String getUsuario() {
        if (usuario == null) {
            usuario = "";
        }
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
}
