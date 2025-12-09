/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.facade.jdbc.financeiro;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONString;
import org.json.simple.parser.JSONParser;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.InvalidResultSetAccessException;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import negocio.comuns.administrativo.AtributoJsonVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConsultaLogContaReceberVO;
import negocio.comuns.financeiro.ContaReceberLogVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoOrigemContaReceber;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.ConsultaLogContaReceberInterfaceFacade;

/**
 * 
 * @author Philippe
 */
@Repository
@Scope("singleton")
@Lazy
public class ConsultaLogContaReceber extends ControleAcesso implements ConsultaLogContaReceberInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7910019671102295880L;
	private static String idEntidade;

	public static String getIdEntidade() {
		return idEntidade;
	}

	public void setIdEntidade(String aIdEntidade) {
		idEntidade = aIdEntidade;
	}

	public ConsultaLogContaReceber() throws Exception {
		super();
		setIdEntidade("ConsultaLogContaReceber");
	}
	
	public void validarDados(String ano, String mes, UsuarioVO usuarioVO) throws Exception {
		if (ano.equals("")) {
			throw new Exception("O campo ANO deve ser informado.");
		}
		if (mes.equals("")) {
			throw new Exception("O campo MÊS deve ser informado.");
		}
	}
	
	@Override
	public List<ConsultaLogContaReceberVO> consultar(String matricula, String nossoNumero, Integer codigo, TipoOrigemContaReceber tipoOrigem, String ano, String mes, String acao, String coluna, Date dataInicio, Date dataFim, Date dataInicioRecebimento, Date dataFimRecebimento, UsuarioVO usuarioLogVO, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
//		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioVO);
		validarDados(ano, mes, usuarioVO);
		StringBuilder sb = new StringBuilder();
		if (!coluna.equals("")) {
			sb.append("select event_id, schema_name, session_user_name, usuario_logado, action_tstamp_clk, transaction_id, client_addr, action, changed_fields, row_data ->> '").append(coluna).append("' as ").append(coluna).append(" from ").append(getAuditMesAno(ano, mes));
		} else {
			sb.append("select * from ").append(getAuditMesAno(ano, mes));
		}
		sb.append(" where relid = 'contareceber'::regclass ");
		if (!matricula.equals("")) {
			sb.append(" and row_data ->> 'matriculaaluno' = '").append(matricula).append("' ");
		}
		if (!nossoNumero.equals("")) {
			sb.append(" and row_data ->> 'nossonumero' = '").append(nossoNumero).append("' ");
		}
		if (tipoOrigem != null) {
			sb.append(" and row_data ->> 'tipoorigem' = '").append(tipoOrigem.getValor()).append("' ");
		}
		if (!acao.equals("")) {
			sb.append(" and action = '").append(acao).append("' ");
		}
		if (!codigo.equals(0)) {
			sb.append(" and primary_key ->> 'codigo' = '").append(codigo).append("' ");
		}
		if (Uteis.isAtributoPreenchido(usuarioLogVO.getCodigo())) {
			sb.append(" and usuario_logado = ").append(usuarioLogVO.getCodigo());
		}
		if (dataInicio != null && dataFim != null) {
			sb.append(" and action_tstamp_clk::date >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ").append(" and action_tstamp_clk::date <= '").append(Uteis.getDataJDBC(dataFim)).append("' ");
		} else if (dataInicio != null && dataFim == null) {
			sb.append(" and action_tstamp_clk::date >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
		} else if (dataFim != null) {
			sb.append(" and action_tstamp_clk::date <= '").append(Uteis.getDataJDBC(dataFim)).append("' ");
		}
		if (dataInicioRecebimento != null || dataFimRecebimento != null) {
			sb.append(" and exists (");
			sb.append(" select * from ").append(getAuditMesAno(ano, mes)).append(" negociacaoRecebimento ");
			sb.append(" where relid = 'negociacaorecebimento'::regclass and exists (");
			sb.append(" select negociacaoRecebimento.primary_key ->> 'codigo' from ").append(getAuditMesAno(ano, mes)).append(" contaRecebernegociacaoRecebimento ");
			sb.append(" where relid = 'contarecebernegociacaorecebimento'::regclass ");
			sb.append(" and contaRecebernegociacaoRecebimento.row_data ->> 'negociacaorecebimento' = negociacaoRecebimento.primary_key ->> 'codigo' ");
			sb.append(" and contaRecebernegociacaoRecebimento.row_data ->> 'contareceber' = ").append(getAuditMesAno(ano, mes)).append(".primary_key ->> 'codigo') ");
			if (dataInicioRecebimento != null && dataFimRecebimento != null) {
				sb.append(" and cast(negociacaoRecebimento.row_data ->> 'data' as date) >= '").append(Uteis.getDataJDBC(dataInicioRecebimento)).append("' ").append(" and cast(negociacaoRecebimento.row_data ->> 'data' as date) <= '").append(Uteis.getDataJDBC(dataFimRecebimento)).append("' ");
			} else if (dataInicioRecebimento != null && dataFimRecebimento == null) {
				sb.append(" and cast(negociacaoRecebimento.row_data ->> 'data' as date) >= '").append(Uteis.getDataJDBC(dataInicioRecebimento)).append("' ");
			} else if (dataInicioRecebimento == null && dataFimRecebimento != null) {
				sb.append(" and cast(negociacaoRecebimento.row_data ->> 'data' as date) <= '").append(Uteis.getDataJDBC(dataFimRecebimento)).append("' ");
			}
			
			sb.append(") ");
			
		}
		sb.append(" order by action_tstamp_clk ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		return montarDadosConsulta(tabelaResultado, coluna, usuarioVO);
	}
	
	public String getAuditMesAno(String ano, String mes) {
		return "audit.audit_" + ano + "_" + mes;
	}
	
	@Override
	public List<String> consultarColunasContaReceber() {
		StringBuilder sb = new StringBuilder();
		sb.append("select column_name  from information_schema.columns WHERE table_name = 'contareceber' ");
		List<String> listaColunaVOs =  new ArrayList<String>(0);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (tabelaResultado.next()) {
			listaColunaVOs.add(tabelaResultado.getString("column_name"));
		}
		return listaColunaVOs;
	}
	
	public List<ConsultaLogContaReceberVO> montarDadosConsulta(SqlRowSet dadosSQL, String coluna, UsuarioVO usuario) throws Exception  {
		List<ConsultaLogContaReceberVO> listaConsultaLogContaReceberVOs = new ArrayList<ConsultaLogContaReceberVO>(0);
		while (dadosSQL.next()) {
			listaConsultaLogContaReceberVOs.add(montarDados(dadosSQL, coluna, usuario));
		}
		return listaConsultaLogContaReceberVOs;
	}

	private ConsultaLogContaReceberVO montarDados(SqlRowSet dadosSQL, String coluna, UsuarioVO usuario) throws Exception, JSONException {
		ConsultaLogContaReceberVO obj = new ConsultaLogContaReceberVO();
		obj.getAuditVO().setEvent_id(dadosSQL.getInt("event_id"));
		obj.getAuditVO().setSchema_name(dadosSQL.getString("schema_name"));
		obj.getAuditVO().setSession_user_name(dadosSQL.getString("session_user_name"));
		obj.getAuditVO().getUsuarioLogadoVO().setCodigo(dadosSQL.getInt("usuario_logado"));
		if (Uteis.isAtributoPreenchido(obj.getAuditVO().getUsuarioLogadoVO().getCodigo())) {
			obj.getAuditVO().setUsuarioLogadoVO(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getAuditVO().getUsuarioLogadoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));
		}
		obj.getAuditVO().setAction_tstamp_clk(dadosSQL.getTimestamp("action_tstamp_clk"));
		obj.getAuditVO().setTransaction_id(dadosSQL.getLong("transaction_id"));
		obj.getAuditVO().setClient_addr(dadosSQL.getString("client_addr"));
		obj.getAuditVO().setAction(dadosSQL.getString("action"));
		if (!coluna.equals("")) {
			AtributoJsonVO atributoJsonVO = new AtributoJsonVO();
			atributoJsonVO.setAtributo(coluna);
			atributoJsonVO.setValor(dadosSQL.getString(coluna));
			obj.getAuditVO().getListaAtributoJsonRow_dataVOs().add(atributoJsonVO);
		} else {
			obj.getAuditVO().setRow_data(new JSONObject(dadosSQL.getString("row_data")));
			obj.getAuditVO().setListaAtributoJsonRow_dataVOs(realizarConversaoAtributoJsonRow_data(dadosSQL.getString("row_data")));
		}
		if (dadosSQL.getString("changed_fields") != null) {
			obj.getAuditVO().setChanged_fields(new JSONObject(dadosSQL.getString("changed_fields")));
			obj.getAuditVO().setListaAtributoJsonChanged_fieldsVOs(realizarConversaoAtributoJsonChanged_fields(dadosSQL.getString("changed_fields")));
		}
		return obj;
	}
	
	public List<AtributoJsonVO> realizarConversaoAtributoJsonRow_data(String json) throws Exception, JsonMappingException, IOException {
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
	
	public List<AtributoJsonVO> realizarConversaoAtributoJsonChanged_fields(String json) throws Exception, JsonMappingException, IOException {
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
	
//	public List<ContaReceberLogVO> consultarPorSacado(String nomeSacado, boolean controlarAcesso, UsuarioVO usuario, Integer limit, Integer offset) throws Exception {
//		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
//		StringBuilder sql = new StringBuilder(getSQLPadraoConsultaCompleta());
//		sql.append(" WHERE ");
//		sql.append(" case tipoPessoa ");
//		sql.append(" when 'AL' then (sem_acentos ( upper (pessoamatricula.nome)) LIKE sem_acentos ( upper ( '%").append(nomeSacado).append("%'))) ");
//		sql.append(" when 'RF' then (sem_acentos ( upper (responsavelfinanceiro.nome)) LIKE sem_acentos ( upper ( '%").append(nomeSacado).append("%'))) or (sem_acentos ( upper (pessoamatricula.nome)) LIKE sem_acentos ( upper ( '%").append(nomeSacado).append("%'))) ");
//		sql.append(" when 'FU' then (sem_acentos ( upper (pessoafuncionario.nome)) LIKE sem_acentos ( upper ( '%").append(nomeSacado).append("%')))  ");
//		sql.append(" when 'CA' then (sem_acentos ( upper (pessoacandidato.nome)) LIKE sem_acentos ( upper ( '%").append(nomeSacado).append("%')))  ");
//		sql.append(" when 'CO' then (sem_acentos ( upper (convenio.descricao)) LIKE sem_acentos ( upper ( '%").append(nomeSacado).append("%')))  ");
//		sql.append(" when 'FO' then (sem_acentos ( upper (fornecedor.nome)) LIKE sem_acentos ( upper ( '%").append(nomeSacado).append("%')))  ");
//		sql.append(" when 'PA' then (sem_acentos ( upper (parceiro.nome)) LIKE sem_acentos ( upper ( '%").append(nomeSacado).append("%')))  or (sem_acentos ( upper (pessoamatricula.nome)) LIKE sem_acentos ( upper ( '%").append(nomeSacado).append("%')))  ");
//		sql.append(" else (sem_acentos ( upper (pessoa.nome)) LIKE sem_acentos ( upper ( '%").append(nomeSacado).append("%'))) end ");		
//		sql.append(" order by contareceberlog.datacriacao desc ");
//		if(limit != null && limit > 0 ){
//			sql.append(" limit ").append(limit).append(" offset ").append(offset);
//		}
//		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
//		List<ContaReceberLogVO> contaReceberVOs = new ArrayList<ContaReceberLogVO>(0);
//		while (tabelaResultado.next()) {
//			contaReceberVOs.add(montarDados(tabelaResultado, usuario));
//		}
//		return contaReceberVOs;
//	}
//	public List<ContaReceberLogVO> consultarPorMatricula(String nossoNumero, boolean controlarAcesso, UsuarioVO usuario, Integer limit, Integer offset) throws Exception {
//		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
//		StringBuilder sql = new StringBuilder(getSQLPadraoConsultaCompleta());
//		sql.append(" WHERE contareceberlog.matriculaAluno like '").append(nossoNumero).append("%'");
//		sql.append(" order by contareceberlog.datacriacao desc ");
//		if(limit != null && limit > 0 ){
//			sql.append(" limit ").append(limit).append(" offset ").append(offset);
//		}
//		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
//		List<ContaReceberLogVO> contaReceberVOs = new ArrayList<ContaReceberLogVO>(0);
//		while (tabelaResultado.next()) {
//			contaReceberVOs.add(montarDados(tabelaResultado, usuario));
//		}
//		return contaReceberVOs;
//	}

	
}
