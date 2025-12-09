package jobs;

import java.util.List;

import negocio.comuns.arquitetura.EmailVO;
import negocio.facade.jdbc.arquitetura.ControleAcesso;

public class JobEnviarEmailProspectsSelecionadosPainelGestor extends ControleAcesso implements Runnable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private List<EmailVO> emailVOs;
    
    public JobEnviarEmailProspectsSelecionadosPainelGestor(List<EmailVO> emailVOs) {
    	this.emailVOs = emailVOs;
    }
    
	@Override
	public void run() {
		try {
			for (EmailVO emailVO : emailVOs) {
				getFacadeFactory().getEmailFacade().incluir(emailVO);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
}
