package negocio.facade.jdbc.compras;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
import negocio.comuns.compras.UnidadeMedidaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.compras.UnidadeMedidaInterfaceFacade;


@Repository
@Scope("singleton")
@Lazy
public class UnidadeMedida extends ControleAcesso implements UnidadeMedidaInterfaceFacade {

    /**
	 * 
	 */
	private static final long serialVersionUID = -6908515946145317854L;
	protected static String idEntidade;

    
    public UnidadeMedida() {    
        setIdEntidade("UnidadeMedida");
    }
    
    public void verificarExistenciaUnidade(String nomeCat, Integer codigo) throws Exception {
        String sqlStr = "SELECT * FROM UnidadeMedida WHERE lower (nome) = ('" + nomeCat.toLowerCase() + "') ";
        if (codigo.intValue() != 0) {
            sqlStr += " and codigo != " + codigo.intValue();
        }

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        if (tabelaResultado.next()) {
            throw new Exception("Já existe uma Unidade de Medida cadastrada com este nome.");
        }
    }

   
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)   
    public void incluir(final UnidadeMedidaVO obj) throws Exception {
        try {
            UnidadeMedidaVO.validarDados(obj);
            UnidadeMedida.incluir(getIdEntidade());
            verificarExistenciaUnidade(obj.getNome(), 0);
            final String sql = "INSERT INTO UnidadeMedida ( nome, descricao, sigla, fracionado ) VALUES ( ?, ?, ?, ? )returning codigo";
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    sqlInserir.setString(1, obj.getNome());
                    sqlInserir.setString(2, obj.getDescricao());
                    sqlInserir.setString(3, obj.getSigla());
                    Uteis.setValuePreparedStatement(obj.isFracionado(), 4, sqlInserir);
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
            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>UnidadeMedidaVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>UnidadeMedidaVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)   
    public void alterar(final UnidadeMedidaVO obj) throws Exception {
        try {
            UnidadeMedidaVO.validarDados(obj);
            UnidadeMedida.alterar(getIdEntidade());
            verificarExistenciaUnidade(obj.getNome(), obj.getCodigo());
            final String sql = "UPDATE UnidadeMedida set nome=?, descricao=?, sigla=?, fracionado=? WHERE (codigo = ?)";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setString(1, obj.getNome());
                    sqlAlterar.setString(2, obj.getDescricao());
                    sqlAlterar.setString(3, obj.getSigla());
                    Uteis.setValuePreparedStatement(obj.isFracionado(), 4, sqlAlterar);
                    Uteis.setValuePreparedStatement(obj.getCodigo(), 5, sqlAlterar);
                    return sqlAlterar;
                }
            });

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>UnidadeMedidaVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>UnidadeMedidaVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)   
    public void excluir(UnidadeMedidaVO obj) throws Exception {
        try {
            UnidadeMedida.excluir(getIdEntidade());
            String sql = "DELETE FROM UnidadeMedida WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>UnidadeMedida</code> através do valor do atributo
     * <code>String tipoUnidade</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>UnidadeMedidaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
   
    public List<UnidadeMedidaVO> consultarPorDescricao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM UnidadeMedida WHERE lower (descricao) like('" + valorConsulta.toLowerCase() + "%') ORDER BY descricao";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>UnidadeMedida</code> através do valor do atributo
     * <code>nome</code> da classe <code>CategoriaProduto</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>UnidadeMedidaVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
   
    public List<UnidadeMedidaVO> consultarPorNome(String valorConsulta, boolean controlaAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlaAcesso, usuario);
        String sqlStr = "SELECT UnidadeMedida.* FROM UnidadeMedida WHERE lower (nome) like('" + valorConsulta.toLowerCase() + "%') ORDER BY nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>UnidadeMedida</code> através do valor do atributo
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>UnidadeMedidaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
   
    public List<UnidadeMedidaVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM UnidadeMedida WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>UnidadeMedidaVO</code> resultantes da consulta.
     */
    public static List<UnidadeMedidaVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List<UnidadeMedidaVO> vetResultado = new ArrayList<>(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>UnidadeMedidaVO</code>.
     * @return  O objeto da classe <code>UnidadeMedidaVO</code> com os dados devidamente montados.
     */
    public static UnidadeMedidaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        UnidadeMedidaVO obj = new UnidadeMedidaVO();
        obj.setNovoObj(Boolean.FALSE);
        obj.setCodigo((dadosSQL.getInt("codigo")));
        obj.setNome(dadosSQL.getString("nome"));
        obj.setDescricao(dadosSQL.getString("descricao"));
        obj.setSigla(dadosSQL.getString("sigla"));
        obj.setFracionado(dadosSQL.getBoolean("fracionado"));
        return obj;
    }


    /**
     * Operação responsável por localizar um objeto da classe <code>UnidadeMedidaVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public UnidadeMedidaVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sql = "SELECT * FROM UnidadeMedida WHERE codigo = ?";

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, codigoPrm);
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return UnidadeMedida.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        UnidadeMedida.idEntidade = idEntidade;
    }

}
