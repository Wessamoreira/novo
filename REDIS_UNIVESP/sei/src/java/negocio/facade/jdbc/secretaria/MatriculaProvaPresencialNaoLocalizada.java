/**
 * 
 */
package negocio.facade.jdbc.secretaria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.secretaria.MatriculaProvaPresencialNaoLocalizadaVO;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.secretaria.MatriculaProvaPresencialNaoLocalizadaInterfaceFacade;

/**
 * @author Carlos Eugênio
 *
 */
@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class MatriculaProvaPresencialNaoLocalizada extends ControleAcesso implements MatriculaProvaPresencialNaoLocalizadaInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public MatriculaProvaPresencialNaoLocalizada() {
		super();
		setIdEntidade("MatriculaProvaPresencialNaoLocalizada");
	}

	public void validarDados(MatriculaProvaPresencialNaoLocalizadaVO obj) throws Exception {
		if (obj.getMatriculaNaoLocalizada().equals("")) {
			throw new Exception("Não foi encontrada a matrícula do conjungo de matrículas não localizadas.");
		}
		if (obj.getRespostaGabarito().equals("")) {
			throw new Exception("Não foi encontrado a resposta do gabarito do conjunto de matrículas não localizadas");
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final MatriculaProvaPresencialNaoLocalizadaVO obj, UsuarioVO usuario) throws Exception {
		try {
			validarDados(obj);
			final String sql = "INSERT INTO MatriculaProvaPresencialNaoLocalizada( resultadoProcessamentoArquivoRespostaProvaPresencial, matriculaNaoLocalizada, respostaGabarito ) VALUES ( ?, ?, ? ) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					if (obj.getResultadoProcessamentoArquivoRespostaProvaPresencialVO().getCodigo().intValue() != 0) {
						sqlInserir.setInt(1, obj.getResultadoProcessamentoArquivoRespostaProvaPresencialVO().getCodigo().intValue());
					} else {
						sqlInserir.setNull(1, 0);
					}
					sqlInserir.setString(2, obj.getMatriculaNaoLocalizada());
					sqlInserir.setString(3, obj.getRespostaGabarito());
					return sqlInserir;
				}
			}, new ResultSetExtractor() {

				public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final MatriculaProvaPresencialNaoLocalizadaVO obj, UsuarioVO usuario) throws Exception {
		try {
			validarDados(obj);
			final String sql = "UPDATE MatriculaProvaPresencialNaoLocalizada set resultadoProcessamentoArquivoRespostaProvaPresencial=?, matriculaNaoLocalizada=? respostaGabarito=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					if (obj.getResultadoProcessamentoArquivoRespostaProvaPresencialVO().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(1, obj.getResultadoProcessamentoArquivoRespostaProvaPresencialVO().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(1, 0);
					}
					sqlAlterar.setString(2, obj.getMatriculaNaoLocalizada());
					sqlAlterar.setString(3, obj.getRespostaGabarito());
					sqlAlterar.setInt(4, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(MatriculaProvaPresencialNaoLocalizadaVO obj, UsuarioVO usuario) throws Exception {
		String sql = "DELETE FROM MatriculaProvaPresencialNaoLocalizada WHERE (codigo = ?)" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
	}

	public static List montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
		List vetResultado = new ArrayList(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, usuario));
		}
		return vetResultado;
	}

	public static MatriculaProvaPresencialNaoLocalizadaVO montarDados(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
		MatriculaProvaPresencialNaoLocalizadaVO obj = new MatriculaProvaPresencialNaoLocalizadaVO();
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setMatriculaNaoLocalizada(dadosSQL.getString("matriculaNaoLocalizada"));
		obj.setRespostaGabarito(dadosSQL.getString("respostaGabarito"));
		obj.setNovoObj(Boolean.FALSE);
		return obj;
	}

	public static String getIdEntidade() {
		return MatriculaProvaPresencial.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		MatriculaProvaPresencial.idEntidade = idEntidade;
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarMatriculaProvaPresencialNaoLocalizada(Integer resultadoProvaPresencial, List<MatriculaProvaPresencialNaoLocalizadaVO> objetos, UsuarioVO usuario) throws Exception {
		excluirMatriculaProvaPresencialNaoLocalizadaPorResultadoArquivo(resultadoProvaPresencial, usuario);
		incluirMatriculaProvaPresencialNaoLocalizada(resultadoProvaPresencial, objetos, usuario);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirMatriculaProvaPresencialNaoLocalizadaPorResultadoArquivo(Integer matriculaProvaPresencial, UsuarioVO usuario) throws Exception {
		String sql = "DELETE FROM MatriculaProvaPresencialNaoLocalizada WHERE (resultadoprocessamentoarquivorespostaprovapresencial = ?)" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { matriculaProvaPresencial });
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirMatriculaProvaPresencialNaoLocalizada(Integer resultadoProvaPresencial, List<MatriculaProvaPresencialNaoLocalizadaVO> objetos, UsuarioVO usuario) throws Exception {
		Iterator e = objetos.iterator();
		while (e.hasNext()) {
			MatriculaProvaPresencialNaoLocalizadaVO obj = (MatriculaProvaPresencialNaoLocalizadaVO) e.next();
			obj.getResultadoProcessamentoArquivoRespostaProvaPresencialVO().setCodigo(resultadoProvaPresencial);
			incluir(obj, usuario);
		}
	}
	
	public List<MatriculaProvaPresencialNaoLocalizadaVO> consultarPorResultadoProcessamentoArquivo(Integer resultadoProcessamentoArquivo, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select matriculaprovapresencialnaolocalizada.codigo, resultadoprocessamentoarquivorespostaprovapresencial,  ");
		sb.append(" matriculaprovapresencialnaolocalizada.matriculaNaoLocalizada, matriculaprovapresencialnaolocalizada.respostaGabarito ");
		sb.append(" from matriculaprovapresencialnaolocalizada ");
		sb.append(" where resultadoprocessamentoarquivorespostaprovapresencial = ").append(resultadoProcessamentoArquivo);
		sb.append(" order by matriculaNaoLocalizada ");
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<MatriculaProvaPresencialNaoLocalizadaVO> listaMatriculaProvaPresencialNaoLocalizadaVOs = new ArrayList<MatriculaProvaPresencialNaoLocalizadaVO>(0);
		while (tabelaResultado.next()) {
			MatriculaProvaPresencialNaoLocalizadaVO obj = new MatriculaProvaPresencialNaoLocalizadaVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.getResultadoProcessamentoArquivoRespostaProvaPresencialVO().setCodigo(tabelaResultado.getInt("resultadoprocessamentoarquivorespostaprovapresencial"));
			obj.setMatriculaNaoLocalizada(tabelaResultado.getString("matriculaNaoLocalizada"));
			obj.setRespostaGabarito(tabelaResultado.getString("respostaGabarito"));
			listaMatriculaProvaPresencialNaoLocalizadaVOs.add(obj);
			
		}
		return listaMatriculaProvaPresencialNaoLocalizadaVOs;
	}

}
