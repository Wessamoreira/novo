package jobs;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import negocio.facade.jdbc.arquitetura.SuperFacadeJDBC;

@Service
@Lazy
public class JobNotificacaoAniversariante extends SuperFacadeJDBC implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2684202535345333675L;

	@Override
	public void run() {
		 try{
	            getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemAniversarianteDia();
	        }catch (Exception e) {
	            e.printStackTrace();
	        }
		
	}

}
