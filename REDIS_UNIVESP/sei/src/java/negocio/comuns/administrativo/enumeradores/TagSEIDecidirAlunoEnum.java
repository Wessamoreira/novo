package negocio.comuns.administrativo.enumeradores;

import negocio.interfaces.administrativo.PerfilTagSEIDecidirEnum;

public enum TagSEIDecidirAlunoEnum implements PerfilTagSEIDecidirEnum {
	
	ALUNO_CODIGO("aluno","Código Aluno", "aluno.codigo",TipoCampoEnum.INTEIRO, 20),
	ALUNO_NOME("aluno","Nome Aluno","aluno.nome",TipoCampoEnum.TEXTO, 40),
	ALUNO_CIDADE("alunocidade", "Cidade Aluno", "alunocidade.nome",TipoCampoEnum.TEXTO, 30),
	ALUNO_ESTADO_NOME("alunoestado","Nome Estado Aluno", "alunoestado.nome",TipoCampoEnum.TEXTO, 20),
	ALUNO_ESTADO_SIGLA("alunoestado","Sigla Estado Aluno", "alunoestado.sigla",TipoCampoEnum.TEXTO, 10),
	ALUNO_CPF("aluno", "CPF", "aluno.cpf",TipoCampoEnum.TEXTO, 20),
	ALUNO_RG("aluno","RG", "aluno.rg",TipoCampoEnum.TEXTO, 20),
	ALUNO_ORGAO_EMISSOR_RG("aluno","Orgão Emissor RG", "aluno.orgaoEmissor",TipoCampoEnum.TEXTO, 20),
	ALUNO_ESTADO_EMISSOR_RG("aluno","Estado Emissor RG", "aluno.estadoEmissor",TipoCampoEnum.TEXTO, 20),
	ALUNO_DATA_EMISSAO_RG("aluno","Data Emissão RG", "aluno.dataEmissaoRG",TipoCampoEnum.DATA, 20),
	ALUNO_CERTIFICADO_MILITAR("aluno","Certificado Militar", "aluno.certificadoMilitar",TipoCampoEnum.TEXTO, 20),
	ALUNO_DATA_EXPEDICAO_CERTIFICADO_MILITAR("aluno","Data Exp. Certificado Militar", "aluno.dataexpedicaocertificadoMilitar",TipoCampoEnum.DATA, 20),
	ALUNO_ORGAO_EXPEDIDOR_CERTIFICADO_MILITAR("aluno","Orgão Exp. Certificado Militar", "aluno.orgaoexpedidorcertificadoMilitar",TipoCampoEnum.TEXTO, 20),
	ALUNO_TITULO_ELEITORAL("aluno","Título Eleitoral", "aluno.tituloEleitoral",TipoCampoEnum.TEXTO, 20),
	ALUNO_ZONA_ELEITORAL("aluno","Zona Eleitoral", "aluno.zonaEleitoral",TipoCampoEnum.TEXTO, 20),
	ALUNO_DATA_EXPEDICAO_TITULO_ELEITORAL("aluno","Data Exp. Título Eleitoral", "aluno.dataexpedicaotituloEleitoral",TipoCampoEnum.DATA, 20),
	ALUNO_DATA_NASCIMENTO("aluno","Data Nascimento", "aluno.datanasc",TipoCampoEnum.DATA, 20),
	ALUNO_CEP("aluno", "Cep", "aluno.cep",TipoCampoEnum.TEXTO, 20),
	ALUNO_ENDERECO("aluno", "Endereço", "aluno.endereco",TipoCampoEnum.TEXTO, 40),
	ALUNO_SETOR("aluno", "Setor Endereço", "aluno.setor",TipoCampoEnum.TEXTO, 30),
	ALUNO_NUMERO("aluno", "Número Endereço", "aluno.numero",TipoCampoEnum.TEXTO, 10),
	ALUNO_COMPLEMENTO("aluno", "Complemento Endereço", "aluno.complemento",TipoCampoEnum.TEXTO, 30),
	ALUNO_EMAIL("aluno","E-mail Aluno", "aluno.email",TipoCampoEnum.TEXTO, 25),
	ALUNO_INSTITUCIONAL_EMAIL("aluno","E-mail Institucional Aluno", "(ARRAY_TO_STRING(ARRAY(SELECT pei.email from pessoaemailinstitucional pei where pei.pessoa = aluno.codigo), '; '))",TipoCampoEnum.TEXTO, 100),
	ALUNO_TODOS_EMAILS("aluno","E-mail Aluno", "(ARRAY_TO_STRING(ARRAY(SELECT pei.email from pessoaemailinstitucional pei where pei.pessoa = aluno.codigo union all SELECT aluno.email), '; '))",TipoCampoEnum.TEXTO, 100),
	ALUNO_PAGINA_PESSOAL("aluno","Página Pessoal", "aluno.paginaPessoal",TipoCampoEnum.TEXTO, 25),
	ALUNO_TELEFONE_RES("aluno","Telefone Residencial","aluno.telefoneRes",TipoCampoEnum.TEXTO, 20),
	ALUNO_TELEFONE_COMERCIAL("aluno","Telefone Comercial","aluno.telefoneComer",TipoCampoEnum.TEXTO, 20),
	ALUNO_TELEFONE_RECADO("aluno","Telefone Comercial","aluno.telefoneRecado",TipoCampoEnum.TEXTO, 20),
	ALUNO_CELULAR("aluno","Celular", "aluno.celular",TipoCampoEnum.TEXTO, 20),
	ALUNO_PAI("filiacao","Nome Pai", "(select p.nome from filiacao inner join pessoa as p on p.codigo = filiacao.pais where filiacao.tipo = 'PA' and filiacao.aluno = aluno.codigo limit 1 )",TipoCampoEnum.TEXTO, 40), 
	ALUNO_MAE("filiacao","Nome Mãe", "(select p.nome from filiacao inner join pessoa as p on p.codigo = filiacao.pais where filiacao.tipo = 'MA' and filiacao.aluno = aluno.codigo limit 1 )",TipoCampoEnum.TEXTO, 40),
	ALUNO_RESPONSAVEL_FINANCEIRO("filiacao","Nome Responsável Financeiro", "(select p.nome from filiacao inner join pessoa as p on p.codigo = filiacao.pais where filiacao.responsavelfinanceiro  and filiacao.aluno = aluno.codigo limit 1 )",TipoCampoEnum.TEXTO, 40),
	ALUNO_REGISTRO_ACADEMICO("aluno","Registro Acadêmico","aluno.registroAcademico",TipoCampoEnum.TEXTO, 40)

	
	;

	private TagSEIDecidirAlunoEnum(String entidade, String atributo, String campo, TipoCampoEnum tipoCampo, Integer tamanhoCampo) {
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
