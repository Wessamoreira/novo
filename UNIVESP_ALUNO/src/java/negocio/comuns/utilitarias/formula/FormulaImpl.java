package negocio.comuns.utilitarias.formula;

import java.util.Map;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import negocio.comuns.utilitarias.formula.common.StringFormula;
import negocio.comuns.utilitarias.formula.interpreters.ContextFunction;

public class FormulaImpl implements Formula {

	private String nome;
	private final String script;
	private final ScriptEngine engine;
	private final ContextFunction contextFunction;
	private StringFormula logger;

	public FormulaImpl(String script, ScriptEngine engine, ContextFunction contextFunction) {
		super();
		this.engine = engine;
		this.script = script;
		this.contextFunction = contextFunction;
		this.eval(this.script);
	}

	private void eval(String script) {
		try {

			this.engine.eval(script);
		} catch (ScriptException e) {
			throw new FormulaException(e);
		}
	}

	@Override
	public String getNome() {
		return this.nome;
	}

	@Override
	public Object execute(Object context) {
		return this.execute(context, null);
	}

	@Override
	public Object execute() {
		return this.execute(null, null);
	}

	@Override
	public Object execute(Map<String, Object> map) {
		return this.execute(null, map);
	}

	@Override
	public Object execute(Object context, Map<String, Object> map) {
		this.logger = new StringFormula();
		try {
			return ((Invocable) engine).invokeFunction("main", context, map, this.contextFunction, this.logger);
		} catch (ScriptException e) {
			throw new FormulaException(e);
		} catch (NoSuchMethodException e) {
			throw new FormulaException(e);
		} catch (Exception e) {
			throw new FormulaException(e);
		}
	}

	@Override
	public Object execute(Object context, Map<String, Object> map, Map<String, Object> eventos) {
		this.logger = new StringFormula();
		try {
			return ((Invocable) engine).invokeFunction("main", context, map, this.contextFunction, this.logger, eventos);
		} catch (ScriptException e) {
			throw new FormulaException(e);
		} catch (NoSuchMethodException e) {
			throw new FormulaException(e);
		} catch (Exception e) {
			e.printStackTrace();
			throw new FormulaException(e);
		}
	}
	
	@Override
	public Object getLog() {
		return this.logger.toString();
	}

}