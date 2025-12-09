package negocio.facade.jdbc.academico;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.MatriculaCamposAlteradosVO;
import negocio.comuns.academico.enumeradores.OpcaoTabelaLogMatriculaEnum;
import negocio.comuns.administrativo.AtributoJsonVO;
import negocio.comuns.administrativo.AuditVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoVinculoMatricula;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.LogMatriculaInterfaceFacade;

@Lazy
@Repository
@Scope("singleton")
@SuppressWarnings({"unchecked", "rawtypes"})
public class LogMatricula extends ControleAcesso implements LogMatriculaInterfaceFacade {
	
	protected static String idEntidade;
	public static final long serialVersionUID = 1L;
	
	public LogMatricula() throws Exception {
		super();
		setIdEntidade("LogAuditMatricula");
	}
	
	public void validarDados(String ano, String mes, UsuarioVO usuarioVO) throws Exception {
		
	}
	
	@Override
	public List<AuditVO> consultar(String matricula, SituacaoVinculoMatricula situacao, OpcaoTabelaLogMatriculaEnum opcaoTabelaLogMatriculaEnum, String ano, String mes, String acao, String coluna, Date dataInicio, Date dataFim, UsuarioVO usuarioLogVO, boolean controlarAcesso, MatriculaCamposAlteradosVO matriculaCamposAlteradosVO, Integer turma, String anoMatriculaPeriodo, String semestreMatriculaPeriodo, Integer codigoTransacao, UsuarioVO usuarioVO) throws Exception {
		validarDados(ano, mes, usuarioVO);
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT DISTINCT resultado.* FROM ( ");
		sb.append(this.getSQLPadraoConsulta(matricula, situacao, opcaoTabelaLogMatriculaEnum, ano, mes, acao, coluna, dataInicio, dataFim, usuarioLogVO, matriculaCamposAlteradosVO, turma, anoMatriculaPeriodo, semestreMatriculaPeriodo, codigoTransacao).toString());
		sb.append(" ) AS resultado "); 
		sb.append(" ORDER BY resultado.event_id; ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		return montarDadosConsulta(tabelaResultado, coluna, usuarioVO);
	}
	
	private StringBuilder getSQLPadraoConsulta(String matricula, SituacaoVinculoMatricula situacao, OpcaoTabelaLogMatriculaEnum opcaoTabelaLogMatriculaEnum, String ano, String mes, String acao, String coluna, Date dataInicio, Date dataFim, UsuarioVO usuarioLogVO , MatriculaCamposAlteradosVO matriculaCamposAlteradosVO, Integer turma, String anoMatriculaPeriodo, String semestreMatriculaPeriodo, Integer codigoTransacao) {
		StringBuilder sb = new StringBuilder();
		if (!coluna.equals("")) {
			sb.append("SELECT event_id, schema_name, session_user_name, usuario_logado, action_tstamp_clk, transaction_id, client_addr, action, changed_fields, row_data ->> '").append(coluna.toLowerCase()).append("' AS ").append(coluna).append(" FROM ").append(getAuditMesAno(ano, mes));
		} else {
			sb.append("SELECT * FROM ").append(getAuditMesAno(ano, mes));
		}
		if (!codigoTransacao.equals(0)) {
			sb.append(" WHERE transaction_id = ").append(codigoTransacao);
		} else {
			if (opcaoTabelaLogMatriculaEnum.name().equals("MATRICULA")) {
				sb.append(" WHERE relid = 'matricula'::regclass ");
			} else {
				sb.append(" WHERE relid = 'matriculaPeriodo'::regclass ");
			}
		}
		sb.append(" AND row_data ? 'matricula' ");
		
		if (!matricula.equals("")) {
			sb.append(" AND row_data ->> 'matricula' = '").append(matricula).append("' ");
		}
		if (opcaoTabelaLogMatriculaEnum.name().equals("MATRICULA_PERIODO")) {
			if (!turma.equals(0)) {
				sb.append(" AND row_data ->> 'turma' = '").append(turma).append("' ");
			}
			if (!anoMatriculaPeriodo.equals("")) {
				sb.append(" AND row_data ->> 'ano' = '").append(anoMatriculaPeriodo).append("' ");
			}
			if (!semestreMatriculaPeriodo.equals("")) {
				sb.append(" AND row_data ->> 'semestre' = '").append(semestreMatriculaPeriodo).append("' ");
			}
			if (situacao != null) {
				sb.append(" AND row_data ->> 'situacaomatriculaperiodo' = '").append(situacao.getValor()).append("' ");
			}
		} else {
			if (situacao != null) {
				sb.append(" AND row_data ->> 'situacao' = '").append(situacao.getValor()).append("' ");
			}
		}
			
		if (!acao.equals("")) {
			sb.append(" AND action = '").append(acao).append("' ");
		}
		if (Uteis.isAtributoPreenchido(usuarioLogVO.getCodigo())) {
			sb.append(" AND usuario_logado = ").append(usuarioLogVO.getCodigo());
		}
		if (dataInicio != null && dataFim != null) {
			sb.append(" AND action_tstamp_clk::date >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ").append(" AND action_tstamp_clk::date <= '").append(Uteis.getDataJDBC(dataFim)).append("' ");
		} else if (dataInicio != null && dataFim == null) {
			sb.append(" AND action_tstamp_clk::date >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
		} else if (dataFim != null) {
			sb.append(" AND action_tstamp_clk::date <= '").append(Uteis.getDataJDBC(dataFim)).append("' ");
		}
		
		
		if (opcaoTabelaLogMatriculaEnum.name().equals(OpcaoTabelaLogMatriculaEnum.MATRICULA.name())) {
			Boolean primeiro = true;
			if (matriculaCamposAlteradosVO.getMatricula()) {
				sb.append(" and (changed_fields ? 'matricula' ");
				primeiro = false;
			}
			if (matriculaCamposAlteradosVO.getSituacao()) {
				if (primeiro) {
					sb.append(" and (changed_fields ? 'situacao' ");
					primeiro = false;
				} else {
					sb.append(" or changed_fields ? 'situacao' ");
				}
			}
			if (matriculaCamposAlteradosVO.getCurso()) {
				if (primeiro) {
					sb.append(" and (changed_fields ? 'curso' ");
					primeiro = false;
				} else {
					sb.append(" or changed_fields ? 'curso' ");
				}
			}
			if (matriculaCamposAlteradosVO.getTurno()) {
				if (primeiro) {
					sb.append(" and (changed_fields ? 'turno' ");
					primeiro = false;
				} else {
					sb.append(" or changed_fields ? 'turno' ");
				}
			}
			if (matriculaCamposAlteradosVO.getUnidadeEnsino()) {
				if (primeiro) {
					sb.append(" and (changed_fields ? 'unidadeensino' ");
					primeiro = false;
				} else {
					sb.append(" or changed_fields ? 'unidadeensino' ");
				}
			}
			if (matriculaCamposAlteradosVO.getAluno()) {
				if (primeiro) {
					sb.append(" and (changed_fields ? 'aluno' ");
					primeiro = false;
				} else {
					sb.append(" or changed_fields ? 'aluno' ");
				}
			}
			if (matriculaCamposAlteradosVO.getFormaIngresso()) {
				if (primeiro) {
					sb.append(" and (changed_fields ? 'formaingresso' ");
					primeiro = false;
				} else {
					sb.append(" or changed_fields ? 'formaingresso' ");
				}
			}
			if (matriculaCamposAlteradosVO.getAnoIngresso()) {
				if (primeiro) {
					sb.append(" and (changed_fields ? 'anoingresso' ");
					primeiro = false;
				} else {
					sb.append(" or changed_fields ? 'anoingresso' ");
				}
			}
			if (matriculaCamposAlteradosVO.getSemestreIngresso()) {
				if (primeiro) {
					sb.append(" and (changed_fields ? 'semestreingresso' ");
					primeiro = false;
				} else {
					sb.append(" or changed_fields ? 'semestreingresso' ");
				}
			}
			if (matriculaCamposAlteradosVO.getDataInicioCurso()) {
				if (primeiro) {
					sb.append(" and (changed_fields ? 'datainiciocurso' ");
					primeiro = false;
				} else {
					sb.append(" or changed_fields ? 'datainiciocurso' ");
				}
			}
			if (!primeiro) {
				sb.append(") ");
			}
		}
		
		if (opcaoTabelaLogMatriculaEnum.name().equals(OpcaoTabelaLogMatriculaEnum.MATRICULA_PERIODO.name())) {
			Boolean primeiro = true;
			if (matriculaCamposAlteradosVO.getGradeCurricular()) {
				sb.append(" and (changed_fields ? 'gradecurricular' ");
				primeiro = false;
			}
			if (matriculaCamposAlteradosVO.getTurma()) {
				if (primeiro) {
					sb.append(" and (changed_fields ? 'turma' ");
					primeiro = false;
				} else {
					sb.append(" or changed_fields ? 'turma' ");
				}
			}
			if (matriculaCamposAlteradosVO.getSituacaoMatriculaPeriodo()) {
				if (primeiro) {
					sb.append(" and (changed_fields ? 'situacaomatriculaperiodo' ");
					primeiro = false;
				} else {
					sb.append(" or changed_fields ? 'situacaomatriculaperiodo' ");
				}
			}
			if (matriculaCamposAlteradosVO.getAno()) {
				if (primeiro) {
					sb.append(" and (changed_fields ? 'ano' ");
					primeiro = false;
				} else {
					sb.append(" or changed_fields ? 'ano' ");
				}
			}
			if (matriculaCamposAlteradosVO.getSemestre()) {
				if (primeiro) {
					sb.append(" and (changed_fields ? 'semestre' ");
					primeiro = false;
				} else {
					sb.append(" or changed_fields ? 'semestre' ");
				}
			}
			if (matriculaCamposAlteradosVO.getPeriodoLetivoMatricula()) {
				if (primeiro) {
					sb.append(" and (changed_fields ? 'periodoletivomatricula' ");
					primeiro = false;
				} else {
					sb.append(" or changed_fields ? 'periodoletivomatricula' ");
				}
			}
			if (matriculaCamposAlteradosVO.getData()) {
				if (primeiro) {
					sb.append(" and (changed_fields ? 'data' ");
					primeiro = false;
				} else {
					sb.append(" or changed_fields ? 'data' ");
				}
			}
			if (!primeiro) {
				sb.append(") ");
			}
		}
		
		return sb;
	}
	
	public String getAuditMesAno(String ano, String mes) {
		return "audit.audit_" + ano + "_" + mes;
	}
	
	public List<AuditVO> montarDadosConsulta(SqlRowSet dadosSQL, String coluna, UsuarioVO usuario) throws Exception  {
		List<AuditVO> listaLogAuditMatriculaVOs = new ArrayList<AuditVO>(0);
		while (dadosSQL.next()) {
			listaLogAuditMatriculaVOs.add(montarDados(dadosSQL, coluna, usuario));
		}
		return listaLogAuditMatriculaVOs;
	}

	private AuditVO montarDados(SqlRowSet dadosSQL, String coluna, UsuarioVO usuario) throws Exception, JSONException {
		AuditVO obj = new AuditVO();
		obj.setEvent_id(dadosSQL.getInt("event_id"));
		obj.setSchema_name(dadosSQL.getString("schema_name"));
		obj.setSession_user_name(dadosSQL.getString("session_user_name"));
		obj.getUsuarioLogadoVO().setCodigo(dadosSQL.getInt("usuario_logado"));
		if (Uteis.isAtributoPreenchido(obj.getUsuarioLogadoVO().getCodigo())) {
			obj.setUsuarioLogadoVO(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getUsuarioLogadoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));
		}
		if (obj.getUsuarioLogadoVO().getUsername().equals("otimize-ti") && obj.getUsuarioLogadoVO().getNome().equals("")) {
			obj.getUsuarioLogadoVO().setNome("Diretor MultiCampus");
		}
		obj.setAction_tstamp_clk(dadosSQL.getTimestamp("action_tstamp_clk"));
		obj.setTransaction_id(dadosSQL.getLong("transaction_id"));
		obj.setClient_addr(dadosSQL.getString("client_addr"));
		obj.setAction(dadosSQL.getString("action"));
		if (!coluna.equals("")) {
			AtributoJsonVO atributoJsonVO = new AtributoJsonVO();
			atributoJsonVO.setAtributo(coluna);
			atributoJsonVO.setValor(dadosSQL.getString(coluna));
			obj.getListaAtributoJsonRow_dataVOs().add(atributoJsonVO);
		} else {
			obj.setRow_data(new JSONObject(dadosSQL.getString("row_data")));
			obj.setListaAtributoJsonRow_dataVOs(realizarConversaoAtributoJsonRow_data(dadosSQL.getString("row_data"), usuario));
		}
		if (dadosSQL.getString("changed_fields") != null) {
			obj.setChanged_fields(new JSONObject(dadosSQL.getString("changed_fields")));
			obj.setListaAtributoJsonChanged_fieldsVOs(realizarConversaoAtributoJsonChanged_fields(dadosSQL.getString("changed_fields"), usuario));
		}
		return obj;
	}
	
	public List<AtributoJsonVO> realizarConversaoAtributoJsonRow_data(String json, UsuarioVO usuarioVO) throws Exception, JsonMappingException, IOException {
		ObjectMapper mapperRowData = new ObjectMapper();
		 Map<String, String> mapJsonRowDataVOs = mapperRowData.readValue(json, Map.class);
		List<AtributoJsonVO> listaAtributoJsonVOs = new ArrayList<AtributoJsonVO>(0);
		for(Entry<String, String> map : mapJsonRowDataVOs.entrySet()) {
			AtributoJsonVO atributoJsonVO = new AtributoJsonVO();
			atributoJsonVO.setAtributo(map.getKey());
			atributoJsonVO.setValor(map.getValue());
			listaAtributoJsonVOs.add(atributoJsonVO);
		}
		return listaAtributoJsonVOs;
	}
	
	public List<AtributoJsonVO> realizarConversaoAtributoJsonChanged_fields(String json, UsuarioVO usuarioVO) throws Exception, JsonMappingException, IOException {
		ObjectMapper mapperChangedFields = new ObjectMapper();
		 Map<String, String> mapJsonChangedFieldsVOs = mapperChangedFields.readValue(json, Map.class);
		List<AtributoJsonVO> listaAtributoJsonVOs = new ArrayList<AtributoJsonVO>(0);
		for(Entry<String, String> map : mapJsonChangedFieldsVOs.entrySet()) {
			AtributoJsonVO atributoJsonVO = new AtributoJsonVO();
			atributoJsonVO.setAtributo(map.getKey());
			atributoJsonVO.setValor(map.getValue());
			listaAtributoJsonVOs.add(atributoJsonVO);
		}
		return listaAtributoJsonVOs;
	}

	@Override
	public List<String> consultarAnoAudit(UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct substring(relname, 7, 4) as ano ");
		sb.append(" FROM pg_catalog.pg_statio_user_tables where relname  ilike 'audit_%' ");
		sb.append(" order by ano ");
		List<String> listaAnoVOs = new ArrayList<String>(0);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (tabelaResultado.next()) {
			listaAnoVOs.add(tabelaResultado.getString("ano"));
		}
		return listaAnoVOs;
	}
	
	@Override
	public List<String> consultarMesAuditPorAno(String ano, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT substring(relname, 12) as mes ");
		sb.append(" FROM pg_catalog.pg_statio_user_tables where relname  ilike 'audit_%").append(ano).append("%'");
		sb.append(" order by mes ");
		List<String> listaMesVOs = new ArrayList<String>(0);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (tabelaResultado.next()) {
			listaMesVOs.add(tabelaResultado.getString("mes"));
		}
		return listaMesVOs;
	}
	
	@Override
	public List<String> consultarColunasAuditMatricula(OpcaoTabelaLogMatriculaEnum opcaoTabelaLogMatriculaEnum) {
		StringBuilder sb = new StringBuilder();
		if (opcaoTabelaLogMatriculaEnum.name().equals("MATRICULA")) {
			sb.append("select initcap(column_name) as column_name from information_schema.columns WHERE table_name = 'matricula' order by initcap(column_name)");
		} else {
			sb.append("select initcap(column_name) as column_name from information_schema.columns WHERE table_name = 'matriculaperiodo' order by initcap(column_name)");
		}
		List<String> listaColunaVOs =  new ArrayList<String>(0);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (tabelaResultado.next()) {
			listaColunaVOs.add(tabelaResultado.getString("column_name"));
		}
		return listaColunaVOs;
	}
	
	public static String getIdEntidade() {
		return LogMatricula.idEntidade;
	}
	
	public void setIdEntidade(String idEntidade) {
		LogMatricula.idEntidade = idEntidade;
	}
}
