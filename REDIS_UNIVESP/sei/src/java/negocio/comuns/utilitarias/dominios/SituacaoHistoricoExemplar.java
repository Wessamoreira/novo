package negocio.comuns.utilitarias.dominios;

/**
 * 
 * @author Diego
 */
public enum SituacaoHistoricoExemplar {

	DEVOLVIDO("DE", "Devolvido"), EMPRESTADO("EM", "Emprestado"), RENOVADO("RE", "Renovado"), DEVOLVIDO_RESTAURACAO("DR", "Devolução Restauração"),RESTAURACAO("RS", "Restauração"), REMOVIDO("RM", "Removido"), RECUPERADO("RC", "Recuperado");

	String valor;
	String descricao;

	SituacaoHistoricoExemplar(String valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
	}

	public static SituacaoHistoricoExemplar getEnum(String valor) {
		SituacaoHistoricoExemplar[] valores = values();
		for (SituacaoHistoricoExemplar obj : valores) {
			if (obj.getValor().equals(valor)) {
				return obj;
			}
		}
		return null;
	}

	public static String getDescricao(String valor) {
		SituacaoHistoricoExemplar obj = getEnum(valor);
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
