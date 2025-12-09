package negocio.comuns.utilitarias.dominios;

/**
 *
 * @author Diego
 */
public enum Escolaridade {
	PRIMARIO(-1, "PR", "Primário"),
    BASICO(-1, "BA", "Básico"),
    DOUTORADO(3, "DO", "Doutorado"),
    POS_DOUTORADO(-1, "PD", "Pós-Doutorado"),
    SUPERIOR(0, "SU", "Superior"),
    LATO_SENSO_MBA(1, "PL", "Pós-Graduação Lato-Senso (MBA)"),
    MESTRADO(2, "ME", "Mestrado"),
    TECNICO(-1, "TE", "Técnico");    
	int codigoCenso;
    String valor;
    String descricao;
    
    Escolaridade(int codigoCenso, String valor, String descricao) {
    	this.codigoCenso = codigoCenso;
    	this.valor = valor;
        this.descricao = descricao;
    }

    public static Escolaridade getEnum(String valor) {
        Escolaridade[] valores = values();
        for (Escolaridade obj : valores) {
            if (obj.getValor().equals(valor) || obj.getDescricao().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
        Escolaridade obj = getEnum(valor);
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
