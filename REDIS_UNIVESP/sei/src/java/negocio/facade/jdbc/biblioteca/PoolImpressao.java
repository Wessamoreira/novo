package negocio.facade.jdbc.biblioteca;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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
import negocio.comuns.biblioteca.ImpressoraVO;
import negocio.comuns.biblioteca.PoolImpressaoVO;
import negocio.comuns.biblioteca.enumeradores.FormatoImpressaoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.biblioteca.PoolImpressaoInterfaceFacade;

@Repository
@Lazy
@Scope("singleton")
public class PoolImpressao extends ControleAcesso implements PoolImpressaoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4412177110276158788L;
	protected static String idEntidade;
	
	public PoolImpressao() throws Exception {
		super();
		setIdEntidade("PoolImpressao");
	}
	
	private void validarDados(PoolImpressaoVO poolImpressaoVO) throws ConsistirException{
		if(!Uteis.isAtributoPreenchido(poolImpressaoVO.getImpressoraVO())){
			throw new ConsistirException("O campo IMPRESSORA deve ser informado.");
		}
		if(poolImpressaoVO.getImprimir().trim().isEmpty()){
			throw new ConsistirException("O texto de impressão deve ser informado.");
		}
	}
	

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final PoolImpressaoVO poolImpressaoVO, final UsuarioVO usuarioVO) throws Exception {
		try {
			validarDados(poolImpressaoVO);
			final String sql = "INSERT INTO PoolImpressao( imprimir, formatoImpressao, data, impressora ) VALUES ( ?, ?, ?, ? ) returning codigo"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			poolImpressaoVO.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

	            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
	                PreparedStatement ps = arg0.prepareStatement(sql);
	                ps.setString(1, poolImpressaoVO.getImprimir());
	                ps.setString(2, poolImpressaoVO.getFormatoImpressao().name());
	                ps.setTimestamp(3, Uteis.getDataJDBCTimestamp(poolImpressaoVO.getData()));
	                ps.setInt(4, poolImpressaoVO.getImpressoraVO().getCodigo());
	                return ps;
	            }
	        }, new ResultSetExtractor<Integer>() {

	            public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
	                if (arg0.next()) {
	                	poolImpressaoVO.setNovoObj(Boolean.FALSE);
	                    return arg0.getInt("codigo");
	                }
	                return null;
	            }
	        }));
	    } catch (Exception e) {
	    	poolImpressaoVO.setNovoObj(true);
	        throw e;
	    }

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(PoolImpressaoVO poolImpressaoVO, UsuarioVO usuarioVO) throws Exception {
		PoolImpressao.verificarPermissaoFuncionalidadeUsuario(getIdEntidade(), usuarioVO);
		getConexao().getJdbcTemplate().update("DELETE FROM PoolImpressao where codigo = ?"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO), poolImpressaoVO.getCodigo());
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirPorImpressora(ImpressoraVO impressoraVO, UsuarioVO usuarioVO) throws Exception {
		PoolImpressao.verificarPermissaoFuncionalidadeUsuario(getIdEntidade(), usuarioVO);
		getConexao().getJdbcTemplate().update("DELETE FROM PoolImpressao where impressora = ?"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO), impressoraVO.getCodigo());
	}

	@Override
	public List<PoolImpressaoVO> consultarPorImpressora(ImpressoraVO impressoraVO, boolean validarAcesso,
			UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql =  new StringBuilder("select poolImpressao.* from poolImpressao ");
		sql.append(" where impressora = ").append(impressoraVO.getCodigo());
		sql.append(" order by data ");
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()));
	}
	
	private List<PoolImpressaoVO> montarDadosConsulta(SqlRowSet rs) throws Exception{
		List<PoolImpressaoVO> poolImpressaoVOs = new ArrayList<PoolImpressaoVO>(0);
		while(rs.next()){
			poolImpressaoVOs.add(montarDados(rs));
		}
		return poolImpressaoVOs;
	}
	
	private PoolImpressaoVO montarDados(SqlRowSet rs) throws Exception{
		PoolImpressaoVO poolImpressaoVO = new PoolImpressaoVO();
		poolImpressaoVO.setCodigo(rs.getInt("codigo"));
		poolImpressaoVO.setData(rs.getTimestamp("data"));
		poolImpressaoVO.setImprimir(rs.getString("imprimir"));
		poolImpressaoVO.getImpressoraVO().setCodigo(rs.getInt("impressora"));
		poolImpressaoVO.setErroImpressao(rs.getBoolean("erroImpressao"));
		poolImpressaoVO.setMotivoErroImpressao(rs.getString("motivoerroImpressao"));
		poolImpressaoVO.setFormatoImpressao(FormatoImpressaoEnum.valueOf(rs.getString("formatoImpressao")));
		poolImpressaoVO.setNovoObj(false);
		return poolImpressaoVO;
	}
	
	 /**
     * Operação reponsável por retornar o identificador desta classe. Este
     * identificar é utilizado para verificar as permissões de acesso as
     * operações desta classe.
     */
    public static String getIdEntidade() {
        return PoolImpressao.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta
     * classe. Esta alteração deve ser possível, pois, uma mesma classe de
     * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
     * que Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
    	PoolImpressao.idEntidade = idEntidade;
    }
    
    @Override
    public void incluirPoolImpressao(ImpressoraVO impressoraVO, FormatoImpressaoEnum formatoImpressaoEnum, String textoImpressao, UsuarioVO usuario ) throws Exception{		
			PoolImpressaoVO poolImpressaoVO = new PoolImpressaoVO();
			poolImpressaoVO.setData(new Date());
			poolImpressaoVO.setImprimir(textoImpressao);
			poolImpressaoVO.setFormatoImpressao(formatoImpressaoEnum);
			poolImpressaoVO.setImpressoraVO(impressoraVO);
			getFacadeFactory().getPoolImpressaoFacade().incluir(poolImpressaoVO, usuario);
		
	}
	

}
