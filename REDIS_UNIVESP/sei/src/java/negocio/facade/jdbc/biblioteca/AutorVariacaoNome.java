package negocio.facade.jdbc.biblioteca;

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

import negocio.comuns.biblioteca.AutorVO;
import negocio.comuns.biblioteca.AutorVariacaoNomeVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.biblioteca.AutorVariacaoNomeInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>AutorVariacaoNomeVO</code>. Responsável por implementar
 * operações como incluir, alterar, excluir e consultar pertinentes a classe <code>AutorVariacaoNomeVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see AutorVariacaoNomeVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class AutorVariacaoNome extends ControleAcesso implements AutorVariacaoNomeInterfaceFacade {

    protected static String idEntidade;

    public AutorVariacaoNome() throws Exception {
        super();
        setIdEntidade("AutorVariacaoNome");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>AutorVariacaoNomeVO</code>.
     */
    @Override
    public AutorVariacaoNomeVO novo() throws Exception {
        AutorVariacaoNome.incluir(getIdEntidade());
        AutorVariacaoNomeVO obj = new AutorVariacaoNomeVO();
        return obj;
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe <code>AutorVO</code>. Todos os tipos de consistência de dados são e devem ser
     * implementadas neste método. São validações típicas: verificação de campos obrigatórios, verificação de valores válidos para os atributos.
     * 
     * @exception ConsistirExecption Se uma inconsistência for encontrada aumaticamente é gerada uma exceção descrevendo o atributo e o erro ocorrido.
     */
    @Override
    public void validarDados(AutorVariacaoNomeVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if (obj.getVariacaoNome().equals("")) {
            throw new ConsistirException("O campo VARIAÇÃO NOME (Variação Nome) deve ser informado.");
        }
    }

    /**
     * Operação reponsável por realizar o UpperCase dos atributos do tipo String.
     */
    public AutorVariacaoNomeVO realizarUpperCaseDados(AutorVariacaoNomeVO obj) {
        if (Uteis.realizarUpperCaseDadosAntesPersistencia) {
            obj.setVariacaoNome(obj.getVariacaoNome().toUpperCase());
        }
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>AutorVariacaoNomeVO</code>. Primeiramente valida os dados (
     * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto,
     * através da operação <code>incluir</code> da superclasse.
     * 
     * @param obj Objeto da classe <code>AutorVariacaoNomeVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final AutorVariacaoNomeVO obj) throws Exception {
        try {
            validarDados(obj);
            realizarUpperCaseDados(obj);
            final String sql = "INSERT INTO autorvariacaonome( variacaonome, autor ) VALUES ( ?, ?) returning codigo";
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    sqlInserir.setString(1, obj.getVariacaoNome());
                    sqlInserir.setInt(2, obj.getAutor().getCodigo());
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
            obj.setNovoObj(true);
            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>AutorVariacaoNomeVO</code>. Sempre utiliza a chave primária da classe como
     * atributo para localização do registro a ser alterado. Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco
     * de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>alterar</code> da superclasse.
     * 
     * @param obj Objeto da classe <code>AutorVariacaoNomeVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final AutorVariacaoNomeVO obj) throws Exception {
        try {
            validarDados(obj);
            realizarUpperCaseDados(obj);
            final String sql = "UPDATE autorvariacaonome set variacaonome=?, autor=? WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setString(1, obj.getVariacaoNome());
                    sqlAlterar.setInt(2, obj.getAutor().getCodigo());
                    sqlAlterar.setInt(3, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>AutorVariacaoNomeVO</code>. Sempre localiza o registro a ser excluído através da chave
     * primária da entidade. Primeiramente verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto,
     * através da operação <code>excluir</code> da superclasse.
     * 
     * @param obj Objeto da classe <code>AutorVariacaoNomeVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(AutorVariacaoNomeVO obj) throws Exception {
        try {
            String sql = "DELETE FROM autorvariacaonome WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>AutorVariacaoNome</code> através do valor do atributo <code>String nome</code> . Retorna os objetos, com
     * início do valor do atributo idêntico ao parâmetro fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
     * List resultante.
     * 
     * @param controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>AutorVariacaoNomeVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    @Override
    public List<AutorVariacaoNomeVO> consultarPorVariacaoNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados) throws Exception {
        String sqlStr = "SELECT * FROM autorvariacaonome WHERE upper( variacaonome ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY variacaonome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado));
    }

    /**
     * Responsável por realizar uma consulta de <code>AutorVariacaoNome</code> através do valor do atributo <code>Integer codigo</code>. Retorna os objetos com
     * valores iguais ou superiores ao parâmetro fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List
     * resultante.
     * 
     * @param controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>AutorVariacaoNomeVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    @Override
    public List<AutorVariacaoNomeVO> consultarPorAutor(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados) throws Exception {
        String sqlStr = "SELECT * FROM autorvariacaonome WHERE autor = " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados ( <code>ResultSet</code>). Faz uso da operação
     * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * 
     * @return List Contendo vários objetos da classe <code>AutorVariacaoNomeVO</code> resultantes da consulta.
     */
    public static List<AutorVariacaoNomeVO> montarDadosConsulta(SqlRowSet tabelaResultado) throws Exception {
        List<AutorVariacaoNomeVO> vetResultado = new ArrayList<AutorVariacaoNomeVO>(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um objeto da classe
     * <code>AutorVariacaoNomeVO</code>.
     * 
     * @return O objeto da classe <code>AutorVariacaoNomeVO</code> com os dados devidamente montados.
     */
    public static AutorVariacaoNomeVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados) throws Exception {
        AutorVariacaoNomeVO obj = new AutorVariacaoNomeVO();
        obj.setAutor(new AutorVO());
        obj.setCodigo(dadosSQL.getInt("codigo"));
        obj.setVariacaoNome(dadosSQL.getString("variacaonome"));
        obj.getAutor().setCodigo(dadosSQL.getInt("autor"));
        obj.setNovoObj(Boolean.FALSE);
        return obj;
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>AutorVariacaoNomeVO</code> através de sua chave primária.
     * 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    @Override
    public AutorVariacaoNomeVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados) throws Exception {
        String sqlStr = "SELECT * FROM autorvariacaonome WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[] { codigoPrm });
        tabelaResultado.next();
        if (tabelaResultado.getRow() == 0) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado, nivelMontarDados));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações desta
     * classe.
     */
    public static String getIdEntidade() {
        return AutorVariacaoNome.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser possível, pois, uma mesma classe de negócio pode
     * ser utilizada com objetivos distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        AutorVariacaoNome.idEntidade = idEntidade;
    }
}