package negocio.comuns.utilitarias.dominios;

public enum TipoDashboardEnum {
	
	PDF_VIEW("far fa-file-pdf", "PDF"),
	MARKETING("far fa-images", "Marketing"),
//	VAGAS_EMPREGO("fas fa-id-card", "Currículum/Vaga Emprego"),
	BANNER_MATRICULA("fas fa-cart-plus", "Matricule-se"),
	DISCIPLINAS_ALUNO("fas fa-book-reader", "Minhas Disciplinas"),
	EVOLUCAO_ACADEMICA_ALUNO("fas fa-chart-bar text-primary", "Evolução Acadêmica"),
	EVOLUCAO_ACADEMICA_ESTAGIO("fas fa-chart-bar text-warning", "Estágio"),
	EVOLUCAO_ACADEMICA_ATIVIDADE_COMPLEMENTAR("fas fa-chart-bar text-danger", "Atividade Complementar"),
	HORARIO_ALUNO("fas fa-user-clock", "Calendário"),
	HORARIO_PROFESSOR("fas fa-user-clock", "Calendário"),
	TUTORIA_ONLINE("fas fa-user-clock", "Tutoria Online"),
	MAPA_ESTAGIO("fas fa-user-clock", "Mapa Estágios"),
	TCC("fas fa-briefcase", "TCC"),
	PROJETO_INTEGRADOR("far fa-handshake", "Projeto Integrador"),
	PENDENCIA_PROFESSOR("fas fa-exclamation-triangle", "Alertas"),	
	TWITTER("fab fa-twitter", "Twitter"),
	MONITOR_CONHECIMENTO_EAD("fas fa-chart-bar text-info", "Monitor de Conhecimento"),
	EVOLUCAO_ESTUDO_ONLINE("fas fa-chart-bar text-primary", "Evolução Estudo do Conteúdo"),
	SEI_DECIDIR("fas fa-chart-bar", "Lista Relatório Sei Decidir"),
	REQUERIMENTO("fas fa-thumbtack", "Requerimentos"),
	LANCAMENTOS_FINANCEIROS_PENDENTE("fas fa-dollar-sign text-danger", "Lançamentos Financeiros Pendentes"),
	RECEBIMENTO_COMPRA("fas fa-truck", "Compra Aguardando Recebimento"),
	COTACAO("fas fa-columns", "Cotações Aguadando Autorização"),
	ESTOQUE("fas fa-boxes", "Produtos (Estoque Abaixo do Mínimo)"),
	REQUISICAO_PENDENTE_AUTORIZACAO("fas fa-truck-loading", "Requisições Aguardando Autorização"),
	FAVORITOS("fa fa-star iconeDesfavoritar", "Menu Favoritos"),
	EMPRESTIMOS_BIBLIOTECA("fas fa-atlas text-danger", "Empréstimos Biblioteca"),
//	BANCO_CURRICULUM("fas fa-briefcase", "Banco de Curriculum"),
	BI_RECEITA_DESPESA("fas fa-chart-line", "Receita X Despesa Consolidado"),
	BI_ACADEMICO_POR_NIVEL_EDUCACIONAL("fas fa-university", "Painel Gestor Acadêmico"),
	BI_CONSUMO_DEPARTAMENTO("fas fa-stamp", "Consumo Por Departamento"),
	BI_CONSUMO_CATEGORIA("fas fa-comment-dollar", "Despesa Por Categoria"),
	BI_PLANO_ORCAMENTARIO("fas fa-balance-scale", "Plano Orçamentário"),	
	BI_SITUACOES_ACADEMICAS("fas fa-graduation-cap", "Situações Acadêmicas"),
	BI_EVOLUCOES_ACADEMICAS("fas fa-university", "Evolução Acadêmica - Nível Educacional"),
	BI_EVASOES_ACADEMICAS("fas fa-ban", "Evasão / Saída"),
	BI_PROCESSO_SELETIVO("fas fa-user-tag", "Monitoramento Processo Seletivo"),
	BI_ALUNOS_POR_TURNO("fas fa-user-clock", "Número de Alunos por Turno"),
	BI_MONITOR_RENOVACOES("fas fa-user-graduate", "Não Renovados / Pré-Matrícula"),
	BI_MONITOR_ACADEMICO("fas fa-school", "Outras Informações Acadêmicas"),
	BI_ALUNO_POR_SEXO("fas fa-venus-mars", "Número de Alunos por Sexo"),
	BI_TURMAS_POR_TURNO("fas fa-history", "Número de Turmas por Turno"),
	BI_PROFESSORES_POR_TURNO("fas fa-chalkboard-teacher", "Número de Professores por Turno"),
	BI_IMPRESSAO_DECLARACOES("fas fa-file-pdf", "Monitoramento Impressão Declaração"),
	BI_ALUNO_POR_TURMA("fas fa-users", "Alunos Por Turma"),
	BI_RECEITA_DESPESA_CONSOLIDADO("fas fa-chart-line", "Receita X Despesa Consolidado"),
	BI_RECEITA_POR_COMPETENCIA("fas fa-money-bill-alt", "Mapa de Receitas por Competência"),
	BI_RECEITA_POR_FLUXO_CAIXA("fas fa-cash-register", "Mapa de Receitas por Fluxo de Caixa"),
	BI_MAPA_DESPESAS("fas fa-stamp", "Mapa de Despesas"),
	LINK_UTEIS("fas fa-link", "Menu Links Úteis"),
	NOVIDADE_DESTAQUE("fas fa-certificate", "Otimize Informa"),
	INTEGRALIZACAO_CURRICULAR("fas fa-calendar-check", "Integralização Curricular");
	
	
	private String iconePadrao;
	private String titulo;
	
	
	private TipoDashboardEnum(String iconePadrao, String titulo) {
		this.iconePadrao = iconePadrao;
		this.titulo = titulo;
	}

	public String getIconePadrao() {
		return iconePadrao;
	}

	public void setIconePadrao(String iconePadrao) {
		this.iconePadrao = iconePadrao;
	}

	public String getTitulo() {
		if(titulo == null) {
			titulo =  "";
		}
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	
	
	
}
