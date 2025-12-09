package negocio.comuns.utilitarias.dominios;

public enum TipoChancela {

    BASICO("PC", "Paga Chancela"),
    MEDIO("RC", "Receber Chancela");
    
    String valor;
    String descricao;

    TipoChancela(String valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public static TipoChancela getEnum(String valor) {
        TipoChancela[] valores = values();
        for (TipoChancela obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
        TipoChancela obj = getEnum(valor);
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
