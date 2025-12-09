package negocio.comuns.utilitarias.dominios;

/**
 *
 * @author Diego
 */
public enum SituacaoTransferenciaEntrada {

    EFETIVADO("EF", "Efetivado"),
    EM_AVALIACAO("AV", "Em Avaliação"),
    ESTORNADO("ES", "Estornado"),
    INDEFERIDO("IN", "Indeferido");
    
    String valor;
    String descricao;

    SituacaoTransferenciaEntrada(String valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public static SituacaoTransferenciaEntrada getEnum(String valor) {
        SituacaoTransferenciaEntrada[] valores = values();
        for (SituacaoTransferenciaEntrada obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
        SituacaoTransferenciaEntrada obj = getEnum(valor);
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
