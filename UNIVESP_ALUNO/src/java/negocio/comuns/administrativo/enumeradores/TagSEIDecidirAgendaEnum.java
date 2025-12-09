package negocio.comuns.administrativo.enumeradores;

import negocio.interfaces.administrativo.PerfilTagSEIDecidirEnum;

public enum TagSEIDecidirAgendaEnum implements PerfilTagSEIDecidirEnum{
	
	AGENDA_CODIGO("agendapessoa", "Código Agenda", "agendapessoa.codigo",TipoCampoEnum.INTEIRO , 20),
	AGENDA_HORARIO_CODIGO("agendapessoahorario", "Código Agenda Horário", "agendapessoahorario.codigo",TipoCampoEnum.INTEIRO , 20),
	AGENDA_HORARIO_DIA("agendapessoahorario", "Dia Agenda Horário", "agendapessoahorario.dia",TipoCampoEnum.INTEIRO , 20),
	AGENDA_HORARIO_MES("agendapessoahorario", "Mês Agenda Horário", "agendapessoahorario.mes",TipoCampoEnum.INTEIRO , 20),
	AGENDA_HORARIO_ANO("agendapessoahorario", "Ano Agenda Horário", "agendapessoahorario.ano",TipoCampoEnum.INTEIRO , 20),
	AGENDA_HORARIO_DIASEMANAENUM("agendapessoahorario", "Dia da Semana Agenda Horário", "agendapessoahorario.diasemanaenum",TipoCampoEnum.TEXTO , 20),
	AGENDA_COMPROMISSO_DESCRICAO("compromissoagendapessoahorario", "Descrição Compromisso", "compromissoagendapessoahorario.descricao",TipoCampoEnum.TEXTO , 20),
	AGENDA_COMPROMISSO_HORA("compromissoagendapessoahorario", "Hora Inicio Compromisso", "compromissoagendapessoahorario.hora",TipoCampoEnum.TEXTO , 20),
	AGENDA_COMPROMISSO_HORAFIM("compromissoagendapessoahorario", "Hora Fim Compromisso", "compromissoagendapessoahorario.horafim",TipoCampoEnum.TEXTO , 20),
	AGENDA_COMPROMISSO_TIPOCOMPROMISSO("compromissoagendapessoahorario", "Tipo Compromisso - Compromisso", "compromissoagendapessoahorario.tipocompromisso",TipoCampoEnum.TEXTO , 20),
	AGENDA_COMPROMISSO_OBSERVACAO("compromissoagendapessoahorario", "Observação Compromisso", "compromissoagendapessoahorario.observacao",TipoCampoEnum.TEXTO , 20),
	AGENDA_COMPROMISSO_ORIGEM("compromissoagendapessoahorario", "Origem Compromisso", "compromissoagendapessoahorario.origem",TipoCampoEnum.TEXTO , 20),
	AGENDA_COMPROMISSO_TIPOCONTATO("compromissoagendapessoahorario", "Tipo Contato Compromisso", "compromissoagendapessoahorario.tipocontato",TipoCampoEnum.TEXTO , 20),
	AGENDA_COMPROMISSO_DATACOMPROMISSO("compromissoagendapessoahorario", "Data Compromisso", "compromissoagendapessoahorario.datacompromisso",TipoCampoEnum.TEXTO , 20),
	AGENDA_COMPROMISSO_TIPO_SITUACAO_COMPROMISSO("compromissoagendapessoahorario", "Tipo  Situação Compromisso", "compromissoagendapessoahorario.tiposituacaocompromissoenum",TipoCampoEnum.TEXTO , 20),
	AGENDA_COMPROMISSO_HISTORICO_REAGENDAMENTO("compromissoagendapessoahorario", "Historico Reagendamento Compromisso", "compromissoagendapessoahorario.historicoreagendamentocompromisso",TipoCampoEnum.TEXTO , 20),
	AGENDA_COMPROMISSO_DATA_INICIAL("compromissoagendapessoahorario", "Data Inicial Compromisso", "compromissoagendapessoahorario.datainicialcompromisso",TipoCampoEnum.TEXTO , 20),
	;
	
	private TagSEIDecidirAgendaEnum(String entidade, String atributo, String campo, TipoCampoEnum tipoCampo, Integer tamanhoCampo) {
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
