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

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.biblioteca.EditoraVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.biblioteca.EditoraInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>EditoraVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>EditoraVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see EditoraVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class Editora extends ControleAcesso implements EditoraInterfaceFacade {

    protected static String idEntidade;

    public Editora() throws Exception {
        super();
        setIdEntidade("Editora");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>EditoraVO</code>.
     */
    public EditoraVO novo() throws Exception {
        Editora.incluir(getIdEntidade());
        EditoraVO obj = new EditoraVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>EditoraVO</code>. Primeiramente
     * valida os dados ( <code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do
     * usuário para realizar esta operacão na entidade. Isto, através da operação <code>incluir</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>EditoraVO</code> que será gravado no banco de dados.
     * @exception Exception
     *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    public void incluir(final EditoraVO obj, UsuarioVO usuarioVO) throws Exception {
        try {
            EditoraVO.validarDados(obj);
            Editora.incluir(getIdEntidade(), true, usuarioVO);
            obj.realizarUpperCaseDados();
            final String sql = "INSERT INTO Editora( nome, site, endereco, numero, bairro, cidade, telefone, telefone1, fax, cep, email, contato ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo";
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    sqlInserir.setString(1, obj.getNome());
                    sqlInserir.setString(2, obj.getSite());
                    sqlInserir.setString(3, obj.getEndereco());
                    sqlInserir.setString(4, obj.getNumero());
                    sqlInserir.setString(5, obj.getBairro());
                    if (obj.getCidade().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(6, obj.getCidade().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(6, 0);
                    }
                    sqlInserir.setString(7, obj.getTelefone());
                    sqlInserir.setString(8, obj.getTelefone1());
                    sqlInserir.setString(9, obj.getFax());
                    sqlInserir.setString(10, obj.getCEP());
                    sqlInserir.setString(11, obj.getEmail());
                    sqlInserir.setString(12, obj.getContato());
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
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>EditoraVO</code>. Sempre utiliza a
     * chave primária da classe como atributo para localização do registro a ser alterado. Primeiramente valida os dados
     * (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário para
     * realizar esta operacão na entidade. Isto, através da operação <code>alterar</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>EditoraVO</code> que será alterada no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final EditoraVO obj, UsuarioVO usuarioVO) throws Exception {
        try {

            EditoraVO.validarDados(obj);
            Editora.alterar(getIdEntidade(), true, usuarioVO);
            obj.realizarUpperCaseDados();
            final String sql = "UPDATE Editora set nome=?, site=?, endereco=?, numero=?, bairro=?, cidade=?, telefone=?, telefone1=?, fax=?, cep=?, email=?, contato=? WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setString(1, obj.getNome());
                    sqlAlterar.setString(2, obj.getSite());
                    sqlAlterar.setString(3, obj.getEndereco());
                    sqlAlterar.setString(4, obj.getNumero());
                    sqlAlterar.setString(5, obj.getBairro());
                    if (obj.getCidade().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(6, obj.getCidade().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(6, 0);
                    }
                    sqlAlterar.setString(7, obj.getTelefone());
                    sqlAlterar.setString(8, obj.getTelefone1());
                    sqlAlterar.setString(9, obj.getFax());
                    sqlAlterar.setString(10, obj.getCEP());
                    sqlAlterar.setString(11, obj.getEmail());
                    sqlAlterar.setString(12, obj.getContato());
                    sqlAlterar.setInt(13, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>EditoraVO</code>. Sempre localiza o registro a
     * ser excluído através da chave primária da entidade. Primeiramente verifica a conexão com o banco de dados e a
     * permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>excluir</code> da
     * superclasse.
     *
     * @param obj
     *            Objeto da classe <code>EditoraVO</code> que será removido no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(EditoraVO obj, UsuarioVO usuarioVO) throws Exception {
        try {
            Editora.excluir(getIdEntidade(), true, usuarioVO);
            String sql = "DELETE FROM Editora WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>Editora</code> através do valor do atributo
     * <code>String nome</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>EditoraVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Editora WHERE upper(sem_acentos( nome )) ilike(sem_acentos(?)) ORDER BY nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta.toUpperCase() + "%");
        return (montarDadosConsulta(tabelaResultado, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>Editora</code> através do valor do atributo
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso
     * da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>EditoraVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Editora WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, usuario));
    }

    public List<EditoraVO> consultarEdiraPorNomeComboBox(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT DISTINCT codigo, nome FROM Editora WHERE lower(nome) like('"+ valorConsulta.toLowerCase()+ "%') ORDER BY nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            EditoraVO obj = new EditoraVO();
            obj.setCodigo(tabelaResultado.getInt("codigo"));
            obj.setNome(tabelaResultado.getString("nome"));
            vetResultado.add(obj);
        }
        return vetResultado;
    }

    public List<EditoraVO> consultarEdiraPorCodigoComboBox(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT DISTINCT codigo, nome FROM Editora WHERE codigo  = "+ valorConsulta.intValue()+ " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            EditoraVO obj = new EditoraVO();
            obj.setCodigo(tabelaResultado.getInt("codigo"));
            obj.setNome(tabelaResultado.getString("nome"));
            vetResultado.add(obj);
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (
     * <code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por
     * vez.
     *
     * @return List Contendo vários objetos da classe <code>EditoraVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um
     * objeto da classe <code>EditoraVO</code>.
     *
     * @return O objeto da classe <code>EditoraVO</code> com os dados devidamente montados.
     */
    public static EditoraVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        EditoraVO obj = new EditoraVO();
        obj.setCodigo(dadosSQL.getInt("codigo"));
        obj.setNome(dadosSQL.getString("nome"));
        obj.setSite(dadosSQL.getString("site"));
        obj.setEndereco(dadosSQL.getString("endereco"));
        obj.setNumero(dadosSQL.getString("numero"));
        obj.setBairro(dadosSQL.getString("bairro"));
        obj.getCidade().setCodigo(dadosSQL.getInt("cidade"));
        obj.setTelefone(dadosSQL.getString("telefone"));
        obj.setTelefone1(dadosSQL.getString("telefone1"));
        obj.setFax(dadosSQL.getString("fax"));
        obj.setCEP(dadosSQL.getString("cep"));
        obj.setEmail(dadosSQL.getString("email"));
        obj.setContato(dadosSQL.getString("contato"));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        montarDadosCidade(obj, nivelMontarDados, usuario);
        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>CidadeVO</code> relacionado ao objeto
     * <code>EditoraVO</code>. Faz uso da chave primária da classe <code>CidadeVO</code> para realizar a consulta.
     *
     * @param obj
     *            Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosCidade(EditoraVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getCidade().getCodigo().intValue() == 0) {
            obj.setCidade(new CidadeVO());
            return;
        }
        obj.setCidade(getFacadeFactory().getCidadeFacade().consultarPorChavePrimaria(obj.getCidade().getCodigo(), false, usuario));
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>EditoraVO</code> através de sua chave primária.
     *
     * @exception Exception
     *                Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public EditoraVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sqlStr = "SELECT * FROM Editora WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( Editora ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }
    
    public EditoraVO consultarPorNomeRegistroUnico(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        String sqlStr = "SELECT * FROM Editora WHERE upper(sem_acentos( nome )) ilike(sem_acentos(?)) LIMIT 1";
        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta.toUpperCase() + "%");
        if (!rs.next()) {
        	return new EditoraVO();
        }
        return montarDados(rs, nivelMontarDados, usuario);
    }

    /**
     * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as
     * permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return Editora.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser
     * possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que
     * Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        Editora.idEntidade = idEntidade;
    }
}
