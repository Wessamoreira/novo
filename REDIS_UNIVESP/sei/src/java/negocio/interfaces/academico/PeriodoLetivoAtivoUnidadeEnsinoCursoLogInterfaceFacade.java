package negocio.interfaces.academico;

import negocio.comuns.academico.PeriodoLetivoAtivoUnidadeEnsinoCursoVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface PeriodoLetivoAtivoUnidadeEnsinoCursoLogInterfaceFacade {

	public void preencherIncluirProcessoMatriculaLog(PeriodoLetivoAtivoUnidadeEnsinoCursoVO periodoLetivoAtivoUnidadeEnsinoCursoVO, String nivelProcessoMatricula, String operacao, UsuarioVO usuario) throws Exception;

}
