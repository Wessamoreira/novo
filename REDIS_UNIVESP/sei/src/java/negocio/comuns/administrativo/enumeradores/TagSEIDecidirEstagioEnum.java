package negocio.comuns.administrativo.enumeradores;

import negocio.interfaces.administrativo.PerfilTagSEIDecidirEnum;

public enum TagSEIDecidirEstagioEnum implements PerfilTagSEIDecidirEnum{
	ESTAGIO_MATRICULA("estagio","Matrícula", "estagio.matricula",TipoCampoEnum.TEXTO, 50),
	ESTAGIO_ANO("estagio","Ano", "estagio.ano",TipoCampoEnum.TEXTO, 4),
	ESTAGIO_SEMESTRE("estagio","Semestre", "estagio.semestre",TipoCampoEnum.TEXTO, 2),
	ESTAGIO_CARGA_HORARIA("estagio","Carga Horária", "estagio.cargahoraria", TipoCampoEnum.INTEIRO, 10),
	ESTAGIO_CARGA_HORARIA_DEFERIDA("estagio","Carga Horária Deferida", "estagio.cargahorariadeferida", TipoCampoEnum.INTEIRO, 10),
	ESTAGIO_TIPO_ESTAGIO_ENUM("estagio","Tipo Estágio Enum", "estagio.tipoEstagio", TipoCampoEnum.TEXTO, 30),
	ESTAGIO_TIPO_ESTAGIO_DESCRICAO("estagio","Tipo Estágio - Descrição",  "(case estagio.tipoEstagio when 'OBRIGATORIO' then 'Formulário Final' "
			+ " when 'OBRIGATORIO_APROVEITAMENTO' then 'Formulário Aproveitamento'"
			+ " when 'OBRIGATORIO_EQUIVALENCIA' then 'Formulário Equivalência' else 'Não Obrigatório' end) ",TipoCampoEnum.TEXTO, 50),
	ESTAGIO_DATA_CREATED("estagio","Data Registro", "estagio.created", TipoCampoEnum.DATA, 10),
	ESTAGIO_NOME_CREATED("estagio","Usuário Registro", "estagio.nomecreated", TipoCampoEnum.TEXTO, 50),
	ESTAGIO_SITUACAO_ESTAGIO_ENUM("estagio","Situação Estágio - SIGLA", "estagio.situacaoestagioenum", TipoCampoEnum.TEXTO, 30),
	ESTAGIO_SITUACAO_ESTAGIO_DESCRICAO("estagio", "Situação Estágio - Descrição", "(case estagio.situacaoestagioenum when 'AGUARDANDO_ASSINATURA' then 'Aguardando Assinatura' "
			+ " when 'REALIZANDO' then 'Realizando'"
			+ " when 'EM_ANALISE' then 'Em Análise'"
			+ " when 'EM_CORRECAO' then 'Em Correção'"
			+ " when 'DEFERIDO' then 'Deferido'"
			+ " when 'INDEFERIDO' then 'Indeferido'"
			+ " when 'EXIGIDO' then 'Exigido' else 'Pendente' end) ",TipoCampoEnum.TEXTO, 30),
	ESTAGIO_SITUACAOADICIONAL_ESTAGIO_ENUM("estagio","Situação Adicional Estágio", "estagio.situacaoadicionalestagioenum", TipoCampoEnum.TEXTO, 40),
	ESTAGIO_SITUACAOADICIONAL_ESTAGIO_DESCRICAO("estagio", "Situação Adicional Estágio - Descrição", "(case estagio.situacaoadicionalestagioenum when 'INDEFERIDO_ALUNO' then 'Indeferido Pelo Aluno' "
			+ " when 'INDEFERIDO_FACILITADOR' then 'Indeferido Pelo Avaliador/Facilitador'"
			+ " when 'PENDENTE_SOLICITACAO_ASSINATURA' then 'Pendente de Solicitação'"
			+ " when 'ASSINATURA_PENDENTE' then 'Assinatura Pendente'"
			+ " when 'DEFERIDO' then 'Deferido' else 'Agurando Formulário' end) ",TipoCampoEnum.TEXTO, 40),
	
