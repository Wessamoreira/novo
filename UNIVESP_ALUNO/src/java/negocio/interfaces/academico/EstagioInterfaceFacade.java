package negocio.interfaces.academico;

import java.util.List;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.DocumentoAssinadoVO;
import negocio.comuns.academico.EstagioVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TransferenciaEntradaVO;
import negocio.comuns.academico.enumeradores.OperacaoDeVinculoEstagioEnum;
import negocio.comuns.academico.enumeradores.TipoEstagioEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.estagio.ConfiguracaoEstagioObrigatorioVO;
import negocio.comuns.estagio.DashboardEstagioVO;
import negocio.comuns.estagio.GrupoPessoaItemVO;
import negocio.comuns.estagio.enumeradores.RegrasSubstituicaoGrupoPessoaItemEnum;
import negocio.comuns.estagio.enumeradores.SituacaoAdicionalEstagioEnum;
import negocio.comuns.estagio.enumeradores.SituacaoEstagioEnum;
import negocio.comuns.protocolo.RequerimentoVO;

public interface EstagioInterfaceFacade {

	
	void realizarCorrecaoBancoDadosPorEstagioCancelado(String codigoEstagios, UsuarioVO usuario) throws Exception;
	
	void processarEstagioSituacaoAguardandoParaRealizando(String codigoEstagios, UsuarioVO usuario) throws Exception;
	
	boolean realizarVerificacaoUsuarioFacilitador(Integer codigoPessoa) throws Exception;
	
	Integer consultarCargaHorariaRealizadaEstagioMatricula(String matricula) throws Exception;
	
	Integer consultarCargaHorariaEmRealizacaoEstagioMatricula(String matricula) throws Exception;
	
	Integer consultarCargaHorariaEstagioFracionadaAproveitamentoOuEquivalencia(String matricula, Integer gradeCurricularEstagio, Integer codigo) throws Exception;
	
	Integer consultarCargaHorariaEstagioFracionada(String matricula, Integer gradeCurricularEstagio, Integer codigo) throws Exception;
	
	boolean realizarVerificacaoEstornoEstagioPorCargaHorario(Integer codigoEstagio, Integer gradeCurricularEstagio, String matricula) throws Exception;
	
	boolean realizarVerificacaoCargaHorarioDeferidaCompleta(String matricula, Integer gradeCurricularEstagio) throws Exception;
	
	boolean realizarVerificacaoSeExisteEstagioAproveitamentoOuEquivalencia(String matricula, Integer gradeCurricularEstagio, TipoEstagioEnum tipoEstagio) throws Exception;
   
	void atualizarEstagioIndeferimento(EstagioVO estagio, UsuarioVO usuarioVO) throws Exception;
	
	void atualizarEstagioAguardandoAssinatura(EstagioVO obj, UsuarioVO usuarioVO) throws Exception;
	
	void atualizarCampoSqlMensagem(EstagioVO estagio, String sqlmensagem, UsuarioVO usuarioVO) throws Exception;
	
	void excluir(EstagioVO obj, boolean verificarAcesso, ConfiguracaoGeralSistemaVO configGeralSistema, UsuarioVO usuarioVO) throws Exception;

	List<EstagioVO> consultarEstagio(EstagioVO estagio, DashboardEstagioVO  dashboardEstagioVO, TipoEstagioEnum tipoEstagio, String ano, String semestre, int nivelMontarDados, DataModelo dataModelo, UsuarioVO usuario) throws Exception;
	
