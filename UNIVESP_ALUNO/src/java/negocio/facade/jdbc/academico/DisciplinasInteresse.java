package negocio.facade.jdbc.academico;

import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.DisciplinasInteresseVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.basico.Pessoa;
import negocio.interfaces.academico.DisciplinasInteresseInterfaceFacade;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe
 * <code>DisciplinasInteresseVO</code>. Responsável por implementar operações como incluir, alterar, excluir e consultar
 * pertinentes a classe <code>DisciplinasInteresseVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see DisciplinasInteresseVO
 * @see ControleAcesso
 * @see Pessoa
 */
@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class DisciplinasInteresse extends ControleAcesso implements DisciplinasInteresseInterfaceFacade {

    protected static String idEntidade;

    public DisciplinasInteresse() throws Exception {
        super();
        setIdEntidade("DisciplinasInteresse");
    }

    public DisciplinasInteresseVO novo() throws Exception {
        DisciplinasInteresse.incluir(getIdEntidade());
        DisciplinasInteresseVO obj = new DisciplinasInteresseVO();
        return obj;
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final DisciplinasInteresseVO obj, UsuarioVO usuario) throws Exception {
        DisciplinasInteresseVO.validarDados(obj);
        final String sql = "INSERT INTO DisciplinasInteresse( professor, disciplina ) VALUES ( ?, ? )"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                sqlInserir.setInt(1, obj.getProfessor().intValue());
                sqlInserir.setInt(2, obj.getDisciplina().getCodigo().intValue());
                return sqlInserir;
            }
        });

        obj.setNovoObj(Boolean.FALSE);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final DisciplinasInteresseVO obj, UsuarioVO usuario) throws Exception {
        DisciplinasInteresseVO.validarDados(obj);
        final String sql = "UPDATE DisciplinasInteresse set  WHERE ((professor = ?) and (disciplina = ?))";

        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                sqlAlterar.setInt(1, obj.getProfessor().intValue());
                sqlAlterar.setInt(2, obj.getDisciplina().getCodigo().intValue());
                return sqlAlterar;
            }
        });
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(DisciplinasInteresseVO obj, UsuarioVO usuario) throws Exception {
        DisciplinasInteresse.excluir(getIdEntidade());
        String sql = "DELETE FROM DisciplinasInteresse WHERE ((professor = ?) and (disciplina = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getProfessor(), obj.getDisciplina().getCodigo()});
    }

    public List consultarPorNomeDisciplina(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT DisciplinasInteresse.* FROM DisciplinasInteresse, Disciplina WHERE DisciplinasInteresse.disciplina = Disciplina.codigo and Disciplina.nome like('" + valorConsulta
                + "%') ORDER BY Disciplina.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, usuario);
    }

    public List consultarPorNomePessoa(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT DisciplinasInteresse.* FROM DisciplinasInteresse, Pessoa WHERE DisciplinasInteresse.professor = Pessoa.codigo and Pessoa.nome like('" + valorConsulta
                + "%') ORDER BY Pessoa.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, usuario);
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (
     * <code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por
     * vez.
     * 
     * @return List Contendo vários objetos da classe <code>DisciplinasInteresseVO</code> resultantes da consulta.
     */
    public List montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um
     * objeto da classe <code>DisciplinasInteresseVO</code>.
     * 
     * @return O objeto da classe <code>DisciplinasInteresseVO</code> com os dados devidamente montados.
     */
    public DisciplinasInteresseVO montarDados(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
        DisciplinasInteresseVO obj = new DisciplinasInteresseVO();
        obj.setProfessor(new Integer(dadosSQL.getInt("professor")));
        obj.getDisciplina().setCodigo(new Integer(dadosSQL.getInt("disciplina")));
        obj.setNovoObj(Boolean.FALSE);
        montarDadosDisciplina(obj, usuario);
        obj.setNomeDisciplina(obj.getDisciplina().getNome());
        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>DisciplinaVO</code> relacionado ao objeto
     * <code>DisciplinasInteresseVO</code>. Faz uso da chave primária da classe <code>DisciplinaVO</code> para realizar
     * a consulta.
     * 
     * @param obj
     *            Objeto no qual será montado os dados consultados.
     */
    public void montarDadosDisciplina(DisciplinasInteresseVO obj, UsuarioVO usuario) throws Exception {
        if (obj.getDisciplina().getCodigo().intValue() == 0) {
            obj.setDisciplina(new DisciplinaVO());
            return;
        }
        obj.setDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(obj.getDisciplina().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirDisciplinasInteresses(Integer professor, UsuarioVO usuario) throws Exception {
        excluirDisciplinasInteresses(professor, true, usuario);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirDisciplinasInteresses(Integer professor, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
excluir(getIdEntidade(), verificarAcesso, usuario);
        String sql = "DELETE FROM DisciplinasInteresse WHERE (professor = ?)"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(sql, new Object[]{professor});
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarDisciplinasInteresses(Integer professor, List objetos, UsuarioVO usuario) throws Exception {
        alterarDisciplinasInteresses(professor, objetos, true, usuario);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarDisciplinasInteresses(Integer professor, List objetos, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		if (!objetos.isEmpty()) {
			excluirDisciplinasInteresses(professor, verificarAcesso, usuario);
			incluirDisciplinasInteresses(professor, objetos, usuario);
		}
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirDisciplinasInteresses(Integer professorPrm, List objetos, UsuarioVO usuario) throws Exception {
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            DisciplinasInteresseVO obj = (DisciplinasInteresseVO) e.next();
            obj.setProfessor(professorPrm);
            incluir(obj, usuario);
        }
    }

    /**
     * Operação responsável por consultar todos os <code>DisciplinasInteresseVO</code> relacionados a um objeto da
     * classe <code>basico.Pessoa</code>.
     * 
     * @param professor
     *            Atributo de <code>basico.Pessoa</code> a ser utilizado para localizar os objetos da classe
     *            <code>DisciplinasInteresseVO</code>.
     * @return List Contendo todos os objetos da classe <code>DisciplinasInteresseVO</code> resultantes da consulta.
     * @exception Exception
     *                Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public List consultarDisciplinasInteresses(Integer professor, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        List objetos = new ArrayList(0);
        String sql = "SELECT * FROM DisciplinasInteresse WHERE professor = ?";
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{professor});
        while (resultado.next()) {
            objetos.add(montarDados(resultado, usuario));
        }
        return objetos;
    }

    public List consultarDisciplinasInteressesPorCodigoDisciplina(Integer disciplina, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        List objetos = new ArrayList(0);
        String sql = "SELECT * FROM DisciplinasInteresse WHERE disciplina = ?";
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{disciplina});
        while (resultado.next()) {
            objetos.add(montarDados(resultado, usuario));
        }
        return objetos;
    }

    public Boolean consultarExistenciaDisciplinaComProfessor(Integer disciplina, Integer professor) {
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("select * from disciplinasinteresse where disciplina = ");
        sqlStr.append(disciplina);
        sqlStr.append(" and professor = ");
        sqlStr.append(professor);
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        if (tabelaResultado.next()) {
            return true;
        }
        return false;
    }

    public DisciplinasInteresseVO consultarPorChavePrimaria(Integer professorPrm, Integer disciplinaPrm, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM DisciplinasInteresse WHERE professor = ?, disciplina = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{professorPrm, disciplinaPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado, usuario));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as
     * permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return DisciplinasInteresse.idEntidade;
    }

    public void setIdEntidade(String idEntidade) {
        DisciplinasInteresse.idEntidade = idEntidade;
    }
}