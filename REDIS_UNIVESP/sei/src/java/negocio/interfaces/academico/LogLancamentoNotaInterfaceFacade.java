package negocio.interfaces.academico;

import java.util.List;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.LogLancamentoNotaVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface LogLancamentoNotaInterfaceFacade {

	public void realizarRegistroLogLancamentoNota(String dados, UsuarioVO usuario) throws Exception;

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final LogLancamentoNotaVO obj) throws Exception;

	public LogLancamentoNotaVO consultarPorChavePrimaria(Integer codigoPrm, UsuarioVO usuario) throws Exception;
	
	public void registrarLogLancamentoNota(List<HistoricoVO> historicoVOs, String operacao, UsuarioVO usuario) throws Exception;

}