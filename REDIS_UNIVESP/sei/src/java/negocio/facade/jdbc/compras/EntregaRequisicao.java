package negocio.facade.jdbc.compras;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import controle.arquitetura.DataModelo;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.EntregaRequisicaoItemVO;
import negocio.comuns.compras.EntregaRequisicaoVO;
import negocio.comuns.compras.OperacaoEstoqueVO;
import negocio.comuns.compras.RequisicaoItemVO;
import negocio.comuns.compras.RequisicaoVO;
import negocio.comuns.compras.enumeradores.OperacaoEstoqueEnum;
import negocio.comuns.compras.enumeradores.TipoOperacaoEstoqueOrigemEnum;
import negocio.comuns.compras.enumeradores.TipoProdutoServicoEnum;
import negocio.comuns.financeiro.CentroResultadoOrigemVO;
import negocio.comuns.financeiro.enumerador.TipoCentroResultadoOrigemEnum;
import negocio.comuns.financeiro.enumerador.TipoMovimentacaoCentroResultadoOrigemEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.compras.EntregaRequisicaoInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class EntregaRequisicao extends ControleAcesso implements EntregaRequisicaoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3743887218464533740L;
	protected static String idEntidade;

	public EntregaRequisicao() {
		super();
		setIdEntidade("EntregaRequisicao");
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final EntregaRequisicaoVO obj, UsuarioVO usuario) throws Exception {
		try {
			EntregaRequisicao.incluir(getIdEntidade(), true, usuario);
			EntregaRequisicaoVO.validarDados(obj);
			validarSaldoDisponivelPlanoOrcamentario(obj, usuario);
			obj.realizarUpperCaseDados();
			final String sql = "INSERT INTO EntregaRequisicao( requisicao, responsavel, data ) VALUES ( ?, ?, ? ) returning codigo";
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					if (obj.getRequisicao().getCodigo().intValue() != 0) {
						sqlInserir.setInt(1, obj.getRequisicao().getCodigo().intValue());
					} else {
						sqlInserir.setNull(1, 0);
					}
					if (obj.getResponsavel().getCodigo().intValue() != 0) {
						sqlInserir.setInt(2, obj.getResponsavel().getCodigo().intValue());
					} else {
						sqlInserir.setNull(2, 0);
					}
					sqlInserir.setDate(3, Uteis.getDataJDBC(obj.getData()));
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
			getFacadeFactory().getEntregaRequisicaoItemFacade().incluirEntregaRequisicaoItems(obj, usuario);
			getFacadeFactory().getRequisicaoFacade().alterarSituacaoEntrega(obj.getRequisicao().getCodigo());
			atualizarCentroResultadoEstoquePorTipoAutorizacaoRequisicaoEnumRetirada(obj, OperacaoEstoqueEnum.INCLUIR, usuario);
			
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setCodigo(0);
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}

	private void validarSaldoDisponivelPlanoOrcamentario(final EntregaRequisicaoVO obj, UsuarioVO usuario) throws Exception {
		if(getAplicacaoControle().getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(obj.getRequisicao().getUnidadeEnsino().getCodigo()).getUsaPlanoOrcamentario()) {
			List<Integer> listItemPlanoOrcamentario = new ArrayList<Integer>();

			for(EntregaRequisicaoItemVO entregaRequisicaoItemVO: obj.getEntregaRequisicaoItemVOs()) {					
				if(Uteis.isAtributoPreenchido(entregaRequisicaoItemVO.getRequisicaoItem().getItemSolicitacaoOrcamentoPlanoOrcamentarioVO())){
					if(!listItemPlanoOrcamentario.contains(entregaRequisicaoItemVO.getRequisicaoItem().getItemSolicitacaoOrcamentoPlanoOrcamentarioVO().getCodigo())) {
						listItemPlanoOrcamentario.add(entregaRequisicaoItemVO.getRequisicaoItem().getItemSolicitacaoOrcamentoPlanoOrcamentarioVO().getCodigo());
						entregaRequisicaoItemVO.getRequisicaoItem().setItemSolicitacaoOrcamentoPlanoOrcamentarioVO(getFacadeFactory().getItemSolicitacaoOrcamentoPlanoOrcamentarioFacade().consultarPorChavePrimaria(entregaRequisicaoItemVO.getRequisicaoItem().getItemSolicitacaoOrcamentoPlanoOrcamentarioVO().getCodigo(), usuario));
						if(entregaRequisicaoItemVO.getRequisicaoItem().getItemSolicitacaoOrcamentoPlanoOrcamentarioVO().getValorDisponivel() < 0 ) {
							throw new Exception("O saldo disponível no plano orçamentário para a categoria de despesa " 
									+ obj.getRequisicao().getCategoriaDespesa().getDescricao()+" ultrapassou em R$ " 
									+ Uteis.getDoubleFormatado(entregaRequisicaoItemVO.getRequisicaoItem().getItemSolicitacaoOrcamentoPlanoOrcamentarioVO().getValorDisponivel() *-1) + ", neste caso deve ser realizado o remanejamento de saldo no plano orçamentário do departamento " 
									+ obj.getRequisicao().getDepartamento().getNome()+".");
						}					
					}
				}
			}
		}
	}

	public void atualizarRequisicaoItem(EntregaRequisicaoVO obj, UsuarioVO usuario) throws Exception {
		obj.getRequisicao().setRequisicaoItemVOs(getFacadeFactory().getRequisicaoItemFacade().consultarRapidaRequisicaoItems(obj.getRequisicao(), null, Uteis.NIVELMONTARDADOS_TODOS, usuario));
		Iterator<RequisicaoItemVO> i = obj.getRequisicao().getRequisicaoItemVOs().iterator();
		while (i.hasNext()) {
			RequisicaoItemVO requisicaoItem = i.next();
			Optional<EntregaRequisicaoItemVO> entregaItemExistente = obj.getEntregaRequisicaoItemVOs().stream().filter(p -> p.getRequisicaoItem().getCodigo().equals(requisicaoItem.getCodigo())).findFirst();
			if (entregaItemExistente.isPresent() && Uteis.isAtributoPreenchido(entregaItemExistente.get())) {
				entregaItemExistente.get().setRequisicaoItem(requisicaoItem);
			}
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(EntregaRequisicaoVO obj, UsuarioVO usuario) throws Exception {
		try {
			EntregaRequisicao.excluir(getIdEntidade(), true, usuario);
			for (EntregaRequisicaoItemVO eri : obj.getEntregaRequisicaoItemVOs()) {
				if (eri.getRequisicaoItem().getProdutoServico().getControlarEstoque()) {
					Integer unidadeEnsino = Uteis.isAtributoPreenchido(eri.getRequisicaoItem().getUnidadeEnsinoEstoqueRetirada()) ? eri.getRequisicaoItem().getUnidadeEnsinoEstoqueRetirada().getCodigo() : eri.getEntregaRequisicaoVO().getRequisicao().getUnidadeEnsino().getCodigo();
					Estoque.estornaEstoque(eri.getCodigo().toString(), TipoOperacaoEstoqueOrigemEnum.ENTREGA_REQUISICAO_ITEM, eri.getRequisicaoItem().getProdutoServico().getCodigo(), eri.getQuantidade(), 0.0, null, unidadeEnsino, OperacaoEstoqueEnum.INCLUIR, usuario);
					eri.getRequisicaoItem().getUnidadeEnsinoEstoqueRetirada().setCodigo(unidadeEnsino);
				}
				eri.getRequisicaoItem().setQuantidadeEntregue(eri.getRequisicaoItem().getQuantidadeEntregue() - eri.getQuantidade());
				getFacadeFactory().getRequisicaoItemFacade().atualizarRequisitoItemPorEntregaRequisicaoItem(eri.getRequisicaoItem(), usuario);
			}
			getFacadeFactory().getRequisicaoFacade().alterarSituacaoEntrega(obj.getRequisicao().getCodigo());
			atualizarCentroResultadoEstoquePorTipoAutorizacaoRequisicaoEnumRetirada(obj, OperacaoEstoqueEnum.EXCLUIR, usuario);
			String sql = "DELETE FROM EntregaRequisicao WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(sql, obj.getCodigo());
		} catch (Exception e) {
			throw e;
		}
	}
	
	private void atualizarCentroResultadoEstoquePorTipoAutorizacaoRequisicaoEnumRetirada(EntregaRequisicaoVO obj, OperacaoEstoqueEnum operacaoEstoqueEnum, UsuarioVO usuario)  {
		try {
			Map<Integer, List<EntregaRequisicaoItemVO>> collect = obj.getEntregaRequisicaoItemVOs().stream()
					.filter(p -> p.getRequisicaoItem().getProdutoServico().getTipoProdutoServicoEnum().equals(TipoProdutoServicoEnum.PRODUTO)).collect(Collectors.groupingBy(p-> p.getCentroResultadoEstoque().getCodigo()));

			List<EntregaRequisicaoItemVO> listaRetirada = obj.getEntregaRequisicaoItemVOs().stream()
					.filter(p->Uteis.isAtributoPreenchido(p.getRequisicaoItem().getTipoAutorizacaoRequisicaoEnum()) && p.getRequisicaoItem().getTipoAutorizacaoRequisicaoEnum().isRetirada() && p.getRequisicaoItem().getProdutoServico().getControlarEstoque())
					.collect(Collectors.toList());

			if(Uteis.isAtributoPreenchido(listaRetirada)){
				for (Map.Entry<Integer, List<EntregaRequisicaoItemVO>> mapaCentroResultadoEstoque : collect.entrySet()) {
					realizarCalculoCentroResultadoEstoque(obj, mapaCentroResultadoEstoque, usuario);
					if(operacaoEstoqueEnum.isIncluir()){
						movimentarCentroResultadoEstoque(obj, obj.getCodigo().toString(), TipoCentroResultadoOrigemEnum.ENTREGA_REQUISICAO, mapaCentroResultadoEstoque.getKey(), TipoMovimentacaoCentroResultadoOrigemEnum.SAIDA, usuario);
						movimentarCentroResultadoEstoque(obj, obj.getRequisicao().getCodigo().toString(), TipoCentroResultadoOrigemEnum.REQUISICAO, obj.getRequisicao().getCentroResultadoAdministrativo().getCodigo(), TipoMovimentacaoCentroResultadoOrigemEnum.ENTRADA, usuario);
					}else{
						movimentarCentroResultadoEstoque(obj, obj.getCodigo().toString(), TipoCentroResultadoOrigemEnum.ENTREGA_REQUISICAO, mapaCentroResultadoEstoque.getKey(), TipoMovimentacaoCentroResultadoOrigemEnum.ENTRADA, usuario);
						movimentarCentroResultadoEstoque(obj, obj.getRequisicao().getCodigo().toString(), TipoCentroResultadoOrigemEnum.REQUISICAO, obj.getRequisicao().getCentroResultadoAdministrativo().getCodigo(), TipoMovimentacaoCentroResultadoOrigemEnum.SAIDA, usuario);	
					}	
				}
			}
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	private void realizarCalculoCentroResultadoEstoque(EntregaRequisicaoVO obj, Map.Entry<Integer, List<EntregaRequisicaoItemVO>> mapaCentroResultadoEstoque, UsuarioVO usuario) {
		try {
			Map<Double, List<OperacaoEstoqueVO>> collect = mapaCentroResultadoEstoque.getValue()
					.stream()
					.flatMap(p-> p.getListaOperacaoEstoqueVOs().stream())
					.collect(Collectors.groupingBy(p-> p.getEstoqueVO().getPrecoUnitario()));
			
			obj.setQuantidadeTotalEstoque(0.0);
			obj.setValorTotalEstoque(0.0);
			for (Map.Entry<Double, List<OperacaoEstoqueVO>> mapaOperacaoEstoqueVO : collect.entrySet()) {
				Double quantidadePorPreco =  mapaOperacaoEstoqueVO.getValue().stream().mapToDouble(OperacaoEstoqueVO::getQuantidade).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b));
				obj.setQuantidadeTotalEstoque(Uteis.arrendondarForcando2CadasDecimais(obj.getQuantidadeTotalEstoque() + quantidadePorPreco));
				obj.setValorTotalEstoque(Uteis.arrendondarForcando2CadasDecimais(obj.getValorTotalEstoque() + (quantidadePorPreco * mapaOperacaoEstoqueVO.getKey())));			
			}
			Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getQuantidadeTotalEstoque()), "A Quantidade Total do Estoque (CentroResultadoOrigem) deve ser informado.");
			Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getValorTotalEstoque()), "O Valor Total do Estoque (CentroResultadoOrigem) deve ser informado.");
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	private void movimentarCentroResultadoEstoque(EntregaRequisicaoVO obj, String codOrigem, TipoCentroResultadoOrigemEnum tipoCentroResultadoOrigemEnum,  Integer codigoCentroResultado, TipoMovimentacaoCentroResultadoOrigemEnum tipoMovimentacaoCentroResultadoOrigemEnum,  UsuarioVO usuario) throws Exception {
		CentroResultadoOrigemVO cro = new CentroResultadoOrigemVO();
		cro.setFuncionarioCargoVO(obj.getRequisicao().getFuncionarioCargoVO());
		cro.setTipoMovimentacaoCentroResultadoOrigemEnum(tipoMovimentacaoCentroResultadoOrigemEnum);
		cro.setTipoNivelCentroResultadoEnum(obj.getRequisicao().getTipoNivelCentroResultadoEnum());
		cro.setCategoriaDespesaVO(obj.getRequisicao().getCategoriaDespesa());
		cro.setUnidadeEnsinoVO(obj.getRequisicao().getUnidadeEnsino());
		cro.setDepartamentoVO(obj.getRequisicao().getDepartamento());
		cro.setCursoVO(obj.getRequisicao().getCurso());
		cro.setTurnoVO(obj.getRequisicao().getTurno());
		cro.setTurmaVO(obj.getRequisicao().getTurma());
		cro.getCentroResultadoAdministrativo().setCodigo(codigoCentroResultado);
		cro.setQuantidade(obj.getQuantidadeTotalEstoque());
		cro.setValor(obj.getValorTotalEstoque());
		cro.setPorcentagem(100.0);
		getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().persistir(cro, codOrigem, tipoCentroResultadoOrigemEnum, false, false, usuario, false);
	}

	@Override
	public void consultar(Integer unidadeEnsino, DataModelo dataModelo) {
		try {
			if (dataModelo.getCampoConsulta().equals("codigo")) {
				dataModelo.setListaConsulta(consultarPorCodigo(Uteis.getValorInteiro(dataModelo.getValorConsulta()), dataModelo.getDataIni(), dataModelo.getDataFim(), unidadeEnsino, dataModelo));
				dataModelo.getListaFiltros().clear();
				dataModelo.setTotalRegistrosEncontrados(consultarTotalPorCodigo(Uteis.getValorInteiro(dataModelo.getValorConsulta()), dataModelo.getDataIni(), dataModelo.getDataFim(), unidadeEnsino, dataModelo));
			} else if (dataModelo.getCampoConsulta().equals("codigoRequisicao")) {
				dataModelo.setListaConsulta(consultarPorCodigoRequisicao(Uteis.getValorInteiro(dataModelo.getValorConsulta()), dataModelo.getDataIni(), dataModelo.getDataFim(), unidadeEnsino, dataModelo));
				dataModelo.getListaFiltros().clear();
				dataModelo.setTotalRegistrosEncontrados(consultarTotalPorCodigoRequisicao(Uteis.getValorInteiro(dataModelo.getValorConsulta()), dataModelo.getDataIni(), dataModelo.getDataFim(), unidadeEnsino, dataModelo));
			} else if (dataModelo.getCampoConsulta().equals("nomeUsuario")) {
				dataModelo.setListaConsulta(consultarPorNomeUsuario(dataModelo.getValorConsulta(), dataModelo.getDataIni(), dataModelo.getDataFim(), unidadeEnsino, dataModelo));
				dataModelo.getListaFiltros().clear();
				dataModelo.setTotalRegistrosEncontrados(consultarTotalPorNomeUsuario(dataModelo.getValorConsulta(), dataModelo.getDataIni(), dataModelo.getDataFim(), unidadeEnsino, dataModelo));
			}
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	private StringBuilder getSQLPadraoConsultaBasica() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT EntregaRequisicao.codigo as \"EntregaRequisicao.codigo\", EntregaRequisicao.data as \"EntregaRequisicao.data\", ");
		sql.append(" requisicao.codigo as \"requisicao.codigo\",  ");
		sql.append(" usuario.codigo as \"usuario.codigo\", usuario.nome as \"usuario.nome\"  ");
		sql.append(" FROM EntregaRequisicao ");
		sql.append(" INNER JOIN requisicao ON requisicao.codigo = EntregaRequisicao.requisicao ");
		sql.append(" INNER JOIN usuario ON EntregaRequisicao.responsavel = usuario.codigo ");
		return sql;
	}

	private StringBuilder getSQLPadraoConsultaTotalBasica() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT count(EntregaRequisicao.codigo) as qtde FROM EntregaRequisicao  ");
		sql.append(" INNER JOIN requisicao ON requisicao.codigo = EntregaRequisicao.requisicao ");
		sql.append(" INNER JOIN usuario ON EntregaRequisicao.responsavel = Usuario.codigo ");
		return sql;
	}

	private void montarFiltrosParaConsulta(Date dataIni, Date dataFim, Integer unidadeEnsino, StringBuilder sqlStr) {
		if (Uteis.isAtributoPreenchido(dataIni) && Uteis.isAtributoPreenchido(dataFim)) {
			sqlStr.append(" AND EntregaRequisicao.data between '").append(Uteis.getDataJDBC(dataIni)).append("' and '").append(Uteis.getDataJDBC(dataFim)).append("' ");
		}
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sqlStr.append(" and requisicao.unidadeEnsino =   ").append(unidadeEnsino);
		}
	}

	public List<EntregaRequisicaoVO> consultarPorNomeUsuario(String valorConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, DataModelo dataModelo) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE upper( usuario.nome ) like(?) ");
		dataModelo.getListaFiltros().add(PERCENT + valorConsulta + PERCENT);
		montarFiltrosParaConsulta(dataIni, dataFim, unidadeEnsino, sqlStr);
		sqlStr.append(" ORDER BY usuario.nome ");
		UteisTexto.addLimitAndOffset(sqlStr, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), dataModelo.getListaFiltros().toArray());
		return (montarDadosConsultaBasica(tabelaResultado, dataModelo.getNivelMontarDados(), dataModelo.getUsuario()));
	}

	public Integer consultarTotalPorNomeUsuario(String valorConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, DataModelo dataModelo) {
		try {
			StringBuilder sqlStr = getSQLPadraoConsultaTotalBasica();
			sqlStr.append(" WHERE upper( usuario.nome ) like(?) ");
			dataModelo.getListaFiltros().add(PERCENT + valorConsulta + PERCENT);
			montarFiltrosParaConsulta(dataIni, dataFim, unidadeEnsino, sqlStr);
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), dataModelo.getListaFiltros().toArray());
			return (Integer) Uteis.getSqlRowSetTotalizador(rs, "qtde", TipoCampoEnum.INTEIRO);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	public List<EntregaRequisicaoVO> consultarPorCodigoRequisicao(Integer valorConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, DataModelo dataModelo) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		if(Uteis.isAtributoPreenchido(valorConsulta)){
			sqlStr.append(" WHERE Requisicao.codigo =  ").append(valorConsulta);	
		}else{
			montarFiltrosParaConsulta(dataIni, dataFim, unidadeEnsino, sqlStr);	
		}
		sqlStr.append(" ORDER BY Requisicao.codigo");
		UteisTexto.addLimitAndOffset(sqlStr, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsultaBasica(tabelaResultado, dataModelo.getNivelMontarDados(), dataModelo.getUsuario()));
	}

	public Integer consultarTotalPorCodigoRequisicao(Integer valorConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, DataModelo dataModelo) {
		try {
			StringBuilder sqlStr = getSQLPadraoConsultaTotalBasica();
			if(Uteis.isAtributoPreenchido(valorConsulta)){
				sqlStr.append(" WHERE Requisicao.codigo =  ").append(valorConsulta);	
			}else{
				montarFiltrosParaConsulta(dataIni, dataFim, unidadeEnsino, sqlStr);	
			}
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			return (Integer) Uteis.getSqlRowSetTotalizador(rs, "qtde", TipoCampoEnum.INTEIRO);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	public List<EntregaRequisicaoVO> consultarPorCodigo(Integer valorConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, DataModelo dataModelo) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE EntregaRequisicao.codigo >=  ").append(valorConsulta);
		montarFiltrosParaConsulta(dataIni, dataFim, unidadeEnsino, sqlStr);
		sqlStr.append(" ORDER BY EntregaRequisicao.codigo");
		UteisTexto.addLimitAndOffset(sqlStr, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsultaBasica(tabelaResultado, dataModelo.getNivelMontarDados(), dataModelo.getUsuario()));
	}

	public Integer consultarTotalPorCodigo(Integer valorConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, DataModelo dataModelo) {
		try {
			StringBuilder sqlStr = getSQLPadraoConsultaTotalBasica();
			sqlStr.append(" WHERE EntregaRequisicao.codigo >=  ").append(valorConsulta);
			montarFiltrosParaConsulta(dataIni, dataFim, unidadeEnsino, sqlStr);
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			return (Integer) Uteis.getSqlRowSetTotalizador(rs, "qtde", TipoCampoEnum.INTEIRO);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<EntregaRequisicaoVO> montarDadosConsultaBasica(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) {
		List<EntregaRequisicaoVO> vetResultado = new ArrayList<>();
		while (tabelaResultado.next()) {
			EntregaRequisicaoVO obj = new EntregaRequisicaoVO();
			montarDadosBasico(obj, tabelaResultado, nivelMontarDados, usuario);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	private void montarDadosBasico(EntregaRequisicaoVO obj, SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) {
		try {
			obj.setNovoObj(Boolean.FALSE);
			obj.setCodigo(dadosSQL.getInt("EntregaRequisicao.codigo"));
			obj.setData(dadosSQL.getDate("EntregaRequisicao.data"));
			obj.getRequisicao().setCodigo((dadosSQL.getInt("requisicao.codigo")));
			obj.getResponsavel().setCodigo((dadosSQL.getInt("usuario.codigo")));
			obj.getResponsavel().setNome((dadosSQL.getString("usuario.nome")));
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	public static List<EntregaRequisicaoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<EntregaRequisicaoVO> vetResultado = new ArrayList<>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	public static EntregaRequisicaoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		EntregaRequisicaoVO obj = new EntregaRequisicaoVO();
		obj.setCodigo((dadosSQL.getInt("codigo")));
		obj.getRequisicao().setCodigo((dadosSQL.getInt("requisicao")));
		obj.getResponsavel().setCodigo((dadosSQL.getInt("responsavel")));
		obj.setData(dadosSQL.getDate("data"));
		obj.setNovoObj(Boolean.FALSE);
		montarDadosResponsavel(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			return obj;
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		obj.setEntregaRequisicaoItemVOs(getFacadeFactory().getEntregaRequisicaoItemFacade().consultarEntregaRequisicaoItems(obj.getCodigo(), nivelMontarDados, usuario));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS) {
			return obj;
		}
		montarDadosRequisicao(obj, nivelMontarDados, usuario);
		return obj;
	}

	public static void montarDadosResponsavel(EntregaRequisicaoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getResponsavel().getCodigo().intValue() == 0) {
			obj.setResponsavel(new UsuarioVO());
			return;
		}
		obj.setResponsavel(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavel().getCodigo(), nivelMontarDados, usuario));
	}

	public static void montarDadosRequisicao(EntregaRequisicaoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getRequisicao().getCodigo().intValue() == 0) {
			obj.setRequisicao(new RequisicaoVO());
			return;
		}
		obj.setRequisicao(getFacadeFactory().getRequisicaoFacade().consultarPorChavePrimaria(obj.getRequisicao().getCodigo(), 0, false, nivelMontarDados, usuario));
	}

	public EntregaRequisicaoVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sql = "SELECT * FROM EntregaRequisicao WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, codigoPrm);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( EntregaRequisicao ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	public static String getIdEntidade() {
		return EntregaRequisicao.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		EntregaRequisicao.idEntidade = idEntidade;
	}
}
