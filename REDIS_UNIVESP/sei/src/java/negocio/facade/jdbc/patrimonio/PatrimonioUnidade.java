package negocio.facade.jdbc.patrimonio;

/**
 * 
 * @author Leonardo Riciolle
 */
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

import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.patrimonio.PatrimonioUnidadeVO;
import negocio.comuns.patrimonio.PatrimonioVO;
import negocio.comuns.patrimonio.enumeradores.SituacaoPatrimonioUnidadeEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.patrimonio.PatrimonioUnidadeInterface;

@Repository
@Scope("singleton")
@Lazy
public class PatrimonioUnidade extends ControleAcesso implements PatrimonioUnidadeInterface {

	private static final long serialVersionUID = 2416524974886644210L;
	protected static String idEntidade;

	public PatrimonioUnidade() throws Exception {
		super();
		setIdEntidade("Patrimonio");
	}

	public PatrimonioUnidade novo() throws Exception {
		PatrimonioUnidade.incluir(getIdEntidade());
		PatrimonioUnidade obj = new PatrimonioUnidade();
		return obj;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(PatrimonioUnidadeVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			if (obj.getNovoObj()) {
				incluir(obj, verificarAcesso, usuarioVO);
			} else {
				alterar(obj, verificarAcesso, usuarioVO);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void validarDados(PatrimonioUnidadeVO obj) throws ConsistirException {
		if (obj.getCodigoBarra().trim().isEmpty()) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_PatrimonioUnidade_codigoBarra"));
		}
		if (obj.getSituacaoPatrimonioUnidade() == null) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_PatrimonioUnidade_situacao"));
		}
		if (!Uteis.isAtributoPreenchido(obj.getLocalArmazenamento())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_PatrimonioUnidade_localArmazenamento"));
		}
		realizarValidacaoUnicidadeCodigoBarraPatrimonioUnidade(obj);
		realizarValidacaoUnicidadeNumeroSeriePatrimonioUnidade(obj);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final PatrimonioUnidadeVO obj, boolean verificarAcesso, final UsuarioVO usuarioVO) throws Exception {
		try {
			validarDados(obj);
			final StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO patrimoniounidade(patrimonio, codigobarra, numeroserie, situacao, valorrecurso,  unidadelocado, permitereserva, localarmazenamento, estadobem)");
			sql.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql.toString());
					try {
						int x = 1;
						sqlInserir.setInt(x++, obj.getPatrimonioVO().getCodigo());
						sqlInserir.setString(x++, obj.getCodigoBarra());
						sqlInserir.setString(x++, obj.getNumeroDeSerie());
						sqlInserir.setString(x++, obj.getSituacaoPatrimonioUnidade().toString());
						sqlInserir.setBigDecimal(x++, obj.getValorRecurso());
						sqlInserir.setBoolean(x++, obj.getUnidadeLocado());
						sqlInserir.setBoolean(x++, obj.getPermiteReserva());
						sqlInserir.setInt(x++, obj.getLocalArmazenamento().getCodigo());
						sqlInserir.setString(x++, obj.getEstadoBem());
					} catch (final Exception x) {
						return null;
					}
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(final ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final PatrimonioUnidadeVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			validarDados(obj);
			final StringBuilder sql = new StringBuilder();
			sql.append(" UPDATE patrimoniounidade SET  patrimonio=?, codigobarra=?, numeroserie=?, ");
			sql.append(" valorrecurso=?, unidadelocado=?, permitereserva=?, estadobem=?  ");
			sql.append(" WHERE (codigo = ? )" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql.toString());
					int x = 1;
					sqlAlterar.setInt(x++, obj.getPatrimonioVO().getCodigo());
					sqlAlterar.setString(x++, obj.getCodigoBarra());
					sqlAlterar.setString(x++, obj.getNumeroDeSerie());
					sqlAlterar.setBigDecimal(x++, obj.getValorRecurso());
					sqlAlterar.setBoolean(x++, obj.getUnidadeLocado());
					sqlAlterar.setBoolean(x++, obj.getPermiteReserva());
					sqlAlterar.setString(x++, obj.getEstadoBem());
					sqlAlterar.setInt(x++, obj.getCodigo());
					return sqlAlterar;
				}
			}) == 0) {
				incluir(obj, verificarAcesso, usuarioVO);
			}
			;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSituacaoELocalArmazenamentoPatrimonioUnidade(final PatrimonioUnidadeVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			validarDados(obj);
			final StringBuilder sql = new StringBuilder();
			sql.append(" UPDATE patrimoniounidade SET  situacao = ?, localarmazenamento = ? ");
			sql.append(" WHERE (codigo = ? )" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql.toString());
					int x = 1;
					sqlAlterar.setString(x++, obj.getSituacaoPatrimonioUnidade().name());
					sqlAlterar.setInt(x++, obj.getLocalArmazenamento().getCodigo());
					sqlAlterar.setInt(x++, obj.getCodigo());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarLocalArmazenamentoPatrimonioUnidade(final PatrimonioUnidadeVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			validarDados(obj);
			final StringBuilder sql = new StringBuilder();
			sql.append(" UPDATE patrimoniounidade SET  localarmazenamento = ? ");
			sql.append(" WHERE (codigo = ? )" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql.toString());
					int x = 1;
					sqlAlterar.setInt(x++, obj.getLocalArmazenamento().getCodigo());
					sqlAlterar.setInt(x++, obj.getCodigo());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSituacaoPatrimonioUnidade(final PatrimonioUnidadeVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			validarDados(obj);
			final StringBuilder sql = new StringBuilder();
			sql.append(" UPDATE patrimoniounidade SET  situacao = ? ");
			sql.append(" WHERE (codigo = ? )" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql.toString());
					int x = 1;
					sqlAlterar.setString(x++, obj.getSituacaoPatrimonioUnidade().name());
					sqlAlterar.setInt(x++, obj.getCodigo());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(PatrimonioUnidadeVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("DELETE FROM patrimoniounidade WHERE (codigo = ?)" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			getConexao().getJdbcTemplate().update(sql.toString(), obj.getCodigo());
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirPorPatrimonio(Integer patrimonio, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			PatrimonioUnidade.excluir(getIdEntidade(), verificarAcesso, usuarioVO);
			StringBuilder sql = new StringBuilder();
			sql.append("DELETE FROM patrimoniounidade WHERE (patrimonio = ?)" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			getConexao().getJdbcTemplate().update(sql.toString(), patrimonio);
		} catch (Exception e) {
			throw e;
		}
	}

	public List<PatrimonioUnidadeVO> montarDadosConsulta(SqlRowSet tabelaResultado, NivelMontarDados nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		List<PatrimonioUnidadeVO> patrimonioUnidadeVOs = new ArrayList<PatrimonioUnidadeVO>(0);
		while (tabelaResultado.next()) {
			patrimonioUnidadeVOs.add(montarDados(tabelaResultado, nivelMontarDados, usuarioVO));
		}
		return patrimonioUnidadeVOs;
	}

	public PatrimonioUnidadeVO montarDados(SqlRowSet tabelaResultado, NivelMontarDados nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		PatrimonioUnidadeVO obj = new PatrimonioUnidadeVO();
		obj.setNovoObj(false);
		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.setPatrimonioVO(getFacadeFactory().getPatrimonioFacade().consultarPorChavePrimaria(tabelaResultado.getInt("patrimonio"), NivelMontarDados.BASICO, false, usuarioVO));
		obj.getPatrimonioVO().setDescricao(tabelaResultado.getString("patrimonio.descricao"));
		obj.setCodigoBarra(tabelaResultado.getString("codigobarra"));
		obj.setNumeroDeSerie(tabelaResultado.getString("numeroserie"));

		if (Uteis.isAtributoPreenchido(tabelaResultado.getString("situacao"))) {
			obj.setSituacaoPatrimonioUnidade(SituacaoPatrimonioUnidadeEnum.valueOf(tabelaResultado.getString("situacao")));
		}
		obj.setValorRecurso(tabelaResultado.getBigDecimal("valorrecurso"));
		obj.setUnidadeLocado(tabelaResultado.getBoolean("unidadelocado"));
		obj.setPermiteReserva(tabelaResultado.getBoolean("permitereserva"));
		obj.getLocalArmazenamento().setCodigo(tabelaResultado.getInt("localarmazenamento"));
		obj.getLocalArmazenamento().setLocalArmazenamento(tabelaResultado.getString("localarmazenamento.localarmazenamento"));
		obj.getLocalArmazenamento().getUnidadeEnsinoVO().setCodigo(tabelaResultado.getInt("unidadeensino.codigo"));
		obj.getLocalArmazenamento().getUnidadeEnsinoVO().setNome(tabelaResultado.getString("unidadeensino.nome"));
		obj.setEstadoBem(tabelaResultado.getString("estadobem"));
		return obj;
	}

	public StringBuilder getSqlConsultaCompleta() {
		StringBuilder sql = new StringBuilder("select patrimoniounidade.*, localarmazenamento.localarmazenamento as \"localarmazenamento.localarmazenamento\", ");
		sql.append(" unidadeensino.codigo as \"unidadeensino.codigo\", unidadeensino.nome as \"unidadeensino.nome\", patrimonio.descricao  as \"patrimonio.descricao\" ");
		sql.append(" from patrimoniounidade ");
		sql.append(" left join localarmazenamento on localarmazenamento.codigo = patrimoniounidade.localarmazenamento");
		sql.append(" left join unidadeensino on localarmazenamento.unidadeensino = unidadeensino.codigo ");
		sql.append(" left join patrimonio on patrimoniounidade.patrimonio = patrimonio.codigo ");
		return sql;
	}

	@Override
	public List<PatrimonioUnidadeVO> consultar(String campoConsulta, String valorConsulta, boolean verificarAcesso, UsuarioVO usuarioVO, UnidadeEnsinoVO unidadeEnsinoLogado, Integer limite, Integer offset) throws Exception {
		PatrimonioUnidade.consultar(getIdEntidade(), verificarAcesso, usuarioVO);
		StringBuilder sqlStr = getSqlConsultaCompleta();
		if (campoConsulta.equals("CODIGO_BARRA")) {
			if(!valorConsulta.matches("\\d*")){
				throw new Exception(UteisJSF.internacionalizar("msg_PatrimonioUnidade_informeApenasValoresNumericos"));
			}
			sqlStr.append(" WHERE codigoBarra::NUMERIC(20,0) = '").append(valorConsulta).append("'::NUMERIC(20,0) ");
		}else if (campoConsulta.equals("DESCRICAO")) {
			sqlStr.append(" WHERE sem_acentos(patrimonio.descricao) ilike sem_acentos('").append(valorConsulta).append("%')");
		}else if (campoConsulta.equals("LOCAL_ARMAZENAMENTO")) {
			sqlStr.append(" WHERE sem_acentos(localArmazenamento.localArmazenamento) ilike sem_acentos('").append(valorConsulta).append("%')");
		}		
		if (Uteis.isAtributoPreenchido(unidadeEnsinoLogado)) {
			sqlStr.append(" and unidadeensino.codigo =   ").append(unidadeEnsinoLogado.getCodigo());			
		} 	
		if (campoConsulta.equals("CODIGO_BARRA")) {
			sqlStr.append(" order by codigoBarra ");
		}else if (campoConsulta.equals("DESCRICAO")) {
			sqlStr.append(" order by patrimonio.descricao, codigoBarra ");
		}else if (campoConsulta.equals("LOCAL_ARMAZENAMENTO")) {
			sqlStr.append(" order by localArmazenamento.localArmazenamento, codigoBarra ");
		}	
		if(limite != null && limite > 0){
			sqlStr.append(" limit ").append(limite).append(" offset ").append(offset);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, NivelMontarDados.TODOS, usuarioVO);
	}
	
	@Override
	public Integer consultarTotalRegistro(String campoConsulta, String valorConsulta, UnidadeEnsinoVO unidadeEnsinoLogado) throws Exception {		
		StringBuilder sqlStr = new StringBuilder("select count(patrimoniounidade.codigo) as qtde ");
		sqlStr.append(" from patrimoniounidade ");
		sqlStr.append(" left join patrimonio on patrimonio.codigo = patrimoniounidade.patrimonio");
		sqlStr.append(" left join localarmazenamento on localarmazenamento.codigo = patrimoniounidade.localarmazenamento");
		sqlStr.append(" left join unidadeensino on localarmazenamento.unidadeensino = unidadeensino.codigo ");
		if (campoConsulta.equals("CODIGO_BARRA")) {
			if(!valorConsulta.matches("\\d*")){
				throw new Exception(UteisJSF.internacionalizar("msg_PatrimonioUnidade_informeApenasValoresNumericos"));
			}
			sqlStr.append(" WHERE codigoBarra::NUMERIC(20,0) = '").append(valorConsulta).append("'::NUMERIC(20,0) ");
		}else if (campoConsulta.equals("DESCRICAO")) {
			sqlStr.append(" WHERE sem_acentos(patrimonio.descricao) ilike sem_acentos('").append(valorConsulta).append("%')");
		}else if (campoConsulta.equals("LOCAL_ARMAZENAMENTO")) {
			sqlStr.append(" WHERE sem_acentos(localArmazenamento.localArmazenamento) ilike sem_acentos('").append(valorConsulta).append("%')");
		}		
		if (Uteis.isAtributoPreenchido(unidadeEnsinoLogado)) {
			sqlStr.append(" and unidadeensino.codigo =   ").append(unidadeEnsinoLogado.getCodigo());			
		} 		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if(tabelaResultado.next()){
			return tabelaResultado.getInt("qtde");
		}
		return 0;
	}

	@Override
	public List<PatrimonioUnidadeVO> consultarPorChavePrimariaPatrimonio(Integer codigo, NivelMontarDados nivelMontarDados, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		PatrimonioUnidade.consultar(getIdEntidade(), verificarAcesso, usuarioVO);
		StringBuilder sqlStr = getSqlConsultaCompleta();
		sqlStr.append("WHERE patrimonio = ").append(codigo).append(" order by patrimoniounidade.numeroserie");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuarioVO);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistirPatrimonioUnidadeVOs(PatrimonioVO patrimonioVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			this.excluirPatrimonioUnidadeVOs(patrimonioVO, verificarAcesso, usuarioVO);
			for (PatrimonioUnidadeVO obj : patrimonioVO.getPatrimonioUnidadeVOs()) {
				obj.setPatrimonioVO(patrimonioVO);
				if (obj.getNovoObj()) {
					incluir(obj, verificarAcesso, usuarioVO);
				} else {
					alterar(obj, verificarAcesso, usuarioVO);
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirPatrimonioUnidadeVOs(PatrimonioVO patrimonioVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("DELETE FROM patrimoniounidade WHERE (patrimonio = " + patrimonioVO.getCodigo());
			sql.append(") and codigo not in(");
			for (PatrimonioUnidadeVO obj : patrimonioVO.getPatrimonioUnidadeVOs()) {
				sql.append(obj.getCodigo()).append(", ");
			}
			sql.append("0) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			getConexao().getJdbcTemplate().update(sql.toString());
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public String consultarSugestaoCodigoBarra(PatrimonioVO patrimonioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT codigoBarra FROM patrimoniounidade order by codigoBarra::NUMERIC(20,0) desc limit 1 ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		String numeroCodigoBarra = "00000000";
		if (rs.next()) {
			numeroCodigoBarra = rs.getString("codigoBarra");
		}
		for (PatrimonioUnidadeVO patrimonioUnidadeVO : patrimonioVO.getPatrimonioUnidadeVOs()) {
			if (Long.valueOf(patrimonioUnidadeVO.getCodigoBarra()) > Long.valueOf(numeroCodigoBarra)) {
				numeroCodigoBarra = patrimonioUnidadeVO.getCodigoBarra();
			}
		}
		return Uteis.getPreencherComZeroEsquerda(String.valueOf((Long.valueOf(numeroCodigoBarra) + 1)), numeroCodigoBarra.length());
	}

	@Override
	public void realizarValidacaoUnicidadeNumeroSeriePatrimonioUnidade(PatrimonioUnidadeVO patrimonioUnidadeVO) throws ConsistirException {
		if (!patrimonioUnidadeVO.getNumeroDeSerie().trim().isEmpty()) {
			StringBuilder sqlStr = new StringBuilder();
			sqlStr.append("SELECT codigoBarra FROM patrimoniounidade WHERE codigo != ? and numeroserie = ? ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), patrimonioUnidadeVO.getCodigo(), patrimonioUnidadeVO.getNumeroDeSerie());
			try {
				if (tabelaResultado.next()) {
					throw new ConsistirException(UteisJSF.internacionalizar("msg_PatrimonioUnidade_numeroSerieDuplicado").replace("{0}", tabelaResultado.getString("codigoBarra")).replace("{1}", patrimonioUnidadeVO.getNumeroDeSerie()));
				}
			} finally {
				sqlStr = null;
				tabelaResultado = null;
			}
		}
	}

	@Override
	public PatrimonioUnidadeVO consultarPatrimonioUnidadePorCodigoBarra(String codigoBarra, UnidadeEnsinoVO unidadeEnsinoLogado, NivelMontarDados nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		SqlRowSet tabelaResultado = null;
		StringBuilder sqlStr = getSqlConsultaCompleta();
		if(!Uteis.isAtributoPreenchido(codigoBarra)){
			return new PatrimonioUnidadeVO();
		}
		if(!codigoBarra.matches("\\d*")){
			throw new Exception(UteisJSF.internacionalizar("msg_PatrimonioUnidade_informeApenasValoresNumericos"));			
		}
		sqlStr.append(" WHERE codigoBarra::NUMERIC(20,0) = ?  ");
		if (Uteis.isAtributoPreenchido(unidadeEnsinoLogado)) {
			sqlStr.append(" and unidadeensino.codigo = ?  ");
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), Long.valueOf(codigoBarra), unidadeEnsinoLogado.getCodigo());
		} else {
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), Long.valueOf(codigoBarra));
		}
		if (tabelaResultado.next()) {
			return montarDados(tabelaResultado, nivelMontarDados, usuarioVO);
		}
		throw new Exception(UteisJSF.internacionalizar("msg_PatrimonioUnidade_dadoNaoEncontrado"));
	}

	@Override
	public Boolean consultarExistenciaPatrimonioUnidadePorCodigoBarra(String codigoBarra) throws Exception {
		StringBuilder sqlStr = new StringBuilder(" select codigo from patrimoniounidade WHERE codigoBarra::NUMERIC(20,0) = ? ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), Long.valueOf(codigoBarra));
		return tabelaResultado.next();
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarValidacaoUnicidadeCodigoBarraPatrimonioUnidade(PatrimonioUnidadeVO patrimonioUnidadeVO) throws ConsistirException {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT codigo FROM patrimoniounidade WHERE codigobarra::NUMERIC(20,0) = ? and codigo != ? ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), Long.valueOf(patrimonioUnidadeVO.getCodigoBarra().trim()), patrimonioUnidadeVO.getCodigo());
		try {
			while (tabelaResultado.next()) {
				if(tabelaResultado.getInt("codigo") != patrimonioUnidadeVO.getCodigo()){
					throw new ConsistirException(UteisJSF.internacionalizar("msg_PatrimonioUnidade_unidadePatrimonio").replace("{0}", patrimonioUnidadeVO.getCodigoBarra()));
				}
			}

		} finally {
			sqlStr = null;
			tabelaResultado = null;
		}
	}

	public static String getIdEntidade() {
		if (idEntidade == null) {
			idEntidade = "Patrimonio";
		}
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		PatrimonioUnidade.idEntidade = idEntidade;
	}
	
	@Override
	public List<PatrimonioUnidadeVO> consultarPorTipoPatrimonioParaListagemDeOcorrenciasPorUnidade(Integer tipopatrimonio, Date dataReserva, UnidadeEnsinoVO unidadeEnsino){
		
		
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select "); 
		sqlStr.append("patrimoniounidade.codigoBarra, patrimoniounidade.codigo, patrimoniounidade.patrimonio, patrimonio.descricao   "); 
		/*sqlStr.append("ocorrenciapatrimonio.datainicioreserva, "); 
		sqlStr.append("ocorrenciapatrimonio.datafinalizacao "); */
		sqlStr.append(" from patrimoniounidade "); 
		sqlStr.append(" inner join patrimonio on patrimoniounidade.patrimonio = patrimonio.codigo "); 
		sqlStr.append(" inner join tipopatrimonio on tipopatrimonio.codigo = patrimonio.tipopatrimonio "); 
		sqlStr.append(" left join localArmazenamento as localArmazenamento on localArmazenamento.codigo = patrimoniounidade.localArmazenamento ");
		sqlStr.append(" where tipopatrimonio.codigo = ").append(tipopatrimonio);
		sqlStr.append(" and patrimoniounidade.unidadelocado = true ");
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sqlStr.append(" and localArmazenamento.unidadeEnsino = ").append(unidadeEnsino.getCodigo());
		}
		sqlStr.append(" order by patrimoniounidade.codigoBarra "); 
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<PatrimonioUnidadeVO> objs = new ArrayList<PatrimonioUnidadeVO>();
		while (tabelaResultado.next()) {
			PatrimonioUnidadeVO obj = new PatrimonioUnidadeVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setCodigoBarra(tabelaResultado.getString("codigoBarra"));
			obj.getPatrimonioVO().setCodigo(tabelaResultado.getInt("patrimonio"));
			obj.getPatrimonioVO().setDescricao(tabelaResultado.getString("descricao"));
			obj.getListaOcorrencias().addAll(getFacadeFactory().getOcorrenciaPatrimonioFacade().consultarOcorrenciaPatrimonioPorUnidadePatrimonio(obj.getCodigo(), dataReserva));
			objs.add(obj);
			
		}
		return objs;
		
	}
	@Override
	public List<PatrimonioUnidadeVO> consultarPorTipoPatrimonioDataUnidadeEnsinoSolicitanteParaListagemDeOcorrenciasPorUnidade(Integer tipopatrimonio, UnidadeEnsinoVO unidadeEnsino, Date dataInicial, Date dataFinal, FuncionarioVO solicitante){
		
		
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select "); 
		sqlStr.append("patrimoniounidade.codigoBarra, patrimoniounidade.codigo, patrimoniounidade.patrimonio, patrimonio.descricao "); 
		sqlStr.append(" from patrimoniounidade "); 
		sqlStr.append(" inner join patrimonio on patrimoniounidade.patrimonio = patrimonio.codigo "); 
		sqlStr.append(" inner join tipopatrimonio on tipopatrimonio.codigo = patrimonio.tipopatrimonio "); 
		sqlStr.append(" left join localArmazenamento as localArmazenamento on localArmazenamento.codigo = patrimoniounidade.localArmazenamento ");
		sqlStr.append(" where 1=1 ");
		if(Uteis.isAtributoPreenchido(tipopatrimonio)){
			sqlStr.append(" and tipopatrimonio.codigo = ").append(tipopatrimonio);
		}
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sqlStr.append(" and localArmazenamento.unidadeEnsino = ").append(unidadeEnsino.getCodigo());
		}
		sqlStr.append(" order by patrimoniounidade.codigoBarra "); 
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<PatrimonioUnidadeVO> objs = new ArrayList<PatrimonioUnidadeVO>();
		while (tabelaResultado.next()) {
			PatrimonioUnidadeVO obj = new PatrimonioUnidadeVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setCodigoBarra(tabelaResultado.getString("codigoBarra"));
			obj.getPatrimonioVO().setCodigo(tabelaResultado.getInt("patrimonio"));
			obj.getPatrimonioVO().setDescricao(tabelaResultado.getString("descricao"));
			obj.getListaOcorrencias().addAll(getFacadeFactory().getOcorrenciaPatrimonioFacade().consultarOcorrenciaUnidadePatrimioParaGestao(obj.getCodigo(), dataInicial,dataFinal,solicitante));
			objs.add(obj);
			
		}
		return objs;
		
	}
}
