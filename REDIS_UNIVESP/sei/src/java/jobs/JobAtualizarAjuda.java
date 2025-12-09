package jobs;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import controle.arquitetura.SuperControle;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;

@Component
public class JobAtualizarAjuda extends SuperControle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6399636108085272171L;
	public static final String TIME_ZONE = "America/Sao_Paulo";
	
	@Scheduled(cron="00 00 00 * * ?", zone=TIME_ZONE) // initialDelay 10 minuto esperando para rodar e depois fixedDelay roda de 1 em 1 horas caso ja tenha terminado de executar a primeira vez
	public void run() {
		try {
			UsuarioVO usuarioVO = Uteis.isAtributoPreenchido(getUsuarioLogado()) ? getUsuarioLogado() : getAplicacaoControle().getConfiguracaoGeralSistemaVO(null, null).getUsuarioOperacaoExternas();
			getFacadeFactory().getArtefatoAjudaFacade().realizarSincronizacaoArtefatoAjuda(usuarioVO);
			removerControleMemoriaFlashTela("ArtefatoAjudaControle");
		}catch (Exception e) {

		}
		
	}
}
