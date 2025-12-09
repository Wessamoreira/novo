package negocio.facade.jdbc.financeiro;

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
import negocio.comuns.financeiro.PerfilEconomicoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.PerfilEconomicoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>PerfilEconomicoVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>PerfilEconomicoVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see PerfilEconomicoVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy 
public class PerfilEconomico extends ControleAcesso implements PerfilEconomicoInterfaceFacade {

    protected static String idEntidade;

    public PerfilEconomico() throws Exception {
        super();
        setIdEntidade("PerfilEconomico");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>PerfilEconomicoVO</code>.
     */
    public PerfilEconomicoVO novo() throws Exception {
        PerfilEconomico.incluir(getIdEntidade());
        PerfilEconomicoVO obj = new PerfilEconomicoVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>PerfilEconomicoVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>PerfilEconomicoVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final PerfilEconomicoVO obj, UsuarioVO usuario) throws Exception {
        try {
            PerfilEconomico.incluir(getIdEntidade(), true, usuario);
            PerfilEconomicoVO.validarDados(obj);
            final String sql = "INSERT INTO PerfilEconomico( nome ) VALUES ( ? ) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    sqlInserir.setString(1, obj.getNome());
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
            getFacadeFactory().getPerfilEconomicoCondicaoNegociacaoFacade().incluirPerfilEconomicoCondicaoNegociacaos(obj.getCodigo(), obj.getPerfilEconomicoCondicaoNegociacaoVOs(), usuario);
            obj.setNovoObj(Boolean.FALSE);

        } catch (Exception e) {
            obj.setNovoObj(true);
            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>PerfilEconomicoVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>PerfilEconomicoVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final PerfilEconomicoVO obj, UsuarioVO usuario) throws Exception {
        try {

            PerfilEconomico.alterar(getIdEntidade(), true, usuario);
            PerfilEconomicoVO.validarDados(obj);
            final String sql = "UPDATE PerfilEconomico set nome=? WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setString(1, obj.getNome());
                    sqlAlterar.setInt(2, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });
            getFacadeFactory().getPerfilEconomicoCondicaoNegociacaoFacade().alterarPerfilEconomicoCondicaoNegociacaos(obj.getCodigo(), obj.getPerfilEconomicoCondicaoNegociacaoVOs(), usuario);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>PerfilEconomicoVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>PerfilEconomicoVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(final PerfilEconomicoVO obj, UsuarioVO usuario) throws Exception {
        try {
            PerfilEconomico.excluir(getIdEntidade(), true, usuario);
            String sql = "DELETE FROM PerfilEconomico WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
            getFacadeFactory().getPerfilEconomicoCondicaoNegociacaoFacade().excluirPerfilEconomicoCondicaoNegociacaos(obj.getCodigo(), usuario);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>PerfilEconomico</code> através do valor do atributo 
     * <code>String nome</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>PerfilEconomicoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM PerfilEconomico WHERE upper( nome ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado,nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>PerfilEconomico</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>PerfilEconomicoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<PerfilEconomicoVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM PerfilEconomico WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado,nivelMontarDados, usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>PerfilEconomicoVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados , UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>PerfilEconomicoVO</code>.
     * @return  O objeto da classe <code>PerfilEconomicoVO</code> com os dados devidamente montados.
     */
    public static PerfilEconomicoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        PerfilEconomicoVO obj = new PerfilEconomicoVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setNome(dadosSQL.getString("nome"));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
//        obj.setPerfilEconomicoCondicaoNegociacaoVOs(PerfilEconomicoCondicaoNegociacao.consultarPerfilEconomicoCondicaoNegociacaos(obj.getCodigo(), nivelMontarDados,usuario));
//        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS) {
//            return obj;
//        }

        return obj;
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>PerfilEconomicoVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public PerfilEconomicoVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sql = "SELECT * FROM PerfilEconomico WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
         if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( PerfilEconomico ).");
        }
        return (montarDados(tabelaResultado,nivelMontarDados, usuario));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return PerfilEconomico.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        PerfilEconomico.idEntidade = idEntidade;
    }
}
