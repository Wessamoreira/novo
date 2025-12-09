package negocio.interfaces.basico;

import java.util.List;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.LayoutEtiquetaTagVO;

public interface LayoutEtiquetaTagInterfaceFacade {

	void excluir(LayoutEtiquetaTagVO obj, UsuarioVO usuarioLogado) throws Exception;

	void incluir(LayoutEtiquetaTagVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioLogado) throws Exception;

	void alterar(LayoutEtiquetaTagVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioLogado) throws Exception;

	void incluirLayoutEtiquetaItens(Integer LayoutEtiquetaPrm, List<LayoutEtiquetaTagVO> objetos, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioLogado) throws Exception;

	LayoutEtiquetaTagVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioLogado) throws Exception;

	void validarDados(LayoutEtiquetaTagVO obj) throws Exception;

	List<LayoutEtiquetaTagVO> consultarLayoutEtiquetaTagItens(Integer layoutEtiqueta, int nivelMontarDados, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioLogado) throws Exception;

	void alterarLayoutEtiquetaItens(Integer layoutEtiqueta, List<LayoutEtiquetaTagVO> objetos, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioLogado) throws Exception;

	void excluirLayoutEtiquetaTagItens(Integer LayoutEtiqueta, UsuarioVO usuarioLogado) throws Exception;

	/**
	 * @author Rodrigo Wind - 14/03/2016
	 * @param listaLayoutEtiquetaTagVOs
	 * @return
	 * @throws Exception
	 */
	List<LayoutEtiquetaTagVO> realizarCloneListaOriginalLayout(List<LayoutEtiquetaTagVO> listaLayoutEtiquetaTagVOs) throws Exception;

}
