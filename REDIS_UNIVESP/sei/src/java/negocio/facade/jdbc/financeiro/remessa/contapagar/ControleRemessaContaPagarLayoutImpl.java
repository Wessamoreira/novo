package negocio.facade.jdbc.financeiro.remessa.contapagar;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaPagarNaoLocalizadaArquivoRetornoVO;
import negocio.comuns.financeiro.ContaPagarNegociacaoPagamentoVO;
import negocio.comuns.financeiro.ContaPagarRegistroArquivoVO;
import negocio.comuns.financeiro.ContaPagarVO;
import negocio.comuns.financeiro.ControleCobrancaPagarVO;
import negocio.comuns.financeiro.FormaPagamentoNegociacaoPagamentoVO;
import negocio.comuns.financeiro.MapaPendenciasControleCobrancaPagarVO;
import negocio.comuns.financeiro.NegociacaoPagamentoVO;
import negocio.comuns.financeiro.enumerador.SituacaoControleRemessaContaReceberEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoSacado;
import negocio.facade.jdbc.arquitetura.SuperFacadeJDBC;

public abstract class ControleRemessaContaPagarLayoutImpl extends SuperFacadeJDBC {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1171396444891913347L;

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void criarNegociacaoPagamentoBaixandoContasPagas(ControleCobrancaPagarVO controleCobrancaVO, List<ContaPagarRegistroArquivoVO> listaContaPagarRegistroDetalhe, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
		HashMap<Integer, ContaPagarVO> map = new HashMap<Integer, ContaPagarVO>(0);
		for (ContaPagarRegistroArquivoVO detalhe : listaContaPagarRegistroDetalhe) {
			if (detalhe.getContaPagarVO().getObservacao().equals("Conta Não Localizada!")) {
				gerarContaPagarNaoLocalizada(controleCobrancaVO, detalhe, usuarioVO);
			} else if (!map.containsKey(detalhe.getContaPagarVO().getCodigo())) {
				if (detalhe.isContaPagarEfetivado() && !detalhe.getValorPagamentoDivergente() && !detalhe.getContaPagarVO().getQuitada() && (getFacadeFactory().getContaPagarFacade().verificarExistenciaContaPagarRecebidaEmDuplicidade(detalhe.getContaPagarVO().getCodigo()))) {
					// insere no historico da contaPgar a tenta de baixa do arquivo novamente, seja pela duplidade do arquivo ou
					// seja pelo fato de ter renomeado o arquivo de retorno.
					detalhe.getContaPagarVO().setObservacao("Conta Pgto Duplicidade!");
					getFacadeFactory().getContaPagarHistoricoFacade().criarContaPagarHistoricoPorBaixaAutomaticas(detalhe.getContaPagarVO(), controleCobrancaVO, usuarioVO);
				}else if (detalhe.isContaPagarEfetivado() && !detalhe.getValorPagamentoDivergente() && !detalhe.getContaPagarVO().getQuitada()){
					preencherContaPagarComArquivoRetorno(controleCobrancaVO, detalhe, usuarioVO);
					gerarNegociacaoContaPagar(controleCobrancaVO, detalhe, usuarioVO);
				}
				map.put(detalhe.getContaPagarVO().getCodigo(), detalhe.getContaPagarVO());
			}
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private void gerarContaPagarNaoLocalizada(ControleCobrancaPagarVO controleCobrancaVO, ContaPagarRegistroArquivoVO detalhe, UsuarioVO usuarioVO) throws Exception {
		ContaPagarNaoLocalizadaArquivoRetornoVO obj = (ContaPagarNaoLocalizadaArquivoRetornoVO) getFacadeFactory().getContaPagarNaoLocalizadaArquivoRetornoFacade().consultarPorNossoNumeroUnico(detalhe.getRegistroDetalhePagarVO().getNossoNumero(), detalhe.getRegistroDetalhePagarVO().getDataPagamento(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO);
		if (obj.getCodigo() != 0) {
			getFacadeFactory().getContaPagarNaoLocalizadaArquivoRetornoFacade().alterar(obj, usuarioVO);
		} else {
			obj.setNossoNumero(detalhe.getRegistroDetalhePagarVO().getNossoNumero());
			obj.setDataVcto(detalhe.getRegistroDetalhePagarVO().getDataVencimento());
			obj.setDataPagamento(detalhe.getRegistroDetalhePagarVO().getDataPagamento());
			obj.setValorPago(detalhe.getRegistroDetalhePagarVO().getValorPagamento());
			getFacadeFactory().getContaPagarNaoLocalizadaArquivoRetornoFacade().incluir(obj, usuarioVO);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private void preencherContaPagarComArquivoRetorno(ControleCobrancaPagarVO controleCobrancaVO, ContaPagarRegistroArquivoVO detalhe, UsuarioVO usuarioVO) throws Exception {
		// Acrescentado regra para somente gerar pendência somente se o valor for maior que 0.01
		// Devido a arredondamento utilizado no método espírita. Autorizado pelo Edigar no dia 16/05/2017
		if (detalhe.getRegistroDetalhePagarVO().getValorPagamento() < detalhe.getContaPagarVO().getValor() && (detalhe.getContaPagarVO().getValor() - detalhe.getRegistroDetalhePagarVO().getValorPagamento()) > 0.01) {
			MapaPendenciasControleCobrancaPagarVO mapaPendenciasControleCobrancaVO = new MapaPendenciasControleCobrancaPagarVO();
			if (Uteis.isAtributoPreenchido(detalhe.getContaPagarVO().getMatricula())) {
				mapaPendenciasControleCobrancaVO.getMatriculaVO().setMatricula(detalhe.getContaPagarVO().getMatricula());
			}
			mapaPendenciasControleCobrancaVO.setContaPagarVO(detalhe.getContaPagarVO());
			mapaPendenciasControleCobrancaVO.setControleCobrancaPagarVO(controleCobrancaVO);
			mapaPendenciasControleCobrancaVO.setValorDiferenca(detalhe.getContaPagarVO().getValor() - detalhe.getRegistroDetalhePagarVO().getValorDesconto());
			mapaPendenciasControleCobrancaVO.setDataPagamento(detalhe.getRegistroDetalhePagarVO().getDataPagamento());
			mapaPendenciasControleCobrancaVO.setJuro(0.0);
			mapaPendenciasControleCobrancaVO.setMulta(0.0);
			getFacadeFactory().getMapaPendenciasControleCobrancaPagarFacade().incluir(mapaPendenciasControleCobrancaVO, usuarioVO);
			detalhe.getContaPagarVO().setDesconto(detalhe.getContaPagarVO().getValor() - detalhe.getRegistroDetalhePagarVO().getValorPagamento());
		} else if (detalhe.getRegistroDetalhePagarVO().getValorPagamento() > detalhe.getContaPagarVO().getValor()) {
			detalhe.getContaPagarVO().setJuro(detalhe.getRegistroDetalhePagarVO().getValorPagamento() - detalhe.getContaPagarVO().getValor());
		}
		detalhe.getContaPagarVO().setValorPago(detalhe.getRegistroDetalhePagarVO().getValorPagamento());
		getFacadeFactory().getContaPagarControleRemessaContaPagarFacade().atualizarSituacaoContaPagarControleRemessaContaPagarPorContaPagar(detalhe.getContaPagarVO().getCodigo(), SituacaoControleRemessaContaReceberEnum.REMETIDA, usuarioVO);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private void gerarNegociacaoContaPagar(ControleCobrancaPagarVO controleCobrancaVO, ContaPagarRegistroArquivoVO detalhe, UsuarioVO usuarioVO) throws Exception {
		NegociacaoPagamentoVO negociacaoPagamentoVO = new NegociacaoPagamentoVO();
		negociacaoPagamentoVO.setValorTotal(detalhe.getRegistroDetalhePagarVO().getValorPagamento());
		if (detalhe.getContaPagarVO().isTipoSacadoFuncionario()) {
			negociacaoPagamentoVO.setTipoSacado(TipoSacado.FUNCIONARIO_PROFESSOR.getValor());
			negociacaoPagamentoVO.setFuncionario(detalhe.getContaPagarVO().getFuncionario());
		} else if (detalhe.getContaPagarVO().isTipoSacadoParceiro()) {
			negociacaoPagamentoVO.setTipoSacado(TipoSacado.PARCEIRO.getValor());
			negociacaoPagamentoVO.setParceiro(detalhe.getContaPagarVO().getParceiro());
		} else if (detalhe.getContaPagarVO().isTipoSacadoFornecedor()) {
			negociacaoPagamentoVO.setTipoSacado(TipoSacado.FORNECEDOR.getValor());
			negociacaoPagamentoVO.setFornecedor(detalhe.getContaPagarVO().getFornecedor());
		} else if (detalhe.getContaPagarVO().isTipoSacadoBanco()) {
			negociacaoPagamentoVO.setTipoSacado(TipoSacado.BANCO.getValor());
			negociacaoPagamentoVO.setBanco(detalhe.getContaPagarVO().getBanco());
		} else if(detalhe.getContaPagarVO().isTipoSacadoAluno()) {
			negociacaoPagamentoVO.setTipoSacado(TipoSacado.ALUNO.getValor());
			negociacaoPagamentoVO.setAluno(detalhe.getContaPagarVO().getPessoa());
		} else if(detalhe.getContaPagarVO().isTipoSacadoResponsavelFinanceiro()) {
			negociacaoPagamentoVO.setTipoSacado(TipoSacado.RESPONSAVEL_FINANCEIRO.getValor());
			negociacaoPagamentoVO.setResponsavelFinanceiro(detalhe.getContaPagarVO().getResponsavelFinanceiro());
		} else if(detalhe.getContaPagarVO().isTipoSacadoOperadoraCartao()) {
			negociacaoPagamentoVO.setTipoSacado(TipoSacado.OPERADORA_CARTAO.getValor());
			negociacaoPagamentoVO.setOperadoraCartao(detalhe.getContaPagarVO().getOperadoraCartao());
		}

		ContaPagarNegociacaoPagamentoVO contaPagarNegociacaoPagamento = new ContaPagarNegociacaoPagamentoVO();
		contaPagarNegociacaoPagamento.setContaPagar(detalhe.getContaPagarVO());
		contaPagarNegociacaoPagamento.setNegociacaoContaPagar(negociacaoPagamentoVO.getCodigo());
		negociacaoPagamentoVO.getContaPagarNegociacaoPagamentoVOs().add(contaPagarNegociacaoPagamento);
		negociacaoPagamentoVO.calcularTotal();

		FormaPagamentoNegociacaoPagamentoVO formaPagamentoNegociacaoPagamentoVO = new FormaPagamentoNegociacaoPagamentoVO();
		formaPagamentoNegociacaoPagamentoVO.setFormaPagamento(detalhe.getContaPagarVO().getFormaPagamentoVO());
		formaPagamentoNegociacaoPagamentoVO.setContaCorrente(controleCobrancaVO.getContaCorrenteVO());
		formaPagamentoNegociacaoPagamentoVO.setValor(negociacaoPagamentoVO.getValorTotal());
		formaPagamentoNegociacaoPagamentoVO.setNegociacaoContaPagar(negociacaoPagamentoVO.getCodigo());
		negociacaoPagamentoVO.getFormaPagamentoNegociacaoPagamentoVOs().add(formaPagamentoNegociacaoPagamentoVO);
		negociacaoPagamentoVO.calcularTotalPago();

		negociacaoPagamentoVO.setData(detalhe.getRegistroDetalhePagarVO().getDataPagamento());
		negociacaoPagamentoVO.setDataRegistro(new Date());
		negociacaoPagamentoVO.setUnidadeEnsino(detalhe.getContaPagarVO().getUnidadeEnsino());
		negociacaoPagamentoVO.setResponsavel(usuarioVO);
		getFacadeFactory().getNegociacaoPagamentoFacade().incluir(negociacaoPagamentoVO, true, usuarioVO);
	}

}
