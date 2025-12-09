package negocio.interfaces.ead;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.ConfiguracaoEADVO;


public interface ConfiguracaoEADInterfaceFacade {
    
    void persistir(ConfiguracaoEADVO configuracaoEADVO, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;
    
    ConfiguracaoEADVO consultarPorConfiguracao(Integer configuracao) throws  Exception;
    
    ConfiguracaoEADVO consultarPorChavePrimaria(Integer codigo) throws  Exception;

    ConfiguracaoEADVO consultarConfiguracaoEADASerUsada(int nivelMontarDados, UsuarioVO usuario, Integer unidadeEnsino) throws Exception;

    void excluir(ConfiguracaoEADVO configuracaoEADVO, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;

	List<ConfiguracaoEADVO> consultarPorDescricao(String descricao, boolean verificarAcesso, UsuarioVO usuarioVO, int nivelMontarDados) throws Exception;

	void alterarSituacaoConfiguracaoEAD(Integer codigo, String situacao, UsuarioVO usuarioVO) throws Exception;

	ConfiguracaoEADVO clonarConfiguracaoEADVO(ConfiguracaoEADVO configuracaoEADVO) throws Exception;
    
	/** 
	 * @author Victor Hugo de Paula Costa - 14 de jun de 2016 
	 * @param codigoConfiguracaoEADTurma
	 * @return
	 * @throws Exception 
	 */
	List<ConfiguracaoEADVO> consultarConfiguracoesEADAtivasTurma(Integer codigoConfiguracaoEADTurma) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 14 de jun de 2016 
	 * @param codigoTurma
	 * @return
	 * @throws Exception 
	 */
	ConfiguracaoEADVO consultarConfiguracaoEADPorTurma(Integer codigoTurma) throws Exception;
	
	boolean validarConfiguracaoEadPermitirAcessoSemConteudo(Integer codigoTurma) throws Exception;
	
	String consultarVariavelNotaCfgPadraoAvaliacaoOnlineDaConfiguracaoEadPorTurma(Integer codigoTurma) throws Exception;

	boolean validarConfiguracaoEadInformadaParaTurma(Integer codigoTurma) throws Exception;
}
