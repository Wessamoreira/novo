package negocio.comuns.utilitarias.dominios;

/**
 *
 * @author Rodrigo
 */
public enum SituacaoArquivo {
    
    ATIVO("AT", "Ativo"),
    INATIVO("IN", "Inativo");

    String valor;
    String descricao;

    SituacaoArquivo( String valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public static SituacaoArquivo getEnum(String valor) {
        SituacaoArquivo[] valores = values();
        for (SituacaoArquivo obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
        SituacaoArquivo obj = getEnum(valor);
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
