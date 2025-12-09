package negocio.comuns.financeiro;

import java.io.Serializable;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.Uteis;


public class ItemPrestacaoContaPagarVO extends SuperVO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 2391981515491303926L;
    private Integer codigo;
    private ContaPagarVO contaPagar;    
    private ItemPrestacaoContaCategoriaDespesaVO itemPrestacaoContaCategoriaDespesa;
    
    public Integer getCodigo() {
        if(codigo == null){
            codigo = 0;
        }
        return codigo;
    }
    
    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }
    
    public ContaPagarVO getContaPagar() {
        if(contaPagar == null){
            contaPagar = new ContaPagarVO();
        }
        return contaPagar;
    }
    
    public void setContaPagar(ContaPagarVO contaPagar) {
        this.contaPagar = contaPagar;
    }
    
    public ItemPrestacaoContaCategoriaDespesaVO getItemPrestacaoContaCategoriaDespesa() {
        if(itemPrestacaoContaCategoriaDespesa == null){
            itemPrestacaoContaCategoriaDespesa = new ItemPrestacaoContaCategoriaDespesaVO();
        }
        return itemPrestacaoContaCategoriaDespesa;
    }
    
    public void setItemPrestacaoContaCategoriaDespesa(ItemPrestacaoContaCategoriaDespesaVO itemPrestacaoContaCategoriaDespesa) {
        this.itemPrestacaoContaCategoriaDespesa = itemPrestacaoContaCategoriaDespesa;
    }
    
    public Double getValorCategoriaDespesa(){
    	return getContaPagar().getListaCentroResultadoOrigemVOs().stream().filter(p->p.getCategoriaDespesaVO().getCodigo().equals(getItemPrestacaoContaCategoriaDespesa().getCategoriaDespesa().getCodigo())).map(p -> p.getValor()).reduce(0D, (a,b) -> Uteis.arrendondarForcando2CadasDecimais(a + b));
    }
    
    public boolean equalsCampoSelecaoLista(ItemPrestacaoContaPagarVO obj){
    	return Uteis.isAtributoPreenchido(getContaPagar()) && Uteis.isAtributoPreenchido(obj.getContaPagar())
    			&& getContaPagar().getCodigo().equals(obj.getContaPagar().getCodigo());
    	
    }
     
    
    
    
}
