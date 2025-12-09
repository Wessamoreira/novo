package jobs;

import java.util.Date;

import negocio.comuns.ead.CalendarioAtividadeMatriculaVO;
import negocio.comuns.ead.enumeradores.SituacaoAvaliacaoOnlineMatriculaEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.SuperFacadeJDBC;

public class JobAvaliacaoOnline extends SuperFacadeJDBC implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer codigoCalendario;
	private Long tempoLimite;

	public JobAvaliacaoOnline(Integer codigoCalendario, Long tempoLimite) {
		super();
		this.codigoCalendario = codigoCalendario;
		this.tempoLimite = tempoLimite;
	}

	@Override
	public void run() {
		try {
			if (this.tempoLimite - new Date().getTime() > 0) {
				Thread.sleep(this.tempoLimite - new Date().getTime() + 60000);
			} else {
				Thread.sleep(60000);
			}
//			try {
//				CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO = getFacadeFactory().getCalendarioAtividadeMatriculaInterfaceFacade().consultarPorChavePrimaria(codigoCalendario, Uteis.NIVELMONTARDADOS_TODOS, null);
//				if (calendarioAtividadeMatriculaVO.getAvaliacaoOnlineMatriculaVO().getSituacaoAvaliacaoOnlineMatriculaEnum().equals(SituacaoAvaliacaoOnlineMatriculaEnum.EM_REALIZACAO)) {
//					getFacadeFactory().getAvaliacaoOnlineMatriculaInterfaceFacade().executarCorrecaoAvaliacaoOnline(calendarioAtividadeMatriculaVO.getAvaliacaoOnlineMatriculaVO(), calendarioAtividadeMatriculaVO, false, null, false);
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
