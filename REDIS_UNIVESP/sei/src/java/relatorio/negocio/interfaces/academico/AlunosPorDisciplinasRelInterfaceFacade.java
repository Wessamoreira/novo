package relatorio.negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import relatorio.negocio.comuns.academico.AlunosPorDisciplinasRelVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

public interface AlunosPorDisciplinasRelInterfaceFacade {

	public List<AlunosPorDisciplinasRelVO> criarObjeto(List<UnidadeEnsinoVO> unidadeEnsinoVOs, List<CursoVO> cursoVOs, String ano, String semestre, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, String lyout, TurmaVO turma, DisciplinaVO disciplina, Integer turno, Integer gradeCurricular, UsuarioVO usuarioLogado, Boolean trazerAlunoTransferencia) throws Exception;
	
	void validarDados(List<UnidadeEnsinoVO> unidadeEnsinoVOs, List<CursoVO> cursoVOs) throws Exception;
}