package negocio.comuns.administrativo.enumeradores;

import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.interfaces.administrativo.PerfilTagSEIDecidirEnum;

public enum TagSEIDecidirRecebimentoEnum implements PerfilTagSEIDecidirEnum {
	
	RECEBIMENTO_CODIGO("negociacaoRecebimento","Código Recebimento", "negociacaoRecebimento.codigo",TipoCampoEnum.INTEIRO, 20),
	RECEBIMENTO_DATA("negociacaoRecebimento","Data Recebimento","negociacaoRecebimento.data",TipoCampoEnum.DATA, 20),
	RECEBIMENTO_VALOR("negociacaoRecebimento","Valor Total Recebimento","negociacaoRecebimento.valor",TipoCampoEnum.DOUBLE, 20),
	RECEBIMENTO_VALOR_RECEBIDO("negociacaoRecebimento", "Valor Recebido no Recebimento","negociacaoRecebimento.valorRecebido",TipoCampoEnum.DOUBLE, 20),	
	RECEBIMENTO_TIPO_PESSOA_SIGLA("negociacaoRecebimento", "Tipo Pessoa - Sigla", "negociacaoRecebimento.tipoPessoa", TipoCampoEnum.TEXTO, 10),
	RECEBIMENTO_TIPO_PESSOA_DESCICAO("negociacaoRecebimento", "Tipo Pessoa - Descrição", "(case negociacaoRecebimento.tipoPessoa "+
			" when '"+TipoPessoa.FORNECEDOR.getValor()+"' then '"+TipoPessoa.FORNECEDOR.getDescricao()+"'"+
			" when '"+TipoPessoa.ALUNO.getValor()+"' then '"+TipoPessoa.ALUNO.getDescricao()+"'"+
			" when '"+TipoPessoa.CANDIDATO.getValor()+"' then '"+TipoPessoa.CANDIDATO.getDescricao()+"'"+
			" when '"+TipoPessoa.FUNCIONARIO.getValor()+"' then '"+TipoPessoa.FUNCIONARIO.getDescricao()+"'"+
			" when '"+TipoPessoa.PARCEIRO.getValor()+"' then '"+TipoPessoa.PARCEIRO.getDescricao()+"'"+
			" when '"+TipoPessoa.PROFESSOR.getValor()+"' then '"+TipoPessoa.PROFESSOR.getDescricao()+"'"+
			" when '"+TipoPessoa.REQUERENTE.getValor()+"' then '"+TipoPessoa.REQUERENTE.getDescricao()+"'"+
			" when '"+TipoPessoa.RESPONSAVEL_FINANCEIRO.getValor()+"' then '"+TipoPessoa.RESPONSAVEL_FINANCEIRO.getDescricao()+"' end) ",
			TipoCampoEnum.TEXTO, 20),	
	RECEBIMENTO_SACADO("negociacaoRecebimento", "Sacado Recebimento", "case when negociacaoRecebimento.tipoPessoa = '"+TipoPessoa.PARCEIRO.getValor()+"' then negociacaoRecebimentoParceiro.nome else case when negociacaoRecebimento.tipoPessoa = '"+TipoPessoa.FORNECEDOR.getValor()+"' then negociacaoRecebimentoFornecedor.nome else negociacaoRecebimentoPessoa.nome end end",TipoCampoEnum.TEXTO, 60),	
	RECEBIMENTO_RESPONSAVEL("negociacaoRecebimentoResponsavel", "Responsável Recebimento", "negociacaoRecebimentoResponsavel.nome",TipoCampoEnum.TEXTO, 60),	
	RECEBIMENTO_FORMA_RECEBIMENTO("formapagamentonegociacaorecebimento", "Forma Recebimento", "(array_to_string(array(select distinct formapagamento.nome from formapagamentonegociacaorecebimento inner join formapagamento on formapagamento.codigo = formapagamentonegociacaorecebimento.formapagamento where formapagamentonegociacaorecebimento.negociacaorecebimento = negociacaorecebimento.codigo), ', '))",TipoCampoEnum.TEXTO, 60),	
	;
	private TagSEIDecidirRecebimentoEnum(String entidade, String atributo, String campo, TipoCampoEnum tipoCampo, Integer tamanhoCampo) {
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
