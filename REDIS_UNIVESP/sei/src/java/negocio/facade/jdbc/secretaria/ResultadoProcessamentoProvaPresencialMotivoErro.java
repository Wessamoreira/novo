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
import negocio.comuns.processosel.ResultadoProcessamentoProvaPresencialMotivoErroVO;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.secretaria.ResultadoProcessamentoProvaPresencialMotivoErroInterfaceFacade;

/**
 * @author Carlos Eugênio
 *
 */
@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class ResultadoProcessamentoProvaPresencialMotivoErro extends ControleAcesso implements ResultadoProcessamentoProvaPresencialMotivoErroInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public ResultadoProcessamentoProvaPresencialMotivoErro() {
		super();
		setIdEntidade("MatriculaProvaPresencial");
	}

	public void validarDados(ResultadoProcessamentoProvaPresencialMotivoErroVO obj) throws Exception {
		if (obj.getMatriculaVO() == null || obj.getMatriculaVO().getMatricula().equals("")) {
			throw new Exception("O campo MATRÍCULA (Resultado Processamento Prova Presencial Motivo Erro) deve ser informado.");
		}
		if (obj.getDisciplinaVO().getCodigo().equals(0)) {
			throw new Exception("O campo DISCIPLINA (Resultado Processamento Prova Presencial Motivo Erro) deve ser informado.");
		}
		if (obj.getMensagemErro().equals("")) {
			throw new Exception("O campo MENSAGEM ERRO (Resultado Processamento Prova Presencial Motivo Erro) deve ser informado.");
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final ResultadoProcessamentoProvaPresencialMotivoErroVO obj, UsuarioVO usuario) throws Exception {
		try {
			validarDados(obj);
			final String sql = "INSERT INTO ResultadoProcessamentoProvaPresencialMotivoErro( resultadoProcessamentoArquivoRespostaProvaPresencial, matricula, disciplina, mensagemErro ) VALUES ( ?, ?, ?, ? ) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					
					if (obj.getResultadoProcessamentoArquivoRespostaProvaPresencialVO().getCodigo().intValue() != 0) {
						sqlInserir.setInt(1, obj.getResultadoProcessamentoArquivoRespostaProvaPresencialVO().getCodigo().intValue());
					} else {
						sqlInserir.setNull(1, 0);
					}
					sqlInserir.setString(2, obj.getMatriculaVO().getMatricula());
					if (obj.getDisciplinaVO().getCodigo().intValue() != 0) {
						sqlInserir.setInt(3, obj.getDisciplinaVO().getCodigo().intValue());
					} else {
						sqlInserir.setNull(3, 0);
					}
					sqlInserir.setString(4, obj.getMensagemErro());
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
	public void alterar(final ResultadoProcessamentoProvaPresencialMotivoErroVO obj, UsuarioVO usuario) throws Exception {
		try {
			validarDados(obj);
			final String sql = "UPDATE ResultadoProcessamentoProvaPresencialMotivoErro set resultadoProcessamentoArquivoRespostaProvaPresencial=?, matricula=?, disciplina=?, mensagemErro WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					
					if (obj.getResultadoProcessamentoArquivoRespostaProvaPresencialVO().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(1, obj.getResultadoProcessamentoArquivoRespostaProvaPresencialVO().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(1, 0);
					}
					sqlAlterar.setString(2, obj.getMatriculaVO().getMatricula());
					if (obj.getDisciplinaVO().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(3, obj.getDisciplinaVO().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(3, 0);
					}
					sqlAlterar.setString(4, obj.getMensagemErro());
					sqlAlterar.setInt(5, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(ResultadoProcessamentoProvaPresencialMotivoErroVO obj, UsuarioVO usuario) throws Exception {
		String sql = "DELETE FROM ResultadoProcessamentoProvaPresencialMotivoErro WHERE (codigo = ?)" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
	}

	public static List montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
		List vetResultado = new ArrayList(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, usuario));
		}
		return vetResultado;
	}

	public static ResultadoProcessamentoProvaPresencialMotivoErroVO montarDados(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
		ResultadoProcessamentoProvaPresencialMotivoErroVO obj = new ResultadoProcessamentoProvaPresencialMotivoErroVO();
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.getResultadoProcessamentoArquivoRespostaProvaPresencialVO().setCodigo(dadosSQL.getInt("resultadoProcessamentoArquivoRespostaProvaPresencial.codigo"));
		obj.getMatriculaVO().setMatricula(dadosSQL.getString("matricula"));
		obj.getDisciplinaVO().setCodigo(dadosSQL.getInt("disciplina.codigo"));
		obj.setMensagemErro(dadosSQL.getString("mensagemErro"));
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
	public void excluirResultadoErroPorResultadoProcessamentoProvaPresencial(Integer resultadoProcessamento, UsuarioVO usuario) throws Exception {
		String sql = "DELETE FROM ResultadoProcessamentoProvaPresencialMotivoErro WHERE (resultadoProcessamentoArquivoRespostaProvaPresencial = ?)" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { resultadoProcessamento });
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirResultadoProcessamentoErro(Integer resultadoProcessamentoProvaPresencial, List<ResultadoProcessamentoProvaPresencialMotivoErroVO> objetos, UsuarioVO usuario) throws Exception {
		Iterator e = objetos.iterator();
		while (e.hasNext()) {
			ResultadoProcessamentoProvaPresencialMotivoErroVO obj = (ResultadoProcessamentoProvaPresencialMotivoErroVO) e.next();
			obj.getResultadoProcessamentoArquivoRespostaProvaPresencialVO().setCodigo(resultadoProcessamentoProvaPresencial);
			incluir(obj, usuario);
		}
	}
	
	public List<ResultadoProcessamentoProvaPresencialMotivoErroVO> consultarPorResultadoProcessamentoProvaPresencial(Integer resultadoProcessamentoArquivo, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select ResultadoProcessamentoProvaPresencialMotivoErro.codigo, ResultadoProcessamentoProvaPresencialMotivoErro.mensagemErro, ");
		sb.append(" resultadoProcessamentoArquivoRespostaProvaPresencial.codigo AS \"resultadoProcessamentoArquivoRespostaProvaPresencial.codigo\",  ");
		sb.append(" matricula.matricula, disciplina.codigo AS \"disciplina.codigo\", disciplina.nome AS \"disciplina.nome\" ");
		sb.append(" from ResultadoProcessamentoProvaPresencialMotivoErro ");
		sb.append(" inner join resultadoProcessamentoArquivoRespostaProvaPresencial on resultadoProcessamentoArquivoRespostaProvaPresencial.codigo = ResultadoProcessamentoProvaPresencialMotivoErro.resultadoProcessamentoArquivoRespostaProvaPresencial ");
		sb.append(" inner join matricula on matricula.matricula = ResultadoProcessamentoProvaPresencialMotivoErro.matricula ");
		sb.append(" inner join disciplina on disciplina.codigo = ResultadoProcessamentoProvaPresencialMotivoErro.disciplina ");
		sb.append(" where resultadoProcessamentoArquivoRespostaProvaPresencial.codigo = ").append(resultadoProcessamentoArquivo);
		sb.append(" order by disciplina.nome");
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<ResultadoProcessamentoProvaPresencialMotivoErroVO> listaMatriculaProvaPresencialDisciplinaVOs = new ArrayList<ResultadoProcessamentoProvaPresencialMotivoErroVO>(0);
		while (tabelaResultado.next()) {
			ResultadoProcessamentoProvaPresencialMotivoErroVO obj = new ResultadoProcessamentoProvaPresencialMotivoErroVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.getResultadoProcessamentoArquivoRespostaProvaPresencialVO().setCodigo(tabelaResultado.getInt("resultadoProcessamentoArquivoRespostaProvaPresencial.codigo"));
			
			obj.getDisciplinaVO().setCodigo(tabelaResultado.getInt("disciplina.codigo"));
			obj.getDisciplinaVO().setNome(tabelaResultado.getString("disciplina.nome"));
			obj.getMatriculaVO().setMatricula(tabelaResultado.getString("matricula"));
			obj.setMensagemErro(tabelaResultado.getString("mensagemErro"));

			listaMatriculaProvaPresencialDisciplinaVOs.add(obj);
			
		}
		return listaMatriculaProvaPresencialDisciplinaVOs;
	}

}
