package negocio.interfaces.financeiro;

import java.util.List;

import negocio.comuns.academico.enumeradores.MesAnoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.IndiceReajustePeriodoVO;
import negocio.comuns.financeiro.IndiceReajusteVO;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.SituacaoExecucaoEnum;

public interface IndiceReajustePeriodoInterfaceFacade {

	void incluirIndiceReajustePeriodoVOs(Integer indiceReajuste, List<IndiceReajustePeriodoVO> listaIndiceReajustePeriodoVOs, UsuarioVO usuarioVO) throws Exception;

	void alterarIndiceReajustePeriodoVOs(Integer indiceReajuste, List<IndiceReajustePeriodoVO> listaIndiceReajustePeriodoVOs, UsuarioVO usuarioVO) throws Exception;

	List<IndiceReajustePeriodoVO> consultarIndiceReajustePeriodoPorIndiceReajuste(Integer indiceReajuste, UsuarioVO usuarioVO);

	void  executarReajustePrecoContaReceber(ProgressBarVO progressBarVO, IndiceReajusteVO indiceReajusteVO, IndiceReajustePeriodoVO indiceReajustePeriodoVO, UsuarioVO usuarioVO) throws Exception;

	void executarCancelamentoReajustePrecoContaReceber(ProgressBarVO progressBarVO, IndiceReajusteVO indiceReajusteVO, IndiceReajustePeriodoVO indiceReajustePeriodoVO, UsuarioVO usuarioVO) throws Exception;

	void excluirIndiceReajustePeriodoPorIndiceReajuste(IndiceReajusteVO obj, UsuarioVO usuario) throws Exception;

	Integer consultaNumeroMaximoParcelaCondicaoPagamentoPlanoFinanceiroCurso(Integer mes, String ano, UsuarioVO usuarioVO) throws Exception;

//	void persistirIndiceReajustePeriodoMatriculaPeriodoVencimento(Integer indiceReajustePeriodo, ContaReceberVO contaReceberVO, BigDecimal valorIndiceReajuste, BigDecimal valorReajusteDiferencaParcelaRecebida, String observacaoDiferencaParcelaRecebidaOuEnviadaRemessa, UsuarioVO usuarioVO) throws Exception;

	void alterarSituacaoIndiceReajustePeriodo(Integer codigo, SituacaoExecucaoEnum situacaoExecucao, String motivoCancelamento, Boolean processamentoAutomatico, UsuarioVO usuario) throws Exception;

	Boolean consultarExistenciaIndiceReajustePeriodoProcessadoMaisDeUmaVezParaContaReceber(IndiceReajustePeriodoVO indiceReajustePeriodoVO, UsuarioVO usuarioVO);

	IndiceReajustePeriodoVO consultarPorChavePrimaria(Integer indiceReajustePeriodo, UsuarioVO usuarioVO);

	void incluir(IndiceReajustePeriodoVO obj, UsuarioVO usuario) throws Exception;

	List<IndiceReajustePeriodoVO> consultarIndiceReajustePeriodoAguardandoProcessamento(UsuarioVO usuarioVO);

	List<IndiceReajustePeriodoVO> consultarIndiceReajustePeriodoPorMatriculaPeriodoSituacao(Integer matriculaPeriodo, String tipoOrigem, SituacaoExecucaoEnum situacaoExecucao, String parcela, UsuarioVO usuarioVO);

	IndiceReajustePeriodoVO consultarPorCodigoIndiceReajustePorMesPorAno(Integer indiceReajustePeriodo, MesAnoEnum mesAnoEnum, String ano, UsuarioVO usuarioVO);

	List<IndiceReajustePeriodoVO> consultarIndiceReajustePeriodoProcessando(UsuarioVO usuarioVO);

	ProgressBarVO consultarProgressBarEmExecucao(IndiceReajusteVO obj);

}
