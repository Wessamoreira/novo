package webservice;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.SecurityContext;

/**
 * 
 * @author Alessandro
 */
public class Autenticacao {

	/**
	 * Método responsável por validar se origem possui segurança (https) e se
	 * possui ip autorizado.
	 * 
	 * @param request600
	 * @param security
	 * @throws Exception
	 */
	public static void validarOrigem(final HttpServletRequest request,
			final SecurityContext security) throws Exception {
		if (!security.isSecure()) {
			throw new Exception("Acesso Não Seguro, Utilize https");
		}
		if (!request.getRemoteAddr().equals("127.0.0.1")
				&& !request.getRemoteAddr().equals("0:0:0:0:0:0:0:1")) {
			throw new Exception("Acesso Negado (" + request.getRemoteAddr()
					+ ")");
		}
	}

}
