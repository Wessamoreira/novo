package jobs;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.interfaces.administrativo.GestaoEnvioMensagemAutomaticaInterfaceFacade;


public class JobNotificacaoAulaReposicaoDisponivel implements Runnable {
    
    /**
     * 
     */
    
    private Integer horarioTurma = 0;   
    private GestaoEnvioMensagemAutomaticaInterfaceFacade gestaoEnvioMensagemAutomaticaFacade;
    private UsuarioVO usuarioVO = new UsuarioVO();

	public JobNotificacaoAulaReposicaoDisponivel(Integer horarioTurma, GestaoEnvioMensagemAutomaticaInterfaceFacade gestaoEnvioMensagemAutomaticaFacade, UsuarioVO usuarioVO) {
		super();
		this.horarioTurma = horarioTurma;		
		this.gestaoEnvioMensagemAutomaticaFacade = gestaoEnvioMensagemAutomaticaFacade;
		this.usuarioVO = usuarioVO;
	}


	public Integer getHorarioTurma() {
		return horarioTurma;
	}


	public void setHorarioTurma(Integer horarioTurma) {
		this.horarioTurma = horarioTurma;
	}


	@Override
    public void run() {
        try{
            getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemReposicaoAulaDisponivel(getHorarioTurma(), this.usuarioVO);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }


	public GestaoEnvioMensagemAutomaticaInterfaceFacade getGestaoEnvioMensagemAutomaticaFacade() {
		return gestaoEnvioMensagemAutomaticaFacade;
	}


	public void setGestaoEnvioMensagemAutomaticaFacade(GestaoEnvioMensagemAutomaticaInterfaceFacade gestaoEnvioMensagemAutomaticaFacade) {
		this.gestaoEnvioMensagemAutomaticaFacade = gestaoEnvioMensagemAutomaticaFacade;
	}

	
    
    

}
