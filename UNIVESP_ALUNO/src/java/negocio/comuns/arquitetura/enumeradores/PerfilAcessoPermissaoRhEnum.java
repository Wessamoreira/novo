package negocio.comuns.arquitetura.enumeradores;

import java.util.Arrays;
import java.util.List;

import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

/**
 * @author Rodrigo Wind
 *
 */
public enum PerfilAcessoPermissaoRhEnum implements PerfilAcessoPermissaoEnumInterface {

	ATUALIZAR_VALE_TRANSPORTE("AtualizarValeTrasnporte",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_atualizarValeTransporte_titulo"),
					UteisJSF.internacionalizar("per_atualizarValeTransporte_ajuda"),
					new String[] { "atualizarValeTransporteForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.RH_BASICO),

	COMPETENCIA_FOLHA_PAGAMENTO("CompetenciaFolhaPagamento",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_competenciaFolhaPagamento_titulo"),
					UteisJSF.internacionalizar("per_competenciaFolhaPagamento_ajuda"),
					new String[] { "competenciaFolhaPagamentoCons.xhtml", "competenciaFolhaPagamentoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.RH_BASICO),

	SECAO_FOLHA_PAGAMENTO("SecaoFolhaPagamento",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_SecaoFolhaPagamento_titulo"),
					UteisJSF.internacionalizar("per_SecaoFolhaPagamento_ajuda"),
					new String[] { "eventoFolhaPagamentoCons.xhtml", "eventoFolhaPagamentoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.RH_BASICO),

	GRUPO_LANCAMENTO_FOLHA_PAGAMENTO("GrupoLancamentoFolhaPagamento", new PermissaoVisao[] { new PermissaoVisao(
			TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_GrupoLancamentoFolhaPagamento_titulo"),
			UteisJSF.internacionalizar("per_GrupoLancamentoFolhaPagamento_ajuda"),
			new String[] { "grupoLancamentoFolhaPagamentoCons.xhtml", "grupoLancamentoFolhaPagamentoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.RH_FOLHA_PAGAMENTO),

	LANCAMENTO_FOLHA_PAGAMENTO("LancamentoFolhaPagamento",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_LancamentoFolhaPagamento_titulo"),
					UteisJSF.internacionalizar("per_LancamentoFolhaPagamento_ajuda"),
					new String[] { "lancamentoFolhaPagamentoCons.xhtml", "lancamentoFolhaPagamentoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.RH_FOLHA_PAGAMENTO),

	PARAMETRO_VALE_TRANSPORTE("ParametroValeTransporte",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ParametroValeTransporte_titulo"),
					UteisJSF.internacionalizar("per_ParametroValeTransporte_ajuda"),
					new String[] { "parametroValeTransporteCons.xhtml", "parametroValeTransporteForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.RH_BASICO),

	FORMULA_FOLHA_PAGAMENTO("FormulaFolhaPagamento",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_FolhaFolhaPagamento_titulo"),
					UteisJSF.internacionalizar("per_FolhaFolhaPagamento_ajuda"),
					new String[] { "formulaFolhaPagamentoCons.xhtml", "formulaFolhaPagamentoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.RH_BASICO),

	FALTAS_FUNCIONARIO("FaltasFuncionario",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_FaltasFuncionario_titulo"),
					UteisJSF.internacionalizar("per_FaltasFuncionario_ajuda"),
					new String[] { "faltasFuncionarioCons.xhtml", "faltasFuncionarioForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.RH_EVENTOS_FUNCIONARIO),

	// Sub-Menu Eventos
	SALARIO_COMPOSTO("SalarioComposto",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_SalarioComposto_titulo"),
					UteisJSF.internacionalizar("per_SalarioComposto_ajuda"),
					new String[] { "salarioCompostoCons.xhtml", "salarioCompostoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.RH_EVENTOS_FUNCIONARIO),

	EVENTO_FOLHA_PAGAMENTO("EventoFolhaPagamento",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_eventoFolhaPagamento_titulo"),
					UteisJSF.internacionalizar("per_EventoFolhaPagamento_ajuda"),
					new String[] { "eventoFolhaPagamentoCons.xhtml", "eventoFolhaPagamentoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.RH_EVENTOS_FUNCIONARIO),

	EVENTO_VALE_TRANSPORTE_FUNCIONARIO_CARGO("EventoValeTransporteFuncionarioCargo",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_EventoValeTransporte_titulo"),
					UteisJSF.internacionalizar("per_EventoValeTransporte_ajuda"),
					new String[] { "eventoValeTransporteCons.xhtml", "eventoValeTransporteForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.RH_EVENTOS_FUNCIONARIO),

	EVENTO_FIXO_CARGO_FUNCIONARIO_FP("EventoFixoCargoFuncionario",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_EventoCargoFuncionario_titulo"),
					UteisJSF.internacionalizar("per_EventoCargoFuncionario_ajuda"),
					new String[] { "eventoFixoCargoFuncionarioCons.xhtml", "eventoFixoCargoFuncionarioForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.RH_EVENTOS_FUNCIONARIO),

	EVENTO_EMPRESTIMO_CARGO_FUNCIONARIO_FP("EventoEmprestimoCargoFuncionario",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_EventoEmprestimoCargoFuncionario_titulo"),
					UteisJSF.internacionalizar("per_EventoEmprestimoCargoFuncionario_ajuda"),
					new String[] { "eventoEmprestimoCargoFuncionarioCons.xhtml",
							"eventoEmprestimoCargoFuncionarioForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.RH_EVENTOS_FUNCIONARIO),

	/**
	 * Valor Referencia FolhaPagamento
	 *
	 */
	VALOR_REFERENCIA_FOLHA_PAGAMENTO("ValorReferenciaFolhaPagamento", new PermissaoVisao[] { new PermissaoVisao(
			TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_ValorReferenciaFolhaPagamento_titulo"),
			UteisJSF.internacionalizar("per_ValorReferenciaFolhaPagamento_ajuda"),
			new String[] { "valorReferenciaFolhaPagamentoCons.xhtml", "valorReferenciaFolhaPagamentoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.RH_BASICO),

	TIPO_TRANSPORTE("TipoTransporte",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_TipoTransporte_titulo"),
					UteisJSF.internacionalizar("per_TipoTransporte_ajuda"),
					new String[] { "tipoTransporteCons.xhtml", "tipoTransporteForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.RH_BASICO),

	/**
	 * Nivel Salarial da tabela de progressao salarial
	 *
	 */
	NIVEL_SALARIAL("NivelSalarial",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_NivelSalarial_titulo"),
					UteisJSF.internacionalizar("per_NivelSalarial_ajuda"),
					new String[] { "nivelSalarialCons.xhtml", "nivelSalarialForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.RH_RECURSOS_HUMANOS),

	/**
	 * Faixa Salarial da tabela de progressao salarial
	 *
	 */
	FAIXA_SALARIAL("FaixaSalarial",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_FaixaSalarial_titulo"),
					UteisJSF.internacionalizar("per_FaixaSalarial_ajuda"),
					new String[] { "faixaSalarialCons.xhtml", "faixaSalarialForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.RH_RECURSOS_HUMANOS),

	/**
	 * Progressao Salarial do funcionario
	 *
	 */
	PROGRESSAO_SALARIAL("ProgressaoSalarial",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ProgressaoSalarial_titulo"),
					UteisJSF.internacionalizar("per_ProgressaoSalarial_ajuda"),
					new String[] { "progressaoSalarialCons.xhtml", "progressaoSalarialForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.RH_RECURSOS_HUMANOS),

	CONTRA_CHEQUE("FichaFinanceira",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_FichaFinanceira_titulo"),
					UteisJSF.internacionalizar("per_FichaFinanceira_ajuda"),
					new String[] { "fichaFinanceiraCons.xhtml", "contraChequeForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.RH_FOLHA_PAGAMENTO),

	TIPO_EMPRESTIMO("TipoEmprestimo",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_TipoEmprestimo_titulo"),
					UteisJSF.internacionalizar("per_TipoEmprestimo_ajuda"),
					new String[] { "tipoEmprestimoCons.xhtml", "tipoEmprestimoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.RH_EMPRESTIMO),

	MAP_ATIVIDADE_EXTRA_CLASSE_PROFESSOR("MapaAtividadeExtraClasseProfessor",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MapaAtividadeExtraClasseProfessor_titulo"),
					UteisJSF.internacionalizar("per_MapaAtividadeExtraClasseProfessorRel_ajuda"),
					new String[] { "atividadeExtraClasseProfessor.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.RH_BASICO),

	/**
	 * Relatorio da ficha financeira.
	 */
	FICHA_FINANCEIRA_RELATORIO("FichaFinanceiraRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_FichaFinanceira_titulo"),
					UteisJSF.internacionalizar("per_FichaFinanceira_ajuda"),
					new String[] { "fichaFinanceiraRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.RH_RELATORIOS),

	FUNCIONARIO_RELATORIO("FuncionarioRel", new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
			UteisJSF.internacionalizar("per_RelatorioFuncionario_titulo"),
			UteisJSF.internacionalizar("per_RelatorioFuncionario_ajuda"), new String[] { "funcionarioRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.RH_RELATORIOS),

	RELATORIO_SEI_DECIDIR("RelatorioSEIDecidirRecursosHumanos",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_RelatorioSEIDecidir_titulo"),
					UteisJSF.internacionalizar("per_RelatorioSEIDecidir_ajuda"),
					new String[] { "relatorioSEIDecidirRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.RH_RELATORIOS),
	PERMITIR_GERAR_RELATORIO_SEIDECIDIR_RH_APENAS_DADOS("PermitirGerarRelatorioSeiDecidirRHApenasDados",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirGerarRelatorioSeiDecidirAcademicoApenasDados_titulo"),
					UteisJSF.internacionalizar("per_PermitirGerarRelatorioSeiDecidirAcademicoApenasDados_ajuda"),
					new String[] { "relatorioSEIDecidirRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, RELATORIO_SEI_DECIDIR,
			PerfilAcessoSubModuloEnum.RH_RELATORIOS),

	HISTORICO_SITUACAO_FUNCIONARIO_RELATORIO("HistoricoSituacaofuncionarioRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_HistoricoSituacaoFuncionario_titulo"),
					UteisJSF.internacionalizar("per_HistoricoSituacaoFuncionario_ajuda"),
					new String[] { "historicoSituacaofuncionarioRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.RH_RELATORIOS),

	// Sub-Menu Ferias
	PERIODO_AQUISITIVO_FERIAS("PeriodoAquisitivoFerias",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PeriodoAquisitivoFerias_titulo"),
					UteisJSF.internacionalizar("per_PeriodoAquisitivoFerias_ajuda"),
					new String[] { "periodoAquisitivoFeriasCons.xhtml", "periodoAquisitivoFeriasCons.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.RH_FERIAS),

	MARCACAO_FERIAS("MarcacaoFerias",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MarcacaoFerias_titulo"),
					UteisJSF.internacionalizar("per_MarcacaoFerias_ajuda"),
					new String[] { "marcacaoFeriasCons.xhtml", "marcacaoFeriasForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.RH_FERIAS),

	MARCACAO_FERIAS_COLETIVAS("MarcacaoFeriasColetivas",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MarcacaoFeriasColetivas_titulo"),
					UteisJSF.internacionalizar("per_MarcacaoFeriasColetivas_ajuda"),
					new String[] { "marcacaoFeriasColetivasCons.xhtml", "marcacaoFeriasColetivasForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.RH_FERIAS),

	// Sub-Menu Basico
	SINDICATO("Sindicato", new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
			UteisJSF.internacionalizar("per_Sindicato_titulo"), UteisJSF.internacionalizar("per_Sindicato_ajuda"),
			new String[] { "sindicatoCons.xhtml", "sindicatoCons.xhtml" }) }, TipoPerfilAcessoPermissaoEnum.ENTIDADE,
			null, PerfilAcessoSubModuloEnum.RH_BASICO),

	// Sub-Menu Ficha Funcionaro
	FICHA_FUNCIONARIO("HistoricoFuncionario",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_HistoricoFuncionario_titulo"),
					UteisJSF.internacionalizar("per_HistoricoFuncionario_ajuda"),
					new String[] { "historicoFuncionarioCons.xhtml", "historicoFuncionarioCons.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.RH_FICHA_FUNCIONARIO),

	AFASTAMENTO_FUNCIONARIO("AfastamentoFuncionario",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_AfastamentoFuncionario_titulo"),
					UteisJSF.internacionalizar("per_AfastamentoFuncionario_ajuda"),
					new String[] { "afastamentoFuncionarioCons.xhtml", "afastamentoFuncionarioCons.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.RH_FICHA_FUNCIONARIO),

	// Sub-Menu de Rescisão
	RESCISAO("Rescisao",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_RescisaoFuncionario_titulo"),
					UteisJSF.internacionalizar("per_RescisaoFuncionario_ajuda"),
					new String[] { "rescisaoCons.xhtml", "rescisaoCons.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.RH_RESCISAO),

	HORAS_ATIVIDADE_EXTRA_CLASSE_PROFESSOR("AtividadeExtraClasseProfessor",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_DefinirAtividadeExtraClasse_titulo"),
					UteisJSF.internacionalizar("per_DefinirAtividadeExtraClasse_ajuda"),
					new String[] { "atividadeExtraClasseProfessorForm.xhtml" }), },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.RH_BASICO),

	MAPA_ATIVIDADE_EXTRA_CLASSE_PROFESSOR("MapaAtividadeExtraClasseProfessor",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MapaAtividadeExtraClasseProfessor_titulo"),
					UteisJSF.internacionalizar("per_MapaAtividadeExtraClasseProfessorRel_ajuda"),
					new String[] { "mapaAtividadeExtraClasseProfessor.xhtml" }), },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.RH_BASICO),

	// FUNCIONALIDADES
	PERMITE_ALTERAR_DATA_LIMITE_REGISTRO("PermiteAlterarDataLimiteRegistro",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermiteAlterarDataLimiteRegistro_titulo"),
					UteisJSF.internacionalizar("per_PermiteAlterarDataLimiteRegistro_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE,
			PerfilAcessoPermissaoRhEnum.HORAS_ATIVIDADE_EXTRA_CLASSE_PROFESSOR, PerfilAcessoSubModuloEnum.RH_BASICO),

	PERMITE_ALTERAR_DATA_LIMITE_APROVACAO("PermiteAlterarDataLimiteAprovacao",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermiteAlterarDataLimiteAprovacao_titulo"),
					UteisJSF.internacionalizar("per_PermiteAlterarDataLimiteAprovacao_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE,
			PerfilAcessoPermissaoRhEnum.HORAS_ATIVIDADE_EXTRA_CLASSE_PROFESSOR, PerfilAcessoSubModuloEnum.RH_BASICO);

	/**
	 * @param descricao
	 * @param ajuda
	 * @param paginaAcesso
	 * @param tipoPerfilAcesso
	 * @paran permissaoSuperiorEnum - popular quando no tipoPerfilAcesso for
	 *        funcionalidade
	 */

	private PerfilAcessoPermissaoRhEnum(String valor, PermissaoVisao[] permissaoVisao,
			TipoPerfilAcessoPermissaoEnum tipoPerfilAcesso,
			Enum<? extends PerfilAcessoPermissaoEnumInterface> permissaoSuperiorEnum,
			PerfilAcessoSubModuloEnum perfilAcessoSubModulo) {
		this.valor = valor;
		this.permissaoVisao = permissaoVisao;
		this.tipoPerfilAcesso = tipoPerfilAcesso;
		this.permissaoSuperiorEnum = permissaoSuperiorEnum;
		this.perfilAcessoSubModulo = perfilAcessoSubModulo;
	}

	private String valor;
	private PermissaoVisao[] permissaoVisao;
	private TipoPerfilAcessoPermissaoEnum tipoPerfilAcesso;
	private PerfilAcessoSubModuloEnum perfilAcessoSubModulo;
	private Enum<? extends PerfilAcessoPermissaoEnumInterface> permissaoSuperiorEnum;

	/**
	 * @return the tipoPerfilAcesso
	 */
	public TipoPerfilAcessoPermissaoEnum getTipoPerfilAcesso() {
		if (tipoPerfilAcesso == null) {
			tipoPerfilAcesso = TipoPerfilAcessoPermissaoEnum.ENTIDADE;
		}
		return tipoPerfilAcesso;
	}

	/**
	 * @param tipoPerfilAcesso the tipoPerfilAcesso to set
	 */
	public void setTipoPerfilAcesso(TipoPerfilAcessoPermissaoEnum tipoPerfilAcesso) {
		this.tipoPerfilAcesso = tipoPerfilAcesso;
	}

	/**
	 * @return the perfilAcessoSubModulo
	 */
	public PerfilAcessoSubModuloEnum getPerfilAcessoSubModulo() {
		if (perfilAcessoSubModulo == null) {
			perfilAcessoSubModulo = PerfilAcessoSubModuloEnum.TODOS;
		}
		return perfilAcessoSubModulo;
	}

	/**
	 * @param perfilAcessoSubModulo the perfilAcessoSubModulo to set
	 */
	public void setPerfilAcessoSubModulo(PerfilAcessoSubModuloEnum perfilAcessoSubModulo) {
		this.perfilAcessoSubModulo = perfilAcessoSubModulo;
	}

	/**
	 * @return the valor
	 */
	public String getValor() {
		if (valor == null) {
			valor = "";
		}
		return valor;
	}

	/**
	 * @param valor the valor to set
	 */
	public void setValor(String valor) {
		this.valor = valor;
	}

	/**
	 * @return the permissaoSuperiorEnum
	 */
	public Enum<? extends PerfilAcessoPermissaoEnumInterface> getPermissaoSuperiorEnum() {
		return permissaoSuperiorEnum;
	}

	/**
	 * @param permissaoSuperiorEnum the permissaoSuperiorEnum to set
	 */
	public void setPermissaoSuperiorEnum(Enum<? extends PerfilAcessoPermissaoEnumInterface> permissaoSuperiorEnum) {
		this.permissaoSuperiorEnum = permissaoSuperiorEnum;
	}

	/**
	 * @return the PermissaoVisao
	 */
	public PermissaoVisao[] getPermissaoVisao() {
		if (permissaoVisao == null) {
			permissaoVisao = new PermissaoVisao[0];
		}
		return permissaoVisao;
	}

	/**
	 * @param PermissaoVisao the PermissaoVisao to set
	 */
	public void setPermissaoVisao(PermissaoVisao[] PermissaoVisao) {
		this.permissaoVisao = PermissaoVisao;
	}

	/**
	 * @return the utilizarVisaoProfessor
	 */
	public Boolean getUtilizaVisao(TipoVisaoEnum tipoVisaoEnum) {
		for (PermissaoVisao permissaoVisao : getPermissaoVisao()) {
			if (permissaoVisao.equals(tipoVisaoEnum)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return the utilizarVisaoProfessor
	 */
	public String getDescricaoVisao(TipoVisaoEnum tipoVisaoEnum) {
		if (getUtilizaVisao(tipoVisaoEnum)) {
			return getPermissaoVisao(tipoVisaoEnum).getDescricao();
		}
		return "";
	}

	/**
	 * @return the utilizarVisaoProfessor
	 */
	public String getAjudaVisao(TipoVisaoEnum tipoVisaoEnum) {
		if (getUtilizaVisao(tipoVisaoEnum)) {
			return getPermissaoVisao(tipoVisaoEnum).getAjuda();
		}
		return "";
	}

	/**
	 * @return the utilizarVisaoProfessor
	 */
	public List<String> getPaginaAcessoVisao(TipoVisaoEnum tipoVisaoEnum) {
		if (getUtilizaVisao(tipoVisaoEnum)) {
			return Arrays.asList(getPermissaoVisao(tipoVisaoEnum).getPaginaAcesso());
		}
		return null;
	}

	/**
	 * @return the utilizarVisaoProfessor
	 */
	public PermissaoVisao getPermissaoVisao(TipoVisaoEnum tipoVisaoEnum) {
		if (getUtilizaVisao(tipoVisaoEnum)) {
			for (PermissaoVisao permissaoVisao2 : getPermissaoVisao()) {
				if (permissaoVisao2.equals(tipoVisaoEnum)) {
					return permissaoVisao2;
				}
			}
		}
		return null;
	}

	public Boolean getIsApresentarApenasPermissaoTotal() {
		return getTipoPerfilAcesso().equals(TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE)
				&& getTipoPerfilAcesso().equals(TipoPerfilAcessoPermissaoEnum.RELATORIO);
	}

	public String descricaoModulo;

	@Override
	public String getDescricaoModulo() {
		if (descricaoModulo == null) {
			if (Uteis.isAtributoPreenchido(this.getPerfilAcessoSubModulo())) {
				descricaoModulo += getPerfilAcessoSubModulo().getPerfilAcessoModuloEnum().getValorApresentar();
			} else {
				descricaoModulo = "";
			}
		}
		return descricaoModulo;
	}

	public String descricaoSubModulo;

	@Override
	public String getDescricaoSubModulo() {
		if (descricaoSubModulo == null) {
			if (Uteis.isAtributoPreenchido(this.getPerfilAcessoSubModulo())) {
				descricaoSubModulo = getPerfilAcessoSubModulo().getDescricao();

			} else {
				descricaoSubModulo = "";
			}
		}
		return descricaoSubModulo;
	}

	public String descricaoModuloSubModulo;

	@Override
	public String getDescricaoModuloSubModulo() {
		if (descricaoModuloSubModulo == null) {
			if (Uteis.isAtributoPreenchido(this.getPerfilAcessoSubModulo())) {
				descricaoModuloSubModulo = getPerfilAcessoSubModulo().getPerfilAcessoModuloEnum().getValorApresentar()
						+ " - " + getPerfilAcessoSubModulo().getDescricao();
			} else {
				descricaoModuloSubModulo = "";
			}
		}
		return descricaoModuloSubModulo;
	}

}