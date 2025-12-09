package negocio.comuns.administrativo.enumeradores;

import negocio.interfaces.administrativo.PerfilTagSEIDecidirEnum;

public enum TagSEIDecidirCrossTabRHEnum implements PerfilTagSEIDecidirEnum {

	CROSSTAB_DATA_COMPETENCIA("competenciafolhapagamento", "Competência", " cfp.datacompetencia", TipoCampoEnum.DATA , 20),
	CROSSTAB_MES_COMPETENCIA("competenciafolhapagamento", "Mês", "extract('month' from cfp.datacompetencia) ", TipoCampoEnum.DATA , 20),
	CROSSTAB_ANO_COMPETENCIA("competenciafolhapagamento", "Ano", "extract('year' from cfp.datacompetencia) ", TipoCampoEnum.DATA , 20),
	CROSSTAB_VALOR("Valor", "Valor", "CASE\r\n" + 
			"	WHEN contrachequeevento.valorreferencia != 0.0 THEN contrachequeevento.valorreferencia\r\n" + 
			"	WHEN contrachequeevento.provento != 0.0 THEN contrachequeevento.provento\r\n" + 
			"	WHEN contrachequeevento.desconto != 0.0 THEN contrachequeevento.desconto \r\n" + 
			"   END", TipoCampoEnum.BIG_DECIMAL , 20),
	CROSSTAB_VALOR_REFERENCIA("Valor referência", "Competência", "contrachequeevento.valorreferencia", TipoCampoEnum.BIG_DECIMAL , 20),
	CROSSTAB_VALOR_PROVENTO("Provento", "provento", "contrachequeevento.provento", TipoCampoEnum.BIG_DECIMAL , 20),
	CROSSTAB_VALOR_DESCONTO("Desconto", "desconto", "contrachequeevento.desconto", TipoCampoEnum.BIG_DECIMAL , 20),
	CROSSTAB_MES("mes", "Mês", "CASE \r\n" + 
			"	when extract('month' from cfp.datacompetencia) = 01 then 'Janeiro'\r\n" + 
			"	when extract('month' from cfp.datacompetencia) = 02 then 'Fevereiro'\r\n" + 
			"	when extract('month' from cfp.datacompetencia) = 03 then 'Março'\r\n" + 
			"	when extract('month' from cfp.datacompetencia) = 04 then 'Abril'\r\n" + 
			"	when extract('month' from cfp.datacompetencia) = 05 then 'Maio'\r\n" + 
			"	when extract('month' from cfp.datacompetencia) = 06 then 'Junho'\r\n" + 
			"	when extract('month' from cfp.datacompetencia) = 07 then 'Julho'\r\n" + 
			"	when extract('month' from cfp.datacompetencia) = 08 then 'Agosto'\r\n" + 
			"	when extract('month' from cfp.datacompetencia) = 09 then 'Setembro'\r\n" + 
			"	when extract('month' from cfp.datacompetencia) = 10 then 'Outubro'\r\n" + 
			"	when extract('month' from cfp.datacompetencia) = 11 then 'Novembro'\r\n" + 
			"	when extract('month' from cfp.datacompetencia) = 12 then 'Dezembro'\r\n" + 
			"END ", TipoCampoEnum.TEXTO , 20);

	private TagSEIDecidirCrossTabRHEnum(String entidade, String atributo, String campo, TipoCampoEnum tipoCampo, Integer tamanhoCampo) {
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
