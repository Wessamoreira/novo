package negocio.facade.jdbc.financeiro;

import java.util.Iterator;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.OperacaoFuncionalidadeEnum;
import negocio.comuns.arquitetura.enumeradores.OrigemOperacaoFuncionalidadeEnum;
import negocio.comuns.financeiro.DevolucaoChequeVO;
import negocio.comuns.financeiro.FluxoCaixaVO;
import negocio.comuns.financeiro.MapaPendenciaCartaoCreditoVO;
import negocio.comuns.financeiro.MovimentacaoFinanceiraItemVO;
import negocio.comuns.financeiro.MovimentacaoFinanceiraVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoFormaPagamento;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.MapaPendenciaMovimentacaoFinanceiraInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>MapaPendenciaCartaoCreditoVO</code>. Responsável por
 * implementar operações como incluir, alterar, excluir e consultas pertinentes
 * a classe <code>MapaPendenciaCartaoCreditoVO</code>. Encapsula toda a
 * interação com o banco de dados.
 * 
 * @see MapaPendenciaCartaoCreditoVO
 * @see ControleAcesso
 */
@Repository
public class MapaPendenciaMovimentacaoFinanceira extends ControleAcesso implements MapaPendenciaMovimentacaoFinanceiraInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public MapaPendenciaMovimentacaoFinanceira() {
		super();
		setIdEntidade("MapaPendenciaMovimentacaoFinanceira");
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void recusarMovimentacaoFinanceira(MovimentacaoFinanceiraVO obj, UsuarioVO usuario) throws Exception {
		MapaPendenciaMovimentacaoFinanceira.alterar(getIdEntidade(), true, usuario);
		validarMotivoRecusa(obj);
//		for (MovimentacaoFinanceiraItemVO movimentacaoFinanceiraItemVO : obj.getMovimentacaoFinanceiraItemVOs()) {
//			if (movimentacaoFinanceiraItemVO.getFormaPagamento().getTipo().equals(TipoFormaPagamento.CHEQUE.getValor()) && movimentacaoFinanceiraItemVO.getCheque().getCodigo().intValue() != 0) {
//				getFacadeFactory().getMovimentacaoFinanceiraItemFacade().alterarSituacaoCheque(movimentacaoFinanceiraItemVO.getCheque(), obj.getContaCorrenteOrigem().getCodigo(), obj.getContaCorrenteOrigem().getContaCaixa(), usuario);
//			}
//		}
		getFacadeFactory().getMovimentacaoFinanceiraFacade().alterarSituacaoMotivoRecusa(obj.getCodigo(), obj.getSituacao(), obj.getMotivoRecusa(), usuario);

	}