	List<EstagioVO> consultarPorMatriculaAluno(String matricula, SituacaoEstagioEnum situacaoEstagioEnum, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	void executarInicializacaoTotalizadoresDashboardEstagio(DashboardEstagioVO dashboardEstagioVO, EstagioVO estagio, String ano, String semestre, UsuarioVO usuario) throws Exception;
	
	void executarInicializacaoTotalizadoresDashboardEstagioPorMatricula(DashboardEstagioVO  dashboardEstagioVO, String matricula, UsuarioVO usuario) throws Exception;

	void persistir(EstagioVO obj, boolean verificarAcesso, UsuarioVO usuarioVO);
	
	void realizarConfirmacaoEstagioAssinaturaPendente(EstagioVO obj,  boolean verificarAcesso, ConfiguracaoEstagioObrigatorioVO configEstagio, ConfiguracaoGeralSistemaVO configGeralSistema, UsuarioVO usuarioVO) throws Exception;
	
	void realizarAlteracaoEstagioRealizando(DocumentoAssinadoVO documentoAssinado, UsuarioVO usuario) throws Exception;
	
	void realizarConfirmacaoEstagioEmAnalise(EstagioVO estagio, ConfiguracaoEstagioObrigatorioVO configEstagio, ConfiguracaoGeralSistemaVO configGeralSistema, UsuarioVO usuario) throws Exception;
	
	void realizarConfirmacaoEstagioEmCorrecao(EstagioVO estagio, ConfiguracaoEstagioObrigatorioVO configEstagio, UsuarioVO usuario) throws Exception;
	
	void realizarConfirmacaoEstagioIndeferido(EstagioVO estagio, SituacaoAdicionalEstagioEnum situacaoAdicionalEstagioEnum,Boolean indeferirDocumentoAssinado , UsuarioVO usuario) throws Exception;
	
	void realizarConfirmacaoEstagioDeferido(EstagioVO estagio, ConfiguracaoEstagioObrigatorioVO configEstagio, UsuarioVO usuario) throws Exception;
	
	void realizarVerificacaoPersitenciaRelatorioFinalEstagio(EstagioVO obj,  ConfiguracaoEstagioObrigatorioVO configEstagio, ConfiguracaoGeralSistemaVO configGeralSistema, UsuarioVO usuario) throws Exception;
	
	void realizarEstornoFaseEstagio(EstagioVO estagio, ConfiguracaoGeralSistemaVO configGeralSistema, UsuarioVO usuario) throws Exception;
	
	void realizarDistribuicaoGrupoPessoaItemPorInativacao(GrupoPessoaItemVO objInativado, UsuarioVO usuarioVO) throws Exception;

	String realizarVisualizacaoTermoEstagio(EstagioVO obj, ConfiguracaoGeralSistemaVO configGeralSistema, UsuarioVO usuarioVO) throws Exception;
	
	void realizarNovoTermoAssinaturaEstagio(EstagioVO obj, ConfiguracaoEstagioObrigatorioVO configEstagio, ConfiguracaoGeralSistemaVO configGeralSistema, UsuarioVO usuarioVO) throws Exception;

	void validarDados(EstagioVO obj) throws Exception;

	void realizarConfirmacaoEstagioEmAnaliseAprovEquiv(EstagioVO obj, ConfiguracaoEstagioObrigatorioVO configEstagio, ConfiguracaoGeralSistemaVO configGeralSistema, UsuarioVO usuario) throws Exception;

	void carregarUltimoBeneficiarioEstagio(EstagioVO obj);
	
	void executarOperacaoEstagioEmLoteSelecionados(EstagioVO estagio, OperacaoDeVinculoEstagioEnum operacaoDeVinculoEstagioEnum, String motivo, UsuarioVO usuario, ConfiguracaoEstagioObrigatorioVO configEstagio) throws Exception;

	List<EstagioVO> consultaEstagioPeriodoAnaliseAproveitamentoEncerrado(Integer parametro) throws Exception;
	
	void alterarDataLimiteAnaliseNotificacao(final EstagioVO obj, UsuarioVO usuarioVO) throws Exception;
	
	List<EstagioVO> consultaEstagioPeriodoAnaliseEquivalenciaEncerrado(Integer parametro) throws Exception;
	void persistirSubstituicaoGrupoPessoaItem(GrupoPessoaItemVO grupoPessoaItemAtual, GrupoPessoaItemVO grupoPessoaItemEspecifico, RegrasSubstituicaoGrupoPessoaItemEnum regrasSubstituicaoGrupoPessoaItemEnum, EstagioVO estagioVO, boolean inativarGrupoPessoaItemGrupoParticipante, UsuarioVO usuarioVO) throws Exception;

	EstagioVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	EstagioVO consultarPorDocumentoAssinador(Integer codigoDocumentoAssinado,int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<EstagioVO> consultaEstagioPeriodoAnaliseRelatorioFinalEncerrado(Integer parametro) throws Exception;
	
	void realizarProcessamentoNotificacaoDeCancelamentoPorFaltaDeAssinaturaEstagio(ConfiguracaoEstagioObrigatorioVO config, UsuarioVO usuarioOperacaoExterna, EstagioVO estagio) throws Exception;
	
	void alterarDataLimiteCorrecaoNotificacao(final EstagioVO obj, UsuarioVO usuarioVO) throws Exception;
	
	public List<EstagioVO> consultaEstagioCorrecaoRelatorioFinalEncerrado(Integer parametro) throws Exception;
	
	public List<EstagioVO> consultaEstagioAguardandoAssinatura(Integer parametro) throws Exception;
	
	public List<EstagioVO> consultaEstagioParaCancelamentoPorFaltaDeAssinatura(Integer 	qtdDiasMaximoParaAssinaturaEstagio) throws Exception;
	
	public List<EstagioVO> consultaEstagioCorrecaoEquivalenciaEncerrado(Integer parametro) throws Exception;
	
	public List<EstagioVO> consultaEstagioCorrecaoAproveitamentoEncerrado(Integer parametro) throws Exception;

	public void atualizarEmailResponsavelConcedente(EstagioVO obj) throws Exception;

	Boolean consultaSeExisteEstagioParaCancelamentoPorFaltaDeAssinatura(Integer parametro, Integer documentoAssinado) throws Exception;

	List<Integer> consultarFacilitadosInativos() throws Exception;
	
	List<EstagioVO> consultarEstagioPorSituacoesMatricula(List<SituacaoEstagioEnum> listaSituacaoEstagio,String matricula, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

	void realizarRemoverVinculoDoumentoAssinadoEstagio(EstagioVO obj, UsuarioVO usuario) throws Exception;

	void realizarAproveitamentoEstagioDaGradeCurricularAntigaParaNovaGradeCurricularPorTransferenciaEntrada(MatriculaVO matriculaAtual,TransferenciaEntradaVO transferenciaEntrada, String motivoAproveitamento, ConfiguracaoGeralSistemaVO configGeralSistema, UsuarioVO usuarioVO)
			throws Exception;

	void realizarAtualizacaoGrupoPessoaItemEstagioPorRedistribuicaoFacilitadores(Integer codigo,List<Integer> listaCodigosEstagios, boolean controlarAcesso, UsuarioVO usuarioLogado);

	public Integer consultarCargaHorariaCumpridaPorMatriculaEGradeCurricular(String matricula, Integer gradeCurricular, UsuarioVO usuarioVO) throws Exception;

	void realizarAproveitamentoEstagioTransferenciaUnidadeEnsino(MatriculaVO matriculaNova,
			MatriculaVO matriculaAproveitar, UsuarioVO usuario) throws Exception;
	
	public Integer consultarCargaHorariaEstagioRealizadoMatriculaVinculadosOutrasDisciplinas(String matricula, List<Integer> disciplinas) throws Exception;

	public Boolean validarAlunoUtilizaComponenteEstagio(String matricula) throws Exception;
	
	public String realizarVisualizacaoTermoEstagioNaoObrigatorio(RequerimentoVO obj, ConfiguracaoGeralSistemaVO configGeralSistema, UsuarioVO usuarioVO) throws Exception;
}
