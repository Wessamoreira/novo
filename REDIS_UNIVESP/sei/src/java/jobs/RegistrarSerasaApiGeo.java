package jobs;

import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import jobs.enumeradores.JobsEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.job.RegistroExecucaoJobVO;
import negocio.facade.jdbc.arquitetura.ControleAcesso;

public class RegistrarSerasaApiGeo extends ControleAcesso implements Job {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9051984125246110514L;

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		try {
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoPadraoSistema();
			List<RegistroExecucaoJobVO> lista = getFacadeFactory().getRegistroExecucaoJobFacade().consultarRegistroExecucaoJobPorNomeOrigemNaoExecutada(JobsEnum.JOB_SERASA_API_GEO_REGISTRAR);
			executeRegistarSerasaApiGeo(config, lista);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void executeRegistarSerasaApiGeo(ConfiguracaoGeralSistemaVO config , List<RegistroExecucaoJobVO> lista) {
		for (RegistroExecucaoJobVO registroExecucaoJobVO : lista) {
			try {
				getFacadeFactory().getRegistroNegativacaoCobrancaContaReceberFacade().executarCriacaoRegistroPorAgenteNegativacaoCobrancaContaReceberPorAPI(registroExecucaoJobVO, config);
				getFacadeFactory().getRegistroExecucaoJobFacade().atualizarCampoDataTerminoRegistroExecucaoJob(JobsEnum.getEnum(registroExecucaoJobVO.getNome()), registroExecucaoJobVO.getCodigoOrigem());
			} catch (Exception e) {
				try {
					getFacadeFactory().getRegistroExecucaoJobFacade().atualizarCampoErroRegistroExecucaoJob(JobsEnum.getEnum(registroExecucaoJobVO.getNome()), registroExecucaoJobVO.getCodigoOrigem(), e.getMessage());	
				} catch (Exception e2) {
					System.out.println("Erro - RegistrarSerasaApiGeo -executeRegistarSerasaApiGeo -"+ e.getMessage()+" - " + e2.getMessage());
				} 
			}				
		}
	}
	
	
		

}
