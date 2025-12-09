package jobs;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import negocio.facade.jdbc.arquitetura.SuperFacadeJDBC;

@Service
@Lazy
public class JobNotificarCandidatoCodigoAutenticacaoProva extends SuperFacadeJDBC implements Runnable {
	

    /**
	 * 
	 */
	private static final long serialVersionUID = -1465638695992931795L;

	@Override
    public void run() {
        try{
            getFacadeFactory().getInscricaoFacade().enviarCodigosAutenticacaoProvaOnlineDiaAtual(false);            
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
