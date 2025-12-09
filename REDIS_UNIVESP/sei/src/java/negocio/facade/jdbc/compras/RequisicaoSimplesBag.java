package negocio.facade.jdbc.compras;

import java.util.Map;

import org.springframework.stereotype.Component;

import controle.compras.bag.BagFacade;

@Component
public class RequisicaoSimplesBag extends BagFacade {

	private static final long serialVersionUID = -5333701863059545307L;

	public String consulta(Map<String, Object> parametros) {
		return String.format("select codigo, username from usuario");
	}

}
