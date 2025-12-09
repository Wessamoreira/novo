package jobs;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import negocio.facade.jdbc.arquitetura.SuperFacadeJDBC;

@Service
@Lazy
public class JobEmailFollowMe extends SuperFacadeJDBC implements Runnable {
    
    /**
     * 
     */
    private static final long serialVersionUID = -3607942860827285329L;

    

    @Override
    public void run() {
        try{
            getFacadeFactory().getFollowMeFacade().realizarEnvioDadosFollowMe();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
