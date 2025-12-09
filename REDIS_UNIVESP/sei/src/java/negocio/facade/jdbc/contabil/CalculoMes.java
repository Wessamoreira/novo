package negocio.facade.jdbc.contabil;

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

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.contabil.CalculoMesVO;
import negocio.comuns.contabil.PlanoContaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.arquitetura.SuperFacadeJDBC;
import negocio.interfaces.contabil.CalculoMesInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>CalculoMesVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>CalculoMesVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see CalculoMesVO
 * @see SuperEntidade
 */
@Repository
@Scope("singleton")
@Lazy
public class CalculoMes extends SuperFacadeJDBC implements CalculoMesInterfaceFacade {

    protected static String idEntidade;

    public CalculoMes() throws Exception {
        super();
        setIdEntidade("CalculoMes");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>CalculoMesVO</code>.
     */
    public CalculoMesVO novo() throws Exception {
        incluir(getIdEntidade());
        CalculoMesVO obj = new CalculoMesVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>CalculoMesVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>CalculoMesVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final CalculoMesVO obj) throws Exception {
        try {
            CalculoMesVO.validarDados(obj);
            incluir(getIdEntidade());
            final String sql = "INSERT INTO calculoMes(  planoConta, unidadeEnsino, valorDebito, valorCredito, mes, ano ) VALUES ( ?, ?, ?, ?, ?, ? ) returning codigo";

            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    if (obj.getPlanoConta().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(1, obj.getPlanoConta().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(1, 0);
                    }
                    if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(2, obj.getUnidadeEnsino().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(2, 0);
                    }
                    sqlInserir.setDouble(3, obj.getValorDebito().doubleValue());
                    sqlInserir.setDouble(4, obj.getValorCredito().doubleValue());
                    sqlInserir.setInt(5, obj.getMes().intValue());
                    sqlInserir.setInt(6, obj.getAno().intValue());
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
        } finally {
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>CalculoMesVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>CalculoMesVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final CalculoMesVO obj) throws Exception {
        try {
            CalculoMesVO.validarDados(obj);
            alterar(getIdEntidade());
            final String sql = "UPDATE calculoMes set planoConta=?, unidadeEnsino=?, valorDebito=?, valorCredito=?, mes=?, ano=? WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    if (obj.getPlanoConta().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(1, obj.getPlanoConta().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(1, 0);
                    }
                    if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(2, obj.getUnidadeEnsino().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(2, 0);
                    }
                    sqlAlterar.setDouble(3, obj.getValorDebito().doubleValue());
                    sqlAlterar.setDouble(4, obj.getValorCredito().doubleValue());
                    sqlAlterar.setInt(5, obj.getMes().intValue());
                    sqlAlterar.setInt(6, obj.getAno().intValue());
                    sqlAlterar.setInt(7, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });
        } catch (Exception e) {
            throw e;
        } finally {
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>CalculoMesVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>CalculoMesVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(CalculoMesVO obj) throws Exception {
        try {
            excluir(getIdEntidade());
            String sql = "DELETE FROM calculoMes WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        } finally {
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>calculoMes</code> através do valor do atributo 
     * <code>Integer ano</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>CalculoMesVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorAnoMes(Integer ano, Integer mes, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM calculoMes WHERE ano = " + ano + " AND mes = " + mes + " ";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
    }

    public Double consultarPorAnoMesPlanoConta(int ano, int mes, int conta, String tipoConta, boolean controlarAcesso, int unidadeEnsinoLogada,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar("ConferenciaCalculoMes", controlarAcesso,usuario);
        String sqlStr = "SELECT " + tipoConta + " FROM calculoMes WHERE ano = " + ano + " AND mes = " + mes
                + " AND planoConta = " + conta + " AND unidadeEnsino = " + unidadeEnsinoLogada + "";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        if (tabelaResultado.next()) {
            return (new Double(tabelaResultado.getDouble(tipoConta)));
        } else {
            return new Double(0.0);
        }
    }

    public List consultarPorAno(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM calculoMes WHERE ano >= " + valorConsulta.intValue() + " ORDER BY ano";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>calculoMes</code> através do valor do atributo 
     * <code>Integer mes</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>CalculoMesVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorMes(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM calculoMes WHERE mes >= " + valorConsulta.intValue() + " ORDER BY mes";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>calculoMes</code> através do valor do atributo 
     * <code>Double valorCredito</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>CalculoMesVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorValorCredito(Double valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM calculoMes WHERE valorCredito >= " + valorConsulta.doubleValue() + " ORDER BY valorCredito";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>calculoMes</code> através do valor do atributo 
     * <code>Double valorDesconto</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>CalculoMesVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorValorDebito(Double valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM calculoMes WHERE valorDebito >= " + valorConsulta.doubleValue() + " ORDER BY valorDebito";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>calculoMes</code> através do valor do atributo 
     * <code>nomeFantasia</code> da classe <code>UnidadeEnsino</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>CalculoMesVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNomeFantasiaUnidadeEnsino(String valorConsulta, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), true,usuario);
        String sqlStr = "SELECT calculoMes.* FROM calculoMes, UnidadeEnsino WHERE calculoMes.unidadeEnsino = UnidadeEnsino.codigo and upper( UnidadeEnsino.nomeFantasia ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY UnidadeEnsino.nomeFantasia";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>calculoMes</code> através do valor do atributo 
     * <code>descricao</code> da classe <code>PlanoConta</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>CalculoMesVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorDescricaoPlanoConta(String valorConsulta, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), true,usuario);
        String sqlStr = "SELECT calculoMes.* FROM calculoMes, PlanoConta WHERE calculoMes.planoConta = PlanoConta.codigo and upper( PlanoConta.descricao ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY PlanoConta.descricao";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario);
    }


    /**
     * Responsável por realizar uma consulta de <code>calculoMes</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>CalculoMesVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM calculoMes WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
    }

    public List obterCodigosCalculoAnterior(Integer mes, Integer ano, Integer unidadeEnsino) throws Exception {
        String sqlStr = "select codigo from calculoMes where unidadeEnsino IS NULL AND mes=" + mes + " AND ano=" + ano;
        if (unidadeEnsino != 0) {
            sqlStr = "select codigo from calculoMes where unidadeEnsino=" + unidadeEnsino + " AND mes=" + mes + " AND ano=" + ano;
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        List vetResultado = new ArrayList();
        while (tabelaResultado.next()) {
            Integer obj = 0;
            obj = (new Integer(tabelaResultado.getInt("codigo")));
            vetResultado.add(obj);
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>CalculoMesVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList();
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados,usuario));
        }
        tabelaResultado = null;
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>CalculoMesVO</code>.
     * @return  O objeto da classe <code>CalculoMesVO</code> com os dados devidamente montados.
     */
    public static CalculoMesVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        CalculoMesVO obj = new CalculoMesVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.getPlanoConta().setCodigo(new Integer(dadosSQL.getInt("planoConta")));
        obj.getUnidadeEnsino().setCodigo(new Integer(dadosSQL.getInt("unidadeEnsino")));
        obj.setValorDebito(new Double(dadosSQL.getDouble("valorDebito")));
        obj.setValorCredito(new Double(dadosSQL.getDouble("valorCredito")));
        obj.setMes(new Integer(dadosSQL.getInt("mes")));
        obj.setAno(new Integer(dadosSQL.getInt("ano")));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }

        montarDadosPlanoConta(obj, nivelMontarDados,usuario);
        montarDadosUnidadeEnsino(obj, nivelMontarDados,usuario);
        return obj;
    }

    public static CalculoMesVO montarDadosAuxiliar(ResultSet dadosSQL) throws Exception {
        CalculoMesVO obj = new CalculoMesVO();
        obj.setMes(new Integer(dadosSQL.getInt("mes")));
        obj.setAno(new Integer(dadosSQL.getInt("ano")));
        obj.setNovoObj(Boolean.FALSE);
        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>UnidadeEnsinoVO</code> relacionado ao objeto <code>CalculoMesVO</code>.
     * Faz uso da chave primária da classe <code>UnidadeEnsinoVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosUnidadeEnsino(CalculoMesVO obj, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if (obj.getUnidadeEnsino().getCodigo().intValue() == 0) {
            obj.setUnidadeEnsino(new UnidadeEnsinoVO());
            return;
        }
        obj.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsino().getCodigo(), false, nivelMontarDados,usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>PlanoContaVO</code> relacionado ao objeto <code>CalculoMesVO</code>.
     * Faz uso da chave primária da classe <code>PlanoContaVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosPlanoConta(CalculoMesVO obj, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if (obj.getPlanoConta().getCodigo().intValue() == 0) {
            obj.setPlanoConta(new PlanoContaVO());
            return;
        }
        obj.setPlanoConta(getFacadeFactory().getPlanoContaFacade().consultarPorChavePrimaria(obj.getPlanoConta().getCodigo(), nivelMontarDados,usuario));
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>CalculoMesVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public CalculoMesVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false,usuario);
        String sql = "SELECT * FROM calculoMes WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( calculoMes ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados,usuario));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return CalculoMes.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        CalculoMes.idEntidade = idEntidade;
    }
}
