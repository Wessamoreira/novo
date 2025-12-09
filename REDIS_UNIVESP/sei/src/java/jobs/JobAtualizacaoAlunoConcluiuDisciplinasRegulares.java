package jobs;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import negocio.facade.jdbc.arquitetura.SuperFacadeJDBC;

@Service
@Lazy
public class JobAtualizacaoAlunoConcluiuDisciplinasRegulares extends SuperFacadeJDBC implements Runnable {

    
    /**
     * 
     */
    private static final long serialVersionUID = -8424329078909514399L;

    @Override
    public void run() {
        try{
            getFacadeFactory().getMatriculaFacade().realizarAtualizarAutomaticaAlunoConcluiuDisciplinasRegulares();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}