	private void validarMotivoRecusa(MovimentacaoFinanceiraVO obj) throws Exception {
		if (!Uteis.isAtributoPreenchido(obj.getMotivoRecusa())) {
			throw new ConsistirException("É necessário informar o motivo da recusa.");
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void finalizarMovimentacaoFinanceira(MovimentacaoFinanceiraVO obj, UsuarioVO usuario) throws Exception {
		MapaPendenciaMovimentacaoFinanceira.alterar(getIdEntidade(), true, usuario);
		obj.setMovimentacaoFinanceiraItemVOs(getFacadeFactory().getMovimentacaoFinanceiraItemFacade().consultarPorMovimentacaoFinanceiraMapaPendenciaMovimentacaoFinanceira(obj.getCodigo(), usuario));
		for (Iterator<MovimentacaoFinanceiraItemVO> iterator = obj.getMovimentacaoFinanceiraItemVOs().iterator(); iterator.hasNext();) {
			MovimentacaoFinanceiraItemVO movimentacaoFinanceiraItemVO = iterator.next();
			obj.setContaCorrenteDestino(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(obj.getContaCorrenteDestino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, usuario));
			if (obj.getContaCorrenteOrigem().getCodigo().equals(0)) {
				obj.setSomenteContaDestino(true);
			} else {
				obj.setSomenteContaDestino(false);
				obj.setContaCorrenteOrigem(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(obj.getContaCorrenteOrigem().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, usuario));
			}
			getFacadeFactory().getMovimentacaoFinanceiraItemFacade().criarMovimentacaoCaixaAlterandoSaldoConta(obj, movimentacaoFinanceiraItemVO, false, usuario);
		}
		getFacadeFactory().getMovimentacaoFinanceiraFacade().alterarSituacao(obj, "FI", usuario);
		if(!obj.isDesconsiderandoContabilidadeConciliacao()) {
			getFacadeFactory().getLancamentoContabilFacade().gerarLancamentoContabilPorMovimentacaoFinanceira(obj, false, usuario);
		}
	}

	public static String getIdEntidade() {
		return MapaPendenciaMovimentacaoFinanceira.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		MapaPendenciaMovimentacaoFinanceira.idEntidade = idEntidade;
	}
	
	@Override
	public boolean executarVerificacaoExisteDevolucaoChequeComContaReceberNegociadaOuRecebida(MovimentacaoFinanceiraVO movimentacaoFinanceiraVO, UsuarioVO usuarioVO) throws Exception {
		boolean existeDevolucaoChequeComContaReceberNegociadaOuRecebida = false;
		movimentacaoFinanceiraVO.setMovimentacaoFinanceiraItemVOs(getFacadeFactory().getMovimentacaoFinanceiraItemFacade().consultarPorMovimentacaoFinanceiraMapaPendenciaMovimentacaoFinanceira(movimentacaoFinanceiraVO.getCodigo(), usuarioVO));
		for (Iterator<MovimentacaoFinanceiraItemVO> iterator = movimentacaoFinanceiraVO.getMovimentacaoFinanceiraItemVOs().iterator(); iterator.hasNext();) {
			MovimentacaoFinanceiraItemVO movimentacaoFinanceiraItemVO = iterator.next();
			if (movimentacaoFinanceiraItemVO.getFormaPagamento().getTipo().equals(TipoFormaPagamento.CHEQUE.getValor()) 
					&& Uteis.isAtributoPreenchido(movimentacaoFinanceiraItemVO.getCheque()) 
					&& !movimentacaoFinanceiraVO.getContaCorrenteDestino().getContaCaixa()) {
				DevolucaoChequeVO devolucaoChequeVO = getFacadeFactory().getDevolucaoChequeFacade().consultarPorCodigoCheque(movimentacaoFinanceiraItemVO.getCheque().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO);
				if (getFacadeFactory().getContaReceberFacade().executarVerificacaoContaReceberRecebidaOuNegociada(devolucaoChequeVO.getCodigo(), "DCH", usuarioVO)) {
					existeDevolucaoChequeComContaReceberNegociadaOuRecebida = true;
					movimentacaoFinanceiraItemVO.setContaReceberRecebidaOuNegociada(true);
				}
			}
		}
		return existeDevolucaoChequeComContaReceberNegociadaOuRecebida;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirMovimentacaoFinanceiraItemDevolucaoChequeComContaReceberNegociadaOuRecebida(MovimentacaoFinanceiraVO movimentacaoFinanceiraVO, UsuarioVO usuarioVO) throws Exception {
		for (Iterator<MovimentacaoFinanceiraItemVO> iterator = movimentacaoFinanceiraVO.getMovimentacaoFinanceiraItemVOs().iterator(); iterator.hasNext();) {
			MovimentacaoFinanceiraItemVO movimentacaoFinanceiraItemVO = iterator.next();
			if (movimentacaoFinanceiraItemVO.isContaReceberRecebidaOuNegociada()) {
				getFacadeFactory().getMovimentacaoFinanceiraItemFacade().excluir(movimentacaoFinanceiraItemVO, usuarioVO);
				getFacadeFactory().getOperacaoFuncionalidadeFacade().incluir(getFacadeFactory().getOperacaoFuncionalidadeFacade().executarGeracaoOperacaoFuncionalidade(OrigemOperacaoFuncionalidadeEnum.MAPA_PENDENCIA_MOVIMENTACAO_FINACEIRA, movimentacaoFinanceiraItemVO.getCodigo().toString(), OperacaoFuncionalidadeEnum.EXCLUIR_MOVIMENTACAO_FINANCEIRO_ITEM, usuarioVO, executarGeracaoObservacaoOperacaoFuncionalidade(movimentacaoFinanceiraItemVO)));
			}
		}
		finalizarMovimentacaoFinanceira(movimentacaoFinanceiraVO, usuarioVO);
	}
	
	private String executarGeracaoObservacaoOperacaoFuncionalidade(MovimentacaoFinanceiraItemVO movimentacaoFinanceiraItemVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder("Realizada a exclusão da seguinte devolução de cheque cujo a conta a receber está recebida ou negociada: ");
		sqlStr.append("Código cheque: ").append(movimentacaoFinanceiraItemVO.getCheque().getCodigo()).append(", Sacado: ").append(movimentacaoFinanceiraItemVO.getCheque().getSacado()).append(", Valor: ").append(movimentacaoFinanceiraItemVO.getCheque().getValor()).append(".");
		return sqlStr.toString();
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void autorizarMovimentacaoFinanceiraContaCaixaFechado(MovimentacaoFinanceiraVO obj, UsuarioVO usuario)
			throws Exception {

		FluxoCaixaVO fluxoCaixaContaOrigemAbertoHoje = getFacadeFactory().getMovimentacaoFinanceiraFacade()
				.atualizarFluxoCaixa(obj.getContaCorrenteOrigem(), usuario);

		FluxoCaixaVO fluxoCaixaContaDestinoAbertoHoje = getFacadeFactory().getMovimentacaoFinanceiraFacade()
				.atualizarFluxoCaixa(obj.getContaCorrenteDestino(), usuario);

		finalizarMovimentacaoFinanceira(obj, usuario);

		if (Uteis.isAtributoPreenchido(fluxoCaixaContaOrigemAbertoHoje)) {
			getFacadeFactory().getFluxoCaixaFacade().fecharCaixa(fluxoCaixaContaOrigemAbertoHoje, usuario);
		}

		if (Uteis.isAtributoPreenchido(fluxoCaixaContaDestinoAbertoHoje)) {
			getFacadeFactory().getFluxoCaixaFacade().fecharCaixa(fluxoCaixaContaDestinoAbertoHoje, usuario);
		}
	}
	
}
