package negocio.facade.jdbc.financeiro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaReceberNegociacaoRecebimentoVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.NegociacaoRecebimentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoContaReceber;
import negocio.comuns.utilitarias.dominios.TipoOrigemContaReceber;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.ContaReceberNegociacaoRecebimentoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>ContaReceberNegociacaoRecebimentoVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>ContaReceberNegociacaoRecebimentoVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see ContaReceberNegociacaoRecebimentoVO
 * @see ControleAcesso
 * @see NegociacaoRecebimento
 */
@Repository
@Scope("singleton")
@Lazy 
public class ContaReceberNegociacaoRecebimento extends ControleAcesso implements ContaReceberNegociacaoRecebimentoInterfaceFacade{

    protected static String idEntidade;

    public ContaReceberNegociacaoRecebimento() throws Exception {
        super();
        setIdEntidade("NegociacaoRecebimento");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>ContaReceberNegociacaoRecebimentoVO</code>.
     */
    public ContaReceberNegociacaoRecebimentoVO novo() throws Exception {
        ContaReceberNegociacaoRecebimento.incluir(getIdEntidade());
        ContaReceberNegociacaoRecebimentoVO obj = new ContaReceberNegociacaoRecebimentoVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>ContaReceberNegociacaoRecebimentoVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>ContaReceberNegociacaoRecebimentoVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void incluir(final ContaReceberNegociacaoRecebimentoVO obj,ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
        ContaReceberNegociacaoRecebimentoVO.validarDados(obj);        
        //ContaReceberNegociacaoRecebimento.incluir(getIdEntidade());
        obj.realizarUpperCaseDados();
        final String sql = "INSERT INTO ContaReceberNegociacaoRecebimento( negociacaoRecebimento, contaReceber, valorTotal, contaReceberTerceiro ) VALUES ( ?, ?, ?, ? ) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
                PreparedStatement sqlInserir = cnctn.prepareStatement(sql);
                if (obj.getNegociacaoRecebimento().intValue() != 0) {
                    sqlInserir.setInt(1, obj.getNegociacaoRecebimento().intValue());
                } else {
                    sqlInserir.setNull(1, 0);
                }
                if (obj.getContaReceber().getCodigo().intValue() != 0) {
                    sqlInserir.setInt(2, obj.getContaReceber().getCodigo().intValue());
                } else {
                    sqlInserir.setNull(2, 0);
                }
                sqlInserir.setDouble(3, obj.getValorTotal().doubleValue());
                sqlInserir.setBoolean(4, obj.getContaReceberTerceiro());
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

        if (!obj.getContaReceber().getSituacao().equals("AR")) {
        	if(obj.getBloqueioPorFechamentoMesLiberado()) {
        		obj.getContaReceber().liberarVerificacaoBloqueioFechamentoMes();	
        	}
        	// verifica se existe negativação vinculado a conta e estorna o recebimento caso exista, se o usario tiver permissão o sistema avança.
        	Boolean possuiPermissao = Boolean.FALSE;
        	try {
        		ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("RecebimentoRegistroNegativacaoCobranca", usuario);
        		possuiPermissao = Boolean.TRUE;
        	} catch (Exception e) {
        		possuiPermissao = Boolean.FALSE;
        	}
        	if (!possuiPermissao && getFacadeFactory().getRegistroNegativacaoCobrancaContaReceberFacade().consultarContaReceberNegativadaCobrancaContaReceber(obj.getContaReceber().getCodigo())) {
        		throw new Exception ("Não é possível realizar o recebimento de uma conta a receber que possua registro de negativação/cobrança ( Nosso número =  " + obj.getContaReceber().getNossoNumero() + " )!");
        	}
            getFacadeFactory().getContaReceberFacade().alterar(obj.getContaReceber(), true, configuracaoFinanceiro, usuario);
        }
        obj.setNovoObj(Boolean.FALSE);
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>ContaReceberNegociacaoRecebimentoVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>ContaReceberNegociacaoRecebimentoVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final ContaReceberNegociacaoRecebimentoVO obj,ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
        ContaReceberNegociacaoRecebimentoVO.validarDados(obj);
        ContaReceberNegociacaoRecebimento.alterar(getIdEntidade());
        obj.realizarUpperCaseDados();
        final String sql = "UPDATE ContaReceberNegociacaoRecebimento set negociacaoRecebimento=?, contaReceber=?, valorTotal=?, contaReceberTerceiro=? WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
                PreparedStatement sqlAlterar = cnctn.prepareStatement(sql);
                if (obj.getNegociacaoRecebimento().intValue() != 0) {
                    sqlAlterar.setInt(1, obj.getNegociacaoRecebimento().intValue());
                } else {
                    sqlAlterar.setNull(1, 0);
                }
                sqlAlterar.setInt(2, obj.getContaReceber().getCodigo().intValue());
                sqlAlterar.setDouble(3, obj.getValorTotal().doubleValue());
                sqlAlterar.setBoolean(4, obj.getContaReceberTerceiro());
                sqlAlterar.setInt(5, obj.getCodigo().intValue());
                return sqlAlterar;
            }
        });

