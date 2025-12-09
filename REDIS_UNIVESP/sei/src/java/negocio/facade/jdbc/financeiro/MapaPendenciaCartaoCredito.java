package negocio.facade.jdbc.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.contabil.LancamentoContabilVO;
import negocio.comuns.contabil.enumeradores.TipoOrigemHistoricoBloqueioEnum;
import negocio.comuns.contabil.enumeradores.TipoOrigemLancamentoContabilEnum;
import negocio.comuns.contabil.enumeradores.TipoRegraContabilEnum;
import negocio.comuns.financeiro.CentroResultadoOrigemVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ContaPagarNegociacaoPagamentoVO;
import negocio.comuns.financeiro.ContaPagarVO;
import negocio.comuns.financeiro.ContaReceberNegociacaoRecebimentoVO;
import negocio.comuns.financeiro.ContaReceberRecebimentoVO;
import negocio.comuns.financeiro.FluxoCaixaVO;
import negocio.comuns.financeiro.FormaPagamentoNegociacaoPagamentoVO;
import negocio.comuns.financeiro.FormaPagamentoNegociacaoRecebimentoVO;
import negocio.comuns.financeiro.MapaPendenciaCartaoCreditoTotalVO;
import negocio.comuns.financeiro.MapaPendenciaCartaoCreditoVO;
import negocio.comuns.financeiro.MovimentacaoCaixaVO;
import negocio.comuns.financeiro.NegociacaoPagamentoVO;
import negocio.comuns.financeiro.NegociacaoRecebimentoVO;
import negocio.comuns.financeiro.OperadoraCartaoVO;
import negocio.comuns.financeiro.enumerador.OperadoraCartaoCreditoEnum;
import negocio.comuns.financeiro.enumerador.OrigemExtratoContaCorrenteEnum;
import negocio.comuns.financeiro.enumerador.TipoContaCorrenteEnum;
import negocio.comuns.financeiro.enumerador.TipoFinanciamentoEnum;
import negocio.comuns.financeiro.enumerador.TipoNivelCentroResultadoEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.OrigemContaPagar;
import negocio.comuns.utilitarias.dominios.SituacaoFinanceira;
import negocio.comuns.utilitarias.dominios.TipoSacado;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.MapaPendenciaCartaoCreditoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>MapaPendenciaCartaoCreditoVO</code>. Responsável por implementar operações como incluir, alterar, excluir e consultas pertinentes a classe <code>MapaPendenciaCartaoCreditoVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see MapaPendenciaCartaoCreditoVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class MapaPendenciaCartaoCredito extends ControleAcesso implements MapaPendenciaCartaoCreditoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5542103951305415069L;
	protected static String idEntidade;

	public MapaPendenciaCartaoCredito() throws Exception {
		super();
		setIdEntidade("MapaPendenciaCartaoCredito");
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarBaixaParcelaCartaoCredito(List<MapaPendenciaCartaoCreditoTotalVO> listaMapaPendenciaCartaoCreditoTotalVO, Boolean unidadeMatriz, Date dataVencimento, Date dataRecebimento, FormaPagamentoVO formaPagamentoVO, UsuarioVO usuario) throws Exception {
		if (!Uteis.isAtributoPreenchido(formaPagamentoVO)) {
			throw new Exception("O campo FORMA PAGAMENTO deve ser informado.");
		}
		if (!Uteis.isAtributoPreenchido(dataVencimento)) {
			throw new Exception("O campo DATA VENCIMENTO deve ser informado.");
		}
		List<Integer> listaUnidadeEnsino = listaMapaPendenciaCartaoCreditoTotalVO
		.stream()
		.flatMap(p-> p.getMapaPendenciaCartaoCreditoVOs().stream())
		.filter(pp->pp.getEfetuarBaixa() && pp.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getSituacao().equals("AR"))
		.map(p -> p.getFormaPagamentoNegociacaoRecebimentoVO().getNegociacaoRecebimentoVO().getUnidadeEnsino().getCodigo())
		.collect(Collectors.toList());
		verificarCompetenciaBloqueadaParaRegistrosEntidade(new MapaPendenciaCartaoCreditoTotalVO(), "ALTERAR", dataRecebimento, listaUnidadeEnsino, null, TipoOrigemHistoricoBloqueioEnum.RECEBIMENTO, usuario );
		
		for (MapaPendenciaCartaoCreditoTotalVO mapaPendenciaCartaoCreditoTotalVO : listaMapaPendenciaCartaoCreditoTotalVO) {		
			if(getFacadeFactory().getConciliacaoContaCorrenteFacade().validarConciliacaoContaCorrenteFinalizada(dataRecebimento, mapaPendenciaCartaoCreditoTotalVO.getContaCorrenteVO().getNumero(), usuario)) {
				throw new Exception("Não é possível realizar a baixa  da pendência financeira da conta corrente " + mapaPendenciaCartaoCreditoTotalVO.getContaCorrenteVO().getNumero()  + ", na data " + UteisData.getDataAno4Digitos(dataRecebimento) + ", pois a conciliação bancária já está finalizada.");
			}										
		}
		
		List<MapaPendenciaCartaoCreditoVO> listaCompensacaoCartao = new ArrayList<MapaPendenciaCartaoCreditoVO>();
		try {
			UnidadeEnsinoVO unidadeEnsinoMatriz = getFacadeFactory().getUnidadeEnsinoFacade().obterUnidadeMatriz(false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
			formaPagamentoVO = getFacadeFactory().getFormaPagamentoFacade().consultarPorChavePrimaria(formaPagamentoVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuario);
			for (MapaPendenciaCartaoCreditoTotalVO mapaPendenciaCartaoCreditoTotalVO : listaMapaPendenciaCartaoCreditoTotalVO) {
				if (!mapaPendenciaCartaoCreditoTotalVO.getMapaPendenciaCartaoCreditoVOs().isEmpty()) {
					Double saldoAtualizar = 0.0;
					listaCompensacaoCartao.clear();
					for (MapaPendenciaCartaoCreditoVO obj : mapaPendenciaCartaoCreditoTotalVO.getMapaPendenciaCartaoCreditoVOs()) {
						if (obj.getEfetuarBaixa() && obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getSituacao().equals("AR")) {
							obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setDataRecebimento(dataRecebimento);
							obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setSituacao("RE");
							obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setResponsavelPelaBaixa(usuario);
							getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoCartaoCreditoFacade().alterarDataRecebimentoSituacaoResponsavelPelaBaixa(obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO(), usuario);
							saldoAtualizar = Uteis.arrendondarForcando2CadasDecimais(saldoAtualizar + obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getValorParcela());
							listaCompensacaoCartao.add(obj);
						}
					}
					criarMovimentacaoCaixaContaCorrente(listaCompensacaoCartao, dataRecebimento, mapaPendenciaCartaoCreditoTotalVO.getAbaterTaxaExtratoContaCorrente(), usuario);
					Map<Integer, List<MapaPendenciaCartaoCreditoVO>> map = listaCompensacaoCartao.stream().collect(Collectors.groupingBy(p -> p.getFormaPagamentoNegociacaoRecebimentoVO().getNegociacaoRecebimentoVO().getUnidadeEnsino().getCodigo()));
					getFacadeFactory().getLancamentoContabilFacade().gerarLancamentoContabilPorMapaPendenciaCartaoCredito(map, dataRecebimento, mapaPendenciaCartaoCreditoTotalVO.getAbaterTaxaExtratoContaCorrente(), true, usuario);
					gerarContaPagarMapaPendenciaCartaoCredito(mapaPendenciaCartaoCreditoTotalVO, map, unidadeMatriz, dataVencimento, dataRecebimento, formaPagamentoVO, unidadeEnsinoMatriz, usuario);					
					alterarSaldoContaCorrente(mapaPendenciaCartaoCreditoTotalVO, "EN", saldoAtualizar, usuario);
				}
			}
			
		} catch (Exception e) {
			inicioCompensacaoCartao: for (MapaPendenciaCartaoCreditoVO objExistente : listaCompensacaoCartao) {
				for (MapaPendenciaCartaoCreditoTotalVO mapaPendenciaCartaoCreditoTotalVO : listaMapaPendenciaCartaoCreditoTotalVO) {
					for (MapaPendenciaCartaoCreditoVO obj : mapaPendenciaCartaoCreditoTotalVO.getMapaPendenciaCartaoCreditoVOs()) {
						if (objExistente.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getCodigo().equals(obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getCodigo())) {
							obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setSituacao("AR");
							continue inicioCompensacaoCartao;
						}
					}
				}
			}
			throw e;
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void gerarContaPagarMapaPendenciaCartaoCredito(MapaPendenciaCartaoCreditoTotalVO mapaPendenciaCartaoCreditoTotalVO, Map<Integer, List<MapaPendenciaCartaoCreditoVO>> map, boolean unidadeMatriz, Date dataVencimento, Date dataRecebimento, FormaPagamentoVO formaPagamentoVO, UnidadeEnsinoVO unidadeEnsinoMatriz, UsuarioVO usuario) throws Exception {
		try {
			if(!mapaPendenciaCartaoCreditoTotalVO.getAbaterTaxaExtratoContaCorrente()){
				for (Map.Entry<Integer, List<MapaPendenciaCartaoCreditoVO>> mapa : map.entrySet()) {
					UnidadeEnsinoVO ue = new UnidadeEnsinoVO();
					if(unidadeMatriz){
						ue.setCodigo(unidadeEnsinoMatriz.getCodigo());
					}else{
						ue.setCodigo(mapa.getKey());	
					}
					Double taxa = mapa.getValue().stream().mapToDouble(p-> p.getValorTaxaUsar()).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b));
					if (taxa > 0.0) {
						ContaPagarVO contaPagarVO = inicializarDadosContaPagar(mapaPendenciaCartaoCreditoTotalVO, taxa, ue, dataVencimento, usuario);
						inicializarDadosPagamentoTaxa(mapaPendenciaCartaoCreditoTotalVO, contaPagarVO, dataRecebimento, formaPagamentoVO, usuario);
					}
				}	
			}
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void criarMovimentacaoCaixaContaCorrente(List<MapaPendenciaCartaoCreditoVO> listaMapaPendenciaCartaoCreditoVO, Date dataBaixa, Boolean abaterValorTaxa, UsuarioVO usuario) throws Exception {

		if (!listaMapaPendenciaCartaoCreditoVO.isEmpty()) {
			StringBuilder sql = new StringBuilder("INSERT INTO ExtratoContaCorrente( valor, data, origemExtratoContaCorrente, tipoMovimentacaoFinanceira, codigoOrigem, ");
			sql.append(" codigoCheque, sacadoCheque, numeroCheque, bancoCheque, contaCorrenteCheque, ");
			sql.append(" agenciaCheque, dataPrevisaoCheque, nomeSacado, codigoSacado, tipoSacado,  ");
			sql.append(" contaCorrente, unidadeEnsino, responsavel, formaPagamento, formaPagamentoNegociacaoRecebimento, operadoracartao, valorTaxaBancaria ) ");
			sql.append(" select FormaPagamentoNegociacaoRecebimentoCartaoCredito.valorParcela, '" + Uteis.getDataJDBCTimestamp(dataBaixa) + "', '" + OrigemExtratoContaCorrenteEnum.COMPENSACAO_CARTAO.name() + "' as origemExtratoContaCorrente,  ");
			sql.append(" 'ENTRADA' as tipoMovimentacaoFinanceira,  formapagamentonegociacaorecebimento.negociacaorecebimento,  ");
			sql.append(" null, '', '', '', '', '', null,   ");
			sql.append(" case when negociacaorecebimento.tipoPessoa in ('FU', 'AL', 'RF', 'CA', 'RE') then pessoa.nome else parceiro.nome end as nomeSacado, ");
			sql.append(" case when negociacaorecebimento.tipoPessoa in ('FU', 'AL', 'RF', 'CA', 'RE') then pessoa.codigo else parceiro.codigo end as codigoSacado, ");
			sql.append(" case negociacaorecebimento.tipoPessoa  ");
			sql.append(" when 'FO' then 'FORNECEDOR' ");
			sql.append(" when 'FU' then 'FUNCIONARIO_PROFESSOR' ");
			sql.append(" when 'AL' then 'ALUNO'  ");
			sql.append(" when 'RF' then 'RESPONSAVEL_FINANCEIRO' ");
			sql.append(" when 'BA' then 'BANCO'  ");
			sql.append(" when 'CA' then 'CANDIDATO' ");
			sql.append(" when 'RE' then 'REQUERENTE'  ");
			sql.append(" when 'PA' then 'PARCEIRO' end as tipoSacado, contacorrente.codigo, negociacaorecebimento.unidadeEnsino, " + usuario.getCodigo() + ", formaPagamento.codigo, formapagamentonegociacaorecebimento.codigo, operadoracartao, taxa.valor as valorTaxaBancaria ");
			sql.append(" from formapagamentonegociacaorecebimento  ");
			/**
			 * Este join deve ser matido para atender o modelo antigo do relacionamento onde a forma de pagamento negociacao recebimento tinha uma lista de FormaPagamentoNegociacaoRecebimentoCartaoCredito hoje este relacionamento foi alterado 1 para 1
			 */
			sql.append(" left join FormaPagamentoNegociacaoRecebimentoCartaoCredito on (FormaPagamentoNegociacaoRecebimentoCartaoCredito.codigo = formapagamentonegociacaorecebimento.FormaPagamentoNegociacaoRecebimentoCartaoCredito ");
			sql.append("  or FormaPagamentoNegociacaoRecebimentoCartaoCredito.formapagamentonegociacaorecebimento = formapagamentonegociacaorecebimento.codigo) ");
			sql.append(" left join contacorrente on contacorrente.codigo = formapagamentonegociacaorecebimento.contaCorrenteOperadoraCartao ");
			sql.append(" left join formaPagamento on   formaPagamento.codigo = formapagamentonegociacaorecebimento.formaPagamento ");
			sql.append(" left join negociacaorecebimento on negociacaorecebimento.codigo = formapagamentonegociacaorecebimento.negociacaoRecebimento ");
			sql.append(" left join pessoa on pessoa.codigo = negociacaorecebimento.pessoa ");
			sql.append(" left join parceiro on parceiro.codigo = negociacaorecebimento.parceiro ");
			sql.append(" inner join ( ");
			int x = 0;
			for (MapaPendenciaCartaoCreditoVO mapaPendenciaCartaoCreditoVO : listaMapaPendenciaCartaoCreditoVO) {
				if (x > 0) {
					sql.append(" union all ");
				}
				sql.append(" select ").append(mapaPendenciaCartaoCreditoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getCodigo()).append(" as codigo");
				if (abaterValorTaxa) {
					sql.append(", ").append(mapaPendenciaCartaoCreditoVO.getValorTaxaUsar());
				} else {
					sql.append(", 0.0");
				}
				sql.append(" as valor");
				x++;
			}
			sql.append(" ) as taxa on taxa.codigo = FormaPagamentoNegociacaoRecebimentoCartaoCredito.codigo ");
			getConexao().getJdbcTemplate().execute(sql.toString() + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void inicializarDadosPagamentoTaxa(MapaPendenciaCartaoCreditoTotalVO mapaPendenciaCartaoCreditoTotalVO, ContaPagarVO contaPagarVO, Date dataRecebimento, FormaPagamentoVO formaPagamentoVO, UsuarioVO usuario) throws Exception {
		FormaPagamentoNegociacaoPagamentoVO formaPagamentoNegociacaoPagamentoVO = new FormaPagamentoNegociacaoPagamentoVO();

		NegociacaoPagamentoVO negociacaoPagamentoVO = new NegociacaoPagamentoVO();
		ContaPagarNegociacaoPagamentoVO contaPagarNegociacaoPagamento = new ContaPagarNegociacaoPagamentoVO();
		contaPagarNegociacaoPagamento.setContaPagar(contaPagarVO);
		contaPagarNegociacaoPagamento.setNegociacaoContaPagar(negociacaoPagamentoVO.getCodigo());
		contaPagarNegociacaoPagamento.setValorContaPagar(contaPagarVO.getValor());

		formaPagamentoNegociacaoPagamentoVO.setFormaPagamento(formaPagamentoVO);
		formaPagamentoNegociacaoPagamentoVO.setContaCorrente(mapaPendenciaCartaoCreditoTotalVO.getContaCorrenteVO());
		formaPagamentoNegociacaoPagamentoVO.setValor(contaPagarVO.getValor());
		formaPagamentoNegociacaoPagamentoVO.setNegociacaoContaPagar(negociacaoPagamentoVO.getCodigo());

		negociacaoPagamentoVO.getContaPagarNegociacaoPagamentoVOs().add(contaPagarNegociacaoPagamento);
		negociacaoPagamentoVO.getFormaPagamentoNegociacaoPagamentoVOs().add(formaPagamentoNegociacaoPagamentoVO);
		negociacaoPagamentoVO.setData(dataRecebimento);
		negociacaoPagamentoVO.setUnidadeEnsino(contaPagarVO.getUnidadeEnsino());
		negociacaoPagamentoVO.setTipoSacado(TipoSacado.OPERADORA_CARTAO.getValor());
		negociacaoPagamentoVO.setOperadoraCartao(contaPagarVO.getOperadoraCartao());
		negociacaoPagamentoVO.setValorTotal(contaPagarVO.getValor());
		negociacaoPagamentoVO.setValorTotalPagamento(contaPagarVO.getValor());
		negociacaoPagamentoVO.setResponsavel(usuario);
		getFacadeFactory().getNegociacaoPagamentoFacade().incluir(negociacaoPagamentoVO, usuario);
	}

	/**
	 * Operação responsável por criar uma ContaPagar no BD de acordo com a taxa paga para a OperadoraCartaoCredito.
	 * 
	 * @param MapaPendenciaCartaoCreditoVO
	 *            objeto da classe <code>MapaPendenciaCartaoCreditoVO</code>.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public ContaPagarVO inicializarDadosContaPagar(MapaPendenciaCartaoCreditoTotalVO mapaPendenciaCartaoCreditoTotalVO, Double taxa, UnidadeEnsinoVO unidadeEnsinoVO, Date dataVencimento, UsuarioVO usuario) throws Exception {
		ContaPagarVO obj = new ContaPagarVO();
		obj.setValor(taxa);
		obj.setValorPagamento(taxa);
		obj.setValorPago(taxa);
		obj.setData(new Date());
		obj.setDataVencimento(dataVencimento);
		obj.setUnidadeEnsino(unidadeEnsinoVO);
		obj.setSituacao(SituacaoFinanceira.A_PAGAR.getValor());
		obj.setOperadoraCartao(mapaPendenciaCartaoCreditoTotalVO.getOperadoraCartaoVO());
		obj.setContaCorrente(mapaPendenciaCartaoCreditoTotalVO.getContaCorrenteVO());
		obj.setDescricao("Pagamento gerado automático para lançamento de taxa do mapa pendencia cartão crédito .");
		obj.setCodOrigem("");
		obj.setTipoOrigem(OrigemContaPagar.TAXA_CARTAO.getValor());
		obj.setTipoSacado(TipoSacado.OPERADORA_CARTAO.getValor());

		CentroResultadoOrigemVO cro = new CentroResultadoOrigemVO();
		cro.setTipoNivelCentroResultadoEnum(TipoNivelCentroResultadoEnum.UNIDADE_ENSINO);
		cro.setUnidadeEnsinoVO(obj.getUnidadeEnsino());
		cro.setCategoriaDespesaVO(getFacadeFactory().getCategoriaDespesaFacade().consultaCategoriaDespesaPadraoConfiguracaoFinanceiroPorUnidadeEnsinoTipoCategoria(unidadeEnsinoVO.getCodigo(), mapaPendenciaCartaoCreditoTotalVO.getOperadoraCartaoVO().getCodigo(), "categoriadespesaoperadoracartao"));
		cro.setCentroResultadoAdministrativo(getFacadeFactory().getCentroResultadoFacade().consultaCentroResultadoPadraoConfiguracaoFinanceiroPorUnidadeEnsino(unidadeEnsinoVO.getCodigo(), usuario));
		cro.setTipoNivelCentroResultadoEnum(TipoNivelCentroResultadoEnum.UNIDADE_ENSINO);
		cro.setQuantidade(1.00);
		cro.setPorcentagem(100.00);
		cro.setValor(obj.getValor());
		obj.getListaCentroResultadoOrigemVOs().add(cro);
		getFacadeFactory().getContaPagarFacade().incluir(obj, false, true, usuario);
		return obj;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarEstornoBaixa(List<MapaPendenciaCartaoCreditoTotalVO> listaMapaPendenciaCartaoCreditoTotalVO, Boolean unidadeMatriz, UsuarioVO usuario) throws Exception {
		try {
			for (MapaPendenciaCartaoCreditoTotalVO mapaPendenciaCartaoCreditoTotalVO : listaMapaPendenciaCartaoCreditoTotalVO) {
				if (!mapaPendenciaCartaoCreditoTotalVO.getMapaPendenciaCartaoCreditoVOs().isEmpty()) {
					Double saldoAtualizar = 0.0;
					for (MapaPendenciaCartaoCreditoVO obj : mapaPendenciaCartaoCreditoTotalVO.getMapaPendenciaCartaoCreditoVOs()) {
						if (obj.getEfetuarBaixa() && obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getSituacao().equals("RE")) {
							verificarCompetenciaBloqueadaParaRegistrosEntidade(new MapaPendenciaCartaoCreditoTotalVO(), "ALTERAR", obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getDataRecebimento(), obj.getFormaPagamentoNegociacaoRecebimentoVO().getNegociacaoRecebimentoVO().getUnidadeEnsino().getCodigo(), TipoOrigemHistoricoBloqueioEnum.RECEBIMENTO, usuario );
							
							
							getFacadeFactory().getExtratoContaCorrenteFacade().validarExtratoContaCorrenteComVinculoConciliacaoContaCorrenteParaEstorno(OrigemExtratoContaCorrenteEnum.COMPENSACAO_CARTAO, obj.getFormaPagamentoNegociacaoRecebimentoVO().getCodigo(), false, 0, false, usuario);
							
							getFacadeFactory().getLancamentoContabilFacade().excluirPorCodOrigemTipoOrigem(obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getCodigo().toString(), TipoOrigemLancamentoContabilEnum.CARTAO_CREDITO, false, usuario);
							
							getFacadeFactory().getExtratoContaCorrenteFacade().excluirPorFormaPagamentoNegociacaoRecebimento(obj.getFormaPagamentoNegociacaoRecebimentoVO().getCodigo(), OrigemExtratoContaCorrenteEnum.COMPENSACAO_CARTAO, false, usuario);
							
							obj.setValidarDados(false);
							obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setDataRecebimento(null);
							obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setSituacao("AR");
							obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setResponsavelPelaBaixa(null);
							getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoCartaoCreditoFacade().alterarDataRecebimentoSituacaoResponsavelPelaBaixa(obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO(), usuario);
							obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setLancamentoContabil(false);
							obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getListaLancamentoContabeisCredito().clear();
							obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getListaLancamentoContabeisDebito().clear();
							saldoAtualizar = Uteis.arrendondarForcando2CadasDecimais(saldoAtualizar + obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getValorParcela());
						}
					}
					if(Uteis.isAtributoPreenchido(saldoAtualizar)){
						alterarSaldoContaCorrente(mapaPendenciaCartaoCreditoTotalVO, "SA", saldoAtualizar, usuario);	
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persitirAlteracoesAjusteValorLiquido(MapaPendenciaCartaoCreditoVO obj, UsuarioVO usuarioVO) throws Exception{		
		getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoCartaoCreditoFacade().alterarAjusteValorLiquido(obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO(), usuarioVO);
	}
	
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persitirAlteracoesNegociacaoRecebimento(MapaPendenciaCartaoCreditoVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiro,  UsuarioVO usuarioVO) throws Exception{
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamento()), "O campo Forma Pagamento deve ser informado.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getFormaPagamentoNegociacaoRecebimentoVO().getConfiguracaoFinanceiroCartaoVO()), "O campo Operadora deve ser informado.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getTipoFinanciamentoEnum()), "O campo Tipo Financiamento deve ser informado.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getFormaPagamentoNegociacaoRecebimentoVO().getQtdeParcelasCartaoCredito()), "O campo Quantidade de Parcelas deve ser informado.");		
		zerarValoresParaNovaDistribuicaoNegociacaoRecebimento(obj, configuracaoFinanceiro, usuarioVO, obj.getFormaPagamentoNegociacaoRecebimentoVO().getNegociacaoRecebimentoVO());		
		getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoFacade().incluirFormaPagamentoNegociacaoRecebimentosPorMapaPendenciaCartaoCredito(obj, usuarioVO);
		gerarNovaDistribuicaoAlteracaoNegociacaoRecebimento(obj, usuarioVO);		
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private void gerarNovaDistribuicaoAlteracaoNegociacaoRecebimento(MapaPendenciaCartaoCreditoVO obj, UsuarioVO usuarioVO) throws Exception {
		List<LancamentoContabilVO> listaLancamentoContabilVOs = new ArrayList<>();
		List<MovimentacaoCaixaVO> listaMovimentacaoCaixaTemp = new ArrayList<>();
		FluxoCaixaVO fc = new FluxoCaixaVO();
		fc.setCodigo(getFacadeFactory().getMovimentacaoCaixaFacade().excluirMovimentacaoCaixasPorAlteracaoMapaPendenciaCartaoCredito(obj.getFormaPagamentoNegociacaoRecebimentoVO().getNegociacaoRecebimentoVO().getCodigo(), obj.getFormaPagamentoVO().getCodigo(), obj.getOperadoraCartaoVO().getCodigo(), usuarioVO));
		for (FormaPagamentoNegociacaoRecebimentoVO novoFpnr : obj.getListaFormaPagamentoNegociacaoRecebimentoVO()) {
			Double vlrPgmnt = novoFpnr.getValorRecebimento();
			getFacadeFactory().getNegociacaoRecebimentoFacade().distribuirContaReceberNegociacao(obj.getFormaPagamentoNegociacaoRecebimentoVO().getNegociacaoRecebimentoVO(), novoFpnr, vlrPgmnt, listaLancamentoContabilVOs);
			listaMovimentacaoCaixaTemp.add(getFacadeFactory().getMovimentacaoCaixaFacade().executarGeracaoMovimentacaoCaixa(null, fc, obj.getFormaPagamentoNegociacaoRecebimentoVO().getNegociacaoRecebimentoVO().getPessoa().getCodigo(), obj.getFormaPagamentoNegociacaoRecebimentoVO().getNegociacaoRecebimentoVO().getParceiroVO().getCodigo(),  obj.getFormaPagamentoNegociacaoRecebimentoVO().getNegociacaoRecebimentoVO().getFornecedor().getCodigo(), obj.getFormaPagamentoNegociacaoRecebimentoVO().getNegociacaoRecebimentoVO().getResponsavel().getCodigo(), novoFpnr.getOperadoraCartaoVO().getCodigo() , 0, obj.getFormaPagamentoNegociacaoRecebimentoVO().getNegociacaoRecebimentoVO().getCodigo(), novoFpnr.getFormaPagamento().getCodigo(), vlrPgmnt, "RE", "EN"));
		}
		obj.getFormaPagamentoNegociacaoRecebimentoVO().getNegociacaoRecebimentoVO().getContaReceberNegociacaoRecebimentoVOs().stream().forEach(p->{
			Double valorTotalTemp = p.getContaReceber().getContaReceberRecebimentoVOs().stream().filter(c-> Uteis.isAtributoPreenchido(c.getFormaPagamentoNegociacaoRecebimento())).mapToDouble(c->c.getValorRecebimento()).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b));
			Uteis.checkState(!p.getValorTotal().equals(valorTotalTemp), "O valor total das formas de pagamentos é diferente do valor da conta receber vinculada a ela.");
		});		
		
		for (ContaReceberNegociacaoRecebimentoVO crnr : obj.getFormaPagamentoNegociacaoRecebimentoVO().getNegociacaoRecebimentoVO().getContaReceberNegociacaoRecebimentoVOs()) {
			getFacadeFactory().getContaReceberRecebimentoFacade().incluirContaReceberRecebimentos(crnr.getContaReceber().getCodigo(), 0, crnr.getContaReceber().getContaReceberRecebimentoVOs(), usuarioVO);
		}
		getFacadeFactory().getNegociacaoRecebimentoFacade().persistirLancamentoContabilPorNegociacaoRecebimento(obj.getFormaPagamentoNegociacaoRecebimentoVO().getNegociacaoRecebimentoVO(), usuarioVO, listaLancamentoContabilVOs);
		getFacadeFactory().getMovimentacaoCaixaFacade().persistir(listaMovimentacaoCaixaTemp, false, usuarioVO);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private void zerarValoresParaNovaDistribuicaoNegociacaoRecebimento(MapaPendenciaCartaoCreditoVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuarioVO, NegociacaoRecebimentoVO nr) throws Exception {
		List<Integer> listaFormaPagamentoNegociacaoRecebimentoTemp = getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoFacade().consultarCodigoPorNegociacaoRecebimento(nr.getCodigo(), obj.getFormaPagamentoVO().getCodigo(), obj.getContaCorrenteVO().getCodigo(), obj.getOperadoraCartaoVO().getCodigo(),usuarioVO);
		Uteis.checkState(!Uteis.isAtributoPreenchido(listaFormaPagamentoNegociacaoRecebimentoTemp), "Não foi encontrado as forma de pagamento existentes da negociacao recebimento para a operadora de código "+ obj.getOperadoraCartaoVO().getCodigo() + " na conta corrente de código "+ obj.getContaCorrenteVO().getCodigo() + " e a forma de pagamento de código "+obj.getFormaPagamentoVO().getCodigo());
		Uteis.checkState(getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoFacade().consultarSeExisteFormaPagamentoNegociacaoRecebimentoRecebidaCartaoCredito(nr.getCodigo(), obj.getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamento().getCodigo(), obj.getFormaPagamentoNegociacaoRecebimentoVO().getContaCorrente().getCodigo(), obj.getFormaPagamentoNegociacaoRecebimentoVO().getOperadoraCartaoVO().getCodigo(), "", listaFormaPagamentoNegociacaoRecebimentoTemp, usuarioVO), "Não foi possível realizar essa operação, pois já existe um forma de pagamento de cartão de crédito utilizando essa operadora de cartão e forma de pagamento.");
		nr.setContaReceberNegociacaoRecebimentoVOs(ContaReceberNegociacaoRecebimento.consultarContaReceberNegociacaoRecebimentos(nr.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, configuracaoFinanceiro, usuarioVO));
		for (ContaReceberNegociacaoRecebimentoVO crnr : nr.getContaReceberNegociacaoRecebimentoVOs()) {
			getFacadeFactory().getLancamentoContabilFacade().excluirPorCodOrigemTipoOrigem(crnr.getContaReceber().getCodigo().toString(), TipoOrigemLancamentoContabilEnum.RECEBER, false, usuarioVO);
			crnr.getContaReceber().setValorNegociacao(crnr.getValorTotal().doubleValue());
			Iterator<ContaReceberRecebimentoVO> j = crnr.getContaReceber().getContaReceberRecebimentoVOs().iterator();
			while (j.hasNext()) {
				ContaReceberRecebimentoVO crr = j.next();
				if(listaFormaPagamentoNegociacaoRecebimentoTemp.contains(crr.getFormaPagamentoNegociacaoRecebimento())){
					crnr.getContaReceber().setSituacao("AR");
					crnr.getContaReceber().setValorNegociacao(Uteis.arrendondarForcando2CadasDecimais(crnr.getContaReceber().getValorNegociacao() - crr.getValorRecebimento()));
					getFacadeFactory().getContaReceberRecebimentoFacade().excluir(crr, usuarioVO);
					j.remove();
				}
			}
		}
		getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoFacade().excluirPorMapaPendenciaCartaoCredito(listaFormaPagamentoNegociacaoRecebimentoTemp, usuarioVO);
	}
	
	

	/**
	 * Operação responsável por calcular o valor a ser pago para OperadoraCartaoCredito de acordo com a taxa e a data de recebimento.
	 * 
	 * @param MapaPendenciaCartaoCreditoVO
	 *            objeto da classe <code>MapaPendenciaCartaoCreditoVO</code>.
	 * @exception Execption
	 *                Caso haja problemas no cálculo.
	 */
	public void executarCalculoValorPagar(MapaPendenciaCartaoCreditoVO obj) throws Exception {
		try {
			if (obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getDataVencimento() != null) {
				obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setValorDescontoCalculado(0.0);
				Date dataBase = new Date();
				if (obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getDataRecebimento() != null) {
					dataBase = obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getDataRecebimento();
				}
				if (dataBase.before(obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getDataVencimento())) {
					if (obj.getFormaPagamentoNegociacaoRecebimentoVO().getTaxaDeAntecipacao() > 0.0) {
						obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setValorDescontoCalculado(Uteis.arredondar(((obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getValorParcela() * obj.getFormaPagamentoNegociacaoRecebimentoVO().getTaxaDeAntecipacao()) / 100), 2, 0));
					} else if (obj.getFormaPagamentoNegociacaoRecebimentoVO().getTaxaDeOperacao() > 0.0) {
						obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setValorDescontoCalculado(Uteis.arredondar(((obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getValorParcela() * obj.getFormaPagamentoNegociacaoRecebimentoVO().getTaxaDeOperacao()) / 100), 2, 0));
					}
				} else {
					if (obj.getFormaPagamentoNegociacaoRecebimentoVO().getTaxaDeOperacao() > 0.0) {
						obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setValorDescontoCalculado(Uteis.arredondar(((obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getValorParcela() * obj.getFormaPagamentoNegociacaoRecebimentoVO().getTaxaDeOperacao()) / 100), 2, 0));
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por calcular o valor a ser pago para OperadoraCartaoCredito de acordo com a taxa e a data de recebimento.
	 * 
	 * @param MapaPendenciaCartaoCreditoVO
	 *            objeto da classe <code>MapaPendenciaCartaoCreditoVO</code>.
	 * @exception Execption
	 *                Caso haja problemas no cálculo.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSaldoContaCorrente(MapaPendenciaCartaoCreditoTotalVO obj, String tipoMovimentacao, Double valor, UsuarioVO usuario) throws Exception {
		try {
			getFacadeFactory().getContaCorrenteFacade().movimentarSaldoContaCorrente(obj.getContaCorrenteVO().getCodigo(), tipoMovimentacao, valor, usuario);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * SQL responsável para buscar os dados necessários para manipulação do MapaPendenciaCartaoCredito
	 * 
	 * @return String Contendo sql Padráo para consulta dos dados.
	 */
	private StringBuffer getSQLPadraoConsultaCompleta() {
		// StringBuffer sqlStr = new
		// StringBuffer("SELECT matricula.matricula as \"matricula.matricula\",
		// pessoa.nome as \"pessoa.nome\", operadoraCartao.nome as
		// \"operadoraCartao.nome\", ");
		StringBuffer sqlStr = new StringBuffer("SELECT operadoraCartao.nome as \"operadoraCartao.nome\", operadoraCartao.codigo as \"operadoraCartao.codigo\", operadoraCartao.tipo as \"operadoraCartao.tipo\", ");
		sqlStr.append(" operadoraCartao.operadoracartaocredito as \"operadoraCartao.operadoracartaocredito\",  ");
		sqlStr.append(" negociacaorecebimento.tipoPessoa as \"negociacaorecebimento.tipoPessoa\", negociacaorecebimento.matricula as \"negociacaorecebimento.matricula\",  ");
		sqlStr.append(" negociacaorecebimento.codigo as \"negociacaorecebimento.codigo\",  ");
		sqlStr.append(" negociacaorecebimento.data as \"negociacaorecebimento.data\",  ");
		sqlStr.append(" negociacaorecebimento.valorTotalRecebimento as \"negociacaorecebimento.valorTotalRecebimento\",  ");
		sqlStr.append(" parceiro.codigo as \"parceiro.codigo\", parceiro.nome as \"parceiro.nome\",  ");
		sqlStr.append(" fornecedor.codigo as \"fornecedor.codigo\", fornecedor.nome as \"fornecedor.nome\",  ");
		sqlStr.append(" pessoa.codigo as \"pessoa.codigo\", pessoa.nome as \"pessoa.nome\",  ");
		
		sqlStr.append(" formapagamento.codigo as \"formapagamento.codigo\", formapagamento.nome as \"formapagamento.nome\", ");
		sqlStr.append(" formapagamento.tipo as \"formapagamento.tipo\",  ");
		
		sqlStr.append(" formaPagamentoNegociacaoRecebimento.taxaOperadora as \"formaPagamentoNegociacaoRecebimento.taxaOperadora\", formaPagamentoNegociacaoRecebimento.taxaAntecipacao as \"formaPagamentoNegociacaoRecebimento.taxaAntecipacao\", ");
		sqlStr.append(" formaPagamentoNegociacaoRecebimento.qtdeParcelasCartaoCredito as \"formaPagamentoNegociacaoRecebimento.qtdeParcelasCartaoCredito\", formaPagamentoNegociacaoRecebimento.valorrecebimento as \"formaPagamentoNegociacaoRecebimento.valorrecebimento\", ");
		sqlStr.append(" formaPgtoNegociacaoRecebimentoCartaoCredito.codigo as \"formaPgtoNegociacaoRecebimentoCartaoCredito.codigo\", ");

		sqlStr.append(" formaPgtoNegociacaoRecebimentoCartaoCredito.nomecartaocredito as \"formaPgtoNegociacaoRecebimentoCartaoCredito.nomecartaocredito\", ");
		sqlStr.append(" formaPgtoNegociacaoRecebimentoCartaoCredito.numerocartao as \"formaPgtoNegociacaoRecebimentoCartaoCredito.numerocartao\", ");
		sqlStr.append(" formaPgtoNegociacaoRecebimentoCartaoCredito.numerorecibotransacao as \"formaPgtoNegociacaoRecebimentoCartaoCredito.recibotransacao\", ");
		sqlStr.append(" formaPgtoNegociacaoRecebimentoCartaoCredito.chaveDaTransacao as \"formaPgtoNegociacaoRecebimentoCartaoCredito.chaveDaTransacao\", ");
		sqlStr.append(" formapagamento.nome as \"formapagamento.nome\", formapagamento.codigo as \"formapagamento.codigo\", formapagamento.tipo as \"formapagamento.tipo\", ");
		sqlStr.append(" configuracaofinanceirocartao.codigo as \"configuracaofinanceirocartao.codigo\",  ");

		sqlStr.append(" formaPgtoNegociacaoRecebimentoCartaoCredito.numeroParcela as \"formaPgtoNegociacaoRecebimentoCartaoCredito.numeroParcela\", ");
		sqlStr.append(" formaPgtoNegociacaoRecebimentoCartaoCredito.valorParcela as \"formaPgtoNegociacaoRecebimentoCartaoCredito.valorParcela\", ");
		sqlStr.append(" formaPgtoNegociacaoRecebimentoCartaoCredito.dataEmissao as \"formaPgtoNegociacaoRecebimentoCartaoCredito.dataEmissao\", ");
		sqlStr.append(" formaPgtoNegociacaoRecebimentoCartaoCredito.dataVencimento as \"formaPgtoNegociacaoRecebimentoCartaoCredito.dataVencimento\", ");
		sqlStr.append(" formaPgtoNegociacaoRecebimentoCartaoCredito.dataRecebimento as \"formaPgtoNegociacaoRecebimentoCartaoCredito.dataRecebimento\", ");
		sqlStr.append(" formaPgtoNegociacaoRecebimentoCartaoCredito.situacao as \"formaPgtoNegociacaoRecebimentoCartaoCredito.situacao\", ");
		sqlStr.append(" formaPgtoNegociacaoRecebimentoCartaoCredito.tipofinanciamento as \"formaPgtoNegociacaoRecebimentoCartaoCredito.tipofinanciamento\", ");
		sqlStr.append(" formaPgtoNegociacaoRecebimentoCartaoCredito.ajustarValorLiquido as \"formaPgtoNegociacaoRecebimentoCartaoCredito.ajustarValorLiquido\", ");
		sqlStr.append(" formaPagamentoNegociacaoRecebimento.codigo as \"formaPagamentoNegociacaoRecebimento.codigo\", ");
		sqlStr.append(" usuario.codigo as \"usuario.codigo\", usuario.nome as \"usuario.nome\", ");
		sqlStr.append(" responsavelAjustarValorLiquido.codigo as \"responsavelAjustarValorLiquido.codigo\", responsavelAjustarValorLiquido.nome as \"responsavelAjustarValorLiquido.nome\", ");
		sqlStr.append(" bancoCC.codigo as \"bancoCC.codigo\", bancoCC.nrBanco as \"bancoCC.nrBanco\", bancoCC.nome as \"bancoCC.nome\", ");
		sqlStr.append(" contaCorrente.codigo as \"contaCorrente.codigo\", ");
		sqlStr.append(" bancoCCOC.codigo as \"bancoCCOC.codigo\", bancoCCOC.nrBanco as \"bancoCCOC.nrBanco\", bancoCCOC.nome as \"bancoCCOC.nome\", ");
		sqlStr.append(" contaCorrenteOperadoraCartao.codigo as \"contaCorrenteOperadoraCartao.codigo\", ");
		sqlStr.append(" categoriaDespesa.codigo as \"categoriaDespesa.codigo\", ");
		sqlStr.append(" contaCorrenteOperadoraCartao.numero as \"contaCorrenteOperadoraCartao.numero\", ");
		sqlStr.append(" contaCorrenteOperadoraCartao.digito as \"contaCorrenteOperadoraCartao.digito\", ");
		sqlStr.append(" contaCorrenteOperadoraCartao.carteira as \"contaCorrenteOperadoraCartao.carteira\", ");
		sqlStr.append(" contaCorrenteOperadoraCartao.contacaixa as \"contaCorrenteOperadoraCartao.contacaixa\", ");
		sqlStr.append(" contaCorrenteOperadoraCartao.tipoContaCorrente as \"contaCorrenteOperadoraCartao.tipoContaCorrente\", ");
		sqlStr.append(" contaCorrenteOperadoraCartao.nomeApresentacaoSistema as \"contaCorrenteOperadoraCartao.nomeApresentacaoSistema\", ");
		sqlStr.append(" contaCorrenteOperadoraCartao.utilizaTaxaCartaoCredito as \"contaCorrenteOperadoraCartao.utilizaTaxaCartaoCredito\", ");
		sqlStr.append(" agenciaCCOC.codigo as \"agenciaCCOC.codigo\", ");
		sqlStr.append(" agenciaCCOC.numeroAgencia as \"agenciaCCOC.numeroAgencia\", ");
		sqlStr.append(" unidadeensino.codigo as \"unidadeensino.codigo\", unidadeensino.nome as \"unidadeensino.nome\" ");
		sqlStr.append(" FROM formaPagamentoNegociacaoRecebimentoCartaoCredito as formaPgtoNegociacaoRecebimentoCartaoCredito ");
		/**
		 * Este join deve ser matido para atender o modelo antigo do relacionamento onde a forma de pagamento negociacao recebimento tinha uma lista de FormaPagamentoNegociacaoRecebimentoCartaoCredito hoje este relacionamento foi alterado 1 para 1
		 */
		sqlStr.append(" INNER JOIN formaPagamentoNegociacaoRecebimento ON (formaPagamentoNegociacaoRecebimento.formaPagamentoNegociacaoRecebimentoCartaoCredito = formaPgtoNegociacaoRecebimentoCartaoCredito.codigo  ");
		sqlStr.append(" or formaPagamentoNegociacaoRecebimento.codigo = formaPgtoNegociacaoRecebimentoCartaoCredito.formaPagamentoNegociacaoRecebimento)  ");
		sqlStr.append(" LEFT JOIN categoriaDespesa ON categoriaDespesa.codigo = formaPagamentoNegociacaoRecebimento.categoriaDespesa ");
		sqlStr.append(" INNER JOIN contaCorrente ON contaCorrente.codigo = formaPagamentoNegociacaoRecebimento.contaCorrente ");
		sqlStr.append(" INNER JOIN formapagamento ON formapagamento.codigo = formaPagamentoNegociacaoRecebimento.formapagamento ");
		sqlStr.append(" INNER JOIN operadoraCartao ON operadoraCartao.codigo = formaPagamentoNegociacaoRecebimento.operadoraCartao ");
		sqlStr.append(" INNER JOIN negociacaoRecebimento ON negociacaoRecebimento.codigo = formaPagamentoNegociacaoRecebimento.negociacaoRecebimento ");		
		sqlStr.append(" LEFT JOIN agencia as agenciaCC ON agenciaCC.codigo = contaCorrente.agencia ");
		sqlStr.append(" LEFT JOIN banco as bancoCC ON bancoCC.codigo = agenciaCC.banco ");
		sqlStr.append(" LEFT JOIN contaCorrente as contaCorrenteOperadoraCartao ON contaCorrenteOperadoraCartao.codigo = formaPagamentoNegociacaoRecebimento.contaCorrenteOperadoraCartao ");
		sqlStr.append(" LEFT JOIN agencia as agenciaCCOC ON agenciaCCOC.codigo = contaCorrenteOperadoraCartao.agencia ");
		sqlStr.append(" LEFT JOIN banco as bancoCCOC ON bancoCCOC.codigo = agenciaCCOC.banco ");
		sqlStr.append(" LEFT JOIN matricula ON matricula.matricula = negociacaoRecebimento.matricula ");
		sqlStr.append(" LEFT JOIN pessoa ON pessoa.codigo = negociacaoRecebimento.pessoa ");
		sqlStr.append(" LEFT JOIN usuario ON usuario.codigo = formaPgtoNegociacaoRecebimentoCartaoCredito.responsavelPelaBaixa ");
		sqlStr.append(" LEFT JOIN usuario as responsavelAjustarValorLiquido ON responsavelAjustarValorLiquido.codigo = formaPgtoNegociacaoRecebimentoCartaoCredito.responsavelAjustarValorLiquido ");
		sqlStr.append(" LEFT JOIN parceiro ON negociacaorecebimento.parceiro = parceiro.codigo ");
		sqlStr.append(" LEFT JOIN fornecedor ON negociacaorecebimento.fornecedor = fornecedor.codigo ");
		sqlStr.append(" LEFT JOIN unidadeensino ON negociacaorecebimento.unidadeensino = unidadeensino.codigo ");
		sqlStr.append(" LEFT JOIN configuracaofinanceirocartao ON formaPagamentoNegociacaoRecebimento.configuracaorecebimentocartaocredito = configuracaofinanceirocartao.codigo ");
		return sqlStr;
	}
	
	public List<MapaPendenciaCartaoCreditoVO> consultarPorOrigemFormaPagamentoNegociacaoRecebimentoCartaoCredito(String formapagamentonegociacaorecebimentocartaocredito, UsuarioVO usuario) throws Exception {
		StringBuffer sqlStr = getSQLPadraoConsultaCompleta();
		sqlStr.append(" where formaPgtoNegociacaoRecebimentoCartaoCredito.codigo = ").append(formapagamentonegociacaorecebimentocartaocredito).append(" ");
		return (montarDadosConsultaCompleta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString()), usuario));
	}
	
	public MapaPendenciaCartaoCreditoVO consultarPorCodigoFormaPagamentoNegociacaoRecebimentoCartaoCredito(Integer formapagamentonegociacaorecebimentocartaocredito, UsuarioVO usuario) throws Exception {
		StringBuffer sqlStr = getSQLPadraoConsultaCompleta();
		sqlStr.append(" where formaPgtoNegociacaoRecebimentoCartaoCredito.codigo = ").append(formapagamentonegociacaorecebimentocartaocredito).append(" ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		MapaPendenciaCartaoCreditoVO obj = new MapaPendenciaCartaoCreditoVO();
		if (rs.next()) {
			montarDadosCompleto(obj, rs, usuario);
		}
		return obj;
	}

	/**
	 * Monta a consulta com os filtros escolhidos na tela de MapaPendenciaCartaoCredito.
	 */
	@Override
	public List<MapaPendenciaCartaoCreditoVO> consultarPorParcelasCartaoCredito(String situacao, Date dataEmissaoInicial, Date dataEmissaoFinal, Date dataVencimentoInicial, Date dataVencimentoFinal, Date dataRecebimentoOperadoraInicial, Date dataRecebimentoOperadoraFinal, List<UnidadeEnsinoVO> listaUnidadeEnsino, List<OperadoraCartaoVO> listaOperadoraCartao, Integer contaCorrente, String ordenarPor, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String filtro = "WHERE";
		StringBuffer sqlStr = getSQLPadraoConsultaCompleta();
		if (situacao.equals("AR") || situacao.equals("RE")) {
			sqlStr.append(filtro).append(" formaPgtoNegociacaoRecebimentoCartaoCredito.situacao = '").append(situacao).append("' ");
			filtro = "AND";
		}
		if (dataEmissaoInicial != null) {
			sqlStr.append(filtro).append(" formaPgtoNegociacaoRecebimentoCartaoCredito.dataEmissao >= '").append(Uteis.getDataJDBCTimestamp(Uteis.getDateTime(dataEmissaoInicial, 0, 0, 0))).append("' ");
			filtro = "AND";
		}
		if (dataEmissaoFinal != null) {
			sqlStr.append(filtro).append(" formaPgtoNegociacaoRecebimentoCartaoCredito.dataEmissao <= '").append(Uteis.getDataJDBCTimestamp(Uteis.getDateHoraFinalDia(dataEmissaoFinal))).append("' ");
			filtro = "AND";
		}
		if (dataVencimentoInicial != null) {
			sqlStr.append(filtro).append(" formaPgtoNegociacaoRecebimentoCartaoCredito.dataVencimento >= '").append(Uteis.getDataJDBCTimestamp(Uteis.getDateTime(dataVencimentoInicial, 0, 0, 0))).append("' ");
			filtro = "AND";
		}
		if (dataVencimentoFinal != null) {
			sqlStr.append(filtro).append(" formaPgtoNegociacaoRecebimentoCartaoCredito.dataVencimento <= '").append(Uteis.getDataJDBCTimestamp(Uteis.getDateHoraFinalDia(dataVencimentoFinal))).append("' ");
			filtro = "AND";
		}
		if (dataRecebimentoOperadoraInicial != null) {
			sqlStr.append(filtro).append(" formaPgtoNegociacaoRecebimentoCartaoCredito.dataRecebimento >= '").append(Uteis.getDataJDBCTimestamp(Uteis.getDateTime(dataRecebimentoOperadoraInicial, 0, 0, 0))).append("' ");
			filtro = "AND";
		}
		if (dataRecebimentoOperadoraFinal != null) {
			sqlStr.append(filtro).append(" formaPgtoNegociacaoRecebimentoCartaoCredito.dataRecebimento <= '").append(Uteis.getDataJDBCTimestamp(Uteis.getDateHoraFinalDia(dataRecebimentoOperadoraFinal))).append("' ");
			filtro = "AND";
		}
		if (Uteis.isAtributoPreenchido(listaUnidadeEnsino)) {
			sqlStr.append(filtro).append(" negociacaoRecebimento.unidadeEnsino in ( ").append(UteisTexto.converteListaEntidadeCampoCodigoParaString(listaUnidadeEnsino)).append(") ");
			filtro = "AND";
		}
		if (Uteis.isAtributoPreenchido(listaOperadoraCartao)) {
			sqlStr.append(filtro).append(" operadoraCartao.codigo in ( ").append(UteisTexto.converteListaEntidadeCampoCodigoParaString(listaOperadoraCartao)).append(" )");
			filtro = "AND";
		}

		if (Uteis.isAtributoPreenchido(contaCorrente)) {
			sqlStr.append(filtro).append(" contaCorrente.codigo = ").append(contaCorrente).append(" ");
		}
		sqlStr.append(" and (formaPgtoNegociacaoRecebimentoCartaoCredito.tipofinanciamento = 'OPERADORA' or (formaPgtoNegociacaoRecebimentoCartaoCredito.tipofinanciamento = 'INSTITUICAO' and formaPgtoNegociacaoRecebimentoCartaoCredito.contareceber is null)) ");
		if (ordenarPor != null && ordenarPor.equals("PR")) {
			sqlStr.append(" order by formaPgtoNegociacaoRecebimentoCartaoCredito.dataVencimento, \"pessoa.nome\" ");
		}
		if (ordenarPor != null && ordenarPor.equals("EM")) {
			sqlStr.append(" order by formaPgtoNegociacaoRecebimentoCartaoCredito.dataEmissao, \"pessoa.nome\"  ");
		}
		if (ordenarPor != null && ordenarPor.equals("OP")) {
			sqlStr.append(" order by operadoraCartao.nome, \"pessoa.nome\", formaPgtoNegociacaoRecebimentoCartaoCredito.dataVencimento");
		}
		if (ordenarPor != null && ordenarPor.equals("NO")) {
			sqlStr.append(" order by \"pessoa.nome\", formaPgtoNegociacaoRecebimentoCartaoCredito.dataVencimento ");
		}
		return (montarDadosConsultaCompleta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString()), usuario));
	}

	public List<MapaPendenciaCartaoCreditoVO> montarDadosConsultaCompleta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
		List<MapaPendenciaCartaoCreditoVO> vetResultado = new ArrayList<MapaPendenciaCartaoCreditoVO>(0);
		while (tabelaResultado.next()) {
			MapaPendenciaCartaoCreditoVO obj = new MapaPendenciaCartaoCreditoVO();
			montarDadosCompleto(obj, tabelaResultado, usuario);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	/**
	 * Monta os dados do Objeto MapaPendenciaCartaoCredito.
	 */
	private void montarDadosCompleto(MapaPendenciaCartaoCreditoVO obj, SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
		// Dados do MapaPendenciaCartaoCreditoVO
		obj.getFormaPagamentoNegociacaoRecebimentoVO().getNegociacaoRecebimentoVO().setCodigo(dadosSQL.getInt("negociacaorecebimento.codigo"));
		obj.getFormaPagamentoNegociacaoRecebimentoVO().getNegociacaoRecebimentoVO().setData(dadosSQL.getDate("negociacaorecebimento.data"));
		obj.getFormaPagamentoNegociacaoRecebimentoVO().getNegociacaoRecebimentoVO().setTipoPessoa(dadosSQL.getString("negociacaorecebimento.tipoPessoa"));
		obj.getFormaPagamentoNegociacaoRecebimentoVO().getNegociacaoRecebimentoVO().getPessoa().setCodigo(dadosSQL.getInt("pessoa.codigo"));
		obj.getFormaPagamentoNegociacaoRecebimentoVO().getNegociacaoRecebimentoVO().getPessoa().setNome(dadosSQL.getString("pessoa.nome"));
		obj.getFormaPagamentoNegociacaoRecebimentoVO().getNegociacaoRecebimentoVO().getParceiroVO().setCodigo(dadosSQL.getInt("parceiro.codigo"));
		obj.getFormaPagamentoNegociacaoRecebimentoVO().getNegociacaoRecebimentoVO().getParceiroVO().setNome(dadosSQL.getString("parceiro.nome"));
		obj.getFormaPagamentoNegociacaoRecebimentoVO().getNegociacaoRecebimentoVO().getFornecedor().setCodigo(dadosSQL.getInt("fornecedor.codigo"));
		obj.getFormaPagamentoNegociacaoRecebimentoVO().getNegociacaoRecebimentoVO().getFornecedor().setNome(dadosSQL.getString("fornecedor.nome"));
		obj.getFormaPagamentoNegociacaoRecebimentoVO().getNegociacaoRecebimentoVO().getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeensino.codigo"));
		obj.getFormaPagamentoNegociacaoRecebimentoVO().getNegociacaoRecebimentoVO().getUnidadeEnsino().setNome(dadosSQL.getString("unidadeensino.nome"));
		obj.getFormaPagamentoNegociacaoRecebimentoVO().getNegociacaoRecebimentoVO().setValorTotalRecebimento(dadosSQL.getDouble("negociacaorecebimento.valorTotalRecebimento"));
		if (obj.getFormaPagamentoNegociacaoRecebimentoVO().getNegociacaoRecebimentoVO().getTipoAluno()) {
			obj.getFormaPagamentoNegociacaoRecebimentoVO().getNegociacaoRecebimentoVO().setMatricula(dadosSQL.getString("negociacaorecebimento.matricula"));
		}
		
		obj.getFormaPagamentoNegociacaoRecebimentoVO().getConfiguracaoFinanceiroCartaoVO().setCodigo(dadosSQL.getInt("configuracaofinanceirocartao.codigo"));

		obj.getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamento().setNome(dadosSQL.getString("formapagamento.nome"));
		obj.getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamento().setCodigo(dadosSQL.getInt("formapagamento.codigo"));
		obj.getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamento().setTipo(dadosSQL.getString("formapagamento.tipo"));
		
		obj.getFormaPagamentoNegociacaoRecebimentoVO().getOperadoraCartaoVO().setNome(dadosSQL.getString("operadoraCartao.nome"));
		obj.getFormaPagamentoNegociacaoRecebimentoVO().getOperadoraCartaoVO().setCodigo(dadosSQL.getInt("operadoraCartao.codigo"));
		obj.getFormaPagamentoNegociacaoRecebimentoVO().getOperadoraCartaoVO().setTipo(dadosSQL.getString("operadoraCartao.tipo"));
		
		obj.getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamento().setNome(dadosSQL.getString("formapagamento.nome"));
		obj.getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamento().setCodigo(dadosSQL.getInt("formapagamento.codigo"));
		obj.getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamento().setTipo(dadosSQL.getString("formapagamento.tipo"));
		
		if (obj.getFormaPagamentoNegociacaoRecebimentoVO().getOperadoraCartaoVO().getTipo().equals("CARTAO_CREDITO")) {
			if (Uteis.isAtributoPreenchido(dadosSQL.getString("operadoraCartao.operadoracartaocredito"))) {
				obj.getFormaPagamentoNegociacaoRecebimentoVO().getOperadoraCartaoVO().setOperadoraCartaoCreditoEnum(OperadoraCartaoCreditoEnum.valueOf(dadosSQL.getString("operadoraCartao.operadoracartaocredito")));
			} else {
				throw new Exception("A Operadora de cartao : " + obj.getFormaPagamentoNegociacaoRecebimentoVO().getOperadoraCartaoVO().getNome() + " esta sem operadora cadastrasta, efetue o cadastro e retorno a consulta. ");
			}
		}
		obj.getFormaPagamentoNegociacaoRecebimentoVO().setCodigo(dadosSQL.getInt("formaPagamentoNegociacaoRecebimento.codigo"));
		obj.getFormaPagamentoNegociacaoRecebimentoVO().setQtdeParcelasCartaoCredito(dadosSQL.getInt("formaPagamentoNegociacaoRecebimento.qtdeParcelasCartaoCredito"));
		obj.getFormaPagamentoNegociacaoRecebimentoVO().setValorRecebimento(dadosSQL.getDouble("formaPagamentoNegociacaoRecebimento.valorrecebimento"));
		obj.getFormaPagamentoNegociacaoRecebimentoVO().setTaxaDeOperacao(dadosSQL.getDouble("formaPagamentoNegociacaoRecebimento.taxaOperadora"));
		obj.getFormaPagamentoNegociacaoRecebimentoVO().setTaxaDeAntecipacao(dadosSQL.getDouble("formaPagamentoNegociacaoRecebimento.taxaAntecipacao"));
		if (obj.getFormaPagamentoNegociacaoRecebimentoVO().getTaxaDeAntecipacao().equals(0.0)) {
			obj.getFormaPagamentoNegociacaoRecebimentoVO().setTaxaDeAntecipacao(obj.getFormaPagamentoNegociacaoRecebimentoVO().getTaxaDeOperacao());
		}
		obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setCodigo(dadosSQL.getInt("formaPgtoNegociacaoRecebimentoCartaoCredito.codigo"));
		obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setChaveDaTransacao(dadosSQL.getString("formaPgtoNegociacaoRecebimentoCartaoCredito.chaveDaTransacao"));
		obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setNumeroParcela(dadosSQL.getString("formaPgtoNegociacaoRecebimentoCartaoCredito.numeroParcela"));
		obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setNomeCartaoCredito(dadosSQL.getString("formaPgtoNegociacaoRecebimentoCartaoCredito.nomecartaocredito"));
		obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setNumeroCartao(dadosSQL.getString("formaPgtoNegociacaoRecebimentoCartaoCredito.numerocartao"));
		obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setNumeroReciboTransacao(dadosSQL.getString("formaPgtoNegociacaoRecebimentoCartaoCredito.recibotransacao"));
		obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setValorParcela(dadosSQL.getDouble("formaPgtoNegociacaoRecebimentoCartaoCredito.valorParcela"));
		obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setDataEmissao(dadosSQL.getTimestamp("formaPgtoNegociacaoRecebimentoCartaoCredito.dataEmissao"));
		obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setDataVencimento(dadosSQL.getTimestamp("formaPgtoNegociacaoRecebimentoCartaoCredito.dataVencimento"));
		obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setDataRecebimento(dadosSQL.getTimestamp("formaPgtoNegociacaoRecebimentoCartaoCredito.dataRecebimento"));
		obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setSituacao(dadosSQL.getString("formaPgtoNegociacaoRecebimentoCartaoCredito.situacao"));
		obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setTipoFinanciamentoEnum(TipoFinanciamentoEnum.valueOf(dadosSQL.getString("formaPgtoNegociacaoRecebimentoCartaoCredito.tipofinanciamento")));
		obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getResponsavelPelaBaixa().setCodigo(dadosSQL.getInt("usuario.codigo"));
		obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getResponsavelPelaBaixa().setNome(dadosSQL.getString("usuario.nome"));
		obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setAjustarValorLiquido(dadosSQL.getDouble("formaPgtoNegociacaoRecebimentoCartaoCredito.ajustarValorLiquido"));
		obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getResponsavelAjustarValorLiquido().setCodigo(dadosSQL.getInt("responsavelAjustarValorLiquido.codigo"));
		obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getResponsavelAjustarValorLiquido().setNome(dadosSQL.getString("responsavelAjustarValorLiquido.nome"));
		obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setConfiguracaoContabilExistente(getFacadeFactory().getConfiguracaoContabilFacade().consultaSeExisteConfiguracaoContabilPorCodigoUnidadeEnsinoPorTipoRegraContabil(obj.getFormaPagamentoNegociacaoRecebimentoVO().getNegociacaoRecebimentoVO().getUnidadeEnsino().getCodigo(), TipoRegraContabilEnum.CARTAO_CREDITO, usuario));
		obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setLancamentoContabil(getFacadeFactory().getLancamentoContabilFacade().consultaPorCodOrigemPorTipoOrigemPorTipoPlanoConta(obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getCodigo().toString(), TipoOrigemLancamentoContabilEnum.CARTAO_CREDITO, false, usuario));

		obj.getFormaPagamentoNegociacaoRecebimentoVO().getContaCorrente().setCodigo(dadosSQL.getInt("contaCorrente.codigo"));
		obj.getFormaPagamentoNegociacaoRecebimentoVO().getContaCorrente().getAgencia().getBanco().setCodigo(dadosSQL.getInt("bancoCC.codigo"));
		obj.getFormaPagamentoNegociacaoRecebimentoVO().getContaCorrente().getAgencia().getBanco().setNrBanco(dadosSQL.getString("bancoCC.nrBanco"));
		obj.getFormaPagamentoNegociacaoRecebimentoVO().getContaCorrente().getAgencia().getBanco().setNome(dadosSQL.getString("bancoCC.nome"));

		obj.getFormaPagamentoNegociacaoRecebimentoVO().getContaCorrenteOperadoraCartaoVO().setCodigo(dadosSQL.getInt("contaCorrenteOperadoraCartao.codigo"));
		obj.getFormaPagamentoNegociacaoRecebimentoVO().getContaCorrenteOperadoraCartaoVO().setNumero(dadosSQL.getString("contaCorrenteOperadoraCartao.numero"));
		obj.getFormaPagamentoNegociacaoRecebimentoVO().getContaCorrenteOperadoraCartaoVO().setDigito(dadosSQL.getString("contaCorrenteOperadoraCartao.digito"));
		obj.getFormaPagamentoNegociacaoRecebimentoVO().getContaCorrenteOperadoraCartaoVO().setCarteira(dadosSQL.getString("contaCorrenteOperadoraCartao.carteira"));
		obj.getFormaPagamentoNegociacaoRecebimentoVO().getContaCorrenteOperadoraCartaoVO().setNomeApresentacaoSistema(dadosSQL.getString("contaCorrenteOperadoraCartao.nomeApresentacaoSistema"));
		obj.getFormaPagamentoNegociacaoRecebimentoVO().getContaCorrenteOperadoraCartaoVO().setContaCaixa(dadosSQL.getBoolean("contaCorrenteOperadoraCartao.contacaixa"));
		obj.getFormaPagamentoNegociacaoRecebimentoVO().getContaCorrenteOperadoraCartaoVO().setUtilizaTaxaCartaoCredito(dadosSQL.getBoolean("contaCorrenteOperadoraCartao.utilizaTaxaCartaoCredito"));
		obj.getFormaPagamentoNegociacaoRecebimentoVO().getContaCorrenteOperadoraCartaoVO().getAgencia().setCodigo(dadosSQL.getInt("agenciaCCOC.codigo"));
		obj.getFormaPagamentoNegociacaoRecebimentoVO().getContaCorrenteOperadoraCartaoVO().getAgencia().setNumeroAgencia(dadosSQL.getString("agenciaCCOC.numeroAgencia"));
		obj.getFormaPagamentoNegociacaoRecebimentoVO().getContaCorrenteOperadoraCartaoVO().getAgencia().getBanco().setCodigo(dadosSQL.getInt("bancoCCOC.codigo"));
		obj.getFormaPagamentoNegociacaoRecebimentoVO().getContaCorrenteOperadoraCartaoVO().getAgencia().getBanco().setNrBanco(dadosSQL.getString("bancoCCOC.nrBanco"));
		obj.getFormaPagamentoNegociacaoRecebimentoVO().getContaCorrenteOperadoraCartaoVO().getAgencia().getBanco().setNome(dadosSQL.getString("bancoCCOC.nome"));
		if (Uteis.isAtributoPreenchido(dadosSQL.getString("contaCorrenteOperadoraCartao.tipoContaCorrente"))) {
			obj.getFormaPagamentoNegociacaoRecebimentoVO().getContaCorrenteOperadoraCartaoVO().setTipoContaCorrenteEnum(TipoContaCorrenteEnum.valueOf(dadosSQL.getString("contaCorrenteOperadoraCartao.tipoContaCorrente")));
		}
		obj.getFormaPagamentoNegociacaoRecebimentoVO().getCategoriaDespesaVO().setCodigo(dadosSQL.getInt("categoriaDespesa.codigo"));

	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
	 */
	public static String getIdEntidade() {
		return MapaPendenciaCartaoCredito.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		MapaPendenciaCartaoCredito.idEntidade = idEntidade;
	}

	

	/**
	 * SQL responsável para buscar os dados necessários para manipulação do MapaPendenciaCartaoCredito
	 * 
	 * @return String Contendo sql Padráo para consulta dos dados.
	 */
	private StringBuffer getSQLPadraoConsultaCompletaDCC() {
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("SELECT operadoraCartao.nome as \"operadoraCartao.nome\", operadoraCartao.codigo as \"operadoraCartao.codigo\", ");
		sqlStr.append("operadoraCartao.tipo as \"operadoraCartao.tipo\", operadoraCartao.operadoracartaocredito as \"operadoraCartao.operadoracartaocredito\",  ");
		sqlStr.append("negociacaorecebimentodcc.tipoPessoa as \"negociacaorecebimentodcc.tipoPessoa\", negociacaorecebimentodcc.matricula as \"negociacaorecebimentodcc.matricula\", ");
		sqlStr.append(" parceiro.codigo as \"parceiro.codigo\", parceiro.nome as \"parceiro.nome\",  ");
		sqlStr.append(" fornecedor.codigo as \"fornecedor.codigo\", fornecedor.nome as \"fornecedor.nome\",  ");
		sqlStr.append(" pessoa.codigo as \"pessoa.codigo\", pessoa.nome as \"pessoa.nome\",  ");
		
		sqlStr.append(" formapagamento.codigo as \"formapagamento.codigo\", formapagamento.nome as \"formapagamento.nome\", ");
		sqlStr.append(" formapagamento.tipo as \"formapagamento.tipo\",  ");
		
		sqlStr.append("formaPagamentoNegociacaoRecebimentodcc.taxaOperadora as \"formapagamentonegociacaorecebimentodcc.taxaOperadora\",  formaPagamentoNegociacaoRecebimentodcc.codigo as \"formapagamentonegociacaorecebimentodcc.codigo\", ");
		sqlStr.append("formaPagamentoNegociacaoRecebimentodcc.taxaAntecipacao as \"formapagamentonegociacaorecebimentodcc.taxaAntecipacao\",  ");
		sqlStr.append("formaPgtoNegociacaoRecebimentoCartaoCredito.codigo as \"formaPgtoNegociacaoRecebimentoCartaoCredito.codigo\",  ");
		sqlStr.append("formaPgtoNegociacaoRecebimentoCartaoCredito.nomecartaocredito as \"formaPgtoNegociacaoRecebimentoCartaoCredito.nomecartaocredito\",  ");
		sqlStr.append("formaPgtoNegociacaoRecebimentoCartaoCredito.numerocartao as \"formaPgtoNegociacaoRecebimentoCartaoCredito.numerocartao\",  ");
		sqlStr.append(" formaPgtoNegociacaoRecebimentoCartaoCredito.numerorecibotransacao as \"formaPgtoNegociacaoRecebimentoCartaoCredito.recibotransacao\", ");
		sqlStr.append("formaPgtoNegociacaoRecebimentoCartaoCredito.numeroParcela as \"formaPgtoNegociacaoRecebimentoCartaoCredito.numeroParcela\",  ");
		sqlStr.append("formaPgtoNegociacaoRecebimentoCartaoCredito.valorParcela as \"formaPgtoNegociacaoRecebimentoCartaoCredito.valorParcela\",  ");
		sqlStr.append("formaPgtoNegociacaoRecebimentoCartaoCredito.dataEmissao as \"formaPgtoNegociacaoRecebimentoCartaoCredito.dataEmissao\",  ");
		sqlStr.append("formaPgtoNegociacaoRecebimentoCartaoCredito.dataVencimento as \"formaPgtoNegociacaoRecebimentoCartaoCredito.dataVencimento\",  ");
		sqlStr.append("formaPgtoNegociacaoRecebimentoCartaoCredito.dataRecebimento as \"formaPgtoNegociacaoRecebimentoCartaoCredito.dataRecebimento\",  ");
		sqlStr.append("formaPgtoNegociacaoRecebimentoCartaoCredito.situacao as \"formaPgtoNegociacaoRecebimentoCartaoCredito.situacao\",  ");
		sqlStr.append(" formaPgtoNegociacaoRecebimentoCartaoCredito.ajustarValorLiquido as \"formaPgtoNegociacaoRecebimentoCartaoCredito.ajustarValorLiquido\", ");
		sqlStr.append("contareceber.nossonumero as \"contareceber.nossonumero\",  ");
		sqlStr.append("formaPagamentoNegociacaoRecebimentodcc.codigo as \"formaPagamentoNegociacaoRecebimento.codigo\",  ");
		sqlStr.append("usuario.codigo as \"usuario.codigo\", usuario.nome as \"usuario.nome\",  bancoCC.codigo as \"bancoCC.codigo\", ");
		sqlStr.append(" responsavelAjustarValorLiquido.codigo as \"responsavelAjustarValorLiquido.codigo\", responsavelAjustarValorLiquido.nome as \"responsavelAjustarValorLiquido.nome\", ");
		sqlStr.append("bancoCC.nrBanco as \"bancoCC.nrBanco\", bancoCC.nome as \"bancoCC.nome\",  contaCorrente.codigo as \"contaCorrente.codigo\",  ");
		sqlStr.append("bancoCCOC.codigo as \"bancoCCOC.codigo\", bancoCCOC.nrBanco as \"bancoCCOC.nrBanco\", bancoCCOC.nome as \"bancoCCOC.nome\",  ");
		sqlStr.append("contaCorrenteOperadoraCartao.codigo as \"contaCorrenteOperadoraCartao.codigo\",  categoriaDespesa.codigo as \"categoriaDespesa.codigo\",  ");
		sqlStr.append(" unidadeensino.codigo as \"unidadeensino.codigo\", unidadeensino.nome as \"unidadeensino.nome\" ");
		sqlStr.append("FROM formaPagamentoNegociacaoRecebimentoCartaoCredito as formaPgtoNegociacaoRecebimentoCartaoCredito  ");
		sqlStr.append("INNER JOIN formaPagamentoNegociacaoRecebimentodcc ON formaPagamentoNegociacaoRecebimentodcc.formaPagamentoNegociacaoRecebimentoCartaoCredito = formaPgtoNegociacaoRecebimentoCartaoCredito.codigo   ");
		sqlStr.append("LEFT JOIN categoriaDespesa ON categoriaDespesa.codigo = formaPagamentoNegociacaoRecebimentodcc.categoriaDespesa  ");
		sqlStr.append("INNER JOIN contaCorrente ON contaCorrente.codigo = formaPagamentoNegociacaoRecebimentodcc.contaCorrente  ");
		sqlStr.append("INNER JOIN operadoraCartao ON operadoraCartao.codigo = formaPagamentoNegociacaoRecebimentodcc.operadoraCartao  ");
		sqlStr.append("INNER JOIN formapagamento on formapagamento.codigo = formaPagamentoNegociacaoRecebimentodcc.formapagamento ");
		sqlStr.append("INNER JOIN negociacaoRecebimentodcc ON negociacaoRecebimentodcc.codigo = formaPagamentoNegociacaoRecebimentodcc.negociacaoRecebimentodcc  ");
		sqlStr.append("INNER JOIN contareceber on contareceber.codigo = formaPgtoNegociacaoRecebimentoCartaoCredito.contareceber ");
		sqlStr.append("LEFT JOIN agencia as agenciaCC ON agenciaCC.codigo = contaCorrente.agencia  ");
		sqlStr.append("LEFT JOIN banco as bancoCC ON bancoCC.codigo = agenciaCC.banco  ");
		sqlStr.append("LEFT JOIN contaCorrente as contaCorrenteOperadoraCartao ON contaCorrenteOperadoraCartao.codigo = formaPagamentoNegociacaoRecebimentodcc.contaCorrenteOperadoraCartao  ");
		sqlStr.append("LEFT JOIN agencia as agenciaCCOC ON agenciaCCOC.codigo = contaCorrenteOperadoraCartao.agencia  ");
		sqlStr.append("LEFT JOIN banco as bancoCCOC ON bancoCCOC.codigo = agenciaCCOC.banco  ");
		sqlStr.append("LEFT JOIN pessoa ON pessoa.codigo = negociacaoRecebimentodcc.pessoa  ");
		sqlStr.append("LEFT JOIN usuario ON usuario.codigo = formaPgtoNegociacaoRecebimentoCartaoCredito.responsavelPelaBaixa  ");
		sqlStr.append("LEFT JOIN usuario as responsavelAjustarValorLiquido ON responsavelAjustarValorLiquido.codigo = formaPgtoNegociacaoRecebimentoCartaoCredito.responsavelAjustarValorLiquido ");
		sqlStr.append("LEFT JOIN parceiro ON negociacaorecebimentodcc.parceiro = parceiro.codigo  ");
		sqlStr.append("LEFT JOIN fornecedor ON negociacaorecebimentodcc.fornecedor = fornecedor.codigo ");
		sqlStr.append("LEFT JOIN unidadeensino ON negociacaorecebimentodcc.unidadeensino = unidadeensino.codigo ");
		return sqlStr;
	}

	@Override
	public List<MapaPendenciaCartaoCreditoVO> consultarOperacoesInstituicaoDCC(boolean previsto, boolean recebidos, boolean recusado, boolean cancelado, boolean cancelamentopendente, boolean estornopendente, Date dataEmissaoInicial, Date dataEmissaoFinal, Date dataVencimentoInicial, Date dataVencimentoFinal, List<UnidadeEnsinoVO> listaUnidadeEnsino, List<OperadoraCartaoVO> listaOperadoraCartao, Integer contaCorrente, String ordenarPor, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append(getSQLPadraoConsultaCompletaDCC());
		if (previsto) {
			sqlStr.append(" where formaPgtoNegociacaoRecebimentoCartaoCredito.situacao = 'AR'");
		} else if (recebidos) {
			sqlStr.append(" where formaPgtoNegociacaoRecebimentoCartaoCredito.situacao = 'RE'");
		} else if (recusado) {
			sqlStr.append(" where formaPgtoNegociacaoRecebimentoCartaoCredito.situacao = 'RC'");
		} else if (cancelado) {
			sqlStr.append(" where formaPgtoNegociacaoRecebimentoCartaoCredito.situacao = 'CF'");
		} else if (cancelamentopendente) {
			sqlStr.append(" where formaPgtoNegociacaoRecebimentoCartaoCredito.situacao = 'CP'");
		} else if (estornopendente) {
			sqlStr.append(" where formaPgtoNegociacaoRecebimentoCartaoCredito.situacao = 'EP'");
		}
		if (dataEmissaoInicial != null) {
			sqlStr.append(" and formaPgtoNegociacaoRecebimentoCartaoCredito.dataEmissao >= '").append(Uteis.getDataJDBCTimestamp(Uteis.getDateTime(dataEmissaoInicial, 0, 0, 0))).append("' ");
		}
		if (dataEmissaoFinal != null) {
			sqlStr.append(" and formaPgtoNegociacaoRecebimentoCartaoCredito.dataEmissao <= '").append(Uteis.getDataJDBCTimestamp(Uteis.getDateHoraFinalDia(dataEmissaoFinal))).append("' ");
		}
		if (dataVencimentoInicial != null) {
			sqlStr.append(" and formaPgtoNegociacaoRecebimentoCartaoCredito.dataVencimento >= '").append(Uteis.getDataJDBCTimestamp(Uteis.getDateTime(dataVencimentoInicial, 0, 0, 0))).append("' ");
		}
		if (dataVencimentoFinal != null) {
			sqlStr.append(" and formaPgtoNegociacaoRecebimentoCartaoCredito.dataVencimento <= '").append(Uteis.getDataJDBCTimestamp(Uteis.getDateHoraFinalDia(dataVencimentoFinal))).append("' ");
		}
		if (Uteis.isAtributoPreenchido(listaUnidadeEnsino)) {
			sqlStr.append(" and negociacaoRecebimentodcc.unidadeEnsino in ( ").append(UteisTexto.converteListaEntidadeCampoCodigoParaString(listaUnidadeEnsino)).append(") ");
		}
		if (Uteis.isAtributoPreenchido(listaOperadoraCartao)) {
			sqlStr.append(" and operadoraCartao.codigo in ( ").append(UteisTexto.converteListaEntidadeCampoCodigoParaString(listaOperadoraCartao)).append(" )");
		}

		if (Uteis.isAtributoPreenchido(contaCorrente)) {
			sqlStr.append(" and contaCorrente.codigo = ").append(contaCorrente).append(" ");
		}

		sqlStr.append(" and formaPgtoNegociacaoRecebimentoCartaoCredito.tipofinanciamento = 'INSTITUICAO'");
		if (ordenarPor != null && ordenarPor.equals("PR")) {
			sqlStr.append(" order by formaPgtoNegociacaoRecebimentoCartaoCredito.dataVencimento, \"pessoa.nome\" ");
		}
		if (ordenarPor != null && ordenarPor.equals("EM")) {
			sqlStr.append(" order by formaPgtoNegociacaoRecebimentoCartaoCredito.dataEmissao, \"pessoa.nome\"  ");
		}
		if (ordenarPor != null && ordenarPor.equals("OP")) {
			sqlStr.append(" order by operadoraCartao.nome, \"pessoa.nome\"");
		}
		if (ordenarPor != null && ordenarPor.equals("NO")) {
			sqlStr.append(" order by \"pessoa.nome\" ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaCompletaDCC(tabelaResultado);
	}

	public List<MapaPendenciaCartaoCreditoVO> montarDadosConsultaCompletaDCC(SqlRowSet tabelaResultado) throws Exception {
		List<MapaPendenciaCartaoCreditoVO> vetResultado = new ArrayList<MapaPendenciaCartaoCreditoVO>(0);
		while (tabelaResultado.next()) {
			MapaPendenciaCartaoCreditoVO obj = new MapaPendenciaCartaoCreditoVO();
			montarDadosCompletoDCC(obj, tabelaResultado);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	/**
	 * Monta os dados do Objeto MapaPendenciaCartaoCredito.
	 */
	private void montarDadosCompletoDCC(MapaPendenciaCartaoCreditoVO obj, SqlRowSet dadosSQL) throws Exception {
		obj.getFormaPagamentoNegociacaoRecebimentoVO().getNegociacaoRecebimentoVO().setTipoPessoa(dadosSQL.getString("negociacaorecebimentodcc.tipoPessoa"));
		obj.getFormaPagamentoNegociacaoRecebimentoVO().getNegociacaoRecebimentoVO().getPessoa().setCodigo(dadosSQL.getInt("pessoa.codigo"));
		obj.getFormaPagamentoNegociacaoRecebimentoVO().getNegociacaoRecebimentoVO().getPessoa().setNome(dadosSQL.getString("pessoa.nome"));
		obj.getFormaPagamentoNegociacaoRecebimentoVO().getNegociacaoRecebimentoVO().getParceiroVO().setCodigo(dadosSQL.getInt("parceiro.codigo"));
		obj.getFormaPagamentoNegociacaoRecebimentoVO().getNegociacaoRecebimentoVO().getParceiroVO().setNome(dadosSQL.getString("parceiro.nome"));
		obj.getFormaPagamentoNegociacaoRecebimentoVO().getNegociacaoRecebimentoVO().getFornecedor().setCodigo(dadosSQL.getInt("fornecedor.codigo"));
		obj.getFormaPagamentoNegociacaoRecebimentoVO().getNegociacaoRecebimentoVO().getFornecedor().setNome(dadosSQL.getString("fornecedor.nome"));
		obj.getFormaPagamentoNegociacaoRecebimentoVO().getNegociacaoRecebimentoVO().getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeensino.codigo"));
		obj.getFormaPagamentoNegociacaoRecebimentoVO().getNegociacaoRecebimentoVO().getUnidadeEnsino().setNome(dadosSQL.getString("unidadeensino.nome"));
		if (obj.getFormaPagamentoNegociacaoRecebimentoVO().getNegociacaoRecebimentoVO().getTipoAluno()) {
			obj.getFormaPagamentoNegociacaoRecebimentoVO().getNegociacaoRecebimentoVO().setMatricula(dadosSQL.getString("negociacaorecebimentodcc.matricula"));
		}
		obj.getFormaPagamentoNegociacaoRecebimentoVO().setCodigo(dadosSQL.getInt("formaPagamentoNegociacaoRecebimentodcc.codigo"));
		obj.getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamento().setNome(dadosSQL.getString("formapagamento.nome"));
		obj.getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamento().setCodigo(dadosSQL.getInt("formapagamento.codigo"));
		obj.getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamento().setTipo(dadosSQL.getString("formapagamento.tipo"));
		obj.getFormaPagamentoNegociacaoRecebimentoVO().getOperadoraCartaoVO().setNome(dadosSQL.getString("operadoraCartao.nome"));
		obj.getFormaPagamentoNegociacaoRecebimentoVO().getOperadoraCartaoVO().setCodigo(dadosSQL.getInt("operadoraCartao.codigo"));
		obj.getFormaPagamentoNegociacaoRecebimentoVO().getOperadoraCartaoVO().setTipo(dadosSQL.getString("operadoraCartao.tipo"));
		obj.getFormaPagamentoNegociacaoRecebimentoVO().getOperadoraCartaoVO().setOperadoraCartaoCreditoEnum(OperadoraCartaoCreditoEnum.valueOf(dadosSQL.getString("operadoraCartao.operadoracartaocredito")));
		obj.getFormaPagamentoNegociacaoRecebimentoVO().setTaxaDeOperacao(dadosSQL.getDouble("formaPagamentoNegociacaoRecebimentodcc.taxaOperadora"));
		obj.getFormaPagamentoNegociacaoRecebimentoVO().setTaxaDeAntecipacao(dadosSQL.getDouble("formaPagamentoNegociacaoRecebimentodcc.taxaAntecipacao"));

		obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setCodigo(dadosSQL.getInt("formaPgtoNegociacaoRecebimentoCartaoCredito.codigo"));
		obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setNumeroParcela(dadosSQL.getString("formaPgtoNegociacaoRecebimentoCartaoCredito.numeroParcela"));
		obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setNomeCartaoCredito(dadosSQL.getString("formaPgtoNegociacaoRecebimentoCartaoCredito.nomecartaocredito"));
		obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setNumeroCartao(dadosSQL.getString("formaPgtoNegociacaoRecebimentoCartaoCredito.numerocartao"));
		obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setNumeroReciboTransacao(dadosSQL.getString("formaPgtoNegociacaoRecebimentoCartaoCredito.recibotransacao"));
		obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setValorParcela(dadosSQL.getDouble("formaPgtoNegociacaoRecebimentoCartaoCredito.valorParcela"));
		obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setDataEmissao(dadosSQL.getTimestamp("formaPgtoNegociacaoRecebimentoCartaoCredito.dataEmissao"));
		obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setDataVencimento(dadosSQL.getTimestamp("formaPgtoNegociacaoRecebimentoCartaoCredito.dataVencimento"));
		obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setDataRecebimento(dadosSQL.getTimestamp("formaPgtoNegociacaoRecebimentoCartaoCredito.dataRecebimento"));
		obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setSituacao(dadosSQL.getString("formaPgtoNegociacaoRecebimentoCartaoCredito.situacao"));
		obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getResponsavelPelaBaixa().setCodigo(dadosSQL.getInt("usuario.codigo"));
		obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getResponsavelPelaBaixa().setNome(dadosSQL.getString("usuario.nome"));
		obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setAjustarValorLiquido(dadosSQL.getDouble("formaPgtoNegociacaoRecebimentoCartaoCredito.ajustarValorLiquido"));
		obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getResponsavelAjustarValorLiquido().setCodigo(dadosSQL.getInt("responsavelAjustarValorLiquido.codigo"));
		obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getResponsavelAjustarValorLiquido().setNome(dadosSQL.getString("responsavelAjustarValorLiquido.nome"));
		obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getContaReceberVO().setNossoNumero(dadosSQL.getString("contareceber.nossonumero"));

		obj.getFormaPagamentoNegociacaoRecebimentoVO().getContaCorrente().setCodigo(dadosSQL.getInt("contaCorrente.codigo"));
		obj.getFormaPagamentoNegociacaoRecebimentoVO().getContaCorrente().getAgencia().getBanco().setCodigo(dadosSQL.getInt("bancoCC.codigo"));
		obj.getFormaPagamentoNegociacaoRecebimentoVO().getContaCorrente().getAgencia().getBanco().setNrBanco(dadosSQL.getString("bancoCC.nrBanco"));
		obj.getFormaPagamentoNegociacaoRecebimentoVO().getContaCorrente().getAgencia().getBanco().setNome(dadosSQL.getString("bancoCC.nome"));
		obj.getFormaPagamentoNegociacaoRecebimentoVO().getContaCorrenteOperadoraCartaoVO().setCodigo(dadosSQL.getInt("contaCorrenteOperadoraCartao.codigo"));
		obj.getFormaPagamentoNegociacaoRecebimentoVO().getContaCorrenteOperadoraCartaoVO().getAgencia().getBanco().setCodigo(dadosSQL.getInt("bancoCCOC.codigo"));
		obj.getFormaPagamentoNegociacaoRecebimentoVO().getContaCorrenteOperadoraCartaoVO().getAgencia().getBanco().setNrBanco(dadosSQL.getString("bancoCCOC.nrBanco"));
		obj.getFormaPagamentoNegociacaoRecebimentoVO().getContaCorrenteOperadoraCartaoVO().getAgencia().getBanco().setNome(dadosSQL.getString("bancoCCOC.nome"));
		obj.getFormaPagamentoNegociacaoRecebimentoVO().getCategoriaDespesaVO().setCodigo(dadosSQL.getInt("categoriaDespesa.codigo"));

	}

	@Override
	public List<MapaPendenciaCartaoCreditoTotalVO> realizarCalculoMapaPendenciaCartaCreditoTotal(Date dataRecebimento, List<MapaPendenciaCartaoCreditoVO> mapaPendenciaCartaoCreditoVOs) throws Exception {
		List<MapaPendenciaCartaoCreditoTotalVO> mapaPendenciaCartaoCreditoTotalVOs = new ArrayList<MapaPendenciaCartaoCreditoTotalVO>(0);
		for (MapaPendenciaCartaoCreditoVO mapaPendenciaCartaoCreditoVO : mapaPendenciaCartaoCreditoVOs) {
			MapaPendenciaCartaoCreditoTotalVO mapaPendenciaCartaoCreditoTotalVO = consultarMapaPendenciaCartaoCreditoTotalVO(mapaPendenciaCartaoCreditoVO.getFormaPagamentoNegociacaoRecebimentoVO().getOperadoraCartaoVO(), mapaPendenciaCartaoCreditoVO.getFormaPagamentoNegociacaoRecebimentoVO().getContaCorrenteOperadoraCartaoVO(), mapaPendenciaCartaoCreditoVO.getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamento(), mapaPendenciaCartaoCreditoTotalVOs);
			mapaPendenciaCartaoCreditoTotalVO.setValorPrevisto(mapaPendenciaCartaoCreditoTotalVO.getValorPrevisto() + mapaPendenciaCartaoCreditoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getValorParcela());
			if (mapaPendenciaCartaoCreditoVO.getEfetuarBaixa()) {
				if (mapaPendenciaCartaoCreditoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getSituacao().equals("AR")) {
					mapaPendenciaCartaoCreditoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setDataRecebimento(dataRecebimento);
					mapaPendenciaCartaoCreditoVO.setValorTaxaUsar(null);
				}
				realizarCalculoMapaPendenciaCartaCreditoTotal(mapaPendenciaCartaoCreditoVO, mapaPendenciaCartaoCreditoTotalVOs, true, dataRecebimento);
			}
		}
		mapaPendenciaCartaoCreditoTotalVOs.forEach(total -> total.setValorPrevisto(Uteis.arrendondarForcando2CadasDecimais(total.getValorPrevisto())));
		return mapaPendenciaCartaoCreditoTotalVOs;
	}

	@Override
	public void realizarCalculoMapaPendenciaCartaCreditoTotal(MapaPendenciaCartaoCreditoVO mapaPendenciaCartaoCreditoVO, List<MapaPendenciaCartaoCreditoTotalVO> mapaPendenciaCartaoCreditoTotalVOs, boolean adicionar, Date dataRecebimento) throws Exception {
		MapaPendenciaCartaoCreditoTotalVO mapaPendenciaCartaoCreditoTotalVO = consultarMapaPendenciaCartaoCreditoTotalVO(mapaPendenciaCartaoCreditoVO.getFormaPagamentoNegociacaoRecebimentoVO().getOperadoraCartaoVO(), mapaPendenciaCartaoCreditoVO.getFormaPagamentoNegociacaoRecebimentoVO().getContaCorrenteOperadoraCartaoVO(), mapaPendenciaCartaoCreditoVO.getFormaPagamentoNegociacaoRecebimentoVO().getFormaPagamento(), mapaPendenciaCartaoCreditoTotalVOs);
		if (adicionar) {
			if (mapaPendenciaCartaoCreditoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getSituacao().equals("AR")) {
				mapaPendenciaCartaoCreditoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setDataRecebimento(dataRecebimento);
				mapaPendenciaCartaoCreditoVO.setValorTaxa(null);
				mapaPendenciaCartaoCreditoVO.setValorTaxaAntecipacao(null);
				mapaPendenciaCartaoCreditoVO.setValorTaxaUsar(null);
			}
			executarCalculoValorPagar(mapaPendenciaCartaoCreditoVO);
			mapaPendenciaCartaoCreditoTotalVO.setQuantidade(mapaPendenciaCartaoCreditoTotalVO.getQuantidade() + 1);
			mapaPendenciaCartaoCreditoTotalVO.setValor(Uteis.arrendondarForcando2CadasDecimais(mapaPendenciaCartaoCreditoTotalVO.getValor() + mapaPendenciaCartaoCreditoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getValorParcela()));
			mapaPendenciaCartaoCreditoTotalVO.setValorTaxa(Uteis.arrendondarForcando2CadasDecimais(mapaPendenciaCartaoCreditoTotalVO.getValorTaxa() + mapaPendenciaCartaoCreditoVO.getValorTaxaUsar()));
			mapaPendenciaCartaoCreditoTotalVO.getMapaPendenciaCartaoCreditoVOs().add(mapaPendenciaCartaoCreditoVO);
		} else if (mapaPendenciaCartaoCreditoTotalVO.getValorTaxa() >= mapaPendenciaCartaoCreditoVO.getValorTaxaUsar()) {
			mapaPendenciaCartaoCreditoTotalVO.setQuantidade(mapaPendenciaCartaoCreditoTotalVO.getQuantidade() - 1);
			mapaPendenciaCartaoCreditoTotalVO.setValor(Uteis.arrendondarForcando2CadasDecimais(mapaPendenciaCartaoCreditoTotalVO.getValor() - mapaPendenciaCartaoCreditoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getValorParcela()));
			mapaPendenciaCartaoCreditoTotalVO.setValorTaxa(Uteis.arrendondarForcando2CadasDecimais(mapaPendenciaCartaoCreditoTotalVO.getValorTaxa() - mapaPendenciaCartaoCreditoVO.getValorTaxaUsar()));
			mapaPendenciaCartaoCreditoTotalVO.getMapaPendenciaCartaoCreditoVOs().remove(mapaPendenciaCartaoCreditoVO);
			if (mapaPendenciaCartaoCreditoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getSituacao().equals("AR")) {
				mapaPendenciaCartaoCreditoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setDataRecebimento(null);
				mapaPendenciaCartaoCreditoVO.setValorTaxa(null);
				mapaPendenciaCartaoCreditoVO.setValorTaxaAntecipacao(null);
				mapaPendenciaCartaoCreditoVO.setValorTaxaUsar(null);
			}
			executarCalculoValorPagar(mapaPendenciaCartaoCreditoVO);
		}
	}

	public MapaPendenciaCartaoCreditoTotalVO consultarMapaPendenciaCartaoCreditoTotalVO(OperadoraCartaoVO operadoraCartaoVO, ContaCorrenteVO contaCorrenteVO, FormaPagamentoVO formaPagamentoVO, List<MapaPendenciaCartaoCreditoTotalVO> mapaPendenciaCartaoCreditoTotalVOs) {
		for (MapaPendenciaCartaoCreditoTotalVO mapaPendenciaCartaoCreditoTotalVO : mapaPendenciaCartaoCreditoTotalVOs) {
			if (mapaPendenciaCartaoCreditoTotalVO.getOperadoraCartaoVO().getCodigo().equals(operadoraCartaoVO.getCodigo()) && mapaPendenciaCartaoCreditoTotalVO.getContaCorrenteVO().getCodigo().equals(contaCorrenteVO.getCodigo())) {
				return mapaPendenciaCartaoCreditoTotalVO;
			}
		}
		MapaPendenciaCartaoCreditoTotalVO mapaPendenciaCartaoCreditoTotalVO = new MapaPendenciaCartaoCreditoTotalVO();
		mapaPendenciaCartaoCreditoTotalVO.setOperadoraCartaoVO(operadoraCartaoVO);
		mapaPendenciaCartaoCreditoTotalVO.setContaCorrenteVO(contaCorrenteVO);
		mapaPendenciaCartaoCreditoTotalVO.setFormaPagamentoVO(formaPagamentoVO);
		mapaPendenciaCartaoCreditoTotalVOs.add(mapaPendenciaCartaoCreditoTotalVO);
		return mapaPendenciaCartaoCreditoTotalVO;
	}

	@Override
	public void removerMapaPendenciaCartaoPorMapaPendenciaCartaoCreditoTotal(List<MapaPendenciaCartaoCreditoVO> mapaPendenciaCartaoCreditoVOs, MapaPendenciaCartaoCreditoTotalVO mapaPendenciaCartaoCreditoTotalVO, List<MapaPendenciaCartaoCreditoTotalVO> mapaPendenciaCartaoCreditoTotalVOs) throws Exception {
		for (MapaPendenciaCartaoCreditoVO mapaPendenciaCartaoCreditoVO : mapaPendenciaCartaoCreditoVOs) {
			if (mapaPendenciaCartaoCreditoVO.getEfetuarBaixa() && mapaPendenciaCartaoCreditoVO.getFormaPagamentoNegociacaoRecebimentoVO().getOperadoraCartaoVO().getCodigo().equals(mapaPendenciaCartaoCreditoTotalVO.getOperadoraCartaoVO().getCodigo()) && mapaPendenciaCartaoCreditoVO.getFormaPagamentoNegociacaoRecebimentoVO().getContaCorrenteOperadoraCartaoVO().getCodigo().equals(mapaPendenciaCartaoCreditoTotalVO.getContaCorrenteVO().getCodigo())) {
				mapaPendenciaCartaoCreditoVO.setEfetuarBaixa(false);
				if (mapaPendenciaCartaoCreditoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getSituacao().equals("AR")) {
					mapaPendenciaCartaoCreditoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setDataRecebimento(null);
				}
				mapaPendenciaCartaoCreditoTotalVO.getMapaPendenciaCartaoCreditoVOs().remove(mapaPendenciaCartaoCreditoVO);
			}
		}
		mapaPendenciaCartaoCreditoTotalVO.setQuantidade(0);
		mapaPendenciaCartaoCreditoTotalVO.setValor(0.0);
		mapaPendenciaCartaoCreditoTotalVO.setValorTaxa(0.0);
	}

	@Override
	public void selecionarMapaPendenciaCartaoPorMapaPendenciaCartaoCreditoTotal(List<MapaPendenciaCartaoCreditoVO> mapaPendenciaCartaoCreditoVOs, MapaPendenciaCartaoCreditoTotalVO mapaPendenciaCartaoCreditoTotalVO, List<MapaPendenciaCartaoCreditoTotalVO> mapaPendenciaCartaoCreditoTotalVOs, Date dataRecebimento) throws Exception {
		for (MapaPendenciaCartaoCreditoVO mapaPendenciaCartaoCreditoVO : mapaPendenciaCartaoCreditoVOs) {
			if (!mapaPendenciaCartaoCreditoVO.getEfetuarBaixa() && mapaPendenciaCartaoCreditoVO.getFormaPagamentoNegociacaoRecebimentoVO().getOperadoraCartaoVO().getCodigo().equals(mapaPendenciaCartaoCreditoTotalVO.getOperadoraCartaoVO().getCodigo()) && mapaPendenciaCartaoCreditoVO.getFormaPagamentoNegociacaoRecebimentoVO().getContaCorrenteOperadoraCartaoVO().getCodigo().equals(mapaPendenciaCartaoCreditoTotalVO.getContaCorrenteVO().getCodigo())) {
				mapaPendenciaCartaoCreditoVO.setEfetuarBaixa(true);
				realizarCalculoMapaPendenciaCartaCreditoTotal(mapaPendenciaCartaoCreditoVO, mapaPendenciaCartaoCreditoTotalVOs, true, dataRecebimento);
			}
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarAtualizacaoDataPrevisao(List<MapaPendenciaCartaoCreditoVO> mapaPendenciaCartaoCreditoVOs, Date dataPrevisao, UsuarioVO usuarioVO) throws Exception {
		if (Uteis.isAtributoPreenchido(dataPrevisao)) {
			for (MapaPendenciaCartaoCreditoVO mapaPendenciaCartaoCreditoVO : mapaPendenciaCartaoCreditoVOs) {
				if (mapaPendenciaCartaoCreditoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getSituacao().equals("AR") && mapaPendenciaCartaoCreditoVO.getEfetuarBaixa()) {
					mapaPendenciaCartaoCreditoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setDataVencimento(dataPrevisao);
					getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoCartaoCreditoFacade().alterarDataVencimento(mapaPendenciaCartaoCreditoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO(), usuarioVO);
				}
			}
		} else {
			throw new Exception(UteisJSF.internacionalizar("msg_MapaPendenciaCartaoCredito_dataPrevisao"));
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarAlteracaoTaxa(List<MapaPendenciaCartaoCreditoVO> mapaPendenciaCartaoCreditoVOs, Double taxa, boolean taxaAntecipacao, UsuarioVO usuarioVO) throws Exception {
		if (Uteis.isAtributoPreenchido(taxa)) {
			for (MapaPendenciaCartaoCreditoVO mapaPendenciaCartaoCreditoVO : mapaPendenciaCartaoCreditoVOs) {
				if (mapaPendenciaCartaoCreditoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getSituacao().equals("AR")
						&& mapaPendenciaCartaoCreditoVO.getEfetuarBaixa()) {
					if (taxaAntecipacao) {
						mapaPendenciaCartaoCreditoVO.getFormaPagamentoNegociacaoRecebimentoVO().setTaxaDeAntecipacao(taxa);
						getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoFacade().alterarTaxaAntecipacaoCartaoCredito(mapaPendenciaCartaoCreditoVO.getFormaPagamentoNegociacaoRecebimentoVO(), usuarioVO);
					} else {
						mapaPendenciaCartaoCreditoVO.getFormaPagamentoNegociacaoRecebimentoVO().setTaxaDeOperacao(taxa);
						getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoFacade().alterarTaxaCartaoCredito(mapaPendenciaCartaoCreditoVO.getFormaPagamentoNegociacaoRecebimentoVO(), usuarioVO);
					}
				}
			}
		} else {
			throw new Exception(UteisJSF.internacionalizar("msg_MapaPendenciaCartaoCredito_taxa"));
		}
	}

}
