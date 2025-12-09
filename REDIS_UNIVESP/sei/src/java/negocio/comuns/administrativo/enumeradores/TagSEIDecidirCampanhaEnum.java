package negocio.comuns.administrativo.enumeradores;

import negocio.interfaces.administrativo.PerfilTagSEIDecidirEnum;

public enum TagSEIDecidirCampanhaEnum implements PerfilTagSEIDecidirEnum{
	
	CAMPANHA_CODIGO("campanha", "Código Campanha", "campanha.codigo",TipoCampoEnum.INTEIRO , 20),
	CAMPANHA_DESCRICAO("campanha", "Descrição Campanha", "campanha.descricao", TipoCampoEnum.TEXTO, 60),
	CAMPANHA_SITUACAO("campanha", "Situação Campanha", "campanha.situacao", TipoCampoEnum.TEXTO, 40),
	CAMPANHA_POLITICA_GERAR_AGENDA("campanha", "Política Geração Agenda", "campanha.politicaGerarAgenda", TipoCampoEnum.TEXTO, 100),
	CAMPANHA_TIPOCAMPANHA("campanha", "Tipo de Campanha", "campanha.tipoCampanha", TipoCampoEnum.TEXTO, 100),
	CAMPANHA_OBJETIVO("campanha", "Objetivo Campanha", "campanha.objetivo", TipoCampoEnum.TEXTO, 40),
	CAMPANHA_PUBLICOALVO("campanha", "Público Alvo Campanha", "campanha.publicoAlvo", TipoCampoEnum.TEXTO, 40),
	CAMPANHA_PERIODOINICIO("campanha", "Período Inicio", "campanha.periodoInicio", TipoCampoEnum.DATA, 20),
	CAMPANHA_PERIODOFIM("campanha", "Período Fim", "campanha.periodoFim", TipoCampoEnum.DATA, 20),
	CAMPANHA_HORAINICIAL("campanha", "Horário Inicio", "campanha.horaInicial", TipoCampoEnum.TEXTO, 20),
	CAMPANHA_HORAFINAL("campanha", "Horário Fim", "campanha.horaFinal", TipoCampoEnum.TEXTO, 20),
	CAMPANHA_META("campanhameta", "Meta", "campanhameta.descricao",TipoCampoEnum.TEXTO, 30)
	;
	
	private TagSEIDecidirCampanhaEnum(String entidade, String atributo, String campo, TipoCampoEnum tipoCampo, Integer tamanhoCampo) {
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
