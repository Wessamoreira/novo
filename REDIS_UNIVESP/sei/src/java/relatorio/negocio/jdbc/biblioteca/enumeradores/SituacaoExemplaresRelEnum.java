package relatorio.negocio.jdbc.biblioteca.enumeradores;

public enum SituacaoExemplaresRelEnum {

    MONOGRAFICO("MO", "Monográfico"),
    SERIE("SE", "Série");

    private final String valor;
    private final String descricao;

    SituacaoExemplaresRelEnum(String valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public static SituacaoExemplaresRelEnum getEnum(String valor) {
        SituacaoExemplaresRelEnum[] valores = values();
        for (SituacaoExemplaresRelEnum obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
        SituacaoExemplaresRelEnum obj = getEnum(valor);
        if (obj != null) {
            return obj.getDescricao();
        }
        return valor;
    }

    public String getValor() {
        return valor;
    }

    public String getDescricao() {
        return descricao;
    }

}
