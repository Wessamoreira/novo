package negocio.facade.jdbc.compras;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.EstatisticaEstoqueCategoriaProdutoVO;
import negocio.comuns.compras.EstatisticaEstoqueVO;
import negocio.comuns.compras.EstoqueVO;
import negocio.comuns.compras.ItemCotacaoUnidadeEnsinoVO;
import negocio.comuns.compras.OperacaoEstoqueVO;
import negocio.comuns.compras.ProdutoServicoVO;
import negocio.comuns.compras.enumeradores.OperacaoEstoqueEnum;
import negocio.comuns.compras.enumeradores.TipoOperacaoEstoqueOrigemEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.compras.EstoqueInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class Estoque extends ControleAcesso implements EstoqueInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8250977715609900384L;
	protected static String idEntidade;

	public Estoque() {
		setIdEntidade("Estoque");
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final EstoqueVO obj, UsuarioVO usuarioVO, Boolean controlarAcesso) throws Exception {
		try {
			Estoque.incluir(getIdEntidade(), controlarAcesso, usuarioVO);
			EstoqueVO.validarDados(obj);
			final String sql = "INSERT INTO Estoque( quantidade, unidadeEnsino, produto, estoqueMinimo, estoqueMaximo, precoUnitario, dataEntrada ) VALUES ( ?, ?, ?, ?, ?, ?, ? ) returning codigo";
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setDouble(1, obj.getQuantidade().doubleValue());
					sqlInserir.setInt(2, obj.getUnidadeEnsino().getCodigo().intValue());
					sqlInserir.setInt(3, obj.getProduto().getCodigo().intValue());
					sqlInserir.setDouble(4, obj.getEstoqueMinimo());
					sqlInserir.setDouble(5, obj.getEstoqueMaximo());
					sqlInserir.setDouble(6, obj.getPrecoUnitario());
					sqlInserir.setDate(7, Uteis.getDataJDBC(obj.getDataEntrada()));
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {

				public Object extractData(ResultSet arg0) throws SQLException {
					if (arg0.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));

			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirSemValidarDados(final EstoqueVO obj) throws Exception {
		try {
			final String sql = "INSERT INTO Estoque( quantidade, unidadeEnsino, produto, estoqueMinimo, estoqueMaximo, precoUnitario, dataEntrada ) VALUES (?, ?, ?, ?, ?, ?, ? ) returning codigo";
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setDouble(1, obj.getQuantidade().doubleValue());
					sqlInserir.setInt(2, obj.getUnidadeEnsino().getCodigo().intValue());
					sqlInserir.setInt(3, obj.getProduto().getCodigo().intValue());
					sqlInserir.setDouble(4, obj.getEstoqueMinimo());
					sqlInserir.setDouble(5, obj.getEstoqueMaximo());
					sqlInserir.setDouble(6, obj.getPrecoUnitario());
					sqlInserir.setDate(7, Uteis.getDataJDBC(obj.getDataEntrada()));
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(ResultSet arg0) throws SQLException {
					if (arg0.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
			obj.setNovoObj(Boolean.FALSE);

		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final EstoqueVO obj, UsuarioVO usuarioVO, Boolean controlarAcesso) throws Exception {
		try {
			Estoque.alterar(getIdEntidade(), controlarAcesso, usuarioVO);
			EstoqueVO.validarDados(obj);
			final String sql = "UPDATE Estoque set quantidade=?, unidadeEnsino=?, produto=?, estoqueMinimo=?, precoUnitario = ?, dataEntrada=?, estoqueMaximo=?  WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setDouble(1, obj.getQuantidade().doubleValue());
					sqlAlterar.setInt(2, obj.getUnidadeEnsino().getCodigo().intValue());
					sqlAlterar.setInt(3, obj.getProduto().getCodigo().intValue());
					sqlAlterar.setDouble(4, obj.getEstoqueMinimo());
					sqlAlterar.setDouble(5, obj.getPrecoUnitario());
					sqlAlterar.setDate(6, Uteis.getDataJDBC(obj.getDataEntrada()));
					sqlAlterar.setDouble(7, obj.getEstoqueMaximo());
					sqlAlterar.setInt(8, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});

		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
		System.out.println("teste");
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarEstoqueMinimo(final EstoqueVO obj) throws Exception {
		try {
			final String sql = "UPDATE Estoque set estoqueMinimo=?, estoqueMaximo=? WHERE ((unidadeEnsino = ?) and (produto=?))";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {

					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setDouble(1, obj.getEstoqueMinimo().doubleValue());
					sqlAlterar.setDouble(2, obj.getEstoqueMaximo().doubleValue());
					sqlAlterar.setInt(3, obj.getUnidadeEnsino().getCodigo().intValue());
					sqlAlterar.setInt(4, obj.getProduto().getCodigo().intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void atualizarEstoqueCampoQuantidade(EstoqueVO obj, UsuarioVO usuario) throws Exception {
		StringBuilder sql1 = new StringBuilder("UPDATE estoque set quantidade=  ").append(obj.getQuantidade());
		sql1.append(" WHERE codigo = ( ").append(obj.getCodigo()).append(" ) ");
		sql1.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		getConexao().getJdbcTemplate().update(sql1.toString());
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(List<EstoqueVO> lista, UsuarioVO usuarioVO, Boolean controlarAcesso) throws Exception {
		try {
			Estoque.excluir(getIdEntidade(), controlarAcesso, usuarioVO);
			StringBuilder sb = new StringBuilder("DELETE FROM Estoque where codigo in (").append(UteisTexto.converteListaEntidadeCampoCodigoParaString(lista)).append(") ");
			sb.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			getConexao().getJdbcTemplate().execute(sb.toString());
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(EstoqueVO obj, UsuarioVO usuarioVO, Boolean controlarAcesso) throws Exception {
		try {
			Estoque.excluir(getIdEntidade(), controlarAcesso, usuarioVO);
			String sql = "DELETE FROM Estoque WHERE ((codigo = ?)) " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql, obj.getCodigo());
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	@Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public static synchronized List<OperacaoEstoqueVO> estornaEstoque(String codOrigem, TipoOperacaoEstoqueOrigemEnum tipoOperacaoEstoqueOrigemEnum,  Integer produto, Double quantidade, Double precoUnitario, Date data, Integer unidadeEnsino, OperacaoEstoqueEnum operacaoEstoque,UsuarioVO usuario) throws Exception {
		List<OperacaoEstoqueVO> listaOperacaoEstoque = getFacadeFactory().getOperacaoEstoqueFacade().consultaRapidaPorCodOrigemPorTipoOperacaoEstoqueOrigemEnum(codOrigem, tipoOperacaoEstoqueOrigemEnum, unidadeEnsino, Uteis.NIVELMONTARDADOS_TODOS, usuario);
		if(listaOperacaoEstoque.isEmpty()){
			if (operacaoEstoque.isIncluir()) {
				EstoqueVO obj = getFacadeFactory().getEstoqueFacade().consultarPorUnidadeEnsinoProduto(unidadeEnsino, precoUnitario, produto, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
				incluirEstoque(listaOperacaoEstoque, codOrigem, tipoOperacaoEstoqueOrigemEnum, obj, produto, quantidade, precoUnitario, unidadeEnsino, operacaoEstoque, usuario);
			} else if (operacaoEstoque.isExcluir()) {
				List<EstoqueVO> listaEstoque = getFacadeFactory().getEstoqueFacade().consultarPorUnidadeEnsinoProdutoQueNaoTenhaVinculoComOperacaoEstoque(unidadeEnsino, precoUnitario, data, produto, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
				if(listaEstoque.isEmpty()){
					listaEstoque = getFacadeFactory().getEstoqueFacade().consultarPorUnidadeEnsinoProduto(unidadeEnsino, precoUnitario, data, produto, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
				}
				retirarEstoque(listaOperacaoEstoque, listaEstoque, codOrigem, tipoOperacaoEstoqueOrigemEnum, produto, quantidade, unidadeEnsino, operacaoEstoque, usuario);
			}
			getFacadeFactory().getOperacaoEstoqueFacade().persistir(listaOperacaoEstoque, false, usuario);
		}else{
			for (OperacaoEstoqueVO operacaoVO : listaOperacaoEstoque) {
				operacaoVO.setCodigo(0);
				if(operacaoVO.getOperacaoEstoqueEnum().isIncluir()){
					operacaoVO.getEstoqueVO().setQuantidade(operacaoVO.getEstoqueVO().getQuantidade() - operacaoVO.getQuantidade());
					operacaoVO.setOperacaoEstoqueEnum(OperacaoEstoqueEnum.EXCLUIR);
				}else{
					operacaoVO.getEstoqueVO().setQuantidade(operacaoVO.getEstoqueVO().getQuantidade() + operacaoVO.getQuantidade());
					operacaoVO.setOperacaoEstoqueEnum(OperacaoEstoqueEnum.INCLUIR);
				}
				getFacadeFactory().getEstoqueFacade().atualizarEstoqueCampoQuantidade(operacaoVO.getEstoqueVO(), usuario);
				getFacadeFactory().getOperacaoEstoqueFacade().persistir(operacaoVO, false, usuario);
			}
		}
		return listaOperacaoEstoque;
	}

	@Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public static synchronized List<OperacaoEstoqueVO> manipularEstoque(String codOrigem, TipoOperacaoEstoqueOrigemEnum tipoOperacaoEstoqueOrigemEnum, Integer produto, Double quantidade, Double precoUnitario, Date data, Integer unidadeEnsino, OperacaoEstoqueEnum operacaoEstoque, UsuarioVO usuario) throws Exception {
		List<OperacaoEstoqueVO> listaOperacaoEstoque = new ArrayList<>();
		if (operacaoEstoque.isIncluir()) {
			EstoqueVO obj = getFacadeFactory().getEstoqueFacade().consultarPorUnidadeEnsinoProduto(unidadeEnsino, precoUnitario, produto, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			incluirEstoque(listaOperacaoEstoque, codOrigem, tipoOperacaoEstoqueOrigemEnum, obj, produto, quantidade, precoUnitario, unidadeEnsino, operacaoEstoque, usuario);
		} else if (operacaoEstoque.isExcluir()) {
			List<EstoqueVO> listaEstoque = getFacadeFactory().getEstoqueFacade().consultarPorUnidadeEnsinoProduto(unidadeEnsino, precoUnitario, data, produto, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			retirarEstoque(listaOperacaoEstoque, listaEstoque, codOrigem, tipoOperacaoEstoqueOrigemEnum, produto, quantidade, unidadeEnsino, operacaoEstoque, usuario);
		}
		getFacadeFactory().getOperacaoEstoqueFacade().persistir(listaOperacaoEstoque, false, usuario);
		return listaOperacaoEstoque;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private static void incluirEstoque(List<OperacaoEstoqueVO> listaOperacaoEstoque, String codOrigem, TipoOperacaoEstoqueOrigemEnum tipoOperacaoEstoqueOrigemEnum, EstoqueVO obj, Integer produto, Double quantidade, Double precoUnitario, Integer unidadeEnsino, OperacaoEstoqueEnum operacaoEstoque, UsuarioVO usuarioVO) throws Exception {
		if (!Uteis.isAtributoPreenchido(obj)){
			obj.getProduto().setCodigo(produto);
			obj.setQuantidade(quantidade);
			obj.setPrecoUnitario(precoUnitario);
			obj.setDataEntrada(new Date());
			obj.setEstoqueMinimo(consultarPorEstoqueMinimoProduto(produto, unidadeEnsino));
			obj.setEstoqueMaximo(consultarPorEstoqueMaximoProduto(produto, unidadeEnsino));
			obj.getUnidadeEnsino().setCodigo(unidadeEnsino);
			getFacadeFactory().getEstoqueFacade().incluir(obj, usuarioVO, false);
		}else{
			obj.setQuantidade(obj.getQuantidade() + quantidade);
			getFacadeFactory().getEstoqueFacade().alterar(obj, usuarioVO, false);	
		}
		OperacaoEstoqueVO operacao = new OperacaoEstoqueVO();
		operacao.setEstoqueVO(obj);
		operacao.setCodOrigem(codOrigem);		
		operacao.setTipoOperacaoEstoqueOrigemEnum(tipoOperacaoEstoqueOrigemEnum);
		operacao.setOperacaoEstoqueEnum(operacaoEstoque);
		operacao.setQuantidade(quantidade);
		operacao.setUsuario(usuarioVO);
		listaOperacaoEstoque.add(operacao);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private static void retirarEstoque(List<OperacaoEstoqueVO> listaOperacaoEstoque, List<EstoqueVO> objs, String codOrigem, TipoOperacaoEstoqueOrigemEnum tipoOperacaoEstoqueOrigemEnum, Integer produto, Double quantidade, Integer unidadeEnsino, OperacaoEstoqueEnum operacaoEstoque, UsuarioVO usuarioVO) throws Exception {
		Double residuo = quantidade;
		for (EstoqueVO obj : objs) {
			if(Uteis.isAtributoPreenchido(obj.getQuantidade())){
				OperacaoEstoqueVO operacao = new OperacaoEstoqueVO();
				operacao.setEstoqueVO(obj);
				operacao.setCodOrigem(codOrigem);		
				operacao.setTipoOperacaoEstoqueOrigemEnum(tipoOperacaoEstoqueOrigemEnum);
				operacao.setOperacaoEstoqueEnum(operacaoEstoque);
				operacao.setUsuario(usuarioVO);
				if (obj.getQuantidade().doubleValue() >= residuo.doubleValue()) {
					obj.setQuantidade(Uteis.arrendondarForcando2CadasDecimais(obj.getQuantidade() - residuo));
					operacao.setQuantidade(residuo);
					residuo = 0.0;
				} else {
					residuo = residuo.doubleValue() - obj.getQuantidade().doubleValue();
					operacao.setQuantidade(obj.getQuantidade());
					obj.setQuantidade(0.0);
				}
				getFacadeFactory().getEstoqueFacade().alterar(obj, usuarioVO, false);
				listaOperacaoEstoque.add(operacao);
				if (residuo.doubleValue() == 0.0) {
					return;
				}	
			}
		}
		ProdutoServicoVO pr = getFacadeFactory().getProdutoServicoFacade().consultarPorChavePrimaria(produto, false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO);
		if (unidadeEnsino.intValue() != 0) {
			UnidadeEnsinoVO un = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(unidadeEnsino, false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO);
			throw new Exception("O Produto: " + pr.getNome().toUpperCase() + " da Unidade de Ensino " + un.getNome().toUpperCase() + " cadastrado na base, não possui estoque disponível para realizar essa operação.");
		}
		throw new Exception("O Produto: " + pr.getNome().toUpperCase() + " cadastrado na base, não possui estoque disponível para realizar essa operação.");

	}

	public Boolean existeEstoqueProduto(Integer unidadeEnsino, Integer produto, Double quantidade, UsuarioVO usuario) throws Exception {
		List<EstoqueVO> objs = consultarPorUnidadeEnsinoProduto(unidadeEnsino, 0.0, null, produto, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		Double residuo = quantidade;
		for (EstoqueVO obj : objs) {
			if (obj.getQuantidade().doubleValue() >= residuo.doubleValue()) {
				residuo = 0.0;
			} else {
				residuo = residuo.doubleValue() - obj.getQuantidade().doubleValue();
			}
		}
		if (residuo.doubleValue() <= 0) {
			return true;
		} else {
			return false;
		}
	}

	public List<EstoqueVO> consultarPorNomeProduto(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT Estoque.* FROM Estoque, ProdutoServico WHERE Estoque.produto = ProdutoServico.codigo and lower (ProdutoServico.nome) like('" + valorConsulta.toLowerCase() + "%') ORDER BY ProdutoServico.nome";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr = "SELECT Estoque.* FROM Estoque, ProdutoServico WHERE Estoque.produto = ProdutoServico.codigo and lower (ProdutoServico.nome) like('" + valorConsulta.toLowerCase() + "%') and Estoque.unidadeEnsino = " + unidadeEnsino.intValue() + " ORDER BY ProdutoServico.nome";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<EstoqueVO> consultarPorNomeProdutoAgrupado(String valorConsulta, Integer unidadeEnsino, String tipoEstoque, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getSQLPadraoConsultaBasicaGroup();
		sqlStr.append(" WHERE (produtoServico.nome ilike '").append(valorConsulta).append("%')");
		if (unidadeEnsino.intValue() != 0) {
			sqlStr.append(" and Estoque.unidadeEnsino = ").append(unidadeEnsino.intValue()).append(" ");
		}
		if (tipoEstoque.equals("IEM")) {
			sqlStr.append(" AND (SELECT sum(quantidade) from estoque as estoque2 left join ProdutoServico as ProdutoServico2 on ProdutoServico2.codigo = estoque2.produto");
			sqlStr.append(" where estoque2.produto = estoque.produto and ProdutoServico2.nome = ProdutoServico.nome and estoque2.unidadeensino = estoque.unidadeensino group by produto ) < Estoque.estoqueMinimo");
		}
		if (tipoEstoque.equals("SEM")) {
			sqlStr.append(" AND (SELECT sum(quantidade) from estoque as estoque2 left join ProdutoServico as ProdutoServico2 on ProdutoServico2.codigo = estoque2.produto");
			sqlStr.append(" where estoque2.produto = estoque.produto and ProdutoServico2.nome = ProdutoServico.nome and estoque2.unidadeensino = estoque.unidadeensino group by produto ) >= Estoque.estoqueMinimo ");
		}
		sqlStr.append(" Group by Estoque.produto, Estoque.unidadeEnsino, Estoque.estoqueMinimo, Estoque.estoqueMaximo, ProdutoServico.nome, unidadeensino.nome, produtoservico.nome ORDER BY UnidadeEnsino.nome, ProdutoServico.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	@Override
	public List<EstoqueVO> consultarPorProdutoPorUnidadeEnsinoAgrupado(Integer produto, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT sum(Estoque.quantidade) as quantidade,  ");
		sqlStr.append(" unidadeensino.codigo as \"unidadeensino.codigo\", unidadeensino.nome as \"unidadeensino.nome\", ");
		sqlStr.append(" produtoservico.codigo as \"produtoservico.codigo\", produtoservico.nome as \"produtoservico.nome\" ");

		sqlStr.append(" FROM Estoque ");
		sqlStr.append(" inner join ProdutoServico on ProdutoServico.codigo = estoque.produto ");
		sqlStr.append(" inner join unidadeensino on unidadeensino.codigo = estoque.unidadeensino ");
		sqlStr.append(" WHERE produtoServico.codigo = ").append(produto).append(" ");
		sqlStr.append(" and  Estoque.quantidade > 0 ");
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sqlStr.append(" and unidadeensino.codigo = ").append(unidadeEnsino.intValue()).append(" ");
		}
		sqlStr.append(" Group by unidadeensino.codigo, unidadeensino.nome, produtoservico.codigo, produtoservico.nome ORDER BY UnidadeEnsino.nome");
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<EstoqueVO> vetResultado = new ArrayList<>(0);
		while (dadosSQL.next()) {
			EstoqueVO obj = new EstoqueVO();
			obj.getProduto().setCodigo(dadosSQL.getInt("produtoServico.codigo"));
			obj.getProduto().setNome(dadosSQL.getString("produtoServico.nome"));
			obj.getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeEnsino.codigo"));
			obj.getUnidadeEnsino().setNome(dadosSQL.getString("unidadeEnsino.nome"));
			obj.setQuantidade(dadosSQL.getDouble("quantidade"));
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	public List<EstoqueVO> montarDadosConsultaBasica(SqlRowSet tabelaResultado) throws Exception {
		List<EstoqueVO> vetResultado = new ArrayList<EstoqueVO>(0);
		while (tabelaResultado.next()) {
			EstoqueVO obj = new EstoqueVO();
			montarDadosBasico(obj, tabelaResultado);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	private void montarDadosBasico(EstoqueVO obj, SqlRowSet dadosSQL) throws Exception {
		obj.setNivelMontarDados(NivelMontarDados.BASICO);
		// obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.getProduto().setCodigo(dadosSQL.getInt("Estoque.produto"));
		obj.getProduto().setNome(dadosSQL.getString("produtoServico.nome"));
		obj.getUnidadeEnsino().setCodigo(dadosSQL.getInt("Estoque.unidadeEnsino"));
		obj.getUnidadeEnsino().setNome(dadosSQL.getString("unidadeEnsino.nome"));
		obj.setEstoqueMinimo(dadosSQL.getDouble("estoqueminimo"));
		obj.setEstoqueMaximo(dadosSQL.getDouble("estoqueMaximo"));
		obj.setQuantidade(dadosSQL.getDouble("quantidade"));

	}

	public List<EstoqueVO> consultarPorNomeCategoriaProdutoAgrupado(String valorConsulta, Integer unidadeEnsino, String tipoEstoque, boolean agruparPorEstoque, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getSQLPadraoConsultaBasicaGroup();
		sqlStr.append("  WHERE Estoque.produto = ProdutoServico.codigo and CategoriaProduto.codigo = ProdutoServico.CategoriaProduto " + "  and lower (CategoriaProduto.nome) like('" + valorConsulta.toLowerCase() + "%') ");
		if (unidadeEnsino.intValue() != 0) {
			sqlStr.append(" and Estoque.unidadeEnsino = " + unidadeEnsino.intValue() + " ");
		}
		if (tipoEstoque.equals("IEM")) {
			sqlStr.append(" AND (SELECT sum(quantidade) from estoque as estoque2 left join ProdutoServico as prod on prod.codigo = estoque2.produto left join CategoriaProduto as categProd on categProd.codigo = prod.CategoriaProduto");
			sqlStr.append(" where estoque2.produto = estoque.produto and categProd.nome = CategoriaProduto.nome and estoque2.unidadeensino = estoque.unidadeensino group by produto ) < Estoque.estoqueMinimo");
		}
		if (tipoEstoque.equals("SEM")) {
			sqlStr.append(" AND (SELECT sum(quantidade) from estoque as estoque2 left join ProdutoServico as prod on prod.codigo = estoque2.produto left join CategoriaProduto as categProd on categProd.codigo = prod.CategoriaProduto");
			sqlStr.append(" where estoque2.produto = estoque.produto and categProd.nome = CategoriaProduto.nome and estoque2.unidadeensino = estoque.unidadeensino group by produto ) >= Estoque.estoqueMinimo");
		}
		sqlStr.append(" Group by Estoque.produto, Estoque.unidadeEnsino, Estoque.estoqueMinimo, Estoque.estoqueMaximo, ProdutoServico.nome, unidadeensino.nome, produtoservico.nome ");
		if (agruparPorEstoque) {
			sqlStr.append(", Estoque.codigo ");
		}
		sqlStr.append(" ORDER BY ProdutoServico.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public List<EstoqueVO> consultarPorNomeUnidadeEnsinoAgrupado(String valorConsulta, Integer unidadeEnsino, String tipoEstoque, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT Estoque.produto, Estoque.unidadeEnsino, Estoque.estoqueMinimo, sum(Estoque.quantidade) as quantidade FROM Estoque, UnidadeEnsino WHERE Estoque.UnidadeEnsino = UnidadeEnsino.codigo and lower (UnidadeEnsino.nome) like('" + valorConsulta.toLowerCase() + "%') ";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr += " and Estoque.unidadeEnsino = " + unidadeEnsino.intValue() + " ";
		}
		if (tipoEstoque.equals("IEM")) {
			sqlStr += "AND (SELECT sum(quantidade) from estoque as estoque2 left join unidadeensino as unidadeensino2 on unidadeensino2.codigo = estoque2.unidadeensino";
			sqlStr += " where estoque2.produto = estoque.produto and unidadeensino2.nome = unidadeensino.nome group by produto ) < Estoque.estoqueMinimo";
		}
		if (tipoEstoque.equals("SEM")) {
			sqlStr += "AND (SELECT sum(quantidade) from estoque as estoque2 left join unidadeensino as unidadeensino2 on unidadeensino2.codigo = estoque2.unidadeensino";
			sqlStr += " where estoque2.produto = estoque.produto and unidadeensino2.nome = unidadeensino.nome group by produto ) >= Estoque.estoqueMinimo";
		}
		sqlStr += " Group by Estoque.produto, Estoque.unidadeEnsino, Estoque.estoqueMinimo, UnidadeEnsino.nome ORDER BY UnidadeEnsino.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<EstoqueVO> consultarPorCodigoProduto(Integer produto, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT Estoque.* FROM Estoque, ProdutoServico WHERE Estoque.produto = ProdutoServico.codigo and ProdutoServico.codigo = " + produto.intValue() + " ORDER BY Estoque.dataEntrada";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr = "SELECT Estoque.* FROM Estoque, ProdutoServico WHERE Estoque.produto = ProdutoServico.codigo and ProdutoServico.codigo = " + produto.intValue() + " and Estoque.unidadeEnsino = " + unidadeEnsino.intValue() + " ORDER BY Estoque.dataEntrada";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);

	}

	public List<EstoqueVO> consultarPorCodigoProdutoComQuantidade(Integer produto, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT Estoque.* FROM Estoque, ProdutoServico WHERE Estoque.produto = ProdutoServico.codigo and ProdutoServico.codigo = " + produto.intValue() + " and Estoque.quantidade > 0.0 ORDER BY Estoque.dataEntrada";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr = "SELECT Estoque.* FROM Estoque, ProdutoServico WHERE Estoque.produto = ProdutoServico.codigo and ProdutoServico.codigo = " + produto.intValue() + " and Estoque.quantidade > 0.0 and Estoque.unidadeEnsino = " + unidadeEnsino.intValue() + " ORDER BY Estoque.dataEntrada";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);

	}

	public EstoqueVO consultarEstoquerPorProdutoPorUnidadeValidandoEstoqueMinino(Integer produto, Integer unidadeEnsino, int nivelMontarDados) throws Exception {
		String sqlStr = "SELECT Estoque.estoqueMinimo, Estoque.estoqueMaximo, sum(Estoque.quantidade) as soma FROM Estoque WHERE Estoque.produto = " + produto.intValue() + " ";
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sqlStr += " and Estoque.unidadeEnsino = " + unidadeEnsino.intValue() + " ";
		}
		sqlStr += " group by Estoque.estoqueMinimo, Estoque.estoqueMaximo ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		EstoqueVO estoque = new EstoqueVO();
		while (tabelaResultado.next()) {
			estoque.setQuantidade(tabelaResultado.getDouble("soma"));
			estoque.setEstoqueMinimo(tabelaResultado.getDouble("estoqueMinimo"));
			estoque.setEstoqueMaximo(tabelaResultado.getDouble("estoqueMaximo"));
		}
		return estoque;
	}

	public List<EstoqueVO> consultarPorNomeUnidadeEnsino(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT Estoque.* FROM Estoque, UnidadeEnsino WHERE Estoque.unidadeEnsino = UnidadeEnsino.codigo and lower (UnidadeEnsino.nome) like('" + valorConsulta.toLowerCase() + "%') ORDER BY UnidadeEnsino.nome";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr = "SELECT Estoque.* FROM Estoque, UnidadeEnsino WHERE Estoque.unidadeEnsino = UnidadeEnsino.codigo and lower (UnidadeEnsino.nome) like('" + valorConsulta.toLowerCase() + "%') and UnidadeEnsino.codigo = " + unidadeEnsino.intValue() + " ORDER BY UnidadeEnsino.nome";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<EstoqueVO> consultarPorNomeProdutoAbaixoEstoqueMinimo(String nomeProduto, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT Estoque.* FROM Estoque, ProdutoServico WHERE Estoque.produto = ProdutoServico.codigo AND lower (ProdutoServico.nome) like ('" + nomeProduto + "%') AND Estoque.quantidade < Estoque.estoqueminimo";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr += " AND unidadeEnsino = " + unidadeEnsino;
		}
		sqlStr += " ORDER BY ProdutoServico.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<EstoqueVO> consultarPorCodigoProdutoAbaixoEstoqueMinimo(Integer codigoProduto, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Estoque  WHERE quantidade < estoqueminimo AND produto >= " + codigoProduto;
		if (unidadeEnsino.intValue() != 0) {
			sqlStr += "  AND unidadeensino = " + unidadeEnsino;
		}

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<EstoqueVO> consultarPorQuantidade(Double valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Estoque WHERE quantidade >= " + valorConsulta.doubleValue() + " ORDER BY quantidade";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr = "SELECT * FROM Estoque WHERE quantidade >= " + valorConsulta.doubleValue() + " and unidadeEnsino = " + unidadeEnsino.intValue() + " ORDER BY quantidade";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public EstoqueVO consultarPorUnidadeEnsinoProduto(Integer unidadeEnsino, Double precoUnitario, Integer produto, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Estoque WHERE produto = " + produto.intValue() + " and unidadeEnsino = " + unidadeEnsino.intValue() + " ";
		if (precoUnitario.doubleValue() > 0) {
			sqlStr += " and precoUnitario = '" + precoUnitario.doubleValue() + "' and dataEntrada = '" + Uteis.getDataJDBC(new Date()) + " 00:00:00'";
		}
		sqlStr += " ORDER BY codigo limit 1";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (!tabelaResultado.next()) {
			return new EstoqueVO();
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<EstoqueVO> consultarPorUnidadeEnsinoProduto(Integer unidadeEnsino, Double precoUnitario, Date data, Integer produto, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Estoque WHERE produto = " + produto.intValue() + " and quantidade > 0 and unidadeEnsino = " + unidadeEnsino.intValue() + " ";
		if (precoUnitario.doubleValue() > 0) {
			sqlStr += " and precoUnitario = '" + precoUnitario.doubleValue() + "' ";
		}
		if (data != null) {
			sqlStr += " and dataEntrada = '" + Uteis.getDataJDBC(data) + " 00:00:00'";
		}
		sqlStr += " ORDER BY dataEntrada ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}
	
	public List<EstoqueVO> consultarPorUnidadeEnsinoProdutoQueNaoTenhaVinculoComOperacaoEstoque(Integer unidadeEnsino, Double precoUnitario, Date data, Integer produto, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Estoque WHERE produto = " + produto.intValue() + " and quantidade > 0 and unidadeEnsino = " + unidadeEnsino.intValue() + " ";
		if (precoUnitario.doubleValue() > 0) {
			sqlStr += " and precoUnitario = '" + precoUnitario.doubleValue() + "' ";
		}
		if (data != null) {
			sqlStr += " and dataEntrada = '" + Uteis.getDataJDBC(data) + " 00:00:00'";
		}
		sqlStr += " and not exists (select distinct estoque from operacaoestoque where operacaoestoque.estoque = estoque.codigo) "; 
		sqlStr += " ORDER BY dataEntrada ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<EstoqueVO> consultarPorCodigo(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Estoque WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr = "SELECT * FROM Estoque WHERE codigo >= " + valorConsulta.intValue() + " and unidadeEnsino = " + unidadeEnsino.intValue() + " ORDER BY codigo";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	private StringBuilder getSQLPadraoConsultaBasicaGroup() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT Estoque.produto as \"Estoque.produto\", Estoque.unidadeEnsino as \"Estoque.unidadeEnsino\", Estoque.estoqueMinimo, Estoque.estoqueMaximo, sum(Estoque.quantidade) as quantidade, unidadeensino.nome as \"unidadeensino.nome\", produtoservico.nome as \"produtoservico.nome\"");
		sql.append(" FROM Estoque ");
		sql.append(" inner join ProdutoServico on ProdutoServico.codigo = estoque.produto ");
		sql.append(" inner join unidadeensino on unidadeensino.codigo = estoque.unidadeensino ");
		sql.append(" inner join CategoriaProduto on categoriaproduto.codigo = ProdutoServico.CategoriaProduto    ");
		return sql;
	}

	public static List<EstoqueVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<EstoqueVO> vetResultado = new ArrayList<>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	public static EstoqueVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		EstoqueVO obj = new EstoqueVO();
		obj.setNovoObj(false);
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setPrecoUnitario(new Double(dadosSQL.getDouble("precoUnitario")));
		obj.setDataEntrada(dadosSQL.getDate("dataEntrada"));
		obj.getUnidadeEnsino().setCodigo(new Integer(dadosSQL.getInt("unidadeEnsino")));
		obj.getProduto().setCodigo(new Integer(dadosSQL.getInt("produto")));
		obj.setQuantidade(new Double(dadosSQL.getDouble("quantidade")));
		obj.setEstoqueMinimo(new Double(dadosSQL.getDouble("estoqueMinimo")));
		obj.setEstoqueMaximo(new Double(dadosSQL.getDouble("estoqueMaximo")));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			return obj;
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSCONSULTA) {
			montarDadosUnidadeEnsino(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			montarDadosProduto(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			return obj;
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		montarDadosUnidadeEnsino(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosProduto(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		return obj;
	}

	public static void montarDadosProduto(EstoqueVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getProduto().getCodigo().intValue() == 0) {
			obj.setProduto(new ProdutoServicoVO());
			return;
		}
		obj.setProduto(getFacadeFactory().getProdutoServicoFacade().consultarPorChavePrimaria(obj.getProduto().getCodigo(), false, nivelMontarDados, usuario));
	}

	public static void montarDadosUnidadeEnsino(EstoqueVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getUnidadeEnsino().getCodigo().intValue() == 0) {
			obj.setUnidadeEnsino(new UnidadeEnsinoVO());
			return;
		}
		obj.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsino().getCodigo(), false, nivelMontarDados, usuario));
	}

	private static Double consultarPorEstoqueMinimoProduto(Integer produto, Integer unidadeEnsino) throws Exception {
		String sqlStr = "SELECT estoqueMinimo FROM Estoque WHERE produto = " + produto.intValue() + " ";
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sqlStr += " and unidadeEnsino = " + unidadeEnsino.intValue() + " ";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (Double) Uteis.getSqlRowSetTotalizador(tabelaResultado, "estoqueMinimo", TipoCampoEnum.DOUBLE);
	}

	private static Double consultarPorEstoqueMaximoProduto(Integer produto, Integer unidadeEnsino) throws Exception {
		String sqlStr = "SELECT estoqueMaximo FROM Estoque WHERE produto = " + produto.intValue() + " ";
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sqlStr += " and unidadeEnsino = " + unidadeEnsino.intValue() + " ";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (Double) Uteis.getSqlRowSetTotalizador(tabelaResultado, "estoqueMaximo", TipoCampoEnum.DOUBLE);
	}

	public EstoqueVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sql = "SELECT * FROM Estoque WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	public static String getIdEntidade() {
		return Estoque.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		Estoque.idEntidade = idEntidade;
	}

	private void obterQuantidadeProdutosAbaixoMinimo(EstatisticaEstoqueVO estatisticas, UsuarioVO usuario) throws Exception {
		estatisticas.setQuantidadeProdutosAbaixoMinimo(0);
		estatisticas.setNecessidadeCompraSuprirEstoqueMinimo(0);
		try {
 			ControleAcesso.consultar(getIdEntidade(), false, usuario);
			String sql = "SELECT COUNT(*) as nrProdutos FROM (SELECT DISTINCT produto from Estoque " + "WHERE (quantidade < estoqueminimo) " + "GROUP BY produto) AS TOTAL; ";
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
			if (tabelaResultado.next()) {
				estatisticas.setQuantidadeProdutosAbaixoMinimo(tabelaResultado.getInt("nrProdutos"));
			}
			sql = "SELECT sum(estoqueminimo - quantidade)::int as necessidadeCompraSuprirEstoqueMinimo from Estoque WHERE (quantidade < estoqueminimo) ORDER BY necessidadeCompraSuprirEstoqueMinimo desc";
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
			if (tabelaResultado.next()) {
				estatisticas.setNecessidadeCompraSuprirEstoqueMinimo(tabelaResultado.getInt("necessidadeCompraSuprirEstoqueMinimo"));
			}
		} catch (Exception e) {
			throw e;
		}
	}

	private void obterQuantidadeCategoriaDespesasAbaixoMinimo(EstatisticaEstoqueVO estatisticas, Integer nrItensApresentar, UsuarioVO usuario) throws Exception {
		estatisticas.setResumoEstatisticaEstoqueCategoriaProdutoVO(new ArrayList<EstatisticaEstoqueCategoriaProdutoVO>(0));
		try {
			ControleAcesso.consultar(getIdEntidade(), false, usuario);
			StringBuilder sql = new StringBuilder()
					.append(" select unidadeensino, unidadeensinoCodigo, categoriaCodigo, categoriaproduto, sum(necessidadeCompraSuprirEstoqueMinimo)::int as necessidadeCompraSuprirEstoqueMinimo, ")
					.append(" count(produto) as qtdeProduto from ( select produto, categoriaproduto.codigo as categoriaCodigo, categoriaproduto.nome as categoriaproduto, ")
					.append(" unidadeensino.codigo as unidadeensinoCodigo, unidadeensino.nome as unidadeensino, estoqueminimo - sum(quantidade) as necessidadeCompraSuprirEstoqueMinimo from estoque ")
					.append(" inner join produtoservico on produtoservico.codigo = estoque.produto ")
					.append(" inner join unidadeensino on unidadeensino.codigo = estoque.unidadeensino ")
					.append(" inner join categoriaproduto on produtoservico.categoriaproduto = categoriaproduto.codigo ")
					.append(" where estoqueminimo > 0 and produtoservico.controlarEstoque ")
					.append(" group by produto, unidadeensino.nome, unidadeensino.codigo, categoriaproduto.codigo, categoriaproduto.nome, estoqueminimo ")
					.append(" having estoqueminimo - sum(quantidade) > 0 ")
					.append(" ) as t group by unidadeensino, unidadeensinoCodigo, categoriaCodigo, categoriaproduto order by categoriaproduto, unidadeensino ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
			while (tabelaResultado.next()) {
				EstatisticaEstoqueCategoriaProdutoVO categoriaEstatistica = new EstatisticaEstoqueCategoriaProdutoVO();
				categoriaEstatistica.setCodigoCategoriaProduto(tabelaResultado.getInt("categoriaCodigo"));
				categoriaEstatistica.setNomeCategoriaProduto(tabelaResultado.getString("categoriaproduto"));
				categoriaEstatistica.setCodigoUnidadeEnsino(tabelaResultado.getInt("unidadeensinoCodigo"));
				categoriaEstatistica.setNomeUnidadeEnsino(tabelaResultado.getString("unidadeensino"));
				categoriaEstatistica.setQuantidadeUnitariaProdutoAbaixoMinimo(tabelaResultado.getInt("necessidadeCompraSuprirEstoqueMinimo"));
				categoriaEstatistica.setQuantidadeProduto(tabelaResultado.getInt("qtdeProduto"));
				estatisticas.getResumoEstatisticaEstoqueCategoriaProdutoVO().add(categoriaEstatistica);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public EstatisticaEstoqueVO consultarEstatisticaEstoqueAtualizada(UsuarioVO usuario) throws Exception {
		EstatisticaEstoqueVO estatisticas = new EstatisticaEstoqueVO();
		obterQuantidadeProdutosAbaixoMinimo(estatisticas, usuario);
		obterQuantidadeCategoriaDespesasAbaixoMinimo(estatisticas, 2, usuario);
		return estatisticas; 
	}

	public List<ItemCotacaoUnidadeEnsinoVO> consultarQtdeMinimaPorUnidadeEnsinoProduto(List<ItemCotacaoUnidadeEnsinoVO> listaItemCotacaoUnidadeEnsinoVO, Integer produto, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT produto, unidadeensino, estoqueminimo FROM estoque WHERE 1=1 ");
		if (produto != null && !produto.equals(0)) {
			sqlStr.append("AND produto = ").append(produto).append(" ");
		}
		if (!listaItemCotacaoUnidadeEnsinoVO.isEmpty()) {
			sqlStr.append("AND unidadeEnsino in( ");
			for (ItemCotacaoUnidadeEnsinoVO obj : listaItemCotacaoUnidadeEnsinoVO) {
				sqlStr.append(obj.getUnidadeEnsinoVO().getCodigo());
				if (obj.equals(listaItemCotacaoUnidadeEnsinoVO.get(listaItemCotacaoUnidadeEnsinoVO.size() - 1))) {
					sqlStr.append(") ");
				} else {
					sqlStr.append(", ");
				}
			}
		}
		sqlStr.append("GROUP BY produto, unidadeensino, estoqueminimo ");
		sqlStr.append("ORDER BY unidadeEnsino");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsultaEstoqueMinimo(listaItemCotacaoUnidadeEnsinoVO, tabelaResultado, usuario));
	}

	public static List<ItemCotacaoUnidadeEnsinoVO> montarDadosConsultaEstoqueMinimo(List<ItemCotacaoUnidadeEnsinoVO> listaItemCotacaoUnidadeEnsinoVO, SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
		EstoqueVO estoque = new EstoqueVO();
		while (tabelaResultado.next()) {
			estoque.getProduto().setCodigo(tabelaResultado.getInt("produto"));
			estoque.getUnidadeEnsino().setCodigo(tabelaResultado.getInt("unidadeEnsino"));
			estoque.setEstoqueMinimo(tabelaResultado.getDouble("estoqueMinimo"));
			preencherEstoqueMinimoItemCotacaoUnidadeEnsinoVO(estoque, listaItemCotacaoUnidadeEnsinoVO);
		}
		return listaItemCotacaoUnidadeEnsinoVO;
	}

	public static void preencherEstoqueMinimoItemCotacaoUnidadeEnsinoVO(EstoqueVO estoqueVO, List<ItemCotacaoUnidadeEnsinoVO> listaItemCotacaoUnidadeEnsinoVO) throws Exception {
		for (ItemCotacaoUnidadeEnsinoVO obj : listaItemCotacaoUnidadeEnsinoVO) {
			if (obj.getProdutoVO().getCodigo().equals(estoqueVO.getProduto().getCodigo()) && obj.getUnidadeEnsinoVO().getCodigo().equals(estoqueVO.getUnidadeEnsino().getCodigo())) {
				obj.setQtdMinimaUnidade(estoqueVO.getEstoqueMinimo());
				// obj.setQtdRequisicao(getFacadeFactory().getRequisicaoItemFacade().consultarQtdeFaltaEntregarPorProdutoUnidadeEnsinoSituacaoAutorizadaSituacaoEntregaDiferenteDeFinalizada(obj.getProdutoVO().getCodigo(), obj.getUnidadeEnsinoVO().getCodigo()));
			}
		}
	}

}
