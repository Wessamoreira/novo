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
import negocio.comuns.academico.HistoricoCamposAlteradosVO;
import negocio.comuns.administrativo.AtributoJsonVO;
import negocio.comuns.administrativo.AuditVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoHistorico;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.LogAuditHistoricoInterfaceFacade;

@Lazy
@Repository
@Scope("singleton")
@SuppressWarnings({"unchecked", "rawtypes"})
public class LogAuditHistorico extends ControleAcesso implements LogAuditHistoricoInterfaceFacade {
	
	protected static String idEntidade;
	public static final long serialVersionUID = 1L;
	
	public LogAuditHistorico() throws Exception {
		super();
		setIdEntidade("LogAuditHistorico");
	}
	
	public void validarDados(String ano, String mes, UsuarioVO usuarioVO) throws Exception {
		
	}
	
	@Override
	public List<AuditVO> consultar(String matricula, SituacaoHistorico situacao, Integer codigo, Integer disciplina, String ano, String mes, String acao, String coluna, Date dataInicio, Date dataFim, UsuarioVO usuarioLogVO, boolean controlarAcesso, HistoricoCamposAlteradosVO historicoCamposAlteradosVO, UsuarioVO usuarioVO) throws Exception {
		validarDados(ano, mes, usuarioVO);
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT DISTINCT resultado.* FROM ( ");
		sb.append(this.getSQLPadraoConsulta(matricula, situacao, codigo, disciplina, ano, mes, acao, coluna, dataInicio, dataFim, usuarioLogVO, historicoCamposAlteradosVO).toString());
		sb.append(" ) AS resultado "); 
		sb.append(" ORDER BY resultado.event_id; ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		return montarDadosConsulta(tabelaResultado, coluna, usuarioVO);
}
	
	private StringBuilder getSQLPadraoConsulta(String matricula, SituacaoHistorico situacao, Integer codigo, Integer disciplina, String ano, String mes, String acao, String coluna, Date dataInicio, Date dataFim, UsuarioVO usuarioLogVO, HistoricoCamposAlteradosVO historicoCamposAlteradosVO) {
		StringBuilder sb = new StringBuilder();
		if (!coluna.equals("")) {
			sb.append("SELECT event_id, schema_name, session_user_name, usuario_logado, action_tstamp_clk, transaction_id, client_addr, action, changed_fields, row_data ->> '").append(coluna).append("' AS ").append(coluna.toLowerCase()).append(" FROM ").append(getAuditMesAno(ano, mes));
		} else {
			sb.append("SELECT * FROM ").append(getAuditMesAno(ano, mes));
		}
		sb.append(" WHERE relid = 'historico'::regclass ");

		if (Uteis.isAtributoPreenchido(matricula) && !Uteis.isAtributoPreenchido(codigo) ) {
			sb.append(" AND row_data ? 'matricula' ");
		}
		if (!matricula.equals("")) {
			sb.append(" AND row_data ->> 'matricula' = '").append(matricula).append("' ");
		}
		if (!disciplina.equals(0)) {
			sb.append(" AND row_data ->> 'disciplina' = '").append(disciplina).append("' ");
		}
		if (!acao.equals("")) {
			sb.append(" AND action = '").append(acao).append("' ");
		}
		if (!codigo.equals(0)) {
			sb.append(" AND primary_key ->> 'codigo' = '").append(codigo).append("' ");
		}
		if (situacao != null) {
			sb.append(" AND row_data ->> 'situacao' = '").append(situacao.getValor()).append("' ");
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
		
		Boolean primeiro = true;
		if (historicoCamposAlteradosVO.getMediaFinal()) {
			sb.append(" and (changed_fields ? 'mediafinal' ");
			primeiro = false;
		}
		if (historicoCamposAlteradosVO.getSituacao()) {
			if (primeiro) {
				sb.append(" and (changed_fields ? 'situacao' ");
				primeiro = false;
			} else {
				sb.append(" or changed_fields ? 'situacao' ");
			}
		}
		if (historicoCamposAlteradosVO.getFrequencia()) {
			if (primeiro) {
				sb.append(" and (changed_fields ? 'freguencia' ");
				primeiro = false;
			} else {
				sb.append(" or changed_fields ? 'freguencia' ");
			}
		}
		if (historicoCamposAlteradosVO.getNota1()) {
			if (primeiro) {
				sb.append(" and (changed_fields ? 'nota1' ");
				primeiro = false;
			} else {
				sb.append(" or changed_fields ? 'nota1' ");
			}
		}
		if (historicoCamposAlteradosVO.getNota2()) {
			if (primeiro) {
				sb.append(" and (changed_fields ? 'nota2' ");
				primeiro = false;
			} else {
				sb.append(" or changed_fields ? 'nota2' ");
			}
		}
		if (historicoCamposAlteradosVO.getNota3()) {
			if (primeiro) {
				sb.append(" and (changed_fields ? 'nota3' ");
				primeiro = false;
			} else {
				sb.append(" or changed_fields ? 'nota3' ");
			}
		}
		if (historicoCamposAlteradosVO.getNota4()) {
			if (primeiro) {
				sb.append(" and (changed_fields ? 'nota4' ");
				primeiro = false;
			} else {
				sb.append(" or changed_fields ? 'nota4' ");
			}
		}
		if (historicoCamposAlteradosVO.getNota5()) {
			if (primeiro) {
				sb.append(" and (changed_fields ? 'nota5' ");
				primeiro = false;
			} else {
				sb.append(" or changed_fields ? 'nota5' ");
			}
		}
		if (historicoCamposAlteradosVO.getNota6()) {
			if (primeiro) {
				sb.append(" and (changed_fields ? 'nota6' ");
				primeiro = false;
			} else {
				sb.append(" or changed_fields ? 'nota6' ");
			}
		}
		if (historicoCamposAlteradosVO.getNota7()) {
			if (primeiro) {
				sb.append(" and (changed_fields ? 'nota7' ");
				primeiro = false;
			} else {
				sb.append(" or changed_fields ? 'nota7' ");
			}
		}
		if (historicoCamposAlteradosVO.getNota8()) {
			if (primeiro) {
				sb.append(" and (changed_fields ? 'nota8' ");
				primeiro = false;
			} else {
				sb.append(" or changed_fields ? 'nota8' ");
			}
		}
		if (historicoCamposAlteradosVO.getNota9()) {
			if (primeiro) {
				sb.append(" and (changed_fields ? 'nota9' ");
				primeiro = false;
			} else {
				sb.append(" or changed_fields ? 'nota9' ");
			}
		}
		if (historicoCamposAlteradosVO.getNota10()) {
			if (primeiro) {
				sb.append(" and (changed_fields ? 'nota10' ");
				primeiro = false;
			} else {
				sb.append(" or changed_fields ? 'nota10' ");
			}
		}
		
//		11 à 21
		
		if (historicoCamposAlteradosVO.getNota11()) {
			if (primeiro) {
				sb.append(" and (changed_fields ? 'nota11' ");
				primeiro = false;
			} else {
				sb.append(" or changed_fields ? 'nota11' ");
			}
		}
		if (historicoCamposAlteradosVO.getNota12()) {
			if (primeiro) {
				sb.append(" and (changed_fields ? 'nota12' ");
				primeiro = false;
			} else {
				sb.append(" or changed_fields ? 'nota12' ");
			}
		}
		if (historicoCamposAlteradosVO.getNota13()) {
			if (primeiro) {
				sb.append(" and (changed_fields ? 'nota13' ");
				primeiro = false;
			} else {
				sb.append(" or changed_fields ? 'nota13' ");
			}
		}
		if (historicoCamposAlteradosVO.getNota14()) {
			if (primeiro) {
				sb.append(" and (changed_fields ? 'nota14' ");
				primeiro = false;
			} else {
				sb.append(" or changed_fields ? 'nota14' ");
			}
		}
		if (historicoCamposAlteradosVO.getNota15()) {
			if (primeiro) {
				sb.append(" and (changed_fields ? 'nota15' ");
				primeiro = false;
			} else {
				sb.append(" or changed_fields ? 'nota15' ");
			}
		}
		if (historicoCamposAlteradosVO.getNota16()) {
			if (primeiro) {
				sb.append(" and (changed_fields ? 'nota16' ");
				primeiro = false;
			} else {
				sb.append(" or changed_fields ? 'nota16' ");
			}
		}
		if (historicoCamposAlteradosVO.getNota17()) {
			if (primeiro) {
				sb.append(" and (changed_fields ? 'nota17' ");
				primeiro = false;
			} else {
				sb.append(" or changed_fields ? 'nota17' ");
			}
		}
		if (historicoCamposAlteradosVO.getNota18()) {
			if (primeiro) {
				sb.append(" and (changed_fields ? 'nota18' ");
				primeiro = false;
			} else {
				sb.append(" or changed_fields ? 'nota18' ");
			}
		}
		if (historicoCamposAlteradosVO.getNota19()) {
			if (primeiro) {
				sb.append(" and (changed_fields ? 'nota19' ");
				primeiro = false;
			} else {
				sb.append(" or changed_fields ? 'nota19' ");
			}
		}
		if (historicoCamposAlteradosVO.getNota20()) {
			if (primeiro) {
				sb.append(" and (changed_fields ? 'nota20' ");
				primeiro = false;
			} else {
				sb.append(" or changed_fields ? 'nota20' ");
			}
		}
		
//		21 à 30
		
		if (historicoCamposAlteradosVO.getNota21()) {
			if (primeiro) {
				sb.append(" and (changed_fields ? 'nota21' ");
				primeiro = false;
			} else {
				sb.append(" or changed_fields ? 'nota21' ");
			}
		}
		if (historicoCamposAlteradosVO.getNota22()) {
			if (primeiro) {
				sb.append(" and (changed_fields ? 'nota22' ");
				primeiro = false;
			} else {
				sb.append(" or changed_fields ? 'nota22' ");
			}
		}
		if (historicoCamposAlteradosVO.getNota23()) {
			if (primeiro) {
				sb.append(" and (changed_fields ? 'nota23' ");
				primeiro = false;
			} else {
				sb.append(" or changed_fields ? 'nota23' ");
			}
		}
		if (historicoCamposAlteradosVO.getNota24()) {
			if (primeiro) {
				sb.append(" and (changed_fields ? 'nota24' ");
				primeiro = false;
			} else {
				sb.append(" or changed_fields ? 'nota24' ");
			}
		}
		if (historicoCamposAlteradosVO.getNota25()) {
			if (primeiro) {
				sb.append(" and (changed_fields ? 'nota25' ");
				primeiro = false;
			} else {
				sb.append(" or changed_fields ? 'nota25' ");
			}
		}
		if (historicoCamposAlteradosVO.getNota26()) {
			if (primeiro) {
				sb.append(" and (changed_fields ? 'nota26' ");
				primeiro = false;
			} else {
				sb.append(" or changed_fields ? 'nota26' ");
			}
		}
		if (historicoCamposAlteradosVO.getNota27()) {
			if (primeiro) {
				sb.append(" and (changed_fields ? 'nota27' ");
				primeiro = false;
			} else {
				sb.append(" or changed_fields ? 'nota27' ");
			}
		}
		if (historicoCamposAlteradosVO.getNota28()) {
			if (primeiro) {
				sb.append(" and (changed_fields ? 'nota28' ");
				primeiro = false;
			} else {
				sb.append(" or changed_fields ? 'nota28' ");
			}
		}
		if (historicoCamposAlteradosVO.getNota29()) {
			if (primeiro) {
				sb.append(" and (changed_fields ? 'nota29' ");
				primeiro = false;
			} else {
				sb.append(" or changed_fields ? 'nota29' ");
			}
		}
		if (historicoCamposAlteradosVO.getNota30()) {
			if (primeiro) {
				sb.append(" and (changed_fields ? 'nota30' ");
				primeiro = false;
			} else {
				sb.append(" or changed_fields ? 'nota30' ");
			}
		}
		
//		31 à 40
		
		if (historicoCamposAlteradosVO.getNota31()) {
			if (primeiro) {
				sb.append(" and (changed_fields ? 'nota31' ");
				primeiro = false;
			} else {
				sb.append(" or changed_fields ? 'nota31' ");
			}
		}
		if (historicoCamposAlteradosVO.getNota32()) {
			if (primeiro) {
				sb.append(" and (changed_fields ? 'nota32' ");
				primeiro = false;
			} else {
				sb.append(" or changed_fields ? 'nota32' ");
			}
		}
		if (historicoCamposAlteradosVO.getNota33()) {
			if (primeiro) {
				sb.append(" and (changed_fields ? 'nota33' ");
				primeiro = false;
			} else {
				sb.append(" or changed_fields ? 'nota33' ");
			}
		}
		if (historicoCamposAlteradosVO.getNota34()) {
			if (primeiro) {
				sb.append(" and (changed_fields ? 'nota34' ");
				primeiro = false;
			} else {
				sb.append(" or changed_fields ? 'nota34' ");
			}
		}
		if (historicoCamposAlteradosVO.getNota35()) {
			if (primeiro) {
				sb.append(" and (changed_fields ? 'nota35' ");
				primeiro = false;
			} else {
				sb.append(" or changed_fields ? 'nota35' ");
			}
		}
		if (historicoCamposAlteradosVO.getNota36()) {
			if (primeiro) {
				sb.append(" and (changed_fields ? 'nota36' ");
				primeiro = false;
			} else {
				sb.append(" or changed_fields ? 'nota36' ");
			}
		}
		if (historicoCamposAlteradosVO.getNota37()) {
			if (primeiro) {
				sb.append(" and (changed_fields ? 'nota37' ");
				primeiro = false;
			} else {
				sb.append(" or changed_fields ? 'nota37' ");
			}
		}
		if (historicoCamposAlteradosVO.getNota38()) {
			if (primeiro) {
				sb.append(" and (changed_fields ? 'nota38' ");
				primeiro = false;
			} else {
				sb.append(" or changed_fields ? 'nota38' ");
			}
		}
		if (historicoCamposAlteradosVO.getNota39()) {
			if (primeiro) {
				sb.append(" and (changed_fields ? 'nota39' ");
				primeiro = false;
			} else {
				sb.append(" or changed_fields ? 'nota39' ");
			}
		}
		if (historicoCamposAlteradosVO.getNota40()) {
			if (primeiro) {
				sb.append(" and (changed_fields ? 'nota40' ");
				primeiro = false;
			} else {
				sb.append(" or changed_fields ? 'nota40' ");
			}
		}
		
		if (!primeiro) {
			sb.append(") ");
		}
		return sb;
	}
	
	public String getAuditMesAno(String ano, String mes) {
		return "audit.audit_" + ano + "_" + mes;
	}
	
	public List<AuditVO> montarDadosConsulta(SqlRowSet dadosSQL, String coluna, UsuarioVO usuario) throws Exception  {
		List<AuditVO> listaLogAuditHistoricoVOs = new ArrayList<AuditVO>(0);
		while (dadosSQL.next()) {
			listaLogAuditHistoricoVOs.add(montarDados(dadosSQL, coluna, usuario));
		}
		return listaLogAuditHistoricoVOs;
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
		Map<Integer, DisciplinaVO> mapDisciplinaVOs = new HashMap<Integer, DisciplinaVO>(0);
		for(Entry<String, String> map : mapJsonRowDataVOs.entrySet()) {
			AtributoJsonVO atributoJsonVO = new AtributoJsonVO();
			atributoJsonVO.setAtributo(map.getKey());
			
			
			
			if (map.getKey().equals("disciplina")) {
				if (mapDisciplinaVOs.containsKey(Integer.parseInt(map.getValue()))) {
					DisciplinaVO disciplinaMapVO = mapDisciplinaVOs.get(Integer.parseInt(map.getValue()));
					atributoJsonVO.setValor(map.getValue() + " - " + disciplinaMapVO.getNome());
				} else {
					DisciplinaVO disc = getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimariaSemExcecao(Integer.parseInt(map.getValue()), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO);
					atributoJsonVO.setValor(map.getValue() + " - " + disc.getNome());
				}
			} else {
				atributoJsonVO.setValor(map.getValue());
			}
			
			if (map.getKey().equals("situacao")) {
				if (map.getValue().equals("RF")) {
					atributoJsonVO.setValor(map.getValue() + " - Reprovado Falta");
				} else if (map.getValue().equals("DP")) {
					atributoJsonVO.setValor(map.getValue() + " - Dependência");
				} else if (map.getValue().equals("RP")) {
					atributoJsonVO.setValor(map.getValue() + " - Reprovado no Período Letivo");
				} else if (map.getValue().equals("AP")) {
					atributoJsonVO.setValor(map.getValue() + " - Aprovado");
				} else if (map.getValue().equals("RE")) {
					atributoJsonVO.setValor(map.getValue() + " - Reprovado");
				} else if (map.getValue().equals("AA")) {
					atributoJsonVO.setValor(map.getValue() + " - Aprovado por Aproveitamento");
				} else if (map.getValue().equals("AE")) {
					atributoJsonVO.setValor(map.getValue() + " - Aprovado por Equivalência");
				} else if (map.getValue().equals("VS")) {
					atributoJsonVO.setValor(map.getValue() + " - Verificação Suplementar");
				} else if (map.getValue().equals("IS")) {
					atributoJsonVO.setValor(map.getValue() + " - Isento");
				} else if (map.getValue().equals("NC")) {
					atributoJsonVO.setValor(map.getValue() + " - Não Cursada");
				} else if (map.getValue().equals("CS")) {
					atributoJsonVO.setValor(map.getValue() + " - Cursando");
				} else if (map.getValue().equals("CE")) {
					atributoJsonVO.setValor(map.getValue() + " - Cursando por Equivalência");
				} else if (map.getValue().equals("CO")) {
					atributoJsonVO.setValor(map.getValue() + " - Cursando por Correspondência");
				} else if (map.getValue().equals("TR")) {
					atributoJsonVO.setValor(map.getValue() + " - Trancado");
				} else if (map.getValue().equals("AC")) {
					atributoJsonVO.setValor(map.getValue() + " - Abandono de Curso");
				} else if (map.getValue().equals("CA")) {
					atributoJsonVO.setValor(map.getValue() + " - Cancelado");
				} else if (map.getValue().equals("TF")) {
					atributoJsonVO.setValor(map.getValue() + " - Transferido");
				} else if (map.getValue().equals("CC")) {
					atributoJsonVO.setValor(map.getValue() + " - Crédito Concedido");
				} else if (map.getValue().equals("CH")) {
					atributoJsonVO.setValor(map.getValue() + " - Carga Horária Concedida");
				} else if (map.getValue().equals("PR")) {
					atributoJsonVO.setValor(map.getValue() + " - Pré-Matriculado");
				} else if (map.getValue().equals("AD")) {
					atributoJsonVO.setValor(map.getValue() + " - Aprov. c/ Dep.");
				} else if (map.getValue().equals("AB")) {
					atributoJsonVO.setValor(map.getValue() + " - Aproveitamento por Banca");
				} else if (map.getValue().equals("JU")) {
					atributoJsonVO.setValor(map.getValue() + " - Jubilado");
				} else if (map.getValue().equals("CD")) {
					atributoJsonVO.setValor(map.getValue() + " - Cursando c/ Dep.");
				} else {
					atributoJsonVO.setValor(map.getValue());
				}
			}
			
			listaAtributoJsonVOs.add(atributoJsonVO);
		}
		return listaAtributoJsonVOs;
	}
	
	public List<AtributoJsonVO> realizarConversaoAtributoJsonChanged_fields(String json, UsuarioVO usuarioVO) throws Exception, JsonMappingException, IOException {
		ObjectMapper mapperChangedFields = new ObjectMapper();
		 Map<String, String> mapJsonChangedFieldsVOs = mapperChangedFields.readValue(json, Map.class);
		 Map<Integer, DisciplinaVO> mapDisciplinaVOs = new HashMap<Integer, DisciplinaVO>(0);
		List<AtributoJsonVO> listaAtributoJsonVOs = new ArrayList<AtributoJsonVO>(0);
		for(Entry<String, String> map : mapJsonChangedFieldsVOs.entrySet()) {
			AtributoJsonVO atributoJsonVO = new AtributoJsonVO();
			atributoJsonVO.setAtributo(map.getKey());
			atributoJsonVO.setValor(map.getValue());
			
			if (map.getKey().equals("disciplina")) {
				if (mapDisciplinaVOs.containsKey(Integer.parseInt(map.getValue()))) {
					DisciplinaVO disciplinaMapVO = mapDisciplinaVOs.get(Integer.parseInt(map.getValue()));
					atributoJsonVO.setValor(map.getValue() + " - " + disciplinaMapVO.getNome());
				} else {
					DisciplinaVO disc = getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimariaSemExcecao(Integer.parseInt(map.getValue()), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO);
					atributoJsonVO.setValor(map.getValue() + " - " + disc.getNome());
				}
			} else {
				atributoJsonVO.setValor(map.getValue());
			}
			
			if (map.getKey().equals("situacao")) {
				if (map.getValue().equals("RF")) {
					atributoJsonVO.setValor(map.getValue() + " - Reprovado Falta");
				} else if (map.getValue().equals("DP")) {
					atributoJsonVO.setValor(map.getValue() + " - Dependência");
				} else if (map.getValue().equals("RP")) {
					atributoJsonVO.setValor(map.getValue() + " - Reprovado no Período Letivo");
				} else if (map.getValue().equals("AP")) {
					atributoJsonVO.setValor(map.getValue() + " - Aprovado");
				} else if (map.getValue().equals("RE")) {
					atributoJsonVO.setValor(map.getValue() + " - Reprovado");
				} else if (map.getValue().equals("AA")) {
					atributoJsonVO.setValor(map.getValue() + " - Aprovado por Aproveitamento");
				} else if (map.getValue().equals("AE")) {
					atributoJsonVO.setValor(map.getValue() + " - Aprovado por Equivalência");
				} else if (map.getValue().equals("VS")) {
					atributoJsonVO.setValor(map.getValue() + " - Verificação Suplementar");
				} else if (map.getValue().equals("IS")) {
					atributoJsonVO.setValor(map.getValue() + " - Isento");
				} else if (map.getValue().equals("NC")) {
					atributoJsonVO.setValor(map.getValue() + " - Não Cursada");
				} else if (map.getValue().equals("CS")) {
					atributoJsonVO.setValor(map.getValue() + " - Cursando");
				} else if (map.getValue().equals("CE")) {
					atributoJsonVO.setValor(map.getValue() + " - Cursando por Equivalência");
				} else if (map.getValue().equals("CO")) {
					atributoJsonVO.setValor(map.getValue() + " - Cursando por Correspondência");
				} else if (map.getValue().equals("TR")) {
					atributoJsonVO.setValor(map.getValue() + " - Trancado");
				} else if (map.getValue().equals("AC")) {
					atributoJsonVO.setValor(map.getValue() + " - Abandono de Curso");
				} else if (map.getValue().equals("CA")) {
					atributoJsonVO.setValor(map.getValue() + " - Cancelado");
				} else if (map.getValue().equals("TF")) {
					atributoJsonVO.setValor(map.getValue() + " - Transferido");
				} else if (map.getValue().equals("CC")) {
					atributoJsonVO.setValor(map.getValue() + " - Crédito Concedido");
				} else if (map.getValue().equals("CH")) {
					atributoJsonVO.setValor(map.getValue() + " - Carga Horária Concedida");
				} else if (map.getValue().equals("PR")) {
					atributoJsonVO.setValor(map.getValue() + " - Pré-Matriculado");
				} else if (map.getValue().equals("AD")) {
					atributoJsonVO.setValor(map.getValue() + " - Aprov. c/ Dep.");
				} else if (map.getValue().equals("AB")) {
					atributoJsonVO.setValor(map.getValue() + " - Aproveitamento por Banca");
				} else if (map.getValue().equals("JU")) {
					atributoJsonVO.setValor(map.getValue() + " - Jubilado");
				} else if (map.getValue().equals("CD")) {
					atributoJsonVO.setValor(map.getValue() + " - Cursando c/ Dep.");
				} else {
					atributoJsonVO.setValor(map.getValue());
				}
			}
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
	public List<String> consultarColunasAuditHistorico() {
		StringBuilder sb = new StringBuilder();
		sb.append("select initcap(column_name) as column_name from information_schema.columns WHERE table_name = 'historico' order by initcap(column_name)");
		List<String> listaColunaVOs =  new ArrayList<String>(0);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (tabelaResultado.next()) {
			listaColunaVOs.add(tabelaResultado.getString("column_name"));
		}
		return listaColunaVOs;
	}
	
	public static String getIdEntidade() {
		return LogAuditHistorico.idEntidade;
	}
	
	public void setIdEntidade(String idEntidade) {
		LogAuditHistorico.idEntidade = idEntidade;
	}
}
