package negocio.comuns.utilitarias.formula.logger;

import java.util.List;

import negocio.comuns.utilitarias.formula.chain.ChainLink;
import negocio.comuns.utilitarias.formula.chain.ChainRunnerTemplate;
import negocio.comuns.utilitarias.formula.common.StringFormula;

public class LoggerRunner extends ChainRunnerTemplate {

	public LoggerRunner(List<ChainLink> chainList) {
		super(chainList);
	}

	@Override
	protected StringFormula getStringFormula(String script) {
		return new StringFormula(script, "\n");
	}
}
