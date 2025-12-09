package negocio.comuns.utilitarias.formula.preprocessors;

import java.util.Map;

import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.utilitarias.formula.chain.ChainLinkTemplate;
import negocio.comuns.utilitarias.formula.chain.annotations.PreProcessor;
import negocio.comuns.utilitarias.formula.common.StringFormula;

@PreProcessor
class IfPreprocessor extends ChainLinkTemplate {

	private static final long serialVersionUID = -1713191688049080763L;

	@Override
	public StringFormula execute(StringFormula script) {
		return script.replaceAll("se\\s?\\(", "if(");
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
