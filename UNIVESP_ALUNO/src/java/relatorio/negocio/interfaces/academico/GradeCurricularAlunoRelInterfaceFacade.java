package relatorio.negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.CargoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import relatorio.negocio.comuns.academico.GradeCurricularAlunoRelVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

public interface GradeCurricularAlunoRelInterfaceFacade {

	public void validarDados(MatriculaVO matriculaVO, TurmaVO turmaVO, String ano, String semestre, boolean filtrarPorAluno) throws Exception;

	public GradeCurricularAlunoRelVO criarObjeto(GradeCurricularAlunoRelVO gradeCurricularAlunoRelVO, Boolean apresentarTodasDisciplinasGrade, MatriculaVO matriculaVO, String nomeTurma, String layout) throws Exception;

	/**
	 * @author Rodrigo Wind - 19/11/2015
	 * @param filtroRelatorioAcademicoVO
	 * @param filtrarPorAluno
	 * @param matricula
	 * @param turmaVO
	 * @param ano
	 * @param semestre
	 * @param layout
	 * @param ordenarPor
	 * @param apresentarTodasDisciplinasGrade
	 * @param unidadeEnsinoLogado
	 * @param usuarioLogado
	 * @return
	 * @throws Exception
	 */
	List<GradeCurricularAlunoRelVO> realizarGeracaoRelatorio(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, Boolean filtrarPorAluno, MatriculaVO matricula, TurmaVO turmaVO, String ano, String semestre, String layout, String ordenarPor, Boolean apresentarTodasDisciplinasGrade, FuncionarioVO funcionarioPrincipal, CargoVO cargoFuncionarioPrincipal, FuncionarioVO funcionarioSecundario, CargoVO cargoFuncionarioSecundario, UnidadeEnsinoVO unidadeEnsinoLogado, boolean controlarAcesso, UsuarioVO usuarioLogado) throws Exception;

}
