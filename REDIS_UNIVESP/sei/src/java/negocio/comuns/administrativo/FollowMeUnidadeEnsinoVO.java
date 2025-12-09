package negocio.comuns.administrativo;

import negocio.comuns.arquitetura.SuperVO;


public class FollowMeUnidadeEnsinoVO extends SuperVO {

    /**
     * 
     */
    private static final long serialVersionUID = 1461196266385917343L;
    private Integer codigo;
    private FollowMeVO followMe;
    private UnidadeEnsinoVO unidadeEnsino;
    private Boolean selecionar;
    
    public FollowMeUnidadeEnsinoVO clone() throws CloneNotSupportedException{
        FollowMeUnidadeEnsinoVO clone = (FollowMeUnidadeEnsinoVO)super.clone();
        clone.setCodigo(0);
        clone.setFollowMe(new FollowMeVO());
        return clone;
    }
    
    public FollowMeVO getFollowMe() {
        if(followMe == null){
            followMe = new FollowMeVO();
        }
        return followMe;
    }
    
    public void setFollowMe(FollowMeVO followMe) {
        this.followMe = followMe;
    }
    
    public UnidadeEnsinoVO getUnidadeEnsino() {
        if(unidadeEnsino == null){
            unidadeEnsino = new UnidadeEnsinoVO();
        }
        return unidadeEnsino;
    }
    
    public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
        this.unidadeEnsino = unidadeEnsino;
    }

    
    public Boolean getSelecionar() {
        if(selecionar == null){
            selecionar = false;
        }
        return selecionar;
    }

    
    public void setSelecionar(Boolean selecionar) {
        this.selecionar = selecionar;
    }

    
    public Integer getCodigo() {
        return codigo;
    }

    
    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }
    
    
}