	ESTAGIO_TIPO_SITUACAO_APROVEITAMENTO_ENUM("estagio","Tipo Situação Aproveitamento Estágio", "estagio.tiposituacaoaproveitamentoenum", TipoCampoEnum.TEXTO, 30),
	ESTAGIO_TIPO_SITUACAO_APROVEITAMENTO_DESCRICAO("estagio", "Tipo Situação Aproveitamento Estágio - Descrição", "(case estagio.tiposituacaoaproveitamentoenum when 'DOCENTE_REGULAR' then 'Docente Regular' "
			+ " when 'LICENCIADO_OUTRO_CURSO' then 'Licenciado Outro Curso' else '' end) ",TipoCampoEnum.TEXTO, 30),
	
	ESTAGIO_NOME_CONCEDENTE("estagio","Nome Concedente", "estagio.nomeConcedente", TipoCampoEnum.TEXTO, 50),
	ESTAGIO_TIPO_CONCEDENTE("tipoconcedente","Tipo Concedente", "tipoconcedente.nome", TipoCampoEnum.TEXTO, 50),
	ESTAGIO_CNPJ_CONCEDENTE("estagio","CNPJ Concedente", "estagio.cnpj", TipoCampoEnum.TEXTO, 20),
	ESTAGIO_TELEFONE_CONCEDENTE("estagio","Telefone Concedente", "estagio.telefone", TipoCampoEnum.TEXTO, 50),
	ESTAGIO_CEP_CONCEDENTE("estagio","CEP Concedente", "estagio.cep", TipoCampoEnum.TEXTO, 12),
	ESTAGIO_ENDERECO_CONCEDENTE("estagio","Endereço Concedente", "estagio.endereco", TipoCampoEnum.TEXTO, 50),
	ESTAGIO_NUMERO_CONCEDENTE("estagio","Nº Concedente", "estagio.numero", TipoCampoEnum.TEXTO, 10),
	ESTAGIO_BAIRRO_CONCEDENTE("estagio","Bairro Concedente", "estagio.bairro", TipoCampoEnum.TEXTO, 50),
	ESTAGIO_COMPLEMENTO_CONCEDENTE("estagio","Complemento Concedente", "estagio.complemento", TipoCampoEnum.TEXTO, 50),
	ESTAGIO_CIDADE_CONCEDENTE("estagio","Cidade Concedente", "estagio.cidade", TipoCampoEnum.TEXTO, 50),
	
	ESTAGIO_MOTIVO("estagio","Motivo", "estagio.motivo", TipoCampoEnum.TEXTO, 250),
	ESTAGIO_DATA_ENVIO_ANALISE("estagio","Data Envio Análise", "estagio.dataenvioanalise", TipoCampoEnum.DATA, 10),
	ESTAGIO_DATA_LIMITE_ANALISE("estagio","Data Limite Análise", "estagio.datalimiteanalise", TipoCampoEnum.DATA, 10),
	ESTAGIO_DATA_ENVIO_CORRECAO("estagio","Data Envio Correção", "estagio.dataenviocorrecao", TipoCampoEnum.DATA, 10),
	ESTAGIO_DATA_LIMITE_CORRECAO("estagio","Data Limite Correção", "estagio.datalimitecorrecao", TipoCampoEnum.DATA, 10),
	
	ESTAGIO_NOME_BENEFICIARIO("estagio","Nome Beneficiáro", "estagio.nomebeneficiario", TipoCampoEnum.TEXTO, 50),
	ESTAGIO_RG_BENEFICIARIO("estagio","RG Beneficiáro", "estagio.rgbeneficiario", TipoCampoEnum.TEXTO, 50),
	ESTAGIO_CPF_BENEFICIARIO("estagio","CPF Beneficiáro", "estagio.cpfbeneficiario", TipoCampoEnum.TEXTO, 50),
	ESTAGIO_EMAIL_BENEFICIARIO("estagio","E-mail Beneficiáro", "estagio.emailbeneficiario", TipoCampoEnum.TEXTO, 50),
	ESTAGIO_TELEFONE_BENEFICIARIO("estagio","Telefone Beneficiáro", "estagio.telefonebeneficiario", TipoCampoEnum.TEXTO, 50),
	ESTAGIO_CEP_BENEFICIARIO("estagio","CEP Beneficiáro", "estagio.cepbeneficiario", TipoCampoEnum.TEXTO, 50),
	ESTAGIO_CIDADE_BENEFICIARIO("estagio","Cidade Beneficiáro", "estagio.cidadebeneficiario", TipoCampoEnum.TEXTO, 50),
	ESTAGIO_ESTADO_BENEFICIARIO("estagio","Estado Beneficiáro", "estagio.estadobeneficiario", TipoCampoEnum.TEXTO, 2),
	ESTAGIO_NUMERO_BENEFICIARIO("estagio","Nº Beneficiáro", "estagio.numerobeneficiario", TipoCampoEnum.TEXTO, 2),
	ESTAGIO_ENDERECO_BENEFICIARIO("estagio","Endereço Beneficiáro", "estagio.enderecobeneficiario", TipoCampoEnum.TEXTO, 2),
	ESTAGIO_COMPLEMENTO_BENEFICIARIO("estagio","Complemento Beneficiáro", "estagio.complementobeneficiario", TipoCampoEnum.TEXTO, 2),
	ESTAGIO_BAIRRO_BENEFICIARIO("estagio","Bairro Beneficiáro", "estagio.setorbeneficiario", TipoCampoEnum.TEXTO, 2),
	
