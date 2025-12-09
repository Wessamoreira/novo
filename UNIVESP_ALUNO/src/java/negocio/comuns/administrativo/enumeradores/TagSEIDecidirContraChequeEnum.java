package negocio.comuns.administrativo.enumeradores;

import negocio.interfaces.administrativo.PerfilTagSEIDecidirEnum;

public enum TagSEIDecidirContraChequeEnum implements PerfilTagSEIDecidirEnum {

	CONTRA_CHEQUE_TOTAL_PROVENTO("contraCheque", "Total Provento", "contraCheque.totalProvento",TipoCampoEnum.BIG_DECIMAL , 20),
	CONTRA_CHEQUE_TOTAL_DESCONTO("contraCheque", "Total Desconto", "contraCheque.totalDesconto",TipoCampoEnum.BIG_DECIMAL , 20),
	CONTRA_CHEQUE_TOTAL_RECEBER("contraCheque", "Total Receber", "contraCheque.totalReceber",TipoCampoEnum.BIG_DECIMAL , 20),
	CONTRA_CHEQUE_SALARIO("contraCheque", "Salário", "contraCheque.salario",TipoCampoEnum.BIG_DECIMAL , 20),
	CONTRA_CHEQUE_FGTS("contraCheque", "FGTS", "contraCheque.fgts",TipoCampoEnum.BIG_DECIMAL , 20),
	CONTRA_CHEQUE_DSR("contraCheque", "DSR", "contraCheque.dsr",TipoCampoEnum.BIG_DECIMAL , 20),
	CONTRA_CHEQUE_SAL_FAMILIA("contraCheque", "Salário Família", "contraCheque.salariofamilia",TipoCampoEnum.BIG_DECIMAL , 20),
	CONTRA_CHEQUE_NUMERO_DEPENDENTE("contraCheque", "NúmeroDependentes", "contraCheque.numeroDependenteSalarioFamilia",TipoCampoEnum.INTEIRO , 20),
	CONTRA_CHEQUE_PREVIDENCIA_PROPRIA("contraCheque", "Previdência Própria", "contraCheque.previdenciaPropria",TipoCampoEnum.BIG_DECIMAL , 20),
	CONTRA_CHEQUE_PLANO_SAUDE("contraCheque", "Plano de Saúde", "contraCheque.planoSaude",TipoCampoEnum.BIG_DECIMAL , 20),
	CONTRA_CHEQUE_BASE_CALCULO_INSS("contraCheque", "Base Calculo INSS", "contraCheque.baseCalculoINSS",TipoCampoEnum.BIG_DECIMAL , 20),
	CONTRA_CHEQUE_VALOR_INSS("contraCheque", "Valor INSS", "contraCheque.valorINSS",TipoCampoEnum.BIG_DECIMAL , 20),
	CONTRA_CHEQUE_BASE_CALCULO_IRRF("contraCheque", "Base de Calculo IRRF", "contraCheque.baseCalculoIRRF",TipoCampoEnum.BIG_DECIMAL , 20),
	CONTRA_CHEQUE_FAIXA("contraCheque", "faixa", "contraCheque.faixa",TipoCampoEnum.BIG_DECIMAL , 20),
	CONTRA_CHEQUE_DEDUTIVEL("contraCheque", "Dedutível", "contraCheque.dedutivel",TipoCampoEnum.BIG_DECIMAL , 20),
	CONTRA_CHEQUE_DEPENDENTES("contraCheque", "Número de Dependentes", "contraCheque.numerosDependentes",TipoCampoEnum.INTEIRO , 20),
	CONTRA_CHEQUE_VALOR_DEPENDENTE("contraCheque", "Valor Dependente", "contraCheque.valorDependente",TipoCampoEnum.BIG_DECIMAL , 20),
	CONTRA_CHEQUE_VALOR_IRRF("contraCheque", "Valor IRRF", "contraCheque.valorirrf",TipoCampoEnum.BIG_DECIMAL , 20),
	CONTRA_CHEQUE_IRRF_FERIAS("contraCheque", "Valor IRRF Férias", "contraCheque.valorIRRFFerias",TipoCampoEnum.BIG_DECIMAL , 20),
	CONTRA_CHEQUE_BASE_CALCULO_FERIAS_IRRF("contraCheque", "Base de Cálculo Férias", "contraCheque.baseCalculoirrfFerias",TipoCampoEnum.BIG_DECIMAL , 20),
	CONTRA_CHEQUE_INFORME_RENDIMENTO("contraCheque", "Informe de Rendimento", "contraCheque.informeRendimento",TipoCampoEnum.BIG_DECIMAL , 20),
	CONTRA_CHEQUE_RAIS("contraCheque", "RAIS", "contraCheque.rais",TipoCampoEnum.BIG_DECIMAL , 20);

	private TagSEIDecidirContraChequeEnum(String entidade, String atributo, String campo, TipoCampoEnum tipoCampo, Integer tamanhoCampo) {
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
