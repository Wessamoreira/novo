package negocio.comuns.utilitarias.formula;

import java.util.Map;

public interface Formula{
	
	Object getLog();
	String getNome();
	Object execute();
	Object execute(Object context);
	Object execute(Map<String, Object> map);
	Object execute(Object context, Map<String, Object> map);
	Object execute(Object context, Map<String, Object> map, Map<String, Object> eventos);

}