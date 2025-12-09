package jobs;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import negocio.facade.jdbc.arquitetura.SuperFacadeJDBC;

@Service
@Lazy
public class JobCriarCampanhaRecorrente extends SuperFacadeJDBC implements Runnable {

	@Override
	public void run() {
		try {
			getFacadeFactory().getCampanhaFacade().executarCriacaoCampanhaRecorrenteCriacaoAgenda();
		} catch (Exception e) {
			//System.out.println("Não foi possível executar a thread de Geração de Campanha/Agenda Recorrente pelo seguinte motivo "+e.getMessage()+" no dia "+Uteis.getDataAtual());
		}

	}

}
