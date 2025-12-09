package negocio.interfaces.academico;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.LogFechamentoVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface LogFechamentoInterfaceFacade {

	public abstract void realizarRegistroLogFechamento(String matricula) throws Exception;

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public abstract void incluir(final LogFechamentoVO obj) throws Exception;

	public abstract LogFechamentoVO consultarPorChavePrimaria(Integer codigoPrm, UsuarioVO usuario) throws Exception;

}