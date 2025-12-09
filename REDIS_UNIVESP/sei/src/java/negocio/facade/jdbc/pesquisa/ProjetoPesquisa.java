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

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.pesquisa.AreaConhecimentoVO;
import negocio.comuns.pesquisa.ProjetoPesquisaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.pesquisa.ProjetoPesquisaInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>ProjetoPesquisaVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>ProjetoPesquisaVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see ProjetoPesquisaVO
 * @see ControleAcesso
*/
@Repository @Scope("singleton") @Lazy 
public class ProjetoPesquisa extends ControleAcesso implements ProjetoPesquisaInterfaceFacade {
	
    protected static String idEntidade;
	
    public ProjetoPesquisa() throws Exception {
        super();
        setIdEntidade("ProjetoPesquisa");
    }
	
    /**
     * Operação responsável por retornar um novo objeto da classe <code>ProjetoPesquisaVO</code>.
    */
    public ProjetoPesquisaVO novo() throws Exception {
        ProjetoPesquisa.incluir(getIdEntidade());
        ProjetoPesquisaVO obj = new ProjetoPesquisaVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>ProjetoPesquisaVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>ProjetoPesquisaVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
    */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final ProjetoPesquisaVO obj) throws Exception {
        try {
            
            ProjetoPesquisaVO.validarDados(obj);
            ProjetoPesquisa.incluir(getIdEntidade());
            final String sql = "INSERT INTO ProjetoPesquisa( nome, linhaPesquisa, dataCriacao, palavrasChave, areaConhecimento, objetivos, justificativa, impactos, duracaoPrevista ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ? )returning codigo";
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
            sqlInserir.setString( 1, obj.getNome() );
            sqlInserir.setInt( 2, obj.getLinhaPesquisa().intValue() );
            sqlInserir.setDate( 3, Uteis.getDataJDBC(obj.getDataCriacao() ));
            sqlInserir.setString( 4, obj.getPalavrasChave() );
            sqlInserir.setInt( 5, obj.getAreaConhecimento().getCodigo().intValue() );
            sqlInserir.setString( 6, obj.getObjetivos() );
            sqlInserir.setString( 7, obj.getJustificativa() );
            sqlInserir.setString( 8, obj.getImpactos() );
            sqlInserir.setInt( 9, obj.getDuracaoPrevista().intValue() );
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
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>PaizVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>PaizVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
    */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final ProjetoPesquisaVO obj) throws Exception {
        try {
            
            ProjetoPesquisaVO.validarDados(obj);
            ProjetoPesquisa.alterar(getIdEntidade());
            final String sql = "UPDATE ProjetoPesquisa set nome=?, linhaPesquisa=?, dataCriacao=?, palavrasChave=?, areaConhecimento=?, objetivos=?, justificativa=?, impactos=?, duracaoPrevista=? WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
            sqlAlterar.setString( 1, obj.getNome() );
            sqlAlterar.setInt( 2, obj.getLinhaPesquisa().intValue() );
            sqlAlterar.setDate( 3, Uteis.getDataJDBC(obj.getDataCriacao() ));
            sqlAlterar.setString( 4, obj.getPalavrasChave() );
            sqlAlterar.setInt( 5, obj.getAreaConhecimento().getCodigo().intValue() );
            sqlAlterar.setString( 6, obj.getObjetivos() );
            sqlAlterar.setString( 7, obj.getJustificativa() );
            sqlAlterar.setString( 8, obj.getImpactos() );
            sqlAlterar.setInt( 9, obj.getDuracaoPrevista().intValue() );
            sqlAlterar.setInt( 10, obj.getCodigo().intValue() );
            return sqlAlterar;
				}
			});

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>ProjetoPesquisaVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>ProjetoPesquisaVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
    */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(ProjetoPesquisaVO obj) throws Exception {
        try {
           
            ProjetoPesquisa.excluir(getIdEntidade());
            String sql = "DELETE FROM ProjetoPesquisa WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql, new Object[] {obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>ProjetoPesquisa</code> através do valor do atributo 
     * <code>Integer duracaoPrevista</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>ProjetoPesquisaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorDuracaoPrevista(Integer valorConsulta, boolean controlarAcesso,int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM ProjetoPesquisa WHERE duracaoPrevista >= " + valorConsulta.intValue() + " ORDER BY duracaoPrevista";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, usuario));
        }

    /**
     * Responsável por realizar uma consulta de <code>ProjetoPesquisa</code> através do valor do atributo 
     * <code>Date dataCriacao</code>. Retorna os objetos com valores pertecentes ao período informado por parâmetro.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>ProjetoPesquisaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
    */
    public List consultarPorDataCriacao(Date prmIni, Date prmFim, boolean controlarAcesso,int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM ProjetoPesquisa WHERE ((dataCriacao >= '" + Uteis.getDataJDBC(prmIni) + "') and (dataCriacao <= '" + Uteis.getDataJDBC(prmFim) + "')) ORDER BY dataCriacao";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>ProjetoPesquisa</code> através do valor do atributo 
     * <code>Integer linhaPesquisa</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>ProjetoPesquisaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorLinhaPesquisa(Integer valorConsulta, boolean controlarAcesso,int nivelMontarDados , UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM ProjetoPesquisa WHERE linhaPesquisa >= " + valorConsulta.intValue() + " ORDER BY linhaPesquisa";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, usuario));
        }

    /**
     * Responsável por realizar uma consulta de <code>ProjetoPesquisa</code> através do valor do atributo 
     * <code>String nome</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>ProjetoPesquisaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNome(String valorConsulta, boolean controlarAcesso,int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM ProjetoPesquisa WHERE nome like('" + valorConsulta + "%') ORDER BY nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>ProjetoPesquisa</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>ProjetoPesquisaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso,int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM ProjetoPesquisa WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, usuario));
        }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>ProjetoPesquisaVO</code> resultantes da consulta.
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
     * em um objeto da classe <code>ProjetoPesquisaVO</code>.
     * @return  O objeto da classe <code>ProjetoPesquisaVO</code> com os dados devidamente montados.
    */
    public static ProjetoPesquisaVO montarDados(SqlRowSet dadosSQL,int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ProjetoPesquisaVO obj = new ProjetoPesquisaVO();
        obj.setCodigo( new Integer( dadosSQL.getInt("codigo")));
        obj.setNome( dadosSQL.getString("nome"));
        obj.setLinhaPesquisa( new Integer( dadosSQL.getInt("linhaPesquisa")));
        obj.setDataCriacao( dadosSQL.getDate("dataCriacao"));
        obj.setPalavrasChave( dadosSQL.getString("palavrasChave"));
        obj.getAreaConhecimento().setCodigo( new Integer( dadosSQL.getInt("areaConhecimento")));
        obj.setObjetivos( dadosSQL.getString("objetivos"));
        obj.setJustificativa( dadosSQL.getString("justificativa"));
        obj.setImpactos( dadosSQL.getString("impactos"));
        obj.setDuracaoPrevista( new Integer( dadosSQL.getInt("duracaoPrevista")));
        obj.setNovoObj(Boolean.FALSE);
        if(nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS){
            return obj;
        }
        obj.setPesquisadorProjetoPesquisaVOs( PesquisadorProjetoPesquisa.consultarPesquisadorProjetoPesquisas(obj.getCodigo(), false, nivelMontarDados,usuario) );
        if(nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS){
            return  obj;
            }

        montarDadosAreaConhecimento(obj, nivelMontarDados, usuario);
        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>AreaConhecimentoVO</code> relacionado ao objeto <code>ProjetoPesquisaVO</code>.
     * Faz uso da chave primária da classe <code>AreaConhecimentoVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
    */
    public static void montarDadosAreaConhecimento(ProjetoPesquisaVO obj,int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getAreaConhecimento().getCodigo().intValue() == 0) {
            obj.setAreaConhecimento(new AreaConhecimentoVO());
            return;
        }
        obj.setAreaConhecimento(getFacadeFactory().getAreaConhecimentoFacade().consultarPorChavePrimaria( obj.getAreaConhecimento().getCodigo(), usuario ));
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>ProjetoPesquisaVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
    */
    public ProjetoPesquisaVO consultarPorChavePrimaria( Integer codigoPrm,int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sqlStr = "SELECT * FROM ProjetoPesquisa WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
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
        return ProjetoPesquisa.idEntidade;
    }
     
    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
    */
    public void setIdEntidade( String idEntidade ) {
        ProjetoPesquisa.idEntidade = idEntidade;
    }
}