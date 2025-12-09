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

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.CompraItemVO;
import negocio.comuns.compras.CompraVO;
import negocio.comuns.compras.ProdutoServicoVO;
import negocio.comuns.compras.enumeradores.TipoProdutoServicoEnum;
import negocio.comuns.financeiro.CategoriaDespesaVO;
import negocio.comuns.financeiro.CentroResultadoVO;
import negocio.comuns.financeiro.enumerador.TipoNivelCentroResultadoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.compras.CompraItemInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class CompraItem extends ControleAcesso implements CompraItemInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3356117955940074859L;
	protected static String idEntidade;

	public CompraItem() {
		super();
		setIdEntidade("Compra");
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(List<CompraItemVO> lista, UsuarioVO usuarioVO) throws Exception {
		for (CompraItemVO obj : lista) {
			persistir(obj, usuarioVO);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(CompraItemVO obj, UsuarioVO usuarioVO) throws Exception {
		CompraItemVO.validarDados(obj);
		if (obj.getCodigo() == 0) {
			incluir(obj, usuarioVO);
		} else {
			alterar(obj, usuarioVO);
		}
		getFacadeFactory().getRequisicaoItemFacade().atualizarRequisitoItemPorCompraItem(obj, usuarioVO);

	}

	private void incluir(final CompraItemVO obj, UsuarioVO usuario) throws Exception {

		StringBuilder sql = new StringBuilder(" INSERT INTO CompraItem( compra, produto, quantidade, precoUnitario, quantidadeRecebida, ");
		sql.append(" quantidadeRequisicao, quantidadeAdicional, categoriaDespesa, departamento, curso, turno, turma, ");
		sql.append(" centroResultadoAdministrativo, tipoNivelCentroResultadoEnum  ");
		sql.append(" ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?  ) returning codigo ");
		sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
				final PreparedStatement sqlInserir = con.prepareStatement(sql.toString());
				int i = 0;
				Uteis.setValuePreparedStatement(obj.getCompra(), ++i, sqlInserir);
				Uteis.setValuePreparedStatement(obj.getProduto(), ++i, sqlInserir);
				Uteis.setValuePreparedStatement(obj.getQuantidade(), ++i, sqlInserir);
				Uteis.setValuePreparedStatement(obj.getPrecoUnitario(), ++i, sqlInserir);
				Uteis.setValuePreparedStatement(obj.getQuantidadeRecebida(), ++i, sqlInserir);
				Uteis.setValuePreparedStatement(obj.getQuantidadeRequisicao(), ++i, sqlInserir);
				Uteis.setValuePreparedStatement(obj.getQuantidadeAdicional(), ++i, sqlInserir);
				Uteis.setValuePreparedStatement(obj.getCategoriaDespesa(), ++i, sqlInserir);
				Uteis.setValuePreparedStatement(obj.getDepartamentoVO(), ++i, sqlInserir);
				Uteis.setValuePreparedStatement(obj.getCursoVO(), ++i, sqlInserir);
				Uteis.setValuePreparedStatement(obj.getTurnoVO(), ++i, sqlInserir);
				Uteis.setValuePreparedStatement(obj.getTurma(), ++i, sqlInserir);
				Uteis.setValuePreparedStatement(obj.getCentroResultadoAdministrativo(), ++i, sqlInserir);
				Uteis.setValuePreparedStatement(obj.getTipoNivelCentroResultadoEnum(), ++i, sqlInserir);
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
		getFacadeFactory().getProdutoServicoFacade().atualizarValorUltimaCompraProdutoServico(obj.getPrecoUnitario(), obj.getProduto().getCodigo());
		obj.setNovoObj(Boolean.FALSE);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final CompraItemVO obj, UsuarioVO usuario) throws Exception {
		CompraItemVO.validarDados(obj);
		StringBuilder sql = new StringBuilder("UPDATE CompraItem set compra=?, produto=?, quantidade=?, precoUnitario=?, quantidadeRecebida=?, ");
		sql.append(" quantidadeRequisicao=?, quantidadeAdicional=?, categoriaDespesa=?, departamento=?, curso=?, turno=?, turma=?, ");
		sql.append(" centroResultadoAdministrativo=?, tipoNivelCentroResultadoEnum=?  ");
		sql.append(" WHERE codigo = ? ");
		sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement sqlAlterar = con.prepareStatement(sql.toString());
				int i = 0;
				Uteis.setValuePreparedStatement(obj.getCompra(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getProduto(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getQuantidade(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getPrecoUnitario(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getQuantidadeRecebida(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getQuantidadeRequisicao(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getQuantidadeAdicional(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getCategoriaDespesa(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getDepartamentoVO(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getCursoVO(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getTurnoVO(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getTurma(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getCentroResultadoAdministrativo(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getTipoNivelCentroResultadoEnum(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);
				return sqlAlterar;
			}
		}) == 0) {
			incluir(obj, usuario);
		}
		getFacadeFactory().getProdutoServicoFacade().atualizarValorUltimaCompraProdutoServico(obj.getPrecoUnitario(), obj.getProduto().getCodigo());

	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void atualizarCampoQuantidadeRecebida(CompraItemVO obj, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder("UPDATE CompraItem set quantidadeRecebida = ? ");
		sql.append("where codigo = ? ");
		sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		getConexao().getJdbcTemplate().update(sql.toString(), obj.getQuantidadeRecebida(), obj.getCodigo());
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(CompraItemVO obj) throws Exception {
		CompraItem.excluir(getIdEntidade());
		String sql = "DELETE FROM CompraItem WHERE ((codigo = ?))";
		getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
	}

	public List consultarPorNomeProduto(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true, usuario);
		String sqlStr = "SELECT CompraItem.* FROM CompraItem, Produto WHERE CompraItem.produto = Produto.codigo and upper( Produto.nome ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY Produto.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List consultarPorCodigoCompra(Integer valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true, usuario);
		String sqlStr = "SELECT CompraItem.* FROM CompraItem, Compra WHERE CompraItem.compra = Compra.codigo and Compra.codigo >= " + valorConsulta.intValue() + " ORDER BY Compra.codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM CompraItem WHERE codigo = " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}
	
	private StringBuilder getSQLPadraoConsultaBasica() {
		StringBuilder sql = new StringBuilder(" ");
		sql.append(" SELECT ");
		sql.append(" compraitem.codigo as \"compraitem.codigo\", compraitem.quantidaderequisicao as \"compraitem.quantidaderequisicao\",   ");
		sql.append(" compraitem.quantidadeadicional as \"compraitem.quantidadeadicional\", compraitem.quantidadeRecebida as \"compraitem.quantidadeRecebida\",   ");
		sql.append(" compraitem.precoUnitario as \"compraitem.precoUnitario\", ");
		sql.append(" compraitem.tipoNivelCentroResultadoEnum as \"compraitem.tipoNivelCentroResultadoEnum\", ");
		
		sql.append(" produtoServico.codigo as \"produtoServico.codigo\", produtoServico.nome as \"produtoServico.nome\",  ");
		sql.append(" produtoServico.tipoProdutoServico as \"produtoServico.tipoProdutoServico\", produtoServico.descricao as \"produtoServico.descricao\",  ");
		sql.append(" produtoServico.unidadeMedida as \"produtoServico.unidadeMedida\", produtoServico.controlarEstoque as \"produtoServico.controlarEstoque\",  ");
		sql.append(" produtoServico.exigeCompraCotacao as \"produtoServico.exigeCompraCotacao\", produtoServico.valorUnitario as \"produtoServico.valorUnitario\",  ");
		sql.append(" produtoServico.valorUltimaCompra as \"produtoServico.valorUltimaCompra\", produtoServico.justificativaRequisicaoObrigatoria as \"produtoServico.justificativaRequisicaoObrigatoria\",  ");
		
		sql.append(" categoriaproduto.codigo as \"categoriaproduto.codigo\", categoriaproduto.nome as \"categoriaproduto.nome\", ");
		
		sql.append(" departamento.codigo as \"departamento.codigo\", departamento.nome as \"departamento.nome\",   ");
		
		sql.append(" categoriaDespesa.codigo as \"categoriaDespesa.codigo\", categoriaDespesa.descricao as \"categoriaDespesa.descricao\",   ");
		
		sql.append(" curso.codigo as \"curso.codigo\", curso.nome as \"curso.nome\",  ");
		
		sql.append(" turno.codigo as \"turno.codigo\", turno.nome as \"turno.nome\",  ");
		
		sql.append(" turma.codigo as \"turma.codigo\", turma.identificadorturma as \"turma.identificadorturma\",  ");
		
		sql.append(" crad.codigo as \"crad.codigo\", crad.descricao as \"crad.descricao\", crad.identificadorCentroResultado as \"crad.identificadorCentroResultado\",  ");
		
		sql.append(" unidademedida.sigla as \"unidademedida.sigla\" ");
		
		sql.append(" FROM compraitem ");
		sql.append(" INNER JOIN produtoServico ON compraitem.produto = produtoServico.codigo ");
		sql.append(" INNER JOIN categoriaproduto ON produtoServico.categoriaproduto = categoriaproduto.codigo ");
		sql.append(" INNER JOIN unidademedida ON produtoServico.unidademedida = unidademedida.codigo ");
		sql.append(" LEFT JOIN categoriaDespesa ON compraitem.categoriaDespesa = categoriaDespesa.codigo ");
		sql.append(" LEFT JOIN departamento ON compraitem.departamento = departamento.codigo ");
		sql.append(" LEFT JOIN curso on curso.codigo = compraitem.curso");
		sql.append(" LEFT JOIN turno on turno.codigo = compraitem.turno");
		sql.append(" LEFT JOIN turma on turma.codigo = compraitem.turma");
		sql.append(" LEFT JOIN centroresultado AS crad ON compraitem.centroresultadoadministrativo = crad.codigo ");
		return sql;
	}

	@Override
	public List<CompraItemVO> consultarCompraItems(CompraVO compra, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		CompraItem.consultar(getIdEntidade());
		StringBuilder sql = getSQLPadraoConsultaBasica();
		sql.append(" where  compraitem.compra = ?");
		sql.append(" order by produtoServico.nome ");
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), compra.getCodigo());
		List<CompraItemVO> objetos = new ArrayList<>(0);
		while (resultado.next()) {
			CompraItemVO novoObj = montarDadosBasica(resultado, usuario);
			novoObj.setCompra(compra);
			objetos.add(novoObj);
		}
		return objetos;
	}
	
	
	private List<CompraItemVO> montarDadosConsultaBasica(SqlRowSet tabelaResultado,  UsuarioVO usuario) throws Exception {
		List<CompraItemVO> vetResultado = new ArrayList<>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDadosBasica(tabelaResultado, usuario));
		}
		return vetResultado;
	}

	private CompraItemVO montarDadosBasica(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
		CompraItemVO obj = new CompraItemVO();
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo((dadosSQL.getInt("compraitem.codigo")));
		obj.setQuantidadeRequisicao((dadosSQL.getDouble("compraitem.quantidaderequisicao")));
		obj.setQuantidadeAdicional((dadosSQL.getDouble("compraitem.quantidadeadicional")));
		obj.setQuantidadeRecebida((dadosSQL.getDouble("compraitem.quantidadeRecebida")));
		obj.setPrecoUnitario((dadosSQL.getDouble("compraitem.precoUnitario")));
		if(Uteis.isAtributoPreenchido(dadosSQL.getString("compraitem.tipoNivelCentroResultadoEnum"))){
			obj.setTipoNivelCentroResultadoEnum(TipoNivelCentroResultadoEnum.valueOf(dadosSQL.getString("compraitem.tipoNivelCentroResultadoEnum")));	
		}
		obj.getProduto().setCodigo((dadosSQL.getInt("produtoServico.codigo")));
		obj.getProduto().setNome(dadosSQL.getString("produtoServico.nome"));
		obj.getProduto().setTipoProdutoServicoEnum(TipoProdutoServicoEnum.valueOf(dadosSQL.getString("produtoServico.tipoProdutoServico")));
		obj.getProduto().setDescricao(dadosSQL.getString("produtoServico.descricao"));
		obj.getProduto().getUnidadeMedida().setCodigo(dadosSQL.getInt("produtoServico.unidadeMedida"));
		obj.getProduto().getUnidadeMedida().setSigla(dadosSQL.getString("unidademedida.sigla"));
		obj.getProduto().setControlarEstoque(dadosSQL.getBoolean("produtoServico.controlarEstoque"));
		obj.getProduto().setExigeCompraCotacao(dadosSQL.getBoolean("produtoServico.exigeCompraCotacao"));
		obj.getProduto().setJustificativaRequisicaoObrigatoria(dadosSQL.getBoolean("produtoServico.justificativaRequisicaoObrigatoria"));
		obj.getProduto().setValorUnitario(dadosSQL.getDouble("produtoServico.valorUnitario"));
		obj.getProduto().setValorUltimaCompra(dadosSQL.getDouble("produtoServico.valorUltimaCompra"));
		
		obj.getProduto().getCategoriaProduto().setCodigo((dadosSQL.getInt("categoriaProduto.codigo")));
		obj.getProduto().getCategoriaProduto().setNome(dadosSQL.getString("categoriaProduto.nome"));
		
		obj.getCategoriaDespesa().setCodigo((dadosSQL.getInt("categoriaDespesa.codigo")));
		obj.getCategoriaDespesa().setDescricao((dadosSQL.getString("categoriaDespesa.descricao")));

		obj.getDepartamentoVO().setCodigo((dadosSQL.getInt("departamento.codigo")));
		obj.getDepartamentoVO().setNome((dadosSQL.getString("departamento.nome")));
		
		obj.getCursoVO().setCodigo((dadosSQL.getInt("curso.codigo")));
		obj.getCursoVO().setNome((dadosSQL.getString("curso.nome")));

		obj.getTurnoVO().setCodigo((dadosSQL.getInt("turno.codigo")));
		obj.getTurnoVO().setNome((dadosSQL.getString("turno.nome")));

		obj.getTurma().setCodigo((dadosSQL.getInt("turma.codigo")));
		obj.getTurma().setIdentificadorTurma((dadosSQL.getString("turma.identificadorturma")));

		

		obj.getCentroResultadoAdministrativo().setCodigo((dadosSQL.getInt("crad.codigo")));
		obj.getCentroResultadoAdministrativo().setDescricao((dadosSQL.getString("crad.descricao")));
		obj.getCentroResultadoAdministrativo().setIdentificadorCentroResultado((dadosSQL.getString("crad.identificadorCentroResultado")));
		
		if(Uteis.isAtributoPreenchido(obj.getQuantidadeRequisicao())){
			obj.setListaRequisicaoItem(getFacadeFactory().getRequisicaoItemFacade().consultarRequisicaoItemsPorCompraItem(obj, Uteis.NIVELMONTARDADOS_TODOS, usuario));
		}
	
		return obj;
	}
	
	
	

	public List<CompraItemVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<CompraItemVO> vetResultado = new ArrayList<>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	public CompraItemVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		CompraItemVO obj = new CompraItemVO();
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo((dadosSQL.getInt("codigo")));
		obj.getCompra().setCodigo((dadosSQL.getInt("compra")));
		obj.getProduto().setCodigo((dadosSQL.getInt("produto")));
		obj.setQuantidadeRequisicao((dadosSQL.getDouble("quantidaderequisicao")));
		obj.setQuantidadeAdicional((dadosSQL.getDouble("quantidadeadicional")));
		obj.setQuantidadeRecebida((dadosSQL.getDouble("quantidadeRecebida")));
		obj.setPrecoUnitario((dadosSQL.getDouble("precoUnitario")));
		if(Uteis.isAtributoPreenchido(dadosSQL.getString("tipoNivelCentroResultadoEnum"))){
			obj.setTipoNivelCentroResultadoEnum(TipoNivelCentroResultadoEnum.valueOf(dadosSQL.getString("tipoNivelCentroResultadoEnum")));	
		}
		montarDadosProduto(obj, nivelMontarDados, usuario);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		obj.setCategoriaDespesa(Uteis.montarDadosVO(dadosSQL.getInt("categoriaDespesa"), CategoriaDespesaVO.class, p -> getFacadeFactory().getCategoriaDespesaFacade().consultarPorChavePrimaria(p, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario)));
		obj.setDepartamentoVO(Uteis.montarDadosVO(dadosSQL.getInt("departamento"), DepartamentoVO.class, p -> getFacadeFactory().getDepartamentoFacade().consultarPorChavePrimaria(p, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario)));
		obj.setCentroResultadoAdministrativo(Uteis.montarDadosVO(dadosSQL.getInt("centroResultadoAdministrativo"), CentroResultadoVO.class, p -> getFacadeFactory().getCentroResultadoFacade().consultarPorChavePrimaria(p, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario)));
		obj.setCursoVO(Uteis.montarDadosVO(dadosSQL.getInt("curso"), CursoVO.class, p -> getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(p, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, false, usuario)));
		obj.setTurnoVO(Uteis.montarDadosVO(dadosSQL.getInt("turno"), TurnoVO.class, p -> getFacadeFactory().getTurnoFacade().consultarPorChavePrimaria(p, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario)));
		obj.setTurma(Uteis.montarDadosVO(dadosSQL.getInt("turma"), TurmaVO.class, p -> getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(p, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario)));
		return obj;
	}

	public static void montarDadosProduto(CompraItemVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getProduto().getCodigo().intValue() == 0) {
			obj.setProduto(new ProdutoServicoVO());
			return;
		}
		obj.setProduto(getFacadeFactory().getProdutoServicoFacade().consultarPorChavePrimaria(obj.getProduto().getCodigo(), false, nivelMontarDados, usuario));
	}

	public CompraItemVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM CompraItem WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, codigoPrm);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( CompraItem ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	public static String getIdEntidade() {
		return CompraItem.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		CompraItem.idEntidade = idEntidade;
	}
}