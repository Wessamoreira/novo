package negocio.comuns.arquitetura.enumeradores;

import java.util.Arrays;
import java.util.List;

import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

/**
 * @author Rodrigo Wind
 *
 */
public enum PerfilAcessoPermissaoSeiDecidirEnum implements PerfilAcessoPermissaoEnumInterface {

	/**
	 * Painel Gestor
	 *
	 */
	PAINEL_GESTOR("PainelGestor", new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
			UteisJSF.internacionalizar("per_PainelGestor_titulo"), UteisJSF.internacionalizar("per_PainelGestor_ajuda"),
			new String[] { "painelGestor.xhtml", "painelGestorAcademicoFinanceiro.xhtml", "despesaDWForm.xhtml",
					"painelGestorPorCategoriaDespesaForm.xhtml",
					"painelGestorPorCategoriaDespesaDepartamentoForm.xhtml",
					"painelGestorMonitoramentoDescontoForm.xhtml",
					"painelGestorMonitoramentoDescontoConvenioForm.xhtml",
					"painelGestorMonitoramentoDescontoProgressivoForm.xhtml",
					"painelGestorMonitoramentoDescontoInstituicaoForm.xhtml",
					"painelGestorMonitoramentoDescontoInstituicao.xhtml",
					"painelGestorMonitoramentoDescontoTurmaProgressivoForm.xhtml",
					"painelGestorMonitoramentoDescontoTurmaInstituicaoForm.xhtml" }), },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.SEI_DECIDIR),
	PAINEL_GESTOR_ACADEMICO("PainelGestorAcademico",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PainelGestorAcademico_titulo"),
					UteisJSF.internacionalizar("per_PainelGestorAcademico_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoSeiDecidirEnum.PAINEL_GESTOR,
			PerfilAcessoSubModuloEnum.SEI_DECIDIR),
	PAINEL_GESTOR_CONSULTOR_POR_MATRICULA("painelGestorConsultorPorMatricula",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_painelGestorConsultorPorMatricula_titulo"),
					UteisJSF.internacionalizar("per_painelGestorConsultorPorMatricula_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoSeiDecidirEnum.PAINEL_GESTOR,
			PerfilAcessoSubModuloEnum.SEI_DECIDIR),
	PAINEL_GESTOR_PLANO_ORCAMENTARIO("PainelGestorPlanoOrcamentario",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PainelGestorPlanoOrcamentario_titulo"),
					UteisJSF.internacionalizar("per_PainelGestorPlanoOrcamentario_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoSeiDecidirEnum.PAINEL_GESTOR,
			PerfilAcessoSubModuloEnum.SEI_DECIDIR),
	FRASE_INSPIRACAO("FraseInspiracao",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_FraseInspiracao_titulo"),
					UteisJSF.internacionalizar("per_FraseInspiracao_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoSeiDecidirEnum.PAINEL_GESTOR,
			PerfilAcessoSubModuloEnum.SEI_DECIDIR),
	PAINEL_GESTOR_MONITORAR_POR_SEGMENTACAO("PainelGestorMonitorarPorSegmentacao",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PainelGestorMonitorarPorSegmentacao_titulo"),
					UteisJSF.internacionalizar("per_PainelGestorMonitorarPorSegmentacao_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoSeiDecidirEnum.PAINEL_GESTOR,
			PerfilAcessoSubModuloEnum.SEI_DECIDIR),
	PAINEL_GESTOR_MONITORAR_CONSULTOR("PainelGestorMonitorarConsultor",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PainelGestorMonitorarConsultor_titulo"),
					UteisJSF.internacionalizar("per_PainelGestorMonitorarConsultor_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoSeiDecidirEnum.PAINEL_GESTOR,
			PerfilAcessoSubModuloEnum.SEI_DECIDIR),
	PAINEL_GESTOR_CRM("PainelGestorCrm",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PainelGestorCrm_titulo"),
					UteisJSF.internacionalizar("per_PainelGestorCrm_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoSeiDecidirEnum.PAINEL_GESTOR,
			PerfilAcessoSubModuloEnum.SEI_DECIDIR),
	PAINEL_GESTOR_CONSUMO("PainelGestorConsumo",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PainelGestorConsumo_titulo"),
					UteisJSF.internacionalizar("per_PainelGestorConsumo_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoSeiDecidirEnum.PAINEL_GESTOR,
			PerfilAcessoSubModuloEnum.SEI_DECIDIR),
	PAINEL_GESTOR_FINANCEIRO("PainelGestorFinanceiro",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PainelGestorFinanceiro_titulo"),
					UteisJSF.internacionalizar("per_PainelGestorFinanceiro_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoSeiDecidirEnum.PAINEL_GESTOR,
			PerfilAcessoSubModuloEnum.SEI_DECIDIR),
	PAINEL_GESTOR_DESPESA_CATEGORIA("PainelGestorDespesaCategoria",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PainelGestorDespesaCategoria_titulo"),
					UteisJSF.internacionalizar("per_PainelGestorDespesaCategoria_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoSeiDecidirEnum.PAINEL_GESTOR,
			PerfilAcessoSubModuloEnum.SEI_DECIDIR);

	/**
	 * @param descricao
	 * @param ajuda
	 * @param paginaAcesso
	 * @param tipoPerfilAcesso
	 * @paran permissaoSuperiorEnum - popular quando no tipoPerfilAcesso for
	 *        funcionalidade
	 */

	private PerfilAcessoPermissaoSeiDecidirEnum(String valor, PermissaoVisao[] permissaoVisao,
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
