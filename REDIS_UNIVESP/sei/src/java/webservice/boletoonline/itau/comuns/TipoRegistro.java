package webservice.boletoonline.itau.comuns;

public enum TipoRegistro {

	REGISTRO(1, "Registro"), ALTERACAO(2, "Alteração"), CONSULTA(2, "Consulta");

	TipoRegistro(final Integer codigo, final String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}

	private Integer codigo;
	private String descricao;

	public static TipoRegistro valueOfCodigo(final Integer codigo) {
		for (final TipoRegistro ambiente : TipoRegistro.values()) {
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
