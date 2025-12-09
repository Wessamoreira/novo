package negocio.comuns.administrativo.enumeradores;

import negocio.interfaces.administrativo.PerfilTagSEIDecidirEnum;

public enum TagSEIDecidirCentroResultadoOrigemEnum implements PerfilTagSEIDecidirEnum{
	
	CENTRO_RESULTADO_ORIGEM_CODIGO("centroResultadoOrigem", "Código Centro Resultado Origem", "centroresultadoorigem.codigo",TipoCampoEnum.INTEIRO , 20),
	CENTRO_RESULTADO_ORIGEM_TIPO_MOVIMENTACAO("centroResultadoOrigem", "Tipo Movimentação", "centroresultadoorigem.tipomovimentacaocentroresultadoorigemenum",TipoCampoEnum.TEXTO , 60),
	CENTRO_RESULTADO_ORIGEM_VALOR("centroResultadoOrigem", "Valor", "centroresultadoorigem.valor",TipoCampoEnum.BIG_DECIMAL , 20),
	CENTRO_RESULTADO_ORIGEM_DESCRICAO_TIPO_ENTRADA("centroResultadoOrigem", "Valor de Acordo com Tipo Entrada", "case when centroresultadoorigem.tipomovimentacaocentroresultadoorigemenum = 'ENTRADA' then centroresultadoorigem.valor else (centroresultadoorigem.valor*-1) end",TipoCampoEnum.BIG_DECIMAL , 20),
	CENTRO_RESULTADO_ORIGEM_TIPO_ENTRADA("centroResultadoOrigem", "Tipo Receita/Despesa", "case when centroresultadoorigem.categoriadespesa is null then 'RECEITA' else 'DESPESA' end",TipoCampoEnum.TEXTO , 60),
	CENTRO_RESULTADO_ORIGEM_TIPO_RECEITA_DESPESA("centroResultadoOrigem", "Descrição de Receita/Despesa", "case when centroresultadoorigem.categoriadespesa is null then centroreceita.descricao else categoriadespesa.descricao end",TipoCampoEnum.TEXTO , 60);
	
	
	private TagSEIDecidirCentroResultadoOrigemEnum(String entidade, String atributo, String campo, TipoCampoEnum tipoCampo, Integer tamanhoCampo) {
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
