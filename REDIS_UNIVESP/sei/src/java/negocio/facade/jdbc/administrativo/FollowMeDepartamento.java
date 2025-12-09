package negocio.facade.jdbc.administrativo;

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

import negocio.comuns.administrativo.FollowMeDepartamentoVO;
import negocio.comuns.administrativo.FollowMeVO;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.administrativo.FollowMeDepartamentoInterfaceFacade;


@Repository
@Lazy
public class FollowMeDepartamento extends ControleAcesso implements FollowMeDepartamentoInterfaceFacade {

    /**
     * 
     */
    private static final long serialVersionUID = -7104913339235650154L;

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void persistir(FollowMeDepartamentoVO followMeDepartamentoVO, FollowMeVO followMeVO) throws Exception {        
            followMeDepartamentoVO.setFollowMe(followMeVO);
            if (followMeDepartamentoVO.isNovoObj()) {
                incluir(followMeDepartamentoVO);
            } else {
                alterar(followMeDepartamentoVO);
            }        
    }
    
    private PreparedStatementCreator getPreparedStatementCreator(final FollowMeDepartamentoVO followMeDepartamentoVO,final String sql, final boolean incluir){
        return new PreparedStatementCreator() {            
            @Override
            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                int x = 1;
                PreparedStatement preparedStatement = arg0.prepareStatement(sql);
                preparedStatement.setInt(x++, followMeDepartamentoVO.getFollowMe().getCodigo());
                preparedStatement.setInt(x++, followMeDepartamentoVO.getDepartamento().getCodigo());
                if(!incluir){
                    preparedStatement.setInt(x++, followMeDepartamentoVO.getCodigo());
                }
                return preparedStatement;
            }
        };
    }
    
    private void alterar(final FollowMeDepartamentoVO followMeDepartamentoVO) throws Exception{                   
        StringBuilder sql = new StringBuilder("UPDATE FollowMeDepartamento SET followMe = ?, departamento = ?");        
        sql.append(" WHERE codigo = ? ");
        if(getConexao().getJdbcTemplate().update(getPreparedStatementCreator(followMeDepartamentoVO, sql.toString(), false)) ==0){
            incluir(followMeDepartamentoVO);
        };        
}
    
    private void incluir(final FollowMeDepartamentoVO followMeDepartamentoVO) throws Exception{
        try{               
            StringBuilder sql = new StringBuilder("INSERT INTO FollowMeDepartamento (followMe, departamento ");            
            sql.append(") VALUES (?,?) returning codigo");
            followMeDepartamentoVO.setCodigo(getConexao().getJdbcTemplate().query(getPreparedStatementCreator(followMeDepartamentoVO, sql.toString(), true), 
                new ResultSetExtractor<Integer>() {
                public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
                    if (arg0.next()) {
                        followMeDepartamentoVO.setNovoObj(Boolean.FALSE);
                        return arg0.getInt("codigo");
                    }
                    return null;
                }
            }));
            
        }catch (Exception e) {
            followMeDepartamentoVO.setNovoObj(Boolean.TRUE);            
            throw e;
        }
    }
    

    @Override
    public List<FollowMeDepartamentoVO> consultarPorFollowMe(Integer followMe) throws Exception {
            StringBuilder sb  = new StringBuilder("select FollowMeDepartamento.*, departamento.nome as \"departamento.nome\" from FollowMeDepartamento ");
            sb.append(" inner join departamento on  departamento.codigo = FollowMeDepartamento.departamento");
            sb.append(" where followMe = "+followMe+" order by departamento.nome");
            return montarDadosConsulta(getConexao().getJdbcTemplate().
                    queryForRowSet(sb.toString()));
        
    }
    
    private List<FollowMeDepartamentoVO> montarDadosConsulta(SqlRowSet rs){
        List<FollowMeDepartamentoVO> followMeDepartamentoVOs = new ArrayList<FollowMeDepartamentoVO>(0);
        while(rs.next()){
            followMeDepartamentoVOs.add(montarDados(rs));
        }
        return followMeDepartamentoVOs;
    }
    
    private FollowMeDepartamentoVO montarDados(SqlRowSet rs){
        FollowMeDepartamentoVO obj = new FollowMeDepartamentoVO();
        obj.getFollowMe().setCodigo(rs.getInt("followMe"));
        obj.getDepartamento().setCodigo(rs.getInt("departamento"));
        obj.getDepartamento().setNome(rs.getString("departamento.nome"));        
        obj.setCodigo(rs.getInt("codigo"));
        if(obj.getCodigo() > 0){
            obj.setSelecionado(true);        
            obj.setNovoObj(false);
        }
        return obj;
    }

    @Override
    public List<FollowMeDepartamentoVO> consultarPorFollowMeTrazendoTodosDepartamento(Integer followMe) throws Exception {
        StringBuilder sb  = new StringBuilder("select FollowMeDepartamento.codigo, FollowMeDepartamento.followMe, ");
        sb.append(" FollowMeDepartamento.departamento, departamento.nome as \"departamento.nome\" ");        
        sb.append(" from FollowMeDepartamento ");
        sb.append(" inner join departamento on  departamento.codigo = FollowMeDepartamento.departamento");
        sb.append(" where followMe = "+followMe+" ");
        sb.append(" union all");
        sb.append(" select 0 as codigo, 0 as followMe, departamento.codigo as departamento, ");
        sb.append(" departamento.nome as \"departamento.nome\" ");
        sb.append(" from departamento ");        
        sb.append(" where codigo not in (select departamento from FollowMeDepartamento where followMe = "+followMe+") ");
        sb.append(" order by \"departamento.nome\"");
        return montarDadosConsulta(getConexao().getJdbcTemplate().
                queryForRowSet(sb.toString()));
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void excluirFollowMeDepartamento(FollowMeVO followMeVO) throws Exception {
        StringBuilder sb = new StringBuilder("DELETE FROM followMeDepartamento where followme =  ").append(followMeVO.getCodigo());
        sb.append(" and codigo not in ( 0 ");
        for (FollowMeDepartamentoVO followMeDepartamentoVO : followMeVO.getFollowMeDepartamentoVOs()) {
            if (!followMeDepartamentoVO.isNovoObj() && followMeDepartamentoVO.getSelecionado()) {
                sb.append(", ").append(followMeDepartamentoVO.getCodigo());
            }
        }
        sb.append(")");
        getConexao().getJdbcTemplate().execute(sb.toString());

    }

}
