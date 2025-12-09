package relatorio.negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import relatorio.negocio.comuns.academico.AlunosReprovadosRelVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

public interface AlunosReprovadosRelInterfaceFacade {

	public void validarDados(String tipoRelatorio, UnidadeEnsinoVO unidadeEnsino, CursoVO curso, TurmaVO turma, DisciplinaVO disciplina) throws ConsistirException;

	public List<AlunosReprovadosRelVO> criarObjeto(String motivoReprovacao, String tipoRelatorio, String situacaoAlunoReposicao, UnidadeEnsinoVO unidadeEnsino, CursoVO curso, TurmaVO turma, DisciplinaVO disciplina, String ano, String semestre,FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, Boolean desconsiderarAlunosQueAprovaramNaDisciplinaAposReprovacao, Boolean desconsiderarAlunosCursandoDisciplinaAposReprovacao, String tipoLayout, Boolean filtrarSituacaoAtualMatricula) throws Exception;

	String realizarEnvioComunicacaoInternaAlunos(String motivoReprovacao, String tipoRelatorio, String situacaoAlunoReposicao, UnidadeEnsinoVO unidadeEnsino, CursoVO curso, TurmaVO turma, DisciplinaVO disciplina, String ano, String semestre, ComunicacaoInternaVO comunicacaoInternaVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario,FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO) throws Exception;

}