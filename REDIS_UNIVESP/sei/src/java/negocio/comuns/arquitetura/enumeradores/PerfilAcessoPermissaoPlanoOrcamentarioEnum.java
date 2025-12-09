package negocio.comuns.arquitetura.enumeradores;

import java.util.Arrays;
import java.util.List;

import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

/**
 * @author Rodrigo Wind
 *
 */
public enum PerfilAcessoPermissaoPlanoOrcamentarioEnum implements PerfilAcessoPermissaoEnumInterface {

	/**
	 * Plano Orcamentario
	 *
	 */
	PLANO_ORCAMENTARIO("PlanoOrcamentario",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PlanoOrcamentario_titulo"),
					UteisJSF.internacionalizar("per_PlanoOrcamentario_ajuda"),
					new String[] { "planoOrcamentarioCons.xhtml", "planoOrcamentarioForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null,
			PerfilAcessoSubModuloEnum.PLANO_ORCAMENTARIO_PLANO_ORCAMENTARIO),
	PERMITIR_REATIVAR_PLANO_ORCAMENTARIO("PermitirReativarPlanoOrcamentario",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirReativarPlanoOrcamentario_titulo"),
					UteisJSF.internacionalizar("per_PermitirReativarPlanoOrcamentario_ajuda")), },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoPlanoOrcamentarioEnum.PLANO_ORCAMENTARIO,
			PerfilAcessoSubModuloEnum.PLANO_ORCAMENTARIO_PLANO_ORCAMENTARIO),

	/**
	 * Solicitacao Orcamento Plano Orcamentario
	 *
	 */
	SOLICITACAO_ORCAMENTO_PLANO_ORCAMENTARIO("SolicitacaoOrcamentoPlanoOrcamentario",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_SolicitacaoOrcamentoPlanoOrcamentario_titulo"),
					UteisJSF.internacionalizar("per_SolicitacaoOrcamentoPlanoOrcamentario_ajuda"),
					new String[] { "solicitacaoOrcamentoPlanoOrcamentarioCons.xhtml",
							"solicitacaoOrcamentoPlanoOrcamentarioForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null,
			PerfilAcessoSubModuloEnum.PLANO_ORCAMENTARIO_PLANO_ORCAMENTARIO),

	RELATORIO_SEI_DECIDIR("RelatorioSEIDecidirPlanoOrcamentario",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_RelatorioSEIDecidir_titulo"),
					UteisJSF.internacionalizar("per_RelatorioSEIDecidir_ajuda"),
					new String[] { "relatorioSEIDecidirRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.PLANO_ORCAMENTARIO_RELATORIOS),
	PERMITIR_GERAR_RELATORIO_SEIDECIDIR_PLANO_ORCAMENTARIO_APENAS_DADOS("PermitirGerarRelatorioSeiDecidirPlanoOrcamentarioApenasDados",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirGerarRelatorioSeiDecidirAcademicoApenasDados_titulo"),
					UteisJSF.internacionalizar("per_PermitirGerarRelatorioSeiDecidirAcademicoApenasDados_ajuda"),
					new String[] { "relatorioSEIDecidirRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, RELATORIO_SEI_DECIDIR,
			PerfilAcessoSubModuloEnum.PLANO_ORCAMENTARIO_RELATORIOS),

	// Funcionalidades
	PERMITIR_ACESSAR_PLANO_ORCAMENTO_TODOS_DEPARTAMENTOS_UNIDADE_ENSINO(
			"PlanoOrcamentario_permitirAcessarPlanoOrcamentarioTodosDepartamentosUnidadeEnsino",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar(
					"per_PlanoOrcamentario_permitirAcessarPlanoOrcamentarioTodosDepartamentosUnidadeEnsino_titulo"),
					UteisJSF.internacionalizar(
							"per_PlanoOrcamentario_permitirAcessarPlanoOrcamentarioTodosDepartamentosUnidadeEnsino_ajuda")), },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE,
			PerfilAcessoPermissaoPlanoOrcamentarioEnum.SOLICITACAO_ORCAMENTO_PLANO_ORCAMENTARIO,
			PerfilAcessoSubModuloEnum.PLANO_ORCAMENTARIO_PLANO_ORCAMENTARIO),

	PERMITIR_REALIZAR_MANEJAMENTO_SALDOS_ORCAMENTARIOS("PlanoOrcamentario_permitirRealizarManejamento",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PlanoOrcamentario_permitirRealizarRemanejamento_titulo"),
					UteisJSF.internacionalizar("per_PlanoOrcamentario_permitirRealizarRemanejamento_ajuda")), },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE,
			PerfilAcessoPermissaoPlanoOrcamentarioEnum.SOLICITACAO_ORCAMENTO_PLANO_ORCAMENTARIO,
			PerfilAcessoSubModuloEnum.PLANO_ORCAMENTARIO_PLANO_ORCAMENTARIO),

	PERMITIR_REALIZAR_MANEJAMENTO_SALDOS_SOLICITACAO_ORCAMENTO(
			"PlanoOrcamentario_permitirRealizarManejamentoSaldoAprovado",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar(
							"per_PlanoOrcamentario_permitirRealizarRemanejamentoSaldoAprovado_titulo"),
					UteisJSF.internacionalizar(
							"per_PlanoOrcamentario_permitirRealizarRemanejamentoSaldoAprovado_ajuda")), },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE,
			PerfilAcessoPermissaoPlanoOrcamentarioEnum.SOLICITACAO_ORCAMENTO_PLANO_ORCAMENTARIO,
			PerfilAcessoSubModuloEnum.PLANO_ORCAMENTARIO_PLANO_ORCAMENTARIO);

	/**
	 * @param descricao
	 * @param ajuda
	 * @param paginaAcesso
	 * @param tipoPerfilAcesso
	 * @paran permissaoSuperiorEnum - popular quando no tipoPerfilAcesso for
	 *        funcionalidade
	 */

	private PerfilAcessoPermissaoPlanoOrcamentarioEnum(String valor, PermissaoVisao[] permissaoVisao,
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
