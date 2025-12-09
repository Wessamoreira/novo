package controle.compras.bag;

import java.util.List;
import java.util.Map;

import negocio.facade.jdbc.arquitetura.SuperFacadeJDBC;

public abstract class BagFacade extends SuperFacadeJDBC {

	private static final long serialVersionUID = 1L;

	public abstract String consulta(Map<String, Object> parametros);

	public List<Map<String, Object>> consultar(Map<String, Object> parametros, int limit, int offset) {
		if (limit < 10) {
			limit = 10;
		}

		String consulta = this.getConsulta(parametros);
		consulta = consulta + String.format(" limit %d offset %d", limit, offset);

		return getConexao().getJdbcTemplate().queryForList(consulta);
	}

	private String getConsulta(Map<String, Object> parametros) {
		return this.consulta(parametros).replaceAll(";", "");
	}
	
	public int consultaContador(Map<String, Object> parametros) {
		String consulta = String.format("	select  count(*) from (%s) as count;", this.getConsulta(parametros));
		return getConexao().getJdbcTemplate().queryForObject(consulta, Integer.class);
	}
	
	
	

}