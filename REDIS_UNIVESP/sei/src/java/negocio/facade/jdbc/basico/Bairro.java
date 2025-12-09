package negocio.facade.jdbc.basico;
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
import negocio.comuns.basico.BairroVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.basico.BairroInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>BairroVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>BairroVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see BairroVO
 * @see ControleAcesso
*/

@Repository @Scope("singleton") @Lazy 
public class Bairro extends ControleAcesso implements BairroInterfaceFacade {
	
    protected static String idEntidade;
	
    public Bairro() throws Exception {
        super();
        setIdEntidade("Bairro");
    }
	
    /**
     * Operação responsável por retornar um novo objeto da classe <code>BairroVO</code>.
    */
    public BairroVO novo() throws Exception {
        Bairro.incluir(getIdEntidade());
        BairroVO obj = new BairroVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>BairroVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>BairroVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
    */    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final BairroVO obj) throws Exception {
        try {            
            BairroVO.validarDados(obj);
            obj.realizarUpperCaseDados();
            final String sql = "INSERT INTO Bairro( cidade, descricao ) VALUES ( ?, ? ) returning codigo";
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
              public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlInserir = arg0.prepareStatement(sql);

                if (obj.getCidade().getCodigo().intValue() != 0) {
                    sqlInserir.setInt(1, obj.getCidade().getCodigo().intValue() );
                } else {
                    sqlInserir.setNull(1, 0);
                }
                sqlInserir.setString( 2, obj.getDescricao() );
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
            
        } catch (Exception e) {
            obj.setNovoObj(Boolean.TRUE);
            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>BairroVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>BairroVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
    */

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final BairroVO obj) throws Exception {
        try {
            BairroVO.validarDados(obj);
            Bairro.alterar(getIdEntidade());
            obj.realizarUpperCaseDados();
            final String sql = "UPDATE Bairro set cidade=?, descricao=? WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
               public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
       		   PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                   if (obj.getCidade().getCodigo().intValue() != 0) {
                       sqlAlterar.setInt(1, obj.getCidade().getCodigo().intValue() );
                   } else {
                       sqlAlterar.setNull(1, 0);
                   }
                   sqlAlterar.setString( 2, obj.getDescricao() );
                   sqlAlterar.setInt( 3, obj.getCodigo().intValue() );
                   return sqlAlterar;
               }

            });

        } catch (Exception e) {
            throw e;
        }

    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>BairroVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>BairroVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
    */


    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(BairroVO obj) throws Exception {
        try {
            Bairro.excluir(getIdEntidade());
            String sql = "DELETE FROM Bairro WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql, new Object[] {obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>Bairro</code> através do valor do atributo 
     * <code>String descricao</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>BairroVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public List consultarPorDescricao(String valorConsulta, CidadeVO cidade, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        if(cidade == null || cidade.getCodigo() == null || cidade.getCodigo().equals(0)){
            return new ArrayList(0);
        }
        String sqlStr = "SELECT * FROM bairro WHERE trim(sem_acentos(bairro.descricao)) ilike(trim(sem_acentos(?))) and bairro.cidade = " + cidade.getCodigo() + " ORDER BY bairro.descricao";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta + PERCENT);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>Bairro</code> através do valor do atributo 
     * <code>nome</code> da classe <code>Cidade</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>BairroVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
    */
    public List consultarPorNomeCidade(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT Bairro.* FROM Bairro, Cidade WHERE Bairro.cidade = Cidade.codigo and upper( Cidade.nome ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY Cidade.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>Bairro</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>BairroVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Bairro WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario);
        }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>BairroVO</code> resultantes da consulta.
    */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            BairroVO obj = new BairroVO();
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados,usuario));
        }
        tabelaResultado = null;
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>BairroVO</code>.
     * @return  O objeto da classe <code>BairroVO</code> com os dados devidamente montados.
    */
    public static BairroVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        BairroVO obj = new BairroVO();
        obj.setCodigo( new Integer( dadosSQL.getInt("codigo")));
        obj.getCidade().setCodigo( new Integer( dadosSQL.getInt("cidade")));
        obj.setDescricao( dadosSQL.getString("descricao"));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        montarDadosCidade(obj, nivelMontarDados,usuario);
        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>CidadeVO</code> relacionado ao objeto <code>BairroVO</code>.
     * Faz uso da chave primária da classe <code>CidadeVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
    */
    public static void montarDadosCidade(BairroVO obj, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if (obj.getCidade().getCodigo().intValue() == 0) {
            obj.setCidade(new CidadeVO());
            return;
        }
        obj.setCidade(getFacadeFactory().getCidadeFacade().consultarPorChavePrimaria(obj.getCidade().getCodigo(), false,usuario));
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>BairroVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
    */
    public BairroVO consultarPorChavePrimaria( Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario ) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM Bairro WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( Bairro ).");
        }
        return montarDados(tabelaResultado, nivelMontarDados,usuario);
    }
	

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
    */
    public static String getIdEntidade() {
        return Bairro.idEntidade;
    }
     
    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
    */
    public void setIdEntidade( String idEntidade ) {
        Bairro.idEntidade = idEntidade;
    }
}