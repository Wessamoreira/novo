package negocio.comuns.administrativo.enumeradores;

import negocio.comuns.utilitarias.dominios.OrigemContaPagar;
import negocio.comuns.utilitarias.dominios.SituacaoFinanceira;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.comuns.utilitarias.dominios.TipoSacado;
import negocio.interfaces.administrativo.PerfilTagSEIDecidirEnum;

public enum TagSEIDecidirContaPagarEnum implements PerfilTagSEIDecidirEnum {
	CONTA_PAGAR_CODIGO("contapagar","Código", "contapagar.codigo",TipoCampoEnum.INTEIRO, 20),		
	CONTA_PAGAR_DATA_VENCIMENTO("contapagar","Data Vencimento", "contapagar.datavencimento",TipoCampoEnum.DATA, 20),
	CONTA_PAGAR_DATA_FATO_GERADOR("contapagar","Data Fato Gerador", "to_char(contapagar.datafatogerador, 'MM/yyyy')",TipoCampoEnum.TEXTO, 15),		
	CONTA_PAGAR_DATA_PAGAMENTO("contapagar", "Data Pagamento", " (select max(np.data) from negociacaopagamento np inner join contapagarnegociacaopagamento cpnp on cpnp.negociacaocontapagar = np.codigo and contapagar.codigo = cpnp.contaPagar) ",TipoCampoEnum.DATA, 20),		
	CONTA_PAGAR_DESCRICAO("contapagar", "Descrição", "contapagar.descricao",TipoCampoEnum.TEXTO, 30),		
	CONTA_PAGAR_PARCELA("contapagar","Parcela", "contapagar.parcela",TipoCampoEnum.TEXTO, 20),		
	CONTA_PAGAR_NR_DOCUMENTO("contapagar", "Número Documento", "contapagar.nrdocumento",TipoCampoEnum.TEXTO, 20),	
	
