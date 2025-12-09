package relatorio.negocio.interfaces.academico;

import java.util.Date;
import java.util.List;

import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import relatorio.negocio.comuns.academico.NotaNaoLancadaProfessorRelVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

public interface NotaNaoLancadaProfessorRelInterfaceFacade {

	 public List<NotaNaoLancadaProfessorRelVO> criarObjeto(List<UnidadeEnsinoVO> unidadeEnsinoVOs, String periodicidade, String ano, String semestre, Date periodoMatriculaInicial,
				Date periodoMatriculaFinal,	String campoFiltroPor, TurmaVO turma, List<CursoVO> cursoVOs, List<TurnoVO> turnoVOs, DisciplinaVO disciplina, FuncionarioVO professor,
				ConfiguracaoAcademicoVO configuracaoAcademica, String filtrarNotas, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, String layout, String ordenacao, Date periodoAulaInicial, Date periodoAulaFinal) throws Exception;

    public NotaNaoLancadaProfessorRelVO montarDados(SqlRowSet dadosSQL) throws Exception;

    public void validarDados(List<UnidadeEnsinoVO> unidadeEnsinoVOs, ConfiguracaoAcademicoVO configuracaoAcademica, List<CursoVO> cursoVOs, TurmaVO turmaVO,
    		String periodicidade, String ano, String semestre, Date periodoMatriculaInicial, Date periodoMatriculaFinal, String campoFiltroPor) throws Exception;

}