package negocio.comuns.utilitarias.dominios;

/**
 *
 * @author Diego
 */
public enum SituacaoDocente {

	EM_EXERCICIO(1, "EE", "Em Exercício"),
    AFASTADO_QUALIFICACAO(2, "AQ", "Afastado para Qualificação"),
    AFASTADO_EXERCICIO_OUTROS_ORGAOS(3, "OO", "Afastado Para Exercício em Outros Órgãos"),
    AFASTADO_OUTROS_MOTIVOS(4, "OM", "Afastado por Outro Motivo");
    
	int codigoCenso;
    String valor;
    String descricao;
    
    SituacaoDocente(int codigoCenso, String valor, String descricao) {
    	this.codigoCenso = codigoCenso;
    	this.valor = valor;
        this.descricao = descricao;
    }

    public static SituacaoDocente getEnum(String valor) {
        SituacaoDocente[] valores = values();
        for (SituacaoDocente obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
        SituacaoDocente obj = getEnum(valor);
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
