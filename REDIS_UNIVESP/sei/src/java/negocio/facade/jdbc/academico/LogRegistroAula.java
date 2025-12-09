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

import negocio.comuns.academico.FrequenciaAulaVO;
import negocio.comuns.academico.LogRegistroAulaVO;
import negocio.comuns.academico.RegistroAulaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.LogRegistroAulaInterfaceFacade;

@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class LogRegistroAula extends ControleAcesso implements LogRegistroAulaInterfaceFacade {

    public static final long serialVersionUID = 1908603660683288474L;
    protected static String idEntidade;

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public LogRegistroAula() throws Exception {
        super();
        setIdEntidade("LogRegistroAula");
    }

    public LogRegistroAulaVO novo() throws Exception {
        LogRegistroAula.incluir(getIdEntidade());
        LogRegistroAulaVO obj = new LogRegistroAulaVO();
        return obj;
    }

    public void registrarLogRegistroAula(RegistroAulaVO registroAula, String nomeDisciplina, String operacao, UsuarioVO usuario) throws Exception {
        StringBuilder dados = new StringBuilder();
        dados.append("Operação -> ").append(operacao).append(" ");
        dados.append("Turma ").append(registroAula.getTurma().getIdentificadorTurma()).append(" ");
        dados.append("Disciplina ").append(nomeDisciplina).append(" ");
        if (!registroAula.getFrequenciaAulaVOs().isEmpty()) {
            dados.append("Com os seguintes dados : ");
            for (FrequenciaAulaVO frequenciaAulaVO : registroAula.getFrequenciaAulaVOs()) {
                if (!frequenciaAulaVO.getFrequenciaOculta()) {
                	if (registroAula.getHorario().contains("(")) {
                		dados.append("Horário:").append(registroAula.getHorario_Apresentar()).append(" ");
                	} else {
                		dados.append("Horário:").append(registroAula.getNrAula()).append("ª Aula  ");
                	}
                    dados.append("Data Aula:").append(registroAula.getData_Apresentar()).append(" - Matrícula:");
                    dados.append(frequenciaAulaVO.getMatricula().getMatricula()).append(" - ").append(frequenciaAulaVO.getPresente() ? "Presente" : "Falta").append(" ");
                }
            }
        }
        realizarRegistroLogRegistroAula(dados.toString(), usuario);
    }

    public void realizarRegistroLogRegistroAula(String dados, UsuarioVO usuario) throws Exception {
        LogRegistroAulaVO LogRegistroAulaVO = new LogRegistroAulaVO();
        LogRegistroAulaVO.setUsuario(usuario);
        LogRegistroAulaVO.setData(new Date());
        LogRegistroAulaVO.setDados(dados);
        incluir(LogRegistroAulaVO);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final LogRegistroAulaVO obj) throws Exception {
        final String sql = "INSERT INTO LogRegistroAula ( usuario, data, dados) VALUES ( ?, ?, ? ) returning codigo";
        obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                sqlInserir.setInt(1, obj.getUsuario().getCodigo());
                sqlInserir.setTimestamp(2, Uteis.getDataJDBCTimestamp(obj.getData()));
                sqlInserir.setString(3, obj.getDados());
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

    public static List<LogRegistroAulaVO> montarDadosConsulta(SqlRowSet tabelaResultado) throws Exception {
        List<LogRegistroAulaVO> vetResultado = new ArrayList<LogRegistroAulaVO>(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado));
        }
        return vetResultado;
    }

    public static LogRegistroAulaVO montarDados(SqlRowSet dadosSQL) throws Exception {
        LogRegistroAulaVO obj = new LogRegistroAulaVO();
        obj.setCodigo(dadosSQL.getInt("codigo"));
        obj.setDados(dadosSQL.getString("dados"));
        obj.setData(dadosSQL.getDate("data"));
        obj.getUsuario().setCodigo(dadosSQL.getInt("usuario"));
        obj.setNovoObj(Boolean.FALSE);
        return obj;
    }

    public LogRegistroAulaVO consultarPorChavePrimaria(Integer codigoPrm, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM LogRegistroAula WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado));
    }

    public static String getIdEntidade() {
        return LogRegistroAula.idEntidade;
    }

    public void setIdEntidade(String idEntidade) {
        LogRegistroAula.idEntidade = idEntidade;
    }
}
