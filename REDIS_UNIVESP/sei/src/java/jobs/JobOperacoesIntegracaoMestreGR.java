package jobs;

import java.util.Date;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import controle.arquitetura.SuperControle;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.job.RegistroExecucaoJobVO;
import negocio.comuns.utilitarias.Uteis;

@Component
public class JobOperacoesIntegracaoMestreGR extends SuperControle {

    /**
	 * 
	 */
	private static final long serialVersionUID = -8530473975217138891L;
	private static final long SEGUNDO = 1000;
    private static final long MINUTO = SEGUNDO * 60;
    private static final long EXECUTAR_A_CADA = MINUTO * 5;

    @Scheduled(fixedDelay = EXECUTAR_A_CADA, initialDelay = EXECUTAR_A_CADA)
    public void executarJobFilaOperacoesTempoRealMestreGR() {
    	RegistroExecucaoJobVO registroExecucaoJobVO = null;
        try {
        	ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoPadraoSistema();
            if (configuracaoGeralSistemaVO.getHabilitarIntegracaoSistemaProvas() 
            		&& configuracaoGeralSistemaVO.getHabilitarOperacoesTempoRealIntegracaoMestreGR() 
            		&& ((Uteis.isAmbienteProducao() && configuracaoGeralSistemaVO.getTokenIntegracaoSistemasProvaMestreGR().equals("R0RiWmlnWlxnR2JaaGlnWlxn")) 
            				|| ((!Uteis.isAmbienteProducao() || Uteis.isVersaoDev()) && configuracaoGeralSistemaVO.getTokenIntegracaoSistemasProvaMestreGR().equals("R0RiWmlnWlxnTWJaaGlnWlxn")))) {
            	registroExecucaoJobVO = new RegistroExecucaoJobVO();
            	registroExecucaoJobVO.setNome("MESTREGR-OPERAÇÕES");
            	registroExecucaoJobVO.setDataInicio(new Date());
                getFacadeFactory().getIntegracaoMestreGRInterfaceFacade().validarOperacoesParaProcessar(registroExecucaoJobVO, configuracaoGeralSistemaVO, new UsuarioVO());
                
            }        
        } catch (Exception e) {
        	 if(registroExecucaoJobVO != null) {
        		 registroExecucaoJobVO.setErro(e.getMessage());
             }
        }finally {
        	if(registroExecucaoJobVO != null) {
        		registroExecucaoJobVO.setDataTermino(new Date());
        		try {
					getFacadeFactory().getRegistroExecucaoJobFacade().incluir(registroExecucaoJobVO);
				} catch (Exception e) {
					e.printStackTrace();
				}
        	}
		}
    }
}
