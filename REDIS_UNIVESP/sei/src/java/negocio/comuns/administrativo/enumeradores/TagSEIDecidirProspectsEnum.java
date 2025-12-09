package negocio.comuns.administrativo.enumeradores;

import negocio.interfaces.administrativo.PerfilTagSEIDecidirEnum;

public enum TagSEIDecidirProspectsEnum implements PerfilTagSEIDecidirEnum{
	
	PROSPECTS_CODIGO("prospects", "Código Prospects", "prospects.codigo",TipoCampoEnum.INTEIRO , 20),
	PROSPECTS_NOME("prospects","Nome Prospects","prospects.nome",TipoCampoEnum.TEXTO, 40),
	PROSPECTS_CIDADE("prospectscidade", "Cidade Prospects", "prospectscidade.nome",TipoCampoEnum.TEXTO, 30),
	PROSPECTS_ESTADO_NOME("prospectsestado","Nome Estado Prospects", "prospectsestado.nome",TipoCampoEnum.TEXTO, 20),
	PROSPECTS_ESTADO_SIGLA("prospectsestado","Sigla Estado Prospects", "prospectsestado.sigla",TipoCampoEnum.TEXTO, 10),
	PROSPECTS_CPF("prospects", "CPF", "prospects.cpf",TipoCampoEnum.TEXTO, 20),
	PROSPECTS_RG("prospects","RG", "prospects.rg",TipoCampoEnum.TEXTO, 20),
	PROSPECTS_ORGAO_EMISSOR_RG("prospects","Orgão Emissor RG", "prospects.orgaoEmissor",TipoCampoEnum.TEXTO, 20),
	PROSPECTS_ESTADO_EMISSOR_RG("prospects","Estado Emissor RG", "prospects.estadoEmissor",TipoCampoEnum.TEXTO, 20),
	PROSPECTS_DATA_EMISSAO_RG("prospects","Data Emissão RG", "prospects.dataEmissaoRG",TipoCampoEnum.DATA, 20),
	PROSPECTS_CERTIFICADO_MILITAR("prospects","Certificado Militar", "prospects.certificadoMilitar",TipoCampoEnum.TEXTO, 20),
	PROSPECTS_DATA_EXPEDICAO_CERTIFICADO_MILITAR("prospects","Data Exp. Certificado Militar", "prospects.dataexpedicaocertificadoMilitar",TipoCampoEnum.DATA, 20),
	PROSPECTS_ORGAO_EXPEDIDOR_CERTIFICADO_MILITAR("prospects","Orgão Exp. Certificado Militar", "prospects.orgaoexpedidorcertificadoMilitar",TipoCampoEnum.TEXTO, 20),
	PROSPECTS_TITULO_ELEITORAL("prospects","Título Eleitoral", "prospects.tituloEleitoral",TipoCampoEnum.TEXTO, 20),
	PROSPECTS_ZONA_ELEITORAL("prospects","Zona Eleitoral", "prospects.zonaEleitoral",TipoCampoEnum.TEXTO, 20),
	PROSPECTS_DATA_EXPEDICAO_TITULO_ELEITORAL("prospects","Data Exp. Título Eleitoral", "prospects.dataexpedicaotituloEleitoral",TipoCampoEnum.DATA, 20),
	PROSPECTS_DATA_NASCIMENTO("prospects","Data Nascimento", "prospects.datanascimento",TipoCampoEnum.DATA, 20),
	PROSPECTS_CEP("prospects", "Cep", "prospects.cep",TipoCampoEnum.TEXTO, 20),
	PROSPECTS_ENDERECO("prospects", "Endereço", "prospects.endereco",TipoCampoEnum.TEXTO, 40),
	PROSPECTS_SETOR("prospects", "Setor Endereço", "prospects.setor",TipoCampoEnum.TEXTO, 30),
	PROSPECTS_NUMERO("prospects", "Número Endereço", "prospects.numero",TipoCampoEnum.TEXTO, 10),
	PROSPECTS_COMPLEMENTO("prospects", "Complemento Endereço", "prospects.complemento",TipoCampoEnum.TEXTO, 30),
	PROSPECTS_EMAIL("prospects","E-mail Prospects", "prospects.emailprincipal",TipoCampoEnum.TEXTO, 25),
	PROSPECTS_TELEFONE_RES("prospects","Telefone Residencial","prospects.telefoneresidencial",TipoCampoEnum.TEXTO, 20),
	PROSPECTS_TELEFONE_COMERCIAL("prospects","Telefone Comercial","prospects.telefonecomercial",TipoCampoEnum.TEXTO, 20),
	PROSPECTS_TELEFONE_RECADO("prospects","Telefone Comercial","prospects.telefonerecado",TipoCampoEnum.TEXTO, 20),
	PROSPECTS_CELULAR("prospects","Celular", "prospects.celular",TipoCampoEnum.TEXTO, 20),
	PROSPECTS_TIPOORIGEMCADASTRO("prospects","Tipo Origem Cadastro", "prospects.tipoOrigemCadastro",TipoCampoEnum.TEXTO, 20)
	;
	
	private TagSEIDecidirProspectsEnum(String entidade, String atributo, String campo, TipoCampoEnum tipoCampo, Integer tamanhoCampo) {
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
