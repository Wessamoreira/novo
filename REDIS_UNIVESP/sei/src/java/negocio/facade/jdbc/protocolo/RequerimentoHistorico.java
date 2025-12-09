package negocio.facade.jdbc.protocolo;

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

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.avaliacaoinst.RespostaAvaliacaoInstitucionalDWVO;
import negocio.comuns.basico.enumeradores.ConsiderarFeriadoEnum;
import negocio.comuns.protocolo.RequerimentoHistoricoVO;
import negocio.comuns.protocolo.RequerimentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.protocolo.RequerimentoHistoricoInterfaceFacade;


@Repository
@Scope("singleton")
@Lazy
public class RequerimentoHistorico extends ControleAcesso implements RequerimentoHistoricoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2983064627108099535L;
	protected static String idEntidade;

	public RequerimentoHistorico() throws Exception {
		super();
		setIdEntidade("Requerimento");
	}

	public RequerimentoHistoricoVO novo() throws Exception {
		RequerimentoHistorico.incluir(getIdEntidade());
		RequerimentoHistoricoVO obj = new RequerimentoHistoricoVO();
		return obj;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final RequerimentoHistoricoVO obj, UsuarioVO usuario) throws Exception {
		RequerimentoHistoricoVO.validarDados(obj);
		final String sql = "INSERT INTO RequerimentoHistorico( " + "requerimento, dataEntradaDepartamento, dataConclusaoDepartamento, observacaoDepartamento, " + "departamento, responsavelRequerimentoDepartamento, dptoResposanvelPeloIndeferimento, " + "dataInicioExecucaoDepartamento, questionario, ordemExecucaoTramite, retorno,  " + "motivoRetorno, enviouDepartamentoAnterior, departamentoAnterior, ordemExecucaoTramiteAnterior, situacaoRequerimentoDepartamento) " + "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo";
		obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlInserir = arg0.prepareStatement(sql);
				int i = 1;
				sqlInserir.setInt(i++, obj.getRequerimento().intValue());
				if (obj.getDataEntradaDepartamento() != null) {
					sqlInserir.setTimestamp(i++, Uteis.getDataJDBCTimestamp(obj.getDataEntradaDepartamento()));
				} else {
					sqlInserir.setNull(i++, 0);
				}
				if (obj.getDataConclusaoDepartamento() != null) {
					sqlInserir.setTimestamp(i++, Uteis.getDataJDBCTimestamp(obj.getDataConclusaoDepartamento()));
				} else {
					sqlInserir.setNull(i++, 0);
				}
				sqlInserir.setString(i++, obj.getObservacaoDepartamento().toString());
				if (!obj.getDepartamento().getCodigo().equals(0)) {
					sqlInserir.setInt(i++, obj.getDepartamento().getCodigo().intValue());
				} else {
					sqlInserir.setNull(i++, 0);
				}
				if (!obj.getResponsavelRequerimentoDepartamento().getCodigo().equals(0)) {
					sqlInserir.setInt(i++, obj.getResponsavelRequerimentoDepartamento().getCodigo().intValue());
				} else {
					sqlInserir.setNull(i++, 0);
				}
				sqlInserir.setBoolean(i++, obj.getDptoResposanvelPeloIndeferimento());
				if (obj.getDataInicioExecucaoDepartamento() != null) {
					sqlInserir.setTimestamp(i++, Uteis.getDataJDBCTimestamp(obj.getDataInicioExecucaoDepartamento()));
				} else {
					sqlInserir.setNull(i++, 0);
				}
				if (obj.getQuestionario().getCodigo() != null && obj.getQuestionario().getCodigo() > 0) {
					sqlInserir.setInt(i++, obj.getQuestionario().getCodigo());
				} else {
					sqlInserir.setNull(i++, 0);
				}
				sqlInserir.setInt(i++, obj.getOrdemExecucaoTramite());
				sqlInserir.setBoolean(i++, obj.getRetorno());
				sqlInserir.setString(i++, obj.getMotivoRetorno());
				sqlInserir.setBoolean(i++, obj.getEnviouDepartamentoAnterior());
				if (obj.getDepartamentoAnterior().getCodigo() > 0) {
					sqlInserir.setInt(i++, obj.getDepartamentoAnterior().getCodigo());
				} else {
					sqlInserir.setNull(i++, 0);
				}
				sqlInserir.setInt(i++, obj.getOrdemExecucaoTramiteAnterior());
				if(Uteis.isAtributoPreenchido(obj.getSituacaoRequerimentoDepartamentoVO())){
					sqlInserir.setInt(i++, obj.getSituacaoRequerimentoDepartamentoVO().getCodigo());
				}else {
					sqlInserir.setNull(i++, 0);
				}
				return sqlInserir;
			}
		}, new ResultSetExtractor<Integer>() {
			public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
				if (arg0.next()) {
					obj.setNovoObj(Boolean.FALSE);
					return arg0.getInt("codigo");
				}
				return null;
			}
		}));
		obj.setNovoObj(Boolean.FALSE);
		gravarRespostaQuestionario(obj, usuario);
	}

	@Override
	public void gravarRespostaQuestionario(RequerimentoHistoricoVO obj, UsuarioVO usuario) throws Exception {
		List<RespostaAvaliacaoInstitucionalDWVO> listaRespostaQuestionario = null;
		if (obj.getQuestionario().getCodigo() > 0 && obj.getGravarRespostaQuestionario()) {
			listaRespostaQuestionario = getFacadeFactory().getRespostaAvaliacaoInstitucionalDWFacade().gerarRespostaQuestionarioRequerimentoHistorico(obj);
			for (RespostaAvaliacaoInstitucionalDWVO objResp : listaRespostaQuestionario) {
				RespostaAvaliacaoInstitucionalDWVO.validarDados(objResp);
				getFacadeFactory().getRespostaAvaliacaoInstitucionalDWFacade().alterarPorRequerimentoHistorico(objResp, usuario);
			}

		}
		obj.setQuestionarioJaRespondido(true);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final RequerimentoHistoricoVO obj, UsuarioVO usuario) throws Exception {
		RequerimentoHistoricoVO.validarDados(obj);
		final String sql = "UPDATE RequerimentoHistorico SET " + " requerimento=?, dataEntradaDepartamento=?, dataConclusaoDepartamento=?, " + " observacaoDepartamento=?, departamento=?, responsavelRequerimentoDepartamento=?, " + " dptoResposanvelPeloIndeferimento=?, dataInicioExecucaoDepartamento=?, questionario = ?, ordemExecucaoTramite = ?, " + " retorno = ?, motivoRetorno = ?, enviouDepartamentoAnterior = ?, departamentoAnterior = ?, ordemExecucaoTramiteAnterior = ?, situacaoRequerimentoDepartamento = ?, logAlteracaoSituacao = ?, notaTCC = ? " + " WHERE ((codigo = ?))";
		if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				int i = 1;
				sqlAlterar.setInt(i++, obj.getRequerimento().intValue());
				if (obj.getDataEntradaDepartamento() != null) {
					sqlAlterar.setTimestamp(i++, Uteis.getDataJDBCTimestamp(obj.getDataEntradaDepartamento()));
				} else {
					sqlAlterar.setNull(i++, 0);
				}
				if (obj.getDataConclusaoDepartamento() != null) {
					sqlAlterar.setTimestamp(i++, Uteis.getDataJDBCTimestamp(obj.getDataConclusaoDepartamento()));
				} else {
					sqlAlterar.setNull(i++, 0);
				}
				sqlAlterar.setString(i++, obj.getObservacaoDepartamento().toString());
				if (!obj.getDepartamento().getCodigo().equals(0)) {
					sqlAlterar.setInt(i++, obj.getDepartamento().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(i++, 0);
				}
				if (!obj.getResponsavelRequerimentoDepartamento().getCodigo().equals(0)) {
					sqlAlterar.setInt(i++, obj.getResponsavelRequerimentoDepartamento().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(i++, 0);
				}
				sqlAlterar.setBoolean(i++, obj.getDptoResposanvelPeloIndeferimento());
				if (obj.getDataInicioExecucaoDepartamento() != null) {
					sqlAlterar.setTimestamp(i++, Uteis.getDataJDBCTimestamp(obj.getDataInicioExecucaoDepartamento()));
				} else {
					sqlAlterar.setNull(i++, 0);
				}
				if (obj.getQuestionario().getCodigo() != null && obj.getQuestionario().getCodigo() > 0) {
					sqlAlterar.setInt(i++, obj.getQuestionario().getCodigo());
				} else {
					sqlAlterar.setNull(i++, 0);
				}
				sqlAlterar.setInt(i++, obj.getOrdemExecucaoTramite());				
				sqlAlterar.setBoolean(i++, obj.getRetorno());
				sqlAlterar.setString(i++, obj.getMotivoRetorno());
				sqlAlterar.setBoolean(i++, obj.getEnviouDepartamentoAnterior());
				if (obj.getDepartamentoAnterior().getCodigo() > 0) {
					sqlAlterar.setInt(i++, obj.getDepartamentoAnterior().getCodigo());
				} else {
					sqlAlterar.setNull(i++, 0);
				}
				sqlAlterar.setInt(i++, obj.getOrdemExecucaoTramiteAnterior());
				if(Uteis.isAtributoPreenchido(obj.getSituacaoRequerimentoDepartamentoVO())){
					sqlAlterar.setInt(i++, obj.getSituacaoRequerimentoDepartamentoVO().getCodigo());
				}else {
					sqlAlterar.setNull(i++, 0);
				}
				sqlAlterar.setString(i++, obj.getLogAlteracaoSituacao());
				
				if(Uteis.isAtributoPreenchido(obj.getNotaTCC())) {
					Uteis.setValuePreparedStatement(obj.getNotaTCC(), i++, sqlAlterar);
				}else {
					sqlAlterar.setNull(i++, 0);
				}
				
				sqlAlterar.setInt(i++, obj.getCodigo().intValue());
				return sqlAlterar;
			}
		}) == 0) {
			incluir(obj, usuario);
			return;
		}
		;
		gravarRespostaQuestionario(obj, usuario);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(RequerimentoHistoricoVO obj, UsuarioVO usuario) throws Exception {
		RequerimentoHistorico.excluir(getIdEntidade());
		String sql = "DELETE FROM RequerimentoHistorico WHERE ((codigo = ?))";
		getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
	}

	public StringBuilder getSqlCompleto() {
		StringBuilder sql = new StringBuilder("SELECT RequerimentoHistorico.*, ");
		sql.append("Departamento.nome AS \"departamento.nome\", Departamento.responsavel AS \"departamento.responsavel\", ");
		sql.append("DepartamentoAnterior.nome AS \"DepartamentoAnterior.nome\", ");
		sql.append("Pessoa.nome AS \"responsavelRequerimentoDepartamento.nome\", ");
		sql.append("situacaoRequerimentoDepartamento.situacao AS \"situacaoRequerimentoDepartamento.situacao\", ");
		sql.append("tipoRequerimentoDepartamento.podeInserirNota AS \"tipoRequerimentoDepartamento.podeInserirNota\" ");
		sql.append("FROM RequerimentoHistorico ");
		sql.append("INNER JOIN Requerimento ON (Requerimento.codigo = RequerimentoHistorico.requerimento) ");
		sql.append("inner join tiporequerimento on tiporequerimento.codigo = Requerimento.tiporequerimento ");
		sql.append("inner join tiporequerimentodepartamento on tiporequerimentodepartamento.tiporequerimento = tiporequerimento.codigo and requerimentohistorico.departamento = tiporequerimentodepartamento.departamento ");
		sql.append("and tiporequerimentodepartamento.ordemexecucao = RequerimentoHistorico.ordemexecucaotramite ");
		sql.append("LEFT JOIN Departamento ON (Departamento.codigo = RequerimentoHistorico.departamento) ");
		sql.append("LEFT JOIN Departamento as DepartamentoAnterior ON (DepartamentoAnterior.codigo = RequerimentoHistorico.departamentoAnterior) ");
		sql.append("LEFT JOIN Pessoa ON (Pessoa.codigo = RequerimentoHistorico.responsavelRequerimentoDepartamento) ");
		sql.append("LEFT JOIN situacaoRequerimentoDepartamento ON (situacaoRequerimentoDepartamento.codigo = RequerimentoHistorico.situacaoRequerimentoDepartamento) ");
		return sql;
	}
	
	public StringBuilder getSqlFiltrarPorUlitmoHitoricoDepartamento() {
		StringBuilder sql = new StringBuilder("SELECT RequerimentoHistorico.*, ");
		sql.append("Departamento.nome AS \"departamento.nome\", Departamento.responsavel AS \"departamento.responsavel\", ");
		sql.append("DepartamentoAnterior.nome AS \"DepartamentoAnterior.nome\", ");
		sql.append("Pessoa.nome AS \"responsavelRequerimentoDepartamento.nome\", ");
		sql.append("situacaoRequerimentoDepartamento.situacao AS \"situacaoRequerimentoDepartamento.situacao\", ");
		sql.append("tipoRequerimentoDepartamento.prazoexecucao AS \"tipoRequerimentoDepartamento.prazoexecucao\" ");
		sql.append("FROM RequerimentoHistorico ");
		sql.append("INNER JOIN Requerimento ON (Requerimento.codigo = RequerimentoHistorico.requerimento) ");
			sql.append("and RequerimentoHistorico.codigo = (select reqhistorico.codigo from RequerimentoHistorico reqhistorico ");
			sql.append("where reqhistorico.requerimento = requerimento.codigo and reqhistorico.departamento = RequerimentoHistorico.departamento ");
			sql.append("order by reqhistorico.dataconclusaodepartamento desc limit 1 ) ");
		sql.append("inner join tiporequerimento on tiporequerimento.codigo = Requerimento.tiporequerimento ");
		sql.append("inner join tiporequerimentodepartamento on tiporequerimentodepartamento.tiporequerimento = tiporequerimento.codigo and requerimentohistorico.departamento = tiporequerimentodepartamento.departamento ");
		sql.append("LEFT JOIN Departamento ON (Departamento.codigo = RequerimentoHistorico.departamento) ");
		sql.append("LEFT JOIN Departamento as DepartamentoAnterior ON (DepartamentoAnterior.codigo = RequerimentoHistorico.departamentoAnterior) ");
		sql.append("LEFT JOIN Pessoa ON (Pessoa.codigo = RequerimentoHistorico.responsavelRequerimentoDepartamento) ");
		sql.append("LEFT JOIN situacaoRequerimentoDepartamento ON (situacaoRequerimentoDepartamento.codigo = RequerimentoHistorico.situacaoRequerimentoDepartamento) ");
		return sql;
	}

	public List<RequerimentoHistoricoVO> consultarPorCodigoRequerimento(Integer codigoRequerimento, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getSqlCompleto();
		sqlStr.append(" WHERE RequerimentoHistorico.requerimento = ? ORDER BY codigo");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), codigoRequerimento);
		return montarDadosConsulta(tabelaResultado, usuario);
	}
	
	public List<RequerimentoHistoricoVO> consultarPorCodigoRequerimentoFiltrarPorUltimoHistoricoDepartamento(Integer codigoRequerimento, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getSqlFiltrarPorUlitmoHitoricoDepartamento();
		sqlStr.append(" WHERE RequerimentoHistorico.requerimento = ? order by RequerimentoHistorico.dataconclusaodepartamento");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), codigoRequerimento);
		List<RequerimentoHistoricoVO> vetResultado = new ArrayList<RequerimentoHistoricoVO>(0);
		while(tabelaResultado.next()){
			vetResultado.add(montarDados(tabelaResultado, usuario.getUnidadeEnsinoLogado().getCidade().getCodigo()));
		}
		return vetResultado;
	}

	public RequerimentoHistoricoVO consultarPorChavePrimaria(Integer codigoPrm, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		StringBuilder sqlStr = getSqlCompleto();
		sqlStr.append(" WHERE (RequerimentoHistorico.codigo = " + codigoPrm + ") ORDER BY codigo");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), codigoPrm);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados (Tipo Requerimento Departamento).");
		}
		return (montarDados(tabelaResultado, usuario));
	}

	public static List<RequerimentoHistoricoVO> montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
		List<RequerimentoHistoricoVO> vetResultado = new ArrayList<RequerimentoHistoricoVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, usuario));
		}
		return vetResultado;
	}

	public static RequerimentoHistoricoVO montarDados(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
		RequerimentoHistoricoVO obj = new RequerimentoHistoricoVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setRequerimento(new Integer(dadosSQL.getInt("requerimento")));
		obj.setDataEntradaDepartamento(dadosSQL.getTimestamp("dataEntradaDepartamento"));
		obj.setDataInicioExecucaoDepartamento(dadosSQL.getTimestamp("dataInicioExecucaoDepartamento"));
		obj.setDataConclusaoDepartamento(dadosSQL.getTimestamp("dataConclusaoDepartamento"));
		obj.setObservacaoDepartamento(dadosSQL.getString("observacaoDepartamento"));
		obj.getDepartamento().setCodigo(new Integer(dadosSQL.getInt("departamento")));
		obj.getDepartamento().setNome(dadosSQL.getString("departamento.nome"));
		obj.getDepartamento().getResponsavel().setCodigo(new Integer(dadosSQL.getInt("departamento.responsavel")));
		obj.getResponsavelRequerimentoDepartamento().setCodigo(new Integer(dadosSQL.getInt("responsavelRequerimentoDepartamento")));
		obj.getResponsavelRequerimentoDepartamento().setNome(dadosSQL.getString("responsavelRequerimentoDepartamento.nome"));
		obj.setDptoResposanvelPeloIndeferimento(dadosSQL.getBoolean("dptoResposanvelPeloIndeferimento"));
		obj.getQuestionario().setCodigo(dadosSQL.getInt("questionario"));
		obj.setOrdemExecucaoTramite(dadosSQL.getInt("ordemExecucaoTramite"));
		obj.setOrdemExecucaoTramiteAnterior(dadosSQL.getInt("ordemExecucaoTramiteAnterior"));
		obj.getDepartamentoAnterior().setCodigo(dadosSQL.getInt("departamentoAnterior"));
		obj.getDepartamentoAnterior().setNome(dadosSQL.getString("departamentoAnterior.nome"));
		obj.setRetorno(dadosSQL.getBoolean("retorno"));
		obj.setMotivoRetorno(dadosSQL.getString("motivoretorno"));
		obj.setEnviouDepartamentoAnterior(dadosSQL.getBoolean("enviouDepartamentoAnterior"));
		
		obj.getSituacaoRequerimentoDepartamentoVO().setCodigo(dadosSQL.getInt("situacaoRequerimentoDepartamento"));
		obj.getSituacaoRequerimentoDepartamentoVO().setSituacao(dadosSQL.getString("situacaoRequerimentoDepartamento.situacao"));
		obj.setLogAlteracaoSituacao(dadosSQL.getString("logAlteracaoSituacao"));
		
		if(dadosSQL.getObject("notaTCC") == null){
			obj.setNotaTCC((Double) dadosSQL.getObject("notaTCC"));
		}else {
			obj.setNotaTCC(dadosSQL.getDouble("notaTCC"));
		}
		obj.setPodeInserirNota(dadosSQL.getBoolean("tipoRequerimentoDepartamento.podeInserirNota"));
		obj.setNovoObj(Boolean.FALSE);
		obj.setMaterialRequerimentoVOs(getFacadeFactory().getMaterialRequerimentoFacade().consultarPorRequerimentoHistorico(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
		obj.setInteracaoRequerimentoHistoricoVOs(getFacadeFactory().getInteracaoRequerimentoHistoricoFacade().consultarPorRequerimentoHistorico(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, usuario));
		return obj;
	}
	
	
	private static RequerimentoHistoricoVO montarDados(SqlRowSet dadosSQL, Integer codCidade) throws Exception {
		RequerimentoHistoricoVO obj = new RequerimentoHistoricoVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setRequerimento(new Integer(dadosSQL.getInt("requerimento")));
		obj.setDataEntradaDepartamento(dadosSQL.getTimestamp("dataEntradaDepartamento"));
		obj.setDataInicioExecucaoDepartamento(dadosSQL.getTimestamp("dataInicioExecucaoDepartamento"));
		obj.setDataConclusaoDepartamento(dadosSQL.getTimestamp("dataConclusaoDepartamento"));
		obj.setObservacaoDepartamento(dadosSQL.getString("observacaoDepartamento"));
		obj.getDepartamento().setCodigo(new Integer(dadosSQL.getInt("departamento")));
		obj.getDepartamento().setNome(dadosSQL.getString("departamento.nome"));
		obj.getDepartamento().getResponsavel().setCodigo(new Integer(dadosSQL.getInt("departamento.responsavel")));
		obj.getResponsavelRequerimentoDepartamento().setCodigo(new Integer(dadosSQL.getInt("responsavelRequerimentoDepartamento")));
		obj.getResponsavelRequerimentoDepartamento().setNome(dadosSQL.getString("responsavelRequerimentoDepartamento.nome"));
		obj.setDptoResposanvelPeloIndeferimento(dadosSQL.getBoolean("dptoResposanvelPeloIndeferimento"));
		obj.getQuestionario().setCodigo(dadosSQL.getInt("questionario"));
		obj.setOrdemExecucaoTramite(dadosSQL.getInt("ordemExecucaoTramite"));
		obj.setOrdemExecucaoTramiteAnterior(dadosSQL.getInt("ordemExecucaoTramiteAnterior"));
		obj.getDepartamentoAnterior().setCodigo(dadosSQL.getInt("departamentoAnterior"));
		obj.getDepartamentoAnterior().setNome(dadosSQL.getString("departamentoAnterior.nome"));
		obj.setRetorno(dadosSQL.getBoolean("retorno"));
		obj.setMotivoRetorno(dadosSQL.getString("motivoretorno"));
		obj.setEnviouDepartamentoAnterior(dadosSQL.getBoolean("enviouDepartamentoAnterior"));
		obj.getSituacaoRequerimentoDepartamentoVO().setCodigo(dadosSQL.getInt("situacaoRequerimentoDepartamento"));
		obj.getSituacaoRequerimentoDepartamentoVO().setSituacao(dadosSQL.getString("situacaoRequerimentoDepartamento.situacao"));
		obj.setLogAlteracaoSituacao(dadosSQL.getString("logAlteracaoSituacao"));
		
		if(dadosSQL.getObject("notaTCC") == null){
			obj.setNotaTCC((Double) dadosSQL.getObject("notaTCC"));
		}else {
			obj.setNotaTCC(dadosSQL.getDouble("notaTCC"));
		}
		obj.setPrazoExecucao(dadosSQL.getInt("tipoRequerimentoDepartamento.prazoexecucao"));
		obj.setPrevisaoDevolucao(getFacadeFactory().getFeriadoFacade().obterDataFuturaOuRetroativaApenasDiasUteis(obj.getDataEntradaDepartamento(), obj.getPrazoExecucao(), codCidade, false, false, ConsiderarFeriadoEnum.FINANCEIRO));
		obj.setNovoObj(Boolean.FALSE);
		return obj;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirRequerimentoHistoricoVOs(Integer requerimento, List<RequerimentoHistoricoVO> requerimentoHistoricoVOs, UsuarioVO usuario) throws Exception {
		RequerimentoHistorico.excluir(getIdEntidade());
		String sql = "DELETE FROM RequerimentoHistorico WHERE (requerimento = ?) and codigo not in (0";
		for (RequerimentoHistoricoVO requerimentoHistoricoVO : requerimentoHistoricoVOs) {
			sql += ", " + requerimentoHistoricoVO.getCodigo();
		}
		sql += ") ";
		getConexao().getJdbcTemplate().update(sql, requerimento);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarRequerimentoHistoricoVOs(RequerimentoVO requerimento, List<RequerimentoHistoricoVO> requerimentoHistoricoVOs, UsuarioVO usuario) throws Exception {
		excluirRequerimentoHistoricoVOs(requerimento.getCodigo(), requerimentoHistoricoVOs, usuario);
		RequerimentoHistoricoVO ultimoObjExistente = requerimentoHistoricoVOs.get(requerimentoHistoricoVOs.size() - 1);
		
		for (RequerimentoHistoricoVO requerimentoHistoricoVO : requerimentoHistoricoVOs) {
			requerimentoHistoricoVO.setRequerimento(requerimento.getCodigo());
			if (requerimentoHistoricoVO.isNovoObj()) {
				incluir(requerimentoHistoricoVO, usuario);
			} else if (!ultimoObjExistente.getDepartamento().getCodigo().equals(requerimento.getDepartamentoResponsavel().getCodigo())){
				throw new ConsistirException("O último histórico de trâmite do departamento está registrado para um departamento diferente do departamento atual do Requerimento.");
			}else {
				alterar(requerimentoHistoricoVO, usuario);
			}
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirRequerimentoHistoricoVOs(Integer requerimento, List<RequerimentoHistoricoVO> requerimentoHistoricoVOs, UsuarioVO usuario) throws Exception {
		Iterator<RequerimentoHistoricoVO> e = requerimentoHistoricoVOs.iterator();
		while (e.hasNext()) {
			RequerimentoHistoricoVO obj = (RequerimentoHistoricoVO) e.next();
			obj.setRequerimento(requerimento);
			incluir(obj, usuario);
		}
	}

	public static String getIdEntidade() {
		return RequerimentoHistorico.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		RequerimentoHistorico.idEntidade = idEntidade;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSituacaoDepartamento(RequerimentoHistoricoVO requerimentoHistoricoVO, RequerimentoVO requerimentoVO, UsuarioVO  usuarioVO) throws Exception{
		getFacadeFactory().getRequerimentoFacade().alterarSituacaoDepartamento(requerimentoVO.getCodigo(), requerimentoHistoricoVO.getSituacaoRequerimentoDepartamentoVO().getCodigo(), usuarioVO);
		Integer situacaoRequerimentoDepartamentoAnterior = 0;
		if(Uteis.isAtributoPreenchido(requerimentoHistoricoVO.getCodigo())) {
			situacaoRequerimentoDepartamentoAnterior = consultarSituacaoRequermentoAtual(requerimentoHistoricoVO.getCodigo());
		}
		if(!situacaoRequerimentoDepartamentoAnterior.equals(requerimentoHistoricoVO.getSituacaoRequerimentoDepartamentoVO().getCodigo())) {
			if(!requerimentoHistoricoVO.getLogAlteracaoSituacao().trim().isEmpty()) {
				requerimentoHistoricoVO.setLogAlteracaoSituacao(requerimentoHistoricoVO.getLogAlteracaoSituacao()+"/n");
			}
			if(Uteis.isAtributoPreenchido(requerimentoHistoricoVO.getSituacaoRequerimentoDepartamentoVO())) {
				requerimentoHistoricoVO.setSituacaoRequerimentoDepartamentoVO(getFacadeFactory().getSituacaoRequerimentoDepartamentoFacade().consultarPorChavePrimaria(requerimentoHistoricoVO.getSituacaoRequerimentoDepartamentoVO().getCodigo(), usuarioVO));
				requerimentoHistoricoVO.setLogAlteracaoSituacao(requerimentoHistoricoVO.getLogAlteracaoSituacao()+"Situação trâmite alterada para "+ requerimentoHistoricoVO.getSituacaoRequerimentoDepartamentoVO().getSituacao().toUpperCase() +" pelo(a) usuário(a) "+usuarioVO.getNome()+" em "+Uteis.getDataComHora(new Date())+".");
			}else {
				requerimentoHistoricoVO.setLogAlteracaoSituacao(requerimentoHistoricoVO.getLogAlteracaoSituacao()+"Situação trâmite removida pelo(a) usuário(a) "+usuarioVO.getNome()+" em "+Uteis.getDataComHora(new Date())+".");
			}
			alterar(requerimentoHistoricoVO, usuarioVO);					
		}
	}
	
	
	public Integer consultarSituacaoRequermentoAtual(Integer codigoRequerimentoHistorico) {
		SqlRowSet rs  =  getConexao().getJdbcTemplate().queryForRowSet("select situacaoRequerimentoDepartamento from requerimentoHistorico where codigo = ?  ", codigoRequerimentoHistorico);
		if(rs.next()) {
			return rs.getInt("situacaoRequerimentoDepartamento");
		}
		return 0;
	}
}