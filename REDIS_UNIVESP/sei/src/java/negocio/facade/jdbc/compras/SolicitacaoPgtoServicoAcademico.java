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

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.compras.PrevisaoCustosVO;
import negocio.comuns.compras.SolicitacaoPgtoServicoAcademicoVO;
import negocio.comuns.financeiro.CategoriaDespesaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.compras.SolicitacaoPgtoServicoAcademicoInterfaceFacade;

/**
 * Classe de persist?ncia que encapsula todas as opera??es de manipula??o dos dados da classe
 * <code>SolicitacaoPgtoServicoAcademicoVO</code>. Respons?vel por implementar opera??es como incluir, alterar, excluir
 * e consultar pertinentes a classe <code>SolicitacaoPgtoServicoAcademicoVO</code>. Encapsula toda a intera??o com o
 * banco de dados.
 * 
 * @see SolicitacaoPgtoServicoAcademicoVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy 
public class SolicitacaoPgtoServicoAcademico extends ControleAcesso implements SolicitacaoPgtoServicoAcademicoInterfaceFacade {

    protected static String idEntidade;

    public SolicitacaoPgtoServicoAcademico() throws Exception {
        super();
        setIdEntidade("SolicitacaoPgtoServicoAcademico");
    }

    /**
     * Opera??o respons?vel por retornar um novo objeto da classe <code>SolicitacaoPgtoServicoAcademicoVO</code>.
     */
    public SolicitacaoPgtoServicoAcademicoVO novo() throws Exception {
        SolicitacaoPgtoServicoAcademico.incluir(getIdEntidade());
        SolicitacaoPgtoServicoAcademicoVO obj = new SolicitacaoPgtoServicoAcademicoVO();
        return obj;
    }

    /**
     * Opera??o respons?vel por incluir no banco de dados um objeto da classe
     * <code>SolicitacaoPgtoServicoAcademicoVO</code>. Primeiramente valida os dados (<code>validarDados</code>) do
     * objeto. Verifica a conex?o com o banco de dados e a permiss?o do usu?rio para realizar esta operac?o na entidade.
     * Isto, atrav?s da opera??o <code>incluir</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>SolicitacaoPgtoServicoAcademicoVO</code> que ser? gravado no banco de dados.
     * @exception Exception
     *                Caso haja problemas de conex?o, restri??o de acesso ou valida??o de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @SuppressWarnings("static-access")
    public void incluir(final SolicitacaoPgtoServicoAcademicoVO obj) throws Exception {
        try {
            SolicitacaoPgtoServicoAcademicoVO.validarDados(obj);
            SolicitacaoPgtoServicoAcademico.incluir(getIdEntidade());
            final String sql = "INSERT INTO SolicitacaoPgtoServicoAcademico( date, descricao, quantidadeHoras, valorHora, valorTotal, dataAutorizacao, responsavelAutorizacao, parecerResponsavel, situacao, previsaoCustosCurso, tipoDestinatarioPagamento, pessoaPgtoServico, centroDespesa ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo";
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    sqlInserir.setDate(1, Uteis.getDataJDBC(obj.getDate()));
                    sqlInserir.setString(2, obj.getDescricao());
                    sqlInserir.setInt(3, obj.getQuantidadeHoras().intValue());
                    sqlInserir.setDouble(4, obj.getValorHora().doubleValue());
                    sqlInserir.setDouble(5, obj.getValorTotal().doubleValue());
                    sqlInserir.setDate(6, Uteis.getDataJDBC(obj.getDataAutorizacao()));
                    if (obj.getResponsavelAutorizacao().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(7, obj.getResponsavelAutorizacao().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(7, 0);
                    }
                    sqlInserir.setString(8, obj.getParecerResponsavel());
                    sqlInserir.setString(9, obj.getSituacao());
                    if (obj.getPrevisaoCustosCurso().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(10, obj.getPrevisaoCustosCurso().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(10, 0);
                    }
                    sqlInserir.setString(11, obj.getTipoDestinatarioPagamento());
                    if (obj.getPessoaPgtoServico().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(12, obj.getPessoaPgtoServico().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(12, 0);
                    }
                    if (obj.getCentroDespesa().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(13, obj.getCentroDespesa().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(13, 0);
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
     * Opera??o respons?vel por alterar no BD os dados de um objeto da classe
     * <code>SolicitacaoPgtoServicoAcademicoVO</code>. Sempre utiliza a chave prim?ria da classe como atributo para
     * localiza??o do registro a ser alterado. Primeiramente valida os dados (<code>validarDados</code>) do objeto.
     * Verifica a conex?o com o banco de dados e a permiss?o do usu?rio para realizar esta operac?o na entidade. Isto,
     * atrav?s da opera??o <code>alterar</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>SolicitacaoPgtoServicoAcademicoVO</code> que ser? alterada no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conex?o, restri??o de acesso ou valida??o de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @SuppressWarnings("static-access")
    public void alterar(final SolicitacaoPgtoServicoAcademicoVO obj) throws Exception {
        try {
            SolicitacaoPgtoServicoAcademicoVO.validarDados(obj);
            SolicitacaoPgtoServicoAcademico.alterar(getIdEntidade());
            final String sql = "UPDATE SolicitacaoPgtoServicoAcademico set date=?, descricao=?, quantidadeHoras=?, valorHora=?, valorTotal=?, dataAutorizacao=?, responsavelAutorizacao=?, parecerResponsavel=?, situacao=?, previsaoCustosCurso=?, tipoDestinatarioPagamento=?, pessoaPgtoServico=?, centroDespesa=? WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setDate(1, Uteis.getDataJDBC(obj.getDate()));
                    sqlAlterar.setString(2, obj.getDescricao());
                    sqlAlterar.setInt(3, obj.getQuantidadeHoras().intValue());
                    sqlAlterar.setDouble(4, obj.getValorHora().doubleValue());
                    sqlAlterar.setDouble(5, obj.getValorTotal().doubleValue());
                    sqlAlterar.setDate(6, Uteis.getDataJDBC(obj.getDataAutorizacao()));
                    if (obj.getResponsavelAutorizacao().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(7, obj.getResponsavelAutorizacao().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(7, 0);
                    }
                    sqlAlterar.setString(8, obj.getParecerResponsavel());
                    sqlAlterar.setString(9, obj.getSituacao());
                    if (obj.getPrevisaoCustosCurso().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(10, obj.getPrevisaoCustosCurso().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(10, 0);
                    }
                    sqlAlterar.setString(11, obj.getTipoDestinatarioPagamento());
                    if (obj.getPessoaPgtoServico().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(12, obj.getPessoaPgtoServico().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(12, 0);
                    }
                    if (obj.getCentroDespesa().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(13, obj.getCentroDespesa().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(13, 0);
                    }
                    sqlAlterar.setInt(14, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Opera??o respons?vel por excluir no BD um objeto da classe <code>SolicitacaoPgtoServicoAcademicoVO</code>. Sempre
     * localiza o registro a ser exclu?do atrav?s da chave prim?ria da entidade. Primeiramente verifica a conex?o com o
     * banco de dados e a permiss?o do usu?rio para realizar esta operac?o na entidade. Isto, atrav?s da opera??o
     * <code>excluir</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>SolicitacaoPgtoServicoAcademicoVO</code> que ser? removido no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conex?o ou restri??o de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @SuppressWarnings("static-access")
    public void excluir(SolicitacaoPgtoServicoAcademicoVO obj) throws Exception {
        try {
            SolicitacaoPgtoServicoAcademico.excluir(getIdEntidade());
            String sql = "DELETE FROM SolicitacaoPgtoServicoAcademico WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Respons?vel por realizar uma consulta de <code>SolicitacaoPgtoServicoAcademico</code> atrav?s do valor do
     * atributo <code>identificadorCentroDespesa</code> da classe <code>CentroDespesa</code> Faz uso da opera??o
     * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     *
     * @return List Contendo v?rios objetos da classe <code>SolicitacaoPgtoServicoAcademicoVO</code> resultantes da
     *         consulta.
     * @exception Execption
     *                Caso haja problemas de conex?o ou restri??o de acesso.
     */
    @SuppressWarnings("static-access")
    public List consultarPorIdentificadorCentroDespesaCentroDespesa(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), true,usuario);
        String sqlStr = "SELECT SolicitacaoPgtoServicoAcademico.* FROM SolicitacaoPgtoServicoAcademico, CentroDespesa WHERE SolicitacaoPgtoServicoAcademico.centroDespesa = CentroDespesa.codigo and upper( CentroDespesa.identificadorCentroDespesa ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY CentroDespesa.identificadorCentroDespesa";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario);
    }

    /**
     * Respons?vel por realizar uma consulta de <code>SolicitacaoPgtoServicoAcademico</code> atrav?s do valor do
     * atributo <code>String tipoDestinatarioPagamento</code>. Retorna os objetos, com in?cio do valor do atributo
     * id?ntico ao par?metro fornecido. Faz uso da opera??o <code>montarDadosConsulta</code> que realiza o trabalho de
     * prerarar o List resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplica??o dever? verificar se o usu?rio possui permiss?o para esta consulta ou n?o.
     * @return List Contendo v?rios objetos da classe <code>SolicitacaoPgtoServicoAcademicoVO</code> resultantes da
     *         consulta.
     * @exception Exception
     *                Caso haja problemas de conex?o ou restri??o de acesso.
     */
    @SuppressWarnings("static-access")
    public List consultarPorTipoDestinatarioPagamento(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM SolicitacaoPgtoServicoAcademico WHERE upper( tipoDestinatarioPagamento ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY tipoDestinatarioPagamento";

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
    }

    /**
     * Respons?vel por realizar uma consulta de <code>SolicitacaoPgtoServicoAcademico</code> atrav?s do valor do
     * atributo <code>descricao</code> da classe <code>PrevisaoCustos</code> Faz uso da opera??o
     * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     *
     * @return List Contendo v?rios objetos da classe <code>SolicitacaoPgtoServicoAcademicoVO</code> resultantes da
     *         consulta.
     * @exception Execption
     *                Caso haja problemas de conex?o ou restri??o de acesso.
     */
    @SuppressWarnings("static-access")
    public List consultarPorDescricaoPrevisaoCustos(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), true,usuario);
        String sqlStr = "SELECT SolicitacaoPgtoServicoAcademico.* FROM SolicitacaoPgtoServicoAcademico, PrevisaoCustos WHERE SolicitacaoPgtoServicoAcademico.previsaoCustosCurso = PrevisaoCustos.codigo and upper( PrevisaoCustos.descricao ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY PrevisaoCustos.descricao";

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario);
    }

    /**
     * Respons?vel por realizar uma consulta de <code>SolicitacaoPgtoServicoAcademico</code> atrav?s do valor do
     * atributo <code>String situacao</code>. Retorna os objetos, com in?cio do valor do atributo id?ntico ao par?metro
     * fornecido. Faz uso da opera??o <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List
     * resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplica??o dever? verificar se o usu?rio possui permiss?o para esta consulta ou n?o.
     * @return List Contendo v?rios objetos da classe <code>SolicitacaoPgtoServicoAcademicoVO</code> resultantes da
     *         consulta.
     * @exception Exception
     *                Caso haja problemas de conex?o ou restri??o de acesso.
     */
    @SuppressWarnings("static-access")
    public List consultarPorSituacao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM SolicitacaoPgtoServicoAcademico WHERE upper( situacao ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY situacao";

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
    }

    /**
     * Respons?vel por realizar uma consulta de <code>SolicitacaoPgtoServicoAcademico</code> atrav?s do valor do
     * atributo <code>String parecerResponsavel</code>. Retorna os objetos, com in?cio do valor do atributo id?ntico ao
     * par?metro fornecido. Faz uso da opera??o <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
     * List resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplica??o dever? verificar se o usu?rio possui permiss?o para esta consulta ou n?o.
     * @return List Contendo v?rios objetos da classe <code>SolicitacaoPgtoServicoAcademicoVO</code> resultantes da
     *         consulta.
     * @exception Exception
     *                Caso haja problemas de conex?o ou restri??o de acesso.
     */
    @SuppressWarnings("static-access")
    public List consultarPorParecerResponsavel(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM SolicitacaoPgtoServicoAcademico WHERE upper( parecerResponsavel ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY parecerResponsavel";

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
    }

    /**
     * Respons?vel por realizar uma consulta de <code>SolicitacaoPgtoServicoAcademico</code> atrav?s do valor do
     * atributo <code>nome</code> da classe <code>Pessoa</code> Faz uso da opera??o <code>montarDadosConsulta</code> que
     * realiza o trabalho de prerarar o List resultante.
     *
     * @return List Contendo v?rios objetos da classe <code>SolicitacaoPgtoServicoAcademicoVO</code> resultantes da
     *         consulta.
     * @exception Execption
     *                Caso haja problemas de conex?o ou restri??o de acesso.
     */
    @SuppressWarnings("static-access")
    public List consultarPorNomePessoa(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), true,usuario);
        String sqlStr = "SELECT SolicitacaoPgtoServicoAcademico.* FROM SolicitacaoPgtoServicoAcademico, Pessoa WHERE SolicitacaoPgtoServicoAcademico.responsavelAutorizacao = Pessoa.codigo and upper( Pessoa.nome ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY Pessoa.nome";

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario);
    }

    /**
     * Respons?vel por realizar uma consulta de <code>SolicitacaoPgtoServicoAcademico</code> atrav?s do valor do
     * atributo <code>Date dataAutorizacao</code>. Retorna os objetos com valores pertecentes ao per?odo informado por
     * par?metro. Faz uso da opera??o <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List
     * resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplica??o dever? verificar se o usu?rio possui permiss?o para esta consulta ou n?o.
     * @return List Contendo v?rios objetos da classe <code>SolicitacaoPgtoServicoAcademicoVO</code> resultantes da
     *         consulta.
     * @exception Exception
     *                Caso haja problemas de conex?o ou restri??o de acesso.
     */
    @SuppressWarnings("static-access")
    public List consultarPorDataAutorizacao(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM SolicitacaoPgtoServicoAcademico WHERE ((dataAutorizacao >= '" + Uteis.getDataJDBC(prmIni) + "') and (dataAutorizacao <= '" + Uteis.getDataJDBC(prmFim) + "')) ORDER BY dataAutorizacao";

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
    }

    /**
     * Respons?vel por realizar uma consulta de <code>SolicitacaoPgtoServicoAcademico</code> atrav?s do valor do
     * atributo <code>Double valorTotal</code>. Retorna os objetos com valores iguais ou superiores ao par?metro
     * fornecido. Faz uso da opera??o <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List
     * resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplica??o dever? verificar se o usu?rio possui permiss?o para esta consulta ou n?o.
     * @return List Contendo v?rios objetos da classe <code>SolicitacaoPgtoServicoAcademicoVO</code> resultantes da
     *         consulta.
     * @exception Exception
     *                Caso haja problemas de conex?o ou restri??o de acesso.
     */
    @SuppressWarnings("static-access")
    public List consultarPorValorTotal(Double valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM SolicitacaoPgtoServicoAcademico WHERE valorTotal >= " + valorConsulta.doubleValue() + " ORDER BY valorTotal";

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
    }

    /**
     * Respons?vel por realizar uma consulta de <code>SolicitacaoPgtoServicoAcademico</code> atrav?s do valor do
     * atributo <code>Double valorHora</code>. Retorna os objetos com valores iguais ou superiores ao par?metro
     * fornecido. Faz uso da opera??o <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List
     * resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplica??o dever? verificar se o usu?rio possui permiss?o para esta consulta ou n?o.
     * @return List Contendo v?rios objetos da classe <code>SolicitacaoPgtoServicoAcademicoVO</code> resultantes da
     *         consulta.
     * @exception Exception
     *                Caso haja problemas de conex?o ou restri??o de acesso.
     */
    @SuppressWarnings("static-access")
    public List consultarPorValorHora(Double valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM SolicitacaoPgtoServicoAcademico WHERE valorHora >= " + valorConsulta.doubleValue() + " ORDER BY valorHora";

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
    }

    /**
     * Respons?vel por realizar uma consulta de <code>SolicitacaoPgtoServicoAcademico</code> atrav?s do valor do
     * atributo <code>Integer quantidadeHoras</code>. Retorna os objetos com valores iguais ou superiores ao par?metro
     * fornecido. Faz uso da opera??o <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List
     * resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplica??o dever? verificar se o usu?rio possui permiss?o para esta consulta ou n?o.
     * @return List Contendo v?rios objetos da classe <code>SolicitacaoPgtoServicoAcademicoVO</code> resultantes da
     *         consulta.
     * @exception Exception
     *                Caso haja problemas de conex?o ou restri??o de acesso.
     */
    @SuppressWarnings("static-access")
    public List consultarPorQuantidadeHoras(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM SolicitacaoPgtoServicoAcademico WHERE quantidadeHoras >= " + valorConsulta.intValue() + " ORDER BY quantidadeHoras";

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
    }

    /**
     * Respons?vel por realizar uma consulta de <code>SolicitacaoPgtoServicoAcademico</code> atrav?s do valor do
     * atributo <code>String descricao</code>. Retorna os objetos, com in?cio do valor do atributo id?ntico ao par?metro
     * fornecido. Faz uso da opera??o <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List
     * resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplica??o dever? verificar se o usu?rio possui permiss?o para esta consulta ou n?o.
     * @return List Contendo v?rios objetos da classe <code>SolicitacaoPgtoServicoAcademicoVO</code> resultantes da
     *         consulta.
     * @exception Exception
     *                Caso haja problemas de conex?o ou restri??o de acesso.
     */
    @SuppressWarnings("static-access")
    public List consultarPorDescricao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM SolicitacaoPgtoServicoAcademico WHERE upper( descricao ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY descricao";

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
    }

    /**
     * Respons?vel por realizar uma consulta de <code>SolicitacaoPgtoServicoAcademico</code> atrav?s do valor do
     * atributo <code>Date date</code>. Retorna os objetos com valores pertecentes ao per?odo informado por par?metro.
     * Faz uso da opera??o <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplica??o dever? verificar se o usu?rio possui permiss?o para esta consulta ou n?o.
     * @return List Contendo v?rios objetos da classe <code>SolicitacaoPgtoServicoAcademicoVO</code> resultantes da
     *         consulta.
     * @exception Exception
     *                Caso haja problemas de conex?o ou restri??o de acesso.
     */
    @SuppressWarnings("static-access")
    public List consultarPorDate(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM SolicitacaoPgtoServicoAcademico WHERE ((date >= '" + Uteis.getDataJDBC(prmIni) + "') and (date <= '" + Uteis.getDataJDBC(prmFim) + "')) ORDER BY date";

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
    }

    /**
     * Respons?vel por realizar uma consulta de <code>SolicitacaoPgtoServicoAcademico</code> atrav?s do valor do
     * atributo <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao par?metro fornecido.
     * Faz uso da opera??o <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplica??o dever? verificar se o usu?rio possui permiss?o para esta consulta ou n?o.
     * @return List Contendo v?rios objetos da classe <code>SolicitacaoPgtoServicoAcademicoVO</code> resultantes da
     *         consulta.
     * @exception Exception
     *                Caso haja problemas de conex?o ou restri??o de acesso.
     */
    @SuppressWarnings("static-access")
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM SolicitacaoPgtoServicoAcademico WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
    }

    /**
     * Respons?vel por montar os dados de v?rios objetos, resultantes de uma consulta ao banco de dados (
     * <code>ResultSet</code>). Faz uso da opera??o <code>montarDados</code> que realiza o trabalho para um objeto por
     * vez.
     *
     * @return List Contendo v?rios objetos da classe <code>SolicitacaoPgtoServicoAcademicoVO</code> resultantes da
     *         consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados,usuario));
        }
        return vetResultado;
    }

    /**
     * Respons?vel por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um
     * objeto da classe <code>SolicitacaoPgtoServicoAcademicoVO</code>.
     *
     * @return O objeto da classe <code>SolicitacaoPgtoServicoAcademicoVO</code> com os dados devidamente montados.
     */
    public static SolicitacaoPgtoServicoAcademicoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        SolicitacaoPgtoServicoAcademicoVO obj = new SolicitacaoPgtoServicoAcademicoVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setDate(dadosSQL.getDate("date"));
        obj.setDescricao(dadosSQL.getString("descricao"));
        obj.setQuantidadeHoras(new Integer(dadosSQL.getInt("quantidadeHoras")));
        obj.setValorHora(new Double(dadosSQL.getDouble("valorHora")));
        obj.setValorTotal(new Double(dadosSQL.getDouble("valorTotal")));
        obj.setDataAutorizacao(dadosSQL.getDate("dataAutorizacao"));
        obj.getResponsavelAutorizacao().setCodigo(new Integer(dadosSQL.getInt("responsavelAutorizacao")));
        obj.setParecerResponsavel(dadosSQL.getString("parecerResponsavel"));
        obj.setSituacao(dadosSQL.getString("situacao"));
        obj.getPrevisaoCustosCurso().setCodigo(new Integer(dadosSQL.getInt("previsaoCustosCurso")));
        obj.setTipoDestinatarioPagamento(dadosSQL.getString("tipoDestinatarioPagamento"));
        obj.getPessoaPgtoServico().setCodigo(new Integer(dadosSQL.getInt("pessoaPgtoServico")));
        obj.getCentroDespesa().setCodigo(new Integer(dadosSQL.getInt("centroDespesa")));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }

        montarDadosResponsavelAutorizacao(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,usuario);
        montarDadosPrevisaoCustosCurso(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS,usuario);
        montarDadosPessoaPgtoServico(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS,usuario);
        montarDadosCentroDespesa(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS,usuario);
        return obj;
    }

    /**
     * Opera??o respons?vel por montar os dados de um objeto da classe <code>CentroDespesaVO</code> relacionado ao
     * objeto <code>SolicitacaoPgtoServicoAcademicoVO</code>. Faz uso da chave prim?ria da classe
     * <code>CentroDespesaVO</code> para realizar a consulta.
     *
     * @param obj
     *            Objeto no qual ser? montado os dados consultados.
     */
    public static void montarDadosCentroDespesa(SolicitacaoPgtoServicoAcademicoVO obj, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if (obj.getCentroDespesa().getCodigo().intValue() == 0) {
            obj.setCentroDespesa(new CategoriaDespesaVO());
            return;
        }
        obj.setCentroDespesa(getFacadeFactory().getCategoriaDespesaFacade().consultarPorChavePrimaria(obj.getCentroDespesa().getCodigo(), false, nivelMontarDados,usuario));
    }

    /**
     * Opera??o respons?vel por montar os dados de um objeto da classe <code>PessoaVO</code> relacionado ao objeto
     * <code>SolicitacaoPgtoServicoAcademicoVO</code>. Faz uso da chave prim?ria da classe <code>PessoaVO</code> para
     * realizar a consulta.
     *
     * @param obj
     *            Objeto no qual ser? montado os dados consultados.
     */
    public static void montarDadosPessoaPgtoServico(SolicitacaoPgtoServicoAcademicoVO obj, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if (obj.getPessoaPgtoServico().getCodigo().intValue() == 0) {
            obj.setPessoaPgtoServico(new PessoaVO());
            return;
        }
        obj.setPessoaPgtoServico(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(obj.getPessoaPgtoServico().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,usuario));
    }

    /**
     * Opera??o respons?vel por montar os dados de um objeto da classe <code>PrevisaoCustosVO</code> relacionado ao
     * objeto <code>SolicitacaoPgtoServicoAcademicoVO</code>. Faz uso da chave prim?ria da classe
     * <code>PrevisaoCustosVO</code> para realizar a consulta.
     *
     * @param obj
     *            Objeto no qual ser? montado os dados consultados.
     */
    public static void montarDadosPrevisaoCustosCurso(SolicitacaoPgtoServicoAcademicoVO obj, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if (obj.getPrevisaoCustosCurso().getCodigo().intValue() == 0) {
            obj.setPrevisaoCustosCurso(new PrevisaoCustosVO());
            return;
        }
        obj.setPrevisaoCustosCurso(getFacadeFactory().getPrevisaoCustosFacade().consultarPorChavePrimaria(obj.getPrevisaoCustosCurso().getCodigo(), false, nivelMontarDados,usuario));
    }

    /**
     * Opera??o respons?vel por montar os dados de um objeto da classe <code>PessoaVO</code> relacionado ao objeto
     * <code>SolicitacaoPgtoServicoAcademicoVO</code>. Faz uso da chave prim?ria da classe <code>PessoaVO</code> para
     * realizar a consulta.
     *
     * @param obj
     *            Objeto no qual ser? montado os dados consultados.
     */
    public static void montarDadosResponsavelAutorizacao(SolicitacaoPgtoServicoAcademicoVO obj, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if (obj.getResponsavelAutorizacao().getCodigo().intValue() == 0) {
            obj.setResponsavelAutorizacao(new PessoaVO());
            return;
        }
        obj.setResponsavelAutorizacao(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(obj.getResponsavelAutorizacao().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,usuario));
    }

    /**
     * Opera??o respons?vel por localizar um objeto da classe <code>SolicitacaoPgtoServicoAcademicoVO</code> atrav?s de
     * sua chave prim?ria.
     *
     * @exception Exception
     *                Caso haja problemas de conex?o ou localiza??o do objeto procurado.
     */
    @SuppressWarnings("static-access")
    public SolicitacaoPgtoServicoAcademicoVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false,usuario);
        String sql = "SELECT * FROM SolicitacaoPgtoServicoAcademico WHERE codigo = ?";

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados N?o Encontrados ( SolicitacaoPgtoServicoAcademico ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados,usuario));
    }

    /**
     * Opera??o repons?vel por retornar o identificador desta classe. Este identificar ? utilizado para verificar as
     * permiss?es de acesso as opera??es desta classe.
     */
    public static String getIdEntidade() {
        return SolicitacaoPgtoServicoAcademico.idEntidade;
    }

    /**
     * Opera??o repons?vel por definir um novo valor para o identificador desta classe. Esta altera??o deve ser
     * poss?vel, pois, uma mesma classe de neg?cio pode ser utilizada com objetivos distintos. Assim ao se verificar que
     * Como o controle de acesso ? realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        SolicitacaoPgtoServicoAcademico.idEntidade = idEntidade;
    }
}