	ESTAGIO_NOME_RESPONSAVEL_CONCEDENTE("estagio","Nome Responsável Concedente", "estagio.responsavelconcedente", TipoCampoEnum.TEXTO, 50),
	ESTAGIO_CPF_RESPONSAVEL_CONCEDENTE("estagio","Cpf Responsável Concedente", "estagio.cpfresponsavelconcedente", TipoCampoEnum.TEXTO, 15),
	ESTAGIO_EMAIL_RESPONSAVEL_CONCEDENTE("estagio","E-mail Responsável Concedente", "estagio.emailresponsavelconcedente", TipoCampoEnum.TEXTO, 50),
	ESTAGIO_TELEFONE_RESPONSAVEL_CONCEDENTE("estagio","Telefone Responsável Concedente", "estagio.telefoneresponsavelconcedente", TipoCampoEnum.TEXTO, 50),
	
	ESTAGIO_GRADECURRILAR_ESTAGIO("gradecurricularestagio","Componente de Estágio", "gradecurricularestagio.nome", TipoCampoEnum.TEXTO, 50),
	
	ESTAGIO_DATA_DEFERIMENTO("estagio","Data Deferimento", "estagio.datadeferimento", TipoCampoEnum.DATA, 10),
	ESTAGIO_NOME_DEFERIMENTO("usuariodeferimento","Usuário Deferimento", "pessoa_rd.nome", TipoCampoEnum.TEXTO, 50),
	ESTAGIO_DATA_INDEFERIMENTO("estagio","Data Indeferimento", "estagio.dataindeferimento", TipoCampoEnum.DATA, 10),
	ESTAGIO_NOME_INDEFERIMENTO("usuarioindeferimento","Usuário Indeferimento", "pessoa_ri.nome", TipoCampoEnum.TEXTO, 50),
	
	
	ESTAGIO_GRUPOPESSOAITEM_NOME("grupopessoaitem","Nome Avaliador/Facilitador", "pessoa_gpi.nome", TipoCampoEnum.TEXTO, 50),
	ESTAGIO_GRUPOPESSOAITEM_CPF("grupopessoaitem","CPF Avaliador/Facilitador", "pessoa_gpi.CPF", TipoCampoEnum.TEXTO, 15),
	ESTAGIO_GRUPOPESSOAITEM_EMAILS("grupopessoaitem","E-mail Avaliador/Facilitador", "(ARRAY_TO_STRING(ARRAY(SELECT pei.email from pessoaemailinstitucional pei where pei.pessoa = pessoa_gpi.codigo union all SELECT pessoa_gpi.email), '; '))",TipoCampoEnum.TEXTO, 100),
	ESTAGIO_GRUPOPESSOAITEM_EMAIL_PESSOA("grupopessoaitem","E-mail Avaliador/Facilitador", "pessoa_gpi.email",TipoCampoEnum.TEXTO, 30),
	ESTAGIO_GRUPOPESSOAITEM_EMAIL_INSTITUCIONAL("grupopessoaitem","E-mail Avaliador/Facilitador", "(ARRAY_TO_STRING(ARRAY(SELECT pei.email from pessoaemailinstitucional pei where pei.pessoa = pessoa_gpi.codigo), '; '))",TipoCampoEnum.TEXTO, 100),
	
	;

	private TagSEIDecidirEstagioEnum(String entidade, String atributo, String campo, TipoCampoEnum tipoCampo, Integer tamanhoCampo) {
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
