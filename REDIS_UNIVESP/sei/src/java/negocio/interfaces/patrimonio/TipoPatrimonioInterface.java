package negocio.interfaces.patrimonio;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.patrimonio.TipoPatrimonioVO;

public interface TipoPatrimonioInterface {

	public void persistir(TipoPatrimonioVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	public void excluir(TipoPatrimonioVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	public List<TipoPatrimonioVO> consultar(String valorConsulta, String campoConsulta, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	TipoPatrimonioVO consultarPorChavePrimaria(Integer codigo, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	/**
	 * @author Leonardo Riciolle - 02/06/2015
	 * @param descricao
	 * @param verificarAcesso
	 * @param usuarioVO
	 * @return
	 * @throws Exception
	 */
	List<TipoPatrimonioVO> consultarPorDescricao(String descricao, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

}
