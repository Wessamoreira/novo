package negocio.interfaces.financeiro;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.AgenteNegativacaoCobrancaContaReceberVO;
import negocio.comuns.financeiro.TipoDocumentoPendenciaAgenteCobrancaVO;

public interface TipoDocumentoPendenciaAgenteCobrancaInterfaceFacade {

	void persistir(List<TipoDocumentoPendenciaAgenteCobrancaVO> lista, AgenteNegativacaoCobrancaContaReceberVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	List<TipoDocumentoPendenciaAgenteCobrancaVO> consultarPorAgenteNegativacaoCobrancaContaReceberVO(AgenteNegativacaoCobrancaContaReceberVO obj, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

}
