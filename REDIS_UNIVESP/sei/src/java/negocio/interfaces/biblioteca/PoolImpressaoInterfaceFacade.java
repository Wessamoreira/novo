package negocio.interfaces.biblioteca;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.biblioteca.ImpressoraVO;
import negocio.comuns.biblioteca.PoolImpressaoVO;
import negocio.comuns.biblioteca.enumeradores.FormatoImpressaoEnum;

public interface PoolImpressaoInterfaceFacade {

	void incluir(final PoolImpressaoVO poolImpressaoVO, final UsuarioVO usuarioVO) throws Exception;
	void excluir(PoolImpressaoVO poolImpressaoVO, UsuarioVO usuarioVO) throws Exception;
	List<PoolImpressaoVO> consultarPorImpressora(ImpressoraVO impressoraVO, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;
	void excluirPorImpressora(ImpressoraVO impressoraVO, UsuarioVO usuarioVO) throws Exception;
	void incluirPoolImpressao(ImpressoraVO impressoraVO, FormatoImpressaoEnum formatoImpressaoEnum,
			String textoImpressao, UsuarioVO usuario) throws Exception;
}
