package jobs;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import negocio.facade.jdbc.arquitetura.SuperFacadeJDBC;
/**
 * 
 * @author Marco Túlio
 *
 */
@Service
@Lazy
public class JobNotificarConsultorQuantidadeAgendamentosDia extends SuperFacadeJDBC implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3020034186789070167L;

	@Override
	public void run() {
		try {
			getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioConsultorContatosDia();
		} catch (Exception e) {
			  e.printStackTrace();
		}
		
	}

}
