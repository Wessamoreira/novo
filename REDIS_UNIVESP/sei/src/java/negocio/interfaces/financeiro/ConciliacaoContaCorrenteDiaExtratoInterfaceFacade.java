package negocio.interfaces.financeiro;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConciliacaoContaCorrenteDiaExtratoVO;

public interface ConciliacaoContaCorrenteDiaExtratoInterfaceFacade {

	void persistir(List<ConciliacaoContaCorrenteDiaExtratoVO> lista, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void excluir(ConciliacaoContaCorrenteDiaExtratoVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception;

	void persistir(ConciliacaoContaCorrenteDiaExtratoVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void atualizarConciliacaoContaCorrenteDiaExtratoCampoCodigoSei(Integer codigoConciliacaoDiaExtrato, UsuarioVO usuario) throws Exception;

}
