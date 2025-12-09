package negocio.interfaces.biblioteca;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.biblioteca.CatalogoCursoVO;
import negocio.comuns.biblioteca.CatalogoVO;


public interface CatalogoCursoInterfaceFacade {

	void incluir(CatalogoCursoVO obj) throws Exception;

	void alterarListaCatalogoCursoPorCodigoCatalogo(CatalogoVO catalogo, List<CatalogoCursoVO> objetos) throws Exception;

	void excluirCatalogoCursoCatalogos(Integer catalogo) throws Exception;

	List consultarPorCatalogo(Integer catalogo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

}
