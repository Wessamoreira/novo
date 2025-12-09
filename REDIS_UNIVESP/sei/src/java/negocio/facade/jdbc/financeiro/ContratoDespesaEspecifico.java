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
import negocio.comuns.financeiro.ContratoDespesaEspecificoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.ContratoDespesaEspecificoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>ContratoDespesaEspecificoVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>ContratoDespesaEspecificoVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see ContratoDespesaEspecificoVO
 * @see ControleAcesso
 * @see ContratoDespesa
 */
@Repository
@Scope("singleton")
@Lazy 
public class ContratoDespesaEspecifico extends ControleAcesso implements ContratoDespesaEspecificoInterfaceFacade{

    protected static String idEntidade;

    public ContratoDespesaEspecifico() throws Exception {
        super();
        setIdEntidade("ContratosDespesas");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>ContratoDespesaEspecificoVO</code>.
     */
    public ContratoDespesaEspecificoVO novo() throws Exception {
        ContratoDespesaEspecifico.incluir(getIdEntidade());
        ContratoDespesaEspecificoVO obj = new ContratoDespesaEspecificoVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>ContratoDespesaEspecificoVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>ContratoDespesaEspecificoVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final ContratoDespesaEspecificoVO obj, UsuarioVO usuario) throws Exception {
        ContratoDespesaEspecificoVO.validarDados(obj);
		/**
		 * @author Leonardo Riciolle
		 * Comentado 23/10/2014
		 */
         // ContratoDespesaEspecifico.incluir(getIdEntidade());
        final String sql = "INSERT INTO ContratoDespesaEspecifico( contratoDespesa, dataVencimento, valorParcela, nrParcela, numeroDocumento ) VALUES ( ?, ?, ?, ?, ? ) returning codigo"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
                PreparedStatement sqlInserir = cnctn.prepareStatement(sql);
                if (obj.getContratoDespesa().intValue() != 0) {
                    sqlInserir.setInt(1, obj.getContratoDespesa().intValue());
                } else {
                    sqlInserir.setNull(1, 0);
                }
                sqlInserir.setDate(2, Uteis.getDataJDBC(obj.getDataVencimento()));
                sqlInserir.setDouble(3, obj.getValorParcela().doubleValue());                
                sqlInserir.setInt(4, obj.getNrParcela().intValue());
                sqlInserir.setString(5, obj.getNumeroDocumento());
                return sqlInserir;
            }
        }, new ResultSetExtractor<Integer>() {

            public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
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
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>ContratoDespesaEspecificoVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>ContratoDespesaEspecificoVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final ContratoDespesaEspecificoVO obj, UsuarioVO usuario) throws Exception {
        ContratoDespesaEspecificoVO.validarDados(obj);
        /**
		 * @author Leonardo Riciolle
		 * Comentado 23/10/2014
		 */
        // ContratoDespesaEspecifico.alterar(getIdEntidade());
        final String sql = "UPDATE ContratoDespesaEspecifico set contratoDespesa=?, dataVencimento=?, valorParcela=?, nrParcela=?, numeroDocumento =? WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        if(getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
                PreparedStatement sqlAlterar = cnctn.prepareStatement(sql);
                if (obj.getContratoDespesa().intValue() != 0) {
                    sqlAlterar.setInt(1, obj.getContratoDespesa().intValue());
                } else {
                    sqlAlterar.setNull(1, 0);
                }
                sqlAlterar.setDate(2, Uteis.getDataJDBC(obj.getDataVencimento()));
                sqlAlterar.setDouble(3, obj.getValorParcela().doubleValue());
                sqlAlterar.setInt(4, obj.getNrParcela().intValue());
                sqlAlterar.setString(5, obj.getNumeroDocumento());
                sqlAlterar.setInt(6, obj.getCodigo().intValue());
                return sqlAlterar;
            }
        })== 0){
        	incluir(obj, usuario);
        	return;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>ContratoDespesaEspecificoVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>ContratoDespesaEspecificoVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(ContratoDespesaEspecificoVO obj, UsuarioVO usuario) throws Exception {
    	/**
		 * @author Leonardo Riciolle
		 * Comentado 23/10/2014
		 */
    	//ContratoDespesaEspecifico.excluir(getIdEntidade());
        String sql = "DELETE FROM ContratoDespesaEspecifico WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
    }

    /**
     * Responsável por realizar uma consulta de <code>ContratoDespesaEspecifico</code> através do valor do atributo 
     * <code>codigo</code> da classe <code>ContratoDespesa</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>ContratoDespesaEspecificoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigoContratoDespesa(Integer valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), true, usuario);
        String sqlStr = "SELECT ContratoDespesaEspecifico.* FROM ContratoDespesaEspecifico, ContratoDespesa WHERE ContratoDespesaEspecifico.contratoDespesa = ContratoDespesa.codigo and ContratoDespesa.codigo >= " + valorConsulta.intValue() + " ORDER BY ContratoDespesa.codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados);
    }

    /**
     * Responsável por realizar uma consulta de <code>ContratoDespesaEspecifico</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>ContratoDespesaEspecificoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM ContratoDespesaEspecifico WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>ContratoDespesaEspecificoVO</code> resultantes da consulta.
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
     * em um objeto da classe <code>ContratoDespesaEspecificoVO</code>.
     * @return  O objeto da classe <code>ContratoDespesaEspecificoVO</code> com os dados devidamente montados.
     */
    public static ContratoDespesaEspecificoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados) throws Exception {
        ContratoDespesaEspecificoVO obj = new ContratoDespesaEspecificoVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setContratoDespesa(new Integer(dadosSQL.getInt("contratoDespesa")));
        obj.setDataVencimento(dadosSQL.getDate("dataVencimento"));
        obj.setValorParcela(new Double(dadosSQL.getDouble("valorParcela")));
        obj.setNrParcela(new Integer(dadosSQL.getInt("nrParcela")));
        obj.setNumeroDocumento(dadosSQL.getString("numeroDocumento"));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }

        return obj;
    }

    /**
     * Operação responsável por excluir todos os objetos da <code>ContratoDespesaEspecificoVO</code> no BD.
     * Faz uso da operação <code>excluir</code> disponível na classe <code>ContratoDespesaEspecifico</code>.
     * @param <code>contratoDespesa</code> campo chave para exclusão dos objetos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirContratoDespesaEspecificos(Integer contratoDespesa, List<ContratoDespesaEspecificoVO> objetos, UsuarioVO usuario) throws Exception {
        ContratoDespesaEspecifico.excluir(getIdEntidade());
        StringBuilder sql = new StringBuilder("DELETE FROM ContratoDespesaEspecifico WHERE (contratoDespesa = ?) and codigo not in (0 ");
        for(ContratoDespesaEspecificoVO contratoDespesaEspecificoVO: objetos) {
        	if(Uteis.isAtributoPreenchido(contratoDespesaEspecificoVO.getCodigo())) {
        		sql.append(",").append(contratoDespesaEspecificoVO.getCodigo());
        	}
        }
        sql.append(")").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
        getConexao().getJdbcTemplate().update(sql.toString(), new Object[]{contratoDespesa});
    }

    /**
     * Operação responsável por alterar todos os objetos da <code>ContratoDespesaEspecificoVO</code> contidos em um Hashtable no BD.
     * Faz uso da operação <code>excluirContratoDespesaEspecificos</code> e <code>incluirContratoDespesaEspecificos</code> disponíveis na classe <code>ContratoDespesaEspecifico</code>.
     * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarContratoDespesaEspecificos(Integer contratoDespesa, List<ContratoDespesaEspecificoVO> objetos, UsuarioVO usuario) throws Exception {
        excluirContratoDespesaEspecificos(contratoDespesa, objetos, usuario);
        for(ContratoDespesaEspecificoVO contratoDespesaEspecificoVO: objetos) {
        	contratoDespesaEspecificoVO.setContratoDespesa(contratoDespesa);
        	if(Uteis.isAtributoPreenchido(contratoDespesaEspecificoVO.getCodigo())) {
        		alterar(contratoDespesaEspecificoVO, usuario);
        	}else {
        		incluir(contratoDespesaEspecificoVO, usuario);
        	}
        }        
    }

    /**
     * Operação responsável por incluir objetos da <code>ContratoDespesaEspecificoVO</code> no BD.
     * Garantindo o relacionamento com a entidade principal <code>financeiro.ContratoDespesa</code> através do atributo de vínculo.
     * @param objetos List contendo os objetos a serem gravados no BD da classe.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirContratoDespesaEspecificos(Integer contratoDespesaPrm, List<ContratoDespesaEspecificoVO> objetos, UsuarioVO usuario) throws Exception {        
        Ordenacao.ordenarLista(objetos, "dataVencimento");
        int x = 1;
        for (Iterator<ContratoDespesaEspecificoVO> iterator = objetos.iterator(); iterator.hasNext();) {
		    ContratoDespesaEspecificoVO obj = iterator.next();
            obj.setContratoDespesa(contratoDespesaPrm);
            obj.setNrParcela(x);
            incluir(obj, usuario);
            x++;
        }
    }

    /**
     * Operação responsável por consultar todos os <code>ContratoDespesaEspecificoVO</code> relacionados a um objeto da classe <code>financeiro.ContratoDespesa</code>.
     * @param contratoDespesa  Atributo de <code>financeiro.ContratoDespesa</code> a ser utilizado para localizar os objetos da classe <code>ContratoDespesaEspecificoVO</code>.
     * @return List  Contendo todos os objetos da classe <code>ContratoDespesaEspecificoVO</code> resultantes da consulta.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public static List consultarContratoDespesaEspecificos(Integer contratoDespesa, int nivelMontarDados) throws Exception {
        ContratoDespesaEspecifico.consultar(getIdEntidade());
        List objetos = new ArrayList(0);
        String sql = "SELECT * FROM ContratoDespesaEspecifico WHERE contratoDespesa = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{contratoDespesa});
        while (tabelaResultado.next()) {
            ContratoDespesaEspecificoVO novoObj = new ContratoDespesaEspecificoVO();
            novoObj = ContratoDespesaEspecifico.montarDados(tabelaResultado, nivelMontarDados);
            objetos.add(novoObj);
        }
        return objetos;
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>ContratoDespesaEspecificoVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public ContratoDespesaEspecificoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM ContratoDespesaEspecifico WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( ContratoDespesaEspecifico ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return ContratoDespesaEspecifico.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        ContratoDespesaEspecifico.idEntidade = idEntidade;
    }
}
