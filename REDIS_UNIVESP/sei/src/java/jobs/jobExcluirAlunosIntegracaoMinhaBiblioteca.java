package jobs;

import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import controle.arquitetura.SuperControle.MSG_TELA;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.SpringUtil;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.SuperFacadeJDBC;

@Service
@Lazy
public class jobExcluirAlunosIntegracaoMinhaBiblioteca  extends SuperFacadeJDBC implements Runnable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Scheduled(fixedRate = 300000) // 5 minutos
	@Override
	public void run() {
		 ConsistirException consistirException = new ConsistirException();
		try {			
			List<UsuarioVO> usuarios = getFacadeFactory().getUsuarioFacade()
					.consultarUsuarioPossuiIntegracaoMinhaBiblioteca(new Date(),null,null);
			getFacadeFactory().getUsuarioFacade().removerUsuarioMinhaBiblioteca(usuarios,consistirException);				
			if(!consistirException.getListaMensagemErro().isEmpty()) {
					throw consistirException;			          			
		    }

		}catch (ConsistirException e) {
			consistirException.getListaMensagemErro().forEach(item->System.out.println(item));
		} catch (Exception e) {
			consistirException.adicionarListaMensagemErro(e.getMessage());
			consistirException.getListaMensagemErro().forEach(item->System.out.println(item));
		}
	}
	public jobExcluirAlunosIntegracaoMinhaBiblioteca getJobExcluirAlunosIntegracaoMinhaBiblioteca() {
		return SpringUtil.getApplicationContext().getBean(jobExcluirAlunosIntegracaoMinhaBiblioteca.class);
	}

	
}
