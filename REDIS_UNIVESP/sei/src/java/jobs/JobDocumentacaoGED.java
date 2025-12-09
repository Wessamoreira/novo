package jobs;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import controle.arquitetura.AplicacaoControle;
import controle.arquitetura.AssuntoDebugEnum;
import controle.arquitetura.SuperControle;
import negocio.comuns.utilitarias.SpringUtil;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.academico.DocumentacaoGED;

@Component
public class JobDocumentacaoGED extends SuperControle {

	private static final long serialVersionUID = 5561906177399709027L;

	@Scheduled(fixedRate = 300000) // 5 minutos
	public void run() {
		try {
			if (Uteis.isAplicacaoPrincipal()) {
				DocumentacaoGED.processarArquivosDocumentacaoGED(Boolean.FALSE, null);
			}
		} catch (Exception e) {
			AplicacaoControle.realizarEscritaErroDebug(AssuntoDebugEnum.GED, e);
			e.printStackTrace();
		}
	}

	public JobDocumentacaoGED getJobDocumentacaoGED() {
		return SpringUtil.getApplicationContext().getBean(JobDocumentacaoGED.class);
	}

}
