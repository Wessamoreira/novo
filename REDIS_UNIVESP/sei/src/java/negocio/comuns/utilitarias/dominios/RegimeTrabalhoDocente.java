package negocio.comuns.utilitarias.dominios;

/**
 *
 * @author Diego
 */
public enum RegimeTrabalhoDocente {

	TEMPO_INTEGRAL_COM_DE(1, "TC", "Tempo Integral com DE"),
    TEMPO_INTEGRAL_SEM_DE(2, "TS", "Tempo Integral sem DE"),
    TEMPO_PARCIAL(3, "TP", "Tempo Parcial"),
    HORISTA(4, "HO", "Horista");
    
	int codigoCenso;
    String valor;
    String descricao;
    
    RegimeTrabalhoDocente(int codigoCenso, String valor, String descricao) {
    	this.codigoCenso = codigoCenso;
    	this.valor = valor;
        this.descricao = descricao;
    }

    public static RegimeTrabalhoDocente getEnum(String valor) {
        RegimeTrabalhoDocente[] valores = values();
        for (RegimeTrabalhoDocente obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
        RegimeTrabalhoDocente obj = getEnum(valor);
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

	public int getCodigoCenso() {
		return codigoCenso;
	}

	public void setCodigoCenso(int codigoCenso) {
		this.codigoCenso = codigoCenso;
	}
}
