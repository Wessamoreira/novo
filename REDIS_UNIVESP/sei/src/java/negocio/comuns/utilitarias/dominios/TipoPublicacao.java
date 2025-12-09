package negocio.comuns.utilitarias.dominios;

/**
 * 
 * @author Murillo
 */
public enum TipoPublicacao {

	LIVRO("LI", "Livro"), 
	ARTIGO("AR", "Artigo"), 
	REVISTA("RE", "Revista"), 
	MONOGRAFIA("MO", "Monografia"), 
	VIDEO("VI", "Vídeo");

	String valor;
	String descricao;

	TipoPublicacao(String valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
	}

	public static TipoPublicacao getEnum(String valor) {
		TipoPublicacao[] valores = values();
		for (TipoPublicacao obj : valores) {
			if (obj.getValor().equals(valor)) {
				return obj;
			}
		}
		return null;
	}

	public static String getDescricao(String valor) {
		TipoPublicacao obj = getEnum(valor);
		if (obj != null) {
			return obj.getDescricao();
		}
		return "";
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
