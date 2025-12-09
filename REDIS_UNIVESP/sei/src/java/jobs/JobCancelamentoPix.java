package jobs;

import java.io.Serializable;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import negocio.facade.jdbc.arquitetura.SuperFacadeJDBC;

@Service
@Lazy
public class JobCancelamentoPix extends SuperFacadeJDBC implements Runnable, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8247358473093171081L;

	public void run() {
		try {
			//getFacadeFactory().getPixContaCorrenteFacade().realizarProcessamentoJobCancelamentoPix();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
