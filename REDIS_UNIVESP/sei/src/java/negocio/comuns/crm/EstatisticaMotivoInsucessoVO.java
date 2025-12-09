/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.crm;



/**
 *
 * @author RODRIGO
 */
public class EstatisticaMotivoInsucessoVO {
    
    private MotivoInsucessoVO motivoInsucessoVO;
    private Integer quantidadeInsucesso;

    
    
    

    public MotivoInsucessoVO getMotivoInsucessoVO() {
        if(motivoInsucessoVO == null){
            motivoInsucessoVO = new MotivoInsucessoVO();
        }
        return motivoInsucessoVO;
    }

    public void setMotivoInsucessoVO(MotivoInsucessoVO motivoInsucessoVO) {
        this.motivoInsucessoVO = motivoInsucessoVO;
    }

    public Integer getQuantidadeInsucesso() {
        return quantidadeInsucesso;
    }

    public void setQuantidadeInsucesso(Integer quantidadeInsucesso) {
        this.quantidadeInsucesso = quantidadeInsucesso;
    }
    
    
    
}
