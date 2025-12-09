package relatorio.negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import relatorio.negocio.comuns.academico.GestaoTurmaRelVO;

public interface GestaoTurmaRelInterfaceFacade {
	
	List<GestaoTurmaRelVO> consultarDadosRelatorio(UnidadeEnsinoVO unidadeEnsinoVO, PeriodicidadeEnum periodicidade, String ano, String semestre, String filtrarPor, List<CursoVO> cursoVOs, List<TurnoVO> turnoVOs, 
			TurmaVO turmaVO, DisciplinaVO disciplinaVO, String situacaoTurma, String situacaoTurmaSubTurma, String situacaoTurmaAgrupada, String situacaoProgramacaoAula, 
			String situacaoProgramacaoAulaSubTurma, String situacaoProgramacaoAulaTurmaAgrupada, String situacaoVaga, String situacaoVagaSubTurma, String situacaoMatricula, 
			String situacaoMatriculaSubTurma, String situacaoMatriculaTurmaAgrupada, Integer periodoLetivoDe, Integer periodoLetivoAte, String filtarDisciplinaComSubTurma, String filtrarDisciplinaComTurmaAgrupada, UsuarioVO usuarioVO, Integer limit, Integer offset) throws Exception;

	Integer consultarTotalRegistroEncontrado(UnidadeEnsinoVO unidadeEnsinoVO, PeriodicidadeEnum periodicidade,
			String ano, String semestre, String filtrarPor, List<CursoVO> cursoVOs, List<TurnoVO> turnoVOs, TurmaVO turmaVO,
			DisciplinaVO disciplinaVO, String situacaoTurma, String situacaoTurmaSubTurma, String situacaoTurmaAgrupada, String situacaoProgramacaoAula, String situacaoProgramacaoAulaSubTurma, 
			String situacaoProgramacaoAulaTurmaAgrupada, String situacaoVaga, String situacaoVagaSubTurma, String situacaoMatricula, String situacaoMatriculaSubTurma, 
			String situacaoMatriculaTurmaAgrupada, Integer periodoLetivoDe, Integer periodoLetivoAte, String filtarDisciplinaComSubTurma, String filtrarDisciplinaComTurmaAgrupada, UsuarioVO usuarioVO) throws Exception;

}
