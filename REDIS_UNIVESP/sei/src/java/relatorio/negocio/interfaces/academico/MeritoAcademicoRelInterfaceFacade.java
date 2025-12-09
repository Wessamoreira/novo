package relatorio.negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import relatorio.negocio.comuns.academico.MeritoAcademicoRelVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

public interface MeritoAcademicoRelInterfaceFacade {

	/**
	 * @author Wellington Rodrigues - 26/05/2015
	 * @param turma
	 * @param curso
	 * @param unidadeEnsino
	 * @param ano
	 * @param semestre
	 * @param gradeCurricular
	 * @param turno
	 * @param periodoLetivo
	 * @param disciplina
	 * @param descricaoPeriodoLetivo
	 * @param filtroRelatorioAcademicoVO
	 * @param apresentarDisciplinaComposta
	 * @param tipoLayout
	 * @param tituloNota
	 * @return
	 * @throws Exception
	 */
	
	
	
	
	public  List<MeritoAcademicoRelVO> criarObjeto(TurmaVO turma, Integer curso, Integer unidadeEnsino, String ano, String semestre, Integer gradeCurricular, Integer turno, Integer periodoLetivo, Integer disciplina, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, boolean apresentarDisciplinaComposta, String tipoLayout, String tituloNota, String tipoAluno, boolean filtrarPorTurma, Double primeiraNota, Double SegundaNota , String rankingPor , boolean considerarNotasZeradas) throws Exception;
	
	/**
	 * @author Wellington Rodrigues - 26/05/2015
	 * @param turmaVO
	 * @param unidadeEnsinoCursoVO
	 * @param isFiltrarPorAno
	 * @param isFiltrarPorSemestre
	 * @param ano
	 * @param semestre
	 * @param campoFiltrarPor
	 * @throws Exception
	 */
	void validarDados(TurmaVO turmaVO, UnidadeEnsinoCursoVO unidadeEnsinoCursoVO, UnidadeEnsinoVO unidadeEnsinoVO, boolean isFiltrarPorAno, boolean isFiltrarPorSemestre, String ano, String semestre, String campoFiltrarPor, Double primeiraNota, Double SegundaNota) throws Exception;

}
