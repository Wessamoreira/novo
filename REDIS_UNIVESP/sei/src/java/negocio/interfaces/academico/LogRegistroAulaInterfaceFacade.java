package negocio.interfaces.academico;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.LogRegistroAulaVO;
import negocio.comuns.academico.RegistroAulaVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface LogRegistroAulaInterfaceFacade {

	public void registrarLogRegistroAula(RegistroAulaVO registroAula, String nomeDisciplina, String operacao, UsuarioVO usuario) throws Exception;

	public void realizarRegistroLogRegistroAula(String dados, UsuarioVO usuario) throws Exception;

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final LogRegistroAulaVO obj) throws Exception;

	public LogRegistroAulaVO consultarPorChavePrimaria(Integer codigoPrm, UsuarioVO usuario) throws Exception;

}