package negocio.comuns.administrativo.enumeradores;

import negocio.interfaces.administrativo.PerfilTagSEIDecidirEnum;

public enum TagSEIDecidirRecursosHumanosEnum implements PerfilTagSEIDecidirEnum {

	TOTAL_PROVENTO("contraCheque", "Total Provento", "contraCheque.totalProvento",TipoCampoEnum.BIG_DECIMAL , 20),
	TOTAL_DESCONTO("contraCheque", "Total Desconto", "contraCheque.totalDesconto",TipoCampoEnum.BIG_DECIMAL , 20),
	TOTAL_RECEBER("contraCheque", "Total Receber", "contraCheque.totalReceber",TipoCampoEnum.BIG_DECIMAL , 20),
	SALARIO("contraCheque", "Salário", "contraCheque.salario",TipoCampoEnum.BIG_DECIMAL , 20),
	FGTS("contraCheque", "FGTS", "contraCheque.fgts",TipoCampoEnum.BIG_DECIMAL , 20),
	DSR("contraCheque", "DSR", "contraCheque.dsr",TipoCampoEnum.BIG_DECIMAL , 20),
	SAL_FAMILIA("contraCheque", "Salário Família", "contraCheque.salariofamilia",TipoCampoEnum.BIG_DECIMAL , 20),
	NUMERO_DEPENDENTE("contraCheque", "NúmeroDependentes", "contraCheque.numeroDependenteSalarioFamilia",TipoCampoEnum.INTEIRO , 20),
	PREVIDENCIA_PROPRIA("contraCheque", "Previdência Própria", "contraCheque.previdenciaPropria",TipoCampoEnum.BIG_DECIMAL , 20),
	PLANO_SAUDE("contraCheque", "Plano de Saúde", "contraCheque.planoSaude",TipoCampoEnum.BIG_DECIMAL , 20),
	BASE_CALCULO_INSS("contraCheque", "Base Calculo INSS", "contraCheque.baseCalculoINSS",TipoCampoEnum.BIG_DECIMAL , 20),
	VALOR_INSS("contraCheque", "Valor INSS", "contraCheque.valorINSS",TipoCampoEnum.BIG_DECIMAL , 20),
	BASE_CALCULO_IRRF("contraCheque", "Base de Calculo IRRF", "contraCheque.baseCalculoIRRF",TipoCampoEnum.BIG_DECIMAL , 20),
	FAIXA("contraCheque", "faixa", "contraCheque.faixa",TipoCampoEnum.BIG_DECIMAL , 20),
	DEDUTIVEL("contraCheque", "Dedutível", "contraCheque.dedutivel",TipoCampoEnum.BIG_DECIMAL , 20),
	DEPENDENTES("contraCheque", "Número de Dependentes", "contraCheque.numerosDependentes",TipoCampoEnum.INTEIRO , 20),
	VALOR_DEPENDENTE("contraCheque", "Valor Dependente", "contraCheque.valorDependente",TipoCampoEnum.BIG_DECIMAL , 20),
	VALOR_IRRF("contraCheque", "Valor IRRF", "contraCheque.valorirrf",TipoCampoEnum.BIG_DECIMAL , 20),
	IRRF_FERIAS("contraCheque", "Valor IRRF Férias", "contraCheque.valorIRRFFerias",TipoCampoEnum.BIG_DECIMAL , 20),
	BASE_CALCULO_FERIAS_IRRF("contraCheque", "Base de Cálculo Férias", "contraCheque.baseCalculoirrfFerias",TipoCampoEnum.BIG_DECIMAL , 20),
	INFORME_RENDIMENTO("contraCheque", "Informe de Rendimento", "contraCheque.informeRendimento",TipoCampoEnum.BIG_DECIMAL , 20),
	RAIS("contraCheque", "RAIS", "contraCheque.rais",TipoCampoEnum.BIG_DECIMAL , 20),
	NOME_FUNCIONARIO("funcionarioCargo", "Nome", "p.nome",TipoCampoEnum.TEXTO , 50),
	CPF("funcionarioCargo", "CPF", "p.cpf",TipoCampoEnum.TEXTO, 20),
	FORMA_CONTRATACAO("funcionarioCargo", "Forma de Contratação", "fc.formaContratacao",TipoCampoEnum.BIG_DECIMAL , 20),
	SITUACAO_FUNCIONARIO("funcionarioCargo", "Situação do Funcionário", "fc.situacaoFuncionario",TipoCampoEnum.BIG_DECIMAL , 20),
	SECAO_FOLHA("funcionarioCargo", "Seção da Folha Pagamento", "fc.secaoFolhaPagamento",TipoCampoEnum.BIG_DECIMAL , 20),
	MATRICULA_CARGO("funcionarioCargo", "Matrícula Funcionario Cargo", "fc.matriculacargo",TipoCampoEnum.BIG_DECIMAL , 20);

	private TagSEIDecidirRecursosHumanosEnum(String entidade, String atributo, String campo, TipoCampoEnum tipoCampo, Integer tamanhoCampo) {
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
