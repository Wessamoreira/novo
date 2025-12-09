package webservice.certisign.comuns.novaApi;

public enum StatusAssinaturaCertisign {
    VALIDO(1),
    INVALIDO(2),
    INDEFINIDO(3),
    ALERTA(4);

    private final int codigo;

    StatusAssinaturaCertisign(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }

    public static StatusAssinaturaCertisign getFromCodigo(int codigo) {
        for (StatusAssinaturaCertisign status : StatusAssinaturaCertisign.values()) {
            if (status.getCodigo() == codigo) {
                return status;
            }
        }
        throw new IllegalArgumentException("Código inválido: " + codigo);
    }

    public boolean isNaoAssinado() {
        return this.equals(StatusAssinaturaCertisign.INVALIDO) || this.equals(StatusAssinaturaCertisign.ALERTA);
    }

    public boolean isAssinado() {
        return this.equals(StatusAssinaturaCertisign.INDEFINIDO);
    }
}
