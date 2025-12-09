package negocio.comuns.utilitarias.formula;

import javax.script.ScriptEngine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.utilitarias.formula.interpreters.ContextFunction;
import negocio.comuns.utilitarias.formula.logger.LoggerRunner;
import negocio.comuns.utilitarias.formula.preprocessors.PreprocessorRunner;
import negocio.comuns.utilitarias.formula.script.ScriptBuilderChain;

@Component
public class FormulaFactory {
	
	@Autowired
	private ContextFunction contextFunction;

	@Autowired
	private PreprocessorRunner preprocessorRunner;

	@Autowired
	private ScriptBuilderChain scriptBuilderChain;
	
	@Autowired
	private LoggerRunner loggerRunner;

	public Formula getInstance(String script, FuncionarioCargoVO funcionarioCargo, ScriptEngine engine) {
		String process = preprocessorRunner.process(script);
		String waved = loggerRunner.process(process, funcionarioCargo, null);
		String processedScript = scriptBuilderChain.process(waved, funcionarioCargo);
		return new FormulaImpl(processedScript, engine, contextFunction);
	}

	public Formula getInstanceSemLog(String script, ScriptEngine engine) { 
		String process = preprocessorRunner.process(script);
		String processedScript = scriptBuilderChain.process(process);
		return new FormulaImpl(processedScript, engine, contextFunction);
	}

}

