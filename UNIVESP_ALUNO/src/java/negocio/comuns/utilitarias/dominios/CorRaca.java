package negocio.comuns.utilitarias.dominios;

/**
 *
 * @author Diego
 */
public enum CorRaca {

    BRANCA(1, "BR", "Branca"),
    PRETA(2, "PR", "Preta"),
    PARDA(3, "PA", "Parda"),
    AMARELA(4, "AM", "Amarela"),
    INDIGENA(5, "IN", "Indígena"),
    NAO_DISPOE_INFORMACAO(0, "NI", "Não dispõe da Informação"),
    NAO_DECLARADO(0, "", "Não Declarado");
    
    int codigoCenso;
    String valor;
    String descricao;

    CorRaca(int codigoCenso, String valor, String descricao) {
        this.codigoCenso = codigoCenso;
    	this.valor = valor;
        this.descricao = descricao;
    }

    public static CorRaca getEnum(String valor) {
        CorRaca[] valores = values();
        for (CorRaca obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
        CorRaca obj = getEnum(valor);
        if (obj != null) {
            return obj.getDescricao();
        }
        return valor;
    }
    
    public static int getCodigoCenso(String valor) {
        CorRaca obj = getEnum(valor);
        if (obj != null) {
            return obj.getCodigoCenso();
        }
        return 0;
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

	public int getCodigoCenso() {
		return codigoCenso;
	}

	public void setCodigoCenso(int codigoCenso) {
		this.codigoCenso = codigoCenso;
	}
}
