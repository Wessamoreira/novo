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
import negocio.comuns.pesquisa.InsentivoPesquisaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.pesquisa.InsentivoPesquisaInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>InsentivoPesquisaVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>InsentivoPesquisaVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see InsentivoPesquisaVO
 * @see ControleAcesso
*/
@Repository @Scope("singleton") @Lazy 
public class InsentivoPesquisa extends ControleAcesso implements InsentivoPesquisaInterfaceFacade {
	
    protected static String idEntidade;
	
    public InsentivoPesquisa() throws Exception {
        super();
        setIdEntidade("InsentivoPesquisa");
    }
	
    /**
     * Operação responsável por retornar um novo objeto da classe <code>InsentivoPesquisaVO</code>.
    */
    public InsentivoPesquisaVO novo() throws Exception {
        InsentivoPesquisa.incluir(getIdEntidade());
        InsentivoPesquisaVO obj = new InsentivoPesquisaVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>InsentivoPesquisaVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>InsentivoPesquisaVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
    */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final InsentivoPesquisaVO obj) throws Exception {
        try {
            
            InsentivoPesquisaVO.validarDados(obj);
            InsentivoPesquisa.incluir(getIdEntidade());
            final String sql = "INSERT INTO InsentivoPesquisa( data, nome, descricao, orgaoPromotor, dataInicialInscricao, dataFinalInscricao, prerequisitos, documentacao, areasConhecimento, sitePrograma, tipoInsentivo, contatosOrgaoPromotor ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )returning codigo";
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
            sqlInserir.setDate( 1, Uteis.getDataJDBC(obj.getData() ));
            sqlInserir.setString( 2, obj.getNome() );
            sqlInserir.setString( 3, obj.getDescricao() );
            sqlInserir.setString( 4, obj.getOrgaoPromotor() );
            sqlInserir.setDate( 5, Uteis.getDataJDBC(obj.getDataInicialInscricao() ));
            sqlInserir.setDate( 6, Uteis.getDataJDBC(obj.getDataFinalInscricao() ));
            sqlInserir.setString( 7, obj.getPrerequisitos() );
            sqlInserir.setString( 8, obj.getDocumentacao() );
            sqlInserir.setString( 9, obj.getAreasConhecimento() );
            sqlInserir.setString( 10, obj.getSitePrograma() );
            sqlInserir.setString( 11, obj.getTipoInsentivo() );
            sqlInserir.setString( 12, obj.getContatosOrgaoPromotor() );
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
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>InsentivoPesquisaVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>InsentivoPesquisaVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
    */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final InsentivoPesquisaVO obj) throws Exception {
        try {
            
            InsentivoPesquisaVO.validarDados(obj);
            InsentivoPesquisa.alterar(getIdEntidade());
           final String sql = "UPDATE InsentivoPesquisa set data=?, nome=?, descricao=?, orgaoPromotor=?, dataInicialInscricao=?, dataFinalInscricao=?, prerequisitos=?, documentacao=?, areasConhecimento=?, sitePrograma=?, tipoInsentivo=?, contatosOrgaoPromotor=? WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
            sqlAlterar.setDate( 1, Uteis.getDataJDBC(obj.getData() ));
            sqlAlterar.setString( 2, obj.getNome() );
            sqlAlterar.setString( 3, obj.getDescricao() );
            sqlAlterar.setString( 4, obj.getOrgaoPromotor() );
            sqlAlterar.setDate( 5, Uteis.getDataJDBC(obj.getDataInicialInscricao() ));
            sqlAlterar.setDate( 6, Uteis.getDataJDBC(obj.getDataFinalInscricao() ));
            sqlAlterar.setString( 7, obj.getPrerequisitos() );
            sqlAlterar.setString( 8, obj.getDocumentacao() );
            sqlAlterar.setString( 9, obj.getAreasConhecimento() );
            sqlAlterar.setString( 10, obj.getSitePrograma() );
            sqlAlterar.setString( 11, obj.getTipoInsentivo() );
            sqlAlterar.setString( 12, obj.getContatosOrgaoPromotor() );
            sqlAlterar.setInt( 13, obj.getCodigo().intValue() );
            return sqlAlterar;
				}
			});

        } catch (Exception e) {
            throw e;
        }
    }


    /**
     * Operação responsável por excluir no BD um objeto da classe <code>InsentivoPesquisaVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>InsentivoPesquisaVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
    */
     @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(InsentivoPesquisaVO obj) throws Exception {
        try {
            
            InsentivoPesquisa.excluir(getIdEntidade());
            String sql = "DELETE FROM InsentivoPesquisa WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql, new Object[] {obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>InsentivoPesquisa</code> através do valor do atributo 
     * <code>Date dataFinalInscricao</code>. Retorna os objetos com valores pertecentes ao período informado por parâmetro.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>InsentivoPesquisaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
    */
    public List consultarPorDataFinalInscricao(Date prmIni, Date prmFim, boolean controlarAcesso,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM InsentivoPesquisa WHERE ((dataFinalInscricao >= '" + Uteis.getDataJDBC(prmIni) + "') and (dataFinalInscricao <= '" + Uteis.getDataJDBC(prmFim) + "')) ORDER BY dataFinalInscricao";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado));
    }

    /**
     * Responsável por realizar uma consulta de <code>InsentivoPesquisa</code> através do valor do atributo 
     * <code>Date dataInicialInscricao</code>. Retorna os objetos com valores pertecentes ao período informado por parâmetro.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>InsentivoPesquisaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
    */
    public List consultarPorDataInicialInscricao(Date prmIni, Date prmFim, boolean controlarAcesso,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM InsentivoPesquisa WHERE ((dataInicialInscricao >= '" + Uteis.getDataJDBC(prmIni) + "') and (dataInicialInscricao <= '" + Uteis.getDataJDBC(prmFim) + "')) ORDER BY dataInicialInscricao";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado));
    }
    

    /**
     * Responsável por realizar uma consulta de <code>InsentivoPesquisa</code> através do valor do atributo 
     * <code>String orgaoPromotor</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>InsentivoPesquisaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorOrgaoPromotor(String valorConsulta, boolean controlarAcesso,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM InsentivoPesquisa WHERE orgaoPromotor like('" + valorConsulta + "%') ORDER BY orgaoPromotor";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado));
    }
    /**
     * Responsável por realizar uma consulta de <code>InsentivoPesquisa</code> através do valor do atributo 
     * <code>String nome</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>InsentivoPesquisaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNome(String valorConsulta, boolean controlarAcesso,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM InsentivoPesquisa WHERE nome like('" + valorConsulta + "%') ORDER BY nome";
       SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado));
    }
    /**
     * Responsável por realizar uma consulta de <code>InsentivoPesquisa</code> através do valor do atributo 
     * <code>Date data</code>. Retorna os objetos com valores pertecentes ao período informado por parâmetro.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>InsentivoPesquisaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
    */
    public List consultarPorData(Date prmIni, Date prmFim, boolean controlarAcesso,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM InsentivoPesquisa WHERE ((data >= '" + Uteis.getDataJDBC(prmIni) + "') and (data <= '" + Uteis.getDataJDBC(prmFim) + "')) ORDER BY data";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado));
    }

    /**
     * Responsável por realizar uma consulta de <code>InsentivoPesquisa</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>InsentivoPesquisaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM InsentivoPesquisa WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>InsentivoPesquisaVO</code> resultantes da consulta.
    */
public static List montarDadosConsulta(SqlRowSet tabelaResultado) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>InsentivoPesquisaVO</code>.
     * @return  O objeto da classe <code>InsentivoPesquisaVO</code> com os dados devidamente montados.
    */
    public static InsentivoPesquisaVO montarDados(SqlRowSet dadosSQL) throws Exception {
        InsentivoPesquisaVO obj = new InsentivoPesquisaVO();
        obj.setCodigo( new Integer( dadosSQL.getInt("codigo")));
        obj.setData( dadosSQL.getDate("data"));
        obj.setNome( dadosSQL.getString("nome"));
        obj.setDescricao( dadosSQL.getString("descricao"));
        obj.setOrgaoPromotor( dadosSQL.getString("orgaoPromotor"));
        obj.setDataInicialInscricao( dadosSQL.getDate("dataInicialInscricao"));
        obj.setDataFinalInscricao( dadosSQL.getDate("dataFinalInscricao"));
        obj.setPrerequisitos( dadosSQL.getString("prerequisitos"));
        obj.setDocumentacao( dadosSQL.getString("documentacao"));
        obj.setAreasConhecimento( dadosSQL.getString("areasConhecimento"));
        obj.setSitePrograma( dadosSQL.getString("sitePrograma"));
        obj.setTipoInsentivo( dadosSQL.getString("tipoInsentivo"));
        obj.setContatosOrgaoPromotor( dadosSQL.getString("contatosOrgaoPromotor"));
        obj.setNovoObj(Boolean.FALSE);

        return obj;
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>InsentivoPesquisaVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
    */
    public InsentivoPesquisaVO consultarPorChavePrimaria( Integer codigoPrm,UsuarioVO usuario ) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false,usuario);
        String sqlStr = "SELECT * FROM InsentivoPesquisa WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado));
    }
	

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
    */
    public static String getIdEntidade() {
        return InsentivoPesquisa.idEntidade;
    }
     
    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
    */
    public void setIdEntidade( String idEntidade ) {
        InsentivoPesquisa.idEntidade = idEntidade;
    }
}