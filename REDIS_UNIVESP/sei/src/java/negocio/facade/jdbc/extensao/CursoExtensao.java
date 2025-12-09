package negocio.facade.jdbc.extensao;

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

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.extensao.ClassificaoCursoExtensaoVO;
import negocio.comuns.extensao.CursoExtensaoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.extensao.CursoExtensaoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>CursoExtensaoVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>CursoExtensaoVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see CursoExtensaoVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy 
public class CursoExtensao extends ControleAcesso implements CursoExtensaoInterfaceFacade {

    protected static String idEntidade;

    public CursoExtensao() throws Exception {
        super();
        setIdEntidade("CursoExtensao");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>CursoExtensaoVO</code>.
     */
    public CursoExtensaoVO novo() throws Exception {
        CursoExtensao.incluir(getIdEntidade());
        CursoExtensaoVO obj = new CursoExtensaoVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>CursoExtensaoVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>CursoExtensaoVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final CursoExtensaoVO obj) throws Exception {
        try {
            CursoExtensao.incluir(getIdEntidade());
            CursoExtensaoVO.validarDados(obj);
            final String sql = "INSERT INTO CursoExtensao( nome, cargaHoraria, dataInicial, dataFinal, ementa, conteudoProgramatico, situacao, situacaoFinanceira, classificacaoCursoExtensao, local, horario, dataInicialInscricao, dataFinalInscricao, valorComunidade, valorAluno, valorFuncionario, valorProfessor, valorInscricaoComunidade, valorInscricaoAluno, valorInscricaoFuncionario, valorInscricaoProfessor, cobrarInscricao, inscricaoPelaInternet, confirmacaoPresencial, unidadeEnsino ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo";
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    sqlInserir.setString(1, obj.getNome());
                    sqlInserir.setInt(2, obj.getCargaHoraria().intValue());
                    sqlInserir.setDate(3, Uteis.getDataJDBC(obj.getDataInicial()));
                    sqlInserir.setDate(4, Uteis.getDataJDBC(obj.getDataFinal()));
                    sqlInserir.setString(5, obj.getEmenta());
                    sqlInserir.setString(6, obj.getConteudoProgramatico());
                    sqlInserir.setString(7, obj.getSituacao());
                    sqlInserir.setString(8, obj.getSituacaoFinanceira());
                    sqlInserir.setInt(9, obj.getClassificacaoCursoExtensao().getCodigo().intValue());
                    sqlInserir.setString(10, obj.getLocal());
                    sqlInserir.setString(11, obj.getHorario());
                    sqlInserir.setDate(12, Uteis.getDataJDBC(obj.getDataInicialInscricao()));
                    sqlInserir.setDate(13, Uteis.getDataJDBC(obj.getDataFinalInscricao()));
                    sqlInserir.setDouble(14, obj.getValorComunidade().doubleValue());
                    sqlInserir.setDouble(15, obj.getValorAluno().doubleValue());
                    sqlInserir.setDouble(16, obj.getValorFuncionario().doubleValue());
                    sqlInserir.setDouble(17, obj.getValorProfessor().doubleValue());
                    sqlInserir.setDouble(18, obj.getValorInscricaoComunidade().doubleValue());
                    sqlInserir.setDouble(19, obj.getValorInscricaoAluno().doubleValue());
                    sqlInserir.setDouble(20, obj.getValorInscricaoFuncionario().doubleValue());
                    sqlInserir.setDouble(21, obj.getValorInscricaoProfessor().doubleValue());
                    sqlInserir.setBoolean(22, obj.isCobrarInscricao().booleanValue());
                    sqlInserir.setBoolean(23, obj.isInscricaoPelaInternet().booleanValue());
                    sqlInserir.setBoolean(24, obj.isConfirmacaoPresencial().booleanValue());
                    sqlInserir.setInt(25, obj.getUnidadeEnsino().getCodigo().intValue());
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
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>CursoExtensaoVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>CursoExtensaoVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final CursoExtensaoVO obj) throws Exception {
        try {
            CursoExtensao.alterar(getIdEntidade());
            CursoExtensaoVO.validarDados(obj);
            final String sql = "UPDATE CursoExtensao set nome=?, cargaHoraria=?, dataInicial=?, dataFinal=?, ementa=?, conteudoProgramatico=?, situacao=?, situacaoFinanceira=?, classificacaoCursoExtensao=?, local=?, horario=?, dataInicialInscricao=?, dataFinalInscricao=?, valorComunidade=?, valorAluno=?, valorFuncionario=?, valorProfessor=?, valorInscricaoComunidade=?, valorInscricaoAluno=?, valorInscricaoFuncionario=?, valorInscricaoProfessor=?, cobrarInscricao=?, inscricaoPelaInternet=?, confirmacaoPresencial=?, unidadeEnsino=? WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setString(1, obj.getNome());
                    sqlAlterar.setInt(2, obj.getCargaHoraria().intValue());
                    sqlAlterar.setDate(3, Uteis.getDataJDBC(obj.getDataInicial()));
                    sqlAlterar.setDate(4, Uteis.getDataJDBC(obj.getDataFinal()));
                    sqlAlterar.setString(5, obj.getEmenta());
                    sqlAlterar.setString(6, obj.getConteudoProgramatico());
                    sqlAlterar.setString(7, obj.getSituacao());
                    sqlAlterar.setString(8, obj.getSituacaoFinanceira());
                    sqlAlterar.setInt(9, obj.getClassificacaoCursoExtensao().getCodigo().intValue());
                    sqlAlterar.setString(10, obj.getLocal());
                    sqlAlterar.setString(11, obj.getHorario());
                    sqlAlterar.setDate(12, Uteis.getDataJDBC(obj.getDataInicialInscricao()));
                    sqlAlterar.setDate(13, Uteis.getDataJDBC(obj.getDataFinalInscricao()));
                    sqlAlterar.setDouble(14, obj.getValorComunidade().doubleValue());
                    sqlAlterar.setDouble(15, obj.getValorAluno().doubleValue());
                    sqlAlterar.setDouble(16, obj.getValorFuncionario().doubleValue());
                    sqlAlterar.setDouble(17, obj.getValorProfessor().doubleValue());
                    sqlAlterar.setDouble(18, obj.getValorInscricaoComunidade().doubleValue());
                    sqlAlterar.setDouble(19, obj.getValorInscricaoAluno().doubleValue());
                    sqlAlterar.setDouble(20, obj.getValorInscricaoFuncionario().doubleValue());
                    sqlAlterar.setDouble(21, obj.getValorInscricaoProfessor().doubleValue());
                    sqlAlterar.setBoolean(22, obj.isCobrarInscricao().booleanValue());
                    sqlAlterar.setBoolean(23, obj.isInscricaoPelaInternet().booleanValue());
                    sqlAlterar.setBoolean(24, obj.isConfirmacaoPresencial().booleanValue());
                    sqlAlterar.setInt(25, obj.getUnidadeEnsino().getCodigo().intValue());
                    sqlAlterar.setInt(26, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>CursoExtensaoVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>CursoExtensaoVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(CursoExtensaoVO obj) throws Exception {
        try {
            CursoExtensao.excluir(getIdEntidade());
            String sql = "DELETE FROM CursoExtensao WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql, new Object[] {obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>CursoExtensao</code> através do valor do atributo 
     * <code>nome</code> da classe <code>UnidadeEnsino</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>CursoExtensaoVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNomeUnidadeEnsino(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT CursoExtensao.* FROM CursoExtensao, UnidadeEnsino WHERE CursoExtensao.unidadeEnsino = UnidadeEnsino.codigo and UnidadeEnsino.nome like('" + valorConsulta + "%') ORDER BY UnidadeEnsino.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado,nivelMontarDados,usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>CursoExtensao</code> através do valor do atributo 
     * <code>Double valorInscricaoProfessor</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>CursoExtensaoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorValorInscricaoProfessor(Double valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM CursoExtensao WHERE valorInscricaoProfessor >= " + valorConsulta.doubleValue() + " ORDER BY valorInscricaoProfessor";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado,nivelMontarDados,usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>CursoExtensao</code> através do valor do atributo 
     * <code>Double valorInscricaoFuncionario</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>CursoExtensaoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorValorInscricaoFuncionario(Double valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM CursoExtensao WHERE valorInscricaoFuncionario >= " + valorConsulta.doubleValue() + " ORDER BY valorInscricaoFuncionario";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado,nivelMontarDados,usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>CursoExtensao</code> através do valor do atributo 
     * <code>Double valorInscricaoAluno</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>CursoExtensaoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorValorInscricaoAluno(Double valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM CursoExtensao WHERE valorInscricaoAluno >= " + valorConsulta.doubleValue() + " ORDER BY valorInscricaoAluno";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado,nivelMontarDados,usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>CursoExtensao</code> através do valor do atributo 
     * <code>Double valorInscricaoComunidade</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>CursoExtensaoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorValorInscricaoComunidade(Double valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {

        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM CursoExtensao WHERE valorInscricaoComunidade >= " + valorConsulta.doubleValue() + " ORDER BY valorInscricaoComunidade";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado,nivelMontarDados,usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>CursoExtensao</code> através do valor do atributo 
     * <code>Double valorProfessor</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>CursoExtensaoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorValorProfessor(Double valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM CursoExtensao WHERE valorProfessor >= " + valorConsulta.doubleValue() + " ORDER BY valorProfessor";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado,nivelMontarDados,usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>CursoExtensao</code> através do valor do atributo 
     * <code>Double valorFuncionario</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>CursoExtensaoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorValorFuncionario(Double valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM CursoExtensao WHERE valorFuncionario >= " + valorConsulta.doubleValue() + " ORDER BY valorFuncionario";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado,nivelMontarDados,usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>CursoExtensao</code> através do valor do atributo 
     * <code>Double valorAluno</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>CursoExtensaoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorValorAluno(Double valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM CursoExtensao WHERE valorAluno >= " + valorConsulta.doubleValue() + " ORDER BY valorAluno";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado,nivelMontarDados,usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>CursoExtensao</code> através do valor do atributo 
     * <code>Double valorComunidade</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>CursoExtensaoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorValorComunidade(Double valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM CursoExtensao WHERE valorComunidade >= " + valorConsulta.doubleValue() + " ORDER BY valorComunidade";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado,nivelMontarDados,usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>CursoExtensao</code> através do valor do atributo 
     * <code>Date dataFinalInscricao</code>. Retorna os objetos com valores pertecentes ao período informado por parâmetro.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>CursoExtensaoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorDataFinalInscricao(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM CursoExtensao WHERE ((dataFinalInscricao >= '" + Uteis.getDataJDBC(prmIni) + "') and (dataFinalInscricao <= '" + Uteis.getDataJDBC(prmFim) + "')) ORDER BY dataFinalInscricao";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado,nivelMontarDados,usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>CursoExtensao</code> através do valor do atributo 
     * <code>Date dataInicialInscricao</code>. Retorna os objetos com valores pertecentes ao período informado por parâmetro.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>CursoExtensaoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorDataInicialInscricao(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM CursoExtensao WHERE ((dataInicialInscricao >= '" + Uteis.getDataJDBC(prmIni) + "') and (dataInicialInscricao <= '" + Uteis.getDataJDBC(prmFim) + "')) ORDER BY dataInicialInscricao";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado,nivelMontarDados,usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>CursoExtensao</code> através do valor do atributo 
     * <code>nome</code> da classe <code>ClassificaoCursoExtensao</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>CursoExtensaoVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNomeClassificaoCursoExtensao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT CursoExtensao.* FROM CursoExtensao, ClassificaoCursoExtensao WHERE CursoExtensao.classificacaoCursoExtensao = ClassificaoCursoExtensao.codigo and ClassificaoCursoExtensao.nome like('" + valorConsulta + "%') ORDER BY ClassificaoCursoExtensao.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado,nivelMontarDados,usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>CursoExtensao</code> através do valor do atributo 
     * <code>String situacaoFinanceira</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>CursoExtensaoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorSituacaoFinanceira(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM CursoExtensao WHERE situacaoFinanceira like('" + valorConsulta + "%') ORDER BY situacaoFinanceira";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado,nivelMontarDados,usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>CursoExtensao</code> através do valor do atributo 
     * <code>String situacao</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>CursoExtensaoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorSituacao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM CursoExtensao WHERE situacao like('" + valorConsulta + "%') ORDER BY situacao";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado,nivelMontarDados,usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>CursoExtensao</code> através do valor do atributo 
     * <code>Date dataFinal</code>. Retorna os objetos com valores pertecentes ao período informado por parâmetro.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>CursoExtensaoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorDataFinal(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM CursoExtensao WHERE ((dataFinal >= '" + Uteis.getDataJDBC(prmIni) + "') and (dataFinal <= '" + Uteis.getDataJDBC(prmFim) + "')) ORDER BY dataFinal";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado,nivelMontarDados,usuario));

    }

    /**
     * Responsável por realizar uma consulta de <code>CursoExtensao</code> através do valor do atributo 
     * <code>Date dataInicial</code>. Retorna os objetos com valores pertecentes ao período informado por parâmetro.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>CursoExtensaoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorDataInicial(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM CursoExtensao WHERE ((dataInicial >= '" + Uteis.getDataJDBC(prmIni) + "') and (dataInicial <= '" + Uteis.getDataJDBC(prmFim) + "')) ORDER BY dataInicial";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado,nivelMontarDados,usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>CursoExtensao</code> através do valor do atributo 
     * <code>Integer cargaHoraria</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>CursoExtensaoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCargaHoraria(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM CursoExtensao WHERE cargaHoraria >= " + valorConsulta.intValue() + " ORDER BY cargaHoraria";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado,nivelMontarDados,usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>CursoExtensao</code> através do valor do atributo 
     * <code>String nome</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>CursoExtensaoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM CursoExtensao WHERE nome like('" + valorConsulta + "%') ORDER BY nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado,nivelMontarDados,usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>CursoExtensao</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>CursoExtensaoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM CursoExtensao WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado,nivelMontarDados,usuario));

    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>CursoExtensaoVO</code> resultantes da consulta.
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
     * em um objeto da classe <code>CursoExtensaoVO</code>.
     * @return  O objeto da classe <code>CursoExtensaoVO</code> com os dados devidamente montados.
     */
    public static CursoExtensaoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        CursoExtensaoVO obj = new CursoExtensaoVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setNome(dadosSQL.getString("nome"));
        obj.setCargaHoraria(new Integer(dadosSQL.getInt("cargaHoraria")));
        obj.setDataInicial(dadosSQL.getDate("dataInicial"));
        obj.setDataFinal(dadosSQL.getDate("dataFinal"));
        obj.setEmenta(dadosSQL.getString("ementa"));
        obj.setConteudoProgramatico(dadosSQL.getString("conteudoProgramatico"));
        obj.setSituacao(dadosSQL.getString("situacao"));
        obj.setSituacaoFinanceira(dadosSQL.getString("situacaoFinanceira"));
        obj.getClassificacaoCursoExtensao().setCodigo(new Integer(dadosSQL.getInt("classificacaoCursoExtensao")));
        obj.setLocal(dadosSQL.getString("local"));
        obj.setHorario(dadosSQL.getString("horario"));
        obj.setDataInicialInscricao(dadosSQL.getDate("dataInicialInscricao"));
        obj.setDataFinalInscricao(dadosSQL.getDate("dataFinalInscricao"));
        obj.setValorComunidade(new Double(dadosSQL.getDouble("valorComunidade")));
        obj.setValorAluno(new Double(dadosSQL.getDouble("valorAluno")));
        obj.setValorFuncionario(new Double(dadosSQL.getDouble("valorFuncionario")));
        obj.setValorProfessor(new Double(dadosSQL.getDouble("valorProfessor")));
        obj.setValorInscricaoComunidade(new Double(dadosSQL.getDouble("valorInscricaoComunidade")));
        obj.setValorInscricaoAluno(new Double(dadosSQL.getDouble("valorInscricaoAluno")));
        obj.setValorInscricaoFuncionario(new Double(dadosSQL.getDouble("valorInscricaoFuncionario")));
        obj.setValorInscricaoProfessor(new Double(dadosSQL.getDouble("valorInscricaoProfessor")));
        obj.setCobrarInscricao(new Boolean(dadosSQL.getBoolean("cobrarInscricao")));
        obj.setInscricaoPelaInternet(new Boolean(dadosSQL.getBoolean("inscricaoPelaInternet")));
        obj.setConfirmacaoPresencial(new Boolean(dadosSQL.getBoolean("confirmacaoPresencial")));
        obj.getUnidadeEnsino().setCodigo(new Integer(dadosSQL.getInt("unidadeEnsino")));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        obj.setProfessorCursoExtensaoVOs(ProfessorCursoExtensao.consultarProfessorCursoExtensaos(obj.getCodigo(), false,usuario));
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS) {
            return obj;
        }
        montarDadosClassificacaoCursoExtensao(obj, nivelMontarDados,usuario);
        montarDadosUnidadeEnsino(obj, nivelMontarDados, usuario);
        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>UnidadeEnsinoVO</code> relacionado ao objeto <code>CursoExtensaoVO</code>.
     * Faz uso da chave primária da classe <code>UnidadeEnsinoVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosUnidadeEnsino(CursoExtensaoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getUnidadeEnsino().getCodigo().intValue() == 0) {
            obj.setUnidadeEnsino(new UnidadeEnsinoVO());
            return;
        }
        obj.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsino().getCodigo(), false, nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>ClassificaoCursoExtensaoVO</code> relacionado ao objeto <code>CursoExtensaoVO</code>.
     * Faz uso da chave primária da classe <code>ClassificaoCursoExtensaoVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosClassificacaoCursoExtensao(CursoExtensaoVO obj, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		if (obj.getClassificacaoCursoExtensao().getCodigo().intValue() == 0) {
            obj.setClassificacaoCursoExtensao(new ClassificaoCursoExtensaoVO());
            return;
        }
        obj.setClassificacaoCursoExtensao(getFacadeFactory().getClassificacaoCursoExtensaoFacade().consultarPorChavePrimaria(obj.getClassificacaoCursoExtensao().getCodigo(),usuario));
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>CursoExtensaoVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public CursoExtensaoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false,usuario);
        String sql = "SELECT * FROM CursoExtensao WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] {codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado,nivelMontarDados,usuario));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return CursoExtensao.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        CursoExtensao.idEntidade = idEntidade;
    }
}
