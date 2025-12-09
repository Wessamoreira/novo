package negocio.comuns.utilitarias.formula;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import negocio.comuns.utilitarias.formula.chain.ChainLink;
import negocio.comuns.utilitarias.formula.chain.annotations.Logger;
import negocio.comuns.utilitarias.formula.chain.annotations.PreProcessor;
import negocio.comuns.utilitarias.formula.chain.annotations.ScriptPiece;
import negocio.comuns.utilitarias.formula.interpreters.SQLInterpreter;
import negocio.comuns.utilitarias.formula.logger.LoggerRunner;
import negocio.comuns.utilitarias.formula.preprocessors.PreprocessorRunner;
import negocio.comuns.utilitarias.formula.script.ScriptBuilderChain;

@Configuration
public class FormulaConfig {

	@Autowired
	private  JdbcTemplate jdbcTemplate;
	
	@Autowired
	private List<ChainLink> chainList;
	
	private List<ChainLink> preProcessors = new ArrayList<ChainLink>();
	private List<ChainLink> scriptPieces = new ArrayList<ChainLink>();
	private List<ChainLink> loggers = new ArrayList<ChainLink>();

	@PostConstruct
	public void init() {
		preProcessors = chainList.stream().filter(p -> p.getClass().isAnnotationPresent(PreProcessor.class)).collect(Collectors.toList());
		scriptPieces = chainList.stream().filter(p -> p.getClass().isAnnotationPresent(ScriptPiece.class)).collect(Collectors.toList());
		loggers = chainList.stream().filter(p -> p.getClass().isAnnotationPresent(Logger.class)).collect(Collectors.toList());
	}

	@Bean
	public ScriptBuilderChain getScriptBuilderChain() {
		return new ScriptBuilderChain(this.scriptPieces);
	}
	
	@Bean
	public PreprocessorRunner getPreprocessorRunner() {
		return new PreprocessorRunner(this.preProcessors);
	}
	
	@Bean
	public LoggerRunner getLoggerRunner() {
		return new LoggerRunner(this.loggers);
	}
	
	@Bean
	public SQLInterpreter getSQLInterpreter() {
		return new SQLInterpreter(jdbcTemplate);
	}
	
}