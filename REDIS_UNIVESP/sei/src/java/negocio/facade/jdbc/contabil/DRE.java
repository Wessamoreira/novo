package negocio.facade.jdbc.contabil;

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

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.contabil.DREVO;
import negocio.comuns.contabil.PlanoContaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.contabil.DREInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>DREVO</code>. Responsável por implementar operações
 * como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>DREVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see DREVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class DRE extends ControleAcesso implements DREInterfaceFacade {

    protected static String idEntidade;

    public DRE() throws Exception {
        super();
        setIdEntidade("DRE");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe
     * <code>DREVO</code>.
     */
    public DREVO novo() throws Exception {
        incluir(getIdEntidade());
        DREVO obj = new DREVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe
     * <code>DREVO</code>. Primeiramente valida os dados (
     * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de
     * dados e a permissão do usuário para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>DREVO</code> que será gravado no banco
     *            de dados.
     * @exception Exception
     *                Caso haja problemas de conexão, restrição de acesso ou
     *                validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final DREVO obj) throws Exception {
        try {
            DREVO.validarDados(obj);
            incluir(getIdEntidade());
            final String sql = "INSERT INTO DRE( unidadeensino, ordem, planoConta, sinal ) VALUES ( ?, ?, ?, ? ) returning codigo";
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(1, obj.getUnidadeEnsino().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(1, 0);
                    }
                    sqlInserir.setInt(2, obj.getOrdem());
                    if (obj.getPlanoConta().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(3, obj.getPlanoConta().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(3, 0);
                    }
                    sqlInserir.setString(4, obj.getSinal());
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
        } catch (Exception e) {
            obj.setNovoObj(Boolean.TRUE);
            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe
     * <code>DREVO</code>. Sempre utiliza a chave primária da classe como
     * atributo para localização do registro a ser alterado. Primeiramente
     * valida os dados ( <code>validarDados</code>) do objeto. Verifica a
     * conexão com o banco de dados e a permissão do usuário para realizar esta
     * operacão na entidade. Isto, através da operação <code>alterar</code> da
     * superclasse.
     *
     * @param obj
     *            Objeto da classe <code>DREVO</code> que será alterada no banco
     *            de dados.
     * @exception Execption
     *                Caso haja problemas de conexão, restrição de acesso ou
     *                validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final DREVO obj) throws Exception {
        try {
            alterar(getIdEntidade());
            DREVO.validarDados(obj);
            final String sql = "UPDATE DRE set unidadeensino=?, ordem=?, planoConta=?, sinal=? WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(1, obj.getUnidadeEnsino().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(1, 0);
                    }
                    sqlAlterar.setInt(2, obj.getOrdem());
                    if (obj.getPlanoConta().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(3, obj.getPlanoConta().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(3, 0);
                    }
                    sqlAlterar.setString(4, obj.getSinal());
                    sqlAlterar.setInt(5, obj.getCodigo());
                    return sqlAlterar;
                }
            });

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe
     * <code>DREVO</code>. Sempre localiza o registro a ser excluído através da
     * chave primária da entidade. Primeiramente verifica a conexão com o banco
     * de dados e a permissão do usuário para realizar esta operacão na
     * entidade. Isto, através da operação <code>excluir</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>DREVO</code> que será removido no banco
     *            de dados.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(DREVO obj) throws Exception {
        try {
            DRE.excluir(getIdEntidade());
            String sql = "DELETE FROM DRE WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirPorUnidadeEnsino(Integer unidadeEnsino) throws Exception {
        try {
            DRE.excluir(getIdEntidade());
            String sql = "DELETE FROM DRE WHERE ((unidadeEnsino = ?))";
            getConexao().getJdbcTemplate().update(sql, new Object[]{unidadeEnsino});
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirDRE(List<DREVO> lista, Integer unidadeEnsino) throws Exception {
        excluirPorUnidadeEnsino(unidadeEnsino);
        for (DREVO obj : lista) {
            incluir(obj);
        }
    }

    public void moverParaBaixo(DREVO obj, List listaDRE) {
        DREVO aux = new DREVO();
        Iterator i = listaDRE.iterator();
        while (i.hasNext()) {
            DREVO dre = (DREVO) i.next();
            if (dre.getOrdem() == obj.getOrdem() + 1) {
                aux = obj;
                obj = dre;
                dre = aux;
                listaDRE.set(obj.getOrdem().intValue(), dre);
                listaDRE.set(dre.getOrdem().intValue(), obj);
                break;
            }
        }
        enumeraQuestionario(listaDRE);
    }

    public void enumeraQuestionario(List listaDRE) {
        Iterator i = listaDRE.iterator();
        int cont = 0;
        while (i.hasNext()) {
            DREVO obj = (DREVO) i.next();
            obj.setOrdem(cont);
            cont++;
        }
    }

    public void moverParaCima(DREVO obj, List listaDRE) {
        DREVO aux = new DREVO();
        Iterator i = listaDRE.iterator();
        while (i.hasNext()) {
            DREVO dre = (DREVO) i.next();
            if (dre.getOrdem() == obj.getOrdem() - 1) {
                aux = obj;
                obj = dre;
                dre = aux;
                listaDRE.set(obj.getOrdem().intValue(), dre);
                listaDRE.set(dre.getOrdem().intValue(), obj);
                break;
            }
        }
        enumeraQuestionario(listaDRE);
    }

    /**
     * Responsável por realizar uma consulta de <code>DRE</code> através do
     * valor do atributo <code>descricao</code> da classe
     * <code>PlanoConta</code> Faz uso da operação
     * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
     * List resultante.
     *
     * @return List Contendo vários objetos da classe <code>DREVO</code>
     *         resultantes da consulta.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<DREVO> consultarPorDescricaoPlanoConta(String valorConsulta, Integer unidadeEnsino, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), true,usuario);
        String sqlStr = "";
        if (unidadeEnsino == null || unidadeEnsino == 0) {
            sqlStr = "SELECT DRE.* FROM DRE, PlanoConta WHERE DRE.planoConta = PlanoConta.codigo and upper( PlanoConta.descricao ) like('" + valorConsulta.toUpperCase() + "%') AND DRE.unidadeensino is null ORDER BY PlanoConta.descricao";
        } else {
            sqlStr = "SELECT DRE.* FROM DRE, PlanoConta WHERE DRE.planoConta = PlanoConta.codigo and upper( PlanoConta.descricao ) like('" + valorConsulta.toUpperCase() + "%') AND DRE.unidadeensino = " + unidadeEnsino + " ORDER BY PlanoConta.descricao";
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>DRE</code> através do
     * valor do atributo <code>Integer ordem</code> . Retorna os objetos com
     * valores iguais ou superiores ao parâmetro fornecido. Faz uso da operação
     * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
     * List resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui
     *            permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>DREVO</code>
     *         resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<DREVO> consultarPorOrdem(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "";
        if (unidadeEnsino == null || unidadeEnsino == 0) {
            sqlStr = "SELECT * FROM DRE WHERE ordem >= " + valorConsulta.intValue() + " AND DRE.unidadeensino is null ORDER BY ordem";
        } else {
            sqlStr = "SELECT * FROM DRE WHERE ordem >= " + valorConsulta.intValue() + " AND DRE.unidadeensino = " + unidadeEnsino + " ORDER BY ordem";
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));

    }

    /**
     * Responsável por realizar uma consulta de <code>DRE</code> através do
     * valor do atributo <code>Integer codigoAuxililar</code>. Retorna os
     * objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso
     * da operação <code>montarDadosConsulta</code> que realiza o trabalho de
     * prerarar o List resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui
     *            permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>DREVO</code>
     *         resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<DREVO> consultarPorCodigoAuxililar(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "";
        if (unidadeEnsino == null || unidadeEnsino == 0) {
            sqlStr = "SELECT * FROM DRE WHERE codigoAuxiliar >= " + valorConsulta.intValue() + " AND DRE.unidadeEnsino is null ORDER BY codigoAuxiliar";
        } else {
            sqlStr = "SELECT * FROM DRE WHERE codigoAuxiliar >= " + valorConsulta.intValue() + " AND DRE.unidadeEnsino = " + unidadeEnsino + " ORDER BY codigoAuxiliar";
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>DRE</code> através do
     * valor do atributo <code>Integer codigo</code>. Retorna os objetos com
     * valores iguais ou superiores ao parâmetro fornecido. Faz uso da operação
     * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
     * List resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui
     *            permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>DREVO</code>
     *         resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<DREVO> consultarPorCodigo(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "";
        if (unidadeEnsino == null || unidadeEnsino == 0) {
            sqlStr = "SELECT * FROM DRE WHERE codigo >= " + valorConsulta.intValue() + " AND DRE.unidadeensino is null ORDER BY codigo";
        } else {
            sqlStr = "SELECT * FROM DRE WHERE codigo >= " + valorConsulta.intValue() + " AND DRE.unidadeensino = " + unidadeEnsino + " ORDER BY codigo";
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));

    }

    public List<DREVO> consultarPorUnidadeEnsino(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "";
        if (valorConsulta == null || valorConsulta == 0) {
            sqlStr = "SELECT * FROM DRE WHERE unidadeEnsino is null ORDER BY codigo";
        } else {
            sqlStr = "SELECT * FROM DRE WHERE unidadeEnsino = " + valorConsulta.intValue() + " ORDER BY codigo";
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma
     * consulta ao banco de dados ( <code>ResultSet</code>). Faz uso da operação
     * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     *
     * @return List Contendo vários objetos da classe <code>DREVO</code>
     *         resultantes da consulta.
     */
    public static List<DREVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        List<DREVO> vetResultado = new ArrayList<DREVO>(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados,usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de
     * dados (<code>ResultSet</code>) em um objeto da classe <code>DREVO</code>.
     *
     * @return O objeto da classe <code>DREVO</code> com os dados devidamente
     *         montados.
     */
    public static DREVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        DREVO obj = new DREVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.getUnidadeEnsino().setCodigo(new Integer(dadosSQL.getInt("unidadeEnsino")));
        obj.setOrdem(new Integer(dadosSQL.getInt("ordem")));
        obj.setSinal(dadosSQL.getString("sinal"));
        obj.getPlanoConta().setCodigo(new Integer(dadosSQL.getInt("planoConta")));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        montarDadosUnidadeEnsino(obj, nivelMontarDados, usuario);
        montarDadosPlanoConta(obj, nivelMontarDados,usuario);
        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe
     * <code>PlanoContaVO</code> relacionado ao objeto <code>DREVO</code>. Faz
     * uso da chave primária da classe <code>PlanoContaVO</code> para realizar a
     * consulta.
     *
     * @param obj
     *            Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosPlanoConta(DREVO obj, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if (obj.getPlanoConta().getCodigo().intValue() == 0) {
            obj.setPlanoConta(new PlanoContaVO());
            return;
        }
        obj.setPlanoConta(getFacadeFactory().getPlanoContaFacade().consultarPorChavePrimaria(obj.getPlanoConta().getCodigo(), nivelMontarDados,usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe
     * <code>UnidadeEnsinoVO</code> relacionado ao objeto <code>DREVO</code>.
     * Faz uso da chave primária da classe <code>UnidadeEnsinoVO</code> para
     * realizar a consulta.
     *
     * @param obj
     *            Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosUnidadeEnsino(DREVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getUnidadeEnsino().getCodigo().intValue() == 0) {
            obj.setUnidadeEnsino(new UnidadeEnsinoVO());
            return;
        }
        obj.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsino().getCodigo(), false, nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>DREVO</code>
     * através de sua chave primária.
     *
     * @exception Exception
     *                Caso haja problemas de conexão ou localização do objeto
     *                procurado.
     */
    public DREVO consultarPorChavePrimaria(Integer codigoPrm, Integer unidadeEnsino, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false,usuario);
        String sql = "";
        SqlRowSet tabelaResultado = null;
        if (unidadeEnsino == null || unidadeEnsino == 0) {
            sql = "SELECT * FROM DRE WHERE codigo = ? AND DRE.unidadeEnsino is null";
            tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        } else {
            sql = "SELECT * FROM DRE WHERE codigo = ? AND DRE.unidadeEnsino is null";
            tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm, unidadeEnsino});
        }
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( DRE ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados,usuario));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe. Este
     * identificar é utilizado para verificar as permissões de acesso as
     * operações desta classe.
     */
    public static String getIdEntidade() {
        return DRE.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta
     * classe. Esta alteração deve ser possível, pois, uma mesma classe de
     * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
     * que Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        DRE.idEntidade = idEntidade;
    }

}
