package negocio.comuns.administrativo.enumeradores;

import negocio.interfaces.administrativo.PerfilTagSEIDecidirEnum;

public enum TagSEIDecidirRequisicao implements PerfilTagSEIDecidirEnum {
	REQUISICAO_CODIGO("requisicao", "Código", "requisicao.codigo", TipoCampoEnum.INTEIRO, 20),
	REQUISICAO_VALOR_TOTAL_APROVADO("requisicaoitem", "Valor Total Aprovado", "(requisicaoitem.valorunitario * requisicaoitem.quantidadeautorizada)", TipoCampoEnum.DOUBLE, 20),
	REQUISICAO_VALOR_TOTAL_ENTREGUE("requisicaoitem", "Valor Total Entregue", "(requisicaoitem.valorunitario * requisicaoitem.quantidadeentregue)", TipoCampoEnum.DOUBLE, 20),
	REQUISICAO_VALOR_TOTAL_SOLICITADO("requisicaoitem", "Valor Total Solicitado", "(requisicaoitem.valorunitario * requisicaoitem.quantidadesolicitada)", TipoCampoEnum.DOUBLE, 20),
	REQUISICAO_QUANTIDADE_TOTAL_APROVADO("requisicaoitem", "Quantidade Total Aprovado", "requisicaoitem.quantidadeautorizada", TipoCampoEnum.DOUBLE, 20),
	REQUISICAO_QUANTIDADE_TOTAL_ENTREGUE("requisicaoitem", "Quantidade Total Entregue", "requisicaoitem.quantidadeentregue", TipoCampoEnum.DOUBLE, 20),
	REQUISICAO_QUANTIDADE_TOTAL_SOLICITADO("requisicaoitem", "Quantidade Total Solicitado", "requisicaoitem.quantidadesolicitada", TipoCampoEnum.DOUBLE, 20);

	private TagSEIDecidirRequisicao(String entidade, String atributo, String campo, TipoCampoEnum tipoCampo, Integer tamanhoCampo) {
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
