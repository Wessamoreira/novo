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

import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.NotificacaoRegistroAulaNotaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>NotificacaoRegistroAulaNotaVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>NotificacaoRegistroAulaNotaVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see NotificacaoRegistroAulaNotaVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class NotificacaoRegistroAulaNota extends ControleAcesso  {

    protected static String idEntidade;

    public NotificacaoRegistroAulaNota() throws Exception {
        super();
        setIdEntidade("NotificacaoRegistroAulaNota");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>NotificacaoRegistroAulaNotaVO</code>.
     */
    public NotificacaoRegistroAulaNotaVO novo() throws Exception {
        NotificacaoRegistroAulaNotaVO obj = new NotificacaoRegistroAulaNotaVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>NotificacaoRegistroAulaNotaVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>NotificacaoRegistroAulaNotaVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final NotificacaoRegistroAulaNotaVO obj) throws Exception {
        try {
            if (!verificarInclusaoRegistroJaExisteTabelaParaDataAtual(obj.getTurma(), obj.getProfessor(), obj.getDisciplina(), obj.getUsuario().getCodigo())) {
                final String sql = "INSERT INTO NotificacaoRegistroAulaNota( data, turma, professor, disciplina, usuario, unidadeEnsino ) VALUES ( ?, ?, ?, ?, ?, ? ) returning codigo";
                obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                    public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                        PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                        sqlInserir.setDate(1, Uteis.getDataJDBC(new Date()));
                        sqlInserir.setInt(2, obj.getTurma());
                        sqlInserir.setInt(3, obj.getProfessor());
                        sqlInserir.setInt(4, obj.getDisciplina());
                        sqlInserir.setInt(5, obj.getUsuario().getCodigo());
                        sqlInserir.setInt(6, obj.getUnidadeEnsino());
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
        } catch (Exception e) {
            obj.setNovoObj(Boolean.TRUE);
            obj.setCodigo(0);
            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>NotificacaoRegistroAulaNotaVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>NotificacaoRegistroAulaNotaVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final NotificacaoRegistroAulaNotaVO obj) throws Exception {
        try {
            final String sql = "UPDATE NotificacaoRegistroAulaNota set data=?, turma=?, professor=?, disciplina=?, usuario=?, unidadeEnsino=? WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setDate(1, Uteis.getDataJDBC(new Date()));
                    sqlAlterar.setInt(2, obj.getTurma());
                    sqlAlterar.setInt(3, obj.getProfessor());
                    sqlAlterar.setInt(4, obj.getDisciplina());
                    sqlAlterar.setInt(5, obj.getUsuario().getCodigo());
                    sqlAlterar.setInt(6, obj.getUnidadeEnsino());
                    sqlAlterar.setInt(7, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>NotificacaoRegistroAulaNotaVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>NotificacaoRegistroAulaNotaVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(NotificacaoRegistroAulaNotaVO obj) throws Exception {
        try {
            NotificacaoRegistroAulaNota.excluir(getIdEntidade());
            String sql = "DELETE FROM NotificacaoRegistroAulaNota WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>Turno</code> através do valor do atributo 
     * <code>String nome</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>NotificacaoRegistroAulaNotaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM NotificacaoRegistroAulaNota WHERE lower (nome) like('" + valorConsulta.toLowerCase() + "%') ORDER BY nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
    }

    public List consultaRegistroANotificar() throws Exception {
        String sqlStr = "SELECT * FROM NotificacaoRegistroAulaNota WHERE data = '" + Uteis.getDataJDBC(new Date()) + "'";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>NotificacaoRegistroAulaNotaVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>NotificacaoRegistroAulaNotaVO</code>.
     * @return  O objeto da classe <code>NotificacaoRegistroAulaNotaVO</code> com os dados devidamente montados.
     */
    public static NotificacaoRegistroAulaNotaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados) throws Exception {
        ////System.out.println(">> Montar dados(Turno) - " + new Date());
        NotificacaoRegistroAulaNotaVO obj = new NotificacaoRegistroAulaNotaVO();
        obj.setCodigo(dadosSQL.getInt("codigo"));
        obj.setData(dadosSQL.getDate("data"));
        obj.setTurma(dadosSQL.getInt("turma"));
        obj.getTurmaVO().setCodigo(dadosSQL.getInt("turma"));
        obj.setProfessor(dadosSQL.getInt("professor"));
        obj.getProfessorVO().setCodigo(dadosSQL.getInt("professor"));
        obj.setDisciplina(dadosSQL.getInt("disciplina"));
        obj.getDisciplinaVO().setCodigo(dadosSQL.getInt("disciplina"));
        obj.getUsuario().setCodigo(dadosSQL.getInt("usuario"));
        obj.setUnidadeEnsino(dadosSQL.getInt("unidadeEnsino"));

        montarDadosProfessor(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, new UsuarioVO());
        montarDadosUsuario(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, new UsuarioVO());
        montarDadosTurma(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, new UsuarioVO());
        montarDadosDisciplina(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, new UsuarioVO());
        
        return obj;
    }

    public static void montarDadosUsuario(NotificacaoRegistroAulaNotaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getUsuario().getCodigo().intValue() == 0) {
            obj.setUsuario(new UsuarioVO());
            return;
        }
        obj.setUsuario(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getUsuario().getCodigo(), nivelMontarDados, usuario));
    }

    public static void montarDadosDisciplina(NotificacaoRegistroAulaNotaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getDisciplinaVO().getCodigo().intValue() == 0) {
            obj.setDisciplinaVO(new DisciplinaVO());
            return;
        }
        obj.setDisciplinaVO(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(obj.getDisciplinaVO().getCodigo(), nivelMontarDados, usuario));
    }

    public static void montarDadosProfessor(NotificacaoRegistroAulaNotaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getProfessorVO().getCodigo().intValue() == 0) {
            return;
        }
        obj.setProfessorVO(getFacadeFactory().getFuncionarioFacade().consultarPorCodigoPessoa(obj.getProfessor(), 0, false, Uteis.NIVELMONTARDADOS_TODOS, usuario));
        
    }

    public static void montarDadosTurma(NotificacaoRegistroAulaNotaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getTurmaVO().getCodigo().intValue() == 0) {
            obj.setTurmaVO(new TurmaVO());
            return;
        }
        obj.setTurmaVO(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getTurmaVO().getCodigo(), nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>NotificacaoRegistroAulaNotaVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public NotificacaoRegistroAulaNotaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM NotificacaoRegistroAulaNota WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado, nivelMontarDados));
    }

    public boolean verificarInclusaoRegistroJaExisteTabelaParaDataAtual(Integer turma, Integer professor, Integer disciplina, Integer usuario) throws Exception {
        String sql = "SELECT * FROM NotificacaoRegistroAulaNota WHERE data = '" +Uteis.getDataJDBC(new Date())+ "' and turma = " + turma + " and professor = " + professor + " and disciplina = " + disciplina + " and usuario = " + usuario;
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
        if (!tabelaResultado.next()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return NotificacaoRegistroAulaNota.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        NotificacaoRegistroAulaNota.idEntidade = idEntidade;
    }
}
