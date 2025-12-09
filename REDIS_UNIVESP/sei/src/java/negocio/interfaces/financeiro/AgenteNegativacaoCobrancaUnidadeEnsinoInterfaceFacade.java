package negocio.interfaces.financeiro;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.AgenteNegativacaoCobrancaContaReceberVO;
import negocio.comuns.financeiro.AgenteNegativacaoCobrancaUnidadeEnsinoVO;

public interface AgenteNegativacaoCobrancaUnidadeEnsinoInterfaceFacade {

	void persistir(List<AgenteNegativacaoCobrancaUnidadeEnsinoVO> lista, AgenteNegativacaoCobrancaContaReceberVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	List<AgenteNegativacaoCobrancaUnidadeEnsinoVO> consultarPorAgenteNegativacaoCobrancaContaReceberVO(AgenteNegativacaoCobrancaContaReceberVO obj, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

}
