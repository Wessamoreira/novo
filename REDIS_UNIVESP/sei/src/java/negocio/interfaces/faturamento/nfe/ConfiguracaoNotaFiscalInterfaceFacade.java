package negocio.interfaces.faturamento.nfe;

import java.util.List;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.faturamento.nfe.ConfiguracaoNotaFiscalVO;

public interface ConfiguracaoNotaFiscalInterfaceFacade {

	void incluir(final ConfiguracaoNotaFiscalVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception;

	void alterar(ConfiguracaoNotaFiscalVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception;

	List<ConfiguracaoNotaFiscalVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<ConfiguracaoNotaFiscalVO> consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	ConfiguracaoNotaFiscalVO consultarPorChavePrimaria(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	void excluir(ConfiguracaoNotaFiscalVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception;

	ConfiguracaoNotaFiscalVO consultarPorUnidadeEnsino(Integer unidadeEnsino, Boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

	List<ConfiguracaoNotaFiscalVO> consultarConfiguracaoNotaFiscal(Boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

}
