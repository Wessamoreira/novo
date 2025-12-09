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

import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.pesquisa.AreaConhecimentoVO;
import negocio.comuns.pesquisa.GrupoPesquisaVO;
import negocio.comuns.pesquisa.LinhaPesquisaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.pesquisa.LinhaPesquisaInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>LinhaPesquisaVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>LinhaPesquisaVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see LinhaPesquisaVO
 * @see ControleAcesso
*/

@Repository @Scope("singleton") @Lazy 
public class LinhaPesquisa extends ControleAcesso implements LinhaPesquisaInterfaceFacade {
	
    protected static String idEntidade;
	
    public LinhaPesquisa() throws Exception {
        super();
        setIdEntidade("LinhaPesquisa");
    }
	
    /**
     * Operação responsável por retornar um novo objeto da classe <code>LinhaPesquisaVO</code>.
    */
    public LinhaPesquisaVO novo() throws Exception {
        LinhaPesquisa.incluir(getIdEntidade());
        LinhaPesquisaVO obj = new LinhaPesquisaVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>LinhaPesquisaVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>LinhaPesquisaVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
    */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final LinhaPesquisaVO obj) throws Exception {
        try {
            
            LinhaPesquisaVO.validarDados(obj);
            LinhaPesquisa.incluir(getIdEntidade());
            final String sql = "INSERT INTO LinhaPesquisa( nome, descricao, objetivos, lider, grupoPesquisa, palavrasChaves, setoresAplicacao, areaConhecimento ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ? )returning codigo";
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
            sqlInserir.setString( 1, obj.getNome() );
            sqlInserir.setString( 2, obj.getDescricao() );
            sqlInserir.setString( 3, obj.getObjetivos() );
            sqlInserir.setInt( 4, obj.getLider().getCodigo().intValue() );
            sqlInserir.setInt( 5, obj.getGrupoPesquisa().getCodigo().intValue() );
            sqlInserir.setString( 6, obj.getPalavrasChaves() );
            sqlInserir.setString( 7, obj.getSetoresAplicacao() );
            sqlInserir.setInt( 8, obj.getAreaConhecimento().getCodigo().intValue() );
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
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>LinhaPesquisaVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>LinhaPesquisaVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
    */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final LinhaPesquisaVO obj) throws Exception {
        try {
            
            LinhaPesquisaVO.validarDados(obj);
            LinhaPesquisa.alterar(getIdEntidade());
            final String sql = "UPDATE LinhaPesquisa set nome=?, descricao=?, objetivos=?, lider=?, grupoPesquisa=?, palavrasChaves=?, setoresAplicacao=?, areaConhecimento=? WHERE ((codigo = ?))";
           getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
            sqlAlterar.setString( 1, obj.getNome() );
            sqlAlterar.setString( 2, obj.getDescricao() );
            sqlAlterar.setString( 3, obj.getObjetivos() );
            sqlAlterar.setInt( 4, obj.getLider().getCodigo().intValue() );
            sqlAlterar.setInt( 5, obj.getGrupoPesquisa().getCodigo().intValue() );
            sqlAlterar.setString( 6, obj.getPalavrasChaves() );
            sqlAlterar.setString( 7, obj.getSetoresAplicacao() );
            sqlAlterar.setInt( 8, obj.getAreaConhecimento().getCodigo().intValue() );
            sqlAlterar.setInt( 9, obj.getCodigo().intValue() );
         return sqlAlterar;
				}
			});

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>LinhaPesquisaVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>LinhaPesquisaVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
    */
    public void excluir(LinhaPesquisaVO obj) throws Exception {
        try {
            
            LinhaPesquisa.excluir(getIdEntidade());
            String sql = "DELETE FROM LinhaPesquisa WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql, new Object[] {obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>LinhaPesquisa</code> através do valor do atributo 
     * <code>nome</code> da classe <code>AreaConhecimento</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>LinhaPesquisaVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
    */
    public List consultarPorNomeAreaConhecimento(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT LinhaPesquisa.* FROM LinhaPesquisa, AreaConhecimento WHERE LinhaPesquisa.areaConhecimento = AreaConhecimento.codigo and AreaConhecimento.nome like('" + valorConsulta + "%') ORDER BY AreaConhecimento.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado,usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>LinhaPesquisa</code> através do valor do atributo 
     * <code>nome</code> da classe <code>GrupoPesquisa</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>LinhaPesquisaVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
    */
    public List consultarPorNomeGrupoPesquisa(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT LinhaPesquisa.* FROM LinhaPesquisa, GrupoPesquisa WHERE LinhaPesquisa.grupoPesquisa = GrupoPesquisa.codigo and GrupoPesquisa.nome like('" + valorConsulta + "%') ORDER BY GrupoPesquisa.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado,usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>LinhaPesquisa</code> através do valor do atributo 
     * <code>codigo</code> da classe <code>Funcionario</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>LinhaPesquisaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
    */
    public List consultarPorCodigoFuncionario(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT LinhaPesquisa.* FROM LinhaPesquisa, Funcionario WHERE LinhaPesquisa.lider = Funcionario.codigo and Funcionario.codigo >= " + valorConsulta.intValue() + " ORDER BY Funcionario.codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado,usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>LinhaPesquisa</code> através do valor do atributo 
     * <code>String nome</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>LinhaPesquisaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNome(String valorConsulta, boolean controlarAcesso,int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM LinhaPesquisa WHERE nome like('" + valorConsulta + "%') ORDER BY nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado,usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>LinhaPesquisa</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>LinhaPesquisaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso,int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM LinhaPesquisa WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado,usuario));
        }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>LinhaPesquisaVO</code> resultantes da consulta.
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
     * em um objeto da classe <code>LinhaPesquisaVO</code>.
     * @return  O objeto da classe <code>LinhaPesquisaVO</code> com os dados devidamente montados.
    */
    public static LinhaPesquisaVO montarDados(SqlRowSet dadosSQL,int nivelMontarDados,UsuarioVO usuario) throws Exception {
        LinhaPesquisaVO obj = new LinhaPesquisaVO();
        obj.setCodigo( new Integer( dadosSQL.getInt("codigo")));
        obj.setNome( dadosSQL.getString("nome"));
        obj.setDescricao( dadosSQL.getString("descricao"));
        obj.setObjetivos( dadosSQL.getString("objetivos"));
        obj.getLider().setCodigo( new Integer( dadosSQL.getInt("lider")));
        obj.getGrupoPesquisa().setCodigo( new Integer( dadosSQL.getInt("grupoPesquisa")));
        obj.setPalavrasChaves( dadosSQL.getString("palavrasChaves"));
        obj.setSetoresAplicacao( dadosSQL.getString("setoresAplicacao"));
        obj.getAreaConhecimento().setCodigo( new Integer( dadosSQL.getInt("areaConhecimento")));
        obj.setNovoObj(Boolean.FALSE);
        if(nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS){
            return obj;
        }
        montarDadosLider(obj, nivelMontarDados,usuario);
        montarDadosGrupoPesquisa(obj, nivelMontarDados,usuario);
        montarDadosAreaConhecimento(obj, nivelMontarDados,usuario);
        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>AreaConhecimentoVO</code> relacionado ao objeto <code>LinhaPesquisaVO</code>.
     * Faz uso da chave primária da classe <code>AreaConhecimentoVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
    */
    public static void montarDadosAreaConhecimento(LinhaPesquisaVO obj,int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if (obj.getAreaConhecimento().getCodigo().intValue() == 0) {
            obj.setAreaConhecimento(new AreaConhecimentoVO());
            return;
        }
        obj.setAreaConhecimento(getFacadeFactory().getAreaConhecimentoFacade().consultarPorChavePrimaria( obj.getAreaConhecimento().getCodigo(),usuario ));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>GrupoPesquisaVO</code> relacionado ao objeto <code>LinhaPesquisaVO</code>.
     * Faz uso da chave primária da classe <code>GrupoPesquisaVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
    */
    public static void montarDadosGrupoPesquisa(LinhaPesquisaVO obj,int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if (obj.getGrupoPesquisa().getCodigo().intValue() == 0) {
            obj.setGrupoPesquisa(new GrupoPesquisaVO());
            return;
        }
        obj.setGrupoPesquisa(getFacadeFactory().getGrupoPesquisaFacade().consultarPorChavePrimaria( obj.getGrupoPesquisa().getCodigo(), nivelMontarDados,usuario ));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>FuncionarioVO</code> relacionado ao objeto <code>LinhaPesquisaVO</code>.
     * Faz uso da chave primária da classe <code>FuncionarioVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
    */
    public static void montarDadosLider(LinhaPesquisaVO obj,int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if (obj.getLider().getCodigo().intValue() == 0) {
            obj.setLider(new FuncionarioVO());
            return;
        }
        obj.setLider(getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimaria( obj.getLider().getCodigo(),0, false, nivelMontarDados,usuario ));
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>LinhaPesquisaVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
    */
    public LinhaPesquisaVO consultarPorChavePrimaria( Integer codigoPrm ,int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false,usuario);
        String sqlStr = "SELECT * FROM LinhaPesquisa WHERE codigo = ?";
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
        return LinhaPesquisa.idEntidade;
    }
     
    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
    */
    public void setIdEntidade( String idEntidade ) {
        LinhaPesquisa.idEntidade = idEntidade;
    }
}