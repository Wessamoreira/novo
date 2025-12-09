package negocio.interfaces.biblioteca;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.biblioteca.ArquivoMarc21CatalogoVO;

public interface ArquivoMarc21CatalogoInterfaceFacade {
	
	public void incluir(final ArquivoMarc21CatalogoVO obj, UsuarioVO usuarioVO) throws Exception;
	public List<ArquivoMarc21CatalogoVO> consultarPorCodigoArquivoMarc21(Integer codigoArquivoMarc21, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;
	
}
