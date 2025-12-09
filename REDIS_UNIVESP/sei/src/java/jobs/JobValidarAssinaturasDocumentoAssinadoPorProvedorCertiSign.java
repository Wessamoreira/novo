package jobs;

import java.util.Date;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import controle.arquitetura.SuperControle;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;

@Component
public class JobValidarAssinaturasDocumentoAssinadoPorProvedorCertiSign extends SuperControle {
	
	private static final long serialVersionUID = 8743259907143464792L;
	private static final long SEGUNDO = 1000;
	private static final long MINUTO = SEGUNDO * 60;
	private static final long HORA = MINUTO * 60;
	private static final long EXECUTAR_MINUTO = MINUTO * 3;
	private static final long EXECUTAR_DIA =  HORA * 24;
	private static final long EXECUTAR_MES =  EXECUTAR_DIA * 30;
	
	//@Scheduled(fixedDelay = EXECUTAR_DIA, initialDelay = EXECUTAR_MINUTO)
	public void executarJobValidacaoDocumentoAssinadoEnviadosAte30DiasPorProvedorCertiSign() {
		try {
			if (!Uteis.isVersaoDev()) {			
				getFacadeFactory().getDocumentoAssinadoFacade().realizarProcessamentoJobValidacaoDocumentoAssinadoEnviadosPorProvedorCertiSign(Uteis.obterDataPassada( new Date() , 30 ), new Date());
			}
		} catch (ConsistirException e) {
			System.out.println("JobValidarAssinaturasDocumentoAssinadoPorProvedorCertiSign Ate30Dias erro - " + Uteis.getDataComHora(new Date()));
			System.out.println(e.getToStringMensagemErro());	
		} catch (Exception e) {
			System.out.println("JobValidarAssinaturasDocumentoAssinadoPorProvedorCertiSign Ate30Dias  erro - " + Uteis.getDataComHora(new Date()));
			e.printStackTrace();
		}
	}
	
	
	
	
	//@Scheduled(fixedDelay = EXECUTAR_MES, initialDelay = EXECUTAR_MINUTO)
	public void executarJobValidacaoDocumentoAssinadoEnviadosSuperiorA30DiasPorProvedorCertiSign() {
		try {
				if (!Uteis.isVersaoDev()) {
					getFacadeFactory().getDocumentoAssinadoFacade().realizarProcessamentoJobValidacaoDocumentoAssinadoEnviadosPorProvedorCertiSign( null,Uteis.obterDataPassada( new Date() , 30 ));
				}			
			
			} catch (ConsistirException e) {
				System.out.println("JobValidarAssinaturasDocumentoAssinadoPorProvedorCertiSign SuperiorA30Dias erro - " + Uteis.getDataComHora(new Date()));
				System.out.println(e.getToStringMensagemErro());			
			} catch (Exception e) {
				System.out.println("JobValidarAssinaturasDocumentoAssinadoPorProvedorCertiSign SuperiorA30Dias erro - " + Uteis.getDataComHora(new Date()));
				e.printStackTrace();
			}	
			
	}

}
