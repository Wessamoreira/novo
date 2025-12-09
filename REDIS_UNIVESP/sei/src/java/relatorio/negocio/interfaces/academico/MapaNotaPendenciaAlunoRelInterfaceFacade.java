package relatorio.negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.ConfiguracaoAcademicaNotaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import relatorio.negocio.comuns.academico.MapaNotaPendenciaAlunoRelVO;
import relatorio.negocio.comuns.academico.MapaNotaPendenciaAlunoTurmaRelVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

/**
 * @author Wellington Rodrigues - 16 de jul de 2015
 *
 */
public interface MapaNotaPendenciaAlunoRelInterfaceFacade {

	/**
	 * 
	 * @author Wellington Rodrigues - 20 de jul de 2015
	 * @param unidadeEnsino
	 * @param periodicidade
	 * @param ano
	 * @param semestre
	 * @param curso
	 * @param turma
	 * @param disciplina
	 * @param situacaoAluno
	 * @param configuracaoAcademico
	 * @param situacaoNotaRecuperacao
	 * @param tipoLayout
	 * @param configuracaoAcademicaNotaVOs
	 * @param filtroRelatorioAcademicoVO
	 * @param usuarioVO
	 * @return
	 * @throws Exception
	 */
	List<MapaNotaPendenciaAlunoRelVO> executarCriacaoObjeto(Integer unidadeEnsino, String periodicidade, String ano, String semestre, Integer curso, TurmaVO turma, Integer disciplina, String situacaoAluno, Integer configuracaoAcademico, String situacaoNotaRecuperacao, String tipoLayout, String filtrarNota, List<ConfiguracaoAcademicaNotaVO> configuracaoAcademicaNotaVOs, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO,List<ConfiguracaoAcademicaNotaVO> configuracaoAcademicaNotaNaoRecuperacaoVOs, UsuarioVO usuarioVO) throws Exception;

	/**
	 * @author Wellington Rodrigues - 21 de jul de 2015
	 * @param configuracaoAcademicaNotaVOs
	 * @param ano
	 * @param apresentarCampoAno
	 * @param semestre
	 * @param apresentarCampoSemestre
	 * @throws Exception
	 */
	void validarDados(List<ConfiguracaoAcademicaNotaVO> configuracaoAcademicaNotaVOs, String ano, boolean apresentarCampoAno, String semestre, boolean apresentarCampoSemestre, String tipoAluno) throws Exception;

	/**
	 * @author Wellington Rodrigues - 22 de jul de 2015
	 * @param unidadeEnsino
	 * @param periodicidade
	 * @param ano
	 * @param semestre
	 * @param curso
	 * @param turma
	 * @param disciplina
	 * @param situacaoAluno
	 * @param configuracaoAcademico
	 * @param situacaoNotaRecuperacao
	 * @param filtrarNota
	 * @param configuracaoAcademicaNotaVOs
	 * @param filtroRelatorioAcademicoVO
	 * @param usuarioVO
	 * @return
	 * @throws Exception
	 */
	List<MapaNotaPendenciaAlunoTurmaRelVO> executarCriacaoMapaNotaPendenciaAlunoTurma(Integer unidadeEnsino, String periodicidade, String ano, String semestre, Integer curso, TurmaVO turma, Integer disciplina, String situacaoAluno, Integer configuracaoAcademico, String situacaoNotaRecuperacao, String filtrarNota, List<ConfiguracaoAcademicaNotaVO> configuracaoAcademicaNotaVOs, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, UsuarioVO usuarioVO) throws Exception;

}
