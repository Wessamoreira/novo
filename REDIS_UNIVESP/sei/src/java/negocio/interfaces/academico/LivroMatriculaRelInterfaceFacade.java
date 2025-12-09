package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import relatorio.negocio.comuns.academico.LivroMatriculaRelVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

public interface LivroMatriculaRelInterfaceFacade {

	public List<LivroMatriculaRelVO> criarObjeto(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, List<CursoVO> cursoVOs,
			List<UnidadeEnsinoVO> unidadeEnsinoVOs, List<TurnoVO> turnoVOs, TurmaVO turma, String ano, String semestre, String periodicidade, String tipoAluno) throws Exception;

}