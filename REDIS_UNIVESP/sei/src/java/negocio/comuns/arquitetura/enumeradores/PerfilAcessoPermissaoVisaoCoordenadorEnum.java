package negocio.comuns.arquitetura.enumeradores;

import java.util.Arrays;
import java.util.List;

import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

/**
 * @author Rodrigo Wind
 *
 */
public enum PerfilAcessoPermissaoVisaoCoordenadorEnum implements PerfilAcessoPermissaoEnumInterface {

	/**
	 * Coordenador
	 *
	 */
	COORDENADOR("Coordenador",
			new PermissaoVisao[] {
					new PermissaoVisao(TipoVisaoEnum.COORDENADOR, UteisJSF.internacionalizar("per_Coordenador_titulo"),
							UteisJSF.internacionalizar("per_Coordenador_ajuda"), new String[] { "" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.VISAO_COORDENADOR),
	/**
	 * Registro Aula Coordenador
	 *
	 */
	REGISTRO_AULA_COORDENADOR("RegistroAulaCoordenador",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.COORDENADOR,
					UteisJSF.internacionalizar("per_RegistroAulaCoordenador_titulo"),
					UteisJSF.internacionalizar("per_RegistroAulaCoordenador_ajuda"), new String[] { "" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.VISAO_COORDENADOR),

	REGISTRAR_AULA_COORD_VISUALIZAR_MATRICULA_TR_CA("RegistrarAulaCoord_VisualizarMatriculaTR_CA",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.COORDENADOR,
					UteisJSF.internacionalizar("per_RegistrarAulaCoordVisualizarMatriculaTRCA_titulo"),
					UteisJSF.internacionalizar("per_RegistrarAulaCoordVisualizarMatriculaTRCA_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE,
			PerfilAcessoPermissaoVisaoCoordenadorEnum.REGISTRO_AULA_COORDENADOR,
			PerfilAcessoSubModuloEnum.VISAO_COORDENADOR),

	PERMITE_LANCAMENTO_AULA_FUTURA_COORDENADOR("PermiteLancamentoAulaFuturaCoordenador",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.COORDENADOR,
					UteisJSF.internacionalizar("per_PermiteLancamentoAulaFuturaCoordenador_titulo"),
					UteisJSF.internacionalizar("per_PermiteLancamentoAulaFuturaCoordenador_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE,
			PerfilAcessoPermissaoVisaoCoordenadorEnum.REGISTRO_AULA_COORDENADOR,
			PerfilAcessoSubModuloEnum.VISAO_COORDENADOR),
	/**
	 * Sub Menu Relatorio Coordenador
	 *
	 */
	SUB_MENU_RELATORIO_COORDENADOR("SubMenuRelatorioCoordenador",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.COORDENADOR,
					UteisJSF.internacionalizar("per_SubMenuRelatorioCoordenador_titulo"),
					UteisJSF.internacionalizar("per_SubMenuRelatorioCoordenador_ajuda"),
					new String[] { "relatorioCoordenador.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.VISAO_COORDENADOR),
	/**
	 * Criterio Avaliacao Aluno Visao Coordenador
	 *
	 */
	CRITERIO_AVALIACAO_ALUNO_VISAO_COORDENADOR("CriterioAvaliacaoAlunoVisaoCoordenador",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.COORDENADOR,
					UteisJSF.internacionalizar("per_CriterioAvaliacaoAlunoVisaoCoordenador_titulo"),
					UteisJSF.internacionalizar("per_CriterioAvaliacaoAlunoVisaoCoordenador_ajuda"),
					new String[] { "criterioAvaliacaoVisaoCoordenadorCons.xhtml",
							"criterioAvaliacaoVisaoCoordenadorForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.VISAO_COORDENADOR),

	/**
	 * Reserva Patrimonio Visao Coordenador
	 *
	 */
	RESERVA_PATRIMONIO_ALUNO_VISAO_COORDENADOR("OcorrenciaPatrimonioVisaoCoordenador",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.COORDENADOR,
					UteisJSF.internacionalizar("per_ReservaPatrimonioVisaoProfessor_titulo"),
					UteisJSF.internacionalizar("per_ReservaPatrimonioVisaoProfessor_ajuda"),
					new String[] { "ocorrenciaPatrimonioCoordenadorCons.xhtml",
							"ocorrenciaPatrimonioCoordenadorForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.VISAO_COORDENADOR),

	/**
	 * Estagio Coordenador
	 *
	 */
	ESTAGIO_COORDENADOR("EstagioCoordenador",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.COORDENADOR,
					UteisJSF.internacionalizar("per_EstagioCoordenador_titulo"),
					UteisJSF.internacionalizar("per_EstagioCoordenador_ajuda"),
					new String[] { "estagioCoordenadorCons.xhtml", "estagioCoordenadorForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.VISAO_COORDENADOR),

	/**
	 * Cronograma Aula
	 *
	 */
	CRONOGRAMA_AULA("CronogramaAula",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.COORDENADOR,
					UteisJSF.internacionalizar("per_CronogramaAula_titulo"),
					UteisJSF.internacionalizar("per_CronogramaAula_ajuda"), new String[] { "" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.VISAO_COORDENADOR),
	/**
	 * Monitoramento Alunos EAD Visao Coodernador
	 *
	 */
	MONITORAMENTO_ALUNOS_EADVISAO_COODERNADOR("MonitoramentoAlunosEADVisaoCoodernador",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.COORDENADOR,
					UteisJSF.internacionalizar("per_MonitoramentoAlunosEADVisaoCoodernador_titulo"),
					UteisJSF.internacionalizar("per_MonitoramentoAlunosEADVisaoCoodernador_ajuda"),
					new String[] { "monitoramentoAlunosEADVisaoCoodernadorCons.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.VISAO_COORDENADOR),

	MAPA_ATIVIDADE_EXTRA_CLASSE_PROFESSOR("MapaAtividadeExtraClasseProfessorVisaoCoordenador",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.COORDENADOR,
					UteisJSF.internacionalizar("per_MapaAtividadeExtraClasseProfessor_titulo"),
					UteisJSF.internacionalizar("per_MapaAtividadeExtraClasseProfessorRel_ajuda"),
					new String[] { "mapaAtividadeExtraClasseProfessorVisaoCoordenador.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.VISAO_COORDENADOR),

	DEFINIR_ATIVIDADE_EXTRA_CLASSE("DefinirAtividadeExtraClasseProfessorVisaoCoordenador",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.COORDENADOR,
					UteisJSF.internacionalizar("per_AtividadeExtraClasse_titulo"),
					UteisJSF.internacionalizar("per_AtividadeExtraClasse_ajuda"),
					new String[] { "atividadeExtraClasseProfessorVisaoCoordenadorForm.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.VISAO_COORDENADOR),

	RELATORIO_ATIVIDADE_EXTRA_CLASSE("RelatorioAtividadeExtraClasseProfessorVisaoCoordenador",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.COORDENADOR,
					UteisJSF.internacionalizar("per_AtividadeExtraClasseRelatorioSEIDecidir_titulo"),
					UteisJSF.internacionalizar("per_AtividadeExtraClasseRelatorioSEIDecidir_ajuda"),
					new String[] { "" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.VISAO_COORDENADOR);

	/**
	 * @param descricao
	 * @param ajuda
	 * @param paginaAcesso
	 * @param tipoPerfilAcesso
	 * @paran permissaoSuperiorEnum - popular quando no tipoPerfilAcesso for
	 *        funcionalidade
	 */

	private PerfilAcessoPermissaoVisaoCoordenadorEnum(String valor, PermissaoVisao[] permissaoVisao,
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
