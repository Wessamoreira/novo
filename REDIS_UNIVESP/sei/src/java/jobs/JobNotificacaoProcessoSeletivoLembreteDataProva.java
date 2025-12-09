package jobs;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import negocio.facade.jdbc.arquitetura.SuperFacadeJDBC;

@Service
@Lazy
public class JobNotificacaoProcessoSeletivoLembreteDataProva extends SuperFacadeJDBC implements Runnable {

	private static final long serialVersionUID = 1L;

	@Override
	public void run() {
		try {
			getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemNotificacaoProcessoSeletivoLembreteDataProva();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
