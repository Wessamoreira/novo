package negocio.interfaces.academico;

import java.util.List;

import org.richfaces.event.FileUploadEvent;

import negocio.comuns.academico.GradeDisciplinaVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.TrabalhoConclusaoCursoEtapaVO;
import negocio.comuns.academico.TrabalhoConclusaoCursoInteracaoVO;
import negocio.comuns.academico.TrabalhoConclusaoCursoVO;
import negocio.comuns.academico.enumeradores.EtapaTCCEnum;
import negocio.comuns.academico.enumeradores.SituacaoTCCEnum;
import negocio.comuns.academico.enumeradores.TipoArquivoTCCEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface TrabalhoConclusaoCursoInterfaceFacade {

	void persitir(TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO, UsuarioVO usuarioVO) throws Exception;
	
	void gravarPlanoTCC(TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO, UsuarioVO usuarioVO) throws Exception;	
	
	void realizarSolictacaoAvaliacaoOrientadorPlanoTCC(TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO, UsuarioVO usuarioVO) throws Exception;
	
	void realizarRevisaoPlanoTCC(TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO, UsuarioVO usuarioVO) throws Exception;
	
	void realizarAprovacaoPlanoTCC(TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO, UsuarioVO usuarioVO) throws Exception;
	
	void realizarReprovacaoTCC(TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;
	
	void realizarAprovacaoElaboracaoTCC(TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO, UsuarioVO usuarioVO) throws Exception;
	
	void gravarMembrosBanca(TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO, UsuarioVO usuarioVO) throws Exception;
	
	void realizarAprovacaoReprovacaoAvaliacaoTCC(TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, List<HistoricoVO> historicoAluno, UsuarioVO usuarioVO) throws Exception;
	
	
	
	void realizarSolicitacaoOrientadorTCC(TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO, UsuarioVO usuarioVO) throws Exception;
	
	void realizarEnvioInteracaoTCC(TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO, TrabalhoConclusaoCursoInteracaoVO trabalhoConclusaoCursoInteracaoVO, UsuarioVO usuarioVO) throws Exception;
	
	void realizarAlteracaoPrazoExecucaoEtapaTCC(TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO, UsuarioVO usuarioVO) throws Exception;
	
	void realizarRetornoFaseAnteriorTCC(TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO, UsuarioVO usuarioVO) throws Exception;
	
	void realizarPostagemArquivoTCC(FileUploadEvent uploadEvent, TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO, TipoArquivoTCCEnum tipoArquivoTCCEnum, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception;
	
	void realizarEnvioNovoArquivoTCC(TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO, UsuarioVO usuarioVO) throws Exception;
	
	TrabalhoConclusaoCursoVO consultarTrabalhoConclusaoCursoAtualAluno(String matricula) throws Exception;
	
	Boolean realizarVerificacaoTrabalhoConclusaoCursoAptoApresentar(TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO, UsuarioVO usuarioVO) throws Exception;
	
	TrabalhoConclusaoCursoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;
	
	List<TrabalhoConclusaoCursoVO> consultar(Integer unidadeEnsino, Integer curso, Integer turma, Integer orientador, Integer coordenador, String matricula, 
			EtapaTCCEnum etapaTCCEnum, SituacaoTCCEnum situacaoTCCEnum, Boolean filtroSomenteAtrasados, Boolean filtroAguardandoOrientador, Boolean filtroMinhaAvaliacao, 
			Integer limit, Integer offset, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;
	
	List<TrabalhoConclusaoCursoEtapaVO> consultarAgrupandoPorEtapaSituacao(Integer unidadeEnsino, Integer curso, Integer turma, Integer orientador, Integer coordenador, String matricula, Boolean filtroSomenteAtrasados, Boolean filtroAguardandoOrientador, Boolean filtroMinhaAvaliacao) throws Exception;
	
	Integer consultarTotalRegistro(Integer unidadeEnsino, Integer curso, Integer turma, Integer orientador, Integer coordenador, String matricula, EtapaTCCEnum etapaTCCEnum, SituacaoTCCEnum situacaoTCCEnum, Boolean filtroSomenteAtrasados, Boolean filtroAguardandoOrientador, Boolean filtroMinhaAvaliacao);

	void realizarSolicitacaoAvaliacaoArquivoTCC(TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO, UsuarioVO usuarioVO) throws Exception;

	void realizarCriacaoTrabalhoConclusaoCursoAluno(Integer curso, Integer aluno, Integer gradeCurricular, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, UsuarioVO usuarioVO) throws Exception;

	void carregarDadosSubordinados(TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO, UsuarioVO usuario) throws Exception;

	TrabalhoConclusaoCursoVO consultarPorMatriculaPeridoTurmaDisciplina(Integer matriculaPeridoTurmaDisciplina, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

	void realizarConfirmacaoSolicitacaoOrientadorTCC(TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO, UsuarioVO usuarioVO) throws Exception;

	void realizarDefinicaoOrientadorTCC(TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO, UsuarioVO usuarioVO) throws Exception;
	
	Integer realizarVerificacaoQtdeNovidadesTCC(TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO, UsuarioVO usuarioVO) throws Exception;
	
	void alterarDataUltimoAcessoAluno(final TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO) throws Exception;
	
	Boolean realizarVerificacaoPrimeiroAcessoAluno(TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO, UsuarioVO usuarioVO) throws Exception;
	
	void realizarCriacaoTrabalhoConclusaoCursoGradeDisciplina(GradeDisciplinaVO gradeDisciplina, UsuarioVO usuarioLogado) throws Exception;
	
	Integer consultarTotalAlunosTCCGradeDisciplina(GradeDisciplinaVO gradeDisciplina, UsuarioVO usuarioLogado);
	
	void executarAtualizarTrabalhoConclusaoCursoAposLancamentoNotas(TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception;
	
	void executarEnvioMensagemTCCEmAtraso() throws Exception;
		
	void executarEnvioMensagemReprovacaoAutomaticaPorAtrasoTCC() throws Exception;
	
	void alterarCoordenadorTCC(final UnidadeEnsinoVO unidadeEnsino, UsuarioVO usuarioLogado) throws Exception;
	
	public void validarTrabalhoConclusaoCursoIniciado(String matricula) throws Exception;
	
	public void alterarTitulo(final TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO) throws Exception;
	
	public Boolean realizarVerificacaoTrabalhoConclusaoCursoVinculadoProfessor(Integer codPessoa) throws Exception;
	
	public void realizarEnvioEmailDepartamentoFinanceiro(TrabalhoConclusaoCursoVO tcc, String msg, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;
	
	public void alterarMedia(final TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO) throws Exception;
	
	public void alterarMediaFormatacao(final TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO) throws Exception;
	
	public void alterarMediaConteudo(final TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO) throws Exception;
}