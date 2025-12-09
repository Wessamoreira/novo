/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.utilitarias.dominios;

import negocio.comuns.utilitarias.Uteis;

/**
 *
 * @author Diego
 */
public enum SituacaoFinanceira {

    A_PAGAR("AP", "A pagar"),
    PAGO_PARCIAL("PP", "Pago Parcial"),
    CANCELADO_FINANCEIRO("CF", "Cancelado"),
    NEGOCIADO("NE", "Negociado"),
    PAGO("PA", "Pago");
    
    String valor;
    String descricao;

    SituacaoFinanceira(String valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public static SituacaoFinanceira getEnum(String valor) {
        SituacaoFinanceira[] valores = values();
        for (SituacaoFinanceira obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
        SituacaoFinanceira obj = getEnum(valor);
        if (obj != null) {
            return obj.getDescricao();
        }
        return valor;
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
    
    public boolean isApagar() {
    	return Uteis.isAtributoPreenchido(name()) && name().equals(SituacaoFinanceira.A_PAGAR.name());
    }
    
    public boolean isPagarParcial() {
    	return Uteis.isAtributoPreenchido(name()) && name().equals(SituacaoFinanceira.PAGO_PARCIAL.name());
    }
    
    public boolean isCanceladoFinanceiro() {
    	return Uteis.isAtributoPreenchido(name()) && name().equals(SituacaoFinanceira.CANCELADO_FINANCEIRO.name());
    }
    
    public boolean isNegociado() {
    	return Uteis.isAtributoPreenchido(name()) && name().equals(SituacaoFinanceira.NEGOCIADO.name());
    }
    
    public boolean isPago() {
    	return Uteis.isAtributoPreenchido(name()) && name().equals(SituacaoFinanceira.PAGO.name());
    }
    
   
}
