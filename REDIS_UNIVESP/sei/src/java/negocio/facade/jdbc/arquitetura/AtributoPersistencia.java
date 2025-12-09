package negocio.facade.jdbc.arquitetura;

import java.util.HashMap;

public class AtributoPersistencia extends HashMap<String, Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2567787881966358024L;

	public AtributoPersistencia() {
		super();				
	}
	
	public AtributoPersistencia add(String key, Object value) {
		this.put(key, value);
		return this;
	}

}
