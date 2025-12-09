package negocio.facade.jdbc.financeiro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
import negocio.comuns.financeiro.PlanoDescontoContaReceberVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoDescontoAluno;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.PlanoDescontoContaReceberInterfaceFacade;
import relatorio.negocio.comuns.financeiro.BoletoBancarioRelVO;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>PlanoDescontoContaReceberVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>PlanoDescontoContaReceberVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see PlanoDescontoContaReceberVO
 * @see ControleAcesso
 * @see ProvisaoCusto
 */
@Repository
@Scope("singleton")
@Lazy 
public class PlanoDescontoContaReceber extends ControleAcesso implements PlanoDescontoContaReceberInterfaceFacade{

    /**
	 * 
	 */
	private static final long serialVersionUID = 2158629049371296799L;
	protected static String idEntidade;

    public PlanoDescontoContaReceber() throws Exception {
        super();
        setIdEntidade("ContaReceber");
    }
  

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>PlanoDescontoContaReceberVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>PlanoDescontoContaReceberVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final PlanoDescontoContaReceberVO obj, UsuarioVO usuario) throws Exception {
        incluir(obj, usuario, true);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final PlanoDescontoContaReceberVO obj, UsuarioVO usuario, final boolean verificarPermissao) throws Exception {
        PlanoDescontoContaReceberVO.validarDados(obj);
        PlanoDescontoContaReceber.incluir(getIdEntidade(), verificarPermissao, usuario);
        obj.realizarUpperCaseDados();
        final String sql = "INSERT INTO PlanoDescontoContaReceber (contaReceber, planoDesconto, tipoItemPlanoFinanceiro, convenio, " +
        		"valorutilizadorecebimento, " +
        		"diasValidadeVencimento, tipoDesconto, valorDesconto, ordemPrioridadeParaCalculo, " + 
        		"imposto, aplicarSobreValorCheio, aplicarSobreValorBaseDeduzidoValorOutrosPlanosDesconto, utilizarDiaUtil, " + 
        		"utilizarDiaFixo, descontoValidoAteDataVencimento, usuarioResponsavel, observacaoDesconto, " +
        		"dataConcessaoDesconto, nome, suspensoFinanciamentoProprio, utilizarAvancoDiaUtil " 
        		+ ") VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
                PreparedStatement sqlInserir = cnctn.prepareStatement(sql);
                sqlInserir.setInt(1, obj.getContaReceber().intValue());
                if (obj.getPlanoDescontoVO().getCodigo().intValue() > 0) {
                    sqlInserir.setInt(2, obj.getPlanoDescontoVO().getCodigo().intValue());
                } else {
                    sqlInserir.setNull(2, 0);
                }
                sqlInserir.setString(3, obj.getTipoItemPlanoFinanceiro());
                if (obj.getConvenio().getCodigo().intValue() != 0) {
                    sqlInserir.setInt(4, obj.getConvenio().getCodigo().intValue());
                } else {
                    sqlInserir.setNull(4, 0);
                }
                sqlInserir.setDouble(5, obj.getValorUtilizadoRecebimento());
                
                if (obj.getDiasValidadeVencimento() != null) {
                	sqlInserir.setInt(6, obj.getDiasValidadeVencimento());
                } else {
                    sqlInserir.setNull(6, 0);
                }
                sqlInserir.setString(7, obj.getTipoDesconto().getValor().toString());
                sqlInserir.setDouble(8, obj.getValorDesconto().doubleValue());
                sqlInserir.setInt(9, obj.getOrdemPrioridadeParaCalculo());
                if (obj.getImposto().getCodigo().intValue() != 0) {
                    sqlInserir.setInt(10, obj.getImposto().getCodigo().intValue());
                } else {
                	sqlInserir.setNull(10, 0);
                }
                sqlInserir.setBoolean(11, obj.getAplicarSobreValorCheio().booleanValue());
                sqlInserir.setBoolean(12, obj.getAplicarSobreValorBaseDeduzidoValorOutrosPlanosDesconto().booleanValue());
                sqlInserir.setBoolean(13, obj.getUtilizarDiaUtil().booleanValue());
                sqlInserir.setBoolean(14, obj.getUtilizarDiaFixo().booleanValue());
                sqlInserir.setBoolean(15, obj.getDescontoValidoAteDataVencimento().booleanValue());
                if (obj.getUsuarioResponsavel().getCodigo().intValue() != 0) {
                	sqlInserir.setInt(16, obj.getUsuarioResponsavel().getCodigo().intValue());
                } else {
                	sqlInserir.setNull(16, 0);
                }
                sqlInserir.setString(17, obj.getObservacaoDesconto());
                sqlInserir.setDate(18, Uteis.getDataJDBC(obj.getDataConcessaoDesconto()));
                sqlInserir.setString(19, obj.getNome());
                sqlInserir.setBoolean(20, obj.isSuspensoFinanciamentoProprio());
                sqlInserir.setBoolean(21, obj.getUtilizarAvancoDiaUtil());
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
        obj.setNovoObj(Boolean.FALSE);
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>PlanoDescontoContaReceberVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>PlanoDescontoContaReceberVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final PlanoDescontoContaReceberVO obj, UsuarioVO usuario) throws Exception {
        PlanoDescontoContaReceberVO.validarDados(obj);
        PlanoDescontoContaReceber.alterar(getIdEntidade());
        obj.realizarUpperCaseDados();
        final String sql = "UPDATE PlanoDescontoContaReceber set contaReceber=?, planoDesconto=?, tipoItemPlanoFinanceiro=?, convenio=?, " +
        		"valorutilizadorecebimento=? ," +
        		"diasValidadeVencimento=?, tipoDesconto=?, valorDesconto=?, ordemPrioridadeParaCalculo=?, " + 
        		"imposto=?, aplicarSobreValorCheio=?, aplicarSobreValorBaseDeduzidoValorOutrosPlanosDesconto=?, utilizarDiaUtil=?, " + 
        		"utilizarDiaFixo=?, descontoValidoAteDataVencimento=?, usuarioResponsavel=?, observacaoDesconto=?, " +
        		"dataConcessaoDesconto=?, nome=?, suspensoFinanciamentoProprio=?, utilizarAvancoDiaUtil=? "
        		+ "WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
                PreparedStatement sqlAlterar = cnctn.prepareStatement(sql);
                sqlAlterar.setInt(1, obj.getContaReceber().intValue());
                if (obj.getPlanoDescontoVO().getCodigo().intValue() > 0) {
                    sqlAlterar.setInt(2, obj.getPlanoDescontoVO().getCodigo().intValue());
                } else {
                    sqlAlterar.setNull(2, 0);
                }
                sqlAlterar.setString(3, obj.getTipoItemPlanoFinanceiro());
                if (obj.getConvenio().getCodigo().intValue() != 0) {
                    sqlAlterar.setInt(4, obj.getConvenio().getCodigo().intValue());
                } else {
                    sqlAlterar.setNull(4, 0);
                }
                sqlAlterar.setDouble(5, obj.getValorUtilizadoRecebimento());
                
                if (obj.getDiasValidadeVencimento() != null) {
                	sqlAlterar.setInt(6, obj.getDiasValidadeVencimento());
                } else {
                	sqlAlterar.setNull(6, 0);
                }
                sqlAlterar.setString(7, obj.getTipoDesconto().getValor().toString());
                sqlAlterar.setDouble(8, obj.getValorDesconto().doubleValue());
                sqlAlterar.setInt(9, obj.getOrdemPrioridadeParaCalculo());
                if (obj.getImposto().getCodigo().intValue() != 0) {
                    sqlAlterar.setInt(10, obj.getImposto().getCodigo().intValue());
                } else {
                	sqlAlterar.setNull(10, 0);
                }
                sqlAlterar.setBoolean(11, obj.getAplicarSobreValorCheio().booleanValue());
                sqlAlterar.setBoolean(12, obj.getAplicarSobreValorBaseDeduzidoValorOutrosPlanosDesconto().booleanValue());
                sqlAlterar.setBoolean(13, obj.getUtilizarDiaUtil().booleanValue());
                sqlAlterar.setBoolean(14, obj.getUtilizarDiaFixo().booleanValue());
                sqlAlterar.setBoolean(15, obj.getDescontoValidoAteDataVencimento().booleanValue());
                if (obj.getUsuarioResponsavel().getCodigo().intValue() != 0) {
                	sqlAlterar.setInt(16, obj.getUsuarioResponsavel().getCodigo().intValue());
                } else {
                	sqlAlterar.setNull(16, 0);
                }
                sqlAlterar.setString(17, obj.getObservacaoDesconto());
                sqlAlterar.setDate(18, Uteis.getDataJDBC(obj.getDataConcessaoDesconto()));
                sqlAlterar.setString(19, obj.getNome());                
                sqlAlterar.setBoolean(20, obj.isSuspensoFinanciamentoProprio());
                sqlAlterar.setBoolean(21, obj.getUtilizarAvancoDiaUtil());
                sqlAlterar.setInt(22, obj.getCodigo().intValue());
                return sqlAlterar;
            }
        });

    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>PlanoDescontoContaReceberVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>PlanoDescontoContaReceberVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(PlanoDescontoContaReceberVO obj, UsuarioVO usuario) throws Exception {
        PlanoDescontoContaReceber.excluir(getIdEntidade());
        String sql = "DELETE FROM PlanoDescontoContaReceber WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
    }
    
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public List<PlanoDescontoContaReceberVO> consultarPorCodigoOrigemDaContaReceberNegociada(Integer contaReceber, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new  StringBuilder();
        sqlStr.append(" select planodescontocontareceber.* from planodescontocontareceber  ");
        sqlStr.append(" inner join contarecebernegociado on planodescontocontareceber.contareceber = contarecebernegociado.contareceber  ");
        sqlStr.append(" where contarecebernegociado.negociacaocontareceber = (  ");
        sqlStr.append(" select codorigem::int from contareceber where codigo =  ").append(contaReceber);
        sqlStr.append(" ) ");
        
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }
    
    public List consultarPorContaReceber(Integer contaReceber, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
    	String sqlStr = "SELECT PlanoDescontoContaReceber.* FROM PlanoDescontoContaReceber WHERE contaReceber = " + contaReceber.intValue() + " ORDER BY codigo";
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
    	return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    public List consultarPorPlanoDesconto(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM PlanoDescontoContaReceber inner join planoDesconto on PlanoDescontoContaReceber.planoDesconto = planoDesconto.codigo WHERE upper( planoDesconto.nome ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY planoDesconto.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>ItensProvisao</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>PlanoDescontoContaReceberVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM PlanoDescontoContaReceber WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>PlanoDescontoContaReceberVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        tabelaResultado = null;
        
        Ordenacao.ordenarLista(vetResultado, "ordemPrioridadeParaCalculo");
        
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>PlanoDescontoContaReceberVO</code>.
     * @return  O objeto da classe <code>PlanoDescontoContaReceberVO</code> com os dados devidamente montados.
     */
    public static PlanoDescontoContaReceberVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        PlanoDescontoContaReceberVO obj = new PlanoDescontoContaReceberVO();
        obj.setNovoObj(Boolean.FALSE);
        obj.setCodigo(dadosSQL.getInt("codigo"));
        obj.setContaReceber(dadosSQL.getInt("contaReceber"));
        obj.getPlanoDescontoVO().setCodigo(dadosSQL.getInt("planoDesconto"));
        obj.setTipoItemPlanoFinanceiro(dadosSQL.getString("tipoItemPlanoFinanceiro"));
        obj.getConvenio().setCodigo(dadosSQL.getInt("convenio"));
        obj.setValorUtilizadoRecebimento(dadosSQL.getDouble("valorutilizadorecebimento"));
        obj.setSuspensoFinanciamentoProprio(dadosSQL.getBoolean("suspensoFinanciamentoProprio"));
        if(dadosSQL.getObject("diasValidadeVencimento") != null) {
        	obj.setDiasValidadeVencimento(dadosSQL.getInt("diasValidadeVencimento"));
        }
        if(dadosSQL.getString("tipoDesconto") != null && !dadosSQL.getString("tipoDesconto").trim().isEmpty()) {
        	obj.setTipoDesconto(TipoDescontoAluno.getEnum(dadosSQL.getString("tipoDesconto")));
        }else {
        	obj.setTipoDesconto(TipoDescontoAluno.VALOR);
        }
        obj.setValorDesconto(dadosSQL.getDouble("valorDesconto"));
        obj.setOrdemPrioridadeParaCalculo(dadosSQL.getInt("ordemPrioridadeParaCalculo"));
        obj.getImposto().setCodigo(dadosSQL.getInt("imposto"));
        obj.setAplicarSobreValorCheio(dadosSQL.getBoolean("aplicarSobreValorCheio"));        
        obj.setAplicarSobreValorBaseDeduzidoValorOutrosPlanosDesconto(dadosSQL.getBoolean("aplicarSobreValorBaseDeduzidoValorOutrosPlanosDesconto"));
        obj.setUtilizarDiaUtil(dadosSQL.getBoolean("utilizarDiaUtil"));
        obj.setUtilizarDiaFixo(dadosSQL.getBoolean("utilizarDiaFixo"));
        obj.setUtilizarAvancoDiaUtil(dadosSQL.getBoolean("utilizarAvancoDiaUtil"));
        obj.setDescontoValidoAteDataVencimento(dadosSQL.getBoolean("descontoValidoAteDataVencimento"));
        obj.getUsuarioResponsavel().setCodigo(dadosSQL.getInt("usuarioResponsavel"));
        obj.setObservacaoDesconto(dadosSQL.getString("observacaoDesconto"));
        obj.setDataConcessaoDesconto(dadosSQL.getDate("dataConcessaoDesconto"));
        obj.setNome(dadosSQL.getString("nome"));
        if (obj.getTipoItemPlanoFinanceiro().equals("DM")) {
        	// se for do tipo de desconto manual, entao temos inicializar os dados do PlanoDescontoVO
        	// com os dados do desconto manual. Isto é necessário para que as rotinas tratem o planoDescontoVO
        	// como um plano já previamente cadastro e vinculado. Só que na pratica o mesmo foi cadastrado pelo
        	// usuario manualmente.
        	obj.inicializarAtributosPlanoDescontoComDadosDescontoManualContaReceber();
        } else {
        	if (obj.getDiasValidadeVencimento() != null) {
        		// caso exista uma definicao de dias validade no PlanoDescontoContareceber o mesmo deve prevalecar
        		// sobre os demais.
        		obj.getPlanoDescontoVO().setDiasValidadeVencimento(obj.getDiasValidadeVencimento());        		
        	}      
        }
        
        
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        montarDadosPlanoDesconto(obj, nivelMontarDados, usuario);
        montarDadosConvenio(obj, Uteis.NIVELMONTARDADOS_PROCESSAMENTO, usuario);
        montarDadosImposto(obj, nivelMontarDados, usuario);
        montarDadosUsuarioResponsavel(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
        return obj;
    }

    public static void montarDadosPlanoDesconto(PlanoDescontoContaReceberVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	if (obj.getPlanoDescontoVO().getCodigo().intValue() == 0) {
            return;
        }
        obj.setPlanoDescontoVO(getFacadeFactory().getPlanoDescontoFacade().consultarPorChavePrimaria(obj.getPlanoDescontoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));
        if (obj.getDiasValidadeVencimento() != null) {
        	obj.getPlanoDescontoVO().setDiasValidadeVencimento(obj.getDiasValidadeVencimento());
        }
    }

    public static void montarDadosConvenio(PlanoDescontoContaReceberVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getConvenio().getCodigo().intValue() == 0) {
            return;
        }
        obj.setConvenio(getFacadeFactory().getConvenioFacade().consultarPorChavePrimaria(obj.getConvenio().getCodigo(), false, nivelMontarDados, usuario));
    }
    
    public static void montarDadosImposto(PlanoDescontoContaReceberVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getImposto().getCodigo().intValue() == 0) {
            return;
        }
        obj.setImposto(getFacadeFactory().getImpostoFacade().consultarPorChavePrimaria(obj.getImposto().getCodigo(), false, nivelMontarDados, usuario));
    }
    
    public static void montarDadosUsuarioResponsavel(PlanoDescontoContaReceberVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getUsuarioResponsavel().getCodigo().intValue() == 0) {
            return;
        }
        obj.setUsuarioResponsavel(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getUsuarioResponsavel().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
    }    

    /**
     * Operação responsável por excluir todos os objetos da <code>PlanoDescontoContaReceberVO</code> no BD.
     * Faz uso da operação <code>excluir</code> disponível na classe <code>ItensProvisao</code>.
     * @param <code>provisaoCusto</code> campo chave para exclusão dos objetos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirItensPlanoDescontoContaReceber(Integer contaReceber, UsuarioVO usuario) throws Exception {
        PlanoDescontoContaReceber.excluir(getIdEntidade());
        String sql = "DELETE FROM PlanoDescontoContaReceber WHERE (contaReceber = ?)"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(sql, new Object[]{contaReceber});
    }

    /**
     * Operação responsável por alterar todos os objetos da <code>PlanoDescontoContaReceberVO</code> contidos em um Hashtable no BD.
     * Faz uso da operação <code>excluirItensProvisaos</code> e <code>incluirItensProvisaos</code> disponíveis na classe <code>ItensProvisao</code>.
     * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarItensPlanoDescontoContaReceber(Integer contaReceber, List objetos, UsuarioVO usuario) throws Exception {
        String str = "DELETE FROM PlanoDescontoContaReceber WHERE contaReceber = " + contaReceber;
        Iterator i = objetos.iterator();
        while (i.hasNext()) {
            PlanoDescontoContaReceberVO objeto = (PlanoDescontoContaReceberVO) i.next();
            str += " AND codigo <> " + objeto.getCodigo().intValue();
        }
        getConexao().getJdbcTemplate().update(str+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            PlanoDescontoContaReceberVO objeto = (PlanoDescontoContaReceberVO) e.next();
            objeto.setContaReceber(contaReceber);
            if (objeto.getCodigo().equals(0)) {
                incluir(objeto,usuario);
            } else {
                alterar(objeto, usuario);
            }
        }
    }

    /**
     * Operação responsável por incluir objetos da <code>PlanoDescontoContaReceberVO</code> no BD.
     * Garantindo o relacionamento com a entidade principal <code>financeiro.ProvisaoCusto</code> através do atributo de vínculo.
     * @param objetos List contendo os objetos a serem gravados no BD da classe.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirItensPlanoDescontoContaReceber(Integer contaReceberPrm, List objetos, UsuarioVO usuario) throws Exception {
        incluirItensPlanoDescontoContaReceber(contaReceberPrm, objetos, usuario, true);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirItensPlanoDescontoContaReceber(Integer contaReceberPrm, List objetos, UsuarioVO usuario, boolean verificarPermissao) throws Exception {
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            PlanoDescontoContaReceberVO obj = (PlanoDescontoContaReceberVO) e.next();
            obj.setContaReceber(contaReceberPrm);
            incluir(obj, usuario, verificarPermissao);
        }
    }

    /**
     * Operação responsável por consultar todos os <code>PlanoDescontoContaReceberVO</code> relacionados a um objeto da classe <code>financeiro.ProvisaoCusto</code>.
     * @param provisaoCusto  Atributo de <code>financeiro.ProvisaoCusto</code> a ser utilizado para localizar os objetos da classe <code>PlanoDescontoContaReceberVO</code>.
     * @return List  Contendo todos os objetos da classe <code>PlanoDescontoContaReceberVO</code> resultantes da consulta.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public static List consultarItensPlanoDescontoContaReceber(Integer contaReceber, int nivelMontarDados , UsuarioVO usuario) throws Exception {
        PlanoDescontoContaReceber.consultar(getIdEntidade());
        List objetos = new ArrayList(0);
        String sql = "SELECT * FROM PlanoDescontoContaReceber WHERE contaReceber = ?";
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{contaReceber});
        while (resultado.next()) {
            PlanoDescontoContaReceberVO novoObj = new PlanoDescontoContaReceberVO();
            novoObj = PlanoDescontoContaReceber.montarDados(resultado, nivelMontarDados, usuario);
            objetos.add(novoObj);
        }
        return objetos;
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>PlanoDescontoContaReceberVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public PlanoDescontoContaReceberVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM PlanoDescontoContaReceber WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( PlanoDescontoContaReceber ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return PlanoDescontoContaReceber.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        PlanoDescontoContaReceber.idEntidade = idEntidade;
    }
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void modificarValorUtilizadoRecebimentoDosPlanos(Integer codigoConta, Double valor) throws Exception {
		String sqlStr = "UPDATE planodescontocontareceber SET valorutilizadorecebimento = " + valor + " WHERE contareceber = " + codigoConta + " ";
		try {
			getConexao().getJdbcTemplate().execute(sqlStr);
		} finally {
			sqlStr = null;
		}
	}
    
    @Override
    public Map<Integer, List<PlanoDescontoContaReceberVO>> consultarPlanoDescontoContaRecberParaGeracaoBoleto(List<BoletoBancarioRelVO> boletoBancarioRelVOs) throws Exception{
        
        Map<Integer, List<PlanoDescontoContaReceberVO>> resultado = new HashMap<Integer, List<PlanoDescontoContaReceberVO>>(0);
        StringBuilder sb = new StringBuilder("");
        try {
        	if (boletoBancarioRelVOs != null && !boletoBancarioRelVOs.isEmpty()) {
                sb.append(getSqlBaseDadosGeracaoBoletoRemessa());                
                sb.append(" where PlanoDescontoContaReceber.contareceber in (");
                boolean vigula = false;
                for (BoletoBancarioRelVO bancarioRelVO : boletoBancarioRelVOs) {
                    if (vigula) {
                        sb.append(",").append(bancarioRelVO.getContareceber_codigo());
                    } else {
                        sb.append(bancarioRelVO.getContareceber_codigo());
                        vigula = true;
                    }
                }
                sb.append(") order by PlanoDescontoContaReceber.contareceber ");
                SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
                List<PlanoDescontoContaReceberVO> contaReceberVOs = null;                                 
                
                while(tabelaResultado.next()){
                    if(!resultado.containsKey(tabelaResultado.getInt("contareceber"))){
                        contaReceberVOs = new ArrayList<PlanoDescontoContaReceberVO>(0);
                        resultado.put(tabelaResultado.getInt("contareceber"), contaReceberVOs);
                    }
                    contaReceberVOs.add(montarDadosPlanoDescontoContaRecberParaGeracaoBoleto(tabelaResultado));
                }
            }
            return resultado;
        }catch(Exception e){
            throw  e;
        }finally{
        
        }
    }
    
    @Override
    public List<PlanoDescontoContaReceberVO> consultarPlanoDescontoContaReceberParaGeracaoRemessa(Integer codigoContaReceber) throws Exception{        
        List<PlanoDescontoContaReceberVO> resultado = new ArrayList<PlanoDescontoContaReceberVO>(0);
        StringBuilder sb = new StringBuilder("");
        try {
            if (Uteis.isAtributoPreenchido(codigoContaReceber)) {
                sb.append(getSqlBaseDadosGeracaoBoletoRemessa());                
                sb.append(" where PlanoDescontoContaReceber.contareceber = ").append(codigoContaReceber);               
                SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
                while(tabelaResultado.next()){                
                	resultado.add(montarDadosPlanoDescontoContaRecberParaGeracaoBoleto(tabelaResultado));
                }
            }
            return resultado;
        }catch(Exception e){
            throw  e;
        }finally{
        
        }
    }
    
    private StringBuilder getSqlBaseDadosGeracaoBoletoRemessa() {
    	 StringBuilder sb = new StringBuilder("");
    	 sb.append(" select distinct PlanoDescontoContaReceber.codigo, PlanoDescontoContaReceber.contareceber, PlanoDescontoContaReceber.tipoItemPlanoFinanceiro,");
         sb.append(" PlanoDescontoContaReceber.planoDesconto, PlanoDescontoContaReceber.convenio, PlanoDescontoContaReceber.valorUtilizadoRecebimento,");
         
         sb.append(" PlanoDescontoContaReceber.diasValidadeVencimento, PlanoDescontoContaReceber.tipoDesconto, PlanoDescontoContaReceber.valorDesconto, PlanoDescontoContaReceber.ordemPrioridadeParaCalculo, ");
         sb.append(" PlanoDescontoContaReceber.imposto, PlanoDescontoContaReceber.aplicarSobreValorCheio, PlanoDescontoContaReceber.aplicarSobreValorBaseDeduzidoValorOutrosPlanosDesconto, PlanoDescontoContaReceber.utilizarDiaUtil, ");
         sb.append(" PlanoDescontoContaReceber.descontoValidoAteDataVencimento, ");
         sb.append(" PlanoDescontoContaReceber.nome, PlanoDescontoContaReceber.suspensoFinanciamentoProprio, ");
         sb.append(" PlanoDescontoContaReceber.utilizarDiaFixo, PlanoDescontoContaReceber.utilizarAvancoDiaUtil, ");
         
         sb.append(" PlanoDesconto.codigo as PlanoDesconto_codigo, PlanoDesconto.nome as PlanoDesconto_nome, PlanoDesconto.tipoDescontoParcela as PlanoDesconto_tipoDescontoParcela,");
         sb.append(" PlanoDesconto.percDescontoParcela as PlanoDesconto_percDescontoParcela, PlanoDesconto.tipoDescontoMatricula as PlanoDesconto_tipoDescontoMatricula, ");
         sb.append(" PlanoDesconto.percDescontoMatricula as PlanoDesconto_percDescontoMatricula, PlanoDesconto.diasValidadeVencimento as PlanoDesconto_diasValidadeVencimento, ");
         sb.append(" PlanoDesconto.somente1PeriodoLetivoParcela as PlanoDesconto_somente1PeriodoLetivoParcela,PlanoDesconto.descontoValidoAteDataVencimento as PlanoDesconto_descontoValidoAteDataVencimento, ");
         sb.append(" PlanoDesconto.somente1PeriodoLetivoMatricula as PlanoDesconto_somente1PeriodoLetivoMatricula, PlanoDesconto.ativo as  PlanoDesconto_ativo, PlanoDesconto.aplicarSobreValorCheio as PlanoDesconto_aplicarSobreValorCheio, PlanoDesconto.utilizarDiaUtil as PlanoDesconto_utilizarDiaUtil, ");
         sb.append(" PlanoDesconto.aplicarSobreValorBaseDeduzidoValorOutrosPlanosDesconto as PD_aplicarSobreValorBaseDeduzidoValorOutrosPlanosDesconto, ");
         sb.append(" PlanoDesconto.ordemPrioridadeParaCalculo as PlanoDesconto_ordemPrioridadeParaCalculo, ");
         sb.append(" PlanoDesconto.utilizarDiaFixo as PlanoDesconto_utilizarDiaFixo,  PlanoDesconto.utilizarAvancoDiaUtil as PlanoDesconto_utilizarAvancoDiaUtil, ");
         
         sb.append(" convenio.codigo as convenio_codigo, convenio.descontoMatricula as convenio_descontoMatricula,");
         sb.append(" convenio.tipoDescontoMatricula as convenio_tipoDescontoMatricula, convenio.descontoParcela as convenio_descontoParcela, Convenio.calculadoEmCimaValorLiquido as Convenio_calculadoEmCimaValorLiquido,");
         sb.append(" convenio.tipoDescontoParcela as convenio_tipoDescontoParcela, convenio.descontoParcela as convenio_descontoParcela, ");
         sb.append(" convenio.aplicarSobreValorBaseDeduzidoValorOutrosConvenios as convenio_aplicarSobreValorBaseDeduzidoValorOutrosConvenios, convenio.ordemPrioridadeParaCalculo as convenio_ordemPrioridadeParaCalculo, ");
         sb.append(" convenio.abaterDescontoNoValorMatricula as convenio_abaterDescontoNoValorMatricula, convenio.abaterDescontoNoValorParcela as convenio_abaterDescontoNoValorParcela, ");
         sb.append(" parceiro.codigo as parceiro_codigo, parceiro.financiamentoProprio as parceiro_financiamentoProprio, parceiro.emitirBoletoEmNomeBeneficiado as parceiro_emitirBoletoEmNomeBeneficiado ");
         sb.append(" from PlanoDescontoContaReceber ");
         sb.append(" left join planoDesconto on planodesconto.codigo = PlanoDescontoContaReceber.planodesconto");
         sb.append(" left join convenio on convenio.codigo = PlanoDescontoContaReceber.convenio");
         sb.append(" left join parceiro on parceiro.codigo = convenio.parceiro");
         return sb;
    }
    
    private PlanoDescontoContaReceberVO montarDadosPlanoDescontoContaRecberParaGeracaoBoleto(SqlRowSet dadosSQL){
        PlanoDescontoContaReceberVO obj = new PlanoDescontoContaReceberVO();
        obj.setNovoObj(Boolean.FALSE);
        obj.setCodigo(dadosSQL.getInt("codigo"));
        obj.setContaReceber(dadosSQL.getInt("contaReceber"));
        obj.getPlanoDescontoVO().setCodigo(dadosSQL.getInt("planoDesconto"));
        obj.setTipoItemPlanoFinanceiro(dadosSQL.getString("tipoItemPlanoFinanceiro"));
        obj.getConvenio().setCodigo(dadosSQL.getInt("convenio"));
        obj.setValorUtilizadoRecebimento(dadosSQL.getDouble("valorutilizadorecebimento"));
        obj.setSuspensoFinanciamentoProprio(dadosSQL.getBoolean("suspensoFinanciamentoProprio"));
        if(dadosSQL.getObject("diasValidadeVencimento") != null) {
        	obj.setDiasValidadeVencimento(dadosSQL.getInt("diasValidadeVencimento"));
        }
        obj.setTipoDesconto(TipoDescontoAluno.getEnum(dadosSQL.getString("tipoDesconto")));
        obj.setValorDesconto(dadosSQL.getDouble("valorDesconto"));
        obj.setOrdemPrioridadeParaCalculo(dadosSQL.getInt("ordemPrioridadeParaCalculo"));
        obj.getImposto().setCodigo(dadosSQL.getInt("imposto"));
        obj.setAplicarSobreValorCheio(dadosSQL.getBoolean("aplicarSobreValorCheio"));        
        obj.setAplicarSobreValorBaseDeduzidoValorOutrosPlanosDesconto(dadosSQL.getBoolean("aplicarSobreValorBaseDeduzidoValorOutrosPlanosDesconto"));
        obj.setUtilizarDiaUtil(dadosSQL.getBoolean("utilizarDiaUtil"));
        obj.setUtilizarDiaFixo(dadosSQL.getBoolean("utilizarDiaFixo"));
        obj.setUtilizarAvancoDiaUtil(dadosSQL.getBoolean("utilizarAvancoDiaUtil"));
        obj.setDescontoValidoAteDataVencimento(dadosSQL.getBoolean("descontoValidoAteDataVencimento"));
        obj.setNome(dadosSQL.getString("nome"));
        
              
        if (obj.getIsConvenio()){
            obj.getConvenio().setDescontoMatricula(dadosSQL.getDouble("Convenio_descontoMatricula"));
            obj.getConvenio().setTipoDescontoMatricula(dadosSQL.getString("Convenio_tipoDescontoMatricula"));
            obj.getConvenio().setDescontoParcela(dadosSQL.getDouble("Convenio_descontoParcela"));
            obj.getConvenio().setTipoDescontoParcela(dadosSQL.getString("Convenio_tipoDescontoParcela"));
            obj.getConvenio().setCalculadoEmCimaValorLiquido(dadosSQL.getBoolean("Convenio_calculadoEmCimaValorLiquido"));
            obj.getConvenio().setAplicarSobreValorBaseDeduzidoValorOutrosConvenios(dadosSQL.getBoolean("Convenio_aplicarSobreValorBaseDeduzidoValorOutrosConvenios"));
            obj.getConvenio().setOrdemPrioridadeParaCalculo(dadosSQL.getInt("Convenio_ordemPrioridadeParaCalculo"));
            obj.getConvenio().setAbaterDescontoNoValorMatricula(dadosSQL.getBoolean("convenio_abaterDescontoNoValorMatricula"));
            obj.getConvenio().setAbaterDescontoNoValorParcela(dadosSQL.getBoolean("convenio_abaterDescontoNoValorParcela"));
            obj.getConvenio().getParceiro().setCodigo(dadosSQL.getInt("parceiro_codigo"));
            obj.getConvenio().getParceiro().setFinanciamentoProprio(dadosSQL.getBoolean("parceiro_financiamentoProprio"));
            obj.getConvenio().getParceiro().setEmitirBoletoEmNomeBeneficiado(dadosSQL.getBoolean("parceiro_emitirBoletoEmNomeBeneficiado"));
        
        } else {
        	if (obj.getIsPlanoDescontoInstitucional()) { 
        		obj.getPlanoDescontoVO().setNome(dadosSQL.getString("PlanoDesconto_nome"));
        		obj.getPlanoDescontoVO().setPercDescontoParcela(new Double(dadosSQL.getDouble("PlanoDesconto_percDescontoParcela")));
        		obj.getPlanoDescontoVO().setPercDescontoMatricula(new Double(dadosSQL.getDouble("PlanoDesconto_percDescontoMatricula")));        
        		obj.getPlanoDescontoVO().setSomente1PeriodoLetivoParcela(dadosSQL.getBoolean("PlanoDesconto_somente1PeriodoLetivoParcela"));
        		obj.getPlanoDescontoVO().setSomente1PeriodoLetivoMatricula(dadosSQL.getBoolean("PlanoDesconto_somente1PeriodoLetivoMatricula"));
        		obj.getPlanoDescontoVO().setTipoDescontoParcela(dadosSQL.getString("PlanoDesconto_tipoDescontoParcela"));
        		obj.getPlanoDescontoVO().setTipoDescontoMatricula(dadosSQL.getString("PlanoDesconto_tipoDescontoMatricula"));
        		obj.getPlanoDescontoVO().setDiasValidadeVencimento(new Integer(dadosSQL.getInt("PlanoDesconto_diasValidadeVencimento")));
        		obj.getPlanoDescontoVO().setDescontoValidoAteDataVencimento(dadosSQL.getBoolean("PlanoDesconto_descontoValidoAteDataVencimento"));
        		obj.getPlanoDescontoVO().setAplicarSobreValorCheio(dadosSQL.getBoolean("PlanoDesconto_aplicarSobreValorCheio"));
        		obj.getPlanoDescontoVO().setUtilizarDiaUtil(dadosSQL.getBoolean("PlanoDesconto_utilizarDiaUtil"));
        		obj.getPlanoDescontoVO().setUtilizarDiaFixo(dadosSQL.getBoolean("PlanoDesconto_utilizarDiaFixo"));
        		obj.getPlanoDescontoVO().setUtilizarAvancoDiaUtil(dadosSQL.getBoolean("PlanoDesconto_utilizarAvancoDiaUtil"));
        		obj.getPlanoDescontoVO().setAtivo(dadosSQL.getBoolean("PlanoDesconto_ativo"));        
        		obj.getPlanoDescontoVO().setAplicarSobreValorBaseDeduzidoValorOutrosPlanosDesconto(dadosSQL.getBoolean("PD_aplicarSobreValorBaseDeduzidoValorOutrosPlanosDesconto"));
        		obj.getPlanoDescontoVO().setOrdemPrioridadeParaCalculo(dadosSQL.getInt("PlanoDesconto_ordemPrioridadeParaCalculo"));
        		if (obj.getDiasValidadeVencimento() != null) {
        			//
        			obj.getPlanoDescontoVO().setDiasValidadeVencimento(obj.getDiasValidadeVencimento());
        		}
        	} else {
        		// se entrar aqui é por que temos PlanoDescontoContaReceber manual (lancado para a conta a receber manualmente)
        		// neste caso vamos inicializar os dados de PlanoDescontoVO, com os dados manuais informados pelo usuário
        		obj.inicializarAtributosPlanoDescontoComDadosDescontoManualContaReceber();
        	}
        }
        return obj;
    }

}
