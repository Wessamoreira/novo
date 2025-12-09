package jobs;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import negocio.facade.jdbc.arquitetura.SuperFacadeJDBC;


@Service
@Lazy
public class JobNotificacaoTCCEmAtraso extends SuperFacadeJDBC implements Runnable {

    /**
     * 
     */
    private static final long serialVersionUID = -5982402502284567415L;

    @Override
    public void run() {
        try{
            getFacadeFactory().getTrabalhoConclusaoCursoFacade().executarEnvioMensagemTCCEmAtraso();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}