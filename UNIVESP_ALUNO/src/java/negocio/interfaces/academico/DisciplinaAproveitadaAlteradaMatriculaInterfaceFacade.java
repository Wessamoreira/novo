package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.DisciplinaAproveitadaAlteradaMatriculaVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface DisciplinaAproveitadaAlteradaMatriculaInterfaceFacade {
	public void incluirDisciplinaAproveitadaAlteradaMatricula(List<DisciplinaAproveitadaAlteradaMatriculaVO> DisciplinaAproveitadaAlteradaMatriculaVOs, MatriculaVO matriculaVO, GradeCurricularVO gradeCurricularVO, UsuarioVO usuario) throws Exception;
	public void incluir(final DisciplinaAproveitadaAlteradaMatriculaVO obj, UsuarioVO usuario) throws Exception;
	public List<DisciplinaAproveitadaAlteradaMatriculaVO> consultarAproveitamentoPorMatricula(MatriculaVO matricula, GradeCurricularVO gradeCurricularVO, UsuarioVO usuarioVO) throws Exception;
	public DisciplinaAproveitadaAlteradaMatriculaVO consultarAproveitamentoPorMatricula(String matricula,
			Integer codigoDisciplina, UsuarioVO usuarioVO);
}
