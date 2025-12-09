package negocio.interfaces.avaliacaoinst;

import java.util.Date;
import java.util.List;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaDisciplinaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.avaliacaoinst.AvaliacaoInstitucionalPessoaAvaliadaVO;
import negocio.comuns.avaliacaoinst.AvaliacaoInstitucionalVO;

/**
 * Interface reponsvel por criar uma estrutura padro de comunidao entre a camada de controle e camada de negcio (em
 * especial com a classe Faade). Com a utilizao desta interface  possvel substituir tecnologias de uma camada da
 * aplicao com mnimo de impacto nas demais. Alm de padronizar as funcionalidades que devem ser disponibilizadas pela
 * camada de negcio, por intermdio de sua classe Faade (responsvel por persistir os dados das classes VO).
 */
public interface AvaliacaoInstitucionalInterfaceFacade {

    public AvaliacaoInstitucionalVO novo() throws Exception;

	public void incluir(AvaliacaoInstitucionalVO obj, UsuarioVO usuarioVO) throws Exception;

    public void alterar(AvaliacaoInstitucionalVO obj, UsuarioVO usuario) throws Exception;

    public void excluir(AvaliacaoInstitucionalVO obj, UsuarioVO usuario) throws Exception;

    public List<AvaliacaoInstitucionalVO> consultarPorAvaliacaoAtiva(Date prmIni, Integer unidadeEnsino, String publicoAlvo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public AvaliacaoInstitucionalVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<AvaliacaoInstitucionalVO> consultarPorCodigo(Integer valorConsulta, Boolean presencial, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<AvaliacaoInstitucionalVO> consultarPorData(Date prmIni, Date prmFim, Boolean presencial, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<AvaliacaoInstitucionalVO> consultarPorNome(String valorConsulta, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<AvaliacaoInstitucionalVO> consultarPorPublicoAlvo(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<AvaliacaoInstitucionalVO> consultarPorDataInicio(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<AvaliacaoInstitucionalVO> consultarPorDataFinal(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<AvaliacaoInstitucionalVO> consultarPorNomeUnidadeEnsino(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<AvaliacaoInstitucionalVO> consultarPorDescricaoQuestionario(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void setIdEntidade(String aIdEntidade);

    public void alterarSituacaoAvaliacao(final Integer codigo, final String situacao, AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, UsuarioVO usuario) throws Exception;

    public List<AvaliacaoInstitucionalVO> consultaRapidaPorNome(String valorConsulta, Boolean presencial, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<AvaliacaoInstitucionalVO> consultaRapidaPorPublicoAlvo(String valorConsulta, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<AvaliacaoInstitucionalVO> consultaRapidaPorDataInicio(Date dataInicio, Date dataFim, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<AvaliacaoInstitucionalVO> consultaRapidaPorDataFinal(Date dataInicio, Date dataFim, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<AvaliacaoInstitucionalVO> consultaRapidaPorUnidadeEnsino(String nome, Boolean presencial, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<AvaliacaoInstitucionalVO> consultaRapidaPorQuestionario(String nome, Boolean presencial, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<AvaliacaoInstitucionalVO> consultaRapidaPorSituacao(String situacao, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void carregarDados(AvaliacaoInstitucionalVO obj, UsuarioVO usuario) throws Exception;

    public List<AvaliacaoInstitucionalVO> consultaRapidaPorUnidadeEnsinoDataSituacaoPublicoAlvo(Integer unidadeEnsino, Date data, String situacao, String publicoAlvo, Boolean avaliacaoPresencial, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public List<AvaliacaoInstitucionalVO> consultaRapidaPorCurso(String nome, Boolean presencial, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<AvaliacaoInstitucionalVO> consultaRapidaPorTurma(String nome, Boolean presencial, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<AvaliacaoInstitucionalVO> consultaRapidaPorDisciplina(String nome, Boolean presencial, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<AvaliacaoInstitucionalVO> consultaRapidaPorUnidadeEnsinoDataSituacaoVisaoLogar(Integer unidadeEnsino, Date data, String situacao, String visaoLogar, Boolean avaliacaoPresencial, boolean possuiNaoPossuiUnidade, boolean controlarAcesso, UsuarioVO usuario, Integer curso, String nivelEducacional, String matricula, Integer codigoAvaliacaoInstitucional) throws Exception;
    
    public Boolean verificarPermissaoAlteracaoExclusao(Integer codigo, UsuarioVO usuario) throws Exception;
    
    public void alterarDataInicioDataFimAvaliacao(final AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, UsuarioVO usuario) throws Exception;

    public List<AvaliacaoInstitucionalVO> consultaRapidaPorSituacaoAtivaUltimoModuloPublicoAlvo(boolean aluno, boolean professor, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<AvaliacaoInstitucionalVO> consultaRapidaPorUnidadeEnsinoDataSituacaoVisaoLogarUltimoModulo(Integer unidadeEnsino, Integer curso, Integer turma, Date data, String situacao, String visaoLogar, String nivelEducacional, Boolean avaliacaoPresencial, boolean possuiNaoPossuiUnidade, String matriculaAluno, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	void removerAvaliacaoInstitucionalPessoaAvaliadaVO(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, AvaliacaoInstitucionalPessoaAvaliadaVO AvaliacaoInstitucionalPessoaAvaliada);

	void addAvaliacaoInstitucionalPessoaAvaliadaVO(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, AvaliacaoInstitucionalPessoaAvaliadaVO AvaliacaoInstitucionalPessoaAvaliada) throws Exception;

	Boolean consultarExistenciaAvaliacaoInstitucionalHabilitadoApresentacaoAvaliado(UnidadeEnsinoVO unidadeEnsino, UsuarioVO usuarioVO, MatriculaVO matriculaVO) throws Exception;

	List<AvaliacaoInstitucionalVO> consultarAvaliacaoInstitucionalHabilitadoApresentacaoAvaliado(UnidadeEnsinoVO unidadeEnsino, UsuarioVO usuarioVO, MatriculaVO matriculaVO) throws Exception;

	void gravarDataUltimaNotificacao(Integer avaliacaoInstitucional, UsuarioVO usuarioVO);

	List<AvaliacaoInstitucionalVO> consultarAvaliacaoInstitucionalNotificar() throws Exception;

	void realizarCloneAvaliacaoInstitucional(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, UsuarioVO usuarioVO);

	void realizarMarcacaoCursoSelecionado(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, List<CursoVO> cursoVOs);

    void realizarMarcacaoUnidadeEnsinoSelecionado(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs);

	void adicionarAvaliacaoInstitucionalCurso(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, CursoVO cursoVO)
			throws Exception;

	void removerAvaliacaoInstitucionalCurso(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, CursoVO cursoVO)
			throws Exception;

    void adicionarAvaliacaoInstitucionalUnidadeEnsino(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, UnidadeEnsinoVO unidadeEnsinoVO)
            throws Exception;

    void removerAvaliacaoInstitucionalUnidadeEnsino(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, UnidadeEnsinoVO unidadeEnsinoVO, UsuarioVO usuarioVO, Boolean marcarTodasUnidadeEnsino)
            throws Exception;

	List<AvaliacaoInstitucionalVO> consultarAvaliacaoInstitucionalUltimaAulaModuloTurmaVisaoProfessor(
			UsuarioVO usuarioVO,  int limit) throws Exception;

	List<TurmaDisciplinaVO> consultarTurmaDisciplinaUltimoModuloTurmaVisaoProfessorPorAvaliacaoInstitucional(
			UsuarioVO usuarioVO, AvaliacaoInstitucionalVO avaliacaoInstitucional, Boolean  trazerRespondido) throws Exception;

	List<AvaliacaoInstitucionalVO> consultarAvaliacaoInstitucionalUsuarioLogado(UsuarioVO usuarioVO,
			MatriculaVO matriculaVO) throws Exception;

	List<MatriculaPeriodoTurmaDisciplinaVO> consultarTurmaDisciplinaUltimoModuloTurmaVisaoAlunoPorAvaliacaoInstitucional(
			UsuarioVO usuarioVO, AvaliacaoInstitucionalVO avaliacaoInstitucional, String matricula,
			Boolean trazerRespondido) throws Exception;

    public List<AvaliacaoInstitucionalVO> consultarPorNomeAtivosFinalizados(String valorConsulta, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

}
