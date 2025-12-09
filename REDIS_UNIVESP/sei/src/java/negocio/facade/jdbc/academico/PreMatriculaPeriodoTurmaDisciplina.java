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

import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.TurmaDisciplinaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.PreMatriculaPeriodoTurmaDisciplinaInterfaceFacade;

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
public class PreMatriculaPeriodoTurmaDisciplina extends ControleAcesso implements PreMatriculaPeriodoTurmaDisciplinaInterfaceFacade {

    protected static String idEntidade;

    public PreMatriculaPeriodoTurmaDisciplina() throws Exception {
        super();
        setIdEntidade("Matricula");
    }

    /* (non-Javadoc)
     * @see negocio.facade.jdbc.academico.PreMatriculaPeriodoTurmaDisciplinaInterfaceFacade#novo()
     */
    public MatriculaPeriodoTurmaDisciplinaVO novo() throws Exception {
        PreMatriculaPeriodoTurmaDisciplina.incluir(getIdEntidade());
        MatriculaPeriodoTurmaDisciplinaVO obj = new MatriculaPeriodoTurmaDisciplinaVO();
        return obj;
    }

    /* (non-Javadoc)
     * @see negocio.facade.jdbc.academico.PreMatriculaPeriodoTurmaDisciplinaInterfaceFacade#incluir(negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO)
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final MatriculaPeriodoTurmaDisciplinaVO obj) throws Exception {
        MatriculaPeriodoTurmaDisciplinaVO.validarDados(obj);
        final String sql = "INSERT INTO PreMatriculaPeriodoTurmaDisciplina( matriculaPeriodo, turma, disciplina, semestre, ano, matricula, disciplinaIncluida ) VALUES (?, ?, ?, ?, ?, ?, ? ) returning codigo";
        obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                sqlInserir.setInt(1, obj.getMatriculaPeriodo().intValue());
                if (obj.getTurma().getCodigo().intValue() != 0) {
                    sqlInserir.setInt(2, obj.getTurma().getCodigo().intValue());
                } else {
                    sqlInserir.setNull(2, 0);
                }
                sqlInserir.setInt(3, obj.getDisciplina().getCodigo().intValue());
                sqlInserir.setString(4, obj.getSemestre());
                sqlInserir.setString(5, obj.getAno());
                sqlInserir.setString(6, obj.getMatricula());
                sqlInserir.setBoolean(7, obj.getDisciplinaIncluida());
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

    /* (non-Javadoc)
     * @see negocio.facade.jdbc.academico.PreMatriculaPeriodoTurmaDisciplinaInterfaceFacade#alterar(negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO)
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final MatriculaPeriodoTurmaDisciplinaVO obj) throws Exception {
        MatriculaPeriodoTurmaDisciplinaVO.validarDados(obj);
        final String sql = "UPDATE PreMatriculaPeriodoTurmaDisciplina set matriculaPeriodo=?, turma=?, disciplina=?, semestre=?, ano=?, matricula=?, disciplinaIncluida=? WHERE ((codigo = ?))";
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                sqlAlterar.setInt(1, obj.getMatriculaPeriodo().intValue());
                if (obj.getTurma().getCodigo().intValue() != 0) {
                    sqlAlterar.setInt(2, obj.getTurma().getCodigo().intValue());
                } else {
                    sqlAlterar.setNull(2, 0);
                }
                sqlAlterar.setInt(3, obj.getDisciplina().getCodigo().intValue());
                sqlAlterar.setString(4, obj.getSemestre());
                sqlAlterar.setString(5, obj.getAno());
                sqlAlterar.setString(6, obj.getMatricula());
                sqlAlterar.setBoolean(7, obj.getDisciplinaIncluida());
                sqlAlterar.setInt(8, obj.getCodigo().intValue());
                return sqlAlterar;
            }
        });
    }

    /* (non-Javadoc)
     * @see negocio.facade.jdbc.academico.PreMatriculaPeriodoTurmaDisciplinaInterfaceFacade#excluir(negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO)
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(MatriculaPeriodoTurmaDisciplinaVO obj) throws Exception {
        PreMatriculaPeriodoTurmaDisciplina.excluir(getIdEntidade());
        String sql = "DELETE FROM PreMatriculaPeriodoTurmaDisciplina WHERE ((codigo = ?))";
        getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
    }

    /* (non-Javadoc)
     * @see negocio.facade.jdbc.academico.PreMatriculaPeriodoTurmaDisciplinaInterfaceFacade#consultarPorDisciplina(java.lang.Integer, boolean, int)
     */
    public List consultarPorDisciplina(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM PreMatriculaPeriodoTurmaDisciplina WHERE disciplina = " + valorConsulta.intValue() + " ORDER BY disciplina";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
    }

