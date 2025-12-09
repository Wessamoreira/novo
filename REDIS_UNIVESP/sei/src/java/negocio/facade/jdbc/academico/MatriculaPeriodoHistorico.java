package negocio.facade.jdbc.academico;

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

import negocio.comuns.academico.MatriculaPeriodoHistoricoVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.MatriculaPeriodoHistoricoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>MatriculaPeriodoTurmaDisciplinaVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>MatriculaPeriodoTurmaDisciplinaVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see MatriculaPeriodoTurmaDisciplinaVO
 * @see ControleAcesso
 */
@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class MatriculaPeriodoHistorico extends ControleAcesso implements MatriculaPeriodoHistoricoInterfaceFacade {

    protected static String idEntidade;

    public MatriculaPeriodoHistorico() throws Exception {
        super();
        setIdEntidade("Matricula");
    }

    /* (non-Javadoc)
     * @see negocio.facade.jdbc.academico.MatriculaPeriodoHistoricoInterfaceFacade#novo()
     */
    public MatriculaPeriodoTurmaDisciplinaVO novo() throws Exception {
        MatriculaPeriodoHistorico.incluir(getIdEntidade());
        MatriculaPeriodoTurmaDisciplinaVO obj = new MatriculaPeriodoTurmaDisciplinaVO();
        return obj;
    }

    /* (non-Javadoc)
     * @see negocio.facade.jdbc.academico.MatriculaPeriodoHistoricoInterfaceFacade#incluir(negocio.comuns.academico.MatriculaPeriodoHistoricoVO)
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final MatriculaPeriodoHistoricoVO obj) throws Exception {
        try {
           // //System.out.println("++++++++++++++++++++++ INCLUI MATRICULA PERIODO HISTORICO +++++++++++++++++++++++++++");
            final String sql = "INSERT INTO MatriculaPeriodoHistorico ( matriculaPeriodo, descricaoOperacao, dataOperacao ) VALUES (?, ?, ?) returning codigo";
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    sqlInserir.setInt(1, obj.getMatriculaPeriodo().getCodigo().intValue());
                    sqlInserir.setString(2, obj.getDescricaoOperacao());
                    sqlInserir.setTimestamp(3, Uteis.getDataJDBCTimestamp(obj.getDataOperacao()));
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

    /* (non-Javadoc)
     * @see negocio.facade.jdbc.academico.MatriculaPeriodoHistoricoInterfaceFacade#alterar(negocio.comuns.academico.MatriculaPeriodoHistoricoVO)
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final MatriculaPeriodoHistoricoVO obj) throws Exception {
        final String sql = "UPDATE MatriculaPeriodoHistorico set matriculaPeriodo=?, descricaoOperacao=?, dataOperacao=? WHERE (codigo = ?)";
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                sqlAlterar.setInt(1, obj.getMatriculaPeriodo().getCodigo().intValue());
                sqlAlterar.setString(2, obj.getDescricaoOperacao());
                sqlAlterar.setTimestamp(3, Uteis.getDataJDBCTimestamp(obj.getDataOperacao()));
                sqlAlterar.setInt(4, obj.getCodigo().intValue());
                return sqlAlterar;
            }
        });
    }

    /* (non-Javadoc)
     * @see negocio.facade.jdbc.academico.MatriculaPeriodoHistoricoInterfaceFacade#excluir(negocio.comuns.academico.MatriculaPeriodoHistoricoVO)
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(MatriculaPeriodoHistoricoVO obj) throws Exception {
        MatriculaPeriodoHistorico.excluir(getIdEntidade());
        String sql = "DELETE FROM MatriculaPeriodoHistorico WHERE ((codigo = ?))";
        getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
    }


    /* (non-Javadoc)
     * @see negocio.facade.jdbc.academico.MatriculaPeriodoHistoricoInterfaceFacade#consultarPorCodigoMatriculaPeriodo(java.lang.Integer, boolean, int)
     */
    public List consultarPorCodigoMatriculaPeriodo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM MatriculaPeriodoHistorico WHERE matriculaPeriodo = " + valorConsulta.intValue() + " ORDER BY dataOperacao desc";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>MatriculaPeriodoTurmaDisciplinaVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            MatriculaPeriodoHistoricoVO obj = new MatriculaPeriodoHistoricoVO();
            obj = montarDados(tabelaResultado, nivelMontarDados);
            vetResultado.add(obj);
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>MatriculaPeriodoTurmaDisciplinaVO</code>.
     * @return  O objeto da classe <code>MatriculaPeriodoTurmaDisciplinaVO</code> com os dados devidamente montados.
     */
    public static MatriculaPeriodoHistoricoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados) throws Exception {
        MatriculaPeriodoHistoricoVO obj = new MatriculaPeriodoHistoricoVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.getMatriculaPeriodo().setCodigo(new Integer(dadosSQL.getInt("matriculaPeriodo")));
        obj.setDescricaoOperacao(dadosSQL.getString("descricaoOperacao"));
        obj.setDataOperacao(dadosSQL.getTimestamp("dataOperacao"));
        obj.setNovoObj(Boolean.FALSE);
        return obj;
    }

    /* (non-Javadoc)
     * @see negocio.facade.jdbc.academico.MatriculaPeriodoHistoricoInterfaceFacade#excluirPeriodoHistoricoVOs(java.lang.Integer)
     */
    public void excluirPeriodoHistoricoVOs(Integer codigoMatriculaPeriodo) throws Exception {
        MatriculaPeriodo.excluir(getIdEntidade());
        String sql = "DELETE FROM MatriculaPeriodoHistorico WHERE (matriculaPeriodo = ?)";
        getConexao().getJdbcTemplate().update(sql, new Object[]{codigoMatriculaPeriodo});
    }


    /* (non-Javadoc)
     * @see negocio.facade.jdbc.academico.MatriculaPeriodoHistoricoInterfaceFacade#consultarPorChavePrimaria(java.lang.Integer, int)
     */
    public MatriculaPeriodoHistoricoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sqlStr = "SELECT * FROM MatriculaPeriodoHistorico WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( MatriculaPeriodoHistorico ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados));
    }

    /**
     * Operação responsável por obter o último valor gerado para uma chave primária.
     * É utilizada para obter o valor gerado pela SGBD para uma chave primária, 
     * a apresentação do mesmo e a implementação de possíveis relacionamentos. 
     */
    public static Integer obterValorChavePrimariaCodigo() throws Exception {
        String sqlStr = "SELECT MAX(codigo) FROM MatriculaPeriodoHistorico";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        tabelaResultado.next();
        return (new Integer(tabelaResultado.getInt(1)));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return MatriculaPeriodoHistorico.idEntidade;
    }

    /* (non-Javadoc)
     * @see negocio.facade.jdbc.academico.MatriculaPeriodoHistoricoInterfaceFacade#setIdEntidade(java.lang.String)
     */
    public void setIdEntidade(String idEntidade) {
        MatriculaPeriodoHistorico.idEntidade = idEntidade;
    }
}
