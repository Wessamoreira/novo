package negocio.facade.jdbc.planoorcamentario;

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

import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.ComunicadoInternoDestinatarioVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TipoOrigemComunicacaoInternaEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.planoorcamentario.DetalhamentoPlanoOrcamentarioVO;
import negocio.comuns.planoorcamentario.PlanoOrcamentarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.planoorcamentario.DetalhamentoPlanoOrcamentarioInterfaceFacade;

/**
 *
 * @author Carlos
 */
@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class DetalhamentoPlanoOrcamentario extends ControleAcesso implements DetalhamentoPlanoOrcamentarioInterfaceFacade {

    private static final long serialVersionUID = 7987727084987099628L;

	private static String idEntidade;

    public DetalhamentoPlanoOrcamentario() {
        super();
        setIdEntidade("DetalhamentoPlanoOrcamentario");
    }

    @SuppressWarnings("rawtypes")
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final DetalhamentoPlanoOrcamentarioVO obj, UsuarioVO usuario) throws Exception {
        DetalhamentoPlanoOrcamentarioVO.validarDados(obj);
        final String sql = "INSERT INTO DetalhamentoPlanoOrcamentario( planoOrcamentario, departamentoSuperior, departamento, orcamentoRequeridoGestor, orcamentoTotalDepartamento, unidadeEnsino, valorConsumido ) VALUES ( ? , ?, ?, ?, ?, ?, ? ) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

        obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                if (obj.getPlanoOrcamentario().getCodigo().intValue() != 0) {
                    sqlInserir.setInt(1, obj.getPlanoOrcamentario().getCodigo().intValue());
                } else {
                    sqlInserir.setNull(1, 0);
                }
                sqlInserir.setInt(2, obj.getDepartamentoSuperior());
                if (obj.getDepartamento().getCodigo().intValue() != 0) {
                    sqlInserir.setInt(3, obj.getDepartamento().getCodigo().intValue());
                } else {
                    sqlInserir.setNull(3, 0);
                }
                sqlInserir.setDouble(4, obj.getOrcamentoRequeridoGestor());
                sqlInserir.setDouble(5, obj.getOrcamentoTotalDepartamento());
                if (obj.getUnidadeEnsinoVO().getCodigo().intValue() != 0) {
                    sqlInserir.setInt(6, obj.getUnidadeEnsinoVO().getCodigo().intValue());
                } else {
                    sqlInserir.setNull(6, 0);
                }
                sqlInserir.setDouble(7, obj.getValorConsumido());
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
        getFacadeFactory().getItemMensalDetalhamentoPlanoOrcamentarioFacade().incluirItemMensalDetalhamentoPlanoOrcamentario(obj.getCodigo(), obj.getListaItemMensalDetalhamentoPlanoOrcamentarioVOs(), usuario);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final DetalhamentoPlanoOrcamentarioVO obj, UsuarioVO usuario) throws Exception {
        DetalhamentoPlanoOrcamentarioVO.validarDados(obj);
        final String sql = "UPDATE DetalhamentoPlanoOrcamentario set planoOrcamentario=?, departamentoSuperior=?, departamento=?, orcamentoRequeridoGestor=?, orcamentoTotalDepartamento=?, unidadeEnsino=?, valorConsumido=? WHERE (codigo = ?)"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                if (obj.getPlanoOrcamentario().getCodigo().intValue() != 0) {
                    sqlAlterar.setInt(1, obj.getPlanoOrcamentario().getCodigo().intValue());
                } else {
                    sqlAlterar.setNull(1, 0);
                }
                sqlAlterar.setInt(2, obj.getDepartamentoSuperior());
                if (obj.getDepartamento().getCodigo().intValue() != 0) {
                    sqlAlterar.setInt(3, obj.getDepartamento().getCodigo().intValue());
                } else {
                    sqlAlterar.setNull(3, 0);
                }
                sqlAlterar.setDouble(4, obj.getOrcamentoRequeridoGestor());
                sqlAlterar.setDouble(5, obj.getOrcamentoTotalDepartamento());
                if (obj.getUnidadeEnsinoVO().getCodigo().intValue() != 0) {
                    sqlAlterar.setInt(6, obj.getUnidadeEnsinoVO().getCodigo().intValue());
                } else {
                    sqlAlterar.setNull(6, 0);
                }
                sqlAlterar.setDouble(7, obj.getValorConsumido());
                sqlAlterar.setInt(8, obj.getCodigo().intValue());
                return sqlAlterar;
            }
        });
        getFacadeFactory().getItemMensalDetalhamentoPlanoOrcamentarioFacade().alterarItemMensalDetalhamentoPlanoOrcamentario(obj.getCodigo(), obj.getListaItemMensalDetalhamentoPlanoOrcamentarioVOs(), usuario);
    }

    public DetalhamentoPlanoOrcamentarioVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sqlStr = "SELECT * FROM DetalhamentoPlanoOrcamentario WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados (DetalhamentoPlanoOrcamentario).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(DetalhamentoPlanoOrcamentarioVO obj, UsuarioVO usuario) throws Exception {
        ItemSolicitacaoOrcamentoPlanoOrcamentario.excluir(getIdEntidade());
        String sql = "DELETE FROM DetalhamentoPlanoOrcamentario WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
    }

    public List<DetalhamentoPlanoOrcamentarioVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM DetalhamentoPlanoOrcamentario WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List<DetalhamentoPlanoOrcamentarioVO> consultarDetalhamentoPorPlanoOrcamentario(Integer planoOrcamentario, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        List<DetalhamentoPlanoOrcamentarioVO> objetos = new ArrayList<>(0);
        String sql = "SELECT * FROM DetalhamentoPlanoOrcamentario WHERE planoOrcamentario = ?";
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{planoOrcamentario});
        while (resultado.next()) {
            objetos.add(montarDados(resultado, nivelMontarDados, usuario));
        }
        return objetos;
    }

    /**
     * Consulta os {@link DetalhamentoPlanoOrcamentarioVO} pelo codigo da Solicitação Orçamento Plano Orçamentário.
     * 
     * @param codigoSolicitacaoOrcamentoPlanoOrcamentario
     * @param nivelMontarDados
     * @param controlarAcesso
     * @param usuario
     * @return
     * @throws Exception
     */
    public List<DetalhamentoPlanoOrcamentarioVO> consultarDetalhamentoPorSolicitacaoOrcamentoPlanoOrcamentario(Integer codigoSolicitacaoOrcamentoPlanoOrcamentario, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
    	ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
    	List<DetalhamentoPlanoOrcamentarioVO> objetos = new ArrayList<>(0);
    	StringBuilder sql = new StringBuilder();
    	sql.append(" select detalhamentoplanoorcamentario.* from detalhamentoplanoorcamentario where planoorcamentario = ");
    	sql.append(" ( select planoorcamentario.codigo from planoorcamentario");
    	sql.append(" inner join solicitacaoorcamentoplanoorcamentario on solicitacaoorcamentoplanoorcamentario.planoorcamentario = planoorcamentario.codigo");
    	sql.append(" where solicitacaoorcamentoplanoorcamentario.codigo = ? limit 1 ) ");

    	SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigoSolicitacaoOrcamentoPlanoOrcamentario);
    	while (resultado.next()) {
    		objetos.add(montarDados(resultado, nivelMontarDados, usuario));
    	}
    	return objetos;
    }

    public static List<DetalhamentoPlanoOrcamentarioVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List<DetalhamentoPlanoOrcamentarioVO> vetResultado = new ArrayList<>(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        return vetResultado;
    }

    public static DetalhamentoPlanoOrcamentarioVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        DetalhamentoPlanoOrcamentarioVO obj = new DetalhamentoPlanoOrcamentarioVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.getPlanoOrcamentario().setCodigo(dadosSQL.getInt("planoOrcamentario"));
        obj.setDepartamentoSuperior(dadosSQL.getInt("departamentoSuperior"));
        obj.getDepartamento().setCodigo(dadosSQL.getInt("departamento"));
        obj.setOrcamentoRequeridoGestor(dadosSQL.getDouble("orcamentoRequeridoGestor"));
        obj.setOrcamentoTotalDepartamento(dadosSQL.getDouble("orcamentoTotalDepartamento"));
        obj.getUnidadeEnsinoVO().setCodigo(dadosSQL.getInt("unidadeEnsino"));
        obj.setValorConsumido(dadosSQL.getDouble("valorConsumido"));
        
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
            return obj;
        }
        
        montarDadosPlanoOrcamentario(obj, usuario);
        montarDadosDepartamento(obj, usuario);
        montarDadosUnidadeEnsino(obj, usuario);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        obj.setListaItemMensalDetalhamentoPlanoOrcamentarioVOs(getFacadeFactory().getItemMensalDetalhamentoPlanoOrcamentarioFacade().consultarItemMensalDetalhamentoPorDetalhamentoPlanoOrcamentario(obj.getCodigo(), false, usuario));
        obj.setListaItemSolicitacaoOrcamentoVOs(getFacadeFactory().getItemSolicitacaoOrcamentoPlanoOrcamentarioFacade().consultarItemSolicitacaoOrcamentarioPorPlanoOrcamentarioDepartamentoUnidadeEnsino(obj.getPlanoOrcamentario().getCodigo(), obj.getDepartamento().getCodigo(), obj.getUnidadeEnsinoVO().getCodigo(), false, usuario));
        return obj;
    }

    public static void montarDadosPlanoOrcamentario(DetalhamentoPlanoOrcamentarioVO obj, UsuarioVO usuario) throws Exception {
        if (obj.getPlanoOrcamentario().getCodigo().intValue() == 0) {
            obj.setPlanoOrcamentario(new PlanoOrcamentarioVO());
            return;
        }
        obj.setPlanoOrcamentario(getFacadeFactory().getPlanoOrcamentarioFacade().consultarPorChavePrimaria(obj.getPlanoOrcamentario().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
    }

    public static void montarDadosDepartamento(DetalhamentoPlanoOrcamentarioVO obj, UsuarioVO usuario) throws Exception {
        if (obj.getDepartamento().getCodigo().intValue() == 0) {
            obj.setDepartamento(new DepartamentoVO());
            return;
        }
        obj.setDepartamento(getFacadeFactory().getDepartamentoFacade().consultarPorChavePrimaria(obj.getDepartamento().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
    }

    public static void montarDadosUnidadeEnsino(DetalhamentoPlanoOrcamentarioVO obj, UsuarioVO usuario) throws Exception {
        if (obj.getUnidadeEnsinoVO().getCodigo().intValue() == 0) {
            obj.setUnidadeEnsinoVO(new UnidadeEnsinoVO());
            return;
        }
        obj.setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
    }

    public static String getIdEntidade() {
        return idEntidade;
    }

    public static void setIdEntidade(String aIdEntidade) {
        idEntidade = aIdEntidade;
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirDetalhamentoPlanoOrcamentario(Integer planoOrcamentarioPrm, List<DetalhamentoPlanoOrcamentarioVO> objetos, UsuarioVO usuario) throws Exception {
        Iterator<DetalhamentoPlanoOrcamentarioVO> e = objetos.iterator();
        while (e.hasNext()) {
            DetalhamentoPlanoOrcamentarioVO obj = (DetalhamentoPlanoOrcamentarioVO) e.next();
            obj.getPlanoOrcamentario().setCodigo(planoOrcamentarioPrm);
            incluir(obj, usuario);
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarDetalhamentoPlanoOrcamentario(Integer planoOrcamentario, List objetos, UsuarioVO usuario) throws Exception {
        excluirDetalhamentoPlanoOrcamentario(planoOrcamentario, usuario);
        incluirDetalhamentoPlanoOrcamentario(planoOrcamentario, objetos, usuario);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirDetalhamentoPlanoOrcamentario(Integer planoOrcamentario, UsuarioVO usuario) throws Exception {
        DetalhamentoPlanoOrcamentario.excluir(getIdEntidade());
        String sql = "DELETE FROM DetalhamentoPlanoOrcamentario WHERE (planoOrcamentario = ?)"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(sql, new Object[]{planoOrcamentario});
    }

    public void validarDadosValorOrcamentoTotal(Double valorOrcamentoTotal) throws Exception {
        if (valorOrcamentoTotal == null || valorOrcamentoTotal == 0.0) {
            throw new Exception("O campo ORÇAMENTO APROVADO deve ser informado.");
        }
    }
    

    public Double consultarValorOcamentoTotalDetapartamentoPorDataDepartamentoUnidadeEnsino(Date data, Integer departamento, Integer unidadeEnsino) {
        StringBuilder sb = new StringBuilder();
        sb.append("select detalhamentoPlano.orcamentototaldepartamento from planoorcamentario ");
        sb.append(" inner join detalhamentoplanoorcamentario detalhamentoPlano on detalhamentoPlano.planoOrcamentario = planoOrcamentario.codigo ");
        sb.append(" where '");
        sb.append(data);
        sb.append("' >= dataInicio and '");
        sb.append(data);
        sb.append("' <= dataFinal ");
        sb.append(" and detalhamentoPlano.departamento = ");
        sb.append(departamento);
        sb.append(" and detalhamentoPlano.unidadeEnsino = ");
        sb.append(unidadeEnsino);
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        if (tabelaResultado.next()) {
            return tabelaResultado.getDouble("orcamentototaldepartamento");
        }
        return 0.0;
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void atualizarValoConsumidoDetalhamentoPlanoOrcamentario(Double valorAtualizar, Integer departamento, Integer unidadeEnsino, Date dataAutorizacao, UsuarioVO usuario) throws Exception {
        DetalhamentoPlanoOrcamentarioVO detalhamentoPlanoOrcamentarioVO = consultarPorPlanoOrcamentarioDepartamentoUnidadeEnsino(departamento, unidadeEnsino, dataAutorizacao, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
        if (Uteis.isAtributoPreenchido(detalhamentoPlanoOrcamentarioVO)) {
            Double valorTotal = valorAtualizar + detalhamentoPlanoOrcamentarioVO.getValorConsumido();
            alterarValorConsumido(valorTotal, detalhamentoPlanoOrcamentarioVO);
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void atualizarEstornoValoConsumidoDetalhamentoPlanoOrcamentario(Double valorAtualizar, Integer departamento, Integer unidadeEnsino, Date dataAutorizacao, UsuarioVO usuario) throws Exception {
    	DetalhamentoPlanoOrcamentarioVO detalhamentoPlanoOrcamentarioVO = consultarPorPlanoOrcamentarioDepartamentoUnidadeEnsino(departamento, unidadeEnsino, dataAutorizacao, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
    	if (Uteis.isAtributoPreenchido(detalhamentoPlanoOrcamentarioVO)) {
    		Double valorTotal = detalhamentoPlanoOrcamentarioVO.getValorConsumido() - valorAtualizar;
    		alterarValorConsumido(valorTotal, detalhamentoPlanoOrcamentarioVO);
    	}
    }

    public DetalhamentoPlanoOrcamentarioVO consultarPorPlanoOrcamentarioDepartamentoUnidadeEnsino(Integer departamento, Integer unidadeensino, Date dataAutorizacao, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("select detalhamentoplanoorcamentario.* from detalhamentoplanoorcamentario ");
        sb.append(" inner join planoorcamentario on planoorcamentario.codigo = detalhamentoplanoorcamentario.planoorcamentario ");
        sb.append(" where 1 = 1 ");
        if (Uteis.isAtributoPreenchido(departamento)) {
        	sb.append(" and departamento = ");
        	sb.append(departamento);
        }
        sb.append(" and unidadeensino = ");
        sb.append(unidadeensino);
        
        if (Uteis.isAtributoPreenchido(dataAutorizacao)) {
	        sb.append(" and '");
	        sb.append(dataAutorizacao);
	        sb.append("' >= planoorcamentario.datainicio and '");
	        sb.append(dataAutorizacao);
	        sb.append("' <= planoorcamentario.datafinal ");
        }
        sb.append(" and situacao = 'AT'");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        if (!tabelaResultado.next()) {
            return null;
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));

    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Double consultarValorConsumidoQuantidadeAutorizadaPorDepartamentoUnidadeEnsino(Integer departamento, Integer unidadeEnsino, Date dataAutorizacao) {
        StringBuilder sb = new StringBuilder();
        sb.append("select SUM(totaldepartamento) as totaldepartamento from (");
        sb.append("select SUM(requisicaoitem.quantidadeautorizada) * produtoservico.valorUltimacompra AS totaldepartamento from requisicao ");
        sb.append(" inner join requisicaoitem on requisicaoitem.requisicao = requisicao.codigo ");
        sb.append(" inner join produtoservico on produtoservico.codigo = requisicaoitem.produtoservico ");
        sb.append(" inner join detalhamentoplanoorcamentario detalhamento on detalhamento.departamento = requisicao.departamento and detalhamento.unidadeensino = requisicao.unidadeensino ");
        sb.append(" inner join planoorcamentario on planoorcamentario.codigo = detalhamento.planoorcamentario ");
        sb.append(" where requisicaoitem.quantidadeautorizada > 0 ");
        sb.append(" and requisicao.departamento = ");
        sb.append(departamento);
        sb.append(" and requisicao.unidadeensino = ");
        sb.append(unidadeEnsino);
        sb.append(" and '");
        sb.append(dataAutorizacao);
        sb.append("' >= planoorcamentario.datainicio and '");
        sb.append(dataAutorizacao);
        sb.append("' <= planoorcamentario.datafinal ");
        sb.append(" group by produtoservico.valorUltimacompra ");
        sb.append(") as t");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        if (tabelaResultado.next()) {
            return tabelaResultado.getDouble("totaldepartamento");
        }
        return 0.0;
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarValorConsumido(final Double valorConsumido, final Integer departamento, final Integer unidadeEnsino, Date dataAutorizacao) throws Exception {

        final StringBuilder sql = new StringBuilder("update detalhamentoplanoorcamentario  set valorConsumido = ? from planoorcamentario ");
        sql.append(" where planoorcamentario.codigo = detalhamentoplanoorcamentario.planoorcamentario ");
        sql.append(" and detalhamentoplanoorcamentario.departamento = ");
        sql.append(departamento);
        sql.append(" and detalhamentoplanoorcamentario.unidadeensino = ");
        sql.append(unidadeEnsino);
        if (Uteis.isAtributoPreenchido(dataAutorizacao)) {
	        sql.append(" and '");
	        sql.append(dataAutorizacao);
	        sql.append("' >= planoorcamentario.datainicio and '");
	        sql.append(dataAutorizacao);
	        sql.append(" ' <= planoorcamentario.datafinal");
        }

        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
                sqlAlterar.setDouble(1, valorConsumido);
                return sqlAlterar;
            }
        });
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarValorConsumido(final Double valorConsumido, DetalhamentoPlanoOrcamentarioVO detalhamentoPlanoOrcamentarioVO) throws Exception {

    	final StringBuilder sql = new StringBuilder("update detalhamentoplanoorcamentario  set valorConsumido = ? where codigo = ? ");

    	getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

    		public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
    			PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
    			sqlAlterar.setDouble(1, valorConsumido);
    			sqlAlterar.setInt(2, detalhamentoPlanoOrcamentarioVO.getCodigo());
    			return sqlAlterar;
    		}
    	});
    }

    public DetalhamentoPlanoOrcamentarioVO consultarDetalhamentoPlanoOrcamentarioPorPlanoOrcamentarioDepartamentoUnidadeEnsino(Integer planoOrcamentario, Integer departamento, Integer unidadeEnsino, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("select * from detalhamentoplanoorcamentario where planoorcamentario = ");
        sb.append(planoOrcamentario);
        if (Uteis.isAtributoPreenchido(departamento)) {
        	sb.append(" and departamento = ").append(departamento);
        }
        sb.append(" and unidadeensino = ");
        sb.append(unidadeEnsino);
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        if (!tabelaResultado.next()) {
            return null;
        }
        return montarDados(tabelaResultado, nivelMontarDados, usuario);
    }

	public List<DepartamentoVO> consultarDepartantoPorPlanoOrcamentario(List<PlanoOrcamentarioVO> planoOrcamentariosVO, UsuarioVO usuario) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select distinct departamento.codigo, departamento.nome from detalhamentoplanoorcamentario");
		sql.append(" inner join departamento on departamento.codigo = departamento");
		String[] arrayString = new String[planoOrcamentariosVO.size()];
		int contador = 0;
		for (PlanoOrcamentarioVO obj : planoOrcamentariosVO) {
			arrayString[contador] = obj.getCodigo().toString();
			contador++;
		}
		sql.append(" where planoorcamentario  ").append(realizarGeracaoInValor(planoOrcamentariosVO.size(), arrayString));

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<DepartamentoVO> lista = new ArrayList<>();
		while (tabelaResultado.next()) {
			DepartamentoVO obj = new DepartamentoVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setNome(tabelaResultado.getString("nome"));
			lista.add(obj);
		}
		return lista;
	}

    public Boolean consultarExistenciaPorPlanoOrcamentario(Integer planoOrcamentario) {
        StringBuilder sb = new StringBuilder();
        sb.append("select * from detalhamentoplanoorcamentario where planoOrcamentario = ?");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), planoOrcamentario);
        if (tabelaResultado.next()) {
            return true;
        }
        return false;
    }

}
