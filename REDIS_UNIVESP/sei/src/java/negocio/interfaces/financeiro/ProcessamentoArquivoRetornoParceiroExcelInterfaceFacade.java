package negocio.interfaces.financeiro;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ProcessamentoArquivoRetornoParceiroExcelVO;
import negocio.comuns.utilitarias.dominios.SituacaoContaReceber;

public interface ProcessamentoArquivoRetornoParceiroExcelInterfaceFacade {	

	void persistir(List<ProcessamentoArquivoRetornoParceiroExcelVO> lista, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void removerVinculoContaReceber(Integer matriculaPeriodo, SituacaoContaReceber situacaoContaReceber, UsuarioVO usuarioVO) throws Exception;

	void removerVinculoContaReceberEspecifica(Integer contaReceber, UsuarioVO usuarioVO) throws Exception;

}
