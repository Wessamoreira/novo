/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package negocio.comuns.financeiro;

import java.util.Date;

import negocio.comuns.academico.DescontoProgressivoVO;
import negocio.comuns.academico.PlanoDescontoVO;
import negocio.comuns.arquitetura.UsuarioVO;

/**
 *
 * @author Rodrigo
 */
public class AplicarDescontoContaReceberLogVO {

    private Date data;
    private ContaReceberVO contaReceberVO;
    private UsuarioVO responsavel;
    private String observacao;
    private PlanoDescontoVO planoDescontoVO;
    private DescontoProgressivoVO descontoProgressivoVO;

    public ContaReceberVO getContaReceberVO() {
        if(contaReceberVO == null){
            contaReceberVO = new ContaReceberVO();
        }
        return contaReceberVO;
    }

    public void setContaReceberVO(ContaReceberVO contaReceberVO) {
        this.contaReceberVO = contaReceberVO;
    }

    public Date getData() {
        if(data == null){
            data = new Date();
        }
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getObservacao() {
        if(observacao == null){
            observacao = "";
        }
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public UsuarioVO getResponsavel() {
        if(responsavel == null){
            responsavel = new UsuarioVO();
        }
        return responsavel;
    }

    public void setResponsavel(UsuarioVO responsavel) {
        this.responsavel = responsavel;
    }



     public DescontoProgressivoVO getDescontoProgressivoVO() {
        if (descontoProgressivoVO == null) {
            descontoProgressivoVO = new DescontoProgressivoVO();
        }
        return descontoProgressivoVO;
    }

    public void setDescontoProgressivoVO(DescontoProgressivoVO descontoProgressivoVO) {
        this.descontoProgressivoVO = descontoProgressivoVO;
    }

    public PlanoDescontoVO getPlanoDescontoVO() {
        if (planoDescontoVO == null) {
            planoDescontoVO = new PlanoDescontoVO();
        }
        return planoDescontoVO;
    }

    public void setPlanoDescontoVO(PlanoDescontoVO planoDescontoVO) {
        this.planoDescontoVO = planoDescontoVO;
    }

}
