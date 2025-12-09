package negocio.comuns.administrativo.enumeradores;

import negocio.interfaces.administrativo.PerfilTagSEIDecidirEnum;

public enum TagSEIDecidirHistoricoEnum implements PerfilTagSEIDecidirEnum {
	HISTORICO_CODIGO("historico","Código Historico", "historico.codigo",TipoCampoEnum.INTEIRO, 20),	
	ANO_HISTORICO("historico", "Ano Histórico", "historico.anoHistorico", TipoCampoEnum.TEXTO, 4),
	SEMESTRE_HISTORICO("historico", "Semestre Histórico", "historico.semestreHistorico", TipoCampoEnum.TEXTO, 1),
	SITUACAO_HISTORICO("historico", "Situação Histórico", "historico.situacao", TipoCampoEnum.TEXTO, 10),
	TIPO_HISTORICO("historico", "Tipo Histórico", "historico.tipoHistorico", TipoCampoEnum.TEXTO, 10),
	CODIGO_DISCIPLINA_HISTORICO("historico","Código Disciplina Historico", "historico.disciplina",TipoCampoEnum.INTEIRO, 20),
	NOME_DISCIPLINA_HISTORICO("historico", "Nome Disciplina Histórico", "disciplina.nome", TipoCampoEnum.TEXTO, 150),
	CODIGO_TURMA_DISCIPLINA("historico", "Turma da Disciplina", "turmaDisciplinaEstudada.codigo", TipoCampoEnum.INTEIRO, 20),
	IDENTIFICADOR_TURMA_DISCIPLINA("historico", "Identificador Turma da Disciplina", "turmaDisciplinaEstudada.identificadorTurma", TipoCampoEnum.TEXTO, 50),
	NOTA1("historico","Nota 1", "historico.nota1",TipoCampoEnum.DOUBLE, 15),
	NOTA2("historico","Nota 2", "historico.nota2",TipoCampoEnum.DOUBLE, 15),
	NOTA3("historico","Nota 3", "historico.nota3",TipoCampoEnum.DOUBLE, 15),
	NOTA4("historico","Nota 4", "historico.nota4",TipoCampoEnum.DOUBLE, 15),
	NOTA5("historico","Nota 5", "historico.nota5",TipoCampoEnum.DOUBLE, 15),
	NOTA6("historico","Nota 6", "historico.nota6",TipoCampoEnum.DOUBLE, 15),
	NOTA7("historico","Nota 7", "historico.nota7",TipoCampoEnum.DOUBLE, 15),
	NOTA8("historico","Nota 8", "historico.nota8",TipoCampoEnum.DOUBLE, 15),
	NOTA9("historico","Nota 9", "historico.nota9",TipoCampoEnum.DOUBLE, 15),
	NOTA10("historico","Nota 10", "historico.nota10",TipoCampoEnum.DOUBLE, 15),
	NOTA11("historico","Nota 11", "historico.nota11",TipoCampoEnum.DOUBLE, 15),
	NOTA12("historico","Nota 12", "historico.nota12",TipoCampoEnum.DOUBLE, 15),
	NOTA13("historico","Nota 13", "historico.nota13",TipoCampoEnum.DOUBLE, 15),
	NOTA14("historico","Nota 14", "historico.nota14",TipoCampoEnum.DOUBLE, 15),
	NOTA15("historico","Nota 15", "historico.nota15",TipoCampoEnum.DOUBLE, 15),
	NOTA16("historico","Nota 16", "historico.nota16",TipoCampoEnum.DOUBLE, 15),
	NOTA17("historico","Nota 17", "historico.nota17",TipoCampoEnum.DOUBLE, 15),
	NOTA18("historico","Nota 18", "historico.nota18",TipoCampoEnum.DOUBLE, 15),
	NOTA19("historico","Nota 19", "historico.nota19",TipoCampoEnum.DOUBLE, 15),
	NOTA20("historico","Nota 20", "historico.nota20",TipoCampoEnum.DOUBLE, 15),
	NOTA21("historico","Nota 21", "historico.nota21",TipoCampoEnum.DOUBLE, 15),
	NOTA22("historico","Nota 22", "historico.nota22",TipoCampoEnum.DOUBLE, 15),
	NOTA23("historico","Nota 23", "historico.nota23",TipoCampoEnum.DOUBLE, 15),
	NOTA24("historico","Nota 24", "historico.nota24",TipoCampoEnum.DOUBLE, 15),
	NOTA25("historico","Nota 25", "historico.nota25",TipoCampoEnum.DOUBLE, 15),
	NOTA26("historico","Nota 26", "historico.nota26",TipoCampoEnum.DOUBLE, 15),
	NOTA27("historico","Nota 27", "historico.nota27",TipoCampoEnum.DOUBLE, 15),
	NOTA28("historico","Nota 28", "historico.nota28",TipoCampoEnum.DOUBLE, 15),
	NOTA29("historico","Nota 29", "historico.nota29",TipoCampoEnum.DOUBLE, 15),
	NOTA30("historico","Nota 30", "historico.nota30",TipoCampoEnum.DOUBLE, 15),
	NOTA31("historico","Nota 31", "historico.nota31",TipoCampoEnum.DOUBLE, 15),
	NOTA32("historico","Nota 32", "historico.nota32",TipoCampoEnum.DOUBLE, 15),
	NOTA33("historico","Nota 33", "historico.nota33",TipoCampoEnum.DOUBLE, 15),
	NOTA34("historico","Nota 34", "historico.nota34",TipoCampoEnum.DOUBLE, 15),
	NOTA35("historico","Nota 35", "historico.nota35",TipoCampoEnum.DOUBLE, 15),
	NOTA36("historico","Nota 36", "historico.nota36",TipoCampoEnum.DOUBLE, 15),
	NOTA37("historico","Nota 37", "historico.nota37",TipoCampoEnum.DOUBLE, 15),
	NOTA38("historico","Nota 38", "historico.nota38",TipoCampoEnum.DOUBLE, 15),
	NOTA39("historico","Nota 39", "historico.nota39",TipoCampoEnum.DOUBLE, 15),
	NOTA40("historico","Nota 40", "historico.nota40",TipoCampoEnum.DOUBLE, 15),
	FREQUENCIA("historico","Frequência", "historico.freguencia",TipoCampoEnum.DOUBLE, 15),
	MEDIA_FINAL("historico","Média Final", "historico.mediaFinal",TipoCampoEnum.DOUBLE, 15),
	DATA_REGISTRO("historico","Data Registro", "historico.dataRegistro",TipoCampoEnum.DATA, 20),
	CODIGO_CONFIGURACAO_ACADEMICO("historico","Código Configuração Acadêmico", "historico.configuracaoAcademico",TipoCampoEnum.INTEIRO, 20),
	NOME_CONFIGURACAO_ACADEMICO("historico", "Nome Configuração Acadêmico", "(select distinct configuracaoAcademico.nome from configuracaoAcademico where configuracaoAcademico.codigo = historico.configuracaoAcademico)", TipoCampoEnum.TEXTO, 40),
	CARGA_HORARIA_DISCIPLINA("historico","Carga Horária Disciplina", "historico.cargaHorariaDisciplina",TipoCampoEnum.INTEIRO, 20),
	CARGA_HORARIA_CURSADA_DISCIPLINA("historico","Carga Horária Cursada Disciplina", "historico.cargaHorariaCursada",TipoCampoEnum.INTEIRO, 20),
	CREDITO_DISCIPLINA("historico","Créditos Disciplina", "historico.creditoDisciplina",TipoCampoEnum.INTEIRO, 20),
	;

	private TagSEIDecidirHistoricoEnum(String entidade, String atributo, String campo, TipoCampoEnum tipoCampo, Integer tamanhoCampo) {
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
