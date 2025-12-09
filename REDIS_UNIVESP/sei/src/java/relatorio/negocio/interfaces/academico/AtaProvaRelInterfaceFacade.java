package relatorio.negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.SituacaoRecuperacaoNotaEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import relatorio.negocio.comuns.academico.AtaProvaRelVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

public interface AtaProvaRelInterfaceFacade {

	public List<AtaProvaRelVO> executarConsultaParametrizada(TurmaVO turmaVO, CursoVO cursoVO, Integer disciplina, Integer unidadeEnsino, String ano, String semestre, boolean trazerAlunoPendenteFinanceiro, boolean aprovados, boolean reprovados, boolean reprovadosPorFalta, boolean cursando, SituacaoRecuperacaoNotaEnum situacaoRecuperacaoNota, boolean permitirRealizarLancamentoAlunosPreMatriculados, UsuarioVO usuario) throws Exception;

	public List<AtaProvaRelVO> executarConsultaParametrizadaVisaoFuncionario(TurmaVO turmaVO, CursoVO cursoVO, Integer disciplina, Integer professorTitular, Integer unidadeEnsino, String ano, String semestre, FiltroRelatorioAcademicoVO filtroAcademicoVO, boolean aprovados, boolean reprovados, boolean reprovadosPorFalta, boolean cursando, Boolean trazerAlunoTransferenciaMatrizCurricular, SituacaoRecuperacaoNotaEnum situacaoRecuperacaoNota, UsuarioVO usuario, boolean permitirRealizarLancamentoAlunosPreMatriculados) throws Exception;

	public void validarDados(Integer turma, Integer disciplina, boolean gerandoRelatorio, String ano, boolean permitirGerarAtaProvaRetroativo) throws Exception;

	public void validarDadosVisaoCoordenador(TurmaVO turma, Integer disciplina, String ano, String semestre, boolean permitirGerarAtaProvaRetroativo) throws Exception;

}