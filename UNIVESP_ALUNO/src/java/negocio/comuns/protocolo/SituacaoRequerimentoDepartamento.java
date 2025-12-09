package negocio.comuns.protocolo;

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

import controle.arquitetura.LoginControle;
import jakarta.faces.context.FacesContext;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.protocolo.SituacaoRequerimentoDepartamentoInterfaceFacade;

@Scope("singleton")
@Repository
@Lazy
public class SituacaoRequerimentoDepartamento extends ControleAcesso implements SituacaoRequerimentoDepartamentoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7485900485609746255L;

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final SituacaoRequerimentoDepartamentoVO situacaoRequerimentoDepartamentoVO,final  UsuarioVO usuarioVO) throws Exception {
		validarDados(situacaoRequerimentoDepartamentoVO);
		validarUnicidade(situacaoRequerimentoDepartamentoVO);
		situacaoRequerimentoDepartamentoVO.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {			
			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				String sql = "insert into SituacaoRequerimentoDepartamento (situacao) values (?) returning codigo "+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
				PreparedStatement ps = arg0.prepareStatement(sql);
				ps.setString(1, situacaoRequerimentoDepartamentoVO.getSituacao());
				return ps;
			}
		}, new ResultSetExtractor<Integer>() {
			@Override
			public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {				
				return arg0.next()?arg0.getInt("codigo"):0;
			}
		}));
		situacaoRequerimentoDepartamentoVO.setNovoObj(false);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final SituacaoRequerimentoDepartamentoVO situacaoRequerimentoDepartamentoVO,final  UsuarioVO usuarioVO) throws Exception {
		validarDados(situacaoRequerimentoDepartamentoVO);
		validarUnicidade(situacaoRequerimentoDepartamentoVO);
		if(getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {			
			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				String sql = "update SituacaoRequerimentoDepartamento set situacao = ? where codigo = ? "+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
				PreparedStatement ps = arg0.prepareStatement(sql);
				ps.setString(1, situacaoRequerimentoDepartamentoVO.getSituacao());
				ps.setInt(2, situacaoRequerimentoDepartamentoVO.getCodigo());
				return ps;
			}
		})==0) {
			incluir(situacaoRequerimentoDepartamentoVO, usuarioVO);
		};
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(SituacaoRequerimentoDepartamentoVO situacaoRequerimentoDepartamentoVO, UsuarioVO usuarioVO) throws Exception {
		getConexao().getJdbcTemplate().update("DELETE FROM SituacaoRequerimentoDepartamento where codigo = ? "+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO), situacaoRequerimentoDepartamentoVO.getCodigo());
	}

	@Override
	public SituacaoRequerimentoDepartamentoVO consultarPorChavePrimaria(Integer codigo, UsuarioVO usuarioVO) throws Exception {		
		SqlRowSet rs= getConexao().getJdbcTemplate().queryForRowSet("select * from SituacaoRequerimentoDepartamento where codigo = ? ", codigo);
		if(rs.next()) {
			return montarDados(rs);			
		}
		throw new Exception("Dados não encontrados(Situação Requerimento Departamento).");
	}

	@Override
	public void validarDados(SituacaoRequerimentoDepartamentoVO situacaoRequerimentoDepartamentoVO) throws ConsistirException {
		if(situacaoRequerimentoDepartamentoVO.getSituacao().trim().isEmpty()) {
			throw new ConsistirException("O campo SITUAÇÃO deve ser informado.");
		}
	}

	@Override
	public void validarUnicidade(SituacaoRequerimentoDepartamentoVO situacaoRequerimentoDepartamentoVO) throws ConsistirException {
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet("select codigo from SituacaoRequerimentoDepartamento where trim(sem_acentos(situacao)) ilike trim(sem_acentos((?))) and codigo != ?", situacaoRequerimentoDepartamentoVO.getSituacao(), situacaoRequerimentoDepartamentoVO.getCodigo());
		if(rs.next()) {
			throw new ConsistirException("Já existe cadastrado uma SITUAÇÃO REQUERIMENTO DEPARTAMENTO com esta SITUAÇÃO.");
		}
	}

	@Override
	public List<SituacaoRequerimentoDepartamentoVO> consultarPorSituacao(String situacao, UsuarioVO usuarioVO) throws Exception {
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet("select * from SituacaoRequerimentoDepartamento where trim(sem_acentos(situacao)) ilike trim(sem_acentos((?))) order by situacao ", situacao+"%");
		return montarDadosConsulta(rs);
	}

	@Override
	public List<SituacaoRequerimentoDepartamentoVO> montarDadosConsulta(SqlRowSet rs) throws Exception {
		List<SituacaoRequerimentoDepartamentoVO> situacaoRequerimentoDepartamentoVOs = new ArrayList<SituacaoRequerimentoDepartamentoVO>(0);
		while(rs.next()) {
			situacaoRequerimentoDepartamentoVOs.add(montarDados(rs));
		}
		return situacaoRequerimentoDepartamentoVOs;
	}

	@Override
	public SituacaoRequerimentoDepartamentoVO montarDados(SqlRowSet rs) throws Exception {
		SituacaoRequerimentoDepartamentoVO situacaoRequerimentoDepartamentoVO = new SituacaoRequerimentoDepartamentoVO();
		situacaoRequerimentoDepartamentoVO.setNovoObj(false);
		situacaoRequerimentoDepartamentoVO.setCodigo(rs.getInt("codigo"));
		situacaoRequerimentoDepartamentoVO.setSituacao(rs.getString("situacao"));
		return situacaoRequerimentoDepartamentoVO;
	}

	@Override
	public Boolean verificarPermissaoUsuarioFuncionalidade(String funcionalidade, UsuarioVO usuario) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void verificarPermissaoUsuarioFuncionalidade(UsuarioVO usuario, String funcionalidade) throws Exception {
		// TODO Auto-generated method stub
		
	}

	
}
