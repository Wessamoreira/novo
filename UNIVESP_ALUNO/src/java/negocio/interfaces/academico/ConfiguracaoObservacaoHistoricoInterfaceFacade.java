package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.ConfiguracaoHistoricoVO;
import negocio.comuns.academico.ConfiguracaoObservacaoHistoricoVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface ConfiguracaoObservacaoHistoricoInterfaceFacade {

	void persistir(ConfiguracaoHistoricoVO configuracaoHistoricoVO, UsuarioVO usuarioVO) throws Exception;
	
	void adicionarConfiguracaoObservacaoHistoricoVO(ConfiguracaoHistoricoVO configuracaoHistoricoVO, ConfiguracaoObservacaoHistoricoVO configuracaoObservacaoHistoricoVO, UsuarioVO usuarioVO) throws Exception;
	
	List<ConfiguracaoObservacaoHistoricoVO> consultarPorConfiguracaoHistorico(Integer  configuracaoHistorico, UsuarioVO usuarioVO) throws Exception;

	void persistir(ConfiguracaoObservacaoHistoricoVO configuracaoObservacaoHistoricoVO, UsuarioVO usuarioVO)
			throws Exception;

	void adicionarConfiguracaoObservacaoHistoricoVO(ConfiguracaoObservacaoHistoricoVO configuracaoObservacaoHistoricoVO,
			ConfiguracaoHistoricoVO configuracaoHistoricoVO, UsuarioVO usuarioVO) throws Exception;

	void removerConfiguracaoObservacaoHistoricoVO(ConfiguracaoObservacaoHistoricoVO configuracaoObservacaoHistoricoVO,
			ConfiguracaoHistoricoVO configuracaoHistoricoVO, UsuarioVO usuarioVO) throws Exception;

	void realizarDefinicaoObservacaoPadraoConfiguracaoObservacaoHistorico(
			ConfiguracaoHistoricoVO configuracaoHistoricoVO,
			ConfiguracaoObservacaoHistoricoVO configuracaoObservacaoHistoricoVO, UsuarioVO usuarioVO) throws Exception;
	
}
