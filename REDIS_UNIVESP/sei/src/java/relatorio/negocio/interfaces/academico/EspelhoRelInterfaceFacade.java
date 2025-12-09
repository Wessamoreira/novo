package relatorio.negocio.interfaces.academico;

import java.util.Date;
import java.util.List;

import negocio.comuns.academico.ProfessorTitularDisciplinaTurmaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import relatorio.negocio.comuns.academico.DiarioRegistroAulaVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

public interface EspelhoRelInterfaceFacade {

	List<DiarioRegistroAulaVO> consultarRegistroAula(TurmaVO turmaVO, String semestre, String ano, Integer professor, Integer disciplina, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, FiltroRelatorioAcademicoVO filtroAcademicoVO, String mes, String anoMes, UsuarioVO usuarioVO) throws Exception;

	List<DiarioRegistroAulaVO> consultarRegistroAulaEspelho(TurmaVO turmaVO, String semestre, String ano, Integer professor, Integer disciplina, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO, boolean permitirRealizarLancamentoAlunosPreMatriculados) throws Exception;

	String getDescricaoFiltros();

	List<DiarioRegistroAulaVO> consultarRegistroAulaEspelho(List<ProfessorTitularDisciplinaTurmaVO> listaProfessores, TurmaVO turmaVO, Integer disciplina, String semestre, String ano, Boolean apenasAlunosAtivos, Boolean trazerAlunosPendentesFinanceiramente, String filtroTipoCursoAluno, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO, String tipoLayout, String tipoAluno, Date dataInicio, Date dataFim, FiltroRelatorioAcademicoVO filtroAcademicoVO, String mes, String anoMes, Date dataInicioPeriodoMatricula, Date dataFimPeriodoMatricula, Boolean trazerAlunoTransferencia, boolean permitirRealizarLancamentoAlunosPreMatriculados, Boolean apresentarDataMatriculaPeriodo) throws Exception;

}
