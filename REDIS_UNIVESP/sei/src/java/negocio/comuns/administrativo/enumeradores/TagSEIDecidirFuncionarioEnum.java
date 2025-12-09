package negocio.comuns.administrativo.enumeradores;

import negocio.interfaces.administrativo.PerfilTagSEIDecidirEnum;

public enum TagSEIDecidirFuncionarioEnum implements PerfilTagSEIDecidirEnum{	
	
	FUNCIONARIO_CODIGO("funcionario", "Código Funcionário", "funcionario.codigo",TipoCampoEnum.INTEIRO , 20),
	FUNCIONARIO_MATRICULA("funcionario","Matrícula Funcionário","funcionario.matricula",TipoCampoEnum.TEXTO, 40),
	FUNCIONARIO_DATAADMISSAO("funcionario","Data Admissão Funcionário", "funcionario.dataAdmissao",TipoCampoEnum.DATA, 20),	
	FUNCIONARIO_CIDADE("funcionariopessoacidade", "Cidade Funcionário", "funcionariopessoacidade.nome",TipoCampoEnum.TEXTO, 30),
	FUNCIONARIO_ESTADO_NOME("funcionariopessoaestado","Nome Estado Funcionário", "funcionariopessoaestado.nome",TipoCampoEnum.TEXTO, 20),
	FUNCIONARIO_ESTADO_SIGLA("funcionariopessoaestado","Sigla Estado Funcionário", "funcionariopessoaestado.sigla",TipoCampoEnum.TEXTO, 10),
	FUNCIONARIO_CPF("funcionariopessoa", "CPF Funcionário", "funcionariopessoa.cpf",TipoCampoEnum.TEXTO, 20),
	FUNCIONARIO_RG("funcionariopessoa","RG Funcionário", "funcionariopessoa.rg",TipoCampoEnum.TEXTO, 20),
	FUNCIONARIO_DATA_NASCIMENTO("funcionariopessoa","Data Nascimento Funcionário", "funcionariopessoa.datanasc",TipoCampoEnum.DATA, 20),
	FUNCIONARIO_TELEFONE_RES("funcionariopessoa","Telefone Residencial Funcionário","funcionariopessoa.telefoneRes",TipoCampoEnum.TEXTO, 20),
	FUNCIONARIO_TELEFONE_COMERCIAL("funcionariopessoa","Telefone Comercial Funcionário","funcionariopessoa.telefoneComer",TipoCampoEnum.TEXTO, 20),
	FUNCIONARIO_TELEFONE_RECADO("funcionariopessoa","Telefone Comercial Funcionário","funcionariopessoa.telefoneRecado",TipoCampoEnum.TEXTO, 20),
	FUNCIONARIO_CELULAR("funcionariopessoa","Celular Funcionário", "funcionariopessoa.celular",TipoCampoEnum.TEXTO, 20)
	;
	private TagSEIDecidirFuncionarioEnum(String entidade, String atributo, String campo, TipoCampoEnum tipoCampo, Integer tamanhoCampo) {
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
