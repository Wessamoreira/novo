/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.utilitarias.dominios;

/**
 *
 * @author Diego
 */
public enum SituacaoVencimentoMatriculaPeriodo {

    CONTARECEBER_NAO_GERADA("NG", "Conta a Receber Não Gerada"),
    CONTARECEBER_GERADA("GE", "Conta a Receber Já Gerada"),
    CONTARECEBER_GERADAANTERIORMENTE_APTAPARAREGERACAO("RE", "Conta a Receber Gerada Anteriormente, Apta para Regeração"),
    CONTARECEBER_GERADA_EPAGA("GP", "Conta a Receber Gerada e Já Paga"),
    CONTARECEBER_GERADA_E_PAGA_PARCIALMENTE("PP", "Conta a Receber Gerada e Paga Parcialmente"),
    CONTARECEBER_RENEGOCIADA("NCR", "Conta a Receber Renegociada"),
    CONTARECEBER_RENEGOCIADA_EDITADA_MANUALMENTE("CEM", "Conta a Receber Renegociada - Editada Manualmente"),
    CONTARECEBER_NAO_DEVE_SER_GERADA("NA", "Conta a Receber Não Controlada");
    
    String valor;
    String descricao;

    SituacaoVencimentoMatriculaPeriodo(String valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public static SituacaoVencimentoMatriculaPeriodo getEnum(String valor) {
        SituacaoVencimentoMatriculaPeriodo[] valores = values();
        for (SituacaoVencimentoMatriculaPeriodo obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
        SituacaoVencimentoMatriculaPeriodo obj = getEnum(valor);
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
    
    public boolean isSituacaoContaReceberNaoGeracao(){
    	return name() != null && name().equals(SituacaoVencimentoMatriculaPeriodo.CONTARECEBER_NAO_GERADA.name());
    }
    
}
