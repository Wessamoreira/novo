package negocio.interfaces.sentry;

import java.util.List;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface ServicoIntegracaoSentryInterfaceFacade {

	public void registrarExceptionSentry(Throwable e, List<String> listaInformacaoAdicional, Boolean enviarSentry, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO);
}
