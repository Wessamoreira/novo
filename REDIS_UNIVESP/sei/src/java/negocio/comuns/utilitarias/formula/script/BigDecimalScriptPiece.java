package negocio.comuns.utilitarias.formula.script;

import java.util.Map;

import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.utilitarias.formula.chain.ChainLinkTemplate;
import negocio.comuns.utilitarias.formula.chain.annotations.ScriptPiece;
import negocio.comuns.utilitarias.formula.common.StringFormula;

@ScriptPiece
public class BigDecimalScriptPiece extends ChainLinkTemplate{

	private static final long serialVersionUID = -1607220825316722082L;

	@Override
	public StringFormula execute(StringFormula script) {
		return script.append("var BigDecimal = Java.type('java.math.BigDecimal');");
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
