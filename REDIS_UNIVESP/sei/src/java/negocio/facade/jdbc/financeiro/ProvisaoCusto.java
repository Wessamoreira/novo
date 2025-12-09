package negocio.facade.jdbc.financeiro;

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

import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.MapaLancamentoFuturoVO;
import negocio.comuns.financeiro.ProvisaoCustoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoProvisaoCusto;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.ProvisaoCustoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>ProvisaoCustoVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>ProvisaoCustoVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see ProvisaoCustoVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy 
public class ProvisaoCusto extends ControleAcesso implements ProvisaoCustoInterfaceFacade {

    protected static String idEntidade;

    public ProvisaoCusto() throws Exception {
        super();
        setIdEntidade("ProvisaoCusto");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>ProvisaoCustoVO</code>.
     */
    public ProvisaoCustoVO novo() throws Exception {
        ProvisaoCusto.incluir(getIdEntidade());
        ProvisaoCustoVO obj = new ProvisaoCustoVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>ProvisaoCustoVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>ProvisaoCustoVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final ProvisaoCustoVO obj, UsuarioVO usuario, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception {
        try {
            ProvisaoCusto.incluir(getIdEntidade(), true, usuario);
            ProvisaoCustoVO.validarDados(obj);
            obj.gerarMapaLancamentoFuturo();
            getFacadeFactory().getMapaLancamentoFuturoFacade().incluir(obj.getMapaLancamentoFuturo(),usuario);
            obj.setSituacao(SituacaoProvisaoCusto.FINALIZADO.getValor());
            final String sql = "INSERT INTO ProvisaoCusto( data, responsavel, requisitante, valor, descricao, dataPrestacaoConta, contaCorrente, valorTroco, contaCorrenteTroco, situacao, mapalancamentofuturo, funcionariocargocentrocusto, valorfinal ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    sqlInserir.setDate(1, Uteis.getDataJDBC(obj.getData()));
                    if (obj.getResponsavel().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(2, obj.getResponsavel().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(2, 0);
                    }
                    if (obj.getRequisitante().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(3, obj.getRequisitante().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(3, 0);
                    }
                    sqlInserir.setDouble(4, obj.getValor().doubleValue());
                    sqlInserir.setString(5, obj.getDescricao());
                    sqlInserir.setDate(6, Uteis.getDataJDBC(obj.getDataPrestacaoConta()));
                    if (obj.getContaCorrente().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(7, obj.getContaCorrente().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(7, 0);
                    }
                    sqlInserir.setDouble(8, obj.getValorTroco().doubleValue());
                    if (obj.getContaCorrenteTroco().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(9, obj.getContaCorrenteTroco().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(9, 0);
                    }
                    sqlInserir.setString(10, obj.getSituacao());
                    sqlInserir.setInt(11, obj.getMapaLancamentoFuturo().getCodigo());
                    if (obj.getFuncionarioCargoCentroCusto().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(12, obj.getFuncionarioCargoCentroCusto().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(12, 0);
                    }
                    sqlInserir.setDouble(13, obj.getValorFinal());
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

            getFacadeFactory().getItensProvisaoFacade().incluirItensProvisaos(obj.getCodigo(), obj.getItensProvisaoVOs(),usuario);
            getFacadeFactory().getFluxoCaixaFacade().criarMovimentacaoCaixa(obj.getValor(), obj.getContaCorrente().getCodigo(), "SA", "PC", getFormaPagamentoPadraoProvisaoCusto(usuario, configuracaoFinanceiroVO), obj.getCodigo(), obj.getResponsavel().getCodigo(), obj.getRequisitante().getPessoa().getCodigo(), 0, 0, 0, null, 0, usuario);
            obj.setNovoObj(Boolean.FALSE);
        } catch (Exception e) {
            obj.setNovoObj(true);
            throw e;
        }
    }

    private Integer getFormaPagamentoPadraoProvisaoCusto(UsuarioVO usuario, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception {
//        return getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_TODOS, usuario, null).getFormaPagamentoPadraoProvisaoCusto().getCodigo();
        return configuracaoFinanceiroVO.getFormaPagamentoPadraoProvisaoCusto().getCodigo();
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>ProvisaoCustoVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>ProvisaoCustoVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final ProvisaoCustoVO obj, UsuarioVO usuario, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception {
        try {
            ProvisaoCusto.alterar(getIdEntidade(), true, usuario);
            ProvisaoCustoVO.validarDados(obj);
            final String sql = "UPDATE ProvisaoCusto set data=?, responsavel=?, requisitante=?, valor=?, descricao=?, dataPrestacaoConta=?, contaCorrente=?, valorTroco=?, contaCorrenteTroco=?, situacao=?, mapalancamentofuturo=? , funcionariocargocentrocusto=?, valorfinal=? WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setDate(1, Uteis.getDataJDBC(obj.getData()));
                    if (obj.getResponsavel().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(2, obj.getResponsavel().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(2, 0);
                    }
                    if (obj.getRequisitante().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(3, obj.getRequisitante().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(3, 0);
                    }
                    sqlAlterar.setDouble(4, obj.getValor().doubleValue());
                    sqlAlterar.setString(5, obj.getDescricao());
                    sqlAlterar.setDate(6, Uteis.getDataJDBC(obj.getDataPrestacaoConta()));
                    if (obj.getContaCorrente().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(7, obj.getContaCorrente().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(7, 0);
                    }
                    sqlAlterar.setDouble(8, obj.getValorTroco().doubleValue());
                    if (obj.getContaCorrenteTroco().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(9, obj.getContaCorrenteTroco().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(9, 0);
                    }
                    sqlAlterar.setString(10, obj.getSituacao());
                    if (obj.isSituacaoFinalizado()) {
                        sqlAlterar.setNull(11, 0);
                    } else {
                        if (obj.getMapaLancamentoFuturo().getCodigo() != 0) {
                            sqlAlterar.setInt(11, obj.getMapaLancamentoFuturo().getCodigo());
                        } else {
                            sqlAlterar.setNull(11, 0);
                        }
                    }
                    sqlAlterar.setInt(12, obj.getFuncionarioCargoCentroCusto().getCodigo().intValue());
                    sqlAlterar.setDouble(13, obj.getValorFinal());
                    sqlAlterar.setInt(14, obj.getCodigo().intValue());

                    return sqlAlterar;
                }
            });

            getFacadeFactory().getItensProvisaoFacade().alterarItensProvisaos(obj.getCodigo(), obj.getItensProvisaoVOs(),usuario);
            if (obj.isSituacaoFinalizado()) {
                getFacadeFactory().getMapaLancamentoFuturoFacade().excluir(obj.getMapaLancamentoFuturo(),usuario);
                if (obj.isProvisaoPrecisaGuardarTroco()) {
                    getFacadeFactory().getFluxoCaixaFacade().criarMovimentacaoCaixa(obj.getValorTroco(), obj.getContaCorrenteTroco().getCodigo(), "EN", "PC", getFormaPagamentoPadraoProvisaoCusto(usuario, configuracaoFinanceiroVO), obj.getCodigo(), obj.getResponsavel().getCodigo(), obj.getRequisitante().getPessoa().getCodigo(), 0, 0, 0, null, 0, usuario);
                }
                if (obj.isProvisaoValorFinalMaiorQueInicial()) {
                    getFacadeFactory().getFluxoCaixaFacade().criarMovimentacaoCaixa((obj.getValorFinal() - obj.getValor()), obj.getContaCorrenteTroco().getCodigo(), "SA", "PC", getFormaPagamentoPadraoProvisaoCusto(usuario, configuracaoFinanceiroVO), obj.getCodigo(), obj.getResponsavel().getCodigo(), obj.getRequisitante().getPessoa().getCodigo(), 0, 0, 0, null, 0, usuario);
                }
            }
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>ProvisaoCustoVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>ProvisaoCustoVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(ProvisaoCustoVO obj, UsuarioVO usuarioVO) throws Exception {
        try {
            ProvisaoCusto.excluir(getIdEntidade(), true, usuarioVO);
            String sql = "DELETE FROM ProvisaoCusto WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>ProvisaoCusto</code> através do valor do atributo 
     * <code>String situacao</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>ProvisaoCustoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorSituacao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados , UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM ProvisaoCusto WHERE upper( situacao ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY situacao";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>ProvisaoCusto</code> através do valor do atributo 
     * <code>Date dataPrestacaoConta</code>. Retorna os objetos com valores pertecentes ao período informado por parâmetro.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>ProvisaoCustoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorDataPrestacaoConta(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM ProvisaoCusto WHERE ((dataPrestacaoConta >= '" + Uteis.getDataJDBC(prmIni) + "') and (dataPrestacaoConta <= '" + Uteis.getDataJDBC(prmFim) + "')) ORDER BY dataPrestacaoConta";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>ProvisaoCusto</code> através do valor do atributo 
     * <code>codigo</code> da classe <code>Funcionario</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>ProvisaoCustoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigoFuncionario(Integer valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), true, usuario);
        String sqlStr = "SELECT ProvisaoCusto.* FROM ProvisaoCusto, Funcionario WHERE ProvisaoCusto.requisitante = Funcionario.codigo and Funcionario.codigo >= " + valorConsulta.intValue() + " ORDER BY Funcionario.codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List consultarPorNomeFuncionario(String valorConsulta, String situacao, Date dataInicio, Date dataFim, int nivelMontarDados , UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), true, usuario);
        String sqlStr = "SELECT ProvisaoCusto.* FROM ProvisaoCusto "
                + "inner join Funcionario on funcionario.codigo = provisaocusto.requisitante "
                + "inner join pessoa on pessoa.codigo = funcionario.pessoa "
                + "WHERE upper(pessoa.nome) like('" + valorConsulta.toUpperCase() + "%') "
                + "and dataprestacaoconta between '" + Uteis.getDataJDBC(dataInicio) + "' and '" + Uteis.getDataJDBC(dataFim) + "' ";
        if (Uteis.isAtributoPreenchido(situacao)) {
            sqlStr += "and upper(situacao) like('" + situacao.toUpperCase() + "%')";
        }
        sqlStr += "ORDER BY Funcionario.codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>ProvisaoCusto</code> através do valor do atributo 
     * <code>nome</code> da classe <code>Usuario</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>ProvisaoCustoVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNomeUsuario(String valorConsulta, String situacao, Date dataInicio, Date dataFim, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), true, usuario);
        String sqlStr = "SELECT ProvisaoCusto.* FROM ProvisaoCusto, Usuario "
                + "WHERE ProvisaoCusto.responsavel = Usuario.codigo "
                + "and upper( Usuario.nome ) like('" + valorConsulta.toUpperCase() + "%') "
                + "and dataprestacaoconta between '" + Uteis.getDataJDBC(dataInicio) + "' and '" + Uteis.getDataJDBC(dataFim) + "' ";
        if (Uteis.isAtributoPreenchido(situacao)) {
            sqlStr += "and upper(situacao) like('" + situacao.toUpperCase() + "%')";
        }
        sqlStr += "ORDER BY Usuario.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>ProvisaoCusto</code> através do valor do atributo 
     * <code>Date data</code>. Retorna os objetos com valores pertecentes ao período informado por parâmetro.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>ProvisaoCustoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorData(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM ProvisaoCusto WHERE ((data >= '" + Uteis.getDataJDBC(prmIni) + "') and (data <= '" + Uteis.getDataJDBC(prmFim) + "')) ORDER BY data";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>ProvisaoCusto</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>ProvisaoCustoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, String situacao, Date dataInicio, Date dataFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso,usuario);
        String sqlStr = "SELECT * FROM ProvisaoCusto "
                + "WHERE codigo >= " + valorConsulta.intValue();
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>ProvisaoCustoVO</code> resultantes da consulta.
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
     * em um objeto da classe <code>ProvisaoCustoVO</code>.
     * @return  O objeto da classe <code>ProvisaoCustoVO</code> com os dados devidamente montados.
     */
    public static ProvisaoCustoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ProvisaoCustoVO obj = new ProvisaoCustoVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setData(dadosSQL.getDate("data"));
        obj.getResponsavel().setCodigo(new Integer(dadosSQL.getInt("responsavel")));
        obj.getRequisitante().setCodigo(new Integer(dadosSQL.getInt("requisitante")));
        obj.setValor(new Double(dadosSQL.getDouble("valor")));
        obj.setDescricao(dadosSQL.getString("descricao"));
        obj.setDataPrestacaoConta(dadosSQL.getDate("dataPrestacaoConta"));
        obj.getContaCorrente().setCodigo(new Integer(dadosSQL.getInt("contaCorrente")));
        obj.setValorTroco(new Double(dadosSQL.getDouble("valorTroco")));
        obj.getContaCorrenteTroco().setCodigo(new Integer(dadosSQL.getInt("contaCorrenteTroco")));
        obj.setSituacao(dadosSQL.getString("situacao"));
        obj.setNovoObj(Boolean.FALSE);
        obj.getMapaLancamentoFuturo().setCodigo(dadosSQL.getInt("mapalancamentofuturo"));
        obj.getFuncionarioCargoCentroCusto().setCodigo(dadosSQL.getInt("funcionariocargocentrocusto"));
        obj.setValorFinal(dadosSQL.getDouble("valorfinal"));
        montarDadosResponsavel(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,usuario);
        montarDadosRequisitante(obj, nivelMontarDados, usuario);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
            return obj;
        }
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        obj.setItensProvisaoVOs(ItensProvisao.consultarItensProvisaos(obj.getCodigo(), nivelMontarDados));
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS) {
            return obj;
        }
        montarDadosFuncionarioCargoCentroCusto(obj, nivelMontarDados, usuario);
        montarDadosMapaLancamentoFuturo(obj, nivelMontarDados, usuario);
        montarDadosContaCorrente(obj, nivelMontarDados, usuario);
        montarDadosContaCorrenteTroco(obj, nivelMontarDados, usuario);
        return obj;
    }

    public static void montarDadosMapaLancamentoFuturo(ProvisaoCustoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getMapaLancamentoFuturo().getCodigo() == 0) {
            obj.setMapaLancamentoFuturo(new MapaLancamentoFuturoVO());
            return;
        }
        obj.setMapaLancamentoFuturo(getFacadeFactory().getMapaLancamentoFuturoFacade().consultarPorChavePrimaria(obj.getMapaLancamentoFuturo().getCodigo(), false, nivelMontarDados,usuario));
    }

    public static void montarDadosFuncionarioCargoCentroCusto(ProvisaoCustoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getFuncionarioCargoCentroCusto().getCodigo() == 0) {
            obj.setFuncionarioCargoCentroCusto(new FuncionarioCargoVO());
            return;
        }
        obj.setFuncionarioCargoCentroCusto(getFacadeFactory().getFuncionarioCargoFacade().consultarPorChavePrimaria(obj.getFuncionarioCargoCentroCusto().getCodigo(), nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>ContaCorrenteVO</code> relacionado ao objeto <code>ProvisaoCustoVO</code>.
     * Faz uso da chave primária da classe <code>ContaCorrenteVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosContaCorrenteTroco(ProvisaoCustoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getContaCorrenteTroco().getCodigo().intValue() == 0) {
            obj.setContaCorrenteTroco(new ContaCorrenteVO());
            return;
        }
        obj.setContaCorrenteTroco(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(obj.getContaCorrenteTroco().getCodigo(), false, nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>ContaCorrenteVO</code> relacionado ao objeto <code>ProvisaoCustoVO</code>.
     * Faz uso da chave primária da classe <code>ContaCorrenteVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosContaCorrente(ProvisaoCustoVO obj, int nivelMontarDados , UsuarioVO usuario) throws Exception {
        if (obj.getContaCorrente().getCodigo().intValue() == 0) {
            obj.setContaCorrente(new ContaCorrenteVO());
            return;
        }
        obj.setContaCorrente(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(obj.getContaCorrente().getCodigo(), false, nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>FuncionarioVO</code> relacionado ao objeto <code>ProvisaoCustoVO</code>.
     * Faz uso da chave primária da classe <code>FuncionarioVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosRequisitante(ProvisaoCustoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getRequisitante().getCodigo().intValue() == 0) {
            obj.setRequisitante(new FuncionarioVO());
            return;
        }
        obj.setRequisitante(getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimaria(obj.getRequisitante().getCodigo(), 0, false, nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>UsuarioVO</code> relacionado ao objeto <code>ProvisaoCustoVO</code>.
     * Faz uso da chave primária da classe <code>UsuarioVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosResponsavel(ProvisaoCustoVO obj, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if (obj.getResponsavel().getCodigo().intValue() == 0) {
            obj.setResponsavel(new UsuarioVO());
            return;
        }
        obj.setResponsavel(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavel().getCodigo(), nivelMontarDados,usuario));
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>ProvisaoCustoVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public ProvisaoCustoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM ProvisaoCusto WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado, nivelMontarDados,usuario));
    }

    public ProvisaoCustoVO consultarPorMapaLancamentoFuturo(Integer codigoMapa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sql = "select * from provisaocusto where mapalancamentofuturo = " + codigoMapa + " limit 1";
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
        if (!resultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( ProvisaoCusto ).");
        }
        return (montarDados(resultado, nivelMontarDados, usuario));

    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return ProvisaoCusto.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        ProvisaoCusto.idEntidade = idEntidade;
    }
}
