package negocio.comuns.utilitarias.dominios;

/**
 * 
 * @author Diego
 */
public enum SituacaoExemplar {
	
	/**
	 * Qualquer situação nova deverá ver vericado o relatório de Exemplares e Consulta de Acervo
	 */

	DISPONIVEL("DI", "Disponível"),
	CONSULTA("CO", "Consulta"),
	EMPRESTADO("EM", "Emprestado"),
	BAIXADO("BA", "Baixado"),
	RESERVA_TECNICA("RT", "Reserva Técnica"),
	EXTRAVIADO("EX", "Extraviado"),
	RENOVADO("RE", "Renovado"),
	CEDIDO("CE", "Cedido"),
	DESCARTADO("DE", "Descartado"),
	EM_RESTAURACAO("ER", "Em Restauração"),
	INUTILIZADO("IT", "Inutilizado");
	
	String valor;
	String descricao;

	SituacaoExemplar(String valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
	}

	public static SituacaoExemplar getEnum(String valor) {
		SituacaoExemplar[] valores = values();
		for (SituacaoExemplar obj : valores) {
			if (obj.getValor().equals(valor)) {
				return obj;
			}
		}
		return null;
	}

	public static String getDescricao(String valor) {
		SituacaoExemplar obj = getEnum(valor);
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
