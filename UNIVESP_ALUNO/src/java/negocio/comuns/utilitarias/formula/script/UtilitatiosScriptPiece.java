package negocio.comuns.utilitarias.formula.script;

import java.util.Map;

import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.utilitarias.formula.chain.ChainLinkTemplate;
import negocio.comuns.utilitarias.formula.chain.annotations.ScriptPiece;
import negocio.comuns.utilitarias.formula.common.StringFormula;

@ScriptPiece
public class UtilitatiosScriptPiece extends ChainLinkTemplate{

	private static final long serialVersionUID = -1607220825316722082L;

	@Override
	public StringFormula execute(StringFormula script) {
		script.append("var calculo = Java.type('negocio.comuns.utilitarias.UteisCalculoFolhaPagamento');");
		script.append("var uteis = Java.type('negocio.comuns.utilitarias.Uteis');");
		return script.append(" var UteisData = Java.type('negocio.comuns.utilitarias.faturamento.nfe.UteisData');");
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
