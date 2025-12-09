package negocio.comuns.utilitarias.dominios;


/**
 *
 * @author Diego
 */
public enum TipoControleCobranca {

	//ARQUIVO_REMESSA("RM", "Arquivo Remessa"),
    ARQUIVO_RETORNO("RT", "Arquivo Retorno");

    String valor;
    String descricao;

    TipoControleCobranca(String valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public static TipoControleCobranca getEnum(String valor) {
        TipoControleCobranca[] valores = values();
        for (TipoControleCobranca obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
        TipoControleCobranca obj = getEnum(valor);
        if (obj != null) {
            return obj.getDescricao();
        }
        return valor;
    }

    public static String getAbreviatura(String valor) {
        TipoControleCobranca obj = getEnum(valor);
        if (obj != null) {
            return obj.getDescricao().substring(0, 3);
        }
        return valor;
    }

    public static String getValor(Integer valor) {
        TipoControleCobranca obj = getEnum("0"+valor);
        if (obj != null) {
            return obj.getValor();
        }
        return "0";
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
