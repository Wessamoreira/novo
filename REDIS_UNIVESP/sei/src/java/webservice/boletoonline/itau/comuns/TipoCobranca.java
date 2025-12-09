package webservice.boletoonline.itau.comuns;

public enum TipoCobranca {
	
	BOLETO(1, "Registro"), 
	DEBITO_AUTOMATICO(2, "Alteração"), 
	CARTAO_CREDITO(3, "Consulta"),
	TEF_RESERVA(4, "Consulta");
	
	TipoCobranca(final Integer codigo, final String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}

	private Integer codigo;
	private String descricao;

	public static TipoCobranca valueOfCodigo(final Integer codigo) {
		for (final TipoCobranca ambiente : TipoCobranca.values()) {
			if (ambiente.getCodigo() == codigo) {
				return ambiente;
			}
		}
		return null;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}
