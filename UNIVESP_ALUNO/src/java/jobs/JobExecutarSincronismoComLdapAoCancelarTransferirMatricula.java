package jobs;


import controle.arquitetura.SuperControle;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.arquitetura.UsuarioVO;

public class JobExecutarSincronismoComLdapAoCancelarTransferirMatricula  extends SuperControle implements Runnable {
	
	private UsuarioVO usuarioVO;
	private UsuarioVO usuarioLogado;
	private MatriculaVO matriculaVO;
	private Boolean estorno;
	
	public JobExecutarSincronismoComLdapAoCancelarTransferirMatricula(UsuarioVO usuarioVO, MatriculaVO matriculaVO, Boolean estorno ,UsuarioVO usuarioLogado) {
		this.usuarioVO = usuarioVO;
		this.usuarioLogado = usuarioLogado;
		this.matriculaVO = matriculaVO;
		this.estorno = estorno;
	}
	
	private static final long serialVersionUID = -2773826024294952923L;

	@Override
	public void run(){
		 try {
			 getFacadeFactory().getLdapFacade().executarSincronismoComLdapAoCancelarTransferirMatricula(null, usuarioVO, matriculaVO, null, estorno,usuarioLogado);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
