package negocio.interfaces.financeiro;

import java.math.BigDecimal;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.IndiceReajustePeriodoMatriculaPeriodoVencimentoVO;
import negocio.comuns.utilitarias.SituacaoExecucaoEnum;

public interface IndiceReajustePeriodoMatriculaPeriodoVencimentoInterfaceFacade {

	void incluir(IndiceReajustePeriodoMatriculaPeriodoVencimentoVO obj, UsuarioVO usuario) throws Exception;

	List<IndiceReajustePeriodoMatriculaPeriodoVencimentoVO> consultarPorIndiceReajustePeriodoSituacao(Integer indiceReajustePeriodo, SituacaoExecucaoEnum situacaoExecucao, UsuarioVO usuarioVO) throws Exception;

	IndiceReajustePeriodoMatriculaPeriodoVencimentoVO consultarUltimoIndicePorMatriculaPeriodoSituacaoParcela(Integer indiceReajustePeriodo, Integer matriculaPeriodo, SituacaoExecucaoEnum situacaoExecucao, String parcela, String tipoOrigem, UsuarioVO usuarioVO) throws Exception;

	BigDecimal consultarValorIndiceReajusteASerAplicadoContaReceberPorMatriculaPeriodoSituacao(Integer matriculaPeriodo, SituacaoExecucaoEnum situacaoExecucao, String parcela, UsuarioVO usuarioVO);

	List<IndiceReajustePeriodoMatriculaPeriodoVencimentoVO> consultarPorMatriculaPeriodoSituacaoParcela(Integer matriculaPeriodo, SituacaoExecucaoEnum situacaoExecucao, String parcela, String tipoOrigem,  UsuarioVO usuarioVO) throws Exception;

	BigDecimal consultarValorReajusteDiferencaParcelaRecebidaOuEnviadaRemessaASerAplicadoContaReceberPorMatriculaPeriodoSituacaoParcela(Integer matriculaPeriodo, SituacaoExecucaoEnum situacaoExecucao, String parcela, UsuarioVO usuarioVO);

	void alterarIndiceReajustePeriodoMatriculaPeriodoVencimentoSituacaoCanceladoPorIndiceReajustePeriodo(Integer indiceReajustePeriodo, SituacaoExecucaoEnum situacaoExecucao, String motivoCancelamento, UsuarioVO usuario) throws Exception;

	void alterarIndiceReajustePeriodoMatriculaPeriodoVencimentoSituacaoCancelado(Integer codigo, SituacaoExecucaoEnum situacaoExecucao, String motivoCancelamento, UsuarioVO usuario) throws Exception;

	IndiceReajustePeriodoMatriculaPeriodoVencimentoVO inicializarDadosIndiceReajustePeriodoMatriculaPeriodoVencimento(Integer indiceReajustePeriodo, SituacaoExecucaoEnum situacao, String tipoOrigemContaReceber, ContaReceberVO contaReceberVO, BigDecimal valorIndiceReajuste, BigDecimal valorReajusteDiferencaParcelaRecebida, String observacaoDiferencaParcelaRecebidaOuEnviadaRemessa, UsuarioVO usuarioVO) throws Exception;

	void cancelarIndiceReajustePeriodoMatriculaPeriodoVencimento(List<IndiceReajustePeriodoMatriculaPeriodoVencimentoVO> listaIndiceReajusteMatriculaPeriodoVencimentoVOs, String motivoCancelamento, UsuarioVO usuarioVO) throws Exception;

	IndiceReajustePeriodoMatriculaPeriodoVencimentoVO consultarPenultimoIndicePorMatriculaPeriodoSituacaoParcela(Integer indiceReajustePeriodo, Integer matriculaPeriodo, SituacaoExecucaoEnum situacaoExecucao, String parcela,String tipoOrigem, UsuarioVO usuarioVO) throws Exception;

}