    /* (non-Javadoc)
     * @see negocio.facade.jdbc.academico.PreMatriculaPeriodoTurmaDisciplinaInterfaceFacade#consultarPorMatriculaAtiva(java.lang.String, boolean, int)
     */
    public List<MatriculaPeriodoTurmaDisciplinaVO> consultarPorMatriculaAtiva(String matricula, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("select distinct preMatriculaPeriodoTurmaDisciplina.*");
        sqlStr.append(" from matricula  inner join matriculaPeriodo on  matricula.matricula = matriculaPeriodo.matricula ");
        sqlStr.append(" inner join matriculaPeriodoTurmaDisciplina on matriculaPeriodoTurmaDisciplina.matriculaPeriodo = MatriculaPeriodo.codigo");
        sqlStr.append(" where matriculaPeriodo.situacaoMatriculaPeriodo='AT' and matricula.matricula = '" + matricula + "'");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
    }

    /* (non-Javadoc)
     * @see negocio.facade.jdbc.academico.PreMatriculaPeriodoTurmaDisciplinaInterfaceFacade#consultarPorIdentificadorTurmaTurma(java.lang.String, boolean, int)
     */
    public List consultarPorIdentificadorTurmaTurma(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT PreMatriculaPeriodoTurmaDisciplina.* FROM PreMatriculaPeriodoTurmaDisciplina, Turma WHERE PreMatriculaPeriodoTurmaDisciplina.turma = Turma.codigo and upper( Turma.identificadorTurma ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY Turma.identificadorTurma";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario);
    }

    /* (non-Javadoc)
     * @see negocio.facade.jdbc.academico.PreMatriculaPeriodoTurmaDisciplinaInterfaceFacade#consultarPorCodigoTurmaDisciplinaSemestreAno(java.lang.Integer, java.lang.Integer, java.lang.Boolean, java.lang.String, java.lang.String, boolean, int)
     */
    public List consultarPorCodigoTurmaDisciplinaSemestreAno(Integer turma, Integer disciplina, Boolean turmaAgrupada, String ano, String semestre, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        if (ano == null) {
            ano = "";
        }
        if (semestre == null) {
            semestre = "";
        }
        StringBuilder sqlStr = new StringBuilder("SELECT PreMatriculaPeriodoTurmaDisciplina.* FROM PreMatriculaPeriodoTurmaDisciplina, Turma, Disciplina");
        sqlStr.append(" WHERE PreMatriculaPeriodoTurmaDisciplina.turma = Turma.codigo and ");
        if (turmaAgrupada) {
            sqlStr.append(" Turma.codigo  in ( select turma from turmaAgrupada where turmaOrigem =  " + turma + ")");
        } else {
            sqlStr.append(" Turma.codigo  = " + turma.intValue());
        }
        sqlStr.append(" and PreMatriculaPeriodoTurmaDisciplina.disciplina = disciplina.codigo and disciplina.codigo = " + disciplina.intValue());
        sqlStr.append(" and PreMatriculaPeriodoTurmaDisciplina.ano = '" + ano + "' and PreMatriculaPeriodoTurmaDisciplina.semestre = '" + semestre + "'");
        sqlStr.append(" ORDER BY Turma.codigo");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario);
    }

