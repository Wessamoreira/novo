package negocio.facade.jdbc.pesquisa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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

import negocio.comuns.academico.CursoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.pesquisa.AreaConhecimentoVO;
import negocio.comuns.pesquisa.GrupoPesquisaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.pesquisa.GrupoPesquisaInterfaceFacade;


/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>GrupoPesquisaVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>GrupoPesquisaVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see GrupoPesquisaVO
 * @see ControleAcesso
*/
@Repository @Scope("singleton") @Lazy 
public class GrupoPesquisa extends ControleAcesso implements GrupoPesquisaInterfaceFacade {
	
    protected static String idEntidade;
	
    public GrupoPesquisa() throws Exception {
        super();
        setIdEntidade("GrupoPesquisa");
    }
	
    /**
     * Operação responsável por retornar um novo objeto da classe <code>GrupoPesquisaVO</code>.
    */
    public GrupoPesquisaVO novo() throws Exception {
        GrupoPesquisa.incluir(getIdEntidade());
        GrupoPesquisaVO obj = new GrupoPesquisaVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>GrupoPesquisaVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>GrupoPesquisaVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
    */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final GrupoPesquisaVO obj) throws Exception {
        try {
            
            GrupoPesquisaVO.validarDados(obj);
            GrupoPesquisa.incluir(getIdEntidade());
            final String sql = "INSERT INTO GrupoPesquisa( nome, sigla, dataCriacao, liderPrincipal, liderSecundario, descricao, objetivos, areaConhecimento, unidadeEnsino, curso ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo";
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
            sqlInserir.setString( 1, obj.getNome() );
            sqlInserir.setString( 2, obj.getSigla() );
            sqlInserir.setDate( 3, Uteis.getDataJDBC(obj.getDataCriacao() ));
            sqlInserir.setInt( 4, obj.getLiderPrincipal().getCodigo().intValue() );
            sqlInserir.setInt( 5, obj.getLiderSecundario().getCodigo().intValue() );
            sqlInserir.setString( 6, obj.getDescricao() );
            sqlInserir.setString( 7, obj.getObjetivos() );
            sqlInserir.setInt( 8, obj.getAreaConhecimento().getCodigo().intValue() );
            sqlInserir.setInt( 9, obj.getUnidadeEnsino().getCodigo().intValue() );
            sqlInserir.setInt( 10, obj.getCurso().getCodigo().intValue() );
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
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>GrupoPesquisaVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>GrupoPesquisaVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
    */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final GrupoPesquisaVO obj) throws Exception {
        try {
            
            GrupoPesquisaVO.validarDados(obj);
            GrupoPesquisa.alterar(getIdEntidade());
           final  String sql = "UPDATE GrupoPesquisa set nome=?, sigla=?, dataCriacao=?, liderPrincipal=?, liderSecundario=?, descricao=?, objetivos=?, areaConhecimento=?, unidadeEnsino=?, curso=? WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
            sqlAlterar.setString( 1, obj.getNome() );
            sqlAlterar.setString( 2, obj.getSigla() );
            sqlAlterar.setDate( 3, Uteis.getDataJDBC(obj.getDataCriacao() ));
            sqlAlterar.setInt( 4, obj.getLiderPrincipal().getCodigo().intValue() );
            sqlAlterar.setInt( 5, obj.getLiderSecundario().getCodigo().intValue() );
            sqlAlterar.setString( 6, obj.getDescricao() );
            sqlAlterar.setString( 7, obj.getObjetivos() );
            sqlAlterar.setInt( 8, obj.getAreaConhecimento().getCodigo().intValue() );
            sqlAlterar.setInt( 9, obj.getUnidadeEnsino().getCodigo().intValue() );
            sqlAlterar.setInt( 10, obj.getCurso().getCodigo().intValue() );
            sqlAlterar.setInt( 11, obj.getCodigo().intValue() );
            return sqlAlterar;
				}
			});

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>GrupoPesquisaVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>GrupoPesquisaVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
    */
     @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(GrupoPesquisaVO obj) throws Exception {
        try {
           
            GrupoPesquisa.excluir(getIdEntidade());
            String sql = "DELETE FROM GrupoPesquisa WHERE ((codigo = ?))";
           getConexao().getJdbcTemplate().update(sql, new Object[] {obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }
    /**
     * Responsável por realizar uma consulta de <code>GrupoPesquisa</code> através do valor do atributo 
     * <code>nome</code> da classe <code>Curso</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>GrupoPesquisaVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
    */
    public List consultarPorNomeCurso(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT GrupoPesquisa.* FROM GrupoPesquisa, Curso WHERE GrupoPesquisa.curso = Curso.codigo and Curso.nome like('" + valorConsulta + "%') ORDER BY Curso.nome";
         SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>GrupoPesquisa</code> através do valor do atributo 
     * <code>nome</code> da classe <code>UnidadeEnsino</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>GrupoPesquisaVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
    */
    public List consultarPorNomeUnidadeEnsino(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT GrupoPesquisa.* FROM GrupoPesquisa, UnidadeEnsino WHERE GrupoPesquisa.unidadeEnsino = UnidadeEnsino.codigo and UnidadeEnsino.nome like('" + valorConsulta + "%') ORDER BY UnidadeEnsino.nome";
         SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>GrupoPesquisa</code> através do valor do atributo 
     * <code>nome</code> da classe <code>AreaConhecimento</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>GrupoPesquisaVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
    */
    public List consultarPorNomeAreaConhecimento(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT GrupoPesquisa.* FROM GrupoPesquisa, AreaConhecimento WHERE GrupoPesquisa.areaConhecimento = AreaConhecimento.codigo and AreaConhecimento.nome like('" + valorConsulta + "%') ORDER BY AreaConhecimento.nome";
         SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>GrupoPesquisa</code> através do valor do atributo 
     * <code>codigo</code> da classe <code>Funcionario</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>GrupoPesquisaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
    */
    public List consultarPorCodigoFuncionario(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT GrupoPesquisa.* FROM GrupoPesquisa, Funcionario WHERE GrupoPesquisa.liderPrincipal = Funcionario.codigo and Funcionario.codigo >= " + valorConsulta.intValue() + " ORDER BY Funcionario.codigo";
         SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>GrupoPesquisa</code> através do valor do atributo 
     * <code>Date dataCriacao</code>. Retorna os objetos com valores pertecentes ao período informado por parâmetro.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>GrupoPesquisaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
    */
    public List consultarPorDataCriacao(Date prmIni, Date prmFim, boolean controlarAcesso,int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM GrupoPesquisa WHERE ((dataCriacao >= '" + Uteis.getDataJDBC(prmIni) + "') and (dataCriacao <= '" + Uteis.getDataJDBC(prmFim) + "')) ORDER BY dataCriacao";
         SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>GrupoPesquisa</code> através do valor do atributo 
     * <code>String nome</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>GrupoPesquisaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNome(String valorConsulta, boolean controlarAcesso,int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM GrupoPesquisa WHERE nome like('" + valorConsulta + "%') ORDER BY nome";
         SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>GrupoPesquisa</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>GrupoPesquisaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso,int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM GrupoPesquisa WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
         SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, usuario));
        }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>GrupoPesquisaVO</code> resultantes da consulta.
    */
  public static List montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado,Uteis.NIVELMONTARDADOS_TODOS, usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>GrupoPesquisaVO</code>.
     * @return  O objeto da classe <code>GrupoPesquisaVO</code> com os dados devidamente montados.
    */
    public static GrupoPesquisaVO montarDados(SqlRowSet dadosSQL,int nivelMontarDados, UsuarioVO usuario) throws Exception {
        GrupoPesquisaVO obj = new GrupoPesquisaVO();
        obj.setCodigo( new Integer( dadosSQL.getInt("codigo")));
        obj.setNome( dadosSQL.getString("nome"));
        obj.setSigla( dadosSQL.getString("sigla"));
        obj.setDataCriacao( dadosSQL.getDate("dataCriacao"));
        obj.getLiderPrincipal().setCodigo( new Integer( dadosSQL.getInt("liderPrincipal")));
        obj.getLiderSecundario().setCodigo( new Integer( dadosSQL.getInt("liderSecundario")));
        obj.setDescricao( dadosSQL.getString("descricao"));
        obj.setObjetivos( dadosSQL.getString("objetivos"));
        obj.getAreaConhecimento().setCodigo( new Integer( dadosSQL.getInt("areaConhecimento")));
        obj.getUnidadeEnsino().setCodigo( new Integer( dadosSQL.getInt("unidadeEnsino")));
        obj.getCurso().setCodigo( new Integer( dadosSQL.getInt("curso")));
        obj.setNovoObj(Boolean.FALSE);
        if(nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS){
            return obj;
        }
        montarDadosLiderPrincipal(obj, nivelMontarDados,usuario);
        montarDadosLiderSecundario(obj, nivelMontarDados,usuario);
        montarDadosAreaConhecimento(obj, nivelMontarDados, usuario);
        montarDadosUnidadeEnsino(obj, nivelMontarDados, usuario);
        montarDadosCurso(obj, nivelMontarDados,usuario);
        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>CursoVO</code> relacionado ao objeto <code>GrupoPesquisaVO</code>.
     * Faz uso da chave primária da classe <code>CursoVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
    */
    public static void montarDadosCurso(GrupoPesquisaVO obj,int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if (obj.getCurso().getCodigo().intValue() == 0) {
            obj.setCurso(new CursoVO());
            return;
        }
        obj.setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria( obj.getCurso().getCodigo(), nivelMontarDados, false, usuario ));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>UnidadeEnsinoVO</code> relacionado ao objeto <code>GrupoPesquisaVO</code>.
     * Faz uso da chave primária da classe <code>UnidadeEnsinoVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
    */
    public static void montarDadosUnidadeEnsino(GrupoPesquisaVO obj,int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getUnidadeEnsino().getCodigo().intValue() == 0) {
            obj.setUnidadeEnsino(new UnidadeEnsinoVO());
            return;
        }
        obj.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria( obj.getUnidadeEnsino().getCodigo(), false, nivelMontarDados, usuario ));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>AreaConhecimentoVO</code> relacionado ao objeto <code>GrupoPesquisaVO</code>.
     * Faz uso da chave primária da classe <code>AreaConhecimentoVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
    */
    public static void montarDadosAreaConhecimento(GrupoPesquisaVO obj,int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getAreaConhecimento().getCodigo().intValue() == 0) {
            obj.setAreaConhecimento(new AreaConhecimentoVO());
            return;
        }
        obj.setAreaConhecimento(getFacadeFactory().getAreaConhecimentoFacade().consultarPorChavePrimaria( obj.getAreaConhecimento().getCodigo(), usuario ));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>FuncionarioVO</code> relacionado ao objeto <code>GrupoPesquisaVO</code>.
     * Faz uso da chave primária da classe <code>FuncionarioVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
    */
    public static void montarDadosLiderSecundario(GrupoPesquisaVO obj,int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if (obj.getLiderSecundario().getCodigo().intValue() == 0) {
            obj.setLiderSecundario(new FuncionarioVO());
            return;
        }
        obj.setLiderSecundario(getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimaria( obj.getLiderSecundario().getCodigo(),0, false, nivelMontarDados,usuario ));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>FuncionarioVO</code> relacionado ao objeto <code>GrupoPesquisaVO</code>.
     * Faz uso da chave primária da classe <code>FuncionarioVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
    */
    public static void montarDadosLiderPrincipal(GrupoPesquisaVO obj,int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if (obj.getLiderPrincipal().getCodigo().intValue() == 0) {
            obj.setLiderPrincipal(new FuncionarioVO());
            return;
        }
        obj.setLiderPrincipal(getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimaria( obj.getLiderPrincipal().getCodigo(),0, false, nivelMontarDados,usuario ));
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>GrupoPesquisaVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
    */
    public GrupoPesquisaVO consultarPorChavePrimaria( Integer codigoPrm,int nivelMontarDados, UsuarioVO usuario ) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sqlStr = "SELECT * FROM GrupoPesquisa WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
       
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por obter o último valor gerado para uma chave primária.
     * É utilizada para obter o valor gerado pela SGBD para uma chave primária, 
     * a apresentação do mesmo e a implementação de possíveis relacionamentos. 
     */
  
	

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
    */
    public static String getIdEntidade() {
        return GrupoPesquisa.idEntidade;
    }
     
    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
    */
    public void setIdEntidade( String idEntidade ) {
        GrupoPesquisa.idEntidade = idEntidade;
    }
}