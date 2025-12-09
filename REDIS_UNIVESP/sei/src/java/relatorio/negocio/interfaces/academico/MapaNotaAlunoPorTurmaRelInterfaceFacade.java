package relatorio.negocio.interfaces.academico;

import java.util.List;
import java.util.Map;

import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.HorarioTurmaDiaItemVO;
import negocio.comuns.academico.SalaLocalAulaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import relatorio.negocio.comuns.academico.MapaNotaAlunoPorTurmaAlunoRelVO;
import relatorio.negocio.comuns.academico.MapaNotaAlunoPorTurmaRelVO;
import relatorio.negocio.comuns.arquitetura.CrosstabVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;
import negocio.comuns.utilitarias.dominios.OrientacaoPaginaEnum;

public interface MapaNotaAlunoPorTurmaRelInterfaceFacade {

	public List<MapaNotaAlunoPorTurmaAlunoRelVO> montarListaAlunos(TurmaVO turma, String ano, String semestre, String tipoNota, ConfiguracaoAcademicoVO conAcademicoVO, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, String tipoDisciplina, 
			String tipoAluno, List<UnidadeEnsinoVO> unidadesEnsinoVO, List<CursoVO> cursosVO, List<TurnoVO> turnosVO, DisciplinaVO disciplinaVO, SalaLocalAulaVO salaLocalAulaVO, FuncionarioVO funcionarioVO,
			String periodicidade, String tipoLayout, UsuarioVO usuarioVO, List<String> listaNotas, String ordenacao) throws Exception;
	
	public List<MapaNotaAlunoPorTurmaRelVO> executarConsultaMapaNotaTurmaPosGraduacao(TurmaVO turma, String ano, String semestre, String periodicidade, String tipoNota, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, String tipoDisciplina, String tipoAluno , String ordenador, String tipoLayout,  DisciplinaVO disciplina, ConfiguracaoAcademicoVO configuracaoAcademica, FuncionarioVO funcionarioVO, List<UnidadeEnsinoVO> unidadesEnsinoVOs, List<CursoVO> cursosVOs, List<TurnoVO> turnosVOs, SalaLocalAulaVO salaLocalAulaVO, List<String> listaNotas) throws Exception;
	
	/**
	 * @author Carlos Eugênio - 05/10/2016
	 * @param filtroRelatorioAcademicoVO
	 * @param unidadeEnsinoVOs
	 * @param cursoVOs
	 * @param turnoVOs
	 * @param turmaVO
	 * @param disciplinaVO
	 * @param ano
	 * @param semestre
	 * @param funcionarioVO
	 * @param salaLocalAulaVO
	 * @param usuarioVO
	 * @return
	 * @throws Exception
	 */
	List<MapaNotaAlunoPorTurmaRelVO> criarObjetoLayout3e4(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, List<CursoVO> cursoVOs, List<TurnoVO> turnoVOs, TurmaVO turmaVO, DisciplinaVO disciplinaVO, String ano, String semestre, FuncionarioVO funcionarioVO, SalaLocalAulaVO salaLocalAulaVO, String tipoNota, String tipoAluno, String periodicidade, String tipoDisciplina, OrientacaoPaginaEnum orientacaoPaginaEnum,  UsuarioVO usuarioVO) throws Exception;

}