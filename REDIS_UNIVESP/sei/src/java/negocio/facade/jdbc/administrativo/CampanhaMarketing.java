package negocio.facade.jdbc.administrativo;

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

import negocio.comuns.administrativo.CampanhaMarketingVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.administrativo.CampanhaMarketingInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>CampanhaMarketingVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>CampanhaMarketingVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see CampanhaMarketingVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy 
public class CampanhaMarketing extends ControleAcesso implements CampanhaMarketingInterfaceFacade {

    protected static String idEntidade;

    public CampanhaMarketing() throws Exception {
        super();
        setIdEntidade("CampanhaMarketing");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>CampanhaMarketingVO</code>.
     */
    public CampanhaMarketingVO novo() throws Exception {
        CampanhaMarketing.incluir(getIdEntidade());
        CampanhaMarketingVO obj = new CampanhaMarketingVO();
        return obj;
    }

    public void autorizarCampanha(CampanhaMarketingVO obj, UsuarioVO usuario) throws Exception {
    	ControleAcesso.verificarPermissaoUsuarioFuncionalidade("CampanhaMarketing_AutorizarCampanha", usuario);
        obj.setSituacao("EE");
        obj.setDataAutorizacao(new Date());
    }

    public void indeferirCampanha(CampanhaMarketingVO obj, UsuarioVO usuario) throws Exception {
    	ControleAcesso.verificarPermissaoUsuarioFuncionalidade("CampanhaMarketing_AutorizarCampanha", usuario);
        obj.setSituacao("II");
        obj.setDataAutorizacao(new Date());
    }

    public void finalizarCampanha(CampanhaMarketingVO obj, UsuarioVO usuario) throws Exception {
        ControleAcesso.verificarPermissaoUsuarioFuncionalidade("CampanhaMarketing_FinalizarCampanha", usuario);
        if (!obj.getSituacao().equals("EE")) {
            throw new Exception("Uma campanha só pode ser finalizada após ter sido autorizada!");
        } else {
            obj.setSituacao("FI");
            obj.setDataFinalizacaoCampanha(new Date());
        }
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>CampanhaMarketingVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>CampanhaMarketingVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final CampanhaMarketingVO obj, UsuarioVO usuario) throws Exception {
        try {
            validarUnicidade(obj, usuario);
            CampanhaMarketingVO.validarDados(obj);
            CampanhaMarketing.incluir(getIdEntidade());
            final String sql = "INSERT INTO CampanhaMarketing( descricao, situacao, objetivo, publicoAlvo, dataRequisicao, dataFinalizacaoCampanha, dataInicioVinculacao, dataFimVinculacao, nrPessoasImpactadas, resultados, requisitante, custoEstimado, custoEfetivado, responsavelAutorizacao, responsavelFinalizacao, dataAutorizacao ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo";
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement sqlInserir = con.prepareStatement(sql);
                    sqlInserir.setString(1, obj.getDescricao());
                    //sqlInserir.setInt( 2, obj.getTipoMidiaCaptacao().getCodigo().intValue() );
                    sqlInserir.setString(2, obj.getSituacao());
                    sqlInserir.setString(3, obj.getObjetivo());
                    sqlInserir.setString(4, obj.getPublicoAlvo());
                    sqlInserir.setDate(5, Uteis.getDataJDBC(obj.getDataRequisicao()));
                    sqlInserir.setDate(6, Uteis.getDataJDBC(obj.getDataFinalizacaoCampanha()));
                    sqlInserir.setDate(7, Uteis.getDataJDBC(obj.getDataInicioVinculacao()));
                    sqlInserir.setDate(8, Uteis.getDataJDBC(obj.getDataFimVinculacao()));
                    sqlInserir.setInt(9, obj.getNrPessoasImpactadas().intValue());
                    sqlInserir.setString(10, obj.getResultados());
                    sqlInserir.setInt(11, obj.getRequisitante().getCodigo().intValue());
                    sqlInserir.setDouble(12, obj.getCustoEstimado().doubleValue());
                    sqlInserir.setDouble(13, obj.getCustoEfetivado().doubleValue());
                    if (obj.getResponsavelAutorizacao().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(14, obj.getResponsavelAutorizacao().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(14, 0);
                    }
                    if (obj.getResponsavelFinalizacao().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(15, obj.getResponsavelFinalizacao().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(15, 0);
                    }
                    sqlInserir.setDate(16, Uteis.getDataJDBC(obj.getDataAutorizacao()));
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
            getFacadeFactory().getCampanhaMarketingMidiaFacade().incluirMidia(obj.getCodigo(), obj.getCampanhaMarketingMidiaVOs());
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>CampanhaMarketingVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>CampanhaMarketingVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final CampanhaMarketingVO obj, UsuarioVO usuario) throws Exception {
        try {
            validarUnicidade(obj, usuario);
            CampanhaMarketingVO.validarDados(obj);
            CampanhaMarketing.alterar(getIdEntidade());
            final String sql = "UPDATE CampanhaMarketing set descricao=?, situacao=?, objetivo=?, publicoAlvo=?, dataRequisicao=?, dataFinalizacaoCampanha=?, dataInicioVinculacao=?, dataFimVinculacao=?, nrPessoasImpactadas=?, resultados=?, requisitante=?, custoEstimado=?, custoEfetivado=?, responsavelAutorizacao=?, responsavelFinalizacao=?, dataAutorizacao=? WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement sqlAlterar = con.prepareStatement(sql);
                    sqlAlterar.setString(1, obj.getDescricao());
                    sqlAlterar.setString(2, obj.getSituacao());
                    sqlAlterar.setString(3, obj.getObjetivo());
                    sqlAlterar.setString(4, obj.getPublicoAlvo());
                    sqlAlterar.setDate(5, Uteis.getDataJDBC(obj.getDataRequisicao()));
                    sqlAlterar.setDate(6, Uteis.getDataJDBC(obj.getDataFinalizacaoCampanha()));
                    sqlAlterar.setDate(7, Uteis.getDataJDBC(obj.getDataInicioVinculacao()));
                    sqlAlterar.setDate(8, Uteis.getDataJDBC(obj.getDataFimVinculacao()));
                    sqlAlterar.setInt(9, obj.getNrPessoasImpactadas().intValue());
                    sqlAlterar.setString(10, obj.getResultados());
                    sqlAlterar.setInt(11, obj.getRequisitante().getCodigo().intValue());
                    sqlAlterar.setDouble(12, obj.getCustoEstimado().doubleValue());
                    sqlAlterar.setDouble(13, obj.getCustoEfetivado().doubleValue());
                    if (obj.getResponsavelAutorizacao().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(14, obj.getResponsavelAutorizacao().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(14, 0);
                    }
                    if (obj.getResponsavelFinalizacao().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(15, obj.getResponsavelFinalizacao().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(15, 0);
                    }
                    sqlAlterar.setDate(16, Uteis.getDataJDBC(obj.getDataAutorizacao()));
                    sqlAlterar.setInt(17, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });
            getFacadeFactory().getCampanhaMarketingMidiaFacade().alterarMidia(obj.getCodigo(), obj.getCampanhaMarketingMidiaVOs());
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>CampanhaMarketingVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>CampanhaMarketingVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(CampanhaMarketingVO obj) throws Exception {
        try {
            CampanhaMarketing.excluir(getIdEntidade());
            String sql = "DELETE FROM CampanhaMarketing WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql,new Object[] {obj.getCodigo()});
            getFacadeFactory().getCampanhaMarketingMidiaFacade().excluirMidia(obj.getCodigo());
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>CampanhaMarketing</code> através do valor do atributo 
     * <code>nome</code> da classe <code>Pessoa</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>CampanhaMarketingVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNomePessoa(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT CampanhaMarketing.* FROM CampanhaMarketing, Funcionario, Pessoa WHERE CampanhaMarketing.requisitante = Funcionario.codigo and Funcionario.pessoa = Pessoa.codigo and lower (Pessoa.nome) like('" + valorConsulta.toLowerCase() + "%') ORDER BY Pessoa.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>CampanhaMarketing</code> através do valor do atributo 
     * <code>Date dataRequisicao</code>. Retorna os objetos com valores pertecentes ao período informado por parâmetro.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>CampanhaMarketingVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorDataRequisicao(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM CampanhaMarketing WHERE ((dataRequisicao >= '" + Uteis.getDataJDBC(prmIni) + "') and (dataRequisicao <= '" + Uteis.getDataJDBC(prmFim) + "')) ORDER BY dataRequisicao";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
    }

    public List consultarPorDataVinculacao(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM CampanhaMarketing WHERE ((datainiciovinculacao >= '" + Uteis.getDataJDBC(prmIni) + "') and (datafimvinculacao <= '" + Uteis.getDataJDBC(prmFim) + "')) ORDER BY datainiciovinculacao";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
    }

    public List consultarPorDataAutorizacao(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM CampanhaMarketing WHERE ((dataautorizacao >= '" + Uteis.getDataJDBC(prmIni) + "') and (datafinalizacaocampanha<= '" + Uteis.getDataJDBC(prmFim) + "')) ORDER BY dataautorizacao ";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
    }

    public List consultarPorMatricula(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT funcionario.*, campanhamarketing.* FROM campanhamarketing, funcionario WHERE  lower (funcionario.matricula) like ('" + valorConsulta + "%')  and campanhamarketing.requisitante = funcionario.codigo ORDER BY campanhaMarketing.requisitante";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>CampanhaMarketing</code> através do valor do atributo 
     * <code>String situacao</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>CampanhaMarketingVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorSituacao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM CampanhaMarketing WHERE lower (situacao) like('" + valorConsulta.toLowerCase() + "%') ORDER BY situacao";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>CampanhaMarketing</code> através do valor do atributo 
     * <code>nomeMidia</code> da classe <code>TipoMidiaCaptacao</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>CampanhaMarketingVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    /**
     * Responsável por realizar uma consulta de <code>CampanhaMarketing</code> através do valor do atributo 
     * <code>String descricao</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>CampanhaMarketingVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorDescricao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM CampanhaMarketing WHERE lower (descricao) like('" + valorConsulta.toLowerCase() + "%') ORDER BY descricao";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>CampanhaMarketing</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>CampanhaMarketingVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM CampanhaMarketing WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>CampanhaMarketingVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList();
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados,usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>CampanhaMarketingVO</code>.
     * @return  O objeto da classe <code>CampanhaMarketingVO</code> com os dados devidamente montados.
     */
    public static CampanhaMarketingVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        CampanhaMarketingVO obj = new CampanhaMarketingVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setDescricao(dadosSQL.getString("descricao"));
        obj.setSituacao(dadosSQL.getString("situacao"));
        obj.getRequisitante().setCodigo(new Integer(dadosSQL.getInt("requisitante")));
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
            montarDadosRequisitante(obj, nivelMontarDados,usuario);
            return obj;
        }

        obj.setObjetivo(dadosSQL.getString("objetivo"));
        obj.setPublicoAlvo(dadosSQL.getString("publicoAlvo"));
        obj.setDataRequisicao(dadosSQL.getDate("dataRequisicao"));
        obj.setDataFinalizacaoCampanha(dadosSQL.getDate("dataFinalizacaoCampanha"));
        obj.setDataInicioVinculacao(dadosSQL.getDate("dataInicioVinculacao"));
        obj.setDataFimVinculacao(dadosSQL.getDate("dataFimVinculacao"));
        obj.setNrPessoasImpactadas(new Integer(dadosSQL.getInt("nrPessoasImpactadas")));
        obj.setResultados(dadosSQL.getString("resultados"));
        obj.setCustoEstimado(new Double(dadosSQL.getDouble("custoEstimado")));
        obj.setCustoEfetivado(new Double(dadosSQL.getDouble("custoEfetivado")));
        obj.getResponsavelAutorizacao().setCodigo(new Integer(dadosSQL.getInt("responsavelAutorizacao")));
        obj.getResponsavelFinalizacao().setCodigo(new Integer(dadosSQL.getInt("responsavelFinalizacao")));
        obj.setDataAutorizacao(dadosSQL.getDate("dataAutorizacao"));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        obj.setCampanhaMarketingMidiaVOs(getFacadeFactory().getCampanhaMarketingMidiaFacade().consultarMidias(obj.getCodigo()));
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS) {
            return obj;
        }
        montarDadosRequisitante(obj, nivelMontarDados,usuario);
        montarDadosResponsavelAutorizacao(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,usuario);
        montarDadosResponsavelFinalizacao(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,usuario);
        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>PessoaVO</code> relacionado ao objeto <code>CampanhaMarketingVO</code>.
     * Faz uso da chave primária da classe <code>PessoaVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosResponsavelAutorizacao(CampanhaMarketingVO obj, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if (obj.getResponsavelAutorizacao().getCodigo().intValue() == 0) {
            obj.setResponsavelAutorizacao(new UsuarioVO());
            return;
        }
        obj.setResponsavelAutorizacao(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavelAutorizacao().getCodigo(), nivelMontarDados,usuario));
    }

    public static void montarDadosResponsavelFinalizacao(CampanhaMarketingVO obj, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if (obj.getResponsavelFinalizacao().getCodigo().intValue() == 0) {
            obj.setResponsavelFinalizacao(new UsuarioVO());
            return;
        }
        obj.setResponsavelFinalizacao(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavelAutorizacao().getCodigo(), nivelMontarDados,usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>PessoaVO</code> relacionado ao objeto <code>CampanhaMarketingVO</code>.
     * Faz uso da chave primária da classe <code>PessoaVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosRequisitante(CampanhaMarketingVO obj, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if (obj.getRequisitante().getCodigo().intValue() == 0) {
            obj.setRequisitante(new FuncionarioVO());
        }
        obj.setRequisitante(getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimaria(obj.getRequisitante().getCodigo(), obj.getRequisitante().getUnidadeEnsino().getCodigo(), true, nivelMontarDados,usuario));
    }
    
    /**
     * Operação responsável por localizar um objeto da classe <code>CampanhaMarketingVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public CampanhaMarketingVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM CampanhaMarketing WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] {codigoPrm.intValue()});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado, nivelMontarDados,usuario));
    }

    public void validarUnicidade(CampanhaMarketingVO obj, UsuarioVO usuario) throws ConsistirException, Exception {
		List<CampanhaMarketingVO> lista = consultarPorDescricao(obj.getDescricao(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
                for (CampanhaMarketingVO repetido : lista) {
			if (repetido.getDescricao().toLowerCase().equals(obj.getDescricao().toLowerCase())) {
				throw new ConsistirException("O campo Descrição já esta cadastrado!");
			}
		}
	}

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return CampanhaMarketing.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        CampanhaMarketing.idEntidade = idEntidade;
    }
}
