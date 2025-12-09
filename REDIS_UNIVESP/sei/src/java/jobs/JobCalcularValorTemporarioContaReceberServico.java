package jobs;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import controle.arquitetura.SuperControle;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.utilitarias.Uteis;


@Component
public class JobCalcularValorTemporarioContaReceberServico extends SuperControle {   
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -6392914634907856544L;
	private static final long SEGUNDO = 1000;
	private static final long MINUTO = SEGUNDO * 60;
	private static final long HORA = MINUTO * 60;
	private static final long EXECUTAR_MINUTO = MINUTO * 10;
	private static final long EXECUTAR_HORA = HORA * 1;

	@Scheduled(fixedDelay = EXECUTAR_HORA, initialDelay = EXECUTAR_MINUTO) // initialDelay 10 minuto esperando para rodar e depois fixedDelay roda de 1 em 1 horas caso ja tenha terminado de executar a primeira vez
    public void executarJobCalcularValorTemporarioContaReceberServico() {
        try {
        	System.out.println("JobCalcularValorTemporarioContaReceberServico hora inicio-"+ Uteis.getDataComHoraCompleta(new Date()));
        	if(!Uteis.isVersaoDev()) {
	        	JobCalculoValorTemporarioContaReceber job = new JobCalculoValorTemporarioContaReceber();
				job.setContaReceberVOs(new ArrayList<ContaReceberVO>());
				job.run();
        	}
        	System.out.println("JobCalcularValorTemporarioContaReceberServico hora termino-"+ Uteis.getDataComHoraCompleta(new Date()));
        } catch (Exception e) {
        	System.out.println("JobCalcularValorTemporarioContaReceberServico erro - "+Uteis.getDataComHoraCompleta(new Date()));
            e.printStackTrace();
        }
    }
	
}
