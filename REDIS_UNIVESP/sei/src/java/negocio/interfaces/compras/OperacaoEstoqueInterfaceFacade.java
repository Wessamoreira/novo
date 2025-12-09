package negocio.interfaces.compras;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.OperacaoEstoqueVO;
import negocio.comuns.compras.enumeradores.TipoOperacaoEstoqueOrigemEnum;

public interface OperacaoEstoqueInterfaceFacade {

	void persistir(OperacaoEstoqueVO obj, boolean verificarAcesso, UsuarioVO usuarioVO);

	void persistir(List<OperacaoEstoqueVO> lista, boolean verificarAcesso, UsuarioVO usuarioVO);

	void excluir(OperacaoEstoqueVO obj, boolean verificarAcesso, UsuarioVO usuario);

	OperacaoEstoqueVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario);

	List<OperacaoEstoqueVO> consultaRapidaPorCodOrigemPorTipoOperacaoEstoqueOrigemEnum(String codOrigem, TipoOperacaoEstoqueOrigemEnum tipoOperacaoEstoqueOrigemEnum, Integer unidadeEnsino, int nivelMontarDados, UsuarioVO usuario);

}
