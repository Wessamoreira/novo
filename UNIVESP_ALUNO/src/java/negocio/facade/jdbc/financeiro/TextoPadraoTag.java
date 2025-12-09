package negocio.facade.jdbc.financeiro;

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

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.TextoPadraoTagVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.TextoPadraoTagInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>ModalidadeVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>ModalidadeVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see ModalidadeVO
 * @see ControleAcesso
 * @see 
 */
@Repository
@Scope("singleton")
@Lazy 
public class TextoPadraoTag extends ControleAcesso implements TextoPadraoTagInterfaceFacade{

    protected static String idEntidade;

    public TextoPadraoTag() throws Exception {
        super();
        setIdEntidade("TextoPadrao");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>ModalidadeVO</code>.
     */
    public TextoPadraoTagVO novo() throws Exception {
        incluir(getIdEntidade());
        TextoPadraoTagVO obj = new TextoPadraoTagVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>ModalidadeVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>ModalidadeVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final TextoPadraoTagVO obj, UsuarioVO usuario) throws Exception {
        try {
        	/**
        	  * @author Leonardo Riciolle 
        	  * Comentado 29/10/2014
        	  *  Classe Subordinada
        	  */ 
            // TextoPadraoTag.incluir(getIdEntidade());
            TextoPadraoTagVO.validarDados(obj);
            final String sql = "INSERT INTO TextoPadraoTag( tag,textopadrao) VALUES ( ?, ? ) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    sqlInserir.setString(1, obj.getTag());
                    sqlInserir.setInt(2, obj.getTextoPadrao().intValue());
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
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>ModalidadeVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>ModalidadeVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final TextoPadraoTagVO obj, UsuarioVO usuario) throws Exception {
        try {
        	/**
        	  * @author Leonardo Riciolle 
        	  * Comentado 29/10/2014
        	  *  Classe Subordinada
        	  */ 
            // TextoPadraoTag.alterar(getIdEntidade());
            TextoPadraoTagVO.validarDados(obj);
            final String sql = "UPDATE TextoPadraoTag set tag=?, textopadrao=? WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setString(1, obj.getTag());
                    sqlAlterar.setInt(2, obj.getTextoPadrao().intValue());
                    sqlAlterar.setInt(3, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>ModalidadeVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>ModalidadeVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(TextoPadraoTagVO obj, UsuarioVO usuario) throws Exception {
        try {
        	/**
        	  * @author Leonardo Riciolle 
        	  * Comentado 29/10/2014
        	  *  Classe Subordinada
        	  */ 
            // TextoPadraoTag.excluir(getIdEntidade());
            String sql = "DELETE FROM TextoPadraoTag WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sql, new Object[] {obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>Modalidade</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>ModalidadeVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM TextoPadraoTag WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado,nivelMontarDados));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>ModalidadeVO</code> resultantes da consulta.
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
     * em um objeto da classe <code>ModalidadeVO</code>.
     * @return  O objeto da classe <code>ModalidadeVO</code> com os dados devidamente montados.
     */
    public static TextoPadraoTagVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados) throws Exception {
        TextoPadraoTagVO obj = new TextoPadraoTagVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setTag(dadosSQL.getString("tag"));
        obj.setTextoPadrao(new Integer(dadosSQL.getInt("TextoPadrao")));
        obj.setNovoObj(Boolean.FALSE);
        return obj;
    }

    /**
     * Operação responsável por excluir todos os objetos da <code>ModalidadeVO</code> no BD.
     * Faz uso da operação <code>excluir</code> disponível na classe <code>Modalidade</code>.
     * @param <code></code> campo chave para exclusão dos objetos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirTextoPadraoTag(Integer plano, UsuarioVO usuario) throws Exception {
         try {
            excluir(getIdEntidade());
            String sql = "DELETE FROM TextoPadraoTag WHERE (TextoPadrao = ?)"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sql, new Object[] {plano.intValue()});
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por alterar todos os objetos da <code>ModalidadeVO</code> contidos em um Hashtable no BD.
     * Faz uso da operação <code>excluirModalidades</code> e <code>incluirModalidades</code> disponíveis na classe <code>Modalidade</code>.
     * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void alterarTextoPadraoTag(Integer textoPadrao, List objetos, UsuarioVO usuario) throws Exception {
        String str = "DELETE FROM TextoPadraoTag WHERE textoPadrao = " + textoPadrao.intValue();
        Iterator i = objetos.iterator();
        while (i.hasNext()) {
            TextoPadraoTagVO objeto = (TextoPadraoTagVO) i.next();
            str += " AND codigo <> " + objeto.getCodigo().intValue();
        }
        getConexao().getJdbcTemplate().update(str+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            TextoPadraoTagVO obj = (TextoPadraoTagVO) e.next();
            if (obj.getCodigo().equals(0)) {
                obj.setTextoPadrao(textoPadrao);
                incluir(obj, usuario);
            } else {
                alterar(obj, usuario);
            }
        }
    }

    /**
     * Operação responsável por incluir objetos da <code>ModalidadeVO</code> no BD.
     * Garantindo o relacionamento com a entidade principal <code>.</code> através do atributo de vínculo.
     * @param objetos List contendo os objetos a serem gravados no BD da classe.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void incluirTextoPadraoTag(Integer Prm, List objetos, UsuarioVO usuario) throws Exception {
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            TextoPadraoTagVO obj = (TextoPadraoTagVO) e.next();
            obj.setTextoPadrao(Prm);
            incluir(obj, usuario);
        }
    }

    /**
     * Operação responsável por consultar todos os <code>ModalidadeVO</code> relacionados a um objeto da classe <code>.</code>.
     * @param   Atributo de <code>.</code> a ser utilizado para localizar os objetos da classe <code>ModalidadeVO</code>.
     * @return List  Contendo todos os objetos da classe <code>ModalidadeVO</code> resultantes da consulta.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public List consultarTextoPadraoTag(Integer TextoPadrao, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade());
        List objetos = new ArrayList(0);
        String sql = "SELECT * FROM TextoPadraoTag WHERE TextoPadrao = ?";
       SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { TextoPadrao });
        while (resultado.next()) {
            TextoPadraoTagVO novoObj = new TextoPadraoTagVO();
            novoObj = TextoPadraoTag.montarDados(resultado, nivelMontarDados);
            objetos.add(novoObj);
        }
        return objetos;
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>ModalidadeVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public TextoPadraoTagVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM TextoPadraoTag WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] {codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado,nivelMontarDados));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return TextoPadraoTag.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        TextoPadraoTag.idEntidade = idEntidade;
    }

}
