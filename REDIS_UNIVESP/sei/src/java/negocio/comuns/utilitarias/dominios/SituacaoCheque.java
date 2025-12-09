/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.utilitarias.dominios;

/**
 *
 * @author Diego
 */
public enum SituacaoCheque {
	/**
	 * 
	 * **********************ATENÇÃO**************************
	 * NÃO ALTERAR OS VALORES, ESTE AFETA O RELATÓRIO DE CHEQUE
	 * *********************TÔ DE OLHO************************
	 */
    BANCO("BA", "Banco"),
    EM_CAIXA("EC", "Em Caixa"),
    PAGAMENTO("PA", "Usado Em Pagamento"),
    DEVOLVIDO("DE", "Devolvido"),
    DEVOLVIDO_AO_SACADO("DS", "Devolvido ao Sacado"),
    PENDENTE("PE", "Pendente");
    
    String valor;
    String descricao;

    SituacaoCheque(String valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public static SituacaoCheque getEnum(String valor) {
        SituacaoCheque[] valores = values();
        for (SituacaoCheque obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
        SituacaoCheque obj = getEnum(valor);
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
}
