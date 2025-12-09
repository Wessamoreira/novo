package negocio.facade.jdbc.compras;

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
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.compras.CondicaoPagamentoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>CondicaoPagamentoVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>CondicaoPagamentoVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see CondicaoPagamentoVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy 
public class CondicaoPagamento extends ControleAcesso implements CondicaoPagamentoInterfaceFacade {

    protected static String idEntidade;

    public CondicaoPagamento() throws Exception {
        super();
        setIdEntidade("CondicaoPagamento");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>CondicaoPagamentoVO</code>.
     */
    public CondicaoPagamentoVO novo() throws Exception {
        CondicaoPagamento.incluir(getIdEntidade());
        CondicaoPagamentoVO obj = new CondicaoPagamentoVO();
        return obj;
    }

    @SuppressWarnings("static-access")
    public void verificarExistenciaCondicaoPagamento(String nomeCat, Integer codigo) throws Exception {
        String sqlStr = "SELECT * FROM CondicaoPagamento WHERE lower (nome) = ('" + nomeCat.toLowerCase() + "') ";
        if (codigo.intValue() != 0) {
            sqlStr += " and codigo != " + codigo.intValue();
        }

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        if (tabelaResultado.next()) {
            throw new Exception("Já existe um Condição Pagamento cadastrado com este nome.");
        }
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>CondicaoPagamentoVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>CondicaoPagamentoVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @SuppressWarnings("static-access")
    public void incluir(final CondicaoPagamentoVO obj, UsuarioVO usuarioVO) throws Exception {
        try {
            CondicaoPagamentoVO.validarDados(obj);
            CondicaoPagamento.incluir(getIdEntidade(), true, usuarioVO);
            verificarExistenciaCondicaoPagamento(obj.getNome(), 0);
            obj.realizarUpperCaseDados();
            final String sql = "INSERT INTO CondicaoPagamento( nome, nrParcela, intervaloParcela, entrada ) VALUES (?, ?, ?, ? ) returning codigo";
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    sqlInserir.setString(1, obj.getNome());
                    sqlInserir.setInt(2, obj.getNrParcela().intValue());
                    sqlInserir.setInt(3, obj.getIntervaloParcela().intValue());
                    sqlInserir.setBoolean(4, obj.isEntrada().booleanValue());
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
            getFacadeFactory().getParcelaCondicaoPagamentoFacade().incluirParcelaCondicaoPagamentos(obj.getCodigo(), obj.getParcelaCondicaoPagamentoVOs());
        } catch (Exception e) {
            obj.setCodigo(0);
            obj.setNovoObj(Boolean.TRUE);
            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>CondicaoPagamentoVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>CondicaoPagamentoVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @SuppressWarnings("static-access")
    public void alterar(final CondicaoPagamentoVO obj, UsuarioVO usuarioVO) throws Exception {
        try {
            CondicaoPagamentoVO.validarDados(obj);
            CondicaoPagamento.alterar(getIdEntidade(), true, usuarioVO);
            verificarExistenciaCondicaoPagamento(obj.getNome(), obj.getCodigo());
            obj.realizarUpperCaseDados();
            final String sql = "UPDATE CondicaoPagamento set nome=?, nrParcela=?, intervaloParcela=?, entrada=? WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setString(1, obj.getNome());
                    sqlAlterar.setInt(2, obj.getNrParcela().intValue());
                    sqlAlterar.setInt(3, obj.getIntervaloParcela().intValue());
                    sqlAlterar.setBoolean(4, obj.isEntrada().booleanValue());
                    sqlAlterar.setInt(5, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });

            getFacadeFactory().getParcelaCondicaoPagamentoFacade().alterarParcelaCondicaoPagamentos(obj.getCodigo(), obj.getParcelaCondicaoPagamentoVOs());
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>CondicaoPagamentoVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>CondicaoPagamentoVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @SuppressWarnings("static-access")
    public void excluir(CondicaoPagamentoVO obj, UsuarioVO usuarioVO) throws Exception {
        try {
            CondicaoPagamento.excluir(getIdEntidade(), true ,usuarioVO);
            String sql = "DELETE FROM CondicaoPagamento WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
            getFacadeFactory().getParcelaCondicaoPagamentoFacade().excluirParcelaCondicaoPagamentos(obj.getCodigo());
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>CondicaoPagamento</code> através do valor do atributo 
     * <code>String nome</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>CondicaoPagamentoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    @SuppressWarnings("static-access")
    public List consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM CondicaoPagamento WHERE upper( nome ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY nome";

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
    }

    /**
     * Responsável por realizar uma consulta de <code>CondicaoPagamento</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>CondicaoPagamentoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    @SuppressWarnings("static-access")
    public List<CondicaoPagamentoVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM CondicaoPagamento WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>CondicaoPagamentoVO</code> resultantes da consulta.
     */
    @SuppressWarnings("static-access")
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            CondicaoPagamentoVO obj = new CondicaoPagamentoVO();
            obj = montarDados(tabelaResultado, nivelMontarDados);
            vetResultado.add(obj);
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>CondicaoPagamentoVO</code>.
     * @return  O objeto da classe <code>CondicaoPagamentoVO</code> com os dados devidamente montados.
     */
    public static CondicaoPagamentoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados) throws Exception {
        CondicaoPagamentoVO obj = new CondicaoPagamentoVO();
        obj.setCodigo((dadosSQL.getInt("codigo")));
        obj.setNome(dadosSQL.getString("nome"));
        obj.setNrParcela((dadosSQL.getInt("nrParcela")));
        obj.setIntervaloParcela((dadosSQL.getInt("intervaloParcela")));
        obj.setEntrada((dadosSQL.getBoolean("entrada")));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        obj.setParcelaCondicaoPagamentoVOs(getFacadeFactory().getParcelaCondicaoPagamentoFacade().consultarParcelaCondicaoPagamentos(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS));
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS) {
            return obj;
        }

        return obj;
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>CondicaoPagamentoVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    @SuppressWarnings("static-access")
    public CondicaoPagamentoVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sql = "SELECT * FROM CondicaoPagamento WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] {codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( Condição de Pagamento ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return CondicaoPagamento.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        CondicaoPagamento.idEntidade = idEntidade;
    }
}