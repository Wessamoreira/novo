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

import negocio.comuns.administrativo.TipoMidiaCaptacaoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.administrativo.TipoMidiaCaptacaoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>TipoMidiaCaptacaoVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>TipoMidiaCaptacaoVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see TipoMidiaCaptacaoVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class TipoMidiaCaptacao extends ControleAcesso implements TipoMidiaCaptacaoInterfaceFacade {

    protected static String idEntidade;

    public TipoMidiaCaptacao() throws Exception {
        super();
        setIdEntidade("TipoMidiaCaptacao");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>TipoMidiaCaptacaoVO</code>.
     */
    public TipoMidiaCaptacaoVO novo() throws Exception {
        TipoMidiaCaptacao.incluir(getIdEntidade());
        TipoMidiaCaptacaoVO obj = new TipoMidiaCaptacaoVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>TipoMidiaCaptacaoVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>TipoMidiaCaptacaoVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final TipoMidiaCaptacaoVO obj,UsuarioVO usuario) throws Exception {
        try {
            validarUnicidade(obj,usuario);
            TipoMidiaCaptacaoVO.validarDados(obj);
            TipoMidiaCaptacao.incluir(getIdEntidade(), true, usuario);
            final String sql = "INSERT INTO TipoMidiaCaptacao( nomeMidia, descricaoMidia ) VALUES ( ?, ? ) returning codigo";
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement sqlInserir = con.prepareStatement(sql);
                    sqlInserir.setString(1, obj.getNomeMidia());
                    sqlInserir.setString(2, obj.getDescricaoMidia());
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
            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>TipoMidiaCaptacaoVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>TipoMidiaCaptacaoVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final TipoMidiaCaptacaoVO obj,UsuarioVO usuario) throws Exception {
        try {
            //validarUnicidade(obj,usuario);
            TipoMidiaCaptacaoVO.validarDados(obj);
            TipoMidiaCaptacao.alterar(getIdEntidade(), true, usuario);
            final String sql = "UPDATE TipoMidiaCaptacao set nomeMidia=?, descricaoMidia=? WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement sqlAlterar = con.prepareStatement(sql);
                    sqlAlterar.setString(1, obj.getNomeMidia());
                    sqlAlterar.setString(2, obj.getDescricaoMidia());
                    sqlAlterar.setInt(3, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>TipoMidiaCaptacaoVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>TipoMidiaCaptacaoVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(TipoMidiaCaptacaoVO obj, UsuarioVO usuarioVO) throws Exception {
        try {
            TipoMidiaCaptacao.excluir(getIdEntidade(), true, usuarioVO);
            String sql = "DELETE FROM TipoMidiaCaptacao WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>TipoMidiaCaptacao</code> através do valor do atributo 
     * <code>String nomeMidia</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>TipoMidiaCaptacaoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<TipoMidiaCaptacaoVO> consultarPorNomeMidia(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM TipoMidiaCaptacao WHERE lower (nomeMidia) like('" + valorConsulta.toLowerCase() + "%') ORDER BY nomeMidia";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
    }

    /**
     * Responsável por realizar uma consulta de <code>TipoMidiaCaptacao</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>TipoMidiaCaptacaoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM TipoMidiaCaptacao WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>TipoMidiaCaptacaoVO</code> resultantes da consulta.
     */
    public static List<TipoMidiaCaptacaoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
        List<TipoMidiaCaptacaoVO> vetResultado = new ArrayList<TipoMidiaCaptacaoVO>();
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>TipoMidiaCaptacaoVO</code>.
     * @return  O objeto da classe <code>TipoMidiaCaptacaoVO</code> com os dados devidamente montados.
     */
    public static TipoMidiaCaptacaoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados) throws Exception {
        TipoMidiaCaptacaoVO obj = new TipoMidiaCaptacaoVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setNomeMidia(dadosSQL.getString("nomeMidia"));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
            return obj;
        }
        obj.setDescricaoMidia(dadosSQL.getString("descricaoMidia"));        
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
            return obj;
        }

        return obj;
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>TipoMidiaCaptacaoVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public TipoMidiaCaptacaoVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false,usuario);
        String sql = "SELECT * FROM TipoMidiaCaptacao WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm.intValue()});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado, nivelMontarDados));
    }

    public void validarUnicidade(TipoMidiaCaptacaoVO obj,UsuarioVO usuario) throws ConsistirException, Exception {
        List<TipoMidiaCaptacaoVO> lista = consultarPorNomeMidia(obj.getNomeMidia(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,usuario);
        for (TipoMidiaCaptacaoVO repetido : lista) {
            if (repetido.getNomeMidia().toLowerCase().equals(obj.getNomeMidia().toLowerCase())) {
                throw new ConsistirException("O campo Nome já esta cadastrado!");
            }
        }
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return TipoMidiaCaptacao.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        TipoMidiaCaptacao.idEntidade = idEntidade;
    }
}
