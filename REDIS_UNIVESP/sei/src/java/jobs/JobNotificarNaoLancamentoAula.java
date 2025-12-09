package jobs;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import negocio.facade.jdbc.arquitetura.SuperFacadeJDBC;

@Service
@Lazy
public class JobNotificarNaoLancamentoAula extends SuperFacadeJDBC implements Runnable {
    
  
    /**
	 * 
	 */
	private static final long serialVersionUID = -6890952795943176052L;

	@Override
    public void run() {
        try{
            getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemNotificacaoNaoLacamentoNota();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}

