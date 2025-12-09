package negocio.interfaces.ead;

import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.ItemParametrosMonitoramentoAvaliacaoOnlineVO;
import negocio.comuns.ead.ParametrosMonitoramentoAvaliacaoOnlineVO;

/**
 * 
 * @author Victor Hugo de Paula Costa 23/03/2015
 *
 */
public interface ItemParametrosMonitoramentoAvaliacaoOnlineInterfaceFacade {

	void incluir(ItemParametrosMonitoramentoAvaliacaoOnlineVO itemParametrosMonitoramentoAvaliacaoOnlineVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void persistir(ItemParametrosMonitoramentoAvaliacaoOnlineVO itemParametrosMonitoramentoAvaliacaoOnlineVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void alterar(ItemParametrosMonitoramentoAvaliacaoOnlineVO itemParametrosMonitoramentoAvaliacaoOnlineVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void excluir(ItemParametrosMonitoramentoAvaliacaoOnlineVO itemParametrosMonitoramentoAvaliacaoOnlineVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	ItemParametrosMonitoramentoAvaliacaoOnlineVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	List<ItemParametrosMonitoramentoAvaliacaoOnlineVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	void persistirItensParametrosMonitoramentoAvaliacaoOnlineVOs(ParametrosMonitoramentoAvaliacaoOnlineVO parametrosMonitoramentoAvaliacaoOnlineVO, UsuarioVO usuarioVO) throws Exception;

	void adicionarItemParametros(ParametrosMonitoramentoAvaliacaoOnlineVO parametrosMonitoramentoAvaliacaoOnlineVO, ItemParametrosMonitoramentoAvaliacaoOnlineVO itemParametrosMonitoramentoAvaliacaoOnlineVO, UsuarioVO usuarioVO) throws Exception;

	void removerItemParametros(ItemParametrosMonitoramentoAvaliacaoOnlineVO itemParametrosMonitoramentoAvaliacaoOnlineVO, ParametrosMonitoramentoAvaliacaoOnlineVO parametrosMonitoramentoAvaliacaoOnlineVO, UsuarioVO usuarioVO) throws Exception;

	List<ItemParametrosMonitoramentoAvaliacaoOnlineVO> consultarItemParametrosMonitoramentoAvalaicaoOnlinePorTurmaAnoSemestre(Integer codigoTurma, String ano, String semestre, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;
	
	List<ItemParametrosMonitoramentoAvaliacaoOnlineVO> consultarPorCodigoParametrosMonitoramentoAvaliacaOnline(Integer codigoParametros, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

}
