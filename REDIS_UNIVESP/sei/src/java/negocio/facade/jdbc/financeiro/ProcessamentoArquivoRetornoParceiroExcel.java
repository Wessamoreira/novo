package negocio.facade.jdbc.financeiro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ProcessamentoArquivoRetornoParceiroExcelVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoContaReceber;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.ead.GestaoEventoConteudoTurma;
import negocio.interfaces.financeiro.ProcessamentoArquivoRetornoParceiroExcelInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class ProcessamentoArquivoRetornoParceiroExcel extends ControleAcesso implements ProcessamentoArquivoRetornoParceiroExcelInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1651644315008774444L;
	protected static String idEntidade;

	public ProcessamentoArquivoRetornoParceiroExcel() throws Exception {
		super();
		setIdEntidade("ProcessamentoArquivoRetornoParceiroExcel");
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(List<ProcessamentoArquivoRetornoParceiroExcelVO> lista, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		for (ProcessamentoArquivoRetornoParceiroExcelVO obj : lista) {
			persistir(obj, verificarAcesso, usuarioVO);	
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void persistir(ProcessamentoArquivoRetornoParceiroExcelVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		if (obj.getCodigo() == 0) {
			incluir(obj, verificarAcesso, usuarioVO);
		} else {
			alterar(obj, verificarAcesso, usuarioVO);
		}

	}
	

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final ProcessamentoArquivoRetornoParceiroExcelVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try {
			ProcessamentoArquivoRetornoParceiroExcel.incluir(getIdEntidade(), verificarAcesso, usuario);
			GestaoEventoConteudoTurma.incluir(getIdEntidade(), verificarAcesso, usuario);
			final StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO ProcessamentoArquivoRetornoParceiroExcel (processamentoArquivoRetornoParceiroAluno, contaReceber, valorConta, dataCompetencia) ");
			sql.append("    VALUES ( ?, ?, ?, ?)");
			sql.append(" returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql.toString());
					int i = 0;
					sqlInserir.setInt(++i, obj.getProcessamentoArquivoRetornoParceiroAlunoVO().getCodigo());
					if(Uteis.isAtributoPreenchido(obj.getContaReceberVO().getCodigo())){
						sqlInserir.setInt(++i, obj.getContaReceberVO().getCodigo());	
					}else{
						sqlInserir.setNull(++i, 0);
					}
					if(Uteis.isAtributoPreenchido(obj.getValorConta())){
						sqlInserir.setDouble(++i, obj.getValorConta());	
					}else{
						sqlInserir.setNull(++i, 0);
					}
					if(Uteis.isAtributoPreenchido(obj.getDataCompetencia())){
						sqlInserir.setDate(++i, Uteis.getDataJDBC(obj.getDataCompetencia()));	
					}else{
						sqlInserir.setNull(++i, 0);
					}
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(final ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw e;
		}
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe
	 * <code>ProcessamentoArquivoRetornoParceiroVO</code>. Sempre utiliza a
	 * chave primária da classe como atributo para localização do registro a ser
	 * alterado. Primeiramente valida os dados (<code>validarDados</code>) do
	 * objeto. Verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade. Isto, através da operação
	 * <code>alterar</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe
	 *            <code>ProcessamentoArquivoRetornoParceiroVO</code> que será
	 *            alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final ProcessamentoArquivoRetornoParceiroExcelVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try {
			ProcessamentoArquivoRetornoParceiroExcel.alterar(getIdEntidade(), verificarAcesso, usuario);
			final StringBuilder sql = new StringBuilder();
			sql.append("UPDATE ProcessamentoArquivoRetornoParceiroExcel ");
			sql.append("   SET contaReceber=?, processamentoArquivoRetornoParceiroAluno=?, valorConta=?, ");
			sql.append("   dataCompetencia=? ");
			sql.append(" WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql.toString());
					int i = 0;
					if(Uteis.isAtributoPreenchido(obj.getContaReceberVO().getCodigo())){
						sqlAlterar.setInt(++i, obj.getContaReceberVO().getCodigo());	
					}else{
						sqlAlterar.setNull(++i, 0);
					}
					sqlAlterar.setInt(++i, obj.getProcessamentoArquivoRetornoParceiroAlunoVO().getCodigo());
					
					if(Uteis.isAtributoPreenchido(obj.getValorConta())){
						sqlAlterar.setDouble(++i, obj.getValorConta());	
					}else{
						sqlAlterar.setNull(++i, 0);
					}
					if(Uteis.isAtributoPreenchido(obj.getDataCompetencia())){
						sqlAlterar.setDate(++i, Uteis.getDataJDBC(obj.getDataCompetencia()));	
					}else{
						sqlAlterar.setNull(++i, 0);
					}
					sqlAlterar.setInt(++i, obj.getCodigo());
					return sqlAlterar;
				}
			}) == 0) {
				incluir(obj, false, usuario);
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void removerVinculoContaReceber(Integer matriculaPeriodo, SituacaoContaReceber situacaoContaReceber, UsuarioVO usuarioVO) throws Exception {
    	try {
    		getFacadeFactory().getProcessamentoArquivoRetornoParceiroAlunoFacade().atualizarProcessamentoArquivoRetornoParceiroAlunoPorContaReceber(matriculaPeriodo, situacaoContaReceber, usuarioVO);
    		StringBuilder sqlStr = new StringBuilder();
    		sqlStr.append("UPDATE processamentoarquivoretornoparceiroexcel set contaReceber = null    WHERE codigo in (");
    		sqlStr.append(" select codigo from processamentoarquivoretornoparceiroexcel  where contareceber in ( ");
    		sqlStr.append(" select codigo from contareceber where matriculaperiodo = ").append(matriculaPeriodo);
    		if(Uteis.isAtributoPreenchido(situacaoContaReceber)){
    			sqlStr.append(" and situacao = '").append(situacaoContaReceber.getValor()).append("' ");	
    		}
    		sqlStr.append(" and contareceber.tipoorigem in ('MAT', 'MEN', 'MDI','BCC' ))) ");
    		
    		sqlStr.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
    		getConexao().getJdbcTemplate().update(sqlStr.toString());
    	} catch (Exception e) {
    		throw e;
    	}
    }
	

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return ProcessamentoArquivoRetornoParceiroExcel.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		ProcessamentoArquivoRetornoParceiroExcel.idEntidade = idEntidade;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void removerVinculoContaReceberEspecifica(Integer contaReceber, UsuarioVO usuarioVO) throws Exception {
    	try {
    		getFacadeFactory().getProcessamentoArquivoRetornoParceiroAlunoFacade().atualizarProcessamentoArquivoRetornoParceiroAlunoPorContaReceberEspecifica(contaReceber, usuarioVO);
    		StringBuilder sqlStr = new StringBuilder();
    		sqlStr.append("UPDATE processamentoarquivoretornoparceiroexcel set contaReceber = null    WHERE contaReceber = ? ");
    		sqlStr.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
    		getConexao().getJdbcTemplate().update(sqlStr.toString(), contaReceber);
    	} catch (Exception e) {
    		throw e;
    	}
    }
	
}
