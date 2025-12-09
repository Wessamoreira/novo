package negocio.interfaces.academico;

import negocio.comuns.academico.ProcessoMatriculaCalendarioVO;
import negocio.comuns.academico.ProcessoMatriculaVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface ProcessoMatriculaCalendarioLogInterfaceFacade {

	public void preencherIncluirProcessoMatriculaCalendarioLog(ProcessoMatriculaVO processoMatricula, ProcessoMatriculaCalendarioVO processoMatriculaCalendarioVO, String operacao, UsuarioVO usuario) throws Exception;

}
