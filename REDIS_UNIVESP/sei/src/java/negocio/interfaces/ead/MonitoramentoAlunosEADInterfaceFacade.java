package negocio.interfaces.ead;

import java.util.List;

import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.CalendarioAtividadeMatriculaVO;

public interface MonitoramentoAlunosEADInterfaceFacade {
	public void realizarEnvioComunicadoInternoComEnvioEmail(ComunicacaoInternaVO comunicacaoInternaVO, List<CalendarioAtividadeMatriculaVO> calendarioAtividadeMatriculaVOs, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception ;
}
