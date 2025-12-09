package negocio.interfaces.academico;

import negocio.comuns.academico.ProcessoMatriculaVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface ProcessoMatriculaLogInterfaceFacade {
	
	public void preencherIncluirProcessoMatriculaLog(ProcessoMatriculaVO processoMatricula, String operacao, UsuarioVO usuario) throws Exception;
	
}
