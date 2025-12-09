package negocio.facade.jdbc.administrativo;

import negocio.comuns.administrativo.UnidadeEnsinoTipoRequerimentoCentroResultadoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.protocolo.TipoRequerimentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.administrativo.UnidadeEnsinoTipoRequerimentoCentroResultadoInterfaceFacade;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * @author PedroOtimize
 *
 */
@Repository
@Scope("singleton")
@Lazy
public class UnidadeEnsinoTipoRequerimentoCentroResultado extends ControleAcesso implements UnidadeEnsinoTipoRequerimentoCentroResultadoInterfaceFacade {
	
/**
	 * 
	 */
	private static final long serialVersionUID = -2088238658167560859L;
protected static String idEntidade = "UnidadeEnsinoTipoRequerimentoCentroResultado";
	
	@Override
	public void validarDadosAntesAdicionar(UnidadeEnsinoTipoRequerimentoCentroResultadoVO obj) {
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getUnidadeEnsinoVO()), "O campo Unidade Ensino (UnidadeEnsinoTipoRequerimentoCentroResultado) não foi informado.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getTipoRequerimentoVO()), "O campo Tipo Requerimento (UnidadeEnsinoTipoRequerimentoCentroResultado) não foi informado.");
//		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getCentroResultadoVO()), "O campo Centro Resultado (UnidadeEnsinoTipoRequerimentoCentroResultado) não foi informado.");
	}
	
	@Override
	public void validarDados(UnidadeEnsinoTipoRequerimentoCentroResultadoVO obj) {
		validarDadosAntesAdicionar(obj);
		Uteis.checkState(validarUnicidade(obj), "Já existe uma Unidade Ensino Tipo Requerimento Centro de Resultado para o  Tipo Requerimento: " + obj.getTipoRequerimentoVO().getNome()+ ".");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.faturamento.nfe.UnidadeEnsinoTipoRequerimentoCentroResultadoInterfaceFacade#persistir(java.util.List, boolean, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(List<UnidadeEnsinoTipoRequerimentoCentroResultadoVO> lista, boolean verificarAcesso, UsuarioVO usuarioVO) {
		for (UnidadeEnsinoTipoRequerimentoCentroResultadoVO obj : lista) {
			persistir(obj, verificarAcesso, usuarioVO);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.faturamento.nfe.UnidadeEnsinoTipoRequerimentoCentroResultadoInterfaceFacade#persistir(java.util.List, boolean, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(UnidadeEnsinoTipoRequerimentoCentroResultadoVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) {
		validarDados(obj);
		if (obj.getCodigo() == 0) {
			incluir(obj, verificarAcesso, usuarioVO);
		} else {
			alterar(obj, verificarAcesso, usuarioVO);
		}	
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final UnidadeEnsinoTipoRequerimentoCentroResultadoVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
incluir(getIdEntidade(), verificarAcesso, usuario);
			final StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO UnidadeEnsinoTipoRequerimentoCentroResultado (unidadeEnsino, tiporequerimento, centroresultado ) ");
			sql.append("    VALUES (?,?,?)");
			sql.append(" returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql.toString());
					int i = 0;
					Uteis.setValuePreparedStatement(obj.getUnidadeEnsinoVO(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getTipoRequerimentoVO(), ++i, sqlInserir);
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
	public void alterar(final UnidadeEnsinoTipoRequerimentoCentroResultadoVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
alterar(getIdEntidade(), verificarAcesso, usuario);
			final StringBuilder sql = new StringBuilder();
			sql.append("UPDATE UnidadeEnsinoTipoRequerimentoCentroResultado ");
			sql.append("   SET unidadeEnsino=?, tiporequerimento=?, centroresultado=? ");
			sql.append("   WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql.toString());
					int i = 0;
					Uteis.setValuePreparedStatement(obj.getUnidadeEnsinoVO(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getTipoRequerimentoVO(), ++i, sqlAlterar);
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
	 * @see negocio.facade.jdbc.faturamento.nfe.UnidadeEnsinoTipoRequerimentoCentroResultadoInterfaceFacade#excluir(negocio.comuns.faturamento.nfe.UnidadeEnsinoTipoRequerimentoCentroResultadoVO, boolean, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(UnidadeEnsinoTipoRequerimentoCentroResultadoVO obj, boolean verificarAcesso, UsuarioVO usuario)  {
		try {
excluir(getIdEntidade(), verificarAcesso, usuario);
			String sql = "DELETE FROM UnidadeEnsinoTipoRequerimentoCentroResultado WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, obj.getCodigo());
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void adicionarUnidadeEnsinoTipoRequerimentoCentroResultadoVO(UnidadeEnsinoVO obj, UnidadeEnsinoTipoRequerimentoCentroResultadoVO unidadeEnsinoCentroResultadoVO,  UsuarioVO usuario)  {
		try {
			int index = 0;
			unidadeEnsinoCentroResultadoVO.setUnidadeEnsinoVO(obj);
			unidadeEnsinoCentroResultadoVO.setTipoRequerimentoVO(getFacadeFactory().getTipoRequerimentoFacade().consultarPorChavePrimaria(unidadeEnsinoCentroResultadoVO.getTipoRequerimentoVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
			validarDadosAntesAdicionar(unidadeEnsinoCentroResultadoVO);
			for (UnidadeEnsinoTipoRequerimentoCentroResultadoVO objExistente : obj.getUnidadeEnsinoTipoRequerimentoCentroResultado()) {
				if (objExistente.equalsCampoSelecaoLista(unidadeEnsinoCentroResultadoVO)) {
					obj.getUnidadeEnsinoTipoRequerimentoCentroResultado().set(index, unidadeEnsinoCentroResultadoVO);
					return;
				}
				index++;
			}
			obj.getUnidadeEnsinoTipoRequerimentoCentroResultado().add(unidadeEnsinoCentroResultadoVO);	
//			getFacadeFactory().getCentroResultadoFacade().validarGeracaoDoCentroResultadoSuperiorAutomatico(unidadeEnsinoCentroResultadoVO.getCentroResultadoVO(), obj.getCentroResultadoVO(), usuario);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void removerUnidadeEnsinoTipoRequerimentoCentroResultadoVO(UnidadeEnsinoVO obj, UnidadeEnsinoTipoRequerimentoCentroResultadoVO unidadeEnsinoCentroResultadoVO,  UsuarioVO usuario)  {
		Iterator<UnidadeEnsinoTipoRequerimentoCentroResultadoVO> i = obj.getUnidadeEnsinoTipoRequerimentoCentroResultado().iterator();
		while (i.hasNext()) {
			UnidadeEnsinoTipoRequerimentoCentroResultadoVO objExistente =  i.next();
			if (objExistente.equalsCampoSelecaoLista(unidadeEnsinoCentroResultadoVO)) {
				i.remove();
				return;
			}
		}
	}

	private StringBuilder getSQLPadraoConsultaBasica() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT UnidadeEnsinoTipoRequerimentoCentroResultado.codigo as \"UnidadeEnsinoTipoRequerimentoCentroResultado.codigo\",  ");
		sql.append(" unidadeEnsino.codigo as \"unidadeEnsino.codigo\", unidadeEnsino.nome as \"unidadeEnsino.nome\",  ");
		sql.append(" tiporequerimento.codigo as \"tiporequerimento.codigo\", tiporequerimento.nome as \"tiporequerimento.nome\",  ");
		sql.append(" centroResultado.codigo as \"centroResultado.codigo\", centroResultado.descricao as \"centroResultado.descricao\",   ");
		sql.append(" centroResultado.identificadorCentroResultado as \"centroResultado.identificadorCentroResultado\" , centroResultado.centroResultadoPrincipal as \"centroResultado.centroResultadoPrincipal\"   ");

		sql.append(" FROM UnidadeEnsinoTipoRequerimentoCentroResultado ");
		sql.append(" inner join unidadeEnsino on unidadeEnsino.codigo = UnidadeEnsinoTipoRequerimentoCentroResultado.unidadeEnsino");		
		sql.append(" inner join centroResultado on centroResultado.codigo = UnidadeEnsinoTipoRequerimentoCentroResultado.centroResultado");
		sql.append(" inner join tiporequerimento on tiporequerimento.codigo = UnidadeEnsinoTipoRequerimentoCentroResultado.tiporequerimento");
		return sql;
	}
	
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public boolean consultarSeExisteUnidadeEnsinoTipoRequerimentoCentroResultadoPorTipoRequerimentoPorUnidadeEnsino(TipoRequerimentoVO tipoRequerimentoVO, Integer unidadeEnsino)  {
		try {
			StringBuilder sql = new StringBuilder("select count(codigo) as qtd from UnidadeEnsinoTipoRequerimentoCentroResultado where unidadeEnsino = ? and tipoRequerimento = ?  ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), unidadeEnsino, tipoRequerimentoVO.getCodigo());
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
	public List<UnidadeEnsinoTipoRequerimentoCentroResultadoVO> consultaRapidaPorUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario)  {
		try {
			consultar(getIdEntidade(), controlarAcesso, usuario);
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" WHERE unidadeEnsino.codigo = ? ");
			sqlStr.append(" ORDER BY tiporequerimento.nome ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), unidadeEnsino.getCodigo());
			List<UnidadeEnsinoTipoRequerimentoCentroResultadoVO> vetResultado = new ArrayList<>();
			while (tabelaResultado.next()) {
				UnidadeEnsinoTipoRequerimentoCentroResultadoVO obj = new UnidadeEnsinoTipoRequerimentoCentroResultadoVO();
				montarDadosBasico(obj, tabelaResultado, nivelMontarDados);
				obj.setUnidadeEnsinoVO(unidadeEnsino);
				vetResultado.add(obj);
			}
			return vetResultado;
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	private Boolean validarUnicidade(UnidadeEnsinoTipoRequerimentoCentroResultadoVO obj) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT codigo FROM UnidadeEnsinoTipoRequerimentoCentroResultado ");
		sql.append(" WHERE tipoRequerimento = ? and unidadeensino = ?");
		if (Uteis.isAtributoPreenchido(obj.getCodigo())) {
			sql.append(" and codigo != ").append(obj.getCodigo()).append(" ");
		}
		return getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), obj.getTipoRequerimentoVO().getCodigo(), obj.getUnidadeEnsinoVO().getCodigo()).next();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.faturamento.nfe.NotaFiscalEntradaImpostoInterfaceFacade#consultarPorChavePrimaria(java.lang.Integer, boolean, int, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public UnidadeEnsinoTipoRequerimentoCentroResultadoVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario)  {
		try {
			consultar(getIdEntidade(), controlarAcesso, usuario);
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" WHERE UnidadeEnsinoTipoRequerimentoCentroResultado.codigo = ? ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), codigoPrm);
			if (!tabelaResultado.next()) {
				throw new ConsistirException("Dados Não Encontrados ( UnidadeEnsinoTipoRequerimentoCentroResultadoVO ).");
			}
			UnidadeEnsinoTipoRequerimentoCentroResultadoVO obj = new UnidadeEnsinoTipoRequerimentoCentroResultadoVO();
			montarDadosBasico(obj, tabelaResultado, nivelMontarDados);
			return obj;
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<UnidadeEnsinoTipoRequerimentoCentroResultadoVO> montarDadosConsultaBasica(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) {
		List<UnidadeEnsinoTipoRequerimentoCentroResultadoVO> vetResultado = new ArrayList<>();
		while (tabelaResultado.next()) {
			UnidadeEnsinoTipoRequerimentoCentroResultadoVO obj = new UnidadeEnsinoTipoRequerimentoCentroResultadoVO();
			montarDadosBasico(obj, tabelaResultado, nivelMontarDados);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	private void montarDadosBasico(UnidadeEnsinoTipoRequerimentoCentroResultadoVO obj, SqlRowSet dadosSQL, int nivelMontarDados) {
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo(dadosSQL.getInt("UnidadeEnsinoTipoRequerimentoCentroResultado.codigo"));
		
		
		obj.getUnidadeEnsinoVO().setCodigo(dadosSQL.getInt("unidadeensino.codigo"));
		obj.getUnidadeEnsinoVO().setNome(dadosSQL.getString("unidadeensino.nome"));
		
		obj.getTipoRequerimentoVO().setCodigo(dadosSQL.getInt("tipoRequerimento.codigo"));
		obj.getTipoRequerimentoVO().setNome(dadosSQL.getString("tipoRequerimento.nome"));
		
//		obj.getCentroResultadoVO().setCodigo(dadosSQL.getInt("centroResultado.codigo"));
//		obj.getCentroResultadoVO().setDescricao(dadosSQL.getString("centroResultado.descricao"));
//		obj.getCentroResultadoVO().setIdentificadorCentroResultado(dadosSQL.getString("centroResultado.identificadorCentroResultado"));
//		obj.getCentroResultadoVO().getCentroResultadoPrincipal().setCodigo(dadosSQL.getInt("centroResultado.centroResultadoPrincipal"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSCONSULTA) {
			return;
		}
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
	 */
	public static String getIdEntidade() {
		return UnidadeEnsinoTipoRequerimentoCentroResultado.idEntidade;
	}

}
