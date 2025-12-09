package negocio.comuns.arquitetura.enumeradores;

import java.util.Arrays;
import java.util.List;

import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

/**
 * @author Rodrigo Wind
 *
 */
public enum PerfilAcessoPermissaoNotaFiscalEnum implements PerfilAcessoPermissaoEnumInterface {

	/**
	 * Nota Fiscal Saida
	 *
	 */
	NOTA_FISCAL_SAIDA("NotaFiscalSaida",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_NotaFiscalSaida_titulo"),
					UteisJSF.internacionalizar("per_NotaFiscalSaida_ajuda"),
					new String[] { "notaFiscalSaidaCons.xhtml", "notaFiscalSaidaForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.NOTA_FISCAL_NOTA_FISCAL),

	/**
	 * Inutilização Nota Fiscal
	 *
	 */
	INUTILIZACAO_NOTA_FISCAL("InutilizacaoNotaFiscal", new PermissaoVisao[] { new PermissaoVisao(
			TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_InutilizacaoNotaFiscal_titulo"),
			UteisJSF.internacionalizar("per_InutilizacaoNotaFiscal_ajuda"),
			new String[] { "inutilizacaoNumeroNotaFiscalCons.xhtml", "inutilizacaoNumeroNotaFiscalForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.NOTA_FISCAL_NOTA_FISCAL),
	/**
	 * Configuracao Nota Fiscal
	 *
	 */
	CONFIGURACAO_NOTA_FISCAL("ConfiguracaoNotaFiscal",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ConfiguracaoNotaFiscal_titulo"),
					UteisJSF.internacionalizar("per_ConfiguracaoNotaFiscal_ajuda"),
					new String[] { "configuracaoNotaFiscalCons.xhtml", "configuracaoNotaFiscalForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.NOTA_FISCAL_NOTA_FISCAL),
	PERMITE_ALTERAR_ULTIMO_NUMERO_NOTA("PermiteAlterarUltimoNumeroNota",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermiteAlterarUltimoNumeroNota_titulo"),
					UteisJSF.internacionalizar("per_PermiteAlterarUltimoNumeroNota_ajuda")), },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoNotaFiscalEnum.CONFIGURACAO_NOTA_FISCAL,
			PerfilAcessoSubModuloEnum.NOTA_FISCAL_NOTA_FISCAL),

	IMPOSTO("Imposto",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_Imposto_titulo"), UteisJSF.internacionalizar("per_Imposto_ajuda"),
					new String[] { "impostoCons.xhtml", "impostoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.NOTA_FISCAL_ENTRADA),

	NOTA_FISCAL_ENTRADA("NotaFiscalEntrada",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_NotaFiscalEntrada_titulo"),
					UteisJSF.internacionalizar("per_NotaFiscalEntrada_ajuda"),
					new String[] { "notaFiscalEntradaCons.xhtml", "notaFiscalEntradaForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.NOTA_FISCAL_ENTRADA),

	PERMITIR_NOTA_FISCAL_ENTRADA_SEM_ORDEM_COMPRA("PermitirNotaFiscalEntradaSemOrdemCompra",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirNotaFiscalEntradaSemOrdemCompra_titulo"),
					UteisJSF.internacionalizar("per_PermitirNotaFiscalEntradaSemOrdemCompra_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoNotaFiscalEnum.NOTA_FISCAL_ENTRADA,
			PerfilAcessoSubModuloEnum.NOTA_FISCAL_ENTRADA),

	PERMITIR_ALTERAR_LANCAMENTO_CONTABIL_NOTA_FISCAL_ENTRADA("PermitirAlterarLancamentoContabilNotaFiscalEntrada",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirAlterarLancamentoContabilNotaFiscalEntrada_titulo"),
					UteisJSF.internacionalizar("per_PermitirAlterarLancamentoContabilNotaFiscalEntrada_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoNotaFiscalEnum.NOTA_FISCAL_ENTRADA,
			PerfilAcessoSubModuloEnum.NOTA_FISCAL_ENTRADA),

	NATUREZA_OPERACAO("NaturezaOperacao",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_NaturezaOperacao_titulo"),
					UteisJSF.internacionalizar("per_NaturezaOperacao_ajuda"),
					new String[] { "naturezaOperacaoCons.xhtml", "naturezaOperacaoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.NOTA_FISCAL_BASICO),

	IMPOSTOS_RETIDOS_NOTA_FISCAL_ENTRADA_REL("ImpostosRetidosNotaFiscalEntradaRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ImpostosRetidosNotaFiscalEntradaRel_titulo"),
					UteisJSF.internacionalizar("per_ImpostosRetidosNotaFiscalEntradaRel_ajuda"),
					new String[] { "impostosRetidosNotaFiscalEntradaRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.NOTA_FISCAL_RELATORIOS_ENTRADA),
	INTEGRACAO_GINFES_CURSO("IntegracaoGinfesCurso",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_IntegracaoGinfesCurso_titulo"),
					UteisJSF.internacionalizar("per_IntegracaoGinfesCurso_ajuda"),
					new String[] { "integracaoGinfesCursoCons.xhtml", "integracaoGinfesCursoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.NOTA_FISCAL_GINFES),
	INTEGRACAO_GINFES_ALUNO("IntegracaoGinfesAluno",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_IntegracaoGinfesAluno_titulo"),
					UteisJSF.internacionalizar("per_IntegracaoGinfesAluno_ajuda"),
					new String[] { "integracaoGinfesAlunoCons.xhtml", "integracaoGinfesAlunoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.NOTA_FISCAL_GINFES),
	VALORES_CURSO_GINFES("ValoresCursoGinfes",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_ValoresCursoGinfes_titulo"),
					UteisJSF.internacionalizar("per_ValoresCursoGinfes_ajuda"),
					new String[] { "valoresCursoGinfesCons.xhtml", "valoresCursoGinfesForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.NOTA_FISCAL_GINFES),

	INTEGRACAO_SYNDATA("IntegracaoSyndata",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_IntegracaoSyndata_titulo"),
					UteisJSF.internacionalizar("per_IntegracaoSyndata_ajuda"),
					new String[] { "integracaoSyndataCons.xhtml", "integracaoSyndataForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.NOTA_FISCAL_NOTA_FISCAL),
	/**
	 * Relatorio SEI Decidir Nota Fiscal
	 *
	 */
	RELATORIO_SEIDECIDIR_NOTA_FISCAL("RelatorioSEIDecidirNotaFiscal",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_RelatorioSEIDecidirNotaFiscal_titulo"),
					UteisJSF.internacionalizar("per_RelatorioSEIDecidirNotaFiscal_ajuda"),
					new String[] { "relatorioSEIDecidirRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.NOTA_FISCAL_RELATORIOS_ENTRADA),
	PERMITIR_GERAR_RELATORIO_SEIDECIDIR_ESTAGIO_APENAS_DADOS("PermitirGerarRelatorioSeiDecidirNotaFiscalApenasDados",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirGerarRelatorioSeiDecidirNotaFiscalApenasDados_titulo"),
					UteisJSF.internacionalizar("per_PermitirGerarRelatorioSeiDecidirNotaFiscalApenasDados_ajuda"),
					new String[] { "relatorioSEIDecidirRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, RELATORIO_SEIDECIDIR_NOTA_FISCAL,
			PerfilAcessoSubModuloEnum.NOTA_FISCAL_RELATORIOS_ENTRADA),
	
	PERMITIR_VISUALIZAR_SCRIPT_SQL_RELATORIO_SEIDECIDIR_NOTA_FISCAL("PermitirVisualizarScriptSqlRelatorioSeiDecidirNotaFiscal",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirVisualizarScriptSqlRelatorioSeiDecidir_titulo"),
					UteisJSF.internacionalizar("per_PermitirVisualizarScriptSqlRelatorioSeiDecidir_ajuda"),
					new String[] { "relatorioSEIDecidirRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, RELATORIO_SEIDECIDIR_NOTA_FISCAL,
			PerfilAcessoSubModuloEnum.NOTA_FISCAL_RELATORIOS_ENTRADA);

	/**
	 * @param descricao
	 * @param ajuda
	 * @param paginaAcesso
	 * @param tipoPerfilAcesso
	 * @paran permissaoSuperiorEnum - popular quando no tipoPerfilAcesso for
	 *        funcionalidade
	 */

	private PerfilAcessoPermissaoNotaFiscalEnum(String valor, PermissaoVisao[] permissaoVisao,
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
