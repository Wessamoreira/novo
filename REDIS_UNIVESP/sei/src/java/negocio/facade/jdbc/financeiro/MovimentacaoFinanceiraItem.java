package negocio.facade.jdbc.financeiro;

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

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.financeiro.ChequeVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.DevolucaoChequeVO;
import negocio.comuns.financeiro.MapaLancamentoFuturoVO;
import negocio.comuns.financeiro.MovimentacaoFinanceiraItemVO;
import negocio.comuns.financeiro.MovimentacaoFinanceiraVO;
import negocio.comuns.financeiro.enumerador.OrigemExtratoContaCorrenteEnum;
import negocio.comuns.financeiro.enumerador.TipoSacadoExtratoContaCorrenteEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.OrigemMapaLancamentoFuturo;
import negocio.comuns.utilitarias.dominios.SituacaoCheque;
import negocio.comuns.utilitarias.dominios.TipoFormaPagamento;
import negocio.comuns.utilitarias.dominios.TipoMapaLancamentoFuturo;
import negocio.comuns.utilitarias.dominios.TipoMovimentacaoFinanceira;
import negocio.comuns.utilitarias.dominios.TipoOrigemMovimentacaoCaixa;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.MovimentacaoFinanceiraItemInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>MovimentacaoFinanceiraItemVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>MovimentacaoFinanceiraItemVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see MovimentacaoFinanceiraItemVO
 * @see ControleAcesso
 * @see MovimentacaoFinanceira
 */
@Repository
@Scope("singleton")
@Lazy
public class MovimentacaoFinanceiraItem extends ControleAcesso implements MovimentacaoFinanceiraItemInterfaceFacade {

    protected static String idEntidade;

