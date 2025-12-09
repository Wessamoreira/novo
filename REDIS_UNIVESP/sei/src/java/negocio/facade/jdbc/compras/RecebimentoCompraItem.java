package negocio.facade.jdbc.compras;

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
import negocio.comuns.compras.CompraItemVO;
import negocio.comuns.compras.RecebimentoCompraItemVO;
import negocio.comuns.compras.RecebimentoCompraVO;
import negocio.comuns.compras.enumeradores.OperacaoEstoqueEnum;
import negocio.comuns.compras.enumeradores.TipoOperacaoEstoqueOrigemEnum;
import negocio.comuns.compras.enumeradores.TipoProdutoServicoEnum;
import negocio.comuns.faturamento.nfe.NotaFiscalEntradaRecebimentoCompraVO;
import negocio.comuns.financeiro.enumerador.TipoNivelCentroResultadoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.compras.RecebimentoCompraItemInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class RecebimentoCompraItem extends ControleAcesso implements RecebimentoCompraItemInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 240154684301864799L;
	protected static String idEntidade;

	public RecebimentoCompraItem() throws Exception {
		super();
		setIdEntidade("RecebimentoCompra");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(List<RecebimentoCompraItemVO> lista, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		for (RecebimentoCompraItemVO obj : lista) {
			persistir(obj, verificarAcesso, usuarioVO);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(RecebimentoCompraItemVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		if (obj.getCodigo() == 0) {
			incluir(obj, usuarioVO);
		} else {
			alterar(obj, usuarioVO);
		}
		atualizarCompraItem(obj, obj.getRecebimentoCompraVO().getUnidadeEnsino().getCodigo(), OperacaoEstoqueEnum.INCLUIR, usuarioVO);
	}

	

	private void incluir(final RecebimentoCompraItemVO obj, UsuarioVO usuario) throws Exception {
		obj.realizarUpperCaseDados();
		final String sql = "INSERT INTO RecebimentoCompraItem( compraItem, recebimentoCompra, quantidadeRecebida, valorUnitario, valorTotal ) VALUES ( ?, ?, ?, ?, ? ) returning codigo";
		obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlInserir = arg0.prepareStatement(sql);
				if (obj.getCompraItem().getCodigo().intValue() != 0) {
					sqlInserir.setInt(1, obj.getCompraItem().getCodigo().intValue());
				} else {
					sqlInserir.setNull(1, 0);
				}
				if (obj.getRecebimentoCompraVO().getCodigo().intValue() != 0) {
					sqlInserir.setInt(2, obj.getRecebimentoCompraVO().getCodigo().intValue());
				} else {
					sqlInserir.setNull(2, 0);
				}
				sqlInserir.setDouble(3, obj.getQuantidadeRecebida().doubleValue());
				sqlInserir.setDouble(4, obj.getValorUnitario().doubleValue());
				sqlInserir.setDouble(5, obj.getValorTotal().doubleValue());
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
	}

	private void alterar(final RecebimentoCompraItemVO obj, UsuarioVO usuario) throws Exception {
		RecebimentoCompraItemVO.validarDados(obj);
		obj.realizarUpperCaseDados();
		final String sql = "UPDATE RecebimentoCompraItem set compraItem=?, recebimentoCompra=?, quantidadeRecebida=?, valorTotal=?, valorUnitario=? WHERE ((codigo = ?))";
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				sqlAlterar.setInt(1, obj.getCompraItem().getCodigo().intValue());
				if (obj.getRecebimentoCompraVO().getCodigo().intValue() != 0) {
					sqlAlterar.setInt(2, obj.getRecebimentoCompraVO().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(2, 0);
				}
				sqlAlterar.setDouble(3, obj.getQuantidadeRecebida().doubleValue());
				sqlAlterar.setDouble(4, obj.getValorTotal().doubleValue());
				sqlAlterar.setDouble(5, obj.getValorUnitario().doubleValue());
				sqlAlterar.setInt(6, obj.getCodigo().intValue());
				return sqlAlterar;
			}
		});
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void atualizarCompraItem(RecebimentoCompraItemVO obj, Integer unidadeEnsino, OperacaoEstoqueEnum operacaoEstoque, UsuarioVO usuario) throws Exception {
		if (obj.getRecebimentoCompraVO().getRecebimentoFinalizado()) {
			if (operacaoEstoque.isExcluir()) {
				obj.getCompraItem().setQuantidadeRecebida(obj.getCompraItem().getQuantidadeRecebida() - obj.getQuantidadeRecebida());
			} else if (operacaoEstoque.isIncluir()) {
				obj.getCompraItem().setQuantidadeRecebida(obj.getCompraItem().getQuantidadeRecebida() + obj.getQuantidadeRecebida());
			}
			getFacadeFactory().getCompraItemFacade().atualizarCampoQuantidadeRecebida(obj.getCompraItem(), usuario);
			if (obj.getCompraItem().getProduto().getTipoProdutoServicoEnum().isProduto() && obj.getCompraItem().getProduto().getControlarEstoque()) {
				Estoque.manipularEstoque(obj.getCodigo().toString(), TipoOperacaoEstoqueOrigemEnum.RECEBIMENTO_COMPRA_ITEM, obj.getCompraItem().getProduto().getCodigo(), obj.getQuantidadeRecebida(), obj.getValorUnitario(), null, unidadeEnsino, operacaoEstoque, usuario);
			}
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void atualizarCampoQuantidadeRecebidaValorTotal(RecebimentoCompraItemVO obj, UsuarioVO usuario) {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" UPDATE RecebimentoCompraItem SET quantidadeRecebida='").append(obj.getQuantidadeRecebida()).append("', valorTotal= ").append(obj.getValorTotal()).append(" ");
		sqlStr.append(" WHERE codigo = ").append(obj.getCodigo()).append("");
		sqlStr.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		getConexao().getJdbcTemplate().update(sqlStr.toString());
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe <code>RecebimentoCompraItemVO</code>. Sempre localiza o registro a ser excluído através da chave primária da entidade. Primeiramente verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>excluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>RecebimentoCompraItemVO</code> que será removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(RecebimentoCompraItemVO obj) throws Exception {
		String sql = "DELETE FROM RecebimentoCompraItem WHERE ((codigo = ?))";
		getConexao().getJdbcTemplate().update(sql, obj.getCodigo());
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirRecebimentoCompraItem(String listaCodigo, UsuarioVO usuario) throws Exception {
		if (!listaCodigo.isEmpty()) {
			String sql = "DELETE FROM RecebimentoCompraItem WHERE codigo in (" + listaCodigo + ")" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql);
		}
	}

	/**
	 * Responsável por realizar uma consulta de <code>RecebimentoCompraItem</code> através do valor do atributo <code>nome</code> da classe <code>EnderecoEstoque</code> Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe <code>RecebimentoCompraItemVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@SuppressWarnings("static-access")
	public List<RecebimentoCompraItemVO> consultarPorNomeEnderecoEstoque(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true, usuario);
		String sqlStr = "SELECT RecebimentoCompraItem.* FROM RecebimentoCompraItem, EnderecoEstoque WHERE RecebimentoCompraItem.enderecoEstoque = EnderecoEstoque.codigo and upper( EnderecoEstoque.nome ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY EnderecoEstoque.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/**
	 * Responsável por realizar uma consulta de <code>RecebimentoCompraItem</code> através do valor do atributo <code>abreviatura</code> da classe <code>Produto</code> Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe <code>RecebimentoCompraItemVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@SuppressWarnings("static-access")
	public List<RecebimentoCompraItemVO> consultarPorAbreviaturaProduto(Integer valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true, usuario);
		String sqlStr = "SELECT RecebimentoCompraItem.* FROM RecebimentoCompraItem, CompraItem WHERE RecebimentoCompraItem.compraItem = CompraItem.codigo and CompraItem.produto = " + valorConsulta.intValue() + " ORDER BY CompraItem.codigo";

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/**
	 * Responsável por realizar uma consulta de <code>RecebimentoCompraItem</code> através do valor do atributo <code>abreviatura</code> da classe <code>ProdutoConsumo</code> Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe <code>RecebimentoCompraItemVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@SuppressWarnings("static-access")
	public List<RecebimentoCompraItemVO> consultarPorAbreviaturaProdutoConsumo(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true, usuario);
		String sqlStr = "SELECT RecebimentoCompraItem.* FROM RecebimentoCompraItem, CompraItem, ProdutoConsumo WHERE RecebimentoCompraItem.compraItem = CompraItem.codigo and CompraItem.produtoConsumo = ProdutoConsumo.codigo and upper( ProdutoConsumo.abreviatura ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY ProdutoConsumo.abreviatura";

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	@SuppressWarnings("static-access")
	public List<RecebimentoCompraItemVO> consultarPorCodigoCompraItem(Integer valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true, usuario);
		String sqlStr = "SELECT RecebimentoCompraItem.* FROM RecebimentoCompraItem, CompraItem WHERE RecebimentoCompraItem.compraItem = CompraItem.codigo and CompraItem.codigo >= " + valorConsulta.intValue() + " ORDER BY CompraItem.codigo";

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/**
	 * Responsável por realizar uma consulta de <code>RecebimentoCompraItem</code> através do valor do atributo <code>Integer compraItem</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>RecebimentoCompraItemVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@SuppressWarnings("static-access")
	public List<RecebimentoCompraItemVO> consultarPorCompraItem(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM RecebimentoCompraItem WHERE compraItem >= " + valorConsulta.intValue() + " ORDER BY compraItem";

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>RecebimentoCompraItem</code> através do valor do atributo <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>RecebimentoCompraItemVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@SuppressWarnings("static-access")
	public List<RecebimentoCompraItemVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM RecebimentoCompraItem WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe <code>RecebimentoCompraItemVO</code> resultantes da consulta.
	 */
	public static List<RecebimentoCompraItemVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<RecebimentoCompraItemVO> vetResultado = new ArrayList<RecebimentoCompraItemVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um objeto da classe <code>RecebimentoCompraItemVO</code>.
	 * 
	 * @return O objeto da classe <code>RecebimentoCompraItemVO</code> com os dados devidamente montados.
	 */
	public static RecebimentoCompraItemVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		RecebimentoCompraItemVO obj = new RecebimentoCompraItemVO();
		obj.setCodigo((dadosSQL.getInt("codigo")));
		obj.getCompraItem().setCodigo((dadosSQL.getInt("compraItem")));
		obj.getRecebimentoCompraVO().setCodigo(dadosSQL.getInt("recebimentoCompra"));
		obj.setQuantidadeRecebida((dadosSQL.getDouble("quantidadeRecebida")));
		obj.setQuantidadeRecebidaAntesAlteracao((dadosSQL.getDouble("quantidadeRecebida")));
		obj.setValorTotal((dadosSQL.getDouble("valorTotal")));
		obj.setValorUnitario((dadosSQL.getDouble("valorUnitario")));
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		montarDadosCompraItem(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		return obj;
	}

	public static void montarDadosCompraItem(RecebimentoCompraItemVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getCompraItem().getCodigo().intValue() == 0) {
			obj.setCompraItem(new CompraItemVO());
			return;
		}
		obj.setCompraItem(getFacadeFactory().getCompraItemFacade().consultarPorChavePrimaria(obj.getCompraItem().getCodigo(), nivelMontarDados, usuario));
	}

	@Override
	public List<RecebimentoCompraItemVO> consultarRecebimentoCompraItems(RecebimentoCompraVO recebimentoCompra, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		RecebimentoCompraItem.consultar(getIdEntidade());
		String sql = "SELECT * FROM RecebimentoCompraItem WHERE recebimentoCompra = ?";
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, recebimentoCompra.getCodigo());
		List<RecebimentoCompraItemVO> objetos = new ArrayList<>(0);
		while (resultado.next()) {
			RecebimentoCompraItemVO novoObj = montarDados(resultado, nivelMontarDados, usuario);
			novoObj.setRecebimentoCompraVO(recebimentoCompra);
			objetos.add(novoObj);
		}
		objetos.sort((rc1, rc2) -> rc1.getCompraItem().getProduto().getNome().compareTo(rc2.getCompraItem().getProduto().getNome()));
		return objetos;
	}

	/**
	 * Operação responsável por localizar um objeto da classe <code>RecebimentoCompraItemVO</code> através de sua chave primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto procurado.
	 */
	public RecebimentoCompraItemVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM RecebimentoCompraItem WHERE codigo = ?";

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, codigoPrm);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( RecebimentoCompraItem ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public Double consultarQuantidadeRecebibaPorCompraItemComCodigoDiferenteRecebimentoCompraItem(Integer compraItem, Integer codigoRecebimentoCompraItem, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT sum (quantidadeRecebida) as quantidadeRecebida from RecebimentoCompraItem ");
		sql.append(" where compraitem =  ").append(compraItem);
		sql.append(" and codigo !=  ").append(codigoRecebimentoCompraItem);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return tabelaResultado.next() ? tabelaResultado.getDouble("quantidadeRecebida") : 0.0;
	}

	private StringBuilder getSQLPadraoConsultaRecebimentoCompraItem() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT  ");
		sql.append(" recebimentocompraitem.codigo as \"recebimentocompraitem.codigo\", recebimentocompraitem.quantidaderecebida as \"recebimentocompraitem.quantidaderecebida\", ");
		sql.append(" recebimentocompraitem.valortotal as \"recebimentocompraitem.valortotal\", ");
		sql.append(" recebimentocompraitem.valorunitario as \"recebimentocompraitem.valorunitario\", ");

		sql.append(" compraitem.codigo as \"compraitem.codigo\", compraitem.quantidaderecebida as \"compraitem.quantidaderecebida\", ");
		sql.append(" compraitem.precounitario as \"compraitem.precounitario\", compraitem.quantidade as \"compraitem.quantidade\", ");
		sql.append(" compraitem.quantidaderequisicao as \"compraitem.quantidaderequisicao\", compraitem.quantidadeadicional as \"compraitem.quantidadeadicional\", ");
		sql.append(" compraitem.tipoNivelCentroResultadoEnum as \"compraitem.tipoNivelCentroResultadoEnum\",  ");

		sql.append(" compra.codigo as \"compra.codigo\", ");
		sql.append(" unidadeensino.codigo as \"unidadeensino.codigo\", unidadeensino.nome as \"unidadeensino.nome\",  ");

		sql.append(" produtoservico.codigo as \"produtoservico.codigo\", produtoservico.nome as \"produtoservico.nome\",  ");
		sql.append(" produtoservico.controlarEstoque as \"produtoservico.controlarEstoque\",  ");
		sql.append(" produtoservico.tipoProdutoServico as \"produtoservico.tipoProdutoServico\",  ");

		sql.append(" categoriaproduto.codigo as \"categoriaproduto.codigo\", categoriaproduto.nome as \"categoriaproduto.nome\",  ");

		sql.append(" departamento.codigo as \"departamento.codigo\", departamento.nome as \"departamento.nome\",   ");

		sql.append(" categoriaDespesa.codigo as \"categoriaDespesa.codigo\", categoriaDespesa.descricao as \"categoriaDespesa.descricao\",   ");

		sql.append(" curso.codigo as \"curso.codigo\", curso.nome as \"curso.nome\",  ");

		sql.append(" turno.codigo as \"turno.codigo\", turno.nome as \"turno.nome\",  ");

		sql.append(" turma.codigo as \"turma.codigo\", turma.identificadorturma as \"turma.identificadorturma\",  ");

		sql.append(" crad.codigo as \"crad.codigo\", crad.descricao as \"crad.descricao\", crad.identificadorCentroResultado as \"crad.identificadorCentroResultado\"  ");
		
		sql.append(" FROM recebimentocompraitem ");
		sql.append(" inner join compraitem on compraitem.codigo = recebimentocompraitem.compraitem ");
		sql.append(" inner join compra on compra.codigo = compraitem.compra ");
		sql.append(" inner join unidadeensino on unidadeensino.codigo = compra.unidadeensino ");
		sql.append(" inner join produtoservico as produtoservico on produtoservico.codigo = compraitem.produto");
		sql.append(" inner join categoriaproduto on categoriaproduto.codigo = produtoservico.categoriaproduto");
		sql.append(" LEFT JOIN categoriaDespesa ON compraitem.categoriaDespesa = categoriaDespesa.codigo ");
		sql.append(" LEFT JOIN departamento ON compraitem.departamento = departamento.codigo ");
		sql.append(" LEFT JOIN curso on curso.codigo = compraitem.curso");
		sql.append(" LEFT JOIN turno on turno.codigo = compraitem.turno");
		sql.append(" LEFT JOIN turma on turma.codigo = compraitem.turma");
		sql.append(" LEFT JOIN centroresultado AS crad ON compraitem.centroresultadoadministrativo = crad.codigo ");

		return sql;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<RecebimentoCompraItemVO> consultaRapidaPorNotaFiscalEntradaRecebimentoCompraVO(NotaFiscalEntradaRecebimentoCompraVO obj, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = getSQLPadraoConsultaRecebimentoCompraItem();
		sqlStr.append(" where recebimentocompraitem.recebimentoCompra = ").append(obj.getRecebimentoCompraVO().getCodigo());
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<RecebimentoCompraItemVO> vetResultado = new ArrayList<>(0);
		while (resultado.next()) {
			RecebimentoCompraItemVO rci = new RecebimentoCompraItemVO();
			montarDadosRapidos(rci, resultado, usuario);
			rci.setRecebimentoCompraVO(obj.getRecebimentoCompraVO());
			vetResultado.add(rci);
		}
		return vetResultado;
	}

	private void montarDadosRapidos(RecebimentoCompraItemVO rci, SqlRowSet rs, UsuarioVO usuario) throws Exception {
		rci.setCodigo(rs.getInt("recebimentocompraitem.codigo"));
		rci.setQuantidadeRecebida((rs.getDouble("recebimentocompraitem.quantidadeRecebida")));
		rci.setQuantidadeRecebidaAntesAlteracao((rs.getDouble("recebimentocompraitem.quantidadeRecebida")));
		rci.setValorTotal(rs.getDouble("recebimentocompraitem.valorTotal"));
		rci.setValorUnitario(rs.getDouble("recebimentocompraitem.valorUnitario"));

		rci.getCompraItem().setCodigo(rs.getInt("compraItem.codigo"));
		rci.getCompraItem().setQuantidadeRequisicao((rs.getDouble("compraitem.quantidaderequisicao")));
		rci.getCompraItem().setQuantidadeAdicional((rs.getDouble("compraitem.quantidadeadicional")));
		rci.getCompraItem().setQuantidadeRecebida(rs.getDouble("compraItem.quantidadeRecebida"));
		rci.getCompraItem().setPrecoUnitario(rs.getDouble("compraItem.precoUnitario"));
		if(Uteis.isAtributoPreenchido(rs.getString("compraitem.tipoNivelCentroResultadoEnum"))){
			rci.getCompraItem().setTipoNivelCentroResultadoEnum(TipoNivelCentroResultadoEnum.valueOf(rs.getString("compraitem.tipoNivelCentroResultadoEnum")));	
		}
		rci.getCompraItem().getCompra().setCodigo(rs.getInt("compra.codigo"));
		rci.getCompraItem().getCompra().getUnidadeEnsino().setCodigo(rs.getInt("unidadeensino.codigo"));
		rci.getCompraItem().getCompra().getUnidadeEnsino().setNome(rs.getString("unidadeensino.nome"));

		rci.getCompraItem().getProduto().setCodigo(rs.getInt("produtoservico.codigo"));
		rci.getCompraItem().getProduto().setNome(rs.getString("produtoservico.nome"));
		rci.getCompraItem().getProduto().setTipoProdutoServicoEnum(TipoProdutoServicoEnum.valueOf(rs.getString("produtoservico.tipoProdutoServico")));
		rci.getCompraItem().getProduto().setControlarEstoque(rs.getBoolean("produtoservico.controlarEstoque"));
		rci.getCompraItem().getProduto().getCategoriaProduto().setCodigo(rs.getInt("categoriaproduto.codigo"));
		rci.getCompraItem().getProduto().getCategoriaProduto().setNome(rs.getString("categoriaproduto.nome"));

		rci.getCompraItem().getCategoriaDespesa().setCodigo((rs.getInt("categoriaDespesa.codigo")));
		rci.getCompraItem().getCategoriaDespesa().setDescricao((rs.getString("categoriaDespesa.descricao")));

		rci.getCompraItem().getDepartamentoVO().setCodigo((rs.getInt("departamento.codigo")));
		rci.getCompraItem().getDepartamentoVO().setNome((rs.getString("departamento.nome")));

		rci.getCompraItem().getCursoVO().setCodigo((rs.getInt("curso.codigo")));
		rci.getCompraItem().getCursoVO().setNome((rs.getString("curso.nome")));

		rci.getCompraItem().getTurnoVO().setCodigo((rs.getInt("turno.codigo")));
		rci.getCompraItem().getTurnoVO().setNome((rs.getString("turno.nome")));

		rci.getCompraItem().getTurma().setCodigo((rs.getInt("turma.codigo")));
		rci.getCompraItem().getTurma().setIdentificadorTurma((rs.getString("turma.identificadorturma")));

		rci.getCompraItem().getCentroResultadoAdministrativo().setCodigo((rs.getInt("crad.codigo")));
		rci.getCompraItem().getCentroResultadoAdministrativo().setDescricao((rs.getString("crad.descricao")));
		rci.getCompraItem().getCentroResultadoAdministrativo().setIdentificadorCentroResultado((rs.getString("crad.identificadorCentroResultado")));
		
		if (Uteis.isAtributoPreenchido(rci.getCompraItem().getQuantidadeRequisicao())) {
			rci.getCompraItem().setListaRequisicaoItem(getFacadeFactory().getRequisicaoItemFacade().consultarRequisicaoItemsPorCompraItem(rci.getCompraItem(), Uteis.NIVELMONTARDADOS_TODOS, usuario));
		}
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
	 */
	public static String getIdEntidade() {
		return RecebimentoCompraItem.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		RecebimentoCompraItem.idEntidade = idEntidade;
	}
}