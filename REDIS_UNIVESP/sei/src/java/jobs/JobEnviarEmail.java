package jobs;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import controle.arquitetura.AplicacaoControle;
import controle.arquitetura.AssuntoDebugEnum;
import negocio.comuns.utilitarias.SpringUtil;
import negocio.comuns.utilitarias.Uteis;

@Component
public class JobEnviarEmail {
	
	
	@Scheduled(fixedRate=300000) // 5 minutos
	public void realizarEnvioEmail() {
		try {
			if (Uteis.isAplicacaoPrincipal()) {
				AplicacaoControle.realizarEscritaTextoDebug(AssuntoDebugEnum.EMAIL, "Iniciando job");
				getJobEnvioEmail().executarThread();
				AplicacaoControle.realizarEscritaTextoDebug(AssuntoDebugEnum.EMAIL, "Concluindo job");
			}
		} catch (Exception e) {
			AplicacaoControle.realizarEscritaErroDebug(AssuntoDebugEnum.EMAIL, e);
		}
	}

	public JobEnvioEmail getJobEnvioEmail() {
		return SpringUtil.getApplicationContext().getBean(JobEnvioEmail.class);
	}

}