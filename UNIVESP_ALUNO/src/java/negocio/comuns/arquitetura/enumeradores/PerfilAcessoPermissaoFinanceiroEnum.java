package negocio.comuns.arquitetura.enumeradores;

import java.util.Arrays;
import java.util.List;

import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

/**
 * @author Rodrigo Wind
 *
 */
public enum PerfilAcessoPermissaoFinanceiroEnum implements PerfilAcessoPermissaoEnumInterface {
	
	PAINEL_GESTOR_REQUERIMENTOS("PainelGestorRequerimentos",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PainelGestorRequerimentos_titulo"),
					UteisJSF.internacionalizar("per_PainelGestorRequerimentos_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoSeiDecidirEnum.PAINEL_GESTOR,
			PerfilAcessoSubModuloEnum.FINANCEIRO_BANCO_PARCEIRO),

	PAINEL_GESTOR_LANCAMENTOS_PENDENTES("PainelGestorLancamentosPendentes",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PainelGestorLancamentosPendentes_titulo"),
					UteisJSF.internacionalizar("per_PainelGestorLancamentosPendentes_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoSeiDecidirEnum.PAINEL_GESTOR,
			PerfilAcessoSubModuloEnum.FINANCEIRO_BANCO_PARCEIRO),
	PAINEL_GESTOR_COMUNICACAO_INTERNA("PainelGestorComunicacaoInterna",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PainelGestorComunicacaoInterna_titulo"),
					UteisJSF.internacionalizar("per_PainelGestorComunicacaoInterna_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoSeiDecidirEnum.PAINEL_GESTOR,
			PerfilAcessoSubModuloEnum.FINANCEIRO_BANCO_PARCEIRO),
	CONFIGURACAO_RECEBIMENTO_CARTAO_ONLINE("ConfiguracaoRecebimentoCartaoOnline",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ConfiguracaoRecebimentoCartaoOnline_titulo"),
					UteisJSF.internacionalizar("per_ConfiguracaoRecebimentoCartaoOnline_ajuda"),
					new String[] { "configuracaoRecebimentoCartaoOnlineCons.xhtml",
							"configuracaoRecebimentoCartaoOnlineForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_RECEBER),
	/**
	* Prestacao Conta Unidade Ensino
	*
	*/
	PRESTACAO_CONTA_UNIDADE_ENSINO("PrestacaoContaUnidadeEnsino",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PrestacaoContaUnidadeEnsino_titulo"),
					UteisJSF.internacionalizar("per_PrestacaoContaUnidadeEnsino_ajuda"),
					new String[] { "prestacaoContaCons.xhtml", "prestacaoContaForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.FINANCEIRO_PRESTACAO_CONTA),
	/**
	* Prestacao Conta Turma
	*
	*/
	PRESTACAO_CONTA_TURMA("PrestacaoContaTurma",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PrestacaoContaTurma_titulo"),
					UteisJSF.internacionalizar("per_PrestacaoContaTurma_ajuda"),
					new String[] { "prestacaoContaCons.xhtml", "prestacaoContaForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.FINANCEIRO_PRESTACAO_CONTA),
	/**
	* Negociacao Conta Receber
	*
	*/
	NEGOCIACAO_CONTA_RECEBER("NegociacaoContaReceber",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_NegociacaoContaReceber_titulo"),
					UteisJSF.internacionalizar("per_NegociacaoContaReceber_ajuda"),
					new String[] { "negociacaoContaReceberCons.xhtml", "negociacaoContaReceberForm.xhtml" }),
					new PermissaoVisao(
							TipoVisaoEnum.ALUNO, UteisJSF.internacionalizar("per_NegociacaoContaReceber_titulo"),
							UteisJSF.internacionalizar("per_NegociacaoContaReceber_ajuda"),
							new String[] { "renegociacaoContaReceberAlunoForm.xhtml" }),
					new PermissaoVisao(TipoVisaoEnum.PAIS,
							UteisJSF.internacionalizar("per_NegociacaoContaReceber_titulo"),
							UteisJSF.internacionalizar("per_NegociacaoContaReceber_ajuda"),
							new String[] { "renegociacaoContaReceberAlunoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_RECEBER),
	DESCONTO_NEGOCIACAO_CONTA_RECEBER("DescontoNegociacaoContaReceber",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_DescontoNegociacaoContaReceber_titulo"),
					UteisJSF.internacionalizar("per_DescontoNegociacaoContaReceber_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoFinanceiroEnum.NEGOCIACAO_CONTA_RECEBER,
			PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_RECEBER),
	PERMITIR_NEGOCIAR_PARCELAS_NEGOCIACAO_NAO_CUMPRIDA("PermitirNegociarParcelasNegociacaoNaoCumprida",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
							UteisJSF.internacionalizar("per_PermitirNegociarParcelasNegociacaoNaoCumprida_titulo"),
							UteisJSF.internacionalizar("per_PermitirNegociarParcelasNegociacaoNaoCumprida_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.ALUNO,
							UteisJSF.internacionalizar("per_PermitirNegociarParcelasNegociacaoNaoCumprida_titulo"),
							UteisJSF.internacionalizar("per_PermitirNegociarParcelasNegociacaoNaoCumprida_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.PAIS,
							UteisJSF.internacionalizar("per_PermitirNegociarParcelasNegociacaoNaoCumprida_titulo"),
							UteisJSF.internacionalizar("per_PermitirNegociarParcelasNegociacaoNaoCumprida_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoFinanceiroEnum.NEGOCIACAO_CONTA_RECEBER,
			PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_RECEBER),
	
	ALTERAR_SALDO_ANTERIOR_TURMA("SaldoAnteriorTurma",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirInformarSaldoAnteriorPrestacaoContaTurma_titulo"),
					UteisJSF.internacionalizar("per_PermitirInformarSaldoAnteriorPrestacaoContaTurma_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoFinanceiroEnum.PRESTACAO_CONTA_TURMA,
			PerfilAcessoSubModuloEnum.FINANCEIRO_PRESTACAO_CONTA),

	ALTERAR_SALDO_ANTERIOR_UNIDADE_ENSINO("SaldoAnteriorUnidadeEnsino",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirInformarSaldoAnteriorPrestacaoContaUnidadeEnsino_titulo"),
					UteisJSF.internacionalizar("per_PermitirInformarSaldoAnteriorPrestacaoContaUnidadeEnsino_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoFinanceiroEnum.PRESTACAO_CONTA_UNIDADE_ENSINO, 
			PerfilAcessoSubModuloEnum.FINANCEIRO_PRESTACAO_CONTA),
	
	INCLUIR_SALDO_RECEBER_TURMA("IncluirSaldoReceberTurma",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_IncluirNovoPagamentoPrestacaoContasTurma_titulo"),
					UteisJSF.internacionalizar("per_IncluirNovoPagamentoPrestacaoContasTurma_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoFinanceiroEnum.PRESTACAO_CONTA_TURMA,
			PerfilAcessoSubModuloEnum.FINANCEIRO_PRESTACAO_CONTA),
	
	INCLUIR_SALDO_RECEBER_UNIDADE_ENSINO("IncluirSaldoReceberUnidadeEnsino",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_IncluirNovoPagamentoPrestacaoContasUnidadeEnsino_titulo"),
					UteisJSF.internacionalizar("per_IncluirNovoPagamentoPrestacaoContasUnidadeEnsino_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoFinanceiroEnum.PRESTACAO_CONTA_UNIDADE_ENSINO, 
			PerfilAcessoSubModuloEnum.FINANCEIRO_PRESTACAO_CONTA),

	REALIZAR_NEGOCIACAO_CONTA_RECEBER_ARECEBER_VENCIDA("RealizarNegociacaoContaReceberAReceberVencida",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_RealizarNegociacaoContaReceberAReceberVencida_titulo"),
					UteisJSF.internacionalizar("per_RealizarNegociacaoContaReceberAReceberVencida_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoFinanceiroEnum.NEGOCIACAO_CONTA_RECEBER,
			PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_RECEBER),
	
	PERMITIR_RENEGOCIACAO_APENAS_COM_CONDICAO_RENEGOCIACAO("PermitirRenegociacaoApenasComCondicaoRenegociacao",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirRenegociacaoApenasComCondicaoRenegociacao_titulo"),
					UteisJSF.internacionalizar("per_PermitirRenegociacaoApenasComCondicaoRenegociacao_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoFinanceiroEnum.NEGOCIACAO_CONTA_RECEBER,
			PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_RECEBER),
	
	PERMITIR_INFORMAR_DESCONTO_PROG_INST_NEGOCIACAO_CONTA_RECEBER(
			"PermitirInformarDescontoProgInstNegociacaoContaReceber",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirInformarDescontoProgInstNegociacaoContaReceber_titulo"),
					UteisJSF.internacionalizar("per_PermitirInformarDescontoProgInstNegociacaoContaReceber_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoFinanceiroEnum.NEGOCIACAO_CONTA_RECEBER,
			PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_RECEBER),
	
	PERMITIR_LIBERAR_RENOVACAO_APOS_PAGAMENTO("PermitirLiberarRenovacaoApoPagamento",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirLiberarRenovacaoApoPagamento_titulo"),
					UteisJSF.internacionalizar("per_PermitirLiberarRenovacaoApoPagamento_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoFinanceiroEnum.NEGOCIACAO_CONTA_RECEBER,
			PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_RECEBER),
	
	LIBERAR_RENEGOCIACAO_PARA_NAO_VALIDAR_CONDICAO_RENEGOCIACAO("LiberarRenegociacaoParaNaoValidarCondicaoRenegociacao",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_LiberarRenegociacaoParaNaoValidarCondicaoRenegociacao_titulo"),
					UteisJSF.internacionalizar("per_LiberarRenegociacaoParaNaoValidarCondicaoRenegociacao_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoFinanceiroEnum.NEGOCIACAO_CONTA_RECEBER,
			PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_RECEBER),
	
	LIBERAR_USUARIO_NAO_VINCULADO_CONDICAO_RENEGOCIACAO_REALIZAR_NEGOCIACAO(
			"LiberarUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar(
							"per_LiberarUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao_titulo"),
					UteisJSF.internacionalizar(
							"per_LiberarUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoFinanceiroEnum.NEGOCIACAO_CONTA_RECEBER,
			PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_RECEBER),
	
	LIBERAR_PESSOA_COMISSIONADA_NEGOCIACAO_CONTA_RECEBER("LiberarPessoaComissionadaNegociacaoContaReceber",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_LiberarPessoaComissionadaNegociacaoContaReceber_titulo"),
					UteisJSF.internacionalizar("per_LiberarPessoaComissionadaNegociacaoContaReceber_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoFinanceiroEnum.NEGOCIACAO_CONTA_RECEBER,
			PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_RECEBER),
	
	LIBERAR_ISENCAO_JURO_MULTA_DESCONTO_NEGOCIACAO_CONTA_RECEBER(
			"LiberarIsencaoJuroMultaDescontoNegociacaoContaReceber",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_LiberarIsencaoJuroMultaDescontoNegociacaoContaReceber_titulo"),
					UteisJSF.internacionalizar("per_LiberarIsencaoJuroMultaDescontoNegociacaoContaReceber_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoFinanceiroEnum.NEGOCIACAO_CONTA_RECEBER,
			PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_RECEBER),
	
	/**
	* Mapa Recebimento Conta Receber Duplicidade
	*
	*/
	MAPA_RECEBIMENTO_CONTA_RECEBER_DUPLICIDADE("MapaRecebimentoContaReceberDuplicidade",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MapaRecebimentoContaReceberDuplicidade_titulo"),
					UteisJSF.internacionalizar("per_MapaRecebimentoContaReceberDuplicidade_ajuda"),
					new String[] { "mapaRecebimentoContaReceberDuplicidadeCons.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_RECEBER),
	/**
	* Conta Receber
	*
	*/
	CONTA_RECEBER("ContaReceber", new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
			UteisJSF.internacionalizar("per_ContaReceber_titulo"), UteisJSF.internacionalizar("per_ContaReceber_ajuda"),
			new String[] { "contaReceberCons.xhtml", "contaReceberForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_RECEBER),
	PERMITIR_CANCELAR_CONTA_RECEBER("PermitirCancelarContaReceber",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirCancelarContaReceber_titulo"),
					UteisJSF.internacionalizar("per_PermitirCancelarContaReceber_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoFinanceiroEnum.CONTA_RECEBER,
			PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_RECEBER),

	CONTA_RECEBER_PERMITIR_DESCONTO_FINANCEIRO_MANUAL_DESATIVADO(
			"ContaReceber_PermitirDescontoFinanceiroManualDesativado",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ContaReceberPermitirDescontoFinanceiroManualDesativado_titulo"),
					UteisJSF.internacionalizar("per_ContaReceberPermitirDescontoFinanceiroManualDesativado_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoFinanceiroEnum.CONTA_RECEBER,
			PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_RECEBER),

	CONTA_RECEBER_PERMITIR_IMPRESSAO_BOLETO_BLOQUEADO_MATRICULA_ALUNO_COM_DEBITOS(
			"ContaReceber_permitirImpressaoBoletoBloqueadoMatriculaAlunoComDebitos",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar(
							"per_ContaReceberpermitirImpressaoBoletoBloqueadoMatriculaAlunoComDebitos_titulo"),
					UteisJSF.internacionalizar(
							"per_ContaReceberpermitirImpressaoBoletoBloqueadoMatriculaAlunoComDebitos_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoFinanceiroEnum.CONTA_RECEBER,
			PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_RECEBER),

	CONTA_RECEBER_PERMITIR_ALTERAR_DATA_VENCIMENTO("ContaReceber_PermitirAlterarDataVencimento",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ContaReceberPermitirAlterarDataVencimento_titulo"),
					UteisJSF.internacionalizar("per_ContaReceberPermitirAlterarDataVencimento_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoFinanceiroEnum.CONTA_RECEBER,
			PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_RECEBER),
	
	CONTA_RECEBER_PERMITIR_EDITAR_MANUALMENTE_CONTA("ContaReceber_PermitirEditarManualmenteConta",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ContaReceberPermitirEditarManualmenteConta_titulo"),
					UteisJSF.internacionalizar("per_ContaReceberPermitirEditarManualmenteConta_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoFinanceiroEnum.CONTA_RECEBER,
			PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_RECEBER),
	
	CONTA_RECEBER_PERMITIR_LIBERAR_SUSPENSAO_CONVENIO("ContaReceberPermitirLiberarSuspensaoConvenio",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ContaReceberPermitirLiberarSuspensaoConvenio_titulo"),
					UteisJSF.internacionalizar("per_ContaReceberPermitirLiberarSuspensaoConvenio_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoFinanceiroEnum.CONTA_RECEBER,
			PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_RECEBER),	

	CONTA_RECEBER_PERMITIR_IMPRIMIR_BOLETO_RECEBIDO_CANCELADO_RENEGOCIADO(
			"ContaReceber_PermitirImprimirBoletoRecebidoCanceladoRenegociado",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar(
							"per_ContaReceberPermitirImprimirBoletoRecebidoCanceladoRenegociado_titulo"),
					UteisJSF.internacionalizar(
							"per_ContaReceberPermitirImprimirBoletoRecebidoCanceladoRenegociado_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoFinanceiroEnum.CONTA_RECEBER,
			PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_RECEBER),

	PAINEL_GESTOR_CONTAS_RECEBER("PainelGestorContasReceber",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PainelGestorContasReceber_titulo"),
					UteisJSF.internacionalizar("per_PainelGestorContasReceber_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoFinanceiroEnum.CONTA_RECEBER,
			PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_RECEBER),

	PERMITIR_ALTERAR_LANCAMENTO_CONTABIL_RECEBER("PermitirAlterarLancamentoContabilReceber",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirAlterarLancamentoContabilReceber_titulo"),
					UteisJSF.internacionalizar("per_PermitirAlterarLancamentoContabilReceber_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoFinanceiroEnum.CONTA_RECEBER,
			PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_RECEBER),
	
	CONTA_RECEBER_SIMULAR_ALTERACAO("ContaReceber_SimularAlteracao",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ContaReceberSimularAlteracao_titulo"),
					UteisJSF.internacionalizar("per_ContaReceberSimularAlteracao_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoFinanceiroEnum.CONTA_RECEBER,
			PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_RECEBER),

	CONTA_RECEBER_ISENTAR_CONTAS("ContaReceber_IsentarContas",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ContaReceberIsentarContas_titulo"),
					UteisJSF.internacionalizar("per_ContaReceberIsentarContas_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoFinanceiroEnum.CONTA_RECEBER,
			PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_RECEBER),

	PERMITIR_APENAS_CONTAS_DA_BIBLIOTECA("PermitirApenasContasDaBiblioteca",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirApenasContasDaBiblioteca_titulo"),
					UteisJSF.internacionalizar("per_PermitirApenasContasDaBiblioteca_ajuda")), },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoFinanceiroEnum.CONTA_RECEBER,
			PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_RECEBER),
	PERMITIR_ALTERAR_VALOR_CONTA_RECEBER("PermitirAlterarValorContaReceber",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirAlterarValorContaReceber_titulo"),
					UteisJSF.internacionalizar("per_PermitirAlterarValorContaReceber_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoFinanceiroEnum.CONTA_RECEBER,
			PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_RECEBER),

	CONTA_RECEBER_PERMITIR_ENVIO_EMAIL_COBRANCA("ContaReceber_PermitirEnvioEmailCobranca",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ContaReceberPermitirEnvioEmailCobranca_titulo"),
					UteisJSF.internacionalizar("per_ContaReceberPermitirEnvioEmailCobranca_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoFinanceiroEnum.CONTA_RECEBER,
			PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_RECEBER),
	
	PERMITE_ALTERAR_CENTRO_RESULTADO_CONTA_RECEBER("PermiteAlterarCentroResultadoContaReceber",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermiteAlterarCentroResultado_titulo"),
					UteisJSF.internacionalizar("per_PermiteAlterarCentroResultado_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoFinanceiroEnum.CONTA_RECEBER,
			PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_RECEBER),
	
	PERMITE_LIBERAR_INDICE_REAJUSTE_ATRASO_CONTA_RECEBER("PermiteLiberarIndiceReajusteAtrasoContaReceber",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermiteLiberarIndiceReajusteAtrasoContaReceber_titulo"),
					UteisJSF.internacionalizar("per_PermiteLiberarIndiceReajusteAtrasoContaReceber_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoFinanceiroEnum.CONTA_RECEBER,
			PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_RECEBER),
	
	/**
	* Listagem Alunos Desconto Chancela
	*
	*/
	LISTAGEM_ALUNOS_DESCONTO_CHANCELA("DescontoChancela", new PermissaoVisao[] { new PermissaoVisao(
			TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_ListagemAlunosDescontoChancela_titulo"),
			UteisJSF.internacionalizar("per_ListagemAlunosDescontoChancela_ajuda"),
			new String[] { "ListagemAlunosDescontoChancelaCons.xhtml", "ListagemAlunosDescontoChancelaForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_RECEBER),
	/**
	* Contratos Receitas
	*
	*/
	CONTRATOS_RECEITAS("ContratosReceitas",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ContratosReceitas_titulo"),
					UteisJSF.internacionalizar("per_ContratosReceitas_ajuda"),
					new String[] { "contratosReceitasCons.xhtml", "contratosReceitasForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_RECEBER),
	/**
	* Mudanca Carteira
	*
	*/
	MUDANCA_CARTEIRA("MudancaCarteira", new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
			UteisJSF.internacionalizar("per_MudancaCarteira_titulo"),
			UteisJSF.internacionalizar("per_MudancaCarteira_ajuda"), new String[] { "mudancaCarteira.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_RECEBER),
	/**
	* Definir Responsavel Financeiro
	*
	*/
	DEFINIR_RESPONSAVEL_FINANCEIRO("DefinirResponsavelFinanceiro",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_DefinirResponsavelFinanceiro_titulo"),
					UteisJSF.internacionalizar("per_DefinirResponsavelFinanceiro_ajuda"),
					new String[] { "definirResponsavelFinanceiro.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_RECEBER),
	/**
	* Condicao Renegociacao
	*
	*/
	CONDICAO_RENEGOCIACAO("CondicaoRenegociacao",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_CondicaoRenegociacao_titulo"),
					UteisJSF.internacionalizar("per_CondicaoRenegociacao_ajuda"),
					new String[] { "condicaoRenegociacaoCons.xhtml", "condicaoRenegociacaoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_RECEBER),
	
	CONDICAO_DESCONTO_RENEGOCIACAO("CondicaoDescontoRenegociacao", new PermissaoVisao[] { new PermissaoVisao(
			TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_CondicaoDescontoRenegociacao_titulo"),
			UteisJSF.internacionalizar("per_CondicaoDescontoRenegociacao_ajuda"),
			new String[] { "condicaoDescontoRenegociacaoCons.xhtml", "condicaoDescontoRenegociacaoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_RECEBER),
	/**
	* Conta Receber Agrupada
	*
	*/
	CONTA_RECEBER_AGRUPADA("ContaReceberAgrupada",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ContaReceberAgrupada_titulo"),
					UteisJSF.internacionalizar("per_ContaReceberAgrupada_ajuda"),
					new String[] { "contaReceberAgrupadaCons.xhtml", "contaReceberAgrupadaForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_RECEBER),
	CONTA_RECEBER_AGRUPADA_PERMITIR_PROCESSAR_AGRUPAMENTO("ContaReceberAgrupada_PermitirProcessarAgrupamento",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ContaReceberAgrupadaPermitirProcessarAgrupamento_titulo"),
					UteisJSF.internacionalizar("per_ContaReceberAgrupadaPermitirProcessarAgrupamento_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoFinanceiroEnum.CONTA_RECEBER_AGRUPADA,
			PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_RECEBER),
	/**
	* Geracao Manual Parcelas
	*
	*/
	GERACAO_MANUAL_PARCELAS("GeracaoManualParcelas",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_GeracaoManualParcelas_titulo"),
					UteisJSF.internacionalizar("per_GeracaoManualParcelas_ajuda"),
					new String[] { "geracaoManualParcelaCons.xhtml", "geracaoManualParcelaForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_RECEBER),
	/**
	* Índice de Reajuste
	*
	*/
	INDICE_REAJUSTE("IndiceReajuste",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_IndiceReajuste_titulo"),
					UteisJSF.internacionalizar("per_IndiceReajuste_ajuda"),
					new String[] { "indiceReajusteCons.xhtml", "indiceReajusteForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_RECEBER),
	PERMITIR_REALIZAR_CANCELAMENTO_REAJUSTE_PRECO("PermitirRealizarCancelamentoReajustePreco",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirRealizarCancelamentoReajustePreco_titulo"),
					UteisJSF.internacionalizar("per_PermitirRealizarCancelamentoReajustePreco_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoFinanceiroEnum.INDICE_REAJUSTE,
			PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_RECEBER),
	/**
	* Consulta Log Conta Receber
	*
	*/
	CONSULTA_LOG_CONTA_RECEBER("ConsultaLogContaReceber",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ConsultaLogContaReceber_titulo"),
					UteisJSF.internacionalizar("per_ConsultaLogContaReceber_ajuda"),
					new String[] { "consultaLogContaReceber.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_RECEBER),
	/**
	* Centro Receita
	*
	*/
	CENTRO_RECEITA("CentroReceita",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_CentroReceita_titulo"),
					UteisJSF.internacionalizar("per_CentroReceita_ajuda"),
					new String[] { "centroReceitaCons.xhtml", "centroReceitaForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_RECEBER),
	/**
	* Negociacao Recebimento
	*
	*/
	NEGOCIACAO_RECEBIMENTO("NegociacaoRecebimento",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_NegociacaoRecebimento_titulo"),
					UteisJSF.internacionalizar("per_NegociacaoRecebimento_ajuda"),
					new String[] { "negociacaoRecebimentoCons.xhtml", "negociacaoRecebimentoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_RECEBER),
	RECEBIMENTO_RETROATIVO("RecebimentoRetroativo",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_RecebimentoRetroativo_titulo"),
					UteisJSF.internacionalizar("per_RecebimentoRetroativo_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoFinanceiroEnum.NEGOCIACAO_RECEBIMENTO,
			PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_RECEBER),
	
	CAMPO_NUMERO_RECIBO_OBRIGATORIO("CampoNumeroReciboObrigatorio",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_CampoNumeroRecibo_titulo"),
					UteisJSF.internacionalizar("per_CampoNumeroRecibo_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoFinanceiroEnum.NEGOCIACAO_RECEBIMENTO,
			PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_RECEBER),

	PERMITIR_RECEBER_TERCEIRO("PermitirReceberTerceiro",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirReceberTerceiro_titulo"),
					UteisJSF.internacionalizar("per_PermitirReceberTerceiro_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoFinanceiroEnum.NEGOCIACAO_RECEBIMENTO,
			PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_RECEBER),
	BLOQUEAR_DESCONTO_RECEBIMENTO("BloquearDescontoRecebimento",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_BloquearDescontoRecebimento_titulo"),
					UteisJSF.internacionalizar("per_BloquearDescontoRecebimento_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoFinanceiroEnum.NEGOCIACAO_RECEBIMENTO,
			PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_RECEBER),
	RECEBIMENTO_PERMITIR_ALTERAR_CONTA_CAIXA_ESTORNO("Recebimento_PermitirAlterarContaCaixaEstorno",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_RecebimentoPermitirAlterarContaCaixaEstorno_titulo"),
					UteisJSF.internacionalizar("per_RecebimentoPermitirAlterarContaCaixaEstorno_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoFinanceiroEnum.NEGOCIACAO_RECEBIMENTO,
			PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_RECEBER),
	RECEBIMENTO_PERMITIR_ESTORNAR_RECEBIMENTO_CARTAO_CREDITO_JA_RECEBIDO(
			"Recebimento_PermitirEstornarRecebimentoCartaoCreditoJaRecebido",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar(
							"per_Recebimento_PermitirEstornarRecebimentoCartaoCreditoJaRecebido_titulo"),
					UteisJSF.internacionalizar(
							"per_Recebimento_PermitirEstornarRecebimentoCartaoCreditoJaRecebido_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoFinanceiroEnum.NEGOCIACAO_RECEBIMENTO,
			PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_RECEBER),
	/**
	* Geracao Manual Parcelas Aluno
	*
	*/
	GERACAO_MANUAL_PARCELAS_ALUNO("GeracaoManualParcelasAluno",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_GeracaoManualParcelasAluno_titulo"),
					UteisJSF.internacionalizar("per_GeracaoManualParcelasAluno_ajuda"),
					new String[] { "geracaoManualParcelasAluno.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_RECEBER),
	/**
	* Convenio
	*
	*/
	CONVENIO("Convenio",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_Convenio_titulo"), UteisJSF.internacionalizar("per_Convenio_ajuda"),
					new String[] { "convenioCons.xhtml", "convenioForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.FINANCEIRO_BANCO_PARCEIRO),
	CONVENIO_FINALIZAR_CONVENIO("Convenio_FinalizarConvenio",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ConvenioFinalizarConvenio_titulo"),
					UteisJSF.internacionalizar("per_ConvenioFinalizarConvenio_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoFinanceiroEnum.CONVENIO,
			PerfilAcessoSubModuloEnum.FINANCEIRO_BANCO_PARCEIRO),
	CONVENIO_AUTORIZAR_CONVENIO("Convenio_AutorizarConvenio",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ConvenioAutorizarConvenio_titulo"),
					UteisJSF.internacionalizar("per_ConvenioAutorizarConvenio_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoFinanceiroEnum.CONVENIO,
			PerfilAcessoSubModuloEnum.FINANCEIRO_BANCO_PARCEIRO),
	/**
	* Controle Movimentacao Remessa
	*
	*/
//	CONTROLE_MOVIMENTACAO_REMESSA("ControleMovimentacaoRemessa", new PermissaoVisao[] {
//			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_ControleMovimentacaoRemessa_titulo"),UteisJSF.internacionalizar("per_ControleMovimentacaoRemessa_ajuda"), new String[]{"controleMovimentacaoRemessaCons.xhtml","controleMovimentacaoRemessaForm.xhtml"})
//			},
//			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
//			null, 
//			PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_RECEBER),
	
	/**
	* Matricula Liberacao
	*
	*/
	MATRICULA_LIBERACAO("MatriculaLiberacao",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MatriculaLiberacao_titulo"),
					UteisJSF.internacionalizar("per_MatriculaLiberacao_ajuda"),
					new String[] { "matriculaLiberacaoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.FINANCEIRO_BANCO_PARCEIRO),
	/**
	* Parceiro
	*
	*/
	PARCEIRO("Parceiro", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_Parceiro_titulo"),
					UteisJSF.internacionalizar("per_Parceiro_ajuda"),
					new String[] { "parceiroCons.xhtml", "parceiroForm.xhtml" }),
			new PermissaoVisao(TipoVisaoEnum.PARCEIRO, UteisJSF.internacionalizar("per_Parceiro_titulo"),
					UteisJSF.internacionalizar("per_Parceiro_ajuda"), new String[] { "cadastroVisaoParceiro.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.FINANCEIRO_BANCO_PARCEIRO),
	PARCEIRO_UNIFICAR_PARCEIROS_DUPLICADOS("Parceiro_unificarParceirosDuplicados",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ParceirounificarParceirosDuplicados_titulo"),
					UteisJSF.internacionalizar("per_ParceirounificarParceirosDuplicados_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoFinanceiroEnum.PARCEIRO,
			PerfilAcessoSubModuloEnum.FINANCEIRO_BANCO_PARCEIRO),
	PARCEIRO_PERMITE_ALTERAR_DADOS("Parceiro_permiteAlterarDados",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ParceiropermiteAlterarDados_titulo"),
					UteisJSF.internacionalizar("per_ParceiropermiteAlterarDados_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoFinanceiroEnum.PARCEIRO,
			PerfilAcessoSubModuloEnum.FINANCEIRO_BANCO_PARCEIRO),
	/**
	* Conta Receber Nao Localizada Arquivo Retorno
	*
	*/
	CONTA_RECEBER_NAO_LOCALIZADA_ARQUIVO_RETORNO("ContaReceberNaoLocalizadaArquivoRetorno",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ContaReceberNaoLocalizadaArquivoRetorno_titulo"),
					UteisJSF.internacionalizar("per_ContaReceberNaoLocalizadaArquivoRetorno_ajuda"),
					new String[] { "contaReceberNaoLocalizadaArquivoRetornoCons.xhtml",
							"contaReceberNaoLocalizadaArquivoRetornoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_RECEBER),
	/**
	* Matricula Serasa
	*
	*/
	MATRICULA_SERASA("MatriculaSerasa", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_MatriculaSerasa_titulo"),UteisJSF.internacionalizar("per_MatriculaSerasa_ajuda"), new String[]{"matriculaSerasaForm.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.FINANCEIRO_BANCO_PARCEIRO),
	/**
	* Mapa Pendencias Controle Cobranca
	*
	*/
	MAPA_PENDENCIAS_CONTROLE_COBRANCA("MapaPendenciasControleCobranca",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MapaPendenciasControleCobranca_titulo"),
					UteisJSF.internacionalizar("per_MapaPendenciasControleCobranca_ajuda"),
					new String[] { "mapaPendenciasControleCobrancaForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_RECEBER),
	ALTERAR_DESCONTO_CONTA_RECEBER("AlterarDescontoContaReceber",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_AlterarDescontoContaReceber_titulo"),
					UteisJSF.internacionalizar("per_AlterarDescontoContaReceber_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoFinanceiroEnum.MAPA_PENDENCIAS_CONTROLE_COBRANCA, 
			PerfilAcessoSubModuloEnum.FINANCEIRO_BANCO_PARCEIRO),
	
	/**
	* Controle Cobranca
	*
	*/
	CONTROLE_COBRANCA("ControleCobranca",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ControleCobranca_titulo"),
					UteisJSF.internacionalizar("per_ControleCobranca_ajuda"),
					new String[] { "controleCobrancaCons.xhtml", "controleCobrancaForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_RECEBER),

	/**
	* Controle Cobranca
	*
	*/
	PROCESSAMENTO_ARQUIVO_RETORNO_PARCEIRO("ProcessamentoArquivoRetornoParceiro",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ProcessamentoArquivoRetornoParceiro_titulo"),
					UteisJSF.internacionalizar("per_ProcessamentoArquivoRetornoParceiro_ajuda"),
					new String[] { "processamentoArquivoRetornoParceiroCons.xhtml",
							"processamentoArquivoRetornoParceiroForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_RECEBER),
	
	/**
	* Operadora Cartao
	*
	*/
	OPERADORA_CARTAO("OperadoraCartao",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_OperadoraCartao_titulo"),
					UteisJSF.internacionalizar("per_OperadoraCartao_ajuda"),
					new String[] { "operadoraCartaoCons.xhtml", "operadoraCartaoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.FINANCEIRO_BANCO_PARCEIRO),
	/**
	* Forma Pagamento
	*
	*/
	FORMA_PAGAMENTO("FormaPagamento",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_FormaPagamento_titulo"),
					UteisJSF.internacionalizar("per_FormaPagamento_ajuda"),
					new String[] { "formaPagamentoCons.xhtml", "formaPagamentoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null,
			PerfilAcessoSubModuloEnum.FINANCEIRO_BANCO_PARCEIRO),
	/**
	* Agencia
	*
	*/
	AGENCIA("Agencia",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_Agencia_titulo"), UteisJSF.internacionalizar("per_Agencia_ajuda"),
					new String[] { "agenciaCons.xhtml", "agenciaForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.FINANCEIRO_BANCO_PARCEIRO),
	/**
	* Controle Remessa
	*
	*/
	CONTROLE_REMESSA("ControleRemessa",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ControleRemessa_titulo"),
					UteisJSF.internacionalizar("per_ControleRemessa_ajuda"),
					new String[] { "controleRemessaCons.xhtml", "controleRemessaForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_RECEBER),
	PERMITIR_ENVIAR_REMESSA_SEM_REAJUSTE_PRECO("PermitirEnviarRemessaSemReajustePreco",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirEnviarRemessaSemReajustePreco_titulo"),
					UteisJSF.internacionalizar("per_PermitirEnviarRemessaSemReajustePreco_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoFinanceiroEnum.CONTROLE_REMESSA,
			PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_RECEBER),
	/**
	* Banco
	*
	*/
	BANCO("Banco",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_Banco_titulo"), UteisJSF.internacionalizar("per_Banco_ajuda"),
					new String[] { "bancoCons.xhtml", "bancoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.FINANCEIRO_BANCO_PARCEIRO),
	/**
	* Conta Corrente
	*
	*/
	CONTA_CORRENTE("ContaCorrente",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ContaCorrente_titulo"),
					UteisJSF.internacionalizar("per_ContaCorrente_ajuda"),
					new String[] { "contaCorrenteCons.xhtml", "contaCorrenteForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.FINANCEIRO_BANCO_PARCEIRO),
	
	CONTA_CORRENTE_PAGAR_RECEBER_CAIXA_UNIDADE_DIFERENTE("ContaCorrente_PagarReceberCaixaUnidadeDiferente",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ContaCorrentePagarReceberCaixaUnidadeDiferente_titulo"),
					UteisJSF.internacionalizar("per_ContaCorrentePagarReceberCaixaUnidadeDiferente_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoFinanceiroEnum.CONTA_CORRENTE,
			PerfilAcessoSubModuloEnum.FINANCEIRO_BANCO_PARCEIRO),
	/**
	* Condicao Pagamento
	*
	*/
	CONDICAO_PAGAMENTO("CondicaoPagamento",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_CondicaoPagamento_titulo"),
					UteisJSF.internacionalizar("per_CondicaoPagamento_ajuda"),
					new String[] { "condicaoPagamentoCons.xhtml", "condicaoPagamentoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null,
			PerfilAcessoSubModuloEnum.FINANCEIRO_BANCO_PARCEIRO),
	/**
	* Modelo Boleto
	*
	*/
	MODELO_BOLETO("ModeloBoleto", new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
			UteisJSF.internacionalizar("per_ModeloBoleto_titulo"), UteisJSF.internacionalizar("per_ModeloBoleto_ajuda"),
			new String[] { "modeloBoletoCons.xhtml", "modeloBoletoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.FINANCEIRO_BANCO_PARCEIRO),
	/**
	* Controle Geracao Parcela Turma
	*
	*/
	CONTROLE_GERACAO_PARCELA_TURMA("ControleGeracaoParcelaTurma", new PermissaoVisao[] { new PermissaoVisao(
			TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_ControleGeracaoParcelaTurma_titulo"),
			UteisJSF.internacionalizar("per_ControleGeracaoParcelaTurma_ajuda"),
			new String[] { "controleGeracaoParcelaTurmaCons.xhtml", "controleGeracaoParcelaTurmaForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.FINANCEIRO_FINANCEIRO_ACADEMICO),
	/**
	* Plano Financeiro Reposicao
	*
	*/
	PLANO_FINANCEIRO_REPOSICAO("PlanoFinanceiroReposicao",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PlanoFinanceiroReposicao_titulo"),
					UteisJSF.internacionalizar("per_PlanoFinanceiroReposicao_ajuda"),
					new String[] { "planoFinanceiroReposicaoCons.xhtml", "planoFinanceiroReposicaoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.FINANCEIRO_FINANCEIRO_ACADEMICO),
	/**
	* Alteracao Plano Financeiro Aluno
	*
	*/
	ALTERACAO_PLANO_FINANCEIRO_ALUNO("AlteracaoPlanoFinanceiroAluno", new PermissaoVisao[] { new PermissaoVisao(
			TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_AlteracaoPlanoFinanceiroAluno_titulo"),
			UteisJSF.internacionalizar("per_AlteracaoPlanoFinanceiroAluno_ajuda"),
			new String[] { "alteracaoPlanoFinanceiroAlunoCons.xhtml", "alteracaoPlanoFinanceiroAlunoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.FINANCEIRO_FINANCEIRO_ACADEMICO),
	ALTERACAO_PLANO_FINANCEIRO_ALUNO_PERMITIR_LIBERACAO_DESBLOQUEIO_POR_SENHA_DESCONTO_ALUNO(
			"AlteracaoPlanoFinanceiroAluno_PermitirLiberacaoDesbloqueioPorSenhaDescontoAluno",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar(
					"per_AlteracaoPlanoFinanceiroAlunoPermitirLiberacaoDesbloqueioPorSenhaDescontoAluno_titulo"),
					UteisJSF.internacionalizar(
							"per_AlteracaoPlanoFinanceiroAlunoPermitirLiberacaoDesbloqueioPorSenhaDescontoAluno_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoFinanceiroEnum.ALTERACAO_PLANO_FINANCEIRO_ALUNO, 
			PerfilAcessoSubModuloEnum.FINANCEIRO_FINANCEIRO_ACADEMICO),
	ALTERACAO_PLANO_FINANCEIRO_ALUNO_ALTERAR_ORDEM_DESCONTOS("AlteracaoPlanoFinanceiroAluno_AlterarOrdemDescontos",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_AlteracaoPlanoFinanceiroAlunoAlterarOrdemDescontos_titulo"),
					UteisJSF.internacionalizar("per_AlteracaoPlanoFinanceiroAlunoAlterarOrdemDescontos_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoFinanceiroEnum.ALTERACAO_PLANO_FINANCEIRO_ALUNO, 
			PerfilAcessoSubModuloEnum.FINANCEIRO_FINANCEIRO_ACADEMICO),
	ALTERACAO_PLANO_FINANCEIRO_ALUNO_VISUALIZAR_ABA_DESCONTO_PLANO_FINANCEIRO(
			"AlteracaoPlanoFinanceiroAluno_VisualizarAbaDescontoPlanoFinanceiro",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar(
							"per_AlteracaoPlanoFinanceiroAlunoVisualizarAbaDescontoPlanoFinanceiro_titulo"),
					UteisJSF.internacionalizar(
							"per_AlteracaoPlanoFinanceiroAlunoVisualizarAbaDescontoPlanoFinanceiro_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoFinanceiroEnum.ALTERACAO_PLANO_FINANCEIRO_ALUNO, 
			PerfilAcessoSubModuloEnum.FINANCEIRO_FINANCEIRO_ACADEMICO),
	ALTERACAO_PLANO_FINANCEIRO_ALUNO_VISUALIZAR_ABA_DESCONTOS("AlteracaoPlanoFinanceiroAluno_VisualizarAbaDescontos",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_AlteracaoPlanoFinanceiroAlunoVisualizarAbaDescontos_titulo"),
					UteisJSF.internacionalizar("per_AlteracaoPlanoFinanceiroAlunoVisualizarAbaDescontos_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoFinanceiroEnum.ALTERACAO_PLANO_FINANCEIRO_ALUNO, 
			PerfilAcessoSubModuloEnum.FINANCEIRO_FINANCEIRO_ACADEMICO),
	ALTERACAO_PLANO_FINANCEIRO_ALUNO_LIBERAR_DESCONTO_PROGRESSIVO_PRIMEIRA_PARCELA(
			"AlteracaoPlanoFinanceiroAluno_LiberarDescontoProgressivoPrimeiraParcela",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar(
							"per_AlteracaoPlanoFinanceiroAlunoLiberarDescontoProgressivoPrimeiraParcela_titulo"),
					UteisJSF.internacionalizar(
							"per_AlteracaoPlanoFinanceiroAlunoLiberarDescontoProgressivoPrimeiraParcela_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoFinanceiroEnum.ALTERACAO_PLANO_FINANCEIRO_ALUNO, 
			PerfilAcessoSubModuloEnum.FINANCEIRO_FINANCEIRO_ACADEMICO),
	ALTERACAO_PLANO_FINANCEIRO_ALUNO_BLOQUEAR_LANCAMENTO_DESCONTO_PARCELA_MATRICULA(
			"AlteracaoPlanoFinanceiroAluno_BloquearLancamentoDescontoParcelaMatricula",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar(
							"per_AlteracaoPlanoFinanceiroAlunoBloquearLancamentoDescontoParcelaMatricula_titulo"),
					UteisJSF.internacionalizar(
							"per_AlteracaoPlanoFinanceiroAlunoBloquearLancamentoDescontoParcelaMatricula_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoFinanceiroEnum.ALTERACAO_PLANO_FINANCEIRO_ALUNO, 
			PerfilAcessoSubModuloEnum.FINANCEIRO_FINANCEIRO_ACADEMICO),
	ALTERACAO_PLANO_FINANCEIRO_ALUNO_ALTERAR_CATEGORIA_E_CONDICAO_PAGAMENTO(
			"AlteracaoPlanoFinanceiroAluno_AlterarCategoriaECondicaoPagamento",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar(
							"per_AlteracaoPlanoFinanceiroAlunoAlterarCategoriaECondicaoPagamento_titulo"),
					UteisJSF.internacionalizar(
							"per_AlteracaoPlanoFinanceiroAlunoAlterarCategoriaECondicaoPagamento_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoFinanceiroEnum.ALTERACAO_PLANO_FINANCEIRO_ALUNO, 
			PerfilAcessoSubModuloEnum.FINANCEIRO_FINANCEIRO_ACADEMICO),
	/**
	* Integracao Financeiro
	*
	*/
	INTEGRACAO_FINANCEIRO("IntegracaoFinanceiro",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_IntegracaoFinanceiro_titulo"),
					UteisJSF.internacionalizar("per_IntegracaoFinanceiro_ajuda"),
					new String[] { "integracaoFinanceiroCons.xhtml", "integracaoFinanceiroForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.FINANCEIRO_FINANCEIRO_ACADEMICO),
	/**
	* Plano Desconto
	*
	*/
	PLANO_DESCONTO("PlanoDesconto",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PlanoDesconto_titulo"),
					UteisJSF.internacionalizar("per_PlanoDesconto_ajuda"),
					new String[] { "planoDescontoCons.xhtml", "planoDescontoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.FINANCEIRO_FINANCEIRO_ACADEMICO),
	/**
	* Categoria Desconto
	*
	*/
	CATEGORIA_DESCONTO("CategoriaDesconto",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_CategoriaDesconto_titulo"),
					UteisJSF.internacionalizar("per_CategoriaDesconto_ajuda"),
					new String[] { "categoriaDescontoCons.xhtml", "categoriaDescontoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.FINANCEIRO_FINANCEIRO_ACADEMICO),
	/**
	* Plano Desconto Inclusao Disciplina
	*
	*/
	PLANO_DESCONTO_INCLUSAO_DISCIPLINA("PlanoDescontoInclusaoDisciplina",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PlanoDescontoInclusaoDisciplina_titulo"),
					UteisJSF.internacionalizar("per_PlanoDescontoInclusaoDisciplina_ajuda"),
					new String[] { "planoDescontoInclusaoDisciplinaCons.xhtml",
							"planoDescontoInclusaoDisciplinaForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.FINANCEIRO_FINANCEIRO_ACADEMICO),
	/**
	* Desconto Progressivo
	*
	*/
	DESCONTO_PROGRESSIVO("DescontoProgressivo",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_DescontoProgressivo_titulo"),
					UteisJSF.internacionalizar("per_DescontoProgressivo_ajuda"),
					new String[] { "descontoProgressivoCons.xhtml", "descontoProgressivoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.FINANCEIRO_FINANCEIRO_ACADEMICO),
	/**
	* Atualizacao Vencimentos
	*
	*/
	ATUALIZACAO_VENCIMENTOS("AtualizacaoVencimentos",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_AtualizacaoVencimentos_titulo"),
					UteisJSF.internacionalizar("per_AtualizacaoVencimentos_ajuda"),
					new String[] { "atualizacaoVencimentoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.FINANCEIRO_FINANCEIRO_ACADEMICO),
	ATUALIZACAO_VENCIMENTO_MENSALIDADE("AtualizacaoVencimento_Mensalidade",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_AtualizacaoVencimentoMensalidade_titulo"),
					UteisJSF.internacionalizar("per_AtualizacaoVencimentoMensalidade_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoFinanceiroEnum.ATUALIZACAO_VENCIMENTOS,
			PerfilAcessoSubModuloEnum.FINANCEIRO_FINANCEIRO_ACADEMICO),

	ATUALIZACAO_VENCIMENTO_CONTA_VENCIDA("AtualizacaoVencimento_ContaVencida",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_AtualizacaoVencimentoContaVencida_titulo"),
					UteisJSF.internacionalizar("per_AtualizacaoVencimentoContaVencida_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoFinanceiroEnum.ATUALIZACAO_VENCIMENTOS,
			PerfilAcessoSubModuloEnum.FINANCEIRO_FINANCEIRO_ACADEMICO),

	ATUALIZACAO_VENCIMENTO_MATRICULA("AtualizacaoVencimento_Matricula",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_AtualizacaoVencimentoMatricula_titulo"),
					UteisJSF.internacionalizar("per_AtualizacaoVencimentoMatricula_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoFinanceiroEnum.ATUALIZACAO_VENCIMENTOS,
			PerfilAcessoSubModuloEnum.FINANCEIRO_FINANCEIRO_ACADEMICO),
	PERMITIR_ALTERAR_DATA_COMPETENCIA_MESMO_MES_ANO_VENCIMENTO("PermitirAlterarDataCompetenciaMesmoMesAnoVencimento",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirAlterarDataCompetenciaMesmoMesAnoVencimento_titulo"),
					UteisJSF.internacionalizar("per_PermitirAlterarDataCompetenciaMesmoMesAnoVencimento_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoFinanceiroEnum.ATUALIZACAO_VENCIMENTOS,
			PerfilAcessoSubModuloEnum.FINANCEIRO_FINANCEIRO_ACADEMICO),
	/**
	* Plano Financeiro Curso
	*
	*/
	PLANO_FINANCEIRO_CURSO("PlanoFinanceiroCurso",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PlanoFinanceiroCurso_titulo"),
					UteisJSF.internacionalizar("per_PlanoFinanceiroCurso_ajuda"),
					new String[] { "planoFinanceiroCursoCons.xhtml", "planoFinanceiroCursoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.FINANCEIRO_FINANCEIRO_ACADEMICO),
	/**
	* Chancela
	*
	*/
	CHANCELA("Chancela",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_Chancela_titulo"), UteisJSF.internacionalizar("per_Chancela_ajuda"),
					new String[] { "chancelaCons.xhtml", "chancelaForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.FINANCEIRO_FINANCEIRO_ACADEMICO),
	/**
	* Taxa Requerimento
	*
	*/
	TAXA_REQUERIMENTO("TaxaRequerimento",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_TaxaRequerimento_titulo"),
					UteisJSF.internacionalizar("per_TaxaRequerimento_ajuda"),
					new String[] { "taxaValorCons.xhtml", "taxaValorForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.FINANCEIRO_FINANCEIRO_ACADEMICO),
	/**
	* Conta Pagar
	*
	*/
	CONTA_PAGAR("ContaPagar", new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
			UteisJSF.internacionalizar("per_ContaPagar_titulo"), UteisJSF.internacionalizar("per_ContaPagar_ajuda"),
			new String[] { "contaPagarCons.xhtml", "contaPagarForm.xhtml" }) }, TipoPerfilAcessoPermissaoEnum.ENTIDADE,
			null, PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_PAGAR),
	RENEGOCIAR_CONTA_PAGAR("RenegociarContaPagar",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_RenegociarContaPagar_titulo"),
					UteisJSF.internacionalizar("per_RenegociarContaPagar_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoFinanceiroEnum.CONTA_PAGAR,
			PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_PAGAR),
	PAINEL_GESTOR_CONTAS_PAGAR("PainelGestorContasPagar",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PainelGestorContasPagar_titulo"),
					UteisJSF.internacionalizar("per_PainelGestorContasPagar_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			
			PerfilAcessoPermissaoFinanceiroEnum.CONTA_PAGAR, PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_PAGAR),
	
	PERMITIR_ALTERAR_LANCAMENTO_CONTABIL_PAGAR("PermitirAlterarLancamentoContabilPagar",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirAlterarLancamentoContabilPagar_titulo"),
					UteisJSF.internacionalizar("per_PermitirAlterarLancamentoContabilPagar_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			
			PerfilAcessoPermissaoFinanceiroEnum.CONTA_PAGAR, PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_PAGAR),
	
	PERMITE_ALTERAR_CENTRO_RESULTADO_CONTA_PAGAR("PermiteAlterarCentroResultadoContaPagar",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermiteAlterarCentroResultado_titulo"),
					UteisJSF.internacionalizar("per_PermiteAlterarCentroResultado_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoFinanceiroEnum.CONTA_PAGAR,
			PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_PAGAR),
	
	FORNECEDOR("Fornecedor", new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
			UteisJSF.internacionalizar("per_Fornecedor_titulo"), UteisJSF.internacionalizar("per_Fornecedor_ajuda"),
			new String[] { "fornecedorCons.xhtml", "fornecedorForm.xhtml" }) }, TipoPerfilAcessoPermissaoEnum.ENTIDADE,
			null, PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_PAGAR),
	
	/**
	* Pagamento
	*
	*/
	PAGAMENTO("Pagamento", new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
			UteisJSF.internacionalizar("per_Pagamento_titulo"), UteisJSF.internacionalizar("per_Pagamento_ajuda"),
			new String[] { "negociacaoPagamentoCons.xhtml", "negociacaoPagamentoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_PAGAR),
	
	GESTAO_CONTAS_PAGAR("GestaoContasPagar", new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
			UteisJSF.internacionalizar("per_GestaoContasPagar_titulo"),
			UteisJSF.internacionalizar("per_GestaoContasPagar_ajuda"), new String[] { "gestaoContasPagar.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_PAGAR),
	
	PERMITE_OPERACAO_ALTERACAO_GESTAO_CONTAS_PAGAR("PermiteOperacaoAlteracaoGestaoContasPagar",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermiteOperacaoAlteracaoGestaoContasPagar_titulo"),
					UteisJSF.internacionalizar("per_PermiteOperacaoAlteracaoGestaoContasPagar_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoFinanceiroEnum.GESTAO_CONTAS_PAGAR,
			PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_PAGAR),
	
	PERMITE_OPERACAO_CANCELAMENTO_GESTAO_CONTAS_PAGAR("PermiteOperacaoCancelamentoGestaoContasPagar",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermiteOperacaoCancelamentoGestaoContasPagar_titulo"),
					UteisJSF.internacionalizar("per_PermiteOperacaoCancelamentoGestaoContasPagar_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoFinanceiroEnum.GESTAO_CONTAS_PAGAR,
			PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_PAGAR),
	
	PERMITE_OPERACAO_ESTORNO_CANCELAMENTO_GESTAO_CONTAS_PAGAR("PermiteOperacaoEstornoCancelamentoGestaoContasPagar",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermiteOperacaoEstornoCancelamentoGestaoContasPagar_titulo"),
					UteisJSF.internacionalizar("per_PermiteOperacaoEstornoCancelamentoGestaoContasPagar_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoFinanceiroEnum.GESTAO_CONTAS_PAGAR,
			PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_PAGAR),
	
	PERMITE_OPERACAO_EXCLUSAO_GESTAO_CONTAS_PAGAR("PermiteOperacaoExclusaoGestaoContasPagar",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermiteOperacaoExclusaoGestaoContasPagar_titulo"),
					UteisJSF.internacionalizar("per_PermiteOperacaoExclusaoGestaoContasPagar_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoFinanceiroEnum.GESTAO_CONTAS_PAGAR,
			PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_PAGAR),
	
	PERMITE_OPERACAO_PAGAMENTO_GESTAO_CONTAS_PAGAR("PermiteOperacaoPagamentoGestaoContasPagar",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermiteOperacaoPagamentoGestaoContasPagar_titulo"),
					UteisJSF.internacionalizar("per_PermiteOperacaoPagamentoGestaoContasPagar_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoFinanceiroEnum.GESTAO_CONTAS_PAGAR,
			PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_PAGAR),
	
	PERMITE_OPERACAO_ESTORNO_PAGAMENTO_GESTAO_CONTAS_PAGAR("PermiteOperacaoEstornoPagamentoGestaoContasPagar",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermiteOperacaoEstornoPagamentoGestaoContasPagar_titulo"),
					UteisJSF.internacionalizar("per_PermiteOperacaoEstornoPagamentoGestaoContasPagar_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoFinanceiroEnum.GESTAO_CONTAS_PAGAR,
			PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_PAGAR),
	
	/**
	 * Grupo Conta Pagar Permissão desativada temporariamente porque a tela não
	 * atende mais as contas a pagar com centro de resultado
	*
	*/
	
//	GRUPO_CONTA_PAGAR("GrupoContaPagar", new PermissaoVisao[] {
//			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_GrupoContaPagar_titulo"),UteisJSF.internacionalizar("per_GrupoContaPagar_ajuda"), new String[]{"grupoContaPagarCons.xhtml","grupoContaPagarForm.xhtml"})
//			},
//			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
//			null, 
//			PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_PAGAR),
	/**
	* Contratos Despesas
	*
	*/
	CONTRATOS_DESPESAS("ContratosDespesas",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ContratosDespesas_titulo"),
					UteisJSF.internacionalizar("per_ContratosDespesas_ajuda"),
					new String[] { "contratosDespesasCons.xhtml", "contratosDespesasForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_PAGAR),
	INDEFERIR_CONTRATO_DESPESA("IndeferirContratoDespesa",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_IndeferirContratoDespesa_titulo"),
					UteisJSF.internacionalizar("per_IndeferirContratoDespesa_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoFinanceiroEnum.CONTRATOS_DESPESAS,
			PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_PAGAR),
	/**
	* Categoria Despesa
	*
	*/
	CATEGORIA_DESPESA("CategoriaDespesa",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_CategoriaDespesa_titulo"),
					UteisJSF.internacionalizar("per_CategoriaDespesa_ajuda"),
					new String[] { "categoriaDespesaCons.xhtml", "categoriaDespesaForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null,
			PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_PAGAR),
	
	/**
	* Controle Remessa
	*
	*/
	CONTROLE_REMESSA_CONTA_PAGAR("ControleRemessaContaPagar",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ControleRemessaPagar_titulo"),
					UteisJSF.internacionalizar("per_ControleRemessaPagar_ajuda"),
					new String[] { "controleRemessaContaPagarCons.xhtml", "controleRemessaContaPagarForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_PAGAR),
	
	/**
	* Controle Cobranca Pagar
	*
	*/
	CONTROLE_COBRANCA_PAGAR("ControleCobrancaPagar",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ControleCobrancaPagar_titulo"),
					UteisJSF.internacionalizar("per_ControleCobrancaPagar_ajuda"),
					new String[] { "controleCobrancaPagarCons.xhtml", "controleCobrancaPagarForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_PAGAR),
	
	/**
	* Painel Gestor Visualizar Financeiro
	*
	*/
	PAINEL_GESTOR_VISUALIZAR_FINANCEIRO("PainelGestorVisualizarFinanceiro",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PainelGestorVisualizarFinanceiro_titulo"),
					UteisJSF.internacionalizar("per_PainelGestorVisualizarFinanceiro_ajuda"), new String[] { "" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.FINANCEIRO_SEI_DECIDIR_FINANCEIRO),
	PAINEL_GESTOR_COMUNICACAO_INTERNA_FINANCEIRO("PainelGestorComunicacaoInternaFinanceiro",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PainelGestorComunicacaoInternaFinanceiro_titulo"),
					UteisJSF.internacionalizar("per_PainelGestorComunicacaoInternaFinanceiro_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoFinanceiroEnum.PAINEL_GESTOR_VISUALIZAR_FINANCEIRO, 
			PerfilAcessoSubModuloEnum.FINANCEIRO_SEI_DECIDIR_FINANCEIRO),
	PAINEL_GESTOR_REQUERIMENTOS_FINANCEIRO("PainelGestorRequerimentosFinanceiro",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PainelGestorRequerimentosFinanceiro_titulo"),
					UteisJSF.internacionalizar("per_PainelGestorRequerimentosFinanceiro_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoFinanceiroEnum.PAINEL_GESTOR_VISUALIZAR_FINANCEIRO, 
			PerfilAcessoSubModuloEnum.FINANCEIRO_SEI_DECIDIR_FINANCEIRO),
	/**
	* Relatorio SEI Decidir Despesa
	*
	*/
	RELATORIO_SEIDECIDIR_DESPESA("RelatorioSEIDecidirDespesa",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_RelatorioSEIDecidirDespesa_titulo"),
					UteisJSF.internacionalizar("per_RelatorioSEIDecidirDespesa_ajuda"),
					new String[] { "relatorioSEIDecidirRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.FINANCEIRO_RELATORIOS_CONTA_PAGAR),
	PERMITIR_GERAR_RELATORIO_SEIDECIDIR_DESPESA_APENAS_DADOS("PermitirGerarRelatorioSeiDecidirDespesaApenasDados",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirGerarRelatorioSeiDecidirDespesaApenasDados_titulo"),
					UteisJSF.internacionalizar("per_PermitirGerarRelatorioSeiDecidirDespesaApenasDados_ajuda"),
					new String[] { "relatorioSEIDecidirRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, RELATORIO_SEIDECIDIR_DESPESA,
			PerfilAcessoSubModuloEnum.FINANCEIRO_RELATORIOS_CONTA_PAGAR),
	/**
	* Extrato Conta Corrente Rel
	*
	*/
	EXTRATO_CONTA_CORRENTE_REL("ExtratoContaCorrenteRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ExtratoContaCorrenteRel_titulo"),
					UteisJSF.internacionalizar("per_ExtratoContaCorrenteRel_ajuda"),
					new String[] { "extratoContaCorrenteRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.FINANCEIRO_RELATORIOS_CAIXA),
	/**
	* Extrato Conta Corrente Rel
	*
	*/
	EXTRATO_CONTA_CORRENTE_ALTERAR_DATA_COMPENSACAO("ExtratoContaCorrente_permitirAlterarDataCompensacao",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ExtratoContaCorrente_permitirAlterarDataCompensacao_titulo"),
					UteisJSF.internacionalizar("per_ExtratoContaCorrente_permitirAlterarDataCompensacao_ajuda"),
					new String[] { "extratoContaCorrenteRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, EXTRATO_CONTA_CORRENTE_REL,
			PerfilAcessoSubModuloEnum.FINANCEIRO_RELATORIOS_CAIXA),
	
	EXTRATO_CONTA_CORRENTE_EXCLUIR("ExtratoContaCorrente_permitirExcluirExtratoContaCorrenteOrigemNaoLocalizada",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar(
							"per_ExtratoContaCorrente_permitirExcluirExtratoContaCorrenteOrigemNaoLocalizada_titulo"),
					UteisJSF.internacionalizar(
							"per_ExtratoContaCorrente_permitirExcluirExtratoContaCorrenteOrigemNaoLocalizada_ajuda"),
					new String[] { "extratoContaCorrenteRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, EXTRATO_CONTA_CORRENTE_REL,
			PerfilAcessoSubModuloEnum.FINANCEIRO_RELATORIOS_CAIXA),
	
	EXTRATO_CONTA_CORRENTE_INCLUIR("ExtratoContaCorrente_permitirIncluirExtratoContaCorrente",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ExtratoContaCorrente_permitirIncluirExtratoContaCorrente_titulo"),
					UteisJSF.internacionalizar("per_ExtratoContaCorrente_permitirIncluirExtratoContaCorrente_ajuda"),
					new String[] { "extratoContaCorrenteRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, EXTRATO_CONTA_CORRENTE_REL,
			PerfilAcessoSubModuloEnum.FINANCEIRO_RELATORIOS_CAIXA),
	/**
	* Declaracao Imposto Renda Rel
	*
	*/
	DECLARACAO_IMPOSTO_RENDA_REL("DeclaracaoImpostoRendaRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_DeclaracaoImpostoRendaRel_titulo"),
					UteisJSF.internacionalizar("per_DeclaracaoImpostoRendaRel_ajuda"),
					new String[] { "declaracaoImpostoRendaRel.xhtml" }),
					new PermissaoVisao(TipoVisaoEnum.ALUNO,
							UteisJSF.internacionalizar("per_DeclaracaoImpostoRendaRel_titulo"),
							UteisJSF.internacionalizar("per_DeclaracaoImpostoRendaRel_ajuda"),
							new String[] { "declaracaoImpostoRendaRel.xhtml" }),
					new PermissaoVisao(TipoVisaoEnum.PAIS,
							UteisJSF.internacionalizar("per_DeclaracaoImpostoRendaRel_titulo"),
							UteisJSF.internacionalizar("per_DeclaracaoImpostoRendaRel_ajuda"),
							new String[] { "declaracaoImpostoRendaRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null,
			PerfilAcessoSubModuloEnum.FINANCEIRO_RELATORIOS_CONTA_RECEBER),
	APRESENTAR_CONTA_BIBLIOTECA("ApresentarContaBiblioteca", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.ALUNO, UteisJSF.internacionalizar("per_ApresentarContaBiblioteca_titulo"),
					UteisJSF.internacionalizar("per_ApresentarContaBiblioteca_ajuda")),
			new PermissaoVisao(TipoVisaoEnum.PAIS, UteisJSF.internacionalizar("per_ApresentarContaBiblioteca_titulo"),
					UteisJSF.internacionalizar("per_ApresentarContaBiblioteca_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoFinanceiroEnum.DECLARACAO_IMPOSTO_RENDA_REL, 
			PerfilAcessoSubModuloEnum.FINANCEIRO_RELATORIOS_CONTA_RECEBER),
	APRESENTAR_CONTA_BOLSA_CUSTEADA_CONVENIO("ApresentarContaBolsaCusteadaConvenio",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ALUNO,
							UteisJSF.internacionalizar("per_ApresentarContaBolsaCusteadaConvenio_titulo"),
							UteisJSF.internacionalizar("per_ApresentarContaBolsaCusteadaConvenio_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.PAIS,
							UteisJSF.internacionalizar("per_ApresentarContaBolsaCusteadaConvenio_titulo"),
							UteisJSF.internacionalizar("per_ApresentarContaBolsaCusteadaConvenio_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoFinanceiroEnum.DECLARACAO_IMPOSTO_RENDA_REL, 
			PerfilAcessoSubModuloEnum.FINANCEIRO_RELATORIOS_CONTA_RECEBER),
	APRESENTAR_CONTA_CONTRATO_RECEITA("ApresentarContaContratoReceita",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ALUNO,
							UteisJSF.internacionalizar("per_ApresentarContaContratoReceita_titulo"),
							UteisJSF.internacionalizar("per_ApresentarContaContratoReceita_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.PAIS,
							UteisJSF.internacionalizar("per_ApresentarContaContratoReceita_titulo"),
							UteisJSF.internacionalizar("per_ApresentarContaContratoReceita_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoFinanceiroEnum.DECLARACAO_IMPOSTO_RENDA_REL, 
			PerfilAcessoSubModuloEnum.FINANCEIRO_RELATORIOS_CONTA_RECEBER),
	APRESENTAR_CONTA_DEVOLUCAO_CHEQUE("ApresentarContaDevolucaoCheque",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ALUNO,
							UteisJSF.internacionalizar("per_ApresentarContaDevolucaoCheque_titulo"),
							UteisJSF.internacionalizar("per_ApresentarContaDevolucaoCheque_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.PAIS,
							UteisJSF.internacionalizar("per_ApresentarContaDevolucaoCheque_titulo"),
							UteisJSF.internacionalizar("per_ApresentarContaDevolucaoCheque_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoFinanceiroEnum.DECLARACAO_IMPOSTO_RENDA_REL, 
			PerfilAcessoSubModuloEnum.FINANCEIRO_RELATORIOS_CONTA_RECEBER),
	APRESENTAR_CONTA_INCLUSAO_REPOSICAO("ApresentarContaInclusaoReposicao",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ALUNO,
							UteisJSF.internacionalizar("per_ApresentarContaInclusaoReposicao_titulo"),
							UteisJSF.internacionalizar("per_ApresentarContaInclusaoReposicao_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.PAIS,
							UteisJSF.internacionalizar("per_ApresentarContaInclusaoReposicao_titulo"),
							UteisJSF.internacionalizar("per_ApresentarContaInclusaoReposicao_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoFinanceiroEnum.DECLARACAO_IMPOSTO_RENDA_REL, 
			PerfilAcessoSubModuloEnum.FINANCEIRO_RELATORIOS_CONTA_RECEBER),
	APRESENTAR_CONTAS_INSCRICAO_PROCESSO_SELETIVO("ApresentarContasInscricaoProcessoSeletivo",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ALUNO,
							UteisJSF.internacionalizar("per_ApresentarContasInscricaoProcessoSeletivo_titulo"),
							UteisJSF.internacionalizar("per_ApresentarContasInscricaoProcessoSeletivo_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.PAIS,
							UteisJSF.internacionalizar("per_ApresentarContasInscricaoProcessoSeletivo_titulo"),
							UteisJSF.internacionalizar("per_ApresentarContasInscricaoProcessoSeletivo_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoFinanceiroEnum.DECLARACAO_IMPOSTO_RENDA_REL, 
			PerfilAcessoSubModuloEnum.FINANCEIRO_RELATORIOS_CONTA_RECEBER),
	APRESENTAR_CONTA_MATRICULA("ApresentarContaMatricula", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.ALUNO, UteisJSF.internacionalizar("per_ApresentarContaMatricula_titulo"),
					UteisJSF.internacionalizar("per_ApresentarContaMatricula_ajuda")),
			new PermissaoVisao(TipoVisaoEnum.PAIS, UteisJSF.internacionalizar("per_ApresentarContaMatricula_titulo"),
					UteisJSF.internacionalizar("per_ApresentarContaMatricula_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoFinanceiroEnum.DECLARACAO_IMPOSTO_RENDA_REL, 
			PerfilAcessoSubModuloEnum.FINANCEIRO_RELATORIOS_CONTA_RECEBER),
	APRESENTAR_CONTA_MENSALIDADE("ApresentarContaMensalidade", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.ALUNO, UteisJSF.internacionalizar("per_ApresentarContaMensalidade_titulo"),
					UteisJSF.internacionalizar("per_ApresentarContaMensalidade_ajuda")),
			new PermissaoVisao(TipoVisaoEnum.PAIS, UteisJSF.internacionalizar("per_ApresentarContaMensalidade_titulo"),
					UteisJSF.internacionalizar("per_ApresentarContaMensalidade_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoFinanceiroEnum.DECLARACAO_IMPOSTO_RENDA_REL, 
			PerfilAcessoSubModuloEnum.FINANCEIRO_RELATORIOS_CONTA_RECEBER),
	APRESENTAR_CONTA_NEGOCIACAO("ApresentarContaNegociacao", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.ALUNO, UteisJSF.internacionalizar("per_ApresentarContaNegociacao_titulo"),
					UteisJSF.internacionalizar("per_ApresentarContaNegociacao_ajuda")),
			new PermissaoVisao(TipoVisaoEnum.PAIS, UteisJSF.internacionalizar("per_ApresentarContaNegociacao_titulo"),
					UteisJSF.internacionalizar("per_ApresentarContaNegociacao_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoFinanceiroEnum.DECLARACAO_IMPOSTO_RENDA_REL, 
			PerfilAcessoSubModuloEnum.FINANCEIRO_RELATORIOS_CONTA_RECEBER),
	APRESENTAR_CONTA_OUTROS("ApresentarContaOutros", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.ALUNO, UteisJSF.internacionalizar("per_ApresentarContaOutros_titulo"),
					UteisJSF.internacionalizar("per_ApresentarContaOutros_ajuda")),
			new PermissaoVisao(TipoVisaoEnum.PAIS, UteisJSF.internacionalizar("per_ApresentarContaOutros_titulo"),
					UteisJSF.internacionalizar("per_ApresentarContaOutros_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoFinanceiroEnum.DECLARACAO_IMPOSTO_RENDA_REL, 
			PerfilAcessoSubModuloEnum.FINANCEIRO_RELATORIOS_CONTA_RECEBER),
	APRESENTAR_CONTA_REQUERIMENTO("ApresentarContaRequerimento",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ALUNO,
					UteisJSF.internacionalizar("per_ApresentarContaRequerimento_titulo"),
					UteisJSF.internacionalizar("per_ApresentarContaRequerimento_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.PAIS,
							UteisJSF.internacionalizar("per_ApresentarContaRequerimento_titulo"),
							UteisJSF.internacionalizar("per_ApresentarContaRequerimento_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoFinanceiroEnum.DECLARACAO_IMPOSTO_RENDA_REL, 
			PerfilAcessoSubModuloEnum.FINANCEIRO_RELATORIOS_CONTA_RECEBER),
	UTILIZAR_DATA_VENCIMENTO_DATA_RECEBIMENTO("UtilizarDataVencimentoDataRecebimento",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ALUNO,
							UteisJSF.internacionalizar("per_UtilizarDataVencimentoDataRecebimento_titulo"),
							UteisJSF.internacionalizar("per_UtilizarDataVencimentoDataRecebimento_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.PAIS,
							UteisJSF.internacionalizar("per_UtilizarDataVencimentoDataRecebimento_titulo"),
							UteisJSF.internacionalizar("per_UtilizarDataVencimentoDataRecebimento_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoFinanceiroEnum.DECLARACAO_IMPOSTO_RENDA_REL, 
			PerfilAcessoSubModuloEnum.FINANCEIRO_RELATORIOS_CONTA_RECEBER),
	PERMITIR_GERAR_DECLARACAO_ANO_ATUAL("PermitirGerarDeclaracaoAnoAtual",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ALUNO,
							UteisJSF.internacionalizar("per_PermitirGerarDeclaracaoAnoAtual_titulo"),
							UteisJSF.internacionalizar("per_PermitirGerarDeclaracaoAnoAtual_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.PAIS,
							UteisJSF.internacionalizar("per_PermitirGerarDeclaracaoAnoAtual_titulo"),
							UteisJSF.internacionalizar("per_PermitirGerarDeclaracaoAnoAtual_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoFinanceiroEnum.DECLARACAO_IMPOSTO_RENDA_REL, 
			PerfilAcessoSubModuloEnum.FINANCEIRO_RELATORIOS_CONTA_RECEBER),
	IMPRIMIR_ASSINATURADIGITAL("ImprimirAssinaturaDigital",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ImprimirAssinaturaDigital_titulo"),
					UteisJSF.internacionalizar("per_ImprimirAssinaturaDigital_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.ALUNO,
							UteisJSF.internacionalizar("per_ImprimirAssinaturaDigital_titulo"),
							UteisJSF.internacionalizar("per_ImprimirAssinaturaDigital_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.PAIS,
							UteisJSF.internacionalizar("per_ImprimirAssinaturaDigital_titulo"),
							UteisJSF.internacionalizar("per_ImprimirAssinaturaDigital_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoFinanceiroEnum.DECLARACAO_IMPOSTO_RENDA_REL, 
			PerfilAcessoSubModuloEnum.FINANCEIRO_RELATORIOS_CONTA_RECEBER),
	APRESENTAR_DATA_PREVISAO_RECEBIMENTO_VENCIMENTO_CONTA("ApresentarDataPrevisaoRecebimentoVencimentoConta",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
							UteisJSF.internacionalizar("per_ApresentarDataPrevisaoRecebimentoVencimentoConta_titulo"),
							UteisJSF.internacionalizar("per_ApresentarDataPrevisaoRecebimentoVencimentoConta_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.ALUNO,
							UteisJSF.internacionalizar("per_ApresentarDataPrevisaoRecebimentoVencimentoConta_titulo"),
							UteisJSF.internacionalizar(
									"per_ApresentarDataPrevisaoRecebimentoVencimentoConta_ajuda")), },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoFinanceiroEnum.DECLARACAO_IMPOSTO_RENDA_REL, 
			PerfilAcessoSubModuloEnum.FINANCEIRO_RELATORIOS_CONTA_RECEBER),
	CONSIDERAR_DATA_RECEBIMENTO_CONTA_DECLARACAO_IMPOSTO_RENDA("ConsiderarDataRecebimentoContaDeclaracaoImpostoRenda",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ConsiderarDataRecebimentoContaDeclaracaoImpostoRenda_titulo"),
					UteisJSF.internacionalizar("per_ConsiderarDataRecebimentoContaDeclaracaoImpostoRenda_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.ALUNO,
							UteisJSF.internacionalizar(
									"per_ConsiderarDataRecebimentoContaDeclaracaoImpostoRenda_titulo"),
							UteisJSF.internacionalizar(
									"per_ConsiderarDataRecebimentoContaDeclaracaoImpostoRenda_ajuda")),
					new PermissaoVisao(TipoVisaoEnum.PAIS,
							UteisJSF.internacionalizar(
									"per_ConsiderarDataRecebimentoContaDeclaracaoImpostoRenda_titulo"),
							UteisJSF.internacionalizar(
									"per_ConsiderarDataRecebimentoContaDeclaracaoImpostoRenda_ajuda")), },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoFinanceiroEnum.DECLARACAO_IMPOSTO_RENDA_REL, 
			PerfilAcessoSubModuloEnum.FINANCEIRO_RELATORIOS_CONTA_RECEBER),
	
	APRESENTAR_CONTA_MATERIAL_DIDATICO("ApresentarContaMaterialDidatico",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
							UteisJSF.internacionalizar("per_ApresentarContaMaterialDidatico_titulo"),
							UteisJSF.internacionalizar("per_ApresentarContaMaterialDidatico_titulo")),
					new PermissaoVisao(TipoVisaoEnum.ALUNO,
							UteisJSF.internacionalizar("per_ApresentarContaMaterialDidatico_titulo"),
							UteisJSF.internacionalizar("per_ApresentarContaMaterialDidatico_titulo")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoFinanceiroEnum.DECLARACAO_IMPOSTO_RENDA_REL, 
			PerfilAcessoSubModuloEnum.FINANCEIRO_RELATORIOS_CONTA_RECEBER),

	/**
	* Renegociacao Rel
	*
	*/
	RENEGOCIACAO_REL("RenegociacaoRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_RenegociacaoRel_titulo"),
					UteisJSF.internacionalizar("per_RenegociacaoRel_ajuda"),
					new String[] { "renegociacaoRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null,
			PerfilAcessoSubModuloEnum.FINANCEIRO_RELATORIOS_CONTA_RECEBER),
	/**
	* Entrega Boletos Rel
	*
	*/
	ENTREGA_BOLETOS_REL("EntregaBoletosRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_EntregaBoletosRel_titulo"),
					UteisJSF.internacionalizar("per_EntregaBoletosRel_ajuda"),
					new String[] { "entregaBoletosRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null,
			PerfilAcessoSubModuloEnum.FINANCEIRO_RELATORIOS_CONTA_RECEBER),
	/**
	* Fluxo Caixa Rel
	*
	*/
	FLUXO_CAIXA_REL("FluxoCaixaRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_FluxoCaixaRel_titulo"),
					UteisJSF.internacionalizar("per_FluxoCaixaRel_ajuda"), new String[] { "fluxoCaixaRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.FINANCEIRO_RELATORIOS_CAIXA),
	/**
	* Recebimento Por Turma Rel
	*
	*/
	RECEBIMENTO_POR_TURMA_REL("RecebimentoPorTurmaRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_RecebimentoPorTurmaRel_titulo"),
					UteisJSF.internacionalizar("per_RecebimentoPorTurmaRel_ajuda"),
					new String[] { "recebimentoPorTurmaRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null,
			PerfilAcessoSubModuloEnum.FINANCEIRO_RELATORIOS_CONTA_RECEBER),
	RECEBIMENTO_TURMA_EMITIR_APENAS_MATRICULA("RecebimentoTurmaEmitirApenasMatricula",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_RecebimentoTurmaEmitirApenasMatricula_titulo"),
					UteisJSF.internacionalizar("per_RecebimentoTurmaEmitirApenasMatricula_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoFinanceiroEnum.RECEBIMENTO_POR_TURMA_REL,
			PerfilAcessoSubModuloEnum.FINANCEIRO_RELATORIOS_CONTA_RECEBER),
	
	APRESENTAR_VALOR_RECEBIDO_COM_IMPOSTOS_RETIDOS("ApresentarValorRecebidoComImpostosRetidos",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ApresentarValorRecebidoComImpostosRetidos_titulo"),
					UteisJSF.internacionalizar("per_ApresentarValorRecebidoComImpostosRetidos_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoFinanceiroEnum.RECEBIMENTO_POR_TURMA_REL,
			PerfilAcessoSubModuloEnum.FINANCEIRO_RELATORIOS_CONTA_RECEBER),
	/**
	* Recebimento Rel
	*
	*/
	RECEBIMENTO_REL("RecebimentoRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_RecebimentoRel_titulo"),
					UteisJSF.internacionalizar("per_RecebimentoRel_ajuda"), new String[] { "recebimentoRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null,
			PerfilAcessoSubModuloEnum.FINANCEIRO_RELATORIOS_CONTA_RECEBER),
	PERMITIR_APENAS_RELATORIO_RECEBIMENTO_BIBLIOTECA("PermitirApenasRelatorioRecebimentoBiblioteca",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirApenasRelatorioRecebimentoBiblioteca_titulo"),
					UteisJSF.internacionalizar("per_PermitirApenasRelatorioRecebimentoBiblioteca_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoFinanceiroEnum.RECEBIMENTO_REL,
			PerfilAcessoSubModuloEnum.FINANCEIRO_RELATORIOS_CONTA_RECEBER),
	/**
	* Demonstrativo Resultado Financeiro
	*
	*/
	DEMONSTRATIVO_RESULTADO_FINANCEIRO("DemonstrativoResultadoFinanceiro",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_DemonstrativoResultadoFinanceiro_titulo"),
					UteisJSF.internacionalizar("per_DemonstrativoResultadoFinanceiro_ajuda"),
					new String[] { "demonstrativoResultadoFinanceiroRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null,
			PerfilAcessoSubModuloEnum.FINANCEIRO_RELATORIOS_CONTA_RECEBER),
	
	/**
	* Pagamento Rel
	*
	*/
	PAGAMENTO_REL("PagamentoRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PagamentoRel_titulo"),
					UteisJSF.internacionalizar("per_PagamentoRel_ajuda"), new String[] { "pagamentoRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.FINANCEIRO_RELATORIOS_CONTA_PAGAR),
	
	/**
	* Condicoes Pagamento Rel
	*
	*/
	CONDICOES_PAGAMENTO_REL("CondicoesPagamentoRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_CondicoesPagamentoRel_titulo"),
					UteisJSF.internacionalizar("per_CondicoesPagamentoRel_ajuda"),
					new String[] { "condicoesPagamentoRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null,
			PerfilAcessoSubModuloEnum.FINANCEIRO_RELATORIOS_CONTA_RECEBER),
	/**
	* Quadro Comissao Consultores Rel
	*
	*/
	QUADRO_COMISSAO_CONSULTORES_REL("QuadroComissaoConsultoresRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_QuadroComissaoConsultoresRel_titulo"),
					UteisJSF.internacionalizar("per_QuadroComissaoConsultoresRel_ajuda"),
					new String[] { "quadroComissaoConsultoresRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.FINANCEIRO_RELATORIOS_DESCONTOS),
	/**
	* Conta Pagar Por Categoria Despesa Rel
	*
	*/
	CONTA_PAGAR_POR_CATEGORIA_DESPESA_REL("ContaPagarPorCategoriaDespesaRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ContaPagarPorCategoriaDespesaRel_titulo"),
					UteisJSF.internacionalizar("per_ContaPagarPorCategoriaDespesaRel_ajuda"),
					new String[] { "contaPagarPorCategoriaDespesaRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.FINANCEIRO_RELATORIOS_CONTA_PAGAR),
	/**
	* Adiantamento Rel
	*
	*/
	ADIANTAMENTO_REL("AdiantamentoRel", new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
			UteisJSF.internacionalizar("per_AdiantamentoRel_titulo"),
			UteisJSF.internacionalizar("per_AdiantamentoRel_ajuda"), new String[] { "adiantamentoRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.FINANCEIRO_RELATORIOS_CONTA_PAGAR),
	/**
	* Conta Pagar Resumida Por Data Rel
	*
	*/
	CONTA_PAGAR_RESUMIDA_POR_DATA_REL("ContaPagarResumidaPorDataRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ContaPagarResumidaPorDataRel_titulo"),
					UteisJSF.internacionalizar("per_ContaPagarResumidaPorDataRel_ajuda"),
					new String[] { "contaPagarRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.FINANCEIRO_RELATORIOS_CONTA_PAGAR),
	/**
	* Recebimento Por Unidade Curso Turma Rel
	*
	*/
	RECEBIMENTO_POR_UNIDADE_CURSO_TURMA_REL("RecebimentoPorUnidadeCursoTurmaRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_RecebimentoPorUnidadeCursoTurmaRel_titulo"),
					UteisJSF.internacionalizar("per_RecebimentoPorUnidadeCursoTurmaRel_ajuda"),
					new String[] { "recebimentoPorUnidadeCursoTurmaRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null,
			PerfilAcessoSubModuloEnum.FINANCEIRO_RELATORIOS_CONTA_RECEBER),
	/**
	* Cheques Rel
	*
	*/
	CHEQUES_REL("ChequesRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ChequesRel_titulo"),
					UteisJSF.internacionalizar("per_ChequesRel_ajuda"), new String[] { "chequesRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.FINANCEIRO_RELATORIOS_CAIXA),
	/**
	* Inadimplencia Rel
	*
	*/
	INADIMPLENCIA_REL("InadimplenciaRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_InadimplenciaRel_titulo"),
					UteisJSF.internacionalizar("per_InadimplenciaRel_ajuda"),
					new String[] { "inadimplenciaRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null,
			PerfilAcessoSubModuloEnum.FINANCEIRO_RELATORIOS_CONTA_RECEBER),
	CARTAOCREDITODEBITO_REL("CartaoCreditoDebitoRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_CartaoCreditoDebitoRel_titulo"),
					UteisJSF.internacionalizar("per_CartaoCreditoDebitoRel_ajuda"),
					new String[] { "cartaoCreditoDebitoRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null,
			PerfilAcessoSubModuloEnum.FINANCEIRO_RELATORIOS_CONTA_RECEBER),
	INADIMPLENCIA_EMITIR_APENAS_MATRICULA("InadimplenciaEmitirApenasMatricula",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_InadimplenciaEmitirApenasMatricula_titulo"),
					UteisJSF.internacionalizar("per_InadimplenciaEmitirApenasMatricula_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoFinanceiroEnum.INADIMPLENCIA_REL,
			PerfilAcessoSubModuloEnum.FINANCEIRO_RELATORIOS_CONTA_RECEBER),
	/**
	* Conta Receber Rel
	*
	*/
	CONTA_RECEBER_REL("ContaReceberRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ContaReceberRel_titulo"),
					UteisJSF.internacionalizar("per_ContaReceberRel_ajuda"),
					new String[] { "contaReceberRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null,
			PerfilAcessoSubModuloEnum.FINANCEIRO_RELATORIOS_CONTA_RECEBER),
	
	PERMITIR_APENAS_RELATORIO_CONTA_RECEBER_BIBLIOTECA("PermitirApenasRelatorioContaReceberBiblioteca",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirApenasRelatorioContaReceberBiblioteca_titulo"),
					UteisJSF.internacionalizar("per_PermitirApenasRelatorioContaReceberBiblioteca_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoFinanceiroEnum.CONTA_RECEBER_REL,
			PerfilAcessoSubModuloEnum.FINANCEIRO_RELATORIOS_CONTA_RECEBER),

	/**
	* Relatorio SEI Decidir Receita
	*
	*/
	RELATORIO_SEIDECIDIR_RECEITA("RelatorioSEIDecidirReceita",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_RelatorioSEIDecidirReceita_titulo"),
					UteisJSF.internacionalizar("per_RelatorioSEIDecidirReceita_ajuda"),
					new String[] { "relatorioSEIDecidirRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null,
			PerfilAcessoSubModuloEnum.FINANCEIRO_RELATORIOS_CONTA_RECEBER),
	PERMITIR_GERAR_RELATORIO_SEIDECIDIR_RECEITA_APENAS_DADOS("PermitirGerarRelatorioSeiDecidirReceitaApenasDados",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirGerarRelatorioSeiDecidirReceitaApenasDados_titulo"),
					UteisJSF.internacionalizar("per_PermitirGerarRelatorioSeiDecidirReceitaApenasDados_ajuda"),
					new String[] { "relatorioSEIDecidirRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, RELATORIO_SEIDECIDIR_RECEITA,
			PerfilAcessoSubModuloEnum.FINANCEIRO_RELATORIOS_CONTA_RECEBER),
	/**
	* Media Desconto Aluno Rel
	*
	*/
	MEDIA_DESCONTO_ALUNO_REL("MediaDescontoAlunoRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MediaDescontoAlunoRel_titulo"),
					UteisJSF.internacionalizar("per_MediaDescontoAlunoRel_ajuda"),
					new String[] { "mediaDescontoAlunoRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.FINANCEIRO_RELATORIOS_DESCONTOS),
	/**
	* Previsao Faturamento Rel
	*
	*/
	PREVISAO_FATURAMENTO_REL("PrevisaoFaturamentoRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PrevisaoFaturamentoRel_titulo"),
					UteisJSF.internacionalizar("per_PrevisaoFaturamentoRel_ajuda"),
					new String[] { "previsaoFaturamentoRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null,
			PerfilAcessoSubModuloEnum.FINANCEIRO_RELATORIOS_CONTA_RECEBER),
	/**
	* Listagem Descontos Alunos Rel
	*
	*/
	LISTAGEM_DESCONTOS_ALUNOS_REL("ListagemDescontosAlunosRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ListagemDescontosAlunosRel_titulo"),
					UteisJSF.internacionalizar("per_ListagemDescontosAlunosRel_ajuda"),
					new String[] { "listagemDescontosAlunosRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.FINANCEIRO_RELATORIOS_DESCONTOS),
	/**
	* Conta Recebida Verso Conta Receber Rel
	*
	*/
	CONTA_RECEBIDA_VERSO_CONTA_RECEBER_REL("ContaRecebidaVersoContaReceberRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ContaRecebidaVersoContaReceberRel_titulo"),
					UteisJSF.internacionalizar("per_ContaRecebidaVersoContaReceberRel_ajuda"),
					new String[] { "contaRecebidaVersoContaReceberRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null,
			PerfilAcessoSubModuloEnum.FINANCEIRO_RELATORIOS_CONTA_RECEBER),
	/**
	* Mapa Aluno Rel
	*
	*/
	MAPA_ALUNO_REL("MapaAlunoRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MapaAlunoRel_titulo"),
					UteisJSF.internacionalizar("per_MapaAlunoRel_ajuda"), new String[] { "mapaAlunoRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null,
			PerfilAcessoSubModuloEnum.FINANCEIRO_RELATORIOS_CONTA_RECEBER),
	/**
	* Carta Cobranca Rel
	*
	*/
	CARTA_COBRANCA_REL("CartaCobrancaRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_CartaCobrancaRel_titulo"),
					UteisJSF.internacionalizar("per_CartaCobrancaRel_ajuda"),
					new String[] { "cartaCobrancaCons.xhtml", "cartaCobrancaRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null,
			PerfilAcessoSubModuloEnum.FINANCEIRO_RELATORIOS_CONTA_RECEBER),
	/**
	* Pagamento Resumido Rel
	*
	*/
//	PAGAMENTO_RESUMIDO_REL("PagamentoResumidoRel", new PermissaoVisao[] {
//			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_PagamentoResumidoRel_titulo"),UteisJSF.internacionalizar("per_PagamentoResumidoRel_ajuda"), new String[]{"pagamentoRel.xhtml"})
//			},
//			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
//			null, 
//			PerfilAcessoSubModuloEnum.FINANCEIRO_RELATORIOS_FINANCEIRO),
	/**
	* Termo Reconhecimento Divida Rel
	*
	*/
	TERMO_RECONHECIMENTO_DIVIDA_REL("TermoReconhecimentoDividaRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_TermoReconhecimentoDividaRel_titulo"),
					UteisJSF.internacionalizar("per_TermoReconhecimentoDividaRel_ajuda"),
					new String[] { "termoReconhecimentoDividaRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null,
			PerfilAcessoSubModuloEnum.FINANCEIRO_RELATORIOS_CONTA_RECEBER),
	/**
	* Impostos Retidos Conta Receber Rel
	*
	*/
	IMPOSTOS_RETIDOS_CONTA_RECEBER_REL("ImpostosRetidosContaReceberRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ImpostosRetidosContaReceberRel_titulo"),
					UteisJSF.internacionalizar("per_ImpostosRetidosContaReceberRel_ajuda"),
					new String[] { "impostosRetidosContaReceberRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null,
			PerfilAcessoSubModuloEnum.FINANCEIRO_RELATORIOS_CONTA_RECEBER),
	/**
	* Categoria Despesa Rel
	*
	*/
	CATEGORIA_DESPESA_REL("CategoriaDespesaRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_CategoriaDespesaRel_titulo"),
					UteisJSF.internacionalizar("per_CategoriaDespesaRel_ajuda"),
					new String[] { "categoriaDespesaRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.FINANCEIRO_RELATORIOS_CONTA_PAGAR),
	/**
	* Prestacao Conta Rel
	*
	*/
	PRESTACAO_CONTA_REL("PrestacaoContaRel", new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
			UteisJSF.internacionalizar("per_PrestacaoContaRel_titulo"),
			UteisJSF.internacionalizar("per_PrestacaoContaRel_ajuda"), new String[] { "prestacaoContaRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.FINANCEIRO_RELATORIOS_CONTA_PAGAR),
	/**
	* Situacao Financeira Aluno Rel
	*
	*/
	SITUACAO_FINANCEIRA_ALUNO_REL("SituacaoFinanceiraAlunoRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_SituacaoFinanceiraAlunoRel_titulo"),
					UteisJSF.internacionalizar("per_SituacaoFinanceiraAlunoRel_ajuda"),
					new String[] { "situacaoFinanceiraAlunoRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null,
			PerfilAcessoSubModuloEnum.FINANCEIRO_RELATORIOS_CONTA_RECEBER),
	/**
	* Tipo Desconto Rel
	*
	*/
	TIPO_DESCONTO_REL("TipoDescontoRel", new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
			UteisJSF.internacionalizar("per_TipoDescontoRel_titulo"),
			UteisJSF.internacionalizar("per_TipoDescontoRel_ajuda"), new String[] { "tipoDescontoRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.FINANCEIRO_RELATORIOS_DESCONTOS),
	/**
	* Boletos Rel
	*
	*/
	BOLETOS_REL("BoletosRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_BoletosRel_titulo"),
					UteisJSF.internacionalizar("per_BoletosRel_ajuda"), new String[] { "boletosRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null,
			PerfilAcessoSubModuloEnum.FINANCEIRO_RELATORIOS_CONTA_RECEBER),
	/**
	* Extrato Conta Pagar Rel
	*
	*/
	EXTRATO_CONTA_PAGAR_REL("ExtratoContaPagarRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ExtratoContaPagarRel_titulo"),
					UteisJSF.internacionalizar("per_ExtratoContaPagarRel_ajuda"),
					new String[] { "extratoContaPagarRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.FINANCEIRO_RELATORIOS_CONTA_PAGAR),
	/**
	* Operacao Financeira Caixa Rel
	*
	*/
	OPERACAO_FINANCEIRA_CAIXA_REL("OperacaoFinanceiraCaixaRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_OperacaoFinanceiraCaixaRel_titulo"),
					UteisJSF.internacionalizar("per_OperacaoFinanceiraCaixaRel_ajuda"),
					new String[] { "operacaoFinanceiraCaixaRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.FINANCEIRO_RELATORIOS_CAIXA),
	/**
	* Media Desconto Turma Rel
	*
	*/
	MEDIA_DESCONTO_TURMA_REL("MediaDescontoTurmaRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MediaDescontoTurmaRel_titulo"),
					UteisJSF.internacionalizar("per_MediaDescontoTurmaRel_ajuda"),
					new String[] { "mediaDescontoTurmaRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.FINANCEIRO_RELATORIOS_DESCONTOS),
	/**
	* Conta Pagar Por Turma Rel
	*
	*/
	CONTA_PAGAR_POR_TURMA_REL("ContaPagarPorTurmaRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ContaPagarPorTurmaRel_titulo"),
					UteisJSF.internacionalizar("per_ContaPagarPorTurmaRel_ajuda"),
					new String[] { "contaPagarPorTurmaRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.FINANCEIRO_RELATORIOS_CONTA_PAGAR),
	/**
	* Cheque
	*
	*/
	CHEQUE("Cheque",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_Cheque_titulo"), UteisJSF.internacionalizar("per_Cheque_ajuda"),
					new String[] { "chequeCons.xhtml", "chequeForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.FINANCEIRO_CAIXA),
	/**
	* Mapa Pendencia Movimentacao Financeira
	*
	*/
	MAPA_PENDENCIA_MOVIMENTACAO_FINANCEIRA("MapaPendenciaMovimentacaoFinanceira",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MapaPendenciaMovimentacaoFinanceira_titulo"),
					UteisJSF.internacionalizar("per_MapaPendenciaMovimentacaoFinanceira_ajuda"),
					new String[] { "mapaPendenciaMovimentacaoFinanceiraCons.xhtml",
							"mapaPendenciaMovimentacaoFinanceiraForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.FINANCEIRO_CAIXA),
	PERMITE_AUTORIZACAO_MOVIMENTACAO_FINANCEIRA_CONTA_CAIXA_FECHADA(
			"PermiteAutorizacaoMovimentacaoFinanceiraContaCaixaFechada",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermiteAutorizacaoMovimentacaoFinanceiraContaCaixaFechada_titulo"),
					UteisJSF.internacionalizar(
							"per_PermiteAutorizacaoMovimentacaoFinanceiraContaCaixaFechada_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoFinanceiroEnum.MAPA_PENDENCIA_MOVIMENTACAO_FINANCEIRA, 
			PerfilAcessoSubModuloEnum.FINANCEIRO_CAIXA),
	/**
	* Mapa Lancamento Futuro
	*
	*/
	MAPA_LANCAMENTO_FUTURO("MapaLancamentoFuturo",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MapaLancamentoFuturo_titulo"),
					UteisJSF.internacionalizar("per_MapaLancamentoFuturo_ajuda"),
					new String[] { "mapaLancamentoFuturoCons.xhtml", "mapaLancamentoFuturoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.FINANCEIRO_CAIXA),
	/**
	* Devolucao Cheque
	*
	*/
	DEVOLUCAO_CHEQUE("DevolucaoCheque",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_DevolucaoCheque_titulo"),
					UteisJSF.internacionalizar("per_DevolucaoCheque_ajuda"),
					new String[] { "devolucaoChequeCons.xhtml", "devolucaoChequeForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.FINANCEIRO_CAIXA),
	/**
	* Fluxo Caixa
	*
	*/
	FLUXO_CAIXA("FluxoCaixa", new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
			UteisJSF.internacionalizar("per_FluxoCaixa_titulo"), UteisJSF.internacionalizar("per_FluxoCaixa_ajuda"),
			new String[] { "fluxoCaixaCons.xhtml", "fluxoCaixaForm.xhtml" }) }, TipoPerfilAcessoPermissaoEnum.ENTIDADE,
			null, PerfilAcessoSubModuloEnum.FINANCEIRO_CAIXA),
	
	PIX_CONTA_CORRENTE("PixContaCorrente",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PixContaCorrente_titulo"),
					UteisJSF.internacionalizar("per_PixContaCorrente_ajuda"),
					new String[] { "pixContaCorrenteCons.xhtml", "pixContaCorrenteForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.FINANCEIRO_CAIXA),

	CONCILIACAO_CONTA_CORRENTE("ConciliacaoContaCorrente",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ConciliacaoContaCorrente_titulo"),
					UteisJSF.internacionalizar("per_ConciliacaoContaCorrente_ajuda"),
					new String[] { "conciliacaoContaCorrenteCons.xhtml", "conciliacaoContaCorrenteForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.FINANCEIRO_CAIXA),
	
	ABRIR_CONCILIACAO_CONTA_CORRENTE("AbrirConciliacaoContaCorrente",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_AbrirConciliacaoContaCorrente_titulo"),
					UteisJSF.internacionalizar("per_AbrirConciliacaoContaCorrente_ajuda")), },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoFinanceiroEnum.CONCILIACAO_CONTA_CORRENTE, PerfilAcessoSubModuloEnum.FINANCEIRO_CAIXA),

	PARAMETRIZAR_OPERACOES_AUTOMATICAS_CONCILIACAO("ParametrizarOperacoesAutomaticasConciliacaoItem",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ParametrizarOperacoesAutomaticasConciliacaoItem_titulo"),
					UteisJSF.internacionalizar("per_ParametrizarOperacoesAutomaticasConciliacaoItem_ajuda"),
					new String[] { "conciliacaoContaCorrenteCons.xhtml", "conciliacaoContaCorrenteForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.FINANCEIRO_CAIXA),

	/**
	* Movimentacao Financeira
	*
	*/
	MOVIMENTACAO_FINANCEIRA("MovimentacaoFinanceira",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MovimentacaoFinanceira_titulo"),
					UteisJSF.internacionalizar("per_MovimentacaoFinanceira_ajuda"),
					new String[] { "movimentacaoFinanceiraCons.xhtml", "movimentacaoFinanceiraForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.FINANCEIRO_CAIXA),

	MOVIMENTACAO_FINANCEIRA_CONTA_CAIXA_FECHADA("MovimentacaoFinanceiraContaCaixaFechada",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MovimentacaoFinanceiraContaCaixaFechada_titulo"),
					UteisJSF.internacionalizar("per_MovimentacaoFinanceiraContaCaixaFechada_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoFinanceiroEnum.MOVIMENTACAO_FINANCEIRA,
			PerfilAcessoSubModuloEnum.FINANCEIRO_CAIXA),
	
	ALTERAR_DATA_MOVIMENTACAO_FINANCEIRA("AlterarDataMovimentacaoFinanceira",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_AlterarDataMovimentacaoFinanceira_titulo"),
					UteisJSF.internacionalizar("per_AlterarDataMovimentacaoFinanceira_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoFinanceiroEnum.MOVIMENTACAO_FINANCEIRA,
			PerfilAcessoSubModuloEnum.FINANCEIRO_CAIXA),

	MOVIMENTACAO_CONTA_CAIXA_CONTA_CORRENTE("MovimentacaoContaCaixaContaCorrente",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MovimentacaoContaCaixaContaCorrente_titulo"),
					UteisJSF.internacionalizar("per_MovimentacaoContaCaixaContaCorrente_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoFinanceiroEnum.MOVIMENTACAO_FINANCEIRA,
			PerfilAcessoSubModuloEnum.FINANCEIRO_CAIXA),
	ESTORNAR_MOVIMENTACAO_FINANCEIRA("EstornarMovimentacaoFinanceira",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_EstornarMovimentacaoFinanceira_titulo"),
					UteisJSF.internacionalizar("per_EstornarMovimentacaoFinanceira_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoFinanceiroEnum.MOVIMENTACAO_FINANCEIRA,
			PerfilAcessoSubModuloEnum.FINANCEIRO_CAIXA),
	
	PERMITIR_ALTERAR_LANCAMENTO_CONTABIL_MOVIMENTACAO_FINANCEIRA(
			"PermitirAlterarLancamentoContabilMovimentacaoFinanceira",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirAlterarLancamentoContabilMovimentacaoFinanceira_titulo"),
					UteisJSF.internacionalizar("per_PermitirAlterarLancamentoContabilMovimentacaoFinanceira_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoFinanceiroEnum.MOVIMENTACAO_FINANCEIRA,
			PerfilAcessoSubModuloEnum.FINANCEIRO_CAIXA),
	
	PERMITIR_LANCAMENTO_CONTABIL_MOVIMENTACAO_FINANCEIRA_SOMENTE_DESTINO(
			"PermitirLancamentoContabilMovimentacaoFinanceiraSomenteDestino",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar(
							"per_PermitirLancamentoContabilMovimentacaoFinanceiraSomenteDestino_titulo"),
					UteisJSF.internacionalizar(
							"per_PermitirLancamentoContabilMovimentacaoFinanceiraSomenteDestino_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoFinanceiroEnum.MOVIMENTACAO_FINANCEIRA,
			PerfilAcessoSubModuloEnum.FINANCEIRO_CAIXA),
	
	PERMITIR_REALIZAR_MOVIMENTACAO_FINANCEIRO_CONTA_CAIXA_RESPONSAVEL(
			"PermitirRealizarMovimentacaoFinanceiroContaCaixaResponsavel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar(
							"per_PermitirRealizarMovimentacaoFinanceiroContaCaixaResponsavel_titulo"),
					UteisJSF.internacionalizar(
							"per_PermitirRealizarMovimentacaoFinanceiroContaCaixaResponsavel_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoFinanceiroEnum.MOVIMENTACAO_FINANCEIRA,
			PerfilAcessoSubModuloEnum.FINANCEIRO_CAIXA),
	
	PERMITIR_REALIZAR_MOVIMENTACAO_FINANCEIRO_DESCONSIDERANDO_CONTABILIDADE_CONCILIACAO(
			"PermitirRealizarMovimentacaoFinanceiroDesconsiderandoContabilidadeConciliacao",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar(
							"per_PermitirRealizarMovimentacaoFinanceiroDesconsiderandoContabilidadeConciliacao_titulo"),
					UteisJSF.internacionalizar(
							"per_PermitirRealizarMovimentacaoFinanceiroDesconsiderandoContabilidadeConciliacao_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoFinanceiroEnum.MOVIMENTACAO_FINANCEIRA,
			PerfilAcessoSubModuloEnum.FINANCEIRO_CAIXA),
	/**
	* Mapa Pendencia Cartao Credito
	*
	*/
	MAPA_PENDENCIA_CARTAO_CREDITO("MapaPendenciaCartaoCredito",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MapaPendenciaCartaoCredito_titulo"),
					UteisJSF.internacionalizar("per_MapaPendenciaCartaoCredito_ajuda"),
					new String[] { "mapaPendenciaCartaoCreditoCons.xhtml", "mapaPendenciaCartaoCreditoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.FINANCEIRO_CAIXA),
	PERMITIR_CANCELAR_DCC_PREVISTO("PermitirCancelarDCCPrevisto",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirCancelarDCCPrevisto_titulo"),
					UteisJSF.internacionalizar("per_PermitirCancelarDCCPrevisto_titulo")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoFinanceiroEnum.MAPA_PENDENCIA_CARTAO_CREDITO, 
			PerfilAcessoSubModuloEnum.FINANCEIRO_CAIXA),
	PERMITIR_ALTERAR_DATA_VENCIMENTO("MapaPendenciaCartaoCredito_permitirAlterarDataVencimento",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MapaPendenciaCartaoCredito_permitirAlterarDataVencimento_titulo"),
					UteisJSF.internacionalizar("per_MapaPendenciaCartaoCredito_permitirAlterarDataVencimento_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoFinanceiroEnum.MAPA_PENDENCIA_CARTAO_CREDITO, 
			PerfilAcessoSubModuloEnum.FINANCEIRO_CAIXA),
	PERMITIR_ALTERAR_TAXA("MapaPendenciaCartaoCredito_permitirAlterarTaxa",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MapaPendenciaCartaoCredito_permitirAlterarTaxa_titulo"),
					UteisJSF.internacionalizar("per_MapaPendenciaCartaoCredito_permitirAlterarTaxa_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoFinanceiroEnum.MAPA_PENDENCIA_CARTAO_CREDITO, 
			PerfilAcessoSubModuloEnum.FINANCEIRO_CAIXA),
	
	PERMITIR_REALIZAR_ALTERACOES_NEGOCIACAO_RECEBIMENTO("PermitirRealizarAlteracoesNegociacaoRecebimento",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirRealizarAlteracoesNegociacaoRecebimento_titulo"),
					UteisJSF.internacionalizar("per_PermitirRealizarAlteracoesNegociacaoRecebimento_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoFinanceiroEnum.MAPA_PENDENCIA_CARTAO_CREDITO, 
			PerfilAcessoSubModuloEnum.FINANCEIRO_CAIXA),
	PERMITIR_EXCLUIR_CARTAO_RECORRENCIA("PermitirExcluirCartaoRecorrencia",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirExcluirCartaoRecorrencia_titulo"),
					UteisJSF.internacionalizar("per_PermitirExcluirCartaoRecorrencia_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoFinanceiroEnum.MAPA_PENDENCIA_CARTAO_CREDITO, 
			PerfilAcessoSubModuloEnum.FINANCEIRO_CAIXA),
	PERMITIR_EXECUTAR_JOB_CARTAO_RECORRENCIA("PermitirExecutarJobCartaoRecorrencia",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirExecutarJobCartaoRecorrencia_titulo"),
					UteisJSF.internacionalizar("per_PermitirExecutarJobCartaoRecorrencia_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoFinanceiroEnum.MAPA_PENDENCIA_CARTAO_CREDITO, 
			PerfilAcessoSubModuloEnum.FINANCEIRO_CAIXA),
	
	/**
	* Liberacao Financeiro Cancelamento Trancamento
	*
	*/
	LIBERACAO_FINANCEIRO_CANCELAMENTO_TRANCAMENTO("LiberacaoFinanceiroCancelamentoTrancamento",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_LiberacaoFinanceiroCancelamentoTrancamento_titulo"),
					UteisJSF.internacionalizar("per_LiberacaoFinanceiroCancelamentoTrancamento_ajuda"),
					new String[] { "liberacaoFinanceiroCancelamentoTrancamentoCons.xhtml",
							"liberacaoFinanceiroCancelamentoTrancamentoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.FINANCEIRO_LIBERACAO_FINANCEIRO),
	LIBERACAO_FINANCEIRO_CANCELAMENTO_PERMITI_REMOVER_CONTA_RECEBER(
			"LiberacaoFinanceiroCancelamento_PermitiRemoverContaReceber",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_LiberacaoFinanceiroCancelamentoPermitiRemoverContaReceber_titulo"),
					UteisJSF.internacionalizar(
							"per_LiberacaoFinanceiroCancelamentoPermitiRemoverContaReceber_ajuda")), },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoFinanceiroEnum.LIBERACAO_FINANCEIRO_CANCELAMENTO_TRANCAMENTO, 
			PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_RECEBER),
	LIBERACAO_FINANCEIRO_CANCELAMENTO_PERMITI_CANCELAR_CONTA_RECEBER_VENCIDA(
			"LiberacaoFinanceiroCancelamento_PermitiCancelarContaReceberVencida",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar(
							"per_LiberacaoFinanceiroCancelamentoPermitiCancelarContaReceberVencida_titulo"),
					UteisJSF.internacionalizar(
							"per_LiberacaoFinanceiroCancelamentoPermitiCancelarContaReceberVencida_ajuda")), },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoFinanceiroEnum.LIBERACAO_FINANCEIRO_CANCELAMENTO_TRANCAMENTO, 
			PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_RECEBER),
	LIBERACAO_FINANCEIRO_CANCELAMENTO_PERMITI_REMOVER_CONTA_RECEBER_VENCIDA(
			"LiberacaoFinanceiroCancelamento_PermitiRemoverContaReceberVencida",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar(
							"per_LiberacaoFinanceiroCancelamentoPermitiRemoverContaReceberVencida_titulo"),
					UteisJSF.internacionalizar(
							"per_LiberacaoFinanceiroCancelamentoPermitiRemoverContaReceberVencida_ajuda")), },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoFinanceiroEnum.LIBERACAO_FINANCEIRO_CANCELAMENTO_TRANCAMENTO, 
			PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_RECEBER),
	/**
	* Perfil Economico
	*
	*/
	PERFIL_ECONOMICO("PerfilEconomico",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PerfilEconomico_titulo"),
					UteisJSF.internacionalizar("per_PerfilEconomico_ajuda"),
					new String[] { "perfilEconomicoCons.xhtml", "perfilEconomicoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.FINANCEIRO_PERFIL_ECONOMICO),
	/**
	* Texto Padrao
	*
	*/
	TEXTO_PADRAO("TextoPadrao", new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
			UteisJSF.internacionalizar("per_TextoPadrao_titulo"), UteisJSF.internacionalizar("per_TextoPadrao_ajuda"),
			new String[] { "textoPadraoCons.xhtml", "textoPadraoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.FINANCEIRO_FINANCEIRO_ACADEMICO),
	
	CENTRO_RESULTADO("CentroResultado",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_CentroResultado_titulo"),
					UteisJSF.internacionalizar("per_CentroResultado_ajuda"),
					new String[] { "centroResultadoCons.xhtml", "centroResultadoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.FINANCEIRO_FINANCEIRO_ACADEMICO),
	
	MAPA_FINANCEIRO_ALUNO_REL("MapaFinanceiroAlunoRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MapaFinanceiroAlunoRel_titulo"),
					UteisJSF.internacionalizar("per_MapaFinanceiroAlunoRel_ajuda"),
					new String[] { "mapaFinanceiroAlunoRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null,
			PerfilAcessoSubModuloEnum.FINANCEIRO_RELATORIOS_CONTA_RECEBER),
	/**
	* Negativação/Cobrança Conta Receber
	*
	*/
	AGENTE_NEGATIVACAO_COBRANCA_CONTA_RECEBER("AgenteNegativacaoCobrancaContaReceber",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_AgenteNegativacaoCobrancaContaReceber_titulo"),
					UteisJSF.internacionalizar("per_AgenteNegativacaoCobrancaContaReceber_ajuda"),
					new String[] { "agenteNegativacaoCobrancaContaReceberCons.xhtml",
							"agenteNegativacaoCobrancaContaReceberForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null,
			PerfilAcessoSubModuloEnum.FINANCEIRO_NEGATIVACAO_COBRANCA_CONTA_RECEBER),
	
	REGISTRO_NEGATIVACAO_COBRANCA_CONTA_RECEBER("RegistroNegativacaoCobrancaContaReceber",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_RegistroNegativacaoCobrancaContaReceber_titulo"),
					UteisJSF.internacionalizar("per_RegistroNegativacaoCobrancaContaReceber_ajuda"),
					new String[] { "registroNegativacaoCobrancaContaReceberCons.xhtml",
							"registroNegativacaoCobrancaContaReceberForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null,
			PerfilAcessoSubModuloEnum.FINANCEIRO_NEGATIVACAO_COBRANCA_CONTA_RECEBER),
	
	REGISTRO_NEGATIVACAO_COBRANCA_CONTA_RECEBER_VIA_INTEGRACAO("RegistroNegativacaoCobrancaContaReceberViaIntegracao",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_RegistroNegativacaoCobrancaContaReceberViaIntegracao_titulo"),
					UteisJSF.internacionalizar("per_RegistroNegativacaoCobrancaContaReceberViaIntegracao_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoFinanceiroEnum.REGISTRO_NEGATIVACAO_COBRANCA_CONTA_RECEBER, 
			PerfilAcessoSubModuloEnum.FINANCEIRO_NEGATIVACAO_COBRANCA_CONTA_RECEBER),
	
	MAPA_NEGATIVACAO_COBRANCA_CONTA_RECEBER("MapaNegativacaoCobrancaContaReceber",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MapaNegativacaoCobrancaContaReceber_titulo"),
					UteisJSF.internacionalizar("per_MapaNegativacaoCobrancaContaReceber_ajuda"),
					new String[] { "mapaNegativacaoCobrancaContaReceberCons.xhtml",
							"mapaNegativacaoCobrancaContaReceberForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null,
			PerfilAcessoSubModuloEnum.FINANCEIRO_NEGATIVACAO_COBRANCA_CONTA_RECEBER),
	
	MAPA_NEGATIVACAO_COBRANCA_CONTA_RECEBER_VIA_INTEGRACAO("MapaNegativacaoCobrancaContaReceberViaIntegracao",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MapaNegativacaoCobrancaContaReceberViaIntegracao_titulo"),
					UteisJSF.internacionalizar("per_MapaNegativacaoCobrancaContaReceberViaIntegracao_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoFinanceiroEnum.MAPA_NEGATIVACAO_COBRANCA_CONTA_RECEBER, 
			PerfilAcessoSubModuloEnum.FINANCEIRO_NEGATIVACAO_COBRANCA_CONTA_RECEBER),	
	
	LIBERAR_CONTARECEBER_NEGOCIADA_MAPANEGATIVACAOCOBRANCA("LiberarContaReceberNegociadaMapaNegativacaoCobranca",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_LiberarContaReceberNegociadaMapaNegativacaoCobranca_titulo"),
					UteisJSF.internacionalizar("per_LiberarContaReceberNegociadaMapaNegativacaoCobranca_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoFinanceiroEnum.MAPA_NEGATIVACAO_COBRANCA_CONTA_RECEBER, 
			PerfilAcessoSubModuloEnum.FINANCEIRO_NEGATIVACAO_COBRANCA_CONTA_RECEBER),	
//	EXCLUSAO_REGISTRO_NEGATIVACAO_COBRANCA("ExclusaoRegistroNegativacaoCobranca", new PermissaoVisao[] {
//			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_ExclusaoRegistroNegativacaoCobranca_titulo"),UteisJSF.internacionalizar("per_ExclusaoRegistroNegativacaoCobranca_ajuda"))
//			},
//			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
//			PerfilAcessoPermissaoFinanceiroEnum.REGISTRO_NEGATIVACAO_COBRANCA_CONTA_RECEBER, 
//			PerfilAcessoSubModuloEnum.FINANCEIRO_NEGATIVACAO_COBRANCA_CONTA_RECEBER),
	RECEBIMENTO_REGISTRO_NEGATIVACAO_COBRANCA("RecebimentoRegistroNegativacaoCobranca",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_RecebimentoRegistroNegativacaoCobranca_titulo"),
					UteisJSF.internacionalizar("per_RecebimentoRegistroNegativacaoCobranca_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoFinanceiroEnum.REGISTRO_NEGATIVACAO_COBRANCA_CONTA_RECEBER, 
			PerfilAcessoSubModuloEnum.FINANCEIRO_NEGATIVACAO_COBRANCA_CONTA_RECEBER),
	RENEGOCIACAO_REGISTRO_NEGATIVACAO_COBRANCA("RenegociacaoRegistroNegativacaoCobranca",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_RenegociacaoRegistroNegativacaoCobranca_titulo"),
					UteisJSF.internacionalizar("per_RenegociacaoRegistroNegativacaoCobranca_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoFinanceiroEnum.REGISTRO_NEGATIVACAO_COBRANCA_CONTA_RECEBER, 
			PerfilAcessoSubModuloEnum.FINANCEIRO_NEGATIVACAO_COBRANCA_CONTA_RECEBER),
	
	NEGOCIACAO_CONTA_PAGAR("NegociacaoContaPagar",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_NegociacaoContaPagar_titulo"),
					UteisJSF.internacionalizar("per_NegociacaoContaPagar_ajuda"),
					new String[] { "negociacaoContaPagarCons.xhtml", "negociacaoContaPagarForm.xhtml" }), },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_PAGAR),
	
	DESCONTO_NEGOCIACAO_CONTA_PAGAR("DescontoNegociacaoContaPagar",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_DescontoNegociacaoContaPagar_titulo"),
					UteisJSF.internacionalizar("per_DescontoNegociacaoContaPagar_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoFinanceiroEnum.NEGOCIACAO_CONTA_PAGAR,
			PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_PAGAR),
	
	PERMITE_ALTERAR_CENTRO_RESULTADO_CONTRATO_DESPESA("PermiteAlterarCentroResultadoContratoDespesa",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermiteAlterarCentroResultado_titulo"),
					UteisJSF.internacionalizar("per_PermiteAlterarCentroResultado_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoFinanceiroEnum.CONTRATOS_DESPESAS,
			PerfilAcessoSubModuloEnum.FINANCEIRO_CONTA_PAGAR);
	
	/**
	 * @param descricao
	 * @param ajuda
	 * @param paginaAcesso
	 * @param tipoPerfilAcesso
	 * @paran permissaoSuperiorEnum - popular quando no tipoPerfilAcesso for
	 *        funcionalidade
	 */

	private PerfilAcessoPermissaoFinanceiroEnum(String valor, PermissaoVisao[] permissaoVisao, 
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
		for(PermissaoVisao permissaoVisao: getPermissaoVisao()){
			if(permissaoVisao.equals(tipoVisaoEnum)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @return the utilizarVisaoProfessor
	 */
	public String getDescricaoVisao(TipoVisaoEnum tipoVisaoEnum) {
		if(getUtilizaVisao(tipoVisaoEnum)){
			return getPermissaoVisao(tipoVisaoEnum).getDescricao();
		}
		return "";
	}
	
	/**
	 * @return the utilizarVisaoProfessor
	 */
	public String getAjudaVisao(TipoVisaoEnum tipoVisaoEnum) {
		if(getUtilizaVisao(tipoVisaoEnum)){
			return getPermissaoVisao(tipoVisaoEnum).getAjuda();
		}
		return "";
	}
	
	/**
	 * @return the utilizarVisaoProfessor
	 */
	public List<String> getPaginaAcessoVisao(TipoVisaoEnum tipoVisaoEnum) {
		if(getUtilizaVisao(tipoVisaoEnum)){
			return Arrays.asList(getPermissaoVisao(tipoVisaoEnum).getPaginaAcesso());
		}
		return null;
	}
	
	/**
	 * @return the utilizarVisaoProfessor
	 */
	public PermissaoVisao getPermissaoVisao(TipoVisaoEnum tipoVisaoEnum) {		
		if(getUtilizaVisao(tipoVisaoEnum)){
			for(PermissaoVisao permissaoVisao2: getPermissaoVisao()){
				if(permissaoVisao2.equals(tipoVisaoEnum)){
					return permissaoVisao2;
				}
			}			 
		}
		return null;
	}
	
	public Boolean getIsApresentarApenasPermissaoTotal(){
		return getTipoPerfilAcesso().equals(TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE)
				&& getTipoPerfilAcesso().equals(TipoPerfilAcessoPermissaoEnum.RELATORIO);
	}

	public String descricaoModulo;

	@Override
	public String getDescricaoModulo() {
		if(descricaoModulo == null) {
		if(Uteis.isAtributoPreenchido(this.getPerfilAcessoSubModulo())) {			
				descricaoModulo += getPerfilAcessoSubModulo().getPerfilAcessoModuloEnum().getValorApresentar();
		}else {
			descricaoModulo ="";
		}
		}
		return descricaoModulo;
	}

	public String descricaoSubModulo;

	@Override
	public String getDescricaoSubModulo() {
		if(descricaoSubModulo == null) {
			if(Uteis.isAtributoPreenchido(this.getPerfilAcessoSubModulo())) {			
				descricaoSubModulo = getPerfilAcessoSubModulo().getDescricao();

			}else {
				descricaoSubModulo ="";
			}
			}
			return descricaoSubModulo;
	}	
	
	public String descricaoModuloSubModulo;

	@Override
	public String getDescricaoModuloSubModulo() {
		if(descricaoModuloSubModulo == null) {
		if(Uteis.isAtributoPreenchido(this.getPerfilAcessoSubModulo())) {			
				descricaoModuloSubModulo = getPerfilAcessoSubModulo().getPerfilAcessoModuloEnum().getValorApresentar()
						+ " - " + getPerfilAcessoSubModulo().getDescricao();
		}else {
			descricaoModuloSubModulo ="";
		}
		}
		return descricaoModuloSubModulo;
	}			 
	
}
