/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.utilitarias.dominios;

import java.util.ArrayList;
import java.util.List;

import jakarta.faces. model.SelectItem;

import negocio.comuns.utilitarias.Uteis;

/**
 *
 * @author Diego
 */
public enum SituacaoContaReceber {

    A_RECEBER("AR", "A Receber"),
    NEGOCIADO("NE", "Negociado"),
    RECEBIDO("RE", "Recebido"),
    CANCELADO_FINANCEIRO("CF", "Cancelado"),
    REMOVER("RM", "Remover"),
    RECUSADO("RC", "Recusado"),
    CANCELAMENTO_PENDENTE("CP", "Cancelamento Pendente"), 
    ESTORNO_PENDENTE("EP", "Estorno Pendente"),
    EXCLUID("EX", "Excluída");
    
    String valor;
    String descricao;

    SituacaoContaReceber(String valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public static SituacaoContaReceber getEnum(String valor) {
        SituacaoContaReceber[] valores = values();
        for (SituacaoContaReceber obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
        SituacaoContaReceber obj = getEnum(valor);
        if (obj != null) {
            return obj.getDescricao();
        }
        return valor;
    }
    
    public static List<SelectItem> getListaSelectItemSituacaoContaReceberVOs() {
    	List<SelectItem> itens = new ArrayList<SelectItem>(0);
    	itens.add(new SelectItem("", ""));
    	SituacaoContaReceber[] valores = values();
        for (SituacaoContaReceber obj : valores) {
        	itens.add(new SelectItem(obj, obj.getDescricao()));
        }
        return itens;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public boolean isNegociado(){
    	return Uteis.isAtributoPreenchido(name()) && name().equals(SituacaoContaReceber.NEGOCIADO.name());
    }
    
    public boolean isRecebido(){
    	return Uteis.isAtributoPreenchido(name()) && name().equals(SituacaoContaReceber.RECEBIDO.name());
    }
}
