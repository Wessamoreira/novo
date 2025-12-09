package negocio.facade.jdbc.arquitetura;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.TextAlign;
import org.apache.poi.xssf.usermodel.TextDirection;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFTextBox;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.arquitetura.CampoLogTriggerVO;
import negocio.comuns.arquitetura.QueryAtivaLogTriggerVO;
import negocio.comuns.arquitetura.RegistroLogTriggerVO;
import negocio.comuns.arquitetura.TriggerVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.TipoFiltroEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.interfaces.arquitetura.LogTriggerInterfaceFacade;

/**
 *
 * @author Alessandro Lima
 */
@Repository
@Scope("singleton")
@Lazy
public class LogTrigger extends ControleAcesso implements LogTriggerInterfaceFacade {

	private static final long serialVersionUID = 1L;

	public LogTrigger() throws Exception {
		super();
	}

	/**
	 * Consulta as tabelas do log trazendo informações de tamanho, indice
	 */
	@Override
	public List<TriggerVO> consultarTabelaLog() throws Exception {

		StringBuilder sql = new StringBuilder("select relname, pg_size_pretty(max(tabela)) as tabela, pg_size_pretty(max(indice)*8*1024) as indice, pg_size_pretty(max(toast)*8*1024) as toast from ( ");
		sql.append("select relname, case when relkind='r' then relpages else 0 end as tabela, ");
		sql.append("case when relkind='i' then relpages else 0 end as indice, ");
		sql.append("case when relkind='t' then relpages else 0 end as toast from ( ");
		/**
		 * Busca o tamanho da tabela de log
		 */
		sql.append("SELECT relname, pg_relation_size(relid) as relpages, 'r' as relkind ");
		sql.append("FROM pg_catalog.pg_statio_user_tables where relname  ilike 'audit%' ");
		sql.append("union ");
		/**
		 * Busca o tamanho da tabela em
		 */
		sql.append("SELECT toast.relname, sum(relpages) as relpages, 't' as relkind ");
		sql.append("FROM pg_class c, (SELECT relname, reltoastrelid FROM pg_class WHERE relname ilike 'audit%') AS toast ");
		sql.append("WHERE oid = toast.reltoastrelid OR oid = (SELECT reltoastrelid FROM pg_class WHERE oid = toast.reltoastrelid) ");
		sql.append("group by toast.relname ");
		sql.append("union ");
		/**
		 *
		 */
		sql.append("SELECT c.relname, sum(c2.relpages) as relpages, 'i' as relkind ");
		sql.append("FROM pg_class c, pg_class c2, pg_index i ");
		sql.append("WHERE c.oid = i.indrelid AND c2.oid = i.indexrelid and c.relname  ilike 'audit%' ");

		sql.append("group by c.relname) as x ");

		sql.append("group by relname, relkind, relpages ");

		sql.append(") as z ");

		sql.append("group by relname ");
		sql.append("order by relname");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<TriggerVO> logs = new ArrayList<TriggerVO>(0);
		while (rs.next()) {
			TriggerVO log = new TriggerVO();
			log.setTabela(rs.getString("relname"));
			if (rs.getString("relname").contains("audit_2")) {
				String anoMes = rs.getString("relname").replaceAll("audit_", "");
				log.setMesAnoLog(anoMes.substring(5, 7) + "/" + anoMes.substring(0, 4));
			}
			log.setTamanhoTabela(rs.getString("tabela"));
			log.setTamanhoIndice(rs.getString("indice"));
			log.setTamanhoToast(rs.getString("toast"));
			logs.add(log);
		}
		return logs;
	}

	@SuppressWarnings("unchecked")
	public List<TriggerVO> consultarTriggers() throws Exception {

		StringBuilder sql = new StringBuilder("select relname, pg_size_pretty(max(tabela)) as tabela, pg_size_pretty(max(indice)*8*1024) as indice, pg_size_pretty(max(toast)*8*1024) as toast, sum(logs) as logs from ( ");
		sql.append("select relname, logs, case when relkind='r' then relpages else 0 end as tabela, ");
		sql.append("case when relkind='i' then relpages else 0 end as indice, ");
		sql.append("case when relkind='t' then relpages else 0 end as toast from ( ");
		sql.append("SELECT relname, pg_relation_size(relid) as relpages, 'r' as relkind, 0 as logs ");
		sql.append("FROM pg_catalog.pg_statio_user_tables where schemaname = 'public' ");
		sql.append("union ");
		sql.append("SELECT toast.relname, sum(relpages) as relpages, 't' as relkind, 0 as logs ");
		sql.append("FROM pg_class c, (SELECT relname, reltoastrelid FROM pg_class) AS toast ");
		sql.append("WHERE (oid = toast.reltoastrelid OR oid = (SELECT reltoastrelid FROM pg_class WHERE oid = toast.reltoastrelid)) ");
		sql.append("and toast.relname in (select relname from pg_catalog.pg_statio_user_tables where schemaname = 'public') ");
		sql.append("group by toast.relname ");
		sql.append("union ");
		sql.append("SELECT c.relname, sum(c2.relpages) as relpages, 'i' as relkind, 0 as logs ");
		sql.append("FROM pg_class c, pg_class c2, pg_index i ");
		sql.append("WHERE c.oid = i.indrelid AND c2.oid = i.indexrelid and c.relname in (select relname from pg_catalog.pg_statio_user_tables where schemaname = 'public') ");
		sql.append("group by c.relname ");
		sql.append("union ");
		sql.append("select tables.table_name as relname, 0 as relpages, 'r' as relkind, count(triggers.event_object_table) as logs from information_schema.tables as tables ");
		sql.append("left join information_schema.triggers as triggers on triggers.event_object_table = tables.table_name and trigger_name = 'audit_trigger_row' ");
		sql.append("where tables.table_schema = 'public' and tables.table_type = 'BASE TABLE' group by tables.table_name ");
		sql.append(") as x ");
		sql.append("group by relname, logs, relkind, relpages ");
		sql.append(") as z ");
		sql.append("group by relname ");
		sql.append("order by relname");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<TriggerVO> triggers = new ArrayList<TriggerVO>(0);
		while (rs.next()) {
			TriggerVO trigger = new TriggerVO();
			trigger.setTabela(rs.getString("relname"));
			trigger.setTamanhoTabela(rs.getString("tabela"));
			trigger.setTamanhoIndice(rs.getString("indice"));
			trigger.setTamanhoToast(rs.getString("toast"));
			trigger.setQuantidade(rs.getInt("logs"));
			triggers.add(trigger);
		}
		Collections.sort(triggers, new TriggerComparator());
		return triggers;
	}

	@Override
	public void ativarTodasTriggersTabelas(List<TriggerVO> listaTriggers) throws Exception {

		for (TriggerVO trigger : listaTriggers) {
			ativarTriggerTabela(trigger);
		}
	}

