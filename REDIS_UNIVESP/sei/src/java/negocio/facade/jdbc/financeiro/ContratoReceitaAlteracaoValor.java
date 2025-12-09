package negocio.facade.jdbc.financeiro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ContratoReceitaAlteracaoValorVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.ContratoReceitaAlteracaoValorInterfaceFacade;

@Repository
@Lazy
public class ContratoReceitaAlteracaoValor extends ControleAcesso
		implements ContratoReceitaAlteracaoValorInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6207912252814966174L;

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final ContratoReceitaAlteracaoValorVO contratoReceitaAlteracaoValorVO, UsuarioVO usuario) throws Exception {
		final String sql = "INSERT INTO ContratoReceitaAlteracaoValor( contratoReceita, data, valorAnterior, valorNovo,  responsavel, contasAlteradas, motivoAlteracao) VALUES ( ?, ?, ?, ?, ?, ?, ? ) returning codigo"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		contratoReceitaAlteracaoValorVO.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
                PreparedStatement sqlInserir = cnctn.prepareStatement(sql);                
                sqlInserir.setInt(1, contratoReceitaAlteracaoValorVO.getContratosReceitasVO().getCodigo().intValue());                
                sqlInserir.setTimestamp(2, Uteis.getDataJDBCTimestamp(contratoReceitaAlteracaoValorVO.getData()));
                sqlInserir.setDouble(3, contratoReceitaAlteracaoValorVO.getValorAnterior().doubleValue());
                sqlInserir.setDouble(4, contratoReceitaAlteracaoValorVO.getValorNovo().doubleValue());
                sqlInserir.setInt(5, contratoReceitaAlteracaoValorVO.getResponsavel().getCodigo().intValue());
                sqlInserir.setString(6, contratoReceitaAlteracaoValorVO.getContasAlteradas());
                sqlInserir.setString(7, contratoReceitaAlteracaoValorVO.getMotivoAlteracao());
                return sqlInserir;
            }
        }, new ResultSetExtractor<Integer>() {

            public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
                if (rs.next()) {
                	contratoReceitaAlteracaoValorVO.setNovoObj(Boolean.FALSE);
                    return rs.getInt("codigo");
                }
                return null;
            }
        }));
		contratoReceitaAlteracaoValorVO.setNovoObj(Boolean.FALSE);

	}

	@Override
	public List<ContratoReceitaAlteracaoValorVO> consultarPorContratoReceita(Integer contratoReceita, UsuarioVO usuario) throws Exception {		
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet("select * from ContratoReceitaAlteracaoValor where contratoReceita = ? order by data desc ", contratoReceita), usuario);
	}
	
	private List<ContratoReceitaAlteracaoValorVO> montarDadosConsulta(SqlRowSet rs, UsuarioVO usuario) throws Exception{
		List<ContratoReceitaAlteracaoValorVO> contratoReceitaAlteracaoValorVOs = new ArrayList<ContratoReceitaAlteracaoValorVO>(0);
		while(rs.next()){
			contratoReceitaAlteracaoValorVOs.add(montarDados(rs, usuario));
		}
		return contratoReceitaAlteracaoValorVOs;	
	}
	
	private ContratoReceitaAlteracaoValorVO montarDados(SqlRowSet rs, UsuarioVO usuario) throws Exception{
		ContratoReceitaAlteracaoValorVO contratoReceitaAlteracaoValorVO = new ContratoReceitaAlteracaoValorVO();
		contratoReceitaAlteracaoValorVO.setNovoObj(false);
		contratoReceitaAlteracaoValorVO.setCodigo(rs.getInt("codigo"));
		contratoReceitaAlteracaoValorVO.setContasAlteradas(rs.getString("contasAlteradas"));
		contratoReceitaAlteracaoValorVO.setMotivoAlteracao(rs.getString("motivoAlteracao"));
		contratoReceitaAlteracaoValorVO.getContratosReceitasVO().setCodigo(rs.getInt("contratoReceita"));
		contratoReceitaAlteracaoValorVO.setData(rs.getDate("data"));
		contratoReceitaAlteracaoValorVO.getResponsavel().setCodigo(rs.getInt("responsavel"));
		contratoReceitaAlteracaoValorVO.setValorAnterior(rs.getDouble("valorAnterior"));
		contratoReceitaAlteracaoValorVO.setValorNovo(rs.getDouble("valorNovo"));
		contratoReceitaAlteracaoValorVO.setResponsavel(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(contratoReceitaAlteracaoValorVO.getResponsavel().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
		return contratoReceitaAlteracaoValorVO;
		
	}

}
