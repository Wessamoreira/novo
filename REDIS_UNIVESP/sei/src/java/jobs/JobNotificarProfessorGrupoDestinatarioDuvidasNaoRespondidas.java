package jobs;

import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import negocio.facade.jdbc.arquitetura.SuperFacadeJDBC;

@Service
@Lazy
public class JobNotificarProfessorGrupoDestinatarioDuvidasNaoRespondidas extends SuperFacadeJDBC implements Runnable {

	@Override
	public void run() {
		try {
			SqlRowSet rs = getFacadeFactory().getDuvidaProfessorFacade().consultarProfessorQueTemDuvidasAResponder(null);
			
			while(rs.next()) {
				getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemNotificacaoDuvidasNaoRespondidasNoPrazoProfessor(rs, null);
			}
			
			SqlRowSet rs2 = getFacadeFactory().getGrupoDestinatariosFacade().consultarGrupoDestinatariosNotificacaoDuvidasNaoRespondidasProfessor();
			
			while(rs2.next()) {
				getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemNotificacaoGrupoDestinatarioDuvidasNaoRespondidasNoPrazoProfessor(rs2, null);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
}
