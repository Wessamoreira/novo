package negocio.facade.jdbc.protocolo;

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
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.protocolo.ControleCorrespondenciaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.protocolo.ControleCorrespondenciaInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>ControleCorrespondenciaVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>ControleCorrespondenciaVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see ControleCorrespondenciaVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy 
public class ControleCorrespondencia extends ControleAcesso implements ControleCorrespondenciaInterfaceFacade {

    protected static String idEntidade;

    public ControleCorrespondencia() throws Exception {
        super();
        setIdEntidade("ControleCorrespondencia");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>ControleCorrespondenciaVO</code>.
     */
    public ControleCorrespondenciaVO novo() throws Exception {
        ControleCorrespondencia.incluir(getIdEntidade());
        ControleCorrespondenciaVO obj = new ControleCorrespondenciaVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>ControleCorrespondenciaVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>ControleCorrespondenciaVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final ControleCorrespondenciaVO obj, UsuarioVO usuarioVO) throws Exception {
        try {
            ControleCorrespondenciaVO.validarDados(obj);
            ControleCorrespondencia.incluir(getIdEntidade(), true, usuarioVO);
            final String sql = "INSERT INTO ControleCorrespondencia( data, remetente, destinatario, assunto, situacao, tipoCorrespondencia, descricao, responsavelDptoOrigem, responsavelProtocoloOrigem, dataRecebProtocoloOrigem, responsavelProtocoloDestino, dataRecebProtocoloDestino, responsavelDptoDestino, dataRecebDptoDestino ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo";
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement sqlInserir = con.prepareStatement(sql);
                    sqlInserir.setDate(1, Uteis.getDataJDBC(obj.getData()));
                    sqlInserir.setString(2, obj.getRemetente());
                    sqlInserir.setString(3, obj.getDestinatario());
                    sqlInserir.setString(4, obj.getAssunto());
                    sqlInserir.setString(5, obj.getSituacao());
                    sqlInserir.setString(6, obj.getTipoCorrespondencia());
                    sqlInserir.setString(7, obj.getDescricao());
                    sqlInserir.setInt(8, obj.getResponsavelDptoOrigem().getCodigo().intValue());
                    sqlInserir.setInt(9, obj.getResponsavelProtocoloOrigem().getCodigo().intValue());
                    sqlInserir.setDate(10, Uteis.getDataJDBC(obj.getDataRecebProtocoloOrigem()));
                    sqlInserir.setInt(11, obj.getResponsavelProtocoloDestino().getCodigo().intValue());
                    sqlInserir.setDate(12, Uteis.getDataJDBC(obj.getDataRecebProtocoloDestino()));
                    sqlInserir.setInt(13, obj.getResponsavelDptoDestino().getCodigo().intValue());
                    sqlInserir.setDate(14, Uteis.getDataJDBC(obj.getDataRecebDptoDestino()));
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
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>ControleCorrespondenciaVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>ControleCorrespondenciaVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final ControleCorrespondenciaVO obj, UsuarioVO usuarioVO) throws Exception {
        try {
            ControleCorrespondenciaVO.validarDados(obj);
            ControleCorrespondencia.alterar(getIdEntidade(), true, usuarioVO);
            final String sql = "UPDATE ControleCorrespondencia set data=?, remetente=?, destinatario=?, assunto=?, situacao=?, tipoCorrespondencia=?, descricao=?, responsavelDptoOrigem=?, responsavelProtocoloOrigem=?, dataRecebProtocoloOrigem=?, responsavelProtocoloDestino=?, dataRecebProtocoloDestino=?, responsavelDptoDestino=?, dataRecebDptoDestino=? WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement sqlAlterar = con.prepareStatement(sql);
                    sqlAlterar.setDate(1, Uteis.getDataJDBC(obj.getData()));
                    sqlAlterar.setString(2, obj.getRemetente());
                    sqlAlterar.setString(3, obj.getDestinatario());
                    sqlAlterar.setString(4, obj.getAssunto());
                    sqlAlterar.setString(5, obj.getSituacao());
                    sqlAlterar.setString(6, obj.getTipoCorrespondencia());
                    sqlAlterar.setString(7, obj.getDescricao());
                    sqlAlterar.setInt(8, obj.getResponsavelDptoOrigem().getCodigo().intValue());
                    sqlAlterar.setInt(9, obj.getResponsavelProtocoloOrigem().getCodigo().intValue());
                    sqlAlterar.setDate(10, Uteis.getDataJDBC(obj.getDataRecebProtocoloOrigem()));
                    sqlAlterar.setInt(11, obj.getResponsavelProtocoloDestino().getCodigo().intValue());
                    sqlAlterar.setDate(12, Uteis.getDataJDBC(obj.getDataRecebProtocoloDestino()));
                    sqlAlterar.setInt(13, obj.getResponsavelDptoDestino().getCodigo().intValue());
                    sqlAlterar.setDate(14, Uteis.getDataJDBC(obj.getDataRecebDptoDestino()));
                    sqlAlterar.setInt(15, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>ControleCorrespondenciaVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>ControleCorrespondenciaVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(ControleCorrespondenciaVO obj, UsuarioVO usuarioVO) throws Exception {
        try {
            ControleCorrespondencia.excluir(getIdEntidade(), true, usuarioVO);
            String sql = "DELETE FROM ControleCorrespondencia WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql, obj.getCodigo());
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>ControleCorrespondencia</code> através do valor do atributo 
     * <code>Date dataRecebDptoDestino</code>. Retorna os objetos com valores pertecentes ao período informado por parâmetro.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>ControleCorrespondenciaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorDataRecebDptoDestino(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM ControleCorrespondencia WHERE ((dataRecebDptoDestino >= '" + Uteis.getDataJDBC(prmIni) + "') and (dataRecebDptoDestino <= '" + Uteis.getDataJDBC(prmFim) + "')) ORDER BY dataRecebDptoDestino";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>ControleCorrespondencia</code> através do valor do atributo 
     * <code>Date dataRecebProtocoloDestino</code>. Retorna os objetos com valores pertecentes ao período informado por parâmetro.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>ControleCorrespondenciaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorDataRecebProtocoloDestino(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM ControleCorrespondencia WHERE ((dataRecebProtocoloDestino >= '" + Uteis.getDataJDBC(prmIni) + "') and (dataRecebProtocoloDestino <= '" + Uteis.getDataJDBC(prmFim) + "')) ORDER BY dataRecebProtocoloDestino";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>ControleCorrespondencia</code> através do valor do atributo 
     * <code>Date dataRecebProtocoloOrigem</code>. Retorna os objetos com valores pertecentes ao período informado por parâmetro.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>ControleCorrespondenciaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorDataRecebProtocoloOrigem(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM ControleCorrespondencia WHERE ((dataRecebProtocoloOrigem >= '" + Uteis.getDataJDBC(prmIni) + "') and (dataRecebProtocoloOrigem <= '" + Uteis.getDataJDBC(prmFim) + "')) ORDER BY dataRecebProtocoloOrigem";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>ControleCorrespondencia</code> através do valor do atributo 
     * <code>nome</code> da classe <code>Pessoa</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>ControleCorrespondenciaVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNomePessoa(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT ControleCorrespondencia.* FROM ControleCorrespondencia, Pessoa WHERE ControleCorrespondencia.responsavelDptoOrigem = Pessoa.codigo and Pessoa.nome like('" + valorConsulta + "%') ORDER BY Pessoa.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>ControleCorrespondencia</code> através do valor do atributo 
     * <code>String tipoCorrespondencia</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>ControleCorrespondenciaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorTipoCorrespondencia(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM ControleCorrespondencia WHERE tipoCorrespondencia like('" + valorConsulta + "%') ORDER BY tipoCorrespondencia";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>ControleCorrespondencia</code> através do valor do atributo 
     * <code>String situacao</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>ControleCorrespondenciaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorSituacao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM ControleCorrespondencia WHERE situacao like('" + valorConsulta + "%') ORDER BY situacao";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>ControleCorrespondencia</code> através do valor do atributo 
     * <code>String destinatario</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>ControleCorrespondenciaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorDestinatario(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM ControleCorrespondencia WHERE destinatario like('" + valorConsulta + "%') ORDER BY destinatario";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>ControleCorrespondencia</code> através do valor do atributo 
     * <code>String remetente</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>ControleCorrespondenciaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorRemetente(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM ControleCorrespondencia WHERE remetente like('" + valorConsulta + "%') ORDER BY remetente";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>ControleCorrespondencia</code> através do valor do atributo 
     * <code>Date data</code>. Retorna os objetos com valores pertecentes ao período informado por parâmetro.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>ControleCorrespondenciaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorData(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM ControleCorrespondencia WHERE ((data >= '" + Uteis.getDataJDBC(prmIni) + "') and (data <= '" + Uteis.getDataJDBC(prmFim) + "')) ORDER BY data";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>ControleCorrespondencia</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>ControleCorrespondenciaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM ControleCorrespondencia WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>ControleCorrespondenciaVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados,usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>ControleCorrespondenciaVO</code>.
     * @return  O objeto da classe <code>ControleCorrespondenciaVO</code> com os dados devidamente montados.
     */
    public static ControleCorrespondenciaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleCorrespondenciaVO obj = new ControleCorrespondenciaVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setData(dadosSQL.getDate("data"));
        obj.setRemetente(dadosSQL.getString("remetente"));
        obj.setDestinatario(dadosSQL.getString("destinatario"));
        obj.setAssunto(dadosSQL.getString("assunto"));
        obj.setSituacao(dadosSQL.getString("situacao"));
        obj.setTipoCorrespondencia(dadosSQL.getString("tipoCorrespondencia"));
        obj.setDescricao(dadosSQL.getString("descricao"));
        obj.getResponsavelDptoOrigem().setCodigo(new Integer(dadosSQL.getInt("responsavelDptoOrigem")));
        obj.getResponsavelProtocoloOrigem().setCodigo(new Integer(dadosSQL.getInt("responsavelProtocoloOrigem")));
        obj.setDataRecebProtocoloOrigem(dadosSQL.getDate("dataRecebProtocoloOrigem"));
        obj.getResponsavelProtocoloDestino().setCodigo(new Integer(dadosSQL.getInt("responsavelProtocoloDestino")));
        obj.setDataRecebProtocoloDestino(dadosSQL.getDate("dataRecebProtocoloDestino"));
        obj.getResponsavelDptoDestino().setCodigo(new Integer(dadosSQL.getInt("responsavelDptoDestino")));
        obj.setDataRecebDptoDestino(dadosSQL.getDate("dataRecebDptoDestino"));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        montarDadosResponsavelDptoOrigem(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,usuario);
        montarDadosResponsavelProtocoloOrigem(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,usuario);
        montarDadosResponsavelProtocoloDestino(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,usuario);
        montarDadosResponsavelDptoDestino(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,usuario);
        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>PessoaVO</code> relacionado ao objeto <code>ControleCorrespondenciaVO</code>.
     * Faz uso da chave primária da classe <code>PessoaVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosResponsavelDptoDestino(ControleCorrespondenciaVO obj, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if (obj.getResponsavelDptoDestino().getCodigo().intValue() == 0) {
            obj.setResponsavelDptoDestino(new PessoaVO());
            return;
        }
        obj.setResponsavelDptoDestino(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(obj.getResponsavelDptoDestino().getCodigo(), false, nivelMontarDados,usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>PessoaVO</code> relacionado ao objeto <code>ControleCorrespondenciaVO</code>.
     * Faz uso da chave primária da classe <code>PessoaVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosResponsavelProtocoloDestino(ControleCorrespondenciaVO obj, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if (obj.getResponsavelProtocoloDestino().getCodigo().intValue() == 0) {
            obj.setResponsavelProtocoloDestino(new PessoaVO());
            return;
        }
        obj.setResponsavelProtocoloDestino(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(obj.getResponsavelProtocoloDestino().getCodigo(), false, nivelMontarDados,usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>PessoaVO</code> relacionado ao objeto <code>ControleCorrespondenciaVO</code>.
     * Faz uso da chave primária da classe <code>PessoaVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosResponsavelProtocoloOrigem(ControleCorrespondenciaVO obj, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if (obj.getResponsavelProtocoloOrigem().getCodigo().intValue() == 0) {
            obj.setResponsavelProtocoloOrigem(new PessoaVO());
            return;
        }
        obj.setResponsavelProtocoloOrigem(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(obj.getResponsavelProtocoloOrigem().getCodigo(), false, nivelMontarDados,usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>PessoaVO</code> relacionado ao objeto <code>ControleCorrespondenciaVO</code>.
     * Faz uso da chave primária da classe <code>PessoaVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosResponsavelDptoOrigem(ControleCorrespondenciaVO obj, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if (obj.getResponsavelDptoOrigem().getCodigo().intValue() == 0) {
            obj.setResponsavelDptoOrigem(new PessoaVO());
            return;
        }
        obj.setResponsavelDptoOrigem(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(obj.getResponsavelDptoOrigem().getCodigo(), false, nivelMontarDados,usuario));
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>ControleCorrespondenciaVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public ControleCorrespondenciaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false,usuario);
        String sql = "SELECT * FROM ControleCorrespondencia WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, codigoPrm.intValue());
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
        return ControleCorrespondencia.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        ControleCorrespondencia.idEntidade = idEntidade;
    }
}
