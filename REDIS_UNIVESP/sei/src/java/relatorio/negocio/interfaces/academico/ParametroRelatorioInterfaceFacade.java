package relatorio.negocio.interfaces.academico;

import negocio.comuns.arquitetura.UsuarioVO;
import relatorio.negocio.comuns.academico.ParametroRelatorioVO;

public interface ParametroRelatorioInterfaceFacade {

	void persistirParametroRelatorio(String entidade, String campo, boolean apresentarCampo, UsuarioVO usuario) throws Exception;

	ParametroRelatorioVO consultarPorEntidadeCampo(String entidade, String campo, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

}
