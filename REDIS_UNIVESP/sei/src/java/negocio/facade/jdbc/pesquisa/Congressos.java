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
import negocio.comuns.pesquisa.CongressosVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.pesquisa.CongressosInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>CongressosVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>CongressosVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see CongressosVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy 
public class Congressos extends ControleAcesso implements CongressosInterfaceFacade {

    protected static String idEntidade;

    public Congressos() throws Exception {
        super();
        setIdEntidade("Congressos");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>CongressosVO</code>.
     */
    public CongressosVO novo() throws Exception {
        Congressos.incluir(getIdEntidade());
        CongressosVO obj = new CongressosVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>CongressosVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>CongressosVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final CongressosVO obj) throws Exception {
        try {

            CongressosVO.validarDados(obj);
            Congressos.incluir(getIdEntidade());
            final String sql = "INSERT INTO Congressos( nome, dataInicialRealizacao, dataFinalRealizacao, descricao, dataInicialInscricao, dataFinalInscricao, paraPulbicacao, promotor, site, contatosPromotor, localRealizacao ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo";
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    sqlInserir.setString(1, obj.getNome());
                    sqlInserir.setDate(2, Uteis.getDataJDBC(obj.getDataInicialRealizacao()));
                    sqlInserir.setDate(3, Uteis.getDataJDBC(obj.getDataFinalRealizacao()));
                    sqlInserir.setString(4, obj.getDescricao());
                    sqlInserir.setDate(5, Uteis.getDataJDBC(obj.getDataInicialInscricao()));
                    sqlInserir.setDate(6, Uteis.getDataJDBC(obj.getDataFinalInscricao()));
                    sqlInserir.setBoolean(7, obj.isParaPulbicacao().booleanValue());
                    sqlInserir.setString(8, obj.getPromotor());
                    sqlInserir.setString(9, obj.getSite());
                    sqlInserir.setString(10, obj.getContatosPromotor());
                    sqlInserir.setString(11, obj.getLocalRealizacao());
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
                     * Operação responsável por alterar no BD os dados de um objeto da classe <code>CongressosVO</code>.
                     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
                     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
                     * para realizar esta operacão na entidade.
                     * Isto, através da operação <code>alterar</code> da superclasse.
                     * @param obj    Objeto da classe <code>CongressosVO</code> que será alterada no banco de dados.
                     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
                     */

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final CongressosVO obj) throws Exception {
        try {
            
            CongressosVO.validarDados(obj);
            Congressos.alterar(getIdEntidade());
            final String sql = "UPDATE Congressos set nome=?, dataInicialRealizacao=?, dataFinalRealizacao=?, descricao=?, dataInicialInscricao=?, dataFinalInscricao=?, paraPulbicacao=?, promotor=?, site=?, contatosPromotor=?, localRealizacao=? WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
            sqlAlterar.setString(1, obj.getNome());
            sqlAlterar.setDate(2, Uteis.getDataJDBC(obj.getDataInicialRealizacao()));
            sqlAlterar.setDate(3, Uteis.getDataJDBC(obj.getDataFinalRealizacao()));
            sqlAlterar.setString(4, obj.getDescricao());
            sqlAlterar.setDate(5, Uteis.getDataJDBC(obj.getDataInicialInscricao()));
            sqlAlterar.setDate(6, Uteis.getDataJDBC(obj.getDataFinalInscricao()));
            sqlAlterar.setBoolean(7, obj.isParaPulbicacao().booleanValue());
            sqlAlterar.setString(8, obj.getPromotor());
            sqlAlterar.setString(9, obj.getSite());
            sqlAlterar.setString(10, obj.getContatosPromotor());
            sqlAlterar.setString(11, obj.getLocalRealizacao());
            sqlAlterar.setInt(12, obj.getCodigo().intValue());
            return sqlAlterar;
        }
			});

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>CongressosVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>CongressosVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(CongressosVO obj) throws Exception {
        try {
            
            Congressos.excluir(getIdEntidade());
            String sql = "DELETE FROM Congressos WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql, new Object[] {obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>Congressos</code> através do valor do atributo 
     * <code>String localRealizacao</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>CongressosVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorLocalRealizacao(String valorConsulta, boolean controlarAcesso,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Congressos WHERE localRealizacao like('" + valorConsulta + "%') ORDER BY localRealizacao";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado));
    }

    /**
     * Responsável por realizar uma consulta de <code>Congressos</code> através do valor do atributo 
     * <code>String site</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>CongressosVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorSite(String valorConsulta, boolean controlarAcesso,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Congressos WHERE site like('" + valorConsulta + "%') ORDER BY site";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado));
    }

    /**
     * Responsável por realizar uma consulta de <code>Congressos</code> através do valor do atributo 
     * <code>String promotor</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>CongressosVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorPromotor(String valorConsulta, boolean controlarAcesso,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Congressos WHERE promotor like('" + valorConsulta + "%') ORDER BY promotor";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado));
    }

    /**
     * Responsável por realizar uma consulta de <code>Congressos</code> através do valor do atributo 
     * <code>Date dataFinalInscricao</code>. Retorna os objetos com valores pertecentes ao período informado por parâmetro.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>CongressosVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorDataFinalInscricao(Date prmIni, Date prmFim, boolean controlarAcesso,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Congressos WHERE ((dataFinalInscricao >= '" + Uteis.getDataJDBC(prmIni) + "') and (dataFinalInscricao <= '" + Uteis.getDataJDBC(prmFim) + "')) ORDER BY dataFinalInscricao";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado));
    }

    /**
     * Responsável por realizar uma consulta de <code>Congressos</code> através do valor do atributo 
     * <code>Date dataInicialInscricao</code>. Retorna os objetos com valores pertecentes ao período informado por parâmetro.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>CongressosVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorDataInicialInscricao(Date prmIni, Date prmFim, boolean controlarAcesso,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Congressos WHERE ((dataInicialInscricao >= '" + Uteis.getDataJDBC(prmIni) + "') and (dataInicialInscricao <= '" + Uteis.getDataJDBC(prmFim) + "')) ORDER BY dataInicialInscricao";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado));
    }

    /**
     * Responsável por realizar uma consulta de <code>Congressos</code> através do valor do atributo 
     * <code>Date dataFinalRealizacao</code>. Retorna os objetos com valores pertecentes ao período informado por parâmetro.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>CongressosVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorDataFinalRealizacao(Date prmIni, Date prmFim, boolean controlarAcesso,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Congressos WHERE ((dataFinalRealizacao >= '" + Uteis.getDataJDBC(prmIni) + "') and (dataFinalRealizacao <= '" + Uteis.getDataJDBC(prmFim) + "')) ORDER BY dataFinalRealizacao";
       SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado));
    }

    /**
     * Responsável por realizar uma consulta de <code>Congressos</code> através do valor do atributo 
     * <code>Date dataInicialRealizacao</code>. Retorna os objetos com valores pertecentes ao período informado por parâmetro.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>CongressosVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorDataInicialRealizacao(Date prmIni, Date prmFim, boolean controlarAcesso,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Congressos WHERE ((dataInicialRealizacao >= '" + Uteis.getDataJDBC(prmIni) + "') and (dataInicialRealizacao <= '" + Uteis.getDataJDBC(prmFim) + "')) ORDER BY dataInicialRealizacao";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado));
    }

    /**
     * Responsável por realizar uma consulta de <code>Congressos</code> através do valor do atributo 
     * <code>String nome</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>CongressosVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNome(String valorConsulta, boolean controlarAcesso,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Congressos WHERE nome like('" + valorConsulta + "%') ORDER BY nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado));
    }

    /**
     * Responsável por realizar uma consulta de <code>Congressos</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>CongressosVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Congressos WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>CongressosVO</code> resultantes da consulta.
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
     * em um objeto da classe <code>CongressosVO</code>.
     * @return  O objeto da classe <code>CongressosVO</code> com os dados devidamente montados.
     */
    public static CongressosVO montarDados(SqlRowSet dadosSQL) throws Exception {
        CongressosVO obj = new CongressosVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setNome(dadosSQL.getString("nome"));
        obj.setDataInicialRealizacao(dadosSQL.getDate("dataInicialRealizacao"));
        obj.setDataFinalRealizacao(dadosSQL.getDate("dataFinalRealizacao"));
        obj.setDescricao(dadosSQL.getString("descricao"));
        obj.setDataInicialInscricao(dadosSQL.getDate("dataInicialInscricao"));
        obj.setDataFinalInscricao(dadosSQL.getDate("dataFinalInscricao"));
        obj.setParaPulbicacao(new Boolean(dadosSQL.getBoolean("paraPulbicacao")));
        obj.setPromotor(dadosSQL.getString("promotor"));
        obj.setSite(dadosSQL.getString("site"));
        obj.setContatosPromotor(dadosSQL.getString("contatosPromotor"));
        obj.setLocalRealizacao(dadosSQL.getString("localRealizacao"));
        obj.setNovoObj(Boolean.FALSE);

        return obj;
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>CongressosVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public CongressosVO consultarPorChavePrimaria(Integer codigoPrm,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false,usuario);
        String sqlStr = "SELECT * FROM Congressos WHERE codigo = ?";
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
        return Congressos.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        Congressos.idEntidade = idEntidade;
    }
}
