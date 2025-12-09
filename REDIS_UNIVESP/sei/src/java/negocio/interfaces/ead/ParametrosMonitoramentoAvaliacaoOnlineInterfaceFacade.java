package negocio.interfaces.ead;

import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.ParametrosMonitoramentoAvaliacaoOnlineVO;

/**
 * 
 * @author Victor Hugo de Paula Costa 23/03/2015
 *
 */
public interface ParametrosMonitoramentoAvaliacaoOnlineInterfaceFacade {

	void incluir(ParametrosMonitoramentoAvaliacaoOnlineVO parametrosMonitoramentoAvaliacaoOnlineVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void alterar(ParametrosMonitoramentoAvaliacaoOnlineVO parametrosMonitoramentoAvaliacaoOnlineVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void persistir(ParametrosMonitoramentoAvaliacaoOnlineVO parametrosMonitoramentoAvaliacaoOnlineVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void excluir(ParametrosMonitoramentoAvaliacaoOnlineVO parametrosMonitoramentoAvaliacaoOnlineVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	ParametrosMonitoramentoAvaliacaoOnlineVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	List<ParametrosMonitoramentoAvaliacaoOnlineVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	List<ParametrosMonitoramentoAvaliacaoOnlineVO> consultar(String valorConsulta, String campoConsulta, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	List<ParametrosMonitoramentoAvaliacaoOnlineVO> consultarPorDescricao(String valorConsulta, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	ParametrosMonitoramentoAvaliacaoOnlineVO consultarPorChavePrimaria(Integer codigoParametros, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	List<ParametrosMonitoramentoAvaliacaoOnlineVO> consultarTodosParametros(int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;
}
