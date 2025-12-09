package jobs;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.job.RegistroExecucaoJobVO;
import negocio.facade.jdbc.arquitetura.SuperFacadeJDBC;

/**
 * @author Victor Hugo de Paula Costa - 18 de mai de 2016
 *
 */
@Service
@Lazy
public class JobRegistroExecucaoJob extends SuperFacadeJDBC implements Runnable {

	private static final long serialVersionUID = 1L;
	
	private RegistroExecucaoJobVO registroExecucaoJobVO;
	private UsuarioVO usuarioVO;

	/**
	 * @author Victor Hugo de Paula Costa - 18 de mai de 2016
	 */
	public JobRegistroExecucaoJob(RegistroExecucaoJobVO registroExecucaoJobVO, UsuarioVO usuarioVO) {
		super();
		this.registroExecucaoJobVO = registroExecucaoJobVO;
		this.usuarioVO = usuarioVO;
	}
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			getFacadeFactory().getRegistroExecucaoJobFacade().incluir(registroExecucaoJobVO);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