    public MovimentacaoFinanceiraItem() throws Exception {
        super();
        setIdEntidade("MovimentacaoFinanceira");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>MovimentacaoFinanceiraItemVO</code>.
     */
    public MovimentacaoFinanceiraItemVO novo() throws Exception {
        MovimentacaoFinanceiraItem.incluir(getIdEntidade());
        MovimentacaoFinanceiraItemVO obj = new MovimentacaoFinanceiraItemVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>MovimentacaoFinanceiraItemVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>MovimentacaoFinanceiraItemVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final MovimentacaoFinanceiraItemVO obj, UsuarioVO usuario) throws Exception {
        try {
            final String sql = "INSERT INTO MovimentacaoFinanceiraItem( cheque, movimentacaoFinanceira, valor, formaPagamento ) VALUES ( ?, ?, ?, ? ) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    if (obj.getCheque().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(1, obj.getCheque().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(1, 0);
                    }
                    if (obj.getMovimentacaoFinanceira().intValue() != 0) {
                        sqlInserir.setInt(2, obj.getMovimentacaoFinanceira().intValue());
                    } else {
                        sqlInserir.setNull(2, 0);
                    }
                    sqlInserir.setDouble(3, obj.getValor().doubleValue());
                    if (obj.getFormaPagamento().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(4, obj.getFormaPagamento().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(4, 0);
                    }
                    return sqlInserir;
                }
            }, new ResultSetExtractor<Object>() {

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
            obj.setNovoObj(true);
            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>MovimentacaoFinanceiraItemVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>MovimentacaoFinanceiraItemVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final MovimentacaoFinanceiraItemVO obj, UsuarioVO usuario) throws Exception {
        try {

            final String sql = "UPDATE MovimentacaoFinanceiraItem set cheque=?, movimentacaoFinanceira=?, valor=?, formaPagamento=? WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    if (obj.getCheque().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(1, obj.getCheque().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(1, 0);
                    }
                    if (obj.getMovimentacaoFinanceira().intValue() != 0) {
                        sqlAlterar.setInt(2, obj.getMovimentacaoFinanceira().intValue());
                    } else {
                        sqlAlterar.setNull(2, 0);
                    }
                    sqlAlterar.setDouble(3, obj.getValor().doubleValue());
                    if (obj.getFormaPagamento().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(4, obj.getFormaPagamento().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(4, 0);
                    }
                    sqlAlterar.setInt(4, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>MovimentacaoFinanceiraItemVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>MovimentacaoFinanceiraItemVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(MovimentacaoFinanceiraItemVO obj, UsuarioVO usuario) throws Exception {
        try {
            String sql = "DELETE FROM MovimentacaoFinanceiraItem WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>MovimentacaoFinanceiraItem</code> através do valor do atributo 
     * <code>codigo</code> da classe <code>MovimentacaoFinanceira</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>MovimentacaoFinanceiraItemVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigoMovimentacaoFinanceira(Integer valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        String sqlStr = "SELECT MovimentacaoFinanceiraItem.* FROM MovimentacaoFinanceiraItem, MovimentacaoFinanceira WHERE MovimentacaoFinanceiraItem.movimentacaoFinanceira = MovimentacaoFinanceira.codigo and MovimentacaoFinanceira.codigo >= " + valorConsulta.intValue() + " ORDER BY MovimentacaoFinanceira.codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>MovimentacaoFinanceiraItem</code> através do valor do atributo 
     * <code>numero</code> da classe <code>ChqRLog</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>MovimentacaoFinanceiraItemVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNumeroCheque(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        String sqlStr = "SELECT MovimentacaoFinanceiraItem.* FROM MovimentacaoFinanceiraItem, Cheque WHERE MovimentacaoFinanceiraItem.cheque = Cheque.codigo and upper( Cheque.numero ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY Cheque.numero";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>MovimentacaoFinanceiraItem</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>MovimentacaoFinanceiraItemVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        String sqlStr = "SELECT * FROM MovimentacaoFinanceiraItem WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>MovimentacaoFinanceiraItemVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>MovimentacaoFinanceiraItemVO</code>.
     * @return  O objeto da classe <code>MovimentacaoFinanceiraItemVO</code> com os dados devidamente montados.
     */
    public static MovimentacaoFinanceiraItemVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        MovimentacaoFinanceiraItemVO obj = new MovimentacaoFinanceiraItemVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.getCheque().setCodigo(new Integer(dadosSQL.getInt("cheque")));
        obj.setMovimentacaoFinanceira(new Integer(dadosSQL.getInt("movimentacaoFinanceira")));
        obj.getFormaPagamento().setCodigo(new Integer(dadosSQL.getInt("formaPagamento")));
        obj.setValor(new Double(dadosSQL.getDouble("valor")));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }

        montarDadosCheque(obj, nivelMontarDados, usuario);
        montarDadosFormaPagamento(obj, nivelMontarDados, usuario);
        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>ChqRLogVO</code> relacionado ao objeto <code>MovimentacaoFinanceiraItemVO</code>.
     * Faz uso da chave primária da classe <code>ChqRLogVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosCheque(MovimentacaoFinanceiraItemVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getCheque().getCodigo().intValue() == 0) {
            obj.setCheque(new ChequeVO());
            return;
        }
        obj.setCheque(getFacadeFactory().getChequeFacade().consultarPorChavePrimaria(obj.getCheque().getCodigo(), false, nivelMontarDados, usuario));
    }

    public static void montarDadosFormaPagamento(MovimentacaoFinanceiraItemVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getFormaPagamento().getCodigo().intValue() == 0) {
            obj.setFormaPagamento(new FormaPagamentoVO());
            return;
        }
        obj.setFormaPagamento(getFacadeFactory().getFormaPagamentoFacade().consultarPorChavePrimaria(obj.getFormaPagamento().getCodigo(), false, nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por excluir todos os objetos da <code>MovimentacaoFinanceiraItemVO</code> no BD.
     * Faz uso da operação <code>excluir</code> disponível na classe <code>MovimentacaoFinanceiraItem</code>.
     * @param <code>movimentacaoCaixa</code> campo chave para exclusão dos objetos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirMovimentacaoFinanceiraItems(Integer movimentacaoCaixa, UsuarioVO usuario) throws Exception {
        try {
            String sql = "DELETE FROM MovimentacaoFinanceiraItem WHERE (movimentacaoFinanceira = ?)"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sql, new Object[]{movimentacaoCaixa.intValue()});
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por alterar todos os objetos da <code>MovimentacaoFinanceiraItemVO</code> contidos em um Hashtable no BD.
     * Faz uso da operação <code>excluirMovimentacaoFinanceiraItems</code> e <code>incluirMovimentacaoFinanceiraItems</code> disponíveis na classe <code>MovimentacaoFinanceiraItem</code>.
     * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarMovimentacaoFinanceiraItems(MovimentacaoFinanceiraVO obj, UsuarioVO usuario) throws Exception {
        excluirMovimentacaoFinanceiraItems(obj.getCodigo(), usuario);
        incluirMovimentacaoFinanceiraItems(obj, usuario);
    }

    /**
     * Operação responsável por incluir objetos da <code>MovimentacaoFinanceiraItemVO</code> no BD.
     * Garantindo o relacionamento com a entidade principal <code>financeiro.MovimentacaoFinanceira</code> através do atributo de vínculo.
     * @param objetos List contendo os objetos a serem gravados no BD da classe.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirMovimentacaoFinanceiraItems(MovimentacaoFinanceiraVO movimentacaoFinanceira, UsuarioVO usuario) throws Exception {

        Iterator<MovimentacaoFinanceiraItemVO> e = movimentacaoFinanceira.getMovimentacaoFinanceiraItemVOs().iterator();
        while (e.hasNext()) {
            movimentacaoFinanceira.setContaCorrenteDestino(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(movimentacaoFinanceira.getContaCorrenteDestino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, usuario));
            if (movimentacaoFinanceira.isSomenteContaDestino() == false) {
                movimentacaoFinanceira.setContaCorrenteOrigem(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(movimentacaoFinanceira.getContaCorrenteOrigem().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, usuario));
            }
            MovimentacaoFinanceiraItemVO movimentacaoFinanceiraItemVO = e.next();
            movimentacaoFinanceiraItemVO.setMovimentacaoFinanceira(movimentacaoFinanceira.getCodigo());
            incluir(movimentacaoFinanceiraItemVO, usuario);
            
            criarMovimentacaoCaixaAlterandoSaldoConta(movimentacaoFinanceira, movimentacaoFinanceiraItemVO, movimentacaoFinanceira.getContaCorrenteDestino().getRequerConfirmacaoMovimentacaoFinanceira(), usuario);
//            if (movimentacaoFinanceira.isSomenteContaDestino() == false) {
//                criarMovimentacaoConta(movimentacaoFinanceiraItemVO, movimentacaoFinanceira, movimentacaoFinanceira.getContaCorrenteOrigem(), TipoMovimentacaoFinanceira.SAIDA.getValor(), usuario);
//            }
//            if (movimentacaoFinanceira.isSomenteContaDestino() == false) {
//                 if (movimentacaoFinanceiraItemVO.getFormaPagamento().getTipo().equals(TipoFormaPagamento.DINHEIRO.getValor()) && (movimentacaoFinanceira.getContaCorrenteDestino().getContaCaixa() && movimentacaoFinanceira.getContaCorrenteOrigem().getContaCaixa())) {
//                    criarMovimentacaoConta(movimentacaoFinanceiraItemVO, movimentacaoFinanceira, movimentacaoFinanceira.getContaCorrenteDestino(), TipoMovimentacaoFinanceira.ENTRADA.getValor(), usuario);
//                }else if(movimentacaoFinanceiraItemVO.getFormaPagamento().getTipo().equals(TipoFormaPagamento.DINHEIRO.getValor()) && (movimentacaoFinanceira.getContaCorrenteDestino().getContaCaixa() && !movimentacaoFinanceira.getContaCorrenteOrigem().getContaCaixa())){
//                    criarMovimentacaoConta(movimentacaoFinanceiraItemVO, movimentacaoFinanceira, movimentacaoFinanceira.getContaCorrenteDestino(), TipoMovimentacaoFinanceira.ENTRADA.getValor(), usuario);
//                }else if(movimentacaoFinanceiraItemVO.getFormaPagamento().getTipo().equals(TipoFormaPagamento.DINHEIRO.getValor()) && (!movimentacaoFinanceira.getContaCorrenteDestino().getContaCaixa() && movimentacaoFinanceira.getContaCorrenteOrigem().getContaCaixa())){
//                    criarMovimentacaoConta(movimentacaoFinanceiraItemVO, movimentacaoFinanceira, movimentacaoFinanceira.getContaCorrenteDestino(), TipoMovimentacaoFinanceira.ENTRADA.getValor(), usuario);
//                }else if(movimentacaoFinanceiraItemVO.getFormaPagamento().getTipo().equals(TipoFormaPagamento.DINHEIRO.getValor())){
//                    criarMovimentacaoConta(movimentacaoFinanceiraItemVO, movimentacaoFinanceira, movimentacaoFinanceira.getContaCorrenteDestino(), TipoMovimentacaoFinanceira.ENTRADA.getValor(), usuario);
//                }
//            } else {
//                 if (movimentacaoFinanceiraItemVO.getFormaPagamento().getTipo().equals(TipoFormaPagamento.DINHEIRO.getValor()) && (movimentacaoFinanceira.getContaCorrenteDestino().getContaCaixa())) {
//                    criarMovimentacaoConta(movimentacaoFinanceiraItemVO, movimentacaoFinanceira, movimentacaoFinanceira.getContaCorrenteDestino(), TipoMovimentacaoFinanceira.ENTRADA.getValor(),usuario);
//                }else if (movimentacaoFinanceiraItemVO.getFormaPagamento().getTipo().equals(TipoFormaPagamento.DINHEIRO.getValor()) && (!movimentacaoFinanceira.getContaCorrenteDestino().getContaCaixa())) {
//                    criarMovimentacaoConta(movimentacaoFinanceiraItemVO, movimentacaoFinanceira, movimentacaoFinanceira.getContaCorrenteDestino(), TipoMovimentacaoFinanceira.ENTRADA.getValor(),usuario);
//                }
//            }
//            if (movimentacaoFinanceiraItemVO.getFormaPagamento().getTipo().equals(TipoFormaPagamento.CHEQUE.getValor()) && movimentacaoFinanceiraItemVO.getCheque().getCodigo().intValue() != 0) {
//                if (!movimentacaoFinanceira.getContaCorrenteDestino().getContaCaixa()) {
//                    criarPendenciaChequeAReceber(movimentacaoFinanceiraItemVO, movimentacaoFinanceira,usuario);
//                } else {
//                    criarMovimentacaoConta(movimentacaoFinanceiraItemVO, movimentacaoFinanceira, movimentacaoFinanceira.getContaCorrenteDestino(), TipoMovimentacaoFinanceira.ENTRADA.getValor(), usuario);
//                }
//                alterarSituacaoCheque(movimentacaoFinanceiraItemVO.getCheque(), movimentacaoFinanceira.getContaCorrenteDestino().getCodigo(), movimentacaoFinanceira.getContaCorrenteDestino().getContaCaixa(),usuario);
//            }
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void criarMovimentacaoCaixaAlterandoSaldoConta(MovimentacaoFinanceiraVO movimentacaoFinanceira, MovimentacaoFinanceiraItemVO movimentacaoFinanceiraItemVO, Boolean requerConfirmacaoMovimentacaoFinanceira, UsuarioVO usuario) throws Exception {
        
    	if (!requerConfirmacaoMovimentacaoFinanceira || movimentacaoFinanceira.isPularMapaPendenciaMovimentacaoFinanceira()) {
            if(!movimentacaoFinanceira.isDesconsiderandoContabilidadeConciliacao()) {
            	criarMovimentacaoCaixaContaCorrente(movimentacaoFinanceira, movimentacaoFinanceiraItemVO, usuario);
            }
    		
            if (movimentacaoFinanceira.isSomenteContaDestino() == false) {
                criarMovimentacaoConta(movimentacaoFinanceiraItemVO, movimentacaoFinanceira, movimentacaoFinanceira.getContaCorrenteOrigem(), TipoMovimentacaoFinanceira.SAIDA.getValor(), usuario);
            }
            if (movimentacaoFinanceira.isSomenteContaDestino() == false) {
                if (movimentacaoFinanceiraItemVO.getFormaPagamento().getTipo().equals(TipoFormaPagamento.DINHEIRO.getValor()) && (movimentacaoFinanceira.getContaCorrenteDestino().getContaCaixa() && movimentacaoFinanceira.getContaCorrenteOrigem().getContaCaixa())) {                    
                    criarMovimentacaoConta(movimentacaoFinanceiraItemVO, movimentacaoFinanceira, movimentacaoFinanceira.getContaCorrenteDestino(), TipoMovimentacaoFinanceira.ENTRADA.getValor(), usuario);
                } else if (movimentacaoFinanceiraItemVO.getFormaPagamento().getTipo().equals(TipoFormaPagamento.DINHEIRO.getValor()) && (movimentacaoFinanceira.getContaCorrenteDestino().getContaCaixa() && !movimentacaoFinanceira.getContaCorrenteOrigem().getContaCaixa())) {
                    criarMovimentacaoConta(movimentacaoFinanceiraItemVO, movimentacaoFinanceira, movimentacaoFinanceira.getContaCorrenteDestino(), TipoMovimentacaoFinanceira.ENTRADA.getValor(), usuario);
                    
                } else if (movimentacaoFinanceiraItemVO.getFormaPagamento().getTipo().equals(TipoFormaPagamento.DINHEIRO.getValor()) && (!movimentacaoFinanceira.getContaCorrenteDestino().getContaCaixa() && movimentacaoFinanceira.getContaCorrenteOrigem().getContaCaixa())) {
                    criarMovimentacaoConta(movimentacaoFinanceiraItemVO, movimentacaoFinanceira, movimentacaoFinanceira.getContaCorrenteDestino(), TipoMovimentacaoFinanceira.ENTRADA.getValor(), usuario);
                } else if (movimentacaoFinanceiraItemVO.getFormaPagamento().getTipo().equals(TipoFormaPagamento.DINHEIRO.getValor())) {
                    criarMovimentacaoConta(movimentacaoFinanceiraItemVO, movimentacaoFinanceira, movimentacaoFinanceira.getContaCorrenteDestino(), TipoMovimentacaoFinanceira.ENTRADA.getValor(), usuario);
                }
            } else {
                if (movimentacaoFinanceiraItemVO.getFormaPagamento().getTipo().equals(TipoFormaPagamento.DINHEIRO.getValor()) && (movimentacaoFinanceira.getContaCorrenteDestino().getContaCaixa())) {
                    criarMovimentacaoConta(movimentacaoFinanceiraItemVO, movimentacaoFinanceira, movimentacaoFinanceira.getContaCorrenteDestino(), TipoMovimentacaoFinanceira.ENTRADA.getValor(), usuario);
                } else if (movimentacaoFinanceiraItemVO.getFormaPagamento().getTipo().equals(TipoFormaPagamento.DINHEIRO.getValor()) && (!movimentacaoFinanceira.getContaCorrenteDestino().getContaCaixa())) {
                    criarMovimentacaoConta(movimentacaoFinanceiraItemVO, movimentacaoFinanceira, movimentacaoFinanceira.getContaCorrenteDestino(), TipoMovimentacaoFinanceira.ENTRADA.getValor(), usuario);
                }
            }
            if (movimentacaoFinanceiraItemVO.getFormaPagamento().getTipo().equals(TipoFormaPagamento.CHEQUE.getValor()) && movimentacaoFinanceiraItemVO.getCheque().getCodigo().intValue() != 0) {
                if (!movimentacaoFinanceira.getContaCorrenteDestino().getContaCaixa()) {
                	excluirPendenciaChequeDevolvido(movimentacaoFinanceiraItemVO, usuario);
                    criarPendenciaChequeAReceber(movimentacaoFinanceiraItemVO, movimentacaoFinanceira, usuario);
                    criarExtratoMapaLancamentoFuturoChequeAReceber(movimentacaoFinanceiraItemVO, movimentacaoFinanceira, usuario);
                } else {
                    criarMovimentacaoConta(movimentacaoFinanceiraItemVO, movimentacaoFinanceira, movimentacaoFinanceira.getContaCorrenteDestino(), TipoMovimentacaoFinanceira.ENTRADA.getValor(), usuario);
                }
                alterarSituacaoCheque(movimentacaoFinanceiraItemVO.getCheque(), movimentacaoFinanceira.getContaCorrenteDestino().getCodigo(), movimentacaoFinanceira.getContaCorrenteDestino().getContaCaixa(), usuario);
            }
        }
    }
    
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirPendenciaChequeDevolvido(MovimentacaoFinanceiraItemVO movimentacaoFinanceiraItemVO, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getMapaLancamentoFuturoFacade().excluirPorCodigoChequeTipoOrigemETipoMapalancamentoFuturo(movimentacaoFinanceiraItemVO.getCheque().getCodigo(), "DC", "CD", false, usuario);
		DevolucaoChequeVO devCheque = getFacadeFactory().getDevolucaoChequeFacade().consultarPorCodigoCheque(movimentacaoFinanceiraItemVO.getCheque().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		getFacadeFactory().getContaReceberFacade().excluirPorChequeDevolvido(devCheque.getCodigo(), usuario);
	}
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void removerMovimentacaoCaixaAlterandoSaldoConta(MovimentacaoFinanceiraVO movimentacaoFinanceira, MovimentacaoFinanceiraItemVO movimentacaoFinanceiraItemVO, UsuarioVO usuario) throws Exception {
    	if(!movimentacaoFinanceira.isDesconsiderandoContabilidadeConciliacao()) {
    		criarEstornoMovimentacaoCaixaContaCorrente(movimentacaoFinanceira, movimentacaoFinanceiraItemVO, usuario);
    	}
    	if (movimentacaoFinanceiraItemVO.getFormaPagamento().getTipo().equals(TipoFormaPagamento.DINHEIRO.getValor())) {
        	if (movimentacaoFinanceira.isSomenteContaDestino() == false && movimentacaoFinanceira.getContaCorrenteOrigem().getCodigo() != 0) {
        		getFacadeFactory().getContaCorrenteFacade().movimentarSaldoContaCorrente(movimentacaoFinanceira.getContaCorrenteOrigem().getCodigo(), TipoMovimentacaoFinanceira.ENTRADA.getValor(), movimentacaoFinanceiraItemVO.getValor(), usuario);
        	}
        	getFacadeFactory().getContaCorrenteFacade().movimentarSaldoContaCorrente(movimentacaoFinanceira.getContaCorrenteDestino().getCodigo(), TipoMovimentacaoFinanceira.SAIDA.getValor(), movimentacaoFinanceiraItemVO.getValor(), usuario);
        }
        if (movimentacaoFinanceiraItemVO.getFormaPagamento().getTipo().equals(TipoFormaPagamento.CHEQUE.getValor()) && movimentacaoFinanceiraItemVO.getCheque().getCodigo().intValue() != 0) {
        	if (!movimentacaoFinanceira.getContaCorrenteDestino().getContaCaixa()) {
        		getFacadeFactory().getMapaLancamentoFuturoFacade().excluirPorCodigoCheque(movimentacaoFinanceiraItemVO.getCheque().getCodigo(), false, usuario);
        		getFacadeFactory().getExtratoMapaLancamentoFuturoFacade().excluirPorCodigoCheque(movimentacaoFinanceiraItemVO.getCheque().getCodigo(), false, usuario);
            } else {
            	getFacadeFactory().getContaCorrenteFacade().movimentarSaldoContaCorrente(movimentacaoFinanceira.getContaCorrenteDestino().getCodigo(), TipoMovimentacaoFinanceira.SAIDA.getValor(), movimentacaoFinanceiraItemVO.getValor(), usuario);
            }
            alterarSituacaoCheque(movimentacaoFinanceiraItemVO.getCheque(), movimentacaoFinanceira.getContaCorrenteOrigem().getCodigo(), movimentacaoFinanceira.getContaCorrenteOrigem().getContaCaixa(), usuario);
            getFacadeFactory().getContaCorrenteFacade().movimentarSaldoContaCorrente(movimentacaoFinanceira.getContaCorrenteOrigem().getCodigo(), TipoMovimentacaoFinanceira.ENTRADA.getValor(), movimentacaoFinanceiraItemVO.getValor(), usuario);
        }
    
    }
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void criarEstornoMovimentacaoCaixaContaCorrente(MovimentacaoFinanceiraVO movimentacaoFinanceira, MovimentacaoFinanceiraItemVO movimentacaoFinanceiraItemVO, UsuarioVO usuario) throws Exception {
                
        if ((movimentacaoFinanceiraItemVO.getFormaPagamento().getTipo().equals(TipoFormaPagamento.DINHEIRO.getValor()))
                && movimentacaoFinanceira.getContaCorrenteOrigem().getCodigo()>0 && !movimentacaoFinanceira.getContaCorrenteOrigem().getContaCaixa()) {
            getFacadeFactory().getExtratoContaCorrenteFacade().executarCriacaoExtratoContaCorrente(movimentacaoFinanceiraItemVO.getValor(), movimentacaoFinanceira.getData(),
                    OrigemExtratoContaCorrenteEnum.MOVIMENTACAO_FINANCEIRA, TipoMovimentacaoFinanceira.ENTRADA, movimentacaoFinanceira.getCodigo(), 
                    null, "Saque/Transferência", movimentacaoFinanceira.getContaCorrenteOrigem().getCodigo(), TipoSacadoExtratoContaCorrenteEnum.CONTA_CORRENTE,
                    null, movimentacaoFinanceiraItemVO.getFormaPagamento(), movimentacaoFinanceira.getContaCorrenteOrigem(),
                    null, null,  movimentacaoFinanceira.isDesconsiderarConciliacaoBancaria(), null, movimentacaoFinanceira.getBloqueioPorFechamentoMesLiberado(), usuario);
        }
        if ((movimentacaoFinanceiraItemVO.getFormaPagamento().getTipo().equals(TipoFormaPagamento.DINHEIRO.getValor()))
                && movimentacaoFinanceira.getContaCorrenteDestino().getCodigo()>0 && !movimentacaoFinanceira.getContaCorrenteDestino().getContaCaixa()) {
            getFacadeFactory().getExtratoContaCorrenteFacade().executarCriacaoExtratoContaCorrente(movimentacaoFinanceiraItemVO.getValor(), movimentacaoFinanceira.getData(),
                    OrigemExtratoContaCorrenteEnum.MOVIMENTACAO_FINANCEIRA, TipoMovimentacaoFinanceira.SAIDA, movimentacaoFinanceira.getCodigo(), 
                    null, "Depósito/Transferência", movimentacaoFinanceira.getContaCorrenteDestino().getCodigo(), TipoSacadoExtratoContaCorrenteEnum.CONTA_CORRENTE,
                    null, movimentacaoFinanceiraItemVO.getFormaPagamento(), movimentacaoFinanceira.getContaCorrenteDestino(),
                    null, null,  movimentacaoFinanceira.isDesconsiderarConciliacaoBancaria(), null, movimentacaoFinanceira.getBloqueioPorFechamentoMesLiberado(), usuario);
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void criarMovimentacaoCaixaContaCorrente(MovimentacaoFinanceiraVO movimentacaoFinanceira, MovimentacaoFinanceiraItemVO movimentacaoFinanceiraItemVO, UsuarioVO usuario) throws Exception {
    	
    	if ((movimentacaoFinanceiraItemVO.getFormaPagamento().getTipo().equals(TipoFormaPagamento.DINHEIRO.getValor()))
    			&& movimentacaoFinanceira.getContaCorrenteOrigem().getCodigo()>0 && !movimentacaoFinanceira.getContaCorrenteOrigem().getContaCaixa()) {
    		getFacadeFactory().getExtratoContaCorrenteFacade().executarCriacaoExtratoContaCorrente(movimentacaoFinanceiraItemVO.getValor(), movimentacaoFinanceira.getData(),
    				OrigemExtratoContaCorrenteEnum.MOVIMENTACAO_FINANCEIRA, TipoMovimentacaoFinanceira.SAIDA, movimentacaoFinanceira.getCodigo(), 
    				null, "Saque/Transferência", movimentacaoFinanceira.getContaCorrenteOrigem().getCodigo(), TipoSacadoExtratoContaCorrenteEnum.CONTA_CORRENTE,
    				null, movimentacaoFinanceiraItemVO.getFormaPagamento(), movimentacaoFinanceira.getContaCorrenteOrigem(),
    				null, null, false, null, movimentacaoFinanceira.getBloqueioPorFechamentoMesLiberado(), usuario);
    	}
    	if ((movimentacaoFinanceiraItemVO.getFormaPagamento().getTipo().equals(TipoFormaPagamento.DINHEIRO.getValor()))
    			&& movimentacaoFinanceira.getContaCorrenteDestino().getCodigo()>0 && !movimentacaoFinanceira.getContaCorrenteDestino().getContaCaixa()) {
    		getFacadeFactory().getExtratoContaCorrenteFacade().executarCriacaoExtratoContaCorrente(movimentacaoFinanceiraItemVO.getValor(), movimentacaoFinanceira.getData(),
    				OrigemExtratoContaCorrenteEnum.MOVIMENTACAO_FINANCEIRA, TipoMovimentacaoFinanceira.ENTRADA, movimentacaoFinanceira.getCodigo(), 
    				null, "Depósito/Transferência", movimentacaoFinanceira.getContaCorrenteDestino().getCodigo(), TipoSacadoExtratoContaCorrenteEnum.CONTA_CORRENTE,
    				null, movimentacaoFinanceiraItemVO.getFormaPagamento(), movimentacaoFinanceira.getContaCorrenteDestino(),
    				null, null, false, null, movimentacaoFinanceira.getBloqueioPorFechamentoMesLiberado(), usuario);
    	}
    	if ((movimentacaoFinanceiraItemVO.getFormaPagamento().getTipo().equals(TipoFormaPagamento.CHEQUE.getValor()))
    			&& movimentacaoFinanceira.getContaCorrenteOrigem().getCodigo()>0 && !movimentacaoFinanceira.getContaCorrenteOrigem().getContaCaixa()) {
    		getFacadeFactory().getExtratoContaCorrenteFacade().executarCriacaoExtratoContaCorrente(movimentacaoFinanceiraItemVO.getValor(), movimentacaoFinanceira.getData(),
    				OrigemExtratoContaCorrenteEnum.MOVIMENTACAO_FINANCEIRA, TipoMovimentacaoFinanceira.SAIDA, movimentacaoFinanceira.getCodigo(), 
    				movimentacaoFinanceiraItemVO.getCheque(), "Saque/Transferência", movimentacaoFinanceira.getContaCorrenteOrigem().getCodigo(), TipoSacadoExtratoContaCorrenteEnum.CONTA_CORRENTE,
    				null, movimentacaoFinanceiraItemVO.getFormaPagamento(), movimentacaoFinanceira.getContaCorrenteOrigem(),
    				null, null, false, null, movimentacaoFinanceira.getBloqueioPorFechamentoMesLiberado(), usuario);
    	}
//    	if ((movimentacaoFinanceiraItemVO.getFormaPagamento().getTipo().equals(TipoFormaPagamento.CHEQUE.getValor()))
//    			&& movimentacaoFinanceira.getContaCorrenteDestino().getCodigo()>0 && !movimentacaoFinanceira.getContaCorrenteDestino().getContaCaixa()) {
//    		getFacadeFactory().getExtratoContaCorrenteFacade().executarCriacaoExtratoContaCorrente(movimentacaoFinanceiraItemVO.getValor(), movimentacaoFinanceira.getData(),
//    				OrigemExtratoContaCorrenteEnum.MOVIMENTACAO_FINANCEIRA, TipoMovimentacaoFinanceira.ENTRADA, movimentacaoFinanceira.getCodigo(), 
//    				movimentacaoFinanceiraItemVO.getCheque(), "Depósito/Transferência", movimentacaoFinanceira.getContaCorrenteDestino().getCodigo(), TipoSacadoExtratoContaCorrenteEnum.CONTA_CORRENTE,
//    				movimentacaoFinanceiraItemVO.getFormaPagamento(), movimentacaoFinanceira.getContaCorrenteDestino(),
//    				null, usuario);
//    		
//    	}
    }
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void criarExtratoMapaLancamentoFuturoChequeAReceber(MovimentacaoFinanceiraItemVO movimentacaoFinanceiraItemVO, MovimentacaoFinanceiraVO movimentacaoFinanceiraVO, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getExtratoMapaLancamentoFuturoFacade().realizarCriacaoInclusaoExtratoMapaLancamentoFuturo(movimentacaoFinanceiraItemVO.getCheque(), movimentacaoFinanceiraVO.getData(), movimentacaoFinanceiraVO.getContaCorrenteDestino().getCodigo(), TipoMapaLancamentoFuturo.CHEQUE_A_RECEBER, usuario);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void criarPendenciaChequeAReceber(MovimentacaoFinanceiraItemVO movimentacaoFinanceiraItemVO, MovimentacaoFinanceiraVO movimentacaoFinanceiraVO, UsuarioVO usuario) throws Exception {
    	MapaLancamentoFuturoVO mapaLancamentoFuturoVO = movimentacaoFinanceiraItemVO.getCheque().criarMapaLancamentoFuturo(movimentacaoFinanceiraVO.getCodigo(), OrigemMapaLancamentoFuturo.MOVIMENTACAO_FINANCEIRA.getValor(), TipoMapaLancamentoFuturo.CHEQUE_A_RECEBER.getValor(), movimentacaoFinanceiraVO.getResponsavel());
    	getFacadeFactory().getMapaLancamentoFuturoFacade().incluir(mapaLancamentoFuturoVO, usuario);
    }
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarSituacaoCheque(ChequeVO cheque, Integer contaCorrente, Boolean contaCaixa, UsuarioVO usuario) throws Exception {
        if (contaCaixa) {
            cheque.setSituacao(SituacaoCheque.EM_CAIXA.getValor());
        } else {
            cheque.setSituacao(SituacaoCheque.PENDENTE.getValor());
        }
        cheque.getLocalizacaoCheque().setCodigo(contaCorrente);
        getFacadeFactory().getChequeFacade().alterar(cheque);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void criarMovimentacaoConta(MovimentacaoFinanceiraItemVO obj, MovimentacaoFinanceiraVO movimentacaoCaixa, ContaCorrenteVO contaCorrente, String tipo, UsuarioVO usuario) throws Exception {

        if (contaCorrente.getContaCaixa()) {
            Integer pessoa = 0;
            if (obj.getFormaPagamento().getTipo().equals(TipoFormaPagamento.CHEQUE.getValor())) {
                pessoa = obj.getCheque().getPessoa().getCodigo();
            } else {
                pessoa = movimentacaoCaixa.getResponsavel().getPessoa().getCodigo();
            }
            getFacadeFactory().getFluxoCaixaFacade().criarMovimentacaoCaixa(obj.getValor(), contaCorrente.getCodigo(), tipo, TipoOrigemMovimentacaoCaixa.MOVIMENTACAO_FINANCEIRA.getValor(), obj.getFormaPagamento().getCodigo(), movimentacaoCaixa.getCodigo(), movimentacaoCaixa.getResponsavel().getCodigo(), pessoa, obj.getCheque().getFornecedor().getCodigo(), 0, obj.getCheque().getParceiro().getCodigo(), obj.getCheque(), 0, usuario);
            getFacadeFactory().getContaCorrenteFacade().movimentarSaldoContaCorrente(contaCorrente.getCodigo(), tipo, obj.getValor(), usuario);
        } else {
            getFacadeFactory().getContaCorrenteFacade().movimentarSaldoContaCorrente(contaCorrente.getCodigo(), tipo, obj.getValor(), usuario);
        }

    }

    /**
     * Operação responsável por consultar todos os <code>MovimentacaoFinanceiraItemVO</code> relacionados a um objeto da classe <code>financeiro.MovimentacaoFinanceira</code>.
     * @param movimentacaoCaixa  Atributo de <code>financeiro.MovimentacaoFinanceira</code> a ser utilizado para localizar os objetos da classe <code>MovimentacaoFinanceiraItemVO</code>.
     * @return List  Contendo todos os objetos da classe <code>MovimentacaoFinanceiraItemVO</code> resultantes da consulta.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public static List consultarMovimentacaoFinanceiraItems(Integer movimentacaoCaixa, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List objetos = new ArrayList(0);
        String sql = "SELECT * FROM MovimentacaoFinanceiraItem WHERE movimentacaoFinanceira = " + movimentacaoCaixa.intValue();
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
        while (resultado.next()) {
            MovimentacaoFinanceiraItemVO novoObj = new MovimentacaoFinanceiraItemVO();
            novoObj = MovimentacaoFinanceiraItem.montarDados(resultado, nivelMontarDados, usuario);
            objetos.add(novoObj);
        }
        return objetos;

    }

    /**
     * Operação responsável por localizar um objeto da classe <code>MovimentacaoFinanceiraItemVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public MovimentacaoFinanceiraItemVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM MovimentacaoFinanceiraItem WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( MovimentacaoFinanceiraItem ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>MovimentacaoFinanceiraItemVO</code>
     * através de sua chave primária.
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public List<MovimentacaoFinanceiraItemVO> consultarPorMovimentacaoFinanceiraMapaPendenciaMovimentacaoFinanceira(Integer codigoPrm, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        StringBuilder sqlStr = new StringBuilder("SELECT mfi.codigo as \"mficodigo\", mfi.valor as \"mfivalor\", mfi.movimentacaoFinanceira as \"mfiMovimentacaoFinanceira\", ");
        sqlStr.append("fp.codigo as \"fpcodigo\", fp.nome as \"fpnome\", fp.tipo as \"fptipo\", cheque.* ");
        sqlStr.append("FROM movimentacaoFinanceiraItem mfi ");
        sqlStr.append("LEFT JOIN formaPagamento fp on fp.codigo = mfi.formaPagamento ");
        sqlStr.append("LEFT JOIN cheque on cheque.codigo = mfi.cheque ");
        sqlStr.append("WHERE mfi.movimentacaoFinanceira = ? ");
        sqlStr.append("ORDER BY cheque.codigo");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[]{codigoPrm});
        
        return (montarDadosListaMapaPendenciaMovimentacaoFinanceiraItem(tabelaResultado, usuario));
    }

    public List<MovimentacaoFinanceiraItemVO> montarDadosListaMapaPendenciaMovimentacaoFinanceiraItem(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
        List<MovimentacaoFinanceiraItemVO> vetResultado = new ArrayList<MovimentacaoFinanceiraItemVO>(0);
        while (tabelaResultado.next()) {
            MovimentacaoFinanceiraItemVO obj = new MovimentacaoFinanceiraItemVO();
            montarDadosMapaPendenciaMovimentacaoFinanceiraItem(obj, tabelaResultado);
            vetResultado.add(obj);
        }
        return vetResultado;
    }

    /**
     * Monta os dados do Objeto MapaPendenciaCartaoCredito.
     */
    private void montarDadosMapaPendenciaMovimentacaoFinanceiraItem(MovimentacaoFinanceiraItemVO obj, SqlRowSet dadosSQL) throws Exception {
        // Dados do MapaPendenciaMovimentacaoFinanceiraVO
        obj.setCodigo(dadosSQL.getInt("mficodigo"));
        obj.setValor(dadosSQL.getDouble("mfivalor"));
        obj.setMovimentacaoFinanceira(dadosSQL.getInt("mfiMovimentacaoFinanceira"));
        obj.getFormaPagamento().setCodigo(dadosSQL.getInt("fpcodigo"));
        obj.getFormaPagamento().setNome(dadosSQL.getString("fpnome"));
        obj.getFormaPagamento().setTipo(dadosSQL.getString("fptipo"));
        obj.getCheque().setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.getCheque().getLocalizacaoCheque().setCodigo(new Integer(dadosSQL.getInt("localizacaocheque")));
        obj.getCheque().setChequeProprio(dadosSQL.getBoolean("chequeProprio"));
        obj.getCheque().getUnidadeEnsino().setCodigo(new Integer(dadosSQL.getInt("unidadeEnsino")));
        obj.getCheque().setValor(new Double(dadosSQL.getDouble("valor")));
        obj.getCheque().setPago(dadosSQL.getBoolean("pago"));
        obj.getCheque().setSituacao(dadosSQL.getString("situacao"));
        obj.getCheque().setRecebimento(new Integer(dadosSQL.getInt("recebimento")));
        obj.getCheque().setPagamento(new Integer(dadosSQL.getInt("pagamento")));
        obj.getCheque().setValorUsadoRecebimento(new Double(dadosSQL.getDouble("valorusadorecebimento")));
        obj.getCheque().getPessoa().setCodigo(new Integer(dadosSQL.getInt("pessoa")));
        obj.getCheque().getParceiro().setCodigo(new Integer(dadosSQL.getInt("parceiro")));
        obj.getCheque().setSacado(dadosSQL.getString("sacado"));
        obj.getCheque().getContaCorrente().setCodigo(new Integer(dadosSQL.getInt("contacorrente")));
        obj.getCheque().setNumero(dadosSQL.getString("numero"));
        obj.getCheque().setDataEmissao(dadosSQL.getDate("dataemissao"));
        obj.getCheque().setDataPrevisao(dadosSQL.getDate("dataPrevisao"));
        obj.getCheque().setBanco(dadosSQL.getString("banco"));
        obj.getCheque().setAgencia(dadosSQL.getString("agencia"));
        obj.getCheque().setNumeroContaCorrente(dadosSQL.getString("numerocontacorrente"));
        obj.getCheque().getFornecedor().setCodigo(dadosSQL.getInt("fornecedor"));
        obj.setNovoObj(Boolean.FALSE);
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return MovimentacaoFinanceiraItem.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        MovimentacaoFinanceiraItem.idEntidade = idEntidade;
    }
}
