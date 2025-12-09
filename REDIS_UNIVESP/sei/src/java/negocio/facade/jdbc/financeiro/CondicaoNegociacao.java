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
import negocio.comuns.compras.CondicaoPagamentoVO;
import negocio.comuns.financeiro.CondicaoNegociacaoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.CondicaoNegociacaoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>CondicaoNegociacaoVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>CondicaoNegociacaoVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see CondicaoNegociacaoVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class CondicaoNegociacao extends ControleAcesso implements CondicaoNegociacaoInterfaceFacade {

    protected static String idEntidade;

    public CondicaoNegociacao() throws Exception {
        super();
        setIdEntidade("CondicaoNegociacao");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>CondicaoNegociacaoVO</code>.
     */
    public CondicaoNegociacaoVO novo() throws Exception {
        CondicaoNegociacao.incluir(getIdEntidade());
        CondicaoNegociacaoVO obj = new CondicaoNegociacaoVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>CondicaoNegociacaoVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>CondicaoNegociacaoVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final CondicaoNegociacaoVO obj) throws Exception {
        try {
            CondicaoNegociacaoVO.validarDados(obj);
            validarUnicidadeCondicaoPagamento(obj.getCondicaoPagamento().getCodigo());
            CondicaoNegociacao.incluir(getIdEntidade());
            final String sql = "INSERT INTO CondicaoNegociacao( condicaoPagamento, juro, desconto, valorMinimoValido, valorMaximoValido ) VALUES ( ?, ?, ?, ?, ? ) returning codigo";
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {

                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    if (obj.getCondicaoPagamento().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(1, obj.getCondicaoPagamento().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(1, 0);
                    }
                    sqlInserir.setDouble(2, obj.getJuro().doubleValue());
                    sqlInserir.setDouble(3, obj.getDesconto().doubleValue());
                    sqlInserir.setDouble(4, obj.getValorMinimoValido().doubleValue());
                    sqlInserir.setDouble(5, obj.getValorMaximoValido().doubleValue());
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
            obj.setNovoObj(Boolean.TRUE);
            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>CondicaoNegociacaoVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>CondicaoNegociacaoVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final CondicaoNegociacaoVO obj) throws Exception {
        try {
            CondicaoNegociacaoVO.validarDados(obj);
            CondicaoNegociacao.alterar(getIdEntidade());
            final String sql = "UPDATE CondicaoNegociacao set condicaoPagamento=?, juro=?, desconto=?, valorMinimoValido=?, valorMaximoValido=? WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    if (obj.getCondicaoPagamento().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(1, obj.getCondicaoPagamento().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(1, 0);
                    }
                    sqlAlterar.setDouble(2, obj.getJuro().doubleValue());
                    sqlAlterar.setDouble(3, obj.getDesconto().doubleValue());
                    sqlAlterar.setDouble(4, obj.getValorMinimoValido().doubleValue());
                    sqlAlterar.setDouble(5, obj.getValorMaximoValido().doubleValue());
                    sqlAlterar.setInt(6, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>CondicaoNegociacaoVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>CondicaoNegociacaoVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(CondicaoNegociacaoVO obj) throws Exception {
        try {
            CondicaoNegociacao.excluir(getIdEntidade());
            String sql = "DELETE FROM CondicaoNegociacao WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>CondicaoNegociacao</code> através do valor do atributo 
     * <code>nome</code> da classe <code>CondicaoPagamento</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>CondicaoNegociacaoVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNomeCondicaoPagamento(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), true, usuario);
        String sqlStr = "SELECT CondicaoNegociacao.* FROM CondicaoNegociacao, CondicaoPagamento WHERE CondicaoNegociacao.condicaoPagamento = CondicaoPagamento.codigo and upper( CondicaoPagamento.nome ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY CondicaoPagamento.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>CondicaoNegociacao</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>CondicaoNegociacaoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM CondicaoNegociacao WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>CondicaoNegociacaoVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados,usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>CondicaoNegociacaoVO</code>.
     * @return  O objeto da classe <code>CondicaoNegociacaoVO</code> com os dados devidamente montados.
     */
    public static CondicaoNegociacaoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        CondicaoNegociacaoVO obj = new CondicaoNegociacaoVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.getCondicaoPagamento().setCodigo(new Integer(dadosSQL.getInt("condicaoPagamento")));
        montarDadosCondicaoPagamento(obj, nivelMontarDados,usuario);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
            return obj;
        }
        obj.setJuro(new Double(dadosSQL.getDouble("juro")));
        obj.setDesconto(new Double(dadosSQL.getDouble("desconto")));
        obj.setValorMinimoValido(new Double(dadosSQL.getDouble("valorMinimoValido")));
        obj.setValorMaximoValido(new Double(dadosSQL.getDouble("valorMaximoValido")));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }

        montarDadosCondicaoPagamento(obj, nivelMontarDados,usuario);
        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>CondicaoPagamentoVO</code> relacionado ao objeto <code>CondicaoNegociacaoVO</code>.
     * Faz uso da chave primária da classe <code>CondicaoPagamentoVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosCondicaoPagamento(CondicaoNegociacaoVO obj, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if (obj.getCondicaoPagamento().getCodigo().intValue() == 0) {
            obj.setCondicaoPagamento(new CondicaoPagamentoVO());
            return;
        }
        obj.setCondicaoPagamento(getFacadeFactory().getCondicaoPagamentoFacade().consultarPorChavePrimaria(obj.getCondicaoPagamento().getCodigo(), false, nivelMontarDados,usuario));
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>CondicaoNegociacaoVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public CondicaoNegociacaoVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM CondicaoNegociacao WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( CondicaoNegociacao ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados,usuario));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return CondicaoNegociacao.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        CondicaoNegociacao.idEntidade = idEntidade;
    }

    public void validarUnicidadeCondicaoPagamento(Integer condicaoPagamento) throws Exception {
        StringBuilder sql = new StringBuilder();
        try {
            sql.append("select * from condicaoNegociacao where condicaoPagamento = ");
            sql.append(condicaoPagamento);
            SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
            if (tabelaResultado.next()) {
                throw new ConsistirException("CONDIÇÃO NEGOCIAÇÃO já gravada com essa Condição Pagamento, escolha outra Condição Pagamento");
            }
        } catch (Exception e) {
            throw e;
        } finally {
            sql = null;
        }

    }
}
