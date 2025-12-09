package jobs;

import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import jobs.enumeradores.JobsEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.job.RegistroExecucaoJobVO;
import negocio.facade.jdbc.arquitetura.ControleAcesso;

public class RemoverSerasaApiGeo extends ControleAcesso implements Job{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7167961772211809209L;

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		try {
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoPadraoSistema();
			List<RegistroExecucaoJobVO> lista = getFacadeFactory().getRegistroExecucaoJobFacade().consultarRegistroExecucaoJobPorNomeOrigemNaoExecutada(JobsEnum.JOB_SERASA_API_GEO_REMOVER);
			executeRemocaoSerasaApiGeo(config, lista);
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	public void executeRemocaoSerasaApiGeo(ConfiguracaoGeralSistemaVO config , List<RegistroExecucaoJobVO> lista) {
		for (RegistroExecucaoJobVO registroExecucaoJobVO : lista) {
			try {
				getFacadeFactory().getRegistroNegativacaoCobrancaContaReceberFacade().executarRemocaoRegistroPorAgenteNegativacaoCobrancaContaReceberPorAPI(registroExecucaoJobVO, config);
				getFacadeFactory().getRegistroExecucaoJobFacade().atualizarCampoDataTerminoRegistroExecucaoJob(JobsEnum.getEnum(registroExecucaoJobVO.getNome()), registroExecucaoJobVO.getCodigoOrigem());
			} catch (Exception e) {
				try {
					getFacadeFactory().getRegistroExecucaoJobFacade().atualizarCampoErroRegistroExecucaoJob(JobsEnum.getEnum(registroExecucaoJobVO.getNome()), registroExecucaoJobVO.getCodigoOrigem(), e.getMessage());	
				} catch (Exception e2) {
					System.out.println("Erro - RemoverSerasaApiGeo -executeRemocaoSerasaApiGeo -"+ e.getMessage()+" - " + e2.getMessage());
				}
			}				
		}
	}

}
