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
import negocio.comuns.financeiro.CondicaoNegociacaoVO;
import negocio.comuns.financeiro.PerfilEconomicoCondicaoNegociacaoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.PerfilEconomicoCondicaoNegociacaoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>PerfilEconomicoCondicaoNegociacaoVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>PerfilEconomicoCondicaoNegociacaoVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see PerfilEconomicoCondicaoNegociacaoVO
 * @see ControleAcesso
 * @see PerfilEconomico
 */
@Repository
@Scope("singleton")
@Lazy 
public class PerfilEconomicoCondicaoNegociacao extends ControleAcesso implements PerfilEconomicoCondicaoNegociacaoInterfaceFacade{

    protected static String idEntidade;

    public PerfilEconomicoCondicaoNegociacao() throws Exception {
        super();
        setIdEntidade("PerfilEconomico");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>PerfilEconomicoCondicaoNegociacaoVO</code>.
     */
    public PerfilEconomicoCondicaoNegociacaoVO novo() throws Exception {
        PerfilEconomicoCondicaoNegociacao.incluir(getIdEntidade());
        PerfilEconomicoCondicaoNegociacaoVO obj = new PerfilEconomicoCondicaoNegociacaoVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>PerfilEconomicoCondicaoNegociacaoVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>PerfilEconomicoCondicaoNegociacaoVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final PerfilEconomicoCondicaoNegociacaoVO obj,UsuarioVO usuario) throws Exception {
        try {
            PerfilEconomicoCondicaoNegociacaoVO.validarDados(obj);
            final String sql = "INSERT INTO PerfilEconomicoCondicaoNegociacao( perfilEconomico, condicaoNegociacao ) VALUES ( ?, ? ) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    if (obj.getPerfilEconomico().intValue() != 0) {
                        sqlInserir.setInt(1, obj.getPerfilEconomico().intValue());
                    } else {
                        sqlInserir.setNull(1, 0);
                    }
                    if (obj.getCondicaoNegociacao().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(2, obj.getCondicaoNegociacao().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(2, 0);
                    }
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
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>PerfilEconomicoCondicaoNegociacaoVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>PerfilEconomicoCondicaoNegociacaoVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final PerfilEconomicoCondicaoNegociacaoVO obj,UsuarioVO usuario) throws Exception {
        try {

            PerfilEconomicoCondicaoNegociacao.alterar(getIdEntidade());
            PerfilEconomicoCondicaoNegociacaoVO.validarDados(obj);
            final String sql = "UPDATE PerfilEconomicoCondicaoNegociacao set perfilEconomico=?, condicaoNegociacao=? WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    if (obj.getPerfilEconomico().intValue() != 0) {
                        sqlAlterar.setInt(1, obj.getPerfilEconomico().intValue());
                    } else {
                        sqlAlterar.setNull(1, 0);
                    }
                    if (obj.getCondicaoNegociacao().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(2, obj.getCondicaoNegociacao().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(2, 0);
                    }
                    sqlAlterar.setInt(3, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>PerfilEconomicoCondicaoNegociacaoVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>PerfilEconomicoCondicaoNegociacaoVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(PerfilEconomicoCondicaoNegociacaoVO obj,UsuarioVO usuario) throws Exception {
        try {
            PerfilEconomicoCondicaoNegociacao.excluir(getIdEntidade());
            String sql = "DELETE FROM PerfilEconomicoCondicaoNegociacao WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>PerfilEconomicoCondicaoNegociacao</code> através do valor do atributo 
     * <code>codigo</code> da classe <code>CondicaoNegociacao</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>PerfilEconomicoCondicaoNegociacaoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigoCondicaoNegociacao(Integer valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), true, usuario);
        String sqlStr = "SELECT PerfilEconomicoCondicaoNegociacao.* FROM PerfilEconomicoCondicaoNegociacao, CondicaoNegociacao WHERE PerfilEconomicoCondicaoNegociacao.condicaoNegociacao = CondicaoNegociacao.codigo and CondicaoNegociacao.codigo >= " + valorConsulta.intValue() + " ORDER BY CondicaoNegociacao.codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado,nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>PerfilEconomicoCondicaoNegociacao</code> através do valor do atributo 
     * <code>codigo</code> da classe <code>PerfilEconomico</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>PerfilEconomicoCondicaoNegociacaoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigoPerfilEconomico(Integer valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), true, usuario);
        String sqlStr = "SELECT PerfilEconomicoCondicaoNegociacao.* FROM PerfilEconomicoCondicaoNegociacao, PerfilEconomico WHERE PerfilEconomicoCondicaoNegociacao.perfilEconomico = PerfilEconomico.codigo and PerfilEconomico.codigo >= " + valorConsulta.intValue() + " ORDER BY PerfilEconomico.codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado,nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>PerfilEconomicoCondicaoNegociacao</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>PerfilEconomicoCondicaoNegociacaoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), true, usuario);
        String sqlStr = "SELECT * FROM PerfilEconomicoCondicaoNegociacao WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado,nivelMontarDados, usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>PerfilEconomicoCondicaoNegociacaoVO</code> resultantes da consulta.
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
     * em um objeto da classe <code>PerfilEconomicoCondicaoNegociacaoVO</code>.
     * @return  O objeto da classe <code>PerfilEconomicoCondicaoNegociacaoVO</code> com os dados devidamente montados.
     */
    public static PerfilEconomicoCondicaoNegociacaoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        PerfilEconomicoCondicaoNegociacaoVO obj = new PerfilEconomicoCondicaoNegociacaoVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setPerfilEconomico(new Integer(dadosSQL.getInt("perfilEconomico")));
        obj.getCondicaoNegociacao().setCodigo(new Integer(dadosSQL.getInt("condicaoNegociacao")));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }

        montarDadosCondicaoNegociacao(obj, nivelMontarDados, usuario);
        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>CondicaoNegociacaoVO</code> relacionado ao objeto <code>PerfilEconomicoCondicaoNegociacaoVO</code>.
     * Faz uso da chave primária da classe <code>CondicaoNegociacaoVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosCondicaoNegociacao(PerfilEconomicoCondicaoNegociacaoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getCondicaoNegociacao().getCodigo().intValue() == 0) {
            obj.setCondicaoNegociacao(new CondicaoNegociacaoVO());
            return;
        }
        obj.setCondicaoNegociacao(getFacadeFactory().getCondicaoNegociacaoFacade().consultarPorChavePrimaria(obj.getCondicaoNegociacao().getCodigo(), false, nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por excluir todos os objetos da <code>PerfilEconomicoCondicaoNegociacaoVO</code> no BD.
     * Faz uso da operação <code>excluir</code> disponível na classe <code>PerfilEconomicoCondicaoNegociacao</code>.
     * @param <code>perfilEconomico</code> campo chave para exclusão dos objetos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirPerfilEconomicoCondicaoNegociacaos(Integer perfilEconomico,UsuarioVO usuario) throws Exception {
        try {
            String sql = "DELETE FROM PerfilEconomicoCondicaoNegociacao WHERE (perfilEconomico = ?)"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sql, new Object[]{perfilEconomico.intValue()});
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por alterar todos os objetos da <code>PerfilEconomicoCondicaoNegociacaoVO</code> contidos em um Hashtable no BD.
     * Faz uso da operação <code>excluirPerfilEconomicoCondicaoNegociacaos</code> e <code>incluirPerfilEconomicoCondicaoNegociacaos</code> disponíveis na classe <code>PerfilEconomicoCondicaoNegociacao</code>.
     * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarPerfilEconomicoCondicaoNegociacaos(Integer perfilEconomico, List objetos,UsuarioVO usuario) throws Exception {
        excluirPerfilEconomicoCondicaoNegociacaos(perfilEconomico, usuario);
        incluirPerfilEconomicoCondicaoNegociacaos(perfilEconomico, objetos, usuario);
    }

    /**
     * Operação responsável por incluir objetos da <code>PerfilEconomicoCondicaoNegociacaoVO</code> no BD.
     * Garantindo o relacionamento com a entidade principal <code>financeiro.PerfilEconomico</code> através do atributo de vínculo.
     * @param objetos List contendo os objetos a serem gravados no BD da classe.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirPerfilEconomicoCondicaoNegociacaos(Integer perfilEconomicoPrm, List objetos,UsuarioVO usuario) throws Exception {
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            PerfilEconomicoCondicaoNegociacaoVO obj = (PerfilEconomicoCondicaoNegociacaoVO) e.next();
            obj.setPerfilEconomico(perfilEconomicoPrm);
            incluir(obj, usuario);
        }
    }

    /**
     * Operação responsável por consultar todos os <code>PerfilEconomicoCondicaoNegociacaoVO</code> relacionados a um objeto da classe <code>financeiro.PerfilEconomico</code>.
     * @param perfilEconomico  Atributo de <code>financeiro.PerfilEconomico</code> a ser utilizado para localizar os objetos da classe <code>PerfilEconomicoCondicaoNegociacaoVO</code>.
     * @return List  Contendo todos os objetos da classe <code>PerfilEconomicoCondicaoNegociacaoVO</code> resultantes da consulta.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public static List consultarPerfilEconomicoCondicaoNegociacaos(Integer perfilEconomico, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), true, usuario);
        String sqlStr = "SELECT * FROM PerfilEconomicoCondicaoNegociacao WHERE perfilEconomico = "+ perfilEconomico.intValue();
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado,nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>PerfilEconomicoCondicaoNegociacaoVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public PerfilEconomicoCondicaoNegociacaoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM PerfilEconomicoCondicaoNegociacao WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
         if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( PerfilEconomicoCondicaoNegociacao ).");
        }
        return (montarDados(tabelaResultado,nivelMontarDados, usuario));
    }


    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return PerfilEconomicoCondicaoNegociacao.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        PerfilEconomicoCondicaoNegociacao.idEntidade = idEntidade;
    }
}
