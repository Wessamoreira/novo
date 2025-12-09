package negocio.comuns.administrativo.enumeradores;

import negocio.interfaces.administrativo.PerfilTagSEIDecidirEnum;

public enum TagSEIDecidirPlanoOrcamentario implements PerfilTagSEIDecidirEnum {
	PLANO_ORCAMENTARIO_CATEGORIA_DESPESA("categoriadespesa", "Descrição", "categoriadespesa.descricao", TipoCampoEnum.TEXTO, 100),
	PLANO_ORCAMENTARIO_SOMA_TOTAL_SOLICITADO("requisicaoitem", "Total Solicitado", "itemsolicitacaoorcamentoplanoorcamentario.valor", TipoCampoEnum.DOUBLE, 20),
	PLANO_ORCAMENTARIO_TOTAL_ENTREGUE("requisicaoitem", "Total Entregue", "(requisicaoitem.valorunitario * requisicaoitem.quantidadeentregue) ", TipoCampoEnum.DOUBLE, 20),
	PLANO_ORCAMENTARIO_PERCENTUAL("detalhamentoplanoorcamentario", "Percentual", "((itemsolicitacaoorcamentoplanoorcamentario.valor * 100.0) / solicitacaoorcamentoplanoorcamentario.valortotalsolicitado)::numeric(20, 2)", TipoCampoEnum.DOUBLE, 20),
	PLANO_ORCAMENTARIO_TOTAL_SOLICITADO("detalhamentoplanoorcamentario", "Valor Solicitado", "itemsolicitacaoorcamentoplanoorcamentario.valor", TipoCampoEnum.DOUBLE, 20),
	PLANO_ORCAMENTARIO_VALOR_CONSUMIDO("detalhamentoplanoorcamentario", "Valor Consumido", "sum(requisicaoitem.quantidadeautorizada::numeric(20,2)  * requisicaoitem.valorunitario::numeric(20,2) )::numeric(20,2)", TipoCampoEnum.DOUBLE, 20),
	PLANO_ORCAMENTARIO_VALOR_AUTORIZADO("detalhamentoplanoorcamentario", "Valor Autorizado", "itemsolicitacaoorcamentoplanoorcamentario.valor", TipoCampoEnum.DOUBLE, 20),
	PLANO_ORCAMENTARIO_VALOR_RESTANTE("itemsolicitacaoorcamentoplanoorcamentario", "Valor Restante", "itemsolicitacaoorcamentoplanoorcamentario.valor - coalesce (sum(requisicaoitem.quantidadeautorizada::numeric(20,2)  * requisicaoitem.valorunitario::numeric(20,2) )::numeric(20,2), 0.0)", TipoCampoEnum.DOUBLE, 20);

	private TagSEIDecidirPlanoOrcamentario(String entidade, String atributo, String campo, TipoCampoEnum tipoCampo, Integer tamanhoCampo) {
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
