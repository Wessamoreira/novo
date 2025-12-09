package negocio.interfaces.financeiro;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.enumeradores.SituacaoEnum;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroCartaoRecebimentoVO;
import negocio.comuns.financeiro.ConfiguracaoRecebimentoCartaoOnlineVO;
import negocio.comuns.financeiro.ContaReceberNegociacaoRecebimentoVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.utilitarias.ConsistirException;

public interface ConfiguracaoRecebimentoCartaoOnlineInterfaceFacade {

	void incluir(ConfiguracaoRecebimentoCartaoOnlineVO configuracaoRecebimentoCartaoOnlineVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void persistir(ConfiguracaoRecebimentoCartaoOnlineVO configuracaoRecebimentoCartaoOnlineVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void alterar(ConfiguracaoRecebimentoCartaoOnlineVO configuracaoRecebimentoCartaoOnlineVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void excluir(ConfiguracaoRecebimentoCartaoOnlineVO configuracaoRecebimentoCartaoOnlineVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	ConfiguracaoRecebimentoCartaoOnlineVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	void alterarSituacaoConfiguracaoRecebimentoCartaoOnlineVOAtivo(ConfiguracaoRecebimentoCartaoOnlineVO configuracaoRecebimentoCartaoOnlineVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void alterarSituacaoConfiguracaoRecebimentoCartaoOnlineVOInativo(ConfiguracaoRecebimentoCartaoOnlineVO configuracaoRecebimentoCartaoOnlineVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	List<ConfiguracaoRecebimentoCartaoOnlineVO> consultar(String campoConsulta, String valorConsulta, SituacaoEnum situacaoEnum, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

	List<ConfiguracaoRecebimentoCartaoOnlineVO> consultarPorDescricao(String valorConsulta, SituacaoEnum situacaoEnum, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	List<ConfiguracaoRecebimentoCartaoOnlineVO> consultarPorUnidadeEnsino(Integer valorConsulta, SituacaoEnum situacaoEnum, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	List<ConfiguracaoRecebimentoCartaoOnlineVO> consultarPorNivelEducacional(String valorConsulta, SituacaoEnum situacaoEnum, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	List<ConfiguracaoRecebimentoCartaoOnlineVO> consultarPorCurso(Integer valorConsulta, SituacaoEnum situacaoEnum, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	List<ConfiguracaoRecebimentoCartaoOnlineVO> consultarPorTurma(String valorConsulta, SituacaoEnum situacaoEnum, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	ConfiguracaoRecebimentoCartaoOnlineVO clonar(ConfiguracaoRecebimentoCartaoOnlineVO configuracaoRecebimentoCartaoOnlineVO) throws Exception;

	void adicionarConfiguracaoFinanceiroCartaoRecebimento(ConfiguracaoFinanceiroCartaoRecebimentoVO configuracaoFinanceiroCartaoRecebimentoVO, ConfiguracaoRecebimentoCartaoOnlineVO configuracaoRecebimentoCartaoOnlineVO, UsuarioVO usuarioVO) throws ConsistirException;

	void removerConfiguracaoFinanceiroCartaoRecebimento(ConfiguracaoFinanceiroCartaoRecebimentoVO configuracaoFinanceiroCartaoRecebimentoVO, ConfiguracaoRecebimentoCartaoOnlineVO configuracaoRecebimentoCartaoOnlineVO, UsuarioVO usuarioVO) throws Exception;

	Boolean verificarUnicidadeCampos(ConfiguracaoRecebimentoCartaoOnlineVO configuracaoRecebimentoCartaoOnlineVO, UsuarioVO usuarioLogado) throws Exception;

	void verificarContasRecebimentoOnline(List<ContaReceberNegociacaoRecebimentoVO> contaReceberNegociacaoRecebimentoVOs, ConfiguracaoRecebimentoCartaoOnlineVO configuracaoRecebimentoCartaoOnlineVO, ConsistirException consistirException, Boolean negociacao, Boolean mensalidadeRenovacaoOnline, Boolean mensalidadeMatriculaOnline, Boolean matriculaRenovacaoOnline, Boolean matriculaOnline, Boolean renegociacaoOnline, UsuarioVO usuarioVO) throws Exception;

	ConfiguracaoRecebimentoCartaoOnlineVO consultarConfiguracaoRecebimentoCartaoOnlineDisponivel(Integer codigoTurma, Integer codigoCurso, String tipoNivelEducacional, Integer codigoUnidadeEnsino, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	Boolean verificarContasRecebimentoOnlineContaReceberVO(ContaReceberVO contaReceberVO, ConfiguracaoRecebimentoCartaoOnlineVO configuracaoRecebimentoCartaoOnlineVO, ConsistirException consistirException, Boolean negociacao, Boolean mensalidadeRenovacaoOnline, Boolean mensalidadeMatriculaOnline, Boolean matriculaRenovacaoOnline, Boolean matriculaOnline, Boolean renegociacaoOnline, UsuarioVO usuarioVO) throws Exception;

//	Boolean realizarVerificacaoRecebimentoOnlinePermitePorParcelasValorAReceber(ConfiguracaoRecebimentoCartaoOnlineVO configuracaoRecebimentoCartaoOnlineVO, List<ContaReceberNegociacaoRecebimentoVO> contaReceberNegociacaoRecebimentoVOs, Double valorAReceber, UsuarioVO usuarioVO) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 12 de mai de 2016 
	 * @param configuracaoRecebimentoCartaoOnlineVO
	 * @param valorAReceber
	 * @param usuarioVO
	 * @return
	 * @throws ConsistirException 
	 */
	Boolean verificarValorAReceberValorMinimo(ConfiguracaoRecebimentoCartaoOnlineVO configuracaoRecebimentoCartaoOnlineVO, Double valorAReceber, UsuarioVO usuarioVO) throws ConsistirException;
	
	public void validarSeExisteConfiguracaoCartaoRecebimento(ConfiguracaoRecebimentoCartaoOnlineVO conf) throws Exception;

	void validarSeExisteConfiguracaoCartaoRecebimentoMatriculaOnline(ConfiguracaoRecebimentoCartaoOnlineVO conf)throws Exception;

	ConfiguracaoRecebimentoCartaoOnlineVO consultarPorChavePrimariaUnica(Integer codigo, int nivelMontarDados,
			UsuarioVO usuarioLogado) throws Exception;

	ConfiguracaoRecebimentoCartaoOnlineVO consultarConfiguracaoRecebimentoCartaoOnlineDisponivel(String matricula, Integer codigoUnidadeEnsino,
			int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;
}
