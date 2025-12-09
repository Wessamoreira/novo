package negocio.facade.jdbc.contabil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.contabil.LancamentoContabilCentroNegocioVO;
import negocio.comuns.contabil.LancamentoContabilVO;
import negocio.comuns.financeiro.enumerador.TipoCentroNegocioEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.contabil.LancamentoContabilCentroNegocioInterfaceFacade;

/**
 * 
 * @author PedroOtimize
 *
 */

@Repository
@Scope("singleton")
@Lazy
public class LancamentoContabilCentroNegocio extends ControleAcesso implements LancamentoContabilCentroNegocioInterfaceFacade {

	/**
		 * 
		 */
	private static final long serialVersionUID = 6192890682296831948L;
	protected static String idEntidade;

	public LancamentoContabilCentroNegocio() throws Exception {
		super();
		setIdEntidade("LancamentoContabilCentroNegocio");
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void validarDados(LancamentoContabilCentroNegocioVO obj)  {
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getUnidadeEnsinoVO()), "O campo Unidade Ensino (Lancamento Contabil Centro Negocio) não foi informado.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getTipoCentroNegocioEnum()), "O campo Tipo Centro Negocio (Lancamento Contabil Centro Negocio) não foi informado.");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(List<LancamentoContabilCentroNegocioVO> lista, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		for (LancamentoContabilCentroNegocioVO layoutIntegracaoXMLTagVO : lista) {
			persistir(layoutIntegracaoXMLTagVO, verificarAcesso, usuarioVO);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(LancamentoContabilCentroNegocioVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarDados(obj);
		if (obj.getCodigo() == 0) {
			incluir(obj, verificarAcesso, usuarioVO);
		} else {
			alterar(obj, verificarAcesso, usuarioVO);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final LancamentoContabilCentroNegocioVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try {
			LancamentoContabilCentroNegocio.incluir(getIdEntidade(), verificarAcesso, usuario);
			final StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO lancamentocontabilcentronegocio ( lancamentoContabil, unidadeEnsino, curso, departamento, codigocontabil, ");
			sql.append("    nomecontabil, percentual, valor, nivelcontabil, ");
			sql.append("    turno, turma, centroResultadoAdministrativo, tipocentronegocioenum ) ");
			sql.append("    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )");
			sql.append(" returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql.toString());
					int i = 0;
					sqlInserir.setInt(++i, obj.getLancamentoContabilVO().getCodigo());
					sqlInserir.setInt(++i, obj.getUnidadeEnsinoVO().getCodigo());
					if (Uteis.isAtributoPreenchido(obj.getCursoVO())) {
						sqlInserir.setInt(++i, obj.getCursoVO().getCodigo());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getDepartamentoVO())) {
						sqlInserir.setInt(++i, obj.getDepartamentoVO().getCodigo());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					sqlInserir.setString(++i, obj.getCodigoContabil());
					sqlInserir.setString(++i, obj.getNomeContabil());
					sqlInserir.setDouble(++i, obj.getPercentual());
					sqlInserir.setDouble(++i, obj.getValor());
					sqlInserir.setString(++i, obj.getNivelContabil());
					Uteis.setValuePreparedStatement(obj.getTurnoVO(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getTurmaVO(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getCentroResultadoAdministrativo(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getTipoCentroNegocioEnum(), ++i, sqlInserir);
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(final ResultSet rs) throws SQLException {
					if (rs.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw e;
		}
	}

	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final LancamentoContabilCentroNegocioVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try {
			LancamentoContabilCentroNegocio.alterar(getIdEntidade(), verificarAcesso, usuario);
			final StringBuilder sql = new StringBuilder();
			sql.append("UPDATE lancamentocontabilcentronegocio ");
			sql.append("   SET lancamentoContabil=?, unidadeEnsino=?, curso=?, departamento=?, codigocontabil=?, ");
			sql.append("    nomecontabil=?, percentual=?, valor=?, nivelcontabil=?,  ");
			sql.append("    turno=?, turma=?, centroResultadoAdministrativo=?, tipocentronegocioenum=? ");
			sql.append("   WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql.toString());
					int i = 0;
					sqlAlterar.setInt(++i, obj.getLancamentoContabilVO().getCodigo());
					sqlAlterar.setInt(++i, obj.getUnidadeEnsinoVO().getCodigo());
					if (Uteis.isAtributoPreenchido(obj.getCursoVO())) {
						sqlAlterar.setInt(++i, obj.getCursoVO().getCodigo());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getDepartamentoVO())) {
						sqlAlterar.setInt(++i, obj.getDepartamentoVO().getCodigo());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					sqlAlterar.setString(++i, obj.getCodigoContabil());
					sqlAlterar.setString(++i, obj.getNomeContabil());
					sqlAlterar.setDouble(++i, obj.getPercentual());
					sqlAlterar.setDouble(++i, obj.getValor());
					sqlAlterar.setString(++i, obj.getNivelContabil());
					Uteis.setValuePreparedStatement(obj.getTurnoVO(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getTurmaVO(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getCentroResultadoAdministrativo(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getTipoCentroNegocioEnum(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);
					return sqlAlterar;
				}
			}) == 0) {
				incluir(obj, false, usuario);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	
	private StringBuilder getSQLPadraoConsultaBasica() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT lccn.codigo as \"lccn.codigo\", lccn.codigocontabil as \"lccn.codigocontabil\", lccn.nomecontabil as \"lccn.nomecontabil\", ");
		sql.append(" lccn.lancamentocontabil as \"lccn.lancamentocontabil\",  lccn.percentual as \"lccn.percentual\", ");
		sql.append(" lccn.valor as \"lccn.valor\",  lccn.nivelcontabil as \"lccn.nivelcontabil\", ");
		sql.append(" lccn.tipocentronegocioenum as \"lccn.tipocentronegocioenum\", ");
		
		sql.append(" unidadeensino.codigo as \"unidadeensino.codigo\", unidadeensino.nome as \"unidadeensino.nome\", ");
		
		/*sql.append(" categoriaDespesa.codigo as \"categoriaDespesa.codigo\", categoriaDespesa.descricao as \"categoriaDespesa.descricao\",   ");
		sql.append(" categoriaDespesa.informarTurma as \"categoriaDespesa.informarTurma\", categoriaDespesa.nivelCategoriaDespesa as \"categoriaDespesa.nivelCategoriaDespesa\",   ");
		sql.append(" categoriaDespesa.nivelAcademicoObrigatorio as \"categoriaDespesa.nivelAcademicoObrigatorio\", ");
		
		sql.append(" funcionariocargo.codigo as \"funcionariocargo.codigo\",  ");*/
		
		sql.append(" curso.codigo as \"curso.codigo\", curso.nome as \"curso.nome\", curso.codigocontabil as \"curso.codigocontabil\", curso.nomecontabil as \"curso.nomecontabil\", curso.nivelcontabil as \"curso.nivelcontabil\", ");
		
		sql.append(" departamento.codigo as \"departamento.codigo\", departamento.nome as \"departamento.nome\", departamento.codigocontabil as \"departamento.codigocontabil\", departamento.nomecontabil as \"departamento.nomecontabil\", departamento.nivelcontabil as \"departamento.nivelcontabil\", ");
		
		sql.append(" turno.codigo as \"turno.codigo\", turno.nome as \"turno.nome\",  ");
		
		sql.append(" turma.codigo as \"turma.codigo\", turma.identificadorturma as \"turma.identificadorturma\",  ");
		
		sql.append(" centroresultadoadministrativo.codigo as \"centroresultadoadministrativo.codigo\", centroresultadoadministrativo.descricao as \"centroresultadoadministrativo.descricao\", centroresultadoadministrativo.identificadorCentroResultado as \"centroresultadoadministrativo.identificadorCentroResultado\"  ");
		
		
		
		sql.append(" FROM lancamentocontabilcentronegocio lccn ");
		sql.append(" left join unidadeensino on unidadeensino.codigo = lccn.unidadeensino ");
		/*sql.append(" left join categoriadespesa on categoriadespesa.codigo = lccn.categoriadespesa ");
		sql.append(" left join funcionariocargo on funcionariocargo.codigo = lccn.funcionariocargo ");*/
		sql.append(" left join curso on curso.codigo = lccn.curso ");
		sql.append(" left join departamento on departamento.codigo = lccn.departamento ");
		sql.append(" left join turno on turno.codigo = lccn.turno");
		sql.append(" left join turma on turma.codigo = lccn.turma");
		sql.append(" left join centroresultado AS centroresultadoadministrativo ON lccn.centroresultadoadministrativo = centroresultadoadministrativo.codigo ");		
		return sql;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<LancamentoContabilCentroNegocioVO> consultaRapidaPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE lccn.codigo = ").append(valorConsulta).append(" ");
		sqlStr.append(" ORDER BY lccn.codigo asc ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado, nivelMontarDados, usuario);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void consultaRapidaPorLancamentoContabilVO(LancamentoContabilVO obj, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE lccn.lancamentocontabil = ").append(obj.getCodigo()).append(" ");
		sqlStr.append(" ORDER BY lccn.codigo asc ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		while (tabelaResultado.next()) {
			LancamentoContabilCentroNegocioVO lccn = new LancamentoContabilCentroNegocioVO();
			montarDadosBasico(lccn, tabelaResultado, nivelMontarDados, usuario);
			lccn.setLancamentoContabilVO(obj);
			if (lccn.isRateioAcademico()) {
				obj.getListaCentroNegocioAcademico().add(lccn);
			} else {
				obj.getListaCentroNegocioAdministrativo().add(lccn);
			}
		}
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<LancamentoContabilCentroNegocioVO> montarDadosConsultaBasica(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<LancamentoContabilCentroNegocioVO> vetResultado = new ArrayList<>(0);
		while (tabelaResultado.next()) {
			LancamentoContabilCentroNegocioVO obj = new LancamentoContabilCentroNegocioVO();
			montarDadosBasico(obj, tabelaResultado, nivelMontarDados, usuario);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	
	private void montarDadosBasico(LancamentoContabilCentroNegocioVO obj, SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario)  {
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo((dadosSQL.getInt("lccn.codigo")));
		obj.setCodigoContabil(dadosSQL.getString("lccn.codigocontabil"));
		obj.setNomeContabil(dadosSQL.getString("lccn.nomecontabil"));
		obj.setNivelContabil(dadosSQL.getString("lccn.nivelcontabil"));
		obj.setPercentual(dadosSQL.getDouble("lccn.percentual"));
		obj.setValor(dadosSQL.getDouble("lccn.valor"));
		obj.setTipoCentroNegocioEnum(TipoCentroNegocioEnum.valueOf(dadosSQL.getString("lccn.tipocentronegocioenum")));

		obj.getUnidadeEnsinoVO().setCodigo((dadosSQL.getInt("unidadeensino.codigo")));
		obj.getUnidadeEnsinoVO().setNome(dadosSQL.getString("unidadeensino.nome"));
		
		
		
		/*obj.getCategoriaDespesaVO().setCodigo((dadosSQL.getInt("categoriaDespesa.codigo")));
		obj.getCategoriaDespesaVO().setDescricao((dadosSQL.getString("categoriaDespesa.descricao")));
		obj.getCategoriaDespesaVO().setInformarTurma((dadosSQL.getString("categoriaDespesa.informarTurma")));
		obj.getCategoriaDespesaVO().setNivelCategoriaDespesa((dadosSQL.getString("categoriaDespesa.nivelCategoriaDespesa")));
		obj.getCategoriaDespesaVO().setIsNivelAcademicoObrigatorio((dadosSQL.getBoolean("categoriaDespesa.nivelAcademicoObrigatorio")));
		
		obj.getFuncionarioCargoVO().setCodigo((dadosSQL.getInt("funcionariocargo.codigo")));*/

		obj.getCursoVO().setCodigo((dadosSQL.getInt("curso.codigo")));
		obj.getCursoVO().setNome(dadosSQL.getString("curso.nome"));
		obj.getCursoVO().setCodigoContabil(dadosSQL.getString("curso.codigocontabil"));
		obj.getCursoVO().setNomeContabil(dadosSQL.getString("curso.nomecontabil"));
		obj.getCursoVO().setNivelContabil(dadosSQL.getString("curso.nivelcontabil"));

		obj.getDepartamentoVO().setCodigo((dadosSQL.getInt("departamento.codigo")));
		obj.getDepartamentoVO().setNome(dadosSQL.getString("departamento.nome"));
		obj.getDepartamentoVO().setCodigoContabil(dadosSQL.getString("departamento.codigocontabil"));
		obj.getDepartamentoVO().setNomeContabil(dadosSQL.getString("departamento.nomecontabil"));
		obj.getDepartamentoVO().setNivelContabil(dadosSQL.getString("departamento.nivelcontabil"));
		
		

		obj.getTurnoVO().setCodigo((dadosSQL.getInt("turno.codigo")));
		obj.getTurnoVO().setNome((dadosSQL.getString("turno.nome")));

		obj.getTurmaVO().setCodigo((dadosSQL.getInt("turma.codigo")));
		obj.getTurmaVO().setIdentificadorTurma((dadosSQL.getString("turma.identificadorturma")));		

		obj.getCentroResultadoAdministrativo().setCodigo((dadosSQL.getInt("centroResultadoAdministrativo.codigo")));
		obj.getCentroResultadoAdministrativo().setDescricao((dadosSQL.getString("centroResultadoAdministrativo.descricao")));
		obj.getCentroResultadoAdministrativo().setIdentificadorCentroResultado((dadosSQL.getString("centroResultadoAdministrativo.identificadorCentroResultado")));		

		
		if (!Uteis.isAtributoPreenchido(obj.getCodigoContabil())) {
			obj.setCodigoContabil(Uteis.isAtributoPreenchido(obj.getCursoVO()) ? obj.getCursoVO().getCodigoContabil() : obj.getDepartamentoVO().getCodigoContabil());
		}
		if (!Uteis.isAtributoPreenchido(obj.getNomeContabil())) {
			obj.setNomeContabil(Uteis.isAtributoPreenchido(obj.getCursoVO()) ? obj.getCursoVO().getNomeContabil() : obj.getDepartamentoVO().getNomeContabil());
		}
		if (!Uteis.isAtributoPreenchido(obj.getNivelContabil())) {
			obj.setNivelContabil(Uteis.isAtributoPreenchido(obj.getCursoVO()) ? obj.getCursoVO().getNivelContabil() : obj.getDepartamentoVO().getNivelContabil());
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSCONSULTA) {
			return;
		}

	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
	 */
	public static String getIdEntidade() {
		return LancamentoContabilCentroNegocio.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		LancamentoContabilCentroNegocio.idEntidade = idEntidade;
	}

}
