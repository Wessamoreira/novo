package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.DisciplinaAproveitadaAlteradaMatriculaVO;
import negocio.comuns.academico.DisciplinaForaGradeVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface DisciplinaForaGradeInterfaceFacade {
	public void incluir(final DisciplinaForaGradeVO obj, String periodicidadeCurso, UsuarioVO usuario) throws Exception;
	public void alterar(final DisciplinaForaGradeVO obj, String periodicidadeCurso, UsuarioVO usuario) throws Exception;
	public void excluir(DisciplinaForaGradeVO obj, UsuarioVO usuario) throws Exception;
	public void excluirDisciplinaForaGrade(Integer inclusaoDisciplinaForaGrade, UsuarioVO usuario) throws Exception;
	public void alterarDisciplinaForaGrade(Integer inclusaoDisciplinaForaGrade, String periodicidadeCurso, List<DisciplinaForaGradeVO> objetos, UsuarioVO usuario) throws Exception;
	public void incluirDisciplinaForaGrade(Integer inclusaoDisciplinaForaGrade, String periodicidadeCurso, List<DisciplinaForaGradeVO> objetos, UsuarioVO usuario) throws Exception;
	public void validarDados(DisciplinaForaGradeVO obj, String periodiocidadeCurso) throws Exception;
	public void alterarDisciplinaForaGradeAlteracaoAproveitamentoDisciplina(final Integer codigo, final DisciplinaAproveitadaAlteradaMatriculaVO obj, UsuarioVO usuario) throws Exception;
}
