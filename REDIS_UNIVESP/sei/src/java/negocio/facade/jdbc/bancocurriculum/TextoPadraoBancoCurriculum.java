package negocio.facade.jdbc.bancocurriculum;

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
import negocio.comuns.bancocurriculum.TextoPadraoBancoCurriculumVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.bancocurriculum.TextoPadraoBancoCurriculumInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class TextoPadraoBancoCurriculum extends ControleAcesso implements TextoPadraoBancoCurriculumInterfaceFacade {

    private static String idEntidade;

    public static String getIdEntidade() {
        return idEntidade;
    }

    public void setIdEntidade(String aIdEntidade) {
        idEntidade = aIdEntidade;
    }

    public TextoPadraoBancoCurriculum() throws Exception {
        super();
        setIdEntidade("TextoPadraoBancoCurriculum");
    }

    public static void validarDados(TextoPadraoBancoCurriculumVO obj) throws ConsistirException, Exception {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if (obj.getNome() == null || obj.getNome().equals("")) {
            throw new ConsistirException("O campo NOME deve ser informado.");
        }
        if (unicidadeTipo(obj.getTipo()) && obj.isNovoObj().booleanValue()) {
            throw new ConsistirException("Já existe um texto padrão para esse tipo.");
        }
        if (obj.getTexto() == null || obj.getTexto().equals("")) {
            throw new ConsistirException("O campo TEXTO deve ser informado.");
        }
        if (obj.getTexto().contains("<!DOCTYPE")) {
            obj.setTexto(obj.getTexto().replace(obj.getTexto().substring(obj.getTexto().indexOf("<!DOCTYPE"), obj.getTexto().indexOf("<html>")) , ""));
        }
    }

    public TextoPadraoBancoCurriculumVO novo() throws Exception {
        TextoPadraoBancoCurriculum.incluir(getIdEntidade());
        TextoPadraoBancoCurriculumVO obj = new TextoPadraoBancoCurriculumVO();
        return obj;
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final TextoPadraoBancoCurriculumVO obj, UsuarioVO usuario) throws Exception {
        try {
            validarDados(obj);
            final String sql = "INSERT INTO TextoPadraoBancoCurriculum(nome,tipo,situacao,texto) VALUES (?,?,?,?) returning codigo";
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    sqlInserir.setString(1, obj.getNome());
                    sqlInserir.setString(2, obj.getTipo());
                    sqlInserir.setString(3, obj.getSituacao());
                    sqlInserir.setString(4, obj.getTexto());
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
    public void gravarSituacao(final TextoPadraoBancoCurriculumVO obj, UsuarioVO usuario) throws Exception {
        try {
            validarDados(obj);
            ControleAcesso.alterar(getIdEntidade());
            final String sql = "UPDATE TextoPadraoBancoCurriculum SET situacao=? WHERE ((codigo =? ))";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setString(1, obj.getSituacao());
                    sqlAlterar.setInt(2, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });

        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final TextoPadraoBancoCurriculumVO obj, UsuarioVO usuario) throws Exception {
        try {
            validarDados(obj);
            ControleAcesso.alterar(getIdEntidade());
            final String sql = "UPDATE TextoPadraoBancoCurriculum SET nome=?, tipo=?, texto=? WHERE ((codigo =? ))";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setString(1, obj.getNome());
                    sqlAlterar.setString(2, obj.getTipo());
                    sqlAlterar.setString(3, obj.getTexto());
                    sqlAlterar.setInt(4, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });

        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(TextoPadraoBancoCurriculumVO obj, UsuarioVO usuario) throws Exception {
        try {
            TextoPadraoBancoCurriculum.excluir(getIdEntidade());
            String sql = "DELETE FROM TextoPadraoBancoCurriculum WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});

        } catch (Exception e) {
            throw e;
        }
    }

    public List consultarPorSituacao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT TextoPadraobancocurriculum.* FROM TextoPadraoBancoCurriculum WHERE upper(situacao) like('" + valorConsulta.toUpperCase() + "%') ORDER BY nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    public List consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT TextoPadraobancocurriculum.* FROM TextoPadraoBancoCurriculum WHERE upper(nome) like('" + valorConsulta.toUpperCase() + "%') ORDER BY nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    public List consultarPorTipo(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        return consultarPorTipo(valorConsulta, controlarAcesso, null, nivelMontarDados, usuario);
    }

    public List consultarPorTipo(String valorConsulta, boolean controlarAcesso, String situacao, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT TextoPadraobancocurriculum.* FROM TextoPadraoBancoCurriculum WHERE upper(tipo) like('" + valorConsulta.toUpperCase() + "%') ";
        if (situacao != null) {
            sqlStr += " and situacao = '" + situacao + "' ";
        }
        sqlStr += " ORDER BY nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    public TextoPadraoBancoCurriculumVO consultarPorTipoUnica(String valorConsulta, boolean controlarAcesso, String situacao, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT TextoPadraobancocurriculum.* FROM TextoPadraoBancoCurriculum WHERE upper(tipo) like('" + valorConsulta.toUpperCase() + "%') ";
        if (situacao != null) {
            sqlStr += " and situacao = '" + situacao + "' ";
        }
        sqlStr += " ORDER BY nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        if (tabelaResultado.next()) {
            return montarDados(tabelaResultado, nivelMontarDados, usuario);
        } else {
            TextoPadraoBancoCurriculumVO textoErro = new TextoPadraoBancoCurriculumVO();
            textoErro.setTexto("");
            return textoErro;
        }
    }

    public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            TextoPadraoBancoCurriculumVO obj = new TextoPadraoBancoCurriculumVO();
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        tabelaResultado = null;
        return vetResultado;
    }

    public static TextoPadraoBancoCurriculumVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        TextoPadraoBancoCurriculumVO obj = new TextoPadraoBancoCurriculumVO();
        obj.setCodigo(dadosSQL.getInt("codigo"));
        obj.setNome(dadosSQL.getString("nome"));
        obj.setTipo(dadosSQL.getString("tipo"));
        obj.setSituacao(dadosSQL.getString("situacao"));
        obj.setTexto(dadosSQL.getString("texto"));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        return obj;
    }

    public TextoPadraoBancoCurriculumVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sql = "SELECT * FROM TextoPadraoBancoCurriculum WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( AreaProfissional ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    public static boolean unicidadeTipo(String valorConsulta) throws Exception {
        String sqlStr = "SELECT * FROM TextoPadraoBancoCurriculum WHERE upper(tipo) like('" + valorConsulta.toUpperCase() + "%')";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        if (tabelaResultado.next()) {
            return true;
        }
        return false;
    }
}
