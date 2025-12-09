package negocio.interfaces.financeiro;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConciliacaoContaCorrenteVO;
import negocio.comuns.financeiro.ParametrizarOperacoesAutomaticasConciliacaoItemVO;

public interface ParametrizarOperacoesAutomaticasConciliacaoItemInterfaceFacade {

	void persistir(List<ParametrizarOperacoesAutomaticasConciliacaoItemVO> lista, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void excluir(ParametrizarOperacoesAutomaticasConciliacaoItemVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception;

	void consultaRapidaPorParametrizarOperacoesAutomaticasConciliacao(ConciliacaoContaCorrenteVO obj, UsuarioVO usuario) throws Exception;

	void persistir(ConciliacaoContaCorrenteVO conciliacao, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void excluir(ConciliacaoContaCorrenteVO conciliacao, boolean verificarAcesso, UsuarioVO usuario) throws Exception;

}
