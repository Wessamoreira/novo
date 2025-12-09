package relatorio.negocio.interfaces.academico;

import java.util.List;

import relatorio.negocio.comuns.academico.HistoricoTurmaRelVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface HistoricoTurmaRelInterfaceFacade {
	
    public List<HistoricoTurmaRelVO> criarObjeto(TurmaVO turmaVO, String semestre, String ano, Integer unidadeEnsino, Integer disciplina, boolean filtroVisaoProfessor, boolean trazerAlunoPendenteFinanceiramente, Boolean trazerDisciplinaAproveitadas, UsuarioVO usuarioVO, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, String tipoAluno,boolean ApresentarAlunoTurmaOrigem, boolean permitirRealizarLancamentoAlunosPreMatriculados) throws Exception;

    public void setDescricaoFiltros(String string);

    public String getDescricaoFiltros();

	List<HistoricoTurmaRelVO> realizarGeracaoDadosApresentarRelatorio(List<HistoricoVO> historicoVOs, TurmaVO turmaVO, DisciplinaVO disciplinaVO, String ano, String semestre,UsuarioVO usuario,boolean ApresentarAlunoTurmaOrigem) throws Exception;

}
