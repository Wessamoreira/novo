package negocio.facade.jdbc.financeiro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.contabil.enumeradores.TipoOrigemLancamentoContabilEnum;
import negocio.comuns.financeiro.AgenciaVO;
import negocio.comuns.financeiro.ChequeVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ContaPagarNegociacaoPagamentoVO;
import negocio.comuns.financeiro.ContaPagarPagamentoVO;
import negocio.comuns.financeiro.ContaPagarVO;
import negocio.comuns.financeiro.FormaPagamentoNegociacaoPagamentoVO;
import negocio.comuns.financeiro.MapaLancamentoFuturoVO;
import negocio.comuns.financeiro.NegociacaoPagamentoVO;
import negocio.comuns.financeiro.enumerador.OrigemExtratoContaCorrenteEnum;
import negocio.comuns.financeiro.enumerador.TipoSacadoExtratoContaCorrenteEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Extenso;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.OrigemContaPagar;
import negocio.comuns.utilitarias.dominios.OrigemMapaLancamentoFuturo;
import negocio.comuns.utilitarias.dominios.SituacaoCheque;
import negocio.comuns.utilitarias.dominios.TipoFormaPagamento;
import negocio.comuns.utilitarias.dominios.TipoMapaLancamentoFuturo;
import negocio.comuns.utilitarias.dominios.TipoMovimentacaoFinanceira;
import negocio.comuns.utilitarias.dominios.TipoPagamento;
import negocio.comuns.utilitarias.dominios.TipoSacado;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.sad.DespesaDW;
import negocio.interfaces.financeiro.FormaPagamentoNegociacaoPagamentoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>PagamentoVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>PagamentoVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see PagamentoVO
 * @see ControleAcesso
 * @see NegociacaoPagamento
 */
@Repository
@Scope("singleton")
@Lazy
public class FormaPagamentoNegociacaoPagamento extends ControleAcesso implements FormaPagamentoNegociacaoPagamentoInterfaceFacade {

    /**
	 * 
	 */
	private static final long serialVersionUID = -6750575837748842792L;
	protected static String idEntidade;

