package negocio.comuns.utilitarias.dominios;

/**
 *
 * @author Diego
 */
public enum SituacaoMatriculaPeriodo {
	
	/**
	 * De acordo com o censo:
	 * 
	  	1 – Provável formando
		2 – Cursando
		3 – Matrícula trancada
		4 – Desvinculado do curso
 		6 – Formado
		7 – Falecido
	 *
	 * 0 - Controle do sei.
	 *  Utilizado para filtrar as matriculas que não podem ser incluidas no censo.
	 */
    INATIVA("IN", "Inativa"),
    ATIVA("CO", "Confirmada - Ativa"),
    CONCLUIDA("FI", "Finalizado"),
    TRANCADA("TR", "Trancada"),
    CANCELADA("CA", "Cancelada"),
    JUBILADO("PF", "Pendente Financeiramente"),
    PREMATRICULA_CANCELADA("PC", "Pré-Matrícula Cancelada");
    
    String valor;
    String descricao;

    SituacaoMatriculaPeriodo(String valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public static SituacaoMatriculaPeriodo getEnum(String valor) {
    	SituacaoMatriculaPeriodo[] valores = values();
        for (SituacaoMatriculaPeriodo obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
    	SituacaoMatriculaPeriodo obj = getEnum(valor);
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
