package negocio.interfaces.financeiro;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ProcessamentoArquivoRetornoParceiroAlunoVO;
import negocio.comuns.financeiro.ProcessamentoArquivoRetornoParceiroVO;
import negocio.comuns.utilitarias.dominios.SituacaoContaReceber;

public interface ProcessamentoArquivoRetornoParceiroAlunoInterfaceFacade {

	List<ProcessamentoArquivoRetornoParceiroAlunoVO> consultarContaReceberBolsaCusteadaConvenioPorParceiroPorMesCompetencia(ProcessamentoArquivoRetornoParceiroVO processamento, UsuarioVO usuarioVO) throws Exception;

	void persistir(List<ProcessamentoArquivoRetornoParceiroAlunoVO> lista, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	List<ProcessamentoArquivoRetornoParceiroAlunoVO> consultaRapidaPorProcessamentoArquivoRetornoParceiro(ProcessamentoArquivoRetornoParceiroVO obj, UsuarioVO usuario) throws Exception;

	void atualizarProcessamentoArquivoRetornoParceiroAlunoPorContaReceber(Integer matriculaPeriodo, SituacaoContaReceber situacaoContaReceber, UsuarioVO usuarioVO) throws Exception;

	void atualizarProcessamentoArquivoRetornoParceiroAlunoPorContaReceberEspecifica(Integer contaReceber,
			UsuarioVO usuarioVO) throws Exception;

}
