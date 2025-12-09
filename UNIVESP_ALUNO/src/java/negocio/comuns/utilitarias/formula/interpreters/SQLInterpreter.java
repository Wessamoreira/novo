package negocio.comuns.utilitarias.formula.interpreters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.JdbcTemplate;

import negocio.comuns.utilitarias.formula.FormulaException;

public class SQLInterpreter {
	
	private JdbcTemplate jdbcTemplate;
	private static final List<String> termosProibidos = Arrays.asList("update", "delete", "create", "drop");
	private static final Pattern PATTERN = Pattern.compile(termosProibidos.stream().map(p -> p.trim()).collect(Collectors.joining("|", "\\b(", "*)\\b")));
	
	public SQLInterpreter(JdbcTemplate jdbcTemplate) {
		super();
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<Map<String, Object>> consultarPorLista(String sql, Object... parametros) {
		String upperCaseSql = sql.toUpperCase();
		this.validarSQL(upperCaseSql);
		return this.jdbcTemplate.queryForList(upperCaseSql, parametros);
	}
	
	public List<Map<String, Object>> consultarPorValor(String sql, Object... parametros) {
		String upperCaseSql = sql.toUpperCase();
		this.validarSQL(upperCaseSql);
		return this.jdbcTemplate.queryForList(upperCaseSql, parametros);
	}

	public void executaSql(String sql) {
		this.jdbcTemplate.execute(sql);
	}

	
	private void validarSQL(String sql) throws FormulaException{
		List<String> maches = this.getMaches(sql);
		if(!maches.isEmpty()) {
			throw new FormulaException(String.format("O sql :%s%nContem os termos proibidos a seguir:%n%s", sql, maches));
		};
	}
	
	private List<String> getMaches(String sql){
		List<String> maches = new ArrayList<>();
		Matcher matcher = SQLInterpreter.PATTERN.matcher(sql);
		while(matcher.find()) {
			maches.add(matcher.group());
		}
		return maches;
	}

}
