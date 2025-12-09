package negocio.comuns.utilitarias.formula.preprocessors;

import java.util.Map;

import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.utilitarias.formula.chain.ChainLinkTemplate;
import negocio.comuns.utilitarias.formula.chain.annotations.PreProcessor;
import negocio.comuns.utilitarias.formula.common.StringFormula;

@PreProcessor
class AfastamentoPreprocessor extends ChainLinkTemplate {

	private static final long serialVersionUID = -6019534954697452329L;

	@Override
	public StringFormula execute(StringFormula script) {
		if (script.toString().contains("mapa.AFASTAMENTO")) {
			script.replaceAll("mapa.AFASTAMENTO", "funcoes.executaSQL('AFASTAME', mapa.VIGENCIA, contexto.getCodigo(), "
					+ " UteisData.getMesData(mapa.VIGENCIA), UteisData.getAnoData(mapa.VIGENCIA), UteisData.getMesData(mapa.VIGENCIA),"
					+ " UteisData.getAnoData(mapa.VIGENCIA))");
		}

		if (script.toString().contains("mapa.SITUACAO_AFASTAMENTO")) {
			script.replaceAll("mapa.SITUACAO_AFASTAMENTO", "funcoes.executaSQL('AFASTAME', mapa.VIGENCIA, contexto.getCodigo(), mapa.VIGENCIA, mapa.VIGENCIA)[0].tipoafastamento;");
		}

		if (script.toString().contains("mapa.QUANTIDADE_DIAS_AFASTAMENTO")) {
			script.replaceAll("mapa.QUANTIDADE_DIAS_AFASTAMENTO", "calculo.quantidadeDiasAfastado(contexto, mapa.VIGENCIA)");
		}

		if (script.toString().contains("afastamento(")) {
			script.replaceAll("afastamento\\s?\\(", "calculo.quantidadeDiasAfastadoPorMotivo(contexto, mapa.VIGENCIA, ");
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
