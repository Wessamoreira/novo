/**
 * 
 */
package negocio.interfaces.bancocurriculum;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.bancocurriculum.AreaProfissionalParceiroVO;

/**
 * @author Carlos Eugênio
 *
 */
public interface AreaProfissionalParceiroInterfaceFacade {

	/**
	 * @author Carlos Eugênio - 31/05/2016
	 * @param parceiroPrm
	 * @param objetos
	 * @throws Exception
	 */
	void incluirAreaProfissionalParceiroVOs(Integer parceiroPrm, List<AreaProfissionalParceiroVO> objetos) throws Exception;

	/**
	 * @author Carlos Eugênio - 31/05/2016
	 * @param parceiro
	 * @param objetos
	 * @throws Exception
	 */
	void alterarAreaProfissionalParceiro(Integer parceiro, List<AreaProfissionalParceiroVO> objetos) throws Exception;

	/**
	 * @author Carlos Eugênio - 31/05/2016
	 * @param parceiro
	 * @param usuarioVO
	 * @return
	 */
	List<AreaProfissionalParceiroVO> consultarPorParceiro(Integer parceiro, UsuarioVO usuarioVO);

	/**
	 * @author Carlos Eugênio - 31/05/2016
	 * @param listaAreaProfissionalParceiroVOs
	 * @param areaProfissionalParceiroVO
	 */
	void adicionarAreaProfissional(List<AreaProfissionalParceiroVO> listaAreaProfissionalParceiroVOs, AreaProfissionalParceiroVO areaProfissionalParceiroVO);

	/**
	 * @author Carlos Eugênio - 31/05/2016
	 * @param listaAreaProfissionalParceiroVOs
	 * @param areaProfissionalParceiroVO
	 */
	void removerAreaProfissionalParceiro(List<AreaProfissionalParceiroVO> listaAreaProfissionalParceiroVOs, AreaProfissionalParceiroVO areaProfissionalParceiroVO);
	
}
