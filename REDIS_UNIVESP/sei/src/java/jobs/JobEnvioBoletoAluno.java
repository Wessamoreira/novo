package jobs;

import java.util.Date;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import controle.arquitetura.SuperControle;
import negocio.comuns.utilitarias.Uteis;

@Component
public class JobEnvioBoletoAluno extends SuperControle implements Runnable {

    private static final long serialVersionUID = -5982402502284567415L;
	private static final long SEGUNDO = 1000;
	private static final long MINUTO = SEGUNDO * 60;
	private static final long HORA = MINUTO * 60;
	private static final long EXECUTAR_MINUTO = MINUTO * 15;
	private static final long EXECUTAR_HORA = HORA * 2;
    
   
    @Scheduled(fixedDelay = EXECUTAR_HORA, initialDelay = EXECUTAR_MINUTO) // initialDelay 15 minuto esperando para rodar e depois fixedDelay roda de 2 em 2 horas caso ja tenha terminado de executar a primeira vez
    public void executarJobEnvioBoletoAluno() {
        try {
        	System.out.println("JobEnvioBoletoAluno iniciou - "+Uteis.getDataComHoraCompleta(new Date()));
        	if(!Uteis.isVersaoDev()) {
        		getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemAlunoComBoletoAnexo();	
        	}
        	System.out.println("JobEnvioBoletoAluno terminou - "+Uteis.getDataComHoraCompleta(new Date()));
        } catch (Exception e) {
        	System.out.println("JobEnvioBoletoAluno erro - "+Uteis.getDataComHoraCompleta(new Date()));
            e.printStackTrace();
        }
    }
    
    @Override
	public void run() {
    	try {
			getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemAlunoComBoletoAnexo();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
    }
}
