package negocio.interfaces.protocolo;

import java.util.List;

import negocio.comuns.academico.TurmaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.protocolo.TipoRequerimentoCursoTransferenciaInternaCursoVO;
import negocio.comuns.protocolo.TipoRequerimentoCursoVO;
import negocio.comuns.protocolo.TipoRequerimentoTurmaVO;

public interface TipoRequerimentoCursoInterfaceFacade {

	public void alterarTipoRequerimentoCursoVOs(Integer tipoRequerimento, List<TipoRequerimentoCursoVO> tipoRequerimentoCursoVOs, UsuarioVO usuarioVO) throws Exception;

	public void incluirTipoRequerimentoCursoVOs(Integer tipoRequerimento, List<TipoRequerimentoCursoVO> tipoRequerimentoCursoVOs, UsuarioVO usuarioVO) throws Exception;

	public List<TipoRequerimentoCursoVO> consultarTipoRequerimentoCursoPorTipoRequerimento(Integer tipoRequerimento, UsuarioVO usuarioVO) throws Exception;
	
	/**
	 * @author Carlos Eugênio - 28/09/2016
	 * @param listaTipoRequerimentoCursoVOs
	 * @param turmaVO
	 * @param usuarioVO
	 */
	void removerTipoRequerimentoTurma(List<TipoRequerimentoCursoVO> listaTipoRequerimentoCursoVOs, TurmaVO turmaVO, UsuarioVO usuarioVO);

	/**
	 * @author Carlos Eugênio - 28/09/2016
	 * @param listaTipoRequerimentoCursoVOs
	 * @param tipoRequerimentoTurmaIncluirVO
	 * @param usuarioVO
	 */
	void adicionarTipoRequerimentoTurma(List<TipoRequerimentoCursoVO> listaTipoRequerimentoCursoVOs, TipoRequerimentoTurmaVO tipoRequerimentoTurmaIncluirVO, UsuarioVO usuarioVO);

	void adicionarTipoRequerimentoTransferenciaInternaCursoVOs(
			List<TipoRequerimentoCursoTransferenciaInternaCursoVO> tipoRequerimentoTransferenciaInternaCursoVOs,
			TipoRequerimentoCursoTransferenciaInternaCursoVO obj);

	void removerTipoRequerimentoTransferenciaInternaCursoVOs(
			List<TipoRequerimentoCursoTransferenciaInternaCursoVO> tipoRequerimentoTransferenciaInternaCursoVOs,
			TipoRequerimentoCursoTransferenciaInternaCursoVO obj);

}
