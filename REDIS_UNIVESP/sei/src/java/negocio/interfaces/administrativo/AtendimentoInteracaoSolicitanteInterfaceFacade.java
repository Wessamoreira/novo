package negocio.interfaces.administrativo;

import java.util.List;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.administrativo.AtendimentoInteracaoSolicitanteVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface AtendimentoInteracaoSolicitanteInterfaceFacade {

	/* (non-Javadoc)
	 * @see negocio.facade.jdbc.administrativo.AtendimentoInteracaoSolicitanteInterfaceFacade#excluirAtendimentoInteracaoSolicitanteVO(java.lang.Integer, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirAtendimentoInteracaoSolicitanteVO(Integer ouvidoria, UsuarioVO usuarioLogado) throws Exception;

	/* (non-Javadoc)
	 * @see negocio.facade.jdbc.administrativo.AtendimentoInteracaoSolicitanteInterfaceFacade#alterarAtendimentoInteracaoSolicitanteVO(java.lang.Integer, java.util.List, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarAtendimentoInteracaoSolicitanteVO(Integer ouvidoria, List objetos, UsuarioVO usuarioLogado) throws Exception;

	/* (non-Javadoc)
	 * @see negocio.facade.jdbc.administrativo.AtendimentoInteracaoSolicitanteInterfaceFacade#incluirAtendimentoInteracaoSolicitanteVO(java.lang.Integer, java.util.List, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirAtendimentoInteracaoSolicitanteVO(Integer ouvidoria, List<AtendimentoInteracaoSolicitanteVO> objetos, UsuarioVO usuarioLogado) throws Exception;

	

	/* (non-Javadoc)
	 * @see negocio.facade.jdbc.administrativo.AtendimentoInteracaoSolicitanteInterfaceFacade#consultarPorCodigoOuvidoria(java.lang.Integer, boolean, int, negocio.comuns.arquitetura.UsuarioVO)
	 */
	public List consultarPorCodigoOuvidoria(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/* (non-Javadoc)
	 * @see negocio.facade.jdbc.administrativo.AtendimentoInteracaoSolicitanteInterfaceFacade#consultarPorChavePrimaria(java.lang.Integer, int, negocio.comuns.arquitetura.UsuarioVO)
	 */
	public AtendimentoInteracaoSolicitanteVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	void validarDados(AtendimentoInteracaoSolicitanteVO obj) throws Exception;

}