package negocio.facade.jdbc.crm;

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
import negocio.comuns.segmentacao.SegmentacaoOpcaoVO;
import negocio.comuns.segmentacao.SegmentacaoProspectVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.crm.SegmentacaoOpcaotInterfaceFacade;

@Repository
@Scope(value = "singleton")
@Lazy
public class SegmentacaoOpcao extends ControleAcesso implements SegmentacaoOpcaotInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static String idEntidade;

	@Override
	public void validarDadosSegmentacaoOpcao(SegmentacaoOpcaoVO segmentacaoOpcaao) throws ConsistirException {
		if (segmentacaoOpcaao.getDescricao().equals("")) {
			throw new ConsistirException("O Campo descrição (Segmentação Opção é necessário) !");
		}
	}

	@Override
	public void removerSegmentacaoOpcaoVOs(SegmentacaoProspectVO segmentacaoProspectVO, SegmentacaoOpcaoVO segmentacaoOpcao) {
		for (Iterator<SegmentacaoOpcaoVO> iterator = segmentacaoProspectVO.getSegmentacaoOpcaoVOs().iterator(); iterator.hasNext();) {
			SegmentacaoOpcaoVO segmentacao = (SegmentacaoOpcaoVO) iterator.next();
			if (segmentacao.getDescricao().equals(segmentacaoOpcao.getDescricao())) {
				iterator.remove();
			}
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirSegmentacaoOpcoes(SegmentacaoProspectVO segmentacaoProspectVO, UsuarioVO usuario) throws Exception {
		Iterator<SegmentacaoOpcaoVO> e = segmentacaoProspectVO.getSegmentacaoOpcaoVOs().iterator();
		while (e.hasNext()) {
			SegmentacaoOpcaoVO obj = (SegmentacaoOpcaoVO) e.next();
			obj.setSegmentacaoprospect(segmentacaoProspectVO.getCodigo());
			incluir(obj, usuario);
		}
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final SegmentacaoOpcaoVO obj, UsuarioVO usuario) throws Exception {
		/**
		  * @author Leonardo Riciolle 
		  * Comentado 29/10/2014
		  *  Classe Subordinada
		  */ 
		// SegmentacaoOpcao.incluir(getIdEntidade());
		final String sql = "INSERT INTO segmentacaoopcao(segmentacaoprospect, descricao) VALUES (?, ?) returning codigo " +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				PreparedStatement sqlInserir = conn.prepareStatement(sql);
				sqlInserir.setInt(1, obj.getSegmentacaoprospect());
				sqlInserir.setString(2, obj.getDescricao());

				return sqlInserir;
			}
		}, new ResultSetExtractor() {

			public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					obj.setNovoObj(Boolean.FALSE);
					return rs.getInt("codigo");
				}
				return null;
			}
		}));
		obj.setNovoObj(Boolean.FALSE);
	}

	@Override
	public List consultarOpcoesSegmentacao(Integer segmentacao, int nivelMontarDados) throws Exception {
		SegmentacaoOpcao.consultar(getIdEntidade());
		List objetos = new ArrayList(0);
		String sql = "SELECT * FROM segmentacaoopcao WHERE segmentacaoprospect = ?";
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { segmentacao });
		while (resultado.next()) {
			SegmentacaoOpcaoVO novoObj = new SegmentacaoOpcaoVO();
			novoObj = montarDados(resultado, nivelMontarDados);
			objetos.add(novoObj);
		}
		return objetos;
	}

	@Override
	public SegmentacaoOpcaoVO consultarPorChavePrimaria(Integer segmentacaoOpcao) throws Exception {		
		String sql = "SELECT segmentacaoopcao.* FROM segmentacaoopcao WHERE codigo = ?";
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, segmentacaoOpcao);
		if (resultado.next()) {
			return montarDados(resultado, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS);
		}
		return new SegmentacaoOpcaoVO();
	}

	public static SegmentacaoOpcaoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados) throws Exception {
		SegmentacaoOpcaoVO obj = new SegmentacaoOpcaoVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.getSegmentacaoProspectVO().setCodigo(new Integer(dadosSQL.getInt("segmentacaoprospect")));
		obj.setDescricao(new String(dadosSQL.getString("descricao")));
		obj.setNovoObj(Boolean.FALSE);
		if(nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS) {
			obj.setSegmentacaoProspectVO(getFacadeFactory().getSegmentacaoProspectFacade().consultarPorChavePrimaria(obj.getSegmentacaoProspectVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, null));	
		}
		return obj;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarOpcoesSegmentacao(SegmentacaoProspectVO segmentoProspectVO, UsuarioVO usuario) throws Exception {
		excluirSegmentcaoOpcaoVOs(segmentoProspectVO, usuario);
		for (SegmentacaoOpcaoVO segmentoOpcaoVO : segmentoProspectVO.getSegmentacaoOpcaoVOs()) {
			segmentoOpcaoVO.setSegmentacaoprospect(segmentoProspectVO.getCodigo());
			try {
				if (segmentoOpcaoVO.isNovoObj()) {
					incluir(segmentoOpcaoVO, usuario);
				}else {
					alterarOpcaoSegmentacao(segmentoOpcaoVO, usuario);
				}

			} catch (Exception e) {
				throw e;
			} finally {

			}
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirSegmentcaoOpcaoVOs(SegmentacaoProspectVO segmentacaoProspectVO, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder(" delete from segmentacaoopcao where segmentacaoprospect = ").append(segmentacaoProspectVO.getCodigo());
		sql.append(" and codigo not in(0");

		for (SegmentacaoOpcaoVO segmentacaoOpcaoVO : segmentacaoProspectVO.getSegmentacaoOpcaoVOs()) {

			sql.append(",").append(segmentacaoOpcaoVO.getCodigo());
		}
		sql.append(" ) ");
		sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		try {

			getConexao().getJdbcTemplate().update(sql.toString());

		} catch (Exception e) {
			throw e;
		} finally {

		}

	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarOpcaoSegmentacao(final SegmentacaoOpcaoVO obj, UsuarioVO usuario) throws Exception {

		try {
			/**
			  * @author Leonardo Riciolle 
			  * Comentado 29/10/2014
			  *  Classe Subordinada
			  */ 
			// SegmentacaoProspect.alterar(getIdEntidade());
			final String sql = "UPDATE segmentacaoopcao SET descricao=? WHERE ((codigo = ? ));" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			if(getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
					PreparedStatement sqlAlterar = conn.prepareStatement(sql);
					sqlAlterar.setString(1, obj.getDescricao());
					sqlAlterar.setLong(2, obj.getCodigo().longValue());
					return sqlAlterar;
				}
			})==0) {
				incluir(obj, usuario);
				return;
			};

			obj.setNovoObj(Boolean.FALSE);

		} catch (Exception e) {
			obj.setNovoObj(true);
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirSegmentacaoOpcao(SegmentacaoOpcaoVO obj, UsuarioVO usuario) throws Exception {
		try {
			/**
			  * @author Leonardo Riciolle 
			  * Comentado 29/10/2014
			  *  Classe Subordinada
			  */ 	
			// SegmentacaoOpcao.excluir(getIdEntidade());
			String sql = "DELETE FROM segmentacaoopcao WHERE ((codigo = ?))" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		} finally {
		}
	}

	public static String getIdEntidade() {
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		SegmentacaoOpcao.idEntidade = idEntidade;
	}
}
