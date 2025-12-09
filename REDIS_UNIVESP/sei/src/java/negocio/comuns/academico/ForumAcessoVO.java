package negocio.comuns.academico;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;


public class ForumAcessoVO extends SuperVO {
    
    /**
     * 
     */
    private static final long serialVersionUID = 412468563642991712L;
    private Integer codigo;
    private UsuarioVO usuarioAcesso;
    private Integer forum;
    private Date dataAcesso;
    
    public Integer getCodigo() {
        if(codigo == null){
            codigo = 0;
        }
        return codigo;
    }
    
    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }
    
    public UsuarioVO getUsuarioAcesso() {
        if(usuarioAcesso == null){
            usuarioAcesso = new UsuarioVO();
        }
        return usuarioAcesso;
    }
    
    public void setUsuarioAcesso(UsuarioVO usuarioAcesso) {
        this.usuarioAcesso = usuarioAcesso;
    }
    
    public Integer getForum() {
        if(forum == null){
            forum = 0;
        }
        return forum;
    }
    
    public void setForum(Integer forum) {
        this.forum = forum;
    }
    
    public Date getDataAcesso() {
        if(dataAcesso == null){
            dataAcesso = new Date();
        }
        return dataAcesso;
    }
    
    public void setDataAcesso(Date dataAcesso) {
        this.dataAcesso = dataAcesso;
    }
    
    

}
