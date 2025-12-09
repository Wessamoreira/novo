package negocio.interfaces.secretaria;

import java.util.List;

import negocio.comuns.academico.HistoricoGradeAnteriorAlteradaVO;
import negocio.comuns.arquitetura.UsuarioVO;

import negocio.comuns.secretaria.HistoricoGradeVO;

public interface HistoricoGradeInterfaceFacade {
	public void incluir(final HistoricoGradeVO obj, UsuarioVO usuario) throws Exception;

	public void alterar(final HistoricoGradeVO obj, UsuarioVO usuario) throws Exception;

	public void excluir(HistoricoGradeVO obj, UsuarioVO usuario) throws Exception;

	public void validarDados(HistoricoGradeVO obj) throws Exception;

	public List<HistoricoGradeVO> consultarGradeOrigemAntigaAlunoPorMatricula(String matricula, UsuarioVO usuarioVO);

	public void incluirHistoricoGrade(Integer transferenciaMatrizCurricular, List objetos, UsuarioVO usuario) throws Exception;

	public List<HistoricoGradeVO> consultarPorMatriculaGradeMigrarAlunoDisciplinasIguaisTransferidasGrade(String matricula, Integer gradeAtual, UsuarioVO usuarioVO);

	public void alterarHistoricoGradePorHistoricoGradeAnteriorAlterada(final HistoricoGradeAnteriorAlteradaVO obj, UsuarioVO usuario) throws Exception;

	void excluirComBaseNaMatriculaPeriodo(Integer matriculaPeriodo , UsuarioVO usuarioLogado) throws Exception;

}
