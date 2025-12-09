package negocio.comuns.administrativo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.arquitetura.SuperVO;

public class ConfiguracaoSeiGsuiteVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2101436563093071854L;
	private Integer codigo;
	private String clienteSeiGsuite;
	private String tokenSeiGsuite;
	private String usuarioSeiGsuite;
	private String senhaSeiGsuite;
	private String urlExternaSeiGsuite;
	private ArquivoVO arquivoCredencialGoogle;
	private String refreshTokenCredencial;
	private String contaEmailGoogle;
	private String clienteGoogle;
	private String tokenGoogle;
	private String redirectUrlAplicacao;
	private String diretorioCredencialGoogle;
	private List<ConfiguracaoSeiGsuiteUnidadeEnsinoVO> listaConfiguracaoSeiGsuiteUnidadeEnsinoVO;
	private boolean ativarOperacoesDoClassroom = false;
	private boolean habilitarVerificacaoCalendarioClassroom = false;
	
	//transient
	private String valorAlterarDominioEmail;
	private String valorAlterarDominioEmailFuncionario;
	private String valorAlterarUnidadeOrganizacionalAluno;
	private String valorAlterarUnidadeOrganizacionalFuncionario;
	private boolean valorAlterarSenhaProximoLogin = false;

	public enum enumCampoConsultaConfiguracaoSeiGsuite {
		CODIGO, CLIENTE_SEI_GSUITE, CONTA_EMAIL_GOOGLE,
	}

	public Map<String, Object> getParametroBody() {
		Map<String, Object> parametroBody = new HashMap<>();
		parametroBody.put("grant_type", "password");
		parametroBody.put("scope", "all");
		parametroBody.put("username", getUsuarioSeiGsuite());
		parametroBody.put("password", getSenhaSeiGsuite());
		return parametroBody;
	}

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getClienteSeiGsuite() {
		if (clienteSeiGsuite == null) {
			clienteSeiGsuite = "client";
		}
		return clienteSeiGsuite;
	}

	public void setClienteSeiGsuite(String clienteSeiGsuite) {
		this.clienteSeiGsuite = clienteSeiGsuite;
	}

	public String getTokenSeiGsuite() {
		if (tokenSeiGsuite == null) {
			tokenSeiGsuite = "sei-password-oauth";
		}
		return tokenSeiGsuite;
	}

	public void setTokenSeiGsuite(String tokenSeiGsuite) {
		this.tokenSeiGsuite = tokenSeiGsuite;
	}

	public String getUsuarioSeiGsuite() {
		if (usuarioSeiGsuite == null) {
			usuarioSeiGsuite = "";
		}
		return usuarioSeiGsuite;
	}

	public void setUsuarioSeiGsuite(String usuarioSeiGsuite) {
		this.usuarioSeiGsuite = usuarioSeiGsuite;
	}

	public String getSenhaSeiGsuite() {
		if (senhaSeiGsuite == null) {
			senhaSeiGsuite = "";
		}
		return senhaSeiGsuite;
	}

	public void setSenhaSeiGsuite(String senhaSeiGsuite) {
		this.senhaSeiGsuite = senhaSeiGsuite;
	}

	public ArquivoVO getArquivoCredencialGoogle() {
		if (arquivoCredencialGoogle == null) {
			arquivoCredencialGoogle = new ArquivoVO();
		}
		return arquivoCredencialGoogle;
	}

	public void setArquivoCredencialGoogle(ArquivoVO arquivoCredencialGoogle) {
		this.arquivoCredencialGoogle = arquivoCredencialGoogle;
	}

	public String getContaEmailGoogle() {
		if (contaEmailGoogle == null) {
			contaEmailGoogle = "";
		}
		return contaEmailGoogle;
	}

	public void setContaEmailGoogle(String contaEmailGoogle) {
		this.contaEmailGoogle = contaEmailGoogle;
	}	

	public String getClienteGoogle() {
		if (clienteGoogle == null) {
			clienteGoogle = "";
		}
		return clienteGoogle;
	}

	public void setClienteGoogle(String clienteGoogle) {
		this.clienteGoogle = clienteGoogle;
	}

	public String getTokenGoogle() {
		if (tokenGoogle == null) {
			tokenGoogle = "";
		}
		return tokenGoogle;
	}

	public void setTokenGoogle(String tokenGoogle) {
		this.tokenGoogle = tokenGoogle;
	}

	public String getRedirectUrlAplicacao() {
		if (redirectUrlAplicacao == null) {
			redirectUrlAplicacao = "";
		}
		return redirectUrlAplicacao;
	}

	public void setRedirectUrlAplicacao(String redirectUrlAplicacao) {
		this.redirectUrlAplicacao = redirectUrlAplicacao;
	}

	public String getUrlExternaSeiGsuite() {
		if (urlExternaSeiGsuite == null) {
			urlExternaSeiGsuite = "";
		}
		return urlExternaSeiGsuite;
	}

	public void setUrlExternaSeiGsuite(String urlExternaSeiGsuite) {
		this.urlExternaSeiGsuite = urlExternaSeiGsuite;
	}

	public String getDiretorioCredencialGoogle() {
		if (diretorioCredencialGoogle == null) {
			diretorioCredencialGoogle = "";
		}
		return diretorioCredencialGoogle;
	}

	public void setDiretorioCredencialGoogle(String diretorioCredencialGoogle) {
		this.diretorioCredencialGoogle = diretorioCredencialGoogle;
	}

	public List<ConfiguracaoSeiGsuiteUnidadeEnsinoVO> getListaConfiguracaoSeiGsuiteUnidadeEnsinoVO() {
		if (listaConfiguracaoSeiGsuiteUnidadeEnsinoVO == null) {
			listaConfiguracaoSeiGsuiteUnidadeEnsinoVO = new ArrayList<>();
		}
		return listaConfiguracaoSeiGsuiteUnidadeEnsinoVO;
	}

	public void setListaConfiguracaoSeiGsuiteUnidadeEnsinoVO(List<ConfiguracaoSeiGsuiteUnidadeEnsinoVO> listaConfiguracaoSeiGsuiteUnidadeEnsinoVO) {
		this.listaConfiguracaoSeiGsuiteUnidadeEnsinoVO = listaConfiguracaoSeiGsuiteUnidadeEnsinoVO;
	}
	
	public ConfiguracaoSeiGsuiteUnidadeEnsinoVO getConfiguracaoSeiGsuiteUnidadeEnsinoVO(UnidadeEnsinoVO ue) {
		return getListaConfiguracaoSeiGsuiteUnidadeEnsinoVO()
		.stream()
		.filter(p->p.getUnidadeEnsinoVO().getCodigo().equals(ue.getCodigo()))
		.findFirst().get();
		 
	}

	public String getValorAlterarDominioEmailFuncionario() {
		if (valorAlterarDominioEmailFuncionario == null) {
			valorAlterarDominioEmailFuncionario = "";
		}
		return valorAlterarDominioEmailFuncionario;
	}

	public void setValorAlterarDominioEmailFuncionario(String valorAlterarDominioEmailFuncionario) {
		this.valorAlterarDominioEmailFuncionario = valorAlterarDominioEmailFuncionario;
	}

	public String getValorAlterarDominioEmail() {
		if (valorAlterarDominioEmail == null) {
			valorAlterarDominioEmail = "";
		}
		return valorAlterarDominioEmail;
	}

	public void setValorAlterarDominioEmail(String valorAlterarDominioEmail) {
		this.valorAlterarDominioEmail = valorAlterarDominioEmail;
	}

	public String getValorAlterarUnidadeOrganizacionalAluno() {
		if (valorAlterarUnidadeOrganizacionalAluno == null) {
			valorAlterarUnidadeOrganizacionalAluno = "";
		}
		return valorAlterarUnidadeOrganizacionalAluno;
	}

	public void setValorAlterarUnidadeOrganizacionalAluno(String valorAlterarUnidadeOrganizacionalAluno) {
		this.valorAlterarUnidadeOrganizacionalAluno = valorAlterarUnidadeOrganizacionalAluno;
	}

	public String getValorAlterarUnidadeOrganizacionalFuncionario() {
		if (valorAlterarUnidadeOrganizacionalFuncionario == null) {
			valorAlterarUnidadeOrganizacionalFuncionario = "";
		}
		return valorAlterarUnidadeOrganizacionalFuncionario;
	}

	public void setValorAlterarUnidadeOrganizacionalFuncionario(String valorAlterarUnidadeOrganizacionalFuncionario) {
		this.valorAlterarUnidadeOrganizacionalFuncionario = valorAlterarUnidadeOrganizacionalFuncionario;
	}

	public boolean isValorAlterarSenhaProximoLogin() {
		return valorAlterarSenhaProximoLogin;
	}

	public void setValorAlterarSenhaProximoLogin(boolean valorAlterarSenhaProximoLogin) {
		this.valorAlterarSenhaProximoLogin = valorAlterarSenhaProximoLogin;
	}

	public boolean isAtivarOperacoesDoClassroom() {
		return ativarOperacoesDoClassroom;
	}

	public void setAtivarOperacoesDoClassroom(boolean ativarOperacoesDoClassroom) {
		this.ativarOperacoesDoClassroom = ativarOperacoesDoClassroom;
	}

	public boolean isHabilitarVerificacaoCalendarioClassroom() {
		return habilitarVerificacaoCalendarioClassroom;
	}

	public void setHabilitarVerificacaoCalendarioClassroom(boolean habilitarVerificacaoCalendarioClassroom) {
		this.habilitarVerificacaoCalendarioClassroom = habilitarVerificacaoCalendarioClassroom;
	}

	public String getRefreshTokenCredencial() {
		return refreshTokenCredencial;
	}

	public void setRefreshTokenCredencial(String refreshTokenCredencial) {
		this.refreshTokenCredencial = refreshTokenCredencial;
	}
	
	
	
	
	
}
