package negocio.comuns.utilitarias.formula.preprocessors;

import java.util.HashMap;
import java.util.Map;

import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.utilitarias.formula.chain.ChainLinkTemplate;
import negocio.comuns.utilitarias.formula.chain.annotations.PreProcessor;
import negocio.comuns.utilitarias.formula.common.StringFormula;

@PreProcessor
public class EventoFixoPreProcessor extends ChainLinkTemplate {

	private static final long serialVersionUID = 1L;

	@Override
	public StringFormula execute(StringFormula script) {
		return script.replaceAll("EVT_FIXO\\s?\\(", "funcoes.executaSQL('EVT_FIXO', mapa.VIGENCIA, contexto.getMatriculaCargo(), ");
	}

	@Override
	public StringFormula execute(StringFormula script, FuncionarioCargoVO funcionario, Map<String, Object> eventos) {
		return script.replaceAll("EVT_FIXO\\s?\\(", "funcoes.executaSQL('EVT_FIXO', mapa.VIGENCIA, contexto.getMatriculaCargo(), ");
	}

	@Override
	public StringFormula execute(StringFormula script, FuncionarioCargoVO funcionario) {
		return this.execute(script, funcionario, new HashMap<String, Object>());
	}

}
