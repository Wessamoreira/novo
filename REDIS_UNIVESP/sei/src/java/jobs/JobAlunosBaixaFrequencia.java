package jobs;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import controle.arquitetura.SuperControle;
import negocio.comuns.utilitarias.SpringUtil;

@Component
public class JobAlunosBaixaFrequencia extends SuperControle {
	
	private static final long serialVersionUID = -2773826024294952923L;

	@Scheduled(cron = "0 10 4 1 * ?")
	public void run(){
		 try {
			getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemAlunosBaixaFrequencia();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public JobAlunosBaixaFrequencia getJobAlunosBaixaFrequencia() {
		return SpringUtil.getApplicationContext().getBean(JobAlunosBaixaFrequencia.class);
	}
}
