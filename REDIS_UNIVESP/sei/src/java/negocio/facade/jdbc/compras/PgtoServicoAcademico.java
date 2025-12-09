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
import negocio.comuns.compras.PgtoServicoAcademicoVO;
import negocio.comuns.compras.SolicitacaoPgtoServicoAcademicoVO;
import negocio.comuns.financeiro.CategoriaDespesaVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.compras.PgtoServicoAcademicoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe
 * <code>PgtoServicoAcademicoVO</code>. Responsável por implementar operações como incluir, alterar, excluir e consultar
 * pertinentes a classe <code>PgtoServicoAcademicoVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see PgtoServicoAcademicoVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy 
public class PgtoServicoAcademico extends ControleAcesso implements PgtoServicoAcademicoInterfaceFacade {

    protected static String idEntidade;

    public PgtoServicoAcademico() throws Exception {
        super();
        setIdEntidade("PgtoServicoAcademico");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>PgtoServicoAcademicoVO</code>.
     */
    public PgtoServicoAcademicoVO novo() throws Exception {
        PgtoServicoAcademico.incluir(getIdEntidade());
        PgtoServicoAcademicoVO obj = new PgtoServicoAcademicoVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>PgtoServicoAcademicoVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a
     * permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>incluir</code> da
     * superclasse.
     *
     * @param obj
     *            Objeto da classe <code>PgtoServicoAcademicoVO</code> que será gravado no banco de dados.
     * @exception Exception
     *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @SuppressWarnings("static-access")
    public void incluir(final PgtoServicoAcademicoVO obj) throws Exception {
        try {
            PgtoServicoAcademicoVO.validarDados(obj);
            PgtoServicoAcademico.incluir(getIdEntidade());
            final String sql = "INSERT INTO PgtoServicoAcademico( data, formaPagamento, valorPagamento, nrDocumento, situacao, solicitacaoPgtoServicoAcademico, unidadeEnsino, tipoDestinatario, codigoDestinatario, bancoDestinatario, agenciaDestinatario, contaDestinatario, contaCorrente, centroDespesa ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo";
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    sqlInserir.setDate(1, Uteis.getDataJDBC(obj.getData()));
                    sqlInserir.setString(2, obj.getFormaPagamento());
                    sqlInserir.setDouble(3, obj.getValorPagamento().doubleValue());
                    sqlInserir.setString(4, obj.getNrDocumento());
                    sqlInserir.setString(5, obj.getSituacao());
                    if (obj.getSolicitacaoPgtoServicoAcademico().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(6, obj.getSolicitacaoPgtoServicoAcademico().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(6, 0);
                    }
                    if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(7, obj.getUnidadeEnsino().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(7, 0);
                    }
                    sqlInserir.setString(8, obj.getTipoDestinatario());
                    sqlInserir.setString(9, obj.getCodigoDestinatario());
                    sqlInserir.setString(10, obj.getBancoDestinatario());
                    sqlInserir.setString(11, obj.getAgenciaDestinatario());
                    sqlInserir.setString(12, obj.getContaDestinatario());
                    if (obj.getContaCorrente().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(13, obj.getContaCorrente().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(13, 0);
                    }
                    if (obj.getCentroDespesa().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(14, obj.getCentroDespesa().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(14, 0);
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
            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>PgtoServicoAcademicoVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a
     * permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>alterar</code> da
     * superclasse.
     *
     * @param obj
     *            Objeto da classe <code>PgtoServicoAcademicoVO</code> que será alterada no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @SuppressWarnings("static-access")
    public void alterar(final PgtoServicoAcademicoVO obj) throws Exception {
        try {
            PgtoServicoAcademicoVO.validarDados(obj);
            PgtoServicoAcademico.alterar(getIdEntidade());
            final String sql = "UPDATE PgtoServicoAcademico set data=?, formaPagamento=?, valorPagamento=?, nrDocumento=?, situacao=?, solicitacaoPgtoServicoAcademico=?, unidadeEnsino=?, tipoDestinatario=?, codigoDestinatario=?, bancoDestinatario=?, agenciaDestinatario=?, contaDestinatario=?, contaCorrente=?, centroDespesa=? WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setDate(1, Uteis.getDataJDBC(obj.getData()));
                    sqlAlterar.setString(2, obj.getFormaPagamento());
                    sqlAlterar.setDouble(3, obj.getValorPagamento().doubleValue());
                    sqlAlterar.setString(4, obj.getNrDocumento());
                    sqlAlterar.setString(5, obj.getSituacao());
                    if (obj.getSolicitacaoPgtoServicoAcademico().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(6, obj.getSolicitacaoPgtoServicoAcademico().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(6, 0);
                    }
                    if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(7, obj.getUnidadeEnsino().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(7, 0);
                    }
                    sqlAlterar.setString(8, obj.getTipoDestinatario());
                    sqlAlterar.setString(9, obj.getCodigoDestinatario());
                    sqlAlterar.setString(10, obj.getBancoDestinatario());
                    sqlAlterar.setString(11, obj.getAgenciaDestinatario());
                    sqlAlterar.setString(12, obj.getContaDestinatario());
                    if (obj.getContaCorrente().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(13, obj.getContaCorrente().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(13, 0);
                    }
                    if (obj.getCentroDespesa().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(14, obj.getCentroDespesa().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(14, 0);
                    }
                    sqlAlterar.setInt(15, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>PgtoServicoAcademicoVO</code>. Sempre localiza o
     * registro a ser excluído através da chave primária da entidade. Primeiramente verifica a conexão com o banco de
     * dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação
     * <code>excluir</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>PgtoServicoAcademicoVO</code> que será removido no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @SuppressWarnings("static-access")
    public void excluir(PgtoServicoAcademicoVO obj) throws Exception {
        try {
            PgtoServicoAcademico.excluir(getIdEntidade());
            String sql = "DELETE FROM PgtoServicoAcademico WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>PgtoServicoAcademico</code> através do valor do atributo
     * <code>numero</code> da classe <code>ContaCorrente</code> Faz uso da operação <code>montarDadosConsulta</code> que
     * realiza o trabalho de prerarar o List resultante.
     *
     * @return List Contendo vários objetos da classe <code>PgtoServicoAcademicoVO</code> resultantes da consulta.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    @SuppressWarnings("static-access")
    public List consultarPorNumeroContaCorrente(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT PgtoServicoAcademico.* FROM PgtoServicoAcademico, ContaCorrente WHERE PgtoServicoAcademico.contaCorrente = ContaCorrente.codigo and ContaCorrente.numero like('" + valorConsulta + "%') ORDER BY ContaCorrente.numero";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>PgtoServicoAcademico</code> através do valor do atributo
     * <code>String tipoDestinatario</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro
     * fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List
     * resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>PgtoServicoAcademicoVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    @SuppressWarnings("static-access")
    public List consultarPorTipoDestinatario(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM PgtoServicoAcademico WHERE tipoDestinatario like('" + valorConsulta + "%') ORDER BY tipoDestinatario";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>PgtoServicoAcademico</code> através do valor do atributo
     * <code>nome</code> da classe <code>UnidadeEnsino</code> Faz uso da operação <code>montarDadosConsulta</code> que
     * realiza o trabalho de prerarar o List resultante.
     *
     * @return List Contendo vários objetos da classe <code>PgtoServicoAcademicoVO</code> resultantes da consulta.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    @SuppressWarnings("static-access")
    public List consultarPorNomeUnidadeEnsino(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT PgtoServicoAcademico.* FROM PgtoServicoAcademico, UnidadeEnsino WHERE PgtoServicoAcademico.unidadeEnsino = UnidadeEnsino.codigo and UnidadeEnsino.nome like('" + valorConsulta + "%') ORDER BY UnidadeEnsino.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    /**
     * Respons?vel por realizar uma consulta de <code>PgtoServicoAcademico</code> atrav?s do valor do atributo
     * <code>identificadorCentroDespesa</code> da classe <code>CentroDespesa</code> Faz uso da opera??o
     * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     *
     * @return List Contendo v?rios objetos da classe <code>PgtoServicoAcademicoVO</code> resultantes da consulta.
     * @exception Execption
     *                Caso haja problemas de conex?o ou restri??o de acesso.
     */
    @SuppressWarnings("static-access")
    public List consultarPorIdentificadorCentroDespesaCentroDespesa(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT PgtoServicoAcademico.* FROM PgtoServicoAcademico, CentroDespesa WHERE PgtoServicoAcademico.centroDespesa = CentroDespesa.codigo and upper( CentroDespesa.identificadorCentroDespesa ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY CentroDespesa.identificadorCentroDespesa";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>PgtoServicoAcademico</code> através do valor do atributo
     * <code>descricao</code> da classe <code>SolicitacaoPgtoServicoAcademico</code> Faz uso da operação
     * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     *
     * @return List Contendo vários objetos da classe <code>PgtoServicoAcademicoVO</code> resultantes da consulta.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    @SuppressWarnings("static-access")
    public List consultarPorDescricaoSolicitacaoPgtoServicoAcademico(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT PgtoServicoAcademico.* FROM PgtoServicoAcademico, SolicitacaoPgtoServicoAcademico WHERE PgtoServicoAcademico.solicitacaoPgtoServicoAcademico = SolicitacaoPgtoServicoAcademico.codigo and SolicitacaoPgtoServicoAcademico.descricao like('" + valorConsulta + "%') ORDER BY SolicitacaoPgtoServicoAcademico.descricao";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>PgtoServicoAcademico</code> através do valor do atributo
     * <code>String situacao</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro
     * fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List
     * resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>PgtoServicoAcademicoVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    @SuppressWarnings("static-access")
    public List consultarPorSituacao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM PgtoServicoAcademico WHERE situacao like('" + valorConsulta + "%') ORDER BY situacao";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>PgtoServicoAcademico</code> através do valor do atributo
     * <code>Date data</code>. Retorna os objetos com valores pertecentes ao período informado por parâmetro. Faz uso da
     * operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>PgtoServicoAcademicoVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    @SuppressWarnings("static-access")
    public List consultarPorData(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM PgtoServicoAcademico WHERE ((data >= '" + Uteis.getDataJDBC(prmIni) + "') and (data <= '" + Uteis.getDataJDBC(prmFim) + "')) ORDER BY data";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>PgtoServicoAcademico</code> através do valor do atributo
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso
     * da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>PgtoServicoAcademicoVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    @SuppressWarnings("static-access")
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM PgtoServicoAcademico WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (
     * <code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por
     * vez.
     *
     * @return List Contendo vários objetos da classe <code>PgtoServicoAcademicoVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um
     * objeto da classe <code>PgtoServicoAcademicoVO</code>.
     *
     * @return O objeto da classe <code>PgtoServicoAcademicoVO</code> com os dados devidamente montados.
     */
    public static PgtoServicoAcademicoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        PgtoServicoAcademicoVO obj = new PgtoServicoAcademicoVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setData(dadosSQL.getDate("data"));
        obj.setFormaPagamento(dadosSQL.getString("formaPagamento"));
        obj.setValorPagamento(new Double(dadosSQL.getDouble("valorPagamento")));
        obj.setNrDocumento(dadosSQL.getString("nrDocumento"));
        obj.setSituacao(dadosSQL.getString("situacao"));
        obj.getSolicitacaoPgtoServicoAcademico().setCodigo(new Integer(dadosSQL.getInt("solicitacaoPgtoServicoAcademico")));
        obj.getUnidadeEnsino().setCodigo(new Integer(dadosSQL.getInt("unidadeEnsino")));
        obj.setTipoDestinatario(dadosSQL.getString("tipoDestinatario"));
        obj.setCodigoDestinatario(dadosSQL.getString("codigoDestinatario"));
        obj.setBancoDestinatario(dadosSQL.getString("bancoDestinatario"));
        obj.setAgenciaDestinatario(dadosSQL.getString("agenciaDestinatario"));
        obj.setContaDestinatario(dadosSQL.getString("contaDestinatario"));
        obj.getContaCorrente().setCodigo(new Integer(dadosSQL.getInt("contaCorrente")));
        obj.getCentroDespesa().setCodigo(new Integer(dadosSQL.getInt("centroDespesa")));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }

        montarDadosSolicitacaoPgtoServicoAcademico(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
        montarDadosUnidadeEnsino(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS,usuario);
        montarDadosContaCorrente(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
        montarDadosCentroDespesa(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>ContaCorrenteVO</code> relacionado ao
     * objeto <code>PgtoServicoAcademicoVO</code>. Faz uso da chave primária da classe <code>ContaCorrenteVO</code> para
     * realizar a consulta.
     *
     * @param obj
     *            Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosContaCorrente(PgtoServicoAcademicoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getContaCorrente().getCodigo().intValue() == 0) {
            obj.setContaCorrente(new ContaCorrenteVO());
            return;
        }
        obj.setContaCorrente(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(obj.getContaCorrente().getCodigo(), false, nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>UnidadeEnsinoVO</code> relacionado ao
     * objeto <code>PgtoServicoAcademicoVO</code>. Faz uso da chave primária da classe <code>UnidadeEnsinoVO</code> para
     * realizar a consulta.
     *
     * @param obj
     *            Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosUnidadeEnsino(PgtoServicoAcademicoVO obj, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if (obj.getUnidadeEnsino().getCodigo().intValue() == 0) {
            obj.setUnidadeEnsino(new UnidadeEnsinoVO());
            return;
        }
        obj.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsino().getCodigo(), false, nivelMontarDados,usuario));
    }

    /**
     * Opera??o respons?vel por montar os dados de um objeto da classe <code>CentroDespesaVO</code> relacionado ao
     * objeto <code>PgtoServicoAcademicoVO</code>. Faz uso da chave prim?ria da classe <code>CentroDespesaVO</code> para
     * realizar a consulta.
     *
     * @param obj
     *            Objeto no qual ser? montado os dados consultados.
     */
    public static void montarDadosCentroDespesa(PgtoServicoAcademicoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getCentroDespesa().getCodigo().intValue() == 0) {
            obj.setCentroDespesa(new CategoriaDespesaVO());
            return;
        }
        obj.setCentroDespesa(getFacadeFactory().getCategoriaDespesaFacade().consultarPorChavePrimaria(obj.getCentroDespesa().getCodigo(), false, nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>SolicitacaoPgtoServicoAcademicoVO</code>
     * relacionado ao objeto <code>PgtoServicoAcademicoVO</code>. Faz uso da chave primária da classe
     * <code>SolicitacaoPgtoServicoAcademicoVO</code> para realizar a consulta.
     *
     * @param obj
     *            Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosSolicitacaoPgtoServicoAcademico(PgtoServicoAcademicoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getSolicitacaoPgtoServicoAcademico().getCodigo().intValue() == 0) {
            obj.setSolicitacaoPgtoServicoAcademico(new SolicitacaoPgtoServicoAcademicoVO());
            return;
        }
        obj.setSolicitacaoPgtoServicoAcademico(getFacadeFactory().getSolicitacaoPgtoServicoAcademicoFacade().consultarPorChavePrimaria(obj.getSolicitacaoPgtoServicoAcademico().getCodigo(), false, nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>PgtoServicoAcademicoVO</code> através de sua chave
     * primária.
     *
     * @exception Exception
     *                Caso haja problemas de conexão ou localização do objeto procurado.
     */
    @SuppressWarnings("static-access")
    public PgtoServicoAcademicoVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sql = "SELECT * FROM PgtoServicoAcademico WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as
     * permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return PgtoServicoAcademico.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser
     * possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que
     * Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        PgtoServicoAcademico.idEntidade = idEntidade;
    }
}