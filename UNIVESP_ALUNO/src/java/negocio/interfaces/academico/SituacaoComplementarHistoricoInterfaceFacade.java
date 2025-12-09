package negocio.interfaces.academico;

import negocio.comuns.academico.SituacaoComplementarHistoricoVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface SituacaoComplementarHistoricoInterfaceFacade {

	void persistir(SituacaoComplementarHistoricoVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	SituacaoComplementarHistoricoVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario);

	void excluir(SituacaoComplementarHistoricoVO obj, boolean verificarAcesso, UsuarioVO usuario);

}
