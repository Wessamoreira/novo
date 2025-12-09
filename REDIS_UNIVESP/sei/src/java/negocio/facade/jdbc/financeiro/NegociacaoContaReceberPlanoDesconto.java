package negocio.facade.jdbc.financeiro;

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

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.NegociacaoContaReceberPlanoDescontoVO;
import negocio.comuns.financeiro.NegociacaoContaReceberVO;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.NegociacaoContaReceberPlanoDescontoInterfaceFacade;

@Repository
@Lazy
@Scope("singleton")
public class NegociacaoContaReceberPlanoDesconto extends ControleAcesso
		implements NegociacaoContaReceberPlanoDescontoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2163876867950679137L;

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirNegociacaoContaReceberPlanoDesconto(NegociacaoContaReceberVO obj, UsuarioVO usuarioVO)
			throws Exception {
		for(NegociacaoContaReceberPlanoDescontoVO negociacaoContaReceberPlanoDescontoVO: obj.getNegociacaoContaReceberPlanoDescontoVOs()){
			negociacaoContaReceberPlanoDescontoVO.setNegociacaoContaReceberVO(obj);
			incluir(negociacaoContaReceberPlanoDescontoVO, usuarioVO);
		}

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final NegociacaoContaReceberPlanoDescontoVO obj, UsuarioVO usuarioVO) throws Exception {
		final StringBuilder sql =  new StringBuilder("insert into NegociacaoContaReceberPlanoDesconto (NegociacaoContaReceber, PlanoDesconto) values (?, ?) returning codigo");
		sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		obj.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement ps = arg0.prepareStatement(sql.toString());
				ps.setInt(1, obj.getNegociacaoContaReceberVO().getCodigo());
				ps.setInt(2, obj.getPlanoDescontoVO().getCodigo());
				return ps;
			}
		}, new ResultSetExtractor<Integer>() {

			@Override
			public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
				if(arg0.next()){
					return arg0.getInt("codigo");
				}
				return null;
			}
		}));
		obj.setNovoObj(false);
	}

	@Override
	public List<NegociacaoContaReceberPlanoDescontoVO> consultarPorNegociacaoContaReceber(
			Integer negociacaoContaReceber, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql  = new StringBuilder("select NegociacaoContaReceberPlanoDesconto.*, PlanoDesconto.nome, PlanoDesconto.tipoDescontoParcela, PlanoDesconto.percDescontoParcela ");
		sql.append(" from NegociacaoContaReceberPlanoDesconto ");
		sql.append(" inner join planodesconto on planodesconto.codigo = NegociacaoContaReceberPlanoDesconto.planodesconto "); 
		sql.append(" where negociacaoContaReceber = ? order by codigo ");		
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), negociacaoContaReceber));
	}
	
	private List<NegociacaoContaReceberPlanoDescontoVO> montarDadosConsulta(SqlRowSet rs) throws Exception{
		List<NegociacaoContaReceberPlanoDescontoVO> negociacaoContaReceberPlanoDescontoVOs = new ArrayList<NegociacaoContaReceberPlanoDescontoVO>(0);
		while(rs.next()){
			negociacaoContaReceberPlanoDescontoVOs.add(montarDados(rs));
		}
		return negociacaoContaReceberPlanoDescontoVOs;
	}
	
	private NegociacaoContaReceberPlanoDescontoVO montarDados(SqlRowSet rs) throws Exception{
		NegociacaoContaReceberPlanoDescontoVO negociacaoContaReceberPlanoDescontoVO = new NegociacaoContaReceberPlanoDescontoVO();
		negociacaoContaReceberPlanoDescontoVO.setNovoObj(false);
		negociacaoContaReceberPlanoDescontoVO.setCodigo(rs.getInt("codigo"));
		negociacaoContaReceberPlanoDescontoVO.getNegociacaoContaReceberVO().setCodigo(rs.getInt("negociacaoContaReceber"));
		negociacaoContaReceberPlanoDescontoVO.getPlanoDescontoVO().setCodigo(rs.getInt("planoDesconto"));
		negociacaoContaReceberPlanoDescontoVO.getPlanoDescontoVO().setNome(rs.getString("nome"));
		negociacaoContaReceberPlanoDescontoVO.getPlanoDescontoVO().setTipoDescontoParcela(rs.getString("tipoDescontoParcela"));
		negociacaoContaReceberPlanoDescontoVO.getPlanoDescontoVO().setPercDescontoParcela(rs.getDouble("percDescontoParcela"));
		
		return negociacaoContaReceberPlanoDescontoVO;
	}

}
