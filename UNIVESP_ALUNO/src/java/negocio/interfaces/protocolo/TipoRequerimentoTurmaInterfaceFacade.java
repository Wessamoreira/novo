package negocio.interfaces.protocolo;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.protocolo.TipoRequerimentoTurmaVO;

public interface TipoRequerimentoTurmaInterfaceFacade {

	/**
	 * @author Carlos Eugênio - 28/09/2016
	 * @param tipoRequerimentoCurso
	 * @param tipoRequerimentoTurmaVOs
	 * @param usuarioVO
	 * @throws Exception
	 */
	void incluirTipoRequerimentoTurmaVOs(Integer tipoRequerimentoCurso, List<TipoRequerimentoTurmaVO> tipoRequerimentoTurmaVOs, UsuarioVO usuarioVO) throws Exception;

	/**
	 * @author Carlos Eugênio - 28/09/2016
	 * @param tipoRequerimentoCurso
	 * @param tipoRequerimentoTurmaVOs
	 * @param usuarioVO
	 * @throws Exception
	 */
	void alterarTipoRequerimentoTurmaVOs(Integer tipoRequerimentoCurso, List<TipoRequerimentoTurmaVO> tipoRequerimentoTurmaVOs, UsuarioVO usuarioVO) throws Exception;

	/**
	 * @author Carlos Eugênio - 28/09/2016
	 * @param tipoRequerimentoCurso
	 * @param usuarioVO
	 * @return
	 * @throws Exception
	 */
	List<TipoRequerimentoTurmaVO> consultarTipoRequerimentoTurmaPorTipoRequerimentoCurso(Integer tipoRequerimentoCurso, UsuarioVO usuarioVO) throws Exception;

	/**
	 * @author Carlos Eugênio - 28/09/2016
	 * @param tipoRequerimentoCurso
	 * @param tipoRequerimentoTurmaVOs
	 * @param usuarioVO
	 * @throws Exception
	 */
	void excluirTipoRequerimentoTurmaVOs(Integer tipoRequerimentoCurso, List<TipoRequerimentoTurmaVO> tipoRequerimentoTurmaVOs, UsuarioVO usuarioVO) throws Exception;


}
