package negocio.facade.jdbc.planoorcamentario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

import controle.arquitetura.DataModelo;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.RequisicaoItemVO;
import negocio.comuns.compras.RequisicaoVO;
import negocio.comuns.planoorcamentario.DetalhamentoPeriodoOrcamentoVO;
import negocio.comuns.planoorcamentario.ItemSolicitacaoOrcamentoPlanoOrcamentarioVO;
import negocio.comuns.planoorcamentario.PlanoOrcamentarioVO;
import negocio.comuns.planoorcamentario.SolicitacaoOrcamentoPlanoOrcamentarioVO;
import negocio.comuns.planoorcamentario.UnidadesPlanoOrcamentarioVO;
import negocio.comuns.planoorcamentario.enumeradores.SituacaoPlanoOrcamentarioEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.planoorcamentario.PlanoOrcamentarioInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>PlanoOrcamentarioVO</code>. Responsável por implementar operações
 * como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>PlanoOrcamentarioVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see PlanoOrcamentarioVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class PlanoOrcamentario extends ControleAcesso implements PlanoOrcamentarioInterfaceFacade {

	private static final long serialVersionUID = -6801048562721621770L;

	protected static String idEntidade;

    public PlanoOrcamentario() throws Exception {
        super();
        setIdEntidade("PlanoOrcamentario");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe
     * <code>PlanoOrcamentarioVO</code>.
     */
    public PlanoOrcamentarioVO novo() throws Exception {
        PlanoOrcamentario.incluir(getIdEntidade());
        PlanoOrcamentarioVO obj = new PlanoOrcamentarioVO();
        return obj;
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void incluir(final PlanoOrcamentarioVO obj, UsuarioVO usuario) throws Exception {
    	incluir(obj, usuario, false);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void alterar(final PlanoOrcamentarioVO obj, UsuarioVO usuario) throws Exception {
    	alterar(obj, usuario, false);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void finalizar(final PlanoOrcamentarioVO obj, UsuarioVO usuario) throws Exception {
    	finalizar(obj, usuario, false);
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe
     * <code>PlanoOrcamentarioVO</code>. Primeiramente valida os dados (
     * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de
     * dados e a permissão do usuário para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * 
     * @param obj
     *            Objeto da classe <code>PlanoOrcamentarioVO</code> que será gravado no
     *            banco de dados.
     * @exception Exception
     *                Caso haja problemas de conexão, restrição de acesso ou
     *                validação de dados.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void incluir(final PlanoOrcamentarioVO obj, UsuarioVO usuario, boolean permitirRealizarManejamentoSaldoAprovado) throws Exception {
        try {
            PlanoOrcamentarioVO.validarDados(obj, permitirRealizarManejamentoSaldoAprovado);
            validarDadosUnicidadeNome(obj.getCodigo(), obj.getNome());

            StringBuilder sql = new StringBuilder();
            sql.append(" INSERT INTO PlanoOrcamentario( nome,dataInicio,dataFinal,situacao,objetivo,receitaPeriodoAnterior,");
            sql.append(" receitaProvisionadaPeriodo,receitaPrevistaPeriodo,receitaTotalPrevista,despesaPeriodoAnterior,");
            sql.append(" despesaProvisionadaPeriodo,despesaPrevistaPeriodo,despesaTotalPrevista,crescimentoTurmasAtivas,");
            sql.append(" percentualCrescimentoGastos,turmasEncerrandoPeriodo,turmasNaoEncerramPeriodo,turmaPrevistasPeriodo,");
            sql.append(" orcamentoTotalPrevisto,valorAlteracaoPrevistaPeriodo,saldoOrcamentarioPlanejar,orcamentoTotalRealizado,");
            sql.append(" lucro,lucroPrevisto,percentualCrescimentoReceita,percentualCrescimentoDespesa, responsavel) VALUES ");
            sql.append(" (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ");
            sql.append("  ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo");
            sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));

            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());

                    sqlInserir.setString(1, obj.getNome());
                    sqlInserir.setDate(2, Uteis.getDataJDBC(obj.getDataInicio()));
                    sqlInserir.setDate(3, Uteis.getDataJDBC(obj.getDataFinal()));
                    sqlInserir.setString(4, obj.getSituacao());
                    sqlInserir.setString(5, obj.getObjetivo());
                    sqlInserir.setDouble(6, obj.getReceitaPeriodoAnterior().doubleValue());
                    sqlInserir.setDouble(7, obj.getReceitaProvisionadaPeriodo().doubleValue());
                    sqlInserir.setDouble(8, obj.getReceitaPrevistaPeriodo().doubleValue());
                    sqlInserir.setDouble(9, obj.getReceitaTotalPrevista().doubleValue());
                    sqlInserir.setDouble(10, obj.getDespesaPeriodoAnterior().intValue());
                    sqlInserir.setDouble(11, obj.getDespesaProvisionadaPeriodo().doubleValue());
                    sqlInserir.setDouble(12, obj.getDespesaPrevistaPeriodo().doubleValue());
                    sqlInserir.setDouble(13, obj.getDespesaTotalPrevista().doubleValue());
                    sqlInserir.setDouble(14, obj.getCrescimentoTurmasAtivas().doubleValue());
                    sqlInserir.setDouble(15, obj.getPercentualCrescimentoGastos().doubleValue());
                    sqlInserir.setInt(16, obj.getTurmasEncerrandoPeriodo().intValue());
                    sqlInserir.setInt(17, obj.getTurmasNaoEncerramPeriodo().intValue());
                    sqlInserir.setInt(18, obj.getTurmaPrevistasPeriodo().intValue());
                    sqlInserir.setDouble(19, obj.getOrcamentoTotalPrevisto().doubleValue());
                    sqlInserir.setDouble(20, obj.getValorAlteracaoPrevistaPeriodo().doubleValue());
                    sqlInserir.setDouble(21, obj.getSaldoOrcamentarioPlanejar().doubleValue());
                    sqlInserir.setDouble(22, obj.getOrcamentoTotalRealizado().doubleValue());
                    sqlInserir.setDouble(23, obj.getLucro().intValue());
                    sqlInserir.setDouble(24, obj.getLucroPrevisto().intValue());
                    sqlInserir.setDouble(25, obj.getPercentualCrescimentoReceita().intValue());
                    sqlInserir.setDouble(26, obj.getPercentualCrescimentoDespesa().intValue());
                    int i = 26;
                    Uteis.setValuePreparedStatement(obj.getResponsavel(), ++i, sqlInserir);
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
            getFacadeFactory().getUnidadesPlanoOrcamentarioFacade().incluirUnidadesPlanoOrcamentarios(obj.getCodigo(), obj.getListaUnidades(), usuario);
//            getFacadeFactory().getDetalhamentoPlanoOrcamentarioFacade().incluirDetalhamentoPlanoOrcamentario(obj.getCodigo(), obj.getDetalhamentoPlanoOrcamentario(), usuario);
//            getFacadeFactory().getDetalhamentoPeriodoOrcamentoFacade().incluirDetalhamentoPeriodoOrcamentario(obj.getCodigo(), obj.getDetalhamentoPeriodoOrcamento(), usuario);
        } catch (Exception e) {
            obj.setNovoObj(Boolean.TRUE);
            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe
     * <code>PlanoOrcamentarioVO</code>. Sempre utiliza a chave primária da classe como
     * atributo para localização do registro a ser alterado. Primeiramente
     * valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão
     * com o banco de dados e a permissão do usuário para realizar esta operacão
     * na entidade. Isto, através da operação <code>alterar</code> da
     * superclasse.
     * 
     * @param obj
     *            Objeto da classe <code>PlanoOrcamentarioVO</code> que será alterada no
     *            banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão, restrição de acesso ou
     *                validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void alterar(final PlanoOrcamentarioVO obj, UsuarioVO usuario, boolean permitirRealizarManejamentoSaldoAprovado) throws Exception {
        try {
            PlanoOrcamentarioVO.validarDados(obj, permitirRealizarManejamentoSaldoAprovado);
            validarDadosUnicidadeNome(obj.getCodigo(), obj.getNome());
            final String sql = "UPDATE PlanoOrcamentario set nome=?, dataInicio=?, dataFinal=?, situacao=?, objetivo=?, receitaPeriodoAnterior=?, "
                    + "receitaProvisionadaPeriodo=?, receitaPrevistaPeriodo=?, receitaTotalPrevista=?, despesaPeriodoAnterior=?, "
                    + "despesaProvisionadaPeriodo=?, despesaPrevistaPeriodo=?, despesaTotalPrevista=?, crescimentoTurmasAtivas=?, "
                    + "percentualCrescimentoGastos=?, turmasEncerrandoPeriodo=?, turmasNaoEncerramPeriodo=?, turmaPrevistasPeriodo=?, "
                    + "orcamentoTotalPrevisto=?, valorAlteracaoPrevistaPeriodo=?, saldoOrcamentarioPlanejar=?, orcamentoTotalRealizado=?, "
                    + "lucro=?, lucroPrevisto=?,percentualCrescimentoReceita=?, percentualCrescimentoDespesa=? WHERE (codigo = ?)"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setString(1, obj.getNome());
                    sqlAlterar.setDate(2, Uteis.getDataJDBC(obj.getDataInicio()));
                    sqlAlterar.setDate(3, Uteis.getDataJDBC(obj.getDataFinal()));
                    sqlAlterar.setString(4, obj.getSituacao());
                    sqlAlterar.setString(5, obj.getObjetivo());
                    sqlAlterar.setDouble(6, obj.getReceitaPeriodoAnterior().doubleValue());
                    sqlAlterar.setDouble(7, obj.getReceitaProvisionadaPeriodo().doubleValue());
                    sqlAlterar.setDouble(8, obj.getReceitaPrevistaPeriodo().doubleValue());
                    sqlAlterar.setDouble(9, obj.getReceitaTotalPrevista().doubleValue());
                    sqlAlterar.setDouble(10, obj.getDespesaPeriodoAnterior());
                    sqlAlterar.setDouble(11, obj.getDespesaProvisionadaPeriodo().doubleValue());
                    sqlAlterar.setDouble(12, obj.getDespesaPrevistaPeriodo().doubleValue());
                    sqlAlterar.setDouble(13, obj.getDespesaTotalPrevista().doubleValue());
                    sqlAlterar.setDouble(14, obj.getCrescimentoTurmasAtivas().doubleValue());
                    sqlAlterar.setDouble(15, obj.getPercentualCrescimentoGastos().doubleValue());
                    sqlAlterar.setInt(16, obj.getTurmasEncerrandoPeriodo().intValue());
                    sqlAlterar.setInt(17, obj.getTurmasNaoEncerramPeriodo().intValue());
                    sqlAlterar.setInt(18, obj.getTurmaPrevistasPeriodo().intValue());
                    sqlAlterar.setDouble(19, obj.getOrcamentoTotalPrevisto().doubleValue());
                    sqlAlterar.setDouble(20, obj.getValorAlteracaoPrevistaPeriodo().doubleValue());
                    sqlAlterar.setDouble(21, obj.getSaldoOrcamentarioPlanejar().doubleValue());
                    sqlAlterar.setDouble(22, obj.getOrcamentoTotalRealizado().doubleValue());
                    sqlAlterar.setDouble(23, obj.getLucro().intValue());
                    sqlAlterar.setDouble(24, obj.getLucroPrevisto().intValue());
                    sqlAlterar.setDouble(25, obj.getPercentualCrescimentoReceita().intValue());
                    sqlAlterar.setDouble(26, obj.getPercentualCrescimentoDespesa().intValue());
                    sqlAlterar.setInt(27, obj.getCodigo());
                    return sqlAlterar;
                }
            });
            getFacadeFactory().getUnidadesPlanoOrcamentarioFacade().alterarUnidadesPlanoOrcamentarios(obj.getCodigo(), obj.getListaUnidades(), usuario);
            getFacadeFactory().getSolicitacaoOrcamentoPlanoOrcamentarioFacade().alterarValorAprovado(obj, usuario);
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void finalizar(final PlanoOrcamentarioVO obj, UsuarioVO usuario, boolean permitirRealizarManejamentoSaldoAprovado) throws Exception {
        try {
            PlanoOrcamentarioVO.validarDados(obj, permitirRealizarManejamentoSaldoAprovado);
            validarDadosUnicidadeNome(obj.getCodigo(), obj.getNome());
            final String sql = "UPDATE PlanoOrcamentario set situacao=?"
                    + " WHERE (codigo = ?)"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setString(1, obj.getSituacao());
                    sqlAlterar.setInt(2, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });
        } catch (Exception e) {
            throw e;
        }
    }

    public void validarDadosUnicidadeNome(Integer planoOrcamentario, String nome) throws Exception {
        String sql = "";
        if (planoOrcamentario == 0) {
            sql = "SELECT * FROM planoOrcamentario WHERE upper(nome) like('" + nome.toUpperCase() + "')";
        } else {
            sql = "SELECT * FROM planoOrcamentario WHERE codigo <> " + planoOrcamentario.intValue() + " AND upper(nome) like('" + nome.toUpperCase() + "')";
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
        if (tabelaResultado.next()) {
            throw new Exception("Nome já cadastrado para Plano Orçamentário.");
        }
    }

    public void validarDadosPeriodoCriacoPlanoOrcamentario(List<UnidadesPlanoOrcamentarioVO> listaUnidades, Date dataInicio, Date dataFim, Integer planoOrcamentario) throws Exception {
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append(" SELECT * FROM planoorcamentario ");
        sqlStr.append(" INNER JOIN unidadesplanoorcamentario ON unidadesplanoorcamentario.planoorcamentario = planoorcamentario.codigo ");
        sqlStr.append(" WHERE  planoorcamentario.codigo in (SELECT po.codigo FROM planoorcamentario po WHERE po.codigo <> planoorcamentario.codigo and (('");
        sqlStr.append(dataInicio);
        sqlStr.append("' between po.datainicio::date AND  po.datafinal::date ");
        sqlStr.append(")) or ('");
        sqlStr.append(dataFim);
        sqlStr.append("' between po.datainicio::date AND  po.datafinal::date ");
        sqlStr.append(")) and planoorcamentario.codigo <> ");
        sqlStr.append(planoOrcamentario);

        if (!listaUnidades.isEmpty()) {
            sqlStr.append(" and unidadesplanoorcamentario.unidadeensino in (");
            int x = 0;
            for (UnidadesPlanoOrcamentarioVO unidadePlanoOrcamentarioVO : listaUnidades) {
                if (x > 0) {
                    sqlStr.append(", ");
                }
                sqlStr.append(unidadePlanoOrcamentarioVO.getUnidadeEnsino().getCodigo());
                x++;
            }
            sqlStr.append(") ");
        }

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        if (tabelaResultado.next()) {
            throw new Exception("Já existe um PLANO ORÇAMENTARIO cadastrado nesse período.");
        }

    }

    /**
     * Operação responsável por excluir no BD um objeto da classe
     * <code>PlanoOrcamentarioVO</code>. Sempre localiza o registro a ser excluído através
     * da chave primária da entidade. Primeiramente verifica a conexão com o
     * banco de dados e a permissão do usuário para realizar esta operacão na
     * entidade. Isto, através da operação <code>excluir</code> da superclasse.
     * 
     * @param obj
     *            Objeto da classe <code>PlanoOrcamentarioVO</code> que será removido no
     *            banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(PlanoOrcamentarioVO obj) throws Exception {
        String sql = "DELETE FROM PlanoOrcamentario WHERE ((codigo = ?))";
        getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
    }

    /**
     * Responsável por realizar uma consulta de <code>Cheque</code> através do
     * valor do atributo <code>Integer codigo</code>. Retorna os objetos com
     * valores iguais ou superiores ao parâmetro fornecido. Faz uso da operação
     * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
     * List resultante.
     * 
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui
     *            permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>PlanoOrcamentarioVO</code>
     *         resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<PlanoOrcamentarioVO> consultarPorCodigo(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "select * from planoorcamentario ";
        if (unidadeEnsino.intValue() != 0) {
            sqlStr += " inner join unidadesplanoorcamentario on planoorcamentario.codigo = unidadesplanoorcamentario.planoorcamentario  ";
            sqlStr += " where 1=1 and unidadesplanoorcamentario.unidadeensino = " + unidadeEnsino.intValue();
        } else {
            sqlStr += " where 1=1 ";
        }
        sqlStr += " and planoorcamentario.codigo >= " + valorConsulta.intValue();
        sqlStr += " ORDER BY PlanoOrcamentario.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List<PlanoOrcamentarioVO> consultarPorNome(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "select * from planoorcamentario ";
        if (unidadeEnsino.intValue() != 0) {
            sqlStr += " inner join unidadesplanoorcamentario on planoorcamentario.codigo = unidadesplanoorcamentario.planoorcamentario  ";
            sqlStr += " where 1=1 and unidadesplanoorcamentario.unidadeensino = " + unidadeEnsino.intValue();
        } else {
            sqlStr += " where 1=1 ";
        }
        sqlStr += " and planoorcamentario.nome ilike '" + valorConsulta + "%'";
        sqlStr += " ORDER BY PlanoOrcamentario.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List<PlanoOrcamentarioVO> consultarPorSituacao(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sql = new StringBuilder();
        sql.append("select PlanoOrcamentario.* from planoorcamentario ");
        if (unidadeEnsino.intValue() != 0) {
            sql.append(" inner join unidadesplanoorcamentario on planoorcamentario.codigo = unidadesplanoorcamentario.planoorcamentario  ");
            sql.append(" where 1=1 and unidadesplanoorcamentario.unidadeensino = ").append(unidadeEnsino.intValue());
        } else {
            sql.append(" where 1=1 ");
        }
        sql.append(" and planoorcamentario.situacao ilike '").append(valorConsulta).append("%'");
        sql.append(" group by PlanoOrcamentario.codigo, planoorcamentario.nome"); 
        sql.append(" ORDER BY PlanoOrcamentario.nome");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List<PlanoOrcamentarioVO> consultarPorPeriodo(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "select * from planoorcamentario ";
        if (unidadeEnsino.intValue() != 0) {
            sqlStr += " inner join unidadesplanoorcamentario on planoorcamentario.codigo = unidadesplanoorcamentario.planoorcamentario  ";
            sqlStr += " where 1=1 and unidadesplanoorcamentario.unidadeensino = " + unidadeEnsino.intValue();
        } else {
            sqlStr += " where 1=1 ";
        }
        sqlStr += " and planoorcamentario.situacao ilike '" + valorConsulta + "%'";
        sqlStr += " ORDER BY PlanoOrcamentario.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List<PlanoOrcamentarioVO> consultarPorNomeUnidadeEnsino(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "select * from planoorcamentario ";
        if (unidadeEnsino.intValue() != 0) {
            sqlStr += " inner join unidadesplanoorcamentario on planoorcamentario.codigo = unidadesplanoorcamentario.planoorcamentario  ";
            sqlStr += " where 1=1 and unidadesplanoorcamentario.unidadeensino = " + unidadeEnsino.intValue();
        } else {
            sqlStr += " where 1=1 ";
        }
        sqlStr += " and planoorcamentario.situacao ilike '" + valorConsulta + "%'";
        sqlStr += " ORDER BY PlanoOrcamentario.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma
     * consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação
     * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * 
     * @return List Contendo vários objetos da classe <code>PlanoOrcamentarioVO</code>
     *         resultantes da consulta.
     */
    public List<PlanoOrcamentarioVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List<PlanoOrcamentarioVO> vetResultado = new ArrayList<>(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de
     * dados (<code>ResultSet</code>) em um objeto da classe
     * <code>PlanoOrcamentarioVO</code>.
     * 
     * @return O objeto da classe <code>PlanoOrcamentarioVO</code> com os dados devidamente
     *         montados.
     */
    @SuppressWarnings("unchecked")
	public PlanoOrcamentarioVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        PlanoOrcamentarioVO obj = new PlanoOrcamentarioVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setNome(dadosSQL.getString("nome"));
        obj.setDataInicio(dadosSQL.getDate("dataInicio"));
        obj.setDataFinal(dadosSQL.getDate("dataFinal"));
        obj.setSituacao(dadosSQL.getString("situacao"));
        obj.setObjetivo(dadosSQL.getString("objetivo"));
        obj.setReceitaPeriodoAnterior(new Double(dadosSQL.getDouble("receitaPeriodoAnterior")));
        obj.setReceitaProvisionadaPeriodo(new Double(dadosSQL.getDouble("receitaProvisionadaPeriodo")));
        obj.setReceitaPrevistaPeriodo(new Double(dadosSQL.getDouble("receitaPrevistaPeriodo")));
        obj.setReceitaTotalPrevista(new Double(dadosSQL.getDouble("receitaTotalPrevista")));
        obj.setDespesaPeriodoAnterior(new Double(dadosSQL.getDouble("despesaPeriodoAnterior")));
        obj.setDespesaProvisionadaPeriodo(new Double(dadosSQL.getDouble("despesaProvisionadaPeriodo")));
        obj.setDespesaPrevistaPeriodo(new Double(dadosSQL.getDouble("despesaPrevistaPeriodo")));
        obj.setDespesaTotalPrevista(new Double(dadosSQL.getDouble("despesaTotalPrevista")));
        obj.setCrescimentoTurmasAtivas(new Double(dadosSQL.getDouble("crescimentoTurmasAtivas")));
        obj.setPercentualCrescimentoGastos(new Double(dadosSQL.getDouble("percentualCrescimentoGastos")));
        obj.setTurmasEncerrandoPeriodo(new Integer(dadosSQL.getInt("turmasEncerrandoPeriodo")));
        obj.setTurmasNaoEncerramPeriodo(new Integer(dadosSQL.getInt("turmasNaoEncerramPeriodo")));
        obj.setTurmaPrevistasPeriodo(new Integer(dadosSQL.getInt("turmaPrevistasPeriodo")));
        obj.setOrcamentoTotalPrevisto(new Double(dadosSQL.getDouble("orcamentoTotalPrevisto")));
        obj.setValorAlteracaoPrevistaPeriodo(new Double(dadosSQL.getDouble("valorAlteracaoPrevistaPeriodo")));
        obj.setSaldoOrcamentarioPlanejar(new Double(dadosSQL.getDouble("saldoOrcamentarioPlanejar")));
        obj.setOrcamentoTotalRealizado(new Double(dadosSQL.getDouble("orcamentoTotalRealizado")));
        obj.setLucro(dadosSQL.getDouble("lucro"));
        obj.setLucroPrevisto(dadosSQL.getDouble("lucroPrevisto"));
        obj.setPercentualCrescimentoReceita(dadosSQL.getDouble("percentualCrescimentoReceita"));
        obj.setPercentualCrescimentoDespesa(dadosSQL.getDouble("percentualCrescimentoDespesa"));
        obj.setNovoObj(Boolean.FALSE);
        if (Uteis.isAtributoPreenchido(dadosSQL.getInt("responsavel"))) {
        	obj.setResponsavel(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(dadosSQL.getInt("responsavel"), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
        }
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        obj.setListaUnidades(UnidadesPlanoOrcamentario.consultarUnidadesPlanoOrcamentarios(obj.getCodigo(), false, usuario));
//        obj.setDetalhamentoPlanoOrcamentario(getFacadeFactory().getDetalhamentoPlanoOrcamentarioFacade().consultarDetalhamentoPorPlanoOrcamentario(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, false, usuario));
        obj.setDetalhamentoPeriodoOrcamento(getFacadeFactory().getDetalhamentoPeriodoOrcamentoFacade().consultarDetalhamentoPorPlanoOrcamentario(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, false, usuario));
        return obj;
    }

    /**
     * Operação responsável por localizar um objeto da classe
     * <code>PlanoOrcamentarioVO</code> através de sua chave primária.
     * 
     * @exception Exception
     *                Caso haja problemas de conexão ou localização do objeto
     *                procurado.
     */
    public PlanoOrcamentarioVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM PlanoOrcamentario WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( PlanoOrcamentario ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe. Este
     * identificar é utilizado para verificar as permissões de acesso as
     * operações desta classe.
     */
    public static String getIdEntidade() {
        return PlanoOrcamentario.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta
     * classe. Esta alteração deve ser possível, pois, uma mesma classe de
     * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
     * que Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        PlanoOrcamentario.idEntidade = idEntidade;
    }

    public Double consultarReceitaPeriodoAnterior(Date dataInicio, Date dataFim, List<UnidadesPlanoOrcamentarioVO> unidadesPlanoOrcamentarioVOs) {
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("SELECT TRUNC(SUM(contareceber.valor)::NUMERIC, 2) as valor FROM contareceber ");
        sqlStr.append("WHERE contareceber.data between '");
        sqlStr.append(dataInicio);
        sqlStr.append("' AND '");
        sqlStr.append(dataFim);
        sqlStr.append("'");
        if (!unidadesPlanoOrcamentarioVOs.isEmpty()) {
	        sqlStr.append(unidadesPlanoOrcamentarioVOs.stream()
	    			.map(u -> String.valueOf(u.getUnidadeEnsino().getCodigo()))
	    			.collect(Collectors.joining(", ", " and contareceber.unidadeensino in (", ")")));
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        if (tabelaResultado.next()) {
            return tabelaResultado.getDouble("valor");
        }
        return 0.0;
    }

    public Double consultarDespesaPeriodoAnterior(Date dataInicio, Date dataFim, List<UnidadesPlanoOrcamentarioVO> unidadesPlanoOrcamentarioVOs) {
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("SELECT TRUNC(SUM(contapagar.valor)::NUMERIC, 2) as valor FROM contapagar ");
        sqlStr.append("WHERE contapagar.data between '");
        sqlStr.append(dataInicio);
        sqlStr.append("' AND '");
        sqlStr.append(dataFim);
        sqlStr.append("'");

        if (!unidadesPlanoOrcamentarioVOs.isEmpty()) {
        	sqlStr.append(unidadesPlanoOrcamentarioVOs.stream()
        			.map(u -> String.valueOf(u.getUnidadeEnsino().getCodigo()))
        			.collect(Collectors.joining(", ", " and contapagar.unidadeensino in (", ")")));
        }

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        if (tabelaResultado.next()) {
            return tabelaResultado.getDouble("valor");
        }
        return 0.0;
    }

    public Double consultarReceitaProvisionadaPeriodo(Date dataInicio, Date dataFim, List<UnidadesPlanoOrcamentarioVO> unidadesPlanoOrcamentarioVOs) {
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("SELECT TRUNC(SUM(contareceber.valor)::NUMERIC, 2) as valor FROM contareceber ");
        sqlStr.append("WHERE contareceber.data between '");
        sqlStr.append(dataInicio);
        sqlStr.append("' AND '");
        sqlStr.append(dataFim);
        sqlStr.append("'");

        if (!unidadesPlanoOrcamentarioVOs.isEmpty()) {
        	sqlStr.append(unidadesPlanoOrcamentarioVOs.stream()
        			.map(u -> String.valueOf(u.getUnidadeEnsino().getCodigo()))
        			.collect(Collectors.joining(", ", " and contareceber.unidadeensino in (", ")")));
        }

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        if (tabelaResultado.next()) {
            return tabelaResultado.getDouble("valor");
        }
        return 0.0;
    }

    public Double consultarDespesaProvisionadaPeriodo(Date dataInicio, Date dataFim, List<UnidadesPlanoOrcamentarioVO> unidadesPlanoOrcamentarioVOs) {
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("SELECT TRUNC(SUM(contapagar.valor)::NUMERIC, 2) as valor FROM contapagar ");
        sqlStr.append("WHERE contapagar.data between '");
        sqlStr.append(dataInicio);
        sqlStr.append("' AND '");
        sqlStr.append(dataFim);
        sqlStr.append("'");

        if (!unidadesPlanoOrcamentarioVOs.isEmpty()) {
        	sqlStr.append(unidadesPlanoOrcamentarioVOs.stream()
        			.map(u -> String.valueOf(u.getUnidadeEnsino().getCodigo()))
        			.collect(Collectors.joining(", ", " and contapagar.unidadeensino in (", ")")));
        }

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        if (tabelaResultado.next()) {
            return tabelaResultado.getDouble("valor");
        }
        return 0.0;
    }

    public Double executarCalculoLucro(Double valorReceita, Double valorDespesa) {
        if (valorReceita.equals(0.0) && valorDespesa.equals(0.0)) {
            return 0.0;
        } else {
            Double resultado = 0.0;
            resultado = valorReceita / 100;
            Double lucro = valorReceita - valorDespesa;
            if (lucro < 0) {
            	return 0.0;
            }
            return lucro / resultado;
        }
    }

    public Double executarCalculoCrescimento(Double valorBase, Double valorCalcular) {
        if (valorBase.equals(0.0)) {
        	return 0.0;
        } else if (valorCalcular.equals(0.0)) {
            return 0.0;
        } else {
            Double resultado = 0.0;
            resultado = valorCalcular * 100;
            Double lucro = resultado / valorBase;
            return lucro - 100;
        }
    }

    public void consultarDadosOrcamentarios(PlanoOrcamentarioVO planoOrcamentarioVO, Date dataInicio, Date dataFim) throws Exception {
        //Dados Orçamentários do Período Anterior
        planoOrcamentarioVO.setReceitaPeriodoAnterior(consultarReceitaPeriodoAnterior(dataReceitaPeriodoAnterior(dataInicio), dataReceitaPeriodoAnterior(dataFim), planoOrcamentarioVO.getListaUnidades()));
        planoOrcamentarioVO.setDespesaPeriodoAnterior(consultarDespesaPeriodoAnterior(dataReceitaPeriodoAnterior(dataInicio), dataReceitaPeriodoAnterior(dataFim), planoOrcamentarioVO.getListaUnidades()));
        planoOrcamentarioVO.setLucro(Uteis.arrendondarForcando2CadasDecimais(executarCalculoLucro(planoOrcamentarioVO.getReceitaPeriodoAnterior(), planoOrcamentarioVO.getDespesaPeriodoAnterior())));
        
        //Dados Orçamentários Provisionada para o Período
        planoOrcamentarioVO.setReceitaProvisionadaPeriodo(consultarReceitaProvisionadaPeriodo(dataInicio, dataFim, planoOrcamentarioVO.getListaUnidades()));
        planoOrcamentarioVO.setDespesaProvisionadaPeriodo(consultarDespesaProvisionadaPeriodo(dataInicio, dataFim, planoOrcamentarioVO.getListaUnidades()));
        planoOrcamentarioVO.setLucroPrevisto(Uteis.arrendondarForcando2CadasDecimais(executarCalculoLucro(planoOrcamentarioVO.getReceitaProvisionadaPeriodo(), planoOrcamentarioVO.getDespesaProvisionadaPeriodo())));

        //Dados Orçamentários Prevista para o Período
        planoOrcamentarioVO.setReceitaPrevistaPeriodo(getFacadeFactory().getTurmaFacade().consultarReceitaPrevistaPeriodo(dataInicio, dataFim, planoOrcamentarioVO.getListaUnidades()));
        planoOrcamentarioVO.setDespesaPrevistaPeriodo(getFacadeFactory().getTurmaFacade().consultarDespesaPrevistaPeriodo(dataInicio, dataFim, planoOrcamentarioVO.getListaUnidades()));
        
        //Dados Orçamentários Receita Total Prevista
        planoOrcamentarioVO.setReceitaTotalPrevista(planoOrcamentarioVO.getReceitaProvisionadaPeriodo() + planoOrcamentarioVO.getReceitaPrevistaPeriodo());
        planoOrcamentarioVO.setDespesaTotalPrevista(planoOrcamentarioVO.getDespesaProvisionadaPeriodo() + planoOrcamentarioVO.getDespesaPrevistaPeriodo());
        planoOrcamentarioVO.setPercentualCrescimentoDespesa(Uteis.arrendondarForcando2CadasDecimais(executarCalculoCrescimento(planoOrcamentarioVO.getDespesaPeriodoAnterior(), planoOrcamentarioVO.getDespesaTotalPrevista())));
        planoOrcamentarioVO.setPercentualCrescimentoReceita(Uteis.arrendondarForcando2CadasDecimais(executarCalculoCrescimento(planoOrcamentarioVO.getReceitaPeriodoAnterior(), planoOrcamentarioVO.getReceitaTotalPrevista())));
        
        //Dados Orçamentários Turmas
        planoOrcamentarioVO.setTurmasEncerrandoPeriodo(getFacadeFactory().getTurmaFacade().consultarQuantidadeTurmasEncerrandoPeriodo(dataInicio, dataFim));
        planoOrcamentarioVO.setTurmasNaoEncerramPeriodo(getFacadeFactory().getTurmaFacade().consultarQuantidadeTurmasNaoEncerrandoPeriodo(dataInicio, dataFim));
        planoOrcamentarioVO.setTurmaPrevistasPeriodo(getFacadeFactory().getTurmaFacade().consultarQuantidadeNovasTurmasPrevistaPeriodo(dataInicio, dataFim, planoOrcamentarioVO.getListaUnidades()));

        //Oçamento Total Previsto
        planoOrcamentarioVO.setSaldoOrcamentarioPlanejar(executarCalculoOrcamentoAPlanejar(planoOrcamentarioVO.getOrcamentoTotalPrevisto(), planoOrcamentarioVO.getSolicitacaoOrcamentoPlanoOrcamentarioVOs()));
    }

    public void consultarPorEnumCampoConsulta(DataModelo dataModelo, UsuarioVO usuarioVO) throws Exception {
    	dataModelo.getListaFiltros().clear();

		dataModelo.setListaConsulta(consultarPorNome(dataModelo, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
		dataModelo.setTotalRegistrosEncontrados(consultarTotalPorNome(dataModelo));
    }

    public List<PlanoOrcamentarioVO> consultarPorNome(DataModelo dataModelo, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	StringBuilder sql = new StringBuilder();
        sql.append(" select * from planoorcamentario ");
        sql.append(" where planoorcamentario.nome ilike(?)");
        sql.append(" ORDER BY PlanoOrcamentario.nome");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), "%" + dataModelo.getValorConsulta() + "%");
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public int consultarTotalPorNome(DataModelo dataModelo) throws Exception {
    	StringBuilder sql = new StringBuilder();
    	sql.append(" select count(codigo) as qtde from planoorcamentario ");
    	sql.append(" where planoorcamentario.nome ilike(?)");
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(),"%" + dataModelo.getValorConsulta() + "%");

    	return (Integer) Uteis.getSqlRowSetTotalizador(tabelaResultado, "qtde", TipoCampoEnum.INTEIRO);
    }

    public Double executarCalculoOrcamentoAPlanejar(Double orcamentoTotalPrevisto, List<SolicitacaoOrcamentoPlanoOrcamentarioVO> solicitacaoOrcamentoPlanoOrcamentarioVOs) {
        Double valorDepartamento = 0.0;
        for (SolicitacaoOrcamentoPlanoOrcamentarioVO solicitacaoOrcamentoPlanoOrcamentarioVO : solicitacaoOrcamentoPlanoOrcamentarioVOs) {
            valorDepartamento = valorDepartamento + solicitacaoOrcamentoPlanoOrcamentarioVO.getValorTotalAprovado();
        }
        return orcamentoTotalPrevisto - valorDepartamento;
    }

    public Date dataReceitaPeriodoAnterior(Date data) throws Exception {
        String anoAnterior = "";
        String dia = "";
        String mes = "";

        Integer ano = Uteis.getAnoData(data);
        anoAnterior = String.valueOf(ano - 1);
        dia = String.valueOf(Uteis.getDiaMesData(data));
        mes = String.valueOf(Uteis.getMesData(data));
        return Uteis.getDate(dia + "/" + mes + "/" + anoAnterior);
    }

    public PlanoOrcamentarioVO clonar(PlanoOrcamentarioVO obj) throws CloneNotSupportedException {
        PlanoOrcamentarioVO objClonado = (PlanoOrcamentarioVO) obj.clone();
        objClonado.setNovoObj(Boolean.TRUE);
        objClonado.setCodigo(0);
        objClonado.setNome(objClonado.getNome() + " - Clonado");
        return objClonado;
    }

    public void validarDadosPreenchimentoListaOrcamentoTotalNoPeriodo(List<DetalhamentoPeriodoOrcamentoVO> listaDetalhamentoPeriodoOrcamentoVOs) throws Exception {
        if (listaDetalhamentoPeriodoOrcamentoVOs.isEmpty()) {
            throw new Exception("Ainda não foi feita a distribuição do Orçamento Total no Período.");
        }
    }

    public Double executarCalculoSaldoAtualMes(RequisicaoVO requisicaoVO, Date data, Integer departamento, Integer unidadeEnsino) {
        String mes = iniciliazarDadosMesItemMensalData(new Date());
        //Realiza consulta do valor no ItemMensalDetalhamentoPlanoOrcamento caso não encontre valor Busca no DetalhamentoPlanoOrcamentario
        Double valorItemMensal = getFacadeFactory().getItemMensalDetalhamentoPlanoOrcamentarioFacade().consultarTotalMesAutorizacaoRequisicao(new Date(), departamento, unidadeEnsino, mes);
        if (valorItemMensal == null || valorItemMensal == 0) {
            //Consulta o valor do OrcamentoTotalDepartamento 
            valorItemMensal = getFacadeFactory().getDetalhamentoPlanoOrcamentarioFacade().consultarValorOcamentoTotalDetapartamentoPorDataDepartamentoUnidadeEnsino(new Date(), departamento, unidadeEnsino);
        }
        if (valorItemMensal == 0.0) {
            return 0.0;
        }
        Double totalQuantidadeAutorizada = getFacadeFactory().getProdutoServicoFacade().consultarTotalQuantidadeAutorizadaPorPrecoUnitarioProduto();
        return valorItemMensal - totalQuantidadeAutorizada;

    }

    public String iniciliazarDadosMesItemMensalData(Date data) {
        String mes = Uteis.getMesReferenciaExtenso(String.valueOf(Uteis.getMesData(data)));
        int ano = Uteis.getAnoData(data);
        return mes.toUpperCase() + " - " + ano;
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public PlanoOrcamentarioVO consultarPlanoOrcamentarioPorDataRequisicao(Date data, Integer departamento, Integer unidadeEnsino, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" select * from planoOrcamentario ");
        sb.append(" inner join detalhamentoplanoorcamentario detalhamento on detalhamento.planoorcamentario = planoorcamentario.codigo ");
        sb.append(" where 1 = 1 ");
        
        if (Uteis.isAtributoPreenchido(data)) {
	        sb.append("and '").append(data);
	        sb.append("' >= datainicio and '");
	        sb.append(data);
	        sb.append("' <= datafinal ");
        }
        if (Uteis.isAtributoPreenchido(departamento)) {
        	sb.append(" and detalhamento.departamento = ");
        	sb.append(departamento);
        }
        sb.append(" and situacao = 'AT'");
        sb.append(" and detalhamento.unidadeEnsino = ");
        sb.append(unidadeEnsino);
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        if (!tabelaResultado.next()) {
            return null;
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void atualizarOrcamentoTotalRealizado(Double valorAtualizar, Date dataAutorizacao, Integer departamento, Integer unidadeEnsino, UsuarioVO usuarioVO) throws Exception {
        PlanoOrcamentarioVO planoOrcamentario = consultarPlanoOrcamentarioPorDataRequisicao(dataAutorizacao, departamento, unidadeEnsino, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
        if (Uteis.isAtributoPreenchido(planoOrcamentario)) {
        	Double valorAlteracaoPrevisaoPeriodo = executarCalculoValorAlteracaoPrevistaPeriodo(planoOrcamentario.getOrcamentoTotalRealizado(), planoOrcamentario.getOrcamentoTotalPrevisto(), valorAtualizar, true);
            Double valorTotal = valorAtualizar + planoOrcamentario.getOrcamentoTotalRealizado();
            alterarOrcamentoTotalRealizado(valorTotal, valorAlteracaoPrevisaoPeriodo, planoOrcamentario.getCodigo());
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void atualizarEstornoOrcamentoTotalRealizado(Double valorAtualizar, Date dataAutorizacao, Integer departamento, Integer unidadeEnsino, UsuarioVO usuarioVO) throws Exception {
    	PlanoOrcamentarioVO planoOrcamentario = consultarPlanoOrcamentarioPorDataRequisicao(dataAutorizacao, departamento, unidadeEnsino, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
    	if (Uteis.isAtributoPreenchido(planoOrcamentario)) {
    		Double valorAlteracaoPrevisaoPeriodo = planoOrcamentario.getOrcamentoTotalRealizado() - valorAtualizar;
    		alterarOrcamentoTotalRealizado(valorAlteracaoPrevisaoPeriodo, valorAlteracaoPrevisaoPeriodo, planoOrcamentario.getCodigo());
    	}
    }

    /**
     * Método responsável por calcular o valor autorizado pelo Usuário Acima do Previsto
     * @return
     */
    public Double executarCalculoValorAlteracaoPrevistaPeriodo(Double orcamentoTotalRealizado, Double orcamentoTotalPrevisto, Double valorAtualizar, boolean adicionar) {
        if ((orcamentoTotalRealizado + valorAtualizar) > orcamentoTotalPrevisto) {
        	
        	Double resultado = 0.0;
        	if (adicionar) {
        		 resultado = (orcamentoTotalRealizado + valorAtualizar) - orcamentoTotalPrevisto;
        	} else {
        		resultado = (orcamentoTotalRealizado - valorAtualizar) - orcamentoTotalPrevisto;        		
        	}
            return resultado;
        }
        return 0.0;
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarOrcamentoTotalRealizado(final Double orcamentoTotalRealizado, final Double valorAlteracaoPrevistaPeriodo, final Integer codigo) throws Exception {

        final String sql = "UPDATE PlanoOrcamentario set orcamentoTotalRealizado=?, valorAlteracaoPrevistaPeriodo=?  WHERE ((codigo = ?))";
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                sqlAlterar.setDouble(1, orcamentoTotalRealizado);
                sqlAlterar.setDouble(2, valorAlteracaoPrevistaPeriodo);
                sqlAlterar.setInt(3, codigo.intValue());
                return sqlAlterar;
            }
        });
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void validarDadosExistenciaPlanoOrcamentarioPorDepartamentoUnidadeEnsinoEPeriodo(DepartamentoVO departamentoVO, UnidadeEnsinoVO unidadeEnsinoVO, UsuarioVO usuario) throws Exception {
        Boolean existePlanoOrcamentarioParaData = consultarExistenciaPlanoOrcamentarioData(new Date());
        if (!existePlanoOrcamentarioParaData) {
            throw new Exception("Não foi encontrado nenhum PLANO ORÇAMENTÁRIO ATIVO com o período configurado para a data " + Uteis.getDataAtual() + ".");
        }
        PlanoOrcamentarioVO planoOrcamentarioVO = consultarPlanoOrcamentarioPorData(new Date(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
        if (Uteis.isAtributoPreenchido(departamentoVO) && !consultarExistenciaPlanoOrcamentarioPorDepartamentoUnidadeEnsino(new Date(), departamentoVO.getCodigo(), unidadeEnsinoVO.getCodigo())) {
        	throw new Exception("Não foi encontrado distribuição de Orçamento para a Unidade de Ensino: " + unidadeEnsinoVO.getNome().toUpperCase() + ", e para o Departamento: " + departamentoVO.getNome().toUpperCase() + ", no Plano Orçamentário: " + planoOrcamentarioVO.getNome().toUpperCase() + ".");
        } else if (!Uteis.isAtributoPreenchido(departamentoVO) && !consultarExistenciaPlanoOrcamentarioPorDepartamentoUnidadeEnsino(new Date(), departamentoVO.getCodigo(), unidadeEnsinoVO.getCodigo())) {
        	throw new Exception("Não foi encontrado distribuição de Orçamento para a Unidade de Ensino: " + unidadeEnsinoVO.getNome().toUpperCase() + ", no Plano Orçamentário: " + planoOrcamentarioVO.getNome().toUpperCase() + ".");
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public PlanoOrcamentarioVO consultarPlanoOrcamentarioPorData(Date data, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("select * from planoOrcamentario ");
        sb.append(" where '");
        sb.append(data);
        sb.append("' >= datainicio and '");
        sb.append(data);
        sb.append("' <= datafinal");
        sb.append(" and situacao = 'AT'");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        if (!tabelaResultado.next()) {
            return null;
        }
        return montarDados(tabelaResultado, nivelMontarDados, usuario);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Boolean consultarExistenciaPlanoOrcamentarioData(Date data) {
        StringBuilder sb = new StringBuilder();
        sb.append("select * from planoOrcamentario ");
        sb.append(" where '");
        sb.append(data);
        sb.append("' >= datainicio and '");
        sb.append(data);
        sb.append("' <= datafinal");
        sb.append(" and situacao = 'AT'");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        if (tabelaResultado.next()) {
            return true;
        }
        return false;
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Boolean consultarExistenciaPlanoOrcamentarioPorDepartamentoUnidadeEnsino(Date data, Integer departamento, Integer unidadeEnsino) {
        StringBuilder sb = new StringBuilder();
        sb.append("select * from planoOrcamentario ");
        sb.append(" inner join detalhamentoplanoorcamentario detalhamento on detalhamento.planoorcamentario = planoorcamentario.codigo ");
        sb.append(" where '");
        sb.append(data);
        sb.append("' >= datainicio and '");
        sb.append(data);
        sb.append("' <= datafinal");
        if (Uteis.isAtributoPreenchido(departamento)) {
        	sb.append(" and detalhamento.departamento = ");
        	sb.append(departamento);
        }
        sb.append(" and detalhamento.unidadeEnsino = ");
        sb.append(unidadeEnsino);
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        if (tabelaResultado.next()) {
            return true;
        }
        return false;
    }

    public void validarDadosSaldoPlanoOrcamentario(RequisicaoVO requisicaoVO, Double valorAtualizar, DepartamentoVO departamentoVO, UnidadeEnsinoVO unidadeEnsinoVO, UsuarioVO usuario) throws Exception {

        if (requisicaoVO.getValorAcimaPrevistoAutorizado()) {
            return;
        }
        ItemSolicitacaoOrcamentoPlanoOrcamentarioVO itemSolicitacaoOrcamentoPlanoOrcamentarioVO = getFacadeFactory().getItemSolicitacaoOrcamentoPlanoOrcamentarioFacade().consultarItemSolicitacaoOrcamentoPlanoOrcamentarioPorValorSolicitadoUnidadeEnsinoCategoriaDespesaDepartamento(valorAtualizar, requisicaoVO.getUnidadeEnsino(), departamentoVO, requisicaoVO.getCategoriaDespesa(), requisicaoVO.getDataRequisicao(), usuario);
        if (!Uteis.isAtributoPreenchido(itemSolicitacaoOrcamentoPlanoOrcamentarioVO)) {               	
          requisicaoVO.setSaldoPlanoOrcamentarioInsuficiente(Boolean.TRUE);
          throw new Exception("Não foi encontrado nenhuma SOLICITAÇÃO DO PLANO ORÇAMENTÁRIO com saldo suficiente para aprovar a requisição.");	
        }
        if(requisicaoVO.getSituacaoAutorizacao().equals("AU")) {
            for(RequisicaoItemVO requisicaoItemVO: requisicaoVO.getRequisicaoItemVOs()) {
            	if(requisicaoItemVO.getQuantidadeAutorizada() > 0) {
            		requisicaoItemVO.setItemSolicitacaoOrcamentoPlanoOrcamentarioVO(itemSolicitacaoOrcamentoPlanoOrcamentarioVO);
            	}
            }
        }
    }

    public void carregarDados(PlanoOrcamentarioVO obj, UsuarioVO usuario) throws Exception {
        carregarDados((PlanoOrcamentarioVO) obj, NivelMontarDados.TODOS, usuario);
    }

    /**
     * Método responsavel por validar se o Nivel de Montar Dados é Básico ou Completo e faz a consulta
     * de acordo com o nível especificado.
     * @param obj
     * @param nivelMontarDados
     * @throws Exception
     * @author Carlos
     */
    public void carregarDados(PlanoOrcamentarioVO obj, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception {
        SqlRowSet resultado = null;
        if ((nivelMontarDados.equals(NivelMontarDados.BASICO)) && (obj.getIsNivelMontarDadosNaoInicializado())) {
            resultado = consultaRapidaPorChavePrimariaDadosBasicos(obj.getCodigo(), usuario);
            montarDadosBasico((PlanoOrcamentarioVO) obj, resultado);
        }
        if ((nivelMontarDados.equals(NivelMontarDados.TODOS)) && (!obj.getIsNivelMontarDadosTodos())) {
            resultado = consultaRapidaPorChavePrimariaDadosCompletos(obj.getCodigo(), usuario);
            montarDadosCompleto((PlanoOrcamentarioVO) obj, resultado, usuario);
        }
    }

    private StringBuffer getSQLPadraoConsultaBasica() {
        StringBuffer str = new StringBuffer();
        str.append("SELECT codigo, nome, situacao, dataInicio, dataFinal FROM planoOrcamentario ");
        return str;
    }

    private StringBuffer getSQLPadraoConsultaCompleta() {
        StringBuffer str = new StringBuffer();
        //Dados Plano Orçamentário
        str.append("SELECT DISTINCT planoOrcamentario.*  ");
        str.append(" from planoorcamentario ");
        return str;
    }

    private SqlRowSet consultaRapidaPorChavePrimariaDadosBasicos(Integer codPlanoOrcamentario, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append(" WHERE (planoOrcamentario.codigo= " + codPlanoOrcamentario + ")");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return tabelaResultado;
    }

    private SqlRowSet consultaRapidaPorChavePrimariaDadosCompletos(Integer codPlanoOrcamentario, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaCompleta();
        sqlStr.append(" WHERE (planoOrcamentario.codigo= " + codPlanoOrcamentario + ")");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return tabelaResultado;
    }

    private void montarDadosBasico(PlanoOrcamentarioVO obj, SqlRowSet dadosSQL) throws Exception {
        // Dados do Plano Orçamentário
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setNome(dadosSQL.getString("nome"));
        obj.setSituacao(dadosSQL.getString("situacao"));
        obj.setDataInicio(dadosSQL.getDate("dataInicio"));
        obj.setDataFinal(dadosSQL.getDate("dataFinal"));
    }

	private void montarDadosCompleto(PlanoOrcamentarioVO obj, SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
        //Dados Plano Orçamentário
        obj.setCodigo(dadosSQL.getInt("codigo"));
        obj.setNome(dadosSQL.getString("nome"));
        obj.setDataInicio(dadosSQL.getDate("dataInicio"));
        obj.setDataFinal(dadosSQL.getDate("dataFinal"));
        obj.setSituacao(dadosSQL.getString("situacao"));
        obj.setObjetivo(dadosSQL.getString("objetivo"));
        obj.setReceitaPeriodoAnterior(dadosSQL.getDouble("receitaPeriodoAnterior"));
        obj.setReceitaProvisionadaPeriodo(dadosSQL.getDouble("receitaProvisionadaPeriodo"));
        obj.setReceitaPrevistaPeriodo(dadosSQL.getDouble("receitaPrevistaPeriodo"));
        obj.setReceitaTotalPrevista(dadosSQL.getDouble("receitaTotalPrevista"));
        obj.setDespesaPeriodoAnterior(dadosSQL.getDouble("despesaPeriodoAnterior"));
        obj.setDespesaProvisionadaPeriodo(dadosSQL.getDouble("despesaProvisionadaPeriodo"));
        obj.setDespesaPrevistaPeriodo(dadosSQL.getDouble("despesaPrevistaPeriodo"));
        obj.setDespesaTotalPrevista(dadosSQL.getDouble("despesaTotalPrevista"));
        obj.setCrescimentoTurmasAtivas(dadosSQL.getDouble("crescimentoTurmasAtivas"));
        obj.setPercentualCrescimentoGastos(dadosSQL.getDouble("percentualCrescimentoGastos"));
        obj.setTurmasEncerrandoPeriodo(dadosSQL.getInt("turmasEncerrandoPeriodo"));
        obj.setTurmasNaoEncerramPeriodo(dadosSQL.getInt("turmasNaoEncerramPeriodo"));
        obj.setTurmaPrevistasPeriodo(dadosSQL.getInt("TurmaPrevistasPeriodo"));
        obj.setOrcamentoTotalPrevisto(dadosSQL.getDouble("orcamentoTotalPrevisto"));
        obj.setValorAlteracaoPrevistaPeriodo(dadosSQL.getDouble("valorAlteracaoPrevistaPeriodo"));
        obj.setSaldoOrcamentarioPlanejar(dadosSQL.getDouble("saldoOrcamentarioPlanejar"));
        obj.setOrcamentoTotalRealizado(dadosSQL.getDouble("orcamentoTotalRealizado"));
        obj.setLucro(dadosSQL.getDouble("lucro"));
        obj.setLucroPrevisto(dadosSQL.getDouble("lucroPrevisto"));
        obj.setPercentualCrescimentoReceita(dadosSQL.getDouble("percentualCrescimentoReceita"));
        obj.setPercentualCrescimentoDespesa(dadosSQL.getDouble("percentualCrescimentoDespesa"));
        obj.setListaUnidades(getFacadeFactory().getUnidadesPlanoOrcamentarioFacade().consultaRapidaPorPlanoOrcamentario(obj.getCodigo(), false, usuario));
        if (Uteis.isAtributoPreenchido(dadosSQL.getInt("responsavel"))) {
        	obj.setResponsavel(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(dadosSQL.getInt("responsavel"), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));
        }
//        obj.setDetalhamentoPeriodoOrcamento(getFacadeFactory().getDetalhamentoPeriodoOrcamentoFacade().consultarDetalhamentoPorPlanoOrcamentario(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, false, usuario));
        
        obj.setSolicitacaoOrcamentoPlanoOrcamentarioVOs(getFacadeFactory().getSolicitacaoOrcamentoPlanoOrcamentarioFacade().consultaPorPlanoOrcamentario(obj, usuario));
    }

    public List<PlanoOrcamentarioVO> consultaRapidaPorNome(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append("WHERE sem_acentos(lower(planoOrcamentario.nome)) like(sem_acentos('");
        sqlStr.append(valorConsulta.toLowerCase());
        sqlStr.append("%'))");
        sqlStr.append(" ORDER BY planoOrcamentario.nome");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsultaRapida(tabelaResultado);
    }

    public List<PlanoOrcamentarioVO> consultaRapidaPorSituacao(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append("WHERE sem_acentos(lower(planoOrcamentario.situacao)) like(sem_acentos('");
        sqlStr.append(valorConsulta.toLowerCase());
        sqlStr.append("%'))");
        sqlStr.append(" ORDER BY planoOrcamentario.situacao");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsultaRapida(tabelaResultado);
    }

    public List<PlanoOrcamentarioVO> consultaRapidaPorCodigo(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append("WHERE planoOrcamentario.codigo = ");
        sqlStr.append(valorConsulta.intValue());
        sqlStr.append(" ORDER BY planoOrcamentario.codigo");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsultaRapida(tabelaResultado);
    }

    public List<PlanoOrcamentarioVO> montarDadosConsultaRapida(SqlRowSet tabelaResultado) throws Exception {
        List<PlanoOrcamentarioVO> vetResultado = new ArrayList<PlanoOrcamentarioVO>(0);
        while (tabelaResultado.next()) {
            PlanoOrcamentarioVO obj = new PlanoOrcamentarioVO();
            montarDadosBasico(obj, tabelaResultado);
            vetResultado.add(obj);
            if (tabelaResultado.getRow() == 0) {
                return vetResultado;
            }
        }
        return vetResultado;
    }

    public List<PlanoOrcamentarioVO> executarVerificacaoUnidadesPainelGestor(List<UnidadeEnsinoVO> unidadeEnsinoGestorVOs, List<PlanoOrcamentarioVO> planoOrcamentarioVOs) {
        List<PlanoOrcamentarioVO> listaPlano = new ArrayList<>(0);
        for (PlanoOrcamentarioVO planoOrcamentarioVO : planoOrcamentarioVOs) {
            Boolean planoPossuiTodasUnidades = realizarVerificacaoUnidadesPainelGestor(unidadeEnsinoGestorVOs, planoOrcamentarioVO.getCodigo());
            if (planoPossuiTodasUnidades) {
                listaPlano.add(planoOrcamentarioVO);
            }
        }
        return listaPlano;

    }

    public Boolean realizarVerificacaoUnidadesPainelGestor(List<UnidadeEnsinoVO> unidadeEnsinoGestorVOs, Integer planoOrcamentario) {
        for (UnidadeEnsinoVO unidadeEnsinoVO : unidadeEnsinoGestorVOs) {
            Boolean possuiUnidade = getFacadeFactory().getUnidadesPlanoOrcamentarioFacade().consultarPorUnidadeEnsinoPlanoOrcamentario(planoOrcamentario, unidadeEnsinoVO.getCodigo());
            if (!possuiUnidade) {
                return possuiUnidade;
            }
        }

        return true;
    }

	@Override
	public void validarSeTodasSolicitacoesOrcamentoAprovadas(PlanoOrcamentarioVO planoOrcamentarioVO) throws Exception {
		if (!Uteis.isAtributoPreenchido(planoOrcamentarioVO.getSolicitacaoOrcamentoPlanoOrcamentarioVOs())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_PlanoOrcamentario_aprovarSomenteSeExisteSolicitacaoOrcamento"));
		}
		for (SolicitacaoOrcamentoPlanoOrcamentarioVO obj : planoOrcamentarioVO.getSolicitacaoOrcamentoPlanoOrcamentarioVOs()) {
			if (!obj.getSituacao().equals(SituacaoPlanoOrcamentarioEnum.APROVADO)) {
				throw new ConsistirException(UteisJSF.internacionalizar("msg_PlanoOrcamentario_aprovarSomenteSeTodasSolicitacoesAprovadas"));
			}
		}
	}
	
	@Override
	public List<PlanoOrcamentarioVO> consultarDadosPainelGestor(List<UnidadeEnsinoVO> unidadeEnsinoVOs){
		StringBuilder sql  = new StringBuilder("");
		sql.append(" select planoorcamentario.codigo, planoorcamentario.nome, planoorcamentario.datainicio, planoorcamentario.datafinal,   ");
		sql.append(" sum(solicitacaoorcamentoplanoorcamentario.valortotalaprovado) as valortotalaprovado, ");
		sql.append(" sum((select sum(quantidadeautorizada * case when compraitem.codigo is not null then compraitem.precoUnitario else requisicaoitem.valorunitario end) as valorconsumido ");
		sql.append(" 	from requisicaoitem ");
		sql.append(" 	inner join requisicao on requisicao.codigo = requisicaoitem.requisicao ");
		sql.append(" 	inner join itemsolicitacaoorcamentoplanoorcamentario on itemsolicitacaoorcamentoplanoorcamentario.codigo = requisicaoitem.itemsolicitacaoorcamentoplanoorcamentario ");
		sql.append("     left join compraitem on compraitem.codigo = requisicaoitem.compraitem  ");
		sql.append(" 	where itemsolicitacaoorcamentoplanoorcamentario.solicitacaoorcamentoplanoorcamentario = solicitacaoorcamentoplanoorcamentario.codigo ");
		sql.append(" 	and requisicao.situacaoautorizacao = 'AU' and requisicaoitem.quantidadeautorizada > 0 ");
		sql.append(" ))::numeric(20,2) as valorconsumido ");
		sql.append(" from planoorcamentario  ");
		sql.append(" inner join solicitacaoorcamentoplanoorcamentario on solicitacaoorcamentoplanoorcamentario.planoorcamentario = planoorcamentario.codigo ");
		sql.append(" where planoorcamentario.situacao = 'AT' and solicitacaoorcamentoplanoorcamentario.situacao = 'AP' ");
		if(Uteis.isAtributoPreenchido(unidadeEnsinoVOs)) {			
			sql.append(" and solicitacaoorcamentoplanoorcamentario.unidadeensino in (0 ");
			for(UnidadeEnsinoVO unidadeEnsinoVO: unidadeEnsinoVOs) {
				if(unidadeEnsinoVO.getFiltrarUnidadeEnsino()) {
					sql.append(",").append(unidadeEnsinoVO.getCodigo());
				}
			}
			sql.append(") ");
		}
		sql.append(" group by planoorcamentario.codigo, planoorcamentario.nome, planoorcamentario.datainicio, planoorcamentario.datafinal ");
		sql.append(" order by planoorcamentario.nome ");
		
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<PlanoOrcamentarioVO> planoOrcamentarioVOs = new ArrayList<PlanoOrcamentarioVO>();
		while(rs.next()) {
			PlanoOrcamentarioVO planoOrcamentarioVO = new PlanoOrcamentarioVO();
			planoOrcamentarioVO.setCodigo(rs.getInt("codigo"));
			planoOrcamentarioVO.setNome(rs.getString("nome"));
			planoOrcamentarioVO.setDataInicio(rs.getDate("datainicio"));
			planoOrcamentarioVO.setDataFinal(rs.getDate("datafinal"));
			planoOrcamentarioVO.setValorTotalAprovado(rs.getDouble("valortotalaprovado"));
			planoOrcamentarioVO.setValorTotalConsumido(rs.getDouble("valorconsumido"));
			planoOrcamentarioVO.setValorTotalPendente(rs.getDouble("valortotalaprovado") - rs.getDouble("valorconsumido"));
			planoOrcamentarioVOs.add(planoOrcamentarioVO);
		}
		return planoOrcamentarioVOs;
	}
}
