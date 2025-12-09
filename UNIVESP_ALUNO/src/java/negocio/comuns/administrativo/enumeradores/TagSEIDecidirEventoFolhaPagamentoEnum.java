package negocio.comuns.administrativo.enumeradores;

import negocio.interfaces.administrativo.PerfilTagSEIDecidirEnum;

public enum TagSEIDecidirEventoFolhaPagamentoEnum implements PerfilTagSEIDecidirEnum {

	EVENTO_FOLHA_IDENTIFICADOR("identificador", "Identificador", "eventofolhapagamento.identificador", TipoCampoEnum.TEXTO , 20),
	EVENTO_FOLHA_DESCRICAO("descricao", "Descrição", "eventofolhapagamento.descricao", TipoCampoEnum.TEXTO , 50),
	EVENTO_FOLHA_TIPO_LANCAMENTO("tipoLancamento", "Tipo Lançamento", "eventofolhapagamento.tipoLancamento", TipoCampoEnum.TEXTO , 50),
	EVENTO_FOLHA_TIPO_EVENTO("tipoEvento", "Tipo Evento", "eventofolhapagamento.tipoEvento", TipoCampoEnum.TEXTO , 50),
	EVENTO_FOLHA_VALOR("valor", "Valor", " CASE " +
			" WHEN contrachequeevento.provento != 0  THEN contrachequeevento.provento" + 
			" WHEN contrachequeevento.desconto != 0  THEN contrachequeevento.desconto" + 
			" WHEN contrachequeevento.valorreferencia != 0  THEN contrachequeevento.valorreferencia" + 
			" ELSE 0" + 
			" END", TipoCampoEnum.BIG_DECIMAL , 20);

	private TagSEIDecidirEventoFolhaPagamentoEnum(String entidade, String atributo, String campo, TipoCampoEnum tipoCampo, Integer tamanhoCampo) {
		this.tamanhoCampo = tamanhoCampo;
		this.entidade = entidade;
		this.campo = campo;
		this.atributo = atributo;
		this.tipoCampo = tipoCampo;
	}

	private String entidade;
	private String atributo;
	private String campo;
	private Integer tamanhoCampo;
	private TipoCampoEnum tipoCampo;

	public String getEntidade() {
		return entidade;
	}

	public void setEntidade(String entidade) {
		this.entidade = entidade;
	}

	public String getCampo() {
		return campo;
	}

	public void setCampo(String campo) {
		this.campo = campo;
	}

	public TipoCampoEnum getTipoCampo() {
		return tipoCampo;
	}

	public void setTipoCampo(TipoCampoEnum tipoCampo) {
		this.tipoCampo = tipoCampo;
	}

	@Override
	public String getTag() {
		return this.name();
	}

	@Override
	public void setAtributo(String atributo) {
		this.atributo = atributo;

	}

	@Override
	public String getAtributo() {
		return atributo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.interfaces.administrativo.PerfilTagSEIDecidirEnum#getTamanhoCampo()
	 */
	@Override
	public Integer getTamanhoCampo() {
		return tamanhoCampo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.interfaces.administrativo.PerfilTagSEIDecidirEnum#setTamanhoCampo(
	 * java.lang.Integer)
	 */
	@Override
	public void setTamanhoCampo(Integer tamanhoCampo) {
		this.tamanhoCampo = tamanhoCampo;
	}

}
