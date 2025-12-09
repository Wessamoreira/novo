package negocio.interfaces.financeiro;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.CategoriaDespesaRateioVO;
import negocio.comuns.financeiro.CategoriaDespesaVO;

public interface CategoriaDespesaRateioInterfaceFacade {

	void persistir(List<CategoriaDespesaRateioVO> lista, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void persistir(CategoriaDespesaRateioVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void excluir(CategoriaDespesaRateioVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception;

	List<CategoriaDespesaRateioVO> consultaRapidaPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	void consultaRapidaPorCategoriaDespesaVO(CategoriaDespesaVO obj, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

}
