package negocio.facade.jdbc.financeiro;

import java.math.BigDecimal;
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

import negocio.comuns.academico.enumeradores.MesAnoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.IndiceReajustePeriodoMatriculaPeriodoVencimentoVO;
import negocio.comuns.utilitarias.SituacaoExecucaoEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.IndiceReajustePeriodoMatriculaPeriodoVencimentoInterfaceFacade;

@Lazy
@Repository
@Scope("singleton")
public class IndiceReajustePeriodoMatriculaPeriodoVencimento extends ControleAcesso implements IndiceReajustePeriodoMatriculaPeriodoVencimentoInterfaceFacade {

	private static final long serialVersionUID = 1L;
	
	public IndiceReajustePeriodoMatriculaPeriodoVencimento() {
		super();
	}
	
	public void validarDados(IndiceReajustePeriodoMatriculaPeriodoVencimentoVO obj, UsuarioVO usuario) throws Exception {
//		if (obj.getDescricao().equals("")) {
//			throw new Exception("O campo DESCRIÇÃO deve ser informado!");
//		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final IndiceReajustePeriodoMatriculaPeriodoVencimentoVO obj, UsuarioVO usuario) throws Exception {
		try {
			validarDados(obj, usuario);
			final String sql = "INSERT INTO IndiceReajustePeriodoMatriculaPeriodoVencimento( dataReajuste, valorBaseContaReceberReajuste, valorReajuste, indiceReajustePeriodo, parcela, matriculaPeriodo, valorReferenteDiferencaParcelaRecebidaOuEnviadaRemessa, observacaoDiferencaParcelaRecebidaOuEnviadaRemessa, situacao, tipoorigem) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setTimestamp(1, Uteis.getDataJDBCTimestamp(obj.getDataReajuste()));
					sqlInserir.setBigDecimal(2, obj.getValorBaseContaReceberReajuste());
					sqlInserir.setBigDecimal(3, obj.getValorReajuste());
					if (obj.getIndiceReajustePeriodoVO().getCodigo().intValue() != 0) {
						sqlInserir.setInt(4, obj.getIndiceReajustePeriodoVO().getCodigo().intValue());
					} else {
						sqlInserir.setNull(4, 0);
					}
					sqlInserir.setString(5, obj.getParcela());
					if (obj.getMatriculaPeriodoVO().getCodigo().intValue() != 0) {
						sqlInserir.setInt(6, obj.getMatriculaPeriodoVO().getCodigo().intValue());
					} else {
						sqlInserir.setNull(6, 0);
					}
					sqlInserir.setBigDecimal(7, obj.getValorReferenteDiferencaParcelaRecebidaOuEnviadaRemessa());
					sqlInserir.setString(8, obj.getObservacaoDiferencaParcelaRecebidaOuEnviadaRemessa());
					sqlInserir.setString(9, obj.getSituacao().toString());
					sqlInserir.setString(10, obj.getOrigemContaReceber());

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
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final IndiceReajustePeriodoMatriculaPeriodoVencimentoVO obj, UsuarioVO usuario) throws Exception {
		try {
			validarDados(obj, usuario);
			IndiceReajuste.alterar(getIdEntidade(), usuario);
			final String sql = "UPDATE IndiceReajustePeriodoMatriculaPeriodoVencimento set dataReajuste=?, valorBaseContaReceberReajuste=?, valorReajuste=?, indiceReajustePeriodo=?, parcela=?, matriculaPeriodo=?, valorReferenteDiferencaParcelaRecebidaOuEnviadaRemessa=?, observacaoDiferencaParcelaRecebidaOuEnviadaRemessa=?, situacao=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setTimestamp(1, Uteis.getDataJDBCTimestamp(obj.getDataReajuste()));
					sqlAlterar.setBigDecimal(2, obj.getValorBaseContaReceberReajuste());
					sqlAlterar.setBigDecimal(3, obj.getValorReajuste());
					if (obj.getIndiceReajustePeriodoVO().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(4, obj.getIndiceReajustePeriodoVO().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(4, 0);
					}
					sqlAlterar.setString(5, obj.getParcela());
					if (obj.getMatriculaPeriodoVO().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(6, obj.getMatriculaPeriodoVO().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(6, 0);
					}
					sqlAlterar.setBigDecimal(7, obj.getValorReferenteDiferencaParcelaRecebidaOuEnviadaRemessa());
					sqlAlterar.setString(8, obj.getObservacaoDiferencaParcelaRecebidaOuEnviadaRemessa());
					sqlAlterar.setString(9, obj.getSituacao().toString());
					sqlAlterar.setInt(10, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public List<IndiceReajustePeriodoMatriculaPeriodoVencimentoVO> consultarPorIndiceReajustePeriodoSituacao(Integer indiceReajustePeriodo, SituacaoExecucaoEnum situacaoExecucao, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct indicereajusteperiodomatriculaperiodovencimento.codigo, indicereajusteperiodomatriculaperiodovencimento.datareajuste, ");
		sb.append(" indicereajusteperiodomatriculaperiodovencimento.valorBaseContaReceberReajuste, indicereajusteperiodomatriculaperiodovencimento.valorReajuste, ");
		sb.append(" indicereajusteperiodomatriculaperiodovencimento.indiceReajustePeriodo, indicereajusteperiodomatriculaperiodovencimento.parcela, indicereajusteperiodomatriculaperiodovencimento.situacao, ");
		sb.append(" indicereajusteperiodomatriculaperiodovencimento.matriculaPeriodo, matricula.matricula, pessoa.nome, indicereajusteperiodomatriculaperiodovencimento.situacao, ");
		sb.append(" indicereajusteperiodomatriculaperiodovencimento.motivoCancelamento, indicereajusteperiodomatriculaperiodovencimento.dataCancelamento, indicereajusteperiodomatriculaperiodovencimento.responsavelCancelamento, ");
		sb.append(" indicereajusteperiodomatriculaperiodovencimento.valorreferentediferencaparcelarecebidaouenviadaremessa, indicereajusteperiodomatriculaperiodovencimento.observacaodiferencaparcelarecebidaouenviadaremessa, ");
		sb.append(" indiceReajustePeriodo.mes, indiceReajustePeriodo.ano, indiceReajuste.codigo AS \"indiceReajuste.codigo\", indiceReajuste.descricao AS \"indiceReajuste.descricao\", ");
		sb.append(" indiceReajustePeriodo.percentualReajuste ");
		sb.append(" from indicereajusteperiodomatriculaperiodovencimento ");
		sb.append(" inner join indiceReajustePeriodo on indiceReajustePeriodo.codigo = indicereajusteperiodomatriculaperiodovencimento.indiceReajustePeriodo ");
		sb.append(" inner join indiceReajuste on indiceReajuste.codigo = indiceReajustePeriodo.indiceReajuste ");
		sb.append(" inner join matriculaperiodo on matriculaperiodo.codigo = indicereajusteperiodomatriculaperiodovencimento.matriculaperiodo ");
		sb.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno ");
		sb.append(" where indicereajusteperiodomatriculaperiodovencimento.indiceReajustePeriodo = ").append(indiceReajustePeriodo);
		sb.append(" and indiceReajustePeriodo.situacaoexecucao = '").append(situacaoExecucao.toString()).append("' ");
		sb.append(" order by matricula.matricula, indicereajusteperiodomatriculaperiodovencimento.parcela, indicereajusteperiodomatriculaperiodovencimento.datareajuste ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString()) ;
		return montarDadosLista(tabelaResultado, usuarioVO);
	}
	
	public List<IndiceReajustePeriodoMatriculaPeriodoVencimentoVO> montarDadosLista(SqlRowSet dadosSQL, UsuarioVO usuarioVO) throws Exception {
		List<IndiceReajustePeriodoMatriculaPeriodoVencimentoVO> listaIndiceReajustePeriodoMPVVOs = new ArrayList<IndiceReajustePeriodoMatriculaPeriodoVencimentoVO>(0);
		while (dadosSQL.next()) {
			IndiceReajustePeriodoMatriculaPeriodoVencimentoVO obj = new IndiceReajustePeriodoMatriculaPeriodoVencimentoVO();
			montarDados(obj, dadosSQL, usuarioVO);
			listaIndiceReajustePeriodoMPVVOs.add(obj);
		}
		return listaIndiceReajustePeriodoMPVVOs;
	}
	
	@Override
	public IndiceReajustePeriodoMatriculaPeriodoVencimentoVO consultarUltimoIndicePorMatriculaPeriodoSituacaoParcela(Integer indiceReajustePeriodo, Integer matriculaPeriodo, SituacaoExecucaoEnum situacaoExecucao, String parcela, String tipoOrigem, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct indicereajusteperiodomatriculaperiodovencimento.codigo, indicereajusteperiodomatriculaperiodovencimento.datareajuste, ");
		sb.append(" indicereajusteperiodomatriculaperiodovencimento.valorBaseContaReceberReajuste, indicereajusteperiodomatriculaperiodovencimento.valorReajuste, ");
		sb.append(" indicereajusteperiodomatriculaperiodovencimento.indiceReajustePeriodo, indicereajusteperiodomatriculaperiodovencimento.parcela, indicereajusteperiodomatriculaperiodovencimento.situacao, ");
		sb.append(" indicereajusteperiodomatriculaperiodovencimento.valorreferentediferencaparcelarecebidaouenviadaremessa, indicereajusteperiodomatriculaperiodovencimento.observacaodiferencaparcelarecebidaouenviadaremessa, ");
		sb.append(" indicereajusteperiodomatriculaperiodovencimento.matriculaPeriodo, matricula.matricula, pessoa.nome, ");
		sb.append(" indicereajusteperiodomatriculaperiodovencimento.motivoCancelamento, indicereajusteperiodomatriculaperiodovencimento.dataCancelamento, indicereajusteperiodomatriculaperiodovencimento.responsavelCancelamento, ");
		sb.append(" indiceReajustePeriodo.mes, indiceReajustePeriodo.ano, indiceReajuste.codigo AS \"indiceReajuste.codigo\", indiceReajuste.descricao AS \"indiceReajuste.descricao\", ");
		sb.append(" indiceReajustePeriodo.percentualReajuste ");
		sb.append(" from indicereajusteperiodomatriculaperiodovencimento ");
		sb.append(" inner join indiceReajustePeriodo on indiceReajustePeriodo.codigo = indicereajusteperiodomatriculaperiodovencimento.indiceReajustePeriodo ");
		sb.append(" inner join indiceReajuste on indiceReajuste.codigo = indiceReajustePeriodo.indiceReajuste ");
		sb.append(" inner join matriculaperiodo on matriculaperiodo.codigo = indicereajusteperiodomatriculaperiodovencimento.matriculaperiodo ");
		sb.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno ");
		sb.append(" where indicereajusteperiodomatriculaperiodovencimento.matriculaPeriodo = ").append(matriculaPeriodo);
		sb.append(" and indiceReajustePeriodo.codigo = ").append(indiceReajustePeriodo);
		sb.append(" and indiceReajustePeriodo.situacaoexecucao = '").append(situacaoExecucao.toString()).append("' ");
		sb.append(" and indicereajusteperiodomatriculaperiodovencimento.parcela = '").append(parcela.substring(0, parcela.indexOf("/"))).append("' ");
		sb.append(" and indicereajusteperiodomatriculaperiodovencimento.situacao = '").append(situacaoExecucao.toString()).append("' ");
		sb.append(" and indicereajusteperiodomatriculaperiodovencimento.tipoorigem = '").append(tipoOrigem).append("' ");
		sb.append(" order by matricula.matricula, indicereajusteperiodomatriculaperiodovencimento.datareajuste, indicereajusteperiodomatriculaperiodovencimento.parcela ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		IndiceReajustePeriodoMatriculaPeriodoVencimentoVO obj = new IndiceReajustePeriodoMatriculaPeriodoVencimentoVO();
		if (tabelaResultado.next()) {
			montarDados(obj, tabelaResultado, usuarioVO);
		}
		return obj;
	}
	
	@Override
	public IndiceReajustePeriodoMatriculaPeriodoVencimentoVO consultarPenultimoIndicePorMatriculaPeriodoSituacaoParcela(Integer indiceReajustePeriodoUltimo, Integer matriculaPeriodo, SituacaoExecucaoEnum situacaoExecucao, String parcela,  String tipoOrigem, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct indicereajusteperiodomatriculaperiodovencimento.codigo, indicereajusteperiodomatriculaperiodovencimento.datareajuste, ");
		sb.append(" indicereajusteperiodomatriculaperiodovencimento.valorBaseContaReceberReajuste, indicereajusteperiodomatriculaperiodovencimento.valorReajuste, ");
		sb.append(" indicereajusteperiodomatriculaperiodovencimento.indiceReajustePeriodo, indicereajusteperiodomatriculaperiodovencimento.parcela, indicereajusteperiodomatriculaperiodovencimento.situacao, ");
		sb.append(" indicereajusteperiodomatriculaperiodovencimento.valorreferentediferencaparcelarecebidaouenviadaremessa, indicereajusteperiodomatriculaperiodovencimento.observacaodiferencaparcelarecebidaouenviadaremessa, ");
		sb.append(" indicereajusteperiodomatriculaperiodovencimento.matriculaPeriodo, matricula.matricula, pessoa.nome, ");
		sb.append(" indicereajusteperiodomatriculaperiodovencimento.motivoCancelamento, indicereajusteperiodomatriculaperiodovencimento.dataCancelamento, indicereajusteperiodomatriculaperiodovencimento.responsavelCancelamento, ");
		sb.append(" indiceReajustePeriodo.mes, indiceReajustePeriodo.ano, indiceReajuste.codigo AS \"indiceReajuste.codigo\", indiceReajuste.descricao AS \"indiceReajuste.descricao\", ");
		sb.append(" indiceReajustePeriodo.percentualReajuste ");
		sb.append(" from indicereajusteperiodomatriculaperiodovencimento ");
		sb.append(" inner join indiceReajustePeriodo on indiceReajustePeriodo.codigo = indicereajusteperiodomatriculaperiodovencimento.indiceReajustePeriodo ");
		sb.append(" inner join indiceReajuste on indiceReajuste.codigo = indiceReajustePeriodo.indiceReajuste ");
		sb.append(" inner join matriculaperiodo on matriculaperiodo.codigo = indicereajusteperiodomatriculaperiodovencimento.matriculaperiodo ");
		sb.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno ");
		sb.append(" where indicereajusteperiodomatriculaperiodovencimento.matriculaPeriodo = ").append(matriculaPeriodo);
		sb.append(" and indiceReajustePeriodo.codigo != ").append(indiceReajustePeriodoUltimo);
		sb.append(" and indiceReajustePeriodo.situacaoexecucao = '").append(situacaoExecucao.toString()).append("' ");
		sb.append(" and indicereajusteperiodomatriculaperiodovencimento.parcela = '").append(parcela.substring(0, parcela.indexOf("/"))).append("' ");
		sb.append(" and indicereajusteperiodomatriculaperiodovencimento.situacao = '").append(situacaoExecucao.toString()).append("' ");
		sb.append(" and indicereajusteperiodomatriculaperiodovencimento.tipoorigem = '").append(tipoOrigem).append("' ");
		sb.append(" order by matricula.matricula, indicereajusteperiodomatriculaperiodovencimento.datareajuste, indicereajusteperiodomatriculaperiodovencimento.parcela ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		IndiceReajustePeriodoMatriculaPeriodoVencimentoVO obj = new IndiceReajustePeriodoMatriculaPeriodoVencimentoVO();
		
		if (tabelaResultado.next()) {
			montarDados(obj, tabelaResultado, usuarioVO);
		}
		return obj;
	}
	
	public void montarDados(IndiceReajustePeriodoMatriculaPeriodoVencimentoVO obj, SqlRowSet dadosSql, UsuarioVO usuarioVO) throws Exception {
		obj.setCodigo(dadosSql.getInt("codigo"));
		obj.setDataReajuste(dadosSql.getTimestamp("dataReajuste"));
		obj.setValorBaseContaReceberReajuste(dadosSql.getBigDecimal("valorBaseContaReceberReajuste"));
		obj.setValorReajuste(dadosSql.getBigDecimal("valorReajuste"));
		obj.getIndiceReajustePeriodoVO().setCodigo(dadosSql.getInt("indiceReajustePeriodo"));
		obj.getIndiceReajustePeriodoVO().setMes(MesAnoEnum.valueOf(dadosSql.getString("mes")));
		obj.getIndiceReajustePeriodoVO().setAno(dadosSql.getString("ano"));
		obj.getIndiceReajustePeriodoVO().setPercentualReajuste(dadosSql.getBigDecimal("percentualReajuste"));
		obj.setParcela(dadosSql.getString("parcela"));
		obj.getMatriculaPeriodoVO().setCodigo(dadosSql.getInt("matriculaPeriodo"));
		obj.getMatriculaPeriodoVO().getMatriculaVO().setMatricula(dadosSql.getString("matricula"));
		obj.getMatriculaPeriodoVO().getMatriculaVO().getAluno().setNome(dadosSql.getString("nome"));
		obj.getIndiceReajustePeriodoVO().getIndiceReajusteVO().setCodigo(dadosSql.getInt("indiceReajuste.codigo"));
		obj.getIndiceReajustePeriodoVO().getIndiceReajusteVO().setDescricao(dadosSql.getString("indiceReajuste.descricao"));
		obj.setValorReferenteDiferencaParcelaRecebidaOuEnviadaRemessa(dadosSql.getBigDecimal("valorreferentediferencaparcelarecebidaouenviadaremessa"));
		obj.setObservacaoDiferencaParcelaRecebidaOuEnviadaRemessa(dadosSql.getString("observacaodiferencaparcelarecebidaouenviadaremessa"));
		obj.setMotivoCancelamento(dadosSql.getString("motivoCancelamento"));
		obj.setDataCancelamento(dadosSql.getDate("dataCancelamento"));
		obj.getResponsavelCancelamentoVO().setCodigo(dadosSql.getInt("responsavelCancelamento"));
		obj.setSituacao(SituacaoExecucaoEnum.valueOf(dadosSql.getString("situacao")));
		if (!obj.getResponsavelCancelamentoVO().getCodigo().equals(0)) {
			obj.setResponsavelCancelamentoVO(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavelCancelamentoVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO));
		}
		
	}
	
	@Override
	public BigDecimal consultarValorIndiceReajusteASerAplicadoContaReceberPorMatriculaPeriodoSituacao(Integer matriculaPeriodo, SituacaoExecucaoEnum situacaoExecucao, String parcela, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select sum(valorreajuste) AS valorreajuste from ( ");
		sb.append(" select distinct  ");
		sb.append(" indicereajusteperiodo.codigo, indicereajusteperiodomatriculaperiodovencimento.valorreajuste ");
		sb.append(" from indicereajusteperiodomatriculaperiodovencimento  ");
		sb.append(" inner join indiceReajustePeriodo on indiceReajustePeriodo.codigo = indicereajusteperiodomatriculaperiodovencimento.indiceReajustePeriodo ");
		sb.append(" inner join matriculaperiodo on matriculaperiodo.codigo = indicereajusteperiodomatriculaperiodovencimento.matriculaperiodo ");
		sb.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
		sb.append(" where indicereajusteperiodomatriculaperiodovencimento.matriculaPeriodo = ").append(matriculaPeriodo);
		sb.append(" and indiceReajustePeriodo.situacaoexecucao = '").append(situacaoExecucao.toString()).append("' ");
		sb.append(" and indicereajusteperiodomatriculaperiodovencimento.parcela = '").append(parcela.contains("/") ? parcela.substring(0, parcela.indexOf("/")) : parcela).append("' ");
		sb.append(" and indicereajusteperiodomatriculaperiodovencimento.situacao = '").append(situacaoExecucao.toString()).append("' ");
		sb.append(") as t  ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getBigDecimal("valorreajuste");
		}
		return BigDecimal.ZERO;
	}
	
	@Override
	public BigDecimal consultarValorReajusteDiferencaParcelaRecebidaOuEnviadaRemessaASerAplicadoContaReceberPorMatriculaPeriodoSituacaoParcela(Integer matriculaPeriodo, SituacaoExecucaoEnum situacaoExecucao, String parcela, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select sum(valorReferenteDiferencaParcelaRecebidaOuEnviadaRemessa) AS valorReferenteDiferencaParcelaRecebidaOuEnviadaRemessa from ( ");
		sb.append(" select distinct  ");
		sb.append(" indicereajusteperiodo.codigo, indicereajusteperiodomatriculaperiodovencimento.valorReferenteDiferencaParcelaRecebidaOuEnviadaRemessa ");
		sb.append(" from indicereajusteperiodomatriculaperiodovencimento  ");
		sb.append(" inner join indiceReajustePeriodo on indiceReajustePeriodo.codigo = indicereajusteperiodomatriculaperiodovencimento.indiceReajustePeriodo ");
		sb.append(" inner join matriculaperiodo on matriculaperiodo.codigo = indicereajusteperiodomatriculaperiodovencimento.matriculaperiodo ");
		sb.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
		sb.append(" where indicereajusteperiodomatriculaperiodovencimento.matriculaPeriodo = ").append(matriculaPeriodo);
		sb.append(" and indiceReajustePeriodo.situacaoexecucao = '").append(situacaoExecucao.toString()).append("' ");
		sb.append(" and indicereajusteperiodomatriculaperiodovencimento.parcela = '").append(parcela.contains("/") ? parcela.substring(0, parcela.indexOf("/")) : parcela).append("' ");
		sb.append(" and indicereajusteperiodomatriculaperiodovencimento.situacao = '").append(situacaoExecucao.toString()).append("' ");
		sb.append(") as t  ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getBigDecimal("valorReferenteDiferencaParcelaRecebidaOuEnviadaRemessa");
		}
		return BigDecimal.ZERO;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public List<IndiceReajustePeriodoMatriculaPeriodoVencimentoVO> consultarPorMatriculaPeriodoSituacaoParcela(Integer matriculaPeriodo, SituacaoExecucaoEnum situacaoExecucao, String parcela, String tipoOrigem, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct indicereajusteperiodomatriculaperiodovencimento.codigo, indicereajusteperiodomatriculaperiodovencimento.datareajuste, ");
		sb.append(" indicereajusteperiodomatriculaperiodovencimento.valorBaseContaReceberReajuste, indicereajusteperiodomatriculaperiodovencimento.valorReajuste, ");
		sb.append(" indicereajusteperiodomatriculaperiodovencimento.indiceReajustePeriodo, indicereajusteperiodomatriculaperiodovencimento.parcela, ");
		sb.append(" indicereajusteperiodomatriculaperiodovencimento.matriculaPeriodo, matricula.matricula, pessoa.nome, indicereajusteperiodomatriculaperiodovencimento.situacao, ");
		sb.append(" indicereajusteperiodomatriculaperiodovencimento.motivoCancelamento, indicereajusteperiodomatriculaperiodovencimento.dataCancelamento, indicereajusteperiodomatriculaperiodovencimento.responsavelCancelamento, ");
		sb.append(" indicereajusteperiodomatriculaperiodovencimento.valorreferentediferencaparcelarecebidaouenviadaremessa, indicereajusteperiodomatriculaperiodovencimento.observacaodiferencaparcelarecebidaouenviadaremessa, ");
		sb.append(" indiceReajustePeriodo.mes, indiceReajustePeriodo.ano, indiceReajuste.codigo AS \"indiceReajuste.codigo\", indiceReajuste.descricao AS \"indiceReajuste.descricao\", ");
		sb.append(" indiceReajustePeriodo.percentualReajuste ");
		sb.append(" from indicereajusteperiodomatriculaperiodovencimento ");
		sb.append(" inner join indiceReajustePeriodo on indiceReajustePeriodo.codigo = indicereajusteperiodomatriculaperiodovencimento.indiceReajustePeriodo ");
		sb.append(" inner join indiceReajuste on indiceReajuste.codigo = indiceReajustePeriodo.indiceReajuste ");
		sb.append(" inner join matriculaperiodo on matriculaperiodo.codigo = indicereajusteperiodomatriculaperiodovencimento.matriculaperiodo ");
		sb.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno ");
		sb.append(" where indicereajusteperiodomatriculaperiodovencimento.matriculaPeriodo = ").append(matriculaPeriodo);
		sb.append(" and indicereajusteperiodomatriculaperiodovencimento.parcela = '").append(parcela).append("' ");
		sb.append(" and indicereajusteperiodomatriculaperiodovencimento.situacao = '").append(situacaoExecucao.toString()).append("' ");
		sb.append(" and indicereajusteperiodomatriculaperiodovencimento.tipoorigem = '").append(tipoOrigem).append("' ");
		sb.append(" order by matricula.matricula, indicereajusteperiodomatriculaperiodovencimento.datareajuste, indicereajusteperiodomatriculaperiodovencimento.parcela ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString()) ;
		return montarDadosLista(tabelaResultado, usuarioVO);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarIndiceReajustePeriodoMatriculaPeriodoVencimentoSituacaoCanceladoPorIndiceReajustePeriodo(Integer indiceReajustePeriodo, SituacaoExecucaoEnum situacaoExecucao, String motivoCancelamento, UsuarioVO usuario) throws Exception {
		String sqlStr = "";
		sqlStr = "UPDATE IndiceReajustePeriodoMatriculaPeriodoVencimento set situacao=?, motivoCancelamento=?, responsavelCancelamento=?, dataCancelamento=CURRENT_TIMESTAMP WHERE ((indiceReajustePeriodo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sqlStr, new Object[] { situacaoExecucao.toString(), motivoCancelamento, usuario.getCodigo(), indiceReajustePeriodo });
		
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarIndiceReajustePeriodoMatriculaPeriodoVencimentoSituacaoCancelado(Integer codigo, SituacaoExecucaoEnum situacaoExecucao, String motivoCancelamento, UsuarioVO usuario) throws Exception {
		String sqlStr = "";
		sqlStr = "UPDATE IndiceReajustePeriodoMatriculaPeriodoVencimento set situacao=?, motivoCancelamento=?, responsavelCancelamento=?, dataCancelamento=CURRENT_TIMESTAMP WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sqlStr, new Object[] { situacaoExecucao.toString(), motivoCancelamento, usuario.getCodigo(), codigo });
		
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public IndiceReajustePeriodoMatriculaPeriodoVencimentoVO inicializarDadosIndiceReajustePeriodoMatriculaPeriodoVencimento(Integer indiceReajustePeriodo, SituacaoExecucaoEnum situacao, String tipoOrigemContaReceber,  ContaReceberVO contaReceberVO, BigDecimal valorIndiceReajuste, BigDecimal valorReajusteDiferencaParcelaRecebida, String observacaoDiferencaParcelaRecebidaOuEnviadaRemessa, UsuarioVO usuarioVO) throws Exception {
		IndiceReajustePeriodoMatriculaPeriodoVencimentoVO obj = new IndiceReajustePeriodoMatriculaPeriodoVencimentoVO();
		obj.setDataReajuste(new Date());
		obj.setSituacao(situacao);
		if(contaReceberVO.getConsiderarDescontoSemValidadeCalculoIndiceReajuste()) {
			obj.setValorBaseContaReceberReajuste(BigDecimal.valueOf(contaReceberVO.getValorBaseContaReceber() - contaReceberVO.getValorDescontoSemValidade()));
		}else {
			obj.setValorBaseContaReceberReajuste(BigDecimal.valueOf(contaReceberVO.getValorBaseContaReceber()));
		}
		obj.setValorReajuste(valorIndiceReajuste);
		obj.getIndiceReajustePeriodoVO().setCodigo(indiceReajustePeriodo);
		obj.setParcela(contaReceberVO.getParcela().substring(0, contaReceberVO.getParcela().indexOf("/")));
		obj.getMatriculaPeriodoVO().setCodigo(contaReceberVO.getMatriculaPeriodo());
		obj.setOrigemContaReceber(tipoOrigemContaReceber);
		if (valorReajusteDiferencaParcelaRecebida != null && !valorReajusteDiferencaParcelaRecebida.equals(BigDecimal.ZERO)) {
			obj.setValorReferenteDiferencaParcelaRecebidaOuEnviadaRemessa(valorReajusteDiferencaParcelaRecebida);
			obj.setObservacaoDiferencaParcelaRecebidaOuEnviadaRemessa(observacaoDiferencaParcelaRecebidaOuEnviadaRemessa);
		}
		return obj;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void cancelarIndiceReajustePeriodoMatriculaPeriodoVencimento(List<IndiceReajustePeriodoMatriculaPeriodoVencimentoVO> listaIndiceReajusteMatriculaPeriodoVencimentoVOs, String motivoCancelamento, UsuarioVO usuarioVO) throws Exception {
		if (motivoCancelamento.equals("")) {
			throw new Exception("O MOTIVO DE CANCELAMENTO deve ser informado!");
		}
		for (IndiceReajustePeriodoMatriculaPeriodoVencimentoVO obj : listaIndiceReajusteMatriculaPeriodoVencimentoVOs) {
			if (obj.getSelecionado() && !obj.getSituacao().equals(SituacaoExecucaoEnum.CANCELADO)) {
				ContaReceberVO contaReceberVO = getFacadeFactory().getContaReceberFacade().consultarContaReceberProcessadaParaCancelamentoPorIndiceReajustePeriodoMatriculaPeriodoVencimento(obj.getCodigo(), usuarioVO);
				if (!contaReceberVO.getCodigo().equals(0)) {
					String tipoOrigem = contaReceberVO.getTipoOrigem().equals("MEN") ? "MENSALIDADE" : "BCC";
					BigDecimal valorIndiceReajuste = obj.getValorReajuste();
					IndiceReajustePeriodoMatriculaPeriodoVencimentoVO indicePenultimoPeriodoVencimentoVO = getFacadeFactory().getIndiceReajustePeriodoMatriculaPeriodoVencimentoFacade().consultarPenultimoIndicePorMatriculaPeriodoSituacaoParcela(obj.getIndiceReajustePeriodoVO().getCodigo(), contaReceberVO.getMatriculaPeriodo(), SituacaoExecucaoEnum.PROCESSADO, contaReceberVO.getParcela(), tipoOrigem, usuarioVO);
					if(Uteis.isAtributoPreenchido(indicePenultimoPeriodoVencimentoVO)) {
						contaReceberVO.setValorIndiceReajuste(indicePenultimoPeriodoVencimentoVO.getValorReajuste());
					}else {
						contaReceberVO.setValorIndiceReajuste(contaReceberVO.getValorIndiceReajuste().subtract(valorIndiceReajuste));
					}
					contaReceberVO.setValorBaseContaReceber(contaReceberVO.getValorBaseContaReceber() - valorIndiceReajuste.doubleValue());
					
					contaReceberVO.setValor(contaReceberVO.getValor() - valorIndiceReajuste.doubleValue());
					getFacadeFactory().getContaReceberFacade().alterarValorBaseContaReceber(contaReceberVO.getCodigo(), contaReceberVO.getValorBaseContaReceber(), usuarioVO);
					getFacadeFactory().getContaReceberFacade().alterarValor(contaReceberVO, usuarioVO);
					getFacadeFactory().getContaReceberFacade().alterarValorIndiceReajusteContaReceber(contaReceberVO.getCodigo(), contaReceberVO.getValorIndiceReajuste(), BigDecimal.ZERO, usuarioVO);
					
					List<ContaReceberVO> listaContaReceberVOs = new ArrayList<ContaReceberVO>(0);
					listaContaReceberVOs.add(contaReceberVO);
					
					getFacadeFactory().getContaReceberFacade().realizarProcessamentoValorFinalContaReceberAtualizadoComAcrescimosEDescontos(null, listaContaReceberVOs, false, "", true, usuarioVO, true, false , null , null, true);
					
					if(getFacadeFactory().getControleRemessaContaReceberFacade().consultarExisteRemessaContaReceber(contaReceberVO.getCodigo())) {
						ContaCorrenteVO contaCorrente = consultarContaCorrente(contaReceberVO.getContaCorrenteVO().getCodigo(),usuarioVO);
						getFacadeFactory().getContaReceberFacade().processarRegerarNossoNumeroContaReceber(contaReceberVO, contaCorrente, usuarioVO);
					}
				}
				obj.setDataCancelamento(new Date());
				obj.setMotivoCancelamento(motivoCancelamento);
				obj.setResponsavelCancelamentoVO(usuarioVO);
				obj.setSituacao(SituacaoExecucaoEnum.CANCELADO);
				obj.setSelecionado(false);
				getFacadeFactory().getIndiceReajustePeriodoMatriculaPeriodoVencimentoFacade().alterarIndiceReajustePeriodoMatriculaPeriodoVencimentoSituacaoCancelado(obj.getCodigo(), SituacaoExecucaoEnum.CANCELADO, motivoCancelamento, usuarioVO);
			}
		}
	}
	
	private ContaCorrenteVO consultarContaCorrente(Integer codigoContaCorrente, UsuarioVO usuarioVO) throws Exception {
		return  getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(codigoContaCorrente, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
	}
}
