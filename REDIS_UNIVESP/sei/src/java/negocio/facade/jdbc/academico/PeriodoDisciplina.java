package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.PeriodoDisciplinaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.PeriodoDisciplinaInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>PeriodoDisciplinaVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>PeriodoDisciplinaVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see PeriodoDisciplinaVO
 * @see ControleAcesso
 * @see PeriodoLetivo
 */
@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy 
public class PeriodoDisciplina extends ControleAcesso implements PeriodoDisciplinaInterfaceFacade {

    protected static String idEntidade;

    public PeriodoDisciplina() throws Exception {
        super();
        setIdEntidade("PeriodoDisciplina");
    }

    /* (non-Javadoc)
     * @see negocio.facade.jdbc.academico.PeriodoDisciplinaInterfaceFacade#novo()
     */
    public PeriodoDisciplinaVO novo() throws Exception {
        PeriodoDisciplina.incluir(getIdEntidade());
        PeriodoDisciplinaVO obj = new PeriodoDisciplinaVO();
        return obj;
    }

    /* (non-Javadoc)
     * @see negocio.facade.jdbc.academico.PeriodoDisciplinaInterfaceFacade#incluir(negocio.comuns.academico.PeriodoDisciplinaVO)
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final PeriodoDisciplinaVO obj) throws Exception {
        PeriodoDisciplinaVO.validarDados(obj);
        final String sql = "INSERT INTO PeriodoDisciplina( periodoLetivo, disciplina ) VALUES ( ?, ? )";
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                sqlInserir.setInt(1, obj.getPeriodoLetivo().intValue());
                sqlInserir.setInt(2, obj.getDisciplina().getCodigo().intValue());
                return sqlInserir;
            }
        });
        obj.setNovoObj(Boolean.FALSE);
    }

    /* (non-Javadoc)
     * @see negocio.facade.jdbc.academico.PeriodoDisciplinaInterfaceFacade#alterar(negocio.comuns.academico.PeriodoDisciplinaVO)
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final PeriodoDisciplinaVO obj) throws Exception {
        PeriodoDisciplinaVO.validarDados(obj);
        final String sql = "UPDATE PeriodoDisciplina set Where((periodoLetivo = ?) and (disciplina = ?))";
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                sqlAlterar.setInt(1, obj.getPeriodoLetivo().intValue());
                sqlAlterar.setInt(2, obj.getDisciplina().getCodigo().intValue());
                return sqlAlterar;
            }
        });
    }

    /* (non-Javadoc)
     * @see negocio.facade.jdbc.academico.PeriodoDisciplinaInterfaceFacade#excluir(negocio.comuns.academico.PeriodoDisciplinaVO)
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(PeriodoDisciplinaVO obj) throws Exception {
        PeriodoDisciplina.excluir(getIdEntidade());
        String sql = "DELETE FROM PeriodoDisciplina WHERE ((periodoLetivo = ?) and (disciplina = ?))";
        getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getPeriodoLetivo(), obj.getDisciplina().getCodigo()});
    }

    /* (non-Javadoc)
     * @see negocio.facade.jdbc.academico.PeriodoDisciplinaInterfaceFacade#consultarPorNomeDisciplina(java.lang.String, boolean)
     */
    public List consultarPorNomeDisciplina(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT PeriodoDisciplina.* FROM PeriodoDisciplina, Disciplina WHERE PeriodoDisciplina.disciplina = Disciplina.codigo and Disciplina.nome like('" + valorConsulta + "%') ORDER BY Disciplina.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, usuario);
    }

    /* (non-Javadoc)
     * @see negocio.facade.jdbc.academico.PeriodoDisciplinaInterfaceFacade#consultarPorSiglaPeriodoLetivo(java.lang.String, boolean)
     */
    public List consultarPorSiglaPeriodoLetivo(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT PeriodoDisciplina.* FROM PeriodoDisciplina, PeriodoLetivo WHERE PeriodoDisciplina.periodoLetivo = PeriodoLetivo.codigo and PeriodoLetivo.sigla like('" + valorConsulta + "%') ORDER BY PeriodoLetivo.sigla";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, usuario);
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>PeriodoDisciplinaVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            PeriodoDisciplinaVO obj = new PeriodoDisciplinaVO();
            obj = montarDados(tabelaResultado, usuario);
            vetResultado.add(obj);
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>PeriodoDisciplinaVO</code>.
     * @return  O objeto da classe <code>PeriodoDisciplinaVO</code> com os dados devidamente montados.
     */
    public static PeriodoDisciplinaVO montarDados(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
        PeriodoDisciplinaVO obj = new PeriodoDisciplinaVO();
        obj.setPeriodoLetivo(new Integer(dadosSQL.getInt("periodoLetivo")));
        obj.getDisciplina().setCodigo(new Integer(dadosSQL.getInt("disciplina")));
        obj.setNovoObj(Boolean.FALSE);
        montarDadosDisciplina(obj, usuario);
        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>DisciplinaVO</code> relacionado ao objeto <code>PeriodoDisciplinaVO</code>.
     * Faz uso da chave primária da classe <code>DisciplinaVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosDisciplina(PeriodoDisciplinaVO obj, UsuarioVO usuario) throws Exception {
        if (obj.getDisciplina().getCodigo().intValue() == 0) {
            return;
        }
        obj.setDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(obj.getDisciplina().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
    }

    /* (non-Javadoc)
     * @see negocio.facade.jdbc.academico.PeriodoDisciplinaInterfaceFacade#excluirPeriodoDisciplinas(java.lang.Integer)
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirPeriodoDisciplinas(Integer periodoLetivo) throws Exception {
        PeriodoDisciplina.excluir(getIdEntidade());
        String sql = "DELETE FROM PeriodoDisciplina WHERE (periodoLetivo = ?)";
        getConexao().getJdbcTemplate().update(sql, new Object[]{periodoLetivo});
    }

    /* (non-Javadoc)
     * @see negocio.facade.jdbc.academico.PeriodoDisciplinaInterfaceFacade#alterarPeriodoDisciplinas(java.lang.Integer, java.util.List)
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarPeriodoDisciplinas(Integer periodoLetivo, List objetos) throws Exception {
        excluirPeriodoDisciplinas(periodoLetivo);
        incluirPeriodoDisciplinas(periodoLetivo, objetos);
    }

    /* (non-Javadoc)
     * @see negocio.facade.jdbc.academico.PeriodoDisciplinaInterfaceFacade#incluirPeriodoDisciplinas(java.lang.Integer, java.util.List)
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirPeriodoDisciplinas(Integer periodoLetivoPrm, List objetos) throws Exception {
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            PeriodoDisciplinaVO obj = (PeriodoDisciplinaVO) e.next();
            obj.setPeriodoLetivo(periodoLetivoPrm);
            incluir(obj);
        }
    }

    /**
     * Operação responsável por consultar todos os <code>PeriodoDisciplinaVO</code> relacionados a um objeto da classe <code>academico.PeriodoLetivo</code>.
     * @param periodoLetivo  Atributo de <code>academico.PeriodoLetivo</code> a ser utilizado para localizar os objetos da classe <code>PeriodoDisciplinaVO</code>.
     * @return List  Contendo todos os objetos da classe <code>PeriodoDisciplinaVO</code> resultantes da consulta.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public static List consultarPeriodoDisciplinas(Integer periodoLetivo, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        PeriodoDisciplina.consultar(getIdEntidade(), controlarAcesso, usuario);
        List objetos = new ArrayList(0);
        String sqlStr = "SELECT * FROM PeriodoDisciplina WHERE periodoLetivo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[]{periodoLetivo});
        while (tabelaResultado.next()) {
            PeriodoDisciplinaVO novoObj = new PeriodoDisciplinaVO();
            novoObj = PeriodoDisciplina.montarDados(tabelaResultado, usuario);
            objetos.add(novoObj);
        }
        return objetos;
    }

    /* (non-Javadoc)
     * @see negocio.facade.jdbc.academico.PeriodoDisciplinaInterfaceFacade#consultarPorChavePrimaria(java.lang.Integer, java.lang.Integer)
     */
    public PeriodoDisciplinaVO consultarPorChavePrimaria(Integer periodoLetivoPrm, Integer disciplinaPrm, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sqlStr = "SELECT * FROM PeriodoDisciplina WHERE periodoLetivo = ?, disciplina = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[]{periodoLetivoPrm, disciplinaPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado, usuario));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return PeriodoDisciplina.idEntidade;
    }

    /* (non-Javadoc)
     * @see negocio.facade.jdbc.academico.PeriodoDisciplinaInterfaceFacade#setIdEntidade(java.lang.String)
     */
    public void setIdEntidade(String idEntidade) {
        PeriodoDisciplina.idEntidade = idEntidade;
    }
}
