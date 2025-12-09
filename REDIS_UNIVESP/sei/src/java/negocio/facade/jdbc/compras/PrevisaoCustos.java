package negocio.facade.jdbc.compras;

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
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.compras.ClassificaoCustosVO;
import negocio.comuns.compras.PrevisaoCustosVO;
import negocio.comuns.eventos.EventoVO;
import negocio.comuns.extensao.CursoExtensaoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.compras.PrevisaoCustosInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>PrevisaoCustosVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>PrevisaoCustosVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see PrevisaoCustosVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy 
public class PrevisaoCustos extends ControleAcesso implements PrevisaoCustosInterfaceFacade {

    protected static String idEntidade;

    public PrevisaoCustos() throws Exception {
        super();
        setIdEntidade("PrevisaoCustos");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>PrevisaoCustosVO</code>.
     */
    public PrevisaoCustosVO novo() throws Exception {
        PrevisaoCustos.incluir(getIdEntidade());
        PrevisaoCustosVO obj = new PrevisaoCustosVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>PrevisaoCustosVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>PrevisaoCustosVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @SuppressWarnings("static-access")
    public void incluir(final PrevisaoCustosVO obj) throws Exception {
        try {
            PrevisaoCustosVO.validarDados(obj);
            PrevisaoCustos.incluir(getIdEntidade());
            final String sql = "INSERT INTO PrevisaoCustos( descricao, data, classificaoCustos, responsavelRequisicao, autorizacaoCustos, curso, evento, cursoExtensao, unidadeEnsino, valorEstimado, valorGasto, tipoDestinacaoCusto, pagamentoServico, cargaHoraria, valorPagamentoHora ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )returning codigo";
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    sqlInserir.setString(1, obj.getDescricao());
                    sqlInserir.setDate(2, Uteis.getDataJDBC(obj.getData()));
                    sqlInserir.setInt(3, obj.getClassificaoCustos().getCodigo().intValue());
                    sqlInserir.setInt(4, obj.getResponsavelRequisicao().getCodigo().intValue());
                    sqlInserir.setInt(5, obj.getAutorizacaoCustos().getCodigo().intValue());
                    sqlInserir.setInt(6, obj.getCurso().getCodigo().intValue());
                    sqlInserir.setInt(7, obj.getEvento().getCodigo().intValue());
                    sqlInserir.setInt(8, obj.getCursoExtensao().getCodigo().intValue());
                    sqlInserir.setInt(9, obj.getUnidadeEnsino().getCodigo().intValue());
                    sqlInserir.setDouble(10, obj.getValorEstimado().doubleValue());
                    sqlInserir.setDouble(11, obj.getValorGasto().doubleValue());
                    sqlInserir.setString(12, obj.getTipoDestinacaoCusto());
                    sqlInserir.setBoolean(13, obj.isPagamentoServico().booleanValue());
                    sqlInserir.setInt(14, obj.getCargaHoraria().intValue());
                    sqlInserir.setDouble(15, obj.getValorPagamentoHora().doubleValue());
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
            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>PrevisaoCustosVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>PrevisaoCustosVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @SuppressWarnings("static-access")
    public void alterar(final PrevisaoCustosVO obj) throws Exception {
        try {
            PrevisaoCustosVO.validarDados(obj);
            PrevisaoCustos.alterar(getIdEntidade());
            final String sql = "UPDATE PrevisaoCustos set descricao=?, data=?, classificaoCustos=?, responsavelRequisicao=?, autorizacaoCustos=?, curso=?, evento=?, cursoExtensao=?, unidadeEnsino=?, valorEstimado=?, valorGasto=?, tipoDestinacaoCusto=?, pagamentoServico=?, cargaHoraria=?, valorPagamentoHora=? WHERE ((codigo = ?))";
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setString(1, obj.getDescricao());
                    sqlAlterar.setDate(2, Uteis.getDataJDBC(obj.getData()));
                    sqlAlterar.setInt(3, obj.getClassificaoCustos().getCodigo().intValue());
                    sqlAlterar.setInt(4, obj.getResponsavelRequisicao().getCodigo().intValue());
                    sqlAlterar.setInt(5, obj.getAutorizacaoCustos().getCodigo().intValue());
                    sqlAlterar.setInt(6, obj.getCurso().getCodigo().intValue());
                    sqlAlterar.setInt(7, obj.getEvento().getCodigo().intValue());
                    sqlAlterar.setInt(8, obj.getCursoExtensao().getCodigo().intValue());
                    sqlAlterar.setInt(9, obj.getUnidadeEnsino().getCodigo().intValue());
                    sqlAlterar.setDouble(10, obj.getValorEstimado().doubleValue());
                    sqlAlterar.setDouble(11, obj.getValorGasto().doubleValue());
                    sqlAlterar.setString(12, obj.getTipoDestinacaoCusto());
                    sqlAlterar.setBoolean(13, obj.isPagamentoServico().booleanValue());
                    sqlAlterar.setInt(14, obj.getCargaHoraria().intValue());
                    sqlAlterar.setDouble(15, obj.getValorPagamentoHora().doubleValue());
                    sqlAlterar.setInt(16, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            }));

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>PrevisaoCustosVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>PrevisaoCustosVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @SuppressWarnings("static-access")
    public void excluir(PrevisaoCustosVO obj) throws Exception {
        try {
            PrevisaoCustos.excluir(getIdEntidade());
            String sql = "DELETE FROM PrevisaoCustos WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>PrevisaoCustos</code> através do valor do atributo 
     * <code>Double valorPagamentoHora</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>PrevisaoCustosVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    @SuppressWarnings("static-access")
    public List consultarPorValorPagamentoHora(Double valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM PrevisaoCustos WHERE valorPagamentoHora >= " + valorConsulta.doubleValue() + " ORDER BY valorPagamentoHora";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>PrevisaoCustos</code> através do valor do atributo 
     * <code>Integer cargaHoraria</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>PrevisaoCustosVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    @SuppressWarnings("static-access")
    public List consultarPorCargaHoraria(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM PrevisaoCustos WHERE cargaHoraria >= " + valorConsulta.intValue() + " ORDER BY cargaHoraria";

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>PrevisaoCustos</code> através do valor do atributo 
     * <code>String tipoDestinacaoCusto</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>PrevisaoCustosVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    @SuppressWarnings("static-access")
    public List consultarPorTipoDestinacaoCusto(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM PrevisaoCustos WHERE tipoDestinacaoCusto like('" + valorConsulta + "%') ORDER BY tipoDestinacaoCusto";

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>PrevisaoCustos</code> através do valor do atributo 
     * <code>Double valorGasto</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>PrevisaoCustosVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    @SuppressWarnings("static-access")
    public List consultarPorValorGasto(Double valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM PrevisaoCustos WHERE valorGasto >= " + valorConsulta.doubleValue() + " ORDER BY valorGasto";

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>PrevisaoCustos</code> através do valor do atributo 
     * <code>Double valorEstimado</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>PrevisaoCustosVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    @SuppressWarnings("static-access")
    public List consultarPorValorEstimado(Double valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM PrevisaoCustos WHERE valorEstimado >= " + valorConsulta.doubleValue() + " ORDER BY valorEstimado";

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>PrevisaoCustos</code> através do valor do atributo 
     * <code>nome</code> da classe <code>UnidadeEnsino</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>PrevisaoCustosVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @SuppressWarnings("static-access")
    public List consultarPorNomeUnidadeEnsino(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT PrevisaoCustos.* FROM PrevisaoCustos, UnidadeEnsino WHERE PrevisaoCustos.unidadeEnsino = UnidadeEnsino.codigo and UnidadeEnsino.nome like('" + valorConsulta + "%') ORDER BY UnidadeEnsino.nome";

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>PrevisaoCustos</code> através do valor do atributo 
     * <code>nome</code> da classe <code>CursoExtensao</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>PrevisaoCustosVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @SuppressWarnings("static-access")
    public List consultarPorNomeCursoExtensao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT PrevisaoCustos.* FROM PrevisaoCustos, CursoExtensao WHERE PrevisaoCustos.cursoExtensao = CursoExtensao.codigo and CursoExtensao.nome like('" + valorConsulta + "%') ORDER BY CursoExtensao.nome";

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>PrevisaoCustos</code> através do valor do atributo 
     * <code>nome</code> da classe <code>Evento</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>PrevisaoCustosVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @SuppressWarnings("static-access")
    public List consultarPorNomeEvento(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT PrevisaoCustos.* FROM PrevisaoCustos, Evento WHERE PrevisaoCustos.evento = Evento.codigo and Evento.nome like('" + valorConsulta + "%') ORDER BY Evento.nome";

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>PrevisaoCustos</code> através do valor do atributo 
     * <code>nome</code> da classe <code>Curso</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>PrevisaoCustosVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @SuppressWarnings("static-access")
    public List consultarPorNomeCurso(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT PrevisaoCustos.* FROM PrevisaoCustos, Curso WHERE PrevisaoCustos.curso = Curso.codigo and Curso.nome like('" + valorConsulta + "%') ORDER BY Curso.nome";

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>PrevisaoCustos</code> através do valor do atributo 
     * <code>nome</code> da classe <code>Pessoa</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>PrevisaoCustosVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @SuppressWarnings("static-access")
    public List consultarPorNomePessoa(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT PrevisaoCustos.* FROM PrevisaoCustos, Pessoa WHERE PrevisaoCustos.responsavelRequisicao = Pessoa.codigo and Pessoa.nome like('" + valorConsulta + "%') ORDER BY Pessoa.nome";

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>PrevisaoCustos</code> através do valor do atributo 
     * <code>descricao</code> da classe <code>ClassificaoCustos</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>PrevisaoCustosVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @SuppressWarnings("static-access")
    public List consultarPorDescricaoClassificaoCustos(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT PrevisaoCustos.* FROM PrevisaoCustos, ClassificaoCustos WHERE PrevisaoCustos.classificaoCustos = ClassificaoCustos.codigo and ClassificaoCustos.descricao like('" + valorConsulta + "%') ORDER BY ClassificaoCustos.descricao";

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>PrevisaoCustos</code> através do valor do atributo 
     * <code>Date data</code>. Retorna os objetos com valores pertecentes ao período informado por parâmetro.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>PrevisaoCustosVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    @SuppressWarnings("static-access")
    public List consultarPorData(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM PrevisaoCustos WHERE ((data >= '" + Uteis.getDataJDBC(prmIni) + "') and (data <= '" + Uteis.getDataJDBC(prmFim) + "')) ORDER BY data";

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>PrevisaoCustos</code> através do valor do atributo 
     * <code>String descricao</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>PrevisaoCustosVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    @SuppressWarnings("static-access")
    public List consultarPorDescricao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM PrevisaoCustos WHERE descricao like('" + valorConsulta + "%') ORDER BY descricao";

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>PrevisaoCustos</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>PrevisaoCustosVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    @SuppressWarnings("static-access")
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM PrevisaoCustos WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>PrevisaoCustosVO</code> resultantes da consulta.
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
     * em um objeto da classe <code>PrevisaoCustosVO</code>.
     * @return  O objeto da classe <code>PrevisaoCustosVO</code> com os dados devidamente montados.
     */
    public static PrevisaoCustosVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        PrevisaoCustosVO obj = new PrevisaoCustosVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setDescricao(dadosSQL.getString("descricao"));
        obj.setData(dadosSQL.getDate("data"));
        obj.getClassificaoCustos().setCodigo(new Integer(dadosSQL.getInt("classificaoCustos")));
        obj.getResponsavelRequisicao().setCodigo(new Integer(dadosSQL.getInt("responsavelRequisicao")));
        obj.getAutorizacaoCustos().setCodigo(new Integer(dadosSQL.getInt("autorizacaoCustos")));
        obj.getCurso().setCodigo(new Integer(dadosSQL.getInt("curso")));
        obj.getEvento().setCodigo(new Integer(dadosSQL.getInt("evento")));
        obj.getCursoExtensao().setCodigo(new Integer(dadosSQL.getInt("cursoExtensao")));
        obj.getUnidadeEnsino().setCodigo(new Integer(dadosSQL.getInt("unidadeEnsino")));
        obj.setValorEstimado(new Double(dadosSQL.getDouble("valorEstimado")));
        obj.setValorGasto(new Double(dadosSQL.getDouble("valorGasto")));
        obj.setTipoDestinacaoCusto(dadosSQL.getString("tipoDestinacaoCusto"));
        obj.setPagamentoServico(new Boolean(dadosSQL.getBoolean("pagamentoServico")));
        obj.setCargaHoraria(new Integer(dadosSQL.getInt("cargaHoraria")));
        obj.setValorPagamentoHora(new Double(dadosSQL.getDouble("valorPagamentoHora")));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        montarDadosClassificaoCustos(obj, nivelMontarDados,usuario);
        montarDadosResponsavelRequisicao(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,usuario);
        montarDadosAutorizacaoCustos(obj, nivelMontarDados,usuario);
        montarDadosCurso(obj, nivelMontarDados,usuario);
        montarDadosEvento(obj, nivelMontarDados,usuario);
        montarDadosCursoExtensao(obj, nivelMontarDados,usuario);
        montarDadosUnidadeEnsino(obj, nivelMontarDados,usuario);
        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>UnidadeEnsinoVO</code> relacionado ao objeto <code>PrevisaoCustosVO</code>.
     * Faz uso da chave primária da classe <code>UnidadeEnsinoVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosUnidadeEnsino(PrevisaoCustosVO obj, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if (obj.getUnidadeEnsino().getCodigo().intValue() == 0) {
            obj.setUnidadeEnsino(new UnidadeEnsinoVO());
            return;
        }
        obj.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsino().getCodigo(), false, nivelMontarDados,usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>CursoExtensaoVO</code> relacionado ao objeto <code>PrevisaoCustosVO</code>.
     * Faz uso da chave primária da classe <code>CursoExtensaoVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosCursoExtensao(PrevisaoCustosVO obj, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if (obj.getCursoExtensao().getCodigo().intValue() == 0) {
            obj.setCursoExtensao(new CursoExtensaoVO());
            return;
        }
        obj.setCursoExtensao(getFacadeFactory().getCursoExtensaoFacade().consultarPorChavePrimaria(obj.getCursoExtensao().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS,usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>EventoVO</code> relacionado ao objeto <code>PrevisaoCustosVO</code>.
     * Faz uso da chave primária da classe <code>EventoVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosEvento(PrevisaoCustosVO obj, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if (obj.getEvento().getCodigo().intValue() == 0) {
            obj.setEvento(new EventoVO());
            return;
        }
        obj.setEvento(getFacadeFactory().getEventoFacade().consultarPorChavePrimaria(obj.getEvento().getCodigo(), nivelMontarDados,usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>CursoVO</code> relacionado ao objeto <code>PrevisaoCustosVO</code>.
     * Faz uso da chave primária da classe <code>CursoVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosCurso(PrevisaoCustosVO obj, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if (obj.getCurso().getCodigo().intValue() == 0) {
            obj.setCurso(new CursoVO());
            return;
        }
        obj.setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(obj.getCurso().getCodigo(), nivelMontarDados, false, usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>PessoaVO</code> relacionado ao objeto <code>PrevisaoCustosVO</code>.
     * Faz uso da chave primária da classe <code>PessoaVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosAutorizacaoCustos(PrevisaoCustosVO obj, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if (obj.getAutorizacaoCustos().getCodigo().intValue() == 0) {
            obj.setAutorizacaoCustos(new PessoaVO());
            return;
        }
        obj.setAutorizacaoCustos(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(obj.getAutorizacaoCustos().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>PessoaVO</code> relacionado ao objeto <code>PrevisaoCustosVO</code>.
     * Faz uso da chave primária da classe <code>PessoaVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosResponsavelRequisicao(PrevisaoCustosVO obj, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if (obj.getResponsavelRequisicao().getCodigo().intValue() == 0) {
            obj.setResponsavelRequisicao(new PessoaVO());
            return;
        }
        obj.setResponsavelRequisicao(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(obj.getResponsavelRequisicao().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>ClassificaoCustosVO</code> relacionado ao objeto <code>PrevisaoCustosVO</code>.
     * Faz uso da chave primária da classe <code>ClassificaoCustosVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosClassificaoCustos(PrevisaoCustosVO obj, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if (obj.getClassificaoCustos().getCodigo().intValue() == 0) {
            obj.setClassificaoCustos(new ClassificaoCustosVO());
            return;
        }
        obj.setClassificaoCustos(getFacadeFactory().getClassificacaoCustosFacade().consultarPorChavePrimaria(obj.getClassificaoCustos().getCodigo(), false,usuario));
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>PrevisaoCustosVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    @SuppressWarnings("static-access")
    public PrevisaoCustosVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sql = "SELECT * FROM PrevisaoCustos WHERE codigo = ?";

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
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
        return PrevisaoCustos.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        PrevisaoCustos.idEntidade = idEntidade;
    }
}