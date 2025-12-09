package negocio.facade.jdbc.protocolo;

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
import negocio.comuns.protocolo.TipoRequerimentoDepartamentoFuncionarioVO;
import negocio.comuns.protocolo.TipoRequerimentoDepartamentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.protocolo.TipoRequerimentoDepartamentoFuncionarioInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class TipoRequerimentoDepartamentoFuncionario extends ControleAcesso implements TipoRequerimentoDepartamentoFuncionarioInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7448666060646105229L;

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final TipoRequerimentoDepartamentoFuncionarioVO tipoRequerimentoDepartamentoFuncionarioVO, UsuarioVO usuarioVO) throws Exception {
		validarDados(tipoRequerimentoDepartamentoFuncionarioVO);
		final String sql = "INSERT INTO TipoRequerimentoDepartamentoFuncionario( TipoRequerimentoDepartamento, Funcionario, unidadeEnsino ) VALUES ( ?, ?, ?) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
		tipoRequerimentoDepartamentoFuncionarioVO.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlInserir = arg0.prepareStatement(sql);				
				sqlInserir.setInt(1, tipoRequerimentoDepartamentoFuncionarioVO.getTipoRequerimentoDepartamentoVO().getCodigo());
				sqlInserir.setInt(2, tipoRequerimentoDepartamentoFuncionarioVO.getFuncionarioVO().getCodigo());				
				sqlInserir.setInt(3, tipoRequerimentoDepartamentoFuncionarioVO.getUnidadeEnsinoVO().getCodigo());				
				return sqlInserir;
			}
		}, new ResultSetExtractor<Integer>() {
			public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
				if (arg0.next()) {
					tipoRequerimentoDepartamentoFuncionarioVO.setNovoObj(Boolean.FALSE);
					return arg0.getInt("codigo");
				}
				return null;
			}
		}));

	}
		
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final TipoRequerimentoDepartamentoFuncionarioVO tipoRequerimentoDepartamentoFuncionarioVO, UsuarioVO usuarioVO) throws Exception {
		validarDados(tipoRequerimentoDepartamentoFuncionarioVO);
		final String sql = "UPDATE TipoRequerimentoDepartamentoFuncionario set TipoRequerimentoDepartamento = ?, Funcionario = ?, unidadeEnsino = ? where  codigo = ? " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
		if(getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement update = arg0.prepareStatement(sql);				
				update.setInt(1, tipoRequerimentoDepartamentoFuncionarioVO.getTipoRequerimentoDepartamentoVO().getCodigo());
				update.setInt(2, tipoRequerimentoDepartamentoFuncionarioVO.getFuncionarioVO().getCodigo());				
				update.setInt(3, tipoRequerimentoDepartamentoFuncionarioVO.getUnidadeEnsinoVO().getCodigo());				
				update.setInt(4, tipoRequerimentoDepartamentoFuncionarioVO.getCodigo());				
				return update;
			}
		}) == 0) {
			incluir(tipoRequerimentoDepartamentoFuncionarioVO, usuarioVO);
		}

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(TipoRequerimentoDepartamentoFuncionarioVO tipoRequerimentoDepartamentoFuncionarioVO, UsuarioVO usuarioVO) throws Exception {
		getConexao().getJdbcTemplate().update("DELETE FROM TipoRequerimentoDepartamentoFuncionario WHERE codigo = ? "+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO), tipoRequerimentoDepartamentoFuncionarioVO.getCodigo());
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirListaTipoRequerimentoDepartamentoFuncionarioVO(TipoRequerimentoDepartamentoVO tipoRequerimentoDepartamentoVO, UsuarioVO usuarioVO) throws Exception {
		for (Iterator<TipoRequerimentoDepartamentoFuncionarioVO> iterator = tipoRequerimentoDepartamentoVO.getTipoRequerimentoDepartamentoFuncionarioVOs().iterator(); iterator.hasNext();) {
			TipoRequerimentoDepartamentoFuncionarioVO tipoRequerimentoDepartamentoFuncionarioVO = iterator.next();
			tipoRequerimentoDepartamentoFuncionarioVO.setTipoRequerimentoDepartamentoVO(tipoRequerimentoDepartamentoVO);
			incluir(tipoRequerimentoDepartamentoFuncionarioVO, usuarioVO);			
		}

	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarListaTipoRequerimentoDepartamentoFuncionarioVO(TipoRequerimentoDepartamentoVO tipoRequerimentoDepartamentoVO, UsuarioVO usuarioVO) throws Exception {
		excluirListaTipoRequerimentoDepartamentoFuncionarioVO(tipoRequerimentoDepartamentoVO, usuarioVO);
		for (Iterator<TipoRequerimentoDepartamentoFuncionarioVO> iterator = tipoRequerimentoDepartamentoVO.getTipoRequerimentoDepartamentoFuncionarioVOs().iterator(); iterator.hasNext();) {
			TipoRequerimentoDepartamentoFuncionarioVO tipoRequerimentoDepartamentoFuncionarioVO = iterator.next();
			tipoRequerimentoDepartamentoFuncionarioVO.setTipoRequerimentoDepartamentoVO(tipoRequerimentoDepartamentoVO);
			if(tipoRequerimentoDepartamentoFuncionarioVO.isNovoObj()) {
				incluir(tipoRequerimentoDepartamentoFuncionarioVO, usuarioVO);
			}else {
				alterar(tipoRequerimentoDepartamentoFuncionarioVO, usuarioVO);
			}
		}

	}
	
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirListaTipoRequerimentoDepartamentoFuncionarioVO(TipoRequerimentoDepartamentoVO tipoRequerimentoDepartamentoVO, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql  =  new StringBuilder("DELETE FROM TipoRequerimentoDepartamentoFuncionario WHERE TipoRequerimentoDepartamento = ? and codigo not in (0 ");
		for (Iterator<TipoRequerimentoDepartamentoFuncionarioVO> iterator = tipoRequerimentoDepartamentoVO.getTipoRequerimentoDepartamentoFuncionarioVOs().iterator(); iterator.hasNext();) {
			sql.append(", ").append(iterator.next().getCodigo());
		}
		sql.append(") ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString(), tipoRequerimentoDepartamentoVO.getCodigo());
	}

	@Override	
	public List<TipoRequerimentoDepartamentoFuncionarioVO> consultarPorTipoRequerimentoDepartamento(Integer tipoRequerimentoDepartamento, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql  =  new StringBuilder("SELECT *, funcionario.matricula as \"funcionario.matricula\", pessoa.nome as \"pessoa.nome\", unidadeensino.nome as \"unidadeensino.nome\" FROM TipoRequerimentoDepartamentoFuncionario ");
		sql.append(" inner join funcionario on funcionario.codigo =  TipoRequerimentoDepartamentoFuncionario.funcionario ");
		sql.append(" inner join pessoa on pessoa.codigo =  funcionario.pessoa ");
		sql.append(" inner join unidadeensino on unidadeensino.codigo =  TipoRequerimentoDepartamentoFuncionario.unidadeensino ");
		sql.append(" WHERE TipoRequerimentoDepartamento = ? order by unidadeensino.nome, pessoa.nome ");
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), tipoRequerimentoDepartamento), usuarioVO);
	}
	
	public List<TipoRequerimentoDepartamentoFuncionarioVO> montarDadosConsulta(SqlRowSet rs, UsuarioVO usuarioVO) throws Exception{
		List<TipoRequerimentoDepartamentoFuncionarioVO> tipoRequerimentoDepartamentoFuncionarioVOs = new ArrayList<TipoRequerimentoDepartamentoFuncionarioVO>(0);
		while(rs.next()) {
			tipoRequerimentoDepartamentoFuncionarioVOs.add(montarDados(rs, usuarioVO));
		}
		return tipoRequerimentoDepartamentoFuncionarioVOs;
	}
	
	public TipoRequerimentoDepartamentoFuncionarioVO montarDados(SqlRowSet rs, UsuarioVO usuarioVO) throws Exception{
		TipoRequerimentoDepartamentoFuncionarioVO tipoRequerimentoDepartamentoFuncionarioVO = new TipoRequerimentoDepartamentoFuncionarioVO();
		tipoRequerimentoDepartamentoFuncionarioVO.setCodigo(rs.getInt("codigo"));
		tipoRequerimentoDepartamentoFuncionarioVO.getTipoRequerimentoDepartamentoVO().setCodigo(rs.getInt("TipoRequerimentoDepartamento"));
		tipoRequerimentoDepartamentoFuncionarioVO.getFuncionarioVO().setCodigo(rs.getInt("funcionario"));
		tipoRequerimentoDepartamentoFuncionarioVO.getUnidadeEnsinoVO().setCodigo(rs.getInt("unidadeensino"));
		tipoRequerimentoDepartamentoFuncionarioVO.getUnidadeEnsinoVO().setNome(rs.getString("unidadeensino.nome"));
		tipoRequerimentoDepartamentoFuncionarioVO.getFuncionarioVO().getPessoa().setNome(rs.getString("pessoa.nome"));
		tipoRequerimentoDepartamentoFuncionarioVO.getFuncionarioVO().setMatricula(rs.getString("funcionario.matricula"));
		tipoRequerimentoDepartamentoFuncionarioVO.setNovoObj(false);
		return tipoRequerimentoDepartamentoFuncionarioVO;
	}

	@Override	
	public void validarDados(TipoRequerimentoDepartamentoFuncionarioVO tipoRequerimentoDepartamentoFuncionarioVO) throws ConsistirException {
		if(!Uteis.isAtributoPreenchido(tipoRequerimentoDepartamentoFuncionarioVO.getFuncionarioVO())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_TipoRequerimentoDepartamentoFuncionario_funcionario"));
		}
	}

}
