package negocio.comuns.administrativo.enumeradores;

import negocio.interfaces.administrativo.PerfilTagSEIDecidirEnum;

public enum TagSEIDecidirFuncionarioCargoEnum implements PerfilTagSEIDecidirEnum {

	FUNCIONARIO_CARGO_NOME("funcionarioCargo", "Nome", "p.nome",TipoCampoEnum.TEXTO , 50),
	FUNCIONARIO_CARGO_RG("funcionarioCargo", "RG", "p.rg",TipoCampoEnum.TEXTO , 20),
	FUNCIONARIO_CARGO_CPF("funcionarioCargo", "CPF", "p.cpf",TipoCampoEnum.TEXTO, 20),
	FUNCIONARIO_CARGO_FORMA_CONTRATACAO("funcionarioCargo", "Forma Contratação", "fc.formaContratacao",TipoCampoEnum.TEXTO , 30),
	FUNCIONARIO_CARGO_SITUACAO("funcionarioCargo", "Situação do Funcionário", "fc.situacaoFuncionario",TipoCampoEnum.TEXTO , 20),
	FUNCIONARIO_CARGO_SECAOFOLHA("funcionarioCargo", "Seção da Folha Pagamento", "fc.secaoFolhaPagamento",TipoCampoEnum.TEXTO , 20),
	FUNCIONARIO_CARGO_MATRICULACARGO("funcionarioCargo", "Matrícula Funcionario Cargo", "fc.matriculacargo",TipoCampoEnum.TEXTO , 20),
	FUNCIONARIO_CARGO_CARGO("cargo", "Cargo", "c.nome",TipoCampoEnum.TEXTO , 50),
	FUNCIONARIO_CARGO_DATAADMISSAO("funcionarioCargo", "Data de Admissão", "fc.dataadmissao",TipoCampoEnum.DATA , 20),
	FUNCIONARIO_CARGO_CODIGO("funcionarioCargo", "Código Funcionário", "fc.codigo",TipoCampoEnum.INTEIRO , 20),

	FUNCIONARIO_NUMERO_BANCO_RECEBIMENTO("numerobancorecebimento", "Número Banco Recebimento", "f.numerobancorecebimento",TipoCampoEnum.TEXTO , 20),
	FUNCIONARIO_NUMERO_AGENCIA_RECEBIMENTO("numeroagenciarecebimento", "Número Ageência Recebimento", "f.numeroagenciarecebimento",TipoCampoEnum.TEXTO , 20),
	FUNCIONARIO_NOME_BANCO("nomebanco", "Nome Banco", "f.nomebanco",TipoCampoEnum.TEXTO , 50),
	FUNCIONARIO_CONTA_CORRENTE_RECEBIMENTO("contacorrenterecebimento", "Conta Corrente Recebimento", "f.contacorrenterecebimento",TipoCampoEnum.TEXTO , 20),
	FUNCIONARIO_CODIGO("funcionario", "Código Funcionário", "f.codigo",TipoCampoEnum.INTEIRO , 20);

	private TagSEIDecidirFuncionarioCargoEnum(String entidade, String atributo, String campo, TipoCampoEnum tipoCampo, Integer tamanhoCampo) {
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