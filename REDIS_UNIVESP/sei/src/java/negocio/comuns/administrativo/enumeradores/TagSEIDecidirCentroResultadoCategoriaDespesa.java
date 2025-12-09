package negocio.comuns.administrativo.enumeradores;

import negocio.interfaces.administrativo.PerfilTagSEIDecidirEnum;

public enum TagSEIDecidirCentroResultadoCategoriaDespesa implements PerfilTagSEIDecidirEnum {
	CENTRO_RESULTADO_CATEGORIA_DESPESA_NOME("centroreusltado/categoriadespesa", "Plano Orçamentário", "planoorcamentario.nome", TipoCampoEnum.TEXTO, 100),
	PRODUTO_SERVICO_DEPARTAMENTO_NOME("centroreusltado/categoriadespesa", "Departamento", "departamento.nome", TipoCampoEnum.TEXTO, 100),
	PRODUTO_SERVICO_PRODUTO_SERVICO("centroreusltado/categoriadespesa", "Produto/Servico", "produtoservico.descricao", TipoCampoEnum.TEXTO, 100),
	PRODUTO_SERVICO_CATEGORIA_DESPESA("centroreusltado/categoriadespesa", "Categoria Despesa", "categoriadespesa.descricao", TipoCampoEnum.TEXTO, 100),
	PRODUTO_SERVICO_CATEGORIA_PRODUTO("centroreusltado/categoriadespesa", "Categoria Produto", "categoriaproduto.nome", TipoCampoEnum.TEXTO, 100),
	PRODUTO_SERVICO_CENTRO_RESULTADO("centroreusltado/categoriadespesa", "Centro Resultado", "centroresultado.descricao", TipoCampoEnum.TEXTO, 100);

	private TagSEIDecidirCentroResultadoCategoriaDespesa(String entidade, String atributo, String campo, TipoCampoEnum tipoCampo, Integer tamanhoCampo) {
		this.tamanhoCampo = tamanhoCampo;
		this.entidade = entidade;
		this.campo = campo;
		this.atributo = atributo;
		this.tipoCampo = tipoCampo;
	}

	private String entidade;
	private String campo;
	private String atributo;
	private TipoCampoEnum tipoCampo;
	private Integer tamanhoCampo;
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

	public String getAtributo() {
		if (atributo == null) {
			atributo = "";
		}
		return atributo;
	}

	public void setAtributo(String atributo) {
		this.atributo = atributo;
	}
	
	/* (non-Javadoc)
	 * @see negocio.interfaces.administrativo.PerfilTagSEIDecidirEnum#getTamanhoCampo()
	 */
	@Override
	public Integer getTamanhoCampo() {

		return tamanhoCampo;
	}

	/* (non-Javadoc)
	 * @see negocio.interfaces.administrativo.PerfilTagSEIDecidirEnum#setTamanhoCampo(java.lang.Integer)
	 */
	@Override
	public void setTamanhoCampo(Integer tamanhoCampo) {
		this.tamanhoCampo = tamanhoCampo;		
	}
}
