package relatorio.negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import relatorio.negocio.comuns.academico.AlunosPorUnidadeCursoTurmaRelVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

public interface AlunosPorUnidadeCursoTurmaRelInterfaceFacade {

	public List<AlunosPorUnidadeCursoTurmaRelVO> criarObjeto(List<UnidadeEnsinoVO> unidadeEnsinoVOs, List<CursoVO> cursoVOs, List<TurnoVO> turnoVOs, TurmaVO turma, DisciplinaVO disciplina, String ano, String semestre, String tipoMatricula, Boolean trazerSomenteAlunosAtivos, Boolean trazerFiliacao, String tipoAluno, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, FiltroRelatorioAcademicoVO filtroAcademicoVO, UsuarioVO usuarioVO, String situacaoAlunoCurso, Boolean trazerAlunoTransferencia, String periodicidade) throws Exception;

	public String getIdEntidadePerfilTurmaRel();
	
	public void validarDadosAnoSemestre(PeriodicidadeEnum periodicidadeEnum, String ano, String semestre, String filtrarPor) throws Exception;
}
