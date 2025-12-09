package negocio.facade.jdbc.academico;

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

import negocio.comuns.academico.IconeVO;
import negocio.comuns.academico.enumeradores.SituacaoIconeEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.IconeInterfaceFacade;

@Repository
@Lazy
public class Icone extends ControleAcesso implements IconeInterfaceFacade {

    /**
     * 
     */
    private static final long serialVersionUID = -3144701294624037858L;

    @Override
    public void perisitir(IconeVO iconeVO, Boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        if(iconeVO.isNovoObj()){
            incluir(iconeVO, controlarAcesso, usuario);
        }else{
            alterar(iconeVO, controlarAcesso, usuario);
        }
    }
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void incluir(final IconeVO iconeVO, Boolean controlarAcesso, UsuarioVO usuario) throws Exception {

        try {
            final StringBuilder sql = new StringBuilder("INSERT INTO Icone ");
            sql.append(" ( caminhoBase, nomeReal, situacaoIcone) "); 
            sql.append(" VALUES ( ?, ?, ?) returning codigo").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
            iconeVO.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    int x = 1;
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
                    sqlInserir.setString(x++, iconeVO.getCaminhoBase());
                    sqlInserir.setString(x++, iconeVO.getNomeReal());                    
                    sqlInserir.setString(x++, iconeVO.getSituacaoIcone().name());
                    return sqlInserir;
                }
            }, new ResultSetExtractor<Integer>() {

                public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
                    if (arg0.next()) {
                        iconeVO.setNovoObj(Boolean.FALSE);
                        return arg0.getInt("codigo");
                    }
                    return null;
                }
            }));
        } catch (Exception e) {
            iconeVO.setNovoObj(true);
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void alterar(final IconeVO iconeVO, Boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        try {
            final StringBuilder sql = new StringBuilder("UPDATE Icone SET ");
            sql.append(" caminhoBase = ? , nomeReal = ? , situacaoIcone = ? ");
            sql.append(" where codigo = ? ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
            if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    int x = 1;
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
                    sqlAlterar.setString(x++, iconeVO.getCaminhoBase());
                    sqlAlterar.setString(x++, iconeVO.getNomeReal());                    
                    sqlAlterar.setString(x++, iconeVO.getSituacaoIcone().name());                    
                    sqlAlterar.setInt(x++, iconeVO.getCodigo());
                    return sqlAlterar;
                }
            }) <= 0) {
                incluir(iconeVO, controlarAcesso, usuario);
                return;
            }
            iconeVO.setNovoObj(false);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public List<IconeVO> consultarIcones(Integer limite, Integer pagina, String caminhoWebRepositorio) throws Exception {
        StringBuilder sb = new StringBuilder("SELECT * FROM icone ");
        if(limite != null && limite > 0){
            sb.append(" limit ").append(limite).append(" offset ").append(pagina);
        }
        return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sb.toString()), caminhoWebRepositorio);
    }
    
    private List<IconeVO> montarDadosConsulta(SqlRowSet rs, String caminhoWebRepositorio){
        List<IconeVO> iconeVOs = new ArrayList<IconeVO>(0);
        while (rs.next()) {            
            iconeVOs.add(montarDados(rs, caminhoWebRepositorio));
        }
        return iconeVOs;
    }
    
    private IconeVO montarDados(SqlRowSet rs, String caminhoWebRepositorio){
        IconeVO iconeVO = new IconeVO();
        iconeVO.setCaminhoBase(rs.getString("caminhoBase"));        
        iconeVO.setNomeReal(rs.getString("nomeReal"));
        iconeVO.setSituacaoIcone(SituacaoIconeEnum.valueOf(rs.getString("situacaoIcone")));
        iconeVO.setCodigo(rs.getInt("codigo"));
        iconeVO.setCaminhoWebRepositorio(caminhoWebRepositorio);
        iconeVO.setNovoObj(false);
        return iconeVO;
    }

}
