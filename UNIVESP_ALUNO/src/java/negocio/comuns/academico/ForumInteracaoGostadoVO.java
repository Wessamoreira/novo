package negocio.comuns.academico;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;


public class ForumInteracaoGostadoVO extends SuperVO {
    
    /**
     * 
     */
    private static final long serialVersionUID = 2352722506891135141L;
    private UsuarioVO usuarioGostou;
    private Date dataGostou;
    private Integer forumInteracao;
    
    public UsuarioVO getUsuarioGostou() {
        if(usuarioGostou == null){
            usuarioGostou = new UsuarioVO();
        }
        return usuarioGostou;
    }
    
    public void setUsuarioGostou(UsuarioVO usuarioGostou) {
        this.usuarioGostou = usuarioGostou;
    }
    
    public Date getDataGostou() {
        if(dataGostou == null){
            dataGostou = new Date();
        }
        return dataGostou;
    }
    
    public void setDataGostou(Date dataGostou) {
        this.dataGostou = dataGostou;
    }
    
    public Integer getForumInteracao() {
        if(forumInteracao == null){
            forumInteracao = 0;
        }
        return forumInteracao;
    }
    
    public void setForumInteracao(Integer forumInteracao) {
        this.forumInteracao = forumInteracao;
    }
    
    
    

}
