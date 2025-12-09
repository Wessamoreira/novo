package negocio.interfaces.academico;

import java.util.Date;
import java.util.List;

import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TextoPadraoDeclaracaoVO;
import negocio.comuns.academico.TransferenciaEntradaDisciplinasAproveitadasVO;
import negocio.comuns.academico.TransferenciaEntradaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.protocolo.RequerimentoVO;
import negocio.comuns.utilitarias.dominios.TipoTransferenciaEntrada;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a
 * camada de controle e camada de negócio (em especial com a classe Façade). Com
 * a utilização desta interface é possível substituir tecnologias de uma camada
 * da aplicação com mínimo de impacto nas demais. Além de padronizar as
 * funcionalidades que devem ser disponibilizadas pela camada de negócio, por
 * intermédio de sua classe Façade (responsável por persistir os dados das
 * classes VO).
 */
public interface TransferenciaEntradaInterfaceFacade {

	public TransferenciaEntradaVO novo() throws Exception;

	public void incluir(TransferenciaEntradaVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO,Boolean criarHistoricoTransferenciaEntrada, Boolean controleAcesso,   UsuarioVO usuario) throws Exception;

	public void alterar(TransferenciaEntradaVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	void alterarTransferenciaEntradaEfetivada(final TransferenciaEntradaVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;
	
	public void alterarSituacao(Integer codigo, String situacao) throws Exception;

	public void alterarMatricula(Integer codigo, String matricula) throws Exception;

	public void excluir(TransferenciaEntradaVO obj, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;

	public TransferenciaEntradaVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	public List<TransferenciaEntradaVO> consultarPorCodigo(Integer valorConsulta, String situacaoMatriculado, TipoTransferenciaEntrada tipoTransferenciaEntrada, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	public List<TransferenciaEntradaVO> consultarPorData(Date prmIni, Date prmFim, String situacaoMatriculado, TipoTransferenciaEntrada tipoTransferenciaEntrada, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	public List<TransferenciaEntradaVO> consultarPorSituacao(String valorConsulta, String situacaoMatriculado, TipoTransferenciaEntrada tipoTransferenciaEntrada, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	public List<TransferenciaEntradaVO> consultarPorCurso(String valorConsulta, String situacaoMatriculado, TipoTransferenciaEntrada tipoTransferenciaEntrada, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	public List<TransferenciaEntradaVO> consultarPorAluno(String valorConsulta, String situacaoMatriculado, TipoTransferenciaEntrada tipoTransferenciaEntrada, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	public List<TransferenciaEntradaVO> consultarPorMatriculaMatricula(String valorConsulta, String situacaoMatriculado, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	public List<TransferenciaEntradaVO> consultarPorCodigoRequerimento(Integer valorConsulta, String situacaoMatriculado, TipoTransferenciaEntrada tipoTransferenciaEntrada, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	public List<TransferenciaEntradaVO> consultarPorInstituicaoOrigem(String valorConsulta, String situacaoMatriculado, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, TipoTransferenciaEntrada tipoTransferenciaEntrada, UsuarioVO usuario) throws Exception;

	public List<TransferenciaEntradaVO> consultarPorTipoJustificativa(String valorConsulta, String situacaoMatriculado, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	public void setIdEntidade(String aIdEntidade);

	public TransferenciaEntradaVO montarDadosTransferenciaInternaPeloCodigoRequerimento(Integer codigo, Integer codigo2, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, Boolean possuiPermissaoTransferirInternamenteMesmoCursoMatriculaIntegral, UsuarioVO usuario) throws Exception;

	public void adicionarCursoTransferenciaInterna(TransferenciaEntradaVO transferenciaEntradaVO, UnidadeEnsinoCursoVO obj, Boolean possuiPermissaoTransferirInternamenteMesmoCursoMatriculaIntegral) throws Exception;

	public List<TransferenciaEntradaVO> consultarTodosTiposPorData(Date datainicio, Date dataFim, String unidadeEnsino, boolean b, int nivelmontardadosDadosconsulta, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	public List<TransferenciaEntradaVO> consultarPorDataUnidadeEnsino(Date datainicio, Date dataFim, String unidadeEnsino, boolean matriculado, TipoTransferenciaEntrada tipoTransferenciaEntrada, boolean b, int nivelmontardadosDadosconsulta, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	public void executarEfetivacaoSituacaoTransferenciaEntrada(TransferenciaEntradaVO transferenciaEntradaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	public List<TransferenciaEntradaDisciplinasAproveitadasVO> buscarDisciplinaSeremAproveitadaTransferenciaInterna(Integer aluno, Integer curso, Integer gradeCurricular, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	public void montarListaDisciplinasAproveitadas(TransferenciaEntradaVO transferenciaEntrada, UsuarioVO usuario) throws Exception;

	public void alterarSituacaoRequerimento(TransferenciaEntradaVO obj, UsuarioVO usuarioVO) throws Exception;

	public void verificarTransferenciaEntrada(TransferenciaEntradaVO transferenciaEntrada, String situacao, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	public TransferenciaEntradaVO consultarPorCodigoRequerimento(Integer codigoPrm, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	public void adicionarDisciplinaSeremAproveitadaTransferenciaInterna(List<HistoricoVO> historicoVOs, List<TransferenciaEntradaDisciplinasAproveitadasVO> transferenciaEntradaDisciplinasAproveitadasVOs, Integer curso, Integer gradeCurricular, UsuarioVO usuario) throws Exception;

	public String consultarInstituicaoOrigemCursoOrigemPorMatricula(String matricula) throws Exception;

	List<TransferenciaEntradaVO> consultarPorMatriculaETipo(String valorConsulta, String situacaoMatriculado, TipoTransferenciaEntrada tipoTransferenciaEntrada, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	public String imprimirDeclaracaoTransferenciaInterna(TransferenciaEntradaVO transferenciaEntradaVO, TextoPadraoDeclaracaoVO textoPadraoDeclaracao, MatriculaVO matriculaVO, ConfiguracaoGeralSistemaVO config, UsuarioVO usuario) throws Exception;

	boolean executarVerificarTransferenciaEntradaVinculadaMatricula(Integer transferenciaEntrada, UsuarioVO usuarioVO) throws Exception;

	void vincularMatriculaTransferenciaEntrada(TransferenciaEntradaVO obj, UsuarioVO usuario) throws Exception;

	/**
	 * @author Wellington Rodrigues - 08/04/2015
	 * @param transferenciaEntrada
	 * @param verificarAcesso
	 * @param usuarioVO
	 * @throws Exception
	 */
	void removerVinculoTransferenciaEntradaMatricula(Integer transferenciaEntrada, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;
	
	TransferenciaEntradaVO consultarPorCodigoParaHistoricoAluno(Integer codigo) throws Exception;

	void realizarEstornoTransferenciaInterna(TransferenciaEntradaVO transferenciaEntradaVO,
			ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception;
	
	void realizarVinculoTransferenciaInternaMatriculaExistente(TransferenciaEntradaVO transferenciaEntrada,
			MatriculaVO matriculaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception;

	void alteraData(TransferenciaEntradaVO transferenciaEntradaVO, UsuarioVO usuarioVO) throws Exception;
	
	public TransferenciaEntradaVO consultarPorMatriculaESituacao(String matricula, String situacao, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;
	
	void validarDadosEnturmacaoAlunoTransferencia(TransferenciaEntradaVO transferenciaEntradaVO, UsuarioVO usuarioVO) throws Exception;
	
	public void criarHistoricoTransferenciaEntrada(TransferenciaEntradaVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	List<HistoricoVO> criarHistoricoTransferenciaEntrada(TransferenciaEntradaVO obj, UsuarioVO usuarioVO, boolean persistirHistorico) throws Exception;

	List<TransferenciaEntradaVO> consultarPorRegistroAcademicoAluno(String valorConsulta, String situacaoMatriculado,TipoTransferenciaEntrada tipoTransferenciaEntrada, boolean controlarAcesso, int nivelMontarDados,
			ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	void realizarCriacaoTranferenciaInternaGerandoNovaMatriculaAproveitandoDisciplinasAprovadasProximoPeriodoLetivoPorRequerimento(RequerimentoVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistema,
			ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, Boolean apresentarVisaoAluno, UsuarioVO usuario) throws Exception;

	void verificarDisciplinasAproveitadasRepetidas(List<TransferenciaEntradaDisciplinasAproveitadasVO> transferenciaEntradaDisciplinasAproveitadasVOs);

	public Boolean verificarCursoOrigemTransferenciaInternaCorrespondeNovoCursoTransferenciaInterna(Integer curso, Integer aluno , String ano , String semestre )throws Exception;

	void removerVinculoTransferenciaEntradaRequerimento(Integer requerimento, UsuarioVO usuarioVO) throws Exception;

	Date consultarDataTransferenciaExternaPorMatricula(String matricula, UsuarioVO usuarioVO) throws Exception;
	
	public Boolean consultarExisteTransferencia(Integer valorConsulta,  TipoTransferenciaEntrada tipoTransferenciaEntrada,  UsuarioVO usuario) throws Exception;
}
