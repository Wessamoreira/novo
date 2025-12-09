package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.CalendarioLancamentoNotaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;

public interface CalendarioLancamentoNotaInterfaceFacade {

	void persistir(CalendarioLancamentoNotaVO calendarioLancamentoNotaVO, Boolean validarAcesso, UsuarioVO usuarioVO, Boolean atualizarCalendarioAtividadeMatriculaComPeriodo) throws Exception;

	void excluir(CalendarioLancamentoNotaVO calendarioLancamentoNotaVO, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;

	CalendarioLancamentoNotaVO consultarPorConfiguracaoAcademicoAnoSemestre(Integer codigoDesconsiderar, Integer unidadeEnsino, Integer unidadeEnsinoCurso, Integer turma, Integer professor, Integer disciplina, Integer configuracaoAcademica, String ano, String semestre, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;

	Integer consultarTotalRegistro(Integer codigoCalendarioLancamentoNota, Integer unidadeEnsino, Integer unidadeEnsinoCurso, Integer turma, Integer professor, Integer disciplina, Integer configuracaoAcademica, String ano, String semestre) throws Exception;

	List<CalendarioLancamentoNotaVO> consultar(Integer codigoCalendarioLancamentoNota, Integer unidadeEnsino, Integer unidadeEnsinoCurso, Integer turma, Integer professor, Integer disciplina, Integer configuracaoAcademica, String ano, String semestre, Boolean validarAcesso, UsuarioVO usuarioVO, Integer limit, Integer offset) throws Exception;

	CalendarioLancamentoNotaVO consultarPorCalendarioAcademicoUtilizar(Integer unidadeEnsino, Integer turma, Boolean turmaAgrupada, Integer professor, Integer disciplina, Integer configuracaoAcademica, String periodicidade, String ano, String semestre, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;

	void validarDados(CalendarioLancamentoNotaVO obj) throws ConsistirException;

	void verificarApresentarCampoNotaVisaoAluno(CalendarioLancamentoNotaVO calendarioLancamentoNotaVO) throws Exception;

	CalendarioLancamentoNotaVO consultarPorCalendarioAcademicoProfessorExcluisoLancamentoNota(Integer unidadeEnsino,
			Integer turma, Boolean turmaAgrupada, Integer professor, Integer disciplina, Integer configuracaoAcademica,
			String periodicidade, String ano, String semestre, Boolean validarAcesso, UsuarioVO usuarioVO)
			throws Exception;

}
