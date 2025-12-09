package jobs;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import negocio.facade.jdbc.arquitetura.SuperFacadeJDBC;

@Service
@Lazy
public class JobNotificarAlunoInadimplente extends SuperFacadeJDBC implements Runnable {

    
    /**
     * 
     */
    private static final long serialVersionUID = -6424329078909514399L;

    @Override
    public void run() {
        try{
            getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemCobrancaAlunoInadimplenteSegundoConfiguracaoFinanceira();
            //realizarBloqueioMatriculaAlunoInadimplente();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}
