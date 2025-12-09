package negocio.interfaces.blackboard;

import java.util.List;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.blackboard.SalaAulaBlackboardPessoaVO;
import negocio.comuns.blackboard.SalaAulaBlackboardVO;
import negocio.comuns.blackboard.enumeradores.TipoSalaAulaBlackboardEnum;
import negocio.comuns.blackboard.enumeradores.TipoSalaAulaBlackboardPessoaEnum;
import negocio.comuns.ead.ProgramacaoTutoriaOnlineVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

public interface SalaAulaBlackboardPessoaInterfaceFacade {
	
	public void validarTransferenciaInternaMatriculaEnsalada(MatriculaVO matricula, List<MatriculaPeriodoTurmaDisciplinaVO> listaMatriculaPeriodoTurmaDisciplinaVO, UsuarioVO usuarioVO) throws Exception;
	
	public Integer consultarQuantidadeMatriculaPorSalaAulaBlackboard(SalaAulaBlackboardVO salaAulaBlackboardEstagio, TipoSalaAulaBlackboardPessoaEnum tipoSalaAulaBlackboardPessoaEnum, UsuarioVO usuarioVO);
	
	public Boolean consultarSeExisteInscricaoSalaAulaBlackboardGrupo(MatriculaVO matricula, Integer disciplina, TipoSalaAulaBlackboardPessoaEnum tipoSalaAulaBlackboardPessoaEnum, TipoSalaAulaBlackboardEnum tipoSalaAulaBlackboardEnum, String ano, String semestre, UsuarioVO usuarioVO);

	public List<SalaAulaBlackboardPessoaVO> consultarAlunosDoEadTurmaDisciplinaDisponivel(CursoVO curso, TurmaVO turma, DisciplinaVO disciplina, String ano, String semestre, Integer bimestre, ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO, boolean isSomentePessoaGsuite, Integer salaaulablackboard, UsuarioVO usuarioVO) throws Exception;

	public List<SalaAulaBlackboardPessoaVO> consultarAlunosDoHorarioTurmaDisciplinaDisponivel(Integer unidadeEnsino, Integer curso, TurmaVO turma, Integer disciplina, String ano, String semestre, Integer codigoProfessor, boolean isSomentePessoaEmailInstitucional, Integer salaAulaBlackboard, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception;

	List<SalaAulaBlackboardPessoaVO> consultarPorSalaAulaBlackboard(SalaAulaBlackboardVO obj, int nivelMontarDados, UsuarioVO usuario);

	void consultarAlunosPorSalaAulaBlackboardOtimizado(SalaAulaBlackboardVO obj, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, int nivelMontarDados, UsuarioVO usuarioVO);

	void consultarProfessoresFacilitadoresESupervisoresPorSalaAulaBlackboardOtimizado(SalaAulaBlackboardVO obj, int nivelMontarDados, UsuarioVO usuarioVO);

	void persistir(SalaAulaBlackboardPessoaVO obj, UsuarioVO usuarioVO) throws Exception;

	void excluir(SalaAulaBlackboardPessoaVO obj, UsuarioVO usuarioLogado) throws Exception;

	public SalaAulaBlackboardPessoaVO consultarPorSalaAulaBlackboardEmail(SalaAulaBlackboardVO obj, String email, int nivelMontarDados, UsuarioVO usuario);

    SalaAulaBlackboardPessoaVO consultarPorIdSalaAulaBlackboardEmailTipoPessoa(String idSalaAulaBlackboard, String email, TipoSalaAulaBlackboardPessoaEnum tipoSalaAulaBlackboardPessoaEnum, int nivelmontardadosDadosminimos, UsuarioVO usuario) throws Exception;

	SalaAulaBlackboardPessoaVO consultarPorCodigo(SalaAulaBlackboardPessoaVO obj, int nivelMontarDados, UsuarioVO usuario);

	SalaAulaBlackboardPessoaVO consultarSalaAulaBlackboardPessoaPorMatriculaPorDisciplinaPorTipoSalaAulaBlackboardEnum(String matricula, Integer disciplina, TipoSalaAulaBlackboardEnum tipoSalaAulaBlackboardEnum, UsuarioVO usuarioVO);

	List<SalaAulaBlackboardPessoaVO> consultarPorIdSalaAulaBlackboardTipoPessoa(String idSalaAulaBlackboard, TipoSalaAulaBlackboardPessoaEnum tipoSalaAulaBlackboardPessoaEnum, UsuarioVO usuario);

	SalaAulaBlackboardPessoaVO consultarPorIdSalaAulaBlackboardMatricula(String idSalaAulaBlackboard, String matricula,
			int nivelMontarDados, UsuarioVO usuario) throws Exception;

}
