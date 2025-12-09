package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.ProgramacaoFormaturaCursoVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface ProgramacaoFormaturaCursoInterfaceFacade {
	
	public List<ProgramacaoFormaturaCursoVO> consultarCursosPorCodigoProgramacaoFormatura(Integer codigoProgramacao, UsuarioVO usuario) throws Exception;
	
}
