package negocio.comuns.utilitarias.dominios;

/**
 *
 * @author Diego
 */
public enum TiposRequerimentoRelatorio {

	 CANCELAMENTO("CA", "Cancelamento"),
	 ABANDONO_CURSO("AC", "Abandono de Curso"),
     TRANCAMENTO("TR", "Trancamento"),
     TRANSF_ENTRADA("TE", "Transf. Entrada"),
     TRANSF_SAIDA("TS", "Transf. Saída"),
     TRANSF_INTERNA("TI", "Transf. Interna"),
     FORMADO("FO", "Formado");    
    
    String valor;
    String descricao;

    TiposRequerimentoRelatorio(String valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public static TiposRequerimentoRelatorio getEnum(String valor) {
        TiposRequerimentoRelatorio[] valores = values();
        for (TiposRequerimentoRelatorio obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
        TiposRequerimentoRelatorio obj = getEnum(valor);
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
