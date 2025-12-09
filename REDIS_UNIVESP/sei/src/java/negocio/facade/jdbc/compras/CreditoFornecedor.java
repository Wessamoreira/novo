package negocio.facade.jdbc.compras;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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
import negocio.comuns.compras.CreditoFornecedorVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.compras.CreditoFornecedorInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>CreditoFornecedorVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>CreditoFornecedorVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see CreditoFornecedorVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy 
public class CreditoFornecedor extends ControleAcesso implements CreditoFornecedorInterfaceFacade {

    protected static String idEntidade;

    public CreditoFornecedor() throws Exception {
        super();
        setIdEntidade("CreditoFornecedor");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>CreditoFornecedorVO</code>.
     */
    public CreditoFornecedorVO novo() throws Exception {
        CreditoFornecedor.incluir(getIdEntidade());
        CreditoFornecedorVO obj = new CreditoFornecedorVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>CreditoFornecedorVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>CreditoFornecedorVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @SuppressWarnings("static-access")
    public void incluir(final CreditoFornecedorVO obj) throws Exception {
        try {
            CreditoFornecedorVO.validarDados(obj);
            //CreditoFornecedor.incluir(getIdEntidade());
            obj.realizarUpperCaseDados();
            final String sql = "INSERT INTO CreditoFornecedor( valor, valorUtilizado, saldo, data, responsavelCadastro, codigoOrigem, origem, fornecedor, unidadeEnsino, situacao, responsavelDevolucao, contaCorrente, valorDevolucao, dataDevolucao ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo";
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    sqlInserir.setDouble(1, obj.getValor().doubleValue());
                    sqlInserir.setDouble(2, obj.getValorUtilizado().doubleValue());
                    sqlInserir.setDouble(3, obj.getSaldo().doubleValue());
                    sqlInserir.setDate(4, Uteis.getDataJDBC(obj.getData()));
                    if (obj.getResponsavelCadastro().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(5, obj.getResponsavelCadastro().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(5, 0);
                    }
                    sqlInserir.setInt(6, obj.getCodigoOrigem().intValue());
                    sqlInserir.setString(7, obj.getOrigem());
                    if (obj.getFornecedor().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(8, obj.getFornecedor().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(8, 0);
                    }
                    if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(9, obj.getUnidadeEnsino().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(9, 0);
                    }
                    sqlInserir.setString(10, obj.getSituacao());
                    if (obj.getResponsavelDevolucao().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(11, obj.getResponsavelDevolucao().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(11, 0);
                    }
                    if (obj.getContaCorrente().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(12, obj.getContaCorrente().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(12, 0);
                    }
                    sqlInserir.setDouble(13, obj.getValorDevolucao().doubleValue());
                    sqlInserir.setDate(14, Uteis.getDataJDBC(obj.getDataDevolucao()));
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
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>CreditoFornecedorVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>CreditoFornecedorVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @SuppressWarnings("static-access")
    public void alterar(final CreditoFornecedorVO obj) throws Exception {
        try {
            CreditoFornecedorVO.validarDados(obj);
            CreditoFornecedor.alterar(getIdEntidade());
            obj.realizarUpperCaseDados();
            final String sql = "UPDATE CreditoFornecedor set valor=?, valorUtilizado=?, saldo=?, data=?, responsavelCadastro=?, codigoOrigem=?, origem=?, fornecedor=?, unidadeEnsino=?, situacao=?, responsavelDevolucao=?, contaCorrente=?, valorDevolucao=?, dataDevolucao=? WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setDouble(1, obj.getValor().doubleValue());
                    sqlAlterar.setDouble(2, obj.getValorUtilizado().doubleValue());
                    sqlAlterar.setDouble(3, obj.getSaldo().doubleValue());
                    sqlAlterar.setDate(4, Uteis.getDataJDBC(obj.getData()));
                    if (obj.getResponsavelCadastro().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(5, obj.getResponsavelCadastro().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(5, 0);
                    }
                    sqlAlterar.setInt(6, obj.getCodigoOrigem().intValue());
                    sqlAlterar.setString(7, obj.getOrigem());
                    if (obj.getFornecedor().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(8, obj.getFornecedor().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(8, 0);
                    }
                    if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(9, obj.getUnidadeEnsino().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(9, 0);
                    }
                    sqlAlterar.setString(10, obj.getSituacao());
                    if (obj.getResponsavelDevolucao().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(11, obj.getResponsavelDevolucao().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(11, 0);
                    }
                    if (obj.getContaCorrente().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(12, obj.getContaCorrente().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(12, 0);
                    }
                    sqlAlterar.setDouble(13, obj.getValorDevolucao().doubleValue());
                    sqlAlterar.setDate(14, Uteis.getDataJDBC(obj.getDataDevolucao()));
                    sqlAlterar.setInt(15, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>CreditoFornecedorVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>CreditoFornecedorVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @SuppressWarnings("static-access")
    public void excluir(CreditoFornecedorVO obj) throws Exception {
        try {
            CreditoFornecedor.excluir(getIdEntidade());
            String sql = "DELETE FROM CreditoFornecedor WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>CreditoFornecedor</code> através do valor do atributo 
     * <code>nome</code> da classe <code>Fornecedor</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>CreditoFornecedorVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @SuppressWarnings("static-access")
    public List consultarPorNomeFornecedor(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), true, usuario);
        String sqlStr = "SELECT CreditoFornecedor.* FROM CreditoFornecedor, Fornecedor WHERE CreditoFornecedor.fornecedor = Fornecedor.codigo and upper( Fornecedor.nome ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY Fornecedor.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>CreditoFornecedor</code> através do valor do atributo 
     * <code>Date data</code>. Retorna os objetos com valores pertecentes ao período informado por parâmetro.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>CreditoFornecedorVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    @SuppressWarnings("static-access")
    public List consultarPorData(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM CreditoFornecedor WHERE ((data >= '" + Uteis.getDataJDBC(prmIni) + "') and (data <= '" + Uteis.getDataJDBC(prmFim) + "')) ORDER BY data";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>CreditoFornecedor</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>CreditoFornecedorVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    @SuppressWarnings("static-access")
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM CreditoFornecedor WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>CreditoFornecedorVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        tabelaResultado = null;
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>CreditoFornecedorVO</code>.
     * @return  O objeto da classe <code>CreditoFornecedorVO</code> com os dados devidamente montados.
     */
    public static CreditoFornecedorVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        CreditoFornecedorVO obj = new CreditoFornecedorVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setValor(new Double(dadosSQL.getDouble("valor")));
        obj.setValorUtilizado(new Double(dadosSQL.getDouble("valorUtilizado")));
        obj.setSaldo(new Double(dadosSQL.getDouble("saldo")));
        obj.setData(dadosSQL.getDate("data"));
        obj.getResponsavelCadastro().setCodigo(new Integer(dadosSQL.getInt("responsavelCadastro")));
        obj.setCodigoOrigem(new Integer(dadosSQL.getInt("codigoOrigem")));
        obj.setOrigem(dadosSQL.getString("origem"));
        obj.getFornecedor().setCodigo(new Integer(dadosSQL.getInt("fornecedor")));
        obj.getUnidadeEnsino().setCodigo(new Integer(dadosSQL.getInt("unidadeEnsino")));
        obj.setSituacao(dadosSQL.getString("situacao"));
        obj.getResponsavelDevolucao().setCodigo(new Integer(dadosSQL.getInt("responsavelDevolucao")));
        obj.getContaCorrente().setCodigo(new Integer(dadosSQL.getInt("contaCorrente")));
        obj.setValorDevolucao(new Double(dadosSQL.getDouble("valorDevolucao")));
        obj.setDataDevolucao(dadosSQL.getDate("dataDevolucao"));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }

        montarDadosResponsavelCadastro(obj, nivelMontarDados, usuario);
        montarDadosFornecedor(obj, nivelMontarDados, usuario);
        montarDadosUnidadeEnsino(obj, nivelMontarDados,usuario);
        montarDadosResponsavelDevolucao(obj, nivelMontarDados, usuario);
        montarDadosContaCorrente(obj, nivelMontarDados, usuario);
        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>ContaCorrenteVO</code> relacionado ao objeto <code>CreditoFornecedorVO</code>.
     * Faz uso da chave primária da classe <code>ContaCorrenteVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosContaCorrente(CreditoFornecedorVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getContaCorrente().getCodigo().intValue() == 0) {
            obj.setContaCorrente(new ContaCorrenteVO());
            return;
        }
        obj.setContaCorrente(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(obj.getContaCorrente().getCodigo(), true, nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>UsuarioVO</code> relacionado ao objeto <code>CreditoFornecedorVO</code>.
     * Faz uso da chave primária da classe <code>UsuarioVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosResponsavelDevolucao(CreditoFornecedorVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getResponsavelDevolucao().getCodigo().intValue() == 0) {
            obj.setResponsavelDevolucao(new UsuarioVO());
            return;
        }
        obj.setResponsavelDevolucao(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavelDevolucao().getCodigo(), nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>UnidadeEnsinoVO</code> relacionado ao objeto <code>CreditoFornecedorVO</code>.
     * Faz uso da chave primária da classe <code>UnidadeEnsinoVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosUnidadeEnsino(CreditoFornecedorVO obj, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if (obj.getUnidadeEnsino().getCodigo().intValue() == 0) {
            obj.setUnidadeEnsino(new UnidadeEnsinoVO());
            return;
        }
        obj.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsino().getCodigo(), false, nivelMontarDados,usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>FornecedorVO</code> relacionado ao objeto <code>CreditoFornecedorVO</code>.
     * Faz uso da chave primária da classe <code>FornecedorVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosFornecedor(CreditoFornecedorVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getFornecedor().getCodigo().intValue() == 0) {
            obj.setFornecedor(new FornecedorVO());
            return;
        }
        obj.setFornecedor(getFacadeFactory().getFornecedorFacade().consultarPorChavePrimaria(obj.getFornecedor().getCodigo(), true, nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>UsuarioVO</code> relacionado ao objeto <code>CreditoFornecedorVO</code>.
     * Faz uso da chave primária da classe <code>UsuarioVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosResponsavelCadastro(CreditoFornecedorVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getResponsavelCadastro().getCodigo().intValue() == 0) {
            obj.setResponsavelCadastro(new UsuarioVO());
            return;
        }
        obj.setResponsavelCadastro(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavelCadastro().getCodigo(), nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>CreditoFornecedorVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    @SuppressWarnings("static-access")
    public CreditoFornecedorVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM CreditoFornecedor WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( CreditoFornecedor ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }


    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return CreditoFornecedor.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        CreditoFornecedor.idEntidade = idEntidade;
    }
}