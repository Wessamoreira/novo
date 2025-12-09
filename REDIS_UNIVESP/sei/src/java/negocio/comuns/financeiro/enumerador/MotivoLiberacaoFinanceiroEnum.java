package negocio.comuns.financeiro.enumerador;

public enum MotivoLiberacaoFinanceiroEnum {

	TRANCAMENTO("TR", "Trancamento"),
	ABANDONO_CURSO("AC", "Abandono de Curso"),
	CANCELAMENTO("CA", "Cancelamento"),
	TRANFERENCIA_SAIDA("TS", "Tranferência de Saída"),
	TRANFERENCIA_INTERNA("TI", "Tranferência Interna"),
	JUBILAMENTO("JU", "Jubilamento");
	
	private String valor;
	private String descricao;

	private MotivoLiberacaoFinanceiroEnum(String valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
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
