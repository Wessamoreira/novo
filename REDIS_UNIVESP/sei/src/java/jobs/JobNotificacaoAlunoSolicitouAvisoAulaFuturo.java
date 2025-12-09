package jobs;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import negocio.facade.jdbc.arquitetura.SuperFacadeJDBC;

@Service
@Lazy
public class JobNotificacaoAlunoSolicitouAvisoAulaFuturo extends SuperFacadeJDBC implements Runnable {

    
 
    @Override
    public void run() {
        try{
            getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemAlunoSolicitouAvisoAulaFuturo();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}
