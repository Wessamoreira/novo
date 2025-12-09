package jobs;

import java.util.Date;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import controle.arquitetura.SuperControle;
import negocio.comuns.utilitarias.Uteis;

@Component
public class JobWebhookPix extends SuperControle {
	
	private static final long serialVersionUID = 8743259907143464792L;
	private static final long SEGUNDO = 1000;
	private static final long MINUTO = SEGUNDO * 60;
	private static final long EXECUTAR_MINUTO = MINUTO * 3;
	private static final long EXECUTAR_HORA =  MINUTO * 3;
	
	@Scheduled(fixedDelay = EXECUTAR_HORA, initialDelay = EXECUTAR_MINUTO) 
	public void executarJobBancoBrasilPix() {
		try {
			if (!Uteis.isVersaoDev()) {
				getFacadeFactory().getPixContaCorrenteFacade().realizarProcessamentoJobWebhookPix();
			}
		} catch (Exception e) {
			System.out.println("JobWebhookPix erro - " + Uteis.getDataComHora(new Date()));
			e.printStackTrace();
		}
	}

}
