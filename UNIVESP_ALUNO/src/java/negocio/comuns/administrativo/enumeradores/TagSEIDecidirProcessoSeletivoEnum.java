package negocio.comuns.administrativo.enumeradores;

import negocio.interfaces.administrativo.PerfilTagSEIDecidirEnum;

public enum TagSEIDecidirProcessoSeletivoEnum implements PerfilTagSEIDecidirEnum{
	
	PROCESSO_SELETIVO_CODIGO("processoSeletivo", "Código Processo Seletivo", "procSeletivo.codigo",TipoCampoEnum.INTEIRO , 20),
	PROCESSO_SELETIVO_DESCRICAO("processoSeletivo", "Descrição Processo Seletivo", "procSeletivo.descricao", TipoCampoEnum.TEXTO, 60),
	PROCESSO_SELETIVO_NIVEL_EDUCACIONAL("processoSeletivo", "Nível Educacional", "procSeletivo.nivelEducacional", TipoCampoEnum.TEXTO, 40),
	PROCESSO_SELETIVO_VALOR_INSCRICAO("processoSeletivo", "Valor Inscrição", "procSeletivo.valorInscricao", TipoCampoEnum.BIG_DECIMAL, 10),
	PROCESSO_SELETIVO_DATA_FIM_INTERNET("processoSeletivo", "Data Fim Internet", "procSeletivo.dataFimInternet", TipoCampoEnum.DATA, 10),
	PROCESSO_SELETIVO_DATA_INICIO_INTERNET("processoSeletivo", "Data Início Internet", "procSeletivo.dataInicioInternet", TipoCampoEnum.DATA, 10),
	PROCESSO_SELETIVO_DATA_FIM("processoSeletivo", "Data Fim", "procSeletivo.dataFim", TipoCampoEnum.DATA, 10),
	PROCESSO_SELETIVO_DATA_INICIO("processoSeletivo", "Data Início", "procSeletivo.dataInicio", TipoCampoEnum.DATA, 10),
	PROCESSO_SELETIVO_ANO("processoSeletivo", "Ano", "procSeletivo.ano", TipoCampoEnum.TEXTO, 4),
	PROCESSO_SELETIVO_SEMESTRE("processoSeletivo", "Semestre", "procSeletivo.semestre", TipoCampoEnum.TEXTO, 1)
	
	;
	
	private TagSEIDecidirProcessoSeletivoEnum(String entidade, String atributo, String campo, TipoCampoEnum tipoCampo, Integer tamanhoCampo) {
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
