package negocio.comuns.utilitarias.formula.interpreters;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.FaixaValorVO;
import negocio.comuns.recursoshumanos.FormulaFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.ValorReferenciaFolhaPagamentoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.interfaces.recursoshumanos.FormulaFolhaPagamentoInterfaceFacade;
import negocio.interfaces.recursoshumanos.ValorReferenciaFolhaPagamentoInterfaceFacade;

@Component
public class ContextFunction {

	@Autowired
	private SQLInterpreter sqlInterpreter;
	
	@Autowired
	private ValorReferenciaFolhaPagamentoInterfaceFacade valorReferenciaFolhaPagamentoInterfaceFacade;

	@Autowired
	private FormulaFolhaPagamentoInterfaceFacade formulaFolhaPagamentoInterfaceFacade;
	
	public BigDecimal consultarValorFixo(String identificador, Date dataVigencia) throws Exception {
		return valorReferenciaFolhaPagamentoInterfaceFacade.consultarValorFixo(identificador, dataVigencia);
	}
	
	public BigDecimal consultarValorFixo(String identificador, Object dataVigencia) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy", java.util.Locale.US);
		Date date = sdf.parse(dataVigencia.toString());
		return consultarValorFixo(identificador, date);
	}
	
	public BigDecimal consultarValorFixo(ValorReferenciaFolhaPagamentoVO valorReferencia) throws Exception {
		return new BigDecimal(0);
	}

	public FaixaValorVO consultarFaixaValor(String identificador, Date dataVigencia, BigDecimal valor) {
		return valorReferenciaFolhaPagamentoInterfaceFacade.consultarFaixaValor(identificador, dataVigencia, valor);
	}
	
	public FaixaValorVO consultarFaixaValor(String identificador, Date dataVigencia, int valor) {
		return consultarFaixaValor(identificador, dataVigencia, new BigDecimal(valor));
	}
	
	public FaixaValorVO consultarFaixaValor(String identificador, Object dataVigencia, int valor) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy", java.util.Locale.US);
		Date date = sdf.parse(dataVigencia.toString());
		return consultarFaixaValor(identificador,date, new BigDecimal(valor));
	}

//	public FaixaValorVO consultarFaixaValor(String identificador, Object dataVigencia) throws ParseException {
//		SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy", java.util.Locale.US);
//		Date date = sdf.parse(dataVigencia.toString());
//		return consultarFaixaValor(identificador,date);
//	}

	public List<Map<String, Object>> executaSQL(String nomeSql, Date vigencia, Object... parametros) {
		String sql = valorReferenciaFolhaPagamentoInterfaceFacade.consultarSql(nomeSql, vigencia);
		return sqlInterpreter.consultarPorLista(sql, parametros);
	}

	/**
	 * Consulta a formula pelo identificador informado.
	 * 
	 * @param identificador
	 * @return
	 * @throws Exception
	 */
	public Object consultarFormulaPorIdentificador(String identificador, FuncionarioCargoVO funcionarioCargo, Object... parametros) throws Exception {
		FormulaFolhaPagamentoVO obj =  formulaFolhaPagamentoInterfaceFacade.consultarFormulaPorIdentificador(identificador);
		
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("nashorn");
		
		if (Uteis.isAtributoPreenchido(obj)) {
			if (obj.getFormula().contains("eventos")) {
				return formulaFolhaPagamentoInterfaceFacade.executarFormula(obj.getFormula(), funcionarioCargo, formulaFolhaPagamentoInterfaceFacade.montarEventosCalculoContraCheque(funcionarioCargo, new UsuarioVO(), engine), engine);
			} else {
				return formulaFolhaPagamentoInterfaceFacade.executarFormula(obj, funcionarioCargo, new UsuarioVO(), engine);
			}
		}

		return "Nenhuma fórmula encontrada!";
	}}