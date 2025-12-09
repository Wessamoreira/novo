package negocio.comuns.utilitarias.formula.chain;

import java.util.Map;

import controle.arquitetura.SuperControle;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.utilitarias.formula.common.StringFormula;

public abstract class ChainLinkTemplate extends SuperControle implements ChainLink {

	private static final long serialVersionUID = -1819287570916356020L;

	private ChainLink nextLink;
	
	@Override
	public void setNextLink(ChainLink link) {
		this.nextLink = link;
	}

	@Override
	public StringFormula process(StringFormula script) {
		return this.nextLink != null ? this.nextLink.process(this.execute(script)) : this.execute(script);
	}

	@Override
	public StringFormula process(StringFormula script, FuncionarioCargoVO funcionarioCargo) {
		return this.nextLink != null ? this.nextLink.process(this.execute(script, funcionarioCargo)) : this.execute(script, funcionarioCargo);
	}

	@Override
	public StringFormula process(StringFormula script, FuncionarioCargoVO funcionarioCargo, Map<String, Object> eventos) {
		return this.nextLink != null ? this.nextLink.process(this.execute(script, funcionarioCargo, eventos)) : this.execute(script, funcionarioCargo, eventos);
	}

	public abstract StringFormula execute(StringFormula script);
	
	public abstract StringFormula execute(StringFormula script, FuncionarioCargoVO funcionario);
}
