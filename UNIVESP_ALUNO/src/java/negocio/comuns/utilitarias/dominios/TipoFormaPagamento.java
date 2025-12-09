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
public enum TipoFormaPagamento {

    DEBITO_EM_CONTA_CORRENTE("DE", "Crédito/Débito em Conta Corrente"),
    CARTAO_DE_CREDITO("CA", "Cartão de Crédito"),
    CARTAO_DE_DEBITO("CD", "Cartão de Débito"),
    DINHEIRO("DI", "Dinheiro"),
    CHEQUE("CH", "Cheque"),
    DEPOSITO("DC", "Depósito"),
    BOLETO_BANCARIO("BO", "Boleto Bancário"),
    ISENCAO("IS", "Isenção"),
    PERMUTA("PE", "Permuta");
    
    String valor;
    String descricao;

    TipoFormaPagamento(String valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public static TipoFormaPagamento getEnum(String valor) {
        TipoFormaPagamento[] valores = values();
        for (TipoFormaPagamento obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
        TipoFormaPagamento obj = getEnum(valor);
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
    
    public boolean isIsencao() {
    	return Uteis.isAtributoPreenchido(name()) && name().equals(TipoFormaPagamento.ISENCAO.name());    	
    }
}
