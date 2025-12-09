package negocio.facade.jdbc.recursoshumanos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.AtivoInativoEnum;
import negocio.comuns.recursoshumanos.IncidenciaFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.enumeradores.LocalIncidenciaEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.recursoshumanos.IncidenciaFolhaPagamentoInterfaceFacade;

/*Classe de persistencia que encapsula todas as operacoes de manipulacao dos
* dados da classe <code>IncidenciaFolhaPagamentoVO</code>. Responsavel por implementar
* operacoes como incluir, alterar, excluir e consultar pertinentes a classe
* <code>IncidenciaFolhaPagamentoVO</code>. Encapsula toda a interacao com o banco de
* dados.
* 
*/
@SuppressWarnings({ "unchecked", "rawtypes" })
@Service
@Scope
@Lazy
public class IncidenciaFolhaPagamento extends ControleAcesso implements IncidenciaFolhaPagamentoInterfaceFacade {

	private static final long serialVersionUID = -1084004772804472456L;

	protected static String idEntidade;

	public IncidenciaFolhaPagamento() throws Exception {
		super();
		setIdEntidade("IncidenciaFolhaPagamento");
	}

	@Override
	public void persistir(IncidenciaFolhaPagamentoVO obj, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarDados(obj);

		if (obj.getCodigo() == null || obj.getCodigo() == 0) {
			incluir(obj, validarAcesso, usuarioVO);
		} else {
			alterar(obj, validarAcesso, usuarioVO);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void incluir(IncidenciaFolhaPagamentoVO obj, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			IncidenciaFolhaPagamento.incluir(getIdEntidade(), validarAcesso, usuarioVO);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection arg0) throws SQLException {

					StringBuilder sql = new StringBuilder(" INSERT INTO incidenciafolhapagamento ( descricao, incidenciaImposto, imposto, localIncidencia, formula, ")
					        .append(" situacao) ")
					        .append(" VALUES ( ?, ?, ?, ?, ?, ")
					        .append(" ?) returning codigo ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));

					final PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
					int i = 0;
					Uteis.setValuePreparedStatement(obj.getDescricao(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getIncidenciaImposto(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getImposto(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getLocalIncidencia().name(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getFormula(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getSituacao().name(), ++i, sqlInserir);
					return sqlInserir;
				}
			}, new ResultSetExtractor() {

				public Object extractData(final ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void alterar(IncidenciaFolhaPagamentoVO obj, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			IncidenciaFolhaPagamento.alterar(getIdEntidade(), validarAcesso, usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {

					StringBuilder sql = new StringBuilder("UPDATE incidenciafolhapagamento set ")
					        .append(" descricao=?, incidenciaImposto=?, imposto=?, localIncidencia=?, formula=?, ")
					        .append(" situacao=? ")
					        .append(" WHERE codigo = ? ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));

					PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
					int i = 0;
					Uteis.setValuePreparedStatement(obj.getDescricao(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getIncidenciaImposto(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getImposto(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getLocalIncidencia().name(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getFormula(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getSituacao().name(), ++i, sqlAlterar);

					Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);

					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(IncidenciaFolhaPagamentoVO obj, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			IncidenciaFolhaPagamento.excluir(getIdEntidade(), validarAcesso, usuarioVO);
			getFacadeFactory().getEventoIncidenciaFolhaPagamentoInterfaceFacade().validarDadosPorIncidenciaFolhaPagamento(obj);
			StringBuilder sql = new StringBuilder("DELETE FROM incidenciafolhapagamento WHERE ((codigo = ?)) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			getConexao().getJdbcTemplate().update(sql.toString(), new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	public void validarRestricoesInicidenciaFolhaPagamento(IncidenciaFolhaPagamentoVO incidenciaFolhaPagamentoVO) throws ConsistirException {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from incidenciafolhapagamento ")
		.append(" where upper(trim(sem_acentos(descricao))) ilike upper(trim(sem_acentos(?))) and situacao = 'ATIVO' AND localincidencia = ? and codigo <> ?");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), incidenciaFolhaPagamentoVO.getDescricao(), incidenciaFolhaPagamentoVO.getLocalIncidencia().toString(), incidenciaFolhaPagamentoVO.getCodigo());
		if (rs.next()) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_IncidenciaFolhaPagamento_duplicado"));
		}
	}

	@Override
	public List<IncidenciaFolhaPagamentoVO> consultarPorFiltro(String campoConsulta, String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(getSelectSqlBasico());

		switch (campoConsulta) {
		case "descricao":
			sql.append(" WHERE upper( incidenciafolhapagamento.descricao ) like(?) ");
			valorConsulta = "%"+valorConsulta.toUpperCase()+"%"; 
			break;
		case "codigo":
			sql.append(" WHERE incidenciafolhapagamento.codigo = ?");
			break;
		default:
			break;
		}

		sql.append(" ORDER BY incidenciafolhapagamento.codigo DESC ");

		SqlRowSet tabelaResultado = null;
		if (!campoConsulta.equals("codigo")) {
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), valorConsulta);			
		} else {
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), Integer.valueOf(valorConsulta));
		}
		List<IncidenciaFolhaPagamentoVO> incidenciasDaFolhaDePagamento = new ArrayList<>();
		while (tabelaResultado.next()) {
			incidenciasDaFolhaDePagamento.add(montarDados(tabelaResultado));
		}
		return incidenciasDaFolhaDePagamento;
	}

	/**
	 * Monta o objeto <code>IncidenciaFolhaPagamentoVO<code> consultado do banco de
	 * dados.
	 * 
	 * @param tabelaResultado
	 * @return
	 */
	public IncidenciaFolhaPagamentoVO montarDados(SqlRowSet tabelaResultado) {

		IncidenciaFolhaPagamentoVO obj = new IncidenciaFolhaPagamentoVO();
		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.setDescricao(tabelaResultado.getString("descricao"));
		obj.setIncidenciaImposto(tabelaResultado.getBoolean("incidenciaImposto"));

		if (tabelaResultado.getString("localIncidencia") != null) {
			obj.setLocalIncidencia(LocalIncidenciaEnum.valueOf(tabelaResultado.getString("localIncidencia")));
		} else {
			obj.setLocalIncidencia(null);
		}

		obj.getImposto().setCodigo(tabelaResultado.getInt("imposto"));
		obj.getFormula().setCodigo(tabelaResultado.getInt("formula"));

		if (tabelaResultado.getString("situacao") != null) {
			obj.setSituacao(AtivoInativoEnum.valueOf(tabelaResultado.getString("situacao")));
		} else {
			obj.setSituacao(AtivoInativoEnum.INATIVO);
		}

		return obj;
	}

	/**
	 * Sql basico para consultad da <code>IncidenciaFolhaPagamentoVO<code>
	 * 
	 * @return
	 */
	public String getSelectSqlBasico() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM incidenciafolhapagamento ");
		sql.append(" left JOIN imposto ON incidenciafolhapagamento.imposto = imposto.codigo");

		return sql.toString();
	}

	/**
	 * Valida os campos obrigatorios do <code>IncidenciaFolhaPagamentoVO<code>
	 * 
	 * @param obj <code>FaixaValor</code>
	 * 
	 * @throws ConsistirException
	 * @throws ParseException
	 */
	private void validarDados(IncidenciaFolhaPagamentoVO obj) throws ConsistirException, ParseException {

		if (obj.getDescricao().trim().isEmpty()) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_IncidenciaFolhaPagamento_descricao"));
		}

		if (obj.getIncidenciaImposto()) {
			if (obj.getImposto().getCodigo() <= 0) {
				throw new ConsistirException(UteisJSF.internacionalizar("msg_IncidenciaFolhaPagamento_imposto"));
			}
		}

		this.validarRestricoesInicidenciaFolhaPagamento(obj);

	}

	public static String getIdEntidade() {
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		IncidenciaFolhaPagamento.idEntidade = idEntidade;
	}
	
	@Override
	public List<IncidenciaFolhaPagamentoVO> consultarPorNome(String campoConsulta, String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		
		StringBuilder sql = new StringBuilder();
		sql.append(getSelectSqlBasico())
		.append(" WHERE upper( incidenciafolhapagamento.descricao ) like('%").append(valorConsulta.toUpperCase()).append("%') ");
		sql.append(" ORDER BY incidenciafolhapagamento.codigo DESC ");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<IncidenciaFolhaPagamentoVO> incidenciasDaFolhaDePagamento = new ArrayList<>();
		while (tabelaResultado.next()) {
			incidenciasDaFolhaDePagamento.add(montarDados(tabelaResultado));
		}
		return incidenciasDaFolhaDePagamento;
	}
	
	@Override
	public IncidenciaFolhaPagamentoVO consultarPorChavePrimaria(Integer codigo, UsuarioVO usuario) throws Exception {

		StringBuilder sql = new StringBuilder("SELECT * FROM incidenciafolhapagamento WHERE codigo = ?");
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigo);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return (montarDados(tabelaResultado));
	}
}