package negocio.facade.jdbc.administrativo;

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

import negocio.comuns.administrativo.ComunicadoInternoRegistroLeituraVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.administrativo.ComunicadoInternoRegistroLeituraInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>ComunicadoInternoRegistroLeituraVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>ComunicadoInternoRegistroLeituraVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see ComunicadoInternoRegistroLeituraVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy 
public class ComunicadoInternoRegistroLeitura extends ControleAcesso implements ComunicadoInternoRegistroLeituraInterfaceFacade {

    protected static String idEntidade;

    public ComunicadoInternoRegistroLeitura() throws Exception {
        super();
        setIdEntidade("ComunicadoInternoRegistroLeitura");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>ComunicadoInternoRegistroLeituraVO</code>.
     */
    public ComunicadoInternoRegistroLeituraVO novo() throws Exception {
        ComunicadoInternoRegistroLeitura.incluir(getIdEntidade());
        ComunicadoInternoRegistroLeituraVO obj = new ComunicadoInternoRegistroLeituraVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>ComunicadoInternoRegistroLeituraVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>ComunicadoInternoRegistroLeituraVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final ComunicadoInternoRegistroLeituraVO obj) throws Exception {
        try {
            ComunicadoInternoRegistroLeituraVO.validarDados(obj);
            ComunicadoInternoRegistroLeitura.incluir(getIdEntidade());
            final String sql = "INSERT INTO ComunicadoInternoRegistroLeitura( comunicadoInterno, dataLeitura, destinatario ) VALUES ( ?, ?, ? ) returning codigo";
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement sqlInserir = con.prepareStatement(sql);
                    sqlInserir.setInt(1, obj.getComunicadoInterno().intValue());
                    sqlInserir.setDate(2, Uteis.getDataJDBC(obj.getDataLeitura()));
                    sqlInserir.setInt(3, obj.getDestinatario().intValue());
                    return sqlInserir;
                }
            }, new ResultSetExtractor() {

                public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                    if (rs.next()) {
                        obj.setNovoObj(Boolean.FALSE);
                        return rs.getInt("codigo");
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
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>ComunicadoInternoRegistroLeituraVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>ComunicadoInternoRegistroLeituraVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final ComunicadoInternoRegistroLeituraVO obj) throws Exception {
        try {
            ComunicadoInternoRegistroLeituraVO.validarDados(obj);
            ComunicadoInternoRegistroLeitura.alterar(getIdEntidade());
            final String sql = "UPDATE ComunicadoInternoRegistroLeitura set comunicadoInterno=?, dataLeitura=?, destinatario=? WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement sqlAlterar = con.prepareStatement(sql);
                    sqlAlterar.setInt(1, obj.getComunicadoInterno().intValue());
                    sqlAlterar.setDate(2, Uteis.getDataJDBC(obj.getDataLeitura()));
                    sqlAlterar.setInt(3, obj.getDestinatario().intValue());
                    sqlAlterar.setInt(4, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>ComunicadoInternoRegistroLeituraVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>ComunicadoInternoRegistroLeituraVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(ComunicadoInternoRegistroLeituraVO obj) throws Exception {
        try {
            ComunicadoInternoRegistroLeitura.excluir(getIdEntidade());
            String sql = "DELETE FROM ComunicadoInternoRegistroLeitura WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql,new Object[] {obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>ComunicadoInternoRegistroLeitura</code> através do valor do atributo 
     * <code>Integer destinatario</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>ComunicadoInternoRegistroLeituraVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorDestinatario(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM ComunicadoInternoRegistroLeitura WHERE destinatario >= " + valorConsulta.intValue() + " ORDER BY destinatario";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
    }

    /**
     * Responsável por realizar uma consulta de <code>ComunicadoInternoRegistroLeitura</code> através do valor do atributo 
     * <code>Integer comunicadoInterno</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>ComunicadoInternoRegistroLeituraVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorComunicadoInterno(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM ComunicadoInternoRegistroLeitura WHERE comunicadoInterno >= " + valorConsulta.intValue() + " ORDER BY comunicadoInterno";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
    }

    /**
     * Responsável por realizar uma consulta de <code>ComunicadoInternoRegistroLeitura</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>ComunicadoInternoRegistroLeituraVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM ComunicadoInternoRegistroLeitura WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>ComunicadoInternoRegistroLeituraVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
        List vetResultado = new ArrayList();
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>ComunicadoInternoRegistroLeituraVO</code>.
     * @return  O objeto da classe <code>ComunicadoInternoRegistroLeituraVO</code> com os dados devidamente montados.
     */
    public static ComunicadoInternoRegistroLeituraVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados) throws Exception {
        ComunicadoInternoRegistroLeituraVO obj = new ComunicadoInternoRegistroLeituraVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setComunicadoInterno(new Integer(dadosSQL.getInt("comunicadoInterno")));
        obj.setDataLeitura(dadosSQL.getDate("dataLeitura"));
        obj.setDestinatario(new Integer(dadosSQL.getInt("destinatario")));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }

        return obj;
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return ComunicadoInternoRegistroLeitura.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        ComunicadoInternoRegistroLeitura.idEntidade = idEntidade;
    }
}
