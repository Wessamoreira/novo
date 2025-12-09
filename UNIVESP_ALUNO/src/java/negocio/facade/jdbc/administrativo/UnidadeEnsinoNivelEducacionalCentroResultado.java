package negocio.facade.jdbc.administrativo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
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

import negocio.comuns.administrativo.UnidadeEnsinoNivelEducacionalCentroResultadoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.administrativo.UnidadeEnsinoNivelEducacionalCentroResultadoInterfaceFacade;


/**
 * 
 * @author PedroOtimize
 *
 */
@Repository
@Scope("singleton")
@Lazy
public class UnidadeEnsinoNivelEducacionalCentroResultado extends ControleAcesso implements UnidadeEnsinoNivelEducacionalCentroResultadoInterfaceFacade{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6336341537230274784L;
	protected static String idEntidade = "UnidadeEnsinoNivelEducacionalCentroResultado";
	
	@Override
	public void validarDadosAntesAdicionar(UnidadeEnsinoNivelEducacionalCentroResultadoVO obj) {
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getUnidadeEnsinoVO()), "O campo Unidade Ensino (UnidadeEnsinoNivelEducacionalCentroResultado) não foi informado.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getTipoNivelEducacional()), "O campo Nivel Educacional (UnidadeEnsinoNivelEducacionalCentroResultado) não foi informado.");
//		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getCentroResultadoVO()), "O campo Centro Resultado (UnidadeEnsinoNivelEducacionalCentroResultado) não foi informado.");
		
	}
	
	@Override
	public void validarDados(UnidadeEnsinoNivelEducacionalCentroResultadoVO obj) {
		validarDadosAntesAdicionar(obj);
		Uteis.checkState(validarUnicidade(obj), "Já existe uma Unidade Ensino Nivel Educacional Centro de Resultado para o  Nivel Educacional: " + obj.getTipoNivelEducacional().getDescricao()+ ".");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.faturamento.nfe.UnidadeEnsinoNivelEducacionalCentroResultadoInterfaceFacade#persistir(java.util.List, boolean, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(List<UnidadeEnsinoNivelEducacionalCentroResultadoVO> lista, boolean verificarAcesso, UsuarioVO usuarioVO) {
		for (UnidadeEnsinoNivelEducacionalCentroResultadoVO obj : lista) {
			persistir(obj, verificarAcesso, usuarioVO);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.faturamento.nfe.UnidadeEnsinoNivelEducacionalCentroResultadoInterfaceFacade#persistir(java.util.List, boolean, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(UnidadeEnsinoNivelEducacionalCentroResultadoVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) {
		validarDados(obj);
		if (obj.getCodigo() == 0) {
			incluir(obj, verificarAcesso, usuarioVO);
		} else {
			alterar(obj, verificarAcesso, usuarioVO);
		}
//		getFacadeFactory().getCentroResultadoFacade().validarGeracaoDoCentroResultadoSuperiorAutomatico(obj.getCentroResultadoVO(), obj.getUnidadeEnsinoVO().getCentroResultadoVO(), usuarioVO);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final UnidadeEnsinoNivelEducacionalCentroResultadoVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			incluir(getIdEntidade(), verificarAcesso, usuario);
			final StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO UnidadeEnsinoNivelEducacionalCentroResultado (unidadeEnsino, tipoNivelEducacional, centroresultado ) ");
			sql.append("    VALUES (?,?,?)");
			sql.append(" returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql.toString());
					int i = 0;
					Uteis.setValuePreparedStatement(obj.getUnidadeEnsinoVO(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getTipoNivelEducacional(), ++i, sqlInserir);
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
	public void alterar(final UnidadeEnsinoNivelEducacionalCentroResultadoVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			alterar(getIdEntidade(), verificarAcesso, usuario);
			final StringBuilder sql = new StringBuilder();
			sql.append("UPDATE UnidadeEnsinoNivelEducacionalCentroResultado ");
			sql.append("   SET unidadeEnsino=?, tipoNivelEducacional=?, centroresultado=? ");
			sql.append("   WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql.toString());
					int i = 0;
					Uteis.setValuePreparedStatement(obj.getUnidadeEnsinoVO(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getTipoNivelEducacional(), ++i, sqlAlterar);
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
	 * @see negocio.facade.jdbc.faturamento.nfe.UnidadeEnsinoNivelEducacionalCentroResultadoInterfaceFacade#excluir(negocio.comuns.faturamento.nfe.UnidadeEnsinoNivelEducacionalCentroResultadoVO, boolean, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(UnidadeEnsinoNivelEducacionalCentroResultadoVO obj, boolean verificarAcesso, UsuarioVO usuario)  {
		try {
			excluir(getIdEntidade(), verificarAcesso, usuario);
			String sql = "DELETE FROM UnidadeEnsinoNivelEducacionalCentroResultado WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, obj.getCodigo());
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void adicionarUnidadeEnsinoNivelEducacionalCentroResultadoVO(UnidadeEnsinoVO obj, UnidadeEnsinoNivelEducacionalCentroResultadoVO unidadeEnsinoCentroResultadoVO,  UsuarioVO usuario)  {
		int index = 0;
		unidadeEnsinoCentroResultadoVO.setUnidadeEnsinoVO(obj);
		validarDadosAntesAdicionar(unidadeEnsinoCentroResultadoVO);
		for (UnidadeEnsinoNivelEducacionalCentroResultadoVO objExistente : obj.getUnidadeEnsinoNivelEducacionalCentroResultado()) {
			if (objExistente.equalsCampoSelecaoLista(unidadeEnsinoCentroResultadoVO)) {
				obj.getUnidadeEnsinoNivelEducacionalCentroResultado().set(index, unidadeEnsinoCentroResultadoVO);
				return;
			}
			index++;
		}
		obj.getUnidadeEnsinoNivelEducacionalCentroResultado().add(unidadeEnsinoCentroResultadoVO);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void removerUnidadeEnsinoNivelEducacionalCentroResultadoVO(UnidadeEnsinoVO obj, UnidadeEnsinoNivelEducacionalCentroResultadoVO unidadeEnsinoCentroResultadoVO,  UsuarioVO usuario)  {
		Iterator<UnidadeEnsinoNivelEducacionalCentroResultadoVO> i = obj.getUnidadeEnsinoNivelEducacionalCentroResultado().iterator();
		while (i.hasNext()) {
			UnidadeEnsinoNivelEducacionalCentroResultadoVO objExistente =  i.next();
			if (objExistente.equalsCampoSelecaoLista(unidadeEnsinoCentroResultadoVO)) {
				i.remove();
				return;
			}
		}
	}
	
	

	private StringBuilder getSQLPadraoConsultaBasica() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT nivelEducacionalCentroResultado.codigo as \"nivelEducacionalCentroResultado.codigo\",  ");
		sql.append(" nivelEducacionalCentroResultado.tipoNivelEducacional as \"nivelEducacionalCentroResultado.tipoNivelEducacional\",  ");
		sql.append(" unidadeEnsino.codigo as \"unidadeEnsino.codigo\", unidadeEnsino.nome as \"unidadeEnsino.nome\",  ");
		sql.append(" centroResultado.codigo as \"centroResultado.codigo\", centroResultado.descricao as \"centroResultado.descricao\",   ");
		sql.append(" centroResultado.identificadorCentroResultado as \"centroResultado.identificadorCentroResultado\" , centroResultado.centroResultadoPrincipal as \"centroResultado.centroResultadoPrincipal\",   ");
		sql.append(" centroResultadoPrincipal.identificadorCentroResultado as \"centroResultadoPrincipal.identificadorCentroResultado\" , centroResultadoPrincipal.descricao as \"centroResultadoPrincipal.descricao\"   ");

		sql.append(" FROM unidadeEnsinoNivelEducacionalCentroResultado as  nivelEducacionalCentroResultado ");
		sql.append(" inner join unidadeEnsino on unidadeEnsino.codigo = nivelEducacionalCentroResultado.unidadeEnsino");		
		sql.append(" inner join centroResultado on centroResultado.codigo = nivelEducacionalCentroResultado.centroResultado");
		sql.append(" left join centroResultado as centroResultadoPrincipal on centroResultado.centroResultadoPrincipal = centroResultadoPrincipal.codigo ");
		return sql;
	}
	
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public boolean consultarSeExisteUnidadeEnsinoNivelEducacionalCentroResultadoPorTipoNivelEducacionalPorUnidadeEnsino(TipoNivelEducacional tipoNivelEducacional, Integer unidadeEnsino)  {
		try {
			StringBuilder sql = new StringBuilder("select count(codigo) as qtd from UnidadeEnsinoNivelEducacionalCentroResultado where unidadeEnsino = ? and tipoNivelEducacional = ?  ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), unidadeEnsino, tipoNivelEducacional);
			return Uteis.isAtributoPreenchido(tabelaResultado, "qtd", TipoCampoEnum.INTEIRO);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public UnidadeEnsinoNivelEducacionalCentroResultadoVO consultaRapidaPorUnidadeEnsinoPorTipoNivelEducacional(TipoNivelEducacional tipoNivelEducacional, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario)  {
		try {
			consultar(getIdEntidade(), controlarAcesso, usuario);
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" WHERE unidadeEnsino.codigo = ? and nivelEducacionalCentroResultado.tipoNivelEducacional = ?");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), unidadeEnsino, tipoNivelEducacional.name());
			UnidadeEnsinoNivelEducacionalCentroResultadoVO obj = new UnidadeEnsinoNivelEducacionalCentroResultadoVO();
			if (tabelaResultado.next()) {
				montarDadosBasico(obj, tabelaResultado, nivelMontarDados);	
			}
			return obj;
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
	public List<UnidadeEnsinoNivelEducacionalCentroResultadoVO> consultaRapidaPorUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario)  {
		try {
			consultar(getIdEntidade(), controlarAcesso, usuario);
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" WHERE unidadeEnsino.codigo = ? ");
			sqlStr.append(" ORDER BY nivelEducacionalCentroResultado.tipoNivelEducacional ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), unidadeEnsino.getCodigo());
			List<UnidadeEnsinoNivelEducacionalCentroResultadoVO> vetResultado = new ArrayList<>();
			while (tabelaResultado.next()) {
				UnidadeEnsinoNivelEducacionalCentroResultadoVO obj = new UnidadeEnsinoNivelEducacionalCentroResultadoVO();
				montarDadosBasico(obj, tabelaResultado, nivelMontarDados);
				obj.setUnidadeEnsinoVO(unidadeEnsino);
				vetResultado.add(obj);
			}
			return vetResultado;
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	private Boolean validarUnicidade(UnidadeEnsinoNivelEducacionalCentroResultadoVO obj) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT codigo FROM unidadeEnsinoNivelEducacionalCentroResultado ");
		sql.append(" WHERE tipoNivelEducacional = ? and unidadeensino = ?");
		if (Uteis.isAtributoPreenchido(obj.getCodigo())) {
			sql.append(" and codigo != ").append(obj.getCodigo()).append(" ");
		}
		return getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), obj.getTipoNivelEducacional().name(), obj.getUnidadeEnsinoVO().getCodigo()).next();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.faturamento.nfe.NotaFiscalEntradaImpostoInterfaceFacade#consultarPorChavePrimaria(java.lang.Integer, boolean, int, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public UnidadeEnsinoNivelEducacionalCentroResultadoVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario)  {
		try {
			consultar(getIdEntidade(), controlarAcesso, usuario);
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" WHERE nivelEducacionalCentroResultado.codigo = ? ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), codigoPrm);
			if (!tabelaResultado.next()) {
				throw new ConsistirException("Dados Não Encontrados ( UnidadeEnsinoNivelEducacionalCentroResultadoVO ).");
			}
			UnidadeEnsinoNivelEducacionalCentroResultadoVO obj = new UnidadeEnsinoNivelEducacionalCentroResultadoVO();
			montarDadosBasico(obj, tabelaResultado, nivelMontarDados);
			return obj;
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<UnidadeEnsinoNivelEducacionalCentroResultadoVO> montarDadosConsultaBasica(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) {
		List<UnidadeEnsinoNivelEducacionalCentroResultadoVO> vetResultado = new ArrayList<>();
		while (tabelaResultado.next()) {
			UnidadeEnsinoNivelEducacionalCentroResultadoVO obj = new UnidadeEnsinoNivelEducacionalCentroResultadoVO();
			montarDadosBasico(obj, tabelaResultado, nivelMontarDados);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	private void montarDadosBasico(UnidadeEnsinoNivelEducacionalCentroResultadoVO obj, SqlRowSet dadosSQL, int nivelMontarDados) {
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo(dadosSQL.getInt("nivelEducacionalCentroResultado.codigo"));
		obj.setTipoNivelEducacional(TipoNivelEducacional.valueOf(dadosSQL.getString("nivelEducacionalCentroResultado.tipoNivelEducacional")));
		
		obj.getUnidadeEnsinoVO().setCodigo(dadosSQL.getInt("unidadeensino.codigo"));
		obj.getUnidadeEnsinoVO().setNome(dadosSQL.getString("unidadeensino.nome"));
		
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
		return UnidadeEnsinoNivelEducacionalCentroResultado.idEntidade;
	}

}
