package jobs;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import controle.arquitetura.SuperControle;


@Component  
public class JobSymplicty  extends SuperControle {
	
	private static final long serialVersionUID = 8247358473093171081L;


	@Scheduled(cron = "0 0 2 * * ?", zone = "America/Sao_Paulo")
    public void executarJobSymplicty() {
        try {
            getFacadeFactory().getIntegracaoSymplictyInterfaceFacade().executarJobSymplicty();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }   
    
}