	@Override
	public void desativarTodasTriggersTabelas(List<TriggerVO> listaTriggers) throws Exception {

		for (TriggerVO trigger : listaTriggers) {
			try {
				desativarTriggerTabela(trigger);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void ativarTriggerTabela(TriggerVO trigger) throws Exception {

		if (!trigger.getAtiva()) {
			StringBuilder sql = new StringBuilder("SELECT audit.audit_table('").append(trigger.getTabela()).append("');");
			try {
				getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
			} catch (Exception e) {
				throw e;
			}
		}
	}

	@Override
	public void desativarTriggerTabela(TriggerVO trigger) throws Exception {

		if (trigger.getAtiva()) {
			StringBuilder sql = new StringBuilder("DROP TRIGGER audit_trigger_row ON ").append(trigger.getTabela()).append(";");
			sql.append("DROP TRIGGER audit_trigger_stm ON ").append(trigger.getTabela()).append(";");
			try {
				getConexao().getJdbcTemplate().execute(sql.toString());
			} catch (Exception e) {
				throw e;
			}
		}
	}

	@Override
	public List<SelectItem> consultarUsuarios() throws Exception {

		List<SelectItem> usuarios = new ArrayList<SelectItem>(0);
		usuarios.add(new SelectItem(0, "Todos"));
		String sql = "select codigo, nome from public.usuario order by nome";
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql);
		while (rs.next()) {
			usuarios.add(new SelectItem(rs.getInt("codigo"), rs.getString("nome")));
		}
		return usuarios;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SelectItem> consultarTabelas() throws Exception {

		List<SelectItem> tabelas = new ArrayList<SelectItem>(0);
		String sql = "select table_name from information_schema.tables where table_schema = 'public' and table_type = 'BASE TABLE'";
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql);
		tabelas.add(new SelectItem("", "Todas"));
		while (rs.next()) {
			tabelas.add(new SelectItem(rs.getString("table_name"), rs.getString("table_name")));
		}
		Collections.sort(tabelas, new SelectItemComparator());
		return tabelas;
	}

	public void montarCampos(List<CampoLogTriggerVO> campos, final String tabela) throws Exception {
		String sql = "select column_name, data_type from information_schema.columns where table_name = ? and table_schema = 'public' order by column_name";
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql, tabela);
		while (rs.next()) {
			if (Uteis.isAtributoPreenchido(rs.getString("column_name"))) {
				campos.add(new CampoLogTriggerVO(rs.getString("column_name"), rs.getString("data_type")));
			}
		}
		// if (!campos.isEmpty()) {
		// campos.add(new CampoLogTriggerVO("", "text"));
		// }
	}

	@SuppressWarnings("rawtypes")
	private class SelectItemComparator implements Comparator {

		public int compare(Object obj1, Object obj2) {
			SelectItem selectItem1 = (SelectItem) obj1;
			SelectItem selectItem2 = (SelectItem) obj2;

			String label1 = selectItem1.getLabel();
			String label2 = selectItem2.getLabel();

			return label1.compareTo(label2);
		}
	}

	@SuppressWarnings("rawtypes")
	private class TriggerComparator implements Comparator {

		public int compare(Object obj1, Object obj2) {
			TriggerVO trigger1 = (TriggerVO) obj1;
			TriggerVO trigger2 = (TriggerVO) obj2;

			String label1 = trigger1.getTabela();
			String label2 = trigger2.getTabela();

			return label1.compareTo(label2);
		}
	}

	@SuppressWarnings("rawtypes")
	private class RegistroLogTriggerComparator implements Comparator {

		public int compare(Object obj1, Object obj2) {
			RegistroLogTriggerVO trigger1 = (RegistroLogTriggerVO) obj1;
			RegistroLogTriggerVO trigger2 = (RegistroLogTriggerVO) obj2;
			Date data1 = trigger1.getData();
			Date data2 = trigger2.getData();
			return data1.compareTo(data2);
		}
	}

	@Override
	public List<RegistroLogTriggerVO> executarConsultaRegistrosLogTrigger(final String tabela, Date dataInicial, Date dataFinal, final Integer usuario, final List<CampoLogTriggerVO> filtros, int limit, int offset) throws Exception {
		Map<String, Map<Date, Date>> tabelas = realizarSeparacaoTabelaFiltarPorPeriodo(dataInicial, dataFinal);
		StringBuilder sql = new StringBuilder("");
		StringBuilder sqlInterno = new StringBuilder("");
		sql.append(" SELECT event_id, action_tstamp_clk, client_query, session_user_name, ");
		sql.append(" (SELECT usuario.nome FROM usuario WHERE usuario.codigo = t.usuario_logado) AS usuario, ").append(" action, transaction_id, table_name ");
		sql.append(" FROM ( ");
		for (String from : tabelas.keySet()) {
			if (Uteis.isAtributoPreenchido(sqlInterno.toString())) {
				sqlInterno.append(" UNION ALL ");
			}

			sqlInterno.append(" SELECT event_id, action_tstamp_clk, client_query, session_user_name, usuario_logado, action, transaction_id, table_name ");
			sqlInterno.append(" FROM audit.").append(from);
			sqlInterno.append(" WHERE relid = '").append(tabela).append("'::regclass ");

			for (CampoLogTriggerVO filtro : filtros) {
				sqlInterno.append(montarFiltroUsandoRowDataOrChangedField(filtro));
			}
            dataInicial = tabelas.get(from).keySet().iterator().next();
			dataFinal = tabelas.get(from).get(dataInicial);
			executarFiltroPorPeriodo(sqlInterno, "action_tstamp_stm", dataInicial, dataFinal);
			executarFiltroPorUsuario(sqlInterno, "usuario_logado", usuario);

		}
		sql.append(sqlInterno);
		sql.append(" ) t ");
		sql.append(" ORDER BY action_tstamp_clk ");
		sql.append(" LIMIT ").append(limit);
		sql.append(" OFFSET ").append(offset);

		List<RegistroLogTriggerVO> logs = new ArrayList<RegistroLogTriggerVO>(0);
		try {
			logs = executarMontagemRegistros(getConexao().getJdbcTemplate(), sql, filtros);
		} catch (Exception e) {
			throw e;
		}
		return logs;
	}

	@Override
	public Integer executarConsultaTotalRegistrosLogTrigger(final String tabela, Date dataInicial, Date dataFinal, final Integer usuario, final List<CampoLogTriggerVO> filtros) throws Exception {
		StringBuilder sql = new StringBuilder(" SELECT COUNT(event_id) AS qtde FROM ( ");
		Map<String, Map<Date, Date>> tabelas = realizarSeparacaoTabelaFiltarPorPeriodo(dataInicial, dataFinal);
		StringBuilder sqlInterno = new StringBuilder("");
		for (String from : tabelas.keySet()) {
			if (Uteis.isAtributoPreenchido(sqlInterno.toString())) {
				sqlInterno.append(" UNION ALL ");
			}
			/*
			 * consulta todos os campos após INSERT, todos os campos antes de
			 * DELETE e apenas campos alterados de UPDATE
			 */
			sqlInterno.append(" ( SELECT event_id ");
			sqlInterno.append(" FROM audit.").append(from);
			sqlInterno.append(" WHERE relid = '").append(tabela).append("'::regclass ");
			dataInicial = tabelas.get(from).keySet().iterator().next();
			dataFinal = tabelas.get(from).get(dataInicial);
			executarFiltroPorPeriodo(sqlInterno, "action_tstamp_stm", dataInicial, dataFinal);
			executarFiltroPorUsuario(sqlInterno, "usuario_logado", usuario);
			for (CampoLogTriggerVO filtro : filtros) {
				sqlInterno.append(montarFiltroUsandoRowDataOrChangedField(filtro));
			}

 			sqlInterno.append(" )");
		}
		sql.append(sqlInterno);
		sql.append(") AS t ");
		return getConexao().getJdbcTemplate().queryForInt(sql.toString());
	}

	public StringBuilder montarFiltro(final CampoLogTriggerVO filtro, final String hstore) {
		StringBuilder retorno = new StringBuilder();
		if (filtro.getSelecionado()) {
            //Condição adicionado especificamente para atender alguns índices criado no BD e realizar uma busca otimizada
			if (( filtro.getNome().equals("codigo") || filtro.getNome().equals("nossonumero") || filtro.getNome().equals("matricula") ||  filtro.getNome().equals("contareceber") ||  filtro.getNome().equals("matriculaaluno")  ) && (filtro.getTipoFiltro().equals(TipoFiltroEnum.IGUAL_NUMERO) || filtro.getTipoFiltro().equals(TipoFiltroEnum.IGUAL_TEXTO)) && Uteis.isAtributoPreenchido(filtro.getInput())) {
				this.validarFiltroInput();
				if (filtro.getNome().equals("codigo")) {
					retorno.append(" AND primary_key -> '").append(filtro.getNome()).append("' = '").append(filtro.getInput()).append("' ");
				} else {
				 	retorno.append(" AND ").append(hstore).append(" ? '").append(filtro.getNome()).append("'");
					retorno.append(" AND ").append(hstore).append(" -> '").append(filtro.getNome()).append("' = '").append(filtro.getInput()).append("' ");
				}
            } else if ((filtro.getTipoFiltro().equals(TipoFiltroEnum.IGUAL_NUMERO) || filtro.getTipoFiltro().equals(TipoFiltroEnum.IGUAL_TEXTO)) && Uteis.isAtributoPreenchido(filtro.getInput())) {
				validarFiltroInput();
				retorno.append(" and ").append(hstore).append(" -> '").append(filtro.getNome()).append("' = '").append(filtro.getInput()).append("' ");
			} else if (filtro.getTipoFiltro().equals(TipoFiltroEnum.IGUAL_DATA) && Uteis.isAtributoPreenchido(filtro.getDataInicialCampo())) {
				validarFiltroDataInicial();
				retorno.append(" and ").append(hstore).append(" -> '").append(filtro.getNome()).append("' >= '").append(Uteis.getDataJDBCTimestamp(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(filtro.getDataInicialCampo()))).append("' ");
				retorno.append(" and ").append(hstore).append(" -> '").append(filtro.getNome()).append("' <= '").append(Uteis.getDataJDBCTimestamp(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(filtro.getDataInicialCampo()))).append("' ");
			} else if (filtro.getTipoFiltro().equals(TipoFiltroEnum.DIFERENTE_NUMERO) || filtro.getTipoFiltro().equals(TipoFiltroEnum.DIFERENTE_TEXTO)) {
				validarFiltroInput();
				retorno.append(" and ").append(hstore).append(" -> '").append(filtro.getNome()).append("' != '").append(filtro.getInput()).append("' ");
			} else if (filtro.getTipoFiltro().equals(TipoFiltroEnum.DIFERENTE_DATA)) {
				validarFiltroDataInicial();
				retorno.append(" and (").append(hstore).append(" -> '").append(filtro.getNome()).append("' < '").append(Uteis.getDataJDBCTimestamp(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(filtro.getDataInicialCampo()))).append("' ");
				retorno.append(" or ").append(hstore).append(" -> '").append(filtro.getNome()).append("' > '").append(Uteis.getDataJDBCTimestamp(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(filtro.getDataInicialCampo()))).append("') ");
			} else if (filtro.getTipoFiltro().equals(TipoFiltroEnum.CONTENDO_TEXTO)) {
				validarFiltroInput();
				retorno.append(" and ").append(hstore).append(" -> '").append(filtro.getNome()).append("' ilike '%").append(filtro.getInput()).append("%' ");
			} else if (filtro.getTipoFiltro().equals(TipoFiltroEnum.INICIANDO_COM_TEXTO)) {
				validarFiltroInput();
				retorno.append(" and ").append(hstore).append(" -> '").append(filtro.getNome()).append("' ilike '").append(filtro.getInput()).append("%' ");
			} else if (filtro.getTipoFiltro().equals(TipoFiltroEnum.TERMINANDO_COM_TEXTO)) {
				validarFiltroInput();
				retorno.append(" and ").append(hstore).append(" -> '").append(filtro.getNome()).append("' ilike '%").append(filtro.getInput()).append("' ");
			} else if (filtro.getTipoFiltro().equals(TipoFiltroEnum.MAIOR_IGUAL_NUMERO)) {
				validarFiltroInput();
				retorno.append(" and ").append(hstore).append(" -> '").append(filtro.getNome()).append("' >= '").append(filtro.getInput()).append("' ");
			} else if (filtro.getTipoFiltro().equals(TipoFiltroEnum.MAIOR_NUMERO)) {
				validarFiltroInput();
				retorno.append(" and ").append(hstore).append(" -> '").append(filtro.getNome()).append("' > '").append(filtro.getInput()).append("' ");
			} else if (filtro.getTipoFiltro().equals(TipoFiltroEnum.MENOR_IGUAL_NUMERO)) {
				validarFiltroInput();
				retorno.append(" and ").append(hstore).append(" -> '").append(filtro.getNome()).append("' <= '").append(filtro.getInput()).append("' ");
			} else if (filtro.getTipoFiltro().equals(TipoFiltroEnum.MENOR_NUMERO)) {
				validarFiltroInput();
				retorno.append(" and ").append(hstore).append(" -> '").append(filtro.getNome()).append("' < '").append(filtro.getInput()).append("' ");
			} else if (filtro.getTipoFiltro().equals(TipoFiltroEnum.CONTENDO_NUMEROS)) {
				validarFiltroInput();
				retorno.append(" and ").append(hstore).append(" -> '").append(filtro.getNome()).append("' in (").append(filtro.getInput()).append(") ");
			} else if (filtro.getTipoFiltro().equals(TipoFiltroEnum.NAO_CONTENDO_NUMEROS)) {
				validarFiltroInput();
				retorno.append(" and ").append(hstore).append(" -> '").append(filtro.getNome()).append("' not in (").append(filtro.getInput()).append(") ");
			} else if (filtro.getTipoFiltro().equals(TipoFiltroEnum.A_PARTIR_DATA)) {
				validarFiltroDataInicial();
				retorno.append(" and ").append(hstore).append(" -> '").append(filtro.getNome()).append("' >= '").append(Uteis.getDataJDBCTimestamp(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(filtro.getDataInicialCampo()))).append("' ");
			} else if (filtro.getTipoFiltro().equals(TipoFiltroEnum.ATE_DATA)) {
				validarFiltroDataFinal();
				retorno.append(" and ").append(hstore).append(" -> '").append(filtro.getNome()).append("' >= '").append(Uteis.getDataJDBCTimestamp(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(filtro.getDataInicialCampo()))).append("' ");
			} else if (filtro.getTipoFiltro().equals(TipoFiltroEnum.ENTRE_DATAS)) {
				validarFiltroDataInicial();
				validarFiltroDataFinal();
				retorno.append(" and ").append(hstore).append(" -> '").append(filtro.getNome()).append("' >= '").append(Uteis.getDataJDBCTimestamp(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(filtro.getDataInicialCampo()))).append("' ");
				retorno.append(" and ").append(hstore).append(" -> '").append(filtro.getNome()).append("' <= '").append(Uteis.getDataJDBCTimestamp(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(filtro.getDataFinalCampo()))).append("' ");
			} else if (filtro.getTipoFiltro().equals(TipoFiltroEnum.NAO_ENTRE_DATAS)) {
				validarFiltroDataInicial();
				validarFiltroDataFinal();
				retorno.append(" and ").append(hstore).append(" -> '").append(filtro.getNome()).append("' not between '").append(Uteis.getDataJDBCTimestamp(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(filtro.getDataInicialCampo()))).append("' and '").append(Uteis.getDataJDBCTimestamp(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(filtro.getDataFinalCampo()))).append("' ");
			} else if (filtro.getTipoFiltro().equals(TipoFiltroEnum.VERDADEIRO)) {
				retorno.append(" and ").append(hstore).append(" -> '").append(filtro.getNome()).append("' is true ");
			} else if (filtro.getTipoFiltro().equals(TipoFiltroEnum.FALSO)) {
				retorno.append(" and ").append(hstore).append(" -> '").append(filtro.getNome()).append("' is false ");
			} else if (filtro.getTipoFiltro().equals(TipoFiltroEnum.NULO)) {
				retorno.append(" and ").append(hstore).append(" -> '").append(filtro.getNome()).append("' is null ");
			} else if (filtro.getTipoFiltro().equals(TipoFiltroEnum.NAO_NULO)) {
				retorno.append(" and ").append(hstore).append(" -> '").append(filtro.getNome()).append("' is not  null ");
			}
		}
		return retorno;

	}

	private void validarFiltroInput() {

	}

	private void validarFiltroDataInicial() {

	}

	private void validarFiltroDataFinal() {

	}

	@Override
	public List<CampoLogTriggerVO> executarConsultaCamposEventoLogTrigger(final Long evento, final String tabela, Date dataTransacao) throws Exception {

		List<CampoLogTriggerVO> fields = new ArrayList<CampoLogTriggerVO>(0);
		String from = realizarObtencaoTabelaPorData(dataTransacao);
		StringBuilder sql = new StringBuilder("SELECT ");
		sql.append("(json_each_text(row_data::json)).key as chave, ");
		sql.append("(json_each_text(row_data::json)).value as valor ");
		sql.append("FROM audit.").append(from);
		sql.append(" WHERE relid = '").append(tabela).append("'::regclass ");
		sql.append(" AND event_id = ").append(evento);
		sql.append(" ORDER BY chave ");


		fields.addAll(executarMontagemCampos(getConexao().getJdbcTemplate(), sql));

		StringBuilder sqlAlterados = new StringBuilder("SELECT ");
		sqlAlterados.append("(json_each_text(changed_fields::json)).key as chave, ");
		sqlAlterados.append("(json_each_text(changed_fields::json)).value as valor ");
		sqlAlterados.append(" FROM audit.").append(from);
		sqlAlterados.append(" WHERE relid = '").append(tabela).append("'::regclass ");
		sqlAlterados.append(" AND event_id = ").append(evento);
		sqlAlterados.append(" ORDER BY chave");

		List<CampoLogTriggerVO> changedFields = new ArrayList<CampoLogTriggerVO>(0);

		changedFields.addAll(executarMontagemCampos(getConexao().getJdbcTemplate(), sqlAlterados));

		for (CampoLogTriggerVO campoAlterado : changedFields) {
			for (CampoLogTriggerVO campo : fields) {
				if (campo.getNome().equals(campoAlterado.getNome())) {
					campo.setValorAlterado(campoAlterado.getValor());
				}
			}
		}

		return fields;
	}

	@Override
	public List<RegistroLogTriggerVO> executarConsultaRegistrosMesmaTransacao(final Long transacao, Date dataTransacao) throws Exception {

		List<RegistroLogTriggerVO> registros = new ArrayList<RegistroLogTriggerVO>(0);

		StringBuilder sql = new StringBuilder("select event_id, transaction_id, table_name, session_user_name, action_tstamp_clk, client_query, action, usuario.nome as usuario ");
		String from = realizarObtencaoTabelaPorData(dataTransacao);
		sql.append(" from audit.").append(from);
		sql.append(" left join usuario on   usuario.codigo::VARCHAR = audit." + from + ".session_user_name ");
		sql.append(" where schema_name = 'public' and transaction_id = ").append(transacao);
		sql.append(" order by table_name");

		registros.addAll(executarMontagemRegistros(getConexao().getJdbcTemplate(), sql, null));

		return registros;
	}

	private String realizarObtencaoTabelaPorData(Date data) throws Exception {
		int mes = Uteis.getMesData(data);
		String tabela = "audit_" + Uteis.getAno(data) + "_" + (mes < 10 ? "0" + Uteis.getMesData(data) : mes);
		return tabela;
	}

	private void validarDados(Date dataInicial, Date dataFinal) throws ConsistirException {
		if (!Uteis.isAtributoPreenchido(dataInicial)) {
			throw new ConsistirException("O campo DATA INICIAL deve ser informado.");
		}
		if (!Uteis.isAtributoPreenchido(dataFinal)) {
			throw new ConsistirException("O campo DATA FINAL deve ser informado.");
		}
		if (dataInicial.compareTo(dataFinal) > 0) {
			throw new ConsistirException("O campo DATA INICIAL não pode ser maior que a DATA FINAL deve ser informado.");
		}
	}

	private Map<String, Map<Date, Date>> realizarSeparacaoTabelaFiltarPorPeriodo(Date dataInicial, Date dataFinal) throws Exception {
		validarDados(dataInicial, dataFinal);
		Map<String, Map<Date, Date>> tabelas = new HashMap<String, Map<Date, Date>>(0);

		while (Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(dataInicial).compareTo(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(dataFinal)) <= 0) {
			Map<Date, Date> periodo = new HashMap<Date, Date>();
			if (Uteis.getDataUltimoDiaMes(dataInicial).compareTo(dataFinal) > 0) {
				periodo.put(dataInicial, Uteis.getDataUltimoDiaMes(dataFinal));
			} else {
				periodo.put(dataInicial, Uteis.getDataUltimoDiaMes(dataInicial));
			}
			int mes = Uteis.getMesData(dataInicial);
			String tabela = "audit_" + Uteis.getAno(dataInicial) + "_" + (mes < 10 ? "0" + Uteis.getMesData(dataInicial) : mes);
			StringBuilder sql = new StringBuilder("");
			sql.append(" select c.relname from pg_catalog.pg_class c ");
			sql.append(" JOIN   pg_catalog.pg_namespace n ON n.oid = c.relnamespace");
			sql.append(" WHERE  c.relkind = 'r' AND c.relname = '").append(tabela).append("' AND n.nspname = 'audit' ");
			if (getConexao().getJdbcTemplate().queryForRowSet(sql.toString()).next()) {
				tabelas.put(tabela, periodo);
			}
			dataInicial = Uteis.getDataPrimeiroDiaMes(Uteis.obterDataAvancadaPorMes(dataInicial, 1));
		}
		if (tabelas.isEmpty()) {
			throw new Exception("Não foi localizado nenhuma tabela de log disponível para o período.");
		}
		return tabelas;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RegistroLogTriggerVO> executarConsultaRegistrosPorDataUsuarioTabela(Date dataInicial, Date dataFinal, final Integer usuario, final String tabela, int limit, int offset) throws Exception {
		List<RegistroLogTriggerVO> registros = new ArrayList<RegistroLogTriggerVO>(0);
		Map<String, Map<Date, Date>> tabelas = realizarSeparacaoTabelaFiltarPorPeriodo(dataInicial, dataFinal);
		StringBuilder sqlInterno = new StringBuilder("");
		StringBuilder sql = new StringBuilder("");
		sql.append(" SELECT event_id, transaction_id, table_name, session_user_name, action_tstamp_clk, client_query, action, ");
		sql.append(" (SELECT usuario.nome FROM usuario WHERE usuario.codigo = t.usuario_logado) AS usuario ");
		sql.append(" FROM ( ");
		for (String from : tabelas.keySet()) {
			if (Uteis.isAtributoPreenchido(sqlInterno.toString())) {
				sqlInterno.append(" UNION ALL ");
			}
			sqlInterno.append(" SELECT event_id, transaction_id, table_name, session_user_name, action_tstamp_clk, client_query, action, usuario_logado ");
			sqlInterno.append(" FROM audit.").append(from);
			if (!tabela.isEmpty()) {
				sqlInterno.append(" WHERE relid = '").append(tabela).append("'::regclass ");
			}else {
				sqlInterno.append(" WHERE 1=1");
			}
			dataInicial = tabelas.get(from).keySet().iterator().next();
			dataFinal = tabelas.get(from).get(dataInicial);
			executarFiltroPorPeriodo(sqlInterno, "action_tstamp_stm", dataInicial, dataFinal);
			executarFiltroPorUsuario(sqlInterno, "usuario_logado", usuario);
		}
		sql.append(sqlInterno);
		sql.append(" ) t ");
		sql.append(" ORDER BY action_tstamp_clk ");
		sql.append(" LIMIT ").append(limit);
		sql.append(" OFFSET ").append(offset);
		registros.addAll(executarMontagemRegistros(getConexao().getJdbcTemplate(), sql, null));
		Collections.sort(registros, new RegistroLogTriggerComparator());
		return registros;
	}

	@Override
	public Integer executarConsultaTotalRegistroPorDataUsuarioTabela(Date dataInicial, Date dataFinal, final Integer usuario, final String tabela) throws Exception {
		Map<String, Map<Date, Date>> tabelas = realizarSeparacaoTabelaFiltarPorPeriodo(dataInicial, dataFinal);
		StringBuilder sql = new StringBuilder("");
		for (String from : tabelas.keySet()) {
			if (Uteis.isAtributoPreenchido(sql.toString())) {
				sql.append(" UNION ALL ");
			}
			sql.append("SELECT COUNT (event_id) AS qtde ");
			sql.append(" FROM audit.").append(from);
			if (!tabela.isEmpty()) {
				sql.append(" WHERE relid = '").append(tabela).append("'::regclass ");
			}else {
				sql.append(" WHERE 1=1");
			}
			dataInicial = tabelas.get(from).keySet().iterator().next();
			dataFinal = tabelas.get(from).get(dataInicial);
			executarFiltroPorPeriodo(sql, "action_tstamp_stm", dataInicial, dataFinal);
			executarFiltroPorUsuario(sql, "usuario_logado", usuario);
		}
		return getConexao().getJdbcTemplate().queryForInt("SELECT SUM(qtde) AS qtde FROM (" + sql.toString() + ") AS t");
	}

	public void executarFiltroPorPeriodo(StringBuilder sql, final String campo, final Date dataInicial, final Date dataFinal) throws Exception {
		if (dataInicial != null && dataFinal != null) {
			sql.append(" and ").append(campo).append(" >= '").append(Uteis.getDataJDBCTimestamp(Uteis.getDateHoraInicialDia(dataInicial))).append("' and ").append(campo).append(" <= '").append(Uteis.getDataJDBCTimestamp(Uteis.getDateHoraFinalDia(dataFinal))).append("'");
		} else if (dataInicial != null) {
			sql.append(" and ").append(campo).append(" >= '").append(Uteis.getDataJDBCTimestamp(Uteis.getDateHoraInicialDia(dataInicial))).append("'");
		} else if (dataFinal != null) {
			sql.append(" and ").append(campo).append(" <= '").append(Uteis.getDataJDBCTimestamp(Uteis.getDateHoraFinalDia(dataFinal))).append("'");
		}
	}

	public void executarFiltroPorUsuario(StringBuilder sql, final String campo, final Integer usuario) throws Exception {

		if (usuario != null && usuario.intValue() != 0) {
			sql.append(" AND ").append(campo).append(" = '").append(usuario).append("'");
		}
	}

	@Override
	public Map<Integer, String> consultarUsuarios(JdbcTemplate template, StringBuilder sqlUser) throws Exception {

		Map<Integer, String> usuarios = new HashMap<Integer, String>(0);
		SqlRowSet rsUser = template.queryForRowSet(sqlUser.toString());
		if (rsUser.next()) {
			if (!rsUser.getString("users").isEmpty()) {
				StringBuilder sql = new StringBuilder("select codigo, username from usuario where codigo in (").append(rsUser.getString("users")).append(")");
				SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
				while (rs.next()) {
					usuarios.put(rs.getInt("codigo"), rs.getString("username"));
				}
			}
		}
		return usuarios;
	}

	public List<RegistroLogTriggerVO> executarMontagemRegistros(JdbcTemplate template, final StringBuilder sql, final List<CampoLogTriggerVO> filtros) throws Exception {

		List<RegistroLogTriggerVO> logs = new ArrayList<RegistroLogTriggerVO>(0);
		SqlRowSet rs = template.queryForRowSet(sql.toString());
		while (rs.next()) {
			RegistroLogTriggerVO registro = new RegistroLogTriggerVO();

			registro.setId(rs.getLong("event_id"));
			registro.setTabela(rs.getString("table_name"));
			registro.setData(rs.getTimestamp("action_tstamp_clk"));
			registro.setUsuario(rs.getString("usuario"));
			registro.setQuery(rs.getString("client_query"));
			registro.setAction(rs.getString("action"));
			registro.setTransaction(rs.getLong("transaction_id"));

			if (filtros != null) {
//				for (CampoLogTriggerVO campo : filtros) {
//					CampoLogTriggerVO campoVO = new CampoLogTriggerVO();
//					campoVO.setNome(campo.getNome());
//					String aux = rs.getString(campo.getNome());
//					if (aux == null) {
//						campoVO.setValor("NULL");
//					} else {
//						campoVO.setValor(aux);
//					}
//					registro.getCampos().add(campoVO);
//				}
			}

			logs.add(registro);
		}
		return logs;
	}

	public List<CampoLogTriggerVO> executarMontagemCampos(JdbcTemplate template, final StringBuilder sql) throws Exception {

		List<CampoLogTriggerVO> fields = new ArrayList<CampoLogTriggerVO>(0);
		SqlRowSet rs = template.queryForRowSet(sql.toString());
		while (rs.next()) {
			CampoLogTriggerVO campoVO = new CampoLogTriggerVO();
			campoVO.setNome(rs.getString("chave"));
			campoVO.setValor(rs.getString("valor"));
			fields.add(campoVO);
		}
		return fields;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarCriacaoTabelaLogPorMesAno(String mes, String ano, ProgressBarVO progressBarVO) throws Exception {
		String tabela = "audit_" + ano + "_" + mes;
		StringBuilder sql = new StringBuilder("");
		sql.append(" select c.relname from pg_catalog.pg_class c ");
		sql.append(" JOIN   pg_catalog.pg_namespace n ON n.oid = c.relnamespace");
		sql.append(" WHERE  c.relkind = 'r' AND c.relname = '").append(tabela).append("' AND n.nspname = 'audit' ");
//		if (!getConexao().getJdbcTemplate().queryForRowSet(sql.toString()).next()) {
			Date dataInicio = Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(Uteis.getDataPrimeiroDiaMes(Uteis.getData(ano + "/" + mes + "/01", "yyyy/MM/dd")));
			Date dataFim = Uteis.getDataComHoraSetadaParaUltimoMinutoDia(Uteis.getDataUltimoDiaMes(Uteis.getData(ano + "/" + mes + "/01", "yyyy/MM/dd")));
			progressBarVO.setMaxValue(Uteis.getDiaMesData(dataFim));
			while (dataInicio.compareTo(dataFim) <= 0) {
				progressBarVO.setStatus("Extraindo log do dia " + Uteis.getData(dataInicio));
				progressBarVO.setProgresso(Long.valueOf(Uteis.getDiaMesData(dataInicio)));
				sql = new StringBuilder(" insert into audit.audit_temp (select * from only audit.audit where ");
				sql.append(" action_tstamp_stm >=  '").append(Uteis.getDataJDBC(dataInicio)).append(" 00:00:00.000-00' ");
				sql.append(" and action_tstamp_stm <=  '").append(Uteis.getDataJDBC(dataInicio)).append(" 23:59:59.999' ) ");
				int x = getConexao().getJdbcTemplate().update(sql.toString());
				if (x == 0) {
					sql = new StringBuilder("delete from only audit.audit where ");
					sql.append(" action_tstamp_stm >=  '").append(Uteis.getDataJDBC(dataInicio)).append(" 00:00:00.000-00' ");
					sql.append(" and action_tstamp_stm <=  '").append(Uteis.getDataJDBC(dataInicio)).append(" 23:59:59.999' ");
					getConexao().getJdbcTemplate().execute(sql.toString());
				}
				dataInicio = Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(Uteis.obterDataFutura(dataInicio, 1));
			}
//		} else {
//			throw new Exception("Já existe a tabela de log para o mês e ano informado.");
//		}
	}

	@Override
	@Transactional(readOnly = true, isolation = Isolation.DEFAULT, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public Map<String, Object> realizarConsultaParametrizada(String sql, int limit, int offset) throws Exception {
		String sqlValidar = sql.toLowerCase();
		if (sqlValidar.contains("insert") || sqlValidar.contains("update") || sqlValidar.contains("truncate") || sqlValidar.contains("delete") || sqlValidar.contains("create") || sqlValidar.contains("replace") || sqlValidar.contains("drop")) {
			throw new Exception("Não é possível executar comandos de INSERT, UPDATE, DELETE, DROP, CREATE, TRUNCATE ou REPLACE.");
		}
		List<List<CampoLogTriggerVO>> registroLogTriggerVOs = new ArrayList<List<CampoLogTriggerVO>>(0);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql);
		String[] colunas = rs.getMetaData().getColumnNames();
		Integer nrLinha = 0;
		CampoLogTriggerVO campo = null;
		while (rs.next()) {
			nrLinha++;
			Integer nrColuna = 1;
			List<CampoLogTriggerVO> registroLogTriggerVO = new ArrayList<CampoLogTriggerVO>(0);
			for (String coluna : colunas) {
				campo = new CampoLogTriggerVO();
				campo.setLinha(nrLinha);
				campo.setColuna(nrColuna++);
				campo.setNome(coluna);
				if (rs.getObject(coluna) instanceof String) {
					campo.setTipo("TEXTO");
				} else if (rs.getObject(coluna) instanceof Integer || rs.getObject(coluna) instanceof BigInteger || rs.getObject(coluna) instanceof Long) {
					campo.setTipo("INTEIRO");
				} else if (rs.getObject(coluna) instanceof Double || rs.getObject(coluna) instanceof BigDecimal) {
					campo.setTipo("DOUBLE");
				} else if (rs.getObject(coluna) instanceof Boolean) {
					campo.setTipo("BOOLEAN");
				} else if (rs.getObject(coluna) instanceof Date) {
					campo.setTipo("Data");
				}
				if (rs.getObject(coluna) != null) {
					campo.setValor(rs.getObject(coluna).toString());
				} else {
					campo.setValor("");
				}
				registroLogTriggerVO.add(campo);
			}
			registroLogTriggerVOs.add(registroLogTriggerVO);
		}
		Map<String, Object> resultado = new HashMap<String, Object>(0);
		resultado.put("COLUNAS", colunas);
		resultado.put("LISTA", registroLogTriggerVOs);
		return resultado;
	}

	@Override
	public List<RegistroLogTriggerVO> consultarScripts() {
		StringBuilder sql = new StringBuilder("SELECT query, nome, data, usuario, codigo from RegistroLogTrigger order by data desc ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<RegistroLogTriggerVO> registroLogTriggerVOs = new ArrayList<RegistroLogTriggerVO>(0);
		RegistroLogTriggerVO obj = null;
		while (rs.next()) {
			obj = new RegistroLogTriggerVO();
			obj.setQuery(rs.getString("query"));
			obj.setAction(rs.getString("nome"));
			obj.setUsuario(rs.getString("usuario"));
			obj.setData(rs.getTimestamp("data"));
			obj.setId(rs.getLong("codigo"));
			registroLogTriggerVOs.add(obj);
		}
		return registroLogTriggerVOs;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(RegistroLogTriggerVO registroLogTriggerVO, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder("DELETE FROM RegistroLogTrigger where codigo = ? " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString(), registroLogTriggerVO.getId());
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(final RegistroLogTriggerVO registroLogTriggerVO, final UsuarioVO usuarioVO) throws Exception {

		if (registroLogTriggerVO.getId().equals(0l)) {
			incluir(registroLogTriggerVO, usuarioVO);
		} else {
			alterar(registroLogTriggerVO, usuarioVO);
		}
	}

	public void validarDadosQuery(RegistroLogTriggerVO registroLogTriggerVO) throws ConsistirException {
		if (registroLogTriggerVO.getQuery().trim().isEmpty()) {
			throw new ConsistirException("O campo QUERY deve ser informado");
		}
		if (registroLogTriggerVO.getAction().trim().isEmpty()) {
			throw new ConsistirException("O campo NOME deve ser informado");
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final RegistroLogTriggerVO registroLogTriggerVO, final UsuarioVO usuarioVO) throws Exception {
		// LogTrigger.incluir("RegistroLogTrigger", true, usuarioVO);
		validarDadosQuery(registroLogTriggerVO);
		registroLogTriggerVO.setData(new Date());
		registroLogTriggerVO.setUsuario(usuarioVO.getNome());
		final StringBuilder sqlInserir = new StringBuilder();
		sqlInserir.append("INSERT INTO RegistroLogTrigger (query, nome, data, usuario) values (?, ?, ?, ?) returning codigo " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		registroLogTriggerVO.setId(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement ps = arg0.prepareStatement(sqlInserir.toString());
				ps.setString(1, registroLogTriggerVO.getQuery());
				ps.setString(2, registroLogTriggerVO.getAction());
				ps.setTimestamp(3, Uteis.getDataJDBCTimestamp(registroLogTriggerVO.getData()));
				ps.setString(4, usuarioVO.getNome());
				return ps;
			}
		}, new ResultSetExtractor<Long>() {
			@Override
			public Long extractData(ResultSet arg0) throws SQLException, DataAccessException {
				if (arg0.next()) {
					return arg0.getLong("codigo");
				}
				return null;
			}
		}));
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final RegistroLogTriggerVO registroLogTriggerVO, final UsuarioVO usuarioVO) throws Exception {
		// LogTrigger.alterar("RegistroLogTrigger", true, usuarioVO);
		validarDadosQuery(registroLogTriggerVO);
		registroLogTriggerVO.setData(new Date());
		registroLogTriggerVO.setUsuario(usuarioVO.getNome());
		final StringBuilder sqlInserir = new StringBuilder();
		sqlInserir.append(" UPDATE RegistroLogTrigger set query = ?, nome = ?, data = ?, usuario = ? where codigo = ? " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement ps = arg0.prepareStatement(sqlInserir.toString());
				ps.setString(1, registroLogTriggerVO.getQuery());
				ps.setString(2, registroLogTriggerVO.getAction());
				ps.setTimestamp(3, Uteis.getDataJDBCTimestamp(registroLogTriggerVO.getData()));
				ps.setString(4, usuarioVO.getNome());
				ps.setLong(5, registroLogTriggerVO.getId());
				return ps;
			}
		}) == 0) {
			incluir(registroLogTriggerVO, usuarioVO);
			return;
		}
		;
	}

	@Override
	public String realizarCriacaoArquivo(RegistroLogTriggerVO registroLogTriggerVO, String urlLogoPadraoRelatorio, List<List<CampoLogTriggerVO>> campoLogTriggerVOs, String[] colunas, UsuarioVO usuarioVO) throws Exception {
		String nomeArquivo = "LOG_" + Uteis.getData(new Date(), "dd_MM_yy_hh_mm_ss") + "_" + usuarioVO.getCodigo() + ".xlsx";
		FileOutputStream fileOut = null;
		XSSFWorkbook workbook = null;
		XSSFSheet worksheetfiltros = null;
		XSSFSheet worksheetresultado = null;
		try {
			fileOut = new FileOutputStream(UteisJSF.getCaminhoWeb() + File.separator + "relatorio" + File.separator + nomeArquivo);
			workbook = new XSSFWorkbook();
			worksheetresultado = workbook.createSheet("Resultado Relatório");
			worksheetresultado.setAutobreaks(true);
			worksheetresultado.setVerticallyCenter(true);
			worksheetresultado.createFreezePane(0, 7);

			realizarGeracaoTopoPadraoRelatorio(registroLogTriggerVO, colunas, workbook, worksheetresultado, urlLogoPadraoRelatorio);
			realizarGeracaoCabecalhoRelatorio(worksheetresultado, colunas);
			realizarGeracaoCorpoRelatorio(worksheetresultado, campoLogTriggerVOs);

			worksheetfiltros = workbook.createSheet("Query do Relatório");
			worksheetfiltros.setAutobreaks(true);
			worksheetfiltros.setVerticallyCenter(true);
			realizarGeracaoTopoPadraoRelatorio(registroLogTriggerVO, colunas, workbook, worksheetfiltros, urlLogoPadraoRelatorio);
			realizarGeracaoFiltrosRelatoriosRelatorio(registroLogTriggerVO, workbook, worksheetfiltros);
			workbook.write(fileOut);

		} catch (Exception e) {
			throw e;
		} finally {
			if (fileOut != null) {
				fileOut.flush();
				fileOut.close();
			}
			fileOut = null;
			workbook = null;
			worksheetfiltros = null;
			worksheetresultado = null;
		}

		return nomeArquivo;

	}

	public void realizarGeracaoTopoPadraoRelatorio(RegistroLogTriggerVO registroLogTriggerVO, String[] colunas, XSSFWorkbook workbook, XSSFSheet worksheet, String caminhoLogo) throws Exception {
		int qtdeColuna = 0;
		int pictureIndex = realizarImportacaoImagemExcel(workbook, caminhoLogo);
		
		worksheet.createRow(worksheet.getLastRowNum() + 1);
		worksheet.createRow(worksheet.getLastRowNum() + 1);
		worksheet.createRow(worksheet.getLastRowNum() + 1);
		worksheet.createRow(worksheet.getLastRowNum() + 1);
		worksheet.createRow(worksheet.getLastRowNum() + 1);

		if (!worksheet.getSheetName().contains("Filtros")) {
			int coluna = 0;
			qtdeColuna = colunas.length;
			for (String campo : colunas) {
				worksheet.setColumnWidth(coluna, 10000);
				coluna++;
			}
		} else {			
			worksheet.setColumnWidth(1, 31000);					
		}
		CellRangeAddress rangerAddress = new CellRangeAddress(0, 4, 0, 0);		
		worksheet.addMergedRegion(rangerAddress);				
		CellRangeAddress rangerAddress2 = new CellRangeAddress(0, 4, 1, qtdeColuna);		
		worksheet.addMergedRegion(rangerAddress2);				
		if(!registroLogTriggerVO.getAction().trim().isEmpty()) {
			XSSFClientAnchor anchor2 = new XSSFClientAnchor(0, 0, 1023, 180, 1, 0, qtdeColuna+1, 5);			
			anchor2.setAnchorType(2);	
			XSSFDrawing patriarch = worksheet.createDrawingPatriarch();
			XSSFTextBox textbox1 = patriarch.createTextbox(anchor2);
			XSSFRichTextString texto = new XSSFRichTextString(registroLogTriggerVO.getAction());				
			textbox1.setText(texto);
			textbox1.setVerticalAlignment(VerticalAlignment.CENTER);											
			textbox1.setTextDirection(TextDirection.HORIZONTAL);
			textbox1.getTextParagraphs().get(0).setTextAlign(TextAlign.CENTER);
			worksheet.autoSizeColumn(1);
//			textbox1.getTextParagraphs().get(0).setBulletFontSize(16);
		}
		XSSFDrawing patriarch = worksheet.createDrawingPatriarch();		
		XSSFClientAnchor anchor = null;
		anchor = new XSSFClientAnchor(0, 0, (short) 1023, 180, (short) 0, 0, 1, 5);		
		patriarch.createPicture(anchor, pictureIndex);
		worksheet.autoSizeColumn(0);
		
	}

	private int realizarImportacaoImagemExcel(XSSFWorkbook workbook, String caminhoLogo) throws Exception {
		FileInputStream fis = null;
		ByteArrayOutputStream bos = null;
		int pictureIndex = 0;
		try {
			fis = new FileInputStream(caminhoLogo);
			bos = new ByteArrayOutputStream();
			int c;
			while ((c = fis.read()) != -1)
				bos.write(c);
			if (caminhoLogo.endsWith("png") || caminhoLogo.endsWith("PNG")) {
				pictureIndex = workbook.addPicture(bos.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG);
			} else {
				pictureIndex = workbook.addPicture(bos.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG);
			}
		} finally {
			if (fis != null){
				fis.close();
			}
			if (bos != null){
				bos.flush();
				bos.close();
			}
		}
		return pictureIndex;
	}

	private void realizarGeracaoCabecalhoRelatorio(XSSFSheet worksheet, String[] colunas) throws Exception {

		XSSFCellStyle styleOfHeader = getCss(worksheet.getWorkbook(), HSSFColor.GREY_25_PERCENT.index, HSSFCellStyle.ALIGN_CENTER, "", true, true, true, true, HSSFColor.BLACK.index);
		XSSFRow cabecalho = null;
		XSSFCell cellCabecalho = null;
		cabecalho = worksheet.createRow(worksheet.getLastRowNum() + 1);
		Integer coluna = 0;
		cellCabecalho = cabecalho.createCell(coluna++);
		cellCabecalho.setCellValue("Nrº");
		cellCabecalho.setCellStyle(styleOfHeader);
		cellCabecalho.setCellType(HSSFCell.CELL_TYPE_STRING);

		for (String titulo : colunas) {
			cellCabecalho = cabecalho.createCell(coluna);
			cellCabecalho.setCellValue(titulo);
			cellCabecalho.setCellStyle(styleOfHeader);
			cellCabecalho.setCellType(HSSFCell.CELL_TYPE_STRING);
			coluna++;
		}
		worksheet.setAutobreaks(true);
	}

	private void realizarGeracaoCorpoRelatorio(XSSFSheet worksheet, List<List<CampoLogTriggerVO>> campoLogTriggerVOs) throws Exception {

		XSSFRow cellLinha = null;
		XSSFCell cellColuna = null;		
		for (List<CampoLogTriggerVO> campoLogTriggers : campoLogTriggerVOs) {
			cellLinha = worksheet.createRow(worksheet.getLastRowNum() + 1);
			cellColuna = cellLinha.createCell(0);
			cellColuna.setCellType(HSSFCell.CELL_TYPE_STRING);
			cellColuna.setCellValue(campoLogTriggers.get(0).getLinha());
			cellColuna.setCellStyle(getCss(worksheet.getWorkbook(), HSSFColor.WHITE.index, HSSFCellStyle.ALIGN_LEFT, "", true, true, true, true, HSSFColor.BLACK.index));
			
		for (CampoLogTriggerVO campoLogTriggerVO : campoLogTriggers) {			
			cellColuna = cellLinha.createCell(campoLogTriggerVO.getColuna());
			cellColuna.setCellType(HSSFCell.CELL_TYPE_STRING);
			cellColuna.setCellValue(campoLogTriggerVO.getValor());
			cellColuna.setCellStyle(getCss(worksheet.getWorkbook(), HSSFColor.WHITE.index, HSSFCellStyle.ALIGN_LEFT, "", true, true, true, true, HSSFColor.BLACK.index));
		}
		}

	}

	public void realizarGeracaoFiltrosRelatoriosRelatorio(RegistroLogTriggerVO registroLogTriggerVO, XSSFWorkbook workbook, XSSFSheet worksheet) throws Exception {

		XSSFRow cabecalho = null;
		XSSFCell cellCabecalho = null;

		cabecalho = worksheet.createRow(worksheet.getLastRowNum() + 1);
		worksheet.addMergedRegion(new CellRangeAddress(worksheet.getLastRowNum(), worksheet.getLastRowNum(), 0, 1));

		cellCabecalho = cabecalho.createCell(0);
		cellCabecalho.setCellType(HSSFCell.CELL_TYPE_STRING);
		cellCabecalho.setCellValue("Script");
		cellCabecalho.setCellStyle(getCss(workbook, HSSFColor.GREY_25_PERCENT.index, HSSFCellStyle.ALIGN_LEFT, "", true, true, true, true, HSSFColor.BLACK.index));

		cellCabecalho = cabecalho.createCell(1);
		cellCabecalho.setCellType(HSSFCell.CELL_TYPE_STRING);
		cellCabecalho.setCellStyle(getCss(workbook, HSSFColor.GREY_25_PERCENT.index, HSSFCellStyle.ALIGN_LEFT, "", true, true, true, true, HSSFColor.BLACK.index));

		XSSFCellStyle styleOfString = getCss(worksheet.getWorkbook(), HSSFColor.WHITE.index, HSSFCellStyle.ALIGN_LEFT, "", true, true, true, false, HSSFColor.BLACK.index);

		cabecalho = worksheet.createRow(worksheet.getLastRowNum() + 1);
		worksheet.addMergedRegion(new CellRangeAddress(worksheet.getLastRowNum(), worksheet.getLastRowNum(), 0, 1));

		cellCabecalho = cabecalho.createCell(0);
		cellCabecalho.setCellValue(registroLogTriggerVO.getQuery());
		cellCabecalho.setCellStyle(styleOfString);

	}

	public XSSFCellStyle getCss(XSSFWorkbook workbook, short corFundo, short alinhamentoHorizontal, String pattner, boolean bordaTopo, boolean bordaRodape, boolean bordaEsquerda, boolean bordaDireita, short corBorda) {
		XSSFCellStyle style = null;
		workbook.getNumCellStyles();
		for (int x = 0; x < workbook.getNumCellStyles(); x++) {
			style = workbook.getCellStyleAt((short) x);
			if (style.getFillForegroundColor() == corFundo && style.getAlignment() == alinhamentoHorizontal && ((!pattner.trim().isEmpty() && style.getDataFormat() == workbook.createDataFormat().getFormat(pattner)) || (pattner.trim().isEmpty() && style.getDataFormat() == 0)) && ((bordaRodape && style.getBorderBottom() == HSSFCellStyle.BORDER_THIN && style.getBottomBorderColor() == corBorda) || (!bordaRodape && style.getBorderBottom() == HSSFCellStyle.BORDER_NONE)) && ((bordaTopo && style.getBorderTop() == HSSFCellStyle.BORDER_THIN && style.getTopBorderColor() == corBorda) || (!bordaTopo && style.getBorderTop() == HSSFCellStyle.BORDER_NONE)) && ((bordaEsquerda && style.getBorderLeft() == HSSFCellStyle.BORDER_THIN && style.getLeftBorderColor() == corBorda) || (!bordaTopo && style.getBorderLeft() == HSSFCellStyle.BORDER_NONE))
					&& ((bordaDireita && style.getBorderRight() == HSSFCellStyle.BORDER_THIN && style.getRightBorderColor() == corBorda) || (!bordaTopo && style.getBorderRight() == HSSFCellStyle.BORDER_NONE))) {
				return style;
			}
		}
		style = workbook.createCellStyle();
		style.setFillForegroundColor(corFundo);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		if (!pattner.trim().isEmpty()) {
			style.setDataFormat(workbook.createDataFormat().getFormat(pattner));
		}
		if (bordaRodape) {
			style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style.setBottomBorderColor(corBorda);
		} else {
			style.setBorderBottom(HSSFCellStyle.BORDER_NONE);
		}
		if (bordaTopo) {
			style.setBorderTop(HSSFCellStyle.BORDER_THIN);
			style.setTopBorderColor(corBorda);
		} else {
			style.setBorderTop(HSSFCellStyle.BORDER_NONE);
		}
		if (bordaEsquerda) {
			style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style.setLeftBorderColor(corBorda);
		} else {
			style.setBorderLeft(HSSFCellStyle.BORDER_NONE);
		}
		if (bordaDireita) {
			style.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style.setRightBorderColor(corBorda);
		} else {
			style.setBorderRight(HSSFCellStyle.BORDER_NONE);
		}
		style.setAlignment(alinhamentoHorizontal);
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style.setWrapText(true);

		return style;
	}

	@Override
	public String realizarCriacaoArquivoCsv(RegistroLogTriggerVO registroLogTriggerVO, String urlLogoPadraoRelatorio, List<List<CampoLogTriggerVO>> campoLogTriggerVOs, String[] colunas, UsuarioVO usuarioVO) throws Exception {
		String nomeArquivo = "LOG_" + Uteis.getData(new Date(), "dd_MM_yy_hh_mm_ss") + "_" + usuarioVO.getCodigo() + ".csv";
		File file = null;
		PrintWriter printWriter = null;
		try {
			file = new File(UteisJSF.getCaminhoWeb() + File.separator + "relatorio" + File.separator + nomeArquivo);

			StringBuilder csv = new StringBuilder("");
			Integer coluna = 0;
			for (String titulo : colunas) {
				if(coluna != 0){
					csv.append(";");
				}
				csv.append(titulo);
				coluna++;
			}

			
			coluna = 0;
			for (List<CampoLogTriggerVO> campoLogTrigger : campoLogTriggerVOs) {
				if(csv.length() > 0) {
					csv.append("\n");
				}
					coluna = 0;
				
				for (CampoLogTriggerVO campoLogTriggerVO : campoLogTrigger) {
				if(coluna != 0){
					csv.append(";");
				}
				csv.append(campoLogTriggerVO.getValor());
				coluna++;
				}
			}
			printWriter = new PrintWriter(file, "ISO-8859-1");
			printWriter.append(csv.toString());
		} catch (Exception e) {
			throw e;
		} finally {
			if(printWriter != null){
				printWriter.flush();
				printWriter.close();
			}

		}

		return nomeArquivo;

	}


	public StringBuilder montarFiltroUsandoRowDataOrChangedField(final CampoLogTriggerVO filtro) {
		StringBuilder retorno = new StringBuilder();
		if (filtro.getSelecionado()) {
            //Condição adicionado especificamente para atender alguns índices criado no BD e realizar uma busca otimizada
			if (( filtro.getNome().equals("codigo") || filtro.getNome().equals("nossonumero") || filtro.getNome().equals("matricula") ||  filtro.getNome().equals("contareceber") ||  filtro.getNome().equals("matriculaaluno")  ) && (filtro.getTipoFiltro().equals(TipoFiltroEnum.IGUAL_NUMERO) || filtro.getTipoFiltro().equals(TipoFiltroEnum.IGUAL_TEXTO)) && Uteis.isAtributoPreenchido(filtro.getInput())) {
				this.validarFiltroInput();
				if (filtro.getNome().equals("codigo")) {
					retorno.append(" AND primary_key ->> '").append(filtro.getNome()).append("' = '").append(filtro.getInput()).append("' ");
				} else {
				 	retorno.append(" AND row_data ? '").append(filtro.getNome()).append("'");
					retorno.append(" AND ( row_data ->> '").append(filtro.getNome()).append("' = '").append(filtro.getInput()).append("' ").
					append(" OR changed_fields ->> '").append(filtro.getNome()).append("' = '").append(filtro.getInput()).append("') ");
				}
            } else if ((filtro.getTipoFiltro().equals(TipoFiltroEnum.IGUAL_NUMERO) || filtro.getTipoFiltro().equals(TipoFiltroEnum.IGUAL_TEXTO)) && Uteis.isAtributoPreenchido(filtro.getInput())) {
				validarFiltroInput();
				retorno.append(" AND ( row_data ->> '").append(filtro.getNome()).append("' = '").append(filtro.getInput()).append("' ").
				append(" OR changed_fields ->> '").append(filtro.getNome()).append("' = '").append(filtro.getInput()).append("') ");
			} else if (filtro.getTipoFiltro().equals(TipoFiltroEnum.IGUAL_DATA) && Uteis.isAtributoPreenchido(filtro.getDataInicialCampo())) {
				validarFiltroDataInicial();
				retorno.append(" and row_data ->> '").append(filtro.getNome()).append("' >= '").append(Uteis.getDataJDBCTimestamp(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(filtro.getDataInicialCampo()))).append("' ");
				retorno.append(" and row_data ->> '").append(filtro.getNome()).append("' <= '").append(Uteis.getDataJDBCTimestamp(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(filtro.getDataInicialCampo()))).append("' ");
			} else if (filtro.getTipoFiltro().equals(TipoFiltroEnum.DIFERENTE_NUMERO) || filtro.getTipoFiltro().equals(TipoFiltroEnum.DIFERENTE_TEXTO)) {
				validarFiltroInput();
				retorno.append(" AND ( row_data ->> '").append(filtro.getNome()).append("' != '").append(filtro.getInput()).append("' ").
				append(" OR changed_fields ->> '").append(filtro.getNome()).append("' != '").append(filtro.getInput()).append("') ");
			} else if (filtro.getTipoFiltro().equals(TipoFiltroEnum.DIFERENTE_DATA)) {
				validarFiltroDataInicial();
				retorno.append(" and (row_data ->> '").append(filtro.getNome()).append("' < '").append(Uteis.getDataJDBCTimestamp(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(filtro.getDataInicialCampo()))).append("' ");
				retorno.append(" or row_data ->> '").append(filtro.getNome()).append("' > '").append(Uteis.getDataJDBCTimestamp(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(filtro.getDataInicialCampo()))).append("') ");
			} else if (filtro.getTipoFiltro().equals(TipoFiltroEnum.CONTENDO_TEXTO)) {
				validarFiltroInput();
				retorno.append(" AND ( row_data ->> '").append(filtro.getNome()).append("' ILIKE '%").append(filtro.getInput()).append("%' ").
				append(" OR changed_fields ->> '").append(filtro.getNome()).append("' ILIKE '%").append(filtro.getInput()).append("%') ");

			} else if (filtro.getTipoFiltro().equals(TipoFiltroEnum.INICIANDO_COM_TEXTO)) {
				validarFiltroInput();
				retorno.append(" AND ( row_data ->> '").append(filtro.getNome()).append("' ILIKE '").append(filtro.getInput()).append("%' ").
				append(" OR changed_fields ->> '").append(filtro.getNome()).append("' ILIKE '").append(filtro.getInput()).append("%') ");
	 		} else if (filtro.getTipoFiltro().equals(TipoFiltroEnum.TERMINANDO_COM_TEXTO)) {
				validarFiltroInput();
				retorno.append(" AND ( row_data ->> '").append(filtro.getNome()).append("' ILIKE '%").append(filtro.getInput()).append("' ").
				append(" OR changed_fields ->> '").append(filtro.getNome()).append("' ILIKE '%").append(filtro.getInput()).append("') ");

			} else if (filtro.getTipoFiltro().equals(TipoFiltroEnum.MAIOR_IGUAL_NUMERO)) {
				validarFiltroInput();
				retorno.append(" AND ( row_data ->> '").append(filtro.getNome()).append("' >= '").append(filtro.getInput()).append("' ").
				append(" OR changed_fields ->> '").append(filtro.getNome()).append("' >= '").append(filtro.getInput()).append("') ");

			} else if (filtro.getTipoFiltro().equals(TipoFiltroEnum.MAIOR_NUMERO)) {
				validarFiltroInput();
				retorno.append(" AND ( row_data ->> '").append(filtro.getNome()).append("' > '").append(filtro.getInput()).append("' ").
				append(" OR changed_fields ->> '").append(filtro.getNome()).append("' > '").append(filtro.getInput()).append("') ");

			} else if (filtro.getTipoFiltro().equals(TipoFiltroEnum.MENOR_IGUAL_NUMERO)) {
				validarFiltroInput();
				retorno.append(" AND ( row_data ->> '").append(filtro.getNome()).append("' <= '").append(filtro.getInput()).append("' ").
				append(" OR changed_fields ->> '").append(filtro.getNome()).append("' <= '").append(filtro.getInput()).append("') ");

			} else if (filtro.getTipoFiltro().equals(TipoFiltroEnum.MENOR_NUMERO)) {
				validarFiltroInput();
				retorno.append(" AND ( row_data ->> '").append(filtro.getNome()).append("' < '").append(filtro.getInput()).append("' ").
				append(" OR changed_fields ->> '").append(filtro.getNome()).append("' < '").append(filtro.getInput()).append("') ");
 			} else if (filtro.getTipoFiltro().equals(TipoFiltroEnum.CONTENDO_NUMEROS)) {
				validarFiltroInput();
				retorno.append(" AND ( row_data ->> '").append(filtro.getNome()).append("' IN ( '").append(filtro.getInput()).append("') ").
				append(" OR changed_fields ->> '").append(filtro.getNome()).append("' IN ( '").append(filtro.getInput()).append("')) ");
			} else if (filtro.getTipoFiltro().equals(TipoFiltroEnum.NAO_CONTENDO_NUMEROS)) {
				validarFiltroInput();
				retorno.append(" AND ( row_data ->> '").append(filtro.getNome()).append("' NOT IN ( '").append(filtro.getInput()).append("') ").
				append(" OR changed_fields ->> '").append(filtro.getNome()).append("' NOT IN ( '").append(filtro.getInput()).append("')) ");
			} else if (filtro.getTipoFiltro().equals(TipoFiltroEnum.A_PARTIR_DATA)) {
				validarFiltroDataInicial();
				retorno.append(" AND ( row_data ->> '").append(filtro.getNome()).append("' >= '").append(Uteis.getDataJDBCTimestamp(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(filtro.getDataInicialCampo()))).append("' ").
				append(" OR changed_fields ->> '").append(filtro.getNome()).append("' >= '").append(Uteis.getDataJDBCTimestamp(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(filtro.getDataInicialCampo()))).append("') ");
			} else if (filtro.getTipoFiltro().equals(TipoFiltroEnum.ATE_DATA)) {
				validarFiltroDataFinal();
				retorno.append(" AND ( row_data ->> '").append(filtro.getNome()).append("' >= '").append(Uteis.getDataJDBCTimestamp(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(filtro.getDataInicialCampo()))).append("' ").
				append(" OR changed_fields ->> '").append(filtro.getNome()).append("' >= '").append(Uteis.getDataJDBCTimestamp(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(filtro.getDataInicialCampo()))).append("') ");
			} else if (filtro.getTipoFiltro().equals(TipoFiltroEnum.ENTRE_DATAS)) {
				validarFiltroDataInicial();
				validarFiltroDataFinal();
			 	retorno.append(" and  row_data ->> '").append(filtro.getNome()).append("' >= '").append(Uteis.getDataJDBCTimestamp(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(filtro.getDataInicialCampo()))).append("' ");
			 	retorno.append(" and  row_data ->> '").append(filtro.getNome()).append("' <= '").append(Uteis.getDataJDBCTimestamp(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(filtro.getDataFinalCampo()))).append("' ");
			} else if (filtro.getTipoFiltro().equals(TipoFiltroEnum.NAO_ENTRE_DATAS)) {
				validarFiltroDataInicial();
				validarFiltroDataFinal();
				retorno.append(" AND ( row_data ->> '").append(filtro.getNome()).append("' not between '").append(Uteis.getDataJDBCTimestamp(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(filtro.getDataInicialCampo()))).append("' and '").append(Uteis.getDataJDBCTimestamp(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(filtro.getDataFinalCampo()))).append("' ").
				append(" OR changed_fields ->> '").append(filtro.getNome()).append("' not between '").append(Uteis.getDataJDBCTimestamp(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(filtro.getDataInicialCampo()))).append("' and '").append(Uteis.getDataJDBCTimestamp(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(filtro.getDataFinalCampo()))).append("') ");
			} else if (filtro.getTipoFiltro().equals(TipoFiltroEnum.VERDADEIRO)) {
				retorno.append(" AND ( coalesce((row_data ->> '").append(filtro.getNome()).append("')::boolean,NULL)  IS TRUE ").
				append(" OR coalesce((changed_fields ->> '").append(filtro.getNome()).append("')::boolean,NULL)   IS TRUE ) ");
			} else if (filtro.getTipoFiltro().equals(TipoFiltroEnum.FALSO)) {
				retorno.append(" AND ( coalesce((row_data ->> '").append(filtro.getNome()).append("')::boolean,NULL)  IS FALSE ").
				append(" OR coalesce((changed_fields ->> '").append(filtro.getNome()).append("')::boolean,NULL)  IS FALSE  ) ");
			} else if (filtro.getTipoFiltro().equals(TipoFiltroEnum.NULO)) {
				retorno.append(" AND ( row_data ->> '").append(filtro.getNome()).append("' IS NULL ").
				append(" OR changed_fields ->> '").append(filtro.getNome()).append("' IS NULL ) ");
			} else if (filtro.getTipoFiltro().equals(TipoFiltroEnum.NAO_NULO)) {
				retorno.append(" AND ( row_data ->> '").append(filtro.getNome()).append("' IS NOT NULL ").
				append(" OR changed_fields ->> '").append(filtro.getNome()).append("' IS NOT NULL ) ");
			}
		}
		return retorno;

	}
	
	
	@Override
	public List<QueryAtivaLogTriggerVO> executarConsultarQueryAtivaLogTrigger() throws Exception {

		List<QueryAtivaLogTriggerVO> queryAtivaLogTriggerVOs = new ArrayList<QueryAtivaLogTriggerVO>(0);
		StringBuilder sql = new StringBuilder();

		sql.append("SELECT "); 
		sql.append("  pg_stat_activity.pid,substring(pg_stat_activity.query,0,130) as query,COALESCE(pg_stat_activity.usename,'') AS usuarioBancoDados,COALESCE(pg_stat_activity.application_name,'') AS nomeAplicacao, "); 
		sql.append("  pg_stat_activity.state AS situacao "); 
	    sql.append("FROM pg_stat_activity "); 
	    sql.append("WHERE pg_stat_activity.state = 'active' AND pg_stat_activity.query ILIKE '%audit.audit%' AND pg_stat_activity.query NOT ILIKE '%pg_stat_activity%' ; "); 
		 
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());

		while (rs.next()) {
			QueryAtivaLogTriggerVO queryAtiva = new QueryAtivaLogTriggerVO();
			queryAtiva.setPid(rs.getInt("pid"));
			queryAtiva.setQuery(rs.getString("query"));
			queryAtiva.setUsuarioBancoDados(rs.getString("usuarioBancoDados"));
			queryAtiva.setNomeAplicacao(rs.getString("nomeAplicacao"));
			queryAtiva.setSituacao(rs.getString("situacao"));
			queryAtivaLogTriggerVOs.add(queryAtiva);
		}

		return queryAtivaLogTriggerVOs;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarCancelamentoQueryAtivaLogTrigger(QueryAtivaLogTriggerVO obj) throws Exception {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT public.fn_finalizarQueryPostgreSQL(").append(obj.getPid()).append(")");
			getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

}
