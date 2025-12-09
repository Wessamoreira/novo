/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.facade.jdbc.academico;

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

import negocio.comuns.academico.LogTurmaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.LogTurmaInterfaceFacade;

/**
 *
 * @author Philippe
 */
@Repository
@Scope("singleton")
@Lazy
public class LogTurma extends ControleAcesso implements LogTurmaInterfaceFacade {

    private static String idEntidade;

    public static String getIdEntidade() {
        return idEntidade;
    }

    public void setIdEntidade(String aIdEntidade) {
        idEntidade = aIdEntidade;
    }

    public LogTurma() throws Exception {
        super();
        setIdEntidade("LogTurma");
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final LogTurmaVO obj, UsuarioVO usuario) throws Exception {
        try {
            final String sql = "INSERT INTO logturma(turma, contacorrente, curso, periodoletivo, gradecurricular, acao, dataalteracao, usuarioResponsavel, dataBaseGeracaoParcelas) VALUES (?,?,?,?,?,?,?,?,?) returning codigo";
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    if (obj.getTurma().getCodigo() != 0) {
                        sqlInserir.setInt(1, obj.getTurma().getCodigo());
                    } else {
                        sqlInserir.setNull(1, 0);
                    }
                    if (obj.getContaCorrente().getCodigo() != 0) {
                        sqlInserir.setInt(2, obj.getContaCorrente().getCodigo());
                    } else {
                        sqlInserir.setNull(2, 0);
                    }
                    if (obj.getCurso().getCodigo() != 0) {
                        sqlInserir.setInt(3, obj.getCurso().getCodigo());
                    } else {
                        sqlInserir.setNull(3, 0);
                    }
                    if (obj.getPeriodoLetivo().getCodigo() != 0) {
                        sqlInserir.setInt(4, obj.getPeriodoLetivo().getCodigo());
                    } else {
                        sqlInserir.setNull(4, 0);
                    }
                    if (obj.getGradeCurricular().getCodigo() != 0) {
                        sqlInserir.setInt(5, obj.getGradeCurricular().getCodigo());
                    } else {
                        sqlInserir.setNull(5, 0);
                    }
                    sqlInserir.setString(6, obj.getAcao());
                    sqlInserir.setTimestamp(7, Uteis.getDataJDBCTimestamp(obj.getDataAlteracao()));
                    if (obj.getUsuarioResponsavel().getCodigo() != 0) {
                        sqlInserir.setInt(8, obj.getUsuarioResponsavel().getCodigo());
                    } else {
                        sqlInserir.setNull(8, 0);
                    }
                    if(Uteis.isAtributoPreenchido(obj.getDataBaseGeracaoParcelas())){
                    	sqlInserir.setDate(9, Uteis.getDataJDBC(obj.getDataBaseGeracaoParcelas()));	
                    }else{
                    	sqlInserir.setNull(9, 0);
                    }
                    return sqlInserir;
                }
            }, new ResultSetExtractor<Object>() {

                public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
                    if (arg0.next()) {
                        return arg0.getInt("codigo");
                    }
                    return null;
                }
            }));
        } catch (Exception e) {
            throw e;
        }
    }

    private StringBuilder getSQLPadraoConsulta() {
        StringBuilder str = new StringBuilder();
        //Dados campanha
        str.append("SELECT LogTurma.codigo as codigo, ");
        str.append("contacorrente.codigo AS codigoContaCorrente, contacorrente.numero AS numeroContaCorrente, contacorrente.digito AS digitoContaCorrente, ");
        str.append("curso.codigo AS codigoCurso, curso.nome AS nomeCurso, turma.codigo AS codigoTurma, turma.identificadorTurma AS identificadorTurma, ");
        str.append("gradecurricular.codigo AS codigoGradeCurricular, gradecurricular.nome AS nomeGradeCurricular, periodoLetivo.codigo AS codigoPeriodoLetivo, ");
        str.append("periodoLetivo.descricao AS descricaoPeriodoLetivo, usuario.codigo AS codigoUsuario, pessoa.nome AS nomeResponsavel, logturma.acao As acao, logturma.dataAlteracao As dataAlteracao,  ");
        str.append("LogTurma.databasegeracaoparcelas as \"LogTurma.databasegeracaoparcelas\"  ");
        str.append(" FROM LogTurma ");

        str.append(" LEFT JOIN turma ON turma.codigo = LogTurma.turma ");
        str.append(" LEFT JOIN usuario ON usuario.codigo = LogTurma.usuarioResponsavel ");
        str.append(" LEFT JOIN curso ON curso.codigo = LogTurma.curso ");
        str.append(" LEFT JOIN gradecurricular ON gradecurricular.codigo = LogTurma.gradecurricular ");
        str.append(" LEFT JOIN periodoletivo ON periodoletivo.codigo = LogTurma.periodoletivo ");
        str.append(" LEFT JOIN contacorrente ON contacorrente.codigo = LogTurma.contacorrente ");
        str.append(" LEFT JOIN pessoa ON usuario.pessoa = pessoa.codigo ");
        return str;
    }

    public List consultarPorPeriodo(Date dataInicial, Date dataFinal, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = getSQLPadraoConsulta().append("WHERE LogTurma.dataAlteracao >= '").append(dataInicial).append("' AND LogTurma.dataAlteracao <= '").append(dataFinal).append("' ");
        sqlStr.append(" ORDER BY LogTurma.dataAlteracao desc");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    public List consultarPorIdentificadorTurma(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = getSQLPadraoConsulta().append("WHERE turma.identificadorturma ilike '").append(valorConsulta).append("%' ");
        sqlStr.append(" ORDER BY turma.identificadorturma, curso.nome ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    public List consultarPorCurso(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = getSQLPadraoConsulta().append("WHERE curso.nome ilike '").append(valorConsulta).append("%' ");
        sqlStr.append(" ORDER BY curso.nome, turma.identificadorturma ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    public List consultarPorAcao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = getSQLPadraoConsulta().append("WHERE logturma.acao = '").append(valorConsulta);
        sqlStr.append("' ORDER BY logturma.acao, turma.identificadorturma, curso.nome ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
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

    public static LogTurmaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        LogTurmaVO obj = new LogTurmaVO();
        obj.setCodigo((dadosSQL.getInt("codigo")));
        obj.setDataAlteracao((dadosSQL.getTimestamp("dataAlteracao")));
        obj.setDataBaseGeracaoParcelas((dadosSQL.getDate("LogTurma.databasegeracaoparcelas")));
        obj.setAcao((dadosSQL.getString("acao")));
        obj.getTurma().setCodigo(dadosSQL.getInt("codigoTurma"));
        obj.getTurma().setIdentificadorTurma(dadosSQL.getString("identificadorTurma"));
        obj.getGradeCurricular().setCodigo(dadosSQL.getInt("codigoGradeCurricular"));
        obj.getGradeCurricular().setNome(dadosSQL.getString("nomeGradeCurricular"));
        obj.getPeriodoLetivo().setCodigo(dadosSQL.getInt("codigoPeriodoLetivo"));
        obj.getPeriodoLetivo().setDescricao(dadosSQL.getString("descricaoPeriodoLetivo"));
        obj.getContaCorrente().setCodigo(dadosSQL.getInt("codigoContaCorrente"));
        obj.getContaCorrente().setDigito(dadosSQL.getString("digitoContaCorrente"));
        obj.getContaCorrente().setNumero(dadosSQL.getString("numeroContaCorrente"));
        obj.getCurso().setNome(dadosSQL.getString("nomeCurso"));
        obj.getCurso().setCodigo(dadosSQL.getInt("codigoCurso"));
        obj.getUsuarioResponsavel().setCodigo(dadosSQL.getInt("codigoUsuario"));
        obj.getUsuarioResponsavel().getPessoa().setNome(dadosSQL.getString("nomeResponsavel"));
        return obj;
    }
    
}
