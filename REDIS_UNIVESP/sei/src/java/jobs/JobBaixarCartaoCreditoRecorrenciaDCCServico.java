package jobs;

import java.util.Date;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import controle.arquitetura.SuperControle;
import negocio.comuns.utilitarias.Uteis;


@Component
public class JobBaixarCartaoCreditoRecorrenciaDCCServico extends SuperControle {   
    
	private static final long serialVersionUID = 1L;
	public static final String TIME_ZONE = "America/Sao_Paulo";
	private static final long SEGUNDO = 1000;
	private static final long MINUTO = SEGUNDO * 60;
	private static final long HORA = MINUTO * 60;
	private static final long EXECUTAR_MINUTO = MINUTO * 5;
	private static final long EXECUTAR_HORA = HORA * 5;

	@Scheduled(fixedDelay = EXECUTAR_HORA, initialDelay = EXECUTAR_MINUTO) // initialDelay 15 minuto esperando para rodar e depois fixedDelay roda de 5 em 5 horas caso ja tenha terminado de executar a primeira vez
//	@Scheduled(cron="0 50 00 * * ?", zone=TIME_ZONE)
    public void executarJobBaixarCartaoCreditoRecorrenciaDCCServico() {
        try {
        	System.out.println("JobBaixarCartaoCreditoRecorrenciaDCCServico hora inicio-"+ Uteis.getDataComHoraCompleta(new Date()));
//        	if(!Uteis.isVersaoDev()) {
	        	JobBaixaCartaoCreditoDCC job = new JobBaixaCartaoCreditoDCC();
				job.run();
//        	}
        	System.out.println("JobBaixarCartaoCreditoRecorrenciaDCCServico hora termino-"+ Uteis.getDataComHoraCompleta(new Date()));
        } catch (Exception e) {
        	System.out.println("JobBaixarCartaoCreditoRecorrenciaDCCServico erro - "+Uteis.getDataComHoraCompleta(new Date()));
            e.printStackTrace();
        }
    }
	
}
