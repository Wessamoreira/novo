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

import negocio.comuns.administrativo.FollowMeCategoriaDespesaVO;
import negocio.comuns.administrativo.FollowMeVO;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.administrativo.FollowMeCategoriaDespesaInterfaceFacade;


@Repository
@Lazy
public class FollowMeCategoriaDespesa extends ControleAcesso implements FollowMeCategoriaDespesaInterfaceFacade {

    /**
     * 
     */
    private static final long serialVersionUID = -7104913339235650154L;

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void persistir(FollowMeCategoriaDespesaVO followMeCategoriaDespesaVO, FollowMeVO followMeVO) throws Exception {        
            followMeCategoriaDespesaVO.setFollowMe(followMeVO);
            if (followMeCategoriaDespesaVO.isNovoObj()) {
                incluir(followMeCategoriaDespesaVO);
            } else {
                alterar(followMeCategoriaDespesaVO);
            }        
    }
    
    private PreparedStatementCreator getPreparedStatementCreator(final FollowMeCategoriaDespesaVO followMeCategoriaDespesaVO,final String sql, final boolean incluir){
        return new PreparedStatementCreator() {            
            @Override
            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                int x = 1;
                PreparedStatement preparedStatement = arg0.prepareStatement(sql);
                preparedStatement.setInt(x++, followMeCategoriaDespesaVO.getFollowMe().getCodigo());
                preparedStatement.setInt(x++, followMeCategoriaDespesaVO.getCategoriaDespesa().getCodigo());
                if(!incluir){
                    preparedStatement.setInt(x++, followMeCategoriaDespesaVO.getCodigo());
                }
                return preparedStatement;
            }
        };
    }
    
    private void alterar(final FollowMeCategoriaDespesaVO followMeCategoriaDespesaVO) throws Exception{                   
        StringBuilder sql = new StringBuilder("UPDATE FollowMeCategoriaDespesa SET followMe = ?, categoriaDespesa = ?");        
        sql.append(" WHERE codigo = ? ");
        if(getConexao().getJdbcTemplate().update(getPreparedStatementCreator(followMeCategoriaDespesaVO, sql.toString(), false)) ==0){
            incluir(followMeCategoriaDespesaVO);
        };        
}
    
    private void incluir(final FollowMeCategoriaDespesaVO followMeCategoriaDespesaVO) throws Exception{
        try{               
            StringBuilder sql = new StringBuilder("INSERT INTO FollowMeCategoriaDespesa (followMe, categoriaDespesa ");            
            sql.append(") VALUES (?,?) returning codigo");
            followMeCategoriaDespesaVO.setCodigo(getConexao().getJdbcTemplate().query(getPreparedStatementCreator(followMeCategoriaDespesaVO, sql.toString(), true), 
                new ResultSetExtractor<Integer>() {
                public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
                    if (arg0.next()) {
                        followMeCategoriaDespesaVO.setNovoObj(Boolean.FALSE);
                        return arg0.getInt("codigo");
                    }
                    return null;
                }
            }));
            
        }catch (Exception e) {
            followMeCategoriaDespesaVO.setNovoObj(Boolean.TRUE);            
            throw e;
        }
    }
    

    @Override
    public List<FollowMeCategoriaDespesaVO> consultarPorFollowMe(Integer followMe) throws Exception {
            StringBuilder sb  = new StringBuilder("select FollowMeCategoriaDespesa.*, categoriaDespesa.descricao as \"categoriaDespesa.descricao\", categoriaDespesa.identificadorCategoriaDespesa as \"categoriaDespesa.identificadorCategoriaDespesa\" from FollowMeCategoriaDespesa ");
            sb.append(" inner join categoriaDespesa on  categoriaDespesa.codigo = FollowMeCategoriaDespesa.categoriaDespesa");
            sb.append(" where followMe = "+followMe+" order by categoriaDespesa.descricao");
            return montarDadosConsulta(getConexao().getJdbcTemplate().
                    queryForRowSet(sb.toString()));
        
    }
    
    private List<FollowMeCategoriaDespesaVO> montarDadosConsulta(SqlRowSet rs){
        List<FollowMeCategoriaDespesaVO> followMeCategoriaDespesaVOs = new ArrayList<FollowMeCategoriaDespesaVO>(0);
        while(rs.next()){
            followMeCategoriaDespesaVOs.add(montarDados(rs));
        }
        return followMeCategoriaDespesaVOs;
    }
    
    private FollowMeCategoriaDespesaVO montarDados(SqlRowSet rs){
        FollowMeCategoriaDespesaVO obj = new FollowMeCategoriaDespesaVO();
        obj.getFollowMe().setCodigo(rs.getInt("followMe"));
        obj.getCategoriaDespesa().setCodigo(rs.getInt("categoriaDespesa"));
        obj.getCategoriaDespesa().setDescricao(rs.getString("categoriaDespesa.descricao"));
        obj.getCategoriaDespesa().setIdentificadorCategoriaDespesa(rs.getString("categoriaDespesa.identificadorCategoriaDespesa"));
        obj.setCodigo(rs.getInt("codigo"));
        if(obj.getCodigo() > 0){
            obj.setSelecionado(true);        
            obj.setNovoObj(false);
        }
        return obj;
    }

    @Override
    public List<FollowMeCategoriaDespesaVO> consultarPorFollowMeTrazendoTodasCategoriaDespesa(Integer followMe) throws Exception {
        StringBuilder sb  = new StringBuilder("select FollowMeCategoriaDespesa.codigo, FollowMeCategoriaDespesa.followMe, ");
        sb.append(" FollowMeCategoriaDespesa.categoriaDespesa, categoriaDespesa.descricao as \"categoriaDespesa.descricao\", ");
        sb.append(" categoriaDespesa.identificadorCategoriaDespesa as \"categoriaDespesa.identificadorCategoriaDespesa\" ");
        sb.append(" from FollowMeCategoriaDespesa ");
        sb.append(" inner join categoriaDespesa on  categoriaDespesa.codigo = FollowMeCategoriaDespesa.categoriaDespesa");
        sb.append(" where followMe = "+followMe+" ");
        sb.append(" union all");
        sb.append(" select 0 as codigo, 0 as followMe, categoriaDespesa.codigo as categoriaDespesa, ");
        sb.append(" categoriaDespesa.descricao as \"categoriaDespesa.descricao\", categoriaDespesa.identificadorCategoriaDespesa as \"categoriaDespesa.identificadorCategoriaDespesa\" ");
        sb.append(" from categoriaDespesa ");        
        sb.append(" where codigo not in (select categoriaDespesa from FollowMeCategoriaDespesa where followMe = "+followMe+") ");
        sb.append(" order by \"categoriaDespesa.descricao\"");
        return montarDadosConsulta(getConexao().getJdbcTemplate().
                queryForRowSet(sb.toString()));
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void excluirFollowMeCategoriaDespesa(FollowMeVO followMeVO) throws Exception {
        StringBuilder sb = new StringBuilder("DELETE FROM followMeCategoriaDespesa where followme =  ").append(followMeVO.getCodigo());
        sb.append(" and codigo not in ( 0 ");
        for (FollowMeCategoriaDespesaVO followMeCategoriaDespesaVO : followMeVO.getFollowMeCategoriaDespesaVOs()) {
            if (!followMeCategoriaDespesaVO.isNovoObj() && followMeCategoriaDespesaVO.getSelecionado()) {
                sb.append(", ").append(followMeCategoriaDespesaVO.getCodigo());
            }
        }
        sb.append(")");
        getConexao().getJdbcTemplate().execute(sb.toString());

    }

}
