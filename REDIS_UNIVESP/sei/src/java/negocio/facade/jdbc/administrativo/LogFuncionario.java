package negocio.facade.jdbc.administrativo;

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

import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.LogFuncionarioVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.administrativo.LogFuncionarioInterfaceFacade;

@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class LogFuncionario extends ControleAcesso implements LogFuncionarioInterfaceFacade {

    public static final long serialVersionUID = 1908603660683298488L;
    protected static String idEntidade;

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public LogFuncionario() throws Exception {
        super();
        setIdEntidade("LogFuncionario");
    }

    public LogFuncionarioVO novo() throws Exception {
        LogFuncionario.incluir(getIdEntidade());
        LogFuncionarioVO obj = new LogFuncionarioVO();
        return obj;
    }

    public void registrarLogFuncionario(FuncionarioVO funcionario, String operacao, UsuarioVO usuario) throws Exception {
        LogFuncionarioVO LogFuncionarioVO = new LogFuncionarioVO();
        LogFuncionarioVO.setUsuario(usuario);
        LogFuncionarioVO.setData(new Date());
        LogFuncionarioVO.setAcao(operacao);
        LogFuncionarioVO.setNome(funcionario.getPessoa().getNome());
        LogFuncionarioVO.setCPF(funcionario.getPessoa().getCPF());
        LogFuncionarioVO.setCodigo(funcionario.getCodigo());
        incluir(LogFuncionarioVO);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final LogFuncionarioVO obj) throws Exception {
        final String sql = "INSERT INTO LogFuncionario ( usuario, data, acao, nome, CPF, codigoFuncionario) VALUES ( ?, ?, ?, ?, ?, ? ) returning codigo";
        obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                sqlInserir.setInt(1, obj.getUsuario().getCodigo());
                sqlInserir.setTimestamp(2, Uteis.getDataJDBCTimestamp(obj.getData()));
                sqlInserir.setString(3, obj.getAcao());
                sqlInserir.setString(4, obj.getNome());
                sqlInserir.setString(5, obj.getCPF());
                sqlInserir.setInt(6, obj.getCodigo());
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
    }

    public static List<LogFuncionarioVO> montarDadosConsulta(SqlRowSet tabelaResultado) throws Exception {
        List<LogFuncionarioVO> vetResultado = new ArrayList<LogFuncionarioVO>(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado));
        }
        return vetResultado;
    }

    public static LogFuncionarioVO montarDados(SqlRowSet dadosSQL) throws Exception {
        LogFuncionarioVO obj = new LogFuncionarioVO();
        obj.setCodigo(dadosSQL.getInt("codigo"));
        obj.setAcao(dadosSQL.getString("acao"));
        obj.setData(dadosSQL.getDate("data"));
        obj.getUsuario().setCodigo(dadosSQL.getInt("usuario"));
        obj.setNome(dadosSQL.getString("nome"));
        obj.setCPF(dadosSQL.getString("cpf"));
        obj.setCodigoFuncionario(dadosSQL.getInt("codigoFuncionario"));
        obj.setNovoObj(Boolean.FALSE);
        return obj;
    }

    public LogFuncionarioVO consultarPorChavePrimaria(Integer codigoPrm, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM LogFuncionario WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado));
    }

    public static String getIdEntidade() {
        return LogFuncionario.idEntidade;
    }

    public void setIdEntidade(String idEntidade) {
        LogFuncionario.idEntidade = idEntidade;
    }
}
