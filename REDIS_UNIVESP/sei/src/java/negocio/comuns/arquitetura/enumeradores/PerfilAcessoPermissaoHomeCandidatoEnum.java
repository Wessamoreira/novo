package negocio.comuns.arquitetura.enumeradores;

import java.util.Arrays;
import java.util.List;

import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

/**
 * @author Rodrigo Wind
 *
 */
public enum PerfilAcessoPermissaoHomeCandidatoEnum implements PerfilAcessoPermissaoEnumInterface {

	/**
	 * Resultado Candidato
	 *
	 */
	RESULTADO_CANDIDATO("ResultadoCandidato", new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.CANDIDATO,
			UteisJSF.internacionalizar("per_ResultadoCandidato_titulo"),
			UteisJSF.internacionalizar("per_ResultadoCandidato_ajuda"), new String[] { "resultadoCandidato.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.HOME_CANDIDATO),
	/**
	 * Cursos Candidato
	 *
	 */
	CURSOS_CANDIDATO("CursosCandidato",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.CANDIDATO,
					UteisJSF.internacionalizar("per_CursosCandidato_titulo"),
					UteisJSF.internacionalizar("per_CursosCandidato_ajuda"), new String[] { "cursoCandidato.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.HOME_CANDIDATO),
	/**
	 * Boleto Candidato
	 *
	 */
	BOLETO_CANDIDATO("BoletoCandidato", new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.CANDIDATO,
			UteisJSF.internacionalizar("per_BoletoCandidato_titulo"),
			UteisJSF.internacionalizar("per_BoletoCandidato_ajuda"), new String[] { "boletoCandidato.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.HOME_CANDIDATO),
	/**
	 * Inscricao Candidato
	 *
	 */
	INSCRICAO_CANDIDATO("InscricaoCandidato", new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.CANDIDATO,
			UteisJSF.internacionalizar("per_InscricaoCandidato_titulo"),
			UteisJSF.internacionalizar("per_InscricaoCandidato_ajuda"), new String[] { "inscricaoCandidato.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.HOME_CANDIDATO),
	/**
	 * Comprovante Inscricao Candidato
	 *
	 */
	COMPROVANTE_INSCRICAO_CANDIDATO("ComprovanteInscricaoCandidato",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.CANDIDATO,
					UteisJSF.internacionalizar("per_ComprovanteInscricaoCandidato_titulo"),
					UteisJSF.internacionalizar("per_ComprovanteInscricaoCandidato_ajuda"),
					new String[] { "comprovanteInscricao.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.HOME_CANDIDATO),
	/**
	 * Calendario Candidato
	 *
	 */
	CALENDARIO_CANDIDATO("CalendarioCandidato",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.CANDIDATO,
					UteisJSF.internacionalizar("per_CalendarioCandidato_titulo"),
					UteisJSF.internacionalizar("per_CalendarioCandidato_ajuda"),
					new String[] { "calendarioCandidato.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.HOME_CANDIDATO);

	/**
	 * @param descricao
	 * @param ajuda
	 * @param paginaAcesso
	 * @param tipoPerfilAcesso
	 * @paran permissaoSuperiorEnum - popular quando no tipoPerfilAcesso for
	 *        funcionalidade
	 */

	private PerfilAcessoPermissaoHomeCandidatoEnum(String valor, PermissaoVisao[] permissaoVisao,
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
