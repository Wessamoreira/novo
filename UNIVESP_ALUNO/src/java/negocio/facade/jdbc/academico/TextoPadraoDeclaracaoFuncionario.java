/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.facade.jdbc.academico;

/**
 *
 * @author Rogerio
 */
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

import negocio.comuns.academico.TextoPadraoDeclaracaoFuncionarioVO;
import negocio.comuns.academico.TextoPadraoDeclaracaoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.basico.Pessoa;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>TextoPadraoDeclaracaoFuncionarioVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>TextoPadraoDeclaracaoFuncionarioVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see TextoPadraoDeclaracaoFuncionarioVO
 * @see ControleAcesso
 * @see Pessoa
 */
@Repository
@Scope("singleton")
@Lazy
public class TextoPadraoDeclaracaoFuncionario extends ControleAcesso {

    protected static String idEntidade;

    public TextoPadraoDeclaracaoFuncionario() throws Exception {
        super();
        setIdEntidade("TextoPadraoDeclaracaoFuncionario");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>TextoPadraoDeclaracaoFuncionarioVO</code>.
     */
    public TextoPadraoDeclaracaoFuncionarioVO novo() throws Exception {
        TextoPadraoDeclaracaoFuncionario.incluir(getIdEntidade());
        TextoPadraoDeclaracaoFuncionarioVO obj = new TextoPadraoDeclaracaoFuncionarioVO();
        return obj;
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
    public void incluir(final TextoPadraoDeclaracaoFuncionarioVO obj) throws Exception {
        final String sql = "INSERT INTO TextoPadraoDeclaracaoFuncionario( textoPadraoDeclaracao, funcionario) VALUES (?, ? ) returning codigo";
        obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement sqlInserir = con.prepareStatement(sql);
                if (Uteis.isAtributoPreenchido(obj.getTextoPadraoDeclaracao().getCodigo())) {
                    sqlInserir.setInt(1, obj.getTextoPadraoDeclaracao().getCodigo());
                } else {
                    sqlInserir.setNull(1, 0);
                }
                if (Uteis.isAtributoPreenchido(obj.getFuncionario().getCodigo())) {
                	sqlInserir.setInt(2, obj.getFuncionario().getCodigo());
                } else {
                	sqlInserir.setNull(2, 0);
                }
                return sqlInserir;
            }
        }, new ResultSetExtractor() {

            public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                if (rs.next()) {
                    obj.setNovoObj(Boolean.FALSE);
                    return rs.getInt("codigo");
                }
                return null;
            }
        }));
        obj.setNovoObj(Boolean.FALSE);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
    public void alterar(final TextoPadraoDeclaracaoFuncionarioVO obj) throws Exception {
        final String sql = "UPDATE TextoPadraoDeclaracaoFuncionario set textoPadraoDeclaracao=?, funcionario=? WHERE ((codigo = ?))";
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement sqlAlterar = con.prepareStatement(sql);
                if (Uteis.isAtributoPreenchido(obj.getTextoPadraoDeclaracao().getCodigo())) {
                    sqlAlterar.setInt(1, obj.getTextoPadraoDeclaracao().getCodigo());
                } else {
                    sqlAlterar.setNull(1, 0);
                }
                if (Uteis.isAtributoPreenchido(obj.getFuncionario().getCodigo())) {
                	sqlAlterar.setInt(2, obj.getFuncionario().getCodigo());
                } else {
                	sqlAlterar.setNull(2, 0);
                }
                sqlAlterar.setInt(3, obj.getCodigo().intValue());
                return sqlAlterar;
            }
        });
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>TextoPadraoDeclaracaoFuncionarioVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>TextoPadraoDeclaracaoFuncionarioVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
    public void excluir(TextoPadraoDeclaracaoFuncionarioVO obj) throws Exception {
        TextoPadraoDeclaracaoFuncionario.excluir(getIdEntidade());
        String sql = "DELETE FROM TextoPadraoDeclaracaoFuncionario WHERE ((codigo = ?))";
        getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
    }

    /**
     * Responsável por realizar uma consulta de <code>TextoPadraoDeclaracaoFuncionario</code> através do valor do atributo
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>TextoPadraoDeclaracaoFuncionarioVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM TextoPadraoDeclaracaoFuncionario WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>TextoPadraoDeclaracaoFuncionarioVO</code> resultantes da consulta.
     */
    public  List montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList();
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>TextoPadraoDeclaracaoFuncionarioVO</code>.
     * @return  O objeto da classe <code>TextoPadraoDeclaracaoFuncionarioVO</code> com os dados devidamente montados.
     */
    public  TextoPadraoDeclaracaoFuncionarioVO montarDados(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
        TextoPadraoDeclaracaoFuncionarioVO obj = new TextoPadraoDeclaracaoFuncionarioVO();
        obj.getTextoPadraoDeclaracao().setCodigo(dadosSQL.getInt("textoPadraoDeclaracao"));
        obj.getFuncionario().setCodigo(dadosSQL.getInt("funcionario"));
        obj.setNovoObj(false);
        montarDadosFuncionario(obj, usuario);
        return obj;
    }

    public  void montarDadosFuncionario(TextoPadraoDeclaracaoFuncionarioVO obj, UsuarioVO usuario) throws Exception {
        if (obj.getFuncionario().getCodigo().intValue() == 0) {
            obj.setFuncionario(new FuncionarioVO());
            return;
        }
        obj.setFuncionario(getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimaria(obj.getFuncionario().getCodigo(), 0, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
    }

    /**
     * Operação responsável por excluir todos os objetos da <code>TextoPadraoDeclaracaoFuncionarioVO</code> no BD.
     * Faz uso da operação <code>excluir</code> disponível na classe <code>TextoPadraoDeclaracaoFuncionario</code>.
     * @param <code>pessoa</code> campo chave para exclusão dos objetos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
    public void excluirTextoPadraoDeclaracaoFuncionario(TextoPadraoDeclaracaoVO textoPadraoDeclaracao, List objetos) throws Exception {
        String sql = "DELETE FROM TextoPadraoDeclaracaoFuncionario WHERE (textoPadraoDeclaracao = ?)";
        Iterator i = objetos.iterator();
        while (i.hasNext()) {
            TextoPadraoDeclaracaoFuncionarioVO obj = (TextoPadraoDeclaracaoFuncionarioVO) i.next();
            sql += " and codigo != " + obj.getCodigo().intValue();
        }
        getConexao().getJdbcTemplate().update(sql, new Object[]{textoPadraoDeclaracao.getCodigo()});
    }

    /**
     * Operação responsável por alterar todos os objetos da <code>TextoPadraoDeclaracaoFuncionarioVO</code> contidos em um Hashtable no BD.
     * Faz uso da operação <code>excluirTextoPadraoDeclaracaoFuncionario</code> e <code>incluirTextoPadraoDeclaracaoFuncionario</code> disponíveis na classe <code>TextoPadraoDeclaracaoFuncionario</code>.
     * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
    public void alterarTextoPadraoDeclaracaoFuncionario(TextoPadraoDeclaracaoVO funcionario, List objetos) throws Exception {
        incluirTextoPadraoDeclaracaoFuncionario(funcionario, objetos);
        excluirTextoPadraoDeclaracaoFuncionario(funcionario, objetos);
    }

    /**
     * Operação responsável por incluir objetos da <code>TextoPadraoDeclaracaoFuncionarioVO</code> no BD.
     * Garantindo o relacionamento com a entidade principal <code>basico.Pessoa</code> através do atributo de vínculo.
     * @param objetos List contendo os objetos a serem gravados no BD da classe.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
    public void incluirTextoPadraoDeclaracaoFuncionario(TextoPadraoDeclaracaoVO textoPrm, List objetos) throws Exception {
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            TextoPadraoDeclaracaoFuncionarioVO obj = (TextoPadraoDeclaracaoFuncionarioVO) e.next();
            obj.setTextoPadraoDeclaracao(textoPrm);
            if (obj.getCodigo().intValue() == 0) {
                incluir(obj);
            } else {
                alterar(obj);
            }
        }
    }

    /**
     * Operação responsável por consultar todos os <code>TextoPadraoDeclaracaoFuncionarioVO</code> relacionados a um objeto da classe <code>basico.Pessoa</code>.
     * @param pessoa  Atributo de <code>basico.Pessoa</code> a ser utilizado para localizar os objetos da classe <code>TextoPadraoDeclaracaoFuncionarioVO</code>.
     * @return List  Contendo todos os objetos da classe <code>TextoPadraoDeclaracaoFuncionarioVO</code> resultantes da consulta.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public  List consultarTextoPadraoDeclaracaoFuncionario(Integer textoPadraoDeclaracao, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        return consultarTextoPadraoDeclaracaoFuncionario(textoPadraoDeclaracao, controlarAcesso, false, usuario);
    }

    public  List consultarTextoPadraoDeclaracaoFuncionario(Integer textoPadraoDeclaracao, boolean controlarAcesso, boolean funcionario, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        List objetos = new ArrayList();
        String sql = "SELECT * FROM TextoPadraoDeclaracaoFuncionario WHERE textoPadraoDeclaracao = ?";
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{textoPadraoDeclaracao.intValue()});
        while (resultado.next()) {
            TextoPadraoDeclaracaoFuncionarioVO novoObj = new TextoPadraoDeclaracaoFuncionarioVO();
            novoObj = montarDados(resultado, usuario);
            objetos.add(novoObj);
        }
        return objetos;
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return TextoPadraoDeclaracaoFuncionario.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        TextoPadraoDeclaracaoFuncionario.idEntidade = idEntidade;
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>TextoPadraoDeclaracaoFuncionarioVO</code>
     * através de sua chave primária.
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public TextoPadraoDeclaracaoFuncionarioVO consultarPorChavePrimaria(Integer codigoPrm, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM TextoPadraoDeclaracaoFuncionario WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm.intValue()});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado, usuario));
    }

}
