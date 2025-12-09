package negocio.comuns.administrativo;

import negocio.comuns.administrativo.enumeradores.FormaGeracaoEventoAulaOnLineGoogleMeetEnum;
import negocio.comuns.administrativo.enumeradores.TipoGeracaoEmailIntegracaoEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.Uteis;

public class ConfiguracaoSeiGsuiteUnidadeEnsinoVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5519881769160369949L;
	private Integer codigo;
	private ConfiguracaoSeiGsuiteVO configuracaoSeiGsuiteVO;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private String unidadeOrganizacionalAluno;
	private String unidadeOrganizacionalFuncionario;
	private String dominioEmail;
	private String dominioEmailFuncionario;
	private boolean alterarSenhaProximoLogin = false;
	private boolean selecionado = false;
	private FormaGeracaoEventoAulaOnLineGoogleMeetEnum formaGeracaoEventoAulaOnLineGoogleMeet;
	private TipoGeracaoEmailIntegracaoEnum tipoGeracaoEmailGsuiteEnum;
	private Integer eventoAulaOnLineGoogleMeetDiasAntesAulaProgramada;
	private Integer notificarAlunoEventoAulaOnLineGoogleMeetDiasAntesAulaProgramada;
	
	private String listaMatriculaString;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public ConfiguracaoSeiGsuiteVO getConfiguracaoSeiGsuiteVO() {
		if (configuracaoSeiGsuiteVO == null) {
			configuracaoSeiGsuiteVO = new ConfiguracaoSeiGsuiteVO();
		}
		return configuracaoSeiGsuiteVO;
	}

	public void setConfiguracaoSeiGsuiteVO(ConfiguracaoSeiGsuiteVO configuracaoSeiGsuiteVO) {
		this.configuracaoSeiGsuiteVO = configuracaoSeiGsuiteVO;
	}

	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}

	public String getUnidadeOrganizacionalAluno() {
		if (unidadeOrganizacionalAluno == null) {
			unidadeOrganizacionalAluno = "";
		}
		return unidadeOrganizacionalAluno;
	}

	public void setUnidadeOrganizacionalAluno(String unidadeOrganizacionalAluno) {
		this.unidadeOrganizacionalAluno = unidadeOrganizacionalAluno;
	}

	public String getUnidadeOrganizacionalFuncionario() {
		if (unidadeOrganizacionalFuncionario == null) {
			unidadeOrganizacionalFuncionario = "";
		}
		return unidadeOrganizacionalFuncionario;
	}

	public void setUnidadeOrganizacionalFuncionario(String unidadeOrganizacionalFuncionario) {
		this.unidadeOrganizacionalFuncionario = unidadeOrganizacionalFuncionario;
	}

	public String getDominioEmailFuncionario() {
		if (dominioEmailFuncionario == null) {
			dominioEmailFuncionario = "";
		}
		return dominioEmailFuncionario;
	}

	public void setDominioEmailFuncionario(String dominioEmailFuncionario) {
		this.dominioEmailFuncionario = dominioEmailFuncionario;
	}

	public String getDominioEmail() {
		if (dominioEmail == null) {
			dominioEmail = "";
		}
		return dominioEmail;
	}

	public void setDominioEmail(String dominioEmail) {
		this.dominioEmail = dominioEmail;
	}

	public boolean isAlterarSenhaProximoLogin() {
		return alterarSenhaProximoLogin;
	}

	public void setAlterarSenhaProximoLogin(boolean alterarSenhaProximoLogin) {
		this.alterarSenhaProximoLogin = alterarSenhaProximoLogin;
	}

	public boolean isSelecionado() {
		return selecionado;
	}

	public void setSelecionado(boolean selecionado) {
		this.selecionado = selecionado;
	}

	public boolean equalsCampoSelecaoLista(ConfiguracaoSeiGsuiteUnidadeEnsinoVO obj) {
		return Uteis.isAtributoPreenchido(getUnidadeEnsinoVO()) && Uteis.isAtributoPreenchido(obj.getUnidadeEnsinoVO())
				&& getUnidadeEnsinoVO().getCodigo().equals(obj.getUnidadeEnsinoVO().getCodigo());

	}

	public FormaGeracaoEventoAulaOnLineGoogleMeetEnum getFormaGeracaoEventoAulaOnLineGoogleMeet() {
		if (formaGeracaoEventoAulaOnLineGoogleMeet == null) {
			formaGeracaoEventoAulaOnLineGoogleMeet = FormaGeracaoEventoAulaOnLineGoogleMeetEnum.MANUAL;
		}
		return formaGeracaoEventoAulaOnLineGoogleMeet;
	}

	public Integer getEventoAulaOnLineGoogleMeetDiasAntesAulaProgramada() {
		if (eventoAulaOnLineGoogleMeetDiasAntesAulaProgramada == null) {
			eventoAulaOnLineGoogleMeetDiasAntesAulaProgramada = 0;
		}
		return eventoAulaOnLineGoogleMeetDiasAntesAulaProgramada;
	}

	public void setFormaGeracaoEventoAulaOnLineGoogleMeet(FormaGeracaoEventoAulaOnLineGoogleMeetEnum formaGeracaoEventoAulaOnLineGoogleMeet) {
		this.formaGeracaoEventoAulaOnLineGoogleMeet = formaGeracaoEventoAulaOnLineGoogleMeet;
	}

	public void setEventoAulaOnLineGoogleMeetDiasAntesAulaProgramada(
			Integer eventoAulaOnLineGoogleMeetDiasAntesAulaProgramada) {
		this.eventoAulaOnLineGoogleMeetDiasAntesAulaProgramada = eventoAulaOnLineGoogleMeetDiasAntesAulaProgramada;
	}

	public Integer getNotificarAlunoEventoAulaOnLineGoogleMeetDiasAntesAulaProgramada() {
		if (notificarAlunoEventoAulaOnLineGoogleMeetDiasAntesAulaProgramada == null) {
			notificarAlunoEventoAulaOnLineGoogleMeetDiasAntesAulaProgramada = 0;
		}
		return notificarAlunoEventoAulaOnLineGoogleMeetDiasAntesAulaProgramada;
	}

	public void setNotificarAlunoEventoAulaOnLineGoogleMeetDiasAntesAulaProgramada(
			Integer notificarAlunoEventoAulaOnLineGoogleMeetDiasAntesAulaProgramada) {
		this.notificarAlunoEventoAulaOnLineGoogleMeetDiasAntesAulaProgramada = notificarAlunoEventoAulaOnLineGoogleMeetDiasAntesAulaProgramada;
	}

	public TipoGeracaoEmailIntegracaoEnum getTipoGeracaoEmailGsuiteEnum() {
		if (tipoGeracaoEmailGsuiteEnum == null) {
			tipoGeracaoEmailGsuiteEnum = TipoGeracaoEmailIntegracaoEnum.NOME;
		}
		return tipoGeracaoEmailGsuiteEnum;
	}

	public void setTipoGeracaoEmailGsuiteEnum(TipoGeracaoEmailIntegracaoEnum tipoGeracaoEmailGsuiteEnum) {
		this.tipoGeracaoEmailGsuiteEnum = tipoGeracaoEmailGsuiteEnum;
	}	
	
	public String getListaMatriculaString() {
		if(listaMatriculaString == null) {
			listaMatriculaString = "";
		}
		return listaMatriculaString;
	}

	public void setListaMatriculaString(String listaMatriculaString) {
		this.listaMatriculaString = listaMatriculaString;
	}	
	
	
}