	CONTA_PAGAR_CATEGORIA_DESPESA_DESCRICAO("contapagarcategoriadespesa", "Descrição Categoria Despesa", " (select array_to_string(array_agg(distinct categoriadespesa.descricao order by categoriadespesa.descricao), ', ') from centroresultadoorigem inner join categoriadespesa on categoriadespesa.codigo= centroresultadoorigem.categoriadespesa  where tipoCentroResultadoOrigem = 'CONTA_PAGAR' " + 
			" and centroresultadoorigem.codOrigem = contapagar.codigo::varchar ) ",TipoCampoEnum.TEXTO, 30),	
	CONTA_PAGAR_CATEGORIA_DESPESA_IDENTIFICADOR("contapagarcategoriadespesa","Identificador Categoria Despesa", "(select array_to_string(array_agg(distinct categoriadespesa.identificadorCategoriaDespesa order by categoriadespesa.identificadorCategoriaDespesa), ', ') from centroresultadoorigem inner join categoriadespesa on categoriadespesa.codigo= centroresultadoorigem.categoriadespesa  where tipoCentroResultadoOrigem = 'CONTA_PAGAR' " + 
			" and centroresultadoorigem.codOrigem = contapagar.codigo::varchar )",TipoCampoEnum.TEXTO, 20),	
	CONTA_PAGAR_SACADO("contapagar", "Sacado", "(case contapagar.tipoSacado "+
			" when '"+TipoSacado.FORNECEDOR.getValor()+"' then contapagarfornecedor.nome "+
			" when '"+TipoSacado.ALUNO.getValor()+"' then contapagarpessoa.nome "+			
			" when '"+TipoSacado.FUNCIONARIO_PROFESSOR.getValor()+"' then contapagarfuncionariopessoa.nome "+
			" when '"+TipoSacado.PARCEIRO.getValor()+"' then contapagarparceiro.nome "+			
			" when '"+TipoSacado.BANCO.getValor()+"' then contapagarbanco.nome "+
			" when '"+TipoSacado.OPERADORA_CARTAO.getValor()+"' then contapagaroperadoracartao.nome "+
			" when '"+TipoSacado.RESPONSAVEL_FINANCEIRO.getValor()+"' then contapagarresponsavelfinanceiro.nome else '' end) "
			,TipoCampoEnum.TEXTO, 40),
	CONTA_PAGAR_SITUACAO_SIGLA("contapagar", "Situação - Sigla", "contapagar.situacao", TipoCampoEnum.TEXTO, 10),
    CONTA_PAGAR_SITUACAO_DESCRICAO("contapagar", "Situação - Descrição", "(case contapagar.situacao"
			+ " when '"+SituacaoFinanceira.A_PAGAR.getValor()+"' then '"+SituacaoFinanceira.A_PAGAR.getDescricao()+"' "
			+ " when '"+SituacaoFinanceira.PAGO.getValor()+"' then '"+SituacaoFinanceira.PAGO.getDescricao()+"' "
			+ " when '"+SituacaoFinanceira.NEGOCIADO.getValor()+"' then '"+SituacaoFinanceira.NEGOCIADO.getDescricao()+"' "
			+ " when '"+SituacaoFinanceira.CANCELADO_FINANCEIRO.getValor()+"' then '"+SituacaoFinanceira.CANCELADO_FINANCEIRO.getDescricao()+"' "
			+ " else '"+SituacaoFinanceira.PAGO_PARCIAL.getDescricao()+"' end)", TipoCampoEnum.TEXTO, 20),
	CONTA_PAGAR_TIPO_PESSOA_SIGLA("contapagar", "Tipo Sacado - Sigla",  "contapagar.tipoSacado ", TipoCampoEnum.TEXTO, 10),
	CONTA_PAGAR_TIPO_PESSOA_DESCRICAO("contapagar", "Tipo Sacado - Descrição",  "(case contapagar.tipoSacado "+
			" when '"+TipoSacado.FORNECEDOR.getValor()+"' then '"+TipoPessoa.FORNECEDOR.getDescricao()+"'"+
			" when '"+TipoSacado.ALUNO.getValor()+"' then '"+TipoSacado.ALUNO.getDescricao()+"'"+
			" when '"+TipoSacado.FUNCIONARIO_PROFESSOR.getValor()+"' then '"+TipoSacado.FUNCIONARIO_PROFESSOR.getDescricao()+"'"+			
			" when '"+TipoSacado.PARCEIRO.getValor()+"' then '"+TipoSacado.PARCEIRO.getDescricao()+"'"+			
			" when '"+TipoSacado.BANCO.getValor()+"' then '"+TipoSacado.BANCO.getDescricao()+"'"+
			" when '"+TipoSacado.OPERADORA_CARTAO.getValor()+"' then '"+TipoSacado.OPERADORA_CARTAO.getDescricao()+"'"+
			" when '"+TipoSacado.RESPONSAVEL_FINANCEIRO.getValor()+"' then '"+TipoSacado.RESPONSAVEL_FINANCEIRO.getDescricao()+"' else '' end) ",
			TipoCampoEnum.TEXTO, 20),	
			CONTA_PAGAR_TIPO_ORIGEM_SIGLA("contapagar", "Tipo Origem - Sigla",  "contapagar.tipoOrigem", TipoCampoEnum.TEXTO,10),
			CONTA_PAGAR_TIPO_ORIGEM_DESCRICAO("contapagar", "Tipo Origem - Descrição",  "(case contapagar.tipoOrigem "+
					" when '"+OrigemContaPagar.COMPRA.getValor()+"' then '"+OrigemContaPagar.COMPRA.getDescricao()+"'"+
					" when '"+OrigemContaPagar.SERVICO.getValor()+"' then '"+OrigemContaPagar.SERVICO.getDescricao()+"'"+
					" when '"+OrigemContaPagar.MULTA.getValor()+"' then '"+OrigemContaPagar.MULTA.getDescricao()+"'"+			
					" when '"+OrigemContaPagar.REQUISICAO.getValor()+"' then '"+OrigemContaPagar.REQUISICAO.getDescricao()+"'"+			
					" when '"+OrigemContaPagar.CONTRATO_DESPESA.getValor()+"' then '"+OrigemContaPagar.CONTRATO_DESPESA.getDescricao()+"'"+
					" when '"+OrigemContaPagar.PROVISAO_DE_CUSTO.getValor()+"' then '"+OrigemContaPagar.PROVISAO_DE_CUSTO.getDescricao()+"'"+
					" when '"+OrigemContaPagar.RESTITUICAO_CONVENIO_MATRICULA_PERIODO.getValor()+"' then '"+OrigemContaPagar.RESTITUICAO_CONVENIO_MATRICULA_PERIODO.getDescricao()+"'"+
					" when '"+OrigemContaPagar.REGISTRO_MANUAL.getValor()+"' then '"+OrigemContaPagar.REGISTRO_MANUAL.getDescricao()+"' else '' end) ",
					TipoCampoEnum.TEXTO, 20),	
	CONTA_PAGAR_VALOR_PAGAR("contapagar", "Valor", "contapagar.valor",TipoCampoEnum.DOUBLE, 20),	
	CONTA_PAGAR_VALOR_PAGO("contapagar", "Valor Pago", "contapagar.valorPago",TipoCampoEnum.DOUBLE, 20),
	CONTA_PAGAR_VALOR_JURO("contapagar", "Juro", "contapagar.juro",TipoCampoEnum.DOUBLE,20),
	CONTA_PAGAR_VALOR_MULTA("contapagar", "Multa", "contapagar.multa",TipoCampoEnum.DOUBLE,20),	
	CONTA_PAGAR_VALOR_DESCONTO("contapagar", "Desconto", "contapagar.desconto",TipoCampoEnum.DOUBLE,20),	
	CONTA_PAGAR_RESPONSAVEL_PAGAMENTO("negociacaoPagamentoResponsavel", "Responsável Pagamento", "negociacaoPagamentoResponsavel.nome",TipoCampoEnum.TEXTO,40),	
	CONTA_PAGAR_CONTA_CORRENTE("contacorrente", "Conta Corrente", "(select case when cc.contacaixa then cc.numero else 'Banco: '||ba.nome||' AG: '||case when ag.numero is not null then ag.numero else '' end||'-'||ag.digito||' CC: '||cc.numero end "
																 +" from contacorrente as cc left join agencia as ag on ag.codigo = cc.agencia left join banco as ba on ag.banco = ba.codigo"
																 +" where cc.codigo = contapagar.contacorrente  )", TipoCampoEnum.TEXTO, 30),
	CONTA_PAGAR_FORMAS_DE_PAGAMENTO("contapagarpagamento", "Forma Pagamento - Lista", "(array_to_string(array(select distinct formapagamento.nome from contapagarpagamento inner join formapagamento on formapagamento.codigo = contapagarpagamento.formapagamento where contapagarpagamento.contapagar = contapagar.codigo and contapagarpagamento.formapagamentonegociacaopagamento is not null and contapagarpagamento.tipopagamento = 'CR'), chr(10)))",TipoCampoEnum.TEXTO,30),	
	CONTA_PAGAR_NOME_FORMA_PAGAMENTO("formapagamento", "Forma Pagamento - Individual", "formapagamento.nome",TipoCampoEnum.TEXTO, 20),
	CONTA_PAGAR_DATA_COMPENSACAO_FORMA_PAGAMENTO("contapagarpagamento", "Data Compensação Forma Pgto", "(case when formapagamento.tipo = 'CH' then cheque.databaixa else negociacaopagamento.data end) ", TipoCampoEnum.DATA, 20),
	CONTA_PAGAR_VALOR_UTILIZADO_FORMA_PAGAMENTO("contapagarpagamento", "Valor Utilizado Forma Pagto", "contapagarpagamento.valortotalpagamento",TipoCampoEnum.DOUBLE,20),

	CONTA_PAGAR_NOME_BANCO_RECEBIMENTO("bancorecebimento", "Nome Banco", "banco.nome",TipoCampoEnum.TEXTO,50),
	CONTA_PAGAR_NR_BANCO_BANCO_RECEBIMENTO("bancorecebimento", "Número Banco", "banco.nrBanco",TipoCampoEnum.TEXTO,20);

	private TagSEIDecidirContaPagarEnum(String entidade, String atributo, String campo, TipoCampoEnum tipoCampo, Integer tamanhoCampo) {
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
