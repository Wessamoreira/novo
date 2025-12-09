package negocio.comuns.administrativo;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.financeiro.CategoriaDespesaVO;


public class FollowMeCategoriaDespesaVO extends SuperVO {
    
    private Integer codigo;
    private FollowMeVO followMe;
    private CategoriaDespesaVO categoriaDespesa;
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
    
    public CategoriaDespesaVO getCategoriaDespesa() {
        if(categoriaDespesa == null){
            categoriaDespesa = new CategoriaDespesaVO();
        }
        return categoriaDespesa;
    }
    
    public void setCategoriaDespesa(CategoriaDespesaVO categoriaDespesa) {
        this.categoriaDespesa = categoriaDespesa;
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
    
    
    

}
