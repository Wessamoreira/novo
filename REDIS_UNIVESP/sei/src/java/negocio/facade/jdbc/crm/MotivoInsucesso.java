/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.facade.jdbc.crm;

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
import negocio.comuns.crm.MotivoInsucessoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.crm.MotivoInsucessoInterfaceFacade;

/**
 *
 * @author Philippe
 */
@Repository
@Scope("singleton")
@Lazy
public class MotivoInsucesso extends ControleAcesso implements MotivoInsucessoInterfaceFacade {

    private static String idEntidade;

    public static String getIdEntidade() {
        if (idEntidade == null) {
            idEntidade = "MotivoInsucesso";
        }
        return idEntidade;
    }

    public void setIdEntidade(String aIdEntidade) {
        idEntidade = aIdEntidade;
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final MotivoInsucessoVO obj, UsuarioVO usuario) throws Exception {
        try {
            validarDados(obj);
            final String sql = "INSERT INTO motivoInsucesso(descricao) VALUES (?) returning codigo" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    sqlInserir.setString(1, obj.getDescricao());
                    return sqlInserir;
                }
            }, new ResultSetExtractor() {

                public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
                    if (arg0.next()) {
                        obj.setNovoObj(Boolean.FALSE);
                        return arg0.getInt("codigo");
                    }
                    return null;
                }
            }));
            obj.setNovoObj(Boolean.FALSE);
        } catch (Exception e) {
            obj.setNovoObj(Boolean.TRUE);
            throw e;
        }
    }

     @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final MotivoInsucessoVO obj, UsuarioVO usuario) throws Exception {
        try {
            MotivoInsucesso.alterar(getIdEntidade());
            validarDados(obj);
            final String sql = "UPDATE motivoInsucesso SET descricao=? WHERE ((codigo =? ))" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setString(1, obj.getDescricao());
                    sqlAlterar.setInt(2, obj.getCodigo());
                    return sqlAlterar;
                }
            });
        } catch (Exception e) {
            throw e;
        }

    }

    public List consultar(String campoConsulta, String valorConsulta, UsuarioVO usuario) throws Exception {
        List objs = new ArrayList(0);
        if (campoConsulta.equals("descricao")) {
            objs = consultarPorDescricao(valorConsulta, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
        }
        return objs;
    }

    private void validarDados(MotivoInsucessoVO obj) throws ConsistirException{
        if (obj.getDescricao() == null || obj.getDescricao().equals("")){
            throw new ConsistirException("O campo descrição deve ser informado.");
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(MotivoInsucessoVO obj, UsuarioVO usuario) throws Exception {
        try {
            MotivoInsucesso.excluir(getIdEntidade());
            String sqlAlteracao = "UPDATE interacaoworkflow SET motivoinsucesso = null WHERE ((MotivoInsucesso = " + obj.getCodigo() + ")) " +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sqlAlteracao);
            String sql = "DELETE FROM MotivoInsucesso WHERE ((codigo = ?))" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    public List consultarPorDescricao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM MotivoInsucesso WHERE descricao like '" + valorConsulta + "%'";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        tabelaResultado = null;
        return vetResultado;
    }

    public static MotivoInsucessoVO montarDados(SqlRowSet dadosSQL, Integer nivelMontarDados, UsuarioVO usuario) {
        MotivoInsucessoVO obj = new MotivoInsucessoVO();
        obj.setCodigo(dadosSQL.getInt("codigo"));
        obj.setDescricao(dadosSQL.getString("descricao"));
        return obj;
    }

}
