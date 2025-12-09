package negocio.interfaces.arquitetura;


public interface Filter<T> {

	boolean accept(T t);
}
