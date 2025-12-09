package negocio.comuns.utilitarias.formula.preprocessors;

import java.util.HashMap;
import java.util.Map;

import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.utilitarias.formula.chain.ChainLinkTemplate;
import negocio.comuns.utilitarias.formula.chain.annotations.PreProcessor;
import negocio.comuns.utilitarias.formula.common.StringFormula;

@PreProcessor
public class MapasPreProcessor extends ChainLinkTemplate {

	private static final long serialVersionUID = 1L;

	@Override
	public StringFormula execute(StringFormula script) {
		script.replaceAll("mesAtual", "UteisData.getMesDataAtual");
		return script.replaceAll("mapa.30_JUNHO", "UteisData.getData30JunhoAnoAtual()");
	}

	@Override
	public StringFormula execute(StringFormula script, FuncionarioCargoVO funcionario, Map<String, Object> eventos) {
		script.replaceAll("mesAtual", "UteisData.getMesDataAtual");
		return script.replaceAll("mapa.30_JUNHO", "UteisData.getData30JunhoAnoAtual()");
	}

	@Override
	public StringFormula execute(StringFormula script, FuncionarioCargoVO funcionario) {
		return this.execute(script, funcionario, new HashMap<String, Object>());
	}

}
