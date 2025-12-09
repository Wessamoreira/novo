package negocio.comuns.financeiro;

import java.io.Serializable;

import negocio.comuns.arquitetura.SuperVO;


public class ItemPrestacaoContaTurmaVO extends SuperVO implements Serializable {

    private Integer codigo;
    private PrestacaoContaVO prestacaoConta;
    private PrestacaoContaVO prestacaoContaTurma;
    
    public Integer getCodigo() {
        if(codigo == null){
            codigo = 0;
        }
        return codigo;
    }
    
    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    
    public PrestacaoContaVO getPrestacaoConta() {
        if(prestacaoConta == null){
            prestacaoConta = new PrestacaoContaVO();
        }
        return prestacaoConta;
    }

    
    public void setPrestacaoConta(PrestacaoContaVO prestacaoConta) {
        this.prestacaoConta = prestacaoConta;
    }

    
    public PrestacaoContaVO getPrestacaoContaTurma() {
        if(prestacaoContaTurma == null){
            prestacaoContaTurma = new PrestacaoContaVO();
        }
        return prestacaoContaTurma;
    }

    
    public void setPrestacaoContaTurma(PrestacaoContaVO prestacaoContaTurma) {
        this.prestacaoContaTurma = prestacaoContaTurma;
    }
    
   
    
   
    
    
    
}