        // TODO Alberto 07/12/10 corrigido para não gravar duas negociações recebimento com mesma contareceber
        if (!obj.getContaReceber().getSituacao().equals("AR")) {
        	if(obj.getBloqueioPorFechamentoMesLiberado()) {
        		obj.getContaReceber().liberarVerificacaoBloqueioFechamentoMes();	
        	}
            getFacadeFactory().getContaReceberFacade().alterar(obj.getContaReceber(), true, configuracaoFinanceiro, usuario);
        }
        // TODO Alberto 07/12/10 corrigido para não gravar duas negociações recebimento com mesma contareceber
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarSituacaoParaNotificacaoSerasa(final Boolean notificarSerasa, final Integer codigo, UsuarioVO usuario) throws Exception {
        final String sql = "UPDATE ContaReceberNegociacaoRecebimento set notificarSerasa=?, datanotificacaoserasa=? WHERE (codigo = ?)"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
                PreparedStatement sqlAlterar = cnctn.prepareStatement(sql);
                sqlAlterar.setBoolean(1, notificarSerasa);
                sqlAlterar.setTimestamp(2, Uteis.getDataJDBCTimestamp(new Date()));
                sqlAlterar.setInt(3, codigo.intValue());
                return sqlAlterar;
            }
        });
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarSituacaoTodasContaReceberParaNotificacaoSerasaPorMatricula(final Boolean notificarSerasa, final String matricula, UsuarioVO usuario) throws Exception {
        final String sql = "UPDATE ContaReceberNegociacaoRecebimento set notificarSerasa=?, datanotificacaoserasa=? WHERE codigo in (select contarecebernegociacaorecebimento.codigo from contarecebernegociacaorecebimento "
                + " inner join contareceber on contareceber.codigo = contarecebernegociacaorecebimento.contareceber "
                + " where contareceber.matriculaaluno = ?)"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
                PreparedStatement sqlAlterar = cnctn.prepareStatement(sql);
                sqlAlterar.setBoolean(1, notificarSerasa);
                sqlAlterar.setTimestamp(2, Uteis.getDataJDBCTimestamp(new Date()));
                sqlAlterar.setString(3, matricula);
                return sqlAlterar;
            }
        });
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>ContaReceberNegociacaoRecebimentoVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>ContaReceberNegociacaoRecebimentoVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(ContaReceberNegociacaoRecebimentoVO obj, UsuarioVO usuario) throws Exception {
        ContaReceberNegociacaoRecebimento.excluir(getIdEntidade());
        String sql = "DELETE FROM ContaReceberNegociacaoRecebimento WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
    }

    /**
     * Responsável por realizar uma consulta de <code>ContaReceberNegociacaoRecebimento</code> através do valor do atributo 
     * <code>Integer contaReceber</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>ContaReceberNegociacaoRecebimentoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorContaReceber(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM ContaReceberNegociacaoRecebimento WHERE contaReceber >= " + valorConsulta.intValue() + " ORDER BY contaReceber";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>ContaReceberNegociacaoRecebimento</code> através do valor do atributo 
     * <code>Double valorRecebido</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>ContaReceberNegociacaoRecebimentoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorValorRecebido(Double valorConsulta, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM ContaReceberNegociacaoRecebimento WHERE valorRecebido >= " + valorConsulta.doubleValue() + " ORDER BY valorRecebido";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>ContaReceberNegociacaoRecebimento</code> através do valor do atributo 
     * <code>Double valorContaReceber</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>ContaReceberNegociacaoRecebimentoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorValorContaReceber(Double valorConsulta, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM ContaReceberNegociacaoRecebimento WHERE valorContaReceber >= " + valorConsulta.doubleValue() + " ORDER BY valorContaReceber";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>ContaReceberNegociacaoRecebimento</code> através do valor do atributo 
     * <code>codigo</code> da classe <code>NegociacaoRecebimento</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>ContaReceberNegociacaoRecebimentoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<ContaReceberNegociacaoRecebimentoVO> consultarPorCodigoNegociacaoRecebimento(Integer valorConsulta, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), true, usuario);
        String sqlStr = "SELECT ContaReceberNegociacaoRecebimento.* FROM ContaReceberNegociacaoRecebimento, NegociacaoRecebimento WHERE ContaReceberNegociacaoRecebimento.negociacaoRecebimento = NegociacaoRecebimento.codigo and NegociacaoRecebimento.codigo >= " + valorConsulta.intValue() + " ORDER BY NegociacaoRecebimento.codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>ContaReceberNegociacaoRecebimento</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>ContaReceberNegociacaoRecebimentoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM ContaReceberNegociacaoRecebimento WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>ContaReceberNegociacaoRecebimentoVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario));
        }
        tabelaResultado = null;
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>ContaReceberNegociacaoRecebimentoVO</code>.
     * @return  O objeto da classe <code>ContaReceberNegociacaoRecebimentoVO</code> com os dados devidamente montados.
     */
    public static ContaReceberNegociacaoRecebimentoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
        ContaReceberNegociacaoRecebimentoVO obj = new ContaReceberNegociacaoRecebimentoVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setNegociacaoRecebimento(new Integer(dadosSQL.getInt("negociacaoRecebimento")));
        obj.setValorTotal(new Double(dadosSQL.getDouble("valorTotal")));
        obj.getContaReceber().setCodigo(new Integer(dadosSQL.getInt("contaReceber")));
        obj.setContaReceberTerceiro(dadosSQL.getBoolean("contaReceberTerceiro"));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        montarDadosContaReceber(obj, nivelMontarDados, configuracaoFinanceiro, usuario);
        return obj;
    }

    public static void montarDadosContaReceber(ContaReceberNegociacaoRecebimentoVO obj, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
        if (obj.getContaReceber().getCodigo().intValue() == 0) {
            obj.setContaReceber(new ContaReceberVO());
            return;
        }
        obj.setContaReceber(getFacadeFactory().getContaReceberFacade().consultarPorChavePrimaria(obj.getContaReceber().getCodigo(), false, nivelMontarDados, configuracaoFinanceiro,usuario));
        
    }

    /**
     * Operação responsável por excluir todos os objetos da <code>ContaReceberNegociacaoRecebimentoVO</code> no BD.
     * Faz uso da operação <code>excluir</code> disponível na classe <code>ContaReceberNegociacaoRecebimento</code>.
     * @param <code>negociacaoRecebimento</code> campo chave para exclusão dos objetos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void excluirContaReceberNegociacaoRecebimentos(Integer negociacaoRecebimento, UsuarioVO usuario) throws Exception {
        String sql = "DELETE FROM ContaReceberNegociacaoRecebimento WHERE (negociacaoRecebimento = ?)"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(sql, new Object[]{negociacaoRecebimento});
    }

    /**
     * Operação responsável por alterar todos os objetos da <code>ContaReceberNegociacaoRecebimentoVO</code> contidos em um Hashtable no BD.
     * Faz uso da operação <code>excluirContaReceberNegociacaoRecebimentos</code> e <code>incluirContaReceberNegociacaoRecebimentos</code> disponíveis na classe <code>ContaReceberNegociacaoRecebimento</code>.
     * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarContaReceberNegociacaoRecebimentos(NegociacaoRecebimentoVO negociacaoRecebimento, List objetos,ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
        String str = "DELETE FROM ContaReceberNegociacaoRecebimento WHERE negociacaoRecebimento = " + negociacaoRecebimento.getCodigo();
        Iterator i = objetos.iterator();
        while (i.hasNext()) {
            ContaReceberNegociacaoRecebimentoVO objeto = (ContaReceberNegociacaoRecebimentoVO) i.next();
            str += " AND codigo <> " + objeto.getCodigo().intValue();
        }
        getConexao().getJdbcTemplate().update(str+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            ContaReceberNegociacaoRecebimentoVO objeto = (ContaReceberNegociacaoRecebimentoVO) e.next();
            objeto.setBloqueioPorFechamentoMesLiberado(negociacaoRecebimento.getBloqueioPorFechamentoMesLiberado());
            objeto.setNegociacaoRecebimento(negociacaoRecebimento.getCodigo());
            if (objeto.getCodigo().equals(0)) {
                incluir(objeto, configuracaoFinanceiro, usuario);
            } else {
                alterar(objeto, configuracaoFinanceiro, usuario);
            }
        }
    }

    /**
     * Operação responsável por incluir objetos da <code>ContaReceberNegociacaoRecebimentoVO</code> no BD.
     * Garantindo o relacionamento com a entidade principal <code>financeiro.NegociacaoRecebimento</code> através do atributo de vínculo.
     * @param objetos List contendo os objetos a serem gravados no BD da classe.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void incluirContaReceberNegociacaoRecebimentos(NegociacaoRecebimentoVO negociacaoRecebimentoPrm, List<ContaReceberNegociacaoRecebimentoVO> objetos,ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
        for (ContaReceberNegociacaoRecebimentoVO obj : objetos) {
        	obj.setBloqueioPorFechamentoMesLiberado(negociacaoRecebimentoPrm.getBloqueioPorFechamentoMesLiberado());
            obj.setNegociacaoRecebimento(negociacaoRecebimentoPrm.getCodigo());
            incluir(obj, configuracaoFinanceiro, usuario);          	
            if (obj.getContaReceber().getTipoOrigem().equals(TipoOrigemContaReceber.DEVOLUCAO_CHEQUE.getValor())) {
            	getFacadeFactory().getMapaLancamentoFuturoFacade().excluirPorCodigoOrigem(Integer.parseInt(obj.getContaReceber().getCodOrigem()), false, usuario);
            }
        }
    }

    /**
     * Operação responsável por consultar todos os <code>ContaReceberNegociacaoRecebimentoVO</code> relacionados a um objeto da classe <code>financeiro.NegociacaoRecebimento</code>.
     * @param negociacaoRecebimento  Atributo de <code>financeiro.NegociacaoRecebimento</code> a ser utilizado para localizar os objetos da classe <code>ContaReceberNegociacaoRecebimentoVO</code>.
     * @return List  Contendo todos os objetos da classe <code>ContaReceberNegociacaoRecebimentoVO</code> resultantes da consulta.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public static List<ContaReceberNegociacaoRecebimentoVO> consultarContaReceberNegociacaoRecebimentos(Integer negociacaoRecebimento, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
        ContaReceberNegociacaoRecebimento.consultar(getIdEntidade());
        List objetos = new ArrayList(0);
        String sql = "SELECT * FROM ContaReceberNegociacaoRecebimento WHERE negociacaoRecebimento = ?";
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{negociacaoRecebimento});
        while (resultado.next()) {
            ContaReceberNegociacaoRecebimentoVO novoObj = new ContaReceberNegociacaoRecebimentoVO();
            novoObj = ContaReceberNegociacaoRecebimento.montarDados(resultado, nivelMontarDados, configuracaoFinanceiro, usuario);
            objetos.add(novoObj);
        }
        return objetos;
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>ContaReceberNegociacaoRecebimentoVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public ContaReceberNegociacaoRecebimentoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM ContaReceberNegociacaoRecebimento WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( ContaReceberNegociacaoRecebimento ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return ContaReceberNegociacaoRecebimento.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        ContaReceberNegociacaoRecebimento.idEntidade = idEntidade;
    }
    
    /**
     * Operação responsável por verificar se existe registro na tabela contaReceberNegociacaoRecebimento
     * consultando pelo código da conta receber
     */
    public Boolean consultarExistenciaContaReceberNegociacaoRecebimentoPorContaReceber(Integer contaReceber) throws Exception {
		Integer codigoContaReceberNegociacaoRecebimentoExistente = null;
		StringBuilder sql = new StringBuilder(" SELECT contaReceberNegociacaoRecebimento.codigo ");
		sql.append(" FROM contaReceberNegociacaoRecebimento ");
		sql.append(" INNER JOIN contaReceber ON contaReceber.codigo = contaReceberNegociacaoRecebimento.contaReceber ");
		sql.append(" WHERE contaReceber.codigo = ? AND contareceber.situacao = ? ");
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), contaReceber, SituacaoContaReceber.RECEBIDO.getValor());
		while (resultado.next()) {
			codigoContaReceberNegociacaoRecebimentoExistente = resultado.getInt("codigo");
		}
		if (Uteis.isAtributoPreenchido(codigoContaReceberNegociacaoRecebimentoExistente)) {
			return Boolean.TRUE;
		} 
		return Boolean.FALSE;
	}
}
