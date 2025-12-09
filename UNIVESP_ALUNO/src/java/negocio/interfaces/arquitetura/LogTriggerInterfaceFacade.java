package negocio.interfaces.arquitetura;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import jakarta.faces.model.SelectItem;

import org.springframework.jdbc.core.JdbcTemplate;

import negocio.comuns.arquitetura.CampoLogTriggerVO;
import negocio.comuns.arquitetura.QueryAtivaLogTriggerVO;
import negocio.comuns.arquitetura.RegistroLogTriggerVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.TriggerVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ProgressBarVO;

public interface LogTriggerInterfaceFacade {

	public <T extends SuperVO> void  realizarVisualizacaoSeiLog( T obj, String nomeTabela, String value, String key) throws SQLException ;
	
	public List<TriggerVO> consultarTabelaLog() throws Exception;
	
	public List<TriggerVO> consultarTriggers() throws Exception;
	
	void montarCampos(List<CampoLogTriggerVO> campos, final String tabela) throws Exception;

	void ativarTodasTriggersTabelas(List<TriggerVO> listaTriggers) throws Exception;

	void desativarTodasTriggersTabelas(List<TriggerVO> listaTriggers) throws Exception;

	void ativarTriggerTabela(TriggerVO trigger) throws Exception;

	void desativarTriggerTabela(TriggerVO trigger) throws Exception;

	List<RegistroLogTriggerVO> executarConsultaRegistrosPorDataUsuarioTabela(Date dataInicial, Date dataFinal, Integer usuario, String tabela, int limit, int offset) throws Exception;

	List<RegistroLogTriggerVO> executarConsultaRegistrosLogTrigger(String tabela, Date dataInicial, Date dataFinal, Integer usuario, List<CampoLogTriggerVO> filtros, int limit, int offset) throws Exception;

	List<CampoLogTriggerVO> executarConsultaCamposEventoLogTrigger(Long evento, String tabela, Date dataTransacao) throws Exception;

	List<RegistroLogTriggerVO> executarConsultaRegistrosMesmaTransacao(Long transacao, Date dataTransacao) throws Exception;

	List<SelectItem> consultarTabelas() throws Exception;

	Map<Integer, String> consultarUsuarios(JdbcTemplate template, StringBuilder sqlUser) throws Exception;

	List<SelectItem> consultarUsuarios() throws Exception;

	Integer executarConsultaTotalRegistroPorDataUsuarioTabela(Date dataInicial, Date dataFinal, Integer usuario, String tabela) throws Exception;

	Integer executarConsultaTotalRegistrosLogTrigger(String tabela, Date dataInicial, Date dataFinal, Integer usuario, List<CampoLogTriggerVO> filtros) throws Exception;

	/**
	 * @author Rodrigo Wind - 17/06/2015
	 * @param mes
	 * @param ano
	 * @throws Exception
	 */
	void realizarCriacaoTabelaLogPorMesAno(String mes, String ano, ProgressBarVO progressBarVO) throws Exception;

	/**
	 * @author Rodrigo Wind - 18/06/2015
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> realizarConsultaParametrizada(String sql, int limit, int offset) throws Exception;

	/**
	 * @author Rodrigo Wind - 18/06/2015
	 * @param registroLogTriggerVO
	 * @param usuarioVO
	 * @throws Exception
	 */
	void persistir(RegistroLogTriggerVO registroLogTriggerVO, UsuarioVO usuarioVO) throws Exception;

	/**
	 * @author Rodrigo Wind - 18/06/2015
	 * @return
	 */
	List<RegistroLogTriggerVO> consultarScripts();

	/**
	 * @author Rodrigo Wind - 18/06/2015
	 * @param registroLogTriggerVO
	 */
	void excluir(RegistroLogTriggerVO registroLogTriggerVO, UsuarioVO usuarioVO) throws Exception;

	/**
	 * @author Rodrigo Wind - 18/06/2015
	 * @param registroLogTriggerVO
	 * @param urlLogoPadraoRelatorio
	 * @param campoLogTriggerVOs
	 * @param colunas
	 * @param usuarioVO
	 * @return
	 * @throws Exception
	 */
	String realizarCriacaoArquivo(RegistroLogTriggerVO registroLogTriggerVO, String urlLogoPadraoRelatorio, List<List<CampoLogTriggerVO>> campoLogTriggerVOs, String[] colunas, UsuarioVO usuarioVO) throws Exception;

	String realizarCriacaoArquivoCsv(RegistroLogTriggerVO registroLogTriggerVO, String urlLogoPadraoRelatorio,
			List<List<CampoLogTriggerVO>> campoLogTriggerVOs, String[] colunas, UsuarioVO usuarioVO) throws Exception;
	
	List<QueryAtivaLogTriggerVO> executarConsultarQueryAtivaLogTrigger() throws Exception;
	void realizarCancelamentoQueryAtivaLogTrigger(QueryAtivaLogTriggerVO queryAtivaLogTriggerVO) throws Exception;
	
	
}
