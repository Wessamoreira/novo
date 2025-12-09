package negocio.comuns.utilitarias.dominios;

public enum IntegracaoMestreGREnum {
    URL("https://prova.univesp.br"),
    TOKEN("R0RiWmlnWlxnTWJaaGlnWlxn");
    String valor;

    IntegracaoMestreGREnum(String valor) {
        this.valor = valor;
    }

    public static IntegracaoMestreGREnum getEnum(String valor) {
        IntegracaoMestreGREnum[] valores = values();
        for (IntegracaoMestreGREnum obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
