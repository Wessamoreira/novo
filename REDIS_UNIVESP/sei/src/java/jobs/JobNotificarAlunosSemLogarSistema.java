package jobs;

import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import negocio.facade.jdbc.arquitetura.SuperFacadeJDBC;

@Service
@Lazy
public class JobNotificarAlunosSemLogarSistema extends SuperFacadeJDBC implements Runnable {

	@Override
	public void run() {
		try {
			SqlRowSet rs = getFacadeFactory().getUsuarioFacade().consultarAlunosSemLogarSistema();
			while(rs.next()) {
				getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemNotificacaoDiasAntesConclusaoAtividadeDiscursiva(rs, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
