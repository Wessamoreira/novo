package negocio.comuns.administrativo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.Constantes;

public class ConfiguracaoSeiBlackboardVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9197505774107480450L;
	private Integer codigo;
	private String usernameSeiBlackboard;
	private String senhaSeiBlackboard;
	private String clienteSeiBlackboard;
	private String tokenSeiBlackboard;
	private String urlExternaSeiBlackboard;
	private String urlExternaBlackboard;
	private String clientBlackboard;
	private String secretBlackboard;
	private String idBlackboard;
	private boolean ativarOperacoesDoBlackboard = false;
	private boolean ativarOperacaoEnsalamentoEstagio = false;
	private boolean ativarOperacaoEnsalamentoTCC = false;
	private Boolean importacaoEmRealizacao =  false;
	private List<ConfiguracaoSeiBlackboardDominioVO> listaConfiguracaoSeiBlackboardDominioVO;
	private String fonteDeDadosConteudoMasterBlackboard;

	public enum enumCampoConsultaConfiguracaoSeiBlackboard {
		CODIGO;
	}
	
	public void carregarDadosIniciaisTemporario() {
		setUrlExternaSeiBlackboard("http://localhost:8089/seiblackboard");
		setClientBlackboard("458eeb67-d7f5-4b1c-8558-e46648033de2");
		setSecretBlackboard("CgljMnP0Vz7XbtW0GUX0T6gZveZhVjYc");
		setUrlExternaBlackboard("https://univesp-test.blackboard.com");
	}

	public Map<String, Object> getParametroBody() {
		Map<String, Object> parametroBody = new HashMap<>();
		parametroBody.put("grant_type", "password");
		parametroBody.put("username", getUsernameSeiBlackboard());
		parametroBody.put("password", getSenhaSeiBlackboard());
		parametroBody.put("scope", "all");
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

	public String getUsernameSeiBlackboard() {
		if (usernameSeiBlackboard == null) {
			usernameSeiBlackboard = "sei.blackboard";
		}
		return usernameSeiBlackboard;
	}

	public void setUsernameSeiBlackboard(String usernameSeiBlackboard) {
		this.usernameSeiBlackboard = usernameSeiBlackboard;
	}

	public String getSenhaSeiBlackboard() {
		if (senhaSeiBlackboard == null) {
			senhaSeiBlackboard = "0t1m1z3bl4ckb04rd";
		}
		return senhaSeiBlackboard;
	}

	public void setSenhaSeiBlackboard(String senhaSeiBlackboard) {
		this.senhaSeiBlackboard = senhaSeiBlackboard;
	}

	public String getClienteSeiBlackboard() {
		if (clienteSeiBlackboard == null) {
			clienteSeiBlackboard = "client-Blackboard";
		}
		return clienteSeiBlackboard;
	}

	public void setClienteSeiBlackboard(String clienteSeiBlackboard) {
		this.clienteSeiBlackboard = clienteSeiBlackboard;
	}

	public String getTokenSeiBlackboard() {
		if (tokenSeiBlackboard == null) {
			tokenSeiBlackboard = "blackboard-password-oauth";
		}
		return tokenSeiBlackboard;
	}

	public void setTokenSeiBlackboard(String tokenSeiBlackboard) {
		this.tokenSeiBlackboard = tokenSeiBlackboard;
	}

	public String getUrlExternaSeiBlackboard() {
		if (urlExternaSeiBlackboard == null) {
			urlExternaSeiBlackboard = Constantes.EMPTY;
		}
		return urlExternaSeiBlackboard;
	}

	public void setUrlExternaSeiBlackboard(String urlExternaSeiBlackboard) {
		this.urlExternaSeiBlackboard = urlExternaSeiBlackboard;
	}

	public String getUrlExternaBlackboard() {
		if (urlExternaBlackboard == null) {
			urlExternaBlackboard = Constantes.EMPTY;
		}
		return urlExternaBlackboard;
	}

	public void setUrlExternaBlackboard(String urlExternaBlackboard) {
		this.urlExternaBlackboard = urlExternaBlackboard;
	}

	public String getClientBlackboard() {
		if (clientBlackboard == null) {
			clientBlackboard = Constantes.EMPTY;
		}
		return clientBlackboard;
	}

	public void setClientBlackboard(String clientBlackboard) {
		this.clientBlackboard = clientBlackboard;
	}

	public String getSecretBlackboard() {
		if (secretBlackboard == null) {
			secretBlackboard = Constantes.EMPTY;
		}
		return secretBlackboard;
	}

	public void setSecretBlackboard(String secretBlackboard) {
		this.secretBlackboard = secretBlackboard;
	}

	public String getIdBlackboard() {
		if (secretBlackboard == null) {
			secretBlackboard = Constantes.EMPTY;
		}
		return idBlackboard;
	}

	public void setIdBlackboard(String idBlackboard) {
		this.idBlackboard = idBlackboard;
	}

	public boolean isAtivarOperacoesDoBlackboard() {
		return ativarOperacoesDoBlackboard;
	}

	public void setAtivarOperacoesDoBlackboard(boolean ativarOperacoesDoBlackboard) {
		this.ativarOperacoesDoBlackboard = ativarOperacoesDoBlackboard;
	}

	public List<ConfiguracaoSeiBlackboardDominioVO> getListaConfiguracaoSeiBlackboardDominioVO() {
		if (listaConfiguracaoSeiBlackboardDominioVO == null) {
			listaConfiguracaoSeiBlackboardDominioVO = new ArrayList<>();
		}
		return listaConfiguracaoSeiBlackboardDominioVO;
	}

	public void setListaConfiguracaoSeiBlackboardDominioVO(List<ConfiguracaoSeiBlackboardDominioVO> listaConfiguracaoSeiBlackboardDominioVO) {
		this.listaConfiguracaoSeiBlackboardDominioVO = listaConfiguracaoSeiBlackboardDominioVO;
	}

	public Boolean getImportacaoEmRealizacao() {
		if(importacaoEmRealizacao == null) {
			importacaoEmRealizacao =  false;
		}
		return importacaoEmRealizacao;
	}

	public void setImportacaoEmRealizacao(Boolean importacaoEmRealizacao) {
		this.importacaoEmRealizacao = importacaoEmRealizacao;
	}

	public String getFonteDeDadosConteudoMasterBlackboard() {
		if (fonteDeDadosConteudoMasterBlackboard == null) {
			fonteDeDadosConteudoMasterBlackboard = Constantes.EMPTY;
		}
		return fonteDeDadosConteudoMasterBlackboard;
	}

	public void setFonteDeDadosConteudoMasterBlackboard(String fonteDeDadosConteudoMasterBlackboard) {
		this.fonteDeDadosConteudoMasterBlackboard = fonteDeDadosConteudoMasterBlackboard;
	}

	public boolean isAtivarOperacaoEnsalamentoEstagio() {
		return ativarOperacaoEnsalamentoEstagio;
	}

	public void setAtivarOperacaoEnsalamentoEstagio(boolean ativarEnsalamentoEstagio) {
		this.ativarOperacaoEnsalamentoEstagio = ativarEnsalamentoEstagio;
	}

	public boolean isAtivarOperacaoEnsalamentoTCC() {
		return ativarOperacaoEnsalamentoTCC;
	}

	public void setAtivarOperacaoEnsalamentoTCC(boolean ativarEnsalamentoTCC) {
		this.ativarOperacaoEnsalamentoTCC = ativarEnsalamentoTCC;
	}
	
	
	
	

	

}
