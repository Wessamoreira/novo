package negocio.interfaces.contabil;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.contabil.LancamentoContabilCentroNegocioVO;
import negocio.comuns.contabil.LancamentoContabilVO;

public interface LancamentoContabilCentroNegocioInterfaceFacade {

	void persistir(List<LancamentoContabilCentroNegocioVO> lista, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void persistir(LancamentoContabilCentroNegocioVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;	

	List<LancamentoContabilCentroNegocioVO> consultaRapidaPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	void consultaRapidaPorLancamentoContabilVO(LancamentoContabilVO obj, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

}
