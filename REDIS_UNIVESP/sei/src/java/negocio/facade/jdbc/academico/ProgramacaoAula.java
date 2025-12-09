package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
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

import negocio.comuns.academico.ProgramacaoAulaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.ProgramacaoAulaInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>ProgramacaoAulaVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>ProgramacaoAulaVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see ProgramacaoAulaVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class ProgramacaoAula extends ControleAcesso implements ProgramacaoAulaInterfaceFacade {

    protected static String idEntidade;

    public ProgramacaoAula() throws Exception {
        super();
        setIdEntidade("ProgramacaoAula");

    }

    /**
     * @see negocio.facade.jdbc.academico.ProgramacaoAulaInterfaceFacade#novo()
     */
    public ProgramacaoAulaVO novo() throws Exception {
        ProgramacaoAula.incluir(getIdEntidade());
        ProgramacaoAulaVO obj = new ProgramacaoAulaVO();
        return obj;
    }

    /**
     * @see negocio.facade.jdbc.academico.ProgramacaoAulaInterfaceFacade#validarQuantidadeAulas(negocio.comuns.academico.ProgramacaoAulaVO)
     */
    public void validarQuantidadeAulas(ProgramacaoAulaVO obj) throws Exception {
        if (obj.getAulaFim().intValue() > obj.getTurno().getNrAulas().intValue()) {
            throw new ConsistirException("Esse turno possui somente " + obj.getAulaFim().intValue() + " aulas, você deve informar um número de aulas inferior a este.");
        }
    }

    /**
     * @see negocio.facade.jdbc.academico.ProgramacaoAulaInterfaceFacade#validarSePeriodoAulaInicioFimJaExiste(negocio.comuns.academico.ProgramacaoAulaVO)
     */
    public void validarSePeriodoAulaInicioFimJaExiste(ProgramacaoAulaVO obj, UsuarioVO usuario) throws Exception {
        List programacoesAulas = this.consultarProgramacaoAulaPorTurmaPorDisciplinaPorProfessor(obj, false, Uteis.NIVELMONTARDADOS_TODOS, usuario);
        Iterator i = programacoesAulas.iterator();
        while (i.hasNext()) {
            ProgramacaoAulaVO p = (ProgramacaoAulaVO) i.next();
            if ((obj.getAulaInicio().intValue() >= p.getAulaInicio().intValue()) && (obj.getAulaInicio().intValue() <= p.getAulaFim().intValue())) {
                throw new ConsistirException("Essa turma já possui uma aula programada (" + p.getDisciplina().getNome() + ") para este horário neste dia da semana (" + p.getDiaSemana_Apresentar() + ").");
            }
            if ((obj.getAulaFim().intValue() <= p.getAulaFim().intValue()) && (obj.getAulaFim().intValue() >= p.getAulaInicio().intValue())) {
                throw new ConsistirException("Essa turma já possui uma aula programada (" + p.getDisciplina().getNome() + ") para este horário neste dia da semana (" + p.getDiaSemana_Apresentar() + ").");
            }
        }
    }

    /**
     * @see negocio.facade.jdbc.academico.ProgramacaoAulaInterfaceFacade#validarSeProfessorJaMinistraAulaNesseHorario(negocio.comuns.academico.ProgramacaoAulaVO)
     */
    public void validarSeProfessorJaMinistraAulaNesseHorario(ProgramacaoAulaVO obj, UsuarioVO usuario) throws Exception {
        List programacoesAulasProfessor = this.consultarProgramacaoAulaQueProfessorMinistra(obj, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
        Iterator i = programacoesAulasProfessor.iterator();
        while (i.hasNext()) {
            ProgramacaoAulaVO p = (ProgramacaoAulaVO) i.next();
            if (p.getCodigo().intValue() != obj.getCodigo().intValue()) {
                throw new ConsistirException("Esse professor já ministra aula neste horário na turma (" + p.getTurma().getIdentificadorTurma() + ").");
            }
        }
    }

    /**
     * @see negocio.facade.jdbc.academico.ProgramacaoAulaInterfaceFacade#consultarProgramacaoAulaQueProfessorMinistra(negocio.comuns.academico.ProgramacaoAulaVO, boolean, int)
     */
    public List consultarProgramacaoAulaQueProfessorMinistra(ProgramacaoAulaVO obj, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sql = "select * from programacaoAula where professor = " + obj.getProfessor().getCodigo() +
                " and diasemana = " + obj.getDiaSemana() + " and turno = " + obj.getTurno().getCodigo() + " and " +
                "((" + obj.getAulaInicio() + " >= aulainicio and " + obj.getAulaInicio() + " <= aulafim) or " +
                "(" + obj.getAulaFim() + " >= aulainicio and " + obj.getAulaFim() + " <= aulafim))";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    /**
     * @see negocio.facade.jdbc.academico.ProgramacaoAulaInterfaceFacade#consultarProgramacaoAulaQueProfessorMinistraPorProfessor(java.lang.Integer, java.lang.Integer, boolean, int)
     */
    public List consultarProgramacaoAulaQueProfessorMinistraPorProfessor(Integer professor, Integer turno, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sql = "select * from programacaoAula where professor = ? and turno = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{professor, turno});
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    /**
     * @see negocio.facade.jdbc.academico.ProgramacaoAulaInterfaceFacade#consultarProgramacaoAulaPorTurmaPorDisciplinaPorProfessor(negocio.comuns.academico.ProgramacaoAulaVO, boolean, int)
     */
    public List consultarProgramacaoAulaPorTurmaPorDisciplinaPorProfessor(ProgramacaoAulaVO obj, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sql = "SELECT * FROM ProgramacaoAula WHERE turma = ? and diasemana = ? and codigo != ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{obj.getTurma().getCodigo(), obj.getDiaSemana(), obj.getCodigo()});
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    /**
     * @see negocio.facade.jdbc.academico.ProgramacaoAulaInterfaceFacade#consultarProgramacaoAulaPorTurmaPorDisciplina(java.lang.Integer, java.lang.Integer, boolean, int)
     */
    public List consultarProgramacaoAulaPorTurmaPorDisciplina(Integer turma, Integer disciplina, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sql = "SELECT * FROM ProgramacaoAula WHERE turma = ? and disciplina = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{turma, disciplina});
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    /**
     * @see negocio.facade.jdbc.academico.ProgramacaoAulaInterfaceFacade#consultarProgramacaoAulaPorTurmaPorDisciplinaPorDiaSemana(java.lang.Integer, java.lang.Integer, java.lang.String, boolean, int)
     */
    public List consultarProgramacaoAulaPorTurmaPorDisciplinaPorDiaSemana(Integer turma, Integer disciplina, String diasemana, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sql = "SELECT * FROM ProgramacaoAula WHERE turma = " + turma + " and disciplina = " + disciplina + " and diasemana = '" + diasemana + "'";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    /**
     * @see negocio.facade.jdbc.academico.ProgramacaoAulaInterfaceFacade#consultarProgramacaoAulaPorTurmaPorDisciplinaPorDiaSemanaPorPeriodo(java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.Integer, java.lang.Integer, boolean, boolean, int)
     */
    public ProgramacaoAulaVO consultarProgramacaoAulaPorTurmaPorDisciplinaPorDiaSemanaPorPeriodo(Integer turma, Integer disciplina, String diasemana, Integer aulaInicio, Integer aulaFim, boolean programada, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sql;
        if (programada) {
            sql = "SELECT * FROM ProgramacaoAula WHERE turma = " + turma + " and disciplina = " + turma + " and diasemana = " + diasemana + " and (aulainicio <= " + aulaInicio + " and " + aulaInicio + " <= aulafim) and (" + aulaFim + " >= aulainicio and aulafim >= " + aulaFim + ")";
        } else {
            sql = "SELECT * FROM ProgramacaoAula WHERE turma = " + turma + " and disciplina = " + disciplina + "";
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
        if (!tabelaResultado.next()) {
            return null;
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * @see negocio.facade.jdbc.academico.ProgramacaoAulaInterfaceFacade#incluir(negocio.comuns.academico.ProgramacaoAulaVO)
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final ProgramacaoAulaVO obj, UsuarioVO usuario) throws Exception {
        try {
            ProgramacaoAulaVO.validarDados(obj);
            validarSePeriodoAulaInicioFimJaExiste(obj, usuario);
            validarSeProfessorTemDisponibilidadeNesseHorario(obj);
            validarSeProfessorJaMinistraAulaNesseHorario(obj, usuario);
            validarQuantidadeAulas(obj);
            ProgramacaoAula.incluir(getIdEntidade(), true, usuario);
            final String sql = "INSERT INTO ProgramacaoAula( turma, disciplina, professor, diaSemana, aulaInicio, aulaFim, local, laboratorial, turno ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    sqlInserir.setInt(1, obj.getTurma().getCodigo().intValue());
                    sqlInserir.setInt(2, obj.getDisciplina().getCodigo().intValue());
                    sqlInserir.setInt(3, obj.getProfessor().getCodigo().intValue());
                    sqlInserir.setString(4, obj.getDiaSemana());
                    sqlInserir.setInt(5, obj.getAulaInicio().intValue());
                    sqlInserir.setInt(6, obj.getAulaFim().intValue());
                    sqlInserir.setString(7, obj.getLocal());
                    sqlInserir.setBoolean(8, obj.isLaboratorial().booleanValue());
                    sqlInserir.setInt(9, obj.getTurno().getCodigo().intValue());
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


        } catch (Exception e) {
            obj.setNovoObj(Boolean.TRUE);
            throw e;
        }
    }

    /**
     * @see negocio.facade.jdbc.academico.ProgramacaoAulaInterfaceFacade#alterar(negocio.comuns.academico.ProgramacaoAulaVO)
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final ProgramacaoAulaVO obj, UsuarioVO usuario) throws Exception {
        try {
            ProgramacaoAulaVO.validarDados(obj);
            validarSePeriodoAulaInicioFimJaExiste(obj, usuario);
            validarSeProfessorTemDisponibilidadeNesseHorario(obj);
            validarSeProfessorJaMinistraAulaNesseHorario(obj, usuario);
            validarQuantidadeAulas(obj);
            ProgramacaoAula.alterar(getIdEntidade(), true, usuario);
            final String sql = "UPDATE ProgramacaoAula set turma=?, disciplina=?, professor=?, diaSemana=?, aulaInicio=?, aulaFim=?, local=?, laboratorial=?, turno=? WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setInt(1, obj.getTurma().getCodigo().intValue());
                    sqlAlterar.setInt(2, obj.getDisciplina().getCodigo().intValue());
                    sqlAlterar.setInt(3, obj.getProfessor().getCodigo().intValue());
                    sqlAlterar.setString(4, obj.getDiaSemana());
                    sqlAlterar.setInt(5, obj.getAulaInicio().intValue());
                    sqlAlterar.setInt(6, obj.getAulaFim().intValue());
                    sqlAlterar.setString(7, obj.getLocal());
                    sqlAlterar.setBoolean(8, obj.isLaboratorial().booleanValue());
                    sqlAlterar.setInt(9, obj.getTurno().getCodigo().intValue());
                    sqlAlterar.setInt(10, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * @see negocio.facade.jdbc.academico.ProgramacaoAulaInterfaceFacade#excluir(negocio.comuns.academico.ProgramacaoAulaVO)
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(ProgramacaoAulaVO obj, UsuarioVO usuarioVO) throws Exception {
        try {
            ProgramacaoAula.excluir(getIdEntidade(), true, usuarioVO);
            String sql = "DELETE FROM ProgramacaoAula WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * @see negocio.facade.jdbc.academico.ProgramacaoAulaInterfaceFacade#consultarPorTurma(java.lang.Integer, boolean, int)
     */
    public List consultarPorTurma(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM ProgramacaoAula WHERE turma = " + valorConsulta.intValue();
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * @see negocio.facade.jdbc.academico.ProgramacaoAulaInterfaceFacade#consultarPorLocal(java.lang.String, boolean, int)
     */
    public List consultarPorLocal(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM ProgramacaoAula WHERE local like('" + valorConsulta + "%') ORDER BY local";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * @see negocio.facade.jdbc.academico.ProgramacaoAulaInterfaceFacade#consultarPorDiaSemana(java.lang.String, boolean, int)
     */
    public List consultarPorDiaSemana(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM ProgramacaoAula WHERE diaSemana like('" + valorConsulta + "%') ORDER BY diaSemana";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * @see negocio.facade.jdbc.academico.ProgramacaoAulaInterfaceFacade#consultarPorNomePessoa(java.lang.String, boolean, int)
     */
    public List consultarPorNomePessoa(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT ProgramacaoAula.* FROM ProgramacaoAula, Pessoa WHERE ProgramacaoAula.professor = Pessoa.codigo and Pessoa.nome like('" + valorConsulta + "%') ORDER BY Pessoa.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    /**
     * @see negocio.facade.jdbc.academico.ProgramacaoAulaInterfaceFacade#consultarPorNomeDisciplina(java.lang.String, boolean, int)
     */
    public List consultarPorNomeDisciplina(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT ProgramacaoAula.* FROM ProgramacaoAula, Disciplina WHERE ProgramacaoAula.disciplina = Disciplina.codigo and Disciplina.nome like('" + valorConsulta + "%') ORDER BY Disciplina.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    /**
     * @see negocio.facade.jdbc.academico.ProgramacaoAulaInterfaceFacade#consultarPorIdentificadorTurmaTurma(java.lang.String, boolean, int)
     */
    public List consultarPorIdentificadorTurmaTurma(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT ProgramacaoAula.* FROM ProgramacaoAula, Turma WHERE ProgramacaoAula.turma = Turma.codigo and Turma.identificadorTurma like('" + valorConsulta + "%') ORDER BY Turma.identificadorTurma";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    /**
     * @see negocio.facade.jdbc.academico.ProgramacaoAulaInterfaceFacade#consultarPorCodigo(java.lang.Integer, boolean, int)
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM ProgramacaoAula WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>ProgramacaoAulaVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>ProgramacaoAulaVO</code>.
     * @return  O objeto da classe <code>ProgramacaoAulaVO</code> com os dados devidamente montados.
     */
    public static ProgramacaoAulaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ProgramacaoAulaVO obj = new ProgramacaoAulaVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.getTurma().setCodigo(new Integer(dadosSQL.getInt("turma")));
        obj.getTurno().setCodigo(new Integer(dadosSQL.getInt("turno")));
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
            return obj;
        }
        obj.getDisciplina().setCodigo(new Integer(dadosSQL.getInt("disciplina")));
        obj.getProfessor().setCodigo(new Integer(dadosSQL.getInt("professor")));
        obj.setDiaSemana(dadosSQL.getString("diaSemana"));
        obj.setAulaInicio(new Integer(dadosSQL.getInt("aulaInicio")));
        obj.setAulaFim(new Integer(dadosSQL.getInt("aulaFim")));
        obj.setLocal(dadosSQL.getString("local"));
        obj.setLaboratorial(new Boolean(dadosSQL.getBoolean("laboratorial")));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }

        montarDadosTurma(obj, nivelMontarDados, usuario);
        montarDadosDisciplina(obj, nivelMontarDados, usuario);
        montarDadosProfessor(obj, nivelMontarDados, usuario);
        montarDadosTurno(obj, nivelMontarDados, usuario);
        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>TurnoVO</code> relacionado ao objeto <code>ProgramacaoAulaVO</code>.
     * Faz uso da chave primária da classe <code>TurnoVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosTurno(ProgramacaoAulaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getTurno().getCodigo().intValue() == 0) {
            return;
        }
        obj.setTurno(getFacadeFactory().getTurnoFacade().consultarPorChavePrimaria(obj.getTurno().getCodigo(), nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>PessoaVO</code> relacionado ao objeto <code>ProgramacaoAulaVO</code>.
     * Faz uso da chave primária da classe <code>PessoaVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosProfessor(ProgramacaoAulaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getProfessor().getCodigo().intValue() == 0) {
            return;
        }
        obj.setProfessor(getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(obj.getProfessor().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>DisciplinaVO</code> relacionado ao objeto <code>ProgramacaoAulaVO</code>.
     * Faz uso da chave primária da classe <code>DisciplinaVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosDisciplina(ProgramacaoAulaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getDisciplina().getCodigo().intValue() == 0) {
            return;
        }
        obj.setDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(obj.getDisciplina().getCodigo(), nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>TurmaVO</code> relacionado ao objeto <code>ProgramacaoAulaVO</code>.
     * Faz uso da chave primária da classe <code>TurmaVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosTurma(ProgramacaoAulaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getTurma().getCodigo().intValue() == 0) {
            return;
        }
        obj.setTurma(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getTurma().getCodigo(), nivelMontarDados, usuario));
    }

    /**
     * @see negocio.facade.jdbc.academico.ProgramacaoAulaInterfaceFacade#consultarPorChavePrimaria(java.lang.Integer, int)
     */
    public ProgramacaoAulaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM ProgramacaoAula WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return ProgramacaoAula.idEntidade;
    }

    /**
     * @see negocio.facade.jdbc.academico.ProgramacaoAulaInterfaceFacade#setIdEntidade(java.lang.String)
     */
    public void setIdEntidade(String idEntidade) {
        ProgramacaoAula.idEntidade = idEntidade;
    }

    public void validarSeProfessorTemDisponibilidadeNesseHorario(ProgramacaoAulaVO obj) throws Exception {
//      List l = getFacadeFactory().getDisponibilidadeHorarioFacade().consultarDisponibilidadeHorarioProfessorNoHorarioProgramadoAula(obj.getProfessor().getCodigo(), 
//              obj.getDiaSemana(),obj.getTurno().getCodigo(),obj.get);
//      Iterator i = l.iterator();
//      if (!i.hasNext()) {
//          throw new ConsistirException("O professor não indicou disponibilidade para este horário.");            
//      }
    }

    
}
