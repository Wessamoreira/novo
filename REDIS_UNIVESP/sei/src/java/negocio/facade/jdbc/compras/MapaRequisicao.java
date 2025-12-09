package negocio.facade.jdbc.compras;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.EstatisticaRequisicaoVO;
import negocio.comuns.compras.ItemCronologicoEstatisticaVO;
import negocio.comuns.compras.ItemSumarioUnidadeEstatisticaVO;
import negocio.comuns.compras.MapaRequisicaoVO;
import negocio.comuns.compras.RequisicaoItemVO;
import negocio.comuns.compras.RequisicaoVO;
import negocio.comuns.compras.enumeradores.TipoAutorizacaoRequisicaoEnum;
import negocio.comuns.compras.enumeradores.TipoProdutoServicoEnum;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.compras.MapaRequisicaoInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class MapaRequisicao extends ControleAcesso implements MapaRequisicaoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -807237423878719813L;
	protected static String idEntidade;

	public MapaRequisicao() {
		super();
		setIdEntidade("MapaRequisicao");
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public synchronized void autorizarTodas(List<MapaRequisicaoVO> lista, TipoAutorizacaoRequisicaoEnum tipoAutorizacaoRequisicaoEnum, UsuarioVO responsavel, ConfiguracaoFinanceiroVO confFinanceiro, UsuarioVO usuario) throws Exception {
		for (MapaRequisicaoVO obj : lista) {
			getFacadeFactory().getRequisicaoFacade().verificarBloqueioRequisicao(Uteis.BLOQUEIO_GERAR, true, obj.getRequisicao());
			obj.setRequisicao(getFacadeFactory().getRequisicaoFacade().consultarPorChavePrimaria(obj.getRequisicao().getCodigo(), 0, false, Uteis.NIVELMONTARDADOS_TODOS, usuario));
			if (!obj.getRequisicao().getAutorizado()) {
				obj.setarQtdeAprovar();
				obj.getRequisicao().getRequisicaoItemVOs().forEach(p-> p.setTipoAutorizacaoRequisicaoEnum(tipoAutorizacaoRequisicaoEnum));
				autorizar(obj, responsavel, confFinanceiro, usuario);
			}
			getFacadeFactory().getRequisicaoFacade().verificarBloqueioRequisicao(Uteis.BLOQUEIO_GERAR, false, obj.getRequisicao());
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public synchronized void autorizar(MapaRequisicaoVO obj, UsuarioVO responsavel, ConfiguracaoFinanceiroVO confFinanceiro, UsuarioVO usuario) throws Exception {
		
		processarAutorizacao(obj, responsavel, confFinanceiro, usuario);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public synchronized void desfazerAutorizar(MapaRequisicaoVO obj, ConfiguracaoFinanceiroVO confFinanceiro, UsuarioVO usuario) throws Exception {
		String situacaoEntrega = getFacadeFactory().getRequisicaoFacade().consultarSituacaoEntregaPorCodigo(obj.getRequisicao().getCodigo(), false, usuario);
		if (situacaoEntrega.equals("FI")) {
			throw new StreamSeiException("Não é possível desfazer a Autorização da Requisição que esteja Entregue (Situação Entrega: Finalizada).");
		} else if (situacaoEntrega.equals("PA")) {
			throw new StreamSeiException("Não é possível desfazer a Autorização da Requisição que esteja Parcialmente Entregue (Situação Entrega: Parcial).");
		}
		for (RequisicaoItemVO objRequisicaoItem : obj.getRequisicao().getRequisicaoItemVOs()) {			
			if (Uteis.isAtributoPreenchido(objRequisicaoItem.getCotacao())) {
				throw new StreamSeiException("Não é possível desfazer a Autorização da Requisição que esteja Vinculada a uma Cotação.");
			}
			
		}
		String situacao = getFacadeFactory().getRequisicaoFacade().consultarSituacaoAutorizacaoPorCodigo(obj.getRequisicao().getCodigo(), false, usuario);
			if (situacao.equals("AU")) {
			obj.getRequisicao().setSituacaoAutorizacao("PE");
			obj.getRequisicao().setSituacaoEntrega("PE");
			obj.getRequisicao().setResponsavelAutorizacao(null);
			obj.getRequisicao().setDataAutorizacao(null);
			obj.getRequisicao().getRequisicaoItemVOs().forEach(p -> {
				p.setQuantidadeAutorizada(0.0);
				p.setUnidadeEnsinoEstoqueRetirada(obj.getRequisicao().getUnidadeEnsino());
				p.setItemSolicitacaoOrcamentoPlanoOrcamentarioVO(null);
			});
			getFacadeFactory().getRequisicaoFacade().persistir(obj.getRequisicao(), false, usuario, false);
		} else {
			obj.getRequisicao().setSituacaoAutorizacao("PE");
			obj.getRequisicao().setSituacaoEntrega("PE");
			obj.getRequisicao().setResponsavelAutorizacao(null);
			obj.getRequisicao().setDataAutorizacao(null);
			getFacadeFactory().getRequisicaoFacade().persistir(obj.getRequisicao(), false, usuario);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public synchronized void indeferir(MapaRequisicaoVO obj, UsuarioVO responsavel, ConfiguracaoFinanceiroVO confFinanceiro, UsuarioVO usuario) throws Exception {
		try {
			obj.getRequisicao().setResponsavelAutorizacao(responsavel);
			obj.getRequisicao().setDataAutorizacao(new Date());
			obj.getRequisicao().setSituacaoAutorizacao("IN"); // indeferido
			obj.getRequisicao().setSituacaoEntrega("PE"); // pendente
			MapaRequisicaoVO.validarDados(obj);
			getFacadeFactory().getRequisicaoFacade().persistir(obj.getRequisicao(), false, usuario, false);
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public synchronized void processarAutorizacao(MapaRequisicaoVO obj, UsuarioVO responsavel, ConfiguracaoFinanceiroVO confFinanceiro, UsuarioVO usuario) throws Exception {
		String situacao = getFacadeFactory().getRequisicaoFacade().consultarSituacaoAutorizacaoPorCodigo(obj.getRequisicao().getCodigo(), false, usuario);
		try {
		if (situacao.equals("PE")) {
			obj.getRequisicao().setResponsavelAutorizacao(responsavel);
			obj.getRequisicao().setDataAutorizacao(new Date());
			obj.getRequisicao().setSituacaoAutorizacao("AU");
			obj.getRequisicao().setSituacaoEntrega("PE");
			obj.getRequisicao().setHabilitarBotaoLiberacaoValorMaximoCompraDiretaRequisicao(obj.getRequisicao().isExisteRequisicaoItemPorCompraDireta() 
					&& !obj.getRequisicao().isLiberadoValorMaximoCompraDireta()
					&& Uteis.isAtributoPreenchido(confFinanceiro.getValorMaximoCompraDiretaRequisicao()) 
					&& obj.getRequisicao().getValorTotalRequisicaoPorCompraDireta() > confFinanceiro.getValorMaximoCompraDiretaRequisicao());
			//Uteis.checkState(obj.getRequisicao().isHabilitarBotaoLiberacaoValorMaximoCompraDiretaRequisicao(), "Os Itens da Requisição que são do Tipo Autorização Por Compra Direta ultrapassou o limite configurado. Sendo assim não é possível realizar essa operação.");			
			if (obj.getRequisicao().isExisteRequisicaoItemPorRetirada()) {				
				boolean existeQtdAutorizadoMAiorQueEstoque = obj.getRequisicao().getRequisicaoItemVOs().stream().anyMatch(p -> p.getTipoAutorizacaoRequisicaoEnum().isRetirada() && p.getProdutoServico().getTipoProdutoServicoEnum().equals(TipoProdutoServicoEnum.PRODUTO) && p.getQuantidadeAutorizada() > p.getQuantidadeEstoque());
				Uteis.checkState(existeQtdAutorizadoMAiorQueEstoque, "Existe Itens da Requisição que são do Tipo Autorização Por Retirada que a Quantidade Estoque é menor que a Quantidade Autorizada. Sendo assim não é possível realizar essa operação.");
			}
			MapaRequisicaoVO.validarDados(obj);
			getFacadeFactory().getRequisicaoFacade().persistir(obj.getRequisicao(), false, usuario, confFinanceiro.getUsaPlanoOrcamentario());
		
		}else {
			throw new StreamSeiException("Não é possivel autorizar uma Requisição que não esteja com a situação Pendente.");
		}
	} catch (Exception e) {
		obj.getRequisicao().setResponsavelAutorizacao(new UsuarioVO());
		obj.getRequisicao().setDataAutorizacao(null);
		obj.getRequisicao().setSituacaoAutorizacao(situacao);
		throw e;
	}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public synchronized void realizarEstornoBaixaPlanoOrcamentario(RequisicaoVO obj, UsuarioVO usuario) throws Exception {
		Double valorTotal = 0.0;
		DepartamentoVO departamentoVO = obj.getDepartamento();
		
		valorTotal = getFacadeFactory().getRequisicaoFacade().validarDadosEstornoBaixaPlanoOrcamentario(obj, usuario, departamentoVO, valorTotal);

		getFacadeFactory().getPlanoOrcamentarioFacade().atualizarEstornoOrcamentoTotalRealizado(valorTotal, obj.getDataAutorizacao(), departamentoVO.getCodigo(), obj.getUnidadeEnsino().getCodigo(), usuario);
		getFacadeFactory().getDetalhamentoPlanoOrcamentarioFacade().atualizarEstornoValoConsumidoDetalhamentoPlanoOrcamentario(valorTotal, departamentoVO.getCodigo(), obj.getUnidadeEnsino().getCodigo(), obj.getDataAutorizacao(), usuario);
		getFacadeFactory().getItemMensalDetalhamentoPlanoOrcamentarioFacade().atualizarEstornoValorConsumidoMes(valorTotal, departamentoVO.getCodigo(), obj.getUnidadeEnsino().getCodigo(), usuario);
	}

	/*@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void gerarContaPagarParaRequisicaoItem(RequisicaoVO requisicao, RequisicaoItemVO item, UsuarioVO usuario) throws Exception {
		ContaPagarVO contaPagar = new ContaPagarVO();
		contaPagar.setTipoOrigem("RE");
		contaPagar.setCodOrigem(requisicao.getCodigo().toString());
		contaPagar.setTipoSacado(requisicao.getTipoSacado());
		contaPagar.setUnidadeEnsino(requisicao.getUnidadeEnsino());
		contaPagar.setFornecedor(requisicao.getSacadoFornecedor());
		contaPagar.setFuncionario(requisicao.getSacadoFuncionario());
		contaPagar.setValor(item.getValorUnitario() * item.getQuantidadeAutorizada());
		contaPagar.setCentroDespesa(requisicao.getCategoriaDespesa());
		contaPagar.setNrDocumento(requisicao.getCodigo() + "." + item.getCodigo());
		contaPagar.setDataVencimento(item.getDataPrevisaoPagamento());
		contaPagar.setUnidadeEnsino(requisicao.getUnidadeEnsino());
		contaPagar.setDepartamento(requisicao.getDepartamento());
		contaPagar.setTurma(requisicao.getTurma());
		if (requisicao.getResponsavelRequisicao().getCodigo().intValue() != 0) {
			UsuarioVO usuarioComPessoa = getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(requisicao.getResponsavelRequisicao().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario);
			FuncionarioVO fun = getFacadeFactory().getFuncionarioFacade().consultarPorCodigoPessoa(usuarioComPessoa.getPessoa().getCodigo(), 0, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			contaPagar.setPessoa(usuarioComPessoa.getPessoa());
			contaPagar.setFuncionarioCentroCusto(fun);
		}
		getFacadeFactory().getContaPagarFacade().incluir(contaPagar, false, usuario);
	}*/

	@Override
	public List<MapaRequisicaoVO> consultar(RequisicaoVO requisicaoFiltro, Date dataIni, Date dataFim, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = new StringBuilder(" ");
		sql.append(" SELECT ");
		sql.append(" requisicao.codigo as \"requisicao.codigo\", requisicao.dataRequisicao as \"requisicao.dataRequisicao\",  requisicao.dataNecessidadeRequisicao as \"requisicao.dataNecessidadeRequisicao\",   ");
		sql.append(" requisicao.situacaoEntrega as \"requisicao.situacaoEntrega\", requisicao.situacaoAutorizacao as \"requisicao.situacaoAutorizacao\",   ");
		//sql.append(" requisicao.tipoAutorizacaoRequisicao as \"requisicao.tipoAutorizacaoRequisicao\", ");
		sql.append(" responsavel.codigo as \"responsavel.codigo\", responsavel.nome as \"responsavel.nome\",   ");
		sql.append(" solicitante.codigo as \"solicitante.codigo\", solicitante.nome as \"solicitante.nome\",   ");
		sql.append(" categoriaproduto.codigo as \"categoriaproduto.codigo\", categoriaproduto.nome as \"categoriaproduto.nome\",   ");
		sql.append(" unidadeensino.codigo as \"unidadeensino.codigo\", unidadeensino.nome as \"unidadeensino.nome\",   ");
		sql.append(" categoriaDespesa.codigo as \"categoriaDespesa.codigo\", categoriaDespesa.descricao as \"categoriaDespesa.descricao\",   ");
		sql.append(" centroresultadoadministrativo.codigo as \"centroresultadoadministrativo.codigo\", centroresultadoadministrativo.descricao as \"centroresultadoadministrativo.descricao\", centroresultadoadministrativo.identificadorCentroResultado as \"centroresultadoadministrativo.identificadorCentroResultado\" ");
		sql.append(" FROM requisicao ");
		sql.append(" INNER JOIN usuario AS responsavel ON requisicao.responsavelrequisicao = responsavel.codigo ");
		sql.append(" INNER JOIN usuario AS solicitante ON requisicao.solicitanterequisicao = solicitante.codigo ");
		sql.append(" INNER JOIN categoriaDespesa ON requisicao.categoriaDespesa = categoriaDespesa.codigo ");
		sql.append(" INNER JOIN categoriaproduto ON requisicao.categoriaproduto = categoriaproduto.codigo ");
		sql.append(" INNER JOIN unidadeensino ON requisicao.unidadeensino = unidadeensino.codigo ");
		sql.append(" LEFT JOIN centroresultado AS centroresultadoadministrativo ON requisicao.centroresultadoadministrativo = centroresultadoadministrativo.codigo ");
		sql.append(" WHERE 1=1 ");
		montarFiltrosConsulta(requisicaoFiltro, dataIni, dataFim, sql);
		sql.append(" ORDER BY requisicao.codigo ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaBasica(tabelaResultado, usuario);

	}

	private void montarFiltrosConsulta(RequisicaoVO requisicaoFiltro, Date dataIni, Date dataFim, StringBuilder stringJoiner) {
		if (Uteis.isAtributoPreenchido(requisicaoFiltro.getCodigo())) {
			stringJoiner.append(String.format(" and requisicao.codigo = %s", requisicaoFiltro.getCodigo().toString()));
			return;
		}
		if (Uteis.isAtributoPreenchido(requisicaoFiltro.getResponsavelRequisicao().getNome())) {
			stringJoiner.append(String.format(" and lower(sem_acentos(responsavel.nome)) ilike sem_acentos('%%%s%%')", requisicaoFiltro.getResponsavelRequisicao().getNome().toLowerCase()));
		}
		if (Uteis.isAtributoPreenchido(requisicaoFiltro.getSolicitanteRequisicao().getNome())) {
			stringJoiner.append(String.format(" and lower(sem_acentos(solicitante.nome)) ilike sem_acentos('%%%s%%')", requisicaoFiltro.getSolicitanteRequisicao().getNome().toLowerCase()));
		}
		if (Uteis.isAtributoPreenchido(requisicaoFiltro.getCategoriaProduto().getNome())) {
			stringJoiner.append(String.format(" and lower(sem_acentos(categoriaproduto.nome)) ilike sem_acentos('%%%s%%')", requisicaoFiltro.getCategoriaProduto().getNome().toLowerCase()));
		}
		if (Uteis.isAtributoPreenchido(requisicaoFiltro.getUnidadeEnsino())) {
			stringJoiner.append(String.format(" and unidadeensino.codigo = %s", requisicaoFiltro.getUnidadeEnsino().getCodigo().toString()));
		}
		if (Uteis.isAtributoPreenchido(dataIni) && Uteis.isAtributoPreenchido(dataFim)) {
			stringJoiner.append(String.format(" and requisicao.dataRequisicao BETWEEN '%s'  AND '%s'", Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(dataIni), Uteis.getDataComHoraSetadaParaUltimoMinutoDia(dataFim)));
		}

		if (Uteis.isAtributoPreenchido(requisicaoFiltro.getProdutoCategoriaPesquisa())) {
			stringJoiner.append(" and requisicao.codigo in (");
			stringJoiner.append(" select distinct requisicao from requisicaoitem");
			stringJoiner.append(" inner join produtoservico on requisicaoitem.produtoservico = produtoservico.codigo");
			stringJoiner.append(String.format(" where lower(sem_acentos(produtoservico.nome)) ilike sem_acentos('%%%s%%'))", requisicaoFiltro.getProdutoCategoriaPesquisa().toLowerCase()));
		}
		if (Uteis.isAtributoPreenchido(requisicaoFiltro.getSituacaoAutorizacao())) {
			stringJoiner.append(String.format(" and requisicao.situacaoAutorizacao = '%s'", requisicaoFiltro.getSituacaoAutorizacao()));
		}
		if (Uteis.isAtributoPreenchido(requisicaoFiltro.getSituacaoEntrega())) {
			if(requisicaoFiltro.getSituacaoEntrega().equals("PEPA")) {
				stringJoiner.append(" and requisicao.situacaoEntrega IN('PE','PA') ");				
			}
			else {
				stringJoiner.append(String.format(" and requisicao.situacaoEntrega = '%s'", requisicaoFiltro.getSituacaoEntrega()));
			}
			
		} 
		if (requisicaoFiltro.getAutorizado() && (Uteis.isAtributoPreenchido(requisicaoFiltro.getFiltroTipoAutorizacaoRequisicaoEnum()) 
				&& (requisicaoFiltro.getFiltroTipoAutorizacaoRequisicaoEnum().equals(TipoAutorizacaoRequisicaoEnum.RETIRADA.name()) 
						|| (requisicaoFiltro.getFiltroTipoAutorizacaoRequisicaoEnum().equals(TipoAutorizacaoRequisicaoEnum.COMPRA_DIRETA.name()) 
								|| requisicaoFiltro.getFiltroTipoAutorizacaoRequisicaoEnum().equals(TipoAutorizacaoRequisicaoEnum.COTACAO.name()) 
								&& Uteis.isAtributoPreenchido(requisicaoFiltro.getSituacaoTipoAutorizacaoRequisicaoEnum()))))) {
			stringJoiner.append(" and exists (select requisicaoitem.codigo from requisicaoitem where requisicaoitem.requisicao = requisicao.codigo ");
			if (requisicaoFiltro.getAutorizado() && Uteis.isAtributoPreenchido(requisicaoFiltro.getFiltroTipoAutorizacaoRequisicaoEnum()) && !requisicaoFiltro.getFiltroTipoAutorizacaoRequisicaoEnum().equals(TipoAutorizacaoRequisicaoEnum.NENHUM.name()) ) {
				stringJoiner.append(String.format(" and requisicaoitem.tipoAutorizacaoRequisicao = '%s'", requisicaoFiltro.getFiltroTipoAutorizacaoRequisicaoEnum()));
			}
			if((requisicaoFiltro.getFiltroTipoAutorizacaoRequisicaoEnum().equals(TipoAutorizacaoRequisicaoEnum.COMPRA_DIRETA.name()) 
					|| requisicaoFiltro.getFiltroTipoAutorizacaoRequisicaoEnum().equals(TipoAutorizacaoRequisicaoEnum.COTACAO.name()) 
					&& Uteis.isAtributoPreenchido(requisicaoFiltro.getSituacaoTipoAutorizacaoRequisicaoEnum()))) {
			if (requisicaoFiltro.getSituacaoTipoAutorizacaoRequisicaoEnum().equals("AC")) {//Aguardando Cotacao
					stringJoiner.append(" and (requisicaoitem.cotacao is null or requisicaoitem.cotacao = 0)) ");
			}else if (requisicaoFiltro.getSituacaoTipoAutorizacaoRequisicaoEnum().equals("CO")) {//Cotado
				stringJoiner.append(" and requisicaoitem.cotacao > 0) ");
			}else if (requisicaoFiltro.getSituacaoTipoAutorizacaoRequisicaoEnum().equals("AD")) {//Aguardando Compra Direta
				stringJoiner.append(" and (requisicaoitem.compraitem is null or requisicaoitem.compraitem = 0)) ");
			}else {//Comprado
				stringJoiner.append(" and requisicaoitem.compraitem > 0) ");
			}
			}
			else {
				stringJoiner.append(")");
			}
		}	
	}

	private List<MapaRequisicaoVO> montarDadosConsultaBasica(SqlRowSet tabelaResultado, UsuarioVO usuario) {
		List<MapaRequisicaoVO> vetResultado = new ArrayList<>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDadosBasica(tabelaResultado, usuario));
		}
		return vetResultado;
	}

	private MapaRequisicaoVO montarDadosBasica(SqlRowSet dadosSQL, UsuarioVO usuario) {
		MapaRequisicaoVO obj = new MapaRequisicaoVO();
		obj.getRequisicao().setCodigo((dadosSQL.getInt("requisicao.codigo")));
		obj.getRequisicao().setDataRequisicao(dadosSQL.getTimestamp("requisicao.dataRequisicao"));
		obj.getRequisicao().setSituacaoEntrega(dadosSQL.getString("requisicao.situacaoEntrega"));
		obj.getRequisicao().setDataNecessidadeRequisicao(dadosSQL.getDate("requisicao.dataNecessidadeRequisicao"));
		obj.getRequisicao().setSituacaoAutorizacao(dadosSQL.getString("requisicao.situacaoAutorizacao"));

		obj.getRequisicao().getResponsavelRequisicao().setCodigo((dadosSQL.getInt("responsavel.codigo")));
		obj.getRequisicao().getResponsavelRequisicao().setNome((dadosSQL.getString("responsavel.nome")));

		obj.getRequisicao().getSolicitanteRequisicao().setCodigo((dadosSQL.getInt("solicitante.codigo")));
		obj.getRequisicao().getSolicitanteRequisicao().setNome((dadosSQL.getString("solicitante.nome")));

		obj.getRequisicao().getCategoriaProduto().setCodigo((dadosSQL.getInt("categoriaProduto.codigo")));
		obj.getRequisicao().getCategoriaProduto().setNome((dadosSQL.getString("categoriaProduto.nome")));

		obj.getRequisicao().getCategoriaDespesa().setCodigo((dadosSQL.getInt("categoriaDespesa.codigo")));
		obj.getRequisicao().getCategoriaDespesa().setDescricao((dadosSQL.getString("categoriaDespesa.descricao")));

		obj.getRequisicao().getUnidadeEnsino().setCodigo((dadosSQL.getInt("unidadeEnsino.codigo")));
		obj.getRequisicao().getUnidadeEnsino().setNome((dadosSQL.getString("unidadeEnsino.nome")));

		obj.getRequisicao().getCentroResultadoAdministrativo().setCodigo((dadosSQL.getInt("centroResultadoAdministrativo.codigo")));
		obj.getRequisicao().getCentroResultadoAdministrativo().setDescricao((dadosSQL.getString("centroResultadoAdministrativo.descricao")));
		obj.getRequisicao().getCentroResultadoAdministrativo().setIdentificadorCentroResultado((dadosSQL.getString("centroResultadoAdministrativo.identificadorCentroResultado")));
		
		/*if (Uteis.isAtributoPreenchido(dadosSQL.getString("requisicao.tipoAutorizacaoRequisicao"))) {
			obj.getRequisicao().setTipoAutorizacaoRequisicaoEnum(TipoAutorizacaoRequisicaoEnum.valueOf(dadosSQL.getString("requisicao.tipoAutorizacaoRequisicao")));
		}*/
		return obj;
	}

	public static String getIdEntidade() {
		return MapaRequisicao.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		MapaRequisicao.idEntidade = idEntidade;
	}

	private ItemCronologicoEstatisticaVO obterItemCronologicoEstatisticaRequisicaoVO(Date dataBusca, String descricao, UsuarioVO usuario) throws Exception {
		ItemCronologicoEstatisticaVO diaEstatistica = new ItemCronologicoEstatisticaVO();
		diaEstatistica.setDescricao(descricao);
		diaEstatistica.setDataInicial(Uteis.getDateSemHora(dataBusca));
		diaEstatistica.setDataFinal(Uteis.getDateSemHora(dataBusca));
		try {
			ControleAcesso.consultar(getIdEntidade(), false, usuario);
			String sql = "SELECT DATE_PART('YEAR',Requisicao.dataRequisicao) as ano, " + "DATE_PART('MONTH',Requisicao.dataRequisicao) as mes, " + "DATE_PART('DAY',Requisicao.dataRequisicao) as dia, " + "COUNT(Requisicao.codigo) as nrRequisicoes " + "FROM Requisicao " + "WHERE (DATE_PART('YEAR',Requisicao.dataRequisicao) = ?) and " + "(DATE_PART('MONTH',Requisicao.dataRequisicao) = ?) and " + "(DATE_PART('DAY',Requisicao.dataRequisicao) = ?) and  Requisicao.situacaoautorizacao = 'PE'" + "GROUP BY ano, mes, dia " + "ORDER BY ano, mes, dia;";

			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { Uteis.getAnoData(dataBusca), Uteis.getMesData(dataBusca), Uteis.getDiaMesData(dataBusca) });

			if (!tabelaResultado.next()) {
				diaEstatistica.setQuantidade(0);
			} else {
				diaEstatistica.setQuantidade(tabelaResultado.getInt("nrRequisicoes"));
			}
		} catch (Exception e) {
			diaEstatistica.setQuantidade(0);
		}
		return diaEstatistica;
	}

	private ItemCronologicoEstatisticaVO obterItemCronologicoEstatisticaRequisicaoVOAnterioresData(Date dataBusca, String descricao, UsuarioVO usuario) throws Exception {
		ItemCronologicoEstatisticaVO diaEstatistica = new ItemCronologicoEstatisticaVO();
		diaEstatistica.setDescricao(descricao);
		diaEstatistica.setDataInicial(Uteis.getDate("01/01/1900"));
		diaEstatistica.setDataFinal(Uteis.getDateSemHora(Uteis.obterDataFutura(dataBusca, -1)));
		dataBusca = Uteis.getDateSemHora(dataBusca);
		try {
			ControleAcesso.consultar(getIdEntidade(), false, usuario);
			String sql = "SELECT COUNT(Requisicao.codigo) as nrRequisicoes " + "FROM Requisicao " + "WHERE (Requisicao.dataRequisicao < ?) and situacaoautorizacao = 'PE' ";

			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { Uteis.getDataJDBC(dataBusca) });
			if (!tabelaResultado.next()) {
				diaEstatistica.setQuantidade(0);
			} else {
				diaEstatistica.setQuantidade(tabelaResultado.getInt("nrRequisicoes"));
			}
		} catch (Exception e) {
			diaEstatistica.setQuantidade(0);
		}
		return diaEstatistica;
	}

	private List<ItemSumarioUnidadeEstatisticaVO> obterItemSumarioUnidadeEstatisticaRequisicaoVO(int nrUnidadeApresentar, UsuarioVO usuario) throws Exception {
		List<ItemSumarioUnidadeEstatisticaVO> resultado = new ArrayList<>(0);
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT Requisicao.unidadeEnsino, UnidadeEnsino.nome, COUNT(Requisicao.codigo) as nrRequisicoes " + "FROM Requisicao " + "INNER JOIN UnidadeEnsino ON (UnidadeEnsino.codigo = Requisicao.unidadeEnsino) where  Requisicao.situacaoautorizacao = 'PE' " + "GROUP BY Requisicao.unidadeEnsino, UnidadeEnsino.nome " + "ORDER BY nrRequisicoes DESC;";

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql);

		while (tabelaResultado.next()) {
			ItemSumarioUnidadeEstatisticaVO sumario = new ItemSumarioUnidadeEstatisticaVO();
			sumario.setCodigo(tabelaResultado.getInt("unidadeEnsino"));
			sumario.setNome(tabelaResultado.getString("nome"));
			if (tabelaResultado.getInt("unidadeEnsino") == 0) {
				sumario.setNome("Sem Unidade");
			}
			sumario.setQuantidade(tabelaResultado.getInt("nrRequisicoes"));
			resultado.add(sumario);
		}

		if (resultado.size() > nrUnidadeApresentar) {
			int posSomarRemover = resultado.size() - 1;
			ItemSumarioUnidadeEstatisticaVO sumarioSoma = new ItemSumarioUnidadeEstatisticaVO();
			sumarioSoma.setCodigo(0);
			sumarioSoma.setNome("Outras");
			sumarioSoma.setQuantidade(0);
			while (posSomarRemover >= nrUnidadeApresentar) {
				ItemSumarioUnidadeEstatisticaVO itemRemover = resultado.remove(posSomarRemover);
				sumarioSoma.setQuantidade(sumarioSoma.getQuantidade() + itemRemover.getQuantidade());
				posSomarRemover--;
			}
			resultado.add(sumarioSoma);
		}

		return resultado;
	}

	public EstatisticaRequisicaoVO consultarEstatisticaRequisicoesAtualizada(UsuarioVO usuario) throws Exception {
		EstatisticaRequisicaoVO estatisticas = new EstatisticaRequisicaoVO();
		Date hoje = new Date();
		ItemCronologicoEstatisticaVO hojeEstatistica = obterItemCronologicoEstatisticaRequisicaoVO(hoje, "Hoje", usuario);
		estatisticas.getResumoCronologico().add(hojeEstatistica);
		ItemCronologicoEstatisticaVO anterioresEstatistica = obterItemCronologicoEstatisticaRequisicaoVOAnterioresData(hoje, "Anteriores", usuario);
		estatisticas.getResumoCronologico().add(anterioresEstatistica);

		List<ItemSumarioUnidadeEstatisticaVO> sumario = obterItemSumarioUnidadeEstatisticaRequisicaoVO(3, usuario);
		estatisticas.setSumarioPorUnidade(sumario);

		return estatisticas;
	}
}
