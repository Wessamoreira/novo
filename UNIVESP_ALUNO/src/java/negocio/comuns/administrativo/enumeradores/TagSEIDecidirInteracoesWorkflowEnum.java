package negocio.comuns.administrativo.enumeradores;

import negocio.interfaces.administrativo.PerfilTagSEIDecidirEnum;

public enum TagSEIDecidirInteracoesWorkflowEnum implements PerfilTagSEIDecidirEnum{
	
	INTERACAO_WORKFLOW_CODIGO("interacaoworkflow", "Código Interação Workflow", "interacaoworkflow.codigo",TipoCampoEnum.INTEIRO , 20),
	INTERACAO_WORKFLOW_MOTIVO("interacaoworkflow", "Motivo Interação Workflow", "interacaoworkflow.motivo",TipoCampoEnum.TEXTO , 100),
	INTERACAO_WORKFLOW_OBSERVACAO("interacaoworkflow", "Observação Interação Workflow", "interacaoworkflow.observacao",TipoCampoEnum.TEXTO , 100),
	INTERACAO_WORKFLOW_TIPOINTERACAO("interacaoworkflow", "Tipo Interação Interação Workflow", "interacaoworkflow.tipoInteracao",TipoCampoEnum.TEXTO , 20),
	INTERACAO_WORKFLOW_TIPOORIGEMINTERACAO("interacaoworkflow", "Tipo Origem", "interacaoworkflow.tipoOrigemInteracao",TipoCampoEnum.TEXTO , 20),
	INTERACAO_WORKFLOW_IDENTIFICADORORIGEM("interacaoworkflow", "Identificador Origem", "interacaoworkflow.identificadorOrigem",TipoCampoEnum.TEXTO , 20),
	INTERACAO_WORKFLOW_CODIGOENTIDADEORIGEM("interacaoworkflow", "Código Entidade Origem", "interacaoworkflow.codigoEntidadeOrigem",TipoCampoEnum.INTEIRO , 20),
	INTERACAO_WORKFLOW_DATAINICIO("interacaoworkflow", "Data Inicio", "interacaoworkflow.dataInicio", TipoCampoEnum.DATA, 20),
	INTERACAO_WORKFLOW_DATATERMINO("interacaoworkflow", "Data Termino", "interacaoworkflow.dataTermino", TipoCampoEnum.DATA, 20),
	INTERACAO_WORKFLOW_HORAINICIO("interacaoworkflow", "Horário Inicio", "interacaoworkflow.horaInicio", TipoCampoEnum.TEXTO, 20),
	INTERACAO_WORKFLOW_HORATERMINO("interacaoworkflow", "Horário Termino", "interacaoworkflow.horaTermino", TipoCampoEnum.TEXTO, 20),
	INTERACAO_WORKFLOW_RESPONSAVEL("interacaoworkflowResponsavel", "Responsável Interação Workflow", "interacaoworkflowResponsavel.nome",TipoCampoEnum.TEXTO, 60)
	;
	
	private TagSEIDecidirInteracoesWorkflowEnum(String entidade, String atributo, String campo, TipoCampoEnum tipoCampo, Integer tamanhoCampo) {
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
