package negocio.comuns.utilitarias.dominios;

/**
 * 
 * @author Diego
 */
public enum TipoEntradaAcervo {

	DOACAO("DO", "Doação"), 
	TRANSFERENCIA("TR", "Transferência"), 
	COMPRA("CO", "Compra"), 
	RESTAURACAO("RE", "Restauração"), 
	RECUPERADO("RP", "Recuperado"), 
	PERMUTA("PE", "Permuta"), 
	ASSINATURA("AS", "Assinatura"), 
	PUBLICACAO_PROPRIA("PP", "Publicação Própria"), 
	SUBSTITUICAO("SU", "Substituição"), 
	ENTRADA_SIMPLES("ES", "Entrada Simples"),
	OUTROS("OU", "Outros");

	String valor;
	String descricao;

	TipoEntradaAcervo(String valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
	}

	public static TipoEntradaAcervo getEnum(String valor) {
		TipoEntradaAcervo[] valores = values();
		for (TipoEntradaAcervo obj : valores) {
			if (obj.getValor().equals(valor)) {
				return obj;
			}
		}
		return null;
	}

	public static String getDescricao(String valor) {
		TipoEntradaAcervo obj = getEnum(valor);
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
