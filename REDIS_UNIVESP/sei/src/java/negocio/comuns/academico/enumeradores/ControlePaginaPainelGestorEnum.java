package negocio.comuns.academico.enumeradores;

public enum ControlePaginaPainelGestorEnum {

	PAGINA_INICIAL("painelGestor"),
	PAINEL_GESTOR_FINANCEIRO("painelGestorFinanceiroForm"),
	PAINEL_GESTOR_CATEGORIA_DESPESA("painelGestorPorCategoriaDespesaForm"),
	PAINEL_GESTOR_CATEGORIA_DESPESA_DEPARTAMENTO("painelGestorPorCategoriaDespesaDepartamentoForm"),
	
	PAINEL_GESTOR_FINANCEIRO_POR_NIVEL_EDUCACIONAL("painelGestorFinanceiroPorNivelEducacionalForm"),
	PAINEL_GESTOR_FINANCEIRO_POR_NIVEL_EDUCACIONAL_FLUXO_CAIXA("painelGestorFinanceiroPorNivelEducacionalFluxoCaixaForm"),
	
	PAINEL_GESTOR_FINANCEIRO_MONITORAMENTO_DESCONTO_PROGRESSIVO("painelGestorMonitoramentoDescontoProgressivoForm"),
	PAINEL_GESTOR_FINANCEIRO_MONITORAMENTO_DESCONTO_INSTITUICAO("painelGestorMonitoramentoDescontoInstituicaoForm"),
	PAINEL_GESTOR_FINANCEIRO_MONITORAMENTO_DESCONTO_CONVENIO("painelGestorMonitoramentoDescontoConvenioForm"),
	
	PAINEL_GESTOR_FINANCEIRO_POR_TURMA("painelGestorFinanceiroPorTurmaForm"),
	PAINEL_GESTOR_FINANCEIRO_POR_TURMA_FLUXO_CAIXA("painelGestorFinanceiroPorTurmaFluxoCaixaForm"),
	
	PAINEL_GESTOR_FINANCEIRO_MONITORAMENTO_DESCONTO_PROGRESSIVO_TURMA("painelGestorMonitoramentoDescontoTurmaProgressivoForm"),
	PAINEL_GESTOR_FINANCEIRO_MONITORAMENTO_DESCONTO_INSTITUICAO_TURMA("painelGestorMonitoramentoDescontoTurmaInstituicaoForm"),
	PAINEL_GESTOR_FINANCEIRO_MONITORAMENTO_DESCONTO_CONVENIO_TURMA("painelGestorMonitoramentoDescontoTurmaConvenioForm"),
	
	PAINEL_GESTOR_MONITORAMENTO_DESCONTO("painelGestorMonitoramentoDescontoForm"),
	PAINEL_GESTOR_MONITORAMENTO_DESCONTO_POR_NIVEL_EDUCACIONAL("painelGestorMonitoramentoDescontoNivelEducacionalForm"),
	PAINEL_GESTOR_MONITORAMENTO_DESCONTO_POR_TURMA("painelGestorMonitoramentoDescontoPorTurma"),
	
	PAINEL_GESTOR_FINANCEIRO_DETALHAMENTO_CONTARECEBER("painelGestorFinanceiroDetalhamentoContaReceberForm"),
	
	PAINEL_GESTOR_FINANCEIRO_ACADEMICO("painelGestorAcademicoFinanceiro");	
	
	
	
	private String navegacao;
	
	private ControlePaginaPainelGestorEnum(String navegacao) {
		this.navegacao = navegacao;
	}

	public String getNavegacao() {		
		return navegacao;
	}

	public void setNavegacao(String navegacao) {
		this.navegacao = navegacao;
	}
	
	
}
