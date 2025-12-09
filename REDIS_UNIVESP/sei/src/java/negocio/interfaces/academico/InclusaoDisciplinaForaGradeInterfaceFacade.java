package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.DisciplinaForaGradeVO;
import negocio.comuns.academico.InclusaoDisciplinaForaGradeVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

public interface InclusaoDisciplinaForaGradeInterfaceFacade {
	public void incluir(final InclusaoDisciplinaForaGradeVO obj, UsuarioVO usuario) throws Exception;
	public void alterar(final InclusaoDisciplinaForaGradeVO obj, UsuarioVO usuario) throws Exception;
	public void excluir(InclusaoDisciplinaForaGradeVO obj, UsuarioVO usuario) throws Exception;
	public List<InclusaoDisciplinaForaGradeVO> consultaRapidaPorMatricula(String matricula, UsuarioVO usuarioVO) throws Exception;
	public List<InclusaoDisciplinaForaGradeVO> consultaRapidaPorNome(String nome, UsuarioVO usuarioVO) throws Exception;
	public void carregarDados(InclusaoDisciplinaForaGradeVO obj, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception;
	public List<InclusaoDisciplinaForaGradeVO> consultar(String campoConsulta, String valorConsulta, UsuarioVO usuarioVO) throws Exception;
	public List<MatriculaVO> consultarAluno(String campoConsultaAluno, String valorConsultaAluno, Integer unidadeEnsino, UsuarioVO usuarioVO) throws Exception;
	public MatriculaVO consultarAlunoPorMatricula(MatriculaVO matriculaVO, Integer unidadeEnsino, UsuarioVO usuarioVO) throws Exception;
	public void adicionarDisciplinaForaGrade(InclusaoDisciplinaForaGradeVO obj, DisciplinaForaGradeVO disciplinaForaGradeVO, UsuarioVO usuarioVO) throws Exception;
	public void removerDisciplinaForaPrazo(InclusaoDisciplinaForaGradeVO obj, DisciplinaForaGradeVO disciplinaForaGradeVO, UsuarioVO usuarioVO);
	public void persistir(InclusaoDisciplinaForaGradeVO obj, UsuarioVO usuario) throws Exception;
}
