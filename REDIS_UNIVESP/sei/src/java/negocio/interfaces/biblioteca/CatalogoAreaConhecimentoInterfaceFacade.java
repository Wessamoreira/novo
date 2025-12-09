package negocio.interfaces.biblioteca;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.biblioteca.CatalogoAreaConhecimentoVO;
import negocio.comuns.biblioteca.CatalogoVO;

public interface CatalogoAreaConhecimentoInterfaceFacade {
	
	public CatalogoAreaConhecimentoVO novo() throws Exception;
	public void incluir(final CatalogoAreaConhecimentoVO obj) throws Exception;
	public void alterar(final CatalogoAreaConhecimentoVO obj) throws Exception;
	public void excluir(CatalogoAreaConhecimentoVO obj) throws Exception;
	public void excluirCatalogoCursoCatalogos(Integer catalogo) throws Exception;
	public void alterarListaCatalogoCursoPorCodigoCatalogo(CatalogoVO catalogo, List<CatalogoAreaConhecimentoVO> objetos) throws Exception;
	public List consultarPorCatalogo(Integer catalogo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	public CatalogoAreaConhecimentoVO consultarPorCodigo(Integer codigo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

}
