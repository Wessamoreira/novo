package negocio.interfaces.administrativo;

import java.util.List;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.administrativo.AtendimentoInteracaoDepartamentoVO;
import negocio.comuns.administrativo.AtendimentoVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface AtendimentoInteracaoDepartamentoInterfaceFacade {

	/* (non-Javadoc)
	 * @see negocio.facade.jdbc.administrativo.AtendimentoInteracaoDepartamentoInterfaceFacade#excluirAtendimentoInteracaoDepartamentoVO(java.lang.Integer, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirAtendimentoInteracaoDepartamentoVO(Integer ouvidoria, UsuarioVO usuarioLogado) throws Exception;

	/* (non-Javadoc)
	 * @see negocio.facade.jdbc.administrativo.AtendimentoInteracaoDepartamentoInterfaceFacade#alterarAtendimentoInteracaoDepartamentoVO(java.lang.Integer, java.util.List, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarAtendimentoInteracaoDepartamentoVO(AtendimentoVO ouvidoria, List objetos, UsuarioVO usuarioLogado , Integer codigoRespostaAtendimentoInteracaoSolicitanteVO) throws Exception;

	/* (non-Javadoc)
	 * @see negocio.facade.jdbc.administrativo.AtendimentoInteracaoDepartamentoInterfaceFacade#incluirAtendimentoInteracaoDepartamentoVO(java.lang.Integer, java.util.List, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirAtendimentoInteracaoDepartamentoVO(AtendimentoVO  ouvidoria, List<AtendimentoInteracaoDepartamentoVO> objetos, UsuarioVO usuarioLogado) throws Exception;

	/* (non-Javadoc)
	 * @see negocio.facade.jdbc.administrativo.AtendimentoInteracaoDepartamentoInterfaceFacade#validarDados(negocio.comuns.administrativo.AtendimentoInteracaoDepartamentoVO)
	 */
	public void validarDados(AtendimentoInteracaoDepartamentoVO obj) throws Exception;

	/* (non-Javadoc)
	 * @see negocio.facade.jdbc.administrativo.AtendimentoInteracaoDepartamentoInterfaceFacade#consultarPorCodigoouvidoria(java.lang.Integer, boolean, int, negocio.comuns.arquitetura.UsuarioVO)
	 */
	public List consultarPorCodigoOuvidoria(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/* (non-Javadoc)
	 * @see negocio.facade.jdbc.administrativo.AtendimentoInteracaoDepartamentoInterfaceFacade#consultarPorChavePrimaria(java.lang.Integer, int, negocio.comuns.arquitetura.UsuarioVO)
	 */
	public AtendimentoInteracaoDepartamentoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

}