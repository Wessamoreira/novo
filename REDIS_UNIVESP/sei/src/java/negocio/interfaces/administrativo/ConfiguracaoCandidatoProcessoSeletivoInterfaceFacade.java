package negocio.interfaces.administrativo;

import java.util.List;

import negocio.comuns.administrativo.ConfiguracaoCandidatoProcessoSeletivoVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface ConfiguracaoCandidatoProcessoSeletivoInterfaceFacade {

	public ConfiguracaoCandidatoProcessoSeletivoVO novo() throws Exception;

	public void incluir(ConfiguracaoCandidatoProcessoSeletivoVO obj, UsuarioVO usuario) throws Exception;

	public void alterar(ConfiguracaoCandidatoProcessoSeletivoVO obj, UsuarioVO usuario) throws Exception;

	public void excluir(ConfiguracaoCandidatoProcessoSeletivoVO obj, UsuarioVO usuario) throws Exception;

	public ConfiguracaoCandidatoProcessoSeletivoVO consultarPorChavePrimaria(Integer codigo, boolean controlarAcesso,
			int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,
			UsuarioVO usuario) throws Exception;

	public List<ConfiguracaoCandidatoProcessoSeletivoVO> consultar(String valorConsulta, String campoConsulta,
			boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public ConfiguracaoCandidatoProcessoSeletivoVO consultarPorConfiguracaoGeralSistema(Integer codigoPrm,
			boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
}
