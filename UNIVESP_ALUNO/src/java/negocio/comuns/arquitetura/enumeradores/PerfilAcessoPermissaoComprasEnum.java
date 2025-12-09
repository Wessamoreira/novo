package negocio.comuns.arquitetura.enumeradores;

import java.util.Arrays;
import java.util.List;

import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

/**
 * @author Rodrigo Wind
 *
 */
public enum PerfilAcessoPermissaoComprasEnum implements PerfilAcessoPermissaoEnumInterface {
	
	/**
	* Cotacao
	*
	*/	
	COTACAO("Cotacao", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_Cotacao_titulo"),UteisJSF.internacionalizar("per_Cotacao_ajuda"), new String[]{"cotacaoCons.xhtml","cotacaoForm.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.COMPRAS_COTACAO_COMPRA),
	
	
	PERMITE_ALTERAR_CATEGORIA_DESPESA_COTACAO("PermiteAlterarCategoriaDespesaCotacao", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_PermiteAlterarCategoriaDespesa_titulo"),UteisJSF.internacionalizar("per_PermiteAlterarCategoriaDespesa_ajuda"))
			},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoComprasEnum.COTACAO, 
			PerfilAcessoSubModuloEnum.COMPRAS_REQUISICOES),
	
	PERMITE_ALTERAR_CENTRO_RESULTADO_COTACAO("PermiteAlterarCentroResultadoCotacao", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_PermiteAlterarCentroResultado_titulo"),UteisJSF.internacionalizar("per_PermiteAlterarCentroResultado_ajuda"))
			},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoComprasEnum.COTACAO, 
			PerfilAcessoSubModuloEnum.COMPRAS_REQUISICOES),
	
	PERMITE_CONSULTAR_COTACAO_OUTROS_RESPONSAVEIS_TODAS_UNIDADE_ENSINO("PermiteConsultarCotacaoOutrosResponsaveisTodasUnidadeEnsino", 
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_PermiteConsultarCotacaoOutrosResponsaveisTodasUnidadeEnsino_titulo"), UteisJSF.internacionalizar("per_PermiteConsultarCotacaoOutrosResponsaveisTodasUnidadeEnsino_ajuda")) }, 
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoComprasEnum.COTACAO, 
			PerfilAcessoSubModuloEnum.COMPRAS_COTACAO_COMPRA),
	
	PERMITE_CONSULTAR_COTACAO_OUTROS_RESPONSAVEIS_MESMA_UNIDADE_ENSINO("PermiteConsultarCotacaosOutrosResponsaveisMesmaUnidadeEnsino", 
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_PermiteConsultarCotacaoOutrosResponsaveisMesmaUnidadeEnsino_titulo"), UteisJSF.internacionalizar("per_PermiteConsultarCotacaoOutrosResponsaveisMesmaUnidadeEnsino_ajuda")) }, 
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoComprasEnum.COTACAO, 
			PerfilAcessoSubModuloEnum.COMPRAS_COTACAO_COMPRA),
	
	PERMITE_CONSULTAR_COTACAO_OUTROS_RESPONSAVEIS_MESMO_DEPARTAMENTO("PermiteConsultarCotacaoOutrosResponsaveisMesmoDepartamento", 
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_PermiteConsultarCotacaoOutrosResponsaveisMesmoDepartamento_titulo"), UteisJSF.internacionalizar("per_PermiteConsultarCotacaoOutrosResponsaveisMesmoDepartamento_ajuda")) }, 
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoComprasEnum.COTACAO, 
			PerfilAcessoSubModuloEnum.COMPRAS_COTACAO_COMPRA),
	/**
	* Recebimento Compra
	*
	*/	
	RECEBIMENTO_COMPRA("RecebimentoCompra", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_RecebimentoCompra_titulo"),UteisJSF.internacionalizar("per_RecebimentoCompra_ajuda"), new String[]{"recebimentoCompraCons.xhtml","recebimentoCompraForm.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.COMPRAS_COTACAO_COMPRA),
	/**
	* Devolucao Compra
	*
	*/	
	DEVOLUCAO_COMPRA("DevolucaoCompra", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_DevolucaoCompra_titulo"),UteisJSF.internacionalizar("per_DevolucaoCompra_ajuda"), new String[]{"devolucaoCompraCons.xhtml","devolucaoCompraForm.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.COMPRAS_COTACAO_COMPRA),
	/**
	* Mapa Cotacao
	*
	*/	
	MAPA_COTACAO("MapaCotacao", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_MapaCotacao_titulo"),UteisJSF.internacionalizar("per_MapaCotacao_ajuda"), new String[]{"mapaCotacaoCons.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.COMPRAS_COTACAO_COMPRA),
	
	AUTORIZAR_COTACAO("AutorizarCotacao", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_AutorizarCotacao_titulo"),UteisJSF.internacionalizar("per_AutorizarCotacao_ajuda"))
			},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoComprasEnum.MAPA_COTACAO, 
			PerfilAcessoSubModuloEnum.COMPRAS_COTACAO_COMPRA),
	
	
	PERMITE_CONSULTAR_MAPA_COTACOES_OUTROS_RESPONSAVEIS_TODAS_UNIDADE_ENSINO("PermiteConsultarMapaCotacoesOutrosResponsaveisTodasUnidadeEnsino", 
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_PermiteConsultarMapaCotacoesOutrosResponsaveisTodasUnidadeEnsino_titulo"), UteisJSF.internacionalizar("per_PermiteConsultarMapaCotacoesOutrosResponsaveisTodasUnidadeEnsino_ajuda")) }, 
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoComprasEnum.MAPA_COTACAO, 
			PerfilAcessoSubModuloEnum.COMPRAS_COTACAO_COMPRA),
	
	PERMITE_CONSULTAR_MAPA_COTACOES_OUTROS_RESPONSAVEIS_MESMA_UNIDADE_ENSINO("PermiteConsultarMapaCotacoesOutrosResponsaveisMesmaUnidadeEnsino", 
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_PermiteConsultarMapaCotacoesOutrosResponsaveisMesmaUnidadeEnsino_titulo"), UteisJSF.internacionalizar("per_PermiteConsultarMapaCotacoesOutrosResponsaveisMesmaUnidadeEnsino_ajuda")) }, 
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoComprasEnum.MAPA_COTACAO, 
			PerfilAcessoSubModuloEnum.COMPRAS_COTACAO_COMPRA),
	
	PERMITE_CONSULTAR_MAPA_COTACOES_OUTROS_RESPONSAVEIS_MESMO_DEPARTAMENTO("PermiteConsultarMapaCotacoesOutrosResponsaveisMesmoDepartamento", 
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_PermiteConsultarMapaCotacoesOutrosResponsaveisMesmoDepartamento_titulo"), UteisJSF.internacionalizar("per_PermiteConsultarMapaCotacoesOutrosResponsaveisMesmoDepartamento_ajuda")) }, 
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoComprasEnum.MAPA_COTACAO, 
			PerfilAcessoSubModuloEnum.COMPRAS_COTACAO_COMPRA),
	
	PERMITE_ALTERAR_RESPONSAVEL("PermiteAlterarResponsavel", 
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_PermiteAlterarResponsavel_titulo"), UteisJSF.internacionalizar("per_PermiteAlterarResponsavel_ajuda")) }, 
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoComprasEnum.MAPA_COTACAO, 
			PerfilAcessoSubModuloEnum.COMPRAS_COTACAO_COMPRA),

	PERMITE_RETORNAR_TRAMITE("PermiteRetornarTramite", 
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_PermiteRetornarTramite_titulo"), UteisJSF.internacionalizar("per_PermiteRetornarTramite_ajuda")) }, 
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoComprasEnum.MAPA_COTACAO, 
			PerfilAcessoSubModuloEnum.COMPRAS_COTACAO_COMPRA),

	/**
	* Compra
	*
	*/	
	COMPRA("Compra", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_Compra_titulo"),UteisJSF.internacionalizar("per_Compra_ajuda"), new String[]{"compraCons.xhtml","compraForm.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.COMPRAS_COTACAO_COMPRA),
	
	PERMITE_ALTERAR_CENTRO_RESULTADO_COMPRA("PermiteAlterarCentroResultadoCompra", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_PermiteAlterarCentroResultado_titulo"),UteisJSF.internacionalizar("per_PermiteAlterarCentroResultado_ajuda"))
			},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoComprasEnum.COMPRA, 
			PerfilAcessoSubModuloEnum.COMPRAS_REQUISICOES),
	
	PERMITE_ALTERAR_CATEGORIA_DESPESA_COMPRA("PermiteAlterarCategoriaDespesaCompra", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_PermiteAlterarCategoriaDespesa_titulo"),UteisJSF.internacionalizar("per_PermiteAlterarCategoriaDespesa_ajuda"))
			},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoComprasEnum.COMPRA, 
			PerfilAcessoSubModuloEnum.COMPRAS_REQUISICOES),
	
	LIBERAR_VALOR_ACIMA_PREVISTO_PLANO_ORCAMENTARIO("LiberarValorAcimaPrevistoPlanoOrcamentario", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_LiberarValorAcimaPrevistoPlanoOrcamentario_titulo"),UteisJSF.internacionalizar("per_LiberarValorAcimaPrevistoPlanoOrcamentario_ajuda"))
			},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoComprasEnum.COMPRA, 
			PerfilAcessoSubModuloEnum.COMPRAS_COTACAO_COMPRA),
	/**
	* Painel Gestor Compras Aguardando Recebimento
	*
	*/	
	PAINEL_GESTOR_COMPRAS_AGUARDANDO_RECEBIMENTO("PainelGestorComprasAguardandoRecebimento", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_PainelGestorComprasAguardandoRecebimento_titulo"),UteisJSF.internacionalizar("per_PainelGestorComprasAguardandoRecebimento_ajuda"), new String[]{""})
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.COMPRAS_BASICO),
	/**
	* Produto Servico
	*
	*/	
	PRODUTO_SERVICO("ProdutoServico", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_ProdutoServico_titulo"),UteisJSF.internacionalizar("per_ProdutoServico_ajuda"), new String[]{"produtoServicoCons.xhtml","produtoServicoForm.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.COMPRAS_BASICO),
	/**
	* Movimentacao Estoque
	*
	*/	
	MOVIMENTACAO_ESTOQUE("MovimentacaoEstoque", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_MovimentacaoEstoque_titulo"),UteisJSF.internacionalizar("per_MovimentacaoEstoque_ajuda"), new String[]{"movimentacaoEstoqueCons.xhtml","movimentacaoEstoqueForm.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.COMPRAS_BASICO),
	
	PERMITE_ALTERAR_CENTRO_RESULTADO_MOVIMENTACAO_ESTOQUE("PermiteAlterarCentroResultadoMovimentacaoEstoque", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_PermiteAlterarCentroResultadoMovimentacaoEstoque_titulo"),UteisJSF.internacionalizar("per_PermiteAlterarCentroResultadoMovimentacaoEstoque_ajuda"))
			},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoComprasEnum.MOVIMENTACAO_ESTOQUE, 
			PerfilAcessoSubModuloEnum.COMPRAS_BASICO),
	
	PERMITE_ALTERAR_CENTRO_RESULTADO_DESTINO_MOVIMENTACAO_ESTOQUE("PermiteAlterarCentroResultadoDestinoMovimentacaoEstoque", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_PermiteAlterarCentroResultadoDestinoMovimentacaoEstoque_titulo"),UteisJSF.internacionalizar("per_PermiteAlterarCentroResultadoDestinoMovimentacaoEstoque_ajuda"))
			},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoComprasEnum.MOVIMENTACAO_ESTOQUE, 
			PerfilAcessoSubModuloEnum.COMPRAS_BASICO),
	/**
	* Painel Gestor Cotação Aguardando Autorização
	*
	*/	
	PAINEL_GESTOR_COTAÇÃO_AGUARDANDO_AUTORIZAÇÃO("PainelGestorCotaçãoAguardandoAutorização", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_PainelGestorCotacaoAguardandoAutorizacao_titulo"),UteisJSF.internacionalizar("per_PainelGestorCotacaoAguardandoAutorizacao_ajuda"), new String[]{""})
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.COMPRAS_BASICO),
	/**
	* Estoque
	*
	*/	
	ESTOQUE("Estoque", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_Estoque_titulo"),UteisJSF.internacionalizar("per_Estoque_ajuda"), new String[]{"estoqueCons.xhtml","estoqueForm.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.COMPRAS_BASICO),
	/**
	* Categoria Produto
	*
	*/	
	CATEGORIA_PRODUTO("CategoriaProduto", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_CategoriaProduto_titulo"),UteisJSF.internacionalizar("per_CategoriaProduto_ajuda"), new String[]{"categoriaProdutoCons.xhtml","categoriaProdutoForm.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.COMPRAS_BASICO),
	/**
	* Painel Gestor Requisições Aguardando Autorização
	*
	*/	
	PAINEL_GESTOR_REQUISIÇÕES_AGUARDANDO_AUTORIZAÇÃO("PainelGestorRequisiçõesAguardandoAutorização", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_PainelGestorRequisicoesAguardandoAutorizacao_titulo"),UteisJSF.internacionalizar("per_PainelGestorRequisicoesAguardandoAutorizacao_ajuda"), new String[]{""})
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.COMPRAS_BASICO),
	/**
	* Credito Fornecedor
	*
	*/	
	CREDITO_FORNECEDOR("CreditoFornecedor", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_CreditoFornecedor_titulo"),UteisJSF.internacionalizar("per_CreditoFornecedor_ajuda"), new String[]{""})
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.COMPRAS_BASICO),

	/**
	* Painel Gestor Estoque Abaixo Mínimo
	*
	*/	
	PAINEL_GESTOR_ESTOQUE_ABAIXO_MÍNIMO("PainelGestorEstoqueAbaixoMínimo", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_PainelGestorEstoqueAbaixoMinimo_titulo"),UteisJSF.internacionalizar("per_PainelGestorEstoqueAbaixoMinimo_ajuda"), new String[]{""})
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.COMPRAS_BASICO),
	
	QUESTIONARIO_REQUISICAO("QuestionarioRequisicao", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_QuestionarioCompras_titulo"),UteisJSF.internacionalizar("per_QuestionarioCompras_ajuda"), new String[]{"questionarioCons.xhtml","questionarioForm.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.COMPRAS_BASICO),

	PERGUNTA_REQUISICAO("PerguntaRequisicao", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_PerguntasCompras_titulo"),UteisJSF.internacionalizar("per_PerguntasCompras_ajuda"), new String[]{"perguntaCons.xhtml","perguntaForm.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.COMPRAS_BASICO),

	/**
	* Requisicao Rel
	*
	*/	
	REQUISICAO_REL("RequisicaoRel", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_RequisicaoRel_titulo"),UteisJSF.internacionalizar("per_RequisicaoRel_ajuda"), new String[]{"requisicaoRel.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.RELATORIO, 
			null, 
			PerfilAcessoSubModuloEnum.COMPRAS_RELATORIOS),
	/**
	* Estoque Rel
	*
	*/	
	ESTOQUE_REL("EstoqueRel", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_EstoqueRel_titulo"),UteisJSF.internacionalizar("per_EstoqueRel_ajuda"), new String[]{"estoqueRel.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.RELATORIO, 
			null, 
			PerfilAcessoSubModuloEnum.COMPRAS_RELATORIOS),
	/**
	* Recebimento Compra Rel
	*
	*/	
	RECEBIMENTO_COMPRA_REL("RecebimentoCompraRel", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_RecebimentoCompraRel_titulo"),UteisJSF.internacionalizar("per_RecebimentoCompraRel_ajuda"), new String[]{"recebimentoCompraRel.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.RELATORIO, 
			null, 
			PerfilAcessoSubModuloEnum.COMPRAS_RELATORIOS),
	/**
	* Compra Rel
	*
	*/	
	COMPRA_REL("CompraRel", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_CompraRel_titulo"),UteisJSF.internacionalizar("per_CompraRel_ajuda"), new String[]{"compraRel.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.RELATORIO, 
			null, 
			PerfilAcessoSubModuloEnum.COMPRAS_RELATORIOS),	
	/**
	* Produto/Servico Rel
	*
	*/	
	PRODUTO_SERVICO_REL("ProdutoServicoRel", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_ProdutoServicoRel_titulo"),UteisJSF.internacionalizar("per_ProdutoServicoRel_ajuda"), new String[]{"produtoServicoRel.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.RELATORIO, 
			null, 
			PerfilAcessoSubModuloEnum.COMPRAS_RELATORIOS),
	
	/**
	 * Relatorio SEI Decidir Compras
	 *
	 */
	RELATORIO_SEIDECIDIR_COMPRA("RelatorioSEIDecidirCompra",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_RelatorioSEIDecidirCompra_titulo"),
					UteisJSF.internacionalizar("per_RelatorioSEIDecidirCompra_ajuda"),
					new String[] { "relatorioSEIDecidirRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.COMPRAS_RELATORIOS),
	PERMITIR_GERAR_RELATORIO_SEIDECIDIR_COMPRA_APENAS_DADOS("PermitirGerarRelatorioSeiDecidirCompraApenasDados",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirGerarRelatorioSeiDecidirCompraApenasDados_titulo"),
					UteisJSF.internacionalizar("per_PermitirGerarRelatorioSeiDecidirCompraApenasDados_ajuda"),
					new String[] { "relatorioSEIDecidirRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, RELATORIO_SEIDECIDIR_COMPRA,
			PerfilAcessoSubModuloEnum.COMPRAS_RELATORIOS),
	
	PERMITIR_VISUALIZAR_SCRIPT_SQL_RELATORIO_SEIDECIDIR_COMPRA("PermitirVisualizarScriptSqlRelatorioSeiDecidirCompra",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirVisualizarScriptSqlRelatorioSeiDecidir_titulo"),
					UteisJSF.internacionalizar("per_PermitirVisualizarScriptSqlRelatorioSeiDecidir_ajuda"),
					new String[] { "relatorioSEIDecidirRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, RELATORIO_SEIDECIDIR_COMPRA,
			PerfilAcessoSubModuloEnum.COMPRAS_RELATORIOS),
	
	/**
	* Entrega Requisicao
	*
	*/
	ENTREGA_REQUISICAO("EntregaRequisicao", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_EntregaRequisicao_titulo"),UteisJSF.internacionalizar("per_EntregaRequisicao_ajuda"), new String[]{"entregaRequisicaoCons.xhtml","entregaRequisicaoForm.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.COMPRAS_REQUISICOES),
	
	/**
	* Mapa Requisicao
	*
	*/
	MAPA_REQUISICAO("MapaRequisicao", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_MapaRequisicao_titulo"),UteisJSF.internacionalizar("per_MapaRequisicao_ajuda"), new String[]{"mapaRequisicaoCons.xhtml","mapaRequisicaoForm.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.COMPRAS_REQUISICOES),
	
	AUTORIZAR_MAPA_REQUISICAO("AutorizarMapaRequisicao", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_AutorizarRequisicao_titulo"),UteisJSF.internacionalizar("per_AutorizarRequisicao_ajuda"))
			},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoComprasEnum.MAPA_REQUISICAO, 
			PerfilAcessoSubModuloEnum.COMPRAS_REQUISICOES),
	
	PERMITE_VISUALIZAR_TODAS_UNIDADES_ENSINO("PermiteVisualiarRequisicaoTodasUnidadesEnsino", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_PermiteVisualiarRequisicaoTodasUnidadesEnsino_titulo"),UteisJSF.internacionalizar("per_PermiteVisualiarRequisicaoTodasUnidadesEnsino_ajuda"))
			},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoComprasEnum.MAPA_REQUISICAO, 
			PerfilAcessoSubModuloEnum.COMPRAS_REQUISICOES),
	
	PERMITE_ALTERAR_ESTOQUE_RETIRADA("PermiteAlterarEstoqueRetirada", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_PermiteAlterarEstoqueRetirada_titulo"),UteisJSF.internacionalizar("per_PermiteAlterarEstoqueRetirada_ajuda"))
			},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoComprasEnum.MAPA_REQUISICAO, 
			PerfilAcessoSubModuloEnum.COMPRAS_REQUISICOES),
	
	PERMITE_DESFAZER_AUTORIZACAO_REQUISICAO("PermiteDesfazerAutorizacaoRequisicao", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_PermiteDesfazerAutorizacaoRequisicao_titulo"),UteisJSF.internacionalizar("per_PermiteDesfazerAutorizacaoRequisicao_ajuda"))
	},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoComprasEnum.MAPA_REQUISICAO, 
			PerfilAcessoSubModuloEnum.COMPRAS_REQUISICOES),
	
	PERMITE_LIBERAR_VALOR_MAXIMO_COMPRA_DIRETA("PermiteLiberarValorMaximoCompraDireta", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_PermiteLiberarValorMaximoCompraDireta_titulo"),UteisJSF.internacionalizar("per_PermiteLiberarValorMaximoCompraDireta_ajuda"))
			},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoComprasEnum.MAPA_REQUISICAO, 
			PerfilAcessoSubModuloEnum.COMPRAS_REQUISICOES),
	
	
	
	
	
	/**
	 * Requisicao
	 *
	 */
	REQUISICAO("Requisicao", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.PROFESSOR, UteisJSF.internacionalizar("per_Requisicao_titulo"),UteisJSF.internacionalizar("per_Requisicao_ajuda"), new String[]{"requisicaoProfessor.xhtml"}),
			 new PermissaoVisao(TipoVisaoEnum.COORDENADOR, UteisJSF.internacionalizar("per_Requisicao_titulo"),UteisJSF.internacionalizar("per_Requisicao_ajuda"), new String[]{"requisicaoCoordenador.xhtml"}),
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_Requisicao_titulo"),UteisJSF.internacionalizar("per_Requisicao_ajuda"), new String[]{"requisicaoCons.xhtml","requisicaoForm.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.COMPRAS_REQUISICOES),
	
	AUTORIZAR_REQUISICAO("AutorizarRequisicao", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.PROFESSOR, UteisJSF.internacionalizar("per_AutorizarRequisicao_titulo"),UteisJSF.internacionalizar("per_AutorizarRequisicao_ajuda")),
			 new PermissaoVisao(TipoVisaoEnum.COORDENADOR, UteisJSF.internacionalizar("per_AutorizarRequisicao_titulo"),UteisJSF.internacionalizar("per_AutorizarRequisicao_ajuda")),
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_AutorizarRequisicao_titulo"),UteisJSF.internacionalizar("per_AutorizarRequisicao_ajuda"))
			},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoComprasEnum.REQUISICAO, 
			PerfilAcessoSubModuloEnum.COMPRAS_REQUISICOES),
	
	PERMITE_ALTERAR_CATEGORIA_DESPESA("PermiteAlterarCategoriaDespesa", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.PROFESSOR, UteisJSF.internacionalizar("per_PermiteAlterarCategoriaDespesa_titulo"),UteisJSF.internacionalizar("per_PermiteAlterarCategoriaDespesa_ajuda")),
			 new PermissaoVisao(TipoVisaoEnum.COORDENADOR, UteisJSF.internacionalizar("per_PermiteAlterarCategoriaDespesa_titulo"),UteisJSF.internacionalizar("per_PermiteAlterarCategoriaDespesa_ajuda")),
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_PermiteAlterarCategoriaDespesa_titulo"),UteisJSF.internacionalizar("per_PermiteAlterarCategoriaDespesa_ajuda"))
			},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoComprasEnum.REQUISICAO, 
			PerfilAcessoSubModuloEnum.COMPRAS_REQUISICOES),
	
	PERMITE_ALTERAR_REQUISITANTE("PermiteAlterarRequisitante", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.PROFESSOR, UteisJSF.internacionalizar("per_PermiteAlterarRequisitante_titulo"),UteisJSF.internacionalizar("per_PermiteAlterarRequisitante_ajuda")),
			 new PermissaoVisao(TipoVisaoEnum.COORDENADOR, UteisJSF.internacionalizar("per_PermiteAlterarRequisitante_titulo"),UteisJSF.internacionalizar("per_PermiteAlterarRequisitante_ajuda")),
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_PermiteAlterarRequisitante_titulo"),UteisJSF.internacionalizar("per_PermiteAlterarRequisitante_ajuda"))
			},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoComprasEnum.REQUISICAO, 
			PerfilAcessoSubModuloEnum.COMPRAS_REQUISICOES),
	
	PERMITE_CADASTRAR_REQUISICAO_TODAS_UNIDADES_ENSINO("PermiteCadastrarRequisicaoTodasUnidadesEnsino", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.PROFESSOR, UteisJSF.internacionalizar("per_PermiteCadastrarRequisicaoTodasUnidadesEnsino_titulo"),UteisJSF.internacionalizar("per_PermiteCadastrarRequisicaoTodasUnidadesEnsino_ajuda")),
			 new PermissaoVisao(TipoVisaoEnum.COORDENADOR, UteisJSF.internacionalizar("per_PermiteCadastrarRequisicaoTodasUnidadesEnsino_titulo"),UteisJSF.internacionalizar("per_PermiteCadastrarRequisicaoTodasUnidadesEnsino_ajuda")), 
			new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_PermiteCadastrarRequisicaoTodasUnidadesEnsino_titulo"),UteisJSF.internacionalizar("per_PermiteCadastrarRequisicaoTodasUnidadesEnsino_ajuda"))
			},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoComprasEnum.REQUISICAO, 
			PerfilAcessoSubModuloEnum.COMPRAS_REQUISICOES),
	
	PERMITE_ALTERAR_CENTRO_RESULTADO_REQUISICAO("PermiteAlterarCentroResultadoRequisicao", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.PROFESSOR, UteisJSF.internacionalizar("per_PermiteAlterarCentroResultado_titulo"),UteisJSF.internacionalizar("per_PermiteAlterarCentroResultado_ajuda")),
			 new PermissaoVisao(TipoVisaoEnum.COORDENADOR, UteisJSF.internacionalizar("per_PermiteAlterarCentroResultado_titulo"),UteisJSF.internacionalizar("per_PermiteAlterarCentroResultado_ajuda")),
			new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_PermiteAlterarCentroResultado_titulo"),UteisJSF.internacionalizar("per_PermiteAlterarCentroResultado_ajuda"))
			},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoComprasEnum.REQUISICAO, 
			PerfilAcessoSubModuloEnum.COMPRAS_REQUISICOES),
	PERMITIR_ANEXAR_ARQUIVO("PermitirAnexarArquivo", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.PROFESSOR, UteisJSF.internacionalizar("per_PermitirAnexarArquivo_titulo"),UteisJSF.internacionalizar("per_PermitirAnexarArquivo_ajuda")),
			 new PermissaoVisao(TipoVisaoEnum.COORDENADOR, UteisJSF.internacionalizar("per_PermitirAnexarArquivo_titulo"),UteisJSF.internacionalizar("per_PermitirAnexarArquivo_ajuda")),
			new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_PermitirAnexarArquivo_titulo"),UteisJSF.internacionalizar("per_PermitirAnexarArquivo_ajuda"))
			},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoComprasEnum.REQUISICAO, 
			PerfilAcessoSubModuloEnum.COMPRAS_REQUISICOES),
	
	
	
	TRAMITE_COTACAO_COMPRA("TramiteCotacaoCompra", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_Tramite_titulo"),UteisJSF.internacionalizar("per_Tramite_ajuda"), new String[]{"tramiteCotacaoCompraCons.xhtml","tramiteCotacaoCompraForm.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.COMPRAS_COTACAO_COMPRA);
	
	

	/**
	 * @param descricao
	 * @param ajuda
	 * @param paginaAcesso
	 * @param tipoPerfilAcesso
	 * @paran permissaoSuperiorEnum - popular quando no tipoPerfilAcesso for funcionalidade 
	 */
	private PerfilAcessoPermissaoComprasEnum(String valor, PermissaoVisao[] permissaoVisao, 
			TipoPerfilAcessoPermissaoEnum tipoPerfilAcesso,
			Enum<? extends PerfilAcessoPermissaoEnumInterface> permissaoSuperiorEnum,
			PerfilAcessoSubModuloEnum perfilAcessoSubModulo			
			) {
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
		return getTipoPerfilAcesso().equals(TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE) && getTipoPerfilAcesso().equals(TipoPerfilAcessoPermissaoEnum.RELATORIO);
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
				descricaoModuloSubModulo = getPerfilAcessoSubModulo().getPerfilAcessoModuloEnum().getValorApresentar()+" - "+getPerfilAcessoSubModulo().getDescricao();				
		}else {
			descricaoModuloSubModulo ="";
		}
		}
		return descricaoModuloSubModulo;
	}		
		 
	 
	
}
