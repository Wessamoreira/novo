package negocio.interfaces.administrativo;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.LogFuncionarioVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface LogFuncionarioInterfaceFacade {

	public void registrarLogFuncionario(FuncionarioVO funcionario, String operacao, UsuarioVO usuario) throws Exception;

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final LogFuncionarioVO obj) throws Exception;

	public LogFuncionarioVO consultarPorChavePrimaria(Integer codigoPrm, UsuarioVO usuario) throws Exception;

}