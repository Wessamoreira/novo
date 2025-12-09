package negocio.interfaces.biblioteca;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.biblioteca.BibliotecaVO;
import negocio.comuns.biblioteca.ConfiguracaoBibliotecaNivelEducacionalVO;


public interface ConfiguracaoBibliotecaNivelEducacionalInterface {
	
	public void incluir(final ConfiguracaoBibliotecaNivelEducacionalVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;
	public void alterar(final ConfiguracaoBibliotecaNivelEducacionalVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;
	public void incluirConfiguracaoBibliotecaNivelEducacionalVOs(BibliotecaVO bibliotecaVO, List<ConfiguracaoBibliotecaNivelEducacionalVO> configuracaoBibliotecaNivelEducacionalVOs, UsuarioVO usuario) throws Exception;
	public void alterarConfiguracaoBibliotecaNivelEducacionalVOs(BibliotecaVO bibliotecaVO, List<ConfiguracaoBibliotecaNivelEducacionalVO> configuracaoBibliotecaNivelEducacionalVOs, UsuarioVO usuario) throws Exception;
	public void excluirConfiguracaoBibliotecaNivelEducacionalVOs(BibliotecaVO bibliotecaVO, UsuarioVO usuario) throws Exception;
	public void executarAdicionarConfiguracaoBibliotecaNivelEducacional(ConfiguracaoBibliotecaNivelEducacionalVO obj, List<ConfiguracaoBibliotecaNivelEducacionalVO> configuracaoBibliotecaNivelEducacionalVOs, UsuarioVO usuarioVO) throws Exception;
	public void executarRemoverConfiguracaoBibliotecaNivelEducacional(ConfiguracaoBibliotecaNivelEducacionalVO obj, List<ConfiguracaoBibliotecaNivelEducacionalVO> configuracaoBibliotecaNivelEducacionalVOs, UsuarioVO usuarioVO) throws Exception;
	public List<ConfiguracaoBibliotecaNivelEducacionalVO> consultarPorBiblioteca(Integer biblioteca, int nivelMontarDados,UsuarioVO usuarioVO) throws Exception;

}