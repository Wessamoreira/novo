package negocio.facade.jdbc.pesquisa;
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
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.pesquisa.AreaConhecimentoVO;
import negocio.comuns.pesquisa.PesquisadorConvidadoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.pesquisa.PesquisadorConvidadoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>PesquisadorConvidadoVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>PesquisadorConvidadoVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see PesquisadorConvidadoVO
 * @see ControleAcesso
*/
@Repository @Scope("singleton") @Lazy 
public class PesquisadorConvidado extends ControleAcesso implements PesquisadorConvidadoInterfaceFacade {
	
    protected static String idEntidade;
	
    public PesquisadorConvidado() throws Exception {
        super();
        setIdEntidade("PesquisadorConvidado");
    }
	
    /**
     * Operação responsável por retornar um novo objeto da classe <code>PesquisadorConvidadoVO</code>.
    */
    public PesquisadorConvidadoVO novo() throws Exception {
        PesquisadorConvidado.incluir(getIdEntidade());
        PesquisadorConvidadoVO obj = new PesquisadorConvidadoVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>PesquisadorConvidadoVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>PesquisadorConvidadoVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
    */
    public void incluir(final PesquisadorConvidadoVO obj) throws Exception {
        try {
            
            PesquisadorConvidadoVO.validarDados(obj);
            PesquisadorConvidado.incluir(getIdEntidade());
            final String sql = "INSERT INTO PesquisadorConvidado( codigo, areaConhecimento, pessoa ) VALUES ( ?, ?, ? )returning codigo";
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
            sqlInserir.setInt( 1, obj.getCodigo().intValue() );
            sqlInserir.setInt( 2, obj.getAreaConhecimento().getCodigo().intValue() );
            sqlInserir.setInt( 3, obj.getPessoa().getCodigo().intValue() );
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
        	obj.setNovoObj(true);
        	throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>PesquisadorConvidadoVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>PesquisadorConvidadoVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
    */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final PesquisadorConvidadoVO obj) throws Exception {
        try {
            
            PesquisadorConvidadoVO.validarDados(obj);
            PesquisadorConvidado.alterar(getIdEntidade());
            final String sql = "UPDATE PesquisadorConvidado set areaConhecimento=?, pessoa=? WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
            sqlAlterar.setInt( 1, obj.getAreaConhecimento().getCodigo().intValue() );
            sqlAlterar.setInt( 2, obj.getPessoa().getCodigo().intValue() );
            sqlAlterar.setInt( 3, obj.getCodigo().intValue() );
            return sqlAlterar;
				}
			});

        } catch (Exception e) {
            throw e;
        }
    }
    /**
     * Operação responsável por excluir no BD um objeto da classe <code>PesquisadorConvidadoVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>PesquisadorConvidadoVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
    */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(PesquisadorConvidadoVO obj) throws Exception {
        try {
            PesquisadorConvidado.excluir(getIdEntidade());
            String sql = "DELETE FROM PesquisadorConvidado WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql, new Object[] {obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }


    /**
     * Responsável por realizar uma consulta de <code>PesquisadorConvidado</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>PesquisadorConvidadoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM PesquisadorConvidado WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado,usuario));
        }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>PesquisadorConvidadoVO</code> resultantes da consulta.
    */
 public static List montarDadosConsulta(SqlRowSet tabelaResultado,UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado,usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>PesquisadorConvidadoVO</code>.
     * @return  O objeto da classe <code>PesquisadorConvidadoVO</code> com os dados devidamente montados.
    */
    public static PesquisadorConvidadoVO montarDados(SqlRowSet dadosSQL,UsuarioVO usuario) throws Exception {
        PesquisadorConvidadoVO obj = new PesquisadorConvidadoVO();
        obj.setCodigo( new Integer( dadosSQL.getInt("codigo")));
        obj.getAreaConhecimento().setCodigo( new Integer( dadosSQL.getInt("areaConhecimento")));
        obj.getPessoa().setCodigo( new Integer( dadosSQL.getInt("pessoa")));
        obj.setNovoObj(Boolean.FALSE);

        montarDadosAreaConhecimento(obj,usuario);
        montarDadosPessoa(obj,usuario);
        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>PessoaVO</code> relacionado ao objeto <code>PesquisadorConvidadoVO</code>.
     * Faz uso da chave primária da classe <code>PessoaVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
    */
    public static void montarDadosPessoa(PesquisadorConvidadoVO obj,UsuarioVO usuario) throws Exception {
        if (obj.getPessoa().getCodigo().intValue() == 0) {
            obj.setPessoa(new PessoaVO());
            return;
        }
        obj.setPessoa(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria( obj.getPessoa().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,usuario ));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>AreaConhecimentoVO</code> relacionado ao objeto <code>PesquisadorConvidadoVO</code>.
     * Faz uso da chave primária da classe <code>AreaConhecimentoVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
    */
    public static void montarDadosAreaConhecimento(PesquisadorConvidadoVO obj,UsuarioVO usuario) throws Exception {
        if (obj.getAreaConhecimento().getCodigo().intValue() == 0) {
            obj.setAreaConhecimento(new AreaConhecimentoVO());
            return;
        }
        obj.setAreaConhecimento(getFacadeFactory().getAreaConhecimentoFacade().consultarPorChavePrimaria( obj.getAreaConhecimento().getCodigo(),usuario ));
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>PesquisadorConvidadoVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
    */
    public PesquisadorConvidadoVO consultarPorChavePrimaria( Integer codigoPrm,UsuarioVO usuario ) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false,usuario);
        String sqlStr = "SELECT * FROM PesquisadorConvidado WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado,usuario));
    }
	

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
    */
    public static String getIdEntidade() {
        return PesquisadorConvidado.idEntidade;
    }
     
    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
    */
    public void setIdEntidade( String idEntidade ) {
        PesquisadorConvidado.idEntidade = idEntidade;
    }
}