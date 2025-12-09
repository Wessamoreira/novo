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
import negocio.comuns.financeiro.ContaReceberHistoricoVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.ContaReceberHistoricoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>ContaReceberHistoricoVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>ContaReceberHistoricoVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see ContaReceberHistoricoVO
 * @see ControleAcesso
 * @see ContaReceber
 */
@Repository
@Scope("singleton")
@Lazy
public class ContaReceberHistorico extends ControleAcesso implements ContaReceberHistoricoInterfaceFacade {

    protected static String idEntidade;

    public ContaReceberHistorico() throws Exception {
        super();
        setIdEntidade("ContaReceber");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>ContaReceberHistoricoVO</code>.
     */
    public ContaReceberHistoricoVO novo() throws Exception {
        ContaReceberHistorico.incluir(getIdEntidade());
        ContaReceberHistoricoVO obj = new ContaReceberHistoricoVO();
        return obj;
    }
    
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void criarContaReceberHistoricoPorBaixaAutomaticas(ContaReceberVO contaReceberVO,  UsuarioVO responsavel, UsuarioVO usuario) throws Exception {
    	ContaReceberHistoricoVO crh = new ContaReceberHistoricoVO();
    	try {
    		crh.setContaReceber(contaReceberVO.getCodigo());
    		crh.setData(new Date());
    		crh.setMotivo("Conta Receber já processada por baixa convênio. Tentativa em duplicidade!");
    		crh.setNomeArquivo("");
    		crh.setNossoNumero(contaReceberVO.getNossoNumero());
    		crh.setResponsavel(responsavel);
    		crh.setValorRecebimento(contaReceberVO.getValorRecebido());
    		incluir(crh, usuario);	
		} finally {
			crh = null;	
		}
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>ContaReceberHistoricoVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>ContaReceberHistoricoVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void incluir(final ContaReceberHistoricoVO obj, UsuarioVO usuario) throws Exception {
        try {
//            ContaReceberHistoricoVO.validarDados(obj);
//            obj.realizarUpperCaseDados();
            final String sql = "INSERT INTO ContaReceberHistorico( contaReceber, valorRecebimento, data, motivo, responsavel, nossoNumero) VALUES ( ?, ?, ?, ?, ?, ?) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
                    PreparedStatement sqlInserir = cnctn.prepareStatement(sql);
                    if (obj.getContaReceber().intValue() != 0) {
                        sqlInserir.setInt(1, obj.getContaReceber().intValue());
                    } else {
                        sqlInserir.setNull(1, 0);
                    }
                    sqlInserir.setDouble(2, obj.getValorRecebimento().doubleValue());
                    sqlInserir.setTimestamp(3, Uteis.getDataJDBCTimestamp(obj.getData()));
                    sqlInserir.setString(4, obj.getMotivo());
                    if (obj.getResponsavel().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(5, obj.getResponsavel().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(5, 0);
                    }
                    sqlInserir.setString(6, obj.getNossoNumero());
                    return sqlInserir;
                }
            }, new ResultSetExtractor() {

                public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                    if (rs.next()) {
                        obj.setNovoObj(Boolean.FALSE);
                        return rs.getInt("codigo");
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
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>ContaReceberHistoricoVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>ContaReceberHistoricoVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void alterar(final ContaReceberHistoricoVO obj, UsuarioVO usuario) throws Exception {
        try {
//            obj.realizarUpperCaseDados();
            final String sql = "UPDATE ContaReceberHistorico set ContaReceber=?, valorRecebimento=?, data=?, motivo=?, responsavel=?, nossoNumero=? WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
                    PreparedStatement sqlAlterar = cnctn.prepareStatement(sql);
                    if (obj.getContaReceber().intValue() != 0) {
                        sqlAlterar.setInt(1, obj.getContaReceber().intValue());
                    } else {
                        sqlAlterar.setNull(1, 0);
                    }
                    sqlAlterar.setDouble(2, obj.getValorRecebimento().doubleValue());
                    sqlAlterar.setTimestamp(3, Uteis.getDataJDBCTimestamp(obj.getData()));
                    sqlAlterar.setString(4, obj.getMotivo());
                    if (obj.getResponsavel().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(5, obj.getResponsavel().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(5, 0);
                    }
                    sqlAlterar.setString(6, obj.getNossoNumero());
                    sqlAlterar.setInt(7, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>ContaReceberHistoricoVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>ContaReceberHistoricoVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(ContaReceberHistoricoVO obj, UsuarioVO usuario) throws Exception {
        //   ContaReceberHistorico.excluir(getIdEntidade());
        String sql = "DELETE FROM ContaReceberHistorico WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
    }

    /**
     * Responsável por realizar uma consulta de <code>ContaReceberHistorico</code> através do valor do atributo
     * <code>codigo</code> da classe <code>ContaReceber</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>ContaReceberHistoricoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigoContaReceber(Integer valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        //   ControleAcesso.consultar(getIdEntidade(), true);
        String sqlStr = "SELECT * FROM ContaReceberHistorico WHERE ContaReceber = " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>ContaReceberHistorico</code> através do valor do atributo
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>ContaReceberHistoricoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM ContaReceberHistorico WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>ContaReceberHistoricoVO</code> resultantes da consulta.
     */
    public static List<ContaReceberHistoricoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List<ContaReceberHistoricoVO> vetResultado = new ArrayList<ContaReceberHistoricoVO>(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>ContaReceberHistoricoVO</code>.
     * @return  O objeto da classe <code>ContaReceberHistoricoVO</code> com os dados devidamente montados.
     */
    public static ContaReceberHistoricoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ContaReceberHistoricoVO obj = new ContaReceberHistoricoVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setContaReceber(new Integer(dadosSQL.getInt("ContaReceber")));
        obj.setValorRecebimento(new Double(dadosSQL.getDouble("valorRecebimento")));
        obj.setData(dadosSQL.getTimestamp("data"));
        obj.setMotivo(dadosSQL.getString("motivo"));
        obj.setNossoNumero(dadosSQL.getString("nossoNumero"));
        obj.getResponsavel().setCodigo(new Integer(dadosSQL.getInt("responsavel")));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        montarDadosResponsavel(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
        return obj;
    }

    public static void montarDadosResponsavel(ContaReceberHistoricoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getResponsavel() == null || obj.getResponsavel().getCodigo().intValue() == 0) {
            obj.setResponsavel(new UsuarioVO());
            return;
        }
        obj.setResponsavel(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavel().getCodigo(), nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por excluir todos os objetos da <code>ContaReceberHistoricoVO</code> no BD.
     * Faz uso da operação <code>excluir</code> disponível na classe <code>ContaReceberHistorico</code>.
     * @param <code>ContaReceber</code> campo chave para exclusão dos objetos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirContaReceberHistoricos(Integer contaReceber, UsuarioVO usuario) throws Exception {
        //   ContaReceberHistorico.excluir(getIdEntidade());
        String sql = "DELETE FROM ContaReceberHistorico WHERE (ContaReceber = ?)"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(sql, new Object[]{contaReceber});
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirContaReceberHistoricos(Integer contaReceber, List objeto, UsuarioVO usuario) throws Exception {
        // CtRPtRLog.excluir(getIdEntidade());
        String sql = "DELETE FROM ContaReceberHistorico WHERE (ContaReceber = ?) ";
        Iterator i = objeto.iterator();
        while (i.hasNext()) {
            ContaReceberHistoricoVO obj = (ContaReceberHistoricoVO) i.next();
            if (obj.getCodigo().intValue() != 0) {
                sql += " and codigo != " + obj.getCodigo();
            }
        }
        getConexao().getJdbcTemplate().update(sql+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario), new Object[]{contaReceber});
    }

    /**
     * Operação responsável por alterar todos os objetos da <code>ContaReceberHistoricoVO</code> contidos em um Hashtable no BD.
     * Faz uso da operação <code>excluirContaReceberHistoricos</code> e <code>incluirContaReceberHistoricos</code> disponíveis na classe <code>ContaReceberHistorico</code>.
     * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void alterarContaReceberHistoricos(Integer ContaReceber, List objetos, UsuarioVO usuario) throws Exception {
        //excluirContaReceberHistoricos( ContaReceber );
        incluirContaReceberHistoricos(ContaReceber, objetos, usuario);
    }

    /**
     * Operação responsável por incluir objetos da <code>ContaReceberHistoricoVO</code> no BD.
     * Garantindo o relacionamento com a entidade principal <code>financeiro.ContaReceber</code> através do atributo de vínculo.
     * @param objetos List contendo os objetos a serem gravados no BD da classe.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void incluirContaReceberHistoricos(Integer ContaReceberPrm, List objetos, UsuarioVO usuario) throws Exception {
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            ContaReceberHistoricoVO obj = (ContaReceberHistoricoVO) e.next();
            obj.setContaReceber(ContaReceberPrm);
            if (obj.getCodigo().intValue() == 0) {
                incluir(obj, usuario);
            }
        }
    }

    /**
     * Operação responsável por consultar todos os <code>ContaReceberHistoricoVO</code> relacionados a um objeto da classe <code>financeiro.ContaReceber</code>.
     * @param ContaReceber  Atributo de <code>financeiro.ContaReceber</code> a ser utilizado para localizar os objetos da classe <code>ContaReceberHistoricoVO</code>.
     * @return List  Contendo todos os objetos da classe <code>ContaReceberHistoricoVO</code> resultantes da consulta.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public static List consultarContaReceberHistoricos(Integer contaReceber, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        //  ContaReceberHistorico.consultar(getIdEntidade());
        List objetos = new ArrayList(0);
        String sql = "SELECT * FROM ContaReceberHistorico WHERE ContaReceber = ?";
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{contaReceber});
        while (resultado.next()) {
            ContaReceberHistoricoVO novoObj = new ContaReceberHistoricoVO();
            novoObj = ContaReceberHistorico.montarDados(resultado, nivelMontarDados, usuario);
            objetos.add(novoObj);
        }
        return objetos;
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>ContaReceberHistoricoVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public ContaReceberHistoricoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM ContaReceberHistorico WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( ContaReceberHistorico ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return ContaReceberHistorico.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        ContaReceberHistorico.idEntidade = idEntidade;
    }
}
