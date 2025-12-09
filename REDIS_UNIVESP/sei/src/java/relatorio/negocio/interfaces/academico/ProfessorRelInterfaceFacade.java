package relatorio.negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import relatorio.negocio.comuns.academico.ProfessoresPorUnidadeCursoTurmaRelVO;

public interface ProfessorRelInterfaceFacade {

    public List<ProfessoresPorUnidadeCursoTurmaRelVO> criarObjeto(UnidadeEnsinoVO unidadeEnsino, CursoVO curso, TurmaVO turma, DisciplinaVO disciplina, String semestre, String ano, String situacaoProfessor,String escolaridade, UsuarioVO usuarioVO) throws Exception;
}