    public FormaPagamentoNegociacaoPagamento() throws Exception {
        super();
        setIdEntidade("Pagamento");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>PagamentoVO</code>.
     */
    public FormaPagamentoNegociacaoPagamentoVO novo() throws Exception {
        FormaPagamentoNegociacaoPagamento.incluir(getIdEntidade());
        FormaPagamentoNegociacaoPagamentoVO obj = new FormaPagamentoNegociacaoPagamentoVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>PagamentoVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param formaPagamentoNegociacaoPagamentoVO  Objeto da classe <code>PagamentoVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final FormaPagamentoNegociacaoPagamentoVO obj, UsuarioVO usuario) throws Exception {
        FormaPagamentoNegociacaoPagamentoVO.validarDados(obj);
        final String sql = "INSERT INTO FormaPagamentoNegociacaoPagamento( valor, formaPagamento, negociacaoContaPagar, contaCorrente, cheque, qtdeParcelasCartaoCredito, operadoraCartao, categoriaDespesa,contaCorrenteOperadoraCartao ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
                PreparedStatement sqlInserir = cnctn.prepareStatement(sql);
                sqlInserir.setDouble(1, obj.getValor().doubleValue());
                if (obj.getFormaPagamento().getCodigo().intValue() != 0) {
                    sqlInserir.setInt(2, obj.getFormaPagamento().getCodigo().intValue());
                } else {
                    sqlInserir.setNull(2, 0);
                }
                if (obj.getNegociacaoContaPagar().intValue() != 0) {
                    sqlInserir.setInt(3, obj.getNegociacaoContaPagar().intValue());
                } else {
                    sqlInserir.setNull(3, 0);
                }
                if (obj.getContaCorrente().getCodigo().intValue() != 0) {
                    sqlInserir.setInt(4, obj.getContaCorrente().getCodigo().intValue());
                } else {
                    sqlInserir.setNull(4, 0);
                }
                if (obj.getCheque().getCodigo().intValue() != 0) {
                    sqlInserir.setInt(5, obj.getCheque().getCodigo().intValue());
                } else {
                    sqlInserir.setNull(5, 0);
                }
                sqlInserir.setInt(6, obj.getQtdeParcelasCartaoCredito());
                if (obj.getOperadoraCartaoVO().getCodigo() != 0) {
                    sqlInserir.setInt(7, obj.getOperadoraCartaoVO().getCodigo());
                } else {
                    sqlInserir.setNull(7, 0);
                }
                if (obj.getCategoriaDespesaVO().getCodigo() != 0) {
                    sqlInserir.setInt(8, obj.getCheque().getCodigo().intValue());
                } else {
                    sqlInserir.setNull(8, 0);
                }
                if (obj.getContaCorrenteOperadoraCartaoVO().getCodigo() != 0) {
                    sqlInserir.setInt(9, obj.getContaCorrenteOperadoraCartaoVO().getCodigo().intValue());
                } else {
                    sqlInserir.setNull(9, 0);
                }
                return sqlInserir;
            }
        }, new ResultSetExtractor<Object>() {

            public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                if (rs.next()) {
                    obj.setNovoObj(Boolean.FALSE);
                    return rs.getInt("codigo");
                }
                return null;
            }
        }));
        if (obj.getFormaPagamento().getTipo().equals(TipoFormaPagamento.CHEQUE.getValor())) {
            adicionarCodigoPagamentoEmCheque(obj, usuario);
        }
        obj.setNovoObj(Boolean.FALSE);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirCheque(final Integer rec, final Integer cheque, UsuarioVO usuario) throws Exception {
        final String sql = "UPDATE FormaPagamentoNegociacaoPagamento set cheque=? WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
                PreparedStatement sqlAlterar = cnctn.prepareStatement(sql);
                if (cheque.intValue() != 0) {
                    sqlAlterar.setInt(1, cheque.intValue());
                } else {
                    sqlAlterar.setNull(1, 0);
                }
                sqlAlterar.setInt(2, rec.intValue());
                return sqlAlterar;
            }
        });
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void adicionarCodigoPagamentoEmCheque(FormaPagamentoNegociacaoPagamentoVO obj, UsuarioVO usuario) throws Exception {
        Integer unidadeEnsino = 0;
        if (obj.getCheque().getChequeProprio()) {
            obj.getCheque().setContaCorrente(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(obj.getCheque().getContaCorrente().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
            unidadeEnsino = getFacadeFactory().getNegociacaoPagamentoFacade().consultarCodigoUnidadeEnsinoPelaNegociacaoPagamento(obj.getNegociacaoContaPagar());
//            unidadeEnsino = obj.getCheque().getContaCorrente().getUnidadeEnsino().getCodigo();
        } else {
            unidadeEnsino = obj.getCheque().getUnidadeEnsino().getCodigo();
        }
        Integer cheque = getFacadeFactory().getChequeFacade().incluirChequePagamento(obj.getCodigo(), unidadeEnsino, obj.getCheque(), usuario);
        incluirCheque(obj.getCodigo(), cheque, usuario);
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>PagamentoVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param formaPagamentoNegociacaoPagamentoVO    Objeto da classe <code>PagamentoVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final FormaPagamentoNegociacaoPagamentoVO obj, UsuarioVO usuario) throws Exception {
        FormaPagamentoNegociacaoPagamentoVO.validarDados(obj);
        final String sql = "UPDATE FormaPagamentoNegociacaoPagamento set valor=?, formaPagamento=?, negociacaoContaPagar=?, contaCorrente=?, cheque = ?, qtdeParcelasCartaoCredito = ?, operadoraCartao = ?, categoriaDespesa = ?, contaCorrenteOperadoraCartao = ? WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
                PreparedStatement sqlAlterar = cnctn.prepareStatement(sql);
                sqlAlterar.setDouble(1, obj.getValor().doubleValue());
                if (obj.getFormaPagamento().getCodigo().intValue() != 0) {
                    sqlAlterar.setInt(2, obj.getFormaPagamento().getCodigo().intValue());
                } else {
                    sqlAlterar.setNull(2, 0);
                }
                if (obj.getNegociacaoContaPagar().intValue() != 0) {
                    sqlAlterar.setInt(3, obj.getNegociacaoContaPagar().intValue());
                } else {
                    sqlAlterar.setNull(3, 0);
                }
                if (obj.getContaCorrente().getCodigo().intValue() != 0) {
                    sqlAlterar.setInt(4, obj.getContaCorrente().getCodigo().intValue());
                } else {
                    sqlAlterar.setNull(4, 0);
                }
                if (obj.getCheque().getCodigo().intValue() != 0) {
                    sqlAlterar.setInt(5, obj.getCheque().getCodigo().intValue());
                } else {
                    sqlAlterar.setNull(5, 0);
                }
                sqlAlterar.setInt(6, obj.getQtdeParcelasCartaoCredito());
                if (obj.getOperadoraCartaoVO().getCodigo() != 0) {
                	sqlAlterar.setInt(7, obj.getOperadoraCartaoVO().getCodigo());
                } else {
                	sqlAlterar.setNull(7, 0);
                }
                if (obj.getCategoriaDespesaVO().getCodigo() != 0) {
                	sqlAlterar.setInt(8, obj.getCheque().getCodigo().intValue());
                } else {
                	sqlAlterar.setNull(8, 0);
                }
                if (obj.getContaCorrenteOperadoraCartaoVO().getCodigo() != 0) {
                	sqlAlterar.setInt(9, obj.getContaCorrenteOperadoraCartaoVO().getCodigo().intValue());
                } else {
                	sqlAlterar.setNull(9, 0);
                }
                sqlAlterar.setInt(10, obj.getCodigo().intValue());
                return sqlAlterar;
            }
        });
        adicionarCodigoPagamentoEmCheque(obj, usuario);
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>PagamentoVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param formaPagamentoNegociacaoPagamentoVO    Objeto da classe <code>PagamentoVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(FormaPagamentoNegociacaoPagamentoVO obj, UsuarioVO usuario) throws Exception {
        String sql = "DELETE FROM FormaPagamentoNegociacaoPagamento WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarVinculoCheque(Integer codigo, Integer cheque) throws Exception {
        String sql = "UPDATE FormaPagamentoNegociacaoPagamento SET cheque = ? WHERE ((codigo = ?))";
        getConexao().getJdbcTemplate().update(sql, new Object[]{cheque, codigo});
    }

    /**
     * Responsável por realizar uma consulta de <code>Pagamento</code> através do valor do atributo 
     * <code>codigo</code> da classe <code>NegociacaoPagamento</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>PagamentoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigoNegociacaoPagamento(Integer valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), true, usuario);
        String sqlStr = "SELECT FormaPagamentoNegociacaoPagamento.* FROM FormaPagamentoNegociacaoPagamento, NegociacaoPagamento WHERE FormaPagamentoNegociacaoPagamento.negociacaoContaPagar = NegociacaoPagamento.codigo and NegociacaoPagamento.codigo = " + valorConsulta.intValue() + " ORDER BY NegociacaoPagamento.codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

//    /**
//     * Responsável por realizar uma consulta de <code>Pagamento</code> através do valor do atributo
//     * <code>nome</code> da classe <code>FormaPagamento</code>
//     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
//     * @return  List Contendo vários objetos da classe <code>PagamentoVO</code> resultantes da consulta.
//     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
//     */
//    public List consultarPorNomeFormaPagamento(String valorConsulta, int nivelMontarDados) throws Exception {
//        ControleAcesso.consultar(getIdEntidade(), true);
//        String sqlStr = "SELECT Pagamento.* FROM Pagamento, FormaPagamento WHERE Pagamento.formaPagamento = FormaPagamento.codigo and upper( FormaPagamento.nome ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY FormaPagamento.nome";
//        Statement stm = con.createStatement();
//        ResultSet tabelaResultado = stm.executeQuery(sqlStr);
//        return montarDadosConsulta(tabelaResultado, nivelMontarDados);
//    }
    /**
     * Responsável por realizar uma consulta de <code>Pagamento</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>PagamentoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
//    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados) throws Exception {
//        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
//        String sqlStr = "SELECT * FROM Pagamento WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
//        Statement stm = con.createStatement();
//        ResultSet tabelaResultado = stm.executeQuery(sqlStr);
//        return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
//    }
    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>PagamentoVO</code> resultantes da consulta.
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
     * em um objeto da classe <code>PagamentoVO</code>.
     * @return  O objeto da classe <code>PagamentoVO</code> com os dados devidamente montados.
     */
    public static FormaPagamentoNegociacaoPagamentoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        FormaPagamentoNegociacaoPagamentoVO obj = new FormaPagamentoNegociacaoPagamentoVO();
        obj.setCodigo((dadosSQL.getInt("codigo")));
        obj.setValor((dadosSQL.getDouble("valor")));
        obj.getFormaPagamento().setCodigo((dadosSQL.getInt("formaPagamento")));
        obj.setNegociacaoContaPagar((dadosSQL.getInt("negociacaoContaPagar")));
        obj.getContaCorrente().setCodigo((dadosSQL.getInt("contaCorrente")));
        obj.getCheque().setCodigo((dadosSQL.getInt("cheque")));
        obj.getOperadoraCartaoVO().setCodigo((dadosSQL.getInt("operadoraCartao")));
        obj.getCategoriaDespesaVO().setCodigo((dadosSQL.getInt("categoriaDespesa")));
        obj.getContaCorrenteOperadoraCartaoVO().setCodigo((dadosSQL.getInt("contaCorrenteOperadoraCartao")));
        obj.setQtdeParcelasCartaoCredito((dadosSQL.getInt("qtdeParcelasCartaoCredito")));

        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        montarDadosCheque(obj, nivelMontarDados, usuario);
        montarDadosFormaPagamento(obj, nivelMontarDados, usuario);
        montarDadosContaCorrente(obj, nivelMontarDados, usuario);
        return obj;
    }

    public static void montarDadosCheque(FormaPagamentoNegociacaoPagamentoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getCheque().getCodigo().intValue() == 0) {
            obj.setCheque(new ChequeVO());
            return;
        }
        obj.setCheque(getFacadeFactory().getChequeFacade().consultarPorChavePrimaria(obj.getCheque().getCodigo(), false, nivelMontarDados, usuario));
    }

    public static void montarDadosFormaPagamento(FormaPagamentoNegociacaoPagamentoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getFormaPagamento().getCodigo().intValue() == 0) {
            obj.setFormaPagamento(new FormaPagamentoVO());
            return;
        }
        obj.setFormaPagamento(getFacadeFactory().getFormaPagamentoFacade().consultarPorChavePrimaria(obj.getFormaPagamento().getCodigo(), false, nivelMontarDados, usuario));
    }

    public static void montarDadosContaCorrente(FormaPagamentoNegociacaoPagamentoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getContaCorrente().getCodigo().intValue() == 0) {
            obj.setContaCorrente(new ContaCorrenteVO());
            return;
        }
        obj.setContaCorrente(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(obj.getContaCorrente().getCodigo(), false, nivelMontarDados, usuario));
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirPagamentos(NegociacaoPagamentoVO nPtRLog, UsuarioVO usuario) throws Exception {
        desfazerTodosPagamentos(nPtRLog, usuario);
        String sql = "DELETE FROM FormaPagamentoNegociacaoPagamento WHERE (negociacaoContaPagar = ?)"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(sql, new Object[]{nPtRLog.getCodigo()});
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void desfazerTodosPagamentos(NegociacaoPagamentoVO nPtRLog, UsuarioVO usuario) throws Exception {
        List listaPtRLog = consultarFormaPagamentoNegociacaoPagamento(nPtRLog.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario);
        Iterator i = listaPtRLog.iterator();
        while (i.hasNext()) {
            FormaPagamentoNegociacaoPagamentoVO pgtR = (FormaPagamentoNegociacaoPagamentoVO) i.next();
            if (!pgtR.getCheque().getCodigo().equals(0) && pgtR.getCheque().getChequeProprio()) {
                getFacadeFactory().getMapaLancamentoFuturoFacade().excluirPorCodigoCheque(pgtR.getCheque().getCodigo(), false, usuario);
                getFacadeFactory().getExtratoMapaLancamentoFuturoFacade().excluirPorCodigoCheque(pgtR.getCheque().getCodigo(), false, usuario);
            }
            if(Uteis.isAtributoPreenchido(pgtR.getContaCorrente())) {
            	criarMovimentacaoCaixaContaCorrente(pgtR, nPtRLog, TipoMovimentacaoFinanceira.ENTRADA, usuario);
                criarMovimentacaoCaixa(pgtR, nPtRLog, "EN", false, usuario);	
            }
            desvincularChequePagamento(pgtR.getCheque(), nPtRLog.getCaixa().getCodigo(), pgtR.getCodigo(), usuario);
            alterarContasPagarPagamento(pgtR.getCodigo(), nPtRLog.getMotivoAlteracao(), nPtRLog.getResponsavel().getCodigo(), usuario);
            
           
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void desvincularChequePagamento(ChequeVO chequeVO, Integer contaCorrente, Integer pgto, UsuarioVO usuario) throws Exception {
        if (chequeVO.getChequeProprio()) {
            getFacadeFactory().getFormaPagamentoNegociacaoPagamentoFacade().alterarVinculoCheque(pgto, null);
            getFacadeFactory().getChequeFacade().excluir(chequeVO);
        } else {
            getFacadeFactory().getChequeFacade().desvincularChequePagamento(pgto, "EC", contaCorrente);
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarValorPagoContaPagar(Integer codigoContaReceber, Double valor, UsuarioVO usuario) throws Exception {
        ContaPagarVO contaPagar = getFacadeFactory().getContaPagarFacade().consultarPorChavePrimariaSituacao(codigoContaReceber, false, Uteis.NIVELMONTARDADOS_TODOS, usuario);
        if (contaPagar != null) {
            new DespesaDW().incluir(contaPagar.getDespesaDWVO(contaPagar.getValorPago() * -1, "PA"), usuario);
            if (!contaPagar.getTipoOrigem().equals(OrigemContaPagar.COMPRA.getValor())) {
                new DespesaDW().incluir(contaPagar.getDespesaDWVO(contaPagar.getValorPago() * -1, "RE"), usuario);
            }
            contaPagar.setValorPago(0.0);
            contaPagar.setJuro(0.0);
            contaPagar.setMulta(0.0);
            contaPagar.setDesconto(0.0);
            contaPagar.setDescontoPorUsoAdiantamento(0.0);
            contaPagar.setSituacao("AP");
            getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().realizarDistribuicaoValoresCentroResultado(contaPagar.getListaCentroResultadoOrigemVOs(), contaPagar.getPrevisaoValorPago(), usuario);
            getFacadeFactory().getContaPagarFacade().alterar(contaPagar, false, true, usuario);
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarContasPagarPagamento(Integer pagamento, String motivo, Integer responsavel, UsuarioVO usuario) throws Exception {
        ContaPagarPagamentoVO contaPagarPagamentoVO;
        List listaCtRPtRLog = getFacadeFactory().getContaPagarPagamentoFacade().consultarPorPagamento(pagamento, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
        Iterator j = listaCtRPtRLog.iterator();
        while (j.hasNext()) {
            contaPagarPagamentoVO = (ContaPagarPagamentoVO) j.next();
            //retirado temporariamente por nao fazer sentido na alteracao.
            //resposta ao comentário anterior, alteração tem que ser efetuada para conseguir estornar uma negociacaoPagamento
            contaPagarPagamentoVO.setPagamento(0);
            getFacadeFactory().getContaPagarPagamentoFacade().alterar(contaPagarPagamentoVO, usuario);
            contaPagarPagamentoVO.setTipoPagamento(TipoPagamento.DEBITO.getValor());
            contaPagarPagamentoVO.setData(new Date());
            contaPagarPagamentoVO.setMotivo(motivo);
            contaPagarPagamentoVO.getResponsavel().setCodigo(responsavel);
            getFacadeFactory().getContaPagarPagamentoFacade().incluir(contaPagarPagamentoVO, usuario);
            alterarValorPagoContaPagar(contaPagarPagamentoVO.getContaPagar(), contaPagarPagamentoVO.getValorTotalPagamento(), usuario);
            getFacadeFactory().getLancamentoContabilFacade().excluirPorCodOrigemTipoOrigem(contaPagarPagamentoVO.getContaPagar().toString(), TipoOrigemLancamentoContabilEnum.PAGAR, false, usuario);
        }
    }

    /**
     * Operação responsável por alterar todos os objetos da <code>PagamentoVO</code> contidos em um Hashtable no BD.
     * Faz uso da operação <code>excluirPagamentos</code> e <code>incluirPagamentos</code> disponíveis na classe <code>Pagamento</code>.
     * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarPagamentos(NegociacaoPagamentoVO negociacaoPagamentoVO, UsuarioVO usuario) throws Exception {
        excluirPagamentos(negociacaoPagamentoVO, usuario);
        prepararContaPagarParaMovimentacao(negociacaoPagamentoVO, usuario);
        incluirPagamentos(negociacaoPagamentoVO, usuario);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void prepararContaPagarParaMovimentacao(NegociacaoPagamentoVO negociacaoPagamentoVO, UsuarioVO usuario) throws Exception {
        Iterator i = negociacaoPagamentoVO.getContaPagarNegociacaoPagamentoVOs().iterator();
        while (i.hasNext()) {
            ContaPagarNegociacaoPagamentoVO cpnpVO = (ContaPagarNegociacaoPagamentoVO) i.next();
            getFacadeFactory().getContaPagarFacade().alterar(cpnpVO.getContaPagar(), false, true, usuario);
        }
    }

    /**
     * Operação responsável por incluir objetos da <code>PagamentoVO</code> no BD.
     * Garantindo o relacionamento com a entidade principal <code>financeiro.NegociacaoPagamento</code> através do atributo de vínculo.
     * @param objetos List contendo os objetos a serem gravados no BD da classe.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirPagamentos(NegociacaoPagamentoVO negociacaoPagamentoVO, UsuarioVO usuario) throws Exception {
        try {
            Iterator e = negociacaoPagamentoVO.getFormaPagamentoNegociacaoPagamentoVOs().iterator();
            while (e.hasNext()) {
                FormaPagamentoNegociacaoPagamentoVO formaPagamentoNegociacaoPagamentoVO = (FormaPagamentoNegociacaoPagamentoVO) e.next();
                formaPagamentoNegociacaoPagamentoVO.setNegociacaoContaPagar(negociacaoPagamentoVO.getCodigo());
                criarMovimentacaoCaixa(formaPagamentoNegociacaoPagamentoVO, negociacaoPagamentoVO, "SA", true, usuario);
                criarMovimentacaoCaixaContaCorrente(formaPagamentoNegociacaoPagamentoVO, negociacaoPagamentoVO, TipoMovimentacaoFinanceira.SAIDA, usuario);
                incluir(formaPagamentoNegociacaoPagamentoVO, usuario);
            }
        } catch (Exception e) {
            for (FormaPagamentoNegociacaoPagamentoVO obj : negociacaoPagamentoVO.getFormaPagamentoNegociacaoPagamentoVOs()) {
                obj.setNovoObj(true);
                obj.setCodigo(0);
            }
            throw e;
        }
    }
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void criarMovimentacaoCaixaContaCorrente(FormaPagamentoNegociacaoPagamentoVO formaPagamentoNegociacaoPagamentoVO, NegociacaoPagamentoVO negociacaoPagamentoVO, TipoMovimentacaoFinanceira tipoMovimentacao, UsuarioVO usuario) throws Exception {
        Integer codigoSacado = 0;
        String nomeSacado = "";
        TipoSacadoExtratoContaCorrenteEnum tipoSacado = null;
        Date dataOcorrido =  negociacaoPagamentoVO.getData();
        if(tipoMovimentacao.equals(TipoMovimentacaoFinanceira.ENTRADA)){
            dataOcorrido = negociacaoPagamentoVO.getDataEstorno();
        }
        if (negociacaoPagamentoVO.getTipoSacado().equals(TipoSacado.FORNECEDOR.getValor())) {
            codigoSacado = negociacaoPagamentoVO.getFornecedor().getCodigo();
            nomeSacado = negociacaoPagamentoVO.getFornecedor().getNome();
            tipoSacado = TipoSacadoExtratoContaCorrenteEnum.FORNECEDOR;
        } else if (negociacaoPagamentoVO.getTipoSacado().equals(TipoSacado.FUNCIONARIO_PROFESSOR.getValor())) {
            codigoSacado = negociacaoPagamentoVO.getFuncionario().getPessoa().getCodigo();
            nomeSacado = negociacaoPagamentoVO.getFuncionario().getPessoa().getNome();
            tipoSacado = TipoSacadoExtratoContaCorrenteEnum.FUNCIONARIO_PROFESSOR;
        }else if (negociacaoPagamentoVO.getTipoSacado().equals(TipoSacado.ALUNO.getValor())) {
            codigoSacado = negociacaoPagamentoVO.getAluno().getCodigo();
            nomeSacado = negociacaoPagamentoVO.getAluno().getNome();
            tipoSacado = TipoSacadoExtratoContaCorrenteEnum.ALUNO;
        }else if (negociacaoPagamentoVO.getTipoSacado().equals(TipoSacado.RESPONSAVEL_FINANCEIRO.getValor())) {
            codigoSacado = negociacaoPagamentoVO.getResponsavelFinanceiro().getCodigo();
            nomeSacado = negociacaoPagamentoVO.getResponsavelFinanceiro().getNome();
            tipoSacado = TipoSacadoExtratoContaCorrenteEnum.RESPONSAVEL_FINANCEIRO;
        } else if (negociacaoPagamentoVO.getTipoSacado().equals(TipoSacado.BANCO.getValor())) {
            codigoSacado = negociacaoPagamentoVO.getBanco().getCodigo();
            negociacaoPagamentoVO.setBanco(getFacadeFactory().getBancoFacade().consultarPorChavePrimaria(codigoSacado, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
            nomeSacado = negociacaoPagamentoVO.getBanco().getNome();
            tipoSacado = TipoSacadoExtratoContaCorrenteEnum.BANCO;
        }else if (negociacaoPagamentoVO.getTipoSacado().equals(TipoSacado.PARCEIRO.getValor())) {
            codigoSacado = negociacaoPagamentoVO.getParceiro().getCodigo();
            nomeSacado = negociacaoPagamentoVO.getParceiro().getNome();
            tipoSacado = TipoSacadoExtratoContaCorrenteEnum.PARCEIRO;
        }else if (negociacaoPagamentoVO.getTipoSacado().equals(TipoSacado.OPERADORA_CARTAO.getValor())) {
        	 codigoSacado = negociacaoPagamentoVO.getOperadoraCartao().getCodigo();
             nomeSacado = negociacaoPagamentoVO.getOperadoraCartao().getNome();
             tipoSacado = TipoSacadoExtratoContaCorrenteEnum.OPERADORA_CARTAO;
		}
        
        
        if ((formaPagamentoNegociacaoPagamentoVO.getFormaPagamento().getTipo().equals(TipoFormaPagamento.DINHEIRO.getValor())
                || formaPagamentoNegociacaoPagamentoVO.getFormaPagamento().getTipo().equals(TipoFormaPagamento.BOLETO_BANCARIO.getValor())
                || formaPagamentoNegociacaoPagamentoVO.getFormaPagamento().getTipo().equals(TipoFormaPagamento.CARTAO_DE_CREDITO.getValor())                
                || formaPagamentoNegociacaoPagamentoVO.getFormaPagamento().getTipo().equals(TipoFormaPagamento.CARTAO_DE_DEBITO.getValor())
                || formaPagamentoNegociacaoPagamentoVO.getFormaPagamento().getTipo().equals(TipoFormaPagamento.DEBITO_EM_CONTA_CORRENTE.getValor())
                || formaPagamentoNegociacaoPagamentoVO.getFormaPagamento().getTipo().equals(TipoFormaPagamento.DEPOSITO.getValor()))
                && !formaPagamentoNegociacaoPagamentoVO.getContaCorrente().getContaCaixa()){                               
            getFacadeFactory().getExtratoContaCorrenteFacade().executarCriacaoExtratoContaCorrente(formaPagamentoNegociacaoPagamentoVO.getValor(), dataOcorrido, 
                    OrigemExtratoContaCorrenteEnum.PAGAMENTO, tipoMovimentacao, negociacaoPagamentoVO.getCodigo(), null, nomeSacado, codigoSacado, tipoSacado, 
                    null,formaPagamentoNegociacaoPagamentoVO.getFormaPagamento(), formaPagamentoNegociacaoPagamentoVO.getContaCorrente(), 
                    negociacaoPagamentoVO.getUnidadeEnsino(), formaPagamentoNegociacaoPagamentoVO.getOperadoraCartaoVO(), negociacaoPagamentoVO.isDesconsiderarConciliacaoBancaria(), null, negociacaoPagamentoVO.getBloqueioPorFechamentoMesLiberado(), negociacaoPagamentoVO.getResponsavel());
        }
        if(formaPagamentoNegociacaoPagamentoVO.getFormaPagamento().getTipo().equals(TipoFormaPagamento.CHEQUE.getValor())
                && formaPagamentoNegociacaoPagamentoVO.getCheque().getChequeProprio() && formaPagamentoNegociacaoPagamentoVO.getCheque().getPago()
                && formaPagamentoNegociacaoPagamentoVO.getCheque().getSituacao().equals("PA")
                && !formaPagamentoNegociacaoPagamentoVO.getContaCorrente().getContaCaixa()){
            getFacadeFactory().getExtratoContaCorrenteFacade().executarCriacaoExtratoContaCorrente(formaPagamentoNegociacaoPagamentoVO.getValor(), dataOcorrido, 
                    OrigemExtratoContaCorrenteEnum.PAGAMENTO, tipoMovimentacao, negociacaoPagamentoVO.getCodigo(), formaPagamentoNegociacaoPagamentoVO.getCheque(), nomeSacado, codigoSacado, tipoSacado, 
                    null,formaPagamentoNegociacaoPagamentoVO.getFormaPagamento(), formaPagamentoNegociacaoPagamentoVO.getContaCorrente(), 
                    negociacaoPagamentoVO.getUnidadeEnsino(), null, negociacaoPagamentoVO.isDesconsiderarConciliacaoBancaria(), null, negociacaoPagamentoVO.getBloqueioPorFechamentoMesLiberado(), negociacaoPagamentoVO.getResponsavel());
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void criarMovimentacaoCaixa(FormaPagamentoNegociacaoPagamentoVO formaPagamentoNegociacaoPagamentoVO, NegociacaoPagamentoVO negociacaoPagamentoVO, String tipoMovimentacao, Boolean validarSaldoPagamentoCaixa, UsuarioVO usuario) throws Exception {

        Integer codigoFornecedor = 0;
        Integer codigoPessoa = 0;
        Integer codigoBanco = 0;
        Integer codigoParceiro = 0;
        Integer codigoOperadoraCartao = 0;

        if (negociacaoPagamentoVO.getTipoSacado().equals(TipoSacado.FORNECEDOR.getValor())) {
            codigoFornecedor = negociacaoPagamentoVO.getFornecedor().getCodigo();
        } else if (negociacaoPagamentoVO.getTipoSacado().equals(TipoSacado.FUNCIONARIO_PROFESSOR.getValor())) {
            codigoPessoa = negociacaoPagamentoVO.getFuncionario().getPessoa().getCodigo();
        }else if (negociacaoPagamentoVO.getTipoSacado().equals(TipoSacado.ALUNO.getValor())) {
                codigoPessoa = negociacaoPagamentoVO.getAluno().getCodigo();
        }else if (negociacaoPagamentoVO.getTipoSacado().equals(TipoSacado.RESPONSAVEL_FINANCEIRO.getValor())) {
            codigoPessoa = negociacaoPagamentoVO.getResponsavelFinanceiro().getCodigo();
        } else if (negociacaoPagamentoVO.getTipoSacado().equals(TipoSacado.BANCO.getValor())) {
            codigoBanco = negociacaoPagamentoVO.getBanco().getCodigo();
        }else if (negociacaoPagamentoVO.getTipoSacado().equals(TipoSacado.PARCEIRO.getValor())) {
            codigoParceiro = negociacaoPagamentoVO.getParceiro().getCodigo();
        }else if (negociacaoPagamentoVO.getTipoSacado().equals(TipoSacado.OPERADORA_CARTAO.getValor())) {
        	codigoOperadoraCartao = negociacaoPagamentoVO.getOperadoraCartao().getCodigo();
        }

        if (formaPagamentoNegociacaoPagamentoVO.getFormaPagamento().getTipo().equals(TipoFormaPagamento.DINHEIRO.getValor())) {
        	if (validarSaldoPagamentoCaixa.booleanValue()) {
        		getFacadeFactory().getFluxoCaixaFacade().validarSaldoDinheiroPagamento(formaPagamentoNegociacaoPagamentoVO, usuario);
        	}
            getFacadeFactory().getFluxoCaixaFacade().criarMovimentacaoCaixa(formaPagamentoNegociacaoPagamentoVO.getValor(), formaPagamentoNegociacaoPagamentoVO.getContaCorrente().getCodigo(), tipoMovimentacao, "PA", formaPagamentoNegociacaoPagamentoVO.getFormaPagamento().getCodigo(), negociacaoPagamentoVO.getCodigo(), negociacaoPagamentoVO.getResponsavel().getCodigo(), codigoPessoa, codigoFornecedor, codigoBanco, codigoParceiro, formaPagamentoNegociacaoPagamentoVO.getCheque(), codigoOperadoraCartao, usuario);            
        }
        if (formaPagamentoNegociacaoPagamentoVO.getFormaPagamento().getTipo().equals(TipoFormaPagamento.CHEQUE.getValor())) {
            if (formaPagamentoNegociacaoPagamentoVO.getCheque().getChequeProprio()) {
                if(tipoMovimentacao.equals("SA")){
                    getFacadeFactory().getChequeFacade().incluir(formaPagamentoNegociacaoPagamentoVO.getCheque(), usuario);                
                    criarPendenciaFinanceiraCheque(formaPagamentoNegociacaoPagamentoVO.getCheque(), negociacaoPagamentoVO, usuario);
                    criarExtratoMapaLancamentoFuturoChequeAReceber(formaPagamentoNegociacaoPagamentoVO.getCheque(), negociacaoPagamentoVO, usuario);
                }
            } else {
                criarMovimentacaoContaChequeTerceiros(formaPagamentoNegociacaoPagamentoVO, negociacaoPagamentoVO, tipoMovimentacao, usuario);
            }
        }
        if (formaPagamentoNegociacaoPagamentoVO.getFormaPagamento().getTipo().equals(TipoFormaPagamento.CARTAO_DE_CREDITO.getValor())
                || formaPagamentoNegociacaoPagamentoVO.getFormaPagamento().getTipo().equals(TipoFormaPagamento.CARTAO_DE_DEBITO.getValor())
                || formaPagamentoNegociacaoPagamentoVO.getFormaPagamento().getTipo().equals(TipoFormaPagamento.BOLETO_BANCARIO.getValor())
                || formaPagamentoNegociacaoPagamentoVO.getFormaPagamento().getTipo().equals(TipoFormaPagamento.DEBITO_EM_CONTA_CORRENTE.getValor())) {
            getFacadeFactory().getContaCorrenteFacade().movimentarSaldoContaCorrente(formaPagamentoNegociacaoPagamentoVO.getContaCorrente().getCodigo(), tipoMovimentacao, formaPagamentoNegociacaoPagamentoVO.getValor(), usuario);
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void criarPendenciaFinanceiraCheque(ChequeVO cheque, NegociacaoPagamentoVO negociacaoPagamentoVO, UsuarioVO usuario) throws Exception {
        MapaLancamentoFuturoVO mapaLancamentoFuturoVO = cheque.criarMapaLancamentoFuturo(negociacaoPagamentoVO.getCodigo(), OrigemMapaLancamentoFuturo.NEGOCIACAO_PAGAMENTO.getValor(), TipoMapaLancamentoFuturo.CHEQUE_A_PAGAR.getValor(), usuario);
        getFacadeFactory().getMapaLancamentoFuturoFacade().incluir(mapaLancamentoFuturoVO, usuario);
    }
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void criarExtratoMapaLancamentoFuturoChequeAReceber(ChequeVO cheque, NegociacaoPagamentoVO negociacaoPagamentoVO, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getExtratoMapaLancamentoFuturoFacade().realizarCriacaoInclusaoExtratoMapaLancamentoFuturo(cheque, negociacaoPagamentoVO.getData(), cheque.getContaCorrente().getCodigo(), TipoMapaLancamentoFuturo.CHEQUE_A_PAGAR, usuario);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void criarMovimentacaoContaChequeTerceiros(FormaPagamentoNegociacaoPagamentoVO formaPagamentoNegociacaoPagamentoVO, NegociacaoPagamentoVO negociacaoPagamentoVO, String tipoMovimentacao, UsuarioVO usuario) throws Exception {
        Integer codigoFornecedor = 0;
        Integer codigoPessoa = 0;
        Integer codigoBanco = 0;
        Integer codigoParceiro = 0;
        Integer codigoOperadoraCartao = 0;

        if (negociacaoPagamentoVO.getTipoSacado().equals(TipoSacado.FORNECEDOR.getValor())) {
            codigoFornecedor = negociacaoPagamentoVO.getFornecedor().getCodigo();
        } else if (negociacaoPagamentoVO.getTipoSacado().equals(TipoSacado.FUNCIONARIO_PROFESSOR.getValor())) {
            codigoPessoa = negociacaoPagamentoVO.getFuncionario().getPessoa().getCodigo();
        }else if (negociacaoPagamentoVO.getTipoSacado().equals(TipoSacado.ALUNO.getValor())) {
                codigoPessoa = negociacaoPagamentoVO.getAluno().getCodigo();
        }else if (negociacaoPagamentoVO.getTipoSacado().equals(TipoSacado.RESPONSAVEL_FINANCEIRO.getValor())) {
            codigoPessoa = negociacaoPagamentoVO.getResponsavelFinanceiro().getCodigo();
        } else if (negociacaoPagamentoVO.getTipoSacado().equals(TipoSacado.BANCO.getValor())) {
            codigoBanco = negociacaoPagamentoVO.getBanco().getCodigo();
        }else if (negociacaoPagamentoVO.getTipoSacado().equals(TipoSacado.PARCEIRO.getValor())) {
            codigoParceiro = negociacaoPagamentoVO.getParceiro().getCodigo();
        }else if (negociacaoPagamentoVO.getTipoSacado().equals(TipoSacado.OPERADORA_CARTAO.getValor())) {
        	codigoOperadoraCartao = negociacaoPagamentoVO.getOperadoraCartao().getCodigo();
        }
        if(tipoMovimentacao.equals("SA")){
            formaPagamentoNegociacaoPagamentoVO.getCheque().setSituacao(SituacaoCheque.PAGAMENTO.getValor());
        }else{
            formaPagamentoNegociacaoPagamentoVO.getCheque().setSituacao(SituacaoCheque.EM_CAIXA.getValor());
            //TODO
            //Alterar da conta Corrente para o Caixa
        }
        getFacadeFactory().getChequeFacade().alterar(formaPagamentoNegociacaoPagamentoVO.getCheque());
        getFacadeFactory().getFluxoCaixaFacade().criarMovimentacaoCaixa(formaPagamentoNegociacaoPagamentoVO.getValor(), formaPagamentoNegociacaoPagamentoVO.getContaCorrente().getCodigo(), tipoMovimentacao, "PA", formaPagamentoNegociacaoPagamentoVO.getFormaPagamento().getCodigo(), negociacaoPagamentoVO.getCodigo(), negociacaoPagamentoVO.getResponsavel().getCodigo(), codigoPessoa, codigoFornecedor, codigoBanco, codigoParceiro, formaPagamentoNegociacaoPagamentoVO.getCheque(), codigoOperadoraCartao, usuario);
    }

    /**
     * Operação responsável por consultar todos os <code>PagamentoVO</code> relacionados a um objeto da classe <code>financeiro.NegociacaoPagamento</code>.
     * @param negociacaoContaPagar  Atributo de <code>financeiro.NegociacaoPagamento</code> a ser utilizado para localizar os objetos da classe <code>PagamentoVO</code>.
     * @return List  Contendo todos os objetos da classe <code>PagamentoVO</code> resultantes da consulta.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public static List consultarFormaPagamentoNegociacaoPagamento(Integer negociacaoContaPagar, int nivelMontarDados, UsuarioVO usuario) throws Exception {

        List objetos = new ArrayList(0);
        String sql = "SELECT * FROM FormaPagamentoNegociacaoPagamento WHERE negociacaoContaPagar = ?";
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{negociacaoContaPagar});
        while (resultado.next()) {
            FormaPagamentoNegociacaoPagamentoVO novoObj = new FormaPagamentoNegociacaoPagamentoVO();
            novoObj = FormaPagamentoNegociacaoPagamento.montarDados(resultado, nivelMontarDados, usuario);
            objetos.add(novoObj);
        }
        return objetos;
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>PagamentoVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public FormaPagamentoNegociacaoPagamentoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM FormaPagamentoNegociacaoPagamento WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( Pagamento ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }
    
    
    @Override
    public FormaPagamentoNegociacaoPagamentoVO consultarFormaPagamentoNegociacaoPagamentoUsadaNaNegociacaoPagamentoPorContaPagar(Integer contaPagar, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	ControleAcesso.consultar(getIdEntidade(), false, usuario);
    	StringBuilder sb = new StringBuilder();
    	sb.append(" select formapagamentonegociacaopagamento.*  ");
    	sb.append(" from formapagamentonegociacaopagamento ");
    	sb.append(" inner join contapagarpagamento on contapagarpagamento.formapagamentonegociacaopagamento = formapagamentonegociacaopagamento.codigo ");
    	sb.append(" where contapagarpagamento.contapagar = ? ");
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), contaPagar);
    	if (!tabelaResultado.next()) {
    		throw new ConsistirException("Dados Não Encontrados ( Pagamento ).");
    	}
    	return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return FormaPagamentoNegociacaoPagamento.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
    	FormaPagamentoNegociacaoPagamento.idEntidade = idEntidade;
    }
    
    @Override
	public List<ContaCorrenteVO> separarChequesPorContaCorrente(List<FormaPagamentoNegociacaoPagamentoVO> formaPagamento, UsuarioVO usuario, NegociacaoPagamentoVO negociacaoPagamentoVO) throws Exception {
		List<ContaCorrenteVO> contasCorrente = new ArrayList<ContaCorrenteVO>();
		Extenso ext = new Extenso();
		negociacaoPagamentoVO.setUnidadeEnsino(getAplicacaoControle().getUnidadeEnsinoVO(negociacaoPagamentoVO.getUnidadeEnsino().getCodigo(), usuario));		
		
    	Map<Integer, ContaCorrenteVO> mapContaCorrente = new HashMap<Integer, ContaCorrenteVO>(0);
    	for(FormaPagamentoNegociacaoPagamentoVO forma : formaPagamento){
    		if(forma.getCheque() != null  && forma.getCheque().getValor() > 0 && forma.getCheque().getChequeProprio()){
    			if(!mapContaCorrente.containsKey(forma.getCheque().getContaCorrente().getCodigo())){
    				AgenciaVO a = getFacadeFactory().getAgenciaFacade().consultarPorChavePrimaria(forma.getCheque().getContaCorrente().getAgencia().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
    				forma.getCheque().getContaCorrente().setAgencia(a);
    				forma.getCheque().setUnidadeEnsino(negociacaoPagamentoVO.getUnidadeEnsino());
    				mapContaCorrente.put(forma.getCheque().getContaCorrente().getCodigo(), forma.getCheque().getContaCorrente());
    				
    			}
    		    ext.setNumber(forma.getCheque().getValor());
    		    forma.getCheque().setValorPorExtenso(ext.toString());
    		    mapContaCorrente.get(forma.getCheque().getContaCorrente().getCodigo()).getCheques().add(forma.getCheque());
    		}
    	}
    	
    	contasCorrente.addAll(mapContaCorrente.values());
	
		return contasCorrente;
	}
}
