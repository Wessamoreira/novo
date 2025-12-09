package negocio.facade.jdbc.compras;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.administrativo.CargoVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.CompraVO;
import negocio.comuns.compras.DepartamentoTramiteCotacaoCompraVO;
import negocio.comuns.compras.DepartamentoTramiteCotacaoCompraVO.TipoControleFinanceiroEnum;
import negocio.comuns.compras.SituacaoTramiteEnum;
import negocio.comuns.compras.TipoDistribuicaoCotacaoEnum;
import negocio.comuns.compras.TramiteCotacaoCompraVO;
import negocio.comuns.protocolo.enumeradores.TipoPoliticaDistribuicaoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.compras.TramiteCotacaoCompraInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>CompraVO</code>. Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>CompraVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see CompraVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class TramiteCotacaoCompra extends ControleAcesso implements TramiteCotacaoCompraInterfaceFacade {

	private static final long serialVersionUID = -5299481858844693598L;
	private static String IdEntidade = "TramiteCotacaoCompra";

	

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	@SuppressWarnings("static-access")
	public void incluir(final TramiteCotacaoCompraVO tramite, Boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		try {

			tramite.validarDados();
			TramiteCotacaoCompra.alterar(getIdEntidade(), controlarAcesso, usuario);

			this.validarRestricoes(tramite);

			String sqlInsertTramite = "INSERT INTO tramite(nome, situacaotramite, tramitepadrao, responsavel, unidadeensinopadrao) VALUES (?, ?, ?, ?, ?);";

			GeneratedKeyHolder holder = new GeneratedKeyHolder();
			this.getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement statement = con.prepareStatement(sqlInsertTramite, new String[] { "codigo" });
					statement.setString(1, tramite.getNome());
					statement.setString(2, tramite.getSituacaoTramite().toString());
					statement.setBoolean(3, tramite.isTramitePadrao());
					statement.setInt(4, tramite.getResponsavel().getCodigo());
					if ( tramite.getUnidadeEnsinoPadrao().getCodigo() > 0) {
						statement.setInt(5,  tramite.getUnidadeEnsinoPadrao().getCodigo());
					} else {
						statement.setNull(5, 0);
					}
					
					return statement;
				}
			}, holder);

			tramite.setCodigo(holder.getKey().intValue());

			for (DepartamentoTramiteCotacaoCompraVO departamentoTramite : tramite.getListaDepartamentoTramite()) {
				this.incluirDepartamentoTramite(departamentoTramite);
			}

			tramite.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			tramite.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	@SuppressWarnings("static-access")
	public void incluirDepartamentoTramite(final DepartamentoTramiteCotacaoCompraVO departamentoTramite) throws Exception {
		try {

			String sqlInsertDepartamentoTramite = "INSERT INTO departamentotramite(tipodistribuicaocotacao, tipopoliticadistribuicao, observacaoobrigatoria, tramite, departamento, cargo, funcionario, prazoexecucao, observacao, orientacaoresponsavel, valorminimo, ordem, tipoControleFinanceiro, valorMaximo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

			GeneratedKeyHolder holder = new GeneratedKeyHolder();
			this.getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement statement = con.prepareStatement(sqlInsertDepartamentoTramite, new String[] { "codigo" });

					statement.setString(1, departamentoTramite.getTipoDistribuicaoCotacao().toString());

					if (departamentoTramite.getTipoPoliticaDistribuicao() != null) {
						statement.setString(2, departamentoTramite.getTipoPoliticaDistribuicao().toString());
					} else {
						statement.setNull(2, java.sql.Types.VARCHAR);
					}

					statement.setBoolean(3, departamentoTramite.isObservacaoObrigatoria());
					statement.setInt(4, departamentoTramite.getTramiteVO().getCodigo());
					statement.setInt(5, departamentoTramite.getDepartamentoVO().getCodigo());

					if (departamentoTramite.getCargoVO() != null) {
						statement.setInt(6, departamentoTramite.getCargoVO().getCodigo());
					} else {
						statement.setNull(6, java.sql.Types.INTEGER);
					}

					if (departamentoTramite.getFuncionario() != null) {
						statement.setInt(7, departamentoTramite.getFuncionario().getCodigo());
					} else {
						statement.setNull(7, java.sql.Types.INTEGER);
					}

					statement.setInt(8, departamentoTramite.getPrazoExecucao());

					if (departamentoTramite.getObservacao() != null) {
						statement.setString(9, departamentoTramite.getObservacao().toString());
					} else {
						statement.setNull(9, java.sql.Types.VARCHAR);
					}

					statement.setString(10, departamentoTramite.getOrientacaoResponsavel());
					statement.setBigDecimal(11, departamentoTramite.getValorMinimo());
					statement.setInt(12, departamentoTramite.getOrdem());

					Uteis.setValuePreparedStatement(departamentoTramite.getTipoControleFinanceiro().toString(), 13, statement);
					Uteis.setValuePreparedStatement(departamentoTramite.getValorMaximo(), 14, statement);

					return statement;
				}
			}, holder);

			departamentoTramite.setCodigo(holder.getKey().intValue());

			departamentoTramite.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			departamentoTramite.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final TramiteCotacaoCompraVO tramite, Boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {

		String sqlUpdateTramite = "UPDATE tramite SET  nome=?, situacaotramite=?, tramitepadrao=?, responsavel=?, unidadeensinopadrao=? WHERE codigo = ?;";

		try {

			tramite.validarDados();
			TramiteCotacaoCompra.alterar(getIdEntidade(), controlarAcesso, usuarioVO);
			this.validarRestricoes(tramite);
			int update = getConexao().getJdbcTemplate().update(sqlUpdateTramite, 
					tramite.getNome(), 
					tramite.getSituacaoTramite().toString(), 
					tramite.isTramitePadrao(), 
					tramite.getResponsavel().getCodigo(), 
					tramite.getUnidadeEnsinoPadrao().getCodigo() > 0 ? tramite.getUnidadeEnsinoPadrao().getCodigo() : null, 
					tramite.getCodigo());
			if (!(update > 0)) {
				throw new SQLException();
			}
			this.mergeListaDepartamentoTramite(tramite);
			for (DepartamentoTramiteCotacaoCompraVO departamentoTramiteVO : tramite.getListaDepartamentoTramite()) {
				if (departamentoTramiteVO.isNovoObj()) {
					this.incluirDepartamentoTramite(departamentoTramiteVO);
				} else {
					this.alterarDepartamentoTramite(departamentoTramiteVO);
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void validarRestricoes(final TramiteCotacaoCompraVO tramite) throws Exception {

		String sqlQueryNomeTramite = "select * from tramite where LOWER(nome) = '" + tramite.getNome().toLowerCase() + "' and codigo != " + tramite.getCodigo() + "";

		if (!getConexao().getJdbcTemplate().query(sqlQueryNomeTramite.toString(), new TramiteMapper()).isEmpty()) {

			throw new SQLException("Já existe um Trâmite Cotação Compra com o este nome!");
		}

		if (tramite.isTramitePadrao() && tramite.getSituacaoTramite().equals(SituacaoTramiteEnum.ATIVO)) {
			sqlQueryNomeTramite = "select * from tramite where  situacaotramite = 'ATIVO' and tramitepadrao = true and codigo != " + Optional.ofNullable(tramite.getCodigo()).orElse(0) + "";
			if (!getConexao().getJdbcTemplate().query(sqlQueryNomeTramite.toString(), new TramiteMapper()).isEmpty()) {

				throw new SQLException("Já existe um Trâmite Cotação Compra marcado como \"trâmite padrão\" e com a situação do trâmite \"Ativo\"!");
			}
		}

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarDepartamentoTramite(final DepartamentoTramiteCotacaoCompraVO obj) throws Exception {

		String sqlUpdateDepartamentoTramite = "UPDATE departamentotramite SET tipodistribuicaocotacao=?, tipopoliticadistribuicao=?, observacaoobrigatoria=?, tramite=?, departamento=?," 
		+ " cargo=?, funcionario=?, prazoexecucao=?, observacao=?, orientacaoresponsavel=?, valorminimo=?, ordem=?, tipoControleFinanceiro=?, valorMaximo=? WHERE codigo=?;";

		try {
			int update = getConexao()
					.getJdbcTemplate()
					.update(sqlUpdateDepartamentoTramite, 
							obj.getTipoDistribuicaoCotacao().toString(), 
							Objects.nonNull(obj.getTipoPoliticaDistribuicao()) ? obj.getTipoPoliticaDistribuicao().toString() : null, 
							obj.isObservacaoObrigatoria(), 
							obj.getTramiteVO().getCodigo(), 
							obj.getDepartamentoVO().getCodigo(), 
							Objects.nonNull(obj.getCargoVO()) ? obj.getCargoVO().getCodigo() : null, 
							Objects.nonNull(obj.getFuncionario()) ? obj.getFuncionario().getCodigo() : null,
							obj.getPrazoExecucao(), 
							obj.getObservacao(), 
							obj.getOrientacaoResponsavel(), 
							obj.getValorMinimo(), 
							obj.getOrdem(), 
							obj.getTipoControleFinanceiro().toString(), 
							obj.getValorMaximo(), 
							obj.getCodigo());

			if (!(update > 0)) {
				throw new SQLException();
			}

		} catch (Exception e) {
			throw e;
		}
	}

	public List<TramiteCotacaoCompraVO> consultarPornome(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);

		String sqlQueryNomeTramite = "select * from tramite where LOWER(nome) like ('" + valorConsulta.toLowerCase() + "%') ORDER BY codigo";
		List<TramiteCotacaoCompraVO> listaTramite = getConexao().getJdbcTemplate().query(sqlQueryNomeTramite, new TramiteMapper());

		listaTramite.forEach(p -> p.setNovoObj(false));
		return listaTramite;
	}

	public List<TramiteCotacaoCompraVO> consultarPorId(int valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);

		String sqlQueryNomeTramite = "select * from tramite where codigo = " + valorConsulta + " ORDER BY codigo";
		List<TramiteCotacaoCompraVO> listaTramite = getConexao().getJdbcTemplate().query(sqlQueryNomeTramite, new TramiteMapper());

		listaTramite.forEach(p -> p.setNovoObj(false));
		return listaTramite;
	}

	public void popularListaDepartamentoTransite(TramiteCotacaoCompraVO obj, UsuarioVO usuario) throws Exception {

		String sqlQueryDepartamentoTramiteId = "select * from departamentotramite where tramite = ?";

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlQueryDepartamentoTramiteId, new Object[] { obj.getCodigo() });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( Departamento Tramite ).");
		}
		List<DepartamentoTramiteCotacaoCompraVO> listaDepartamentoTramite = montarDados(tabelaResultado, usuario);

		listaDepartamentoTramite.forEach(p -> obj.adicionarDepartamentoTramite(p));
	}

	public DepartamentoTramiteCotacaoCompraVO consultarDepartamentoTramitePorCodigo(int codigo, UsuarioVO usuario) throws Exception {

		String sqlQueryDepartamentoTramiteId = "select * from departamentotramite where codigo = ?";

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlQueryDepartamentoTramiteId, new Object[] { codigo });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( Departamento Tramite ).");
		}
		List<DepartamentoTramiteCotacaoCompraVO> listaDepartamentoTramite = montarDados(tabelaResultado, usuario);

		return listaDepartamentoTramite.get(0);
	}

	private void mergeListaDepartamentoTramite(TramiteCotacaoCompraVO tramite) throws Exception {

		String sqlQueryDepartamentoTramiteId = "select codigo from departamentotramite where tramite = ?";

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlQueryDepartamentoTramiteId, new Object[] { tramite.getCodigo() });

		ArrayList<Integer> idList = new ArrayList<>();

		while (tabelaResultado.next()) {
			idList.add(tabelaResultado.getInt("codigo"));
		}

		tramite.getListaDepartamentoTramite().forEach(p -> idList.remove(p.getCodigo()));

		for (Integer id : idList) {
			this.excluirDepartamentoTramite(id);
		}
	}

	public List<DepartamentoTramiteCotacaoCompraVO> montarDados(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {

		List<DepartamentoTramiteCotacaoCompraVO> listaDepartamentoTramiteVO = new ArrayList<>();
		do {

			DepartamentoTramiteCotacaoCompraVO departamentoTramite = new DepartamentoTramiteCotacaoCompraVO();
			departamentoTramite.setCodigo(new Integer(dadosSQL.getInt("codigo")));
			departamentoTramite.setTipoDistribuicaoCotacao(TipoDistribuicaoCotacaoEnum.valueOf(dadosSQL.getString("tipodistribuicaocotacao")));

			if (dadosSQL.getString("tipopoliticadistribuicao") != null) {
				departamentoTramite.setTipoPoliticaDistribuicao(TipoPoliticaDistribuicaoEnum.valueOf(dadosSQL.getString("tipopoliticadistribuicao")));
			}

			departamentoTramite.setObservacaoObrigatoria(dadosSQL.getBoolean("observacaoobrigatoria"));

			DepartamentoVO departamentoVO = getFacadeFactory().getDepartamentoFacade().consultarPorCodigo(dadosSQL.getInt("departamento"), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario).get(0);
			departamentoTramite.setDepartamentoVO(departamentoVO);

			if (dadosSQL.getInt("cargo") != 0) {
				CargoVO cargoVO = (CargoVO) getFacadeFactory().getCargoFacade().consultarPorCodigo(dadosSQL.getInt("cargo"), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario).get(0);
				departamentoTramite.setCargoVO(cargoVO);
			}

			if (dadosSQL.getInt("funcionario") != 0) {
				FuncionarioVO funcionario = getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimariaUnica(dadosSQL.getInt("funcionario"), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
				departamentoTramite.setFuncionario(funcionario);
			}

			departamentoTramite.setPrazoExecucao(dadosSQL.getInt("prazoexecucao"));

			departamentoTramite.setOrdem(dadosSQL.getInt("ordem"));
			departamentoTramite.setObservacao(dadosSQL.getString("observacao"));
			departamentoTramite.setOrientacaoResponsavel(dadosSQL.getString("orientacaoresponsavel"));

			departamentoTramite.setValorMinimo(dadosSQL.getBigDecimal("valorminimo"));
			departamentoTramite.setValorMaximo(dadosSQL.getBigDecimal("valorMaximo"));

			departamentoTramite.setTipoControleFinanceiro(TipoControleFinanceiroEnum.valueOf(dadosSQL.getString("tipoControleFinanceiro")));

			departamentoTramite.setNovoObj(Boolean.FALSE);

			listaDepartamentoTramiteVO.add(departamentoTramite);

		} while (dadosSQL.next());

		return listaDepartamentoTramiteVO;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(TramiteCotacaoCompraVO obj, Boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {

		String sqlDeleteTramite = "DELETE FROM tramite WHERE codigo = ?;";
		TramiteCotacaoCompra.excluir(getIdEntidade(), controlarAcesso, usuarioVO);

		for (DepartamentoTramiteCotacaoCompraVO departamentoTramite : obj.getListaDepartamentoTramite()) {
			this.excluirDepartamentoTramite(departamentoTramite);
		}
		getConexao().getJdbcTemplate().update(sqlDeleteTramite, new Object[] { obj.getCodigo() });
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirDepartamentoTramite(DepartamentoTramiteCotacaoCompraVO obj) throws Exception {

		this.excluirDepartamentoTramite(obj.getCodigo());
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirDepartamentoTramite(Integer obj) throws Exception {

		String sqlDeleteDepartamentoTramite = "DELETE FROM departamentotramite WHERE codigo = ?;";
		getConexao().getJdbcTemplate().update(sqlDeleteDepartamentoTramite, new Object[] { obj });
	}

	public static String getIdEntidade() {
		return TramiteCotacaoCompra.IdEntidade;
	}

	@Override
	public void setIdEntidade(String idEntidade) {
		TramiteCotacaoCompra.IdEntidade = idEntidade;
	}

	@Override
	public List<TramiteCotacaoCompraVO> consultarSituacaoAtivaTramitePadrao(boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlQueryNomeTramite = "select * from tramite where  situacaotramite = 'ATIVO' ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlQueryNomeTramite);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	@Override
	public List<TramiteCotacaoCompraVO> consultarSituacaoAtiva(boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);

		String sqlQueryNomeTramite = "select * from tramite where  situacaotramite = 'ATIVO'";
		List<TramiteCotacaoCompraVO> listaTramite = getConexao().getJdbcTemplate().query(sqlQueryNomeTramite, new TramiteMapper());

		listaTramite.forEach(p -> p.setNovoObj(false));
		return listaTramite;
	}

	@Override
	public boolean consultarUsoTramite(TramiteCotacaoCompraVO obj, UsuarioVO usuarioLogado) {
		String sqlDeleteDepartamentoTramite = "select tramitecotacaocompra from cotacao where tramitecotacaocompra = ?;";
		SqlRowSet sqlRowSet = getConexao().getJdbcTemplate().queryForRowSet(sqlDeleteDepartamentoTramite, new Object[] { obj.getCodigo() });

		return sqlRowSet.next();

	}

	@Override
	public TramiteCotacaoCompraVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sql = "SELECT * FROM tramite WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, codigoPrm);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( TramiteCotacaoCompraVO ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}
	
	public List<TramiteCotacaoCompraVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<TramiteCotacaoCompraVO> vetResultado = new ArrayList<>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	public TramiteCotacaoCompraVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		TramiteCotacaoCompraVO obj = new TramiteCotacaoCompraVO();
		obj.setNovoObj(false);
		obj.setCodigo((dadosSQL.getInt("codigo")));
		obj.setNome(dadosSQL.getString("nome"));
		obj.setSituacaoTramite(SituacaoTramiteEnum.valueOf(dadosSQL.getString("situacaoTramite")));
		obj.setTramitePadrao(dadosSQL.getBoolean("tramitePadrao"));
		obj.getResponsavel().setCodigo(dadosSQL.getInt("responsavel"));
		obj.getUnidadeEnsinoPadrao().setCodigo(dadosSQL.getInt("unidadeensinopadrao"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
			return obj;
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			return obj;
		}
		obj.setResponsavel(Uteis.montarDadosVO(dadosSQL.getInt("responsavel"), UsuarioVO.class, p -> getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(p, Uteis.NIVELMONTARDADOS_TODOS, usuario)));
		obj.setUnidadeEnsinoPadrao(Uteis.montarDadosVO(dadosSQL.getInt("unidadeensinopadrao"), UnidadeEnsinoVO.class, p -> getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(p, false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario)));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		popularListaDepartamentoTransite(obj, usuario);
		return obj;
	}

	class TramiteMapper implements RowMapper<TramiteCotacaoCompraVO> {

		public TramiteCotacaoCompraVO mapRow(ResultSet resultSet, int i) throws SQLException {

			TramiteCotacaoCompraVO tramite = new TramiteCotacaoCompraVO();

			tramite.setNovoObj(false);
			tramite.setCodigo(resultSet.getInt("codigo"));

			tramite.setNome(resultSet.getString("nome"));

			tramite.setSituacaoTramite(SituacaoTramiteEnum.valueOf(resultSet.getString("situacaoTramite")));

			tramite.setTramitePadrao(resultSet.getBoolean("tramitePadrao"));
			if(Uteis.isAtributoPreenchido(resultSet.getInt("unidadeensinopadrao"))){
				
			}
			

			return tramite;
		}
	}
}