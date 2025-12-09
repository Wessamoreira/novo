package negocio.facade.jdbc.compras;

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
import negocio.comuns.compras.ParcelaCondicaoPagamentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.compras.ParcelaCondicaoPagamentoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>ParcelaCondicaoPagamentoVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>ParcelaCondicaoPagamentoVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see ParcelaCondicaoPagamentoVO
 * @see ControleAcesso
 * @see CondicaoPagamento
 */
@Repository
@Scope("singleton")
@Lazy 
public class ParcelaCondicaoPagamento extends ControleAcesso implements ParcelaCondicaoPagamentoInterfaceFacade{

    protected static String idEntidade;

    public ParcelaCondicaoPagamento() throws Exception {
        super();
        setIdEntidade("CondicaoPagamento");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>ParcelaCondicaoPagamentoVO</code>.
     */
    public ParcelaCondicaoPagamentoVO novo() throws Exception {
        ParcelaCondicaoPagamento.incluir(getIdEntidade());
        ParcelaCondicaoPagamentoVO obj = new ParcelaCondicaoPagamentoVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>ParcelaCondicaoPagamentoVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>ParcelaCondicaoPagamentoVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @SuppressWarnings("static-access")
    public void incluir(final ParcelaCondicaoPagamentoVO obj) throws Exception {
        ParcelaCondicaoPagamentoVO.validarDados(obj);
        /**
         * @author Leonardo Riciolle 
         * Comentado 28/10/2014
         *  Classe Subordinada
         */ 
        // ParcelaCondicaoPagamento.incluir(getIdEntidade());
        obj.realizarUpperCaseDados();
        final String sql = "INSERT INTO ParcelaCondicaoPagamento( numeroParcela, percentualValor, intervalo, condicaoPagamento ) VALUES ( ?, ?, ?, ? ) returning codigo";
        obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                sqlInserir.setInt(1, obj.getNumeroParcela().intValue());
                sqlInserir.setDouble(2, obj.getPercentualValor().doubleValue());
                sqlInserir.setInt(3, obj.getIntervalo().intValue());
                if (obj.getCondicaoPagamento().intValue() != 0) {
                    sqlInserir.setInt(4, obj.getCondicaoPagamento().intValue());
                } else {
                    sqlInserir.setNull(4, 0);
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
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>ParcelaCondicaoPagamentoVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>ParcelaCondicaoPagamentoVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @SuppressWarnings("static-access")
    public void alterar(final ParcelaCondicaoPagamentoVO obj) throws Exception {
        ParcelaCondicaoPagamentoVO.validarDados(obj);
        /**
         * @author Leonardo Riciolle 
         * Comentado 28/10/2014
         *  Classe Subordinada
         */ 
        // ParcelaCondicaoPagamento.alterar(getIdEntidade());
        obj.realizarUpperCaseDados();
        final String sql = "UPDATE ParcelaCondicaoPagamento set numeroParcela=?, percentualValor=?, intervalo=?, condicaoPagamento=? WHERE ((codigo = ?))";
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                sqlAlterar.setInt(1, obj.getNumeroParcela().intValue());
                sqlAlterar.setDouble(2, obj.getPercentualValor().doubleValue());
                sqlAlterar.setInt(3, obj.getIntervalo().intValue());
                if (obj.getCondicaoPagamento().intValue() != 0) {
                    sqlAlterar.setInt(4, obj.getCondicaoPagamento().intValue());
                } else {
                    sqlAlterar.setNull(4, 0);
                }
                sqlAlterar.setInt(5, obj.getCodigo().intValue());
                return sqlAlterar;
            }
        });

    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>ParcelaCondicaoPagamentoVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>ParcelaCondicaoPagamentoVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @SuppressWarnings("static-access")
    public void excluir(ParcelaCondicaoPagamentoVO obj) throws Exception {
    	/**
    	  * @author Leonardo Riciolle 
    	  * Comentado 28/10/2014
    	  *  Classe Subordinada
    	  */ 
         // ParcelaCondicaoPagamento.excluir(getIdEntidade());
        String sql = "DELETE FROM ParcelaCondicaoPagamento WHERE ((codigo = ?))";
        getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
    }

    /**
     * Responsável por realizar uma consulta de <code>ParcelaCondicaoPagamento</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>ParcelaCondicaoPagamentoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    @SuppressWarnings("static-access")
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM ParcelaCondicaoPagamento WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>ParcelaCondicaoPagamentoVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>ParcelaCondicaoPagamentoVO</code>.
     * @return  O objeto da classe <code>ParcelaCondicaoPagamentoVO</code> com os dados devidamente montados.
     */
    public static ParcelaCondicaoPagamentoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados) throws Exception {
        ParcelaCondicaoPagamentoVO obj = new ParcelaCondicaoPagamentoVO();
        obj.setCodigo((dadosSQL.getInt("codigo")));
        obj.setNumeroParcela((dadosSQL.getInt("numeroParcela")));
        obj.setPercentualValor(new Double(dadosSQL.getDouble("percentualValor")));
        obj.setIntervalo((dadosSQL.getInt("intervalo")));
        obj.setCondicaoPagamento((dadosSQL.getInt("condicaoPagamento")));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }

        return obj;
    }

