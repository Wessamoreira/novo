package negocio.comuns.administrativo.enumeradores;

import negocio.comuns.utilitarias.dominios.SituacaoCheque;
import negocio.interfaces.administrativo.PerfilTagSEIDecidirEnum;

public enum TagSEIDecidirChequeEnum implements PerfilTagSEIDecidirEnum {
	
	CHEQUE_CODIGO("cheque", "Código Cheque", "cheque.codigo",TipoCampoEnum.TEXTO, 10),
	CHEQUE_BANCO("cheque", "Banco Cheque", "cheque.banco",TipoCampoEnum.TEXTO, 20),
	CHEQUE_AGENCIA("cheque", "Agência Cheque", "cheque.agencia",TipoCampoEnum.TEXTO, 20),
	CHEQUE_CONTA_CORRENTE("cheque", "Conta Corrente Cheque", "cheque.contaCorrente",TipoCampoEnum.TEXTO, 20),
	CHEQUE_NUMERO("cheque", "Nº Cheque", "cheque.numero",TipoCampoEnum.TEXTO, 20),
	CHEQUE_VALOR("cheque", "Valor Cheque", "cheque.valor",TipoCampoEnum.DOUBLE, 20),	
	CHEQUE_DATA_EMISSAO("cheque", "Data Emissão Cheque", "cheque.dataemissao", TipoCampoEnum.DATA, 20),
	CHEQUE_DATA_PREVISAO("cheque", "Data Previsão Cheque", "cheque.dataprevisao", TipoCampoEnum.DATA, 20),
	CHEQUE_DATA_COMPENSACAO("cheque", "Data Compensação Cheque", "(case when cheque.situacao = '"+SituacaoCheque.BANCO.getValor()+"' then cheque.databaixa else null end)",TipoCampoEnum.DATA, 20),
	CHEQUE_SITUACAO("cheque", "Situação Cheque", "(case cheque.situacao when '"+SituacaoCheque.BANCO.getValor()+"' then '"+SituacaoCheque.BANCO.getDescricao()+"' "+
			" when '"+SituacaoCheque.EM_CAIXA.getValor()+"' then '"+SituacaoCheque.EM_CAIXA.getDescricao()+"'"+
			" when '"+SituacaoCheque.PAGAMENTO.getValor()+"' then '"+SituacaoCheque.PAGAMENTO.getDescricao()+"'"+
			" when '"+SituacaoCheque.DEVOLVIDO.getValor()+"' then '"+SituacaoCheque.DEVOLVIDO.getDescricao()+"'"+
			" when '"+SituacaoCheque.DEVOLVIDO_AO_SACADO.getValor()+"' then '"+SituacaoCheque.DEVOLVIDO_AO_SACADO.getDescricao()+"'"+
			" when '"+SituacaoCheque.PENDENTE.getValor()+"' then '"+SituacaoCheque.PENDENTE.getDescricao()+"' else '' end) ",TipoCampoEnum.TEXTO, 25),
	CHEQUE_EMITENTE("cheque", "Emitente Cheque", "cheque.sacado",TipoCampoEnum.TEXTO, 20),
	CHEQUE_CPF_CNPJ("cheque", "CPF/CNPJ Cheque", "(case when cheque.emitentepessoajuridica is null or emitentepessoajuridica = false then cheque.cnpj else cheque.cnpj end)",TipoCampoEnum.TEXTO, 20),
	CHEQUE_LOCALIZACAO("cheque", "Localização Cheque", "(select case when cc.contacaixa then cc.numero else 'Banco: '||ba.nome||' AG: '||case when ag.numero is not null then ag.numero else '' end||'-'||ag.digito||' CC: '||cc.numero end "
														+" from contacorrente as cc left join agencia as ag on ag.codigo = cc.agencia left join banco as ba on ag.banco = ba.codigo "
														+"where ((cheque.situacao not in ('PE', 'BA') and cc.codigo = cheque.localizacaocheque) or (cheque.situacao in ('PE', 'BA') and cc.codigo = cheque.contacorrente)))",TipoCampoEnum.TEXTO, 25),
		
	
			
 ;
	private TagSEIDecidirChequeEnum(String entidade, String atributo, String campo, TipoCampoEnum tipoCampo, Integer tamanhoCampo) {
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
