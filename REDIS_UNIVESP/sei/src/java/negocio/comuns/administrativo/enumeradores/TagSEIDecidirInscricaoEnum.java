package negocio.comuns.administrativo.enumeradores;

import negocio.interfaces.administrativo.PerfilTagSEIDecidirEnum;

public enum TagSEIDecidirInscricaoEnum implements PerfilTagSEIDecidirEnum{
	
	INSCRICAO_CODIGO("inscricao", "Código Inscrição", "inscricao.codigo",TipoCampoEnum.INTEIRO , 20),
	INSCRICAO_NOME_CANDIDATO("inscricao", "Nome Candidato", "candidato.nome", TipoCampoEnum.TEXTO, 60),
	INSCRICAO_SEXO_CANDIDATO("inscricao", "Sexo Candidato", "candidato.sexo", TipoCampoEnum.TEXTO, 40),
	INSCRICAO_CPF_CANDIDATO("inscricao", "CPF Candidato", "candidato.CPF", TipoCampoEnum.TEXTO, 20),
	INSCRICAO_CELULAR_CANDIDATO("inscricao", "Telefone Candidato", "candidato.celular", TipoCampoEnum.TEXTO, 20),
	INSCRICAO_UNIDADE_ENSINO("inscricao", "Unidade Ensino", "unidadeEnsino.nome", TipoCampoEnum.TEXTO, 40),
	INSCRICAO_CURSO_OPCAO_1("inscricao", "Curso Opção 1", "cursoOpcao1.nome", TipoCampoEnum.TEXTO, 40),
	INSCRICAO_CURSO_OPCAO_2("inscricao", "Curso Opção 2", "cursoOpcao2.nome", TipoCampoEnum.TEXTO, 40),
	INSCRICAO_CURSO_OPCAO_3("inscricao", "Curso Opção 3", "cursoOpcao3.nome", TipoCampoEnum.TEXTO, 40),
	INSCRICAO_EIXO_CURSO_OPCAO_1("inscricao", "Eixo Curso Opção 1", "eixoCursoOpcao1.nome", TipoCampoEnum.TEXTO, 40),
	INSCRICAO_EIXO_CURSO_OPCAO_2("inscricao", "Eixo Curso Opção 2", "eixoCursoOpcao2.nome", TipoCampoEnum.TEXTO, 40),
	INSCRICAO_EIXO_CURSO_OPCAO_3("inscricao", "Eixo Curso Opção 3", "eixoCursoOpcao3.nome", TipoCampoEnum.TEXTO, 40),
	INSCRICAO_SITUACAO_INSCRICAO("inscricao", "Situação Inscrição", "inscricao.situacaoInscricao", TipoCampoEnum.TEXTO, 40),
	INSCRICAO_SITUACAO_FINANCEIRA("inscricao", "Situação Financeira", "case when inscricao.situacao = 'CO' then 'Confirmada' else 'Pendente Financeiramente' end ", TipoCampoEnum.TEXTO, 40),
	INSCRICAO_DATA("inscricao", "Data Inscrição", "inscricao.data", TipoCampoEnum.DATA, 40)
	
	;
	
	private TagSEIDecidirInscricaoEnum(String entidade, String atributo, String campo, TipoCampoEnum tipoCampo, Integer tamanhoCampo) {
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
