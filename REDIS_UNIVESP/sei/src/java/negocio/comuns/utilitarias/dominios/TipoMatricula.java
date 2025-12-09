package negocio.comuns.utilitarias.dominios;

/**
 *
 * @author Diego
 */
public enum TipoMatricula {

	MATRICULA_CALOURO("CA", "Matrícula Calouro"),
	MATRICULA_VETERANO("VE", "Matrícula Veterano"),
	PORTADOR_DE_DIPLOMA("PD", "Portador de Diploma"),	
	REABERTURA("RA", "Reabertura"),
	REINGRESSO("RE", "Reingresso");
    
    String valor;
    String descricao;
    
    TipoMatricula(String valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public static TipoMatricula getEnum(String valor) {
        TipoMatricula[] valores = values();
        for (TipoMatricula obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
        TipoMatricula obj = getEnum(valor);
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
