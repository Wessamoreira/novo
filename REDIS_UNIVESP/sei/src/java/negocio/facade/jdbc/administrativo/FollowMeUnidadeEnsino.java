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

import negocio.comuns.administrativo.FollowMeUnidadeEnsinoVO;
import negocio.comuns.administrativo.FollowMeVO;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.administrativo.FollowMeUnidadeEnsinoInterfaceFacade;

@Repository
@Lazy
public class FollowMeUnidadeEnsino extends ControleAcesso implements FollowMeUnidadeEnsinoInterfaceFacade {

    /**
     * 
     */
    private static final long serialVersionUID = -7104913339235650154L;

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void persistir(FollowMeUnidadeEnsinoVO followMeUnidadeEnsinoVO, FollowMeVO followMeVO) throws Exception {        
            followMeUnidadeEnsinoVO.setFollowMe(followMeVO);
            if (followMeUnidadeEnsinoVO.isNovoObj()) {
                incluir(followMeUnidadeEnsinoVO);
            } else {
                alterar(followMeUnidadeEnsinoVO);
            }        
    }
    
    private PreparedStatementCreator getPreparedStatementCreator(final FollowMeUnidadeEnsinoVO followMeUnidadeEnsinoVO,final String sql, final boolean incluir){
        return new PreparedStatementCreator() {            
            @Override
            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                int x = 1;
                PreparedStatement preparedStatement = arg0.prepareStatement(sql);
                preparedStatement.setInt(x++, followMeUnidadeEnsinoVO.getFollowMe().getCodigo());
                preparedStatement.setInt(x++, followMeUnidadeEnsinoVO.getUnidadeEnsino().getCodigo());
                if(!incluir){
                    preparedStatement.setInt(x++, followMeUnidadeEnsinoVO.getCodigo());
                }
                return preparedStatement;
            }
        };
    }
    
    private void alterar(final FollowMeUnidadeEnsinoVO followMeUnidadeEnsinoVO) throws Exception{                   
        StringBuilder sql = new StringBuilder("UPDATE FollowMeUnidadeEnsino SET followMe = ?, unidadeEnsino = ?");        
        sql.append(" WHERE codigo = ? ");
        if(getConexao().getJdbcTemplate().update(getPreparedStatementCreator(followMeUnidadeEnsinoVO, sql.toString(), false)) ==0){
            incluir(followMeUnidadeEnsinoVO);
        };        
}
    
    private void incluir(final FollowMeUnidadeEnsinoVO followMeUnidadeEnsinoVO) throws Exception{
        try{               
            StringBuilder sql = new StringBuilder("INSERT INTO FollowMeUnidadeEnsino (followMe, unidadeEnsino ");            
            sql.append(") VALUES (?,?) returning codigo");
            followMeUnidadeEnsinoVO.setCodigo(getConexao().getJdbcTemplate().query(getPreparedStatementCreator(followMeUnidadeEnsinoVO, sql.toString(), true), 
                new ResultSetExtractor<Integer>() {
                public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
                    if (arg0.next()) {
                        followMeUnidadeEnsinoVO.setNovoObj(Boolean.FALSE);
                        return arg0.getInt("codigo");
                    }
                    return null;
                }
            }));
            
        }catch (Exception e) {
            followMeUnidadeEnsinoVO.setNovoObj(Boolean.TRUE);            
            throw e;
        }
    }
    

    @Override
    public List<FollowMeUnidadeEnsinoVO> consultarPorFollowMe(Integer followMe) throws Exception {
            StringBuilder sb  = new StringBuilder("select FollowMeUnidadeEnsino.*, unidadeEnsino.nome as \"unidadeEnsino.nome\" from FollowMeUnidadeEnsino ");
            sb.append(" inner join unidadeEnsino on  unidadeEnsino.codigo = FollowMeUnidadeEnsino.unidadeEnsino");
            sb.append(" where followMe = "+followMe+" order by unidadeEnsino.nome");
            return montarDadosConsulta(getConexao().getJdbcTemplate().
                    queryForRowSet(sb.toString()));
        
    }
    
    private List<FollowMeUnidadeEnsinoVO> montarDadosConsulta(SqlRowSet rs){
        List<FollowMeUnidadeEnsinoVO> followMeUnidadeEnsinoVOs = new ArrayList<FollowMeUnidadeEnsinoVO>(0);
        while(rs.next()){
            followMeUnidadeEnsinoVOs.add(montarDados(rs));
        }
        return followMeUnidadeEnsinoVOs;
    }
    
    private FollowMeUnidadeEnsinoVO montarDados(SqlRowSet rs){
        FollowMeUnidadeEnsinoVO obj = new FollowMeUnidadeEnsinoVO();
        obj.getFollowMe().setCodigo(rs.getInt("followMe"));
        obj.getUnidadeEnsino().setCodigo(rs.getInt("unidadeEnsino"));
        obj.getUnidadeEnsino().setNome(rs.getString("unidadeEnsino.nome"));
        obj.setCodigo(rs.getInt("codigo"));
        if(obj.getCodigo() > 0){
            obj.setSelecionar(true);        
            obj.setNovoObj(false);
        }
        return obj;
    }

    @Override
    public List<FollowMeUnidadeEnsinoVO> consultarPorFollowMeTrazendoTodasUnidadesEnsino(Integer followMe) throws Exception {
        StringBuilder sb  = new StringBuilder("select FollowMeUnidadeEnsino.codigo, FollowMeUnidadeEnsino.followMe, FollowMeUnidadeEnsino.unidadeEnsino, unidadeEnsino.nome as \"unidadeEnsino.nome\" from FollowMeUnidadeEnsino ");
        sb.append(" inner join unidadeEnsino on  unidadeEnsino.codigo = FollowMeUnidadeEnsino.unidadeEnsino");
        sb.append(" where followMe = "+followMe+" ");
        sb.append(" union all");
        sb.append(" select 0 as codigo, 0 as followMe, unidadeEnsino.codigo as unidadeEnsino, unidadeEnsino.nome as \"unidadeEnsino.nome\" from unidadeEnsino ");        
        sb.append(" where codigo not in (select unidadeEnsino from FollowMeUnidadeEnsino where followMe = "+followMe+") ");
        sb.append(" order by \"unidadeEnsino.nome\"");
        return montarDadosConsulta(getConexao().getJdbcTemplate().
                queryForRowSet(sb.toString()));
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void excluirFollowMeUnidadeEnsino(FollowMeVO followMeVO) throws Exception {
        StringBuilder sb = new StringBuilder("DELETE FROM followMeUnidadeEnsino where followme =  ").append(followMeVO.getCodigo());
        sb.append(" and codigo not in ( 0 ");
        for (FollowMeUnidadeEnsinoVO followMeUnidadeEnsinoVO : followMeVO.getFollowMeUnidadeEnsinoVOs()) {
            if (!followMeUnidadeEnsinoVO.isNovoObj() && followMeUnidadeEnsinoVO.getSelecionar()) {
                sb.append(", ").append(followMeUnidadeEnsinoVO.getCodigo());
            }
        }
        sb.append(")");
        getConexao().getJdbcTemplate().execute(sb.toString());

    }

}
