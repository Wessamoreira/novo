package negocio.facade.jdbc.financeiro;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import negocio.comuns.financeiro.IndiceReajustePeriodoVO;
import negocio.comuns.financeiro.IndiceReajusteVO;
import negocio.comuns.financeiro.MatriculaPeriodoVencimentoVO;
import negocio.comuns.job.RegistroExecucaoJobVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ProcessarParalelismo;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.SituacaoExecucaoEnum;
import negocio.comuns.utilitarias.Stopwatch;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoContaReceber;
import negocio.comuns.utilitarias.dominios.SituacaoVencimentoMatriculaPeriodo;
import negocio.comuns.utilitarias.dominios.TipoOrigemContaReceber;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.IndiceReajustePeriodoInterfaceFacade;

@Lazy
@Repository
@Scope("singleton")
public class IndiceReajustePeriodo extends ControleAcesso implements IndiceReajustePeriodoInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public IndiceReajustePeriodo() throws Exception {
		super();
		setIdEntidade("IndiceReajuste");
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void validarDados(IndiceReajustePeriodoVO obj, UsuarioVO usuario) throws Exception {
		if (obj.getMes() == null) {
			throw new Exception("O campo MÊS deve ser informado!");
		}
		if (obj.getAno() == null) {
			throw new Exception("O campo ANO deve ser informado!");
		}
		if (obj.getResponsavelVO().getCodigo().equals(0)) {
			throw new Exception("Não possui um responsável para a operação!");
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final IndiceReajustePeriodoVO obj, UsuarioVO usuario) throws Exception {
		try {
			validarDados(obj, usuario);
			IndiceReajustePeriodo.incluir(getIdEntidade(), usuario);
			final String sql = "INSERT INTO IndiceReajustePeriodo( indiceReajuste, mes, ano, responsavel, data, situacaoExecucao, percentualReajuste, motivoCancelamento, dataCancelamento, considerarDescontoSemValidadeCalculoIndiceReajuste ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);

					if (obj.getIndiceReajusteVO().getCodigo().intValue() != 0) {
						sqlInserir.setInt(1, obj.getIndiceReajusteVO().getCodigo().intValue());
					} else {
						sqlInserir.setNull(1, 0);
					}
					sqlInserir.setString(2, obj.getMes().name().toString());
					sqlInserir.setString(3, obj.getAno());
					if (obj.getResponsavelVO().getCodigo().intValue() != 0) {
						sqlInserir.setInt(4, obj.getResponsavelVO().getCodigo().intValue());
					} else {
						sqlInserir.setNull(4, 0);
					}
					sqlInserir.setTimestamp(5, Uteis.getDataJDBCTimestamp(obj.getData()));
					sqlInserir.setString(6, obj.getSituacaoExecucao().toString());
					sqlInserir.setBigDecimal(7, obj.getPercentualReajuste());
					sqlInserir.setString(8, obj.getMotivoCancelamento());
					if (obj.getSituacaoExecucao().equals(SituacaoExecucaoEnum.CANCELADO)) {
						sqlInserir.setTimestamp(9, Uteis.getDataJDBCTimestamp(obj.getDataCancelamento()));
					} else {
						sqlInserir.setNull(9, 0);
					}
					
					int x = 10;
					Uteis.setValuePreparedStatement(obj.getConsiderarDescontoSemValidadeCalculoIndiceReajuste(), x++, sqlInserir);

					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {

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
	public void alterar(final IndiceReajustePeriodoVO obj, UsuarioVO usuario) throws Exception {
		try {

			validarDados(obj, usuario);
			IndiceReajustePeriodo.alterar(getIdEntidade(), usuario);
			final String sql = "UPDATE IndiceReajustePeriodo set indiceReajuste=?, mes=?, ano=?, responsavel=?, data=?, situacaoExecucao=?, percentualReajuste=?, motivoCancelamento=?, dataCancelamento=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					if (obj.getIndiceReajusteVO().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(1, obj.getIndiceReajusteVO().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(1, 0);
					}
					sqlAlterar.setString(2, obj.getMes().name().toString());
					sqlAlterar.setString(3, obj.getAno());
					if (obj.getResponsavelVO().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(4, obj.getResponsavelVO().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(4, 0);
					}
					sqlAlterar.setTimestamp(5, Uteis.getDataJDBCTimestamp(obj.getData()));
					sqlAlterar.setString(6, obj.getSituacaoExecucao().toString());
					sqlAlterar.setBigDecimal(7, obj.getPercentualReajuste());
					sqlAlterar.setString(8, obj.getMotivoCancelamento());
					if (obj.getSituacaoExecucao().equals(SituacaoExecucaoEnum.CANCELADO)) {
						sqlAlterar.setTimestamp(9, Uteis.getDataJDBCTimestamp(obj.getDataCancelamento()));
					} else {
						sqlAlterar.setNull(9, 0);
					}
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
	public void excluirIndiceReajustePeriodoPorIndiceReajuste(IndiceReajusteVO obj, UsuarioVO usuario) throws Exception {
		try {
			Banco.excluir(getIdEntidade(), true, usuario);
			String sql = "DELETE FROM IndiceReajustePeriodo WHERE ((IndiceReajuste = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void incluirIndiceReajustePeriodoVOs(Integer indiceReajuste, List<IndiceReajustePeriodoVO> listaIndiceReajustePeriodoVOs, UsuarioVO usuarioVO) throws Exception {
		for (IndiceReajustePeriodoVO indiceReajustePeriodoVO : listaIndiceReajustePeriodoVOs) {
			indiceReajustePeriodoVO.getIndiceReajusteVO().setCodigo(indiceReajuste);
			incluir(indiceReajustePeriodoVO, usuarioVO);
		}
	}

	@Override
	public void alterarIndiceReajustePeriodoVOs(Integer indiceReajuste, List<IndiceReajustePeriodoVO> listaIndiceReajustePeriodoVOs, UsuarioVO usuarioVO) throws Exception {
		excluirIndiceReajustePeriodoVOs(indiceReajuste, listaIndiceReajustePeriodoVOs, usuarioVO);
		for (IndiceReajustePeriodoVO indiceReajustePeriodoVO : listaIndiceReajustePeriodoVOs) {
			indiceReajustePeriodoVO.getIndiceReajusteVO().setCodigo(indiceReajuste);
			if (indiceReajustePeriodoVO.getCodigo().equals(0)) {
				incluir(indiceReajustePeriodoVO, usuarioVO);
			} else {
				alterar(indiceReajustePeriodoVO, usuarioVO);
			}
		}
	}

	public void excluirIndiceReajustePeriodoVOs(Integer indiceReajuste, List<IndiceReajustePeriodoVO> listaIndiceReajustePeriodoVOs, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM indiceReajustePeriodo WHERE indiceReajuste = ").append(indiceReajuste);
		sb.append(" AND CODIGO NOT IN(");
		for (IndiceReajustePeriodoVO indiceReajustePeriodoVO : listaIndiceReajustePeriodoVOs) {
			sb.append(indiceReajustePeriodoVO.getCodigo()).append(", ");
		}
		sb.append("0) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sb.toString());
	}

	@Override
	public List<IndiceReajustePeriodoVO> consultarIndiceReajustePeriodoPorIndiceReajuste(Integer indiceReajuste, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select indiceReajustePeriodo.codigo, indiceReajustePeriodo.mes, indiceReajustePeriodo.considerarDescontoSemValidadeCalculoIndiceReajuste, ");
		sb.append(" indiceReajustePeriodo.ano, indiceReajustePeriodo.responsavel, ");
		sb.append(" usuario.nome, indiceReajustePeriodo.data, indiceReajustePeriodo.situacaoExecucao,  ");
		sb.append(" indiceReajustePeriodo.percentualReajuste, indiceReajustePeriodo.motivoCancelamento, indiceReajustePeriodo.dataCancelamento,  ");
		sb.append(" indiceReajustePeriodo.logAlunosNaoDevemSofrerReajuste, indiceReajustePeriodo.logErroProcessamento, ");
		sb.append(" indiceReajuste.codigo as \"indiceReajuste.codigo\", indiceReajuste.descricao as \"indiceReajuste.descricao\" ");
		sb.append(" from indiceReajustePeriodo ");
		sb.append(" inner join indiceReajuste on indiceReajuste.codigo = indiceReajustePeriodo.indiceReajuste ");
		sb.append(" inner join usuario on usuario.codigo = indiceReajustePeriodo.responsavel ");
		sb.append(" where indiceReajustePeriodo.indiceReajuste = ").append(indiceReajuste);
		sb.append(" order by indiceReajustePeriodo.ano desc, ");
		sb.append("	case indiceReajustePeriodo.mes when 'JANEIRO' THEN 1");
		sb.append("			 when 'FEVEREIRO' THEN 2");
		sb.append("			 when 'MARCO' THEN 3");
		sb.append("			 when 'ABRIL' THEN 4");
		sb.append("			 when 'MAIO' THEN 5");
		sb.append("			 when 'JUNHO' THEN 6");
		sb.append("			 when 'JULHO' THEN 7");
		sb.append("			 when 'AGOSTO' THEN 8");
		sb.append("			 when 'SETEMBRO' THEN 9");
		sb.append("			 when 'OUTUBRO' THEN 10");
		sb.append("			 when 'NOVEMBRO' THEN 11 ELSE 12 END desc ");		
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		return montarDadosLista(tabelaResultado, usuarioVO);
	}

	@Override
	public IndiceReajustePeriodoVO consultarPorChavePrimaria(Integer indiceReajustePeriodo, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select indiceReajustePeriodo.codigo, indiceReajustePeriodo.mes, indiceReajustePeriodo.considerarDescontoSemValidadeCalculoIndiceReajuste, ");
		sb.append(" indiceReajustePeriodo.ano, indiceReajustePeriodo.responsavel, ");
		sb.append(" usuario.nome, indiceReajustePeriodo.data, indiceReajustePeriodo.situacaoExecucao, ");
		sb.append(" indiceReajustePeriodo.percentualReajuste, indiceReajustePeriodo.motivoCancelamento, indiceReajustePeriodo.dataCancelamento, ");
		sb.append(" indiceReajustePeriodo.logAlunosNaoDevemSofrerReajuste, indiceReajustePeriodo.logErroProcessamento, ");
		sb.append(" indiceReajuste.codigo as \"indiceReajuste.codigo\", indiceReajuste.descricao as \"indiceReajuste.descricao\" ");
		sb.append(" from indiceReajustePeriodo ");
		sb.append(" inner join indiceReajuste on indiceReajuste.codigo = indiceReajustePeriodo.indiceReajuste ");
		sb.append(" inner join usuario on usuario.codigo = indiceReajustePeriodo.responsavel ");
		sb.append(" where indiceReajustePeriodo.codigo = ").append(indiceReajustePeriodo);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		IndiceReajustePeriodoVO obj = new IndiceReajustePeriodoVO();
		if (tabelaResultado.next()) {
			montarDados(obj, tabelaResultado, usuarioVO);
		}
		return obj;
	}
	
	@Override
	public IndiceReajustePeriodoVO consultarPorCodigoIndiceReajustePorMesPorAno(Integer indiceReajuste, MesAnoEnum mesAnoEnum, String ano, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select indiceReajustePeriodo.codigo, indiceReajustePeriodo.mes, indiceReajustePeriodo.considerarDescontoSemValidadeCalculoIndiceReajuste, ");
		sb.append(" indiceReajustePeriodo.ano, indiceReajustePeriodo.responsavel, ");
		sb.append(" usuario.nome, indiceReajustePeriodo.data, indiceReajustePeriodo.situacaoExecucao,  ");
		sb.append(" indiceReajustePeriodo.percentualReajuste, indiceReajustePeriodo.motivoCancelamento, indiceReajustePeriodo.dataCancelamento, ");
		sb.append(" indiceReajustePeriodo.logAlunosNaoDevemSofrerReajuste, indiceReajustePeriodo.logErroProcessamento, ");
		sb.append(" indiceReajuste.codigo as \"indiceReajuste.codigo\", indiceReajuste.descricao as \"indiceReajuste.descricao\" ");
		sb.append(" from indiceReajustePeriodo ");
		sb.append(" inner join indiceReajuste on indiceReajuste.codigo = indiceReajustePeriodo.indiceReajuste ");
		sb.append(" inner join usuario on usuario.codigo = indiceReajustePeriodo.responsavel ");
		sb.append(" where indiceReajuste.codigo = ").append(indiceReajuste);
		sb.append(" and indiceReajustePeriodo.mes = '").append(mesAnoEnum.name()).append("' ");
		sb.append(" and indiceReajustePeriodo.ano = '").append(ano).append("' ");
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		IndiceReajustePeriodoVO obj = new IndiceReajustePeriodoVO();
		if (tabelaResultado.next()) {
			montarDados(obj, tabelaResultado, usuarioVO);
		}
		return obj;
	}

	public List<IndiceReajustePeriodoVO> montarDadosLista(SqlRowSet tabelaResultado, UsuarioVO usuarioVO) {
		List<IndiceReajustePeriodoVO> listaIndiceReajustePeriodoVOs = new ArrayList<IndiceReajustePeriodoVO>(0);
		while (tabelaResultado.next()) {
			IndiceReajustePeriodoVO obj = new IndiceReajustePeriodoVO();
			montarDados(obj, tabelaResultado, usuarioVO);
			listaIndiceReajustePeriodoVOs.add(obj);
		}
		return listaIndiceReajustePeriodoVOs;
	}

	public void montarDados(IndiceReajustePeriodoVO obj, SqlRowSet dadosSql, UsuarioVO usuarioVO) {
		obj.setCodigo(dadosSql.getInt("codigo"));
		obj.setMes(MesAnoEnum.valueOf(dadosSql.getString("mes")));
		obj.setAno(dadosSql.getString("ano"));
		obj.getResponsavelVO().setCodigo(dadosSql.getInt("responsavel"));
		obj.getResponsavelVO().setNome(dadosSql.getString("nome"));
		obj.setData(dadosSql.getTimestamp("data"));
		obj.setSituacaoExecucao(SituacaoExecucaoEnum.valueOf(dadosSql.getString("situacaoExecucao")));
		obj.setPercentualReajuste(dadosSql.getBigDecimal("percentualReajuste"));
		obj.setMotivoCancelamento(dadosSql.getString("motivoCancelamento"));
		obj.setDataCancelamento(dadosSql.getTimestamp("dataCancelamento"));
		obj.setConsiderarDescontoSemValidadeCalculoIndiceReajuste(dadosSql.getBoolean("considerarDescontoSemValidadeCalculoIndiceReajuste"));
		obj.getIndiceReajusteVO().setCodigo(dadosSql.getInt("indiceReajuste.codigo"));
		obj.getIndiceReajusteVO().setDescricao(dadosSql.getString("indiceReajuste.descricao"));
		obj.setLogAlunosNaoDevemSofrerReajuste(dadosSql.getString("logAlunosNaoDevemSofrerReajuste"));
		obj.setLogErroProcessamento(dadosSql.getString("logErroProcessamento"));

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public synchronized void executarReajustePrecoContaReceber(ProgressBarVO progressBarVO, IndiceReajusteVO indiceReajusteVO, final IndiceReajustePeriodoVO indiceReajustePeriodoVO, UsuarioVO usuarioVO) throws Exception {
		if (indiceReajustePeriodoVO.getCodigo().equals(0)) {
			indiceReajustePeriodoVO.getIndiceReajusteVO().setCodigo(indiceReajusteVO.getCodigo());
			incluir(indiceReajustePeriodoVO, usuarioVO);
		}
		Thread job = new Thread(new IndiceReajustePrecoExecucao(progressBarVO, indiceReajustePeriodoVO, indiceReajusteVO, usuarioVO), "IndiceReajustePeriodo"+indiceReajustePeriodoVO.getCodigo());
		job.start();		
	}
	
	@Override
	public ProgressBarVO consultarProgressBarEmExecucao(IndiceReajusteVO obj) {
	for(IndiceReajustePeriodoVO indiceReajustePeriodoVO : obj.getListaIndiceReajustePeriodoVOs()) {
		if(indiceReajustePeriodoVO.getSituacaoExecucao().equals(SituacaoExecucaoEnum.EM_PROCESSAMENTO)) {			
			if(getAplicacaoControle().getMapThreadIndiceReajuste().containsKey("IndiceReajustePeriodo"+indiceReajustePeriodoVO.getCodigo())) {
				return getAplicacaoControle().getMapThreadIndiceReajuste().get("IndiceReajustePeriodo"+indiceReajustePeriodoVO.getCodigo());				
			}
		}
	}
	return null;
	}
	
	class IndiceReajustePrecoExecucao implements Runnable {
		private IndiceReajustePeriodoVO indiceReajustePeriodoVO;
		private IndiceReajusteVO indiceReajusteVO;
		private UsuarioVO usuarioVO;
		private ProgressBarVO progressBarVO;
		

	public IndiceReajustePrecoExecucao(ProgressBarVO progressBarVO, IndiceReajustePeriodoVO indiceReajustePeriodoVO,
				IndiceReajusteVO indiceReajusteVO, UsuarioVO usuarioVO) {
			super();
			this.progressBarVO = progressBarVO;
			this.indiceReajustePeriodoVO = indiceReajustePeriodoVO;
			this.indiceReajusteVO = indiceReajusteVO;
			this.usuarioVO = usuarioVO;
		}

	public ProgressBarVO getProgressBarVO() {		
		return progressBarVO;
	}

	public void setProgressBarVO(ProgressBarVO progressBarVO) {
		this.progressBarVO = progressBarVO;
	}



	@Override
	public void run() {
		IndiceReajustePeriodoVO indiceReajustePeriodoVO2 =  consultarPorChavePrimaria(indiceReajustePeriodoVO.getCodigo(), usuarioVO);
		indiceReajustePeriodoVO.setSituacaoExecucao(indiceReajustePeriodoVO2.getSituacaoExecucao());	
		if(indiceReajustePeriodoVO.getSituacaoExecucao().equals(SituacaoExecucaoEnum.AGUARDANDO_PROCESSAMENTO)) {
			getAplicacaoControle().getMapThreadIndiceReajuste().put("IndiceReajustePeriodo"+indiceReajustePeriodoVO.getCodigo(), progressBarVO);
			Stopwatch stopwatch =  new Stopwatch();
			stopwatch.start();
			RegistroExecucaoJobVO registroExecucaoJobVO = new RegistroExecucaoJobVO();
			registroExecucaoJobVO.setDataInicio(new Date());
			registroExecucaoJobVO.setNome("EXECUÇÃO INDICE REAJUESTE DE "+indiceReajustePeriodoVO.getMesAno_Apresentar());
			try {
				indiceReajustePeriodoVO.setSituacaoExecucao(SituacaoExecucaoEnum.EM_PROCESSAMENTO);			
				alterarSituacaoIndiceReajustePeriodo(indiceReajustePeriodoVO.getCodigo(), SituacaoExecucaoEnum.EM_PROCESSAMENTO, "", false, usuarioVO);
				progressBarVO.setStatus("Consultando turmas vinculado ao indice de reajuste.");
				Boolean existeTurmaVinculadaIndiceReajuste = getFacadeFactory().getTurmaFacade().consultarExistenciaTurmaVinculadaIndiceReajustePreco(indiceReajusteVO.getCodigo(), usuarioVO);
				progressBarVO.setProgresso(500l);
				if (!existeTurmaVinculadaIndiceReajuste) {
					if (indiceReajustePeriodoVO.getProcessamentoAutomatico()) {
						return;
					}
					throw new Exception("Índice ainda não configurado para uma TURMA, deve ser vinculado o mesmo a uma TURMA para prosseguir.");
			}
			
			HashMap<String, String> mapMatriculasNaoPodemSofrerReajustePrecoVOs = new HashMap<String, String>(0);
			progressBarVO.setStatus("Consultando condições pagamento para aplicação do indice de reajuste.");
			Integer numeroMaximoParcelaCondicao = consultaNumeroMaximoParcelaCondicaoPagamentoPlanoFinanceiroCurso(Integer.parseInt(indiceReajustePeriodoVO.getMes().getKey()), indiceReajustePeriodoVO.getAno(), usuarioVO);
			progressBarVO.setProgresso(1000l);
			ConsistirException  consistirException = new ConsistirException();
			consistirException = realizarReajustePrecoContaReceberOuRecebidaMesesAnterior(progressBarVO, consistirException, indiceReajusteVO, indiceReajustePeriodoVO, numeroMaximoParcelaCondicao, mapMatriculasNaoPodemSofrerReajustePrecoVOs, usuarioVO);
			consistirException = realizarReajustePrecoContaReceberRecebidaOuEnviadaRemessa(progressBarVO, consistirException, indiceReajusteVO, indiceReajustePeriodoVO, numeroMaximoParcelaCondicao, mapMatriculasNaoPodemSofrerReajustePrecoVOs, usuarioVO);
			consistirException = realizarReajustePrecoContaReceberOuRecebidaMesesAnteriorBolsaCusteadaConvenio(progressBarVO, consistirException, indiceReajusteVO, indiceReajustePeriodoVO, numeroMaximoParcelaCondicao, mapMatriculasNaoPodemSofrerReajustePrecoVOs, usuarioVO);
			consistirException = realizarReajustePrecoContaReceberRecebidaOuEnviadaRemessaBolsaCusteadaConvenio(progressBarVO, consistirException, indiceReajusteVO, indiceReajustePeriodoVO, numeroMaximoParcelaCondicao, mapMatriculasNaoPodemSofrerReajustePrecoVOs, usuarioVO);
				if(!consistirException.getListaMensagemErro().isEmpty()) {
					indiceReajustePeriodoVO.setLogErroProcessamento(indiceReajustePeriodoVO.getLogErroProcessamento() + consistirException.getToStringMensagemErro());
					registroExecucaoJobVO.setErro(indiceReajustePeriodoVO.getLogErroProcessamento() + consistirException.getToStringMensagemErro());
				}
				indiceReajustePeriodoVO.setSituacaoExecucao(SituacaoExecucaoEnum.PROCESSADO);
				alterarSituacaoIndiceReajustePeriodo(indiceReajustePeriodoVO.getCodigo(), SituacaoExecucaoEnum.PROCESSADO, "", false, usuarioVO);
				inicializarDadosLogReajustePreco(indiceReajustePeriodoVO, mapMatriculasNaoPodemSofrerReajustePrecoVOs, usuarioVO);
				progressBarVO.setProgresso(progressBarVO.getMaxValue().longValue());
				stopwatch.stop();
			}catch (Exception e) {
				registroExecucaoJobVO.setErro(e.getMessage());
				indiceReajustePeriodoVO.setLogErroProcessamento(e.getMessage());	
			}finally {
				stopwatch.stop();
				registroExecucaoJobVO.setTempoExecucao(stopwatch.getElapsedTicks());
				registroExecucaoJobVO.setDataTermino(new Date());
				
				getFacadeFactory().getRegistroExecucaoJobFacade().incluirRegistroExecucaoJob(registroExecucaoJobVO, usuarioVO);
				progressBarVO.setForcarEncerramento(true);
				progressBarVO.encerrar();
				getAplicacaoControle().getMapThreadIndiceReajuste().remove("IndiceReajustePeriodo"+indiceReajustePeriodoVO.getCodigo());
			}
		}else {
			indiceReajustePeriodoVO.setLogAlunosNaoDevemSofrerReajuste(indiceReajustePeriodoVO2.getLogAlunosNaoDevemSofrerReajuste());	
			indiceReajustePeriodoVO.setLogErroProcessamento(indiceReajustePeriodoVO2.getLogErroProcessamento());	
		}
		
	}
	}
	
	private ConsistirException realizarReajustePrecoContaReceberOuRecebidaMesesAnteriorBolsaCusteadaConvenio(final ProgressBarVO progressBarVO, final ConsistirException ex, IndiceReajusteVO indiceReajusteVO, IndiceReajustePeriodoVO indiceReajustePeriodoVO, Integer numeroMaximoParcelaCondicao, HashMap<String, String> mapMatriculasNaoPodemSofrerReajustePrecoVOs, UsuarioVO usuarioVO) throws Exception {
		BigDecimal percentualReajuste = indiceReajustePeriodoVO.getPercentualReajuste();
		Integer codigoIndiceReajuste = indiceReajustePeriodoVO.getCodigo();
		Integer qtdeVezesReajustar = numeroMaximoParcelaCondicao / 12;
		Integer parcelaInicialReajuste = 13;
		List<ContaReceberVO> listaContaReceberBolsaCusteadaConvenio = new ArrayList<ContaReceberVO>();
		for (int i = 0; i < qtdeVezesReajustar; i++) {
			progressBarVO.setStatus("Consultando Bolsas Custeados Convenios aptas para reajuste de preço");
			listaContaReceberBolsaCusteadaConvenio.addAll(getFacadeFactory().getContaReceberFacade().consultarBolsaCusteadaConveioParcelaReajusteAReceberAptasParaAplicacaoReajuste(indiceReajusteVO.getCodigo(), Integer.parseInt(indiceReajustePeriodoVO.getMes().getKey()), indiceReajustePeriodoVO.getAno(), parcelaInicialReajuste, 0,indiceReajustePeriodoVO.getConsiderarDescontoSemValidadeCalculoIndiceReajuste(), false, usuarioVO));
			parcelaInicialReajuste = parcelaInicialReajuste + 12;
		}
		progressBarVO.setMaxValue(progressBarVO.getMaxValue() - 500 +listaContaReceberBolsaCusteadaConvenio.size()+4000);
		int tamanhoLista = listaContaReceberBolsaCusteadaConvenio.size();
		ProcessarParalelismo.executar(0, listaContaReceberBolsaCusteadaConvenio.size(), ex, new ProcessarParalelismo.Processo() {
			int cont = 1;
			@Override
			@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
			public void run(int i) {
				final ContaReceberVO contaReceberVO = listaContaReceberBolsaCusteadaConvenio.get(i);
				try {
					if (contaReceberVO.getParceiroVO().getRealizarReajustePrecoComBaseIndiceReajuste()) {
						progressBarVO.setStatus("Realizando reajuste de preço da matrícula "+contaReceberVO.getMatriculaAluno().getMatricula() +" "+ cont+"/"+tamanhoLista);
						if (contaReceberVO.getSituacaoAReceber()) {
	
							// REGRA PARA CONTA GERADA
							contaReceberVO.setConsiderarDescontoSemValidadeCalculoIndiceReajuste(indiceReajustePeriodoVO.getConsiderarDescontoSemValidadeCalculoIndiceReajuste());
							BigDecimal valorIndiceReajuste = new BigDecimal(0);
							if(indiceReajustePeriodoVO.getConsiderarDescontoSemValidadeCalculoIndiceReajuste()) {
								valorIndiceReajuste = BigDecimal.valueOf(contaReceberVO.getValorBaseContaReceber() - contaReceberVO.getValorDescontoSemValidade()).multiply(percentualReajuste).divide(BigDecimal.valueOf(100L));
							}else {
								valorIndiceReajuste = BigDecimal.valueOf(contaReceberVO.getValorBaseContaReceber()).multiply(percentualReajuste).divide(BigDecimal.valueOf(100L));
							}
							IndiceReajustePeriodoMatriculaPeriodoVencimentoVO indicePeriodoVencimentoIncluirVO = getFacadeFactory().getIndiceReajustePeriodoMatriculaPeriodoVencimentoFacade().inicializarDadosIndiceReajustePeriodoMatriculaPeriodoVencimento(codigoIndiceReajuste, SituacaoExecucaoEnum.PROCESSADO, contaReceberVO.getTipoOrigem(),  contaReceberVO, valorIndiceReajuste, null, "", usuarioVO);
							getFacadeFactory().getIndiceReajustePeriodoMatriculaPeriodoVencimentoFacade().incluir(indicePeriodoVencimentoIncluirVO, usuarioVO);
	
							contaReceberVO.setValorIndiceReajuste(valorIndiceReajuste);
							contaReceberVO.setValorBaseContaReceber(contaReceberVO.getValorBaseContaReceber() + valorIndiceReajuste.doubleValue());
							contaReceberVO.setValor(contaReceberVO.getValor() + valorIndiceReajuste.doubleValue());
							List<ContaReceberVO> listaContaReceberVOs = new ArrayList<ContaReceberVO>(0);
							listaContaReceberVOs.add(contaReceberVO);
							getFacadeFactory().getContaReceberFacade().alterarValorBaseContaReceber(contaReceberVO.getCodigo(), contaReceberVO.getValorBaseContaReceber(), usuarioVO);
							getFacadeFactory().getContaReceberFacade().alterarValor(contaReceberVO, usuarioVO);
							getFacadeFactory().getContaReceberFacade().alterarValorIndiceReajusteContaReceber(contaReceberVO.getCodigo(), contaReceberVO.getValorIndiceReajuste(), null, usuarioVO);
							getFacadeFactory().getContaReceberFacade().realizarProcessamentoValorFinalContaReceberAtualizadoComAcrescimosEDescontos(null, listaContaReceberVOs, false, "", true, usuarioVO, true, false , null , null, true);
							if(contaReceberVO.getPossuiRemessa()) {
								ContaCorrenteVO contaCorrente = consultarContaCorrente(contaReceberVO.getContaCorrenteVO().getCodigo(),usuarioVO);
								getFacadeFactory().getContaReceberFacade().processarRegerarNossoNumeroContaReceber(contaReceberVO, contaCorrente, usuarioVO);
							}
							
						}else if(contaReceberVO.getSituacaoNegociado()) {
							// REGRA PARA CONTA NÃO GERADA
							BigDecimal valorIndiceReajuste = new BigDecimal(0);
							if(indiceReajustePeriodoVO.getConsiderarDescontoSemValidadeCalculoIndiceReajuste()) {
								valorIndiceReajuste = BigDecimal.valueOf(contaReceberVO.getValor() - contaReceberVO.getValorDescontoSemValidade()).multiply(percentualReajuste).divide(BigDecimal.valueOf(100L));
							}else {
								valorIndiceReajuste = BigDecimal.valueOf(contaReceberVO.getValor()).multiply(percentualReajuste).divide(BigDecimal.valueOf(100L));
							}
	
							ContaReceberVO contaReceberASerGeradaVO = new ContaReceberVO();
							contaReceberASerGeradaVO.getMatriculaAluno().setMatricula(contaReceberVO.getMatriculaAluno().getMatricula());
							contaReceberASerGeradaVO.setParcela(contaReceberVO.getParcela());
							contaReceberASerGeradaVO.setMatriculaPeriodo(contaReceberVO.getMatriculaPeriodo());
							contaReceberASerGeradaVO.setDataVencimento(contaReceberVO.getDataVencimento());
							contaReceberASerGeradaVO.setValor(contaReceberVO.getValor());
							contaReceberASerGeradaVO.setValorBaseContaReceber(contaReceberVO.getValor());
							contaReceberASerGeradaVO.setValorDescontoSemValidade(contaReceberVO.getValorDescontoSemValidade());
							contaReceberASerGeradaVO.setConsiderarDescontoSemValidadeCalculoIndiceReajuste(indiceReajustePeriodoVO.getConsiderarDescontoSemValidadeCalculoIndiceReajuste());
	
							IndiceReajustePeriodoMatriculaPeriodoVencimentoVO indicePeriodoVencimentoIncluirVO = getFacadeFactory().getIndiceReajustePeriodoMatriculaPeriodoVencimentoFacade().inicializarDadosIndiceReajustePeriodoMatriculaPeriodoVencimento(codigoIndiceReajuste, SituacaoExecucaoEnum.PROCESSADO,contaReceberVO.getTipoOrigem(), contaReceberASerGeradaVO, valorIndiceReajuste, null, "", usuarioVO);
							getFacadeFactory().getIndiceReajustePeriodoMatriculaPeriodoVencimentoFacade().incluir(indicePeriodoVencimentoIncluirVO, usuarioVO);
						}
					} else {
						// CRIAR LOG DAS MATRÍCULAS QUE NÃO DEVE SER REAJUSTADA.
						String matricula =contaReceberVO.getMatriculaAluno().getMatricula();
						if (!mapMatriculasNaoPodemSofrerReajustePrecoVOs.containsKey(matricula)) {
							mapMatriculasNaoPodemSofrerReajustePrecoVOs.put(matricula, matricula);
						}
					}

				} catch (ConsistirException e) {
					ex.adicionarListaMensagemErro("Erro ao processar matrícula " + contaReceberVO.getMatriculaAluno().getMatricula() + " - " + e.getMessage() + ".");
				} catch (Exception e) {
					ex.adicionarListaMensagemErro("Erro ao processar matrícula " + contaReceberVO.getMatriculaAluno().getMatricula() + " - " + e.getMessage() + ".");
				}finally {
					progressBarVO.incrementar();
				}
				cont++;
			}
		});
		
		return ex;		
	}
	
	
	/**
	 * MÉTODO RESPONSÁVEL POR REAJUSTAR AS PARCELAS POSTERIOR ONDE A CONTA DA DATA
	 * DO REAJUSTE POR EXEMPLO A 13º ESTEJA RECEBIDA OU ENVIADA PARA REMESSA. NESSE
	 * CASO ONDE POSSUA PARCELAS 14º/15º/16º SERÃO ATUALIZADAS INCLUINDO TAMBÉM O
	 * REAJUSTE DA 13º PARA A PRIMEIRA APÓS ELA, NESSE EXEMPLO SERIA A 14º.
	 * 
	 * @param indiceReajusteVO
	 * @param indiceReajustePeriodoVO
	 * @param numeroMaximoParcelaCondicao
	 * @param usuarioVO
	 * @throws Exception
	 * @author CarlosEugenio
	 * @throws ConsistirException
	 */
	
	public ConsistirException realizarReajustePrecoContaReceberRecebidaOuEnviadaRemessaBolsaCusteadaConvenio(final ProgressBarVO progressBarVO, final ConsistirException ex, IndiceReajusteVO indiceReajusteVO, final IndiceReajustePeriodoVO indiceReajustePeriodoVO, Integer numeroMaximoParcelaCondicao, HashMap<String, String> mapMatriculasNaoPodemSofrerReajustePrecoVOs, UsuarioVO usuarioVO) throws ConsistirException, Exception {
		
		Integer qtdeVezesReajustar = numeroMaximoParcelaCondicao / 12;		
		indiceReajustePeriodoVO.setParcelaInicialReajuste(13);
		List<ContaReceberVO> listaContaReceberRecebidaOuEnviadaRemessaVOs = new ArrayList<ContaReceberVO>(0);
		
		for (int i = 0; i < qtdeVezesReajustar; i++) {
			
			// CONSULTA AS CONTAS RECEBIDAS E OU ENVIADAS PARA REMESSA COM DATA DE PAGAMENTO
			// IGUAL OU POSTERIOR
			// AO PERÍODO DO REAJUSTE
			progressBarVO.setStatus("Consultando contas de Bolsa Custeadas Convenios recebidas.");
			listaContaReceberRecebidaOuEnviadaRemessaVOs.addAll(getFacadeFactory().getContaReceberFacade().consultarContaReceberRecebidasOuEnviadasParaRemessaParaAplicacaoReajustePosteriorEmOutraContaBolsaCusteadaConvenio(indiceReajusteVO.getCodigo(), Integer.parseInt(indiceReajustePeriodoVO.getMes().getKey()), indiceReajustePeriodoVO.getAno(), indiceReajustePeriodoVO.getParcelaInicialReajuste(), usuarioVO));
			final HashMap<Integer, List<ContaReceberVO>> mapMatriculaPeriodoValorReajusteVOs = new HashMap<Integer, List<ContaReceberVO>>(0);			
			for (ContaReceberVO contaReceberVO : listaContaReceberRecebidaOuEnviadaRemessaVOs) {				
				if (contaReceberVO.getMatriculaAluno().getPermiteExecucaoReajustePreco()) {					
					BigDecimal valorIndiceReajuste = BigDecimal.valueOf(contaReceberVO.getValorBaseContaReceber()).multiply(indiceReajustePeriodoVO.getPercentualReajuste()).divide(BigDecimal.valueOf(100L));
					if (mapMatriculaPeriodoValorReajusteVOs.containsKey(contaReceberVO.getMatriculaPeriodo())) {
						contaReceberVO.setValorIndiceReajuste(valorIndiceReajuste);
						List<ContaReceberVO> listaContaReceberVOs = mapMatriculaPeriodoValorReajusteVOs.get(contaReceberVO.getMatriculaPeriodo());
						listaContaReceberVOs.add(contaReceberVO);
						mapMatriculaPeriodoValorReajusteVOs.put(contaReceberVO.getMatriculaPeriodo(), listaContaReceberVOs);
					} else {
						contaReceberVO.setValorIndiceReajuste(valorIndiceReajuste);
						List<ContaReceberVO> listaContaReceberVOs = new ArrayList<ContaReceberVO>(0);
						listaContaReceberVOs.add(contaReceberVO);
						mapMatriculaPeriodoValorReajusteVOs.put(contaReceberVO.getMatriculaPeriodo(), listaContaReceberVOs);
					}
					
				} else {
					if (!mapMatriculasNaoPodemSofrerReajustePrecoVOs.containsKey(contaReceberVO.getMatriculaAluno().getMatricula())) {
						mapMatriculasNaoPodemSofrerReajustePrecoVOs.put(contaReceberVO.getMatriculaAluno().getMatricula(), contaReceberVO.getMatriculaAluno().getMatricula());
					}
				}
			}
			realizarProcessamentoValorReajusteDiferencaParcelaRecebidaNegociada(mapMatriculaPeriodoValorReajusteVOs, indiceReajustePeriodoVO, "BCC", ex, progressBarVO, usuarioVO);
			indiceReajustePeriodoVO.setParcelaInicialReajuste(indiceReajustePeriodoVO.getParcelaInicialReajuste()+12);
		}
		return ex;
		
	}
	
	public void inicializarDadosLogReajustePreco(IndiceReajustePeriodoVO indiceReajustePeriodoVO, HashMap<String, String> mapMatriculasNaoPodemSofrerReajustePrecoVOs, UsuarioVO usuarioVO) throws Exception {
		StringBuilder logAlunosNaoDevemSofrerReajuste = null;
		for (String matricula : mapMatriculasNaoPodemSofrerReajustePrecoVOs.values()) {
			if (logAlunosNaoDevemSofrerReajuste == null) {
				logAlunosNaoDevemSofrerReajuste = new StringBuilder();
			}
			logAlunosNaoDevemSofrerReajuste.append("O Aluno de Matrícula: ").append(matricula).append(" ");
			logAlunosNaoDevemSofrerReajuste.append("não sofreu Reajuste \n");
		}
		if (logAlunosNaoDevemSofrerReajuste != null) {
			alterarLogAlunosNaoDevemSofrerReajusteIndiceReajustePeriodo(indiceReajustePeriodoVO.getCodigo(), logAlunosNaoDevemSofrerReajuste.toString(), usuarioVO);
			indiceReajustePeriodoVO.setLogAlunosNaoDevemSofrerReajuste(logAlunosNaoDevemSofrerReajuste.toString());
		}
		
		if (!indiceReajustePeriodoVO.getLogErroProcessamento().equals("")) {
			alterarLogErroIndiceReajustePeriodo(indiceReajustePeriodoVO.getCodigo(), indiceReajustePeriodoVO.getLogErroProcessamento(), usuarioVO);
		}
		
	}

	/**
	 * PROCESSA AS CONTAS A RECEBER E A QUE FOI RECEBIDA EM MESES ANTERIOR POR
	 * EXEMPLO SE UMA CONTA DE PARCELA 13 FOI RECEBIDA EM DEZEMBRO DE 2017 COM DATA
	 * DE VENCIMENTO PARA JANEIRO DE 2018 ELA NÃO SERÁ PROCESSADA PORÉM AS DEMAIS
	 * PARCELAS DEVERÃO RECEBER O REAJUSTE SEM O ACRÉSCIMO DA PARCELA 13.
	 * 
	 * @param indiceReajusteVO
	 * @param indiceReajustePeriodoVO
	 * @param numeroMaximoParcelaCondicao
	 * @param usuarioVO
	 * @throws Exception
	 * @author CarlosEugenio
	 */
//	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public ConsistirException realizarReajustePrecoContaReceberOuRecebidaMesesAnterior(final ProgressBarVO progressBarVO, final ConsistirException ex, IndiceReajusteVO indiceReajusteVO, IndiceReajustePeriodoVO indiceReajustePeriodoVO, Integer numeroMaximoParcelaCondicao, HashMap<String, String> mapMatriculasNaoPodemSofrerReajustePrecoVOs, UsuarioVO usuarioVO) throws Exception {
		BigDecimal percentualReajuste = indiceReajustePeriodoVO.getPercentualReajuste();
		Integer codigoIndiceReajuste = indiceReajustePeriodoVO.getCodigo();
		Integer qtdeVezesReajustar = numeroMaximoParcelaCondicao / 12;
		Integer parcelaInicialReajuste = 13;
		List<MatriculaPeriodoVencimentoVO> listaMatriculaPeriodoVencimentoVOs = new ArrayList<MatriculaPeriodoVencimentoVO>(0);
		for (int i = 0; i < qtdeVezesReajustar; i++) {
			progressBarVO.setStatus("Consultando alunos aptos para reajuste de preço de parcelas a receber");
			listaMatriculaPeriodoVencimentoVOs.addAll(getFacadeFactory().getMatriculaPeriodoVencimentoFacade().consultarMatriculaPeriodoVencimentoParcelaReajusteAReceberAptasParaAplicacaoReajuste(indiceReajusteVO.getCodigo(), Integer.parseInt(indiceReajustePeriodoVO.getMes().getKey()), indiceReajustePeriodoVO.getAno(), parcelaInicialReajuste, 0,indiceReajustePeriodoVO.getConsiderarDescontoSemValidadeCalculoIndiceReajuste(), false, usuarioVO));
			parcelaInicialReajuste = parcelaInicialReajuste + 12;
		}
		
		progressBarVO.setMaxValue(1000+listaMatriculaPeriodoVencimentoVOs.size()+2000);
		int tamanhoLista = listaMatriculaPeriodoVencimentoVOs.size();
		ProcessarParalelismo.executar(0, listaMatriculaPeriodoVencimentoVOs.size(), ex, new ProcessarParalelismo.Processo() {
			int cont = 1;
			@Override
			@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
			public void run(int i) {
				final MatriculaPeriodoVencimentoVO matriculaPeriodoVencimentoVO = listaMatriculaPeriodoVencimentoVOs.get(i);
				try {
					if (matriculaPeriodoVencimentoVO.getMatriculaPeriodoVO().getMatriculaVO().getPermiteExecucaoReajustePreco()) {
						progressBarVO.setStatus("Realizando reajuste de preço da matrícula "+matriculaPeriodoVencimentoVO.getMatriculaPeriodoVO().getMatriculaVO().getMatricula()+" "+ cont+"/"+tamanhoLista);
						if (matriculaPeriodoVencimentoVO.getSituacao().equals(SituacaoVencimentoMatriculaPeriodo.CONTARECEBER_GERADA) && !matriculaPeriodoVencimentoVO.getContaReceber().getSituacao().equals(SituacaoContaReceber.CANCELADO_FINANCEIRO.getValor())) {
	
							// REGRA PARA CONTA GERADA
							ContaReceberVO contaReceberVO = matriculaPeriodoVencimentoVO.getContaReceber();
							contaReceberVO.setConsiderarDescontoSemValidadeCalculoIndiceReajuste(indiceReajustePeriodoVO.getConsiderarDescontoSemValidadeCalculoIndiceReajuste());
							BigDecimal valorIndiceReajuste = new BigDecimal(0);
							if(indiceReajustePeriodoVO.getConsiderarDescontoSemValidadeCalculoIndiceReajuste()) {
								valorIndiceReajuste = BigDecimal.valueOf(contaReceberVO.getValorBaseContaReceber() - contaReceberVO.getValorDescontoSemValidade()).multiply(percentualReajuste).divide(BigDecimal.valueOf(100L));
							}else {
								valorIndiceReajuste = BigDecimal.valueOf(contaReceberVO.getValorBaseContaReceber()).multiply(percentualReajuste).divide(BigDecimal.valueOf(100L));
							}
							IndiceReajustePeriodoMatriculaPeriodoVencimentoVO indicePeriodoVencimentoIncluirVO = getFacadeFactory().getIndiceReajustePeriodoMatriculaPeriodoVencimentoFacade().inicializarDadosIndiceReajustePeriodoMatriculaPeriodoVencimento(codigoIndiceReajuste, SituacaoExecucaoEnum.PROCESSADO, matriculaPeriodoVencimentoVO.getTipoOrigemMatriculaPeriodoVencimento().toString(), contaReceberVO, valorIndiceReajuste, null, "", usuarioVO);
							getFacadeFactory().getIndiceReajustePeriodoMatriculaPeriodoVencimentoFacade().incluir(indicePeriodoVencimentoIncluirVO, usuarioVO);
	
							contaReceberVO.setValorIndiceReajuste(valorIndiceReajuste);
							contaReceberVO.setValorBaseContaReceber(contaReceberVO.getValorBaseContaReceber() + valorIndiceReajuste.doubleValue());
							contaReceberVO.setValor(contaReceberVO.getValor() + valorIndiceReajuste.doubleValue());
							List<ContaReceberVO> listaContaReceberVOs = new ArrayList<ContaReceberVO>(0);
							listaContaReceberVOs.add(contaReceberVO);
							getFacadeFactory().getContaReceberFacade().alterarValorBaseContaReceber(contaReceberVO.getCodigo(), contaReceberVO.getValorBaseContaReceber(), usuarioVO);
							getFacadeFactory().getContaReceberFacade().alterarValor(contaReceberVO, usuarioVO);
							getFacadeFactory().getContaReceberFacade().alterarValorIndiceReajusteContaReceber(contaReceberVO.getCodigo(), contaReceberVO.getValorIndiceReajuste(), null, usuarioVO);
							getFacadeFactory().getContaReceberFacade().realizarProcessamentoValorFinalContaReceberAtualizadoComAcrescimosEDescontos(null, listaContaReceberVOs, false, "", true, usuarioVO, true, false , null , null, true);
							
							if(contaReceberVO.getPossuiRemessa()) {
								ContaCorrenteVO contaCorrente = consultarContaCorrente(contaReceberVO.getContaCorrenteVO().getCodigo(),usuarioVO);
								getFacadeFactory().getContaReceberFacade().processarRegerarNossoNumeroContaReceber(contaReceberVO, contaCorrente, usuarioVO);
							}
							
						} else if (matriculaPeriodoVencimentoVO.getSituacao().equals(SituacaoVencimentoMatriculaPeriodo.CONTARECEBER_NAO_GERADA)) {
	
							// REGRA PARA CONTA NÃO GERADA 
							BigDecimal valorIndiceReajuste = new BigDecimal(0);
							if(indiceReajustePeriodoVO.getConsiderarDescontoSemValidadeCalculoIndiceReajuste()) {
								valorIndiceReajuste = BigDecimal.valueOf(matriculaPeriodoVencimentoVO.getValor() - matriculaPeriodoVencimentoVO.getContaReceber().getValorDescontoSemValidade()).multiply(percentualReajuste).divide(BigDecimal.valueOf(100L));
							}else {
								valorIndiceReajuste = BigDecimal.valueOf(matriculaPeriodoVencimentoVO.getValor()).multiply(percentualReajuste).divide(BigDecimal.valueOf(100L));
							}
	
							ContaReceberVO contaReceberASerGeradaVO = new ContaReceberVO();
							contaReceberASerGeradaVO.getMatriculaAluno().setMatricula(matriculaPeriodoVencimentoVO.getMatriculaPeriodoVO().getMatricula());
							contaReceberASerGeradaVO.setParcela(matriculaPeriodoVencimentoVO.getParcela());
							contaReceberASerGeradaVO.setMatriculaPeriodo(matriculaPeriodoVencimentoVO.getMatriculaPeriodoVO().getCodigo());
							contaReceberASerGeradaVO.setDataVencimento(matriculaPeriodoVencimentoVO.getDataVencimento());
							contaReceberASerGeradaVO.setValor(matriculaPeriodoVencimentoVO.getValor());
							contaReceberASerGeradaVO.setValorBaseContaReceber(matriculaPeriodoVencimentoVO.getValor());
							contaReceberASerGeradaVO.setValorDescontoSemValidade(matriculaPeriodoVencimentoVO.getContaReceber().getValorDescontoSemValidade());
							contaReceberASerGeradaVO.setConsiderarDescontoSemValidadeCalculoIndiceReajuste(indiceReajustePeriodoVO.getConsiderarDescontoSemValidadeCalculoIndiceReajuste());
	
							IndiceReajustePeriodoMatriculaPeriodoVencimentoVO indicePeriodoVencimentoIncluirVO = getFacadeFactory().getIndiceReajustePeriodoMatriculaPeriodoVencimentoFacade().inicializarDadosIndiceReajustePeriodoMatriculaPeriodoVencimento(codigoIndiceReajuste, SituacaoExecucaoEnum.PROCESSADO,matriculaPeriodoVencimentoVO.getTipoOrigemMatriculaPeriodoVencimento().toString(), contaReceberASerGeradaVO, valorIndiceReajuste, null, "", usuarioVO);
							getFacadeFactory().getIndiceReajustePeriodoMatriculaPeriodoVencimentoFacade().incluir(indicePeriodoVencimentoIncluirVO, usuarioVO);
	
						}
					
					} else {
						// CRIAR LOG DAS MATRÍCULAS QUE NÃO DEVE SER REAJUSTADA.
						String matricula = matriculaPeriodoVencimentoVO.getMatriculaPeriodoVO().getMatriculaVO().getMatricula();
						if (!mapMatriculasNaoPodemSofrerReajustePrecoVOs.containsKey(matricula)){
							mapMatriculasNaoPodemSofrerReajustePrecoVOs.put(matricula, matricula);
						}
					}
					

				} catch (ConsistirException e) {
					ex.adicionarListaMensagemErro("Erro ao processar matrícula " + matriculaPeriodoVencimentoVO.getMatriculaPeriodoVO().getMatricula() + " - " + e.getMessage() + ".");
				} catch (Exception e) {
					ex.adicionarListaMensagemErro("Erro ao processar matrícula " + matriculaPeriodoVencimentoVO.getMatriculaPeriodoVO().getMatricula() + " - " + e.getMessage() + ".");
				}finally {
					progressBarVO.incrementar();
				}
				cont ++;
			}
		});
		
		return ex;		
		
	}

	/**
	 * MÉTODO RESPONSÁVEL POR REAJUSTAR AS PARCELAS POSTERIOR ONDE A CONTA DA DATA
	 * DO REAJUSTE POR EXEMPLO A 13º ESTEJA RECEBIDA OU ENVIADA PARA REMESSA. NESSE
	 * CASO ONDE POSSUA PARCELAS 14º/15º/16º SERÃO ATUALIZADAS INCLUINDO TAMBÉM O
	 * REAJUSTE DA 13º PARA A PRIMEIRA APÓS ELA, NESSE EXEMPLO SERIA A 14º.
	 * 
	 * @param indiceReajusteVO
	 * @param indiceReajustePeriodoVO
	 * @param numeroMaximoParcelaCondicao
	 * @param usuarioVO
	 * @throws Exception
	 * @author CarlosEugenio
	 * @throws ConsistirException
	 */
	
	public ConsistirException realizarReajustePrecoContaReceberRecebidaOuEnviadaRemessa(final ProgressBarVO progressBarVO, final ConsistirException ex, IndiceReajusteVO indiceReajusteVO, final IndiceReajustePeriodoVO indiceReajustePeriodoVO, Integer numeroMaximoParcelaCondicao, HashMap<String, String> mapMatriculasNaoPodemSofrerReajustePrecoVOs, UsuarioVO usuarioVO) throws ConsistirException, Exception {
		BigDecimal percentualReajuste = indiceReajustePeriodoVO.getPercentualReajuste();
		Integer qtdeVezesReajustar = numeroMaximoParcelaCondicao / 12;

		indiceReajustePeriodoVO.setParcelaInicialReajuste(13);
		List<ContaReceberVO> listaContaReceberRecebidaOuEnviadaRemessaVOs = new ArrayList<ContaReceberVO>(0);
		
		for (int i = 0; i < qtdeVezesReajustar; i++) {
			
			// CONSULTA AS CONTAS RECEBIDAS E OU ENVIADAS PARA REMESSA COM DATA DE PAGAMENTO
			// IGUAL OU POSTERIOR
			// AO PERÍODO DO REAJUSTE
			progressBarVO.setStatus("Consultando contas de alunos com contas recebidas.");
			listaContaReceberRecebidaOuEnviadaRemessaVOs.addAll(getFacadeFactory().getContaReceberFacade().consultarContaReceberRecebidasOuEnviadasParaRemessaParaAplicacaoReajustePosteriorEmOutraConta(indiceReajusteVO.getCodigo(), Integer.parseInt(indiceReajustePeriodoVO.getMes().getKey()), indiceReajustePeriodoVO.getAno(), indiceReajustePeriodoVO.getParcelaInicialReajuste(), indiceReajustePeriodoVO.getConsiderarDescontoSemValidadeCalculoIndiceReajuste(),  usuarioVO));
			final HashMap<Integer, List<ContaReceberVO>> mapMatriculaPeriodoValorReajusteVOs = new HashMap<Integer, List<ContaReceberVO>>(0);			
			for (ContaReceberVO contaReceberVO : listaContaReceberRecebidaOuEnviadaRemessaVOs) {				
				if (contaReceberVO.getMatriculaAluno().getPermiteExecucaoReajustePreco()) {					
					BigDecimal valorIndiceReajuste = new BigDecimal(0);
					if(indiceReajustePeriodoVO.getConsiderarDescontoSemValidadeCalculoIndiceReajuste()) {
						valorIndiceReajuste = BigDecimal.valueOf(contaReceberVO.getValorBaseContaReceber() - contaReceberVO.getValorDescontoSemValidade()).multiply(percentualReajuste).divide(BigDecimal.valueOf(100L));
					}else {
						valorIndiceReajuste = BigDecimal.valueOf(contaReceberVO.getValorBaseContaReceber()).multiply(percentualReajuste).divide(BigDecimal.valueOf(100L));
					}
					
					if (mapMatriculaPeriodoValorReajusteVOs.containsKey(contaReceberVO.getMatriculaPeriodo())) {
						contaReceberVO.setValorIndiceReajuste(valorIndiceReajuste);
						List<ContaReceberVO> listaContaReceberVOs = mapMatriculaPeriodoValorReajusteVOs.get(contaReceberVO.getMatriculaPeriodo());
						listaContaReceberVOs.add(contaReceberVO);
						mapMatriculaPeriodoValorReajusteVOs.put(contaReceberVO.getMatriculaPeriodo(), listaContaReceberVOs);
					} else {
						contaReceberVO.setValorIndiceReajuste(valorIndiceReajuste);
						List<ContaReceberVO> listaContaReceberVOs = new ArrayList<ContaReceberVO>(0);
						listaContaReceberVOs.add(contaReceberVO);
						mapMatriculaPeriodoValorReajusteVOs.put(contaReceberVO.getMatriculaPeriodo(), listaContaReceberVOs);
					}
					
				} else {
					if (!mapMatriculasNaoPodemSofrerReajustePrecoVOs.containsKey(contaReceberVO.getMatriculaAluno().getMatricula())) {
						mapMatriculasNaoPodemSofrerReajustePrecoVOs.put(contaReceberVO.getMatriculaAluno().getMatricula(), contaReceberVO.getMatriculaAluno().getMatricula());
					}
				}
				
			}			
			realizarProcessamentoValorReajusteDiferencaParcelaRecebidaNegociada(mapMatriculaPeriodoValorReajusteVOs, indiceReajustePeriodoVO, "MENSALIDADE", ex, progressBarVO, usuarioVO);
			indiceReajustePeriodoVO.setParcelaInicialReajuste(indiceReajustePeriodoVO.getParcelaInicialReajuste()+12);
		}
		return ex;
		
	}
	
	private void realizarProcessamentoValorReajusteDiferencaParcelaRecebidaNegociada(final HashMap<Integer, List<ContaReceberVO>> mapMatriculaPeriodoValorReajusteVOs, final IndiceReajustePeriodoVO indiceReajustePeriodoVO, final String tipoOrigemContaReceber, final ConsistirException ex,  final ProgressBarVO progressBarVO, final UsuarioVO usuarioVO) throws Exception { 
	List<Integer> matriculaPeriodoIntegers =  new ArrayList<Integer>();
	matriculaPeriodoIntegers.addAll(mapMatriculaPeriodoValorReajusteVOs.keySet());
	progressBarVO.setMaxValue(progressBarVO.getMaxValue() - 2000 + matriculaPeriodoIntegers.size() + 500);
	final int tamanhoLista = matriculaPeriodoIntegers.size();
	ProcessarParalelismo.executar(0, matriculaPeriodoIntegers.size(), ex, new ProcessarParalelismo.Processo() {
		int cont = 1;
		@Override
		@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
		public void run(int i) {
			
			Integer matriculaPeriodo = matriculaPeriodoIntegers.get(i);
			StringBuilder logErroProcessamento = null;
				progressBarVO.setStatus("Consultando contas de alunos aptas a realizar reajuste de preço - lote "+i+"."+ cont+"/"+tamanhoLista);
				String matricula = null;
				String parcela = null;
				String dataVencimento = null;
				try {
					
					BigDecimal valorIndiceReajusteDiferencaValorParcelaRecebida = BigDecimal.ZERO;
					StringBuilder observacaoDiferenca = new StringBuilder();
					List<ContaReceberVO> listaContaReceberRecebidaOuEnviadaRemessaCalcularReajusteVOs = mapMatriculaPeriodoValorReajusteVOs.get(matriculaPeriodo);
					for (ContaReceberVO contaReceberRecebidaOuEnviadaRemessaVO : listaContaReceberRecebidaOuEnviadaRemessaCalcularReajusteVOs) {								
						valorIndiceReajusteDiferencaValorParcelaRecebida = valorIndiceReajusteDiferencaValorParcelaRecebida.add(contaReceberRecebidaOuEnviadaRemessaVO.getValorIndiceReajuste());
						inicializarDadosObservacaoDiferencaValorRecebidoOuEnviadoRemessa(contaReceberRecebidaOuEnviadaRemessaVO, observacaoDiferenca, usuarioVO);
					}
					if(!valorIndiceReajusteDiferencaValorParcelaRecebida.equals(BigDecimal.ZERO)) {
					SqlRowSet rs = consultarIndiceReajusteMatriculaPeriodoVencimentoAptoReceberDiferencaValorContaRecebidaNegociada(matriculaPeriodo, indiceReajustePeriodoVO.getCodigo(), tipoOrigemContaReceber);
					if(rs.next()) {
						Integer codigoIRPMPV = rs.getInt("indicereajusteperiodomatriculaperiodovencimento");
						matricula = rs.getString("matricula");
						parcela = rs.getString("parcela");
						dataVencimento = Uteis.getData(rs.getDate("dataVencimento"));
						Integer codigoContaReceber = rs.getInt("contareceber");
						
						gravarValorReajusteDiferencaParcelaRecebidaNegociada(codigoIRPMPV, observacaoDiferenca.toString(),  codigoContaReceber, valorIndiceReajusteDiferencaValorParcelaRecebida);
						if(Uteis.isAtributoPreenchido(codigoContaReceber)) {
							ContaReceberVO cr =  new ContaReceberVO();
							cr.setCodigo(codigoContaReceber);
							List<ContaReceberVO> contaReceberVOs =  new ArrayList<ContaReceberVO>(0);
							contaReceberVOs.add(cr);
							getFacadeFactory().getContaReceberFacade().realizarProcessamentoValorFinalContaReceberAtualizadoComAcrescimosEDescontos(null, contaReceberVOs, false, "", true, usuarioVO, true , false , null , null, true);
						}
					}else {
						// CRIAR PARCELA EXTRA
					}
					}
				} catch (Exception e) {
					if (logErroProcessamento == null) {
						logErroProcessamento = new StringBuilder();
					}
					logErroProcessamento.append("Erro ao processar a parcela ").append(parcela).append(", \n");
					logErroProcessamento.append(" Matrícula: ").append(matricula).append(" \n");
					logErroProcessamento.append(" Data de Vencimento: ").append(dataVencimento);
					ex.adicionarListaMensagemErro(logErroProcessamento.toString());
				}
				progressBarVO.incrementar();
				cont ++;
	}

	});
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void gravarValorReajusteDiferencaParcelaRecebidaNegociada(Integer codigoIRPMPV, String observacaoDiferencaParcelaRecebidaNegociada, Integer codigoContaReceber, BigDecimal valor) {
		getConexao().getJdbcTemplate().update(" update indicereajusteperiodomatriculaperiodovencimento set valorreferentediferencaparcelarecebidaouenviadaremessa =?, observacaodiferencaparcelarecebidaouenviadaremessa = ? where codigo = ? ",new Object[] {valor, observacaoDiferencaParcelaRecebidaNegociada,codigoIRPMPV});
		if(Uteis.isAtributoPreenchido(codigoContaReceber)) {
			getConexao().getJdbcTemplate().update(" update contareceber set valorreajustediferencaparcelarecebidaouenviadaremessa =? where codigo = ? ",new Object[] { valor, codigoContaReceber});
		}
	}
	
	private SqlRowSet consultarIndiceReajusteMatriculaPeriodoVencimentoAptoReceberDiferencaValorContaRecebidaNegociada(Integer matriculaPeriodo, Integer indiceReajustePeriodo, String tipoOrigemContaReceber) {
		StringBuilder sql  =  new StringBuilder("");
		sql.append(" select indicereajusteperiodomatriculaperiodovencimento.codigo as indicereajusteperiodomatriculaperiodovencimento, contareceber.codigo as contareceber, indicereajusteperiodomatriculaperiodovencimento.parcela, matriculaperiodo.matricula, case when contareceber.codigo is not null then contareceber.datavencimento else matriculaperiodovencimento.datavencimento end as datavencimento ");
		sql.append(" from indicereajusteperiodomatriculaperiodovencimento");
		
		sql.append(" inner join matriculaperiodo on indicereajusteperiodomatriculaperiodovencimento.matriculaperiodo = matriculaperiodo.codigo ");
		sql.append(" inner join matriculaperiodovencimento on matriculaperiodovencimento.matriculaperiodo = matriculaperiodo.codigo ");
		sql.append(" and matriculaperiodovencimento.parcela ilike ('%/%') ");
		sql.append(" and (position('/' in matriculaperiodovencimento.parcela) -1) > 0");
		sql.append(" and isnumeric(substring(matriculaperiodovencimento.parcela, 1, position('/' in matriculaperiodovencimento.parcela) - 1))");
		sql.append(" and (cast(substring(matriculaperiodovencimento.parcela, 1, position('/' in matriculaperiodovencimento.parcela) - 1) as integer) ");
		sql.append(" = indicereajusteperiodomatriculaperiodovencimento.parcela::INT)");
		sql.append(" left join contareceber on matriculaperiodovencimento.contareceber is not null and contareceber.codigo = matriculaperiodovencimento.contareceber ");
		sql.append(" where indicereajusteperiodomatriculaperiodovencimento.matriculaperiodo = ? and indicereajusteperiodomatriculaperiodovencimento.situacao = 'PROCESSADO'");
		sql.append(" and matriculaperiodovencimento.tipoorigemmatriculaperiodovencimento = (?) ");
		sql.append(" and indicereajusteperiodomatriculaperiodovencimento.valorReajuste > 0");
		sql.append(" and indicereajusteperiodo = ?");
		sql.append(" order by indicereajusteperiodomatriculaperiodovencimento.parcela::INT asc limit 1 ");
		return  getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), matriculaPeriodo, tipoOrigemContaReceber, indiceReajustePeriodo);
	}
	
	private ContaCorrenteVO consultarContaCorrente(Integer codigoContaCorrente, UsuarioVO usuarioVO) throws Exception {
		return  getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(codigoContaCorrente, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
	}

	public void inicializarDadosObservacaoDiferencaValorRecebidoOuEnviadoRemessa(ContaReceberVO contaReceberRecebidaOuEnviadaRemessaVO, StringBuilder observacaoDiferenca, UsuarioVO usuarioVO) {
		observacaoDiferenca.append("Diferença referente a Parcela ").append(contaReceberRecebidaOuEnviadaRemessaVO.getParcela()).append(", ");
		observacaoDiferenca.append("Situação Parcela: ");
		if (contaReceberRecebidaOuEnviadaRemessaVO.getSituacao().equals(SituacaoContaReceber.RECEBIDO.getValor())) {
			observacaoDiferenca.append("Recebida! \n");
		} else if (contaReceberRecebidaOuEnviadaRemessaVO.getSituacao().equals(SituacaoContaReceber.NEGOCIADO.getValor())){
			observacaoDiferenca.append("Negociada! \n");
		} else {
			observacaoDiferenca.append("Enviada para Remessa! \n");
		}
	}

//	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
//	public void persistirIndiceReajustePeriodoMatriculaPeriodoVencimento(Integer indiceReajustePeriodo, ContaReceberVO contaReceberVO, BigDecimal valorIndiceReajuste, BigDecimal valorReajusteDiferencaParcelaRecebida, String observacaoDiferencaParcelaRecebidaOuEnviadaRemessa, UsuarioVO usuarioVO) throws Exception {
//		IndiceReajustePeriodoMatriculaPeriodoVencimentoVO obj = new IndiceReajustePeriodoMatriculaPeriodoVencimentoVO();
//		obj.setDataReajuste(new Date());
//		obj.setValorBaseContaReceberReajuste(BigDecimal.valueOf(contaReceberVO.getValorBaseContaReceber()));
//		obj.setValorReajuste(valorIndiceReajuste);
//		obj.getIndiceReajustePeriodoVO().setCodigo(indiceReajustePeriodo);
//		obj.setParcela(contaReceberVO.getParcela().substring(0, contaReceberVO.getParcela().indexOf("/")));
//		obj.getMatriculaPeriodoVO().setCodigo(contaReceberVO.getMatriculaPeriodo());
//		if (valorReajusteDiferencaParcelaRecebida != null && !valorReajusteDiferencaParcelaRecebida.equals(BigDecimal.ZERO)) {
//			obj.setValorReferenteDiferencaParcelaRecebidaOuEnviadaRemessa(valorReajusteDiferencaParcelaRecebida);
//			obj.setObservacaoDiferencaParcelaRecebidaOuEnviadaRemessa(observacaoDiferencaParcelaRecebidaOuEnviadaRemessa);
//		}
//		getFacadeFactory().getIndiceReajustePeriodoMatriculaPeriodoVencimentoFacade().incluir(obj, usuarioVO);
//	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Integer consultaNumeroMaximoParcelaCondicaoPagamentoPlanoFinanceiroCurso(Integer mes, String ano, UsuarioVO usuarioVO) throws Exception {
		Date dataInicioMes = Uteis.getDate("01/"+mes+"/"+ano);		
		Date dataFimMes = Uteis.getDataUltimoDiaMes(dataInicioMes);
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct max(distinct condicaopagamentoplanofinanceirocurso.nrparcelasperiodo) AS qtde from indiceReajuste ");
		sb.append(" inner join turma on turma.indiceReajuste = indiceReajuste.codigo ");
		sb.append(" inner join matriculaperiodo on matriculaperiodo.turma = turma.codigo ");
		sb.append(" inner join condicaopagamentoplanofinanceirocurso on condicaopagamentoplanofinanceirocurso.codigo = matriculaperiodo.condicaopagamentoplanofinanceirocurso ");
		sb.append(" inner join matriculaperiodovencimento on matriculaperiodovencimento.matriculaperiodo = matriculaperiodo.codigo   ");
		sb.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
		sb.append(" inner join curso on curso.codigo = matricula.curso ");
		sb.append(" inner join indiceReajustePeriodo on indiceReajustePeriodo.indiceReajuste = indiceReajuste.codigo ");
		sb.append(" where tipoorigemmatriculaperiodovencimento = 'MENSALIDADE' and matriculaperiodovencimento.situacao in('NG', 'GE', 'GP', 'NCR', 'CEM') ");
		sb.append(" and curso.periodicidade = 'IN' ");
		sb.append(" and condicaopagamentoplanofinanceirocurso.nrparcelasperiodo > 12 ");
		sb.append(" and matriculaperiodovencimento.datavencimento >= '").append(Uteis.getDataJDBC(dataInicioMes)).append("'");
		sb.append(" and matriculaperiodovencimento.datavencimento <= '").append(Uteis.getDataJDBC(dataFimMes)).append("'");
		sb.append(" and matriculaperiodovencimento.contareceber not in(select distinct controleremessacontareceber.contareceber from controleremessacontareceber  where controleremessacontareceber.contareceber = matriculaperiodovencimento.contareceber) ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("qtde");
		}
		throw new Exception("Problema no número de Parcelas da Condição de Pagamento!");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarCancelamentoReajustePrecoContaReceber(ProgressBarVO progressBarVO, IndiceReajusteVO indiceReajusteVO, IndiceReajustePeriodoVO indiceReajustePeriodoVO, UsuarioVO usuarioVO) throws Exception {
			if (indiceReajustePeriodoVO.getMotivoCancelamento().trim().equals("")) {
				throw new Exception("O MOTIVO DE CANCELAMENTO deve ser informado!");
			}
			Boolean possuiMaisDeUmaVezProcessadoParaAluno = consultarExistenciaIndiceReajustePeriodoProcessadoMaisDeUmaVezParaContaReceber(indiceReajustePeriodoVO, usuarioVO);
			if (possuiMaisDeUmaVezProcessadoParaAluno) {
				throw new Exception("Não é possível realizar o CANCELAMENTO, devido já possuir processamento(s) posterior a esse!");
			}
			progressBarVO.setStatus("Consultando Contas a Receber Processadas para cancelamento.");
			List<ContaReceberVO> listaContaReceberVOs = getFacadeFactory().getContaReceberFacade().consultarContaReceberProcessadaParaCancelamentoPorIndiceReajustePeriodo(indiceReajustePeriodoVO.getCodigo(), usuarioVO);
			int cont = 1;
			int tamanhoLista = listaContaReceberVOs.size();
			progressBarVO.setMaxValue(tamanhoLista);
			for (ContaReceberVO contaReceberVO : listaContaReceberVOs) {
				progressBarVO.setStatus("Realizando Cancelamento reajuste de preço da Matricula  "+contaReceberVO.getMatriculaAluno().getMatricula() +" "+cont +"/"+tamanhoLista );
				String tipoOrigem = contaReceberVO.getTipoOrigem().equals("MEN") ? "MENSALIDADE" : "BCC";
				IndiceReajustePeriodoMatriculaPeriodoVencimentoVO indicePeriodoVencimentoVO = getFacadeFactory().getIndiceReajustePeriodoMatriculaPeriodoVencimentoFacade().consultarUltimoIndicePorMatriculaPeriodoSituacaoParcela(indiceReajustePeriodoVO.getCodigo(), contaReceberVO.getMatriculaPeriodo(), SituacaoExecucaoEnum.PROCESSADO, contaReceberVO.getParcela(), tipoOrigem, usuarioVO);
				IndiceReajustePeriodoMatriculaPeriodoVencimentoVO indicePenultimoPeriodoVencimentoVO = getFacadeFactory().getIndiceReajustePeriodoMatriculaPeriodoVencimentoFacade().consultarPenultimoIndicePorMatriculaPeriodoSituacaoParcela(indiceReajustePeriodoVO.getCodigo(), contaReceberVO.getMatriculaPeriodo(), SituacaoExecucaoEnum.PROCESSADO, contaReceberVO.getParcela(),tipoOrigem,usuarioVO);
				BigDecimal valorIndiceReajuste = indicePeriodoVencimentoVO.getValorReajuste();
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
				if(getFacadeFactory().getControleRemessaContaReceberFacade().consultarExisteRemessaContaReceber(contaReceberVO.getCodigo())) {
					ContaCorrenteVO contaCorrente = consultarContaCorrente(contaReceberVO.getContaCorrenteVO().getCodigo(),usuarioVO);
					getFacadeFactory().getContaReceberFacade().processarRegerarNossoNumeroContaReceber(contaReceberVO, contaCorrente, usuarioVO);
				}
				cont ++;
				progressBarVO.incrementar();
			}
			if (!listaContaReceberVOs.isEmpty()) {
				getFacadeFactory().getContaReceberFacade().realizarProcessamentoValorFinalContaReceberAtualizadoComAcrescimosEDescontos(null, listaContaReceberVOs, false, "", true, usuarioVO, true, false , null , null, true);
			} 
			indiceReajustePeriodoVO.setSituacaoExecucao(SituacaoExecucaoEnum.CANCELADO);
			alterarSituacaoIndiceReajustePeriodo(indiceReajustePeriodoVO.getCodigo(), SituacaoExecucaoEnum.CANCELADO, indiceReajustePeriodoVO.getMotivoCancelamento(), false, usuarioVO);
			getFacadeFactory().getIndiceReajustePeriodoMatriculaPeriodoVencimentoFacade().alterarIndiceReajustePeriodoMatriculaPeriodoVencimentoSituacaoCanceladoPorIndiceReajustePeriodo(indiceReajustePeriodoVO.getCodigo(), SituacaoExecucaoEnum.CANCELADO, indiceReajustePeriodoVO.getMotivoCancelamento(), usuarioVO);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSituacaoIndiceReajustePeriodo(Integer codigo, SituacaoExecucaoEnum situacaoExecucao, String motivoCancelamento, Boolean processamentoAutomatico, UsuarioVO usuario) throws Exception {
		String sqlStr = "";
		if (situacaoExecucao.equals(SituacaoExecucaoEnum.CANCELADO)) {
			sqlStr = "UPDATE IndiceReajustePeriodo set situacaoExecucao=?, motivoCancelamento=?, data=CURRENT_TIMESTAMP WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sqlStr, new Object[] { situacaoExecucao.toString(), motivoCancelamento, codigo });
		} else {
			sqlStr = "UPDATE IndiceReajustePeriodo set situacaoExecucao=?, data=CURRENT_TIMESTAMP WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sqlStr, new Object[] { situacaoExecucao.toString(), codigo });
		}
	}
	
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarLogAlunosNaoDevemSofrerReajusteIndiceReajustePeriodo(Integer codigo, String logAlunosNaoDevemSofrerReajuste, UsuarioVO usuario) throws Exception {
		String sqlStr = "";
		sqlStr = "UPDATE IndiceReajustePeriodo set logAlunosNaoDevemSofrerReajuste=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sqlStr, new Object[] { logAlunosNaoDevemSofrerReajuste.toString(), codigo });
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarLogErroIndiceReajustePeriodo(Integer codigo, String logErroProcessamento, UsuarioVO usuario) throws Exception {
		String sqlStr = "";
		sqlStr = "UPDATE IndiceReajustePeriodo set logErroProcessamento=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sqlStr, new Object[] { logErroProcessamento.toString(), codigo });
	}

	public static String getIdEntidade() {
		return IndiceReajustePeriodo.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		IndiceReajustePeriodo.idEntidade = idEntidade;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Boolean consultarExistenciaIndiceReajustePeriodoProcessadoMaisDeUmaVezParaContaReceber(IndiceReajustePeriodoVO indiceReajustePeriodoVO, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct indiceReajustePeriodo.codigo  ");
		sb.append(" from indicereajusteperiodomatriculaperiodovencimento ");
		sb.append(" inner join indiceReajustePeriodo on indiceReajustePeriodo.codigo = indicereajusteperiodomatriculaperiodovencimento.indiceReajustePeriodo   ");
		sb.append(" where indicereajusteperiodomatriculaperiodovencimento.indicereajusteperiodo = ").append(indiceReajustePeriodoVO.getCodigo());
		sb.append(" and situacaoexecucao = 'PROCESSADO' ");
		sb.append(" and exists (select irpmpv.matriculaperiodo from indicereajusteperiodomatriculaperiodovencimento as irpmpv ");
		sb.append(" inner join indiceReajustePeriodo irp on irp.codigo = irpmpv.indiceReajustePeriodo ");
		sb.append(" where irpmpv.matriculaperiodo = indicereajusteperiodomatriculaperiodovencimento.matriculaperiodo ");
		sb.append(" and irp.situacaoexecucao = 'PROCESSADO' ");
		sb.append(" and irp.data > indiceReajustePeriodo.data ");
		sb.append(" ) limit 1 ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			return true;
		}
		return false;
	}
	
	@Override
	public List<IndiceReajustePeriodoVO> consultarIndiceReajustePeriodoAguardandoProcessamento(UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select indiceReajustePeriodo.codigo, indiceReajustePeriodo.mes, indiceReajustePeriodo.considerarDescontoSemValidadeCalculoIndiceReajuste, ");
		sb.append(" indiceReajustePeriodo.ano, indiceReajustePeriodo.responsavel, ");
		sb.append(" usuario.nome, indiceReajustePeriodo.data, indiceReajustePeriodo.situacaoExecucao, ");
		sb.append(" indiceReajustePeriodo.percentualReajuste, indiceReajustePeriodo.motivoCancelamento, indiceReajustePeriodo.dataCancelamento, ");
		sb.append(" indiceReajustePeriodo.logAlunosNaoDevemSofrerReajuste, indiceReajustePeriodo.logErroProcessamento, ");
		sb.append(" indiceReajuste.codigo as \"indiceReajuste.codigo\", indiceReajuste.descricao as \"indiceReajuste.descricao\" ");
		sb.append(" from indiceReajustePeriodo ");
		sb.append(" inner join indiceReajuste on indiceReajuste.codigo = indiceReajustePeriodo.indiceReajuste ");
		sb.append(" inner join usuario on usuario.codigo = indiceReajustePeriodo.responsavel ");
		sb.append(" where indiceReajustePeriodo.situacaoExecucao = '").append(SituacaoExecucaoEnum.AGUARDANDO_PROCESSAMENTO.toString()).append("' ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		return montarDadosLista(tabelaResultado, usuarioVO);
	}
	
	@Override
	public List<IndiceReajustePeriodoVO> consultarIndiceReajustePeriodoProcessando(UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select indiceReajustePeriodo.codigo, indiceReajustePeriodo.mes, indiceReajustePeriodo.considerarDescontoSemValidadeCalculoIndiceReajuste, ");
		sb.append(" indiceReajustePeriodo.ano, indiceReajustePeriodo.responsavel, ");
		sb.append(" usuario.nome, indiceReajustePeriodo.data, indiceReajustePeriodo.situacaoExecucao, ");
		sb.append(" indiceReajustePeriodo.percentualReajuste, indiceReajustePeriodo.motivoCancelamento, indiceReajustePeriodo.dataCancelamento, ");
		sb.append(" indiceReajustePeriodo.logAlunosNaoDevemSofrerReajuste, indiceReajustePeriodo.logErroProcessamento, ");
		sb.append(" indiceReajuste.codigo as \"indiceReajuste.codigo\", indiceReajuste.descricao as \"indiceReajuste.descricao\" ");
		sb.append(" from indiceReajustePeriodo ");
		sb.append(" inner join indiceReajuste on indiceReajuste.codigo = indiceReajustePeriodo.indiceReajuste ");
		sb.append(" inner join usuario on usuario.codigo = indiceReajustePeriodo.responsavel ");
		sb.append(" where indiceReajustePeriodo.situacaoExecucao = '").append(SituacaoExecucaoEnum.EM_PROCESSAMENTO.toString()).append("' ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		return montarDadosLista(tabelaResultado, usuarioVO);
	}

	@Override
	public List <IndiceReajustePeriodoVO> consultarIndiceReajustePeriodoPorMatriculaPeriodoSituacao(Integer matriculaPeriodo, String tipoOrigem, SituacaoExecucaoEnum situacaoExecucao, String parcela, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct indiceReajustePeriodo.codigo, indiceReajustePeriodo.mes, indiceReajustePeriodo.considerarDescontoSemValidadeCalculoIndiceReajuste, ");
		sb.append(" indiceReajustePeriodo.ano, indiceReajustePeriodo.responsavel, ");
		sb.append(" usuario.nome, indiceReajustePeriodo.data, indiceReajustePeriodo.situacaoExecucao,  ");
		sb.append(" indiceReajustePeriodo.percentualReajuste, indiceReajustePeriodo.motivoCancelamento, indiceReajustePeriodo.dataCancelamento, ");
		sb.append(" indiceReajustePeriodo.logAlunosNaoDevemSofrerReajuste, indiceReajustePeriodo.logErroProcessamento, ");
		sb.append(" indiceReajuste.codigo as \"indiceReajuste.codigo\", indiceReajuste.descricao as \"indiceReajuste.descricao\" ");
		sb.append(" from indiceReajustePeriodo ");
		sb.append(" inner join indiceReajuste on indiceReajuste.codigo = indiceReajustePeriodo.indiceReajuste ");
		sb.append(" inner join usuario on usuario.codigo = indiceReajustePeriodo.responsavel ");
		sb.append(" inner join indicereajusteperiodomatriculaperiodovencimento on indicereajusteperiodomatriculaperiodovencimento.indiceReajustePeriodo = indiceReajustePeriodo.codigo");
		sb.append(" where indicereajusteperiodomatriculaperiodovencimento.matriculaPeriodo = ").append(matriculaPeriodo);
		sb.append(" and indiceReajustePeriodo.situacaoexecucao = '").append(situacaoExecucao.toString()).append("' ");
		sb.append(" and indicereajusteperiodomatriculaperiodovencimento.parcela = '").append(parcela.contains("/") ? parcela.substring(0, parcela.indexOf("/")) : parcela).append("' ");
		sb.append(" and indicereajusteperiodomatriculaperiodovencimento.situacao = '").append(situacaoExecucao.toString()).append("' ");
		sb.append(" and indicereajusteperiodomatriculaperiodovencimento.tipoorigem = '").append(tipoOrigem).append("' ");
		sb.append(" order by indiceReajustePeriodo.data asc ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		return montarDadosLista(tabelaResultado, usuarioVO);
	}
}
