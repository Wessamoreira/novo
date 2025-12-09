package negocio.comuns.utilitarias.formula.chain;

import java.util.List;
import java.util.Map;

import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.utilitarias.formula.common.StringFormula;

public abstract class ChainRunnerTemplate {

	private ChainLink firstChain;

	public ChainRunnerTemplate(List<ChainLink> chainList) {

		this.firstChain = chainList.remove(0);
		ChainLink current = firstChain;

		for (ChainLink chainLink : chainList) {
			current.setNextLink(chainLink);
			current = chainLink;
		}
	}

	public String process(String script) {
		return this.firstChain.process(this.getStringFormula(script)).toString();
	}

	public String process(String script, FuncionarioCargoVO funcionarioCargo) {
		return this.firstChain.process(this.getStringFormula(script), funcionarioCargo).toString();
	}

	public String process(String script, FuncionarioCargoVO funcionarioCargo,  Map<String, Object> eventos) {
		return this.firstChain.process(this.getStringFormula(script), funcionarioCargo, eventos).toString();
	}
	
	protected abstract StringFormula getStringFormula(String script);
}
