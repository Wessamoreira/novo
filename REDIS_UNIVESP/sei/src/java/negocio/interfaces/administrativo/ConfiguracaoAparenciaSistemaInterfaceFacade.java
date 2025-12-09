package negocio.interfaces.administrativo;

import java.util.List;

import org.richfaces.event.FileUploadEvent;

import controle.administrativo.ConfiguracaoAparenciaSistemaVO;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

public interface ConfiguracaoAparenciaSistemaInterfaceFacade {

	public void persistir(ConfiguracaoAparenciaSistemaVO configuracaoAparenciaSistemaVO, UsuarioVO usuarioVO) throws Exception;
	
	public void excluir(ConfiguracaoAparenciaSistemaVO configuracaoAparenciaSistemaVO, UsuarioVO usuarioVO) throws Exception;
	
	ConfiguracaoAparenciaSistemaVO consultarConfiguracaoAparenciaSistema(boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;

	void adicionarBanner(ConfiguracaoAparenciaSistemaVO configuracaoAparenciaSistemaVO, FileUploadEvent upload,
			UsuarioVO usuarioVO) throws Exception;

	void removerBanner(ConfiguracaoAparenciaSistemaVO configuracaoAparenciaSistemaVO, ArquivoVO arquivoVO,
			UsuarioVO usuarioVO) throws Exception;

	void realizarGeracaoScriptBannerLogin(ConfiguracaoAparenciaSistemaVO configuracaoAparenciaSistemaVO)
			throws Exception;

	void realizarGeracaoScriptCss(ConfiguracaoAparenciaSistemaVO configuracaoAparenciaSistemaVO);

	void realizarDefinicaoPadraoSistema(ConfiguracaoAparenciaSistemaVO configuracaoAparenciaSistemaVO);

	List<ConfiguracaoAparenciaSistemaVO> consultar(String campoConsulta, String nome, NivelMontarDados nivelMontarDados, boolean validarAcesso, UsuarioVO usuarioVO)
			throws Exception;

	ConfiguracaoAparenciaSistemaVO consultarPorChavePrimaria(Integer codigo, boolean validarAcesso, UsuarioVO usuarioVO)
			throws Exception;

	List<ConfiguracaoAparenciaSistemaVO> consultarAparenciaDisponibilizadoUsuario(NivelMontarDados nivelMontarDados, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;

	void uploadLogoTopo(ConfiguracaoAparenciaSistemaVO configuracaoAparenciaSistemaVO, FileUploadEvent upload,
			UsuarioVO usuarioVO) throws Exception;

	void excluirLogoTopo(ConfiguracaoAparenciaSistemaVO configuracaoAparenciaSistemaVO, UsuarioVO usuarioVO)
			throws Exception;
}
