package negocio.interfaces.recursoshumanos;

import java.util.List;

import javax.script.ScriptEngine;

import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.FormulaFolhaPagamentoVO;
import negocio.facade.jdbc.recursoshumanos.CalculoContraCheque;

public interface FormulaFolhaPagamentoInterfaceFacade {

	public void persistir(FormulaFolhaPagamentoVO incidencia, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;

	public void excluir(FormulaFolhaPagamentoVO incidencia, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;

	public List<FormulaFolhaPagamentoVO> consultarPorFiltro(String campoConsulta, String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, String situacao) throws Exception;

	public FormulaFolhaPagamentoVO consultarPorChavePrimaria(Integer codigo) throws Exception;

	public Object executarFormula(FormulaFolhaPagamentoVO formula, FuncionarioCargoVO funcionarioCargo, UsuarioVO usuario, ScriptEngine engine);
	
	public Object executarFormula(String formula, FuncionarioCargoVO funcionario, CalculoContraCheque calculoContraCheque, ScriptEngine engine);
	
	public Object executarFormulaSemLog(String formula, FuncionarioCargoVO funcionarioCargo, ScriptEngine engine);
	
	public Object executarFormulaSemLog(String formula, FuncionarioCargoVO funcionario, CalculoContraCheque calculoContraCheque, ScriptEngine engine);

	public void inativar(FormulaFolhaPagamentoVO obj, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public String formatarLog(String string);

	public FormulaFolhaPagamentoVO consultarFormulaPorIdentificador(String identificador) throws Exception;

	public CalculoContraCheque montarEventosCalculoContraCheque(FuncionarioCargoVO funcionarioCargo, UsuarioVO usuario, ScriptEngine engine);

	public ScriptEngine inicializaEngineFormula();
}