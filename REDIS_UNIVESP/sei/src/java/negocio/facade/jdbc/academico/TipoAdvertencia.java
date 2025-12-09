package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

import negocio.comuns.academico.TipoAdvertenciaVO;
import negocio.comuns.academico.enumeradores.SituacaoTipoAdvertenciaEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.TipoAdvertenciaInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class TipoAdvertencia extends ControleAcesso implements TipoAdvertenciaInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public TipoAdvertencia() throws Exception {
		super();
		setIdEntidade("TipoAdvertencia");
	}

	public TipoAdvertenciaVO novo() throws Exception {
		TipoAdvertencia.incluir(getIdEntidade());
		TipoAdvertenciaVO obj = new TipoAdvertenciaVO();
		return obj;
	}

	public static void validarDados(TipoAdvertenciaVO obj) throws Exception {
		if (obj.getDescricao().equals("")) {
			throw new Exception("O campo DESCRICAO (Tipo Advertência) deve ser informado.");
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(TipoAdvertenciaVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			if (obj.getNovoObj()) {
				incluir(obj, verificarAcesso, usuarioVO);
			} else {
				alterar(obj, verificarAcesso, usuarioVO);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final TipoAdvertenciaVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			validarDados(obj);
			TipoAdvertencia.incluir(getIdEntidade(), verificarAcesso, usuarioVO);
			final StringBuilder sql = new StringBuilder(); 
			sql.append("INSERT INTO TipoAdvertencia (descricao, situacao, descricaoadvertencia, gerarsuspensao,advertenciaverbal, visaopais, visaoaluno, enviaremail,datacadastro,responsavel) ");
			sql.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql.toString());
					try {
						sqlInserir.setString(1, obj.getDescricao());
						sqlInserir.setString(2, obj.getSituacao().name());
						sqlInserir.setString(3, obj.getDescricaoAdvertencia());
						sqlInserir.setBoolean(4, obj.getGerarSuspensao());
						sqlInserir.setBoolean(5, obj.getAdvertenciaVerbal());
						sqlInserir.setBoolean(6, obj.getVisaoPais());
						sqlInserir.setBoolean(7, obj.getVisaoAluno());
						sqlInserir.setBoolean(8, obj.getEnviarEmail());
						sqlInserir.setDate(9, Uteis.getSQLData (obj.getDataCadastro()));
						sqlInserir.setString(10, obj.getUsuarioVO().getNome());
					} catch (final Exception x) {
						return null;
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

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final TipoAdvertenciaVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			TipoAdvertencia.alterar(getIdEntidade(), verificarAcesso, usuarioVO);
			final StringBuilder sql = new StringBuilder();
			sql.append("UPDATE TipoAdvertencia set descricao = ?, situacao = ?, descricaoadvertencia=?, gerarsuspensao=?, advertenciaverbal=?, visaopais=?, visaoaluno=?, enviaremail=?,responsavel = ?");
			sql.append("WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql.toString());
					sqlAlterar.setString(1, obj.getDescricao());
					sqlAlterar.setString(2, obj.getSituacao().name());
					sqlAlterar.setString(3, obj.getDescricaoAdvertencia());
					sqlAlterar.setBoolean(4, obj.getGerarSuspensao());
					sqlAlterar.setBoolean(5, obj.getAdvertenciaVerbal());
					sqlAlterar.setBoolean(6, obj.getVisaoPais());
					sqlAlterar.setBoolean(7, obj.getVisaoAluno());
					sqlAlterar.setBoolean(8, obj.getEnviarEmail());
					sqlAlterar.setString(9, obj.getUsuarioVO().getNome());
					sqlAlterar.setInt (10, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(TipoAdvertenciaVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			TipoAdvertencia.excluir(getIdEntidade(), verificarAcesso, usuarioVO);
			String sql = "DELETE FROM TipoAdvertencia WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql, obj.getCodigo());
		} catch (Exception e) {
			throw e;
		}
	}
	
	public List<TipoAdvertenciaVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		List<TipoAdvertenciaVO> tipoAdvertenciaVOs = new ArrayList<TipoAdvertenciaVO>(0);
		while (tabelaResultado.next()) {
			tipoAdvertenciaVOs.add(montarDados(tabelaResultado, nivelMontarDados));
		}
		return tipoAdvertenciaVOs;
	}
	@Override
	public TipoAdvertenciaVO consultarPorCodigo(Integer codigo, SituacaoTipoAdvertenciaEnum situacao, Boolean controlarAcesso, UsuarioVO usuarioVO,int nivelMontarDados) throws Exception {
		StringBuilder sqlStr = new StringBuilder("SELECT * FROM TipoAdvertencia where codigo = ").append(codigo);
		if (situacao != null) {
			if (situacao.equals(SituacaoTipoAdvertenciaEnum.ATIVO)) {
				sqlStr.append(" AND situacao = 'ATIVO'");
			} else if (situacao.equals(SituacaoTipoAdvertenciaEnum.INATIVO)) {
				sqlStr.append(" AND situacao = 'INATIVO'");
			}
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if(rs.next()){
			return montarDados(rs, nivelMontarDados);
		}
		return new TipoAdvertenciaVO();
	}

	@Override
	public List<TipoAdvertenciaVO> consultarPorDescricao(String descricao, SituacaoTipoAdvertenciaEnum situacao, Boolean controlarAcesso, UsuarioVO usuarioVO, int nivelMontarDados) throws Exception {
		StringBuilder sqlStr = new StringBuilder("SELECT * FROM TipoAdvertencia where descricao like('").append(descricao).append("%') ");
		if (situacao != null) {
			if (situacao.equals(SituacaoTipoAdvertenciaEnum.ATIVO)) {
				sqlStr.append(" AND situacao = 'ATIVO'");
			} else if (situacao.equals(SituacaoTipoAdvertenciaEnum.INATIVO)) {
				sqlStr.append(" AND situacao = 'INATIVO'");
			}
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado,nivelMontarDados, usuarioVO);
	}
	
	public TipoAdvertenciaVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados) {
		TipoAdvertenciaVO obj = new TipoAdvertenciaVO();
			obj = new TipoAdvertenciaVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setDescricao(tabelaResultado.getString("descricao"));
			obj.setSituacao(SituacaoTipoAdvertenciaEnum.valueOf(tabelaResultado.getString("situacao")));
			obj.setDescricaoAdvertencia(tabelaResultado.getString("descricaoAdvertencia"));
			obj.setGerarSuspensao(tabelaResultado.getBoolean("gerarSuspensao"));
			obj.setAdvertenciaVerbal(tabelaResultado.getBoolean("advertenciaVerbal"));
			obj.setVisaoPais(tabelaResultado.getBoolean("visaoPais"));
			obj.setVisaoAluno(tabelaResultado.getBoolean("visaoAluno"));
			obj.setEnviarEmail(tabelaResultado.getBoolean("enviarEmail"));
			obj.setDataCadastro(tabelaResultado.getDate("datacadastro"));
			obj.getUsuarioVO().setNome(tabelaResultado.getString("responsavel"));
			obj.setNovoObj(false);
			if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
				return obj;
			}
		return obj;
	}
	
	@Override
	public TipoAdvertenciaVO consultarNotificacaoEmail(TipoAdvertenciaVO tipoAdvertenciaVO) {
		StringBuilder sqlStr = new StringBuilder("SELECT enviaremail, visaoaluno from tipoadvertencia where codigo = ").append(tipoAdvertenciaVO.getCodigo());
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		TipoAdvertenciaVO obj = new TipoAdvertenciaVO();
		if (rs.next()) {
			obj.setEnviarEmail(rs.getBoolean("enviarEmail"));
			obj.setVisaoAluno(rs.getBoolean("visaoaluno"));
		}
		return obj;
	}
	
	public static String getIdEntidade() {
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		TipoAdvertencia.idEntidade = idEntidade;
	}

}
