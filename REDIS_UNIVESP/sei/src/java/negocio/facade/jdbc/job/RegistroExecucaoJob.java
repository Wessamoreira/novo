package negocio.facade.jdbc.job;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
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

import controle.arquitetura.DataModelo;
import jobs.JobRegistroExecucaoJob;
import jobs.enumeradores.JobsEnum;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.job.RegistroExecucaoJobTotaisVO;
import negocio.comuns.job.RegistroExecucaoJobVO;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.job.RegistroExecucaoJobInterfaceFacade;

/**
 * @author Victor Hugo de Paula Costa - 18 de mai de 2016
 *
 */
@Repository
@Scope("singleton")
@Lazy
public class RegistroExecucaoJob extends ControleAcesso implements RegistroExecucaoJobInterfaceFacade {

	private static final long serialVersionUID = 1L;
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRES_NEW)
	public void incluir(final RegistroExecucaoJobVO registroExecucaoJobVO) throws Exception {
		try {			
			final String sql = "INSERT INTO RegistroExecucaoJob(nome, datainicio, datatermino, tempoexecucao, erro, total, totalsucesso, totalerro , codigoOrigem) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo";
			getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setString(1, registroExecucaoJobVO.getNome());
					sqlInserir.setTimestamp(2, Uteis.getDataJDBCTimestamp(registroExecucaoJobVO.getDataInicio()));
					if(Uteis.isAtributoPreenchido(registroExecucaoJobVO.getDataTermino())) {
						sqlInserir.setTimestamp(3, Uteis.getDataJDBCTimestamp(registroExecucaoJobVO.getDataTermino()));	
					}else {
						sqlInserir.setNull(3, Types.NULL);
					}
					sqlInserir.setLong(4, registroExecucaoJobVO.getTempoExecucao());
					sqlInserir.setString(5, registroExecucaoJobVO.getErro());
					sqlInserir.setInt(6, registroExecucaoJobVO.getTotal());
					sqlInserir.setInt(7, registroExecucaoJobVO.getTotalSucesso());
					sqlInserir.setInt(8, registroExecucaoJobVO.getTotalErro());
					if(Uteis.isAtributoPreenchido(registroExecucaoJobVO.getCodigoOrigem())) {
						sqlInserir.setInt(9, registroExecucaoJobVO.getCodigoOrigem());	
					}else {
						sqlInserir.setNull(9, Types.NULL);
					}
					return sqlInserir;
				}
			}, new ResultSetExtractor<Integer>() {
				public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						return arg0.getInt("codigo");
					}
					return null;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void atualizarAgendamentoExecucaoRegistroExecucaoJob(JobsEnum jobsEnum, Integer codigoOrigem, Date dataInico) throws Exception {
		StringBuilder sql1 = new StringBuilder("UPDATE RegistroExecucaoJob set dataInicio = '").append(Uteis.getDataJDBCTimestamp(dataInico)).append("' ");
		sql1.append(" , datatermino = null, erro = null WHERE codigoorigem = ").append(codigoOrigem).append(" ");
		sql1.append(" and  nome = '").append(jobsEnum.getName()).append("' ");
		getConexao().getJdbcTemplate().update(sql1.toString());
		
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void atualizarCampoDataTerminoRegistroExecucaoJob(JobsEnum jobsEnum, Integer codigoOrigem) throws Exception {
		StringBuilder sql1 = new StringBuilder("UPDATE RegistroExecucaoJob set datatermino = '").append(Uteis.getDataJDBCTimestamp(new Date())).append("' ");
		sql1.append(" WHERE codigoorigem = ").append(codigoOrigem).append(" ");
		sql1.append(" and  nome = '").append(jobsEnum.getName()).append("' ");
		getConexao().getJdbcTemplate().update(sql1.toString());
		
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void atualizarCampoErroRegistroExecucaoJob(JobsEnum jobsEnum, Integer codigoOrigem, String erro) throws Exception {
		StringBuilder sql1 = new StringBuilder("UPDATE RegistroExecucaoJob set datatermino = '").append(Uteis.getDataJDBCTimestamp(new Date())).append("' ");
		sql1.append(" , erro = '").append(erro).append("' ");
		sql1.append(" WHERE codigoorigem = ").append(codigoOrigem).append(" ");
		sql1.append(" and  nome = '").append(jobsEnum.getName()).append("' ");
		getConexao().getJdbcTemplate().update(sql1.toString());
		
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirRegistroExecucaoJobPorCodigoOrigem(JobsEnum jobsEnum, Integer codigoOrigem) throws Exception {
		StringBuilder sql1 = new StringBuilder("delete from RegistroExecucaoJob ");
		sql1.append(" WHERE codigoorigem = ").append(codigoOrigem).append(" ");
		sql1.append(" and  nome = '").append(jobsEnum.getName()).append("' ");
		getConexao().getJdbcTemplate().update(sql1.toString());
		
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirRegistroExecucaoJob(final RegistroExecucaoJobVO registroExecucaoJobVO, UsuarioVO usuarioVO) {
		Thread trThread = new Thread(new JobRegistroExecucaoJob(registroExecucaoJobVO, usuarioVO));
		trThread.start();
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public boolean consultarSeExisterRegistroExecucaoJobPorCodigoOrigem(JobsEnum jobsEnum, Integer codigoOrigem) throws Exception {
		StringBuilder sqlStr = new StringBuilder("SELECT count(codigo) QTDE from registroexecucaojob ");
		sqlStr.append(" WHERE codigoorigem = ?  and  nome = ? ");
	    SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), codigoOrigem, jobsEnum.getName());
		return Uteis.isAtributoPreenchido(rs, Uteis.QTDE, TipoCampoEnum.INTEIRO);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public List<RegistroExecucaoJobVO> consultarRegistroExecucaoJobInterrompidasPorNomeOrigem(JobsEnum jobsEnum) throws Exception {
		StringBuilder sqlStr = new StringBuilder("SELECT * from registroexecucaojob ");
		sqlStr.append(" WHERE datatermino is null and nome = ? ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), jobsEnum.getName());
	    return montarDadosConsulta(tabelaResultado);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public RegistroExecucaoJobVO consultarRegistroExecucaoJobPorCodigoOrigem(JobsEnum jobsEnum) throws Exception {
		StringBuilder sqlStr = new StringBuilder("SELECT * from registroexecucaojob ");
		sqlStr.append(" WHERE nome = ?  order by codigo desc limit 1");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), jobsEnum.getName());
		if (rs.next()) {
			return (montarDados(rs));
		}
		return new RegistroExecucaoJobVO();
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public RegistroExecucaoJobVO consultarRegistroExecucaoJobPorCodigoOrigemSemErro(JobsEnum jobsEnum) throws Exception {
		StringBuilder sqlStr = new StringBuilder("SELECT * from registroexecucaojob ");
		sqlStr.append(" WHERE nome = ? and totalErro = 0 order by codigo desc limit 1");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), jobsEnum.getName());
		if (rs.next()) {
			return (montarDados(rs));
		}
		return new RegistroExecucaoJobVO();
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public List<RegistroExecucaoJobVO> consultarRegistroExecucaoJobPorNomeOrigemNaoExecutada(JobsEnum jobsEnum) throws Exception {
		StringBuilder sqlStr = new StringBuilder("SELECT * from registroexecucaojob ");
		sqlStr.append(" WHERE datatermino is null and nome = ? ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), jobsEnum.getName());
	    return montarDadosConsulta(tabelaResultado);
	}
	
	@Override
	public int consultarTotalEmailsAguardandoEnvio() {
		String sql = "select count(*) total from email where datacadastro >= (current_date - 1) and redefinirSenha = false";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("total");
		} else {
			return 0;
		}
	}
	
	@Override
	public int consultarTotalEmailsQueNaoForamEnviados() {
		String sql = "select count(*) total from email where datacadastro < (current_date - 1) and redefinirSenha = false";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("total");
		} else {
			return 0;
		}
	}
	
	@Override
	public void consultarTotaisUltimaHora(RegistroExecucaoJobTotaisVO totaisUltimaHora) {
		String sql = "select count(codigo) contador, sum(total) total, sum(totalsucesso) totalsucesso, sum(totalerro) totalerro from registroexecucaojob where datainicio > (now() - interval '1 hour') and nome = 'Enviar Email'";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
		if (tabelaResultado.next()) {
			totaisUltimaHora.setTotalRotinasExecutadas(tabelaResultado.getInt("contador"));
			totaisUltimaHora.setTotalEmailsEncontrados(tabelaResultado.getInt("total"));
			totaisUltimaHora.setTotalEmailsEnviados(tabelaResultado.getInt("totalsucesso"));
			totaisUltimaHora.setTotalEmailsErro(tabelaResultado.getInt("totalerro"));
		}
	}
	
	@Override
	public void consultarTotaisUltimas24Horas(RegistroExecucaoJobTotaisVO totaisUltimas24Horas) {
		String sql = "select count(codigo) contador, sum(total) total, sum(totalsucesso) totalsucesso, sum(totalerro) totalerro from registroexecucaojob where datainicio > (now() - interval '1 day') and nome = 'Enviar Email'";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
		if (tabelaResultado.next()) {
			totaisUltimas24Horas.setTotalRotinasExecutadas(tabelaResultado.getInt("contador"));
			totaisUltimas24Horas.setTotalEmailsEncontrados(tabelaResultado.getInt("total"));
			totaisUltimas24Horas.setTotalEmailsEnviados(tabelaResultado.getInt("totalsucesso"));
			totaisUltimas24Horas.setTotalEmailsErro(tabelaResultado.getInt("totalerro"));
		}
	}
	
	@Override
	public List<RegistroExecucaoJobVO> consultarRegistrosUltimaHora() throws Exception {
        String sqlStr = "SELECT * from registroexecucaojob where datainicio > (now() - interval '1 hour') and nome = 'Enviar Email' order by datainicio desc";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado);
    }
	
	@Override
	public List<RegistroExecucaoJobVO> consultarUltimosRegistros() throws Exception {
        String sqlStr = "SELECT * from registroexecucaojob where nome = 'Enviar Email' order by datainicio desc limit 12";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado);
    }
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void consultar(DataModelo dataModelo, RegistroExecucaoJobVO obj) {
		List<RegistroExecucaoJobVO> objs = consultaRapidaPorFiltros(obj, dataModelo);
		dataModelo.getListaFiltros().clear();
		dataModelo.setTotalRegistrosEncontrados(consultarTotalPorFiltros(obj, dataModelo));
		dataModelo.setListaConsulta(objs);
	}

	private List<RegistroExecucaoJobVO> consultaRapidaPorFiltros(RegistroExecucaoJobVO obj, DataModelo dataModelo) {
		try {
			StringBuilder sqlStr = new StringBuilder(" select * from registroexecucaojob ");
			sqlStr.append(" WHERE 1= 1 ");
			montarFiltrosParaConsulta(obj, dataModelo, sqlStr);
			sqlStr.append(" ORDER BY datainicio desc ");
			UteisTexto.addLimitAndOffset(sqlStr, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), dataModelo.getListaFiltros().toArray());
			return montarDadosConsulta(tabelaResultado);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	private Integer consultarTotalPorFiltros(RegistroExecucaoJobVO obj, DataModelo dataModelo) {
		try {
			StringBuilder sqlStr = new StringBuilder(" select count(codigo) as QTDE from registroexecucaojob ");
			sqlStr.append(" WHERE 1= 1 ");
			montarFiltrosParaConsulta(obj, dataModelo, sqlStr);
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), dataModelo.getListaFiltros().toArray());
			return (Integer) Uteis.getSqlRowSetTotalizador(rs, Uteis.QTDE, TipoCampoEnum.INTEIRO);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	private void montarFiltrosParaConsulta(RegistroExecucaoJobVO obj, DataModelo dataModelo, StringBuilder sqlStr) {
		if(obj.getNome().equals(JobsEnum.JOB_BLACKBOARD_CONTAS_SALA.getName())) {
			sqlStr.append(" and codigoorigem = ? ");
			dataModelo.getListaFiltros().add(obj.getCodigoOrigem());
			return;
		}
		
		if (!obj.getListaJobsEnum().isEmpty()) {
			boolean adicionarOr = false;
			for (String jobsEnum : obj.getListaJobsEnum()) {
				if(!adicionarOr) {
					adicionarOr = true;
					sqlStr.append(" and (nome ilike (?) ");
					dataModelo.getListaFiltros().add(jobsEnum);
				}else {
					sqlStr.append(" or nome ilike (?) ");
					dataModelo.getListaFiltros().add(jobsEnum);
				}
			}
			sqlStr.append(" )");	
		}
		if (Uteis.isAtributoPreenchido(obj.getNome())) {
			sqlStr.append(" and nome ilike (?) ");
			dataModelo.getListaFiltros().add(obj.getNome());
		}
		
		if (Uteis.isAtributoPreenchido(obj.getErro())) {
			sqlStr.append(" and erro ilike (?) ");
			dataModelo.getListaFiltros().add(PERCENT + obj.getErro() + PERCENT);
		}
		
		if (Uteis.isAtributoPreenchido(dataModelo.getDataIni())) {
			sqlStr.append(" and (datainicio >= '").append(Uteis.getDataBD0000(dataModelo.getDataIni())).append("' and datatermino <= '").append(Uteis.getDataBD2359(dataModelo.getDataIni())).append("') ");
			//sqlStr.append(" or (datainicio >= '").append(Uteis.getDataBD0000(dataModelo.getDataIni())).append("' and datatermino is null )) ");
		}
	}
	
    
    public List<RegistroExecucaoJobVO> montarDadosConsulta(SqlRowSet tabelaResultado) throws Exception {
		List<RegistroExecucaoJobVO> vetResultado = new ArrayList<RegistroExecucaoJobVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado));
		}
		return vetResultado;
	}
    
    public static RegistroExecucaoJobVO montarDados(SqlRowSet dadosSQL) throws Exception {
    	RegistroExecucaoJobVO obj = new RegistroExecucaoJobVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));        
        obj.setNome(dadosSQL.getString("nome"));
        obj.setDataInicio(dadosSQL.getTimestamp("datainicio"));
        if(Uteis.isAtributoPreenchido(dadosSQL.getTimestamp("datatermino"))){
        	obj.setDataTermino(dadosSQL.getTimestamp("datatermino"));
        }
        obj.setTempoExecucao(dadosSQL.getLong("tempoexecucao"));
        obj.setErro(dadosSQL.getString("erro"));
        obj.setTotal(dadosSQL.getInt("total"));
        obj.setTotalSucesso(dadosSQL.getInt("totalSucesso"));
        obj.setTotalErro(dadosSQL.getInt("totalErro"));
        if(Uteis.isAtributoPreenchido(dadosSQL.getInt("codigoorigem"))){
        	obj.setCodigoOrigem(dadosSQL.getInt("codigoorigem"));	
        }
        return obj;
    }	
