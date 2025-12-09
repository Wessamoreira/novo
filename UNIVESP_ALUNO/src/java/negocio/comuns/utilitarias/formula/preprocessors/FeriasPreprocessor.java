package negocio.comuns.utilitarias.formula.preprocessors;

import java.util.Map;

import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.utilitarias.formula.chain.ChainLinkTemplate;
import negocio.comuns.utilitarias.formula.chain.annotations.PreProcessor;
import negocio.comuns.utilitarias.formula.common.StringFormula;

@PreProcessor
public class FeriasPreprocessor extends ChainLinkTemplate {

	private static final long serialVersionUID = -7788082902629693137L;

	@Override
	public StringFormula execute(StringFormula script) {
		if (script.toString().contains("mapa.FERIAS_VENCIDAS")) {
			script.replaceAll("mapa.FERIAS_VENCIDAS", "funcoes.executaSQL('QTD_FERI', mapa.VIGENCIA, contexto.getCodigo())[0].count");
		}
		return script;
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