package jobs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jobs.enumeradores.JobsEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.AgenteNegativacaoCobrancaContaReceberVO;
import negocio.comuns.job.RegistroExecucaoJobVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;

public class ThreadSerasaApiGeoProcessamentoInterrompido extends ControleAcesso implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2232187751305808987L;

	@Override
	public void run() {
		try {
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoPadraoSistema();
			List<RegistroExecucaoJobVO> registroExecucaoJobVOs = getFacadeFactory().getRegistroExecucaoJobFacade().consultarRegistroExecucaoJobPorNomeOrigemNaoExecutada(JobsEnum.JOB_SERASA_API_GEO_REGISTRAR);
			executarJob(config, registroExecucaoJobVOs, JobsEnum.JOB_SERASA_API_GEO_REGISTRAR);
			registroExecucaoJobVOs.clear();
			registroExecucaoJobVOs = getFacadeFactory().getRegistroExecucaoJobFacade().consultarRegistroExecucaoJobPorNomeOrigemNaoExecutada(JobsEnum.JOB_SERASA_API_GEO_REMOVER);		
			executarJob(config, registroExecucaoJobVOs, JobsEnum.JOB_SERASA_API_GEO_REMOVER);
		} catch (Exception e) {
			System.out.println("Erro - ThreadSerasaApiGeoProcessamentoInterrompido -" + e.getMessage()+"- dia"+ Uteis.getDataComHora(new Date()));
			e.printStackTrace();
		}

	}

	private void executarJob(ConfiguracaoGeralSistemaVO config, List<RegistroExecucaoJobVO> registroExecucaoJobVOs, JobsEnum jobsEnum) {
		try {
			if (jobsEnum.equals(JobsEnum.JOB_SERASA_API_GEO_REGISTRAR)) {
				for (RegistroExecucaoJobVO registroExecucaoJobVO : registroExecucaoJobVOs) {
					AgenteNegativacaoCobrancaContaReceberVO anccr = getFacadeFactory().getAgenteNegativacaoCobrancaContaReceberFacade().consultarPorChavePrimaria(registroExecucaoJobVO.getCodigoOrigem(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, new UsuarioVO());
					Uteis.checkState((!Uteis.isAmbienteProducao() || Uteis.isVersaoDev()) && anccr.getAmbienteAgenteNegativacaoCobranca().isProducao(), "O SEI está em um ambiente diferente de Produção e por isso não é possível executar  a Integração do Agente de Negativação para ambiente de Produção.");
					if(anccr.getIntegracaoSerasaApiGeo() && anccr.getRegistrarAutomaticamenteContasNegativacao() &&  Integer.valueOf(anccr.getHoraExecucaoRotinaRegistrarContasNegativacao().getValor()) <= Uteis.getHoraMinutoSegundo(new Date(), "hora")) {
						RegistrarSerasaApiGeo registrarSerasaApiGeo = new RegistrarSerasaApiGeo();
						List<RegistroExecucaoJobVO> listaTemp = new ArrayList<>();
						listaTemp.add(registroExecucaoJobVO);
						registrarSerasaApiGeo.executeRegistarSerasaApiGeo(config, listaTemp);	
					}	
				}
			}else if (jobsEnum.equals(JobsEnum.JOB_SERASA_API_GEO_REMOVER)) {
				for (RegistroExecucaoJobVO registroExecucaoJobVO : registroExecucaoJobVOs) {
					AgenteNegativacaoCobrancaContaReceberVO anccr = getFacadeFactory().getAgenteNegativacaoCobrancaContaReceberFacade().consultarPorChavePrimaria(registroExecucaoJobVO.getCodigoOrigem(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, new UsuarioVO());
					Uteis.checkState((!Uteis.isAmbienteProducao() || Uteis.isVersaoDev()) && anccr.getAmbienteAgenteNegativacaoCobranca().isProducao(), "O SEI está em um ambiente diferente de Produção e por isso não é possível executar  a Integração do Agente de Negativação para ambiente de Produção.");
					if(anccr.getIntegracaoSerasaApiGeo() && anccr.getRemoverAutomaticamenteContasNegativacao() &&  Integer.valueOf(anccr.getHoraExecucaoRotinaRegistrarContasNegativacao().getValor()) <= Uteis.getHoraMinutoSegundo(new Date(), "hora")) {
						RemoverSerasaApiGeo removerSerasaApiGeo = new RemoverSerasaApiGeo();
						List<RegistroExecucaoJobVO> listaTemp = new ArrayList<>();
						listaTemp.add(registroExecucaoJobVO);
						removerSerasaApiGeo.executeRemocaoSerasaApiGeo(config, listaTemp);	
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
