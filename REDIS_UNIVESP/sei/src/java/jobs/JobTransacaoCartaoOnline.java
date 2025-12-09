package jobs;

import org.springframework.stereotype.Service;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.TransacaoCartaoOnlineVO;
import negocio.facade.jdbc.arquitetura.SuperFacadeJDBC;


/**
 * @author Victor Hugo 06/04/2016 5.0.4.0
 */
@Service
public class JobTransacaoCartaoOnline extends SuperFacadeJDBC implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TransacaoCartaoOnlineVO transacaoCartaoOnlineVO;
	private UsuarioVO usuarioVO;

	public JobTransacaoCartaoOnline(TransacaoCartaoOnlineVO transacaoCartaoOnlineVO, UsuarioVO usuarioVO) {
		super();
		this.transacaoCartaoOnlineVO = transacaoCartaoOnlineVO;
		this.usuarioVO = usuarioVO;
	}

	@Override
	public void run() {
		try {
			getFacadeFactory().getTransacaoCartaoOnlineInterfaceFacade().incluir(transacaoCartaoOnlineVO, usuarioVO);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
