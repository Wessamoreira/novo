package negocio.comuns.utilitarias.formula.pool;

import java.util.HashMap;
import java.util.Map;

public abstract class ObjectPool<T> {

	private Map<String, T> pool;

	ObjectPool() {
		this.pool = new HashMap<>();
	}

	public T get(String key) {
		return pool.get(key);
	}

	public void put(String key, T object) {
		this.pool.put(key, object);
	}

}