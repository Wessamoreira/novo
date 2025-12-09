package negocio.interfaces.academico;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.LogInclusaoExclusaoVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface LogInclusaoExclusaoInterfaceFacade {

	public void realizarRegistroLogInclusaoExclusao(String dados, UsuarioVO usuario) throws Exception;

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final LogInclusaoExclusaoVO obj) throws Exception;

	public LogInclusaoExclusaoVO consultarPorChavePrimaria(Integer codigoPrm, UsuarioVO usuario) throws Exception;

}