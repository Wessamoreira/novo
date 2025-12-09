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
import negocio.comuns.basico.PaizVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.basico.PaizInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>PaizVO</code>. Responsável por implementar operações
 * como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>PaizVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see PaizVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy 
public class Paiz extends ControleAcesso implements PaizInterfaceFacade {

    protected static String idEntidade;

    public Paiz() throws Exception {
        super();
        setIdEntidade("Pais");
    }

//    /**
//     * Operação responsável por retornar um novo objeto da classe
//     * <code>PaizVO</code>.
//     */
//    public PaizVO novo() throws Exception {
//        Paiz.incluir(getIdEntidade());
//        PaizVO obj = new PaizVO();
//        return obj;
//    }

    public void imprimirObjeto() throws Exception {
        Paiz.imprimir();
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe
     * <code>PaizVO</code>. Primeiramente valida os dados (
     * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de
     * dados e a permissão do usuário para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>PaizVO</code> que será gravado no banco
     *            de dados.
     * @exception Exception
     *                Caso haja problemas de conexão, restrição de acesso ou
     *                validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final PaizVO obj,UsuarioVO usuario) throws Exception {
        try {
            incluir(getIdEntidade(), true,usuario);
            PaizVO.validarDados(obj);
            final String sql = "INSERT INTO Paiz( nome, nacao, siglainep, nacionalidade ) VALUES ( ?, ?, ?, ? ) returning codigo";
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    sqlInserir.setString(1, obj.getNome());
                    sqlInserir.setBoolean(2, obj.isNacao());
                    sqlInserir.setString(3, obj.getSiglaInep());
                    sqlInserir.setString(4, obj.getNacionalidade());
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
        } catch (Exception e) {
            obj.setNovoObj(true);
            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe
     * <code>PaizVO</code>. Sempre utiliza a chave primária da classe como
     * atributo para localização do registro a ser alterado. Primeiramente
     * valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão
     * com o banco de dados e a permissão do usuário para realizar esta operacão
     * na entidade. Isto, através da operação <code>alterar</code> da
     * superclasse.
     *
     * @param obj
     *            Objeto da classe <code>PaizVO</code> que será alterada no
     *            banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão, restrição de acesso ou
     *                validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final PaizVO obj) throws Exception {
        try {
            Paiz.alterar(getIdEntidade());
            PaizVO.validarDados(obj);
            final String sql = "UPDATE Paiz set nome=?, nacao=?, siglainep=?, nacionalidade=? WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setString(1, obj.getNome());
                    sqlAlterar.setBoolean(2, obj.isNacao());
                    sqlAlterar.setString(3, obj.getSiglaInep());
                    sqlAlterar.setString(4, obj.getNacionalidade());
                    sqlAlterar.setInt(5, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });
            
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe
     * <code>PaizVO</code>. Sempre localiza o registro a ser excluído através da
     * chave primária da entidade. Primeiramente verifica a conexão com o banco
     * de dados e a permissão do usuário para realizar esta operacão na
     * entidade. Isto, através da operação <code>excluir</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>PaizVO</code> que será removido no
     *            banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(PaizVO obj) throws Exception {
        try {
            Paiz.excluir(getIdEntidade());
            String sql = "DELETE FROM Paiz WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>Paiz</code> através do
     * valor do atributo <code>String nome</code>. Retorna os objetos, com
     * início do valor do atributo idêntico ao parâmetro fornecido. Faz uso da
     * operação <code>montarDadosConsulta</code> que realiza o trabalho de
     * prerarar o List resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui
     *            permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>PaizVO</code>
     *         resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<PaizVO> consultarPorNome(String valorConsulta, boolean controlarAcesso,UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Paiz WHERE lower (nome) like('" + valorConsulta.toLowerCase() + "%') ORDER BY nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado));
    }

    /**
     * Responsável por realizar uma consulta de <code>Paiz</code> através do
     * valor do atributo <code>Integer codigo</code>. Retorna os objetos com
     * valores iguais ou superiores ao parâmetro fornecido. Faz uso da operação
     * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
     * List resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui
     *            permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>PaizVO</code>
     *         resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso,UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Paiz WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma
     * consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação
     * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     *
     * @return List Contendo vários objetos da classe <code>PaizVO</code>
     *         resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de
     * dados (<code>ResultSet</code>) em um objeto da classe <code>PaizVO</code>
     * .
     *
     * @return O objeto da classe <code>PaizVO</code> com os dados devidamente
     *         montados.
     */
    public static PaizVO montarDados(SqlRowSet dadosSQL) throws Exception {
        PaizVO obj = new PaizVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setNome(dadosSQL.getString("nome"));
        obj.setNacao(dadosSQL.getBoolean("nacao"));
        obj.setSiglaInep(dadosSQL.getString("siglainep"));
        obj.setNacionalidade(dadosSQL.getString("nacionalidade"));
        obj.setNovoObj(Boolean.FALSE);
        return obj;
    }

    /**
     * Operação responsável por localizar um objeto da classe
     * <code>PaizVO</code> através de sua chave primária.
     *
     * @exception Exception
     *                Caso haja problemas de conexão ou localização do objeto
     *                procurado.
     */
    public PaizVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso,UsuarioVO usuario) throws Exception {        
        return getAplicacaoControle().getPaizVO(codigoPrm);
    }
    
    @Override
    public PaizVO consultarPorChavePrimariaUnico(Integer codigoPrm, boolean controlarAcesso,UsuarioVO usuario) throws Exception {
    	consultar(getIdEntidade(), false,usuario);
    	String sql = "SELECT * FROM Paiz WHERE codigo = ?";
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
    	if (!tabelaResultado.next()) {
    		throw new ConsistirException("Dados Não Encontrados.");
    	}
    	return (montarDados(tabelaResultado));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe. Este
     * identificar é utilizado para verificar as permissões de acesso as
     * operações desta classe.
     */
    public static String getIdEntidade() {
        return Paiz.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta
     * classe. Esta alteração deve ser possível, pois, uma mesma classe de
     * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
     * que Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        Paiz.idEntidade = idEntidade;
    }
    
    @Override
    public PaizVO consultarPorNacionalidade(String nacionalidade, boolean controlarAcesso,UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), false,usuario);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM Paiz ");
        sql.append(" WHERE upper(sem_acentos(paiz.nacionalidade)) like (upper(sem_acentos(?))) ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[]{nacionalidade + PERCENT});
        if (!tabelaResultado.next()) {
            return new PaizVO();
        }
        return (montarDados(tabelaResultado));
    }
}
