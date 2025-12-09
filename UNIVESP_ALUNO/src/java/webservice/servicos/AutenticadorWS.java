package webservice.servicos;

import java.util.StringTokenizer;


import java.util.Base64;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;

/**
 * @author Victor Hugo de Paula Costa - 3 de out de 2016
 *
 */
public class AutenticadorWS extends ControleAcesso {
	/**
	 * @author Victor Hugo de Paula Costa - 3 de out de 2016
	 * @throws Exception 
	 */
	private static final String AUTHENTICATION_SCHEME = "Basic";
	
	public UsuarioVO autenticar(String authCredentials) throws Exception {
		if (null == authCredentials)
			return null;
		// header value format will be "Basic encodedstring" for Basic
		// authentication. Example "Basic YWRtaW46YWRtaW4="
		final String encodedUserPassword;
		String usernameAndPassword;
		if (authCredentials.contains("SHA-256")) {
			encodedUserPassword = authCredentials.replaceFirst("SHA-256" + " ", "");
		} else {
			encodedUserPassword = authCredentials.replaceFirst(AUTHENTICATION_SCHEME + " ", "");
		}
		usernameAndPassword = new String(Base64.getDecoder().decode(encodedUserPassword));

		final StringTokenizer tokenizer = new StringTokenizer(
				usernameAndPassword, ":");
		final String username = tokenizer.nextToken();
		final String password = tokenizer.nextToken();
		String naoEncryptar = "";
		if (authCredentials.contains("SHA-256")) {
			naoEncryptar = "true";
		} else if (tokenizer.hasMoreTokens()) {
			naoEncryptar = tokenizer.nextToken();
		}
		return realizarAutenticacaoUsuario(Uteis.removerCaracteresEspeciais3(username).trim(), Uteis.removerCaracteresEspeciais3(password).trim(), naoEncryptar);
	}
	
	private UsuarioVO realizarAutenticacaoUsuario(final String username, final String password, final String naoEncryptar) {
		UsuarioVO usuarioVO = null;
		try {
			if (naoEncryptar != "") {
				usuarioVO = getFacadeFactory().getControleAcessoFacade().verificarLoginUsuario(username, password, false, Uteis.NIVELMONTARDADOS_DADOSLOGIN);
			} else {
				usuarioVO = getFacadeFactory().getControleAcessoFacade().verificarLoginUsuario(username, password, true, Uteis.NIVELMONTARDADOS_DADOSLOGIN);
			}
		} catch (Exception e) {
			return usuarioVO;
		}
		return usuarioVO;
	}
}
