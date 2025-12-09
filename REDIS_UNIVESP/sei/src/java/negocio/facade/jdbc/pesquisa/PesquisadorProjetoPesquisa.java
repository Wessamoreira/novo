package negocio.facade.jdbc.pesquisa;
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
import negocio.comuns.pesquisa.PesquisadorLinhaPesquisaVO;
import negocio.comuns.pesquisa.PesquisadorProjetoPesquisaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.pesquisa.PesquisadorProjetoPesquisaInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>PesquisadorProjetoPesquisaVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>PesquisadorProjetoPesquisaVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see PesquisadorProjetoPesquisaVO
 * @see ControleAcesso
 * @see ProjetoPesquisa
*/
@Repository @Scope("singleton") @Lazy 
public class PesquisadorProjetoPesquisa extends ControleAcesso implements PesquisadorProjetoPesquisaInterfaceFacade{
	
    protected static String idEntidade;
	
    public PesquisadorProjetoPesquisa() throws Exception {
        super();
        setIdEntidade("PesquisadorProjetoPesquisa");
    }
	
    /**
     * Operação responsável por retornar um novo objeto da classe <code>PesquisadorProjetoPesquisaVO</code>.
    */
    public PesquisadorProjetoPesquisaVO novo() throws Exception {
        PesquisadorProjetoPesquisa.incluir(getIdEntidade());
        PesquisadorProjetoPesquisaVO obj = new PesquisadorProjetoPesquisaVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>PesquisadorProjetoPesquisaVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>PesquisadorProjetoPesquisaVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
    */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final PesquisadorProjetoPesquisaVO obj) throws Exception {
        try {
    
        PesquisadorProjetoPesquisaVO.validarDados(obj);
        final String sql = "INSERT INTO PesquisadorProjetoPesquisa( pesquisadorLinhaPesquisa, projetoPesquisa ) VALUES ( ?, ? )";
        obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
        sqlInserir.setInt( 1, obj.getPesquisadorLinhaPesquisa().getCodigo().intValue() );
        sqlInserir.setInt( 2, obj.getProjetoPesquisa().intValue() );
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
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>PesquisadorProjetoPesquisaVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>PesquisadorProjetoPesquisaVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
    */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final PesquisadorProjetoPesquisaVO obj) throws Exception {
        try{

        PesquisadorProjetoPesquisaVO.validarDados(obj);
        final String sql = "UPDATE PesquisadorProjetoPesquisa set pesquisadorLinhaPesquisa=?, projetoPesquisa=? WHERE ((codigo = ?))";
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
        sqlAlterar.setInt( 1, obj.getPesquisadorLinhaPesquisa().getCodigo().intValue() );
        sqlAlterar.setInt( 2, obj.getProjetoPesquisa().intValue() );
        sqlAlterar.setInt( 3, obj.getCodigo().intValue() );
        return sqlAlterar;
				}
			});

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>PesquisadorProjetoPesquisaVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>PesquisadorProjetoPesquisaVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
    */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(PesquisadorProjetoPesquisaVO obj) throws Exception {
        try{
        PesquisadorProjetoPesquisa.excluir(getIdEntidade());
        String sql = "DELETE FROM PesquisadorProjetoPesquisa WHERE ((codigo = ?))";
        getConexao().getJdbcTemplate().update(sql, new Object[] {obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>PesquisadorProjetoPesquisa</code> através do valor do atributo 
     * <code>nome</code> da classe <code>ProjetoPesquisa</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>PesquisadorProjetoPesquisaVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
    */
    public List consultarPorNomeProjetoPesquisa(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT PesquisadorProjetoPesquisa.* FROM PesquisadorProjetoPesquisa, ProjetoPesquisa WHERE PesquisadorProjetoPesquisa.projetoPesquisa = ProjetoPesquisa.codigo and ProjetoPesquisa.nome like('" + valorConsulta + "%') ORDER BY ProjetoPesquisa.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado,usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>PesquisadorProjetoPesquisa</code> através do valor do atributo 
     * <code>codigo</code> da classe <code>PesquisadorLinhaPesquisa</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>PesquisadorProjetoPesquisaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
    */
    public List consultarPorCodigoPesquisadorLinhaPesquisa(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT PesquisadorProjetoPesquisa.* FROM PesquisadorProjetoPesquisa, PesquisadorLinhaPesquisa WHERE PesquisadorProjetoPesquisa.pesquisadorLinhaPesquisa = PesquisadorLinhaPesquisa.codigo and PesquisadorLinhaPesquisa.codigo >= " + valorConsulta.intValue() + " ORDER BY PesquisadorLinhaPesquisa.codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado,usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>PesquisadorProjetoPesquisa</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>PesquisadorProjetoPesquisaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso,int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM PesquisadorProjetoPesquisa WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado,usuario));
        }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>PesquisadorProjetoPesquisaVO</code> resultantes da consulta.
    */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado,UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado,Uteis.NIVELMONTARDADOS_TODOS,usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>PesquisadorProjetoPesquisaVO</code>.
     * @return  O objeto da classe <code>PesquisadorProjetoPesquisaVO</code> com os dados devidamente montados.
    */
    public static PesquisadorProjetoPesquisaVO montarDados(SqlRowSet dadosSQL,int nivelMontarDados,UsuarioVO usuario) throws Exception {
        PesquisadorProjetoPesquisaVO obj = new PesquisadorProjetoPesquisaVO();
        obj.setCodigo( new Integer( dadosSQL.getInt("codigo")));
        obj.getPesquisadorLinhaPesquisa().setCodigo( new Integer( dadosSQL.getInt("pesquisadorLinhaPesquisa")));
        obj.setProjetoPesquisa( new Integer( dadosSQL.getInt("projetoPesquisa")));
        obj.setNovoObj(Boolean.FALSE);
        if(nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS){
            return obj;
        }
        montarDadosPesquisadorLinhaPesquisa(obj, nivelMontarDados,usuario);
        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>PesquisadorLinhaPesquisaVO</code> relacionado ao objeto <code>PesquisadorProjetoPesquisaVO</code>.
     * Faz uso da chave primária da classe <code>PesquisadorLinhaPesquisaVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
    */
    public static void montarDadosPesquisadorLinhaPesquisa(PesquisadorProjetoPesquisaVO obj,int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if (obj.getPesquisadorLinhaPesquisa().getCodigo().intValue() == 0) {
            obj.setPesquisadorLinhaPesquisa(new PesquisadorLinhaPesquisaVO());
            return;
        }
        obj.setPesquisadorLinhaPesquisa(getFacadeFactory().getPesquisadorLinhaPesquisaFacade().consultarPorChavePrimaria( obj.getPesquisadorLinhaPesquisa().getCodigo(), nivelMontarDados,usuario ));
    }

    /**
     * Operação responsável por excluir todos os objetos da <code>PesquisadorProjetoPesquisaVO</code> no BD.
     * Faz uso da operação <code>excluir</code> disponível na classe <code>PesquisadorProjetoPesquisa</code>.
     * @param <code>projetoPesquisa</code> campo chave para exclusão dos objetos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
    */
    public void excluirPesquisadorProjetoPesquisas( Integer projetoPesquisa ) throws Exception {
        PesquisadorProjetoPesquisa.excluir(getIdEntidade());
        String sqlStr = "DELETE FROM PesquisadorProjetoPesquisa WHERE (projetoPesquisa = ?)";
       getConexao().getJdbcTemplate().update(sqlStr, new Object[] { projetoPesquisa });
    }

    /**
     * Operação responsável por alterar todos os objetos da <code>PesquisadorProjetoPesquisaVO</code> contidos em um Hashtable no BD.
     * Faz uso da operação <code>excluirPesquisadorProjetoPesquisas</code> e <code>incluirPesquisadorProjetoPesquisas</code> disponíveis na classe <code>PesquisadorProjetoPesquisa</code>.
     * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
    */
    public void alterarPesquisadorProjetoPesquisas( Integer projetoPesquisa, List objetos ) throws Exception {
        excluirPesquisadorProjetoPesquisas( projetoPesquisa );
        incluirPesquisadorProjetoPesquisas( projetoPesquisa, objetos );
    }

    /**
     * Operação responsável por incluir objetos da <code>PesquisadorProjetoPesquisaVO</code> no BD.
     * Garantindo o relacionamento com a entidade principal <code>pesquisa.ProjetoPesquisa</code> através do atributo de vínculo.
     * @param objetos List contendo os objetos a serem gravados no BD da classe.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
    */
    public void incluirPesquisadorProjetoPesquisas( Integer projetoPesquisaPrm, List objetos ) throws Exception {
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            PesquisadorProjetoPesquisaVO obj = (PesquisadorProjetoPesquisaVO)e.next();
            obj.setProjetoPesquisa(projetoPesquisaPrm);
            incluir(obj);
        }
    }

    /**
     * Operação responsável por consultar todos os <code>PesquisadorProjetoPesquisaVO</code> relacionados a um objeto da classe <code>pesquisa.ProjetoPesquisa</code>.
     * @param projetoPesquisa  Atributo de <code>pesquisa.ProjetoPesquisa</code> a ser utilizado para localizar os objetos da classe <code>PesquisadorProjetoPesquisaVO</code>.
     * @return List  Contendo todos os objetos da classe <code>PesquisadorProjetoPesquisaVO</code> resultantes da consulta.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
    */
    public static List consultarPesquisadorProjetoPesquisas(Integer projetoPesquisa, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        PesquisadorProjetoPesquisa.consultar(getIdEntidade(), controlarAcesso,usuario);
        List objetos = new ArrayList(0);
        String sqlStr = "SELECT * FROM PesquisadorProjetoPesquisa WHERE projetoPesquisa = ?";
       SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        
        while (tabelaResultado.next()) {
            objetos.add(PesquisadorProjetoPesquisa.montarDados(tabelaResultado, nivelMontarDados,usuario));
        }
        return objetos;
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>PesquisadorProjetoPesquisaVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
    */
    public PesquisadorProjetoPesquisaVO consultarPorChavePrimaria( Integer codigoPrm ,int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false,usuario);
        String sqlStr = "SELECT * FROM PesquisadorProjetoPesquisa WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);

        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado, nivelMontarDados,usuario));
    }
	

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
    */
    public static String getIdEntidade() {
        return PesquisadorProjetoPesquisa.idEntidade;
    }
     
    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
    */
    public void setIdEntidade( String idEntidade ) {
        PesquisadorProjetoPesquisa.idEntidade = idEntidade;
    }
}