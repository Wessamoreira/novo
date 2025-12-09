package negocio.interfaces.protocolo;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.protocolo.TipoRequerimentoCursoTransferenciaInternaCursoVO;


/**
 * @author edson
 *
 */
public interface TipoRequerimentoCursoTransferenciaInterfaceFacade {
	

	/**
	 * @param tipoRequerimentoCurso
	 * @param tipoRequerimentoCursoTransInternaVOs
	 * @param usuarioVO
	 * @throws Exception
	 */
	void incluirTipoRequerimentoCursoTransferenciaInternaCursoVOs(Integer tipoRequerimentoCurso, List<TipoRequerimentoCursoTransferenciaInternaCursoVO> tipoRequerimentoCursoTransInternaVOs, UsuarioVO usuarioVO) throws Exception;

	
	/**
	 * @param tipoRequerimentoCurso
	 * @param tipoRequerimentoCursoTransInternaVOs
	 * @param usuarioVO
	 * @throws Exception
	 */
	void alterarTipoRequerimentoCursoTransferenciaInternaCursoVOs(Integer tipoRequerimentoCurso, List<TipoRequerimentoCursoTransferenciaInternaCursoVO> tipoRequerimentoCursoTransInternaVOs, UsuarioVO usuarioVO) throws Exception;

	
	/**
	 * @param tipoRequerimentoCurso
	 * @param usuarioVO
	 * @return
	 * @throws Exception
	 */
	List<TipoRequerimentoCursoTransferenciaInternaCursoVO> consultarTipoRequerimentoCursoTransferenciaInternaCursoPorTipoRequerimentoCurso(Integer tipoRequerimentoCurso, UsuarioVO usuarioVO) throws Exception;

	
	/**
	 * @param tipoRequerimentoCurso
	 * @param tipoRequerimentoCursoTransInternaVOs
	 * @param usuarioVO
	 * @throws Exception
	 */
	void excluirTipoRequerimentoCursoTransferenciaInternaCursoVOs(Integer tipoRequerimentoCurso, List<TipoRequerimentoCursoTransferenciaInternaCursoVO> tipoRequerimentoCursoTransInternaVOs, UsuarioVO usuarioVO) throws Exception;



}
