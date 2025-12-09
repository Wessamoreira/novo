package negocio.comuns.utilitarias.formula.chain;

import java.util.Map;

import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.utilitarias.formula.common.StringFormula;

public interface ChainLink {

	void setNextLink(ChainLink nextChainLink);

	StringFormula process(StringFormula script);

	StringFormula process(StringFormula script, FuncionarioCargoVO funcionarioCargo);

	StringFormula process(StringFormula script, FuncionarioCargoVO funcionarioCargo, Map<String, Object> eventos);

	StringFormula execute(StringFormula script, FuncionarioCargoVO funcionario, Map<String, Object> eventos);

}