//    @Override
//    public void incluirRegistroExecucaoJobSerasaApiGeo(JobsEnum jobsEnum , int codigoAgenteNegativacaoCobrancaContaReceberVO) {
//    	try {
//	    	RegistroExecucaoJobVO registroExecucaoJobVO = new RegistroExecucaoJobVO();
//	    	registroExecucaoJobVO.setNome(jobsEnum.getName());
//	    	registroExecucaoJobVO.setDataInicio(new Date());
//	    	registroExecucaoJobVO.setDataTermino(null);
//	    	registroExecucaoJobVO.setCodigoOrigem(codigoAgenteNegativacaoCobrancaContaReceberVO);
//	    	incluir(registroExecucaoJobVO);
//	    	
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    }
   
    
    @Override
    public Date consultarDataUltimaExecucaoJobBaixaCartaoCreditoRecorrenciaDCC() {
    	StringBuilder sb = new StringBuilder();
    	sb.append("select dataInicio from registroexecucaojob where nome ilike 'JobBaixaCartaoCreditoDCC' order by datainicio desc limit 1 ");
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
    	if (tabelaResultado.next()) {
    		return tabelaResultado.getTimestamp("dataInicio");
    	}
    	return null;
    }
	
    @Override
	public void consultarOtimizado(DataModelo dataModelo) throws Exception {
    	dataModelo.setLimitePorPagina(10);
    	List<Object> valoresFiltros = new ArrayList<>(0);
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT count(*) over() as qtde_total_registros, * FROM registroexecucaojob ");
		sql.append("WHERE nome = ? ");
		valoresFiltros.add(dataModelo.getValorConsulta());
		if (Uteis.isAtributoPreenchido(dataModelo.getDataIni()) && Uteis.isAtributoPreenchido(dataModelo.getDataFim())) {
			sql.append("AND (registroexecucaojob.datainicio BETWEEN ? AND ?) ");
			valoresFiltros.add(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(dataModelo.getDataIni()));
			valoresFiltros.add(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(dataModelo.getDataFim()));
		} else if (Uteis.isAtributoPreenchido(dataModelo.getDataIni())) {
			sql.append("AND (registroexecucaojob.datainicio >= ?) ");
			valoresFiltros.add(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(dataModelo.getDataIni()));
		} else {
			dataModelo.setDataIni(null);
			dataModelo.setDataFim(null);
		}
		sql.append("ORDER BY codigo DESC ");
		sql.append("LIMIT ").append(dataModelo.getLimitePorPagina()).append(" OFFSET ").append(dataModelo.getOffset());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), valoresFiltros.toArray());
		if (dataModelo != null && tabelaResultado.next()) {
			dataModelo.setTotalRegistrosEncontrados(tabelaResultado.getInt("qtde_total_registros"));
		}
		tabelaResultado.beforeFirst();
		dataModelo.setListaConsulta(montarDadosConsulta(tabelaResultado));
	}
}
