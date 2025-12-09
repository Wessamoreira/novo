package negocio.facade.jdbc.financeiro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
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

import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.LiberacaoFinanceiroCancelamentoTrancamentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.financeiro.LiberacaoFinanceiroCancelamentoTrancamentoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>LiberacaoFinanceiroCancelamentoTrancamentoVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>LiberacaoFinanceiroCancelamentoTrancamentoVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see LiberacaoFinanceiroCancelamentoTrancamentoVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy 
public class LiberacaoFinanceiroCancelamentoTrancamento extends ControleAcesso implements LiberacaoFinanceiroCancelamentoTrancamentoInterfaceFacade {

    protected static String idEntidade;

    public LiberacaoFinanceiroCancelamentoTrancamento() throws Exception {
        super();
        setIdEntidade("LiberacaoFinanceiroCancelamentoTrancamento");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>LiberacaoFinanceiroCancelamentoTrancamentoVO</code>.
     */
    public LiberacaoFinanceiroCancelamentoTrancamentoVO novo() throws Exception {
        LiberacaoFinanceiroCancelamentoTrancamento.incluir(getIdEntidade());
        LiberacaoFinanceiroCancelamentoTrancamentoVO obj = new LiberacaoFinanceiroCancelamentoTrancamentoVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>LiberacaoFinanceiroCancelamentoTrancamentoVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>LiberacaoFinanceiroCancelamentoTrancamentoVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void removerContasReceber(List lista, UsuarioVO usuario) throws Exception {
        Iterator i = lista.iterator();
        while (i.hasNext()) {
            ContaReceberVO c = (ContaReceberVO) i.next();
            if (c.getRemover()) {
            	getFacadeFactory().getMatriculaPeriodoVencimentoFacade().excluirContaReceber(c.getCodigo(), usuario);
            	getFacadeFactory().getControleRemessaContaReceberFacade().removerVinculoContaReceber(c.getCodigo(), usuario);
                getFacadeFactory().getContaReceberFacade().excluir(c, usuario);
            }
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void baixarContasReceber(LiberacaoFinanceiroCancelamentoTrancamentoVO obj, List lista,ConfiguracaoFinanceiroVO configuracaoFinanceiroVO ,UsuarioVO usuario) throws Exception {
        Iterator i = lista.iterator();
        while (i.hasNext()) {
            ContaReceberVO c = (ContaReceberVO) i.next();
            if (c.getIsentar()) {
                getFacadeFactory().getContaReceberFacade().baixarContaReceberConcedendoDescontoTotalAMesma(c.getCodigo(), new Date(), "Isenção concedida durante liberação para cancelamento de matrícula (código da liberação: " + obj.getCodigo() + ")", true, false, obj.getResponsavel(), configuracaoFinanceiroVO, usuario );
            }
            if (c.isCancelar()) {
                   getFacadeFactory().getContaReceberFacade().cancelarContaReceber(c, usuario);
            }
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir( final LiberacaoFinanceiroCancelamentoTrancamentoVO obj, List listaPendencias, List listaContaReceber, List listaContaReceberPersistirBanco ,ConfiguracaoFinanceiroVO configuracaoFinanceiroVO ,UsuarioVO usuario) throws Exception {
        try {
            LiberacaoFinanceiroCancelamentoTrancamentoVO.validarDados(obj);
            LiberacaoFinanceiroCancelamentoTrancamento.incluir(getIdEntidade(), true, usuario);
            obj.realizarUpperCaseDados();
            final String sql = "INSERT INTO LiberacaoFinanceiroCancelamentoTrancamento( matriculaAluno, unidadeEnsino, dataCadastro, responsavel, motivoLiberacaoFinanceiro ) VALUES ( ?, ?, ?, ?, ? ) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
                    PreparedStatement sqlInserir = cnctn.prepareStatement(sql);
                    if (!obj.getMatriculaAluno().getMatricula().equals("")) {
                        sqlInserir.setString(1, obj.getMatriculaAluno().getMatricula());
                    } else {
                        sqlInserir.setNull(1, 0);
                    }
                    if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(2, obj.getUnidadeEnsino().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(2, 0);
                    }
                    sqlInserir.setDate(3, Uteis.getDataJDBC(obj.getDataCadastro()));
                    if (obj.getResponsavel().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(4, obj.getResponsavel().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(4, 0);
                    }
                    sqlInserir.setString(5, obj.getMotivoLiberacaoFinanceiro());
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
            getFacadeFactory().getHistoricoLiberacaoFinanceiroCancelamentoTrancamentoFacade().incluirHistoricoLiberacaoFinanceiroCancelamentoTrancamentos(obj.getCodigo(), obj.getHistoricoLiberacaoFinanceiroCancelamentoTrancamentoVOs(), usuario);
            baixarContasReceber(obj, listaContaReceber, configuracaoFinanceiroVO, usuario);
            baixarContasReceber(obj, listaPendencias, configuracaoFinanceiroVO, usuario);
            removerContasReceber(listaContaReceberPersistirBanco,usuario);
            getFacadeFactory().getMatriculaFacade().alterarMatriculaCanceladoFinanceiro(obj.getMatriculaAluno().getMatricula(), true);
            obj.setNovoObj(Boolean.FALSE);
        } catch (Exception e) {
            obj.setNovoObj(Boolean.TRUE);
            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>LiberacaoFinanceiroCancelamentoTrancamentoVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>LiberacaoFinanceiroCancelamentoTrancamentoVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final LiberacaoFinanceiroCancelamentoTrancamentoVO obj,UsuarioVO usuario) throws Exception {
        try {
            LiberacaoFinanceiroCancelamentoTrancamentoVO.validarDados(obj);
            LiberacaoFinanceiroCancelamentoTrancamento.alterar(getIdEntidade(), true, usuario);
            obj.realizarUpperCaseDados();
            final String sql = "UPDATE LiberacaoFinanceiroCancelamentoTrancamento set matriculaAluno=?, unidadeEnsino=?, dataCadastro=?, responsavel=?, motivoLiberacaoFinanceiro=? WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
                    PreparedStatement sqlAlterar = cnctn.prepareStatement(sql);
            if (!obj.getMatriculaAluno().getMatricula().equals("")) {
                sqlAlterar.setString(1, obj.getMatriculaAluno().getMatricula());
            } else {
                sqlAlterar.setNull(1, 0);
            }
            if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
                sqlAlterar.setInt(2, obj.getUnidadeEnsino().getCodigo().intValue());
            } else {
                sqlAlterar.setNull(2, 0);
            }
            sqlAlterar.setDate(3, Uteis.getDataJDBC(obj.getDataCadastro()));
            if (obj.getResponsavel().getCodigo().intValue() != 0) {
                sqlAlterar.setInt(4, obj.getResponsavel().getCodigo().intValue());
            } else {
                sqlAlterar.setNull(4, 0);
            }
            sqlAlterar.setString(5, obj.getMotivoLiberacaoFinanceiro());
            sqlAlterar.setInt(6, obj.getCodigo().intValue());
            return sqlAlterar;
                }
            });
            
            getFacadeFactory().getHistoricoLiberacaoFinanceiroCancelamentoTrancamentoFacade().alterarHistoricoLiberacaoFinanceiroCancelamentoTrancamentos(obj.getCodigo(), obj.getHistoricoLiberacaoFinanceiroCancelamentoTrancamentoVOs(), usuario);
        } catch (Exception e) {
            throw e;
        }
    }
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarSituacao(final LiberacaoFinanceiroCancelamentoTrancamentoVO obj, UsuarioVO usuario) throws Exception {
    	try {
    		LiberacaoFinanceiroCancelamentoTrancamentoVO.validarDados(obj);
            LiberacaoFinanceiroCancelamentoTrancamento.alterar(getIdEntidade());
            obj.realizarUpperCaseDados();
            
            final String sql = "UPDATE LiberacaoFinanceiroCancelamentoTrancamento set situacao=?, dataalteracao=?, usuario=?, motivoestorno=? WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
                    PreparedStatement sqlAlterar = cnctn.prepareStatement(sql);
            if (!obj.getSituacao().equals("")) {
                sqlAlterar.setString(1, obj.getSituacao());
            } else {
                sqlAlterar.setNull(1, 0);
            }
            sqlAlterar.setDate(2, Uteis.getDataJDBC(obj.getDataAlteracao()));
            if (obj.getResponsavelEstorno().getCodigo().intValue() != 0) {
                sqlAlterar.setInt(3, obj.getResponsavelEstorno().getCodigo().intValue());
            } else {
                sqlAlterar.setNull(3, 0);
            }
            if (!obj.getMotivoEstorno().equals("")) {
                sqlAlterar.setString(4, obj.getMotivoEstorno());
            } else {
                sqlAlterar.setNull(4, 0);
            }
            sqlAlterar.setInt(5, obj.getCodigo().intValue());
            return sqlAlterar;
                }
            });
            
		} catch (Exception e) {
			throw e;
		}
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>LiberacaoFinanceiroCancelamentoTrancamentoVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>LiberacaoFinanceiroCancelamentoTrancamentoVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(LiberacaoFinanceiroCancelamentoTrancamentoVO obj,UsuarioVO usuario) throws Exception {
        try {
            LiberacaoFinanceiroCancelamentoTrancamento.excluir(getIdEntidade(), true, usuario);
            String sql = "DELETE FROM LiberacaoFinanceiroCancelamentoTrancamento WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
            getFacadeFactory().getHistoricoLiberacaoFinanceiroCancelamentoTrancamentoFacade().excluirHistoricoLiberacaoFinanceiroCancelamentoTrancamentos(obj.getCodigo(), usuario);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>LiberacaoFinanceiroCancelamentoTrancamento</code> através do valor do atributo 
     * <code>nome</code> da classe <code>Usuario</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>LiberacaoFinanceiroCancelamentoTrancamentoVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNomeUsuario(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT LiberacaoFinanceiroCancelamentoTrancamento.* FROM LiberacaoFinanceiroCancelamentoTrancamento, Usuario WHERE LiberacaoFinanceiroCancelamentoTrancamento.responsavel = Usuario.codigo and upper( Usuario.nome ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY Usuario.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>LiberacaoFinanceiroCancelamentoTrancamento</code> através do valor do atributo 
     * <code>Date dataCadastro</code>. Retorna os objetos com valores pertecentes ao período informado por parâmetro.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>LiberacaoFinanceiroCancelamentoTrancamentoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorDataCadastro(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados,ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM LiberacaoFinanceiroCancelamentoTrancamento WHERE ((dataCadastro >= '" + Uteis.getDataJDBC(prmIni) + "') and (dataCadastro <= '" + Uteis.getDataJDBC(prmFim) + "')) ORDER BY dataCadastro";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>LiberacaoFinanceiroCancelamentoTrancamento</code> através do valor do atributo 
     * <code>nome</code> da classe <code>UnidadeEnsino</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>LiberacaoFinanceiroCancelamentoTrancamentoVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNomeUnidadeEnsino(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT LiberacaoFinanceiroCancelamentoTrancamento.* FROM LiberacaoFinanceiroCancelamentoTrancamento, UnidadeEnsino WHERE LiberacaoFinanceiroCancelamentoTrancamento.unidadeEnsino = UnidadeEnsino.codigo and upper( UnidadeEnsino.nome ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY UnidadeEnsino.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>LiberacaoFinanceiroCancelamentoTrancamento</code> através do valor do atributo 
     * <code>matricula</code> da classe <code>Matricula</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>LiberacaoFinanceiroCancelamentoTrancamentoVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorMatriculaMatricula(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT LiberacaoFinanceiroCancelamentoTrancamento.* FROM LiberacaoFinanceiroCancelamentoTrancamento, Matricula WHERE LiberacaoFinanceiroCancelamentoTrancamento.matriculaAluno = Matricula.matricula and upper( Matricula.matricula ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY Matricula.matricula";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>LiberacaoFinanceiroCancelamentoTrancamento</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>LiberacaoFinanceiroCancelamentoTrancamentoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM LiberacaoFinanceiroCancelamentoTrancamento WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>LiberacaoFinanceiroCancelamentoTrancamentoVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados,ConfiguracaoFinanceiroVO configuracaoFinanceiro ,UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario));
        }
        tabelaResultado = null;
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>LiberacaoFinanceiroCancelamentoTrancamentoVO</code>.
     * @return  O objeto da classe <code>LiberacaoFinanceiroCancelamentoTrancamentoVO</code> com os dados devidamente montados.
     */
    public static LiberacaoFinanceiroCancelamentoTrancamentoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados,ConfiguracaoFinanceiroVO configuracaoFinanceiro,UsuarioVO usuario) throws Exception {
        LiberacaoFinanceiroCancelamentoTrancamentoVO obj = new LiberacaoFinanceiroCancelamentoTrancamentoVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.getMatriculaAluno().setMatricula(dadosSQL.getString("matriculaAluno"));
        obj.getUnidadeEnsino().setCodigo(new Integer(dadosSQL.getInt("unidadeEnsino")));
        obj.setDataCadastro(dadosSQL.getDate("dataCadastro"));
        obj.getResponsavel().setCodigo(new Integer(dadosSQL.getInt("responsavel")));
        obj.setMotivoLiberacaoFinanceiro(dadosSQL.getString("motivoLiberacaoFinanceiro"));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        obj.setHistoricoLiberacaoFinanceiroCancelamentoTrancamentoVOs(HistoricoLiberacaoFinanceiroCancelamentoTrancamento.consultarHistoricoLiberacaoFinanceiroCancelamentoTrancamentos(obj.getCodigo(), nivelMontarDados, usuario));
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS) {
            return obj;
        }

        montarDadosMatriculaAluno(obj, nivelMontarDados, configuracaoFinanceiro, usuario);
        montarDadosUnidadeEnsino(obj, nivelMontarDados, usuario);
        montarDadosResponsavel(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,usuario);
        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>UsuarioVO</code> relacionado ao objeto <code>LiberacaoFinanceiroCancelamentoTrancamentoVO</code>.
     * Faz uso da chave primária da classe <code>UsuarioVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosResponsavel(LiberacaoFinanceiroCancelamentoTrancamentoVO obj, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if (obj.getResponsavel().getCodigo().intValue() == 0) {
            return;
        }
        obj.setResponsavel(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavel().getCodigo(), nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>UnidadeEnsinoVO</code> relacionado ao objeto <code>LiberacaoFinanceiroCancelamentoTrancamentoVO</code>.
     * Faz uso da chave primária da classe <code>UnidadeEnsinoVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosUnidadeEnsino(LiberacaoFinanceiroCancelamentoTrancamentoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getUnidadeEnsino().getCodigo().intValue() == 0) {
            return;
        }
        obj.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsino().getCodigo(), false, nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>MatriculaVO</code> relacionado ao objeto <code>LiberacaoFinanceiroCancelamentoTrancamentoVO</code>.
     * Faz uso da chave primária da classe <code>MatriculaVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosMatriculaAluno(LiberacaoFinanceiroCancelamentoTrancamentoVO obj, int nivelMontarDados,ConfiguracaoFinanceiroVO configuracaoFinanceiro,UsuarioVO usuario) throws Exception {
        if ((obj.getMatriculaAluno().getMatricula() == null) || (obj.getMatriculaAluno().getMatricula().equals(""))) {
            return;
        }
        obj.setMatriculaAluno(getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatriculaAluno().getMatricula(), obj.getUnidadeEnsino().getCodigo(), NivelMontarDados.TODOS, usuario));
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>LiberacaoFinanceiroCancelamentoTrancamentoVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public LiberacaoFinanceiroCancelamentoTrancamentoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados,ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM LiberacaoFinanceiroCancelamentoTrancamento WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( LiberacaoFinanceiroCancelamentoTrancamento ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return LiberacaoFinanceiroCancelamentoTrancamento.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        LiberacaoFinanceiroCancelamentoTrancamento.idEntidade = idEntidade;
    }
    
    
    public void excluirObjListaContaReceberVOs(Integer codigo, List listaContaReceber,UsuarioVO usuario)throws Exception {
		int index = 0;
		Iterator i = listaContaReceber.iterator();
		while (i.hasNext()) {
			ContaReceberVO objExistente = (ContaReceberVO) i.next();
			if (objExistente.getCodigo().equals(codigo)) {
				listaContaReceber.remove(index);
				return;
			}
			index++;
		}
	}

    private StringBuffer getSQLPadraoConsultaBasica() {
        StringBuffer sql = new StringBuffer();
        sql.append("select liberacao.codigo AS \"liberacao.codigo\", liberacao.motivoliberacaofinanceiro AS \"liberacao.motivoliberacaofinanceiro\", liberacao.dataCadastro, liberacao.motivoEstorno, liberacao.dataAlteracao, liberacao.situacao, matricula.matricula, unidadeensino.codigo AS \"unidade.codigo\", ");
        sql.append(" unidadeensino.nome AS \"unidade.nome\", usuario.codigo AS \"usuario.codigo\", usuario.nome AS \"usuario.nome\", usuarioEstorno.codigo as \"usuarioEstorno.codigo\", usuarioEstorno.nome as \"usuarioEstorno.nome\", ");
        sql.append(" pessoa.nome AS \"pessoa.nome\", pessoa.codigo AS \"pessoa.codigo\" ");
        sql.append(" from liberacaofinanceirocancelamentotrancamento as liberacao ");
        sql.append(" inner join matricula on matricula.matricula = liberacao.matriculaaluno ");
        sql.append(" inner join unidadeensino on unidadeensino.codigo = liberacao.unidadeensino ");
        sql.append(" inner join usuario on usuario.codigo = liberacao.responsavel ");
        sql.append(" left join usuario usuarioEstorno on usuarioEstorno.codigo = liberacao.usuario ");
        sql.append(" inner join pessoa on pessoa.codigo = matricula.aluno ");
        return sql;
    }


    public List<LiberacaoFinanceiroCancelamentoTrancamentoVO> consultaRapidaPorMatricula(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuffer sql = getSQLPadraoConsultaBasica();
        sql.append(" WHERE lower(matricula.matricula) like ('");
        sql.append(valorConsulta.toLowerCase());
        sql.append("%')");
        if (unidadeEnsino.intValue() != 0) {
            sql.append(" and unidadeensino.codigo = ");
            sql.append(unidadeEnsino.intValue());
        }
        sql.append(" ORDER BY matricula.matricula ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        return montarDadosConsultaBasica(tabelaResultado);

    }

    public List<LiberacaoFinanceiroCancelamentoTrancamentoVO> consultaRapidaPorNomeAluno(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuffer sql = getSQLPadraoConsultaBasica();
        sql.append(" WHERE lower(pessoa.nome) like ('");
        sql.append(valorConsulta.toLowerCase());
        sql.append("%')");
        if (unidadeEnsino.intValue() != 0) {
            sql.append(" and unidadeensino.codigo = ");
            sql.append(unidadeEnsino.intValue());
        }
        sql.append(" ORDER BY pessoa.nome ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        return montarDadosConsultaBasica(tabelaResultado);

    }

    public List<LiberacaoFinanceiroCancelamentoTrancamentoVO> consultaRapidaPorNomeUnidadeEnsino(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuffer sql = getSQLPadraoConsultaBasica();
        sql.append(" WHERE lower(unidadeensino.nome) like ('");
        sql.append(valorConsulta.toLowerCase());
        sql.append("%')");
        if (unidadeEnsino.intValue() != 0) {
            sql.append(" and unidadeensino.codigo = ");
            sql.append(unidadeEnsino.intValue());
        }
        sql.append(" ORDER BY unidadeensino.nome ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        return montarDadosConsultaBasica(tabelaResultado);

    }

    public List<LiberacaoFinanceiroCancelamentoTrancamentoVO> consultaRapidaPorDataCadastro(Date prmIni, Date prmFim, Integer unidadeEnsino, boolean controlarAcesso, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuffer sql = getSQLPadraoConsultaBasica();
        sql.append(" WHERE (liberacao.dataCadastro >= '");
        sql.append(Uteis.getDataJDBC(prmIni));
        sql.append("') and liberacao.dataCadastro <= '");
        sql.append(Uteis.getDataJDBC(prmFim));
        sql.append("'");
        if (unidadeEnsino.intValue() != 0) {
            sql.append(" and unidadeensino.codigo = ");
            sql.append(unidadeEnsino.intValue());
        }
        sql.append(" ORDER BY liberacao.dataCadastro ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        return montarDadosConsultaBasica(tabelaResultado);

    }

    public List<LiberacaoFinanceiroCancelamentoTrancamentoVO> consultaRapidaPorResponsavel(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuffer sql = getSQLPadraoConsultaBasica();
        sql.append(" WHERE lower(usuario.nome) like ('");
        sql.append(valorConsulta.toLowerCase());
        sql.append("%')");
        if (unidadeEnsino.intValue() != 0) {
            sql.append(" and unidadeensino.codigo = ");
            sql.append(unidadeEnsino.intValue());
        }
        sql.append(" ORDER BY usuario.nome ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        return montarDadosConsultaBasica(tabelaResultado);

    }

    public List<LiberacaoFinanceiroCancelamentoTrancamentoVO> montarDadosConsultaBasica(SqlRowSet tabelaResultado) throws Exception {
        List<LiberacaoFinanceiroCancelamentoTrancamentoVO> vetResultado = new ArrayList<LiberacaoFinanceiroCancelamentoTrancamentoVO>(0);
        while (tabelaResultado.next()) {
            LiberacaoFinanceiroCancelamentoTrancamentoVO obj = new LiberacaoFinanceiroCancelamentoTrancamentoVO();
            montarDadosBasico(obj, tabelaResultado);
            vetResultado.add(obj);
        }
        return vetResultado;
    }

    private void montarDadosBasico(LiberacaoFinanceiroCancelamentoTrancamentoVO obj, SqlRowSet dadosSQL) throws Exception {
        obj.setNivelMontarDados(NivelMontarDados.BASICO);
        //Dados LiberacaoFinanceiro
        obj.setCodigo(new Integer(dadosSQL.getInt("liberacao.codigo")));
        obj.setDataCadastro((dadosSQL.getDate("datacadastro")));
        obj.setMotivoLiberacaoFinanceiro((dadosSQL.getString("liberacao.motivoLiberacaoFinanceiro")));
        //Dados matricula
        obj.getMatriculaAluno().setMatricula(dadosSQL.getString("matricula"));
        //Dados unidade ensino
        obj.getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidade.codigo"));
        obj.getUnidadeEnsino().setNome(dadosSQL.getString("unidade.nome"));
        //Dados usuario
        obj.getResponsavel().setCodigo(dadosSQL.getInt("usuario.codigo"));
        obj.getResponsavel().setNome(dadosSQL.getString("usuario.nome"));
        //Dados Aluno
        obj.getMatriculaAluno().getAluno().setNome(dadosSQL.getString("pessoa.nome"));
        obj.getMatriculaAluno().getAluno().setCodigo(dadosSQL.getInt("pessoa.codigo"));
        
        //Dados Estorno
        obj.setMotivoEstorno(dadosSQL.getString("motivoEstorno"));
        obj.getResponsavelEstorno().setCodigo(dadosSQL.getInt("usuarioEstorno.codigo"));
        obj.getResponsavelEstorno().setNome(dadosSQL.getString("usuarioEstorno.nome"));
        obj.setDataAlteracao(dadosSQL.getDate("dataAlteracao"));
        obj.setSituacao(dadosSQL.getString("situacao"));
    }


    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirComBaseNaMatricula(String matricula, ConfiguracaoFinanceiroVO confFinanVO, UsuarioVO usuarioLogado) throws Exception {
    	StringBuilder sqlStr = new StringBuilder("DELETE FROM liberacaoFinanceiroCancelamentoTrancamento WHERE matriculaaluno = '").append(matricula).append("' ");
    	try {
    		getConexao().getJdbcTemplate().update(sqlStr.toString());
    	} finally {
    		sqlStr = null;
    	}
    }
    
	/**
	 * Método responsável por executar a validação de pendências na biblioteca
	 * pelas origens Trancamento, Abandono, Cancelamento, Transfência de Saída,
	 * Tranferência Interna e Renovação levando em consideração os parâmetros
	 * passados na Configuração Geral do Sistema.
	 * 
	 * @param pessoa
	 * @param realizandoTrancamento
	 * @param realizandoAbandono
	 * @param realizandoCancelamento
	 * @param realizandoTransferenciaSaida
	 * @param realizandoTransferenciaInterna
	 * @param realizandoRenovacao
	 * @param configuracaoGeralSistemaVO
	 * @param usuarioVO
	 * @throws Exception
	 */
    @Override
    public void validarDadosPendenciaEmprestimoBiblioteca(MatriculaVO matriculaVO, boolean realizandoTrancamento, boolean realizandoAbandono, boolean realizandoCancelamento, 
    		boolean realizandoTransferenciaSaida, boolean realizandoTransferenciaInterna, boolean realizandoRenovacao, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception {
    	String matricula = getFacadeFactory().getItemEmprestimoFacade().consultarPorSituacaoExecucaoAtrasadoRenovadoEPorPessoa(matriculaVO.getAluno().getCodigo(), usuarioVO);
    	Boolean possuiPendenciaEmprestimo = Uteis.isAtributoPreenchido(matricula);
		if (possuiPendenciaEmprestimo) {
			if (realizandoTrancamento && configuracaoGeralSistemaVO.getBloquearRealizarTrancamentoComEmprestimoBiblioteca()) {
				throw new Exception(UteisJSF.internacionalizar("msg_MatriculaPeriodo_AlunoPossuiEmprestimos").replace("{0}", matricula));
			}
			if (realizandoAbandono && configuracaoGeralSistemaVO.getBloquearRealizarAbandonoCursoComEmprestimoBiblioteca()) {
				throw new Exception(UteisJSF.internacionalizar("msg_MatriculaPeriodo_AlunoPossuiEmprestimos").replace("{0}", matricula));
			}
			if (realizandoCancelamento && configuracaoGeralSistemaVO.getBloquearRealizarCancelamentoComEmprestimoBiblioteca()) {
				throw new Exception(UteisJSF.internacionalizar("msg_MatriculaPeriodo_AlunoPossuiEmprestimos").replace("{0}", matricula));
			}
			if (realizandoTransferenciaSaida && configuracaoGeralSistemaVO.getBloquearRealizarTransferenciaSaidaComEmprestimoBiblioteca()) {
				throw new Exception(UteisJSF.internacionalizar("msg_MatriculaPeriodo_AlunoPossuiEmprestimos").replace("{0}", matricula));
			}
			if (realizandoTransferenciaInterna && configuracaoGeralSistemaVO.getBloquearRealizarTransferenciaInternaComEmprestimoBiblioteca()) {
				throw new Exception(UteisJSF.internacionalizar("msg_MatriculaPeriodo_AlunoPossuiEmprestimos").replace("{0}", matricula));
			}
			if (realizandoRenovacao && configuracaoGeralSistemaVO.getBloquearRealizarRenovacaoComEmprestimoBiblioteca()) {
				throw new Exception(UteisJSF.internacionalizar("msg_MatriculaPeriodo_AlunoPossuiEmprestimos").replace("{0}", matricula));
			}
		}
    }
    
    public void realizarEstorno(LiberacaoFinanceiroCancelamentoTrancamentoVO obj, List<ContaReceberVO> contaReceberCancela, ConfiguracaoFinanceiroVO confFinanVO, UsuarioVO usuarioLogado) throws Exception {
		try {
			obj.setSituacao("EST");
			obj.setResponsavelEstorno(usuarioLogado);
			obj.setDataAlteracao(new Date());
			
			if (obj.getMotivoEstorno().equals("")) {
				throw new Exception("É obrigatório informar o MOTIVO do estorno do cancelamento financeiro.");
			}
	    	MatriculaPeriodoVO matriculaPeriodo = getFacadeFactory().getMatriculaPeriodoFacade().consultaUltimaMatriculaPeriodoPorMatricula(obj.getMatriculaAluno().getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, confFinanVO, usuarioLogado);
			if (matriculaPeriodo.getSituacaoMatriculaPeriodo().equals("AT") || matriculaPeriodo.getSituacaoMatriculaPeriodo().equals("PR") || matriculaPeriodo.getSituacaoMatriculaPeriodo().equals("FI")) {
				getFacadeFactory().getMatriculaFacade().alterarMatriculaCanceladoFinanceiro(obj.getMatriculaAluno().getMatricula(), false);
				getFacadeFactory().getLiberacaoFinanceiroCancelamentoTrancamentoFacade().alterarSituacao(obj, usuarioLogado);
				Iterator i = contaReceberCancela.iterator();
				while (i.hasNext()) {
					ContaReceberVO conta = (ContaReceberVO)i.next();
					getFacadeFactory().getContaReceberFacade().reativarContaReceberCancelada(conta, usuarioLogado);
				}
			} else {
				throw new Exception("Só é possível realizar o estorno do cancelamento financeiro caso a situação acadêmica da matrícula esteja como \"Pré-Matricula\" ou \"Ativa\".");
			}
		} catch (Exception e) {
			throw e;
		}
    }

}
