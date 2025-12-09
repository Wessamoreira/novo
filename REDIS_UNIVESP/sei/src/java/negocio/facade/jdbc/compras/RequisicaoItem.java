package negocio.facade.jdbc.compras;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.InvalidResultSetAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.enumeradores.MesAnoEnum;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.CompraItemVO;
import negocio.comuns.compras.ItemCotacaoUnidadeEnsinoVO;
import negocio.comuns.compras.ProdutoServicoVO;
import negocio.comuns.compras.RequisicaoItemVO;
import negocio.comuns.compras.RequisicaoVO;
import negocio.comuns.compras.enumeradores.TipoAutorizacaoRequisicaoEnum;
import negocio.comuns.compras.enumeradores.TipoProdutoServicoEnum;
import negocio.comuns.financeiro.enumerador.TipoNivelCentroResultadoEnum;
import negocio.comuns.planoorcamentario.DetalhamentoPlanoOrcamentarioVO;
import negocio.comuns.planoorcamentario.PlanoOrcamentarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.compras.RequisicaoItemInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class RequisicaoItem extends ControleAcesso implements RequisicaoItemInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6297031476059447496L;
	protected static String idEntidade;

	public RequisicaoItem() {
		super();
		setIdEntidade("Requisicao");
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(List<RequisicaoItemVO> lista, UsuarioVO usuarioVO) throws Exception {
		for (RequisicaoItemVO obj : lista) {
			RequisicaoItemVO.validarDados(obj);
			validarQuantidadeAutorizadaEntregue(obj);
			
			if (obj.getCodigo() == 0) {
				incluir(obj, usuarioVO);
			} else {
				alterar(obj, usuarioVO);
			}
		}
	}

	private void incluir(RequisicaoItemVO obj, UsuarioVO usuarioVO) throws Exception {
		final String sql = "INSERT INTO RequisicaoItem( requisicao, produtoServico, quantidadeSolicitada, quantidadeAutorizada, " 
	+ " quantidadeEntregue, cotacao, valorUnitario, dataPrevisaoPagamento, justificativa, unidadeEnsinoEstoqueRetirada,"
	+ " tipoautorizacaorequisicao, itemsolicitacaoorcamentoplanoorcamentario) " 
				+ " VALUES ( ?, ? ,?, ?, ?, ?, ?, ?, ?, ?,?, ?) " + " returning codigo " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
		obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlInserir = arg0.prepareStatement(sql);
				sqlInserir.setInt(1, obj.getRequisicaoVO().getCodigo());
				if (obj.getProdutoServico().getCodigo().intValue() != 0) {
					sqlInserir.setInt(2, obj.getProdutoServico().getCodigo().intValue());
				} else {
					sqlInserir.setNull(2, 0);
				}
				sqlInserir.setDouble(3, obj.getQuantidadeSolicitada().doubleValue());
				sqlInserir.setDouble(4, 0.0);
				sqlInserir.setDouble(5, 0.0);				
				sqlInserir.setNull(6, 0);			
				sqlInserir.setDouble(7, obj.getValorUnitario().doubleValue());
				if (obj.getDataPrevisaoPagamento() != null) {
					sqlInserir.setTimestamp(8, Uteis.getDataJDBCTimestamp(obj.getDataPrevisaoPagamento()));
				} else {
					sqlInserir.setTimestamp(8, null);
				}

				Uteis.setValuePreparedStatement(obj.getJustificativa(), 9, sqlInserir);
				Uteis.setValuePreparedStatement(obj.getUnidadeEnsinoEstoqueRetirada(), 10, sqlInserir);
				int i=10;
				Uteis.setValuePreparedStatement(obj.getTipoAutorizacaoRequisicaoEnum(), ++i, sqlInserir);

				Uteis.setValuePreparedStatement(obj.getItemSolicitacaoOrcamentoPlanoOrcamentarioVO(), ++i, sqlInserir);
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

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final RequisicaoItemVO obj, UsuarioVO usuario) throws Exception {
		final String sql = " UPDATE RequisicaoItem set requisicao=?, produtoServico=?, quantidadeSolicitada=?, quantidadeAutorizada=?, quantidadeEntregue=?," 
				+ " cotacao=?, valorUnitario=?, dataPrevisaoPagamento=?, tipoautorizacaorequisicao=?, justificativa=?, unidadeEnsinoEstoqueRetirada=? ,"
				+ " itemsolicitacaoorcamentoplanoorcamentario = ?" 
				+ " WHERE ((codigo = ?)) " 
				+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		if(getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				
				sqlAlterar.setInt(1, obj.getRequisicaoVO().getCodigo());
				if (obj.getProdutoServico().getCodigo().intValue() != 0) {
					sqlAlterar.setInt(2, obj.getProdutoServico().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(2, 0);
				}
				sqlAlterar.setDouble(3, obj.getQuantidadeSolicitada().doubleValue());
				sqlAlterar.setDouble(4, obj.getQuantidadeAutorizada().doubleValue());
				sqlAlterar.setDouble(5, obj.getQuantidadeEntregue().doubleValue());
				if (Uteis.isAtributoPreenchido(obj.getCotacao())) {
					sqlAlterar.setInt(6, obj.getCotacao());
				} else {
					sqlAlterar.setNull(6, 0);
				}
				sqlAlterar.setDouble(7, obj.getValorUnitario().doubleValue());
				if (obj.getDataPrevisaoPagamento() != null) {
					sqlAlterar.setTimestamp(8, Uteis.getDataJDBCTimestamp(obj.getDataPrevisaoPagamento()));
				} else {
					sqlAlterar.setTimestamp(8, null);
				}
				int i = 8;
				Uteis.setValuePreparedStatement(obj.getTipoAutorizacaoRequisicaoEnum(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getJustificativa(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getUnidadeEnsinoEstoqueRetirada(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getItemSolicitacaoOrcamentoPlanoOrcamentarioVO(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);
				return sqlAlterar;
			}
		}) == 0) {
			incluir(obj, usuario);
		};

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void atualizarRequisitoItemPorEntregaRequisicaoItem(RequisicaoItemVO obj, UsuarioVO usuario) throws Exception {
		StringBuilder sql1 = new StringBuilder("UPDATE RequisicaoItem set quantidadeEntregue =  ").append(obj.getQuantidadeEntregue());
		if (Uteis.isAtributoPreenchido(obj.getUnidadeEnsinoEstoqueRetirada().getCodigo())) {
			sql1.append(" , unidadeEnsinoEstoqueRetirada =  ").append(obj.getUnidadeEnsinoEstoqueRetirada().getCodigo());	
		}
		sql1.append(" WHERE codigo = ( ").append(obj.getCodigo()).append(" ) ");
		sql1.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		getConexao().getJdbcTemplate().update(sql1.toString());
		
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void atualizarRequisitoItemPorCompraItem(CompraItemVO compraItemVO, UsuarioVO usuario) throws Exception {
		if (Uteis.isAtributoPreenchido(compraItemVO.getListaRequisicaoItem())) {
			StringBuilder sql1 = new StringBuilder("UPDATE RequisicaoItem set compraitem =  ").append(compraItemVO.getCodigo());
			sql1.append(" WHERE codigo in( ").append(UteisTexto.converteListaEntidadeCampoCodigoParaString(compraItemVO.getListaRequisicaoItem())).append(" ) ");
			sql1.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			getConexao().getJdbcTemplate().update(sql1.toString());
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void anularVinculoRequisicaoItemComCompraItem(List<CompraItemVO> lista, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder("UPDATE RequisicaoItem set compraitem = null ");
		sql.append("where compraitem  in (").append(UteisTexto.converteListaEntidadeCampoCodigoParaString(lista)).append(" ) ");
		sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		getConexao().getJdbcTemplate().update(sql.toString());
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void atualizarRequisitoItemPorCotacao(ItemCotacaoUnidadeEnsinoVO icue, UsuarioVO usuario) throws Exception {
		if (Uteis.isAtributoPreenchido(icue.getListaRequisicaoItemVOs())) {
			StringBuilder sql1 = new StringBuilder("UPDATE RequisicaoItem set cotacao =  ").append(icue.getCotacao().getCodigo());
			sql1.append(" WHERE codigo in( ").append(UteisTexto.converteListaEntidadeCampoCodigoParaString(icue.getListaRequisicaoItemVOs())).append(" ) ");
			sql1.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			getConexao().getJdbcTemplate().update(sql1.toString());
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void anularVinculoRequisicaoItemComCotacao(Integer cotacao, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder("UPDATE RequisicaoItem set cotacao = null ");
		sql.append("where cotacao=").append(cotacao).append("  ");
		sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		getConexao().getJdbcTemplate().update(sql.toString());
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(final RequisicaoItemVO obj) throws Exception {
		final String sql = "DELETE FROM RequisicaoItem WHERE ((codigo = ?))";
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlExcluir = arg0.prepareStatement(sql);
				sqlExcluir.setInt(1, obj.getCodigo().intValue());
				return sqlExcluir;
			}
		});

	}

	private StringBuilder getSQLPadraoConsultaBasica() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT requisicaoitem.codigo as \"requisicaoitem.codigo\",  ");
		sql.append(" requisicaoitem.quantidadeSolicitada as \"requisicaoitem.quantidadeSolicitada\", requisicaoitem.quantidadeAutorizada as \"requisicaoitem.quantidadeAutorizada\", ");
		sql.append(" requisicaoitem.valorUnitario as \"requisicaoitem.valorUnitario\", requisicaoitem.quantidadeEntregue as \"requisicaoitem.quantidadeEntregue\", ");
		sql.append(" requisicaoitem.cotacao as \"requisicaoitem.cotacao\", requisicaoitem.compraitem as \"requisicaoitem.compraitem\", ");
		sql.append(" requisicaoitem.dataPrevisaoPagamento as \"requisicaoitem.dataPrevisaoPagamento\", requisicaoitem.justificativa as \"requisicaoitem.justificativa\", ");
		sql.append(" requisicaoitem.tipoautorizacaorequisicao as \"requisicaoitem.tipoautorizacaorequisicao\",  ");
		sql.append(" requisicaoitem.itemsolicitacaoorcamentoplanoorcamentario as \"requisicaoitem.itemsolicitacaoorcamentoplanoorcamentario\", ");

		sql.append(" produtoServico.codigo as \"produtoServico.codigo\", produtoServico.nome as \"produtoServico.nome\",  ");
		sql.append(" produtoServico.tipoProdutoServico as \"produtoServico.tipoProdutoServico\", produtoServico.descricao as \"produtoServico.descricao\",  ");
		sql.append(" produtoServico.controlarEstoque as \"produtoServico.controlarEstoque\", produtoServico.exigeCompraCotacao as \"produtoServico.exigeCompraCotacao\", ");
		sql.append(" produtoServico.valorUnitario as \"produtoServico.valorUnitario\", produtoServico.valorUltimaCompra as \"produtoServico.valorUltimaCompra\", ");
		sql.append(" produtoServico.justificativaRequisicaoObrigatoria as \"produtoServico.justificativaRequisicaoObrigatoria\",  ");
		
		sql.append(" unidadeMedida.codigo as \"unidadeMedida.codigo\", unidadeMedida.nome as \"unidadeMedida.nome\",  unidadeMedida.fracionado as \"unidadeMedida.fracionado\", ");

		sql.append(" categoriaproduto.codigo as \"categoriaproduto.codigo\", categoriaproduto.nome as \"categoriaproduto.nome\", ");

		sql.append(" unidadeensino.codigo as \"unidadeensino.codigo\", unidadeensino.nome as \"unidadeensino.nome\", ");

		sql.append(" unidadeEnsinoEstoqueRetirada.codigo as \"unidadeEnsinoEstoqueRetirada.codigo\", unidadeEnsinoEstoqueRetirada.nome as \"unidadeEnsinoEstoqueRetirada.nome\", ");

		sql.append(" requisicao.codigo as \"requisicao.codigo\",   ");
		sql.append(" requisicao.tipoNivelCentroResultadoEnum as \"requisicao.tipoNivelCentroResultadoEnum\",   ");
		sql.append(" categoriaDespesa.codigo as \"categoriaDespesa.codigo\", categoriaDespesa.descricao as \"categoriaDespesa.descricao\",   ");
		sql.append(" categoriaDespesa.informarTurma as \"categoriaDespesa.informarTurma\", categoriaDespesa.nivelCategoriaDespesa as \"categoriaDespesa.nivelCategoriaDespesa\",   ");		
		sql.append(" funcionariocargo.codigo as \"funcionariocargo.codigo\", ");
		sql.append(" departamento.codigo as \"departamento.codigo\", departamento.nome as \"departamento.nome\",  ");
		sql.append(" curso.codigo as \"curso.codigo\", curso.nome as \"curso.nome\",  ");
		sql.append(" turno.codigo as \"turno.codigo\", turno.nome as \"turno.nome\",  ");
		sql.append(" turma.codigo as \"turma.codigo\", turma.identificadorturma as \"turma.identificadorturma\",  ");
		sql.append(" compraitem.compra as \"compraitem.compra\",  ");
		sql.append(" centroresultadoadministrativo.codigo as \"centroresultadoadministrativo.codigo\", centroresultadoadministrativo.descricao as \"centroresultadoadministrativo.descricao\", centroresultadoadministrativo.identificadorCentroResultado as \"centroresultadoadministrativo.identificadorCentroResultado\",   ");
		

		sql.append(" Sum(estoque.quantidade) AS \"requisicaoitem.quantidadeestoque\"  ");

		sql.append(" FROM requisicaoitem ");
		sql.append(" INNER JOIN requisicao ON requisicao.codigo = requisicaoitem.requisicao ");
		sql.append(" INNER JOIN categoriaDespesa ON requisicao.categoriaDespesa = categoriaDespesa.codigo ");
		sql.append(" LEFT JOIN funcionariocargo on requisicao.funcionariocargo = funcionariocargo.codigo ");
		sql.append(" LEFT JOIN departamento on requisicao.departamento = departamento.codigo ");
		sql.append(" LEFT JOIN curso on requisicao.curso = curso.codigo ");
		sql.append(" LEFT JOIN turma on requisicao.turma = turma.codigo ");
		sql.append(" LEFT JOIN turno on requisicao.turno = turno.codigo ");
		sql.append(" LEFT JOIN centroresultado AS centroresultadoadministrativo ON requisicao.centroresultadoadministrativo = centroresultadoadministrativo.codigo ");
		sql.append(" INNER JOIN unidadeensino ON unidadeensino.codigo = requisicao.unidadeensino ");
		sql.append(" INNER JOIN produtoServico ON produtoServico.codigo = requisicaoitem.produtoServico ");
		sql.append(" INNER JOIN unidademedida ON unidademedida.codigo = produtoServico.unidademedida ");
		sql.append(" INNER JOIN categoriaproduto ON categoriaproduto.codigo = produtoServico.categoriaproduto ");
		sql.append(" LEFT JOIN unidadeensino as unidadeEnsinoEstoqueRetirada ON unidadeEnsinoEstoqueRetirada.codigo = requisicaoitem.unidadeEnsinoEstoqueRetirada ");
		sql.append(" LEFT JOIN estoque ON case when requisicaoitem.unidadeEnsinoEstoqueRetirada is not null then estoque.unidadeensino = requisicaoitem.unidadeEnsinoEstoqueRetirada else estoque.unidadeensino = requisicao.unidadeensino end  and produto = requisicaoitem.produtoservico ");
		sql.append(" LEFT JOIN compraitem on compraitem.codigo = requisicaoitem.compraitem ");
		return sql;
	}

	private StringBuilder getSQLPadraoGroupBy() {
		StringBuilder sql = new StringBuilder();
		sql.append(" GROUP BY requisicaoitem.codigo,");
		sql.append(" requisicaoitem.quantidadesolicitada, requisicaoitem.quantidadeautorizada , requisicaoitem.valorunitario,");
		sql.append(" requisicaoitem.quantidadeentregue, requisicaoitem.cotacao, ");
		sql.append(" requisicaoitem.dataprevisaopagamento, requisicaoitem.justificativa, ");
		sql.append(" requisicaoitem.unidadeEnsinoEstoqueRetirada, requisicaoitem.tipoautorizacaorequisicao, ");
		sql.append(" produtoservico.codigo, ");
		sql.append(" produtoservico.nome, produtoservico.tipoprodutoservico, produtoservico.descricao, ");
		sql.append(" produtoservico.controlarestoque, produtoservico.exigecompracotacao, ");
		sql.append(" produtoservico.valorunitario, produtoservico.valorultimacompra, produtoservico.justificativarequisicaoobrigatoria, ");		
		sql.append(" unidademedida.codigo, unidademedida.nome, unidademedida.fracionado, ");
		sql.append(" categoriaproduto.codigo, categoriaproduto.nome, ");
		sql.append(" unidadeensino.codigo, unidadeensino.nome,  ");
		sql.append(" unidadeEnsinoEstoqueRetirada.codigo, unidadeEnsinoEstoqueRetirada.nome,  ");
		sql.append(" requisicao.codigo, requisicao.tipoNivelCentroResultadoEnum, ");
		sql.append(" categoriaDespesa.codigo, categoriaDespesa.descricao,   ");
		sql.append(" categoriaDespesa.informarTurma, categoriaDespesa.nivelCategoriaDespesa,   ");
		sql.append(" categoriaDespesa.nivelAcademicoObrigatorio, ");
		sql.append(" centroresultadoadministrativo.codigo, centroresultadoadministrativo.descricao, centroresultadoadministrativo.identificadorCentroResultado,   ");
		sql.append(" funcionariocargo.codigo , ");
		sql.append(" departamento.codigo, departamento.nome,  ");
		sql.append(" curso.codigo, curso.nome,  ");
		sql.append(" turno.codigo, turno.nome,  ");
		sql.append(" turma.codigo, turma.identificadorturma, compraitem.compra  ");

		return sql;
	}

	@Override
	public List<RequisicaoItemVO> consultarRequisicaoItemsPorItemCotacaoUnidadeEnsinoVO(List<RequisicaoVO> listaRequisicao, ItemCotacaoUnidadeEnsinoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE requisicaoitem.produtoServico = ").append(obj.getProdutoVO().getCodigo()).append(" ");
		sql.append(" AND requisicao.unidadeEnsino = ").append(obj.getUnidadeEnsinoVO().getCodigo()).append(" ");
		sql.append(" AND requisicaoitem.tipoAutorizacaoRequisicao = '").append(TipoAutorizacaoRequisicaoEnum.COTACAO.name()).append("' ");
		if (Uteis.isAtributoPreenchido(obj.getCotacao())) {
			sql.append(" AND requisicaoitem.cotacao = ").append(obj.getCotacao().getCodigo());
		} else {
			sql.append(" AND (requisicaoitem.cotacao is null  OR requisicaoitem.cotacao = 0) ");
			sql.append(" AND requisicaoitem.quantidadeAutorizada > 0  AND requisicao.situacaoAutorizacao = 'AU' AND (requisicao.situacaoentrega in('PA','PE')) ");
		}
		if(Uteis.isAtributoPreenchido(listaRequisicao)) {

			boolean virgula = false;
			sql.append("AND requisicaoitem.requisicao IN(");
			for (RequisicaoVO requisicaoVO : listaRequisicao) {
				if (!virgula) {
					sql.append(requisicaoVO.getCodigo());
				} else {
					sql.append(", ").append(requisicaoVO.getCodigo());
				}
				virgula = true;
			}
			sql.append(") ");			
			
		}
		sql.append(getSQLPadraoGroupBy());
		sql.append(" order by  requisicao.datarequisicao ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaBasica(tabelaResultado, usuario);
	}

	@Override
	public List<RequisicaoItemVO> consultarRequisicaoItemsPorCompraItem(CompraItemVO compraItem, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sql = getSQLPadraoConsultaBasica();
		sql.append(" where  requisicaoitem.compraitem = ?");
		sql.append(getSQLPadraoGroupBy());
		sql.append(" order by  requisicao.datarequisicao ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), compraItem.getCodigo());
		return montarDadosConsultaBasica(tabelaResultado, usuario);
	}

	public List<RequisicaoItemVO> consultarRapidaRequisicaoItems(RequisicaoVO requisicao, TipoAutorizacaoRequisicaoEnum tipoAutorizacaoRequisicaoEnum ,int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sql = getSQLPadraoConsultaBasica();
		sql.append(" where  requisicao.codigo = ?");
		if(Uteis.isAtributoPreenchido(tipoAutorizacaoRequisicaoEnum)) {
			sql.append(" and  requisicaoitem.tipoautorizacaorequisicao = '").append(tipoAutorizacaoRequisicaoEnum.name()).append("' ");
		}
		sql.append(getSQLPadraoGroupBy());
		sql.append(" order by  requisicao.datarequisicao, produtoServico.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), requisicao.getCodigo());
		List<RequisicaoItemVO> vetResultado = new ArrayList<>(0);
		while (tabelaResultado.next()) {
			RequisicaoItemVO ri = montarDadosBasica(tabelaResultado, usuario);
			ri.setRequisicaoVO(requisicao);
			vetResultado.add(ri);
		}
		return vetResultado;
	}
	
	public List<RequisicaoItemVO> consultarRequisicaoItems(RequisicaoVO requisicao, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT * FROM RequisicaoItem ");
		sql.append(" where  requisicao = ?");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), requisicao.getCodigo());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	private List<RequisicaoItemVO> montarDadosConsultaBasica(SqlRowSet tabelaResultado, UsuarioVO usuario) throws InvalidResultSetAccessException, Exception {
		List<RequisicaoItemVO> vetResultado = new ArrayList<>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDadosBasica(tabelaResultado, usuario));
		}
		return vetResultado;
	}

	private RequisicaoItemVO montarDadosBasica(SqlRowSet dadosSQL, UsuarioVO usuario) throws InvalidResultSetAccessException, Exception {
		RequisicaoItemVO obj = new RequisicaoItemVO();

		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo((dadosSQL.getInt("requisicaoitem.codigo")));
		obj.setQuantidadeSolicitada((dadosSQL.getDouble("requisicaoitem.quantidadeSolicitada")));
		obj.setQuantidadeAutorizada((dadosSQL.getDouble("requisicaoitem.quantidadeAutorizada")));
		obj.setQuantidadeEntregue((dadosSQL.getDouble("requisicaoitem.quantidadeEntregue")));
		obj.setQuantidadeEstoque((dadosSQL.getDouble("requisicaoitem.quantidadeestoque")));
		obj.setCotacao((dadosSQL.getInt("requisicaoitem.cotacao")));
		obj.getCompraItemVO().setCodigo(dadosSQL.getInt("requisicaoitem.compraitem"));
		obj.getCompraItemVO().getCompra().setCodigo(dadosSQL.getInt("compraitem.compra"));
		obj.setValorUnitario((dadosSQL.getDouble("requisicaoitem.valorUnitario")));
		obj.setDataPrevisaoPagamento(dadosSQL.getTimestamp("requisicaoitem.dataPrevisaoPagamento"));
		obj.setJustificativa(dadosSQL.getString("requisicaoitem.justificativa"));
		obj.setTipoAutorizacaoRequisicaoEnum(TipoAutorizacaoRequisicaoEnum.valueOf(dadosSQL.getString("requisicaoitem.tipoautorizacaorequisicao")));
		
		if (Uteis.isAtributoPreenchido(dadosSQL.getInt("requisicaoitem.itemsolicitacaoorcamentoplanoorcamentario"))) {
			obj.setItemSolicitacaoOrcamentoPlanoOrcamentarioVO(getFacadeFactory().getItemSolicitacaoOrcamentoPlanoOrcamentarioFacade().
					consultarPorChavePrimaria(dadosSQL.getInt("requisicaoitem.itemsolicitacaoorcamentoplanoorcamentario"), usuario));
		}

		obj.getRequisicaoVO().setCodigo((dadosSQL.getInt("requisicao.codigo")));
		if(Uteis.isAtributoPreenchido(dadosSQL.getString("requisicao.tipoNivelCentroResultadoEnum"))){
			obj.getRequisicaoVO().setTipoNivelCentroResultadoEnum(TipoNivelCentroResultadoEnum.valueOf(dadosSQL.getString("requisicao.tipoNivelCentroResultadoEnum")));
		}
		obj.getRequisicaoVO().getCategoriaDespesa().setCodigo((dadosSQL.getInt("categoriaDespesa.codigo")));
		obj.getRequisicaoVO().getCategoriaDespesa().setDescricao((dadosSQL.getString("categoriaDespesa.descricao")));
		obj.getRequisicaoVO().getCategoriaDespesa().setInformarTurma((dadosSQL.getString("categoriaDespesa.informarTurma")));
		obj.getRequisicaoVO().getCategoriaDespesa().setNivelCategoriaDespesa((dadosSQL.getString("categoriaDespesa.nivelCategoriaDespesa")));

		
		obj.getRequisicaoVO().getFuncionarioCargoVO().setCodigo((dadosSQL.getInt("funcionariocargo.codigo")));
		
		obj.getRequisicaoVO().getDepartamento().setCodigo((dadosSQL.getInt("departamento.codigo")));
		obj.getRequisicaoVO().getDepartamento().setNome((dadosSQL.getString("departamento.nome")));
		
		obj.getRequisicaoVO().getCurso().setCodigo((dadosSQL.getInt("curso.codigo")));
		obj.getRequisicaoVO().getCurso().setNome((dadosSQL.getString("curso.nome")));

		obj.getRequisicaoVO().getTurno().setCodigo((dadosSQL.getInt("turno.codigo")));
		obj.getRequisicaoVO().getTurno().setNome((dadosSQL.getString("turno.nome")));

		obj.getRequisicaoVO().getTurma().setCodigo((dadosSQL.getInt("turma.codigo")));
		obj.getRequisicaoVO().getTurma().setIdentificadorTurma((dadosSQL.getString("turma.identificadorturma")));

		obj.getRequisicaoVO().getUnidadeEnsino().setCodigo((dadosSQL.getInt("unidadeEnsino.codigo")));
		obj.getRequisicaoVO().getUnidadeEnsino().setNome((dadosSQL.getString("unidadeEnsino.nome")));		

		obj.getRequisicaoVO().getCentroResultadoAdministrativo().setCodigo((dadosSQL.getInt("centroResultadoAdministrativo.codigo")));
		obj.getRequisicaoVO().getCentroResultadoAdministrativo().setDescricao((dadosSQL.getString("centroResultadoAdministrativo.descricao")));
		obj.getRequisicaoVO().getCentroResultadoAdministrativo().setIdentificadorCentroResultado((dadosSQL.getString("centroResultadoAdministrativo.identificadorCentroResultado")));
		try {
			obj.getRequisicaoVO().setArquivoVO(getFacadeFactory().getArquivoFacade().consultarPorCodOrigemRequisicao(obj.getRequisicaoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		obj.getProdutoServico().setCodigo((dadosSQL.getInt("produtoServico.codigo")));
		obj.getProdutoServico().setNome(dadosSQL.getString("produtoServico.nome"));
		obj.getProdutoServico().setTipoProdutoServicoEnum(TipoProdutoServicoEnum.valueOf(dadosSQL.getString("produtoServico.tipoProdutoServico")));
		obj.getProdutoServico().setDescricao(dadosSQL.getString("produtoServico.descricao"));
		obj.getProdutoServico().setControlarEstoque(dadosSQL.getBoolean("produtoServico.controlarEstoque"));
		obj.getProdutoServico().setExigeCompraCotacao(dadosSQL.getBoolean("produtoServico.exigeCompraCotacao"));
		obj.getProdutoServico().setJustificativaRequisicaoObrigatoria(dadosSQL.getBoolean("produtoServico.justificativaRequisicaoObrigatoria"));
		obj.getProdutoServico().setValorUnitario(dadosSQL.getDouble("produtoServico.valorUnitario"));
		obj.getProdutoServico().setValorUltimaCompra(dadosSQL.getDouble("produtoServico.valorUltimaCompra"));
		if (!Uteis.isAtributoPreenchido(obj.getValorUnitario())) {
			obj.setValorUnitario((dadosSQL.getDouble("produtoServico.valorUltimaCompra")));
		}

		obj.getProdutoServico().getUnidadeMedida().setCodigo((dadosSQL.getInt("unidademedida.codigo")));
		obj.getProdutoServico().getUnidadeMedida().setNome(dadosSQL.getString("unidademedida.nome"));
		obj.getProdutoServico().getUnidadeMedida().setFracionado(dadosSQL.getBoolean("unidademedida.fracionado"));
		
		obj.getProdutoServico().getCategoriaProduto().setCodigo((dadosSQL.getInt("categoriaProduto.codigo")));
		obj.getProdutoServico().getCategoriaProduto().setNome(dadosSQL.getString("categoriaProduto.nome"));

		if (Uteis.isAtributoPreenchido(dadosSQL.getInt("unidadeEnsinoEstoqueRetirada.codigo"))) {
			obj.getUnidadeEnsinoEstoqueRetirada().setCodigo(dadosSQL.getInt("unidadeEnsinoEstoqueRetirada.codigo"));
			obj.getUnidadeEnsinoEstoqueRetirada().setNome(dadosSQL.getString("unidadeEnsinoEstoqueRetirada.nome"));
		} else {
			obj.getUnidadeEnsinoEstoqueRetirada().setCodigo(dadosSQL.getInt("unidadeEnsino.codigo"));
			obj.getUnidadeEnsinoEstoqueRetirada().setNome(dadosSQL.getString("unidadeEnsino.nome"));
		}
		return obj;
	}

	public static List<RequisicaoItemVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<RequisicaoItemVO> vetResultado = new ArrayList<>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	public static RequisicaoItemVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		RequisicaoItemVO obj = new RequisicaoItemVO();
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo((dadosSQL.getInt("codigo")));
		obj.getRequisicaoVO().setCodigo((dadosSQL.getInt("requisicao")));
		obj.getProdutoServico().setCodigo((dadosSQL.getInt("produtoServico")));
		obj.getUnidadeEnsinoEstoqueRetirada().setCodigo((dadosSQL.getInt("unidadeEnsinoEstoqueRetirada")));
		obj.setQuantidadeSolicitada((dadosSQL.getDouble("quantidadeSolicitada")));
		obj.setQuantidadeAutorizada((dadosSQL.getDouble("quantidadeAutorizada")));
		obj.setQuantidadeEntregue((dadosSQL.getDouble("quantidadeEntregue")));
		obj.setCotacao((dadosSQL.getInt("cotacao")));
		obj.setValorUnitario((dadosSQL.getDouble("valorUnitario")));
		obj.setDataPrevisaoPagamento(dadosSQL.getTimestamp("dataPrevisaoPagamento"));
		obj.setJustificativa(dadosSQL.getString("justificativa"));
		obj.setTipoAutorizacaoRequisicaoEnum(TipoAutorizacaoRequisicaoEnum.valueOf(dadosSQL.getString("requisicaoitem.tipoautorizacaorequisicao")));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			return obj;
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		montarDadosProduto(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		return obj;
	}

	public static void montarDadosProduto(RequisicaoItemVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getProdutoServico().getCodigo().intValue() == 0) {
			obj.setProdutoServico(new ProdutoServicoVO());
			return;
		}
		obj.setProdutoServico(getFacadeFactory().getProdutoServicoFacade().consultarPorChavePrimaria(obj.getProdutoServico().getCodigo(), false, nivelMontarDados, usuario));
	}

	public RequisicaoItemVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		StringBuilder sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE requisicaoitem.codigo = ?");
		sql.append(getSQLPadraoGroupBy());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigoPrm);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( RequisicaoItem ).");
		}
		return (montarDadosBasica(tabelaResultado, usuario));
	}

	 /**
     * Consulta todos as {@link RequisicaoItemVO} com vinculo no {@link DetalhamentoPlanoOrcamentarioVO}
     * dos {@link PlanoOrcamentarioVO}.
     * 
     * @param detalhamentoPlanoOrcamentario
     * @param controlarAcesso
     * @param usuario
     * @return
     * @throws Exception
     */
	public List<RequisicaoItemVO> consultarItemSolicitacaoOrcamentarioPorSolicitacaoPlanoOrcamentario(Integer solicitacaoPlanoOrcamentario, boolean controlarAcesso, UsuarioVO usuario, DepartamentoVO departamento) throws Exception {
    	StringBuilder sql = new StringBuilder();
    	sql.append(" select usuario.codigo \"usuario.codigo\", usuario.nome \"usuario.nome\", ");
    	sql.append(" categoriadespesa.codigo \"categoriadespesa.codigo\", categoriadespesa.descricao \"categoriadespesa.descricao\",  requisicao.datarequisicao \"requisicao.datarequisicao\", ");
    	sql.append(" requisicaoitem.quantidadeautorizada \"requisicaoitem.quantidadeautorizada\", requisicaoitem.valorunitario \"requisicaoitem.valorunitario\" from requisicao ");
    	sql.append(" inner join requisicaoitem on requisicaoitem.requisicao = requisicao.codigo" );
    	sql.append(" inner join categoriadespesa on requisicao.categoriadespesa = categoriadespesa.codigo ");
    	sql.append(" inner join itemsolicitacaoorcamentoplanoorcamentario as item on item.codigo = requisicaoitem.itemsolicitacaoorcamentoplanoorcamentario ");
    	sql.append(" inner join solicitacaoorcamentoplanoorcamentario on solicitacaoorcamentoplanoorcamentario.codigo = item.solicitacaoorcamentoplanoorcamentario ");
    	sql.append(" inner join planoorcamentario on planoorcamentario.codigo = solicitacaoorcamentoplanoorcamentario.planoorcamentario ");
    	sql.append(" inner join usuario on usuario.codigo = requisicao.responsavelrequisicao ");
    	sql.append(" where requisicao.situacaoautorizacao = 'AU' and requisicaoitem.quantidadeautorizada > 0 ");
    	sql.append(" and solicitacaoorcamentoplanoorcamentario.codigo = ? and solicitacaoorcamentoplanoorcamentario.departamento = ? ");

    	SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), solicitacaoPlanoOrcamentario, departamento.getCodigo());

    	List<RequisicaoItemVO> lista = new ArrayList<>(0);
    	while (resultado.next()) {
    		RequisicaoItemVO obj = new RequisicaoItemVO();
    		obj.getRequisicaoVO().getResponsavelRequisicao().setCodigo(resultado.getInt("usuario.codigo"));
    		obj.getRequisicaoVO().getResponsavelRequisicao().setNome(resultado.getString("usuario.nome"));
    		obj.getRequisicaoVO().getCategoriaDespesa().setCodigo(resultado.getInt("categoriadespesa.codigo"));
    		obj.getRequisicaoVO().getCategoriaDespesa().setDescricao(resultado.getString("categoriadespesa.descricao"));
    		obj.getRequisicaoVO().setDataRequisicao(resultado.getDate("requisicao.datarequisicao"));
    		obj.setQuantidadeAutorizada(resultado.getDouble("requisicaoitem.quantidadeautorizada"));
    		obj.setValorUnitario(resultado.getDouble("requisicaoitem.valorunitario"));

    		lista.add(obj);
    	}
    	return lista;
    }

    @Override
    public void validarExisteRequisicaoItemPorPlanoOrcamentario(PlanoOrcamentarioVO planoOrcamentarioVO) throws Exception {
    	StringBuilder sql = new StringBuilder();
    	sql.append(" select requisicao.codigo from requisicao");
    	sql.append(" inner join requisicaoitem on requisicaoitem.requisicao = requisicao.codigo");
    	sql.append(" inner join itemsolicitacaoorcamentoplanoorcamentario as item on item.codigo = requisicaoitem.itemsolicitacaoorcamentoplanoorcamentario");
    	sql.append(" inner join solicitacaoorcamentoplanoorcamentario on item.solicitacaoorcamentoplanoorcamentario = solicitacaoorcamentoplanoorcamentario.codigo");
    	sql.append(" inner join planoorcamentario on solicitacaoorcamentoplanoorcamentario.planoorcamentario = planoorcamentario.codigo");
    	sql.append(" where planoorcamentario = ? order by requisicao limit 1");

    	SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), planoOrcamentarioVO.getCodigo());
    	if (resultado.next()) {
    		throw new Exception("Esse Plano Orçamentario é referenciado pela Requisição de número " + resultado.getInt("codigo"));
    	}
    }

	private void validarQuantidadeAutorizadaEntregue(RequisicaoItemVO requisicaoItemVO) {
		if (Uteis.isAtributoPreenchido(requisicaoItemVO.getRequisicaoVO().getSituacaoAutorizacao()) && requisicaoItemVO.getRequisicaoVO().getSituacaoAutorizacao().equals("PE")) {
			requisicaoItemVO.setQuantidadeAutorizada(0.0);
		}
		if (Uteis.isAtributoPreenchido(requisicaoItemVO.getRequisicaoVO().getSituacaoAutorizacao()) && requisicaoItemVO.getRequisicaoVO().getSituacaoEntrega().equals("PE")) {
			requisicaoItemVO.setQuantidadeEntregue(0.0);
		}
	}
	
	@Override
	public List<RequisicaoItemVO> consultarRequisicaoItemConsumidoPlanoOrcamentario(Integer planoOrcamentario,  Integer solicitacaoPlanoOrcamentario, Integer unidadeEnsino, Integer departamento, Integer categoriaDespesa, MesAnoEnum mesAno, String ano) throws Exception {
		StringBuilder sql  =  new StringBuilder("");
		sql.append("  select requisicao.datarequisicao, requisicao.dataautorizacao, requisicao.tipoAutorizacaoRequisicao, requisicaoitem.requisicao, produtoservico.nome as nomeproduto, produtoservico.codigo as produtoservico, requisicaoitem.justificativa, requisicaoitem.quantidadeAutorizada, requisicaoitem.quantidadeEntregue, requisicaoitem.valorUnitario from requisicaoitem");
		sql.append("  inner join produtoservico on produtoservico.codigo = requisicaoitem.produtoservico ");
		if(Uteis.isAtributoPreenchido(solicitacaoPlanoOrcamentario) || Uteis.isAtributoPreenchido(planoOrcamentario)  || Uteis.isAtributoPreenchido(departamento)   || Uteis.isAtributoPreenchido(categoriaDespesa)) {
			sql.append("  inner join itemsolicitacaoorcamentoplanoorcamentario on itemsolicitacaoorcamentoplanoorcamentario.codigo = requisicaoitem.itemsolicitacaoorcamentoplanoorcamentario ");
		}
		if(Uteis.isAtributoPreenchido(solicitacaoPlanoOrcamentario) || Uteis.isAtributoPreenchido(planoOrcamentario)  || Uteis.isAtributoPreenchido(departamento)) {
			sql.append("  inner join solicitacaoorcamentoplanoorcamentario on itemsolicitacaoorcamentoplanoorcamentario.solicitacaoorcamentoplanoorcamentario = solicitacaoorcamentoplanoorcamentario.codigo ");
		}
		if(Uteis.isAtributoPreenchido(planoOrcamentario)) {
			sql.append("  inner join planoorcamentario on planoorcamentario.codigo = solicitacaoorcamentoplanoorcamentario.planoorcamentario ");
		}
		sql.append("  inner join requisicao on requisicao.codigo = requisicaoitem.requisicao ");
		sql.append("  where requisicao.situacaoautorizacao = 'AU' and requisicaoitem.quantidadeautorizada > 0  ");	
		if(Uteis.isAtributoPreenchido(planoOrcamentario)) {
			sql.append(" and   planoorcamentario.codigo  = ").append(planoOrcamentario).append("");
		}
		if(Uteis.isAtributoPreenchido(solicitacaoPlanoOrcamentario)) {
			sql.append(" and   solicitacaoorcamentoplanoorcamentario.codigo  = ").append(solicitacaoPlanoOrcamentario).append("");
		}
		if(Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sql.append(" and   solicitacaoorcamentoplanoorcamentario.unidadeEnsino  = ").append(unidadeEnsino).append("");
		}
		if(Uteis.isAtributoPreenchido(departamento)) {
			sql.append(" and   solicitacaoorcamentoplanoorcamentario.departamento  = ").append(departamento).append("");
		}
		if(Uteis.isAtributoPreenchido(categoriaDespesa)) {
			sql.append(" and   itemsolicitacaoorcamentoplanoorcamentario.categoriaDespesa  = ").append(categoriaDespesa).append("");
		}
		if(Uteis.isAtributoPreenchido(mesAno)) {
			sql.append(" and  extract(month from requisicao.datarequisicao) = '").append(mesAno.getKey()).append("'::int");
		}
		if(Uteis.isAtributoPreenchido(mesAno)) {
			sql.append(" and  extract(year from requisicao.datarequisicao) = ").append(ano).append("");
		}
		sql.append(" order by datarequisicao ");
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());

		List<RequisicaoItemVO> requisicaoItemVOs =  new ArrayList<>();
		RequisicaoItemVO obj = null;
		while (tabelaResultado.next()) {
			obj = new RequisicaoItemVO();
			obj.getRequisicaoVO().setCodigo(tabelaResultado.getInt("requisicao"));
			obj.getRequisicaoVO().setDataRequisicao(tabelaResultado.getDate("datarequisicao"));
			obj.getRequisicaoVO().setDataAutorizacao(tabelaResultado.getDate("dataautorizacao"));

			obj.getProdutoServico().setCodigo(tabelaResultado.getInt("produtoservico"));
			obj.getProdutoServico().setNome(tabelaResultado.getString("nomeproduto"));
			obj.setJustificativa(tabelaResultado.getString("justificativa"));
			obj.setQuantidadeAutorizada(tabelaResultado.getDouble("quantidadeAutorizada"));
			obj.setQuantidadeEntregue(tabelaResultado.getDouble("quantidadeEntregue"));
			obj.setValorUnitario(tabelaResultado.getDouble("valorUnitario"));
			requisicaoItemVOs.add(obj);
		}
		
		return requisicaoItemVOs;
	}

	public static String getIdEntidade() {
		return RequisicaoItem.idEntidade;
	}
	
	public void setIdEntidade(String idEntidade) {
		RequisicaoItem.idEntidade = idEntidade;
	}
}
