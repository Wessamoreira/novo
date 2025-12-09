package negocio.comuns.arquitetura.enumeradores;

import java.util.Arrays;
import java.util.List;

import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

/**
 * @author Rodrigo Wind
 *
 */
public enum PerfilAcessoPermissaoPatrimonioEnum implements PerfilAcessoPermissaoEnumInterface {

	/**
	 * Local Armazenamento
	 *
	 */
	LOCAL_ARMAZENAMENTO("LocalArmazenamento",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_LocalArmazenamento_titulo"),
					UteisJSF.internacionalizar("per_LocalArmazenamento_ajuda"),
					new String[] { "localArmazenamentoCons.xhtml", "localArmazenamentoForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.PATRIMONIO_PATRIMONIO),
	/**
	 * Tipo Patrimonio
	 *
	 */
	TIPO_PATRIMONIO("TipoPatrimonio",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_TipoPatrimonio_titulo"),
					UteisJSF.internacionalizar("per_TipoPatrimonio_ajuda"),
					new String[] { "tipoPatrimonioCons.xhtml", "tipoPatrimonioForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.PATRIMONIO_PATRIMONIO),
	/**
	 * Patrimonio
	 *
	 */
	PATRIMONIO("Patrimonio", new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
			UteisJSF.internacionalizar("per_Patrimonio_titulo"), UteisJSF.internacionalizar("per_Patrimonio_ajuda"),
			new String[] { "patrimonioCons.xhtml", "patrimonioForm.xhtml" }) }, TipoPerfilAcessoPermissaoEnum.ENTIDADE,
			null, PerfilAcessoSubModuloEnum.PATRIMONIO_PATRIMONIO),
	/**
	 * Ocorrencia Patrimonio
	 *
	 */
	OCORRENCIA_PATRIMONIO("OcorrenciaPatrimonio",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_OcorrenciaPatrimonio_titulo"),
					UteisJSF.internacionalizar("per_OcorrenciaPatrimonio_ajuda"),
					new String[] { "ocorrenciaPatrimonioCons.xhtml", "ocorrenciaPatrimonioForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.PATRIMONIO_PATRIMONIO),
	PERMITIR_CADASTRAR_OCORRENCIA_EMPRESTIMO("PermitirCadastrarOcorrenciaEmprestimo",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirCadastrarOcorrenciaEmprestimo_titulo"),
					UteisJSF.internacionalizar("per_PermitirCadastrarOcorrenciaEmprestimo_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoPatrimonioEnum.OCORRENCIA_PATRIMONIO,
			PerfilAcessoSubModuloEnum.PATRIMONIO_PATRIMONIO),
	PERMITIR_CADASTRAR_OCORRENCIA_MANUTENCAO("PermitirCadastrarOcorrenciaManutencao",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirCadastrarOcorrenciaManutencao_titulo"),
					UteisJSF.internacionalizar("per_PermitirCadastrarOcorrenciaManutencao_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoPatrimonioEnum.OCORRENCIA_PATRIMONIO,
			PerfilAcessoSubModuloEnum.PATRIMONIO_PATRIMONIO),
	PERMITIR_CADASTRAR_OCORRENCIA_TROCA_LOCAL("PermitirCadastrarOcorrenciaTrocaLocal",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirCadastrarOcorrenciaTrocaLocal_titulo"),
					UteisJSF.internacionalizar("per_PermitirCadastrarOcorrenciaTrocaLocal_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoPatrimonioEnum.OCORRENCIA_PATRIMONIO,
			PerfilAcessoSubModuloEnum.PATRIMONIO_PATRIMONIO),
	PERMITIR_CADASTRAR_OCORRENCIA_DESCARTE("PermitirCadastrarOcorrenciaDescarte",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirCadastrarOcorrenciaDescarte_titulo"),
					UteisJSF.internacionalizar("per_PermitirCadastrarOcorrenciaDescarte_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoPatrimonioEnum.OCORRENCIA_PATRIMONIO,
			PerfilAcessoSubModuloEnum.PATRIMONIO_PATRIMONIO),
	PERMITIR_CADASTRAR_OCORRENCIA_SEPARACAO_DESCARTE("PermitirCadastrarOcorrenciaSeparacaoDescarte",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirCadastrarOcorrenciaSeparacaoDescarte_titulo"),
					UteisJSF.internacionalizar("per_PermitirCadastrarOcorrenciaSeparacaoDescarte_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoPatrimonioEnum.OCORRENCIA_PATRIMONIO,
			PerfilAcessoSubModuloEnum.PATRIMONIO_PATRIMONIO),

	PERMITIR_CADASTRAR_OCORRENCIA_RESERVA_UNIDADE("PermitirCadastrarOcorrenciaReservaUnidade",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirCadastrarOcorrenciaReservaUnidade_titulo"),
					UteisJSF.internacionalizar("per_PermitirCadastrarOcorrenciaReservaUnidade_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoPatrimonioEnum.OCORRENCIA_PATRIMONIO,
			PerfilAcessoSubModuloEnum.PATRIMONIO_PATRIMONIO),

	PERMITIR_CADASTRAR_OCORRENCIA_RESERVA_LOCAL("PermitirCadastrarOcorrenciaReservaLocal",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirCadastrarOcorrenciaReservaLocal_titulo"),
					UteisJSF.internacionalizar("per_PermitirCadastrarOcorrenciaReservaLocal_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoPatrimonioEnum.OCORRENCIA_PATRIMONIO,
			PerfilAcessoSubModuloEnum.PATRIMONIO_PATRIMONIO),

	PERMITIR_LIBERAR_RESERVA_ACIMA_QUANTIDADE_LIMITE_POR_REQUISITANTE(
			"PermissaoUsuarioLiberarReservaAcimaQuantidadeLimitePorRequisitante",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar(
							"per_PermitirUsuarioLiberarReservaAcimaQuantidadeLimitePorRequisitante_titulo"),
					UteisJSF.internacionalizar(
							"per_PermitirUsuarioLiberarReservaAcimaQuantidadeLimitePorRequisitante_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoPatrimonioEnum.OCORRENCIA_PATRIMONIO,
			PerfilAcessoSubModuloEnum.PATRIMONIO_PATRIMONIO),
	PERMITIR_LIBERAR_RESERVA_FORA_LIMITE_DATA_MAXIMA("permissaoUsuarioLiberarReservaForaLimiteDataMaxima",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirUsuarioLiberarReservaForaLimiteDataMaxima_titulo"),
					UteisJSF.internacionalizar("per_PermitirUsuarioLiberarReservaForaLimiteDataMaxima_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoPatrimonioEnum.OCORRENCIA_PATRIMONIO,
			PerfilAcessoSubModuloEnum.PATRIMONIO_PATRIMONIO),
	/**
	 * Mapa Patrimonio Separado Descarte
	 *
	 */
	MAPA_PATRIMONIO_SEPARADO_DESCARTE("MapaPatrimonioSeparadoDescarte",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_MapaPatrimonioSeparadoDescarte_titulo"),
					UteisJSF.internacionalizar("per_MapaPatrimonioSeparadoDescarte_ajuda"),
					new String[] { "mapaPatrimonioSeparadoDescarteForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.PATRIMONIO_PATRIMONIO),

	/**
	 * Mapa Patrimonio Separado Descarte
	 *
	 */
	GESTAO_RESERVA_PATRIMONIO("GestaoReservaPatrimonio",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_GestaoReservaPatrimonio_titulo"),
					UteisJSF.internacionalizar("per_MapaPatrimonioSeparadoDescarte_ajuda"),
					new String[] { "mapaPatrimonioSeparadoDescarteForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.PATRIMONIO_PATRIMONIO),

	/**
	 * Texto Padrao Patrimonio
	 *
	 */
	TEXTO_PADRAO_PATRIMONIO("TextoPadraoPatrimonio",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_TextoPadraoPatrimonio_titulo"),
					UteisJSF.internacionalizar("per_TextoPadraoPatrimonio_ajuda"),
					new String[] { "textoPadraoPatrimonioCons.xhtml", "textoPadraoPatrimonioForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.PATRIMONIO_RELATORIO),
	/**
	 * Patrimonio Rel
	 *
	 */
	PATRIMONIO_REL("PatrimonioRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PatrimonioRel_titulo"),
					UteisJSF.internacionalizar("per_PatrimonioRel_ajuda"), new String[] { "patrimonioRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.PATRIMONIO_RELATORIO),
	PERMITIR_APRESENTAR_VALOR_RECURSO_PATRIMONIO_REL("PermitirApresentarValorRecursoPatrimonioRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirApresentarValorRecursoPatrimonioRel_titulo"),
					UteisJSF.internacionalizar("per_PermitirApresentarValorRecursoPatrimonioRel_ajuda"),
					new String[] { "patrimonioRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PATRIMONIO_REL,
			PerfilAcessoSubModuloEnum.PATRIMONIO_RELATORIO),
	/**
	 * Ocorrencia Patrimonio Rel
	 *
	 */
	OCORRENCIA_PATRIMONIO_REL("OcorrenciaPatrimonioRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_OcorrenciaPatrimonioRel_titulo"),
					UteisJSF.internacionalizar("per_OcorrenciaPatrimonioRel_ajuda"),
					new String[] { "ocorrenciaPatrimonioRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.PATRIMONIO_RELATORIO),
	
	/**
	 * Relatorio SEI Decidir Patrimônio
	 *
	 */
	RELATORIO_SEIDECIDIR_PATRIMONIO("RelatorioSEIDecidirPatrimonio",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_RelatorioSEIDecidirPatrimonio_titulo"),
					UteisJSF.internacionalizar("per_RelatorioSEIDecidirPatrimonio_ajuda"),
					new String[] { "relatorioSEIDecidirRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.PATRIMONIO_RELATORIO),
	PERMITIR_GERAR_RELATORIO_SEIDECIDIR_PATRIMONIO_APENAS_DADOS("PermitirGerarRelatorioSeiDecidirPatrimonioApenasDados",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirGerarRelatorioSeiDecidirPatrimonioApenasDados_titulo"),
					UteisJSF.internacionalizar("per_PermitirGerarRelatorioSeiDecidirPatrimonioApenasDados_ajuda"),
					new String[] { "relatorioSEIDecidirRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, RELATORIO_SEIDECIDIR_PATRIMONIO,
			PerfilAcessoSubModuloEnum.PATRIMONIO_RELATORIO),
	
	PERMITIR_VISUALIZAR_SCRIPT_SQL_RELATORIO_SEIDECIDIR_PATRIMONIO("PermitirVisualizarScriptSqlRelatorioSeiDecidirPatrimonio",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirVisualizarScriptSqlRelatorioSeiDecidir_titulo"),
					UteisJSF.internacionalizar("per_PermitirVisualizarScriptSqlRelatorioSeiDecidir_ajuda"),
					new String[] { "relatorioSEIDecidirRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, RELATORIO_SEIDECIDIR_PATRIMONIO,
			PerfilAcessoSubModuloEnum.PATRIMONIO_RELATORIO);

	/**
	 * @param descricao
	 * @param ajuda
	 * @param paginaAcesso
	 * @param tipoPerfilAcesso
	 * @paran permissaoSuperiorEnum - popular quando no tipoPerfilAcesso for
	 *        funcionalidade
	 */

	private PerfilAcessoPermissaoPatrimonioEnum(String valor, PermissaoVisao[] permissaoVisao,
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
