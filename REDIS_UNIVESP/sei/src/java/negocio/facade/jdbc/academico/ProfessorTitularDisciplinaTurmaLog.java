package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.ProfessorTitularDisciplinaTurmaLogVO;
import negocio.comuns.academico.ProfessorTitularDisciplinaTurmaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.ProfessorTitularDisciplinaTurmaLogInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe
 * <code>ProfessorTitularDisciplinaTurmaVO</code>. Responsável por implementar operações como incluir, alterar, excluir e consultar
 * pertinentes a classe <code>ProfessorTitularDisciplinaTurmaVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see ProfessorTitularDisciplinaTurmaVO
 * @see ControleAcesso
 */
@Lazy
@Repository
@Scope("singleton")
public class ProfessorTitularDisciplinaTurmaLog extends ControleAcesso implements ProfessorTitularDisciplinaTurmaLogInterfaceFacade {

    protected static String idEntidade;
    public static final long serialVersionUID = 1L;

    public ProfessorTitularDisciplinaTurmaLog() throws Exception {
        super();
        setIdEntidade("ProfessorTitularDisciplinaTurma");
    }

    public static String getIdEntidade() {
        return ProfessorTitularDisciplinaTurmaLog.idEntidade;
    }

    public void setIdEntidade(String idEntidade) {
        ProfessorTitularDisciplinaTurmaLog.idEntidade = idEntidade;
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void preencherProfessorTitularDisciplinaTurmaLog(ProfessorTitularDisciplinaTurmaVO obj, String operacao, UsuarioVO usuario) throws Exception {
        try {
            ProfessorTitularDisciplinaTurmaLogVO professorTitularDisciplinaTurmaLog = new ProfessorTitularDisciplinaTurmaLogVO();
            professorTitularDisciplinaTurmaLog.setOperacao(operacao);
            if (usuario != null) {
                professorTitularDisciplinaTurmaLog.setResponsavel(usuario.getCodigo());
            }
            professorTitularDisciplinaTurmaLog.setAno(obj.getAno());
            professorTitularDisciplinaTurmaLog.setSemestre(obj.getSemestre());
            if (Uteis.isAtributoPreenchido(obj.getTurma().getCodigo())) {
            	professorTitularDisciplinaTurmaLog.setTurma(obj.getTurma().getCodigo());
            } else {
            	professorTitularDisciplinaTurmaLog.setCurso(obj.getCursoVO().getCodigo());
            }
            professorTitularDisciplinaTurmaLog.setDisciplina(obj.getDisciplina().getCodigo());
            professorTitularDisciplinaTurmaLog.setProfessor(obj.getProfessor().getCodigo());
            professorTitularDisciplinaTurmaLog.setTitular(obj.getTitular());
            incluir(professorTitularDisciplinaTurmaLog);
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void preencherProfessorTitularDisciplinaTurmaLogOperacaoExclusao(ProfessorTitularDisciplinaTurmaVO obj, String operacao, UsuarioVO usuario) throws Exception {
        try {
            ProfessorTitularDisciplinaTurmaLogVO professorTitularDisciplinaTurmaLog = new ProfessorTitularDisciplinaTurmaLogVO();
            professorTitularDisciplinaTurmaLog.setOperacao(operacao);
            if (usuario != null) {
                professorTitularDisciplinaTurmaLog.setResponsavel(usuario.getCodigo());
            }
            professorTitularDisciplinaTurmaLog.setAno(obj.getAno());
            professorTitularDisciplinaTurmaLog.setSemestre(obj.getSemestre());
            professorTitularDisciplinaTurmaLog.setTurma(obj.getTurma().getCodigo());
            professorTitularDisciplinaTurmaLog.setDisciplina(obj.getDisciplina().getCodigo());
            professorTitularDisciplinaTurmaLog.setProfessor(obj.getProfessor().getCodigo());
            professorTitularDisciplinaTurmaLog.setTitular(obj.getTitular());
            incluirOperacaoExclusao(professorTitularDisciplinaTurmaLog);
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final ProfessorTitularDisciplinaTurmaLogVO obj) throws Exception {
        try {
            final String sql = "INSERT INTO professorTitularDisciplinaTurmaLog( semestre, ano, turma, disciplina, professor, titular, "
                    + "responsavel, operacao) "
                    + " VALUES ( ?, ?, ?, ?, ?, ?, ?, ?) returning codigo";
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
                    PreparedStatement sqlInserir = cnctn.prepareStatement(sql);
                    int i = 0;
                    sqlInserir.setString(++i, obj.getSemestre());
                    sqlInserir.setString(++i, obj.getAno());
                    if (!obj.getTurma().equals(0)) {
                        sqlInserir.setInt(++i, obj.getTurma());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    if (!obj.getDisciplina().equals(0)) {
                        sqlInserir.setInt(++i, obj.getDisciplina());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    if (!obj.getProfessor().equals(0)) {
                        sqlInserir.setInt(++i, obj.getProfessor());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    sqlInserir.setBoolean(++i, obj.getTitular());
                    if (!obj.getResponsavel().equals(0)) {
                        sqlInserir.setInt(++i, obj.getResponsavel());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }

                    sqlInserir.setString(++i, obj.getOperacao());
                    return sqlInserir;
                }
            },
                    new ResultSetExtractor() {

                        public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                            if (rs.next()) {
                                obj.setNovoObj(Boolean.FALSE);
                                return rs.getInt("codigo");
                            }
                            return null;
                        }
                    }));
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirOperacaoExclusao(final ProfessorTitularDisciplinaTurmaLogVO obj) throws Exception {
        try {

            final String sql = "INSERT INTO professorTitularDisciplinaTurmaLog( semestre, ano, turma, disciplina, professor, titular, responsavel, operacao) " //51 - 54
                    + " VALUES ( ?, ?, ?, ?, ?, ?, ?, ?) returning codigo";  //39 - 54
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
                    PreparedStatement sqlInserir = cnctn.prepareStatement(sql);
                    int i = 0;
                    sqlInserir.setString(++i, obj.getSemestre());
                    sqlInserir.setString(++i, obj.getAno());
                    if (!obj.getTurma().equals(0)) {
                        sqlInserir.setInt(++i, obj.getTurma());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    if (!obj.getDisciplina().equals(0)) {
                        sqlInserir.setInt(++i, obj.getDisciplina());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    if (!obj.getProfessor().equals(0)) {
                        sqlInserir.setInt(++i, obj.getProfessor());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    sqlInserir.setBoolean(++i, obj.getTitular());
                    if (!obj.getResponsavel().equals(0)) {
                        sqlInserir.setInt(++i, obj.getResponsavel());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }

                    sqlInserir.setString(++i, obj.getOperacao());
                    return sqlInserir;
                }
            },
                    new ResultSetExtractor() {

                        public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                            if (rs.next()) {
                                obj.setNovoObj(Boolean.FALSE);
                                return rs.getInt("codigo");
                            }
                            return null;
                        }
                    }));
        } catch (Exception e) {
            throw e;
        }
    }
}
