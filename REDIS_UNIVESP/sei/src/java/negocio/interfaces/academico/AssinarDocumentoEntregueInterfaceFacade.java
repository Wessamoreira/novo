package negocio.interfaces.academico;

import negocio.comuns.academico.AssinarDocumentoEntregueVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ProgressBarVO;

public interface AssinarDocumentoEntregueInterfaceFacade {
	
	public void assinarDocumentoEntregue(ProgressBarVO progressBarVO, AssinarDocumentoEntregueVO assinarDocumentoEntregueVO, Integer nivelMontarDados ,ConfiguracaoGeralSistemaVO configuracaoGeralSistema, UsuarioVO usuarioVO) throws Exception;

	public ProgressBarVO consultarProgressBarEmExecucao();

}
