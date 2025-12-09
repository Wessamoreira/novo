package negocio.comuns.administrativo.enumeradores;

import negocio.interfaces.administrativo.PerfilTagSEIDecidirEnum;

public enum TagSEIDecidirCentroResultadoEnum  implements PerfilTagSEIDecidirEnum{
	
	CENTRO_RESULTADO_CODIGO("centroResultado", "Código Centro Resultado", "centroResultado.codigo",TipoCampoEnum.INTEIRO , 20),
	CENTRO_RESULTADO_DESCRICAO("centroResultado", "Descrição Centro Resultado", "centroResultado.descricao",TipoCampoEnum.TEXTO , 60),
	CENTRO_RESULTADO_SITUACAO("centroResultado", "Situação Centro Resultado", "centroResultado.situacao",TipoCampoEnum.TEXTO , 60),
	CENTRO_RESULTADO_IDENTIFICADOR("centroResultado", "Identificador Centro Resultado", "centroResultado.identificadorcentroresultado",TipoCampoEnum.TEXTO , 60);
	
	private TagSEIDecidirCentroResultadoEnum(String entidade, String atributo, String campo, TipoCampoEnum tipoCampo, Integer tamanhoCampo) {
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
