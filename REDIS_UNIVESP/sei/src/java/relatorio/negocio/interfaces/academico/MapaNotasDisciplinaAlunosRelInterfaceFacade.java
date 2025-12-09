package relatorio.negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import relatorio.negocio.comuns.academico.MapaNotasDisciplinaAlunosRelVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

public interface MapaNotasDisciplinaAlunosRelInterfaceFacade {

	public List<MapaNotasDisciplinaAlunosRelVO> criarObjeto(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, UnidadeEnsinoVO unidadeEnsino, CursoVO curso, TurmaVO turma, DisciplinaVO disciplina, String ano, String semestre, UsuarioVO usuarioVO, boolean permitirRealizarLancamentoAlunosPreMatriculados) throws Exception;

}
