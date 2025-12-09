package negocio.comuns.financeiro;

import java.io.Serializable;

import negocio.comuns.arquitetura.SuperVO;


public class ItemPrestacaoContaReceberVO extends SuperVO implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = -1216704007044997394L;
    private Integer codigo;
    private ContaReceberVO contaReceber;
    private ItemPrestacaoContaOrigemContaReceberVO itemPrestacaoContaOrigemContaReceber;
    
    public Integer getCodigo() {
        if(codigo == null){
            codigo = 0;
        }
        return codigo;
    }
    
    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }
    
    public ContaReceberVO getContaReceber() {
        if(contaReceber == null){
            contaReceber = new ContaReceberVO();
        }
        return contaReceber;
    }
    
    public void setContaReceber(ContaReceberVO contaReceber) {
        this.contaReceber = contaReceber;
    }
    
    public ItemPrestacaoContaOrigemContaReceberVO getItemPrestacaoContaOrigemContaReceber() {
        if(itemPrestacaoContaOrigemContaReceber == null){
            itemPrestacaoContaOrigemContaReceber = new ItemPrestacaoContaOrigemContaReceberVO();
        }
        return itemPrestacaoContaOrigemContaReceber;
    }
    
    public void setItemPrestacaoContaOrigemContaReceber(ItemPrestacaoContaOrigemContaReceberVO itemPrestacaoContaOrigemContaReceber) {
        this.itemPrestacaoContaOrigemContaReceber = itemPrestacaoContaOrigemContaReceber;
    }
    
    
}
