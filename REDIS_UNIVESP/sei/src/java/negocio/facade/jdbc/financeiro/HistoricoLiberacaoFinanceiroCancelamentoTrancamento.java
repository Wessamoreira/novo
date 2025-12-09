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
import negocio.comuns.financeiro.HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.HistoricoLiberacaoFinanceiroCancelamentoTrancamentoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO
 * @see ControleAcesso
 * @see LiberacaoFinanceiroCancelamentoTrancamento
 */
@Repository
@Scope("singleton")
@Lazy 
public class HistoricoLiberacaoFinanceiroCancelamentoTrancamento extends ControleAcesso implements HistoricoLiberacaoFinanceiroCancelamentoTrancamentoInterfaceFacade {

    protected static String idEntidade;

    public HistoricoLiberacaoFinanceiroCancelamentoTrancamento() throws Exception {
        super();
        setIdEntidade("LiberacaoFinanceiroCancelamentoTrancamento");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO</code>.
     */
    public HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO novo() throws Exception {
        HistoricoLiberacaoFinanceiroCancelamentoTrancamento.incluir(getIdEntidade());
        HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO obj = new HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO obj ,UsuarioVO usuario) throws Exception {
        HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO.validarDados(obj);
        /**
         * @author Leonardo Riciolle
         * Comentado 28/10/2014
         * Classe Subordinada
         */
        // HistoricoLiberacaoFinanceiroCancelamentoTrancamento.incluir(getIdEntidade());
        obj.realizarUpperCaseDados();
        final String sql = "INSERT INTO HistoricoLiberacaoFinanceiroCancelamentoTrancamento( liberacaoFinanceiroCancelamentoTrancamento, descricao ) VALUES ( ?, ? ) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
                PreparedStatement sqlInserir = cnctn.prepareStatement(sql);
                if (obj.getLiberacaoFinanceiroCancelamentoTrancamento().getCodigo().intValue() != 0) {
                    sqlInserir.setInt(1, obj.getLiberacaoFinanceiroCancelamentoTrancamento().getCodigo().intValue());
                } else {
                    sqlInserir.setNull(1, 0);
                }
                sqlInserir.setString(2, obj.getDescricao());
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

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO obj,UsuarioVO usuario) throws Exception {
        HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO.validarDados(obj);
        /**
         * @author Leonardo Riciolle
         * Comentado 28/10/2014
         * Classe Subordinada
         */
		// HistoricoLiberacaoFinanceiroCancelamentoTrancamento.alterar(getIdEntidade());
        obj.realizarUpperCaseDados();
        final String sql = "UPDATE HistoricoLiberacaoFinanceiroCancelamentoTrancamento set liberacaoFinanceiroCancelamentoTrancamento=?, descricao=? WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
                PreparedStatement sqlAlterar = cnctn.prepareStatement(sql);
                if (obj.getLiberacaoFinanceiroCancelamentoTrancamento().getCodigo().intValue() != 0) {
                    sqlAlterar.setInt(1, obj.getLiberacaoFinanceiroCancelamentoTrancamento().getCodigo().intValue());
                } else {
                    sqlAlterar.setNull(1, 0);
                }
                sqlAlterar.setString(2, obj.getDescricao());
                sqlAlterar.setInt(3, obj.getCodigo().intValue());
                return sqlAlterar;
            }
        });
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO obj,UsuarioVO usuario) throws Exception {
    	/**
         * @author Leonardo Riciolle
         * Comentado 28/10/2014
         * Classe Subordinada
         */
		// HistoricoLiberacaoFinanceiroCancelamentoTrancamento.excluir(getIdEntidade());
        String sql = "DELETE FROM HistoricoLiberacaoFinanceiroCancelamentoTrancamento WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
    }

    /**
     * Responsável por realizar uma consulta de <code>HistoricoLiberacaoFinanceiroCancelamentoTrancamento</code> através do valor do atributo 
     * <code>String descricao</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorDescricao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM HistoricoLiberacaoFinanceiroCancelamentoTrancamento WHERE upper( descricao ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY descricao";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
    }

    /**
     * Responsável por realizar uma consulta de <code>HistoricoLiberacaoFinanceiroCancelamentoTrancamento</code> através do valor do atributo 
     * <code>codigo</code> da classe <code>LiberacaoFinanceiroCancelamentoTrancamento</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigoLiberacaoFinanceiroCancelamentoTrancamento(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados ,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT HistoricoLiberacaoFinanceiroCancelamentoTrancamento.* FROM HistoricoLiberacaoFinanceiroCancelamentoTrancamento, LiberacaoFinanceiroCancelamentoTrancamento WHERE HistoricoLiberacaoFinanceiroCancelamentoTrancamento.liberacaoFinanceiroCancelamentoTrancamento = LiberacaoFinanceiroCancelamentoTrancamento.codigo and LiberacaoFinanceiroCancelamentoTrancamento.codigo >= " + valorConsulta.intValue() + " ORDER BY LiberacaoFinanceiroCancelamentoTrancamento.codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados);
    }

    /**
     * Responsável por realizar uma consulta de <code>HistoricoLiberacaoFinanceiroCancelamentoTrancamento</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados ,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM HistoricoLiberacaoFinanceiroCancelamentoTrancamento WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados));
        }
        tabelaResultado = null;
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO</code>.
     * @return  O objeto da classe <code>HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO</code> com os dados devidamente montados.
     */
    public static HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados) throws Exception {
        HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO obj = new HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.getLiberacaoFinanceiroCancelamentoTrancamento().setCodigo(new Integer(dadosSQL.getInt("liberacaoFinanceiroCancelamentoTrancamento")));
        obj.setDescricao(dadosSQL.getString("descricao"));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }

        return obj;
    }

    /**
     * Operação responsável por excluir todos os objetos da <code>HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO</code> no BD.
     * Faz uso da operação <code>excluir</code> disponível na classe <code>HistoricoLiberacaoFinanceiroCancelamentoTrancamento</code>.
     * @param <code>liberacaoFinanceiroCancelamentoTrancamento</code> campo chave para exclusão dos objetos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirHistoricoLiberacaoFinanceiroCancelamentoTrancamentos(Integer liberacaoFinanceiroCancelamentoTrancamento ,UsuarioVO usuario) throws Exception {
        HistoricoLiberacaoFinanceiroCancelamentoTrancamento.excluir(getIdEntidade());
        String sql = "DELETE FROM HistoricoLiberacaoFinanceiroCancelamentoTrancamento WHERE (liberacaoFinanceiroCancelamentoTrancamento = ?)"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(sql, new Object[]{liberacaoFinanceiroCancelamentoTrancamento});
    }

    /**
     * Operação responsável por alterar todos os objetos da <code>HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO</code> contidos em um Hashtable no BD.
     * Faz uso da operação <code>excluirHistoricoLiberacaoFinanceiroCancelamentoTrancamentos</code> e <code>incluirHistoricoLiberacaoFinanceiroCancelamentoTrancamentos</code> disponíveis na classe <code>HistoricoLiberacaoFinanceiroCancelamentoTrancamento</code>.
     * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarHistoricoLiberacaoFinanceiroCancelamentoTrancamentos(Integer liberacaoFinanceiroCancelamentoTrancamento, List objetos ,UsuarioVO usuario) throws Exception {
        String str = "DELETE FROM HistoricoLiberacaoFinanceiroCancelamentoTrancamento WHERE liberacaoFinanceiroCancelamentoTrancamento = " + liberacaoFinanceiroCancelamentoTrancamento;
        Iterator i = objetos.iterator();
        while (i.hasNext()) {
            HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO objeto = (HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO) i.next();
            str += " AND codigo <> " + objeto.getCodigo().intValue();
        }
        getConexao().getJdbcTemplate().update(str+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO objeto = (HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO) e.next();
            if (objeto.getCodigo().equals(0)) {
                incluir(objeto, usuario);
            } else {
                alterar(objeto, usuario);
            }
        }
    }

    /**
     * Operação responsável por incluir objetos da <code>HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO</code> no BD.
     * Garantindo o relacionamento com a entidade principal <code>financeiro.LiberacaoFinanceiroCancelamentoTrancamento</code> através do atributo de vínculo.
     * @param objetos List contendo os objetos a serem gravados no BD da classe.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirHistoricoLiberacaoFinanceiroCancelamentoTrancamentos(Integer liberacaoFinanceiroCancelamentoTrancamentoPrm, List objetos,UsuarioVO usuario) throws Exception {
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO obj = (HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO) e.next();
            obj.getLiberacaoFinanceiroCancelamentoTrancamento().setCodigo(liberacaoFinanceiroCancelamentoTrancamentoPrm);
            incluir(obj, usuario);
        }
    }

    /**
     * Operação responsável por consultar todos os <code>HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO</code> relacionados a um objeto da classe <code>financeiro.LiberacaoFinanceiroCancelamentoTrancamento</code>.
     * @param liberacaoFinanceiroCancelamentoTrancamento  Atributo de <code>financeiro.LiberacaoFinanceiroCancelamentoTrancamento</code> a ser utilizado para localizar os objetos da classe <code>HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO</code>.
     * @return List  Contendo todos os objetos da classe <code>HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO</code> resultantes da consulta.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public static List consultarHistoricoLiberacaoFinanceiroCancelamentoTrancamentos(Integer liberacaoFinanceiroCancelamentoTrancamento, int nivelMontarDados ,UsuarioVO usuario) throws Exception {
        HistoricoLiberacaoFinanceiroCancelamentoTrancamento.consultar(getIdEntidade());
        List objetos = new ArrayList(0);
        String sql = "SELECT * FROM HistoricoLiberacaoFinanceiroCancelamentoTrancamento WHERE liberacaoFinanceiroCancelamentoTrancamento = ?";
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{liberacaoFinanceiroCancelamentoTrancamento});
        while (resultado.next()) {
            HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO novoObj = new HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO();
            novoObj = HistoricoLiberacaoFinanceiroCancelamentoTrancamento.montarDados(resultado, nivelMontarDados);
            objetos.add(novoObj);
        }
        return objetos;
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados ,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM HistoricoLiberacaoFinanceiroCancelamentoTrancamento WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( HistoricoLiberacaoFinanceiroCancelamentoTrancamento ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return HistoricoLiberacaoFinanceiroCancelamentoTrancamento.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        HistoricoLiberacaoFinanceiroCancelamentoTrancamento.idEntidade = idEntidade;
    }
}
