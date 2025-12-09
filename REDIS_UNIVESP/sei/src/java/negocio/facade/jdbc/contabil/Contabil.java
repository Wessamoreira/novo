package negocio.facade.jdbc.contabil;

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
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.contabil.ContabilVO;
import negocio.comuns.contabil.PlanoContaVO;
import negocio.comuns.financeiro.BancoVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaPagarVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.contabil.ContabilInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>ContabilVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>ContabilVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see ContabilVO
 * @see SuperEntidade
 */
@Repository
@Scope("singleton")
@Lazy
public class Contabil extends ControleAcesso implements ContabilInterfaceFacade {

    protected static String idEntidade;

    public Contabil() throws Exception {
        super();
        setIdEntidade("Contabil");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>ContabilVO</code>.
     */
    public ContabilVO novo() throws Exception {
        incluir(getIdEntidade());
        ContabilVO obj = new ContabilVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>ContabilVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>ContabilVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final ContabilVO obj) throws Exception {
        ContabilVO.validarDados(obj);
        Contabil.incluir(getIdEntidade());
        final String sql = "INSERT INTO Contabil( "
                + "contraPartida, "
                + "unidadeEnsino, "
                + "numeroDocumento, "
                + "data, "
                + "dataVencimento, "
                + "historico, "
                + "valor, "
                + "sinal, "
                + "contaReceber, "
                + "contaPagar, "
                + "conta, "
                + "pessoa, "
                + "fornecedor, "
                + "banco ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo";
        obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                sqlInserir.setInt(1, obj.getContraPartida());
                if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
                    sqlInserir.setInt(2, obj.getUnidadeEnsino().getCodigo().intValue());
                } else {
                    sqlInserir.setNull(2, 0);
                }
                sqlInserir.setString(3, obj.getNumeroDocumento());
                sqlInserir.setDate(4, Uteis.getDataJDBC(obj.getData()));
                sqlInserir.setDate(5, Uteis.getDataJDBC(obj.getDataVencimento()));
                sqlInserir.setString(6, obj.getHistorico());
                sqlInserir.setDouble(7, obj.getValor().doubleValue());
                sqlInserir.setString(8, obj.getSinal());
                if (obj.getContaReceber().getCodigo().intValue() != 0) {
                    sqlInserir.setInt(9, obj.getContaReceber().getCodigo().intValue());
                } else {
                    sqlInserir.setNull(9, 0);
                }
                if (obj.getContaPagar().getCodigo().intValue() != 0) {
                    sqlInserir.setInt(10, obj.getContaPagar().getCodigo().intValue());
                } else {
                    sqlInserir.setNull(10, 0);
                }
                if (obj.getConta().getCodigo().intValue() != 0) {
                    sqlInserir.setInt(11, obj.getConta().getCodigo().intValue());
                } else {
                    sqlInserir.setNull(11, 0);
                }
                if (obj.getPessoa().getCodigo().intValue() != 0) {
                    sqlInserir.setInt(12, obj.getPessoa().getCodigo().intValue());
                } else {
                    sqlInserir.setNull(12, 0);
                }
                if (obj.getFornecedor().getCodigo().intValue() != 0) {
                    sqlInserir.setInt(13, obj.getFornecedor().getCodigo().intValue());
                } else {
                    sqlInserir.setNull(13, 0);
                }
                if (obj.getBanco().getCodigo().intValue() != 0) {
                    sqlInserir.setInt(14, obj.getBanco().getCodigo().intValue());
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

    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>ContabilVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>ContabilVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(ContabilVO obj) throws Exception {
        try {
            excluir(getIdEntidade());
            excluirSemComit(obj);
        } catch (Exception e) {
            throw e;
        } finally {
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void gravarComContraPartida(ContabilVO contabil, PlanoContaVO contaDebito, PlanoContaVO contaCredito, PlanoContaVO contaContraPartida, Boolean pagamento,UsuarioVO usuario, ConfiguracaoFinanceiroVO configuracaoFinanceiro) throws Exception {
        try {
            Contabil.incluir(getIdEntidade());
            contabil.setConta(contaDebito);
            if (contaContraPartida != null && contaContraPartida.getCodigo() != 0) {
                contabil.setContraPartida(contaContraPartida.getCodigo());
            } else {
                contabil.setContraPartida(contaCredito.getCodigo());
            }
            contabil.setSinal("DE");
            incluir(contabil);
            contabil.setContraPartida(contaDebito.getCodigo());
            if (contaContraPartida != null && contaContraPartida.getCodigo() != 0) {
                contabil.setConta(contaContraPartida);
            } else {
                contabil.setConta(contaCredito);
            }
            contabil.setSinal("CR");
            contabil.setNovoObj(true);
            incluir(contabil);
            if (contabil.getJuro() != 0.0) {
                contabil.setSinal("DE");
                contabil.setValor(contabil.getJuro());
                contabil.setContraPartida(contaContraPartida.getCodigo());
//                contabil.setConta(getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSCONSULTARTODOS, usuario, null).getPlanoContaReceberPadraoJuro());
//                contabil.setHistorico(getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSCONSULTARTODOS, usuario, null).getHistoricoContaReceberPadraoJuro().getDescricao());
                contabil.setConta(configuracaoFinanceiro.getPlanoContaReceberPadraoJuro());
                contabil.setHistorico(configuracaoFinanceiro.getHistoricoContaReceberPadraoJuro().getDescricao());
                incluir(contabil);
                contabil.setSinal("CR");
                contabil.setContraPartida(configuracaoFinanceiro.getPlanoContaReceberPadraoJuro().getCodigo());
                contabil.setConta(contaContraPartida);
                incluir(contabil);
            }
            if (contabil.getDesconto() != 0.0) {
                contabil.setSinal("DE");
                contabil.setValor(contabil.getDesconto());
                contabil.setContraPartida(contaDebito.getCodigo());
                contabil.setConta(configuracaoFinanceiro.getPlanoContaReceberPadraoDesconto());
                contabil.setHistorico(configuracaoFinanceiro.getHistoricoContaReceberPadraoDesconto().getDescricao());
                incluir(contabil);
                contabil.setSinal("CR");
                contabil.setContraPartida(configuracaoFinanceiro.getPlanoContaReceberPadraoDesconto().getCodigo());
                contabil.setConta(contaDebito);
                incluir(contabil);
            }
        } catch (Exception e) {
            throw e;
        } finally {
        }
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP ContabilCons.jsp.
     * Define o tipo de consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP.
     * Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    public List consultar(String campoConsulta, String valorConsulta, ConfiguracaoFinanceiroVO configuracaoFinanceiro,UsuarioVO usuario) throws Exception {
        List objs = new ArrayList();
        if (campoConsulta.equals("codigo")) {
            if (valorConsulta.equals("")) {
                valorConsulta = "0";
            }
            int valorInt = Integer.parseInt(valorConsulta);
            objs = consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS,configuracaoFinanceiro,usuario);
        }
        if (campoConsulta.equals("numeroRegistro")) {
            if (valorConsulta.equals("")) {
                valorConsulta = "0";
            }
            int valorInt = Integer.parseInt(valorConsulta);
            objs = consultarPorNumeroRegistro(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS,configuracaoFinanceiro,usuario);
        }
        if (campoConsulta.equals("identificadorPlanoContaPlanoConta")) {
            objs = consultarPorIdentificadorPlanoContaPlanoConta(valorConsulta, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS,configuracaoFinanceiro,usuario);
        }
        if (campoConsulta.equals("numeroDocumento")) {
            objs = consultarPorNumeroDocumento(valorConsulta, true, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS,configuracaoFinanceiro,usuario);
        }
        if (campoConsulta.equals("sinal")) {
            objs = consultarPorSinal(valorConsulta, true, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS,configuracaoFinanceiro,usuario);
        }
        if (campoConsulta.equals("nomePessoa")) {
            objs = consultarPorNomeCliente(valorConsulta, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS,configuracaoFinanceiro,usuario);
        }
        if (campoConsulta.equals("nomeFornecedor")) {
            objs = consultarPorNomeFornecedor(valorConsulta, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS,configuracaoFinanceiro,usuario);
        }
        return objs;
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirSemComit(ContabilVO obj) throws Exception {
        String sql = "DELETE FROM Contabil WHERE ((codigo = ?))";
        getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
    }

    /**
     * Responsável por realizar uma consulta de <code>Contabil</code> através do valor do atributo 
     * <code>nome</code> da classe <code>Fornecedor</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>ContabilVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNomeFornecedor(String valorConsulta, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro,UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), true,usuario);
        String sqlStr = "SELECT Contabil.* FROM Contabil, Fornecedor WHERE Contabil.fornecedor = Fornecedor.codigo and upper( Fornecedor.nome ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY Fornecedor.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiro,usuario);
    }

    public ContabilVO consultarPorSinal_requisicaoDiferenteZero(String sinal, Integer requisicaoPagamento, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro,UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), true,usuario);
        String sqlStr = "SELECT Contabil.* FROM Contabil WHERE sinal = ('" + sinal + "') and numerorequisicaopagamento = " + requisicaoPagamento.intValue();
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( Contabil ).");
        }
        return montarDados(tabelaResultado, nivelMontarDados, configuracaoFinanceiro,usuario);
    }

    public List consultarPorRequisicaoPagamento(Integer valorConsulta, int nivelMontarDados, boolean verificarPermissao, ConfiguracaoFinanceiroVO configuracaoFinanceiro,UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), verificarPermissao,usuario);
        String sqlStr = "SELECT Contabil.* FROM Contabil WHERE numeroRequisicaoPagamento = " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiro,usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>Contabil</code> através do valor do atributo 
     * <code>nome</code> da classe <code>Cliente</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>ContabilVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNomeCliente(String valorConsulta, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro,UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), true,usuario);
        String sqlStr = "SELECT Contabil.* FROM Contabil, Cliente WHERE Contabil.cliente = Cliente.codigo and upper( Cliente.nome ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY Cliente.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiro,usuario);
    }

    public List consultarPorPlanoContaContraPartida(Integer conta, Integer contraPartida, Date dataIni, Date dataFim, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro,UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), true,usuario);
        String sqlStr = "SELECT * FROM contabil WHERE conta = " + conta + " AND contraPartida = " + contraPartida + " "
                + "AND data >= '" + Uteis.getDataJDBC(dataIni) + "' AND  data <= '" + Uteis.getDataJDBC(dataFim) + "' ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiro,usuario);
    }

    public List obterContasNoPeriodo(Date dataIni, Date dataFim, Integer unidadeEnsino,UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), true,usuario);
        String sqlStr = "select distinct conta from contabil where (data >= '" + Uteis.getDataJDBC(dataIni) + "') AND "
                + "(data <= '" + Uteis.getDataJDBC(dataFim) + "') ";
        if (unidadeEnsino != 0) {
            sqlStr = "select distinct conta from contabil where (data >= '" + Uteis.getDataJDBC(dataIni) + "') AND "
                    + "(data <= '" + Uteis.getDataJDBC(dataFim) + "') AND "
                    + "unidadeEnsino=" + unidadeEnsino;
        }

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        List vetResultado = new ArrayList();
        while (tabelaResultado.next()) {
            Integer obj = 0;
            obj = (new Integer(tabelaResultado.getInt("conta")));
            vetResultado.add(obj);
        }
        return vetResultado;
    }

    public Double obterTotalValorPorConta(Date dataIni, Date dataFim, Integer conta, String sinal, Integer unidadeEnsino) throws Exception {
        String sqlStr = "select sum(valor) from contabil where (data >= '" + Uteis.getDataJDBC(dataIni) + "') AND "
                + "(data <= '" + Uteis.getDataJDBC(dataFim) + "') AND "
                + "conta=" + conta + " AND sinal='" + sinal + "' ";
        if (unidadeEnsino != 0) {
            sqlStr = "select sum(valor) from contabil where (data >= '" + Uteis.getDataJDBC(dataIni) + "') AND "
                    + "(data <= '" + Uteis.getDataJDBC(dataFim) + "') AND "
                    + "conta=" + conta + " AND sinal='" + sinal + "' "
                    + "AND unidadeEnsino=" + unidadeEnsino;
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        tabelaResultado.next();
        return (new Double(tabelaResultado.getDouble(1)));
    }

    public Double obterTotalValorPorIdentificadorPlanoContaConsiderandoFilhas(Date dataIni, Date dataFim, String identificadorPlanoConta, String sinal, Integer unidadeEnsino) throws Exception {
        String sqlStr = "select sum(contabil.valor) from contabil, planoConta where ";
        if (dataIni != null) {
            sqlStr += "(contabil.data >= '" + Uteis.getDataJDBC(dataIni) + "') AND ";
        }
        if (dataFim != null) {
            sqlStr += "(contabil.data <= '" + Uteis.getDataJDBC(dataFim) + "') AND ";
        }
        sqlStr += "planoConta.identificadorPlanoConta like ('" + identificadorPlanoConta + "%') AND contabil.sinal='" + sinal + "' "
                + "AND planoConta.codigo = contabil.conta ";
        if (unidadeEnsino != 0) {
            sqlStr += "AND contabil.unidadeEnsino=" + unidadeEnsino;
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        tabelaResultado.next();
        return (new Double(tabelaResultado.getDouble(1)));
    }

    /**
     * Responsável por realizar uma consulta de <code>Contabil</code> através do valor do atributo 
     * <code>String sinal</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>ContabilVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorSinal(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro,UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso,usuario);
        String sqlStr = "SELECT * FROM Contabil WHERE upper( sinal ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY sinal";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiro,usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>Contabil</code> através do valor do atributo 
     * <code>String numeroDocumento</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>ContabilVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNumeroDocumento(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro,UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso,usuario);
        String sqlStr = "SELECT * FROM Contabil WHERE upper( numeroDocumento ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY numeroDocumento";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiro,usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>Contabil</code> através do valor do atributo 
     * <code>identificadorPlanoConta</code> da classe <code>PlanoConta</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>ContabilVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorIdentificadorPlanoContaPlanoConta(String valorConsulta, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro,UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), true,usuario);
        String sqlStr = "SELECT Contabil.* FROM Contabil, PlanoConta WHERE Contabil.conta = PlanoConta.codigo and upper( PlanoConta.identificadorPlanoConta ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY PlanoConta.identificadorPlanoConta";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiro,usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>Contabil</code> através do valor do atributo 
     * <code>String numeroRegistro</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>ContabilVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNumeroRegistro(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro,UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso,usuario);
        String sqlStr = "SELECT * FROM Contabil WHERE numeroRegistro >= " + valorConsulta.intValue() + " ORDER BY numeroRegistro";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiro,usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>Contabil</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>ContabilVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro,UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso,usuario);
        String sqlStr = "SELECT * FROM Contabil WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiro,usuario));
    }

    public List consultarPorContaAnoMesSinal(Integer conta, Integer ano, Integer mes, String sinal, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro,UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso,usuario);
        String sqlStr = "SELECT * FROM contabil WHERE conta=" + conta + " AND sinal='" + sinal
                + "' AND EXTRACT(YEAR FROM data)=" + ano + " AND EXTRACT(MONTH FROM data)=" + mes + "";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiro,usuario));
    }

    public Double consultarSaldoAnterior(Date dataIni, String sinal, Integer conta) throws Exception {
        String sqlStr = "SELECT SUM(valor) FROM Contabil WHERE data < '" + Uteis.getDataJDBC(dataIni) + "' AND sinal = '" + sinal + "' AND conta = " + conta;
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        tabelaResultado.next();
        double a = tabelaResultado.getDouble("sum");
        return (a);
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>ContabilVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro,UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList();
        while (tabelaResultado.next()) {
            ContabilVO obj = new ContabilVO();
            obj = montarDados(tabelaResultado, nivelMontarDados, configuracaoFinanceiro,usuario);
            vetResultado.add(obj);
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>ContabilVO</code>.
     * @return  O objeto da classe <code>ContabilVO</code> com os dados devidamente montados.
     */
    public static ContabilVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro,UsuarioVO usuario) throws Exception {
        ContabilVO obj = new ContabilVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.getConta().setCodigo(new Integer(dadosSQL.getInt("conta")));
        obj.getUnidadeEnsino().setCodigo(new Integer(dadosSQL.getInt("unidadeEnsino")));
        obj.setContraPartida(new Integer(dadosSQL.getInt("contraPartida")));
        obj.setNumeroDocumento(dadosSQL.getString("numeroDocumento"));
        obj.setData(dadosSQL.getDate("data"));
        obj.setDataVencimento(dadosSQL.getDate("dataVencimento"));
        obj.setHistorico(dadosSQL.getString("historico"));
        obj.setValor(new Double(dadosSQL.getDouble("valor")));
        obj.setSinal(dadosSQL.getString("sinal"));
        obj.getPessoa().setCodigo(new Integer(dadosSQL.getInt("pessoa")));
        obj.getFornecedor().setCodigo(new Integer(dadosSQL.getInt("fornecedor")));
        obj.getContaReceber().setCodigo(new Integer(dadosSQL.getInt("contaReceber")));
        obj.getContaPagar().setCodigo(new Integer(dadosSQL.getInt("contaPagar")));
        obj.getBanco().setCodigo(new Integer(dadosSQL.getInt("banco")));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }

        montarDadosConta(obj, nivelMontarDados,usuario);
        montarDadosPessoa(obj, nivelMontarDados,usuario);
        montarDadosBanco(obj, nivelMontarDados,usuario);
        montarDadosFornecedor(obj, nivelMontarDados, usuario);
        montarDadosUnidadeEnsino(obj, nivelMontarDados, usuario);
        montarDadosContaReceber(obj, nivelMontarDados, configuracaoFinanceiro,usuario);
        montarDadosContaPagar(obj, nivelMontarDados, usuario);
        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>FornecedorVO</code> relacionado ao objeto <code>ContabilVO</code>.
     * Faz uso da chave primária da classe <code>FornecedorVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosFornecedor(ContabilVO obj, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if (obj.getFornecedor().getCodigo().intValue() == 0) {
            obj.setFornecedor(new FornecedorVO());
            return;
        }
        obj.setFornecedor(getFacadeFactory().getFornecedorFacade().consultarPorChavePrimaria(obj.getFornecedor().getCodigo(), false, nivelMontarDados,usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>UnidadeEnsinoVO</code> relacionado ao objeto <code>ContabilVO</code>.
     * Faz uso da chave primária da classe <code>UnidadeEnsinoVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosUnidadeEnsino(ContabilVO obj, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if (obj.getUnidadeEnsino().getCodigo().intValue() == 0) {
            obj.setUnidadeEnsino(new UnidadeEnsinoVO());
            return;
        }
        obj.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsino().getCodigo(), false, nivelMontarDados,usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>ContaReceberVO</code> relacionado ao objeto <code>ContabilVO</code>.
     * Faz uso da chave primária da classe <code>ContaReceberVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosContaReceber(ContabilVO obj, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro , UsuarioVO usuario) throws Exception {
        if (obj.getContaReceber().getCodigo().intValue() == 0) {
            obj.setContaReceber(new ContaReceberVO());
            return;
        }
        obj.setContaReceber(getFacadeFactory().getContaReceberFacade().consultarPorChavePrimaria(obj.getContaReceber().getCodigo(), false, nivelMontarDados, configuracaoFinanceiro,usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>ContaPagarVO</code> relacionado ao objeto <code>ContabilVO</code>.
     * Faz uso da chave primária da classe <code>ContaPagarVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosContaPagar(ContabilVO obj, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if (obj.getContaPagar().getCodigo().intValue() == 0) {
            obj.setContaPagar(new ContaPagarVO());
            return;
        }
        obj.setContaPagar(getFacadeFactory().getContaPagarFacade().consultarPorChavePrimaria(obj.getContaPagar().getCodigo(), false, nivelMontarDados,usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>PessoaVO</code> relacionado ao objeto <code>ContabilVO</code>.
     * Faz uso da chave primária da classe <code>PessoaVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosPessoa(ContabilVO obj, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if (obj.getPessoa().getCodigo().intValue() == 0) {
            obj.setPessoa(new PessoaVO());
            return;
        }
        obj.setPessoa(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(obj.getPessoa().getCodigo(), false, nivelMontarDados,usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>BancoVO</code> relacionado ao objeto <code>ContabilVO</code>.
     * Faz uso da chave primária da classe <code>BancoVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosBanco(ContabilVO obj, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if (obj.getBanco().getCodigo().intValue() == 0) {
            obj.setBanco(new BancoVO());
            return;
        }
        obj.setBanco(getFacadeFactory().getBancoFacade().consultarPorChavePrimaria(obj.getBanco().getCodigo(), false, nivelMontarDados,usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>PlanoContaVO</code> relacionado ao objeto <code>ContabilVO</code>.
     * Faz uso da chave primária da classe <code>PlanoContaVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosConta(ContabilVO obj, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if (obj.getConta().getCodigo().intValue() == 0) {
            obj.setConta(new PlanoContaVO());
            return;
        }
        obj.setConta(getFacadeFactory().getPlanoContaFacade().consultarPorChavePrimaria(obj.getConta().getCodigo(), nivelMontarDados,usuario));
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>ContabilVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public ContabilVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro,UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), false,usuario);
        String sql = "SELECT * FROM Contabil WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( Contabil ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, configuracaoFinanceiro,usuario));
    }

    public Integer obterMenorAno() throws Exception {
        String sqlStr = "SELECT MIN(data) FROM Contabil";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        tabelaResultado.next();
        Date data = tabelaResultado.getDate(1);
        return Uteis.getAnoData(data);
    }

    public Integer obterValorChavePrimariaCodigo() throws Exception {
        String sqlStr = "SELECT MAX(codigo) FROM Contabil";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        tabelaResultado.next();
        return (new Integer(tabelaResultado.getInt(1)));
    }

    public Integer obterValorNumeroRegistroUltimo() throws Exception {
        String sqlStr = "SELECT MAX(numeroRegistro) FROM Contabil";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        if (!tabelaResultado.next()) {
            return null;
        }
        return (new Integer(tabelaResultado.getInt(1)));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return Contabil.idEntidade;
    }

    public static void setIdEntidade(String idEntidade) {
        Contabil.idEntidade = idEntidade;
    }
}
