package jobs;

import java.util.Date;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import controle.arquitetura.SuperControle;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;

@Component
public class JobAlterarSituacaoMatriculaIntegralizada extends SuperControle {
	
	private static final long serialVersionUID = 8743259907143464792L;
//	private static final long SEGUNDO = 1000;
//	private static final long MINUTO = SEGUNDO * 60;
//	private static final long HORA = MINUTO * 60;
//	private static final long EXECUTAR_MINUTO = MINUTO * 3;
//	private static final long EXECUTAR_DIA =  HORA * 24;
//	private static final long EXECUTAR_MES =  EXECUTAR_DIA * 30;
//	
	public static final String TIME_ZONE = "America/Sao_Paulo";
	
	@Scheduled(cron="00 00 01 * * ?", zone=TIME_ZONE) 
	public void executarJobAlteracaoSituacaoMatriculaIntegralizadaParaFinalizada() {
		try {
			System.out.println("JobAlterarSituacaoMatriculaIntegralizada inicio - " + Uteis.getDataComHora(new Date()));
			if (!Uteis.isVersaoDev()) {
				UsuarioVO usuarioOperacaoExterna = getFacadeFactory().getUsuarioFacade().obterUsuarioResponsavelOperacoesExternas();
				getFacadeFactory().getMatriculaFacade().realizarAlteracaoSituacaoMatriculaIntegralizada(usuarioOperacaoExterna);
			}
			System.out.println("JobAlterarSituacaoMatriculaIntegralizada termino - " + Uteis.getDataComHora(new Date()));
		} catch (Exception e) {
			System.out.println("JobAlterarSituacaoMatriculaIntegralizada erro" + Uteis.getDataComHora(new Date()));
			e.printStackTrace();
		}
	}

}


