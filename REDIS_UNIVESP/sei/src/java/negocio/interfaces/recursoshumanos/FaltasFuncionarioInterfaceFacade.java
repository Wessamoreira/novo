package negocio.interfaces.recursoshumanos;

import java.util.Date;
import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import controle.arquitetura.DataModelo;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.CompetenciaFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.EventoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.FaltasFuncionarioVO;
import negocio.facade.jdbc.recursoshumanos.SuperFacadeInterface;

public interface FaltasFuncionarioInterfaceFacade <T extends SuperVO> extends SuperFacadeInterface<T> {

	public void consultarPorEnumCampoConsulta(DataModelo dataModelo, String situacaoFuncionario) throws Exception;

	public void persistirTodos(List<FaltasFuncionarioVO> listaFaltasFuncionario, FuncionarioCargoVO funcionarioCargo, boolean validarAcesso, UsuarioVO usuarioLogado) throws Exception;

	public List<FaltasFuncionarioVO> consultarPorFuncionarioCargo(FuncionarioCargoVO funcionarioCargoVO, boolean validarAcesso, UsuarioVO usuarioLogado) throws Exception;

	public void excluirPorFuncionarioCargo(FuncionarioCargoVO obj, boolean verificarAcesso, UsuarioVO usuario);

	public void validarDados(Date dataInicio, FuncionarioCargoVO funcionarioCargoVO, FaltasFuncionarioVO faltasFuncionario) throws Exception;

	public List<FaltasFuncionarioVO> consultarTotalFaltasPeriodo() throws Exception ;

	public Integer consultarQtdFaltasDoPeriodo(FuncionarioCargoVO funcionarioCargo, Date inicioPeriodo, Date finalPeriodo);

	public void adicionarEventosDeFaltasFuncionario(CompetenciaFolhaPagamentoVO competenciaFolhaPagamentoAtiva, FuncionarioCargoVO funcionarioCargo, List<EventoFolhaPagamentoVO> listaDeEventosDoFuncionario) throws Exception;


	/**
	 * Caso o funcionario tenha 15 dias ou mais de faltas injustificadas, o mesmo perde o direito a 1 avo
	 * Retorna a qtd de meses que o mesmo teve mais que 15 dias
	 */
	public Integer consultarQtdPeriodoPerdidoFaltasDoFuncionarioCargoPorPeriodo(FuncionarioCargoVO funcionarioCargo, Date inicioPeriodo, Date finalPeriodo);
	
	
	public Integer consultarQtdFaltasDoPeriodo(FuncionarioCargoVO funcionarioCargo, Date inicioPeriodo, Date finalPeriodo, Boolean integral);

	public SqlRowSet consultarQuantidadeDeFaltasDoFuncionarioCargoPorPeriodo(FuncionarioCargoVO funcionarioCargo, Date inicioPeriodo, Date finalPeriodo) ;
	
}