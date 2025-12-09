package negocio.interfaces.administrativo;

import java.util.List;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.administrativo.FuncionarioGrupoDestinatariosVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface FuncionarioGrupoDestinatariosInterfaceFacade {

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public abstract void incluir(final FuncionarioGrupoDestinatariosVO obj) throws Exception;

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public abstract void excluir(FuncionarioGrupoDestinatariosVO obj) throws Exception;

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public abstract void excluirGrupoDestinatarios(List<FuncionarioGrupoDestinatariosVO> listaFuncionarioGrupoDestinatariosVOs) throws Exception;

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public abstract void alterarGrupoDestinatarioss(Integer codigoGrupo, List<FuncionarioGrupoDestinatariosVO> FuncionarioGrupoDestinatariosVOs) throws Exception;

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public abstract void incluirListaFuncionarioGrupoDestinatarios(Integer codigo, List<FuncionarioGrupoDestinatariosVO> listaFuncionarioGrupoDestinatarios) throws Exception;
	
	public abstract List<FuncionarioGrupoDestinatariosVO> consultarPorCodigoGrupoDestinatarios(Integer codigoGrupo, int nivelMontarDados, UsuarioVO usuario) throws Exception;

}