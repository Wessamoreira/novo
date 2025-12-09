package negocio.comuns.utilitarias.formula.preprocessors;

import java.util.Map;

import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.utilitarias.formula.chain.ChainLinkTemplate;
import negocio.comuns.utilitarias.formula.chain.annotations.PreProcessor;
import negocio.comuns.utilitarias.formula.common.StringFormula;

@PreProcessor
class ReturnPreprocessor extends ChainLinkTemplate {

	private static final long serialVersionUID = 5086733996757981225L;

	@Override
	public StringFormula execute(StringFormula script) {
		return script.replaceAll("retorna ", "return ");
	}

	@Override
	public StringFormula execute(StringFormula script, FuncionarioCargoVO funcionario) {
		return this.execute(script);
	}

	@Override
	public StringFormula execute(StringFormula script, FuncionarioCargoVO funcionario, Map<String, Object> eventos) {
		return this.execute(script);
	}

}
