package negocio.facade.jdbc.protocolo;

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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.protocolo.TipoRequerimentoUnidadeEnsinoVO;
import negocio.comuns.protocolo.TipoRequerimentoVO;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.protocolo.TipoRequerimentoUnidadeEnsinoInterfaceFacade;

@Service
@Lazy
public class TipoRequerimentoUnidadeEnsino extends ControleAcesso implements TipoRequerimentoUnidadeEnsinoInterfaceFacade  {

    /**
     * 
     */
    private static final long serialVersionUID = -4399677640957550635L;
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final TipoRequerimentoUnidadeEnsinoVO obj) throws Exception {
        final String sql = "INSERT INTO TipoRequerimentoUnidadeEnsino( tipoRequerimento, unidadeEnsino) VALUES ( ?, ? ) returning codigo";
        obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(final Connection arg0) throws SQLException {
                final PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                sqlInserir.setInt(1, obj.getTipoRequerimento().getCodigo());
                sqlInserir.setInt(2, obj.getUnidadeEnsino().getCodigo());
                return sqlInserir;
            }
        }, new ResultSetExtractor<Integer>() {

            public Integer extractData(final ResultSet arg0) throws SQLException, DataAccessException {
                if (arg0.next()) {
                    obj.setNovoObj(Boolean.FALSE);
                    return arg0.getInt("codigo");
                }
                return null;
            }
        }));
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final TipoRequerimentoUnidadeEnsinoVO obj) throws Exception {
        try {
            final String sql = "UPDATE TipoRequerimentoUnidadeEnsino set tipoRequerimento=?, unidadeEnsino = ? WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setInt(1, obj.getTipoRequerimento().getCodigo());
                    sqlAlterar.setInt(2, obj.getUnidadeEnsino().getCodigo());
                    sqlAlterar.setInt(3, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });
        } catch (Exception e) {
            throw e;
        }
    }


    @Override
    public void incluirItemTipoRequerimentoUnidadeEnsinoVOs(List<TipoRequerimentoUnidadeEnsinoVO> tipoRequerimentoUnidadeEnsinoVOs, TipoRequerimentoVO tipoRequerimentoVO) throws Exception {
        for (TipoRequerimentoUnidadeEnsinoVO tipoRequerimentoUnidadeEnsinoVO : tipoRequerimentoUnidadeEnsinoVOs) {
            tipoRequerimentoUnidadeEnsinoVO.setTipoRequerimento(tipoRequerimentoVO);
            incluir(tipoRequerimentoUnidadeEnsinoVO);
        }

    }

    @Override
    public void alterarItemTipoRequerimentoUnidadeEnsinoVOs(List<TipoRequerimentoUnidadeEnsinoVO> tipoRequerimentoUnidadeEnsinoVOs, TipoRequerimentoVO tipoRequerimentoVO) throws Exception {
        excluirItemTipoRequerimentoUnidadeEnsinoVOs(tipoRequerimentoUnidadeEnsinoVOs, tipoRequerimentoVO);
        for (TipoRequerimentoUnidadeEnsinoVO tipoRequerimentoUnidadeEnsinoVO : tipoRequerimentoUnidadeEnsinoVOs) {
            tipoRequerimentoUnidadeEnsinoVO.setTipoRequerimento(tipoRequerimentoVO);
            if (tipoRequerimentoUnidadeEnsinoVO.getCodigo() == null || tipoRequerimentoUnidadeEnsinoVO.getCodigo() == 0) {
                incluir(tipoRequerimentoUnidadeEnsinoVO);
            } else {
                alterar(tipoRequerimentoUnidadeEnsinoVO);
            }
        }

    }

    @Override
    public void excluirItemTipoRequerimentoUnidadeEnsinoVOs(List<TipoRequerimentoUnidadeEnsinoVO> tipoRequerimentoUnidadeEnsinoVOs, TipoRequerimentoVO tipoRequerimentoVO) throws Exception {
        StringBuilder sql = new StringBuilder("");
        try {
            sql.append("DELETE FROM TipoRequerimentoUnidadeEnsino where tipoRequerimento = ").append(tipoRequerimentoVO.getCodigo());
            int x = 0;
            for (TipoRequerimentoUnidadeEnsinoVO tipoRequerimentoUnidadeEnsinoVO : tipoRequerimentoUnidadeEnsinoVOs) {
                if (x == 0) {
                    sql.append(" and codigo not in (").append(tipoRequerimentoUnidadeEnsinoVO.getCodigo());
                    x++;
                } else {
                    sql.append(", ").append(tipoRequerimentoUnidadeEnsinoVO.getCodigo());
                }
            }
            if (x > 0) {
                sql.append(") ");
            }
            getConexao().getJdbcTemplate().execute(sql.toString());
        } catch (Exception e) {
            throw e;
        } finally {

            sql = null;
        }

    }

    @Override
    public List<TipoRequerimentoUnidadeEnsinoVO> consultarItemTipoRequerimentoUnidadeEnsinoPorTipoRequerimento(Integer tipoRequerimento, NivelMontarDados nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
        StringBuilder sql = new StringBuilder("SELECT * FROM TipoRequerimentoUnidadeEnsino WHERE  tipoRequerimento = ").append(tipoRequerimento);
        return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()), nivelMontarDados, usuarioVO);
    }
    
    
    public List<TipoRequerimentoUnidadeEnsinoVO> montarDadosConsulta(SqlRowSet rs, NivelMontarDados nivelMontarDados, UsuarioVO usuarioVO) throws Exception{
        List<TipoRequerimentoUnidadeEnsinoVO> objs = new ArrayList<TipoRequerimentoUnidadeEnsinoVO>(0);
        while(rs.next()){
            objs.add(montarDados(rs, nivelMontarDados, usuarioVO));
        }
        return objs;
    }
    
    public TipoRequerimentoUnidadeEnsinoVO montarDados(SqlRowSet rs, NivelMontarDados nivelMontarDados, UsuarioVO usuarioVO) throws Exception{
        TipoRequerimentoUnidadeEnsinoVO obj = new TipoRequerimentoUnidadeEnsinoVO();
        obj.setCodigo(rs.getInt("codigo"));        
        obj.getTipoRequerimento().setCodigo(rs.getInt("tipoRequerimento"));
        obj.getUnidadeEnsino().setCodigo(rs.getInt("unidadeEnsino"));
        obj.setNovoObj(false);
        return obj;
    }

}
