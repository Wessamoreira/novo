package negocio.comuns.administrativo.enumeradores;

import negocio.interfaces.administrativo.PerfilTagSEIDecidirEnum;

public enum TagSEIDecidirRequerimentoEnum implements PerfilTagSEIDecidirEnum {
	REQUERIMENTO_CODIGO("requerimento","Código Requerimento", "requerimento.codigo",TipoCampoEnum.INTEIRO, 20),
	SITUACAO_FINANCEIRA_REQUERIMENTO("requerimento", "Situação Financeira Requerimento", "requerimento.situacaofinanceira", TipoCampoEnum.TEXTO, 30),
	SITUACAO_REQUERIMENTO("requerimento", "Situação Requerimento", "requerimento.situacao", TipoCampoEnum.TEXTO, 30),
	DATA_FINALIZACAO_REQUERIMENTO("requerimento", "Data Finalização Requerimento", "requerimento.datafinalizacao", TipoCampoEnum.DATA, 15),
	DATA_PREVISTA_FINALIZACAO_REQUERIMENTO("requerimento", "Data Prevista Finalização ", "requerimento.dataprevistafinalizacao", TipoCampoEnum.DATA, 15),
	DATA_REQUERIMENTO("requerimento", "Data Requerimento", "requerimento.data", TipoCampoEnum.DATA, 15),
	VALOR_REQUERIMENTO("requerimento", "Valor Requerimento", "requerimento.valor", TipoCampoEnum.BIG_DECIMAL, 15),
	TIPO_PESSOA_REQUERIMENTO("requerimento", "Tipo Pessoa Requerimento", "requerimento.tipopessoa", TipoCampoEnum.TEXTO, 30),
	NOME_PESSOA_REQUERIMENTO("requerimento", "Nome Pessoa Requerimento", "pessoa.nome", TipoCampoEnum.TEXTO, 150),
	CPF_PESSOA("requerimento", "CPF Pessoa", "pessoa.cpf", TipoCampoEnum.TEXTO, 20),
	EMAIL_PESSOA("requerimento", "Email Pessoa", "pessoa.email", TipoCampoEnum.TEXTO, 150),
	CODIGO_TURMA_REQUERIMENTO("requerimento", "Código Turma", "turma.codigo", TipoCampoEnum.INTEIRO, 20),
	IDENTIFICADOR_TURMA_REQUERIMENTO("requerimento", "Identificador Turma", "turma.identificadorturma", TipoCampoEnum.TEXTO, 70),
	CODIGO_DISCIPLINA_REQUERIMENTO("requerimento", "Código Disciplina", "disciplina.codigo", TipoCampoEnum.INTEIRO, 20),
	NOME_DISCIPLINA_REQUERIMENTO("requerimento", "Nome Disciplina", "disciplina.nome", TipoCampoEnum.TEXTO, 150),
	CODIGO_TIPO_REQUERIMENTO("requerimento", "Código Tipo Requerimento", "tiporequerimento.codigo", TipoCampoEnum.INTEIRO, 20),
	NOME_TIPO_REQUERIMENTO("requerimento", "Nome Tipo Requerimento", "tiporequerimento.nome", TipoCampoEnum.TEXTO, 150),
	CODIGO_DEPARTAMENTO_RESPONSAVEL("requerimento", "Código Departamento Responsável", "departamentoresponsavel.codigo", TipoCampoEnum.INTEIRO, 20),
	NOME_DEPARTAMENTO_RESPONSAVEL("requerimento", "Nome Departamento Responsável", "departamentoresponsavel.nome", TipoCampoEnum.TEXTO, 150),
	CODIGO_RESPONSAVEL_REQUERIMENTO("requerimento", "Código Responsável", "responsavel.codigo", TipoCampoEnum.INTEIRO, 20),
	NOME_RESPONSAVEL_REQUERIMENTO("requerimento", "Nome Responsável", "responsavel.nome", TipoCampoEnum.TEXTO, 150),
	;

	private TagSEIDecidirRequerimentoEnum(String entidade, String atributo, String campo, TipoCampoEnum tipoCampo, Integer tamanhoCampo) {
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
