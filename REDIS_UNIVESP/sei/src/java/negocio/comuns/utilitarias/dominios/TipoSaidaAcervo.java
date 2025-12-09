package negocio.comuns.utilitarias.dominios;

/**
 * 
 * @author Diego
 */
public enum TipoSaidaAcervo {

	DOACAO("DO", "Doação"),
        EXTRAVIADO("EX", "Extraviado"),
        TRANSFERENCIA("TR", "Transferência"),
        RESTAURACAO("RE", "Restauração"),
        MUTILADO("MU","Mutilado"),
        DESCARTE("DE","Descarte");

	String valor;
	String descricao;

	TipoSaidaAcervo(String valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
	}

	public static TipoSaidaAcervo getEnum(String valor) {
		TipoSaidaAcervo[] valores = values();
		for (TipoSaidaAcervo obj : valores) {
			if (obj.getValor().equals(valor)) {
				return obj;
			}
		}
		return null;
	}

	public static String getDescricao(String valor) {
		TipoSaidaAcervo obj = getEnum(valor);
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
