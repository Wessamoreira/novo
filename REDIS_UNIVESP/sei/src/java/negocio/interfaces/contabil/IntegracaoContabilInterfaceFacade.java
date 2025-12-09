package negocio.interfaces.contabil;

import java.util.Date;
import java.util.List;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.contabil.IntegracaoContabilVO;

public interface IntegracaoContabilInterfaceFacade {

	void persistir(IntegracaoContabilVO obj, boolean verificarAcesso, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

	void excluir(IntegracaoContabilVO obj, boolean verificarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

	List<IntegracaoContabilVO> consultaRapidaPorCodigo(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	IntegracaoContabilVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<IntegracaoContabilVO> consultaRapidaPorNomeUnidadeEnsino(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<IntegracaoContabilVO> consultaRapidaPorDataGeracao(Date dataInicial, Date dataFinal, Integer unidadeEnsino,  boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<IntegracaoContabilVO> consultaRapidaPorResponsavel(String valorConsulta, Integer unidadeEnsino,  boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<IntegracaoContabilVO> consultaRapidaPorPeriodoContabil(Date dataInicio, Date dataTermino, Integer unidadeEnsino,  boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<IntegracaoContabilVO> consultaRapidaPorLote(Integer valorConsulta, Integer unidadeEnsino,  boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	void consultarLancamentoContabilDisponivelParaIntegracao(IntegracaoContabilVO obj, UsuarioVO usuarioVO) throws Exception;

}
