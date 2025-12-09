package negocio.facade.jdbc.academico;

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

import negocio.comuns.academico.CondicaoPagamentoPlanoFinanceiroCursoVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PlanoFinanceiroCursoVO;
import negocio.comuns.academico.enumeradores.SituacaoPlanoFinanceiroCursoEnum;
import negocio.comuns.academico.enumeradores.TipoGeracaoMaterialDidaticoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.academico.PlanoFinanceiroCursoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>PlanoFinanceiroCursoVO</code>. Responsável por implementar operações como incluir, alterar, excluir
 * e consultar pertinentes a classe <code>PlanoFinanceiroCursoVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see PlanoFinanceiroCursoVO
 * @see ControleAcesso
 */
@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class PlanoFinanceiroCurso extends ControleAcesso implements PlanoFinanceiroCursoInterfaceFacade {

    /**
	 * 
	 */
	private static final long serialVersionUID = 4635234730148949248L;
	protected static String idEntidade;

    public PlanoFinanceiroCurso() throws Exception {
        super();
        setIdEntidade("PlanoFinanceiroCurso");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>PlanoFinanceiroCursoVO</code>.
     */
    public PlanoFinanceiroCursoVO novo() throws Exception {
        PlanoFinanceiroCurso.incluir(getIdEntidade());
        PlanoFinanceiroCursoVO obj = new PlanoFinanceiroCursoVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>PlanoFinanceiroCursoVO</code>. Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a
     * conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>incluir</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>PlanoFinanceiroCursoVO</code> que será gravado no banco de dados.
     * @exception Exception
     *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final PlanoFinanceiroCursoVO obj, UsuarioVO usuario) throws Exception {
        try {
            PlanoFinanceiroCursoVO.validarDados(obj);
            PlanoFinanceiroCurso.incluir(getIdEntidade(), true, usuario);
            final String sql = "INSERT INTO PlanoFinanceiroCurso(modeloGeracaoParcelas, responsavelAutorizacao, controlarValorPorDisciplina, "
                    + "textoPadraoContratoMatricula, textopadraocontratofiador, descricao, textoPadraoContratoExtensao, "
                    + "textoPadraoContratoModular, textoPadraoContratoAditivo, unidadeEnsino, turma , textoPadraoContratoInclusaoReposicao, situacao, dataInativacao, responsavelInativacao) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    int i = 1;
                    sqlInserir.setString(i++, obj.getModeloGeracaoParcelas());
                    if (obj.getResponsavelAutorizacao().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(i++, obj.getResponsavelAutorizacao().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(i++, 0);
                    }
                    sqlInserir.setBoolean(i++, obj.getControlarValorPorDisciplina());
                    if (obj.getTextoPadraoContratoMatricula().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(i++, obj.getTextoPadraoContratoMatricula().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(i++, 0);
                    }
                    if (obj.getTextoPadraoContratoFiador().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(i++, obj.getTextoPadraoContratoFiador().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(i++, 0);
                    }
                    sqlInserir.setString(i++, obj.getDescricao());
                    if (obj.getTextoPadraoContratoExtensao().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(i++, obj.getTextoPadraoContratoExtensao().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(i++, 0);
                    }
                    if (obj.getTextoPadraoContratoModular().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(i++, obj.getTextoPadraoContratoModular().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(i++, 0);
                    }
                    if (obj.getTextoPadraoContratoAditivo().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(i++, obj.getTextoPadraoContratoAditivo().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(i++, 0);
                    }
                    if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(i++, obj.getUnidadeEnsino().getCodigo());
                    } else {
                        sqlInserir.setNull(i++, 0);
                    }
                    if (obj.getTurma().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(i++, obj.getTurma().getCodigo());
                    } else {
                        sqlInserir.setNull(i++, 0);
                    }
                    if (obj.getTextoPadraoContratoInclusaoReposicao().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(i++, obj.getTextoPadraoContratoInclusaoReposicao().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(i++, 0);
                    }
                    sqlInserir.setString(i++, obj.getSituacao().toString());
                    sqlInserir.setTimestamp(i++, Uteis.getDataJDBCTimestamp(obj.getDataInativacao()));
                    if (obj.getResponsavelInativacaoVO().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(i++, obj.getResponsavelInativacaoVO().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(i++, 0);
                    }
                    					
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
            incluirCondicoesPagamentoPlanoFinanceiroCurso(obj, usuario);
            obj.setNovoObj(Boolean.FALSE);
        } catch (Exception e) {
            obj.setCodigo(0);
            obj.setNovoObj(Boolean.TRUE);
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirCondicoesPagamentoPlanoFinanceiroCurso(PlanoFinanceiroCursoVO obj, UsuarioVO usuario) throws Exception {
        for (CondicaoPagamentoPlanoFinanceiroCursoVO condicao : obj.getCondicaoPagamentoPlanoFinanceiroCursoVOs()) {
            condicao.setPlanoFinanceiroCurso(obj.getCodigo());
            if (condicao.getNovoObj().booleanValue()) {
                getFacadeFactory().getCondicaoPagamentoPlanoFinanceiroCursoFacade().incluir(condicao, usuario);
            } else {
                getFacadeFactory().getCondicaoPagamentoPlanoFinanceiroCursoFacade().alterar(condicao, usuario);
            }
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarCondicoesPagamentoPlanoFinanceiroCurso(PlanoFinanceiroCursoVO obj, UsuarioVO usuario) throws Exception {
        for (CondicaoPagamentoPlanoFinanceiroCursoVO condicao : obj.getCondicaoPagamentoPlanoFinanceiroCursoVOs()) {
            condicao.setPlanoFinanceiroCurso(obj.getCodigo());
            if (condicao.getNovoObj().booleanValue()) {
                getFacadeFactory().getCondicaoPagamentoPlanoFinanceiroCursoFacade().incluir(condicao, usuario);
            } else {
                getFacadeFactory().getCondicaoPagamentoPlanoFinanceiroCursoFacade().alterar(condicao, usuario);
            }
        }
        getFacadeFactory().getCondicaoPagamentoPlanoFinanceiroCursoFacade().excluirCondicaoPagamentoNaoUtilizadaMatriculaPeriodo(obj.getCodigo(), obj.getCondicaoPagamentoPlanoFinanceiroCursoVOs(), usuario);
    }


    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final PlanoFinanceiroCursoVO obj, UsuarioVO usuario) throws Exception {
        try {
            PlanoFinanceiroCursoVO.validarDados(obj);
            PlanoFinanceiroCurso.alterar(getIdEntidade(), true, usuario);
            final String sql = "UPDATE PlanoFinanceiroCurso set modeloGeracaoParcelas=?, responsavelAutorizacao=?, controlarValorPorDisciplina=?, "
                    + "textoPadraoContratoMatricula=?, textopadraocontratofiador=?, descricao=?, textoPadraoContratoExtensao=?, textoPadraoContratoModular=?, textoPadraoContratoAditivo=?, unidadeEnsino=?, turma = ?, textopadraocontratoinclusaoreposicao=?, "
                    + " situacao=?, dataInativacao=?, responsavelInativacao=? WHERE (codigo = ?)"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setString(1, obj.getModeloGeracaoParcelas());
                    if (obj.getResponsavelAutorizacao().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(2, obj.getResponsavelAutorizacao().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(2, 0);
                    }
                    sqlAlterar.setBoolean(3, obj.getControlarValorPorDisciplina());
                    if (obj.getTextoPadraoContratoMatricula().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(4, obj.getTextoPadraoContratoMatricula().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(4, 0);
                    }
                    if (obj.getTextoPadraoContratoFiador().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(5, obj.getTextoPadraoContratoFiador().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(5, 0);
                    }
                    sqlAlterar.setString(6, obj.getDescricao());
                    if (obj.getTextoPadraoContratoExtensao().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(7, obj.getTextoPadraoContratoExtensao().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(7, 0);
                    }
                    if (obj.getTextoPadraoContratoModular().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(8, obj.getTextoPadraoContratoModular().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(8, 0);
                    }
                    if (obj.getTextoPadraoContratoAditivo().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(9, obj.getTextoPadraoContratoAditivo().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(9, 0);
                    }
                    if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(10, obj.getUnidadeEnsino().getCodigo());
                    } else {
                        sqlAlterar.setNull(10, 0);
                    }
                    if (obj.getTurma().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(11, obj.getTurma().getCodigo());
                    } else {
                        sqlAlterar.setNull(11, 0);
                    }
                    if (obj.getTextoPadraoContratoInclusaoReposicao().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(12, obj.getTextoPadraoContratoInclusaoReposicao().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(12, 0);
                    }
                    sqlAlterar.setString(13, obj.getSituacao().toString());
                    sqlAlterar.setTimestamp(14, Uteis.getDataJDBCTimestamp(obj.getDataInativacao()));
                    if (obj.getResponsavelInativacaoVO().getCodigo().intValue() != 0) {
                    	sqlAlterar.setInt(15, obj.getResponsavelInativacaoVO().getCodigo().intValue());
                    } else {
                    	sqlAlterar.setNull(15, 0);
                    }
                    sqlAlterar.setInt(16, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });
            alterarCondicoesPagamentoPlanoFinanceiroCurso(obj, usuario);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>PlanoFinanceiroCursoVO</code>. Sempre localiza o registro a ser excluído através da chave primária da entidade. Primeiramente
     * verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>excluir</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>PlanoFinanceiroCursoVO</code> que será removido no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(PlanoFinanceiroCursoVO obj, UsuarioVO usuarioVO) throws Exception {
        try {
            PlanoFinanceiroCurso.excluir(getIdEntidade(), true, usuarioVO);
            String sql = "DELETE FROM PlanoFinanceiroCurso WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>PlanoFinanceiroCurso</code> através do valor do atributo <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao
     * parâmetro fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>PlanoFinanceiroCursoVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<PlanoFinanceiroCursoVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM PlanoFinanceiroCurso WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, "", nivelMontarDados, usuario));

    }

    public List<PlanoFinanceiroCursoVO> consultarPorDescricao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM PlanoFinanceiroCurso WHERE upper(descricao) like ('" + valorConsulta.toUpperCase() + "%') ORDER BY descricao";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, "", nivelMontarDados, usuario));

    }

    public List consultarPorUnidadeEnsino(Integer unidadeEnsino, SituacaoPlanoFinanceiroCursoEnum situacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM PlanoFinanceiroCurso WHERE 1=1";
        if (!unidadeEnsino.equals(0)) {
            sqlStr += " AND (unidadeEnsino = " + unidadeEnsino + " OR unidadeEnsino is null) ";
        } else {
            sqlStr += " AND  unidadeEnsino is null ";
        }
        if (situacao != null && !situacao.equals(SituacaoPlanoFinanceiroCursoEnum.TODAS)) {
        	sqlStr += " AND (situacao = '"+situacao.toString()+"' OR situacao is null) ";
        }
        sqlStr += " ORDER BY descricao";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, "", nivelMontarDados, usuario));

    }

    public PlanoFinanceiroCursoVO consultarUnidadeEnsinoCursoTurno(Integer codigoUnidadeEnsino, Integer codigoCurso, Integer codigoTurno, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM PlanoFinanceiroCurso WHERE unidadeEnsino = ? and curso = ? and turno = ? ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[]{codigoUnidadeEnsino, codigoCurso, codigoTurno});
        if (!tabelaResultado.next()) {
            return new PlanoFinanceiroCursoVO();
        }
        return (montarDados(tabelaResultado, "", nivelMontarDados, usuario));
    }

    public List consultarPorCursoETurnoVinculadoTurma(Integer curso, Integer turno, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        StringBuilder sqlStr = new StringBuilder("");
        sqlStr.append("SELECT DISTINCT pfc.* FROM planofinanceirocurso pfc ");
        sqlStr.append("INNER JOIN turma on turma.planofinanceirocurso = pfc.codigo ");
        sqlStr.append("WHERE turma.curso = ? AND turma.turno = ?");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[]{curso, turno});
        return (montarDadosConsulta(tabelaResultado, "", nivelMontarDados, usuario));

    }

    public List consultarPorCursoETurnoVinculadoUnidadeEnsinoCurso(Integer curso, Integer turno, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        StringBuilder sqlStr = new StringBuilder("");
        sqlStr.append("SELECT DISTINCT pfc.* FROM planofinanceirocurso pfc ");
        sqlStr.append("INNER JOIN unidadeensinocurso uec ON uec.planofinanceirocurso = pfc.codigo ");
        sqlStr.append("WHERE uec.curso = ? AND uec.turno = ?");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[]{curso, turno});
        return (montarDadosConsulta(tabelaResultado, "", nivelMontarDados, usuario));

    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho
     * para um objeto por vez.
     *
     * @return List Contendo vários objetos da classe <code>PlanoFinanceiroCursoVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, String situacaoCondicaoPagamento, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            PlanoFinanceiroCursoVO obj = new PlanoFinanceiroCursoVO();
            obj = montarDados(tabelaResultado, situacaoCondicaoPagamento, nivelMontarDados, usuario);
            vetResultado.add(obj);
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um objeto da classe <code>PlanoFinanceiroCursoVO</code>.
     *
     * @return O objeto da classe <code>PlanoFinanceiroCursoVO</code> com os dados devidamente montados.
     */
    public static PlanoFinanceiroCursoVO montarDados(SqlRowSet dadosSQL, String situacao, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        PlanoFinanceiroCursoVO obj = new PlanoFinanceiroCursoVO();
        obj.setNovoObj(Boolean.FALSE);
        obj.setCodigo(dadosSQL.getInt("codigo"));
        obj.setDescricao(dadosSQL.getString("descricao"));
        obj.getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeEnsino"));
        obj.setModeloGeracaoParcelas(dadosSQL.getString("modeloGeracaoParcelas"));
        obj.getTextoPadraoContratoMatricula().setCodigo(dadosSQL.getInt("textoPadraoContratoMatricula"));
        obj.getTextoPadraoContratoFiador().setCodigo(dadosSQL.getInt("textoPadraoContratoFiador"));
        obj.getTextoPadraoContratoExtensao().setCodigo(dadosSQL.getInt("textoPadraoContratoExtensao"));
        obj.getTextoPadraoContratoModular().setCodigo(dadosSQL.getInt("textoPadraoContratoModular"));
        obj.getTextoPadraoContratoAditivo().setCodigo(dadosSQL.getInt("textoPadraoContratoAditivo"));
        obj.getTextoPadraoContratoInclusaoReposicao().setCodigo(dadosSQL.getInt("textoPadraoContratoInclusaoReposicao"));        
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
            return obj;
        }
        obj.getResponsavelAutorizacao().setCodigo(dadosSQL.getInt("responsavelAutorizacao"));
        obj.setControlarValorPorDisciplina(dadosSQL.getBoolean("controlarValorPorDisciplina"));
        obj.getTurma().setCodigo(dadosSQL.getInt("turma"));

        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
            montarDadosTextoPadrao(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
            return obj;
        }
        montarDadosTurma(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
        obj.setNovoObj(Boolean.FALSE);
        obj.setCondicaoPagamentoPlanoFinanceiroCursoVOs(montarDadosCondicoesPagamento(obj, situacao, nivelMontarDados, usuario));
        
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            montarDadosTextoPadrao(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
            return obj;
        }
        montarDadosResponsavelAutorizacao(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
        montarDadosTextoPadrao(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
        return obj;
    }
    
    public static void montarDadosTurma(PlanoFinanceiroCursoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getTurma().getCodigo().intValue() == 0) {
            return;
        }
        obj.setTurma(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getTurma().getCodigo(), nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>PessoaVO</code> relacionado ao objeto <code>PlanoFinanceiroCursoVO</code>. Faz uso da chave primária da classe
     * <code>PessoaVO</code> para realizar a consulta.
     *
     * @param obj
     *            Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosResponsavelAutorizacao(PlanoFinanceiroCursoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getResponsavelAutorizacao().getCodigo().intValue() == 0) {
            return;
        }
        obj.setResponsavelAutorizacao(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavelAutorizacao().getCodigo(), nivelMontarDados, usuario));
    }

    public static List<CondicaoPagamentoPlanoFinanceiroCursoVO> montarDadosCondicoesPagamento(PlanoFinanceiroCursoVO obj, String situacao, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List<CondicaoPagamentoPlanoFinanceiroCursoVO> lista = new ArrayList<CondicaoPagamentoPlanoFinanceiroCursoVO>(0);
        lista = getFacadeFactory().getCondicaoPagamentoPlanoFinanceiroCursoFacade().consultarPorCodigoPlanoFinanceiroCurso(obj.getCodigo(), situacao, false, nivelMontarDados, usuario);
        return lista;
    }

    public static void montarDadosTextoPadrao(PlanoFinanceiroCursoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getTextoPadraoContratoMatricula().getCodigo().intValue() != 0) {
            obj.setTextoPadraoContratoMatricula(getFacadeFactory().getTextoPadraoFacade().consultarPorChavePrimaria(obj.getTextoPadraoContratoMatricula().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));
        }
        if (obj.getTextoPadraoContratoFiador().getCodigo().intValue() != 0) {
            obj.setTextoPadraoContratoFiador(getFacadeFactory().getTextoPadraoFacade().consultarPorChavePrimaria(obj.getTextoPadraoContratoFiador().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));
        }
        if (obj.getTextoPadraoContratoExtensao().getCodigo().intValue() != 0) {
            obj.setTextoPadraoContratoExtensao(getFacadeFactory().getTextoPadraoFacade().consultarPorChavePrimaria(obj.getTextoPadraoContratoExtensao().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));
        }
        if (obj.getTextoPadraoContratoModular().getCodigo().intValue() != 0) {
            obj.setTextoPadraoContratoModular(getFacadeFactory().getTextoPadraoFacade().consultarPorChavePrimaria(obj.getTextoPadraoContratoModular().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));
        }
        if (obj.getTextoPadraoContratoAditivo().getCodigo().intValue() != 0) {
            obj.setTextoPadraoContratoAditivo(getFacadeFactory().getTextoPadraoFacade().consultarPorChavePrimaria(obj.getTextoPadraoContratoAditivo().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));
        }
        if (obj.getTextoPadraoContratoInclusaoReposicao().getCodigo().intValue() != 0) {
        	obj.setTextoPadraoContratoInclusaoReposicao(getFacadeFactory().getTextoPadraoFacade().consultarPorChavePrimaria(obj.getTextoPadraoContratoInclusaoReposicao().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));
        }
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>PlanoFinanceiroCursoVO</code> através de sua chave primária.
     *
     * @exception Exception
     *                Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public PlanoFinanceiroCursoVO consultarPorChavePrimaria(Integer codigoPrm, String situacao, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sqlStr = "SELECT * FROM PlanoFinanceiroCurso WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado, situacao, nivelMontarDados, usuario));
    }

    public PlanoFinanceiroCursoVO consultarModeloGeracaoPorChavePrimaria(Integer codigoPrm) throws Exception {    	
    	String sqlStr = "SELECT codigo, descricao, modeloGeracaoParcelas FROM PlanoFinanceiroCurso WHERE codigo = ?";
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[]{codigoPrm});
    	if (!tabelaResultado.next()) {
    		throw new ConsistirException("Dados Não Encontrados(Plano Financeiro Curso).");
    	}
        PlanoFinanceiroCursoVO obj = new PlanoFinanceiroCursoVO();
        obj.setNovoObj(Boolean.FALSE);
        obj.setCodigo(tabelaResultado.getInt("codigo"));
        obj.setDescricao(tabelaResultado.getString("descricao"));
        obj.setModeloGeracaoParcelas(tabelaResultado.getString("modeloGeracaoParcelas"));
    	return obj;
    }
	
    public void carregarDados(PlanoFinanceiroCursoVO obj, UsuarioVO usuario) throws Exception {
        carregarDados((PlanoFinanceiroCursoVO) obj, NivelMontarDados.TODOS, usuario);
    }
	
    /**
     * Método responsavel por validar se o Nivel de Montar Dados é Básico ou Completo e faz a consulta
     * de acordo com o nível especificado.
     * @param obj
     * @param nivelMontarDados
     * @throws Exception
     * @author Carlos
     */
    public void carregarDados(PlanoFinanceiroCursoVO obj, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception {
        SqlRowSet resultado = null;
        if ((nivelMontarDados.equals(NivelMontarDados.BASICO)) && (obj.getIsNivelMontarDadosNaoInicializado())) {
            resultado = consultaRapidaPorChavePrimariaDadosBasicos(obj.getCodigo(), usuario);
            montarDadosBasico((PlanoFinanceiroCursoVO) obj, resultado);
        }
        if ((nivelMontarDados.equals(NivelMontarDados.TODOS)) && (!obj.getIsNivelMontarDadosTodos())) {
            resultado = consultaRapidaPorChavePrimariaDadosCompletos(obj.getCodigo(), usuario);
            montarDadosCompleto((PlanoFinanceiroCursoVO) obj, resultado);
        }
    }

    private SqlRowSet consultaRapidaPorChavePrimariaDadosBasicos(Integer codPlanoFinanceiroCurso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append(" WHERE (PlanoFinanceiroCurso.codigo= ").append(codPlanoFinanceiroCurso).append(")");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return tabelaResultado;
    }

    private SqlRowSet consultaRapidaPorChavePrimariaDadosCompletos(Integer codPlanoFinanceiroCurso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaCompleta();
        sqlStr.append(" WHERE (PlanoFinanceiroCurso.codigo= ").append(codPlanoFinanceiroCurso).append(")");

        //Posteriormente essa lista vai ser ordenada por categoria, a descricao ficara como um segundo criterio para melhorar a visualizacao do usuario 
        sqlStr.append(" order by cppf.categoria ");
        
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return tabelaResultado;
    }

    private void montarDadosBasico(PlanoFinanceiroCursoVO obj, SqlRowSet dadosSQL) throws Exception {
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setDescricao(dadosSQL.getString("descricao"));
        if (dadosSQL.getString("situacao") != null) {
        	obj.setSituacao(SituacaoPlanoFinanceiroCursoEnum.valueOf(dadosSQL.getString("situacao")));
        }
        obj.setNivelMontarDados(NivelMontarDados.BASICO);
    }
    
    private void montarDadosCompleto(PlanoFinanceiroCursoVO obj, SqlRowSet dadosSQL) throws Exception {
        // Dados do PlanoFinanceiroCurso
        obj.setCodigo(new Integer(dadosSQL.getInt("planoFinanceiroCurso.codigo")));
        obj.setDescricao(dadosSQL.getString("planoFinanceiroCurso.descricao"));
        obj.setModeloGeracaoParcelas(dadosSQL.getString("planoFinanceiroCurso.modeloGeracaoParcelas"));
        obj.setControlarValorPorDisciplina(dadosSQL.getBoolean("planoFinanceiroCurso.controlarValorPorDisciplina"));
        obj.getUnidadeEnsino().setCodigo(dadosSQL.getInt("planoFinanceiroCurso.unidadeEnsino"));
        //Responsáve Autorização
        obj.getResponsavelAutorizacao().setCodigo(dadosSQL.getInt("responsavelAutorizacao.codigo"));
        obj.getResponsavelAutorizacao().setNome(dadosSQL.getString("responsavelAutorizacao.nome"));
        //Contrato Matricula
        obj.getTextoPadraoContratoMatricula().setCodigo(dadosSQL.getInt("contratoMatricula.codigo"));
        obj.getTextoPadraoContratoMatricula().setDescricao(dadosSQL.getString("contratoMatricula.descricao"));
        obj.getTextoPadraoContratoMatricula().setSituacao(dadosSQL.getString("contratoMatricula.situacao"));
        //Contrato Fiador
        obj.getTextoPadraoContratoFiador().setCodigo(dadosSQL.getInt("contratoFiador.codigo"));
        obj.getTextoPadraoContratoFiador().setDescricao(dadosSQL.getString("contratoFiador.descricao"));
        obj.getTextoPadraoContratoFiador().setSituacao(dadosSQL.getString("contratoFiador.situacao"));
        //Contrato Extensao
        obj.getTextoPadraoContratoExtensao().setCodigo(dadosSQL.getInt("contratoExtensao.codigo"));
        obj.getTextoPadraoContratoExtensao().setDescricao(dadosSQL.getString("contratoExtensao.descricao"));
        obj.getTextoPadraoContratoExtensao().setSituacao(dadosSQL.getString("contratoExtensao.situacao"));
        //Contrato Modular
        obj.getTextoPadraoContratoModular().setCodigo(dadosSQL.getInt("contratoModular.codigo"));
        obj.getTextoPadraoContratoModular().setDescricao(dadosSQL.getString("contratoModular.descricao"));
        obj.getTextoPadraoContratoModular().setSituacao(dadosSQL.getString("contratoModular.situacao"));
        //Contrato Aditivo
        obj.getTextoPadraoContratoAditivo().setCodigo(dadosSQL.getInt("contratoAditivo.codigo"));
        obj.getTextoPadraoContratoAditivo().setDescricao(dadosSQL.getString("contratoAditivo.descricao"));
        obj.getTextoPadraoContratoAditivo().setSituacao(dadosSQL.getString("contratoAditivo.situacao"));
        //Contrato InclusaoReposicao
        obj.getTextoPadraoContratoInclusaoReposicao().setCodigo(dadosSQL.getInt("contratoInclusaoReposicao.codigo"));
        obj.getTextoPadraoContratoInclusaoReposicao().setDescricao(dadosSQL.getString("contratoInclusaoReposicao.descricao"));
        obj.getTextoPadraoContratoInclusaoReposicao().setSituacao(dadosSQL.getString("contratoInclusaoReposicao.situacao"));
        
        obj.getTurma().setCodigo(dadosSQL.getInt("turma.codigo"));
        obj.getTurma().setIdentificadorTurma(dadosSQL.getString("turma.identificadorturma"));
        if (dadosSQL.getString("planoFinanceiroCurso.situacao") != null) {
        	obj.setSituacao(SituacaoPlanoFinanceiroCursoEnum.valueOf(dadosSQL.getString("planoFinanceiroCurso.situacao")));
        }
        obj.setDataInativacao(dadosSQL.getDate("planoFinanceiroCurso.dataInativacao"));
        obj.getResponsavelInativacaoVO().setCodigo(dadosSQL.getInt("planoFinanceiroCurso.responsavelInativacao"));
        obj.getResponsavelInativacaoVO().setNome(dadosSQL.getString("responsavelInativacao.nome"));

        // Dados da CondicaoPagamento
        CondicaoPagamentoPlanoFinanceiroCursoVO condicaoPagamentoVO = null;
        obj.getCondicaoPagamentoPlanoFinanceiroCursoVOs().clear();
        do {
            condicaoPagamentoVO = new CondicaoPagamentoPlanoFinanceiroCursoVO();
            condicaoPagamentoVO.setNovoObj(Boolean.FALSE);
            //Dados do DescontoProgressivo
            condicaoPagamentoVO.getDescontoProgressivoPadrao().setCodigo(dadosSQL.getInt("descontoProgressivoPadrao.codigo"));
            condicaoPagamentoVO.getDescontoProgressivoPadrao().setNome(dadosSQL.getString("descontoProgressivoPadrao.nome"));
            condicaoPagamentoVO.getDescontoProgressivoPadrao().setAtivado(dadosSQL.getBoolean("descontoProgressivoPadrao.ativado"));
            condicaoPagamentoVO.getDescontoProgressivoPrimeiraParcela().setCodigo(dadosSQL.getInt("descontoProgressivoPrimeiraParcela.codigo"));
            condicaoPagamentoVO.getDescontoProgressivoPrimeiraParcela().setNome(dadosSQL.getString("descontoProgressivoPrimeiraParcela.nome"));
            condicaoPagamentoVO.getDescontoProgressivoPrimeiraParcela().setAtivado(dadosSQL.getBoolean("descontoProgressivoPrimeiraParcela.ativado"));
            //Dados da Condição Pagamento
            condicaoPagamentoVO.setRestituirDiferencaValorMatriculaSistemaPorCredito(dadosSQL.getBoolean("cppf.restituirDiferencaValorMatriculaSistemaPorCredito"));
            condicaoPagamentoVO.setValorUnitarioCredito(dadosSQL.getDouble("cppf.valorUnitarioCredito"));
            condicaoPagamentoVO.setValorMinimoParcelaSistemaPorCredito(dadosSQL.getDouble("cppf.valorMinimoParcelaSistemaPorCredito"));
            condicaoPagamentoVO.setValorMatriculaSistemaPorCredito(dadosSQL.getDouble("cppf.valorMatriculaSistemaPorCredito"));
            condicaoPagamentoVO.setNrParcelasPeriodo(dadosSQL.getInt("cppf.nrparcelasPeriodo"));
            condicaoPagamentoVO.setValorParcela(dadosSQL.getDouble("cppf.valorParcela"));
            condicaoPagamentoVO.setValorPrimeiraParcela(dadosSQL.getDouble("cppf.valorPrimeiraParcela"));
            condicaoPagamentoVO.setVencimentoPrimeiraParcelaAntesMaterialDidatico(dadosSQL.getBoolean("cppf.vencimentoPrimeiraParcelaAntesMaterialDidatico"));
            condicaoPagamentoVO.setUsaValorPrimeiraParcela(dadosSQL.getBoolean("cppf.usaValorPrimeiraParcela"));
            condicaoPagamentoVO.setValorMatricula(dadosSQL.getDouble("cppf.valorMatricula"));
            condicaoPagamentoVO.setCodigo(dadosSQL.getInt("cppf.codigo"));
            condicaoPagamentoVO.setDescricao(dadosSQL.getString("cppf.descricao"));
            condicaoPagamentoVO.setNomeFormula(dadosSQL.getString("cppf.nomeFormula"));
            condicaoPagamentoVO.setFormulaCalculoValorFinal(dadosSQL.getString("cppf.formulaCalculoValorFinal"));
            condicaoPagamentoVO.setFormulaUsoVariavel1(dadosSQL.getString("cppf.formulaUsoVariavel1"));
            condicaoPagamentoVO.setFormulaCalculoVariavel1(dadosSQL.getString("cppf.formulaCalculoVariavel1"));
            condicaoPagamentoVO.setUtilizarVariavel1(dadosSQL.getBoolean("cppf.utilizarVariavel1"));
            condicaoPagamentoVO.setVariavel1(dadosSQL.getDouble("cppf.variavel1"));
            condicaoPagamentoVO.setFormulaUsoVariavel2(dadosSQL.getString("cppf.formulaUsoVariavel2"));
            condicaoPagamentoVO.setFormulaCalculoVariavel2(dadosSQL.getString("cppf.formulaCalculoVariavel2"));
            condicaoPagamentoVO.setUtilizarVariavel2(dadosSQL.getBoolean("cppf.utilizarVariavel2"));
            condicaoPagamentoVO.setVariavel2(dadosSQL.getDouble("cppf.variavel2"));
            condicaoPagamentoVO.setFormulaUsoVariavel3(dadosSQL.getString("cppf.formulaUsoVariavel3"));
            condicaoPagamentoVO.setFormulaCalculoVariavel3(dadosSQL.getString("cppf.formulaCalculoVariavel3"));
            condicaoPagamentoVO.setUtilizarVariavel3(dadosSQL.getBoolean("cppf.utilizarVariavel3"));
            condicaoPagamentoVO.setVariavel3(dadosSQL.getDouble("cppf.variavel3"));
            condicaoPagamentoVO.setTituloVariavel1(dadosSQL.getString("cppf.tituloVariavel1"));
            condicaoPagamentoVO.setTituloVariavel2(dadosSQL.getString("cppf.tituloVariavel2"));
            condicaoPagamentoVO.setTituloVariavel3(dadosSQL.getString("cppf.tituloVariavel3"));
            condicaoPagamentoVO.setPlanoFinanceiroCurso(dadosSQL.getInt("cppf.planoFinanceiroCurso"));
            condicaoPagamentoVO.setValorFixoDisciplinaIncluida(dadosSQL.getDouble("cppf.valorFixoDisciplinaIncluida"));
            condicaoPagamentoVO.setAplicarCalculoComBaseDescontosCalculados(dadosSQL.getBoolean("cppf.aplicarCalculoComBaseDescontosCalculados"));            
            
            condicaoPagamentoVO.setValorDescontoDisciplinaExcluida(dadosSQL.getDouble("cppf.valorDescontoDisciplinaExcluida"));
            
            condicaoPagamentoVO.setGerarParcelasSeparadasParaDisciplinasIncluidas(dadosSQL.getBoolean("cppf.gerarParcelasSeparadasParaDisciplinasincluidas"));
            condicaoPagamentoVO.setUtilizarValorMatriculaFixo(dadosSQL.getBoolean("cppf.utilizarValorMatriculaFixo"));
            condicaoPagamentoVO.setGerarCobrancaPorDisciplinasIncluidas(dadosSQL.getBoolean("cppf.gerarCobrancaPorDisciplinasIncluidas"));
            condicaoPagamentoVO.setGerarDescontoPorDiscipliaExcluidas(dadosSQL.getBoolean("cppf.gerarDescontoPorDisciplinaExcluidas"));
            condicaoPagamentoVO.setGerarDescontoPorDisciplinExcluidasSomenteUltimoPeriodo(dadosSQL.getBoolean("cppf.gerarDescontoPorDisciplinExcluidasSomenteUltimoPeriodo"));
            condicaoPagamentoVO.setNumeroMaximoDisciplinaCursarParaGerarDescontos(dadosSQL.getInt("cppf.numeroMaximoDisciplinaCursarParaGerarDescontos"));
            condicaoPagamentoVO.setRatiarValorDiferencaInclusaoExclusaoParaTodasParcelas(dadosSQL.getBoolean("cppf.ratiarValorDiferencaInclusaoExclusaoParaTodasParcelas"));
            condicaoPagamentoVO.setDescricaoDuracao(dadosSQL.getString("cppf.descricaoDuracao"));
            condicaoPagamentoVO.setSituacao(dadosSQL.getString("cppf.situacao"));
            condicaoPagamentoVO.setDataAtivacao(dadosSQL.getDate("cppf.dataAtivacao"));
            condicaoPagamentoVO.getResponsavelAtivacao().setCodigo(dadosSQL.getInt("cppf.responsavelAtivacao"));
            condicaoPagamentoVO.setDataInativacao(dadosSQL.getDate("cppf.dataInativacao"));
            condicaoPagamentoVO.getResponsavelInativacao().setCodigo(dadosSQL.getInt("cppf.responsavelInativacao"));
            condicaoPagamentoVO.setNaoControlarMatricula(dadosSQL.getBoolean("cppf.naoControlarMatricula"));
            condicaoPagamentoVO.setNaoRegerarParcelaVencida(dadosSQL.getBoolean("cppf.naoRegerarParcelaVencida"));
            condicaoPagamentoVO.setRegerarFinanceiro(dadosSQL.getBoolean("regerarFinanceiro"));
            
            //TextoPadraoContratoMatricula
            condicaoPagamentoVO.getTextoPadraoContratoMatricula().setCodigo(dadosSQL.getInt("contratoMatriculaCondicao.codigo"));
            condicaoPagamentoVO.getTextoPadraoContratoMatricula().setDescricao(dadosSQL.getString("contratoMatriculaCondicao.descricao"));
            condicaoPagamentoVO.getTextoPadraoContratoMatricula().setSituacao(dadosSQL.getString("contratoMatriculaCondicao.situacao"));
            //DescontoProgressivo Matricula
            condicaoPagamentoVO.getDescontoProgressivoPadraoMatricula().setCodigo(dadosSQL.getInt("descontoPadraoCondicao.codigo"));
            condicaoPagamentoVO.getDescontoProgressivoPadraoMatricula().setNome(dadosSQL.getString("descontoPadraoCondicao.nome"));
            condicaoPagamentoVO.getDescontoProgressivoPadraoMatricula().setAtivado(dadosSQL.getBoolean("descontoPadraoCondicao.ativado"));
            
            condicaoPagamentoVO.setTipoCobrancaInclusaoDisciplinaRegular(dadosSQL.getString("cppf.tipoCobrancaInclusaoDisciplinaRegular"));
            condicaoPagamentoVO.setCobrarDiscipRegularQuandoUltrapassarCreditosCHMaxPeriodoLetivo(dadosSQL.getBoolean("cobrarDiscipRegularQuandoUltrapassarCreditosCHMaxPeriodoLetivo"));
            condicaoPagamentoVO.setTipoCobrancaInclusaoDisciplinaDependencia(dadosSQL.getString("cppf.tipoCobrancaInclusaoDisciplinaDependencia"));
            condicaoPagamentoVO.setCobrarDiscipDependenciaQuandoUltrapassarCreditosCHMaxPerLet(dadosSQL.getBoolean("cobrarDiscipDependenciaQuandoUltrapassarCreditosCHMaxPerLet"));
            condicaoPagamentoVO.setTipoCalculoParcela(dadosSQL.getString("cppf.tipoCalculoParcela"));
            condicaoPagamentoVO.setTipoUsoCondicaoPagamento(dadosSQL.getString("cppf.tipoUsoCondicaoPagamento"));
            
            condicaoPagamentoVO.setValorPorAtividadeComplementar(dadosSQL.getDouble("cppf.valorPorAtividadeComplementar"));
            condicaoPagamentoVO.setTipoCobrancaAtividadeComplementar(dadosSQL.getString("cppf.tipoCobrancaAtividadeComplementar"));
            condicaoPagamentoVO.setNrCreditoPorAtividadeComplementar(dadosSQL.getInt("cppf.nrCreditoPorAtividadeComplementar"));
            condicaoPagamentoVO.setTipoCobrancaENADE(dadosSQL.getString("cppf.tipoCobrancaENADE"));
            condicaoPagamentoVO.setNrCreditoPorENADE(dadosSQL.getInt("cppf.nrCreditoPorENADE"));
            condicaoPagamentoVO.setValorPorENADE(dadosSQL.getDouble("cppf.valorPorENADE"));
            condicaoPagamentoVO.setTipoCobrancaEstagio(dadosSQL.getString("cppf.tipoCobrancaEstagio"));
            condicaoPagamentoVO.setNrCreditoPorEstagio(dadosSQL.getInt("cppf.nrCreditoPorEstagio"));
            condicaoPagamentoVO.setValorPorEstagio(dadosSQL.getDouble("cppf.valorPorEstagio"));
            condicaoPagamentoVO.setValorFixoDisciplinaIncluidaEAD(dadosSQL.getDouble("cppf.valorFixoDisciplinaIncluidaEAD"));
            condicaoPagamentoVO.setUtilizarPoliticaCobrancaEspecificaParaOptativas(dadosSQL.getBoolean("utilizarPoliticaCobrancaEspecificaParaOptativas"));
            condicaoPagamentoVO.setTipoCobrancaDisciplinaOptativa(dadosSQL.getString("cppf.tipoCobrancaDisciplinaOptativa"));
            condicaoPagamentoVO.setCobrarDiscipOptativaQuandoUltrapassarCredCHMaxPerLet(dadosSQL.getBoolean("cobrarDiscipOptativaQuandoUltrapassarCredCHMaxPerLet"));
            condicaoPagamentoVO.setValorDisciplinaOptativa(dadosSQL.getDouble("cppf.valorDisciplinaOptativa"));
            condicaoPagamentoVO.setValorDisciplinaOptativaEAD(dadosSQL.getDouble("cppf.valorDisciplinaOptativaEAD"));
            condicaoPagamentoVO.setValorDisciplinaIncluidaDependencia(dadosSQL.getDouble("cppf.valorDisciplinaIncluidaDependencia"));
            condicaoPagamentoVO.setValorDisciplinaIncluidaDependenciaEAD(dadosSQL.getDouble("cppf.valorDisciplinaIncluidaDependenciaEAD"));
            condicaoPagamentoVO.setUtilizarPoliticaDescontoDiscipRegularFeitasViaEAD(dadosSQL.getBoolean("cppf.utilizarPoliticaDescontoDiscipRegularFeitasViaEAD"));
            condicaoPagamentoVO.setTipoDescontoDisciplinaRegularEAD(dadosSQL.getString("cppf.tipoDescontoDisciplinaRegularEAD"));
            condicaoPagamentoVO.setValorDescontoDisciplinaRegularEAD(dadosSQL.getDouble("cppf.valorDescontoDisciplinaRegularEAD"));         
            condicaoPagamentoVO.setTipoCobrancaExclusaoDisciplinaRegular(dadosSQL.getString("cppf.tipoCobrancaExclusaoDisciplinaRegular"));
            condicaoPagamentoVO.setValorFixoDisciplinaExcluidaEAD(dadosSQL.getDouble("cppf.valorFixoDisciplinaExcluidaEAD"));
            condicaoPagamentoVO.setConsiderarValorRateioBaseadoValorBaseComDescontosAplicados(dadosSQL.getBoolean("considerarValorRateioBaseadoValorBaseComDescontosAplicados"));
            condicaoPagamentoVO.setAbaterValorRateiroComoDescontoRateio(dadosSQL.getBoolean("abaterValorRateiroComoDescontoRateio"));
            condicaoPagamentoVO.setGerarParcelasExtrasSeparadoMensalidadeAReceber(dadosSQL.getBoolean("gerarParcelasExtrasSeparadoMensalidadeAReceber"));
            condicaoPagamentoVO.setConsiderarValorRateioExtraParcelaVencida(dadosSQL.getBoolean("considerarValorRateioExtraParcelaVencida"));

            //Aba material didatico
            condicaoPagamentoVO.setGerarParcelaMaterialDidatico(dadosSQL.getBoolean("gerarParcelaMaterialDidatico"));
            condicaoPagamentoVO.setUsarUnidadeEnsinoEspecifica(dadosSQL.getBoolean("usarUnidadeEnsinoEspecifica"));
            condicaoPagamentoVO.setControlaDiaBaseVencimentoParcelaMaterialDidatico(dadosSQL.getBoolean("controlaDiaBaseVencimentoParcelaMaterialDidatico"));
            condicaoPagamentoVO.setAplicarDescontosParcelasNoMaterialDidatico(dadosSQL.getBoolean("aplicarDescontosParcelasNoMaterialDidatico"));
            condicaoPagamentoVO.setAplicarDescontoMaterialDidaticoDescontoAluno(dadosSQL.getBoolean("aplicarDescontoMaterialDidaticoDescontoAluno"));
            condicaoPagamentoVO.setAplicarDescontoMaterialDidaticoDescontoInstitucional(dadosSQL.getBoolean("aplicarDescontoMaterialDidaticoDescontoInstitucional"));
            condicaoPagamentoVO.setAplicarDescontoMaterialDidaticoDescontoProgressivo(dadosSQL.getBoolean("aplicarDescontoMaterialDidaticoDescontoProgressivo"));
            condicaoPagamentoVO.setAplicarDescontoMaterialDidaticoDescontoConvenio(dadosSQL.getBoolean("aplicarDescontoMaterialDidaticoDescontoConvenio"));
            condicaoPagamentoVO.setAplicarDescontosDesconsiderandosVencimento(dadosSQL.getBoolean("aplicarDescontosDesconsiderandosVencimento"));
            condicaoPagamentoVO.getUnidadeEnsinoFinanceira().setCodigo(dadosSQL.getInt("unidadeEnsinoFinanceira.codigo"));;
            condicaoPagamentoVO.getUnidadeEnsinoFinanceira().setNome(dadosSQL.getString("unidadeEnsinoFinanceira.nome"));;
            condicaoPagamentoVO.setQuantidadeParcelasMaterialDidatico(dadosSQL.getLong("quantidadeParcelasMaterialDidatico"));
            condicaoPagamentoVO.setDiaBaseVencimentoParcelaOutraUnidade(dadosSQL.getLong("diaBaseVencimentoParcelaOutraUnidade"));
            condicaoPagamentoVO.setValorPorParcelaMaterialDidatico(dadosSQL.getDouble("valorPorParcelaMaterialDidatico"));
            condicaoPagamentoVO.setConsiderarParcelasMaterialDidaticoReajustePreco(dadosSQL.getBoolean("considerarParcelasMaterialDidaticoReajustePreco"));
            if(Uteis.isAtributoPreenchido(dadosSQL.getString("tipoGeracaoMaterialDidatico"))){
            	condicaoPagamentoVO.setTipoGeracaoMaterialDidatico(TipoGeracaoMaterialDidaticoEnum.valueOf(dadosSQL.getString("tipoGeracaoMaterialDidatico")));	
            }
            
          //Aba Outras configuracoes
            condicaoPagamentoVO.setApresentarCondicaoVisaoAluno(dadosSQL.getBoolean("cppf.apresentarCondicaoVisaoAluno"));
            condicaoPagamentoVO.getCentroReceita().setCodigo(dadosSQL.getInt("centroReceita.codigo"));
            condicaoPagamentoVO.getCentroReceita().setDescricao(dadosSQL.getString("centroReceita.descricao"));
            condicaoPagamentoVO.setApresentarSomenteParaDeterminadaFormaIngresso(dadosSQL.getBoolean("cppf.apresentarSomenteParaDeterminadaFormaIngresso"));
            condicaoPagamentoVO.setApresentarSomenteParaIngressanteNoSemestreAno(dadosSQL.getBoolean("cppf.apresentarSomenteParaIngressanteNoSemestreAno"));
            condicaoPagamentoVO.setFormaIngressoEnem(dadosSQL.getBoolean("cppf.formaIngressoEnem"));
            condicaoPagamentoVO.setFormaIngressoEntrevista(dadosSQL.getBoolean("cppf.formaIngressoEntrevista"));
            condicaoPagamentoVO.setFormaIngressoOutroTipoSelecao(dadosSQL.getBoolean("cppf.formaIngressoOutroTipoSelecao"));
            condicaoPagamentoVO.setFormaIngressoPortadorDiploma(dadosSQL.getBoolean("cppf.formaIngressoPortadorDiploma"));
            condicaoPagamentoVO.setFormaIngressoProcessoSeletivo(dadosSQL.getBoolean("cppf.formaIngressoProcessoSeletivo"));
            condicaoPagamentoVO.setFormaIngressoProuni(dadosSQL.getBoolean("cppf.formaIngressoProuni"));
            condicaoPagamentoVO.setFormaIngressoReingresso(dadosSQL.getBoolean("cppf.formaIngressoReingresso"));
            condicaoPagamentoVO.setFormaIngressoTransferenciaExterna(dadosSQL.getBoolean("cppf.formaIngressoTransferenciaExterna"));
            condicaoPagamentoVO.setFormaIngressoTransferenciaInterna(dadosSQL.getBoolean("cppf.formaIngressoTransferenciaInterna"));
            condicaoPagamentoVO.setSemestreIngressante(dadosSQL.getString("cppf.semestreIngressante"));
            condicaoPagamentoVO.setAnoIngressante(dadosSQL.getString("cppf.anoIngressante"));

            condicaoPagamentoVO.setApresentarSomenteParaRenovacoesNoSemestreAno(dadosSQL.getBoolean("cppf.apresentarSomenteParaRenovacoesNoSemestreAno"));
            condicaoPagamentoVO.setSemestreRenovacao(dadosSQL.getString("cppf.semestreRenovacao"));
            condicaoPagamentoVO.setAnoRenovacao(dadosSQL.getString("cppf.anoRenovacao"));
            
            condicaoPagamentoVO.setApresentarSomenteParaAlunosIntegralizandoCurso(dadosSQL.getBoolean("cppf.apresentarSomenteParaAlunosIntegralizandoCurso"));
            condicaoPagamentoVO.setConsiderarIntegralizandoEstiverCursandoUltimoPer2Vez(dadosSQL.getBoolean("cppf.considerarIntegralizandoEstiverCursandoUltimoPer2Vez"));
            condicaoPagamentoVO.setTipoControleAlunoIntegralizandoCurso(dadosSQL.getString("cppf.tipoControleAlunoIntegralizandoCurso"));
            condicaoPagamentoVO.setValorBaseDefinirAlunoIntegralizandoCurso(dadosSQL.getDouble("cppf.valorBaseDefinirAlunoIntegralizandoCurso"));
            condicaoPagamentoVO.setPermiteRecebimentoOnlineCartaoCredito(dadosSQL.getBoolean("cppf.permiterecebimentoonlinecartaocredito"));
            condicaoPagamentoVO.setApresentarMatriculaOnlineExterna(dadosSQL.getBoolean("apresentarMatriculaOnlineExterna"));
            condicaoPagamentoVO.setApresentarMatriculaOnlineProfessor(dadosSQL.getBoolean("apresentarMatriculaOnlineProfessor"));
            condicaoPagamentoVO.setApresentarMatriculaOnlineCoordenador(dadosSQL.getBoolean("apresentarMatriculaOnlineCoordenador"));

            condicaoPagamentoVO.setDefinirPlanoDescontoApresentarMatricula(dadosSQL.getBoolean("definirPlanoDescontoApresentarMatricula"));
            
            
            
            condicaoPagamentoVO.setCondicaoPlanoFinanceiroCursoTurmaVOs(getFacadeFactory().getCondicaoPlanoFinanceiroCursoTurmaFacade().consultarPorCondicaoPlanoFinanceiroCurso(condicaoPagamentoVO.getCodigo()));
            condicaoPagamentoVO.setCondicaoPagamentoPlanoDescontoVOs(getFacadeFactory().getCondicaoPagamentoPlanoDescontoFacade().consultarPorCondicaoPagamentoPlanoFinanceiroCurso(condicaoPagamentoVO.getCodigo(), false, null));
            condicaoPagamentoVO.setPlanoDescontoDisponivelMatriculaVOs(getFacadeFactory().getPlanoDescontoDisponivelMatriculaFacade().consultarPorCodigoPlanoDescontoDisponivelMatricula(condicaoPagamentoVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));
            
            condicaoPagamentoVO.setCategoria(dadosSQL.getString("cppf.categoria"));
            condicaoPagamentoVO.setNaoControlarValorParcela(dadosSQL.getBoolean("cppf.naoControlarValorParcela"));
            condicaoPagamentoVO.setLancarValorRatiadoSobreValorBaseContaReceber(dadosSQL.getBoolean("lancarvalorratiadosobrevalorbasecontareceber"));
            obj.getCondicaoPagamentoPlanoFinanceiroCursoVOs().add(condicaoPagamentoVO);
            if (dadosSQL.isLast() || (obj.getCodigo() != (dadosSQL.getInt("planoFinanceiroCurso.codigo")))) {
                return;
            }
        } while (dadosSQL.next());

        obj.setNivelMontarDados(NivelMontarDados.TODOS);
    }

    private StringBuffer getSQLPadraoConsultaBasica() {
        StringBuffer str = new StringBuffer();
        str.append("SELECT DISTINCT codigo, descricao, situacao FROM planofinanceirocurso ");
        return str;
    }

    private StringBuffer getSQLPadraoConsultaCompleta() {
        StringBuffer str = new StringBuffer();
        str.append("SELECT planoFinanceiroCurso.codigo AS \"planoFinanceiroCurso.codigo\", planoFinanceiroCurso.unidadeEnsino AS \"planoFinanceiroCurso.unidadeEnsino\", planoFinanceiroCurso.descricao AS \"planoFinanceiroCurso.descricao\", ");
        str.append(" planoFinanceiroCurso.modeloGeracaoParcelas AS \"planoFinanceiroCurso.modeloGeracaoParcelas\", planoFinanceiroCurso.controlarValorPorDisciplina AS \"planoFinanceiroCurso.controlarValorPorDisciplina\", ");
        str.append(" planoFinanceiroCurso.diaVencimentoParcela AS \"planoFinanceiroCurso.diaVencimentoParcela\", planoFinanceiroCurso.situacao AS \"planoFinanceiroCurso.situacao\", ");
        str.append(" planoFinanceiroCurso.dataInativacao AS \"planoFinanceiroCurso.dataInativacao\", planoFinanceiroCurso.responsavelInativacao AS \"planoFinanceiroCurso.responsavelInativacao\", ");
        str.append(" responsavelInativacao.nome AS \"responsavelInativacao.nome\", ");
        str.append(" usuario.codigo AS \"responsavelAutorizacao.codigo\", usuario.nome AS \"responsavelAutorizacao.nome\", ");
        str.append(" contratoMatricula.codigo AS \"contratoMatricula.codigo\", contratoMatricula.descricao AS \"contratoMatricula.descricao\", contratoMatricula.situacao AS \"contratoMatricula.situacao\", ");
        str.append(" contratoFiador.codigo AS \"contratoFiador.codigo\", contratoFiador.descricao AS \"contratoFiador.descricao\", contratoFiador.situacao AS \"contratoFiador.situacao\", ");
        str.append(" contratoExtensao.codigo AS \"contratoExtensao.codigo\", contratoExtensao.descricao AS \"contratoExtensao.descricao\", contratoExtensao.situacao AS \"contratoExtensao.situacao\", ");
        str.append(" contratoModular.codigo AS \"contratoModular.codigo\", contratoModular.descricao AS \"contratoModular.descricao\", contratoModular.situacao AS \"contratoModular.situacao\", ");
        str.append(" contratoAditivo.codigo AS \"contratoAditivo.codigo\", contratoAditivo.descricao AS \"contratoAditivo.descricao\", contratoAditivo.situacao AS \"contratoAditivo.situacao\", ");
        str.append(" contratoInclusaoReposicao.codigo AS \"contratoInclusaoReposicao.codigo\", contratoInclusaoReposicao.descricao AS \"contratoInclusaoReposicao.descricao\", contratoInclusaoReposicao.situacao AS \"contratoInclusaoReposicao.situacao\", ");
        str.append(" descontoProgressivo.codigo AS \"descontoProgressivoPadrao.codigo\", descontoProgressivo.nome AS \"descontoProgressivoPadrao.nome\", descontoProgressivo.ativado AS \"descontoProgressivoPadrao.ativado\", ");
        str.append(" descontoProgressivoPrimeiraParcela.codigo AS \"descontoProgressivoPrimeiraParcela.codigo\", descontoProgressivoPrimeiraParcela.nome AS \"descontoProgressivoPrimeiraParcela.nome\", descontoProgressivoPrimeiraParcela.ativado AS \"descontoProgressivoPrimeiraParcela.ativado\", ");
        str.append(" cppf.restituirDiferencaValorMatriculaSistemaPorCredito AS \"cppf.restituirDiferencaValorMatriculaSistemaPorCredito\", cppf.valorUnitarioCredito AS \"cppf.valorUnitarioCredito\", cppf.valorMinimoParcelaSistemaPorCredito AS \"cppf.valorMinimoParcelaSistemaPorCredito\", ");
        str.append(" cppf.valorMatriculaSistemaPorCredito AS \"cppf.valorMatriculaSistemaPorCredito\", cppf.nrparcelasPeriodo AS \"cppf.nrparcelasPeriodo\", cppf.valorParcela AS \"cppf.valorParcela\", cppf.valorMatricula AS \"cppf.valorMatricula\", ");
        str.append(" cppf.codigo AS \"cppf.codigo\", cppf.descricao AS \"cppf.descricao\", cppf.nomeFormula AS \"cppf.nomeFormula\", cppf.formulaCalculoValorFinal AS \"cppf.formulaCalculoValorFinal\", cppf.formulaUsoVariavel1 AS \"cppf.formulaUsoVariavel1\", ");
        str.append(" cppf.formulaCalculoVariavel1 AS \"cppf.formulaCalculoVariavel1\", cppf.utilizarVariavel1 AS \"cppf.utilizarVariavel1\", cppf.utilizarVariavel1 AS \"cppf.utilizarVariavel1\", cppf.variavel1 AS \"cppf.variavel1\",  ");
        str.append(" cppf.formulaUsoVariavel2 AS \"cppf.formulaUsoVariavel2\", cppf.formulaCalculoVariavel2 AS \"cppf.formulaCalculoVariavel2\", cppf.utilizarVariavel2 AS \"cppf.utilizarVariavel2\", cppf.variavel2 AS \"cppf.variavel2\", ");
        str.append(" cppf.formulaUsoVariavel3 AS \"cppf.formulaUsoVariavel3\", cppf.formulaCalculoVariavel3 AS \"cppf.formulaCalculoVariavel3\", cppf.utilizarVariavel3 AS \"cppf.utilizarVariavel3\", cppf.variavel3 AS \"cppf.variavel3\", ");
        str.append(" cppf.tituloVariavel1 AS \"cppf.tituloVariavel1\", cppf.tituloVariavel2 AS \"cppf.tituloVariavel2\", cppf.tituloVariavel3 AS \"cppf.tituloVariavel3\", cppf.planoFinanceiroCurso AS \"cppf.planoFinanceiroCurso\", ");
        str.append(" cppf.valorFixoDisciplinaIncluida AS \"cppf.valorFixoDisciplinaIncluida\", cppf.valorDescontoDisciplinaExcluida AS \"cppf.valorDescontoDisciplinaExcluida\", cppf.gerarParcelasSeparadasParaDisciplinasincluidas AS \"cppf.gerarParcelasSeparadasParaDisciplinasincluidas\", ");
        str.append(" cppf.utilizarValorMatriculaFixo AS \"cppf.utilizarValorMatriculaFixo\", cppf.gerarCobrancaPorDisciplinasIncluidas AS \"cppf.gerarCobrancaPorDisciplinasIncluidas\", cppf.gerarDescontoPorDiscipliaExcluidas AS \"cppf.gerarDescontoPorDisciplinaExcluidas\", ");
        str.append(" cppf.gerarDescontoPorDisciplinExcluidasSomenteUltimoPeriodo AS \"cppf.gerarDescontoPorDisciplinExcluidasSomenteUltimoPeriodo\", cppf.numeroMaximoDisciplinaCursarParaGerarDescontos AS \"cppf.numeroMaximoDisciplinaCursarParaGerarDescontos\", ");
        str.append(" cppf.ratiarValorDiferencaInclusaoExclusaoParaTodasParcelas AS \"cppf.ratiarValorDiferencaInclusaoExclusaoParaTodasParcelas\", cppf.descricaoDuracao AS \"cppf.descricaoDuracao\", cppf.situacao AS \"cppf.situacao\",  ");
        str.append(" cppf.dataAtivacao AS \"cppf.dataAtivacao\", cppf.responsavelAtivacao AS \"cppf.responsavelAtivacao\", cppf.dataInativacao AS \"cppf.dataInativacao\", cppf.responsavelInativacao AS \"cppf.responsavelInativacao\", ");
        str.append(" cppf.naoControlarMatricula AS \"cppf.naoControlarMatricula\", cppf.apresentarCondicaoVisaoAluno AS \"cppf.apresentarCondicaoVisaoAluno\", ");
        str.append(" cppf.tipoCobrancaExclusaoDisciplinaRegular AS \"cppf.tipoCobrancaExclusaoDisciplinaRegular\", cppf.valorFixoDisciplinaExcluidaEAD AS \"cppf.valorFixoDisciplinaExcluidaEAD\", cppf.apresentarSomenteParaDeterminadaFormaIngresso AS \"cppf.apresentarSomenteParaDeterminadaFormaIngresso\", cppf.apresentarSomenteParaIngressanteNoSemestreAno AS \"cppf.apresentarSomenteParaIngressanteNoSemestreAno\", ");
        str.append(" cppf.formaIngressoEntrevista AS \"cppf.formaIngressoEntrevista\", cppf.formaIngressoPortadorDiploma AS \"cppf.formaIngressoPortadorDiploma\", cppf.formaIngressoTransferenciaInterna AS \"cppf.formaIngressoTransferenciaInterna\", cppf.formaIngressoProcessoSeletivo AS \"cppf.formaIngressoProcessoSeletivo\", cppf.formaIngressoTransferenciaExterna \"cppf.formaIngressoTransferenciaExterna\", cppf.formaIngressoReingresso AS \"cppf.formaIngressoReingresso\", ");
        str.append(" cppf.formaIngressoProuni AS \"cppf.formaIngressoProuni\", cppf.formaIngressoEnem AS \"cppf.formaIngressoEnem\", cppf.formaIngressoOutroTipoSelecao AS \"cppf.formaIngressoOutroTipoSelecao\", cppf.anoIngressante AS \"cppf.anoIngressante\", cppf.semestreIngressante AS \"cppf.semestreIngressante\", cppf.naoRegerarParcelaVencida AS \"cppf.naoRegerarParcelaVencida\", ");
        str.append(" cppf.apresentarSomenteParaAlunosIntegralizandoCurso AS \"cppf.apresentarSomenteParaAlunosIntegralizandoCurso\", cppf.considerarIntegralizandoEstiverCursandoUltimoPer2Vez AS \"cppf.considerarIntegralizandoEstiverCursandoUltimoPer2Vez\", cppf.tipoControleAlunoIntegralizandoCurso AS \"cppf.tipoControleAlunoIntegralizandoCurso\", cppf.valorBaseDefinirAlunoIntegralizandoCurso AS \"cppf.valorBaseDefinirAlunoIntegralizandoCurso\", ");
        str.append(" cppf.apresentarSomenteParaRenovacoesNoSemestreAno AS \"cppf.apresentarSomenteParaRenovacoesNoSemestreAno\", cppf.anoRenovacao AS \"cppf.anoRenovacao\", cppf.semestreRenovacao AS \"cppf.semestreRenovacao\", ");
        		
        str.append("cppf.tipoCobrancaInclusaoDisciplinaRegular AS \"cppf.tipoCobrancaInclusaoDisciplinaRegular\", cppf.cobrarDiscipRegularQuandoUltrapassarCreditosCHMaxPeriodoLetivo AS \"cobrarDiscipRegularQuandoUltrapassarCreditosCHMaxPeriodoLetivo\", cppf.tipoCobrancaInclusaoDisciplinaDependencia AS \"cppf.tipoCobrancaInclusaoDisciplinaDependencia\", ");
        str.append("cppf.cobrarDiscipDependenciaQuandoUltrapassarCreditosCHMaxPerLet AS \"cobrarDiscipDependenciaQuandoUltrapassarCreditosCHMaxPerLet\", cppf.tipoCalculoParcela AS \"cppf.tipoCalculoParcela\", cppf.tipoUsoCondicaoPagamento AS \"cppf.tipoUsoCondicaoPagamento\", ");
        str.append("cppf.valorPorAtividadeComplementar AS \"cppf.valorPorAtividadeComplementar\", cppf.tipoCobrancaAtividadeComplementar AS \"cppf.tipoCobrancaAtividadeComplementar\", cppf.nrCreditoPorAtividadeComplementar AS \"cppf.nrCreditoPorAtividadeComplementar\", ");
        str.append("cppf.tipoCobrancaENADE AS \"cppf.tipoCobrancaENADE\", cppf.nrCreditoPorENADE AS \"cppf.nrCreditoPorENADE\", cppf.valorPorENADE AS \"cppf.valorPorENADE\", cppf.tipoCobrancaEstagio AS \"cppf.tipoCobrancaEstagio\", cppf.nrCreditoPorEstagio AS \"cppf.nrCreditoPorEstagio\", cppf.valorPorEstagio AS \"cppf.valorPorEstagio\", ");
        str.append("cppf.valorFixoDisciplinaIncluidaEAD AS \"cppf.valorFixoDisciplinaIncluidaEAD\", cppf.utilizarPoliticaCobrancaEspecificaParaOptativas AS \"utilizarPoliticaCobrancaEspecificaParaOptativas\", cppf.tipoCobrancaDisciplinaOptativa AS \"cppf.tipoCobrancaDisciplinaOptativa\", ");
        str.append("cppf.cobrarDiscipOptativaQuandoUltrapassarCredCHMaxPerLet AS \"cobrarDiscipOptativaQuandoUltrapassarCredCHMaxPerLet\", cppf.valorDisciplinaOptativa AS \"cppf.valorDisciplinaOptativa\", cppf.valorDisciplinaOptativaEAD AS \"cppf.valorDisciplinaOptativaEAD\", ");
        str.append("cppf.valorDisciplinaIncluidaDependencia AS \"cppf.valorDisciplinaIncluidaDependencia\", cppf.valorDisciplinaIncluidaDependenciaEAD AS \"cppf.valorDisciplinaIncluidaDependenciaEAD\", cppf.utilizarPoliticaDescontoDiscipRegularFeitasViaEAD AS \"cppf.utilizarPoliticaDescontoDiscipRegularFeitasViaEAD\", ");
        str.append("cppf.tipoDescontoDisciplinaRegularEAD AS \"cppf.tipoDescontoDisciplinaRegularEAD\", cppf.valorDescontoDisciplinaRegularEAD AS \"cppf.valorDescontoDisciplinaRegularEAD\", cppf.naoRegerarParcelaVencida AS \"cppf.naoRegerarParcelaVencida\", ");
        str.append("cppf.aplicarCalculoComBaseDescontosCalculados AS \"cppf.aplicarCalculoComBaseDescontosCalculados\", ");
        str.append("cppf.apresentarMatriculaOnlineExterna, cppf.apresentarMatriculaOnlineProfessor, cppf.apresentarMatriculaOnlineCoordenador, ");
        str.append("cppf.considerarValorRateioExtraParcelaVencida, ");
        str.append("cppf.considerarValorRateioBaseadoValorBaseComDescontosAplicados, cppf.abaterValorRateiroComoDescontoRateio, cppf.gerarParcelasExtrasSeparadoMensalidadeAReceber, ");
        str.append("cppf.gerarParcelaMaterialDidatico, cppf.usarUnidadeEnsinoEspecifica, cppf.controlaDiaBaseVencimentoParcelaMaterialDidatico, ");
        str.append("cppf.aplicarDescontosParcelasNoMaterialDidatico, cppf.aplicarDescontoMaterialDidaticoDescontoAluno, cppf.aplicarDescontoMaterialDidaticoDescontoInstitucional, ");
        str.append("cppf.aplicarDescontoMaterialDidaticoDescontoProgressivo, cppf.aplicarDescontoMaterialDidaticoDescontoConvenio, cppf.aplicarDescontosDesconsiderandosVencimento, ");
        str.append("cppf.quantidadeParcelasMaterialDidatico, cppf.diaBaseVencimentoParcelaOutraUnidade, ");
        str.append("cppf.valorPorParcelaMaterialDidatico, cppf.tipoGeracaoMaterialDidatico,  ");

        str.append("cppf.definirPlanoDescontoApresentarMatricula, cppf.lancarvalorratiadosobrevalorbasecontareceber, cppf.considerarParcelasMaterialDidaticoReajustePreco, ");
        str.append("cppf.valorPrimeiraParcela AS \"cppf.valorPrimeiraParcela\",  cppf.vencimentoPrimeiraParcelaAntesMaterialDidatico AS \"cppf.vencimentoPrimeiraParcelaAntesMaterialDidatico\",  ");
        str.append("cppf.usaValorPrimeiraParcela AS \"cppf.usaValorPrimeiraParcela\", ");
        
        str.append("unidadeEnsinoFinanceira.codigo AS \"unidadeEnsinoFinanceira.codigo\", unidadeEnsinoFinanceira.nome AS \"unidadeEnsinoFinanceira.nome\",  ");
		
        str.append(" contratoMatriculaCondicao.codigo AS \"contratoMatriculaCondicao.codigo\", contratoMatriculaCondicao.descricao AS \"contratoMatriculaCondicao.descricao\", contratoMatriculaCondicao.situacao AS \"contratoMatriculaCondicao.situacao\", ");
        str.append(" descontoPadraoCondicao.codigo AS \"descontoPadraoCondicao.codigo\", descontoPadraoCondicao.nome AS \"descontoPadraoCondicao.nome\", descontoPadraoCondicao.ativado AS \"descontoPadraoCondicao.ativado\", turma.codigo as \"turma.codigo\", turma.identificadorturma as \"turma.identificadorturma\",cppf.permiterecebimentoonlinecartaocredito AS \"cppf.permiterecebimentoonlinecartaocredito\",  ");
        
        str.append(" cppf.categoria AS \"cppf.categoria\", cppf.naoControlarValorParcela AS \"cppf.naoControlarValorParcela\",  ");
        str.append(" centroReceita.codigo AS \"centroReceita.codigo\", centroReceita.descricao AS \"centroReceita.descricao\", cppf.regerarFinanceiro  ");
        
        str.append(" FROM planoFinanceiroCurso ");
        str.append(" LEFT JOIN usuario on usuario.codigo = planofinanceirocurso.responsavelautorizacao ");
        str.append(" LEFT JOIN textopadrao contratoMatricula on contratoMatricula.codigo = planofinanceiroCurso.textoPadraoContratoMatricula ");
        str.append(" LEFT JOIN textopadrao contratoFiador on contratoFiador.codigo = planofinanceiroCurso.textoPadraoContratoFiador ");
        str.append(" LEFT JOIN textopadrao contratoExtensao on contratoExtensao.codigo = planofinanceiroCurso.textoPadraoContratoExtensao ");
        str.append(" LEFT JOIN textopadrao contratoModular on contratoModular.codigo = planofinanceiroCurso.textoPadraoContratoModular ");
        str.append(" LEFT JOIN textopadrao contratoAditivo on contratoAditivo.codigo = planofinanceiroCurso.textoPadraoContratoAditivo ");
        str.append(" LEFT JOIN condicaoPagamentoPlanoFinanceiroCurso cppf on cppf.planoFinanceiroCurso = planoFinanceiroCurso.codigo ");
        str.append(" LEFT JOIN textopadrao contratoInclusaoReposicao on contratoInclusaoReposicao.codigo = planofinanceiroCurso.textoPadraoContratoInclusaoReposicao ");
        str.append(" LEFT JOIN descontoProgressivo ON descontoProgressivo.codigo = cppf.descontoProgressivoPadrao ");
        str.append(" LEFT JOIN descontoProgressivo descontoProgressivoPrimeiraParcela ON descontoProgressivoPrimeiraParcela.codigo = cppf.descontoProgressivoPrimeiraParcela ");
        str.append(" LEFT JOIN textoPadrao contratoMatriculaCondicao ON contratoMatriculaCondicao.codigo = cppf.textoPadraoContratoMatricula ");
        str.append(" LEFT JOIN descontoProgressivo descontoPadraoCondicao ON descontoPadraoCondicao.codigo = cppf.descontoProgressivoPadraoMatricula ");
        str.append(" LEFT JOIN turma ON turma.codigo = planoFinanceiroCurso.turma ");
        str.append(" LEFT JOIN centroreceita ON centroreceita.codigo = cppf.centroreceita ");
        str.append(" LEFT JOIN unidadeensino as unidadeEnsinoFinanceira ON unidadeEnsinoFinanceira.codigo = cppf.unidadeensinofinanceira ");
        str.append(" LEFT JOIN usuario AS responsavelInativacao on responsavelInativacao.codigo = planofinanceirocurso.responsavelInativacao ");
        return str;
    }

    public List<PlanoFinanceiroCursoVO> montarDadosConsultaRapida(SqlRowSet tabelaResultado) throws Exception {
        List<PlanoFinanceiroCursoVO> vetResultado = new ArrayList<PlanoFinanceiroCursoVO>(0);
        while (tabelaResultado.next()) {
            PlanoFinanceiroCursoVO obj = new PlanoFinanceiroCursoVO();
            montarDadosBasico(obj, tabelaResultado);
            vetResultado.add(obj);
            if (tabelaResultado.getRow() == 0) {
                return vetResultado;
            }
        }
        return vetResultado;
    }

    public List<PlanoFinanceiroCursoVO> consultaRapidaPorDescricao(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, SituacaoPlanoFinanceiroCursoEnum situacao, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append("WHERE sem_acentos(lower(planoFinanceiroCurso.descricao)) like(sem_acentos('");
        sqlStr.append(valorConsulta.toLowerCase());
        sqlStr.append("%'))");
        if (unidadeEnsino != 0) {
            sqlStr.append(" AND (unidadeEnsino = ").append(unidadeEnsino).append(" OR unidadeEnsino is null) ");
        }
        if (situacao != null && !situacao.equals(SituacaoPlanoFinanceiroCursoEnum.TODAS)) {
        	sqlStr.append(" AND (planoFinanceiroCurso.situacao = '").append(situacao.toString()).append("' OR planoFinanceiroCurso.situacao is null) ");
        }
        sqlStr.append(" ORDER BY planoFinanceiroCurso.descricao");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsultaRapida(tabelaResultado);
    }

    public List<PlanoFinanceiroCursoVO> consultaRapidaPorCodigo(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append("WHERE planoFinanceiroCurso.codigo = ");
        sqlStr.append(valorConsulta.intValue());
        if (unidadeEnsino != 0) {
            sqlStr.append(" AND (unidadeEnsino = ").append(unidadeEnsino).append(" OR unidadeEnsino is null) ");
        }
        sqlStr.append(" ORDER BY planoFinanceiroCurso.descricao");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsultaRapida(tabelaResultado);
    }

    /**
     * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return PlanoFinanceiroCurso.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos.
     * Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        PlanoFinanceiroCurso.idEntidade = idEntidade;
    }
    
    @Override
    public List<PlanoFinanceiroCursoVO> consultarPlanoFinanceiroCursoFiltrarRenovacaoTurmaNivelCombobox(Integer unidadeEnsino,  Integer curso, Integer turma, Integer gradeCurricular, String ano, String semestre) throws Exception{
    	StringBuilder sql  =  new StringBuilder("select distinct planofinanceirocurso.codigo, planofinanceirocurso.descricao from PlanoFinanceiroCurso ");
    	sql.append(" inner join planofinanceiroaluno on planofinanceiroaluno.PlanoFinanceiroCurso = PlanoFinanceiroCurso.codigo ");
    	sql.append(" inner join matriculaperiodo on planofinanceiroaluno.matriculaperiodo = matriculaperiodo.codigo ");
    	sql.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");    	
    	sql.append(" where 1=1 ");
    	if(Uteis.isAtributoPreenchido(unidadeEnsino)){
    		sql.append(" and matricula.unidadeensino = ").append(unidadeEnsino);
    	}
    	if(Uteis.isAtributoPreenchido(curso)){
    		sql.append(" and matricula.curso = ").append(curso);
    	}
    	if(Uteis.isAtributoPreenchido(turma)){
    		sql.append(" and matriculaperiodo.turma = ").append(turma);
    	}
    	if(ano != null && !ano.trim().isEmpty()){ 
    		sql.append(" and matriculaperiodo.ano = '").append(ano).append("' ");
    		if(semestre != null && !semestre.trim().isEmpty()){
    			sql.append(" and matriculaperiodo.semestre = '").append(semestre).append("' ");
    		}
    	}
    	if(gradeCurricular != null && gradeCurricular > 0){
    		sql.append(" and matricula.gradeCurricularAtual = ").append(gradeCurricular);
    	}
    	sql.append(" and planofinanceirocurso.situacao = 'ATIVO' ");
    	sql.append(" order by planofinanceirocurso.descricao ");
    	SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
    	List<PlanoFinanceiroCursoVO> planoFinanceiroCursoVOs = new ArrayList<PlanoFinanceiroCursoVO>(0);
    	PlanoFinanceiroCursoVO obj = null;
    	while(rs.next()){
    		obj = new PlanoFinanceiroCursoVO();
    		obj.setCodigo(rs.getInt("codigo"));
    		obj.setDescricao(rs.getString("descricao"));
    		planoFinanceiroCursoVOs.add(obj);
    	}
    	return planoFinanceiroCursoVOs;
    	
    }
    
    @Override
    public List<CondicaoPagamentoPlanoFinanceiroCursoVO> inicializarListaSelectItemPlanoFinanceiroCursoParaTurma(Boolean realizandoNovaMatricula, Integer codigoBanner, MatriculaPeriodoVO matriculaPeriodoVO, MatriculaVO matriculaVO, UsuarioVO usuarioVO) throws Exception {
    	List<CondicaoPagamentoPlanoFinanceiroCursoVO> condicaoPagamentoPlanoFinanceiroCursoVOs = new ArrayList<CondicaoPagamentoPlanoFinanceiroCursoVO>();
    	if((realizandoNovaMatricula) && !(codigoBanner.equals(0)) && !Uteis.isAtributoPreenchido(matriculaPeriodoVO.getUnidadeEnsinoCursoVO())) {
    		matriculaPeriodoVO.setUnidadeEnsinoCursoVO(getFacadeFactory().getUnidadeEnsinoCursoFacade().consultaRapidaPorCursoUnidadeTurno(matriculaVO.getCurso().getCodigo(), matriculaVO.getUnidadeEnsino().getCodigo(), matriculaVO.getTurno().getCodigo(), usuarioVO));
    		matriculaPeriodoVO.setUnidadeEnsinoCurso(matriculaPeriodoVO.getUnidadeEnsinoCursoVO().getCodigo());
		}
		if (matriculaPeriodoVO.getUnidadeEnsinoCurso().equals(0)) {
			return condicaoPagamentoPlanoFinanceiroCursoVOs;
		}
		String planoFinanceiroDesc = matriculaPeriodoVO.getPlanoFinanceiroCurso().getDescricao();
		if (!planoFinanceiroDesc.equals("")) {
			if (planoFinanceiroDesc.length() > 20) {
				planoFinanceiroDesc = planoFinanceiroDesc.substring(0, 20);
			}
			planoFinanceiroDesc = planoFinanceiroDesc + " - ";
		}
		condicaoPagamentoPlanoFinanceiroCursoVOs = getFacadeFactory().getMatriculaPeriodoFacade().executarMontagemComboCondicaoPagamentoPlanoFinanceiroCurso(matriculaPeriodoVO.getPlanoFinanceiroCurso().getCodigo(), matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso().getCodigo(), matriculaVO, matriculaPeriodoVO, usuarioVO);
    	return condicaoPagamentoPlanoFinanceiroCursoVOs;
    }
    
    @Override
    public void realizarAtivacaoPlanoFinanceiroCurso(PlanoFinanceiroCursoVO planoFinanceiroCursoVO, UsuarioVO usuarioVO) throws Exception {
    	planoFinanceiroCursoVO.setSituacao(SituacaoPlanoFinanceiroCursoEnum.ATIVO);
    	alterarSituacaoPlanoFinanceiroCurso(SituacaoPlanoFinanceiroCursoEnum.ATIVO, planoFinanceiroCursoVO.getCodigo());
	}
    
    @Override
    public void realizarInativacaoPlanoFinanceiroCurso(PlanoFinanceiroCursoVO planoFinanceiroCursoVO, UsuarioVO usuarioVO) throws Exception {
    	planoFinanceiroCursoVO.setSituacao(SituacaoPlanoFinanceiroCursoEnum.INATIVO);
    	planoFinanceiroCursoVO.setDataInativacao(new Date());
    	planoFinanceiroCursoVO.setResponsavelInativacaoVO(usuarioVO);
    	alterarSituacaoInativandoPlanoFinanceiroCurso(usuarioVO.getCodigo(), planoFinanceiroCursoVO.getCodigo(), planoFinanceiroCursoVO.getDataInativacao());
    	for (CondicaoPagamentoPlanoFinanceiroCursoVO condicaoPlanoVO : planoFinanceiroCursoVO.getCondicaoPagamentoPlanoFinanceiroCursoVOs()) {
    		getFacadeFactory().getCondicaoPagamentoPlanoFinanceiroCursoFacade().realizarInativacaoCondicaoPagamento(condicaoPlanoVO, Boolean.FALSE, usuarioVO);
		}
	}
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarSituacaoPlanoFinanceiroCurso(SituacaoPlanoFinanceiroCursoEnum situacao, Integer planoFinanceiro) throws Exception {
        PlanoFinanceiroCurso.alterar(getIdEntidade());
        String sqlStr = "UPDATE PlanoFinanceiroCurso set situacao = ? where codigo = ?";
        getConexao().getJdbcTemplate().update(sqlStr, new Object[]{situacao.toString(), planoFinanceiro});
    }
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarSituacaoInativandoPlanoFinanceiroCurso(Integer responsavelInativacao, Integer planoFinanceiro, Date dataInativacao) throws Exception {
        PlanoFinanceiroCurso.alterar(getIdEntidade());
        String sqlStr = "UPDATE PlanoFinanceiroCurso set situacao = 'INATIVO', responsavelInativacao = ?, dataInativacao = ? where codigo = ?";
        getConexao().getJdbcTemplate().update(sqlStr, new Object[]{responsavelInativacao, dataInativacao, planoFinanceiro});
    }
    
    @Override
    public List<PlanoFinanceiroCursoVO> consultarPlanoFinanceiroTurmaEspecificaEPlanoFinanceiroAtivoUnidade(Integer turma, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("SELECT distinct PlanoFinanceiroCurso.* FROM PlanoFinanceiroCurso ");
        sqlStr.append(" LEFT JOIN turma on turma.PlanoFinanceiroCurso = PlanoFinanceiroCurso.codigo ");
        sqlStr.append(" WHERE 1=1 ");
        if (!unidadeEnsino.equals(0)) {
        	sqlStr.append(" AND (PlanoFinanceiroCurso.unidadeEnsino = ").append(unidadeEnsino);
        	sqlStr.append(" OR PlanoFinanceiroCurso.unidadeEnsino is null) ");
        } else {
        	sqlStr.append(" AND PlanoFinanceiroCurso.unidadeEnsino is null ");
        }
        sqlStr.append(" AND (PlanoFinanceiroCurso.situacao = 'ATIVO' OR turma.codigo = ").append(turma).append(") ");
        
        sqlStr.append(" ORDER BY PlanoFinanceiroCurso.descricao ");        
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, "", nivelMontarDados, usuario));
    }
    
    @Override
    public List<PlanoFinanceiroCursoVO> consultarPlanoFinanceiroUnidadeEnsinoCursoEspecificaEPlanoFinanceiroAtivoUnidade(Integer unidadeEnsinoCurso, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("SELECT distinct PlanoFinanceiroCurso.* FROM PlanoFinanceiroCurso ");
        sqlStr.append(" LEFT JOIN unidadeEnsinoCurso on unidadeEnsinoCurso.PlanoFinanceiroCurso = PlanoFinanceiroCurso.codigo ");
        sqlStr.append(" WHERE 1=1 ");
        if (!unidadeEnsino.equals(0)) {
        	sqlStr.append(" AND (PlanoFinanceiroCurso.unidadeEnsino = ").append(unidadeEnsino);
        	sqlStr.append(" OR PlanoFinanceiroCurso.unidadeEnsino is null) ");
        } else {
        	sqlStr.append(" AND PlanoFinanceiroCurso.unidadeEnsino is null ");
        }
        sqlStr.append(" AND (PlanoFinanceiroCurso.situacao = 'ATIVO' OR unidadeEnsinoCurso.codigo = ").append(unidadeEnsinoCurso).append(") ");
        
        sqlStr.append(" ORDER BY PlanoFinanceiroCurso.descricao ");        
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, "", nivelMontarDados, usuario));
    }
}
