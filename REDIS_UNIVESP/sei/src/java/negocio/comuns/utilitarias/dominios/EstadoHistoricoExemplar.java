package negocio.comuns.utilitarias.dominios;

/**
 * 
 * @author Diego
 */
public enum EstadoHistoricoExemplar {

	BOM("BO", "Bom"), FALTANDO_PAGINAS("FP", "Faltando Páginas"), MUTILADO("MU", "Mutilado");

	String valor;
	String descricao;

	EstadoHistoricoExemplar(String valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
	}

	public static EstadoHistoricoExemplar getEnum(String valor) {
		EstadoHistoricoExemplar[] valores = values();
		for (EstadoHistoricoExemplar obj : valores) {
			if (obj.getValor().equals(valor)) {
				return obj;
			}
		}
		return null;
	}

	public static String getDescricao(String valor) {
		EstadoHistoricoExemplar obj = getEnum(valor);
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
