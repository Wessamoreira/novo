package negocio.facade.jdbc.arquitetura;

import java.util.HashMap;

public class AtributoComparacao extends HashMap<String, Object> {

	private static final long serialVersionUID = -4003740938485308110L;
	
	public AtributoComparacao add(String key, Object value) {
		this.put(key, value);
		return this;
	}
}
