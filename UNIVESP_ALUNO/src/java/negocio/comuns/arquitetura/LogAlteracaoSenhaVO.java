/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.arquitetura;

/**
 *
 * @author Kennedy Souza
 */
import java.io.Serializable;
import java.util.Date;

public class LogAlteracaoSenhaVO extends SuperVO implements Serializable {
    private Integer codigo;
    private Date data;
    private UsuarioVO usuario;
    private UsuarioVO usuarioResponsavelAlteracao;
    private String senha;
    public static final long serialVersionUID = 1L;


    public Date getData() {
	if(data == null){
	    data = new Date();
	}
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public UsuarioVO getUsuario() {
	if(usuario == null){
	    usuario = new UsuarioVO();
	}
        return usuario;
    }

    public void setUsuario(UsuarioVO usuario) {
        this.usuario = usuario;
    }

    public UsuarioVO getUsuarioResponsavelAlteracao() {
	if(usuarioResponsavelAlteracao == null){
	    usuarioResponsavelAlteracao =  new UsuarioVO();
	}
        return usuarioResponsavelAlteracao;
    }

    public void setUsuarioResponsavelAlteracao(UsuarioVO usuarioResponsavelAlteracao) {
        this.usuarioResponsavelAlteracao =  usuarioResponsavelAlteracao;
    }

    public String getSenha() {
	if(senha == null){
	    senha = "";
	}
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Integer getCodigo() {
	if(codigo == null){
	    codigo = 0;
	}
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    
}
