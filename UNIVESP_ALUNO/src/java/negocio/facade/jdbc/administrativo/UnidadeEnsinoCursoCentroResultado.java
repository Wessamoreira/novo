package negocio.facade.jdbc.administrativo;

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

import negocio.comuns.administrativo.UnidadeEnsinoCursoCentroResultadoVO;
import negocio.comuns.administrativo.UnidadeEnsinoNivelEducacionalCentroResultadoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
//import negocio.comuns.financeiro.CentroResultadoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.administrativo.UnidadeEnsinoCursoCentroResultadoInterfaceFacade;

/**
 * 
 * @author PedroOtimize
 *
 */
@Repository
@Scope("singleton")
@Lazy
public class UnidadeEnsinoCursoCentroResultado extends ControleAcesso implements UnidadeEnsinoCursoCentroResultadoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3333537929608680997L;
	protected static String idEntidade = "UnidadeEnsinoCursoCentroResultado";

	public UnidadeEnsinoCursoCentroResultado() {
		super();
	}

	@Override
	public void validarDadosAntesAdicionar(UnidadeEnsinoCursoCentroResultadoVO obj) {
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getUnidadeEnsinoVO()), "O campo Unidade Ensino (UnidadeEnsinoCursoCentroResultado) não foi informado.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getCursoVO()), "O campo Curso (UnidadeEnsinoCursoCentroResultado) não foi informado.");
//		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getCentroResultadoVO()), "O campo Centro Resultado (UnidadeEnsinoCursoCentroResultado) não foi informado.");
	}

	@Override
	public void validarDados(UnidadeEnsinoCursoCentroResultadoVO obj) {
		validarDadosAntesAdicionar(obj);
		Uteis.checkState(validarUnicidade(obj), "Já existe uma Unidade Ensino Centro de Resultado Curso para esse curso: " + obj.getCursoVO().getNome() + " e essa Unidade Ensino: " + obj.getUnidadeEnsinoVO().getNome() + ".");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.faturamento.nfe.UnidadeEnsinoCursoCentroResultadoInterfaceFacade#persistir(java.util.List, boolean, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(List<UnidadeEnsinoCursoCentroResultadoVO> lista, boolean verificarAcesso, UsuarioVO usuarioVO) {
		for (UnidadeEnsinoCursoCentroResultadoVO obj : lista) {
			persistir(obj, verificarAcesso, usuarioVO);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.faturamento.nfe.UnidadeEnsinoCursoCentroResultadoInterfaceFacade#persistir(java.util.List, boolean, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(UnidadeEnsinoCursoCentroResultadoVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) {
		validarDados(obj);
		if (obj.getCodigo() == 0) {
			incluir(obj, verificarAcesso, usuarioVO);
		} else {
			alterar(obj, verificarAcesso, usuarioVO);
		}
		UnidadeEnsinoNivelEducacionalCentroResultadoVO nivelEducacionalCentroResultado =  getFacadeFactory().getUnidadeEnsinoNivelEducacionalCentroResultadoFacade().consultaRapidaPorUnidadeEnsinoPorTipoNivelEducacional(TipoNivelEducacional.getEnum(obj.getCursoVO().getNivelEducacional()), obj.getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
		validarGeracaoUnidadeEnsinoNivelEducacionalCentroResultado(obj, usuarioVO, nivelEducacionalCentroResultado);
//		getFacadeFactory().getCentroResultadoFacade().validarGeracaoDoCentroResultadoSuperiorAutomatico(obj.getCentroResultadoVO(), nivelEducacionalCentroResultado.getCentroResultadoVO(), usuarioVO);
	}

	private void validarGeracaoUnidadeEnsinoNivelEducacionalCentroResultado(UnidadeEnsinoCursoCentroResultadoVO obj, UsuarioVO usuarioVO, UnidadeEnsinoNivelEducacionalCentroResultadoVO nivelEducacionalCentroResultado) {
		if (!Uteis.isAtributoPreenchido(nivelEducacionalCentroResultado)) {
//			CentroResultadoVO centroResultadoNivel = getFacadeFactory().getCentroResultadoFacade().validarGeracaoDoCentroResultadoAutomatico(obj.getCursoVO().getNivelEducacional_Apresentar() + " - " + obj.getUnidadeEnsinoVO().getNome(), obj.getUnidadeEnsinoVO().getCodigo(), null, null, true, usuarioVO);
//			nivelEducacionalCentroResultado.setCentroResultadoVO(centroResultadoNivel);
			nivelEducacionalCentroResultado.setUnidadeEnsinoVO(obj.getUnidadeEnsinoVO());
			nivelEducacionalCentroResultado.setTipoNivelEducacional(TipoNivelEducacional.getEnum(obj.getCursoVO().getNivelEducacional()));
			getFacadeFactory().getUnidadeEnsinoNivelEducacionalCentroResultadoFacade().persistir(nivelEducacionalCentroResultado, false, usuarioVO);
			obj.getUnidadeEnsinoVO().getUnidadeEnsinoNivelEducacionalCentroResultado().add(nivelEducacionalCentroResultado);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final UnidadeEnsinoCursoCentroResultadoVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
//			UnidadeEnsinoCursoCentroResultado.incluir(getIdEntidade(), verificarAcesso, usuario);
			final StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO UnidadeEnsinoCursoCentroResultado (unidadeEnsino, curso, centroresultado ) ");
			sql.append("    VALUES (?,?,?)");
			sql.append(" returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql.toString());
					int i = 0;
					Uteis.setValuePreparedStatement(obj.getUnidadeEnsinoVO(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getCursoVO(), ++i, sqlInserir);
//					Uteis.setValuePreparedStatement(obj.getCentroResultadoVO(), ++i, sqlInserir);
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
			throw new StreamSeiException(e);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final UnidadeEnsinoCursoCentroResultadoVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
//			UnidadeEnsinoCursoCentroResultado.alterar(getIdEntidade(), verificarAcesso, usuario);
			final StringBuilder sql = new StringBuilder();
			sql.append("UPDATE UnidadeEnsinoCursoCentroResultado ");
			sql.append("   SET unidadeEnsino=?, curso=?, centroresultado=? ");
			sql.append("   WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql.toString());
					int i = 0;
					Uteis.setValuePreparedStatement(obj.getUnidadeEnsinoVO(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getCursoVO(), ++i, sqlAlterar);
//					Uteis.setValuePreparedStatement(obj.getCentroResultadoVO(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);
					return sqlAlterar;
				}
			}) == 0) {
				incluir(obj, false, usuario);
			}
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.faturamento.nfe.UnidadeEnsinoCursoCentroResultadoInterfaceFacade#excluir(negocio.comuns.faturamento.nfe.UnidadeEnsinoCursoCentroResultadoVO, boolean, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(UnidadeEnsinoCursoCentroResultadoVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
//			UnidadeEnsinoCursoCentroResultado.excluir(getIdEntidade(), verificarAcesso, usuario);
			String sql = "DELETE FROM UnidadeEnsinoCursoCentroResultado WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, obj.getCodigo());
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	private StringBuilder getSQLPadraoConsultaBasica() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT UnidadeEnsinoCursoCentroResultado.codigo as \"UnidadeEnsinoCursoCentroResultado.codigo\",  ");
		sql.append(" unidadeEnsino.codigo as \"unidadeEnsino.codigo\", unidadeEnsino.nome as \"unidadeEnsino.nome\",  ");
		sql.append(" curso.codigo as \"curso.codigo\", curso.nome as \"curso.nome\", curso.nivelEducacional as \"curso.nivelEducacional\",  ");
		sql.append(" centroResultado.codigo as \"centroResultado.codigo\", centroResultado.descricao as \"centroResultado.descricao\",   ");
		sql.append(" centroResultado.identificadorCentroResultado as \"centroResultado.identificadorCentroResultado\" , centroResultado.centroResultadoPrincipal as \"centroResultado.centroResultadoPrincipal\",   ");
		sql.append(" centroResultadoPrincipal.identificadorCentroResultado as \"centroResultadoPrincipal.identificadorCentroResultado\" , centroResultadoPrincipal.descricao as \"centroResultadoPrincipal.descricao\"   ");
		sql.append(" FROM UnidadeEnsinoCursoCentroResultado ");
		sql.append(" inner join unidadeEnsino on unidadeEnsino.codigo = UnidadeEnsinoCursoCentroResultado.unidadeEnsino");
		sql.append(" inner join curso on curso.codigo = UnidadeEnsinoCursoCentroResultado.curso");
		sql.append(" inner join centroResultado on centroResultado.codigo = UnidadeEnsinoCursoCentroResultado.centroResultado");
		sql.append(" left join centroResultado as centroResultadoPrincipal on centroResultado.centroResultadoPrincipal = centroResultadoPrincipal.codigo ");
		return sql;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public boolean consultarSeExisteUnidadeEnsinoCursoCentroResultadoPorCursoPorUnidadeEnsino(Integer curso, Integer unidadeEnsino) {
		try {
			StringBuilder sql = new StringBuilder("select count(codigo) as qtd from unidadeEnsinoCursoCentroResultado where unidadeEnsino = ? and curso = ?  ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), unidadeEnsino, curso);
			return Uteis.isAtributoPreenchido(tabelaResultado, "qtd", TipoCampoEnum.INTEIRO);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.faturamento.nfe.NotaFiscalEntradaImpostoInterfaceFacade#consultaRapidaPorNotaFiscalEntrada(java.lang.Integer, boolean, int, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<UnidadeEnsinoCursoCentroResultadoVO> consultaRapidaPorUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) {
		try {
			consultar(getIdEntidade(), controlarAcesso, usuario);
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" WHERE unidadeEnsino.codigo = ? ");
			sqlStr.append(" ORDER BY curso.nome ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), unidadeEnsino.getCodigo());
			List<UnidadeEnsinoCursoCentroResultadoVO> vetResultado = new ArrayList<>();
			while (tabelaResultado.next()) {
				UnidadeEnsinoCursoCentroResultadoVO obj = new UnidadeEnsinoCursoCentroResultadoVO();
				montarDadosBasico(obj, tabelaResultado, nivelMontarDados);
				obj.setUnidadeEnsinoVO(unidadeEnsino);
				vetResultado.add(obj);
			}
			return vetResultado;
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	

	private Boolean validarUnicidade(UnidadeEnsinoCursoCentroResultadoVO obj) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT codigo FROM UnidadeEnsinoCursoCentroResultado ");
		sql.append(" WHERE curso = ? and unidadeensino = ?");
		if (Uteis.isAtributoPreenchido(obj.getCodigo())) {
			sql.append(" and codigo != ").append(obj.getCodigo()).append(" ");
		}
		return getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), obj.getCursoVO().getCodigo(), obj.getUnidadeEnsinoVO().getCodigo()).next();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.faturamento.nfe.NotaFiscalEntradaImpostoInterfaceFacade#consultarPorChavePrimaria(java.lang.Integer, boolean, int, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public UnidadeEnsinoCursoCentroResultadoVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) {
		try {
			consultar(getIdEntidade(), controlarAcesso, usuario);
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" WHERE UnidadeEnsinoCursoCentroResultado.codigo = ? ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), codigoPrm);
			if (!tabelaResultado.next()) {
				throw new ConsistirException("Dados Não Encontrados ( UnidadeEnsinoCursoCentroResultadoVO ).");
			}
			UnidadeEnsinoCursoCentroResultadoVO obj = new UnidadeEnsinoCursoCentroResultadoVO();
			montarDadosBasico(obj, tabelaResultado, nivelMontarDados);
			return obj;
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<UnidadeEnsinoCursoCentroResultadoVO> montarDadosConsultaBasica(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) {
		List<UnidadeEnsinoCursoCentroResultadoVO> vetResultado = new ArrayList<>();
		while (tabelaResultado.next()) {
			UnidadeEnsinoCursoCentroResultadoVO obj = new UnidadeEnsinoCursoCentroResultadoVO();
			montarDadosBasico(obj, tabelaResultado, nivelMontarDados);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	private void montarDadosBasico(UnidadeEnsinoCursoCentroResultadoVO obj, SqlRowSet dadosSQL, int nivelMontarDados) {
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo(dadosSQL.getInt("UnidadeEnsinoCursoCentroResultado.codigo"));

		obj.getUnidadeEnsinoVO().setCodigo(dadosSQL.getInt("unidadeensino.codigo"));
		obj.getUnidadeEnsinoVO().setNome(dadosSQL.getString("unidadeensino.nome"));

		obj.getCursoVO().setCodigo(dadosSQL.getInt("curso.codigo"));
		obj.getCursoVO().setNome(dadosSQL.getString("curso.nome"));
		obj.getCursoVO().setNivelEducacional(dadosSQL.getString("curso.nivelEducacional"));

//		obj.getCentroResultadoVO().setCodigo(dadosSQL.getInt("centroResultado.codigo"));
//		obj.getCentroResultadoVO().setDescricao(dadosSQL.getString("centroResultado.descricao"));
//		obj.getCentroResultadoVO().setIdentificadorCentroResultado(dadosSQL.getString("centroResultado.identificadorCentroResultado"));
//		obj.getCentroResultadoVO().getCentroResultadoPrincipal().setCodigo(dadosSQL.getInt("centroResultado.centroResultadoPrincipal"));
//		obj.getCentroResultadoVO().getCentroResultadoPrincipal().setDescricao(dadosSQL.getString("centroResultadoPrincipal.descricao"));
//		obj.getCentroResultadoVO().getCentroResultadoPrincipal().setIdentificadorCentroResultado(dadosSQL.getString("centroResultadoPrincipal.identificadorCentroResultado"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSCONSULTA) {
			return;
		}
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
	 */
	public static String getIdEntidade() {
		return UnidadeEnsinoCursoCentroResultado.idEntidade;
	}

}
