package negocio.comuns.administrativo;

import negocio.comuns.arquitetura.SuperVO;



public class FollowMeDepartamentoVO extends SuperVO {
    
    private Integer codigo;
    private FollowMeVO followMe;
    private DepartamentoVO departamento;
    private Boolean selecionado;
    
    public Integer getCodigo() {
        return codigo;
    }
    
    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
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
    
    
    public Boolean getSelecionado() {
        if(selecionado == null){
            selecionado = false;
        }
        return selecionado;
    }
    
    public void setSelecionado(Boolean selecionado) {
        this.selecionado = selecionado;
    }

    
    public DepartamentoVO getDepartamento() {
        if(departamento == null){
            departamento = new DepartamentoVO();
        }
        return departamento;
    }

    
    public void setDepartamento(DepartamentoVO departamento) {
        this.departamento = departamento;
    }
    
    
    

}