    /* (non-Javadoc)
     * @see negocio.facade.jdbc.academico.PreMatriculaPeriodoTurmaDisciplinaInterfaceFacade#consultarPorMatriculaPeriodo(java.lang.Integer, boolean, int)
     */
    public List consultarPorMatriculaPeriodo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM PreMatriculaPeriodoTurmaDisciplina WHERE matriculaPeriodo = " + valorConsulta.intValue() + " ORDER BY matriculaPeriodo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
    }

    /* (non-Javadoc)
     * @see negocio.facade.jdbc.academico.PreMatriculaPeriodoTurmaDisciplinaInterfaceFacade#consultarPorMatriculaPeriodoDisciplinaSemestreAno(java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String, boolean, int)
     */
    public MatriculaPeriodoTurmaDisciplinaVO consultarPorMatriculaPeriodoDisciplinaSemestreAno(Integer valorConsulta, Integer disciplina, String semestre, String ano, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        if (semestre == null) {
            semestre = "";
        }
        if (ano == null) {
            ano = "";
        }
        String sqlStr = "SELECT * FROM PreMatriculaPeriodoTurmaDisciplina WHERE matriculaPeriodo = " + valorConsulta.intValue() + " and disciplina= " + disciplina.intValue() + " and semestre = '" + semestre + "' and ano = '" + ano + "' ORDER BY matriculaPeriodo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        if (!tabelaResultado.next()) {
            return null;
        }
        return (montarDados(tabelaResultado, nivelMontarDados,usuario));
    }

    /* (non-Javadoc)
     * @see negocio.facade.jdbc.academico.PreMatriculaPeriodoTurmaDisciplinaInterfaceFacade#consultarPorMatricula(java.lang.String, boolean, int)
     */
    public List consultarPorMatricula(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM PreMatriculaPeriodoTurmaDisciplina WHERE lower (matriculaPeriodo) = " + valorConsulta.toLowerCase() + " ORDER BY matricula";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
    }

    /* (non-Javadoc)
     * @see negocio.facade.jdbc.academico.PreMatriculaPeriodoTurmaDisciplinaInterfaceFacade#consultarPorCodigo(java.lang.Integer, boolean, int)
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM PreMatriculaPeriodoTurmaDisciplina WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>MatriculaPeriodoTurmaDisciplinaVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            MatriculaPeriodoTurmaDisciplinaVO obj = new MatriculaPeriodoTurmaDisciplinaVO();
            obj = montarDados(tabelaResultado, nivelMontarDados,usuario);
            vetResultado.add(obj);
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>MatriculaPeriodoTurmaDisciplinaVO</code>.
     * @return  O objeto da classe <code>MatriculaPeriodoTurmaDisciplinaVO</code> com os dados devidamente montados.
     */
    public static MatriculaPeriodoTurmaDisciplinaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        MatriculaPeriodoTurmaDisciplinaVO obj = new MatriculaPeriodoTurmaDisciplinaVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setMatriculaPeriodo(new Integer(dadosSQL.getInt("matriculaPeriodo")));
        obj.getTurma().setCodigo(new Integer(dadosSQL.getInt("turma")));
        obj.getDisciplina().setCodigo(new Integer(dadosSQL.getInt("disciplina")));
        obj.setAno(dadosSQL.getString("ano"));
        obj.setSemestre(dadosSQL.getString("semestre"));
        obj.setMatricula(dadosSQL.getString("matricula"));
        obj.setDisciplinaIncluida(dadosSQL.getBoolean("disciplinaIncluida"));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        montarDadosTurma(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,usuario);
        montarDadosDisciplina(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,usuario);
        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>TurmaVO</code> relacionado ao objeto <code>MatriculaPeriodoTurmaDisciplinaVO</code>.
     * Faz uso da chave primária da classe <code>TurmaVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosTurma(MatriculaPeriodoTurmaDisciplinaVO obj, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if (obj.getTurma().getCodigo().intValue() == 0) {
            return;
        }
        obj.setTurma(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getTurma().getCodigo(), nivelMontarDados,usuario));
    }

    public static void montarDadosDisciplina(MatriculaPeriodoTurmaDisciplinaVO obj, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if (obj.getDisciplina().getCodigo().intValue() == 0) {
            return;
        }
        obj.setDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(obj.getDisciplina().getCodigo(), nivelMontarDados,usuario));
    }

    /* (non-Javadoc)
     * @see negocio.facade.jdbc.academico.PreMatriculaPeriodoTurmaDisciplinaInterfaceFacade#retirarReservaTurmaDisciplina(java.util.List)
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void retirarReservaTurmaDisciplina(List listaItems,UsuarioVO usuario) throws Exception {
        Iterator i = listaItems.iterator();
        while (i.hasNext()) {
            MatriculaPeriodoTurmaDisciplinaVO obj = (MatriculaPeriodoTurmaDisciplinaVO) i.next();
            TurmaDisciplinaVO turmaDisciplina = getFacadeFactory().getTurmaDisciplinaFacade().consultarPorCodigoTurmaCodigoDisciplina(obj.getTurma().getCodigo(), obj.getDisciplina().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,usuario);
            if (turmaDisciplina.getCodigo().intValue() != 0) {
                if (turmaDisciplina.getNrAlunosMatriculados() > 0) {
                    turmaDisciplina.setNrAlunosMatriculados(turmaDisciplina.getNrAlunosMatriculados() - new Integer(1));
                    getFacadeFactory().getTurmaDisciplinaFacade().alterar(turmaDisciplina, usuario);
                }
            }
        }
    }

    /* (non-Javadoc)
     * @see negocio.facade.jdbc.academico.PreMatriculaPeriodoTurmaDisciplinaInterfaceFacade#excluirPreMatriculaPeriodoTurmaDisciplinas(java.lang.Integer, java.util.List)
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirPreMatriculaPeriodoTurmaDisciplinas(Integer matriculaPeriodo, List objetos) throws Exception {
        String sql = "DELETE FROM PreMatriculaPeriodoTurmaDisciplina WHERE (matriculaPeriodo = ?)";
        Iterator i = objetos.iterator();
        while (i.hasNext()) {
            MatriculaPeriodoTurmaDisciplinaVO obj = (MatriculaPeriodoTurmaDisciplinaVO) i.next();
            if (obj.getCodigo().intValue() != 0) {
                sql += " and (codigo != " + obj.getCodigo() + ") ";
            }
        }
        MatriculaPeriodo.excluir(getIdEntidade());
        getConexao().getJdbcTemplate().update(sql, new Object[]{matriculaPeriodo});
    }

    /* (non-Javadoc)
     * @see negocio.facade.jdbc.academico.PreMatriculaPeriodoTurmaDisciplinaInterfaceFacade#alterarPreMatriculaPeriodos(java.lang.Integer, java.lang.String, java.util.List)
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarPreMatriculaPeriodos(Integer matriculaPeriodo, String matricula, List objetos) throws Exception {
        excluirPreMatriculaPeriodoTurmaDisciplinas(matriculaPeriodo, objetos);
        incluirPreMatriculaPeriodoTurmaDisciplinas(matriculaPeriodo, matricula, objetos);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirPreMatriculaPeriodosForaPrazo(Integer matriculaPeriodo, String matricula, List objetos) throws Exception {
        //excluirPreMatriculaPeriodoTurmaDisciplinas(matriculaPeriodo, objetos);
        incluirPreMatriculaPeriodoTurmaDisciplinasForaPrazo(matriculaPeriodo, matricula, objetos);
    }

    /* (non-Javadoc)
     * @see negocio.facade.jdbc.academico.PreMatriculaPeriodoTurmaDisciplinaInterfaceFacade#incluirPreMatriculaPeriodoTurmaDisciplinas(java.lang.Integer, java.lang.String, java.util.List)
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirPreMatriculaPeriodoTurmaDisciplinasForaPrazo(Integer matriculaPrm, String matricula, List objetos) throws Exception {
        //excluirPreMatriculaPeriodoTurmaDisciplinas(matriculaPrm, objetos);
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            MatriculaPeriodoTurmaDisciplinaVO obj = (MatriculaPeriodoTurmaDisciplinaVO) e.next();
            obj.setMatricula(matricula);
            obj.setMatriculaPeriodo(matriculaPrm);
            if (obj.getCodigo().intValue() != 0) {
                //alterar(obj);
            } else {
                incluir(obj);
            }
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirPreMatriculaPeriodoTurmaDisciplinas(Integer matriculaPrm, String matricula, List objetos) throws Exception {
        excluirPreMatriculaPeriodoTurmaDisciplinas(matriculaPrm, objetos);
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            MatriculaPeriodoTurmaDisciplinaVO obj = (MatriculaPeriodoTurmaDisciplinaVO) e.next();
            obj.setMatricula(matricula);
            obj.setMatriculaPeriodo(matriculaPrm);
            if (obj.getCodigo().intValue() != 0) {
                alterar(obj);
            } else {
                incluir(obj);
            }
        }
    }

    /**
     * Operação responsável por consultar todos os <code>MatriculaPeriodoVO</code> relacionados a um objeto da classe <code>academico.Matricula</code>.
     * @param matricula  Atributo de <code>academico.Matricula</code> a ser utilizado para localizar os objetos da classe <code>MatriculaPeriodoVO</code>.
     * @return List  Contendo todos os objetos da classe <code>MatriculaPeriodoVO</code> resultantes da consulta.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public static List consultarPreMatriculaPeriodoTurmaDisciplinas(Integer matriculaPeriodo, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        MatriculaPeriodo.consultar(getIdEntidade(), controlarAcesso,usuario);
        List objetos = new ArrayList(0);
        String sqlStr = "SELECT * FROM PreMatriculaPeriodoTurmaDisciplina WHERE matriculaPeriodo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[]{matriculaPeriodo});
        while (tabelaResultado.next()) {
            MatriculaPeriodoTurmaDisciplinaVO novoObj = new MatriculaPeriodoTurmaDisciplinaVO();
            novoObj = PreMatriculaPeriodoTurmaDisciplina.montarDados(tabelaResultado, nivelMontarDados,usuario);
            objetos.add(novoObj);
        }
        return objetos;
    }

    /* (non-Javadoc)
     * @see negocio.facade.jdbc.academico.PreMatriculaPeriodoTurmaDisciplinaInterfaceFacade#consultarPorChavePrimaria(java.lang.Integer, int)
     */
    public MatriculaPeriodoTurmaDisciplinaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false,usuario);
        String sqlStr = "SELECT * FROM PreMatriculaPeriodoTurmaDisciplina WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( PreMatriculaPeriodoTurmaDisciplina ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados,usuario));
    }

    /* (non-Javadoc)
     * @see negocio.facade.jdbc.academico.PreMatriculaPeriodoTurmaDisciplinaInterfaceFacade#consultarDisciplinaDoAlunoPorMatricula(java.lang.Integer, java.lang.String, int)
     */
    public List consultarDisciplinaDoAlunoPorMatricula(Integer unidadeEnsino, String matricula, int nivelMontarDados,UsuarioVO usuario) throws SQLException, Exception {
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append(" SELECT prematriculaperiodoturmadisciplina.* from preMatriculaperiodoturmadisciplina");
        sqlStr.append(" INNER JOIN matricula ON (matricula.matricula = prematriculaperiodoturmadisciplina.matricula)");
        sqlStr.append(" INNER JOIN pessoa ON (matricula.aluno = pessoa.codigo)");
        sqlStr.append(" WHERE prematriculaperiodoturmadisciplina.matricula = '" + matricula + "'");
        if (unidadeEnsino != null && unidadeEnsino.intValue() != 0) {
            sqlStr.append(" AND matricula.unidadeensino = " + unidadeEnsino);
        }
        sqlStr.append(" ORDER BY prematriculaperiodoturmadisciplina.disciplina; ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        List listaResultado = montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario);
        return listaResultado;
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return PreMatriculaPeriodoTurmaDisciplina.idEntidade;
    }

    /* (non-Javadoc)
     * @see negocio.facade.jdbc.academico.PreMatriculaPeriodoTurmaDisciplinaInterfaceFacade#setIdEntidade(java.lang.String)
     */
    public void setIdEntidade(String idEntidade) {
        PreMatriculaPeriodoTurmaDisciplina.idEntidade = idEntidade;
    }
}