    /**
     * Operação responsável por excluir todos os objetos da <code>ParcelaCondicaoPagamentoVO</code> no BD.
     * Faz uso da operação <code>excluir</code> disponível na classe <code>ParcelaCondicaoPagamento</code>.
     * @param <code>condicaoPagamento</code> campo chave para exclusão dos objetos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @SuppressWarnings("static-access")
    public void excluirParcelaCondicaoPagamentos(Integer condicaoPagamento) throws Exception {
        ParcelaCondicaoPagamento.excluir(getIdEntidade());
        String sql = "DELETE FROM ParcelaCondicaoPagamento WHERE (condicaoPagamento = ?)";
        getConexao().getJdbcTemplate().update(sql, new Object[]{condicaoPagamento});
    }

    /**
     * Operação responsável por alterar todos os objetos da <code>ParcelaCondicaoPagamentoVO</code> contidos em um Hashtable no BD.
     * Faz uso da operação <code>excluirParcelaCondicaoPagamentos</code> e <code>incluirParcelaCondicaoPagamentos</code> disponíveis na classe <code>ParcelaCondicaoPagamento</code>.
     * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarParcelaCondicaoPagamentos(Integer condicaoPagamento, List objetos) throws Exception {
        excluirParcelaCondicaoPagamentos(condicaoPagamento);
        incluirParcelaCondicaoPagamentos(condicaoPagamento, objetos);
    }

    /**
     * Operação responsável por incluir objetos da <code>ParcelaCondicaoPagamentoVO</code> no BD.
     * Garantindo o relacionamento com a entidade principal <code>financeiro.CondicaoPagamento</code> através do atributo de vínculo.
     * @param objetos List contendo os objetos a serem gravados no BD da classe.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirParcelaCondicaoPagamentos(Integer condicaoPagamentoPrm, List objetos) throws Exception {
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            ParcelaCondicaoPagamentoVO obj = (ParcelaCondicaoPagamentoVO) e.next();
            ParcelaCondicaoPagamentoVO.validarDados(obj);
            obj.setCondicaoPagamento(condicaoPagamentoPrm);
            incluir(obj);
        }
    }

    
    public List<ParcelaCondicaoPagamentoVO> consultarParcelaCondicaoPagamentos(Integer condicaoPagamento, int nivelMontarDados) throws Exception {
        ParcelaCondicaoPagamento.consultar(getIdEntidade());
        List objetos = new ArrayList(0);
        String sql = "SELECT * FROM ParcelaCondicaoPagamento WHERE condicaoPagamento = ? order by numeroParcela";
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql,condicaoPagamento);
        while (resultado.next()) {
            objetos.add(ParcelaCondicaoPagamento.montarDados(resultado, nivelMontarDados));
        }
        return objetos;
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>ParcelaCondicaoPagamentoVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    @SuppressWarnings("static-access")
    public ParcelaCondicaoPagamentoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM ParcelaCondicaoPagamento WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( ParcelaCondicaoPagamento ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return ParcelaCondicaoPagamento.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        ParcelaCondicaoPagamento.idEntidade = idEntidade;
    }
}