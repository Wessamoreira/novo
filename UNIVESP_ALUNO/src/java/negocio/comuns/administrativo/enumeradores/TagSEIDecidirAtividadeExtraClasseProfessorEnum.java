package negocio.comuns.administrativo.enumeradores;

import negocio.interfaces.administrativo.PerfilTagSEIDecidirEnum;

public enum TagSEIDecidirAtividadeExtraClasseProfessorEnum implements PerfilTagSEIDecidirEnum {

	ATIVIDADE_EXTRA_CLASSE_PROFESSOR_DATA_GERAL("data", "Data", "atividadeextraclasseprofessor.data", TipoCampoEnum.DATA , 20),
	ATIVIDADE_EXTRA_CLASSE_PROFESSOR_DATA_CADASTRO("dataCadastro", "Data Cadastro", "atividadeextraclasseprofessor.datacadastro", TipoCampoEnum.DATA , 20),
	ATIVIDADE_EXTRA_CLASSE_PROFESSOR_HORA_PREVISTA("horaPrevista", "Hora Prevista", "atividadeextraclasseprofessor.horaPrevista", TipoCampoEnum.INTEIRO , 20),

	ATIVIDADE_EXTRA_CLASSE_PROFESSOR_POSTADO_DESCRICAO("descricao", "Descricao", "atividadeextraclasseprofessorpostado.descricao", TipoCampoEnum.TEXTO , 200),
	ATIVIDADE_EXTRA_CLASSE_PROFESSOR_POSTADO_DATA_ATIVIDADE("dataAtividade", "Data Atividade", "atividadeextraclasseprofessorpostado.dataAtividade", TipoCampoEnum.DATA , 20),
	ATIVIDADE_EXTRA_CLASSE_PROFESSOR_POSTADO_DATA_CADASTRO("dataCadastro", "Data Cadastro Postagem", "atividadeextraclasseprofessorpostado.datacadastro", TipoCampoEnum.DATA , 20),
	ATIVIDADE_EXTRA_CLASSE_PROFESSOR_POSTADO_SITUACAO("situacaoHoraAtividadeExtraClasseEnum", "Situação", "atividadeextraclasseprofessorpostado.situacao", TipoCampoEnum.TEXTO , 200),
	ATIVIDADE_EXTRA_CLASSE_PROFESSOR_POSTADO_HORAS_REALIZADAS("horasRealizada", "Horas Realizada", "atividadeextraclasseprofessorpostado.horasRealizada", TipoCampoEnum.INTEIRO , 20),
	ATIVIDADE_EXTRA_CLASSE_PROFESSOR_POSTADO_MOTIVO_INDEFERIMENTO("motivoIndeferimento", "Motivo Indeferimento", "atividadeextraclasseprofessorpostado.motivoIndeferimento", TipoCampoEnum.TEXTO , 200),
	ATIVIDADE_EXTRA_CLASSE_PROFESSOR_POSTADO_NOME("nome", "Nome Pessoa", "p.nome", TipoCampoEnum.TEXTO , 200),
	ATIVIDADE_EXTRA_CLASSE_PROFESSOR_POSTADO_MATRICULA_CARGO("matriculaCargo", "Matricula Cargo", "fc.matriculacargo", TipoCampoEnum.TEXTO , 50),

	ATIVIDADE_EXTRA_CLASSE_PROFESSOR_POSTADO_MES_ATIVIDADE("dataAtividade", "Mês Atividade", "extract(month FROM atividadeextraclasseprofessorpostado.dataAtividade)", TipoCampoEnum.DOUBLE , 20),
	ATIVIDADE_EXTRA_CLASSE_PROFESSOR_POSTADO_ANO_ATIVIDADE("dataAtividade", "Ano Atividade", "extract(year FROM atividadeextraclasseprofessorpostado.dataAtividade)", TipoCampoEnum.DOUBLE , 20),
	ATIVIDADE_EXTRA_CLASSE_PROFESSOR_POSTADO_CODIGO_ATIVIDADE_EXTRA_CLASSE("atividadeextraclasseprofessor", "Atividade Extra Classe Professor", "atividadeextraclasseprofessor.codigo", TipoCampoEnum.INTEIRO , 20),
	ATIVIDADE_EXTRA_CLASSE_PROFESSOR_POSTADO_AGUARDANDO_APROVACAO("horasrealizada", "Soma Aguardando Aproveitamento", "(select sum(ap.horasrealizada) from atividadeextraclasseprofessorpostado ap where ap.situacao = 'AGUARDANDO_APROVACAO' and ap.atividadeextraclasseprofessor = atividadeextraclasseprofessor.codigo)", TipoCampoEnum.DOUBLE , 20),
	ATIVIDADE_EXTRA_CLASSE_PROFESSOR_POSTADO_APROVADO("horasrealizada", "Soma Aprovados", "(select sum(ap.horasrealizada) from atividadeextraclasseprofessorpostado ap where ap.situacao = 'APROVADO' and ap.atividadeextraclasseprofessor = atividadeextraclasseprofessor.codigo)", TipoCampoEnum.DOUBLE , 20),
	ATIVIDADE_EXTRA_CLASSE_PROFESSOR_POSTADO_INDEFERIDO("horasrealizada", "Soma Indeferidos", "(select sum(ap.horasrealizada) from atividadeextraclasseprofessorpostado ap where ap.situacao = 'INDEFERIDO' and ap.atividadeextraclasseprofessor = atividadeextraclasseprofessor.codigo)", TipoCampoEnum.DOUBLE , 20),
	ATIVIDADE_EXTRA_CLASSE_PROFESSOR_POSTADO_PENDENTE("horasrealizada", "Soma Pendentes", "(select sum(ap.horasrealizada) from atividadeextraclasseprofessorpostado ap where ap.situacao = 'PENDENTE' and ap.atividadeextraclasseprofessor = atividadeextraclasseprofessor.codigo)", TipoCampoEnum.DOUBLE , 20),

	ATIVIDADE_EXTRA_CLASSE_PROFESSOR_POSTADO_DATA_APROVACAO("dataAprovacao", "Data Aprovação", "atividadeextraclasseprofessorpostado.dataAprovacao", TipoCampoEnum.TEXTO , 20),
	ATIVIDADE_EXTRA_CLASSE_PROFESSOR_POSTADO_DATA_INDEFERIMENTO("dataIndeferimento", "Data Indeferimento", "atividadeextraclasseprofessorpostado.dataIndeferimento", TipoCampoEnum.DATA , 20);

	private TagSEIDecidirAtividadeExtraClasseProfessorEnum(String entidade, String atributo, String campo, TipoCampoEnum tipoCampo, Integer tamanhoCampo) {
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
