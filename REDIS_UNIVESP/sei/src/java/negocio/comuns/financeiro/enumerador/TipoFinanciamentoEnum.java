package negocio.comuns.financeiro.enumerador;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.utilitarias.UteisJSF;

/**
* 
* @author Victor Hugo de Paula Costa 20/05/2015 11:22
*
*/
public enum TipoFinanciamentoEnum {

	OPERADORA, INSTITUICAO, AMBAS;
	
	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_TipoFinanciamentoEnum_" + this.name());
	}

	public String getName() {
		return this.name();
	}
	
    private static List<SelectItem> listaSelectItemTipoFinanciamento;
    public static List<SelectItem> getListaSelectItemTipoFinanciamento(){
        if(listaSelectItemTipoFinanciamento == null || listaSelectItemTipoFinanciamento.isEmpty()){
        	listaSelectItemTipoFinanciamento = new ArrayList<SelectItem>(0);      
        	listaSelectItemTipoFinanciamento.add(new SelectItem(TipoFinanciamentoEnum.OPERADORA, TipoFinanciamentoEnum.OPERADORA.getValorApresentar()));
        	listaSelectItemTipoFinanciamento.add(new SelectItem(TipoFinanciamentoEnum.INSTITUICAO, TipoFinanciamentoEnum.INSTITUICAO.getValorApresentar()));        	
        }
          
        return listaSelectItemTipoFinanciamento;
    }
	
    private static List<SelectItem> listaSelectItemTipoFinanciamentoOperadoraCartao;
    public static List<SelectItem> getListaSelectItemTipoFinanciamentoOperadoraCartao(){
        if(listaSelectItemTipoFinanciamentoOperadoraCartao == null || listaSelectItemTipoFinanciamentoOperadoraCartao.isEmpty()){
        	listaSelectItemTipoFinanciamentoOperadoraCartao = new ArrayList<SelectItem>(0);      
        	listaSelectItemTipoFinanciamentoOperadoraCartao.add(new SelectItem(TipoFinanciamentoEnum.OPERADORA, TipoFinanciamentoEnum.OPERADORA.getValorApresentar()));
        	listaSelectItemTipoFinanciamentoOperadoraCartao.add(new SelectItem(TipoFinanciamentoEnum.INSTITUICAO, TipoFinanciamentoEnum.INSTITUICAO.getValorApresentar()));
        	listaSelectItemTipoFinanciamentoOperadoraCartao.add(new SelectItem(TipoFinanciamentoEnum.AMBAS, TipoFinanciamentoEnum.AMBAS.getValorApresentar())); 
        }
          
        return listaSelectItemTipoFinanciamentoOperadoraCartao;
    }
}
